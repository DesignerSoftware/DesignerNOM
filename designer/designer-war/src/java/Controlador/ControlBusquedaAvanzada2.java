package Controlador;


import ClasesAyuda.ColumnasBusquedaAvanzada;
import ClasesAyuda.ParametrosQueryBusquedaAvanzada;
import Entidades.Cargos;
import Entidades.CentrosCostos;
import Entidades.Ciudades;
import Entidades.ColumnasEscenarios;
import Entidades.Contratos;
import Entidades.Cursos;
import Entidades.Empleados;
import Entidades.EstadosAfiliaciones;
import Entidades.EstadosCiviles;
import Entidades.Estructuras;
import Entidades.HvExperienciasLaborales;
import Entidades.Idiomas;
import Entidades.IdiomasPersonas;
import Entidades.Indicadores;
import Entidades.Instituciones;
import Entidades.JornadasLaborales;
import Entidades.MotivosCambiosCargos;
import Entidades.MotivosCambiosSueldos;
import Entidades.MotivosContratos;
import Entidades.MotivosLocalizaciones;
import Entidades.MotivosRetiros;
import Entidades.Motivosmvrs;
import Entidades.Mvrs;
import Entidades.NormasLaborales;
import Entidades.Papeles;
import Entidades.Periodicidades;
import Entidades.Personas;
import Entidades.Profesiones;
import Entidades.Proyectos;
import Entidades.PryRoles;
import Entidades.QVWEmpleadosCorte;
import Entidades.ReformasLaborales;
import Entidades.ResultadoBusquedaAvanzada;
import Entidades.SectoresEconomicos;
import Entidades.Sucursales;
import Entidades.TercerosSucursales;
import Entidades.TiposContratos;
import Entidades.TiposEntidades;
import Entidades.TiposIndicadores;
import Entidades.TiposSueldos;
import Entidades.TiposTrabajadores;
import Entidades.UbicacionesGeograficas;
import Entidades.VigenciasAfiliaciones;
import Entidades.VigenciasCargos;
import Entidades.VigenciasContratos;
import Entidades.VigenciasFormales;
import Entidades.VigenciasFormasPagos;
import Entidades.VigenciasIndicadores;
import Entidades.VigenciasJornadas;
import Entidades.VigenciasLocalizaciones;
import Entidades.VigenciasNoFormales;
import Entidades.VigenciasNormasEmpleados;
import Entidades.VigenciasProyectos;
import Entidades.VigenciasReformasLaborales;
import Entidades.VigenciasSueldos;
import Entidades.VigenciasTiposContratos;
import Entidades.VigenciasTiposTrabajadores;
import Entidades.VigenciasUbicaciones;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarBusquedaAvanzadaInterface2;
import InterfaceAdministrar.AdministrarEmplMvrsInterface;
import InterfaceAdministrar.AdministrarEmplVigenciaIndicadorInterface;
import InterfaceAdministrar.AdministrarEmplVigenciasFormasPagosInterface;
import InterfaceAdministrar.AdministrarEmpleadoIndividualInterface;
import InterfaceAdministrar.AdministrarEstadosCivilesInterface;
import InterfaceAdministrar.AdministrarIdiomaPersonaInterface;
import InterfaceAdministrar.AdministrarPerExperienciaLaboralInterface;
import InterfaceAdministrar.AdministrarVigenciaLocalizacionInterface;
import InterfaceAdministrar.AdministrarVigenciaNormaLaboralInterface;
import InterfaceAdministrar.AdministrarVigenciasAfiliaciones3Interface;
import InterfaceAdministrar.AdministrarVigenciasCargosBusquedaAvanzadaInterface;
import InterfaceAdministrar.AdministrarVigenciasContratosInterface;
import InterfaceAdministrar.AdministrarVigenciasFormalesInterface;
import InterfaceAdministrar.AdministrarVigenciasJornadasInterface;
import InterfaceAdministrar.AdministrarVigenciasNoFormalesInterface;
import InterfaceAdministrar.AdministrarVigenciasProyectosInterface;
import InterfaceAdministrar.AdministrarVigenciasReformasLaboralesInterface;
import InterfaceAdministrar.AdministrarVigenciasSueldosInterface;
import InterfaceAdministrar.AdministrarVigenciasTiposContratosInterface;
import InterfaceAdministrar.AdministrarVigenciasTiposTrabajadoresInterface;
import InterfaceAdministrar.AdministrarVigenciasUbicacionesInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.primefaces.component.column.Column;
import org.primefaces.component.columns.Columns;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.export.Exporter;
import org.primefaces.component.scrollpanel.ScrollPanel;
import org.primefaces.context.RequestContext;

/**
 *
 * @author PROYECTO01
 */
@ManagedBean
@SessionScoped
public class ControlBusquedaAvanzada2 implements Serializable {

    /*
     * MODULO DE BUSQUEDA AVANZADA NOMINA
     */
    //Inyeccion EJB Administrar de cada modulo
    @EJB
    AdministrarVigenciasCargosBusquedaAvanzadaInterface administrarVigenciaCargoBusquedaAvanzada;
    @EJB
    AdministrarVigenciaLocalizacionInterface administrarVigenciaLocalizacion;
    @EJB
    AdministrarVigenciasSueldosInterface administrarVigenciaSueldo;
    @EJB
    AdministrarVigenciasTiposContratosInterface administrarVigenciaTipoContrato;
    @EJB
    AdministrarVigenciasTiposTrabajadoresInterface administrarVigenciasTiposTrabajadores;
    @EJB
    AdministrarVigenciasReformasLaboralesInterface administrarVigenciaReformaLaboral;
    @EJB
    AdministrarVigenciaNormaLaboralInterface administrarVigenciaNormaEmpleado;
    @EJB
    AdministrarVigenciasContratosInterface administrarVigenciaContrato;
    @EJB
    AdministrarVigenciasUbicacionesInterface administrarVigenciasUbicaciones;
    @EJB
    AdministrarVigenciasAfiliaciones3Interface administrarVigenciaAfiliacion3;
    @EJB
    AdministrarEmplVigenciasFormasPagosInterface administrarEmplVigenciaFormaPago;
    @EJB
    AdministrarEmplMvrsInterface administrarEmplMvrs;
    @EJB
    AdministrarVigenciasJornadasInterface administrarVigenciaJornada;
    //
    @EJB
    AdministrarBusquedaAvanzadaInterface2 administrarBusquedaAvanzada;
    //Objetos para realizar el proceso de busqueda avanzada
    //Modulo Cargos
    private VigenciasCargos vigenciaCargoBA;
    private int tabActivaCargos;
    private int casillaVigenciaCargo;
    private boolean permitirIndexVigenciaCargo;
    private Date fechaInicialCargo, fechaFinalCargo;
    private Date auxFechaInicialCargo, auxFechaFinalCargo;
    private int tipoFechaCargo;
    //Modulo Centro de Costo
    private VigenciasLocalizaciones vigenciaLocalizacionBA;
    private int tabActivaCentroCosto;
    private int casillaVigenciaLocalizacion;
    private boolean permitirIndexVigenciaLocalizacion;
    private Date fechaInicialCentroCosto, fechaFinalCentroCosto;
    private Date auxFechaInicialCentroCosto, auxFechaFinalCentroCosto;
    private int tipoFechaCentroCosto;
    //Modulo Sueldos
    private VigenciasSueldos vigenciaSueldoBA;
    private int tabActivaSueldo;
    private int casillaVigenciaSueldo;
    private boolean permitirIndexVigenciaSueldo;
    private Date fechaInicialSueldo, fechaFinalSueldo;
    private Date auxFechaInicialSueldo, auxFechaFinalSueldo;
    private int tipoFechaSueldo;
    //Modulo Fecha Contrato
    private VigenciasTiposContratos vigenciaTipoContratoBA;
    private int tabActivaFechaContrato;
    private int casillaVigenciaTipoContrato;
    private boolean permitirIndexVigenciaTipoContrato;
    private Date fechaInicialFechaContrato, fechaFinalFechaContrato;
    private Date auxFechaInicialFechaContrato, auxFechaFinalFechaContrato;
    private int tipoFechaFechaContrato;
    //Modulo Tipo de Trabajador
    private VigenciasTiposTrabajadores vigenciaTipoTrabajadorBA;
    private int tabActivaTipoTrabajador;
    private int casillaVigenciaTipoTrabajador;
    private boolean permitirIndexVigenciaTipoTrabajador;
    private Date fechaInicialTipoTrabajador, fechaFinalTipoTrabajador;
    private Date auxFechaInicialTipoTrabajador, auxFechaFinalTipoTrabajador;
    private int tipoFechaTipoTrabajador;
    //Modulo Tipo de Salario
    private VigenciasReformasLaborales vigenciaReformaLaboralBA;
    private int tabActivaTipoSalario;
    private int casillaVigenciaReformaLaboral;
    private boolean permitirIndexVigenciaReformaLaboral;
    private Date fechaInicialTipoSalario, fechaFinalTipoSalario;
    private Date auxFechaInicialTipoSalario, auxFechaFinalTipoSalario;
    private int tipoFechaTipoSalario;
    //Modulo Norma Laboral
    private VigenciasNormasEmpleados vigenciaNormaEmpleadoBA;
    private int tabActivaNormaLaboral;
    private int casillaVigenciaNormaLaboral;
    private boolean permitirIndexVigenciaNormaEmpleado;
    private Date fechaInicialNormaLaboral, fechaFinalNormaLaboral;
    private Date auxFechaInicialNormaLaboral, auxFechaFinalNormaLaboral;
    private int tipoFechaNormaLaboral;
    //Modulo Legislacion Laboral
    private VigenciasContratos vigenciaContratoBA;
    private int tabActivaLegislacionLaboral;
    private int casillaVigenciaContrato;
    private boolean permitirIndexVigenciaContrato;
    private Date fechaMIInicialLegislacionLaboral, fechaMIFinalLegislacionLaboral;
    private Date fechaMFInicialLegislacionLaboral, fechaMFFinalLegislacionLaboral;
    private Date auxFechaMIInicialLegislacionLaboral, auxFechaMIFinalLegislacionLaboral;
    private Date auxFechaMFInicialLegislacionLaboral, auxFechaMFFinalLegislacionLaboral;
    private int tipoFechaLegislacionLaboral;
    //Modulo Ubicacion Geografica
    private VigenciasUbicaciones vigenciaUbicacionBA;
    private int tabActivaUbicacion;
    private int casillaVigenciaUbicacion;
    private boolean permitirIndexVigenciaUbicacion;
    private Date fechaInicialUbicacion, fechaFinalUbicacion;
    private Date auxFechaInicialUbicacion, auxFechaFinalUbicacion;
    private int tipoFechaUbicacion;
    //Modulo Afiliaciones
    private VigenciasAfiliaciones vigenciaAfiliacionBA;
    private int tabActivaAfiliacion;
    private int casillaVigenciaAfiliacion;
    private boolean permitirIndexVigenciaAfiliacion;
    private Date fechaInicialAfiliacion, fechaFinalAfiliacion;
    private Date auxFechaInicialAfiliacion, auxFechaFinalAfiliacion;
    private int tipoFechaAfiliacion;
    //Modulo Forma Pago
    private VigenciasFormasPagos vigenciaFormaPagoBA;
    private int tabActivaFormaPago;
    private int casillaVigenciaFormaPago;
    private boolean permitirIndexVigenciaFormaPago;
    private Date fechaInicialFormaPago, fechaFinalFormaPago;
    private Date auxFechaInicialFormaPago, auxFechaFinalFormaPago;
    private int tipoFechaFormaPago;
    //Modulo Menor Valor Retencion (MVR)
    private Mvrs mvrsBA;
    private int tabActivaMvrs;
    private int casillaMvrs;
    private boolean permitirIndexMvrs;
    private Date fechaInicialMvrs, fechaFinalMvrs;
    private Date auxFechaInicialMvrs, auxFechaFinalMvrs;
    private int tipoFechaMvrs;
    //Modulo Sets
    private int tabActivaSets;
    private int casillaSets;
    private Date fechaMIInicialSets, fechaMIFinalSets;
    private Date fechaMFInicialSets, fechaMFFinalSets;
    private Date auxFechaMFInicialSets, auxFechaMFFinalSets;
    private Date auxFechaMIInicialSets, auxFechaMIFinalSets;
    private int tipoFechaSets;
    //Modulo Vacaciones
    private int tabActivaVacacion;
    private int casillaVacacion;
    private Date fechaMIInicialVacacion, fechaMIFinalVacacion;
    private Date fechaMFInicialVacacion, fechaMFFinalVacacion;
    private Date auxFechaMFInicialVacacion, auxFechaMFFinalVacacion;
    private Date auxFechaMIInicialVacacion, auxFechaMIFinalVacacion;
    //Modulo Norma Laboral
    private VigenciasJornadas vigenciaJornadaBA;
    private int tabActivaJornadaLaboral;
    private int casillaVigenciaJornadaLaboral;
    private boolean permitirIndexVigenciaJornada;
    private Date fechaInicialJornadaLaboral, fechaFinalJornadaLaboral;
    private Date auxFechaInicialJornadaLaboral, auxFechaFinalJornadaLaboral;
    //Modulo Fecha Retiro
    private MotivosRetiros motivoRetiroBA;
    private int tabActivaFechaRetiro;
    private int casillaMotivoRetiro;
    private boolean permitirIndexMotivoRetiro;
    private Date fechaInicialFechaRetiro, fechaFinalFechaRetiro;
    private Date auxFechaInicialFechaRetiro, auxFechaFinalFechaRetiro;
    //LOVS 
    //Motivos Retiros
    private List<MotivosRetiros> lovMotivosRetiros;
    private List<MotivosRetiros> filtrarLovMotivosRetiros;
    private MotivosRetiros motivoRetiroSeleccionado;
    //Jornadas Laborales
    private List<JornadasLaborales> lovJornadasLaborales;
    private List<JornadasLaborales> filtrarLovJornadasLaborales;
    private JornadasLaborales jornadaLaboralSeleccionada;
    //Motivos Mvrs
    private List<Motivosmvrs> lovMotivosMvrs;
    private List<Motivosmvrs> filtrarLovMotivosMvrs;
    private Motivosmvrs motivoMvrsSeleccionado;
    //Formas Pagos
    private List<Periodicidades> lovPeriodicidades;
    private List<Periodicidades> filtrarLovPeriodicidades;
    private Periodicidades periodicidadSeleccionada;
    //Sucursales
    private List<Sucursales> lovSucursales;
    private List<Sucursales> filtrarLovSucursales;
    private Sucursales sucursalSeleccionada;
    //Terceros
    private List<TercerosSucursales> lovTercerosSucursales;
    private List<TercerosSucursales> filtrarLovTercerosSucursales;
    private TercerosSucursales terceroSucursalSeleccionado;
    //Tipos Entidades
    private List<TiposEntidades> lovTiposEntidades;
    private List<TiposEntidades> filtrarLovTiposEntidades;
    private TiposEntidades tipoEntidadSeleccionado;
    //Estados Afiliaciones
    private List<EstadosAfiliaciones> lovEstadosAfiliaciones;
    private List<EstadosAfiliaciones> filtrarLovEstadosAfiliaciones;
    private EstadosAfiliaciones estadoAfiliacionSeleccionado;
    //Ciudades
    private List<UbicacionesGeograficas> lovUbicacionesGeograficas;
    private List<UbicacionesGeograficas> filtrarLovUbicacionesGeograficas;
    private UbicacionesGeograficas ubicacionGeograficaSeleccionada;
    //Contratos
    private List<Contratos> lovContratos;
    private List<Contratos> filtrarLovContratos;
    private Contratos contratoSeleccionado;
    //Normas Laborales
    private List<NormasLaborales> lovNormasLaborales;
    private List<NormasLaborales> filtrarLovNormasLaborales;
    private NormasLaborales normaLaboralSeleccionada;
    //Reformas Laborales
    private List<ReformasLaborales> lovReformasLaborales;
    private List<ReformasLaborales> filtrarLovReformasLaborales;
    private ReformasLaborales reformaLaboralSeleccionada;
    //Tipos Contratos
    private List<TiposTrabajadores> lovTiposTrabajadores;
    private List<TiposTrabajadores> filtrarLovTiposTrabajadores;
    private TiposTrabajadores tipoTrabajadorSeleccionado;
    //Tipos Contratos
    private List<TiposContratos> lovTiposContratos;
    private List<TiposContratos> filtrarLovTiposContratos;
    private TiposContratos tipoContratoSeleccionado;
    //Motivos Contratos
    private List<MotivosContratos> lovMotivosContratos;
    private List<MotivosContratos> filtrarLovMotivosContratos;
    private MotivosContratos motivoContratoSeleccionado;
    //Tipos Sueldos
    private List<TiposSueldos> lovTiposSueldos;
    private List<TiposSueldos> filtrarLovTiposSueldos;
    private TiposSueldos tipoSueldoSeleccionado;
    //Motivos Cambios Sueldos
    private List<MotivosCambiosSueldos> lovMotivosCambiosSueldos;
    private List<MotivosCambiosSueldos> filtrarLovMotivosCambiosSueldos;
    private MotivosCambiosSueldos motivoSueldoSeleccionado;
    //Motivos Localizaciones
    private List<MotivosLocalizaciones> lovMotivosLocalizaciones;
    private List<MotivosLocalizaciones> filtrarLovMotivosLocalizaciones;
    private MotivosLocalizaciones motivoLocalizacionSeleccionado;
    //Estructuras
    private List<Estructuras> lovEstructuras;
    private List<Estructuras> filtrarLovEstructuras;
    private Estructuras estructuraSeleccionada;
    //Cargos
    private List<Cargos> lovCargos;
    private List<Cargos> filtrarLovCargos;
    private Cargos cargoSeleccionado;
    //Papeles
    private List<Papeles> lovPapeles;
    private List<Papeles> filtrarLovPapeles;
    private Papeles papelSeleccionado;
    //MotivosCambiosCargos
    private List<MotivosCambiosCargos> lovMotivosCambiosCargos;
    private List<MotivosCambiosCargos> filtrarLovMotivosCambiosCargos;
    private MotivosCambiosCargos motivoCambioSeleccionado;
    //Empleado Jefe
    private List<Empleados> lovEmpleados;
    private List<Empleados> filtrarLovEmpleados;
    private Empleados empleadoSeleccionado;
    ////////
    private String auxEstructuraVigenciaCargo, auxCargoVigenciaCargo, auxPapelVigenciaCargo, auxMotivoCambioCargoVigenciaCargo, auxJefeVigenciaCargo;
    private String auxLocalizacionVigenciaLocalizacion, auxMotivoLocalizacionVigenciaLocalizacion;
    private String auxTipoSueldoVigenciaSueldo, auxMotivoCambioSueldoVigenciaSueldo;
    private BigDecimal auxSueldoMinimoSueldo, auxSueldoMaximoSueldo;
    private String auxTipoContratoVigenciaTipoContrato, auxMotivoContratoVigenciaTipoContrato;
    private String auxTipoTrabajadorVigenciaTipoTrabajador;
    private String auxReformaLaboralVigenciaReformaLaboral;
    private String auxNormaLaboralVigenciaNormaEmpleado;
    private String auxContratoVigenciaContrato;
    private String auxUbicacionVigenciaUbicacion;
    private String auxTerceroVigenciaAfiliacion3, auxTipoEntidadVigenciaAfiliacion3, auxEstadoVigenciaAfiliacion3;
    private String auxPeriodicidadVigenciaFormaPago, auxSucursalVigenciaFormaPago;
    private String auxMotivoMvrs;
    private BigDecimal auxSueldoMinimoMvrs, auxSueldoMaximoMvrs;
    private BigDecimal auxPromedioMinimoSets, auxPromedioMaximoSets;
    private String auxJornadaJornadaLaboral;
    private String auxMotivoMotivoRetiro;
    //Otros
    private boolean activoFechasCargos, activoFechasCentroCosto, activoFechasSueldo, activoFechasFechaContrato, activoFechasTipoTrabajador;
    private boolean activoFechasTipoSalario, activoFechasNormaLaboral, activoFechasLegislacionLaboral, activoFechasUbicacion;
    private boolean activoFechasAfiliacion, activoFechasFormaPago, activoFechasMvrs, activoFechasSets;
    private BigDecimal sueldoMaxSueldo, sueldoMinSueldo;
    private BigDecimal sueldoMaxMvrs, sueldoMinMvrs;
    private BigDecimal promedioMinimoSets, promedioMaximoSets;
    private String tipoMetodoSets;
    /*
     * MODULO DE BUSQUEDA AVANZADA NOMINA
     */
    /*
     * MODULO DE BUSQUEDA AVANZADA PERSONAL
     */
    //Inyeccion EJB Para Busqueda Avanzada Personal
    @EJB
    AdministrarEmpleadoIndividualInterface administrarEmpleadoIndividual;
    @EJB
    AdministrarEstadosCivilesInterface administrarEstadosCiviles;
    @EJB
    AdministrarIdiomaPersonaInterface administrarIdiomasPersonas;
    @EJB
    AdministrarEmplVigenciaIndicadorInterface administrarEmplVigenciaIndicador;
    @EJB
    AdministrarVigenciasNoFormalesInterface administrarVigenciasNoFormales;
    @EJB
    AdministrarVigenciasFormalesInterface administrarVigenciasFormales;
    @EJB
    AdministrarPerExperienciaLaboralInterface administrarPerExperienciaLaboral;
    @EJB
    AdministrarVigenciasProyectosInterface administrarVigenciasProyectos;
    //Objetos para realizar el proceso de busqueda avanzada
    //Modulo Datos Personales
    private Empleados empleadoBA;
    private int tabActivaDatosPersonales;
    private int casillaEmpleado;
    private boolean permitirIndexEmpleado;
    private Date fechaInicialDatosPersonales, fechaFinalDatosPersonales;
    private Date auxFechaInicialDatosPersonales, auxFechaFinalDatosPersonales;
    //Modulo Factor RH
    private int tabActivaFactorRH;
    //Modulo Estado Civil
    private EstadosCiviles estadoCivilBA;
    private int tabActivaEstadoCivil;
    private int casillaEstadoCivil;
    private boolean permitirIndexEstadoCivil;
    private Date fechaInicialEstadoCivil, fechaFinalEstadoCivil;
    private Date auxFechaInicialEstadoCivil, auxFechaFinalEstadoCivil;
    private int tipoFechaEstadoCivil;
    //Modulo Idioma
    private IdiomasPersonas idiomaPersonaBA;
    private int tabActivaIdioma;
    private int casillaIdiomaPersona;
    private boolean permitirIndexIdiomaPersona;
    //Modulo Censo
    private VigenciasIndicadores vigenciaIndicadorBA;
    private int tabActivaCenso;
    private int casillaVigenciaIndicador;
    private boolean permitirIndexVigenciaIndicador;
    private Date fechaInicialCenso, fechaFinalCenso;
    private Date auxFechaInicialCenso, auxFechaFinalCenso;
    private int tipoFechaCenso;
    //Modulo Educacion Formal
    private VigenciasFormales vigenciaFormalBA;
    private int tabActivaEducacionFormal;
    private int casillaVigenciaFormal;
    private boolean permitirIndexVigenciaFormal;
    private Date fechaInicialEducacionFormal, fechaFinalEducacionFormal;
    private Date auxFechaInicialEducacionFormal, auxFechaFinalEducacionFormal;
    private int tipoFechaEducacionFormal;
    //Modulo Educacion No Formal
    private VigenciasNoFormales vigenciaNoFormalBA;
    private int tabActivaEducacionNoFormal;
    private int casillaVigenciaNoFormal;
    private boolean permitirIndexVigenciaNoFormal;
    private Date fechaInicialEducacionNoFormal, fechaFinalEducacionNoFormal;
    private Date auxFechaInicialEducacionNoFormal, auxFechaFinalEducacionNoFormal;
    private int tipoFechaEducacionNoFormal;
    //Modulo Experiencia Laboral
    private HvExperienciasLaborales hvExperienciaLaboralBA;
    private int tabActivaExperienciaLaboral;
    private int casillaHvExperienciaLaboral;
    private boolean permitirIndexHvExperienciaLaboral;
    private Date fechaInicialExperienciaLaboral, fechaFinalExperienciaLaboral;
    private Date auxFechaInicialExperienciaLaboral, auxFechaFinalExperienciaLaboral;
    //Modulo Proyecto
    private VigenciasProyectos vigenciaProyectoBA;
    private int tabActivaProyecto;
    private int casillaVigenciaProyecto;
    private boolean permitirIndexVigenciaProyecto;
    private Date fechaInicialProyecto, fechaFinalProyecto;
    private Date auxFechaInicialProyecto, auxFechaFinalProyecto;
    //Modulo Cargo A Postularse
    private VigenciasCargos vigenciaCargoPersonalBA;
    private int tabActivaCargoPostularse;
    private int casillaVigenciaCargoPersonal;
    private boolean permitirIndexVigenciaCargoPersonal;
    //LOVS
    //PryRoles
    private List<PryRoles> lovPryRoles;
    private List<PryRoles> filtrarLovPryRoles;
    private PryRoles pryRolSeleccionado;
    //Proyectos
    private List<Proyectos> lovProyectos;
    private List<Proyectos> filtrarLovProyectos;
    private Proyectos proyectoSeleccionado;
    //Sectores Economicos
    private List<SectoresEconomicos> lovSectoresEconomicos;
    private List<SectoresEconomicos> filtrarLovSectoresEconomicos;
    private SectoresEconomicos sectorEconomicoSeleccionado;
    //Instituciones
    private List<Instituciones> lovInstituciones;
    private List<Instituciones> filtrarLovInstituciones;
    private Instituciones institucionSeleccionada;
    //Profesiones
    private List<Profesiones> lovProfesiones;
    private List<Profesiones> filtrarLovProfesiones;
    private Profesiones profesionSeleccionada;
    //Cursos
    private List<Cursos> lovCursos;
    private List<Cursos> filtrarLovCursos;
    private Cursos cursoSeleccionado;
    //Tipos Indicadores
    private List<TiposIndicadores> lovTiposIndicadores;
    private List<TiposIndicadores> filtrarLovTiposIndicadores;
    private TiposIndicadores tipoIndicadorSeleccionado;
    //Indicadores
    private List<Indicadores> lovIndicadores;
    private List<Indicadores> filtrarLovIndicadores;
    private Indicadores indicadorSeleccionado;
    //Idiomas
    private List<Idiomas> lovIdiomas;
    private List<Idiomas> filtrarLovIdiomas;
    private Idiomas idiomaSeleccionado;
    //Ciudades
    private List<Ciudades> lovCiudades;
    private List<Ciudades> filtrarLovCiudades;
    private Ciudades ciudadSeleccionada;
    //Estados Civiles
    private List<EstadosCiviles> lovEstadosCiviles;
    private List<EstadosCiviles> filtrarLovEstadosCiviles;
    private EstadosCiviles estadoCivilSeleccionado;
    //
    private boolean activoFechasEstadoCivil;
    private boolean activoFechasCenso;
    private boolean activoFechasEducacionFormal;
    private boolean activoFechasEducacionNoFormal;
    ////////
    private String auxCiudadDocumentoEmpleado, auxCiudadNacimientoEmpleado;
    private String auxEstadoCivilEstadoCivil;
    private String auxIdiomaIdiomaPersona;
    private BigInteger auxConversacionDesde, auxConversacionHasta, auxLecturaDesde, auxLecturaHasta, auxEscrituraDesde, auxEscrituraHasta;
    private String auxTipoIndicadorCenso, auxIndicadorCenso;
    private String auxInstitucionEducacionFormal, auxProfesionEducacionFormal;
    private String auxInstitucionEducacionNoFormal, auxCursoEducacionNoFormal;
    private String auxMotivoRetiroExperienciaLaboral, auxSectorEconomicoExperienciaLaboral;
    private String auxRolProyecto, auxProyectoProyecto;
    private String auxCargoCargoPostularse;
    //Otros
    private BigInteger conversacionDesde, conversacionHasta, lecturaDesde, lecturaHasta, escrituraDesde, escrituraHasta;
    private String desarrolladoEducacionFormal;
    private String desarrolladoEducacionNoFormal;
    private String empresaExperienciaLaboral, cargoExperienciaLaboral;
    /*
     * MODULO DE BUSQUEDA AVANZADA PERSONAL
     */
    /*
     * VARIABLES TABLA DINAMICA
     */
    private boolean aceptar;
    private Date fechaParametro;
    //Resulatos de busqueda
    private final static List<String> VALID_COLUMN_KEYS = Arrays.asList("codigo", "primeroApellido", "segundoApellido", "nombre", "columna0", "columna1", "columna2", "columna3", "columna4", "columna5", "columna6", "columna7", "columna8", "columna9");
    //Nombres de los valores en la clase que tomaran lo valores correspondientes de las columnas que se desean agregar
    private String columnTemplate;
    //Valores que seran mostrados en los header de cada columna
    private Map<String, String> mapValoresColumnas;
    //Cargue de los valores static de los arreglos color y manufactura
    private List<ResultadoBusquedaAvanzada> filteredListaResultadoBusquedaAvanzada;
    //Lista de filtrado
    private List<ResultadoBusquedaAvanzada> listaResultadoBusquedaAvanzada;
    //Lista de carros que sera cargada por la tabla
    private List<ColumnModel> columns = new ArrayList<ColumnModel>();
    //Lista de las columnas que seran agregadas a la tabla
    private String nuevaColumna;
    // 
    private List<ParametrosQueryBusquedaAvanzada> listaParametrosQueryModulos;
    //
    private List<Empleados> listaEmpleadosResultados;
    //
    private boolean auxTabActivoCentroCosto;
    //
    private List<ColumnasEscenarios> listaColumnasEscenarios;
    //
    private List<ColumnasBusquedaAvanzada> listaColumnasBusquedaAvanzada;
    private List<ColumnasBusquedaAvanzada> filtrarListaColumnasBusquedaAvanzada;
    private ColumnasBusquedaAvanzada columnaSeleccionada;
    //PRUEBA
    List<BigInteger> listaCodigosEmpleado;
    List<QVWEmpleadosCorte> registros;
    List<QVWEmpleadosCorte> registrosFiltrado;
    private int indice, cualCelda;
    private String cabeceraEditarCelda, infoVariableEditarCelda;
    /*
     * VARIABLES TABLA DINAMICA
     */
    private int numeroTipoBusqueda;
    private ScrollPanel scrollPanelNomina, scrollPanelPersonal;
    private int tipoLista;
    private int bandera;
    private Columns columnasDinamicas;
    private String altoTabla;
    private String visibilidadNomina, visibilidadPersonal;
    private String displayNomina, displayPersonal;

    public ControlBusquedaAvanzada2() {
        displayNomina = "";
        displayPersonal = "";
        visibilidadPersonal = "";
        visibilidadNomina = "";
        altoTabla = "80";
        bandera = 0;
        tipoLista = 0;
        numeroTipoBusqueda = 0;
        cabeceraEditarCelda = "";
        infoVariableEditarCelda = "";
        indice = -1;
        cualCelda = -1;
        listaColumnasBusquedaAvanzada = new ArrayList<ColumnasBusquedaAvanzada>();
        registros = new ArrayList<QVWEmpleadosCorte>();
        listaColumnasEscenarios = null;
        auxTabActivoCentroCosto = false;
        //
        listaEmpleadosResultados = new ArrayList<Empleados>();
        listaParametrosQueryModulos = new ArrayList<ParametrosQueryBusquedaAvanzada>();
        //Fechas Modulos
        //PERSONAL
        auxFechaFinalDatosPersonales = null;
        auxFechaInicialDatosPersonales = null;
        auxFechaFinalEstadoCivil = null;
        auxFechaInicialEstadoCivil = null;
        auxFechaFinalCenso = null;
        auxFechaInicialCenso = null;
        auxFechaFinalEducacionFormal = null;
        auxFechaInicialEducacionFormal = null;
        auxFechaFinalEducacionNoFormal = null;
        auxFechaInicialEducacionNoFormal = null;
        auxFechaFinalExperienciaLaboral = null;
        auxFechaInicialExperienciaLaboral = null;
        auxFechaFinalProyecto = null;
        auxFechaInicialProyecto = null;
        //NOMINA
        auxFechaFinalCargo = null;
        auxFechaInicialCargo = null;
        auxFechaFinalCentroCosto = null;
        auxFechaInicialCentroCosto = null;
        auxFechaFinalSueldo = null;
        auxFechaInicialSueldo = null;
        auxFechaFinalFechaContrato = null;
        auxFechaInicialFechaContrato = null;
        auxFechaInicialTipoTrabajador = null;
        auxFechaFinalTipoTrabajador = null;
        auxFechaFinalTipoSalario = null;
        auxFechaInicialTipoSalario = null;
        auxFechaFinalNormaLaboral = null;
        auxFechaInicialNormaLaboral = null;
        auxFechaMIInicialLegislacionLaboral = null;
        auxFechaMIFinalLegislacionLaboral = null;
        auxFechaMFInicialLegislacionLaboral = null;
        auxFechaMFFinalLegislacionLaboral = null;
        auxFechaFinalUbicacion = null;
        auxFechaInicialUbicacion = null;
        auxFechaInicialAfiliacion = null;
        auxFechaFinalAfiliacion = null;
        auxFechaFinalFormaPago = null;
        auxFechaInicialFormaPago = null;
        auxFechaInicialMvrs = null;
        auxFechaFinalMvrs = null;
        auxFechaMFFinalSets = null;
        auxFechaMFInicialSets = null;
        auxFechaMIFinalSets = null;
        auxFechaMIInicialSets = null;
        auxFechaMIFinalVacacion = null;
        auxFechaMIInicialVacacion = null;
        auxFechaMFFinalVacacion = null;
        auxFechaMFInicialVacacion = null;
        auxFechaInicialJornadaLaboral = null;
        auxFechaFinalJornadaLaboral = null;
        auxFechaFinalFechaRetiro = null;
        auxFechaInicialFechaRetiro = null;
        //Tipo Fecha
        //PERSONAL
        tipoFechaEstadoCivil = 1;
        tipoFechaCenso = 1;
        tipoFechaEducacionFormal = 1;
        tipoFechaEducacionNoFormal = 1;
        //NOMINA
        tipoFechaCargo = 1;
        tipoFechaCentroCosto = 1;
        tipoFechaSueldo = 1;
        tipoFechaFechaContrato = 1;
        tipoFechaTipoTrabajador = 1;
        tipoFechaTipoSalario = 1;
        tipoFechaNormaLaboral = 1;
        tipoFechaLegislacionLaboral = 1;
        tipoFechaUbicacion = 1;
        tipoFechaAfiliacion = 1;
        tipoFechaFormaPago = 1;
        tipoFechaMvrs = 1;
        tipoFechaSets = 1;
        //Activo Fechas
        //PERSONAL
        activoFechasEstadoCivil = true;
        activoFechasCenso = true;
        activoFechasEducacionFormal = true;
        activoFechasEducacionNoFormal = true;
        //NOMINA
        activoFechasCargos = true;
        activoFechasCentroCosto = true;
        activoFechasSueldo = true;
        activoFechasFechaContrato = true;
        activoFechasTipoTrabajador = true;
        activoFechasTipoSalario = true;
        activoFechasNormaLaboral = true;
        activoFechasLegislacionLaboral = true;
        activoFechasUbicacion = true;
        activoFechasAfiliacion = true;
        activoFechasFormaPago = true;
        activoFechasMvrs = true;
        activoFechasSets = true;
        //POSICION MODULOS
        //PERSONAL
        casillaEmpleado = -1;
        casillaEstadoCivil = -1;
        casillaIdiomaPersona = -1;
        casillaVigenciaIndicador = -1;
        casillaVigenciaFormal = -1;
        casillaVigenciaNoFormal = -1;
        casillaHvExperienciaLaboral = -1;
        casillaVigenciaProyecto = -1;
        casillaVigenciaCargoPersonal = -1;
        //NOMINA
        casillaVigenciaSueldo = -1;
        casillaVigenciaCargo = -1;
        casillaVigenciaLocalizacion = -1;
        casillaVigenciaTipoContrato = -1;
        casillaVigenciaTipoTrabajador = -1;
        casillaVigenciaTipoContrato = -1;
        casillaVigenciaNormaLaboral = -1;
        casillaVigenciaContrato = -1;
        casillaVigenciaUbicacion = -1;
        casillaVigenciaAfiliacion = -1;
        casillaVigenciaFormaPago = -1;
        casillaMvrs = -1;
        casillaSets = -1;
        casillaVacacion = -1;
        casillaVigenciaJornadaLaboral = -1;
        casillaMotivoRetiro = -1;
        //
        //PERSONAL
        tabActivaIdioma = 0;
        tabActivaEstadoCivil = 0;
        tabActivaDatosPersonales = 0;
        tabActivaFactorRH = 0;
        tabActivaCenso = 0;
        tabActivaEducacionFormal = 0;
        tabActivaEducacionNoFormal = 0;
        tabActivaExperienciaLaboral = 0;
        tabActivaProyecto = 0;
        tabActivaCargoPostularse = 0;
        //NOMINA
        auxTabActivoCentroCosto = false;
        tabActivaCentroCosto = 0;
        tabActivaCargos = 0;
        tabActivaSueldo = 0;
        tabActivaFechaContrato = 0;
        tabActivaTipoTrabajador = 0;
        tabActivaTipoSalario = 0;
        tabActivaNormaLaboral = 0;
        tabActivaLegislacionLaboral = 0;
        tabActivaUbicacion = 0;
        tabActivaAfiliacion = 0;
        tabActivaFormaPago = 0;
        tabActivaMvrs = 0;
        tabActivaSets = 0;
        tabActivaVacacion = 0;
        tabActivaJornadaLaboral = 0;
        tabActivaFechaRetiro = 0;
        //PERMITIR INDEX
        //PERSONAL
        permitirIndexEstadoCivil = true;
        permitirIndexEmpleado = true;
        permitirIndexIdiomaPersona = true;
        permitirIndexVigenciaIndicador = true;
        permitirIndexVigenciaFormal = true;
        permitirIndexVigenciaNoFormal = true;
        permitirIndexHvExperienciaLaboral = true;
        permitirIndexVigenciaProyecto = true;
        permitirIndexVigenciaCargoPersonal = true;
        //NOMINA
        permitirIndexVigenciaCargo = true;
        permitirIndexVigenciaLocalizacion = true;
        permitirIndexVigenciaSueldo = true;
        permitirIndexVigenciaTipoContrato = true;
        permitirIndexVigenciaTipoTrabajador = true;
        permitirIndexVigenciaReformaLaboral = true;
        permitirIndexVigenciaNormaEmpleado = true;
        permitirIndexVigenciaContrato = true;
        permitirIndexVigenciaUbicacion = true;
        permitirIndexVigenciaAfiliacion = true;
        permitirIndexVigenciaFormaPago = true;
        permitirIndexMvrs = true;
        permitirIndexVigenciaJornada = true;
        permitirIndexMotivoRetiro = true;
        //MODULOS
        //PERSONAL
        empleadoBA = new Empleados();
        empleadoBA.setPersona(new Personas());
        empleadoBA.getPersona().setCiudaddocumento(new Ciudades());
        empleadoBA.getPersona().setCiudadnacimiento(new Ciudades());
        estadoCivilBA = new EstadosCiviles();
        idiomaPersonaBA = new IdiomasPersonas();
        idiomaPersonaBA.setIdioma(new Idiomas());
        vigenciaIndicadorBA = new VigenciasIndicadores();
        vigenciaIndicadorBA.setTipoindicador(new TiposIndicadores());
        vigenciaIndicadorBA.setIndicador(new Indicadores());
        vigenciaFormalBA = new VigenciasFormales();
        vigenciaFormalBA.setInstitucion(new Instituciones());
        vigenciaFormalBA.setProfesion(new Profesiones());
        desarrolladoEducacionFormal = null;
        vigenciaNoFormalBA = new VigenciasNoFormales();
        vigenciaNoFormalBA.setInstitucion(new Instituciones());
        vigenciaNoFormalBA.setCurso(new Cursos());
        desarrolladoEducacionNoFormal = null;
        hvExperienciaLaboralBA = new HvExperienciasLaborales();
        hvExperienciaLaboralBA.setMotivoretiro(new MotivosRetiros());
        hvExperienciaLaboralBA.setSectoreconomico(new SectoresEconomicos());
        vigenciaProyectoBA = new VigenciasProyectos();
        vigenciaProyectoBA.setProyecto(new Proyectos());
        vigenciaProyectoBA.setPryRol(new PryRoles());
        vigenciaCargoPersonalBA = new VigenciasCargos();
        vigenciaCargoPersonalBA.setCargo(new Cargos());
        //NOMINA
        vigenciaCargoBA = new VigenciasCargos();
        vigenciaCargoBA.setEstructura(new Estructuras());
        vigenciaCargoBA.getEstructura().setCentrocosto(new CentrosCostos());
        vigenciaCargoBA.setMotivocambiocargo(new MotivosCambiosCargos());
        vigenciaCargoBA.setPapel(new Papeles());
        vigenciaCargoBA.setCargo(new Cargos());
        vigenciaCargoBA.setEmpleadojefe(new Empleados());
        vigenciaLocalizacionBA = new VigenciasLocalizaciones();
        vigenciaLocalizacionBA.setLocalizacion(new Estructuras());
        vigenciaLocalizacionBA.setMotivo(new MotivosLocalizaciones());
        vigenciaSueldoBA = new VigenciasSueldos();
        vigenciaSueldoBA.setTiposueldo(new TiposSueldos());
        vigenciaSueldoBA.setMotivocambiosueldo(new MotivosCambiosSueldos());
        vigenciaTipoContratoBA = new VigenciasTiposContratos();
        vigenciaTipoContratoBA.setTipocontrato(new TiposContratos());
        vigenciaTipoContratoBA.setMotivocontrato(new MotivosContratos());
        vigenciaTipoTrabajadorBA = new VigenciasTiposTrabajadores();
        vigenciaTipoTrabajadorBA.setTipotrabajador(new TiposTrabajadores());
        vigenciaReformaLaboralBA = new VigenciasReformasLaborales();
        vigenciaReformaLaboralBA.setReformalaboral(new ReformasLaborales());
        vigenciaNormaEmpleadoBA = new VigenciasNormasEmpleados();
        vigenciaNormaEmpleadoBA.setNormalaboral(new NormasLaborales());
        vigenciaContratoBA = new VigenciasContratos();
        vigenciaContratoBA.setContrato(new Contratos());
        vigenciaUbicacionBA = new VigenciasUbicaciones();
        vigenciaUbicacionBA.setUbicacion(new UbicacionesGeograficas());
        vigenciaUbicacionBA.getUbicacion().setCiudad(new Ciudades());
        vigenciaAfiliacionBA = new VigenciasAfiliaciones();
        vigenciaAfiliacionBA.setTercerosucursal(new TercerosSucursales());
        vigenciaAfiliacionBA.setTipoentidad(new TiposEntidades());
        vigenciaAfiliacionBA.setEstadoafiliacion(new EstadosAfiliaciones());
        vigenciaFormaPagoBA = new VigenciasFormasPagos();
        vigenciaFormaPagoBA.setFormapago(new Periodicidades());
        vigenciaFormaPagoBA.setSucursal(new Sucursales());
        mvrsBA = new Mvrs();
        mvrsBA.setMotivo(new Motivosmvrs());
        vigenciaJornadaBA = new VigenciasJornadas();
        vigenciaJornadaBA.setJornadatrabajo(new JornadasLaborales());
        motivoRetiroBA = new MotivosRetiros();
        //LOVS
        //PERSONAL
        lovProyectos = null;
        proyectoSeleccionado = new Proyectos();
        lovPryRoles = null;
        pryRolSeleccionado = new PryRoles();
        lovSectoresEconomicos = null;
        sectorEconomicoSeleccionado = new SectoresEconomicos();
        lovCursos = null;
        cursoSeleccionado = new Cursos();
        lovInstituciones = null;
        institucionSeleccionada = new Instituciones();
        lovProfesiones = null;
        profesionSeleccionada = new Profesiones();
        lovTiposIndicadores = null;
        tipoIndicadorSeleccionado = new TiposIndicadores();
        lovIndicadores = null;
        indicadorSeleccionado = new Indicadores();
        lovIdiomas = null;
        idiomaSeleccionado = new Idiomas();
        lovEstadosCiviles = null;
        estadoCivilSeleccionado = new EstadosCiviles();
        lovCiudades = null;
        ciudadSeleccionada = new Ciudades();
        //NOMINA
        lovMotivosRetiros = null;
        motivoRetiroSeleccionado = new MotivosRetiros();
        lovJornadasLaborales = null;
        jornadaLaboralSeleccionada = new JornadasLaborales();
        lovMotivosMvrs = null;
        motivoMvrsSeleccionado = new Motivosmvrs();
        lovPeriodicidades = null;
        periodicidadSeleccionada = new Periodicidades();
        lovSucursales = null;
        sucursalSeleccionada = new Sucursales();
        lovEstadosAfiliaciones = null;
        estadoAfiliacionSeleccionado = new EstadosAfiliaciones();
        lovTercerosSucursales = null;
        terceroSucursalSeleccionado = new TercerosSucursales();
        lovTiposEntidades = null;
        tipoEntidadSeleccionado = new TiposEntidades();
        lovUbicacionesGeograficas = null;
        ubicacionGeograficaSeleccionada = new UbicacionesGeograficas();
        lovContratos = null;
        contratoSeleccionado = new Contratos();
        lovNormasLaborales = null;
        normaLaboralSeleccionada = new NormasLaborales();
        lovReformasLaborales = null;
        reformaLaboralSeleccionada = new ReformasLaborales();
        lovMotivosContratos = null;
        motivoContratoSeleccionado = new MotivosContratos();
        lovTiposSueldos = null;
        tipoSueldoSeleccionado = new TiposSueldos();
        lovMotivosCambiosSueldos = null;
        motivoSueldoSeleccionado = new MotivosCambiosSueldos();
        lovTiposSueldos = null;
        tipoSueldoSeleccionado = new TiposSueldos();
        lovMotivosLocalizaciones = null;
        motivoLocalizacionSeleccionado = new MotivosLocalizaciones();
        lovEstructuras = null;
        estructuraSeleccionada = new Estructuras();
        lovCargos = null;
        cargoSeleccionado = new Cargos();
        lovEmpleados = null;
        empleadoSeleccionado = new Empleados();
        lovPapeles = null;
        papelSeleccionado = new Papeles();
        lovMotivosCambiosCargos = null;
        motivoCambioSeleccionado = new MotivosCambiosCargos();
        lovTiposTrabajadores = null;
        tipoTrabajadorSeleccionado = new TiposTrabajadores();
        //OTROS
        aceptar = true;
        auxSueldoMinimoSueldo = null;
        auxSueldoMaximoSueldo = null;
        auxSueldoMaximoMvrs = null;
        auxSueldoMinimoMvrs = null;
        auxPromedioMaximoSets = null;
        auxPromedioMinimoSets = null;
        conversacionDesde = null;
        conversacionHasta = null;
        lecturaDesde = null;
        lecturaHasta = null;
        escrituraDesde = null;
        escrituraHasta = null;
        tipoMetodoSets = "";
        //Resulatos de busqueda
        nuevaColumna = "";
        columnTemplate = "Codigo Primer_Apellido Segundo_Apellido Nombre ";
        mapValoresColumnas = new HashMap<String, String>();
        listaResultadoBusquedaAvanzada = new ArrayList<ResultadoBusquedaAvanzada>();
        listaCodigosEmpleado = new ArrayList<BigInteger>();
        createStaticColumns();
        //cambioBtnPrueba();
    }

   public void limpiarListasValor() {

   }

   @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarVigenciaCargoBusquedaAvanzada.obtenerConexion(ses.getId());
            administrarVigenciaLocalizacion.obtenerConexion(ses.getId());
            administrarVigenciaSueldo.obtenerConexion(ses.getId());
            administrarVigenciaTipoContrato.obtenerConexion(ses.getId());
            administrarVigenciasTiposTrabajadores.obtenerConexion(ses.getId());
            administrarVigenciaReformaLaboral.obtenerConexion(ses.getId());
            administrarVigenciaNormaEmpleado.obtenerConexion(ses.getId());
            administrarVigenciaContrato.obtenerConexion(ses.getId());
            administrarVigenciasUbicaciones.obtenerConexion(ses.getId());
            administrarVigenciaAfiliacion3.obtenerConexion(ses.getId());
            administrarEmplVigenciaFormaPago.obtenerConexion(ses.getId());
            administrarEmplMvrs.obtenerConexion(ses.getId());
            administrarVigenciaJornada.obtenerConexion(ses.getId());
            administrarBusquedaAvanzada.obtenerConexion(ses.getId());
            administrarEmpleadoIndividual.obtenerConexion(ses.getId());
            administrarEstadosCiviles.obtenerConexion(ses.getId());
            administrarIdiomasPersonas.obtenerConexion(ses.getId());
            administrarEmplVigenciaIndicador.obtenerConexion(ses.getId());
            administrarVigenciasNoFormales.obtenerConexion(ses.getId());
            administrarVigenciasFormales.obtenerConexion(ses.getId());
            administrarPerExperienciaLaboral.obtenerConexion(ses.getId());
            administrarVigenciasProyectos.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void obtenerListaColumnasEscenarios(List<ColumnasEscenarios> listaRetorno) {
        if (listaRetorno != null) {
            listaColumnasEscenarios = new ArrayList<ColumnasEscenarios>();
            listaColumnasEscenarios = listaRetorno;
        }
    }

    public void recibirTipoBusqueda(int tipoBusqueda) {
        numeroTipoBusqueda = tipoBusqueda;
        if (numeroTipoBusqueda == 0) {
            displayNomina = "inline";
            displayPersonal = "none";
            visibilidadPersonal = "hidden";
            visibilidadNomina = "visible";
        }
        if (numeroTipoBusqueda == 1) {
            displayNomina = "none";
            displayPersonal = "inline";
            visibilidadPersonal = "visible";
            visibilidadNomina = "hidden";
        }
    }

    public void ejecutarQuery() {
        restaurar();
        listaParametrosQueryModulos = new ArrayList<ParametrosQueryBusquedaAvanzada>();
        cargueQueryModuloNomina();
        cargueQueryModuloPersonal();
        String query = administrarBusquedaAvanzada.armarQueryModulosBusquedaAvanzada(listaParametrosQueryModulos);
        String queryEmpleado = "SELECT codigoempleado FROM EMPLEADOS EM ";
        if (!query.isEmpty()) {
            queryEmpleado = queryEmpleado + query;
        }
        listaCodigosEmpleado = administrarBusquedaAvanzada.ejecutarQueryBusquedaAvanzadaPorModulosCodigo(queryEmpleado);
        updateColumns();
    }

    public void cargueQueryModuloNomina() {
        cargueParametrosModuloCargo();
        cargueParametrosModuloCentroCosto();
        cargueParametrosModuloSueldo();
        cargueParametrosModuloFechaContrato();
        cargueParametrosModuloTipoTrabajador();
        cargueParametrosModuloTipoSalario();
        cargueParametrosModuloNormaLaboral();
        cargueParametrosModuloLegislacionLaboral();
        cargueParametrosModuloUbicacionGeografica();
        cargueParametrosModuloAfiliaciones();
        cargueParametrosModuloFormaPago();
        cargueParametrosModuloMVR();
        cargueParametrosModuloSET();
        cargueParametrosModuloVacaciones();
        cargueParametrosModuloFechaRetiro();
        cargueParametrosModuloJornadaLaboral();
    }

    public void cargueQueryModuloPersonal() {
        if (tabActivaTipoTrabajador == 0) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("TIPOTRABAJADOR", "BTIPOTRABAJADOR", "A");
            listaParametrosQueryModulos.add(parametro);
            ParametrosQueryBusquedaAvanzada parametro2 = new ParametrosQueryBusquedaAvanzada("TIPOTRABAJADOR", "TIPOTRABAJADORACTIVO", "ACTIVO");
            listaParametrosQueryModulos.add(parametro2);
        }
        cargueParametrosModuloDatosPersonales();
        cargueParametrosModuloFactorRH();
        cargueParametrosModuloEstadoCivil();
        cargueParametrosModuloIdioma();
        cargueParametrosModuloCensos();
        cargueParametrosModuloEducacionFormal();
        cargueParametrosModuloEducacionNoFormal();
        cargueParametrosModuloCargoPostularse();
        cargueParametrosModuloProyecto();
        cargueParametrosModuloExperienciaLaboral();
    }

    public void cargueParametrosModuloCargo() {
        if (tabActivaCargos == 1) {
            if (tipoFechaCargo == 1) {
                ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("CARGO", "BCARGO", "A");
                listaParametrosQueryModulos.add(parametro);
            }
            if (tipoFechaCargo == 2) {
                ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("CARGO", "BCARGO", "H");

                DateFormat df = DateFormat.getDateInstance();
                listaParametrosQueryModulos.add(parametro);
                ParametrosQueryBusquedaAvanzada parametro2 = null;
                ParametrosQueryBusquedaAvanzada parametro3 = null;
                if (fechaInicialCargo != null) {
                    parametro2 = new ParametrosQueryBusquedaAvanzada("CARGO", "CARGODESDE", df.format(fechaInicialCargo).toString());
                    listaParametrosQueryModulos.add(parametro2);
                }
                if (fechaFinalCargo != null) {
                    parametro3 = new ParametrosQueryBusquedaAvanzada("CARGO", "CARGOHASTA", df.format(fechaFinalCargo).toString());
                    listaParametrosQueryModulos.add(parametro3);
                }
            }
            if (vigenciaCargoBA.getCargo().getSecuencia() != null) {
                ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("CARGO", "CARGO", vigenciaCargoBA.getCargo().getSecuencia().toString());
                listaParametrosQueryModulos.add(parametro);
            }
            if (vigenciaCargoBA.getEstructura().getSecuencia() != null) {
                ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("CARGO", "ESTRUCTURA", vigenciaCargoBA.getEstructura().getSecuencia().toString());
                ParametrosQueryBusquedaAvanzada parametro2 = new ParametrosQueryBusquedaAvanzada("CARGO", "CENTROCOSTO", vigenciaCargoBA.getEstructura().getCentrocosto().getSecuencia().toString());
                listaParametrosQueryModulos.add(parametro);
                listaParametrosQueryModulos.add(parametro2);
            }
            if (vigenciaCargoBA.getEmpleadojefe().getSecuencia() != null) {
                ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("CARGO", "EMPLEADOJEFE", vigenciaCargoBA.getEmpleadojefe().getSecuencia().toString());
                listaParametrosQueryModulos.add(parametro);
            }
            if (vigenciaCargoBA.getMotivocambiocargo().getSecuencia() != null) {
                ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("CARGO", "MOTIVOCAMBIOCARGO", vigenciaCargoBA.getMotivocambiocargo().getSecuencia().toString());
                listaParametrosQueryModulos.add(parametro);
            }
            if (vigenciaCargoBA.getPapel().getSecuencia() != null) {
                ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("CARGO", "PAPEL", vigenciaCargoBA.getPapel().toString());
                listaParametrosQueryModulos.add(parametro);
            }
        }
    }

    public void cargueParametrosModuloCentroCosto() {
        if (auxTabActivoCentroCosto == true) {
            tabActivaCentroCosto = 1;
        }
        if (tabActivaCentroCosto == 1) {
            if (tipoFechaCentroCosto == 1) {
                ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("CENTROCOSTO", "BCENTROCOSTO", "A");
                listaParametrosQueryModulos.add(parametro);
            }
            if (tipoFechaCentroCosto == 2) {
                ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("CENTROCOSTO", "BCENTROCOSTO", "H");
                DateFormat df = DateFormat.getDateInstance();
                listaParametrosQueryModulos.add(parametro);
                ParametrosQueryBusquedaAvanzada parametro2 = null;
                ParametrosQueryBusquedaAvanzada parametro3 = null;
                if (fechaInicialCentroCosto != null) {
                    parametro2 = new ParametrosQueryBusquedaAvanzada("CENTROCOSTO", "CENTROCOSTODESDE", df.format(fechaInicialCentroCosto).toString());
                    listaParametrosQueryModulos.add(parametro2);
                }
                if (fechaFinalCentroCosto != null) {
                    parametro3 = new ParametrosQueryBusquedaAvanzada("CENTROCOSTO", "CENTROCOSTOHASTA", df.format(fechaFinalCentroCosto).toString());
                    listaParametrosQueryModulos.add(parametro3);
                }
            }
            if (vigenciaLocalizacionBA.getLocalizacion().getSecuencia() != null) {
                ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("CENTROCOSTO", "LOCALIZACION", vigenciaLocalizacionBA.getLocalizacion().getSecuencia().toString());
                listaParametrosQueryModulos.add(parametro);
            }
            if (vigenciaLocalizacionBA.getMotivo().getSecuencia() != null) {
                ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("CENTROCOSTO", "MOTIVOLOCALIZACION", vigenciaLocalizacionBA.getMotivo().getSecuencia().toString());
                listaParametrosQueryModulos.add(parametro);
            }
        }
    }

    public void cargueParametrosModuloSueldo() {
        if (tabActivaSueldo == 1) {
            if (tipoFechaSueldo == 1) {
                ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("SUELDO", "BSUELDO", "A");
                listaParametrosQueryModulos.add(parametro);
            }
            if (tipoFechaSueldo == 2) {
                ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("SUELDO", "BSUELDO", "H");
                DateFormat df = DateFormat.getDateInstance();
                listaParametrosQueryModulos.add(parametro);
                ParametrosQueryBusquedaAvanzada parametro2 = null;
                ParametrosQueryBusquedaAvanzada parametro3 = null;
                if (fechaInicialSueldo != null) {
                    parametro2 = new ParametrosQueryBusquedaAvanzada("SUELDO", "SUELDODESDE", df.format(fechaInicialSueldo).toString());
                    listaParametrosQueryModulos.add(parametro2);
                }
                if (fechaFinalSueldo != null) {
                    parametro3 = new ParametrosQueryBusquedaAvanzada("SUELDO", "SUELDOHASTA", df.format(fechaFinalSueldo).toString());
                    listaParametrosQueryModulos.add(parametro3);
                }
            }
            if (vigenciaSueldoBA.getTiposueldo().getSecuencia() != null) {
                ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("SUELDO", "TIPOSUELDO ", vigenciaSueldoBA.getTiposueldo().getSecuencia().toString());
                listaParametrosQueryModulos.add(parametro);
            }
            if (vigenciaSueldoBA.getMotivocambiosueldo().getSecuencia() != null) {
                ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("SUELDO", "MOTIVOCAMBIOSUELDO", vigenciaSueldoBA.getMotivocambiosueldo().getSecuencia().toString());
                listaParametrosQueryModulos.add(parametro);
            }
            if (sueldoMinSueldo != null) {
                ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("SUELDO", "SUELDOMINIMO", sueldoMinSueldo.toString());
                listaParametrosQueryModulos.add(parametro);
            }
            if (sueldoMaxSueldo != null) {
                ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("SUELDO", "SUELDOMAXIMO", sueldoMaxSueldo.toString());
                listaParametrosQueryModulos.add(parametro);
            }
        }
    }

    public void cargueParametrosModuloFechaContrato() {
        if (tabActivaFechaContrato == 1) {
            if (tipoFechaFechaContrato == 1) {
                ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("FECHACONTRATO", "BFECHACONTRATO", "A");
                listaParametrosQueryModulos.add(parametro);
            }
            if (tipoFechaFechaContrato == 2) {
                ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("FECHACONTRATO", "BFECHACONTRATO", "H");
                DateFormat df = DateFormat.getDateInstance();
                listaParametrosQueryModulos.add(parametro);
                ParametrosQueryBusquedaAvanzada parametro2 = null;
                ParametrosQueryBusquedaAvanzada parametro3 = null;
                if (fechaInicialFechaContrato != null) {
                    parametro2 = new ParametrosQueryBusquedaAvanzada("FECHACONTRATO", "FECHACONTRATODESDE", df.format(fechaInicialFechaContrato).toString());
                    listaParametrosQueryModulos.add(parametro2);
                }
                if (fechaFinalFechaContrato != null) {
                    parametro3 = new ParametrosQueryBusquedaAvanzada("FECHACONTRATO", "FECHACONTRATOHASTA", df.format(fechaFinalFechaContrato).toString());
                    listaParametrosQueryModulos.add(parametro3);
                }
            }
            if (vigenciaTipoContratoBA.getTipocontrato().getSecuencia() != null) {
                ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("FECHACONTRATO", "TIPOCONTRATO", vigenciaTipoContratoBA.getTipocontrato().toString());
                listaParametrosQueryModulos.add(parametro);
            }
            if (vigenciaTipoContratoBA.getMotivocontrato().getSecuencia() != null) {
                ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("FECHACONTRATO", "MOTIVOCONTRATO", vigenciaTipoContratoBA.getMotivocontrato().getSecuencia().toString());
                listaParametrosQueryModulos.add(parametro);
            }
        }
    }

    public void cargueParametrosModuloTipoTrabajador() {
        if (tabActivaTipoTrabajador == 1) {
            if (tipoFechaTipoTrabajador == 1) {
                ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("TIPOTRABAJADOR", "BTIPOTRABAJADOR", "A");
                listaParametrosQueryModulos.add(parametro);
            }
            if (tipoFechaTipoTrabajador == 2) {
                ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("TIPOTRABAJADOR", "BTIPOTRABAJADOR", "H");
                DateFormat df = DateFormat.getDateInstance();
                listaParametrosQueryModulos.add(parametro);
                ParametrosQueryBusquedaAvanzada parametro2 = null;
                ParametrosQueryBusquedaAvanzada parametro3 = null;
                if (fechaInicialTipoTrabajador != null) {
                    parametro2 = new ParametrosQueryBusquedaAvanzada("TIPOTRABAJADOR", "TIPOTRABAJADORDESDE", df.format(fechaInicialTipoTrabajador).toString());
                    listaParametrosQueryModulos.add(parametro2);
                }
                if (fechaFinalTipoTrabajador != null) {
                    parametro3 = new ParametrosQueryBusquedaAvanzada("TIPOTRABAJADOR", "TIPOTRABAJADORHASTA", df.format(fechaFinalTipoTrabajador).toString());
                    listaParametrosQueryModulos.add(parametro3);
                }
            }
            if (vigenciaTipoTrabajadorBA.getTipotrabajador().getSecuencia() != null) {
                ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("TIPOTRABAJADOR", "TIPOTRABAJADOR", vigenciaTipoTrabajadorBA.getTipotrabajador().toString());
                listaParametrosQueryModulos.add(parametro);
            }
        }
    }

    public void cargueParametrosModuloTipoSalario() {
        if (tabActivaTipoSalario == 1) {
            if (tipoFechaTipoSalario == 1) {
                ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("TIPOSALARIO", "BTIPOSALARIO", "A");
                listaParametrosQueryModulos.add(parametro);
            }
            if (tipoFechaTipoSalario == 2) {
                ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("TIPOSALARIO", "BTIPOSALARIO", "H");
                DateFormat df = DateFormat.getDateInstance();
                listaParametrosQueryModulos.add(parametro);
                ParametrosQueryBusquedaAvanzada parametro2 = null;
                ParametrosQueryBusquedaAvanzada parametro3 = null;
                if (fechaInicialTipoSalario != null) {
                    parametro2 = new ParametrosQueryBusquedaAvanzada("TIPOSALARIO", "TIPOSALARIODESDE", df.format(fechaInicialTipoSalario).toString());
                    listaParametrosQueryModulos.add(parametro2);
                }
                if (fechaFinalTipoSalario != null) {
                    parametro3 = new ParametrosQueryBusquedaAvanzada("TIPOSALARIO", "TIPOSALARIOHASTA", df.format(fechaFinalTipoSalario).toString());
                    listaParametrosQueryModulos.add(parametro3);
                }
            }
            if (vigenciaReformaLaboralBA.getReformalaboral().getSecuencia() != null) {
                ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("TIPOSALARIO", "REFORMA", vigenciaReformaLaboralBA.getReformalaboral().getSecuencia().toString());
                listaParametrosQueryModulos.add(parametro);
            }
        }
    }

    public void cargueParametrosModuloNormaLaboral() {
        if (tabActivaNormaLaboral == 1) {
            if (tipoFechaNormaLaboral == 1) {
                ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("NORMALABORAL", "BNORMALABORAL", "A");
                listaParametrosQueryModulos.add(parametro);
            }
            if (tipoFechaNormaLaboral == 2) {
                ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("NORMALABORAL", "BNORMALABORAL", "H");

                DateFormat df = DateFormat.getDateInstance();
                listaParametrosQueryModulos.add(parametro);
                ParametrosQueryBusquedaAvanzada parametro2 = null;
                ParametrosQueryBusquedaAvanzada parametro3 = null;
                if (fechaInicialNormaLaboral != null) {
                    parametro2 = new ParametrosQueryBusquedaAvanzada("NORMALABORAL", "NORMALABORALDESDE", df.format(fechaInicialNormaLaboral).toString());
                    listaParametrosQueryModulos.add(parametro2);
                }
                if (fechaFinalNormaLaboral != null) {
                    parametro3 = new ParametrosQueryBusquedaAvanzada("NORMALABORAL", "NORMALABORALHASTA", df.format(fechaFinalNormaLaboral).toString());
                    listaParametrosQueryModulos.add(parametro3);
                }
            }
            if (vigenciaNormaEmpleadoBA.getNormalaboral().getSecuencia() != null) {
                ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("NORMALABORAL", "NORMA", vigenciaNormaEmpleadoBA.getNormalaboral().getSecuencia().toString());
                listaParametrosQueryModulos.add(parametro);
            }
        }
    }

    public void cargueParametrosModuloLegislacionLaboral() {
        if (tabActivaLegislacionLaboral == 1) {
            if (tipoFechaLegislacionLaboral == 1) {
                ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("LEGISLACIONLABORAL", "BLEGISLACIONLABORAL", "A");
                listaParametrosQueryModulos.add(parametro);
            }
            if (tipoFechaLegislacionLaboral == 2) {
                ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("LEGISLACIONLABORAL", "BLEGISLACIONLABORAL", "H");
                DateFormat df = DateFormat.getDateInstance();
                listaParametrosQueryModulos.add(parametro);
                ParametrosQueryBusquedaAvanzada parametro2 = null;
                ParametrosQueryBusquedaAvanzada parametro3 = null;
                ParametrosQueryBusquedaAvanzada parametro4 = null;
                ParametrosQueryBusquedaAvanzada parametro5 = null;
                if (fechaMIInicialLegislacionLaboral != null) {
                    parametro2 = new ParametrosQueryBusquedaAvanzada("LEGISLACIONLABORAL", "LEGISLACIONLABORALDESDE", df.format(fechaMIInicialLegislacionLaboral).toString());
                    listaParametrosQueryModulos.add(parametro2);
                }
                if (fechaMIFinalLegislacionLaboral != null) {
                    parametro3 = new ParametrosQueryBusquedaAvanzada("LEGISLACIONLABORAL", "LEGISLACIONLABORALHASTA", df.format(fechaMIFinalLegislacionLaboral).toString());
                    listaParametrosQueryModulos.add(parametro3);
                }
                if (fechaMFInicialLegislacionLaboral != null) {
                    parametro4 = new ParametrosQueryBusquedaAvanzada("LEGISLACIONLABORAL", "LEGISLACIONLABORALDESDEF", df.format(fechaMFInicialLegislacionLaboral).toString());
                    listaParametrosQueryModulos.add(parametro4);
                }
                if (fechaMFFinalLegislacionLaboral != null) {
                    parametro5 = new ParametrosQueryBusquedaAvanzada("LEGISLACIONLABORAL", "LEGISLACIONLABORALHASTAF", df.format(fechaMFFinalLegislacionLaboral).toString());
                    listaParametrosQueryModulos.add(parametro5);
                }
            }
            if (vigenciaContratoBA.getContrato().getSecuencia() != null) {
                ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("LEGISLACIONLABORAL", "CONTRATO", vigenciaContratoBA.getContrato().getSecuencia().toString());
                listaParametrosQueryModulos.add(parametro);
            }
        }
    }

    public void cargueParametrosModuloUbicacionGeografica() {
        if (tabActivaUbicacion == 1) {
            if (tipoFechaUbicacion == 1) {
                ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("UBICACION", "BUBICACION", "A");
                listaParametrosQueryModulos.add(parametro);
            }
            if (tipoFechaUbicacion == 2) {
                ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("UBICACION", "BUBICACION", "H");
                DateFormat df = DateFormat.getDateInstance();
                listaParametrosQueryModulos.add(parametro);
                ParametrosQueryBusquedaAvanzada parametro2 = null;
                ParametrosQueryBusquedaAvanzada parametro3 = null;
                if (fechaInicialUbicacion != null) {
                    parametro2 = new ParametrosQueryBusquedaAvanzada("UBICACION", "UBICACIONDESDE", df.format(fechaInicialUbicacion).toString());
                    listaParametrosQueryModulos.add(parametro2);
                }
                if (fechaFinalUbicacion != null) {
                    parametro3 = new ParametrosQueryBusquedaAvanzada("UBICACION", "UBICACIONHASTA", df.format(fechaFinalUbicacion).toString());
                    listaParametrosQueryModulos.add(parametro3);
                }
            }
            if (vigenciaUbicacionBA.getUbicacion().getSecuencia() != null) {
                ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("UBICACION", "UBICACION", vigenciaUbicacionBA.getUbicacion().getSecuencia().toString());
                listaParametrosQueryModulos.add(parametro);
            }
        }
    }

    public void cargueParametrosModuloAfiliaciones() {
        if (tabActivaAfiliacion == 1) {
            if (tipoFechaAfiliacion == 1) {
                ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("AFILIACIONES", "BAFILIACIONES", "A");
                listaParametrosQueryModulos.add(parametro);
            }
            if (tipoFechaAfiliacion == 2) {
                ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("AFILIACIONES", "BAFILIACIONES", "H");
                listaParametrosQueryModulos.add(parametro);
                DateFormat df = DateFormat.getDateInstance();
                ParametrosQueryBusquedaAvanzada parametro2 = null;
                ParametrosQueryBusquedaAvanzada parametro3 = null;
                if (fechaInicialAfiliacion != null) {
                    parametro2 = new ParametrosQueryBusquedaAvanzada("AFILIACIONES", "AFILIACIONESDESDE", df.format(fechaInicialAfiliacion).toString());
                    listaParametrosQueryModulos.add(parametro2);
                }
                if (fechaFinalAfiliacion != null) {
                    parametro3 = new ParametrosQueryBusquedaAvanzada("AFILIACIONES", "AFILIACIONESHASTA", df.format(fechaFinalAfiliacion).toString());
                    listaParametrosQueryModulos.add(parametro3);
                }
            }
            if (vigenciaAfiliacionBA.getTercerosucursal().getSecuencia() != null) {
                ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("AFILIACIONES", "TERCERO", vigenciaAfiliacionBA.getTercerosucursal().getSecuencia().toString());
                listaParametrosQueryModulos.add(parametro);
            }
            if (vigenciaAfiliacionBA.getTipoentidad().getSecuencia() != null) {
                ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("AFILIACIONES", "TIPOENTIDAD", vigenciaAfiliacionBA.getTipoentidad().getSecuencia().toString());
                listaParametrosQueryModulos.add(parametro);
            }
            if (vigenciaAfiliacionBA.getEstadoafiliacion().getSecuencia() != null) {
                ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("AFILIACIONES", "ESTADO", vigenciaAfiliacionBA.getEstadoafiliacion().getSecuencia().toString());
                listaParametrosQueryModulos.add(parametro);
            }
        }
    }

    public void cargueParametrosModuloFormaPago() {
        if (tabActivaFormaPago == 1) {
            if (tipoFechaFormaPago == 1) {
                ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("FORMAPAGO", "BFORMAPAGO", "A");
                listaParametrosQueryModulos.add(parametro);
            }
            if (tipoFechaFormaPago == 2) {
                ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("FORMAPAGO", "BFORMAPAGO", "H");

                listaParametrosQueryModulos.add(parametro);
                DateFormat df = DateFormat.getDateInstance();
                ParametrosQueryBusquedaAvanzada parametro2 = null;
                ParametrosQueryBusquedaAvanzada parametro3 = null;
                if (fechaInicialFormaPago != null) {
                    parametro2 = new ParametrosQueryBusquedaAvanzada("FORMAPAGO", "FORMAPAGODESDE", df.format(fechaInicialFormaPago).toString());
                    listaParametrosQueryModulos.add(parametro2);
                }
                if (fechaFinalFormaPago != null) {
                    parametro3 = new ParametrosQueryBusquedaAvanzada("FORMAPAGO", "FORMAPAGOHASTA", df.format(fechaFinalFormaPago).toString());
                    listaParametrosQueryModulos.add(parametro3);
                }
            }
            if (vigenciaFormaPagoBA.getFormapago().getSecuencia() != null) {
                ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("FORMAPAGO", "FORMAPAGO", vigenciaFormaPagoBA.getFormapago().getSecuencia().toString());
                listaParametrosQueryModulos.add(parametro);
            }
            if (vigenciaFormaPagoBA.getSucursal().getSecuencia() != null) {
                ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("FORMAPAGO", "SUCURSAL", vigenciaFormaPagoBA.getSucursal().getSecuencia().toString());
                listaParametrosQueryModulos.add(parametro);
            }

        }
    }

    public void cargueParametrosModuloMVR() {
        if (tabActivaMvrs == 1) {
            if (tipoFechaMvrs == 1) {
                ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("MVRS", "BMVRS", "A");
                listaParametrosQueryModulos.add(parametro);
            }
            if (tipoFechaMvrs == 2) {
                ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("MVRS", "BMVRS", "H");
                listaParametrosQueryModulos.add(parametro);
                DateFormat df = DateFormat.getDateInstance();
                ParametrosQueryBusquedaAvanzada parametro2 = null;
                ParametrosQueryBusquedaAvanzada parametro3 = null;
                if (fechaInicialMvrs != null) {
                    parametro2 = new ParametrosQueryBusquedaAvanzada("MVRS", "MVRSDESDE", df.format(fechaInicialMvrs).toString());
                    listaParametrosQueryModulos.add(parametro2);
                }
                if (fechaFinalMvrs != null) {
                    parametro3 = new ParametrosQueryBusquedaAvanzada("MVRS", "MVRSHASTA", df.format(fechaFinalMvrs).toString());
                    listaParametrosQueryModulos.add(parametro3);
                }
            }
            if (mvrsBA.getMotivo().getSecuencia() != null) {
                ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("MVRS", "MOTIVO", mvrsBA.getMotivo().getSecuencia().toString());
                listaParametrosQueryModulos.add(parametro);
            }
            if (sueldoMinMvrs != null) {
                ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("MVRS", "SUELDOMINIMO", sueldoMinMvrs.toString());
                listaParametrosQueryModulos.add(parametro);
            }
            if (sueldoMaxMvrs != null) {
                ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("MVRS", "SUELDOMAXIMO", sueldoMaxMvrs.toString());
                listaParametrosQueryModulos.add(parametro);
            }
        }
    }

    public void cargueParametrosModuloSET() {
        if (tabActivaSets == 1) {
            if (tipoFechaSets == 1) {
                ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("SETS", "BSETS", "A");
                listaParametrosQueryModulos.add(parametro);
            }
            if (tipoFechaMvrs == 2) {
                ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("SETS", "BSETS", "H");

                listaParametrosQueryModulos.add(parametro);
                DateFormat df = DateFormat.getDateInstance();
                ParametrosQueryBusquedaAvanzada parametro2 = null;
                ParametrosQueryBusquedaAvanzada parametro3 = null;
                ParametrosQueryBusquedaAvanzada parametro4 = null;
                ParametrosQueryBusquedaAvanzada parametro5 = null;
                if (fechaMIInicialSets != null) {
                    parametro2 = new ParametrosQueryBusquedaAvanzada("SETS", "SETSDESDE", df.format(fechaMIInicialSets).toString());
                    listaParametrosQueryModulos.add(parametro2);
                }
                if (fechaMIFinalSets != null) {
                    parametro3 = new ParametrosQueryBusquedaAvanzada("SETS", "SETSHASTA", df.format(fechaMIFinalSets).toString());
                    listaParametrosQueryModulos.add(parametro3);
                }
                if (fechaMFInicialSets != null) {
                    parametro4 = new ParametrosQueryBusquedaAvanzada("SETS", "SETSDESDEF", df.format(fechaMFInicialSets).toString());
                    listaParametrosQueryModulos.add(parametro4);
                }
                if (fechaMFFinalSets != null) {
                    parametro5 = new ParametrosQueryBusquedaAvanzada("SETS", "SETSHASTAF", df.format(fechaMFFinalSets).toString());
                    listaParametrosQueryModulos.add(parametro5);
                }
            }
            if (tipoMetodoSets != null) {
                ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("SETS", "METODO", tipoMetodoSets);
                listaParametrosQueryModulos.add(parametro);
            }
            if (promedioMinimoSets != null) {
                ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("SETS", "PROMEDIOMINIMO", promedioMinimoSets.toString());
                listaParametrosQueryModulos.add(parametro);
            }
            if (promedioMaximoSets != null) {
                ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("SETS", "PROMEDIOMAXIMO", promedioMaximoSets.toString());
                listaParametrosQueryModulos.add(parametro);
            }
        }
    }

    public void cargueParametrosModuloVacaciones() {
        if (tabActivaVacacion == 1) {
            ParametrosQueryBusquedaAvanzada parametroInicial = new ParametrosQueryBusquedaAvanzada("VACACIONES", "NN", "NN");
            listaParametrosQueryModulos.add(parametroInicial);
            if (fechaMIInicialVacacion != null) {
                DateFormat df = DateFormat.getDateInstance();
                ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("VACACIONES", "FECHASALIDADESDE", df.format(fechaMIInicialVacacion).toString());
                listaParametrosQueryModulos.add(parametro);
            }
            if (fechaMIFinalVacacion != null) {
                DateFormat df = DateFormat.getDateInstance();
                ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("VACACIONES", "FECHASALIDAHASTA", df.format(fechaMIFinalVacacion).toString());
                listaParametrosQueryModulos.add(parametro);
            }
            if (fechaMFInicialVacacion != null) {
                DateFormat df = DateFormat.getDateInstance();
                ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("VACACIONES", "FECHAREGRESODESDE", df.format(fechaMFInicialVacacion).toString());
                listaParametrosQueryModulos.add(parametro);
            }
            if (fechaMFFinalVacacion != null) {
                DateFormat df = DateFormat.getDateInstance();
                ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("VACACIONES", "FECHAREGRESOHASTA", df.format(fechaMFFinalVacacion).toString());
                listaParametrosQueryModulos.add(parametro);
            }
        }
    }

    public void cargueParametrosModuloFechaRetiro() {
        if (tabActivaFechaRetiro == 1) {
            ParametrosQueryBusquedaAvanzada parametroInicial = new ParametrosQueryBusquedaAvanzada("FECHARETIRO", "NN", "NN");
            listaParametrosQueryModulos.add(parametroInicial);
            if (fechaInicialFechaRetiro != null) {
                DateFormat df = DateFormat.getDateInstance();
                ParametrosQueryBusquedaAvanzada parametro2 = new ParametrosQueryBusquedaAvanzada("FECHARETIRO", "FECHARETIRODESDE", df.format(fechaInicialFechaRetiro).toString());
                listaParametrosQueryModulos.add(parametro2);
            }
            if (fechaFinalFechaRetiro != null) {
                DateFormat df = DateFormat.getDateInstance();
                ParametrosQueryBusquedaAvanzada parametro3 = new ParametrosQueryBusquedaAvanzada("FECHARETIRO", "FECHARETIROHASTA", df.format(fechaFinalFechaRetiro).toString());
                listaParametrosQueryModulos.add(parametro3);
            }
            if (motivoRetiroBA.getSecuencia() != null) {
                ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("FECHARETIRO", "MOTIVO", motivoRetiroBA.getSecuencia().toString());
                listaParametrosQueryModulos.add(parametro);
            }
        }
    }

    public void cargueParametrosModuloJornadaLaboral() {
        if (tabActivaJornadaLaboral == 1) {
            ParametrosQueryBusquedaAvanzada parametroInicial = new ParametrosQueryBusquedaAvanzada("JORNADALABORAL", "NN", "NN");
            listaParametrosQueryModulos.add(parametroInicial);
            if (fechaInicialJornadaLaboral != null) {
                DateFormat df = DateFormat.getDateInstance();
                ParametrosQueryBusquedaAvanzada parametro2 = new ParametrosQueryBusquedaAvanzada("JORNADALABORAL", "JORNADALABORALDESDE", df.format(fechaInicialJornadaLaboral).toString());
                listaParametrosQueryModulos.add(parametro2);
            }
            if (fechaFinalJornadaLaboral != null) {
                DateFormat df = DateFormat.getDateInstance();
                ParametrosQueryBusquedaAvanzada parametro3 = new ParametrosQueryBusquedaAvanzada("JORNADALABORAL", "JORNADALABORALHASTA", df.format(fechaFinalJornadaLaboral).toString());
                listaParametrosQueryModulos.add(parametro3);
            }
            if (vigenciaJornadaBA.getJornadatrabajo().getSecuencia() != null) {
                ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("JORNADALABORAL", "JORNADA", vigenciaJornadaBA.getJornadatrabajo().getSecuencia().toString());
                listaParametrosQueryModulos.add(parametro);
            }
        }
    }

    public void cargueParametrosModuloDatosPersonales() {
        if (tabActivaDatosPersonales == 1) {

            ParametrosQueryBusquedaAvanzada parametroInicial = new ParametrosQueryBusquedaAvanzada("DATOSPERSONALES", "NN", "NN");
            listaParametrosQueryModulos.add(parametroInicial);

            if (empleadoBA.getPersona().getStrNumeroDocumento() != null) {
                if (!empleadoBA.getPersona().getStrNumeroDocumento().isEmpty()) {
                    ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("DATOSPERSONALES", "NUMERODOCUMENTO", empleadoBA.getPersona().getStrNumeroDocumento());
                    listaParametrosQueryModulos.add(parametro);
                }
            }
            if (empleadoBA.getPersona().getCiudaddocumento().getSecuencia() != null) {
                ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("DATOSPERSONALES", "CIUDADDOCUMENTO", empleadoBA.getPersona().getCiudaddocumento().getSecuencia().toString());
                listaParametrosQueryModulos.add(parametro);
            }
            if (!empleadoBA.getPersona().getSexo().isEmpty()) {
                ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("DATOSPERSONALES", "SEXO", empleadoBA.getPersona().getSexo().toString());
                listaParametrosQueryModulos.add(parametro);
                System.out.println("sexo : " + empleadoBA.getPersona().getSexo());
            }
            if (empleadoBA.getPersona().getCiudadnacimiento().getSecuencia() != null) {
                ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("DATOSPERSONALES", "CIUDADNACIMIENTO", empleadoBA.getPersona().getCiudadnacimiento().getSecuencia().toString());
                listaParametrosQueryModulos.add(parametro);
            }
            if (fechaInicialDatosPersonales != null) {
                DateFormat df = DateFormat.getDateInstance();
                ParametrosQueryBusquedaAvanzada parametro2 = new ParametrosQueryBusquedaAvanzada("DATOSPERSONALES", "FECHANACIMIENTODESDE", df.format(fechaInicialDatosPersonales).toString());
                listaParametrosQueryModulos.add(parametro2);
            }
            if (fechaFinalDatosPersonales != null) {
                DateFormat df = DateFormat.getDateInstance();
                ParametrosQueryBusquedaAvanzada parametro3 = new ParametrosQueryBusquedaAvanzada("DATOSPERSONALES", "FECHANACIMIENTOHASTA", df.format(fechaFinalDatosPersonales).toString());
                listaParametrosQueryModulos.add(parametro3);
            }
        }
    }

    public void cargueParametrosModuloFactorRH() {
        if (tabActivaFactorRH == 1) {
            if (!empleadoBA.getPersona().getFactorrh().isEmpty()) {
                ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("FACTORRH", "FACTORRH", empleadoBA.getPersona().getFactorrh().toString());
                listaParametrosQueryModulos.add(parametro);
            }
            if (!empleadoBA.getPersona().getGruposanguineo().isEmpty()) {
                ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("FACTORRH", "GRUPOSANGUINEO", empleadoBA.getPersona().getGruposanguineo().toString());
                listaParametrosQueryModulos.add(parametro);
            }
        }
    }

    public void cargueParametrosModuloEstadoCivil() {
        if (tabActivaEstadoCivil == 1) {
            if (tipoFechaEstadoCivil == 1) {
                ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("ESTADOCIVIL", "BESTADOCIVIL", "A");
                listaParametrosQueryModulos.add(parametro);
            }
            if (tipoFechaEstadoCivil == 2) {
                ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("ESTADOCIVIL", "BESTADOCIVIL", "H");

                listaParametrosQueryModulos.add(parametro);
                DateFormat df = DateFormat.getDateInstance();
                ParametrosQueryBusquedaAvanzada parametro2 = null;
                ParametrosQueryBusquedaAvanzada parametro3 = null;
                if (fechaInicialEstadoCivil != null) {
                    parametro2 = new ParametrosQueryBusquedaAvanzada("ESTADOCIVIL", "ESTADOCIVILDESDE", df.format(fechaInicialEstadoCivil).toString());
                    listaParametrosQueryModulos.add(parametro2);
                }
                if (fechaFinalEstadoCivil != null) {
                    parametro3 = new ParametrosQueryBusquedaAvanzada("ESTADOCIVIL", "ESTADOCIVILHASTA", df.format(fechaFinalEstadoCivil).toString());
                    listaParametrosQueryModulos.add(parametro3);
                }
            }
            if (estadoCivilBA.getSecuencia() != null) {
                ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("ESTADOCIVIL", "ESTADOCIVIL", estadoCivilBA.getSecuencia().toString());
                listaParametrosQueryModulos.add(parametro);
            }
        }
    }

    public void cargueParametrosModuloIdioma() {
        if (tabActivaIdioma == 1) {

            ParametrosQueryBusquedaAvanzada parametroInicial = new ParametrosQueryBusquedaAvanzada("IDIOMA", "NN", "NN");
            listaParametrosQueryModulos.add(parametroInicial);

            if (idiomaPersonaBA.getIdioma().getSecuencia() != null) {
                ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("IDIOMA", "IDIOMA", idiomaPersonaBA.getIdioma().getSecuencia().toString());
                listaParametrosQueryModulos.add(parametro);
            }
            if (conversacionDesde != null) {
                ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("IDIOMA", "CONVERSACIONDESDE", conversacionDesde.toString());
                listaParametrosQueryModulos.add(parametro);
            }
            if (conversacionHasta != null) {
                ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("IDIOMA", "CONVERSACIONHASTA", conversacionHasta.toString());
                listaParametrosQueryModulos.add(parametro);
            }
            if (lecturaDesde != null) {
                ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("IDIOMA", "LECTURADESDE", lecturaDesde.toString());
                listaParametrosQueryModulos.add(parametro);
            }
            if (lecturaHasta != null) {
                ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("IDIOMA", "LECTURAHASTA", lecturaHasta.toString());
                listaParametrosQueryModulos.add(parametro);
            }
            if (escrituraDesde != null) {
                ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("IDIOMA", "ESCRITURADESDE", escrituraDesde.toString());
                listaParametrosQueryModulos.add(parametro);
            }
            if (escrituraHasta != null) {
                ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("IDIOMA", "ESCRITURAHASTA", escrituraHasta.toString());
                listaParametrosQueryModulos.add(parametro);
            }
        }
    }

    public void cargueParametrosModuloCensos() {
        if (tabActivaCenso == 1) {
            if (tipoFechaCenso == 1) {
                ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("CENSOS", "BCENSOS", "A");
                listaParametrosQueryModulos.add(parametro);
            }
            if (tipoFechaCenso == 2) {
                ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("CENSOS", "BCENSOS", "H");
                listaParametrosQueryModulos.add(parametro);
                DateFormat df = DateFormat.getDateInstance();
                ParametrosQueryBusquedaAvanzada parametro2 = null;
                ParametrosQueryBusquedaAvanzada parametro3 = null;
                if (fechaInicialCenso != null) {
                    parametro2 = new ParametrosQueryBusquedaAvanzada("CENSOS", "CENSOSDESDE", df.format(fechaInicialCenso).toString());
                    listaParametrosQueryModulos.add(parametro2);
                }
                if (fechaFinalCenso != null) {
                    parametro3 = new ParametrosQueryBusquedaAvanzada("CENSOS", "CENSOSHASTA", df.format(fechaFinalCenso).toString());
                    listaParametrosQueryModulos.add(parametro3);
                }
            }
            if (vigenciaIndicadorBA.getTipoindicador().getSecuencia() != null) {
                ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("CENSOS", "TIPOINDICADOR", vigenciaIndicadorBA.getTipoindicador().getSecuencia().toString());
                listaParametrosQueryModulos.add(parametro);
            }
            if (vigenciaIndicadorBA.getIndicador().getSecuencia() != null) {
                ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("CENSOS", "INDICADOR", vigenciaIndicadorBA.getIndicador().getSecuencia().toString());
                listaParametrosQueryModulos.add(parametro);
            }
        }
    }

    public void cargueParametrosModuloEducacionFormal() {
        if (tabActivaEducacionFormal == 1) {
            if (tipoFechaEducacionFormal == 1) {
                ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("EDUCACIONFORMAL", "BEDUCACIONFORMAL", "A");
                listaParametrosQueryModulos.add(parametro);
            }
            if (tipoFechaEducacionFormal == 2) {
                ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("EDUCACIONFORMAL", "BEDUCACIONFORMAL", "H");
                listaParametrosQueryModulos.add(parametro);
                DateFormat df = DateFormat.getDateInstance();
                ParametrosQueryBusquedaAvanzada parametro2 = null;
                ParametrosQueryBusquedaAvanzada parametro3 = null;
                if (fechaInicialEducacionFormal != null) {
                    parametro2 = new ParametrosQueryBusquedaAvanzada("EDUCACIONFORMAL", "EDUCACIONFORMALDESDE", df.format(fechaInicialEducacionFormal).toString());
                    listaParametrosQueryModulos.add(parametro2);
                }
                if (fechaFinalEducacionFormal != null) {
                    parametro3 = new ParametrosQueryBusquedaAvanzada("EDUCACIONFORMAL", "EDUCACIONFORMALHASTA", df.format(fechaFinalEducacionFormal).toString());
                    listaParametrosQueryModulos.add(parametro3);
                }
            }
            if (vigenciaFormalBA.getInstitucion().getSecuencia() != null) {
                ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("EDUCACIONFORMAL", "INSTITUCION", vigenciaFormalBA.getInstitucion().getSecuencia().toString());
                listaParametrosQueryModulos.add(parametro);
            }
            if (vigenciaFormalBA.getProfesion().getSecuencia() != null) {
                ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("EDUCACIONFORMAL", "PROFESION", vigenciaFormalBA.getProfesion().getSecuencia().toString());
                listaParametrosQueryModulos.add(parametro);
            }
            if (desarrolladoEducacionFormal != null) {
                ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("EDUCACIONFORMAL", "REALIZADO", desarrolladoEducacionFormal.toString());
                listaParametrosQueryModulos.add(parametro);
            }
        }
    }

    public void cargueParametrosModuloEducacionNoFormal() {
        if (tabActivaEducacionNoFormal == 1) {
            if (tipoFechaEducacionNoFormal == 1) {
                ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("EDUCACIONNOFORMAL", "BEDUCACIONNOFORMAL", "A");
                listaParametrosQueryModulos.add(parametro);
            }
            if (tipoFechaEducacionNoFormal == 2) {
                ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("EDUCACIONNOFORMAL", "BEDUCACIONNOFORMAL", "H");
                DateFormat df = DateFormat.getDateInstance();
                ParametrosQueryBusquedaAvanzada parametro2 = null;
                ParametrosQueryBusquedaAvanzada parametro3 = null;
                listaParametrosQueryModulos.add(parametro);
                if (fechaInicialEducacionNoFormal != null) {
                    parametro2 = new ParametrosQueryBusquedaAvanzada("EDUCACIONNOFORMAL", "EDUCACIONNOFORMALDESDE", df.format(fechaInicialEducacionNoFormal).toString());
                    listaParametrosQueryModulos.add(parametro2);
                }
                if (fechaFinalEducacionNoFormal != null) {
                    parametro3 = new ParametrosQueryBusquedaAvanzada("EDUCACIONNOFORMAL", "EDUCACIONNOFORMALHASTA", df.format(fechaFinalEducacionNoFormal).toString());
                    listaParametrosQueryModulos.add(parametro3);
                }
            }
            if (vigenciaNoFormalBA.getInstitucion().getSecuencia() != null) {
                ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("EDUCACIONNOFORMAL", "INSTITUCION", vigenciaNoFormalBA.getInstitucion().getSecuencia().toString());
                listaParametrosQueryModulos.add(parametro);
            }
            if (vigenciaNoFormalBA.getCurso().getSecuencia() != null) {
                ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("EDUCACIONNOFORMAL", "CURSO", vigenciaNoFormalBA.getCurso().getSecuencia().toString());
                listaParametrosQueryModulos.add(parametro);
            }
            if (desarrolladoEducacionNoFormal != null) {
                ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("EDUCACIONNOFORMAL", "REALIZADO", desarrolladoEducacionNoFormal.toString());
                listaParametrosQueryModulos.add(parametro);
            }
        }
    }

    public void cargueParametrosModuloCargoPostularse() {
        if (tabActivaCargoPostularse == 1) {

            ParametrosQueryBusquedaAvanzada parametroInicial = new ParametrosQueryBusquedaAvanzada("CARGOPOSTULARSE", "NN", "NN");
            listaParametrosQueryModulos.add(parametroInicial);

            if (vigenciaCargoPersonalBA.getCargo().getSecuencia() != null) {
                ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("CARGOPOSTULARSE", "CARGO", vigenciaCargoPersonalBA.getCargo().getSecuencia().toString());
                listaParametrosQueryModulos.add(parametro);
            }
        }
    }

    public void cargueParametrosModuloProyecto() {
        if (tabActivaProyecto == 1) {

            ParametrosQueryBusquedaAvanzada parametroInicial = new ParametrosQueryBusquedaAvanzada("PROYECTO", "NN", "NN");
            listaParametrosQueryModulos.add(parametroInicial);

            if (vigenciaProyectoBA.getProyecto().getSecuencia() != null) {
                ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("PROYECTO", "PROYECTO", vigenciaProyectoBA.getProyecto().getSecuencia().toString());
                listaParametrosQueryModulos.add(parametro);
            }
            if (vigenciaProyectoBA.getPryRol().getSecuencia() != null) {
                ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("PROYECTO", "ROL", vigenciaProyectoBA.getPryRol().getSecuencia().toString());
                listaParametrosQueryModulos.add(parametro);
            }
            if (fechaInicialProyecto != null) {
                DateFormat df = DateFormat.getDateInstance();
                ParametrosQueryBusquedaAvanzada parametro2 = new ParametrosQueryBusquedaAvanzada("PROYECTO", "PROYECTODESDE", df.format(fechaInicialProyecto).toString());
                listaParametrosQueryModulos.add(parametro2);
            }
            if (fechaInicialProyecto != null) {
                DateFormat df = DateFormat.getDateInstance();
                ParametrosQueryBusquedaAvanzada parametro3 = new ParametrosQueryBusquedaAvanzada("PROYECTO", "PROYECTOHASTA", df.format(fechaInicialProyecto).toString());
                listaParametrosQueryModulos.add(parametro3);
            }
        }
    }

    public void cargueParametrosModuloExperienciaLaboral() {
        if (tabActivaExperienciaLaboral == 1) {

            ParametrosQueryBusquedaAvanzada parametroInicial = new ParametrosQueryBusquedaAvanzada("EXPERIENCIALABORAL", "NN", "NN");
            listaParametrosQueryModulos.add(parametroInicial);

            if (!cargoExperienciaLaboral.isEmpty()) {
                ParametrosQueryBusquedaAvanzada parametro2 = new ParametrosQueryBusquedaAvanzada("EXPERIENCIALABORAL", "CARGO", cargoExperienciaLaboral.toString());
                listaParametrosQueryModulos.add(parametro2);
            }
            if (!empresaExperienciaLaboral.isEmpty()) {
                ParametrosQueryBusquedaAvanzada parametro3 = new ParametrosQueryBusquedaAvanzada("EXPERIENCIALABORAL", "EMPRESA", empresaExperienciaLaboral.toString());
                listaParametrosQueryModulos.add(parametro3);
            }
            if (hvExperienciaLaboralBA.getSectoreconomico().getSecuencia() != null) {
                ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("EXPERIENCIALABORAL", "SECTORECONOMICO", hvExperienciaLaboralBA.getSectoreconomico().getSecuencia().toString());
                listaParametrosQueryModulos.add(parametro);
            }
            if (hvExperienciaLaboralBA.getMotivoretiro().getSecuencia() != null) {
                ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("EXPERIENCIALABORAL", "MOTIVORETIRO", hvExperienciaLaboralBA.getMotivoretiro().getSecuencia().toString());
                listaParametrosQueryModulos.add(parametro);
            }
            if (fechaInicialExperienciaLaboral != null) {
                DateFormat df = DateFormat.getDateInstance();
                ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("EXPERIENCIALABORAL", "EXPERIENCIALABORALDESDE", df.format(fechaInicialExperienciaLaboral).toString());
                listaParametrosQueryModulos.add(parametro);
            }
            if (fechaFinalExperienciaLaboral != null) {
                DateFormat df = DateFormat.getDateInstance();
                ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("EXPERIENCIALABORAL", "EXPERIENCIALABORALHASTA", df.format(fechaFinalExperienciaLaboral).toString());
                listaParametrosQueryModulos.add(parametro);
            }
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void salir() {  limpiarListasValor();
        cancelarModificaciones();
        //PERSONAL
        lovProyectos = null;
        proyectoSeleccionado = new Proyectos();
        lovPryRoles = null;
        pryRolSeleccionado = new PryRoles();
        lovSectoresEconomicos = null;
        sectorEconomicoSeleccionado = new SectoresEconomicos();
        lovCursos = null;
        cursoSeleccionado = new Cursos();
        lovInstituciones = null;
        institucionSeleccionada = new Instituciones();
        lovProfesiones = null;
        profesionSeleccionada = new Profesiones();
        lovTiposIndicadores = null;
        tipoIndicadorSeleccionado = new TiposIndicadores();
        lovIndicadores = null;
        indicadorSeleccionado = new Indicadores();
        lovIdiomas = null;
        idiomaSeleccionado = new Idiomas();
        lovEstadosCiviles = null;
        estadoCivilSeleccionado = new EstadosCiviles();
        lovCiudades = null;
        ciudadSeleccionada = new Ciudades();
        //NOMINA
        lovMotivosRetiros = null;
        motivoRetiroSeleccionado = new MotivosRetiros();
        lovJornadasLaborales = null;
        jornadaLaboralSeleccionada = new JornadasLaborales();
        lovMotivosMvrs = null;
        motivoMvrsSeleccionado = new Motivosmvrs();
        lovPeriodicidades = null;
        periodicidadSeleccionada = new Periodicidades();
        lovSucursales = null;
        sucursalSeleccionada = new Sucursales();
        lovEstadosAfiliaciones = null;
        estadoAfiliacionSeleccionado = new EstadosAfiliaciones();
        lovTercerosSucursales = null;
        terceroSucursalSeleccionado = new TercerosSucursales();
        lovTiposEntidades = null;
        tipoEntidadSeleccionado = new TiposEntidades();
        lovUbicacionesGeograficas = null;
        ubicacionGeograficaSeleccionada = new UbicacionesGeograficas();
        lovContratos = null;
        contratoSeleccionado = new Contratos();
        lovNormasLaborales = null;
        normaLaboralSeleccionada = new NormasLaborales();
        lovReformasLaborales = null;
        reformaLaboralSeleccionada = new ReformasLaborales();
        lovMotivosContratos = null;
        motivoContratoSeleccionado = new MotivosContratos();
        lovTiposSueldos = null;
        tipoSueldoSeleccionado = new TiposSueldos();
        lovMotivosCambiosSueldos = null;
        motivoSueldoSeleccionado = new MotivosCambiosSueldos();
        lovTiposSueldos = null;
        tipoSueldoSeleccionado = new TiposSueldos();
        lovMotivosLocalizaciones = null;
        motivoLocalizacionSeleccionado = new MotivosLocalizaciones();
        lovEstructuras = null;
        estructuraSeleccionada = new Estructuras();
        lovCargos = null;
        cargoSeleccionado = new Cargos();
        lovEmpleados = null;
        empleadoSeleccionado = new Empleados();
        lovPapeles = null;
        papelSeleccionado = new Papeles();
        lovMotivosCambiosCargos = null;
        motivoCambioSeleccionado = new MotivosCambiosCargos();
        lovTiposTrabajadores = null;
        tipoTrabajadorSeleccionado = new TiposTrabajadores();
        //OTROS
        aceptar = true;
        auxSueldoMinimoSueldo = null;
        auxSueldoMaximoSueldo = null;
        auxSueldoMaximoMvrs = null;
        auxSueldoMinimoMvrs = null;
        auxPromedioMaximoSets = null;
        auxPromedioMinimoSets = null;
        conversacionDesde = null;
        conversacionHasta = null;
        lecturaDesde = null;
        lecturaHasta = null;
        escrituraDesde = null;
        escrituraHasta = null;
        tipoMetodoSets = "";
        //Resulatos de busqueda
        nuevaColumna = "";
        columnTemplate = "Codigo Primer_Apellido Segundo_Apellido Nombre ";
        mapValoresColumnas = new HashMap<String, String>();
        listaResultadoBusquedaAvanzada = new ArrayList<ResultadoBusquedaAvanzada>();
        listaCodigosEmpleado = new ArrayList<BigInteger>();
        numeroTipoBusqueda = 0;
        cabeceraEditarCelda = "";
        infoVariableEditarCelda = "";
        indice = -1;
        cualCelda = -1;
        listaColumnasBusquedaAvanzada = new ArrayList<ColumnasBusquedaAvanzada>();
        registros = new ArrayList<QVWEmpleadosCorte>();
        listaColumnasEscenarios = null;
        auxTabActivoCentroCosto = false;
        //
        listaEmpleadosResultados = new ArrayList<Empleados>();
        listaParametrosQueryModulos = new ArrayList<ParametrosQueryBusquedaAvanzada>();
    }

    public void cancelarModificaciones() {
        //
        indice = -1;
        cualCelda = -1;
        //
        RequestContext context = RequestContext.getCurrentInstance();
        if (numeroTipoBusqueda == 0) {
            vigenciaCargoBA = new VigenciasCargos();
            vigenciaCargoBA.setEstructura(new Estructuras());
            vigenciaCargoBA.getEstructura().setCentrocosto(new CentrosCostos());
            vigenciaCargoBA.setMotivocambiocargo(new MotivosCambiosCargos());
            vigenciaCargoBA.setPapel(new Papeles());
            vigenciaCargoBA.setCargo(new Cargos());
            vigenciaCargoBA.setEmpleadojefe(new Empleados());
            vigenciaLocalizacionBA = new VigenciasLocalizaciones();
            vigenciaLocalizacionBA.setLocalizacion(new Estructuras());
            vigenciaLocalizacionBA.setMotivo(new MotivosLocalizaciones());
            vigenciaSueldoBA = new VigenciasSueldos();
            vigenciaSueldoBA.setTiposueldo(new TiposSueldos());
            vigenciaSueldoBA.setMotivocambiosueldo(new MotivosCambiosSueldos());
            vigenciaTipoContratoBA = new VigenciasTiposContratos();
            vigenciaTipoContratoBA.setTipocontrato(new TiposContratos());
            vigenciaTipoContratoBA.setMotivocontrato(new MotivosContratos());
            vigenciaTipoTrabajadorBA = new VigenciasTiposTrabajadores();
            vigenciaTipoTrabajadorBA.setTipotrabajador(new TiposTrabajadores());
            vigenciaReformaLaboralBA = new VigenciasReformasLaborales();
            vigenciaReformaLaboralBA.setReformalaboral(new ReformasLaborales());
            vigenciaNormaEmpleadoBA = new VigenciasNormasEmpleados();
            vigenciaNormaEmpleadoBA.setNormalaboral(new NormasLaborales());
            vigenciaContratoBA = new VigenciasContratos();
            vigenciaContratoBA.setContrato(new Contratos());
            vigenciaUbicacionBA = new VigenciasUbicaciones();
            vigenciaUbicacionBA.setUbicacion(new UbicacionesGeograficas());
            vigenciaUbicacionBA.getUbicacion().setCiudad(new Ciudades());
            vigenciaAfiliacionBA = new VigenciasAfiliaciones();
            vigenciaAfiliacionBA.setTercerosucursal(new TercerosSucursales());
            vigenciaAfiliacionBA.setTipoentidad(new TiposEntidades());
            vigenciaAfiliacionBA.setEstadoafiliacion(new EstadosAfiliaciones());
            vigenciaFormaPagoBA = new VigenciasFormasPagos();
            vigenciaFormaPagoBA.setFormapago(new Periodicidades());
            vigenciaFormaPagoBA.setSucursal(new Sucursales());
            mvrsBA = new Mvrs();
            mvrsBA.setMotivo(new Motivosmvrs());
            vigenciaJornadaBA = new VigenciasJornadas();
            vigenciaJornadaBA.setJornadatrabajo(new JornadasLaborales());
            motivoRetiroBA = new MotivosRetiros();
            //
            auxTabActivoCentroCosto = false;
            tabActivaFechaContrato = 0;
            tabActivaCargos = 0;
            tabActivaCentroCosto = 0;
            tabActivaSueldo = 0;
            tabActivaTipoTrabajador = 0;
            tabActivaTipoSalario = 0;
            tabActivaNormaLaboral = 0;
            tabActivaLegislacionLaboral = 0;
            tabActivaUbicacion = 0;
            tabActivaAfiliacion = 0;
            tabActivaFormaPago = 0;
            tabActivaMvrs = 0;
            tabActivaSets = 0;
            tabActivaVacacion = 0;
            tabActivaJornadaLaboral = 0;
            tabActivaFechaRetiro = 0;
            //
            auxFechaFinalFechaContrato = null;
            fechaFinalFechaContrato = null;
            auxFechaInicialFechaContrato = null;
            fechaInicialFechaContrato = null;
            auxFechaFinalCargo = null;
            fechaFinalCargo = null;
            auxFechaInicialCargo = null;
            fechaInicialCargo = null;
            auxFechaFinalCentroCosto = null;
            fechaFinalCentroCosto = null;
            auxFechaInicialCentroCosto = null;
            fechaInicialCentroCosto = null;
            auxFechaFinalSueldo = null;
            fechaFinalSueldo = null;
            auxFechaInicialSueldo = null;
            fechaInicialSueldo = null;
            auxFechaInicialTipoTrabajador = null;
            fechaInicialTipoTrabajador = null;
            auxFechaFinalTipoTrabajador = null;
            fechaFinalTipoTrabajador = null;
            auxFechaFinalTipoSalario = null;
            fechaFinalTipoSalario = null;
            auxFechaInicialTipoSalario = null;
            fechaInicialTipoSalario = null;
            auxFechaFinalNormaLaboral = null;
            fechaFinalNormaLaboral = null;
            auxFechaInicialNormaLaboral = null;
            fechaInicialNormaLaboral = null;
            auxFechaMIInicialLegislacionLaboral = null;
            auxFechaMIFinalLegislacionLaboral = null;
            auxFechaMFInicialLegislacionLaboral = null;
            auxFechaMFFinalLegislacionLaboral = null;
            fechaMFFinalLegislacionLaboral = null;
            fechaMFInicialLegislacionLaboral = null;
            fechaMIFinalLegislacionLaboral = null;
            fechaMIInicialLegislacionLaboral = null;
            auxFechaFinalUbicacion = null;
            fechaFinalUbicacion = null;
            auxFechaInicialUbicacion = null;
            fechaInicialUbicacion = null;
            auxFechaInicialAfiliacion = null;
            fechaInicialAfiliacion = null;
            auxFechaFinalAfiliacion = null;
            fechaFinalAfiliacion = null;
            auxFechaFinalFormaPago = null;
            fechaFinalFormaPago = null;
            auxFechaInicialFormaPago = null;
            fechaInicialFormaPago = null;
            auxFechaInicialMvrs = null;
            fechaInicialMvrs = null;
            auxFechaFinalMvrs = null;
            fechaFinalMvrs = null;
            auxFechaMFInicialSets = null;
            fechaMFInicialSets = null;
            auxFechaMFFinalSets = null;
            fechaMFFinalSets = null;
            auxFechaMIFinalSets = null;
            fechaMIFinalSets = null;
            auxFechaMIInicialSets = null;
            fechaMIInicialSets = null;
            auxFechaMFInicialVacacion = null;
            fechaMFInicialVacacion = null;
            auxFechaMFFinalVacacion = null;
            fechaMFFinalVacacion = null;
            auxFechaMIFinalVacacion = null;
            fechaMIFinalVacacion = null;
            auxFechaMIInicialVacacion = null;
            fechaMIInicialVacacion = null;
            auxFechaFinalJornadaLaboral = null;
            fechaFinalJornadaLaboral = null;
            auxFechaInicialJornadaLaboral = null;
            fechaInicialJornadaLaboral = null;
            auxFechaFinalFechaRetiro = null;
            fechaFinalFechaRetiro = null;
            auxFechaInicialFechaRetiro = null;
            fechaInicialFechaRetiro = null;
            //
            sueldoMaxSueldo = null;
            sueldoMinSueldo = null;
            auxSueldoMaximoSueldo = null;
            auxSueldoMinimoSueldo = null;
            sueldoMaxMvrs = null;
            sueldoMinMvrs = null;
            auxSueldoMaximoMvrs = null;
            auxSueldoMinimoMvrs = null;
            auxPromedioMaximoSets = null;
            promedioMaximoSets = null;
            auxPromedioMinimoSets = null;
            promedioMinimoSets = null;
            //
            tipoMetodoSets = "";
            //
            tipoFechaCargo = 1;
            activarCasillasFechasCargo();
            tipoFechaCentroCosto = 1;
            activarCasillasFechasCentroCosto();
            tipoFechaSueldo = 1;
            activarCasillasFechasSueldo();
            tipoFechaFechaContrato = 1;
            activarCasillasFechasFechaContrato();
            tipoFechaTipoTrabajador = 1;
            activarCasillasFechasTipoTrabajador();
            tipoFechaTipoSalario = 1;
            activarCasillasFechasTipoSalario();
            tipoFechaNormaLaboral = 1;
            activarCasillasFechasNormaLaboral();
            tipoFechaLegislacionLaboral = 1;
            activarCasillasFechasLegislacionLaboral();
            tipoFechaUbicacion = 1;
            activarCasillasFechasUbicacion();
            tipoFechaAfiliacion = 1;
            activarCasillasFechasAfiliacion();
            tipoFechaFormaPago = 1;
            activarCasillasFechasFormaPago();
            tipoFechaMvrs = 1;
            activarCasillasFechasMvrs();
            tipoFechaSets = 1;
            activarCasillasFechasSets();
            //
            RequestContext.getCurrentInstance().update("form:tabViewCosto");
            RequestContext.getCurrentInstance().update("form:tabViewCentroCosto");
            RequestContext.getCurrentInstance().update("form:tabViewSueldo");
            RequestContext.getCurrentInstance().update("form:tabViewFechaContrato");
            RequestContext.getCurrentInstance().update("form:tabViewTipoTrabajador");
            RequestContext.getCurrentInstance().update("form:tabViewTipoSalario");
            RequestContext.getCurrentInstance().update("form:tabViewNormaLaboral");
            RequestContext.getCurrentInstance().update("form:tabViewLegislacionLaboral");
            RequestContext.getCurrentInstance().update("form:tabViewUbicacion");
            RequestContext.getCurrentInstance().update("form:tabViewAfiliacion");
            RequestContext.getCurrentInstance().update("form:tabViewFormaPago");
            RequestContext.getCurrentInstance().update("form:tabViewMvrs");
            RequestContext.getCurrentInstance().update("form:tabViewSets");
            RequestContext.getCurrentInstance().update("form:tabViewVacacion");
            RequestContext.getCurrentInstance().update("form:tabViewJornadaLaboral");
            RequestContext.getCurrentInstance().update("form:tabViewFechaRetiro");
        }
        if (numeroTipoBusqueda == 1) {
            empleadoBA = new Empleados();
            empleadoBA.setPersona(new Personas());
            empleadoBA.getPersona().setCiudaddocumento(new Ciudades());
            empleadoBA.getPersona().setCiudadnacimiento(new Ciudades());
            estadoCivilBA = new EstadosCiviles();
            idiomaPersonaBA = new IdiomasPersonas();
            idiomaPersonaBA.setIdioma(new Idiomas());
            vigenciaIndicadorBA = new VigenciasIndicadores();
            vigenciaIndicadorBA.setTipoindicador(new TiposIndicadores());
            vigenciaIndicadorBA.setIndicador(new Indicadores());
            vigenciaFormalBA = new VigenciasFormales();
            vigenciaFormalBA.setProfesion(new Profesiones());
            vigenciaFormalBA.setInstitucion(new Instituciones());
            desarrolladoEducacionFormal = null;
            vigenciaNoFormalBA = new VigenciasNoFormales();
            vigenciaNoFormalBA.setCurso(new Cursos());
            vigenciaNoFormalBA.setInstitucion(new Instituciones());
            desarrolladoEducacionNoFormal = null;
            hvExperienciaLaboralBA = new HvExperienciasLaborales();
            hvExperienciaLaboralBA.setMotivoretiro(new MotivosRetiros());
            hvExperienciaLaboralBA.setSectoreconomico(new SectoresEconomicos());
            empresaExperienciaLaboral = "";
            cargoExperienciaLaboral = "";
            vigenciaProyectoBA = new VigenciasProyectos();
            vigenciaProyectoBA.setProyecto(new Proyectos());
            vigenciaProyectoBA.setPryRol(new PryRoles());
            vigenciaCargoPersonalBA = new VigenciasCargos();
            vigenciaCargoPersonalBA.setCargo(new Cargos());
            //
            fechaInicialDatosPersonales = null;
            auxFechaInicialDatosPersonales = null;
            fechaFinalDatosPersonales = null;
            auxFechaFinalDatosPersonales = null;
            fechaInicialEstadoCivil = null;
            auxFechaInicialEstadoCivil = null;
            fechaFinalEstadoCivil = null;
            auxFechaFinalEstadoCivil = null;
            fechaInicialCenso = null;
            auxFechaInicialCenso = null;
            fechaFinalCenso = null;
            auxFechaFinalCenso = null;
            fechaInicialEducacionFormal = null;
            auxFechaInicialEducacionFormal = null;
            fechaFinalEducacionFormal = null;
            auxFechaFinalEducacionFormal = null;
            fechaInicialEducacionNoFormal = null;
            auxFechaInicialEducacionNoFormal = null;
            fechaFinalEducacionNoFormal = null;
            auxFechaFinalEducacionNoFormal = null;
            fechaInicialExperienciaLaboral = null;
            auxFechaInicialExperienciaLaboral = null;
            fechaFinalExperienciaLaboral = null;
            auxFechaFinalExperienciaLaboral = null;
            fechaInicialProyecto = null;
            auxFechaInicialProyecto = null;
            fechaFinalProyecto = null;
            auxFechaFinalProyecto = null;
            //
            conversacionDesde = null;
            conversacionHasta = null;
            lecturaDesde = null;
            lecturaHasta = null;
            escrituraDesde = null;
            escrituraHasta = null;
            auxConversacionDesde = null;
            auxConversacionHasta = null;
            auxLecturaDesde = null;
            auxLecturaHasta = null;
            auxEscrituraDesde = null;
            auxEscrituraHasta = null;
            //
            tabActivaCenso = 0;
            tabActivaEstadoCivil = 0;
            tabActivaFactorRH = 0;
            tabActivaDatosPersonales = 0;
            tabActivaIdioma = 0;
            tabActivaEducacionFormal = 0;
            tabActivaEducacionNoFormal = 0;
            tabActivaExperienciaLaboral = 0;
            tabActivaProyecto = 0;
            tabActivaCargoPostularse = 0;
            //
            tipoFechaCenso = 1;
            activarCasillasFechasCenso();
            tipoFechaEstadoCivil = 1;
            activarCasillasFechasEstadoCivil();
            tipoFechaEducacionFormal = 1;
            activarCasillasFechasEducacionFormal();
            tipoFechaEducacionNoFormal = 1;
            activarCasillasFechasEducacionNoFormal();
            //
            RequestContext.getCurrentInstance().update("form:tabViewCargoPostularse");
            RequestContext.getCurrentInstance().update("form:tabViewProyecto");
            RequestContext.getCurrentInstance().update("form:tabViewExperienciaLaboral");
            RequestContext.getCurrentInstance().update("form:tabViewEducacionNoFormal");
            RequestContext.getCurrentInstance().update("form:tabViewEducacionFormal");
            RequestContext.getCurrentInstance().update("form:tabViewCenso");
            RequestContext.getCurrentInstance().update("form:tabViewIdioma");
            RequestContext.getCurrentInstance().update("form:tabViewEstadoCivil");
            RequestContext.getCurrentInstance().update("form:tabViewFactorRH");
            RequestContext.getCurrentInstance().update("form:tabViewDatosPersonales");
        }
        restaurar();
        //

        //
    }

    public void editarCelda() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (indice >= 0) {
            String[] columnas = columnTemplate.split(" ");
            cabeceraEditarCelda = columnas[cualCelda];
            ColumnasBusquedaAvanzada columna = null;
            if (tipoLista == 0) {
                columna = listaColumnasBusquedaAvanzada.get(indice);
            } else {
                columna = filtrarListaColumnasBusquedaAvanzada.get(indice);
            }
            if (cualCelda == 0) {
                infoVariableEditarCelda = columna.getCodigo();
            }
            if (cualCelda == 1) {
                infoVariableEditarCelda = columna.getPrimeroApellido();
            }
            if (cualCelda == 2) {
                infoVariableEditarCelda = columna.getSegundoApellido();
            }
            if (cualCelda == 3) {
                infoVariableEditarCelda = columna.getNombre();
            }
            if (cualCelda == 4) {
                infoVariableEditarCelda = columna.getColumna0();
            }
            if (cualCelda == 5) {
                infoVariableEditarCelda = columna.getColumna1();
            }
            if (cualCelda == 6) {
                infoVariableEditarCelda = columna.getColumna2();
            }
            if (cualCelda == 7) {
                infoVariableEditarCelda = columna.getColumna3();
            }
            if (cualCelda == 8) {
                infoVariableEditarCelda = columna.getColumna4();
            }
            if (cualCelda == 8) {
                infoVariableEditarCelda = columna.getColumna5();
            }
            if (cualCelda == 10) {
                infoVariableEditarCelda = columna.getColumna6();
            }
            if (cualCelda == 11) {
                infoVariableEditarCelda = columna.getColumna7();
            }
            if (cualCelda == 12) {
                infoVariableEditarCelda = columna.getColumna8();
            }
            if (cualCelda == 13) {
                infoVariableEditarCelda = columna.getColumna9();
            }
            RequestContext.getCurrentInstance().update("formularioDialogos:editarTablaBusquedaAvanzada");
            RequestContext.getCurrentInstance().execute("PF('editarTablaBusquedaAvanzada').show()");
        } else {
            if (numeroTipoBusqueda == 0) {
                if (casillaVigenciaCargo >= 0) {
                    if (casillaVigenciaCargo == 0) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarCargoModCargo");
                        RequestContext.getCurrentInstance().execute("PF('editarCargoModCargo').show()");
                        casillaVigenciaCargo = -1;
                    } else if (casillaVigenciaCargo == 1) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarEstructuraModCargo");
                        RequestContext.getCurrentInstance().execute("PF('editarEstructuraModCargo').show()");
                        casillaVigenciaCargo = -1;
                    } else if (casillaVigenciaCargo == 2) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarJefeModCargo");
                        RequestContext.getCurrentInstance().execute("PF('editarJefeModCargo').show()");
                        casillaVigenciaCargo = -1;
                    } else if (casillaVigenciaCargo == 3) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarMotivoModCargo");
                        RequestContext.getCurrentInstance().execute("PF('editarMotivoModCargo').show()");
                        casillaVigenciaCargo = -1;
                    } else if (casillaVigenciaCargo == 4) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarCentroCostoModCargo");
                        RequestContext.getCurrentInstance().execute("PF('editarCentroCostoModCargo').show()");
                        casillaVigenciaCargo = -1;
                    } else if (casillaVigenciaCargo == 5) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarPapelModCargo");
                        RequestContext.getCurrentInstance().execute("PF('editarPapelModCargo').show()");
                        casillaVigenciaCargo = -1;
                    } else if (casillaVigenciaCargo == 6) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaInicialModCargo");
                        RequestContext.getCurrentInstance().execute("PF('editarFechaInicialModCargo').show()");
                        casillaVigenciaCargo = -1;
                    } else if (casillaVigenciaCargo == 7) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaFinalModCargo");
                        RequestContext.getCurrentInstance().execute("PF('editarFechaFinalModCargo').show()");
                        casillaVigenciaCargo = -1;
                    }
                }
                if (casillaVigenciaLocalizacion >= 0) {
                    if (casillaVigenciaLocalizacion == 0) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarLocalizacionModCentroCosto");
                        RequestContext.getCurrentInstance().execute("PF('editarLocalizacionModCentroCosto').show()");
                        casillaVigenciaLocalizacion = -1;
                    } else if (casillaVigenciaLocalizacion == 1) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarMotivoModCentroCosto");
                        RequestContext.getCurrentInstance().execute("PF('editarMotivoModCentroCosto').show()");
                        casillaVigenciaLocalizacion = -1;
                    } else if (casillaVigenciaLocalizacion == 2) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaInicialModCentroCosto");
                        RequestContext.getCurrentInstance().execute("PF('editarFechaInicialModCentroCosto').show()");
                        casillaVigenciaLocalizacion = -1;
                    } else if (casillaVigenciaLocalizacion == 3) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaFinalModCentroCosto");
                        RequestContext.getCurrentInstance().execute("PF('editarFechaFinalModCentroCosto').show()");
                        casillaVigenciaLocalizacion = -1;
                    }
                }
                if (casillaVigenciaSueldo >= 0) {
                    if (casillaVigenciaSueldo == 0) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarTipoSueldoModSueldo");
                        RequestContext.getCurrentInstance().execute("PF('editarTipoSueldoModSueldo').show()");
                        casillaVigenciaSueldo = -1;
                    } else if (casillaVigenciaSueldo == 1) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarMotivoModSueldo");
                        RequestContext.getCurrentInstance().execute("PF('editarMotivoModSueldo').show()");
                        casillaVigenciaSueldo = -1;
                    } else if (casillaVigenciaSueldo == 2) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarSueldoMinimoModSueldo");
                        RequestContext.getCurrentInstance().execute("PF('editarSueldoMinimoModSueldo').show()");
                        casillaVigenciaSueldo = -1;
                    } else if (casillaVigenciaSueldo == 3) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarSueldoMaximoModSueldo");
                        RequestContext.getCurrentInstance().execute("PF('editarSueldoMaximoModSueldo').show()");
                        casillaVigenciaSueldo = -1;
                    } else if (casillaVigenciaSueldo == 4) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaInicialModSueldo");
                        RequestContext.getCurrentInstance().execute("PF('editarFechaInicialModSueldo').show()");
                        casillaVigenciaSueldo = -1;
                    } else if (casillaVigenciaSueldo == 5) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaFinalModSueldo");
                        RequestContext.getCurrentInstance().execute("PF('editarFechaFinalModSueldo').show()");
                        casillaVigenciaSueldo = -1;
                    }
                }
                if (casillaVigenciaTipoContrato >= 0) {
                    if (casillaVigenciaTipoContrato == 0) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarTipoContratoModFechaContrato");
                        RequestContext.getCurrentInstance().execute("PF('editarTipoContratoModFechaContrato').show()");
                        casillaVigenciaTipoContrato = -1;
                    } else if (casillaVigenciaTipoContrato == 1) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarMotivoModFechaContrato");
                        RequestContext.getCurrentInstance().execute("PF('editarMotivoModFechaContrato').show()");
                        casillaVigenciaTipoContrato = -1;
                    } else if (casillaVigenciaTipoContrato == 2) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaInicialModFechaContrato");
                        RequestContext.getCurrentInstance().execute("PF('editarFechaInicialModFechaContrato').show()");
                        casillaVigenciaTipoContrato = -1;
                    } else if (casillaVigenciaTipoContrato == 3) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaFinalModFechaContrato");
                        RequestContext.getCurrentInstance().execute("PF('editarFechaFinalModFechaContrato').show()");
                        casillaVigenciaTipoContrato = -1;
                    }
                }
                if (casillaVigenciaTipoTrabajador >= 0) {
                    if (casillaVigenciaTipoTrabajador == 0) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarTipoTrabajadorModTipoTrabajador");
                        RequestContext.getCurrentInstance().execute("PF('editarTipoTrabajadorModTipoTrabajador').show()");
                        casillaVigenciaTipoTrabajador = -1;
                    } else if (casillaVigenciaTipoTrabajador == 1) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaInicialModTipoTrabajador");
                        RequestContext.getCurrentInstance().execute("PF('editarFechaInicialModTipoTrabajador').show()");
                        casillaVigenciaTipoTrabajador = -1;
                    } else if (casillaVigenciaTipoTrabajador == 2) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaFinalModTipoTrabajador");
                        RequestContext.getCurrentInstance().execute("PF('editarFechaFinalModTipoTrabajador').show()");
                        casillaVigenciaTipoTrabajador = -1;
                    }
                }
                if (casillaVigenciaReformaLaboral >= 0) {
                    if (casillaVigenciaReformaLaboral == 0) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarReformaLaboralModTipoSalario");
                        RequestContext.getCurrentInstance().execute("PF('editarReformaLaboralModTipoSalario').show()");
                        casillaVigenciaReformaLaboral = -1;
                    } else if (casillaVigenciaReformaLaboral == 1) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaInicialModTipoSalario");
                        RequestContext.getCurrentInstance().execute("PF('editarFechaInicialModTipoSalario').show()");
                        casillaVigenciaReformaLaboral = -1;
                    } else if (casillaVigenciaReformaLaboral == 2) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaFinalModTipoSalario");
                        RequestContext.getCurrentInstance().execute("PF('editarFechaFinalModTipoSalario').show()");
                        casillaVigenciaReformaLaboral = -1;
                    }
                }
                if (casillaVigenciaNormaLaboral >= 0) {
                    if (casillaVigenciaNormaLaboral == 0) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarNormaLaboralModNormaLaboral");
                        RequestContext.getCurrentInstance().execute("PF('editarNormaLaboralModNormaLaboral').show()");
                        casillaVigenciaNormaLaboral = -1;
                    } else if (casillaVigenciaNormaLaboral == 1) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaInicialModNormaLaboral");
                        RequestContext.getCurrentInstance().execute("PF('editarFechaInicialModNormaLaboral').show()");
                        casillaVigenciaNormaLaboral = -1;
                    } else if (casillaVigenciaNormaLaboral == 2) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaFinalModNormaLaboral");
                        RequestContext.getCurrentInstance().execute("PF('editarFechaFinalModNormaLaboral').show()");
                        casillaVigenciaNormaLaboral = -1;
                    }
                }
                if (casillaVigenciaContrato >= 0) {
                    if (casillaVigenciaContrato == 0) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarLegislacionModLegislacionLaboral");
                        RequestContext.getCurrentInstance().execute("PF('editarLegislacionModLegislacionLaboral').show()");
                        casillaVigenciaContrato = -1;
                    } else if (casillaVigenciaContrato == 1) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaMIInicialModLegislacionLaboral");
                        RequestContext.getCurrentInstance().execute("PF('editarFechaMIInicialModLegislacionLaboral').show()");
                        casillaVigenciaContrato = -1;
                    } else if (casillaVigenciaContrato == 2) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaMIFinalModLegislacionLaboral");
                        RequestContext.getCurrentInstance().execute("PF('editarFechaMIFinalModLegislacionLaboral').show()");
                        casillaVigenciaContrato = -1;
                    } else if (casillaVigenciaContrato == 3) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaMFInicialModLegislacionLaboral");
                        RequestContext.getCurrentInstance().execute("PF('editarFechaMFInicialModLegislacionLaboral').show()");
                        casillaVigenciaContrato = -1;
                    } else if (casillaVigenciaContrato == 4) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaMFFinalModLegislacionLaboral");
                        RequestContext.getCurrentInstance().execute("PF('editarFechaMFFinalModLegislacionLaboral').show()");
                        casillaVigenciaContrato = -1;
                    }
                }
                if (casillaVigenciaUbicacion >= 0) {
                    if (casillaVigenciaUbicacion == 0) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarUbicacionModUbicacion");
                        RequestContext.getCurrentInstance().execute("PF('editarUbicacionModUbicacion').show()");
                        casillaVigenciaUbicacion = -1;
                    } else if (casillaVigenciaUbicacion == 1) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarCiudadModUbicacion");
                        RequestContext.getCurrentInstance().execute("PF('editarCiudadModUbicacion').show()");
                        casillaVigenciaUbicacion = -1;
                    } else if (casillaVigenciaUbicacion == 2) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaInicialModUbicacion");
                        RequestContext.getCurrentInstance().execute("PF('editarFechaInicialModUbicacion').show()");
                        casillaVigenciaUbicacion = -1;
                    } else if (casillaVigenciaUbicacion == 3) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaFinalModUbicacion");
                        RequestContext.getCurrentInstance().execute("PF('editarFechaFinalModUbicacion').show()");
                        casillaVigenciaUbicacion = -1;
                    }
                }
                if (casillaVigenciaAfiliacion >= 0) {
                    if (casillaVigenciaAfiliacion == 0) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarTerceroModAfiliacion");
                        RequestContext.getCurrentInstance().execute("PF('editarTerceroModAfiliacion').show()");
                        casillaVigenciaAfiliacion = -1;
                    } else if (casillaVigenciaAfiliacion == 1) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarTipoEntidadModAfiliacion");
                        RequestContext.getCurrentInstance().execute("PF('editarTipoEntidadModAfiliacion').show()");
                        casillaVigenciaAfiliacion = -1;
                    } else if (casillaVigenciaAfiliacion == 2) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarEstadoModAfiliacion");
                        RequestContext.getCurrentInstance().execute("PF('editarEstadoModAfiliacion').show()");
                        casillaVigenciaAfiliacion = -1;
                    } else if (casillaVigenciaAfiliacion == 3) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaInicialModAfiliacion");
                        RequestContext.getCurrentInstance().execute("PF('editarFechaInicialModAfiliacion').show()");
                        casillaVigenciaAfiliacion = -1;
                    } else if (casillaVigenciaAfiliacion == 4) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaFinalModAfiliacion");
                        RequestContext.getCurrentInstance().execute("PF('editarFechaFinalModAfiliacion').show()");
                        casillaVigenciaAfiliacion = -1;
                    }
                }
                if (casillaVigenciaFormaPago >= 0) {
                    if (casillaVigenciaFormaPago == 0) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarFormaPagoModFormaPago");
                        RequestContext.getCurrentInstance().execute("PF('editarFormaPagoModFormaPago').show()");
                        casillaVigenciaFormaPago = -1;
                    } else if (casillaVigenciaFormaPago == 1) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarSucursalModFormaPago");
                        RequestContext.getCurrentInstance().execute("PF('editarSucursalModFormaPago').show()");
                        casillaVigenciaFormaPago = -1;
                    } else if (casillaVigenciaFormaPago == 2) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaInicialModFormaPago");
                        RequestContext.getCurrentInstance().execute("PF('editarFechaInicialModFormaPago').show()");
                        casillaVigenciaFormaPago = -1;
                    } else if (casillaVigenciaFormaPago == 3) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaFinalModFormaPago");
                        RequestContext.getCurrentInstance().execute("PF('editarFechaFinalModFormaPago').show()");
                        casillaVigenciaFormaPago = -1;
                    }
                }
                if (casillaMvrs >= 0) {
                    if (casillaMvrs == 0) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarMotivoModMvrs");
                        RequestContext.getCurrentInstance().execute("PF('editarMotivoModMvrs').show()");
                        casillaMvrs = -1;
                    } else if (casillaMvrs == 1) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarSueldoMinModMvrs");
                        RequestContext.getCurrentInstance().execute("PF('editarSueldoMinModMvrs').show()");
                        casillaMvrs = -1;
                    } else if (casillaMvrs == 2) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarSueldoMaxModMvrs");
                        RequestContext.getCurrentInstance().execute("PF('editarSueldoMaxModMvrs').show()");
                        casillaMvrs = -1;
                    } else if (casillaMvrs == 3) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaInicialModMvrs");
                        RequestContext.getCurrentInstance().execute("PF('editarFechaInicialModMvrs').show()");
                        casillaMvrs = -1;
                    } else if (casillaMvrs == 4) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaFinalModMvrs");
                        RequestContext.getCurrentInstance().execute("PF('editarFechaFinalModMvrs').show()");
                        casillaMvrs = -1;
                    }
                }
                if (casillaSets >= 0) {
                    if (casillaSets == 0) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarPromedioMinModSets");
                        RequestContext.getCurrentInstance().execute("PF('editarPromedioMinModSets').show()");
                        casillaSets = -1;
                    } else if (casillaSets == 1) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarPromedioMaxModSets");
                        RequestContext.getCurrentInstance().execute("PF('editarPromedioMaxModSets').show()");
                        casillaSets = -1;
                    } else if (casillaSets == 2) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaMIInicialModSets");
                        RequestContext.getCurrentInstance().execute("PF('editarFechaMIInicialModSets').show()");
                        casillaSets = -1;
                    } else if (casillaSets == 3) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaMIFinalModSets");
                        RequestContext.getCurrentInstance().execute("PF('editarFechaMIFinalModSets').show()");
                        casillaSets = -1;
                    } else if (casillaSets == 4) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaMFInicialModSets");
                        RequestContext.getCurrentInstance().execute("PF('editarFechaMFInicialModSets').show()");
                        casillaSets = -1;
                    } else if (casillaSets == 5) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaMFFinalModSets");
                        RequestContext.getCurrentInstance().execute("PF('editarFechaMFFinalModSets').show()");
                        casillaSets = -1;
                    }
                }
                if (casillaVacacion >= 0) {
                    if (casillaVacacion == 0) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaMIInicialModVacacion");
                        RequestContext.getCurrentInstance().execute("PF('editarFechaMIInicialModVacacion').show()");
                        casillaVacacion = -1;
                    } else if (casillaVacacion == 1) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaMIFinalModVacacion");
                        RequestContext.getCurrentInstance().execute("PF('editarFechaMIFinalModVacacion').show()");
                        casillaVacacion = -1;
                    } else if (casillaVacacion == 2) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaMFInicialModVacacion");
                        RequestContext.getCurrentInstance().execute("PF('editarFechaMFInicialModVacacion').show()");
                        casillaVacacion = -1;
                    } else if (casillaVacacion == 3) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaMFFinalModVacacion");
                        RequestContext.getCurrentInstance().execute("PF('editarFechaMFFinalModVacacion').show()");
                        casillaVacacion = -1;
                    }
                }
                if (casillaVigenciaJornadaLaboral >= 0) {
                    if (casillaVigenciaJornadaLaboral == 0) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarJornadaModJornadaLaboral");
                        RequestContext.getCurrentInstance().execute("PF('editarJornadaModJornadaLaboral').show()");
                        casillaVigenciaJornadaLaboral = -1;
                    } else if (casillaVigenciaJornadaLaboral == 1) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaInicialModJornadaLaboral");
                        RequestContext.getCurrentInstance().execute("PF('editarFechaInicialModJornadaLaboral').show()");
                        casillaVigenciaJornadaLaboral = -1;
                    } else if (casillaVigenciaJornadaLaboral == 2) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaFinalModJornadaLaboral");
                        RequestContext.getCurrentInstance().execute("PF('editarFechaFinalModJornadaLaboral').show()");
                        casillaVigenciaJornadaLaboral = -1;
                    }
                }
                if (casillaMotivoRetiro >= 0) {
                    if (casillaMotivoRetiro == 0) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarMotivoModFechaRetiro");
                        RequestContext.getCurrentInstance().execute("PF('editarMotivoModFechaRetiro').show()");
                        casillaMotivoRetiro = -1;
                    } else if (casillaMotivoRetiro == 1) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaInicialModFechaRetiro");
                        RequestContext.getCurrentInstance().execute("PF('editarFechaInicialModFechaRetiro').show()");
                        casillaMotivoRetiro = -1;
                    } else if (casillaMotivoRetiro == 2) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaFinalModFechaRetiro");
                        RequestContext.getCurrentInstance().execute("PF('editarFechaFinalModFechaRetiro').show()");
                        casillaMotivoRetiro = -1;
                    }
                }
            }
            if (numeroTipoBusqueda == 1) {
                if (casillaEmpleado >= 0) {
                    if (casillaEmpleado == 0) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarNumeroDocumentoModDatosPersonales");
                        RequestContext.getCurrentInstance().execute("PF('editarNumeroDocumentoModDatosPersonales').show()");
                        casillaEmpleado = -1;
                    } else if (casillaEmpleado == 1) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarCiudadDocumentoModDatosPersonales");
                        RequestContext.getCurrentInstance().execute("PF('editarCiudadDocumentoModDatosPersonales').show()");
                        casillaEmpleado = -1;
                    } else if (casillaEmpleado == 2) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarCiudadNacimientoModDatosPersonales");
                        RequestContext.getCurrentInstance().execute("PF('editarCiudadNacimientoModDatosPersonales').show()");
                        casillaEmpleado = -1;
                    } else if (casillaEmpleado == 3) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaInicialModDatosPersonales");
                        RequestContext.getCurrentInstance().execute("PF('editarFechaInicialModDatosPersonales').show()");
                        casillaEmpleado = -1;
                    } else if (casillaEmpleado == 4) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaFinalModDatosPersonales");
                        RequestContext.getCurrentInstance().execute("PF('editarFechaFinalModDatosPersonales').show()");
                        casillaEmpleado = -1;
                    }
                }
                if (casillaEstadoCivil >= 0) {
                    if (casillaEstadoCivil == 0) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarEstadoCivilModEstadoCivil");
                        RequestContext.getCurrentInstance().execute("PF('editarEstadoCivilModEstadoCivil').show()");
                        casillaEstadoCivil = -1;
                    } else if (casillaEstadoCivil == 1) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaInicialModEstadoCivil");
                        RequestContext.getCurrentInstance().execute("PF('editarFechaInicialModEstadoCivil').show()");
                        casillaEstadoCivil = -1;
                    } else if (casillaEstadoCivil == 2) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaFinalModEstadoCivil");
                        RequestContext.getCurrentInstance().execute("PF('editarFechaFinalModEstadoCivil').show()");
                        casillaEstadoCivil = -1;
                    }
                }
                if (casillaIdiomaPersona >= 0) {
                    if (casillaIdiomaPersona == 0) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarIdiomaModIdioma");
                        RequestContext.getCurrentInstance().execute("PF('editarIdiomaModIdioma').show()");
                        casillaIdiomaPersona = -1;
                    } else if (casillaIdiomaPersona == 1) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarConversacionDesdeModIdioma");
                        RequestContext.getCurrentInstance().execute("PF('editarConversacionDesdeModIdioma').show()");
                        casillaIdiomaPersona = -1;
                    } else if (casillaIdiomaPersona == 2) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarConversacionHastaModIdioma");
                        RequestContext.getCurrentInstance().execute("PF('editarConversacionHastaModIdioma').show()");
                        casillaIdiomaPersona = -1;
                    } else if (casillaIdiomaPersona == 3) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarLecturaDesdeModIdioma");
                        RequestContext.getCurrentInstance().execute("PF('editarLecturaDesdeModIdioma').show()");
                        casillaIdiomaPersona = -1;
                    } else if (casillaIdiomaPersona == 4) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarLecturaHastaModIdioma");
                        RequestContext.getCurrentInstance().execute("PF('editarLecturaHastaModIdioma').show()");
                        casillaIdiomaPersona = -1;
                    } else if (casillaIdiomaPersona == 5) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarEscrituraDesdeModIdioma");
                        RequestContext.getCurrentInstance().execute("PF('editarEscrituraDesdeModIdioma').show()");
                        casillaIdiomaPersona = -1;
                    } else if (casillaIdiomaPersona == 6) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarEscrituraHastaModIdioma");
                        RequestContext.getCurrentInstance().execute("PF('editarEscrituraHastaModIdioma').show()");
                        casillaIdiomaPersona = -1;
                    }
                }
                if (casillaVigenciaIndicador >= 0) {
                    if (casillaVigenciaIndicador == 0) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarTipoIndicadorModCenso");
                        RequestContext.getCurrentInstance().execute("PF('editarTipoIndicadorModCenso').show()");
                        casillaVigenciaIndicador = -1;
                    } else if (casillaVigenciaIndicador == 1) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarIndicadorModCenso");
                        RequestContext.getCurrentInstance().execute("PF('editarIndicadorModCenso').show()");
                        casillaVigenciaIndicador = -1;
                    } else if (casillaVigenciaIndicador == 2) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaInicialModCenso");
                        RequestContext.getCurrentInstance().execute("PF('editarFechaInicialModCenso').show()");
                        casillaVigenciaIndicador = -1;
                    } else if (casillaVigenciaIndicador == 3) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaFinalModCenso");
                        RequestContext.getCurrentInstance().execute("PF('editarFechaFinalModCenso').show()");
                        casillaVigenciaIndicador = -1;
                    }
                }
                if (casillaVigenciaFormal >= 0) {
                    if (casillaVigenciaFormal == 0) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarProfesionModEducacionFormal");
                        RequestContext.getCurrentInstance().execute("PF('editarProfesionModEducacionFormal').show()");
                        casillaVigenciaFormal = -1;
                    } else if (casillaVigenciaFormal == 1) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarInstitucionModEducacionFormal");
                        RequestContext.getCurrentInstance().execute("PF('editarInstitucionModEducacionFormal').show()");
                        casillaVigenciaFormal = -1;
                    } else if (casillaVigenciaFormal == 2) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaInicialModEducacionFormal");
                        RequestContext.getCurrentInstance().execute("PF('editarFechaInicialModEducacionFormal').show()");
                        casillaVigenciaFormal = -1;
                    } else if (casillaVigenciaFormal == 3) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaFinalModEducacionFormal");
                        RequestContext.getCurrentInstance().execute("PF('editarFechaFinalModEducacionFormal').show()");
                        casillaVigenciaFormal = -1;
                    }
                }
                if (casillaVigenciaNoFormal >= 0) {
                    if (casillaVigenciaNoFormal == 0) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarCursoModEducacionNoFormal");
                        RequestContext.getCurrentInstance().execute("PF('editarCursoModEducacionNoFormal').show()");
                        casillaVigenciaNoFormal = -1;
                    } else if (casillaVigenciaNoFormal == 1) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarInstitucionModEducacionNoFormal");
                        RequestContext.getCurrentInstance().execute("PF('editarInstitucionModEducacionNoFormal').show()");
                        casillaVigenciaNoFormal = -1;
                    } else if (casillaVigenciaNoFormal == 2) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaInicialModEducacionNoFormal");
                        RequestContext.getCurrentInstance().execute("PF('editarFechaInicialModEducacionNoFormal').show()");
                        casillaVigenciaNoFormal = -1;
                    } else if (casillaVigenciaNoFormal == 3) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaFinalModEducacionNoFormal");
                        RequestContext.getCurrentInstance().execute("PF('editarFechaFinalModEducacionNoFormal').show()");
                        casillaVigenciaNoFormal = -1;
                    }
                }
                if (casillaHvExperienciaLaboral >= 0) {
                    if (casillaHvExperienciaLaboral == 0) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarSectorEconomicoModExperienciaLaboral");
                        RequestContext.getCurrentInstance().execute("PF('editarSectorEconomicoModExperienciaLaboral').show()");
                        casillaHvExperienciaLaboral = -1;
                    } else if (casillaHvExperienciaLaboral == 1) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarMotivoRetiroModExperienciaLaboral");
                        RequestContext.getCurrentInstance().execute("PF('editarMotivoRetiroModExperienciaLaboral').show()");
                        casillaHvExperienciaLaboral = -1;
                    } else if (casillaHvExperienciaLaboral == 2) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarEmpresaModExperienciaLaboral");
                        RequestContext.getCurrentInstance().execute("PF('editarEmpresaModExperienciaLaboral').show()");
                        casillaHvExperienciaLaboral = -1;
                    } else if (casillaHvExperienciaLaboral == 3) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarCargoModExperienciaLaboral");
                        RequestContext.getCurrentInstance().execute("PF('editarCargoModExperienciaLaboral').show()");
                        casillaHvExperienciaLaboral = -1;
                    } else if (casillaHvExperienciaLaboral == 4) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaInicialModExperienciaLaboral");
                        RequestContext.getCurrentInstance().execute("PF('editarFechaInicialModExperienciaLaboral').show()");
                        casillaHvExperienciaLaboral = -1;
                    } else if (casillaHvExperienciaLaboral == 5) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaFinalModExperienciaLaboral");
                        RequestContext.getCurrentInstance().execute("PF('editarFechaFinalModExperienciaLaboral').show()");
                        casillaHvExperienciaLaboral = -1;
                    }
                }
                if (casillaVigenciaProyecto >= 0) {
                    if (casillaVigenciaProyecto == 0) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarProyectoModProyecto");
                        RequestContext.getCurrentInstance().execute("PF('editarProyectoModProyecto').show()");
                        casillaVigenciaProyecto = -1;
                    } else if (casillaVigenciaProyecto == 1) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarRolModProyecto");
                        RequestContext.getCurrentInstance().execute("PF('editarRolModProyecto').show()");
                        casillaVigenciaProyecto = -1;
                    } else if (casillaVigenciaProyecto == 2) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaInicialModProyecto");
                        RequestContext.getCurrentInstance().execute("PF('editarFechaInicialModProyecto').show()");
                        casillaVigenciaProyecto = -1;
                    } else if (casillaVigenciaProyecto == 3) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaFinalModProyecto");
                        RequestContext.getCurrentInstance().execute("PF('editarFechaFinalModProyecto').show()");
                        casillaVigenciaProyecto = -1;
                    }
                }
                if (casillaVigenciaCargoPersonal >= 0) {
                    if (casillaVigenciaCargoPersonal == 0) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:editarCargoModCargoPostularse");
                        RequestContext.getCurrentInstance().execute("PF('editarCargoModCargoPostularse').show()");
                        casillaVigenciaCargoPersonal = -1;
                    }
                }
            }
        }
    }

    public void botonListaValores() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (numeroTipoBusqueda == 0) {
            System.out.println("casillaVigenciaCargo : " + casillaVigenciaCargo);
            if (casillaVigenciaCargo >= 0) {
                if (casillaVigenciaCargo == 0) {
                    RequestContext.getCurrentInstance().update("form:CargoCargoDialogo");
                    RequestContext.getCurrentInstance().execute("PF('CargoCargoDialogo').show()");
                    casillaVigenciaCargo = -1;
                }
                if (casillaVigenciaCargo == 1) {
                    RequestContext.getCurrentInstance().update("form:EstructuraCargoDialogo");
                    RequestContext.getCurrentInstance().execute("PF('EstructuraCargoDialogo').show()");
                    casillaVigenciaCargo = -1;
                }
                if (casillaVigenciaCargo == 2) {
                    RequestContext.getCurrentInstance().update("form:JefeCargoDialogo");
                    RequestContext.getCurrentInstance().execute("PF('JefeCargoDialogo').show()");
                    casillaVigenciaCargo = -1;
                }
                if (casillaVigenciaCargo == 3) {
                    RequestContext.getCurrentInstance().update("form:MotivoCargoDialogo");
                    RequestContext.getCurrentInstance().execute("PF('MotivoCargoDialogo').show()");
                    casillaVigenciaCargo = -1;
                }
                if (casillaVigenciaCargo == 5) {
                    RequestContext.getCurrentInstance().update("form:PapelCargoDialogo");
                    RequestContext.getCurrentInstance().execute("PF('PapelCargoDialogo').show()");
                    casillaVigenciaCargo = -1;
                }
            }
            System.out.println("casillaVigenciaLocalizacion : " + casillaVigenciaLocalizacion);
            if (casillaVigenciaLocalizacion >= 0) {
                if (casillaVigenciaLocalizacion == 0) {
                    RequestContext.getCurrentInstance().update("form:LocalizacionCentroCostoDialogo");
                    RequestContext.getCurrentInstance().execute("PF('LocalizacionCentroCostoDialogo').show()");
                    casillaVigenciaLocalizacion = -1;
                }
                if (casillaVigenciaLocalizacion == 1) {
                    RequestContext.getCurrentInstance().update("form:MotivoCentroCostoDialogo");
                    RequestContext.getCurrentInstance().execute("PF('MotivoCentroCostoDialogo').show()");
                    casillaVigenciaLocalizacion = -1;
                }
            }
            System.out.println("casillaVigenciaSueldo : " + casillaVigenciaSueldo);
            if (casillaVigenciaSueldo >= 0) {
                if (casillaVigenciaSueldo == 0) {
                    RequestContext.getCurrentInstance().update("form:TipoSueldoSueldoDialogo");
                    RequestContext.getCurrentInstance().execute("PF('TipoSueldoSueldoDialogo').show()");
                    casillaVigenciaSueldo = -1;
                }
                if (casillaVigenciaSueldo == 1) {
                    RequestContext.getCurrentInstance().update("form:MotivoSueldoDialogo");
                    RequestContext.getCurrentInstance().execute("PF('MotivoSueldoDialogo').show()");
                    casillaVigenciaSueldo = -1;
                }
            }
            System.out.println("casillaVigenciaTipoContrato : " + casillaVigenciaTipoContrato);
            if (casillaVigenciaTipoContrato >= 0) {
                if (casillaVigenciaTipoContrato == 0) {
                    RequestContext.getCurrentInstance().update("form:TipoContratoFechaContratoDialogo");
                    RequestContext.getCurrentInstance().execute("PF('TipoContratoFechaContratoDialogo').show()");
                    casillaVigenciaTipoContrato = -1;
                }
                if (casillaVigenciaTipoContrato == 1) {
                    RequestContext.getCurrentInstance().update("form:MotivoFechaContratoDialogo");
                    RequestContext.getCurrentInstance().execute("PF('MotivoFechaContratoDialogo').show()");
                    casillaVigenciaTipoContrato = -1;
                }
            }
            System.out.println("casillaVigenciaTipoTrabajador : " + casillaVigenciaTipoTrabajador);
            if (casillaVigenciaTipoTrabajador >= 0) {
                if (casillaVigenciaTipoTrabajador == 0) {
                    RequestContext.getCurrentInstance().update("form:TipoTrabajadorTipoTrabajadorDialogo");
                    RequestContext.getCurrentInstance().execute("PF('TipoTrabajadorTipoTrabajadorDialogo').show()");
                    casillaVigenciaTipoTrabajador = -1;
                }
            }
            System.out.println("casillaVigenciaReformaLaboral : " + casillaVigenciaReformaLaboral);
            if (casillaVigenciaReformaLaboral >= 0) {
                if (casillaVigenciaReformaLaboral == 0) {
                    RequestContext.getCurrentInstance().update("form:ReformaLaboralTipoSalarioDialogo");
                    RequestContext.getCurrentInstance().execute("PF('ReformaLaboralTipoSalarioDialogo').show()");
                    casillaVigenciaReformaLaboral = -1;
                }
            }
            System.out.println("casillaVigenciaNormaLaboral : " + casillaVigenciaNormaLaboral);
            if (casillaVigenciaNormaLaboral >= 0) {
                if (casillaVigenciaNormaLaboral == 0) {
                    RequestContext.getCurrentInstance().update("form:NormaLaboralNormaLaboralDialogo");
                    RequestContext.getCurrentInstance().execute("PF('NormaLaboralNormaLaboralDialogo').show()");
                    casillaVigenciaNormaLaboral = -1;
                }
            }
            System.out.println("casillaVigenciaContrato : " + casillaVigenciaContrato);
            if (casillaVigenciaContrato >= 0) {
                if (casillaVigenciaContrato == 0) {
                    RequestContext.getCurrentInstance().update("form:LegislacionLegislacionLaboralDialogo");
                    RequestContext.getCurrentInstance().execute("PF('LegislacionLegislacionLaboralDialogo').show()");
                    casillaVigenciaContrato = -1;
                }
            }
            System.out.println("casillaVigenciaUbicacion : " + casillaVigenciaUbicacion);
            if (casillaVigenciaUbicacion >= 0) {
                if (casillaVigenciaUbicacion == 0) {
                    RequestContext.getCurrentInstance().update("form:UbicacionUbicacionDialogo");
                    RequestContext.getCurrentInstance().execute("PF('UbicacionUbicacionDialogo').show()");
                    casillaVigenciaUbicacion = -1;
                }
            }
            System.out.println("casillaVigenciaAfiliacion : " + casillaVigenciaAfiliacion);
            if (casillaVigenciaAfiliacion >= 0) {
                if (casillaVigenciaAfiliacion == 0) {
                    RequestContext.getCurrentInstance().update("form:TerceroAfiliacionDialogo");
                    RequestContext.getCurrentInstance().execute("PF('TerceroAfiliacionDialogo').show()");
                    casillaVigenciaAfiliacion = -1;
                }
                if (casillaVigenciaAfiliacion == 1) {
                    RequestContext.getCurrentInstance().update("form:TipoEntidadAfiliacionDialogo");
                    RequestContext.getCurrentInstance().execute("PF('TipoEntidadAfiliacionDialogo').show()");
                    casillaVigenciaAfiliacion = -1;
                }
                if (casillaVigenciaAfiliacion == 2) {
                    RequestContext.getCurrentInstance().update("form:EstadoAfiliacionDialogo");
                    RequestContext.getCurrentInstance().execute("PF('EstadoAfiliacionDialogo').show()");
                    casillaVigenciaAfiliacion = -1;
                }
            }
            System.out.println("casillaVigenciaFormaPago : " + casillaVigenciaFormaPago);
            if (casillaVigenciaFormaPago >= 0) {
                if (casillaVigenciaFormaPago == 0) {
                    RequestContext.getCurrentInstance().update("form:PeriodicidadFormaPagoDialogo");
                    RequestContext.getCurrentInstance().execute("PF('PeriodicidadFormaPagoDialogo').show()");
                    casillaVigenciaFormaPago = -1;
                }
                if (casillaVigenciaFormaPago == 1) {
                    RequestContext.getCurrentInstance().update("form:SucursalFormaPagoDialogo");
                    RequestContext.getCurrentInstance().execute("PF('SucursalFormaPagoDialogo').show()");
                    casillaVigenciaFormaPago = -1;
                }
            }
            System.out.println("casillaMvrs : " + casillaMvrs);
            if (casillaMvrs >= 0) {
                if (casillaMvrs == 0) {
                    RequestContext.getCurrentInstance().update("form:MotivoMvrsDialogo");
                    RequestContext.getCurrentInstance().execute("PF('MotivoMvrsDialogo').show()");
                    casillaMvrs = -1;
                }
            }
            System.out.println("casillaVigenciaJornadaLaboral : " + casillaVigenciaJornadaLaboral);
            if (casillaVigenciaJornadaLaboral >= 0) {
                if (casillaVigenciaJornadaLaboral == 0) {
                    RequestContext.getCurrentInstance().update("form:JornadaJornadaLaboralDialogo");
                    RequestContext.getCurrentInstance().execute("PF('JornadaJornadaLaboralDialogo').show()");
                    casillaVigenciaJornadaLaboral = -1;
                }
            }
            System.out.println("casillaMotivoRetiro : " + casillaMotivoRetiro);
            if (casillaMotivoRetiro >= 0) {
                if (casillaMotivoRetiro == 0) {
                    RequestContext.getCurrentInstance().update("form:MotivoFechaRetiroDialogo");
                    RequestContext.getCurrentInstance().execute("PF('MotivoFechaRetiroDialogo').show()");
                    casillaMotivoRetiro = -1;
                }
            }
        }
        if (numeroTipoBusqueda == 1) {
            if (casillaEmpleado >= 0) {
                if (casillaEmpleado == 1) {
                    RequestContext.getCurrentInstance().update("form:CiudadDocumentoDatosPersonalesDialogo");
                    RequestContext.getCurrentInstance().execute("PF('CiudadDocumentoDatosPersonalesDialogo').show()");
                    casillaEmpleado = -1;
                }
                if (casillaEmpleado == 2) {
                    RequestContext.getCurrentInstance().update("form:CiudadNacimientoDatosPersonalesDialogo");
                    RequestContext.getCurrentInstance().execute("PF('CiudadNacimientoDatosPersonalesDialogo').show()");
                    casillaEmpleado = -1;
                }
            }
            if (casillaEstadoCivil >= 0) {
                if (casillaEstadoCivil == 0) {
                    RequestContext.getCurrentInstance().update("form:EstadoCivilEstadoCivilDialogo");
                    RequestContext.getCurrentInstance().execute("PF('EstadoCivilEstadoCivilDialogo').show()");
                    casillaEstadoCivil = -1;
                }
            }
            if (casillaIdiomaPersona >= 0) {
                if (casillaIdiomaPersona == 0) {
                    RequestContext.getCurrentInstance().update("form:IdiomaIdiomaDialogo");
                    RequestContext.getCurrentInstance().execute("PF('IdiomaIdiomaDialogo').show()");
                    casillaIdiomaPersona = -1;
                }
            }
            if (casillaVigenciaIndicador >= 0) {
                if (casillaVigenciaIndicador == 0) {
                    RequestContext.getCurrentInstance().update("form:TipoIndicadorCensoDialogo");
                    RequestContext.getCurrentInstance().execute("PF('TipoIndicadorCensoDialogo').show()");
                    casillaVigenciaIndicador = -1;
                }
                if (casillaVigenciaIndicador == 1) {
                    RequestContext.getCurrentInstance().update("form:IndicadorCensoDialogo");
                    RequestContext.getCurrentInstance().execute("PF('IndicadorCensoDialogo').show()");
                    casillaVigenciaIndicador = -1;
                }
            }
            if (casillaVigenciaFormal >= 0) {
                if (casillaVigenciaFormal == 0) {
                    RequestContext.getCurrentInstance().update("form:ProfesionEducacionFormalDialogo");
                    RequestContext.getCurrentInstance().execute("PF('ProfesionEducacionFormalDialogo').show()");
                    casillaVigenciaFormal = -1;
                }
                if (casillaVigenciaFormal == 1) {
                    RequestContext.getCurrentInstance().update("form:InstitucionEducacionFormalDialogo");
                    RequestContext.getCurrentInstance().execute("PF('InstitucionEducacionFormalDialogo').show()");
                    casillaVigenciaFormal = -1;
                }
            }
            if (casillaVigenciaNoFormal >= 0) {
                if (casillaVigenciaNoFormal == 0) {
                    RequestContext.getCurrentInstance().update("form:CursoEducacionNoFormalDialogo");
                    RequestContext.getCurrentInstance().execute("PF('CursoEducacionNoFormalDialogo').show()");
                    casillaVigenciaFormal = -1;
                }
                if (casillaVigenciaNoFormal == 1) {
                    RequestContext.getCurrentInstance().update("form:InstitucionEducacionNoFormalDialogo");
                    RequestContext.getCurrentInstance().execute("PF('InstitucionEducacionNoFormalDialogo').show()");
                    casillaVigenciaNoFormal = -1;
                }
            }
            if (casillaHvExperienciaLaboral >= 0) {
                if (casillaHvExperienciaLaboral == 0) {
                    RequestContext.getCurrentInstance().update("form:SectorEconomicoExperienciaLaboralDialogo");
                    RequestContext.getCurrentInstance().execute("PF('SectorEconomicoExperienciaLaboralDialogo').show()");
                    casillaHvExperienciaLaboral = -1;
                }
                if (casillaHvExperienciaLaboral == 1) {
                    RequestContext.getCurrentInstance().update("form:MotivoRetiroExperienciaLaboralDialogo");
                    RequestContext.getCurrentInstance().execute("PF('MotivoRetiroExperienciaLaboralDialogo').show()");
                    casillaHvExperienciaLaboral = -1;
                }
            }
            if (casillaVigenciaProyecto >= 0) {
                if (casillaVigenciaProyecto == 0) {
                    RequestContext.getCurrentInstance().update("form:ProyectoProyectoDialogo");
                    RequestContext.getCurrentInstance().execute("PF('ProyectoProyectoDialogo').show()");
                    casillaVigenciaProyecto = -1;
                }
                if (casillaVigenciaProyecto == 1) {
                    RequestContext.getCurrentInstance().update("form:PryRolProyectoDialogo");
                    RequestContext.getCurrentInstance().execute("PF('PryRolProyectoDialogo').show()");
                    casillaVigenciaProyecto = -1;
                }
            }
            if (casillaVigenciaCargoPersonal >= 0) {
                if (casillaVigenciaCargoPersonal == 0) {
                    RequestContext.getCurrentInstance().update("form:CargoCargoPostularseDialogo");
                    RequestContext.getCurrentInstance().execute("PF('CargoCargoPostularseDialogo').show()");
                    casillaVigenciaCargoPersonal = -1;
                }
            }
        }
    }

    public void posicionModuloCargo() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, String> map = context.getExternalContext().getRequestParameterMap();
        String name = map.get("n"); // name attribute of node
        int indice = Integer.parseInt(name);
        cambiarIndiceCargo(indice);
    }

    public void posicionModuloUbicacion() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, String> map = context.getExternalContext().getRequestParameterMap();
        String name = map.get("n"); // name attribute of node
        int indice = Integer.parseInt(name);
        cambiarIndiceUbicacion(indice);
    }

    public void cambiarIndiceCargo(int i) {
        System.out.println("entre");
        if (permitirIndexVigenciaCargo == true) {
            indice = -1;
            casillaVigenciaTipoContrato = -1;
            casillaVigenciaLocalizacion = -1;
            casillaVigenciaTipoTrabajador = -1;
            casillaVigenciaSueldo = -1;
            casillaVigenciaReformaLaboral = -1;
            casillaVigenciaNormaLaboral = -1;
            casillaVigenciaContrato = -1;
            casillaVigenciaUbicacion = -1;
            casillaVigenciaAfiliacion = -1;
            casillaVigenciaFormaPago = -1;
            casillaMvrs = -1;
            casillaSets = -1;
            casillaVacacion = -1;
            casillaVigenciaJornadaLaboral = -1;
            casillaMotivoRetiro = -1;
            casillaVigenciaCargo = i;

            System.out.println("casillaVigenciaTipoContrato : " + casillaVigenciaTipoContrato);
            System.out.println("casillaVigenciaLocalizacion : " + casillaVigenciaTipoContrato);
            System.out.println("casillaVigenciaTipoTrabajador : " + casillaVigenciaTipoContrato);
            System.out.println("casillaVigenciaSueldo : " + casillaVigenciaTipoContrato);
            System.out.println("casillaVigenciaReformaLaboral : " + casillaVigenciaTipoContrato);
            System.out.println("casillaVigenciaNormaLaboral : " + casillaVigenciaTipoContrato);
            System.out.println("casillaVigenciaContrato : " + casillaVigenciaTipoContrato);
            System.out.println("casillaVigenciaUbicacion : " + casillaVigenciaTipoContrato);
            System.out.println("casillaVigenciaAfiliacion : " + casillaVigenciaTipoContrato);
            System.out.println("casillaVigenciaFormaPago : " + casillaVigenciaTipoContrato);
            System.out.println("casillaMvrs : " + casillaVigenciaTipoContrato);
            System.out.println("casillaSets : " + casillaVigenciaTipoContrato);
            System.out.println("casillaVacacion : " + casillaVigenciaTipoContrato);
            System.out.println("casillaVigenciaJornadaLaboral : " + casillaVigenciaTipoContrato);
            System.out.println("casillaMotivoRetiro : " + casillaVigenciaTipoContrato);
            System.out.println("casillaVigenciaCargo : " + casillaVigenciaTipoContrato);

            auxEstructuraVigenciaCargo = vigenciaCargoBA.getEstructura().getNombre();
            auxCargoVigenciaCargo = vigenciaCargoBA.getCargo().getNombre();
            auxJefeVigenciaCargo = vigenciaCargoBA.getEmpleadojefe().getPersona().getNombreCompleto();
            auxMotivoCambioCargoVigenciaCargo = vigenciaCargoBA.getMotivocambiocargo().getNombre();
            auxPapelVigenciaCargo = vigenciaCargoBA.getPapel().getDescripcion();
            auxFechaFinalCargo = fechaFinalCargo;
            auxFechaInicialCargo = fechaInicialCargo;
        }
    }

    public void cambiarIndiceCentroCosto(int i) {
        if (permitirIndexVigenciaLocalizacion == true) {
            indice = -1;
            casillaVigenciaCargo = -1;
            casillaVigenciaTipoTrabajador = -1;
            casillaVigenciaTipoContrato = -1;
            casillaVigenciaSueldo = -1;
            casillaVigenciaReformaLaboral = -1;
            casillaVigenciaNormaLaboral = -1;
            casillaVigenciaContrato = -1;
            casillaVigenciaUbicacion = -1;
            casillaVigenciaAfiliacion = -1;
            casillaVigenciaFormaPago = -1;
            casillaMvrs = -1;
            casillaSets = -1;
            casillaMotivoRetiro = -1;
            casillaVacacion = -1;
            casillaVigenciaJornadaLaboral = -1;
            casillaVigenciaLocalizacion = i;
            auxLocalizacionVigenciaLocalizacion = vigenciaLocalizacionBA.getLocalizacion().getNombre();
            auxMotivoLocalizacionVigenciaLocalizacion = vigenciaLocalizacionBA.getMotivo().getDescripcion();
            auxFechaFinalCentroCosto = fechaFinalCentroCosto;
            auxFechaInicialCentroCosto = fechaInicialCentroCosto;
        }
    }

    public void cambiarIndiceSueldo(int i) {
        if (permitirIndexVigenciaSueldo == true) {
            indice = -1;
            casillaVigenciaCargo = -1;
            casillaVigenciaTipoTrabajador = -1;
            casillaVigenciaTipoContrato = -1;
            casillaVigenciaLocalizacion = -1;
            casillaVigenciaReformaLaboral = -1;
            casillaVigenciaNormaLaboral = -1;
            casillaVigenciaContrato = -1;
            casillaVigenciaUbicacion = -1;
            casillaVigenciaAfiliacion = -1;
            casillaVigenciaFormaPago = -1;
            casillaMvrs = -1;
            casillaSets = -1;
            casillaMotivoRetiro = -1;
            casillaVacacion = -1;
            casillaVigenciaJornadaLaboral = -1;
            casillaVigenciaSueldo = i;
            auxTipoSueldoVigenciaSueldo = vigenciaSueldoBA.getTiposueldo().getDescripcion();
            auxMotivoCambioSueldoVigenciaSueldo = vigenciaSueldoBA.getMotivocambiosueldo().getNombre();
            auxFechaFinalSueldo = fechaFinalSueldo;
            auxFechaInicialSueldo = fechaInicialSueldo;
            auxSueldoMaximoSueldo = sueldoMaxSueldo;
            auxSueldoMinimoSueldo = sueldoMinSueldo;
        }
    }

    public void cambiarIndiceFechaContrato(int i) {
        if (permitirIndexVigenciaTipoContrato == true) {
            indice = -1;
            casillaVigenciaCargo = -1;
            casillaVigenciaSueldo = -1;
            casillaVigenciaLocalizacion = -1;
            casillaVigenciaTipoTrabajador = -1;
            casillaVigenciaReformaLaboral = -1;
            casillaVigenciaNormaLaboral = -1;
            casillaVigenciaUbicacion = -1;
            casillaVigenciaContrato = -1;
            casillaVigenciaAfiliacion = -1;
            casillaVigenciaFormaPago = -1;
            casillaMvrs = -1;
            casillaSets = -1;
            casillaMotivoRetiro = -1;
            casillaVacacion = -1;
            casillaVigenciaJornadaLaboral = -1;
            casillaVigenciaTipoContrato = i;
            auxTipoContratoVigenciaTipoContrato = vigenciaTipoContratoBA.getTipocontrato().getNombre();
            auxMotivoContratoVigenciaTipoContrato = vigenciaTipoContratoBA.getMotivocontrato().getNombre();
            auxFechaFinalFechaContrato = fechaFinalFechaContrato;
            auxFechaInicialFechaContrato = fechaInicialFechaContrato;
        }
    }

    public void cambiarIndiceTipoTrabajador(int i) {
        if (permitirIndexVigenciaTipoTrabajador == true) {
            indice = -1;
            casillaVigenciaCargo = -1;
            casillaVigenciaSueldo = -1;
            casillaVigenciaLocalizacion = -1;
            casillaVigenciaTipoContrato = -1;
            casillaVigenciaReformaLaboral = -1;
            casillaVigenciaNormaLaboral = -1;
            casillaVigenciaUbicacion = -1;
            casillaVigenciaContrato = -1;
            casillaVigenciaAfiliacion = -1;
            casillaVigenciaFormaPago = -1;
            casillaMvrs = -1;
            casillaSets = -1;
            casillaMotivoRetiro = -1;
            casillaVacacion = -1;
            casillaVigenciaJornadaLaboral = -1;
            casillaVigenciaTipoTrabajador = i;
            auxTipoTrabajadorVigenciaTipoTrabajador = vigenciaTipoTrabajadorBA.getTipotrabajador().getNombre();
            auxFechaFinalTipoTrabajador = fechaFinalTipoTrabajador;
            auxFechaInicialTipoTrabajador = fechaInicialTipoTrabajador;
        }
    }

    public void cambiarIndiceTipoSalario(int i) {
        if (permitirIndexVigenciaReformaLaboral == true) {
            indice = -1;
            casillaVigenciaCargo = -1;
            casillaVigenciaSueldo = -1;
            casillaVigenciaLocalizacion = -1;
            casillaVigenciaTipoContrato = -1;
            casillaVigenciaUbicacion = -1;
            casillaVigenciaTipoTrabajador = -1;
            casillaVigenciaNormaLaboral = -1;
            casillaVigenciaContrato = -1;
            casillaVigenciaAfiliacion = -1;
            casillaVigenciaFormaPago = -1;
            casillaMvrs = -1;
            casillaSets = -1;
            casillaMotivoRetiro = -1;
            casillaVacacion = -1;
            casillaVigenciaJornadaLaboral = -1;
            casillaVigenciaReformaLaboral = i;
            auxReformaLaboralVigenciaReformaLaboral = vigenciaReformaLaboralBA.getReformalaboral().getNombre();
            auxFechaFinalTipoSalario = fechaFinalTipoSalario;
            auxFechaInicialTipoSalario = fechaInicialTipoSalario;
        }
    }

    public void cambiarIndiceNormaLaboral(int i) {
        if (permitirIndexVigenciaNormaEmpleado == true) {
            indice = -1;
            casillaVigenciaCargo = -1;
            casillaVigenciaSueldo = -1;
            casillaVigenciaUbicacion = -1;
            casillaVigenciaLocalizacion = -1;
            casillaVigenciaTipoContrato = -1;
            casillaVigenciaTipoTrabajador = -1;
            casillaVigenciaReformaLaboral = -1;
            casillaVigenciaContrato = -1;
            casillaVigenciaAfiliacion = -1;
            casillaVigenciaFormaPago = -1;
            casillaMvrs = -1;
            casillaSets = -1;
            casillaMotivoRetiro = -1;
            casillaVacacion = -1;
            casillaVigenciaJornadaLaboral = -1;
            casillaVigenciaNormaLaboral = i;
            auxNormaLaboralVigenciaNormaEmpleado = vigenciaNormaEmpleadoBA.getNormalaboral().getNombre();
            auxFechaFinalNormaLaboral = fechaFinalNormaLaboral;
            auxFechaInicialNormaLaboral = fechaInicialNormaLaboral;
        }
    }

    public void cambiarIndiceLegislacionLaboral(int i) {
        if (permitirIndexVigenciaContrato == true) {
            indice = -1;
            casillaVigenciaCargo = -1;
            casillaVigenciaSueldo = -1;
            casillaVigenciaLocalizacion = -1;
            casillaVigenciaTipoContrato = -1;
            casillaVigenciaTipoTrabajador = -1;
            casillaVigenciaUbicacion = -1;
            casillaVigenciaReformaLaboral = -1;
            casillaVigenciaNormaLaboral = -1;
            casillaVigenciaAfiliacion = -1;
            casillaVigenciaFormaPago = -1;
            casillaMvrs = -1;
            casillaSets = -1;
            casillaMotivoRetiro = -1;
            casillaVacacion = -1;
            casillaVigenciaJornadaLaboral = -1;
            casillaVigenciaContrato = i;
            auxContratoVigenciaContrato = vigenciaContratoBA.getContrato().getDescripcion();
            auxFechaMFFinalLegislacionLaboral = fechaMFFinalLegislacionLaboral;
            auxFechaMFInicialLegislacionLaboral = fechaMFInicialLegislacionLaboral;
            auxFechaMIFinalLegislacionLaboral = fechaMIFinalLegislacionLaboral;
            auxFechaMIInicialLegislacionLaboral = fechaMIInicialLegislacionLaboral;
        }
    }

    public void cambiarIndiceUbicacion(int i) {
        if (permitirIndexVigenciaUbicacion == true) {
            indice = -1;
            casillaVigenciaCargo = -1;
            casillaVigenciaSueldo = -1;
            casillaVigenciaLocalizacion = -1;
            casillaVigenciaTipoContrato = -1;
            casillaVigenciaTipoTrabajador = -1;
            casillaVigenciaReformaLaboral = -1;
            casillaVigenciaNormaLaboral = -1;
            casillaVigenciaContrato = -1;
            casillaVigenciaAfiliacion = -1;
            casillaVigenciaFormaPago = -1;
            casillaMvrs = -1;
            casillaSets = -1;
            casillaMotivoRetiro = -1;
            casillaVacacion = -1;
            casillaVigenciaJornadaLaboral = -1;
            casillaVigenciaUbicacion = i;
            auxUbicacionVigenciaUbicacion = vigenciaUbicacionBA.getUbicacion().getDescripcion();
            auxFechaFinalUbicacion = fechaFinalUbicacion;
            auxFechaInicialUbicacion = fechaInicialUbicacion;
        }
    }

    public void cambiarIndiceAfiliacion(int i) {
        if (permitirIndexVigenciaAfiliacion == true) {
            indice = -1;
            casillaVigenciaCargo = -1;
            casillaVigenciaSueldo = -1;
            casillaVigenciaLocalizacion = -1;
            casillaVigenciaTipoContrato = -1;
            casillaVigenciaTipoTrabajador = -1;
            casillaVigenciaReformaLaboral = -1;
            casillaVigenciaNormaLaboral = -1;
            casillaVigenciaContrato = -1;
            casillaVigenciaUbicacion = -1;
            casillaVigenciaFormaPago = -1;
            casillaMvrs = -1;
            casillaSets = -1;
            casillaMotivoRetiro = -1;
            casillaVacacion = -1;
            casillaVigenciaJornadaLaboral = -1;
            casillaVigenciaAfiliacion = i;
            auxTerceroVigenciaAfiliacion3 = vigenciaAfiliacionBA.getTercerosucursal().getDescripcion();
            auxTipoEntidadVigenciaAfiliacion3 = vigenciaAfiliacionBA.getTipoentidad().getNombre();
            auxEstadoVigenciaAfiliacion3 = vigenciaAfiliacionBA.getEstadoafiliacion().getNombre();
            auxFechaFinalAfiliacion = fechaFinalAfiliacion;
            auxFechaInicialAfiliacion = fechaInicialAfiliacion;
        }
    }

    public void cambiarIndiceFormaPago(int i) {
        if (permitirIndexVigenciaFormaPago == true) {
            indice = -1;
            casillaVigenciaCargo = -1;
            casillaVigenciaSueldo = -1;
            casillaVigenciaLocalizacion = -1;
            casillaVigenciaTipoContrato = -1;
            casillaVigenciaTipoTrabajador = -1;
            casillaVigenciaReformaLaboral = -1;
            casillaVigenciaNormaLaboral = -1;
            casillaVigenciaContrato = -1;
            casillaVigenciaUbicacion = -1;
            casillaVigenciaAfiliacion = -1;
            casillaMvrs = -1;
            casillaSets = -1;
            casillaMotivoRetiro = -1;
            casillaVacacion = -1;
            casillaVigenciaJornadaLaboral = -1;
            casillaVigenciaFormaPago = i;
            auxSucursalVigenciaFormaPago = vigenciaFormaPagoBA.getSucursal().getNombre();
            auxPeriodicidadVigenciaFormaPago = vigenciaFormaPagoBA.getFormapago().getNombre();
            auxFechaFinalFormaPago = fechaFinalFormaPago;
            auxFechaInicialFormaPago = fechaInicialFormaPago;
        }
    }

    public void cambiarIndiceMvrs(int i) {
        if (permitirIndexMvrs == true) {
            indice = -1;
            casillaVigenciaCargo = -1;
            casillaVigenciaSueldo = -1;
            casillaVigenciaLocalizacion = -1;
            casillaVigenciaTipoContrato = -1;
            casillaVigenciaTipoTrabajador = -1;
            casillaVigenciaReformaLaboral = -1;
            casillaVigenciaNormaLaboral = -1;
            casillaVigenciaContrato = -1;
            casillaVigenciaUbicacion = -1;
            casillaVigenciaAfiliacion = -1;
            casillaVigenciaFormaPago = -1;
            casillaSets = -1;
            casillaVacacion = -1;
            casillaMotivoRetiro = -1;
            casillaVigenciaJornadaLaboral = -1;
            casillaMvrs = i;
            auxMotivoMvrs = mvrsBA.getMotivo().getNombre();
            auxSueldoMaximoMvrs = sueldoMaxMvrs;
            auxSueldoMinimoMvrs = sueldoMinMvrs;
            auxFechaFinalMvrs = fechaFinalMvrs;
            auxFechaInicialMvrs = fechaInicialMvrs;
        }
    }

    public void cambiarIndiceSets(int i) {
        indice = -1;
        casillaVigenciaCargo = -1;
        casillaVigenciaSueldo = -1;
        casillaVigenciaLocalizacion = -1;
        casillaVigenciaTipoContrato = -1;
        casillaVigenciaTipoTrabajador = -1;
        casillaVigenciaReformaLaboral = -1;
        casillaVigenciaNormaLaboral = -1;
        casillaVigenciaContrato = -1;
        casillaVigenciaUbicacion = -1;
        casillaVigenciaAfiliacion = -1;
        casillaVigenciaFormaPago = -1;
        casillaMvrs = -1;
        casillaVacacion = -1;
        casillaMotivoRetiro = -1;
        casillaVigenciaJornadaLaboral = -1;
        casillaSets = i;
        auxPromedioMaximoSets = promedioMaximoSets;
        auxPromedioMinimoSets = promedioMinimoSets;
        auxFechaMFFinalSets = fechaMFFinalSets;
        auxFechaMFInicialSets = fechaMFInicialSets;
        auxFechaMIFinalSets = fechaMIFinalSets;
        auxFechaMIInicialSets = fechaMIInicialSets;

    }

    public void cambiarIndiceVacacion(int i) {
        indice = -1;
        casillaVigenciaCargo = -1;
        casillaVigenciaSueldo = -1;
        casillaVigenciaLocalizacion = -1;
        casillaVigenciaTipoContrato = -1;
        casillaVigenciaTipoTrabajador = -1;
        casillaVigenciaReformaLaboral = -1;
        casillaVigenciaNormaLaboral = -1;
        casillaVigenciaContrato = -1;
        casillaVigenciaUbicacion = -1;
        casillaVigenciaAfiliacion = -1;
        casillaVigenciaFormaPago = -1;
        casillaMvrs = -1;
        casillaSets = -1;
        casillaMotivoRetiro = -1;
        casillaVigenciaJornadaLaboral = -1;
        casillaVacacion = i;
        auxFechaMFFinalVacacion = fechaMFFinalVacacion;
        auxFechaMFInicialVacacion = fechaMFInicialVacacion;
        auxFechaMIFinalVacacion = fechaMIFinalVacacion;
        auxFechaMIInicialVacacion = fechaMIInicialVacacion;
    }

    public void cambiarIndiceJornadaLaboral(int i) {
        if (permitirIndexVigenciaJornada == true) {
            indice = -1;
            casillaVigenciaCargo = -1;
            casillaVigenciaSueldo = -1;
            casillaVigenciaLocalizacion = -1;
            casillaVigenciaTipoContrato = -1;
            casillaVigenciaTipoTrabajador = -1;
            casillaVigenciaReformaLaboral = -1;
            casillaVigenciaNormaLaboral = -1;
            casillaVigenciaContrato = -1;
            casillaVigenciaUbicacion = -1;
            casillaVigenciaAfiliacion = -1;
            casillaVigenciaFormaPago = -1;
            casillaSets = -1;
            casillaVacacion = -1;
            casillaMvrs = -1;
            casillaMotivoRetiro = -1;
            casillaVigenciaJornadaLaboral = i;
            auxJornadaJornadaLaboral = vigenciaJornadaBA.getJornadatrabajo().getDescripcion();
            auxFechaFinalJornadaLaboral = fechaFinalJornadaLaboral;
            auxFechaInicialJornadaLaboral = fechaInicialJornadaLaboral;
        }
    }

    public void cambiarIndiceFechaRetiro(int i) {
        if (permitirIndexMotivoRetiro == true) {
            indice = -1;
            casillaVigenciaCargo = -1;
            casillaVigenciaSueldo = -1;
            casillaVigenciaLocalizacion = -1;
            casillaVigenciaTipoContrato = -1;
            casillaVigenciaTipoTrabajador = -1;
            casillaVigenciaReformaLaboral = -1;
            casillaVigenciaNormaLaboral = -1;
            casillaVigenciaContrato = -1;
            casillaVigenciaUbicacion = -1;
            casillaVigenciaAfiliacion = -1;
            casillaVigenciaFormaPago = -1;
            casillaSets = -1;
            casillaVacacion = -1;
            casillaMvrs = -1;
            casillaVigenciaJornadaLaboral = -1;
            casillaMotivoRetiro = i;
            auxMotivoMotivoRetiro = motivoRetiroBA.getNombre();
            auxFechaFinalFechaRetiro = fechaFinalFechaRetiro;
            auxFechaInicialFechaRetiro = fechaInicialFechaRetiro;
        }
    }

    public void cambiarIndiceDatosPersonales(int i) {
        if (permitirIndexEmpleado == true) {
            indice = -1;
            casillaEstadoCivil = -1;
            casillaIdiomaPersona = -1;
            casillaVigenciaIndicador = -1;
            casillaVigenciaFormal = -1;
            casillaVigenciaNoFormal = -1;
            casillaHvExperienciaLaboral = -1;
            casillaVigenciaProyecto = -1;
            casillaVigenciaCargoPersonal = -1;
            casillaEmpleado = i;
            auxCiudadDocumentoEmpleado = empleadoBA.getPersona().getCiudaddocumento().getNombre();
            auxCiudadNacimientoEmpleado = empleadoBA.getPersona().getCiudadnacimiento().getNombre();
            auxFechaInicialDatosPersonales = fechaInicialDatosPersonales;
            auxFechaFinalDatosPersonales = fechaFinalDatosPersonales;
        }
    }

    public void cambiarIndiceEstadoCivil(int i) {
        if (permitirIndexEstadoCivil == true) {
            indice = -1;
            casillaEmpleado = -1;
            casillaIdiomaPersona = -1;
            casillaVigenciaIndicador = -1;
            casillaVigenciaFormal = -1;
            casillaVigenciaNoFormal = -1;
            casillaHvExperienciaLaboral = -1;
            casillaVigenciaProyecto = -1;
            casillaVigenciaCargoPersonal = -1;
            casillaEstadoCivil = i;
            auxEstadoCivilEstadoCivil = estadoCivilBA.getDescripcion();
            auxFechaInicialEstadoCivil = fechaInicialEstadoCivil;
            auxFechaFinalEstadoCivil = fechaFinalEstadoCivil;
        }
    }

    public void cambiarIndiceIdioma(int i) {
        if (permitirIndexIdiomaPersona == true) {
            indice = -1;
            casillaEmpleado = -1;
            casillaEstadoCivil = -1;
            casillaVigenciaIndicador = -1;
            casillaVigenciaFormal = -1;
            casillaVigenciaNoFormal = -1;
            casillaHvExperienciaLaboral = -1;
            casillaVigenciaProyecto = -1;
            casillaVigenciaCargoPersonal = -1;
            casillaIdiomaPersona = i;
            auxIdiomaIdiomaPersona = idiomaPersonaBA.getIdioma().getNombre();
            auxConversacionDesde = conversacionDesde;
            auxConversacionHasta = conversacionHasta;
            auxLecturaDesde = lecturaDesde;
            auxLecturaHasta = lecturaHasta;
            auxEscrituraDesde = escrituraDesde;
            auxEscrituraHasta = escrituraHasta;
        }
    }

    public void cambiarIndiceCenso(int i) {
        if (permitirIndexVigenciaIndicador == true) {
            indice = -1;
            casillaEmpleado = -1;
            casillaEstadoCivil = -1;
            casillaIdiomaPersona = -1;
            casillaVigenciaFormal = -1;
            casillaVigenciaNoFormal = -1;
            casillaHvExperienciaLaboral = -1;
            casillaVigenciaProyecto = -1;
            casillaVigenciaCargoPersonal = -1;
            casillaVigenciaIndicador = i;
            auxTipoIndicadorCenso = vigenciaIndicadorBA.getTipoindicador().getDescripcion();
            auxIndicadorCenso = vigenciaIndicadorBA.getIndicador().getDescripcion();
            auxFechaInicialCenso = fechaInicialCenso;
            auxFechaFinalCenso = fechaFinalCenso;
        }
    }

    public void cambiarIndiceEducacionFormal(int i) {
        if (permitirIndexVigenciaFormal == true) {
            indice = -1;
            casillaEmpleado = -1;
            casillaEstadoCivil = -1;
            casillaIdiomaPersona = -1;
            casillaVigenciaIndicador = -1;
            casillaVigenciaNoFormal = -1;
            casillaHvExperienciaLaboral = -1;
            casillaVigenciaProyecto = -1;
            casillaVigenciaCargoPersonal = -1;
            casillaVigenciaFormal = i;
            auxInstitucionEducacionFormal = vigenciaFormalBA.getInstitucion().getDescripcion();
            auxProfesionEducacionFormal = vigenciaFormalBA.getProfesion().getDescripcion();
            auxFechaInicialEducacionFormal = fechaInicialEducacionFormal;
            auxFechaFinalEducacionFormal = fechaFinalEducacionFormal;
        }
    }

    public void cambiarIndiceEducacionNoFormal(int i) {
        if (permitirIndexVigenciaNoFormal == true) {
            indice = -1;
            casillaEmpleado = -1;
            casillaEstadoCivil = -1;
            casillaIdiomaPersona = -1;
            casillaVigenciaIndicador = -1;
            casillaVigenciaFormal = -1;
            casillaHvExperienciaLaboral = -1;
            casillaVigenciaProyecto = -1;
            casillaVigenciaCargoPersonal = -1;
            casillaVigenciaNoFormal = i;
            auxCursoEducacionNoFormal = vigenciaNoFormalBA.getCurso().getNombre();
            auxInstitucionEducacionNoFormal = vigenciaFormalBA.getInstitucion().getDescripcion();
            auxFechaInicialEducacionNoFormal = fechaInicialEducacionNoFormal;
            auxFechaFinalEducacionNoFormal = fechaFinalEducacionNoFormal;
        }
    }

    public void cambiarIndiceExperienciaLaboral(int i) {
        if (permitirIndexHvExperienciaLaboral == true) {
            indice = -1;
            casillaEmpleado = -1;
            casillaEstadoCivil = -1;
            casillaIdiomaPersona = -1;
            casillaVigenciaIndicador = -1;
            casillaVigenciaFormal = -1;
            casillaVigenciaNoFormal = -1;
            casillaVigenciaProyecto = -1;
            casillaVigenciaCargoPersonal = -1;
            casillaHvExperienciaLaboral = i;
            auxMotivoRetiroExperienciaLaboral = hvExperienciaLaboralBA.getMotivoretiro().getNombre();
            auxSectorEconomicoExperienciaLaboral = hvExperienciaLaboralBA.getSectoreconomico().getDescripcion();
            auxFechaInicialExperienciaLaboral = fechaInicialExperienciaLaboral;
            auxFechaFinalExperienciaLaboral = fechaFinalExperienciaLaboral;
        }
    }

    public void cambiarIndiceProyecto(int i) {
        if (permitirIndexVigenciaProyecto == true) {
            indice = -1;
            casillaEmpleado = -1;
            casillaEstadoCivil = -1;
            casillaIdiomaPersona = -1;
            casillaVigenciaIndicador = -1;
            casillaVigenciaFormal = -1;
            casillaVigenciaNoFormal = -1;
            casillaHvExperienciaLaboral = -1;
            casillaVigenciaCargoPersonal = -1;
            casillaVigenciaProyecto = i;
            auxProyectoProyecto = vigenciaProyectoBA.getProyecto().getNombreproyecto();
            auxRolProyecto = vigenciaProyectoBA.getPryRol().getDescripcion();
            auxFechaInicialProyecto = fechaInicialProyecto;
            auxFechaFinalProyecto = fechaFinalProyecto;
        }
    }

    public void cambiarIndiceCargoPostularse(int i) {
        if (permitirIndexVigenciaCargoPersonal == true) {
            indice = -1;
            casillaEmpleado = -1;
            casillaEstadoCivil = -1;
            casillaIdiomaPersona = -1;
            casillaVigenciaIndicador = -1;
            casillaVigenciaFormal = -1;
            casillaVigenciaNoFormal = -1;
            casillaHvExperienciaLaboral = -1;
            casillaVigenciaProyecto = -1;
            casillaVigenciaCargoPersonal = i;
            auxCargoCargoPostularse = vigenciaCargoPersonalBA.getCargo().getNombre();
        }
    }

    public void modificarFechaFinalModuloCargo(int i) {
        try {
            boolean retorno = false;
            casillaVigenciaCargo = i;
            cambiarIndiceCargo(i);
            retorno = validarFechasRegistroModuloCargo();
            if (retorno == false) {
                fechaFinalCargo = null;
                fechaInicialCargo = null;
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:tabViewCosto");
                RequestContext.getCurrentInstance().execute("PF('errorFechasIngresadas').show()");
            }
        } catch (Exception e) {
            fechaFinalCargo = null;
            fechaInicialCargo = null;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:tabViewCosto");
            RequestContext.getCurrentInstance().execute("PF('errorExceptionFechas').show()");
            System.out.println("Error modificarFechaFinalModuloCargo Fechas Cargo : " + e.toString());
        }
    }

    public void modificarFechaFinalModuloCentroCosto(int i) {
        try {
            boolean retorno = false;
            casillaVigenciaLocalizacion = i;
            cambiarIndiceCentroCosto(i);
            retorno = validarFechasRegistroModuloCentroCosto();
            if (retorno == false) {
                fechaFinalCentroCosto = null;
                fechaInicialCentroCosto = null;
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:tabViewCentroCosto");
                RequestContext.getCurrentInstance().execute("PF('errorFechasIngresadas').show()");
            }
        } catch (Exception e) {
            fechaFinalCentroCosto = null;
            fechaInicialCentroCosto = null;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:tabViewCentroCosto");
            RequestContext.getCurrentInstance().execute("PF('errorExceptionFechas').show()");
            System.out.println("Error modificarFechaFinalModuloCentroCosto Fechas Centro Costo : " + e.toString());
        }
    }

    public void modificarFechaFinalModuloSueldo(int i) {
        try {
            boolean retorno = false;
            casillaVigenciaSueldo = i;
            cambiarIndiceSueldo(i);
            retorno = validarFechasRegistroModuloSueldo();
            if (retorno == false) {
                fechaFinalSueldo = null;
                fechaInicialSueldo = null;
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("formtabViewSueldo");
                RequestContext.getCurrentInstance().execute("PF('errorFechasIngresadas').show()");
            }
        } catch (Exception e) {
            fechaFinalSueldo = null;
            fechaInicialSueldo = null;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:tabViewSueldo");
            RequestContext.getCurrentInstance().execute("PF('errorExceptionFechas').show()");
            System.out.println("Error modificarFechaFinalModuloSueldo Fechas Sueldo : " + e.toString());
        }
    }

    public void modificarFechaFinalModuloFechaContrato(int i) {
        try {
            boolean retorno = false;
            casillaVigenciaTipoContrato = i;
            cambiarIndiceFechaContrato(i);
            retorno = validarFechasRegistroModuloFechaContrato();
            if (retorno == false) {
                fechaFinalFechaContrato = null;
                fechaInicialFechaContrato = null;
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:tabViewFechaContrato");
                RequestContext.getCurrentInstance().execute("PF('errorFechasIngresadas').show()");
            }
        } catch (Exception e) {
            fechaFinalFechaContrato = null;
            fechaInicialFechaContrato = null;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:tabViewFechaContrato");
            RequestContext.getCurrentInstance().execute("PF('errorExceptionFechas').show()");
            System.out.println("Error modificarFechaFinalModuloFechaContrato Fechas Fecha Contrato : " + e.toString());
        }
    }

    public void modificarFechaFinalModuloTipoTrabajador(int i) {
        try {
            boolean retorno = false;
            casillaVigenciaTipoTrabajador = i;
            cambiarIndiceTipoTrabajador(i);
            retorno = validarFechasRegistroModuloTipoTrabajador();
            if (retorno == false) {
                fechaFinalTipoTrabajador = null;
                fechaInicialTipoTrabajador = null;
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:tabViewTipoTrabajador");
                RequestContext.getCurrentInstance().execute("PF('errorFechasIngresadas').show()");
            }
        } catch (Exception e) {
            fechaFinalTipoTrabajador = null;
            fechaInicialTipoTrabajador = null;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:tabViewTipoTrabajador");
            RequestContext.getCurrentInstance().execute("PF('errorExceptionFechas').show()");
            System.out.println("Error modificarFechaFinalModuloTipoTrabajador Fechas Tipo Contrato : " + e.toString());
        }
    }

    public void modificarFechaFinalModuloTipoSalario(int i) {
        try {
            boolean retorno = false;
            casillaVigenciaReformaLaboral = i;
            cambiarIndiceTipoSalario(i);
            retorno = validarFechasRegistroModuloTipoSalario();
            if (retorno == false) {
                fechaFinalTipoSalario = null;
                fechaInicialTipoSalario = null;
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:tabViewTipoSalario");
                RequestContext.getCurrentInstance().execute("PF('errorFechasIngresadas').show()");
            }
        } catch (Exception e) {
            fechaFinalTipoSalario = null;
            fechaInicialTipoSalario = null;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:tabViewTipoSalario");
            RequestContext.getCurrentInstance().execute("PF('errorExceptionFechas').show()");
            System.out.println("Error modificarFechaFinalModuloTipoSalario Fechas Tipo Salario : " + e.toString());
        }
    }

    public void modificarFechaFinalModuloNormaLaboral(int i) {
        try {
            boolean retorno = false;
            casillaVigenciaNormaLaboral = i;
            cambiarIndiceNormaLaboral(i);
            retorno = validarFechasRegistroModuloNormaLaboral();
            if (retorno == false) {
                fechaFinalNormaLaboral = null;
                fechaInicialNormaLaboral = null;
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:tabViewNormaLaboral");
                RequestContext.getCurrentInstance().execute("PF('errorFechasIngresadas').show()");
            }
        } catch (Exception e) {
            fechaFinalNormaLaboral = null;
            fechaInicialNormaLaboral = null;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:tabViewNormaLaboral");
            RequestContext.getCurrentInstance().execute("PF('errorExceptionFechas').show()");
            System.out.println("Error modificarFechaFinalModuloNormaLaboral Fechas Norma Laboral : " + e.toString());
        }
    }

    public void modificarFechaMIFinalModuloLegislacionLaboral(int i) {
        try {
            if (fechaMIInicialLegislacionLaboral != null) {
                boolean retorno = false;
                casillaVigenciaContrato = i;
                cambiarIndiceLegislacionLaboral(i);
                retorno = validarFechasMIRegistroModuloLegislacionLaboral();
                if (retorno == false) {
                    fechaMIInicialLegislacionLaboral = null;
                    fechaMIFinalLegislacionLaboral = null;
                    RequestContext context = RequestContext.getCurrentInstance();
                    RequestContext.getCurrentInstance().update("form:tabViewLegislacionLaboral");
                    RequestContext.getCurrentInstance().execute("PF('errorFechasIngresadas').show()");
                }
            } else {
                RequestContext context = RequestContext.getCurrentInstance();
                fechaMIFinalLegislacionLaboral = null;
                RequestContext.getCurrentInstance().update("form:tabViewLegislacionLaboral");
                RequestContext.getCurrentInstance().execute("PF('errorFechasRequeridas').show()");
            }
        } catch (Exception e) {
            fechaMIInicialLegislacionLaboral = null;
            fechaMIFinalLegislacionLaboral = null;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:tabViewLegislacionLaboral");
            RequestContext.getCurrentInstance().execute("PF('errorExceptionFechas').show()");
            System.out.println("Error modificarFechaMIFinalModuloLegislacionLaboral Fechas Legislacion Laboral : " + e.toString());
        }
    }

    public void modificarFechaMFFinalModuloLegislacionLaboral(int i) {
        try {
            if (fechaMFInicialLegislacionLaboral != null) {
                boolean retorno = false;
                casillaVigenciaContrato = i;
                cambiarIndiceLegislacionLaboral(i);
                retorno = validarFechasMFRegistroModuloLegislacionLaboral();
                if (retorno == false) {
                    fechaMFInicialLegislacionLaboral = null;
                    fechaMFFinalLegislacionLaboral = null;
                    RequestContext context = RequestContext.getCurrentInstance();
                    RequestContext.getCurrentInstance().update("form:tabViewLegislacionLaboral");
                    RequestContext.getCurrentInstance().execute("PF('errorFechasIngresadas').show()");
                }
            } else {
                RequestContext context = RequestContext.getCurrentInstance();
                fechaMFFinalLegislacionLaboral = null;
                RequestContext.getCurrentInstance().update("form:tabViewLegislacionLaboral");
                RequestContext.getCurrentInstance().execute("PF('errorFechasRequeridas').show()");
            }
        } catch (Exception e) {
            fechaMFInicialLegislacionLaboral = null;
            fechaMFFinalLegislacionLaboral = null;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:tabViewLegislacionLaboral");
            RequestContext.getCurrentInstance().execute("PF('errorExceptionFechas').show()");
            System.out.println("Error modificarFechaMFFinalModuloLegislacionLaboral Fechas Legislacion Laboral : " + e.toString());
        }
    }

    public void modificarFechaFinalModuloUbicacion(int i) {
        try {
            boolean retorno = false;
            casillaVigenciaUbicacion = i;
            cambiarIndiceUbicacion(i);
            retorno = validarFechasRegistroModuloUbicacion();
            if (retorno == false) {
                fechaFinalUbicacion = null;
                fechaInicialUbicacion = null;
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:tabViewUbicacion");
                RequestContext.getCurrentInstance().execute("PF('errorFechasIngresadas').show()");
            }
        } catch (Exception e) {
            fechaFinalUbicacion = null;
            fechaInicialUbicacion = null;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:tabViewUbicacion");
            RequestContext.getCurrentInstance().execute("PF('errorExceptionFechas').show()");
            System.out.println("Error modificarFechaFinalModuloUbicacion Fechas Ubicacion : " + e.toString());
        }
    }

    public void modificarFechaFinalModuloAfiliacion(int i) {
        try {
            boolean retorno = false;
            casillaVigenciaAfiliacion = i;
            cambiarIndiceAfiliacion(i);
            retorno = validarFechasRegistroModuloAfiliacion();
            if (retorno == false) {
                fechaFinalAfiliacion = null;
                fechaInicialAfiliacion = null;
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:tabViewAfiliacion");
                RequestContext.getCurrentInstance().execute("PF('errorFechasIngresadas').show()");
            }
        } catch (Exception e) {
            fechaFinalAfiliacion = null;
            fechaInicialAfiliacion = null;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:tabViewAfiliacion");
            RequestContext.getCurrentInstance().execute("PF('errorExceptionFechas').show()");
            System.out.println("Error modificarFechaFinalModuloAfiliacion Fechas Afiliacion : " + e.toString());
        }
    }

    public void modificarFechaFinalModuloFormaPago(int i) {
        try {
            boolean retorno = false;
            casillaVigenciaFormaPago = i;
            cambiarIndiceFormaPago(i);
            retorno = validarFechasRegistroModuloFormaPago();
            if (retorno == false) {
                fechaFinalFormaPago = null;
                fechaInicialFormaPago = null;
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:tabViewFormaPago");
                RequestContext.getCurrentInstance().execute("PF('errorFechasIngresadas').show()");
            }
        } catch (Exception e) {
            fechaFinalFormaPago = null;
            fechaInicialFormaPago = null;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:tabViewFormaPago");
            RequestContext.getCurrentInstance().execute("PF('errorExceptionFechas').show()");
            System.out.println("Error modificarFechaFinalModuloFormaPago Fechas Forma Pago : " + e.toString());
        }
    }

    public void modificarFechaFinalModuloMvrs(int i) {
        try {
            boolean retorno = false;
            casillaMvrs = i;
            cambiarIndiceMvrs(i);
            retorno = validarFechasRegistroModuloMvrs();
            if (retorno == false) {
                fechaFinalMvrs = null;
                fechaInicialMvrs = null;
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:tabViewMvrs");
                RequestContext.getCurrentInstance().execute("PF('errorFechasIngresadas').show()");
            }
        } catch (Exception e) {
            fechaFinalMvrs = null;
            fechaInicialMvrs = null;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:tabViewMvrs");
            RequestContext.getCurrentInstance().execute("PF('errorExceptionFechas').show()");
            System.out.println("Error modificarFechaFinalModuloMvrs Fechas Mvrs : " + e.toString());
        }
    }

    public void modificarFechaMIFinalModuloSets(int i) {
        try {
            if (fechaMIInicialSets != null) {
                boolean retorno = false;
                casillaSets = i;
                cambiarIndiceSets(i);
                retorno = validarFechasMIRegistroModuloSets();
                if (retorno == false) {
                    fechaMIInicialSets = null;
                    fechaMIFinalSets = null;
                    RequestContext context = RequestContext.getCurrentInstance();
                    RequestContext.getCurrentInstance().update("form:tabViewSets");
                    RequestContext.getCurrentInstance().execute("PF('errorFechasIngresadas').show()");
                }
            } else {
                RequestContext context = RequestContext.getCurrentInstance();
                fechaMIFinalSets = null;
                RequestContext.getCurrentInstance().update("form:tabViewSets");
                RequestContext.getCurrentInstance().execute("PF('errorFechasRequeridas').show()");
            }
        } catch (Exception e) {
            fechaMIInicialSets = null;
            fechaMIFinalSets = null;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:tabViewSets");
            RequestContext.getCurrentInstance().execute("PF('errorExceptionFechas').show()");
            System.out.println("Error modificarFechaMIFinalModuloSets Fechas Sets : " + e.toString());
        }
    }

    public void modificarFechaMFFinalModuloSets(int i) {
        try {
            if (fechaMFInicialSets != null) {
                boolean retorno = false;
                casillaSets = i;
                cambiarIndiceSets(i);
                retorno = validarFechasMFRegistroModuloSets();
                if (retorno == false) {
                    fechaMFInicialSets = null;
                    fechaMFFinalSets = null;
                    RequestContext context = RequestContext.getCurrentInstance();
                    RequestContext.getCurrentInstance().update("formtabViewSets");
                    RequestContext.getCurrentInstance().execute("PF('errorFechasIngresadas').show()");
                }
            } else {
                RequestContext context = RequestContext.getCurrentInstance();
                fechaMFFinalSets = null;
                RequestContext.getCurrentInstance().update("form:tabViewSets");
                RequestContext.getCurrentInstance().execute("PF('errorFechasRequeridas').show()");
            }
        } catch (Exception e) {
            fechaMFInicialSets = null;
            fechaMFFinalSets = null;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:tabViewSets");
            RequestContext.getCurrentInstance().execute("PF('errorExceptionFechas').show()");
            System.out.println("Error modificarFechaMFFinalModuloLegislacionLaboral Fechas Legislacion Laboral : " + e.toString());
        }
    }

    public void modificarFechaMIFinalModuloVacacion(int i) {
        try {
            if (fechaMIInicialVacacion != null) {
                boolean retorno = false;
                casillaVacacion = i;
                cambiarIndiceVacacion(i);
                retorno = validarFechasMIRegistroModuloVacacion();
                if (retorno == false) {
                    fechaMIInicialVacacion = null;
                    fechaMIFinalVacacion = null;
                    RequestContext context = RequestContext.getCurrentInstance();
                    RequestContext.getCurrentInstance().update("form:tabViewVacacion");
                    RequestContext.getCurrentInstance().execute("PF('errorFechasIngresadas').show()");
                }
            } else {
                RequestContext context = RequestContext.getCurrentInstance();
                fechaMIFinalVacacion = null;
                RequestContext.getCurrentInstance().update("form:tabViewVacacion");
                RequestContext.getCurrentInstance().execute("PF('errorFechasRequeridas').show()");
            }
        } catch (Exception e) {
            fechaMIInicialVacacion = null;
            fechaMIFinalVacacion = null;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formtabViewVacacion");
            RequestContext.getCurrentInstance().execute("PF('errorExceptionFechas').show()");
            System.out.println("Error modificarFechaMIFinalModuloVacacion Fechas Vacacion : " + e.toString());
        }
    }

    public void modificarFechaMFFinalModuloVacacion(int i) {
        try {
            if (fechaMFInicialVacacion != null) {
                boolean retorno = false;
                casillaSets = i;
                cambiarIndiceVacacion(i);
                retorno = validarFechasMFRegistroModuloVacacion();
                if (retorno == false) {
                    fechaMFInicialVacacion = null;
                    fechaMFFinalVacacion = null;
                    RequestContext context = RequestContext.getCurrentInstance();
                    RequestContext.getCurrentInstance().update("form:tabViewVacacion");
                    RequestContext.getCurrentInstance().execute("PF('errorFechasIngresadas').show()");
                }
            } else {
                RequestContext context = RequestContext.getCurrentInstance();
                fechaMFFinalVacacion = null;
                RequestContext.getCurrentInstance().update("form:tabViewVacacion");
                RequestContext.getCurrentInstance().execute("PF('errorFechasRequeridas').show()");
            }
        } catch (Exception e) {
            fechaMFInicialVacacion = null;
            fechaMFFinalVacacion = null;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:tabViewVacacion");
            RequestContext.getCurrentInstance().execute("PF('errorExceptionFechas').show()");
            System.out.println("Error modificarFechaMFFinalModuloVacacion Fechas Legislacion Laboral : " + e.toString());
        }
    }

    public void modificarFechaFinalModuloJornadaLaboral(int i) {
        try {
            boolean retorno = false;
            casillaVigenciaJornadaLaboral = i;
            cambiarIndiceJornadaLaboral(i);
            retorno = validarFechasRegistroModuloJornadaLaboral();
            if (retorno == false) {
                fechaFinalJornadaLaboral = null;
                fechaInicialJornadaLaboral = null;
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:tabViewJornadaLaboral");
                RequestContext.getCurrentInstance().execute("PF('errorFechasIngresadas').show()");
            }
        } catch (Exception e) {
            fechaFinalJornadaLaboral = null;
            fechaInicialJornadaLaboral = null;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:tabViewJornadaLaboral");
            RequestContext.getCurrentInstance().execute("PF('errorExceptionFechas').show()");
            System.out.println("Error modificarFechaFinalModuloJornadaLaboral Fechas Jornada Laboral : " + e.toString());
        }
    }

    public void modificarFechaFinalModuloFechaRetiro(int i) {
        try {
            boolean retorno = false;
            casillaMotivoRetiro = i;
            cambiarIndiceFechaRetiro(i);
            retorno = validarFechasRegistroModuloFechaRetiro();
            if (retorno == false) {
                fechaFinalFechaRetiro = null;
                fechaInicialFechaRetiro = null;
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:tabViewFechaRetiro");
                RequestContext.getCurrentInstance().execute("PF('errorFechasIngresadas').show()");
            }
        } catch (Exception e) {
            fechaFinalFechaRetiro = null;
            fechaInicialFechaRetiro = null;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:tabViewFechaRetiro");
            RequestContext.getCurrentInstance().execute("PF('errorExceptionFechas').show()");
            System.out.println("Error modificarFechaFinalModuloFechaRetiro Fechas Fecha Retiro : " + e.toString());
        }
    }

    public void modificarFechaFinalModuloDatosPersonales(int i) {
        try {
            boolean retorno = false;
            casillaEmpleado = i;
            cambiarIndiceDatosPersonales(i);
            retorno = validarFechasRegistroModuloDatosPersonales();
            if (retorno == false) {
                fechaFinalDatosPersonales = null;
                fechaInicialDatosPersonales = null;
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:tabViewDatosPersonales");
                RequestContext.getCurrentInstance().execute("PF('errorFechasIngresadas').show()");
            }
        } catch (Exception e) {
            fechaFinalDatosPersonales = null;
            fechaInicialDatosPersonales = null;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:tabViewDatosPersonales");
            RequestContext.getCurrentInstance().execute("PF('errorExceptionFechas').show()");
            System.out.println("Error modificarFechaFinalModuloDatosPersonales Fechas Datos Personales : " + e.toString());
        }
    }

    public void modificarFechaFinalModuloEstadoCivil(int i) {
        try {
            boolean retorno = false;
            casillaEstadoCivil = i;
            cambiarIndiceEstadoCivil(i);
            retorno = validarFechasRegistroModuloEstadoCivil();
            if (retorno == false) {
                fechaFinalEstadoCivil = null;
                fechaInicialEstadoCivil = null;
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:tabViewEstadoCivil");
                RequestContext.getCurrentInstance().execute("PF('errorFechasIngresadas').show()");
            }
        } catch (Exception e) {
            fechaFinalEstadoCivil = null;
            fechaInicialEstadoCivil = null;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:tabViewEstadoCivil");
            RequestContext.getCurrentInstance().execute("PF('errorExceptionFechas').show()");
            System.out.println("Error modificarFechaFinalModuloEstadoCivil Fechas Estado Civil : " + e.toString());
        }
    }

    public void modificarFechaFinalModuloCenso(int i) {
        try {
            boolean retorno = false;
            casillaVigenciaIndicador = i;
            cambiarIndiceCenso(i);
            retorno = validarFechasRegistroModuloCenso();
            if (retorno == false) {
                fechaFinalCenso = null;
                fechaInicialCenso = null;
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:tabViewCenso");
                RequestContext.getCurrentInstance().execute("PF('errorFechasIngresadas').show()");
            }
        } catch (Exception e) {
            fechaFinalCenso = null;
            fechaInicialCenso = null;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:tabViewCenso");
            RequestContext.getCurrentInstance().execute("PF('errorExceptionFechas').show()");
            System.out.println("Error modificarFechaFinalModuloCenso Fechas Censos : " + e.toString());
        }
    }

    public void modificarFechaFinalModuloEducacionFormal(int i) {
        try {
            boolean retorno = false;
            casillaVigenciaFormal = i;
            cambiarIndiceEducacionFormal(i);
            retorno = validarFechasRegistroModuloEducacionFormal();
            if (retorno == false) {
                fechaFinalEducacionFormal = null;
                fechaInicialEducacionFormal = null;
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:tabViewEducacionFormal");
                RequestContext.getCurrentInstance().execute("PF('errorFechasIngresadas').show()");
            }
        } catch (Exception e) {
            fechaFinalEducacionFormal = null;
            fechaInicialEducacionFormal = null;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:tabViewEducacionFormal");
            RequestContext.getCurrentInstance().execute("PF('errorExceptionFechas').show()");
            System.out.println("Error modificarFechaFinalModuloEducacionFormal Fechas Educacion Formal : " + e.toString());
        }
    }

    public void modificarFechaFinalModuloEducacionNoFormal(int i) {
        try {
            boolean retorno = false;
            casillaVigenciaNoFormal = i;
            cambiarIndiceEducacionNoFormal(i);
            retorno = validarFechasRegistroModuloEducacionNoFormal();
            if (retorno == false) {
                fechaFinalEducacionNoFormal = null;
                fechaInicialEducacionNoFormal = null;
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:tabViewEducacionNoFormal");
                RequestContext.getCurrentInstance().execute("PF('errorFechasIngresadas').show()");
            }
        } catch (Exception e) {
            fechaFinalEducacionNoFormal = null;
            fechaInicialEducacionNoFormal = null;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:tabViewEducacionNoFormal");
            RequestContext.getCurrentInstance().execute("PF('errorExceptionFechas').show()");
            System.out.println("Error modificarFechaFinalModuloEducacionNoFormal Fechas Educacion No Formal : " + e.toString());
        }
    }

    public void modificarFechaFinalModuloExperienciaLaboral(int i) {
        try {
            boolean retorno = false;
            casillaHvExperienciaLaboral = i;
            cambiarIndiceExperienciaLaboral(i);
            retorno = validarFechasRegistroModuloExperienciaLaboral();
            if (retorno == false) {
                fechaFinalExperienciaLaboral = null;
                fechaInicialExperienciaLaboral = null;
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:tabViewExperienciaLaboral");
                RequestContext.getCurrentInstance().execute("PF('errorFechasIngresadas').show()");
            }
        } catch (Exception e) {
            fechaFinalExperienciaLaboral = null;
            fechaInicialExperienciaLaboral = null;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:tabViewExperienciaLaboral");
            RequestContext.getCurrentInstance().execute("PF('errorExceptionFechas').show()");
            System.out.println("Error modificarFechaFinalModuloEducacionNoFormal Fechas Experiencia Laboral : " + e.toString());
        }
    }

    public void modificarFechaFinalModuloProyecto(int i) {
        try {
            boolean retorno = false;
            casillaVigenciaProyecto = i;
            cambiarIndiceProyecto(i);
            retorno = validarFechasRegistroModuloProyecto();
            if (retorno == false) {
                fechaFinalProyecto = null;
                fechaInicialProyecto = null;
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:tabViewProyecto");
                RequestContext.getCurrentInstance().execute("PF('errorFechasIngresadas').show()");
            }
        } catch (Exception e) {
            fechaFinalProyecto = null;
            fechaInicialProyecto = null;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:tabViewProyecto");
            RequestContext.getCurrentInstance().execute("PF('errorExceptionFechas').show()");
            System.out.println("Error modificarFechaFinalModuloEducacionNoFormal Fechas Proyecto : " + e.toString());
        }
    }

    public boolean validarFechasRegistroModuloCargo() {
        fechaParametro = new Date();
        fechaParametro.setYear(0);
        fechaParametro.setMonth(1);
        fechaParametro.setDate(1);
        boolean retorno = true;
        if (fechaInicialCargo != null && fechaFinalCargo != null) {
            if (fechaInicialCargo.after(fechaParametro) && fechaInicialCargo.before(fechaFinalCargo)) {
                retorno = true;
            } else {
                retorno = false;
            }
        }
        return retorno;
    }

    public boolean validarFechasRegistroModuloCentroCosto() {
        fechaParametro = new Date();
        fechaParametro.setYear(0);
        fechaParametro.setMonth(1);
        fechaParametro.setDate(1);
        boolean retorno = true;
        if (fechaInicialCentroCosto != null && fechaFinalCentroCosto != null) {
            if (fechaInicialCentroCosto.after(fechaParametro) && fechaInicialCentroCosto.before(fechaFinalCentroCosto)) {
                retorno = true;
            } else {
                retorno = false;
            }
        }
        return retorno;
    }

    public boolean validarFechasRegistroModuloSueldo() {
        fechaParametro = new Date();
        fechaParametro.setYear(0);
        fechaParametro.setMonth(1);
        fechaParametro.setDate(1);
        boolean retorno = true;
        if (fechaInicialSueldo != null && fechaFinalSueldo != null) {
            if (fechaInicialSueldo.after(fechaParametro) && fechaInicialSueldo.before(fechaFinalSueldo)) {
                retorno = true;
            } else {
                retorno = false;
            }
        }
        return retorno;
    }

    public boolean validarFechasRegistroModuloFechaContrato() {
        fechaParametro = new Date();
        fechaParametro.setYear(0);
        fechaParametro.setMonth(1);
        fechaParametro.setDate(1);
        boolean retorno = true;
        if (fechaInicialFechaContrato != null && fechaFinalFechaContrato != null) {
            if (fechaInicialFechaContrato.after(fechaParametro) && fechaInicialFechaContrato.before(fechaFinalFechaContrato)) {
                retorno = true;
            } else {
                retorno = false;
            }
        }
        return retorno;
    }

    public boolean validarFechasRegistroModuloTipoTrabajador() {
        fechaParametro = new Date();
        fechaParametro.setYear(0);
        fechaParametro.setMonth(1);
        fechaParametro.setDate(1);
        boolean retorno = true;
        if (fechaInicialTipoTrabajador != null && fechaFinalTipoTrabajador != null) {
            if (fechaInicialTipoTrabajador.after(fechaParametro) && fechaInicialTipoTrabajador.before(fechaFinalTipoTrabajador)) {
                retorno = true;
            } else {
                retorno = false;
            }
        }
        return retorno;
    }

    public boolean validarFechasRegistroModuloTipoSalario() {
        fechaParametro = new Date();
        fechaParametro.setYear(0);
        fechaParametro.setMonth(1);
        fechaParametro.setDate(1);
        boolean retorno = true;
        if (fechaInicialTipoSalario != null && fechaFinalTipoSalario != null) {
            if (fechaInicialTipoSalario.after(fechaParametro) && fechaInicialTipoSalario.before(fechaFinalTipoSalario)) {
                retorno = true;
            } else {
                retorno = false;
            }
        }
        return retorno;
    }

    public boolean validarFechasRegistroModuloNormaLaboral() {
        fechaParametro = new Date();
        fechaParametro.setYear(0);
        fechaParametro.setMonth(1);
        fechaParametro.setDate(1);
        boolean retorno = true;
        if (fechaInicialNormaLaboral != null && fechaFinalNormaLaboral != null) {
            if (fechaInicialNormaLaboral.after(fechaParametro) && fechaInicialNormaLaboral.before(fechaFinalNormaLaboral)) {
                retorno = true;
            } else {
                retorno = false;
            }
        }
        return retorno;
    }

    public boolean validarFechasMFRegistroModuloLegislacionLaboral() {
        fechaParametro = new Date();
        fechaParametro.setYear(0);
        fechaParametro.setMonth(1);
        fechaParametro.setDate(1);
        boolean retorno = true;
        if (fechaMFFinalLegislacionLaboral != null && fechaMFInicialLegislacionLaboral != null) {
            if (fechaMFInicialLegislacionLaboral.after(fechaParametro) && fechaMFInicialLegislacionLaboral.before(fechaMFFinalLegislacionLaboral)) {
                retorno = true;
            } else {
                retorno = false;
            }
        }
        return retorno;
    }

    public boolean validarFechasMIRegistroModuloLegislacionLaboral() {
        fechaParametro = new Date();
        fechaParametro.setYear(0);
        fechaParametro.setMonth(1);
        fechaParametro.setDate(1);
        boolean retorno = true;
        if (fechaMIFinalLegislacionLaboral != null && fechaMIInicialLegislacionLaboral != null) {
            if (fechaMIInicialLegislacionLaboral.after(fechaParametro) && fechaMIInicialLegislacionLaboral.before(fechaMIFinalLegislacionLaboral)) {
                retorno = true;
            } else {
                retorno = false;
            }
        }
        return retorno;
    }

    public boolean validarFechasRegistroModuloUbicacion() {
        fechaParametro = new Date();
        fechaParametro.setYear(0);
        fechaParametro.setMonth(1);
        fechaParametro.setDate(1);
        boolean retorno = true;
        if (fechaInicialUbicacion != null && fechaFinalUbicacion != null) {
            if (fechaInicialUbicacion.after(fechaParametro) && fechaInicialUbicacion.before(fechaFinalUbicacion)) {
                retorno = true;
            } else {
                retorno = false;
            }
        }
        return retorno;
    }

    public boolean validarFechasRegistroModuloAfiliacion() {
        fechaParametro = new Date();
        fechaParametro.setYear(0);
        fechaParametro.setMonth(1);
        fechaParametro.setDate(1);
        boolean retorno = true;
        if (fechaInicialAfiliacion != null && fechaFinalAfiliacion != null) {
            if (fechaInicialAfiliacion.after(fechaParametro) && fechaInicialAfiliacion.before(fechaFinalAfiliacion)) {
                retorno = true;
            } else {
                retorno = false;
            }
        }
        return retorno;
    }

    public boolean validarFechasRegistroModuloFormaPago() {
        fechaParametro = new Date();
        fechaParametro.setYear(0);
        fechaParametro.setMonth(1);
        fechaParametro.setDate(1);
        boolean retorno = true;
        if (fechaInicialFormaPago != null && fechaFinalFormaPago != null) {
            if (fechaInicialFormaPago.after(fechaParametro) && fechaInicialFormaPago.before(fechaFinalFormaPago)) {
                retorno = true;
            } else {
                retorno = false;
            }
        }
        return retorno;
    }

    public boolean validarFechasRegistroModuloMvrs() {
        fechaParametro = new Date();
        fechaParametro.setYear(0);
        fechaParametro.setMonth(1);
        fechaParametro.setDate(1);
        boolean retorno = true;
        if (fechaInicialMvrs != null && fechaFinalMvrs != null) {
            if (fechaInicialMvrs.after(fechaParametro) && fechaInicialMvrs.before(fechaFinalMvrs)) {
                retorno = true;
            } else {
                retorno = false;
            }
        }
        return retorno;
    }

    public boolean validarFechasMFRegistroModuloSets() {
        fechaParametro = new Date();
        fechaParametro.setYear(0);
        fechaParametro.setMonth(1);
        fechaParametro.setDate(1);
        boolean retorno = true;
        if (fechaMFFinalSets != null && fechaMFInicialSets != null) {
            if (fechaMFInicialSets.after(fechaParametro) && fechaMFInicialSets.before(fechaMFFinalSets)) {
                retorno = true;
            } else {
                retorno = false;
            }
        }
        return retorno;
    }

    public boolean validarFechasMIRegistroModuloSets() {
        fechaParametro = new Date();
        fechaParametro.setYear(0);
        fechaParametro.setMonth(1);
        fechaParametro.setDate(1);
        boolean retorno = true;
        if (fechaMIFinalSets != null && fechaMIInicialSets != null) {
            if (fechaMIInicialSets.after(fechaParametro) && fechaMIInicialSets.before(fechaMIFinalSets)) {
                retorno = true;
            } else {
                retorno = false;
            }
        }
        return retorno;
    }

    public boolean validarFechasMFRegistroModuloVacacion() {
        fechaParametro = new Date();
        fechaParametro.setYear(0);
        fechaParametro.setMonth(1);
        fechaParametro.setDate(1);
        boolean retorno = true;
        if (fechaMFFinalVacacion != null && fechaMFInicialVacacion != null) {
            if (fechaMFInicialVacacion.after(fechaParametro) && fechaMFInicialVacacion.before(fechaMFFinalVacacion)) {
                retorno = true;
            } else {
                retorno = false;
            }
        }
        return retorno;
    }

    public boolean validarFechasMIRegistroModuloVacacion() {
        fechaParametro = new Date();
        fechaParametro.setYear(0);
        fechaParametro.setMonth(1);
        fechaParametro.setDate(1);
        boolean retorno = true;
        if (fechaMIFinalVacacion != null && fechaMIInicialVacacion != null) {
            if (fechaMIInicialVacacion.after(fechaParametro) && fechaMIInicialVacacion.before(fechaMIFinalVacacion)) {
                retorno = true;
            } else {
                retorno = false;
            }
        }
        return retorno;
    }

    public boolean validarFechasRegistroModuloJornadaLaboral() {
        fechaParametro = new Date();
        fechaParametro.setYear(0);
        fechaParametro.setMonth(1);
        fechaParametro.setDate(1);
        boolean retorno = true;
        if (fechaFinalJornadaLaboral != null && fechaInicialJornadaLaboral != null) {
            if (fechaInicialJornadaLaboral.after(fechaParametro) && fechaInicialJornadaLaboral.before(fechaFinalJornadaLaboral)) {
                retorno = true;
            } else {
                retorno = false;
            }
        }
        return retorno;
    }

    public boolean validarFechasRegistroModuloFechaRetiro() {
        fechaParametro = new Date();
        fechaParametro.setYear(0);
        fechaParametro.setMonth(1);
        fechaParametro.setDate(1);
        boolean retorno = true;
        if (fechaFinalFechaRetiro != null && fechaInicialFechaRetiro != null) {
            if (fechaInicialFechaRetiro.after(fechaParametro) && fechaInicialFechaRetiro.before(fechaFinalFechaRetiro)) {
                retorno = true;
            } else {
                retorno = false;
            }
        }
        return retorno;
    }

    public boolean validarFechasRegistroModuloDatosPersonales() {
        fechaParametro = new Date();
        fechaParametro.setYear(0);
        fechaParametro.setMonth(1);
        fechaParametro.setDate(1);
        boolean retorno = true;
        if (fechaFinalDatosPersonales != null && fechaInicialDatosPersonales != null) {
            if (fechaInicialDatosPersonales.after(fechaParametro) && fechaInicialDatosPersonales.before(fechaFinalDatosPersonales)) {
                retorno = true;
            } else {
                retorno = false;
            }
        }
        return retorno;
    }

    public boolean validarFechasRegistroModuloEstadoCivil() {
        fechaParametro = new Date();
        fechaParametro.setYear(0);
        fechaParametro.setMonth(1);
        fechaParametro.setDate(1);
        boolean retorno = true;
        if (fechaFinalEstadoCivil != null && fechaInicialEstadoCivil != null) {
            if (fechaInicialEstadoCivil.after(fechaParametro) && fechaInicialEstadoCivil.before(fechaFinalEstadoCivil)) {
                retorno = true;
            } else {
                retorno = false;
            }
        }
        return retorno;
    }

    public boolean validarFechasRegistroModuloCenso() {
        fechaParametro = new Date();
        fechaParametro.setYear(0);
        fechaParametro.setMonth(1);
        fechaParametro.setDate(1);
        boolean retorno = true;
        if (fechaFinalCenso != null && fechaInicialCenso != null) {
            if (fechaInicialCenso.after(fechaParametro) && fechaInicialCenso.before(fechaFinalCenso)) {
                retorno = true;
            } else {
                retorno = false;
            }
        }
        return retorno;
    }

    public boolean validarFechasRegistroModuloEducacionFormal() {
        fechaParametro = new Date();
        fechaParametro.setYear(0);
        fechaParametro.setMonth(1);
        fechaParametro.setDate(1);
        boolean retorno = true;
        if (fechaFinalEducacionFormal != null && fechaInicialEducacionFormal != null) {
            if (fechaInicialEducacionFormal.after(fechaParametro) && fechaInicialEducacionFormal.before(fechaFinalEducacionFormal)) {
                retorno = true;
            } else {
                retorno = false;
            }
        }
        return retorno;
    }

    public boolean validarFechasRegistroModuloEducacionNoFormal() {
        fechaParametro = new Date();
        fechaParametro.setYear(0);
        fechaParametro.setMonth(1);
        fechaParametro.setDate(1);
        boolean retorno = true;
        if (fechaFinalEducacionNoFormal != null && fechaInicialEducacionNoFormal != null) {
            if (fechaInicialEducacionNoFormal.after(fechaParametro) && fechaInicialEducacionNoFormal.before(fechaFinalEducacionNoFormal)) {
                retorno = true;
            } else {
                retorno = false;
            }
        }
        return retorno;
    }

    public boolean validarFechasRegistroModuloExperienciaLaboral() {
        fechaParametro = new Date();
        fechaParametro.setYear(0);
        fechaParametro.setMonth(1);
        fechaParametro.setDate(1);
        boolean retorno = true;
        if (fechaFinalExperienciaLaboral != null && fechaInicialExperienciaLaboral != null) {
            if (fechaInicialExperienciaLaboral.after(fechaParametro) && fechaInicialExperienciaLaboral.before(fechaFinalExperienciaLaboral)) {
                retorno = true;
            } else {
                retorno = false;
            }
        }
        return retorno;
    }

    public boolean validarFechasRegistroModuloProyecto() {
        fechaParametro = new Date();
        fechaParametro.setYear(0);
        fechaParametro.setMonth(1);
        fechaParametro.setDate(1);
        boolean retorno = true;
        if (fechaFinalProyecto != null && fechaInicialProyecto != null) {
            if (fechaInicialProyecto.after(fechaParametro) && fechaInicialProyecto.before(fechaFinalProyecto)) {
                retorno = true;
            } else {
                retorno = false;
            }
        }
        return retorno;
    }

    public void modificarParametrosCargo(String cualParametro, String valorConfirmar) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (cualParametro.equals("ESTRUCTURA")) {
            if (valorConfirmar.isEmpty()) {
                vigenciaCargoBA.setEstructura(new Estructuras());
                vigenciaCargoBA.getEstructura().setCentrocosto(new CentrosCostos());
                RequestContext.getCurrentInstance().update("form:tabViewCosto:parametroEstructuraModCargo");
                RequestContext.getCurrentInstance().update("form:tabViewCosto:parametroCentroCostoModCargo");
            } else {
                vigenciaCargoBA.getEstructura().setNombre(auxEstructuraVigenciaCargo);
                for (int i = 0; i < lovEstructuras.size(); i++) {
                    if (lovEstructuras.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    vigenciaCargoBA.setEstructura(lovEstructuras.get(indiceUnicoElemento));
                    lovEstructuras = null;
                    getLovEstructuras();
                    RequestContext.getCurrentInstance().update("form:tabViewCosto:parametroEstructuraModCargo");
                    RequestContext.getCurrentInstance().update("form:tabViewCosto:parametroCentroCostoModCargo");
                } else {
                    permitirIndexVigenciaCargo = false;
                    RequestContext.getCurrentInstance().update("form:EstructuraCargoDialogo");
                    RequestContext.getCurrentInstance().execute("PF('EstructuraCargoDialogo').show()");
                }
            }
        }
        if (cualParametro.equals("CARGO")) {
            if (valorConfirmar.isEmpty()) {
                vigenciaCargoBA.setCargo(new Cargos());
                RequestContext.getCurrentInstance().update("form:tabViewCosto:parametroCargoModCargo");
            } else {
                vigenciaCargoBA.getCargo().setNombre(auxCargoVigenciaCargo);
                for (int i = 0; i < lovCargos.size(); i++) {
                    if (lovCargos.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    vigenciaCargoBA.setCargo(lovCargos.get(indiceUnicoElemento));
                    lovCargos = null;
                    getLovCargos();
                    RequestContext.getCurrentInstance().update("form:tabViewCosto:parametroCargoModCargo");
                } else {
                    permitirIndexVigenciaCargo = false;
                    RequestContext.getCurrentInstance().update("form:CargoCargoDialogo");
                    RequestContext.getCurrentInstance().execute("PF('CargoCargoDialogo').show()");
                }
            }
        }
        if (cualParametro.equals("PAPEL")) {
            if (valorConfirmar.isEmpty()) {
                vigenciaCargoBA.setPapel(new Papeles());
                RequestContext.getCurrentInstance().update("form:tabViewCosto:parametroPapelModCargo");
            } else {
                vigenciaCargoBA.getPapel().setDescripcion(auxPapelVigenciaCargo);
                for (int i = 0; i < lovPapeles.size(); i++) {
                    if (lovPapeles.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    vigenciaCargoBA.setPapel(lovPapeles.get(indiceUnicoElemento));
                    lovPapeles = null;
                    getLovPapeles();
                    RequestContext.getCurrentInstance().update("form:tabViewCosto:parametroPapelModCargo");
                } else {
                    permitirIndexVigenciaCargo = false;
                    RequestContext.getCurrentInstance().update("form:PapelCargoDialogo");
                    RequestContext.getCurrentInstance().execute("PF('PapelCargoDialogo').show()");
                }
            }
        }
        if (cualParametro.equals("MOTIVO")) {
            if (valorConfirmar.isEmpty()) {
                vigenciaCargoBA.setMotivocambiocargo(new MotivosCambiosCargos());
                RequestContext.getCurrentInstance().update("form:tabViewCosto:parametroMotivoModCargo");
            } else {
                vigenciaCargoBA.getMotivocambiocargo().setNombre(auxMotivoCambioCargoVigenciaCargo);
                for (int i = 0; i < lovMotivosCambiosCargos.size(); i++) {
                    if (lovMotivosCambiosCargos.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    vigenciaCargoBA.setMotivocambiocargo(lovMotivosCambiosCargos.get(indiceUnicoElemento));
                    lovMotivosCambiosCargos = null;
                    getLovMotivosCambiosCargos();
                    RequestContext.getCurrentInstance().update("form:tabViewCosto:parametroMotivoModCargo");
                } else {
                    permitirIndexVigenciaCargo = false;
                    RequestContext.getCurrentInstance().update("form:MotivoCargoDialogo");
                    RequestContext.getCurrentInstance().execute("PF('MotivoCargoDialogo').show()");
                }
            }
        }
        if (cualParametro.equals("JEFE")) {
            if (valorConfirmar.isEmpty()) {
                vigenciaCargoBA.setEmpleadojefe(new Empleados());
                vigenciaCargoBA.getEmpleadojefe().setPersona(new Personas());
                RequestContext.getCurrentInstance().update("form:tabViewCosto:parametroJefeModCargo");
            } else {
                vigenciaCargoBA.getEmpleadojefe().getPersona().setNombreCompleto(auxJefeVigenciaCargo);
                for (int i = 0; i < lovEmpleados.size(); i++) {
                    if (lovEmpleados.get(i).getPersona().getNombreCompleto().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    vigenciaCargoBA.setEmpleadojefe(lovEmpleados.get(indiceUnicoElemento));
                    lovEmpleados = null;
                    getLovEmpleados();
                    RequestContext.getCurrentInstance().update("form:tabViewCosto:parametroJefeModCargo");
                } else {
                    permitirIndexVigenciaCargo = false;
                    RequestContext.getCurrentInstance().update("form:JefeCargoDialogo");
                    RequestContext.getCurrentInstance().execute("PF('JefeCargoDialogo').show()");
                }
            }
        }
    }

    public void modificarParametrosCentroCosto(String cualParametro, String valorConfirmar) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (cualParametro.equals("LOCALIZACION")) {
            if (valorConfirmar.isEmpty()) {
                vigenciaLocalizacionBA.setLocalizacion(new Estructuras());
                RequestContext.getCurrentInstance().update("form:tabViewCentroCosto:parametroLocalizacionModCentroCosto");
            } else {
                vigenciaLocalizacionBA.getLocalizacion().setNombre(auxLocalizacionVigenciaLocalizacion);
                for (int i = 0; i < lovEstructuras.size(); i++) {
                    if (lovEstructuras.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    vigenciaLocalizacionBA.setLocalizacion(lovEstructuras.get(indiceUnicoElemento));
                    lovEstructuras = null;
                    getLovEstructuras();
                    RequestContext.getCurrentInstance().update("form:tabViewCentroCosto:parametroLocalizacionModCentroCosto");
                } else {
                    permitirIndexVigenciaLocalizacion = false;
                    RequestContext.getCurrentInstance().update("form:LocalizacionCentroCostoDialogo");
                    RequestContext.getCurrentInstance().execute("PF('LocalizacionCentroCostoDialogo').show()");
                }
            }
        }
        if (cualParametro.equals("MOTIVO")) {
            if (valorConfirmar.isEmpty()) {
                vigenciaLocalizacionBA.setMotivo(new MotivosLocalizaciones());
                RequestContext.getCurrentInstance().update("form:tabViewCentroCosto:parametroMotivoModCentroCosto");
            } else {
                vigenciaLocalizacionBA.getMotivo().setDescripcion(auxMotivoLocalizacionVigenciaLocalizacion);
                for (int i = 0; i < lovMotivosLocalizaciones.size(); i++) {
                    if (lovMotivosLocalizaciones.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    vigenciaLocalizacionBA.setMotivo(lovMotivosLocalizaciones.get(indiceUnicoElemento));
                    lovMotivosLocalizaciones = null;
                    getLovMotivosLocalizaciones();
                    RequestContext.getCurrentInstance().update("form:tabViewCentroCosto:parametroMotivoModCentroCosto");
                } else {
                    permitirIndexVigenciaLocalizacion = false;
                    RequestContext.getCurrentInstance().update("form:MotivoCentroCostoDialogo");
                    RequestContext.getCurrentInstance().execute("PF('MotivoCentroCostoDialogo').show()");
                }
            }
        }
    }

    public void modificarParametrosSueldo(String cualParametro, String valorConfirmar) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (cualParametro.equals("TIPOSUELDO")) {
            if (valorConfirmar.isEmpty()) {
                vigenciaSueldoBA.setTiposueldo(new TiposSueldos());
                RequestContext.getCurrentInstance().update("form:tabViewSueldo:parametroTipoSueldoModSueldo");
            } else {
                vigenciaSueldoBA.getTiposueldo().setDescripcion(auxTipoSueldoVigenciaSueldo);
                for (int i = 0; i < lovTiposSueldos.size(); i++) {
                    if (lovTiposSueldos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    vigenciaSueldoBA.setTiposueldo(lovTiposSueldos.get(indiceUnicoElemento));
                    lovTiposSueldos = null;
                    getLovTiposSueldos();
                    RequestContext.getCurrentInstance().update("form:tabViewSueldo:parametroTipoSueldoModSueldo");
                } else {
                    permitirIndexVigenciaSueldo = false;
                    RequestContext.getCurrentInstance().update("form:TipoSueldoSueldoDialogo");
                    RequestContext.getCurrentInstance().execute("PF('TipoSueldoSueldoDialogo').show()");
                }
            }
        }
        if (cualParametro.equals("MOTIVO")) {
            if (valorConfirmar.isEmpty()) {
                vigenciaSueldoBA.setMotivocambiosueldo(new MotivosCambiosSueldos());
                RequestContext.getCurrentInstance().update("form:tabViewSueldo:parametroMotivoModSueldo");
            } else {
                vigenciaSueldoBA.getMotivocambiosueldo().setNombre(auxMotivoCambioSueldoVigenciaSueldo);
                for (int i = 0; i < lovMotivosCambiosSueldos.size(); i++) {
                    if (lovMotivosCambiosSueldos.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    vigenciaSueldoBA.setMotivocambiosueldo(lovMotivosCambiosSueldos.get(indiceUnicoElemento));
                    lovMotivosCambiosSueldos = null;
                    getLovMotivosCambiosSueldos();
                    RequestContext.getCurrentInstance().update("form:tabViewSueldo:parametroMotivoModSueldo");
                } else {
                    permitirIndexVigenciaSueldo = false;
                    RequestContext.getCurrentInstance().update("form:MotivoSueldoDialogo");
                    RequestContext.getCurrentInstance().execute("PF('MotivoSueldoDialogo').show()");
                }
            }
        }
    }

    public void modificarParametrosFechaContrato(String cualParametro, String valorConfirmar) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (cualParametro.equals("TIPOCONTRATO")) {
            if (valorConfirmar.isEmpty()) {
                vigenciaTipoContratoBA.setTipocontrato(new TiposContratos());
                RequestContext.getCurrentInstance().update("form:tabViewFechaContrato:parametroTipoContratoModFechaContrato");
            } else {
                vigenciaTipoContratoBA.getTipocontrato().setNombre(auxTipoContratoVigenciaTipoContrato);
                for (int i = 0; i < lovTiposContratos.size(); i++) {
                    if (lovTiposContratos.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    vigenciaTipoContratoBA.setTipocontrato(lovTiposContratos.get(indiceUnicoElemento));
                    lovTiposContratos = null;
                    getLovTiposContratos();
                    RequestContext.getCurrentInstance().update("form:tabViewFechaContrato:parametroTipoContratoModFechaContrato");
                } else {
                    permitirIndexVigenciaTipoContrato = false;
                    RequestContext.getCurrentInstance().update("form:TipoContratoFechaContratoDialogo");
                    RequestContext.getCurrentInstance().execute("PF('TipoContratoFechaContratoDialogo').show()");
                }
            }
        }
        if (cualParametro.equals("MOTIVO")) {
            if (valorConfirmar.isEmpty()) {
                vigenciaTipoContratoBA.setMotivocontrato(new MotivosContratos());
                RequestContext.getCurrentInstance().update("form:tabViewFechaContrato:parametroMotivoModFechaContrato");
            } else {
                vigenciaTipoContratoBA.getMotivocontrato().setNombre(auxMotivoContratoVigenciaTipoContrato);
                for (int i = 0; i < lovMotivosContratos.size(); i++) {
                    if (lovMotivosContratos.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    vigenciaTipoContratoBA.setMotivocontrato(lovMotivosContratos.get(indiceUnicoElemento));
                    lovMotivosContratos = null;
                    getLovMotivosContratos();
                    RequestContext.getCurrentInstance().update("form:tabViewFechaContrato:parametroMotivoModFechaContrato");
                } else {
                    permitirIndexVigenciaTipoContrato = false;
                    RequestContext.getCurrentInstance().update("form:MotivoFechaContratoDialogo");
                    RequestContext.getCurrentInstance().execute("PF('MotivoFechaContratoDialogo').show()");
                }
            }
        }
    }

    public void modificarParametrosTipoTrabajador(String cualParametro, String valorConfirmar) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (cualParametro.equals("TIPOTRABAJADOR")) {
            if (valorConfirmar.isEmpty()) {
                vigenciaTipoTrabajadorBA.setTipotrabajador(new TiposTrabajadores());
                RequestContext.getCurrentInstance().update("form:tabViewTipoTrabajador:parametroTipoTrabajadorModTipoContrato");
            } else {
                vigenciaTipoTrabajadorBA.getTipotrabajador().setNombre(auxTipoTrabajadorVigenciaTipoTrabajador);
                for (int i = 0; i < lovTiposTrabajadores.size(); i++) {
                    if (lovTiposTrabajadores.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    vigenciaTipoTrabajadorBA.setTipotrabajador(lovTiposTrabajadores.get(indiceUnicoElemento));
                    lovTiposTrabajadores = null;
                    getLovTiposTrabajadores();
                    RequestContext.getCurrentInstance().update("form:tabViewTipoTrabajador:parametroTipoTrabajadorModTipoContrato");
                } else {
                    permitirIndexVigenciaTipoTrabajador = false;
                    RequestContext.getCurrentInstance().update("form:TipoTrabajadorTipoTrabajadorDialogo");
                    RequestContext.getCurrentInstance().execute("PF('TipoTrabajadorTipoTrabajadorDialogo').show()");
                }
            }
        }
    }

    public void modificarParametrosTipoSalario(String cualParametro, String valorConfirmar) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (cualParametro.equals("REFORMA")) {
            if (valorConfirmar.isEmpty()) {
                vigenciaReformaLaboralBA.setReformalaboral(new ReformasLaborales());
                RequestContext.getCurrentInstance().update("form:tabViewTipoSalario:parametroReformaLaboralModTipoSalario");
            } else {
                vigenciaReformaLaboralBA.getReformalaboral().setNombre(auxReformaLaboralVigenciaReformaLaboral);
                for (int i = 0; i < lovReformasLaborales.size(); i++) {
                    if (lovReformasLaborales.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    vigenciaReformaLaboralBA.setReformalaboral(lovReformasLaborales.get(indiceUnicoElemento));
                    lovReformasLaborales = null;
                    getLovReformasLaborales();
                    RequestContext.getCurrentInstance().update("form:tabViewTipoSalario:parametroReformaLaboralModTipoSalario");
                } else {
                    permitirIndexVigenciaTipoTrabajador = false;
                    RequestContext.getCurrentInstance().update("form:ReformaLaboralTipoSalarioDialogo");
                    RequestContext.getCurrentInstance().execute("PF('ReformaLaboralTipoSalarioDialogo').show()");
                }
            }
        }
    }

    public void modificarParametrosNormaLaboral(String cualParametro, String valorConfirmar) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (cualParametro.equals("NORMA")) {
            if (valorConfirmar.isEmpty()) {
                vigenciaNormaEmpleadoBA.setNormalaboral(new NormasLaborales());
                RequestContext.getCurrentInstance().update("form:tabViewNormaLaboral:parametroNormaLaboralModNormaLaboral");
            } else {
                vigenciaNormaEmpleadoBA.getNormalaboral().setNombre(auxNormaLaboralVigenciaNormaEmpleado);
                for (int i = 0; i < lovNormasLaborales.size(); i++) {
                    if (lovNormasLaborales.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    vigenciaNormaEmpleadoBA.setNormalaboral(lovNormasLaborales.get(indiceUnicoElemento));
                    lovNormasLaborales = null;
                    getLovNormasLaborales();
                    RequestContext.getCurrentInstance().update("form:tabViewNormaLaboral:parametroNormaLaboralModNormaLaboral");
                } else {
                    permitirIndexVigenciaNormaEmpleado = false;
                    RequestContext.getCurrentInstance().update("form:NormaLaboralNormaLaboralDialogo");
                    RequestContext.getCurrentInstance().execute("PF('NormaLaboralNormaLaboralDialogo').show()");
                }
            }
        }
    }

    public void modificarParametrosLegislacionLaboral(String cualParametro, String valorConfirmar) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (cualParametro.equals("CONTRATO")) {
            if (valorConfirmar.isEmpty()) {
                vigenciaContratoBA.setContrato(new Contratos());
                RequestContext.getCurrentInstance().update("form:tabViewLegislacionLaboral:parametroLegislacionModLegislacionLaboral");
            } else {
                vigenciaContratoBA.getContrato().setDescripcion(auxContratoVigenciaContrato);
                for (int i = 0; i < lovContratos.size(); i++) {
                    if (lovContratos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    vigenciaContratoBA.setContrato(lovContratos.get(indiceUnicoElemento));
                    lovContratos = null;
                    getLovContratos();
                    RequestContext.getCurrentInstance().update("form:tabViewLegislacionLaboral:parametroLegislacionModLegislacionLaboral");
                } else {
                    permitirIndexVigenciaContrato = false;
                    RequestContext.getCurrentInstance().update("form:LegislacionLegislacionLaboralDialogo");
                    RequestContext.getCurrentInstance().execute("PF('LegislacionLegislacionLaboralDialogo').show()");
                }
            }
        }
    }

    public void modificarParametrosUbicacion(String cualParametro, String valorConfirmar) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (cualParametro.equals("UBICACION")) {
            if (valorConfirmar.isEmpty()) {
                vigenciaUbicacionBA.setUbicacion(new UbicacionesGeograficas());
                vigenciaUbicacionBA.getUbicacion().setCiudad(new Ciudades());
                RequestContext.getCurrentInstance().update("form:tabViewUbicacion:parametroUbicacionModUbicacion");
                RequestContext.getCurrentInstance().update("form:tabViewUbicacion:parametroCiudadModUbicacion");
            } else {
                vigenciaUbicacionBA.getUbicacion().setDescripcion(auxUbicacionVigenciaUbicacion);
                for (int i = 0; i < lovUbicacionesGeograficas.size(); i++) {
                    if (lovUbicacionesGeograficas.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    vigenciaUbicacionBA.setUbicacion(lovUbicacionesGeograficas.get(indiceUnicoElemento));
                    lovUbicacionesGeograficas = null;
                    getLovUbicacionesGeograficas();
                    RequestContext.getCurrentInstance().update("form:tabViewUbicacion:parametroUbicacionModUbicacion");
                    RequestContext.getCurrentInstance().update("form:tabViewUbicacion:parametroCiudadModUbicacion");
                } else {
                    permitirIndexVigenciaUbicacion = false;
                    RequestContext.getCurrentInstance().update("form:UbicacionUbicacionDialogo");
                    RequestContext.getCurrentInstance().execute("PF('UbicacionUbicacionDialogo').show()");
                }
            }
        }
    }

    public void modificarParametrosAfiliacion(String cualParametro, String valorConfirmar) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (cualParametro.equals("TERCERO")) {
            if (valorConfirmar.isEmpty()) {
                vigenciaAfiliacionBA.setTercerosucursal(new TercerosSucursales());
                RequestContext.getCurrentInstance().update("form:tabViewAfiliacion:parametroTerceroModAfiliacion");
            } else {
                vigenciaAfiliacionBA.getTercerosucursal().setDescripcion(auxTerceroVigenciaAfiliacion3);
                for (int i = 0; i < lovTercerosSucursales.size(); i++) {
                    if (lovTercerosSucursales.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    vigenciaAfiliacionBA.setTercerosucursal(lovTercerosSucursales.get(indiceUnicoElemento));
                    lovTercerosSucursales = null;
                    getLovTercerosSucursales();
                    RequestContext.getCurrentInstance().update("form:tabViewAfiliacion:parametroTerceroModAfiliacion");
                } else {
                    permitirIndexVigenciaAfiliacion = false;
                    RequestContext.getCurrentInstance().update("form:TerceroAfiliacionDialogo");
                    RequestContext.getCurrentInstance().execute("PF('TerceroAfiliacionDialogo').show()");
                }
            }
        }
        if (cualParametro.equals("TIPOENTIDAD")) {
            if (valorConfirmar.isEmpty()) {
                vigenciaAfiliacionBA.setTipoentidad(new TiposEntidades());
                RequestContext.getCurrentInstance().update("form:tabViewAfiliacion:parametroTipoEntidadModAfiliacion");
            } else {
                vigenciaAfiliacionBA.getTipoentidad().setNombre(auxTipoEntidadVigenciaAfiliacion3);
                for (int i = 0; i < lovTiposEntidades.size(); i++) {
                    if (lovTiposEntidades.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    vigenciaAfiliacionBA.setTipoentidad(lovTiposEntidades.get(indiceUnicoElemento));
                    lovTiposEntidades = null;
                    getLovTiposEntidades();
                    RequestContext.getCurrentInstance().update("form:tabViewAfiliacion:parametroTipoEntidadModAfiliacion");
                } else {
                    permitirIndexVigenciaAfiliacion = false;
                    RequestContext.getCurrentInstance().update("form:TipoEntidadAfiliacionDialogo");
                    RequestContext.getCurrentInstance().execute("PF('TipoEntidadAfiliacionDialogo').show()");
                }
            }
        }
        if (cualParametro.equals("ESTADO")) {
            if (valorConfirmar.isEmpty()) {
                vigenciaAfiliacionBA.setEstadoafiliacion(new EstadosAfiliaciones());
                RequestContext.getCurrentInstance().update("form:tabViewAfiliacion:parametroEstadoModAfiliacion");
            } else {
                vigenciaAfiliacionBA.getEstadoafiliacion().setNombre(auxEstadoVigenciaAfiliacion3);
                for (int i = 0; i < lovEstadosAfiliaciones.size(); i++) {
                    if (lovEstadosAfiliaciones.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    vigenciaAfiliacionBA.setEstadoafiliacion(lovEstadosAfiliaciones.get(indiceUnicoElemento));
                    lovEstadosAfiliaciones = null;
                    getLovEstadosAfiliaciones();
                    RequestContext.getCurrentInstance().update("form:tabViewAfiliacion:parametroEstadoModAfiliacion");
                } else {
                    permitirIndexVigenciaAfiliacion = false;
                    RequestContext.getCurrentInstance().update("form:EstadoAfiliacionDialogo");
                    RequestContext.getCurrentInstance().execute("PF('EstadoAfiliacionDialogo').show()");
                }
            }
        }
    }

    public void modificarParametrosFormaPago(String cualParametro, String valorConfirmar) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (cualParametro.equals("PERIODICIDAD")) {
            if (valorConfirmar.isEmpty()) {
                vigenciaFormaPagoBA.setFormapago(new Periodicidades());
                RequestContext.getCurrentInstance().update("form:tabViewFormaPago:parametroFormaPagoModFormaPago");
            } else {
                vigenciaFormaPagoBA.getFormapago().setNombre(auxPeriodicidadVigenciaFormaPago);
                for (int i = 0; i < lovPeriodicidades.size(); i++) {
                    if (lovPeriodicidades.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    vigenciaFormaPagoBA.setFormapago(lovPeriodicidades.get(indiceUnicoElemento));
                    lovPeriodicidades = null;
                    getLovPeriodicidades();
                    RequestContext.getCurrentInstance().update("form:tabViewFormaPago:parametroFormaPagoModFormaPago");
                } else {
                    permitirIndexVigenciaFormaPago = false;
                    RequestContext.getCurrentInstance().update("form:PeriodicidadFormaPagoDialogo");
                    RequestContext.getCurrentInstance().execute("PF('PeriodicidadFormaPagoDialogo').show()");
                }
            }
        }
        if (cualParametro.equals("SUCURSAL")) {
            if (valorConfirmar.isEmpty()) {
                vigenciaFormaPagoBA.setSucursal(new Sucursales());
                RequestContext.getCurrentInstance().update("form:tabViewFormaPago:parametroSucursalModFormaPago");
            } else {
                vigenciaFormaPagoBA.getSucursal().setNombre(auxSucursalVigenciaFormaPago);
                for (int i = 0; i < lovSucursales.size(); i++) {
                    if (lovSucursales.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    vigenciaFormaPagoBA.setSucursal(lovSucursales.get(indiceUnicoElemento));
                    lovSucursales = null;
                    getLovSucursales();
                    RequestContext.getCurrentInstance().update("form:tabViewFormaPago:parametroSucursalModFormaPago");
                } else {
                    permitirIndexVigenciaFormaPago = false;
                    RequestContext.getCurrentInstance().update("form:SucursalFormaPagoDialogo");
                    RequestContext.getCurrentInstance().execute("PF('SucursalFormaPagoDialogo').show()");
                }
            }
        }
    }

    public void modificarParametrosMvrs(String cualParametro, String valorConfirmar) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (cualParametro.equals("MOTIVO")) {
            if (valorConfirmar.isEmpty()) {
                mvrsBA.setMotivo(new Motivosmvrs());
                RequestContext.getCurrentInstance().update("form:tabViewMvrs:parametroMotivoModMvrs");
            } else {
                mvrsBA.getMotivo().setNombre(auxMotivoMvrs);
                for (int i = 0; i < lovMotivosMvrs.size(); i++) {
                    if (lovMotivosMvrs.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    mvrsBA.setMotivo(lovMotivosMvrs.get(indiceUnicoElemento));
                    lovMotivosMvrs = null;
                    getLovMotivosMvrs();
                    RequestContext.getCurrentInstance().update("form:tabViewMvrs:parametroMotivoModMvrs");
                } else {
                    permitirIndexMvrs = false;
                    RequestContext.getCurrentInstance().update("form:MotivoMvrsDialogo");
                    RequestContext.getCurrentInstance().execute("PF('MotivoMvrsDialogo').show()");
                }
            }
        }
    }

    public void modificarParametrosJornadaLaboral(String cualParametro, String valorConfirmar) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (cualParametro.equals("JORNADA")) {
            if (valorConfirmar.isEmpty()) {
                vigenciaJornadaBA.setJornadatrabajo(new JornadasLaborales());
                RequestContext.getCurrentInstance().update("form:tabViewJornadaLaboral:parametroJornadaModJornadaLaboral");
            } else {
                vigenciaJornadaBA.getJornadatrabajo().setDescripcion(auxJornadaJornadaLaboral);
                for (int i = 0; i < lovJornadasLaborales.size(); i++) {
                    if (lovJornadasLaborales.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    vigenciaJornadaBA.setJornadatrabajo(lovJornadasLaborales.get(indiceUnicoElemento));
                    lovJornadasLaborales = null;
                    getLovJornadasLaborales();
                    RequestContext.getCurrentInstance().update("form:tabViewJornadaLaboral:parametroJornadaModJornadaLaboral");
                } else {
                    permitirIndexVigenciaJornada = false;
                    RequestContext.getCurrentInstance().update("form:JornadaJornadaLaboralDialogo");
                    RequestContext.getCurrentInstance().execute("PF('JornadaJornadaLaboralDialogo').show()");
                }
            }
        }
    }

    public void modificarParametrosFechaRetiro(String cualParametro, String valorConfirmar) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (cualParametro.equals("MOTIVO")) {
            if (valorConfirmar.isEmpty()) {
                motivoRetiroBA = new MotivosRetiros();
                RequestContext.getCurrentInstance().update("form:tabViewFechaRetiro:parametroMotivoModFechaRetiro");
            } else {
                motivoRetiroBA.setNombre(auxMotivoMotivoRetiro);
                for (int i = 0; i < lovMotivosRetiros.size(); i++) {
                    if (lovMotivosRetiros.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    motivoRetiroBA = lovMotivosRetiros.get(indiceUnicoElemento);
                    lovMotivosRetiros = null;
                    getLovMotivosRetiros();
                    RequestContext.getCurrentInstance().update("form:tabViewFechaRetiro:parametroMotivoModFechaRetiro");
                } else {
                    permitirIndexMotivoRetiro = false;
                    RequestContext.getCurrentInstance().update("form:MotivoFechaRetiroDialogo");
                    RequestContext.getCurrentInstance().execute("PF('MotivoFechaRetiroDialogo').show()");
                }
            }
        }
    }

    public void modificarParametrosDatosPersonales(String cualParametro, String valorConfirmar) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (cualParametro.equals("DOCUMENTO")) {
            if (valorConfirmar.isEmpty()) {
                empleadoBA.getPersona().setCiudaddocumento(new Ciudades());
                RequestContext.getCurrentInstance().update("form:tabViewDatosPersonales:parametroCiudadDocumentoModDatosPersonales");
            } else {
                empleadoBA.getPersona().getCiudaddocumento().setNombre(auxCiudadDocumentoEmpleado);
                for (int i = 0; i < lovCiudades.size(); i++) {
                    if (lovCiudades.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    empleadoBA.getPersona().setCiudaddocumento(lovCiudades.get(indiceUnicoElemento));
                    lovCiudades = null;
                    getLovCiudades();
                    RequestContext.getCurrentInstance().update("form:tabViewDatosPersonales:parametroCiudadDocumentoModDatosPersonales");
                } else {
                    permitirIndexEmpleado = false;
                    RequestContext.getCurrentInstance().update("form:CiudadDocumentoDatosPersonalesDialogo");
                    RequestContext.getCurrentInstance().execute("PF('CiudadDocumentoDatosPersonalesDialogo').show()");
                }
            }
        }
        if (cualParametro.equals("NACIMIENTO")) {
            if (valorConfirmar.isEmpty()) {
                empleadoBA.getPersona().setCiudadnacimiento(new Ciudades());
                RequestContext.getCurrentInstance().update("form:tabViewDatosPersonales:parametroCiudadNacimientoModDatosPersonales");
            } else {
                empleadoBA.getPersona().getCiudadnacimiento().setNombre(auxCiudadNacimientoEmpleado);
                for (int i = 0; i < lovCiudades.size(); i++) {
                    if (lovCiudades.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    empleadoBA.getPersona().setCiudadnacimiento(lovCiudades.get(indiceUnicoElemento));
                    lovCiudades = null;
                    getLovCiudades();
                    RequestContext.getCurrentInstance().update("form:tabViewDatosPersonales:parametroCiudadNacimientoModDatosPersonales");
                } else {
                    permitirIndexEmpleado = false;
                    RequestContext.getCurrentInstance().update("form:CiudadNacimientoDatosPersonalesDialogo");
                    RequestContext.getCurrentInstance().execute("PF('CiudadNacimientoDatosPersonalesDialogo').show()");
                }
            }
        }
    }

    public void modificarParametrosEstadoCivil(String cualParametro, String valorConfirmar) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (cualParametro.equals("ESTADO")) {
            if (valorConfirmar.isEmpty()) {
                estadoCivilBA = new EstadosCiviles();
                RequestContext.getCurrentInstance().update("form:tabViewEstadoCivil:parametroEstadoCivilModEstadoCivil");
            } else {
                estadoCivilBA.setDescripcion(auxEstadoCivilEstadoCivil);
                for (int i = 0; i < lovEstadosCiviles.size(); i++) {
                    if (lovEstadosCiviles.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    estadoCivilBA = lovEstadosCiviles.get(indiceUnicoElemento);
                    lovEstadosCiviles = null;
                    getLovEstadosCiviles();
                    RequestContext.getCurrentInstance().update("form:tabViewEstadoCivil:parametroEstadoCivilModEstadoCivil");
                } else {
                    permitirIndexEstadoCivil = false;
                    RequestContext.getCurrentInstance().update("form:EstadoCivilEstadoCivilDialogo");
                    RequestContext.getCurrentInstance().execute("PF('EstadoCivilEstadoCivilDialogo').show()");
                }
            }
        }
    }

    public void modificarParametrosIdioma(String cualParametro, String valorConfirmar) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (cualParametro.equals("IDIOMA")) {
            if (valorConfirmar.isEmpty()) {
                idiomaPersonaBA.setIdioma(new Idiomas());
                RequestContext.getCurrentInstance().update("form:tabViewIdioma:parametroIdiomaModIdioma");
            } else {
                idiomaPersonaBA.getIdioma().setNombre(auxIdiomaIdiomaPersona);
                for (int i = 0; i < lovIdiomas.size(); i++) {
                    if (lovIdiomas.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    idiomaPersonaBA.setIdioma(lovIdiomas.get(indiceUnicoElemento));
                    lovIdiomas = null;
                    getLovIdiomas();
                    RequestContext.getCurrentInstance().update("form:tabViewIdioma:parametroIdiomaModIdioma");
                } else {
                    permitirIndexIdiomaPersona = false;
                    RequestContext.getCurrentInstance().update("form:IdiomaIdiomaDialogo");
                    RequestContext.getCurrentInstance().execute("PF('IdiomaIdiomaDialogo').show()");
                }
            }
        }
    }

    public void modificarParametrosCenso(String cualParametro, String valorConfirmar) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (cualParametro.equals("TIPOINDICADOR")) {
            if (valorConfirmar.isEmpty()) {
                vigenciaIndicadorBA.setTipoindicador(new TiposIndicadores());
                RequestContext.getCurrentInstance().update("form:tabViewCenso:parametroTipoIndicadorModCenso");
            } else {
                vigenciaIndicadorBA.getTipoindicador().setDescripcion(auxTipoIndicadorCenso);
                for (int i = 0; i < lovTiposIndicadores.size(); i++) {
                    if (lovTiposIndicadores.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    vigenciaIndicadorBA.setTipoindicador(lovTiposIndicadores.get(indiceUnicoElemento));
                    lovTiposIndicadores = null;
                    getLovTiposIndicadores();
                    RequestContext.getCurrentInstance().update("form:tabViewCenso:parametroTipoIndicadorModCenso");
                } else {
                    permitirIndexVigenciaIndicador = false;
                    RequestContext.getCurrentInstance().update("form:TipoIndicadorCensoDialogo");
                    RequestContext.getCurrentInstance().execute("PF('TipoIndicadorCensoDialogo').show()");
                }
            }
        }
        if (cualParametro.equals("INDICADOR")) {
            if (valorConfirmar.isEmpty()) {
                vigenciaIndicadorBA.setIndicador(new Indicadores());
                RequestContext.getCurrentInstance().update("form:tabViewCenso:parametroIndicadorModCenso");
            } else {
                vigenciaIndicadorBA.getIndicador().setDescripcion(auxIndicadorCenso);
                for (int i = 0; i < lovIndicadores.size(); i++) {
                    if (lovIndicadores.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    vigenciaIndicadorBA.setIndicador(lovIndicadores.get(indiceUnicoElemento));
                    lovIndicadores = null;
                    getLovIndicadores();
                    RequestContext.getCurrentInstance().update("form:tabViewCenso:parametroIndicadorModCenso");
                } else {
                    permitirIndexVigenciaIndicador = false;
                    RequestContext.getCurrentInstance().update("form:IndicadorCensoDialogo");
                    RequestContext.getCurrentInstance().execute("PF('IndicadorCensoDialogo').show()");
                }
            }
        }
    }

    public void modificarParametrosEducacionFormal(String cualParametro, String valorConfirmar) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (cualParametro.equals("PROFESION")) {
            if (valorConfirmar.isEmpty()) {
                vigenciaFormalBA.setProfesion(new Profesiones());
                RequestContext.getCurrentInstance().update("form:tabViewEducacionFormal:parametroProfesionModEducacionFormal");
            } else {
                vigenciaFormalBA.getProfesion().setDescripcion(auxProfesionEducacionFormal);
                for (int i = 0; i < lovProfesiones.size(); i++) {
                    if (lovProfesiones.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    vigenciaFormalBA.setProfesion(lovProfesiones.get(indiceUnicoElemento));
                    lovProfesiones = null;
                    getLovProfesiones();
                    RequestContext.getCurrentInstance().update("form:tabViewEducacionFormal:parametroProfesionModEducacionFormal");
                } else {
                    permitirIndexVigenciaFormal = false;
                    RequestContext.getCurrentInstance().update("form:ProfesionEducacionFormalDialogo");
                    RequestContext.getCurrentInstance().execute("PF('ProfesionEducacionFormalDialogo').show()");
                }
            }
        }
        if (cualParametro.equals("INSTITUCION")) {
            if (valorConfirmar.isEmpty()) {
                vigenciaFormalBA.setInstitucion(new Instituciones());
                RequestContext.getCurrentInstance().update("form:tabViewEducacionFormal:parametroInstitucionModEducacionFormal");
            } else {
                vigenciaFormalBA.getInstitucion().setDescripcion(auxInstitucionEducacionFormal);
                for (int i = 0; i < lovInstituciones.size(); i++) {
                    if (lovInstituciones.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    vigenciaFormalBA.setInstitucion(lovInstituciones.get(indiceUnicoElemento));
                    lovInstituciones = null;
                    getLovInstituciones();
                    RequestContext.getCurrentInstance().update("form:tabViewEducacionFormal:parametroInstitucionModEducacionFormal");
                } else {
                    permitirIndexVigenciaFormal = false;
                    RequestContext.getCurrentInstance().update("form:InstitucionEducacionFormalDialogo");
                    RequestContext.getCurrentInstance().execute("PF('InstitucionEducacionFormalDialogo').show()");
                }
            }
        }
    }

    public void modificarParametrosEducacionNoFormal(String cualParametro, String valorConfirmar) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (cualParametro.equals("CURSO")) {
            if (valorConfirmar.isEmpty()) {
                vigenciaNoFormalBA.setCurso(new Cursos());
                RequestContext.getCurrentInstance().update("form:tabViewEducacionNoFormal:parametroCursoModEducacionNoFormal");
            } else {
                vigenciaNoFormalBA.getCurso().setNombre(auxCursoEducacionNoFormal);
                for (int i = 0; i < lovCursos.size(); i++) {
                    if (lovCursos.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    vigenciaNoFormalBA.setCurso(lovCursos.get(indiceUnicoElemento));
                    lovCursos = null;
                    getLovCursos();
                    RequestContext.getCurrentInstance().update("form:tabViewEducacionNoFormal:parametroCursoModEducacionNoFormal");
                } else {
                    permitirIndexVigenciaNoFormal = false;
                    RequestContext.getCurrentInstance().update("form:CursoEducacionNoFormalDialogo");
                    RequestContext.getCurrentInstance().execute("PF('CursoEducacionNoFormalDialogo').show()");
                }
            }
        }
        if (cualParametro.equals("INSTITUCION")) {
            if (valorConfirmar.isEmpty()) {
                vigenciaNoFormalBA.setInstitucion(new Instituciones());
                RequestContext.getCurrentInstance().update("form:tabViewEducacionNoFormal:parametroInstitucionModEducacionNoFormal");
            } else {
                vigenciaNoFormalBA.getInstitucion().setDescripcion(auxInstitucionEducacionNoFormal);
                for (int i = 0; i < lovInstituciones.size(); i++) {
                    if (lovInstituciones.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    vigenciaNoFormalBA.setInstitucion(lovInstituciones.get(indiceUnicoElemento));
                    lovInstituciones = null;
                    getLovInstituciones();
                    RequestContext.getCurrentInstance().update("form:tabViewEducacionNoFormal:parametroInstitucionModEducacionNoFormal");
                } else {
                    permitirIndexVigenciaNoFormal = false;
                    RequestContext.getCurrentInstance().update("form:InstitucionEducacionNoFormalDialogo");
                    RequestContext.getCurrentInstance().execute("PF('InstitucionEducacionNoFormalDialogo').show()");
                }
            }
        }
    }

    public void modificarParametrosExperienciaLaboral(String cualParametro, String valorConfirmar) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (cualParametro.equals("MOTIVO")) {
            if (valorConfirmar.isEmpty()) {
                hvExperienciaLaboralBA.setMotivoretiro(new MotivosRetiros());
                RequestContext.getCurrentInstance().update("form:tabViewExperienciaLaboral:parametroMotivoRetiroModExperienciaLaboral");
            } else {
                hvExperienciaLaboralBA.getMotivoretiro().setNombre(auxMotivoRetiroExperienciaLaboral);
                for (int i = 0; i < lovMotivosRetiros.size(); i++) {
                    if (lovMotivosRetiros.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    hvExperienciaLaboralBA.setMotivoretiro(lovMotivosRetiros.get(indiceUnicoElemento));
                    lovMotivosRetiros = null;
                    getLovMotivosRetiros();
                    RequestContext.getCurrentInstance().update("form:tabViewExperienciaLaboral:parametroMotivoRetiroModExperienciaLaboral");
                } else {
                    permitirIndexHvExperienciaLaboral = false;
                    RequestContext.getCurrentInstance().update("form:MotivoRetiroExperienciaLaboralDialogo");
                    RequestContext.getCurrentInstance().execute("PF('MotivoRetiroExperienciaLaboralDialogo').show()");
                }
            }
        }
        if (cualParametro.equals("SECTOR")) {
            if (valorConfirmar.isEmpty()) {
                hvExperienciaLaboralBA.setSectoreconomico(new SectoresEconomicos());
                RequestContext.getCurrentInstance().update("form:tabViewExperienciaLaboral:parametroSectorEconomicoModExperienciaLaboral");
            } else {
                hvExperienciaLaboralBA.getSectoreconomico().setDescripcion(auxSectorEconomicoExperienciaLaboral);
                for (int i = 0; i < lovSectoresEconomicos.size(); i++) {
                    if (lovSectoresEconomicos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    hvExperienciaLaboralBA.setSectoreconomico(lovSectoresEconomicos.get(indiceUnicoElemento));
                    lovSectoresEconomicos = null;
                    getLovSectoresEconomicos();
                    RequestContext.getCurrentInstance().update("form:tabViewExperienciaLaboral:parametroSectorEconomicoModExperienciaLaboral");
                } else {
                    permitirIndexHvExperienciaLaboral = false;
                    RequestContext.getCurrentInstance().update("form:SectorEconomicoExperienciaLaboralDialogo");
                    RequestContext.getCurrentInstance().execute("PF('SectorEconomicoExperienciaLaboralDialogo').show()");
                }
            }
        }
    }

    public void modificarParametrosProyecto(String cualParametro, String valorConfirmar) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (cualParametro.equals("PROYECTO")) {
            if (valorConfirmar.isEmpty()) {
                vigenciaProyectoBA.setProyecto(new Proyectos());
                RequestContext.getCurrentInstance().update("form:tabViewProyecto:parametroProyectoModProyecto");
            } else {
                vigenciaProyectoBA.getProyecto().setNombreproyecto(auxProyectoProyecto);
                for (int i = 0; i < lovProyectos.size(); i++) {
                    if (lovProyectos.get(i).getNombreproyecto().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    vigenciaProyectoBA.setProyecto(lovProyectos.get(indiceUnicoElemento));
                    lovProyectos = null;
                    getLovProyectos();
                    RequestContext.getCurrentInstance().update("form:tabViewProyecto:parametroProyectoModProyecto");
                } else {
                    permitirIndexVigenciaProyecto = false;
                    RequestContext.getCurrentInstance().update("form:ProyectoProyectoDialogo");
                    RequestContext.getCurrentInstance().execute("PF('ProyectoProyectoDialogo').show()");
                }
            }
        }
        if (cualParametro.equals("ROL")) {
            if (valorConfirmar.isEmpty()) {
                vigenciaProyectoBA.setPryRol(new PryRoles());
                RequestContext.getCurrentInstance().update("form:tabViewProyecto:parametroRolModProyecto");
            } else {
                vigenciaProyectoBA.getPryRol().setDescripcion(auxRolProyecto);
                for (int i = 0; i < lovPryRoles.size(); i++) {
                    if (lovPryRoles.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    vigenciaProyectoBA.setPryRol(lovPryRoles.get(indiceUnicoElemento));
                    lovPryRoles = null;
                    getLovPryRoles();
                    RequestContext.getCurrentInstance().update("form:tabViewProyecto:parametroRolModProyecto");
                } else {
                    permitirIndexVigenciaProyecto = false;
                    RequestContext.getCurrentInstance().update("form:PryRolProyectoDialogo");
                    RequestContext.getCurrentInstance().execute("PF('PryRolProyectoDialogo').show()");
                }
            }
        }
    }

    public void modificarParametrosCargoPostularse(String cualParametro, String valorConfirmar) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (cualParametro.equals("CARGO")) {
            if (valorConfirmar.isEmpty()) {
                vigenciaCargoPersonalBA.setCargo(new Cargos());
                RequestContext.getCurrentInstance().update("form:tabViewCargoPostularse:parametroCargoModCargoPostularse");
            } else {
                vigenciaCargoPersonalBA.getCargo().setNombre(auxCargoCargoPostularse);
                for (int i = 0; i < lovCargos.size(); i++) {
                    if (lovCargos.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    vigenciaCargoPersonalBA.setCargo(lovCargos.get(indiceUnicoElemento));
                    lovCargos = null;
                    getLovCargos();
                    RequestContext.getCurrentInstance().update("form:tabViewCargoPostularse:parametroCargoModCargoPostularse");
                } else {
                    permitirIndexVigenciaCargoPersonal = false;
                    RequestContext.getCurrentInstance().update("form:CargoCargoPostularseDialogo");
                    RequestContext.getCurrentInstance().execute("PF('CargoCargoPostularseDialogo').show()");
                }
            }
        }
    }

    public void modificarConversacionModuloIdioma() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (conversacionDesde != null && conversacionHasta != null) {
            if (conversacionHasta.intValue() < conversacionDesde.intValue()) {
                conversacionDesde = auxConversacionDesde;
                conversacionHasta = auxConversacionHasta;
                RequestContext.getCurrentInstance().update("form:tabViewIdioma:parametroConversacionDesdeModIdioma");
                RequestContext.getCurrentInstance().update("form:tabViewIdioma:parametroConversacionHastaModIdioma");
                RequestContext.getCurrentInstance().execute("PF('errorIdiomaPorcentajes').show()");
            }
        }
    }

    public void modificarEscrituraModuloIdioma() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (escrituraDesde != null && escrituraHasta != null) {
            if (escrituraHasta.intValue() < escrituraDesde.intValue()) {
                escrituraDesde = auxEscrituraDesde;
                escrituraHasta = auxEscrituraHasta;
                RequestContext.getCurrentInstance().update("form:tabViewIdioma:parametroEscrituraDesdeModIdioma");
                RequestContext.getCurrentInstance().update("form:tabViewIdioma:parametroEscrituraHastaModIdioma");
                RequestContext.getCurrentInstance().execute("PF('errorIdiomaPorcentajes').show()");
            }
        }
    }

    public void modificarLecturaModuloIdioma() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (lecturaDesde != null && lecturaHasta != null) {
            if (lecturaHasta.intValue() < lecturaDesde.intValue()) {
                lecturaDesde = auxLecturaDesde;
                lecturaHasta = auxLecturaHasta;
                RequestContext.getCurrentInstance().update("form:tabViewIdioma:parametroLecturaDesdeModIdioma");
                RequestContext.getCurrentInstance().update("form:tabViewIdioma:parametroLecturaHastaModIdioma");
                RequestContext.getCurrentInstance().execute("PF('errorIdiomaPorcentajes').show()");
            }
        }
    }

    public void modificarSueldosModuloSueldo() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (sueldoMaxSueldo != null && sueldoMinSueldo != null) {
            if (sueldoMaxSueldo.intValue() < sueldoMinSueldo.intValue()) {
                sueldoMaxSueldo = auxSueldoMaximoSueldo;
                sueldoMinSueldo = auxSueldoMinimoSueldo;
                RequestContext.getCurrentInstance().update("form:tabViewSueldo:parametroSueldoMinimoModSueldo");
                RequestContext.getCurrentInstance().update("form:tabViewSueldo:parametroSueldoMaximoModSueldo");
                RequestContext.getCurrentInstance().execute("PF('errorSueldos').show()");
            }
        }
    }

    public void modificarSueldosModuloMvrs() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (sueldoMaxMvrs != null && sueldoMinMvrs != null) {
            if (sueldoMaxMvrs.intValue() < sueldoMinMvrs.intValue()) {
                sueldoMaxMvrs = auxSueldoMaximoMvrs;
                sueldoMinMvrs = auxSueldoMinimoMvrs;
                RequestContext.getCurrentInstance().update("form:tabViewMvrs:parametroSueldoMinimoModMvrs");
                RequestContext.getCurrentInstance().update("form:tabViewMvrs:parametroSueldoMaximoModMvrs");
                RequestContext.getCurrentInstance().execute("PF('errorSueldos').show()");
            }
        }
    }

    public void modificarPromediosModuloSets() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (promedioMaximoSets != null && promedioMinimoSets != null) {
            if (promedioMaximoSets.intValue() < promedioMinimoSets.intValue()) {
                promedioMaximoSets = auxPromedioMaximoSets;
                promedioMinimoSets = auxPromedioMinimoSets;
                RequestContext.getCurrentInstance().update("form:tabViewSets:parametroPromedioMaxModSets");
                RequestContext.getCurrentInstance().update("form:tabViewSets:parametroPromedioMinModSets");
                RequestContext.getCurrentInstance().execute("PF('errorPromedios').show()");
            }
        }
    }

    public void actualizarParametroCargoCargoPostularse() {
        vigenciaCargoPersonalBA.setCargo(cargoSeleccionado);
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:tabViewCargoPostularse:parametroCargoModCargoPostularse");
        cargoSeleccionado = null;
        filtrarLovCargos = null;
        casillaVigenciaCargoPersonal = -1;
        aceptar = true;
        permitirIndexVigenciaCargoPersonal = true;
        /*
         * RequestContext.getCurrentInstance().update("form:CargoCargoPostularseDialogo");
         * RequestContext.getCurrentInstance().update("form:lovCargoCargoPostularse");
         RequestContext.getCurrentInstance().update("form:aceptarCCP");
         */

        context.reset("form:lovCargoCargoPostularse:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovCargoCargoPostularse').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('CargoCargoPostularseDialogo').hide()");
    }

    public void cancelarParametroCargoCargoPostularse() {
        cargoSeleccionado = null;
        filtrarLovCargos = null;
        casillaVigenciaCargoPersonal = -1;
        aceptar = true;
        permitirIndexVigenciaCargoPersonal = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovCargoCargoPostularse:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovCargoCargoPostularse').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('CargoCargoPostularseDialogo').hide()");
    }

    public void actualizarParametroProyectoProyecto() {
        vigenciaProyectoBA.setProyecto(proyectoSeleccionado);
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:tabViewProyecto:parametroProyectoModProyecto");
        proyectoSeleccionado = null;
        filtrarLovProyectos = null;
        casillaVigenciaProyecto = -1;
        aceptar = true;
        permitirIndexVigenciaProyecto = true;
        /*
         * RequestContext.getCurrentInstance().update("form:ProyectoProyectoDialogo");
         * RequestContext.getCurrentInstance().update("form:lovProyectoProyecto");
         RequestContext.getCurrentInstance().update("form:aceptarPP");
         */
        context.reset("form:lovProyectoProyecto:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovProyectoProyecto').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('ProyectoProyectoDialogo').hide()");
    }

    public void cancelarParametroProyectoProyecto() {
        proyectoSeleccionado = null;
        filtrarLovProyectos = null;
        casillaVigenciaProyecto = -1;
        aceptar = true;
        permitirIndexVigenciaProyecto = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovProyectoProyecto:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovProyectoProyecto').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('ProyectoProyectoDialogo').hide()");
    }

    public void actualizarParametroPryRolProyecto() {
        vigenciaProyectoBA.setPryRol(pryRolSeleccionado);
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:tabViewProyecto:parametroRolModProyecto");
        pryRolSeleccionado = null;
        filtrarLovPryRoles = null;
        casillaVigenciaProyecto = -1;
        aceptar = true;
        permitirIndexVigenciaProyecto = true;/*
         * RequestContext.getCurrentInstance().update("form:PryRolProyectoDialogo");
         * RequestContext.getCurrentInstance().update("form:lovPryRolProyecto");
         RequestContext.getCurrentInstance().update("form:aceptarPRYP");
         */

        context.reset("form:lovPryRolProyecto:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovPryRolProyecto').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('PryRolProyectoDialogo').hide()");
    }

    public void cancelarParametroPryRolProyecto() {
        pryRolSeleccionado = null;
        filtrarLovPryRoles = null;
        casillaVigenciaProyecto = -1;
        aceptar = true;
        permitirIndexVigenciaProyecto = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovPryRolProyecto:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovPryRolProyecto').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('PryRolProyectoDialogo').hide()");
    }

    public void actualizarParametroSectorEconomicoExperienciaLaboral() {
        hvExperienciaLaboralBA.setSectoreconomico(sectorEconomicoSeleccionado);
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:tabViewExperienciaLaboral:parametroSectorEconomicoModExperienciaLaboral");
        sectorEconomicoSeleccionado = null;
        filtrarLovSectoresEconomicos = null;
        casillaHvExperienciaLaboral = -1;
        aceptar = true;
        permitirIndexHvExperienciaLaboral = true;/*
         * RequestContext.getCurrentInstance().update("form:SectorEconomicoExperienciaLaboralDialogo");
         * RequestContext.getCurrentInstance().update("form:lovSectorEconomicoExperienciaLaboral");
         RequestContext.getCurrentInstance().update("form:aceptarSEEL");
         */

        context.reset("form:lovSectorEconomicoExperienciaLaboral:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovSectorEconomicoExperienciaLaboral').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('SectorEconomicoExperienciaLaboralDialogo').hide()");
    }

    public void cancelarParametroSectorEconomicoExperienciaLaboral() {
        sectorEconomicoSeleccionado = null;
        filtrarLovSectoresEconomicos = null;
        casillaHvExperienciaLaboral = -1;
        aceptar = true;
        permitirIndexHvExperienciaLaboral = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovSectorEconomicoExperienciaLaboral:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovSectorEconomicoExperienciaLaboral').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('SectorEconomicoExperienciaLaboralDialogo').hide()");
    }

    public void actualizarParametroMotivoRetiroExperienciaLaboral() {
        hvExperienciaLaboralBA.setMotivoretiro(motivoRetiroSeleccionado);
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:tabViewExperienciaLaboral:parametroMotivoRetiroModExperienciaLaboral");
        motivoRetiroSeleccionado = null;
        filtrarLovMotivosRetiros = null;
        casillaHvExperienciaLaboral = -1;
        aceptar = true;
        permitirIndexHvExperienciaLaboral = true;/*
         * RequestContext.getCurrentInstance().update("form:MotivoRetiroExperienciaLaboralDialogo");
         * RequestContext.getCurrentInstance().update("form:lovMotivoRetiroExperienciaLaboral");
         RequestContext.getCurrentInstance().update("form:aceptarMREL");
         */

        context.reset("form:lovMotivoRetiroExperienciaLaboral:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovMotivoRetiroExperienciaLaboral').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('MotivoRetiroExperienciaLaboralDialogo').hide()");
    }

    public void cancelarParametroMotivoRetiroExperienciaLaboral() {
        motivoRetiroSeleccionado = null;
        filtrarLovMotivosRetiros = null;
        casillaHvExperienciaLaboral = -1;
        aceptar = true;
        permitirIndexHvExperienciaLaboral = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovMotivoRetiroExperienciaLaboral:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovMotivoRetiroExperienciaLaboral').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('MotivoRetiroExperienciaLaboralDialogo').hide()");
    }

    public void actualizarParametroInstitucionEducacionNoFormal() {
        vigenciaNoFormalBA.setInstitucion(institucionSeleccionada);
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:tabViewEducacionNoFormal:parametroInstitucionModEducacionNoFormal");
        institucionSeleccionada = null;
        filtrarLovInstituciones = null;
        casillaVigenciaNoFormal = -1;
        aceptar = true;
        permitirIndexVigenciaNoFormal = true;/*
         * RequestContext.getCurrentInstance().update("form:InstitucionEducacionNoFormalDialogo");
         * RequestContext.getCurrentInstance().update("form:lovInstitucionEducacionNoFormal");
         RequestContext.getCurrentInstance().update("form:aceptarIENF");
         */

        context.reset("form:lovInstitucionEducacionNoFormal:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovInstitucionEducacionNoFormal').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('InstitucionEducacionNoFormalDialogo').hide()");
    }

    public void cancelarParametroInstitucionEducacionNoFormal() {
        institucionSeleccionada = null;
        filtrarLovInstituciones = null;
        casillaVigenciaNoFormal = -1;
        aceptar = true;
        permitirIndexVigenciaNoFormal = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovInstitucionEducacionNoFormal:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovInstitucionEducacionNoFormal').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('InstitucionEducacionNoFormalDialogo').hide()");
    }

    public void actualizarParametroCursoEducacionNoFormal() {
        vigenciaNoFormalBA.setCurso(cursoSeleccionado);
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:tabViewEducacionNoFormal:parametroCursoModEducacionNoFormal");
        cursoSeleccionado = null;
        filtrarLovCursos = null;
        casillaVigenciaNoFormal = -1;
        aceptar = true;
        permitirIndexVigenciaNoFormal = true;/*
         * RequestContext.getCurrentInstance().update("form:CursoEducacionNoFormalDialogo");
         * RequestContext.getCurrentInstance().update("form:lovCursoEducacionNoFormal");
         RequestContext.getCurrentInstance().update("form:aceptarPENF");
         */

        context.reset("form:lovCursoEducacionNoFormal:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovCursoEducacionNoFormal').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('CursoEducacionNoFormalDialogo').hide()");
    }

    public void cancelarParametroCursoEducacionNoFormal() {
        cursoSeleccionado = null;
        filtrarLovCursos = null;
        casillaVigenciaNoFormal = -1;
        aceptar = true;
        permitirIndexVigenciaNoFormal = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovCursoEducacionNoFormal:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovCursoEducacionNoFormal').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('CursoEducacionNoFormalDialogo').hide()");
    }

    public void actualizarParametroProfesionEducacionFormal() {
        vigenciaFormalBA.setProfesion(profesionSeleccionada);
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:tabViewEducacionFormal:parametroProfesionModEducacionFormal");
        profesionSeleccionada = null;
        filtrarLovProfesiones = null;
        casillaVigenciaFormal = -1;
        aceptar = true;
        permitirIndexVigenciaFormal = true;/*
         * RequestContext.getCurrentInstance().update("form:ProfesionEducacionFormalDialogo");
         * RequestContext.getCurrentInstance().update("form:lovProfesionEducacionFormal");
         RequestContext.getCurrentInstance().update("form:aceptarPEF");
         */

        context.reset("form:lovProfesionEducacionFormal:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovProfesionEducacionFormal').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('ProfesionEducacionFormalDialogo').hide()");
    }

    public void cancelarParametroProfesionEducacionFormal() {
        profesionSeleccionada = null;
        filtrarLovProfesiones = null;
        casillaVigenciaFormal = -1;
        aceptar = true;
        permitirIndexVigenciaFormal = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovProfesionEducacionFormal:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovProfesionEducacionFormal').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('ProfesionEducacionFormalDialogo').hide()");
    }

    public void actualizarParametroInstitucionEducacionFormal() {
        vigenciaFormalBA.setInstitucion(institucionSeleccionada);
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:tabViewEducacionFormal:parametroInstitucionModEducacionFormal");
        institucionSeleccionada = null;
        filtrarLovInstituciones = null;
        casillaVigenciaFormal = -1;
        aceptar = true;
        permitirIndexVigenciaFormal = true;/*
         * RequestContext.getCurrentInstance().update("form:InstitucionEducacionFormalDialogo");
         * RequestContext.getCurrentInstance().update("form:lovInstitucionEducacionFormal");
         RequestContext.getCurrentInstance().update("form:aceptarIEF");
         */

        context.reset("form:lovInstitucionEducacionFormal:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovInstitucionEducacionFormal').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('InstitucionEducacionFormalDialogo').hide()");
    }

    public void cancelarParametroInstitucionEducacionFormal() {
        institucionSeleccionada = null;
        filtrarLovInstituciones = null;
        casillaVigenciaFormal = -1;
        aceptar = true;
        permitirIndexVigenciaFormal = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovInstitucionEducacionFormal:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovInstitucionEducacionFormal').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('InstitucionEducacionFormalDialogo').hide()");
    }

    public void actualizarParametroTipoIndicadorCenso() {
        vigenciaIndicadorBA.setTipoindicador(tipoIndicadorSeleccionado);
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:tabViewCenso:parametroTipoIndicadorModCenso");
        tipoIndicadorSeleccionado = null;
        filtrarLovTiposIndicadores = null;
        casillaVigenciaIndicador = -1;
        aceptar = true;
        permitirIndexVigenciaIndicador = true;/*
         * RequestContext.getCurrentInstance().update("form:TipoIndicadorCensoDialogo");
         * RequestContext.getCurrentInstance().update("form:lovTipoIndicadorCensol");
         RequestContext.getCurrentInstance().update("form:aceptarTIC");
         */

        context.reset("form:lovTipoIndicadorCenso:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovTipoIndicadorCenso').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('TipoIndicadorCensoDialogo').hide()");
    }

    public void cancelarParametroTipoIndicadorCenso() {
        tipoIndicadorSeleccionado = null;
        filtrarLovTiposIndicadores = null;
        casillaVigenciaIndicador = -1;
        aceptar = true;
        permitirIndexVigenciaIndicador = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovTipoIndicadorCenso:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovTipoIndicadorCenso').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('TipoIndicadorCensoDialogo').hide()");
    }

    public void actualizarParametroIndicadorCenso() {
        vigenciaIndicadorBA.setIndicador(indicadorSeleccionado);
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:tabViewCenso:parametroIndicadorModCenso");
        indicadorSeleccionado = null;
        filtrarLovIndicadores = null;
        casillaVigenciaIndicador = -1;
        aceptar = true;
        permitirIndexVigenciaIndicador = true;/*
         * RequestContext.getCurrentInstance().update("form:IndicadorCensoDialogo");
         * RequestContext.getCurrentInstance().update("form:lovIndicadorCensol");
         RequestContext.getCurrentInstance().update("form:aceptarIC");
         */

        context.reset("form:lovIndicadorCenso:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovIndicadorCenso').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('IndicadorCensoDialogo').hide()");
    }

    public void cancelarParametroIndicadorCenso() {
        indicadorSeleccionado = null;
        filtrarLovIndicadores = null;
        casillaVigenciaIndicador = -1;
        aceptar = true;
        permitirIndexVigenciaIndicador = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovIndicadorCenso:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovIndicadorCenso').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('IndicadorCensoDialogo').hide()");
    }

    public void actualizarParametroIdiomaIdioma() {
        idiomaPersonaBA.setIdioma(idiomaSeleccionado);
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:tabViewIdioma:parametroIdiomaModIdioma");
        idiomaSeleccionado = null;
        filtrarLovIdiomas = null;
        casillaIdiomaPersona = -1;
        aceptar = true;
        permitirIndexIdiomaPersona = true;/*
         * RequestContext.getCurrentInstance().update("form:IdiomaIdiomaDialogo");
         * RequestContext.getCurrentInstance().update("form:lovIdiomaIdiomal");
         RequestContext.getCurrentInstance().update("form:aceptarII");
         */

        context.reset("form:lovIdiomaIdioma:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovIdiomaIdioma').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('IdiomaIdiomaDialogo').hide()");
    }

    public void cancelarParametroIdiomaIdioma() {
        idiomaSeleccionado = null;
        filtrarLovIdiomas = null;
        casillaIdiomaPersona = -1;
        aceptar = true;
        permitirIndexIdiomaPersona = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovIdiomaIdioma:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovIdiomaIdioma').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('IdiomaIdiomaDialogo').hide()");
    }

    public void actualizarParametroEstadoCivilEstadoCivil() {
        estadoCivilBA = estadoCivilSeleccionado;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:tabViewEstadoCivil:parametroEstadoCivilModEstadoCivil");
        estadoCivilSeleccionado = null;
        filtrarLovEstadosCiviles = null;
        casillaEstadoCivil = -1;
        aceptar = true;
        permitirIndexEstadoCivil = true;/*
         * RequestContext.getCurrentInstance().update("form:EstadoCivilEstadoCivilDialogo");
         * RequestContext.getCurrentInstance().update("form:lovEstadoCivilEstadoCivil");
         RequestContext.getCurrentInstance().update("form:aceptarECEC");
         */

        context.reset("form:lovEstadoCivilEstadoCivil:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovEstadoCivilEstadoCivil').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('EstadoCivilEstadoCivilDialogo').hide()");
    }

    public void cancelarParametroEstadoCivilEstadoCivil() {
        estadoCivilSeleccionado = null;
        filtrarLovEstadosCiviles = null;
        casillaEstadoCivil = -1;
        aceptar = true;
        permitirIndexEstadoCivil = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovEstadoCivilEstadoCivil:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovEstadoCivilEstadoCivil').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('EstadoCivilEstadoCivilDialogo').hide()");
    }

    public void actualizarParametroCiudadDocumentoDatosPersonales() {
        empleadoBA.getPersona().setCiudaddocumento(ciudadSeleccionada);
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:tabViewDatosPersonales:parametroCiudadDocumentoModDatosPersonales");
        ciudadSeleccionada = null;
        filtrarLovCiudades = null;
        casillaEmpleado = -1;
        aceptar = true;
        permitirIndexEmpleado = true;/*
         * RequestContext.getCurrentInstance().update("form:CiudadDocumentoDatosPersonalesDialogo");
         * RequestContext.getCurrentInstance().update("form:lovCiudadDocumentoDatosPersonales");
         RequestContext.getCurrentInstance().update("form:aceptarCDDP");
         */

        context.reset("form:lovCiudadDocumentoDatosPersonales:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovCiudadDocumentoDatosPersonales').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('CiudadDocumentoDatosPersonalesDialogo').hide()");
    }

    public void cancelarParametroCiudadDocumentoDatosPersonales() {
        ciudadSeleccionada = null;
        filtrarLovCiudades = null;
        casillaEmpleado = -1;
        aceptar = true;
        permitirIndexEmpleado = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovCiudadDocumentoDatosPersonales:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovCiudadDocumentoDatosPersonales').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('CiudadDocumentoDatosPersonalesDialogo').hide()");
    }

    public void actualizarParametroCiudadNacimientoDatosPersonales() {
        empleadoBA.getPersona().setCiudadnacimiento(ciudadSeleccionada);
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:tabViewDatosPersonales:parametroCiudadNacimientoModDatosPersonales");
        ciudadSeleccionada = null;
        filtrarLovCiudades = null;
        casillaEmpleado = -1;
        aceptar = true;
        permitirIndexEmpleado = true;/*
         * RequestContext.getCurrentInstance().update("form:CiudadNacimientoDatosPersonalesDialogo");
         * RequestContext.getCurrentInstance().update("form:lovCiudadNacimientoDatosPersonales");
         RequestContext.getCurrentInstance().update("form:aceptarCNDP");
         */

        context.reset("form:lovCiudadNacimientoDatosPersonales:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovCiudadNacimientoDatosPersonales').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('CiudadNacimientoDatosPersonalesDialogo').hide()");
    }

    public void cancelarParametroCiudadNacimientoDatosPersonales() {
        ciudadSeleccionada = null;
        filtrarLovCiudades = null;
        casillaEmpleado = -1;
        aceptar = true;
        permitirIndexEmpleado = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovCiudadNacimientoDatosPersonales:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovCiudadNacimientoDatosPersonales').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('CiudadNacimientoDatosPersonalesDialogo').hide()");
    }

    public void actualizarParametroMotivoFechaRetiro() {
        motivoRetiroBA = motivoRetiroSeleccionado;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:tabViewFechaRetiro:parametroMotivoModFechaRetiro");
        motivoRetiroSeleccionado = null;
        filtrarLovMotivosRetiros = null;
        casillaMotivoRetiro = -1;
        aceptar = true;
        permitirIndexMotivoRetiro = true;/*
         * RequestContext.getCurrentInstance().update("form:MotivoFechaRetiroDialogo");
         * RequestContext.getCurrentInstance().update("form:lovMotivoFechaRetiro");
         RequestContext.getCurrentInstance().update("form:aceptarMRFR");
         */

        context.reset("form:lovMotivoFechaRetiro:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovMotivoFechaRetiro').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('MotivoFechaRetiroDialogo').hide()");
    }

    public void cancelarParametroMotivoFechaRetiro() {
        motivoRetiroSeleccionado = null;
        filtrarLovMotivosRetiros = null;
        casillaMotivoRetiro = -1;
        aceptar = true;
        permitirIndexMotivoRetiro = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovMotivoFechaRetiro:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovMotivoFechaRetiro').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('MotivoFechaRetiroDialogo').hide()");
    }

    public void actualizarParametroJornadaJornadaLaboral() {
        vigenciaJornadaBA.setJornadatrabajo(jornadaLaboralSeleccionada);
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:tabViewJornadaLaboral:parametroJornadaModJornadaLaboral");
        jornadaLaboralSeleccionada = null;
        filtrarLovJornadasLaborales = null;
        casillaVigenciaJornadaLaboral = -1;
        aceptar = true;
        permitirIndexVigenciaJornada = true;/*
         * RequestContext.getCurrentInstance().update("form:JornadaJornadaLaboralDialogo");
         * RequestContext.getCurrentInstance().update("form:lovJornadaJornadaLaboral");
         RequestContext.getCurrentInstance().update("form:aceptarJLJL");
         */

        context.reset("form:lovJornadaJornadaLaboral:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovJornadaJornadaLaboral').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('JornadaJornadaLaboralDialogo').hide()");
    }

    public void cancelarParametroJornadaJornadaLaboral() {
        jornadaLaboralSeleccionada = null;
        filtrarLovJornadasLaborales = null;
        casillaVigenciaJornadaLaboral = -1;
        aceptar = true;
        permitirIndexVigenciaJornada = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovJornadaJornadaLaboral:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovJornadaJornadaLaboral').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('JornadaJornadaLaboralDialogo').hide()");
    }

    public void actualizarParametroMotivoMvrs() {
        mvrsBA.setMotivo(motivoMvrsSeleccionado);
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:tabViewMvrs:parametroMotivoModMvrs");
        motivoMvrsSeleccionado = null;
        filtrarLovMotivosMvrs = null;
        casillaMvrs = -1;
        aceptar = true;
        permitirIndexMvrs = true;/*
         * RequestContext.getCurrentInstance().update("form:MotivoMvrsDialogo");
         * RequestContext.getCurrentInstance().update("form:lovMotivoMvrs");
         RequestContext.getCurrentInstance().update("form:aceptarMMVR");
         */

        context.reset("form:lovMotivoMvrs:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovMotivoMvrs').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('MotivoMvrsDialogo').hide()");
    }

    public void cancelarParametroMotivoMvrs() {
        motivoMvrsSeleccionado = null;
        filtrarLovMotivosMvrs = null;
        casillaMvrs = -1;
        aceptar = true;
        permitirIndexMvrs = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovMotivoMvrs:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovMotivoMvrs').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('MotivoMvrsDialogo').hide()");
    }

    public void actualizarParametroPeriodicidadFormaPago() {
        vigenciaFormaPagoBA.setFormapago(periodicidadSeleccionada);
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:tabViewFormaPago:parametroFormaPagoModFormaPago");
        periodicidadSeleccionada = null;
        filtrarLovPeriodicidades = null;
        casillaVigenciaFormaPago = -1;
        aceptar = true;
        permitirIndexVigenciaFormaPago = true;/*
         * RequestContext.getCurrentInstance().update("form:PeriodicidadFormaPagoDialogo");
         * RequestContext.getCurrentInstance().update("form:lovPeriodicidadFormaPago");
         RequestContext.getCurrentInstance().update("form:aceptarPFP");
         */

        context.reset("form:lovPeriodicidadFormaPago:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovPeriodicidadFormaPago').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('PeriodicidadFormaPagoDialogo').hide()");
    }

    public void cancelarParametroPeriodicidadFormaPago() {
        periodicidadSeleccionada = null;
        filtrarLovPeriodicidades = null;
        casillaVigenciaFormaPago = -1;
        aceptar = true;
        permitirIndexVigenciaFormaPago = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovPeriodicidadFormaPago:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovPeriodicidadFormaPago').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('PeriodicidadFormaPagoDialogo').hide()");
    }

    public void actualizarParametroSucursalFormaPago() {
        vigenciaFormaPagoBA.setSucursal(sucursalSeleccionada);
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:tabViewFormaPago:parametroSucursalModFormaPago");
        sucursalSeleccionada = null;
        filtrarLovSucursales = null;
        casillaVigenciaFormaPago = -1;
        aceptar = true;
        permitirIndexVigenciaFormaPago = true;/*
         * RequestContext.getCurrentInstance().update("form:SucursalFormaPagoDialogo");
         * RequestContext.getCurrentInstance().update("form:lovSucursalFormaPago");
         RequestContext.getCurrentInstance().update("form:aceptarSFP");
         */

        context.reset("form:lovSucursalFormaPago:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovSucursalFormaPago').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('SucursalFormaPagoDialogo').hide()");
    }

    public void cancelarParametroSucursalFormaPago() {
        sucursalSeleccionada = null;
        filtrarLovSucursales = null;
        casillaVigenciaFormaPago = -1;
        aceptar = true;
        permitirIndexVigenciaFormaPago = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovSucursalFormaPago:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovSucursalFormaPago').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('SucursalFormaPagoDialogo').hide()");
    }

    public void actualizarParametroEstadoAfiliacion() {
        vigenciaAfiliacionBA.setEstadoafiliacion(estadoAfiliacionSeleccionado);
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:tabViewAfiliacion:parametroEstadoModAfiliacion");
        estadoAfiliacionSeleccionado = null;
        filtrarLovEstadosAfiliaciones = null;
        casillaVigenciaAfiliacion = -1;
        aceptar = true;
        permitirIndexVigenciaAfiliacion = true;/*
         * RequestContext.getCurrentInstance().update("form:EstadoAfiliacionDialogo");
         * RequestContext.getCurrentInstance().update("form:lovEstadoAfiliacion");
         RequestContext.getCurrentInstance().update("form:aceptarEA");
         */

        context.reset("form:lovEstadoAfiliacion:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovEstadoAfiliacion').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('EstadoAfiliacionDialogo').hide()");
    }

    public void cancelarParametroEstadoAfiliacion() {
        estadoAfiliacionSeleccionado = null;
        filtrarLovEstadosAfiliaciones = null;
        casillaVigenciaAfiliacion = -1;
        aceptar = true;
        permitirIndexVigenciaAfiliacion = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovEstadoAfiliacion:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovEstadoAfiliacion').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('EstadoAfiliacionDialogo').hide()");
    }

    public void actualizarParametroTerceroAfiliacion() {
        vigenciaAfiliacionBA.setTercerosucursal(terceroSucursalSeleccionado);
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:tabViewAfiliacion:parametroTerceroModAfiliacion");
        terceroSucursalSeleccionado = null;
        filtrarLovTercerosSucursales = null;
        casillaVigenciaAfiliacion = -1;
        aceptar = true;
        permitirIndexVigenciaAfiliacion = true;/*
         * RequestContext.getCurrentInstance().update("form:TerceroAfiliacionDialogo");
         * RequestContext.getCurrentInstance().update("form:lovTerceroAfiliacion");
         RequestContext.getCurrentInstance().update("form:aceptarTSA");
         */

        context.reset("form:lovTerceroAfiliacion:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovTerceroAfiliacion').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('TerceroAfiliacionDialogo').hide()");
    }

    public void cancelarParametroTerceroAfiliacion() {
        terceroSucursalSeleccionado = null;
        filtrarLovTercerosSucursales = null;
        casillaVigenciaAfiliacion = -1;
        aceptar = true;
        permitirIndexVigenciaAfiliacion = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovTerceroAfiliacion:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovTerceroAfiliacion').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('TerceroAfiliacionDialogo').hide()");
    }

    public void actualizarParametroTipoEntidadAfiliacion() {
        vigenciaAfiliacionBA.setTipoentidad(tipoEntidadSeleccionado);
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:tabViewAfiliacion:parametroTipoEntidadModAfiliacion");
        tipoEntidadSeleccionado = null;
        filtrarLovTiposEntidades = null;
        casillaVigenciaAfiliacion = -1;
        aceptar = true;
        permitirIndexVigenciaAfiliacion = true;/*
         * RequestContext.getCurrentInstance().update("form:TipoEntidadAfiliacionDialogo");
         * RequestContext.getCurrentInstance().update("form:lovTipoEntidadAfiliacion");
         RequestContext.getCurrentInstance().update("form:aceptarTEA");
         */

        context.reset("form:lovTipoEntidadAfiliacion:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovTipoEntidadAfiliacion').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('TipoEntidadAfiliacionDialogo').hide()");
    }

    public void cancelarParametroTipoEntidadAfiliacion() {
        tipoEntidadSeleccionado = null;
        filtrarLovTiposEntidades = null;
        casillaVigenciaAfiliacion = -1;
        aceptar = true;
        permitirIndexVigenciaAfiliacion = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovTipoEntidadAfiliacion:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovTipoEntidadAfiliacion').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('TipoEntidadAfiliacionDialogo').hide()");
    }

    public void actualizarParametroUbicacionUbicacion() {
        vigenciaUbicacionBA.setUbicacion(ubicacionGeograficaSeleccionada);
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:tabViewUbicacion:parametroUbicacionModUbicacion");
        RequestContext.getCurrentInstance().update("form:tabViewUbicacion:parametroCiudadModUbicacion");
        ubicacionGeograficaSeleccionada = null;
        filtrarLovUbicacionesGeograficas = null;
        casillaVigenciaUbicacion = -1;
        aceptar = true;
        permitirIndexVigenciaUbicacion = true;/*
         * RequestContext.getCurrentInstance().update("form:UbicacionUbicacionDialogo");
         * RequestContext.getCurrentInstance().update("form:lovUbicacionUbicacion");
         RequestContext.getCurrentInstance().update("form:aceptarUUG");
         */

        context.reset("form:lovUbicacionUbicacion:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovUbicacionUbicacion').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('UbicacionUbicacionDialogo').hide()");
    }

    public void cancelarParametroUbicacionUbicacion() {
        ubicacionGeograficaSeleccionada = null;
        filtrarLovUbicacionesGeograficas = null;
        casillaVigenciaUbicacion = -1;
        aceptar = true;
        permitirIndexVigenciaUbicacion = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovUbicacionUbicacion:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovUbicacionUbicacion').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('UbicacionUbicacionDialogo').hide()");
    }

    public void actualizarParametroContratoLegislacionLaboral() {
        vigenciaContratoBA.setContrato(contratoSeleccionado);
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:tabViewLegislacionLaboral:parametroLegislacionModLegislacionLaboral");
        contratoSeleccionado = null;
        filtrarLovContratos = null;
        casillaVigenciaContrato = -1;
        aceptar = true;
        permitirIndexVigenciaContrato = true;/*
         * RequestContext.getCurrentInstance().update("form:LegislacionLegislacionLaboralDialogo");
         * RequestContext.getCurrentInstance().update("form:lovLegislacionLegislacionLaboral");
         RequestContext.getCurrentInstance().update("form:aceptarLLL");
         */

        context.reset("form:lovLegislacionLegislacionLaboral:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovLegislacionLegislacionLaboral').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('LegislacionLegislacionLaboralDialogo').hide()");
    }

    public void cancelarParametroContratoLegislacionLaboral() {
        contratoSeleccionado = null;
        filtrarLovContratos = null;
        casillaVigenciaContrato = -1;
        aceptar = true;
        permitirIndexVigenciaContrato = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovLegislacionLegislacionLaboral:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovLegislacionLegislacionLaboral').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('LegislacionLegislacionLaboralDialogo').hide()");
    }

    public void actualizarParametroNormaLaboralNormaLaboral() {
        vigenciaNormaEmpleadoBA.setNormalaboral(normaLaboralSeleccionada);
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:tabViewNormaLaboral:parametroNormaLaboralModNormaLaboral");
        normaLaboralSeleccionada = null;
        filtrarLovNormasLaborales = null;
        casillaVigenciaNormaLaboral = -1;
        aceptar = true;
        permitirIndexVigenciaNormaEmpleado = true;/*
         * RequestContext.getCurrentInstance().update("form:NormaLaboralNormaLaboralDialogo");
         * RequestContext.getCurrentInstance().update("form:lovNormaLaboralNormaLaboral");
         RequestContext.getCurrentInstance().update("form:aceptarNLNL");
         */

        context.reset("form:lovNormaLaboralNormaLaboral:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovNormaLaboralNormaLaboral').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('NormaLaboralNormaLaboralDialogo').hide()");
    }

    public void cancelarParametroNormaLaboralNormaLaboral() {
        normaLaboralSeleccionada = null;
        filtrarLovNormasLaborales = null;
        casillaVigenciaNormaLaboral = -1;
        aceptar = true;
        permitirIndexVigenciaNormaEmpleado = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovNormaLaboralNormaLaboral:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovNormaLaboralNormaLaboral').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('NormaLaboralNormaLaboralDialogo').hide()");
    }

    public void actualizarParametroReformaLaboralTipoSalario() {
        vigenciaReformaLaboralBA.setReformalaboral(reformaLaboralSeleccionada);
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:tabViewTipoSalario:parametroReformaLaboralModTipoSalario");
        reformaLaboralSeleccionada = null;
        filtrarLovReformasLaborales = null;
        casillaVigenciaReformaLaboral = -1;
        aceptar = true;
        permitirIndexVigenciaReformaLaboral = true;/*
         * RequestContext.getCurrentInstance().update("form:ReformaLaboralTipoSalarioDialogo");
         * RequestContext.getCurrentInstance().update("form:lovReformaLaboralTipoSalario");
        RequestContext.getCurrentInstance().update("form:aceptarRLTS");
         */
        context.reset("form:lovReformaLaboralTipoSalario:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovReformaLaboralTipoSalario').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('ReformaLaboralTipoSalarioDialogo').hide()");
    }

    public void cancelarParametroReformaLaboralTipoSalario() {
        reformaLaboralSeleccionada = null;
        filtrarLovReformasLaborales = null;
        casillaVigenciaReformaLaboral = -1;
        aceptar = true;
        permitirIndexVigenciaReformaLaboral = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovReformaLaboralTipoSalario:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovReformaLaboralTipoSalario').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('ReformaLaboralTipoSalarioDialogo').hide()");
    }

    public void actualizarParametroTipoTrabajadorTipoTrabajador() {
        vigenciaTipoTrabajadorBA.setTipotrabajador(tipoTrabajadorSeleccionado);
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:tabViewTipoTrabajador:parametroTipoTrabajadorModTipoTrabajador");
        tipoTrabajadorSeleccionado = null;
        filtrarLovTiposTrabajadores = null;
        casillaVigenciaTipoTrabajador = -1;
        aceptar = true;
        permitirIndexVigenciaTipoTrabajador = true;/*
         * RequestContext.getCurrentInstance().update("form:TipoTrabajadorTipoTrabajadorDialogo");
         * RequestContext.getCurrentInstance().update("form:lovTipoTrabajadorTipoTrabajador");
        RequestContext.getCurrentInstance().update("form:aceptarTTTT");
         */
        context.reset("form:lovTipoTrabajadorTipoTrabajador:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovTipoTrabajadorTipoTrabajador').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('TipoTrabajadorTipoTrabajadorDialogo').hide()");
    }

    public void cancelarParametroTipoTrabajadorTipoTrabajador() {
        tipoTrabajadorSeleccionado = null;
        filtrarLovTiposTrabajadores = null;
        casillaVigenciaTipoTrabajador = -1;
        aceptar = true;
        permitirIndexVigenciaTipoTrabajador = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovTipoTrabajadorTipoTrabajador:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovTipoTrabajadorTipoTrabajador').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('TipoTrabajadorTipoTrabajadorDialogo').hide()");
    }

    public void actualizarParametroTipoContratoFechaContrato() {
        vigenciaTipoContratoBA.setTipocontrato(tipoContratoSeleccionado);
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:tabViewFechaContrato:parametroTipoContratoModFechaContrato");
        tipoContratoSeleccionado = null;
        filtrarLovTiposContratos = null;
        casillaVigenciaTipoContrato = -1;
        aceptar = true;
        permitirIndexVigenciaTipoContrato = true;/*
         * RequestContext.getCurrentInstance().update("form:TipoContratoFechaContratoDialogo");
         * RequestContext.getCurrentInstance().update("form:lovTipoContratoFechaContrato");
        RequestContext.getCurrentInstance().update("form:aceptarTCFC");
         */
        context.reset("form:lovTipoContratoFechaContrato:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovTipoContratoFechaContrato').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('TipoContratoFechaContratoDialogo').hide()");
    }

    public void cancelarParametroTipoContratoFechaContrato() {
        tipoContratoSeleccionado = null;
        filtrarLovTiposContratos = null;
        casillaVigenciaTipoContrato = -1;
        aceptar = true;
        permitirIndexVigenciaTipoContrato = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovTipoContratoFechaContrato:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovTipoContratoFechaContrato').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('TipoContratoFechaContratoDialogo').hide()");
    }

    public void actualizarParametroMotivoFechaContrato() {
        vigenciaTipoContratoBA.setMotivocontrato(motivoContratoSeleccionado);
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:tabViewFechaContrato:parametroMotivoModFechaContrato");
        motivoContratoSeleccionado = null;
        filtrarLovMotivosContratos = null;
        casillaVigenciaTipoContrato = -1;
        aceptar = true;
        permitirIndexVigenciaTipoContrato = true;/*
         * RequestContext.getCurrentInstance().update("form:MotivoFechaContratoDialogo");
         * RequestContext.getCurrentInstance().update("form:lovMotivoFechaContrato");
        RequestContext.getCurrentInstance().update("form:aceptarMFC");
         */
        context.reset("form:lovMotivoFechaContrato:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovMotivoFechaContrato').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('MotivoFechaContratoDialogo').hide()");
    }

    public void cancelarParametroMotivoFechaContrato() {
        motivoContratoSeleccionado = null;
        filtrarLovMotivosContratos = null;
        casillaVigenciaTipoContrato = -1;
        aceptar = true;
        permitirIndexVigenciaTipoContrato = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovMotivoFechaContrato:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovMotivoFechaContrato').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('MotivoFechaContratoDialogo').hide()");
    }

    public void actualizarParametroTipoSueldoSueldo() {
        vigenciaSueldoBA.setTiposueldo(tipoSueldoSeleccionado);
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:tabViewSueldo:parametroTipoSueldoModSueldo");
        tipoSueldoSeleccionado = null;
        filtrarLovTiposSueldos = null;
        casillaVigenciaSueldo = -1;
        aceptar = true;
        permitirIndexVigenciaSueldo = true;/*
         * RequestContext.getCurrentInstance().update("form:TipoSueldoSueldoDialogo");
         * RequestContext.getCurrentInstance().update("form:lovTipoSueldoSueldo");
        RequestContext.getCurrentInstance().update("form:aceptarTSS");
         */
        context.reset("form:lovTipoSueldoSueldo:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovTipoSueldoSueldo').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('TipoSueldoSueldoDialogo').hide()");
    }

    public void cancelarParametroTipoSueldoSueldo() {
        tipoSueldoSeleccionado = null;
        filtrarLovTiposSueldos = null;
        casillaVigenciaSueldo = -1;
        aceptar = true;
        permitirIndexVigenciaSueldo = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovTipoSueldoSueldo:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovTipoSueldoSueldo').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('TipoSueldoSueldoDialogo').hide()");
    }

    public void actualizarParametroMotivoSueldo() {
        vigenciaSueldoBA.setMotivocambiosueldo(motivoSueldoSeleccionado);
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:tabViewSueldo:parametroMotivoModSueldo");
        motivoSueldoSeleccionado = null;
        filtrarLovMotivosCambiosSueldos = null;
        casillaVigenciaSueldo = -1;
        aceptar = true;
        permitirIndexVigenciaSueldo = true;/*
         * RequestContext.getCurrentInstance().update("form:MotivoSueldoDialogo");
         * RequestContext.getCurrentInstance().update("form:lovMotivoSueldo");
        RequestContext.getCurrentInstance().update("form:aceptarMSS");
         */
        context.reset("form:lovMotivoSueldo:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovMotivoSueldo').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('MotivoSueldoDialogo').hide()");
    }

    public void cancelarParametroMotivoSueldo() {
        motivoSueldoSeleccionado = null;
        filtrarLovMotivosCambiosSueldos = null;
        casillaVigenciaSueldo = -1;
        aceptar = true;
        permitirIndexVigenciaSueldo = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovMotivoSueldo:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovMotivoSueldo').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('MotivoSueldoDialogo').hide()");
    }

    public void actualizarParametroLocalizacionCentroCosto() {
        vigenciaLocalizacionBA.setLocalizacion(estructuraSeleccionada);
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:tabViewCentroCosto:parametroLocalizacionModCentroCosto");
        estructuraSeleccionada = null;
        filtrarLovEstructuras = null;
        casillaVigenciaLocalizacion = -1;
        aceptar = true;
        permitirIndexVigenciaLocalizacion = true;/*
         * RequestContext.getCurrentInstance().update("form:LocalizacionCentroCostoDialogo");
         * RequestContext.getCurrentInstance().update("form:lovLocalizacionCentroCosto");
        RequestContext.getCurrentInstance().update("form:aceptarECC");
         */
        context.reset("form:lovLocalizacionCentroCosto:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovLocalizacionCentroCosto').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('LocalizacionCentroCostoDialogo').hide()");
    }

    public void cancelarParametroLocalizacionCentroCosto() {
        estructuraSeleccionada = null;
        filtrarLovEstructuras = null;
        casillaVigenciaLocalizacion = -1;
        aceptar = true;
        permitirIndexVigenciaLocalizacion = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovLocalizacionCentroCosto:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovLocalizacionCentroCosto').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('LocalizacionCentroCostoDialogo').hide()");
    }

    public void actualizarParametroMotivoCentroCosto() {
        vigenciaLocalizacionBA.setMotivo(motivoLocalizacionSeleccionado);
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:tabViewCentroCosto:parametroMotivoModCentroCosto");
        motivoLocalizacionSeleccionado = null;
        filtrarLovMotivosLocalizaciones = null;
        casillaVigenciaLocalizacion = -1;
        aceptar = true;
        permitirIndexVigenciaLocalizacion = true;/*
         * RequestContext.getCurrentInstance().update("form:MotivoCentroCostoDialogo");
         * RequestContext.getCurrentInstance().update("form:lovMotivoCentroCosto");
        RequestContext.getCurrentInstance().update("form:aceptarMCC");
         */
        context.reset("form:lovMotivoCentroCosto:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovMotivoCentroCosto').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('MotivoCentroCostoDialogo').hide()");
    }

    public void cancelarParametroMotivoCentroCosto() {
        motivoLocalizacionSeleccionado = null;
        filtrarLovMotivosLocalizaciones = null;
        casillaVigenciaLocalizacion = -1;
        aceptar = true;
        permitirIndexVigenciaLocalizacion = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovMotivoCentroCosto:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovMotivoCentroCosto').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('MotivoCentroCostoDialogo').hide()");
    }

    public void actualizarParametroEstructuraCargo() {
        vigenciaCargoBA.setEstructura(estructuraSeleccionada);
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:tabViewCosto:parametroEstructuraModCargo");
        RequestContext.getCurrentInstance().update("form:tabViewCosto:parametroCentroCostoModCargo");
        estructuraSeleccionada = null;
        filtrarLovEstructuras = null;
        casillaVigenciaCargo = -1;
        aceptar = true;
        permitirIndexVigenciaCargo = true;/*
         * RequestContext.getCurrentInstance().update("form:EstructuraCargoDialogo");
         * RequestContext.getCurrentInstance().update("form:lovEstructuraCargo");
        RequestContext.getCurrentInstance().update("form:aceptarEC");
         */
        context.reset("form:lovEstructuraCargo:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovEstructuraCargo').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('EstructuraCargoDialogo').hide()");
    }

    public void cancelarParametroEstructuraCargo() {
        estructuraSeleccionada = null;
        filtrarLovEstructuras = null;
        casillaVigenciaCargo = -1;
        aceptar = true;
        permitirIndexVigenciaCargo = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovEstructuraCargo:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovEstructuraCargo').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('EstructuraCargoDialogo').hide()");
    }

    public void actualizarParametroCargoCargo() {
        vigenciaCargoBA.setCargo(cargoSeleccionado);
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:tabViewCosto:parametroCargoModCargo");
        cargoSeleccionado = null;
        filtrarLovCargos = null;
        casillaVigenciaCargo = -1;
        aceptar = true;
        permitirIndexVigenciaCargo = true;/*
         * RequestContext.getCurrentInstance().update("form:CargoCargoDialogo");
         * RequestContext.getCurrentInstance().update("form:lovCargoCargo");
        RequestContext.getCurrentInstance().update("form:aceptarCC");
         */
        context.reset("form:lovCargoCargo:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovCargoCargo').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('CargoCargoDialogo').hide()");
    }

    public void cancelarParametroCargoCargo() {
        cargoSeleccionado = null;
        filtrarLovCargos = null;
        casillaVigenciaCargo = -1;
        aceptar = true;
        permitirIndexVigenciaCargo = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovCargoCargo:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovCargoCargo').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('CargoCargoDialogo').hide()");
    }

    public void actualizarParametroPapelCargo() {
        vigenciaCargoBA.setPapel(papelSeleccionado);
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:tabViewCosto:parametroPapelModCargo");
        papelSeleccionado = null;
        filtrarLovPapeles = null;
        casillaVigenciaCargo = -1;
        aceptar = true;
        permitirIndexVigenciaCargo = true;/*
         * RequestContext.getCurrentInstance().update("form:PapelCargoDialogo");
         * RequestContext.getCurrentInstance().update("form:lovPapelCargo");
        RequestContext.getCurrentInstance().update("form:aceptarPC");
         */
        context.reset("form:lovPapelCargo:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovPapelCargo').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('PapelCargoDialogo').hide()");
    }

    public void cancelarParametroPapelCargo() {
        papelSeleccionado = null;
        filtrarLovPapeles = null;
        casillaVigenciaCargo = -1;
        aceptar = true;
        permitirIndexVigenciaCargo = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovPapelCargo:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovPapelCargo').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('PapelCargoDialogo').hide()");
    }

    public void actualizarParametroMotivoCargo() {
        vigenciaCargoBA.setMotivocambiocargo(motivoCambioSeleccionado);
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:tabViewCosto:parametroMotivoModCargo");
        motivoCambioSeleccionado = null;
        filtrarLovMotivosCambiosCargos = null;
        casillaVigenciaCargo = -1;
        aceptar = true;
        permitirIndexVigenciaCargo = true;/*
         * RequestContext.getCurrentInstance().update("form:MotivoCargoDialogo");
         * RequestContext.getCurrentInstance().update("form:lovMotivoCargo");
        RequestContext.getCurrentInstance().update("form:aceptarMC");
         */
        context.reset("form:lovMotivoCargo:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovMotivoCargo').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('MotivoCargoDialogo').hide()");
    }

    public void cancelarParametroMotivoCargo() {
        motivoCambioSeleccionado = null;
        filtrarLovMotivosCambiosCargos = null;
        casillaVigenciaCargo = -1;
        aceptar = true;
        permitirIndexVigenciaCargo = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovMotivoCargo:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovMotivoCargo').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('MotivoCargoDialogo').hide()");
    }

    public void actualizarParametroJefeCargo() {
        vigenciaCargoBA.setEmpleadojefe(empleadoSeleccionado);
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:tabViewCosto:parametroJefeModCargo");
        empleadoSeleccionado = null;
        filtrarLovEmpleados = null;
        casillaVigenciaCargo = -1;
        aceptar = true;
        permitirIndexVigenciaCargo = true;/*
         * RequestContext.getCurrentInstance().update("form:JefeCargoDialogo");
         * RequestContext.getCurrentInstance().update("form:lovJefeCargo");
        RequestContext.getCurrentInstance().update("form:aceptarJC");
         */
        context.reset("form:lovJefeCargo:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovJefeCargo').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('JefeCargoDialogo').hide()");
    }

    public void cancelarParametroJefeCargo() {
        empleadoSeleccionado = null;
        filtrarLovEmpleados = null;
        casillaVigenciaCargo = -1;
        aceptar = true;
        permitirIndexVigenciaCargo = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovJefeCargo:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovJefeCargo').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('JefeCargoDialogo').hide()");
    }

    public void changeTapCargo() {
        System.out.println("tabActivaCargos : " + tabActivaCargos);
    }

    public void changeTapCentroCosto() {
        if (tabActivaCentroCosto == 1) {
            auxTabActivoCentroCosto = true;
        } else {
            auxTabActivoCentroCosto = false;
        }
    }

    public void changeTapSueldo() {
        System.out.println("tabActivaSueldo : " + tabActivaSueldo);
    }

    public void changeTapFechaContrato() {
        System.out.println("tabActivaFechaContrato : " + tabActivaFechaContrato);
    }

    public void changeTapTipoTrabajador() {
        System.out.println("tabActivaTipoTrabajador : " + tabActivaTipoTrabajador);
    }

    public void changeTapTipoSalario() {
        System.out.println("tabActivaTipoSalario : " + tabActivaTipoSalario);
    }

    public void changeTapNormaLaboral() {
        System.out.println("tabActivaNormaLaboral : " + tabActivaNormaLaboral);
    }

    public void changeTapLegislacionLaboral() {
        System.out.println("tabActivaLegislacionLaboral : " + tabActivaLegislacionLaboral);
    }

    public void changeTapUbicacion() {
        System.out.println("tabActivaUbicacion : " + tabActivaUbicacion);
    }

    public void changeTapAfiliacion() {
        System.out.println("tabActivaAfiliacion : " + tabActivaAfiliacion);
    }

    public void changeTapFormaPago() {
        System.out.println("tabActivaFormaPago : " + tabActivaFormaPago);
    }

    public void changeTapMvrs() {
        System.out.println("tabActivaMvrs : " + tabActivaMvrs);
    }

    public void changeTapSets() {
        System.out.println("tabActivaSets : " + tabActivaSets);
    }

    public void changeTapVacacion() {
        System.out.println("tabActivaVacacion : " + tabActivaVacacion);
    }

    public void changeTapJornadaLaboral() {
        System.out.println("tabActivaJornadaLaboral : " + tabActivaJornadaLaboral);
    }

    public void changeTapFechaRetiro() {
        System.out.println("tabActivaFechaRetiro : " + tabActivaFechaRetiro);
    }

    public void changeTapDatosPersonales() {
        System.out.println("tabActivaDatosPersonales : " + tabActivaDatosPersonales);
    }

    public void changeTapFactorRH() {
        System.out.println("tabActivaFactorRH : " + tabActivaFactorRH);
    }

    public void changeTapEstadoCivil() {
        System.out.println("tabActivaEstadoCivil : " + tabActivaEstadoCivil);
    }

    public void changeTapIdioma() {
        System.out.println("tabActivaIdioma : " + tabActivaIdioma);
    }

    public void changeTapCenso() {
        System.out.println("tabActivaCenso : " + tabActivaCenso);
    }

    public void changeTapEducacionFormal() {
        System.out.println("tabActivaEducacionFormal : " + tabActivaEducacionFormal);
    }

    public void changeTapEducacionNoFormal() {
        System.out.println("tabActivaEducacionNoFormal : " + tabActivaEducacionNoFormal);
    }

    public void changeTapExperienciaLaboral() {
        System.out.println("tabActivaExperienciaLaboral : " + tabActivaExperienciaLaboral);
    }

    public void changeTapProyecto() {
        System.out.println("tabActivaProyecto : " + tabActivaProyecto);
    }

    public void changeTapCargoPostularse() {
        System.out.println("tabActivaCargoPostularse : " + tabActivaCargoPostularse);
    }

    public void changeTapTipoBusqueda() {
        System.out.println("numeroTipoBusqueda : " + numeroTipoBusqueda);
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void activarCasillasFechasCargo() {
        if (tipoFechaCargo == 1) {
            fechaInicialCargo = null;
            fechaFinalCargo = null;
            auxFechaFinalCargo = null;
            auxFechaInicialCargo = null;
            activoFechasCargos = true;
        }
        if (tipoFechaCargo == 2) {
            activoFechasCargos = false;
        }
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:tabViewCosto");
        RequestContext.getCurrentInstance().update("form:tabViewCosto:tipoFechaCargo");
    }

    public void activarCasillasFechasCentroCosto() {
        if (tipoFechaCentroCosto == 1) {
            activoFechasCentroCosto = true;
            fechaInicialCentroCosto = null;
            fechaFinalCentroCosto = null;
            auxFechaFinalCentroCosto = null;
            auxFechaInicialCentroCosto = null;
        }
        if (tipoFechaCentroCosto == 2) {
            activoFechasCentroCosto = false;
        }
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:tabViewCentroCosto");
        RequestContext.getCurrentInstance().update("form:tabViewCentroCosto:tipoFechaCentroCosto");
    }

    public void activarCasillasFechasSueldo() {
        if (tipoFechaSueldo == 1) {
            activoFechasSueldo = true;
            fechaInicialSueldo = null;
            fechaFinalSueldo = null;
            auxFechaFinalSueldo = null;
            auxFechaInicialSueldo = null;
        }
        if (tipoFechaSueldo == 2) {
            activoFechasSueldo = false;
        }
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:tabViewSueldo");
        RequestContext.getCurrentInstance().update("form:tabViewSueldo:tipoFechaSueldo");
    }

    public void activarCasillasFechasFechaContrato() {
        if (tipoFechaFechaContrato == 1) {
            activoFechasFechaContrato = true;
            fechaInicialFechaContrato = null;
            fechaFinalFechaContrato = null;
            auxFechaFinalFechaContrato = null;
            auxFechaInicialFechaContrato = null;
        }
        if (tipoFechaFechaContrato == 2) {
            activoFechasFechaContrato = false;
        }
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:tabViewFechaContrato");
        RequestContext.getCurrentInstance().update("form:tabViewFechaContrato:tipoFechaFechaContrato");
    }

    public void activarCasillasFechasTipoTrabajador() {
        if (tipoFechaTipoTrabajador == 1) {
            activoFechasTipoTrabajador = true;
            fechaInicialTipoTrabajador = null;
            fechaFinalTipoTrabajador = null;
            auxFechaFinalTipoTrabajador = null;
            auxFechaInicialTipoTrabajador = null;
        }
        if (tipoFechaTipoTrabajador == 2) {
            activoFechasTipoTrabajador = false;
        }
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:tabViewTipoTrabajador");
        RequestContext.getCurrentInstance().update("form:tabViewTipoTrabajador:tipoFechaTipoTrabajador");
    }

    public void activarCasillasFechasTipoSalario() {
        if (tipoFechaTipoSalario == 1) {
            activoFechasTipoSalario = true;
            fechaInicialTipoSalario = null;
            fechaFinalTipoSalario = null;
            auxFechaFinalTipoSalario = null;
            auxFechaInicialTipoSalario = null;
        }
        if (tipoFechaTipoSalario == 2) {
            activoFechasTipoSalario = false;
        }
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:tabViewTipoSalario");
        RequestContext.getCurrentInstance().update("form:tabViewTipoSalario:tipoFechaTipoSalario");
    }

    public void activarCasillasFechasNormaLaboral() {
        if (tipoFechaNormaLaboral == 1) {
            activoFechasNormaLaboral = true;
            fechaInicialNormaLaboral = null;
            fechaFinalNormaLaboral = null;
            auxFechaFinalNormaLaboral = null;
            auxFechaInicialNormaLaboral = null;
        }
        if (tipoFechaNormaLaboral == 2) {
            activoFechasNormaLaboral = false;
        }
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:tabViewNormaLaboral");
        RequestContext.getCurrentInstance().update("form:tabViewNormaLaboral:tipoFechaNormaLaboral");
    }

    public void activarCasillasFechasLegislacionLaboral() {
        if (tipoFechaLegislacionLaboral == 1) {
            activoFechasLegislacionLaboral = true;
            fechaMFFinalLegislacionLaboral = null;
            fechaMFInicialLegislacionLaboral = null;
            fechaMIFinalLegislacionLaboral = null;
            fechaMIInicialLegislacionLaboral = null;
            auxFechaMFFinalLegislacionLaboral = null;
            auxFechaMFInicialLegislacionLaboral = null;
            auxFechaMIFinalLegislacionLaboral = null;
            auxFechaMIInicialLegislacionLaboral = null;
        }
        if (tipoFechaLegislacionLaboral == 2) {
            activoFechasLegislacionLaboral = false;
        }
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:tabViewLegislacionLaboral");
        RequestContext.getCurrentInstance().update("form:tabViewLegislacionLaboral:tipoFechaLegislacionLaboral");
    }

    public void activarCasillasFechasUbicacion() {
        if (tipoFechaUbicacion == 1) {
            activoFechasUbicacion = true;
            fechaInicialUbicacion = null;
            fechaFinalUbicacion = null;
            auxFechaFinalUbicacion = null;
            auxFechaInicialUbicacion = null;
        }
        if (tipoFechaUbicacion == 2) {
            activoFechasUbicacion = false;
        }
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:tabViewUbicacion");
        RequestContext.getCurrentInstance().update("form:tabViewUbicacion:tipoFechaUbicacion");
    }

    public void activarCasillasFechasAfiliacion() {
        if (tipoFechaAfiliacion == 1) {
            activoFechasAfiliacion = true;
            fechaInicialAfiliacion = null;
            fechaFinalAfiliacion = null;
            auxFechaFinalAfiliacion = null;
            auxFechaInicialAfiliacion = null;
        }
        if (tipoFechaAfiliacion == 2) {
            activoFechasAfiliacion = false;
        }
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:tabViewAfiliacion");
        RequestContext.getCurrentInstance().update("form:tabViewAfiliacion:tipoFechaAfiliacion");
    }

    public void activarCasillasFechasFormaPago() {
        if (tipoFechaFormaPago == 1) {
            activoFechasFormaPago = true;
            fechaInicialFormaPago = null;
            fechaFinalFormaPago = null;
            auxFechaFinalFormaPago = null;
            auxFechaInicialFormaPago = null;
        }
        if (tipoFechaFormaPago == 2) {
            activoFechasFormaPago = false;
        }
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:tabViewFormaPago");
        RequestContext.getCurrentInstance().update("form:tabViewFormaPago:tipoFechaFormaPago");
    }

    public void activarCasillasFechasMvrs() {
        if (tipoFechaMvrs == 1) {
            activoFechasMvrs = true;
            fechaInicialMvrs = null;
            fechaFinalMvrs = null;
            auxFechaFinalMvrs = null;
            auxFechaInicialMvrs = null;
        }
        if (tipoFechaMvrs == 2) {
            activoFechasMvrs = false;
        }
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:tabViewMvrs");
        RequestContext.getCurrentInstance().update("form:tabViewMvrs:tipoFechaMvrs");
    }

    public void activarCasillasFechasSets() {
        if (tipoFechaSets == 1) {
            activoFechasSets = true;
            fechaMFFinalSets = null;
            fechaMFInicialSets = null;
            fechaMIFinalSets = null;
            fechaMIInicialSets = null;
            auxFechaMFFinalSets = null;
            auxFechaMFInicialSets = null;
            auxFechaMIFinalSets = null;
            auxFechaMIInicialSets = null;
        }
        if (tipoFechaSets == 2) {
            activoFechasSets = false;
        }
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:tabViewSets");
        RequestContext.getCurrentInstance().update("form:tabViewSets:tipoFechaLegislacionLaboral");
    }

    public void activarCasillasFechasEstadoCivil() {
        if (tipoFechaEstadoCivil == 1) {
            activoFechasEstadoCivil = true;
            fechaFinalEstadoCivil = null;
            fechaInicialEstadoCivil = null;
            auxFechaFinalEstadoCivil = null;
            auxFechaInicialEstadoCivil = null;
        }
        if (tipoFechaEstadoCivil == 2) {
            activoFechasEstadoCivil = false;
        }
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:tabViewEstadoCivil");
        RequestContext.getCurrentInstance().update("form:tabViewEstadoCivil:tipoFechaEstadoCivil");
    }

    public void activarCasillasFechasCenso() {
        if (tipoFechaCenso == 1) {
            activoFechasCenso = true;
            fechaFinalCenso = null;
            fechaInicialCenso = null;
            auxFechaFinalCenso = null;
            auxFechaInicialCenso = null;
        }
        if (tipoFechaCenso == 2) {
            activoFechasCenso = false;
        }
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:tabViewCenso");
        RequestContext.getCurrentInstance().update("form:tabViewCenso:tipoFechaCenso");
    }

    public void activarCasillasFechasEducacionFormal() {
        if (tipoFechaEducacionFormal == 1) {
            activoFechasEducacionFormal = true;
            fechaFinalEducacionFormal = null;
            fechaInicialEducacionFormal = null;
            auxFechaFinalEducacionFormal = null;
            auxFechaInicialEducacionFormal = null;
        }
        if (tipoFechaEducacionFormal == 2) {
            activoFechasEducacionFormal = false;
        }
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:tabViewEducacionFormal");
        RequestContext.getCurrentInstance().update("form:tabViewEducacionFormal:tipoFechaEducacionFormal");
    }

    public void activarCasillasFechasEducacionNoFormal() {
        if (tipoFechaEducacionNoFormal == 1) {
            activoFechasEducacionNoFormal = true;
            fechaFinalEducacionNoFormal = null;
            fechaInicialEducacionNoFormal = null;
            auxFechaFinalEducacionNoFormal = null;
            auxFechaInicialEducacionNoFormal = null;
        }
        if (tipoFechaEducacionNoFormal == 2) {
            activoFechasEducacionNoFormal = false;
        }
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:tabViewEducacionNoFormal");
        RequestContext.getCurrentInstance().update("form:tabViewEducacionNoFormal:tipoFechaEducacionNoFormal");
    }

    //Clase de columnas que sera cargadas en la tabla
    static public class ColumnModel implements Serializable {

        private String header;
        //Cabecera de la columna
        private String property;
        //Valor que sera mostrado en cada columna

        public ColumnModel(String header, String property) {
            this.header = header;
            this.property = property;
        }

        public String getHeader() {
            return header;
        }

        public String getProperty() {
            return property;
        }
    }

    public String getColumnTemplate() {
        return columnTemplate;
    }

    public void setColumnTemplate(String columnTemplate) {
        this.columnTemplate = columnTemplate;
    }

    public void updateColumns() {
        //reset table state
        UIComponent table = FacesContext.getCurrentInstance().getViewRoot().findComponent(":form:resultadoBusquedaAvanzada");
        table.setValueExpression("sortBy", null);
        //update columns
        int tam = 0;
        listaResultadoBusquedaAvanzada = new ArrayList<ResultadoBusquedaAvanzada>();
        if (listaColumnasEscenarios != null) {
            tam = listaColumnasEscenarios.size();
        }
        String camposBusqueda = " secuencia, codigoempleado, primerapellido, segundoapellido, nombre";
        if (tam > 0) {
            for (int j = 0; j < tam; j++) {
                camposBusqueda = camposBusqueda + ", " + listaColumnasEscenarios.get(j).getNombrecolumna();
            }
        }
        listaColumnasBusquedaAvanzada = new ArrayList<ColumnasBusquedaAvanzada>();
//        listaResultadoBusquedaAvanzada = administrarBusquedaAvanzada.obtenerQVWEmpleadosCorteParaEmpleadoCodigo(listaCodigosEmpleado, camposBusqueda);
        int tamanoLista = 0;
        if (listaResultadoBusquedaAvanzada != null) {
            tamanoLista = listaResultadoBusquedaAvanzada.size();
        }
        for (int i = 0; i < tamanoLista; i++) {
            listaColumnasBusquedaAvanzada.add(new ColumnasBusquedaAvanzada("", "", "", "", "", "", "", "", "", "", "", "", "", ""));
        }
        for (int j = 0; j < tam; j++) {
            String[] palabras = listaColumnasEscenarios.get(j).getNombrecolumna().split(" ");
            nuevaColumna = palabras[0];
            for (int i = 1; i < palabras.length; i++) {
                nuevaColumna = nuevaColumna + "_" + palabras[i];
            }
            columnTemplate = columnTemplate + nuevaColumna + " ";
            nuevaColumna = "";
        }
        columns.clear();
        String[] valoresColumnasDeseadas = camposBusqueda.split(",");
        mapValoresColumnas.clear();
        if (tamanoLista > 0) {
            if (valoresColumnasDeseadas.length == 5) {
                createStaticColumns();
                cargarTablaColumnasEstaticas();
            }
            if (valoresColumnasDeseadas.length >= 6) {
                createStaticColumns();
                createDynamicColumns();
                cargarTablaColumnasEstaticas();
                cargarTablaColumnasDinamicas();
            }
        } else {
            createStaticColumns();
        }
        FacesContext c = FacesContext.getCurrentInstance();
        altoTabla = "80";
        columnasDinamicas = (Columns) c.getViewRoot().findComponent("form:resultadoBusquedaAvanzada:columnasDinamicas");
        columnasDinamicas.setFilterStyle("display: none; visibility: hidden;");
        RequestContext.getCurrentInstance().update("form:resultadoBusquedaAvanzada");
        bandera = 0;
        filtrarListaColumnasBusquedaAvanzada = null;
        tipoLista = 0;
        int tamLL = 0;
        if (listaColumnasBusquedaAvanzada != null) {
            tamLL = listaColumnasBusquedaAvanzada.size();
            if (tamLL > 0) {
                columnaSeleccionada = listaColumnasBusquedaAvanzada.get(0);
            }
        }
        /*
         * int tamEmpleado = listaEmpleadosResultados.size(); for (int i = 0; i
         * < tamEmpleado; i++) { listaColumnasBusquedaAvanzada.add(new
         * ColumnasBusquedaAvanzada("", "", "", "", "", "", "", "", "", "", "",
         * "", "", "")); } int tam = 0; if (listaColumnasEscenarios != null) {
         * tam = listaColumnasEscenarios.size(); } System.out.println("dsadsa
         * listaColumnasEscenarios : " + tam); if (tam > 0) { for (int j = 0; j
         * < listaColumnasEscenarios.size(); j++) { String[] palabras =
         * listaColumnasEscenarios.get(j).getNombrecolumna().split(" ");
         * nuevaColumna = palabras[0]; for (int i = 1; i < palabras.length; i++)
         * { nuevaColumna = nuevaColumna + "_" + palabras[i]; } columnTemplate =
         * columnTemplate + nuevaColumna + " "; nuevaColumna = ""; } } String[]
         * valoresColumnasDeseadas = columnTemplate.split(" "); columns.clear();
         * mapValoresColumnas.clear(); if (tamEmpleado > 0) {
         * System.out.println("valoresColumnasDeseadas.length : " +
         * valoresColumnasDeseadas.length); if (valoresColumnasDeseadas.length
         * == 4) { createStaticColumns(); cargarTablaColumnasEstaticas(); } if
         * (valoresColumnasDeseadas.length >= 5) { createStaticColumns();
         * createDynamicColumns(); cargarTablaColumnasEstaticas();
         * cargarTablaColumnasDinamicas(); } } else {
         * listaColumnasBusquedaAvanzada = null; createStaticColumns();
         }
         */
    }

    public void createStaticColumns() {
        String nameColumnaEstatica0 = "codigo";
        mapValoresColumnas.put(nameColumnaEstatica0, "Codigo");
        String nameColumnaEstatica1 = "primeroApellido";
        mapValoresColumnas.put(nameColumnaEstatica1, "Primer_Apellido");
        String nameColumnaEstatica2 = "segundoApellido";
        mapValoresColumnas.put(nameColumnaEstatica2, "Segundo_Apellido");
        String nameColumnaEstatica3 = "nombre";
        mapValoresColumnas.put(nameColumnaEstatica3, "Nombre");
        for (int i = 0; i < 4; i++) {
            columns.add(i, new ColumnModel("", ""));
        }
        columns.set(0, new ColumnModel("Codigo".toUpperCase(), "codigo"));
        columns.set(1, new ColumnModel("Primer_Apellido".toUpperCase(), "primeroApellido"));
        columns.set(2, new ColumnModel("Segundo_Apellido".toUpperCase(), "segundoApellido"));
        columns.set(3, new ColumnModel("Nombre".toUpperCase(), "nombre"));
    }

    public void createDynamicColumns() {
        String[] valoresColumnasDeseadas = columnTemplate.split(" ");
        int numColumna = 0;
        for (int i = 4; i < valoresColumnasDeseadas.length; i++) {
            String nameColumna = "columna" + String.valueOf(numColumna);
            mapValoresColumnas.put(nameColumna, valoresColumnasDeseadas[i]);
            numColumna = numColumna + 1;
        }
        int numeroColumnas = mapValoresColumnas.size();
        for (int i = 4; i < numeroColumnas; i++) {
            columns.add(i, new ColumnModel("", ""));
        }
        for (Map.Entry<String, String> entry : mapValoresColumnas.entrySet()) {
            String numero = entry.getKey().charAt(entry.getKey().length() - 1) + "";
            if (esNumero(numero) == true) {
                int numeroCol = Integer.parseInt(numero);
                if (VALID_COLUMN_KEYS.contains(entry.getKey())) {
                    columns.set(numeroCol + 4, new ColumnModel(entry.getValue().toUpperCase(), entry.getKey().toString()));
                }
            }
        }
    }

    public boolean esNumero(String valor) {
        try {
            int numeroCol = Integer.parseInt(valor);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public void cargarTablaColumnasEstaticas() {
        for (int i = 0; i < 4; i++) {
            int tamEmpleado = listaResultadoBusquedaAvanzada.size();
            if (columns.get(i).getProperty().equalsIgnoreCase("codigo")) {
                for (int j = 0; j < tamEmpleado; j++) {
                    listaColumnasBusquedaAvanzada.get(j).setCodigo(listaResultadoBusquedaAvanzada.get(j).getCodigoEmpleado().toString().toUpperCase());
                }
            }
            if (columns.get(i).getProperty().equalsIgnoreCase("primeroApellido")) {
                for (int j = 0; j < tamEmpleado; j++) {
                    listaColumnasBusquedaAvanzada.get(j).setPrimeroApellido(listaResultadoBusquedaAvanzada.get(j).getPrimerApellido().toUpperCase());
                }
            }
            if (columns.get(i).getProperty().equalsIgnoreCase("segundoApellido")) {
                for (int j = 0; j < tamEmpleado; j++) {
                    listaColumnasBusquedaAvanzada.get(j).setSegundoApellido(listaResultadoBusquedaAvanzada.get(j).getSegundoApellido().toUpperCase());
                }
            }
            if (columns.get(i).getProperty().equalsIgnoreCase("nombre")) {
                for (int j = 0; j < tamEmpleado; j++) {
                    listaColumnasBusquedaAvanzada.get(j).setNombre(listaResultadoBusquedaAvanzada.get(j).getNombre().toUpperCase());
                }
            }
        }

    }

    public void cargarTablaColumnasDinamicas() {
        List<String> campos = new ArrayList<String>();
        for (int i = 4; i < columns.size(); i++) {
            campos.add(columns.get(i).getHeader());
        }
        int tam = columns.size();
        for (int i = 4; i < tam; i++) {
            for (int j = 0; j < listaColumnasBusquedaAvanzada.size(); j++) {
                if (columns.get(i).getProperty().equalsIgnoreCase("columna0")) {
                    if (listaResultadoBusquedaAvanzada.get(j).getColumna0() != null) {
                        listaColumnasBusquedaAvanzada.get(j).setColumna0(listaResultadoBusquedaAvanzada.get(j).getColumna0().toUpperCase());
                    } else {
                        listaColumnasBusquedaAvanzada.get(j).setColumna0(" ");
                    }
                }
                if (columns.get(i).getProperty().equalsIgnoreCase("columna1")) {
                    if (listaResultadoBusquedaAvanzada.get(j).getColumna0() != null) {
                        listaColumnasBusquedaAvanzada.get(j).setColumna1(listaResultadoBusquedaAvanzada.get(j).getColumna1().toUpperCase());
                    } else {
                        listaColumnasBusquedaAvanzada.get(j).setColumna0(" ");
                    }
                }
                if (columns.get(i).getProperty().equalsIgnoreCase("columna2")) {
                    if (listaResultadoBusquedaAvanzada.get(j).getColumna0() != null) {
                        listaColumnasBusquedaAvanzada.get(j).setColumna2(listaResultadoBusquedaAvanzada.get(j).getColumna2().toUpperCase());
                    } else {
                        listaColumnasBusquedaAvanzada.get(j).setColumna0(" ");
                    }
                }
                if (columns.get(i).getProperty().equalsIgnoreCase("columna3")) {
                    if (listaResultadoBusquedaAvanzada.get(j).getColumna0() != null) {
                        listaColumnasBusquedaAvanzada.get(j).setColumna3(listaResultadoBusquedaAvanzada.get(j).getColumna3().toUpperCase());
                    } else {
                        listaColumnasBusquedaAvanzada.get(j).setColumna0(" ");
                    }
                }
                if (columns.get(i).getProperty().equalsIgnoreCase("columna4")) {
                    if (listaResultadoBusquedaAvanzada.get(j).getColumna0() != null) {
                        listaColumnasBusquedaAvanzada.get(j).setColumna4(listaResultadoBusquedaAvanzada.get(j).getColumna4().toUpperCase());
                    } else {
                        listaColumnasBusquedaAvanzada.get(j).setColumna0(" ");
                    }
                }
                if (columns.get(i).getProperty().equalsIgnoreCase("columna5")) {
                    if (listaResultadoBusquedaAvanzada.get(j).getColumna0() != null) {
                        listaColumnasBusquedaAvanzada.get(j).setColumna5(listaResultadoBusquedaAvanzada.get(j).getColumna5().toUpperCase());
                    } else {
                        listaColumnasBusquedaAvanzada.get(j).setColumna0(" ");
                    }
                }
                if (columns.get(i).getProperty().equalsIgnoreCase("columna6")) {
                    if (listaResultadoBusquedaAvanzada.get(j).getColumna0() != null) {
                        listaColumnasBusquedaAvanzada.get(j).setColumna6(listaResultadoBusquedaAvanzada.get(j).getColumna6().toUpperCase());
                    } else {
                        listaColumnasBusquedaAvanzada.get(j).setColumna0(" ");
                    }
                }
                if (columns.get(i).getProperty().equalsIgnoreCase("columna7")) {
                    if (listaResultadoBusquedaAvanzada.get(j).getColumna0() != null) {
                        listaColumnasBusquedaAvanzada.get(j).setColumna7(listaResultadoBusquedaAvanzada.get(j).getColumna7().toUpperCase());
                    } else {
                        listaColumnasBusquedaAvanzada.get(j).setColumna0(" ");
                    }
                }
                if (columns.get(i).getProperty().equalsIgnoreCase("columna8")) {
                    if (listaResultadoBusquedaAvanzada.get(j).getColumna0() != null) {
                        listaColumnasBusquedaAvanzada.get(j).setColumna8(listaResultadoBusquedaAvanzada.get(j).getColumna8().toUpperCase());
                    } else {
                        listaColumnasBusquedaAvanzada.get(j).setColumna0(" ");
                    }
                }
                if (columns.get(i).getProperty().equalsIgnoreCase("columna9")) {
                    if (listaResultadoBusquedaAvanzada.get(j).getColumna0() != null) {
                        listaColumnasBusquedaAvanzada.get(j).setColumna9(listaResultadoBusquedaAvanzada.get(j).getColumna9().toUpperCase());
                    } else {
                        listaColumnasBusquedaAvanzada.get(j).setColumna0(" ");
                    }
                }
            }
        }
    }

    public void restaurar() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 1) {
            altoTabla = "80";
            columnasDinamicas = (Columns) c.getViewRoot().findComponent("form:resultadoBusquedaAvanzada:columnasDinamicas");
            columnasDinamicas.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:resultadoBusquedaAvanzada");
            bandera = 0;
            filtrarListaColumnasBusquedaAvanzada = null;
            tipoLista = 0;
        }
        mapValoresColumnas.clear();
        columns.clear();
        columnTemplate = "";
        columnTemplate = "Codigo Primer_Apellido Segundo_Apellido Nombre ";
        listaResultadoBusquedaAvanzada = null;
        listaColumnasBusquedaAvanzada = new ArrayList<ColumnasBusquedaAvanzada>();
        createStaticColumns();
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:resultadoBusquedaAvanzada");
    }

    public void eventoFiltrar() {
        if (indice >= 0) {
            if (tipoLista == 0) {
                tipoLista = 1;
            }
        }
    }

    public void filtradoBusquedaAvanzada() {
        if (indice >= 0) {
            FacesContext c = FacesContext.getCurrentInstance();
            if (bandera == 0) {
                altoTabla = "58";
                columnasDinamicas = (Columns) c.getViewRoot().findComponent("form:resultadoBusquedaAvanzada:columnasDinamicas");
                columnasDinamicas.setFilterStyle("width: 85% !important;");
                RequestContext.getCurrentInstance().update("form:resultadoBusquedaAvanzada");
                bandera = 1;
            } else if (bandera == 1) {
                altoTabla = "80";
                columnasDinamicas = (Columns) c.getViewRoot().findComponent("form:resultadoBusquedaAvanzada:columnasDinamicas");
                columnasDinamicas.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:resultadoBusquedaAvanzada");
                bandera = 0;
                filtrarListaColumnasBusquedaAvanzada = null;
                tipoLista = 0;
            }
        }
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:resultadoBusqueda");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "ResultadosBusquedaAvanzada_PDF", false, false, "UTF-8", null, null);
        context.responseComplete();

    }

    /**
     * Metodo que exporta datos a XLS
     *
     * @throws IOException Excepcion de In-Out de datos
     */
    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:resultadoBusqueda");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "ResultadosBusquedaAvanzada_XLS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void posicionTablaBusquedaAvanzada() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, String> map = context.getExternalContext().getRequestParameterMap();
        String name = map.get("n"); // name attribute of node
        String type = map.get("t"); // type attribute of node
        int indice = Integer.parseInt(type);
        int columna = Integer.parseInt(name);
        cambiarIndice(indice, columna);
    }

    public void cambiarIndice(int i, int c) {
        indice = i;
        cualCelda = c;
        System.out.println("Indice : " + i + " -- Celda : " + c);
    }

    public void cambioBtnPrueba() {
        if (numeroTipoBusqueda == 0) {
            displayNomina = "inline";
            displayPersonal = "none";
            visibilidadPersonal = "hidden";
            visibilidadNomina = "visible";
        }
        if (numeroTipoBusqueda == 1) {
            displayNomina = "none";
            displayPersonal = "inline";
            visibilidadPersonal = "visible";
            visibilidadNomina = "hidden";
        }
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:scrollPanelNomina");
        RequestContext.getCurrentInstance().update("form:scrollPanelPersonal");
    }

    public void modificarParametroNumeroDocumentoModDatosPersonales(String numeroDocumento) {
        System.out.println("numeroDocumento: " + numeroDocumento);
        boolean esNumero = isNumber(numeroDocumento);
        if (esNumero == false) {
            empleadoBA.getPersona().setStrNumeroDocumento("");
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:tabViewDatosPersonales:parametroNDocModDatosPersonales");
            RequestContext.getCurrentInstance().execute("PF('errorFormatoNumDocumento').show()");
        }
    }

    public boolean isNumber(String numeroDocumento) {
        try {
            boolean retorno = true;
            int numero = Integer.parseInt(numeroDocumento);
            return retorno;
        } catch (NumberFormatException e) {
            System.out.println("El documento no es numero : " + e.toString());
            return false;
        }
    }

    public int getNumeroTipoBusqueda() {
        return numeroTipoBusqueda;
    }

    public void setNumeroTipoBusqueda(int numeroTipoBusqueda) {
        this.numeroTipoBusqueda = numeroTipoBusqueda;
    }

    ////
    public List<ResultadoBusquedaAvanzada> getListaResultadoBusquedaAvanzada() {
        return listaResultadoBusquedaAvanzada;
    }

    public void setListaResultadoBusquedaAvanzada(List<ResultadoBusquedaAvanzada> lista) {
        this.listaResultadoBusquedaAvanzada = lista;
    }

    public List<ColumnModel> getColumns() {
        return columns;
    }

    public List<ResultadoBusquedaAvanzada> getFilteredListaResultadoBusquedaAvanzada() {
        return filteredListaResultadoBusquedaAvanzada;
    }

    public void setFilteredListaResultadoBusquedaAvanzada(List<ResultadoBusquedaAvanzada> filteredCars) {
        this.filteredListaResultadoBusquedaAvanzada = filteredCars;
    }

    public String getNuevaColumna() {
        return nuevaColumna;
    }

    public void setNuevaColumna(String nuevaColumna) {
        this.nuevaColumna = nuevaColumna;
    }

    //GET-SET
    public VigenciasLocalizaciones getVigenciaLocalizacionBA() {
        return vigenciaLocalizacionBA;
    }

    public void setVigenciaLocalizacionBA(VigenciasLocalizaciones vigenciaLocalizacionBA) {
        this.vigenciaLocalizacionBA = vigenciaLocalizacionBA;
    }

    public VigenciasCargos getVigenciaCargoBA() {
        return vigenciaCargoBA;
    }

    public void setVigenciaCargoBA(VigenciasCargos vigenciaCargoBA) {
        this.vigenciaCargoBA = vigenciaCargoBA;
    }

    public List<Estructuras> getLovEstructuras() {
        if (lovEstructuras == null) {
            lovEstructuras = administrarVigenciaCargoBusquedaAvanzada.lovEstructura();
        }
        return lovEstructuras;
    }

    public void setLovEstructuras(List<Estructuras> lovEstructurasVigenciasCargos) {
        this.lovEstructuras = lovEstructurasVigenciasCargos;
    }

    public List<Estructuras> getFiltrarLovEstructuras() {
        return filtrarLovEstructuras;
    }

    public void setFiltrarLovEstructuras(List<Estructuras> filtrarLovEstructurasVigenciasCargos) {
        this.filtrarLovEstructuras = filtrarLovEstructurasVigenciasCargos;
    }

    public Estructuras getEstructuraSeleccionada() {
        return estructuraSeleccionada;
    }

    public void setEstructuraSeleccionada(Estructuras estructuraVigenciaCargoSeleccionada) {
        this.estructuraSeleccionada = estructuraVigenciaCargoSeleccionada;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public void setAceptar(boolean aceptar) {
        this.aceptar = aceptar;
    }

    public List<Cargos> getLovCargos() {
        if (lovCargos == null) {
            lovCargos = administrarVigenciaCargoBusquedaAvanzada.lovCargos();
        }
        return lovCargos;
    }

    public void setLovCargos(List<Cargos> lovCargosVigenciasCargos) {
        this.lovCargos = lovCargosVigenciasCargos;
    }

    public List<Cargos> getFiltrarLovCargos() {
        return filtrarLovCargos;
    }

    public void setFiltrarLovCargos(List<Cargos> filtrarLovCargosVigenciasCargos) {
        this.filtrarLovCargos = filtrarLovCargosVigenciasCargos;
    }

    public Cargos getCargoSeleccionado() {
        return cargoSeleccionado;
    }

    public void setCargoSeleccionado(Cargos cargoVigenciaCargoSeleccionado) {
        this.cargoSeleccionado = cargoVigenciaCargoSeleccionado;
    }

    public List<Papeles> getLovPapeles() {
        if (lovPapeles == null) {
            lovPapeles = administrarVigenciaCargoBusquedaAvanzada.lovPapeles();
        }
        return lovPapeles;
    }

    public void setLovPapeles(List<Papeles> lovPapelesVigenciasCargos) {
        this.lovPapeles = lovPapelesVigenciasCargos;
    }

    public List<Papeles> getFiltrarLovPapeles() {
        return filtrarLovPapeles;
    }

    public void setFiltrarLovPapeles(List<Papeles> filtrarLovPapelesVigenciasCargos) {
        this.filtrarLovPapeles = filtrarLovPapelesVigenciasCargos;
    }

    public Papeles getPapelSeleccionado() {
        return papelSeleccionado;
    }

    public void setPapelSeleccionado(Papeles papelVigenciaCargoSeleccionado) {
        this.papelSeleccionado = papelVigenciaCargoSeleccionado;
    }

    public List<MotivosCambiosCargos> getLovMotivosCambiosCargos() {
        if (lovMotivosCambiosCargos == null) {
            lovMotivosCambiosCargos = administrarVigenciaCargoBusquedaAvanzada.lovMotivosCambiosCargos();
        }
        return lovMotivosCambiosCargos;
    }

    public void setLovMotivosCambiosCargos(List<MotivosCambiosCargos> lovMotivosCambiosCargosVigenciasCargos) {
        this.lovMotivosCambiosCargos = lovMotivosCambiosCargosVigenciasCargos;
    }

    public List<MotivosCambiosCargos> getFiltrarLovMotivosCambiosCargos() {
        return filtrarLovMotivosCambiosCargos;
    }

    public void setFiltrarLovMotivosCambiosCargos(List<MotivosCambiosCargos> filtrarLovMotivosCambiosCargosVigenciasCargos) {
        this.filtrarLovMotivosCambiosCargos = filtrarLovMotivosCambiosCargosVigenciasCargos;
    }

    public MotivosCambiosCargos getMotivoCambioSeleccionado() {
        return motivoCambioSeleccionado;
    }

    public void setMotivoCambioSeleccionado(MotivosCambiosCargos motivoCambioVigenciaCargoSeleccionado) {
        this.motivoCambioSeleccionado = motivoCambioVigenciaCargoSeleccionado;
    }

    public List<Empleados> getLovEmpleados() {
        if (lovEmpleados == null) {
            lovEmpleados = administrarVigenciaCargoBusquedaAvanzada.lovEmpleados();
        }
        return lovEmpleados;
    }

    public void setLovEmpleados(List<Empleados> lovEmpleadosVigenciasCargos) {
        this.lovEmpleados = lovEmpleadosVigenciasCargos;
    }

    public List<Empleados> getFiltrarLovEmpleados() {
        return filtrarLovEmpleados;
    }

    public void setFiltrarLovEmpleados(List<Empleados> filtrarLovEmpleadosVigenciasCargos) {
        this.filtrarLovEmpleados = filtrarLovEmpleadosVigenciasCargos;
    }

    public Empleados getEmpleadoSeleccionado() {
        return empleadoSeleccionado;
    }

    public void setEmpleadoSeleccionado(Empleados empleadoVigenciaCargoSeleccionado) {
        this.empleadoSeleccionado = empleadoVigenciaCargoSeleccionado;
    }

    public List<MotivosLocalizaciones> getLovMotivosLocalizaciones() {
        if (lovMotivosLocalizaciones == null) {
            lovMotivosLocalizaciones = administrarVigenciaLocalizacion.motivosLocalizaciones();
        }
        return lovMotivosLocalizaciones;
    }

    public void setLovMotivosLocalizaciones(List<MotivosLocalizaciones> lovMotivosLocalizaciones) {
        this.lovMotivosLocalizaciones = lovMotivosLocalizaciones;
    }

    public List<MotivosLocalizaciones> getFiltrarLovMotivosLocalizaciones() {
        return filtrarLovMotivosLocalizaciones;
    }

    public void setFiltrarLovMotivosLocalizaciones(List<MotivosLocalizaciones> filtarLovMotivosLocalizaciones) {
        this.filtrarLovMotivosLocalizaciones = filtarLovMotivosLocalizaciones;
    }

    public MotivosLocalizaciones getMotivoLocalizacionSeleccionado() {
        return motivoLocalizacionSeleccionado;
    }

    public void setMotivoLocalizacionSeleccionado(MotivosLocalizaciones motivoLocalizacionSeleccionado) {
        this.motivoLocalizacionSeleccionado = motivoLocalizacionSeleccionado;
    }

    public Date getFechaInicialCargo() {
        return fechaInicialCargo;
    }

    public void setFechaInicialCargo(Date fechaInicialCargo) {
        this.fechaInicialCargo = fechaInicialCargo;
    }

    public Date getFechaFinalCargo() {
        return fechaFinalCargo;
    }

    public void setFechaFinalCargo(Date fechaFinalCargo) {
        this.fechaFinalCargo = fechaFinalCargo;
    }

    public boolean isActivoFechasCentroCosto() {
        return activoFechasCentroCosto;
    }

    public void setActivoFechasCentroCosto(boolean activoFechasCentroCosto) {
        this.activoFechasCentroCosto = activoFechasCentroCosto;
    }

    public boolean isActivoFechasCargos() {
        return activoFechasCargos;
    }

    public void setActivoFechasCargos(boolean activoFechasCargos) {
        this.activoFechasCargos = activoFechasCargos;
    }

    public int getTabActivaCentroCosto() {
        return tabActivaCentroCosto;
    }

    public void setTabActivaCentroCosto(int tabActivaCentroCosto) {

        this.tabActivaCentroCosto = tabActivaCentroCosto;
    }

    public int getTipoFechaCargo() {
        return tipoFechaCargo;
    }

    public void setTipoFechaCargo(int tipoFechaCargo) {
        this.tipoFechaCargo = tipoFechaCargo;
    }

    public int getTabActivaCargos() {
        return tabActivaCargos;
    }

    public void setTabActivaCargos(int tabActivaCargos) {
        this.tabActivaCargos = tabActivaCargos;
    }

    public int getTipoFechaCentroCosto() {
        return tipoFechaCentroCosto;
    }

    public void setTipoFechaCentroCosto(int tipoFechaCentroCosto) {
        this.tipoFechaCentroCosto = tipoFechaCentroCosto;
    }

    public Date getFechaInicialCentroCosto() {
        return fechaInicialCentroCosto;
    }

    public void setFechaInicialCentroCosto(Date fechaInicialCentroCosto) {
        this.fechaInicialCentroCosto = fechaInicialCentroCosto;
    }

    public Date getFechaFinalCentroCosto() {
        return fechaFinalCentroCosto;
    }

    public void setFechaFinalCentroCosto(Date fechaFinalCentroCosto) {
        this.fechaFinalCentroCosto = fechaFinalCentroCosto;
    }

    public VigenciasSueldos getVigenciaSueldoBA() {
        return vigenciaSueldoBA;
    }

    public void setVigenciaSueldoBA(VigenciasSueldos vigenciaSueldoBA) {
        this.vigenciaSueldoBA = vigenciaSueldoBA;
    }

    public int getTabActivaSueldo() {
        return tabActivaSueldo;
    }

    public void setTabActivaSueldo(int tabActivaSueldo) {
        this.tabActivaSueldo = tabActivaSueldo;
    }

    public Date getFechaInicialSueldo() {
        return fechaInicialSueldo;
    }

    public void setFechaInicialSueldo(Date fechaInicialSueldo) {
        this.fechaInicialSueldo = fechaInicialSueldo;
    }

    public Date getFechaFinalSueldo() {
        return fechaFinalSueldo;
    }

    public void setFechaFinalSueldo(Date fechaFinalSueldo) {
        this.fechaFinalSueldo = fechaFinalSueldo;
    }

    public List<TiposSueldos> getLovTiposSueldos() {
        if (lovTiposSueldos == null) {
            lovTiposSueldos = administrarVigenciaSueldo.tiposSueldos();
        }
        return lovTiposSueldos;
    }

    public void setLovTiposSueldos(List<TiposSueldos> lovTiposSueldos) {
        this.lovTiposSueldos = lovTiposSueldos;
    }

    public List<TiposSueldos> getFiltrarLovTiposSueldos() {
        return filtrarLovTiposSueldos;
    }

    public void setFiltrarLovTiposSueldos(List<TiposSueldos> filtrarLovTiposSueldos) {
        this.filtrarLovTiposSueldos = filtrarLovTiposSueldos;
    }

    public TiposSueldos getTipoSueldoSeleccionado() {
        return tipoSueldoSeleccionado;
    }

    public void setTipoSueldoSeleccionado(TiposSueldos tipoSueldoSeleccionado) {
        this.tipoSueldoSeleccionado = tipoSueldoSeleccionado;
    }

    public List<MotivosCambiosSueldos> getLovMotivosCambiosSueldos() {
        if (lovMotivosCambiosSueldos == null) {
            lovMotivosCambiosSueldos = administrarVigenciaSueldo.motivosCambiosSueldos();
        }
        return lovMotivosCambiosSueldos;
    }

    public void setLovMotivosCambiosSueldos(List<MotivosCambiosSueldos> lovMotivosCambiosSueldos) {
        this.lovMotivosCambiosSueldos = lovMotivosCambiosSueldos;
    }

    public List<MotivosCambiosSueldos> getFiltrarLovMotivosCambiosSueldos() {
        return filtrarLovMotivosCambiosSueldos;
    }

    public void setFiltrarLovMotivosCambiosSueldos(List<MotivosCambiosSueldos> filtrarLovMotivosCambiosSueldos) {
        this.filtrarLovMotivosCambiosSueldos = filtrarLovMotivosCambiosSueldos;
    }

    public MotivosCambiosSueldos getMotivoSueldoSeleccionado() {
        return motivoSueldoSeleccionado;
    }

    public void setMotivoSueldoSeleccionado(MotivosCambiosSueldos motivoSueldoSeleccionado) {
        this.motivoSueldoSeleccionado = motivoSueldoSeleccionado;
    }

    public boolean isActivoFechasSueldo() {
        return activoFechasSueldo;
    }

    public void setActivoFechasSueldo(boolean activoFechasSueldo) {
        this.activoFechasSueldo = activoFechasSueldo;
    }

    public int getTipoFechaSueldo() {
        return tipoFechaSueldo;
    }

    public void setTipoFechaSueldo(int tipoFechaSueldo) {
        this.tipoFechaSueldo = tipoFechaSueldo;
    }

    public BigDecimal getSueldoMaxSueldo() {
        return sueldoMaxSueldo;
    }

    public void setSueldoMaxSueldo(BigDecimal sueldoMaxSueldo) {
        this.sueldoMaxSueldo = sueldoMaxSueldo;
    }

    public BigDecimal getSueldoMinSueldo() {
        return sueldoMinSueldo;
    }

    public void setSueldoMinSueldo(BigDecimal sueldoMinSueldoM) {
        this.sueldoMinSueldo = sueldoMinSueldoM;
    }

    public VigenciasTiposContratos getVigenciaTipoContratoBA() {
        return vigenciaTipoContratoBA;
    }

    public void setVigenciaTipoContratoBA(VigenciasTiposContratos vigenciaTipoContratoBA) {
        this.vigenciaTipoContratoBA = vigenciaTipoContratoBA;
    }

    public int getTabActivaFechaContrato() {
        return tabActivaFechaContrato;
    }

    public void setTabActivaFechaContrato(int tabActivaFechaContrato) {
        this.tabActivaFechaContrato = tabActivaFechaContrato;
    }

    public Date getFechaInicialFechaContrato() {
        return fechaInicialFechaContrato;
    }

    public void setFechaInicialFechaContrato(Date fechaInicialFechaContrato) {
        this.fechaInicialFechaContrato = fechaInicialFechaContrato;
    }

    public Date getFechaFinalFechaContrato() {
        return fechaFinalFechaContrato;
    }

    public void setFechaFinalFechaContrato(Date fechaFinalFechaContrato) {
        this.fechaFinalFechaContrato = fechaFinalFechaContrato;
    }

    public int getTipoFechaFechaContrato() {
        return tipoFechaFechaContrato;
    }

    public void setTipoFechaFechaContrato(int tipoFechaFechaContrato) {
        this.tipoFechaFechaContrato = tipoFechaFechaContrato;
    }

    public List<TiposContratos> getLovTiposContratos() {
        if (lovTiposContratos == null) {
            lovTiposContratos = administrarVigenciaTipoContrato.tiposContratos();
        }
        return lovTiposContratos;
    }

    public void setLovTiposContratos(List<TiposContratos> lovTiposContratos) {
        this.lovTiposContratos = lovTiposContratos;
    }

    public List<TiposContratos> getFiltrarLovTiposContratos() {
        return filtrarLovTiposContratos;
    }

    public void setFiltrarLovTiposContratos(List<TiposContratos> filtrarLovTiposContratos) {
        this.filtrarLovTiposContratos = filtrarLovTiposContratos;
    }

    public TiposContratos getTipoContratoSeleccionado() {
        return tipoContratoSeleccionado;
    }

    public void setTipoContratoSeleccionado(TiposContratos tipoContratoSeleccionado) {
        this.tipoContratoSeleccionado = tipoContratoSeleccionado;
    }

    public List<MotivosContratos> getLovMotivosContratos() {
        if (lovMotivosContratos == null) {
            lovMotivosContratos = administrarVigenciaTipoContrato.motivosContratos();
        }
        return lovMotivosContratos;
    }

    public void setLovMotivosContratos(List<MotivosContratos> lovMotivosContratos) {
        this.lovMotivosContratos = lovMotivosContratos;
    }

    public MotivosContratos getMotivoContratoSeleccionado() {
        return motivoContratoSeleccionado;
    }

    public void setMotivoContratoSeleccionado(MotivosContratos motivoContratoSeleccionado) {
        this.motivoContratoSeleccionado = motivoContratoSeleccionado;
    }

    public boolean isActivoFechasFechaContrato() {
        return activoFechasFechaContrato;
    }

    public void setActivoFechasFechaContrato(boolean activoFechasFechaContrato) {
        this.activoFechasFechaContrato = activoFechasFechaContrato;
    }

    public List<MotivosContratos> getFiltrarLovMotivosContratos() {
        return filtrarLovMotivosContratos;
    }

    public void setFiltrarLovMotivosContratos(List<MotivosContratos> filtrarLovMotivosContratos) {
        this.filtrarLovMotivosContratos = filtrarLovMotivosContratos;
    }

    public VigenciasTiposTrabajadores getVigenciaTipoTrabajadorBA() {
        return vigenciaTipoTrabajadorBA;
    }

    public void setVigenciaTipoTrabajadorBA(VigenciasTiposTrabajadores vigenciaTipoTrabajadorBA) {
        this.vigenciaTipoTrabajadorBA = vigenciaTipoTrabajadorBA;
    }

    public int getTabActivaTipoTrabajador() {
        return tabActivaTipoTrabajador;
    }

    public void setTabActivaTipoTrabajador(int tabActivaTipoTrabajador) {
        this.tabActivaTipoTrabajador = tabActivaTipoTrabajador;
    }

    public TiposTrabajadores getTipoTrabajadorSeleccionado() {
        return tipoTrabajadorSeleccionado;
    }

    public void setTipoTrabajadorSeleccionado(TiposTrabajadores tipoTrabajadorSeleccionado) {
        this.tipoTrabajadorSeleccionado = tipoTrabajadorSeleccionado;
    }

    public boolean isActivoFechasTipoTrabajador() {
        return activoFechasTipoTrabajador;
    }

    public void setActivoFechasTipoTrabajador(boolean activoFechasTipoTrabajador) {
        this.activoFechasTipoTrabajador = activoFechasTipoTrabajador;
    }

    public Date getFechaInicialTipoTrabajador() {
        return fechaInicialTipoTrabajador;
    }

    public void setFechaInicialTipoTrabajador(Date fechaInicialTipoTrabajador) {
        this.fechaInicialTipoTrabajador = fechaInicialTipoTrabajador;
    }

    public Date getFechaFinalTipoTrabajador() {
        return fechaFinalTipoTrabajador;
    }

    public void setFechaFinalTipoTrabajador(Date fechaFinalTipoTrabajador) {
        this.fechaFinalTipoTrabajador = fechaFinalTipoTrabajador;
    }

    public int getTipoFechaTipoTrabajador() {
        return tipoFechaTipoTrabajador;
    }

    public void setTipoFechaTipoTrabajador(int tipoFechaTipoTrabajador) {
        this.tipoFechaTipoTrabajador = tipoFechaTipoTrabajador;
    }

    public List<TiposTrabajadores> getLovTiposTrabajadores() {
        if (lovTiposTrabajadores == null) {
            lovTiposTrabajadores = administrarVigenciasTiposTrabajadores.tiposTrabajadores();
        }
        return lovTiposTrabajadores;
    }

    public void setLovTiposTrabajadores(List<TiposTrabajadores> lovTiposTrabajadores) {
        this.lovTiposTrabajadores = lovTiposTrabajadores;
    }

    public List<TiposTrabajadores> getFiltrarLovTiposTrabajadores() {
        return filtrarLovTiposTrabajadores;
    }

    public void setFiltrarLovTiposTrabajadores(List<TiposTrabajadores> filtrarLovTiposTrabajadores) {
        this.filtrarLovTiposTrabajadores = filtrarLovTiposTrabajadores;
    }

    public VigenciasReformasLaborales getVigenciaReformaLaboralBA() {
        return vigenciaReformaLaboralBA;
    }

    public void setVigenciaReformaLaboralBA(VigenciasReformasLaborales vigenciaReformaLaboralBA) {
        this.vigenciaReformaLaboralBA = vigenciaReformaLaboralBA;
    }

    public int getTabActivaTipoSalario() {
        return tabActivaTipoSalario;
    }

    public void setTabActivaTipoSalario(int tabActivaTipoSalario) {
        this.tabActivaTipoSalario = tabActivaTipoSalario;
    }

    public Date getFechaInicialTipoSalario() {
        return fechaInicialTipoSalario;
    }

    public void setFechaInicialTipoSalario(Date fechaInicialTipoSalario) {
        this.fechaInicialTipoSalario = fechaInicialTipoSalario;
    }

    public Date getFechaFinalTipoSalario() {
        return fechaFinalTipoSalario;
    }

    public void setFechaFinalTipoSalario(Date fechaFinalTipoSalario) {
        this.fechaFinalTipoSalario = fechaFinalTipoSalario;
    }

    public int getTipoFechaTipoSalario() {
        return tipoFechaTipoSalario;
    }

    public void setTipoFechaTipoSalario(int tipoFechaTipoSalario) {
        this.tipoFechaTipoSalario = tipoFechaTipoSalario;
    }

    public List<ReformasLaborales> getLovReformasLaborales() {
        if (lovReformasLaborales == null) {
            lovReformasLaborales = administrarVigenciaReformaLaboral.reformasLaborales();
        }
        return lovReformasLaborales;
    }

    public void setLovReformasLaborales(List<ReformasLaborales> lovReformasLaborales) {
        this.lovReformasLaborales = lovReformasLaborales;
    }

    public List<ReformasLaborales> getFiltrarLovReformasLaborales() {
        return filtrarLovReformasLaborales;
    }

    public void setFiltrarLovReformasLaborales(List<ReformasLaborales> filtrarLovReformasLaborales) {
        this.filtrarLovReformasLaborales = filtrarLovReformasLaborales;
    }

    public ReformasLaborales getReformaLaboralSeleccionada() {
        return reformaLaboralSeleccionada;
    }

    public void setReformaLaboralSeleccionada(ReformasLaborales reformaLaboralSeleccionada) {
        this.reformaLaboralSeleccionada = reformaLaboralSeleccionada;
    }

    public boolean isActivoFechasTipoSalario() {
        return activoFechasTipoSalario;
    }

    public void setActivoFechasTipoSalario(boolean activoFechasTipoSalario) {
        this.activoFechasTipoSalario = activoFechasTipoSalario;
    }

    public VigenciasNormasEmpleados getVigenciaNormaEmpleadoBA() {
        return vigenciaNormaEmpleadoBA;
    }

    public void setVigenciaNormaEmpleadoBA(VigenciasNormasEmpleados vigenciaNormaEmpleadoBA) {
        this.vigenciaNormaEmpleadoBA = vigenciaNormaEmpleadoBA;
    }

    public int getTabActivaNormaLaboral() {
        return tabActivaNormaLaboral;
    }

    public void setTabActivaNormaLaboral(int tabActivaNormaLaboral) {
        this.tabActivaNormaLaboral = tabActivaNormaLaboral;
    }

    public Date getFechaInicialNormaLaboral() {
        return fechaInicialNormaLaboral;
    }

    public void setFechaInicialNormaLaboral(Date fechaInicialNormaLaboral) {
        this.fechaInicialNormaLaboral = fechaInicialNormaLaboral;
    }

    public Date getFechaFinalNormaLaboral() {
        return fechaFinalNormaLaboral;
    }

    public void setFechaFinalNormaLaboral(Date fechaFinalNormaLaboral) {
        this.fechaFinalNormaLaboral = fechaFinalNormaLaboral;
    }

    public int getTipoFechaNormaLaboral() {
        return tipoFechaNormaLaboral;
    }

    public void setTipoFechaNormaLaboral(int tipoFechaNormaLaboral) {
        this.tipoFechaNormaLaboral = tipoFechaNormaLaboral;
    }

    public List<NormasLaborales> getLovNormasLaborales() {
        if (lovNormasLaborales == null) {
            lovNormasLaborales = administrarVigenciaNormaEmpleado.lovNormasLaborales();
        }
        return lovNormasLaborales;
    }

    public void setLovNormasLaborales(List<NormasLaborales> lovNormasLaborales) {
        this.lovNormasLaborales = lovNormasLaborales;
    }

    public List<NormasLaborales> getFiltrarLovNormasLaborales() {
        return filtrarLovNormasLaborales;
    }

    public void setFiltrarLovNormasLaborales(List<NormasLaborales> filtrarLovNormasLaborales) {
        this.filtrarLovNormasLaborales = filtrarLovNormasLaborales;
    }

    public NormasLaborales getNormaLaboralSeleccionada() {
        return normaLaboralSeleccionada;
    }

    public void setNormaLaboralSeleccionada(NormasLaborales normaLaboralSeleccionada) {
        this.normaLaboralSeleccionada = normaLaboralSeleccionada;
    }

    public boolean isActivoFechasNormaLaboral() {
        return activoFechasNormaLaboral;
    }

    public void setActivoFechasNormaLaboral(boolean activoFechasNormaLaboral) {
        this.activoFechasNormaLaboral = activoFechasNormaLaboral;
    }

    public VigenciasContratos getVigenciaContratoBA() {
        return vigenciaContratoBA;
    }

    public void setVigenciaContratoBA(VigenciasContratos vigenciaContratoBA) {
        this.vigenciaContratoBA = vigenciaContratoBA;
    }

    public int getTabActivaLegislacionLaboral() {
        return tabActivaLegislacionLaboral;
    }

    public void setTabActivaLegislacionLaboral(int tabActivaLegislacionLaboral) {
        this.tabActivaLegislacionLaboral = tabActivaLegislacionLaboral;
    }

    public Date getFechaMIInicialLegislacionLaboral() {
        return fechaMIInicialLegislacionLaboral;
    }

    public void setFechaMIInicialLegislacionLaboral(Date fechaMIInicialLegislacionLaboral) {
        this.fechaMIInicialLegislacionLaboral = fechaMIInicialLegislacionLaboral;
    }

    public Date getFechaMIFinalLegislacionLaboral() {
        return fechaMIFinalLegislacionLaboral;
    }

    public void setFechaMIFinalLegislacionLaboral(Date fechaMIFinalLegislacionLaboral) {
        this.fechaMIFinalLegislacionLaboral = fechaMIFinalLegislacionLaboral;
    }

    public Date getFechaMFInicialLegislacionLaboral() {
        return fechaMFInicialLegislacionLaboral;
    }

    public void setFechaMFInicialLegislacionLaboral(Date fechaMFInicialLegislacionLaboral) {
        this.fechaMFInicialLegislacionLaboral = fechaMFInicialLegislacionLaboral;
    }

    public Date getFechaMFFinalLegislacionLaboral() {
        return fechaMFFinalLegislacionLaboral;
    }

    public void setFechaMFFinalLegislacionLaboral(Date fechaMFFinalLegislacionLaboral) {
        this.fechaMFFinalLegislacionLaboral = fechaMFFinalLegislacionLaboral;
    }

    public int getTipoFechaLegislacionLaboral() {
        return tipoFechaLegislacionLaboral;
    }

    public void setTipoFechaLegislacionLaboral(int tipoFechaLegislacionLaboral) {
        this.tipoFechaLegislacionLaboral = tipoFechaLegislacionLaboral;
    }

    public List<Contratos> getLovContratos() {
        if (lovContratos == null) {
            lovContratos = administrarVigenciaContrato.contratos();
        }
        return lovContratos;
    }

    public void setLovContratos(List<Contratos> lovContratos) {
        this.lovContratos = lovContratos;
    }

    public List<Contratos> getFiltrarLovContratos() {
        return filtrarLovContratos;
    }

    public void setFiltrarLovContratos(List<Contratos> filtrarLovContratos) {
        this.filtrarLovContratos = filtrarLovContratos;
    }

    public Contratos getContratoSeleccionado() {
        return contratoSeleccionado;
    }

    public void setContratoSeleccionado(Contratos contratoSeleccionado) {
        this.contratoSeleccionado = contratoSeleccionado;
    }

    public boolean isActivoFechasLegislacionLaboral() {
        return activoFechasLegislacionLaboral;
    }

    public void setActivoFechasLegislacionLaboral(boolean activoFechasLegislacionLaboral) {
        this.activoFechasLegislacionLaboral = activoFechasLegislacionLaboral;
    }

    public VigenciasUbicaciones getVigenciaUbicacionBA() {
        return vigenciaUbicacionBA;
    }

    public void setVigenciaUbicacionBA(VigenciasUbicaciones vigenciaUbicacionBA) {
        this.vigenciaUbicacionBA = vigenciaUbicacionBA;
    }

    public int getTabActivaUbicacion() {
        return tabActivaUbicacion;
    }

    public void setTabActivaUbicacion(int tabActivaUbicacion) {
        this.tabActivaUbicacion = tabActivaUbicacion;
    }

    public Date getFechaInicialUbicacion() {
        return fechaInicialUbicacion;
    }

    public void setFechaInicialUbicacion(Date fechaInicialUbicacion) {
        this.fechaInicialUbicacion = fechaInicialUbicacion;
    }

    public Date getFechaFinalUbicacion() {
        return fechaFinalUbicacion;
    }

    public void setFechaFinalUbicacion(Date fechaFinalUbicacion) {
        this.fechaFinalUbicacion = fechaFinalUbicacion;
    }

    public int getTipoFechaUbicacion() {
        return tipoFechaUbicacion;
    }

    public void setTipoFechaUbicacion(int tipoFechaUbicacion) {
        this.tipoFechaUbicacion = tipoFechaUbicacion;
    }

    public List<UbicacionesGeograficas> getLovUbicacionesGeograficas() {
        if (lovUbicacionesGeograficas == null) {
            lovUbicacionesGeograficas = administrarVigenciasUbicaciones.ubicacionesGeograficas();
        }
        return lovUbicacionesGeograficas;
    }

    public void setLovUbicacionesGeograficas(List<UbicacionesGeograficas> lovUbicacionesGeograficas) {
        this.lovUbicacionesGeograficas = lovUbicacionesGeograficas;
    }

    public List<UbicacionesGeograficas> getFiltrarLovUbicacionesGeograficas() {
        return filtrarLovUbicacionesGeograficas;
    }

    public void setFiltrarLovUbicacionesGeograficas(List<UbicacionesGeograficas> filtrarLovUbicacionesGeograficas) {
        this.filtrarLovUbicacionesGeograficas = filtrarLovUbicacionesGeograficas;
    }

    public UbicacionesGeograficas getUbicacionGeograficaSeleccionada() {
        return ubicacionGeograficaSeleccionada;
    }

    public void setUbicacionGeograficaSeleccionada(UbicacionesGeograficas ubicacionGeograficaSeleccionada) {
        this.ubicacionGeograficaSeleccionada = ubicacionGeograficaSeleccionada;
    }

    public boolean isActivoFechasUbicacion() {
        return activoFechasUbicacion;
    }

    public void setActivoFechasUbicacion(boolean activoFechasUbicacion) {
        this.activoFechasUbicacion = activoFechasUbicacion;
    }

    public VigenciasAfiliaciones getVigenciaAfiliacionBA() {
        return vigenciaAfiliacionBA;
    }

    public void setVigenciaAfiliacionBA(VigenciasAfiliaciones vigenciaAfiliacionBA) {
        this.vigenciaAfiliacionBA = vigenciaAfiliacionBA;
    }

    public int getTabActivaAfiliacion() {
        return tabActivaAfiliacion;
    }

    public void setTabActivaAfiliacion(int tabActivaAfiliacion) {
        this.tabActivaAfiliacion = tabActivaAfiliacion;
    }

    public Date getFechaInicialAfiliacion() {
        return fechaInicialAfiliacion;
    }

    public void setFechaInicialAfiliacion(Date fechaInicialAfiliacion) {
        this.fechaInicialAfiliacion = fechaInicialAfiliacion;
    }

    public Date getFechaFinalAfiliacion() {
        return fechaFinalAfiliacion;
    }

    public void setFechaFinalAfiliacion(Date fechaFinalAfiliacion) {
        this.fechaFinalAfiliacion = fechaFinalAfiliacion;
    }

    public int getTipoFechaAfiliacion() {
        return tipoFechaAfiliacion;
    }

    public void setTipoFechaAfiliacion(int tipoFechaAfiliacion) {
        this.tipoFechaAfiliacion = tipoFechaAfiliacion;
    }

    public List<TercerosSucursales> getLovTercerosSucursales() {
        if (lovTercerosSucursales == null) {
            lovTercerosSucursales = administrarVigenciaAfiliacion3.listTercerosSucursales();
        }
        return lovTercerosSucursales;
    }

    public void setLovTercerosSucursales(List<TercerosSucursales> lovTercerosSucursales) {
        this.lovTercerosSucursales = lovTercerosSucursales;
    }

    public List<TercerosSucursales> getFiltrarLovTercerosSucursales() {
        return filtrarLovTercerosSucursales;
    }

    public void setFiltrarLovTercerosSucursales(List<TercerosSucursales> filtrarLovTercerosSucursales) {
        this.filtrarLovTercerosSucursales = filtrarLovTercerosSucursales;
    }

    public TercerosSucursales getTerceroSucursalSeleccionado() {
        return terceroSucursalSeleccionado;
    }

    public void setTerceroSucursalSeleccionado(TercerosSucursales terceroSucursalSeleccionado) {
        this.terceroSucursalSeleccionado = terceroSucursalSeleccionado;
    }

    public List<TiposEntidades> getLovTiposEntidades() {
        if (lovTiposEntidades == null) {
            lovTiposEntidades = administrarVigenciaAfiliacion3.listTiposEntidades();
        }
        return lovTiposEntidades;
    }

    public void setLovTiposEntidades(List<TiposEntidades> lovTiposEntidades) {
        this.lovTiposEntidades = lovTiposEntidades;
    }

    public List<TiposEntidades> getFiltrarLovTiposEntidades() {
        return filtrarLovTiposEntidades;
    }

    public void setFiltrarLovTiposEntidades(List<TiposEntidades> filtrarLovTiposEntidades) {
        this.filtrarLovTiposEntidades = filtrarLovTiposEntidades;
    }

    public TiposEntidades getTipoEntidadSeleccionado() {
        return tipoEntidadSeleccionado;
    }

    public void setTipoEntidadSeleccionado(TiposEntidades tipoEntidadSeleccionado) {
        this.tipoEntidadSeleccionado = tipoEntidadSeleccionado;
    }

    public boolean isActivoFechasAfiliacion() {
        return activoFechasAfiliacion;
    }

    public void setActivoFechasAfiliacion(boolean activoFechasAfiliacion) {
        this.activoFechasAfiliacion = activoFechasAfiliacion;
    }

    public List<EstadosAfiliaciones> getLovEstadosAfiliaciones() {
        if (lovEstadosAfiliaciones == null) {
            lovEstadosAfiliaciones = administrarVigenciaAfiliacion3.listEstadosAfiliaciones();
        }
        return lovEstadosAfiliaciones;
    }

    public void setLovEstadosAfiliaciones(List<EstadosAfiliaciones> lovEstadosAfiliaciones) {
        this.lovEstadosAfiliaciones = lovEstadosAfiliaciones;
    }

    public List<EstadosAfiliaciones> getFiltrarLovEstadosAfiliaciones() {
        return filtrarLovEstadosAfiliaciones;
    }

    public void setFiltrarLovEstadosAfiliaciones(List<EstadosAfiliaciones> filtrarLovEstadosAfiliaciones) {
        this.filtrarLovEstadosAfiliaciones = filtrarLovEstadosAfiliaciones;
    }

    public EstadosAfiliaciones getEstadoAfiliacionSeleccionado() {
        return estadoAfiliacionSeleccionado;
    }

    public void setEstadoAfiliacionSeleccionado(EstadosAfiliaciones estadoAfiliacionSeleccionado) {
        this.estadoAfiliacionSeleccionado = estadoAfiliacionSeleccionado;
    }

    public VigenciasFormasPagos getVigenciaFormaPagoBA() {
        return vigenciaFormaPagoBA;
    }

    public void setVigenciaFormaPagoBA(VigenciasFormasPagos vigenciaFormaPagoBA) {
        this.vigenciaFormaPagoBA = vigenciaFormaPagoBA;
    }

    public int getTabActivaFormaPago() {
        return tabActivaFormaPago;
    }

    public void setTabActivaFormaPago(int tabActivaFormaPago) {
        this.tabActivaFormaPago = tabActivaFormaPago;
    }

    public Date getFechaInicialFormaPago() {
        return fechaInicialFormaPago;
    }

    public void setFechaInicialFormaPago(Date fechaInicialFormaPago) {
        this.fechaInicialFormaPago = fechaInicialFormaPago;
    }

    public Date getFechaFinalFormaPago() {
        return fechaFinalFormaPago;
    }

    public void setFechaFinalFormaPago(Date fechaFinalFormaPago) {
        this.fechaFinalFormaPago = fechaFinalFormaPago;
    }

    public int getTipoFechaFormaPago() {
        return tipoFechaFormaPago;
    }

    public void setTipoFechaFormaPago(int tipoFechaFormaPago) {
        this.tipoFechaFormaPago = tipoFechaFormaPago;
    }

    public List<Periodicidades> getLovPeriodicidades() {
        if (lovPeriodicidades == null) {
            lovPeriodicidades = administrarEmplVigenciaFormaPago.consultarLOVPerdiocidades();
        }
        return lovPeriodicidades;
    }

    public void setLovPeriodicidades(List<Periodicidades> lovPeriodicidades) {
        this.lovPeriodicidades = lovPeriodicidades;
    }

    public List<Periodicidades> getFiltrarLovPeriodicidades() {
        return filtrarLovPeriodicidades;
    }

    public void setFiltrarLovPeriodicidades(List<Periodicidades> filtrarLovPeriodicidades) {
        this.filtrarLovPeriodicidades = filtrarLovPeriodicidades;
    }

    public Periodicidades getPeriodicidadSeleccionada() {
        return periodicidadSeleccionada;
    }

    public void setPeriodicidadSeleccionada(Periodicidades periodicidadSeleccionada) {
        this.periodicidadSeleccionada = periodicidadSeleccionada;
    }

    public List<Sucursales> getLovSucursales() {
        if (lovSucursales == null) {
            lovSucursales = administrarEmplVigenciaFormaPago.consultarLOVSucursales();
        }
        return lovSucursales;
    }

    public void setLovSucursales(List<Sucursales> lovSucursales) {
        this.lovSucursales = lovSucursales;
    }

    public List<Sucursales> getFiltrarLovSucursales() {
        return filtrarLovSucursales;
    }

    public void setFiltrarLovSucursales(List<Sucursales> filtrarLovSucursales) {
        this.filtrarLovSucursales = filtrarLovSucursales;
    }

    public Sucursales getSucursalSeleccionada() {
        return sucursalSeleccionada;
    }

    public void setSucursalSeleccionada(Sucursales sucursalSeleccionada) {
        this.sucursalSeleccionada = sucursalSeleccionada;
    }

    public boolean isActivoFechasFormaPago() {
        return activoFechasFormaPago;
    }

    public void setActivoFechasFormaPago(boolean activoFechasFormaPago) {
        this.activoFechasFormaPago = activoFechasFormaPago;
    }

    public Mvrs getMvrsBA() {
        return mvrsBA;
    }

    public void setMvrsBA(Mvrs mvrsBA) {
        this.mvrsBA = mvrsBA;
    }

    public int getTabActivaMvrs() {
        return tabActivaMvrs;
    }

    public void setTabActivaMvrs(int tabActivaMvrs) {
        this.tabActivaMvrs = tabActivaMvrs;
    }

    public Date getFechaInicialMvrs() {
        return fechaInicialMvrs;
    }

    public void setFechaInicialMvrs(Date fechaInicialMvrs) {
        this.fechaInicialMvrs = fechaInicialMvrs;
    }

    public Date getFechaFinalMvrs() {
        return fechaFinalMvrs;
    }

    public void setFechaFinalMvrs(Date fechaFinalMvrs) {
        this.fechaFinalMvrs = fechaFinalMvrs;
    }

    public int getTipoFechaMvrs() {
        return tipoFechaMvrs;
    }

    public void setTipoFechaMvrs(int tipoFechaMvrs) {
        this.tipoFechaMvrs = tipoFechaMvrs;
    }

    public List<Motivosmvrs> getLovMotivosMvrs() {
        if (lovMotivosMvrs == null) {
            lovMotivosMvrs = administrarEmplMvrs.listMotivos();
        }
        return lovMotivosMvrs;
    }

    public void setLovMotivosMvrs(List<Motivosmvrs> lovMotivosMvrs) {
        this.lovMotivosMvrs = lovMotivosMvrs;
    }

    public List<Motivosmvrs> getFiltrarLovMotivosMvrs() {
        return filtrarLovMotivosMvrs;
    }

    public void setFiltrarLovMotivosMvrs(List<Motivosmvrs> filtrarLovMotivosMvrs) {
        this.filtrarLovMotivosMvrs = filtrarLovMotivosMvrs;
    }

    public Motivosmvrs getMotivoMvrsSeleccionado() {
        return motivoMvrsSeleccionado;
    }

    public void setMotivoMvrsSeleccionado(Motivosmvrs motivoMvrsSeleccionado) {
        this.motivoMvrsSeleccionado = motivoMvrsSeleccionado;
    }

    public boolean isActivoFechasMvrs() {
        return activoFechasMvrs;
    }

    public void setActivoFechasMvrs(boolean activoFechasMvrs) {
        this.activoFechasMvrs = activoFechasMvrs;
    }

    public BigDecimal getSueldoMaxMvrs() {
        return sueldoMaxMvrs;
    }

    public void setSueldoMaxMvrs(BigDecimal sueldoMaxMvrs) {
        this.sueldoMaxMvrs = sueldoMaxMvrs;
    }

    public BigDecimal getSueldoMinMvrs() {
        return sueldoMinMvrs;
    }

    public void setSueldoMinMvrs(BigDecimal sueldoMinMvrs) {
        this.sueldoMinMvrs = sueldoMinMvrs;
    }

    public int getTabActivaSets() {
        return tabActivaSets;
    }

    public void setTabActivaSets(int tabActivaSets) {
        this.tabActivaSets = tabActivaSets;
    }

    public Date getFechaMIInicialSets() {
        return fechaMIInicialSets;
    }

    public void setFechaMIInicialSets(Date fechaMIInicialSets) {
        this.fechaMIInicialSets = fechaMIInicialSets;
    }

    public Date getFechaMIFinalSets() {
        return fechaMIFinalSets;
    }

    public void setFechaMIFinalSets(Date fechaMIFinalSets) {
        this.fechaMIFinalSets = fechaMIFinalSets;
    }

    public Date getFechaMFInicialSets() {
        return fechaMFInicialSets;
    }

    public void setFechaMFInicialSets(Date fechaMFInicialSets) {
        this.fechaMFInicialSets = fechaMFInicialSets;
    }

    public Date getFechaMFFinalSets() {
        return fechaMFFinalSets;
    }

    public void setFechaMFFinalSets(Date fechaMFFinalSets) {
        this.fechaMFFinalSets = fechaMFFinalSets;
    }

    public int getTipoFechaSets() {
        return tipoFechaSets;
    }

    public void setTipoFechaSets(int tipoFechaSets) {
        this.tipoFechaSets = tipoFechaSets;
    }

    public boolean isActivoFechasSets() {
        return activoFechasSets;
    }

    public void setActivoFechasSets(boolean activoFechasSets) {
        this.activoFechasSets = activoFechasSets;
    }

    public BigDecimal getPromedioMinimoSets() {
        return promedioMinimoSets;
    }

    public void setPromedioMinimoSets(BigDecimal promedioMinimoSets) {
        this.promedioMinimoSets = promedioMinimoSets;
    }

    public BigDecimal getPromedioMaximoSets() {
        return promedioMaximoSets;
    }

    public void setPromedioMaximoSets(BigDecimal promedioMaximoSets) {
        this.promedioMaximoSets = promedioMaximoSets;
    }

    public String getTipoMetodoSets() {
        return tipoMetodoSets;
    }

    public void setTipoMetodoSets(String tipoMetodoSets) {
        this.tipoMetodoSets = tipoMetodoSets;
    }

    public int getTabActivaVacacion() {
        return tabActivaVacacion;
    }

    public void setTabActivaVacacion(int tabActivaVacacion) {
        this.tabActivaVacacion = tabActivaVacacion;
    }

    public Date getFechaMIInicialVacacion() {
        return fechaMIInicialVacacion;
    }

    public void setFechaMIInicialVacacion(Date fechaMIInicialVacacion) {
        this.fechaMIInicialVacacion = fechaMIInicialVacacion;
    }

    public Date getFechaMIFinalVacacion() {
        return fechaMIFinalVacacion;
    }

    public void setFechaMIFinalVacacion(Date fechaMIFinalVacacion) {
        this.fechaMIFinalVacacion = fechaMIFinalVacacion;
    }

    public Date getFechaMFInicialVacacion() {
        return fechaMFInicialVacacion;
    }

    public void setFechaMFInicialVacacion(Date fechaMFInicialVacacion) {
        this.fechaMFInicialVacacion = fechaMFInicialVacacion;
    }

    public Date getFechaMFFinalVacacion() {
        return fechaMFFinalVacacion;
    }

    public void setFechaMFFinalVacacion(Date fechaMFFinalVacacion) {
        this.fechaMFFinalVacacion = fechaMFFinalVacacion;
    }

    public VigenciasJornadas getVigenciaJornadaBA() {
        return vigenciaJornadaBA;
    }

    public void setVigenciaJornadaBA(VigenciasJornadas vigenciaJornadaBA) {
        this.vigenciaJornadaBA = vigenciaJornadaBA;
    }

    public int getTabActivaJornadaLaboral() {
        return tabActivaJornadaLaboral;
    }

    public void setTabActivaJornadaLaboral(int tabActivaJornadaLaboral) {
        this.tabActivaJornadaLaboral = tabActivaJornadaLaboral;
    }

    public Date getFechaInicialJornadaLaboral() {
        return fechaInicialJornadaLaboral;
    }

    public void setFechaInicialJornadaLaboral(Date fechaInicialJornadaLaboral) {
        this.fechaInicialJornadaLaboral = fechaInicialJornadaLaboral;
    }

    public Date getFechaFinalJornadaLaboral() {
        return fechaFinalJornadaLaboral;
    }

    public void setFechaFinalJornadaLaboral(Date fechaFinalJornadaLaboral) {
        this.fechaFinalJornadaLaboral = fechaFinalJornadaLaboral;
    }

    public List<JornadasLaborales> getLovJornadasLaborales() {
        if (lovJornadasLaborales == null) {
            lovJornadasLaborales = administrarVigenciaJornada.jornadasLaborales();
        }
        return lovJornadasLaborales;
    }

    public void setLovJornadasLaborales(List<JornadasLaborales> lovJornadasLaborales) {
        this.lovJornadasLaborales = lovJornadasLaborales;
    }

    public List<JornadasLaborales> getFiltrarLovJornadasLaborales() {
        return filtrarLovJornadasLaborales;
    }

    public void setFiltrarLovJornadasLaborales(List<JornadasLaborales> filtrarLovJornadasLaborales) {
        this.filtrarLovJornadasLaborales = filtrarLovJornadasLaborales;
    }

    public JornadasLaborales getJornadaLaboralSeleccionada() {
        return jornadaLaboralSeleccionada;
    }

    public void setJornadaLaboralSeleccionada(JornadasLaborales jornadaLaboralSeleccionada) {
        this.jornadaLaboralSeleccionada = jornadaLaboralSeleccionada;
    }

    public MotivosRetiros getMotivoRetiroBA() {
        return motivoRetiroBA;
    }

    public void setMotivoRetiroBA(MotivosRetiros motivoRetiroBA) {
        this.motivoRetiroBA = motivoRetiroBA;
    }

    public int getTabActivaFechaRetiro() {
        return tabActivaFechaRetiro;
    }

    public void setTabActivaFechaRetiro(int tabActivaFechaRetiro) {
        this.tabActivaFechaRetiro = tabActivaFechaRetiro;
    }

    public Date getFechaInicialFechaRetiro() {
        return fechaInicialFechaRetiro;
    }

    public void setFechaInicialFechaRetiro(Date fechaInicialFechaRetiro) {
        this.fechaInicialFechaRetiro = fechaInicialFechaRetiro;
    }

    public Date getFechaFinalFechaRetiro() {
        return fechaFinalFechaRetiro;
    }

    public void setFechaFinalFechaRetiro(Date fechaFinalFechaRetiro) {
        this.fechaFinalFechaRetiro = fechaFinalFechaRetiro;
    }

    public List<MotivosRetiros> getLovMotivosRetiros() {
        if (lovMotivosRetiros == null) {
            lovMotivosRetiros = administrarVigenciasTiposTrabajadores.motivosRetiros();
        }
        return lovMotivosRetiros;
    }

    public void setLovMotivosRetiros(List<MotivosRetiros> lovMotivosRetiros) {
        this.lovMotivosRetiros = lovMotivosRetiros;
    }

    public List<MotivosRetiros> getFiltrarLovMotivosRetiros() {
        return filtrarLovMotivosRetiros;
    }

    public void setFiltrarLovMotivosRetiros(List<MotivosRetiros> filtrarLovMotivosRetiros) {
        this.filtrarLovMotivosRetiros = filtrarLovMotivosRetiros;
    }

    public MotivosRetiros getMotivoRetiroSeleccionado() {
        return motivoRetiroSeleccionado;
    }

    public void setMotivoRetiroSeleccionado(MotivosRetiros motivoRetiroSeleccionado) {
        this.motivoRetiroSeleccionado = motivoRetiroSeleccionado;
    }

    public List<ColumnasEscenarios> getListaColumnasEscenarios() {
        return this.listaColumnasEscenarios;
    }

    public void setListaColumnasEscenarios(List<ColumnasEscenarios> setListColumnasEscenarios) {
        this.listaColumnasEscenarios = setListColumnasEscenarios;
    }

    public List<QVWEmpleadosCorte> getRegistros() {
        return this.registros;
    }

    public void setRegistros(List<QVWEmpleadosCorte> setListColumnasEscenarios) {
        this.registros = setListColumnasEscenarios;
    }

    public List<QVWEmpleadosCorte> getRegistrosFiltrado() {
        return this.registrosFiltrado;
    }

    public void setRegistrosFiltrado(List<QVWEmpleadosCorte> setListColumnasEscenarios) {
        this.registrosFiltrado = setListColumnasEscenarios;
    }

    public List<ColumnasBusquedaAvanzada> getFiltrarListaColumnasBusquedaAvanzada() {
        return filtrarListaColumnasBusquedaAvanzada;
    }

    public void setFiltrarListaColumnasBusquedaAvanzada(List<ColumnasBusquedaAvanzada> filtrarListaColumnasBusquedaAvanzada) {
        this.filtrarListaColumnasBusquedaAvanzada = filtrarListaColumnasBusquedaAvanzada;
    }

    public List<ColumnasBusquedaAvanzada> getListaColumnasBusquedaAvanzada() {
        return listaColumnasBusquedaAvanzada;
    }

    public void setListaColumnasBusquedaAvanzada(List<ColumnasBusquedaAvanzada> listaColumnasBusquedaAvanzada) {
        this.listaColumnasBusquedaAvanzada = listaColumnasBusquedaAvanzada;
    }

    public String getCabeceraEditarCelda() {
        return cabeceraEditarCelda;
    }

    public void setCabeceraEditarCelda(String cabeceraEditarCelda) {
        this.cabeceraEditarCelda = cabeceraEditarCelda;
    }

    public String getInfoVariableEditarCelda() {
        return infoVariableEditarCelda;
    }

    public void setInfoVariableEditarCelda(String infoVariableEditarCelda) {
        this.infoVariableEditarCelda = infoVariableEditarCelda;
    }

    public Empleados getEmpleadoBA() {
        return empleadoBA;
    }

    public void setEmpleadoBA(Empleados empleadoBA) {
        this.empleadoBA = empleadoBA;
    }

    public Date getFechaInicialDatosPersonales() {
        return fechaInicialDatosPersonales;
    }

    public void setFechaInicialDatosPersonales(Date fechaInicialDatosPersonales) {
        this.fechaInicialDatosPersonales = fechaInicialDatosPersonales;
    }

    public Date getFechaFinalDatosPersonales() {
        return fechaFinalDatosPersonales;
    }

    public void setFechaFinalDatosPersonales(Date fechaFinalDatosPersonales) {
        this.fechaFinalDatosPersonales = fechaFinalDatosPersonales;
    }

    public List<Ciudades> getLovCiudades() {
        if (lovCiudades == null) {
            lovCiudades = administrarEmpleadoIndividual.ciudades();
        }
        return lovCiudades;
    }

    public void setLovCiudades(List<Ciudades> lovCiudades) {
        this.lovCiudades = lovCiudades;
    }

    public List<Ciudades> getFiltrarLovCiudades() {
        return filtrarLovCiudades;
    }

    public void setFiltrarLovCiudades(List<Ciudades> filtrarLovCiudades) {
        this.filtrarLovCiudades = filtrarLovCiudades;
    }

    public Ciudades getCiudadSeleccionada() {
        return ciudadSeleccionada;
    }

    public void setCiudadSeleccionada(Ciudades ciudadSeleccionada) {
        this.ciudadSeleccionada = ciudadSeleccionada;
    }

    public List<Empleados> getListaEmpleadosResultados() {
        return listaEmpleadosResultados;
    }

    public void setListaEmpleadosResultados(List<Empleados> listaEmpleadosResultados) {
        this.listaEmpleadosResultados = listaEmpleadosResultados;
    }

    public int getTabActivaFactorRH() {
        return tabActivaFactorRH;
    }

    public void setTabActivaFactorRH(int tabActivaFactorRH) {
        this.tabActivaFactorRH = tabActivaFactorRH;
    }

    public int getTabActivaDatosPersonales() {
        return tabActivaDatosPersonales;
    }

    public void setTabActivaDatosPersonales(int tabActivaDatosPersonales) {
        this.tabActivaDatosPersonales = tabActivaDatosPersonales;
    }

    public EstadosCiviles getEstadoCivilBA() {
        return estadoCivilBA;
    }

    public void setEstadoCivilBA(EstadosCiviles estadoCivilBA) {
        this.estadoCivilBA = estadoCivilBA;
    }

    public int getTabActivaEstadoCivil() {
        return tabActivaEstadoCivil;
    }

    public void setTabActivaEstadoCivil(int tabActivaEstadoCivil) {
        this.tabActivaEstadoCivil = tabActivaEstadoCivil;
    }

    public Date getFechaInicialEstadoCivil() {
        return fechaInicialEstadoCivil;
    }

    public void setFechaInicialEstadoCivil(Date fechaInicialEstadoCivil) {
        this.fechaInicialEstadoCivil = fechaInicialEstadoCivil;
    }

    public Date getFechaFinalEstadoCivil() {
        return fechaFinalEstadoCivil;
    }

    public void setFechaFinalEstadoCivil(Date fechaFinalEstadoCivil) {
        this.fechaFinalEstadoCivil = fechaFinalEstadoCivil;
    }

    public int getTipoFechaEstadoCivil() {
        return tipoFechaEstadoCivil;
    }

    public void setTipoFechaEstadoCivil(int tipoFechaEstadoCivil) {
        this.tipoFechaEstadoCivil = tipoFechaEstadoCivil;
    }

    public List<EstadosCiviles> getLovEstadosCiviles() {
        if (lovEstadosCiviles == null) {
            lovEstadosCiviles = administrarEstadosCiviles.consultarEstadosCiviles();
        }
        return lovEstadosCiviles;
    }

    public void setLovEstadosCiviles(List<EstadosCiviles> lovEstadosCiviles) {
        this.lovEstadosCiviles = lovEstadosCiviles;
    }

    public List<EstadosCiviles> getFiltrarLovEstadosCiviles() {
        return filtrarLovEstadosCiviles;
    }

    public void setFiltrarLovEstadosCiviles(List<EstadosCiviles> filtrarLovEstadosCiviles) {
        this.filtrarLovEstadosCiviles = filtrarLovEstadosCiviles;
    }

    public EstadosCiviles getEstadoCivilSeleccionado() {
        return estadoCivilSeleccionado;
    }

    public void setEstadoCivilSeleccionado(EstadosCiviles estadoCivilSeleccionado) {
        this.estadoCivilSeleccionado = estadoCivilSeleccionado;
    }

    public boolean isActivoFechasEstadoCivil() {
        return activoFechasEstadoCivil;
    }

    public void setActivoFechasEstadoCivil(boolean activoFechasEstadoCivil) {
        this.activoFechasEstadoCivil = activoFechasEstadoCivil;
    }

    public IdiomasPersonas getIdiomaPersonaBA() {
        return idiomaPersonaBA;
    }

    public void setIdiomaPersonaBA(IdiomasPersonas idiomaPersonaBA) {
        this.idiomaPersonaBA = idiomaPersonaBA;
    }

    public int getTabActivaIdioma() {
        return tabActivaIdioma;
    }

    public void setTabActivaIdioma(int tabActivaIdioma) {
        this.tabActivaIdioma = tabActivaIdioma;
    }

    public List<Idiomas> getLovIdiomas() {
        if (lovIdiomas == null) {
            lovIdiomas = administrarIdiomasPersonas.listIdiomas();
        }
        return lovIdiomas;
    }

    public void setLovIdiomas(List<Idiomas> lovIdiomas) {
        this.lovIdiomas = lovIdiomas;
    }

    public List<Idiomas> getFiltrarLovIdiomas() {
        return filtrarLovIdiomas;
    }

    public void setFiltrarLovIdiomas(List<Idiomas> filtrarLovIdiomas) {
        this.filtrarLovIdiomas = filtrarLovIdiomas;
    }

    public Idiomas getIdiomaSeleccionado() {
        return idiomaSeleccionado;
    }

    public void setIdiomaSeleccionado(Idiomas idiomaSeleccionado) {
        this.idiomaSeleccionado = idiomaSeleccionado;
    }

    public BigInteger getConversacionDesde() {
        return conversacionDesde;
    }

    public void setConversacionDesde(BigInteger conversacionDesde) {
        this.conversacionDesde = conversacionDesde;
    }

    public BigInteger getConversacionHasta() {
        return conversacionHasta;
    }

    public void setConversacionHasta(BigInteger conversacionHasta) {
        this.conversacionHasta = conversacionHasta;
    }

    public BigInteger getLecturaDesde() {
        return lecturaDesde;
    }

    public void setLecturaDesde(BigInteger lecturaDesde) {
        this.lecturaDesde = lecturaDesde;
    }

    public BigInteger getLecturaHasta() {
        return lecturaHasta;
    }

    public void setLecturaHasta(BigInteger lecturaHasta) {
        this.lecturaHasta = lecturaHasta;
    }

    public BigInteger getEscrituraDesde() {
        return escrituraDesde;
    }

    public void setEscrituraDesde(BigInteger escrituraDesde) {
        this.escrituraDesde = escrituraDesde;
    }

    public BigInteger getEscrituraHasta() {
        return escrituraHasta;
    }

    public void setEscrituraHasta(BigInteger escrituraHasta) {
        this.escrituraHasta = escrituraHasta;
    }

    public VigenciasIndicadores getVigenciaIndicadorBA() {
        return vigenciaIndicadorBA;
    }

    public void setVigenciaIndicadorBA(VigenciasIndicadores vigenciaIndicadorBA) {
        this.vigenciaIndicadorBA = vigenciaIndicadorBA;
    }

    public int getTabActivaCenso() {
        return tabActivaCenso;
    }

    public void setTabActivaCenso(int tabActivaCenso) {
        this.tabActivaCenso = tabActivaCenso;
    }

    public Date getFechaInicialCenso() {
        return fechaInicialCenso;
    }

    public void setFechaInicialCenso(Date fechaInicialCenso) {
        this.fechaInicialCenso = fechaInicialCenso;
    }

    public int getTipoFechaCenso() {
        return tipoFechaCenso;
    }

    public void setTipoFechaCenso(int tipoFechaCenso) {
        this.tipoFechaCenso = tipoFechaCenso;
    }

    public List<TiposIndicadores> getLovTiposIndicadores() {
        if (lovTiposIndicadores == null) {
            lovTiposIndicadores = administrarEmplVigenciaIndicador.listTiposIndicadores();
        }
        return lovTiposIndicadores;
    }

    public void setLovTiposIndicadores(List<TiposIndicadores> lovTiposIndicadores) {
        this.lovTiposIndicadores = lovTiposIndicadores;
    }

    public List<TiposIndicadores> getFiltrarLovTiposIndicadores() {
        return filtrarLovTiposIndicadores;
    }

    public void setFiltrarLovTiposIndicadores(List<TiposIndicadores> filtrarLovTiposIndicadores) {
        this.filtrarLovTiposIndicadores = filtrarLovTiposIndicadores;
    }

    public TiposIndicadores getTipoIndicadorSeleccionado() {
        return tipoIndicadorSeleccionado;
    }

    public void setTipoIndicadorSeleccionado(TiposIndicadores tipoIndicadorSeleccionado) {
        this.tipoIndicadorSeleccionado = tipoIndicadorSeleccionado;
    }

    public List<Indicadores> getLovIndicadores() {
        if (lovIndicadores == null) {
            lovIndicadores = administrarEmplVigenciaIndicador.listIndicadores();
        }
        return lovIndicadores;
    }

    public void setLovIndicadores(List<Indicadores> lovIndicadores) {
        this.lovIndicadores = lovIndicadores;
    }

    public List<Indicadores> getFiltrarLovIndicadores() {
        return filtrarLovIndicadores;
    }

    public void setFiltrarLovIndicadores(List<Indicadores> filtrarLovIndicadores) {
        this.filtrarLovIndicadores = filtrarLovIndicadores;
    }

    public Indicadores getIndicadorSeleccionado() {
        return indicadorSeleccionado;
    }

    public void setIndicadorSeleccionado(Indicadores indicadorSeleccionado) {
        this.indicadorSeleccionado = indicadorSeleccionado;
    }

    public boolean isActivoFechasCenso() {
        return activoFechasCenso;
    }

    public void setActivoFechasCenso(boolean activoFechasCenso) {
        this.activoFechasCenso = activoFechasCenso;
    }

    public Date getFechaFinalCenso() {
        return fechaFinalCenso;
    }

    public void setFechaFinalCenso(Date fechaFinalCenso) {
        this.fechaFinalCenso = fechaFinalCenso;
    }

    public VigenciasFormales getVigenciaFormalBA() {
        return vigenciaFormalBA;
    }

    public void setVigenciaFormalBA(VigenciasFormales vigenciaFormalBA) {
        this.vigenciaFormalBA = vigenciaFormalBA;
    }

    public int getTabActivaEducacionFormal() {
        return tabActivaEducacionFormal;
    }

    public void setTabActivaEducacionFormal(int tabActivaEducacionFormal) {
        this.tabActivaEducacionFormal = tabActivaEducacionFormal;
    }

    public Date getFechaInicialEducacionFormal() {
        return fechaInicialEducacionFormal;
    }

    public void setFechaInicialEducacionFormal(Date fechaInicialEducacionFormal) {
        this.fechaInicialEducacionFormal = fechaInicialEducacionFormal;
    }

    public Date getFechaFinalEducacionFormal() {
        return fechaFinalEducacionFormal;
    }

    public void setFechaFinalEducacionFormal(Date fechaFinalEducacionFormal) {
        this.fechaFinalEducacionFormal = fechaFinalEducacionFormal;
    }

    public int getTipoFechaEducacionFormal() {
        return tipoFechaEducacionFormal;
    }

    public void setTipoFechaEducacionFormal(int tipoFechaEducacionFormal) {
        this.tipoFechaEducacionFormal = tipoFechaEducacionFormal;
    }

    public List<Instituciones> getLovInstituciones() {
        if (lovInstituciones == null) {
            lovInstituciones = administrarVigenciasFormales.lovInstituciones();
        }
        return lovInstituciones;
    }

    public void setLovInstituciones(List<Instituciones> lovInstituciones) {
        this.lovInstituciones = lovInstituciones;
    }

    public List<Instituciones> getFiltrarLovInstituciones() {
        return filtrarLovInstituciones;
    }

    public void setFiltrarLovInstituciones(List<Instituciones> filtrarLovInstituciones) {
        this.filtrarLovInstituciones = filtrarLovInstituciones;
    }

    public Instituciones getInstitucionSeleccionada() {
        return institucionSeleccionada;
    }

    public void setInstitucionSeleccionada(Instituciones institucionSeleccionada) {
        this.institucionSeleccionada = institucionSeleccionada;
    }

    public List<Profesiones> getLovProfesiones() {
        if (lovProfesiones == null) {
            lovProfesiones = administrarVigenciasFormales.lovProfesiones();
        }
        return lovProfesiones;
    }

    public void setLovProfesiones(List<Profesiones> lovProfesiones) {
        this.lovProfesiones = lovProfesiones;
    }

    public List<Profesiones> getFiltrarLovProfesiones() {
        return filtrarLovProfesiones;
    }

    public void setFiltrarLovProfesiones(List<Profesiones> filtrarLovProfesiones) {
        this.filtrarLovProfesiones = filtrarLovProfesiones;
    }

    public Profesiones getProfesionSeleccionada() {
        return profesionSeleccionada;
    }

    public void setProfesionSeleccionada(Profesiones profesionSeleccionada) {
        this.profesionSeleccionada = profesionSeleccionada;
    }

    public boolean isActivoFechasEducacionFormal() {
        return activoFechasEducacionFormal;
    }

    public void setActivoFechasEducacionFormal(boolean activoFechasEducacionFormal) {
        this.activoFechasEducacionFormal = activoFechasEducacionFormal;
    }

    public String getDesarrolladoEducacionFormal() {
        return desarrolladoEducacionFormal;
    }

    public void setDesarrolladoEducacionFormal(String desarrolladoEducacionFormal) {
        this.desarrolladoEducacionFormal = desarrolladoEducacionFormal;
    }

    public VigenciasNoFormales getVigenciaNoFormalBA() {
        return vigenciaNoFormalBA;
    }

    public void setVigenciaNoFormalBA(VigenciasNoFormales vigenciaNoFormalBA) {
        this.vigenciaNoFormalBA = vigenciaNoFormalBA;
    }

    public int getTabActivaEducacionNoFormal() {
        return tabActivaEducacionNoFormal;
    }

    public void setTabActivaEducacionNoFormal(int tabActivaEducacionNoFormal) {
        this.tabActivaEducacionNoFormal = tabActivaEducacionNoFormal;
    }

    public Date getFechaInicialEducacionNoFormal() {
        return fechaInicialEducacionNoFormal;
    }

    public void setFechaInicialEducacionNoFormal(Date fechaInicialEducacionNoFormal) {
        this.fechaInicialEducacionNoFormal = fechaInicialEducacionNoFormal;
    }

    public Date getFechaFinalEducacionNoFormal() {
        return fechaFinalEducacionNoFormal;
    }

    public void setFechaFinalEducacionNoFormal(Date fechaFinalEducacionNoFormal) {
        this.fechaFinalEducacionNoFormal = fechaFinalEducacionNoFormal;
    }

    public int getTipoFechaEducacionNoFormal() {
        return tipoFechaEducacionNoFormal;
    }

    public void setTipoFechaEducacionNoFormal(int tipoFechaEducacionNoFormal) {
        this.tipoFechaEducacionNoFormal = tipoFechaEducacionNoFormal;
    }

    public List<Cursos> getLovCursos() {
        if (lovCursos == null) {
            lovCursos = administrarVigenciasNoFormales.lovCursos();
        }
        return lovCursos;
    }

    public void setLovCursos(List<Cursos> lovCursos) {
        this.lovCursos = lovCursos;
    }

    public List<Cursos> getFiltrarLovCursos() {
        return filtrarLovCursos;
    }

    public void setFiltrarLovCursos(List<Cursos> filtrarLovCursos) {
        this.filtrarLovCursos = filtrarLovCursos;
    }

    public Cursos getCursoSeleccionado() {
        return cursoSeleccionado;
    }

    public void setCursoSeleccionado(Cursos cursoSeleccionado) {
        this.cursoSeleccionado = cursoSeleccionado;
    }

    public boolean isActivoFechasEducacionNoFormal() {
        return activoFechasEducacionNoFormal;
    }

    public void setActivoFechasEducacionNoFormal(boolean activoFechasEducacionNoFormal) {
        this.activoFechasEducacionNoFormal = activoFechasEducacionNoFormal;
    }

    public String getDesarrolladoEducacionNoFormal() {
        return desarrolladoEducacionNoFormal;
    }

    public void setDesarrolladoEducacionNoFormal(String desarrolladoEducacionNoFormal) {
        this.desarrolladoEducacionNoFormal = desarrolladoEducacionNoFormal;
    }

    public HvExperienciasLaborales getHvExperienciaLaboralBA() {
        return hvExperienciaLaboralBA;
    }

    public void setHvExperienciaLaboralBA(HvExperienciasLaborales hvExperienciaLaboralBA) {
        this.hvExperienciaLaboralBA = hvExperienciaLaboralBA;
    }

    public int getTabActivaExperienciaLaboral() {
        return tabActivaExperienciaLaboral;
    }

    public void setTabActivaExperienciaLaboral(int tabActivaExperienciaLaboral) {
        this.tabActivaExperienciaLaboral = tabActivaExperienciaLaboral;
    }

    public Date getFechaInicialExperienciaLaboral() {
        return fechaInicialExperienciaLaboral;
    }

    public void setFechaInicialExperienciaLaboral(Date fechaInicialExperienciaLaboral) {
        this.fechaInicialExperienciaLaboral = fechaInicialExperienciaLaboral;
    }

    public Date getFechaFinalExperienciaLaboral() {
        return fechaFinalExperienciaLaboral;
    }

    public void setFechaFinalExperienciaLaboral(Date fechaFinalExperienciaLaboral) {
        this.fechaFinalExperienciaLaboral = fechaFinalExperienciaLaboral;
    }

    public List<SectoresEconomicos> getLovSectoresEconomicos() {
        if (lovSectoresEconomicos == null) {
            lovSectoresEconomicos = administrarPerExperienciaLaboral.listSectoresEconomicos();
        }
        return lovSectoresEconomicos;
    }

    public void setLovSectoresEconomicos(List<SectoresEconomicos> lovSectoresEconomicos) {
        this.lovSectoresEconomicos = lovSectoresEconomicos;
    }

    public List<SectoresEconomicos> getFiltrarLovSectoresEconomicos() {
        return filtrarLovSectoresEconomicos;
    }

    public void setFiltrarLovSectoresEconomicos(List<SectoresEconomicos> filtrarLovSectoresEconomicos) {
        this.filtrarLovSectoresEconomicos = filtrarLovSectoresEconomicos;
    }

    public SectoresEconomicos getSectorEconomicoSeleccionado() {
        return sectorEconomicoSeleccionado;
    }

    public void setSectorEconomicoSeleccionado(SectoresEconomicos sectorEconomicoSeleccionado) {
        this.sectorEconomicoSeleccionado = sectorEconomicoSeleccionado;
    }

    public String getEmpresaExperienciaLaboral() {
        return empresaExperienciaLaboral;
    }

    public void setEmpresaExperienciaLaboral(String empresaExperienciaLaboral) {
        this.empresaExperienciaLaboral = empresaExperienciaLaboral;
    }

    public String getCargoExperienciaLaboral() {
        return cargoExperienciaLaboral;
    }

    public void setCargoExperienciaLaboral(String cargoExperienciaLaboral) {
        this.cargoExperienciaLaboral = cargoExperienciaLaboral;
    }

    public VigenciasProyectos getVigenciaProyectoBA() {
        return vigenciaProyectoBA;
    }

    public void setVigenciaProyectoBA(VigenciasProyectos vigenciaProyectoBA) {
        this.vigenciaProyectoBA = vigenciaProyectoBA;
    }

    public int getTabActivaProyecto() {
        return tabActivaProyecto;
    }

    public void setTabActivaProyecto(int tabActivaProyecto) {
        this.tabActivaProyecto = tabActivaProyecto;
    }

    public Date getFechaInicialProyecto() {
        return fechaInicialProyecto;
    }

    public void setFechaInicialProyecto(Date fechaInicialProyecto) {
        this.fechaInicialProyecto = fechaInicialProyecto;
    }

    public Date getFechaFinalProyecto() {
        return fechaFinalProyecto;
    }

    public void setFechaFinalProyecto(Date fechaFinalProyecto) {
        this.fechaFinalProyecto = fechaFinalProyecto;
    }

    public List<PryRoles> getLovPryRoles() {
        if (lovPryRoles == null) {
            lovPryRoles = administrarVigenciasProyectos.lovPryRoles();
        }
        return lovPryRoles;
    }

    public void setLovPryRoles(List<PryRoles> lovPryRoles) {
        this.lovPryRoles = lovPryRoles;
    }

    public List<PryRoles> getFiltrarLovPryRoles() {
        return filtrarLovPryRoles;
    }

    public void setFiltrarLovPryRoles(List<PryRoles> filtrarLovPryRoles) {
        this.filtrarLovPryRoles = filtrarLovPryRoles;
    }

    public PryRoles getPryRolSeleccionado() {
        return pryRolSeleccionado;
    }

    public void setPryRolSeleccionado(PryRoles pryRolSeleccionado) {
        this.pryRolSeleccionado = pryRolSeleccionado;
    }

    public List<Proyectos> getLovProyectos() {
        if (lovProyectos == null) {
            lovProyectos = administrarVigenciasProyectos.lovProyectos();
        }
        return lovProyectos;
    }

    public void setLovProyectos(List<Proyectos> lovProyectos) {
        this.lovProyectos = lovProyectos;
    }

    public List<Proyectos> getFiltrarLovProyectos() {
        return filtrarLovProyectos;
    }

    public void setFiltrarLovProyectos(List<Proyectos> filtrarLovProyectos) {
        this.filtrarLovProyectos = filtrarLovProyectos;
    }

    public Proyectos getProyectoSeleccionado() {
        return proyectoSeleccionado;
    }

    public void setProyectoSeleccionado(Proyectos proyectoSeleccionado) {
        this.proyectoSeleccionado = proyectoSeleccionado;
    }

    public VigenciasCargos getVigenciaCargoPersonalBA() {
        return vigenciaCargoPersonalBA;
    }

    public void setVigenciaCargoPersonalBA(VigenciasCargos vigenciaCargoPersonalBA) {
        this.vigenciaCargoPersonalBA = vigenciaCargoPersonalBA;
    }

    public int getTabActivaCargoPostularse() {
        return tabActivaCargoPostularse;
    }

    public void setTabActivaCargoPostularse(int tabActivaCargoPostularse) {
        this.tabActivaCargoPostularse = tabActivaCargoPostularse;
    }

    public String getAltoTabla() {
        return this.altoTabla;
    }

    public void setAltoTabla(String altoTabla) {
        this.altoTabla = altoTabla;
    }

    public String getVisibilidadNomina() {
        return visibilidadNomina;
    }

    public void setVisibilidadNomina(String visibilidadNomina) {
        this.visibilidadNomina = visibilidadNomina;
    }

    public String getVisibilidadPersonal() {
        return visibilidadPersonal;
    }

    public void setVisibilidadPersonal(String visibilidadPersonal) {
        this.visibilidadPersonal = visibilidadPersonal;
    }

    public String getDisplayNomina() {
        return displayNomina;
    }

    public void setDisplayNomina(String displayNomina) {
        this.displayNomina = displayNomina;
    }

    public String getDisplayPersonal() {
        return displayPersonal;
    }

    public void setDisplayPersonal(String displayPersonal) {
        this.displayPersonal = displayPersonal;
    }

    public ColumnasBusquedaAvanzada getColumnaSeleccionada() {
        return this.columnaSeleccionada;
    }

    public void setColumnaSeleccionada(ColumnasBusquedaAvanzada columnaSeleccionada) {
        this.columnaSeleccionada = columnaSeleccionada;
    }
}
