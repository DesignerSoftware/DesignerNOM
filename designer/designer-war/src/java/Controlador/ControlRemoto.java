package Controlador;

import Administrar.AdministrarCarpetaPersonal;
import Banner.BannerInicioRed;
import Entidades.*;
import InterfaceAdministrar.*;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
//import java.util.logging.Level;
//import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;
import org.primefaces.component.column.Column;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.tabview.Tab;
import org.primefaces.context.RequestContext;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.UploadedFile;

@ManagedBean
@SessionScoped
public class ControlRemoto implements Serializable {

    @EJB
    AdministrarCarpetaPersonalInterface administrarCarpetaPersonal;
    @EJB
    AdministrarCarpetaDesignerInterface administrarCarpetaDesigner;
    private Empleados empleado;
    private Personas persona;
    private DetallesEmpresas detallesEmpresas;
    private VWActualesCargos vwActualesCargos;
    private Usuarios usuarios;
    private ParametrosEstructuras parametrosEstructuras;
    private VWActualesTiposContratos vwActualesTiposContratos;
    private VWActualesNormasEmpleados vwActualesNormasEmpleados;
    private VWActualesAfiliacionesSalud vwActualesAfiliacionesSalud;
    private VWActualesAfiliacionesPension vwActualesAfiliacionesPension;
    private VWActualesLocalizaciones vwActualesLocalizaciones;
    private VWActualesTiposTrabajadores vwActualesTiposTrabajadores;
    private VWActualesTiposTrabajadores trabajador;
    private VWActualesContratos vwActualesContratos;
    private VWActualesJornadas vwActualesJornadas;
    private VWActualesReformasLaborales vwActualesReformasLaborales;
    private VWActualesUbicaciones vwActualesUbicaciones;
    private VWActualesFormasPagos vwActualesFormasPagos;
    private VWActualesVigenciasViajeros vwActualesVigenciasViajeros;
    private String estadoVacaciones, actualIBC, actualSet, actualComprobante;
    private BigDecimal actualMVR;
    private VWActualesTiposTrabajadores vwActualesTiposTrabajadoresPosicion;
    private VWActualesTiposTrabajadores backup;
    private List<VwTiposEmpleados> busquedaRapida;
    private List<VwTiposEmpleados> filterBusquedaRapida;
    private List<VwTiposEmpleados> filterBuscarEmpleado;
    private List<VwTiposEmpleados> buscarEmplTipo;
    private List<Empleados> empl;
    private List<VigenciasCargos> vigenciasCargosEmpleados;
    private VigenciasCargos vigenciaSeleccionada;
    private String fechaActualesTiposContratos;
    private String FechaDesde, FechaHasta, FechaSistema;
    private String Sueldo;
    private String tipo, tipoBk;
    private final SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
    private final Locale locale = new Locale("es", "CO");
    private final NumberFormat nf = NumberFormat.getCurrencyInstance(locale);
    private String Mensaje;
    private String Imagen;
    private String fotoEmpleado;
    private BigInteger secuencia, identificacion;
    private boolean acumulado, novedad, evaluacion, activo, pensionado, aspirante, hv1, hv2, bandera;
    private VwTiposEmpleados emplSeleccionado;
    private VwTiposEmpleados emplSeleccionadoBE;
    private SelectItem[] tipoEmpleado;
    private boolean buscar, buscarEmp, mostrarT;
    private UploadedFile file;
    private String nombreTabla;
    private Integer estadoEmpleado;
    //datos tablas ctrl+f11
    private Column tablasNombre, tablasDescripcion, moduloCodigo, moduloNombre, moduloObs;
    private boolean filtrosActivos;
    private List<Modulos> listModulos;
    private List<Modulos> filtradolistModulos;
    private List<Tablas> listTablas;
    private List<Tablas> filterListTablas;
    private Modulos selectModulo;
    private Tablas selectTabla;
    private Pantallas pantalla;
    private String tablaExportar, nombreArchivo;
    private BigInteger secuenciaMod;
    //PESTAÑA ACTUAL
    private int numPestanha;
    //SELECT ONE RADIO
    private String mensajePagos, tituloPago;
    private String pago;
    //ALTO TABLAS (PESTAÑA DESIGNER)
    private String altoModulos, altoTablas;
    //Buscar Tablas LOV
    private List<Tablas> listTablasLOV;
    private List<Tablas> filtradoListTablasLOV;
    private Tablas seleccionTablaLOV;
    private boolean buscarTablasLOV, mostrarTodasTablas;
    //Visualizar seleccion de tipos trabajadores (StyleClass)
    private String styleActivos, stylePensionados, styleRetirados, styleAspirantes;
    private String actualCargo;
    private String tipoPersonal;
    private String accion;
    private String redirigir;
    private String infoRegistroBuscarEmpleados;
    private String infoRegistroBuscarTablas;
    private String infoRegistroBusquedaRapida;
    private int posicion;
    private int totalRegistros;
    private String informacionTiposTrabajadores;
    private final String extension;
    private List<BannerInicioRed> banner;
    //Seleccion de empresa para ingreso de personal
    private List<Empresas> filtradoLOVEmpresas;
    private List<Empresas> lovEmpresas;
    private Empresas empresaSeleccionada;
    private String infoRegistroEmpresas;
    private boolean activarAceptarEmpresas;
    private Empresas unicaEmpresa;

    public ControlRemoto() {
        extension = ".png";
        accion = null;
        tipoPersonal = "activos";
        tipo = "ACTIVO";
        vwActualesCargos = new VWActualesCargos();
        vwActualesTiposContratos = new VWActualesTiposContratos();
        vwActualesNormasEmpleados = new VWActualesNormasEmpleados();
        vwActualesAfiliacionesSalud = new VWActualesAfiliacionesSalud();
        vwActualesAfiliacionesPension = new VWActualesAfiliacionesPension();
        vwActualesLocalizaciones = new VWActualesLocalizaciones();
        vwActualesTiposTrabajadores = new VWActualesTiposTrabajadores();
        vwActualesContratos = new VWActualesContratos();
        vwActualesJornadas = new VWActualesJornadas();
        vwActualesReformasLaborales = new VWActualesReformasLaborales();
        vwActualesUbicaciones = new VWActualesUbicaciones();
        vwActualesFormasPagos = new VWActualesFormasPagos();
        vwActualesVigenciasViajeros = new VWActualesVigenciasViajeros();
        vwActualesTiposTrabajadoresPosicion = new VWActualesTiposTrabajadores();
        administrarCarpetaPersonal = new AdministrarCarpetaPersonal();
        busquedaRapida = null;
        Imagen = "personal1" + extension;
        styleActivos = "ui-state-highlight";
        acumulado = false;
        novedad = false;
        evaluacion = false;
        activo = false;
        pensionado = true;
        aspirante = false;
        hv1 = true;
        hv2 = false;
        buscar = true;
        buscarEmp = false;
        mostrarT = true;
        bandera = false;
        //Carpeta Designer //
        listModulos = null;
        selectModulo = null;
        tablaExportar = "data1";
        nombreArchivo = "Modulos";
        //Inicializar pestanha en 0
        numPestanha = 0;
        pago = "AUTOMATICO";
        tituloPago = "PAGOS AUTOMATICOS";
        mensajePagos = "Realice liquidaciones automáticas quincenales, mensuales, entre otras, por estructuras o por tipo de empleado. Primero ingrese los parametros a liquidar, después genere la liquidación para luego poder observar los comprobantes de pago. Usted puede deshacer todas las liquidaciones que desee siempre y cuando no se hayan cerrado. Al cerrar una liquidación se generan acumulados, por eso es importante estar seguro que la liquidación es correcta antes de cerrarla.";
        altoModulos = "93";
        altoTablas = "202";
        buscarTablasLOV = true;
        mostrarTodasTablas = true;
        filtrosActivos = false;
        posicion = 0;
        totalRegistros = -1;
        banner = new ArrayList<BannerInicioRed>();
        filtradoLOVEmpresas = new ArrayList<Empresas>();
        infoRegistroEmpresas = "0";
        activarAceptarEmpresas = true;
        lovEmpresas = null;
        unicaEmpresa = null;
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarCarpetaPersonal.obtenerConexion(ses.getId());
            administrarCarpetaDesigner.obtenerConexion(ses.getId());
            primerTipoTrabajador();
            totalRegistros = administrarCarpetaPersonal.obtenerTotalRegistrosTipoTrabajador(tipo);
            actualizarInformacionTipoTrabajador();
            llenarBannerDefault();
            lovEmpresas = administrarCarpetaPersonal.consultarEmpresas();
            RequestContext.getCurrentInstance().update("form:LovEmpresasDialogo");
            RequestContext.getCurrentInstance().update("form:LovEmpresasTabla");
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void datosIniciales(int pestaña) {
        numPestanha = pestaña;
    }

    public void valorInputText() throws ParseException {
        trabajador = vwActualesTiposTrabajadoresPosicion;
        if (trabajador.getEmpleado() != null) {
            secuencia = trabajador.getEmpleado().getSecuencia();
            identificacion = trabajador.getEmpleado().getPersona().getNumerodocumento();
        }

        try {
            vwActualesCargos = administrarCarpetaPersonal.consultarActualCargoEmpleado(secuencia);
            Date actualFechaHasta = administrarCarpetaPersonal.consultarActualesFechas();
            String actualARP = administrarCarpetaPersonal.consultarActualARP(vwActualesCargos.getEstructura().getSecuencia(), vwActualesCargos.getCargo().getSecuencia(), actualFechaHasta);
            actualCargo = "%ARP: " + actualARP + " > " + vwActualesCargos.getCargo().getNombre() + " - " + vwActualesCargos.getEstructura().getOrganigrama().getEmpresa().getNombre();
            //actualCargo = "%ARP: " + " > " + vwActualesCargos.getCargo().getNombre() + " - " + vwActualesCargos.getEstructura().getNombre();
        } catch (Exception e) {
            actualCargo = null;
        }

        try {
            vwActualesTiposContratos = administrarCarpetaPersonal.consultarActualTipoContratoEmpleado(secuencia);
            fechaActualesTiposContratos = formato.format(vwActualesTiposContratos.getFechaVigencia());
            //} catch (Exception e) {
        } catch (ParseException pe) {
            vwActualesTiposContratos = null;
            fechaActualesTiposContratos = null;
        }

        try {
            vwActualesNormasEmpleados = administrarCarpetaPersonal.consultarActualNormaLaboralEmpleado(secuencia);
        } catch (Exception e) {
            vwActualesNormasEmpleados = null;
        }

        try {
            vwActualesAfiliacionesSalud = administrarCarpetaPersonal.consultarActualAfiliacionSaludEmpleado(secuencia);
        } catch (Exception e) {
            vwActualesAfiliacionesSalud = null;
        }

        try {
            vwActualesAfiliacionesPension = administrarCarpetaPersonal.consultarActualAfiliacionPensionEmpleado(secuencia);
        } catch (Exception e) {
            vwActualesAfiliacionesPension = null;
        }

        try {
            vwActualesLocalizaciones = administrarCarpetaPersonal.consultarActualLocalizacionEmpleado(secuencia);
        } catch (Exception e) {
            vwActualesLocalizaciones = null;
        }

        try {
            vwActualesTiposTrabajadores = administrarCarpetaPersonal.consultarActualTipoTrabajadorEmpleado(secuencia);
        } catch (Exception e) {

            vwActualesTiposTrabajadores = null;
        }

        try {
            vwActualesContratos = administrarCarpetaPersonal.consultarActualContratoEmpleado(secuencia);
        } catch (Exception e) {
            vwActualesContratos = null;
        }

        try {
            vwActualesJornadas = administrarCarpetaPersonal.consultarActualJornadaEmpleado(secuencia);
        } catch (Exception e) {
            vwActualesJornadas = null;
        }

        try {
            Sueldo = "TOTAL: " + nf.format(administrarCarpetaPersonal.consultarActualSueldoEmpleado(secuencia));
        } catch (Exception e) {
            Sueldo = null;
        }

        try {
            vwActualesReformasLaborales = administrarCarpetaPersonal.consultarActualReformaLaboralEmpleado(secuencia);
        } catch (Exception e) {
            vwActualesReformasLaborales = null;
        }

        try {
            vwActualesUbicaciones = administrarCarpetaPersonal.consultarActualUbicacionEmpleado(secuencia);
        } catch (Exception e) {
            vwActualesUbicaciones = null;
        }

        try {
            vwActualesFormasPagos = administrarCarpetaPersonal.consultarActualFormaPagoEmpleado(secuencia);
        } catch (Exception e) {
            vwActualesFormasPagos = null;
        }

        try {
            vwActualesVigenciasViajeros = administrarCarpetaPersonal.consultarActualTipoViajeroEmpleado(secuencia);
        } catch (Exception e) {
            vwActualesVigenciasViajeros = null;
        }

        try {
            estadoVacaciones = administrarCarpetaPersonal.consultarActualEstadoVacaciones(secuencia);
        } catch (Exception e) {
            estadoVacaciones = null;
        }
        try {
            actualMVR = administrarCarpetaPersonal.consultarActualMVR(secuencia);
        } catch (Exception e) {
            actualMVR = null;
        }

        try {
            actualIBC = administrarCarpetaPersonal.actualIBC(secuencia, trabajador.getEmpleado().getEmpresa().getRetencionysegsocxpersona());
            //actualIBC = administrarCarpetaPersonal.actualIBC(secuencia, administrarCarpetaPersonal.obtenerEmpresa(trabajador.getEmpleado().getEmpresa()).getRetencionysegsocxpersona());
        } catch (Exception e) {
            actualIBC = null;
        }

        try {
            actualSet = administrarCarpetaPersonal.consultarActualSet(secuencia);
        } catch (Exception e) {
            actualSet = null;
        }

        try {
            actualComprobante = administrarCarpetaPersonal.consultarActualComprobante(secuencia);
        } catch (Exception e) {
            actualComprobante = null;
        }
        //RequestContext.getCurrentInstance().update("formulario:info:VCargoDesempeñado");
        //FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add("formulario:VCargoDesempeñado");
    }

    public void activos() {
        tipoPersonal = "activos";
        backup = vwActualesTiposTrabajadoresPosicion;
        tipoBk = tipo;
        tipo = "ACTIVO";
        posicion = 0;
        requerirTipoTrabajador(posicion);
        totalRegistros = administrarCarpetaPersonal.obtenerTotalRegistrosTipoTrabajador(tipo);
        RequestContext context = RequestContext.getCurrentInstance();
        if (vwActualesTiposTrabajadoresPosicion == null) {
            vwActualesTiposTrabajadoresPosicion = backup;
            Mensaje = "Activo";
            context.execute("alerta.show()");
            tipo = tipoBk;
        } else {
            backup = null;
            Imagen = "personal1" + extension;
            styleActivos = "ui-state-highlight";
            stylePensionados = "";
            styleRetirados = "";
            styleAspirantes = "";
            buscarEmp = false;
            tipoBk = null;
            acumulado = false;
            novedad = false;
            evaluacion = false;
            activo = false;
            pensionado = true;
            aspirante = false;
            hv1 = true;
            hv2 = false;
            mostrarT = true;
            try {
                valorInputText();
            } catch (ParseException ex) {
                //Logger.getLogger(ControlRemoto.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println(this.getClass().getName() + "activos() error ");
                ex.printStackTrace();
            }
            buscarEmplTipo = null;
            actualizarInformacionTipoTrabajador();
            context.update("form:tabmenu");
            context.update("form:tabmenu:activos");
            context.update("form:tabmenu:pensionados");
            context.update("form:tabmenu:retirados");
            context.update("form:tabmenu:aspirantes");
        }
    }

    public void pensionados() {
        tipoPersonal = "pensionados";
        backup = vwActualesTiposTrabajadoresPosicion;
        tipoBk = tipo;
        tipo = "PENSIONADO";
        posicion = 0;
        requerirTipoTrabajador(posicion);
        totalRegistros = administrarCarpetaPersonal.obtenerTotalRegistrosTipoTrabajador(tipo);
        RequestContext context = RequestContext.getCurrentInstance();
        if (vwActualesTiposTrabajadoresPosicion == null) {
            vwActualesTiposTrabajadoresPosicion = backup;
            Mensaje = "Pensionado";
            context.execute("alerta.show()");
            tipo = tipoBk;
        } else {
            backup = null;
            Imagen = "personal2" + extension;
            stylePensionados = "ui-state-highlight";
            styleActivos = "";
            styleRetirados = "";
            styleAspirantes = "";
            buscarEmp = false;
            tipoBk = null;
            acumulado = false;
            novedad = false;
            evaluacion = false;
            activo = true;
            pensionado = false;
            aspirante = true;
            hv1 = true;
            hv2 = true;
            mostrarT = true;
            try {
                valorInputText();
            } catch (ParseException ex) {
                //Logger.getLogger(ControlRemoto.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println(ControlRemoto.class.getName() + " error en la entrada");
            }
            buscarEmplTipo = null;
            actualizarInformacionTipoTrabajador();
            context.update("form:tabmenu");
            context.update("form:tabmenu:activos");
            context.update("form:tabmenu:pensionados");
            context.update("form:tabmenu:retirados");
            context.update("form:tabmenu:aspirantes");
        }
    }

    public void retirados() {
        tipoPersonal = "retirados";
        backup = vwActualesTiposTrabajadoresPosicion;
        tipoBk = tipo;
        tipo = "RETIRADO";
        posicion = 0;
        requerirTipoTrabajador(posicion);
        totalRegistros = administrarCarpetaPersonal.obtenerTotalRegistrosTipoTrabajador(tipo);
        RequestContext context = RequestContext.getCurrentInstance();
        if (vwActualesTiposTrabajadoresPosicion == null) {
            vwActualesTiposTrabajadoresPosicion = backup;
            Mensaje = "Retirado";
            context.execute("alerta.show()");
            tipo = tipoBk;
        } else {
            backup = null;
            Imagen = "personal3" + extension;
            styleRetirados = "ui-state-highlight";
            stylePensionados = "";
            styleActivos = "";
            styleAspirantes = "";
            buscarEmp = false;
            tipoBk = null;
            acumulado = false;
            novedad = false;
            evaluacion = true;
            activo = true;
            pensionado = true;
            aspirante = true;
            hv1 = true;
            hv2 = true;
            mostrarT = true;
            try {
                valorInputText();
            } catch (ParseException ex) {
                //Logger.getLogger(ControlRemoto.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println(ControlRemoto.class.getName() + " error en la entrada");
            }
            buscarEmplTipo = null;
            actualizarInformacionTipoTrabajador();
            context.update("form:tabmenu");
            context.update("form:tabmenu:activos");
            context.update("form:tabmenu:pensionados");
            context.update("form:tabmenu:retirados");
            context.update("form:tabmenu:aspirantes");
        }
    }

    public void aspirantes() {
        tipoPersonal = "aspirantes";
        backup = vwActualesTiposTrabajadoresPosicion;
        tipoBk = tipo;
        tipo = "DISPONIBLE";
        posicion = 0;
        requerirTipoTrabajador(posicion);
        totalRegistros = administrarCarpetaPersonal.obtenerTotalRegistrosTipoTrabajador(tipo);
        RequestContext context = RequestContext.getCurrentInstance();
        if (vwActualesTiposTrabajadoresPosicion == null) {
            vwActualesTiposTrabajadoresPosicion = backup;
            Mensaje = "Aspirante";
            context.execute("alerta.show()");
            tipo = tipoBk;
        } else {
            backup = null;
            Imagen = "personal4" + extension;
            styleAspirantes = "ui-state-highlight";
            stylePensionados = "";
            styleActivos = "";
            styleRetirados = "";
            buscarEmp = false;
            tipoBk = null;
            acumulado = true;
            novedad = true;
            evaluacion = false;
            activo = true;
            pensionado = true;
            aspirante = false;
            hv1 = false;
            hv2 = true;
            mostrarT = true;
            try {
                valorInputText();
            } catch (ParseException ex) {
                //Logger.getLogger(ControlRemoto.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println(ControlRemoto.class.getName() + " error en la entrada");
            }
            buscarEmplTipo = null;
            actualizarInformacionTipoTrabajador();
            context.update("form:tabmenu");
            context.update("form:tabmenu:activos");
            context.update("form:tabmenu:pensionados");
            context.update("form:tabmenu:retirados");
            context.update("form:tabmenu:aspirantes");
        }
    }

    public String pantallaReintegrar() {
        return accion;
    }

    public void pruebita() {
        Long result = Long.parseLong("-1");
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoPersonal.equals("activos")) {
            result = administrarCarpetaPersonal.borrarActivo(secuencia);
            if (result == 0) {
                context.update("formulariodialogos:activoeliminarpaso1");
                context.execute("activoeliminarpaso1.show()");
            } else {
                context.update("formulariodialogos:activonoeliminar");
                context.execute("activonoeliminar.show()");
            }
        } else if (tipoPersonal.equals("retirados")) {
            accion = "reintegro";
        }
        buscarEmplTipo = null;
        busquedaRapida = null;
    }

    public void paso2() {
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("formulariodialogos:activoeliminarpaso2");
        context.execute("activoeliminarpaso2.show()");
    }

    public void paso3() {
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("formulariodialogos:activoeliminarpaso3");
        context.execute("activoeliminarpaso3.show()");
    }

    public void paso4() {
        RequestContext context = RequestContext.getCurrentInstance();
        try {
            administrarCarpetaPersonal.borrarEmpleadoActivo(trabajador.getEmpleado().getSecuencia(), trabajador.getEmpleado().getPersona().getSecuencia());
            context.update("formulariodialogos:activoeliminarpaso4");
            context.execute("activoeeliminarpaso4.show()");
        } catch (Exception e) {
            System.out.println("Error en borrar al empleado");
        }
    }

    public void busquedaRapida() {
        System.out.println("Entro en busqueda Rapida()");
        RequestContext context = RequestContext.getCurrentInstance();
        filterBusquedaRapida = null;
        VWActualesTiposTrabajadores empleadoSeleccionado = administrarCarpetaPersonal.consultarActualTipoTrabajadorEmpleado(emplSeleccionado.getRfEmpleado());
        if (empleadoSeleccionado.getTipoTrabajador().getTipo().equalsIgnoreCase("activo")) {
            Imagen = "personal1" + extension;
            styleActivos = "ui-state-highlight";
            stylePensionados = "";
            styleRetirados = "";
            styleAspirantes = "";
            acumulado = false;
            novedad = false;
            evaluacion = false;
            activo = false;
            pensionado = true;
            aspirante = false;
            hv1 = true;
            hv2 = false;
            tipo = "ACTIVO";
        }
        if (empleadoSeleccionado.getTipoTrabajador().getTipo().equalsIgnoreCase("pensionado")) {
            Imagen = "personal2" + extension;
            stylePensionados = "ui-state-highlight";
            styleActivos = "";
            styleRetirados = "";
            styleAspirantes = "";
            acumulado = false;
            novedad = false;
            evaluacion = false;
            activo = true;
            pensionado = false;
            aspirante = true;
            hv1 = true;
            hv2 = true;
            tipo = "PENSIONADO";
        }
        if (empleadoSeleccionado.getTipoTrabajador().getTipo().equalsIgnoreCase("retirado")) {
            Imagen = "personal3" + extension;
            styleRetirados = "ui-state-highlight";
            stylePensionados = "";
            styleActivos = "";
            styleAspirantes = "";
            acumulado = false;
            novedad = false;
            evaluacion = true;
            activo = true;
            pensionado = true;
            aspirante = true;
            hv1 = true;
            hv2 = true;
            tipo = "RETIRADO";
        }
        if (empleadoSeleccionado.getTipoTrabajador().getTipo().equalsIgnoreCase("disponible")) {
            Imagen = "personal4" + extension;
            styleAspirantes = "ui-state-highlight";
            styleRetirados = "";
            stylePensionados = "";
            styleActivos = "";
            acumulado = true;
            novedad = true;
            evaluacion = false;
            activo = true;
            pensionado = true;
            aspirante = false;
            hv1 = false;
            hv2 = true;
            tipo = "DISPONIBLE";
        }

        context.reset("form:lvbusquedarapida:globalfilter");
        context.execute("lvbusquedarapida.clearFilters()");
        context.execute("lvbr.hide()");
        context.update("form:lvbusquedarapida");
        context.update("form:lvbr");
        context.update("form:buscarrap");

        vwActualesTiposTrabajadoresPosicion = empleadoSeleccionado;
        emplSeleccionado = null;
        buscar = true;
        buscarEmp = true;
        mostrarT = false;
        totalRegistros = 1;
        posicion = 0;

        context.update("form:tabmenu:activos");
        context.update("form:tabmenu:pensionados");
        context.update("form:tabmenu:retirados");
        context.update("form:tabmenu:aspirantes");

        context.update("form:tabmenu");
        context.update("form:tabmenu:tipopersonal");
        context.update("form:tabmenu:panelinf");
        context.update("form:tabmenu:mostrartodos");
        try {
            valorInputText();
        } catch (ParseException ex) {
            System.out.println(ControlRemoto.class.getName() + " error en la entrada");
        }
        actualizarInformacionTipoTrabajador();
        actualizarNavegacion();
    }

    public void busquedaRapidaAtras() {
        buscar = true;
        filterBusquedaRapida = null;
        emplSeleccionado = null;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lvbusquedarapida:globalfilter");
        context.execute("lvbusquedarapida.clearFilters()");
        context.execute("lvbr.hide()");
        context.update("form:lvbusquedarapida");
        context.update("form:lvbr");
        context.update("form:buscarrap");
    }

    public void activarBuscar() {
        buscar = false;
    }

    public void buscarEmpleado() {
        System.out.println("Entro en buscarEmpleado();");
        System.out.println("emplSeleccionadoBE : " + emplSeleccionadoBE);
        RequestContext context = RequestContext.getCurrentInstance();
        filterBuscarEmpleado = null;
        VWActualesTiposTrabajadores empleadoSeleccionado = administrarCarpetaPersonal.consultarActualTipoTrabajadorEmpleado(emplSeleccionadoBE.getRfEmpleado());
        tipo = empleadoSeleccionado.getTipoTrabajador().getTipo();
        vwActualesTiposTrabajadoresPosicion = empleadoSeleccionado;
        emplSeleccionadoBE = null;
        buscar = true;
        mostrarT = false;
        totalRegistros = 1;
        posicion = 0;

        context.update("form:tabmenu");
        context.update("form:tabmenu:mostrartodos");

        context.reset("form:lvbuscarempleado:globalfilter");
        context.execute("lvbuscarempleado.clearFilters()");
        context.execute("lvbe.hide()");
        context.update("form:lvbuscarempleado");
        context.update("form:lvbe");
        context.update("form:buscarempl");
        
        try {
            valorInputText();
        } catch (ParseException ex) {
            System.out.println(ControlRemoto.class.getName() + " error en la entrada");
        }
        actualizarInformacionTipoTrabajador();
        actualizarNavegacion();
    }

    public void busquedaEmpleadoAtras() {
        buscar = true;
        filterBuscarEmpleado = null;
        emplSeleccionadoBE = null;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lvbuscarempleado:globalfilter");
        context.execute("lvbuscarempleado.clearFilters()");
        context.execute("lvbe.hide()");
        context.update("form:lvbuscarempleado");
        context.update("form:lvbe");
        context.update("form:buscarempl");
    }

    public void mostrarTodos() {
//        posicion = 0;
        requerirTipoTrabajador(0);
        totalRegistros = administrarCarpetaPersonal.obtenerTotalRegistrosTipoTrabajador(tipo);
        mostrarT = true;
        buscarEmp = false;
        try {
            valorInputText();
        } catch (ParseException ex) {
            System.out.println(ControlRemoto.class.getName() + " error en la entrada");
        }
        actualizarInformacionTipoTrabajador();
        actualizarNavegacion();
    }

    public void probar(String nombreTab) {
        setNombreTabla(nombreTab);
    }

    public void lab() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tablaExportar.equals("tablas")) {
            tablasNombre = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:tabmenu:tablas:tablasnombre");
            tablasNombre.setFilterStyle("");
            tablasDescripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:tabmenu:tablas:tablasdescripcion");
            tablasDescripcion.setFilterStyle("");
            altoTablas = "176";
            context.update("form:tabmenu:tablas");
            filtrosActivos = true;
        } else if (tablaExportar.equals("data1")) {
            moduloCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:tabmenu:data1:modulocodigo");
            moduloCodigo.setFilterStyle("width: 40px;");
            moduloNombre = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:tabmenu:data1:modulonombre");
            moduloNombre.setFilterStyle("");
            moduloObs = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:tabmenu:data1:moduloobs");
            moduloObs.setFilterStyle("");
            altoModulos = "70";
            context.update("form:tabmenu:data1");
            filtrosActivos = true;
        }
    }

    public void lab2() {
        tablasNombre = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:tabmenu:tablas:tablasnombre");
        tablasNombre.setFilterStyle("display: none; visibility: hidden;");
        tablasDescripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:tabmenu:tablas:tablasdescripcion");
        tablasDescripcion.setFilterStyle("display: none; visibility: hidden;");
        altoTablas = "202";
        filtrosActivos = false;
        RequestContext.getCurrentInstance().update("form:tabmenu:tablas");
    }
/////////////////////////  
    private Integer progress;

    public Integer getProgress() {
        if (progress == null) {
            progress = 0;
        } else {
            progress = progress + 1;
            RequestContext.getCurrentInstance().update("formu:progreso");
            if (progress > 100) {
                progress = 100;
            }
        }
        return progress;
    }

    public void cancel() {
        progress = null;
    }

// Carpeta Designer //
    public List<Modulos> getListModulos() {
        if (listModulos == null) {
            listModulos = administrarCarpetaDesigner.consultarModulos();
            return listModulos;
        } else {
            return listModulos;
        }
    }

    public void cambiarTablas() {
        secuenciaMod = selectModulo.getSecuencia();
        listTablas = administrarCarpetaDesigner.consultarTablas(secuenciaMod);
        /*
         * if (listTablas != null && !listTablas.isEmpty()) { buscarTablasLOV =
         * false; } else { buscarTablasLOV = true; }
         */
        buscarTablasLOV = (listTablas == null || listTablas.isEmpty());
        RequestContext context = RequestContext.getCurrentInstance();
        if (tablaExportar.equals("tablas")) {
            tablasNombre = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:tabmenu:tablas:tablasnombre");
            tablasNombre.setFilterStyle("display: none; visibility: hidden;");
            tablasDescripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:tabmenu:tablas:tablasdescripcion");
            tablasDescripcion.setFilterStyle("display: none; visibility: hidden;");
            context.execute("tabl.clearFilters()");
            altoTablas = "202";
            context.update("form:tabmenu:tablas");
            filtrosActivos = false;
        }
        mostrarTodasTablas = true;
        context.update("form:tabmenu:mostrartodastablas");
        context.update("form:tabmenu:buscartablas");
        tablaExportar = "data1";
        nombreArchivo = "modulos";
        filterListTablas = null;
    }

    public void exportarTabla() {
        if (tablaExportar.equals("data1")) {
            moduloCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:tabmenu:data1:modulocodigo");
            moduloCodigo.setFilterStyle("display: none; visibility: hidden;");
            moduloNombre = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:tabmenu:data1:modulonombre");
            moduloNombre.setFilterStyle("display: none; visibility: hidden;");
            moduloObs = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:tabmenu:data1:moduloobs");
            moduloObs.setFilterStyle("display: none; visibility: hidden;");
            filtradolistModulos = null;
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("data1.clearFilters()");
            altoModulos = "93";
            context.update("form:tabmenu:data1");
            filtrosActivos = false;
        }
        tablaExportar = "tablas";
        nombreArchivo = "tablas";
    }

    public void redireccion(Integer indice) {
        if (indice >= 0) {
            if (listTablas.get(indice).getNombre().equalsIgnoreCase("USUARIOS")) {
                RequestContext context = RequestContext.getCurrentInstance();
                context.execute("dirigirusuario()");

            } else if (listTablas.get(indice).getNombre().equalsIgnoreCase("USUARIOSVISTAS")) {
                RequestContext context = RequestContext.getCurrentInstance();
                context.execute("dirigirusuariovista()");

            }// Aca vienen un huevo de Else if para el resto de las pantallas
        }
        infoTablas(selectTabla);
    }

    public void infoTablas(Tablas tab) {

        selectTabla = tab;
        BigInteger secuenciaTab = selectTabla.getSecuencia();
        pantalla = administrarCarpetaDesigner.consultarPantalla(secuenciaTab);
        RequestContext context = RequestContext.getCurrentInstance();
        tablaExportar = "tablas";
        context.execute("ventanatab.show()");
    }

    public void requerirBusquedaRapida() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (busquedaRapida == null) {
            filterBusquedaRapida = null;
            busquedaRapida = administrarCarpetaPersonal.consultarRapidaEmpleados();
            contarRegistrosBR();
            context.update("form:lvbr");
        }
        context.execute("lvbr.show();");
    }

    public void requerirBuscarEmpleadoTipo() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (buscarEmplTipo == null || buscarEmplTipo.isEmpty()) {
            filterBuscarEmpleado = null;
            buscarEmplTipo = administrarCarpetaPersonal.consultarEmpleadosTipoTrabajador(tipo);
            contarRegistrosBE();
            context.update("form:lvbe");
        }
        context.execute("lvbe.show()");
        //actualizarInformacionTipoTrabajador();
    }

    public void primerTipoTrabajador() {
        posicion = 0;
        requerirTipoTrabajador(posicion);
        try {
            valorInputText();
        } catch (ParseException ex) {
            //Logger.getLogger(ControlRemoto.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("error en primerTipoTrabajador");
        }
        actualizarInformacionTipoTrabajador();
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("form:tabmenu");
    }

    public void atrasTipoTrabajador() {
        if (posicion > 0) {
            posicion--;
            requerirTipoTrabajador(posicion);
            try {
                valorInputText();
            } catch (ParseException ex) {
                //Logger.getLogger(ControlRemoto.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println(ControlRemoto.class.getName() + " error en la entrada.");
            }
            actualizarInformacionTipoTrabajador();
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("form:tabmenu");
        }

    }

    public void siguienteTipoTrabajador() {
        if (posicion <= (totalRegistros - 1)) {
            posicion++;
            requerirTipoTrabajador(posicion);
            try {
                valorInputText();
            } catch (ParseException ex) {
                //Logger.getLogger(ControlRemoto.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println(ControlRemoto.class.getName() + " error en la entrada");
            }
            actualizarInformacionTipoTrabajador();
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("form:tabmenu");
        }
    }

    public void ultimoTipoTrabajador() {
        posicion = totalRegistros - 1;
        requerirTipoTrabajador(posicion);
        try {
            valorInputText();
        } catch (ParseException ex) {
            //Logger.getLogger(ControlRemoto.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ControlRemoto.class.getName() + " error en la entrada");
        }
        actualizarInformacionTipoTrabajador();
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("form:tabmenu");
    }

    public void validarEstadoNominaF() {
        int totalActual = administrarCarpetaPersonal.obtenerTotalRegistrosTipoTrabajador(tipo);
        if (totalActual != totalRegistros) {
            posicion = 0;
            totalRegistros = totalActual;
            primerTipoTrabajador();
            buscarEmplTipo = null;
            busquedaRapida = null;
            actualizarInformacionTipoTrabajador();
        }
    }

    public void actualizarInformacionTipoTrabajador() {
        System.out.println("actualizarInformacionTipoTrabajador() 1");
        informacionTiposTrabajadores = "Reg. " + (posicion + 1) + " de " + totalRegistros;
        RequestContext.getCurrentInstance().update("form:tabmenu:informaciontt");
        System.out.println("actualizarInformacionTipoTrabajador() 2");
    }

    public void actualizarNavegacion() {
        System.out.println("actualizarNavegacion() 1");
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("form:tabmenu:btnprimero");
        context.update("form:tabmenu:btnatras");
        context.update("form:tabmenu:btnsiguiente");
        context.update("form:tabmenu:btnultimo");
        System.out.println("actualizarNavegacion() 2");
    }

    public VWActualesTiposTrabajadores requerirTipoTrabajador(int posicion) {
        vwActualesTiposTrabajadoresPosicion = administrarCarpetaPersonal.consultarEmpleadosTipoTrabajadorPosicion(tipo, posicion);
        return vwActualesTiposTrabajadoresPosicion;
    }

    public void llenarBannerDefault() {
        banner.clear();
        //banner.add(new BannerInicioRed("Imagenes/sinPublicidad.JPG", ""));
        banner.add(new BannerInicioRed("http://www.nomina.com.co/images/publicidadInn/pInn01.png", "www.nomina.com.co"));
        banner.add(new BannerInicioRed("http://www.nomina.com.co/images/publicidadInn/pInn02.png", "www.nomina.com.co"));
        banner.add(new BannerInicioRed("https://www.nomina.com.co/images/publicidadInn/pInn03.png", "www.nomina.com.co"));
        banner.add(new BannerInicioRed("https://www.nomina.com.co/images/publicidadInn/pInn04.png", "www.nomina.com.co"));
    }

    public List<Tablas> getListTablas() {
        return listTablas;
    }

    public Modulos getSelectModulo() {
        System.out.println(this.getClass().getName() + ".getSelectModulo()");
        if (selectModulo == null) {
            if (listModulos == null) {
                getListModulos();
            }
            if (listModulos != null && !listModulos.isEmpty()) {
                selectModulo = listModulos.get(0);
                secuenciaMod = selectModulo.getSecuencia();
                listTablas = administrarCarpetaDesigner.consultarTablas(secuenciaMod);
                if (listTablas != null && !listTablas.isEmpty()) {
                    buscarTablasLOV = false;
                }
            } else {
                listTablas = null;
            }
            return selectModulo;
        } else {
            return selectModulo;
        }
    }

    public void setSelectModulo(Modulos selectModulo) {
        this.selectModulo = selectModulo;
    }

    public Tablas getSelectTabla() {
        return selectTabla;
    }

    public void setSelectTabla(Tablas selectTabla) {
        this.selectTabla = selectTabla;
    }

    public Pantallas getPantalla() {
        return pantalla;
    }

    public void setPantalla(Pantallas pantalla) {
        this.pantalla = pantalla;
    }

    public String getTablaExportar() {
        return tablaExportar;
    }

    public List<Tablas> getFilterListTablas() {
        return filterListTablas;
    }

    public void setFilterListTablas(List<Tablas> filterListTablas) {
        this.filterListTablas = filterListTablas;
    }

    public List<Modulos> getFiltradolistModulos() {
        return filtradolistModulos;
    }

    public void setFiltradolistModulos(List<Modulos> filtradolistModulos) {
        this.filtradolistModulos = filtradolistModulos;
    }

    // Getter and Setter //
    public Empleados getEmpleado() {
        return empleado;
    }

    public VWActualesCargos getVwActualesCargos() {
        return vwActualesCargos;
    }

    public VWActualesTiposContratos getVwActualesTiposContratos() {
        return vwActualesTiposContratos;
    }

    public VWActualesNormasEmpleados getVwActualesNormasEmpleados() {
        return vwActualesNormasEmpleados;
    }

    public String getFechaActualesTiposContratos() {
        return fechaActualesTiposContratos;
    }

    public VWActualesAfiliacionesSalud getVwActualesAfiliacionesSalud() {
        return vwActualesAfiliacionesSalud;
    }

    public VWActualesAfiliacionesPension getVwActualesAfiliacionesPension() {
        return vwActualesAfiliacionesPension;
    }

    public VWActualesLocalizaciones getVwActualesLocalizaciones() {
        return vwActualesLocalizaciones;
    }

    public VWActualesTiposTrabajadores getVwActualesTiposTrabajadores() {
        return vwActualesTiposTrabajadores;
    }

    public VWActualesContratos getVwActualesContratos() {
        return vwActualesContratos;
    }

    public VWActualesJornadas getVwActualesJornadas() {
        return vwActualesJornadas;
    }

    public String getSueldo() {
        return Sueldo;
    }

    public VWActualesReformasLaborales getVwActualesReformasLaborales() {
        return vwActualesReformasLaborales;
    }

    public VWActualesUbicaciones getVwActualesUbicaciones() {
        return vwActualesUbicaciones;
    }

    public VWActualesFormasPagos getVwActualesFormasPagos() {
        return vwActualesFormasPagos;
    }

    public VWActualesVigenciasViajeros getVwActualesVigenciasViajeros() {
        return vwActualesVigenciasViajeros;
    }

    public String getNombreTabla() {
        nombreTabla = "data1";
        return nombreTabla;
    }

    public void setNombreTabla(String nombreTabla) {
        this.nombreTabla = nombreTabla;
    }

    public VWActualesTiposTrabajadores getVwActualesTiposTrabajadoresesPosicion() {
        return vwActualesTiposTrabajadoresPosicion;
    }

    public String getMensaje() {
        return Mensaje;
    }

    public String getImagen() {
        return Imagen;
    }

    public DetallesEmpresas getDetallesEmpresas() {
        detallesEmpresas = administrarCarpetaPersonal.consultarDetalleEmpresaUsuario();
        return detallesEmpresas;
    }

    public Usuarios getUsuarios() {
        String alias = administrarCarpetaPersonal.consultarAliasActualUsuario();
        usuarios = administrarCarpetaPersonal.consultarUsuario(alias);
        return usuarios;
    }

    public ParametrosEstructuras getParametrosEstructuras() {
        return parametrosEstructuras;
    }

    public String getFechaDesde() {
        parametrosEstructuras = administrarCarpetaPersonal.consultarParametrosUsuario();
        FechaDesde = formato.format(parametrosEstructuras.getFechadesdecausado());
        return FechaDesde;
    }

    public String getFechaHasta() {
        FechaHasta = formato.format(parametrosEstructuras.getFechahastacausado());
        return FechaHasta;
    }

    public String getFechaSistema() {
        FechaSistema = formato.format(parametrosEstructuras.getFechasistema());
        return FechaSistema;
    }

    public List<VigenciasCargos> getVigenciasCargosEmpleados() {
        //BigInteger s = BigInteger.valueOf(10661039);
        vigenciasCargosEmpleados = administrarCarpetaPersonal.consultarVigenciasCargosEmpleado(secuencia);
        return vigenciasCargosEmpleados;
    }

    public VigenciasCargos getVigenciaSeleccionada() {
        return vigenciaSeleccionada;
    }

    public void setVigenciaSeleccionada(VigenciasCargos vigenciaSeleccionada) {
        this.vigenciaSeleccionada = vigenciaSeleccionada;
    }

    public VWActualesTiposTrabajadores getTrabajador() {
        return trabajador;
    }

    public boolean isAcumulado() {
        return acumulado;
    }

    public boolean isNovedad() {
        return novedad;
    }

    public boolean isEvaluacion() {
        return evaluacion;
    }

    public boolean isActivo() {
        return activo;
    }

    public boolean isPensionado() {
        return pensionado;
    }

    public boolean isAspirante() {
        return aspirante;
    }

    public boolean isHv1() {
        return hv1;
    }

    public boolean isHv2() {
        return hv2;
    }

    public VwTiposEmpleados getEmplSeleccionado() {
        return emplSeleccionado;
    }

    public void setEmplSeleccionado(VwTiposEmpleados emplSeleccionado) {
        this.emplSeleccionado = emplSeleccionado;
    }

    public String getMensajePagos() {
        return mensajePagos;
    }

    public String getTituloPago() {
        return tituloPago;
    }

    public String getPago() {
        return pago;
    }

    public void setPago(String pago) {
        this.pago = pago;
    }

    public List<VwTiposEmpleados> getBusquedaRapida() {
        return busquedaRapida;
    }

    public List<VwTiposEmpleados> getFilterBusquedaRapida() {
        return filterBusquedaRapida;
    }

    public void setFilterBusquedaRapida(List<VwTiposEmpleados> filterBusquedaRapida) {
        this.filterBusquedaRapida = filterBusquedaRapida;
    }

    public List<VwTiposEmpleados> getFilterBuscarEmpleado() {
        return filterBuscarEmpleado;
    }

    public void setFilterBuscarEmpleado(List<VwTiposEmpleados> filterBuscarEmpleado) {
        this.filterBuscarEmpleado = filterBuscarEmpleado;
    }

    public VwTiposEmpleados getEmplSeleccionadoBE() {
        return emplSeleccionadoBE;
    }

    public void setEmplSeleccionadoBE(VwTiposEmpleados emplSeleccionadoBE) {
        this.emplSeleccionadoBE = emplSeleccionadoBE;
    }

    public boolean isBuscar() {
        return buscar;
    }

    public boolean isBuscarEmp() {
        return buscarEmp;
    }

    public boolean isMostrarT() {
        return mostrarT;
    }

    public List<VwTiposEmpleados> getBuscarEmplTipo() {
        return buscarEmplTipo;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public BigInteger getIdentificacion() {
        return identificacion;
    }

    public Integer getEstadoEmpleado() {
        return estadoEmpleado;
    }

    public int getNumPestanha() {
        return numPestanha;
    }

    public void setNumPestanha(int numPestanha) {
        this.numPestanha = numPestanha;
    }

    public String getAltoModulos() {
        return altoModulos;
    }

    public String getAltoTablas() {
        return altoTablas;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public List<Tablas> getListTablasLOV() {
        return listTablasLOV;
    }

    public void setListTablasLOV(List<Tablas> listTablasLOV) {
        this.listTablasLOV = listTablasLOV;
    }

    public List<Tablas> getFiltradoListTablasLOV() {
        return filtradoListTablasLOV;
    }

    public void setFiltradoListTablasLOV(List<Tablas> filtradoListTablasLOV) {
        this.filtradoListTablasLOV = filtradoListTablasLOV;
    }

    public Tablas getSeleccionTablaLOV() {
        return seleccionTablaLOV;
    }

    public void setSeleccionTablaLOV(Tablas seleccionTablaLOV) {
        this.seleccionTablaLOV = seleccionTablaLOV;
    }

    public String getEstadoVacaciones() {
        return estadoVacaciones;
    }

    public BigDecimal getActualMVR() {
        return actualMVR;
    }

    public void setActualMVR(BigDecimal actualMVR) {
        this.actualMVR = actualMVR;
    }

    public void setEstadoVacaciones(String estadoVacaciones) {
        this.estadoVacaciones = estadoVacaciones;
    }

    public String getActualIBC() {
        return actualIBC;
    }

    public void setActualIBC(String actualIBC) {
        this.actualIBC = actualIBC;
    }

    public String getActualSet() {
        return actualSet;
    }

    public void setActualSet(String actualSet) {
        this.actualSet = actualSet;
    }

    public String getActualComprobante() {
        return actualComprobante;
    }

    public void setActualComprobante(String actualComprobante) {
        this.actualComprobante = actualComprobante;
    }

    public String getStyleActivos() {
        return styleActivos;
    }

    public String getStylePensionados() {
        return stylePensionados;
    }

    public String getStyleRetirados() {
        return styleRetirados;
    }

    public String getStyleAspirantes() {
        return styleAspirantes;
    }

    public String getActualCargo() {
        return actualCargo;
    }

    public String getFotoEmpleado() {
        persona = administrarCarpetaPersonal.consultarFotoPersona(identificacion);
        if (persona.getPathfoto() == null || persona.getPathfoto().equalsIgnoreCase("N")) {
            fotoEmpleado = "default.jpg";
            return fotoEmpleado;
        } else {
            fotoEmpleado = identificacion.toString() + ".jpg";
            return fotoEmpleado;
        }
    }

    public void pestanhaActual(TabChangeEvent event) {
        Tab pestanha = event.getTab();
        if (pestanha.getId().equals("personal")) {
            numPestanha = 0;
        } else if (pestanha.getId().equals("nomina")) {
            numPestanha = 1;
        } else if (pestanha.getId().equals("integracion")) {
            numPestanha = 2;
        } else if (pestanha.getId().equals("gerencial")) {
            numPestanha = 3;
        } else if (pestanha.getId().equals("designer")) {
            numPestanha = 4;
        }
    }

    public void recargar() {
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("form:tablainferiorderecha");
        context.update("form:tablainferiorizquierda");
    }

    public void activarFiltro() {
        Column columna;
        Column columna2;
        Column columna3;
        DataTable tabla;
        DataTable tabla2;
        tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:tablasuperiorderecha");
        tabla2 = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:tablasuperiorizquierda");
        columna = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:tablasuperiorderecha:modulonombre2");
        columna2 = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:tablasuperiorderecha:moduloobs2");
        columna3 = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:tablasuperiorizquierda:modulocodigo1");
        tabla.setStyle("font-size: 11px; width: 600px; position: relative;");
        tabla2.setStyle("font-size: 11px; width: 300px; position: relative; top: -6px;");
        columna.setFilterStyle("");
        columna2.setFilterStyle("");
        columna3.setFilterStyle("");
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("form:tablasuperiorderecha");
        context.update("form:tablasuperiorizquierda");
    }

    public void cambiarFormaPago() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (pago.equalsIgnoreCase("AUTOMATICO")) {
            tituloPago = "PAGOS AUTOMATICOS";
            mensajePagos = "Realice liquidaciones automáticas quincenales, mensuales, entre otras, por estructuras o por tipo de empleado. Primero ingrese los parametros a liquidar, después genere la liquidación para luego poder observar los comprobantes de pago. Usted puede deshacer todas las liquidaciones que desee siempre y cuando no se hayan cerrado. Al cerrar una liquidación se generan acumulados, por eso es importante estar seguro que la liquidación es correcta antes de cerrarla.";
            context.update("form:tabmenu:tipopago");
            context.update("form:tabmenu:mensajepago");
        } else if (pago.equalsIgnoreCase("NO AUTOMATICO")) {
            tituloPago = "PAGOS POR FUERA DE NÓMINA";
            mensajePagos = "Genere pagos por fuera de nómina cuando necesite liquidar vacaciones por anticipado, viaticos, entre otros. esta liquidaciones se pueden efectuar por estructura o por empleado. Primero ingrese los parametros a liquidar, después genere la liquidación para luego poder observar los comprobantes de pago. Usted puede deshacer todas las liquidaciones que desee siempre y cuando no se hayan cerrado. Al cerrar una liquidación se generan acumulados, por eso es importante estar seguro que la liquidación es correcta antes de cerrarla.";
            context.update("form:tabmenu:tipopago");
            context.update("form:tabmenu:mensajepago");
        }
    }

    public void buscarTablas() {
        if (selectModulo != null) {
            filtradoListTablasLOV = null;
            listTablasLOV = administrarCarpetaDesigner.consultarTablas(selectModulo.getSecuencia());
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("form:lovtablas");
            context.execute("buscartablasdialogo.show()");
        }
    }

    public void seleccionTabla() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (filtrosActivos == true) {
            tablasNombre = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:tabmenu:tablas:tablasnombre");
            tablasNombre.setFilterStyle("display: none; visibility: hidden;");
            tablasDescripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:tabmenu:tablas:tablasdescripcion");
            tablasDescripcion.setFilterStyle("display: none; visibility: hidden;");
            altoTablas = "200";
            filtrosActivos = false;
            context.execute("tabl.clearFilters()");
        }
        tablaExportar = "data1";
        nombreArchivo = "modulos";
        listTablas.clear();
        listTablas.add(seleccionTablaLOV);
        mostrarTodasTablas = false;
        filtradoListTablasLOV = null;
        seleccionTablaLOV = null;
        buscar = true;
        context.reset("form:lovtablas:globalfilter");
        context.execute("lovtablas.clearFilters()");
        context.execute("buscartablasdialogo.hide()");
        //context.update("form:lovTablas");
        context.update("form:mostrartodastablas");
        context.update("form:tabmenu:tablas");
        context.update("form:tabmenu:mostrartodastablas");
    }

    public void cancelarSeleccionTabla() {
        filtradoListTablasLOV = null;
        seleccionTablaLOV = null;
        buscar = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovtablas:globalfilter");
        context.execute("lovtablas.clearFilters()");
        context.execute("buscartablasdialogo.hide()");
    }

    public void mostrarTodo_Tablas() {
        listTablas.clear();
        listTablas = administrarCarpetaDesigner.consultarTablas(selectModulo.getSecuencia());
        filterListTablas = null;
        mostrarTodasTablas = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("form:tabmenu:tablas");
        context.update("form:tabmenu:mostrartodastablas");
    }

    public void validarBorradoLiquidacion() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (pago.equals("AUTOMATICO")) {
            context.execute("confirmarborrarliquidacion.show()");
        } else if (pago.equals("NO AUTOMATICO")) {
            context.execute("confirmarborrarliquidacionporfuera.show()");
        }
    }

    public void borrarLiquidacion() {
        administrarCarpetaPersonal.borrarLiquidacionAutomatico();
        mensajeResultadoBorrarLiquidacion();
    }

    public void borrarLiquidacionPorFuera() {
        administrarCarpetaPersonal.borrarLiquidacionNoAutomatico();
        mensajeResultadoBorrarLiquidacion();
    }

    public void mensajeResultadoBorrarLiquidacion() {
        RequestContext context = RequestContext.getCurrentInstance();
        FacesMessage msg = new FacesMessage("Información", "Liquidación borrada con éxito.");
        FacesContext.getCurrentInstance().addMessage(null, msg);
        context.update("form:growl");
    }

    public void redireccionPersonaIndividual() {
        FacesContext fc = FacesContext.getCurrentInstance();
        anularBotonEmpresas();
        fc.getApplication().getNavigationHandler().handleNavigation(fc, null, "personaIndividual");
    }

    public void anularBotonEmpresas() {
        activarAceptarEmpresas = true;
        RequestContext.getCurrentInstance().update("form:tabmenu:aceptaremp");
    }

    public void eventoFiltrarEmpresas() {
        anularBotonEmpresas();
        infoRegistroEmpresas = String.valueOf(filtradoLOVEmpresas.size());
        RequestContext.getCurrentInstance().update("form:tabmenu:infoRegistroEmpresas");
    }

    public void eventoFiltrarBusquedaRapida() {
        modificarInfoRegistroBR(filterBusquedaRapida.size());
    }

    public void modificarInfoRegistroBR(int valor) {
        infoRegistroBusquedaRapida = String.valueOf(valor);
        RequestContext.getCurrentInstance().update("form:inforegistrobusquedarapida");
    }

    public void modificarInfoRegistroBE(int valor) {
        infoRegistroBuscarEmpleados = String.valueOf(valor);
        RequestContext.getCurrentInstance().update("form:inforegistrobuscarempleados");
    }

    public void eventoFiltrarBusquedaEmpleado() {
        modificarInfoRegistroBE(filterBuscarEmpleado.size());
    }

    public void modificarInfoRegistroBT(int valor) {
        infoRegistroBuscarTablas = String.valueOf(valor);
        RequestContext.getCurrentInstance().update("form:inforegistrobuscartablas");
    }

    public void eventoFiltrarBusquedaTablas() {
        modificarInfoRegistroBT(filtradoListTablasLOV.size());
    }

    public void contarRegistrosBE() {
        if (buscarEmplTipo == null || buscarEmplTipo.isEmpty()) {
            modificarInfoRegistroBE(0);
        } else {
            modificarInfoRegistroBE(buscarEmplTipo.size());
        }
    }

    public void contarRegistrosBR() {
        if (busquedaRapida == null || busquedaRapida.isEmpty()) {
            modificarInfoRegistroBR(0);
        } else {
            modificarInfoRegistroBR(busquedaRapida.size());
        }
    }

    public void activarAceptarEmp() {
        System.out.println("Entro en activarAceptarEmp()");
        activarAceptarEmpresas = false;
        RequestContext.getCurrentInstance().update("form:tabmenu:aceptaremp");
    }

    public void asignarUnicaEmpresa() {
        System.out.println("Entro en asignarUnicaEmpresa() 1");
        RequestContext context = RequestContext.getCurrentInstance();
        getLovEmpresas();
        if (lovEmpresas != null) {
            if (lovEmpresas.size() == 1) {
                unicaEmpresa = lovEmpresas.get(0);
                redireccionPersonaIndividual();
            } else {
                infoRegistroEmpresas = String.valueOf(lovEmpresas.size());
                context.update("form:LovEmpresasDialogo");
                context.update("form:LovEmpresasTabla");
                context.update("form:infoRegistroEmpresas");
                context.execute("LovEmpresasDialogo.show()");
            }
        }
        System.out.println("Unica Empresa : " + unicaEmpresa);
    }

    public void actualizarLOVEmpresas() {
        System.out.println("Entro en actualizarLOVEmpresas()");
        RequestContext context = RequestContext.getCurrentInstance();
        getLovEmpresas();
        if (lovEmpresas != null) {
            if (lovEmpresas.size() == 1) {
                redireccionPersonaIndividual();
            } else {
                infoRegistroEmpresas = String.valueOf(lovEmpresas.size());
                context.update("form:LovEmpresasDialogo");
                context.update("form:LovEmpresasTabla");
                context.update("form:infoRegistroEmpresas");
                context.execute("LovEmpresasDialogo.show()");
            }
        }
    }

    public boolean isBuscarTablasLOV() {
        return buscarTablasLOV;
    }

    public boolean isMostrarTodasTablas() {
        return mostrarTodasTablas;
    }

    public boolean isBandera() {
        return bandera;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public String getInfoRegistroBuscarEmpleados() {
        return infoRegistroBuscarEmpleados;
    }

    public void setInfoRegistroBuscarEmpleados(String infoRegistroBuscarEmpleados) {
        this.infoRegistroBuscarEmpleados = infoRegistroBuscarEmpleados;
    }

    public String getInformacionTiposTrabajadores() {
        return informacionTiposTrabajadores;
    }

    public String getInfoRegistroBusquedaRapida() {
        return infoRegistroBusquedaRapida;
    }

    public List<BannerInicioRed> getBanner() {
        return banner;
    }

    public void setBanner(List<BannerInicioRed> banner) {
        this.banner = banner;
    }

    public List<Empresas> getFiltradoLOVEmpresas() {
        return filtradoLOVEmpresas;
    }

    public void setFiltradoLOVEmpresas(List<Empresas> filtradoLOVEmpresas) {
        this.filtradoLOVEmpresas = filtradoLOVEmpresas;
    }

    public String getInfoRegistroEmpresas() {
        return infoRegistroEmpresas;
    }

    public void setInfoRegistroEmpresas(String infoRegistroEmpresas) {
        this.infoRegistroEmpresas = infoRegistroEmpresas;
    }

    public List<Empresas> getLovEmpresas() {
        if (lovEmpresas == null) {
            lovEmpresas = administrarCarpetaPersonal.consultarEmpresas();
            System.out.println("cargo getLovEmpresas");
        }
        return lovEmpresas;
    }

    public void setLovEmpresas(List<Empresas> lovEmpresas) {
        this.lovEmpresas = lovEmpresas;
    }

    public Empresas getEmpresaSeleccionada() {
        return empresaSeleccionada;
    }

    public void setEmpresaSeleccionada(Empresas empresaSeleccionada) {
        this.empresaSeleccionada = empresaSeleccionada;
    }

    public boolean isActivarAceptarEmpresas() {
        return activarAceptarEmpresas;
    }

    public void setActivarAceptarEmpresas(boolean activarAceptarEmpresas) {
        this.activarAceptarEmpresas = activarAceptarEmpresas;
    }

    public Empresas getUnicaEmpresa() {
        if (unicaEmpresa == null) {
            getLovEmpresas();
            if (lovEmpresas != null) {
                unicaEmpresa = lovEmpresas.get(0);
            }
        }
        return unicaEmpresa;
    }

    public void setUnicaEmpresa(Empresas unicaEmpresa) {
        this.unicaEmpresa = unicaEmpresa;
    }

    public String getInfoRegistroBuscarTablas() {
        return infoRegistroBuscarTablas;
    }

    public void setInfoRegistroBuscarTablas(String infoRegistroBuscarTablas) {
        this.infoRegistroBuscarTablas = infoRegistroBuscarTablas;
    }

}
