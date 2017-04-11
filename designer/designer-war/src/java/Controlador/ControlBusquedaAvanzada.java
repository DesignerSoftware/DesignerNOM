package Controlador;

import ClasesAyuda.ParametrosBusquedaAvanzada;
import ClasesAyuda.ParametrosBusquedaNomina;
import ClasesAyuda.ParametrosBusquedaPersonal;
import ClasesAyuda.ParametrosQueryBusquedaAvanzada;
import ComponentesDinamicos.ControladorColumnasDinamicas;
import Entidades.AdiestramientosF;
import Entidades.AdiestramientosNF;
import Entidades.Cargos;
import Entidades.CentrosCostos;
import Entidades.Ciudades;
import Entidades.ColumnasEscenarios;
import Entidades.Conceptos;
import Entidades.Contratos;
import Entidades.Cursos;
import Entidades.Empleados;
import Entidades.Empresas;
import Entidades.EstadosAfiliaciones;
import Entidades.EstadosCiviles;
import Entidades.Estructuras;
import Entidades.Formulas;
import Entidades.Formulascontratos;
import Entidades.GruposSalariales;
import Entidades.GruposViaticos;
import Entidades.HVHojasDeVida;
import Entidades.HvExperienciasLaborales;
import Entidades.Idiomas;
import Entidades.IdiomasPersonas;
import Entidades.Indicadores;
import Entidades.Instituciones;
import Entidades.JornadasLaborales;
import Entidades.MetodosPagos;
import Entidades.Monedas;
import Entidades.MotivosCambiosCargos;
import Entidades.MotivosCambiosSueldos;
import Entidades.MotivosContratos;
import Entidades.MotivosLocalizaciones;
import Entidades.MotivosRetiros;
import Entidades.Motivosmvrs;
import Entidades.Mvrs;
import Entidades.NormasLaborales;
import Entidades.Papeles;
import Entidades.Parametros;
import Entidades.ParametrosEstructuras;
import Entidades.Periodicidades;
import Entidades.Personas;
import Entidades.Procesos;
import Entidades.ProcesosProductivos;
import Entidades.Profesiones;
import Entidades.Proyectos;
import Entidades.PryRoles;
import Entidades.ReformasLaborales;
import Entidades.ResultadoBusquedaAvanzada;
import Entidades.SectoresEconomicos;
import Entidades.Sucursales;
import Entidades.TercerosSucursales;
import Entidades.TiposContratos;
import Entidades.TiposCotizantes;
import Entidades.TiposDescansos;
import Entidades.TiposDocumentos;
import Entidades.TiposEducaciones;
import Entidades.TiposEntidades;
import Entidades.TiposIndicadores;
import Entidades.TiposSueldos;
import Entidades.TiposTrabajadores;
import Entidades.UbicacionesGeograficas;
import Entidades.Unidades;
import Entidades.Usuarios;
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
import InterfaceAdministrar.AdministrarBusquedaAvanzadaInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import ControlNavegacion.ControlListaNavegacion;
import java.util.Map;
import java.util.LinkedHashMap;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.export.Exporter;
import org.primefaces.component.tabview.TabView;
import org.primefaces.context.RequestContext;

@ManagedBean
@ViewScoped
public class ControlBusquedaAvanzada implements Serializable {

   @EJB
   private AdministrarBusquedaAvanzadaInterface administrarBusquedaAvanzada;
   //Objeto de negocio con todos los parametros posibles.
   private ParametrosBusquedaAvanzada parametros;
   //Listas de valores
   //Cargos
   private List<Cargos> lovCargos;
   private List<Cargos> filtroLovCargos;
   private Cargos cargoSeleccionado;
   private String infoRegistroCargo;
   private String infoRegistroCargo2;
   //Estructuras
   private List<Estructuras> lovEstructuras;
   private List<Estructuras> filtroLovEstructuras;
   private Estructuras estructuraSeleccionada;
   private String infoRegistroEstructura;
   private String infoRegistroEstructura2;
   //Empleado Jefe
   private List<Empleados> lovJefe;
   private List<Empleados> filtroLovJefe;
   private Empleados jefeSeleccionado;
   private String infoRegistroJefe;
   //Motivos Cambios Cargos
   private List<MotivosCambiosCargos> lovMotivosCargos;
   private List<MotivosCambiosCargos> filtroLovMotivosCargos;
   private MotivosCambiosCargos motivoCargoSeleccionado;
   private String infoRegistroMotivoCargo;
   //Papeles
   private List<Papeles> lovPapeles;
   private List<Papeles> filtroLovPapeles;
   private Papeles papelSeleccionado;
   private String infoRegistroPapel;
   //Motivos Localizaciones
   private List<MotivosLocalizaciones> lovMotivosLocalizaciones;
   private List<MotivosLocalizaciones> filtroLovMotivosLocalizaciones;
   private MotivosLocalizaciones motivoLocalizacionSeleccionado;
   private String infoRegistroMotivoLocalizacion;
   //Tipos Sueldos
   private List<TiposSueldos> lovTiposSueldos;
   private List<TiposSueldos> filtroLovTiposSueldos;
   private TiposSueldos tipoSueldoSeleccionado;
   private String infoRegistroTipoSueldo;
   //Motivos Sueldos
   private List<MotivosCambiosSueldos> lovMotivosSueldos;
   private List<MotivosCambiosSueldos> filtroLovMotivosSueldos;
   private MotivosCambiosSueldos motivoSueldoSeleccionado;
   private String infoRegistroMotivoSueldo;
   //Tipos Contratos
   private List<TiposContratos> lovTiposContratos;
   private List<TiposContratos> filtroLovTiposContratos;
   private TiposContratos tipoContratoSeleccionado;
   private String infoRegistroTipoContrato;
   //Motivos Contratos
   private List<MotivosContratos> lovMotivosContratos;
   private List<MotivosContratos> filtroLovMotivosContratos;
   private MotivosContratos motivoContratoSeleccionado;
   private String infoRegistroMotivoContrato;
   //Tipos Trabajadores
   private List<TiposTrabajadores> lovTiposTrabajadores;
   private List<TiposTrabajadores> filtroLovTiposTrabajadores;
   private TiposTrabajadores tipoTrabajadorSeleccionado;
   private String infoRegistroTipoTrabajador;
   //Reformas Laborales
   private List<ReformasLaborales> lovReformasLaborales;
   private List<ReformasLaborales> filtroLovReformasLaborales;
   private ReformasLaborales reformaLaboralSeleccionada;
   private String infoRegistroReformaLaboral;
   //Contratos
   private List<Contratos> lovContratos;
   private List<Contratos> filtroLovContratos;
   private Contratos contratoSeleccionado;
   private String infoRegistroContrato;
   //Ubicaciones geograficas
   private List<UbicacionesGeograficas> lovUbicaciones;
   private List<UbicacionesGeograficas> filtroLovUbicaciones;
   private UbicacionesGeograficas ubicacionSeleccionado;
   private String infoRegistroUbicacion;
   //Terceros Sucursales
   private List<TercerosSucursales> lovTercerosSucursales;
   private List<TercerosSucursales> filtroLovTercerosSucursales;
   private TercerosSucursales terceroSucursalSeleccionado;
   private String infoRegistroTerceroSucursal;
   //Tipos Entidades
   private List<TiposEntidades> lovTiposEntidades;
   private List<TiposEntidades> filtroLovTiposEntidades;
   private TiposEntidades tipoEntidadSeleccionado;
   private String infoRegistroTipoEntidad;
   //Estados Afiliaciones
   private List<EstadosAfiliaciones> lovEstadosAfiliaciones;
   private List<EstadosAfiliaciones> filtroLovEstadosAfiliaciones;
   private EstadosAfiliaciones estadoAfiliacionSeleccionado;
   private String infoRegistroEstadoAfiliacion;
   //Formas de pago
   private List<Periodicidades> lovFormasPagos;
   private List<Periodicidades> filtroLovFormasPagos;
   private Periodicidades formaPagoSeleccionado;
   private String infoRegistroFormaPago;
   //Sucursales
   private List<Sucursales> lovSucursales;
   private List<Sucursales> filtroLovSucursales;
   private Sucursales sucursalSeleccionado;
   private String infoRegistroSucursal;
   //mOTIVOS mVRS
   private List<Motivosmvrs> lovMotivosMvrs;
   private List<Motivosmvrs> filtroLovMotivosMvrs;
   private Motivosmvrs motivoMvrSeleccionado;
   private String infoRegistroMotivoMvr;
   //Norma Laboral
   private List<NormasLaborales> lovNormasLaborales;
   private List<NormasLaborales> filtroLovNormasLaborales;
   private NormasLaborales normaLaboralSeleccionado;
   private String infoRegistroNormaLaboral;
   //Jornada Laboral
   private List<JornadasLaborales> lovJornadasLaborales;
   private List<JornadasLaborales> filtroLovJornadasLaborales;
   private JornadasLaborales jornadaLaboralSeleccionado;
   private String infoRegistroJornadaLaboral;
   //Motivo Retiro
   private List<MotivosRetiros> lovMotivosRetiros;
   private List<MotivosRetiros> filtroLovMotivosRetiros;
   private MotivosRetiros motivoRetiroSeleccionado;
   private String infoRegistroMotivoRetiro;
   private String infoRegistroMotivoRetiro2;
   //Ciudades
   private List<Ciudades> lovCiudades;
   private List<Ciudades> filtroLovCiudades;
   private Ciudades ciudadSeleccionado;
   private String infoRegistroCiudad;
   private String infoRegistroCiudad2;
   //Estado Civil
   private List<EstadosCiviles> lovEstadosCiviles;
   private List<EstadosCiviles> filtroLovEstadosCiviles;
   private EstadosCiviles estadoCivilSeleccionado;
   private String infoRegistroEstadoCivil;
   //Idioma
   private List<Idiomas> lovIdiomas;
   private List<Idiomas> filtroLovIdiomas;
   private Idiomas idiomaSeleccionado;
   private String infoRegistroIdioma;
   //Tipo Indicador
   private List<TiposIndicadores> lovTiposIndicadores;
   private List<TiposIndicadores> filtroLovTiposIndicadores;
   private TiposIndicadores tipoIndicadorSeleccionado;
   private String infoRegistroTipoIndicador;
   //Indicador
   private List<Indicadores> lovIndicadores;
   private List<Indicadores> filtroLovIndicadores;
   private Indicadores indicadorSeleccionado;
   private String infoRegistroIndicador;
   //Profesion
   private List<Profesiones> lovProfesiones;
   private List<Profesiones> filtroLovProfesiones;
   private Profesiones profesionSeleccionado;
   private String infoRegistroProfesion;
   //Institucion
   private List<Instituciones> lovInstituciones;
   private List<Instituciones> filtroLovInstituciones;
   private Instituciones institucionSeleccionado;
   private String infoRegistroInstitucion;
   private String infoRegistroInstitucion2;
   //Cursos
   private List<Cursos> lovCursos;
   private List<Cursos> filtroLovCursos;
   private Cursos cursoSeleccionado;
   private String infoRegistroCurso;
   //Sector economico
   private List<SectoresEconomicos> lovSectoresEconomicos;
   private List<SectoresEconomicos> filtroLovSectoresEconomicos;
   private SectoresEconomicos sectorEconomicoSeleccionado;
   private String infoRegistroSectorEconomico;
   //Proyecto
   private List<Proyectos> lovProyectos;
   private List<Proyectos> filtroLovProyectos;
   private Proyectos proyectoSeleccionado;
   private String infoRegistroProyecto;
   //Rol
   private List<PryRoles> lovRoles;
   private List<PryRoles> filtroLovRoles;
   private PryRoles rolSeleccionado;
   private String infoRegistroRol;
   //LOV Columnas de busqueda
   private List<ColumnasEscenarios> lovColumnasEscenarios;
   private List<ColumnasEscenarios> filtradoColumnasEscenarios;
   private List<ColumnasEscenarios> columnasEsSeleccionadas;
   private String infoRegistroColumnasEs;
   //Variables control visibilidad.
   private String vTipoBusqueda;
   private String vTipoFechaCargo;
   private String vTipoFechaCentroCosto;
   private String vTipoFechaSueldo;
   private String vTipoFechaTipoContrato;
   private String vTipoFechaTipoTrabajador;
   private String vTipoFechaReformaLaboral;
   private String vTipoFechaLegislacion;
   private String vTipoFechaUbicacion;
   private String vTipoFechaAfiliacion;
   private String vTipoFechaFormaPago;
   private String vTipoFechaMvr;
   private String vTipoFechaSet;
   private String vTipoFechaNormaLaboral;
   private String vTipoFechaDatosPersonales;
   private String vTipoFechaEstadoCivil;
   private String vTipoFechaCenso;
   private String vTipoFechaEducacionFormal;
   private String vTipoFechaEducacionNoFormal;
   //Parametros Busqueda Avanzada
   private List<ParametrosQueryBusquedaAvanzada> listaParametrosQueryModulos;
   //Respuesta de la busqueda avanzada
   private List<ResultadoBusquedaAvanzada> listaResultadoBusqueda;
   private List<ResultadoBusquedaAvanzada> filtroResultadoBusqueda;
   //Variables miscelaneas
   private String valorPorDefecto;
   private String valorCopia;
   List<BigInteger> listaCodigosEmpleado;
   List<BigInteger> listaSecuenciasEmpleado;
   //Para Todos los listados
   private String infoRegistro;
   private boolean aceptar;
   private String anchoTablaResultados;
   private String txt_vcargo_cargo, txt_vcargo_empleadoJefe_Per, txt_vcargo_estructura, txt_vcargo_estructura_CC, txt_vcargo_motivoCam, txt_vcargo_papel,
           txt_vlocalizacion_locali, txt_vlocalizacion_motivo, txt_vlocalizacion_proyecto, txt_vsueldo_motivo, txt_vsueldo_tipoS,
           txt_vtipoContrato_ciudad, txt_vtipoContrato_motivo, txt_vtipoContrato_tipoCont, txt_vtipoTrab_tipoTrab,
           txt_vreformaL_ReforLab, txt_vcontrato_contrato, txt_vubicacion_ubic, txt_vubicacion_ciudad, txt_vafiliacion_estado,
           txt_vafiliacion_tercSuc, txt_vafiliacion_tipoEntidad, txt_vformapago_formaP, txt_vformapago_sucursal, txt_mvrs_motivo, txt_vnorma_norma,
           txt_vjornada_jornada, txtP_empleado_per_ciudadNac, txtP_empleado_per_ciudadDoc, txtP_empleado_per_tipoDoc, txtP_estadoCivil, txtP_idiomap_idioma,
           txtP_vindicador_indic, txtP_vindicador_tipoIndi, txtP_vFormal_institu, txtP_vFormal_profecion, txtP_vNoformal_curso, txtP_vNoformal_institu,
           txtP_experLab_SectorEco, txtP_experLab_motivo, txtP_vproyecto_proyect, txtP_vproyecto_rol, txtP_cargoPostul, txt_motivoRet;

   public ControlBusquedaAvanzada() {
      //Inicializar objeto de negocio
      parametros = new ParametrosBusquedaAvanzada();
      parametros.setParametrosBusquedaNomina(new ParametrosBusquedaNomina());

      parametros.getParametrosBusquedaNomina().setVigenciaCargo(new VigenciasCargos());
      parametros.getParametrosBusquedaNomina().getVigenciaCargo().setCargo(new Cargos());
      parametros.getParametrosBusquedaNomina().getVigenciaCargo().setEmpleadojefe(new Empleados());
      parametros.getParametrosBusquedaNomina().getVigenciaCargo().getEmpleadojefe().setPersona(new Personas());
      parametros.getParametrosBusquedaNomina().getVigenciaCargo().setEstructura(new Estructuras());
      parametros.getParametrosBusquedaNomina().getVigenciaCargo().getEstructura().setCentrocosto(new CentrosCostos());
      parametros.getParametrosBusquedaNomina().getVigenciaCargo().setMotivocambiocargo(new MotivosCambiosCargos());
      parametros.getParametrosBusquedaNomina().getVigenciaCargo().setPapel(new Papeles());

      parametros.getParametrosBusquedaNomina().setVigenciaLocalizacion(new VigenciasLocalizaciones());
      parametros.getParametrosBusquedaNomina().getVigenciaLocalizacion().setLocalizacion(new Estructuras());
      parametros.getParametrosBusquedaNomina().getVigenciaLocalizacion().setMotivo(new MotivosLocalizaciones());
      parametros.getParametrosBusquedaNomina().getVigenciaLocalizacion().setProyecto(new Proyectos());

      parametros.getParametrosBusquedaNomina().setVigenciaSueldo(new VigenciasSueldos());
      parametros.getParametrosBusquedaNomina().getVigenciaSueldo().setMotivocambiosueldo(new MotivosCambiosSueldos());
      parametros.getParametrosBusquedaNomina().getVigenciaSueldo().setTiposueldo(new TiposSueldos());

      parametros.getParametrosBusquedaNomina().setVigenciaTipoContrato(new VigenciasTiposContratos());
      parametros.getParametrosBusquedaNomina().getVigenciaTipoContrato().setCiudad(new Ciudades());
      parametros.getParametrosBusquedaNomina().getVigenciaTipoContrato().setMotivocontrato(new MotivosContratos());
      parametros.getParametrosBusquedaNomina().getVigenciaTipoContrato().setTipocontrato(new TiposContratos());

      parametros.getParametrosBusquedaNomina().setVigenciaTipoTrabajador(new VigenciasTiposTrabajadores());
      parametros.getParametrosBusquedaNomina().getVigenciaTipoTrabajador().setTipotrabajador(new TiposTrabajadores());
      parametros.getParametrosBusquedaNomina().getVigenciaTipoTrabajador().getTipotrabajador().setTipocotizante(new TiposCotizantes());

      parametros.getParametrosBusquedaNomina().setVigenciaReformasLaboral(new VigenciasReformasLaborales());
      parametros.getParametrosBusquedaNomina().getVigenciaReformasLaboral().setReformalaboral(new ReformasLaborales());

      parametros.getParametrosBusquedaNomina().setVigenciaContrato(new VigenciasContratos());
      parametros.getParametrosBusquedaNomina().getVigenciaContrato().setContrato(new Contratos());
      parametros.getParametrosBusquedaNomina().getVigenciaContrato().setTipocontrato(new TiposContratos());

      parametros.getParametrosBusquedaNomina().setVigenciaUbicacion(new VigenciasUbicaciones());
      parametros.getParametrosBusquedaNomina().getVigenciaUbicacion().setUbicacion(new UbicacionesGeograficas());
      parametros.getParametrosBusquedaNomina().getVigenciaUbicacion().getUbicacion().setCiudad(new Ciudades());

      parametros.getParametrosBusquedaNomina().setVigenciaAfiliacion(new VigenciasAfiliaciones());
      parametros.getParametrosBusquedaNomina().getVigenciaAfiliacion().setEstadoafiliacion(new EstadosAfiliaciones());
      parametros.getParametrosBusquedaNomina().getVigenciaAfiliacion().setTercerosucursal(new TercerosSucursales());
      parametros.getParametrosBusquedaNomina().getVigenciaAfiliacion().setTipoentidad(new TiposEntidades());

      parametros.getParametrosBusquedaNomina().setVigenciaFormaPago(new VigenciasFormasPagos());
      parametros.getParametrosBusquedaNomina().getVigenciaFormaPago().setFormapago(new Periodicidades());
      parametros.getParametrosBusquedaNomina().getVigenciaFormaPago().setSucursal(new Sucursales());

      parametros.getParametrosBusquedaNomina().setMvrs(new Mvrs());
      parametros.getParametrosBusquedaNomina().getMvrs().setMotivo(new Motivosmvrs());

      parametros.getParametrosBusquedaNomina().setVigenciaNormaEmpleado(new VigenciasNormasEmpleados());
      parametros.getParametrosBusquedaNomina().getVigenciaNormaEmpleado().setNormalaboral(new NormasLaborales());

      parametros.getParametrosBusquedaNomina().setVigenciaJornada(new VigenciasJornadas());
      parametros.getParametrosBusquedaNomina().getVigenciaJornada().setJornadatrabajo(new JornadasLaborales());
      parametros.getParametrosBusquedaNomina().getVigenciaJornada().setTipodescanso(new TiposDescansos());

      parametros.getParametrosBusquedaNomina().setMotivosRetiros(new MotivosRetiros());

      parametros.setParametrosBusquedaPersonal(new ParametrosBusquedaPersonal());
      parametros.getParametrosBusquedaPersonal().setEmpleado(new Empleados());
      parametros.getParametrosBusquedaPersonal().getEmpleado().setEmpresa(new Empresas());
      parametros.getParametrosBusquedaPersonal().getEmpleado().setPersona(new Personas());
      parametros.getParametrosBusquedaPersonal().getEmpleado().getPersona().setCiudaddocumento(new Ciudades());
      parametros.getParametrosBusquedaPersonal().getEmpleado().getPersona().setCiudadnacimiento(new Ciudades());
      parametros.getParametrosBusquedaPersonal().getEmpleado().getPersona().setTipodocumento(new TiposDocumentos());

      parametros.getParametrosBusquedaPersonal().setEstadoCivil(new EstadosCiviles());
      parametros.getParametrosBusquedaPersonal().setIdiomaPersona(new IdiomasPersonas());
      parametros.getParametrosBusquedaPersonal().getIdiomaPersona().setIdioma(new Idiomas());

      parametros.getParametrosBusquedaPersonal().setVigenciaIndicador(new VigenciasIndicadores());
      parametros.getParametrosBusquedaPersonal().getVigenciaIndicador().setIndicador(new Indicadores());
      parametros.getParametrosBusquedaPersonal().getVigenciaIndicador().setTipoindicador(new TiposIndicadores());

      parametros.getParametrosBusquedaPersonal().setVigenciaFormal(new VigenciasFormales());
      parametros.getParametrosBusquedaPersonal().getVigenciaFormal().setProfesion(new Profesiones());
      parametros.getParametrosBusquedaPersonal().getVigenciaFormal().setInstitucion(new Instituciones());

      parametros.getParametrosBusquedaPersonal().setVigenciaNoFormal(new VigenciasNoFormales());
      parametros.getParametrosBusquedaPersonal().getVigenciaNoFormal().setCurso(new Cursos());
      parametros.getParametrosBusquedaPersonal().getVigenciaNoFormal().setInstitucion(new Instituciones());

      parametros.getParametrosBusquedaPersonal().setHvExperienciaLaboral(new HvExperienciasLaborales());
      parametros.getParametrosBusquedaPersonal().getHvExperienciaLaboral().setHojadevida(new HVHojasDeVida());
      parametros.getParametrosBusquedaPersonal().getHvExperienciaLaboral().setMotivoretiro(new MotivosRetiros());
      parametros.getParametrosBusquedaPersonal().getHvExperienciaLaboral().setSectoreconomico(new SectoresEconomicos());

      parametros.getParametrosBusquedaPersonal().setVigenciaProyecto(new VigenciasProyectos());
      parametros.getParametrosBusquedaPersonal().getVigenciaProyecto().setProyecto(new Proyectos());
      parametros.getParametrosBusquedaPersonal().getVigenciaProyecto().setPryCargoproyecto(new Cargos());
      parametros.getParametrosBusquedaPersonal().getVigenciaProyecto().setPryRol(new PryRoles());

      parametros.getParametrosBusquedaPersonal().setCargo(new Cargos());

      //Inicializar variables visibilidad
      vTipoBusqueda = "Nomina"; //Por defecto al ingresar la opción sera Nomina.
      vTipoFechaCargo = "false";
      vTipoFechaCentroCosto = "false";
      vTipoFechaSueldo = "false";
      vTipoFechaTipoContrato = "false";
      vTipoFechaTipoTrabajador = "false";
      vTipoFechaReformaLaboral = "false";
      vTipoFechaLegislacion = "false";
      vTipoFechaUbicacion = "false";
      vTipoFechaAfiliacion = "false";
      vTipoFechaFormaPago = "false";
      vTipoFechaMvr = "false";
      vTipoFechaSet = "false";
      vTipoFechaNormaLaboral = "false";
      vTipoFechaDatosPersonales = "false";
      vTipoFechaEstadoCivil = "false";
      vTipoFechaCenso = "false";
      vTipoFechaEducacionFormal = "false";
      vTipoFechaEducacionNoFormal = "false";
      infoRegistro = "";
      aceptar = true;
      anchoTablaResultados = 800 + "px";

      txt_vcargo_cargo = "";
      txt_vcargo_empleadoJefe_Per = "";
      txt_vcargo_estructura_CC = "";
      txt_vcargo_estructura = "";
      txt_vcargo_motivoCam = "";
      txt_vcargo_papel = "";
      txt_vlocalizacion_locali = "";
      txt_vlocalizacion_motivo = "";
      txt_vlocalizacion_proyecto = "";
      txt_vsueldo_motivo = "";
      txt_vsueldo_tipoS = "";
      txt_vtipoContrato_ciudad = "";
      txt_vtipoContrato_motivo = "";
      txt_vtipoContrato_tipoCont = "";
      txt_vtipoTrab_tipoTrab = "";
      txt_vreformaL_ReforLab = "";
      txt_vcontrato_contrato = "";
      txt_vubicacion_ubic = "";
      txt_vubicacion_ciudad = "";
      txt_vafiliacion_estado = "";
      txt_vafiliacion_tercSuc = "";
      txt_vafiliacion_tipoEntidad = "";
      txt_vformapago_formaP = "";
      txt_vformapago_sucursal = "";
      txt_mvrs_motivo = "";
      txt_vnorma_norma = "";
      txt_vjornada_jornada = "";
      txtP_empleado_per_ciudadNac = "";
      txtP_empleado_per_ciudadDoc = "";
      txtP_empleado_per_tipoDoc = "";
      txtP_estadoCivil = "";
      txtP_idiomap_idioma = "";
      txtP_vindicador_indic = "";
      txtP_vindicador_tipoIndi = "";
      txtP_vFormal_institu = "";
      txtP_vFormal_profecion = "";
      txtP_vNoformal_curso = "";
      txtP_vNoformal_institu = "";
      txtP_experLab_SectorEco = "";
      txtP_experLab_motivo = "";
      txtP_vproyecto_proyect = "";
      txtP_vproyecto_rol = "";
      txtP_cargoPostul = "";
      txt_motivoRet = "";

      listaCodigosEmpleado = new ArrayList<BigInteger>();
      listaSecuenciasEmpleado = new ArrayList<BigInteger>();
   }

   public void cancelarModificaciones() {
      //Inicializar objeto de negocio
      parametros = new ParametrosBusquedaAvanzada();
      parametros.setParametrosBusquedaNomina(new ParametrosBusquedaNomina());

      parametros.getParametrosBusquedaNomina().setVigenciaCargo(new VigenciasCargos());
      parametros.getParametrosBusquedaNomina().getVigenciaCargo().setCargo(new Cargos());
      parametros.getParametrosBusquedaNomina().getVigenciaCargo().setEmpleadojefe(new Empleados());
      parametros.getParametrosBusquedaNomina().getVigenciaCargo().getEmpleadojefe().setPersona(new Personas());
      parametros.getParametrosBusquedaNomina().getVigenciaCargo().setEstructura(new Estructuras());
      parametros.getParametrosBusquedaNomina().getVigenciaCargo().getEstructura().setCentrocosto(new CentrosCostos());
      parametros.getParametrosBusquedaNomina().getVigenciaCargo().setMotivocambiocargo(new MotivosCambiosCargos());
      parametros.getParametrosBusquedaNomina().getVigenciaCargo().setPapel(new Papeles());

      parametros.getParametrosBusquedaNomina().setVigenciaLocalizacion(new VigenciasLocalizaciones());
      parametros.getParametrosBusquedaNomina().getVigenciaLocalizacion().setLocalizacion(new Estructuras());
      parametros.getParametrosBusquedaNomina().getVigenciaLocalizacion().setMotivo(new MotivosLocalizaciones());
      parametros.getParametrosBusquedaNomina().getVigenciaLocalizacion().setProyecto(new Proyectos());

      parametros.getParametrosBusquedaNomina().setVigenciaSueldo(new VigenciasSueldos());
      parametros.getParametrosBusquedaNomina().getVigenciaSueldo().setMotivocambiosueldo(new MotivosCambiosSueldos());
      parametros.getParametrosBusquedaNomina().getVigenciaSueldo().setTiposueldo(new TiposSueldos());
      parametros.getParametrosBusquedaNomina().getVigenciaSueldo().setUnidadpago(new Unidades());

      parametros.getParametrosBusquedaNomina().setVigenciaTipoContrato(new VigenciasTiposContratos());
      parametros.getParametrosBusquedaNomina().getVigenciaTipoContrato().setCiudad(new Ciudades());
      parametros.getParametrosBusquedaNomina().getVigenciaTipoContrato().setMotivocontrato(new MotivosContratos());
      parametros.getParametrosBusquedaNomina().getVigenciaTipoContrato().setTipocontrato(new TiposContratos());

      parametros.getParametrosBusquedaNomina().setVigenciaTipoTrabajador(new VigenciasTiposTrabajadores());
      parametros.getParametrosBusquedaNomina().getVigenciaTipoTrabajador().setTipotrabajador(new TiposTrabajadores());
      parametros.getParametrosBusquedaNomina().getVigenciaTipoTrabajador().getTipotrabajador().setTipocotizante(new TiposCotizantes());

      parametros.getParametrosBusquedaNomina().setVigenciaReformasLaboral(new VigenciasReformasLaborales());
      parametros.getParametrosBusquedaNomina().getVigenciaReformasLaboral().setReformalaboral(new ReformasLaborales());

      parametros.getParametrosBusquedaNomina().setVigenciaContrato(new VigenciasContratos());
      parametros.getParametrosBusquedaNomina().getVigenciaContrato().setContrato(new Contratos());
      parametros.getParametrosBusquedaNomina().getVigenciaContrato().setTipocontrato(new TiposContratos());

      parametros.getParametrosBusquedaNomina().setVigenciaUbicacion(new VigenciasUbicaciones());
      parametros.getParametrosBusquedaNomina().getVigenciaUbicacion().setUbicacion(new UbicacionesGeograficas());
      parametros.getParametrosBusquedaNomina().getVigenciaUbicacion().getUbicacion().setCiudad(new Ciudades());

      parametros.getParametrosBusquedaNomina().setVigenciaAfiliacion(new VigenciasAfiliaciones());
      parametros.getParametrosBusquedaNomina().getVigenciaAfiliacion().setConcepto(new Conceptos());
      parametros.getParametrosBusquedaNomina().getVigenciaAfiliacion().setEstadoafiliacion(new EstadosAfiliaciones());
      parametros.getParametrosBusquedaNomina().getVigenciaAfiliacion().setFormula(new Formulas());
      parametros.getParametrosBusquedaNomina().getVigenciaAfiliacion().setFormulacontrato(new Formulascontratos());
      parametros.getParametrosBusquedaNomina().getVigenciaAfiliacion().setTercerosucursal(new TercerosSucursales());
      parametros.getParametrosBusquedaNomina().getVigenciaAfiliacion().setTipoentidad(new TiposEntidades());
      parametros.getParametrosBusquedaNomina().getVigenciaAfiliacion().setVigenciasueldo(new VigenciasSueldos());

      parametros.getParametrosBusquedaNomina().setVigenciaFormaPago(new VigenciasFormasPagos());
      parametros.getParametrosBusquedaNomina().getVigenciaFormaPago().setFormapago(new Periodicidades());
      parametros.getParametrosBusquedaNomina().getVigenciaFormaPago().setMetodopago(new MetodosPagos());
      parametros.getParametrosBusquedaNomina().getVigenciaFormaPago().setMoneda(new Monedas());
      parametros.getParametrosBusquedaNomina().getVigenciaFormaPago().setSucursal(new Sucursales());

      parametros.getParametrosBusquedaNomina().setMvrs(new Mvrs());
      parametros.getParametrosBusquedaNomina().getMvrs().setMotivo(new Motivosmvrs());

      parametros.getParametrosBusquedaNomina().setVigenciaNormaEmpleado(new VigenciasNormasEmpleados());
      parametros.getParametrosBusquedaNomina().getVigenciaNormaEmpleado().setNormalaboral(new NormasLaborales());

      parametros.getParametrosBusquedaNomina().setVigenciaJornada(new VigenciasJornadas());
      parametros.getParametrosBusquedaNomina().getVigenciaJornada().setJornadatrabajo(new JornadasLaborales());
      parametros.getParametrosBusquedaNomina().getVigenciaJornada().setTipodescanso(new TiposDescansos());

      parametros.getParametrosBusquedaNomina().setMotivosRetiros(new MotivosRetiros());

      parametros.setParametrosBusquedaPersonal(new ParametrosBusquedaPersonal());
      parametros.getParametrosBusquedaPersonal().setEmpleado(new Empleados());
      parametros.getParametrosBusquedaPersonal().getEmpleado().setEmpresa(new Empresas());
      parametros.getParametrosBusquedaPersonal().getEmpleado().setPersona(new Personas());
      parametros.getParametrosBusquedaPersonal().getEmpleado().getPersona().setCiudaddocumento(new Ciudades());
      parametros.getParametrosBusquedaPersonal().getEmpleado().getPersona().setCiudadnacimiento(new Ciudades());
      parametros.getParametrosBusquedaPersonal().getEmpleado().getPersona().setTipodocumento(new TiposDocumentos());

      parametros.getParametrosBusquedaPersonal().setEstadoCivil(new EstadosCiviles());
      parametros.getParametrosBusquedaPersonal().setIdiomaPersona(new IdiomasPersonas());

      parametros.getParametrosBusquedaPersonal().setVigenciaIndicador(new VigenciasIndicadores());
      parametros.getParametrosBusquedaPersonal().getVigenciaIndicador().setIndicador(new Indicadores());
      parametros.getParametrosBusquedaPersonal().getVigenciaIndicador().setTipoindicador(new TiposIndicadores());

      parametros.getParametrosBusquedaPersonal().setVigenciaFormal(new VigenciasFormales());
      parametros.getParametrosBusquedaPersonal().getVigenciaFormal().setAdiestramientof(new AdiestramientosF());
      parametros.getParametrosBusquedaPersonal().getVigenciaFormal().setInstitucion(new Instituciones());
      parametros.getParametrosBusquedaPersonal().getVigenciaFormal().setPersona(new Personas());
      parametros.getParametrosBusquedaPersonal().getVigenciaFormal().setProfesion(new Profesiones());
      parametros.getParametrosBusquedaPersonal().getVigenciaFormal().setTipoeducacion(new TiposEducaciones());

      parametros.getParametrosBusquedaPersonal().setVigenciaNoFormal(new VigenciasNoFormales());
      parametros.getParametrosBusquedaPersonal().getVigenciaNoFormal().setPersona(new Personas());
      parametros.getParametrosBusquedaPersonal().getVigenciaNoFormal().setAdiestramientonf(new AdiestramientosNF());
      parametros.getParametrosBusquedaPersonal().getVigenciaNoFormal().setCurso(new Cursos());
      parametros.getParametrosBusquedaPersonal().getVigenciaNoFormal().setInstitucion(new Instituciones());

      parametros.getParametrosBusquedaPersonal().setHvExperienciaLaboral(new HvExperienciasLaborales());
      parametros.getParametrosBusquedaPersonal().getHvExperienciaLaboral().setHojadevida(new HVHojasDeVida());
      parametros.getParametrosBusquedaPersonal().getHvExperienciaLaboral().setMotivoretiro(new MotivosRetiros());
      parametros.getParametrosBusquedaPersonal().getHvExperienciaLaboral().setSectoreconomico(new SectoresEconomicos());

      parametros.getParametrosBusquedaPersonal().setVigenciaProyecto(new VigenciasProyectos());
      parametros.getParametrosBusquedaPersonal().getVigenciaProyecto().setProyecto(new Proyectos());
      parametros.getParametrosBusquedaPersonal().getVigenciaProyecto().setPryCargoproyecto(new Cargos());
      parametros.getParametrosBusquedaPersonal().getVigenciaProyecto().setPryRol(new PryRoles());

      parametros.getParametrosBusquedaPersonal().setCargo(new Cargos());
      parametros.getParametrosBusquedaPersonal().getCargo().setGruposalarial(new GruposSalariales());
      parametros.getParametrosBusquedaPersonal().getCargo().setGrupoviatico(new GruposViaticos());
      parametros.getParametrosBusquedaPersonal().getCargo().setProcesoproductivo(new ProcesosProductivos());

      parametros.getParametrosBusquedaPersonal().setIdiomaPersona(new IdiomasPersonas());
      parametros.getParametrosBusquedaPersonal().getIdiomaPersona().setIdioma(new Idiomas());

      //Inicializar variables visibilidad
      vTipoBusqueda = "Nomina"; //Por defecto al ingresar la opción sera Nomina.
      vTipoFechaCargo = "false";
      vTipoFechaCentroCosto = "false";
      vTipoFechaSueldo = "false";
      vTipoFechaTipoContrato = "false";
      vTipoFechaTipoTrabajador = "false";
      vTipoFechaReformaLaboral = "false";
      vTipoFechaLegislacion = "false";
      vTipoFechaUbicacion = "false";
      vTipoFechaAfiliacion = "false";
      vTipoFechaFormaPago = "false";
      vTipoFechaMvr = "false";
      vTipoFechaSet = "false";
      vTipoFechaNormaLaboral = "false";
      vTipoFechaDatosPersonales = "false";
      vTipoFechaEstadoCivil = "false";
      vTipoFechaCenso = "false";
      vTipoFechaEducacionFormal = "false";
      vTipoFechaEducacionNoFormal = "false";
      infoRegistro = "";
      aceptar = true;
      anchoTablaResultados = 800 + "px";

      txt_vcargo_cargo = "";
      txt_vcargo_empleadoJefe_Per = "";
      txt_vcargo_estructura_CC = "";
      txt_vcargo_estructura = "";
      txt_vcargo_motivoCam = "";
      txt_vcargo_papel = "";
      txt_vlocalizacion_locali = "";
      txt_vlocalizacion_motivo = "";
      txt_vlocalizacion_proyecto = "";
      txt_vsueldo_motivo = "";
      txt_vsueldo_tipoS = "";
      txt_vtipoContrato_ciudad = "";
      txt_vtipoContrato_motivo = "";
      txt_vtipoContrato_tipoCont = "";
      txt_vtipoTrab_tipoTrab = "";
      txt_vreformaL_ReforLab = "";
      txt_vcontrato_contrato = "";
      txt_vubicacion_ubic = "";
      txt_vubicacion_ciudad = "";
      txt_vafiliacion_estado = "";
      txt_vafiliacion_tercSuc = "";
      txt_vafiliacion_tipoEntidad = "";
      txt_vformapago_formaP = "";
      txt_vformapago_sucursal = "";
      txt_mvrs_motivo = "";
      txt_vnorma_norma = "";
      txt_vjornada_jornada = "";
      txtP_empleado_per_ciudadNac = "";
      txtP_empleado_per_ciudadDoc = "";
      txtP_empleado_per_tipoDoc = "";
      txtP_estadoCivil = "";
      txtP_idiomap_idioma = "";
      txtP_vindicador_indic = "";
      txtP_vindicador_tipoIndi = "";
      txtP_vFormal_institu = "";
      txtP_vFormal_profecion = "";
      txtP_vNoformal_curso = "";
      txtP_vNoformal_institu = "";
      txtP_experLab_SectorEco = "";
      txtP_experLab_motivo = "";
      txtP_vproyecto_proyect = "";
      txtP_vproyecto_rol = "";
      txtP_cargoPostul = "";
      txt_motivoRet = "";

      listaCodigosEmpleado = new ArrayList<BigInteger>();
      listaSecuenciasEmpleado = new ArrayList<BigInteger>();
      mapParametros.put("paginaAnterior", paginaAnterior);
   }

   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

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
         System.out.println("navegar('Atras') : " + pag);
      } else {
         String pagActual = "busquedaavanzada";
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
         controlListaNavegacion.guardarNavegacion(pagActual, pag);
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
         administrarBusquedaAvanzada.obtenerConexion(ses.getId());
      } catch (Exception e) {
         System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
         System.out.println("Causa: " + e.getCause());
      }
   }

   public void activarAceptar() {
      aceptar = false;
   }

   //METODOS AUTOCOMPLETAR
   public void autocompletarParametros(String cualParametro, String valor) {
      System.out.println("autocompletarParametros() valor : __" + valor + "__");
      int coincidencias = 0;
      int indiceUnicoElemento = 0;

      if (valorPorDefecto != null) {
         if (!valorPorDefecto.isEmpty()) {
            valorPorDefecto = "";
         }
      }

      if (cualParametro.equals("CARGO")) {
         parametros.getParametrosBusquedaNomina().getVigenciaCargo().setCargo(new Cargos());
         if (lovCargos == null) {
            requerirLOV(cualParametro);
         }

         if (valor.equals("") || valor.isEmpty()) {
            txt_vcargo_cargo = "";
         } else if (lovCargos != null) {
            for (int i = 0; i < lovCargos.size(); i++) {
               if (lovCargos.get(i).getNombre().startsWith(valor.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               parametros.getParametrosBusquedaNomina().getVigenciaCargo().setCargo(lovCargos.get(indiceUnicoElemento));
               txt_vcargo_cargo = parametros.getParametrosBusquedaNomina().getVigenciaCargo().getCargo().getNombre();
            } else {
               txt_vcargo_cargo = "";
               RequestContext.getCurrentInstance().update("formularioDialogos:DialogoCargos");
               RequestContext.getCurrentInstance().execute("PF('DialogoCargos').show()");
            }
         } else {
            RequestContext.getCurrentInstance().execute("PF('errorListaDialogo').show()");
         }
      } else if (cualParametro.equals("ESTRUCTURA")) {
         parametros.getParametrosBusquedaNomina().getVigenciaCargo().setEstructura(new Estructuras());
         parametros.getParametrosBusquedaNomina().getVigenciaCargo().getEstructura().setCentrocosto(new CentrosCostos());
         if (lovEstructuras == null) {
            requerirLOV(cualParametro);
         }
         if (valor.equals("") || valor.isEmpty()) {
            txt_vcargo_estructura_CC = "";
            txt_vcargo_estructura = "";
         } else if (lovEstructuras != null) {
            for (int i = 0; i < lovEstructuras.size(); i++) {
               if (lovEstructuras.get(i).getNombre().startsWith(valor.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               parametros.getParametrosBusquedaNomina().getVigenciaCargo().setEstructura(lovEstructuras.get(indiceUnicoElemento));
               txt_vcargo_estructura_CC = parametros.getParametrosBusquedaNomina().getVigenciaCargo().getEstructura().getCentrocosto().getNombre();
               txt_vcargo_estructura = parametros.getParametrosBusquedaNomina().getVigenciaCargo().getEstructura().getNombre();
            } else {
               txt_vcargo_estructura_CC = "";
               txt_vcargo_estructura = "";
               RequestContext.getCurrentInstance().update("formularioDialogos:EstructuraDialogo");
               RequestContext.getCurrentInstance().execute("PF('EstructuraDialogo').show()");
            }
         } else {
            RequestContext.getCurrentInstance().execute("PF('errorListaDialogo').show()");
         }
      } else if (cualParametro.equals("JEFE")) {
         parametros.getParametrosBusquedaNomina().getVigenciaCargo().setEmpleadojefe(new Empleados());
         if (lovJefe == null) {
            requerirLOV(cualParametro);
         }

         if (valor.equals("") || valor.isEmpty()) {
            txt_vcargo_empleadoJefe_Per = "";
         } else if (lovJefe != null) {
            for (int i = 0; i < lovJefe.size(); i++) {
               if (lovJefe.get(i).getPersona().getNombreCompleto().startsWith(valor.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               parametros.getParametrosBusquedaNomina().getVigenciaCargo().setEmpleadojefe(lovJefe.get(indiceUnicoElemento));
               txt_vcargo_empleadoJefe_Per = parametros.getParametrosBusquedaNomina().getVigenciaCargo().getEmpleadojefe().getPersona().getNombreCompleto();
            } else {
               txt_vcargo_empleadoJefe_Per = "";
               RequestContext.getCurrentInstance().update("formularioDialogos:JefeDialogo");
               RequestContext.getCurrentInstance().execute("PF('JefeDialogo').show()");
            }
         } else {
            RequestContext.getCurrentInstance().execute("PF('errorListaDialogo').show()");
         }
      } else if (cualParametro.equals("MOTIVOCARGO")) {
         parametros.getParametrosBusquedaNomina().getVigenciaCargo().setMotivocambiocargo(new MotivosCambiosCargos());
         if (lovMotivosCargos == null) {
            requerirLOV(cualParametro);
         }

         if (valor.equals("") || valor.isEmpty()) {
            txt_vcargo_motivoCam = "";
         } else if (lovMotivosCargos != null) {
            for (int i = 0; i < lovMotivosCargos.size(); i++) {
               if (lovMotivosCargos.get(i).getNombre().startsWith(valor.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               parametros.getParametrosBusquedaNomina().getVigenciaCargo().setMotivocambiocargo(lovMotivosCargos.get(indiceUnicoElemento));
               txt_vcargo_motivoCam = parametros.getParametrosBusquedaNomina().getVigenciaCargo().getMotivocambiocargo().getNombre();
            } else {
               txt_vcargo_motivoCam = "";
               RequestContext.getCurrentInstance().update("formularioDialogos:MotivoCargoDialogo");
               RequestContext.getCurrentInstance().execute("PF('MotivoCargoDialogo').show()");
            }
         } else {
            RequestContext.getCurrentInstance().execute("PF('errorListaDialogo').show()");
         }
      } else if (cualParametro.equals("PAPEL")) {
         parametros.getParametrosBusquedaNomina().getVigenciaCargo().setPapel(new Papeles());
         if (lovPapeles == null) {
            requerirLOV(cualParametro);
         }

         if (lovPapeles != null) {
            for (int i = 0; i < lovPapeles.size(); i++) {
               if (lovPapeles.get(i).getDescripcion().startsWith(valor.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               parametros.getParametrosBusquedaNomina().getVigenciaCargo().setPapel(lovPapeles.get(indiceUnicoElemento));
               txt_vcargo_papel = parametros.getParametrosBusquedaNomina().getVigenciaCargo().getPapel().getDescripcion();
            } else {
               txt_vcargo_papel = "";
               RequestContext.getCurrentInstance().update("formularioDialogos:PapelCargoDialogo");
               RequestContext.getCurrentInstance().execute("PF('PapelCargoDialogo').show()");
            }
         } else {
            RequestContext.getCurrentInstance().execute("PF('errorListaDialogo').show()");
         }
      } else if (cualParametro.equals("LOCALIZACION")) {
         parametros.getParametrosBusquedaNomina().getVigenciaLocalizacion().setLocalizacion(new Estructuras());
         if (lovEstructuras == null) {
            requerirLOV(cualParametro);
         }

         if (valor.equals("") || valor.isEmpty()) {
            txt_vlocalizacion_locali = "";
         } else if (lovEstructuras != null) {
            for (int i = 0; i < lovEstructuras.size(); i++) {
               if (lovEstructuras.get(i).getNombre().startsWith(valor.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               parametros.getParametrosBusquedaNomina().getVigenciaLocalizacion().setLocalizacion(lovEstructuras.get(indiceUnicoElemento));
               txt_vlocalizacion_locali = parametros.getParametrosBusquedaNomina().getVigenciaLocalizacion().getLocalizacion().getNombre();
            } else {
               txt_vlocalizacion_locali = "";
               RequestContext.getCurrentInstance().update("formularioDialogos:LocalizacionCentroCostoDialogo");
               RequestContext.getCurrentInstance().execute("PF('LocalizacionCentroCostoDialogo').show()");
            }
         } else {
            RequestContext.getCurrentInstance().execute("PF('errorListaDialogo').show()");
         }
      } else if (cualParametro.equals("MOTIVOLOCALIZACION")) {
         parametros.getParametrosBusquedaNomina().getVigenciaLocalizacion().setMotivo(new MotivosLocalizaciones());
         if (lovMotivosLocalizaciones == null) {
            requerirLOV(cualParametro);
         }

         if (valor.equals("") || valor.isEmpty()) {
            txt_vlocalizacion_motivo = "";
         } else if (lovMotivosLocalizaciones != null) {
            for (int i = 0; i < lovMotivosLocalizaciones.size(); i++) {
               if (lovMotivosLocalizaciones.get(i).getDescripcion().startsWith(valor.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               parametros.getParametrosBusquedaNomina().getVigenciaLocalizacion().setMotivo(lovMotivosLocalizaciones.get(indiceUnicoElemento));
               txt_vlocalizacion_motivo = parametros.getParametrosBusquedaNomina().getVigenciaLocalizacion().getMotivo().getDescripcion();
            } else {
               txt_vlocalizacion_motivo = "";
               RequestContext.getCurrentInstance().update("formularioDialogos:MotivoCentroCostoDialogo");
               RequestContext.getCurrentInstance().execute("PF('MotivoCentroCostoDialogo').show()");
            }
         } else {
            RequestContext.getCurrentInstance().execute("PF('errorListaDialogo').show()");
         }
      } else if (cualParametro.equals("TIPOSUELDO")) {
         parametros.getParametrosBusquedaNomina().getVigenciaSueldo().setTiposueldo(new TiposSueldos());
         if (lovTiposSueldos == null) {
            requerirLOV(cualParametro);
         }

         if (valor.equals("") || valor.isEmpty()) {
            txt_vsueldo_tipoS = "";
         } else if (lovTiposSueldos != null) {
            for (int i = 0; i < lovTiposSueldos.size(); i++) {
               if (lovTiposSueldos.get(i).getDescripcion().startsWith(valor.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               parametros.getParametrosBusquedaNomina().getVigenciaSueldo().setTiposueldo(lovTiposSueldos.get(indiceUnicoElemento));
               txt_vsueldo_tipoS = parametros.getParametrosBusquedaNomina().getVigenciaSueldo().getTiposueldo().getDescripcion();
            } else {
               txt_vsueldo_tipoS = "";
               RequestContext.getCurrentInstance().update("formularioDialogos:TipoSueldoSueldoDialogo");
               RequestContext.getCurrentInstance().execute("PF('TipoSueldoSueldoDialogo').show()");
            }
         } else {
            RequestContext.getCurrentInstance().execute("PF('errorListaDialogo').show()");
         }
      } else if (cualParametro.equals("MOTIVOSUELDO")) {
         parametros.getParametrosBusquedaNomina().getVigenciaSueldo().setMotivocambiosueldo(new MotivosCambiosSueldos());
         if (lovMotivosSueldos == null) {
            requerirLOV(cualParametro);
         }

         if (valor.equals("") || valor.isEmpty()) {
            txt_vsueldo_motivo = "";
         } else if (lovMotivosSueldos != null) {
            for (int i = 0; i < lovMotivosSueldos.size(); i++) {
               if (lovMotivosSueldos.get(i).getNombre().startsWith(valor.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               parametros.getParametrosBusquedaNomina().getVigenciaSueldo().setMotivocambiosueldo(lovMotivosSueldos.get(indiceUnicoElemento));
               txt_vsueldo_motivo = parametros.getParametrosBusquedaNomina().getVigenciaSueldo().getMotivocambiosueldo().getNombre();
            } else {
               txt_vsueldo_motivo = "";
               RequestContext.getCurrentInstance().update("formularioDialogos:MotivoSueldoDialogo");
               RequestContext.getCurrentInstance().execute("PF('MotivoSueldoDialogo').show()");
            }
         } else {
            RequestContext.getCurrentInstance().execute("PF('errorListaDialogo').show()");
         }
      } else if (cualParametro.equals("TIPOCONTRATO")) {
         parametros.getParametrosBusquedaNomina().getVigenciaTipoContrato().setTipocontrato(new TiposContratos());
         if (lovTiposContratos == null) {
            requerirLOV(cualParametro);
         }

         if (valor.equals("") || valor.isEmpty()) {
            txt_vtipoContrato_tipoCont = "";
         } else if (lovTiposContratos != null) {
            for (int i = 0; i < lovTiposContratos.size(); i++) {
               if (lovTiposContratos.get(i).getNombre().startsWith(valor.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               parametros.getParametrosBusquedaNomina().getVigenciaTipoContrato().setTipocontrato(lovTiposContratos.get(indiceUnicoElemento));
               txt_vtipoContrato_tipoCont = parametros.getParametrosBusquedaNomina().getVigenciaTipoContrato().getTipocontrato().getNombre();
            } else {
               txt_vtipoContrato_tipoCont = "";
               RequestContext.getCurrentInstance().update("formularioDialogos:TipoContratoFechaContratoDialogo");
               RequestContext.getCurrentInstance().execute("PF('TipoContratoFechaContratoDialogo').show()");
            }
         } else {
            RequestContext.getCurrentInstance().execute("PF('errorListaDialogo').show()");
         }
      } else if (cualParametro.equals("MOTIVOCONTRATO")) {
         parametros.getParametrosBusquedaNomina().getVigenciaTipoContrato().setMotivocontrato(new MotivosContratos());
         if (lovMotivosContratos == null) {
            requerirLOV(cualParametro);
         }

         if (valor.equals("") || valor.isEmpty()) {
            txt_vtipoContrato_motivo = "";
         } else if (lovMotivosContratos != null) {
            for (int i = 0; i < lovMotivosContratos.size(); i++) {
               if (lovMotivosContratos.get(i).getNombre().startsWith(valor.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               parametros.getParametrosBusquedaNomina().getVigenciaTipoContrato().setMotivocontrato(lovMotivosContratos.get(indiceUnicoElemento));
               txt_vtipoContrato_motivo = parametros.getParametrosBusquedaNomina().getVigenciaTipoContrato().getMotivocontrato().getNombre();
            } else {
               txt_vtipoContrato_motivo = "";
               RequestContext.getCurrentInstance().update("formularioDialogos:MotivoFechaContratoDialogo");
               RequestContext.getCurrentInstance().execute("PF('MotivoFechaContratoDialogo').show()");
            }
         } else {
            RequestContext.getCurrentInstance().execute("PF('errorListaDialogo').show()");
         }
      } else if (cualParametro.equals("TIPOTRABAJADOR")) {
         parametros.getParametrosBusquedaNomina().getVigenciaTipoTrabajador().setTipotrabajador(new TiposTrabajadores());
         if (lovTiposTrabajadores == null) {
            requerirLOV(cualParametro);
         }

         if (valor.equals("") || valor.isEmpty()) {
            txt_vtipoTrab_tipoTrab = "";
         } else if (lovTiposTrabajadores != null) {
            for (int i = 0; i < lovTiposTrabajadores.size(); i++) {
               if (lovTiposTrabajadores.get(i).getNombre().startsWith(valor.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               parametros.getParametrosBusquedaNomina().getVigenciaTipoTrabajador().setTipotrabajador(lovTiposTrabajadores.get(indiceUnicoElemento));
               txt_vtipoTrab_tipoTrab = parametros.getParametrosBusquedaNomina().getVigenciaTipoTrabajador().getTipotrabajador().getNombre();
            } else {
               txt_vtipoTrab_tipoTrab = "";
               RequestContext.getCurrentInstance().update("formularioDialogos:TipoTrabajadorTipoTrabajadorDialogo");
               RequestContext.getCurrentInstance().execute("PF('TipoTrabajadorTipoTrabajadorDialogo').show()");
            }
         } else {
            RequestContext.getCurrentInstance().execute("PF('errorListaDialogo').show()");
         }
      } else if (cualParametro.equals("REFORMALABORAL")) {
         parametros.getParametrosBusquedaNomina().getVigenciaReformasLaboral().setReformalaboral(new ReformasLaborales());
         if (lovReformasLaborales == null) {
            requerirLOV(cualParametro);
         }

         if (valor.equals("") || valor.isEmpty()) {
            txt_vreformaL_ReforLab = "";
         } else if (lovReformasLaborales != null) {
            for (int i = 0; i < lovReformasLaborales.size(); i++) {
               if (lovReformasLaborales.get(i).getNombre().startsWith(valor.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               parametros.getParametrosBusquedaNomina().getVigenciaReformasLaboral().setReformalaboral(lovReformasLaborales.get(indiceUnicoElemento));
               txt_vreformaL_ReforLab = parametros.getParametrosBusquedaNomina().getVigenciaReformasLaboral().getReformalaboral().getNombre();
            } else {
               txt_vreformaL_ReforLab = "";
               RequestContext.getCurrentInstance().update("formularioDialogos:ReformaLaboralTipoSalarioDialogo");
               RequestContext.getCurrentInstance().execute("PF('ReformaLaboralTipoSalarioDialogo').show()");
            }
         } else {
            RequestContext.getCurrentInstance().execute("PF('errorListaDialogo').show()");
         }
      } else if (cualParametro.equals("CONTRATO")) {
         parametros.getParametrosBusquedaNomina().getVigenciaContrato().setContrato(new Contratos());
         if (lovContratos == null) {
            requerirLOV(cualParametro);
         }

         if (valor.equals("") || valor.isEmpty()) {
            txt_vcontrato_contrato = "";
         } else if (lovContratos != null) {
            for (int i = 0; i < lovContratos.size(); i++) {
               if (lovContratos.get(i).getDescripcion().startsWith(valor.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               parametros.getParametrosBusquedaNomina().getVigenciaContrato().setContrato(lovContratos.get(indiceUnicoElemento));
               txt_vcontrato_contrato = parametros.getParametrosBusquedaNomina().getVigenciaContrato().getContrato().getDescripcion();
            } else {
               txt_vcontrato_contrato = "";
               RequestContext.getCurrentInstance().update("formularioDialogos:LegislacionLegislacionLaboralDialogo");
               RequestContext.getCurrentInstance().execute("PF('LegislacionLegislacionLaboralDialogo').show()");
            }
         } else {
            RequestContext.getCurrentInstance().execute("PF('errorListaDialogo').show()");
         }
      } else if (cualParametro.equals("UBICACION")) {
         parametros.getParametrosBusquedaNomina().getVigenciaUbicacion().setUbicacion(new UbicacionesGeograficas());
         if (lovUbicaciones == null) {
            requerirLOV(cualParametro);
         }

         if (valor.equals("") || valor.isEmpty()) {
            txt_vubicacion_ubic = "";
            txt_vubicacion_ciudad = "";
         } else if (lovUbicaciones != null) {
            for (int i = 0; i < lovUbicaciones.size(); i++) {
               if (lovUbicaciones.get(i).getDescripcion().startsWith(valor.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               parametros.getParametrosBusquedaNomina().getVigenciaUbicacion().setUbicacion(lovUbicaciones.get(indiceUnicoElemento));
               txt_vubicacion_ubic = parametros.getParametrosBusquedaNomina().getVigenciaUbicacion().getUbicacion().getDescripcion();
               txt_vubicacion_ciudad = parametros.getParametrosBusquedaNomina().getVigenciaUbicacion().getUbicacion().getCiudad().getNombre();
            } else {
               txt_vubicacion_ubic = "";
               txt_vubicacion_ciudad = "";
               RequestContext.getCurrentInstance().update("formularioDialogos:UbicacionUbicacionDialogo");
               RequestContext.getCurrentInstance().execute("PF('UbicacionUbicacionDialogo').show()");
            }
         } else {
            RequestContext.getCurrentInstance().execute("PF('errorListaDialogo').show()");
         }
      } else if (cualParametro.equals("TERCEROSUCURSAL")) {
         parametros.getParametrosBusquedaNomina().getVigenciaAfiliacion().setTercerosucursal(new TercerosSucursales());
         if (lovTercerosSucursales == null) {
            requerirLOV(cualParametro);
         }

         if (valor.equals("") || valor.isEmpty()) {
            txt_vafiliacion_tercSuc = "";
         } else if (lovTercerosSucursales != null) {
            for (int i = 0; i < lovTercerosSucursales.size(); i++) {
               if (lovTercerosSucursales.get(i).getDescripcion().startsWith(valor.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               parametros.getParametrosBusquedaNomina().getVigenciaAfiliacion().setTercerosucursal(lovTercerosSucursales.get(indiceUnicoElemento));
               txt_vafiliacion_tercSuc = parametros.getParametrosBusquedaNomina().getVigenciaAfiliacion().getTercerosucursal().getDescripcion();
            } else {
               txt_vafiliacion_tercSuc = "";
               RequestContext.getCurrentInstance().update("formularioDialogos:TerceroAfiliacionDialogo");
               RequestContext.getCurrentInstance().execute("PF('TerceroAfiliacionDialogo').show()");
            }
         } else {
            RequestContext.getCurrentInstance().execute("PF('errorListaDialogo').show()");
         }
      } else if (cualParametro.equals("TIPOENTIDAD")) {
         parametros.getParametrosBusquedaNomina().getVigenciaAfiliacion().setTipoentidad(new TiposEntidades());
         if (lovTiposEntidades == null) {
            requerirLOV(cualParametro);
         }

         if (valor.equals("") || valor.isEmpty()) {
            txt_vafiliacion_tipoEntidad = "";
         } else if (lovTiposEntidades != null) {
            for (int i = 0; i < lovTiposEntidades.size(); i++) {
               if (lovTiposEntidades.get(i).getNombre().startsWith(valor.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               parametros.getParametrosBusquedaNomina().getVigenciaAfiliacion().setTipoentidad(lovTiposEntidades.get(indiceUnicoElemento));
               txt_vafiliacion_tipoEntidad = parametros.getParametrosBusquedaNomina().getVigenciaAfiliacion().getTipoentidad().getNombre();
            } else {
               txt_vafiliacion_tipoEntidad = "";
               RequestContext.getCurrentInstance().update("formularioDialogos:TipoEntidadAfiliacionDialogo");
               RequestContext.getCurrentInstance().execute("PF('TipoEntidadAfiliacionDialogo').show()");
            }
         } else {
            RequestContext.getCurrentInstance().execute("PF('errorListaDialogo').show()");
         }
      } else if (cualParametro.equals("ESTADOAFILIACION")) {
         parametros.getParametrosBusquedaNomina().getVigenciaAfiliacion().setEstadoafiliacion(new EstadosAfiliaciones());
         if (lovEstadosAfiliaciones == null) {
            requerirLOV(cualParametro);
         }

         if (valor.equals("") || valor.isEmpty()) {
            txt_vafiliacion_estado = "";
         } else if (lovEstadosAfiliaciones != null) {
            for (int i = 0; i < lovEstadosAfiliaciones.size(); i++) {
               if (lovEstadosAfiliaciones.get(i).getNombre().startsWith(valor.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               parametros.getParametrosBusquedaNomina().getVigenciaAfiliacion().setEstadoafiliacion(lovEstadosAfiliaciones.get(indiceUnicoElemento));
               txt_vafiliacion_estado = parametros.getParametrosBusquedaNomina().getVigenciaAfiliacion().getEstadoafiliacion().getNombre();
            } else {
               txt_vafiliacion_estado = "";
               RequestContext.getCurrentInstance().update("formularioDialogos:EstadoAfiliacionDialogo");
               RequestContext.getCurrentInstance().execute("PF('EstadoAfiliacionDialogo').show()");
            }
         } else {
            RequestContext.getCurrentInstance().execute("PF('errorListaDialogo').show()");
         }
      } else if (cualParametro.equals("FORMAPAGO")) {
         parametros.getParametrosBusquedaNomina().getVigenciaFormaPago().setFormapago(new Periodicidades());
         if (lovFormasPagos == null) {
            requerirLOV(cualParametro);
         }

         if (valor.equals("") || valor.isEmpty()) {
            txt_vformapago_formaP = "";
         } else if (lovFormasPagos != null) {
            for (int i = 0; i < lovFormasPagos.size(); i++) {
               if (lovFormasPagos.get(i).getNombre().startsWith(valor.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               parametros.getParametrosBusquedaNomina().getVigenciaFormaPago().setFormapago(lovFormasPagos.get(indiceUnicoElemento));
               txt_vformapago_formaP = parametros.getParametrosBusquedaNomina().getVigenciaFormaPago().getFormapago().getNombre();
            } else {
               txt_vformapago_formaP = "";
               RequestContext.getCurrentInstance().update("formularioDialogos:PeriodicidadFormaPagoDialogo");
               RequestContext.getCurrentInstance().execute("PF('PeriodicidadFormaPagoDialogo').show()");
            }
         } else {
            RequestContext.getCurrentInstance().execute("PF('errorListaDialogo').show()");
         }
      } else if (cualParametro.equals("SUCURSAL")) {
         parametros.getParametrosBusquedaNomina().getVigenciaFormaPago().setSucursal(new Sucursales());
         if (lovSucursales == null) {
            requerirLOV(cualParametro);
         }

         if (valor.equals("") || valor.isEmpty()) {
            txt_vformapago_sucursal = "";
         } else if (lovSucursales != null) {
            for (int i = 0; i < lovSucursales.size(); i++) {
               if (lovSucursales.get(i).getNombre().startsWith(valor.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               parametros.getParametrosBusquedaNomina().getVigenciaFormaPago().setSucursal(lovSucursales.get(indiceUnicoElemento));
               txt_vformapago_sucursal = parametros.getParametrosBusquedaNomina().getVigenciaFormaPago().getSucursal().getNombre();
            } else {
               txt_vformapago_sucursal = "";
               RequestContext.getCurrentInstance().update("formularioDialogos:SucursalFormaPagoDialogo");
               RequestContext.getCurrentInstance().execute("PF('SucursalFormaPagoDialogo').show()");
            }
         } else {
            RequestContext.getCurrentInstance().execute("PF('errorListaDialogo').show()");
         }
      } else if (cualParametro.equals("MOTIVOMVR")) {
         parametros.getParametrosBusquedaNomina().getMvrs().setMotivo(new Motivosmvrs());
         if (lovMotivosMvrs == null) {
            requerirLOV(cualParametro);
         }

         if (valor.equals("") || valor.isEmpty()) {
            txt_mvrs_motivo = "";
         } else if (lovMotivosMvrs != null) {
            for (int i = 0; i < lovMotivosMvrs.size(); i++) {
               if (lovMotivosMvrs.get(i).getNombre().startsWith(valor.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               parametros.getParametrosBusquedaNomina().getMvrs().setMotivo(lovMotivosMvrs.get(indiceUnicoElemento));
               txt_mvrs_motivo = parametros.getParametrosBusquedaNomina().getMvrs().getMotivo().getNombre();
            } else {
               txt_mvrs_motivo = "";
               RequestContext.getCurrentInstance().update("formularioDialogos:MotivoMvrsDialogo");
               RequestContext.getCurrentInstance().execute("PF('MotivoMvrsDialogo').show()");
            }
         } else {
            RequestContext.getCurrentInstance().execute("PF('errorListaDialogo').show()");
         }
      } else if (cualParametro.equals("NORMALABORAL")) {
         parametros.getParametrosBusquedaNomina().getVigenciaNormaEmpleado().setNormalaboral(new NormasLaborales());
         if (lovNormasLaborales == null) {
            requerirLOV(cualParametro);
         }

         if (valor.equals("") || valor.isEmpty()) {
            txt_vnorma_norma = "";
         } else if (lovNormasLaborales != null) {
            for (int i = 0; i < lovNormasLaborales.size(); i++) {
               if (lovNormasLaborales.get(i).getNombre().startsWith(valor.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               parametros.getParametrosBusquedaNomina().getVigenciaNormaEmpleado().setNormalaboral(lovNormasLaborales.get(indiceUnicoElemento));
               txt_vnorma_norma = parametros.getParametrosBusquedaNomina().getVigenciaNormaEmpleado().getNormalaboral().getNombre();
            } else {
               txt_vnorma_norma = "";
               RequestContext.getCurrentInstance().update("formularioDialogos:NormaLaboralNormaLaboralDialogo");
               RequestContext.getCurrentInstance().execute("PF('NormaLaboralNormaLaboralDialogo').show()");
            }
         } else {
            RequestContext.getCurrentInstance().execute("PF('errorListaDialogo').show()");
         }
      } else if (cualParametro.equals("JORNADALABORAL")) {
         parametros.getParametrosBusquedaNomina().getVigenciaJornada().setJornadatrabajo(new JornadasLaborales());
         if (lovJornadasLaborales == null) {
            requerirLOV(cualParametro);
         }

         if (valor.equals("") || valor.isEmpty()) {
            txt_vjornada_jornada = "";
         } else if (lovJornadasLaborales != null) {
            for (int i = 0; i < lovJornadasLaborales.size(); i++) {
               if (lovJornadasLaborales.get(i).getDescripcion().startsWith(valor.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               parametros.getParametrosBusquedaNomina().getVigenciaJornada().setJornadatrabajo(lovJornadasLaborales.get(indiceUnicoElemento));
               txt_vjornada_jornada = parametros.getParametrosBusquedaNomina().getVigenciaJornada().getJornadatrabajo().getDescripcion();
            } else {
               txt_vjornada_jornada = "";
               RequestContext.getCurrentInstance().update("formularioDialogos:JornadaJornadaLaboralDialogo");
               RequestContext.getCurrentInstance().execute("PF('JornadaJornadaLaboralDialogo').show()");
            }
         } else {
            RequestContext.getCurrentInstance().execute("PF('errorListaDialogo').show()");
         }
      } else if (cualParametro.equals("MOTIVORETIRO")) {
         parametros.getParametrosBusquedaNomina().setMotivosRetiros(new MotivosRetiros());
         if (lovMotivosRetiros == null) {
            requerirLOV(cualParametro);
         }

         if (valor.equals("") || valor.isEmpty()) {
            txt_motivoRet = "";
         } else if (lovMotivosRetiros != null) {
            for (int i = 0; i < lovMotivosRetiros.size(); i++) {
               if (lovMotivosRetiros.get(i).getNombre().startsWith(valor.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               parametros.getParametrosBusquedaNomina().setMotivosRetiros(lovMotivosRetiros.get(indiceUnicoElemento));
               txt_motivoRet = parametros.getParametrosBusquedaNomina().getMotivosRetiros().getNombre();
            } else {
               txt_motivoRet = "";
               RequestContext.getCurrentInstance().update("formularioDialogos:MotivoFechaRetiroDialogo");
               RequestContext.getCurrentInstance().execute("PF('MotivoFechaRetiroDialogo').show()");
            }
         } else {
            RequestContext.getCurrentInstance().execute("PF('errorListaDialogo').show()");
         }
      } else if (cualParametro.equals("CIUDADNACIMIENDO")) {
         parametros.getParametrosBusquedaPersonal().getEmpleado().getPersona().setCiudadnacimiento(new Ciudades());
         if (lovCiudades == null) {
            requerirLOV("CIUDAD");
         }

         if (valor.equals("") || valor.isEmpty()) {
            txtP_empleado_per_ciudadNac = "";
         } else if (lovCiudades != null) {
            for (int i = 0; i < lovCiudades.size(); i++) {
               if (lovCiudades.get(i).getNombre().startsWith(valor.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               parametros.getParametrosBusquedaPersonal().getEmpleado().getPersona().setCiudadnacimiento(lovCiudades.get(indiceUnicoElemento));
               txtP_empleado_per_ciudadNac = parametros.getParametrosBusquedaPersonal().getEmpleado().getPersona().getCiudadnacimiento().getNombre();
            } else {
               txtP_empleado_per_ciudadNac = "";
               RequestContext.getCurrentInstance().update("formularioDialogos:CiudadNacimientoDatosPersonalesDialogo");
               RequestContext.getCurrentInstance().execute("PF('CiudadNacimientoDatosPersonalesDialogo').show()");
            }
         } else {
            RequestContext.getCurrentInstance().execute("PF('errorListaDialogo').show()");
         }
      } else if (cualParametro.equals("CIUDADDOCUMENTO")) {
         parametros.getParametrosBusquedaPersonal().getEmpleado().getPersona().setCiudaddocumento(new Ciudades());
         if (lovCiudades == null) {
            requerirLOV("CIUDAD");
         }

         if (valor.equals("") || valor.isEmpty()) {
            txtP_empleado_per_ciudadDoc = "";
         } else if (lovCiudades != null) {
            for (int i = 0; i < lovCiudades.size(); i++) {
               if (lovCiudades.get(i).getNombre().startsWith(valor.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               parametros.getParametrosBusquedaPersonal().getEmpleado().getPersona().setCiudaddocumento(lovCiudades.get(indiceUnicoElemento));
               txtP_empleado_per_ciudadDoc = parametros.getParametrosBusquedaPersonal().getEmpleado().getPersona().getCiudaddocumento().getNombre();
            } else {
               txtP_empleado_per_ciudadDoc = "";
               RequestContext.getCurrentInstance().update("formularioDialogos:CiudadDocumentoDatosPersonalesDialogo");
               RequestContext.getCurrentInstance().execute("PF('CiudadDocumentoDatosPersonalesDialogo').show()");
            }
         } else {
            RequestContext.getCurrentInstance().execute("PF('errorListaDialogo').show()");
         }
      } else if (cualParametro.equals("ESTADOCIVIL")) {
         parametros.getParametrosBusquedaPersonal().setEstadoCivil(new EstadosCiviles());
         if (lovEstadosCiviles == null) {
            requerirLOV(cualParametro);
         }

         if (valor.equals("") || valor.isEmpty()) {
            txtP_estadoCivil = "";
         } else if (lovEstadosCiviles != null) {
            for (int i = 0; i < lovEstadosCiviles.size(); i++) {
               if (lovEstadosCiviles.get(i).getDescripcion().startsWith(valor.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               parametros.getParametrosBusquedaPersonal().setEstadoCivil(lovEstadosCiviles.get(indiceUnicoElemento));
               txtP_estadoCivil = parametros.getParametrosBusquedaPersonal().getEstadoCivil().getDescripcion();
            } else {
               txtP_estadoCivil = "";
               RequestContext.getCurrentInstance().update("formularioDialogos:EstadoCivilEstadoCivilDialogo");
               RequestContext.getCurrentInstance().execute("PF('EstadoCivilEstadoCivilDialogo').show()");
            }
         } else {
            RequestContext.getCurrentInstance().execute("PF('errorListaDialogo').show()");
         }
      } else if (cualParametro.equals("IDIOMA")) {
         parametros.getParametrosBusquedaPersonal().getIdiomaPersona().setIdioma(new Idiomas());
         if (lovIdiomas == null) {
            requerirLOV(cualParametro);
         }

         if (valor.equals("") || valor.isEmpty()) {
            txtP_idiomap_idioma = "";
         } else if (lovIdiomas != null) {
            for (int i = 0; i < lovIdiomas.size(); i++) {
               if (lovIdiomas.get(i).getNombre().startsWith(valor.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               parametros.getParametrosBusquedaPersonal().getIdiomaPersona().setIdioma(lovIdiomas.get(indiceUnicoElemento));
               txtP_idiomap_idioma = parametros.getParametrosBusquedaPersonal().getIdiomaPersona().getIdioma().getNombre();
            } else {
               txtP_idiomap_idioma = "";
               RequestContext.getCurrentInstance().update("formularioDialogos:IdiomaIdiomaDialogo");
               RequestContext.getCurrentInstance().execute("PF('IdiomaIdiomaDialogo').show()");
            }
         } else {
            RequestContext.getCurrentInstance().execute("PF('errorListaDialogo').show()");
         }
      } else if (cualParametro.equals("TIPOINDICADOR")) {
         parametros.getParametrosBusquedaPersonal().getVigenciaIndicador().setTipoindicador(new TiposIndicadores());
         if (lovTiposIndicadores == null) {
            requerirLOV(cualParametro);
         }

         if (valor.equals("") || valor.isEmpty()) {
            txtP_vindicador_tipoIndi = "";
         } else if (lovTiposIndicadores != null) {
            for (int i = 0; i < lovTiposIndicadores.size(); i++) {
               if (lovTiposIndicadores.get(i).getDescripcion().startsWith(valor.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               parametros.getParametrosBusquedaPersonal().getVigenciaIndicador().setTipoindicador(lovTiposIndicadores.get(indiceUnicoElemento));
               txtP_vindicador_tipoIndi = parametros.getParametrosBusquedaPersonal().getVigenciaIndicador().getTipoindicador().getDescripcion();
            } else {
               txtP_vindicador_tipoIndi = "";
               RequestContext.getCurrentInstance().update("formularioDialogos:TipoIndicadorCensoDialogo");
               RequestContext.getCurrentInstance().execute("PF('TipoIndicadorCensoDialogo').show()");
            }
         } else {
            RequestContext.getCurrentInstance().execute("PF('errorListaDialogo').show()");
         }
      } else if (cualParametro.equals("INDICADOR")) {
         parametros.getParametrosBusquedaPersonal().getVigenciaIndicador().setIndicador(new Indicadores());
         if (lovIndicadores == null) {
            requerirLOV(cualParametro);
         }

         if (valor.equals("") || valor.isEmpty()) {
            txtP_vindicador_indic = "";
         } else if (lovIndicadores != null) {
            for (int i = 0; i < lovIndicadores.size(); i++) {
               if (lovIndicadores.get(i).getDescripcion().startsWith(valor.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               parametros.getParametrosBusquedaPersonal().getVigenciaIndicador().setIndicador(lovIndicadores.get(indiceUnicoElemento));
               txtP_vindicador_indic = parametros.getParametrosBusquedaPersonal().getVigenciaIndicador().getIndicador().getDescripcion();
            } else {
               txtP_vindicador_indic = "";
               RequestContext.getCurrentInstance().update("formularioDialogos:IndicadorCensoDialogo");
               RequestContext.getCurrentInstance().execute("PF('IndicadorCensoDialogo').show()");
            }
         } else {
            RequestContext.getCurrentInstance().execute("PF('errorListaDialogo').show()");
         }
      } else if (cualParametro.equals("PROFESION")) {
         parametros.getParametrosBusquedaPersonal().getVigenciaFormal().setProfesion(new Profesiones());
         if (lovProfesiones == null) {
            requerirLOV(cualParametro);
         }

         if (valor.equals("") || valor.isEmpty()) {
            txtP_vFormal_profecion = "";
         } else if (lovProfesiones != null) {
            for (int i = 0; i < lovProfesiones.size(); i++) {
               if (lovProfesiones.get(i).getDescripcion().startsWith(valor.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               parametros.getParametrosBusquedaPersonal().getVigenciaFormal().setProfesion(lovProfesiones.get(indiceUnicoElemento));
               txtP_vFormal_profecion = parametros.getParametrosBusquedaPersonal().getVigenciaFormal().getProfesion().getDescripcion();
            } else {
               txtP_vFormal_profecion = "";
               RequestContext.getCurrentInstance().update("formularioDialogos:ProfesionEducacionFormalDialogo");
               RequestContext.getCurrentInstance().execute("PF('ProfesionEducacionFormalDialogo').show()");
            }
         } else {
            RequestContext.getCurrentInstance().execute("PF('errorListaDialogo').show()");
         }
      } else if (cualParametro.equals("INSTITUCIONF")) {
         parametros.getParametrosBusquedaPersonal().getVigenciaFormal().setInstitucion(new Instituciones());
         if (lovInstituciones == null) {
            requerirLOV("INSTITUCION");
         }

         if (valor.equals("") || valor.isEmpty()) {
            txtP_vFormal_institu = "";
         } else if (lovInstituciones != null) {
            for (int i = 0; i < lovInstituciones.size(); i++) {
               if (lovInstituciones.get(i).getDescripcion().startsWith(valor.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               parametros.getParametrosBusquedaPersonal().getVigenciaFormal().setInstitucion(lovInstituciones.get(indiceUnicoElemento));
               txtP_vFormal_institu = parametros.getParametrosBusquedaPersonal().getVigenciaFormal().getInstitucion().getDescripcion();
            } else {
               txtP_vFormal_institu = "";
               RequestContext.getCurrentInstance().update("formularioDialogos:InstitucionEducacionFormalDialogo");
               RequestContext.getCurrentInstance().execute("PF('InstitucionEducacionFormalDialogo').show()");
            }
         } else {
            RequestContext.getCurrentInstance().execute("PF('errorListaDialogo').show()");
         }
      } else if (cualParametro.equals("INSTITUCIONNF")) {
         parametros.getParametrosBusquedaPersonal().getVigenciaNoFormal().setInstitucion(new Instituciones());
         if (lovInstituciones == null) {
            requerirLOV("INSTITUCION");
         }

         if (valor.equals("") || valor.isEmpty()) {
            txtP_vNoformal_institu = "";
         } else if (lovInstituciones != null) {
            for (int i = 0; i < lovInstituciones.size(); i++) {
               if (lovInstituciones.get(i).getDescripcion().startsWith(valor.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               parametros.getParametrosBusquedaPersonal().getVigenciaNoFormal().setInstitucion(lovInstituciones.get(indiceUnicoElemento));
               txtP_vNoformal_institu = parametros.getParametrosBusquedaPersonal().getVigenciaNoFormal().getInstitucion().getDescripcion();
            } else {
               txtP_vNoformal_institu = "";
               RequestContext.getCurrentInstance().update("formularioDialogos:InstitucionEducacionNoFormalDialogo");
               RequestContext.getCurrentInstance().execute("PF('InstitucionEducacionNoFormalDialogo').show()");
            }
         } else {
            RequestContext.getCurrentInstance().execute("PF('errorListaDialogo').show()");
         }
      } else if (cualParametro.equals("CURSO")) {
         parametros.getParametrosBusquedaPersonal().getVigenciaNoFormal().setCurso(new Cursos());
         if (lovCursos == null) {
            requerirLOV(cualParametro);
         }

         if (valor.equals("") || valor.isEmpty()) {
            txtP_vNoformal_curso = "";
         } else if (lovCursos != null) {
            for (int i = 0; i < lovCursos.size(); i++) {
               if (lovCursos.get(i).getNombre().startsWith(valor.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               parametros.getParametrosBusquedaPersonal().getVigenciaNoFormal().setCurso(lovCursos.get(indiceUnicoElemento));
               txtP_vNoformal_curso = parametros.getParametrosBusquedaPersonal().getVigenciaNoFormal().getCurso().getNombre();
            } else {
               txtP_vNoformal_curso = "";
               RequestContext.getCurrentInstance().update("formularioDialogos:CursoEducacionNoFormalDialogo");
               RequestContext.getCurrentInstance().execute("PF('CursoEducacionNoFormalDialogo').show()");
            }
         } else {
            RequestContext.getCurrentInstance().execute("PF('errorListaDialogo').show()");
         }
      } else if (cualParametro.equals("SECTORECONOMICO")) {
         parametros.getParametrosBusquedaPersonal().getHvExperienciaLaboral().setSectoreconomico(new SectoresEconomicos());
         if (lovSectoresEconomicos == null) {
            requerirLOV(cualParametro);
         }

         if (valor.equals("") || valor.isEmpty()) {
            txtP_experLab_SectorEco = "";
         } else if (lovSectoresEconomicos != null) {
            for (int i = 0; i < lovSectoresEconomicos.size(); i++) {
               if (lovSectoresEconomicos.get(i).getDescripcion().startsWith(valor.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               parametros.getParametrosBusquedaPersonal().getHvExperienciaLaboral().setSectoreconomico(lovSectoresEconomicos.get(indiceUnicoElemento));
               txtP_experLab_SectorEco = parametros.getParametrosBusquedaPersonal().getHvExperienciaLaboral().getSectoreconomico().getDescripcion();
            } else {
               txtP_experLab_SectorEco = "";
               RequestContext.getCurrentInstance().update("formularioDialogos:SectorEconomicoExperienciaLaboralDialogo");
               RequestContext.getCurrentInstance().execute("PF('SectorEconomicoExperienciaLaboralDialogo').show()");
            }
         } else {
            RequestContext.getCurrentInstance().execute("PF('errorListaDialogo').show()");
         }
      } else if (cualParametro.equals("PROYECTO")) {
         parametros.getParametrosBusquedaPersonal().getVigenciaProyecto().setProyecto(new Proyectos());
         if (lovProyectos == null) {
            requerirLOV(cualParametro);
         }

         if (valor.equals("") || valor.isEmpty()) {
            txtP_vproyecto_proyect = "";
         } else if (lovProyectos != null) {
            for (int i = 0; i < lovProyectos.size(); i++) {
               if (lovProyectos.get(i).getNombreproyecto().startsWith(valor.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               parametros.getParametrosBusquedaPersonal().getVigenciaProyecto().setProyecto(lovProyectos.get(indiceUnicoElemento));
               txtP_vproyecto_proyect = parametros.getParametrosBusquedaPersonal().getVigenciaProyecto().getProyecto().getNombreproyecto();
            } else {
               txtP_vproyecto_proyect = "";
               RequestContext.getCurrentInstance().update("formularioDialogos:ProyectoProyectoDialogo");
               RequestContext.getCurrentInstance().execute("PF('ProyectoProyectoDialogo').show()");
            }
         } else {
            RequestContext.getCurrentInstance().execute("PF('errorListaDialogo').show()");
         }
      } else if (cualParametro.equals("ROL")) {
         parametros.getParametrosBusquedaPersonal().getVigenciaProyecto().setPryRol(new PryRoles());
         if (lovRoles == null) {
            requerirLOV(cualParametro);
         }

         if (valor.equals("") || valor.isEmpty()) {
            txtP_vproyecto_rol = "";
         } else if (lovRoles != null) {
            for (int i = 0; i < lovRoles.size(); i++) {
               if (lovRoles.get(i).getDescripcion().startsWith(valor.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               parametros.getParametrosBusquedaPersonal().getVigenciaProyecto().setPryRol(lovRoles.get(indiceUnicoElemento));
               txtP_vproyecto_rol = parametros.getParametrosBusquedaPersonal().getVigenciaProyecto().getPryRol().getDescripcion();
            } else {
               txtP_vproyecto_rol = "";
               RequestContext.getCurrentInstance().update("formularioDialogos:PryRolProyectoDialogo");
               RequestContext.getCurrentInstance().execute("PF('PryRolProyectoDialogo').show()");
            }
         } else {
            RequestContext.getCurrentInstance().execute("PF('errorListaDialogo').show()");
         }
      } else if (cualParametro.equals("CARGOPERSONAL")) {
         parametros.getParametrosBusquedaPersonal().setCargo(new Cargos());
         if (lovCargos == null) {
            requerirLOV("CARGO");
         }

         if (valor.equals("") || valor.isEmpty()) {
            txtP_cargoPostul = "";
         } else if (lovCargos != null) {
            for (int i = 0; i < lovCargos.size(); i++) {
               if (lovCargos.get(i).getNombre().startsWith(valor.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               parametros.getParametrosBusquedaPersonal().setCargo(lovCargos.get(indiceUnicoElemento));
               txtP_cargoPostul = parametros.getParametrosBusquedaPersonal().getCargo().getNombre();
            } else {
               txtP_cargoPostul = "";
               RequestContext.getCurrentInstance().update("formularioDialogos:CargoCargoPostularseDialogo");
               RequestContext.getCurrentInstance().execute("PF('CargoCargoPostularseDialogo').show()");
            }
         } else {
            RequestContext.getCurrentInstance().execute("PF('errorListaDialogo').show()");
         }
      } else if (cualParametro.equals("MOTIVORETIROPERSONAL")) {
         parametros.getParametrosBusquedaNomina().setMotivosRetiros(new MotivosRetiros());
         if (lovMotivosRetiros == null) {
            requerirLOV("MOTIVORETIRO");
         }

         if (valor.equals("") || valor.isEmpty()) {
            txtP_experLab_motivo = "";
         } else if (lovMotivosRetiros != null) {
            for (int i = 0; i < lovMotivosRetiros.size(); i++) {
               if (lovMotivosRetiros.get(i).getNombre().startsWith(valor.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            txtP_experLab_motivo = parametros.getParametrosBusquedaPersonal().getHvExperienciaLaboral().getMotivoretiro().getNombre();
            if (coincidencias == 1) {
               parametros.getParametrosBusquedaPersonal().getHvExperienciaLaboral().setMotivoretiro(lovMotivosRetiros.get(indiceUnicoElemento));
            } else {
               txtP_experLab_motivo = "";
               RequestContext.getCurrentInstance().update("formularioDialogos:MotivoRetiroExperienciaLaboralDialogo");
               RequestContext.getCurrentInstance().execute("PF('MotivoRetiroExperienciaLaboralDialogo').show()");
            }
         } else {
            RequestContext.getCurrentInstance().execute("PF('errorListaDialogo').show()");
         }
      }
   }
   //METODOS LISTA DE VALORES

   public void requerirLOV(String tipoLov) {
      if (vTipoBusqueda.equals("Nomina")) {
         if (tipoLov.equals("CARGO") && lovCargos == null) {
            lovCargos = administrarBusquedaAvanzada.lovCargos();
            if (lovCargos != null) {
               RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroC");
            }
         } else if (tipoLov.equals("ESTRUCTURA") && lovEstructuras == null) {
            lovEstructuras = administrarBusquedaAvanzada.lovEstructuras();
            if (lovEstructuras != null) {
               RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroE");
            }
         } else if (tipoLov.equals("JEFE") && lovJefe == null) {
            lovJefe = administrarBusquedaAvanzada.lovJefe();
            if (lovJefe != null) {
               RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroJ");
            }
         } else if (tipoLov.equals("MOTIVOCARGO") && lovMotivosCargos == null) {
            lovMotivosCargos = administrarBusquedaAvanzada.lovMotivosCargos();
            if (lovMotivosCargos != null) {
               RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroMC");
            }
         } else if (tipoLov.equals("PAPEL") && lovPapeles == null) {
            lovPapeles = administrarBusquedaAvanzada.lovPapeles();
            if (lovPapeles != null) {
               RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroPC");
            }
         } else if (tipoLov.equals("MOTIVOLOCALIZACION") && lovMotivosLocalizaciones == null) {
            lovMotivosLocalizaciones = administrarBusquedaAvanzada.lovMotivosLocalizaciones();
            if (lovMotivosLocalizaciones != null) {
               RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroMCC");
            }
         } else if (tipoLov.equals("TIPOSUELDO") && lovTiposSueldos == null) {
            lovTiposSueldos = administrarBusquedaAvanzada.lovTiposSueldos();
            if (lovTiposSueldos != null) {
               RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroTSS");
            }
         } else if (tipoLov.equals("MOTIVOSUELDO") && lovMotivosSueldos == null) {
            lovMotivosSueldos = administrarBusquedaAvanzada.lovMotivosSueldos();
            if (lovMotivosSueldos != null) {
               RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroMSS");
            }
         } else if (tipoLov.equals("TIPOCONTRATO") && lovTiposContratos == null) {
            lovTiposContratos = administrarBusquedaAvanzada.lovTiposContratos();
            if (lovTiposContratos != null) {
               RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroTCFC");
            }
         } else if (tipoLov.equals("MOTIVOCONTRATO") && lovMotivosContratos == null) {
            lovMotivosContratos = administrarBusquedaAvanzada.lovMotivosContratos();
            if (lovMotivosContratos != null) {
               RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroMFC");
            }
         } else if (tipoLov.equals("TIPOTRABAJADOR") && lovTiposTrabajadores == null) {
            lovTiposTrabajadores = administrarBusquedaAvanzada.lovTiposTrabajadores();
            if (lovTiposTrabajadores != null) {
               RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroTTTT");
            }
         } else if (tipoLov.equals("REFORMALABORAL") && lovReformasLaborales == null) {
            lovReformasLaborales = administrarBusquedaAvanzada.lovReformasLaborales();
            if (lovReformasLaborales != null) {
               RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroRLTS");
            }
         } else if (tipoLov.equals("CONTRATO") && lovContratos == null) {
            lovContratos = administrarBusquedaAvanzada.lovContratos();
            if (lovContratos != null) {
               RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroLLL");
            }
         } else if (tipoLov.equals("UBICACION") && lovUbicaciones == null) {
            lovUbicaciones = administrarBusquedaAvanzada.lovUbicaciones();
            if (lovUbicaciones != null) {
               RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroUUG");
            }
         } else if (tipoLov.equals("TERCEROSUCURSAL") && lovTercerosSucursales == null) {
            lovTercerosSucursales = administrarBusquedaAvanzada.lovTercerosSucursales();
            if (lovTercerosSucursales != null) {
               RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroTSA");
            }
         } else if (tipoLov.equals("TIPOENTIDAD") && lovTiposEntidades == null) {
            lovTiposEntidades = administrarBusquedaAvanzada.lovTiposEntidades();
            if (lovTiposEntidades != null) {
               RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroTEA");
            }
         } else if (tipoLov.equals("ESTADOAFILIACION") && lovEstadosAfiliaciones == null) {
            lovEstadosAfiliaciones = administrarBusquedaAvanzada.lovEstadosAfiliaciones();
            if (lovEstadosAfiliaciones != null) {
               RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroEA");
            }
         } else if (tipoLov.equals("FORMAPAGO") && lovFormasPagos == null) {
            lovFormasPagos = administrarBusquedaAvanzada.lovFormasPagos();
            if (lovFormasPagos != null) {
               RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroPFP");
            }
         } else if (tipoLov.equals("SUCURSAL") && lovSucursales == null) {
            lovSucursales = administrarBusquedaAvanzada.lovSucursales();
            if (lovSucursales != null) {
               RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroSFP");
            }
         } else if (tipoLov.equals("MOTIVOMVR") && lovMotivosMvrs == null) {
            lovMotivosMvrs = administrarBusquedaAvanzada.lovMotivosMvrs();
            if (lovMotivosMvrs != null) {
               RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroMMVR");
            }
         } else if (tipoLov.equals("NORMALABORAL") && lovNormasLaborales == null) {
            lovNormasLaborales = administrarBusquedaAvanzada.lovNormasLaborales();
            if (lovNormasLaborales != null) {
               RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroNLNL");
            }
         } else if (tipoLov.equals("JORNADALABORAL") && lovJornadasLaborales == null) {
            lovJornadasLaborales = administrarBusquedaAvanzada.lovJornadasLaborales();
            if (lovJornadasLaborales != null) {
               RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroJLJL");
            }
         } else if (tipoLov.equals("MOTIVORETIRO") && lovMotivosRetiros == null) {
            lovMotivosRetiros = administrarBusquedaAvanzada.lovMotivosRetiros();
            if (lovMotivosRetiros != null) {
               RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroMRFR");
            }
         }
      } else if (vTipoBusqueda.equals("Personal")) {
         if (tipoLov.equals("CIUDAD") && lovCiudades == null) {
            lovCiudades = administrarBusquedaAvanzada.lovCiudades();
            if (lovCiudades != null) {
               RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroCDDP");
            }
         } else if (tipoLov.equals("ESTADOCIVIL") && lovEstructuras == null) {
            lovEstadosCiviles = administrarBusquedaAvanzada.lovEstadosCiviles();
            if (lovEstadosCiviles != null) {
               RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroECEC");
            }
         } else if (tipoLov.equals("IDIOMA") && lovIdiomas == null) {
            lovIdiomas = administrarBusquedaAvanzada.lovIdiomas();
            if (lovIdiomas != null) {
               RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroII");
            }
         } else if (tipoLov.equals("TIPOINDICADOR") && lovTiposIndicadores == null) {
            lovTiposIndicadores = administrarBusquedaAvanzada.lovTiposIndicadores();
            if (lovTiposIndicadores != null) {
               RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroTIC");
            }
         } else if (tipoLov.equals("INDICADOR") && lovIndicadores == null) {
            lovIndicadores = administrarBusquedaAvanzada.lovIndicadores();
            if (lovIndicadores != null) {
               RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroIC");
            }
         } else if (tipoLov.equals("PROFESION") && lovProfesiones == null) {
            lovProfesiones = administrarBusquedaAvanzada.lovProfesiones();
            if (lovProfesiones != null) {
               RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroPEF");
            }
         } else if (tipoLov.equals("INSTITUCION") && lovInstituciones == null) {
            lovInstituciones = administrarBusquedaAvanzada.lovInstitucioneses();
            if (lovInstituciones != null) {
               RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroIEF");
            }
         } else if (tipoLov.equals("CURSO") && lovCursos == null) {
            lovCursos = administrarBusquedaAvanzada.lovCursos();
            if (lovCursos != null) {
               RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroCENF");
            }
         } else if (tipoLov.equals("SECTORECONOMICO") && lovSectoresEconomicos == null) {
            lovSectoresEconomicos = administrarBusquedaAvanzada.lovSectoresEconomicos();
            System.out.println("SECTOR ECONOMICO: " + lovSectoresEconomicos);
            if (lovSectoresEconomicos != null) {
               RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroSEEL");
            }
         } else if (tipoLov.equals("PROYECTO") && lovProyectos == null) {
            lovProyectos = administrarBusquedaAvanzada.lovProyectos();
            if (lovProyectos != null) {
               RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroPP");
            }
         } else if (tipoLov.equals("ROL") && lovRoles == null) {
            lovRoles = administrarBusquedaAvanzada.lovRoles();
            if (lovRoles != null) {
               RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroPRYP");
            }
         } else if (tipoLov.equals("MOTIVORETIRO") && lovMotivosRetiros == null) {
            lovMotivosRetiros = administrarBusquedaAvanzada.lovMotivosRetiros();
            if (lovMotivosRetiros != null) {
               RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroMREL");
            }
         } else if (tipoLov.equals("CARGO") && lovCargos == null) {
            lovCargos = administrarBusquedaAvanzada.lovCargos();
            if (lovCargos != null) {
               RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroCCP");
            }
         }
      }
   }

   public void requerirLovColumnasBusqueda() {
      if (lovColumnasEscenarios == null) {
         lovColumnasEscenarios = administrarBusquedaAvanzada.buscarColumnasEscenarios();
      } else if (lovColumnasEscenarios.isEmpty()) {
         lovColumnasEscenarios = administrarBusquedaAvanzada.buscarColumnasEscenarios();
      }
      columnasEsSeleccionadas = null;
//      modificarInfoR(lovColumnasEscenarios.size()
      RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroCB");
   }

   //METODO ACEPTAR - PARAMETROS NOMIMA
   public void actualizarParametroNomina(String tipoLov) {
      aceptar = true;
      if (tipoLov.equals("CARGO")) {
         parametros.getParametrosBusquedaNomina().getVigenciaCargo().setCargo(cargoSeleccionado);
         txt_vcargo_cargo = parametros.getParametrosBusquedaNomina().getVigenciaCargo().getCargo().getNombre();
         cargoSeleccionado = null;
         filtroLovCargos = null;
      } else if (tipoLov.equals("ESTRUCTURA")) {
         parametros.getParametrosBusquedaNomina().getVigenciaCargo().setEstructura(estructuraSeleccionada);
         txt_vcargo_estructura = parametros.getParametrosBusquedaNomina().getVigenciaCargo().getEstructura().getNombre();
         txt_vcargo_estructura_CC = parametros.getParametrosBusquedaNomina().getVigenciaCargo().getEstructura().getCentrocosto().getNombre();
         estructuraSeleccionada = null;
         filtroLovEstructuras = null;
      } else if (tipoLov.equals("JEFE")) {
         parametros.getParametrosBusquedaNomina().getVigenciaCargo().setEmpleadojefe(jefeSeleccionado);
         txt_vcargo_empleadoJefe_Per = parametros.getParametrosBusquedaNomina().getVigenciaCargo().getEmpleadojefe().getPersona().getNombreCompleto();
         jefeSeleccionado = null;
         filtroLovJefe = null;
      } else if (tipoLov.equals("MOTIVOCARGO")) {
         parametros.getParametrosBusquedaNomina().getVigenciaCargo().setMotivocambiocargo(motivoCargoSeleccionado);
         txt_vcargo_motivoCam = parametros.getParametrosBusquedaNomina().getVigenciaCargo().getMotivocambiocargo().getNombre();
         motivoCargoSeleccionado = null;
         filtroLovMotivosCargos = null;
      } else if (tipoLov.equals("PAPEL")) {
         parametros.getParametrosBusquedaNomina().getVigenciaCargo().setPapel(papelSeleccionado);
         txt_vcargo_papel = parametros.getParametrosBusquedaNomina().getVigenciaCargo().getPapel().getDescripcion();
         papelSeleccionado = null;
         filtroLovPapeles = null;
      } else if (tipoLov.equals("LOCALIZACION")) {
         parametros.getParametrosBusquedaNomina().getVigenciaLocalizacion().setLocalizacion(estructuraSeleccionada);
         txt_vlocalizacion_locali = parametros.getParametrosBusquedaNomina().getVigenciaLocalizacion().getLocalizacion().getNombre();
         estructuraSeleccionada = null;
         filtroLovEstructuras = null;
      } else if (tipoLov.equals("MOTIVOLOCALIZACION")) {
         parametros.getParametrosBusquedaNomina().getVigenciaLocalizacion().setMotivo(motivoLocalizacionSeleccionado);
         txt_vlocalizacion_motivo = parametros.getParametrosBusquedaNomina().getVigenciaLocalizacion().getMotivo().getDescripcion();
         motivoLocalizacionSeleccionado = null;
         filtroLovMotivosLocalizaciones = null;
      } else if (tipoLov.equals("TIPOSUELDO")) {
         parametros.getParametrosBusquedaNomina().getVigenciaSueldo().setTiposueldo(tipoSueldoSeleccionado);
         txt_vsueldo_tipoS = parametros.getParametrosBusquedaNomina().getVigenciaSueldo().getTiposueldo().getDescripcion();
         tipoSueldoSeleccionado = null;
         filtroLovTiposSueldos = null;
      } else if (tipoLov.equals("MOTIVOSUELDO")) {
         parametros.getParametrosBusquedaNomina().getVigenciaSueldo().setMotivocambiosueldo(motivoSueldoSeleccionado);
         txt_vsueldo_motivo = parametros.getParametrosBusquedaNomina().getVigenciaSueldo().getMotivocambiosueldo().getNombre();
         motivoSueldoSeleccionado = null;
         filtroLovMotivosSueldos = null;
      } else if (tipoLov.equals("TIPOCONTRATO")) {
         parametros.getParametrosBusquedaNomina().getVigenciaTipoContrato().setTipocontrato(tipoContratoSeleccionado);
         txt_vtipoContrato_tipoCont = parametros.getParametrosBusquedaNomina().getVigenciaTipoContrato().getTipocontrato().getNombre();
         tipoContratoSeleccionado = null;
         filtroLovTiposContratos = null;
      } else if (tipoLov.equals("MOTIVOCONTRATO")) {
         parametros.getParametrosBusquedaNomina().getVigenciaTipoContrato().setMotivocontrato(motivoContratoSeleccionado);
         txt_vtipoContrato_motivo = parametros.getParametrosBusquedaNomina().getVigenciaTipoContrato().getMotivocontrato().getNombre();
         motivoContratoSeleccionado = null;
         filtroLovMotivosContratos = null;
      } else if (tipoLov.equals("TIPOTRABAJADOR")) {
         parametros.getParametrosBusquedaNomina().getVigenciaTipoTrabajador().setTipotrabajador(tipoTrabajadorSeleccionado);
         txt_vtipoTrab_tipoTrab = parametros.getParametrosBusquedaNomina().getVigenciaTipoTrabajador().getTipotrabajador().getNombre();
         tipoTrabajadorSeleccionado = null;
         filtroLovTiposTrabajadores = null;
      } else if (tipoLov.equals("REFORMALABORAL")) {
         parametros.getParametrosBusquedaNomina().getVigenciaReformasLaboral().setReformalaboral(reformaLaboralSeleccionada);
         txt_vreformaL_ReforLab = parametros.getParametrosBusquedaNomina().getVigenciaReformasLaboral().getReformalaboral().getNombre();
         reformaLaboralSeleccionada = null;
         filtroLovReformasLaborales = null;
      } else if (tipoLov.equals("CONTRATO")) {
         parametros.getParametrosBusquedaNomina().getVigenciaContrato().setContrato(contratoSeleccionado);
         txt_vcontrato_contrato = parametros.getParametrosBusquedaNomina().getVigenciaContrato().getContrato().getDescripcion();
         contratoSeleccionado = null;
         filtroLovContratos = null;
      } else if (tipoLov.equals("UBICACION")) {
         parametros.getParametrosBusquedaNomina().getVigenciaUbicacion().setUbicacion(ubicacionSeleccionado);
         txt_vubicacion_ubic = parametros.getParametrosBusquedaNomina().getVigenciaUbicacion().getUbicacion().getDescripcion();
         txt_vubicacion_ciudad = parametros.getParametrosBusquedaNomina().getVigenciaUbicacion().getUbicacion().getCiudad().getNombre();
         ubicacionSeleccionado = null;
         filtroLovUbicaciones = null;
      } else if (tipoLov.equals("TERCEROSUCURSAL")) {
         parametros.getParametrosBusquedaNomina().getVigenciaAfiliacion().setTercerosucursal(terceroSucursalSeleccionado);
         txt_vafiliacion_tercSuc = parametros.getParametrosBusquedaNomina().getVigenciaAfiliacion().getTercerosucursal().getDescripcion();
         terceroSucursalSeleccionado = null;
         filtroLovTercerosSucursales = null;
      } else if (tipoLov.equals("TIPOENTIDAD")) {
         parametros.getParametrosBusquedaNomina().getVigenciaAfiliacion().setTipoentidad(tipoEntidadSeleccionado);
         txt_vafiliacion_tipoEntidad = parametros.getParametrosBusquedaNomina().getVigenciaAfiliacion().getTipoentidad().getNombre();
         tipoEntidadSeleccionado = null;
         filtroLovTiposEntidades = null;
      } else if (tipoLov.equals("ESTADOAFILIACION")) {
         parametros.getParametrosBusquedaNomina().getVigenciaAfiliacion().setEstadoafiliacion(estadoAfiliacionSeleccionado);
         txt_vafiliacion_estado = parametros.getParametrosBusquedaNomina().getVigenciaAfiliacion().getEstadoafiliacion().getNombre();
         estadoAfiliacionSeleccionado = null;
         filtroLovEstadosAfiliaciones = null;
      } else if (tipoLov.equals("FORMAPAGO")) {
         parametros.getParametrosBusquedaNomina().getVigenciaFormaPago().setFormapago(formaPagoSeleccionado);
         txt_vformapago_formaP = parametros.getParametrosBusquedaNomina().getVigenciaFormaPago().getFormapago().getNombre();
         formaPagoSeleccionado = null;
         filtroLovFormasPagos = null;
      } else if (tipoLov.equals("SUCURSAL")) {
         parametros.getParametrosBusquedaNomina().getVigenciaFormaPago().setSucursal(sucursalSeleccionado);
         txt_vformapago_sucursal = parametros.getParametrosBusquedaNomina().getVigenciaFormaPago().getSucursal().getNombre();
         sucursalSeleccionado = null;
         filtroLovSucursales = null;
      } else if (tipoLov.equals("MOTIVOMVR")) {
         parametros.getParametrosBusquedaNomina().getMvrs().setMotivo(motivoMvrSeleccionado);
         txt_mvrs_motivo = parametros.getParametrosBusquedaNomina().getMvrs().getMotivo().getNombre();
         motivoMvrSeleccionado = null;
         filtroLovMotivosMvrs = null;
      } else if (tipoLov.equals("NORMALABORAL")) {
         parametros.getParametrosBusquedaNomina().getVigenciaNormaEmpleado().setNormalaboral(normaLaboralSeleccionado);
         txt_vnorma_norma = parametros.getParametrosBusquedaNomina().getVigenciaNormaEmpleado().getNormalaboral().getNombre();
         normaLaboralSeleccionado = null;
         filtroLovNormasLaborales = null;
      } else if (tipoLov.equals("JORNADALABORAL")) {
         parametros.getParametrosBusquedaNomina().getVigenciaJornada().setJornadatrabajo(jornadaLaboralSeleccionado);
         txt_vjornada_jornada = parametros.getParametrosBusquedaNomina().getVigenciaJornada().getJornadatrabajo().getDescripcion();
         jornadaLaboralSeleccionado = null;
         filtroLovJornadasLaborales = null;
      } else if (tipoLov.equals("MOTIVORETIRO")) {
         parametros.getParametrosBusquedaNomina().setMotivosRetiros(motivoRetiroSeleccionado);
         txt_motivoRet = parametros.getParametrosBusquedaNomina().getMotivosRetiros().getNombre();
         motivoRetiroSeleccionado = null;
         filtroLovMotivosRetiros = null;
      } else if (tipoLov.equals("MOTIVORETIROPERSONAL")) {
         parametros.getParametrosBusquedaPersonal().getHvExperienciaLaboral().setMotivoretiro(motivoRetiroSeleccionado);
         txt_motivoRet = parametros.getParametrosBusquedaPersonal().getHvExperienciaLaboral().getMotivoretiro().getNombre();
         motivoRetiroSeleccionado = null;
         filtroLovMotivosRetiros = null;
      }
   }

   //METODO CANCELAR - PARAMETROS NOMIMA
   public void cancelarParametroNomina(String tipoLov) {
      aceptar = true;
      if (tipoLov.equals("CARGO")) {
         txt_vcargo_cargo = "";
         cargoSeleccionado = null;
         filtroLovCargos = null;
      } else if (tipoLov.equals("ESTRUCTURA") || tipoLov.equals("LOCALIZACION")) {
         txt_vcargo_estructura_CC = "";
         txt_vcargo_estructura = "";
         txt_vlocalizacion_locali = "";
         estructuraSeleccionada = null;
         filtroLovEstructuras = null;
      } else if (tipoLov.equals("JEFE")) {
         txt_vcargo_empleadoJefe_Per = "";
         jefeSeleccionado = null;
         filtroLovJefe = null;
      } else if (tipoLov.equals("MOTIVOCARGO")) {
         txt_vcargo_motivoCam = "";
         motivoCargoSeleccionado = null;
         filtroLovMotivosCargos = null;
      } else if (tipoLov.equals("PAPEL")) {
         txt_vcargo_papel = "";
         papelSeleccionado = null;
         filtroLovPapeles = null;
      } else if (tipoLov.equals("MOTIVOLOCALIZACION")) {
         txt_vlocalizacion_motivo = "";
         motivoLocalizacionSeleccionado = null;
         filtroLovMotivosLocalizaciones = null;
      } else if (tipoLov.equals("TIPOSUELDO")) {
         txt_vsueldo_tipoS = "";
         tipoSueldoSeleccionado = null;
         filtroLovTiposSueldos = null;
      } else if (tipoLov.equals("MOTIVOSUELDO")) {
         txt_vsueldo_motivo = "";
         motivoSueldoSeleccionado = null;
         filtroLovMotivosSueldos = null;
      } else if (tipoLov.equals("TIPOCONTRATO")) {
         txt_vtipoContrato_tipoCont = "";
         tipoContratoSeleccionado = null;
         filtroLovTiposContratos = null;
      } else if (tipoLov.equals("MOTIVOCONTRATO")) {
         txt_vtipoContrato_motivo = "";
         motivoContratoSeleccionado = null;
         filtroLovMotivosContratos = null;
      } else if (tipoLov.equals("TIPOTRABAJADOR")) {
         txt_vtipoTrab_tipoTrab = "";
         tipoTrabajadorSeleccionado = null;
         filtroLovTiposTrabajadores = null;
      } else if (tipoLov.equals("REFORMALABORAL")) {
         txt_vreformaL_ReforLab = "";
         reformaLaboralSeleccionada = null;
         filtroLovReformasLaborales = null;
      } else if (tipoLov.equals("CONTRATO")) {
         txt_vcontrato_contrato = "";
         contratoSeleccionado = null;
         filtroLovContratos = null;
      } else if (tipoLov.equals("UBICACION")) {
         txt_vubicacion_ubic = "";
         txt_vubicacion_ciudad = "";
         ubicacionSeleccionado = null;
         filtroLovUbicaciones = null;
      } else if (tipoLov.equals("TERCEROSUCURSAL")) {
         txt_vafiliacion_tercSuc = "";
         terceroSucursalSeleccionado = null;
         filtroLovTercerosSucursales = null;
      } else if (tipoLov.equals("TIPOENTIDAD")) {
         txt_vafiliacion_tipoEntidad = "";
         tipoEntidadSeleccionado = null;
         filtroLovTiposEntidades = null;
      } else if (tipoLov.equals("ESTADOAFILIACION")) {
         txt_vafiliacion_estado = "";
         estadoAfiliacionSeleccionado = null;
         filtroLovEstadosAfiliaciones = null;
      } else if (tipoLov.equals("FORMAPAGO")) {
         txt_vformapago_formaP = "";
         formaPagoSeleccionado = null;
         filtroLovFormasPagos = null;
      } else if (tipoLov.equals("SUCURSAL")) {
         txt_vformapago_sucursal = "";
         sucursalSeleccionado = null;
         filtroLovSucursales = null;
      } else if (tipoLov.equals("MOTIVOMVR")) {
         txt_mvrs_motivo = "";
         motivoMvrSeleccionado = null;
         filtroLovMotivosMvrs = null;
      } else if (tipoLov.equals("NORMALABORAL")) {
         txt_vnorma_norma = "";
         normaLaboralSeleccionado = null;
         filtroLovNormasLaborales = null;
      } else if (tipoLov.equals("JORNADALABORAL")) {
         txt_vjornada_jornada = "";
         jornadaLaboralSeleccionado = null;
         filtroLovJornadasLaborales = null;
      } else if (tipoLov.equals("MOTIVORETIRO")) {
         txt_motivoRet = "";
         motivoRetiroSeleccionado = null;
         filtroLovMotivosRetiros = null;
      }
      anchoTablaResultados = 800 + "px";
   }

   //METODO ACEPTAR - PARAMETROS PERSONAL
   public void actualizarParametroPersonal(String tipoLov) {
      aceptar = true;
      if (tipoLov.equals("CIUDADNACIMIENTO")) {
         parametros.getParametrosBusquedaPersonal().getEmpleado().getPersona().setCiudadnacimiento(ciudadSeleccionado);
         txtP_empleado_per_ciudadNac = parametros.getParametrosBusquedaPersonal().getEmpleado().getPersona().getCiudadnacimiento().getNombre();
         ciudadSeleccionado = null;
         filtroLovCiudades = null;
      } else if (tipoLov.equals("CIUDADDOCUMENTO")) {
         parametros.getParametrosBusquedaPersonal().getEmpleado().getPersona().setCiudaddocumento(ciudadSeleccionado);
         txtP_empleado_per_ciudadDoc = parametros.getParametrosBusquedaPersonal().getEmpleado().getPersona().getCiudaddocumento().getNombre();
         ciudadSeleccionado = null;
         filtroLovCiudades = null;
      } else if (tipoLov.equals("ESTADOCIVIL")) {
         parametros.getParametrosBusquedaPersonal().setEstadoCivil(estadoCivilSeleccionado);
         txtP_estadoCivil = parametros.getParametrosBusquedaPersonal().getEstadoCivil().getDescripcion();
         estadoCivilSeleccionado = null;
         filtroLovEstadosCiviles = null;
      } else if (tipoLov.equals("IDIOMA")) {
         parametros.getParametrosBusquedaPersonal().getIdiomaPersona().setIdioma(idiomaSeleccionado);
         txtP_idiomap_idioma = parametros.getParametrosBusquedaPersonal().getIdiomaPersona().getIdioma().getNombre();
         idiomaSeleccionado = null;
         filtroLovIdiomas = null;
      } else if (tipoLov.equals("TIPOINDICADOR")) {
         parametros.getParametrosBusquedaPersonal().getVigenciaIndicador().setTipoindicador(tipoIndicadorSeleccionado);
         txtP_vindicador_tipoIndi = parametros.getParametrosBusquedaPersonal().getVigenciaIndicador().getTipoindicador().getDescripcion();
         tipoIndicadorSeleccionado = null;
         filtroLovTiposIndicadores = null;
      } else if (tipoLov.equals("INDICADOR")) {
         parametros.getParametrosBusquedaPersonal().getVigenciaIndicador().setIndicador(indicadorSeleccionado);
         txtP_vindicador_indic = parametros.getParametrosBusquedaPersonal().getVigenciaIndicador().getIndicador().getDescripcion();
         indicadorSeleccionado = null;
         filtroLovIndicadores = null;
      } else if (tipoLov.equals("PROFESION")) {
         parametros.getParametrosBusquedaPersonal().getVigenciaFormal().setProfesion(profesionSeleccionado);
         txtP_vFormal_profecion = parametros.getParametrosBusquedaPersonal().getVigenciaFormal().getProfesion().getDescripcion();
         profesionSeleccionado = null;
         filtroLovProfesiones = null;
      } else if (tipoLov.equals("INSTITUCIONF")) {
         parametros.getParametrosBusquedaPersonal().getVigenciaFormal().setInstitucion(institucionSeleccionado);
         txtP_vFormal_institu = parametros.getParametrosBusquedaPersonal().getVigenciaFormal().getInstitucion().getDescripcion();
         institucionSeleccionado = null;
         filtroLovInstituciones = null;
      } else if (tipoLov.equals("INSTITUCIONNF")) {
         parametros.getParametrosBusquedaPersonal().getVigenciaNoFormal().setInstitucion(institucionSeleccionado);
         txtP_vNoformal_institu = parametros.getParametrosBusquedaPersonal().getVigenciaNoFormal().getInstitucion().getDescripcion();
         institucionSeleccionado = null;
         filtroLovInstituciones = null;
      } else if (tipoLov.equals("CURSO")) {
         parametros.getParametrosBusquedaPersonal().getVigenciaNoFormal().setCurso(cursoSeleccionado);
         txtP_vNoformal_curso = parametros.getParametrosBusquedaPersonal().getVigenciaNoFormal().getCurso().getNombre();
         cursoSeleccionado = null;
         filtroLovCursos = null;
      } else if (tipoLov.equals("SECTORECONOMICO")) {
         parametros.getParametrosBusquedaPersonal().getHvExperienciaLaboral().setSectoreconomico(sectorEconomicoSeleccionado);
         txtP_experLab_SectorEco = parametros.getParametrosBusquedaPersonal().getHvExperienciaLaboral().getSectoreconomico().getDescripcion();
         sectorEconomicoSeleccionado = null;
         filtroLovSectoresEconomicos = null;
      } else if (tipoLov.equals("MOTIVORETIRO")) {
         parametros.getParametrosBusquedaPersonal().getHvExperienciaLaboral().setMotivoretiro(motivoRetiroSeleccionado);
         txtP_experLab_motivo = parametros.getParametrosBusquedaPersonal().getHvExperienciaLaboral().getMotivoretiro().getNombre();
         motivoRetiroSeleccionado = null;
         filtroLovMotivosRetiros = null;
      } else if (tipoLov.equals("PROYECTO")) {
         parametros.getParametrosBusquedaPersonal().getVigenciaProyecto().setProyecto(proyectoSeleccionado);
         txtP_vproyecto_proyect = parametros.getParametrosBusquedaPersonal().getVigenciaProyecto().getProyecto().getNombreproyecto();
         proyectoSeleccionado = null;
         filtroLovProyectos = null;
      } else if (tipoLov.equals("ROL")) {
         parametros.getParametrosBusquedaPersonal().getVigenciaProyecto().setPryRol(rolSeleccionado);
         txtP_vproyecto_rol = parametros.getParametrosBusquedaPersonal().getVigenciaProyecto().getPryRol().getDescripcion();
         rolSeleccionado = null;
         filtroLovRoles = null;
      } else if (tipoLov.equals("CARGO")) {
         parametros.getParametrosBusquedaPersonal().setCargo(cargoSeleccionado);
         txtP_cargoPostul = parametros.getParametrosBusquedaPersonal().getCargo().getNombre();
         cargoSeleccionado = null;
         filtroLovCargos = null;
      } else if (tipoLov.equals("COLUMNASBUSQUEDA")) {
         filtradoColumnasEscenarios = null;
      }
   }

   //METODO CANCELAR - PARAMETROS PERSONAL
   public void cancelarParametroPersonal(String tipoLov) {
      aceptar = true;
      if (tipoLov.equals("CIUDADNACIMIENTO")) {
         txtP_empleado_per_ciudadNac = "";
         ciudadSeleccionado = null;
         filtroLovCiudades = null;
      } else if (tipoLov.equals("CIUDADDOCUMENTO")) {
         txtP_empleado_per_ciudadDoc = "";
         ciudadSeleccionado = null;
         filtroLovCiudades = null;
      } else if (tipoLov.equals("ESTADOCIVIL")) {
         txtP_estadoCivil = "";
         estadoCivilSeleccionado = null;
         filtroLovEstadosCiviles = null;
      } else if (tipoLov.equals("IDIOMA")) {
         txtP_idiomap_idioma = "";
         idiomaSeleccionado = null;
         filtroLovIdiomas = null;
      } else if (tipoLov.equals("TIPOINDICADOR")) {
         txtP_vindicador_tipoIndi = "";
         tipoIndicadorSeleccionado = null;
         filtroLovTiposIndicadores = null;
      } else if (tipoLov.equals("INDICADOR")) {
         txtP_vindicador_indic = "";
         indicadorSeleccionado = null;
         filtroLovIndicadores = null;
      } else if (tipoLov.equals("PROFESION")) {
         txtP_vFormal_profecion = "";
         profesionSeleccionado = null;
         filtroLovProfesiones = null;
      } else if (tipoLov.equals("INSTITUCION")) {
         txtP_vFormal_institu = "";
         txtP_vNoformal_institu = "";
         institucionSeleccionado = null;
         filtroLovInstituciones = null;
      } else if (tipoLov.equals("CURSO")) {
         txtP_vNoformal_curso = "";
         cursoSeleccionado = null;
         filtroLovCursos = null;
      } else if (tipoLov.equals("SECTORECONOMICO")) {
         txtP_experLab_SectorEco = "";
         sectorEconomicoSeleccionado = null;
         filtroLovSectoresEconomicos = null;
      } else if (tipoLov.equals("MOTIVORETIRO")) {
         txtP_experLab_motivo = "";
         motivoRetiroSeleccionado = null;
         filtroLovMotivosRetiros = null;
      } else if (tipoLov.equals("PROYECTO")) {
         txtP_vproyecto_proyect = "";
         proyectoSeleccionado = null;
         filtroLovProyectos = null;
      } else if (tipoLov.equals("ROL")) {
         txtP_vproyecto_rol = "";
         parametros.getParametrosBusquedaPersonal().getVigenciaProyecto().setPryRol(rolSeleccionado);
         rolSeleccionado = null;
         filtroLovRoles = null;
      } else if (tipoLov.equals("CARGO")) {
         txtP_cargoPostul = "";
         cargoSeleccionado = null;
         filtroLovCargos = null;
      } else if (tipoLov.equals("COLUMNASBUSQUEDA")) {
         columnasEsSeleccionadas = null;
         filtradoColumnasEscenarios = null;
      }
      anchoTablaResultados = 800 + "px";

   }

   //VALIDAR FECHAS
   public void modificarFechaFinalCargo() {
      boolean retorno;
      retorno = validarFechas(parametros.getParametrosBusquedaNomina().getFechaInicialCargo(), parametros.getParametrosBusquedaNomina().getFechaFinalCargo());
      if (!retorno) {
         parametros.getParametrosBusquedaNomina().setFechaInicialCargo(null);
         parametros.getParametrosBusquedaNomina().setFechaFinalCargo(null);
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:opcionesCosto:parametroFechaInicialCargo");
         RequestContext.getCurrentInstance().update("form:opcionesCosto:parametroFechaFinalCargo");
         RequestContext.getCurrentInstance().execute("PF('errorFechasIngresadas').show()");
      }
   }

   public void modificarFechaFinalCentroCosto() {
      boolean retorno;
      retorno = validarFechas(parametros.getParametrosBusquedaNomina().getFechaInicialCentroCosto(), parametros.getParametrosBusquedaNomina().getFechaFinalCentroCosto());
      if (!retorno) {
         parametros.getParametrosBusquedaNomina().setFechaInicialCentroCosto(null);
         parametros.getParametrosBusquedaNomina().setFechaFinalCentroCosto(null);
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:opcionesCentroCosto:parametroFechaInicialCentroCosto");
         RequestContext.getCurrentInstance().update("form:opcionesCentroCosto:parametroFechaFinalCentroCosto");
         RequestContext.getCurrentInstance().execute("PF('errorFechasIngresadas').show()");
      }
   }

   public void modificarFechaFinalSueldo() {
      boolean retorno;
      retorno = validarFechas(parametros.getParametrosBusquedaNomina().getFechaInicialSueldo(), parametros.getParametrosBusquedaNomina().getFechaFinalSueldo());
      if (!retorno) {
         parametros.getParametrosBusquedaNomina().setFechaInicialSueldo(null);
         parametros.getParametrosBusquedaNomina().setFechaFinalSueldo(null);
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:opcionesSueldo:parametroFechaInicialSueldo");
         RequestContext.getCurrentInstance().update("form:opcionesSueldo:parametroFechaFinalSueldo");
         RequestContext.getCurrentInstance().execute("PF('errorFechasIngresadas').show()");
      }
   }

   public void modificarFechaFinalTipoContrato() {
      boolean retorno;
      retorno = validarFechas(parametros.getParametrosBusquedaNomina().getFechaInicialTipoContrato(), parametros.getParametrosBusquedaNomina().getFechaFinalTipoContrato());
      if (!retorno) {
         parametros.getParametrosBusquedaNomina().setFechaInicialTipoContrato(null);
         parametros.getParametrosBusquedaNomina().setFechaFinalTipoContrato(null);
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:opcionesTipoContrato:parametroFechaInicialTipoContrato");
         RequestContext.getCurrentInstance().update("form:opcionesTipoContrato:parametroFechaFinalTipoContrato");
         RequestContext.getCurrentInstance().execute("PF('errorFechasIngresadas').show()");
      }
   }

   public void modificarFechaFinalTipoTrabajador() {
      boolean retorno;
      retorno = validarFechas(parametros.getParametrosBusquedaNomina().getFechaInicialTipoTrabajador(), parametros.getParametrosBusquedaNomina().getFechaFinalTipoTrabajador());
      if (!retorno) {
         parametros.getParametrosBusquedaNomina().setFechaInicialTipoTrabajador(null);
         parametros.getParametrosBusquedaNomina().setFechaFinalTipoTrabajador(null);
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:opcionesTipoTrabajador:parametroFechaInicialTipoTrabajador");
         RequestContext.getCurrentInstance().update("form:opcionesTipoTrabajador:parametroFechaFinalTipoTrabajador");
         RequestContext.getCurrentInstance().execute("PF('errorFechasIngresadas').show()");
      }
   }

   public void modificarFechaFinalTipoSalario() {
      boolean retorno;
      retorno = validarFechas(parametros.getParametrosBusquedaNomina().getFechaInicialReformaLaboral(), parametros.getParametrosBusquedaNomina().getFechaFinalReformaLaboral());
      if (!retorno) {
         parametros.getParametrosBusquedaNomina().setFechaInicialReformaLaboral(null);
         parametros.getParametrosBusquedaNomina().setFechaFinalReformaLaboral(null);
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:opcionesTipoSalario:parametroFechaInicialTipoSalario");
         RequestContext.getCurrentInstance().update("form:opcionesTipoSalario:parametroFechaFinalTipoSalario");
         RequestContext.getCurrentInstance().execute("PF('errorFechasIngresadas').show()");
      }
   }

   public void modificarFechaFinalContratoMI() {
      boolean retorno;
      retorno = validarFechas(parametros.getParametrosBusquedaNomina().getFechaInicialLegislacionMI(), parametros.getParametrosBusquedaNomina().getFechaFinalLegislacionMI());
      if (!retorno) {
         parametros.getParametrosBusquedaNomina().setFechaInicialLegislacionMI(null);
         parametros.getParametrosBusquedaNomina().setFechaFinalLegislacionMI(null);
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:opcionesLegislacionLaboral:parametroFechaInicialLegislacionLaboralMI");
         RequestContext.getCurrentInstance().update("form:opcionesLegislacionLaboral:parametroFechaFinalLegislacionLaboralMI");
         RequestContext.getCurrentInstance().execute("PF('errorFechasIngresadas').show()");
      }
   }

   public void modificarFechaFinalContratoMF() {
      boolean retorno;
      retorno = validarFechas(parametros.getParametrosBusquedaNomina().getFechaInicialLegislacionMF(), parametros.getParametrosBusquedaNomina().getFechaFinalLegislacionMF());
      if (!retorno) {
         parametros.getParametrosBusquedaNomina().setFechaInicialLegislacionMF(null);
         parametros.getParametrosBusquedaNomina().setFechaFinalLegislacionMF(null);
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:opcionesLegislacionLaboral:parametroFechaInicialLegislacionLaboralMF");
         RequestContext.getCurrentInstance().update("form:opcionesLegislacionLaboral:parametroFechaFinalLegislacionLaboralMF");
         RequestContext.getCurrentInstance().execute("PF('errorFechasIngresadas').show()");
      }
   }

   public void modificarFechaFinalUbicacion() {
      boolean retorno;
      retorno = validarFechas(parametros.getParametrosBusquedaNomina().getFechaInicialUbicacion(), parametros.getParametrosBusquedaNomina().getFechaFinalUbicacion());
      if (!retorno) {
         parametros.getParametrosBusquedaNomina().setFechaInicialUbicacion(null);
         parametros.getParametrosBusquedaNomina().setFechaFinalUbicacion(null);
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:opcionesUbicacion:parametroFechaInicialUbicacion");
         RequestContext.getCurrentInstance().update("form:opcionesUbicacion:parametroFechaFinalUbicacion");
         RequestContext.getCurrentInstance().execute("PF('errorFechasIngresadas').show()");
      }
   }

   public void modificarFechaFinalAfiliacion() {
      boolean retorno;
      retorno = validarFechas(parametros.getParametrosBusquedaNomina().getFechaInicialAfiliacion(), parametros.getParametrosBusquedaNomina().getFechaFinalAfiliacion());
      if (!retorno) {
         parametros.getParametrosBusquedaNomina().setFechaInicialAfiliacion(null);
         parametros.getParametrosBusquedaNomina().setFechaFinalAfiliacion(null);
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:opcionesAfiliacion:parametroFechaInicialAfiliacion");
         RequestContext.getCurrentInstance().update("form:opcionesAfiliacion:parametroFechaFinalAfiliacion");
         RequestContext.getCurrentInstance().execute("PF('errorFechasIngresadas').show()");
      }
   }

   public void modificarFechaFinalFormaPago() {
      boolean retorno;
      retorno = validarFechas(parametros.getParametrosBusquedaNomina().getFechaInicialFormaPago(), parametros.getParametrosBusquedaNomina().getFechaFinalFormaPago());
      if (!retorno) {
         parametros.getParametrosBusquedaNomina().setFechaInicialFormaPago(null);
         parametros.getParametrosBusquedaNomina().setFechaFinalFormaPago(null);
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:opcionesFormaPago:parametroFechaInicialFormaPago");
         RequestContext.getCurrentInstance().update("form:opcionesFormaPago:parametroFechaFinalFormaPago");
         RequestContext.getCurrentInstance().execute("PF('errorFechasIngresadas').show()");
      }
   }

   public void modificarFechaFinalMvr() {
      boolean retorno;
      retorno = validarFechas(parametros.getParametrosBusquedaNomina().getFechaInicialMvr(), parametros.getParametrosBusquedaNomina().getFechaFinalMvr());
      if (!retorno) {
         parametros.getParametrosBusquedaNomina().setFechaInicialMvr(null);
         parametros.getParametrosBusquedaNomina().setFechaFinalMvr(null);
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:opcionesMvrs:parametroFechaInicialMvrs");
         RequestContext.getCurrentInstance().update("form:opcionesMvrs:parametroFechaFinalMvrs");
         RequestContext.getCurrentInstance().execute("PF('errorFechasIngresadas').show()");
      }
   }

   public void modificarFechaFinalSetMI() {
      boolean retorno;
      retorno = validarFechas(parametros.getParametrosBusquedaNomina().getFechaInicialSetMI(), parametros.getParametrosBusquedaNomina().getFechaFinalSetMI());
      if (!retorno) {
         parametros.getParametrosBusquedaNomina().setFechaInicialSetMI(null);
         parametros.getParametrosBusquedaNomina().setFechaFinalSetMI(null);
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:opcionesSets:parametroFechaMIInicialSets");
         RequestContext.getCurrentInstance().update("form:opcionesSets:parametroFechaMIFinalSets");
         RequestContext.getCurrentInstance().execute("PF('errorFechasIngresadas').show()");
      }
   }

   public void modificarFechaFinalSetMF() {
      boolean retorno;
      retorno = validarFechas(parametros.getParametrosBusquedaNomina().getFechaInicialSetMF(), parametros.getParametrosBusquedaNomina().getFechaFinalSetMF());
      if (!retorno) {
         parametros.getParametrosBusquedaNomina().setFechaInicialSetMF(null);
         parametros.getParametrosBusquedaNomina().setFechaFinalSetMF(null);
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:opcionesSets:parametroFechaMFInicialSets");
         RequestContext.getCurrentInstance().update("form:opcionesSets:parametroFechaMFFinalSets");
         RequestContext.getCurrentInstance().execute("PF('errorFechasIngresadas').show()");
      }
   }

   public void modificarFechaFinalNormaLaboral() {
      boolean retorno;
      retorno = validarFechas(parametros.getParametrosBusquedaNomina().getFechaInicialNormaLaboral(), parametros.getParametrosBusquedaNomina().getFechaFinalNormaLaboral());
      if (!retorno) {
         parametros.getParametrosBusquedaNomina().setFechaInicialNormaLaboral(null);
         parametros.getParametrosBusquedaNomina().setFechaFinalNormaLaboral(null);
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:opcionesNormaLaboral:parametroFechaInicialNormaLaboral");
         RequestContext.getCurrentInstance().update("form:opcionesNormaLaboral:parametroFechaFinalNormaLaboral");
         RequestContext.getCurrentInstance().execute("PF('errorFechasIngresadas').show()");
      }
   }

   public void modificarFechaFinalVacacionesMI() {
      boolean retorno;
      retorno = validarFechas(parametros.getParametrosBusquedaNomina().getFechaInicialVacacionesMI(), parametros.getParametrosBusquedaNomina().getFechaFinalVacacionesMI());
      if (!retorno) {
         parametros.getParametrosBusquedaNomina().setFechaInicialVacacionesMI(null);
         parametros.getParametrosBusquedaNomina().setFechaFinalVacacionesMI(null);
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:opcionesVacacion:parametroFechaMIInicialVacacion");
         RequestContext.getCurrentInstance().update("form:opcionesVacacion:parametroFechaMIFinalVacacion");
         RequestContext.getCurrentInstance().execute("PF('errorFechasIngresadas').show()");
      }
   }

   public void modificarFechaFinalVacacionesMF() {
      boolean retorno;
      retorno = validarFechas(parametros.getParametrosBusquedaNomina().getFechaInicialVacacionesMF(), parametros.getParametrosBusquedaNomina().getFechaFinalVacacionesMF());
      if (!retorno) {
         parametros.getParametrosBusquedaNomina().setFechaInicialVacacionesMF(null);
         parametros.getParametrosBusquedaNomina().setFechaFinalVacacionesMF(null);
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:opcionesVacacion:parametroFechaMFInicialVacacion");
         RequestContext.getCurrentInstance().update("form:opcionesVacacion:parametroFechaMFFinalVacacion");
         RequestContext.getCurrentInstance().execute("PF('errorFechasIngresadas').show()");
      }
   }

   public void modificarFechaFinalJornadaLaboral() {
      boolean retorno;
      retorno = validarFechas(parametros.getParametrosBusquedaNomina().getFechaInicialJornadaLaboral(), parametros.getParametrosBusquedaNomina().getFechaFinalJornadaLaboral());
      if (!retorno) {
         parametros.getParametrosBusquedaNomina().setFechaInicialJornadaLaboral(null);
         parametros.getParametrosBusquedaNomina().setFechaFinalJornadaLaboral(null);
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:opcionesLegislacionLaboral:parametroFechaInicialAfiliacion");
         RequestContext.getCurrentInstance().update("form:opcionesLegislacionLaboral:parametroFechaFinalAfiliacion");
         RequestContext.getCurrentInstance().execute("PF('errorFechasIngresadas').show()");
      }
   }

   public void modificarFechaFinalRetiro() {
      boolean retorno;
      retorno = validarFechas(parametros.getParametrosBusquedaNomina().getFechaInicialRetiro(), parametros.getParametrosBusquedaNomina().getFechaFinalRetiro());
      if (!retorno) {
         parametros.getParametrosBusquedaNomina().setFechaInicialRetiro(null);
         parametros.getParametrosBusquedaNomina().setFechaFinalRetiro(null);
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:opcionesJornadaLaboral:parametroFechaInicialJornadaLaboral");
         RequestContext.getCurrentInstance().update("form:opcionesJornadaLaboral:parametroFechaFinalJornadaLaboral");
         RequestContext.getCurrentInstance().execute("PF('errorFechasIngresadas').show()");
      }
   }

   public void modificarFechaFinalDatosPersonales() {
      boolean retorno;
      retorno = validarFechas(parametros.getParametrosBusquedaPersonal().getFechaInicialDatosPersonales(), parametros.getParametrosBusquedaPersonal().getFechaFinalDatosPersonales());
      if (!retorno) {
         parametros.getParametrosBusquedaPersonal().setFechaInicialDatosPersonales(null);
         parametros.getParametrosBusquedaPersonal().setFechaFinalDatosPersonales(null);
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:opcionesDatosPersonales:parametroFechaInicialDatosPersonales");
         RequestContext.getCurrentInstance().update("form:opcionesDatosPersonales:parametroFechaFinalDatosPersonales");
         RequestContext.getCurrentInstance().execute("PF('errorFechasIngresadas').show()");
      }
   }

   public void modificarFechaFinalEstadoCivil() {
      boolean retorno;
      retorno = validarFechas(parametros.getParametrosBusquedaPersonal().getFechaInicialEstadoCivil(), parametros.getParametrosBusquedaPersonal().getFechaFinalEstadoCivil());
      if (!retorno) {
         parametros.getParametrosBusquedaPersonal().setFechaInicialEstadoCivil(null);
         parametros.getParametrosBusquedaPersonal().setFechaFinalEstadoCivil(null);
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:opcionesEstadoCivil:parametroFechaInicialEstadoCivil");
         RequestContext.getCurrentInstance().update("form:opcionesEstadoCivil:parametroFechaFinalEstadoCivil");
         RequestContext.getCurrentInstance().execute("PF('errorFechasIngresadas').show()");
      }
   }

   public void modificarFechaFinalCenso() {
      boolean retorno;
      retorno = validarFechas(parametros.getParametrosBusquedaPersonal().getFechaInicialCenso(), parametros.getParametrosBusquedaPersonal().getFechaFinalCenso());
      if (!retorno) {
         parametros.getParametrosBusquedaPersonal().setFechaInicialCenso(null);
         parametros.getParametrosBusquedaPersonal().setFechaFinalCenso(null);
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:opcionesCenso:parametroFechaInicialCenso");
         RequestContext.getCurrentInstance().update("form:opcionesCenso:parametroFechaFinalCenso");
         RequestContext.getCurrentInstance().execute("PF('errorFechasIngresadas').show()");
      }
   }

   public void modificarFechaFinalFormal() {
      boolean retorno;
      retorno = validarFechas(parametros.getParametrosBusquedaPersonal().getFechaInicialFormal(), parametros.getParametrosBusquedaPersonal().getFechaFinalFormal());
      if (!retorno) {
         parametros.getParametrosBusquedaPersonal().setFechaInicialFormal(null);
         parametros.getParametrosBusquedaPersonal().setFechaFinalFormal(null);
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:opcionesEducacionFormal:parametroFechaInicialEducacionFormal");
         RequestContext.getCurrentInstance().update("form:opcionesEducacionFormal:parametroFechaFinalEducacionFormal");
         RequestContext.getCurrentInstance().execute("PF('errorFechasIngresadas').show()");
      }
   }

   public void modificarFechaFinalNoFormal() {
      boolean retorno;
      retorno = validarFechas(parametros.getParametrosBusquedaPersonal().getFechaInicialNoFormal(), parametros.getParametrosBusquedaPersonal().getFechaFinalNoFormal());
      if (!retorno) {
         parametros.getParametrosBusquedaPersonal().setFechaInicialNoFormal(null);
         parametros.getParametrosBusquedaPersonal().setFechaFinalNoFormal(null);
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:opcionesEducacionNoFormal:parametroFechaInicialEducacionNoFormal");
         RequestContext.getCurrentInstance().update("form:opcionesEducacionNoFormal:parametroFechaFinalEducacionNoFormal");
         RequestContext.getCurrentInstance().execute("PF('errorFechasIngresadas').show()");
      }
   }

   public void modificarFechaFinalExperienciaLaboral() {
      boolean retorno;
      retorno = validarFechas(parametros.getParametrosBusquedaPersonal().getFechaInicialExperienciaLaboral(), parametros.getParametrosBusquedaPersonal().getFechaFinalExperienciaLaboral());
      if (!retorno) {
         parametros.getParametrosBusquedaPersonal().setFechaInicialExperienciaLaboral(null);
         parametros.getParametrosBusquedaPersonal().setFechaFinalExperienciaLaboral(null);
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:opcionesExperienciaLaboral:parametroFechaInicialExperienciaLaboral");
         RequestContext.getCurrentInstance().update("form:opcionesExperienciaLaboral:parametroFechaFinalExperienciaLaboral");
         RequestContext.getCurrentInstance().execute("PF('errorFechasIngresadas').show()");
      }
   }

   public void modificarFechaFinalProyecto() {
      boolean retorno;
      retorno = validarFechas(parametros.getParametrosBusquedaPersonal().getFechaInicialProyecto(), parametros.getParametrosBusquedaPersonal().getFechaFinalProyecto());
      if (!retorno) {
         parametros.getParametrosBusquedaPersonal().setFechaInicialProyecto(null);
         parametros.getParametrosBusquedaPersonal().setFechaFinalProyecto(null);
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:opcionesProyecto:parametroFechaInicialProyecto");
         RequestContext.getCurrentInstance().update("form:opcionesProyecto:parametroFechaFinalProyecto");
         RequestContext.getCurrentInstance().execute("PF('errorFechasIngresadas').show()");
      }
   }

   public void modificarSueldoMaximo() {
      RequestContext context = RequestContext.getCurrentInstance();
      boolean retorno = validarValores(parametros.getParametrosBusquedaNomina().getSueldoMinimo(), parametros.getParametrosBusquedaNomina().getSueldoMaximo());
      if (!retorno) {
         parametros.getParametrosBusquedaNomina().setSueldoMinimo(BigDecimal.ZERO);
         parametros.getParametrosBusquedaNomina().setSueldoMaximo(BigDecimal.ZERO);
         RequestContext.getCurrentInstance().update("form:opcionesSueldo:parametroSueldoMinimo");
         RequestContext.getCurrentInstance().update("form:opcionesSueldo:parametroSueldoMaximo");
         RequestContext.getCurrentInstance().execute("PF('errorSueldos').show()");
      }
   }

   public void modificarSueldoMaximoMvr() {
      RequestContext context = RequestContext.getCurrentInstance();
      boolean retorno = validarValores(parametros.getParametrosBusquedaNomina().getSueldoMinimoMvr(), parametros.getParametrosBusquedaNomina().getSueldoMaximoMvr());
      if (!retorno) {
         parametros.getParametrosBusquedaNomina().setSueldoMinimoMvr(BigDecimal.ZERO);
         parametros.getParametrosBusquedaNomina().setSueldoMaximoMvr(BigDecimal.ZERO);
         RequestContext.getCurrentInstance().update("form:opcionesMvrs:parametroSueldoMinimoMvrs");
         RequestContext.getCurrentInstance().update("form:opcionesMvrs:parametroSueldoMaximoMvrs");
         RequestContext.getCurrentInstance().execute("PF('errorSueldos').show()");
      }
   }

   public void modificarPromedioMaximoSet() {
      RequestContext context = RequestContext.getCurrentInstance();
      boolean retorno = validarValores(parametros.getParametrosBusquedaNomina().getPromedioMinimoSet(), parametros.getParametrosBusquedaNomina().getPromedioMaximoSet());
      if (!retorno) {
         parametros.getParametrosBusquedaNomina().setPromedioMinimoSet(BigDecimal.ZERO);
         parametros.getParametrosBusquedaNomina().setPromedioMaximoSet(BigDecimal.ZERO);
         RequestContext.getCurrentInstance().update("form:opcionesSets:parametroPromedioMinSets");
         RequestContext.getCurrentInstance().update("form:opcionesSets:parametroPromedioMaxSets");
         RequestContext.getCurrentInstance().execute("PF('errorPromedios').show()");
      }
   }

   public void modificarHastaConversacionIdioma() {
      RequestContext context = RequestContext.getCurrentInstance();
      boolean retorno = validarValoresEnteros(parametros.getParametrosBusquedaPersonal().getConversacionDesde(), parametros.getParametrosBusquedaPersonal().getConversacionHasta());
      if (!retorno) {
         parametros.getParametrosBusquedaPersonal().setConversacionDesde(BigInteger.ZERO);
         parametros.getParametrosBusquedaPersonal().setConversacionHasta(BigInteger.ZERO);
         RequestContext.getCurrentInstance().update("form:opcionesIdioma:parametroConversacionDesdeIdioma");
         RequestContext.getCurrentInstance().update("form:opcionesIdioma:parametroConversacionHastaIdioma");
         RequestContext.getCurrentInstance().execute("PF('errorIdiomaPorcentajes').show()");
      }
   }

   public void modificarHastaLecturaIdioma() {
      RequestContext context = RequestContext.getCurrentInstance();
      boolean retorno = validarValoresEnteros(parametros.getParametrosBusquedaPersonal().getLecturaDesde(), parametros.getParametrosBusquedaPersonal().getLecturaHasta());
      if (!retorno) {
         parametros.getParametrosBusquedaPersonal().setLecturaDesde(BigInteger.ZERO);
         parametros.getParametrosBusquedaPersonal().setLecturaHasta(BigInteger.ZERO);
         RequestContext.getCurrentInstance().update("form:opcionesIdioma:parametroLecturaDesdeIdioma");
         RequestContext.getCurrentInstance().update("form:opcionesIdioma:parametroLecturaHastaIdioma");
         RequestContext.getCurrentInstance().execute("PF('errorIdiomaPorcentajes').show()");
      }
   }

   public void modificarHastaEscrituraIdioma() {
      RequestContext context = RequestContext.getCurrentInstance();
      boolean retorno = validarValoresEnteros(parametros.getParametrosBusquedaPersonal().getEscrituraDesde(), parametros.getParametrosBusquedaPersonal().getEscrituraHasta());
      if (!retorno) {
         parametros.getParametrosBusquedaPersonal().setEscrituraDesde(BigInteger.ZERO);
         parametros.getParametrosBusquedaPersonal().setEscrituraHasta(BigInteger.ZERO);
         RequestContext.getCurrentInstance().update("form:opcionesIdioma:parametroEscrituraDesdeIdioma");
         RequestContext.getCurrentInstance().update("form:opcionesIdioma:parametroEscrituraHastaIdioma");
         RequestContext.getCurrentInstance().execute("PF('errorIdiomaPorcentajes').show()");
      }
   }

   public boolean validarFechas(Date fechaInicial, Date fechaFinal) {
      boolean retorno = false;
      if (fechaInicial != null && fechaFinal != null) {
         if (fechaInicial.before(fechaFinal)) {
            retorno = true;
         }
      }
      return retorno;
   }

   public boolean validarValores(BigDecimal valorMinimo, BigDecimal valorMaximo) {
      boolean retorno = false;
      if (valorMinimo != null && valorMaximo != null) {
         if (valorMinimo.compareTo(valorMaximo) < 0) {
            retorno = true;
         }
      }
      return retorno;
   }

   public boolean validarValoresEnteros(BigInteger valorMinimo, BigInteger valorMaximo) {
      boolean retorno = false;
      if (valorMinimo != null && valorMaximo != null) {
         if (valorMinimo.compareTo(valorMaximo) < 0) {
            retorno = true;
         }
      }
      return retorno;
   }

   //ARMAR QUERY BUSQUEDA AVANZADA
   public void ejecutarBusqueda() {
      //restaurar();
      System.out.println("Entro en Controlador.ControlBusquedaAvanzada.ejecutarBusqueda()");
      listaParametrosQueryModulos = new ArrayList<ParametrosQueryBusquedaAvanzada>();
      cargueQueryModuloNomina();
      cargueQueryModuloPersonal();
      String query = administrarBusquedaAvanzada.armarQueryModulosBusquedaAvanzada(listaParametrosQueryModulos);
      System.out.println("QUERY: " + query);
      String queryEmpleado = "SELECT codigoempleado FROM EMPLEADOS EM ";
//      if (!query.isEmpty()) {
      queryEmpleado = queryEmpleado + query;
      System.out.println("SUPER QUERY: " + queryEmpleado);
      listaCodigosEmpleado = administrarBusquedaAvanzada.ejecutarQueryBusquedaAvanzadaPorModulosCodigo(queryEmpleado);
      if (listaCodigosEmpleado != null) {
         System.out.println("listaCodigosEmpleado.size() : " + listaCodigosEmpleado.size());
         if (listaCodigosEmpleado.size() > 4) {
            System.out.println("listaCodigosEmpleado (Primeros 4): " + listaCodigosEmpleado.get(0) + ", " + listaCodigosEmpleado.get(1) + ", " + listaCodigosEmpleado.get(2) + ", " + listaCodigosEmpleado.get(3));
         }
      }

      FacesContext context = FacesContext.getCurrentInstance();
      String columnas = "";
      List<String> listCampoColumns = new ArrayList<String>();
      if (columnasEsSeleccionadas != null && !columnasEsSeleccionadas.isEmpty()) {
         for (int i = 0; i < columnasEsSeleccionadas.size(); i++) {
            if (i == 0) {
               columnas = columnasEsSeleccionadas.get(i).getNombrecolumna();
            } else {
               columnas += "," + columnasEsSeleccionadas.get(i).getNombrecolumna();
            }
         }
         String[] campoColumns = columnas.split(",");
         for (int i = 0; i < campoColumns.length; i++) {
            listCampoColumns.add(campoColumns[i]);
         }
      }
      listaResultadoBusqueda = administrarBusquedaAvanzada.obtenerQVWEmpleadosCortePorEmpleadoCodigoCompletos(listaCodigosEmpleado, listCampoColumns);
      listaSecuenciasEmpleado = new ArrayList<BigInteger>();
      if (listaResultadoBusqueda != null) {
         if (!listaResultadoBusqueda.isEmpty()) {
            for (int i = 0; i < listaResultadoBusqueda.size(); i++) {
               listaSecuenciasEmpleado.add(listaResultadoBusqueda.get(i).getSecuencia());
            }
         }
      }
      columnas = convertirListaAString();
      ControladorColumnasDinamicas controladorColumnasDinamicas = (ControladorColumnasDinamicas) context.getApplication().evaluateExpressionGet(context, "#{controladorColumnasDinamicas}", ControladorColumnasDinamicas.class);
      controladorColumnasDinamicas.updateColumns(columnas, primerResultado());
      RequestContext.getCurrentInstance().update("form:infoRegistro");
      RequestContext.getCurrentInstance().update("form:resultadoBusqueda");
      RequestContext.getCurrentInstance().execute("PF('detallesBusquedaDialogo').hide();");
   }

   public void cargueQueryModuloNomina() {
      System.out.println("Controlador.ControlBusquedaAvanzada.cargueQueryModuloNomina()");
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
      System.out.println("Controlador.ControlBusquedaAvanzada.cargueQueryModuloPersonal()");
      FacesContext c = FacesContext.getCurrentInstance();
      TabView t = (TabView) c.getViewRoot().findComponent("form:opcionesCosto");
      System.out.println("t : " + t);
      System.out.println("t.getActiveIndex() : " + t.getActiveIndex());
      if (t.getActiveIndex() == 1) {
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
      FacesContext c = FacesContext.getCurrentInstance();
      TabView t = (TabView) c.getViewRoot().findComponent("form:opcionesCosto");
      System.out.println("t : " + t);
      System.out.println("t.getActiveIndex() : " + t.getActiveIndex());
      if (t.getActiveIndex() == 1) {
         if (vTipoFechaCargo.equals("false")) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("CARGO", "BCARGO", "A");
            listaParametrosQueryModulos.add(parametro);
         }
         if (vTipoFechaCargo.equals("true")) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("CARGO", "BCARGO", "H");
            DateFormat df = DateFormat.getDateInstance();
            listaParametrosQueryModulos.add(parametro);
            ParametrosQueryBusquedaAvanzada parametro2 = null;
            ParametrosQueryBusquedaAvanzada parametro3 = null;
            if (this.parametros.getParametrosBusquedaNomina().getFechaInicialCargo() != null) {
               parametro2 = new ParametrosQueryBusquedaAvanzada("CARGO", "CARGODESDE", df.format(this.parametros.getParametrosBusquedaNomina().getFechaInicialCargo()).toString());
               listaParametrosQueryModulos.add(parametro2);
            }
            if (this.parametros.getParametrosBusquedaNomina().getFechaFinalCargo() != null) {
               parametro3 = new ParametrosQueryBusquedaAvanzada("CARGO", "CARGOHASTA", df.format(this.parametros.getParametrosBusquedaNomina().getFechaFinalCargo()).toString());
               listaParametrosQueryModulos.add(parametro3);
            }
         }
         if (this.parametros.getParametrosBusquedaNomina().getVigenciaCargo().getCargo().getSecuencia() != null) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("CARGO", "CARGO", this.parametros.getParametrosBusquedaNomina().getVigenciaCargo().getCargo().getSecuencia().toString());
            listaParametrosQueryModulos.add(parametro);
         }
         if (this.parametros.getParametrosBusquedaNomina().getVigenciaCargo().getEstructura().getSecuencia() != null) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("CARGO", "ESTRUCTURA", this.parametros.getParametrosBusquedaNomina().getVigenciaCargo().getEstructura().getSecuencia().toString());
            ParametrosQueryBusquedaAvanzada parametro2 = new ParametrosQueryBusquedaAvanzada("CARGO", "CENTROCOSTO", this.parametros.getParametrosBusquedaNomina().getVigenciaCargo().getEstructura().getCentrocosto().getSecuencia().toString());
            listaParametrosQueryModulos.add(parametro);
            listaParametrosQueryModulos.add(parametro2);
         }
         if (this.parametros.getParametrosBusquedaNomina().getVigenciaCargo().getEmpleadojefe().getSecuencia() != null) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("CARGO", "EMPLEADOJEFE", this.parametros.getParametrosBusquedaNomina().getVigenciaCargo().getEmpleadojefe().getSecuencia().toString());
            listaParametrosQueryModulos.add(parametro);
         }
         if (this.parametros.getParametrosBusquedaNomina().getVigenciaCargo().getMotivocambiocargo().getSecuencia() != null) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("CARGO", "MOTIVOCAMBIOCARGO", this.parametros.getParametrosBusquedaNomina().getVigenciaCargo().getMotivocambiocargo().getSecuencia().toString());
            listaParametrosQueryModulos.add(parametro);
         }
         if (this.parametros.getParametrosBusquedaNomina().getVigenciaCargo().getPapel().getSecuencia() != null) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("CARGO", "PAPEL", this.parametros.getParametrosBusquedaNomina().getVigenciaCargo().getPapel().getSecuencia().toString());
            listaParametrosQueryModulos.add(parametro);
         }
      }
   }

   public void cargueParametrosModuloCentroCosto() {
      FacesContext c = FacesContext.getCurrentInstance();
      TabView t = (TabView) c.getViewRoot().findComponent("form:opcionesCentroCosto");
      System.out.println("t : " + t);
      System.out.println("t.getActiveIndex() : " + t.getActiveIndex());
//      System.out.println("cargueModuloCentroCosto : " + cargueModuloCentroCosto);
      if (t.getActiveIndex() == 1) {
         if (vTipoFechaCentroCosto.equals("false")) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("CENTROCOSTO", "BCENTROCOSTO", "A");
            listaParametrosQueryModulos.add(parametro);
         }
         if (vTipoFechaCentroCosto.equals("true")) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("CENTROCOSTO", "BCENTROCOSTO", "H");
            DateFormat df = DateFormat.getDateInstance();
            listaParametrosQueryModulos.add(parametro);
            ParametrosQueryBusquedaAvanzada parametro2 = null;
            ParametrosQueryBusquedaAvanzada parametro3 = null;
            if (this.parametros.getParametrosBusquedaNomina().getFechaInicialCentroCosto() != null) {
               parametro2 = new ParametrosQueryBusquedaAvanzada("CENTROCOSTO", "CENTROCOSTODESDE", df.format(this.parametros.getParametrosBusquedaNomina().getFechaInicialCentroCosto()).toString());
               listaParametrosQueryModulos.add(parametro2);
            }
            if (this.parametros.getParametrosBusquedaNomina().getFechaFinalCentroCosto() != null) {
               parametro3 = new ParametrosQueryBusquedaAvanzada("CENTROCOSTO", "CENTROCOSTOHASTA", df.format(this.parametros.getParametrosBusquedaNomina().getFechaFinalCentroCosto()).toString());
               listaParametrosQueryModulos.add(parametro3);
            }
         }
         if (this.parametros.getParametrosBusquedaNomina().getVigenciaLocalizacion().getLocalizacion().getSecuencia() != null) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("CENTROCOSTO", "LOCALIZACION", this.parametros.getParametrosBusquedaNomina().getVigenciaLocalizacion().getLocalizacion().getSecuencia().toString());
            listaParametrosQueryModulos.add(parametro);
         }
         if (this.parametros.getParametrosBusquedaNomina().getVigenciaLocalizacion().getMotivo().getSecuencia() != null) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("CENTROCOSTO", "MOTIVOLOCALIZACION", this.parametros.getParametrosBusquedaNomina().getVigenciaLocalizacion().getMotivo().getSecuencia().toString());
            listaParametrosQueryModulos.add(parametro);
         }
      }
   }

   public void cargueParametrosModuloSueldo() {
      FacesContext c = FacesContext.getCurrentInstance();
      TabView t = (TabView) c.getViewRoot().findComponent("form:opcionesSueldo");
      System.out.println("t : " + t);
      System.out.println("t.getActiveIndex() : " + t.getActiveIndex());
//      System.out.println("cargueModuloSueldo : " + cargueModuloSueldo);
      if (t.getActiveIndex() == 1) {
         if (vTipoFechaSueldo.equals("false")) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("SUELDO", "BSUELDO", "A");
            listaParametrosQueryModulos.add(parametro);
         }
         if (vTipoFechaSueldo.equals("true")) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("SUELDO", "BSUELDO", "H");
            DateFormat df = DateFormat.getDateInstance();
            listaParametrosQueryModulos.add(parametro);
            ParametrosQueryBusquedaAvanzada parametro2 = null;
            ParametrosQueryBusquedaAvanzada parametro3 = null;
            if (this.parametros.getParametrosBusquedaNomina().getFechaInicialSueldo() != null) {
               parametro2 = new ParametrosQueryBusquedaAvanzada("SUELDO", "SUELDODESDE", df.format(this.parametros.getParametrosBusquedaNomina().getFechaInicialSueldo()).toString());
               listaParametrosQueryModulos.add(parametro2);
            }
            if (this.parametros.getParametrosBusquedaNomina().getFechaFinalSueldo() != null) {
               parametro3 = new ParametrosQueryBusquedaAvanzada("SUELDO", "SUELDOHASTA", df.format(this.parametros.getParametrosBusquedaNomina().getFechaFinalSueldo()).toString());
               listaParametrosQueryModulos.add(parametro3);
            }
         }
         if (this.parametros.getParametrosBusquedaNomina().getVigenciaSueldo().getTiposueldo().getSecuencia() != null) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("SUELDO", "TIPOSUELDO", this.parametros.getParametrosBusquedaNomina().getVigenciaSueldo().getTiposueldo().getSecuencia().toString());
            listaParametrosQueryModulos.add(parametro);
         }
         if (this.parametros.getParametrosBusquedaNomina().getVigenciaSueldo().getMotivocambiosueldo().getSecuencia() != null) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("SUELDO", "MOTIVOCAMBIOSUELDO", this.parametros.getParametrosBusquedaNomina().getVigenciaSueldo().getMotivocambiosueldo().getSecuencia().toString());
            listaParametrosQueryModulos.add(parametro);
         }
         if (this.parametros.getParametrosBusquedaNomina().getSueldoMinimo() != null) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("SUELDO", "SUELDOMINIMO", this.parametros.getParametrosBusquedaNomina().getSueldoMinimo().toString());
            listaParametrosQueryModulos.add(parametro);
         }
         if (this.parametros.getParametrosBusquedaNomina().getSueldoMaximo() != null) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("SUELDO", "SUELDOMAXIMO", this.parametros.getParametrosBusquedaNomina().getSueldoMaximo().toString());
            listaParametrosQueryModulos.add(parametro);
         }
      }
   }

   public void cargueParametrosModuloFechaContrato() {
      FacesContext c = FacesContext.getCurrentInstance();
      TabView t = (TabView) c.getViewRoot().findComponent("form:opcionesTipoContrato");
      System.out.println("t : " + t);
      System.out.println("t.getActiveIndex() : " + t.getActiveIndex());
      if (t.getActiveIndex() == 1) {
         if (vTipoFechaTipoContrato.equals("false")) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("FECHACONTRATO", "BFECHACONTRATO", "A");
            listaParametrosQueryModulos.add(parametro);
         }
         if (vTipoFechaTipoContrato.equals("true")) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("FECHACONTRATO", "BFECHACONTRATO", "H");
            DateFormat df = DateFormat.getDateInstance();
            listaParametrosQueryModulos.add(parametro);
            ParametrosQueryBusquedaAvanzada parametro2 = null;
            ParametrosQueryBusquedaAvanzada parametro3 = null;
            if (this.parametros.getParametrosBusquedaNomina().getFechaInicialTipoContrato() != null) {
               parametro2 = new ParametrosQueryBusquedaAvanzada("FECHACONTRATO", "FECHACONTRATODESDE", df.format(this.parametros.getParametrosBusquedaNomina().getFechaInicialTipoContrato()).toString());
               listaParametrosQueryModulos.add(parametro2);
            }
            if (this.parametros.getParametrosBusquedaNomina().getFechaFinalTipoContrato() != null) {
               parametro3 = new ParametrosQueryBusquedaAvanzada("FECHACONTRATO", "FECHACONTRATOHASTA", df.format(this.parametros.getParametrosBusquedaNomina().getFechaFinalTipoContrato()).toString());
               listaParametrosQueryModulos.add(parametro3);
            }
         }
         if (this.parametros.getParametrosBusquedaNomina().getVigenciaTipoContrato().getTipocontrato().getSecuencia() != null) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("FECHACONTRATO", "TIPOCONTRATO", this.parametros.getParametrosBusquedaNomina().getVigenciaTipoContrato().getTipocontrato().getSecuencia().toString());
            listaParametrosQueryModulos.add(parametro);
         }
         if (this.parametros.getParametrosBusquedaNomina().getVigenciaTipoContrato().getMotivocontrato().getSecuencia() != null) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("FECHACONTRATO", "MOTIVOCONTRATO", this.parametros.getParametrosBusquedaNomina().getVigenciaTipoContrato().getMotivocontrato().getSecuencia().toString());
            listaParametrosQueryModulos.add(parametro);
         }
      }
   }

   public void cargueParametrosModuloTipoTrabajador() {
      FacesContext c = FacesContext.getCurrentInstance();
      TabView t = (TabView) c.getViewRoot().findComponent("form:opcionesTipoTrabajador");
      System.out.println("t : " + t);
      System.out.println("t.getActiveIndex() : " + t.getActiveIndex());
      if (t.getActiveIndex() == 1) {
         if (vTipoFechaTipoTrabajador.equals("false")) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("TIPOTRABAJADOR", "BTIPOTRABAJADOR", "A");
            listaParametrosQueryModulos.add(parametro);
         }
         if (vTipoFechaTipoTrabajador.equals("true")) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("TIPOTRABAJADOR", "BTIPOTRABAJADOR", "H");
            DateFormat df = DateFormat.getDateInstance();
            listaParametrosQueryModulos.add(parametro);
            ParametrosQueryBusquedaAvanzada parametro2 = null;
            ParametrosQueryBusquedaAvanzada parametro3 = null;
            if (this.parametros.getParametrosBusquedaNomina().getFechaInicialTipoTrabajador() != null) {
               parametro2 = new ParametrosQueryBusquedaAvanzada("TIPOTRABAJADOR", "TIPOTRABAJADORDESDE", df.format(this.parametros.getParametrosBusquedaNomina().getFechaInicialTipoTrabajador()).toString());
               listaParametrosQueryModulos.add(parametro2);
            }
            if (this.parametros.getParametrosBusquedaNomina().getFechaFinalTipoTrabajador() != null) {
               parametro3 = new ParametrosQueryBusquedaAvanzada("TIPOTRABAJADOR", "TIPOTRABAJADORHASTA", df.format(this.parametros.getParametrosBusquedaNomina().getFechaFinalTipoTrabajador()).toString());
               listaParametrosQueryModulos.add(parametro3);
            }
         }
         if (this.parametros.getParametrosBusquedaNomina().getVigenciaTipoTrabajador().getTipotrabajador().getSecuencia() != null) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("TIPOTRABAJADOR", "TIPOTRABAJADOR", this.parametros.getParametrosBusquedaNomina().getVigenciaTipoTrabajador().getTipotrabajador().getSecuencia().toString());
            listaParametrosQueryModulos.add(parametro);
         }
      }
   }

   public void cargueParametrosModuloTipoSalario() {
      FacesContext c = FacesContext.getCurrentInstance();
      TabView t = (TabView) c.getViewRoot().findComponent("form:opcionesTipoSalario");
      System.out.println("t : " + t);
      System.out.println("t.getActiveIndex() : " + t.getActiveIndex());
      if (t.getActiveIndex() == 1) {
         if (vTipoFechaReformaLaboral.equals("false")) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("TIPOSALARIO", "BTIPOSALARIO", "A");
            listaParametrosQueryModulos.add(parametro);
         }
         if (vTipoFechaReformaLaboral.equals("true")) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("TIPOSALARIO", "BTIPOSALARIO", "H");
            DateFormat df = DateFormat.getDateInstance();
            listaParametrosQueryModulos.add(parametro);
            ParametrosQueryBusquedaAvanzada parametro2 = null;
            ParametrosQueryBusquedaAvanzada parametro3 = null;
            if (this.parametros.getParametrosBusquedaNomina().getFechaInicialReformaLaboral() != null) {
               parametro2 = new ParametrosQueryBusquedaAvanzada("TIPOSALARIO", "TIPOSALARIODESDE", df.format(this.parametros.getParametrosBusquedaNomina().getFechaInicialReformaLaboral()).toString());
               listaParametrosQueryModulos.add(parametro2);
            }
            if (this.parametros.getParametrosBusquedaNomina().getFechaFinalReformaLaboral() != null) {
               parametro3 = new ParametrosQueryBusquedaAvanzada("TIPOSALARIO", "TIPOSALARIOHASTA", df.format(this.parametros.getParametrosBusquedaNomina().getFechaFinalReformaLaboral()).toString());
               listaParametrosQueryModulos.add(parametro3);
            }
         }
         if (this.parametros.getParametrosBusquedaNomina().getVigenciaReformasLaboral().getReformalaboral().getSecuencia() != null) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("TIPOSALARIO", "REFORMA", this.parametros.getParametrosBusquedaNomina().getVigenciaReformasLaboral().getReformalaboral().getSecuencia().toString());
            listaParametrosQueryModulos.add(parametro);
         }
      }
   }

   public void cargueParametrosModuloNormaLaboral() {
      FacesContext c = FacesContext.getCurrentInstance();
      TabView t = (TabView) c.getViewRoot().findComponent("form:opcionesNormaLaboral");
      System.out.println("t : " + t);
      System.out.println("t.getActiveIndex() : " + t.getActiveIndex());
      if (t.getActiveIndex() == 1) {
         if (vTipoFechaNormaLaboral.equals("false")) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("NORMALABORAL", "BNORMALABORAL", "A");
            listaParametrosQueryModulos.add(parametro);
         }
         if (vTipoFechaNormaLaboral.equals("true")) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("NORMALABORAL", "BNORMALABORAL", "H");

            DateFormat df = DateFormat.getDateInstance();
            listaParametrosQueryModulos.add(parametro);
            ParametrosQueryBusquedaAvanzada parametro2 = null;
            ParametrosQueryBusquedaAvanzada parametro3 = null;
            if (this.parametros.getParametrosBusquedaNomina().getFechaInicialNormaLaboral() != null) {
               parametro2 = new ParametrosQueryBusquedaAvanzada("NORMALABORAL", "NORMALABORALDESDE", df.format(this.parametros.getParametrosBusquedaNomina().getFechaInicialNormaLaboral()).toString());
               listaParametrosQueryModulos.add(parametro2);
            }
            if (this.parametros.getParametrosBusquedaNomina().getFechaFinalNormaLaboral() != null) {
               parametro3 = new ParametrosQueryBusquedaAvanzada("NORMALABORAL", "NORMALABORALHASTA", df.format(this.parametros.getParametrosBusquedaNomina().getFechaFinalNormaLaboral()).toString());
               listaParametrosQueryModulos.add(parametro3);
            }
         }
         if (this.parametros.getParametrosBusquedaNomina().getVigenciaNormaEmpleado().getNormalaboral().getSecuencia() != null) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("NORMALABORAL", "NORMA", this.parametros.getParametrosBusquedaNomina().getVigenciaNormaEmpleado().getNormalaboral().getSecuencia().toString());
            listaParametrosQueryModulos.add(parametro);
         }
      }
   }

   public void cargueParametrosModuloLegislacionLaboral() {
      FacesContext c = FacesContext.getCurrentInstance();
      TabView t = (TabView) c.getViewRoot().findComponent("form:opcionesLegislacionLaboral");
      System.out.println("t : " + t);
      System.out.println("t.getActiveIndex() : " + t.getActiveIndex());
      if (t.getActiveIndex() == 1) {
         if (vTipoFechaLegislacion.equals("false")) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("LEGISLACIONLABORAL", "BLEGISLACIONLABORAL", "A");
            listaParametrosQueryModulos.add(parametro);
         }
         if (vTipoFechaLegislacion.equals("true")) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("LEGISLACIONLABORAL", "BLEGISLACIONLABORAL", "H");
            DateFormat df = DateFormat.getDateInstance();
            listaParametrosQueryModulos.add(parametro);
            ParametrosQueryBusquedaAvanzada parametro2 = null;
            ParametrosQueryBusquedaAvanzada parametro3 = null;
            ParametrosQueryBusquedaAvanzada parametro4 = null;
            ParametrosQueryBusquedaAvanzada parametro5 = null;
            if (this.parametros.getParametrosBusquedaNomina().getFechaInicialLegislacionMI() != null) {
               parametro2 = new ParametrosQueryBusquedaAvanzada("LEGISLACIONLABORAL", "LEGISLACIONLABORALDESDE", df.format(this.parametros.getParametrosBusquedaNomina().getFechaInicialLegislacionMI()).toString());
               listaParametrosQueryModulos.add(parametro2);
            }
            if (this.parametros.getParametrosBusquedaNomina().getFechaFinalLegislacionMI() != null) {
               parametro3 = new ParametrosQueryBusquedaAvanzada("LEGISLACIONLABORAL", "LEGISLACIONLABORALHASTA", df.format(this.parametros.getParametrosBusquedaNomina().getFechaFinalLegislacionMI()).toString());
               listaParametrosQueryModulos.add(parametro3);
            }
            if (this.parametros.getParametrosBusquedaNomina().getFechaInicialLegislacionMF() != null) {
               parametro4 = new ParametrosQueryBusquedaAvanzada("LEGISLACIONLABORAL", "LEGISLACIONLABORALDESDEF", df.format(this.parametros.getParametrosBusquedaNomina().getFechaInicialLegislacionMF()).toString());
               listaParametrosQueryModulos.add(parametro4);
            }
            if (this.parametros.getParametrosBusquedaNomina().getFechaFinalLegislacionMF() != null) {
               parametro5 = new ParametrosQueryBusquedaAvanzada("LEGISLACIONLABORAL", "LEGISLACIONLABORALHASTAF", df.format(this.parametros.getParametrosBusquedaNomina().getFechaFinalLegislacionMF()).toString());
               listaParametrosQueryModulos.add(parametro5);
            }
         }
         if (this.parametros.getParametrosBusquedaNomina().getVigenciaContrato().getContrato().getSecuencia() != null) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("LEGISLACIONLABORAL", "CONTRATO", this.parametros.getParametrosBusquedaNomina().getVigenciaContrato().getContrato().getSecuencia().toString());
            listaParametrosQueryModulos.add(parametro);
         }
      }
   }

   public void cargueParametrosModuloUbicacionGeografica() {
      FacesContext c = FacesContext.getCurrentInstance();
      TabView t = (TabView) c.getViewRoot().findComponent("form:opcionesUbicacion");
      System.out.println("t : " + t);
      System.out.println("t.getActiveIndex() : " + t.getActiveIndex());
      if (t.getActiveIndex() == 1) {
         if (vTipoFechaUbicacion.equals("false")) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("UBICACION", "BUBICACION", "A");
            listaParametrosQueryModulos.add(parametro);
         }
         if (vTipoFechaUbicacion.equals("true")) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("UBICACION", "BUBICACION", "H");
            DateFormat df = DateFormat.getDateInstance();
            listaParametrosQueryModulos.add(parametro);
            ParametrosQueryBusquedaAvanzada parametro2 = null;
            ParametrosQueryBusquedaAvanzada parametro3 = null;
            if (this.parametros.getParametrosBusquedaNomina().getFechaInicialUbicacion() != null) {
               parametro2 = new ParametrosQueryBusquedaAvanzada("UBICACION", "UBICACIONDESDE", df.format(this.parametros.getParametrosBusquedaNomina().getFechaInicialUbicacion()).toString());
               listaParametrosQueryModulos.add(parametro2);
            }
            if (this.parametros.getParametrosBusquedaNomina().getFechaFinalUbicacion() != null) {
               parametro3 = new ParametrosQueryBusquedaAvanzada("UBICACION", "UBICACIONHASTA", df.format(this.parametros.getParametrosBusquedaNomina().getFechaFinalUbicacion()).toString());
               listaParametrosQueryModulos.add(parametro3);
            }
         }
         if (this.parametros.getParametrosBusquedaNomina().getVigenciaUbicacion().getUbicacion().getSecuencia() != null) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("UBICACION", "UBICACION", this.parametros.getParametrosBusquedaNomina().getVigenciaUbicacion().getUbicacion().getSecuencia().toString());
            listaParametrosQueryModulos.add(parametro);
         }
      }
   }

   public void cargueParametrosModuloAfiliaciones() {
      FacesContext c = FacesContext.getCurrentInstance();
      TabView t = (TabView) c.getViewRoot().findComponent("form:opcionesAfiliacion");
      System.out.println("t : " + t);
      System.out.println("t.getActiveIndex() : " + t.getActiveIndex());
      if (t.getActiveIndex() == 1) {
         if (vTipoFechaAfiliacion.equals("false")) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("AFILIACIONES", "BAFILIACIONES", "A");
            listaParametrosQueryModulos.add(parametro);
         }
         if (vTipoFechaAfiliacion.equals("true")) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("AFILIACIONES", "BAFILIACIONES", "H");
            listaParametrosQueryModulos.add(parametro);
            DateFormat df = DateFormat.getDateInstance();
            ParametrosQueryBusquedaAvanzada parametro2 = null;
            ParametrosQueryBusquedaAvanzada parametro3 = null;
            if (this.parametros.getParametrosBusquedaNomina().getFechaInicialAfiliacion() != null) {
               parametro2 = new ParametrosQueryBusquedaAvanzada("AFILIACIONES", "AFILIACIONESDESDE", df.format(this.parametros.getParametrosBusquedaNomina().getFechaInicialAfiliacion()).toString());
               listaParametrosQueryModulos.add(parametro2);
            }
            if (this.parametros.getParametrosBusquedaNomina().getFechaFinalAfiliacion() != null) {
               parametro3 = new ParametrosQueryBusquedaAvanzada("AFILIACIONES", "AFILIACIONESHASTA", df.format(this.parametros.getParametrosBusquedaNomina().getFechaFinalAfiliacion()).toString());
               listaParametrosQueryModulos.add(parametro3);
            }
         }
         if (this.parametros.getParametrosBusquedaNomina().getVigenciaAfiliacion().getTercerosucursal().getSecuencia() != null) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("AFILIACIONES", "TERCERO", this.parametros.getParametrosBusquedaNomina().getVigenciaAfiliacion().getTercerosucursal().getSecuencia().toString());
            listaParametrosQueryModulos.add(parametro);
         }
         if (this.parametros.getParametrosBusquedaNomina().getVigenciaAfiliacion().getTipoentidad().getSecuencia() != null) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("AFILIACIONES", "TIPOENTIDAD", this.parametros.getParametrosBusquedaNomina().getVigenciaAfiliacion().getTipoentidad().getSecuencia().toString());
            listaParametrosQueryModulos.add(parametro);
         }
         if (this.parametros.getParametrosBusquedaNomina().getVigenciaAfiliacion().getEstadoafiliacion().getSecuencia() != null) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("AFILIACIONES", "ESTADO", this.parametros.getParametrosBusquedaNomina().getVigenciaAfiliacion().getEstadoafiliacion().getSecuencia().toString());
            listaParametrosQueryModulos.add(parametro);
         }
      }
   }

   public void cargueParametrosModuloFormaPago() {
      FacesContext c = FacesContext.getCurrentInstance();
      TabView t = (TabView) c.getViewRoot().findComponent("form:opcionesFormaPago");
      System.out.println("t : " + t);
      System.out.println("t.getActiveIndex() : " + t.getActiveIndex());
      if (t.getActiveIndex() == 1) {
         if (vTipoFechaFormaPago.equals("false")) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("FORMAPAGO", "BFORMAPAGO", "A");
            listaParametrosQueryModulos.add(parametro);
         }
         if (vTipoFechaFormaPago.equals("true")) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("FORMAPAGO", "BFORMAPAGO", "H");

            listaParametrosQueryModulos.add(parametro);
            DateFormat df = DateFormat.getDateInstance();
            ParametrosQueryBusquedaAvanzada parametro2 = null;
            ParametrosQueryBusquedaAvanzada parametro3 = null;
            if (this.parametros.getParametrosBusquedaNomina().getFechaInicialFormaPago() != null) {
               parametro2 = new ParametrosQueryBusquedaAvanzada("FORMAPAGO", "FORMAPAGODESDE", df.format(this.parametros.getParametrosBusquedaNomina().getFechaInicialFormaPago()).toString());
               listaParametrosQueryModulos.add(parametro2);
            }
            if (this.parametros.getParametrosBusquedaNomina().getFechaFinalFormaPago() != null) {
               parametro3 = new ParametrosQueryBusquedaAvanzada("FORMAPAGO", "FORMAPAGOHASTA", df.format(this.parametros.getParametrosBusquedaNomina().getFechaFinalFormaPago()).toString());
               listaParametrosQueryModulos.add(parametro3);
            }
         }
         if (this.parametros.getParametrosBusquedaNomina().getVigenciaFormaPago().getFormapago().getSecuencia() != null) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("FORMAPAGO", "FORMAPAGO", this.parametros.getParametrosBusquedaNomina().getVigenciaFormaPago().getFormapago().getSecuencia().toString());
            listaParametrosQueryModulos.add(parametro);
         }
         if (this.parametros.getParametrosBusquedaNomina().getVigenciaFormaPago().getSucursal().getSecuencia() != null) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("FORMAPAGO", "SUCURSAL", this.parametros.getParametrosBusquedaNomina().getVigenciaFormaPago().getSucursal().getSecuencia().toString());
            listaParametrosQueryModulos.add(parametro);
         }

      }
   }

   public void cargueParametrosModuloMVR() {
      FacesContext c = FacesContext.getCurrentInstance();
      TabView t = (TabView) c.getViewRoot().findComponent("form:opcionesMvrs");
      System.out.println("t : " + t);
      System.out.println("t.getActiveIndex() : " + t.getActiveIndex());
      if (t.getActiveIndex() == 1) {
         if (vTipoFechaMvr.equals("false")) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("MVRS", "BMVRS", "A");
            listaParametrosQueryModulos.add(parametro);
         }
         if (vTipoFechaMvr.equals("true")) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("MVRS", "BMVRS", "H");
            listaParametrosQueryModulos.add(parametro);
            DateFormat df = DateFormat.getDateInstance();
            ParametrosQueryBusquedaAvanzada parametro2 = null;
            ParametrosQueryBusquedaAvanzada parametro3 = null;
            if (this.parametros.getParametrosBusquedaNomina().getFechaInicialMvr() != null) {
               parametro2 = new ParametrosQueryBusquedaAvanzada("MVRS", "MVRSDESDE", df.format(this.parametros.getParametrosBusquedaNomina().getFechaInicialMvr()).toString());
               listaParametrosQueryModulos.add(parametro2);
            }
            if (this.parametros.getParametrosBusquedaNomina().getFechaFinalMvr() != null) {
               parametro3 = new ParametrosQueryBusquedaAvanzada("MVRS", "MVRSHASTA", df.format(this.parametros.getParametrosBusquedaNomina().getFechaFinalMvr()).toString());
               listaParametrosQueryModulos.add(parametro3);
            }
         }
         if (this.parametros.getParametrosBusquedaNomina().getMvrs().getMotivo().getSecuencia() != null) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("MVRS", "MOTIVO", this.parametros.getParametrosBusquedaNomina().getMvrs().getMotivo().getSecuencia().toString());
            listaParametrosQueryModulos.add(parametro);
         }
         if (this.parametros.getParametrosBusquedaNomina().getSueldoMinimoMvr() != null) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("MVRS", "SUELDOMINIMO", this.parametros.getParametrosBusquedaNomina().getSueldoMinimoMvr().toString());
            listaParametrosQueryModulos.add(parametro);
         }
         if (this.parametros.getParametrosBusquedaNomina().getSueldoMaximoMvr() != null) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("MVRS", "SUELDOMAXIMO", this.parametros.getParametrosBusquedaNomina().getSueldoMaximoMvr().toString());
            listaParametrosQueryModulos.add(parametro);
         }
      }
   }

   public void cargueParametrosModuloSET() {
      FacesContext c = FacesContext.getCurrentInstance();
      TabView t = (TabView) c.getViewRoot().findComponent("form:opcionesSets");
      System.out.println("t : " + t);
      System.out.println("t.getActiveIndex() : " + t.getActiveIndex());
      if (t.getActiveIndex() == 1) {
         if (vTipoFechaSet.equals("false")) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("SETS", "BSETS", "A");
            listaParametrosQueryModulos.add(parametro);
         }
         if (vTipoFechaSet.equals("true")) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("SETS", "BSETS", "H");

            listaParametrosQueryModulos.add(parametro);
            DateFormat df = DateFormat.getDateInstance();
            ParametrosQueryBusquedaAvanzada parametro2 = null;
            ParametrosQueryBusquedaAvanzada parametro3 = null;
            ParametrosQueryBusquedaAvanzada parametro4 = null;
            ParametrosQueryBusquedaAvanzada parametro5 = null;
            if (this.parametros.getParametrosBusquedaNomina().getFechaInicialSetMI() != null) {
               parametro2 = new ParametrosQueryBusquedaAvanzada("SETS", "SETSDESDE", df.format(this.parametros.getParametrosBusquedaNomina().getFechaInicialSetMI()).toString());
               listaParametrosQueryModulos.add(parametro2);
            }
            if (this.parametros.getParametrosBusquedaNomina().getFechaFinalSetMI() != null) {
               parametro3 = new ParametrosQueryBusquedaAvanzada("SETS", "SETSHASTA", df.format(this.parametros.getParametrosBusquedaNomina().getFechaFinalSetMI()).toString());
               listaParametrosQueryModulos.add(parametro3);
            }
            if (this.parametros.getParametrosBusquedaNomina().getFechaInicialSetMF() != null) {
               parametro4 = new ParametrosQueryBusquedaAvanzada("SETS", "SETSDESDEF", df.format(this.parametros.getParametrosBusquedaNomina().getFechaInicialSetMF()).toString());
               listaParametrosQueryModulos.add(parametro4);
            }
            if (this.parametros.getParametrosBusquedaNomina().getFechaFinalSetMF() != null) {
               parametro5 = new ParametrosQueryBusquedaAvanzada("SETS", "SETSHASTAF", df.format(this.parametros.getParametrosBusquedaNomina().getFechaFinalSetMF()).toString());
               listaParametrosQueryModulos.add(parametro5);
            }
         }
         if (this.parametros.getParametrosBusquedaNomina().getTipoMetodoSet() != null && !this.parametros.getParametrosBusquedaNomina().getTipoMetodoSet().isEmpty()) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("SETS", "METODO", this.parametros.getParametrosBusquedaNomina().getTipoMetodoSet());
            listaParametrosQueryModulos.add(parametro);
         }
         if (this.parametros.getParametrosBusquedaNomina().getPromedioMinimoSet() != null) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("SETS", "PROMEDIOMINIMO", this.parametros.getParametrosBusquedaNomina().getPromedioMinimoSet().toString());
            listaParametrosQueryModulos.add(parametro);
         }
         if (this.parametros.getParametrosBusquedaNomina().getPromedioMaximoSet() != null) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("SETS", "PROMEDIOMAXIMO", this.parametros.getParametrosBusquedaNomina().getPromedioMaximoSet().toString());
            listaParametrosQueryModulos.add(parametro);
         }
      }
   }

   public void cargueParametrosModuloVacaciones() {
      FacesContext c = FacesContext.getCurrentInstance();
      TabView t = (TabView) c.getViewRoot().findComponent("form:opcionesVacacion");
      System.out.println("t : " + t);
      System.out.println("t.getActiveIndex() : " + t.getActiveIndex());
      if (t.getActiveIndex() == 1) {
         ParametrosQueryBusquedaAvanzada parametroInicial = new ParametrosQueryBusquedaAvanzada("VACACIONES", "NN", "NN");
         listaParametrosQueryModulos.add(parametroInicial);
         if (this.parametros.getParametrosBusquedaNomina().getFechaInicialVacacionesMI() != null) {
            DateFormat df = DateFormat.getDateInstance();
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("VACACIONES", "FECHASALIDADESDE", df.format(this.parametros.getParametrosBusquedaNomina().getFechaInicialVacacionesMI()).toString());
            listaParametrosQueryModulos.add(parametro);
         }
         if (this.parametros.getParametrosBusquedaNomina().getFechaFinalVacacionesMI() != null) {
            DateFormat df = DateFormat.getDateInstance();
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("VACACIONES", "FECHASALIDAHASTA", df.format(this.parametros.getParametrosBusquedaNomina().getFechaFinalVacacionesMI()).toString());
            listaParametrosQueryModulos.add(parametro);
         }
         if (this.parametros.getParametrosBusquedaNomina().getFechaInicialVacacionesMF() != null) {
            DateFormat df = DateFormat.getDateInstance();
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("VACACIONES", "FECHAREGRESODESDE", df.format(this.parametros.getParametrosBusquedaNomina().getFechaInicialVacacionesMF()).toString());
            listaParametrosQueryModulos.add(parametro);
         }
         if (this.parametros.getParametrosBusquedaNomina().getFechaFinalVacacionesMF() != null) {
            DateFormat df = DateFormat.getDateInstance();
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("VACACIONES", "FECHAREGRESOHASTA", df.format(this.parametros.getParametrosBusquedaNomina().getFechaFinalVacacionesMF()).toString());
            listaParametrosQueryModulos.add(parametro);
         }
      }
   }

   public void cargueParametrosModuloFechaRetiro() {
      FacesContext c = FacesContext.getCurrentInstance();
      TabView t = (TabView) c.getViewRoot().findComponent("form:opcionesRetiro");
      System.out.println("t : " + t);
      System.out.println("t.getActiveIndex() : " + t.getActiveIndex());
      if (t.getActiveIndex() == 1) {
         ParametrosQueryBusquedaAvanzada parametroInicial = new ParametrosQueryBusquedaAvanzada("FECHARETIRO", "NN", "NN");
         listaParametrosQueryModulos.add(parametroInicial);
         if (this.parametros.getParametrosBusquedaNomina().getFechaInicialRetiro() != null) {
            DateFormat df = DateFormat.getDateInstance();
            ParametrosQueryBusquedaAvanzada parametro2 = new ParametrosQueryBusquedaAvanzada("FECHARETIRO", "FECHARETIRODESDE", df.format(this.parametros.getParametrosBusquedaNomina().getFechaInicialRetiro()).toString());
            listaParametrosQueryModulos.add(parametro2);
         }
         if (this.parametros.getParametrosBusquedaNomina().getFechaFinalRetiro() != null) {
            DateFormat df = DateFormat.getDateInstance();
            ParametrosQueryBusquedaAvanzada parametro3 = new ParametrosQueryBusquedaAvanzada("FECHARETIRO", "FECHARETIROHASTA", df.format(this.parametros.getParametrosBusquedaNomina().getFechaFinalRetiro()).toString());
            listaParametrosQueryModulos.add(parametro3);
         }
         if (this.parametros.getParametrosBusquedaNomina().getMotivosRetiros().getSecuencia() != null) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("FECHARETIRO", "MOTIVO", this.parametros.getParametrosBusquedaNomina().getMotivosRetiros().getSecuencia().toString());
            listaParametrosQueryModulos.add(parametro);
         }
      }
   }

   public void cargueParametrosModuloJornadaLaboral() {
      FacesContext c = FacesContext.getCurrentInstance();
      TabView t = (TabView) c.getViewRoot().findComponent("form:opcionesJornadaLaboral");
      System.out.println("t : " + t);
      System.out.println("t.getActiveIndex() : " + t.getActiveIndex());
      if (t.getActiveIndex() == 1) {
         ParametrosQueryBusquedaAvanzada parametroInicial = new ParametrosQueryBusquedaAvanzada("JORNADALABORAL", "NN", "NN");
         listaParametrosQueryModulos.add(parametroInicial);
         if (this.parametros.getParametrosBusquedaNomina().getFechaInicialJornadaLaboral() != null) {
            DateFormat df = DateFormat.getDateInstance();
            ParametrosQueryBusquedaAvanzada parametro2 = new ParametrosQueryBusquedaAvanzada("JORNADALABORAL", "JORNADALABORALDESDE", df.format(this.parametros.getParametrosBusquedaNomina().getFechaInicialJornadaLaboral()).toString());
            listaParametrosQueryModulos.add(parametro2);
         }
         if (this.parametros.getParametrosBusquedaNomina().getFechaFinalJornadaLaboral() != null) {
            DateFormat df = DateFormat.getDateInstance();
            ParametrosQueryBusquedaAvanzada parametro3 = new ParametrosQueryBusquedaAvanzada("JORNADALABORAL", "JORNADALABORALHASTA", df.format(this.parametros.getParametrosBusquedaNomina().getFechaFinalJornadaLaboral()).toString());
            listaParametrosQueryModulos.add(parametro3);
         }
         if (this.parametros.getParametrosBusquedaNomina().getVigenciaJornada().getJornadatrabajo().getSecuencia() != null) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("JORNADALABORAL", "JORNADA", this.parametros.getParametrosBusquedaNomina().getVigenciaJornada().getJornadatrabajo().getSecuencia().toString());
            listaParametrosQueryModulos.add(parametro);
         }
      }
   }

   public void cargueParametrosModuloDatosPersonales() {
      FacesContext c = FacesContext.getCurrentInstance();
      TabView t = (TabView) c.getViewRoot().findComponent("form:opcionesDatosPersonales");
      System.out.println("t : " + t);
      System.out.println("t.getActiveIndex() : " + t.getActiveIndex());
      if (t.getActiveIndex() == 1) {

         ParametrosQueryBusquedaAvanzada parametroInicial = new ParametrosQueryBusquedaAvanzada("DATOSPERSONALES", "NN", "NN");
         listaParametrosQueryModulos.add(parametroInicial);

         if (this.parametros.getParametrosBusquedaPersonal().getEmpleado().getPersona().getNumerodocumento() != null) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("DATOSPERSONALES", "NUMERODOCUMENTO", this.parametros.getParametrosBusquedaPersonal().getEmpleado().getPersona().getNumerodocumento().toString());
            listaParametrosQueryModulos.add(parametro);
         }
         if (this.parametros.getParametrosBusquedaPersonal().getEmpleado().getPersona().getCiudaddocumento().getSecuencia() != null) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("DATOSPERSONALES", "CIUDADDOCUMENTO", this.parametros.getParametrosBusquedaPersonal().getEmpleado().getPersona().getCiudaddocumento().getSecuencia().toString());
            listaParametrosQueryModulos.add(parametro);
         }
         if (this.parametros.getParametrosBusquedaPersonal().getEmpleado().getPersona().getSexo() != null && !this.parametros.getParametrosBusquedaPersonal().getEmpleado().getPersona().getSexo().isEmpty()) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("DATOSPERSONALES", "SEXO", this.parametros.getParametrosBusquedaPersonal().getEmpleado().getPersona().getSexo().toString());
            listaParametrosQueryModulos.add(parametro);
            System.out.println("sexo : " + this.parametros.getParametrosBusquedaPersonal().getEmpleado().getPersona().getSexo());
         }
         if (this.parametros.getParametrosBusquedaPersonal().getEmpleado().getPersona().getCiudadnacimiento().getSecuencia() != null) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("DATOSPERSONALES", "CIUDADNACIMIENTO", this.parametros.getParametrosBusquedaPersonal().getEmpleado().getPersona().getCiudadnacimiento().getSecuencia().toString());
            listaParametrosQueryModulos.add(parametro);
         }
         if (this.parametros.getParametrosBusquedaPersonal().getFechaInicialDatosPersonales() != null) {
            DateFormat df = DateFormat.getDateInstance();
            ParametrosQueryBusquedaAvanzada parametro2 = new ParametrosQueryBusquedaAvanzada("DATOSPERSONALES", "FECHANACIMIENTODESDE", df.format(this.parametros.getParametrosBusquedaPersonal().getFechaInicialDatosPersonales()).toString());
            listaParametrosQueryModulos.add(parametro2);
         }
         if (this.parametros.getParametrosBusquedaPersonal().getFechaFinalDatosPersonales() != null) {
            DateFormat df = DateFormat.getDateInstance();
            ParametrosQueryBusquedaAvanzada parametro3 = new ParametrosQueryBusquedaAvanzada("DATOSPERSONALES", "FECHANACIMIENTOHASTA", df.format(this.parametros.getParametrosBusquedaPersonal().getFechaFinalDatosPersonales()).toString());
            listaParametrosQueryModulos.add(parametro3);
         }
      }
   }

   public void cargueParametrosModuloFactorRH() {
      FacesContext c = FacesContext.getCurrentInstance();
      TabView t = (TabView) c.getViewRoot().findComponent("form:opcionesFactorRH");
      System.out.println("t : " + t);
      System.out.println("t.getActiveIndex() : " + t.getActiveIndex());
      if (t.getActiveIndex() == 1) {
         if (this.parametros.getParametrosBusquedaPersonal().getEmpleado().getPersona().getFactorrh() != null && !this.parametros.getParametrosBusquedaPersonal().getEmpleado().getPersona().getFactorrh().isEmpty()) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("FACTORRH", "FACTORRH", this.parametros.getParametrosBusquedaPersonal().getEmpleado().getPersona().getFactorrh().toString());
            listaParametrosQueryModulos.add(parametro);
         }
         if (this.parametros.getParametrosBusquedaPersonal().getEmpleado().getPersona().getGruposanguineo() != null && !this.parametros.getParametrosBusquedaPersonal().getEmpleado().getPersona().getGruposanguineo().isEmpty()) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("FACTORRH", "GRUPOSANGUINEO", this.parametros.getParametrosBusquedaPersonal().getEmpleado().getPersona().getGruposanguineo().toString());
            listaParametrosQueryModulos.add(parametro);
         }
      }
   }

   public void cargueParametrosModuloEstadoCivil() {
      FacesContext c = FacesContext.getCurrentInstance();
      TabView t = (TabView) c.getViewRoot().findComponent("form:opcionesEstadoCivil");
      System.out.println("t : " + t);
      System.out.println("t.getActiveIndex() : " + t.getActiveIndex());
      if (t.getActiveIndex() == 1) {
         if (vTipoFechaEstadoCivil.equals("false")) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("ESTADOCIVIL", "BESTADOCIVIL", "A");
            listaParametrosQueryModulos.add(parametro);
         }
         if (vTipoFechaEstadoCivil.equals("true")) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("ESTADOCIVIL", "BESTADOCIVIL", "H");

            listaParametrosQueryModulos.add(parametro);
            DateFormat df = DateFormat.getDateInstance();
            ParametrosQueryBusquedaAvanzada parametro2 = null;
            ParametrosQueryBusquedaAvanzada parametro3 = null;
            if (this.parametros.getParametrosBusquedaPersonal().getFechaInicialEstadoCivil() != null) {
               parametro2 = new ParametrosQueryBusquedaAvanzada("ESTADOCIVIL", "ESTADOCIVILDESDE", df.format(this.parametros.getParametrosBusquedaPersonal().getFechaInicialEstadoCivil()).toString());
               listaParametrosQueryModulos.add(parametro2);
            }
            if (this.parametros.getParametrosBusquedaPersonal().getFechaFinalEstadoCivil() != null) {
               parametro3 = new ParametrosQueryBusquedaAvanzada("ESTADOCIVIL", "ESTADOCIVILHASTA", df.format(this.parametros.getParametrosBusquedaPersonal().getFechaFinalEstadoCivil()).toString());
               listaParametrosQueryModulos.add(parametro3);
            }
         }
         if (this.parametros.getParametrosBusquedaPersonal().getEstadoCivil().getSecuencia() != null) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("ESTADOCIVIL", "ESTADOCIVIL", this.parametros.getParametrosBusquedaPersonal().getEstadoCivil().getSecuencia().toString());
            listaParametrosQueryModulos.add(parametro);
         }
      }
   }

   public void cargueParametrosModuloIdioma() {
      FacesContext c = FacesContext.getCurrentInstance();
      TabView t = (TabView) c.getViewRoot().findComponent("form:opcionesIdioma");
      System.out.println("t : " + t);
      System.out.println("t.getActiveIndex() : " + t.getActiveIndex());
      if (t.getActiveIndex() == 1) {

         ParametrosQueryBusquedaAvanzada parametroInicial = new ParametrosQueryBusquedaAvanzada("IDIOMA", "NN", "NN");
         listaParametrosQueryModulos.add(parametroInicial);

         if (this.parametros.getParametrosBusquedaPersonal().getIdiomaPersona().getIdioma().getSecuencia() != null) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("IDIOMA", "IDIOMA", this.parametros.getParametrosBusquedaPersonal().getIdiomaPersona().getIdioma().getSecuencia().toString());
            listaParametrosQueryModulos.add(parametro);
         }
         if (this.parametros.getParametrosBusquedaPersonal().getConversacionDesde() != null) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("IDIOMA", "CONVERSACIONDESDE", this.parametros.getParametrosBusquedaPersonal().getConversacionDesde().toString());
            listaParametrosQueryModulos.add(parametro);
         }
         if (this.parametros.getParametrosBusquedaPersonal().getConversacionHasta() != null) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("IDIOMA", "CONVERSACIONHASTA", this.parametros.getParametrosBusquedaPersonal().getConversacionHasta().toString());
            listaParametrosQueryModulos.add(parametro);
         }
         if (this.parametros.getParametrosBusquedaPersonal().getLecturaDesde() != null) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("IDIOMA", "LECTURADESDE", this.parametros.getParametrosBusquedaPersonal().getLecturaDesde().toString());
            listaParametrosQueryModulos.add(parametro);
         }
         if (this.parametros.getParametrosBusquedaPersonal().getLecturaHasta() != null) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("IDIOMA", "LECTURAHASTA", this.parametros.getParametrosBusquedaPersonal().getLecturaHasta().toString());
            listaParametrosQueryModulos.add(parametro);
         }
         if (this.parametros.getParametrosBusquedaPersonal().getEscrituraDesde() != null) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("IDIOMA", "ESCRITURADESDE", this.parametros.getParametrosBusquedaPersonal().getEscrituraDesde().toString());
            listaParametrosQueryModulos.add(parametro);
         }
         if (this.parametros.getParametrosBusquedaPersonal().getEscrituraHasta() != null) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("IDIOMA", "ESCRITURAHASTA", this.parametros.getParametrosBusquedaPersonal().getEscrituraHasta().toString());
            listaParametrosQueryModulos.add(parametro);
         }
      }
   }

   public void cargueParametrosModuloCensos() {
      FacesContext c = FacesContext.getCurrentInstance();
      TabView t = (TabView) c.getViewRoot().findComponent("form:opcionesCenso");
      System.out.println("t : " + t);
      System.out.println("t.getActiveIndex() : " + t.getActiveIndex());
      if (t.getActiveIndex() == 1) {
         if (vTipoFechaCenso.equals("false")) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("CENSOS", "BCENSOS", "A");
            listaParametrosQueryModulos.add(parametro);
         }
         if (vTipoFechaCenso.equals("true")) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("CENSOS", "BCENSOS", "H");
            listaParametrosQueryModulos.add(parametro);
            DateFormat df = DateFormat.getDateInstance();
            ParametrosQueryBusquedaAvanzada parametro2 = null;
            ParametrosQueryBusquedaAvanzada parametro3 = null;
            if (this.parametros.getParametrosBusquedaPersonal().getFechaInicialCenso() != null) {
               parametro2 = new ParametrosQueryBusquedaAvanzada("CENSOS", "CENSOSDESDE", df.format(this.parametros.getParametrosBusquedaPersonal().getFechaInicialCenso()).toString());
               listaParametrosQueryModulos.add(parametro2);
            }
            if (this.parametros.getParametrosBusquedaPersonal().getFechaFinalCenso() != null) {
               parametro3 = new ParametrosQueryBusquedaAvanzada("CENSOS", "CENSOSHASTA", df.format(this.parametros.getParametrosBusquedaPersonal().getFechaFinalCenso()).toString());
               listaParametrosQueryModulos.add(parametro3);
            }
         }
         if (this.parametros.getParametrosBusquedaPersonal().getVigenciaIndicador().getTipoindicador().getSecuencia() != null) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("CENSOS", "TIPOINDICADOR", this.parametros.getParametrosBusquedaPersonal().getVigenciaIndicador().getTipoindicador().getSecuencia().toString());
            listaParametrosQueryModulos.add(parametro);
         }
         if (this.parametros.getParametrosBusquedaPersonal().getVigenciaIndicador().getIndicador().getSecuencia() != null) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("CENSOS", "INDICADOR", this.parametros.getParametrosBusquedaPersonal().getVigenciaIndicador().getIndicador().getSecuencia().toString());
            listaParametrosQueryModulos.add(parametro);
         }
      }
   }

   public void cargueParametrosModuloEducacionFormal() {
      FacesContext c = FacesContext.getCurrentInstance();
      TabView t = (TabView) c.getViewRoot().findComponent("form:opcionesEducacionFormal");
      System.out.println("t : " + t);
      System.out.println("t.getActiveIndex() : " + t.getActiveIndex());
      if (t.getActiveIndex() == 1) {
         if (vTipoFechaEducacionFormal.equals("false")) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("EDUCACIONFORMAL", "BEDUCACIONFORMAL", "A");
            listaParametrosQueryModulos.add(parametro);
         }
         if (vTipoFechaEducacionFormal.equals("true")) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("EDUCACIONFORMAL", "BEDUCACIONFORMAL", "H");
            listaParametrosQueryModulos.add(parametro);
            DateFormat df = DateFormat.getDateInstance();
            ParametrosQueryBusquedaAvanzada parametro2 = null;
            ParametrosQueryBusquedaAvanzada parametro3 = null;
            if (this.parametros.getParametrosBusquedaPersonal().getFechaInicialFormal() != null) {
               parametro2 = new ParametrosQueryBusquedaAvanzada("EDUCACIONFORMAL", "EDUCACIONFORMALDESDE", df.format(this.parametros.getParametrosBusquedaPersonal().getFechaInicialFormal()).toString());
               listaParametrosQueryModulos.add(parametro2);
            }
            if (this.parametros.getParametrosBusquedaPersonal().getFechaFinalFormal() != null) {
               parametro3 = new ParametrosQueryBusquedaAvanzada("EDUCACIONFORMAL", "EDUCACIONFORMALHASTA", df.format(this.parametros.getParametrosBusquedaPersonal().getFechaFinalFormal()).toString());
               listaParametrosQueryModulos.add(parametro3);
            }
         }
         if (this.parametros.getParametrosBusquedaPersonal().getVigenciaFormal().getInstitucion().getSecuencia() != null) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("EDUCACIONFORMAL", "INSTITUCION", this.parametros.getParametrosBusquedaPersonal().getVigenciaFormal().getInstitucion().getSecuencia().toString());
            listaParametrosQueryModulos.add(parametro);
         }
         if (this.parametros.getParametrosBusquedaPersonal().getVigenciaFormal().getProfesion().getSecuencia() != null) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("EDUCACIONFORMAL", "PROFESION", this.parametros.getParametrosBusquedaPersonal().getVigenciaFormal().getProfesion().getSecuencia().toString());
            listaParametrosQueryModulos.add(parametro);
         }
         if (this.parametros.getParametrosBusquedaPersonal().getDesarrolladoEducacionFormal() != null) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("EDUCACIONFORMAL", "REALIZADO", this.parametros.getParametrosBusquedaPersonal().getDesarrolladoEducacionFormal().toString());
            listaParametrosQueryModulos.add(parametro);
         }
      }
   }

   public void cargueParametrosModuloEducacionNoFormal() {
      FacesContext c = FacesContext.getCurrentInstance();
      TabView t = (TabView) c.getViewRoot().findComponent("form:opcionesEducacionNoFormal");
      System.out.println("t : " + t);
      System.out.println("t.getActiveIndex() : " + t.getActiveIndex());
      if (t.getActiveIndex() == 1) {
         if (vTipoFechaEducacionNoFormal.equals("false")) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("EDUCACIONNOFORMAL", "BEDUCACIONNOFORMAL", "A");
            listaParametrosQueryModulos.add(parametro);
         }
         if (vTipoFechaEducacionNoFormal.equals("true")) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("EDUCACIONNOFORMAL", "BEDUCACIONNOFORMAL", "H");
            DateFormat df = DateFormat.getDateInstance();
            ParametrosQueryBusquedaAvanzada parametro2 = null;
            ParametrosQueryBusquedaAvanzada parametro3 = null;
            listaParametrosQueryModulos.add(parametro);
            if (this.parametros.getParametrosBusquedaPersonal().getFechaInicialNoFormal() != null) {
               parametro2 = new ParametrosQueryBusquedaAvanzada("EDUCACIONNOFORMAL", "EDUCACIONNOFORMALDESDE", df.format(this.parametros.getParametrosBusquedaPersonal().getFechaInicialNoFormal()).toString());
               listaParametrosQueryModulos.add(parametro2);
            }
            if (this.parametros.getParametrosBusquedaPersonal().getFechaFinalNoFormal() != null) {
               parametro3 = new ParametrosQueryBusquedaAvanzada("EDUCACIONNOFORMAL", "EDUCACIONNOFORMALHASTA", df.format(this.parametros.getParametrosBusquedaPersonal().getFechaFinalNoFormal()).toString());
               listaParametrosQueryModulos.add(parametro3);
            }
         }
         if (this.parametros.getParametrosBusquedaPersonal().getVigenciaNoFormal().getInstitucion().getSecuencia() != null) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("EDUCACIONNOFORMAL", "INSTITUCION", this.parametros.getParametrosBusquedaPersonal().getVigenciaNoFormal().getInstitucion().getSecuencia().toString());
            listaParametrosQueryModulos.add(parametro);
         }
         if (this.parametros.getParametrosBusquedaPersonal().getVigenciaNoFormal().getCurso().getSecuencia() != null) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("EDUCACIONNOFORMAL", "CURSO", this.parametros.getParametrosBusquedaPersonal().getVigenciaNoFormal().getCurso().getSecuencia().toString());
            listaParametrosQueryModulos.add(parametro);
         }
         if (this.parametros.getParametrosBusquedaPersonal().getDesarrolladoEducacionNoFormal() != null) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("EDUCACIONNOFORMAL", "REALIZADO", this.parametros.getParametrosBusquedaPersonal().getDesarrolladoEducacionNoFormal().toString());
            listaParametrosQueryModulos.add(parametro);
         }
      }
   }

   public void cargueParametrosModuloCargoPostularse() {
      FacesContext c = FacesContext.getCurrentInstance();
      TabView t = (TabView) c.getViewRoot().findComponent("form:opcionesCargoPostularse");
      System.out.println("t : " + t);
      System.out.println("t.getActiveIndex() : " + t.getActiveIndex());
      if (t.getActiveIndex() == 1) {

         ParametrosQueryBusquedaAvanzada parametroInicial = new ParametrosQueryBusquedaAvanzada("CARGOPOSTULARSE", "NN", "NN");
         listaParametrosQueryModulos.add(parametroInicial);

         if (this.parametros.getParametrosBusquedaPersonal().getCargo().getSecuencia() != null) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("CARGOPOSTULARSE", "CARGO", this.parametros.getParametrosBusquedaPersonal().getCargo().getSecuencia().toString());
            listaParametrosQueryModulos.add(parametro);
         }
      }
   }

   public void cargueParametrosModuloProyecto() {
      FacesContext c = FacesContext.getCurrentInstance();
      TabView t = (TabView) c.getViewRoot().findComponent("form:opcionesProyecto");
      System.out.println("t : " + t);
      System.out.println("t.getActiveIndex() : " + t.getActiveIndex());
      if (t.getActiveIndex() == 1) {

         ParametrosQueryBusquedaAvanzada parametroInicial = new ParametrosQueryBusquedaAvanzada("PROYECTO", "NN", "NN");
         listaParametrosQueryModulos.add(parametroInicial);

         if (this.parametros.getParametrosBusquedaPersonal().getVigenciaProyecto().getProyecto().getSecuencia() != null) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("PROYECTO", "PROYECTO", this.parametros.getParametrosBusquedaPersonal().getVigenciaProyecto().getProyecto().getSecuencia().toString());
            listaParametrosQueryModulos.add(parametro);
         }
         if (this.parametros.getParametrosBusquedaPersonal().getVigenciaProyecto().getPryRol().getSecuencia() != null) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("PROYECTO", "ROL", this.parametros.getParametrosBusquedaPersonal().getVigenciaProyecto().getPryRol().getSecuencia().toString());
            listaParametrosQueryModulos.add(parametro);
         }
         if (this.parametros.getParametrosBusquedaPersonal().getFechaInicialProyecto() != null) {
            DateFormat df = DateFormat.getDateInstance();
            ParametrosQueryBusquedaAvanzada parametro2 = new ParametrosQueryBusquedaAvanzada("PROYECTO", "PROYECTODESDE", df.format(this.parametros.getParametrosBusquedaPersonal().getFechaInicialProyecto()).toString());
            listaParametrosQueryModulos.add(parametro2);
         }
         if (this.parametros.getParametrosBusquedaPersonal().getFechaFinalProyecto() != null) {
            DateFormat df = DateFormat.getDateInstance();
            ParametrosQueryBusquedaAvanzada parametro3 = new ParametrosQueryBusquedaAvanzada("PROYECTO", "PROYECTOHASTA", df.format(this.parametros.getParametrosBusquedaPersonal().getFechaFinalProyecto()).toString());
            listaParametrosQueryModulos.add(parametro3);
         }
      }
   }

   public void cargueParametrosModuloExperienciaLaboral() {
      FacesContext c = FacesContext.getCurrentInstance();
      TabView t = (TabView) c.getViewRoot().findComponent("form:opcionesExperienciaLaboral");
      System.out.println("t : " + t);
      System.out.println("t.getActiveIndex() : " + t.getActiveIndex());
      if (t.getActiveIndex() == 1) {

         ParametrosQueryBusquedaAvanzada parametroInicial = new ParametrosQueryBusquedaAvanzada("EXPERIENCIALABORAL", "NN", "NN");
         listaParametrosQueryModulos.add(parametroInicial);

         if (!this.parametros.getParametrosBusquedaPersonal().getCargoExperienciaLaboral().isEmpty()) {
            ParametrosQueryBusquedaAvanzada parametro2 = new ParametrosQueryBusquedaAvanzada("EXPERIENCIALABORAL", "CARGO", this.parametros.getParametrosBusquedaPersonal().getCargoExperienciaLaboral().toString());
            listaParametrosQueryModulos.add(parametro2);
         }
         if (!this.parametros.getParametrosBusquedaPersonal().getEmpresaExperienciaLaboral().isEmpty()) {
            ParametrosQueryBusquedaAvanzada parametro3 = new ParametrosQueryBusquedaAvanzada("EXPERIENCIALABORAL", "EMPRESA", this.parametros.getParametrosBusquedaPersonal().getEmpresaExperienciaLaboral().toString());
            listaParametrosQueryModulos.add(parametro3);
         }
         if (this.parametros.getParametrosBusquedaPersonal().getHvExperienciaLaboral().getSectoreconomico().getSecuencia() != null) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("EXPERIENCIALABORAL", "SECTORECONOMICO", this.parametros.getParametrosBusquedaPersonal().getHvExperienciaLaboral().getSectoreconomico().getSecuencia().toString());
            listaParametrosQueryModulos.add(parametro);
         }
         if (this.parametros.getParametrosBusquedaPersonal().getHvExperienciaLaboral().getMotivoretiro().getSecuencia() != null) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("EXPERIENCIALABORAL", "MOTIVORETIRO", this.parametros.getParametrosBusquedaPersonal().getHvExperienciaLaboral().getMotivoretiro().getSecuencia().toString());
            listaParametrosQueryModulos.add(parametro);
         }
         if (this.parametros.getParametrosBusquedaPersonal().getFechaInicialExperienciaLaboral() != null) {
            DateFormat df = DateFormat.getDateInstance();
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("EXPERIENCIALABORAL", "EXPERIENCIALABORALDESDE", df.format(this.parametros.getParametrosBusquedaPersonal().getFechaInicialExperienciaLaboral()).toString());
            listaParametrosQueryModulos.add(parametro);
         }
         if (this.parametros.getParametrosBusquedaPersonal().getFechaFinalExperienciaLaboral() != null) {
            DateFormat df = DateFormat.getDateInstance();
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("EXPERIENCIALABORAL", "EXPERIENCIALABORALHASTA", df.format(this.parametros.getParametrosBusquedaPersonal().getFechaFinalExperienciaLaboral()).toString());
            listaParametrosQueryModulos.add(parametro);
         }
      }
   }

   //CONVERTIR LISTA A STRING
   public String convertirListaAString() {
      String columnas = "CODIGOEMPLEADO, NOMBRE, PRIMERAPELLIDO, SEGUNDOAPELLIDO";
      if (columnasEsSeleccionadas != null && !columnasEsSeleccionadas.isEmpty()) {
         for (ColumnasEscenarios columna : columnasEsSeleccionadas) {
            columnas += "," + columna.getDescripcion();
         }
      }
      return columnas;
   }

   public ResultadoBusquedaAvanzada primerResultado() {
      System.out.println("Controlador.ControlBusquedaAvanzada.primerResultado()");
      ResultadoBusquedaAvanzada resultado = new ResultadoBusquedaAvanzada();
      if (listaResultadoBusqueda != null) {
         if (!listaResultadoBusqueda.isEmpty()) {
            resultado = listaResultadoBusqueda.get(0);
            if (listaResultadoBusqueda.size() > 1) {
               ResultadoBusquedaAvanzada resultado2 = listaResultadoBusqueda.get(1);
               try {
                  if (resultado2.getColumna0().length() > resultado.getColumna0().length()) {
                     resultado.setColumna0(resultado2.getColumna0());
                  }
                  if (resultado2.getColumna1().length() > resultado.getColumna1().length()) {
                     resultado.setColumna1(resultado2.getColumna1());
                  }
                  if (resultado2.getColumna2().length() > resultado.getColumna2().length()) {
                     resultado.setColumna2(resultado2.getColumna2());
                  }
                  if (resultado2.getColumna3().length() > resultado.getColumna3().length()) {
                     resultado.setColumna3(resultado2.getColumna3());
                  }
                  if (resultado2.getColumna4().length() > resultado.getColumna4().length()) {
                     resultado.setColumna4(resultado2.getColumna4());
                  }
                  if (resultado2.getColumna5().length() > resultado.getColumna5().length()) {
                     resultado.setColumna5(resultado2.getColumna5());
                  }
                  if (resultado2.getColumna6().length() > resultado.getColumna6().length()) {
                     resultado.setColumna6(resultado2.getColumna6());
                  }
                  if (resultado2.getColumna7().length() > resultado.getColumna7().length()) {
                     resultado.setColumna7(resultado2.getColumna7());
                  }
                  if (resultado2.getColumna8().length() > resultado.getColumna8().length()) {
                     resultado.setColumna8(resultado2.getColumna8());
                  }
                  if (resultado2.getColumna9().length() > resultado.getColumna9().length()) {
                     resultado.setColumna9(resultado2.getColumna9());
                  }
               } catch (Exception e) {
                  System.out.println("Entro al catch1() e : " + e);
               }
            }
            if (listaResultadoBusqueda.size() > 1) {
               ResultadoBusquedaAvanzada resultado2 = listaResultadoBusqueda.get(2);
               try {
                  if (resultado2.getColumna0().length() > resultado.getColumna0().length()) {
                     resultado.setColumna0(resultado2.getColumna0());
                  }
                  if (resultado2.getColumna1().length() > resultado.getColumna1().length()) {
                     resultado.setColumna1(resultado2.getColumna1());
                  }
                  if (resultado2.getColumna2().length() > resultado.getColumna2().length()) {
                     resultado.setColumna2(resultado2.getColumna2());
                  }
                  if (resultado2.getColumna3().length() > resultado.getColumna3().length()) {
                     resultado.setColumna3(resultado2.getColumna3());
                  }
                  if (resultado2.getColumna4().length() > resultado.getColumna4().length()) {
                     resultado.setColumna4(resultado2.getColumna4());
                  }
                  if (resultado2.getColumna5().length() > resultado.getColumna5().length()) {
                     resultado.setColumna5(resultado2.getColumna5());
                  }
                  if (resultado2.getColumna6().length() > resultado.getColumna6().length()) {
                     resultado.setColumna6(resultado2.getColumna6());
                  }
                  if (resultado2.getColumna7().length() > resultado.getColumna7().length()) {
                     resultado.setColumna7(resultado2.getColumna7());
                  }
                  if (resultado2.getColumna8().length() > resultado.getColumna8().length()) {
                     resultado.setColumna8(resultado2.getColumna8());
                  }
                  if (resultado2.getColumna9().length() > resultado.getColumna9().length()) {
                     resultado.setColumna9(resultado2.getColumna9());
                  }
               } catch (Exception e) {
                  System.out.println("Entro al catch1() e : " + e);
               }
            }

            int totalAncho = 800;

            String col = convertirListaAString();
            String[] columnKeys = col.split(",");
            try {
               if (columnKeys[4].length() >= resultado.getColumna0().length()) {
                  totalAncho = totalAncho + calcularAncho(columnKeys[4].length());
               } else {
                  totalAncho = totalAncho + calcularAncho(resultado.getColumna0().length());
               }

               if (columnKeys[5].length() >= resultado.getColumna1().length()) {
                  totalAncho = totalAncho + calcularAncho(columnKeys[5].length());
               } else {
                  totalAncho = totalAncho + calcularAncho(resultado.getColumna1().length());
               }

               if (columnKeys[6].length() >= resultado.getColumna2().length()) {
                  totalAncho = totalAncho + calcularAncho(columnKeys[6].length());
               } else {
                  totalAncho = totalAncho + calcularAncho(resultado.getColumna2().length());
               }

               if (columnKeys[7].length() >= resultado.getColumna3().length()) {
                  totalAncho = totalAncho + calcularAncho(columnKeys[7].length());
               } else {
                  totalAncho = totalAncho + calcularAncho(resultado.getColumna3().length());
               }

               if (columnKeys[8].length() >= resultado.getColumna4().length()) {
                  totalAncho = totalAncho + calcularAncho(columnKeys[8].length());
               } else {
                  totalAncho = totalAncho + calcularAncho(resultado.getColumna4().length());
               }

               if (columnKeys[9].length() >= resultado.getColumna5().length()) {
                  totalAncho = totalAncho + calcularAncho(columnKeys[9].length());
               } else {
                  totalAncho = totalAncho + calcularAncho(resultado.getColumna5().length());
               }

               if (columnKeys[10].length() >= resultado.getColumna6().length()) {
                  totalAncho = totalAncho + calcularAncho(columnKeys[10].length());
               } else {
                  totalAncho = totalAncho + calcularAncho(resultado.getColumna6().length());
               }

               if (columnKeys[11].length() >= resultado.getColumna7().length()) {
                  calcularAncho(columnKeys[11].length());
               } else {
                  totalAncho = totalAncho + calcularAncho(resultado.getColumna7().length());
               }

               if (columnKeys[12].length() >= resultado.getColumna8().length()) {
                  totalAncho = totalAncho + calcularAncho(columnKeys[12].length());
               } else {
                  totalAncho = totalAncho + calcularAncho(resultado.getColumna8().length());
               }

               if (columnKeys[13].length() >= resultado.getColumna0().length()) {
                  totalAncho = totalAncho + calcularAncho(columnKeys[9].length());
               } else {
                  totalAncho = totalAncho + calcularAncho(resultado.getColumna9().length());
               }
            } catch (Exception e) {
               System.out.println("Entro al catch2() e : " + e);
            }
            System.out.println("totalAncho : " + totalAncho);
            anchoTablaResultados = totalAncho + "px";
         }
      }
      return resultado;
   }

   public int calcularAncho(int tamanoString) {
      if (tamanoString > 0) {
         return (tamanoString * 9);
      } else {
         return 100;
      }
   }

   public void validarAnadirAParametros() {
      List<Parametros> listEmpleadosParametros = administrarBusquedaAvanzada.empleadosParametros();
      System.out.println("validarAnadirAParametros() ya consulto");
      if (listEmpleadosParametros != null) {
         System.out.println("listEmpleadosParametros.size() : " + listEmpleadosParametros.size());
         if (!listEmpleadosParametros.isEmpty()) {
            System.out.println("1");
            RequestContext.getCurrentInstance().execute("PF('validarParametrosExistentes').show()");
         } else {
            System.out.println("2");
            RequestContext.getCurrentInstance().execute("PF('validarCrearParametros').show()");
         }
      } else {
         System.out.println("2");
         RequestContext.getCurrentInstance().execute("PF('validarCrearParametros').show()");
      }
   }

   public void limpiarParametros(ParametrosEstructuras parametroLiquidacion) {
      administrarBusquedaAvanzada.borrarParametros(parametroLiquidacion.getSecuencia());
   }

   public ParametrosEstructuras consultarParametroLiquidacion() {
      ParametrosEstructuras parametroLiquidacion = administrarBusquedaAvanzada.parametrosLiquidacion();
      if (parametroLiquidacion == null) {
         Usuarios au = administrarBusquedaAvanzada.usuarioActual();
         parametroLiquidacion = new ParametrosEstructuras();
         parametroLiquidacion.setUsuario(au);
         parametroLiquidacion.setProceso(new Procesos());
         parametroLiquidacion.setSecuencia(BigInteger.valueOf(0));
      }
      return parametroLiquidacion;
   }

   public void borrarYCrearNuavosParametros() {
      if (listaResultadoBusqueda != null) {
         if (!listaResultadoBusqueda.isEmpty()) {
            ParametrosEstructuras parametroLiquidacion = consultarParametroLiquidacion();
            limpiarParametros(parametroLiquidacion);
            parametroLiquidacion = consultarParametroLiquidacion();
            administrarBusquedaAvanzada.crearParametroEstructura(parametroLiquidacion);
            parametroLiquidacion = consultarParametroLiquidacion();
            List<Empleados> listaEmpleados = administrarBusquedaAvanzada.consultarEmpleadosXCodigo(listaCodigosEmpleado);
            añadirEmpleadosParametros(parametroLiquidacion, listaEmpleados);
         } else {
            RequestContext.getCurrentInstance().execute("PF('ErrorSinEmpleadosAParametros').show()");
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('ErrorSinEmpleadosAParametros').show()");
      }
   }

   public void crearNuavosParametros() {
      if (listaResultadoBusqueda != null) {
         if (!listaResultadoBusqueda.isEmpty()) {
            ParametrosEstructuras parametroLiquidacion = consultarParametroLiquidacion();
            administrarBusquedaAvanzada.crearParametroEstructura(parametroLiquidacion);
            parametroLiquidacion = consultarParametroLiquidacion();
            List<Empleados> listaEmpleados = administrarBusquedaAvanzada.consultarEmpleadosXCodigo(listaCodigosEmpleado);
            añadirEmpleadosParametros(parametroLiquidacion, listaEmpleados);
         } else {
            RequestContext.getCurrentInstance().execute("PF('ErrorSinEmpleadosAParametros').show()");
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('ErrorSinEmpleadosAParametros').show()");
      }
   }

   public void añadirMasParametros() {
      if (listaResultadoBusqueda != null) {
         if (!listaResultadoBusqueda.isEmpty()) {
            ParametrosEstructuras parametroLiquidacion = consultarParametroLiquidacion();
            administrarBusquedaAvanzada.crearParametroEstructura(parametroLiquidacion);
            parametroLiquidacion = consultarParametroLiquidacion();
            List<Parametros> listEmpleadosParametros = administrarBusquedaAvanzada.empleadosParametros();
            List<Empleados> listaEmpleados = administrarBusquedaAvanzada.consultarEmpleadosXCodigo(listaCodigosEmpleado);
            for (int i = 0; i < listEmpleadosParametros.size(); i++) {
               for (int j = 0; j < listaEmpleados.size(); j++) {
                  if (listEmpleadosParametros.get(i).getEmpleado().getSecuencia().equals(listaEmpleados.get(j).getSecuencia())) {
                     listaEmpleados.remove(listaEmpleados.get(j));
                     break;
                  }
               }
            }
            añadirEmpleadosParametros(parametroLiquidacion, listaEmpleados);
         } else {
            RequestContext.getCurrentInstance().execute("PF('ErrorSinEmpleadosAParametros').show()");
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('ErrorSinEmpleadosAParametros').show()");
      }
   }

   public void añadirEmpleadosParametros(ParametrosEstructuras parametroLiquidacion, List<Empleados> listaEmpleados) {
      int k = 0;
      BigInteger l;
      List<Parametros> listaCrearParametros = new ArrayList<Parametros>();
      System.out.println("Controlador.ControlBusquedaAvanzada.añadirEmpleadosParametros() Va a entrar al primer try{}");
      try {
         for (int i = 0; i < listaEmpleados.size(); i++) {
            k++;
            l = BigInteger.valueOf(k);
            Parametros parametro = new Parametros();
            parametro.setSecuencia(l);
            parametro.setParametroestructura(parametroLiquidacion);
            parametro.setEmpleado(listaEmpleados.get(i));
            listaCrearParametros.add(parametro);
         }
      } catch (Exception e) {
         System.out.println("ERROR añadirEmpleadosParametros() llenando listaCrearParametros e: " + e);
         RequestContext.getCurrentInstance().execute("PF('errorCreandoParametros').show()");
      }
      if (!listaCrearParametros.isEmpty()) {
         try {
            Usuarios au = administrarBusquedaAvanzada.usuarioActual();
            Date fechaDesde = parametroLiquidacion.getFechadesdecausado();
            Date fechaHasta = parametroLiquidacion.getFechahastacausado();
            Date fechaSistema = parametroLiquidacion.getFechasistema();
            Procesos proceso = parametroLiquidacion.getProceso();
            for (int i = 0; i < listaCrearParametros.size(); i++) {
               listaCrearParametros.get(i).setFechadesdecausado(fechaDesde);
               listaCrearParametros.get(i).setFechahastacausado(fechaHasta);
               listaCrearParametros.get(i).setFechasistema(fechaSistema);
               listaCrearParametros.get(i).setProceso(proceso);
               listaCrearParametros.get(i).setUsuario(au);
               if (listaCrearParametros.get(i).getParametroestructura().getTipotrabajador().getSecuencia() == null) {
                  listaCrearParametros.get(i).getParametroestructura().setTipotrabajador(null);
               }
            }
         } catch (Exception e) {
            System.out.println("ERROR añadirEmpleadosParametros() añadiemdo datos a listaCrearParametros e: " + e);
            RequestContext.getCurrentInstance().execute("PF('errorCreandoParametros').show()");
         }
         administrarBusquedaAvanzada.crearParametros(listaCrearParametros);
         RequestContext.getCurrentInstance().execute("PF('parametrosModificados').show()");
      }
   }

   public void exportPDF() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosBusquedaAvanzadaExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "ResultadosBusquedaAvanzada_PDF", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void eventoFiltrar_ColumnasEscenarios() {
//        modificarInfoR(filtradoColumnasEscenarios.size());
      //RequestContext.getCurrentInstance().update("formLovs:formDInformacionPersonal:infoRegistroTipoDocumentoInformacionPersonal";
   }

   /**
    * Metodo que exporta datos a XLS
    *
    * @throws IOException Excepcion de In-Out de datos
    */
   public void exportXLS() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosBusquedaAvanzadaExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "ResultadosBusquedaAvanzada_XLS", false, false, "UTF-8", null, null);
      context.responseComplete();

   }
   //GETTER AND SETTERS UNICAMENTE

   public ParametrosBusquedaAvanzada getParametros() {
      if (parametros != null) {

      }
      return parametros;
   }

   public void setParametros(ParametrosBusquedaAvanzada parametrosBusquedaAvanzada) {
      this.parametros = parametrosBusquedaAvanzada;
   }

   public String getVTipoBusqueda() {
      return vTipoBusqueda;
   }

   public void setVTipoBusqueda(String vTipoBusqueda) {
      this.vTipoBusqueda = vTipoBusqueda;
   }

   public String getVTipoFechaCargo() {
      return vTipoFechaCargo;
   }

   public void setVTipoFechaCargo(String vTipoFechaCargo) {
      this.vTipoFechaCargo = vTipoFechaCargo;
   }

   public String getVTipoFechaCentroCosto() {
      return vTipoFechaCentroCosto;
   }

   public void setVTipoFechaCentroCosto(String vTipoFechaCentroCosto) {
      this.vTipoFechaCentroCosto = vTipoFechaCentroCosto;
   }

   public String getVTipoFechaSueldo() {
      return vTipoFechaSueldo;
   }

   public void setVTipoFechaSueldo(String vTipoFechaSueldo) {
      this.vTipoFechaSueldo = vTipoFechaSueldo;
   }

   public String getVTipoFechaTipoContrato() {
      return vTipoFechaTipoContrato;
   }

   public void setVTipoFechaTipoContrato(String vTipoFechaTipoContrato) {
      this.vTipoFechaTipoContrato = vTipoFechaTipoContrato;
   }

   public String getValorPorDefecto() {
      return valorPorDefecto;
   }

   public void setValorPorDefecto(String valorPorDefecto) {
      this.valorPorDefecto = valorPorDefecto;
   }

   public String getValorCopia() {
      if (valorCopia == null) {
         valorCopia = "";
      }
      return valorCopia;
   }

   public void setValorCopia(String valorCopia) {
      this.valorCopia = valorCopia;
   }

   public List<Cargos> getLovCargos() {
      return lovCargos;
   }

   public List<Cargos> getFiltroLovCargos() {
      return filtroLovCargos;
   }

   public void setFiltroLovCargos(List<Cargos> filtroLovCargos) {
      this.filtroLovCargos = filtroLovCargos;
   }

   public Cargos getCargoSeleccionado() {
      return cargoSeleccionado;
   }

   public void setCargoSeleccionado(Cargos cargoSeleccionado) {
      this.cargoSeleccionado = cargoSeleccionado;
   }

   public List<Estructuras> getFiltroLovEstructuras() {
      return filtroLovEstructuras;
   }

   public void setFiltroLovEstructuras(List<Estructuras> filtroLovEstructuras) {
      this.filtroLovEstructuras = filtroLovEstructuras;
   }

   public List<Estructuras> getLovEstructuras() {
      return lovEstructuras;
   }

   public void setLovEstructuras(List<Estructuras> lovEstructuras) {
      this.lovEstructuras = lovEstructuras;
   }

   public Estructuras getEstructuraSeleccionada() {
      return estructuraSeleccionada;
   }

   public void setEstructuraSeleccionada(Estructuras estructuraSeleccionada) {
      this.estructuraSeleccionada = estructuraSeleccionada;
   }

   public List<Empleados> getFiltroLovJefe() {
      return filtroLovJefe;
   }

   public void setFiltroLovJefe(List<Empleados> filtroLovJefe) {
      this.filtroLovJefe = filtroLovJefe;
   }

   public List<Empleados> getLovJefe() {
      return lovJefe;
   }

   public void setLovJefe(List<Empleados> lovJefe) {
      this.lovJefe = lovJefe;
   }

   public Empleados getJefeSeleccionado() {
      return jefeSeleccionado;
   }

   public void setJefeSeleccionado(Empleados jefeSeleccionado) {
      this.jefeSeleccionado = jefeSeleccionado;
   }

   public List<Papeles> getFiltroLovPapeles() {
      return filtroLovPapeles;
   }

   public void setFiltroLovPapeles(List<Papeles> filtroLovPapeles) {
      this.filtroLovPapeles = filtroLovPapeles;
   }

   public List<Papeles> getLovPapeles() {
      return lovPapeles;
   }

   public void setLovPapeles(List<Papeles> lovPapeles) {
      this.lovPapeles = lovPapeles;
   }

   public Papeles getPapelSeleccionado() {
      return papelSeleccionado;
   }

   public void setPapelSeleccionado(Papeles papelSeleccionado) {
      this.papelSeleccionado = papelSeleccionado;
   }

   public List<Contratos> getFiltroLovContratos() {
      return filtroLovContratos;
   }

   public void setFiltroLovContratos(List<Contratos> filtroLovContratos) {
      this.filtroLovContratos = filtroLovContratos;
   }

   public List<EstadosAfiliaciones> getFiltroLovEstadosAfiliaciones() {
      return filtroLovEstadosAfiliaciones;
   }

   public void setFiltroLovEstadosAfiliaciones(List<EstadosAfiliaciones> filtroLovEstadosAfiliaciones) {
      this.filtroLovEstadosAfiliaciones = filtroLovEstadosAfiliaciones;
   }

   public List<Periodicidades> getFiltroLovFormasPagos() {
      return filtroLovFormasPagos;
   }

   public void setFiltroLovFormasPagos(List<Periodicidades> filtroLovFormasPagos) {
      this.filtroLovFormasPagos = filtroLovFormasPagos;
   }

   public List<MotivosCambiosCargos> getFiltroLovMotivosCargos() {
      return filtroLovMotivosCargos;
   }

   public void setFiltroLovMotivosCargos(List<MotivosCambiosCargos> filtroLovMotivosCargos) {
      this.filtroLovMotivosCargos = filtroLovMotivosCargos;
   }

   public List<MotivosContratos> getFiltroLovMotivosContratos() {
      return filtroLovMotivosContratos;
   }

   public void setFiltroLovMotivosContratos(List<MotivosContratos> filtroLovMotivosContratos) {
      this.filtroLovMotivosContratos = filtroLovMotivosContratos;
   }

   public List<MotivosLocalizaciones> getFiltroLovMotivosLocalizaciones() {
      return filtroLovMotivosLocalizaciones;
   }

   public void setFiltroLovMotivosLocalizaciones(List<MotivosLocalizaciones> filtroLovMotivosLocalizaciones) {
      this.filtroLovMotivosLocalizaciones = filtroLovMotivosLocalizaciones;
   }

   public List<MotivosCambiosSueldos> getFiltroLovMotivosSueldos() {
      return filtroLovMotivosSueldos;
   }

   public void setFiltroLovMotivosSueldos(List<MotivosCambiosSueldos> filtroLovMotivosSueldos) {
      this.filtroLovMotivosSueldos = filtroLovMotivosSueldos;
   }

   public List<ReformasLaborales> getFiltroLovReformasLaborales() {
      return filtroLovReformasLaborales;
   }

   public void setFiltroLovReformasLaborales(List<ReformasLaborales> filtroLovReformasLaborales) {
      this.filtroLovReformasLaborales = filtroLovReformasLaborales;
   }

   public List<Sucursales> getFiltroLovSucursales() {
      return filtroLovSucursales;
   }

   public void setFiltroLovSucursales(List<Sucursales> filtroLovSucursales) {
      this.filtroLovSucursales = filtroLovSucursales;
   }

   public List<TercerosSucursales> getFiltroLovTercerosSucursales() {
      return filtroLovTercerosSucursales;
   }

   public void setFiltroLovTercerosSucursales(List<TercerosSucursales> filtroLovTercerosSucursales) {
      this.filtroLovTercerosSucursales = filtroLovTercerosSucursales;
   }

   public List<TiposContratos> getFiltroLovTiposContratos() {
      return filtroLovTiposContratos;
   }

   public void setFiltroLovTiposContratos(List<TiposContratos> filtroLovTiposContratos) {
      this.filtroLovTiposContratos = filtroLovTiposContratos;
   }

   public List<TiposEntidades> getFiltroLovTiposEntidades() {
      return filtroLovTiposEntidades;
   }

   public void setFiltroLovTiposEntidades(List<TiposEntidades> filtroLovTiposEntidades) {
      this.filtroLovTiposEntidades = filtroLovTiposEntidades;
   }

   public List<TiposSueldos> getFiltroLovTiposSueldos() {
      return filtroLovTiposSueldos;
   }

   public void setFiltroLovTiposSueldos(List<TiposSueldos> filtroLovTiposSueldos) {
      this.filtroLovTiposSueldos = filtroLovTiposSueldos;
   }

   public List<TiposTrabajadores> getFiltroLovTiposTrabajadores() {
      return filtroLovTiposTrabajadores;
   }

   public void setFiltroLovTiposTrabajadores(List<TiposTrabajadores> filtroLovTiposTrabajadores) {
      this.filtroLovTiposTrabajadores = filtroLovTiposTrabajadores;
   }

   public List<UbicacionesGeograficas> getFiltroLovUbicaciones() {
      return filtroLovUbicaciones;
   }

   public void setFiltroLovUbicaciones(List<UbicacionesGeograficas> filtroLovUbicaciones) {
      this.filtroLovUbicaciones = filtroLovUbicaciones;
   }

   public List<Contratos> getLovContratos() {
      return lovContratos;
   }

   public void setLovContratos(List<Contratos> lovContratos) {
      this.lovContratos = lovContratos;
   }

   public List<EstadosAfiliaciones> getLovEstadosAfiliaciones() {
      return lovEstadosAfiliaciones;
   }

   public void setLovEstadosAfiliaciones(List<EstadosAfiliaciones> lovEstadosAfiliaciones) {
      this.lovEstadosAfiliaciones = lovEstadosAfiliaciones;
   }

   public List<Periodicidades> getLovFormasPagos() {
      return lovFormasPagos;
   }

   public void setLovFormasPagos(List<Periodicidades> lovFormasPagos) {
      this.lovFormasPagos = lovFormasPagos;
   }

   public List<MotivosCambiosCargos> getLovMotivosCargos() {
      return lovMotivosCargos;
   }

   public void setLovMotivosCargos(List<MotivosCambiosCargos> lovMotivosCargos) {
      this.lovMotivosCargos = lovMotivosCargos;
   }

   public List<MotivosContratos> getLovMotivosContratos() {
      return lovMotivosContratos;
   }

   public void setLovMotivosContratos(List<MotivosContratos> lovMotivosContratos) {
      this.lovMotivosContratos = lovMotivosContratos;
   }

   public List<MotivosLocalizaciones> getLovMotivosLocalizaciones() {
      return lovMotivosLocalizaciones;
   }

   public void setLovMotivosLocalizaciones(List<MotivosLocalizaciones> lovMotivosLocalizaciones) {
      this.lovMotivosLocalizaciones = lovMotivosLocalizaciones;
   }

   public List<MotivosCambiosSueldos> getLovMotivosSueldos() {
      return lovMotivosSueldos;
   }

   public void setLovMotivosSueldos(List<MotivosCambiosSueldos> lovMotivosSueldos) {
      this.lovMotivosSueldos = lovMotivosSueldos;
   }

   public List<ReformasLaborales> getLovReformasLaborales() {
      return lovReformasLaborales;
   }

   public void setLovReformasLaborales(List<ReformasLaborales> lovReformasLaborales) {
      this.lovReformasLaborales = lovReformasLaborales;
   }

   public List<Sucursales> getLovSucursales() {
      return lovSucursales;
   }

   public void setLovSucursales(List<Sucursales> lovSucursales) {
      this.lovSucursales = lovSucursales;
   }

   public List<TercerosSucursales> getLovTercerosSucursales() {
      return lovTercerosSucursales;
   }

   public void setLovTercerosSucursales(List<TercerosSucursales> lovTercerosSucursales) {
      this.lovTercerosSucursales = lovTercerosSucursales;
   }

   public List<TiposContratos> getLovTiposContratos() {
      return lovTiposContratos;
   }

   public void setLovTiposContratos(List<TiposContratos> lovTiposContratos) {
      this.lovTiposContratos = lovTiposContratos;
   }

   public List<TiposEntidades> getLovTiposEntidades() {
      return lovTiposEntidades;
   }

   public void setLovTiposEntidades(List<TiposEntidades> lovTiposEntidades) {
      this.lovTiposEntidades = lovTiposEntidades;
   }

   public List<TiposSueldos> getLovTiposSueldos() {
      return lovTiposSueldos;
   }

   public void setLovTiposSueldos(List<TiposSueldos> lovTiposSueldos) {
      this.lovTiposSueldos = lovTiposSueldos;
   }

   public List<TiposTrabajadores> getLovTiposTrabajadores() {
      return lovTiposTrabajadores;
   }

   public void setLovTiposTrabajadores(List<TiposTrabajadores> lovTiposTrabajadores) {
      this.lovTiposTrabajadores = lovTiposTrabajadores;
   }

   public List<UbicacionesGeograficas> getLovUbicaciones() {
      return lovUbicaciones;
   }

   public void setLovUbicaciones(List<UbicacionesGeograficas> lovUbicaciones) {
      this.lovUbicaciones = lovUbicaciones;
   }

   public Contratos getContratoSeleccionado() {
      return contratoSeleccionado;
   }

   public void setContratoSeleccionado(Contratos contratoSeleccionado) {
      this.contratoSeleccionado = contratoSeleccionado;
   }

   public EstadosAfiliaciones getEstadoAfiliacionSeleccionado() {
      return estadoAfiliacionSeleccionado;
   }

   public void setEstadoAfiliacionSeleccionado(EstadosAfiliaciones estadoAfiliacionSeleccionado) {
      this.estadoAfiliacionSeleccionado = estadoAfiliacionSeleccionado;
   }

   public Periodicidades getFormaPagoSeleccionado() {
      return formaPagoSeleccionado;
   }

   public void setFormaPagoSeleccionado(Periodicidades formaPagoSeleccionado) {
      this.formaPagoSeleccionado = formaPagoSeleccionado;
   }

   public MotivosCambiosCargos getMotivoCargoSeleccionado() {
      return motivoCargoSeleccionado;
   }

   public void setMotivoCargoSeleccionado(MotivosCambiosCargos motivoCargoSeleccionado) {
      this.motivoCargoSeleccionado = motivoCargoSeleccionado;
   }

   public MotivosContratos getMotivoContratoSeleccionado() {
      return motivoContratoSeleccionado;
   }

   public void setMotivoContratoSeleccionado(MotivosContratos motivoContratoSeleccionado) {
      this.motivoContratoSeleccionado = motivoContratoSeleccionado;
   }

   public MotivosLocalizaciones getMotivoLocalizacionSeleccionado() {
      return motivoLocalizacionSeleccionado;
   }

   public void setMotivoLocalizacionSeleccionado(MotivosLocalizaciones motivoLocalizacionSeleccionado) {
      this.motivoLocalizacionSeleccionado = motivoLocalizacionSeleccionado;
   }

   public MotivosCambiosSueldos getMotivoSueldoSeleccionado() {
      return motivoSueldoSeleccionado;
   }

   public void setMotivoSueldoSeleccionado(MotivosCambiosSueldos motivoSueldoSeleccionado) {
      this.motivoSueldoSeleccionado = motivoSueldoSeleccionado;
   }

   public ReformasLaborales getReformaLaboralSeleccionada() {
      return reformaLaboralSeleccionada;
   }

   public void setReformaLaboralSeleccionada(ReformasLaborales reformaLaboralSeleccionada) {
      this.reformaLaboralSeleccionada = reformaLaboralSeleccionada;
   }

   public Sucursales getSucursalSeleccionado() {
      return sucursalSeleccionado;
   }

   public void setSucursalSeleccionado(Sucursales sucursalSeleccionado) {
      this.sucursalSeleccionado = sucursalSeleccionado;
   }

   public TercerosSucursales getTerceroSucursalSeleccionado() {
      return terceroSucursalSeleccionado;
   }

   public void setTerceroSucursalSeleccionado(TercerosSucursales terceroSucursalSeleccionado) {
      this.terceroSucursalSeleccionado = terceroSucursalSeleccionado;
   }

   public TiposContratos getTipoContratoSeleccionado() {
      return tipoContratoSeleccionado;
   }

   public void setTipoContratoSeleccionado(TiposContratos tipoContratoSeleccionado) {
      this.tipoContratoSeleccionado = tipoContratoSeleccionado;
   }

   public TiposEntidades getTipoEntidadSeleccionado() {
      return tipoEntidadSeleccionado;
   }

   public void setTipoEntidadSeleccionado(TiposEntidades tipoEntidadSeleccionado) {
      this.tipoEntidadSeleccionado = tipoEntidadSeleccionado;
   }

   public TiposSueldos getTipoSueldoSeleccionado() {
      return tipoSueldoSeleccionado;
   }

   public void setTipoSueldoSeleccionado(TiposSueldos tipoSueldoSeleccionado) {
      this.tipoSueldoSeleccionado = tipoSueldoSeleccionado;
   }

   public TiposTrabajadores getTipoTrabajadorSeleccionado() {
      return tipoTrabajadorSeleccionado;
   }

   public void setTipoTrabajadorSeleccionado(TiposTrabajadores tipoTrabajadorSeleccionado) {
      this.tipoTrabajadorSeleccionado = tipoTrabajadorSeleccionado;
   }

   public UbicacionesGeograficas getUbicacionSeleccionado() {
      return ubicacionSeleccionado;
   }

   public void setUbicacionSeleccionado(UbicacionesGeograficas ubicacionSeleccionado) {
      this.ubicacionSeleccionado = ubicacionSeleccionado;
   }

   public String getVTipoFechaTipoTrabajador() {
      return vTipoFechaTipoTrabajador;
   }

   public void setVTipoFechaTipoTrabajador(String vTipoFechaTipoTrabajador) {
      this.vTipoFechaTipoTrabajador = vTipoFechaTipoTrabajador;
   }

   public String getVTipoFechaReformaLaboral() {
      return vTipoFechaReformaLaboral;
   }

   public void setVTipoFechaReformaLaboral(String vTipoFechaReformaLaboral) {
      this.vTipoFechaReformaLaboral = vTipoFechaReformaLaboral;
   }

   public String getVTipoFechaLegislacion() {
      return vTipoFechaLegislacion;
   }

   public void setVTipoFechaLegislacion(String vTipoFechaLegislacion) {
      this.vTipoFechaLegislacion = vTipoFechaLegislacion;
   }

   public String getVTipoFechaAfiliacion() {
      return vTipoFechaAfiliacion;
   }

   public void setVTipoFechaAfiliacion(String vTipoFechaAfiliacion) {
      this.vTipoFechaAfiliacion = vTipoFechaAfiliacion;
   }

   public String getVTipoFechaUbicacion() {
      return vTipoFechaUbicacion;
   }

   public void setVTipoFechaUbicacion(String vTipoFechaUbicacion) {
      this.vTipoFechaUbicacion = vTipoFechaUbicacion;
   }

   public String getVTipoFechaFormaPago() {
      return vTipoFechaFormaPago;
   }

   public void setVTipoFechaFormaPago(String vTipoFechaFormaPago) {
      this.vTipoFechaFormaPago = vTipoFechaFormaPago;
   }

   public String getVTipoFechaMvr() {
      return vTipoFechaMvr;
   }

   public void setVTipoFechaMvr(String vTipoFechaMvr) {
      this.vTipoFechaMvr = vTipoFechaMvr;
   }

   public String getVTipoFechaNormaLaboral() {
      return vTipoFechaNormaLaboral;
   }

   public void setVTipoFechaNormaLaboral(String vTipoFechaNormaLaboral) {
      this.vTipoFechaNormaLaboral = vTipoFechaNormaLaboral;
   }

   public String getVTipoFechaSet() {
      return vTipoFechaSet;
   }

   public void setVTipoFechaSet(String vTipoFechaSet) {
      this.vTipoFechaSet = vTipoFechaSet;
   }

   public List<JornadasLaborales> getFiltroLovJornadasLaborales() {
      return filtroLovJornadasLaborales;
   }

   public void setFiltroLovJornadasLaborales(List<JornadasLaborales> filtroLovJornadasLaborales) {
      this.filtroLovJornadasLaborales = filtroLovJornadasLaborales;
   }

   public List<NormasLaborales> getFiltroLovNormasLaborales() {
      return filtroLovNormasLaborales;
   }

   public void setFiltroLovNormasLaborales(List<NormasLaborales> filtroLovNormasLaborales) {
      this.filtroLovNormasLaborales = filtroLovNormasLaborales;
   }

   public List<JornadasLaborales> getLovJornadasLaborales() {
      return lovJornadasLaborales;
   }

   public void setLovJornadasLaborales(List<JornadasLaborales> lovJornadasLaborales) {
      this.lovJornadasLaborales = lovJornadasLaborales;
   }

   public List<NormasLaborales> getLovNormasLaborales() {
      return lovNormasLaborales;
   }

   public void setLovNormasLaborales(List<NormasLaborales> lovNormasLaborales) {
      this.lovNormasLaborales = lovNormasLaborales;
   }

   public JornadasLaborales getJornadaLaboralSeleccionado() {
      return jornadaLaboralSeleccionado;
   }

   public void setJornadaLaboralSeleccionado(JornadasLaborales jornadaLaboralSeleccionado) {
      this.jornadaLaboralSeleccionado = jornadaLaboralSeleccionado;
   }

   public NormasLaborales getNormaLaboralSeleccionado() {
      return normaLaboralSeleccionado;
   }

   public void setNormaLaboralSeleccionado(NormasLaborales normaLaboralSeleccionado) {
      this.normaLaboralSeleccionado = normaLaboralSeleccionado;
   }

   public List<Motivosmvrs> getFiltroLovMotivosmvrs() {
      return filtroLovMotivosMvrs;
   }

   public void setFiltroLovMotivosmvrs(List<Motivosmvrs> filtroLovMotivosmvrs) {
      this.filtroLovMotivosMvrs = filtroLovMotivosmvrs;
   }

   public List<Motivosmvrs> getLovMotivosmvrs() {
      return lovMotivosMvrs;
   }

   public void setLovMotivosmvrs(List<Motivosmvrs> lovMotivosmvrs) {
      this.lovMotivosMvrs = lovMotivosmvrs;
   }

   public Motivosmvrs getMotivoMvrSeleccionado() {
      return motivoMvrSeleccionado;
   }

   public void setMotivoMvrSeleccionado(Motivosmvrs motivoMvrSeleccionado) {
      this.motivoMvrSeleccionado = motivoMvrSeleccionado;
   }

   public List<MotivosRetiros> getFiltroLovMotivosRetiros() {
      return filtroLovMotivosRetiros;
   }

   public void setFiltroLovMotivosRetiros(List<MotivosRetiros> filtroLovMotivosRetiros) {
      this.filtroLovMotivosRetiros = filtroLovMotivosRetiros;
   }

   public List<MotivosRetiros> getLovMotivosRetiros() {
      return lovMotivosRetiros;
   }

   public void setLovMotivosRetiros(List<MotivosRetiros> lovMotivosRetiros) {
      this.lovMotivosRetiros = lovMotivosRetiros;
   }

   public MotivosRetiros getMotivoRetiroSeleccionado() {
      return motivoRetiroSeleccionado;
   }

   public void setMotivoRetiroSeleccionado(MotivosRetiros motivoRetiroSeleccionado) {
      this.motivoRetiroSeleccionado = motivoRetiroSeleccionado;
   }

   public String getVTipoFechaCenso() {
      return vTipoFechaCenso;
   }

   public void setVTipoFechaCenso(String vTipoFechaCenso) {
      this.vTipoFechaCenso = vTipoFechaCenso;
   }

   public String getVTipoFechaDatosPersonales() {
      return vTipoFechaDatosPersonales;
   }

   public void setVTipoFechaDatosPersonales(String vTipoFechaDatosPersonales) {
      this.vTipoFechaDatosPersonales = vTipoFechaDatosPersonales;
   }

   public String getVTipoFechaEducacionFormal() {
      return vTipoFechaEducacionFormal;
   }

   public void setVTipoFechaEducacionFormal(String vTipoFechaEducacionFormal) {
      this.vTipoFechaEducacionFormal = vTipoFechaEducacionFormal;
   }

   public String getVTipoFechaEducacionNoFormal() {
      return vTipoFechaEducacionNoFormal;
   }

   public void setVTipoFechaEducacionNoFormal(String vTipoFechaEducacionNoFormal) {
      this.vTipoFechaEducacionNoFormal = vTipoFechaEducacionNoFormal;
   }

   public String getVTipoFechaEstadoCivil() {
      return vTipoFechaEstadoCivil;
   }

   public void setVTipoFechaEstadoCivil(String vTipoFechaEstadoCivil) {
      this.vTipoFechaEstadoCivil = vTipoFechaEstadoCivil;
   }

   public List<Ciudades> getFiltroLovCiudades() {
      return filtroLovCiudades;
   }

   public void setFiltroLovCiudades(List<Ciudades> filtroLovCiudades) {
      this.filtroLovCiudades = filtroLovCiudades;
   }

   public List<Cursos> getFiltroLovCursos() {
      return filtroLovCursos;
   }

   public void setFiltroLovCursos(List<Cursos> filtroLovCursos) {
      this.filtroLovCursos = filtroLovCursos;
   }

   public List<EstadosCiviles> getFiltroLovEstadosCiviles() {
      return filtroLovEstadosCiviles;
   }

   public void setFiltroLovEstadosCiviles(List<EstadosCiviles> filtroLovEstadosCiviles) {
      this.filtroLovEstadosCiviles = filtroLovEstadosCiviles;
   }

   public List<Idiomas> getFiltroLovIdiomas() {
      return filtroLovIdiomas;
   }

   public void setFiltroLovIdiomas(List<Idiomas> filtroLovIdiomas) {
      this.filtroLovIdiomas = filtroLovIdiomas;
   }

   public List<Indicadores> getFiltroLovIndicadores() {
      return filtroLovIndicadores;
   }

   public void setFiltroLovIndicadores(List<Indicadores> filtroLovIndicadores) {
      this.filtroLovIndicadores = filtroLovIndicadores;
   }

   public List<Instituciones> getFiltroLovInstituciones() {
      return filtroLovInstituciones;
   }

   public void setFiltroLovInstituciones(List<Instituciones> filtroLovInstituciones) {
      this.filtroLovInstituciones = filtroLovInstituciones;
   }

   public List<Profesiones> getFiltroLovProfesiones() {
      return filtroLovProfesiones;
   }

   public void setFiltroLovProfesiones(List<Profesiones> filtroLovProfesiones) {
      this.filtroLovProfesiones = filtroLovProfesiones;
   }

   public List<Proyectos> getFiltroLovProyectos() {
      return filtroLovProyectos;
   }

   public void setFiltroLovProyectos(List<Proyectos> filtroLovProyectos) {
      this.filtroLovProyectos = filtroLovProyectos;
   }

   public List<PryRoles> getFiltroLovRoles() {
      return filtroLovRoles;
   }

   public void setFiltroLovRoles(List<PryRoles> filtroLovRoles) {
      this.filtroLovRoles = filtroLovRoles;
   }

   public List<SectoresEconomicos> getFiltroLovSectoresEconomicos() {
      return filtroLovSectoresEconomicos;
   }

   public void setFiltroLovSectoresEconomicos(List<SectoresEconomicos> filtroLovSectoresEconomicos) {
      this.filtroLovSectoresEconomicos = filtroLovSectoresEconomicos;
   }

   public List<TiposIndicadores> getFiltroLovTiposIndicadores() {
      return filtroLovTiposIndicadores;
   }

   public void setFiltroLovTiposIndicadores(List<TiposIndicadores> filtroLovTiposIndicadores) {
      this.filtroLovTiposIndicadores = filtroLovTiposIndicadores;
   }

   public List<Ciudades> getLovCiudades() {
      return lovCiudades;
   }

   public void setLovCiudades(List<Ciudades> lovCiudades) {
      this.lovCiudades = lovCiudades;
   }

   public List<Cursos> getLovCursos() {
      return lovCursos;
   }

   public void setLovCursos(List<Cursos> lovCursos) {
      this.lovCursos = lovCursos;
   }

   public List<EstadosCiviles> getLovEstadosCiviles() {
      return lovEstadosCiviles;
   }

   public void setLovEstadosCiviles(List<EstadosCiviles> lovEstadosCiviles) {
      this.lovEstadosCiviles = lovEstadosCiviles;
   }

   public List<Idiomas> getLovIdiomas() {
      return lovIdiomas;
   }

   public void setLovIdiomas(List<Idiomas> lovIdiomas) {
      this.lovIdiomas = lovIdiomas;
   }

   public List<Indicadores> getLovIndicadores() {
      return lovIndicadores;
   }

   public void setLovIndicadores(List<Indicadores> lovIndicadores) {
      this.lovIndicadores = lovIndicadores;
   }

   public List<Instituciones> getLovInstituciones() {
      return lovInstituciones;
   }

   public void setLovInstituciones(List<Instituciones> lovInstituciones) {
      this.lovInstituciones = lovInstituciones;
   }

   public List<Proyectos> getLovProyectos() {
      return lovProyectos;
   }

   public void setLovProyectos(List<Proyectos> lovProyectos) {
      this.lovProyectos = lovProyectos;
   }

   public List<PryRoles> getLovRoles() {
      return lovRoles;
   }

   public void setLovRoles(List<PryRoles> lovRoles) {
      this.lovRoles = lovRoles;
   }

   public List<SectoresEconomicos> getLovSectoresEconomicos() {
      return lovSectoresEconomicos;
   }

   public void setLovSectoresEconomicos(List<SectoresEconomicos> lovSectoresEconomicos) {
      this.lovSectoresEconomicos = lovSectoresEconomicos;
   }

   public List<TiposIndicadores> getLovTiposIndicadores() {
      return lovTiposIndicadores;
   }

   public void setLovTiposIndicadores(List<TiposIndicadores> lovTiposIndicadores) {
      this.lovTiposIndicadores = lovTiposIndicadores;
   }

   public List<Profesiones> getLovpProfesiones() {
      return lovProfesiones;
   }

   public void setLovpProfesiones(List<Profesiones> lovpProfesiones) {
      this.lovProfesiones = lovpProfesiones;
   }

   public Ciudades getCiudadSeleccionado() {
      return ciudadSeleccionado;
   }

   public void setCiudadSeleccionado(Ciudades ciudadSeleccionado) {
      this.ciudadSeleccionado = ciudadSeleccionado;
   }

   public Cursos getCursoSeleccionado() {
      return cursoSeleccionado;
   }

   public void setCursoSeleccionado(Cursos cursoSeleccionado) {
      this.cursoSeleccionado = cursoSeleccionado;
   }

   public EstadosCiviles getEstadoCivilSeleccionado() {
      return estadoCivilSeleccionado;
   }

   public void setEstadoCivilSeleccionado(EstadosCiviles estadoCivilSeleccionado) {
      this.estadoCivilSeleccionado = estadoCivilSeleccionado;
   }

   public Idiomas getIdiomaSeleccionado() {
      return idiomaSeleccionado;
   }

   public void setIdiomaSeleccionado(Idiomas idiomaSeleccionado) {
      this.idiomaSeleccionado = idiomaSeleccionado;
   }

   public Indicadores getIndicadorSeleccionado() {
      return indicadorSeleccionado;
   }

   public void setIndicadorSeleccionado(Indicadores indicadorSeleccionado) {
      this.indicadorSeleccionado = indicadorSeleccionado;
   }

   public Instituciones getInstitucionSeleccionado() {
      return institucionSeleccionado;
   }

   public void setInstitucionSeleccionado(Instituciones institucionSeleccionado) {
      this.institucionSeleccionado = institucionSeleccionado;
   }

   public Profesiones getProfesionSeleccionado() {
      return profesionSeleccionado;
   }

   public void setProfesionSeleccionado(Profesiones profesionSeleccionado) {
      this.profesionSeleccionado = profesionSeleccionado;
   }

   public Proyectos getProyectoSeleccionado() {
      return proyectoSeleccionado;
   }

   public void setProyectoSeleccionado(Proyectos proyectoSeleccionado) {
      this.proyectoSeleccionado = proyectoSeleccionado;
   }

   public PryRoles getRolSeleccionado() {
      return rolSeleccionado;
   }

   public void setRolSeleccionado(PryRoles rolSeleccionado) {
      this.rolSeleccionado = rolSeleccionado;
   }

   public SectoresEconomicos getSectorEconomicoSeleccionado() {
      return sectorEconomicoSeleccionado;
   }

   public void setSectorEconomicoSeleccionado(SectoresEconomicos sectorEconomicoSeleccionado) {
      this.sectorEconomicoSeleccionado = sectorEconomicoSeleccionado;
   }

   public TiposIndicadores getTipoIndicadorSeleccionado() {
      return tipoIndicadorSeleccionado;
   }

   public void setTipoIndicadorSeleccionado(TiposIndicadores tipoIndicadorSeleccionado) {
      this.tipoIndicadorSeleccionado = tipoIndicadorSeleccionado;
   }

   public List<ColumnasEscenarios> getFiltradoColumnasEscenarios() {
      return filtradoColumnasEscenarios;
   }

   public void setFiltradoColumnasEscenarios(List<ColumnasEscenarios> filtradoColumnasEscenarios) {
      this.filtradoColumnasEscenarios = filtradoColumnasEscenarios;
   }

   public List<ColumnasEscenarios> getLovColumnasEscenarios() {
      return lovColumnasEscenarios;
   }

   public void setLovColumnasEscenarios(List<ColumnasEscenarios> lovColumnasEscenarios) {
      this.lovColumnasEscenarios = lovColumnasEscenarios;
   }

   public List<ColumnasEscenarios> getColumnasEsSeleccionadas() {
      return columnasEsSeleccionadas;
   }

   public void setColumnasEsSeleccionadas(List<ColumnasEscenarios> seleccionColumnasEscenarios) {
      this.columnasEsSeleccionadas = seleccionColumnasEscenarios;
   }

   public List<ResultadoBusquedaAvanzada> getFiltroResultadoBusqueda() {
      return filtroResultadoBusqueda;
   }

   public void setFiltroResultadoBusqueda(List<ResultadoBusquedaAvanzada> filtroResultadoBusqueda) {
      this.filtroResultadoBusqueda = filtroResultadoBusqueda;
   }

   public List<ResultadoBusquedaAvanzada> getListaResultadoBusqueda() {
      return listaResultadoBusqueda;
   }

   public void setListaResultadoBusqueda(List<ResultadoBusquedaAvanzada> listaResultadoBusqueda) {
      this.listaResultadoBusqueda = listaResultadoBusqueda;
   }

   public void setInfoRegistrosCargo(String infoRegistroCargo) {
      this.infoRegistroCargo = infoRegistroCargo;
   }

   public void setInfoRegistrosEstructura(String infoRegistroEstructura) {
      this.infoRegistroEstructura = infoRegistroEstructura;
   }

   public void setInfoRegistrosJefe(String infoRegistroJefe) {
      this.infoRegistroJefe = infoRegistroJefe;
   }

   public void setInfoRegistrosMotivoCargo(String infoRegistroMotivoCargo) {
      this.infoRegistroMotivoCargo = infoRegistroMotivoCargo;
   }

   public void setInfoRegistrosPapel(String infoRegistroPapel) {
      this.infoRegistroPapel = infoRegistroPapel;
   }

   public void setInfoRegistrosMotivoLocalizacion(String infoRegistroMotivoLocalizacion) {
      this.infoRegistroMotivoLocalizacion = infoRegistroMotivoLocalizacion;
   }

   public void setInfoRegistrosTipoSueldo(String infoRegistroTipoSueldo) {
      this.infoRegistroTipoSueldo = infoRegistroTipoSueldo;
   }

   public void setInfoRegistrosMotivoSueldo(String infoRegistroMotivoSueldo) {
      this.infoRegistroMotivoSueldo = infoRegistroMotivoSueldo;
   }

   public void setInfoRegistrosTipoContrato(String infoRegistroTipoContrato) {
      this.infoRegistroTipoContrato = infoRegistroTipoContrato;
   }

   public void setInfoRegistrosMotivoContrato(String infoRegistroMotivoContrato) {
      this.infoRegistroMotivoContrato = infoRegistroMotivoContrato;
   }

   public void setInfoRegistrosTipoTrabajador(String infoRegistroTipoTrabajador) {
      this.infoRegistroTipoTrabajador = infoRegistroTipoTrabajador;
   }

   public void setInfoRegistrosReformaLaboral(String infoRegistroReformaLaboral) {
      this.infoRegistroReformaLaboral = infoRegistroReformaLaboral;
   }

   public void setInfoRegistrosContrato(String infoRegistroContrato) {
      this.infoRegistroContrato = infoRegistroContrato;
   }

   public void setInfoRegistrosUbicacion(String infoRegistroUbicacion) {
      this.infoRegistroUbicacion = infoRegistroUbicacion;
   }

   public void setInfoRegistrosTerceroSucursal(String infoRegistroTerceroSucursal) {
      this.infoRegistroTerceroSucursal = infoRegistroTerceroSucursal;
   }

   public void setInfoRegistrosTipoEntidad(String infoRegistroTipoEntidad) {
      this.infoRegistroTipoEntidad = infoRegistroTipoEntidad;
   }

   public void setInfoRegistrosEstadoAfiliacion(String infoRegistroEstadoAfiliacion) {
      this.infoRegistroEstadoAfiliacion = infoRegistroEstadoAfiliacion;
   }

   public void setInfoRegistrosFormaPago(String infoRegistroFormaPago) {
      this.infoRegistroFormaPago = infoRegistroFormaPago;
   }

   public void setInfoRegistrosSucursal(String infoRegistroSucursal) {
      this.infoRegistroSucursal = infoRegistroSucursal;
   }

   public void setInfoRegistrosMotivoMvr(String infoRegistroMotivoMvr) {
      this.infoRegistroMotivoMvr = infoRegistroMotivoMvr;
   }

   public void setInfoRegistrosNormaLaboral(String infoRegistroNormaLaboral) {
      this.infoRegistroNormaLaboral = infoRegistroNormaLaboral;
   }

   public void setInfoRegistrosJornadaLaboral(String infoRegistroJornadaLaboral) {
      this.infoRegistroJornadaLaboral = infoRegistroJornadaLaboral;
   }

   public void setInfoRegistrosMotivoRetiro(String infoRegistroMotivoRetiro) {
      this.infoRegistroMotivoRetiro = infoRegistroMotivoRetiro;
   }

   public void setInfoRegistrosCiudad(String infoRegistroCiudad) {
      this.infoRegistroCiudad = infoRegistroCiudad;
   }

   public void setInfoRegistrosEstadoCivil(String infoRegistroEstadoCivil) {
      this.infoRegistroEstadoCivil = infoRegistroEstadoCivil;
   }

   public void setInfoRegistrosIdioma(String infoRegistroIdioma) {
      this.infoRegistroIdioma = infoRegistroIdioma;
   }

   public void setInfoRegistrosTipoIndicador(String infoRegistroTipoIndicador) {
      this.infoRegistroTipoIndicador = infoRegistroTipoIndicador;
   }

   public void setInfoRegistrosIndicador(String infoRegistroIndicador) {
      this.infoRegistroIndicador = infoRegistroIndicador;
   }

   public void setInfoRegistrosProfesion(String infoRegistroProfesion) {
      this.infoRegistroProfesion = infoRegistroProfesion;
   }

   public void setInfoRegistrosInstitucion(String infoRegistroInstitucion) {
      this.infoRegistroInstitucion = infoRegistroInstitucion;
   }

   public void setInfoRegistrosCurso(String infoRegistroCurso) {
      this.infoRegistroCurso = infoRegistroCurso;
   }

   public void setInfoRegistrosSectorEconomico(String infoRegistroSectorEconomico) {
      this.infoRegistroSectorEconomico = infoRegistroSectorEconomico;
   }

   public void setInfoRegistrosProyecto(String infoRegistroProyecto) {
      this.infoRegistroProyecto = infoRegistroProyecto;
   }

   public void setInfoRegistrosRol(String infoRegistroRol) {
      this.infoRegistroRol = infoRegistroRol;
   }

   public void setInfoRegistrosColumnasEs(String infoRegistroColumnasEs) {
      this.infoRegistroColumnasEs = infoRegistroColumnasEs;
   }

   public void setInfoRegistroEstructura2(String infoRegistroEstructura2) {
      this.infoRegistroEstructura2 = infoRegistroEstructura2;
   }

   public void setInfoRegistroCiudad2(String infoRegistroCiudad2) {
      this.infoRegistroCiudad2 = infoRegistroCiudad2;
   }

   public void setInfoRegistroInstitucion2(String infoRegistroInstitucion2) {
      this.infoRegistroInstitucion2 = infoRegistroInstitucion2;
   }

   public void setInfoRegistroMotivoRetiro2(String infoRegistroMotivoRetiro2) {
      this.infoRegistroMotivoRetiro2 = infoRegistroMotivoRetiro2;
   }

   public void setInfoRegistroCargo2(String infoRegistroCargo2) {
      this.infoRegistroCargo2 = infoRegistroCargo2;
   }

   public String getInfoRegistroCargo() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:LOVCargos");
      infoRegistroCargo = String.valueOf(tabla.getRowCount());
      return infoRegistroCargo;
   }

   public String getInfoRegistroEstructura() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovEstructuras");
      infoRegistroEstructura = String.valueOf(tabla.getRowCount());
      return infoRegistroEstructura;
   }

   public String getInfoRegistroJefe() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovJefe");
      infoRegistroJefe = String.valueOf(tabla.getRowCount());
      return infoRegistroJefe;
   }

   public String getInfoRegistroMotivoCargo() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovMotivoCargo");
      infoRegistroMotivoCargo = String.valueOf(tabla.getRowCount());
      return infoRegistroMotivoCargo;
   }

   public String getInfoRegistroPapel() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovPapelCargo");
      infoRegistroPapel = String.valueOf(tabla.getRowCount());
      return infoRegistroPapel;
   }

   public String getInfoRegistroMotivoLocalizacion() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovMotivoCentroCosto");
      infoRegistroMotivoLocalizacion = String.valueOf(tabla.getRowCount());
      return infoRegistroMotivoLocalizacion;
   }

   public String getInfoRegistroEstructura2() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovLocalizacionCentroCosto");
      infoRegistroEstructura2 = String.valueOf(tabla.getRowCount());
      return infoRegistroEstructura2;
   }

   public String getInfoRegistroTipoSueldo() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovTipoSueldoSueldo");
      infoRegistroTipoSueldo = String.valueOf(tabla.getRowCount());
      return infoRegistroTipoSueldo;
   }

   public String getInfoRegistroMotivoSueldo() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovMotivoSueldo");
      infoRegistroMotivoSueldo = String.valueOf(tabla.getRowCount());
      return infoRegistroMotivoSueldo;
   }

   public String getInfoRegistroTipoContrato() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovTipoContratoFechaContrato");
      infoRegistroTipoContrato = String.valueOf(tabla.getRowCount());
      return infoRegistroTipoContrato;
   }

   public String getInfoRegistroMotivoContrato() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovMotivoFechaContrato");
      infoRegistroMotivoContrato = String.valueOf(tabla.getRowCount());
      return infoRegistroMotivoContrato;
   }

   public String getInfoRegistroTipoTrabajador() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovTipoTrabajadorTipoTrabajador");
      infoRegistroTipoTrabajador = String.valueOf(tabla.getRowCount());
      return infoRegistroTipoTrabajador;
   }

   public String getInfoRegistroReformaLaboral() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovReformaLaboralTipoSalario");
      infoRegistroReformaLaboral = String.valueOf(tabla.getRowCount());
      return infoRegistroReformaLaboral;
   }

   public String getInfoRegistroContrato() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovLegislacionLegislacionLaboral");
      infoRegistroContrato = String.valueOf(tabla.getRowCount());
      return infoRegistroContrato;
   }

   public String getInfoRegistroUbicacion() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovUbicacionUbicacion");
      infoRegistroUbicacion = String.valueOf(tabla.getRowCount());
      return infoRegistroUbicacion;
   }

   public String getInfoRegistroTerceroSucursal() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovTerceroAfiliacion");
      infoRegistroTerceroSucursal = String.valueOf(tabla.getRowCount());
      return infoRegistroTerceroSucursal;
   }

   public String getInfoRegistroTipoEntidad() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovTipoEntidadAfiliacion");
      infoRegistroTipoEntidad = String.valueOf(tabla.getRowCount());
      return infoRegistroTipoEntidad;
   }

   public String getInfoRegistroEstadoAfiliacion() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovEstadoAfiliacion");
      infoRegistroEstadoAfiliacion = String.valueOf(tabla.getRowCount());
      return infoRegistroEstadoAfiliacion;
   }

   public String getInfoRegistroFormaPago() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovPeriodicidadFormaPago");
      infoRegistroFormaPago = String.valueOf(tabla.getRowCount());
      return infoRegistroFormaPago;
   }

   public String getInfoRegistroSucursal() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovSucursalFormaPago");
      infoRegistroSucursal = String.valueOf(tabla.getRowCount());
      return infoRegistroSucursal;
   }

   public String getInfoRegistroMotivoMvr() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovMotivoMvrs");
      infoRegistroMotivoMvr = String.valueOf(tabla.getRowCount());
      return infoRegistroMotivoMvr;
   }

   public String getInfoRegistroNormaLaboral() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovNormaLaboralNormaLaboral");
      infoRegistroNormaLaboral = String.valueOf(tabla.getRowCount());
      return infoRegistroNormaLaboral;
   }

   public String getInfoRegistroJornadaLaboral() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovJornadaJornadaLaboral");
      infoRegistroJornadaLaboral = String.valueOf(tabla.getRowCount());
      return infoRegistroJornadaLaboral;
   }

   public String getInfoRegistroMotivoRetiro() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovMotivoFechaRetiro");
      infoRegistroMotivoRetiro = String.valueOf(tabla.getRowCount());
      return infoRegistroMotivoRetiro;
   }

   public String getInfoRegistroCiudad() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovCiudadDocumentoDatosPersonales");
      infoRegistroCiudad = String.valueOf(tabla.getRowCount());
      return infoRegistroCiudad;
   }

   public String getInfoRegistroCiudad2() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovCiudadNacimientoDatosPersonales");
      infoRegistroCiudad2 = String.valueOf(tabla.getRowCount());
      return infoRegistroCiudad2;
   }

   public String getInfoRegistroEstadoCivil() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovEstadoCivilEstadoCivil");
      infoRegistroEstadoCivil = String.valueOf(tabla.getRowCount());
      return infoRegistroEstadoCivil;
   }

   public String getInfoRegistroIdioma() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovIdiomaIdioma");
      infoRegistroIdioma = String.valueOf(tabla.getRowCount());
      return infoRegistroIdioma;
   }

   public String getInfoRegistroTipoIndicador() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovTipoIndicadorCenso");
      infoRegistroTipoIndicador = String.valueOf(tabla.getRowCount());
      return infoRegistroTipoIndicador;
   }

   public String getInfoRegistroIndicador() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovIndicadorCenso");
      infoRegistroIndicador = String.valueOf(tabla.getRowCount());
      return infoRegistroIndicador;
   }

   public String getInfoRegistroProfesion() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovProfesionEducacionFormal");
      infoRegistroProfesion = String.valueOf(tabla.getRowCount());
      return infoRegistroProfesion;
   }

   public String getInfoRegistroInstitucion() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovInstitucionEducacionFormal");
      infoRegistroInstitucion = String.valueOf(tabla.getRowCount());
      return infoRegistroInstitucion;
   }

   public String getInfoRegistroInstitucion2() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovInstitucionEducacionNoFormal");
      infoRegistroInstitucion2 = String.valueOf(tabla.getRowCount());
      return infoRegistroInstitucion2;
   }

   public String getInfoRegistroCurso() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovCursoEducacionNoFormal");
      infoRegistroCurso = String.valueOf(tabla.getRowCount());
      return infoRegistroCurso;
   }

   public String getInfoRegistroSectorEconomico() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovSectorEconomicoExperienciaLaboral");
      infoRegistroSectorEconomico = String.valueOf(tabla.getRowCount());
      return infoRegistroSectorEconomico;
   }

   public String getInfoRegistroMotivoRetiro2() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovMotivoRetiroExperienciaLaboral");
      infoRegistroMotivoRetiro2 = String.valueOf(tabla.getRowCount());
      return infoRegistroMotivoRetiro2;
   }

   public String getInfoRegistroProyecto() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovProyectoProyecto");
      infoRegistroProyecto = String.valueOf(tabla.getRowCount());
      return infoRegistroProyecto;
   }

   public String getInfoRegistroRol() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovPryRolProyecto");
      infoRegistroRol = String.valueOf(tabla.getRowCount());
      return infoRegistroRol;
   }

   public String getInfoRegistroCargo2() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovCargoCargoPostularse");
      infoRegistroCargo2 = String.valueOf(tabla.getRowCount());
      return infoRegistroCargo2;
   }

   public String getInfoRegistroColumnasEs() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovColumnasBusqueda");
      infoRegistroColumnasEs = String.valueOf(tabla.getRowCount());
      return infoRegistroColumnasEs;
   }

   public String getInfoRegistro() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:resultadoBusqueda");
      infoRegistro = String.valueOf(tabla.getRowCount());
      return infoRegistro;
   }

   public void setInfoRegistros(String infoRegistro) {
      this.infoRegistro = infoRegistro;
   }

   public boolean isAceptar() {
      return aceptar;
   }

   public void setAceptar(boolean aceptar) {
      this.aceptar = aceptar;
   }

   public String getAnchoTablaResultados() {
      return anchoTablaResultados;
   }

   public void setAnchoTablaResultados(String anchoTablaResultados) {
      this.anchoTablaResultados = anchoTablaResultados;
   }

   public String getTxt_vcargo_cargo() {
      return txt_vcargo_cargo;
   }

   public void setTxt_vcargo_cargo(String txt_vcargo_cargo) {
      this.txt_vcargo_cargo = txt_vcargo_cargo;
   }

   public String getTxt_vcargo_empleadoJefe_Per() {
      return txt_vcargo_empleadoJefe_Per;
   }

   public void setTxt_vcargo_empleadoJefe_Per(String txt_vcargo_empleadoJefe_Per) {
      this.txt_vcargo_empleadoJefe_Per = txt_vcargo_empleadoJefe_Per;
   }

   public String getTxt_vcargo_estructura_CC() {
      return txt_vcargo_estructura_CC;
   }

   public void setTxt_vcargo_estructura_CC(String txt_vcargo_estructura_CC) {
      this.txt_vcargo_estructura_CC = txt_vcargo_estructura_CC;
   }

   public String getTxt_vcargo_motivoCam() {
      return txt_vcargo_motivoCam;
   }

   public void setTxt_vcargo_motivoCam(String txt_vcargo_motivoCam) {
      this.txt_vcargo_motivoCam = txt_vcargo_motivoCam;
   }

   public String getTxt_vcargo_papel() {
      return txt_vcargo_papel;
   }

   public void setTxt_vcargo_papel(String txt_vcargo_papel) {
      this.txt_vcargo_papel = txt_vcargo_papel;
   }

   public String getTxt_vlocalizacion_locali() {
      return txt_vlocalizacion_locali;
   }

   public void setTxt_vlocalizacion_locali(String txt_vlocalizacion_locali) {
      this.txt_vlocalizacion_locali = txt_vlocalizacion_locali;
   }

   public String getTxt_vlocalizacion_motivo() {
      return txt_vlocalizacion_motivo;
   }

   public void setTxt_vlocalizacion_motivo(String txt_vlocalizacion_motivo) {
      this.txt_vlocalizacion_motivo = txt_vlocalizacion_motivo;
   }

   public String getTxt_vlocalizacion_proyecto() {
      return txt_vlocalizacion_proyecto;
   }

   public void setTxt_vlocalizacion_proyecto(String txt_vlocalizacion_proyecto) {
      this.txt_vlocalizacion_proyecto = txt_vlocalizacion_proyecto;
   }

   public String getTxt_vsueldo_motivo() {
      return txt_vsueldo_motivo;
   }

   public void setTxt_vsueldo_motivo(String txt_vsueldo_motivo) {
      this.txt_vsueldo_motivo = txt_vsueldo_motivo;
   }

   public String getTxt_vsueldo_tipoS() {
      return txt_vsueldo_tipoS;
   }

   public void setTxt_vsueldo_tipoS(String txt_vsueldo_tipoS) {
      this.txt_vsueldo_tipoS = txt_vsueldo_tipoS;
   }

   public String getTxt_vtipoContrato_ciudad() {
      return txt_vtipoContrato_ciudad;
   }

   public void setTxt_vtipoContrato_ciudad(String txt_vtipoContrato_ciudad) {
      this.txt_vtipoContrato_ciudad = txt_vtipoContrato_ciudad;
   }

   public String getTxt_vtipoContrato_motivo() {
      return txt_vtipoContrato_motivo;
   }

   public void setTxt_vtipoContrato_motivo(String txt_vtipoContrato_motivo) {
      this.txt_vtipoContrato_motivo = txt_vtipoContrato_motivo;
   }

   public String getTxt_vtipoContrato_tipoCont() {
      return txt_vtipoContrato_tipoCont;
   }

   public void setTxt_vtipoContrato_tipoCont(String txt_vtipoContrato_tipoCont) {
      this.txt_vtipoContrato_tipoCont = txt_vtipoContrato_tipoCont;
   }

   public String getTxt_vtipoTrab_tipoTrab() {
      return txt_vtipoTrab_tipoTrab;
   }

   public void setTxt_vtipoTrab_tipoTrab(String txt_vtipoTrab_tipoTrab) {
      this.txt_vtipoTrab_tipoTrab = txt_vtipoTrab_tipoTrab;
   }

   public String getTxt_vreformaL_ReforLab() {
      return txt_vreformaL_ReforLab;
   }

   public void setTxt_vreformaL_ReforLab(String txt_vreformaL_ReforLab) {
      this.txt_vreformaL_ReforLab = txt_vreformaL_ReforLab;
   }

   public String getTxt_vcontrato_contrato() {
      return txt_vcontrato_contrato;
   }

   public void setTxt_vcontrato_contrato(String txt_vcontrato_contrato) {
      this.txt_vcontrato_contrato = txt_vcontrato_contrato;
   }

   public String getTxt_vubicacion_ciudad() {
      return txt_vubicacion_ciudad;
   }

   public void setTxt_vubicacion_ciudad(String txt_vubicacion_ciudad) {
      this.txt_vubicacion_ciudad = txt_vubicacion_ciudad;
   }

   public String getTxt_vubicacion_ubic() {
      return txt_vubicacion_ubic;
   }

   public void setTxt_vubicacion_ubic(String txt_vubicacion_ubic) {
      this.txt_vubicacion_ubic = txt_vubicacion_ubic;
   }

   public String getTxt_vafiliacion_estado() {
      return txt_vafiliacion_estado;
   }

   public void setTxt_vafiliacion_estado(String txt_vafiliacion_estado) {
      this.txt_vafiliacion_estado = txt_vafiliacion_estado;
   }

   public String getTxt_vafiliacion_tercSuc() {
      return txt_vafiliacion_tercSuc;
   }

   public void setTxt_vafiliacion_tercSuc(String txt_vafiliacion_tercSuc) {
      this.txt_vafiliacion_tercSuc = txt_vafiliacion_tercSuc;
   }

   public String getTxt_vafiliacion_tipoEntidad() {
      return txt_vafiliacion_tipoEntidad;
   }

   public void setTxt_vafiliacion_tipoEntidad(String txt_vafiliacion_tipoEntidad) {
      this.txt_vafiliacion_tipoEntidad = txt_vafiliacion_tipoEntidad;
   }

   public String getTxt_vformapago_formaP() {
      return txt_vformapago_formaP;
   }

   public void setTxt_vformapago_formaP(String txt_vformapago_formaP) {
      this.txt_vformapago_formaP = txt_vformapago_formaP;
   }

   public String getTxt_vformapago_sucursal() {
      return txt_vformapago_sucursal;
   }

   public void setTxt_vformapago_sucursal(String txt_vformapago_sucursal) {
      this.txt_vformapago_sucursal = txt_vformapago_sucursal;
   }

   public String getTxt_mvrs_motivo() {
      return txt_mvrs_motivo;
   }

   public void setTxt_mvrs_motivo(String txt_mvrs_motivo) {
      this.txt_mvrs_motivo = txt_mvrs_motivo;
   }

   public String getTxt_vnorma_norma() {
      return txt_vnorma_norma;
   }

   public void setTxt_vnorma_norma(String txt_vnorma_norma) {
      this.txt_vnorma_norma = txt_vnorma_norma;
   }

   public String getTxt_vjornada_jornada() {
      return txt_vjornada_jornada;
   }

   public void setTxt_vjornada_jornada(String txt_vjornada_jornada) {
      this.txt_vjornada_jornada = txt_vjornada_jornada;
   }

   public String getTxtP_empleado_per_ciudadNac() {
      return txtP_empleado_per_ciudadNac;
   }

   public void setTxtP_empleado_per_ciudadNac(String txtP_empleado_per_ciudadNac) {
      this.txtP_empleado_per_ciudadNac = txtP_empleado_per_ciudadNac;
   }

   public String getTxtP_empleado_per_ciudadDoc() {
      return txtP_empleado_per_ciudadDoc;
   }

   public void setTxtP_empleado_per_ciudadDoc(String txtP_empleado_per_ciudadDoc) {
      this.txtP_empleado_per_ciudadDoc = txtP_empleado_per_ciudadDoc;
   }

   public String getTxtP_empleado_per_tipoDoc() {
      return txtP_empleado_per_tipoDoc;
   }

   public void setTxtP_empleado_per_tipoDoc(String txtP_empleado_per_tipoDoc) {
      this.txtP_empleado_per_tipoDoc = txtP_empleado_per_tipoDoc;
   }

   public String getTxtP_estadoCivil() {
      return txtP_estadoCivil;
   }

   public void setTxtP_estadoCivil(String txtP_estadoCivil) {
      this.txtP_estadoCivil = txtP_estadoCivil;
   }

   public String getTxtP_idiomap_idioma() {
      return txtP_idiomap_idioma;
   }

   public void setTxtP_idiomap_idioma(String txtP_idiomap_idioma) {
      this.txtP_idiomap_idioma = txtP_idiomap_idioma;
   }

   public String getTxtP_vindicador_indic() {
      return txtP_vindicador_indic;
   }

   public void setTxtP_vindicador_indic(String txtP_vindicador_indic) {
      this.txtP_vindicador_indic = txtP_vindicador_indic;
   }

   public String getTxtP_vindicador_tipoIndi() {
      return txtP_vindicador_tipoIndi;
   }

   public void setTxtP_vindicador_tipoIndi(String txtP_vindicador_tipoIndi) {
      this.txtP_vindicador_tipoIndi = txtP_vindicador_tipoIndi;
   }

   public String getTxtP_vFormal_institu() {
      return txtP_vFormal_institu;
   }

   public void setTxtP_vFormal_institu(String txtP_vFormal_institu) {
      this.txtP_vFormal_institu = txtP_vFormal_institu;
   }

   public String getTxtP_vFormal_profecion() {
      return txtP_vFormal_profecion;
   }

   public void setTxtP_vFormal_profecion(String txtP_vFormal_profecion) {
      this.txtP_vFormal_profecion = txtP_vFormal_profecion;
   }

   public String getTxtP_vNoformal_curso() {
      return txtP_vNoformal_curso;
   }

   public void setTxtP_vNoformal_curso(String txtP_vNoformal_curso) {
      this.txtP_vNoformal_curso = txtP_vNoformal_curso;
   }

   public String getTxtP_vNoformal_institu() {
      return txtP_vNoformal_institu;
   }

   public void setTxtP_vNoformal_institu(String txtP_vNoformal_institu) {
      this.txtP_vNoformal_institu = txtP_vNoformal_institu;
   }

   public String getTxtP_experLab_SectorEco() {
      return txtP_experLab_SectorEco;
   }

   public void setTxtP_experLab_SectorEco(String txtP_experLab_SectorEco) {
      this.txtP_experLab_SectorEco = txtP_experLab_SectorEco;
   }

   public String getTxtP_experLab_motivo() {
      return txtP_experLab_motivo;
   }

   public void setTxtP_experLab_motivo(String txtP_experLab_motivo) {
      this.txtP_experLab_motivo = txtP_experLab_motivo;
   }

   public String getTxtP_vproyecto_proyect() {
      return txtP_vproyecto_proyect;
   }

   public void setTxtP_vproyecto_proyect(String txtP_vproyecto_proyect) {
      this.txtP_vproyecto_proyect = txtP_vproyecto_proyect;
   }

   public String getTxtP_vproyecto_rol() {
      return txtP_vproyecto_rol;
   }

   public void setTxtP_vproyecto_rol(String txtP_vproyecto_rol) {
      this.txtP_vproyecto_rol = txtP_vproyecto_rol;
   }

   public String getTxtP_cargoPostul() {
      return txtP_cargoPostul;
   }

   public void setTxtP_cargoPostul(String txtP_cargoPostul) {
      this.txtP_cargoPostul = txtP_cargoPostul;
   }

   public String getTxt_motivoRet() {
      return txt_motivoRet;
   }

   public void setTxt_motivoRet(String txt_motivoRet) {
      this.txt_motivoRet = txt_motivoRet;
   }

   public String getTxt_vcargo_estructura() {
      return txt_vcargo_estructura;
   }

   public void setTxt_vcargo_estructura(String txt_vcargo_estructura) {
      this.txt_vcargo_estructura = txt_vcargo_estructura;
   }

   public List<BigInteger> getListaSecuenciasEmpleado() {
      return listaSecuenciasEmpleado;
   }

   public void setListaSecuenciasEmpleado(List<BigInteger> listaSecuenciasEmpleado) {
      this.listaSecuenciasEmpleado = listaSecuenciasEmpleado;
   }

}
