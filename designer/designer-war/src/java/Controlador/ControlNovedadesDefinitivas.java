/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Empleados;
import Entidades.MotivosRetiros;
import Entidades.MotivosDefinitivas;
import Entidades.NovedadesSistema;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarNovedadesSistemaInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import ControlNavegacion.ControlListaNavegacion;
import java.util.Map;
import java.util.LinkedHashMap;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.primefaces.component.column.Column;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.export.Exporter;
import org.primefaces.context.RequestContext;

/**
 *
 * @author user
 */
@ManagedBean
@SessionScoped
public class ControlNovedadesDefinitivas implements Serializable {

   private static Logger log = Logger.getLogger(ControlNovedadesDefinitivas.class);

    @EJB
    AdministrarNovedadesSistemaInterface administrarNovedadesSistema;
    @EJB
    AdministrarRastrosInterface administrarRastros;
    //SECUENCIA DEL Empleado
    //LISTA NOVEDADES
    private List<NovedadesSistema> listaNovedades;
    private NovedadesSistema novedadMostrar;
    private List<NovedadesSistema> listaBorrar;
    private List<NovedadesSistema> listaCrear;
    private List<NovedadesSistema> listaModificar;
    private List<NovedadesSistema> filtradosListaNovedades;
    //Consultas sobre novedad
    private NovedadesSistema novedadParametro;
    //LISTA DE ARRIBA EMPLEADOS
    private List<Empleados> listaEmpleados;
    private List<Empleados> filtradosListaEmpleadosNovedad;
    private Empleados empleadoSeleccionado; //Seleccion Mostrar
    private Empleados empleadoBack; //Seleccion Mostrar
    //OTROS
    private boolean aceptar;
    //private int index;
    private int tipoActualizacion; //Activo/Desactivo Crtl + F11
    private int bandera;
    private boolean permitirIndex;
    //RASTROS
    private boolean guardado, guardarOk;
    //editar celda
    private NovedadesSistema editarNovedades;
    private boolean cambioEditor, aceptarEditar;
    private int cualCelda, tipoLista;
    //Crear Novedades
    //private List<NovedadesSistema> listaNovedadesCrear;
    public NovedadesSistema nuevaNovedad;
    public NovedadesSistema duplicarNovedad;
    private int k;
    private BigInteger l;
    private String mensajeValidacion;
    //L.O.V Empleados
    private List<Empleados> lovEmpleados;
    private List<Empleados> lovEmpleadosFiltrar;
    private Empleados empleadoSeleccionadoLOV;
    //L.O.V MOTIVOS
    private List<MotivosDefinitivas> lovMotivos;
    private List<MotivosDefinitivas> lovMotivosFiltrar;
    private MotivosDefinitivas seleccionMotivos;
    //L.O.V RETIROS
    private List<MotivosRetiros> lovMotiRetiros;
    private List<MotivosRetiros> lovMotiRetirosFiltrar;
    private MotivosRetiros seleccionRetiros;
    //AUTOCOMPLETAR
    private String motivoDefinitiva, motivoRetiro;
    private String celda;
    //Desactivar Campos
    private Boolean activate;
    //Filtrado
    private String altoTabla;
    private String altoTablaReg;
    private Column nDEmpleadosCodigos, nDEmpleadosNombres;
    //Activar boton mostrar todos
    private boolean activarMostrarTodos;
    private String infoRegistro;
    private String infoRegistroEmpl;
    private String infoRegistroMLiqDef;
    private String infoRegistroMRetiro;
    private int numNovedad;
    private DataTable tablaC;
    private boolean activarNoRango;
    private boolean activarLOV;
    private String paginaAnterior = "nominaf";
    private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

    public ControlNovedadesDefinitivas() {
        paginaAnterior = "nominaf";
        permitirIndex = true;
        lovEmpleados = null;
        listaEmpleados = null;
        permitirIndex = true;
        aceptar = true;
        guardado = true;
        tipoLista = 0;
        listaNovedades = null;
        nuevaNovedad = new NovedadesSistema();
        nuevaNovedad.setMotivodefinitiva(new MotivosDefinitivas());
        nuevaNovedad.setMotivoretiro(new MotivosRetiros());
        nuevaNovedad.setDias(BigInteger.valueOf(0));
        nuevaNovedad.setTipo(" ");
        nuevaNovedad.setSubtipo(" ");
        nuevaNovedad.setFechainicialdisfrute(new Date());
        activarMostrarTodos = true;
        numNovedad = -1;
        activarNoRango = false;
        listaCrear = new ArrayList<NovedadesSistema>();
        listaBorrar = new ArrayList<NovedadesSistema>();
        listaModificar = new ArrayList<NovedadesSistema>();
        empleadoBack = new Empleados();
        activarLOV = true;
        mapParametros.put("paginaAnterior", paginaAnterior);
        altoTabla = "165";
        altoTablaReg = "9";
    }

    public void recibirPaginaEntrante(String pagina) {
        paginaAnterior = pagina;
        listaNovedades = null;
        getListaNovedades();

        getListaEmpleados();
        if (listaEmpleados != null) {
            empleadoSeleccionado = listaEmpleados.get(0);
        }
    }

    public void recibirParametros(Map<String, Object> map) {
        mapParametros = map;
        paginaAnterior = (String) mapParametros.get("paginaAnterior");
        listaNovedades = null;
        getListaNovedades();

        getListaEmpleados();
        if (listaEmpleados != null) {
            empleadoSeleccionado = listaEmpleados.get(0);
        }
    }

    //Reemplazar la funcion volverAtras, retornarPagina, Redirigir.....Atras.etc
    public void navegar(String pag) {
        FacesContext fc = FacesContext.getCurrentInstance();
        ControlListaNavegacion controlListaNavegacion = (ControlListaNavegacion) fc.getApplication().evaluateExpressionGet(fc, "#{controlListaNavegacion}", ControlListaNavegacion.class);
        String pagActual = "novedaddefinitivas";

        if (pag.equals("atras")) {
            pag = paginaAnterior;
            paginaAnterior = "nominaf";
            controlListaNavegacion.quitarPagina(pagActual);
        } else {
            controlListaNavegacion.guardarNavegacion(pagActual, pag);
            fc.getApplication().getNavigationHandler().handleNavigation(fc, null, pag);
//Map<String, Object> mapParaEnviar = new LinkedHashMap<String, Object>();
            //mapParaEnviar.put("paginaAnterior", pagActual);
            //mas Parametros
//         if (pag.equals("rastrotabla")) {
//           ControlRastro controlRastro = (ControlRastro) fc.getApplication().evaluateExpressionGet(fc, "#{controlRastro}", ControlRastro.class);
            //           controlRastro.recibirDatosTabla(conceptoSeleccionado.getSecuencia(), "Conceptos", pagActual);
            //      } else if (pag.equals("rastrotablaH")) {
            //       ControlRastro controlRastro = (ControlRastro) fc.getApplication().evaluateExpressionGet(fc, "#{controlRastro}", ControlRastro.class);
            //     controlRastro.historicosTabla("Conceptos", pagActual);
            //   pag = "rastrotabla";
            //}
        }
        limpiarListasValor();
    }

    public void limpiarListasValor() {
        lovEmpleados = null;
        lovMotiRetiros = null;
        lovMotivos = null;
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarNovedadesSistema.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
            getListaEmpleados();
            if (listaEmpleados != null) {
                if (!listaEmpleados.isEmpty()) {
                    empleadoSeleccionado = listaEmpleados.get(0);
                }
            }

            listaNovedades = null;
            getListaNovedades();

        } catch (Exception e) {
            log.error("Error postconstruct " + this.getClass().getName() + ": " + e);
            log.error("Causa: " + e.getCause());
        }
    }

    public String volverPagAnterior() {
        return paginaAnterior;
    }

    public void asignarIndex(int cualLOV, int tipoAct) {
        RequestContext context = RequestContext.getCurrentInstance();
        tipoActualizacion = tipoAct;

        if (cualLOV == 0) {
            contarRegistrosLovEmpl();
            RequestContext.getCurrentInstance().update("formularioDialogos:empleadosDialogo");
            RequestContext.getCurrentInstance().execute("PF('empleadosDialogo').show()");
        } else if (cualLOV == 1) {
            activarBotonLov();
            contarRegistrosLovMLiqDef();
            RequestContext.getCurrentInstance().update("formularioDialogos:motivosDialogo");
            RequestContext.getCurrentInstance().execute("PF('motivosDialogo').show()");
        } else if (cualLOV == 2) {
            activarBotonLov();
            contarRegistrosLovMRetiro();
            RequestContext.getCurrentInstance().update("formularioDialogos:retirosDialogo");
            RequestContext.getCurrentInstance().execute("PF('retirosDialogo').show()");
        } else {
            deshabilitarBotonLov();
        }
    }

    public void mostrarTodos() {
        RequestContext context = RequestContext.getCurrentInstance();
        listaNovedades = null;
        listaEmpleados.clear();
        for (int i = 0; i < lovEmpleados.size(); i++) {
            listaEmpleados.add(lovEmpleados.get(i));
        }
        empleadoSeleccionado = listaEmpleados.get(0);
        //ACTUALIZAR CADA COMPONENTE
        filtradosListaEmpleadosNovedad = null;
        aceptar = true;
        tipoActualizacion = -1;
        activarMostrarTodos = true;
        contarRegistros();
        RequestContext.getCurrentInstance().update("form:datosEmpleados");
        RequestContext.getCurrentInstance().update("form:formularioNovedades");
    }

//Ubicacion Celda Arriba 
    public void cambiarEmpleado() {
        RequestContext context = RequestContext.getCurrentInstance();//ACTUALIZAR DATOS POR CADA EMPLEADO
        if (guardado) {
            empleadoBack = empleadoSeleccionado;
            listaNovedades = null;
            novedadMostrar = null;
            numNovedad = -1;
            getListaNovedades();
            activarNoRango = false;
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:formularioNovedades");
            RequestContext.getCurrentInstance().update("form:btnMostrarTodos");
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
        } else {
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
        }
        deshabilitarBotonLov();
    }

    public void valoresBackupAutocompletar(String Campo) {
        if (novedadMostrar != null) {
            if (Campo.equals("MOTIVO")) {
                motivoDefinitiva = novedadMostrar.getMotivodefinitiva().getNombre();
            } else if (Campo.equals("RETIRO")) {
                motivoRetiro = novedadMostrar.getMotivoretiro().getNombre();
            }
            activarBotonLov();
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void autocompletarBool(String campoaCambiar, Boolean bool) {
        if (campoaCambiar.equalsIgnoreCase("BOOL")) {
            if (tipoActualizacion == 1) {
                nuevaNovedad.setIndemnizaBool(bool);
            } else if (tipoActualizacion == 2) {
                duplicarNovedad.setIndemnizaBool(bool);
            }
        }
    }

    public void autocompletar(String campoaCambiar, String valor) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;

        if (campoaCambiar.equalsIgnoreCase("MOTIVO")) {
            novedadMostrar.getMotivodefinitiva().setNombre(motivoDefinitiva);

            for (int i = 0; i < lovMotivos.size(); i++) {
                if (lovMotivos.get(i).getNombre().startsWith(valor.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                novedadMostrar.setMotivodefinitiva(lovMotivos.get(indiceUnicoElemento));
                RequestContext.getCurrentInstance().update("form:formularioNovedades:motivoLiquidacion");
                listaModificar.add(novedadMostrar);
            } else {
                contarRegistrosLovMLiqDef();
                RequestContext.getCurrentInstance().update("formularioDialogos:motivosDialogo");
                RequestContext.getCurrentInstance().execute("PF('motivosDialogo').show()");
                RequestContext.getCurrentInstance().update("form:formularioNovedades:motivoLiquidacion");
            }
        } else if (campoaCambiar.equalsIgnoreCase("RETIRO")) {
            novedadMostrar.getMotivoretiro().setNombre(motivoRetiro);
            for (int i = 0; i < lovMotiRetiros.size(); i++) {
                if (lovMotiRetiros.get(i).getNombre().startsWith(valor.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                novedadMostrar.setMotivoretiro(lovMotiRetiros.get(indiceUnicoElemento));
                RequestContext.getCurrentInstance().update("form:formularioNovedades:motivoRetiro");
                listaModificar.add(novedadMostrar);
            } else {
                contarRegistrosLovMRetiro();
                RequestContext.getCurrentInstance().update("formularioDialogos:retirosDialogo");
                RequestContext.getCurrentInstance().execute("PF('retirosDialogo').show()");
                RequestContext.getCurrentInstance().update("form:formularioNovedades:motivoRetiro");
            }
        }
    }

    public void cambiosCampos() {
        RequestContext context = RequestContext.getCurrentInstance();
        int error = 0;
        log.info("cambios campos, lista Novedades : " + listaNovedades.size());
        log.info("cambios campos ,novedad mostrar: " + novedadMostrar.getSecuencia());
        log.info("cambios campos ,novedad mostrar getFechainicialdisfrute: " + novedadMostrar.getFechainicialdisfrute());
        log.info("cambios campos ,nueva novedad getFechainicialdisfrute: " + nuevaNovedad.getFechainicialdisfrute());
        for (int i = 0; i < listaNovedades.size(); i++) {
            if (novedadMostrar.getFechainicialdisfrute() != null) {
                log.info("listaNovedades.get(i).getFechainicialdisfrute() : " + listaNovedades.get(i).getFechainicialdisfrute());
                if (nuevaNovedad.getFechainicialdisfrute().equals(listaNovedades.get(i).getFechainicialdisfrute())) {
                    error++;
                }
            }
        }
        log.warn("Error : " + error);
        if (error > 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:fechaRepetida");
            RequestContext.getCurrentInstance().execute("PF('fechaRepetida').show()");
        } else {
            listaModificar.add(novedadMostrar);
            guardado = false;
        }
    }

    public void celda(String Campo) {
        if (Campo.equals("MOTIVO")) {
            activarBotonLov();
            cualCelda = 0;
        } else if (Campo.equals("RETIRO")) {
            activarBotonLov();
            cualCelda = 1;
        } else if (Campo.equals("FECHA")) {
            deshabilitarBotonLov();
            cualCelda = 2;
        } else if (Campo.equals("OBSERVACION")) {
            deshabilitarBotonLov();
            cualCelda = 3;
        } else {
            deshabilitarBotonLov();
        }
    }

    public void editarCelda() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (cualCelda == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarMotivos");
            RequestContext.getCurrentInstance().execute("PF('editarMotivos').show()");
            cualCelda = -1;
        } else if (cualCelda == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarRetiros");
            RequestContext.getCurrentInstance().execute("PF('editarRetiros').show()");
            cualCelda = -1;
        } else if (cualCelda == 2) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarFechas");
            RequestContext.getCurrentInstance().execute("PF('editarFechas').show()");
            cualCelda = -1;
        } else if (cualCelda == 3) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarObservaciones");
            RequestContext.getCurrentInstance().execute("PF('editarObservaciones').show()");
            cualCelda = -1;
        }
    }

    public void actualizarEmpleadosNovedad() {
        RequestContext context = RequestContext.getCurrentInstance();

        listaEmpleados.clear();
        listaEmpleados.add(empleadoSeleccionadoLOV);
        empleadoSeleccionado = empleadoSeleccionadoLOV;
        contarRegistros();
        context.reset("formularioDialogos:LOVEmpleados:globalFilter");
        context.reset("formularioDialogos:LOVEmpleados");
        RequestContext.getCurrentInstance().execute("PF('LOVEmpleados').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('empleadosDialogo').hide()");
        //RequestContext.getCurrentInstance().update("formularioDialogos:LOVEmpleados");
        RequestContext.getCurrentInstance().update("form:datosEmpleados");
        RequestContext.getCurrentInstance().update("form:formularioNovedades");

        listaNovedades = null;
        getListaNovedades();
        //getNovedadMostrar();

        lovEmpleados = null;
        filtradosListaEmpleadosNovedad = null;
        empleadoSeleccionadoLOV = null;
        aceptar = true;
        tipoActualizacion = -1;
        activarMostrarTodos = false;

        context.reset("formularioDialogos:LOVEmpleados:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVEmpleados').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('empleadosDialogo').hide()");
        RequestContext.getCurrentInstance().update("form:btnMostrarTodos");
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
    }

    public void listaValoresBoton() {

        RequestContext context = RequestContext.getCurrentInstance();
        if (cualCelda == 0) {
            contarRegistrosLovMLiqDef();
            RequestContext.getCurrentInstance().update("formularioDialogos:motivosDialogo");
            RequestContext.getCurrentInstance().execute("PF('motivosDialogo').show()");
            tipoActualizacion = 0;
        } else if (cualCelda == 1) {
            contarRegistrosLovMRetiro();
            RequestContext.getCurrentInstance().update("formularioDialogos:retirosDialogo");
            RequestContext.getCurrentInstance().execute("PF('retirosDialogo').show()");
            tipoActualizacion = 0;
        }
    }

    public void limpiarNuevaNovedad() {
        nuevaNovedad = new NovedadesSistema();
        nuevaNovedad.setMotivodefinitiva(new MotivosDefinitivas());
        nuevaNovedad.setMotivoretiro(new MotivosRetiros());
        nuevaNovedad.setFechainicialdisfrute(new Date());
    }

    public void activarCtrlF11() {
        if (bandera == 0) {
            FacesContext c = FacesContext.getCurrentInstance();
            altoTabla = "145";
            nDEmpleadosNombres = (Column) c.getViewRoot().findComponent("form:datosEmpleados:nDEmpleadosNombres");
            nDEmpleadosNombres.setFilterStyle("width: 85% !important;");
            nDEmpleadosCodigos = (Column) c.getViewRoot().findComponent("form:datosEmpleados:nDEmpleadosCodigos");
            nDEmpleadosCodigos.setFilterStyle("width: 80% !important;");
            RequestContext.getCurrentInstance().update("form:datosEmpleados");
            bandera = 1;
            tipoLista = 1;
        } else if (bandera == 1) {
            restaurarTabla();
        }
        contarRegistros();
    }

    public void restaurarTabla() {
        FacesContext c = FacesContext.getCurrentInstance();
        altoTabla = "165";
        nDEmpleadosNombres = (Column) c.getViewRoot().findComponent("form:datosEmpleados:nDEmpleadosNombres");
        nDEmpleadosNombres.setFilterStyle("display: none; visibility: hidden;");
        nDEmpleadosCodigos = (Column) c.getViewRoot().findComponent("form:datosEmpleados:nDEmpleadosCodigos");
        nDEmpleadosCodigos.setFilterStyle("display: none; visibility: hidden;");
        RequestContext.getCurrentInstance().execute("PF('datosEmpleados').clearFilters()");
        RequestContext.getCurrentInstance().update("form:datosEmpleados");
        contarRegistros();
        bandera = 0;
        tipoLista = 0;
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:novedadesEmpleadosExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "NovedadesDefinitivasEmpleadosPDF", false, false, "UTF-8", null, null);
        context.responseComplete();
        deshabilitarBotonLov();
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:novedadesEmpleadosExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "NovedadesDefinitivasEmpleadosXLS", false, false, "UTF-8", null, null);
        context.responseComplete();
        deshabilitarBotonLov();
    }

    public void actualizarMotivo() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (guardado == true) {
                guardado = false;
            }
            permitirIndex = true;
            novedadMostrar.setMotivodefinitiva(seleccionMotivos);
            RequestContext.getCurrentInstance().update("form:motivoLiquidacion");
            listaModificar.add(novedadMostrar);
        } else if (tipoActualizacion == 1) {
            nuevaNovedad.setMotivodefinitiva(seleccionMotivos);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaNovedad");
        } else if (tipoActualizacion == 2) {
            duplicarNovedad.setMotivodefinitiva(seleccionMotivos);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarNovedad");
        }
        seleccionMotivos = null;
        aceptar = true;
        tipoActualizacion = -1;
        context.reset("formularioDialogos:LOVMotivos:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVMotivos').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('motivosDialogo').hide()");
        RequestContext.getCurrentInstance().update("formularioDialogos:motivosDialogo");
    }

    public void bool() {
        novedadMostrar.getIndemnizaBool();
    }

    //CREAR NOVEDADES
    public void agregarNuevaNovedad() {
        getListaNovedades();
        int pasa = 0;
        int pasa2 = 0;
        mensajeValidacion = new String();
        if (!listaNovedades.isEmpty()) {
            for (int i = 0; i < listaNovedades.size(); i++) {
                if (nuevaNovedad.getFechainicialdisfrute() != null && (nuevaNovedad.getFechainicialdisfrute().equals(listaNovedades.get(i).getFechainicialdisfrute()))) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:fechaRepetida");
                    RequestContext.getCurrentInstance().execute("PF('fechaRepetida').show()");
                    pasa2++;
                }
            }
        }
        if (nuevaNovedad.getFechainicialdisfrute() == null) {
            log.info("Entro a Fecha ");
            mensajeValidacion = mensajeValidacion + " * Fecha Liquidacion Definitiva\n";
            pasa++;
        }
        if (nuevaNovedad.getMotivodefinitiva().getNombre() == null) {
            log.info("Entro a Motivo");
            mensajeValidacion = mensajeValidacion + " * Motivo Liquidacion Definitiva\n";
            pasa++;
        }
        if (nuevaNovedad.getMotivoretiro().getNombre() == null) {
            log.info("Entro a Retiro");
            mensajeValidacion = mensajeValidacion + " * Motivo Retiro\n";
            pasa++;
        }
        log.info("Valor Pasa: " + pasa);
        if (pasa != 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevaNovedad");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevaNovedad').show()");
        }
        if (pasa == 0 && !listaNovedades.isEmpty()) {
            RequestContext.getCurrentInstance().update("formularioDialogos:existeRegistro");
            RequestContext.getCurrentInstance().execute("PF('existeRegistro').show()");
        }
        if (pasa == 0 && pasa2 == 0) {
            //AGREGAR REGISTRO A LA LISTA NOVEDADES .
            k++;
            l = BigInteger.valueOf(k);
            nuevaNovedad.setSecuencia(l);
            nuevaNovedad.setEmpleado(empleadoSeleccionado);
            nuevaNovedad.setTipo("DEFINITIVA");
            nuevaNovedad.setSubtipo("DINERO");
            nuevaNovedad.setDias(BigInteger.valueOf(0));
            listaNovedades.add(nuevaNovedad);
            novedadMostrar = listaNovedades.get(listaNovedades.indexOf(nuevaNovedad));
            listaCrear.add(nuevaNovedad);
            if (novedadMostrar != null) {
                activate = false;
            }
            nuevaNovedad = new NovedadesSistema();
            nuevaNovedad.setMotivodefinitiva(new MotivosDefinitivas());
            nuevaNovedad.setMotivoretiro(new MotivosRetiros());
            nuevaNovedad.setDias(BigInteger.valueOf(0));
            nuevaNovedad.setTipo("DEFINITIVA");
            nuevaNovedad.setSubtipo("DINERO");
            nuevaNovedad.setFechainicialdisfrute(new Date());
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            primeraNovedad();
            RequestContext.getCurrentInstance().execute("PF('NuevaNovedadEmpleado').hide()");
        } else {
            RequestContext.getCurrentInstance().update("form:formularioNovedades");
        }
        contarRegistros();
    }

    public void save(ActionEvent actionEvent) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Guardado Exitoso", "Los cambios han sido guardados con éxito"));
    }

    public void guardarYSalir() {
        guardarCambiosNovedades();
        salir();
    }

    //GUARDAR
    public void guardarCambiosNovedades() {
        if (guardado == false) {

            //MODIFICAR
            if (listaModificar != null) {
                if (!listaModificar.isEmpty()) {
                    for (int n = 0; n < listaModificar.size(); n++) {
                        listaModificar.get(n).setEmpleado(empleadoSeleccionado);
                        listaModificar.get(n).setTipo("DEFINITIVA");
                        listaModificar.get(n).setSubtipo("DINERO");
                        administrarNovedadesSistema.modificarNovedades(listaModificar.get(n));
                    }
                    listaModificar.clear();
                }
            }
            //CREAR
            if (listaCrear != null) {
                if (!listaCrear.isEmpty()) {
                    for (int n = 0; n < listaCrear.size(); n++) {
                        administrarNovedadesSistema.crearNovedades(listaCrear.get(n));
                    }
                    listaCrear.clear();
                }
            }
            if (listaBorrar != null) {
                if (!listaBorrar.isEmpty()) {
                    for (int n = 0; n < listaBorrar.size(); n++) {
                        administrarNovedadesSistema.borrarNovedades(listaBorrar.get(n));
                    }
                    listaBorrar.clear();
                }
            }

            listaNovedades = null;
            RequestContext.getCurrentInstance().update("form:datosEmpleados");
            guardado = true;
            permitirIndex = true;

            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext context = RequestContext.getCurrentInstance();
            FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }
        deshabilitarBotonLov();
    }

    public void noGuardarCambiosNovedades() {
        listaBorrar.clear();
        listaCrear.clear();
        listaModificar.clear();
        guardado = true;
    }

    public void seleccionAntes() {
        empleadoSeleccionado = empleadoBack;
        FacesContext c = FacesContext.getCurrentInstance();
        tablaC = (DataTable) c.getViewRoot().findComponent("form:datosEmpleados");
        tablaC.setSelection(empleadoSeleccionado);
        RequestContext.getCurrentInstance().update("form:datosEmpleados");
    }

    public void activarBotonLov() {
        activarLOV = false;
        RequestContext.getCurrentInstance().update("form:listaValores");
    }

    public void deshabilitarBotonLov() {
        activarLOV = true;
        RequestContext.getCurrentInstance().update("form:listaValores");
    }

    //BORRAR Novedades
    public void borrarNovedades() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (novedadMostrar != null) {
            listaBorrar.add(novedadMostrar);
            if (listaCrear.contains(novedadMostrar)) {
                listaCrear.remove(novedadMostrar);
            }
            if (listaModificar.contains(novedadMostrar)) {
                listaModificar.remove(novedadMostrar);
            }
            listaNovedades.remove(novedadMostrar);
        }
        if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
        primeraNovedad();
        deshabilitarBotonLov();
        RequestContext.getCurrentInstance().update("form:formularioNovedades");
    }

    //DUPLICAR NOVEDAD
    public void duplicarN() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (novedadMostrar != null) {
            if (novedadMostrar.getEmpleado() != null) {
                duplicarNovedad = new NovedadesSistema();
                k++;
                l = BigInteger.valueOf(k);
                if (tipoLista == 0) {
                    duplicarNovedad.setSecuencia(l);
                    duplicarNovedad.setEmpleado(empleadoSeleccionado);
                    duplicarNovedad.setTipo(novedadMostrar.getTipo());
                    duplicarNovedad.setSubtipo(novedadMostrar.getSubtipo());
                    duplicarNovedad.setDias(novedadMostrar.getDias());
                    duplicarNovedad.setFechainicialdisfrute(novedadMostrar.getFechainicialdisfrute());
                    duplicarNovedad.setMotivodefinitiva(novedadMostrar.getMotivodefinitiva());
                    duplicarNovedad.setMotivoretiro(novedadMostrar.getMotivoretiro());
                    duplicarNovedad.setObservaciones(novedadMostrar.getObservaciones());
                }
                RequestContext.getCurrentInstance().update("formularioDialogos:duplicarNovedad");
                RequestContext.getCurrentInstance().execute("PF('DuplicarNovedadEmpleado').show()");
            }
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void confirmarDuplicar() {
        int pasa = 0;
        int pasa2 = 0;
        RequestContext context = RequestContext.getCurrentInstance();

        if (!listaNovedades.isEmpty()) {
            for (int i = 0; i < listaNovedades.size(); i++) {
                if (duplicarNovedad.getFechainicialdisfrute() != null && (duplicarNovedad.getFechainicialdisfrute().equals(listaNovedades.get(i).getFechainicialdisfrute()))) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:fechaRepetida");
                    RequestContext.getCurrentInstance().execute("PF('fechaRepetida').show()");
                    pasa2++;
                }
            }
        }

        if (duplicarNovedad.getFechainicialdisfrute() == null) {
            mensajeValidacion = mensajeValidacion + " * Fecha Liquidacion Definitiva\n";
            pasa++;
        }

        if (duplicarNovedad.getMotivodefinitiva().getNombre().equals(" ")) {
            mensajeValidacion = mensajeValidacion + " * Motivo Liquidacion Definitiva\n";
            pasa++;
        }
        if (duplicarNovedad.getMotivoretiro().getNombre().equals(" ")) {
            mensajeValidacion = mensajeValidacion + " * Motivo Retiro\n";
            pasa++;
        }

        if (pasa != 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevaNovedad");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevaNovedad').show()");
        }

        if (pasa == 0 && pasa2 == 0) {
            //AGREGAR REGISTRO A LA LISTA NOVEDADES .
            k++;
            l = BigInteger.valueOf(k);
            duplicarNovedad.setSecuencia(l);
            duplicarNovedad.setEmpleado(empleadoSeleccionado);
            duplicarNovedad.setTipo("DEFINITIVA");
            duplicarNovedad.setSubtipo("DINERO");
            listaNovedades.add(duplicarNovedad);
            novedadMostrar = listaNovedades.get(listaNovedades.indexOf(duplicarNovedad));
            listaCrear.add(duplicarNovedad);

            duplicarNovedad = new NovedadesSistema();
            duplicarNovedad.setMotivodefinitiva(new MotivosDefinitivas());
            duplicarNovedad.setMotivoretiro(new MotivosRetiros());
            duplicarNovedad.setDias(BigInteger.valueOf(0));
            duplicarNovedad.setTipo("DEFINITIVA");
            duplicarNovedad.setSubtipo("DINERO");
            RequestContext.getCurrentInstance().update("form:formularioNovedades");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            RequestContext.getCurrentInstance().execute("PF('DuplicarNovedadEmpleado').hide()");
        } else {
        }
        contarRegistros();
    }

    //LIMPIAR DUPLICAR
    /**
     * Metodo que limpia los datos de un duplicar Novedad
     */
    public void limpiarduplicarNovedad() {
        duplicarNovedad = new NovedadesSistema();
        duplicarNovedad.setTipo("DEFINITIVA");
        duplicarNovedad.setSubtipo("DINERO");
        duplicarNovedad.setMotivodefinitiva(new MotivosDefinitivas());
        duplicarNovedad.setMotivoretiro(new MotivosRetiros());
    }

    public void actualizarRetiros() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (guardado == true) {
                guardado = false;
            }
            permitirIndex = true;
            novedadMostrar.setMotivoretiro(seleccionRetiros);
            RequestContext.getCurrentInstance().update("form:motivoRetiro");
            listaModificar.add(novedadMostrar);
        } else if (tipoActualizacion == 1) {
            nuevaNovedad.setMotivoretiro(seleccionRetiros);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaNovedad");
        } else if (tipoActualizacion == 2) {
            duplicarNovedad.setMotivoretiro(seleccionRetiros);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarNovedad");
        }
        seleccionRetiros = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        context.reset("formularioDialogos:LOVRetiros:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVRetiros').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('retirosDialogo').hide()");
        RequestContext.getCurrentInstance().update("formularioDialogos:LOVRetiros");
    }

    public void cancelarCambioMotivos() {
        seleccionMotivos = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:LOVMotivos:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVMotivos').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('motivosDialogo').hide()");
        RequestContext.getCurrentInstance().update("formularioDialogos:motivosDialogo");
    }

    public void cancelarCambioRetiros() {
        seleccionRetiros = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:LOVRetiros:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVRetiros').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('retirosDialogo').hide()");
        RequestContext.getCurrentInstance().update("formularioDialogos:LOVRetiros");
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void cancelarCambioEmpleados() {
        lovEmpleadosFiltrar = null;
        empleadoSeleccionadoLOV = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:LOVEmpleados:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVEmpleados').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('empleadosDialogo').hide()");
    }

    //RASTROS 
    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        deshabilitarBotonLov();
        if (novedadMostrar != null) {
            int result = administrarRastros.obtenerTabla(novedadMostrar.getSecuencia(), "NOVEDADESSISTEMA");
            if (result == 1) {
                RequestContext.getCurrentInstance().execute("PF('errorObjetosDB').show()");
            } else if (result == 2) {
                RequestContext.getCurrentInstance().execute("PF('confirmarRastro').show()");
            } else if (result == 3) {
                RequestContext.getCurrentInstance().execute("PF('errorRegistroRastro').show()");
            } else if (result == 4) {
                RequestContext.getCurrentInstance().execute("PF('errorTablaConRastro').show()");
            } else if (result == 5) {
                RequestContext.getCurrentInstance().execute("PF('errorTablaSinRastro').show()");
            }
        } else if (administrarRastros.verificarHistoricosTabla("NOVEDADESSISTEMA")) {
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
    }

    //CANCELAR MODIFICACIONES
    public void refrescar() {
        RequestContext context = RequestContext.getCurrentInstance();
        permitirIndex = true;
        cualCelda = -1;
        listaNovedades = null;
        guardado = true;
        permitirIndex = true;
        aceptar = true;
        activarMostrarTodos = true;
        k = 0;
        tipoLista = 0;
        numNovedad = -1;
        activarNoRango = false;

        nuevaNovedad = new NovedadesSistema();
        duplicarNovedad = new NovedadesSistema();
        permitirIndex = true;

        listaBorrar.clear();
        listaCrear.clear();
        listaModificar.clear();
        empleadoSeleccionado = null;
        listaEmpleados = null;
        getListaEmpleados();
        contarRegistros();
        contarRegistrosLovEmpl();
        deshabilitarBotonLov();
        RequestContext.getCurrentInstance().update("form:btnMostrarTodos");
        RequestContext.getCurrentInstance().update("form:datosEmpleados");
        RequestContext.getCurrentInstance().update("form:formularioNovedades");
    }

    public void salir() {
        limpiarListasValor();
        listaBorrar.clear();
        listaCrear.clear();
        listaModificar.clear();
        empleadoSeleccionado = null;
        k = 0;
        listaEmpleados = null;
        listaNovedades = null;
        guardado = true;
        navegar("atras");
    }

    //GETTERS & SETTERS
    public List<NovedadesSistema> getListaNovedades() {

        if (listaNovedades == null) {
            if (empleadoSeleccionado != null) {
                listaNovedades = administrarNovedadesSistema.novedadesEmpleado(empleadoSeleccionado.getSecuencia());
            }
        }
        novedadMostrar = null;
        if (listaNovedades == null || listaNovedades.isEmpty()) {
            activate = true;
        } else {
            activate = false;
            getNovedadMostrar();
        }
        return listaNovedades;
    }

    public void setListaNovedades(List<NovedadesSistema> listaNovedades) {
        this.listaNovedades = listaNovedades;
    }

    public List<NovedadesSistema> getFiltradosListaNovedades() {
        return filtradosListaNovedades;
    }

    public void setFiltradosListaNovedades(List<NovedadesSistema> filtradosListaNovedades) {
        this.filtradosListaNovedades = filtradosListaNovedades;
    }

    public List<Empleados> getListaEmpleados() {
        if (listaEmpleados == null) {
            listaEmpleados = administrarNovedadesSistema.buscarEmpleados();
        }
        return listaEmpleados;
    }

    public void siguienteNovedad() {
        int poss = listaNovedades.indexOf(novedadMostrar);
        if (poss < (listaNovedades.size() - 1)) {
            novedadMostrar = listaNovedades.get(poss + 1);
            activarNoRango = false;
            numNovedad++;
        } else {
            activarNoRango = true;
        }
        RequestContext.getCurrentInstance().update("form:formularioNovedades");
    }

    public void anteriorNovedad() {
        int poss = listaNovedades.indexOf(novedadMostrar);
        if (poss > 0) {
            novedadMostrar = listaNovedades.get(poss - 1);
            activarNoRango = false;
            numNovedad--;
        } else {
            activarNoRango = true;
        }
        RequestContext.getCurrentInstance().update("form:formularioNovedades");
    }

    public void primeraNovedad() {
        if (listaNovedades.size() >= 1) {
            novedadMostrar = listaNovedades.get(0);
            activarNoRango = false;
            numNovedad = 0;
        } else {
            activarNoRango = true;
            numNovedad = -1;
        }
        RequestContext.getCurrentInstance().update("form:formularioNovedades");
    }

    public void ultimaNovedad() {
        if (listaNovedades.size() > 1) {
            novedadMostrar = listaNovedades.get(listaNovedades.size() - 1);
            activarNoRango = false;
            numNovedad = listaNovedades.size() - 1;
        } else {
            activarNoRango = true;
        }
        RequestContext.getCurrentInstance().update("form:formularioNovedades");
    }

    public void eventoFiltrar() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
        deshabilitarBotonLov();
        contarRegistros();
    }

    public void eventoFiltrarEmpleados() {
        empleadoBack = null;
        listaNovedades = null;
        empleadoSeleccionado = null;
        novedadMostrar = null;
        numNovedad = -1;
        activarNoRango = false;
        RequestContext.getCurrentInstance().update("form:formularioNovedades");
        RequestContext.getCurrentInstance().update("form:btnMostrarTodos");
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
        deshabilitarBotonLov();
        contarRegistros();
    }

    public void contarRegistros() {
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
    }

    public void contarRegistrosLovEmpl() {
        RequestContext.getCurrentInstance().update("formularioDialogos:informacionRegistroEmplLov");
    }

    public void contarRegistrosLovMRetiro() {
        RequestContext.getCurrentInstance().update("formularioDialogos:informacionRegistroRet");
    }

    public void contarRegistrosLovMLiqDef() {
        RequestContext.getCurrentInstance().update("formularioDialogos:informacionRegistroMot");
    }

    public void setListaEmpleados(List<Empleados> listaEmpleadosNovedad) {
        this.listaEmpleados = listaEmpleadosNovedad;
    }

    public List<Empleados> getFiltradosListaEmpleadosNovedad() {
        return filtradosListaEmpleadosNovedad;
    }

    public void setFiltradosListaEmpleadosNovedad(List<Empleados> filtradosListaEmpleadosNovedad) {
        this.filtradosListaEmpleadosNovedad = filtradosListaEmpleadosNovedad;
    }

    public Empleados getEmpleadoSeleccionado() {
        return empleadoSeleccionado;
    }

    public void setEmpleadoSeleccionado(Empleados seleccion) {
        this.empleadoSeleccionado = seleccion;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public void setAceptar(boolean aceptar) {
        this.aceptar = aceptar;
    }
    //LOV empleados

    public List<Empleados> getLovEmpleados() {
        if (lovEmpleados == null) {
            lovEmpleados = administrarNovedadesSistema.lovEmpleados();
        }
        return lovEmpleados;
    }

    public void setLovEmpleados(List<Empleados> listaEmpleados) {
        this.lovEmpleados = listaEmpleados;
    }

    public List<Empleados> getLovEmpleadosFiltrar() {
        return lovEmpleadosFiltrar;
    }

    public void setLovEmpleadosFiltrar(List<Empleados> filtradoslistaEmpleados) {
        this.lovEmpleadosFiltrar = filtradoslistaEmpleados;
    }

    public Empleados getEmpleadoSeleccionadoLOV() {
        return empleadoSeleccionadoLOV;
    }

    public void setEmpleadoSeleccionadoLOV(Empleados seleccionEmpleados) {
        this.empleadoSeleccionadoLOV = seleccionEmpleados;
    }

    public NovedadesSistema getNovedadMostrar() {
        if (listaNovedades != null) {
            if (!listaNovedades.isEmpty()) {
                if (novedadMostrar == null) {
                    novedadMostrar = listaNovedades.get(0);
                }
                numNovedad = listaNovedades.indexOf(novedadMostrar);
            }
        }
        if (novedadMostrar == null) {
            novedadMostrar = new NovedadesSistema();
            numNovedad = -1;
        }
        return novedadMostrar;
    }

    public void setNovedadMostrar(NovedadesSistema mostrar) {
        this.novedadMostrar = mostrar;
    }
//LOV MOTIVOS LIQUIDACIONES

    public List<MotivosDefinitivas> getLovMotivos() {
        if (lovMotivos == null) {
            lovMotivos = administrarNovedadesSistema.lovMotivos();
        }
        return lovMotivos;
    }

    public void setLovMotivos(List<MotivosDefinitivas> lovMotivos) {
        this.lovMotivos = lovMotivos;
    }

    public List<MotivosDefinitivas> getLovMotivosFiltrar() {
        return lovMotivosFiltrar;
    }

    public void setLovMotivosFiltrar(List<MotivosDefinitivas> lovMotivosFiltrar) {
        this.lovMotivosFiltrar = lovMotivosFiltrar;
    }

    public MotivosDefinitivas getSeleccionMotivos() {
        return seleccionMotivos;
    }

    public void setSeleccionMotivos(MotivosDefinitivas seleccionMotivos) {
        this.seleccionMotivos = seleccionMotivos;
    }
//LOV RETIROS

    public List<MotivosRetiros> getLovMotiRetiros() {
        if (lovMotiRetiros == null) {
            lovMotiRetiros = administrarNovedadesSistema.lovRetiros();
        }
        return lovMotiRetiros;
    }

    public void setLovMotiRetiros(List<MotivosRetiros> listaRetiros) {
        this.lovMotiRetiros = listaRetiros;
    }

    public List<MotivosRetiros> getLovMotiRetirosFiltrar() {
        return lovMotiRetirosFiltrar;
    }

    public void setLovMotiRetirosFiltrar(List<MotivosRetiros> lovMotiRetirosFiltrar) {
        this.lovMotiRetirosFiltrar = lovMotiRetirosFiltrar;
    }

    public MotivosRetiros getSeleccionRetiros() {
        return seleccionRetiros;
    }

    public void setSeleccionRetiros(MotivosRetiros seleccionRetiros) {
        this.seleccionRetiros = seleccionRetiros;
    }

    public NovedadesSistema getDuplicarNovedad() {
        return duplicarNovedad;
    }

    public void setDuplicarNovedad(NovedadesSistema duplicarNovedad) {
        this.duplicarNovedad = duplicarNovedad;
    }

    public NovedadesSistema getNuevaNovedad() {
        return nuevaNovedad;
    }

    public void setNuevaNovedad(NovedadesSistema nuevaNovedad) {
        this.nuevaNovedad = nuevaNovedad;
    }

    public String getMensajeValidacion() {
        return mensajeValidacion;
    }

    public void setMensajeValidacion(String mensajeValidacion) {
        this.mensajeValidacion = mensajeValidacion;
    }

    public Boolean getActivate() {
        return activate;
    }

    public void setActivate(Boolean activate) {
        this.activate = activate;
    }

    public String getPaginaAnterior() {
        return paginaAnterior;
    }

    public void setPaginaAnterior(String paginaAnterior) {
        this.paginaAnterior = paginaAnterior;
    }

    public boolean isActivarMostrarTodos() {
        return activarMostrarTodos;
    }

    public void setActivarMostrarTodos(boolean activarMostrarTodos) {
        this.activarMostrarTodos = activarMostrarTodos;
    }

    public String getInfoRegistro() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosEmpleados");
        infoRegistro = String.valueOf(tabla.getRowCount());
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

    public int getNumNovedad() {
        return numNovedad;
    }

    public void setNumNovedad(int numNovedad) {
        this.numNovedad = numNovedad;
    }

    public boolean isActivarNoRango() {
        return activarNoRango;
    }

    public void setActivarNoRango(boolean activarNoRango) {
        this.activarNoRango = activarNoRango;
    }

    public String getInfoRegistroEmpl() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:LOVEmpleados");
        infoRegistroEmpl = String.valueOf(tabla.getRowCount());
        return infoRegistroEmpl;
    }

    public void setInfoRegistroEmpl(String infoRegistroEmpl) {
        this.infoRegistroEmpl = infoRegistroEmpl;
    }

    public boolean isActivarLOV() {
        return activarLOV;
    }

    public void setActivarLOV(boolean activarLOV) {
        this.activarLOV = activarLOV;
    }

    public String getInfoRegistroMLiqDef() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:LOVMotivos");
        infoRegistroMLiqDef = String.valueOf(tabla.getRowCount());
        return infoRegistroMLiqDef;
    }

    public void setInfoRegistroMLiqDef(String infoRegistroMLiqDef) {
        this.infoRegistroMLiqDef = infoRegistroMLiqDef;
    }

    public String getInfoRegistroMRetiro() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:LOVRetiros");
        infoRegistroMRetiro = String.valueOf(tabla.getRowCount());
        return infoRegistroMRetiro;
    }

    public void setInfoRegistroMRetiro(String infoRegistroMRetiro) {
        this.infoRegistroMRetiro = infoRegistroMRetiro;
    }

    public String getAltoTabla() {
        return altoTabla;
    }

    public void setAltoTabla(String altoTabla) {
        this.altoTabla = altoTabla;
    }

    public String getAltoTablaReg() {
        if (altoTabla.equals("145")) {
            altoTablaReg = "8";
        } else {
            altoTablaReg = "9";
        }
        return altoTablaReg;
    }

    public void setAltoTablaReg(String altoTablaReg) {
        this.altoTablaReg = altoTablaReg;
    }
}
