/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Causasausentismos;
import Entidades.Clasesausentismos;
import Entidades.Diagnosticoscategorias;
import Entidades.Empleados;
import Entidades.EnfermeadadesProfesionales;
import Entidades.Ibcs;
import Entidades.Soaccidentes;
import Entidades.Soausentismos;
import Entidades.Terceros;
import Entidades.Tiposausentismos;
import Exportar.ExportarPDFTablasAnchas;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarRastrosInterface;
import InterfaceAdministrar.AdministrarSoausentismosInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
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
import javax.servlet.http.HttpSession;
import org.primefaces.component.column.Column;
import org.primefaces.component.commandbutton.CommandButton;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.export.Exporter;
import org.primefaces.context.RequestContext;

/**
 *
 * @author user
 */
@ManagedBean
@SessionScoped
public class ControlAusentismos implements Serializable {

    @EJB
    AdministrarSoausentismosInterface administrarAusentismos;
    @EJB
    AdministrarRastrosInterface administrarRastros;
    //LISTA FICTI PORCENTAJES
    private List<String> listaPorcentaje;
    private List<String> filtradosListaPorcentajes;
    private String seleccionPorcentajes;
    //LISTA FICTI IBCS
    private List<Ibcs> listaIBCS;
    private List<Ibcs> filtradosListaIBCS;
    private Ibcs seleccionIBCS;
    //LISTA FICTI FORMA LIQUIDACION
    private List<String> listaForma;
    private List<String> filtradosListaForma;
    private String seleccionForma;
    //SECUENCIA DEL EMPLEADO
    private BigInteger secuenciaEmpleado;
    //Secuencia de la Causa
    private BigInteger secuenciaCausa;
    //Secuencia del ausentismo
    private BigInteger secuenciaAusentismo;
    //LISTA AUSENTISMOS
    private List<Soausentismos> listaAusentismos;
    private List<Soausentismos> filtradosListaAusentismos;
    private Soausentismos ausentismoSeleccionado;
    //LISTA DE ARRIBA
    private List<Empleados> listaEmpleadosAusentismo;
    private List<Empleados> filtradosListaEmpleadosAusentismo;
    private Empleados seleccionMostrar; //Seleccion Mostrar
    //editar celda
    private Soausentismos editarAusentismos;
    private boolean cambioEditor, aceptarEditar;
    private int cualCelda, tipoLista;
    //OTROS
    private boolean aceptar;
    private int tipoActualizacion; //Activo/Desactivo Crtl + F11
    private int bandera;
    private int banderaBotones;
    private int banderaBotonesD;
    private boolean permitirIndex;
    //RASTROS
    private boolean guardado, guardarOk;
    //Crear Novedades
    private List<Soausentismos> listaAusentismosCrear;
    public Soausentismos nuevoAusentismo;
    private int k;
    private BigInteger l;
    private String mensajeValidacion;
    //Modificar Novedades
    private List<Soausentismos> listaAusentismosModificar;
    //Borrar Novedades
    private List<Soausentismos> listaAusentismosBorrar;
    //L.O.V EMPLEADOS
    private List<Empleados> listaEmpleados;
    private List<Empleados> filtradoslistaEmpleados;
    private Empleados seleccionEmpleados;
    //Autocompletar
    private String TipoAusentismo, Tercero, ClaseAusentismo, CausaAusentismo, Porcentaje, Forma, AD, Enfermedad, Diagnostico;
    private String BaseLiquidacion, Dias, Horas, Fechafinaus, Fechaexpedicion, InicioPago, FinPago, NumeroCertificado, Prorrogas; // faltan más campos
    private String Relacionn, Observacion; // faltan más campos
    // faltan más campos
    //L.O.V TIPO AUSENTISMO
    private List<Tiposausentismos> listaTiposAusentismos;
    private List<Tiposausentismos> filtradoslistaTiposAusentismos;
    private Tiposausentismos seleccionTiposAusentismos;
    //L.O.V CLASE AUSENTISMO
    private List<Clasesausentismos> listaClasesAusentismos;
    private List<Clasesausentismos> filtradoslistaClasesAusentismos;
    private Clasesausentismos seleccionClasesAusentismos;
    //L.O.V CAUSA AUSENTISMO
    private List<Causasausentismos> listaCausasAusentismos;
    private List<Causasausentismos> filtradoslistaCausasAusentismos;
    private Causasausentismos seleccionCausasAusentismos;
    //L.O.V Descripcion Accidente
    private List<Soaccidentes> listaAccidentes;
    private List<Soaccidentes> filtradoslistaAccidentes;
    private Soaccidentes seleccionAccidentes;
    //L.O.V Enfermedades Profesionales
    private List<EnfermeadadesProfesionales> listaEnfermeadadesProfesionales;
    private List<EnfermeadadesProfesionales> filtradoslistaEnfermeadadesProfesionales;
    private EnfermeadadesProfesionales seleccionEnfermeadadesProfesionales;
    //L.O.V Terceros
    private List<Terceros> listaTerceros;
    private List<Terceros> filtradoslistaTerceros;
    private Terceros seleccionTerceros;
    //L.O.V Diagnostivos
    private List<Diagnosticoscategorias> listaDiagnosticos;
    private List<Diagnosticoscategorias> filtradoslistaDiagnosticos;
    private Diagnosticoscategorias seleccionDiagnosticos;
    //L.O.V Prorrogas
    private List<Soausentismos> listaProrrogas;
    private List<Soausentismos> filtradoslistaProrrogas;
    private Soausentismos seleccionProrrogas;
    //Duplicar
    public Soausentismos duplicarAusentismo;
    //PRORROGA MOSTRAR
    private String Prorroga, Relacion;
    //Columnas Tabla NOVEDADES
    private Column ATipo, AClase, ACausa, ADias, AHoras, AFecha, AFechaFinaus, AFechaExpedicion, AFechaInipago,
            AFechaFinpago, APorcentaje, ABase, AForma, ADescripcionCaso, AEnfermedad, ANumero, ADiagnostico,
            AProrroga, ARelacion, ARelacionada, ATercero, AObservaciones;
    //
    private CommandButton botonAgregar, botonCancelar, botonLimpiar;
    private CommandButton botonAgregarD, botonCancelarD, botonLimpiarD;
    //
    private boolean cambiosPagina;
    //
    private String altoTabla;
    private String altoTablaReg;
    private String altoDialogoNuevo;
    private String altoDialogoDuplicar;
    private boolean colapsado;
    private String infoRegistroAusentismos;
    private String infoRegistroTipo;
    private String infoRegistroClase;
    private String infoRegistroCausa;
    private String infoRegistroPorcentaje;
    private String infoRegistroBase;
    private String infoRegistroForma;
    private String infoRegistroAccidente;
    private String infoRegistroEnfermedad;
    private String infoRegistroDiagnostico;
    private String infoRegistroProrroga;
    private String infoRegistroTercero;
    private String infoRegistroEmpleado;
    private String infoRegistroEmpleadoLov;
    private boolean activarLov;
    private DataTable tablaC;
    private String paginaAnterior = "nominaf";
    private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

    public ControlAusentismos() {
        colapsado = true;
        altoDialogoNuevo = "430";
        altoDialogoDuplicar = "430";
        altoTabla = "139";
        cambiosPagina = true;
        Relacion = null;
        Prorroga = null;
        listaProrrogas = null;
        listaIBCS = null;
        listaAccidentes = null;
        listaDiagnosticos = null;
        listaPorcentaje = new ArrayList<String>();
        listaPorcentaje.add("50");
        listaPorcentaje.add("66.6666");
        listaPorcentaje.add("80");
        listaPorcentaje.add("100");
        listaForma = new ArrayList<String>();
        listaForma.add("BASICO");
        listaForma.add("IBC MES ANTERIOR");
        listaForma.add("IBC MES ENERO");
        listaForma.add("IBC MES INCAPACIDAD");
        listaForma.add("PROMEDIO ACUMULADOS 12 MESES");
        listaForma.add("PROMEDIO IBC 12 MESES");
        listaForma.add("PROMEDIO IBC 6 MESES");
        permitirIndex = true;
        listaAusentismos = null;
        listaEmpleados = null;
        listaEmpleadosAusentismo = null;
        permitirIndex = true;
        aceptar = true;
        ausentismoSeleccionado = null;
        guardado = true;
        tipoLista = 0;
        listaAusentismosBorrar = new ArrayList<Soausentismos>();
        listaAusentismosCrear = new ArrayList<Soausentismos>();
        listaAusentismosModificar = new ArrayList<Soausentismos>();
        //Crear VC
        nuevoAusentismo = new Soausentismos();
        nuevoAusentismo.setFecha(new Date());
        nuevoAusentismo.setTipo(new Tiposausentismos());
        nuevoAusentismo.setClase(new Clasesausentismos());
        nuevoAusentismo.setCausa(new Causasausentismos());
        nuevoAusentismo.setPorcentajeindividual(null);
        nuevoAusentismo.setBaseliquidacion(null);
        nuevoAusentismo.setRelacionadaBool(false);
        nuevoAusentismo.setAccidente(new Soaccidentes());
        nuevoAusentismo.setEnfermedad(new EnfermeadadesProfesionales());
        nuevoAusentismo.setDiagnosticocategoria(new Diagnosticoscategorias());
        nuevoAusentismo.setProrroga(new Soausentismos());
        nuevoAusentismo.setTercero(new Terceros());
        bandera = 0;
        banderaBotones = 0;
        banderaBotonesD = 0;
        ausentismoSeleccionado = null;
        mapParametros.put("paginaAnterior", paginaAnterior);
    }

    public void recibirPaginaEntrante(String pagina) {
        paginaAnterior = pagina;
        //inicializarCosas(); Inicializar cosas de ser necesario
    }

    public void recibirParametros(Map<String, Object> map) {
        mapParametros = map;
        paginaAnterior = (String) mapParametros.get("paginaAnterior");
        //inicializarCosas(); Inicializar cosas de ser necesario
    }

    //Reemplazar la funcion volverAtras, retornarPagina, Redirigir.....Atras.etc
    public void navegar(String pag) {
        FacesContext fc = FacesContext.getCurrentInstance();
        ControlListaNavegacion controlListaNavegacion = (ControlListaNavegacion) fc.getApplication().evaluateExpressionGet(fc, "#{controlListaNavegacion}", ControlListaNavegacion.class);
        if (pag.equals("atras")) {
            pag = paginaAnterior;
            paginaAnterior = "nominaf";
            controlListaNavegacion.quitarPagina();
        } else {
            String pagActual = "atausentismos";
            //Map<String, Object> mapParaEnviar = new LinkedHashMap<String, Object>();
            //mapParametros.put("paginaAnterior", pagActual);
            //mas Parametros
//         if (pag.equals("rastrotabla")) {
//           ControlRastro controlRastro = (ControlRastro) fc.getApplication().evaluateExpressionGet(fc, "#{controlRastro}", ControlRastro.class);
            //           controlRastro.recibirDatosTabla(conceptoSeleccionado.getSecuencia(), "Conceptos", pagActual);
            //      } else if (pag.equals("rastrotablaH")) {
            //       ControlRastro controlRastro = (ControlRastro) fc.getApplication().evaluateExpressionGet(fc, "#{controlRastro}", ControlRastro.class);
            //     controlRastro.historicosTabla("Conceptos", pagActual);
            //   pag = "rastrotabla";
            //}
            controlListaNavegacion.adicionarPagina(pagActual);
        }
        limpiarListasValor();fc.getApplication().getNavigationHandler().handleNavigation(fc, null, pag);
    }

    public void limpiarListasValor() {

    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarAusentismos.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
            listaEmpleadosAusentismo = null;
            getListaEmpleadosAusentismo();
            if (listaEmpleadosAusentismo != null) {
                if (!listaEmpleadosAusentismo.isEmpty()) {
                    seleccionMostrar = listaEmpleadosAusentismo.get(0);
                }
            }
            listaAusentismos = null;
            getListaAusentismos();
            cambiarEmpleado();

        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public String retornarPagina() {
        return paginaAnterior;
    }

    public void cambiarEmpleado() {
        if (listaAusentismosCrear.isEmpty() && listaAusentismosBorrar.isEmpty() && listaAusentismosModificar.isEmpty()) {
            secuenciaEmpleado = seleccionMostrar.getSecuencia();
            listaAusentismos = null;
            getListaAusentismos();
            contarRegistroAusentismos();
            RequestContext.getCurrentInstance().update("form:datosAusentismosEmpleado");
        }
    }

    public void limpiarListas() {
        listaAusentismosCrear.clear();
        listaAusentismosBorrar.clear();
        listaAusentismosModificar.clear();
        secuenciaEmpleado = seleccionMostrar.getSecuencia();
        listaAusentismos = null;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosAusentismosEmpleado");
    }

    public void asignarIndex(Soausentismos ausentismo, int dlg, int LND) {
        ausentismoSeleccionado = ausentismo;
        tipoActualizacion = LND;
        if (dlg == 0) {
            listaEmpleados = null;
            cargarListaEmpleados();
            contarRegistroEmpleadoLov();
            RequestContext.getCurrentInstance().update("formLovEmpleados:empleadosDialogo");
            RequestContext.getCurrentInstance().execute("PF('empleadosDialogo').show()");
        } else if (dlg == 1) {
            listaTiposAusentismos = null;
            cargarListaTiposAusentismos();
            contarRegistroTipo();
            RequestContext.getCurrentInstance().update("formLovTiposAusentismos:tiposAusentismosDialogo");
            RequestContext.getCurrentInstance().execute("PF('tiposAusentismosDialogo').show()");
        } else if (dlg == 2) {
            listaClasesAusentismos = null;
            cargarListaClasesAusentismos();
            contarRegistroClase();
            RequestContext.getCurrentInstance().update("formLOVClasesAusentismo:clasesAusentismosDialogo");
            RequestContext.getCurrentInstance().execute("PF('clasesAusentismosDialogo').show()");
        } else if (dlg == 3) {
            listaCausasAusentismos = null;
            cargarListaCausasAusentismos();
            contarRegistroCausa();
            RequestContext.getCurrentInstance().update("formLovCausasAusentismos:causasAusentismosDialogo");
            RequestContext.getCurrentInstance().execute("PF('causasAusentismosDialogo').show()");
        } else if (dlg == 4) {
            contarRegistroPorcentaje();
            RequestContext.getCurrentInstance().update("formLovPorcentajes:porcentajesDialogo");
            RequestContext.getCurrentInstance().execute("PF('porcentajesDialogo').show()");
        } else if (dlg == 5) {
            listaIBCS = null;
            cargarListaIBC();
            contarRegistroBase();
            RequestContext.getCurrentInstance().update("formLovIbcs:ibcsDialogo");
            RequestContext.getCurrentInstance().execute("PF('ibcsDialogo').show()");
        } else if (dlg == 6) {
            contarRegistroForma();
            RequestContext.getCurrentInstance().update("formLoVFormas:formasDialogo");
            RequestContext.getCurrentInstance().execute("PF('formasDialogo').show()");
        } else if (dlg == 7) {
            listaAccidentes = null;
            cargarListaAccidentes();
            contarRegistroAccidente();
            RequestContext.getCurrentInstance().update("formLovAccidentes:accidentesDialogo");
            RequestContext.getCurrentInstance().execute("PF('accidentesDialogo').show()");
        } else if (dlg == 8) {
            listaTerceros = null;
            cargarListaTerceros();
            contarRegistroTercero();
            RequestContext.getCurrentInstance().update("formLovTerceros:tercerosDialogo");
            RequestContext.getCurrentInstance().execute("PF('tercerosDialogo').show()");
        } else if (dlg == 9) {
            listaEnfermeadadesProfesionales = null;
            cargarListasEnfermedades();
            contarRegistroEnfermedad();
            RequestContext.getCurrentInstance().update("formLovEnfermedades:enfermedadesDialogo");
            RequestContext.getCurrentInstance().execute("PF('enfermedadesDialogo').show()");
        } else if (dlg == 10) {
            listaProrrogas = null;
            cargarListaProrrogas();
            contarRegistroProrroga();
            RequestContext.getCurrentInstance().update("formLovProrrogas:prorrogasDialogo");
            RequestContext.getCurrentInstance().execute("PF('prorrogasDialogo').show()");
        } else if (dlg == 11) {
            listaDiagnosticos = null;
            cargarListaDiagnosticos();
            contarRegistroDiagnostico();
            RequestContext.getCurrentInstance().update("formLovDiagnosticos:diagnosticosDialogo");
            RequestContext.getCurrentInstance().execute("PF('diagnosticosDialogo').show()");
        }
    }

    public void mostrarTodos() {

        if (!listaEmpleadosAusentismo.isEmpty()) {
            listaEmpleadosAusentismo.clear();
        }
        //listaEmpleadosNovedad = listaValEmpleados;
        if (listaEmpleadosAusentismo != null) {
            for (int i = 0; i < listaEmpleados.size(); i++) {
                listaEmpleadosAusentismo.add(listaEmpleados.get(i));
            }
        }
        seleccionMostrar = listaEmpleadosAusentismo.get(0);
        listaAusentismos = administrarAusentismos.ausentismosEmpleado(seleccionMostrar.getSecuencia());
        getListaAusentismos();
        contarRegistroEmpleado();
        contarRegistroAusentismos();
        RequestContext.getCurrentInstance().update("form:datosEmpleados");
        RequestContext.getCurrentInstance().update("form:datosAusentismosEmpleado");
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void actualizarEmpleados() {
        if (!listaEmpleadosAusentismo.isEmpty()) {
            listaEmpleadosAusentismo.clear();
            listaEmpleadosAusentismo.add(seleccionEmpleados);
            seleccionMostrar = listaEmpleadosAusentismo.get(0);
        } else {
            listaEmpleadosAusentismo.add(seleccionEmpleados);
        }
        cambiosPagina = false;
        secuenciaEmpleado = seleccionEmpleados.getSecuencia();
        listaAusentismos = null;
        getListaAusentismos();
        if (listaAusentismos != null) {
            if (!listaAusentismos.isEmpty()) {
                ausentismoSeleccionado = listaAusentismos.get(0);
            }
        }
        contarRegistroAusentismos();
        contarRegistroEmpleado();
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formLovEmpleados:LOVEmpleados:globalFilter");
        context.execute("PF('LOVEmpleados').clearFilters()");
        context.execute("PF('empleadosDialogo').hide()");
        context.update("formLovEmpleados:LOVEmpleados");
        context.update("form:datosEmpleados");
        context.update("form:datosAusentismosEmpleado");
        filtradosListaAusentismos = null;
        seleccionEmpleados = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
    }

    public void actualizarProrrogas() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            ausentismoSeleccionado.setProrroga(seleccionProrrogas);
            if (!listaAusentismosCrear.contains(ausentismoSeleccionado)) {
                if (listaAusentismosModificar.isEmpty()) {
                    listaAusentismosModificar.add(ausentismoSeleccionado);
                } else if (!listaAusentismosModificar.contains(ausentismoSeleccionado)) {
                    listaAusentismosModificar.add(ausentismoSeleccionado);
                }
            }
        } else if (tipoActualizacion == 1) {
            nuevoAusentismo.setProrroga(seleccionProrrogas);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaProrroga");
        } else if (tipoActualizacion == 2) {
            duplicarAusentismo.setProrroga(seleccionProrrogas);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarProrroga");
        }
        filtradosListaAusentismos = null;
        seleccionProrrogas = null;
        aceptar = true;
        tipoActualizacion = -1;
        cambiosPagina = false;

        context.reset("formLovProrrogas:LOVProrrogas:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVProrrogas').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('prorrogasDialogo').hide()");
        RequestContext.getCurrentInstance().update("formLovProrrogas:prorrogasDialogo");
        RequestContext.getCurrentInstance().update("formLovProrrogas:LOVProrrogas");
        RequestContext.getCurrentInstance().update("formLovProrrogas:aceptarPR");

    }

    public void actualizarTiposAusentismos() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            ausentismoSeleccionado.setTipo(seleccionTiposAusentismos);
            if (!listaAusentismosCrear.contains(ausentismoSeleccionado)) {
                if (listaAusentismosModificar.isEmpty()) {
                    listaAusentismosModificar.add(ausentismoSeleccionado);
                } else if (!listaAusentismosModificar.contains(ausentismoSeleccionado)) {
                    listaAusentismosModificar.add(ausentismoSeleccionado);
                }
            }

            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");

            }
            cambiosPagina = false;
            permitirIndex = true;
            RequestContext.getCurrentInstance().update("form:datosAusentismosEmpleado");
        } else if (tipoActualizacion == 1) {
            nuevoAusentismo.setTipo(seleccionTiposAusentismos);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoAusentismo");
        } else if (tipoActualizacion == 2) {
            duplicarAusentismo.setTipo(seleccionTiposAusentismos);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarAusentismo");

        }

        filtradoslistaTiposAusentismos = null;
        seleccionTiposAusentismos = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        context.reset("formLovTiposAusentismos:LOVTiposAusentismos:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVTiposAusentismos').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('tiposAusentismosDialogo').hide()");
        RequestContext.getCurrentInstance().update("formLovTiposAusentismos:tiposAusentismosDialogo");
        RequestContext.getCurrentInstance().update("formLovTiposAusentismos:LOVTiposAusentismos");
        RequestContext.getCurrentInstance().update("formLovTiposAusentismos:aceptarTA");

    }

    public void actualizarClasesAusentismos() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            ausentismoSeleccionado.setClase(seleccionClasesAusentismos);
            if (!listaAusentismosCrear.contains(ausentismoSeleccionado)) {
                if (listaAusentismosModificar.isEmpty()) {
                    listaAusentismosModificar.add(ausentismoSeleccionado);
                } else if (!listaAusentismosModificar.contains(ausentismoSeleccionado)) {
                    listaAusentismosModificar.add(ausentismoSeleccionado);
                }
            }

            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");

            }
            cambiosPagina = false;
            permitirIndex = true;
            RequestContext.getCurrentInstance().update("form:datosAusentismosEmpleado");
        } else if (tipoActualizacion == 1) {
            nuevoAusentismo.setClase(seleccionClasesAusentismos);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoAusentismo");
        } else if (tipoActualizacion == 2) {
            duplicarAusentismo.setClase(seleccionClasesAusentismos);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarAusentismo");
        }

        filtradoslistaClasesAusentismos = null;
        seleccionClasesAusentismos = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        context.reset("formLOVClasesAusentismo:LOVClasesAusentismos:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVClasesAusentismos').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('clasesAusentismosDialogo').hide()");
        RequestContext.getCurrentInstance().update("formLOVClasesAusentismo:clasesAusentismosDialogo");
        RequestContext.getCurrentInstance().update("formLOVClasesAusentismo:LOVClasesAusentismos");
        RequestContext.getCurrentInstance().update("formLOVClasesAusentismo:aceptarCA");

    }

    public void actualizarCausasAusentismos() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            ausentismoSeleccionado.setCausa(seleccionCausasAusentismos);
            if (!listaAusentismosCrear.contains(ausentismoSeleccionado)) {
                if (listaAusentismosModificar.isEmpty()) {
                    listaAusentismosModificar.add(ausentismoSeleccionado);
                } else if (!listaAusentismosModificar.contains(ausentismoSeleccionado)) {
                    listaAusentismosModificar.add(ausentismoSeleccionado);
                }
            }
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");

            }
            cambiosPagina = false;
            permitirIndex = true;
            RequestContext.getCurrentInstance().update("form:datosAusentismosEmpleado");
        } else if (tipoActualizacion == 1) {
            nuevoAusentismo.setCausa(seleccionCausasAusentismos);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoAusentismo");
        } else if (tipoActualizacion == 2) {
            duplicarAusentismo.setCausa(seleccionCausasAusentismos);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarAusentismo");
        }
        filtradoslistaCausasAusentismos = null;
        seleccionCausasAusentismos = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        context.reset("formLovCausasAusentismos:LOVCausasAusentismos:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVCausasAusentismos').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('causasAusentismosDialogo').hide()");
        RequestContext.getCurrentInstance().update("formLovCausasAusentismos:causasAusentismosDialogo");
        RequestContext.getCurrentInstance().update("formLovCausasAusentismos:LOVCausasAusentismos");
        RequestContext.getCurrentInstance().update("formLovCausasAusentismos:aceptarCAS");

    }

    public void actualizarPorcentajes() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            ausentismoSeleccionado.setPorcentajeindividual(new BigDecimal(seleccionPorcentajes));
            if (!listaAusentismosCrear.contains(ausentismoSeleccionado)) {
                if (listaAusentismosModificar.isEmpty()) {
                    listaAusentismosModificar.add(ausentismoSeleccionado);
                } else if (!listaAusentismosModificar.contains(ausentismoSeleccionado)) {
                    listaAusentismosModificar.add(ausentismoSeleccionado);
                }
            }
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");

            }
            cambiosPagina = false;
            permitirIndex = true;
            RequestContext.getCurrentInstance().update("form:datosAusentismosEmpleado");
        } else if (tipoActualizacion == 1) {
            nuevoAusentismo.setPorcentajeindividual(new BigDecimal(seleccionPorcentajes));
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoAusentismo");
        } else if (tipoActualizacion == 2) {
            duplicarAusentismo.setPorcentajeindividual(new BigDecimal(seleccionPorcentajes));
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarAusentismo");

        }
        filtradosListaPorcentajes = null;
        seleccionPorcentajes = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        context.reset("formLovPorcentajes:LOVPorcentajes:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVPorcentajes').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('porcentajesDialogo').hide()");
        RequestContext.getCurrentInstance().update("formLovPorcentajes:porcentajesDialogo");
        RequestContext.getCurrentInstance().update("formLovPorcentajes:LOVPorcentajes");
        RequestContext.getCurrentInstance().update("formLovPorcentajes:aceptarP");
    }

    public void actualizarIBCS() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            ausentismoSeleccionado.setBaseliquidacion(seleccionIBCS.getValor().toBigInteger());
            if (!listaAusentismosCrear.contains(ausentismoSeleccionado)) {
                if (listaAusentismosModificar.isEmpty()) {
                    listaAusentismosModificar.add(ausentismoSeleccionado);
                } else if (!listaAusentismosModificar.contains(ausentismoSeleccionado)) {
                    listaAusentismosModificar.add(ausentismoSeleccionado);
                }
            }
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");

            }
            cambiosPagina = false;
            permitirIndex = true;
            RequestContext.getCurrentInstance().update("form:datosAusentismosEmpleado");
        } else if (tipoActualizacion == 1) {
            nuevoAusentismo.setBaseliquidacion((seleccionIBCS.getValor().toBigInteger()));
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoAusentismo");
        } else if (tipoActualizacion == 2) {
            duplicarAusentismo.setBaseliquidacion((seleccionIBCS.getValor().toBigInteger()));
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarAusentismo");

        }
        filtradosListaIBCS = null;
        seleccionIBCS = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        context.reset("formLovIbcs:LOVIbcs:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVIbcs').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('ibcsDialogo').hide()");
        RequestContext.getCurrentInstance().update("formLovIbcs:ibcsDialogo");
        RequestContext.getCurrentInstance().update("formLovIbcs:LOVIbcs");
        RequestContext.getCurrentInstance().update("formLovIbcs:aceptarI");
    }

    public void actualizarEnfermedades() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            ausentismoSeleccionado.setEnfermedad(seleccionEnfermeadadesProfesionales);
            if (!listaAusentismosCrear.contains(ausentismoSeleccionado)) {
                if (listaAusentismosModificar.isEmpty()) {
                    listaAusentismosModificar.add(ausentismoSeleccionado);
                } else if (!listaAusentismosModificar.contains(ausentismoSeleccionado)) {
                    listaAusentismosModificar.add(ausentismoSeleccionado);
                }
            }
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");

            }
            cambiosPagina = false;
            permitirIndex = true;
            RequestContext.getCurrentInstance().update("form:datosAusentismosEmpleado");
        } else if (tipoActualizacion == 1) {
            nuevoAusentismo.setEnfermedad(seleccionEnfermeadadesProfesionales);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoAusentismo");
        } else if (tipoActualizacion == 2) {
            duplicarAusentismo.setEnfermedad(seleccionEnfermeadadesProfesionales);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarAusentismo");

        }
        filtradoslistaEnfermeadadesProfesionales = null;
        seleccionEnfermeadadesProfesionales = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        context.reset("formLovEnfermedades:LOVEnfermedades:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVEnfermedades').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('enfermedadesDialogo').hide()");
        RequestContext.getCurrentInstance().update("formLovEnfermedades:enfermedadesDialogo");
        RequestContext.getCurrentInstance().update("formLovEnfermedades:LOVEnfermedades");
        RequestContext.getCurrentInstance().update("formLovEnfermedades:aceptarEP");
    }

    public void actualizarFormas() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            ausentismoSeleccionado.setFormaliquidacion(seleccionForma);
            if (!listaAusentismosCrear.contains(ausentismoSeleccionado)) {
                if (listaAusentismosModificar.isEmpty()) {
                    listaAusentismosModificar.add(ausentismoSeleccionado);
                } else if (!listaAusentismosModificar.contains(ausentismoSeleccionado)) {
                    listaAusentismosModificar.add(ausentismoSeleccionado);
                }
            }
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");

            }
            cambiosPagina = false;
            permitirIndex = true;
            RequestContext.getCurrentInstance().update("form:datosAusentismosEmpleado");
        } else if (tipoActualizacion == 1) {
            nuevoAusentismo.setFormaliquidacion(seleccionForma);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoAusentismo");
        } else if (tipoActualizacion == 2) {
            duplicarAusentismo.setFormaliquidacion(seleccionForma);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarAusentismo");

        }
        filtradosListaForma = null;
        seleccionForma = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        context.reset("formLoVFormas:LOVFormas:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVFormas').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('formasDialogo').hide()");
        RequestContext.getCurrentInstance().update("formLoVFormas:formasDialogo");
        RequestContext.getCurrentInstance().update("formLoVFormas:LOVFormas");
        RequestContext.getCurrentInstance().update("formLoVFormas:aceptarFP");
    }

    public void actualizarAD() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            ausentismoSeleccionado.setAccidente(seleccionAccidentes);
            if (!listaAusentismosCrear.contains(ausentismoSeleccionado)) {
                if (listaAusentismosModificar.isEmpty()) {
                    listaAusentismosModificar.add(ausentismoSeleccionado);
                } else if (!listaAusentismosModificar.contains(ausentismoSeleccionado)) {
                    listaAusentismosModificar.add(ausentismoSeleccionado);
                }
            }
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            cambiosPagina = false;
            permitirIndex = true;
            RequestContext.getCurrentInstance().update("form:datosAusentismosEmpleado");
        } else if (tipoActualizacion == 1) {
            nuevoAusentismo.setAccidente((seleccionAccidentes));
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoAusentismo");
        } else if (tipoActualizacion == 2) {
            duplicarAusentismo.setAccidente((seleccionAccidentes));
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarAusentismo");

        }
        filtradoslistaAccidentes = null;
        seleccionAccidentes = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        context.reset("formLovAccidentes:LOVAccidentes:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVAccidentes').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('accidentesDialogo').hide()");
        RequestContext.getCurrentInstance().update("formLovAccidentes:accidentesDialogo");
        RequestContext.getCurrentInstance().update("formLovAccidentes:LOVAccidentes");
        RequestContext.getCurrentInstance().update("formLovAccidentes:aceptarAD");
    }

    public void actualizarTerceros() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            ausentismoSeleccionado.setTercero(seleccionTerceros);
            if (!listaAusentismosCrear.contains(ausentismoSeleccionado)) {
                if (listaAusentismosModificar.isEmpty()) {
                    listaAusentismosModificar.add(ausentismoSeleccionado);
                } else if (!listaAusentismosModificar.contains(ausentismoSeleccionado)) {
                    listaAusentismosModificar.add(ausentismoSeleccionado);
                }
            }
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");

            }
            cambiosPagina = false;
            permitirIndex = true;
            RequestContext.getCurrentInstance().update("form:datosAusentismosEmpleado");
        } else if (tipoActualizacion == 1) {
            nuevoAusentismo.setTercero(seleccionTerceros);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoAusentismo");
        } else if (tipoActualizacion == 2) {
            duplicarAusentismo.setTercero(seleccionTerceros);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarAusentismo");

        }
        filtradoslistaTerceros = null;
        seleccionTerceros = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        context.reset("formLovTerceros:LOVTerceros:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVTerceros').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('tercerosDialogo').hide()");
        RequestContext.getCurrentInstance().update("formLovTerceros:tercerosDialogo");
        RequestContext.getCurrentInstance().update("formLovTerceros:LOVTerceros");
        RequestContext.getCurrentInstance().update("formLovTerceros:aceptarTER");
    }

    public void actualizarDiagnosticos() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            ausentismoSeleccionado.setDiagnosticocategoria(seleccionDiagnosticos);
            if (!listaAusentismosCrear.contains(ausentismoSeleccionado)) {
                if (listaAusentismosModificar.isEmpty()) {
                    listaAusentismosModificar.add(ausentismoSeleccionado);
                } else if (!listaAusentismosModificar.contains(ausentismoSeleccionado)) {
                    listaAusentismosModificar.add(ausentismoSeleccionado);
                }
            }
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");

            }
            cambiosPagina = false;
            permitirIndex = true;
            RequestContext.getCurrentInstance().update("form:datosAusentismosEmpleado");
        } else if (tipoActualizacion == 1) {
            nuevoAusentismo.setDiagnosticocategoria(seleccionDiagnosticos);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoAusentismo");
        } else if (tipoActualizacion == 2) {
            duplicarAusentismo.setDiagnosticocategoria(seleccionDiagnosticos);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarAusentismo");

        }
        filtradoslistaDiagnosticos = null;
        seleccionDiagnosticos = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        context.reset("formLovDiagnosticos:LOVDiagnosticos:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVDiagnosticos').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('diagnosticosDialogo').hide()");
        RequestContext.getCurrentInstance().update("formLovDiagnosticos:diagnosticosDialogo");
        RequestContext.getCurrentInstance().update("formLovDiagnosticos:LOVDiagnosticos");
        RequestContext.getCurrentInstance().update("formLovDiagnosticos:aceptarD");
    }

    public void cancelarCambioDiagnosticos() {
        RequestContext context = RequestContext.getCurrentInstance();
        filtradoslistaDiagnosticos = null;
        seleccionDiagnosticos = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        permitirIndex = true;
        context.reset("formLovDiagnosticos:LOVDiagnosticos:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVDiagnosticos').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('diagnosticosDialogo').hide()");
        RequestContext.getCurrentInstance().update("formLovDiagnosticos:diagnosticosDialogo");
        RequestContext.getCurrentInstance().update("formLovDiagnosticos:LOVDiagnosticos");
        RequestContext.getCurrentInstance().update("formLovDiagnosticos:aceptarD");
    }

    public void cancelarCambioEmpleados() {
        RequestContext context = RequestContext.getCurrentInstance();
        filtradoslistaEmpleados = null;
        seleccionEmpleados = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        permitirIndex = true;
        context.reset("formLovEmpleados:LOVEmpleados:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVEmpleados').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('empleadosDialogo').hide()");
        RequestContext.getCurrentInstance().update("formLovEmpleados:empleadosDialogo");
        RequestContext.getCurrentInstance().update("formLovEmpleados:LOVEmpleados");
        RequestContext.getCurrentInstance().update("formLovEmpleados:aceptarE");
    }

    public void cancelarCambioTiposAusentismos() {
        filtradoslistaTiposAusentismos = null;
        seleccionTiposAusentismos = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formLovTiposAusentismos:LOVTiposAusentismos:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVTiposAusentismos').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('tiposAusentismosDialogo').hide()");
        RequestContext.getCurrentInstance().update("formLovTiposAusentismos:tiposAusentismosDialogo");
        RequestContext.getCurrentInstance().update("formLovTiposAusentismos:LOVTiposAusentismos");
        RequestContext.getCurrentInstance().update("formLovTiposAusentismos:aceptarTA");
    }

    public void cancelarCambioClasesAusentismos() {
        RequestContext context = RequestContext.getCurrentInstance();
        filtradoslistaClasesAusentismos = null;
        seleccionClasesAusentismos = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        permitirIndex = true;
        context.reset("formLOVClasesAusentismo:LOVClasesAusentismos:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVClasesAusentismos').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('clasesAusentismosDialogo').hide()");
        RequestContext.getCurrentInstance().update("formLOVClasesAusentismo:clasesAusentismosDialogo");
        RequestContext.getCurrentInstance().update("formLOVClasesAusentismo:LOVClasesAusentismos");
        RequestContext.getCurrentInstance().update("formLOVClasesAusentismo:aceptarCA");
    }

    public void cancelarCambioCausasAusentismos() {
        RequestContext context = RequestContext.getCurrentInstance();
        filtradoslistaCausasAusentismos = null;
        seleccionCausasAusentismos = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        permitirIndex = true;
        context.reset("formLovCausasAusentismos:LOVCausasAusentismos:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVCausasAusentismos').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('causasAusentismosDialogo').hide()");
        RequestContext.getCurrentInstance().update("formLovCausasAusentismos:causasAusentismosDialogo");
        RequestContext.getCurrentInstance().update("formLovCausasAusentismos:LOVCausasAusentismos");
        RequestContext.getCurrentInstance().update("formLovCausasAusentismos:aceptarCAS");
    }

    public void cancelarCambioPorcentajes() {
        RequestContext context = RequestContext.getCurrentInstance();
        filtradosListaPorcentajes = null;
        seleccionPorcentajes = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        permitirIndex = true;
        context.reset("formLovPorcentajes:LOVPorcentajes:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVPorcentajes').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('porcentajesDialogo').hide()");
        RequestContext.getCurrentInstance().update("formLovPorcentajes:porcentajesDialogo");
        RequestContext.getCurrentInstance().update("formLovPorcentajes:LOVPorcentajes");
        RequestContext.getCurrentInstance().update("formLovPorcentajes:aceptarP");
    }

    public void cancelarCambioFormas() {
        RequestContext context = RequestContext.getCurrentInstance();
        filtradosListaForma = null;
        seleccionForma = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        permitirIndex = true;
        context.reset("formLoVFormas:LOVFormas:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVFormas').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('formasDialogo').hide()");
        RequestContext.getCurrentInstance().update("formLoVFormas:formasDialogo");
        RequestContext.getCurrentInstance().update("formLoVFormas:LOVFormas");
        RequestContext.getCurrentInstance().update("formLoVFormas:aceptarFP");
    }

    public void cancelarCambioIBCS() {
        RequestContext context = RequestContext.getCurrentInstance();
        filtradosListaIBCS = null;
        seleccionIBCS = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        permitirIndex = true;
        context.reset("formLovIbcs:LOVIbcs:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVIbcs').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('ibcsDialogo').hide()");
        RequestContext.getCurrentInstance().update("formLovIbcs:ibcsDialogo");
        RequestContext.getCurrentInstance().update("formLovIbcs:LOVIbcs");
        RequestContext.getCurrentInstance().update("formLovIbcs:aceptarI");
    }

    public void cancelarCambioEnfermedades() {
        RequestContext context = RequestContext.getCurrentInstance();
        filtradoslistaEnfermeadadesProfesionales = null;
        seleccionEnfermeadadesProfesionales = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        permitirIndex = true;
        context.reset("formLovEnfermedades:LOVEnfermedades:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVEnfermedades').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('enfermedadesDialogo').hide()");
        RequestContext.getCurrentInstance().update("formLovEnfermedades:enfermedadesDialogo");
        RequestContext.getCurrentInstance().update("formLovEnfermedades:LOVEnfermedades");
        RequestContext.getCurrentInstance().update("formLovEnfermedades:aceptarEP");
    }

    public void cancelarCambioProrrogas() {
        RequestContext context = RequestContext.getCurrentInstance();
        filtradoslistaProrrogas = null;
        seleccionProrrogas = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        permitirIndex = true;
        context.reset("formLovProrrogas:LOVProrrogas:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVProrrogas').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('prorrogasDialogo').hide()");
        RequestContext.getCurrentInstance().update("formLovProrrogas:prorrogasDialogo");
        RequestContext.getCurrentInstance().update("formLovProrrogas:LOVProrrogas");
        RequestContext.getCurrentInstance().update("formLovProrrogas:aceptarPR");
    }

    public void cancelarCambioAD() {
        RequestContext context = RequestContext.getCurrentInstance();
        filtradoslistaAccidentes = null;
        seleccionAccidentes = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        permitirIndex = true;
        context.reset("formLovAccidentes:LOVAccidentes:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVAccidentes').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('accidentesDialogo').hide()");
        RequestContext.getCurrentInstance().update("formLovAccidentes:accidentesDialogo");
        RequestContext.getCurrentInstance().update("formLovAccidentes:LOVAccidentes");
        RequestContext.getCurrentInstance().update("formLovAccidentes:aceptarAD");
    }

    public void cancelarCambioTerceros() {
        RequestContext context = RequestContext.getCurrentInstance();
        filtradoslistaTerceros = null;
        seleccionTerceros = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        permitirIndex = true;
        context.reset("formLovTerceros:LOVTerceros:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVTerceros').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('tercerosDialogo').hide()");
        RequestContext.getCurrentInstance().update("formLovTerceros:tercerosDialogo");
        RequestContext.getCurrentInstance().update("formLovTerceros:LOVTerceros");
        RequestContext.getCurrentInstance().update("formLovTerceros:aceptarTER");
    }

    //RASTROS 
    public void verificarRastro() {
        if (ausentismoSeleccionado != null) {
            System.out.println("lol 2");
            int result = administrarRastros.obtenerTabla(ausentismoSeleccionado.getSecuencia(), "SOAUSENTISMOS");
            System.out.println("resultado: " + result);
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
        } else if (administrarRastros.verificarHistoricosTabla("SOAUSENTISMOS")) {
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
        ausentismoSeleccionado = null;
    }

    //AUTOCOMPLETAR
    public void modificarAusentismos(Soausentismos ausentismo, String confirmarCambio, String valorConfirmar) {
        ausentismoSeleccionado = ausentismo;
        System.out.println("modificarAusentismos");
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("N")) {
            if (!listaAusentismosCrear.contains(ausentismoSeleccionado)) {
                if (listaAusentismosModificar.isEmpty()) {
                    listaAusentismosModificar.add(ausentismoSeleccionado);
                } else if (!listaAusentismosModificar.contains(ausentismoSeleccionado)) {
                    listaAusentismosModificar.add(ausentismoSeleccionado);
                }
                if (guardado == true) {
                    guardado = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");

                }
            }

            RequestContext.getCurrentInstance().update("form:datosAusentismosEmpleado");
        } else if (confirmarCambio.equalsIgnoreCase("TIPO")) {
            ausentismoSeleccionado.getTipo().setDescripcion(TipoAusentismo);

            for (int i = 0; i < listaTiposAusentismos.size(); i++) {
                if (listaTiposAusentismos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                ausentismoSeleccionado.setTipo(listaTiposAusentismos.get(indiceUnicoElemento));
                listaTiposAusentismos.clear();
                getListaTiposAusentismos();
                cambiosPagina = false;
            } else {
                permitirIndex = false;
                RequestContext.getCurrentInstance().update("formLovTiposAusentismos:tiposAusentismosDialogo");
                RequestContext.getCurrentInstance().execute("PF('tiposAusentismosDialogo').show()");
                tipoActualizacion = 0;
            }
        } else if (confirmarCambio.equalsIgnoreCase("TERCERO")) {
            ausentismoSeleccionado.getTercero().setNombre(Tercero);

            for (int i = 0; i < listaAusentismos.size(); i++) {
                if (listaAusentismos.get(i).getTercero().getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                ausentismoSeleccionado.setTercero(listaTerceros.get(indiceUnicoElemento));
                listaTerceros.clear();
                getListaTerceros();
                cambiosPagina = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            } else {
                permitirIndex = false;
                RequestContext.getCurrentInstance().update("formLovTerceros:tercerosDialogo");
                RequestContext.getCurrentInstance().execute("PF('tercerosDialogo').show()");
                tipoActualizacion = 0;
            }
        } else if (confirmarCambio.equalsIgnoreCase("CLASE")) {
            ausentismoSeleccionado.getClase().setDescripcion(ClaseAusentismo);

            for (int i = 0; i < listaClasesAusentismos.size(); i++) {
                if (listaClasesAusentismos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                ausentismoSeleccionado.setClase(listaClasesAusentismos.get(indiceUnicoElemento));
                listaClasesAusentismos.clear();
                getListaClasesAusentismos();
                cambiosPagina = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            } else {
                permitirIndex = false;
                RequestContext.getCurrentInstance().update("formLOVClasesAusentismo:clasesAusentismosDialogo");
                RequestContext.getCurrentInstance().execute("PF('clasesAusentismosDialogo').show()");
                tipoActualizacion = 0;
            }
        } else if (confirmarCambio.equalsIgnoreCase("CAUSA")) {
            ausentismoSeleccionado.getCausa().setDescripcion(CausaAusentismo);

            for (int i = 0; i < listaCausasAusentismos.size(); i++) {
                if (listaCausasAusentismos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                ausentismoSeleccionado.setCausa(listaCausasAusentismos.get(indiceUnicoElemento));
                listaCausasAusentismos.clear();
                getListaCausasAusentismos();
                cambiosPagina = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            } else {
                permitirIndex = false;
                RequestContext.getCurrentInstance().update("formLovCausasAusentismos:causasAusentismosDialogo");
                RequestContext.getCurrentInstance().execute("PF('causasAusentismosDialogo').show()");
                tipoActualizacion = 0;
            }
        } else if (confirmarCambio.equalsIgnoreCase("PORCENTAJE")) {
            ausentismoSeleccionado.setPorcentajeindividual(new BigDecimal(Porcentaje));

            for (int i = 0; i < listaPorcentaje.size(); i++) {
                if ((listaPorcentaje.get(i)).toString().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                ausentismoSeleccionado.setPorcentajeindividual(new BigDecimal(listaPorcentaje.get(indiceUnicoElemento)));
                listaPorcentaje.clear();
                getListaPorcentaje();
                cambiosPagina = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            } else {
                permitirIndex = false;
                RequestContext.getCurrentInstance().update("formLovPorcentajes:porcentajesDialogo");
                RequestContext.getCurrentInstance().execute("PF('porcentajesDialogo').show()");
                tipoActualizacion = 0;
            }
        } else if (confirmarCambio.equalsIgnoreCase("BASE")) {
            ausentismoSeleccionado.setBaseliquidacion(new BigInteger(BaseLiquidacion));

            for (int i = 0; i < listaIBCS.size(); i++) {
                if ((listaIBCS.get(i)).toString().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                ausentismoSeleccionado.setBaseliquidacion(listaIBCS.get(indiceUnicoElemento).getSecuencia());
                listaIBCS.clear();
                getListaIBCS();
                cambiosPagina = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            } else {
                permitirIndex = false;
                RequestContext.getCurrentInstance().update("formLovIbcs:ibcsDialogo");
                RequestContext.getCurrentInstance().execute("PF('ibcsDialogo').show()");
                tipoActualizacion = 0;
            }
        } else if (confirmarCambio.equalsIgnoreCase("FORMA")) {
            ausentismoSeleccionado.setFormaliquidacion(Forma);

            for (int i = 0; i < listaForma.size(); i++) {
                if ((listaForma.get(i)).startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                ausentismoSeleccionado.setFormaliquidacion(listaForma.get(indiceUnicoElemento));
                listaForma.clear();
                getListaForma();
                cambiosPagina = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            } else {
                permitirIndex = false;
                RequestContext.getCurrentInstance().update("formLoVFormas:formasDialogo");
                RequestContext.getCurrentInstance().execute("PF('formasDialogo').show()");
                tipoActualizacion = 0;
            }
        } else if (confirmarCambio.equalsIgnoreCase("AD")) {
            ausentismoSeleccionado.getAccidente().setDescripcioncaso(AD);

            for (int i = 0; i < listaAccidentes.size(); i++) {
                if (listaAccidentes.get(i).getDescripcioncaso().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                ausentismoSeleccionado.setAccidente(listaAccidentes.get(indiceUnicoElemento));
                listaAccidentes.clear();
                getListaAccidentes();
                cambiosPagina = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            } else {
                permitirIndex = false;
                RequestContext.getCurrentInstance().update("formLovAccidentes:accidentesDialogo");
                RequestContext.getCurrentInstance().execute("PF('accidentesDialogo').show()");
                tipoActualizacion = 0;
            }
        } else if (confirmarCambio.equalsIgnoreCase("ENFERMEDADES")) {
            ausentismoSeleccionado.getEnfermedad().getCategoria().setDescripcion(Enfermedad);

            for (int i = 0; i < listaEnfermeadadesProfesionales.size(); i++) {
                if (listaEnfermeadadesProfesionales.get(i).getCategoria().getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                ausentismoSeleccionado.setEnfermedad(listaEnfermeadadesProfesionales.get(indiceUnicoElemento));
                listaEnfermeadadesProfesionales.clear();
                getListaEnfermeadadesProfesionales();
                cambiosPagina = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            } else {
                permitirIndex = false;
                RequestContext.getCurrentInstance().update("formLovEnfermedades:enfermedadesDialogo");
                RequestContext.getCurrentInstance().execute("PF('enfermedadesDialogo').show()");
                tipoActualizacion = 0;
            }
        } else if (confirmarCambio.equalsIgnoreCase("DIAGNOSTICO")) {
            ausentismoSeleccionado.getDiagnosticocategoria().setCodigo(Diagnostico);

            for (int i = 0; i < listaDiagnosticos.size(); i++) {
                if (listaDiagnosticos.get(i).getCodigo().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                ausentismoSeleccionado.setDiagnosticocategoria(listaDiagnosticos.get(indiceUnicoElemento));
                listaDiagnosticos.clear();
                getListaDiagnosticos();
                cambiosPagina = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            } else {
                permitirIndex = false;
                RequestContext.getCurrentInstance().update("formLovDiagnosticos:diagnosticosDialogo");
                RequestContext.getCurrentInstance().execute("PF('diagnosticosDialogo').show()");
                tipoActualizacion = 0;
            }
        } else if (confirmarCambio.equalsIgnoreCase("PRORROGA")) {
            ausentismoSeleccionado.getProrroga().setProrrogaAusentismo(Prorroga);

            for (int i = 0; i < listaProrrogas.size(); i++) {
                if (listaProrrogas.get(i).getProrrogaAusentismo().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                ausentismoSeleccionado.setProrroga(listaProrrogas.get(indiceUnicoElemento));
                listaProrrogas.clear();
                getListaProrrogas();
                cambiosPagina = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            } else {
                permitirIndex = false;
                RequestContext.getCurrentInstance().update("formLovProrrogas:prorrogasDialogo");
                RequestContext.getCurrentInstance().execute("PF('prorrogasDialogo').show()");
                tipoActualizacion = 0;
            }
        }
        if (coincidencias == 1) {
            if (!listaAusentismosCrear.contains(ausentismoSeleccionado)) {
                if (listaAusentismosModificar.isEmpty()) {
                    listaAusentismosModificar.add(ausentismoSeleccionado);
                } else if (!listaAusentismosModificar.contains(ausentismoSeleccionado)) {
                    listaAusentismosModificar.add(ausentismoSeleccionado);
                }
                if (guardado == true) {
                    guardado = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");

                }
            }
        }
        RequestContext.getCurrentInstance().update("form:datosAusentismosEmpleado");
    }

    //BORRAR Novedades
    public void borrarAusentismos() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (ausentismoSeleccionado == null) {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        } else if (ausentismoSeleccionado != null) {
            cambiosPagina = false;
            if (!listaAusentismosModificar.isEmpty() && listaAusentismosModificar.contains(ausentismoSeleccionado)) {
                int modIndex = listaAusentismosModificar.indexOf(ausentismoSeleccionado);
                listaAusentismosModificar.remove(modIndex);
                listaAusentismosBorrar.add(ausentismoSeleccionado);
            } else if (!listaAusentismosCrear.isEmpty() && listaAusentismosCrear.contains(ausentismoSeleccionado)) {
                int crearIndex = listaAusentismosCrear.indexOf(ausentismoSeleccionado);
                listaAusentismosCrear.remove(crearIndex);
            } else {
                listaAusentismosBorrar.add(ausentismoSeleccionado);
            }
            listaAusentismos.remove(ausentismoSeleccionado);
            if (tipoLista == 1) {
                filtradosListaAusentismos.remove(ausentismoSeleccionado);
            }

            RequestContext.getCurrentInstance().update("form:datosAusentismosEmpleado");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            ausentismoSeleccionado = null;

            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        }
    }

//    public void periodoAusentismo() {
//        FacesContext context = FacesContext.getCurrentInstance();
//        Map<String, String> map = context.getExternalContext().getRequestParameterMap();
//        String celda = map.get("celda"); // name attribute of node 
//        String registro = map.get("registro"); // type attribute of node 
//        int indice = Integer.parseInt(registro);
//        int columna = Integer.parseInt(celda);
//        ausentismoSeleccionado = listaAusentismos.get(indice);
//        cambiarIndice(ausentismoSeleccionado, columna);
//    }
    //Ubicacion Celda Indice Abajo. //Van los que no son NOT NULL.
    public void cambiarIndice(Soausentismos ausentismo, int celda) {
        System.out.println("Cambiar Indice");
        if (permitirIndex == true) {
            ausentismoSeleccionado = ausentismo;
            cualCelda = celda;
            secuenciaCausa = ausentismoSeleccionado.getCausa().getSecuencia();
            secuenciaAusentismo = ausentismoSeleccionado.getSecuencia();
            ausentismoSeleccionado.getSecuencia();
            Relacion = administrarAusentismos.mostrarRelacion(ausentismoSeleccionado.getSecuencia());
            if (cualCelda == 0) {
                habilitarBotonLov();
                if (ausentismoSeleccionado.getTipo() != null) {
                    TipoAusentismo = ausentismoSeleccionado.getTipo().getDescripcion();
                } else {
                    TipoAusentismo = null;
                }
            } else if (cualCelda == 1) {
                habilitarBotonLov();
                ClaseAusentismo = ausentismoSeleccionado.getClaseausentismo();
            } else if (cualCelda == 2) {
                habilitarBotonLov();
                if (ausentismoSeleccionado.getCausa() != null) {
                    CausaAusentismo = ausentismoSeleccionado.getCausa().getDescripcion();
                } else {
                    CausaAusentismo = null;
                }
            } else if (cualCelda == 3) {
                deshabilitarBotonLov();
                if (ausentismoSeleccionado.getDias() != null) {
                    Dias = ausentismoSeleccionado.getDias().toString();
                } else {
                    Dias = null;
                }
            } else if (cualCelda == 4) {
                deshabilitarBotonLov();
                if (ausentismoSeleccionado.getHoras() != null) {
                    Horas = ausentismoSeleccionado.getHoras().toString();
                } else {
                    Horas = null;
                }
            } else if (cualCelda == 6) {
                deshabilitarBotonLov();
                if (ausentismoSeleccionado.getFechafinaus() != null) {
                    Fechafinaus = ausentismoSeleccionado.getFechafinaus().toString();
                } else {
                    Fechafinaus = null;
                }
            } else if (cualCelda == 7) {
                deshabilitarBotonLov();
                if (ausentismoSeleccionado.getFechaexpedicion() != null) {
                    Fechaexpedicion = ausentismoSeleccionado.getFechaexpedicion().toString();
                } else {
                    Fechaexpedicion = null;
                }
            } else if (cualCelda == 8) {
                deshabilitarBotonLov();
                if (ausentismoSeleccionado.getFechainipago() != null) {
                    InicioPago = ausentismoSeleccionado.getFechainipago().toString();
                } else {
                    InicioPago = null;
                }
            } else if (cualCelda == 9) {
                deshabilitarBotonLov();
                if (ausentismoSeleccionado.getFechafinpago() != null) {
                    FinPago = ausentismoSeleccionado.getFechafinpago().toString();
                } else {
                    FinPago = null;
                }
            } else if (cualCelda == 10) {
                habilitarBotonLov();
                if (ausentismoSeleccionado.getPorcentajeindividual() != null) {
                    Porcentaje = ausentismoSeleccionado.getPorcentajeindividual().toString();
                } else {
                    Porcentaje = null;
                }
            } else if (cualCelda == 11) {
                habilitarBotonLov();
                if (ausentismoSeleccionado.getBaseliquidacion() != null) {
                    BaseLiquidacion = ausentismoSeleccionado.getBaseliquidacion().toString();
                } else {
                    BaseLiquidacion = null;
                }
            } else if (cualCelda == 12) {
                habilitarBotonLov();
                Forma = ausentismoSeleccionado.getFormaliquidacion();
            } else if (cualCelda == 13) {
                habilitarBotonLov();
                if (ausentismoSeleccionado.getAccidente() != null) {
                    AD = ausentismoSeleccionado.getAccidente().getDescripcioncaso();
                } else {
                    AD = null;
                }
            } else if (cualCelda == 14) {
                habilitarBotonLov();
                if (ausentismoSeleccionado.getEnfermedad() != null) {
                    Enfermedad = ausentismoSeleccionado.getEnfermedad().getCategoria().getDescripcion();
                } else {
                    Enfermedad = null;
                }
            } else if (cualCelda == 15) {
                deshabilitarBotonLov();
                NumeroCertificado = ausentismoSeleccionado.getNumerocertificado();
            } else if (cualCelda == 16) {
                habilitarBotonLov();
                if (ausentismoSeleccionado.getDiagnosticocategoria() != null) {
                    Diagnostico = ausentismoSeleccionado.getDiagnosticocategoria().getCodigo();
                } else {
                    Diagnostico = null;
                }
            } else if (cualCelda == 17) {
                habilitarBotonLov();
                if (ausentismoSeleccionado.getProrroga() != null) {
                    Prorrogas = ausentismoSeleccionado.getProrroga().getProrrogaAusentismo();
                } else {
                    Prorrogas = null;
                }
            } else if (cualCelda == 18) {
                deshabilitarBotonLov();
                Relacionn = ausentismoSeleccionado.getRelacion();
            } else if (cualCelda == 19) {
                habilitarBotonLov();
                if (ausentismoSeleccionado.getTercero() != null) {
                    Tercero = ausentismoSeleccionado.getTercero().getNombre();
                } else {
                    Tercero = null;
                }
            } else if (cualCelda == 20) {
                deshabilitarBotonLov();
                Observacion = ausentismoSeleccionado.getObservaciones();
            }
        }
    }

//EXPORTAR
    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosAusentismosExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDFTablasAnchas();
        exporter.export(context, tabla, "AusentismosPDF", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosAusentismosExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "AusentismosXLS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

//MOSTRAR DATOS CELDA
    public void editarCelda() {
        if (ausentismoSeleccionado != null) {
            editarAusentismos = ausentismoSeleccionado;
            System.out.println("Entro a editar... valor celda: " + cualCelda);
            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarTiposAusentismos");
                RequestContext.getCurrentInstance().execute("PF('editarTiposAusentismos').show()");
            } else if (cualCelda == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarClasesAusentismos");
                RequestContext.getCurrentInstance().execute("PF('editarClasesAusentismos').show()");
            } else if (cualCelda == 2) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarCausasAusentismos");
                RequestContext.getCurrentInstance().execute("PF('editarCausasAusentismos').show()");
            } else if (cualCelda == 3) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarDiasD");
                RequestContext.getCurrentInstance().execute("PF('editarDiasD').show()");
            } else if (cualCelda == 4) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarHorasD");
                RequestContext.getCurrentInstance().execute("PF('editarHorasD').show()");
            } else if (cualCelda == 5) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarFIausencias");
                RequestContext.getCurrentInstance().execute("PF('editarFIausencias').show()");
            } else if (cualCelda == 6) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarFFausencias");
                RequestContext.getCurrentInstance().execute("PF('editarFFausencias').show()");
            } else if (cualCelda == 7) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarFexpediciones");
                RequestContext.getCurrentInstance().execute("PF('editarFexpediciones').show()");
            } else if (cualCelda == 8) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarFIpagos");
                RequestContext.getCurrentInstance().execute("PF('editarFIpagos').show()");
            } else if (cualCelda == 9) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarFFpagos");
                RequestContext.getCurrentInstance().execute("PF('editarFFpagos').show()");
            } else if (cualCelda == 10) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarPorcentajes");
                RequestContext.getCurrentInstance().execute("PF('editarPorcentajes').show()");
            } else if (cualCelda == 11) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarBliquidaciones");
                RequestContext.getCurrentInstance().execute("PF('editarBliquidaciones').show()");
            } else if (cualCelda == 12) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarFliquidaciones");
                RequestContext.getCurrentInstance().execute("PF('editarFliquidaciones').show()");
            } else if (cualCelda == 13) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarAccidentes");
                RequestContext.getCurrentInstance().execute("PF('editarAccidentes').show()");
            } else if (cualCelda == 14) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarEnfermedades");
                RequestContext.getCurrentInstance().execute("PF('editarEnfermedades').show()");
            } else if (cualCelda == 15) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarNcertificados");
                RequestContext.getCurrentInstance().execute("PF('editarNcertificados').show()");
            } else if (cualCelda == 16) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarDiagnosticos");
                RequestContext.getCurrentInstance().execute("PF('editarDiagnosticos').show()");
            } else if (cualCelda == 17) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarProrrogas");
                RequestContext.getCurrentInstance().execute("PF('editarProrrogas').show()");
            } else if (cualCelda == 18) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarRelacion");
                RequestContext.getCurrentInstance().execute("PF('editarRelacion').show()");
            } else if (cualCelda == 19) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarTerceros");
                RequestContext.getCurrentInstance().execute("PF('editarTerceros').show()");
            } else if (cualCelda == 20) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarObservacionesD");
                RequestContext.getCurrentInstance().execute("PF('editarObservacionesD').show()");
            }
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void valoresBackupAutocompletar(int tipoNuevo, String Campo) {
        if (Campo.equals("TIPO")) {
            if (tipoNuevo == 1) {
                TipoAusentismo = nuevoAusentismo.getTipo().getDescripcion();
            } else if (tipoNuevo == 2) {
                TipoAusentismo = duplicarAusentismo.getTipo().getDescripcion();
            }
        } else if (Campo.equals("CLASE")) {
            if (tipoNuevo == 1) {
                ClaseAusentismo = nuevoAusentismo.getClase().getDescripcion();
            } else if (tipoNuevo == 2) {
                ClaseAusentismo = duplicarAusentismo.getClase().getDescripcion();
            }
        } else if (Campo.equals("CAUSA")) {
            if (tipoNuevo == 1) {
                CausaAusentismo = nuevoAusentismo.getCausa().getDescripcion();
            } else if (tipoNuevo == 2) {
                CausaAusentismo = duplicarAusentismo.getCausa().getDescripcion();
            }
        } else if (Campo.equals("PORCENTAJE")) {
            if (tipoNuevo == 1) {
                Porcentaje = nuevoAusentismo.getPorcentajeindividual().toString();
            } else if (tipoNuevo == 2) {
                Porcentaje = duplicarAusentismo.getPorcentajeindividual().toString();
            }
        } else if (Campo.equals("BASE")) {
            if (tipoNuevo == 1) {
                BaseLiquidacion = nuevoAusentismo.getBaseliquidacion().toString();
            } else if (tipoNuevo == 2) {
                BaseLiquidacion = duplicarAusentismo.getBaseliquidacion().toString();
            }
        } else if (Campo.equals("FORMA")) {
            if (tipoNuevo == 1) {
                Forma = nuevoAusentismo.getFormaliquidacion();
            } else if (tipoNuevo == 2) {
                Forma = duplicarAusentismo.getFormaliquidacion();
            }
        } else if (Campo.equals("AD")) {
            if (tipoNuevo == 1) {
                AD = nuevoAusentismo.getAccidente().getDescripcioncaso();
            } else if (tipoNuevo == 2) {
                AD = duplicarAusentismo.getAccidente().getDescripcioncaso();
            }
        } else if (Campo.equals("ENFERMEDADES")) {
            if (tipoNuevo == 1) {
                Enfermedad = nuevoAusentismo.getEnfermedad().getCategoria().getDescripcion();
            } else if (tipoNuevo == 2) {
                Enfermedad = duplicarAusentismo.getEnfermedad().getCategoria().getDescripcion();
            }
        } else if (Campo.equals("DIAGNOSTICO")) {
            if (tipoNuevo == 1) {
                Diagnostico = nuevoAusentismo.getDiagnosticocategoria().getCodigo();
            } else if (tipoNuevo == 2) {
                Diagnostico = duplicarAusentismo.getDiagnosticocategoria().getCodigo();
            }
        } else if (Campo.equals("PRORROGA")) {
            if (tipoNuevo == 1) {
                Prorroga = nuevoAusentismo.getProrroga().getProrrogaAusentismo();
            } else if (tipoNuevo == 2) {
                Prorroga = duplicarAusentismo.getProrroga().getProrrogaAusentismo();
            }
        } else if (Campo.equals("TERCERO")) {
            if (tipoNuevo == 1) {
                Tercero = nuevoAusentismo.getTercero().getNombre();
            } else if (tipoNuevo == 2) {
                Tercero = duplicarAusentismo.getTercero().getNombre();
            }
        }
    }

    public void autocompletarNuevoyDuplicado(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("TIPO")) {
            if (tipoNuevo == 1) {
                nuevoAusentismo.getTipo().setDescripcion(TipoAusentismo);
            } else if (tipoNuevo == 2) {
                duplicarAusentismo.getTipo().setDescripcion(TipoAusentismo);
            }
            for (int i = 0; i < listaTiposAusentismos.size(); i++) {
                if (listaTiposAusentismos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevoAusentismo.setTipo(listaTiposAusentismos.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTipo");
                } else if (tipoNuevo == 2) {
                    duplicarAusentismo.setTipo(listaTiposAusentismos.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTipo");
                }
                listaTiposAusentismos.clear();
                getListaTiposAusentismos();
            } else {
                RequestContext.getCurrentInstance().update("formLovTiposAusentismos:tiposAusentismosDialogo");
                RequestContext.getCurrentInstance().execute("PF('tiposAusentismosDialogo').show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTipo");
                } else if (tipoNuevo == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTipo");
                }
            }
        } else if (confirmarCambio.equalsIgnoreCase("CLASE")) {
            if (tipoNuevo == 1) {
                nuevoAusentismo.getClase().setDescripcion(ClaseAusentismo);
            } else if (tipoNuevo == 2) {
                duplicarAusentismo.getClase().setDescripcion(ClaseAusentismo);
            }

            for (int i = 0; i < listaClasesAusentismos.size(); i++) {
                if (listaClasesAusentismos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevoAusentismo.setClase(listaClasesAusentismos.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevaClase");
                } else if (tipoNuevo == 2) {
                    duplicarAusentismo.setClase(listaClasesAusentismos.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarClase");
                }
                listaClasesAusentismos.clear();
                getListaClasesAusentismos();
            } else {
                RequestContext.getCurrentInstance().update("formLOVClasesAusentismo:clasesAusentismosDialogo");
                RequestContext.getCurrentInstance().execute("PF('clasesAusentismosDialogo').show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevaClase");

                } else if (tipoNuevo == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarClase");

                }
            }
        } else if (confirmarCambio.equalsIgnoreCase("CAUSA")) {
            if (tipoNuevo == 1) {
                nuevoAusentismo.getCausa().setDescripcion(CausaAusentismo);
            } else if (tipoNuevo == 2) {
                duplicarAusentismo.getCausa().setDescripcion(CausaAusentismo);
            }

            for (int i = 0; i < listaCausasAusentismos.size(); i++) {
                if (listaCausasAusentismos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevoAusentismo.setCausa(listaCausasAusentismos.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevaCausa");
                } else if (tipoNuevo == 2) {
                    duplicarAusentismo.setCausa(listaCausasAusentismos.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCausa");
                }
                listaCausasAusentismos.clear();
                getListaCausasAusentismos();
            } else {
                RequestContext.getCurrentInstance().update("formLovCausasAusentismos:causasAusentismosDialogo");
                RequestContext.getCurrentInstance().execute("PF('causasAusentismosDialogo').show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevaCausa");
                } else if (tipoNuevo == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCausa");
                }
            }
        } else if (confirmarCambio.equalsIgnoreCase("PORCENTAJE")) {
            if (tipoNuevo == 1) {
                nuevoAusentismo.setPorcentajeindividual(new BigDecimal(Porcentaje));
            } else if (tipoNuevo == 2) {
                duplicarAusentismo.setPorcentajeindividual(new BigDecimal(Porcentaje));
            }

            for (int i = 0; i < listaPorcentaje.size(); i++) {
                if (listaPorcentaje.get(i).startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevoAusentismo.setPorcentajeindividual(new BigDecimal(listaPorcentaje.get(indiceUnicoElemento)));
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarPorcentaje");
                } else if (tipoNuevo == 2) {
                    duplicarAusentismo.setPorcentajeindividual(new BigDecimal(listaPorcentaje.get(indiceUnicoElemento)));
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarPorcentaje");
                }
                listaPorcentaje.clear();
                getListaPorcentaje();
            } else {
                RequestContext.getCurrentInstance().update("formLovPorcentajes:porcentajesDialogo");
                RequestContext.getCurrentInstance().execute("PF('porcentajesDialogo').show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarPorcentaje");
                } else if (tipoNuevo == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarPorcentaje");
                }
            }
        } else if (confirmarCambio.equalsIgnoreCase("BASE")) {
            if (tipoNuevo == 1) {
                nuevoAusentismo.setBaseliquidacion(new BigInteger(BaseLiquidacion));
            } else if (tipoNuevo == 2) {
                duplicarAusentismo.setBaseliquidacion(new BigInteger(BaseLiquidacion));
            }

            for (int i = 0; i < listaIBCS.size(); i++) {
                if (listaIBCS.get(i).getValor().toString().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevoAusentismo.setBaseliquidacion(listaIBCS.get(indiceUnicoElemento).getValor().toBigInteger());
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarIBCS");
                } else if (tipoNuevo == 2) {
                    duplicarAusentismo.setBaseliquidacion(listaIBCS.get(indiceUnicoElemento).getValor().toBigInteger());
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarIBCS");
                }
                listaIBCS.clear();
                getListaIBCS();
            } else {
                RequestContext.getCurrentInstance().update("formLovIbcs:ibcsDialogo");
                RequestContext.getCurrentInstance().execute("PF('ibcsDialogo').show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarIBCS");
                } else if (tipoNuevo == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarIBCS");
                }
            }
        } else if (confirmarCambio.equalsIgnoreCase("FORMA")) {
            if (tipoNuevo == 1) {
                nuevoAusentismo.setFormaliquidacion(Forma);
            } else if (tipoNuevo == 2) {
                duplicarAusentismo.setFormaliquidacion(Forma);
            }

            for (int i = 0; i < listaForma.size(); i++) {
                if (listaForma.get(i).startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevoAusentismo.setFormaliquidacion(listaForma.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevaForma");
                } else if (tipoNuevo == 2) {
                    duplicarAusentismo.setFormaliquidacion(listaForma.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarForma");
                }
                listaForma.clear();
                getListaForma();
            } else {
                RequestContext.getCurrentInstance().update("formLoVFormas:formasDialogo");
                RequestContext.getCurrentInstance().execute("PF('formasDialogo').show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevaForma");
                } else if (tipoNuevo == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarForma");
                }
            }
        } else if (confirmarCambio.equalsIgnoreCase("AD")) {
            if (tipoNuevo == 1) {
                nuevoAusentismo.getAccidente().setDescripcioncaso(AD);
            } else if (tipoNuevo == 2) {
                duplicarAusentismo.getAccidente().setDescripcioncaso(AD);
            }

            for (int i = 0; i < listaAccidentes.size(); i++) {
                if (listaAccidentes.get(i).getDescripcioncaso().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevoAusentismo.setAccidente(listaAccidentes.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarAccidente");
                } else if (tipoNuevo == 2) {
                    duplicarAusentismo.setAccidente(listaAccidentes.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarAccidente");
                }
                listaAccidentes.clear();
                getListaAccidentes();
            } else {
                RequestContext.getCurrentInstance().update("formLovAccidentes:accidentesDialogo");
                RequestContext.getCurrentInstance().execute("PF('accidentesDialogo').show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarAccidente");
                } else if (tipoNuevo == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarAccidente");
                }
            }
        } else if (confirmarCambio.equalsIgnoreCase("ENFERMEDADES")) {
            if (tipoNuevo == 1) {
                nuevoAusentismo.getEnfermedad().getCategoria().setDescripcion(Enfermedad);
            } else if (tipoNuevo == 2) {
                duplicarAusentismo.getEnfermedad().getCategoria().setDescripcion(Enfermedad);
            }

            for (int i = 0; i < listaEnfermeadadesProfesionales.size(); i++) {
                if (listaEnfermeadadesProfesionales.get(i).getCategoria().getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevoAusentismo.setEnfermedad(listaEnfermeadadesProfesionales.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevaEnfermedad");
                } else if (tipoNuevo == 2) {
                    duplicarAusentismo.setEnfermedad(listaEnfermeadadesProfesionales.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarEnfermedad");
                }
                listaAccidentes.clear();
                getListaAccidentes();
            } else {
                RequestContext.getCurrentInstance().update("formLovEnfermedades:enfermedadesDialogo");
                RequestContext.getCurrentInstance().execute("PF('enfermedadesDialogo').show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevaEnfermedad");
                } else if (tipoNuevo == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarEnfermedad");
                }
            }
        } else if (confirmarCambio.equalsIgnoreCase("DIAGNOSTICO")) {
            if (tipoNuevo == 1) {
                nuevoAusentismo.getDiagnosticocategoria().setDescripcion(Diagnostico);
            } else if (tipoNuevo == 2) {
                duplicarAusentismo.getDiagnosticocategoria().setDescripcion(Diagnostico);
            }

            for (int i = 0; i < listaDiagnosticos.size(); i++) {
                if (listaDiagnosticos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevoAusentismo.setDiagnosticocategoria(listaDiagnosticos.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarDiagnostico");
                } else if (tipoNuevo == 2) {
                    duplicarAusentismo.setDiagnosticocategoria(listaDiagnosticos.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarDiagnostico");
                }
                listaDiagnosticos.clear();
                getListaDiagnosticos();
            } else {
                RequestContext.getCurrentInstance().update("formLovDiagnosticos:diagnosticosDialogo");
                RequestContext.getCurrentInstance().execute("PF('diagnosticosDialogo').show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarDiagnostico");
                } else if (tipoNuevo == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarDiagnostico");
                }
            }
        } else if (confirmarCambio.equalsIgnoreCase("PRORROGA")) {
            if (tipoNuevo == 1) {
                nuevoAusentismo.getProrroga().setProrrogaAusentismo(Prorroga);
            } else if (tipoNuevo == 2) {
                duplicarAusentismo.getProrroga().setProrrogaAusentismo(Prorroga);
            }

            for (int i = 0; i < listaProrrogas.size(); i++) {
                if (listaProrrogas.get(i).getProrrogaAusentismo().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevoAusentismo.setProrroga(listaProrrogas.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevaProrroga");
                } else if (tipoNuevo == 2) {
                    duplicarAusentismo.setProrroga(listaProrrogas.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarProrroga");
                }
                listaProrrogas.clear();
                getListaProrrogas();
            } else {
                RequestContext.getCurrentInstance().update("formLovProrrogas:prorrogasDialogo");
                RequestContext.getCurrentInstance().execute("PF('prorrogasDialogo').show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevaProrroga");
                } else if (tipoNuevo == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarProrroga");
                }
            }
        } else if (confirmarCambio.equalsIgnoreCase("TERCERO")) {
            if (tipoNuevo == 1) {
                nuevoAusentismo.getTercero().setNombre(Tercero);
            } else if (tipoNuevo == 2) {
                duplicarAusentismo.getTercero().setNombre(Tercero);
            }

            for (int i = 0; i < listaTerceros.size(); i++) {
                if (listaTerceros.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevoAusentismo.setTercero(listaTerceros.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTercero");
                } else if (tipoNuevo == 2) {
                    duplicarAusentismo.setTercero(listaTerceros.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTercero");
                }
                listaTerceros.clear();
                getListaTerceros();
            } else {
                RequestContext.getCurrentInstance().update("formLovTerceros:tercerosDialogo");
                RequestContext.getCurrentInstance().execute("PF('tercerosDialogo').show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTercero");
                } else if (tipoNuevo == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTercero");
                }
            }
        }
    }

    //DUPLICAR ENCARGATURA
    public void duplicarA() {
        if (ausentismoSeleccionado != null) {
            duplicarAusentismo = new Soausentismos();

            duplicarAusentismo.setEmpleado(seleccionMostrar);
            duplicarAusentismo.setTipo(ausentismoSeleccionado.getTipo());
            duplicarAusentismo.setClase(ausentismoSeleccionado.getClase());
            duplicarAusentismo.setCausa(ausentismoSeleccionado.getCausa());
            duplicarAusentismo.setDias(ausentismoSeleccionado.getDias());
            duplicarAusentismo.setHoras(ausentismoSeleccionado.getHoras());
            duplicarAusentismo.setFecha(ausentismoSeleccionado.getFecha());
            duplicarAusentismo.setFechafinaus(ausentismoSeleccionado.getFechafinaus());
            duplicarAusentismo.setFechaexpedicion(ausentismoSeleccionado.getFechaexpedicion());
            duplicarAusentismo.setFechainipago(ausentismoSeleccionado.getFechainipago());
            duplicarAusentismo.setFechafinpago(ausentismoSeleccionado.getFechafinpago());
            duplicarAusentismo.setPorcentajeindividual(ausentismoSeleccionado.getPorcentajeindividual());
            duplicarAusentismo.setBaseliquidacion(ausentismoSeleccionado.getBaseliquidacion());
            duplicarAusentismo.setFormaliquidacion(ausentismoSeleccionado.getFormaliquidacion());
            duplicarAusentismo.setAccidente(ausentismoSeleccionado.getAccidente());
            duplicarAusentismo.setEnfermedad(ausentismoSeleccionado.getEnfermedad());
            duplicarAusentismo.setNumerocertificado(ausentismoSeleccionado.getNumerocertificado());
            duplicarAusentismo.setDiagnosticocategoria(ausentismoSeleccionado.getDiagnosticocategoria());
            duplicarAusentismo.setProrroga(ausentismoSeleccionado.getProrroga());
            duplicarAusentismo.setRelacion(ausentismoSeleccionado.getRelacion());
            duplicarAusentismo.setRelacionada(ausentismoSeleccionado.getRelacionada());
            duplicarAusentismo.setTercero(ausentismoSeleccionado.getTercero());
            duplicarAusentismo.setObservaciones(ausentismoSeleccionado.getObservaciones());
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarAusentismo");
            RequestContext.getCurrentInstance().execute("PF('DuplicarAusentismoEmpleado').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    //LIMPIAR NUEVO AUSENTISMO
    public void limpiarNuevoAusentismo() {
        FacesContext c = FacesContext.getCurrentInstance();
        nuevoAusentismo = new Soausentismos();
        nuevoAusentismo.setTipo(new Tiposausentismos());
        nuevoAusentismo.setCausa(new Causasausentismos());
        nuevoAusentismo.setClase(new Clasesausentismos());
        System.out.println("Entro a Bandera B. 1");
    }

//Salir NUEVO AUSENTISMO
    public void salirNuevoAusentismo() {
        FacesContext c = FacesContext.getCurrentInstance();
        nuevoAusentismo = new Soausentismos();
        nuevoAusentismo.setTipo(new Tiposausentismos());
        nuevoAusentismo.setCausa(new Causasausentismos());
        nuevoAusentismo.setClase(new Clasesausentismos());
        ausentismoSeleccionado = null;
        ausentismoSeleccionado = null;
        System.out.println("Entro a Bandera B. 1");
        botonLimpiar = (CommandButton) c.getViewRoot().findComponent("formularioDialogos:limpiar");
        botonLimpiar.setStyle("position: absolute; left: 50px; top: 400px;");
        botonAgregar = (CommandButton) c.getViewRoot().findComponent("formularioDialogos:agregarNA");
        botonAgregar.setStyle("position: absolute; left: 350px; top: 400px;");
        botonCancelar = (CommandButton) c.getViewRoot().findComponent("formularioDialogos:cancelarNA");
        botonCancelar.setStyle("position: absolute; left: 450px; top: 400px;");
        altoDialogoNuevo = "430";
        banderaBotones = 0;
        colapsado = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("formularioDialogos:NuevoAusentismoEmpleado");
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();

        System.out.println("TipoLista= " + tipoLista);
        if (bandera == 0) {
            altoTabla = "119";
            System.out.println("Activar");
            System.out.println("TipoLista= " + tipoLista);
            ATipo = (Column) c.getViewRoot().findComponent("form:datosAusentismosEmpleado:ATipo");
            ATipo.setFilterStyle("width: 85% !important");
            AClase = (Column) c.getViewRoot().findComponent("form:datosAusentismosEmpleado:AClase");
            AClase.setFilterStyle("width: 85% !important");
            ACausa = (Column) c.getViewRoot().findComponent("form:datosAusentismosEmpleado:ACausa");
            ACausa.setFilterStyle("width: 85% !important");
            ADias = (Column) c.getViewRoot().findComponent("form:datosAusentismosEmpleado:ADias");
            ADias.setFilterStyle("width: 80% !important");
            AHoras = (Column) c.getViewRoot().findComponent("form:datosAusentismosEmpleado:AHoras");
            AHoras.setFilterStyle("width: 80% !important");
            AFecha = (Column) c.getViewRoot().findComponent("form:datosAusentismosEmpleado:AFecha");
            AFecha.setFilterStyle("width: 85% !important");
            AFechaFinaus = (Column) c.getViewRoot().findComponent("form:datosAusentismosEmpleado:AFechaFinaus");
            AFechaFinaus.setFilterStyle("width: 85% !important");
            AFechaExpedicion = (Column) c.getViewRoot().findComponent("form:datosAusentismosEmpleado:AFechaExpedicion");
            AFechaExpedicion.setFilterStyle("width: 85% !important");
            AFechaInipago = (Column) c.getViewRoot().findComponent("form:datosAusentismosEmpleado:AFechaInipago");
            AFechaInipago.setFilterStyle("width: 85% !important");
            AFechaFinpago = (Column) c.getViewRoot().findComponent("form:datosAusentismosEmpleado:AFechaFinpago");
            AFechaFinpago.setFilterStyle("width: 85% !important");
            APorcentaje = (Column) c.getViewRoot().findComponent("form:datosAusentismosEmpleado:APorcentaje");
            APorcentaje.setFilterStyle("width: 80% !important");
            ABase = (Column) c.getViewRoot().findComponent("form:datosAusentismosEmpleado:ABase");
            ABase.setFilterStyle("width: 85% !important");
            AForma = (Column) c.getViewRoot().findComponent("form:datosAusentismosEmpleado:AForma");
            AForma.setFilterStyle("width: 85% !important");
            ADescripcionCaso = (Column) c.getViewRoot().findComponent("form:datosAusentismosEmpleado:ADescripcionCaso");
            ADescripcionCaso.setFilterStyle("width: 85% !important");
            AEnfermedad = (Column) c.getViewRoot().findComponent("form:datosAusentismosEmpleado:AEnfermedad");
            AEnfermedad.setFilterStyle("width: 85% !important");
            ANumero = (Column) c.getViewRoot().findComponent("form:datosAusentismosEmpleado:ANumero");
            ANumero.setFilterStyle("width: 85% !important");
            ADiagnostico = (Column) c.getViewRoot().findComponent("form:datosAusentismosEmpleado:ADiagnostico");
            ADiagnostico.setFilterStyle("width: 85% !important");
            AProrroga = (Column) c.getViewRoot().findComponent("form:datosAusentismosEmpleado:AProrroga");
            AProrroga.setFilterStyle("width: 85% !important");
            ANumero = (Column) c.getViewRoot().findComponent("form:datosAusentismosEmpleado:ANumero");
            ANumero.setFilterStyle("width: 85% !important");
            ARelacion = (Column) c.getViewRoot().findComponent("form:datosAusentismosEmpleado:ARelacion");
            ARelacion.setFilterStyle("width: 85% !important");
            ARelacionada = (Column) c.getViewRoot().findComponent("form:datosAusentismosEmpleado:ARelacionada");
            ARelacionada.setFilterStyle("width: 85% !important");
            ATercero = (Column) c.getViewRoot().findComponent("form:datosAusentismosEmpleado:ATercero");
            ATercero.setFilterStyle("width: 85% !important");
            AObservaciones = (Column) c.getViewRoot().findComponent("form:datosAusentismosEmpleado:AObservaciones");
            AObservaciones.setFilterStyle("width: 85% !important");
            RequestContext.getCurrentInstance().update("form:datosAusentismosEmpleado");
            bandera = 1;
            tipoLista = 1;
        } else if (bandera == 1) {
            altoTabla = "139";
            System.out.println("Activar");
            System.out.println("TipoLista= " + tipoLista);
            cerrarFiltrado();
        }
    }

    //LISTA DE VALORES DINAMICA
    public void listaValoresBoton() {
        if (ausentismoSeleccionado != null) {
            RequestContext context = RequestContext.getCurrentInstance();
            if (cualCelda == 0) {
                cargarListaTiposAusentismos();
                RequestContext.getCurrentInstance().update("formLovTiposAusentismos:tiposAusentismosDialogo");
                RequestContext.getCurrentInstance().execute("PF('tiposAusentismosDialogo').show()");
                tipoActualizacion = 0;
            } else if (cualCelda == 1) {
                cargarListaClasesAusentismos();
                RequestContext.getCurrentInstance().update("formLOVClasesAusentismo:clasesAusentismosDialogo");
                RequestContext.getCurrentInstance().execute("PF('clasesAusentismosDialogo').show()");
                tipoActualizacion = 0;
            } else if (cualCelda == 2) {
                cargarListaCausasAusentismos();
                RequestContext.getCurrentInstance().update("formLovCausasAusentismos:causasAusentismosDialogo");
                RequestContext.getCurrentInstance().execute("PF('causasAusentismosDialogo').show()");
                tipoActualizacion = 0;
            } else if (cualCelda == 10) {
                RequestContext.getCurrentInstance().update("formLovPorcentajes:porcentajesDialogo");
                RequestContext.getCurrentInstance().execute("PF('porcentajesDialogo').show()");
            } else if (cualCelda == 11) {
                cargarListaIBC();
                RequestContext.getCurrentInstance().update("formLovIbcs:ibcsDialogo");
                RequestContext.getCurrentInstance().execute("PF('ibcsDialogo').show()");
            } else if (cualCelda == 12) {
                RequestContext.getCurrentInstance().update("formLoVFormas:formasDialogo");
                RequestContext.getCurrentInstance().execute("PF('formasDialogo').show()");
            } else if (cualCelda == 13) {
                cargarListaAccidentes();
                RequestContext.getCurrentInstance().update("formLovAccidentes:accidentesDialogo");
                RequestContext.getCurrentInstance().execute("PF('accidentesDialogo').show()");
            } else if (cualCelda == 14) {
                cargarListasEnfermedades();
                RequestContext.getCurrentInstance().update("formLovEnfermedades:enfermedadesDialogo");
                RequestContext.getCurrentInstance().execute("PF('enfermedadesDialogo').show()");
            } else if (cualCelda == 16) {
                cargarListaDiagnosticos();
                RequestContext.getCurrentInstance().update("formLovDiagnosticos:diagnosticosDialogo");
                RequestContext.getCurrentInstance().execute("PF('diagnosticosDialogo').show()");
            } else if (cualCelda == 17) {
                cargarListaProrrogas();
                RequestContext.getCurrentInstance().update("formLovProrrogas:prorrogasDialogo");
                RequestContext.getCurrentInstance().execute("PF('prorrogasDialogo').show()");
            } else if (cualCelda == 19) {
                cargarListaTerceros();
                RequestContext.getCurrentInstance().update("formLovTerceros:tercerosDialogo");
                RequestContext.getCurrentInstance().execute("PF('tercerosDialogo').show()");
            }
        }
    }

    public void experimento(Date aux) {
        System.out.println("experimento Valor experimento : " + aux);
        System.out.println("experimento Nuevo Ausentismo Fecha Inicial: " + nuevoAusentismo.getFecha());
    }

    //CREAR NOVEDADES
    public void agregarNuevoAusentismo() {
        int pasa = 0;
        mensajeValidacion = new String();
        RequestContext context = RequestContext.getCurrentInstance();

        if (nuevoAusentismo.getFecha() == null) {
            System.out.println("Entro a Fecha ");
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            pasa++;
        }

        if (nuevoAusentismo.getTipo().getDescripcion().equals(" ") || nuevoAusentismo.getTipo().getDescripcion().equals("")) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            pasa++;
        }

        if (nuevoAusentismo.getCausa().getDescripcion().equals("")) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            pasa++;
        }
        if (nuevoAusentismo.getClase().getDescripcion().equals("")) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            pasa++;
        }

        if (nuevoAusentismo.getFormaliquidacion() == null) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            pasa++;
        }

        if (pasa != 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevoAusentismo");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevoAusentismo').show()");
        }

        if (pasa == 0) {
            if (bandera == 1) {
                altoTabla = "139";
                cerrarFiltrado();
            }
            cambiosPagina = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");

            k++;
            l = BigInteger.valueOf(k);
            nuevoAusentismo.setSecuencia(l);
            nuevoAusentismo.setEmpleado(seleccionMostrar);
            listaAusentismosCrear.add(nuevoAusentismo);
            listaAusentismos.add(nuevoAusentismo);
            ausentismoSeleccionado = nuevoAusentismo;
            contarRegistroAusentismos();
            nuevoAusentismo = new Soausentismos();
            nuevoAusentismo.setTipo(new Tiposausentismos());
            nuevoAusentismo.setClase(new Clasesausentismos());
            nuevoAusentismo.setCausa(new Causasausentismos());
            nuevoAusentismo.setPorcentajeindividual(null);
            nuevoAusentismo.setBaseliquidacion(null);
            nuevoAusentismo.setFormaliquidacion(" ");
            nuevoAusentismo.setRelacionadaBool(false);
            nuevoAusentismo.setAccidente(new Soaccidentes());
            nuevoAusentismo.setEnfermedad(new EnfermeadadesProfesionales());
            nuevoAusentismo.setDiagnosticocategoria(new Diagnosticoscategorias());
            nuevoAusentismo.setProrroga(new Soausentismos());
            nuevoAusentismo.setTercero(new Terceros());

            RequestContext.getCurrentInstance().update("form:datosAusentismosEmpleado");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            RequestContext.getCurrentInstance().execute("PF('NuevoAusentismoEmpleado').hide()");
        }
    }

    public void confirmarDuplicar() {

        int pasa = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        cambiosPagina = false;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");

        if (duplicarAusentismo.getFecha() == null) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            pasa++;
        }

        if (duplicarAusentismo.getTipo().getDescripcion().equals(" ") || duplicarAusentismo.getTipo().getDescripcion().equals("")) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            pasa++;
        }

        if (duplicarAusentismo.getCausa().getDescripcion().equals(" ")) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            pasa++;
        }
        if (duplicarAusentismo.getClase().getDescripcion().equals(" ")) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            pasa++;
        }

        if (duplicarAusentismo.getFormaliquidacion().equals(" ")) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            pasa++;
        }

        if (pasa != 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevoAusentismo");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevoAusentismo').show()");
        }
        k++;
        l = BigInteger.valueOf(k);
        duplicarAusentismo.setSecuencia(l);
        listaAusentismosCrear.add(duplicarAusentismo);
        listaAusentismos.add(duplicarAusentismo);
        ausentismoSeleccionado = duplicarAusentismo;
        contarRegistroAusentismos();
        RequestContext.getCurrentInstance().update("form:datosAusentismosEmpleado");
        if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }

        if (bandera == 1) {
            System.out.println("Activar");
            System.out.println("TipoLista= " + tipoLista);
            altoTabla = "139";
            cerrarFiltrado();
        }
        duplicarAusentismo.setEmpleado(seleccionMostrar);
        duplicarAusentismo = new Soausentismos();
        RequestContext.getCurrentInstance().update("formularioDialogos:DuplicarAusentismoEmpleado");
        RequestContext.getCurrentInstance().execute("PF('DuplicarAusentismoEmpleado').hide()");
    }

    //LIMPIAR DUPLICAR
    /**
     * Metodo que limpia los datos de un duplicar Ausentismos
     */
    public void limpiarduplicarAusentismos() {
        duplicarAusentismo = new Soausentismos();
        duplicarAusentismo.setTipo(new Tiposausentismos());
        duplicarAusentismo.setCausa(new Causasausentismos());
        duplicarAusentismo.setClase(new Clasesausentismos());
    }

    public void salirduplicarAusentismos() {
        duplicarAusentismo = new Soausentismos();
        duplicarAusentismo.setTipo(new Tiposausentismos());
        duplicarAusentismo.setCausa(new Causasausentismos());
        duplicarAusentismo.setClase(new Clasesausentismos());
        ausentismoSeleccionado = null;
        FacesContext c = FacesContext.getCurrentInstance();
        System.out.println("Entro a Bandera B. 1");
        botonLimpiarD = (CommandButton) c.getViewRoot().findComponent("formularioDialogos:limpiarDuplicado");
        botonLimpiarD.setStyle("position: absolute; left: 50px; top: 400px;");
        botonAgregarD = (CommandButton) c.getViewRoot().findComponent("formularioDialogos:agregarNADuplicado");
        botonAgregarD.setStyle("position: absolute; left: 350px; top: 400px;");
        botonCancelarD = (CommandButton) c.getViewRoot().findComponent("formularioDialogos:cancelarNADuplicado");
        botonCancelarD.setStyle("position: absolute; left: 450px; top: 400px;");
        altoDialogoDuplicar = "430";
        banderaBotonesD = 0;
        colapsado = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("formularioDialogos:DuplicarAusentismoEmpleado");
    }

    //GUARDAR
    public void guardarCambiosAusentismos() {

        if (guardado == false) {
            System.out.println("Realizando Operaciones Ausentismos");

            if (!listaAusentismosBorrar.isEmpty()) {
                for (int i = 0; i < listaAusentismosBorrar.size(); i++) {
                    System.out.println("Borrando..." + listaAusentismosBorrar.size());

                    if (listaAusentismosBorrar.get(i).getDias() == null) {
                        listaAusentismosBorrar.get(i).setDias(null);
                    }
                    if (listaAusentismosBorrar.get(i).getHoras() == null) {
                        listaAusentismosBorrar.get(i).setHoras(null);
                    }
                    if (listaAusentismosBorrar.get(i).getFechafinaus() == null) {
                        listaAusentismosBorrar.get(i).setFechafinaus(null);
                    }
                    if (listaAusentismosBorrar.get(i).getFechaexpedicion() == null) {
                        listaAusentismosBorrar.get(i).setFechaexpedicion(null);
                    }
                    if (listaAusentismosBorrar.get(i).getFechainipago() == null) {
                        listaAusentismosBorrar.get(i).setFechainipago(null);
                    }
                    if (listaAusentismosBorrar.get(i).getFechafinpago() == null) {
                        listaAusentismosBorrar.get(i).setFechafinpago(null);
                    }
                    if (listaAusentismosBorrar.get(i).getPorcentajeindividual() == null) {
                        listaAusentismosBorrar.get(i).setPorcentajeindividual(null);
                    }
                    if (listaAusentismosBorrar.get(i).getBaseliquidacion() == null) {
                        listaAusentismosBorrar.get(i).setBaseliquidacion(null);
                    }
                    if (listaAusentismosBorrar.get(i).getFormaliquidacion() == null) {
                        listaAusentismosBorrar.get(i).setFormaliquidacion(null);
                    }
                    if (listaAusentismosBorrar.get(i).getAccidente().getSecuencia() == null) {
                        listaAusentismosBorrar.get(i).setAccidente(null);
                    }
                    if (listaAusentismosBorrar.get(i).getEnfermedad().getSecuencia() == null) {
                        listaAusentismosBorrar.get(i).setEnfermedad(null);
                    }
                    if (listaAusentismosBorrar.get(i).getNumerocertificado() == null) {
                        listaAusentismosBorrar.get(i).setNumerocertificado(null);
                    }
                    if (listaAusentismosBorrar.get(i).getDiagnosticocategoria().getSecuencia() == null) {
                        listaAusentismosBorrar.get(i).setDiagnosticocategoria(null);
                    }
                    if (listaAusentismosBorrar.get(i).getProrroga().getSecuencia() == null) {
                        listaAusentismosBorrar.get(i).setProrroga(null);
                    }
                    if (listaAusentismosBorrar.get(i).getRelacion() == null) {
                        listaAusentismosBorrar.get(i).setRelacion(null);
                    }
                    /*
                     * if (listaAusentismosBorrar.get(i).getRelacionadaBool()==
                     * false) {
                     * listaAusentismosBorrar.get(i).setRelacionada("N"); }
                     */
                    if (listaAusentismosBorrar.get(i).getTercero().getSecuencia() == null) {
                        listaAusentismosBorrar.get(i).setTercero(null);
                    }
                    if (listaAusentismosBorrar.get(i).getObservaciones() == null) {
                        listaAusentismosBorrar.get(i).setObservaciones(null);
                    }
                    administrarAusentismos.borrarAusentismos(listaAusentismosBorrar.get(i));
                }
                System.out.println("Entra");
                listaAusentismosBorrar.clear();
            }

            if (!listaAusentismosCrear.isEmpty()) {
                for (int i = 0; i < listaAusentismosCrear.size(); i++) {
                    System.out.println("Creando...");

                    if (listaAusentismosCrear.get(i).getDias() == null) {
                        listaAusentismosCrear.get(i).setDias(null);
                    }
                    if (listaAusentismosCrear.get(i).getHoras() == null) {
                        listaAusentismosCrear.get(i).setHoras(null);
                    }
                    if (listaAusentismosCrear.get(i).getFechafinaus() == null) {
                        listaAusentismosCrear.get(i).setFechafinaus(null);
                    }
                    if (listaAusentismosCrear.get(i).getFechaexpedicion() == null) {
                        listaAusentismosCrear.get(i).setFechaexpedicion(null);
                    }
                    if (listaAusentismosCrear.get(i).getFechainipago() == null) {
                        listaAusentismosCrear.get(i).setFechainipago(null);
                    }
                    if (listaAusentismosCrear.get(i).getFechafinpago() == null) {
                        listaAusentismosCrear.get(i).setFechafinpago(null);
                    }
                    if (listaAusentismosCrear.get(i).getPorcentajeindividual() == null) {
                        listaAusentismosCrear.get(i).setPorcentajeindividual(null);
                    }
                    if (listaAusentismosCrear.get(i).getBaseliquidacion() == null) {
                        listaAusentismosCrear.get(i).setBaseliquidacion(null);
                    }
                    if (listaAusentismosCrear.get(i).getFormaliquidacion() == null) {
                        listaAusentismosCrear.get(i).setFormaliquidacion(null);
                    }
                    if (listaAusentismosCrear.get(i).getAccidente().getSecuencia() == null) {
                        listaAusentismosCrear.get(i).setAccidente(null);
                    }
                    if (listaAusentismosCrear.get(i).getEnfermedad().getSecuencia() == null) {
                        listaAusentismosCrear.get(i).setEnfermedad(null);
                    }
                    if (listaAusentismosCrear.get(i).getNumerocertificado() == null) {
                        listaAusentismosCrear.get(i).setNumerocertificado(null);
                    }
                    if (listaAusentismosCrear.get(i).getDiagnosticocategoria().getSecuencia() == null) {
                        listaAusentismosCrear.get(i).setDiagnosticocategoria(null);
                    }
                    if (listaAusentismosCrear.get(i).getProrroga().getSecuencia() == null) {
                        listaAusentismosCrear.get(i).setProrroga(null);
                    }
                    if (listaAusentismosCrear.get(i).getRelacion() == null) {
                        listaAusentismosCrear.get(i).setRelacion(null);
                    }
                    if (listaAusentismosCrear.get(i).getRelacionadaBool() == false) {
                        listaAusentismosCrear.get(i).setRelacionada("N");
                    }
                    if (listaAusentismosCrear.get(i).getTercero().getSecuencia() == null) {
                        listaAusentismosCrear.get(i).setTercero(null);
                    }
                    if (listaAusentismosCrear.get(i).getObservaciones() == null) {
                        listaAusentismosCrear.get(i).setObservaciones(null);
                    }
                    administrarAusentismos.crearAusentismos(listaAusentismosCrear.get(i));
                }
                System.out.println("LimpiaLista");
                listaAusentismosCrear.clear();
            }
            if (!listaAusentismosModificar.isEmpty()) {
                administrarAusentismos.modificarAusentismos(listaAusentismosModificar);
                listaAusentismosModificar.clear();
            }

            System.out.println("Se guardaron los datos con exito");
            listaAusentismos = null;
            getListaAusentismos();
            contarRegistroAusentismos();
            RequestContext context = RequestContext.getCurrentInstance();
            cambiosPagina = true;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosAusentismosEmpleado");
            guardado = true;
            ausentismoSeleccionado = null;
            permitirIndex = true;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
        System.out.println("Tamaño lista: " + listaAusentismos.size());
        System.out.println("Valor k: " + k);
    }

    //CANCELAR MODIFICACIONES
    public void cancelarModificacion() {
        if (bandera == 1) {
            altoTabla = "139";
            cerrarFiltrado();
        }
        FacesContext d = FacesContext.getCurrentInstance();

        botonLimpiar = (CommandButton) d.getViewRoot().findComponent("formularioDialogos:limpiar");
        botonLimpiar.setStyle("position: absolute; left: 50px; top: 400px;");
        botonAgregar = (CommandButton) d.getViewRoot().findComponent("formularioDialogos:agregarNA");
        botonAgregar.setStyle("position: absolute; left: 350px; top: 400px;");
        botonCancelar = (CommandButton) d.getViewRoot().findComponent("formularioDialogos:cancelarNA");
        botonCancelar.setStyle("position: absolute; left: 450px; top: 400px;");
        altoDialogoNuevo = "430";
        botonLimpiarD = (CommandButton) d.getViewRoot().findComponent("formularioDialogos:limpiarDuplicado");
        botonLimpiarD.setStyle("position: absolute; left: 50px; top: 400px;");
        botonAgregarD = (CommandButton) d.getViewRoot().findComponent("formularioDialogos:agregarNADuplicado");
        botonAgregarD.setStyle("position: absolute; left: 350px; top: 400px;");
        botonCancelarD = (CommandButton) d.getViewRoot().findComponent("formularioDialogos:cancelarNADuplicado");
        botonCancelarD.setStyle("position: absolute; left: 450px; top: 400px;");
        altoDialogoDuplicar = "430";
        banderaBotonesD = 0;
        banderaBotones = 0;
        listaAusentismosBorrar.clear();
        listaAusentismosCrear.clear();
        listaAusentismosModificar.clear();
        colapsado = true;
        seleccionEmpleados = null;
        seleccionMostrar = null;
        ausentismoSeleccionado = null;
        listaAusentismos = null;
        getListaAusentismos();
        contarRegistroAusentismos();
        guardado = true;
        permitirIndex = true;
        cambiosPagina = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("form:datosAusentismosEmpleado");
        RequestContext.getCurrentInstance().update("formularioDialogos:nuevoAusentismo");
        RequestContext.getCurrentInstance().update("formularioDialogos:duplicarAusentismo");
        RequestContext.getCurrentInstance().update("formularioDialogos:NuevoAusentismoEmpleado");
        RequestContext.getCurrentInstance().update("formularioDialogos:DuplicarAusentismoEmpleado");

    }

    public void cambiosToggle() {
        System.out.println("cambiosToggle");
        FacesContext c = FacesContext.getCurrentInstance();
        if (banderaBotones == 0) {
            System.out.println("Entro a Bandera B. 0");
            botonLimpiar = (CommandButton) c.getViewRoot().findComponent("formularioDialogos:limpiar");
            botonLimpiar.setStyle("position: absolute; left: 50px; top: 570px;");
            botonAgregar = (CommandButton) c.getViewRoot().findComponent("formularioDialogos:agregarNA");
            botonAgregar.setStyle("position: absolute; left: 350px; top: 570px;");
            botonCancelar = (CommandButton) c.getViewRoot().findComponent("formularioDialogos:cancelarNA");
            botonCancelar.setStyle("position: absolute; left: 450px; top: 570px;");
            altoDialogoNuevo = "600";
            banderaBotones = 1;
        } else if (banderaBotones == 1) {
            System.out.println("Entro a Bandera B. 1");
            botonLimpiar = (CommandButton) c.getViewRoot().findComponent("formularioDialogos:limpiar");
            botonLimpiar.setStyle("position: absolute; left: 50px; top: 400px;");
            botonAgregar = (CommandButton) c.getViewRoot().findComponent("formularioDialogos:agregarNA");
            botonAgregar.setStyle("position: absolute; left: 350px; top: 400px;");
            botonCancelar = (CommandButton) c.getViewRoot().findComponent("formularioDialogos:cancelarNA");
            botonCancelar.setStyle("position: absolute; left: 450px; top: 400px;");
            altoDialogoNuevo = "430";
            banderaBotones = 0;
        }

        RequestContext context = RequestContext.getCurrentInstance();
        //RequestContext.getCurrentInstance().update("formularioDialogos:limpiar");
        RequestContext.getCurrentInstance().update("formularioDialogos:nuevoAusentismo");
        RequestContext.getCurrentInstance().update("formularioDialogos:NuevoAusentismoEmpleado");
        RequestContext.getCurrentInstance().execute("PF('NuevoAusentismoEmpleado').show()");
    }

    public void cambiosToggleD() {
        System.out.println("cambiosToggle");
        FacesContext c = FacesContext.getCurrentInstance();
        if (banderaBotonesD == 0) {
            System.out.println("Entro a Bandera B. 0");
            botonLimpiarD = (CommandButton) c.getViewRoot().findComponent("formularioDialogos:limpiarDuplicado");
            botonLimpiarD.setStyle("position: absolute; left: 50px; top: 570px;");
            botonAgregarD = (CommandButton) c.getViewRoot().findComponent("formularioDialogos:agregarNADuplicado");
            botonAgregarD.setStyle("position: absolute; left: 350px; top: 570px;");
            botonCancelarD = (CommandButton) c.getViewRoot().findComponent("formularioDialogos:cancelarNADuplicado");
            botonCancelarD.setStyle("position: absolute; left: 450px; top: 570px;");
            altoDialogoDuplicar = "530";
            banderaBotonesD = 1;
        } else if (banderaBotonesD == 1) {
            System.out.println("Entro a Bandera B. 1");
            botonLimpiarD = (CommandButton) c.getViewRoot().findComponent("formularioDialogos:limpiarDuplicado");
            botonLimpiarD.setStyle("position: absolute; left: 50px; top: 400px;");
            botonAgregarD = (CommandButton) c.getViewRoot().findComponent("formularioDialogos:agregarNADuplicado");
            botonAgregarD.setStyle("position: absolute; left: 350px; top: 400px;");
            botonCancelarD = (CommandButton) c.getViewRoot().findComponent("formularioDialogos:cancelarNADuplicado");
            botonCancelarD.setStyle("position: absolute; left: 450px; top: 400px;");
            altoDialogoDuplicar = "430";
            banderaBotonesD = 0;
        }

        RequestContext context = RequestContext.getCurrentInstance();
        //RequestContext.getCurrentInstance().update("formularioDialogos:limpiar");
        RequestContext.getCurrentInstance().update("formularioDialogos:duplicarAusentismo");
        RequestContext.getCurrentInstance().update("formularioDialogos:DuplicarAusentismoEmpleado");
        RequestContext.getCurrentInstance().execute("PF('DuplicarAusentismoEmpleado').show()");
    }

    //SALIR
    public void salir() {  limpiarListasValor();
        if (bandera == 1) {
            altoTabla = "139";
            cerrarFiltrado();
        }
        listaAusentismosBorrar.clear();
        listaAusentismosCrear.clear();
        listaAusentismosModificar.clear();
        ausentismoSeleccionado = null;
        listaAusentismos = null;
        listaEmpleados = null;
        listaEmpleadosAusentismo = null;
        guardado = true;
        cambiosPagina = true;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosAusentismosEmpleado");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void cerrarFiltrado() {
        FacesContext c = FacesContext.getCurrentInstance();
        ATipo = (Column) c.getViewRoot().findComponent("form:datosAusentismosEmpleado:ATipo");
        ATipo.setFilterStyle("display: none; visibility: hidden;");
        AClase = (Column) c.getViewRoot().findComponent("form:datosAusentismosEmpleado:AClase");
        AClase.setFilterStyle("display: none; visibility: hidden;");
        ACausa = (Column) c.getViewRoot().findComponent("form:datosAusentismosEmpleado:ACausa");
        ACausa.setFilterStyle("display: none; visibility: hidden;");
        ADias = (Column) c.getViewRoot().findComponent("form:datosAusentismosEmpleado:ADias");
        ADias.setFilterStyle("display: none; visibility: hidden;");
        AHoras = (Column) c.getViewRoot().findComponent("form:datosAusentismosEmpleado:AHoras");
        AHoras.setFilterStyle("display: none; visibility: hidden;");
        AFecha = (Column) c.getViewRoot().findComponent("form:datosAusentismosEmpleado:AFecha");
        AFecha.setFilterStyle("display: none; visibility: hidden;");
        AFechaFinaus = (Column) c.getViewRoot().findComponent("form:datosAusentismosEmpleado:AFechaFinaus");
        AFechaFinaus.setFilterStyle("display: none; visibility: hidden;");
        AFechaExpedicion = (Column) c.getViewRoot().findComponent("form:datosAusentismosEmpleado:AFechaExpedicion");
        AFechaExpedicion.setFilterStyle("display: none; visibility: hidden;");
        AFechaInipago = (Column) c.getViewRoot().findComponent("form:datosAusentismosEmpleado:AFechaInipago");
        AFechaInipago.setFilterStyle("display: none; visibility: hidden;");
        AFechaFinpago = (Column) c.getViewRoot().findComponent("form:datosAusentismosEmpleado:AFechaFinpago");
        AFechaFinpago.setFilterStyle("display: none; visibility: hidden;");
        APorcentaje = (Column) c.getViewRoot().findComponent("form:datosAusentismosEmpleado:APorcentaje");
        APorcentaje.setFilterStyle("display: none; visibility: hidden;");
        ABase = (Column) c.getViewRoot().findComponent("form:datosAusentismosEmpleado:ABase");
        ABase.setFilterStyle("display: none; visibility: hidden;");
        AForma = (Column) c.getViewRoot().findComponent("form:datosAusentismosEmpleado:AForma");
        AForma.setFilterStyle("display: none; visibility: hidden;");
        ADescripcionCaso = (Column) c.getViewRoot().findComponent("form:datosAusentismosEmpleado:ADescripcionCaso");
        ADescripcionCaso.setFilterStyle("display: none; visibility: hidden;");
        AEnfermedad = (Column) c.getViewRoot().findComponent("form:datosAusentismosEmpleado:AEnfermedad");
        AEnfermedad.setFilterStyle("display: none; visibility: hidden;");
        ANumero = (Column) c.getViewRoot().findComponent("form:datosAusentismosEmpleado:ANumero");
        ANumero.setFilterStyle("display: none; visibility: hidden;");
        ADiagnostico = (Column) c.getViewRoot().findComponent("form:datosAusentismosEmpleado:ADiagnostico");
        ADiagnostico.setFilterStyle("display: none; visibility: hidden;");
        AProrroga = (Column) c.getViewRoot().findComponent("form:datosAusentismosEmpleado:AProrroga");
        AProrroga.setFilterStyle("display: none; visibility: hidden;");
        ANumero = (Column) c.getViewRoot().findComponent("form:datosAusentismosEmpleado:ANumero");
        ANumero.setFilterStyle("display: none; visibility: hidden;");
        ARelacion = (Column) c.getViewRoot().findComponent("form:datosAusentismosEmpleado:ARelacion");
        ARelacion.setFilterStyle("display: none; visibility: hidden;");
        ARelacionada = (Column) c.getViewRoot().findComponent("form:datosAusentismosEmpleado:ARelacionada");
        ARelacionada.setFilterStyle("display: none; visibility: hidden;");
        ATercero = (Column) c.getViewRoot().findComponent("form:datosAusentismosEmpleado:ATercero");
        ATercero.setFilterStyle("display: none; visibility: hidden;");
        AObservaciones = (Column) c.getViewRoot().findComponent("form:datosAusentismosEmpleado:AObservaciones");
        AObservaciones.setFilterStyle("display: none; visibility: hidden;");
        RequestContext.getCurrentInstance().update("form:datosAusentismosEmpleado");
        bandera = 0;
        filtradosListaAusentismos = null;
        tipoLista = 0;
    }

    public void eventoFiltrar() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
        contarRegistroAusentismos();
    }

    public void contarRegistroAusentismos() {
        RequestContext.getCurrentInstance().update("form:infoRegistroAusentismos");
    }

    public void contarRegistroEmpleado() {
        RequestContext.getCurrentInstance().update("form:infoRegistroEmpleados");
    }

    public void contarRegistroEmpleadoLov() {
        RequestContext.getCurrentInstance().update("formLovEmpleados:infoRegistroEmpleadoLOV");
    }

    public void contarRegistroAccidente() {
        RequestContext.getCurrentInstance().update("formLovAccidentes:infoRegistroAccidente");
    }

    public void contarRegistroDiagnostico() {
        RequestContext.getCurrentInstance().update("formLovDiagnosticos:infoRegistroDiagnostico");
    }

    public void contarRegistroProrroga() {
        RequestContext.getCurrentInstance().update("formLovProrrogas:infoRegistroProrroga");
    }

    public void contarRegistroTercero() {
        RequestContext.getCurrentInstance().update("formLovTerceros:infoRegistroTercero");
    }

    public void contarRegistroEnfermedad() {
        RequestContext.getCurrentInstance().update("formLovEnfermedades:infoRegistroEnfermedad");
    }

    public void contarRegistroTipo() {
        RequestContext.getCurrentInstance().update("formLovTiposAusentismos:infoRegistroTipos");
    }

    public void contarRegistroClase() {
        RequestContext.getCurrentInstance().update("formLOVClasesAusentismo:infoRegistroClases");
    }

    public void contarRegistroCausa() {
        RequestContext.getCurrentInstance().update("formLovCausasAusentismos:infoRegistroCausas");
    }

    public void contarRegistroPorcentaje() {
        RequestContext.getCurrentInstance().update("formLovPorcentajes:infoRegistroPorcentaje");
    }

    public void contarRegistroBase() {
        RequestContext.getCurrentInstance().update("formLovIbcs:infoRegistroIBCS");
    }

    public void contarRegistroForma() {
        RequestContext.getCurrentInstance().update("formLoVFormas:infoRegistroForma");
    }

    public void habilitarBotonLov() {
        activarLov = false;
        RequestContext.getCurrentInstance().update("form:listaValores");
    }

    public void deshabilitarBotonLov() {
        activarLov = true;
        RequestContext.getCurrentInstance().update("form:listaValores");
    }

    public void recordarSeleccion() {
        if (ausentismoSeleccionado != null) {
            FacesContext c = FacesContext.getCurrentInstance();
            tablaC = (DataTable) c.getViewRoot().findComponent("form:datosAusentismosEmpleado");
            tablaC.setSelection(ausentismoSeleccionado);
        }
    }

    public void autocompletarFechas(Date fechaInicial, BigInteger dias, int tipo) {
        System.out.println("Controlador.ControlAusentismos.autocompletarFechas()");
        System.out.println("fecha Inicial : " + fechaInicial);
        System.out.println("dias : " + dias);
        Calendar cd = Calendar.getInstance();
        cd.setTime(fechaInicial);
        cd.add(Calendar.DAY_OF_MONTH, (dias.subtract(BigInteger.ONE).intValue()));
        Date fechaH = cd.getTime();
        System.out.println("fecha Hasta : " + fechaH);
        if (tipo == 1) {
            nuevoAusentismo.setFechafinaus(fechaH);
            nuevoAusentismo.setFechaexpedicion(fechaInicial);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaFechaExpedicion");
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaFechaFinaus");
        } else if (tipo == 2) {
            duplicarAusentismo.setFechafinaus(fechaH);
            duplicarAusentismo.setFechaexpedicion(fechaInicial);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarFechaExpedicion");
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarFechaFinaus");
        }
    }

    public void autocompletarFechapago(Date fechaInicioPago, BigInteger dias, int tipo) {
        System.out.println("Controlador.ControlAusentismos.autocompletarFechapago()");
        Calendar aux = Calendar.getInstance();
        aux.setTime(fechaInicioPago);
        aux.add(Calendar.DAY_OF_MONTH, (dias.subtract(BigInteger.ONE).intValue()));
        Date fechaH = aux.getTime();
        System.out.println("fecha Hasta : " + fechaH);
        if (tipo == 1) {
            nuevoAusentismo.setFechafinpago(fechaH);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaFechaFinalPago");
        } else if (tipo == 2) {
            duplicarAusentismo.setFechafinpago(fechaH);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarFechaFinPago");
        }
    }

    public void cargarListaIBC() {
        if (listaIBCS == null) {
            listaIBCS = administrarAusentismos.empleadosIBCS(secuenciaEmpleado);
        }
    }

    public void cargarListaEmpleados() {
        if (listaEmpleados == null) {
            listaEmpleados = administrarAusentismos.lovEmpleados();
        }
    }

    public void cargarListaTiposAusentismos() {
        if (listaTiposAusentismos == null) {
            listaTiposAusentismos = administrarAusentismos.lovTiposAusentismos();
        }

    }

    public void cargarListaClasesAusentismos() {
        if (listaClasesAusentismos == null) {
            listaClasesAusentismos = administrarAusentismos.lovClasesAusentismos();
        }
    }

    public void cargarListaCausasAusentismos() {
        if (listaCausasAusentismos == null) {
            listaCausasAusentismos = administrarAusentismos.lovCausasAusentismos();
        }
    }

    public void cargarListaAccidentes() {
        if (listaAccidentes == null) {
            listaAccidentes = administrarAusentismos.lovAccidentes(secuenciaEmpleado);
        }

    }

    public void cargarListasEnfermedades() {
        if (listaEnfermeadadesProfesionales == null) {
            listaEnfermeadadesProfesionales = administrarAusentismos.empleadosEP(secuenciaEmpleado);
        }
    }

    public void cargarListaTerceros() {
        if (listaTerceros == null) {
            listaTerceros = administrarAusentismos.lovTerceros();
        }
    }

    public void cargarListaDiagnosticos() {
        if (listaDiagnosticos == null) {
            listaDiagnosticos = administrarAusentismos.lovDiagnosticos();
        }
    }

    public void cargarListaProrrogas() {
        if (listaProrrogas == null) {
            listaProrrogas = administrarAusentismos.lovProrrogas(secuenciaEmpleado, secuenciaCausa, secuenciaAusentismo);
        }
    }

    //GETTER & SETTER
    public List<Soausentismos> getListaAusentismos() {
        if (listaAusentismos == null) {
            if (secuenciaEmpleado != null) {
                listaAusentismos = administrarAusentismos.ausentismosEmpleado(secuenciaEmpleado);
                if (listaAusentismos != null) {
                    if (!listaAusentismos.isEmpty()) {
                        for (int i = 0; i < listaAusentismos.size(); i++) {
                            listaAusentismos.get(i).setRelacion(administrarAusentismos.mostrarRelacion(listaAusentismos.get(i).getSecuencia()));
                        }
                    }
                }
            }
        }
        return listaAusentismos;
    }

    public void setListaAusentismos(List<Soausentismos> listaAusentismos) {
        this.listaAusentismos = listaAusentismos;
    }

    public List<Soausentismos> getFiltradosListaAusentismos() {
        return filtradosListaAusentismos;
    }

    public void setFiltradosListaAusentismos(List<Soausentismos> filtradosListaAusentismos) {
        this.filtradosListaAusentismos = filtradosListaAusentismos;
    }

    public List<Empleados> getListaEmpleadosAusentismo() {
        if (listaEmpleadosAusentismo == null) {
            listaEmpleadosAusentismo = administrarAusentismos.lovEmpleados();
        }
        return listaEmpleadosAusentismo;
    }

    public void setListaEmpleadosAusentismo(List<Empleados> listaEmpleadosAusentismo) {
        this.listaEmpleadosAusentismo = listaEmpleadosAusentismo;
    }

    public List<Empleados> getFiltradosListaEmpleadosAusentismo() {
        return filtradosListaEmpleadosAusentismo;
    }

    public void setFiltradosListaEmpleadosAusentismo(List<Empleados> filtradosListaEmpleadosAusentismo) {
        this.filtradosListaEmpleadosAusentismo = filtradosListaEmpleadosAusentismo;
    }

    public Empleados getSeleccionMostrar() {
        return seleccionMostrar;
    }

    public void setSeleccionMostrar(Empleados seleccionMostrar) {
        this.seleccionMostrar = seleccionMostrar;
    }

    public List<Empleados> getListaEmpleados() {
        return listaEmpleados;
    }

    public void setListaEmpleados(List<Empleados> listaEmpleados) {
        this.listaEmpleados = listaEmpleados;
    }

    public List<Empleados> getFiltradoslistaEmpleados() {
        return filtradoslistaEmpleados;
    }

    public void setFiltradoslistaEmpleados(List<Empleados> filtradoslistaEmpleados) {
        this.filtradoslistaEmpleados = filtradoslistaEmpleados;
    }

    public Empleados getSeleccionEmpleados() {
        return seleccionEmpleados;
    }

    public void setSeleccionEmpleados(Empleados seleccionEmpleados) {
        this.seleccionEmpleados = seleccionEmpleados;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public void setAceptar(boolean aceptar) {
        this.aceptar = aceptar;
    }

    public List<Tiposausentismos> getListaTiposAusentismos() {
        return listaTiposAusentismos;
    }

    public void setListaTiposAusentismos(List<Tiposausentismos> listaTiposAusentismos) {
        this.listaTiposAusentismos = listaTiposAusentismos;
    }

    public List<Tiposausentismos> getFiltradoslistaTiposAusentismos() {
        return filtradoslistaTiposAusentismos;
    }

    public void setFiltradoslistaTiposAusentismos(List<Tiposausentismos> filtradoslistaTiposAusentismos) {
        this.filtradoslistaTiposAusentismos = filtradoslistaTiposAusentismos;
    }

    public Tiposausentismos getSeleccionTiposAusentismos() {
        return seleccionTiposAusentismos;
    }

    public void setSeleccionTiposAusentismos(Tiposausentismos seleccionTiposAusentismos) {
        this.seleccionTiposAusentismos = seleccionTiposAusentismos;
    }

    public Clasesausentismos getSeleccionClasesAusentismos() {
        return seleccionClasesAusentismos;
    }

    public void setSeleccionClasesAusentismos(Clasesausentismos seleccionClasesAusentismos) {
        this.seleccionClasesAusentismos = seleccionClasesAusentismos;
    }

    public Causasausentismos getSeleccionCausasAusentismos() {
        return seleccionCausasAusentismos;
    }

    public void setSeleccionCausasAusentismos(Causasausentismos seleccionCausasAusentismos) {
        this.seleccionCausasAusentismos = seleccionCausasAusentismos;
    }

    public List<Clasesausentismos> getListaClasesAusentismos() {
        return listaClasesAusentismos;
    }

    public void setListaClasesAusentismos(List<Clasesausentismos> listaClasesAusentismos) {
        this.listaClasesAusentismos = listaClasesAusentismos;
    }

    public List<Clasesausentismos> getFiltradoslistaClasesAusentismos() {
        return filtradoslistaClasesAusentismos;
    }

    public void setFiltradoslistaClasesAusentismos(List<Clasesausentismos> filtradoslistaClasesAusentismos) {
        this.filtradoslistaClasesAusentismos = filtradoslistaClasesAusentismos;
    }

    public List<Causasausentismos> getListaCausasAusentismos() {
        return listaCausasAusentismos;
    }

    public void setListaCausasAusentismos(List<Causasausentismos> listaCausasAusentismos) {
        this.listaCausasAusentismos = listaCausasAusentismos;
    }

    public List<Causasausentismos> getFiltradoslistaCausasAusentismos() {
        return filtradoslistaCausasAusentismos;
    }

    public void setFiltradoslistaCausasAusentismos(List<Causasausentismos> filtradoslistaCausasAusentismos) {
        this.filtradoslistaCausasAusentismos = filtradoslistaCausasAusentismos;
    }

    public List<String> getListaPorcentaje() {
        return listaPorcentaje;
    }

    public void setListaPorcentaje(List<String> listaPorcentaje) {
        this.listaPorcentaje = listaPorcentaje;
    }

    public List<String> getFiltradosListaPorcentajes() {
        return filtradosListaPorcentajes;
    }

    public void setFiltradosListaPorcentajes(List<String> filtradosListaPorcentajes) {
        this.filtradosListaPorcentajes = filtradosListaPorcentajes;
    }

    public String getSeleccionPorcentajes() {
        return seleccionPorcentajes;
    }

    public void setSeleccionPorcentajes(String seleccionPorcentajes) {
        this.seleccionPorcentajes = seleccionPorcentajes;
    }

    public BigInteger getSecuenciaEmpleado() {
        return secuenciaEmpleado;
    }

    public void setSecuenciaEmpleado(BigInteger secuenciaEmpleado) {
        this.secuenciaEmpleado = secuenciaEmpleado;
    }

    public List<Ibcs> getListaIBCS() {
        return listaIBCS;
    }

    public void setListaIBCS(List<Ibcs> listaIBCS) {
        this.listaIBCS = listaIBCS;
    }

    public List<Ibcs> getFiltradosListaIBCS() {
        return filtradosListaIBCS;
    }

    public void setFiltradosListaIBCS(List<Ibcs> filtradosListaIBCS) {
        this.filtradosListaIBCS = filtradosListaIBCS;
    }

    public Ibcs getSeleccionIBCS() {
        return seleccionIBCS;
    }

    public void setSeleccionIBCS(Ibcs seleccionIBCS) {
        this.seleccionIBCS = seleccionIBCS;
    }

    public List<String> getListaForma() {
        return listaForma;
    }

    public void setListaForma(List<String> listaForma) {
        this.listaForma = listaForma;
    }

    public List<String> getFiltradosListaForma() {
        return filtradosListaForma;
    }

    public void setFiltradosListaForma(List<String> filtradosListaForma) {
        this.filtradosListaForma = filtradosListaForma;
    }

    public String getSeleccionForma() {
        return seleccionForma;
    }

    public void setSeleccionForma(String seleccionForma) {
        this.seleccionForma = seleccionForma;
    }

    public List<Soaccidentes> getListaAccidentes() {
        return listaAccidentes;
    }

    public void setListaAccidentes(List<Soaccidentes> listaAccidentes) {
        this.listaAccidentes = listaAccidentes;
    }

    public List<Soaccidentes> getFiltradoslistaAccidentes() {
        return filtradoslistaAccidentes;
    }

    public void setFiltradoslistaAccidentes(List<Soaccidentes> filtradoslistaAccidentes) {
        this.filtradoslistaAccidentes = filtradoslistaAccidentes;
    }

    public Soaccidentes getSeleccionAccidentes() {
        return seleccionAccidentes;
    }

    public void setSeleccionAccidentes(Soaccidentes seleccionAccidentes) {
        this.seleccionAccidentes = seleccionAccidentes;
    }

    public List<EnfermeadadesProfesionales> getListaEnfermeadadesProfesionales() {
        return listaEnfermeadadesProfesionales;
    }

    public void setListaEnfermeadadesProfesionales(List<EnfermeadadesProfesionales> listaEnfermeadadesProfesionales) {
        this.listaEnfermeadadesProfesionales = listaEnfermeadadesProfesionales;
    }

    public List<EnfermeadadesProfesionales> getFiltradoslistaEnfermeadadesProfesionales() {
        return filtradoslistaEnfermeadadesProfesionales;
    }

    public void setFiltradoslistaEnfermeadadesProfesionales(List<EnfermeadadesProfesionales> filtradoslistaEnfermeadadesProfesionales) {
        this.filtradoslistaEnfermeadadesProfesionales = filtradoslistaEnfermeadadesProfesionales;
    }

    public EnfermeadadesProfesionales getSeleccionEnfermeadadesProfesionales() {
        return seleccionEnfermeadadesProfesionales;
    }

    public void setSeleccionEnfermeadadesProfesionales(EnfermeadadesProfesionales seleccionEnfermeadadesProfesionales) {
        this.seleccionEnfermeadadesProfesionales = seleccionEnfermeadadesProfesionales;
    }

    public List<Diagnosticoscategorias> getListaDiagnosticos() {
        return listaDiagnosticos;
    }

    public void setListaDiagnosticos(List<Diagnosticoscategorias> listaDiagnosticos) {
        this.listaDiagnosticos = listaDiagnosticos;
    }

    public List<Diagnosticoscategorias> getFiltradoslistaDiagnosticos() {
        return filtradoslistaDiagnosticos;
    }

    public void setFiltradoslistaDiagnosticos(List<Diagnosticoscategorias> filtradoslistaDiagnosticos) {
        this.filtradoslistaDiagnosticos = filtradoslistaDiagnosticos;
    }

    public Diagnosticoscategorias getSeleccionDiagnosticos() {
        return seleccionDiagnosticos;
    }

    public void setSeleccionDiagnosticos(Diagnosticoscategorias seleccionDiagnosticos) {
        this.seleccionDiagnosticos = seleccionDiagnosticos;
    }

    public List<Terceros> getListaTerceros() {
        return listaTerceros;
    }

    public void setListaTerceros(List<Terceros> listaTerceros) {
        this.listaTerceros = listaTerceros;
    }

    public List<Terceros> getFiltradoslistaTerceros() {
        return filtradoslistaTerceros;
    }

    public void setFiltradoslistaTerceros(List<Terceros> filtradoslistaTerceros) {
        this.filtradoslistaTerceros = filtradoslistaTerceros;
    }

    public Terceros getSeleccionTerceros() {
        return seleccionTerceros;
    }

    public void setSeleccionTerceros(Terceros seleccionTerceros) {
        this.seleccionTerceros = seleccionTerceros;
    }

    public List<Soausentismos> getListaProrrogas() {
        return listaProrrogas;
    }

    public void setListaProrrogas(List<Soausentismos> listaProrrogas) {
        this.listaProrrogas = listaProrrogas;
    }

    public List<Soausentismos> getFiltradoslistaProrrogas() {
        return filtradoslistaProrrogas;
    }

    public void setFiltradoslistaProrrogas(List<Soausentismos> filtradoslistaProrrogas) {
        this.filtradoslistaProrrogas = filtradoslistaProrrogas;
    }

    public Soausentismos getSeleccionProrrogas() {
        return seleccionProrrogas;
    }

    public void setSeleccionProrrogas(Soausentismos seleccionProrrogas) {
        this.seleccionProrrogas = seleccionProrrogas;
    }

    public String getProrroga() {
        if (seleccionProrrogas != null) {
            if (Prorroga == null) {
                Prorroga = administrarAusentismos.mostrarProrroga(seleccionProrrogas.getSecuencia());
            }
        }
        return Prorroga;

    }

    public String getRelacion() {
        if (ausentismoSeleccionado != null) {
            if (Relacion == null) {
                Relacion = administrarAusentismos.mostrarRelacion(ausentismoSeleccionado.getSecuencia());
            }
        }
        return Relacion;
    }

    public Soausentismos getEditarAusentismos() {
        return editarAusentismos;
    }

    public void setEditarAusentismos(Soausentismos editarAusentismos) {
        this.editarAusentismos = editarAusentismos;
    }

    public Soausentismos getNuevoAusentismo() {
        return nuevoAusentismo;
    }

    public void setNuevoAusentismo(Soausentismos nuevoAusentismo) {
        this.nuevoAusentismo = nuevoAusentismo;
    }

    public String getMensajeValidacion() {
        return mensajeValidacion;
    }

    public Soausentismos getDuplicarAusentismo() {
        return duplicarAusentismo;
    }

    public void setDuplicarAusentismo(Soausentismos duplicarAusentismo) {
        this.duplicarAusentismo = duplicarAusentismo;
    }

    public boolean isCambiosPagina() {
        return cambiosPagina;
    }

    public void setCambiosPagina(boolean cambiosPagina) {
        this.cambiosPagina = cambiosPagina;
    }

    public Soausentismos getAusentismoSeleccionado() {
        return ausentismoSeleccionado;
    }

    public void setAusentismoSeleccionado(Soausentismos ausentismoSeleccionado) {
        this.ausentismoSeleccionado = ausentismoSeleccionado;
    }

    public String getAltoTabla() {
        return altoTabla;
    }

    public void setAltoTabla(String altoTabla) {
        this.altoTabla = altoTabla;
    }

    public String getAltoTablaReg() {
        if (altoTabla.equals("119")) {
            altoTablaReg = "4";
        } else {
            altoTablaReg = "5";
        }
        return altoTablaReg;
    }

    public void setAltoTablaReg(String altoTablaReg) {
        this.altoTablaReg = altoTablaReg;
    }

    public String getAltoDialogoNuevo() {
        return altoDialogoNuevo;
    }

    public void setAltoDialogoNuevo(String altoDialogoNuevo) {
        this.altoDialogoNuevo = altoDialogoNuevo;
    }

    public String getAltoDialogoDuplicar() {
        return altoDialogoDuplicar;
    }

    public void setAltoDialogoDuplicar(String altoDialogoDuplicar) {
        this.altoDialogoDuplicar = altoDialogoDuplicar;
    }

    public String getInfoRegistroTipo() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formLovTiposAusentismos:LOVTiposAusentismos");

        if (filtradoslistaTiposAusentismos != null) {
            if (filtradoslistaTiposAusentismos.size() == 1) {
                seleccionTiposAusentismos = filtradoslistaTiposAusentismos.get(0);
                aceptar = false;
                RequestContext.getCurrentInstance().execute("PF('LOVTiposAusentismos').unselectAllRows();PF('LOVTiposAusentismos').selectRow(0);");
            } else {
                seleccionTiposAusentismos = null;
                RequestContext.getCurrentInstance().execute("PF('LOVTiposAusentismos').unselectAllRows();");
            }
        } else {
            seleccionTiposAusentismos = null;
            aceptar = true;
        }
        infoRegistroTipo = String.valueOf(tabla.getRowCount());
        return infoRegistroTipo;
    }

    public void setInfoRegistroTipo(String infoRegistroTipo) {
        this.infoRegistroTipo = infoRegistroTipo;
    }

    public String getInfoRegistroClase() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formLOVClasesAusentismo:LOVClasesAusentismos");
        if (filtradoslistaClasesAusentismos != null) {
            if (filtradoslistaClasesAusentismos.size() == 1) {
                seleccionClasesAusentismos = filtradoslistaClasesAusentismos.get(0);
                aceptar = false;
                RequestContext.getCurrentInstance().execute("PF('LOVClasesAusentismos').unselectAllRows();PF('LOVClasesAusentismos').selectRow(0);");
            } else {
                seleccionClasesAusentismos = null;
                RequestContext.getCurrentInstance().execute("PF('LOVClasesAusentismos').unselectAllRows();");
            }
        } else {
            seleccionClasesAusentismos = null;
            aceptar = true;
        }
        infoRegistroClase = String.valueOf(tabla.getRowCount());
        return infoRegistroClase;
    }

    public void setInfoRegistroClase(String infoRegistroClase) {
        this.infoRegistroClase = infoRegistroClase;
    }

    public String getInfoRegistroCausa() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formLovCausasAusentismos:LOVCausasAusentismos");
        if (filtradoslistaCausasAusentismos != null) {
            if (filtradoslistaCausasAusentismos.size() == 1) {
                seleccionCausasAusentismos = filtradoslistaCausasAusentismos.get(0);
                aceptar = false;
                RequestContext.getCurrentInstance().execute("PF('LOVCausasAusentismos').unselectAllRows();PF('LOVCausasAusentismos').selectRow(0);");
            } else {
                seleccionCausasAusentismos = null;
                RequestContext.getCurrentInstance().execute("PF('LOVCausasAusentismos').unselectAllRows();");
            }
        } else {
            seleccionCausasAusentismos = null;
            aceptar = true;
        }
        infoRegistroCausa = String.valueOf(tabla.getRowCount());
        return infoRegistroCausa;
    }

    public void setInfoRegistroCausa(String infoRegistroCausa) {
        this.infoRegistroCausa = infoRegistroCausa;
    }

    public String getInfoRegistroPorcentaje() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formLovPorcentajes:LOVPorcentajes");
        if (filtradosListaPorcentajes != null) {
            if (filtradosListaPorcentajes.size() == 1) {
                seleccionPorcentajes = filtradosListaPorcentajes.get(0);
                aceptar = false;
                RequestContext.getCurrentInstance().execute("PF('LOVPorcentajes').unselectAllRows();PF('LOVPorcentajes').selectRow(0);");
            } else {
                seleccionPorcentajes = null;
                RequestContext.getCurrentInstance().execute("PF('LOVPorcentajes').unselectAllRows();");
            }
        } else {
            seleccionPorcentajes = null;
            aceptar = true;
        }
        infoRegistroPorcentaje = String.valueOf(tabla.getRowCount());
        return infoRegistroPorcentaje;
    }

    public void setInfoRegistroPorcentaje(String infoRegistroPorcentaje) {
        this.infoRegistroPorcentaje = infoRegistroPorcentaje;
    }

    public String getInfoRegistroBase() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formLovIbcs:LOVIbcs");
        if (filtradosListaIBCS != null) {
            if (filtradosListaIBCS.size() == 1) {
                seleccionIBCS = filtradosListaIBCS.get(0);
                aceptar = false;
                RequestContext.getCurrentInstance().execute("PF('LOVIbcs').unselectAllRows();PF('LOVIbcs').selectRow(0);");
            } else {
                seleccionIBCS = null;
                RequestContext.getCurrentInstance().execute("PF('LOVIbcs').unselectAllRows();");
            }
        } else {
            seleccionIBCS = null;
            aceptar = true;
        }
        infoRegistroBase = String.valueOf(tabla.getRowCount());
        return infoRegistroBase;
    }

    public void setInfoRegistroBase(String infoRegistroBase) {
        this.infoRegistroBase = infoRegistroBase;
    }

    public String getInfoRegistroAccidente() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formLovAccidentes:LOVAccidentes");
        if (filtradoslistaAccidentes != null) {
            if (filtradoslistaAccidentes.size() == 1) {
                seleccionAccidentes = filtradoslistaAccidentes.get(0);
                aceptar = false;
                RequestContext.getCurrentInstance().execute("PF('LOVAccidentes').unselectAllRows();PF('LOVAccidentes').selectRow(0);");
            } else {
                seleccionAccidentes = null;
                RequestContext.getCurrentInstance().execute("PF('LOVAccidentes').unselectAllRows();");
            }
        } else {
            seleccionAccidentes = null;
            aceptar = true;
        }
        infoRegistroAccidente = String.valueOf(tabla.getRowCount());
        return infoRegistroAccidente;
    }

    public void setInfoRegistroAccidente(String infoRegistroAccidente) {
        this.infoRegistroAccidente = infoRegistroAccidente;
    }

    public String getInfoRegistroEnfermedad() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formLovEnfermedades:LOVEnfermedades");
        if (filtradoslistaEnfermeadadesProfesionales != null) {
            if (filtradoslistaEnfermeadadesProfesionales.size() == 1) {
                seleccionEnfermeadadesProfesionales = filtradoslistaEnfermeadadesProfesionales.get(0);
                aceptar = false;
                RequestContext.getCurrentInstance().execute("PF('LOVEnfermedades').unselectAllRows();PF('LOVEnfermedades').selectRow(0);");
            } else {
                seleccionEnfermeadadesProfesionales = null;
                RequestContext.getCurrentInstance().execute("PF('LOVEnfermedades').unselectAllRows();");
            }
        } else {
            seleccionEnfermeadadesProfesionales = null;
            aceptar = true;
        }
        infoRegistroAccidente = String.valueOf(tabla.getRowCount());
        return infoRegistroEnfermedad;
    }

    public void setInfoRegistroEnfermedad(String infoRegistroEnfermedad) {
        this.infoRegistroEnfermedad = infoRegistroEnfermedad;
    }

    public String getInfoRegistroDiagnostico() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formLovDiagnosticos:LOVDiagnosticos");
        if (filtradoslistaDiagnosticos != null) {
            if (filtradoslistaDiagnosticos.size() == 1) {
                seleccionDiagnosticos = filtradoslistaDiagnosticos.get(0);
                aceptar = false;
                RequestContext.getCurrentInstance().execute("PF('LOVDiagnosticos').unselectAllRows();PF('LOVDiagnosticos').selectRow(0);");
            } else {
                seleccionDiagnosticos = null;
                RequestContext.getCurrentInstance().execute("PF('LOVDiagnosticos').unselectAllRows();");
            }
        } else {
            seleccionDiagnosticos = null;
            aceptar = true;
        }
        infoRegistroDiagnostico = String.valueOf(tabla.getRowCount());
        return infoRegistroDiagnostico;
    }

    public void setInfoRegistroDiagnostico(String infoRegistroDiagnostico) {
        this.infoRegistroDiagnostico = infoRegistroDiagnostico;
    }

    public String getInfoRegistroProrroga() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formLovProrrogas:LOVProrrogas");
        if (filtradoslistaProrrogas != null) {
            if (filtradoslistaProrrogas.size() == 1) {
                seleccionProrrogas = filtradoslistaProrrogas.get(0);
                aceptar = false;
                RequestContext.getCurrentInstance().execute("PF('LOVProrrogas').unselectAllRows();PF('LOVProrrogas').selectRow(0);");
            } else {
                seleccionProrrogas = null;
                RequestContext.getCurrentInstance().execute("PF('LOVProrrogas').unselectAllRows();");
            }
        } else {
            seleccionProrrogas = null;
            aceptar = true;
        }
        infoRegistroProrroga = String.valueOf(tabla.getRowCount());
        return infoRegistroProrroga;
    }

    public void setInfoRegistroProrroga(String infoRegistroProrroga) {
        this.infoRegistroProrroga = infoRegistroProrroga;
    }

    public String getInfoRegistroTercero() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formLovTerceros:LOVTerceros");
        if (filtradoslistaTerceros != null) {
            if (filtradoslistaTerceros.size() == 1) {
                seleccionTerceros = filtradoslistaTerceros.get(0);
                aceptar = false;
                RequestContext.getCurrentInstance().execute("PF('LOVTerceros').unselectAllRows();PF('LOVTerceros').selectRow(0);");
            } else {
                seleccionTerceros = null;
                RequestContext.getCurrentInstance().execute("PF('LOVTerceros').unselectAllRows();");
            }
        } else {
            seleccionTerceros = null;
            aceptar = true;
        }
        infoRegistroTercero = String.valueOf(tabla.getRowCount());
        return infoRegistroTercero;
    }

    public void setInfoRegistroTercero(String infoRegistroTercero) {
        this.infoRegistroTercero = infoRegistroTercero;
    }

    public String getInfoRegistroEmpleado() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosEmpleados");
        infoRegistroEmpleado = String.valueOf(tabla.getRowCount());
        return infoRegistroEmpleado;
    }

    public void setInfoRegistroEmpleado(String infoRegistroEmpleado) {
        this.infoRegistroEmpleado = infoRegistroEmpleado;
    }

    public String getInfoRegistroForma() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formLoVFormas:LOVFormas");
        if (filtradosListaForma != null) {
            if (filtradosListaForma.size() == 1) {
                seleccionForma = filtradosListaForma.get(0);
                aceptar = false;
                RequestContext.getCurrentInstance().execute("PF('LOVFormas').unselectAllRows();PF('LOVFormas').selectRow(0);");
            } else {
                seleccionForma = null;
                RequestContext.getCurrentInstance().execute("PF('LOVFormas').unselectAllRows();");
            }
        } else {
            seleccionForma = null;
            aceptar = true;
        }
        infoRegistroForma = String.valueOf(tabla.getRowCount());
        return infoRegistroForma;
    }

    public void setInfoRegistroForma(String infoRegistroForma) {
        this.infoRegistroForma = infoRegistroForma;
    }

    public boolean isColapsado() {
        return colapsado;
    }

    public void setColapsado(boolean colapsado) {
        this.colapsado = colapsado;
    }

    public boolean isGuardado() {
        return guardado;
    }

    public void setGuardado(boolean guardado) {
        this.guardado = guardado;
    }

    public String getInfoRegistroAusentismos() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosAusentismosEmpleado");
        infoRegistroAusentismos = String.valueOf(tabla.getRowCount());
        return infoRegistroAusentismos;
    }

    public void setInfoRegistroAusentismos(String infoRegistroAusentismos) {
        this.infoRegistroAusentismos = infoRegistroAusentismos;
    }

    public String getInfoRegistroEmpleadoLov() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formLovEmpleados:LOVEmpleados");
        if (filtradoslistaEmpleados != null) {
            if (filtradoslistaEmpleados.size() == 1) {
                seleccionEmpleados = filtradoslistaEmpleados.get(0);
                aceptar = false;
                RequestContext.getCurrentInstance().execute("PF('LOVEmpleados').unselectAllRows();PF('LOVEmpleados').selectRow(0);");
            } else {
                seleccionEmpleados = null;
                RequestContext.getCurrentInstance().execute("PF('LOVEmpleados').unselectAllRows();");
            }
        } else {
            seleccionEmpleados = null;
            aceptar = true;
        }
        infoRegistroEmpleadoLov = String.valueOf(tabla.getRowCount());
        return infoRegistroEmpleadoLov;
    }

    public void setInfoRegistroEmpleadoLov(String infoRegistroEmpleadoLov) {
        this.infoRegistroEmpleadoLov = infoRegistroEmpleadoLov;
    }

    public boolean isActivarLov() {
        return activarLov;
    }

    public void setActivarLov(boolean activarLov) {
        this.activarLov = activarLov;
    }

}
