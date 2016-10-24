/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Empleados;
import Entidades.NovedadesSistema;
import Entidades.Vacaciones;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarNovedadesSistemaInterface;
import InterfaceAdministrar.AdministrarNovedadesVacacionesInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
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
public class ControlNovedadesVacaciones implements Serializable {

    @EJB
    AdministrarNovedadesVacacionesInterface administrarNovedadesVacaciones;
    @EJB
    AdministrarRastrosInterface administrarRastros;
    @EJB
    AdministrarNovedadesSistemaInterface administrarNovedadesSistema;
    //SECUENCIA DE LA PERSONA
    private BigInteger secuenciaEmpleado;
    //Lista Empleados Novedad Vacaciones
    private List<Empleados> listaEmpleadosNovedad;
    private List<Empleados> filtradosListaEmpleadosNovedad;
    private Empleados empleadoSeleccionado;
    //LISTA NOVEDADES
    private List<NovedadesSistema> listaNovedades;
    private List<NovedadesSistema> filtradosListaNovedades;
    private NovedadesSistema novedadSeleccionada;
    //Duplicar
    private NovedadesSistema duplicarNovedad;
    //editar celda
    private NovedadesSistema editarNovedades;
    private int cualCelda, tipoLista;
    //Crear Novedades
    private List<NovedadesSistema> listaNovedadesCrear;
    public NovedadesSistema nuevaNovedad;
    private int paraNuevaNovedad;
    private BigInteger nuevaNovedadSec;
    private String mensajeValidacion;
    //Modificar Novedades
    private List<NovedadesSistema> listaNovedadesModificar;
    //Borrar Novedades
    private List<NovedadesSistema> listaNovedadesBorrar;
    //OTROS
    private boolean aceptar;
    private int tipoActualizacion; //Activo/Desactivo Crtl + F11
    private int bandera;
    private boolean permitirIndex;
    //RASTROS
    private boolean guardado;
    //guardarOk;
    //LOV EMPLEADOS
    private List<Empleados> listaValEmpleados;
    private List<Empleados> filtradoslistaValEmpleados;
    private Empleados empleadoSeleccionadoLOV;
    //LOV PERIODOS
    private List<Vacaciones> listaPeriodos;
    private List<Vacaciones> filtradoslistaPeriodos;
    private Vacaciones periodoSeleccionado;
    //Columnas Tabla NOVEDADES
    private Column nEFechaInicialDisfrute, nEPeriodo, nEDias, nEFechaSiguiente,
            nESubTipo, nEAdelantoHasta, nEFechaPago, nEDiasAplazados;
    private Date suma364;
    private Date finalsuma364;
    private BigInteger diasTotales;
    private short diasAplazadosTotal;
    private final String cero;
    //ALTO SCROLL TABLA
    private String altoTabla;
    // Para volver:
    private String paginaAnterior;
    // activar mostrar todos:
    private boolean activarMTodos;
    // fecha contratacion empleado
    private Date fechaContratacionE;
    private String infoRegistroEmpleados, infoRegistroNovedades, infoRegistroEmpleadosLOV,infoRegistroPeriodo;

    public ControlNovedadesVacaciones() {
        cero = "0";
        listaEmpleadosNovedad = null;
        listaNovedadesBorrar = new ArrayList<NovedadesSistema>();
        listaNovedadesCrear = new ArrayList<NovedadesSistema>();
        listaNovedadesModificar = new ArrayList<NovedadesSistema>();
        aceptar = true;
        guardado = true;
        tipoLista = 0;
        listaValEmpleados = null;
        permitirIndex = true;
        nuevaNovedad = new NovedadesSistema();
        nuevaNovedad.setSubtipo("TIEMPO");
        nuevaNovedad.setTipo("VACACION");
        nuevaNovedad.setVacacion(new Vacaciones());
        nuevaNovedad.setVacadiasaplazados(Short.valueOf(cero));
        nuevaNovedad.setDias(BigInteger.valueOf(0));
        duplicarNovedad = new NovedadesSistema();
        duplicarNovedad.setSubtipo("TIEMPO");
        duplicarNovedad.setTipo("VACACION");
        duplicarNovedad.setVacacion(new Vacaciones());
        duplicarNovedad.setVacadiasaplazados(Short.valueOf(cero));
        duplicarNovedad.setDias(BigInteger.valueOf(0));
        diasTotales = BigInteger.valueOf(0);
        diasAplazadosTotal = Short.parseShort(cero);
        altoTabla = "125";
        paginaAnterior = "";
        activarMTodos = true;
        novedadSeleccionada = null;
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarNovedadesVacaciones.obtenerConexion(ses.getId());
            administrarNovedadesSistema.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void recibirPag(String pag) {
        paginaAnterior = pag;
        listaNovedades = null;
        getListaNovedades();
       if (listaEmpleadosNovedad != null) {
            if (!listaEmpleadosNovedad.isEmpty()) {
                empleadoSeleccionado = listaEmpleadosNovedad.get(0);
            }
        }
    }

    public String volverPagAnterior() {
        return paginaAnterior;
    }

    public void editarCelda() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (novedadSeleccionada != null) {
            editarNovedades = novedadSeleccionada;
            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editFechaInicial");
                RequestContext.getCurrentInstance().execute("PF('editFechaInicial').show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editPeriodo");
                RequestContext.getCurrentInstance().execute("PF('editPeriodo').show()");
                cualCelda = -1;
            } else if (cualCelda == 2) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editDias");
                RequestContext.getCurrentInstance().execute("PF('editDias').show()");
                cualCelda = -1;
            } else if (cualCelda == 3) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editFechaSiguiente");
                RequestContext.getCurrentInstance().execute("PF('editFechaSiguiente').show()");
                cualCelda = -1;
            } else if (cualCelda == 4) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editSubtipo");
                RequestContext.getCurrentInstance().execute("PF('editSubtipo').show()");
                cualCelda = -1;
            } else if (cualCelda == 5) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editAdelantoHasta");
                RequestContext.getCurrentInstance().execute("PF('editAdelantoHasta').show()");
                cualCelda = -1;
            } else if (cualCelda == 6) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editFechaPago");
                RequestContext.getCurrentInstance().execute("PF('editFechaPago').show()");
            } else if (cualCelda == 7) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarDA");
                RequestContext.getCurrentInstance().execute("PF('editarDA').show()");
            }
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    //LIMPIAR NUEVO REGISTRO NOVEDAD
    public void limpiarNuevaNovedad() {
        String cero = "0";
        nuevaNovedad = new NovedadesSistema();
        nuevaNovedad.setSubtipo("TIEMPO");
        nuevaNovedad.setTipo("VACACION");
        nuevaNovedad.setVacacion(new Vacaciones());
        nuevaNovedad.setVacadiasaplazados(Short.valueOf(cero));

    }

    public void limpiarduplicarNovedades() {
        String cero = "0";
        duplicarNovedad = new NovedadesSistema();
        duplicarNovedad.setSubtipo("TIEMPO");
        duplicarNovedad.setTipo("VACACION");
        duplicarNovedad.setVacacion(new Vacaciones());
        duplicarNovedad.setVacadiasaplazados(Short.valueOf(cero));
    }

    //CREAR NOVEDADES
    public void agregarNuevaNovedadVacaciones() {
        int pasa = 0;
//        Empleados emp = new Empleados();
        mensajeValidacion = new String();
        RequestContext context = RequestContext.getCurrentInstance();

        if (nuevaNovedad.getFechainicialdisfrute() == null) {
            System.out.println("Entro a Fecha Inicial Disfrute");
            mensajeValidacion = mensajeValidacion + " * Fecha Inicial Disfrute\n";
            pasa++;
        }

        if (nuevaNovedad.getDias() == null) {
            System.out.println("Entro a Dias");
            mensajeValidacion = mensajeValidacion + " * Dias\n";
            pasa++;
        }

        if (pasa != 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevaNovedadEmpleado");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevaNovedadEmpleado').show()");
        }

        if (pasa == 0) {
            if (bandera == 1) {
                cargarTablaDefault();
            }
            //AGREGAR REGISTRO A LA LISTA NOVEDADES .
            paraNuevaNovedad++;
            nuevaNovedadSec = BigInteger.valueOf(paraNuevaNovedad);
            nuevaNovedad.setSecuencia(nuevaNovedadSec);
            nuevaNovedad.setEmpleado(empleadoSeleccionado); //Envia empleado
            System.out.println("Empleado enviado: " + empleadoSeleccionado.getPersona().getNombreCompleto());
            listaNovedadesCrear.add(nuevaNovedad);
            listaNovedades.add(nuevaNovedad);
            System.out.println("periodo : " + nuevaNovedad.getVacacion().getPeriodo());
            novedadSeleccionada = nuevaNovedad;
            limpiarNuevaNovedad();

            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            RequestContext.getCurrentInstance().execute("PF('nuevanovedadVacaciones').hide()");
            RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");
        } else {
        }
    }

    public void duplicarNV() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (novedadSeleccionada != null) {
            duplicarNovedad = new NovedadesSistema();
            paraNuevaNovedad++;
            nuevaNovedadSec = BigInteger.valueOf(paraNuevaNovedad);
            Empleados emple = new Empleados();

            for (int i = 0; i < listaValEmpleados.size(); i++) {
                if (empleadoSeleccionado.getSecuencia().compareTo(listaValEmpleados.get(i).getSecuencia()) == 0) {
                    emple = listaValEmpleados.get(i);
                }
            }
            duplicarNovedad.setSecuencia(nuevaNovedadSec);
            duplicarNovedad.setEmpleado(emple);
            duplicarNovedad.setFechainicialdisfrute(novedadSeleccionada.getFechainicialdisfrute());
            duplicarNovedad.getVacacion().setPeriodo(novedadSeleccionada.getVacacion().getPeriodo());
            duplicarNovedad.setDias(novedadSeleccionada.getDias());
            duplicarNovedad.setFechasiguientefinvaca(novedadSeleccionada.getFechasiguientefinvaca());
            duplicarNovedad.setSubtipo(novedadSeleccionada.getSubtipo());
            duplicarNovedad.setAdelantapagohasta(novedadSeleccionada.getAdelantapagohasta());
            duplicarNovedad.setFechapago(novedadSeleccionada.getFechapago());
            duplicarNovedad.setVacadiasaplazados(novedadSeleccionada.getVacadiasaplazados());

            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarNovedad");
            RequestContext.getCurrentInstance().execute("PF('duplicarregistroNovedad').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void entrarNuevoRegistro() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (empleadoSeleccionado != null) {
            System.out.println("empleado seleccionado al entrar a nueva novedad : " + empleadoSeleccionado.getSecuencia());
            fechaContratacionE = administrarNovedadesVacaciones.obtenerFechaContratacionEmpleado(empleadoSeleccionado.getSecuencia());
            RequestContext.getCurrentInstance().update(":formularioDialogos:nuevaNovedad");
            RequestContext.getCurrentInstance().execute("PF('nuevanovedadVacaciones').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void confirmarDuplicar() {

        int pasa = 0;

        RequestContext context = RequestContext.getCurrentInstance();

        if (duplicarNovedad.getFechainicialdisfrute() == null) {
            System.out.println("Entro a Fecha Inicial");
            mensajeValidacion = mensajeValidacion + " * Fecha Inicial\n";
            pasa++;
        }

        if (duplicarNovedad.getEmpleado() == null) {
            System.out.println("Entro a Empleado");
            mensajeValidacion = mensajeValidacion + " * Empleado\n";
            pasa++;
        }

        if (duplicarNovedad.getDias() == null) {
            System.out.println("Entro a Dias");
            mensajeValidacion = mensajeValidacion + " * Formula\n";
            pasa++;
        }

        System.out.println("Valor Pasa: " + pasa);
        if (pasa != 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevaNovedadEmpleado");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevaNovedadEmpleado').show()");
        }

        if (pasa == 0) {
            listaNovedades.add(duplicarNovedad);
            listaNovedadesCrear.add(duplicarNovedad);

            novedadSeleccionada = duplicarNovedad;
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            if (bandera == 1) {
                cargarTablaDefault();
            }
            RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");
            duplicarNovedad = new NovedadesSistema();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarregistroNovedad");
            RequestContext.getCurrentInstance().execute("PF('duplicarregistroNovedad').hide()");
        }
    }

    //EXPORTAR
    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosNovedadesExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "NovedadVacacionesPDF", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosNovedadesExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "NovedadVacacionesXLS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void activarCtrlF11() {
        System.out.println("TipoLista= " + tipoLista);
        FacesContext c = FacesContext.getCurrentInstance();

        if (bandera == 0) {
            altoTabla = "105";
            nEFechaInicialDisfrute = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nEFechaInicialDisfrute");
            nEFechaInicialDisfrute.setFilterStyle("width: 85% !important");
            nEPeriodo = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nEPeriodo");
            nEPeriodo.setFilterStyle("85% !important");
            nEDias = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nEDias");
            nEDias.setFilterStyle("width: 85% !important");
            nEFechaSiguiente = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nEFechaSiguiente");
            nEFechaSiguiente.setFilterStyle("width: 85% !important");
            nESubTipo = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nESubTipo");
            nESubTipo.setFilterStyle("width: 85% !important");
            nEAdelantoHasta = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nEAdelantoHasta");
            nEAdelantoHasta.setFilterStyle("width: 85% !important");
            nEFechaPago = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nEFechaPago");
            nEFechaPago.setFilterStyle("width: 85% !important");
            nEDiasAplazados = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nEDiasAplazados");
            nEDiasAplazados.setFilterStyle("width: 85% !important");
            RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");
            bandera = 1;
            tipoLista = 1;
        } else if (bandera == 1) {
            cargarTablaDefault();
            RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");
        }
    }

    public void mostrarTodos() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (!listaEmpleadosNovedad.isEmpty()) {
            listaEmpleadosNovedad.clear();
        }
        if (listaValEmpleados != null) {
            for (int i = 0; i < listaValEmpleados.size(); i++) {
                listaEmpleadosNovedad.add(listaValEmpleados.get(i));
            }
        }

        empleadoSeleccionado = listaEmpleadosNovedad.get(0);
        listaNovedades = administrarNovedadesSistema.vacacionesEmpleado(empleadoSeleccionado.getSecuencia());
        getDiasTotales();
        filtradosListaEmpleadosNovedad = null;
        aceptar = true;
        novedadSeleccionada = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        activarMTodos = true;

        RequestContext.getCurrentInstance().update("form:datosEmpleados");
        RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");
        getDiasTotales();
        //getDiasAplazadosTotal();
        RequestContext.getCurrentInstance().update("form:diasTotales");
        RequestContext.getCurrentInstance().update("form:diasAplazados");
        RequestContext.getCurrentInstance().update("form:btnMostrarTodos");
    }

    //Ubicacion Celda Indice Abajo. //Van los que no son NOT NULL.
    public void cambiarIndice(NovedadesSistema novedadS, int celda) {
        if (permitirIndex == true) {
            novedadSeleccionada = novedadS;
            cualCelda = celda;
            novedadSeleccionada.getSecuencia();
            if (cualCelda == 0) {
                novedadSeleccionada.getFechainicialdisfrute();
            } else if (cualCelda == 1) {
                novedadSeleccionada.getVacacion().getPeriodo();
            } else if (cualCelda == 2) {
                novedadSeleccionada.getDias();
            } else if (cualCelda == 3) {
                novedadSeleccionada.getFechasiguientefinvaca();
            } else if (cualCelda == 4) {
                novedadSeleccionada.getSubtipo();
            } else if (cualCelda == 5) {
                novedadSeleccionada.getAdelantapagohasta();
            } else if (cualCelda == 6) {
                novedadSeleccionada.getFechapago();
            } else if (cualCelda == 7) {
                novedadSeleccionada.getVacadiasaplazados();
            }
        }
    }

    //Ubicacion Celda Arriba 
    public void cambiarEmpleado() {
        //Si ninguna de las 3 listas (crear,modificar,borrar) tiene algo, hace esto
        //{
        if (listaNovedadesCrear.isEmpty() && listaNovedadesBorrar.isEmpty() && listaNovedadesModificar.isEmpty()) {
            secuenciaEmpleado = empleadoSeleccionado.getSecuencia();
            //Se recargan las novedades para el empleado
            listaNovedades = administrarNovedadesSistema.vacacionesEmpleado(secuenciaEmpleado);
            if (listaNovedades == null) {
                listaNovedades = new ArrayList<NovedadesSistema>();
            }
            contarRegistrosNovedades();
            getDiasTotales();
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");
            RequestContext.getCurrentInstance().update("form:diasTotales");
            RequestContext.getCurrentInstance().update("form:diasAplazados");

        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:cambiar");
            RequestContext.getCurrentInstance().execute("PF('cambiar').show()");
        }
    }

    public void abrirLista(int listaV) {

        RequestContext context = RequestContext.getCurrentInstance();
        if (listaV == 0) {
            empleadoSeleccionadoLOV = null;
           contarRegistroEmpleadosLOV();
            RequestContext.getCurrentInstance().update("formularioDialogos:empleadosDialogo");
            RequestContext.getCurrentInstance().execute("PF('empleadosDialogo').show()");
        }
    }

    public void actualizarEmpleadosNovedad() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (!listaEmpleadosNovedad.isEmpty()) {
            listaEmpleadosNovedad.clear();
            listaEmpleadosNovedad.add(empleadoSeleccionadoLOV);
            empleadoSeleccionado = listaEmpleadosNovedad.get(0);
        }
        secuenciaEmpleado = empleadoSeleccionadoLOV.getSecuencia();
        listaNovedades = administrarNovedadesSistema.vacacionesEmpleado(empleadoSeleccionado.getSecuencia());
        // Se recargan las novedades:
        getDiasTotales();
        aceptar = true;
        filtradosListaEmpleadosNovedad = null;
        //empleadoSeleccionadoLOV = null;
        novedadSeleccionada = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        activarMTodos = false;
        contarRegistroEmpleados();
        context.reset("formularioDialogos:LOVEmpleados:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVEmpleados').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('empleadosDialogo').hide()");
        RequestContext.getCurrentInstance().update("formularioDialogos:LOVEmpleados");
        RequestContext.getCurrentInstance().update("form:datosEmpleados");
        RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");

        RequestContext.getCurrentInstance().update("form:diasTotales");
        RequestContext.getCurrentInstance().update("form:diasAplazados");
        RequestContext.getCurrentInstance().update("form:btnMostrarTodos");
    }

    public void actualizarPeriodos() {

        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            novedadSeleccionada.getVacacion().setPeriodo(periodoSeleccionado.getPeriodo());
            if (!listaNovedadesCrear.contains(novedadSeleccionada)) {
                if (listaNovedadesModificar.isEmpty()) {
                    listaNovedadesModificar.add(novedadSeleccionada);
                } else if (!listaNovedadesModificar.contains(novedadSeleccionada)) {
                    listaNovedadesModificar.add(novedadSeleccionada);
                }
            }

            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            permitirIndex = true;
            RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");
        } else if (tipoActualizacion == 1) {
            nuevaNovedad.setVacacion(periodoSeleccionado);
//            nuevaNovedad.getVacacion().setDiaspendientes(periodoSeleccionado.getDiaspendientes());
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaNovedad");
        } else if (tipoActualizacion == 2) {
            duplicarNovedad.setVacacion(periodoSeleccionado);
//            duplicarNovedad.getVacacion().setDiaspendientes(periodoSeleccionado.getDiaspendientes());
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarNovedad");
        }
        filtradoslistaPeriodos = null;
        periodoSeleccionado = null;
        aceptar = true;
        novedadSeleccionada = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        contarRegistroPeriodos();
        context.reset("formularioDialogos:LOVPeriodos:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVPeriodos').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('periodosDialogo').hide()");
        RequestContext.getCurrentInstance().update("formularioDialogos:LOVPeriodos");
        RequestContext.getCurrentInstance().update("form:datosEmpleados");
        RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");
        
    }

    //BORRAR NOVEDADES
    public void borrarNovedades() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (novedadSeleccionada != null) {
            if (!listaNovedadesModificar.isEmpty() && listaNovedadesModificar.contains(novedadSeleccionada)) {
                int modIndex = listaNovedadesModificar.indexOf(novedadSeleccionada);
                listaNovedadesModificar.remove(modIndex);
                listaNovedadesBorrar.add(novedadSeleccionada);
            } else if (!listaNovedadesCrear.isEmpty() && listaNovedadesCrear.contains(novedadSeleccionada)) {
                int crearIndex = listaNovedadesCrear.indexOf(novedadSeleccionada);
                listaNovedadesCrear.remove(crearIndex);
            } else {
                listaNovedadesBorrar.add(novedadSeleccionada);
            }
            listaNovedades.remove(novedadSeleccionada);

            if (tipoLista == 1) {
                filtradosListaNovedades.remove(novedadSeleccionada);
            }
            RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");
            novedadSeleccionada = null;

            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    //GUARDAR
    public void guardarCambiosNovedades() {
        Empleados emp = new Empleados();
        if (guardado == false) {
            System.out.println("Realizando Operaciones Novedades");

            if (!listaNovedadesBorrar.isEmpty()) {
                for (int i = 0; i < listaNovedadesBorrar.size(); i++) {
                    System.out.println("Borrando..." + listaNovedadesBorrar.size());

                    if (listaNovedadesBorrar.get(i).getVacacion().getSecuencia() == null) {
                        listaNovedadesBorrar.get(i).setVacacion(null);
                    }
                    if (listaNovedadesBorrar.get(i).getFechasiguientefinvaca() == null) {
                        listaNovedadesBorrar.get(i).setFechasiguientefinvaca(null);
                    }
                    if (listaNovedadesBorrar.get(i).getFechapago() == null) {
                        listaNovedadesBorrar.get(i).setFechapago(null);
                    }
                    if (listaNovedadesBorrar.get(i).getAdelantapagohasta() == null) {
                        listaNovedadesBorrar.get(i).setAdelantapagohasta(null);
                    }
                    if (listaNovedadesBorrar.get(i).getVacadiasaplazados() == null) {
                        listaNovedadesBorrar.get(i).setVacadiasaplazados(null);
                    }
                    administrarNovedadesSistema.borrarNovedades(listaNovedadesBorrar.get(i));
                }
                System.out.println("Entra");
                listaNovedadesBorrar.clear();
            }

            if (!listaNovedadesCrear.isEmpty()) {
                for (int i = 0; i < listaNovedadesCrear.size(); i++) {
                    System.out.println("Creando...");

                    if (listaNovedadesCrear.get(i).getVacacion().getSecuencia() == null) {
                        listaNovedadesCrear.get(i).setVacacion(null);
                    } else if (listaNovedadesCrear.get(i).getFechasiguientefinvaca() == null) {
                        listaNovedadesCrear.get(i).setFechasiguientefinvaca(null);
                    } else if (listaNovedadesCrear.get(i).getFechapago() == null) {
                        listaNovedadesCrear.get(i).setFechapago(null);
                    } else if (listaNovedadesCrear.get(i).getAdelantapagohasta() == null) {
                        listaNovedadesCrear.get(i).setAdelantapagohasta(null);
                    } else if (listaNovedadesCrear.get(i).getVacadiasaplazados() == null) {
                        listaNovedadesCrear.get(i).setVacadiasaplazados(null);
                    }
                    System.out.println(listaNovedadesCrear.get(i).getTipo());
                    administrarNovedadesSistema.crearNovedades(listaNovedadesCrear.get(i));
                }
                System.out.println("LimpiaLista");
                listaNovedadesCrear.clear();
            }
            System.out.println("Se guardaron los datos con exito");
            listaNovedades = null;
            contarRegistrosNovedades();
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");
            guardado = true;
            permitirIndex = true;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            FacesMessage msg = new FacesMessage("Información", "Se gurdarón los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
            //  k = 0;
        }
        novedadSeleccionada = null;
    }

    public void cancelarCambioPeriodos() {
        filtradoslistaPeriodos = null;
        periodoSeleccionado = null;
        aceptar = true;
        novedadSeleccionada = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:LOVPeriodos:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVPeriodos').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('periodosDialogo').hide()");
    }

    //Sumarle dias a una fecha determinada
    //@param fch La fecha para sumarle los dias
    //@param dias Numero de dias a agregar
    //@return La fecha agregando los dias
    public Date sumarFechasDias(Date creacionEmpleado, int dias) {
        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(creacionEmpleado.getTime());
        cal.add(Calendar.DATE, dias);
        suma364 = cal.getTime();
        return suma364;
    }

    public Date sumarFechasDias2(Date inicioPeriodo, int dias) {
        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(inicioPeriodo.getTime());
        cal.add(Calendar.DATE, dias);
        finalsuma364 = cal.getTime();
        return finalsuma364;
    }

    public void lovPeriodo(BigInteger secuenciaEmpleado, int tipoAct) {
        RequestContext context = RequestContext.getCurrentInstance();
        tipoActualizacion = tipoAct;
        if (nuevaNovedad.getDias() == null || nuevaNovedad.getDias() == BigInteger.valueOf(0)) {
            System.out.println("Entró");
            System.out.println("empleado seleccionado = " + secuenciaEmpleado);
            listaPeriodos = administrarNovedadesSistema.periodosEmpleado(secuenciaEmpleado);
            if (listaPeriodos.isEmpty()) {
                RequestContext.getCurrentInstance().update("formularioDialogos:dias");
                RequestContext.getCurrentInstance().execute("PF('dias').show()");
            } else {
                contarRegistroPeriodos();
                RequestContext.getCurrentInstance().update("formularioDialogos:periodosDialogo");
                RequestContext.getCurrentInstance().execute("PF('periodosDialogo').show()");
            }
        }
    }

    public void diasMayor(BigInteger dias) {
        RequestContext context = RequestContext.getCurrentInstance();
        System.out.println("Dias Mayor");
        BigInteger backup = dias;
        if (nuevaNovedad.getDias().compareTo(BigInteger.valueOf(15)) == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:diasMayor");
            nuevaNovedad.setDias(null);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaNovedad");
            RequestContext.getCurrentInstance().execute("PF('diasMayor').show()");
        }
    }

    public void validaciones(Integer tipoAct) {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoAct == 1) {
            if (nuevaNovedad.getFechainicialdisfrute().before(fechaContratacionE)) {
                RequestContext.getCurrentInstance().update("formularioDialogos:validacion1");
                nuevaNovedad.setFechainicialdisfrute(null);
                RequestContext.getCurrentInstance().update("formularioDialogos:nuevaNovedad");
                RequestContext.getCurrentInstance().execute("PF('validacion1').show()");
            }
        } else if (tipoAct == 2) {
            if (duplicarNovedad.getFechainicialdisfrute().before(fechaContratacionE)) {
                RequestContext.getCurrentInstance().update("formularioDialogos:validacion1");
                duplicarNovedad.setFechainicialdisfrute(null);
                RequestContext.getCurrentInstance().update("formularioDialogos:duplicarNovedad");
                RequestContext.getCurrentInstance().execute("PF('validacion1').show()");
            }
        }
        // Esperar para la consulta a BD de si es o no festivo o día habil etc...
    }

    public void agregarPeriodo(BigInteger secuenciaEmpleado) {
        RequestContext context = RequestContext.getCurrentInstance();
        Vacaciones vaca = new Vacaciones();
        sumarFechasDias(empleadoSeleccionado.getFechacreacion(), 364);
        sumarFechasDias2(suma364, 364);
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        String per = formato.format(suma364) + " -> " + formato.format(finalsuma364);
        vaca.setSecuencia(BigDecimal.valueOf(0));
        vaca.setDiaspendientes(BigDecimal.valueOf(15));
        vaca.setInicialcausacion(suma364);
        vaca.setFinalcausacion(finalsuma364);
        vaca.setPeriodo(per);
        listaPeriodos.add(vaca);
        RequestContext.getCurrentInstance().update("formularioDialogos:periodosDialogo");
        RequestContext.getCurrentInstance().execute("PF('periodosDialogo').show()");
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void cancelarCambioEmpleados() {
        filtradoslistaValEmpleados = null;
        empleadoSeleccionadoLOV = null;
        aceptar = true;
        novedadSeleccionada = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:LOVEmpleados:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVEmpleados').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('empleadosDialogo').hide()");
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (novedadSeleccionada != null) {
            int result = administrarRastros.obtenerTabla(novedadSeleccionada.getSecuencia(), "NOVEDADESSISTEMA");
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
    public void cancelarModificacion() {
        if (bandera == 1) {
            cargarTablaDefault();
        }
        if (!listaEmpleadosNovedad.isEmpty()) {
            listaEmpleadosNovedad.clear();
        }
        if (listaValEmpleados != null) {
            for (int i = 0; i < listaValEmpleados.size(); i++) {
                listaEmpleadosNovedad.add(listaValEmpleados.get(i));
            }
        }
        contarRegistrosNovedades();
        aceptar = true;
        guardado = true;
        tipoLista = 0;
        permitirIndex = true;
        limpiarNuevaNovedad();
        limpiarduplicarNovedades();
        diasTotales = BigInteger.valueOf(0);
        diasAplazadosTotal = Short.parseShort(cero);
        altoTabla = "125";
        listaNovedadesBorrar.clear();
        listaNovedadesCrear.clear();
        listaNovedadesModificar.clear();
        listaNovedades = null;
        listaValEmpleados = null;
        activarMTodos = true;
        empleadoSeleccionado = null;
        novedadSeleccionada = null;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");
        RequestContext.getCurrentInstance().update("form:datosEmpleados");
    }

    public void cargarTablaDefault() {
        FacesContext c = FacesContext.getCurrentInstance();
        altoTabla = "125";
        nEFechaInicialDisfrute = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nEFechaInicialDisfrute");
        nEFechaInicialDisfrute.setFilterStyle("display: none; visibility: hidden;");
        nEPeriodo = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nEPeriodo");
        nEPeriodo.setFilterStyle("display: none; visibility: hidden;");
        nEDias = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nEDias");
        nEDias.setFilterStyle("display: none; visibility: hidden;");
        nEFechaSiguiente = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nEFechaSiguiente");
        nEFechaSiguiente.setFilterStyle("display: none; visibility: hidden;");
        nESubTipo = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nESubTipo");
        nESubTipo.setFilterStyle("display: none; visibility: hidden;");
        nEAdelantoHasta = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nEAdelantoHasta");
        nEAdelantoHasta.setFilterStyle("display: none; visibility: hidden;");
        nEFechaPago = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nEFechaPago");
        nEFechaPago.setFilterStyle("display: none; visibility: hidden;");
        nEDiasAplazados = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nEDiasAplazados");
        nEDiasAplazados.setFilterStyle("display: none; visibility: hidden;");
        bandera = 0;
        filtradosListaNovedades = null;
        tipoLista = 0;
    }

    public void salir() {

        if (bandera == 1) {
            cargarTablaDefault();
        }
        listaEmpleadosNovedad = null;
        aceptar = true;
        guardado = true;
        tipoLista = 0;
        listaValEmpleados = null;
        permitirIndex = true;
        limpiarNuevaNovedad();
        limpiarduplicarNovedades();
        diasTotales = BigInteger.valueOf(0);
        diasAplazadosTotal = Short.parseShort(cero);
        altoTabla = "125";
        listaNovedadesBorrar.clear();
        listaNovedadesCrear.clear();
        listaNovedadesModificar.clear();
        novedadSeleccionada = null;
        listaNovedades = null;
        activarMTodos = true;
        RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");
    }

    //EVENTO FILTRAR
    public void eventoFiltrar() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
        contarRegistrosNovedades();
    }

    public void contarRegistroEmpleados() {
        RequestContext.getCurrentInstance().update("form:infoRegistroEmpleados");
    }

    public void contarRegistrosNovedades() {
        RequestContext.getCurrentInstance().update("form:infoRegistroNovedades");
    }

    public void contarRegistroEmpleadosLOV() {
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroEmpleadosLOV");
    }
    
    public void contarRegistroPeriodos() {
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroPeriodosLOV");

    }

    public void posicionOtro() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, String> map = context.getExternalContext().getRequestParameterMap();
        String celda = map.get("celda"); // name attribute of node 
        String registro = map.get("registro"); // type attribute of node 
        int indice = Integer.parseInt(registro);
        int columna = Integer.parseInt(celda);
        novedadSeleccionada = listaNovedades.get(indice);
        cambiarIndice(novedadSeleccionada, columna);
    }

//GETTER & SETTER
    public List<Empleados> getListaEmpleadosNovedad() {
        if (listaEmpleadosNovedad == null) {
            listaEmpleadosNovedad = administrarNovedadesVacaciones.empleadosVacaciones();
        }
        return listaEmpleadosNovedad;
    }

    public void setListaEmpleadosNovedad(List<Empleados> listaEmpleadosNovedad) {
        this.listaEmpleadosNovedad = listaEmpleadosNovedad;
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

    public void setEmpleadoSeleccionado(Empleados empleadoSeleccionado) {
        this.empleadoSeleccionado = empleadoSeleccionado;
    }

    //LOV EMPLEADOS
    public List<Empleados> getListaValEmpleados() {
        if (listaValEmpleados == null) {
            listaValEmpleados = administrarNovedadesVacaciones.empleadosVacaciones();
        }
//        modificarInfoRegistroEmpleadosLOV(listaValEmpleados.size());
        return listaValEmpleados;
    }

    public void setListaValEmpleados(List<Empleados> listaEmpleados) {
        this.listaValEmpleados = listaEmpleados;
    }

    public List<Empleados> getFiltradoslistaValEmpleados() {
        return filtradoslistaValEmpleados;
    }

    public void setFiltradoslistaValEmpleados(List<Empleados> filtradoslistaValEmpleados) {
        this.filtradoslistaValEmpleados = filtradoslistaValEmpleados;
    }

    public Empleados getSeleccionEmpleados() {
        return empleadoSeleccionadoLOV;
    }

    public void setSeleccionEmpleados(Empleados seleccionEmpleados) {
        this.empleadoSeleccionadoLOV = seleccionEmpleados;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public void setAceptar(boolean aceptar) {
        this.aceptar = aceptar;
    }

    public List<NovedadesSistema> getListaNovedades() {
        if (listaNovedades == null) {
            if (empleadoSeleccionado != null) {
                listaNovedades = administrarNovedadesSistema.vacacionesEmpleado(empleadoSeleccionado.getSecuencia());
            }
        }
        return listaNovedades;
    }

    public void setListaNovedades(List<NovedadesSistema> listaNovedades) {
        this.listaNovedades = listaNovedades;
    }

    public List<NovedadesSistema> getListaNovedadesCrear() {
        return listaNovedadesCrear;
    }

    public void setListaNovedadesCrear(List<NovedadesSistema> listaNovedadesCrear) {
        this.listaNovedadesCrear = listaNovedadesCrear;
    }

    public List<NovedadesSistema> getListaNovedadesModificar() {
        return listaNovedadesModificar;
    }

    public void setListaNovedadesModificar(List<NovedadesSistema> listaNovedadesModificar) {
        this.listaNovedadesModificar = listaNovedadesModificar;
    }

    public List<NovedadesSistema> getListaNovedadesBorrar() {
        return listaNovedadesBorrar;
    }

    public void setListaNovedadesBorrar(List<NovedadesSistema> listaNovedadesBorrar) {
        this.listaNovedadesBorrar = listaNovedadesBorrar;
    }

    public NovedadesSistema getEditarNovedades() {
        return editarNovedades;
    }

    public void setEditarNovedades(NovedadesSistema editarNovedades) {
        this.editarNovedades = editarNovedades;
    }

    public List<NovedadesSistema> getFiltradosListaNovedades() {
        return filtradosListaNovedades;
    }

    public void setFiltradosListaNovedades(List<NovedadesSistema> filtradosListaNovedades) {
        this.filtradosListaNovedades = filtradosListaNovedades;
    }

    public NovedadesSistema getNuevaNovedad() {
        return nuevaNovedad;
    }

    public void setNuevaNovedad(NovedadesSistema nuevaNovedad) {
        this.nuevaNovedad = nuevaNovedad;
    }

    public List<Vacaciones> getListaPeriodos() {
        if (listaPeriodos == null) {
            listaPeriodos = administrarNovedadesSistema.periodosEmpleado(secuenciaEmpleado);
        }
        return listaPeriodos;
    }

    public void setListaPeriodos(List<Vacaciones> listaPeriodos) {
        this.listaPeriodos = listaPeriodos;
    }

    public List<Vacaciones> getFiltradoslistaPeriodos() {
        return filtradoslistaPeriodos;
    }

    public void setFiltradoslistaPeriodos(List<Vacaciones> filtradoslistaPeriodos) {
        this.filtradoslistaPeriodos = filtradoslistaPeriodos;
    }

    public Vacaciones getPeriodoSeleccionado() {
        return periodoSeleccionado;
    }

    public void setPeriodoSeleccionado(Vacaciones seleccionPeriodo) {
        this.periodoSeleccionado = seleccionPeriodo;
    }

    public NovedadesSistema getDuplicarNovedad() {
        return duplicarNovedad;
    }

    public void setDuplicarNovedad(NovedadesSistema duplicarNovedad) {
        this.duplicarNovedad = duplicarNovedad;
    }

    public BigInteger getDiasTotales() {
        diasTotales = BigInteger.valueOf(0);
        diasAplazadosTotal = 0;
        if (listaNovedades != null) {
            if (!listaNovedades.isEmpty()) {
                for (int i = 0; i < listaNovedades.size(); i++) {
                    diasTotales = diasTotales.add(listaNovedades.get(i).getDias());
                    diasAplazadosTotal = (short) (diasAplazadosTotal + listaNovedades.get(i).getVacadiasaplazados());
                }
            }
        } else if (listaNovedades == null) {
            contarRegistrosNovedades();
        }
        return diasTotales;
    }

    public void setDiasTotales(BigInteger diasTotales) {
        this.diasTotales = diasTotales;
    }

    public short getDiasAplazadosTotal() {
        return diasAplazadosTotal;
    }

    public void setDiasAplazadosTotal(short diasAplazadosTotal) {
        this.diasAplazadosTotal = diasAplazadosTotal;
    }

    public String getMensajeValidacion() {
        return mensajeValidacion;
    }

    public void setMensajeValidacion(String mensajeValidacion) {
        this.mensajeValidacion = mensajeValidacion;
    }

    public String getAltoTabla() {
        return altoTabla;
    }

    public NovedadesSistema getNovedadSeleccionada() {
        return novedadSeleccionada;
    }

    public void setNovedadSeleccionada(NovedadesSistema novedadSeleccionada) {
        this.novedadSeleccionada = novedadSeleccionada;
    }

    public boolean isGuardado() {
        return guardado;
    }

    public void setGuardado(boolean guardado) {
        this.guardado = guardado;
    }

    public String getPaginaAnterior() {
        return paginaAnterior;
    }

    public void setPaginaAnterior(String paginaAnterior) {
        this.paginaAnterior = paginaAnterior;
    }

    public boolean isActivarMTodos() {
        return activarMTodos;
    }

    public void setActivarMTodos(boolean activarMTodos) {
        this.activarMTodos = activarMTodos;
    }

    public String getInfoRegistroEmpleados() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosEmpleados");
        infoRegistroEmpleados = String.valueOf(tabla.getRowCount());
        return infoRegistroEmpleados;
    }

    public void setInfoRegistroEmpleados(String infoRegistroEmpleados) {
        this.infoRegistroEmpleados = infoRegistroEmpleados;
    }

    public String getInfoRegistroNovedades() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosNovedadesEmpleado");
        infoRegistroNovedades = String.valueOf(tabla.getRowCount());
        return infoRegistroNovedades;
    }

    public void setInfoRegistroNovedades(String infoRegistroNovedades) {
        this.infoRegistroNovedades = infoRegistroNovedades;
    }

    public String getInfoRegistroEmpleadosLOV() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:LOVEmpleados");
        infoRegistroEmpleadosLOV = String.valueOf(tabla.getRowCount());
        return infoRegistroEmpleadosLOV;
    }

    public void setInfoRegistroEmpleadosLOV(String infoRegistroEmpleadosLOV) {
        this.infoRegistroEmpleadosLOV = infoRegistroEmpleadosLOV;
    }

    public String getInfoRegistroPeriodo() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:LOVPeriodos");
        infoRegistroPeriodo = String.valueOf(tabla.getRowCount());
        return infoRegistroPeriodo;
    }

    public void setInfoRegistroPeriodo(String infoRegistroPeriodo) {
        this.infoRegistroPeriodo = infoRegistroPeriodo;
    }

}