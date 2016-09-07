/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.*;
import Exportar.ExportarPDFTablasAnchas;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarFormulaConceptoInterface;
import InterfaceAdministrar.AdministrarNovedadesConceptosInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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
public class ControlNovedadesConceptos implements Serializable {

    @EJB
    AdministrarNovedadesConceptosInterface administrarNovedadesConceptos;
    @EJB
    AdministrarRastrosInterface administrarRastros;
    @EJB
    AdministrarFormulaConceptoInterface administrarFormulaConcepto;
    //LISTA NOVEDADES
    private List<Novedades> listaNovedades;
    private List<Novedades> filtradosListaNovedades;
    private Novedades novedadSeleccionada;
    private Novedades novedadBackup;
    //LISTA QUE NO ES LISTA - 1 SOLO ELEMENTO
    private List<Conceptos> listaConceptosNovedad;
    private List<Conceptos> filtradosListaConceptosNovedad;
    private Conceptos conceptoSeleccionado; //Seleccion Mostrar
    //editar celda
    private Novedades editarNovedades;
    private int cualCelda, tipoLista;
    //OTROS
    private boolean aceptar;
    private int tipoActualizacion; //Activo/Desactivo Crtl + F11
    private int bandera;
    private boolean permitirIndex;
    //RASTROS
    private boolean guardado;
    //Crear Novedades
    private List<Novedades> listaNovedadesCrear;
    public Novedades nuevaNovedad;
    private int k;
    private BigInteger l;
    private String mensajeValidacion;
    //Modificar Novedades
    private List<Novedades> listaNovedadesModificar;
    //Borrar Novedades
    private List<Novedades> listaNovedadesBorrar;
    //Autocompletar
    private String codigoEmpleado, nitTercero, formula, descripcionConcepto, descripcionPeriodicidad, nombreTercero;
    private Date FechaFinal;
    // private Short CodigoPeriodicidad;
    private String CodigoPeriodicidad;
    private BigDecimal Saldo;
    private Integer HoraDia, MinutoHora;
    //L.O.V Conceptos
    private List<Conceptos> lovConceptos;
    private List<Conceptos> filtradoslistaConceptos;
    private Conceptos conceptoSeleccionadoLov;
    //L.O.V Empleados
    private List<Empleados> lovEmpleados;
    private List<Empleados> filtradoslistaEmpleados;
    private Empleados empleadoSeleccionadoLov;
    //L.O.V PERIODICIDADES
    private List<Periodicidades> lovPeriodicidades;
    private List<Periodicidades> filtradoslistaPeriodicidades;
    private Periodicidades periodicidadSeleccionada;
    //L.O.V TERCEROS
    private List<Terceros> lovTerceros;
    private List<Terceros> filtradoslistaTerceros;
    private Terceros terceroSeleccionado;
    //L.O.V FORMULAS
    private List<Formulas> lovFormulas;
    private List<Formulas> filtradoslistaFormulas;
    private Formulas formulaSeleccionadaLov;
    //Columnas Tabla NOVEDADES
    private Column nCEmpleadoCodigo, nCEmpleadoNombre, nCFechasInicial, nCFechasFinal,
            nCValor, nCSaldo, nCPeriodicidadCodigo, nCDescripcionPeriodicidad, nCTercerosNit,
            nCTercerosNombre, nCFormulas, nCHorasDias, nCMinutosHoras, nCTipo;
    //Duplicar
    private Novedades duplicarNovedad;
    //USUARIO
    private String alias;
    private Usuarios usuarioBD;
    //VALIDAR SI EL QUE SE VA A BORRAR ESTÁ EN SOLUCIONES FORMULAS
    private int resultado;
    private boolean todas;
    private boolean actuales;
    private String altoTabla;
    private String infoRegistroConceptos;
    private String infoRegistroNovedades;
    private String infoRegistroLovConceptos;
    private String infoRegistroLovFormulas;
    private String infoRegistroLovEmpleados;
    private String infoRegistroLovPeriodicidades;
    private String infoRegistroLovTerceros;
    private BigDecimal valor;
    private boolean activarLOV;
    public String paginaAnterior;

    public ControlNovedadesConceptos() {
        permitirIndex = true;
        listaNovedades = new ArrayList<Novedades>();
        lovEmpleados = null;
        lovFormulas = null;
        lovEmpleados = null;
        lovPeriodicidades = null;
        lovConceptos = null;
        listaConceptosNovedad = null;
        todas = false;
        actuales = true;
        novedadSeleccionada = null;
        aceptar = true;
        guardado = true;
        tipoLista = 0;
        listaNovedadesBorrar = new ArrayList<Novedades>();
        listaNovedadesCrear = new ArrayList<Novedades>();
        listaNovedadesModificar = new ArrayList<Novedades>();
        //Crear VC
        nuevaNovedad = new Novedades();
        //nuevaNovedad.setFechafinal(new Date());
        nuevaNovedad.setFormula(new Formulas());
        nuevaNovedad.setTercero(new Terceros());
        nuevaNovedad.setPeriodicidad(new Periodicidades());
        nuevaNovedad.setFechareporte(new Date());
        nuevaNovedad.setTipo("FIJA");
        altoTabla = "155";
        valor = new BigDecimal(Integer.toString(0));
        nuevaNovedad.setValortotal(valor);
        infoRegistroConceptos = "0";
        infoRegistroNovedades = "0";
        infoRegistroLovConceptos = "0";
        infoRegistroLovFormulas = "0";
        infoRegistroLovEmpleados = "0";
        infoRegistroLovPeriodicidades = "0";
        infoRegistroLovTerceros = "0";
        activarLOV = true;
        paginaAnterior = "";
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarNovedadesConceptos.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
            administrarFormulaConcepto.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void recibirPagina(String pagina) {
        paginaAnterior = pagina;
        getListaConceptosNovedad();
        if (listaConceptosNovedad != null) {
            conceptoSeleccionado = listaConceptosNovedad.get(0);
        }
        llenarTablaNovedades();
        contarRegistrosConceptos();
        System.out.println("infoRegistroConceptos : " + infoRegistroConceptos);
    }

    public String retornarPagina() {
        anularBotonLOV();
        return paginaAnterior;
    }

    //Ubicacion Celda Arriba 
    public void cambiarConcepto() {
        //Si ninguna de las 3 listas (crear,modificar,borrar) tiene algo, hace esto
        //{
        if (listaNovedadesCrear.isEmpty() && listaNovedadesBorrar.isEmpty() && listaNovedadesModificar.isEmpty()) {
            listaNovedades.clear();
            System.out.println("Concepto Seleccionado : " + conceptoSeleccionado.getDescripcion());
            llenarTablaNovedades();
            RequestContext.getCurrentInstance().update("form:datosNovedadesConcepto");
            //}
        } else {
            RequestContext.getCurrentInstance().update("formularioDialogos:cambiar");
            RequestContext.getCurrentInstance().execute("cambiar.show()");
        }
    }

    public void limpiarListas() {
        listaNovedadesCrear.clear();
        listaNovedadesBorrar.clear();
        listaNovedadesModificar.clear();
        guardado = true;
        listaNovedades.clear();
        anularBotonLOV();
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("form:datosNovedadesConcepto");
        context.update("form:ACEPTAR");
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void cancelarCambioEmpleados() {
        filtradoslistaEmpleados = null;
        empleadoSeleccionadoLov = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:LOVEmpleados:globalFilter");
        context.execute("LOVEmpleados.clearFilters()");
        context.update("formularioDialogos:LOVEmpleados");
        context.update("formularioDialogos:empleadosDialogo");
        context.update("formularioDialogos:aceptarE");
        context.execute("empleadosDialogo.hide()");
    }

    public void cancelarCambioPeriodicidades() {
        filtradoslistaPeriodicidades = null;
        periodicidadSeleccionada = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:LOVPeriodicidades:globalFilter");
        context.execute("LOVPeriodicidades.clearFilters()");
        context.update("formularioDialogos:LOVPeriodicidades");
        context.update("formularioDialogos:periodicidadesDialogo");
        context.update("formularioDialogos:aceptarP");
        context.execute("periodicidadesDialogo.hide()");
    }

    public void cancelarCambioFormulas() {
        filtradoslistaFormulas = null;
        formulaSeleccionadaLov = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:LOVFormulas:globalFilter");
        context.execute("LOVFormulas.clearFilters()");
        context.update("formularioDialogos:LOVFormulas");
        context.update("formularioDialogos:formulasDialogo");
        context.update("formularioDialogos:aceptarF");
        context.execute("formulasDialogo.hide()");
    }

    public void cancelarCambioConceptos() {
        filtradoslistaConceptos = null;
        conceptoSeleccionadoLov = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:LOVConceptos:globalFilter");
        context.execute("LOVConceptos.clearFilters()");
        context.update("formularioDialogos:LOVConceptos");
        context.update("formularioDialogos:conceptosDialogo");
        context.update("formularioDialogos:aceptarC");
        context.execute("conceptosDialogo.hide()");
    }

    public void cancelarCambioTerceros() {
        filtradoslistaTerceros = null;
        terceroSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:LOVTerceros:globalFilter");
        context.execute("LOVTerceros.clearFilters()");
        context.update("formularioDialogos:LOVTerceros");
        context.update("formularioDialogos:tercerosDialogo");
        context.update("formularioDialogos:aceptarT");
        context.execute("tercerosDialogo.hide()");
    }

    public void seleccionarTipoNuevaNovedad(String tipo, int tipoNuevo) {
        anularBotonLOV();
        if (tipoNuevo == 1) {
            if (tipo.equals("FIJA")) {
                nuevaNovedad.setTipo("FIJA");
            } else if (tipo.equals("OCASIONAL")) {
                nuevaNovedad.setTipo("OCASIONAL");
            } else if (tipo.equals("PAGO POR FUERA")) {
                nuevaNovedad.setTipo("PAGO POR FUERA");
            }
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTipo");
        } else {
            if (tipo.equals("FIJA")) {
                duplicarNovedad.setTipo("FIJA");
            } else if (tipo.equals("OCASIONAL")) {
                duplicarNovedad.setTipo("OCASIONAL");
            } else if (tipo.equals("PAGO POR FUERA")) {
                duplicarNovedad.setTipo("PAGO POR FUERA");
            }
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTipo");
        }
    }

    public void seleccionarTipo(String estadoTipo, int indice, int celda) {
        RequestContext context = RequestContext.getCurrentInstance();
        anularBotonLOV();
        if (estadoTipo.equals("FIJA")) {
            novedadSeleccionada.setTipo("FIJA");
        } else if (estadoTipo.equals("OCASIONAL")) {
            novedadSeleccionada.setTipo("OCASIONAL");
        } else if (estadoTipo.equals("PAGO POR FUERA")) {
            novedadSeleccionada.setTipo("PAGO POR FUERA");
        }

        if (!listaNovedadesCrear.contains(novedadSeleccionada)) {
            if (listaNovedadesModificar.isEmpty()) {
                listaNovedadesModificar.add(novedadSeleccionada);
            } else if (!listaNovedadesModificar.contains(novedadSeleccionada)) {
                listaNovedadesModificar.add(novedadSeleccionada);
            }
        }
        if (guardado) {
            guardado = false;
            context.update("form:ACEPTAR");
        }
        RequestContext.getCurrentInstance().update("form:datosNovedadesConcepto");
    }

    public void asignarIndex(Novedades novedad, int dlg, int LND) {

        novedadSeleccionada = novedad;
        RequestContext context = RequestContext.getCurrentInstance();
        tipoActualizacion = LND;
        if (dlg == 0) {
            contarRegistrosEmpleados(0);
            activarBotonLOV();
            context.update("formularioDialogos:empleadosDialogo");
            context.execute("empleadosDialogo.show()");
        } else if (dlg == 2) {
            contarRegistrosLovFormulas(0);
            activarBotonLOV();
            context.update("formularioDialogos:formulasDialogo");
            context.execute("formulasDialogo.show()");
        } else if (dlg == 3) {
            contarRegistrosPeriodicidades(0);
            activarBotonLOV();
            context.update("formularioDialogos:periodicidadesDialogo");
            context.execute("periodicidadesDialogo.show()");
        } else if (dlg == 4) {
            contarRegistrosTerceros(0);
            activarBotonLOV();
            context.update("formularioDialogos:tercerosDialogo");
            context.execute("tercerosDialogo.show()");
        } else {
            anularBotonLOV();
        }
    }

    public void asignarIndex(int dlg, int LND) {
        anularBotonLOV();
        RequestContext context = RequestContext.getCurrentInstance();
        tipoActualizacion = LND;
        if (dlg == 0) {
            contarRegistrosEmpleados(0);
            activarBotonLOV();
            context.update("formularioDialogos:empleadosDialogo");
            context.execute("empleadosDialogo.show()");
        } else if (dlg == 1) {
            contarRegistrosLovConceptos(0);
            activarBotonLOV();
            context.update("formularioDialogos:conceptosDialogo");
            context.execute("conceptosDialogo.show()");
        } else if (dlg == 2) {
            contarRegistrosLovFormulas(0);
            activarBotonLOV();
            context.update("formularioDialogos:formulasDialogo");
            context.execute("formulasDialogo.show()");
        } else if (dlg == 3) {
            contarRegistrosPeriodicidades(0);
            activarBotonLOV();
            context.update("formularioDialogos:periodicidadesDialogo");
            context.execute("periodicidadesDialogo.show()");
        } else if (dlg == 4) {
            contarRegistrosTerceros(0);
            activarBotonLOV();
            context.update("formularioDialogos:tercerosDialogo");
            context.execute("tercerosDialogo.show()");
        }
    }

    public void mostrarTodos() {

        RequestContext context = RequestContext.getCurrentInstance();
        if (!listaConceptosNovedad.isEmpty()) {
            listaConceptosNovedad.clear();
        }
        //listaEmpleadosNovedad = listaValEmpleados;
        if (listaConceptosNovedad != null) {
            for (int i = 0; i < lovConceptos.size(); i++) {
                listaConceptosNovedad.add(lovConceptos.get(i));
            }
        }
        conceptoSeleccionado = listaConceptosNovedad.get(0);
        llenarTablaNovedades();
        contarRegistrosConceptos();
        contarRegistrosNovedades();
        anularBotonLOV();
        context.update("form:datosConceptos");
        context.update("form:diasTotales");
        context.update("form:diasAplazados");

        listaNovedades.clear();
        filtradosListaConceptosNovedad = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
    }

    public void actualizarConceptosNovedad() {
        RequestContext context = RequestContext.getCurrentInstance();
        Conceptos c = conceptoSeleccionadoLov;

        if (!listaConceptosNovedad.isEmpty()) {
            listaConceptosNovedad.clear();
            listaConceptosNovedad.add(c);
            conceptoSeleccionado = listaConceptosNovedad.get(0);
        } else {
            listaConceptosNovedad.add(c);
        }
        empleadoSeleccionadoLov = null;
        llenarTablaNovedades();
        contarRegistrosConceptos();
        filtradosListaConceptosNovedad = null;
        aceptar = true;
        novedadSeleccionada = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        context.reset("formularioDialogos:LOVConceptos:globalFilter");
        context.execute("LOVConceptos.clearFilters()");
        context.update("formularioDialogos:LOVConceptos");
        context.update("formularioDialogos:conceptosDialogo");
        context.update("formularioDialogos:aceptarC");
        context.update("form:datosConceptos");
        context.execute("conceptosDialogo.hide()");
    }

    //AUTOCOMPLETAR
    public void modificarNovedades(Novedades novedad, String confirmarCambio, String valorConfirmar) {

        novedadSeleccionada = novedad;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();

        if (novedadSeleccionada.getFechafinal().compareTo(novedadSeleccionada.getFechainicial()) < 0) {
            System.out.println("La fecha Final es Menor que la Inicial");
            context.update("formularioDialogos:fechas");
            context.execute("fechas.show()");
            novedadSeleccionada.setFechainicial(novedadBackup.getFechainicial());
            novedadSeleccionada.setFechafinal(novedadBackup.getFechafinal());
            context.update("form:datosNovedadesConcepto");
        }

        if (confirmarCambio.equalsIgnoreCase("N")) {
            if (!listaNovedadesCrear.contains(novedadSeleccionada)) {

                if (listaNovedadesModificar.isEmpty()) {
                    listaNovedadesModificar.add(novedadSeleccionada);
                } else if (!listaNovedadesModificar.contains(novedadSeleccionada)) {
                    listaNovedadesModificar.add(novedadSeleccionada);
                }
                if (guardado) {
                    guardado = false;
                    context.update("form:ACEPTAR");
                }
            }

            context.update("form:datosNovedadesConcepto");
        } else if (confirmarCambio.equalsIgnoreCase("FORMULA")) {
            novedadSeleccionada.getFormula().setNombresFormula(formula);
            for (int i = 0; i < lovFormulas.size(); i++) {
                if (lovFormulas.get(i).getNombresFormula().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                novedadSeleccionada.setFormula(lovFormulas.get(indiceUnicoElemento));
                lovFormulas.clear();
                getLovFormulas();
            } else {
                permitirIndex = false;
                contarRegistrosLovFormulas(0);
                context.update("formularioDialogos:formulasDialogo");
                context.execute("formulasDialogo.show()");
                tipoActualizacion = 0;
            }
        } else if (confirmarCambio.equalsIgnoreCase("NIT")) {
            novedadSeleccionada.getTercero().setNitalternativo(nitTercero);

            for (int i = 0; i < lovTerceros.size(); i++) {
                if (lovTerceros.get(i).getNitalternativo().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                novedadSeleccionada.setTercero(lovTerceros.get(indiceUnicoElemento));
                lovTerceros.clear();
                getLovTerceros();
            } else {
                permitirIndex = false;
                contarRegistrosTerceros(0);
                context.update("formularioDialogos:tercerosDialogo");
                context.execute("tercerosDialogo.show()");
                tipoActualizacion = 0;
            }
        } else if (confirmarCambio.equalsIgnoreCase("EMPLEADO")) {

            novedadSeleccionada.getEmpleado().setCodigoempleadoSTR(codigoEmpleado);

            for (int i = 0; i < lovEmpleados.size(); i++) {
                if (lovEmpleados.get(i).getCodigoempleadoSTR().startsWith(valorConfirmar.toString().toUpperCase())) {

                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                novedadSeleccionada.setEmpleado(lovEmpleados.get(indiceUnicoElemento));
                lovEmpleados.clear();
                getLovEmpleados();
            } else {
                permitirIndex = false;
                contarRegistrosEmpleados(0);
                context.update("formularioDialogos:empleadosDialogo");
                context.execute("empleadosDialogo.show()");
                tipoActualizacion = 0;
            }
        } else if (confirmarCambio.equalsIgnoreCase("CODIGOPERIODICIDAD")) {

            novedadSeleccionada.getPeriodicidad().setCodigoStr(CodigoPeriodicidad);

            for (int i = 0; i < lovPeriodicidades.size(); i++) {
                if ((lovPeriodicidades.get(i).getCodigoStr()).startsWith(valorConfirmar.toString().toUpperCase())) {

                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                novedadSeleccionada.setPeriodicidad(lovPeriodicidades.get(indiceUnicoElemento));
                lovPeriodicidades.clear();
                getLovPeriodicidades();
            } else {
                permitirIndex = false;
                contarRegistrosPeriodicidades(0);
                context.update("formularioDialogos:periodicidadesDialogo");
                context.execute("periodicidadesDialogo.show()");
                tipoActualizacion = 0;
            }
        }
        if (coincidencias == 1) {
            if (!listaNovedadesCrear.contains(novedadSeleccionada)) {
                if (listaNovedadesModificar.isEmpty()) {
                    listaNovedadesModificar.add(novedadSeleccionada);
                } else if (!listaNovedadesModificar.contains(novedadSeleccionada)) {
                    listaNovedadesModificar.add(novedadSeleccionada);
                }
                if (guardado) {
                    guardado = false;
                    context.update("form:ACEPTAR");
                }
            }
        }
        context.update("form:datosNovedadesConcepto");
    }

    public void actualizarEmpleados() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            novedadSeleccionada.setEmpleado(empleadoSeleccionadoLov);
            if (!listaNovedadesCrear.contains(novedadSeleccionada)) {
                if (listaNovedadesModificar.isEmpty()) {
                    listaNovedadesModificar.add(novedadSeleccionada);
                } else if (!listaNovedadesModificar.contains(novedadSeleccionada)) {
                    listaNovedadesModificar.add(novedadSeleccionada);
                }
            }
            if (guardado) {
                guardado = false;
                context.update("form:ACEPTAR");
            }
            permitirIndex = true;
            context.update("form:datosNovedadesConcepto");
        } else if (tipoActualizacion == 1) {
            nuevaNovedad.setEmpleado(empleadoSeleccionadoLov);
            context.update("formularioDialogos:nuevaNovedad");
        } else if (tipoActualizacion == 2) {
            duplicarNovedad.setEmpleado(empleadoSeleccionadoLov);
            context.update("formularioDialogos:duplicarNovedad");
        }
        filtradoslistaEmpleados = null;
        empleadoSeleccionadoLov = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        context.reset("formularioDialogos:LOVEmpleados:globalFilter");
        context.execute("LOVEmpleados.clearFilters()");
        context.update("formularioDialogos:LOVEmpleados");
        context.update("formularioDialogos:empleadosDialogo");
        context.update("formularioDialogos:aceptarE");
        context.execute("empleadosDialogo.hide()");
    }

    public void actualizarFormulas() {
        // verificarFormulaConcepto(seleccionConceptos.getSecuencia());
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            novedadSeleccionada.setFormula(formulaSeleccionadaLov);
            if (!listaNovedadesCrear.contains(novedadSeleccionada)) {
                if (listaNovedadesModificar.isEmpty()) {
                    listaNovedadesModificar.add(novedadSeleccionada);
                } else if (!listaNovedadesModificar.contains(novedadSeleccionada)) {
                    listaNovedadesModificar.add(novedadSeleccionada);
                }
            }
            if (guardado) {
                guardado = false;
                context.update("form:ACEPTAR");
            }
            permitirIndex = true;
            context.update("form:datosNovedadesConcepto");
        } else if (tipoActualizacion == 1) {
            nuevaNovedad.setFormula(formulaSeleccionadaLov);
            context.update("formularioDialogos:nuevaNovedad");
        } else if (tipoActualizacion == 2) {
            duplicarNovedad.setFormula(formulaSeleccionadaLov);
            context.update("formularioDialogos:duplicarNovedad");
        }
        filtradoslistaFormulas = null;
        formulaSeleccionadaLov = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        context.reset("formularioDialogos:LOVFormulas:globalFilter");
        context.execute("LOVFormulas.clearFilters()");
        context.update("formularioDialogos:LOVFormulas");
        context.update("formularioDialogos:formulasDialogo");
        context.update("formularioDialogos:aceptarF");
        context.execute("formulasDialogo.hide()");
    }

    public void actualizarPeriodicidades() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            novedadSeleccionada.setPeriodicidad(periodicidadSeleccionada);
            if (!listaNovedadesCrear.contains(novedadSeleccionada)) {
                if (listaNovedadesModificar.isEmpty()) {
                    listaNovedadesModificar.add(novedadSeleccionada);
                } else if (!listaNovedadesModificar.contains(novedadSeleccionada)) {
                    listaNovedadesModificar.add(novedadSeleccionada);
                }
            }
            if (guardado) {
                guardado = false;
                context.update("form:ACEPTAR");
            }
            permitirIndex = true;
            context.update("form:datosNovedadesConcepto");
        } else if (tipoActualizacion == 1) {
            nuevaNovedad.setPeriodicidad(periodicidadSeleccionada);
            context.update("formularioDialogos:nuevaNovedad");
        } else if (tipoActualizacion == 2) {
            duplicarNovedad.setPeriodicidad(periodicidadSeleccionada);
            context.update("formularioDialogos:duplicarNovedad");
        }
        filtradoslistaPeriodicidades = null;
        periodicidadSeleccionada = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        context.reset("formularioDialogos:LOVPeriodicidades:globalFilter");
        context.execute("LOVPeriodicidades.clearFilters()");
        context.update("formularioDialogos:LOVPeriodicidades");
        context.update("formularioDialogos:periodicidadesDialogo");
        context.update("formularioDialogos:aceptarP");
        context.execute("periodicidadesDialogo.hide()");
    }

    public void actualizarTerceros() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            novedadSeleccionada.setTercero(terceroSeleccionado);
            if (!listaNovedadesCrear.contains(novedadSeleccionada)) {
                if (listaNovedadesModificar.isEmpty()) {
                    listaNovedadesModificar.add(novedadSeleccionada);
                } else if (!listaNovedadesModificar.contains(novedadSeleccionada)) {
                    listaNovedadesModificar.add(novedadSeleccionada);
                }
            }
            if (guardado) {
                guardado = false;
                context.update("form:ACEPTAR");
            }
            permitirIndex = true;
            context.update("form:datosNovedadesConcepto");
        } else if (tipoActualizacion == 1) {
            nuevaNovedad.setTercero(terceroSeleccionado);
            context.update("formularioDialogos:nuevaNovedad");
        } else if (tipoActualizacion == 2) {
            duplicarNovedad.setTercero(terceroSeleccionado);
            context.update("formularioDialogos:duplicarNovedad");
        }
        filtradoslistaTerceros = null;
        terceroSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        context.reset("formularioDialogos:LOVTerceros:globalFilter");
        context.execute("LOVTerceros.clearFilters()");
        context.update("formularioDialogos:LOVTerceros");
        context.update("formularioDialogos:tercerosDialogo");
        context.update("formularioDialogos:aceptarT");
        context.execute("tercerosDialogo.hide()");
    }

    //Ubicacion Celda Indice Abajo. //Van los que no son NOT NULL.
    public void cambiarIndice(Novedades novedad, int celda) {
        novedadSeleccionada = novedad;
//        if (permitirIndex == true) {
        cualCelda = celda;
        novedadBackup = novedadSeleccionada;
        if (cualCelda == 0) {
            activarBotonLOV();
            codigoEmpleado = novedadSeleccionada.getEmpleado().getCodigoempleadoSTR();
        } else if (cualCelda == 1) {
            activarBotonLOV();
            descripcionConcepto = novedadSeleccionada.getConcepto().getDescripcion();
        } else if (cualCelda == 3) {
            anularBotonLOV();
            FechaFinal = novedadSeleccionada.getFechafinal();
        } else if (cualCelda == 5) {
            anularBotonLOV();
            Saldo = novedadSeleccionada.getSaldo();
        } else if (cualCelda == 6) {
            activarBotonLOV();
            CodigoPeriodicidad = novedadSeleccionada.getPeriodicidad().getCodigoStr();
        } else if (cualCelda == 7) {
            activarBotonLOV();
            descripcionPeriodicidad = novedadSeleccionada.getPeriodicidad().getNombre();
        } else if (cualCelda == 8) {
            activarBotonLOV();
            nitTercero = novedadSeleccionada.getTercero().getNitalternativo();
        } else if (cualCelda == 9) {
            activarBotonLOV();
            nombreTercero = novedadSeleccionada.getTercero().getNombre();
        } else if (cualCelda == 10) {
            activarBotonLOV();
            formula = novedadSeleccionada.getFormula().getNombrelargo();
        } else if (cualCelda == 11) {
            anularBotonLOV();
            HoraDia = novedadSeleccionada.getUnidadesparteentera();
            MinutoHora = novedadSeleccionada.getUnidadespartefraccion();
        } else {
            anularBotonLOV();
        }
//        }
    }

    //GUARDAR
    public void guardarCambiosNovedades() {
        int pasas = 0;
        if (guardado == false) {
            getResultado();
            if (resultado > 0) {
                RequestContext context = RequestContext.getCurrentInstance();
                context.update("formularioDialogos:solucionesFormulas");
                context.execute("solucionesFormulas.show()");
                listaNovedadesBorrar.clear();
            }
            if (!listaNovedadesBorrar.isEmpty() && pasas == 0) {
                for (int i = 0; i < listaNovedadesBorrar.size(); i++) {
                    if (listaNovedadesBorrar.get(i).getPeriodicidad().getSecuencia() == null) {
                        listaNovedadesBorrar.get(i).setPeriodicidad(null);
                    }
                    if (listaNovedadesBorrar.get(i).getTercero().getSecuencia() == null) {
                        listaNovedadesBorrar.get(i).setTercero(null);
                    }
                    if (listaNovedadesBorrar.get(i).getSaldo() == null) {
                        listaNovedadesBorrar.get(i).setSaldo(null);
                    }
                    if (listaNovedadesBorrar.get(i).getUnidadesparteentera() == null) {
                        listaNovedadesBorrar.get(i).setUnidadesparteentera(null);
                    }
                    if (listaNovedadesBorrar.get(i).getUnidadespartefraccion() == null) {
                        listaNovedadesBorrar.get(i).setUnidadespartefraccion(null);
                    }
                    administrarNovedadesConceptos.borrarNovedades(listaNovedadesBorrar.get(i));
                }
                listaNovedadesBorrar.clear();
            }
            if (!listaNovedadesCrear.isEmpty()) {
                for (int i = 0; i < listaNovedadesCrear.size(); i++) {
                    if (listaNovedadesCrear.get(i).getPeriodicidad().getSecuencia() == null) {
                        listaNovedadesCrear.get(i).setPeriodicidad(null);
                    }
                    if (listaNovedadesCrear.get(i).getTercero().getSecuencia() == null) {
                        listaNovedadesCrear.get(i).setTercero(null);
                    }
                    if (listaNovedadesCrear.get(i).getPeriodicidad().getSecuencia() == null) {
                        listaNovedadesCrear.get(i).setPeriodicidad(null);
                    }
                    if (listaNovedadesCrear.get(i).getSaldo() == null) {
                        listaNovedadesCrear.get(i).setSaldo(null);
                    }
                    if (listaNovedadesCrear.get(i).getUnidadesparteentera() == null) {
                        listaNovedadesCrear.get(i).setUnidadesparteentera(null);
                    }
                    if (listaNovedadesCrear.get(i).getUnidadespartefraccion() == null) {
                        listaNovedadesCrear.get(i).setUnidadespartefraccion(null);
                    }
                    administrarNovedadesConceptos.crearNovedades(listaNovedadesCrear.get(i));
                }
                listaNovedadesCrear.clear();
            }
            if (!listaNovedadesModificar.isEmpty()) {
                administrarNovedadesConceptos.modificarNovedades(listaNovedadesModificar);
                listaNovedadesModificar.clear();
            }
            listaNovedades.clear();
            RequestContext context = RequestContext.getCurrentInstance();
            llenarTablaNovedades();
            guardado = true;
            permitirIndex = true;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            FacesMessage msg = new FacesMessage("Información", "Se guardarón los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            context.update("form:growl");
        }
    }

    //CREAR NOVEDADES
    public void agregarNuevaNovedadConcepto() {
        int pasa = 0;
        int pasa2 = 0;
        mensajeValidacion = new String();
        RequestContext context = RequestContext.getCurrentInstance();

        if (nuevaNovedad.getFechainicial() == null) {
            mensajeValidacion = mensajeValidacion + " * Fecha Inicial\n";
            System.out.println("Fecha Inicial");
            pasa++;
        }

        if (nuevaNovedad.getEmpleado().getCodigoempleado().equals(BigInteger.valueOf(0))) {
            mensajeValidacion = mensajeValidacion + " * Empleado\n";
            System.out.println("Empleado");
            pasa++;
        }

        if (nuevaNovedad.getFormula().getNombrelargo().equals("")) {
            mensajeValidacion = mensajeValidacion + " * Formula\n";
            System.out.println("Formula");
            pasa++;
        }
        if (nuevaNovedad.getValortotal() == null) {
            mensajeValidacion = mensajeValidacion + " * Valor\n";
            System.out.println("Valor");
            pasa++;
        }

        if (nuevaNovedad.getTipo() == null) {
            mensajeValidacion = mensajeValidacion + " * Tipo\n";
            System.out.println("Tipo");
            pasa++;
        }

        if (nuevaNovedad.getEmpleado() != null && pasa == 0) {
            for (int i = 0; i < lovEmpleados.size(); i++) {
                if (nuevaNovedad.getEmpleado().getSecuencia().compareTo(lovEmpleados.get(i).getSecuencia()) == 0) {

                    if (nuevaNovedad.getFechainicial().compareTo(nuevaNovedad.getEmpleado().getFechacreacion()) < 0) {
                        context.update("formularioDialogos:inconsistencia");
                        context.execute("inconsistencia.show()");
                        System.out.println("Inconsistencia Empleado");
                        pasa2++;
                    }
                }
            }
        }

        if (nuevaNovedad.getFechafinal() != null && nuevaNovedad.getFechainicial() != null) {
            if (nuevaNovedad.getFechainicial().compareTo(nuevaNovedad.getFechafinal()) > 0) {
                context.update("formularioDialogos:fechas");
                context.execute("fechas.show()");
                System.out.println("Dialogo de Fechas culas");
                pasa2++;
            }
        }

        if (pasa != 0) {
            context.update("formularioDialogos:validacionNuevaNovedadConcepto");
            context.execute("validacionNuevaNovedadConcepto.show()");
        }

        if (pasa == 0 && pasa2 == 0) {
            System.out.println("Todo esta Bien");
            if (bandera == 1) {
                restaurarTabla();
            }
            //AGREGAR REGISTRO A LA LISTA NOVEDADES .
            k++;
            l = BigInteger.valueOf(k);
            nuevaNovedad.setSecuencia(l);

            // OBTENER EL TERMINAL 
            HttpServletRequest request = (HttpServletRequest) (FacesContext.getCurrentInstance().getExternalContext().getRequest());
            String equipo = null;
            java.net.InetAddress localMachine = null;
            if (request.getRemoteAddr().startsWith("127.0.0.1")) {
                try {
                    localMachine = java.net.InetAddress.getLocalHost();

                } catch (UnknownHostException ex) {
                    Logger.getLogger(ControlNovedadesConceptos.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
                equipo = localMachine.getHostAddress();
            } else {
                equipo = request.getRemoteAddr();
            }
            try {
                localMachine = java.net.InetAddress.getByName(equipo);

            } catch (UnknownHostException ex) {
                Logger.getLogger(ControlNovedadesConceptos.class
                        .getName()).log(Level.SEVERE, null, ex);
            }

            getAlias();
            getUsuarioBD();
            nuevaNovedad.setConcepto(conceptoSeleccionado);
            nuevaNovedad.setTerminal(localMachine.getHostName());
            nuevaNovedad.setUsuarioreporta(usuarioBD);
            listaNovedadesCrear.add(nuevaNovedad);
            listaNovedades.add(nuevaNovedad);
            novedadSeleccionada = listaNovedades.get(listaNovedades.indexOf(nuevaNovedad));
            contarRegistrosNovedades();

            nuevaNovedad = new Novedades();
            nuevaNovedad.setFormula(new Formulas());
            nuevaNovedad.setTercero(new Terceros());
            nuevaNovedad.setFechareporte(new Date());
            nuevaNovedad.setPeriodicidad(new Periodicidades());
            nuevaNovedad.setTipo("FIJA");

            context.update("form:datosNovedadesConcepto");
            if (guardado) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            context.execute("NuevaNovedadConcepto.hide()");
        } else {
        }
    }

    //MOSTRAR DATOS CELDA
    public void editarCelda() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (novedadSeleccionada != null) {
            editarNovedades = novedadSeleccionada;

            if (cualCelda == 0) {
                context.update("formularioDialogos:editarEmpleadosCodigos");
                context.execute("editarEmpleadosCodigos.show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                context.update("formularioDialogos:editarEmpleadosNombres");
                context.execute("editarEmpleadosNombres.show()");
                cualCelda = -1;
            } else if (cualCelda == 2) {
                context.update("formularioDialogos:editFechaInicial");
                context.execute("editFechaInicial.show()");
                cualCelda = -1;
            } else if (cualCelda == 3) {
                context.update("formularioDialogos:editFechasFinales");
                context.execute("editFechasIniciales.show()");
                cualCelda = -1;
            } else if (cualCelda == 4) {
                context.update("formularioDialogos:editarValores");
                context.execute("editarValores.show()");
                cualCelda = -1;
            } else if (cualCelda == 5) {
                context.update("formularioDialogos:editarSaldos");
                context.execute("editarSaldos.show()");
            } else if (cualCelda == 6) {
                context.update("formularioDialogos:editarPeriodicidadesCodigos");
                context.execute("editarPeriodicidadesCodigos.show()");
            } else if (cualCelda == 7) {
                context.update("formularioDialogos:editarPeriodicidadesDescripciones");
                context.execute("editarPeriodicidadesDescripciones.show()");
                cualCelda = -1;
            } else if (cualCelda == 8) {
                context.update("formularioDialogos:editarTercerosNit");
                context.execute("editarTercerosNit.show()");
                cualCelda = -1;
            } else if (cualCelda == 9) {
                context.update("formularioDialogos:editarTercerosNombres");
                context.execute("editarTercerosNombres.show()");
                cualCelda = -1;
            } else if (cualCelda == 10) {
                context.update("formularioDialogos:editarFormulas");
                context.execute("editarFormulas.show()");
                cualCelda = -1;
            } else if (cualCelda == 11) {
                context.update("formularioDialogos:editarHorasDias");
                context.execute("editarHorasDias.show()");
                cualCelda = -1;
            } else if (cualCelda == 12) {
                context.update("formularioDialogos:editarMinutosHoras");
                context.execute("editarMinutosHoras.show()");
                cualCelda = -1;
            } else if (cualCelda == 13) {
                context.update("formularioDialogos:editarTipos");
                context.execute("editarTipos.show()");
                cualCelda = -1;
            }
        } else {
            context.execute("seleccionarRegistro.show()");
        }
    }

    //LISTA DE VALORES DINAMICA
    public void listaValoresBoton() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (novedadSeleccionada != null) {
            if (cualCelda == 0) {
                contarRegistrosEmpleados(0);
                context.update("formularioDialogos:empleadosDialogo");
                context.execute("empleadosDialogo.show()");
                tipoActualizacion = 0;
            } else if (cualCelda == 6) {
                contarRegistrosPeriodicidades(0);
                context.update("formularioDialogos:periodicidadesDialogo");
                context.execute("periodicidadesDialogo.show()");
                tipoActualizacion = 0;
            } else if (cualCelda == 8) {
                contarRegistrosTerceros(0);
                context.update("formularioDialogos:tercerosDialogo");
                context.execute("tercerosDialogo.show()");
                tipoActualizacion = 0;
            } else if (cualCelda == 10) {
                contarRegistrosLovFormulas(0);
                context.update("formularioDialogos:formulasDialogo");
                context.execute("formulasDialogo.show()");
            }
        } else {
            context.execute("seleccionarRegistro.show()");
        }
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();

        if (bandera == 0) {
            nCEmpleadoCodigo = (Column) c.getViewRoot().findComponent("form:datosNovedadesConcepto:nCEmpleadoCodigo");
            nCEmpleadoCodigo.setFilterStyle("width: 85%;");
            nCEmpleadoNombre = (Column) c.getViewRoot().findComponent("form:datosNovedadesConcepto:nCEmpleadoNombre");
            nCEmpleadoNombre.setFilterStyle("85%");
            nCFechasInicial = (Column) c.getViewRoot().findComponent("form:datosNovedadesConcepto:nCFechasInicial");
            nCFechasInicial.setFilterStyle("width: 85%;");
            nCFechasFinal = (Column) c.getViewRoot().findComponent("form:datosNovedadesConcepto:nCFechasFinal");
            nCFechasFinal.setFilterStyle("width: 85%;");
            nCValor = (Column) c.getViewRoot().findComponent("form:datosNovedadesConcepto:nCValor");
            nCValor.setFilterStyle("width: 85%;");
            nCSaldo = (Column) c.getViewRoot().findComponent("form:datosNovedadesConcepto:nCSaldo");
            nCSaldo.setFilterStyle("width: 85%;");
            nCPeriodicidadCodigo = (Column) c.getViewRoot().findComponent("form:datosNovedadesConcepto:nCPeriodicidadCodigo");
            nCPeriodicidadCodigo.setFilterStyle("width: 85%;");
            nCDescripcionPeriodicidad = (Column) c.getViewRoot().findComponent("form:datosNovedadesConcepto:nCDescripcionPeriodicidad");
            nCDescripcionPeriodicidad.setFilterStyle("width: 85%;");
            nCTercerosNit = (Column) c.getViewRoot().findComponent("form:datosNovedadesConcepto:nCTercerosNit");
            nCTercerosNit.setFilterStyle("width: 85%;");
            nCTercerosNombre = (Column) c.getViewRoot().findComponent("form:datosNovedadesConcepto:nCTercerosNombre");
            nCTercerosNombre.setFilterStyle("width: 85%;");
            nCFormulas = (Column) c.getViewRoot().findComponent("form:datosNovedadesConcepto:nCFormulas");
            nCFormulas.setFilterStyle("width: 85%;");
            nCHorasDias = (Column) c.getViewRoot().findComponent("form:datosNovedadesConcepto:nCHorasDias");
            nCHorasDias.setFilterStyle("width: 85%;");
            nCMinutosHoras = (Column) c.getViewRoot().findComponent("form:datosNovedadesConcepto:nCMinutosHoras");
            nCMinutosHoras.setFilterStyle("width: 85%;");
            nCTipo = (Column) c.getViewRoot().findComponent("form:datosNovedadesConcepto:nCTipo");
            nCTipo.setFilterStyle("width: 85%;");
            altoTabla = "135";

            RequestContext.getCurrentInstance().update("form:datosNovedadesConcepto");
            bandera = 1;
            tipoLista = 1;
        } else if (bandera == 1) {
            restaurarTabla();
        }
    }

    //EXPORTAR
    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosNovedadesConceptosExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDFTablasAnchas();
        exporter.export(context, tabla, "NovedadesConceptosPDF", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosNovedadesConceptosExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "NovedadesConceptosXLS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    //LIMPIAR NUEVO REGISTRO NOVEDAD
    public void limpiarNuevaNovedad() {
        nuevaNovedad = new Novedades();
        nuevaNovedad.setFormula(new Formulas());
        nuevaNovedad.setPeriodicidad(new Periodicidades());
        nuevaNovedad.getPeriodicidad().setNombre(" ");
        nuevaNovedad.setTercero(new Terceros());
        nuevaNovedad.getTercero().setNombre(" ");
        nuevaNovedad.setConcepto(new Conceptos());
        nuevaNovedad.getConcepto().setDescripcion(" ");
        nuevaNovedad.setTipo("FIJA");
        nuevaNovedad.setUsuarioreporta(new Usuarios());
        nuevaNovedad.setTerminal(" ");
        nuevaNovedad.setFechareporte(new Date());
        resultado = 0;
    }

    //LIMPIAR DUPLICAR
    /**
     * Metodo que limpia los datos de un duplicar Encargaturas
     */
    public void limpiarduplicarNovedades() {
        duplicarNovedad = new Novedades();
        duplicarNovedad.setPeriodicidad(new Periodicidades());
        duplicarNovedad.getPeriodicidad().setNombre(" ");
        duplicarNovedad.setTercero(new Terceros());
        duplicarNovedad.getTercero().setNombre(" ");
        duplicarNovedad.setConcepto(new Conceptos());
        duplicarNovedad.getConcepto().setDescripcion(" ");
        duplicarNovedad.setTipo("FIJA");
        duplicarNovedad.setUsuarioreporta(new Usuarios());
        duplicarNovedad.setTerminal(" ");
        duplicarNovedad.setFechareporte(new Date());
    }

    //BORRAR NOVEDADES
    public void borrarNovedades() {

        RequestContext context = RequestContext.getCurrentInstance();
        if (novedadSeleccionada != null) {
            if (!listaNovedadesModificar.isEmpty() && listaNovedadesModificar.contains(novedadSeleccionada)) {
                listaNovedadesModificar.remove(novedadSeleccionada);
                listaNovedadesBorrar.add(novedadSeleccionada);
            } else if (!listaNovedadesCrear.isEmpty() && listaNovedadesCrear.contains(novedadSeleccionada)) {
                listaNovedadesCrear.remove(novedadSeleccionada);
            } else {
                listaNovedadesBorrar.add(novedadSeleccionada);
            }
            listaNovedades.remove(novedadSeleccionada);
            if (tipoLista == 1) {
                filtradosListaNovedades.remove(novedadSeleccionada);
            }
            context.update("form:datosNovedadesConcepto");
            novedadSeleccionada = null;
            if (guardado) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            contarRegistrosNovedades();
        } else {
            context.execute("seleccionarRegistro.show()");
        }
    }

    //DUPLICAR ENCARGATURA
    public void duplicarCN() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (novedadSeleccionada != null) {
            duplicarNovedad = new Novedades();

            duplicarNovedad.setEmpleado(novedadSeleccionada.getEmpleado());
            duplicarNovedad.setConcepto(novedadSeleccionada.getConcepto());
            duplicarNovedad.setFechainicial(novedadSeleccionada.getFechainicial());
            duplicarNovedad.setFechafinal(novedadSeleccionada.getFechafinal());
            duplicarNovedad.setFechareporte(novedadSeleccionada.getFechareporte());
            duplicarNovedad.setValortotal(novedadSeleccionada.getValortotal());
            duplicarNovedad.setSaldo(novedadSeleccionada.getSaldo());
            duplicarNovedad.setPeriodicidad(novedadSeleccionada.getPeriodicidad());
            duplicarNovedad.setTercero(novedadSeleccionada.getTercero());
            duplicarNovedad.setFormula(novedadSeleccionada.getFormula());
            duplicarNovedad.setUnidadesparteentera(novedadSeleccionada.getUnidadesparteentera());
            duplicarNovedad.setUnidadespartefraccion(novedadSeleccionada.getUnidadespartefraccion());
            duplicarNovedad.setTipo(novedadSeleccionada.getTipo());
            duplicarNovedad.setTerminal(novedadSeleccionada.getTerminal());
            duplicarNovedad.setUsuarioreporta(novedadSeleccionada.getUsuarioreporta());
            context.update("formularioDialogos:duplicarNovedad");
            context.execute("DuplicarRegistroNovedad.show()");
        } else {
            context.execute("seleccionarRegistro.show()");
        }
    }

    public void confirmarDuplicar() throws UnknownHostException {

        int pasa2 = 0;
        int pasa = 0;
        Empleados emp = new Empleados();
        Empleados emp2 = new Empleados();
        RequestContext context = RequestContext.getCurrentInstance();

        if (duplicarNovedad.getFechainicial() == null) {
            mensajeValidacion = mensajeValidacion + " * Fecha Inicial\n";
            pasa++;
        }
        if (duplicarNovedad.getEmpleado().getPersona().getNombreCompleto().equals(" ")) {
            mensajeValidacion = mensajeValidacion + " * Empleado\n";
            pasa++;
        }
        if (duplicarNovedad.getFormula().getNombrelargo() == null) {
            mensajeValidacion = mensajeValidacion + " * Formula\n";
            pasa++;
        }
        if (duplicarNovedad.getValortotal() == null) {
            mensajeValidacion = mensajeValidacion + " * Valor\n";
            pasa++;
        }

        if (duplicarNovedad.getTipo() == null) {
            mensajeValidacion = mensajeValidacion + " * Tipo\n";
            pasa++;
        }

        for (int i = 0; i < lovEmpleados.size(); i++) {
            if (duplicarNovedad.getEmpleado().getSecuencia().compareTo(lovEmpleados.get(i).getSecuencia()) == 0) {

                if (duplicarNovedad.getFechainicial().compareTo(duplicarNovedad.getEmpleado().getFechacreacion()) < 0) {
                    context.update("formularioDialogos:inconsistencia");
                    context.execute("inconsistencia.show()");
                    pasa2++;
                }
            }
        }

        if (duplicarNovedad.getFechainicial().compareTo(duplicarNovedad.getFechafinal()) > 0) {
            context.update("formularioDialogos:fechas");
            context.execute("fechas.show()");
            pasa2++;
        }
        if (pasa != 0) {
            context.update("formularioDialogos:validacionNuevaNovedadConcepto");
            context.execute("validacionNuevaNovedadConcepto.show()");
        }
        if (pasa2 == 0) {
            k++;
            l = BigInteger.valueOf(k);
            duplicarNovedad.setSecuencia(l);

            listaNovedades.add(duplicarNovedad);
            listaNovedadesCrear.add(duplicarNovedad);
            novedadSeleccionada = listaNovedades.get(listaNovedades.indexOf(duplicarNovedad));
            contarRegistrosNovedades();

            context.update("form:datosNovedadesConcepto");
            if (guardado) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            if (bandera == 1) {
                restaurarTabla();
            }

            // OBTENER EL TERMINAL 
            HttpServletRequest request = (HttpServletRequest) (FacesContext.getCurrentInstance().getExternalContext().getRequest());
            String equipo = null;
            java.net.InetAddress localMachine = null;
            if (request.getRemoteAddr().startsWith("127.0.0.1")) {
                localMachine = java.net.InetAddress.getLocalHost();
                equipo = localMachine.getHostAddress();
            } else {
                equipo = request.getRemoteAddr();
            }
            localMachine = java.net.InetAddress.getByName(equipo);
            getAlias();
            getUsuarioBD();
            duplicarNovedad.setTerminal(localMachine.getHostName());
            duplicarNovedad.setConcepto(conceptoSeleccionado);
            duplicarNovedad = new Novedades();
            context.update("formularioDialogos:DuplicarRegistroNovedad");
            context.execute("DuplicarRegistroNovedad.hide()");
        }
    }

    public void valoresBackupAutocompletar(int tipoNuevo, String Campo) {
        if (Campo.equals("EMPLEADO")) {
            if (tipoNuevo == 1) {
                codigoEmpleado = nuevaNovedad.getEmpleado().getCodigoempleadoSTR();
            } else if (tipoNuevo == 2) {
                codigoEmpleado = duplicarNovedad.getEmpleado().getCodigoempleadoSTR();
            }
        } else if (Campo.equals("CODIGO")) {
            if (tipoNuevo == 1) {
                CodigoPeriodicidad = nuevaNovedad.getPeriodicidad().getCodigoStr();
            } else if (tipoNuevo == 2) {
                CodigoPeriodicidad = duplicarNovedad.getPeriodicidad().getCodigoStr();
            }
        } else if (Campo.equals("NIT")) {
            if (tipoNuevo == 1) {
                nitTercero = nuevaNovedad.getTercero().getNitalternativo();
            } else if (tipoNuevo == 2) {
                nitTercero = duplicarNovedad.getTercero().getNitalternativo();
            }
        } else if (Campo.equals("FORMULAS")) {
            if (tipoNuevo == 1) {
                formula = nuevaNovedad.getFormula().getNombrelargo();
            } else if (tipoNuevo == 2) {
                formula = duplicarNovedad.getFormula().getNombrelargo();
            }
        }

    }

    public void autocompletarNuevoyDuplicado(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("FORMULA")) {
            if (tipoNuevo == 1) {
                nuevaNovedad.getFormula().setNombrelargo(formula);
            } else if (tipoNuevo == 2) {
                duplicarNovedad.getFormula().setNombrelargo(formula);
            }
            for (int i = 0; i < lovFormulas.size(); i++) {
                if (lovFormulas.get(i).getNombrelargo().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevaNovedad.setFormula(lovFormulas.get(indiceUnicoElemento));
                    context.update("formularioDialogos:nuevaFormula");
                } else if (tipoNuevo == 2) {
                    duplicarNovedad.setFormula(lovFormulas.get(indiceUnicoElemento));
                    context.update("formularioDialogos:duplicarFormula");
                }
                lovFormulas.clear();
                getLovFormulas();
            } else {
                contarRegistrosLovFormulas(0);
                context.update("form:formulasDialogo");
                context.execute("formulasDialogo.show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    context.update("formularioDialogos:nuevaFormula");
                } else if (tipoNuevo == 2) {
                    context.update("formularioDialogos:duplicarFormula");
                }
            }
        } else if (confirmarCambio.equalsIgnoreCase("NIT")) {
            if (tipoNuevo == 1) {
                nuevaNovedad.getTercero().setNitalternativo(nitTercero);
            } else if (tipoNuevo == 2) {
                duplicarNovedad.getTercero().setNitalternativo(nitTercero);
            }

            for (int i = 0; i < lovTerceros.size(); i++) {
                if (lovTerceros.get(i).getNitalternativo().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevaNovedad.setTercero(lovTerceros.get(indiceUnicoElemento));
                    context.update("formularioDialogos:nuevoTerceroNit");
                    context.update("formularioDialogos:nuevoTerceroNombre");
                } else if (tipoNuevo == 2) {
                    duplicarNovedad.setTercero(lovTerceros.get(indiceUnicoElemento));
                    context.update("formularioDialogos:duplicarTerceroNit");
                    context.update("formularioDialogos:duplicarTerceroNombre");
                }
                lovTerceros.clear();
                getLovTerceros();
            } else {
                contarRegistrosTerceros(0);
                context.update("form:tercerosDialogo");
                context.execute("tercerosDialogo.show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    context.update("formularioDialogos:nuevoTerceroNit");
                    context.update("formularioDialogos:nuevoTerceroNombre");

                } else if (tipoNuevo == 2) {
                    context.update("formularioDialogos:duplicarTerceroNit");
                    context.update("formularioDialogos:duplicarTerceroNombre");
                }
            }
        } else if (confirmarCambio.equalsIgnoreCase("CODIGO")) {
            if (tipoNuevo == 1) {
                nuevaNovedad.getPeriodicidad().setCodigoStr(CodigoPeriodicidad);
            } else if (tipoNuevo == 2) {
                duplicarNovedad.getPeriodicidad().setCodigoStr(CodigoPeriodicidad);
            }
            for (int i = 0; i < lovPeriodicidades.size(); i++) {
                if (lovPeriodicidades.get(i).getCodigoStr().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevaNovedad.setPeriodicidad(lovPeriodicidades.get(indiceUnicoElemento));
                    context.update("formularioDialogos:nuevaPeriodicidadCodigo");
                    context.update("formularioDialogos:nuevaPeriodicidadDescripcion");
                } else if (tipoNuevo == 2) {
                    duplicarNovedad.setPeriodicidad(lovPeriodicidades.get(indiceUnicoElemento));
                    context.update("formularioDialogos:duplicarPeriodicidadCodigo");
                    context.update("formularioDialogos:duplicarPeriodicidadDescripcion");
                }
                lovPeriodicidades.clear();
                getLovPeriodicidades();
            } else {
                contarRegistrosPeriodicidades(0);
                context.update("form:periodicidadesDialogo");
                context.execute("periodicidadesDialogo.show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    context.update("formularioDialogos:nuevaPeriodicidadCodigo");
                    context.update("formularioDialogos:nuevaPeriodicidadDescripcion");
                } else if (tipoNuevo == 2) {
                    context.update("formularioDialogos:duplicarPeriodicidadCodigo");
                    context.update("formularioDialogos:duplicarPeriodicidadDescripcion");
                }
            }
        } else if (confirmarCambio.equalsIgnoreCase("EMPLEADO")) {
            if (tipoNuevo == 1) {
                nuevaNovedad.getEmpleado().setCodigoempleadoSTR(codigoEmpleado);
            } else if (tipoNuevo == 2) {
                duplicarNovedad.getEmpleado().setCodigoempleadoSTR(codigoEmpleado);
            }

            for (int i = 0; i < lovEmpleados.size(); i++) {
                if (lovEmpleados.get(i).getCodigoempleadoSTR().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevaNovedad.setEmpleado(lovEmpleados.get(indiceUnicoElemento));
                    context.update("formularioDialogos:nuevoEmpleadoCodigo");
                    context.update("formularioDialogos:nuevoEmpleadoNombre");
                } else if (tipoNuevo == 2) {
                    duplicarNovedad.setEmpleado(lovEmpleados.get(indiceUnicoElemento));
                    context.update("formularioDialogos:duplicarEmpleadoCodigo");
                    context.update("formularioDialogos:duplicarEmpleadoNombre");
                }
                lovEmpleados.clear();
                getLovEmpleados();
            } else {
                contarRegistrosEmpleados(0);
                context.update("form:empleadosDialogo");
                context.execute("empleadosDialogo.show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    context.update("formularioDialogos:nuevoEmpleadoCodigo");
                    context.update("formularioDialogos:nuevoEmpleadoNombre");
                } else if (tipoNuevo == 2) {
                    context.update("formularioDialogos:duplicarEmpleadoNombre");
                    context.update("formularioDialogos:duplicarEmpleadoCodigo");
                }
            }
        }
    }

    //RASTROS 
    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (novedadSeleccionada != null) {
            int result = administrarRastros.obtenerTabla(novedadSeleccionada.getSecuencia(), "NOVEDADES");
            if (result == 1) {
                context.execute("errorObjetosDB.show()");
            } else if (result == 2) {
                context.execute("confirmarRastro.show()");
            } else if (result == 3) {
                context.execute("errorRegistroRastro.show()");
            } else if (result == 4) {
                context.execute("errorTablaConRastro.show()");
            } else if (result == 5) {
                context.execute("errorTablaSinRastro.show()");
            }
//            } else {
//                context.execute("seleccionarRegistro.show()");
//            }
        } else {
            if (administrarRastros.verificarHistoricosTabla("NOVEDADES")) {
                context.execute("confirmarRastroHistorico.show()");
            } else {
                context.execute("errorRastroHistorico.show()");
            }
        }
    }

    //CANCELAR MODIFICACIONES
    public void cancelarModificacion() {
        restaurarTabla();
        listaNovedadesBorrar.clear();
        listaNovedadesCrear.clear();
        listaNovedadesModificar.clear();
        if (!listaConceptosNovedad.isEmpty()) {
            conceptoSeleccionado = listaConceptosNovedad.get(0);
        }
        novedadSeleccionada = null;
//        k = 0;
        listaNovedades = new ArrayList<Novedades>();
        guardado = true;
        permitirIndex = true;
        resultado = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        contarRegistrosNovedades();
        context.update("form:datosNovedadesConcepto");
        context.update("form:datosConceptos");
        mostrarTodos();
    }

    public void salir() {
        if (bandera == 1) {
            restaurarTabla();
        }
        listaNovedadesBorrar.clear();
        listaNovedadesCrear.clear();
        listaNovedadesModificar.clear();
        listaNovedades.clear();
        lovConceptos = null;
        listaConceptosNovedad = null;
        lovEmpleados = null;
        lovFormulas = null;
        lovPeriodicidades = null;
        lovTerceros = null;
        tipoLista = 0;
        novedadSeleccionada = null;
//        k = 0;
        resultado = 0;
        guardado = true;
        permitirIndex = true;
    }

    public void restaurarTabla() {
        FacesContext c = FacesContext.getCurrentInstance();

        nCEmpleadoCodigo = (Column) c.getViewRoot().findComponent("form:datosNovedadesConcepto:nCEmpleadoCodigo");
        nCEmpleadoCodigo.setFilterStyle("display: none; visibility: hidden;");
        nCEmpleadoNombre = (Column) c.getViewRoot().findComponent("form:datosNovedadesConcepto:nCEmpleadoNombre");
        nCEmpleadoNombre.setFilterStyle("display: none; visibility: hidden;");
        nCFechasInicial = (Column) c.getViewRoot().findComponent("form:datosNovedadesConcepto:nCFechasInicial");
        nCFechasInicial.setFilterStyle("display: none; visibility: hidden;");
        nCFechasFinal = (Column) c.getViewRoot().findComponent("form:datosNovedadesConcepto:nCFechasFinal");
        nCFechasFinal.setFilterStyle("display: none; visibility: hidden;");
        nCValor = (Column) c.getViewRoot().findComponent("form:datosNovedadesConcepto:nCValor");
        nCValor.setFilterStyle("display: none; visibility: hidden;");
        nCSaldo = (Column) c.getViewRoot().findComponent("form:datosNovedadesConcepto:nCSaldo");
        nCSaldo.setFilterStyle("display: none; visibility: hidden;");
        nCPeriodicidadCodigo = (Column) c.getViewRoot().findComponent("form:datosNovedadesConcepto:nCPeriodicidadCodigo");
        nCPeriodicidadCodigo.setFilterStyle("display: none; visibility: hidden;");
        nCDescripcionPeriodicidad = (Column) c.getViewRoot().findComponent("form:datosNovedadesConcepto:nCDescripcionPeriodicidad");
        nCDescripcionPeriodicidad.setFilterStyle("display: none; visibility: hidden;");
        nCTercerosNit = (Column) c.getViewRoot().findComponent("form:datosNovedadesConcepto:nCTercerosNit");
        nCTercerosNit.setFilterStyle("display: none; visibility: hidden;");
        nCTercerosNombre = (Column) c.getViewRoot().findComponent("form:datosNovedadesConcepto:nCTercerosNombre");
        nCTercerosNombre.setFilterStyle("display: none; visibility: hidden;");
        nCFormulas = (Column) c.getViewRoot().findComponent("form:datosNovedadesConcepto:nCFormulas");
        nCFormulas.setFilterStyle("display: none; visibility: hidden;");
        nCHorasDias = (Column) c.getViewRoot().findComponent("form:datosNovedadesConcepto:nCHorasDias");
        nCHorasDias.setFilterStyle("display: none; visibility: hidden;");
        nCMinutosHoras = (Column) c.getViewRoot().findComponent("form:datosNovedadesConcepto:nCMinutosHoras");
        nCMinutosHoras.setFilterStyle("display: none; visibility: hidden;");
        nCTipo = (Column) c.getViewRoot().findComponent("form:datosNovedadesConcepto:nCTipo");
        nCTipo.setFilterStyle("display: none; visibility: hidden;");
        altoTabla = "155";

        RequestContext.getCurrentInstance().update("form:datosNovedadesConcepto");
        bandera = 0;
        filtradosListaNovedades = null;
        tipoLista = 0;
    }

    public void todasNovedades() {
        listaNovedades.clear();
        listaNovedades = administrarNovedadesConceptos.todasNovedadesConcepto(conceptoSeleccionado.getSecuencia());
        RequestContext context = RequestContext.getCurrentInstance();
        todas = true;
        actuales = false;
        context.update("form:datosNovedadesConcepto");
        context.update("form:TODAS");
        context.update("form:ACTUALES");
    }

    public void actualesNovedades() {
        listaNovedades.clear();
        listaNovedades = administrarNovedadesConceptos.novedadesConcepto(conceptoSeleccionado.getSecuencia());
        RequestContext context = RequestContext.getCurrentInstance();
        todas = false;
        actuales = true;
        context.update("form:datosNovedadesConcepto");
        context.update("form:TODAS");
        context.update("form:ACTUALES");
    }

    public void abrirDialogo() {
        RequestContext context = RequestContext.getCurrentInstance();
        Formulas formula;
        formula = verificarFormulaConcepto(conceptoSeleccionado.getSecuencia());
        nuevaNovedad.setFormula(formula);
        context.update("formularioDialogos:nuevaNovedad");
        context.execute("NuevaNovedadConcepto.show()");

    }

    //CARGAR FORMULA AUTOMATICAMENTE
    public Formulas verificarFormulaConcepto(BigInteger secCon) {
        List<FormulasConceptos> formulasConceptoSel = administrarFormulaConcepto.cargarFormulasConcepto(secCon);
        Formulas formulaR = new Formulas();
        BigInteger autoFormula;

        if (formulasConceptoSel != null) {
            if (!formulasConceptoSel.isEmpty()) {
                autoFormula = formulasConceptoSel.get(0).getFormula();
            } else {
                autoFormula = new BigInteger("4621544");
            }
        } else {
            autoFormula = new BigInteger("4621544");
        }

        for (int i = 0; i < lovFormulas.size(); i++) {
            if (autoFormula.equals(lovFormulas.get(i).getSecuencia())) {
                formulaR = lovFormulas.get(i);
            }
        }
        return formulaR;
    }

    //EVENTO FILTRAR
    public void eventoFiltrar() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
        anularBotonLOV();
        contarRegistrosNovedades();
        novedadSeleccionada = null;
    }

    public void eventoFiltrarLovCon() {
        contarRegistrosLovConceptos(1);
    }

    public void eventoFiltrarLovForm() {
        contarRegistrosLovFormulas(1);
    }

    public void eventoFiltrarLovPer() {
        contarRegistrosPeriodicidades(1);
    }

    public void eventoFiltrarLovEmp() {
        contarRegistrosEmpleados(1);
    }

    public void eventoFiltrarLovTer() {
        contarRegistrosTerceros(1);
    }

    // Conteo de registros : 
    public void contarRegistrosConceptos() {
        if (listaConceptosNovedad != null) {
            infoRegistroConceptos = String.valueOf(listaConceptosNovedad.size());
        } else {
            infoRegistroConceptos = String.valueOf(0);
        }
        RequestContext.getCurrentInstance().update("form:infoRegistro");
    }

    public void contarRegistrosNovedades() {
        if (tipoLista == 1) {
            infoRegistroNovedades = String.valueOf(filtradosListaNovedades.size());
        } else if (listaNovedades != null) {
            infoRegistroNovedades = String.valueOf(listaNovedades.size());
        } else {
            infoRegistroNovedades = String.valueOf(0);
        }
        RequestContext.getCurrentInstance().update("form:infoRegistroNovedades");
    }

    public void contarRegistrosLovConceptos(int tipoAct) {
        if (tipoAct == 1) {
            infoRegistroLovConceptos = String.valueOf(filtradoslistaConceptos.size());
        } else if (lovConceptos != null) {
            infoRegistroLovConceptos = String.valueOf(lovConceptos.size());
        } else {
            infoRegistroLovConceptos = String.valueOf(0);
        }
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroConceptos");
    }

    public void contarRegistrosLovFormulas(int tipoAct) {
        if (tipoAct == 1) {
            infoRegistroLovFormulas = String.valueOf(filtradoslistaFormulas.size());
        } else if (lovFormulas != null) {
            infoRegistroLovFormulas = String.valueOf(lovFormulas.size());
        } else {
            infoRegistroLovFormulas = String.valueOf(0);
        }
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroFormulas");
    }

    public void contarRegistrosEmpleados(int tipoAct) {
        if (tipoAct == 1) {
            infoRegistroLovEmpleados = String.valueOf(filtradoslistaEmpleados.size());
        } else if (lovEmpleados != null) {
            infoRegistroLovEmpleados = String.valueOf(lovEmpleados.size());
        } else {
            infoRegistroLovEmpleados = String.valueOf(0);
        }
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroEmpleados");
    }

    public void contarRegistrosPeriodicidades(int tipoAct) {
        if (tipoAct == 1) {
            infoRegistroLovPeriodicidades = String.valueOf(filtradoslistaPeriodicidades.size());
        } else if (lovPeriodicidades != null) {
            infoRegistroLovPeriodicidades = String.valueOf(lovPeriodicidades.size());
        } else {
            infoRegistroLovPeriodicidades = String.valueOf(0);
        }
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroPeriodicidades");
    }

    public void contarRegistrosTerceros(int tipoAct) {
        if (tipoAct == 1) {
            infoRegistroLovTerceros = String.valueOf(filtradoslistaTerceros.size());
        } else if (lovTerceros != null) {
            infoRegistroLovTerceros = String.valueOf(lovTerceros.size());
        } else {
            infoRegistroLovTerceros = String.valueOf(0);
        }
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroTerceros");
    }

    public void anularBotonLOV() {
        activarLOV = true;
        RequestContext.getCurrentInstance().update("form:listaValores");
    }

    public void activarBotonLOV() {
        activarLOV = false;
        RequestContext.getCurrentInstance().update("form:listaValores");
    }

    public void llenarTablaNovedades() {
        if (conceptoSeleccionado != null) {
            listaNovedades = administrarNovedadesConceptos.novedadesConcepto(conceptoSeleccionado.getSecuencia());
        }
        novedadSeleccionada = null;
        anularBotonLOV();
        contarRegistrosNovedades();
        RequestContext.getCurrentInstance().update("form:datosNovedadesConcepto");
    }

    //GETTER & SETTER
    public List<Conceptos> getListaConceptosNovedad() {
        if (listaConceptosNovedad == null) {
            listaConceptosNovedad = administrarNovedadesConceptos.Conceptos();
        }
        return listaConceptosNovedad;
    }

    public void setListaConceptosNovedad(List<Conceptos> listaConceptosNovedad) {
        this.listaConceptosNovedad = listaConceptosNovedad;
    }

    public List<Conceptos> getFiltradosListaConceptosNovedad() {
        return filtradosListaConceptosNovedad;
    }

    public void setFiltradosListaConceptosNovedad(List<Conceptos> filtradosListaConceptosNovedad) {
        this.filtradosListaConceptosNovedad = filtradosListaConceptosNovedad;
    }

    public Conceptos getConceptoSeleccionado() {
        return conceptoSeleccionado;
    }

    public void setConceptoSeleccionado(Conceptos seleccionMostrar) {
        this.conceptoSeleccionado = seleccionMostrar;
    }

    public List<Novedades> getListaNovedades() {
        return listaNovedades;
    }

    public void setListaNovedades(List<Novedades> listaNovedades) {
        this.listaNovedades = listaNovedades;
    }

    public List<Novedades> getFiltradosListaNovedades() {
        return filtradosListaNovedades;
    }

    public void setFiltradosListaNovedades(List<Novedades> filtradosListaNovedades) {
        this.filtradosListaNovedades = filtradosListaNovedades;
    }

    public List<Novedades> getListaNovedadesCrear() {
        return listaNovedadesCrear;
    }

    public void setListaNovedadesCrear(List<Novedades> listaNovedadesCrear) {
        this.listaNovedadesCrear = listaNovedadesCrear;
    }

    public String getMensajeValidacion() {
        return mensajeValidacion;
    }

    public void setMensajeValidacion(String mensajeValidacion) {
        this.mensajeValidacion = mensajeValidacion;
    }

    public List<Novedades> getListaNovedadesModificar() {
        return listaNovedadesModificar;
    }

    public void setListaNovedadesModificar(List<Novedades> listaNovedadesModificar) {
        this.listaNovedadesModificar = listaNovedadesModificar;
    }

    public List<Novedades> getListaNovedadesBorrar() {
        return listaNovedadesBorrar;
    }

    public void setListaNovedadesBorrar(List<Novedades> listaNovedadesBorrar) {
        this.listaNovedadesBorrar = listaNovedadesBorrar;
    }

    public List<Empleados> getLovEmpleados() {
        if (lovEmpleados == null) {
            lovEmpleados = administrarNovedadesConceptos.lovEmpleados();
        }
        return lovEmpleados;
    }

    public void setLovEmpleados(List<Empleados> listaEmpleados) {
        this.lovEmpleados = listaEmpleados;
    }

    public List<Empleados> getFiltradoslistaEmpleados() {
        return filtradoslistaEmpleados;
    }

    public void setFiltradoslistaEmpleados(List<Empleados> filtradoslistaEmpleados) {
        this.filtradoslistaEmpleados = filtradoslistaEmpleados;
    }

    public Empleados getEmpleadoSeleccionadoLov() {
        return empleadoSeleccionadoLov;
    }

    public void setEmpleadoSeleccionadoLov(Empleados seleccionEmpleados) {
        this.empleadoSeleccionadoLov = seleccionEmpleados;
    }

    public List<Periodicidades> getLovPeriodicidades() {
        if (lovPeriodicidades == null) {
            lovPeriodicidades = administrarNovedadesConceptos.lovPeriodicidades();
        }
        return lovPeriodicidades;
    }

    public void setLovPeriodicidades(List<Periodicidades> listaPeriodicidades) {
        this.lovPeriodicidades = listaPeriodicidades;
    }

    public List<Periodicidades> getFiltradoslistaPeriodicidades() {
        return filtradoslistaPeriodicidades;
    }

    public void setFiltradoslistaPeriodicidades(List<Periodicidades> filtradoslistaPeriodicidades) {
        this.filtradoslistaPeriodicidades = filtradoslistaPeriodicidades;
    }

    public Periodicidades getPeriodicidadSeleccionada() {
        return periodicidadSeleccionada;
    }

    public void setPeriodicidadSeleccionada(Periodicidades seleccionPeriodicidades) {
        this.periodicidadSeleccionada = seleccionPeriodicidades;
    }

    public List<Terceros> getLovTerceros() {
        if (lovTerceros == null) {
            lovTerceros = administrarNovedadesConceptos.lovTerceros();
        }
        return lovTerceros;
    }

    public void setLovTerceros(List<Terceros> listaTerceros) {
        this.lovTerceros = listaTerceros;
    }

    public List<Terceros> getFiltradoslistaTerceros() {
        return filtradoslistaTerceros;
    }

    public void setFiltradoslistaTerceros(List<Terceros> filtradoslistaTerceros) {
        this.filtradoslistaTerceros = filtradoslistaTerceros;
    }

    public Terceros getTerceroSeleccionado() {
        return terceroSeleccionado;
    }

    public void setTerceroSeleccionado(Terceros seleccionTerceros) {
        this.terceroSeleccionado = seleccionTerceros;
    }

    public List<Formulas> getLovFormulas() {
        if (lovFormulas == null) {
            lovFormulas = administrarNovedadesConceptos.lovFormulas();
        }
        return lovFormulas;
    }

    public void setLovFormulas(List<Formulas> listaFormulas) {
        this.lovFormulas = listaFormulas;
    }

    public List<Formulas> getFiltradoslistaFormulas() {
        return filtradoslistaFormulas;
    }

    public void setFiltradoslistaFormulas(List<Formulas> filtradoslistaFormulas) {
        this.filtradoslistaFormulas = filtradoslistaFormulas;
    }

    public Formulas getFormulaSeleccionadaLov() {
        return formulaSeleccionadaLov;
    }

    public void setFormulaSeleccionadaLov(Formulas seleccionFormulas) {
        this.formulaSeleccionadaLov = seleccionFormulas;
    }

    public List<Conceptos> getLovConceptos() {
        if (lovConceptos == null) {
            lovConceptos = administrarNovedadesConceptos.Conceptos();
        }
        return lovConceptos;
    }

    public void setLovConceptos(List<Conceptos> listaConceptos) {
        this.lovConceptos = listaConceptos;
    }

    public List<Conceptos> getFiltradoslistaConceptos() {
        return filtradoslistaConceptos;
    }

    public void setFiltradoslistaConceptos(List<Conceptos> filtradoslistaConceptos) {
        this.filtradoslistaConceptos = filtradoslistaConceptos;
    }

    public Conceptos getConceptoSeleccionadoLov() {
        return conceptoSeleccionadoLov;
    }

    public void setConceptoSeleccionadoLov(Conceptos seleccionConceptos) {
        this.conceptoSeleccionadoLov = seleccionConceptos;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public void setAceptar(boolean aceptar) {
        this.aceptar = aceptar;
    }

    public int getResultado() {
        if (!listaNovedadesBorrar.isEmpty()) {
            for (int i = 0; i < listaNovedadesBorrar.size(); i++) {
                resultado = administrarNovedadesConceptos.solucionesFormulas(listaNovedadesBorrar.get(i).getSecuencia());
            }
        }
        return resultado;
    }

    public void setResultado(int resultado) {
        this.resultado = resultado;
    }
//Terminal y Usuario

    public String getAlias() {
        alias = administrarNovedadesConceptos.alias();
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Usuarios getUsuarioBD() {
        getAlias();
        usuarioBD = administrarNovedadesConceptos.usuarioBD(alias);
        return usuarioBD;
    }

    public void setUsuarioBD(Usuarios usuarioBD) {
        this.usuarioBD = usuarioBD;
    }

    public Novedades getNuevaNovedad() {
        return nuevaNovedad;
    }

    public void setNuevaNovedad(Novedades nuevaNovedad) {
        this.nuevaNovedad = nuevaNovedad;
    }

    public boolean isTodas() {
        return todas;
    }

    public boolean isActuales() {
        return actuales;
    }

    public Novedades getEditarNovedades() {
        return editarNovedades;
    }

    public void setEditarNovedades(Novedades editarNovedades) {
        this.editarNovedades = editarNovedades;
    }

    public Novedades getDuplicarNovedad() {
        return duplicarNovedad;
    }

    public void setDuplicarNovedad(Novedades duplicarNovedad) {
        this.duplicarNovedad = duplicarNovedad;
    }

    public Novedades getNovedadSeleccionada() {
        return novedadSeleccionada;
    }

    public void setNovedadSeleccionada(Novedades novedadSeleccionada) {
        this.novedadSeleccionada = novedadSeleccionada;
    }

    public boolean isGuardado() {
        return guardado;
    }

    public void setGuardado(boolean guardado) {
        this.guardado = guardado;
    }

    public String getAltoTabla() {
        return altoTabla;
    }

    public void setAltoTabla(String altoTabla) {
        this.altoTabla = altoTabla;
    }

    public String getInfoRegistroConceptos() {
        return infoRegistroConceptos;
    }

    public String getInfoRegistroNovedades() {
        return infoRegistroNovedades;
    }

    public String getInfoRegistroLovConceptos() {
        return infoRegistroLovConceptos;
    }

    public String getInfoRegistroLovFormulas() {
        return infoRegistroLovFormulas;
    }

    public String getInfoRegistroLovEmpleados() {
        return infoRegistroLovEmpleados;
    }

    public String getInfoRegistroLovPeriodicidades() {
        return infoRegistroLovPeriodicidades;
    }

    public String getInfoRegistroLovTerceros() {
        return infoRegistroLovTerceros;
    }

    public boolean isActivarLOV() {
        return activarLOV;
    }

    public void setActivarLOV(boolean activarLOV) {
        this.activarLOV = activarLOV;
    }
}
