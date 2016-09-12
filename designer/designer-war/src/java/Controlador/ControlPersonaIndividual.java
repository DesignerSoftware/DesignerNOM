package Controlador;

import Entidades.*;
import InterfaceAdministrar.AdministrarCarpetaPersonalInterface;
import InterfaceAdministrar.AdministrarPersonaIndividualInterface;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
//import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.primefaces.context.RequestContext;
import utilidadesUI.PrimefacesContextUI;

/**
 * @author Administrador
 */
@ManagedBean
@SessionScoped
public class ControlPersonaIndividual implements Serializable {

    @EJB
    AdministrarPersonaIndividualInterface administrarPersonaIndividual;
    @EJB
    AdministrarCarpetaPersonalInterface administrarCarpetaPersonal;
    //Modulo Informacion Personal
    private Personas nuevaPersona;
    private Empleados nuevoEmpleado;
    private int idInformacionPersonal;
    private boolean permitirIndexInformacionPersonal;
    private String auxInformacionPersonaEmpresal, auxInformacionPersonalCiudadNacimiento;
    private String auxInformacionPersonalCiudadDocumento, auxInformacionPersonalTipoDocumento;
    private Date fechaIngreso, fechaCorte;
    private Date auxFechaNacimiento, auxFechaIngreso, auxFechaCorte;
    //Modulo Cargo Desempeñado
    private VigenciasCargos nuevaVigenciaCargo;
    private int idCargoDesempeñado;
    private boolean permitirIndexCargoDesempeñado;
    private String auxCargoDesempeñadoNombreCargo, auxCargoDesempeñadoMotivoCargo, auxCargoDesempeñadoNombreEstructura;
    private String auxCargoDesempeñadoPapel, auxCargoDesempeñadoEmpleadoJefe;
    //Modulo Centro de Costo - Localizacion
    private VigenciasLocalizaciones nuevaVigenciaLocalizacion;
    private int idCentroCosto;
    private boolean permitirIndexCentroCosto;
    private String auxCentroCostoMotivo, auxCentroCostoEstructura;
    //Modulo Tipo Trabajador
    private VigenciasTiposTrabajadores nuevaVigenciaTipoTrabajador;
    private int idTipoTrabajador;
    private boolean permitirIndexTipoTrabajador;
    private String auxTipoTrabajadorNombreTipoTrabajador;
    //Modulo Reforma Laboral
    private VigenciasReformasLaborales nuevaVigenciaReformaLaboral;
    private int idTipoSalario;
    private boolean permitirIndexTipoSalario;
    private String auxTipoSalarioReformaLaboral;
    //Modulo Sueldo
    private VigenciasSueldos nuevaVigenciaSueldo;
    private int idSueldo;
    private boolean permitirIndexSueldo;
    private String auxSueldoMotivoSueldo, auxSueldoTipoSueldo, auxSueldoUnidad;
    private BigDecimal valorSueldo;
    private BigDecimal auxSueldoValor;
    //Modulo Tipo Contrato
    private VigenciasTiposContratos nuevaVigenciaTipoContrato;
    private int idTipoContrato;
    private boolean permitirIndexTipoContrato;
    private String auxTipoContratoMotivo, auxTipoContratoTipoContrato;
    private Date auxTipoContratoFecha;
    //Modulo Norma Laboral
    private VigenciasNormasEmpleados nuevaVigenciaNormaEmpleado;
    private int idNormaLaboral;
    private boolean permitirIndexNormaLaboral;
    private String auxNormaLaboralNorma;
    //Modulo Legislacion Laboral
    private VigenciasContratos nuevaVigenciaContrato;
    private int idLegislacionLaboral;
    private boolean permitirIndexLegislacionLaboral;
    private String auxLegislacionLaboralNombre;
    // Modulo Ubicacion Geografica
    private VigenciasUbicaciones nuevaVigenciaUbicacion;
    private int idUbicacionGeografica;
    private boolean permitirIndexUbicacionGeografica;
    private String auxUbicacionGeograficaUbicacion;
    //Modulo Jornada Laboral
    private VigenciasJornadas nuevaVigenciaJornada;
    private int idJornadaLaboral;
    private boolean permitirIndexJornadaLaboral;
    private String auxJornadaLaboralJornada;
    //Modulo Forma Pago
    private VigenciasFormasPagos nuevaVigenciaFormaPago;
    private int idFormaPago;
    private boolean permitirIndexFormaPago;
    private String auxFormaPagoPeriodicidad, auxFormaPagoSucursal, auxFormaPagoMetodo;
//    Modulo Afiliaciones
    //EPS
    private VigenciasAfiliaciones nuevaVigenciaAfiliacionEPS;
    private int idAfiliacionEPS;
    private boolean permitirIndexAfiliacionEPS;
    private String auxAfiliacionEPS;
    //AFP
    private VigenciasAfiliaciones nuevaVigenciaAfiliacionAFP;
    private int idAfiliacionAFP;
    private boolean permitirIndexAfiliacionAFP;
    private String auxAfiliacionAFP;
    //Fondo Censantias
    private VigenciasAfiliaciones nuevaVigenciaAfiliacionFondo;
    private int idAfiliacionFondo;
    private boolean permitirIndexAfiliacionFondo;
    private String auxAfiliacionFondo;
    //ARP
    private VigenciasAfiliaciones nuevaVigenciaAfiliacionARP;
    private int idAfiliacionARP;
    private boolean permitirIndexAfiliacionARP;
    private String auxAfiliacionARP;
    //Caja
    private VigenciasAfiliaciones nuevaVigenciaAfiliacionCaja;
    private int idAfiliacionCaja;
    private boolean permitirIndexAfiliacionCaja;
    private String auxAfiliacionCaja;
    //Modulo Estado Civil
    private VigenciasEstadosCiviles nuevoEstadoCivil;
    private int idEstadoCivil;
    private boolean permitirIndexEstadoCivil;
    private String auxEstadoCivilEstado;
    //Modulo Direcciones
    private Direcciones nuevaDireccion;
    private int idDireccion;
    private boolean permitirIndexDireccion;
    private String auxDireccionCiudad;
    //Modulo Telefonos
    private Telefonos nuevoTelefono;
    private int idTelefono;
    private boolean permitirIndexTelefono;
    private String auxTelefonoTipo, auxTelefonoCiudad;
    //LOVS
    private List<TiposTelefonos> lovTiposTelefonos;
    private List<TiposTelefonos> filtrarLovTiposTelefonos;
    private TiposTelefonos tipoTelefonoSeleccionado;
    private List<EstadosCiviles> lovEstadosCiviles;
    private List<EstadosCiviles> filtrarLovEstadosCiviles;
    private EstadosCiviles estadoCivilSeleccionado;
    private List<TercerosSucursales> lovTercerosSucursales;
    private List<TercerosSucursales> filtrarLovTercerosSucursales;
    private TercerosSucursales terceroSucursalSeleccionado;
    private List<JornadasLaborales> lovJornadasLaborales;
    private List<JornadasLaborales> filtrarLovJornadasLaborales;
    private JornadasLaborales jornadaLaboralSeleccionada;
    private List<Periodicidades> lovPeriodicidades;
    private List<Periodicidades> filtrarLovPeriodicidades;
    private Periodicidades periodicidadSeleccionada;
    private List<Sucursales> lovSucursales;
    private List<Sucursales> filtrarLovSucursales;
    private Sucursales sucursalSeleccionada;
    private List<MetodosPagos> lovMetodosPagos;
    private List<MetodosPagos> filtrarLovMetodosPagos;
    private MetodosPagos metodoPagoSeleccionado;
    private List<UbicacionesGeograficas> lovUbicacionesGeograficas;
    private List<UbicacionesGeograficas> filtrarLovUbicacionesGeograficas;
    private UbicacionesGeograficas ubicacionGeograficaSeleccionada;
    private List<Contratos> lovContratos;
    private List<Contratos> filtrarLovContratos;
    private Contratos contratoSeleccionado;
    private List<NormasLaborales> lovNormasLaborales;
    private List<NormasLaborales> filtrarLovNormasLaborales;
    private NormasLaborales normaLaboralSeleccionada;
    private List<MotivosContratos> lovMotivosContratos;
    private List<MotivosContratos> filtrarLovMotivosContratos;
    private MotivosContratos motivoContratoSeleccionado;
    private List<TiposContratos> lovTiposContratos;
    private List<TiposContratos> filtrarLovTiposContratos;
    private TiposContratos tipoContratoSeleccionado;
    private List<MotivosCambiosSueldos> lovMotivosCambiosSueldos;
    private List<MotivosCambiosSueldos> filtrarLovMotivosCambiosSueldos;
    private MotivosCambiosSueldos motivoCambioSueldoSeleccionado;
    private List<TiposSueldos> lovTiposSueldos;
    private List<TiposSueldos> filtrarLovTiposSueldos;
    private TiposSueldos tipoSueldoSeleccionado;
    private List<Unidades> lovUnidades;
    private List<Unidades> filtrarLovUnidades;
    private Unidades unidadSeleccionada;
    private List<ReformasLaborales> lovReformasLaborales;
    private List<ReformasLaborales> filtrarLovReformasLaborales;
    private ReformasLaborales reformaLaboralSeleccionada;
    private List<TiposTrabajadores> lovTiposTrabajadores;
    private List<TiposTrabajadores> filtrarLovTiposTrabajadores;
    private TiposTrabajadores tipoTrabajadorSeleccionado;
    private List<Empresas> lovEmpresas;
    private List<Empresas> filtrarLovEmpresas;
    private Empresas empresaSeleccionada;
    private List<TiposDocumentos> lovTiposDocumentos;
    private List<TiposDocumentos> filtrarLovTiposDocumentos;
    private TiposDocumentos tipoDocumentoSeleccionado;
    private List<Ciudades> lovCiudades;
    private List<Ciudades> filtrarLovCiudades;
    private Ciudades ciudadSeleccionada;
    private List<Cargos> lovCargos;
    private List<Cargos> filtrarLovCargos;
    private Cargos cargoSeleccionado;
    private List<MotivosCambiosCargos> lovMotivosCargos;
    private List<MotivosCambiosCargos> filtrarLovMotivosCambiosCargos;
    private MotivosCambiosCargos motivoCambioCargoSeleccionado;
    private List<Estructuras> lovEstructuras;
    private List<Estructuras> filtrarLovEstructuras;
    private Estructuras estructuraSeleccionada;
    private List<Papeles> lovPapeles;
    private List<Papeles> filtrarLovPapeles;
    private Papeles papelSeleccionado;
    private List<Empleados> lovEmpleados;
    private List<Empleados> filtrarLovEmpleados;
    private Empleados empleadoSeleccionado;
    private List<MotivosLocalizaciones> lovMotivosLocalizaciones;
    private List<MotivosLocalizaciones> filtrarLovMotivosCC;
    private MotivosLocalizaciones motivoLocalizacionSeleccionado;
    private List<Estructuras> lovEstructurasCentroCosto;
    private List<Estructuras> filtrarLovEstructurasCentroCosto;
    private Estructuras estructuraCentroCostoSeleccionada;
    //Otros
    private SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
    private Date fechaDesdeParametro, fechaHastaParametro;
    private ParametrosEstructuras parametrosEstructuras;
    private Date fechaParametro;
    private boolean disableNombreEstructuraCargo;
    private boolean disableDescripcionEstructura;
    private boolean disableUbicacionGeografica;
    private boolean disableAfiliaciones;
    private boolean disableCamposDependientesTipoT;
    private boolean aceptar;
    private BigInteger l;
    private int k;
    //Info Registros Lovs
    private String infoRegistroEmpresaInfoPersonal;
    private String infoRegistroTipoDocInfoPersonal;
    private String infoRegistroCiudadDocInfoPersonal;
    private String infoRegistroCiudadNacInfoPersonal;
    private String infoRegistroCargoDesempenado;
    private String infoRegistroMotivoCargoDesempenado;
    private String infoRegistroEstructuraCargoDesempenado;
    private String infoRegistroPapelCargoDesempenado;
    private String infoRegistroJefeCargoDesempenado;
    private String infoRegistroMotivoCentroCosto;
    private String infoRegistroEstructuraCentroCosto;
    private String infoRegistroTipoTrabajadorTT;
    private String infoRegistroReformaTipoSalario;
    private String infoRegistroMotivoSueldo;
    private String infoRegistroTipoSueldoS;
    private String infoRegistroUnidadSueldo;
    private String infoRegistroMotivoTipoContrato;
    private String infoRegistroTipoContrato;
    private String infoRegistroNormaLaboral;
    private String infoRegistroContratoLegislacionL;
    private String infoRegistroUbicacionUb;
    private String infoRegistroJornadaLaboral;
    private String infoRegistroFormaPago;
    private String infoRegistroSucursalFormaPago;
    private String infoRegistroMetodoFormaPago;
    private String infoRegistroTerceroAfiliacion;
    private String infoRegistroEstadoCivil;
    private String infoRegistroCiudadDireccion;
    private String infoRegistroCiudadTelefono;
    private String infoRegistroTipoTelTelefono;
    //
    private String mensajeErrorFechasEmpleado;
    //
    private static final String PATTERN_EMAIL = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    //
    private int desdeNominaFEmpresa;
    private boolean errorDesdeNominaF;
    //unidad PESOS
    private Unidades unidadPesos;
    private String tituloTercero;
    //valiudacion Tipo Trabajador
    private String errorTT;

    //Strings para mostrar la informaciÃ³n sin el objeto principal
    private String txt_tipoDoc, txt_ciudadDoc, txt_ciudadNac, txt_cargo, txt_motivoCargo, txt_papel, txt_empleado,
            txt_motivoCentroC, txt_tipoT, txt_tipoRL, txt_motivoSu, txt_TipoSu, txt_motivoContrato, txt_tipoContrato,
            txt_ubicacionG, txt_jornada, txt_formaP, txt_metodoP, txt_terceroEPS, txt_terceroARL, txt_terceroCes,
            txt_terceroAFP, txt_terceroCC, txt_estadoC, txt_ciudadDir, txt_ciudadTel, txt_tipoTel;

    private boolean permitirDesplegarLista;

    public ControlPersonaIndividual() {
        nuevoEmpleado = new Empleados();
        //nuevoEmpleado.setEmpresa(new Empresas());
        nuevaPersona = new Personas();
        nuevaPersona.setCiudaddocumento(new Ciudades());
        nuevaPersona.setCiudadnacimiento(new Ciudades());
        nuevaPersona.setTipodocumento(new TiposDocumentos());
        nuevaVigenciaCargo = new VigenciasCargos();
        nuevaVigenciaCargo.setPapel(new Papeles());
        nuevaVigenciaCargo.setCargo(new Cargos());
        nuevaVigenciaCargo.setEstructura(new Estructuras());
        nuevaVigenciaCargo.setMotivocambiocargo(new MotivosCambiosCargos());
        nuevaVigenciaCargo.setEmpleadojefe(new Empleados());
        nuevaVigenciaCargo.getEmpleadojefe().setPersona(new Personas());
        nuevaVigenciaLocalizacion = new VigenciasLocalizaciones();
        nuevaVigenciaLocalizacion.setLocalizacion(new Estructuras());
        nuevaVigenciaLocalizacion.getLocalizacion().setCentrocosto(new CentrosCostos());
        nuevaVigenciaLocalizacion.setMotivo(new MotivosLocalizaciones());
        nuevaVigenciaTipoTrabajador = new VigenciasTiposTrabajadores();
        nuevaVigenciaTipoTrabajador.setTipotrabajador(new TiposTrabajadores());
        nuevaVigenciaReformaLaboral = new VigenciasReformasLaborales();
        nuevaVigenciaReformaLaboral.setReformalaboral(new ReformasLaborales());
        nuevaVigenciaSueldo = new VigenciasSueldos();
        nuevaVigenciaSueldo.setUnidadpago(new Unidades());
        nuevaVigenciaSueldo.setTiposueldo(new TiposSueldos());
        nuevaVigenciaSueldo.setMotivocambiosueldo(new MotivosCambiosSueldos());
        nuevaVigenciaTipoContrato = new VigenciasTiposContratos();
        nuevaVigenciaTipoContrato.setTipocontrato(new TiposContratos());
        nuevaVigenciaTipoContrato.setMotivocontrato(new MotivosContratos());
        nuevaVigenciaNormaEmpleado = new VigenciasNormasEmpleados();
        nuevaVigenciaNormaEmpleado.setNormalaboral(new NormasLaborales());
        nuevaVigenciaContrato = new VigenciasContratos();
        nuevaVigenciaContrato.setContrato(new Contratos());
        nuevaVigenciaUbicacion = new VigenciasUbicaciones();
        nuevaVigenciaUbicacion.setUbicacion(new UbicacionesGeograficas());
        nuevaVigenciaFormaPago = new VigenciasFormasPagos();
        nuevaVigenciaFormaPago.setSucursal(new Sucursales());
        nuevaVigenciaFormaPago.setMetodopago(new MetodosPagos());
        nuevaVigenciaFormaPago.setFormapago(new Periodicidades());
        nuevaVigenciaJornada = new VigenciasJornadas();
        nuevaVigenciaJornada.setJornadatrabajo(new JornadasLaborales());
        nuevaVigenciaAfiliacionAFP = new VigenciasAfiliaciones();
        nuevaVigenciaAfiliacionAFP.setTercerosucursal(new TercerosSucursales());
        nuevaVigenciaAfiliacionARP = new VigenciasAfiliaciones();
        nuevaVigenciaAfiliacionARP.setTercerosucursal(new TercerosSucursales());
        nuevaVigenciaAfiliacionCaja = new VigenciasAfiliaciones();
        nuevaVigenciaAfiliacionCaja.setTercerosucursal(new TercerosSucursales());
        nuevaVigenciaAfiliacionEPS = new VigenciasAfiliaciones();
        nuevaVigenciaAfiliacionEPS.setTercerosucursal(new TercerosSucursales());
        nuevaVigenciaAfiliacionFondo = new VigenciasAfiliaciones();
        nuevaVigenciaAfiliacionFondo.setTercerosucursal(new TercerosSucursales());
        nuevoEstadoCivil = new VigenciasEstadosCiviles();
        nuevoEstadoCivil.setEstadocivil(new EstadosCiviles());
        nuevaDireccion = new Direcciones();
        nuevaDireccion.setCiudad(new Ciudades());
        nuevoTelefono = new Telefonos();
        nuevoTelefono.setCiudad(new Ciudades());
        nuevoTelefono.setTipotelefono(new TiposTelefonos());
        //Lovs
        lovTiposTelefonos = null;
        tipoTelefonoSeleccionado = new TiposTelefonos();
        lovEstadosCiviles = null;
        estadoCivilSeleccionado = new EstadosCiviles();
        lovTercerosSucursales = null;
        terceroSucursalSeleccionado = new TercerosSucursales();
        lovJornadasLaborales = null;
        jornadaLaboralSeleccionada = new JornadasLaborales();
        lovPeriodicidades = null;
        periodicidadSeleccionada = new Periodicidades();
        lovSucursales = null;
        sucursalSeleccionada = new Sucursales();
        lovMetodosPagos = null;
        metodoPagoSeleccionado = new MetodosPagos();
        lovUbicacionesGeograficas = null;
        ubicacionGeograficaSeleccionada = new UbicacionesGeograficas();
        lovContratos = null;
        contratoSeleccionado = new Contratos();
        lovNormasLaborales = null;
        normaLaboralSeleccionada = new NormasLaborales();
        lovMotivosContratos = null;
        motivoContratoSeleccionado = new MotivosContratos();
        lovTiposContratos = null;
        tipoContratoSeleccionado = new TiposContratos();
        lovMotivosCambiosSueldos = null;
        motivoCambioSueldoSeleccionado = new MotivosCambiosSueldos();
        lovTiposSueldos = null;
        tipoSueldoSeleccionado = new TiposSueldos();
        lovUnidades = null;
        unidadSeleccionada = new Unidades();
        lovReformasLaborales = null;
        reformaLaboralSeleccionada = new ReformasLaborales();
        lovTiposTrabajadores = null;
        tipoTrabajadorSeleccionado = new TiposTrabajadores();
        lovPapeles = null;
        papelSeleccionado = new Papeles();
        lovEstructuras = null;
        estructuraSeleccionada = new Estructuras();
        lovMotivosCargos = null;
        motivoCambioCargoSeleccionado = new MotivosCambiosCargos();
        lovCargos = null;
        cargoSeleccionado = new Cargos();
        lovEmpresas = null;
        empresaSeleccionada = null;
        lovTiposDocumentos = null;
        tipoDocumentoSeleccionado = new TiposDocumentos();
        lovCiudades = null;
        ciudadSeleccionada = new Ciudades();
        lovMotivosLocalizaciones = null;
        motivoLocalizacionSeleccionado = new MotivosLocalizaciones();
        lovEstructurasCentroCosto = null;
        estructuraCentroCostoSeleccionada = new Estructuras();
        lovEmpleados = null;
        empleadoSeleccionado = new Empleados();
        //Index
        idInformacionPersonal = -1;
        idCargoDesempeñado = -1;
        idCentroCosto = -1;
        idTipoTrabajador = -1;
        idTipoSalario = -1;
        idSueldo = -1;
        idTipoContrato = -1;
        idNormaLaboral = -1;
        idLegislacionLaboral = -1;
        idUbicacionGeografica = -1;
        idFormaPago = -1;
        idJornadaLaboral = -1;
        idAfiliacionAFP = -1;
        idAfiliacionARP = -1;
        idAfiliacionCaja = -1;
        idAfiliacionEPS = -1;
        idAfiliacionFondo = -1;
        idEstadoCivil = -1;
        idDireccion = -1;
        idTelefono = -1;
        //Auxiliares
        auxFechaNacimiento = null;
        auxFechaIngreso = null;
        auxFechaCorte = null;
        auxTipoContratoFecha = null;
        valorSueldo = new BigDecimal("0");
        auxSueldoValor = new BigDecimal("0");
        //Otros
        k = 0;
        aceptar = true;
        disableNombreEstructuraCargo = true;
        System.out.println("disableNombreEstructuraCargo es true");
        disableDescripcionEstructura = true;
        disableUbicacionGeografica = true;
        disableAfiliaciones = true;
        disableCamposDependientesTipoT = true;
        mensajeErrorFechasEmpleado = "";
        //Permitir Index
        permitirIndexInformacionPersonal = true;
        permitirIndexCentroCosto = true;
        permitirIndexCargoDesempeñado = true;
        permitirIndexTipoTrabajador = true;
        permitirIndexTipoSalario = true;
        permitirIndexSueldo = true;
        permitirIndexTipoContrato = true;
        permitirIndexNormaLaboral = true;
        permitirIndexLegislacionLaboral = true;
        permitirIndexUbicacionGeografica = true;
        permitirIndexFormaPago = true;
        permitirIndexJornadaLaboral = true;
        permitirIndexAfiliacionAFP = true;
        permitirIndexAfiliacionARP = true;
        permitirIndexAfiliacionCaja = true;
        permitirIndexAfiliacionEPS = true;
        permitirIndexAfiliacionFondo = true;
        permitirIndexEstadoCivil = true;
        permitirIndexDireccion = true;
        permitirIndexTelefono = true;
        //
        unidadPesos = new Unidades();
        unidadPesos.setNombre("");
        desdeNominaFEmpresa = 0;
        errorDesdeNominaF = false;
        //
        errorTT = " ";
        permitirDesplegarLista = true;
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarPersonaIndividual.obtenerConexion(ses.getId());
            administrarCarpetaPersonal.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
        cargarLovEmpresas();
        getLovUnidades();
        modificarInfoR_EmpresaInfoP(lovEmpresas.size());
    }

    public void procesoDialogoEmpresa() {
        cargarLovEmpresas();
        modificarInfoR_EmpresaInfoP(lovEmpresas.size());
        PrimefacesContextUI.actualizar("formLovs:formDInformacionPersonal:formEmpresa:infoRegistroEmpresaInformacionPersonal");
        PrimefacesContextUI.actualizar("formLovs:formDInformacionPersonal:formEmpresa:infoRegistroEmpresaInformacionPersonalV");
        System.out.println("entro en la funcion procesoDialogoEmpresa()");
        RequestContext context = RequestContext.getCurrentInstance();
        PrimefacesContextUI.actualizar("form:formEmpresa:EmpresaInformacionPersonalDialogo");
        PrimefacesContextUI.ejecutar("PF('EmpresaInformacionPersonalDialogo').show()");
    }

    public void listaValoresInformacionPersonal() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (idInformacionPersonal == 0) {
            cargarLovEmpresas();
            modificarInfoR_EmpresaInfoP(lovEmpresas.size());
            PrimefacesContextUI.actualizar("formLovs:formDInformacionPersonal:formEmpresa:EmpresaInformacionPersonalDialogo");
            PrimefacesContextUI.ejecutar("PF('EmpresaInformacionPersonalDialogo').show()");
            idInformacionPersonal = -1;
        }
        if (idInformacionPersonal == 5) {
            cargarLovTiposDocumentos();
            PrimefacesContextUI.actualizar("formLovs:formDInformacionPersonal:infoP_tipoD:TipoDocumentoInformacionPersonalDialogo");
            modificarInfoR_TipoDocInfoP(lovTiposDocumentos.size());
            PrimefacesContextUI.ejecutar("PF('TipoDocumentoInformacionPersonalDialogo').show()");
            idInformacionPersonal = -1;
        }
        if (idInformacionPersonal == 7) {
            cargarLovCiudades();
            PrimefacesContextUI.actualizar("formLovs:formDInformacionPersonal:infoP_ciudadD:CiudadDocumentoInformacionPersonalDialogo");
            modificarInfoR_CiudadDocInfoP(lovCiudades.size());
            PrimefacesContextUI.ejecutar("PF('CiudadDocumentoInformacionPersonalDialogo').show()");
            idInformacionPersonal = -1;
        }
        if (idInformacionPersonal == 9) {
            cargarLovCiudades();
            PrimefacesContextUI.actualizar("formLovs:formDInformacionPersonal:infoP_ciudadN:CiudadNacimientoInformacionPersonalDialogo");
            modificarInfoR_CiudadNacInfoP(lovCiudades.size());
            PrimefacesContextUI.ejecutar("PF('CiudadNacimientoInformacionPersonalDialogo').show()");
            idInformacionPersonal = -1;
        }
    }

    public void listaValoresCargoDesempenado() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (idCargoDesempeñado == 0) {
            cargarLovCargos();
            PrimefacesContextUI.actualizar("formLovs:formDCargoDesempenado:formCargo:CargoCargoDesempeñadoDialogo");
            modificarInfoR_CargoD(lovCargos.size());
            PrimefacesContextUI.actualizar("formLovs:formDCargoDesempenado:formCargo:infoRegistroCargoCargoDesempenado");
            PrimefacesContextUI.ejecutar("PF('CargoCargoDesempeñadoDialogo').show()");
            idCargoDesempeñado = -1;
        }
        if (idCargoDesempeñado == 1) {
            cargarLovMotivosCargos();
            PrimefacesContextUI.actualizar("formLovs:formDCargoDesempenado:formMotcargo:MotivoCambioCargoCargoDesempeñadoDialogo");
            modificarInfoR_MotivoCargoD(lovMotivosCargos.size());
            PrimefacesContextUI.ejecutar("PF('MotivoCambioCargoCargoDesempeñadoDialogo').show()");
            idCargoDesempeñado = -1;
        }
        if (idCargoDesempeñado == 2) {
            cargarLovEstructuras();
            PrimefacesContextUI.actualizar("formLovs:formDCargoDesempenado:formEstruCargo:EstructuraCargoDesempeñadoDialogo");
            modificarInfoR_EstrCargoD(lovEstructuras.size());
            PrimefacesContextUI.ejecutar("PF('EstructuraCargoDesempeñadoDialogo').show()");
            idCargoDesempeñado = -1;
        }
        if (idCargoDesempeñado == 3) {
            cargarLovPapeles();
            PrimefacesContextUI.actualizar("formLovs:formDCargoDesempenado:formPapelD:PapelCargoDesempeñadoDialogo");
            modificarInfoR_PapelCargoD(lovPapeles.size());
            PrimefacesContextUI.ejecutar("PF('PapelCargoDesempeñadoDialogo').show()");
            idCargoDesempeñado = -1;
        }
        if (idCargoDesempeñado == 4) {
            cargarLovEmpleados();
            PrimefacesContextUI.actualizar("formLovs:formDCargoDesempenado:formjefe:EmpleadoJefeCargoDesempeñadoDialogo");
            modificarInfoR_JefeCargoD(lovEmpleados.size());
            PrimefacesContextUI.ejecutar("PF('EmpleadoJefeCargoDesempeñadoDialogo').show()");
            idCargoDesempeñado = -1;
        }
    }

    public void listaValoresCentroCosto() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (idCentroCosto == 0) {
            cargarLovMotivosLocalizaciones();
            PrimefacesContextUI.actualizar("formLovs:formDCentroCosto:formCentroC:MotivoLocalizacionCentroCostoDialogo");
            modificarInfoR_MotivoCentroC(lovMotivosLocalizaciones.size());
            PrimefacesContextUI.ejecutar("PF('MotivoLocalizacionCentroCostoDialogo').show()");
            idCentroCosto = -1;
        }
        if (idCentroCosto == 1) {
            cargarLovEstructurasCentroCosto();
            PrimefacesContextUI.actualizar("formLovs:formDCentroCosto:formEstrucCC:EstructuraCentroCostoDialogo");
            modificarInfoR_EstrCentroC(lovEstructurasCentroCosto.size());
            PrimefacesContextUI.ejecutar("PF('EstructuraCentroCostoDialogo').show()");
            idCentroCosto = -1;
        }
    }

    public void listaValoresTipoTrabajador() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (idTipoTrabajador == 0) {
            cargarLovTiposTrabajadores();
            PrimefacesContextUI.actualizar("formLovs:formDTipoTrabajador:TipoTrabajadorTipoTrabajadorDialogo");
            modificarInfoR_TipoTraTT(lovTiposTrabajadores.size());
            PrimefacesContextUI.ejecutar("PF('TipoTrabajadorTipoTrabajadorDialogo').show()");
            idTipoTrabajador = -1;
        }
    }

    public void listaValoresTipoSalario() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (idTipoSalario == 0) {
            modificarInfoR_ReformaTipoSa(lovReformasLaborales.size());
            PrimefacesContextUI.ejecutar("PF('ReformaLaboralTipoSalarioDialogo').show()");
            PrimefacesContextUI.actualizar("formLovs:formDTipoSalario:lovReformaLaboralTipoSalario");
            PrimefacesContextUI.actualizar("formLovs:formDTipoSalario:ReformaLaboralTipoSalarioDialogo");
            idTipoSalario = -1;
        }
    }

    public void listaValoresSueldo() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (idSueldo == 1) {
            cargarLovMotivosCambiosSueldos();
            modificarInfoR_MotivoSu(lovMotivosCambiosSueldos.size());
            PrimefacesContextUI.ejecutar("PF('MotivoCambioSueldoSueldoDialogo').show()");
            PrimefacesContextUI.actualizar("formLovs:formDSueldo:formMotivoSu:MotivoCambioSueldoSueldoDialogo");
            idSueldo = -1;
        }
        if (idSueldo == 2) {
            if (lovTiposSueldos == null) {
                PrimefacesContextUI.ejecutar("PF('advertenciaSeleccionTipoT').show()");
            } else {
                modificarInfoR_TipoSuSu(lovTiposSueldos.size());
                PrimefacesContextUI.ejecutar("PF('TipoSueldoSueldoDialogo').show()");
                PrimefacesContextUI.actualizar("formLovs:formDSueldo:formTipoSueldo:TipoSueldoSueldoDialogo");
                PrimefacesContextUI.actualizar("formLovs:formDSueldo:formTipoSueldo:lovTipoSueldoSueldo");
                idSueldo = -1;
            }
        }
        if (idSueldo == 3) {
            PrimefacesContextUI.actualizar("formLovs:formDSueldo:formUnidadS:UnidadSueldoDialogo");
            modificarInfoR_UnidadSu(lovUnidades.size());
            PrimefacesContextUI.ejecutar("PF('UnidadSueldoDialogo').show()");
            idSueldo = -1;
        }
    }

    public void listaValoresTipoContrato() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (idTipoContrato == 0) {
            cargarLovMotivosContratos();
            modificarInfoR_MotivoTipoCo(lovMotivosContratos.size());
            PrimefacesContextUI.ejecutar("PF('MotivoContratoTipoContratoDialogo').show()");
            PrimefacesContextUI.actualizar("formLovs:formDTipoContrato:formTipoContrat:MotivoContratoTipoContratoDialogo");
            PrimefacesContextUI.actualizar("formLovs:formDTipoContrato:formTipoContrat:lovMotivoContratoTipoContrato");
            idTipoContrato = -1;
        }
        if (idTipoContrato == 1) {
            if (lovTiposContratos == null) {
                PrimefacesContextUI.ejecutar("PF('advertenciaSeleccionTipoT').show()");
            } else {
                modificarInfoR_TipoContrato(lovTiposContratos.size());
                PrimefacesContextUI.ejecutar("PF('TipoContratoTipoContratoDialogo').show()");
                PrimefacesContextUI.actualizar("formLovs:formDTipoContrato:formTipoContrato:TipoContratoTipoContratoDialogo");
                PrimefacesContextUI.actualizar("formLovs:formDTipoContrato:formTipoContrato:lovTipoContratoTipoContrato");
                idTipoContrato = -1;
            }
        }
    }

    public void listaValoresNormaLaboral() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (idNormaLaboral == 0) {
            if (lovNormasLaborales == null) {
                PrimefacesContextUI.ejecutar("PF('advertenciaSeleccionTipoT').show()");
            } else {
                modificarInfoR_ReformaTipoSa(lovNormasLaborales.size());
                PrimefacesContextUI.ejecutar("PF('NormaLaboralNormaLaboralDialogo').show()");
                PrimefacesContextUI.actualizar("formLovs:formDNormaLaboral:NormaLaboralNormaLaboralDialogo");
                PrimefacesContextUI.actualizar("formLovs:formDNormaLaboral:lovNormaLaboralNormaLaboral");
                idNormaLaboral = -1;
            }
        }
    }

    public void listaValoresLegislacionLaboral() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (idLegislacionLaboral == 0) {
            if (lovContratos == null) {
                PrimefacesContextUI.ejecutar("PF('advertenciaSeleccionTipoT').show()");
            } else {
                modificarInfoR_ContratoLL(lovContratos.size());
                PrimefacesContextUI.ejecutar("PF('ContratoLegislacionLaboralDialogo').show()");
                PrimefacesContextUI.actualizar("formLovs:formDLegislacionLaboral:ContratoLegislacionLaboralDialogo");
                PrimefacesContextUI.actualizar("formLovs:formDLegislacionLaboral:lovContratoLegislacionLaboral");
                idLegislacionLaboral = -1;
            }
        }
    }

    public void listaValoresUbicacion() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (idUbicacionGeografica == 0) {
            cargarLovUbicacionesGeograficas();
            modificarInfoR_UbicacionUb(lovUbicacionesGeograficas.size());
            PrimefacesContextUI.actualizar("formLovs:formDUbicacion:infoRegistroUbicacionUbicacion");
            PrimefacesContextUI.ejecutar("PF('UbicacionUbicacionGeograficaDialogo').show()");
            PrimefacesContextUI.actualizar("formLovs:formDUbicacion:UbicacionUbicacionGeograficaDialogo");
            idUbicacionGeografica = -1;
        }
    }

    public void listaValoresJornadaLaboral() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (idJornadaLaboral == 0) {
            cargarLovJornadasLaborales();
            modificarInfoR_JornadaL(lovJornadasLaborales.size());
            PrimefacesContextUI.ejecutar("PF('JornadaJornadaLaboralDialogo').show()");
            PrimefacesContextUI.actualizar("formLovs:formDJornadaLaboral:JornadaJornadaLaboralDialogo");
            idJornadaLaboral = -1;
        }
    }

    public void listaValoresFormaPago() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (idFormaPago == 0) {
            cargarLovPeriodicidades();
            PrimefacesContextUI.actualizar("formLovs:formDFormaPago:formPeriodicidad:PeriodicidadFormaPagoDialogo");
            modificarInfoR_PeriodFormaPago(lovPeriodicidades.size());
            PrimefacesContextUI.ejecutar("PF('PeriodicidadFormaPagoDialogo').show()");
            idFormaPago = -1;
        }
        if (idFormaPago == 3) {
            cargarLovSucursales();
            PrimefacesContextUI.actualizar("formLovs:formDFormaPago:formSucursal:SucursalFormaPagoDialogo");
            modificarInfoR_SucursalFormaP(lovSucursales.size());
            PrimefacesContextUI.ejecutar("PF('SucursalFormaPagoDialogo').show()");
            idFormaPago = -1;
        }
        if (idFormaPago == 4) {
            cargarLovMetodosPagos();
            PrimefacesContextUI.actualizar("formLovs:formDFormaPago:formMetodo:MetodoPagoFormaPagoDialogo");
            modificarInfoR_EmpresaInfoP(lovMetodosPagos.size());
            PrimefacesContextUI.ejecutar("PF('MetodoPagoFormaPagoDialogo').show()");
            idFormaPago = -1;
        }
    }

    public void listaValoresAfiliacionEPS() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (idAfiliacionEPS == 0) {
            cargarLovTercerosSucursales();
            tituloTercero = "E.P.S";
            PrimefacesContextUI.actualizar("formLovs:formDAfiliacion:TerceroAfiliacionDialogo");
            modificarInfoR_TerceroAfSuc(lovTercerosSucursales.size());
            PrimefacesContextUI.ejecutar("PF('TerceroAfiliacionDialogo').show()");
        }
    }

    public void listaValoresAfiliacionARP() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (idAfiliacionARP == 0) {
            cargarLovTercerosSucursales();
            tituloTercero = "A.R.L";
            PrimefacesContextUI.actualizar("formLovs:formDAfiliacion:TerceroAfiliacionDialogo");
            modificarInfoR_TerceroAfSuc(lovTercerosSucursales.size());
            PrimefacesContextUI.ejecutar("PF('TerceroAfiliacionDialogo').show()");
        }
    }

    public void listaValoresAfiliacionAFP() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (idAfiliacionAFP == 0) {
            cargarLovTercerosSucursales();
            tituloTercero = "A.F.P";
            PrimefacesContextUI.actualizar("formLovs:formDAfiliacion:TerceroAfiliacionDialogo");
            modificarInfoR_TerceroAfSuc(lovTercerosSucursales.size());
            PrimefacesContextUI.ejecutar("PF('TerceroAfiliacionDialogo').show()");
        }
    }

    public void listaValoresAfiliacionCaja() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (idAfiliacionCaja == 0) {
            cargarLovTercerosSucursales();
            tituloTercero = "C.C.F";
            PrimefacesContextUI.actualizar("formLovs:formDAfiliacion:TerceroAfiliacionDialogo");
            modificarInfoR_TerceroAfSuc(lovTercerosSucursales.size());
            PrimefacesContextUI.ejecutar("PF('TerceroAfiliacionDialogo').show()");
        }
    }

    public void listaValoresAfiliacionFondo() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (idAfiliacionFondo == 0) {
            cargarLovTercerosSucursales();
            tituloTercero = "Fondo de Cesantías";
            PrimefacesContextUI.actualizar("formLovs:formDAfiliacion:TerceroAfiliacionDialogo");
            modificarInfoR_TerceroAfSuc(lovTercerosSucursales.size());
            PrimefacesContextUI.ejecutar("PF('TerceroAfiliacionDialogo').show()");
        }
    }

    public void listaValoresEstadoCivil() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (idEstadoCivil == 0) {
            cargarLovTercerosSucursales();
            cargarLovEstadosCiviles();
            System.out.println("listaValoresEstadoCivil() lovEstadosCiviles : " + lovEstadosCiviles);
            PrimefacesContextUI.actualizar("formLovs:formDEstadoCivil:EstadoCivilEstadoCivilDialogo");
            if (lovEstadosCiviles != null) {
                modificarInfoR_EstadoCivil(lovEstadosCiviles.size());
            }
            PrimefacesContextUI.ejecutar("PF('EstadoCivilEstadoCivilDialogo').show()");
            idEstadoCivil = -1;
        }
    }

    public void listaValoresDireccion() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (idDireccion == 1) {
            cargarLovTercerosSucursales();
            cargarLovCiudades();
            PrimefacesContextUI.actualizar("formLovs:formDDireccion:CiudadDireccionDialogo");
            modificarInfoR_CiudadDir(lovCiudades.size());
            PrimefacesContextUI.ejecutar("PF('CiudadDireccionDialogo').show()");
            idDireccion = -1;
        }
    }

    public void listaValoresTelefono() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (idTelefono == 0) {
            cargarLovTiposTelefonos();
            PrimefacesContextUI.actualizar("formLovs:formDTelefono:formTel:TipoTelefonoTelefonoDialogo");
            modificarInfoR_TipoTelT(lovTiposTelefonos.size());
            PrimefacesContextUI.ejecutar("PF('TipoTelefonoTelefonoDialogo').show()");
            idTelefono = -1;
        }
        if (idTelefono == 1) {
            cargarLovCiudades();
            PrimefacesContextUI.actualizar("formLovs:formDTelefono:formCiudadTel:CiudadTelefonoDialogo");
            modificarInfoR_CiudadTel(lovCiudades.size());
            PrimefacesContextUI.ejecutar("PF('CiudadTelefonoDialogo').show()");
            idTelefono = -1;
        }
    }

    public void botonListaValores() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (idInformacionPersonal >= 0) {
            if (idInformacionPersonal == 0) {
                cargarLovEmpresas();
                PrimefacesContextUI.actualizar("formLovs:formDInformacionPersonal:formEmpresa:EmpresaInformacionPersonalDialogo");
                modificarInfoR_EmpresaInfoP(lovEmpresas.size());
                PrimefacesContextUI.ejecutar("PF('EmpresaInformacionPersonalDialogo').show()");
                idInformacionPersonal = -1;
            }
            if (idInformacionPersonal == 5) {
                cargarLovTiposDocumentos();
                PrimefacesContextUI.actualizar("formLovs:formDInformacionPersonal:infoP_tipoD:TipoDocumentoInformacionPersonalDialogo");
                modificarInfoR_TipoDocInfoP(lovTiposDocumentos.size());
                PrimefacesContextUI.ejecutar("PF('TipoDocumentoInformacionPersonalDialogo').show()");
                idInformacionPersonal = -1;
            }
            if (idInformacionPersonal == 7) {
                cargarLovCiudades();
                PrimefacesContextUI.actualizar("formLovs:formDInformacionPersonal:infoP_ciudadD:CiudadDocumentoInformacionPersonalDialogo");
                modificarInfoR_CiudadDocInfoP(lovCiudades.size());
                PrimefacesContextUI.ejecutar("PF('CiudadDocumentoInformacionPersonalDialogo').show()");
                idInformacionPersonal = -1;
            }
            if (idInformacionPersonal == 9) {
                cargarLovCiudades();
                PrimefacesContextUI.actualizar("formLovs:formDInformacionPersonal:infoP_ciudadN:CiudadNacimientoInformacionPersonalDialogo");
                modificarInfoR_CiudadNacInfoP(lovCiudades.size());
                PrimefacesContextUI.ejecutar("PF('CiudadNacimientoInformacionPersonalDialogo').show()");
                idInformacionPersonal = -1;
            }
        } else if (idCargoDesempeñado >= 0) {
            if (idCargoDesempeñado == 0) {
                cargarLovCargos();
                PrimefacesContextUI.actualizar("formLovs:formDCargoDesempenado:formCargo:CargoCargoDesempeñadoDialogo");
                modificarInfoR_CargoD(lovCargos.size());
                PrimefacesContextUI.ejecutar("PF('CargoCargoDesempeñadoDialogo').show()");
                idCargoDesempeñado = -1;
            }
            if (idCargoDesempeñado == 1) {
                cargarLovMotivosCargos();
                PrimefacesContextUI.actualizar("formLovs:formDCargoDesempenado:formMotcargo:MotivoCambioCargoCargoDesempeñadoDialogo");
                modificarInfoR_MotivoCargoD(lovMotivosCargos.size());
                PrimefacesContextUI.ejecutar("PF('MotivoCambioCargoCargoDesempeñadoDialogo').show()");
                idCargoDesempeñado = -1;
            }
            if (idCargoDesempeñado == 2) {
                cargarLovEstructuras();
                PrimefacesContextUI.actualizar("formLovs:formDCargoDesempenado:formEstruCargo:EstructuraCargoDesempeñadoDialogo");
                modificarInfoR_EstrCargoD(lovEstructuras.size());
                PrimefacesContextUI.ejecutar("PF('EstructuraCargoDesempeñadoDialogo').show()");
                idCargoDesempeñado = -1;
            }
            if (idCargoDesempeñado == 3) {
                cargarLovPapeles();
                PrimefacesContextUI.actualizar("formLovs:formDCargoDesempenado:formPapelD:PapelCargoDesempeñadoDialogo");
                modificarInfoR_PapelCargoD(lovPapeles.size());
                PrimefacesContextUI.ejecutar("PF('PapelCargoDesempeñadoDialogo').show()");
                idCargoDesempeñado = -1;
            }
            if (idCargoDesempeñado == 4) {
                cargarLovEmpleados();
                PrimefacesContextUI.actualizar("formLovs:formDCargoDesempenado:formjefe:EmpleadoJefeCargoDesempeñadoDialogo");
                modificarInfoR_JefeCargoD(lovEmpleados.size());
                PrimefacesContextUI.ejecutar("PF('EmpleadoJefeCargoDesempeñadoDialogo').show()");
                idCargoDesempeñado = -1;
            }
        } else if (idCentroCosto >= 0) {
            if (idCentroCosto == 0) {
                cargarLovMotivosLocalizaciones();
                PrimefacesContextUI.actualizar("formLovs:formDCentroCosto:formCentroC:MotivoLocalizacionCentroCostoDialogo");
                modificarInfoR_MotivoCentroC(lovMotivosLocalizaciones.size());
                PrimefacesContextUI.ejecutar("PF('MotivoLocalizacionCentroCostoDialogo').show()");
                idCentroCosto = -1;
            }
            if (idCentroCosto == 1) {
                cargarLovEstructurasCentroCosto();
                PrimefacesContextUI.actualizar("formLovs:formDCentroCosto:formEstrucCC:EstructuraCentroCostoDialogo");
                modificarInfoR_EstrCentroC(lovEstructurasCentroCosto.size());
                PrimefacesContextUI.ejecutar("PF('EstructuraCentroCostoDialogo').show()");
                idCentroCosto = -1;
            }
        } else if (idTipoTrabajador >= 0) {
            if (idTipoTrabajador == 0) {
                cargarLovTiposTrabajadores();
                PrimefacesContextUI.actualizar("formLovs:formDTipoTrabajador:TipoTrabajadorTipoTrabajadorDialogo");
                modificarInfoR_TipoTraTT(lovTiposTrabajadores.size());
                PrimefacesContextUI.ejecutar("PF('TipoTrabajadorTipoTrabajadorDialogo').show()");
                idTipoTrabajador = -1;
            }
        } else if (idTipoSalario >= 0) {
            if (idTipoSalario == 0) {
                if (lovReformasLaborales == null) {
                    PrimefacesContextUI.ejecutar("PF('advertenciaSeleccionTipoT').show()");
                } else {
                    modificarInfoR_ReformaTipoSa(lovReformasLaborales.size());
                    PrimefacesContextUI.ejecutar("PF('ReformaLaboralTipoSalarioDialogo').show()");
                    PrimefacesContextUI.actualizar("formLovs:formDTipoSalario:ReformaLaboralTipoSalarioDialogo");
                    PrimefacesContextUI.actualizar("formLovs:formDTipoSalario:lovReformaLaboralTipoSalario");
                    idTipoSalario = -1;
                }
            }
        } else if (idSueldo >= 0) {
            if (idSueldo == 1) {
                cargarLovMotivosCambiosSueldos();
                PrimefacesContextUI.actualizar("formLovs:formDSueldo:formMotivoSu:MotivoCambioSueldoSueldoDialogo");
                modificarInfoR_MotivoSu(lovMotivosCambiosSueldos.size());
                PrimefacesContextUI.ejecutar("PF('MotivoCambioSueldoSueldoDialogo').show()");
                idSueldo = -1;
            }
            if (idSueldo == 2) {
                if (lovTiposSueldos == null) {
                    PrimefacesContextUI.ejecutar("PF('advertenciaSeleccionTipoT').show()");
                } else {
                    modificarInfoR_TipoSuSu(lovTiposSueldos.size());
                    PrimefacesContextUI.ejecutar("PF('TipoSueldoSueldoDialogo').show()");
                    PrimefacesContextUI.actualizar("formLovs:formDSueldo:formTipoSueldo:TipoSueldoSueldoDialogo");
                    PrimefacesContextUI.actualizar("formLovs:formDSueldo:formTipoSueldo:lovTipoSueldoSueldo");
                    idSueldo = -1;
                }
            }
            if (idSueldo == 3) {
                getLovUnidades();
                PrimefacesContextUI.actualizar("formLovs:formDSueldo:formUnidadS:UnidadSueldoDialogo");
                modificarInfoR_UnidadSu(lovUnidades.size());
                PrimefacesContextUI.ejecutar("PF('UnidadSueldoDialogo').show()");
                idSueldo = -1;
            }
        } else if (idTipoContrato >= 0) {
            if (idTipoContrato == 0) {
                cargarLovMotivosContratos();
                modificarInfoR_MotivoTipoCo(lovMotivosContratos.size());
                PrimefacesContextUI.ejecutar("PF('MotivoContratoTipoContratoDialogo').show()");
                PrimefacesContextUI.actualizar("formLovs:formDTipoContrato:formTipoContrat:MotivoContratoTipoContratoDialogo");
                PrimefacesContextUI.actualizar("formLovs:formDTipoContrato:formTipoContrat:lovMotivoContratoTipoContrato");
                idTipoContrato = -1;
            }
            if (idTipoContrato == 1) {
                if (lovTiposContratos == null) {
                    PrimefacesContextUI.ejecutar("PF('advertenciaSeleccionTipoT').show()");
                } else {
                    modificarInfoR_TipoContrato(lovTiposContratos.size());
                    PrimefacesContextUI.ejecutar("PF('TipoContratoTipoContratoDialogo').show()");
                    PrimefacesContextUI.actualizar("formLovs:formDTipoContrato:formTipoContrato:TipoContratoTipoContratoDialogo");
                    PrimefacesContextUI.actualizar("formLovs:formDTipoContrato:formTipoContrato:lovTipoContratoTipoContrato");
                    idTipoContrato = -1;
                }
            }
        } else if (idNormaLaboral >= 0) {
            if (idNormaLaboral == 0) {
                if (lovNormasLaborales == null) {
                    PrimefacesContextUI.ejecutar("PF('advertenciaSeleccionTipoT').show()");
                } else {
                    modificarInfoR_NormaL(lovNormasLaborales.size());
                    PrimefacesContextUI.ejecutar("PF('NormaLaboralNormaLaboralDialogo').show()");
                    PrimefacesContextUI.actualizar("formLovs:formDNormaLaboral:NormaLaboralNormaLaboralDialogo");
                    PrimefacesContextUI.actualizar("formLovs:formDNormaLaboral:lovNormaLaboralNormaLaboral");
                    idNormaLaboral = -1;
                }
            }
        } else if (idLegislacionLaboral >= 0) {
            if (idLegislacionLaboral == 0) {
                if (lovContratos == null) {
                    PrimefacesContextUI.ejecutar("PF('advertenciaSeleccionTipoT').show()");
                } else {
                    modificarInfoR_ContratoLL(lovContratos.size());
                    PrimefacesContextUI.ejecutar("PF('ContratoLegislacionLaboralDialogo').show()");
                    PrimefacesContextUI.actualizar("formLovs:formDLegislacionLaboral:ContratoLegislacionLaboralDialogo");
                    PrimefacesContextUI.actualizar("formLovs:formDLegislacionLaboral:lovContratoLegislacionLaboral");
                    idLegislacionLaboral = -1;
                }
            }
        } else if (idUbicacionGeografica >= 0) {
            if (idUbicacionGeografica == 0) {
                cargarLovUbicacionesGeograficas();
                PrimefacesContextUI.actualizar("formLovs:formDUbicacion:UbicacionUbicacionGeograficaDialogo");
                modificarInfoR_UbicacionUb(lovUbicacionesGeograficas.size());
                PrimefacesContextUI.ejecutar("PF('UbicacionUbicacionGeograficaDialogo').show()");
                idUbicacionGeografica = -1;
            }
        } else if (idJornadaLaboral >= 0) {
            if (idJornadaLaboral == 0) {
                cargarLovJornadasLaborales();
                PrimefacesContextUI.actualizar("formLovs:formDJornadaLaboral:JornadaJornadaLaboralDialogo");
                modificarInfoR_JornadaL(lovJornadasLaborales.size());
                PrimefacesContextUI.ejecutar("PF('JornadaJornadaLaboralDialogo').show()");
                idJornadaLaboral = -1;
            }
        } else if (idFormaPago >= 0) {
            if (idFormaPago == 0) {
                cargarLovPeriodicidades();
                PrimefacesContextUI.actualizar("formLovs:formDFormaPago:formPeriodicidad:PeriodicidadFormaPagoDialogo");
                modificarInfoR_PeriodFormaPago(lovPeriodicidades.size());
                PrimefacesContextUI.ejecutar("PF('PeriodicidadFormaPagoDialogo').show()");
                idFormaPago = -1;
            }
            if (idFormaPago == 3) {
                cargarLovSucursales();
                PrimefacesContextUI.actualizar("formLovs:formDFormaPago:formSucursal:SucursalFormaPagoDialogo");
                modificarInfoR_SucursalFormaP(lovSucursales.size());
                PrimefacesContextUI.ejecutar("PF('SucursalFormaPagoDialogo').show()");
                idFormaPago = -1;
            }
            if (idFormaPago == 4) {
                cargarLovMetodosPagos();
                PrimefacesContextUI.actualizar("formLovs:formDFormaPago:formMetodo:MetodoPagoFormaPagoDialogo");
                modificarInfoR_MetodoFormaP(lovMetodosPagos.size());
                PrimefacesContextUI.ejecutar("PF('MetodoPagoFormaPagoDialogo').show()");
                idFormaPago = -1;
            }
        } else if (idAfiliacionAFP >= 0) {
            if (idAfiliacionAFP == 0) {
                cargarLovTercerosSucursales();
                PrimefacesContextUI.actualizar("formLovs:formDAfiliacion:TerceroAfiliacionDialogo");
                modificarInfoR_TerceroAfSuc(lovTercerosSucursales.size());
                PrimefacesContextUI.ejecutar("PF('TerceroAfiliacionDialogo').show()");
            }
        } else if (idAfiliacionARP >= 0) {
            if (idAfiliacionARP == 0) {
                cargarLovTercerosSucursales();
                PrimefacesContextUI.actualizar("formLovs:formDAfiliacion:TerceroAfiliacionDialogo");
                modificarInfoR_TerceroAfSuc(lovTercerosSucursales.size());
                PrimefacesContextUI.ejecutar("PF('TerceroAfiliacionDialogo').show()");
            }
        } else if (idAfiliacionCaja >= 0) {
            if (idAfiliacionCaja == 0) {
                cargarLovTercerosSucursales();
                PrimefacesContextUI.actualizar("formLovs:formDAfiliacion:TerceroAfiliacionDialogo");
                modificarInfoR_TerceroAfSuc(lovTercerosSucursales.size());
                PrimefacesContextUI.ejecutar("PF('TerceroAfiliacionDialogo').show()");
            }
        } else if (idAfiliacionEPS >= 0) {
            if (idAfiliacionEPS == 0) {
                cargarLovTercerosSucursales();
                PrimefacesContextUI.actualizar("formLovs:formDAfiliacion:TerceroAfiliacionDialogo");
                modificarInfoR_TerceroAfSuc(lovTercerosSucursales.size());
                PrimefacesContextUI.ejecutar("PF('TerceroAfiliacionDialogo').show()");
            }
        } else if (idAfiliacionFondo >= 0) {
            if (idAfiliacionFondo == 0) {
                cargarLovTercerosSucursales();
                PrimefacesContextUI.actualizar("formLovs:formDAfiliacion:TerceroAfiliacionDialogo");
                modificarInfoR_TerceroAfSuc(lovTercerosSucursales.size());
                PrimefacesContextUI.ejecutar("PF('TerceroAfiliacionDialogo').show()");
            }
        } else if (idEstadoCivil >= 0) {
            if (idEstadoCivil == 0) {
                cargarLovEstadosCiviles();
                PrimefacesContextUI.actualizar("formLovs:formDEstadoCivil:EstadoCivilEstadoCivilDialogo");
                modificarInfoR_EstadoCivil(lovEstadosCiviles.size());
                PrimefacesContextUI.ejecutar("PF('EstadoCivilEstadoCivilDialogo').show()");
                idEstadoCivil = -1;
            }
        } else if (idDireccion >= 0) {
            if (idDireccion == 1) {
                cargarLovCiudades();
                PrimefacesContextUI.actualizar("formLovs:formDDireccion:CiudadDireccionDialogo");
                modificarInfoR_CiudadDir(lovCiudades.size());
                PrimefacesContextUI.ejecutar("PF('CiudadDireccionDialogo').show()");
                idDireccion = -1;
            }
        } else if (idTelefono >= 0) {
            if (idTelefono == 0) {
                cargarLovTiposTelefonos();
                PrimefacesContextUI.actualizar("formLovs:formDTelefono:formTel:TipoTelefonoTelefonoDialogo");
                modificarInfoR_TipoTelT(lovTiposTelefonos.size());
                PrimefacesContextUI.ejecutar("PF('TipoTelefonoTelefonoDialogo').show()");
                idTelefono = -1;
            }
            if (idTelefono == 1) {
                cargarLovCiudades();
                PrimefacesContextUI.actualizar("formLovs:formDTelefono:formCiudadTel:CiudadTelefonoDialogo");
                modificarInfoR_CiudadTel(lovCiudades.size());
                PrimefacesContextUI.ejecutar("PF('CiudadTelefonoDialogo').show()");
                idTelefono = -1;
            }
        }
    }

    public void editarCelda() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (idInformacionPersonal >= 0) {
            if (idInformacionPersonal == 0) {
                PrimefacesContextUI.actualizar("formDialogos:editarEmpresaInformacionPersonal");
                PrimefacesContextUI.ejecutar("PF('editarEmpresaInformacionPersonal').show()");
                idInformacionPersonal = -1;
            }
            if (idInformacionPersonal == 1) {
                PrimefacesContextUI.actualizar("formDialogos:editarPrimerApellidoInformacionPersonal");
                PrimefacesContextUI.ejecutar("PF('editarPrimerApellidoInformacionPersonal').show()");
                idInformacionPersonal = -1;
            }
            if (idInformacionPersonal == 2) {
                PrimefacesContextUI.actualizar("formDialogos:editarSegundoApellidoInformacionPersonal");
                PrimefacesContextUI.ejecutar("PF('editarSegundoApellidoInformacionPersonal').show()");
                idInformacionPersonal = -1;
            }
            if (idInformacionPersonal == 3) {
                PrimefacesContextUI.actualizar("formDialogos:editarNombresInformacionPersonal");
                PrimefacesContextUI.ejecutar("PF('editarNombresInformacionPersonal').show()");
                idInformacionPersonal = -1;
            }
            if (idInformacionPersonal == 5) {
                PrimefacesContextUI.actualizar("formDialogos:editarTipoDocumentoInformacionPersonal");
                PrimefacesContextUI.ejecutar("PF('editarTipoDocumentoInformacionPersonal').show()");
                idInformacionPersonal = -1;
            }
            if (idInformacionPersonal == 6) {
                PrimefacesContextUI.actualizar("formDialogos:editarNumeroDocumentoInformacionPersonal");
                PrimefacesContextUI.ejecutar("PF('editarNumeroDocumentoInformacionPersonal').show()");
                idInformacionPersonal = -1;
            }
            if (idInformacionPersonal == 7) {
                PrimefacesContextUI.actualizar("formDialogos:editarCiudadDocumentoInformacionPersonal");
                PrimefacesContextUI.ejecutar("PF('editarCiudadDocumentoInformacionPersonal').show()");
                idInformacionPersonal = -1;
            }
            if (idInformacionPersonal == 8) {
                PrimefacesContextUI.actualizar("formDialogos:editarFechaNacimientoInformacionPersonal");
                PrimefacesContextUI.ejecutar("PF('editarFechaNacimientoInformacionPersonal').show()");
                idInformacionPersonal = -1;
            }
            if (idInformacionPersonal == 9) {
                PrimefacesContextUI.actualizar("formDialogos:editarCiudadNacimientoInformacionPersonal");
                PrimefacesContextUI.ejecutar("PF('editarCiudadNacimientoInformacionPersonal').show()");
                idInformacionPersonal = -1;
            }
            if (idInformacionPersonal == 10) {
                PrimefacesContextUI.actualizar("formDialogos:editarFechaIngresoInformacionPersonal");
                PrimefacesContextUI.ejecutar("PF('editarFechaIngresoInformacionPersonal').show()");
                idInformacionPersonal = -1;
            }
            if (idInformacionPersonal == 11) {
                PrimefacesContextUI.actualizar("formDialogos:editarCodigoEmpleadoInformacionPersonal");
                PrimefacesContextUI.ejecutar("PF('editarCodigoEmpleadoInformacionPersonal').show()");
                idInformacionPersonal = -1;
            }
            if (idInformacionPersonal == 12) {
                PrimefacesContextUI.actualizar("formDialogos:editarEmailInformacionPersonal");
                PrimefacesContextUI.ejecutar("PF('editarEmailInformacionPersonal').show()");
                idInformacionPersonal = -1;
            }
            if (idInformacionPersonal == 13) {
                PrimefacesContextUI.actualizar("formDialogos:editarFechaUltimoPagoInformacionPersonal");
                PrimefacesContextUI.ejecutar("PF('editarFechaUltimoPagoInformacionPersonal').show()");
                idInformacionPersonal = -1;
            }
        }
        if (idCargoDesempeñado >= 0) {
            if (idCargoDesempeñado == 0) {
                PrimefacesContextUI.actualizar("formDialogos:editarCargoCargoDesempeñado");
                PrimefacesContextUI.ejecutar("PF('editarCargoCargoDesempeñado').show()");
                idCargoDesempeñado = -1;
            }
            if (idCargoDesempeñado == 1) {
                PrimefacesContextUI.actualizar("formDialogos:editarMotivoCargoDesempeñado");
                PrimefacesContextUI.ejecutar("PF('editarMotivoCargoDesempeñado').show()");
                idCargoDesempeñado = -1;
            }
            if (idCargoDesempeñado == 2) {
                PrimefacesContextUI.actualizar("formDialogos:editarEstructuraCargoDesempeñado");
                PrimefacesContextUI.ejecutar("PF('editarEstructuraCargoDesempeñado').show()");
                idCargoDesempeñado = -1;
            }
            if (idCargoDesempeñado == 3) {
                PrimefacesContextUI.actualizar("formDialogos:editarPapelCargoDesempeñado");
                PrimefacesContextUI.ejecutar("PF('editarPapelCargoDesempeñado').show()");
                idCargoDesempeñado = -1;
            }
            if (idCargoDesempeñado == 4) {
                PrimefacesContextUI.actualizar("formDialogos:editarEmpleadoJefeCargoDesempeñado");
                PrimefacesContextUI.ejecutar("PF('editarEmpleadoJefeCargoDesempeñado').show()");
                idCargoDesempeñado = -1;
            }
        }
        if (idCentroCosto >= 0) {
            if (idCentroCosto == 0) {
                PrimefacesContextUI.actualizar("formDialogos:editarEstructuraCentroCosto");
                PrimefacesContextUI.ejecutar("PF('editarEstructuraCentroCosto').show()");
                idCentroCosto = -1;
            }
            if (idCentroCosto == 1) {
                PrimefacesContextUI.actualizar("formDialogos:editarMotivoCentroCosto");
                PrimefacesContextUI.ejecutar("PF('editarMotivoCentroCosto').show()");
                idCentroCosto = -1;
            }
        }
        if (idTipoTrabajador >= 0) {
            if (idTipoTrabajador == 0) {
                PrimefacesContextUI.actualizar("formDialogos:editarTrabajadorTipoTrabajador");
                PrimefacesContextUI.ejecutar("PF('editarTrabajadorTipoTrabajador').show()");
                idTipoTrabajador = -1;
            }
        }
        if (idTipoSalario >= 0) {
            if (idTipoSalario == 0) {
                PrimefacesContextUI.actualizar("formDialogos:editarReformaTipoSalario");
                PrimefacesContextUI.ejecutar("PF('editarReformaTipoSalario').show()");
                idTipoSalario = -1;
            }
        }
        if (idSueldo >= 0) {
            if (idSueldo == 0) {
                PrimefacesContextUI.actualizar("formDialogos:editarValorSueldo");
                PrimefacesContextUI.ejecutar("PF('editarValorSueldo').show()");
                idSueldo = -1;
            }
            if (idSueldo == 1) {
                PrimefacesContextUI.actualizar("formDialogos:editarMotivoSueldo");
                PrimefacesContextUI.ejecutar("PF('editarMotivoSueldo').show()");
                idSueldo = -1;
            }
            if (idSueldo == 2) {
                PrimefacesContextUI.actualizar("formDialogos:editarTipoSueldoSueldo");
                PrimefacesContextUI.ejecutar("PF('editarTipoSueldoSueldo').show()");
                idSueldo = -1;
            }
            if (idSueldo == 3) {
                PrimefacesContextUI.actualizar("formDialogos:editarUnidadSueldo");
                PrimefacesContextUI.ejecutar("PF('editarUnidadSueldo').show()");
                idSueldo = -1;
            }
        }
        if (idTipoContrato >= 0) {
            if (idTipoContrato == 0) {
                PrimefacesContextUI.actualizar("formDialogos:editarMotivoTipoContrato");
                PrimefacesContextUI.ejecutar("PF('editarMotivoTipoContrato').show()");
                idTipoContrato = -1;
            }
            if (idTipoContrato == 1) {
                PrimefacesContextUI.actualizar("formDialogos:editarTipoTipoContrato");
                PrimefacesContextUI.ejecutar("PF('editarTipoTipoContrato').show()");
                idTipoContrato = -1;
            }
            if (idTipoContrato == 2) {
                PrimefacesContextUI.actualizar("formDialogos:editarFechaTipoContrato");
                PrimefacesContextUI.ejecutar("PF('editarFechaTipoContrato').show()");
                idTipoContrato = -1;
            }
        }
        if (idNormaLaboral >= 0) {
            if (idNormaLaboral == 0) {
                PrimefacesContextUI.actualizar("formDialogos:editarNormaNormaLaboral");
                PrimefacesContextUI.ejecutar("PF('editarNormaNormaLaboral').show()");
                idNormaLaboral = -1;
            }
        }
        if (idLegislacionLaboral >= 0) {
            if (idLegislacionLaboral == 0) {
                PrimefacesContextUI.actualizar("formDialogos:editarContratoLegislacionLaboral");
                PrimefacesContextUI.ejecutar("PF('editarContratoLegislacionLaboral').show()");
                idLegislacionLaboral = -1;
            }
        }
        if (idUbicacionGeografica >= 0) {
            if (idUbicacionGeografica == 0) {
                PrimefacesContextUI.actualizar("formDialogos:editarUbicacionUbicacionGeografica");
                PrimefacesContextUI.ejecutar("PF('editarUbicacionUbicacionGeografica').show()");
                idUbicacionGeografica = -1;
            }
        }
        if (idJornadaLaboral >= 0) {
            if (idJornadaLaboral == 0) {
                PrimefacesContextUI.actualizar("formDialogos:editarJornadaJornadaLaboral");
                PrimefacesContextUI.ejecutar("PF('editarJornadaJornadaLaboral').show()");
                idJornadaLaboral = -1;
            }
        }
        if (idFormaPago >= 0) {
            if (idFormaPago == 0) {
                PrimefacesContextUI.actualizar("formDialogos:editarPeriodicidadFormaPago");
                PrimefacesContextUI.ejecutar("PF('editarPeriodicidadFormaPago').show()");
                idFormaPago = -1;
            }
            if (idFormaPago == 1) {
                PrimefacesContextUI.actualizar("formDialogos:editarCuentaFormaPago");
                PrimefacesContextUI.ejecutar("PF('editarCuentaFormaPago').show()");
                idFormaPago = -1;
            }
            if (idFormaPago == 3) {
                PrimefacesContextUI.actualizar("formDialogos:editarSucursalFormaPago");
                PrimefacesContextUI.ejecutar("PF('editarSucursalFormaPago').show()");
                idFormaPago = -1;
            }
            if (idFormaPago == 4) {
                PrimefacesContextUI.actualizar("formDialogos:editarMetodoFormaPago");
                PrimefacesContextUI.ejecutar("PF('editarMetodoFormaPago').show()");
                idFormaPago = -1;
            }
        }
        if (idAfiliacionAFP >= 0) {
            if (idAfiliacionAFP == 0) {
                PrimefacesContextUI.actualizar("formDialogos:editarAFPAfiliacion");
                PrimefacesContextUI.ejecutar("PF('editarAFPAfiliacion').show()");
                idAfiliacionAFP = -1;
            }
        }
        if (idAfiliacionARP >= 0) {
            if (idAfiliacionARP == 0) {
                PrimefacesContextUI.actualizar("formDialogos:editarARPAfiliacion");
                PrimefacesContextUI.ejecutar("PF('editarARPAfiliacion').show()");
                idAfiliacionARP = -1;
            }
        }
        if (idAfiliacionCaja >= 0) {
            if (idAfiliacionCaja == 0) {
                PrimefacesContextUI.actualizar("formDialogos:editarCajaAfiliacion");
                PrimefacesContextUI.ejecutar("PF('editarCajaAfiliacion').show()");
                idAfiliacionCaja = -1;
            }
        }
        if (idAfiliacionEPS >= 0) {
            if (idAfiliacionEPS == 0) {
                PrimefacesContextUI.actualizar("formDialogos:editarEPSAfiliacion");
                PrimefacesContextUI.ejecutar("PF('editarEPSAfiliacion').show()");
                idAfiliacionEPS = -1;
            }
        }
        if (idAfiliacionFondo >= 0) {
            if (idAfiliacionFondo == 0) {
                PrimefacesContextUI.actualizar("formDialogos:editarFondoAfiliacion");
                PrimefacesContextUI.ejecutar("PF('editarFondoAfiliacion').show()");
                idAfiliacionFondo = -1;
            }
        }
        if (idEstadoCivil >= 0) {
            if (idEstadoCivil == 0) {
                PrimefacesContextUI.actualizar("formDialogos:editarEstadoEstadoCivil");
                PrimefacesContextUI.ejecutar("PF('editarEstadoEstadoCivil').show()");
                idEstadoCivil = -1;
            }
        }
        if (idDireccion >= 0) {
            if (idDireccion == 0) {
                PrimefacesContextUI.actualizar("formDialogos:editarDireccionDireccion");
                PrimefacesContextUI.ejecutar("PF('editarDireccionDireccion').show()");
                idDireccion = -1;
            }
            if (idDireccion == 1) {
                PrimefacesContextUI.actualizar("formDialogos:editarCiudadDireccion");
                PrimefacesContextUI.ejecutar("PF('editarCiudadDireccion').show()");
                idDireccion = -1;
            }
        }
        if (idTelefono >= 0) {
            if (idTelefono == 0) {
                PrimefacesContextUI.actualizar("formDialogos:editarTipoTelefonoTelefono");
                PrimefacesContextUI.ejecutar("PF('editarTipoTelefonoTelefono').show()");
                idTelefono = -1;
            }
            if (idTelefono == 1) {
                PrimefacesContextUI.actualizar("formDialogos:editarCiudadTelefono");
                PrimefacesContextUI.ejecutar("PF('editarCiudadTelefono').show()");
                idTelefono = -1;
            }
            if (idTelefono == 2) {
                PrimefacesContextUI.actualizar("formDialogos:editarNumeroTelTelefono");
                PrimefacesContextUI.ejecutar("PF('editarNumeroTelTelefono').show()");
                idTelefono = -1;
            }
        }
    }

    public void cancelarModificaciones() {
        nuevoEmpleado = new Empleados();
        //nuevoEmpleado.setEmpresa(new Empresas());
        nuevaPersona = new Personas();
        nuevaPersona.setCiudaddocumento(new Ciudades());
        nuevaPersona.setCiudadnacimiento(new Ciudades());
        nuevaPersona.setTipodocumento(new TiposDocumentos());
        nuevaVigenciaCargo = new VigenciasCargos();
        nuevaVigenciaCargo.setPapel(new Papeles());
        nuevaVigenciaCargo.setCargo(new Cargos());
        nuevaVigenciaCargo.setEstructura(new Estructuras());
        nuevaVigenciaCargo.setMotivocambiocargo(new MotivosCambiosCargos());
        nuevaVigenciaCargo.setEmpleadojefe(new Empleados());
        nuevaVigenciaCargo.getEmpleadojefe().setPersona(new Personas());
        nuevaVigenciaLocalizacion = new VigenciasLocalizaciones();
        nuevaVigenciaLocalizacion.setLocalizacion(new Estructuras());
        nuevaVigenciaLocalizacion.getLocalizacion().setCentrocosto(new CentrosCostos());
        nuevaVigenciaLocalizacion.setMotivo(new MotivosLocalizaciones());
        nuevaVigenciaTipoTrabajador = new VigenciasTiposTrabajadores();
        nuevaVigenciaTipoTrabajador.setTipotrabajador(new TiposTrabajadores());
        nuevaVigenciaReformaLaboral = new VigenciasReformasLaborales();
        nuevaVigenciaReformaLaboral.setReformalaboral(new ReformasLaborales());
        nuevaVigenciaSueldo = new VigenciasSueldos();
        nuevaVigenciaSueldo.setUnidadpago(new Unidades());
        nuevaVigenciaSueldo.setTiposueldo(new TiposSueldos());
        nuevaVigenciaSueldo.setMotivocambiosueldo(new MotivosCambiosSueldos());
        nuevaVigenciaTipoContrato = new VigenciasTiposContratos();
        nuevaVigenciaTipoContrato.setTipocontrato(new TiposContratos());
        nuevaVigenciaTipoContrato.setMotivocontrato(new MotivosContratos());
        nuevaVigenciaNormaEmpleado = new VigenciasNormasEmpleados();
        nuevaVigenciaNormaEmpleado.setNormalaboral(new NormasLaborales());
        nuevaVigenciaContrato = new VigenciasContratos();
        nuevaVigenciaContrato.setContrato(new Contratos());
        nuevaVigenciaUbicacion = new VigenciasUbicaciones();
        nuevaVigenciaUbicacion.setUbicacion(new UbicacionesGeograficas());
        nuevaVigenciaFormaPago = new VigenciasFormasPagos();
        nuevaVigenciaFormaPago.setSucursal(new Sucursales());
        nuevaVigenciaFormaPago.setMetodopago(new MetodosPagos());
        nuevaVigenciaFormaPago.setFormapago(new Periodicidades());
        nuevaVigenciaJornada = new VigenciasJornadas();
        nuevaVigenciaJornada.setJornadatrabajo(new JornadasLaborales());
        nuevaVigenciaAfiliacionAFP = new VigenciasAfiliaciones();
        nuevaVigenciaAfiliacionAFP.setTercerosucursal(new TercerosSucursales());
        nuevaVigenciaAfiliacionARP = new VigenciasAfiliaciones();
        nuevaVigenciaAfiliacionARP.setTercerosucursal(new TercerosSucursales());
        nuevaVigenciaAfiliacionCaja = new VigenciasAfiliaciones();
        nuevaVigenciaAfiliacionCaja.setTercerosucursal(new TercerosSucursales());
        nuevaVigenciaAfiliacionEPS = new VigenciasAfiliaciones();
        nuevaVigenciaAfiliacionEPS.setTercerosucursal(new TercerosSucursales());
        nuevaVigenciaAfiliacionFondo = new VigenciasAfiliaciones();
        nuevaVigenciaAfiliacionFondo.setTercerosucursal(new TercerosSucursales());
        nuevoEstadoCivil = new VigenciasEstadosCiviles();
        nuevoEstadoCivil.setEstadocivil(new EstadosCiviles());
        nuevaDireccion = new Direcciones();
        nuevaDireccion.setCiudad(new Ciudades());
        nuevoTelefono = new Telefonos();
        nuevoTelefono.setCiudad(new Ciudades());
        nuevoTelefono.setTipotelefono(new TiposTelefonos());
        //Auxiliares
        valorSueldo = new BigDecimal("0");
        //Otros
        aceptar = true;
        disableNombreEstructuraCargo = true;
        System.out.println("disableNombreEstructuraCargo es true");
        disableDescripcionEstructura = true;
        disableUbicacionGeografica = true;
        disableAfiliaciones = true;
        disableCamposDependientesTipoT = true;
        //Index
        idCargoDesempeñado = -1;
        idCentroCosto = -1;
        idTipoTrabajador = -1;
        idTipoSalario = -1;
        idSueldo = -1;
        idTipoContrato = -1;
        idNormaLaboral = -1;
        idLegislacionLaboral = -1;
        idUbicacionGeografica = -1;
        idFormaPago = -1;
        idJornadaLaboral = -1;
        idAfiliacionAFP = -1;
        idAfiliacionARP = -1;
        idAfiliacionCaja = -1;
        idAfiliacionEPS = -1;
        idAfiliacionFondo = -1;
        idEstadoCivil = -1;
        idDireccion = -1;
        idTelefono = -1;
        idInformacionPersonal = -1;
        //Permitir Index
        permitirIndexAfiliacionAFP = true;
        permitirIndexAfiliacionARP = true;
        permitirIndexAfiliacionCaja = true;
        permitirIndexAfiliacionEPS = true;
        permitirIndexAfiliacionFondo = true;
        permitirIndexCargoDesempeñado = true;
        permitirIndexCentroCosto = true;
        permitirIndexDireccion = true;
        permitirIndexEstadoCivil = true;
        permitirIndexEstadoCivil = true;
        permitirIndexFormaPago = true;
        permitirIndexInformacionPersonal = true;
        permitirIndexJornadaLaboral = true;
        permitirIndexLegislacionLaboral = true;
        permitirIndexNormaLaboral = true;
        permitirIndexSueldo = true;
        permitirIndexTelefono = true;
        permitirIndexTipoContrato = true;
        permitirIndexTipoSalario = true;
        permitirIndexTipoTrabajador = true;
        permitirIndexUbicacionGeografica = true;
        fechaIngreso = null;
        fechaCorte = null;

        lovEmpresas = null;
        lovTiposDocumentos = null;
        lovCiudades = null;
        lovCargos = null;
        lovMotivosCargos = null;
        lovEstructuras = null;
        lovPapeles = null;
        lovEmpleados = null;
        lovMotivosLocalizaciones = null;
        lovEstructurasCentroCosto = null;
        lovTiposTrabajadores = null;
        lovReformasLaborales = null;
        lovMotivosCambiosSueldos = null;
        lovTiposSueldos = null;
        lovUnidades = null;
        lovMotivosContratos = null;
        lovTiposContratos = null;
        lovNormasLaborales = null;
        lovContratos = null;
        lovUbicacionesGeograficas = null;
        lovPeriodicidades = null;
        lovSucursales = null;
        lovMetodosPagos = null;
        lovJornadasLaborales = null;
        lovTercerosSucursales = null;
        lovEstadosCiviles = null;
        lovTiposTelefonos = null;
        permitirDesplegarLista = true;

        cargarLovEmpresas();
        nuevoEmpleado.setEmpresa(lovEmpresas.get(0));

        RequestContext context = RequestContext.getCurrentInstance();
        PrimefacesContextUI.actualizar("form:scrollPrincipal");
    }

    public boolean validarCamposObligatoriosEmpleado() {
        boolean retorno = true;
        int i = 1;
        if (nuevoEmpleado.getEmpresa().getSecuencia() == null) {
            //if (nuevoEmpleado.getEmpresa() == null) {
            System.err.println("validarCamposObligatoriosEmpleado() : 1");
            retorno = false;
        }
        if (nuevaPersona.getPrimerapellido() == null) {
            System.err.println("validarCamposObligatoriosEmpleado() : 2");
            retorno = false;
        } else {
            if (nuevaPersona.getPrimerapellido().isEmpty()) {
                System.err.println("validarCamposObligatoriosEmpleado() : 3");
                retorno = false;
            }
        }
        if (nuevaPersona.getNombre() == null) {
            System.err.println("validarCamposObligatoriosEmpleado() : 4");
            retorno = false;
        } else {
            if (nuevaPersona.getNombre().isEmpty()) {
                System.err.println("validarCamposObligatoriosEmpleado() : 5");
                retorno = false;
            }
        }
        if (nuevaPersona.getTipodocumento() == null) {
            System.err.println("validarCamposObligatoriosEmpleado() : 6");
            retorno = false;
        } else if (nuevaPersona.getTipodocumento().getSecuencia() == null) {
            System.err.println("validarCamposObligatoriosEmpleado() : 6");
            retorno = false;
        }

        if (nuevaPersona.getSexo() == null) {
            System.err.println("validarCamposObligatoriosEmpleado() : 7");
            retorno = false;
        }
        if (nuevaPersona.getNumerodocumento() == null) {
            System.err.println("validarCamposObligatoriosEmpleado() : 8");
            retorno = false;
        }
        if (nuevaPersona.getCiudaddocumento() == null) {
            System.err.println("validarCamposObligatoriosEmpleado() : 9");
            retorno = false;
        } else if (nuevaPersona.getCiudaddocumento().getSecuencia() == null) {
            System.err.println("validarCamposObligatoriosEmpleado() : 9");
            retorno = false;
        }
        if (nuevaPersona.getCiudadnacimiento().getSecuencia() == null) {
            System.err.println("validarCamposObligatoriosEmpleado() : 10");
            retorno = false;
        }
        if (nuevaPersona.getFechanacimiento() == null) {
            System.err.println("validarCamposObligatoriosEmpleado() : 11");
            retorno = false;
        }
        if (fechaIngreso == null) {
            System.err.println("validarCamposObligatoriosEmpleado() : 12");
            retorno = false;
        }
        if (nuevoEmpleado.getCodigoempleado() == null) {
            System.err.println("validarCamposObligatoriosEmpleado() : 13");
            retorno = false;
        }
        //
        if (nuevaVigenciaCargo.getCargo() == null) {
            System.err.println("validarCamposObligatoriosEmpleado() : 14");
            retorno = false;
        } else if (nuevaVigenciaCargo.getCargo().getSecuencia() == null) {
            System.err.println("validarCamposObligatoriosEmpleado() : 14");
            retorno = false;
        }
        //
        if (nuevaVigenciaCargo.getMotivocambiocargo() == null) {
            System.err.println("validarCamposObligatoriosEmpleado() : 15");
            retorno = false;
        } else if (nuevaVigenciaCargo.getMotivocambiocargo().getSecuencia() == null) {
            System.err.println("validarCamposObligatoriosEmpleado() : 15");
            retorno = false;
        }
        //
        if (nuevaVigenciaCargo.getEstructura() == null) {
            System.err.println("validarCamposObligatoriosEmpleado() : 16");
            retorno = false;
        } else if (nuevaVigenciaCargo.getEstructura().getSecuencia() == null) {
            System.err.println("validarCamposObligatoriosEmpleado() : 16");
            retorno = false;
        }
        //
        if (nuevaVigenciaLocalizacion.getMotivo() == null) {
            System.err.println("validarCamposObligatoriosEmpleado() : 17");
            retorno = false;
        } else if (nuevaVigenciaLocalizacion.getMotivo().getSecuencia() == null) {
            System.err.println("validarCamposObligatoriosEmpleado() : 17");
            retorno = false;
        }
        //
        if (nuevaVigenciaLocalizacion.getLocalizacion() == null) {
            System.err.println("validarCamposObligatoriosEmpleado() : 18");
            retorno = false;
        } else if (nuevaVigenciaLocalizacion.getLocalizacion().getSecuencia() == null) {
            System.err.println("validarCamposObligatoriosEmpleado() : 18");
            retorno = false;
        }
        //
        if (nuevaVigenciaTipoTrabajador.getTipotrabajador() == null) {
            System.err.println("validarCamposObligatoriosEmpleado() : 19");
            retorno = false;
        } else if (nuevaVigenciaTipoTrabajador.getTipotrabajador().getSecuencia() == null) {
            System.err.println("validarCamposObligatoriosEmpleado() : 19");
            retorno = false;
        }
        //
        if (nuevaVigenciaReformaLaboral.getReformalaboral() == null) {
            System.err.println("validarCamposObligatoriosEmpleado() : 20");
            retorno = false;
        } else if (nuevaVigenciaReformaLaboral.getReformalaboral().getSecuencia() == null) {
            System.err.println("validarCamposObligatoriosEmpleado() : 20");
            retorno = false;
        }
        //
        if (valorSueldo == null) {
            System.err.println("validarCamposObligatoriosEmpleado() : 21");
            retorno = false;
        } else if (valorSueldo.doubleValue() <= 0) {
            System.err.println("validarCamposObligatoriosEmpleado() : 22");
            retorno = false;
        }
        //
        if (nuevaVigenciaSueldo.getMotivocambiosueldo() == null) {
            System.err.println("validarCamposObligatoriosEmpleado() : 23");
            retorno = false;
        } else if (nuevaVigenciaSueldo.getMotivocambiosueldo().getSecuencia() == null) {
            System.err.println("validarCamposObligatoriosEmpleado() : 23");
            retorno = false;
        }
        //
        nuevaVigenciaSueldo.setUnidadpago(unidadPesos);
        if (nuevaVigenciaSueldo.getUnidadpago() == null) {
            System.err.println("validarCamposObligatoriosEmpleado() : 24");
            retorno = false;
        } else if (nuevaVigenciaSueldo.getUnidadpago().getSecuencia() == null) {
            System.err.println("validarCamposObligatoriosEmpleado() : 24");
            retorno = false;
        }
        //
        if (nuevaVigenciaSueldo.getTiposueldo().getSecuencia() == null) {
            System.err.println("validarCamposObligatoriosEmpleado() : 25");
            retorno = false;
        }
        //
        if (nuevaVigenciaTipoContrato.getMotivocontrato().getSecuencia() == null) {
            System.err.println("validarCamposObligatoriosEmpleado() : 26");
            retorno = false;
        }
        if (nuevaVigenciaTipoContrato.getTipocontrato().getSecuencia() == null) {
            System.err.println("validarCamposObligatoriosEmpleado() : 27");
            retorno = false;
        }
        //
        if (nuevaVigenciaNormaEmpleado.getNormalaboral().getSecuencia() == null) {
            System.err.println("validarCamposObligatoriosEmpleado() : 28");
            retorno = false;
        }
        //
        if (nuevaVigenciaContrato.getContrato().getSecuencia() == null) {
            System.err.println("validarCamposObligatoriosEmpleado() : 29");
            retorno = false;
        }
        //
        if (nuevaVigenciaUbicacion.getUbicacion().getSecuencia() == null) {
            System.err.println("validarCamposObligatoriosEmpleado() : 30");
            retorno = false;
        }
        //
        if (nuevaVigenciaJornada.getJornadatrabajo().getSecuencia() == null) {
            System.err.println("validarCamposObligatoriosEmpleado() : 31");
            retorno = false;
        }
        //
        if (nuevaVigenciaFormaPago.getFormapago().getSecuencia() == null) {
            System.err.println("validarCamposObligatoriosEmpleado() : 32");
            retorno = false;
        }
        if (nuevaVigenciaFormaPago.getMetodopago().getSecuencia() == null) {
            System.err.println("validarCamposObligatoriosEmpleado() : 33");
            retorno = false;
        }
        //
        if (nuevaVigenciaAfiliacionEPS.getTercerosucursal().getSecuencia() == null) {
            System.err.println("validarCamposObligatoriosEmpleado() : 34");
            retorno = false;
        }
        //
        if (nuevaVigenciaAfiliacionARP.getTercerosucursal().getSecuencia() == null) {
            System.err.println("validarCamposObligatoriosEmpleado() : 35");
            retorno = false;
        }
        return retorno;
    }

    public boolean validarFechasEmpleado() {
        mensajeErrorFechasEmpleado = "";
        boolean retorno = true;
        fechaParametro = new Date();
        fechaParametro.setYear(0);
        fechaParametro.setMonth(1);
        fechaParametro.setDate(1);
        if (nuevaPersona.getFechanacimiento() != null) {
            if (nuevaPersona.getFechanacimiento().before(fechaParametro)) {
                retorno = false;
                mensajeErrorFechasEmpleado = mensajeErrorFechasEmpleado + " - Fecha Nacimiento";
                System.err.println("validarFechasEmpleado() : 1");
            }
        }

        if (fechaIngreso != null) {
            if (fechaIngreso.before(fechaParametro)) {
                mensajeErrorFechasEmpleado = mensajeErrorFechasEmpleado + " - Fecha Ingreso";
                retorno = false;
                System.err.println("validarFechasEmpleado() : 2");
            }
        }

        if (fechaCorte != null) {
            if (fechaCorte.before(fechaParametro)) {
                mensajeErrorFechasEmpleado = mensajeErrorFechasEmpleado + " - Fecha Corte";
                retorno = false;
                System.err.println("validarFechasEmpleado() : 3");
            }
        }

        if (nuevaVigenciaTipoContrato.getFechavigencia() != null) {
            if (nuevaVigenciaTipoContrato.getFechavigencia().before(fechaParametro)) {
                mensajeErrorFechasEmpleado = mensajeErrorFechasEmpleado + " - Fecha Final Contrato";
                retorno = false;
                System.err.println("validarFechasEmpleado() : 4");
            } else {
                if (nuevaVigenciaTipoContrato.getFechavigencia().before(fechaIngreso)) {
                    mensajeErrorFechasEmpleado = mensajeErrorFechasEmpleado + " - Fecha Final Contrato";
                    retorno = false;
                    System.err.println("validarFechasEmpleado() : 5");
                }
            }
        }
        return retorno;
    }

    public boolean validarCamposAlternativosEmpleado() {
        boolean retorno = true;
        //Para grupo sanguineo:
        if (!nuevaPersona.getGruposanguineo().isEmpty() && !nuevaPersona.getFactorrh().isEmpty()) {
            if (nuevaPersona.getGruposanguineo().isEmpty() && !nuevaPersona.getFactorrh().isEmpty()) {
                System.err.println("validarCamposAlternativosEmpleado() : 1");
                retorno = false;
            }
            if (!nuevaPersona.getGruposanguineo().isEmpty() && nuevaPersona.getFactorrh().isEmpty()) {
                System.err.println("validarCamposAlternativosEmpleado() : 2");
                retorno = false;
            }
        }

        //Para direcciones
        if (nuevaDireccion.getDireccionalternativa() != null && nuevaDireccion.getCiudad() != null) {
            if (!nuevaDireccion.getDireccionalternativa().isEmpty() && !nuevaDireccion.getCiudad().getNombre().isEmpty()) {
                if (nuevaDireccion.getDireccionalternativa() == null) {
                    if (nuevaDireccion.getCiudad().getSecuencia() != null) {
                        System.err.println("validarCamposAlternativosEmpleado() : 3");
                        retorno = false;
                    }
                } else {
                    if (nuevaDireccion.getDireccionalternativa().isEmpty()) {
                        if (nuevaDireccion.getCiudad().getSecuencia() != null) {
                            System.err.println("validarCamposAlternativosEmpleado() : 4");
                            retorno = false;
                        }
                    }
                }
                if (nuevaDireccion.getCiudad().getSecuencia() == null) {
                    if ((nuevaDireccion.getDireccionalternativa() != null) && (!nuevaDireccion.getDireccionalternativa().isEmpty())) {
                        System.err.println("validarCamposAlternativosEmpleado() : 5");
                        retorno = false;
                    }
                }
            }
        }
        //Para Telefonos
        if ((nuevoTelefono.getNumerotelefono() <= 0) && (nuevoTelefono.getCiudad() != null) && (nuevoTelefono.getTipotelefono() != null)) {
            if ((nuevoTelefono.getCiudad().getNombre() != null) && (nuevoTelefono.getTipotelefono().getNombre() != null)) {

                if (nuevoTelefono.getNumerotelefono() > 0 && nuevoTelefono.getCiudad() == null && nuevoTelefono.getTipotelefono() == null) {
                    System.err.println("validarCamposAlternativosEmpleado() : 6");
                    retorno = false;
                }
                if (nuevoTelefono.getNumerotelefono() <= 0 && nuevoTelefono.getCiudad().getSecuencia() != null && nuevoTelefono.getTipotelefono().getSecuencia() == null) {
                    System.err.println("validarCamposAlternativosEmpleado() : 7");
                    retorno = false;
                }
                if (nuevoTelefono.getNumerotelefono() <= 0 && nuevoTelefono.getCiudad().getSecuencia() == null && nuevoTelefono.getTipotelefono().getSecuencia() != null) {
                    System.err.println("validarCamposAlternativosEmpleado() : 8");
                    retorno = false;
                }
                if (nuevoTelefono.getNumerotelefono() <= 0 && nuevoTelefono.getCiudad().getSecuencia() != null && nuevoTelefono.getTipotelefono().getSecuencia() != null) {
                    System.err.println("validarCamposAlternativosEmpleado() : 9");
                    retorno = false;
                }
                if (nuevoTelefono.getNumerotelefono() > 0 && nuevoTelefono.getCiudad().getSecuencia() != null && nuevoTelefono.getTipotelefono().getSecuencia() == null) {
                    System.err.println("validarCamposAlternativosEmpleado() : 10");
                    retorno = false;
                }
                if (nuevoTelefono.getNumerotelefono() > 0 && nuevoTelefono.getCiudad().getSecuencia() == null && nuevoTelefono.getTipotelefono().getSecuencia() != null) {
                    System.err.println("validarCamposAlternativosEmpleado() : 11");
                    retorno = false;
                }
            }
        }
        return retorno;
    }

    public boolean validarCamposConTipoTrabajador() {
        System.out.println("Entro en validarCamposConTipoTrabajador(), errorTT = " + errorTT);
        boolean continuarV = true;
        if ("Rl".equals(errorTT)) {
            continuarV = false;
            PrimefacesContextUI.ejecutar("PF('aletarTiposTrabajadoresRL').show()");
        } else if ("TS".equals(errorTT)) {
            continuarV = false;
            PrimefacesContextUI.ejecutar("PF('aletarTiposTrabajadoresTS').show()");
        } else if ("TC".equals(errorTT)) {
            continuarV = false;
            PrimefacesContextUI.ejecutar("PF('aletarTiposTrabajadoresTC').show()");
        } else if ("NL".equals(errorTT)) {
            continuarV = false;
            PrimefacesContextUI.ejecutar("PF('aletarTiposTrabajadoresNL').show()");
        } else if ("C".equals(errorTT)) {
            continuarV = false;
            PrimefacesContextUI.ejecutar("PF('aletarTiposTrabajadoresC').show()");
        }
        return continuarV;
    }

    public void crearNuevoEmpleado() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (validarCamposConTipoTrabajador()) {
            if (validarCamposObligatoriosEmpleado()) {
                if (validarFechasEmpleado()) {
                    if (validarCamposAlternativosEmpleado()) {

                        k++;
                        l = BigInteger.valueOf(k);
                        String checkIntegral = administrarPersonaIndividual.obtenerCheckIntegralReformaLaboral(nuevaVigenciaReformaLaboral.getReformalaboral().getSecuencia());
                        nuevaPersona.setSecuencia(l);
                        administrarPersonaIndividual.crearNuevaPersona(nuevaPersona);
                        Personas personaAlmacenada = administrarPersonaIndividual.obtenerUltimoRegistroPersona(nuevaPersona.getNumerodocumento());
                        if (personaAlmacenada != null) {
                            nuevoEmpleado.setPersona(personaAlmacenada);
                            //
                            k++;
                            l = BigInteger.valueOf(k);
                            nuevoEmpleado.setSecuencia(l);
                            nuevaVigenciaCargo.setSecuencia(l);
                            nuevaVigenciaCargo.setFechavigencia(fechaIngreso);
                            System.out.println("Enviando a crear Empleado: COD: " + nuevoEmpleado.getCodigoempleado() + ", PERSONA: " + nuevoEmpleado.getPersona().getSecuencia() + " Y EMPRESA: " + nuevoEmpleado.getEmpresa().getSecuencia());

                            BigInteger secEmpleado = administrarPersonaIndividual.crearEmpl_Con_VCargo(nuevoEmpleado.getCodigoempleado(), nuevoEmpleado.getPersona().getSecuencia(), nuevoEmpleado.getEmpresa().getSecuencia(), nuevaVigenciaCargo);
                            System.out.println("crearNuevoEmpleado() nuevoEmpleado ya volvio de crear el empleado con Vigencia cargo, secEmpleado : " + secEmpleado);
                            nuevoEmpleado.setSecuencia(secEmpleado);
//                            Empleados empleadoAlmacenado = administrarPersonaIndividual.obtenerUltimoRegistroEmpleado(nuevoEmpleado.getEmpresa().getSecuencia(), nuevoEmpleado.getCodigoempleado());
//                            System.out.println("crearNuevoEmpleado() empleadoAlmacenado : " + empleadoAlmacenado);

                            if (nuevoEmpleado != null) {

                                if (nuevaVigenciaCargo.getPapel().getSecuencia() != null || nuevaVigenciaCargo.getEmpleadojefe().getSecuencia() != null) {
                                    Papeles papel = nuevaVigenciaCargo.getPapel();
                                    Empleados empleadoJefe = nuevaVigenciaCargo.getEmpleadojefe();
                                    nuevaVigenciaCargo = new VigenciasCargos();
                                    nuevaVigenciaCargo = administrarPersonaIndividual.obtenerUltimaVigenciaCargo(nuevoEmpleado.getSecuencia(), nuevoEmpleado.getEmpresa().getSecuencia());
                                    nuevaVigenciaCargo.setEmpleadojefe(empleadoJefe);
                                    nuevaVigenciaCargo.setPapel(papel);
                                    administrarPersonaIndividual.modificarVigenciaCargo(nuevaVigenciaCargo);
                                }

                                //
                                k++;
                                l = BigInteger.valueOf(k);
                                nuevaVigenciaLocalizacion.setSecuencia(l);
                                nuevaVigenciaLocalizacion.setEmpleado(nuevoEmpleado);
                                nuevaVigenciaLocalizacion.setFechavigencia(fechaIngreso);
                                administrarPersonaIndividual.crearVigenciaLocalizacion(nuevaVigenciaLocalizacion);
                                //
                                k++;
                                l = BigInteger.valueOf(k);
                                nuevaVigenciaTipoTrabajador.setSecuencia(l);
                                nuevaVigenciaTipoTrabajador.setEmpleado(nuevoEmpleado);
                                nuevaVigenciaTipoTrabajador.setFechavigencia(fechaIngreso);
                                administrarPersonaIndividual.crearVigenciaTipoTrabajador(nuevaVigenciaTipoTrabajador);
                                //
                                k++;
                                l = BigInteger.valueOf(k);
                                nuevaVigenciaReformaLaboral.setSecuencia(l);
                                nuevaVigenciaReformaLaboral.setEmpleado(nuevoEmpleado);
                                nuevaVigenciaReformaLaboral.setFechavigencia(fechaIngreso);
                                administrarPersonaIndividual.crearVigenciaReformaLaboral(nuevaVigenciaReformaLaboral);
                                //
                                k++;
                                l = BigInteger.valueOf(k);
                                nuevaVigenciaSueldo.setSecuencia(l);
                                nuevaVigenciaSueldo.setEmpleado(nuevoEmpleado); //
                                nuevaVigenciaSueldo.setFechavigencia(fechaIngreso); //
                                nuevaVigenciaSueldo.setValor(valorSueldo);//
                                nuevaVigenciaSueldo.setFechasistema(new Date());//
                                nuevaVigenciaSueldo.setFechavigenciaretroactivo(fechaIngreso);//
                                nuevaVigenciaSueldo.setUnidadpago(unidadPesos);
                                if (nuevaVigenciaSueldo.getTiposueldo().getSecuencia() == null) {
                                    nuevaVigenciaSueldo.setTiposueldo(null);
                                }
                                administrarPersonaIndividual.crearVigenciaSueldo(nuevaVigenciaSueldo);//
                                //
                                k++;
                                l = BigInteger.valueOf(k);
                                nuevaVigenciaTipoContrato.setSecuencia(l);
                                nuevaVigenciaTipoContrato.setEmpleado(nuevoEmpleado);
                                nuevaVigenciaTipoContrato.setFechavigencia(fechaIngreso);
                                administrarPersonaIndividual.crearVigenciaTipoContrato(nuevaVigenciaTipoContrato);
                                //
                                k++;
                                l = BigInteger.valueOf(k);
                                nuevaVigenciaNormaEmpleado.setSecuencia(l);
                                nuevaVigenciaNormaEmpleado.setEmpleado(nuevoEmpleado);
                                nuevaVigenciaNormaEmpleado.setFechavigencia(fechaIngreso);
                                administrarPersonaIndividual.crearVigenciaNormaEmpleado(nuevaVigenciaNormaEmpleado);
                                //
                                k++;
                                l = BigInteger.valueOf(k);
                                nuevaVigenciaContrato.setSecuencia(l);
                                nuevaVigenciaContrato.setEmpleado(nuevoEmpleado);
                                nuevaVigenciaContrato.setFechainicial(fechaIngreso);
                                administrarPersonaIndividual.crearVigenciaContrato(nuevaVigenciaContrato);
                                //
                                k++;
                                l = BigInteger.valueOf(k);
                                nuevaVigenciaUbicacion.setSecuencia(l);
                                nuevaVigenciaUbicacion.setEmpleado(nuevoEmpleado);
                                nuevaVigenciaUbicacion.setFechavigencia(fechaIngreso);
                                administrarPersonaIndividual.crearVigenciaUbicacion(nuevaVigenciaUbicacion);
                                //
                                k++;
                                l = BigInteger.valueOf(k);
                                nuevaVigenciaJornada.setSecuencia(l);
                                nuevaVigenciaJornada.setEmpleado(nuevoEmpleado);
                                nuevaVigenciaJornada.setFechavigencia(fechaIngreso);
                                administrarPersonaIndividual.crearVigenciaJornada(nuevaVigenciaJornada);
                                //
                                k++;
                                l = BigInteger.valueOf(k);
                                nuevaVigenciaFormaPago.setSecuencia(l);
                                nuevaVigenciaFormaPago.setEmpleado(nuevoEmpleado);
                                nuevaVigenciaFormaPago.setFechavigencia(fechaIngreso);
                                if (nuevaVigenciaFormaPago.getSucursal().getSecuencia() == null) {
                                    nuevaVigenciaFormaPago.setSucursal(null);
                                }
                                administrarPersonaIndividual.crearVigenciaFormaPago(nuevaVigenciaFormaPago);
                                //
                                k++;
                                l = BigInteger.valueOf(k);
                                nuevaVigenciaAfiliacionEPS.setSecuencia(l);
                                nuevaVigenciaAfiliacionEPS.setEmpleado(nuevoEmpleado);
                                nuevaVigenciaAfiliacionEPS.setFechainicial(fechaIngreso);
                                administrarPersonaIndividual.crearVigenciaAfiliacion(nuevaVigenciaAfiliacionEPS);
                                //
                                k++;
                                l = BigInteger.valueOf(k);
                                nuevaVigenciaAfiliacionARP.setSecuencia(l);
                                nuevaVigenciaAfiliacionARP.setEmpleado(nuevoEmpleado);
                                nuevaVigenciaAfiliacionARP.setFechainicial(fechaIngreso);
                                administrarPersonaIndividual.crearVigenciaAfiliacion(nuevaVigenciaAfiliacionARP);
                                //
                                if (nuevaVigenciaAfiliacionAFP.getTercerosucursal().getSecuencia() != null) {
                                    if (!nuevaVigenciaTipoTrabajador.getTipotrabajador().getNombre().contains("SENA")) {
                                        k++;
                                        l = BigInteger.valueOf(k);
                                        nuevaVigenciaAfiliacionAFP.setSecuencia(l);
                                        nuevaVigenciaAfiliacionAFP.setEmpleado(nuevoEmpleado);
                                        nuevaVigenciaAfiliacionAFP.setFechainicial(fechaIngreso);
                                        administrarPersonaIndividual.crearVigenciaAfiliacion(nuevaVigenciaAfiliacionAFP);
                                    }
                                }
                                //
                                if (nuevaVigenciaAfiliacionCaja.getTercerosucursal().getSecuencia() != null) {
                                    if (!nuevaVigenciaTipoTrabajador.getTipotrabajador().getNombre().contains("SENA")) {
                                        k++;
                                        l = BigInteger.valueOf(k);
                                        nuevaVigenciaAfiliacionCaja.setSecuencia(l);
                                        nuevaVigenciaAfiliacionCaja.setEmpleado(nuevoEmpleado);
                                        nuevaVigenciaAfiliacionCaja.setFechainicial(fechaIngreso);
                                        administrarPersonaIndividual.crearVigenciaAfiliacion(nuevaVigenciaAfiliacionCaja);
                                    }
                                }
                                //

                                if (nuevaVigenciaAfiliacionFondo.getTercerosucursal().getSecuencia() != null) {
                                    if (checkIntegral.equalsIgnoreCase("N") && !nuevaVigenciaTipoTrabajador.getTipotrabajador().getNombre().contains("SENA")) {
                                        k++;
                                        l = BigInteger.valueOf(k);
                                        nuevaVigenciaAfiliacionFondo.setSecuencia(l);
                                        nuevaVigenciaAfiliacionFondo.setEmpleado(nuevoEmpleado);
                                        nuevaVigenciaAfiliacionFondo.setFechainicial(fechaIngreso);
                                        administrarPersonaIndividual.crearVigenciaAfiliacion(nuevaVigenciaAfiliacionFondo);
                                    }
                                }

                                if (nuevoEstadoCivil.getSecuencia() != null) {
                                    k++;
                                    l = BigInteger.valueOf(k);
                                    nuevoEstadoCivil.setSecuencia(l);
                                    nuevoEstadoCivil.setPersona(nuevoEmpleado.getPersona());
                                    nuevoEstadoCivil.setFechavigencia(fechaIngreso);
                                    administrarPersonaIndividual.crearEstadoCivil(nuevoEstadoCivil);
                                }

                                if ((nuevaDireccion.getDireccionalternativa() != null && !nuevaDireccion.getDireccionalternativa().isEmpty()) && nuevaDireccion.getCiudad().getSecuencia() != null) {
                                    k++;
                                    l = BigInteger.valueOf(k);
                                    nuevaDireccion.setSecuencia(l);
                                    nuevaDireccion.setTipoppal("O");
                                    nuevaDireccion.setTiposecundario("O");
                                    nuevaDireccion.setPersona(nuevoEmpleado.getPersona());
                                    nuevaDireccion.setFechavigencia(fechaIngreso);
                                    //nuevaDireccion.setPpal(".");
                                    administrarPersonaIndividual.crearDireccion(nuevaDireccion);
                                }

                                if (nuevoTelefono.getCiudad().getSecuencia() != null && nuevoTelefono.getTipotelefono().getSecuencia() != null && nuevoTelefono.getNumerotelefono() > 0) {
                                    k++;
                                    l = BigInteger.valueOf(k);
                                    nuevoTelefono.setSecuencia(l);
                                    nuevoTelefono.setPersona(nuevoEmpleado.getPersona());
                                    nuevoTelefono.setFechavigencia(fechaIngreso);
                                    administrarPersonaIndividual.crearTelefono(nuevoTelefono);
                                }

                                Sets nuevoSet = new Sets();
                                nuevoSet.setEmpleado(nuevoEmpleado);
                                nuevoSet.setFechainicial(fechaIngreso);
                                nuevoSet.setPromedio(new BigDecimal("0.01"));
                                nuevoSet.setTiposet("1");
                                nuevoSet.setPorcentaje(new BigDecimal("0"));
                                k++;
                                l = BigInteger.valueOf(k);
                                nuevoSet.setSecuencia(l);
                                administrarPersonaIndividual.crearSets(nuevoSet);

                                BigDecimal numeroComprobante = administrarPersonaIndividual.obtenerNumeroMaximoComprobante();

                                Comprobantes comprobante = new Comprobantes();
                                comprobante.setEmpleado(nuevoEmpleado);

                                Calendar calendar = Calendar.getInstance();
                                calendar.setTime(fechaIngreso); // Configuramos la fecha que se recibe
                                calendar.add(Calendar.DAY_OF_YEAR, -1);
                                Date fecha = calendar.getTime();

                                comprobante.setFecha(fecha);

                                if (numeroComprobante == null) {
                                    numeroComprobante = new BigDecimal("0");
                                }
                                String valorComprobante = String.valueOf(numeroComprobante.intValue() + 1);
                                comprobante.setNumero(new BigInteger(valorComprobante));
                                comprobante.setValor(new BigDecimal("0.01"));

                                k++;
                                l = BigInteger.valueOf(k);
                                comprobante.setSecuencia(l);

                                administrarPersonaIndividual.crearComprobante(comprobante);

                                Comprobantes comprobanteEmpleado = administrarPersonaIndividual.buscarComprobanteParaPrimerRegistroEmpleado(nuevoEmpleado.getSecuencia());

                                Procesos procesoCodigo1 = null;
                                short cod1 = 1;
                                procesoCodigo1 = administrarPersonaIndividual.buscarProcesoPorCodigo(cod1);
                                Procesos procesoCodigo80 = null;
                                short cod80 = 80;
                                procesoCodigo80 = administrarPersonaIndividual.buscarProcesoPorCodigo(cod80);

                                CortesProcesos corte = new CortesProcesos();
                                corte.setComprobante(comprobanteEmpleado);
                                if (fechaCorte == null) {
                                    corte.setCorte(fecha);
                                } else {
                                    corte.setCorte(fechaCorte);
                                }
                                corte.setEmpleado(nuevoEmpleado);
                                corte.setProceso(procesoCodigo1);
                                k++;
                                l = BigInteger.valueOf(k);
                                corte.setSecuencia(l);
                                administrarPersonaIndividual.crearCortesProcesos(corte);

                                if (procesoCodigo80 != null) {
                                    CortesProcesos corte2 = new CortesProcesos();
                                    corte2.setComprobante(comprobanteEmpleado);
                                    if (fechaCorte == null) {
                                        corte2.setCorte(fecha);
                                    } else {
                                        corte2.setCorte(fechaCorte);
                                    }
                                    corte2.setEmpleado(nuevoEmpleado);
                                    corte2.setProceso(procesoCodigo80);
                                    k++;
                                    l = BigInteger.valueOf(k);
                                    corte2.setSecuencia(l);
                                    administrarPersonaIndividual.crearCortesProcesos(corte2);
                                }

                                short cod12 = 12;
                                TiposTrabajadores codigo12 = administrarPersonaIndividual.buscarTipoTrabajadorPorCodigo(cod12);
                                VigenciasTiposTrabajadores nuevaVigenciaTT = new VigenciasTiposTrabajadores();
                                nuevaVigenciaTT.setEmpleado(nuevoEmpleado);
                                Date fechaNuevo = new Date(1, 1, 60);
                                nuevaVigenciaTT.setFechavigencia(fechaNuevo);
                                nuevaVigenciaTT.setTipotrabajador(codigo12);
                                k++;
                                l = BigInteger.valueOf(k);
                                nuevaVigenciaTT.setSecuencia(l);
                                administrarPersonaIndividual.crearVigenciaTipoTrabajador(nuevaVigenciaTT);
                                PrimefacesContextUI.ejecutar("PF('procesoGuardadoOK').show()");
                                cancelarModificaciones();
                            } else {
                                System.err.println("No trajo el nuevoEmpleado");
                            }
                        } else {
                            System.err.println("No trajo la ultima persona creada para crear Empleado");
                        }
                    } else {
                        PrimefacesContextUI.ejecutar("PF('errorCamposAlternativos').show()");
                    }
                } else {
                    mensajeErrorFechasEmpleado = "Existe un error en las siguientes fechas: " + mensajeErrorFechasEmpleado;
                    PrimefacesContextUI.actualizar("formDialogos:errorFechasEmpleado");
                    PrimefacesContextUI.ejecutar("PF('errorFechasEmpleado').show()");
                }
            } else {
                PrimefacesContextUI.ejecutar("PF('errorCamposObligatorios').show()");
            }
        }
    }

    public void cambiarItemInformacionPersonal(int iiii) {
        System.out.println("cambiarItemInformacionPersonal : " + iiii);
        if (permitirIndexInformacionPersonal == true) {
            idCargoDesempeñado = -1;
            idCentroCosto = -1;
            idTipoTrabajador = -1;
            idTipoSalario = -1;
            idSueldo = -1;
            idTipoContrato = -1;
            idNormaLaboral = -1;
            idLegislacionLaboral = -1;
            idUbicacionGeografica = -1;
            idFormaPago = -1;
            idJornadaLaboral = -1;
            idAfiliacionAFP = -1;
            idAfiliacionARP = -1;
            idAfiliacionCaja = -1;
            idAfiliacionEPS = -1;
            idAfiliacionFondo = -1;
            idEstadoCivil = -1;
            idDireccion = -1;
            idTelefono = -1;
            idInformacionPersonal = iiii;

            auxFechaCorte = fechaCorte;
            auxFechaIngreso = fechaIngreso;
            auxFechaNacimiento = nuevaPersona.getFechanacimiento();
            if (idInformacionPersonal == 0) {
                if (nuevoEmpleado.getEmpresa().getSecuencia() != null) {
                    auxInformacionPersonaEmpresal = nuevoEmpleado.getEmpresa().getNombre();
                    /*
                     * if (nuevoEmpleado.getEmpresa() != null) {
                     * auxInformacionPersonaEmpresal =
                     * administrarPersonaIndividual.obtenerEmpresa(nuevoEmpleado.getEmpresa()).getNombre();
                     */
                } else {
                    listaValoresInformacionPersonal();
                }
            }
            if (idInformacionPersonal == 5) {
                if (nuevaPersona.getTipodocumento().getSecuencia() != null) {
                    auxInformacionPersonalTipoDocumento = nuevaPersona.getTipodocumento().getNombrelargo();
                } else {
                    listaValoresInformacionPersonal();
                }
            }
            if (idInformacionPersonal == 7) {
                if (nuevaPersona.getCiudaddocumento().getSecuencia() != null) {
                    auxInformacionPersonalCiudadDocumento = nuevaPersona.getCiudaddocumento().getNombre();
                } else {
                    listaValoresInformacionPersonal();
                }
            }
            if (idInformacionPersonal == 9) {
                if (nuevaPersona.getCiudadnacimiento().getSecuencia() != null) {
                    auxInformacionPersonalCiudadNacimiento = nuevaPersona.getCiudadnacimiento().getNombre();
                } else {
                    listaValoresInformacionPersonal();
                }
            }

        }
    }

    public void cambiarItemCargoDesempeñado(int itemCargoD) {
        if (permitirIndexCargoDesempeñado == true) {
            idInformacionPersonal = -1;
            idCentroCosto = -1;
            idTipoTrabajador = -1;
            idTipoSalario = -1;
            idSueldo = -1;
            idTipoContrato = -1;
            idNormaLaboral = -1;
            idLegislacionLaboral = -1;
            idUbicacionGeografica = -1;
            idFormaPago = -1;
            idJornadaLaboral = -1;
            idAfiliacionAFP = -1;
            idAfiliacionARP = -1;
            idAfiliacionCaja = -1;
            idAfiliacionEPS = -1;
            idAfiliacionFondo = -1;
            idEstadoCivil = -1;
            idDireccion = -1;
            idTelefono = -1;
            idCargoDesempeñado = itemCargoD;
            auxCargoDesempeñadoPapel = nuevaVigenciaCargo.getPapel().getDescripcion();
            auxCargoDesempeñadoEmpleadoJefe = nuevaVigenciaCargo.getEmpleadojefe().getPersona().getNombreCompleto();
            if (idCargoDesempeñado == 0) {
                if (nuevaVigenciaCargo.getCargo().getSecuencia() != null) {
                    auxCargoDesempeñadoNombreCargo = nuevaVigenciaCargo.getCargo().getNombre();
                } else {
                    listaValoresCargoDesempenado();
                }
            }
            if (idCargoDesempeñado == 1) {
                if (nuevaVigenciaCargo.getMotivocambiocargo().getSecuencia() != null) {
                    auxCargoDesempeñadoMotivoCargo = nuevaVigenciaCargo.getMotivocambiocargo().getNombre();
                } else {
                    listaValoresCargoDesempenado();
                }
            }
            if (idCargoDesempeñado == 2) {
                if (nuevaVigenciaCargo.getEstructura().getSecuencia() != null) {
                    auxCargoDesempeñadoNombreEstructura = nuevaVigenciaCargo.getEstructura().getNombre();
                } else {
                    listaValoresCargoDesempenado();
                }
            }

        }
    }

    public void cambiarItemCentroCosto(int i) {
        if (permitirIndexCentroCosto == true) {
            idInformacionPersonal = -1;
            idCargoDesempeñado = -1;
            idTipoTrabajador = -1;
            idTipoSalario = -1;
            idSueldo = -1;
            idTipoContrato = -1;
            idNormaLaboral = -1;
            idLegislacionLaboral = -1;
            idUbicacionGeografica = -1;
            idFormaPago = -1;
            idJornadaLaboral = -1;
            idAfiliacionAFP = -1;
            idAfiliacionARP = -1;
            idAfiliacionCaja = -1;
            idAfiliacionEPS = -1;
            idAfiliacionFondo = -1;
            idEstadoCivil = -1;
            idDireccion = -1;
            idTelefono = -1;
            idCentroCosto = i;
            if (idCentroCosto == 0) {
                if (nuevaVigenciaLocalizacion.getMotivo().getSecuencia() != null) {
                    auxCentroCostoMotivo = nuevaVigenciaLocalizacion.getMotivo().getDescripcion();
                } else {
                    listaValoresCentroCosto();
                }
            }
            if (idCentroCosto == 1) {
                if (nuevaVigenciaLocalizacion.getLocalizacion().getSecuencia() != null) {
                    auxCentroCostoEstructura = nuevaVigenciaLocalizacion.getLocalizacion().getCentrocosto().getNombre();
                } else {
                    listaValoresCentroCosto();
                }
            }
        }
    }

    public void cambiarItemTipoTrabajador(int i) {
        if (permitirIndexTipoTrabajador == true) {
            idInformacionPersonal = -1;
            idCargoDesempeñado = -1;
            idCentroCosto = -1;
            idTipoSalario = -1;
            idSueldo = -1;
            idTipoContrato = -1;
            idNormaLaboral = -1;
            idLegislacionLaboral = -1;
            idUbicacionGeografica = -1;
            idFormaPago = -1;
            idJornadaLaboral = -1;
            idAfiliacionAFP = -1;
            idAfiliacionARP = -1;
            idAfiliacionCaja = -1;
            idAfiliacionEPS = -1;
            idAfiliacionFondo = -1;
            idEstadoCivil = -1;
            idDireccion = -1;
            idTelefono = -1;
            idTipoTrabajador = i;
            if (idTipoTrabajador == 0) {
                if (nuevaVigenciaTipoTrabajador.getTipotrabajador().getSecuencia() != null) {
                    auxTipoTrabajadorNombreTipoTrabajador = nuevaVigenciaTipoTrabajador.getTipotrabajador().getNombre();
                } else {
                    listaValoresTipoTrabajador();
                }
            }
        }
    }

    public void cambiarItemReformaLaboral(int i) {
        if (permitirIndexTipoSalario == true) {
            idInformacionPersonal = -1;
            idCargoDesempeñado = -1;
            idCentroCosto = -1;
            idTipoTrabajador = -1;
            idSueldo = -1;
            idTipoContrato = -1;
            idNormaLaboral = -1;
            idLegislacionLaboral = -1;
            idUbicacionGeografica = -1;
            idFormaPago = -1;
            idJornadaLaboral = -1;
            idAfiliacionAFP = -1;
            idAfiliacionARP = -1;
            idAfiliacionCaja = -1;
            idAfiliacionEPS = -1;
            idAfiliacionFondo = -1;
            idEstadoCivil = -1;
            idDireccion = -1;
            idTelefono = -1;
            idTipoSalario = i;
            if (idTipoSalario == 0) {
                if (nuevaVigenciaReformaLaboral.getReformalaboral().getNombre() != null) {
                    auxTipoSalarioReformaLaboral = nuevaVigenciaReformaLaboral.getReformalaboral().getNombre();
                } else {
                    listaValoresTipoSalario();
                }
            }
        }
    }

    public void cambiarItemSueldo(int i) {
        if (permitirIndexSueldo == true) {
            idInformacionPersonal = -1;
            idCargoDesempeñado = -1;
            idCentroCosto = -1;
            idTipoTrabajador = -1;
            idTipoSalario = -1;
            idTipoContrato = -1;
            idNormaLaboral = -1;
            idLegislacionLaboral = -1;
            idUbicacionGeografica = -1;
            idFormaPago = -1;
            idJornadaLaboral = -1;
            idAfiliacionAFP = -1;
            idAfiliacionARP = -1;
            idAfiliacionCaja = -1;
            idAfiliacionEPS = -1;
            idAfiliacionFondo = -1;
            idEstadoCivil = -1;
            idDireccion = -1;
            idTelefono = -1;
            idSueldo = i;
            auxSueldoValor = valorSueldo;
            if (idSueldo == 1) {
                if (nuevaVigenciaSueldo.getMotivocambiosueldo().getSecuencia() != null) {
                    auxSueldoMotivoSueldo = nuevaVigenciaSueldo.getMotivocambiosueldo().getNombre();
                } else {
                    listaValoresSueldo();
                }
            }
            if (idSueldo == 2) {
                if (nuevaVigenciaSueldo.getTiposueldo().getSecuencia() != null) {
                    auxSueldoTipoSueldo = nuevaVigenciaSueldo.getTiposueldo().getDescripcion();
                } else {
                    listaValoresSueldo();
                }
            }
            if (idSueldo == 3) {
                if (nuevaVigenciaSueldo.getUnidadpago().getSecuencia() != null) {
                    auxSueldoUnidad = nuevaVigenciaSueldo.getUnidadpago().getNombre();
                } else {
                    listaValoresSueldo();
                }
            }
        }
    }

    public void cambiarItemTipoContrato(int i) {
        if (permitirIndexTipoContrato == true) {
            idInformacionPersonal = -1;
            idCargoDesempeñado = -1;
            idCentroCosto = -1;
            idTipoTrabajador = -1;
            idTipoSalario = -1;
            idSueldo = -1;
            idNormaLaboral = -1;
            idLegislacionLaboral = -1;
            idUbicacionGeografica = -1;
            idFormaPago = -1;
            idJornadaLaboral = -1;
            idAfiliacionAFP = -1;
            idAfiliacionARP = -1;
            idAfiliacionCaja = -1;
            idAfiliacionEPS = -1;
            idAfiliacionFondo = -1;
            idEstadoCivil = -1;
            idDireccion = -1;
            idTelefono = -1;
            idTipoContrato = i;
            auxTipoContratoFecha = nuevaVigenciaTipoContrato.getFechavigencia();
            if (idTipoContrato == 1) {
                if (nuevaVigenciaTipoContrato.getTipocontrato().getSecuencia() != null) {
                    auxTipoContratoMotivo = nuevaVigenciaTipoContrato.getTipocontrato().getNombre();
                } else {
                    listaValoresTipoContrato();
                }
            }
            if (idTipoContrato == 0) {
                if (nuevaVigenciaTipoContrato.getMotivocontrato().getSecuencia() != null) {
                    auxTipoContratoTipoContrato = nuevaVigenciaTipoContrato.getMotivocontrato().getNombre();
                } else {
                    listaValoresTipoContrato();
                }
            }
        }
    }

    public void cambiarItemNormaLaboral(int i) {
        if (permitirIndexNormaLaboral == true) {
            idInformacionPersonal = -1;
            idCargoDesempeñado = -1;
            idCentroCosto = -1;
            idTipoTrabajador = -1;
            idTipoSalario = -1;
            idSueldo = -1;
            idTipoContrato = -1;
            idLegislacionLaboral = -1;
            idUbicacionGeografica = -1;
            idFormaPago = -1;
            idJornadaLaboral = -1;
            idAfiliacionAFP = -1;
            idAfiliacionARP = -1;
            idAfiliacionCaja = -1;
            idAfiliacionEPS = -1;
            idAfiliacionFondo = -1;
            idEstadoCivil = -1;
            idDireccion = -1;
            idTelefono = -1;
            idNormaLaboral = i;
            if (idNormaLaboral == 0) {
                if (nuevaVigenciaNormaEmpleado.getNormalaboral().getSecuencia() != null) {
                    auxNormaLaboralNorma = nuevaVigenciaNormaEmpleado.getNormalaboral().getNombre();
                } else {
                    listaValoresNormaLaboral();
                }
            }
        }
    }

    public void cambiarItemLegislacionLaboral(int i) {
        if (permitirIndexLegislacionLaboral == true) {
            idInformacionPersonal = -1;
            idCargoDesempeñado = -1;
            idCentroCosto = -1;
            idTipoTrabajador = -1;
            idTipoSalario = -1;
            idSueldo = -1;
            idTipoContrato = -1;
            idNormaLaboral = -1;
            idUbicacionGeografica = -1;
            idFormaPago = -1;
            idJornadaLaboral = -1;
            idAfiliacionAFP = -1;
            idAfiliacionARP = -1;
            idAfiliacionCaja = -1;
            idAfiliacionEPS = -1;
            idAfiliacionFondo = -1;
            idEstadoCivil = -1;
            idDireccion = -1;
            idTelefono = -1;
            idLegislacionLaboral = i;
            if (idLegislacionLaboral == 0) {
                if (nuevaVigenciaContrato.getContrato().getSecuencia() != null) {
                    auxLegislacionLaboralNombre = nuevaVigenciaContrato.getContrato().getDescripcion();
                } else {
                    listaValoresLegislacionLaboral();
                }
            }
        }
    }

    public void cambiarItemUbicacionGeografica(int i) {
        if (permitirIndexUbicacionGeografica == true) {
            idInformacionPersonal = -1;
            idCargoDesempeñado = -1;
            idCentroCosto = -1;
            idTipoTrabajador = -1;
            idTipoSalario = -1;
            idSueldo = -1;
            idTipoContrato = -1;
            idNormaLaboral = -1;
            idLegislacionLaboral = -1;
            idFormaPago = -1;
            idJornadaLaboral = -1;
            idAfiliacionAFP = -1;
            idAfiliacionARP = -1;
            idAfiliacionCaja = -1;
            idAfiliacionEPS = -1;
            idAfiliacionFondo = -1;
            idEstadoCivil = -1;
            idDireccion = -1;
            idTelefono = -1;
            idUbicacionGeografica = i;
            if (idUbicacionGeografica == 0) {
                if (nuevaVigenciaUbicacion.getUbicacion().getSecuencia() != null) {
                    auxUbicacionGeograficaUbicacion = nuevaVigenciaUbicacion.getUbicacion().getDescripcion();
                } else {
                    listaValoresUbicacion();
                }
            }
        }
    }

    public void cambiarItemFormaPago(int i) {
        if (permitirIndexFormaPago == true) {
            idInformacionPersonal = -1;
            idCargoDesempeñado = -1;
            idCentroCosto = -1;
            idTipoTrabajador = -1;
            idTipoSalario = -1;
            idSueldo = -1;
            idTipoContrato = -1;
            idNormaLaboral = -1;
            idLegislacionLaboral = -1;
            idUbicacionGeografica = -1;
            idJornadaLaboral = -1;
            idAfiliacionAFP = -1;
            idAfiliacionARP = -1;
            idAfiliacionCaja = -1;
            idAfiliacionEPS = -1;
            idAfiliacionFondo = -1;
            idEstadoCivil = -1;
            idDireccion = -1;
            idTelefono = -1;
            idFormaPago = i;
            if (idFormaPago == 0) {
                if (nuevaVigenciaFormaPago.getFormapago().getSecuencia() != null) {
                    auxFormaPagoPeriodicidad = nuevaVigenciaFormaPago.getFormapago().getNombre();
                } else {
                    listaValoresFormaPago();
                }
            }
            if (idFormaPago == 3) {
                if (nuevaVigenciaFormaPago.getSucursal().getSecuencia() != null) {
                    auxFormaPagoSucursal = nuevaVigenciaFormaPago.getSucursal().getNombre();
                } else {
                    listaValoresFormaPago();
                }
            }
            if (idFormaPago == 4) {
                if (nuevaVigenciaFormaPago.getMetodopago().getSecuencia() != null) {
                    auxFormaPagoMetodo = nuevaVigenciaFormaPago.getMetodopago().getDescripcion();
                } else {
                    listaValoresFormaPago();
                }
            }
        }
    }

    public void cambiarItemJornadaLaboral(int i) {
        if (permitirIndexJornadaLaboral == true) {
            idInformacionPersonal = -1;
            idCargoDesempeñado = -1;
            idCentroCosto = -1;
            idTipoTrabajador = -1;
            idTipoSalario = -1;
            idSueldo = -1;
            idTipoContrato = -1;
            idNormaLaboral = -1;
            idLegislacionLaboral = -1;
            idUbicacionGeografica = -1;
            idFormaPago = -1;
            idAfiliacionAFP = -1;
            idAfiliacionARP = -1;
            idAfiliacionCaja = -1;
            idAfiliacionEPS = -1;
            idAfiliacionFondo = -1;
            idEstadoCivil = -1;
            idDireccion = -1;
            idTelefono = -1;
            idJornadaLaboral = i;
            if (idJornadaLaboral == 0) {
                if (nuevaVigenciaJornada.getJornadatrabajo().getSecuencia() != null) {
                    auxJornadaLaboralJornada = nuevaVigenciaJornada.getJornadatrabajo().getDescripcion();
                } else {
                    listaValoresJornadaLaboral();
                }
            }
        }
    }

    public void cambiarItemAfiliacionFondo(int i) {
        if (permitirIndexAfiliacionFondo == true) {
            idInformacionPersonal = -1;
            idCargoDesempeñado = -1;
            idCentroCosto = -1;
            idTipoTrabajador = -1;
            idTipoSalario = -1;
            idSueldo = -1;
            idTipoContrato = -1;
            idNormaLaboral = -1;
            idLegislacionLaboral = -1;
            idUbicacionGeografica = -1;
            idFormaPago = -1;
            idAfiliacionAFP = -1;
            idAfiliacionARP = -1;
            idAfiliacionCaja = -1;
            idAfiliacionEPS = -1;
            idJornadaLaboral = -1;
            idEstadoCivil = -1;
            idDireccion = -1;
            idTelefono = -1;
            idAfiliacionFondo = i;

            auxAfiliacionFondo = nuevaVigenciaAfiliacionFondo.getTercerosucursal().getDescripcion();
        }
    }

    public void cambiarItemAfiliacionEPS(int i) {
        if (permitirIndexAfiliacionEPS == true) {
            idInformacionPersonal = -1;
            idCargoDesempeñado = -1;
            idCentroCosto = -1;
            idTipoTrabajador = -1;
            idTipoSalario = -1;
            idSueldo = -1;
            idTipoContrato = -1;
            idNormaLaboral = -1;
            idLegislacionLaboral = -1;
            idUbicacionGeografica = -1;
            idFormaPago = -1;
            idAfiliacionAFP = -1;
            idAfiliacionARP = -1;
            idAfiliacionCaja = -1;
            idJornadaLaboral = -1;
            idAfiliacionFondo = -1;
            idEstadoCivil = -1;
            idDireccion = -1;
            idTelefono = -1;
            idAfiliacionEPS = i;
            if (idAfiliacionEPS == 0) {
                if (nuevaVigenciaAfiliacionEPS.getTercerosucursal().getSecuencia() != null) {
                    auxAfiliacionEPS = nuevaVigenciaAfiliacionEPS.getTercerosucursal().getDescripcion();
                } else {
                    listaValoresAfiliacionEPS();
                }
            }
        }
    }

    public void cambiarItemAfiliacionCaja(int i) {
        if (permitirIndexAfiliacionCaja == true) {
            idInformacionPersonal = -1;
            idCargoDesempeñado = -1;
            idCentroCosto = -1;
            idTipoTrabajador = -1;
            idTipoSalario = -1;
            idSueldo = -1;
            idTipoContrato = -1;
            idNormaLaboral = -1;
            idLegislacionLaboral = -1;
            idUbicacionGeografica = -1;
            idFormaPago = -1;
            idAfiliacionAFP = -1;
            idAfiliacionARP = -1;
            idJornadaLaboral = -1;
            idAfiliacionFondo = -1;
            idAfiliacionEPS = -1;
            idEstadoCivil = -1;
            idDireccion = -1;
            idTelefono = -1;
            idAfiliacionCaja = i;
            auxAfiliacionCaja = nuevaVigenciaAfiliacionCaja.getTercerosucursal().getDescripcion();
        }
    }

    public void cambiarItemAfiliacionARP(int i) {
        if (permitirIndexAfiliacionARP == true) {
            idInformacionPersonal = -1;
            idCargoDesempeñado = -1;
            idCentroCosto = -1;
            idTipoTrabajador = -1;
            idTipoSalario = -1;
            idSueldo = -1;
            idTipoContrato = -1;
            idNormaLaboral = -1;
            idLegislacionLaboral = -1;
            idUbicacionGeografica = -1;
            idFormaPago = -1;
            idAfiliacionAFP = -1;
            idJornadaLaboral = -1;
            idAfiliacionFondo = -1;
            idAfiliacionEPS = -1;
            idAfiliacionCaja = -1;
            idEstadoCivil = -1;
            idDireccion = -1;
            idTelefono = -1;
            idAfiliacionARP = i;
            if (idAfiliacionARP == 0) {
                if (nuevaVigenciaAfiliacionARP.getTercerosucursal().getSecuencia() != null) {
                    auxAfiliacionARP = nuevaVigenciaAfiliacionARP.getTercerosucursal().getDescripcion();
                } else {
                    listaValoresAfiliacionARP();
                }
            }
        }
    }

    public void cambiarItemAfiliacionAFP(int i) {
        if (permitirIndexAfiliacionAFP == true) {
            idInformacionPersonal = -1;
            idCargoDesempeñado = -1;
            idCentroCosto = -1;
            idTipoTrabajador = -1;
            idTipoSalario = -1;
            idSueldo = -1;
            idTipoContrato = -1;
            idNormaLaboral = -1;
            idLegislacionLaboral = -1;
            idUbicacionGeografica = -1;
            idFormaPago = -1;
            idJornadaLaboral = -1;
            idAfiliacionFondo = -1;
            idAfiliacionEPS = -1;
            idAfiliacionCaja = -1;
            idAfiliacionARP = -1;
            idEstadoCivil = -1;
            idDireccion = -1;
            idTelefono = -1;
            idAfiliacionAFP = i;
            auxAfiliacionAFP = nuevaVigenciaAfiliacionAFP.getTercerosucursal().getDescripcion();
        }
    }

    public void cambiarItemEstadoCivil(int i) {
        if (permitirIndexEstadoCivil == true) {
            idInformacionPersonal = -1;
            idCargoDesempeñado = -1;
            idCentroCosto = -1;
            idTipoTrabajador = -1;
            idTipoSalario = -1;
            idSueldo = -1;
            idTipoContrato = -1;
            idNormaLaboral = -1;
            idLegislacionLaboral = -1;
            idUbicacionGeografica = -1;
            idFormaPago = -1;
            idJornadaLaboral = -1;
            idAfiliacionFondo = -1;
            idAfiliacionEPS = -1;
            idAfiliacionCaja = -1;
            idAfiliacionARP = -1;
            idAfiliacionAFP = -1;
            idDireccion = -1;
            idTelefono = -1;
            idEstadoCivil = i;
            auxEstadoCivilEstado = nuevoEstadoCivil.getEstadocivil().getDescripcion();
        }
    }

    public void cambiarItemDireccion(int i) {
        if (permitirIndexDireccion == true) {
            idInformacionPersonal = -1;
            idCargoDesempeñado = -1;
            idCentroCosto = -1;
            idTipoTrabajador = -1;
            idTipoSalario = -1;
            idSueldo = -1;
            idTipoContrato = -1;
            idNormaLaboral = -1;
            idLegislacionLaboral = -1;
            idUbicacionGeografica = -1;
            idFormaPago = -1;
            idJornadaLaboral = -1;
            idAfiliacionFondo = -1;
            idAfiliacionEPS = -1;
            idAfiliacionCaja = -1;
            idAfiliacionARP = -1;
            idAfiliacionAFP = -1;
            idEstadoCivil = -1;
            idTelefono = -1;
            idDireccion = i;
            auxDireccionCiudad = nuevaDireccion.getCiudad().getNombre();
        }
    }

    public void cambiarItemTelefono(int i) {
        if (permitirIndexTelefono == true) {
            idInformacionPersonal = -1;
            idCargoDesempeñado = -1;
            idCentroCosto = -1;
            idTipoTrabajador = -1;
            idTipoSalario = -1;
            idSueldo = -1;
            idTipoContrato = -1;
            idNormaLaboral = -1;
            idLegislacionLaboral = -1;
            idUbicacionGeografica = -1;
            idFormaPago = -1;
            idJornadaLaboral = -1;
            idAfiliacionFondo = -1;
            idAfiliacionEPS = -1;
            idAfiliacionCaja = -1;
            idAfiliacionARP = -1;
            idAfiliacionAFP = -1;
            idEstadoCivil = -1;
            idDireccion = -1;
            idTelefono = i;
            auxTelefonoTipo = nuevoTelefono.getTipotelefono().getNombre();
            auxTelefonoCiudad = nuevoTelefono.getCiudad().getNombre();
        }
    }

    public void modificarCodigoEmpleado() {
        Empleados empleado = null;
        RequestContext context = RequestContext.getCurrentInstance();
        if (nuevoEmpleado.getEmpresa().getSecuencia() != null) {
            empleado = administrarPersonaIndividual.buscarEmpleadoPorCodigoyEmpresa(nuevoEmpleado.getCodigoempleado(), nuevoEmpleado.getEmpresa().getSecuencia());
            /*
             * if (nuevoEmpleado.getEmpresa() != null) { empleado =
             * administrarPersonaIndividual.buscarEmpleadoPorCodigoyEmpresa(nuevoEmpleado.getCodigoempleado(),
             * nuevoEmpleado.getEmpresa());
             */
        } else {
            empleado = administrarPersonaIndividual.buscarEmpleadoPorCodigoyEmpresa(nuevoEmpleado.getCodigoempleado(), null);
        }
        if (empleado != null) {
            PrimefacesContextUI.ejecutar("PF('errorEmpleadoDuplicado').show()");
        }
    }

    public void modificarNumeroDocumentoPersona() {
        RequestContext context = RequestContext.getCurrentInstance();
        Personas persona = administrarPersonaIndividual.buscarPersonaPorNumeroDocumento(nuevaPersona.getNumerodocumento());
        Empleados empleado = null;
        //if (nuevoEmpleado.getEmpresa().getSecuencia() != null) {
        //empleado = administrarPersonaIndividual.buscarEmpleadoPorCodigoyEmpresa(nuevaPersona.getNumerodocumento(), nuevoEmpleado.getEmpresa().getSecuencia());
        //if (nuevoEmpleado.getEmpresa() != null) {
        //empleado = administrarPersonaIndividual.buscarEmpleadoPorCodigoyEmpresa(nuevaPersona.getNumerodocumento(), nuevoEmpleado.getEmpresa());
        if (nuevoEmpleado.getEmpresa() != null) {
            empleado = administrarPersonaIndividual.buscarEmpleadoPorCodigoyEmpresa(new BigDecimal(nuevaPersona.getNumerodocumento()), nuevoEmpleado.getEmpresa().getSecuencia());
        } else {
            empleado = administrarPersonaIndividual.buscarEmpleadoPorCodigoyEmpresa(new BigDecimal(nuevaPersona.getNumerodocumento()), null);
        }
        if (persona != null && empleado != null) {
            nuevaPersona.setNumerodocumento(null);
            PrimefacesContextUI.actualizar("form:numeroDocumentoModPersonal");
            PrimefacesContextUI.ejecutar("PF('errorEmpleadoRegistrado').show()");
        } else {
            if (persona != null && empleado == null) {
                nuevaPersona.setNumerodocumento(null);
                PrimefacesContextUI.actualizar("form:numeroDocumentoModPersonal");
                PrimefacesContextUI.ejecutar("PF('errorPersonaRepetida').show()");
//                PrimefacesContextUI.ejecutar("PF('CiudadDocumentoInformacionPersonalDialogo').hide()");
            }
            String contabilidad = administrarPersonaIndividual.obtenerPreValidadContabilidad();
            String bloqueAIngreso = administrarPersonaIndividual.obtenerPreValidaBloqueAIngreso();
            if (contabilidad != null) {
                if (contabilidad.equalsIgnoreCase("S")) {
                    VWValidaBancos valida = administrarPersonaIndividual.validarCodigoPrimarioVWValidaBancos(nuevaPersona.getNumerodocumento());
                    if (valida == null) {
                        //¡Esta ingresando un codigo de Tercero (nit de empleado) que no existe en el sistema contable. Por favor, antes de ingresar este registro en nómina, valide primero con contabilidad!.
                        PrimefacesContextUI.ejecutar("PF('errorCodigoTerceroPersona').show()");
                    }
                    if (bloqueAIngreso.equalsIgnoreCase("S")) {
                        //¡¡¡No se puede ingresar el registro en Designer.RHN!!! Por favor salga de esta pantalla.
                        PrimefacesContextUI.ejecutar("PF('errorIngresoTotalRegistro').show()");
                    }
                }
            }
        }
        if (nuevaPersona.getNumerodocumento() != null) {
            nuevoEmpleado.setCodigoempleado(new BigDecimal(nuevaPersona.getNumerodocumento()));
        }
        PrimefacesContextUI.actualizar("form:numeroDocumentoModPersonal");
        PrimefacesContextUI.actualizar("form:codigoEmpleadoModPersonal");
    }

    public boolean validarFechasInformacionPersonal(int i) {
        fechaParametro = new Date();
        fechaParametro.setYear(0);
        fechaParametro.setMonth(1);
        fechaParametro.setDate(1);
        boolean retorno = true;
        if (i == 8) {
            if (nuevaPersona.getFechanacimiento() != null) {
                if (nuevaPersona.getFechanacimiento().after(fechaParametro)) {
                    retorno = true;
                } else {
                    retorno = false;
                }
            }
        }
        if (i == 10) {
            if (fechaIngreso != null) {
                if (fechaIngreso.after(fechaParametro)) {
                    retorno = true;
                } else {
                    retorno = false;
                }
            }
        }
        if (i == 13) {
            if (fechaCorte != null) {
                if (fechaCorte.after(fechaParametro)) {
                    retorno = true;
                } else {
                    retorno = false;
                }
            }
        }
        return retorno;
    }

    public boolean validarFechasTipoContrato() {
        fechaParametro = new Date();
        fechaParametro.setYear(0);
        fechaParametro.setMonth(1);
        fechaParametro.setDate(1);
        boolean retorno = true;
        if (nuevaVigenciaTipoContrato.getFechavigencia() != null) {
            if (nuevaVigenciaTipoContrato.getFechavigencia().after(fechaParametro)) {
                retorno = true;
            } else {
                retorno = false;
            }
        }
        return retorno;
    }

    public void modificarFechaNacimientoInformacionPersonal(int i) {
        System.out.println("fecha Nacimiento : " + nuevaPersona.getFechanacimiento());
        boolean retorno = validarFechasInformacionPersonal(8);
        if (retorno == true) {
            cambiarItemInformacionPersonal(i);
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            PrimefacesContextUI.ejecutar("PF('errorFechas').show()");
        }
    }

    public void arreglarFecha() {
        FacesContext c = FacesContext.getCurrentInstance();
        Calendar calend;
        Object obj;
        obj = c.getViewRoot().findComponent("form:fechaIngresoModPersonal");
        calend = (Calendar) obj;
        System.out.println("calend" + calend);
    }

    public void modificarFechaIngresoInformacionPersonal(int i) {
        System.out.println("fechaIngreso : " + fechaIngreso);
        RequestContext context = RequestContext.getCurrentInstance();
        boolean retorno = validarFechasInformacionPersonal(10);
        System.out.println("retorno : " + retorno);
        if (retorno == true) {
            if (fechaIngreso != null) {
                cambiarItemInformacionPersonal(i);
            }
            modificacionesEmpresaFechaIngresoInformacionPersonal();
        } else {
            PrimefacesContextUI.ejecutar("PF('errorFechas').show()");
            modificacionesEmpresaFechaIngresoInformacionPersonal();
        }
    }

    public void modificarFechaUltimoPagoInformacionPersonal(int i) {
        boolean retorno = validarFechasInformacionPersonal(13);
        if (retorno == true) {
            cambiarItemInformacionPersonal(i);
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            PrimefacesContextUI.ejecutar("PF('errorFechas').show()");
        }
    }

    public void posicionFechas() {
        System.out.println("posicionFechas");
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, String> map = context.getExternalContext().getRequestParameterMap();
        String name = map.get("n"); // name attribute of node
        int posicion = Integer.parseInt(name);
        cambiarItemInformacionPersonal(posicion);
    }

    public void modificarFechaFechaVigenciaTipoContrato(int i) {
        boolean retorno = validarFechasTipoContrato();
        if (retorno == true) {
            if (fechaIngreso != null && nuevaVigenciaTipoContrato.getFechavigencia() != null) {
                if (nuevaVigenciaTipoContrato.getFechavigencia().before(fechaIngreso)) {
                    RequestContext context = RequestContext.getCurrentInstance();
                    PrimefacesContextUI.ejecutar("PF('errorFechaContratoFechaIngreso').show()");
                } else {
                    cambiarItemTipoContrato(i);
                }
            }
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            PrimefacesContextUI.ejecutar("PF('errorFechas').show()");
        }
    }

    public void modificarInformacionPersonal(int indice, String campo, String valor) {
        System.out.println("modificarInformacionPersonal()");
        System.out.println("indice : " + indice + ", campo : " + campo + ", valor : " + valor);
        cargarLovCiudades();
        cargarLovTiposDocumentos();
        idInformacionPersonal = indice;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (campo.equalsIgnoreCase("EMPRESA")) {
            nuevoEmpleado.getEmpresa().setNombre(getAuxInformacionPersonaEmpresal());
            //administrarPersonaIndividual.obtenerEmpresa(nuevoEmpleado.getEmpresa()).setNombre(auxInformacionPersonaEmpresal);
            if (lovEmpresas != null) {
                for (int i = 0; i < lovEmpresas.size(); i++) {
                    if (lovEmpresas.get(i).getNombre().startsWith(valor.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
            }
            if (coincidencias == 1) {
                nuevoEmpleado.setEmpresa(lovEmpresas.get(indiceUnicoElemento));
                PrimefacesContextUI.actualizar("form:empresaModPersonal");
                calcularControlEmpleadosEmpresa();
                modificacionesEmpresaFechaIngresoInformacionPersonal();
            } else {
                permitirIndexInformacionPersonal = false;
                PrimefacesContextUI.actualizar("formLovs:formDInformacionPersonal:formEmpresa:EmpresaInformacionPersonalDialogo");
                modificarInfoR_EmpresaInfoP(lovEmpresas.size());
                PrimefacesContextUI.ejecutar("PF('EmpresaInformacionPersonalDialogo').show()");
                PrimefacesContextUI.actualizar("form:empresaModPersonal");
            }
        }
        if (campo.equalsIgnoreCase("TIPODOCUMENTO")) {
            nuevaPersona.getTipodocumento().setNombrelargo(getAuxInformacionPersonalTipoDocumento());
            cargarLovTiposDocumentos();
            if (lovTiposDocumentos != null) {
                for (int i = 0; i < lovTiposDocumentos.size(); i++) {
                    if (lovTiposDocumentos.get(i).getNombrelargo().startsWith(valor.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
            }
            if (coincidencias == 1) {
                nuevaPersona.setTipodocumento(lovTiposDocumentos.get(indiceUnicoElemento));
                txt_tipoDoc = nuevaPersona.getTipodocumento().getNombrelargo();
                PrimefacesContextUI.actualizar("form:tipoDocumentoModPersonal");
            } else {
                txt_tipoDoc = "";
                permitirIndexInformacionPersonal = false;
                PrimefacesContextUI.actualizar("formLovs:formDInformacionPersonal:infoP_tipoD:TipoDocumentoInformacionPersonalDialogo");
                modificarInfoR_TipoDocInfoP(lovTiposDocumentos.size());
                PrimefacesContextUI.ejecutar("PF('TipoDocumentoInformacionPersonalDialogo').show()");
                PrimefacesContextUI.actualizar("form:tipoDocumentoModPersonal");
            }
        }
        if (campo.equalsIgnoreCase("NACIMIENTO")) {
            nuevaPersona.getCiudadnacimiento().setNombre(getAuxInformacionPersonalCiudadNacimiento());
            if (lovCiudades != null) {
                for (int i = 0; i < lovCiudades.size(); i++) {
                    if (lovCiudades.get(i).getNombre().startsWith(valor.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
            }
            if (coincidencias == 1) {
                nuevaPersona.setCiudadnacimiento(lovCiudades.get(indiceUnicoElemento));
                txt_ciudadNac = nuevaPersona.getCiudadnacimiento().getNombre();
                PrimefacesContextUI.actualizar("form:ciudadNacimientoModPersonal");
            } else {
                txt_ciudadNac = "";
                permitirIndexInformacionPersonal = false;
                PrimefacesContextUI.actualizar("formLovs:formDInformacionPersonal:infoP_ciudadN:CiudadNacimientoInformacionPersonalDialogo");
                modificarInfoR_CiudadNacInfoP(lovCiudades.size());
                PrimefacesContextUI.ejecutar("PF('CiudadNacimientoInformacionPersonalDialogo').show()");
                PrimefacesContextUI.actualizar("form:ciudadNacimientoModPersonal");
            }
        }
        if (campo.equalsIgnoreCase("DOCUMENTO")) {
            nuevaPersona.getCiudaddocumento().setNombre(getAuxInformacionPersonalCiudadDocumento());
            if (lovCiudades != null) {
                for (int i = 0; i < lovCiudades.size(); i++) {
                    if (lovCiudades.get(i).getNombre().startsWith(valor.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
            }
            if (coincidencias == 1) {
                nuevaPersona.setCiudaddocumento(lovCiudades.get(indiceUnicoElemento));
                txt_ciudadDoc = nuevaPersona.getCiudaddocumento().getNombre();
                PrimefacesContextUI.actualizar("form:ciudadDocumentoModPersonal");
            } else {
                permitirIndexInformacionPersonal = false;
                txt_ciudadDoc = "";
                PrimefacesContextUI.actualizar("formLovs:formDInformacionPersonal:infoP_ciudadD:CiudadDocumentoInformacionPersonalDialogo");
                modificarInfoR_CiudadDocInfoP(lovCiudades.size());
                PrimefacesContextUI.ejecutar("PF('CiudadDocumentoInformacionPersonalDialogo').show()");
                PrimefacesContextUI.actualizar("form:ciudadDocumentoModPersonal");
            }
        }
    }

    public void modificarCargoDesempeñado(int itemCargoD, String campo, String valor) {
        System.out.println("modificarCargoDesempeñado()");
        System.out.println("itemCargoD : " + itemCargoD + ", campo : " + campo + ", valor : " + valor);
        cargarLovEstructuras();
        cargarLovEmpleados();
        cargarLovPapeles();
        cargarLovMotivosCargos();
        cargarLovCargos();
        idCargoDesempeñado = itemCargoD;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (campo.equalsIgnoreCase("CARGO")) {
            nuevaVigenciaCargo.getCargo().setNombre(getAuxCargoDesempeñadoNombreCargo());
            if (lovCargos != null) {
                for (int i = 0; i < lovCargos.size(); i++) {
                    if (lovCargos.get(i).getNombre().startsWith(valor.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
            }
            if (coincidencias == 1) {
                nuevaVigenciaCargo.setCargo(lovCargos.get(indiceUnicoElemento));
                txt_cargo = nuevaVigenciaCargo.getCargo().getNombre();
                PrimefacesContextUI.actualizar("form:cargoModCargoDesempeñado");
            } else {
                txt_cargo = "";
                permitirIndexCargoDesempeñado = false;
                PrimefacesContextUI.actualizar("formLovs:formDCargoDesempenado:formCargo:CargoCargoDesempeñadoDialogo");
                modificarInfoR_CargoD(lovCargos.size());
                PrimefacesContextUI.ejecutar("PF('CargoCargoDesempeñadoDialogo').show()");
                PrimefacesContextUI.actualizar("form:cargoModCargoDesempeñado");
            }
        }
        if (campo.equalsIgnoreCase("MOTIVO")) {
            nuevaVigenciaCargo.getMotivocambiocargo().setNombre(getAuxCargoDesempeñadoMotivoCargo());
            if (lovMotivosCargos != null) {
                for (int i = 0; i < lovMotivosCargos.size(); i++) {
                    if (lovMotivosCargos.get(i).getNombre().startsWith(valor.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
            }
            if (coincidencias == 1) {
                nuevaVigenciaCargo.setMotivocambiocargo(lovMotivosCargos.get(indiceUnicoElemento));
                txt_motivoCargo = nuevaVigenciaCargo.getMotivocambiocargo().getNombre();
                PrimefacesContextUI.actualizar("form:motivoModCargoDesempeñado");
            } else {
                permitirIndexCargoDesempeñado = false;
                txt_motivoCargo = "";
                PrimefacesContextUI.actualizar("formLovs:formDCargoDesempenado:formMotcargo:MotivoCambioCargoCargoDesempeñadoDialogo");
                modificarInfoR_MotivoCargoD(lovMotivosCargos.size());
                PrimefacesContextUI.ejecutar("PF('MotivoCambioCargoCargoDesempeñadoDialogo').show()");
                PrimefacesContextUI.actualizar("form:motivoModCargoDesempeñado");
            }
        }
        if (campo.equalsIgnoreCase("ESTRUCTURA")) {
            nuevaVigenciaCargo.getEstructura().setNombre(getAuxCargoDesempeñadoNombreEstructura());
            if (lovEstructuras != null) {
                for (int i = 0; i < lovEstructuras.size(); i++) {
                    if (lovEstructuras.get(i).getCentrocosto().getNombre().startsWith(valor.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
            }
            if (coincidencias == 1) {
                nuevaVigenciaCargo.setEstructura(lovEstructuras.get(indiceUnicoElemento));
                PrimefacesContextUI.actualizar("form:estructuraModCargoDesempeñado");
            } else {
                permitirIndexCargoDesempeñado = false;
                PrimefacesContextUI.actualizar("formLovs:formDCargoDesempenado:formEstruCargo:EstructuraCargoDesempeñadoDialogo");
                modificarInfoR_EstrCargoD(lovEstructuras.size());
                PrimefacesContextUI.ejecutar("PF('EstructuraCargoDesempeñadoDialogo').show()");
                PrimefacesContextUI.actualizar("form:estructuraModCargoDesempeñado");
            }
        }
        if (campo.equalsIgnoreCase("PAPEL")) {
            if (!valor.isEmpty()) {
                nuevaVigenciaCargo.getPapel().setDescripcion(getAuxCargoDesempeñadoPapel());
                if (lovPapeles != null) {
                    for (int i = 0; i < lovPapeles.size(); i++) {
                        if (lovPapeles.get(i).getDescripcion().startsWith(valor.toUpperCase())) {
                            indiceUnicoElemento = i;
                            coincidencias++;
                        }
                    }
                }
                if (coincidencias == 1) {
                    nuevaVigenciaCargo.setPapel(lovPapeles.get(indiceUnicoElemento));
                    txt_papel = nuevaVigenciaCargo.getPapel().getDescripcion();
                    PrimefacesContextUI.actualizar("form:papelModCargoDesempeñado");
                } else {
                    txt_papel = "";
                    permitirIndexCargoDesempeñado = false;
                    PrimefacesContextUI.actualizar("formLovs:formDCargoDesempenado:formPapelD:PapelCargoDesempeñadoDialogo");
                    modificarInfoR_PapelCargoD(lovPapeles.size());
                    PrimefacesContextUI.actualizar("form:papelModCargoDesempeñado");
                    PrimefacesContextUI.ejecutar("PF('PapelCargoDesempeñadoDialogo').show()");
                }
            } else {
                nuevaVigenciaCargo.setPapel(new Papeles());
                PrimefacesContextUI.actualizar("form:papelModCargoDesempeñado");
            }
        }
        if (campo.equalsIgnoreCase("JEFE")) {
            if (!valor.isEmpty()) {
                nuevaVigenciaCargo.getEmpleadojefe().getPersona().setNombreCompleto(getAuxCargoDesempeñadoEmpleadoJefe());
                if (lovEmpleados != null) {
                    for (int i = 0; i < lovEmpleados.size(); i++) {
                        if (lovEmpleados.get(i).getPersona().getNombreCompleto().startsWith(valor.toUpperCase())) {
                            indiceUnicoElemento = i;
                            coincidencias++;
                        }
                    }
                }
                if (coincidencias == 1) {
                    nuevaVigenciaCargo.setEmpleadojefe(lovEmpleados.get(indiceUnicoElemento));
                    txt_empleado = nuevaVigenciaCargo.getEmpleadojefe().getPersona().getNombreCompleto();
                    PrimefacesContextUI.actualizar("form:empleadoJefeModCargoDesempeñado");
                } else {
                    permitirIndexCargoDesempeñado = false;
                    txt_empleado = "";
                    PrimefacesContextUI.actualizar("formLovs:formDCargoDesempenado:formjefe:EmpleadoJefeCargoDesempeñadoDialogo");
                    modificarInfoR_JefeCargoD(lovEmpleados.size());
                    PrimefacesContextUI.actualizar("form:empleadoJefeModCargoDesempeñado");
                    permitirDesplegarLista = false;
                    PrimefacesContextUI.ejecutar("PF('MotivoLocalizacionCentroCostoDialogo').hide()");
                    PrimefacesContextUI.ejecutar("PF('EmpleadoJefeCargoDesempeñadoDialogo').show()");
                }
            } else {
                nuevaVigenciaCargo.setEmpleadojefe(new Empleados());
                nuevaVigenciaCargo.getEmpleadojefe().setPersona(new Personas());
                PrimefacesContextUI.actualizar("form:papelModCargoDesempeñado");
            }
        }
    }

    public void modificarCentroCosto(int indice, String campo, String valor) {
        cargarLovEstructurasCentroCosto();
        cargarLovMotivosLocalizaciones();
        idCentroCosto = indice;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (campo.equalsIgnoreCase("ESTRUCTURA")) {
            nuevaVigenciaLocalizacion.getLocalizacion().getCentrocosto().setNombre(getAuxCentroCostoEstructura());
            if (lovEstructurasCentroCosto != null) {
                for (int i = 0; i < lovEstructurasCentroCosto.size(); i++) {
                    if (lovEstructurasCentroCosto.get(i).getCentrocosto().getNombre().startsWith(valor.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
            }
            if (coincidencias == 1) {
                nuevaVigenciaLocalizacion.setLocalizacion(lovEstructurasCentroCosto.get(indiceUnicoElemento));
                PrimefacesContextUI.actualizar("form:estructuraModCentroCosto");
            } else {
                permitirIndexCentroCosto = false;
                PrimefacesContextUI.actualizar("formLovs:formDCentroCosto:formEstrucCC:EstructuraCentroCostoDialogo");
                modificarInfoR_EstrCentroC(lovEstructurasCentroCosto.size());
                PrimefacesContextUI.ejecutar("PF('EstructuraCentroCostoDialogo').show()");
                PrimefacesContextUI.actualizar("form:estructuraModCentroCosto");
            }
        }
        if (campo.equalsIgnoreCase("MOTIVO")) {
            if (permitirDesplegarLista) {
                nuevaVigenciaLocalizacion.getMotivo().setDescripcion(getAuxCentroCostoMotivo());
                if (lovMotivosLocalizaciones != null) {
                    for (int i = 0; i < lovMotivosLocalizaciones.size(); i++) {
                        if (lovMotivosLocalizaciones.get(i).getDescripcion().startsWith(valor.toUpperCase())) {
                            indiceUnicoElemento = i;
                            coincidencias++;
                        }
                    }
                }
                if (coincidencias == 1) {
                    nuevaVigenciaLocalizacion.setMotivo(lovMotivosLocalizaciones.get(indiceUnicoElemento));
                    txt_motivoCentroC = nuevaVigenciaLocalizacion.getMotivo().getDescripcion();
                    PrimefacesContextUI.actualizar("form:motivoModCentroCosto");
                } else {
                    permitirIndexCentroCosto = false;
                    txt_motivoCentroC = "";
                    PrimefacesContextUI.actualizar("formLovs:formDCentroCosto:formCentroC:MotivoLocalizacionCentroCostoDialogo");
                    modificarInfoR_MotivoCentroC(lovMotivosLocalizaciones.size());
                    PrimefacesContextUI.ejecutar("PF('MotivoLocalizacionCentroCostoDialogo').show()");
                    PrimefacesContextUI.actualizar("form:motivoModCentroCosto");
                }
            }
        }
    }

    public void modificarTipoTrabajador(int indice, String campo, String valor) {
        cargarLovTiposTrabajadores();
        idTipoTrabajador = indice;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (campo.equalsIgnoreCase("TIPOTRABAJADOR")) {
            System.out.println("Entro en modificarTipoTrabajador()");
            nuevaVigenciaTipoTrabajador.getTipotrabajador().setNombre(getAuxTipoTrabajadorNombreTipoTrabajador());
            if (lovTiposTrabajadores != null) {
                for (int i = 0; i < lovTiposTrabajadores.size(); i++) {
                    if (lovTiposTrabajadores.get(i).getNombre().startsWith(valor.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
            }
            if (coincidencias == 1) {
                nuevaVigenciaTipoTrabajador.setTipotrabajador(lovTiposTrabajadores.get(indiceUnicoElemento));
                txt_tipoT = nuevaVigenciaTipoTrabajador.getTipotrabajador().getNombre();
                PrimefacesContextUI.actualizar("form:tipoTrabajadorModTipoTrabajador");
                validarDisableTipoTrabajador();
                System.out.println("nuevaVigenciaSueldo.getMotivocambiosueldo() : " + nuevaVigenciaSueldo.getMotivocambiosueldo());
                if (nuevaVigenciaSueldo.getMotivocambiosueldo() != null) {
                    System.out.println("nuevaVigenciaSueldo.getMotivocambiosueldo().getNombre() :" + nuevaVigenciaSueldo.getMotivocambiosueldo().getNombre());
                }
                System.out.println("nuevaVigenciaTipoContrato.getMotivocontrato() : " + nuevaVigenciaTipoContrato.getMotivocontrato());
                if (nuevaVigenciaSueldo.getMotivocambiosueldo() != null) {
                    System.out.println("nuevaVigenciaTipoContrato.getMotivocontrato().getNombre() :" + nuevaVigenciaTipoContrato.getMotivocontrato().getNombre());
                }
                cargarLovsConTipoTrabajador(nuevaVigenciaTipoTrabajador.getTipotrabajador().getSecuencia());
                System.out.println("Despues de cargarLovsConTipoTrabajador.");
                System.out.println("nuevaVigenciaSueldo.getMotivocambiosueldo() : " + nuevaVigenciaSueldo.getMotivocambiosueldo());
                if (nuevaVigenciaSueldo.getMotivocambiosueldo() != null) {
                    System.out.println("nuevaVigenciaSueldo.getMotivocambiosueldo().getNombre() :" + nuevaVigenciaSueldo.getMotivocambiosueldo().getNombre());
                }
                System.out.println("nuevaVigenciaTipoContrato.getMotivocontrato() : " + nuevaVigenciaTipoContrato.getMotivocontrato());
                if (nuevaVigenciaSueldo.getMotivocambiosueldo() != null) {
                    System.out.println("nuevaVigenciaTipoContrato.getMotivocontrato().getNombre() :" + nuevaVigenciaTipoContrato.getMotivocontrato().getNombre());
                }
            } else {
                permitirIndexTipoTrabajador = false;
                txt_tipoT = "";
                PrimefacesContextUI.actualizar("formLovs:formDTipoTrabajador:TipoTrabajadorTipoTrabajadorDialogo");
                modificarInfoR_TipoTraTT(lovTiposTrabajadores.size());
                PrimefacesContextUI.ejecutar("PF('TipoTrabajadorTipoTrabajadorDialogo').show()");
                PrimefacesContextUI.actualizar("form:tipoTrabajadorModTipoTrabajador");
            }
        }
    }

    public void modificarTipoSalario(int indice, String campo, String valor) {
        idTipoSalario = indice;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (campo.equalsIgnoreCase("REFORMA")) {
            if (lovReformasLaborales == null) {
                PrimefacesContextUI.ejecutar("PF('advertenciaSeleccionTipoT').show()");
            } else {
                if (lovReformasLaborales != null) {
                    for (int i = 0; i < lovReformasLaborales.size(); i++) {
                        if (lovReformasLaborales.get(i).getNombre().startsWith(valor.toUpperCase())) {
                            indiceUnicoElemento = i;
                            coincidencias++;
                        }
                    }
                }
                if (coincidencias == 1) {
                    nuevaVigenciaReformaLaboral.setReformalaboral(lovReformasLaborales.get(indiceUnicoElemento));
                    PrimefacesContextUI.actualizar("form:tipoSalarioModTipoSalario");
                    validarTipoTrabajadorReformaLaboral();
                } else {
                    permitirIndexTipoSalario = false;
                    modificarInfoR_ReformaTipoSa(lovReformasLaborales.size());
                    PrimefacesContextUI.ejecutar("PF('ReformaLaboralTipoSalarioDialogo').show()");
                    PrimefacesContextUI.actualizar("formLovs:formDTipoSalario:ReformaLaboralTipoSalarioDialogo");
                    PrimefacesContextUI.actualizar("formLovs:formDTipoSalario:lovReformaLaboralTipoSalario");
                    PrimefacesContextUI.actualizar("form:tipoSalarioModTipoSalario");
                }
            }
        }
    }

    public void modificarSueldo(int indice, String campo, String valor) {
        cargarLovMotivosCambiosSueldos();
        idTipoSalario = indice;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (campo.equalsIgnoreCase("MOTIVO")) {
            nuevaVigenciaSueldo.getMotivocambiosueldo().setNombre(getAuxSueldoMotivoSueldo());
            if (lovMotivosCambiosSueldos != null) {
                for (int i = 0; i < lovMotivosCambiosSueldos.size(); i++) {
                    if (lovMotivosCambiosSueldos.get(i).getNombre().startsWith(valor.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
            }
            if (coincidencias == 1) {
                nuevaVigenciaSueldo.setMotivocambiosueldo(lovMotivosCambiosSueldos.get(indiceUnicoElemento));
                txt_motivoSu = nuevaVigenciaSueldo.getMotivocambiosueldo().getNombre();
                PrimefacesContextUI.actualizar("form:motivoSueldoModSueldo");
            } else {
                permitirIndexSueldo = false;
                txt_motivoSu = "";
                PrimefacesContextUI.actualizar("formLovs:formDSueldo:formMotivoSu:MotivoCambioSueldoSueldoDialogo");
                modificarInfoR_MotivoSu(lovMotivosCambiosSueldos.size());
                PrimefacesContextUI.ejecutar("PF('MotivoCambioSueldoSueldoDialogo').show()");
                PrimefacesContextUI.actualizar("form:motivoSueldoModSueldo");
            }
        }
        if (campo.equalsIgnoreCase("TIPOSUELDO")) {
            if (lovTiposSueldos == null) {
                PrimefacesContextUI.ejecutar("PF('advertenciaSeleccionTipoT').show()");
            } else {
                if (lovTiposSueldos != null) {
                    for (int i = 0; i < lovTiposSueldos.size(); i++) {
                        if (lovTiposSueldos.get(i).getDescripcion().startsWith(valor.toUpperCase())) {
                            indiceUnicoElemento = i;
                            coincidencias++;
                        }
                    }
                }
                if (coincidencias == 1) {
                    nuevaVigenciaSueldo.setTiposueldo(lovTiposSueldos.get(indiceUnicoElemento));
                    txt_TipoSu = nuevaVigenciaSueldo.getTiposueldo().getDescripcion();
                    PrimefacesContextUI.actualizar("form:motivoSueldoModSueldo");
                    validarTipoTrabajadorTipoSueldo();

                } else {
                    permitirIndexSueldo = false;
                    txt_TipoSu = "";
                    modificarInfoR_TipoSuSu(lovTiposSueldos.size());
                    PrimefacesContextUI.ejecutar("PF('TipoSueldoSueldoDialogo').show()");
                    PrimefacesContextUI.actualizar("formLovs:formDSueldo:formTipoSueldo:TipoSueldoSueldoDialogo");
                    PrimefacesContextUI.actualizar("formLovs:formDSueldo:formTipoSueldo:lovTipoSueldoSueldo");
                    PrimefacesContextUI.actualizar("form:tipoSueldoModSueldo");
                }
            }
        }
    }

    public void modificarTipoContrato(int indice, String campo, String valor) {
        cargarLovMotivosContratos();
        idTipoContrato = indice;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (campo.equalsIgnoreCase("MOTIVO")) {
            nuevaVigenciaTipoContrato.getMotivocontrato().setNombre(getAuxTipoContratoMotivo());
            if (lovMotivosContratos != null) {
                for (int i = 0; i < lovMotivosContratos.size(); i++) {
                    if (lovMotivosContratos.get(i).getNombre().startsWith(valor.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
            }

            if (coincidencias == 1) {
                nuevaVigenciaTipoContrato.setMotivocontrato(lovMotivosContratos.get(indiceUnicoElemento));
                txt_motivoContrato = nuevaVigenciaTipoContrato.getMotivocontrato().getNombre();
                PrimefacesContextUI.actualizar("form:motivoContratoModTipoContrato");
            } else {
                permitirIndexTipoContrato = false;
                txt_motivoContrato = "";
                modificarInfoR_MotivoTipoCo(lovMotivosContratos.size());
                PrimefacesContextUI.ejecutar("PF('MotivoContratoTipoContratoDialogo').show()");
                PrimefacesContextUI.actualizar("formLovs:formDTipoContrato:formTipoContrat:MotivoContratoTipoContratoDialogo");
                PrimefacesContextUI.actualizar("formLovs:formDTipoContrato:formTipoContrat:lovMotivoContratoTipoContrato");
                PrimefacesContextUI.actualizar("form:motivoContratoModTipoContrato");
            }
        }

        if (campo.equalsIgnoreCase("TIPO")) {
            if (lovTiposContratos == null) {
                PrimefacesContextUI.ejecutar("PF('advertenciaSeleccionTipoT').show()");
            } else {
                if (lovTiposContratos != null) {
                    for (int i = 0; i < lovTiposContratos.size(); i++) {
                        if (lovTiposContratos.get(i).getNombre().startsWith(valor.toUpperCase())) {
                            indiceUnicoElemento = i;
                            coincidencias++;
                        }
                    }
                }
                if (coincidencias == 1) {
                    nuevaVigenciaTipoContrato.setTipocontrato(lovTiposContratos.get(indiceUnicoElemento));
                    txt_tipoContrato = nuevaVigenciaTipoContrato.getTipocontrato().getNombre();
                    PrimefacesContextUI.actualizar("form:tipoContratoModTipoContrato");
                    validarTipoTrabajadorTipoContrato();
                } else {
                    permitirIndexTipoContrato = false;
                    txt_tipoContrato = "";
                    modificarInfoR_TipoContrato(lovTiposContratos.size());
                    PrimefacesContextUI.ejecutar("PF('TipoContratoTipoContratoDialogo').show()");
                    PrimefacesContextUI.actualizar("formLovs:formDTipoContrato:formTipoContrato:TipoContratoTipoContratoDialogo");
                    PrimefacesContextUI.actualizar("formLovs:formDTipoContrato:formTipoContrato:lovTipoContratoTipoContrato");
                    PrimefacesContextUI.actualizar("form:tipoContratoModTipoContrato");
                }
            }
        }
    }

    public void modificarNormaLaboral(int indice, String campo, String valor) {
        idNormaLaboral = indice;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (campo.equalsIgnoreCase("NORMA")) {
            if (lovNormasLaborales == null) {
                PrimefacesContextUI.ejecutar("PF('advertenciaSeleccionTipoT').show()");
            } else {
                if (lovNormasLaborales != null) {
                    for (int i = 0; i < lovNormasLaborales.size(); i++) {
                        if (lovNormasLaborales.get(i).getNombre().startsWith(valor.toUpperCase())) {
                            indiceUnicoElemento = i;
                            coincidencias++;
                        }
                    }
                }
                if (coincidencias == 1) {
                    nuevaVigenciaNormaEmpleado.setNormalaboral(lovNormasLaborales.get(indiceUnicoElemento));
                    PrimefacesContextUI.actualizar("form:normaLaboralModNormaLaboral");
                    validarTipoTrabajadorNormaLaboral();
                } else {
                    permitirIndexNormaLaboral = false;
                    modificarInfoR_NormaL(lovNormasLaborales.size());
                    PrimefacesContextUI.ejecutar("PF('NormaLaboralNormaLaboralDialogo').show()");
                    PrimefacesContextUI.actualizar("formLovs:formDNormaLaboral:NormaLaboralNormaLaboralDialogo");
                    PrimefacesContextUI.actualizar("formLovs:formDNormaLaboral:lovNormaLaboralNormaLaboral");
                    PrimefacesContextUI.actualizar("form:normaLaboralModNormaLaboral");
                }
            }
        }
    }

    public void modificarLegislacionLaboral(int indice, String campo, String valor) {
        idLegislacionLaboral = indice;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (campo.equalsIgnoreCase("CONTRATO")) {
            if (lovContratos == null) {
                PrimefacesContextUI.ejecutar("PF('advertenciaSeleccionTipoT').show()");
            } else {
                if (lovContratos != null) {
                    for (int i = 0; i < lovContratos.size(); i++) {
                        if (lovContratos.get(i).getDescripcion().startsWith(valor.toUpperCase())) {
                            indiceUnicoElemento = i;
                            coincidencias++;
                        }
                    }
                }
                if (coincidencias == 1) {
                    nuevaVigenciaContrato.setContrato(lovContratos.get(indiceUnicoElemento));
                    PrimefacesContextUI.actualizar("form:legislacionLaboralModLegislacionLaboral");
                    validarTipoTrabajadorContrato();
                } else {
                    permitirIndexLegislacionLaboral = false;
                    modificarInfoR_ContratoLL(lovContratos.size());
                    PrimefacesContextUI.ejecutar("PF('ContratoLegislacionLaboralDialogo').show()");
                    PrimefacesContextUI.actualizar("formLovs:formDLegislacionLaboral:ContratoLegislacionLaboralDialogo");
                    PrimefacesContextUI.actualizar("formLovs:formDLegislacionLaboral:lovContratoLegislacionLaboral");
                    PrimefacesContextUI.actualizar("form:legislacionLaboralModLegislacionLaboral");
                }
            }
        }
    }

    public void modificarUbicacionGeografica(int indice, String campo, String valor) {
        cargarLovUbicacionesGeograficas();
        idUbicacionGeografica = indice;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (campo.equalsIgnoreCase("UBICACION")) {
            nuevaVigenciaUbicacion.getUbicacion().setDescripcion(getAuxUbicacionGeograficaUbicacion());
            if (lovUbicacionesGeograficas != null) {
                for (int i = 0; i < lovUbicacionesGeograficas.size(); i++) {
                    if (lovUbicacionesGeograficas.get(i).getDescripcion().startsWith(valor.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
            }
            if (coincidencias == 1) {
                nuevaVigenciaUbicacion.setUbicacion(lovUbicacionesGeograficas.get(indiceUnicoElemento));
                txt_ubicacionG = nuevaVigenciaUbicacion.getUbicacion().getDescripcion();
                PrimefacesContextUI.actualizar("form:ubicacionGeograficaModUbicacionGeografica");
            } else {
                permitirIndexUbicacionGeografica = false;
                txt_ubicacionG = "";
                PrimefacesContextUI.actualizar("formLovs:formDUbicacion:UbicacionUbicacionGeograficaDialogo");
                modificarInfoR_UbicacionUb(lovUbicacionesGeograficas.size());
                PrimefacesContextUI.ejecutar("PF('UbicacionUbicacionGeograficaDialogo').show()");
                PrimefacesContextUI.actualizar("form:ubicacionGeograficaModUbicacionGeografica");
            }
        }
    }

    public void modificarJornadaLaboral(int indice, String campo, String valor) {
        cargarLovJornadasLaborales();
        idJornadaLaboral = indice;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (campo.equalsIgnoreCase("JORNADA")) {
            nuevaVigenciaJornada.getJornadatrabajo().setDescripcion(getAuxJornadaLaboralJornada());
            if (lovJornadasLaborales != null) {
                for (int i = 0; i < lovJornadasLaborales.size(); i++) {
                    if (lovJornadasLaborales.get(i).getDescripcion().startsWith(valor.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
            }
            if (coincidencias == 1) {
                nuevaVigenciaJornada.setJornadatrabajo(lovJornadasLaborales.get(indiceUnicoElemento));
                txt_jornada = nuevaVigenciaJornada.getJornadatrabajo().getDescripcion();
                PrimefacesContextUI.actualizar("form:jornadaLaboralModJornadaLaboral");
            } else {
                permitirIndexJornadaLaboral = false;
                txt_jornada = "";
                PrimefacesContextUI.actualizar("formLovs:formDJornadaLaboral:JornadaJornadaLaboralDialogo");
                modificarInfoR_JornadaL(lovJornadasLaborales.size());
                PrimefacesContextUI.ejecutar("PF('JornadaJornadaLaboralDialogo').show()");
                PrimefacesContextUI.actualizar("form:jornadaLaboralModJornadaLaboral");
            }
        }
    }

    public void modificarFormaPago(int indice, String campo, String valor) {
        cargarLovMetodosPagos();
        cargarLovPeriodicidades();
        cargarLovSucursales();
        idFormaPago = indice;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (campo.equalsIgnoreCase("FORMA")) {
            nuevaVigenciaFormaPago.getFormapago().setNombre(getAuxFormaPagoPeriodicidad());
            if (lovPeriodicidades != null) {
                for (int i = 0; i < lovPeriodicidades.size(); i++) {
                    if (lovPeriodicidades.get(i).getNombre().startsWith(valor.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
            }
            if (coincidencias == 1) {
                nuevaVigenciaFormaPago.setFormapago(lovPeriodicidades.get(indiceUnicoElemento));
                txt_formaP = nuevaVigenciaFormaPago.getFormapago().getNombre();
                PrimefacesContextUI.actualizar("form:formaPagoModFormaPago");
            } else {
                permitirIndexFormaPago = false;
                txt_formaP = "";
                PrimefacesContextUI.actualizar("formLovs:formDFormaPago:formPeriodicidad:PeriodicidadFormaPagoDialogo");
                modificarInfoR_PeriodFormaPago(lovPeriodicidades.size());
                PrimefacesContextUI.ejecutar("PF('PeriodicidadFormaPagoDialogo').show()");
                PrimefacesContextUI.actualizar("form:formaPagoModFormaPago");
            }
        }
        if (campo.equalsIgnoreCase("SUCURSAL")) {
            if (!valor.isEmpty()) {
                nuevaVigenciaFormaPago.getSucursal().setNombre(getAuxFormaPagoSucursal());
                if (lovSucursales != null) {
                    for (int i = 0; i < lovSucursales.size(); i++) {
                        if (lovSucursales.get(i).getNombre().startsWith(valor.toUpperCase())) {
                            indiceUnicoElemento = i;
                            coincidencias++;
                        }
                    }
                }
                if (coincidencias == 1) {
                    nuevaVigenciaFormaPago.setSucursal(lovSucursales.get(indiceUnicoElemento));
                    PrimefacesContextUI.actualizar("form:sucursalPagoModFormaPago");
                } else {
                    permitirIndexFormaPago = false;
                    PrimefacesContextUI.actualizar("formLovs:formDFormaPago:formSucursal:SucursalFormaPagoDialogo");
                    modificarInfoR_SucursalFormaP(lovSucursales.size());
                    PrimefacesContextUI.ejecutar("PF('SucursalFormaPagoDialogo').show()");
                    PrimefacesContextUI.actualizar("form:sucursalPagoModFormaPago");
                }
            } else {
                nuevaVigenciaFormaPago.setSucursal(new Sucursales());
                PrimefacesContextUI.actualizar("form:sucursalPagoModFormaPago");
            }
        }
        if (campo.equalsIgnoreCase("METODO")) {
            nuevaVigenciaFormaPago.getMetodopago().setDescripcion(getAuxFormaPagoMetodo());
            if (lovMetodosPagos != null) {
                for (int i = 0; i < lovMetodosPagos.size(); i++) {
                    if (lovMetodosPagos.get(i).getDescripcion().startsWith(valor.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
            }
            if (coincidencias == 1) {
                nuevaVigenciaFormaPago.setMetodopago(lovMetodosPagos.get(indiceUnicoElemento));
                txt_metodoP = nuevaVigenciaFormaPago.getMetodopago().getDescripcion();
                PrimefacesContextUI.actualizar("form:metodoPagoModFormaPago");
            } else {
                permitirIndexFormaPago = false;
                txt_metodoP = "";
                PrimefacesContextUI.actualizar("formLovs:formDFormaPago:formMetodo:MetodoPagoFormaPagoDialogo");
                modificarInfoR_MetodoFormaP(lovMetodosPagos.size());
                PrimefacesContextUI.ejecutar("PF('MetodoPagoFormaPagoDialogo').show()");
                PrimefacesContextUI.actualizar("form:metodoPagoModFormaPago");
            }
        }
    }

    public void modificarAfiliacionFondo(int indice, String campo, String valor) {
        cargarLovTercerosSucursales();
        idAfiliacionFondo = indice;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (campo.equalsIgnoreCase("FONDO")) {
            if (!valor.isEmpty()) {
                nuevaVigenciaAfiliacionFondo.getTercerosucursal().setDescripcion(getAuxAfiliacionFondo());
                if (lovTercerosSucursales != null) {
                    for (int i = 0; i < lovTercerosSucursales.size(); i++) {
                        if (lovTercerosSucursales.get(i).getDescripcion().startsWith(valor.toUpperCase())) {
                            indiceUnicoElemento = i;
                            coincidencias++;
                        }
                    }
                }
                if (coincidencias == 1) {
                    nuevaVigenciaAfiliacionFondo.setTercerosucursal(lovTercerosSucursales.get(indiceUnicoElemento));
                    txt_terceroCes = nuevaVigenciaAfiliacionFondo.getTercerosucursal().getDescripcion();
                    TiposEntidades fondo = administrarPersonaIndividual.buscarTipoEntidadPorCodigo(new Short("12"));
                    nuevaVigenciaAfiliacionFondo.setTipoentidad(fondo);
                    PrimefacesContextUI.actualizar("form:fondoCensantiasModAfiliaciones");
                } else {
                    permitirIndexAfiliacionFondo = false;
                    txt_terceroCes = "";
                    PrimefacesContextUI.actualizar("formLovs:formDAfiliacion:TerceroAfiliacionDialogo");
                    modificarInfoR_TerceroAfSuc(lovTercerosSucursales.size());
                    PrimefacesContextUI.ejecutar("PF('TerceroAfiliacionDialogo').show()");
                    PrimefacesContextUI.actualizar("form:fondoCensantiasModAfiliaciones");
                }
            } else {
                nuevaVigenciaAfiliacionFondo.setTercerosucursal(new TercerosSucursales());
                PrimefacesContextUI.actualizar("form:fondoCensantiasModAfiliaciones");
            }
        }
    }

    public void modificarAfiliacionEPS(int indice, String campo, String valor) {
        cargarLovTercerosSucursales();
        idAfiliacionEPS = indice;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (campo.equalsIgnoreCase("EPS")) {
            nuevaVigenciaAfiliacionEPS.getTercerosucursal().setDescripcion(getAuxAfiliacionEPS());
            if (lovTercerosSucursales != null) {
                for (int i = 0; i < lovTercerosSucursales.size(); i++) {
                    if (lovTercerosSucursales.get(i).getDescripcion().startsWith(valor.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
            }
            if (coincidencias == 1) {
                nuevaVigenciaAfiliacionEPS.setTercerosucursal(lovTercerosSucursales.get(indiceUnicoElemento));
                txt_terceroEPS = nuevaVigenciaAfiliacionEPS.getTercerosucursal().getDescripcion();
                PrimefacesContextUI.actualizar("form:epsModAfiliaciones");
                TiposEntidades eps = administrarPersonaIndividual.buscarTipoEntidadPorCodigo(new Short("1"));
                nuevaVigenciaAfiliacionEPS.setTipoentidad(eps);
                consultarCodigoSS();
            } else {
                permitirIndexAfiliacionEPS = false;
                txt_terceroEPS = "";
                PrimefacesContextUI.actualizar("formLovs:formDAfiliacion:TerceroAfiliacionDialogo");
                modificarInfoR_TerceroAfSuc(lovTercerosSucursales.size());
                PrimefacesContextUI.ejecutar("PF('TerceroAfiliacionDialogo').show()");
                PrimefacesContextUI.actualizar("form:epsModAfiliaciones");
            }
        }
    }

    public void modificarAfiliacionCaja(int indice, String campo, String valor) {
        cargarLovTercerosSucursales();
        idAfiliacionCaja = indice;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (campo.equalsIgnoreCase("CAJA")) {
            if (!valor.isEmpty()) {
                nuevaVigenciaAfiliacionCaja.getTercerosucursal().setDescripcion(getAuxAfiliacionCaja());
                if (lovTercerosSucursales != null) {
                    for (int i = 0; i < lovTercerosSucursales.size(); i++) {
                        if (lovTercerosSucursales.get(i).getDescripcion().startsWith(valor.toUpperCase())) {
                            indiceUnicoElemento = i;
                            coincidencias++;
                        }
                    }
                }
                if (coincidencias == 1) {
                    nuevaVigenciaAfiliacionCaja.setTercerosucursal(lovTercerosSucursales.get(indiceUnicoElemento));
                    txt_terceroCC = nuevaVigenciaAfiliacionCaja.getTercerosucursal().getDescripcion();
                    TiposEntidades caja = administrarPersonaIndividual.buscarTipoEntidadPorCodigo(new Short("14"));
                    nuevaVigenciaAfiliacionCaja.setTipoentidad(caja);
                    PrimefacesContextUI.actualizar("form:cajaCompensacionModAfiliaciones");
                } else {
                    txt_terceroCC = "";
                    permitirIndexAfiliacionCaja = false;
                    PrimefacesContextUI.actualizar("formLovs:formDAfiliacion:TerceroAfiliacionDialogo");
                    modificarInfoR_TerceroAfSuc(lovTercerosSucursales.size());
                    PrimefacesContextUI.ejecutar("PF('TerceroAfiliacionDialogo').show()");
                    PrimefacesContextUI.actualizar("form:cajaCompensacionModAfiliaciones");
                }
            } else {
                nuevaVigenciaAfiliacionCaja.setTercerosucursal(new TercerosSucursales());
                PrimefacesContextUI.actualizar("form:cajaCompensacionModAfiliaciones");
            }
        }
    }

    public void modificarAfiliacionARP(int indice, String campo, String valor) {
        cargarLovTercerosSucursales();
        idAfiliacionARP = indice;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (campo.equalsIgnoreCase("ARP")) {
            nuevaVigenciaAfiliacionARP.getTercerosucursal().setDescripcion(getAuxAfiliacionARP());
            if (lovTercerosSucursales != null) {
                for (int i = 0; i < lovTercerosSucursales.size(); i++) {
                    if (lovTercerosSucursales.get(i).getDescripcion().startsWith(valor.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
            }
            if (coincidencias == 1) {
                nuevaVigenciaAfiliacionARP.setTercerosucursal(lovTercerosSucursales.get(indiceUnicoElemento));
                txt_terceroARL = nuevaVigenciaAfiliacionARP.getTercerosucursal().getDescripcion();
                TiposEntidades arp = administrarPersonaIndividual.buscarTipoEntidadPorCodigo(new Short("2"));
                nuevaVigenciaAfiliacionARP.setTipoentidad(arp);
                PrimefacesContextUI.actualizar("form:arpModAfiliaciones");
                consultarCodigoSC();
            } else {
                permitirIndexAfiliacionARP = false;
                txt_terceroARL = "";
                PrimefacesContextUI.actualizar("formLovs:formDAfiliacion:TerceroAfiliacionDialogo");
                modificarInfoR_TerceroAfSuc(lovTercerosSucursales.size());
                PrimefacesContextUI.ejecutar("PF('TerceroAfiliacionDialogo').show()");
                PrimefacesContextUI.actualizar("form:arpModAfiliaciones");
            }
        }
    }

    public void modificarAfiliacionAFP(int indice, String campo, String valor) {
        cargarLovTercerosSucursales();
        idAfiliacionAFP = indice;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (campo.equalsIgnoreCase("AFP")) {
            if (!valor.isEmpty()) {
                nuevaVigenciaAfiliacionAFP.getTercerosucursal().setDescripcion(getAuxAfiliacionAFP());
                if (lovTercerosSucursales != null) {
                    for (int i = 0; i < lovTercerosSucursales.size(); i++) {
                        if (lovTercerosSucursales.get(i).getDescripcion().startsWith(valor.toUpperCase())) {
                            indiceUnicoElemento = i;
                            coincidencias++;
                        }
                    }
                }
                if (coincidencias == 1) {
                    nuevaVigenciaAfiliacionAFP.setTercerosucursal(lovTercerosSucursales.get(indiceUnicoElemento));
                    txt_terceroAFP = nuevaVigenciaAfiliacionAFP.getTercerosucursal().getDescripcion();
                    TiposEntidades afp = administrarPersonaIndividual.buscarTipoEntidadPorCodigo(new Short("3"));
                    nuevaVigenciaAfiliacionAFP.setTipoentidad(afp);
                    PrimefacesContextUI.actualizar("form:afpModAfiliaciones");
                    consultarCodigoSP();
                } else {
                    permitirIndexAfiliacionAFP = false;
                    txt_terceroAFP = "";
                    PrimefacesContextUI.actualizar("formLovs:formDAfiliacion:TerceroAfiliacionDialogo");
                    modificarInfoR_TerceroAfSuc(lovTercerosSucursales.size());
                    PrimefacesContextUI.ejecutar("PF('TerceroAfiliacionDialogo').show()");
                    PrimefacesContextUI.actualizar("form:afpModAfiliaciones");
                }
            } else {
                nuevaVigenciaAfiliacionAFP.setTercerosucursal(new TercerosSucursales());
                PrimefacesContextUI.actualizar("form:afpModAfiliaciones");
            }
        }
    }

    public void modificarEstadoCivil(int indice, String campo, String valor) {
        cargarLovEstadosCiviles();
        idEstadoCivil = indice;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (campo.equalsIgnoreCase("ESTADO")) {
            if (!valor.isEmpty()) {
                nuevoEstadoCivil.getEstadocivil().setDescripcion(getAuxEstadoCivilEstado());
                if (lovEstadosCiviles != null) {
                    for (int i = 0; i < lovEstadosCiviles.size(); i++) {
                        if (lovEstadosCiviles.get(i).getDescripcion().startsWith(valor.toUpperCase())) {
                            indiceUnicoElemento = i;
                            coincidencias++;
                        }
                    }
                }
                if (coincidencias == 1) {
                    nuevoEstadoCivil.setEstadocivil(lovEstadosCiviles.get(indiceUnicoElemento));
                    txt_estadoC = nuevoEstadoCivil.getEstadocivil().getDescripcion();
                    PrimefacesContextUI.actualizar("form:estadoCivilModEstadoCivil");
                } else {
                    permitirIndexEstadoCivil = false;
                    txt_estadoC = "";
                    PrimefacesContextUI.actualizar("formLovs:formDEstadoCivil:EstadoCivilEstadoCivilDialogo");
                    modificarInfoR_EstadoCivil(lovEstadosCiviles.size());
                    PrimefacesContextUI.ejecutar("PF('EstadoCivilEstadoCivilDialogo').show()");
                    PrimefacesContextUI.actualizar("form:estadoCivilModEstadoCivil");
                }
            } else {
                nuevoEstadoCivil.setEstadocivil(new EstadosCiviles());
                PrimefacesContextUI.actualizar("form:estadoCivilModEstadoCivil");
            }
        }
    }

    public void modificarDireccion(int indice, String campo, String valor) {
        cargarLovCiudades();
        idDireccion = indice;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (campo.equalsIgnoreCase("CIUDAD")) {
            if (!valor.isEmpty()) {
                nuevaDireccion.getCiudad().setNombre(getAuxDireccionCiudad());
                if (lovCiudades != null) {
                    for (int i = 0; i < lovCiudades.size(); i++) {
                        if (lovCiudades.get(i).getNombre().startsWith(valor.toUpperCase())) {
                            indiceUnicoElemento = i;
                            coincidencias++;
                        }
                    }
                }
                if (coincidencias == 1) {
                    nuevaDireccion.setCiudad(lovCiudades.get(indiceUnicoElemento));
                    txt_ciudadDir = nuevaDireccion.getCiudad().getNombre();
                    PrimefacesContextUI.actualizar("form:ciudadModDireccion");
                } else {
                    permitirIndexDireccion = false;
                    txt_ciudadDir = "";
                    PrimefacesContextUI.actualizar("formLovs:formDDireccion:CiudadDireccionDialogo");
                    modificarInfoR_CiudadDir(lovCiudades.size());
                    PrimefacesContextUI.ejecutar("PF('CiudadDireccionDialogo').show()");
                    PrimefacesContextUI.actualizar("form:ciudadModDireccion");
                }
            } else {
                nuevaDireccion.setCiudad(new Ciudades());
                PrimefacesContextUI.actualizar("form:ciudadModDireccion");
            }
        }
    }

    public void modificarTelefono(int indice, String campo, String valor) {
        cargarLovTiposTelefonos();
        cargarLovCiudades();
        idTelefono = indice;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (campo.equalsIgnoreCase("CIUDAD")) {
            if (!valor.isEmpty()) {
                nuevoTelefono.getCiudad().setNombre(getAuxTelefonoCiudad());
                if (lovCiudades != null) {
                    for (int i = 0; i < lovCiudades.size(); i++) {
                        if (lovCiudades.get(i).getNombre().startsWith(valor.toUpperCase())) {
                            indiceUnicoElemento = i;
                            coincidencias++;
                        }
                    }
                }
                if (coincidencias == 1) {
                    nuevoTelefono.setCiudad(lovCiudades.get(indiceUnicoElemento));
                    txt_ciudadTel = nuevoTelefono.getCiudad().getNombre();
                    PrimefacesContextUI.actualizar("form:ciudadModTelefono");
                } else {
                    permitirIndexTelefono = false;
                    txt_ciudadTel = "";
                    PrimefacesContextUI.actualizar("formLovs:formDTelefono:formCiudadTel:CiudadTelefonoDialogo");
                    modificarInfoR_CiudadTel(lovCiudades.size());
                    PrimefacesContextUI.ejecutar("PF('CiudadTelefonoDialogo').show()");
                    PrimefacesContextUI.actualizar("form:ciudadModTelefono");
                }
            } else {
                nuevoTelefono.setCiudad(new Ciudades());
                PrimefacesContextUI.actualizar("form:ciudadModTelefono");
            }
        }
        if (campo.equalsIgnoreCase("TIPO")) {
            if (!valor.isEmpty()) {
                nuevoTelefono.getTipotelefono().setNombre(getAuxTelefonoTipo());
                if (lovTiposTelefonos != null) {
                    for (int i = 0; i < lovTiposTelefonos.size(); i++) {
                        if (lovTiposTelefonos.get(i).getNombre().startsWith(valor.toUpperCase())) {
                            indiceUnicoElemento = i;
                            coincidencias++;
                        }
                    }
                }
                if (coincidencias == 1) {
                    nuevoTelefono.setTipotelefono(lovTiposTelefonos.get(indiceUnicoElemento));
                    txt_tipoTel = nuevoTelefono.getTipotelefono().getNombre();
                    PrimefacesContextUI.actualizar("form:tipoTelefonoModTelefono");
                } else {
                    permitirIndexTelefono = false;
                    txt_tipoTel = "";
                    PrimefacesContextUI.actualizar("formLovs:formDTelefono:formTel:TipoTelefonoTelefonoDialogo");
                    modificarInfoR_TipoTelT(lovTiposTelefonos.size());
                    PrimefacesContextUI.ejecutar("PF('TipoTelefonoTelefonoDialogo').show()");
                    PrimefacesContextUI.actualizar("form:tipoTelefonoModTelefono");
                }
            } else {
                nuevoTelefono.setTipotelefono(new TiposTelefonos());
                PrimefacesContextUI.actualizar("form:tipoTelefonoModTelefono");
            }
        }
    }

    public void actualizarParametroTerceroAfiliacion() {
        if (idAfiliacionAFP >= 0) {
            actualizarParametroAFPAfiliacion();
        }
        if (idAfiliacionARP >= 0) {
            actualizarParametroARPAfiliacion();
        }
        if (idAfiliacionCaja >= 0) {
            actualizarParametroCajaAfiliacion();
        }
        if (idAfiliacionEPS >= 0) {
            actualizarParametroEPSAfiliacion();
        }
        if (idAfiliacionFondo >= 0) {
            actualizarParametroFondoAfiliacion();
        }
    }

    public void cancelarParametroTerceroAfiliacion() {
        if (idAfiliacionAFP >= 0) {
            cancelarParametroAFPAfiliacion();
        }
        if (idAfiliacionARP >= 0) {
            cancelarParametroARPAfiliacion();
        }
        if (idAfiliacionCaja >= 0) {
            cancelarParametroCajaAfiliacion();
        }
        if (idAfiliacionEPS >= 0) {
            cancelarParametroEPSAfiliacion();
        }
        if (idAfiliacionFondo >= 0) {
            cancelarParametroFondoAfiliacion();
        }
    }

    public void actualizarParametroCiudadTelefono() {
        RequestContext context = RequestContext.getCurrentInstance();
        nuevoTelefono.setCiudad(ciudadSeleccionada);
        ciudadSeleccionada = new Ciudades();
        txt_ciudadTel = nuevoTelefono.getCiudad().getNombre();
        filtrarLovCiudades = null;
        aceptar = true;
        permitirIndexTelefono = true;
        PrimefacesContextUI.actualizar("form:ciudadModTelefono");
        context.reset("formLovs:formDTelefono:formCiudadTel:lovCiudadTelefono:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovCiudadTelefono').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('lovCiudadTelefono').unselectAllRows()");

        PrimefacesContextUI.actualizar("formLovs:formDTelefono:formCiudadTel:CiudadTelefonoDialogo");
        PrimefacesContextUI.actualizar("formLovs:formDTelefono:formCiudadTel:lovCiudadTelefono");
        PrimefacesContextUI.actualizar("formLovs:formDTelefono:formCiudadTel:aceptarCT");
        PrimefacesContextUI.ejecutar("PF('CiudadTelefonoDialogo').hide()");
    }

    public void cancelarParametroCiudadTelefono() {
        ciudadSeleccionada = new Ciudades();
        filtrarLovCiudades = null;
        aceptar = true;
        permitirIndexTelefono = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formLovs:formDTelefono:formCiudadTel:lovCiudadTelefono:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovCiudadTelefono').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('lovCiudadTelefono').unselectAllRows()");
        PrimefacesContextUI.ejecutar("PF('CiudadTelefonoDialogo').hide()");
    }

    public void actualizarParametroTipoTelefonoTelefono() {
        RequestContext context = RequestContext.getCurrentInstance();
        nuevoTelefono.setTipotelefono(tipoTelefonoSeleccionado);
        tipoTelefonoSeleccionado = new TiposTelefonos();
        txt_tipoTel = nuevoTelefono.getTipotelefono().getNombre();
        filtrarLovTiposTelefonos = null;
        aceptar = true;
        permitirIndexTelefono = true;
        PrimefacesContextUI.actualizar("form:tipoTelefonoModTelefono");
        context.reset("formLovs:formDTelefono:formTel:lovTipoTelefonoTelefono:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovTipoTelefonoTelefono').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('lovTipoTelefonoTelefono').unselectAllRows()");
        PrimefacesContextUI.actualizar("formLovs:formDTelefono:formTel:TipoTelefonoTelefonoDialogo");
        PrimefacesContextUI.actualizar("formLovs:formDTelefono:formTel:lovTipoTelefonoTelefono");
        PrimefacesContextUI.actualizar("formLovs:formDTelefono:formTel:aceptarTTT");
        PrimefacesContextUI.ejecutar("PF('TipoTelefonoTelefonoDialogo').hide()");
    }

    public void cancelarParametroTipoTelefonoTelefono() {
        tipoTelefonoSeleccionado = new TiposTelefonos();
        filtrarLovTiposTelefonos = null;
        aceptar = true;
        permitirIndexTelefono = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formLovs:formDTelefono:formTel:lovTipoTelefonoTelefono:globalFilter");

        PrimefacesContextUI.ejecutar("PF('lovTipoTelefonoTelefono').clearFilters()");

        PrimefacesContextUI.ejecutar("PF('lovTipoTelefonoTelefono').unselectAllRows()");

        PrimefacesContextUI.ejecutar("PF('TipoTelefonoTelefonoDialogo').hide()");
    }

    public void actualizarParametroCiudadDireccion() {
        RequestContext context = RequestContext.getCurrentInstance();
        nuevaDireccion.setCiudad(ciudadSeleccionada);
        ciudadSeleccionada = new Ciudades();
        txt_ciudadDir = nuevaDireccion.getCiudad().getNombre();
        filtrarLovCiudades = null;
        aceptar = true;
        permitirIndexDireccion = true;
        PrimefacesContextUI.actualizar("form:ciudadModDireccion");
        context.reset("formLovs:formDDireccion:lovCiudadDireccion:globalFilter");

        PrimefacesContextUI.ejecutar("PF('lovCiudadDireccion').clearFilters()");

        PrimefacesContextUI.ejecutar("PF('lovCiudadDireccion').unselectAllRows()");

        PrimefacesContextUI.actualizar("formLovs:formDDireccion:CiudadDireccionDialogo");
        PrimefacesContextUI.actualizar("formLovs:formDDireccion:lovCiudadDireccion");
        PrimefacesContextUI.actualizar("formLovs:formDDireccion:aceptarCD");

        PrimefacesContextUI.ejecutar("PF('CiudadDireccionDialogo').hide()");
    }

    public void cancelarParametroCiudadDireccion() {
        ciudadSeleccionada = new Ciudades();
        filtrarLovCiudades = null;
        aceptar = true;
        permitirIndexDireccion = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formLovs:formDDireccion:lovCiudadDireccion:globalFilter");

        PrimefacesContextUI.ejecutar("PF('lovCiudadDireccion').clearFilters()");

        PrimefacesContextUI.ejecutar("PF('lovCiudadDireccion').unselectAllRows()");

        PrimefacesContextUI.ejecutar("PF('CiudadDireccionDialogo').hide()");
    }

    public void actualizarParametroEstadoEstadoCivil() {
        RequestContext context = RequestContext.getCurrentInstance();
        nuevoEstadoCivil.setEstadocivil(estadoCivilSeleccionado);
        estadoCivilSeleccionado = new EstadosCiviles();
        txt_estadoC = nuevoEstadoCivil.getEstadocivil().getDescripcion();
        filtrarLovEstadosCiviles = null;
        aceptar = true;
        permitirIndexEstadoCivil = true;
        PrimefacesContextUI.actualizar("form:estadoCivilModEstadoCivil");
        context.reset("formLovs:formDEstadoCivil:lovEstadoCivilEstadoCivil:globalFilter");

        PrimefacesContextUI.ejecutar("PF('lovEstadoCivilEstadoCivil').clearFilters()");

        PrimefacesContextUI.ejecutar("PF('lovEstadoCivilEstadoCivil').unselectAllRows()");

        PrimefacesContextUI.actualizar("formLovs:formDEstadoCivil:EstadoCivilEstadoCivilDialogo");
        PrimefacesContextUI.actualizar("formLovs:formDEstadoCivil:lovEstadoCivilEstadoCivil");
        PrimefacesContextUI.actualizar("formLovs:formDEstadoCivil:aceptarECEC");

        PrimefacesContextUI.ejecutar("PF('EstadoCivilEstadoCivilDialogo').hide()");
    }

    public void cancelarParametroEstadoEstadoCivil() {
        estadoCivilSeleccionado = new EstadosCiviles();
        filtrarLovEstadosCiviles = null;
        aceptar = true;
        permitirIndexEstadoCivil = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formLovs:formDEstadoCivil:lovEstadoCivilEstadoCivil:globalFilter");

        PrimefacesContextUI.ejecutar("PF('lovEstadoCivilEstadoCivil').clearFilters()");

        PrimefacesContextUI.ejecutar("PF('lovEstadoCivilEstadoCivil').unselectAllRows()");

        PrimefacesContextUI.ejecutar("PF('EstadoCivilEstadoCivilDialogo').hide()");
    }

    public void actualizarParametroEPSAfiliacion() {
        RequestContext context = RequestContext.getCurrentInstance();
        nuevaVigenciaAfiliacionEPS.setTercerosucursal(terceroSucursalSeleccionado);
        terceroSucursalSeleccionado = new TercerosSucursales();
        txt_terceroEPS = nuevaVigenciaAfiliacionEPS.getTercerosucursal().getTercero().getNombre();
        filtrarLovTercerosSucursales = null;
        aceptar = true;
        TiposEntidades eps = administrarPersonaIndividual.buscarTipoEntidadPorCodigo(new Short("1"));
        nuevaVigenciaAfiliacionEPS.setTipoentidad(eps);
        permitirIndexAfiliacionEPS = true;
        PrimefacesContextUI.actualizar("form:epsModAfiliaciones");
        context.reset("formLovs:formDAfiliacion:lovTerceroAfiliacion:globalFilter");

        PrimefacesContextUI.ejecutar("PF('lovTerceroAfiliacion').clearFilters()");

        PrimefacesContextUI.ejecutar("PF('lovTerceroAfiliacion').unselectAllRows()");
        PrimefacesContextUI.actualizar("formLovs:formDAfiliacion:TerceroAfiliacionDialogo");
        PrimefacesContextUI.actualizar("formLovs:formDAfiliacion:lovTerceroAfiliacion");
        PrimefacesContextUI.actualizar("formLovs:formDAfiliacion:aceptarTSA");

        PrimefacesContextUI.ejecutar("PF('TerceroAfiliacionDialogo').hide()");
        consultarCodigoSS();
    }

    public void cancelarParametroEPSAfiliacion() {
        terceroSucursalSeleccionado = new TercerosSucursales();
        filtrarLovTercerosSucursales = null;
        aceptar = true;
        permitirIndexAfiliacionEPS = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formLovs:formDAfiliacion:lovTerceroAfiliacion:globalFilter");

        PrimefacesContextUI.ejecutar("PF('lovTerceroAfiliacion').clearFilters()");

        PrimefacesContextUI.ejecutar("PF('lovTerceroAfiliacion').unselectAllRows()");

        PrimefacesContextUI.ejecutar("PF('TerceroAfiliacionDialogo').hide()");
    }

    public void actualizarParametroCajaAfiliacion() {
        RequestContext context = RequestContext.getCurrentInstance();
        nuevaVigenciaAfiliacionCaja.setTercerosucursal(terceroSucursalSeleccionado);
        terceroSucursalSeleccionado = new TercerosSucursales();
        txt_terceroCC = nuevaVigenciaAfiliacionCaja.getTercerosucursal().getTercero().getNombre();
        filtrarLovTercerosSucursales = null;
        aceptar = true;
        TiposEntidades caja = administrarPersonaIndividual.buscarTipoEntidadPorCodigo(new Short("14"));
        nuevaVigenciaAfiliacionCaja.setTipoentidad(caja);
        permitirIndexAfiliacionCaja = true;
        PrimefacesContextUI.actualizar("form:cajaCompensacionModAfiliaciones");
        context.reset("formLovs:formDAfiliacion:lovTerceroAfiliacion:globalFilter");

        PrimefacesContextUI.ejecutar("PF('lovTerceroAfiliacion').clearFilters()");

        PrimefacesContextUI.ejecutar("PF('lovTerceroAfiliacion').unselectAllRows()");
        PrimefacesContextUI.actualizar("formLovs:formDAfiliacion:TerceroAfiliacionDialogo");
        PrimefacesContextUI.actualizar("formLovs:formDAfiliacion:lovTerceroAfiliacion");
        PrimefacesContextUI.actualizar("formLovs:formDAfiliacion:aceptarTSA");

        PrimefacesContextUI.ejecutar("PF('TerceroAfiliacionDialogo').hide()");
    }

    public void cancelarParametroCajaAfiliacion() {
        terceroSucursalSeleccionado = new TercerosSucursales();
        filtrarLovTercerosSucursales = null;
        aceptar = true;
        permitirIndexAfiliacionCaja = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formLovs:formDAfiliacion:lovTerceroAfiliacion:globalFilter");

        PrimefacesContextUI.ejecutar("PF('lovTerceroAfiliacion').clearFilters()");

        PrimefacesContextUI.ejecutar("PF('lovTerceroAfiliacion').unselectAllRows()");

        PrimefacesContextUI.ejecutar("PF('TerceroAfiliacionDialogo').hide()");
    }

    public void actualizarParametroARPAfiliacion() {
        RequestContext context = RequestContext.getCurrentInstance();
        nuevaVigenciaAfiliacionARP.setTercerosucursal(terceroSucursalSeleccionado);
        terceroSucursalSeleccionado = new TercerosSucursales();
        txt_terceroARL = nuevaVigenciaAfiliacionARP.getTercerosucursal().getTercero().getNombre();
        filtrarLovTercerosSucursales = null;
        aceptar = true;
        TiposEntidades arp = administrarPersonaIndividual.buscarTipoEntidadPorCodigo(new Short("2"));
        nuevaVigenciaAfiliacionARP.setTipoentidad(arp);
        permitirIndexAfiliacionARP = true;
        PrimefacesContextUI.actualizar("form:arpModAfiliaciones");
        context.reset("formLovs:formDAfiliacion:lovTerceroAfiliacion:globalFilter");

        PrimefacesContextUI.ejecutar("PF('lovTerceroAfiliacion').clearFilters()");

        PrimefacesContextUI.ejecutar("PF('lovTerceroAfiliacion').unselectAllRows()");
        PrimefacesContextUI.actualizar("formLovs:formDAfiliacion:TerceroAfiliacionDialogo");
        PrimefacesContextUI.actualizar("formLovs:formDAfiliacion:lovTerceroAfiliacion");
        PrimefacesContextUI.actualizar("formLovs:formDAfiliacion:aceptarTSA");

        consultarCodigoSC();

        PrimefacesContextUI.ejecutar("PF('TerceroAfiliacionDialogo').hide()");
    }

    public void cancelarParametroARPAfiliacion() {
        terceroSucursalSeleccionado = new TercerosSucursales();
        filtrarLovTercerosSucursales = null;
        aceptar = true;
        permitirIndexAfiliacionARP = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formLovs:formDAfiliacion:lovTerceroAfiliacion:globalFilter");

        PrimefacesContextUI.ejecutar("PF('lovTerceroAfiliacion').clearFilters()");

        PrimefacesContextUI.ejecutar("PF('lovTerceroAfiliacion').unselectAllRows()");

        PrimefacesContextUI.ejecutar("PF('TerceroAfiliacionDialogo').hide()");
    }

    public void actualizarParametroAFPAfiliacion() {
        RequestContext context = RequestContext.getCurrentInstance();
        nuevaVigenciaAfiliacionAFP.setTercerosucursal(terceroSucursalSeleccionado);
        terceroSucursalSeleccionado = new TercerosSucursales();
        txt_terceroAFP = nuevaVigenciaAfiliacionAFP.getTercerosucursal().getTercero().getNombre();
        filtrarLovTercerosSucursales = null;
        aceptar = true;
        TiposEntidades afp = administrarPersonaIndividual.buscarTipoEntidadPorCodigo(new Short("3"));
        nuevaVigenciaAfiliacionAFP.setTipoentidad(afp);
        permitirIndexAfiliacionAFP = true;
        PrimefacesContextUI.actualizar("form:afpModAfiliaciones");

        context.reset("formLovs:formDAfiliacion:lovTerceroAfiliacion:globalFilter");

        PrimefacesContextUI.ejecutar("PF('lovTerceroAfiliacion').clearFilters()");

        PrimefacesContextUI.ejecutar("PF('lovTerceroAfiliacion').unselectAllRows()");
        PrimefacesContextUI.actualizar("formLovs:formDAfiliacion:TerceroAfiliacionDialogo");
        PrimefacesContextUI.actualizar("formLovs:formDAfiliacion:lovTerceroAfiliacion");
        context.reset("formLovs:formDAfiliacion:lovTerceroAfiliacion:globalFilter");

        PrimefacesContextUI.ejecutar("PF('TerceroAfiliacionDialogo').hide()");
        consultarCodigoSP();
    }

    public void cancelarParametroAFPAfiliacion() {
        terceroSucursalSeleccionado = new TercerosSucursales();
        filtrarLovTercerosSucursales = null;
        aceptar = true;
        permitirIndexAfiliacionAFP = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formLovs:formDAfiliacion:lovTerceroAfiliacion:globalFilter");

        PrimefacesContextUI.ejecutar("PF('lovTerceroAfiliacion').clearFilters()");

        PrimefacesContextUI.ejecutar("PF('lovTerceroAfiliacion').unselectAllRows()");

        PrimefacesContextUI.ejecutar("PF('TerceroAfiliacionDialogo').hide()");
    }

    public void actualizarParametroFondoAfiliacion() {
        RequestContext context = RequestContext.getCurrentInstance();
        nuevaVigenciaAfiliacionFondo.setTercerosucursal(terceroSucursalSeleccionado);
        terceroSucursalSeleccionado = new TercerosSucursales();
        txt_terceroCes = nuevaVigenciaAfiliacionFondo.getTercerosucursal().getTercero().getNombre();
        filtrarLovTercerosSucursales = null;
        aceptar = true;
        TiposEntidades fondo = administrarPersonaIndividual.buscarTipoEntidadPorCodigo(new Short("12"));
        nuevaVigenciaAfiliacionFondo.setTipoentidad(fondo);
        permitirIndexAfiliacionFondo = true;
        PrimefacesContextUI.actualizar("form:fondoCensantiasModAfiliaciones");
        context.reset("formLovs:formDAfiliacion:lovTerceroAfiliacion:globalFilter");

        PrimefacesContextUI.ejecutar("PF('lovTerceroAfiliacion').clearFilters()");

        PrimefacesContextUI.ejecutar("PF('lovTerceroAfiliacion').unselectAllRows()");

        PrimefacesContextUI.actualizar("formLovs:formDAfiliacion:TerceroAfiliacionDialogo");
        PrimefacesContextUI.actualizar("formLovs:formDAfiliacion:lovTerceroAfiliacion");
        PrimefacesContextUI.actualizar("formLovs:formDAfiliacion:aceptarTSA");

        PrimefacesContextUI.ejecutar("PF('TerceroAfiliacionDialogo').hide()");
    }

    public void cancelarParametroFondoAfiliacion() {
        terceroSucursalSeleccionado = new TercerosSucursales();
        filtrarLovTercerosSucursales = null;
        aceptar = true;
        permitirIndexAfiliacionFondo = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formLovs:formDAfiliacion:lovTerceroAfiliacion:globalFilter");

        PrimefacesContextUI.ejecutar("PF('lovTerceroAfiliacion').clearFilters()");

        PrimefacesContextUI.ejecutar("PF('lovTerceroAfiliacion').unselectAllRows()");

        PrimefacesContextUI.ejecutar("PF('TerceroAfiliacionDialogo').hide()");
    }

    public void actualizarParametroJornadaJornadaLaboral() {
        RequestContext context = RequestContext.getCurrentInstance();
        nuevaVigenciaJornada.setJornadatrabajo(jornadaLaboralSeleccionada);
        jornadaLaboralSeleccionada = new JornadasLaborales();
        txt_jornada = nuevaVigenciaJornada.getJornadatrabajo().getDescripcion();
        filtrarLovJornadasLaborales = null;
        aceptar = true;
        permitirIndexJornadaLaboral = true;
        PrimefacesContextUI.actualizar("form:jornadaLaboralModJornadaLaboral");
        context.reset("formLovs:formDJornadaLaboral:lovJornadaJornadaLaboral:globalFilter");

        PrimefacesContextUI.ejecutar("PF('lovJornadaJornadaLaboral').clearFilters()");

        PrimefacesContextUI.ejecutar("PF('lovJornadaJornadaLaboral').unselectAllRows()");
        PrimefacesContextUI.actualizar("formLovs:formDJornadaLaboral:JornadaJornadaLaboralDialogo");
        PrimefacesContextUI.actualizar("formLovs:formDJornadaLaboral:lovJornadaJornadaLaboral");
        PrimefacesContextUI.actualizar("formLovs:formDJornadaLaboral:aceptarJLJL");

        PrimefacesContextUI.ejecutar("PF('JornadaJornadaLaboralDialogo').hide()");
    }

    public void cancelarParametroJornadaJornadaLaboral() {
        jornadaLaboralSeleccionada = new JornadasLaborales();
        filtrarLovJornadasLaborales = null;
        aceptar = true;
        permitirIndexJornadaLaboral = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formLovs:formDJornadaLaboral:lovJornadaJornadaLaboral:globalFilter");

        PrimefacesContextUI.ejecutar("PF('lovJornadaJornadaLaboral').clearFilters()");

        PrimefacesContextUI.ejecutar("PF('lovJornadaJornadaLaboral').unselectAllRows()");

        PrimefacesContextUI.ejecutar("PF('JornadaJornadaLaboralDialogo').hide()");
    }

    public void actualizarParametroMetodoFormaPago() {
        RequestContext context = RequestContext.getCurrentInstance();
        nuevaVigenciaFormaPago.setMetodopago(metodoPagoSeleccionado);
        metodoPagoSeleccionado = new MetodosPagos();
        txt_metodoP = nuevaVigenciaFormaPago.getMetodopago().getDescripcion();
        filtrarLovMetodosPagos = null;
        aceptar = true;
        permitirIndexFormaPago = true;
        PrimefacesContextUI.actualizar("form:metodoPagoModFormaPago");
        context.reset("formLovs:formDFormaPago:formMetodo:lovMetodoPagoFormaPago:globalFilter");

        PrimefacesContextUI.ejecutar("PF('lovMetodoPagoFormaPago').clearFilters()");

        PrimefacesContextUI.ejecutar("PF('lovMetodoPagoFormaPago').unselectAllRows()");
        PrimefacesContextUI.actualizar("formLovs:formDFormaPago:formMetodo:MetodoPagoFormaPagoDialogo");
        PrimefacesContextUI.actualizar("formLovs:formDFormaPago:formMetodo:lovMetodoPagoFormaPago");
        PrimefacesContextUI.actualizar("formLovs:formDFormaPago:formMetodo:aceptarMPFP");

        PrimefacesContextUI.ejecutar("PF('MetodoPagoFormaPagoDialogo').hide()");
    }

    public void cancelarParametroMetodoFormaPago() {
        metodoPagoSeleccionado = new MetodosPagos();
        filtrarLovMetodosPagos = null;
        aceptar = true;
        permitirIndexFormaPago = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formLovs:formDFormaPago:formMetodo:lovMetodoPagoFormaPago:globalFilter");

        PrimefacesContextUI.ejecutar("PF('lovMetodoPagoFormaPago').clearFilters()");

        PrimefacesContextUI.ejecutar("PF('lovMetodoPagoFormaPago').unselectAllRows()");

        PrimefacesContextUI.ejecutar("PF('MetodoPagoFormaPagoDialogo').hide()");
    }

    public void actualizarParametroSucursalFormaPago() {
        RequestContext context = RequestContext.getCurrentInstance();
        nuevaVigenciaFormaPago.setSucursal(sucursalSeleccionada);
        sucursalSeleccionada = new Sucursales();
        filtrarLovSucursales = null;
        aceptar = true;
        permitirIndexFormaPago = true;
        PrimefacesContextUI.actualizar("form:sucursalPagoModFormaPago");

        context.reset("formLovs:formDFormaPago:formSucursal:lovSucursalPagoFormaPago:globalFilter");

        PrimefacesContextUI.ejecutar("PF('lovSucursalPagoFormaPago').clearFilters()");

        PrimefacesContextUI.ejecutar("PF('lovSucursalPagoFormaPago').unselectAllRows()");
        PrimefacesContextUI.actualizar("formLovs:formDFormaPago:formSucursal:SucursalFormaPagoDialogo");
        PrimefacesContextUI.actualizar("formLovs:formDFormaPago:formSucursal:lovSucursalPagoFormaPago");
        PrimefacesContextUI.actualizar("formLovs:formDFormaPago:formSucursal:aceptarSFP");

        PrimefacesContextUI.ejecutar("PF('SucursalFormaPagoDialogo').hide()");
    }

    public void cancelarParametroSucursalFormaPago() {
        sucursalSeleccionada = new Sucursales();
        filtrarLovSucursales = null;
        aceptar = true;
        permitirIndexFormaPago = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formLovs:formDFormaPago:formSucursal:lovSucursalPagoFormaPago:globalFilter");

        PrimefacesContextUI.ejecutar("PF('lovSucursalPagoFormaPago').clearFilters()");

        PrimefacesContextUI.ejecutar("PF('lovSucursalPagoFormaPago').unselectAllRows()");

        PrimefacesContextUI.ejecutar("PF('SucursalFormaPagoDialogo').hide()");
    }

    public void actualizarParametroPeriodicidadFormaPago() {
        RequestContext context = RequestContext.getCurrentInstance();
        nuevaVigenciaFormaPago.setFormapago(periodicidadSeleccionada);
        periodicidadSeleccionada = new Periodicidades();
        txt_formaP = nuevaVigenciaFormaPago.getFormapago().getNombre();
        filtrarLovPeriodicidades = null;
        aceptar = true;
        permitirIndexFormaPago = true;
        PrimefacesContextUI.actualizar("form:formaPagoModFormaPago");

        context.reset("formLovs:formDFormaPago:formPeriodicidad:lovPeriodicidadFormaPago:globalFilter");

        PrimefacesContextUI.ejecutar("PF('lovPeriodicidadFormaPago').clearFilters()");

        PrimefacesContextUI.ejecutar("PF('lovPeriodicidadFormaPago').unselectAllRows()");
        PrimefacesContextUI.actualizar("formLovs:formDFormaPago:formPeriodicidad:PeriodicidadFormaPagoDialogo");
        PrimefacesContextUI.actualizar("formLovs:formDFormaPago:formPeriodicidad:lovPeriodicidadFormaPago");
        PrimefacesContextUI.actualizar("formLovs:formDFormaPago:formPeriodicidad:aceptarPFP");

        PrimefacesContextUI.ejecutar("PF('PeriodicidadFormaPagoDialogo').hide()");
    }

    public void cancelarParametroPeriodicidadFormaPago() {
        periodicidadSeleccionada = new Periodicidades();
        filtrarLovPeriodicidades = null;
        aceptar = true;
        permitirIndexFormaPago = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formLovs:formDFormaPago:formPeriodicidad:lovPeriodicidadFormaPago:globalFilter");

        PrimefacesContextUI.ejecutar("PF('lovPeriodicidadFormaPago').clearFilters()");

        PrimefacesContextUI.ejecutar("PF('lovPeriodicidadFormaPago').unselectAllRows()");

        PrimefacesContextUI.ejecutar("PF('PeriodicidadFormaPagoDialogo').hide()");
    }

    public void actualizarParametroUbicacionUbicacionGeografica() {
        RequestContext context = RequestContext.getCurrentInstance();
        nuevaVigenciaUbicacion.setUbicacion(ubicacionGeograficaSeleccionada);
        ubicacionGeograficaSeleccionada = new UbicacionesGeograficas();
        txt_ubicacionG = nuevaVigenciaUbicacion.getUbicacion().getDescripcion();
        filtrarLovUbicacionesGeograficas = null;
        aceptar = true;
        permitirIndexUbicacionGeografica = true;
        PrimefacesContextUI.actualizar("form:ubicacionGeograficaModUbicacionGeografica");
        context.reset("formLovs:formDUbicacion:lovUbicacionUbicacionGeografica:globalFilter");

        PrimefacesContextUI.ejecutar("PF('lovUbicacionUbicacionGeografica').clearFilters()");

        PrimefacesContextUI.ejecutar("PF('lovUbicacionUbicacionGeografica').unselectAllRows()");
        PrimefacesContextUI.actualizar("formLovs:formDUbicacion:UbicacionUbicacionGeograficaDialogo");
        PrimefacesContextUI.actualizar("formLovs:formDUbicacion:lovUbicacionUbicacionGeografica");
        PrimefacesContextUI.actualizar("formLovs:formDUbicacion:aceptarUGUG");

        PrimefacesContextUI.ejecutar("PF('UbicacionUbicacionGeograficaDialogo').hide()");
    }

    public void cancelarParametroUbicacionUbicacionGeografica() {
        ubicacionGeograficaSeleccionada = new UbicacionesGeograficas();
        filtrarLovUbicacionesGeograficas = null;
        aceptar = true;
        permitirIndexUbicacionGeografica = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formLovs:formDUbicacion:lovUbicacionUbicacionGeografica:globalFilter");

        PrimefacesContextUI.ejecutar("PF('lovUbicacionUbicacionGeografica').clearFilters()");

        PrimefacesContextUI.ejecutar("PF('lovUbicacionUbicacionGeografica').unselectAllRows()");

        PrimefacesContextUI.ejecutar("PF('UbicacionUbicacionGeograficaDialogo').hide()");
    }

    public void actualizarParametroContratoLegislacionLaboral() {
        RequestContext context = RequestContext.getCurrentInstance();
        nuevaVigenciaContrato.setContrato(contratoSeleccionado);
        contratoSeleccionado = new Contratos();
        filtrarLovContratos = null;
        aceptar = true;
        permitirIndexLegislacionLaboral = true;
        PrimefacesContextUI.actualizar("form:legislacionLaboralModLegislacionLaboral");
        context.reset("formLovs:formDLegislacionLaboral:lovContratoLegislacionLaboral:globalFilter");

        PrimefacesContextUI.ejecutar("PF('lovContratoLegislacionLaboral').clearFilters()");

        PrimefacesContextUI.ejecutar("PF('lovContratoLegislacionLaboral').unselectAllRows()");
        PrimefacesContextUI.actualizar("formLovs:formDLegislacionLaboral:ContratoLegislacionLaboralDialogo");
        PrimefacesContextUI.actualizar("formLovs:formDLegislacionLaboral:lovContratoLegislacionLaboral");
        PrimefacesContextUI.actualizar("formLovs:formDLegislacionLaboral:lovContratoLegislacionLaboral");
        PrimefacesContextUI.actualizar("formLovs:formDLegislacionLaboral:aceptarCLL");

        PrimefacesContextUI.ejecutar("PF('ContratoLegislacionLaboralDialogo').hide()");
        validarTipoTrabajadorContrato();
    }

    public void cancelarParametroContratoLegislacionLaboral() {
        contratoSeleccionado = new Contratos();
        filtrarLovContratos = null;
        aceptar = true;
        permitirIndexLegislacionLaboral = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formLovs:formDLegislacionLaboral:lovContratoLegislacionLaboral:globalFilter");

        PrimefacesContextUI.ejecutar("PF('lovContratoLegislacionLaboral').clearFilters()");

        PrimefacesContextUI.ejecutar("PF('lovContratoLegislacionLaboral').unselectAllRows()");

        PrimefacesContextUI.ejecutar("PF('ContratoLegislacionLaboralDialogo').hide()");
    }

    public void actualizarParametroNormaNormaLaboral() {
        RequestContext context = RequestContext.getCurrentInstance();
        nuevaVigenciaNormaEmpleado.setNormalaboral(normaLaboralSeleccionada);
        normaLaboralSeleccionada = new NormasLaborales();
        filtrarLovNormasLaborales = null;
        aceptar = true;
        permitirIndexNormaLaboral = true;
        PrimefacesContextUI.actualizar("form:normaLaboralModNormaLaboral");

        context.reset("formLovs:formDNormaLaboral:lovNormaLaboralNormaLaboral:globalFilter");

        PrimefacesContextUI.ejecutar("PF('lovNormaLaboralNormaLaboral').clearFilters()");

        PrimefacesContextUI.ejecutar("PF('lovNormaLaboralNormaLaboral').unselectAllRows()");
        PrimefacesContextUI.actualizar("formLovs:formDNormaLaboral:NormaLaboralNormaLaboralDialogo");
        PrimefacesContextUI.actualizar("formLovs:formDNormaLaboral:lovNormaLaboralNormaLaboral");
        PrimefacesContextUI.actualizar("formLovs:formDNormaLaboral:lovNormaLaboralNormaLaboral");
        PrimefacesContextUI.actualizar("formLovs:formDNormaLaboral:aceptarNLNL");

        PrimefacesContextUI.ejecutar("PF('NormaLaboralNormaLaboralDialogo').hide()");
        validarTipoTrabajadorNormaLaboral();
    }

    public void cancelarParametroNormaNormaLaboral() {
        normaLaboralSeleccionada = new NormasLaborales();
        filtrarLovNormasLaborales = null;
        aceptar = true;
        permitirIndexNormaLaboral = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formLovs:formDNormaLaboral:lovNormaLaboralNormaLaboral:globalFilter");

        PrimefacesContextUI.ejecutar("PF('lovNormaLaboralNormaLaboral').clearFilters()");

        PrimefacesContextUI.ejecutar("PF('lovNormaLaboralNormaLaboral').unselectAllRows()");

        PrimefacesContextUI.ejecutar("PF('NormaLaboralNormaLaboralDialogo').hide()");
    }

    public void actualizarParametroMotivoContratoTipoContrato() {
        RequestContext context = RequestContext.getCurrentInstance();
        nuevaVigenciaTipoContrato.setMotivocontrato(motivoContratoSeleccionado);
        motivoContratoSeleccionado = new MotivosContratos();
        txt_motivoContrato = nuevaVigenciaTipoContrato.getMotivocontrato().getNombre();
        filtrarLovMotivosContratos = null;
        aceptar = true;
        permitirIndexTipoContrato = true;
        PrimefacesContextUI.actualizar("form:motivoContratoModTipoContrato");

        context.reset("formLovs:formDTipoContrato:formTipoContrat:lovMotivoContratoTipoContrato:globalFilter");

        PrimefacesContextUI.ejecutar("PF('lovMotivoContratoTipoContrato').clearFilters()");

        PrimefacesContextUI.ejecutar("PF('lovMotivoContratoTipoContrato').unselectAllRows()");
        PrimefacesContextUI.actualizar("formLovs:formDTipoContrato:formTipoContrat:MotivoContratoTipoContratoDialogo");
        PrimefacesContextUI.actualizar("formLovs:formDTipoContrato:formTipoContrat:lovMotivoContratoTipoContrato");
        PrimefacesContextUI.actualizar("formLovs:formDTipoContrato:formTipoContrat:lovMotivoContratoTipoContrato");
        PrimefacesContextUI.actualizar("formLovs:formDTipoContrato:formTipoContrat:aceptarMCTC");

        PrimefacesContextUI.ejecutar("PF('MotivoContratoTipoContratoDialogo').hide()");
    }

    public void cancelarParametroMotivoContratoTipoContrato() {
        motivoContratoSeleccionado = new MotivosContratos();
        filtrarLovMotivosContratos = null;
        aceptar = true;
        permitirIndexTipoContrato = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formLovs:formDTipoContrato:formTipoContrat:lovMotivoContratoTipoContrato:globalFilter");

        PrimefacesContextUI.ejecutar("PF('lovMotivoContratoTipoContrato').clearFilters()");

        PrimefacesContextUI.ejecutar("PF('lovMotivoContratoTipoContrato').unselectAllRows()");

        PrimefacesContextUI.ejecutar("PF('MotivoContratoTipoContratoDialogo').hide()");
    }

    public void actualizarParametroTipoContratoTipoContrato() {
        RequestContext context = RequestContext.getCurrentInstance();
        nuevaVigenciaTipoContrato.setTipocontrato(tipoContratoSeleccionado);
        tipoContratoSeleccionado = new TiposContratos();
        txt_tipoContrato = nuevaVigenciaTipoContrato.getTipocontrato().getNombre();
        filtrarLovTiposContratos = null;
        aceptar = true;
        permitirIndexTipoContrato = true;
        PrimefacesContextUI.actualizar("form:tipoContratoModTipoContrato");
        context.reset("formLovs:formDTipoContrato:formTipoContrato:lovTipoContratoTipoContrato:globalFilter");

        PrimefacesContextUI.ejecutar("PF('lovTipoContratoTipoContrato').clearFilters()");

        PrimefacesContextUI.ejecutar("PF('lovTipoContratoTipoContrato').unselectAllRows()");
        PrimefacesContextUI.actualizar("formLovs:formDTipoContrato:formTipoContrato:TipoContratoTipoContratoDialogo");
        PrimefacesContextUI.actualizar("formLovs:formDTipoContrato:formTipoContrato:lovTipoContratoTipoContrato");
        PrimefacesContextUI.actualizar("formLovs:formDTipoContrato:formTipoContrato:lovTipoContratoTipoContrato");
        PrimefacesContextUI.actualizar("formLovs:formDTipoContrato:formTipoContrato:aceptarTCTC");

        PrimefacesContextUI.ejecutar("PF('TipoContratoTipoContratoDialogo').hide()");
        validarTipoTrabajadorTipoContrato();
    }

    public void cancelarParametroTipoContratoTipoContrato() {
        tipoContratoSeleccionado = new TiposContratos();
        filtrarLovTiposContratos = null;
        aceptar = true;
        permitirIndexTipoContrato = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formLovs:formDTipoContrato:formTipoContrato:lovTipoContratoTipoContrato:globalFilter");

        PrimefacesContextUI.ejecutar("PF('lovTipoContratoTipoContrato').clearFilters()");

        PrimefacesContextUI.ejecutar("PF('lovTipoContratoTipoContrato').unselectAllRows()");

        PrimefacesContextUI.ejecutar("PF('TipoContratoTipoContratoDialogo').hide()");
    }

    public void actualizarParametroMotivoCambioSueldoSueldo() {
        RequestContext context = RequestContext.getCurrentInstance();
        nuevaVigenciaSueldo.setMotivocambiosueldo(motivoCambioSueldoSeleccionado);
        motivoCambioSueldoSeleccionado = new MotivosCambiosSueldos();
        txt_motivoSu = nuevaVigenciaSueldo.getMotivocambiosueldo().getNombre();
        filtrarLovMotivosCambiosSueldos = null;
        aceptar = true;
        permitirIndexSueldo = true;
        PrimefacesContextUI.actualizar("form:motivoSueldoModSueldo");
        context.reset("formLovs:formDSueldo:formMotivoSu:lovMotivoCambioSueldoSueldo:globalFilter");

        PrimefacesContextUI.ejecutar("PF('lovMotivoCambioSueldoSueldo').clearFilters()");

        PrimefacesContextUI.ejecutar("PF('lovMotivoCambioSueldoSueldo').unselectAllRows()");
        PrimefacesContextUI.actualizar("formLovs:formDSueldo:formMotivoSu:MotivoCambioSueldoSueldoDialogo");
        PrimefacesContextUI.actualizar("formLovs:formDSueldo:formMotivoSu:lovMotivoCambioSueldoSueldo");
        PrimefacesContextUI.actualizar("formLovs:formDSueldo:formMotivoSu:aceptarMCSS");

        PrimefacesContextUI.ejecutar("PF('MotivoCambioSueldoSueldoDialogo').hide()");
    }

    public void cancelarParametroMotivoCambioSueldoSueldo() {
        motivoCambioSueldoSeleccionado = new MotivosCambiosSueldos();
        filtrarLovMotivosCambiosSueldos = null;
        aceptar = true;
        permitirIndexSueldo = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formLovs:formDSueldo:formMotivoSu:lovMotivoCambioSueldoSueldo:globalFilter");

        PrimefacesContextUI.ejecutar("PF('lovMotivoCambioSueldoSueldo').clearFilters()");

        PrimefacesContextUI.ejecutar("PF('lovMotivoCambioSueldoSueldo').unselectAllRows()");

        PrimefacesContextUI.ejecutar("PF('MotivoCambioSueldoSueldoDialogo').hide()");
    }

    public void actualizarParametroTipoSueldoSueldo() {
        RequestContext context = RequestContext.getCurrentInstance();
        nuevaVigenciaSueldo.setTiposueldo(tipoSueldoSeleccionado);
        tipoSueldoSeleccionado = new TiposSueldos();
        txt_TipoSu = nuevaVigenciaSueldo.getTiposueldo().getDescripcion();
        filtrarLovTiposSueldos = null;
        aceptar = true;
        permitirIndexSueldo = true;
        PrimefacesContextUI.actualizar("form:tipoSueldoModSueldo");
        context.reset("formLovs:formDSueldo:formTipoSueldo:lovTipoSueldoSueldo:globalFilter");

        PrimefacesContextUI.ejecutar("PF('lovTipoSueldoSueldo').clearFilters()");

        PrimefacesContextUI.ejecutar("PF('lovTipoSueldoSueldo').unselectAllRows()");
        PrimefacesContextUI.actualizar("formLovs:formDSueldo:formTipoSueldo:TipoSueldoSueldoDialogo");
        PrimefacesContextUI.actualizar("formLovs:formDSueldo:formTipoSueldo:lovTipoSueldoSueldo");
        PrimefacesContextUI.actualizar("formLovs:formDSueldo:formTipoSueldo:lovTipoSueldoSueldo");
        PrimefacesContextUI.actualizar("formLovs:formDSueldo:formTipoSueldo:aceptarTSS");

        PrimefacesContextUI.ejecutar("PF('TipoSueldoSueldoDialogo').hide()");
        validarTipoTrabajadorTipoSueldo();
    }

    public void cancelarParametroTipoSueldoSueldo() {
        tipoSueldoSeleccionado = new TiposSueldos();
        filtrarLovTiposSueldos = null;
        aceptar = true;
        permitirIndexSueldo = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formLovs:formDSueldo:formTipoSueldo:lovTipoSueldoSueldo:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovTipoSueldoSueldo').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('lovTipoSueldoSueldo').unselectAllRows()");
        PrimefacesContextUI.ejecutar("PF('TipoSueldoSueldoDialogo').hide()");
    }

    public void actualizarParametroReformaLaboralTipoSalario() {
        RequestContext context = RequestContext.getCurrentInstance();
        nuevaVigenciaReformaLaboral.setReformalaboral(reformaLaboralSeleccionada);
        reformaLaboralSeleccionada = new ReformasLaborales();
        filtrarLovReformasLaborales = null;
        aceptar = true;
        permitirIndexTipoSalario = true;
        PrimefacesContextUI.actualizar("form:tipoSalarioModTipoSalario");

        context.reset("formLovs:formDTipoSalario:lovReformaLaboralTipoSalario:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovReformaLaboralTipoSalario').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('lovReformaLaboralTipoSalario').unselectAllRows()");
        PrimefacesContextUI.actualizar("formLovs:formDTipoSalario:ReformaLaboralTipoSalarioDialogo");
        PrimefacesContextUI.actualizar("formLovs:formDTipoSalario:lovReformaLaboralTipoSalario");
        PrimefacesContextUI.actualizar("formLovs:formDTipoSalario:aceptarRLTS");

        PrimefacesContextUI.ejecutar("PF('ReformaLaboralTipoSalarioDialogo').hide()");
        validarTipoTrabajadorReformaLaboral();
    }

    public void cancelarParametroReformaLaboralTipoSalario() {
        reformaLaboralSeleccionada = new ReformasLaborales();
        filtrarLovReformasLaborales = null;
        aceptar = true;
        permitirIndexTipoSalario = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formLovs:formDTipoSalario:lovReformaLaboralTipoSalario:globalFilter");

        PrimefacesContextUI.ejecutar("PF('lovReformaLaboralTipoSalario').clearFilters()");

        PrimefacesContextUI.ejecutar("PF('lovReformaLaboralTipoSalario').unselectAllRows()");

        PrimefacesContextUI.ejecutar("PF('ReformaLaboralTipoSalarioDialogo').hide()");
    }

    public void actualizarParametroTipoTrabajadorTipoTrabajador() {
        RequestContext context = RequestContext.getCurrentInstance();
        System.out.println("tipoTrabajadorSeleccionado : " + tipoTrabajadorSeleccionado);
        System.out.println("tipoTrabajadorSeleccionado.secuencia : " + tipoTrabajadorSeleccionado.getSecuencia());
        nuevaVigenciaTipoTrabajador.setTipotrabajador(tipoTrabajadorSeleccionado);
        txt_tipoT = nuevaVigenciaTipoTrabajador.getTipotrabajador().getNombre();
        System.out.println("nuevaVigenciaSueldo.getMotivocambiosueldo() : " + nuevaVigenciaSueldo.getMotivocambiosueldo());
        if (nuevaVigenciaSueldo.getMotivocambiosueldo() != null) {
            System.out.println("nuevaVigenciaSueldo.getMotivocambiosueldo().getNombre() :" + nuevaVigenciaSueldo.getMotivocambiosueldo().getNombre());
        }
        System.out.println("nuevaVigenciaTipoContrato.getMotivocontrato() : " + nuevaVigenciaTipoContrato.getMotivocontrato());
        if (nuevaVigenciaSueldo.getMotivocambiosueldo() != null) {
            System.out.println("nuevaVigenciaTipoContrato.getMotivocontrato().getNombre() :" + nuevaVigenciaTipoContrato.getMotivocontrato().getNombre());
        }
        cargarLovsConTipoTrabajador(nuevaVigenciaTipoTrabajador.getTipotrabajador().getSecuencia());
        System.out.println("Despues de cargarLovsConTipoTrabajador.");
        System.out.println("nuevaVigenciaSueldo.getMotivocambiosueldo() : " + nuevaVigenciaSueldo.getMotivocambiosueldo());
        if (nuevaVigenciaSueldo.getMotivocambiosueldo() != null) {
            System.out.println("nuevaVigenciaSueldo.getMotivocambiosueldo().getNombre() :" + nuevaVigenciaSueldo.getMotivocambiosueldo().getNombre());
        }
        System.out.println("nuevaVigenciaTipoContrato.getMotivocontrato() : " + nuevaVigenciaTipoContrato.getMotivocontrato());
        if (nuevaVigenciaSueldo.getMotivocambiosueldo() != null) {
            System.out.println("nuevaVigenciaTipoContrato.getMotivocontrato().getNombre() :" + nuevaVigenciaTipoContrato.getMotivocontrato().getNombre());
        }
        tipoTrabajadorSeleccionado = new TiposTrabajadores();
        filtrarLovTiposTrabajadores = null;
        aceptar = true;
        permitirIndexTipoTrabajador = true;
        PrimefacesContextUI.actualizar("form:tipoTrabajadorModTipoTrabajador");

        context.reset("formLovs:formDTipoTrabajador:lovTipoTrabajadorTipoTrabajador:globalFilter");
        PrimefacesContextUI.actualizar("formLovs:formDTipoTrabajador:TipoTrabajadorTipoTrabajadorDialogo");

        PrimefacesContextUI.ejecutar("PF('lovTipoTrabajadorTipoTrabajador').clearFilters()");

        PrimefacesContextUI.ejecutar("PF('lovTipoTrabajadorTipoTrabajador').unselectAllRows()");
        PrimefacesContextUI.actualizar("formLovs:formDTipoTrabajador:lovTipoTrabajadorTipoTrabajador");
        PrimefacesContextUI.actualizar("formLovs:formDTipoTrabajador:aceptarTTTT");

        PrimefacesContextUI.ejecutar("PF('TipoTrabajadorTipoTrabajadorDialogo').hide()");
        validarDisableTipoTrabajador();
    }

    public void cancelarParametroTipoTrabajadorTipoTrabajador() {
        tipoTrabajadorSeleccionado = new TiposTrabajadores();
        filtrarLovTiposTrabajadores = null;
        aceptar = true;
        permitirIndexTipoTrabajador = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formLovs:formDTipoTrabajador:lovTipoTrabajadorTipoTrabajador:globalFilter");

        PrimefacesContextUI.ejecutar("PF('lovTipoTrabajadorTipoTrabajador').clearFilters()");

        PrimefacesContextUI.ejecutar("PF('lovTipoTrabajadorTipoTrabajador').unselectAllRows()");

        PrimefacesContextUI.ejecutar("PF('TipoTrabajadorTipoTrabajadorDialogo').hide()");
    }

    public void actualizarParametroEstructuraCentroCosto() {
        RequestContext context = RequestContext.getCurrentInstance();
        nuevaVigenciaLocalizacion.setLocalizacion(estructuraCentroCostoSeleccionada);
        estructuraCentroCostoSeleccionada = new Estructuras();
        filtrarLovEstructurasCentroCosto = null;
        aceptar = true;
        permitirIndexCentroCosto = true;
        PrimefacesContextUI.actualizar("form:estructuraModCentroCosto");

        context.reset("formLovs:formDCentroCosto:formEstrucCC:lovEstructuraCentroCosto:globalFilter");

        PrimefacesContextUI.ejecutar("PF('lovEstructuraCentroCosto').clearFilters()");

        PrimefacesContextUI.ejecutar("PF('lovEstructuraCentroCosto').unselectAllRows()");
        PrimefacesContextUI.actualizar("formLovs:formDCentroCosto:formEstrucCC:EstructuraCentroCostoDialogo");
        PrimefacesContextUI.actualizar("formLovs:formDCentroCosto:formEstrucCC:lovEstructuraCentroCosto");
        PrimefacesContextUI.actualizar("formLovs:formDCentroCosto:formEstrucCC:aceptarECC");

        PrimefacesContextUI.ejecutar("PF('EstructuraCentroCostoDialogo').hide()");
    }

    public void cancelarParametroEstructuraCentroCosto() {
        estructuraCentroCostoSeleccionada = new Estructuras();
        filtrarLovEstructurasCentroCosto = null;
        aceptar = true;
        permitirIndexCentroCosto = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formLovs:formDCentroCosto:formEstrucCC:lovEstructuraCentroCosto:globalFilter");

        PrimefacesContextUI.ejecutar("PF('lovEstructuraCentroCosto').clearFilters()");

        PrimefacesContextUI.ejecutar("PF('lovEstructuraCentroCosto').unselectAllRows()");

        PrimefacesContextUI.ejecutar("PF('EstructuraCentroCostoDialogo').hide()");
    }

    public void actualizarParametroMotivoCentroCosto() {
        RequestContext context = RequestContext.getCurrentInstance();
        nuevaVigenciaLocalizacion.setMotivo(motivoLocalizacionSeleccionado);
        motivoLocalizacionSeleccionado = new MotivosLocalizaciones();
        txt_motivoCentroC = nuevaVigenciaLocalizacion.getMotivo().getDescripcion();
        filtrarLovMotivosCC = null;
        aceptar = true;
        permitirIndexCentroCosto = true;
        PrimefacesContextUI.actualizar("form:motivoModCentroCosto");
        context.reset("formLovs:formDCentroCosto:formCentroC:lovMotivoLocalizacionCentroCosto:globalFilter");

        PrimefacesContextUI.ejecutar("PF('lovMotivoLocalizacionCentroCosto').clearFilters()");

        PrimefacesContextUI.ejecutar("PF('lovMotivoLocalizacionCentroCosto').unselectAllRows()");

        PrimefacesContextUI.actualizar("formLovs:formDCentroCosto:formCentroC:MotivoLocalizacionCentroCostoDialogo");
        PrimefacesContextUI.actualizar("formLovs:formDCentroCosto:formCentroC:lovMotivoLocalizacionCentroCosto");
        PrimefacesContextUI.actualizar("formLovs:formDCentroCosto:formCentroC:aceptarMLCC");

        PrimefacesContextUI.ejecutar("PF('MotivoLocalizacionCentroCostoDialogo').hide()");
    }

    public void cancelarParametroMotivoCentroCosto() {
        motivoLocalizacionSeleccionado = new MotivosLocalizaciones();
        filtrarLovMotivosCC = null;
        aceptar = true;
        permitirIndexCentroCosto = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formLovs:formDCentroCosto:formCentroC:lovMotivoLocalizacionCentroCosto:globalFilter");

        PrimefacesContextUI.ejecutar("PF('lovMotivoLocalizacionCentroCosto').clearFilters()");

        PrimefacesContextUI.ejecutar("PF('lovMotivoLocalizacionCentroCosto').unselectAllRows()");

        PrimefacesContextUI.ejecutar("PF('MotivoLocalizacionCentroCostoDialogo').hide()");
    }

    public void actualizarParametroCargoCargoDesempeñado() {
        RequestContext context = RequestContext.getCurrentInstance();
        nuevaVigenciaCargo.setCargo(cargoSeleccionado);
        cargoSeleccionado = new Cargos();
        txt_cargo = nuevaVigenciaCargo.getCargo().getNombre();
        filtrarLovCargos = null;
        aceptar = true;
        permitirIndexCargoDesempeñado = true;
        PrimefacesContextUI.actualizar("form:cargoModCargoDesempeñado");

        context.reset("formLovs:formDCargoDesempenado:formCargo:lovCargoCargoDesempeñado:globalFilter");

        PrimefacesContextUI.ejecutar("PF('lovCargoCargoDesempeñado').clearFilters()");

        PrimefacesContextUI.ejecutar("PF('lovCargoCargoDesempeñado').unselectAllRows()");
        PrimefacesContextUI.actualizar("formLovs:formDCargoDesempenado:formCargo:CargoCargoDesempeñadoDialogo");
        PrimefacesContextUI.actualizar("formLovs:formDCargoDesempenado:formCargo:lovCargoCargoDesempeñado");
        PrimefacesContextUI.actualizar("formLovs:formDCargoDesempenado:formCargo:aceptarCCD");

        PrimefacesContextUI.ejecutar("PF('CargoCargoDesempeñadoDialogo').hide()");
    }

    public void cancelarParametroCargoCargoDesempeñado() {
        cargoSeleccionado = new Cargos();
        filtrarLovCargos = null;
        permitirIndexCargoDesempeñado = true;
        aceptar = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formLovs:formDCargoDesempenado:formCargo:lovCargoCargoDesempeñado:globalFilter");

        PrimefacesContextUI.ejecutar("PF('lovCargoCargoDesempeñado').clearFilters()");

        PrimefacesContextUI.ejecutar("PF('lovCargoCargoDesempeñado').unselectAllRows()");

        PrimefacesContextUI.ejecutar("PF('CargoCargoDesempeñadoDialogo').hide()");
    }

    public void actualizarParametroMotivoCambioCargoCargoDesempeñado() {
        RequestContext context = RequestContext.getCurrentInstance();
        nuevaVigenciaCargo.setMotivocambiocargo(motivoCambioCargoSeleccionado);
        motivoCambioCargoSeleccionado = new MotivosCambiosCargos();
        txt_motivoCargo = nuevaVigenciaCargo.getMotivocambiocargo().getNombre();
        filtrarLovMotivosCambiosCargos = null;
        aceptar = true;
        permitirIndexCargoDesempeñado = true;
        PrimefacesContextUI.actualizar("form:motivoModCargoDesempeñado");

        context.reset("formLovs:formDCargoDesempenado:formMotcargo:lovMotivoCambioCargoCargoDesempeñado:globalFilter");

        PrimefacesContextUI.ejecutar("PF('lovMotivoCambioCargoCargoDesempeñado').clearFilters()");

        PrimefacesContextUI.ejecutar("PF('lovMotivoCambioCargoCargoDesempeñado').unselectAllRows()");
        PrimefacesContextUI.actualizar("formLovs:formDCargoDesempenado:formMotcargo:MotivoCambioCargoCargoDesempeñadoDialogo");
        PrimefacesContextUI.actualizar("formLovs:formDCargoDesempenado:formMotcargo:lovMotivoCambioCargoCargoDesempeñado");
        PrimefacesContextUI.actualizar("formLovs:formDCargoDesempenado:formMotcargo:aceptarMCCCD");

        PrimefacesContextUI.ejecutar("PF('MotivoCambioCargoCargoDesempeñadoDialogo').hide()");
    }

    public void cancelarParametroMotivoCambioCargoCargoDesempeñado() {
        motivoCambioCargoSeleccionado = new MotivosCambiosCargos();
        filtrarLovMotivosCambiosCargos = null;
        permitirIndexCargoDesempeñado = true;
        aceptar = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formLovs:formDCargoDesempenado:formMotcargo:lovMotivoCambioCargoCargoDesempeñado:globalFilter");

        PrimefacesContextUI.ejecutar("PF('lovMotivoCambioCargoCargoDesempeñado').clearFilters()");

        PrimefacesContextUI.ejecutar("PF('lovMotivoCambioCargoCargoDesempeñado').unselectAllRows()");

        PrimefacesContextUI.ejecutar("PF('MotivoCambioCargoCargoDesempeñadoDialogo').hide()");
    }

    public void actualizarParametroEstructuraCargoDesempeñado() {
        RequestContext context = RequestContext.getCurrentInstance();
        nuevaVigenciaCargo.setEstructura(estructuraSeleccionada);
        estructuraSeleccionada = new Estructuras();
        filtrarLovEstructuras = null;
        aceptar = true;
        permitirIndexCargoDesempeñado = true;
        PrimefacesContextUI.actualizar("form:estructuraModCargoDesempeñado");
        context.reset("formLovs:formDCargoDesempenado:formEstruCargo:lovEstructuraCargoDesempeñado:globalFilter");

        PrimefacesContextUI.ejecutar("PF('lovEstructuraCargoDesempeñado').clearFilters()");

        PrimefacesContextUI.ejecutar("PF('lovEstructuraCargoDesempeñado').unselectAllRows()");
        PrimefacesContextUI.actualizar("formLovs:formDCargoDesempenado:formEstruCargo:EstructuraCargoDesempeñadoDialogo");
        PrimefacesContextUI.actualizar("formLovs:formDCargoDesempenado:formEstruCargo:lovEstructuraCargoDesempeñado");
        PrimefacesContextUI.actualizar("formLovs:formDCargoDesempenado:formEstruCargo:aceptarECD");

        PrimefacesContextUI.ejecutar("PF('EstructuraCargoDesempeñadoDialogo').hide()");
    }

    public void cancelarParametroEstructuraCargoDesempeñado() {
        estructuraSeleccionada = new Estructuras();
        filtrarLovEstructuras = null;
        permitirIndexCargoDesempeñado = true;
        aceptar = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formLovs:formDCargoDesempenado:formEstruCargo:lovEstructuraCargoDesempeñado:globalFilter");

        PrimefacesContextUI.ejecutar("PF('lovEstructuraCargoDesempeñado').clearFilters()");

        PrimefacesContextUI.ejecutar("PF('lovEstructuraCargoDesempeñado').unselectAllRows()");

        PrimefacesContextUI.ejecutar("PF('EstructuraCargoDesempeñadoDialogo').hide()");
    }

    public void actualizarParametroPapelCargoDesempeñado() {
        RequestContext context = RequestContext.getCurrentInstance();
        nuevaVigenciaCargo.setPapel(papelSeleccionado);
        papelSeleccionado = new Papeles();
        txt_papel = nuevaVigenciaCargo.getPapel().getDescripcion();
        filtrarLovPapeles = null;
        aceptar = true;
        permitirIndexCargoDesempeñado = true;
        PrimefacesContextUI.actualizar("form:papelModCargoDesempeñado");
        context.reset("formLovs:formDCargoDesempenado:formPapelD:lovPapelCargoDesempeñado:globalFilter");

        PrimefacesContextUI.ejecutar("PF('lovPapelCargoDesempeñado').clearFilters()");

        PrimefacesContextUI.ejecutar("PF('lovPapelCargoDesempeñado').unselectAllRows()");
        PrimefacesContextUI.actualizar("formLovs:formDCargoDesempenado:formPapelD:PapelCargoDesempeñadoDialogo");
        PrimefacesContextUI.actualizar("formLovs:formDCargoDesempenado:formPapelD:lovPapelCargoDesempeñado");
        PrimefacesContextUI.actualizar("formLovs:formDCargoDesempenado:formPapelD:aceptarPCD");

        PrimefacesContextUI.ejecutar("PF('PapelCargoDesempeñadoDialogo').hide()");
    }

    public void cancelarParametroPapelCargoDesempeñado() {
        papelSeleccionado = new Papeles();
        filtrarLovPapeles = null;
        permitirIndexCargoDesempeñado = true;
        aceptar = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formLovs:formDCargoDesempenado:formPapelD:lovPapelCargoDesempeñado:globalFilter");

        PrimefacesContextUI.ejecutar("PF('lovPapelCargoDesempeñado').clearFilters()");

        PrimefacesContextUI.ejecutar("PF('lovPapelCargoDesempeñado').unselectAllRows()");

        PrimefacesContextUI.ejecutar("PF('PapelCargoDesempeñadoDialogo').hide()");
    }

    public void actualizarParametroJefeCargoDesempeñado() {
        RequestContext context = RequestContext.getCurrentInstance();
        nuevaVigenciaCargo.setEmpleadojefe(empleadoSeleccionado);
        empleadoSeleccionado = new Empleados();
        txt_empleado = nuevaVigenciaCargo.getEmpleadojefe().getPersona().getNombreCompleto();
        filtrarLovEmpleados = null;
        aceptar = true;
        permitirIndexCargoDesempeñado = true;
        permitirDesplegarLista = true;
        PrimefacesContextUI.actualizar("form:empleadoJefeModCargoDesempeñado");
        context.reset("formLovs:formDCargoDesempenado:formjefe:lovEmpleadoJefeCargoDesempeñado:globalFilter");

        PrimefacesContextUI.ejecutar("PF('lovEmpleadoJefeCargoDesempeñado').clearFilters()");

        PrimefacesContextUI.ejecutar("PF('lovEmpleadoJefeCargoDesempeñado').unselectAllRows()");
        PrimefacesContextUI.actualizar("formLovs:formDCargoDesempenado:formjefe:EmpleadoJefeCargoDesempeñadoDialogo");
        PrimefacesContextUI.actualizar("formLovs:formDCargoDesempenado:formjefe:lovEmpleadoJefeCargoDesempeñado");
        PrimefacesContextUI.actualizar("formLovs:formDCargoDesempenado:formjefe:aceptarEJCD");

        PrimefacesContextUI.ejecutar("PF('EmpleadoJefeCargoDesempeñadoDialogo').hide()");
    }

    public void cancelarParametroJefeCargoDesempeñado() {
        empleadoSeleccionado = new Empleados();
        filtrarLovEmpleados = null;
        permitirIndexCargoDesempeñado = true;
        aceptar = true;
        permitirDesplegarLista = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formLovs:formDCargoDesempenado:formjefe:lovEmpleadoJefeCargoDesempeñado:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovEmpleadoJefeCargoDesempeñado').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('lovEmpleadoJefeCargoDesempeñado').unselectAllRows()");
        PrimefacesContextUI.ejecutar("PF('EmpleadoJefeCargoDesempeñadoDialogo').hide()");
    }

    public void actualizarParametroEmpresaInformacionPersonalVisible(Empresas empresaS) {
        System.out.println("Entro en actualizarParametroEmpresaInformacionPersonalVisible()");
        empresaSeleccionada = empresaS;
        System.out.println("empresaSeleccionada : " + empresaSeleccionada);
        nuevoEmpleado.setEmpresa(empresaSeleccionada);
        aceptar = true;
        System.out.println("nuevoEmpleado.getEmpresa().getNombre() :" + nuevoEmpleado.getEmpresa().getNombre());
        permitirIndexInformacionPersonal = true;
        lovCargos = administrarPersonaIndividual.lovCargosXEmpresa(nuevoEmpleado.getEmpresa().getSecuencia());
        desdeNominaFEmpresa = 1;
        calcularControlEmpleadosEmpresa();
        modificacionesEmpresaFechaIngresoInformacionPersonal();
        cargarLovCiudades();
        cargarLovTiposDocumentos();
    }

    public void cancelarParametroEmpresaInformacionPersonalVisible() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (empresaSeleccionada == null) {
            filtrarLovEmpresas = null;
            aceptar = true;
            context.reset("primerForm:formEmpresa:lovEmpresaInformacionPersonalVisible:globalFilter");
            PrimefacesContextUI.ejecutar("PF('lovEmpresaInformacionPersonalVisible').clearFilters()");
            PrimefacesContextUI.ejecutar("PF('lovEmpresaInformacionPersonalVisible').unselectAllRows()");
            context.reset("primerForm:formEmpresa:EmpresaInformacionPersonalDialogoVisible");
        } else {
            filtrarLovEmpresas = null;
            permitirIndexInformacionPersonal = true;
            aceptar = true;
            context.reset("primerForm:formEmpresa:lovEmpresaInformacionPersonalVisible:globalFilter");

            PrimefacesContextUI.ejecutar("PF('lovEmpresaInformacionPersonalVisible').clearFilters()");

            PrimefacesContextUI.ejecutar("PF('lovEmpresaInformacionPersonalVisible').unselectAllRows()");

            PrimefacesContextUI.ejecutar("PF('EmpresaInformacionPersonalDialogoVisible').hide()");
        }
    }

    public void actualizarParametroEmpresaInformacionPersonal() {
        RequestContext context = RequestContext.getCurrentInstance();
        nuevoEmpleado.setEmpresa(empresaSeleccionada);
        System.out.println("nuevoEmpleado.getEmpresa().getNombre() :" + nuevoEmpleado.getEmpresa().getNombre());
        //nuevoEmpleado.setEmpresa(empresaSeleccionada.getSecuencia());
        empresaSeleccionada = new Empresas();
        filtrarLovEmpresas = null;
        aceptar = true;
        permitirIndexInformacionPersonal = true;
        calcularControlEmpleadosEmpresa();
        modificacionesEmpresaFechaIngresoInformacionPersonal();
        PrimefacesContextUI.actualizar("form:empresaModPersonal");
        context.reset("formLovs:formDInformacionPersonal:formEmpresa:lovEmpresaInformacionPersonal:globalFilter");

        PrimefacesContextUI.ejecutar("PF('lovEmpresaInformacionPersonal').clearFilters()");
        PrimefacesContextUI.actualizar("formLovs:formDInformacionPersonal:formEmpresa:aceptarEIP");
        PrimefacesContextUI.ejecutar("PF('lovEmpresaInformacionPersonal').unselectAllRows()");
        PrimefacesContextUI.actualizar("formLovs:formDInformacionPersonal:formEmpresa:EmpresaInformacionPersonalDialogo");
        PrimefacesContextUI.actualizar("formLovs:formDInformacionPersonal:formEmpresa:lovEmpresaInformacionPersonal");

        PrimefacesContextUI.ejecutar("PF('EmpresaInformacionPersonalDialogo').hide()");
    }

    public void cancelarParametroEmpresaInformacionPersonal() {
        empresaSeleccionada = new Empresas();
        filtrarLovEmpresas = null;
        permitirIndexInformacionPersonal = true;
        aceptar = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formLovs:formDInformacionPersonal:formEmpresa:lovEmpresaInformacionPersonal:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovEmpresaInformacionPersonal').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('lovEmpresaInformacionPersonal').unselectAllRows()");
        PrimefacesContextUI.ejecutar("PF('EmpresaInformacionPersonalDialogo').hide()");
    }

    public void actualizarParametroTipoDocumentoInformacionPersonal() {
        RequestContext context = RequestContext.getCurrentInstance();
        nuevaPersona.setTipodocumento(tipoDocumentoSeleccionado);
        tipoDocumentoSeleccionado = new TiposDocumentos();
        txt_tipoDoc = nuevaPersona.getTipodocumento().getNombrelargo();
        filtrarLovTiposDocumentos = null;
        aceptar = true;
        permitirIndexInformacionPersonal = true;
        PrimefacesContextUI.actualizar("form:tipoDocumentoModPersonal");

        context.reset("formLovs:formDInformacionPersonal:infoP_tipoD:lovTipoDocumentoInformacionPersonal:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovTipoDocumentoInformacionPersonal').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('lovTipoDocumentoInformacionPersonal').unselectAllRows()");
        PrimefacesContextUI.actualizar("formLovs:formDInformacionPersonal:infoP_tipoD:TipoDocumentoInformacionPersonalDialogo");
        PrimefacesContextUI.actualizar("formLovs:formDInformacionPersonal:infoP_tipoD:lovTipoDocumentoInformacionPersonal");
        PrimefacesContextUI.ejecutar("PF('TipoDocumentoInformacionPersonalDialogo').hide()");
    }

    public void cancelarParametroTipoDocumentoInformacionPersonal() {
        tipoDocumentoSeleccionado = new TiposDocumentos();
        filtrarLovTiposDocumentos = null;
        permitirIndexInformacionPersonal = true;
        aceptar = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formLovs:formDInformacionPersonal:infoP_tipoD:lovTipoDocumentoInformacionPersonal:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovTipoDocumentoInformacionPersonal').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('lovTipoDocumentoInformacionPersonal').unselectAllRows()");
        PrimefacesContextUI.ejecutar("PF('TipoDocumentoInformacionPersonalDialogo').hide()");
    }

    public void actualizarParametroCiudadDocumentoInformacionPersonal() {
        RequestContext context = RequestContext.getCurrentInstance();
        nuevaPersona.setCiudaddocumento(ciudadSeleccionada);
        ciudadSeleccionada = new Ciudades();
        txt_ciudadDoc = nuevaPersona.getCiudaddocumento().getNombre();
        filtrarLovCiudades = null;
        aceptar = true;
        permitirIndexInformacionPersonal = true;
        PrimefacesContextUI.actualizar("form:ciudadDocumentoModPersonal");
        context.reset("formLovs:formDInformacionPersonal:infoP_ciudadD:lovCiudadDocumentoInformacionPersonal:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovCiudadDocumentoInformacionPersonal').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('lovCiudadDocumentoInformacionPersonal').unselectAllRows()");
        PrimefacesContextUI.actualizar("formLovs:formDInformacionPersonal:infoP_ciudadD:CiudadDocumentoInformacionPersonalDialogo");
        PrimefacesContextUI.actualizar("formLovs:formDInformacionPersonal:infoP_ciudadD:lovCiudadDocumentoInformacionPersonal");
        PrimefacesContextUI.ejecutar("PF('CiudadDocumentoInformacionPersonalDialogo').hide()");
        PrimefacesContextUI.actualizar("formLovs:formDInformacionPersonal:infoP_ciudadD:aceptarCDIP");
    }

    public void cancelarParametroCiudadDocumentoInformacionPersonal() {
        ciudadSeleccionada = new Ciudades();
        filtrarLovCiudades = null;
        permitirIndexInformacionPersonal = true;
        aceptar = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formLovs:formDInformacionPersonal:infoP_ciudadD:lovCiudadDocumentoInformacionPersonal:globalFilter");

        PrimefacesContextUI.ejecutar("PF('lovCiudadDocumentoInformacionPersonal').clearFilters()");

        PrimefacesContextUI.ejecutar("PF('lovCiudadDocumentoInformacionPersonal').unselectAllRows()");

        PrimefacesContextUI.ejecutar("PF('CiudadDocumentoInformacionPersonalDialogo').hide()");
    }

    public void actualizarParametroCiudadNacimientoInformacionPersonal() {
        RequestContext context = RequestContext.getCurrentInstance();
        nuevaPersona.setCiudadnacimiento(ciudadSeleccionada);
        ciudadSeleccionada = new Ciudades();
        txt_ciudadNac = nuevaPersona.getCiudadnacimiento().getNombre();
        filtrarLovCiudades = null;
        aceptar = true;
        permitirIndexInformacionPersonal = true;
        PrimefacesContextUI.actualizar("form:ciudadNacimientoModPersonal");

        context.reset("formLovs:formDInformacionPersonal:infoP_ciudadN:lovCiudadNacimientoInformacionPersonal:globalFilter");

        PrimefacesContextUI.ejecutar("PF('lovCiudadNacimientoInformacionPersonal').clearFilters()");

        PrimefacesContextUI.ejecutar("PF('lovCiudadNacimientoInformacionPersonal').unselectAllRows()");
        PrimefacesContextUI.actualizar("formLovs:formDInformacionPersonal:infoP_ciudadN:CiudadNacimientoInformacionPersonalDialogo");
        PrimefacesContextUI.actualizar("formLovs:formDInformacionPersonal:infoP_ciudadN:lovCiudadNacimientoInformacionPersonal");

        PrimefacesContextUI.ejecutar("PF('CiudadNacimientoInformacionPersonalDialogo').hide()");
        PrimefacesContextUI.actualizar("formLovs:formDInformacionPersonal:infoP_ciudadN:aceptarCNIP");
    }

    public void cancelarParametroCiudadNacimientoInformacionPersonal() {
        ciudadSeleccionada = new Ciudades();
        filtrarLovCiudades = null;
        permitirIndexInformacionPersonal = true;
        aceptar = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formLovs:formDInformacionPersonal:infoP_ciudadN:lovCiudadNacimientoInformacionPersonal:globalFilter");

        PrimefacesContextUI.ejecutar("PF('lovCiudadNacimientoInformacionPersonal').clearFilters()");

        PrimefacesContextUI.ejecutar("PF('lovCiudadNacimientoInformacionPersonal').unselectAllRows()");

        PrimefacesContextUI.ejecutar("PF('CiudadNacimientoInformacionPersonalDialogo').hide()");
    }

    public void calcularControlEmpleadosEmpresa() {
        BigInteger empleadosActuales = administrarPersonaIndividual.calcularNumeroEmpleadosEmpresa(nuevoEmpleado.getEmpresa().getSecuencia());
        BigInteger maximoEmpleados = administrarPersonaIndividual.obtenerMaximoEmpleadosEmpresa(nuevoEmpleado.getEmpresa().getSecuencia());

        if (empleadosActuales != null && maximoEmpleados != null) {
            if (empleadosActuales.intValue() >= maximoEmpleados.intValue()) {
                nuevoEmpleado.setEmpresa(new Empresas());
                //nuevoEmpleado.setEmpresa(BigInteger.ZERO);
                if (desdeNominaFEmpresa == 0) {
                    errorDesdeNominaF = true;
                    RequestContext context = RequestContext.getCurrentInstance();
                    PrimefacesContextUI.actualizar("form:empresaModPersonal");
                    PrimefacesContextUI.ejecutar("PF('errorTopeEmpleadosEmpresa').show()");
                }
            }
        }
    }

    public void modificacionesEmpresaFechaIngresoInformacionPersonal() {
        if (nuevoEmpleado.getEmpresa().getSecuencia() != null) {
            //if (nuevoEmpleado.getEmpresa() != null) {
            System.out.println("modificacionesEmpresaFechaIngresoInformacionPersonal() nuevoEmpleado.getEmpresa(): " + nuevoEmpleado.getEmpresa().getSecuencia());
            //System.out.println("paso el if()  nuevoEmpleado.getEmpresa(): " + nuevoEmpleado.getEmpresa());
            disableDescripcionEstructura = false;
            disableUbicacionGeografica = false;
            disableAfiliaciones = false;
            System.out.println("fechaIngreso : " + fechaIngreso);
            if (fechaIngreso != null) {
                desdeNominaFEmpresa = 0;
                disableNombreEstructuraCargo = false;
                lovEstructuras = null;
                cargarLovEstructuras();
                cargarLovCargos();
                cargarLovMotivosCargos();
                cargarLovPapeles();
                cargarLovEmpleados();
                cargarLovMotivosLocalizaciones();
                cargarLovTiposTrabajadores();
                System.out.println("disableNombreEstructuraCargo es false");
            } else {
                disableNombreEstructuraCargo = true;
                System.out.println("disableNombreEstructuraCargo es true 1");
            }
        } else {
            System.out.println("modificacionesEmpresaFechaIngresoInformacionPersonal() nuevoEmpleado.getEmpresa(): NULL");
            System.out.println("disableNombreEstructuraCargo es true 2");
            disableNombreEstructuraCargo = true;
            disableUbicacionGeografica = true;
            disableAfiliaciones = true;
        }
        System.out.println("desdeNominaFEmpresa : " + desdeNominaFEmpresa);
        if (desdeNominaFEmpresa == 0) {
            System.out.println("modificacionesEmpresaFechaIngresoInformacionPersonal() : desdeNominaFEmpresa == 0");
            RequestContext context = RequestContext.getCurrentInstance();
            PrimefacesContextUI.actualizar("form:epsModAfiliaciones");
            PrimefacesContextUI.actualizar("form:arpModAfiliaciones");
            PrimefacesContextUI.actualizar("form:fondoCensantiasModAfiliaciones");
            PrimefacesContextUI.actualizar("form:afpModAfiliaciones");
            PrimefacesContextUI.actualizar("form:cajaCompensacionModAfiliaciones");
            PrimefacesContextUI.actualizar("form:estructuraModCargoDesempeñado");
            PrimefacesContextUI.actualizar("form:estructuraModCentroCosto");
            PrimefacesContextUI.actualizar("form:ubicacionGeograficaModUbicacionGeografica");
            PrimefacesContextUI.actualizar("form:infoEstructuraCargoDesempeñado");
        }
    }

    public void validarDisableTipoTrabajador() {
        if (nuevaVigenciaTipoTrabajador.getTipotrabajador().getSecuencia() != null) {
            disableCamposDependientesTipoT = false;
        } else {
            disableCamposDependientesTipoT = true;
        }
        RequestContext context = RequestContext.getCurrentInstance();
        PrimefacesContextUI.actualizar("form:legislacionLaboralModLegislacionLaboral");
        PrimefacesContextUI.actualizar("form:normaLaboralModNormaLaboral");
        PrimefacesContextUI.actualizar("form:tipoContratoModTipoContrato");
        PrimefacesContextUI.actualizar("form:tipoSueldoModSueldo");
        PrimefacesContextUI.actualizar("form:tipoSalarioModTipoSalario");
    }

    public boolean isNumber(String numero) {
        try {
            double num = Double.parseDouble(numero);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public boolean isNumberTelefono(String numero) {
        try {
            long num = Long.parseLong(numero);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public void modificacionValorSueldo(String sueldo) {
        if (sueldo != null) {
            RequestContext context = RequestContext.getCurrentInstance();
            if (isNumber(sueldo) == true) {
                double numero = Double.parseDouble(sueldo);
                if (numero < 0) {
                    valorSueldo = auxSueldoValor;
                    PrimefacesContextUI.actualizar("form:valorSueldoModSueldo");
                    PrimefacesContextUI.ejecutar("PF('errorValorSueldo').show()");
                }
            } else {
                valorSueldo = auxSueldoValor;
                PrimefacesContextUI.actualizar("form:valorSueldoModSueldo");
                PrimefacesContextUI.ejecutar("PF('errorFormatoValorSueldo').show()");
            }
        }
    }

    public void consultarCodigoSS() {
        String codigo = administrarPersonaIndividual.buscarCodigoSSTercero(nuevaVigenciaAfiliacionEPS.getTercerosucursal().getTercero().getSecuencia());
        if (codigo == null) {
            RequestContext context = RequestContext.getCurrentInstance();
            PrimefacesContextUI.ejecutar("PF('advertenciaEPS').show()");
        }
    }

    public void consultarCodigoSP() {
        String codigo = administrarPersonaIndividual.buscarCodigoSPTercero(nuevaVigenciaAfiliacionAFP.getTercerosucursal().getTercero().getSecuencia());
        if (codigo == null) {
            RequestContext context = RequestContext.getCurrentInstance();
            PrimefacesContextUI.ejecutar("PF('advertenciaAFP').show()");
        }
    }

    public void consultarCodigoSC() {
        String codigo = administrarPersonaIndividual.buscarCodigoSCTercero(nuevaVigenciaAfiliacionARP.getTercerosucursal().getTercero().getSecuencia());
        if (codigo == null) {
            RequestContext context = RequestContext.getCurrentInstance();
            PrimefacesContextUI.ejecutar("PF('advertenciaARP').show()");
        }
    }

    public void modificarNumeroTelefonico(String numero) {
        boolean number = isNumberTelefono(numero);
        RequestContext context = RequestContext.getCurrentInstance();
        if (number == true) {
            long telefono = Long.parseLong(numero);
            if (telefono < 0) {
                nuevoTelefono.setNumerotelefono(0);
                PrimefacesContextUI.actualizar("form:numeroTelefonoModTelefono");
                PrimefacesContextUI.ejecutar("PF('errorNumeroTelefonico').show()");
            }
        } else {
            nuevoTelefono.setNumerotelefono(0);
            PrimefacesContextUI.actualizar("form:numeroTelefonoModTelefono");
            PrimefacesContextUI.ejecutar("PF('errorNumeroTelefonico').show()");
        }
    }

    public void validarTipoTrabajadorReformaLaboral() {
        if (nuevaVigenciaTipoTrabajador.getTipotrabajador().getSecuencia() != null) {
            String validar = administrarPersonaIndividual.validarTipoTrabajadorReformaLaboral(nuevaVigenciaTipoTrabajador.getTipotrabajador().getSecuencia(), nuevaVigenciaReformaLaboral.getReformalaboral().getSecuencia());
            if (validar != null) {
                if (validar.equalsIgnoreCase("N")) {
                    errorTT = "RL";
                    RequestContext context = RequestContext.getCurrentInstance();
                    PrimefacesContextUI.ejecutar("PF('aletarTiposTrabajadoresRL').show()");
                } else {
                    errorTT = " ";
                }
            } else {
                System.out.println("Error en la consulta a la BD");
            }
        }
    }

    public void validarTipoTrabajadorTipoSueldo() {
        if (nuevaVigenciaTipoTrabajador.getTipotrabajador().getSecuencia() != null) {
            String validar = administrarPersonaIndividual.validarTipoTrabajadorTipoSueldo(nuevaVigenciaTipoTrabajador.getTipotrabajador().getSecuencia(), nuevaVigenciaSueldo.getTiposueldo().getSecuencia());
            System.out.println("validarTipoTrabajadorTipoSueldo() validar : " + validar + "  para :");
            System.out.println("nuevaVigenciaTipoTrabajador.getTipotrabajador().getSecuencia() : " + nuevaVigenciaTipoTrabajador.getTipotrabajador().getSecuencia() + "  y nuevaVigenciaSueldo.getTiposueldo().getSecuencia() : " + nuevaVigenciaSueldo.getTiposueldo().getSecuencia());
            if (validar != null) {
                if (validar.equalsIgnoreCase("N")) {
                    errorTT = "TS";
                    RequestContext context = RequestContext.getCurrentInstance();
                    PrimefacesContextUI.ejecutar("PF('aletarTiposTrabajadoresTS').show()");
                } else {
                    errorTT = " ";
                }
            } else {
                System.out.println("Error en la consulta a la BD");
            }
        }
    }

    public void validarTipoTrabajadorTipoContrato() {
        if (nuevaVigenciaTipoTrabajador.getTipotrabajador().getSecuencia() != null) {
            String validar = administrarPersonaIndividual.validarTipoTrabajadorTipoContrato(nuevaVigenciaTipoTrabajador.getTipotrabajador().getSecuencia(), nuevaVigenciaTipoContrato.getTipocontrato().getSecuencia());
            if (validar != null) {
                if (validar.equalsIgnoreCase("N")) {
                    errorTT = "TC";
                    RequestContext context = RequestContext.getCurrentInstance();
                    PrimefacesContextUI.ejecutar("PF('aletarTiposTrabajadoresTC').show()");
                } else {
                    errorTT = " ";
                }
            } else {
                System.out.println("Error en la consulta a la BD");
            }
        }
    }

    public void validarTipoTrabajadorNormaLaboral() {
        if (nuevaVigenciaTipoTrabajador.getTipotrabajador().getSecuencia() != null) {
            String validar = administrarPersonaIndividual.validarTipoTrabajadorNormaLaboral(nuevaVigenciaTipoTrabajador.getTipotrabajador().getSecuencia(), nuevaVigenciaNormaEmpleado.getNormalaboral().getSecuencia());
            if (validar != null) {
                if (validar.equalsIgnoreCase("N")) {
                    errorTT = "NL";
                    RequestContext context = RequestContext.getCurrentInstance();
                    PrimefacesContextUI.ejecutar("PF('aletarTiposTrabajadoresNL').show()");
                } else {
                    errorTT = " ";
                }
            } else {
                System.out.println("Error en la consulta a la BD");
            }
        }
    }

    public void validarTipoTrabajadorContrato() {
        if (nuevaVigenciaTipoTrabajador.getTipotrabajador().getSecuencia() != null) {
            String validar = administrarPersonaIndividual.validarTipoTrabajadorContrato(nuevaVigenciaTipoTrabajador.getTipotrabajador().getSecuencia(), nuevaVigenciaContrato.getContrato().getSecuencia());
            if (validar != null) {
                if (validar.equalsIgnoreCase("N")) {
                    errorTT = "C";
                    RequestContext context = RequestContext.getCurrentInstance();
                    PrimefacesContextUI.ejecutar("PF('aletarTiposTrabajadoresC').show()");
                } else {
                    errorTT = " ";
                }
            } else {
                System.out.println("Error en la consulta a la BD");
            }
        }
    }

    public void validarEmailPersona(String email) {
        boolean esEmail = isEmail(email);
        if (esEmail == false) {
            nuevaPersona.setEmail(null);
            RequestContext context = RequestContext.getCurrentInstance();
            PrimefacesContextUI.actualizar("form:correoModPersonal");
            PrimefacesContextUI.ejecutar("PF('errorEmailPersona').show()");
        }
    }

    public boolean isEmail(String email) {
        // Compiles the given regular expression into a pattern.
        Pattern pattern = Pattern.compile(PATTERN_EMAIL);
        // Match the given input against this pattern
        Matcher matcher = pattern.matcher(email);
        boolean valor = matcher.matches();
        return valor;
    }

    public void activarAceptar() {
        //if (empresaSeleccionada != null) {
        aceptar = false;
        //}
    }

    public Date mostrarFechaIngreso() {
        return fechaIngreso;
    }

    private void modificarInfoR_EmpresaInfoP(int valor) {
        infoRegistroEmpresaInfoPersonal = String.valueOf(valor);
    }

    public void eventoFiltrar_EmpresaInfoP() {
        if (filtrarLovEmpresas.size() == 1) {
            empresaSeleccionada = filtrarLovEmpresas.get(0);
            aceptar = false;
        } else {
            empresaSeleccionada = null;
            aceptar = true;
        }

        modificarInfoR_EmpresaInfoP(filtrarLovEmpresas.size());
        PrimefacesContextUI.actualizar("formLovs:formDInformacionPersonal:formEmpresa:infoRegistroEmpresaInformacionPersonal");
        PrimefacesContextUI.actualizar("formLovs:formDInformacionPersonal:formEmpresa:infoRegistroEmpresaInformacionPersonalV");
    }

    private void modificarInfoR_TipoDocInfoP(int valor) {
        infoRegistroTipoDocInfoPersonal = String.valueOf(valor);
    }

    public void eventoFiltrar_TipoDocInfoP() {
        if (filtrarLovTiposDocumentos.size() == 1) {
            tipoDocumentoSeleccionado = filtrarLovTiposDocumentos.get(0);
            aceptar = false;
        } else {
            tipoDocumentoSeleccionado = null;
            aceptar = true;
        }
        PrimefacesContextUI.actualizar("formLovs:formDInformacionPersonal:infoP_tipoD:infoRegistroTipoDocumentoInformacionPersonal");
    }

    private void modificarInfoR_CiudadDocInfoP(int valor) {
        infoRegistroCiudadDocInfoPersonal = String.valueOf(valor);
    }

    public void eventoFiltrar_CiudadDocInfoP() {
        if (filtrarLovCiudades.size() == 1) {
            ciudadSeleccionada = filtrarLovCiudades.get(0);
            aceptar = false;
        } else {
            ciudadSeleccionada = null;
            aceptar = true;
        }

        modificarInfoR_CiudadDocInfoP(filtrarLovCiudades.size());
        PrimefacesContextUI.actualizar("formLovs:formDInformacionPersonal:infoP_ciudadD:infoRegistroCiudadDocumentoInformacionPersonal");
    }

    private void modificarInfoR_CiudadNacInfoP(int valor) {
        infoRegistroCiudadNacInfoPersonal = String.valueOf(valor);
    }

    public void eventoFiltrar_CiudadNacInfoP() {
        if (filtrarLovCiudades.size() == 1) {
            ciudadSeleccionada = filtrarLovCiudades.get(0);
            aceptar = false;
        } else {
            ciudadSeleccionada = null;
            aceptar = true;
        }
        modificarInfoR_CiudadNacInfoP(filtrarLovCiudades.size());
        PrimefacesContextUI.actualizar("formLovs:formDInformacionPersonal:infoP_ciudadN:infoRegistroCiudadNacimientoInformacionPersonal");
    }

    //Funciones para contar registros:
    private void modificarInfoR_CargoD(int valor) {
        infoRegistroCargoDesempenado = String.valueOf(valor);
    }

    public void eventoFiltrar_CargoD() {
        if (filtrarLovCargos.size() == 1) {
            cargoSeleccionado = filtrarLovCargos.get(0);
            aceptar = false;
        } else {
            cargoSeleccionado = null;
            aceptar = true;
        }
        modificarInfoR_CargoD(filtrarLovCargos.size());
        PrimefacesContextUI.actualizar("formLovs:formDCargoDesempenado:formCargo:infoRegistroCargoCargoDesempenado");
    }

    private void modificarInfoR_JefeCargoD(int valor) {
        infoRegistroJefeCargoDesempenado = String.valueOf(valor);
    }

    public void eventoFiltrar_JefeCargoD() {
        if (filtrarLovEmpleados.size() == 1) {
            empleadoSeleccionado = filtrarLovEmpleados.get(0);
            aceptar = false;
        } else {
            empleadoSeleccionado = null;
            aceptar = true;
        }
        modificarInfoR_JefeCargoD(filtrarLovEmpleados.size());
        PrimefacesContextUI.actualizar("formLovs:formDCargoDesempenado:formjefe:infoRegistroJefeCargoDesempenado");
    }

    private void modificarInfoR_PapelCargoD(int valor) {
        infoRegistroPapelCargoDesempenado = String.valueOf(valor);
    }

    public void eventoFiltrar_PapelCargoD() {
        if (filtrarLovPapeles.size() == 1) {
            papelSeleccionado = filtrarLovPapeles.get(0);
            aceptar = false;
        } else {
            papelSeleccionado = null;
            aceptar = true;
        }
        modificarInfoR_PapelCargoD(filtrarLovPapeles.size());
        PrimefacesContextUI.actualizar("formLovs:formDCargoDesempenado:formPapelD:infoRegistroPapelCargoDesempenado");
    }

    private void modificarInfoR_MotivoCargoD(int valor) {
        infoRegistroMotivoCargoDesempenado = String.valueOf(valor);
    }

    public void eventoFiltrar_MotivoCargoD() {
        if (filtrarLovMotivosCambiosCargos.size() == 1) {
            motivoCambioCargoSeleccionado = filtrarLovMotivosCambiosCargos.get(0);
            aceptar = false;
        } else {
            motivoCambioCargoSeleccionado = null;
            aceptar = true;
        }
        modificarInfoR_MotivoCargoD(filtrarLovMotivosCambiosCargos.size());
        PrimefacesContextUI.actualizar("formLovs:formDCargoDesempenado:formMotcargo:infoRegistroMotivoCargoCargoDesempenado");
    }

    private void modificarInfoR_EstrCargoD(int valor) {
        infoRegistroEstructuraCargoDesempenado = String.valueOf(valor);
    }

    public void eventoFiltrar_EstrCargoD() {
        if (filtrarLovEstructuras.size() == 1) {
            estructuraSeleccionada = filtrarLovEstructuras.get(0);
            aceptar = false;
        } else {
            estructuraSeleccionada = null;
            aceptar = true;
        }
        modificarInfoR_EstrCargoD(filtrarLovEstructuras.size());
        PrimefacesContextUI.actualizar("formLovs:formDCargoDesempenado:formEstruCargo:infoRegistroEstructuraCargoDesempenado");
    }

    private void modificarInfoR_EstrCentroC(int valor) {
        infoRegistroEstructuraCentroCosto = String.valueOf(valor);
    }

    public void eventoFiltrar_EstrCentroC() {
        if (filtrarLovEstructurasCentroCosto.size() == 1) {
            estructuraCentroCostoSeleccionada = filtrarLovEstructurasCentroCosto.get(0);
            aceptar = false;
        } else {
            estructuraCentroCostoSeleccionada = null;
            aceptar = true;
        }
        modificarInfoR_EstrCentroC(filtrarLovEstructurasCentroCosto.size());
        PrimefacesContextUI.actualizar("formLovs:formDCentroCosto:formEstrucCC:infoRegistroEstructuraCentroCosto");
    }

    private void modificarInfoR_MotivoCentroC(int valor) {
        infoRegistroMotivoCentroCosto = String.valueOf(valor);
    }

    public void eventoFiltrar_MotivoCentroC() {
        if (filtrarLovMotivosCC.size() == 1) {
            motivoLocalizacionSeleccionado = filtrarLovMotivosCC.get(0);
            aceptar = false;
        } else {
            motivoLocalizacionSeleccionado = null;
            aceptar = true;
        }
        modificarInfoR_MotivoCentroC(filtrarLovMotivosCC.size());
        PrimefacesContextUI.actualizar("formLovs:formDCentroCosto:formCentroC:infoRegistroMotivoCentroCosto");
    }

    private void modificarInfoR_MetodoFormaP(int valor) {
        infoRegistroMetodoFormaPago = String.valueOf(valor);
    }

    public void eventoFiltrar_MetodoFormaP() {
        if (filtrarLovMetodosPagos.size() == 1) {
            metodoPagoSeleccionado = filtrarLovMetodosPagos.get(0);
            aceptar = false;
        } else {
            metodoPagoSeleccionado = null;
            aceptar = true;
        }
        modificarInfoR_MetodoFormaP(filtrarLovMetodosPagos.size());
        PrimefacesContextUI.actualizar("formLovs:formMetodo:infoRegistroMetodoFormaPago");
    }

    private void modificarInfoR_SucursalFormaP(int valor) {
        infoRegistroSucursalFormaPago = String.valueOf(valor);
    }

    public void eventoFiltrar_SucursalFormaP() {
        if (filtrarLovSucursales.size() == 1) {
            sucursalSeleccionada = filtrarLovSucursales.get(0);
            aceptar = false;
        } else {
            sucursalSeleccionada = null;
            aceptar = true;
        }
        modificarInfoR_SucursalFormaP(filtrarLovSucursales.size());
        PrimefacesContextUI.actualizar("formLovs:formDFormaPago:infoRegistroSucursalFormaPago");
    }

    private void modificarInfoR_PeriodFormaPago(int valor) {
        infoRegistroFormaPago = String.valueOf(valor);
    }

    public void eventoFiltrar_PeriodFormaPago() {
        if (filtrarLovPeriodicidades.size() == 1) {
            periodicidadSeleccionada = filtrarLovPeriodicidades.get(0);
            aceptar = false;
        } else {
            periodicidadSeleccionada = null;
            aceptar = true;
        }
        modificarInfoR_PeriodFormaPago(filtrarLovPeriodicidades.size());
        PrimefacesContextUI.actualizar("formLovs:formDFormaPago:formPeriodicidad:infoRegistroFormaFormaPago");
    }

    private void modificarInfoR_MotivoSu(int valor) {
        infoRegistroMotivoSueldo = String.valueOf(valor);
    }

    public void eventoFiltrar_MotivoSu() {
        if (filtrarLovMotivosCambiosSueldos.size() == 1) {
            motivoCambioSueldoSeleccionado = filtrarLovMotivosCambiosSueldos.get(0);
            aceptar = false;
        } else {
            motivoCambioSueldoSeleccionado = null;
            aceptar = true;
        }
        modificarInfoR_MotivoSu(filtrarLovMotivosCambiosSueldos.size());
        PrimefacesContextUI.actualizar("formLovs:formDSueldo:formMotivoSu:infoRegistroMotivoSueldo");
    }

    private void modificarInfoR_TipoSuSu(int valor) {
        infoRegistroTipoSueldoS = String.valueOf(valor);
    }

    public void eventoFiltrar_TipoSuSu() {
        if (filtrarLovTiposSueldos.size() == 1) {
            tipoSueldoSeleccionado = filtrarLovTiposSueldos.get(0);
            aceptar = false;
        } else {
            tipoSueldoSeleccionado = null;
            aceptar = true;
        }
        modificarInfoR_TipoSuSu(filtrarLovTiposSueldos.size());
        PrimefacesContextUI.actualizar("formLovs:formDSueldo:formTipoSueldo:infoRegistroTipoSueldoSueldo");
    }

    private void modificarInfoR_UnidadSu(int valor) {
        infoRegistroUnidadSueldo = String.valueOf(valor);
    }

    public void eventoFiltrar_UnidadSu() {
        if (filtrarLovUnidades.size() == 1) {
            unidadSeleccionada = filtrarLovUnidades.get(0);
            aceptar = false;
        } else {
            unidadSeleccionada = null;
            aceptar = true;
        }
        modificarInfoR_UnidadSu(filtrarLovUnidades.size());
        PrimefacesContextUI.actualizar("formLovs:formDSueldo:formUnidadS:infoRegistroUnidadSueldo");
    }

    private void modificarInfoR_MotivoTipoCo(int valor) {
        infoRegistroMotivoTipoContrato = String.valueOf(valor);
    }

    public void eventoFiltrar_MotivoTipoCo() {
        if (filtrarLovMotivosContratos.size() == 1) {
            motivoContratoSeleccionado = filtrarLovMotivosContratos.get(0);
            aceptar = false;
        } else {
            motivoContratoSeleccionado = null;
            aceptar = true;
        }
        modificarInfoR_MotivoTipoCo(filtrarLovMotivosContratos.size());
        PrimefacesContextUI.actualizar("formLovs:formDTipoContrato:formTipoContrat:infoRegistroMotivoTipoContrato");
    }

    private void modificarInfoR_TipoContrato(int valor) {
        infoRegistroTipoContrato = String.valueOf(valor);
    }

    public void eventoFiltrar_TipoContrato() {
        if (filtrarLovTiposContratos.size() == 1) {
            tipoContratoSeleccionado = filtrarLovTiposContratos.get(0);
            aceptar = false;
        } else {
            tipoContratoSeleccionado = null;
            aceptar = true;
        }
        modificarInfoR_TipoContrato(filtrarLovTiposContratos.size());
        PrimefacesContextUI.actualizar("formLovs:formDTipoContrato:formTipoContrato:infoRegistroTipoTipoContrato");
    }

    private void modificarInfoR_TipoTelT(int valor) {
        infoRegistroTipoTelTelefono = String.valueOf(valor);
    }

    public void eventoFiltrar_TipoTelT() {
        if (filtrarLovTiposTelefonos.size() == 1) {
            tipoTelefonoSeleccionado = filtrarLovTiposTelefonos.get(0);
            aceptar = false;
        } else {
            tipoTelefonoSeleccionado = null;
            aceptar = true;
        }
        modificarInfoR_TipoTelT(filtrarLovTiposTelefonos.size());
        PrimefacesContextUI.actualizar("formLovs:formDTelefono:formTel:infoRegistroTipoTelefonoTelefono");
    }

    private void modificarInfoR_CiudadTel(int valor) {
        infoRegistroCiudadTelefono = String.valueOf(valor);
    }

    public void eventoFiltrar_CiudadTel() {
        if (filtrarLovCiudades.size() == 1) {
            ciudadSeleccionada = filtrarLovCiudades.get(0);
            aceptar = false;
        } else {
            ciudadSeleccionada = null;
            aceptar = true;
        }
        modificarInfoR_CiudadTel(filtrarLovCiudades.size());
        PrimefacesContextUI.actualizar("formLovs:formDTelefono:infoRegistroCiudadTelefono");
    }

    private void modificarInfoR_NormaL(int valor) {
        infoRegistroNormaLaboral = String.valueOf(valor);
    }

    public void eventoFiltrar_NormaL() {
        if (filtrarLovNormasLaborales.size() == 1) {
            normaLaboralSeleccionada = filtrarLovNormasLaborales.get(0);
            aceptar = false;
        } else {
            normaLaboralSeleccionada = null;
            aceptar = true;
        }
        modificarInfoR_NormaL(filtrarLovNormasLaborales.size());
        PrimefacesContextUI.actualizar("formLovs:formDNormaLaboral:infoRegistroNormaNormaLaboral");
    }

    private void modificarInfoR_ReformaTipoSa(int valor) {
        infoRegistroReformaTipoSalario = String.valueOf(valor);
    }

    public void eventoFiltrar_ReformaTipoSa() {
        if (filtrarLovReformasLaborales.size() == 1) {
            reformaLaboralSeleccionada = filtrarLovReformasLaborales.get(0);
            aceptar = false;
        } else {
            reformaLaboralSeleccionada = null;
            aceptar = true;
        }
        modificarInfoR_ReformaTipoSa(filtrarLovReformasLaborales.size());
        PrimefacesContextUI.actualizar("formLovs:formDTipoSalario:infoRegistroReformaTipoSalario");
    }

    private void modificarInfoR_TerceroAfSuc(int valor) {
        infoRegistroTerceroAfiliacion = String.valueOf(valor);
    }

    public void eventoFiltrar_TerceroAfSuc() {
        if (filtrarLovTercerosSucursales.size() == 1) {
            terceroSucursalSeleccionado = filtrarLovTercerosSucursales.get(0);
            aceptar = false;
        } else {
            terceroSucursalSeleccionado = null;
            aceptar = true;
        }
        modificarInfoR_TerceroAfSuc(filtrarLovTercerosSucursales.size());
        PrimefacesContextUI.actualizar("formLovs:formDAfiliacion:infoRegistroTerceroAfiliacion");
    }

    private void modificarInfoR_CiudadDir(int valor) {
        infoRegistroCiudadDireccion = String.valueOf(valor);
    }

    public void eventoFiltrar_CiudadDir() {
        if (filtrarLovCiudades.size() == 1) {
            ciudadSeleccionada = filtrarLovCiudades.get(0);
            aceptar = false;
        } else {
            ciudadSeleccionada = null;
            aceptar = true;
        }
        modificarInfoR_CiudadDir(filtrarLovCiudades.size());
        PrimefacesContextUI.actualizar("formLovs:formDDireccion:infoRegistroCiudadDireccion");
    }

    private void modificarInfoR_TipoTraTT(int valor) {
        infoRegistroTipoTrabajadorTT = String.valueOf(valor);
    }

    public void eventoFiltrar_TipoTraTT() {
        if (filtrarLovTiposTrabajadores.size() == 1) {
            tipoTrabajadorSeleccionado = filtrarLovTiposTrabajadores.get(0);
            aceptar = false;
        } else {
            tipoTrabajadorSeleccionado = null;
            aceptar = true;
        }
        modificarInfoR_TipoTraTT(filtrarLovTiposTrabajadores.size());
        PrimefacesContextUI.actualizar("formLovs:formDTipoTrabajador:formDTipoTrabajador:infoRegistroTipoTrabajadorTipoTrabajador");
    }

    private void modificarInfoR_UbicacionUb(int valor) {
        infoRegistroUbicacionUb = String.valueOf(valor);
    }

    public void eventoFiltrar_UbicacionUb() {
        if (filtrarLovUbicacionesGeograficas.size() == 1) {
            ubicacionGeograficaSeleccionada = filtrarLovUbicacionesGeograficas.get(0);
            aceptar = false;
        } else {
            ubicacionGeograficaSeleccionada = null;
            aceptar = true;
        }
        modificarInfoR_UbicacionUb(filtrarLovUbicacionesGeograficas.size());
        PrimefacesContextUI.actualizar("formLovs:formDUbicacion:infoRegistroUbicacionUbicacion");
    }

    private void modificarInfoR_ContratoLL(int valor) {
        infoRegistroContratoLegislacionL = String.valueOf(valor);
    }

    public void eventoFiltrar_ContratoLL() {
        if (filtrarLovContratos.size() == 1) {
            contratoSeleccionado = filtrarLovContratos.get(0);
            aceptar = false;
        } else {
            contratoSeleccionado = null;
            aceptar = true;
        }
        modificarInfoR_ContratoLL(filtrarLovContratos.size());
        PrimefacesContextUI.actualizar("formLovs:formDLegislacionLaboral:infoRegistroContratoLegislacionLaboral");
    }

    private void modificarInfoR_EstadoCivil(int valor) {
        infoRegistroEstadoCivil = String.valueOf(valor);
    }

    public void eventoFiltrar_EstadoCivil() {
        if (filtrarLovEstadosCiviles.size() == 1) {
            estadoCivilSeleccionado = filtrarLovEstadosCiviles.get(0);
            aceptar = false;
        } else {
            estadoCivilSeleccionado = null;
            aceptar = true;
        }
        modificarInfoR_EstadoCivil(filtrarLovEstadosCiviles.size());
        PrimefacesContextUI.actualizar("formLovs:formDEstadoCivil:infoRegistroEstadoCivilEstadoCivil");
    }

    private void modificarInfoR_JornadaL(int valor) {
        infoRegistroJornadaLaboral = String.valueOf(valor);
    }

    public void eventoFiltrar_JornadaL() {
        if (filtrarLovJornadasLaborales.size() == 1) {
            jornadaLaboralSeleccionada = filtrarLovJornadasLaborales.get(0);
            aceptar = false;
        } else {
            jornadaLaboralSeleccionada = null;
            aceptar = true;
        }
        modificarInfoR_JornadaL(filtrarLovJornadasLaborales.size());
        PrimefacesContextUI.actualizar("formLovs:formDJornadaLaboral:infoRegistroJornadaJornadaLaboral");
    }

//    public void titulosTercero(int tercero) {
//        if (tercero == 0) {
//            tituloTercero= "eps";
//        } else if (tercero == 1) {
//            tituloTercero= "arp";
//        } else if (tercero == 2) {
//            tituloTercero= "cesantias";
//        } else if (tercero == 3) {
//            tituloTercero= "afp";
//        } else if (tercero == 4) {
//            tituloTercero= "cajacompen";
//        } else {
//            tituloTercero= "seleccionetecero";
//        }
//
//    }
    ///////////////////////CARGAR LISTAS////////////////////////
    public void cargarLovEmpresas() {
        if (lovEmpresas == null) {
            lovEmpresas = administrarPersonaIndividual.lovEmpresas();
            if (lovEmpresas != null){
                if(lovEmpresas.size() == 1){
                    nuevoEmpleado.setEmpresa(lovEmpresas.get(0));
                }
            } else {
                System.out.println("lovEmpresas == null");
            }
        }
    }

    public void cargarLovTiposDocumentos() {
        if (lovTiposDocumentos == null) {
            lovTiposDocumentos = administrarPersonaIndividual.lovTiposDocumentos();
        }
    }

    public void cargarLovCiudades() {
        if (lovCiudades == null && nuevoEmpleado.getEmpresa().getSecuencia() != null) {
            //if (lovCiudades == null && nuevoEmpleado.getEmpresa() != null) {
            lovCiudades = administrarPersonaIndividual.lovCiudades();
        }
    }

//    public List<Cargos> getLovCargos() {
//        if (lovCargos == null) {
//            //lovCargos = administrarPersonaIndividual.lovCargos();
//            lovCargos = new ArrayList<Cargos>();
//        }
//        return lovCargos;
//    }
    public void cargarLovCargos() {
        if (lovCargos == null) {
            lovCargos = administrarPersonaIndividual.lovCargos();
        }
    }

    public void cargarLovMotivosCargos() {
        if (lovMotivosCargos == null) {
            lovMotivosCargos = administrarPersonaIndividual.lovMotivosCambiosCargos();
        }
    }

    public void cargarLovEstructuras() {
        System.out.println("cargarLovEstructuras() : nuevoEmpleado.getEmpresa().getSecuencia() : " + nuevoEmpleado.getEmpresa().getSecuencia());
        System.out.println("&& fechaIngreso : " + fechaIngreso + ",  lovEstructuras : " + lovEstructuras);
        if (nuevoEmpleado.getEmpresa().getSecuencia() != null && (fechaIngreso != null) && lovEstructuras == null) {
            lovEstructuras = administrarPersonaIndividual.lovEstructurasModCargos(nuevoEmpleado.getEmpresa().getSecuencia(), fechaIngreso);
        }
    }

    public void cargarLovPapeles() {
        if (lovPapeles == null) {
            lovPapeles = administrarPersonaIndividual.lovPapeles();
        }
    }

    public void cargarLovEmpleados() {
        if (lovEmpleados == null) {
            lovEmpleados = administrarPersonaIndividual.lovEmpleados();
        }
    }

    public void cargarLovMotivosLocalizaciones() {
        if (lovMotivosLocalizaciones == null) {
            lovMotivosLocalizaciones = administrarPersonaIndividual.lovMotivosLocalizaciones();
        }
    }

    public void cargarLovEstructurasCentroCosto() {
        if (nuevoEmpleado.getEmpresa().getSecuencia() != null && lovEstructurasCentroCosto == null) {
            lovEstructurasCentroCosto = administrarPersonaIndividual.lovEstructurasModCentroCosto(nuevoEmpleado.getEmpresa().getSecuencia());
        }
        /*
         * if (nuevoEmpleado.getEmpresa() != null && lovEstructurasCentroCosto
         * == null) { lovEstructurasCentroCosto =
         * administrarPersonaIndividual.lovEstructurasModCentroCosto(nuevoEmpleado.getEmpresa());
         * }
         */
    }

    public void cargarLovTiposTrabajadores() {
        if (lovTiposTrabajadores == null && nuevoEmpleado.getEmpresa().getSecuencia() != null) {
            //if (lovTiposTrabajadores == null && nuevoEmpleado.getEmpresa() != null) {
            lovTiposTrabajadores = administrarPersonaIndividual.lovTiposTrabajadores();
        }
    }

    public void cargarLovsConTipoTrabajador(BigInteger secTipoTr) {
        RequestContext context = RequestContext.getCurrentInstance();
        System.out.println("Secuencia Tipo Trabajador = " + secTipoTr);
        lovReformasLaborales = null;
        lovTiposSueldos = null;
        lovTiposContratos = null;
        lovNormasLaborales = null;
        lovContratos = null;

        lovReformasLaborales = administrarPersonaIndividual.lovReformasLaboralesValidos(secTipoTr);
        if (lovReformasLaborales != null) {
            modificarInfoR_ReformaTipoSa(lovReformasLaborales.size());
            System.out.println("tamaño lovReformasLaborales : " + lovReformasLaborales.size());
            nuevaVigenciaReformaLaboral.setReformalaboral(new ReformasLaborales());
            if (lovReformasLaborales.size() == 1) {
                nuevaVigenciaReformaLaboral.setReformalaboral(lovReformasLaborales.get(0));
            }
        }
        lovTiposSueldos = administrarPersonaIndividual.lovTiposSueldosValidos(secTipoTr);
        if (lovTiposSueldos != null) {
            modificarInfoR_TipoSuSu(lovTiposSueldos.size());
            System.out.println("tamaño lovTiposSueldos : " + lovTiposSueldos.size());
            nuevaVigenciaSueldo.setTiposueldo(new TiposSueldos());
            txt_TipoSu = "";
            if (lovTiposSueldos.size() == 1) {
                nuevaVigenciaSueldo.setTiposueldo(lovTiposSueldos.get(0));
                txt_TipoSu = nuevaVigenciaSueldo.getTiposueldo().getDescripcion();
            }
        }
        lovTiposContratos = administrarPersonaIndividual.lovTiposContratosValidos(secTipoTr);
        if (lovTiposContratos != null) {
            modificarInfoR_TipoContrato(lovTiposContratos.size());
            System.out.println("tamaño lovTiposContratos : " + lovTiposContratos.size());
            nuevaVigenciaTipoContrato.setTipocontrato(new TiposContratos());
            if (lovTiposContratos.size() == 1) {
                nuevaVigenciaTipoContrato.setTipocontrato(lovTiposContratos.get(0));
            }
        }
        lovNormasLaborales = administrarPersonaIndividual.lovNormasLaboralesValidos(secTipoTr);
        if (lovNormasLaborales != null) {
            modificarInfoR_NormaL(lovNormasLaborales.size());
            System.out.println("tamaño lovNormasLaborales : " + lovNormasLaborales.size());
            nuevaVigenciaNormaEmpleado.setNormalaboral(new NormasLaborales());
            if (lovNormasLaborales.size() == 1) {
                nuevaVigenciaNormaEmpleado.setNormalaboral(lovNormasLaborales.get(0));
            }
        }
        lovContratos = administrarPersonaIndividual.lovContratosValidos(secTipoTr);
        if (lovContratos != null) {
            modificarInfoR_ContratoLL(lovContratos.size());
            System.out.println("tamaño lovContratos : " + lovContratos.size());
            nuevaVigenciaContrato.setContrato(new Contratos());
            if (lovContratos.size() == 1) {
                nuevaVigenciaContrato.setContrato(lovContratos.get(0));
            }
        }
        cargarLovMotivosCambiosSueldos();
        cargarLovMotivosContratos();
        cargarLovTercerosSucursales();
        cargarLovTiposTelefonos();
        cargarLovEstadosCiviles();
        cargarLovSucursales();

        context.reset("formLovs:formDTipoSalario:lovReformaLaboralTipoSalario:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovReformaLaboralTipoSalario').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('lovReformaLaboralTipoSalario').unselectAllRows()");
        PrimefacesContextUI.actualizar("formLovs:formDTipoSalario:ReformaLaboralTipoSalarioDialogo");
        PrimefacesContextUI.actualizar("formLovs:formDTipoSalario:lovReformaLaboralTipoSalario");
        PrimefacesContextUI.actualizar("formLovs:formDTipoSalario:aceptarRLTS");

        context.reset("formLovs:formDSueldo:formTipoSueldo:lovTipoSueldoSueldo:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovTipoSueldoSueldo').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('lovTipoSueldoSueldo').unselectAllRows()");
        PrimefacesContextUI.actualizar("formLovs:formDSueldo:formTipoSueldo:TipoSueldoSueldoDialogo");
        PrimefacesContextUI.actualizar("formLovs:formDSueldo:formTipoSueldo:lovTipoSueldoSueldo");
        PrimefacesContextUI.actualizar("formLovs:formDSueldo:formTipoSueldo:aceptarTSS");

        context.reset("formLovs:formDTipoContrato:formTipoContrato:lovTipoContratoTipoContrato:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovTipoContratoTipoContrato').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('lovTipoContratoTipoContrato').unselectAllRows()");
        PrimefacesContextUI.actualizar("formLovs:formDTipoContrato:formTipoContrato:TipoContratoTipoContratoDialogo");
        PrimefacesContextUI.actualizar("formLovs:formDTipoContrato:formTipoContrato:lovTipoContratoTipoContrato");
        PrimefacesContextUI.actualizar("formLovs:formDTipoContrato:formTipoContrato:aceptarTCTC");

        context.reset("formLovs:formDNormaLaboral:lovNormaLaboralNormaLaboral:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovNormaLaboralNormaLaboral').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('lovNormaLaboralNormaLaboral').unselectAllRows()");
        PrimefacesContextUI.actualizar("formLovs:formDNormaLaboral:NormaLaboralNormaLaboralDialogo");
        PrimefacesContextUI.actualizar("formLovs:formDNormaLaboral:lovNormaLaboralNormaLaboral");
        PrimefacesContextUI.actualizar("formLovs:formDNormaLaboral:aceptarNLNL");

        context.reset("formLovs:formDLegislacionLaboral:lovContratoLegislacionLaboral:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovContratoLegislacionLaboral').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('lovContratoLegislacionLaboral').unselectAllRows()");
        PrimefacesContextUI.actualizar("formLovs:formDLegislacionLaboral:ContratoLegislacionLaboralDialogo");
        PrimefacesContextUI.actualizar("formLovs:formDLegislacionLaboral:lovContratoLegislacionLaboral");
        PrimefacesContextUI.actualizar("formLovs:formDLegislacionLaboral:aceptarCLL");

        PrimefacesContextUI.actualizar("form:tipoSalarioModTipoSalario");
        PrimefacesContextUI.actualizar("form:tipoSueldoModSueldo");
        PrimefacesContextUI.actualizar("form:tipoContratoModTipoContrato");
        PrimefacesContextUI.actualizar("form:normaLaboralModNormaLaboral");
        PrimefacesContextUI.actualizar("form:legislacionLaboralModLegislacionLaboral");
    }

    public void cargarLovMotivosCambiosSueldos() {
        if (lovMotivosCambiosSueldos == null) {
            lovMotivosCambiosSueldos = administrarPersonaIndividual.lovMotivosCambiosSueldos();
        }
    }

    public void cargarLovMotivosContratos() {
        if (lovMotivosContratos == null) {
            lovMotivosContratos = administrarPersonaIndividual.lovMotivosContratos();
        }
    }

    public void cargarLovUbicacionesGeograficas() {
        if (nuevoEmpleado.getEmpresa().getSecuencia() != null && lovUbicacionesGeograficas == null) {
            lovUbicacionesGeograficas = administrarPersonaIndividual.lovUbicacionesGeograficas(nuevoEmpleado.getEmpresa().getSecuencia());
        }
        /*
         * if (nuevoEmpleado.getEmpresa() != null && lovUbicacionesGeograficas
         * == null) { lovUbicacionesGeograficas =
         * administrarPersonaIndividual.lovUbicacionesGeograficas(nuevoEmpleado.getEmpresa());
         * }
         */
    }

    public void cargarLovPeriodicidades() {
        if (lovPeriodicidades == null) {
            lovPeriodicidades = administrarPersonaIndividual.lovPeriodicidades();
        }
    }

    public void cargarLovSucursales() {
        if (lovSucursales == null) {
            lovSucursales = administrarPersonaIndividual.lovSucursales();
        }
    }

    public void cargarLovMetodosPagos() {
        if (lovMetodosPagos == null) {
            lovMetodosPagos = administrarPersonaIndividual.lovMetodosPagos();
        }
    }

    public void cargarLovJornadasLaborales() {
        if (lovJornadasLaborales == null) {
            lovJornadasLaborales = administrarPersonaIndividual.lovJornadasLaborales();
        }
    }

    public void cargarLovTercerosSucursales() {
        if (lovTercerosSucursales == null && nuevoEmpleado.getEmpresa().getSecuencia() != null) {
            lovTercerosSucursales = administrarPersonaIndividual.lovTercerosSucursales(nuevoEmpleado.getEmpresa().getSecuencia());
        }
        /*
         * if (lovTercerosSucursales == null && nuevoEmpleado.getEmpresa() !=
         * null) { lovTercerosSucursales =
         * administrarPersonaIndividual.lovTercerosSucursales(nuevoEmpleado.getEmpresa());
         * }
         */
    }

    public void cargarLovEstadosCiviles() {
        if (lovEstadosCiviles == null) {
            lovEstadosCiviles = administrarPersonaIndividual.lovEstadosCiviles();
        }
    }

    public void cargarLovTiposTelefonos() {
        if (lovTiposTelefonos == null) {
            lovTiposTelefonos = administrarPersonaIndividual.lovTiposTelefonos();
        }
    }

    //////////////////////GETS Y SETS///////////////
    public Personas getNuevaPersona() {
        return nuevaPersona;
    }

    public void setNuevaPersona(Personas nuevoEmpleado) {
        this.nuevaPersona = nuevoEmpleado;

    }

    public boolean isAceptar() {
        return aceptar;
    }

    public void setAceptar(boolean aceptar) {
        this.aceptar = aceptar;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        System.out.println("entro en setFechaIngreso con FechaIngreso: " + fechaIngreso);
        this.fechaIngreso = fechaIngreso;
    }

    public Date getFechaCorte() {
        return fechaCorte;
    }

    public void setFechaCorte(Date fechaCorte) {
        System.out.println("entro en setFechaCorte con fechaCorte: " + fechaCorte);
        this.fechaCorte = fechaCorte;
    }

    public ParametrosEstructuras getParametrosEstructuras() {
        if (parametrosEstructuras == null) {
            parametrosEstructuras = administrarCarpetaPersonal.consultarParametrosUsuario();
        }
        return parametrosEstructuras;
    }

    public void setParametrosEstructuras(ParametrosEstructuras parametrosEstructuras) {
        this.parametrosEstructuras = parametrosEstructuras;
    }

    public Date getFechaDesdeParametro() {
        getParametrosEstructuras();
        fechaDesdeParametro = parametrosEstructuras.getFechadesdecausado();
        return fechaDesdeParametro;
    }

    public void setFechaDesdeParametro(Date fechaDesdeParametro) {
        this.fechaDesdeParametro = fechaDesdeParametro;
    }

    public Date getFechaHastaParametro() {
        getParametrosEstructuras();
        fechaHastaParametro = parametrosEstructuras.getFechahastacausado();
        return fechaHastaParametro;
    }

    public void setFechaHastaParametro(Date fechaHastaParametro) {
        this.fechaHastaParametro = fechaHastaParametro;
    }

    public Empleados getNuevoEmpleado() {
        return nuevoEmpleado;
    }

    public void setNuevoEmpleado(Empleados nuevoEmpleado) {
        this.nuevoEmpleado = nuevoEmpleado;
    }

    public VigenciasCargos getNuevaVigenciaCargo() {
        return nuevaVigenciaCargo;
    }

    public void setNuevaVigenciaCargo(VigenciasCargos nuevaVigenciaCargo) {
        this.nuevaVigenciaCargo = nuevaVigenciaCargo;
    }

    public boolean isDisableNombreEstructuraCargo() {
        return disableNombreEstructuraCargo;
    }

    public void setDisableNombreEstructuraCargo(boolean disableNombreEstructuraCargo) {
        this.disableNombreEstructuraCargo = disableNombreEstructuraCargo;
    }

    public VigenciasLocalizaciones getNuevaVigenciaLocalizacion() {
        return nuevaVigenciaLocalizacion;
    }

    public void setNuevaVigenciaLocalizacion(VigenciasLocalizaciones nuevaVigenciaLocalizacion) {
        this.nuevaVigenciaLocalizacion = nuevaVigenciaLocalizacion;
    }

    public boolean isDisableDescripcionEstructura() {
        return disableDescripcionEstructura;
    }

    public void setDisableDescripcionEstructura(boolean disableDescripcionEstructura) {
        this.disableDescripcionEstructura = disableDescripcionEstructura;
    }

    public VigenciasTiposTrabajadores getNuevaVigenciaTipoTrabajador() {
        return nuevaVigenciaTipoTrabajador;
    }

    public void setNuevaVigenciaTipoTrabajador(VigenciasTiposTrabajadores nuevaVigenciaTipoTrabajador) {
        this.nuevaVigenciaTipoTrabajador = nuevaVigenciaTipoTrabajador;
    }

    public VigenciasReformasLaborales getNuevaVigenciaReformaLaboral() {
        return nuevaVigenciaReformaLaboral;
    }

    public void setNuevaVigenciaReformaLaboral(VigenciasReformasLaborales nuevaVigenciaReformaLaboral) {
        this.nuevaVigenciaReformaLaboral = nuevaVigenciaReformaLaboral;
    }

    public VigenciasSueldos getNuevaVigenciaSueldo() {
        return nuevaVigenciaSueldo;
    }

    public void setNuevaVigenciaSueldo(VigenciasSueldos nuevaVigenciaSueldo) {
        this.nuevaVigenciaSueldo = nuevaVigenciaSueldo;
    }

    public BigDecimal getValorSueldo() {
        return valorSueldo;
    }

    public void setValorSueldo(BigDecimal valorSueldo) {
        this.valorSueldo = valorSueldo;
    }

    public VigenciasTiposContratos getNuevaVigenciaTipoContrato() {
        return nuevaVigenciaTipoContrato;
    }

    public void setNuevaVigenciaTipoContrato(VigenciasTiposContratos nuevaVigenciaTipoContrato) {
        this.nuevaVigenciaTipoContrato = nuevaVigenciaTipoContrato;
    }

    public VigenciasNormasEmpleados getNuevaVigenciaNormaEmpleado() {
        return nuevaVigenciaNormaEmpleado;
    }

    public void setNuevaVigenciaNormaEmpleado(VigenciasNormasEmpleados nuevaVigenciaNormaEmpleado) {
        this.nuevaVigenciaNormaEmpleado = nuevaVigenciaNormaEmpleado;
    }

    public VigenciasContratos getNuevaVigenciaContrato() {
        return nuevaVigenciaContrato;
    }

    public void setNuevaVigenciaContrato(VigenciasContratos nuevaVigenciaContrato) {
        this.nuevaVigenciaContrato = nuevaVigenciaContrato;
    }

    public VigenciasUbicaciones getNuevaVigenciaUbicacion() {
        return nuevaVigenciaUbicacion;
    }

    public void setNuevaVigenciaUbicacion(VigenciasUbicaciones nuevaVigenciaUbicacion) {
        this.nuevaVigenciaUbicacion = nuevaVigenciaUbicacion;
    }

    public boolean isDisableUbicacionGeografica() {
        return disableUbicacionGeografica;
    }

    public void setDisableUbicacionGeografica(boolean disableUbicacionGeografica) {
        this.disableUbicacionGeografica = disableUbicacionGeografica;
    }

    public VigenciasFormasPagos getNuevaVigenciaFormaPago() {
        return nuevaVigenciaFormaPago;
    }

    public void setNuevaVigenciaFormaPago(VigenciasFormasPagos nuevaVigenciaFormaPago) {
        this.nuevaVigenciaFormaPago = nuevaVigenciaFormaPago;
    }

    public VigenciasJornadas getNuevaVigenciaJornada() {
        return nuevaVigenciaJornada;
    }

    public void setNuevaVigenciaJornada(VigenciasJornadas nuevaVigenciaJornada) {
        this.nuevaVigenciaJornada = nuevaVigenciaJornada;
    }

    public VigenciasAfiliaciones getNuevaVigenciaAfiliacionEPS() {
        return nuevaVigenciaAfiliacionEPS;
    }

    public void setNuevaVigenciaAfiliacionEPS(VigenciasAfiliaciones nuevaVigenciaAfiliacionEPS) {
        this.nuevaVigenciaAfiliacionEPS = nuevaVigenciaAfiliacionEPS;
    }

    public VigenciasAfiliaciones getNuevaVigenciaAfiliacionAFP() {
        return nuevaVigenciaAfiliacionAFP;
    }

    public void setNuevaVigenciaAfiliacionAFP(VigenciasAfiliaciones nuevaVigenciaAfiliacionAFP) {
        this.nuevaVigenciaAfiliacionAFP = nuevaVigenciaAfiliacionAFP;
    }

    public VigenciasAfiliaciones getNuevaVigenciaAfiliacionFondo() {
        return nuevaVigenciaAfiliacionFondo;
    }

    public void setNuevaVigenciaAfiliacionFondo(VigenciasAfiliaciones nuevaVigenciaAfiliacionFondo) {
        this.nuevaVigenciaAfiliacionFondo = nuevaVigenciaAfiliacionFondo;
    }

    public VigenciasAfiliaciones getNuevaVigenciaAfiliacionARP() {
        return nuevaVigenciaAfiliacionARP;
    }

    public void setNuevaVigenciaAfiliacionARP(VigenciasAfiliaciones nuevaVigenciaAfiliacionARP) {
        this.nuevaVigenciaAfiliacionARP = nuevaVigenciaAfiliacionARP;
    }

    public VigenciasAfiliaciones getNuevaVigenciaAfiliacionCaja() {
        return nuevaVigenciaAfiliacionCaja;
    }

    public void setNuevaVigenciaAfiliacionCaja(VigenciasAfiliaciones nuevaVigenciaAfiliacionCaja) {
        this.nuevaVigenciaAfiliacionCaja = nuevaVigenciaAfiliacionCaja;
    }

    public boolean isDisableAfiliaciones() {
        return disableAfiliaciones;
    }

    public void setDisableAfiliaciones(boolean disableAfiliaciones) {
        this.disableAfiliaciones = disableAfiliaciones;
    }

    public VigenciasEstadosCiviles getNuevoEstadoCivil() {
        return nuevoEstadoCivil;
    }

    public void setNuevoEstadoCivil(VigenciasEstadosCiviles nuevoEstadoCivil) {
        this.nuevoEstadoCivil = nuevoEstadoCivil;
    }

    public Direcciones getNuevaDireccion() {
        return nuevaDireccion;
    }

    public void setNuevaDireccion(Direcciones nuevaDireccion) {
        this.nuevaDireccion = nuevaDireccion;
    }

    public Telefonos getNuevoTelefono() {
        return nuevoTelefono;
    }

    public void setNuevoTelefono(Telefonos nuevoTelefono) {
        this.nuevoTelefono = nuevoTelefono;
    }

    ///////////CONTEO DE REGISTROS/////////////
    public String getInfoRegistroEmpresaInfoPersonal() {
        return infoRegistroEmpresaInfoPersonal;
    }

    public String getInfoRegistroTipoDocInfoPersonal() {
        return infoRegistroTipoDocInfoPersonal;
    }

    public String getInfoRegistroCiudadDocInfoPersonal() {
        return infoRegistroCiudadDocInfoPersonal;
    }

    public String getInfoRegistroCiudadNacInfoPersonal() {
        return infoRegistroCiudadNacInfoPersonal;
    }

    public String getInfoRegistroCargoDesempenado() {
        return infoRegistroCargoDesempenado;
    }

    public String getInfoRegistroMotivoCargoDesempenado() {
        return infoRegistroMotivoCargoDesempenado;
    }

    public String getInfoRegistroEstructuraCargoDesempenado() {
        return infoRegistroEstructuraCargoDesempenado;
    }

    public String getInfoRegistroPapelCargoDesempenado() {
        return infoRegistroPapelCargoDesempenado;
    }

    public String getInfoRegistroJefeCargoDesempenado() {
        return infoRegistroJefeCargoDesempenado;
    }

    public String getInfoRegistroMotivoCentroCosto() {
        return infoRegistroMotivoCentroCosto;
    }

    public String getInfoRegistroEstructuraCentroCosto() {
        return infoRegistroEstructuraCentroCosto;
    }

    public String getInfoRegistroTipoTrabajadorTT() {
        return infoRegistroTipoTrabajadorTT;
    }

    public String getInfoRegistroReformaTipoSalario() {
        return infoRegistroReformaTipoSalario;
    }

    public String getInfoRegistroMotivoSueldo() {
        return infoRegistroMotivoSueldo;
    }

    public String getInfoRegistroTipoSueldoS() {
        return infoRegistroTipoSueldoS;
    }

    public String getInfoRegistroUnidadSueldo() {
        return infoRegistroUnidadSueldo;
    }

    public String getInfoRegistroMotivoTipoContrato() {
        return infoRegistroMotivoTipoContrato;
    }

    public String getInfoRegistroTipoContrato() {
        return infoRegistroTipoContrato;
    }

    public String getInfoRegistroNormaLaboral() {
        return infoRegistroNormaLaboral;
    }

    public String getInfoRegistroContratoLegislacionL() {
        return infoRegistroContratoLegislacionL;
    }

    public String getInfoRegistroUbicacionUb() {
        return infoRegistroUbicacionUb;
    }

    public String getInfoRegistroJornadaLaboral() {
        return infoRegistroJornadaLaboral;
    }

    public String getInfoRegistroFormaPago() {
        return infoRegistroFormaPago;
    }

    public String getInfoRegistroSucursalFormaPago() {
        return infoRegistroSucursalFormaPago;
    }

    public String getInfoRegistroMetodoFormaPago() {
        return infoRegistroMetodoFormaPago;
    }

    public String getInfoRegistroTerceroAfiliacion() {
        return infoRegistroTerceroAfiliacion;
    }

    public String getInfoRegistrolEstadoCivil() {
        return infoRegistroEstadoCivil;
    }

    public String getInfoRegistroCiudadDireccion() {
        return infoRegistroCiudadDireccion;
    }

    public String getInfoRegistroCiudadTelefono() {
        return infoRegistroCiudadTelefono;
    }

    public String getInfoRegistroTipoTelTelefono() {
        return infoRegistroTipoTelTelefono;
    }

    public boolean isDisableCamposDependientesTipoT() {
        return disableCamposDependientesTipoT;
    }

    public void setDisableCamposDependientesTipoT(boolean disableCamposDependientesTipoTrabajador) {
        this.disableCamposDependientesTipoT = disableCamposDependientesTipoTrabajador;
    }

    public String getMensajeErrorFechasEmpleado() {
        return mensajeErrorFechasEmpleado;
    }

    public void setMensajeErrorFechasEmpleado(String mensajeErrorFechasEmpleado) {
        this.mensajeErrorFechasEmpleado = mensajeErrorFechasEmpleado;
    }

    public Unidades getUnidadPesos() {
        return unidadPesos;
    }

    public void setUnidadPesos(Unidades unidadPesos) {
        this.unidadPesos = unidadPesos;
    }

    ///////////////LOVS////////////////
    public List<TiposTelefonos> getLovTiposTelefonos() {
        return lovTiposTelefonos;
    }

    public List<EstadosCiviles> getLovEstadosCiviles() {
        return lovEstadosCiviles;
    }

    public List<TercerosSucursales> getLovTercerosSucursales() {
        return lovTercerosSucursales;
    }

    public List<JornadasLaborales> getLovJornadasLaborales() {
        return lovJornadasLaborales;
    }

    public List<Periodicidades> getLovPeriodicidades() {
        return lovPeriodicidades;
    }

    public List<Sucursales> getLovSucursales() {
        return lovSucursales;
    }

    public List<MetodosPagos> getLovMetodosPagos() {
        return lovMetodosPagos;
    }

    public List<UbicacionesGeograficas> getLovUbicacionesGeograficas() {
        return lovUbicacionesGeograficas;
    }

    public List<Contratos> getLovContratos() {
        return lovContratos;
    }

    public List<NormasLaborales> getLovNormasLaborales() {
        return lovNormasLaborales;
    }

    public List<MotivosContratos> getLovMotivosContratos() {
        return lovMotivosContratos;
    }

    public List<TiposContratos> getLovTiposContratos() {
        return lovTiposContratos;
    }

    public List<MotivosCambiosSueldos> getLovMotivosCambiosSueldos() {
        return lovMotivosCambiosSueldos;
    }

    public List<TiposSueldos> getLovTiposSueldos() {
        return lovTiposSueldos;
    }

    public List<ReformasLaborales> getLovReformasLaborales() {
        return lovReformasLaborales;
    }

    public List<TiposTrabajadores> getLovTiposTrabajadores() {
        return lovTiposTrabajadores;
    }

    public List<Empresas> getLovEmpresas() {
        if (lovEmpresas == null) {
            lovEmpresas = administrarPersonaIndividual.lovEmpresas();
        }
        return lovEmpresas;
    }

    public List<TiposDocumentos> getLovTiposDocumentos() {
        return lovTiposDocumentos;
    }

    public List<Ciudades> getLovCiudades() {
        return lovCiudades;
    }

    public List<Cargos> getLovCargos() {
        return lovCargos;
    }

    public List<MotivosCambiosCargos> getLovMotivosCargos() {
        return lovMotivosCargos;
    }

    public List<Estructuras> getLovEstructuras() {
        return lovEstructuras;
    }

    public List<Papeles> getLovPapeles() {
        return lovPapeles;
    }

    public List<Empleados> getLovEmpleados() {
        return lovEmpleados;
    }

    public List<MotivosLocalizaciones> getLovMotivosLocalizaciones() {
        return lovMotivosLocalizaciones;
    }

    public List<Estructuras> getLovEstructurasCentroCosto() {
        return lovEstructurasCentroCosto;
    }

    public List<Unidades> getLovUnidades() {
        if (lovUnidades == null) {
            lovUnidades = administrarPersonaIndividual.lovUnidades();
            if (lovUnidades != null) {
                if (!lovUnidades.isEmpty()) {
                    for (int i = 0; i < lovUnidades.size(); i++) {
                        if (lovUnidades.get(i).getNombre().equals("PESOS")) {
                            unidadPesos = lovUnidades.get(i);
                            nuevaVigenciaSueldo.setUnidadpago(unidadPesos);
                            System.out.println("getLovUnidades() nuevaVigenciaSueldo.getUnidadpago sec: " + nuevaVigenciaSueldo.getUnidadpago().getSecuencia() + ",  nombre: " + nuevaVigenciaSueldo.getUnidadpago().getNombre());
                        }
                    }
                }
            }
        }
        return lovUnidades;
    }

    public void setLovTiposTelefonos(List<TiposTelefonos> lovTiposTelefonos) {
        this.lovTiposTelefonos = lovTiposTelefonos;
    }

    public void setLovEstadosCiviles(List<EstadosCiviles> lovEstadosCiviles) {
        this.lovEstadosCiviles = lovEstadosCiviles;
    }

    public void setLovTercerosSucursales(List<TercerosSucursales> lovTercerosSucursales) {
        this.lovTercerosSucursales = lovTercerosSucursales;
    }

    public void setLovJornadasLaborales(List<JornadasLaborales> lovJornadasLaborales) {
        this.lovJornadasLaborales = lovJornadasLaborales;
    }

    public void setLovPeriodicidades(List<Periodicidades> lovPeriodicidades) {
        this.lovPeriodicidades = lovPeriodicidades;
    }

    public void setLovSucursales(List<Sucursales> lovSucursales) {
        this.lovSucursales = lovSucursales;
    }

    public void setLovMetodosPagos(List<MetodosPagos> lovMetodosPagos) {
        this.lovMetodosPagos = lovMetodosPagos;
    }

    public void setLovUbicacionesGeograficas(List<UbicacionesGeograficas> lovUbicacionesGeograficas) {
        this.lovUbicacionesGeograficas = lovUbicacionesGeograficas;
    }

    public void setLovContratos(List<Contratos> lovContratos) {
        this.lovContratos = lovContratos;
    }

    public void setLovNormasLaborales(List<NormasLaborales> lovNormasLaborales) {
        this.lovNormasLaborales = lovNormasLaborales;
    }

    public void setLovMotivosContratos(List<MotivosContratos> lovMotivosContratos) {
        this.lovMotivosContratos = lovMotivosContratos;
    }

    public void setLovTiposContratos(List<TiposContratos> lovTiposContratos) {
        this.lovTiposContratos = lovTiposContratos;
    }

    public void setLovMotivosCambiosSueldos(List<MotivosCambiosSueldos> lovMotivosCambiosSueldos) {
        this.lovMotivosCambiosSueldos = lovMotivosCambiosSueldos;
    }

    public void setLovTiposSueldos(List<TiposSueldos> lovTiposSueldos) {
        this.lovTiposSueldos = lovTiposSueldos;
    }

    public void setLovUnidades(List<Unidades> lovUnidades) {
        this.lovUnidades = lovUnidades;
    }

    public void setLovReformasLaborales(List<ReformasLaborales> lovReformasLaborales) {
        this.lovReformasLaborales = lovReformasLaborales;
    }

    public void setLovTiposTrabajadores(List<TiposTrabajadores> lovTiposTrabajadores) {
        this.lovTiposTrabajadores = lovTiposTrabajadores;
    }

    public void setLovEmpresas(List<Empresas> lovEmpresas) {
        this.lovEmpresas = lovEmpresas;
    }

    public void setLovTiposDocumentos(List<TiposDocumentos> lovTiposDocumentos) {
        this.lovTiposDocumentos = lovTiposDocumentos;
    }

    public void setLovCiudades(List<Ciudades> lovCiudades) {
        this.lovCiudades = lovCiudades;
    }

    public void setLovCargos(List<Cargos> lovCargos) {
        this.lovCargos = lovCargos;
    }

    public void setLovMotivosCargos(List<MotivosCambiosCargos> lovMotivosCargos) {
        this.lovMotivosCargos = lovMotivosCargos;
    }

    public void setLovEstructuras(List<Estructuras> lovEstructuras) {
        this.lovEstructuras = lovEstructuras;
    }

    public void setLovPapeles(List<Papeles> lovPapeles) {
        this.lovPapeles = lovPapeles;
    }

    public void setLovEmpleados(List<Empleados> lovEmpleados) {
        this.lovEmpleados = lovEmpleados;
    }

    public void setLovMotivosLocalizaciones(List<MotivosLocalizaciones> lovMotivosLocalizaciones) {
        this.lovMotivosLocalizaciones = lovMotivosLocalizaciones;
    }

    public void setLovEstructurasCentroCosto(List<Estructuras> lovEstructurasCentroCosto) {
        this.lovEstructurasCentroCosto = lovEstructurasCentroCosto;
    }

    public TiposTelefonos getTipoTelefonoSeleccionado() {
        return tipoTelefonoSeleccionado;
    }

    public void setTipoTelefonoSeleccionado(TiposTelefonos tipoTelefonoSeleccionado) {
        this.tipoTelefonoSeleccionado = tipoTelefonoSeleccionado;
    }

    public EstadosCiviles getEstadoCivilSeleccionado() {
        return estadoCivilSeleccionado;
    }

    public void setEstadoCivilSeleccionado(EstadosCiviles estadoCivilSeleccionado) {
        this.estadoCivilSeleccionado = estadoCivilSeleccionado;
    }

    public TercerosSucursales getTerceroSucursalSeleccionado() {
        return terceroSucursalSeleccionado;
    }

    public void setTerceroSucursalSeleccionado(TercerosSucursales terceroSucursalSeleccionado) {
        this.terceroSucursalSeleccionado = terceroSucursalSeleccionado;
    }

    public JornadasLaborales getJornadaLaboralSeleccionada() {
        return jornadaLaboralSeleccionada;
    }

    public void setJornadaLaboralSeleccionada(JornadasLaborales jornadaLaboralSeleccionada) {
        this.jornadaLaboralSeleccionada = jornadaLaboralSeleccionada;
    }

    public Periodicidades getPeriodicidadSeleccionada() {
        return periodicidadSeleccionada;
    }

    public void setPeriodicidadSeleccionada(Periodicidades periodicidadSeleccionada) {
        this.periodicidadSeleccionada = periodicidadSeleccionada;
    }

    public Sucursales getSucursalSeleccionada() {
        return sucursalSeleccionada;
    }

    public void setSucursalSeleccionada(Sucursales sucursalSeleccionada) {
        this.sucursalSeleccionada = sucursalSeleccionada;
    }

    public MetodosPagos getMetodoPagoSeleccionado() {
        return metodoPagoSeleccionado;
    }

    public void setMetodoPagoSeleccionado(MetodosPagos metodoPagoSeleccionado) {
        this.metodoPagoSeleccionado = metodoPagoSeleccionado;
    }

    public UbicacionesGeograficas getUbicacionGeograficaSeleccionada() {
        return ubicacionGeograficaSeleccionada;
    }

    public void setUbicacionGeograficaSeleccionada(UbicacionesGeograficas ubicacionGeograficaSeleccionada) {
        this.ubicacionGeograficaSeleccionada = ubicacionGeograficaSeleccionada;
    }

    public Contratos getContratoSeleccionado() {
        return contratoSeleccionado;
    }

    public void setContratoSeleccionado(Contratos contratoSeleccionado) {
        this.contratoSeleccionado = contratoSeleccionado;
    }

    public NormasLaborales getNormaLaboralSeleccionada() {
        return normaLaboralSeleccionada;
    }

    public void setNormaLaboralSeleccionada(NormasLaborales normaLaboralSeleccionada) {
        this.normaLaboralSeleccionada = normaLaboralSeleccionada;
    }

    public MotivosContratos getMotivoContratoSeleccionado() {
        return motivoContratoSeleccionado;
    }

    public void setMotivoContratoSeleccionado(MotivosContratos motivoContratoSeleccionado) {
        this.motivoContratoSeleccionado = motivoContratoSeleccionado;
    }

    public TiposContratos getTipoContratoSeleccionado() {
        return tipoContratoSeleccionado;
    }

    public void setTipoContratoSeleccionado(TiposContratos tipoContratoSeleccionado) {
        this.tipoContratoSeleccionado = tipoContratoSeleccionado;
    }

    public MotivosCambiosSueldos getMotivoCambioSueldoSeleccionado() {
        return motivoCambioSueldoSeleccionado;
    }

    public void setMotivoCambioSueldoSeleccionado(MotivosCambiosSueldos motivoCambioSueldoSeleccionado) {
        this.motivoCambioSueldoSeleccionado = motivoCambioSueldoSeleccionado;
    }

    public TiposSueldos getTipoSueldoSeleccionado() {
        return tipoSueldoSeleccionado;
    }

    public void setTipoSueldoSeleccionado(TiposSueldos tipoSueldoSeleccionado) {
        this.tipoSueldoSeleccionado = tipoSueldoSeleccionado;
    }

    public Unidades getUnidadSeleccionada() {
        return unidadSeleccionada;
    }

    public void setUnidadSeleccionada(Unidades unidadSeleccionada) {
        this.unidadSeleccionada = unidadSeleccionada;
    }

    public ReformasLaborales getReformaLaboralSeleccionada() {
        return reformaLaboralSeleccionada;
    }

    public void setReformaLaboralSeleccionada(ReformasLaborales reformaLaboralSeleccionada) {
        this.reformaLaboralSeleccionada = reformaLaboralSeleccionada;
    }

    public TiposTrabajadores getTipoTrabajadorSeleccionado() {
        return tipoTrabajadorSeleccionado;
    }

    public void setTipoTrabajadorSeleccionado(TiposTrabajadores tipoTrabajadorSeleccionado) {
        this.tipoTrabajadorSeleccionado = tipoTrabajadorSeleccionado;
    }

    public Empresas getEmpresaSeleccionada() {
        return empresaSeleccionada;
    }

    public void setEmpresaSeleccionada(Empresas empresaSeleccionada) {
        this.empresaSeleccionada = empresaSeleccionada;
//        System.out.println("setEmpresaSeleccionada :" + empresaSeleccionada);
    }

    public TiposDocumentos getTipoDocumentoSeleccionado() {
        return tipoDocumentoSeleccionado;
    }

    public void setTipoDocumentoSeleccionado(TiposDocumentos tipoDocumentoSeleccionado) {
        this.tipoDocumentoSeleccionado = tipoDocumentoSeleccionado;
    }

    public Ciudades getCiudadSeleccionada() {
        return ciudadSeleccionada;
    }

    public void setCiudadSeleccionada(Ciudades ciudadSeleccionada) {
        System.out.println("setCiudadSeleccionada : ciudadSeleccionada : " + ciudadSeleccionada);
        if (ciudadSeleccionada != null) {
            System.out.println("setCiudadSeleccionada : ciudadSeleccionada.nombre : " + ciudadSeleccionada.getNombre());
        }
        this.ciudadSeleccionada = ciudadSeleccionada;
    }

    public Cargos getCargoSeleccionado() {
        return cargoSeleccionado;
    }

    public void setCargoSeleccionado(Cargos cargoSeleccionado) {
        this.cargoSeleccionado = cargoSeleccionado;
    }

    public MotivosCambiosCargos getMotivoCambioCargoSeleccionado() {
        return motivoCambioCargoSeleccionado;
    }

    public void setMotivoCambioCargoSeleccionado(MotivosCambiosCargos motivoCambioCargoSeleccionado) {
        this.motivoCambioCargoSeleccionado = motivoCambioCargoSeleccionado;
    }

    public Estructuras getEstructuraSeleccionada() {
        return estructuraSeleccionada;
    }

    public void setEstructuraSeleccionada(Estructuras estructuraSeleccionada) {
        this.estructuraSeleccionada = estructuraSeleccionada;
    }

    public Papeles getPapelSeleccionado() {
        return papelSeleccionado;
    }

    public void setPapelSeleccionado(Papeles papelSeleccionado) {
        this.papelSeleccionado = papelSeleccionado;
    }

    public Empleados getEmpleadoSeleccionado() {
        return empleadoSeleccionado;
    }

    public void setEmpleadoSeleccionado(Empleados empleadoSeleccionado) {
        this.empleadoSeleccionado = empleadoSeleccionado;
    }

    public MotivosLocalizaciones getMotivoLocalizacionSeleccionado() {
        return motivoLocalizacionSeleccionado;
    }

    public void setMotivoLocalizacionSeleccionado(MotivosLocalizaciones motivoLocalizacionSeleccionado) {
        this.motivoLocalizacionSeleccionado = motivoLocalizacionSeleccionado;
    }

    public Estructuras getEstructuraCentroCostoSeleccionada() {
        return estructuraCentroCostoSeleccionada;
    }

    public void setEstructuraCentroCostoSeleccionada(Estructuras estructuraCentroCostoSeleccionada) {
        this.estructuraCentroCostoSeleccionada = estructuraCentroCostoSeleccionada;
    }

    public List<TiposTelefonos> getFiltrarLovTiposTelefonos() {
        return filtrarLovTiposTelefonos;
    }

    public void setFiltrarLovTiposTelefonos(List<TiposTelefonos> filtrarLovTiposTelefonos) {
        this.filtrarLovTiposTelefonos = filtrarLovTiposTelefonos;
    }

    public List<EstadosCiviles> getFiltrarLovEstadosCiviles() {
        return filtrarLovEstadosCiviles;
    }

    public void setFiltrarLovEstadosCiviles(List<EstadosCiviles> filtrarLovEstadosCiviles) {
        this.filtrarLovEstadosCiviles = filtrarLovEstadosCiviles;
    }

    public List<TercerosSucursales> getFiltrarLovTercerosSucursales() {
        return filtrarLovTercerosSucursales;
    }

    public void setFiltrarLovTercerosSucursales(List<TercerosSucursales> filtrarLovTercerosSucursales) {
        this.filtrarLovTercerosSucursales = filtrarLovTercerosSucursales;
    }

    public List<JornadasLaborales> getFiltrarLovJornadasLaborales() {
        return filtrarLovJornadasLaborales;
    }

    public void setFiltrarLovJornadasLaborales(List<JornadasLaborales> filtrarLovJornadasLaborales) {
        this.filtrarLovJornadasLaborales = filtrarLovJornadasLaborales;
    }

    public List<Periodicidades> getFiltrarLovPeriodicidades() {
        return filtrarLovPeriodicidades;
    }

    public void setFiltrarLovPeriodicidades(List<Periodicidades> filtrarLovPeriodicidades) {
        this.filtrarLovPeriodicidades = filtrarLovPeriodicidades;
    }

    public List<Sucursales> getFiltrarLovSucursales() {
        return filtrarLovSucursales;
    }

    public void setFiltrarLovSucursales(List<Sucursales> filtrarLovSucursales) {
        this.filtrarLovSucursales = filtrarLovSucursales;
    }

    public List<MetodosPagos> getFiltrarLovMetodosPagos() {
        return filtrarLovMetodosPagos;
    }

    public void setFiltrarLovMetodosPagos(List<MetodosPagos> filtrarLovMetodosPagos) {
        this.filtrarLovMetodosPagos = filtrarLovMetodosPagos;
    }

    public List<UbicacionesGeograficas> getFiltrarLovUbicacionesGeograficas() {
        return filtrarLovUbicacionesGeograficas;
    }

    public void setFiltrarLovUbicacionesGeograficas(List<UbicacionesGeograficas> filtrarLovUbicacionesGeograficas) {
        this.filtrarLovUbicacionesGeograficas = filtrarLovUbicacionesGeograficas;
    }

    public List<Contratos> getFiltrarLovContratos() {
        return filtrarLovContratos;
    }

    public void setFiltrarLovContratos(List<Contratos> filtrarLovContratos) {
        this.filtrarLovContratos = filtrarLovContratos;
    }

    public List<NormasLaborales> getFiltrarLovNormasLaborales() {
        return filtrarLovNormasLaborales;
    }

    public void setFiltrarLovNormasLaborales(List<NormasLaborales> filtrarLovNormasLaborales) {
        this.filtrarLovNormasLaborales = filtrarLovNormasLaborales;
    }

    public List<MotivosContratos> getFiltrarLovMotivosContratos() {
        return filtrarLovMotivosContratos;
    }

    public void setFiltrarLovMotivosContratos(List<MotivosContratos> filtrarLovMotivosContratos) {
        this.filtrarLovMotivosContratos = filtrarLovMotivosContratos;
    }

    public List<TiposContratos> getFiltrarLovTiposContratos() {
        return filtrarLovTiposContratos;
    }

    public void setFiltrarLovTiposContratos(List<TiposContratos> filtrarLovTiposContratos) {
        this.filtrarLovTiposContratos = filtrarLovTiposContratos;
    }

    public List<MotivosCambiosSueldos> getFiltrarLovMotivosCambiosSueldos() {
        return filtrarLovMotivosCambiosSueldos;
    }

    public void setFiltrarLovMotivosCambiosSueldos(List<MotivosCambiosSueldos> filtrarLovMotivosCambiosSueldos) {
        this.filtrarLovMotivosCambiosSueldos = filtrarLovMotivosCambiosSueldos;
    }

    public List<TiposSueldos> getFiltrarLovTiposSueldos() {
        return filtrarLovTiposSueldos;
    }

    public void setFiltrarLovTiposSueldos(List<TiposSueldos> filtrarLovTiposSueldos) {
        this.filtrarLovTiposSueldos = filtrarLovTiposSueldos;
    }

    public List<Unidades> getFiltrarLovUnidades() {
        return filtrarLovUnidades;
    }

    public void setFiltrarLovUnidades(List<Unidades> filtrarLovUnidades) {
        this.filtrarLovUnidades = filtrarLovUnidades;
    }

    public List<ReformasLaborales> getFiltrarLovReformasLaborales() {
        return filtrarLovReformasLaborales;
    }

    public void setFiltrarLovReformasLaborales(List<ReformasLaborales> filtrarLovReformasLaborales) {
        this.filtrarLovReformasLaborales = filtrarLovReformasLaborales;
    }

    public List<TiposTrabajadores> getFiltrarLovTiposTrabajadores() {
        return filtrarLovTiposTrabajadores;
    }

    public void setFiltrarLovTiposTrabajadores(List<TiposTrabajadores> filtrarLovTiposTrabajadores) {
        this.filtrarLovTiposTrabajadores = filtrarLovTiposTrabajadores;
    }

    public List<Empresas> getFiltrarLovEmpresas() {
        return filtrarLovEmpresas;
    }

    public void setFiltrarLovEmpresas(List<Empresas> filtrarLovEmpresas) {
        this.filtrarLovEmpresas = filtrarLovEmpresas;
    }

    public List<TiposDocumentos> getFiltrarLovTiposDocumentos() {
        return filtrarLovTiposDocumentos;
    }

    public void setFiltrarLovTiposDocumentos(List<TiposDocumentos> filtrarLovTiposDocumentos) {
        this.filtrarLovTiposDocumentos = filtrarLovTiposDocumentos;
    }

    public List<Ciudades> getFiltrarLovCiudades() {
        return filtrarLovCiudades;
    }

    public void setFiltrarLovCiudades(List<Ciudades> filtrarLovCiudades) {
        this.filtrarLovCiudades = filtrarLovCiudades;
    }

    public List<Cargos> getFiltrarLovCargos() {
        return filtrarLovCargos;
    }

    public void setFiltrarLovCargos(List<Cargos> filtrarLovCargos) {
        this.filtrarLovCargos = filtrarLovCargos;
    }

    public List<MotivosCambiosCargos> getFiltrarLovMotivosCambiosCargos() {
        return filtrarLovMotivosCambiosCargos;
    }

    public void setFiltrarLovMotivosCambiosCargos(List<MotivosCambiosCargos> filtrarLovMotivosCambiosCargos) {
        this.filtrarLovMotivosCambiosCargos = filtrarLovMotivosCambiosCargos;
    }

    public List<Estructuras> getFiltrarLovEstructuras() {
        return filtrarLovEstructuras;
    }

    public void setFiltrarLovEstructuras(List<Estructuras> filtrarLovEstructuras) {
        this.filtrarLovEstructuras = filtrarLovEstructuras;
    }

    public List<Papeles> getFiltrarLovPapeles() {
        return filtrarLovPapeles;
    }

    public void setFiltrarLovPapeles(List<Papeles> filtrarLovPapeles) {
        this.filtrarLovPapeles = filtrarLovPapeles;
    }

    public List<Empleados> getFiltrarLovEmpleados() {
        return filtrarLovEmpleados;
    }

    public void setFiltrarLovEmpleados(List<Empleados> filtrarLovEmpleados) {
        this.filtrarLovEmpleados = filtrarLovEmpleados;
    }

    public List<MotivosLocalizaciones> getFiltrarLovMotivosCC() {
        return filtrarLovMotivosCC;
    }

    public void setFiltrarLovMotivosCC(List<MotivosLocalizaciones> filtrarLovMotivosCC) {
        this.filtrarLovMotivosCC = filtrarLovMotivosCC;
    }

    public List<Estructuras> getFiltrarLovEstructurasCentroCosto() {
        return filtrarLovEstructurasCentroCosto;
    }

    public void setFiltrarLovEstructurasCentroCosto(List<Estructuras> filtrarLovEstructurasCentroCosto) {
        this.filtrarLovEstructurasCentroCosto = filtrarLovEstructurasCentroCosto;
    }

    public String getTituloTercero() {
        return tituloTercero;
    }

    public void setTituloTercero(String tituloTercero) {
        this.tituloTercero = tituloTercero;
    }

    public void setTxt_tipoDoc(String txt_tipoDoce) {
        System.out.println("--setTxt_tipoDoc Entro : " + txt_tipoDoce);
        System.out.println("--setTxt_tipoDoc() nuevaPersona.getTipodocumento().getNombrelargo() : " + nuevaPersona.getTipodocumento().getNombrelargo());
        this.txt_tipoDoc = txt_tipoDoce;
    }

    public void setTxt_ciudadDoc(String txt_ciudadDoc) {
        this.txt_ciudadDoc = txt_ciudadDoc;
    }

    public void setTxt_ciudadNac(String txt_ciudadNac) {
        this.txt_ciudadNac = txt_ciudadNac;
    }

    public void setTxt_cargo(String txt_cargo) {
        this.txt_cargo = txt_cargo;
    }

    public void setTxt_motivoCargo(String txt_motivoCargo) {
        this.txt_motivoCargo = txt_motivoCargo;
    }

    public void setTxt_papel(String txt_papel) {
        this.txt_papel = txt_papel;
    }

    public void setTxt_empleado(String txt_empleado) {
        this.txt_empleado = txt_empleado;
    }

    public void setTxt_motivoCentroC(String txt_motivoCC) {
        this.txt_motivoCentroC = txt_motivoCC;
    }

    public void setTxt_tipoT(String txt_tipoT) {
        this.txt_tipoT = txt_tipoT;
    }

    public void setTxt_tipoRL(String txt_tipoS) {
        this.txt_tipoRL = txt_tipoS;
    }

    public void setTxt_motivoSu(String txt_motivoSu) {
        this.txt_motivoSu = txt_motivoSu;
    }

    public void setTxt_TipoSu(String txt_TipoSu) {
        this.txt_TipoSu = txt_TipoSu;
    }

    public void setTxt_motivoContrato(String txt_motivoContrato) {
        this.txt_motivoContrato = txt_motivoContrato;
    }

    public void setTxt_tipoContrato(String txt_tipoContrato) {
        this.txt_tipoContrato = txt_tipoContrato;
    }

    public void setTxt_ubicacionG(String txt_ubicacionG) {
        this.txt_ubicacionG = txt_ubicacionG;
    }

    public void setTxt_jornada(String txt_jornada) {
        this.txt_jornada = txt_jornada;
    }

    public void setTxt_formaP(String txt_formaP) {
        this.txt_formaP = txt_formaP;
    }

    public void setTxt_metodoP(String txt_metodoP) {
        this.txt_metodoP = txt_metodoP;
    }

    public void setTxt_terceroEPS(String txt_terceroEPS) {
        this.txt_terceroEPS = txt_terceroEPS;
    }

    public void setTxt_terceroARL(String txt_terceroARL) {
        this.txt_terceroARL = txt_terceroARL;
    }

    public void setTxt_terceroCes(String txt_terceroCes) {
        this.txt_terceroCes = txt_terceroCes;
    }

    public void setTxt_terceroAFP(String txt_terceroAFP) {
        this.txt_terceroAFP = txt_terceroAFP;
    }

    public void setTxt_terceroCC(String txt_terceroCC) {
        this.txt_terceroCC = txt_terceroCC;
    }

    public void setTxt_estadoC(String txt_estadoC) {
        this.txt_estadoC = txt_estadoC;
    }

    public void setTxt_ciudadDir(String txt_ciudadDir) {
        this.txt_ciudadDir = txt_ciudadDir;
    }

    public void setTxt_ciudadTel(String txt_ciudadTel) {
        this.txt_ciudadTel = txt_ciudadTel;
    }

    public void setTxt_tipoTel(String txt_tipoTel) {
        this.txt_tipoTel = txt_tipoTel;
    }

    public String getTxt_tipoDoc() {
        return txt_tipoDoc;
    }

    public String getTxt_ciudadDoc() {
        return txt_ciudadDoc;
    }

    public String getTxt_ciudadNac() {
        return txt_ciudadNac;
    }

    public String getTxt_cargo() {
        return txt_cargo;
    }

    public String getTxt_motivoCargo() {
        return txt_motivoCargo;
    }

    public String getTxt_papel() {
        return txt_papel;
    }

    public String getTxt_empleado() {
        return txt_empleado;
    }

    public String getTxt_motivoCentroC() {
        return txt_motivoCentroC;
    }

    public String getTxt_tipoT() {
        return txt_tipoT;
    }

    public String getTxt_tipoRL() {
        return txt_tipoRL;
    }

    public String getTxt_motivoSu() {
        return txt_motivoSu;
    }

    public String getTxt_TipoSu() {
        return txt_TipoSu;
    }

    public String getTxt_motivoContrato() {
        return txt_motivoContrato;
    }

    public String getTxt_tipoContrato() {
        return txt_tipoContrato;
    }

    public String getTxt_ubicacionG() {
        return txt_ubicacionG;
    }

    public String getTxt_jornada() {
        return txt_jornada;
    }

    public String getTxt_formaP() {
        return txt_formaP;
    }

    public String getTxt_metodoP() {
        return txt_metodoP;
    }

    public String getTxt_terceroEPS() {
        return txt_terceroEPS;
    }

    public String getTxt_terceroARL() {
        return txt_terceroARL;
    }

    public String getTxt_terceroCes() {
        return txt_terceroCes;
    }

    public String getTxt_terceroAFP() {
        return txt_terceroAFP;
    }

    public String getTxt_terceroCC() {
        return txt_terceroCC;
    }

    public String getTxt_estadoC() {
        return txt_estadoC;
    }

    public String getTxt_ciudadDir() {
        return txt_ciudadDir;
    }

    public String getTxt_ciudadTel() {
        return txt_ciudadTel;
    }

    public String getTxt_tipoTel() {
        return txt_tipoTel;
    }

    public String getAuxInformacionPersonaEmpresal() {
        if (auxInformacionPersonaEmpresal == null) {
            auxInformacionPersonaEmpresal = "";
        }
        return auxInformacionPersonaEmpresal;
    }

    public String getAuxInformacionPersonalCiudadNacimiento() {
        if (auxInformacionPersonalCiudadNacimiento == null) {
            auxInformacionPersonalCiudadNacimiento = "";
        }
        return auxInformacionPersonalCiudadNacimiento;
    }

    public String getAuxInformacionPersonalCiudadDocumento() {
        if (auxInformacionPersonalCiudadDocumento == null) {
            auxInformacionPersonalCiudadDocumento = "";
        }
        return auxInformacionPersonalCiudadDocumento;
    }

    public String getAuxInformacionPersonalTipoDocumento() {
        if (auxInformacionPersonalTipoDocumento == null) {
            auxInformacionPersonalTipoDocumento = "";
        }
        return auxInformacionPersonalTipoDocumento;
    }

    public String getAuxCargoDesempeñadoNombreCargo() {
        if (auxCargoDesempeñadoNombreCargo == null) {
            auxCargoDesempeñadoNombreCargo = "";
        }
        return auxCargoDesempeñadoNombreCargo;
    }

    public String getAuxCargoDesempeñadoMotivoCargo() {
        if (auxCargoDesempeñadoMotivoCargo == null) {
            auxCargoDesempeñadoMotivoCargo = "";
        }
        return auxCargoDesempeñadoMotivoCargo;
    }

    public String getAuxCargoDesempeñadoNombreEstructura() {
        if (auxCargoDesempeñadoNombreEstructura == null) {
            auxCargoDesempeñadoNombreEstructura = "";
        }
        return auxCargoDesempeñadoNombreEstructura;
    }

    public String getAuxCargoDesempeñadoPapel() {
        if (auxCargoDesempeñadoPapel == null) {
            auxCargoDesempeñadoPapel = "";
        }
        return auxCargoDesempeñadoPapel;
    }

    public String getAuxCargoDesempeñadoEmpleadoJefe() {
        if (auxCargoDesempeñadoEmpleadoJefe == null) {
            auxCargoDesempeñadoEmpleadoJefe = "";
        }
        return auxCargoDesempeñadoEmpleadoJefe;
    }

    public String getAuxCentroCostoMotivo() {
        if (auxCentroCostoMotivo == null) {
            auxCentroCostoMotivo = "";
        }
        return auxCentroCostoMotivo;
    }

    public String getAuxCentroCostoEstructura() {
        if (auxCentroCostoEstructura == null) {
            auxCentroCostoEstructura = "";
        }
        return auxCentroCostoEstructura;
    }

    public String getAuxTipoTrabajadorNombreTipoTrabajador() {
        if (auxTipoTrabajadorNombreTipoTrabajador == null) {
            auxTipoTrabajadorNombreTipoTrabajador = "";
        }
        return auxTipoTrabajadorNombreTipoTrabajador;
    }

    public String getAuxTipoSalarioReformaLaboral() {
        if (auxTipoSalarioReformaLaboral == null) {
            auxTipoSalarioReformaLaboral = "";
        }
        return auxTipoSalarioReformaLaboral;
    }

    public String getAuxSueldoMotivoSueldo() {
        if (auxSueldoMotivoSueldo == null) {
            auxSueldoMotivoSueldo = "";
        }
        return auxSueldoMotivoSueldo;
    }

    public String getAuxSueldoTipoSueldo() {
        if (auxSueldoTipoSueldo == null) {
            auxSueldoTipoSueldo = "";
        }
        return auxSueldoTipoSueldo;
    }

    public String getAuxSueldoUnidad() {
        if (auxSueldoUnidad == null) {
            auxSueldoUnidad = "";
        }
        return auxSueldoUnidad;
    }

    public String getAuxTipoContratoMotivo() {
        if (auxTipoContratoMotivo == null) {
            auxTipoContratoMotivo = "";
        }
        return auxTipoContratoMotivo;
    }

    public String getAuxTipoContratoTipoContrato() {
        if (auxTipoContratoTipoContrato == null) {
            auxTipoContratoTipoContrato = "";
        }
        return auxTipoContratoTipoContrato;
    }

    public String getAuxNormaLaboralNorma() {
        if (auxNormaLaboralNorma == null) {
            auxNormaLaboralNorma = "";
        }
        return auxNormaLaboralNorma;
    }

    public String getAuxLegislacionLaboralNombre() {
        if (auxLegislacionLaboralNombre == null) {
            auxLegislacionLaboralNombre = "";
        }
        return auxLegislacionLaboralNombre;
    }

    public String getAuxUbicacionGeograficaUbicacion() {
        if (auxUbicacionGeograficaUbicacion == null) {
            auxUbicacionGeograficaUbicacion = "";
        }
        return auxUbicacionGeograficaUbicacion;
    }

    public String getAuxJornadaLaboralJornada() {
        if (auxJornadaLaboralJornada == null) {
            auxJornadaLaboralJornada = "";
        }
        return auxJornadaLaboralJornada;
    }

    public String getAuxFormaPagoPeriodicidad() {
        if (auxFormaPagoPeriodicidad == null) {
            auxFormaPagoPeriodicidad = "";
        }
        return auxFormaPagoPeriodicidad;
    }

    public String getAuxFormaPagoSucursal() {
        if (auxFormaPagoSucursal == null) {
            auxFormaPagoSucursal = "";
        }
        return auxFormaPagoSucursal;
    }

    public String getAuxFormaPagoMetodo() {
        if (auxFormaPagoMetodo == null) {
            auxFormaPagoMetodo = "";
        }
        return auxFormaPagoMetodo;
    }

    public String getAuxAfiliacionEPS() {
        if (auxAfiliacionEPS == null) {
            auxAfiliacionEPS = "";
        }
        return auxAfiliacionEPS;
    }

    public String getAuxAfiliacionAFP() {
        if (auxAfiliacionAFP == null) {
            auxAfiliacionAFP = "";
        }
        return auxAfiliacionAFP;
    }

    public String getAuxAfiliacionFondo() {
        if (auxAfiliacionFondo == null) {
            auxAfiliacionFondo = "";
        }
        return auxAfiliacionFondo;
    }

    public String getAuxAfiliacionARP() {
        if (auxAfiliacionARP == null) {
            auxAfiliacionARP = "";
        }
        return auxAfiliacionARP;
    }

    public String getAuxAfiliacionCaja() {
        if (auxAfiliacionCaja == null) {
            auxAfiliacionCaja = "";
        }
        return auxAfiliacionCaja;
    }

    public String getAuxEstadoCivilEstado() {
        if (auxEstadoCivilEstado == null) {
            auxEstadoCivilEstado = "";
        }
        return auxEstadoCivilEstado;
    }

    public String getAuxDireccionCiudad() {
        if (auxDireccionCiudad == null) {
            auxDireccionCiudad = "";
        }
        return auxDireccionCiudad;
    }

    public String getAuxTelefonoTipo() {
        if (auxTelefonoTipo == null) {
            auxTelefonoTipo = "";
        }
        return auxTelefonoTipo;
    }

    public String getAuxTelefonoCiudad() {
        if (auxTelefonoCiudad == null) {
            auxTelefonoCiudad = "";
        }
        return auxTelefonoCiudad;
    }

}
