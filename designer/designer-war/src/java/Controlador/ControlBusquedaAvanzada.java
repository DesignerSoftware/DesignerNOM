package Controlador;

import ClasesAyuda.ParametrosBusquedaAvanzada;
import ClasesAyuda.ParametrosBusquedaNomina;
import ClasesAyuda.ParametrosBusquedaPersonal;
import ClasesAyuda.ParametrosQueryBusquedaAvanzada;
import ComponentesDinamicos.ControladorColumnasDinamicas;
import Entidades.*;
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
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
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
   //Para Todos los listados
//   private String infoRegistro;
   private boolean aceptar;

   public ControlBusquedaAvanzada() {
      //Inicializar objeto de negocio
      parametros = new ParametrosBusquedaAvanzada();
      parametros.setParametrosBusquedaNomina(new ParametrosBusquedaNomina());

      parametros.getParametrosBusquedaNomina().setVigenciaCargo(new VigenciasCargos());
      parametros.getParametrosBusquedaNomina().getVigenciaCargo().setCargo(new Cargos());
//      parametros.getParametrosBusquedaNomina().getVigenciaCargo().setEmpleado(new Empleados());
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
//      infoRegistro = "";
      aceptar = true;
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
   public void autocompletarParametros(String cualParametro, String valorConfirmar) {
      int coincidencias = 0;
      int indiceUnicoElemento = 0;

      if (!valorPorDefecto.isEmpty()) {
         valorPorDefecto = "";
      }

      if (cualParametro.equals("CARGO")) {
         if (parametros.getParametrosBusquedaNomina().getVigenciaCargo().getCargo() != null) {
            parametros.getParametrosBusquedaNomina().getVigenciaCargo().getCargo().setNombre(valorCopia);
         }
         parametros.getParametrosBusquedaNomina().getVigenciaCargo().setCargo(null);
         if (!valorConfirmar.isEmpty()) {
            if (lovCargos == null) {
               requerirLOV(cualParametro);
            }

            if (lovCargos != null) {
               for (int i = 0; i < lovCargos.size(); i++) {
                  if (lovCargos.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                     indiceUnicoElemento = i;
                     coincidencias++;
                  }
               }
               if (coincidencias == 1) {
                  parametros.getParametrosBusquedaNomina().getVigenciaCargo().setCargo(lovCargos.get(indiceUnicoElemento));
               } else {
                  RequestContext.getCurrentInstance().update("formularioDialogos:DialogoCargos");
                  RequestContext.getCurrentInstance().execute("PF('DialogoCargos').show()");
               }
            } else {
               RequestContext.getCurrentInstance().execute("PF('errorListaDialogo').show()");
            }
         }
      } else if (cualParametro.equals("ESTRUCTURA")) {
         if (parametros.getParametrosBusquedaNomina().getVigenciaCargo().getEstructura() != null) {
            parametros.getParametrosBusquedaNomina().getVigenciaCargo().getEstructura().setNombre(valorCopia);
         }
         parametros.getParametrosBusquedaNomina().getVigenciaCargo().setEstructura(null);
         if (!valorConfirmar.isEmpty()) {
            if (lovEstructuras == null) {
               requerirLOV(cualParametro);
            }

            if (lovEstructuras != null) {
               for (int i = 0; i < lovEstructuras.size(); i++) {
                  if (lovEstructuras.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                     indiceUnicoElemento = i;
                     coincidencias++;
                  }
               }
               if (coincidencias == 1) {
                  parametros.getParametrosBusquedaNomina().getVigenciaCargo().setEstructura(lovEstructuras.get(indiceUnicoElemento));
               } else {
                  RequestContext.getCurrentInstance().update("formularioDialogos:EstructuraDialogo");
                  RequestContext.getCurrentInstance().execute("PF('EstructuraDialogo').show()");
               }
            } else {
               RequestContext.getCurrentInstance().execute("PF('errorListaDialogo').show()");
            }
         }
      } else if (cualParametro.equals("JEFE")) {
         if (parametros.getParametrosBusquedaNomina().getVigenciaCargo().getEmpleadojefe() != null) {
            parametros.getParametrosBusquedaNomina().getVigenciaCargo().getEmpleadojefe().getPersona().setNombreCompleto(valorCopia);
         }
         parametros.getParametrosBusquedaNomina().getVigenciaCargo().setEmpleadojefe(null);
         if (!valorConfirmar.isEmpty()) {
            if (lovJefe == null) {
               requerirLOV(cualParametro);
            }

            if (lovJefe != null) {
               for (int i = 0; i < lovJefe.size(); i++) {
                  if (lovJefe.get(i).getPersona().getNombreCompleto().startsWith(valorConfirmar.toUpperCase())) {
                     indiceUnicoElemento = i;
                     coincidencias++;
                  }
               }
               if (coincidencias == 1) {
                  parametros.getParametrosBusquedaNomina().getVigenciaCargo().setEmpleadojefe(lovJefe.get(indiceUnicoElemento));
               } else {
                  RequestContext.getCurrentInstance().update("formularioDialogos:JefeDialogo");
                  RequestContext.getCurrentInstance().execute("PF('JefeDialogo').show()");
               }
            } else {
               RequestContext.getCurrentInstance().execute("PF('errorListaDialogo').show()");
            }
         }
      } else if (cualParametro.equals("MOTIVOCARGO")) {
         if (parametros.getParametrosBusquedaNomina().getVigenciaCargo().getMotivocambiocargo() != null) {
            parametros.getParametrosBusquedaNomina().getVigenciaCargo().getMotivocambiocargo().setNombre(valorCopia);
         }
         parametros.getParametrosBusquedaNomina().getVigenciaCargo().setMotivocambiocargo(null);
         if (!valorConfirmar.isEmpty()) {
            if (lovMotivosCargos == null) {
               requerirLOV(cualParametro);
            }

            if (lovMotivosCargos != null) {
               for (int i = 0; i < lovMotivosCargos.size(); i++) {
                  if (lovMotivosCargos.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                     indiceUnicoElemento = i;
                     coincidencias++;
                  }
               }
               if (coincidencias == 1) {
                  parametros.getParametrosBusquedaNomina().getVigenciaCargo().setMotivocambiocargo(lovMotivosCargos.get(indiceUnicoElemento));
               } else {
                  RequestContext.getCurrentInstance().update("formularioDialogos:MotivoCargoDialogo");
                  RequestContext.getCurrentInstance().execute("PF('MotivoCargoDialogo').show()");
               }
            } else {
               RequestContext.getCurrentInstance().execute("PF('errorListaDialogo').show()");
            }
         }
      } else if (cualParametro.equals("PAPEL")) {
         if (parametros.getParametrosBusquedaNomina().getVigenciaCargo().getPapel() != null) {
            parametros.getParametrosBusquedaNomina().getVigenciaCargo().getPapel().setDescripcion(valorCopia);
         }
         parametros.getParametrosBusquedaNomina().getVigenciaCargo().setPapel(null);
         if (!valorConfirmar.isEmpty()) {
            if (lovPapeles == null) {
               requerirLOV(cualParametro);
            }

            if (lovPapeles != null) {
               for (int i = 0; i < lovPapeles.size(); i++) {
                  if (lovPapeles.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                     indiceUnicoElemento = i;
                     coincidencias++;
                  }
               }
               if (coincidencias == 1) {
                  parametros.getParametrosBusquedaNomina().getVigenciaCargo().setPapel(lovPapeles.get(indiceUnicoElemento));
               } else {
                  RequestContext.getCurrentInstance().update("formularioDialogos:PapelCargoDialogo");
                  RequestContext.getCurrentInstance().execute("PF('PapelCargoDialogo').show()");
               }
            } else {
               RequestContext.getCurrentInstance().execute("PF('errorListaDialogo').show()");
            }
         }
      } else if (cualParametro.equals("LOCALIZACION")) {
         if (parametros.getParametrosBusquedaNomina().getVigenciaLocalizacion().getLocalizacion() != null) {
            parametros.getParametrosBusquedaNomina().getVigenciaLocalizacion().getLocalizacion().setNombre(valorCopia);
         }
         parametros.getParametrosBusquedaNomina().getVigenciaLocalizacion().setLocalizacion(null);
         if (!valorConfirmar.isEmpty()) {
            if (lovEstructuras == null) {
               requerirLOV(cualParametro);
            }

            if (lovEstructuras != null) {
               for (int i = 0; i < lovEstructuras.size(); i++) {
                  if (lovEstructuras.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                     indiceUnicoElemento = i;
                     coincidencias++;
                  }
               }
               if (coincidencias == 1) {
                  parametros.getParametrosBusquedaNomina().getVigenciaLocalizacion().setLocalizacion(lovEstructuras.get(indiceUnicoElemento));
               } else {
                  RequestContext.getCurrentInstance().update("formularioDialogos:LocalizacionCentroCostoDialogo");
                  RequestContext.getCurrentInstance().execute("PF('LocalizacionCentroCostoDialogo').show()");
               }
            } else {
               RequestContext.getCurrentInstance().execute("PF('errorListaDialogo').show()");
            }
         }
      } else if (cualParametro.equals("MOTIVOLOCALIZACION")) {
         if (parametros.getParametrosBusquedaNomina().getVigenciaLocalizacion().getMotivo() != null) {
            parametros.getParametrosBusquedaNomina().getVigenciaLocalizacion().getMotivo().setDescripcion(valorCopia);
         }
         parametros.getParametrosBusquedaNomina().getVigenciaLocalizacion().setMotivo(null);
         if (!valorConfirmar.isEmpty()) {
            if (lovMotivosLocalizaciones == null) {
               requerirLOV(cualParametro);
            }

            if (lovMotivosLocalizaciones != null) {
               for (int i = 0; i < lovMotivosLocalizaciones.size(); i++) {
                  if (lovMotivosLocalizaciones.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                     indiceUnicoElemento = i;
                     coincidencias++;
                  }
               }
               if (coincidencias == 1) {
                  parametros.getParametrosBusquedaNomina().getVigenciaLocalizacion().setMotivo(lovMotivosLocalizaciones.get(indiceUnicoElemento));
               } else {
                  RequestContext.getCurrentInstance().update("formularioDialogos:MotivoCentroCostoDialogo");
                  RequestContext.getCurrentInstance().execute("PF('MotivoCentroCostoDialogo').show()");
               }
            } else {
               RequestContext.getCurrentInstance().execute("PF('errorListaDialogo').show()");
            }
         }
      } else if (cualParametro.equals("TIPOSUELDO")) {
         if (parametros.getParametrosBusquedaNomina().getVigenciaSueldo().getTiposueldo() != null) {
            parametros.getParametrosBusquedaNomina().getVigenciaSueldo().getTiposueldo().setDescripcion(valorCopia);
         }
         parametros.getParametrosBusquedaNomina().getVigenciaSueldo().setTiposueldo(null);
         if (!valorConfirmar.isEmpty()) {
            if (lovTiposSueldos == null) {
               requerirLOV(cualParametro);
            }

            if (lovTiposSueldos != null) {
               for (int i = 0; i < lovTiposSueldos.size(); i++) {
                  if (lovTiposSueldos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                     indiceUnicoElemento = i;
                     coincidencias++;
                  }
               }
               if (coincidencias == 1) {
                  parametros.getParametrosBusquedaNomina().getVigenciaSueldo().setTiposueldo(lovTiposSueldos.get(indiceUnicoElemento));
               } else {
                  RequestContext.getCurrentInstance().update("formularioDialogos:TipoSueldoSueldoDialogo");
                  RequestContext.getCurrentInstance().execute("PF('TipoSueldoSueldoDialogo').show()");
               }
            } else {
               RequestContext.getCurrentInstance().execute("PF('errorListaDialogo').show()");
            }
         }
      } else if (cualParametro.equals("MOTIVOSUELDO")) {
         if (parametros.getParametrosBusquedaNomina().getVigenciaSueldo().getMotivocambiosueldo() != null) {
            parametros.getParametrosBusquedaNomina().getVigenciaSueldo().getMotivocambiosueldo().setNombre(valorCopia);
         }
         parametros.getParametrosBusquedaNomina().getVigenciaSueldo().setMotivocambiosueldo(null);
         if (!valorConfirmar.isEmpty()) {
            if (lovMotivosSueldos == null) {
               requerirLOV(cualParametro);
            }

            if (lovMotivosSueldos != null) {
               for (int i = 0; i < lovMotivosSueldos.size(); i++) {
                  if (lovMotivosSueldos.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                     indiceUnicoElemento = i;
                     coincidencias++;
                  }
               }
               if (coincidencias == 1) {
                  parametros.getParametrosBusquedaNomina().getVigenciaSueldo().setMotivocambiosueldo(lovMotivosSueldos.get(indiceUnicoElemento));
               } else {
                  RequestContext.getCurrentInstance().update("formularioDialogos:MotivoSueldoDialogo");
                  RequestContext.getCurrentInstance().execute("PF('MotivoSueldoDialogo').show()");
               }
            } else {
               RequestContext.getCurrentInstance().execute("PF('errorListaDialogo').show()");
            }
         }
      } else if (cualParametro.equals("TIPOCONTRATO")) {
         if (parametros.getParametrosBusquedaNomina().getVigenciaTipoContrato().getTipocontrato() != null) {
            parametros.getParametrosBusquedaNomina().getVigenciaTipoContrato().getTipocontrato().setNombre(valorCopia);
         }
         parametros.getParametrosBusquedaNomina().getVigenciaTipoContrato().setTipocontrato(null);
         if (!valorConfirmar.isEmpty()) {
            if (lovTiposContratos == null) {
               requerirLOV(cualParametro);
            }

            if (lovTiposContratos != null) {
               for (int i = 0; i < lovTiposContratos.size(); i++) {
                  if (lovTiposContratos.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                     indiceUnicoElemento = i;
                     coincidencias++;
                  }
               }
               if (coincidencias == 1) {
                  parametros.getParametrosBusquedaNomina().getVigenciaTipoContrato().setTipocontrato(lovTiposContratos.get(indiceUnicoElemento));
               } else {
                  RequestContext.getCurrentInstance().update("formularioDialogos:TipoContratoFechaContratoDialogo");
                  RequestContext.getCurrentInstance().execute("PF('TipoContratoFechaContratoDialogo').show()");
               }
            } else {
               RequestContext.getCurrentInstance().execute("PF('errorListaDialogo').show()");
            }
         }
      } else if (cualParametro.equals("MOTIVOCONTRATO")) {
         if (parametros.getParametrosBusquedaNomina().getVigenciaTipoContrato().getMotivocontrato() != null) {
            parametros.getParametrosBusquedaNomina().getVigenciaTipoContrato().getMotivocontrato().setNombre(valorCopia);
         }
         parametros.getParametrosBusquedaNomina().getVigenciaTipoContrato().setMotivocontrato(null);
         if (!valorConfirmar.isEmpty()) {
            if (lovMotivosContratos == null) {
               requerirLOV(cualParametro);
            }

            if (lovMotivosContratos != null) {
               for (int i = 0; i < lovMotivosContratos.size(); i++) {
                  if (lovMotivosContratos.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                     indiceUnicoElemento = i;
                     coincidencias++;
                  }
               }
               if (coincidencias == 1) {
                  parametros.getParametrosBusquedaNomina().getVigenciaTipoContrato().setMotivocontrato(lovMotivosContratos.get(indiceUnicoElemento));
               } else {
                  RequestContext.getCurrentInstance().update("formularioDialogos:MotivoFechaContratoDialogo");
                  RequestContext.getCurrentInstance().execute("PF('MotivoFechaContratoDialogo').show()");
               }
            } else {
               RequestContext.getCurrentInstance().execute("PF('errorListaDialogo').show()");
            }
         }
      } else if (cualParametro.equals("TIPOTRABAJADOR")) {
         if (parametros.getParametrosBusquedaNomina().getVigenciaTipoTrabajador().getTipotrabajador() != null) {
            parametros.getParametrosBusquedaNomina().getVigenciaTipoTrabajador().getTipotrabajador().setNombre(valorCopia);
         }
         parametros.getParametrosBusquedaNomina().getVigenciaTipoTrabajador().setTipotrabajador(null);
         if (!valorConfirmar.isEmpty()) {
            if (lovTiposTrabajadores == null) {
               requerirLOV(cualParametro);
            }

            if (lovTiposTrabajadores != null) {
               for (int i = 0; i < lovTiposTrabajadores.size(); i++) {
                  if (lovTiposTrabajadores.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                     indiceUnicoElemento = i;
                     coincidencias++;
                  }
               }
               if (coincidencias == 1) {
                  parametros.getParametrosBusquedaNomina().getVigenciaTipoTrabajador().setTipotrabajador(lovTiposTrabajadores.get(indiceUnicoElemento));
               } else {
                  RequestContext.getCurrentInstance().update("formularioDialogos:TipoTrabajadorTipoTrabajadorDialogo");
                  RequestContext.getCurrentInstance().execute("PF('TipoTrabajadorTipoTrabajadorDialogo').show()");
               }
            } else {
               RequestContext.getCurrentInstance().execute("PF('errorListaDialogo').show()");
            }
         }
      } else if (cualParametro.equals("REFORMALABORAL")) {
         if (parametros.getParametrosBusquedaNomina().getVigenciaReformasLaboral().getReformalaboral() != null) {
            parametros.getParametrosBusquedaNomina().getVigenciaReformasLaboral().getReformalaboral().setNombre(valorCopia);
         }
         parametros.getParametrosBusquedaNomina().getVigenciaReformasLaboral().setReformalaboral(null);
         if (!valorConfirmar.isEmpty()) {
            if (lovReformasLaborales == null) {
               requerirLOV(cualParametro);
            }

            if (lovReformasLaborales != null) {
               for (int i = 0; i < lovReformasLaborales.size(); i++) {
                  if (lovReformasLaborales.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                     indiceUnicoElemento = i;
                     coincidencias++;
                  }
               }
               if (coincidencias == 1) {
                  parametros.getParametrosBusquedaNomina().getVigenciaReformasLaboral().setReformalaboral(lovReformasLaborales.get(indiceUnicoElemento));
               } else {
                  RequestContext.getCurrentInstance().update("formularioDialogos:ReformaLaboralTipoSalarioDialogo");
                  RequestContext.getCurrentInstance().execute("PF('ReformaLaboralTipoSalarioDialogo').show()");
               }
            } else {
               RequestContext.getCurrentInstance().execute("PF('errorListaDialogo').show()");
            }
         }
      } else if (cualParametro.equals("CONTRATO")) {
         if (parametros.getParametrosBusquedaNomina().getVigenciaContrato().getContrato() != null) {
            parametros.getParametrosBusquedaNomina().getVigenciaContrato().getContrato().setDescripcion(valorCopia);
         }
         parametros.getParametrosBusquedaNomina().getVigenciaContrato().setContrato(null);
         if (!valorConfirmar.isEmpty()) {
            if (lovContratos == null) {
               requerirLOV(cualParametro);
            }

            if (lovContratos != null) {
               for (int i = 0; i < lovContratos.size(); i++) {
                  if (lovContratos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                     indiceUnicoElemento = i;
                     coincidencias++;
                  }
               }
               if (coincidencias == 1) {
                  parametros.getParametrosBusquedaNomina().getVigenciaContrato().setContrato(lovContratos.get(indiceUnicoElemento));
               } else {
                  RequestContext.getCurrentInstance().update("formularioDialogos:LegislacionLegislacionLaboralDialogo");
                  RequestContext.getCurrentInstance().execute("PF('LegislacionLegislacionLaboralDialogo').show()");
               }
            } else {
               RequestContext.getCurrentInstance().execute("PF('errorListaDialogo').show()");
            }
         }
      } else if (cualParametro.equals("UBICACION")) {
         if (parametros.getParametrosBusquedaNomina().getVigenciaUbicacion().getUbicacion() != null) {
            parametros.getParametrosBusquedaNomina().getVigenciaUbicacion().getUbicacion().setDescripcion(valorCopia);
         }
         parametros.getParametrosBusquedaNomina().getVigenciaUbicacion().setUbicacion(null);
         if (!valorConfirmar.isEmpty()) {
            if (lovUbicaciones == null) {
               requerirLOV(cualParametro);
            }

            if (lovUbicaciones != null) {
               for (int i = 0; i < lovUbicaciones.size(); i++) {
                  if (lovUbicaciones.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                     indiceUnicoElemento = i;
                     coincidencias++;
                  }
               }
               if (coincidencias == 1) {
                  parametros.getParametrosBusquedaNomina().getVigenciaUbicacion().setUbicacion(lovUbicaciones.get(indiceUnicoElemento));
               } else {
                  RequestContext.getCurrentInstance().update("formularioDialogos:UbicacionUbicacionDialogo");
                  RequestContext.getCurrentInstance().execute("PF('UbicacionUbicacionDialogo').show()");
               }
            } else {
               RequestContext.getCurrentInstance().execute("PF('errorListaDialogo').show()");
            }
         }
      } else if (cualParametro.equals("TERCEROSUCURSAL")) {
         if (parametros.getParametrosBusquedaNomina().getVigenciaAfiliacion().getTercerosucursal() != null) {
            parametros.getParametrosBusquedaNomina().getVigenciaAfiliacion().getTercerosucursal().setDescripcion(valorCopia);
         }
         parametros.getParametrosBusquedaNomina().getVigenciaAfiliacion().setTercerosucursal(null);
         if (!valorConfirmar.isEmpty()) {
            if (lovTercerosSucursales == null) {
               requerirLOV(cualParametro);
            }

            if (lovTercerosSucursales != null) {
               for (int i = 0; i < lovTercerosSucursales.size(); i++) {
                  if (lovTercerosSucursales.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                     indiceUnicoElemento = i;
                     coincidencias++;
                  }
               }
               if (coincidencias == 1) {
                  parametros.getParametrosBusquedaNomina().getVigenciaAfiliacion().setTercerosucursal(lovTercerosSucursales.get(indiceUnicoElemento));
               } else {
                  RequestContext.getCurrentInstance().update("formularioDialogos:TerceroAfiliacionDialogo");
                  RequestContext.getCurrentInstance().execute("PF('TerceroAfiliacionDialogo').show()");
               }
            } else {
               RequestContext.getCurrentInstance().execute("PF('errorListaDialogo').show()");
            }
         }
      } else if (cualParametro.equals("TIPOENTIDAD")) {
         if (parametros.getParametrosBusquedaNomina().getVigenciaAfiliacion().getTipoentidad() != null) {
            parametros.getParametrosBusquedaNomina().getVigenciaAfiliacion().getTipoentidad().setNombre(valorCopia);
         }
         parametros.getParametrosBusquedaNomina().getVigenciaAfiliacion().setTipoentidad(null);
         if (!valorConfirmar.isEmpty()) {
            if (lovTiposEntidades == null) {
               requerirLOV(cualParametro);
            }

            if (lovTiposEntidades != null) {
               for (int i = 0; i < lovTiposEntidades.size(); i++) {
                  if (lovTiposEntidades.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                     indiceUnicoElemento = i;
                     coincidencias++;
                  }
               }
               if (coincidencias == 1) {
                  parametros.getParametrosBusquedaNomina().getVigenciaAfiliacion().setTipoentidad(lovTiposEntidades.get(indiceUnicoElemento));
               } else {
                  RequestContext.getCurrentInstance().update("formularioDialogos:TipoEntidadAfiliacionDialogo");
                  RequestContext.getCurrentInstance().execute("PF('TipoEntidadAfiliacionDialogo').show()");
               }
            } else {
               RequestContext.getCurrentInstance().execute("PF('errorListaDialogo').show()");
            }
         }
      } else if (cualParametro.equals("ESTADOAFILIACION")) {
         if (parametros.getParametrosBusquedaNomina().getVigenciaAfiliacion().getEstadoafiliacion() != null) {
            parametros.getParametrosBusquedaNomina().getVigenciaAfiliacion().getEstadoafiliacion().setNombre(valorCopia);
         }
         parametros.getParametrosBusquedaNomina().getVigenciaAfiliacion().setEstadoafiliacion(null);
         if (!valorConfirmar.isEmpty()) {
            if (lovEstadosAfiliaciones == null) {
               requerirLOV(cualParametro);
            }

            if (lovEstadosAfiliaciones != null) {
               for (int i = 0; i < lovEstadosAfiliaciones.size(); i++) {
                  if (lovEstadosAfiliaciones.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                     indiceUnicoElemento = i;
                     coincidencias++;
                  }
               }
               if (coincidencias == 1) {
                  parametros.getParametrosBusquedaNomina().getVigenciaAfiliacion().setEstadoafiliacion(lovEstadosAfiliaciones.get(indiceUnicoElemento));
               } else {
                  RequestContext.getCurrentInstance().update("formularioDialogos:EstadoAfiliacionDialogo");
                  RequestContext.getCurrentInstance().execute("PF('EstadoAfiliacionDialogo').show()");
               }
            } else {
               RequestContext.getCurrentInstance().execute("PF('errorListaDialogo').show()");
            }
         }
      } else if (cualParametro.equals("FORMAPAGO")) {
         if (parametros.getParametrosBusquedaNomina().getVigenciaFormaPago().getFormapago() != null) {
            parametros.getParametrosBusquedaNomina().getVigenciaFormaPago().getFormapago().setNombre(valorCopia);
         }
         parametros.getParametrosBusquedaNomina().getVigenciaFormaPago().setFormapago(null);
         if (!valorConfirmar.isEmpty()) {
            if (lovFormasPagos == null) {
               requerirLOV(cualParametro);
            }

            if (lovFormasPagos != null) {
               for (int i = 0; i < lovFormasPagos.size(); i++) {
                  if (lovFormasPagos.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                     indiceUnicoElemento = i;
                     coincidencias++;
                  }
               }
               if (coincidencias == 1) {
                  parametros.getParametrosBusquedaNomina().getVigenciaFormaPago().setFormapago(lovFormasPagos.get(indiceUnicoElemento));
               } else {
                  RequestContext.getCurrentInstance().update("formularioDialogos:PeriodicidadFormaPagoDialogo");
                  RequestContext.getCurrentInstance().execute("PF('PeriodicidadFormaPagoDialogo').show()");
               }
            } else {
               RequestContext.getCurrentInstance().execute("PF('errorListaDialogo').show()");
            }
         }
      } else if (cualParametro.equals("SUCURSAL")) {
         if (parametros.getParametrosBusquedaNomina().getVigenciaFormaPago().getSucursal() != null) {
            parametros.getParametrosBusquedaNomina().getVigenciaFormaPago().getSucursal().setNombre(valorCopia);
         }
         parametros.getParametrosBusquedaNomina().getVigenciaFormaPago().setSucursal(null);
         if (!valorConfirmar.isEmpty()) {
            if (lovSucursales == null) {
               requerirLOV(cualParametro);
            }

            if (lovSucursales != null) {
               for (int i = 0; i < lovSucursales.size(); i++) {
                  if (lovSucursales.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                     indiceUnicoElemento = i;
                     coincidencias++;
                  }
               }
               if (coincidencias == 1) {
                  parametros.getParametrosBusquedaNomina().getVigenciaFormaPago().setSucursal(lovSucursales.get(indiceUnicoElemento));
               } else {
                  RequestContext.getCurrentInstance().update("formularioDialogos:SucursalFormaPagoDialogo");
                  RequestContext.getCurrentInstance().execute("PF('SucursalFormaPagoDialogo').show()");
               }
            } else {
               RequestContext.getCurrentInstance().execute("PF('errorListaDialogo').show()");
            }
         }
      } else if (cualParametro.equals("MOTIVOMVR")) {
         if (parametros.getParametrosBusquedaNomina().getMvrs().getMotivo() != null) {
            parametros.getParametrosBusquedaNomina().getMvrs().getMotivo().setNombre(valorCopia);
         }
         parametros.getParametrosBusquedaNomina().getMvrs().setMotivo(null);
         if (!valorConfirmar.isEmpty()) {
            if (lovMotivosMvrs == null) {
               requerirLOV(cualParametro);
            }

            if (lovMotivosMvrs != null) {
               for (int i = 0; i < lovMotivosMvrs.size(); i++) {
                  if (lovMotivosMvrs.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                     indiceUnicoElemento = i;
                     coincidencias++;
                  }
               }
               if (coincidencias == 1) {
                  parametros.getParametrosBusquedaNomina().getMvrs().setMotivo(lovMotivosMvrs.get(indiceUnicoElemento));
               } else {
                  RequestContext.getCurrentInstance().update("formularioDialogos:MotivoMvrsDialogo");
                  RequestContext.getCurrentInstance().execute("PF('MotivoMvrsDialogo').show()");
               }
            } else {
               RequestContext.getCurrentInstance().execute("PF('errorListaDialogo').show()");
            }
         }
      } else if (cualParametro.equals("NORMALABORAL")) {
         if (parametros.getParametrosBusquedaNomina().getVigenciaNormaEmpleado().getNormalaboral() != null) {
            parametros.getParametrosBusquedaNomina().getVigenciaNormaEmpleado().getNormalaboral().setNombre(valorCopia);
         }
         parametros.getParametrosBusquedaNomina().getVigenciaNormaEmpleado().setNormalaboral(null);
         if (!valorConfirmar.isEmpty()) {
            if (lovNormasLaborales == null) {
               requerirLOV(cualParametro);
            }

            if (lovNormasLaborales != null) {
               for (int i = 0; i < lovNormasLaborales.size(); i++) {
                  if (lovNormasLaborales.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                     indiceUnicoElemento = i;
                     coincidencias++;
                  }
               }
               if (coincidencias == 1) {
                  parametros.getParametrosBusquedaNomina().getVigenciaNormaEmpleado().setNormalaboral(lovNormasLaborales.get(indiceUnicoElemento));
               } else {
                  RequestContext.getCurrentInstance().update("formularioDialogos:NormaLaboralNormaLaboralDialogo");
                  RequestContext.getCurrentInstance().execute("PF('NormaLaboralNormaLaboralDialogo').show()");
               }
            } else {
               RequestContext.getCurrentInstance().execute("PF('errorListaDialogo').show()");
            }
         }
      } else if (cualParametro.equals("JORNADALABORAL")) {
         if (parametros.getParametrosBusquedaNomina().getVigenciaJornada().getJornadatrabajo() != null) {
            parametros.getParametrosBusquedaNomina().getVigenciaJornada().getJornadatrabajo().setDescripcion(valorCopia);
         }
         parametros.getParametrosBusquedaNomina().getVigenciaJornada().setJornadatrabajo(null);
         if (!valorConfirmar.isEmpty()) {
            if (lovJornadasLaborales == null) {
               requerirLOV(cualParametro);
            }

            if (lovJornadasLaborales != null) {
               for (int i = 0; i < lovJornadasLaborales.size(); i++) {
                  if (lovJornadasLaborales.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                     indiceUnicoElemento = i;
                     coincidencias++;
                  }
               }
               if (coincidencias == 1) {
                  parametros.getParametrosBusquedaNomina().getVigenciaJornada().setJornadatrabajo(lovJornadasLaborales.get(indiceUnicoElemento));
               } else {
                  RequestContext.getCurrentInstance().update("formularioDialogos:JornadaJornadaLaboralDialogo");
                  RequestContext.getCurrentInstance().execute("PF('JornadaJornadaLaboralDialogo').show()");
               }
            } else {
               RequestContext.getCurrentInstance().execute("PF('errorListaDialogo').show()");
            }
         }
      } else if (cualParametro.equals("MOTIVORETIRO")) {
         if (parametros.getParametrosBusquedaNomina().getMotivosRetiros() != null) {
            parametros.getParametrosBusquedaNomina().getMotivosRetiros().setNombre(valorCopia);
         }
         parametros.getParametrosBusquedaNomina().setMotivosRetiros(null);
         if (!valorConfirmar.isEmpty()) {
            if (lovMotivosRetiros == null) {
               requerirLOV(cualParametro);
            }

            if (lovMotivosRetiros != null) {
               for (int i = 0; i < lovMotivosRetiros.size(); i++) {
                  if (lovMotivosRetiros.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                     indiceUnicoElemento = i;
                     coincidencias++;
                  }
               }
               if (coincidencias == 1) {
                  parametros.getParametrosBusquedaNomina().setMotivosRetiros(lovMotivosRetiros.get(indiceUnicoElemento));
               } else {
                  RequestContext.getCurrentInstance().update("formularioDialogos:MotivoFechaRetiroDialogo");
                  RequestContext.getCurrentInstance().execute("PF('MotivoFechaRetiroDialogo').show()");
               }
            } else {
               RequestContext.getCurrentInstance().execute("PF('errorListaDialogo').show()");
            }
         }
      } else if (cualParametro.equals("CIUDADNACIMIENDO")) {
         if (parametros.getParametrosBusquedaPersonal().getEmpleado().getPersona().getCiudadnacimiento() != null) {
            parametros.getParametrosBusquedaPersonal().getEmpleado().getPersona().getCiudadnacimiento().setNombre(valorCopia);
         }
         parametros.getParametrosBusquedaPersonal().getEmpleado().getPersona().setCiudadnacimiento(null);
         if (!valorConfirmar.isEmpty()) {
            if (lovCiudades == null) {
               requerirLOV("CIUDAD");
            }

            if (lovCiudades != null) {
               for (int i = 0; i < lovCiudades.size(); i++) {
                  if (lovCiudades.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                     indiceUnicoElemento = i;
                     coincidencias++;
                  }
               }
               if (coincidencias == 1) {
                  parametros.getParametrosBusquedaPersonal().getEmpleado().getPersona().setCiudadnacimiento(lovCiudades.get(indiceUnicoElemento));
               } else {
                  RequestContext.getCurrentInstance().update("formularioDialogos:CiudadNacimientoDatosPersonalesDialogo");
                  RequestContext.getCurrentInstance().execute("PF('CiudadNacimientoDatosPersonalesDialogo').show()");
               }
            } else {
               RequestContext.getCurrentInstance().execute("PF('errorListaDialogo').show()");
            }
         }
      } else if (cualParametro.equals("CIUDADDOCUMENTO")) {
         if (parametros.getParametrosBusquedaPersonal().getEmpleado().getPersona().getCiudaddocumento() != null) {
            parametros.getParametrosBusquedaPersonal().getEmpleado().getPersona().getCiudaddocumento().setNombre(valorCopia);
         }
         parametros.getParametrosBusquedaPersonal().getEmpleado().getPersona().setCiudaddocumento(null);
         if (!valorConfirmar.isEmpty()) {
            if (lovCiudades == null) {
               requerirLOV("CIUDAD");
            }

            if (lovCiudades != null) {
               for (int i = 0; i < lovCiudades.size(); i++) {
                  if (lovCiudades.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                     indiceUnicoElemento = i;
                     coincidencias++;
                  }
               }
               if (coincidencias == 1) {
                  parametros.getParametrosBusquedaPersonal().getEmpleado().getPersona().setCiudaddocumento(lovCiudades.get(indiceUnicoElemento));
               } else {
                  RequestContext.getCurrentInstance().update("formularioDialogos:CiudadDocumentoDatosPersonalesDialogo");
                  RequestContext.getCurrentInstance().execute("PF('CiudadDocumentoDatosPersonalesDialogo').show()");
               }
            } else {
               RequestContext.getCurrentInstance().execute("PF('errorListaDialogo').show()");
            }
         }
      } else if (cualParametro.equals("ESTADOCIVIL")) {
         if (parametros.getParametrosBusquedaPersonal().getEstadoCivil() != null) {
            parametros.getParametrosBusquedaPersonal().getEstadoCivil().setDescripcion(valorCopia);
         }
         parametros.getParametrosBusquedaPersonal().setEstadoCivil(null);
         if (!valorConfirmar.isEmpty()) {
            if (lovEstadosCiviles == null) {
               requerirLOV(cualParametro);
            }

            if (lovEstadosCiviles != null) {
               for (int i = 0; i < lovEstadosCiviles.size(); i++) {
                  if (lovEstadosCiviles.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                     indiceUnicoElemento = i;
                     coincidencias++;
                  }
               }
               if (coincidencias == 1) {
                  parametros.getParametrosBusquedaPersonal().setEstadoCivil(lovEstadosCiviles.get(indiceUnicoElemento));
               } else {
                  RequestContext.getCurrentInstance().update("formularioDialogos:EstadoCivilEstadoCivilDialogo");
                  RequestContext.getCurrentInstance().execute("PF('EstadoCivilEstadoCivilDialogo').show()");
               }
            } else {
               RequestContext.getCurrentInstance().execute("PF('errorListaDialogo').show()");
            }
         }
      } else if (cualParametro.equals("IDIOMA")) {
         if (parametros.getParametrosBusquedaPersonal().getIdiomaPersona().getIdioma() != null) {
            parametros.getParametrosBusquedaPersonal().getIdiomaPersona().getIdioma().setNombre(valorCopia);
         }
         parametros.getParametrosBusquedaPersonal().getIdiomaPersona().setIdioma(null);
         if (!valorConfirmar.isEmpty()) {
            if (lovIdiomas == null) {
               requerirLOV(cualParametro);
            }

            if (lovIdiomas != null) {
               for (int i = 0; i < lovIdiomas.size(); i++) {
                  if (lovIdiomas.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                     indiceUnicoElemento = i;
                     coincidencias++;
                  }
               }
               if (coincidencias == 1) {
                  parametros.getParametrosBusquedaPersonal().getIdiomaPersona().setIdioma(lovIdiomas.get(indiceUnicoElemento));
               } else {
                  RequestContext.getCurrentInstance().update("formularioDialogos:IdiomaIdiomaDialogo");
                  RequestContext.getCurrentInstance().execute("PF('IdiomaIdiomaDialogo').show()");
               }
            } else {
               RequestContext.getCurrentInstance().execute("PF('errorListaDialogo').show()");
            }
         }
      } else if (cualParametro.equals("TIPOINDICADOR")) {
         if (parametros.getParametrosBusquedaPersonal().getVigenciaIndicador().getTipoindicador() != null) {
            parametros.getParametrosBusquedaPersonal().getVigenciaIndicador().getTipoindicador().setDescripcion(valorCopia);
         }
         parametros.getParametrosBusquedaPersonal().getVigenciaIndicador().setTipoindicador(null);
         if (!valorConfirmar.isEmpty()) {
            if (lovTiposIndicadores == null) {
               requerirLOV(cualParametro);
            }

            if (lovTiposIndicadores != null) {
               for (int i = 0; i < lovTiposIndicadores.size(); i++) {
                  if (lovTiposIndicadores.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                     indiceUnicoElemento = i;
                     coincidencias++;
                  }
               }
               if (coincidencias == 1) {
                  parametros.getParametrosBusquedaPersonal().getVigenciaIndicador().setTipoindicador(lovTiposIndicadores.get(indiceUnicoElemento));
               } else {
                  RequestContext.getCurrentInstance().update("formularioDialogos:TipoIndicadorCensoDialogo");
                  RequestContext.getCurrentInstance().execute("PF('TipoIndicadorCensoDialogo').show()");
               }
            } else {
               RequestContext.getCurrentInstance().execute("PF('errorListaDialogo').show()");
            }
         }
      } else if (cualParametro.equals("INDICADOR")) {
         if (parametros.getParametrosBusquedaPersonal().getVigenciaIndicador().getIndicador() != null) {
            parametros.getParametrosBusquedaPersonal().getVigenciaIndicador().getIndicador().setDescripcion(valorCopia);
         }
         parametros.getParametrosBusquedaPersonal().getVigenciaIndicador().setIndicador(null);
         if (!valorConfirmar.isEmpty()) {
            if (lovIndicadores == null) {
               requerirLOV(cualParametro);
            }

            if (lovIndicadores != null) {
               for (int i = 0; i < lovIndicadores.size(); i++) {
                  if (lovIndicadores.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                     indiceUnicoElemento = i;
                     coincidencias++;
                  }
               }
               if (coincidencias == 1) {
                  parametros.getParametrosBusquedaPersonal().getVigenciaIndicador().setIndicador(lovIndicadores.get(indiceUnicoElemento));
               } else {
                  RequestContext.getCurrentInstance().update("formularioDialogos:IndicadorCensoDialogo");
                  RequestContext.getCurrentInstance().execute("PF('IndicadorCensoDialogo').show()");
               }
            } else {
               RequestContext.getCurrentInstance().execute("PF('errorListaDialogo').show()");
            }
         }
      } else if (cualParametro.equals("PROFESION")) {
         if (parametros.getParametrosBusquedaPersonal().getVigenciaFormal().getProfesion() != null) {
            parametros.getParametrosBusquedaPersonal().getVigenciaFormal().getProfesion().setDescripcion(valorCopia);
         }
         parametros.getParametrosBusquedaPersonal().getVigenciaFormal().setProfesion(null);
         if (!valorConfirmar.isEmpty()) {
            if (lovProfesiones == null) {
               requerirLOV(cualParametro);
            }

            if (lovProfesiones != null) {
               for (int i = 0; i < lovProfesiones.size(); i++) {
                  if (lovProfesiones.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                     indiceUnicoElemento = i;
                     coincidencias++;
                  }
               }
               if (coincidencias == 1) {
                  parametros.getParametrosBusquedaPersonal().getVigenciaFormal().setProfesion(lovProfesiones.get(indiceUnicoElemento));
               } else {
                  RequestContext.getCurrentInstance().update("formularioDialogos:ProfesionEducacionFormalDialogo");
                  RequestContext.getCurrentInstance().execute("PF('ProfesionEducacionFormalDialogo').show()");
               }
            } else {
               RequestContext.getCurrentInstance().execute("PF('errorListaDialogo').show()");
            }
         }
      } else if (cualParametro.equals("INSTITUCIONF")) {
         if (parametros.getParametrosBusquedaPersonal().getVigenciaFormal().getInstitucion() != null) {
            parametros.getParametrosBusquedaPersonal().getVigenciaFormal().getInstitucion().setDescripcion(valorCopia);
         }
         parametros.getParametrosBusquedaPersonal().getVigenciaFormal().setInstitucion(null);
         if (!valorConfirmar.isEmpty()) {
            if (lovInstituciones == null) {
               requerirLOV("INSTITUCION");
            }

            if (lovInstituciones != null) {
               for (int i = 0; i < lovInstituciones.size(); i++) {
                  if (lovInstituciones.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                     indiceUnicoElemento = i;
                     coincidencias++;
                  }
               }
               if (coincidencias == 1) {
                  parametros.getParametrosBusquedaPersonal().getVigenciaFormal().setInstitucion(lovInstituciones.get(indiceUnicoElemento));
               } else {
                  RequestContext.getCurrentInstance().update("formularioDialogos:InstitucionEducacionFormalDialogo");
                  RequestContext.getCurrentInstance().execute("PF('InstitucionEducacionFormalDialogo').show()");
               }
            } else {
               RequestContext.getCurrentInstance().execute("PF('errorListaDialogo').show()");
            }
         }
      } else if (cualParametro.equals("INSTITUCIONNF")) {
         if (parametros.getParametrosBusquedaPersonal().getVigenciaNoFormal().getInstitucion() != null) {
            parametros.getParametrosBusquedaPersonal().getVigenciaNoFormal().getInstitucion().setDescripcion(valorCopia);
         }
         parametros.getParametrosBusquedaPersonal().getVigenciaNoFormal().setInstitucion(null);
         if (!valorConfirmar.isEmpty()) {
            if (lovInstituciones == null) {
               requerirLOV("INSTITUCION");
            }

            if (lovInstituciones != null) {
               for (int i = 0; i < lovInstituciones.size(); i++) {
                  if (lovInstituciones.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                     indiceUnicoElemento = i;
                     coincidencias++;
                  }
               }
               if (coincidencias == 1) {
                  parametros.getParametrosBusquedaPersonal().getVigenciaNoFormal().setInstitucion(lovInstituciones.get(indiceUnicoElemento));
               } else {
                  RequestContext.getCurrentInstance().update("formularioDialogos:InstitucionEducacionNoFormalDialogo");
                  RequestContext.getCurrentInstance().execute("PF('InstitucionEducacionNoFormalDialogo').show()");
               }
            } else {
               RequestContext.getCurrentInstance().execute("PF('errorListaDialogo').show()");
            }
         }
      } else if (cualParametro.equals("CURSO")) {
         if (parametros.getParametrosBusquedaPersonal().getVigenciaNoFormal().getCurso() != null) {
            parametros.getParametrosBusquedaPersonal().getVigenciaNoFormal().getCurso().setNombre(valorCopia);
         }
         parametros.getParametrosBusquedaPersonal().getVigenciaNoFormal().setCurso(null);
         if (!valorConfirmar.isEmpty()) {
            if (lovCursos == null) {
               requerirLOV(cualParametro);
            }

            if (lovCursos != null) {
               for (int i = 0; i < lovCursos.size(); i++) {
                  if (lovCursos.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                     indiceUnicoElemento = i;
                     coincidencias++;
                  }
               }
               if (coincidencias == 1) {
                  parametros.getParametrosBusquedaPersonal().getVigenciaNoFormal().setCurso(lovCursos.get(indiceUnicoElemento));
               } else {
                  RequestContext.getCurrentInstance().update("formularioDialogos:CursoEducacionNoFormalDialogo");
                  RequestContext.getCurrentInstance().execute("PF('CursoEducacionNoFormalDialogo').show()");
               }
            } else {
               RequestContext.getCurrentInstance().execute("PF('errorListaDialogo').show()");
            }
         }
      } else if (cualParametro.equals("SECTORECONOMICO")) {
         if (parametros.getParametrosBusquedaPersonal().getHvExperienciaLaboral().getSectoreconomico() != null) {
            parametros.getParametrosBusquedaPersonal().getHvExperienciaLaboral().getSectoreconomico().setDescripcion(valorCopia);
         }
         parametros.getParametrosBusquedaPersonal().getHvExperienciaLaboral().setSectoreconomico(null);
         if (!valorConfirmar.isEmpty()) {
            if (lovSectoresEconomicos == null) {
               requerirLOV(cualParametro);
            }

            if (lovSectoresEconomicos != null) {
               for (int i = 0; i < lovSectoresEconomicos.size(); i++) {
                  if (lovSectoresEconomicos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                     indiceUnicoElemento = i;
                     coincidencias++;
                  }
               }
               if (coincidencias == 1) {
                  parametros.getParametrosBusquedaPersonal().getHvExperienciaLaboral().setSectoreconomico(lovSectoresEconomicos.get(indiceUnicoElemento));
               } else {
                  RequestContext.getCurrentInstance().update("formularioDialogos:SectorEconomicoExperienciaLaboralDialogo");
                  RequestContext.getCurrentInstance().execute("PF('SectorEconomicoExperienciaLaboralDialogo').show()");
               }
            } else {
               RequestContext.getCurrentInstance().execute("PF('errorListaDialogo').show()");
            }
         }
      } else if (cualParametro.equals("PROYECTO")) {
         if (parametros.getParametrosBusquedaPersonal().getVigenciaProyecto().getProyecto() != null) {
            parametros.getParametrosBusquedaPersonal().getVigenciaProyecto().getProyecto().setNombreproyecto(valorCopia);
         }
         parametros.getParametrosBusquedaPersonal().getVigenciaProyecto().setProyecto(null);
         if (!valorConfirmar.isEmpty()) {
            if (lovProyectos == null) {
               requerirLOV(cualParametro);
            }

            if (lovProyectos != null) {
               for (int i = 0; i < lovProyectos.size(); i++) {
                  if (lovProyectos.get(i).getNombreproyecto().startsWith(valorConfirmar.toUpperCase())) {
                     indiceUnicoElemento = i;
                     coincidencias++;
                  }
               }
               if (coincidencias == 1) {
                  parametros.getParametrosBusquedaPersonal().getVigenciaProyecto().setProyecto(lovProyectos.get(indiceUnicoElemento));
               } else {
                  RequestContext.getCurrentInstance().update("formularioDialogos:ProyectoProyectoDialogo");
                  RequestContext.getCurrentInstance().execute("PF('ProyectoProyectoDialogo').show()");
               }
            } else {
               RequestContext.getCurrentInstance().execute("PF('errorListaDialogo').show()");
            }
         }
      } else if (cualParametro.equals("ROL")) {
         if (parametros.getParametrosBusquedaPersonal().getVigenciaProyecto().getPryRol() != null) {
            parametros.getParametrosBusquedaPersonal().getVigenciaProyecto().getPryRol().setDescripcion(valorCopia);
         }
         parametros.getParametrosBusquedaPersonal().getVigenciaProyecto().setPryRol(null);
         if (!valorConfirmar.isEmpty()) {
            if (lovRoles == null) {
               requerirLOV(cualParametro);
            }

            if (lovRoles != null) {
               for (int i = 0; i < lovRoles.size(); i++) {
                  if (lovRoles.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                     indiceUnicoElemento = i;
                     coincidencias++;
                  }
               }
               if (coincidencias == 1) {
                  parametros.getParametrosBusquedaPersonal().getVigenciaProyecto().setPryRol(lovRoles.get(indiceUnicoElemento));
               } else {
                  RequestContext.getCurrentInstance().update("formularioDialogos:PryRolProyectoDialogo");
                  RequestContext.getCurrentInstance().execute("PF('PryRolProyectoDialogo').show()");
               }
            } else {
               RequestContext.getCurrentInstance().execute("PF('errorListaDialogo').show()");
            }
         }
      } else if (cualParametro.equals("CARGOPERSONAL")) {
         if (parametros.getParametrosBusquedaPersonal().getCargo() != null) {
            parametros.getParametrosBusquedaPersonal().getCargo().setNombre(valorCopia);
         }
         parametros.getParametrosBusquedaPersonal().setCargo(null);
         if (!valorConfirmar.isEmpty()) {
            if (lovCargos == null) {
               requerirLOV("CARGO");
            }

            if (lovCargos != null) {
               for (int i = 0; i < lovCargos.size(); i++) {
                  if (lovCargos.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                     indiceUnicoElemento = i;
                     coincidencias++;
                  }
               }
               if (coincidencias == 1) {
                  parametros.getParametrosBusquedaPersonal().setCargo(lovCargos.get(indiceUnicoElemento));
               } else {
                  RequestContext.getCurrentInstance().update("formularioDialogos:CargoCargoPostularseDialogo");
                  RequestContext.getCurrentInstance().execute("PF('CargoCargoPostularseDialogo').show()");
               }
            } else {
               RequestContext.getCurrentInstance().execute("PF('errorListaDialogo').show()");
            }
         }
      } else if (cualParametro.equals("MOTIVORETIROPERSONAL")) {
         if (parametros.getParametrosBusquedaPersonal().getHvExperienciaLaboral().getMotivoretiro() != null) {
            parametros.getParametrosBusquedaPersonal().getHvExperienciaLaboral().getMotivoretiro().setNombre(valorCopia);
         }
         parametros.getParametrosBusquedaNomina().setMotivosRetiros(null);
         if (!valorConfirmar.isEmpty()) {
            if (lovMotivosRetiros == null) {
               requerirLOV("MOTIVORETIRO");
            }

            if (lovMotivosRetiros != null) {
               for (int i = 0; i < lovMotivosRetiros.size(); i++) {
                  if (lovMotivosRetiros.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                     indiceUnicoElemento = i;
                     coincidencias++;
                  }
               }
               if (coincidencias == 1) {
                  parametros.getParametrosBusquedaNomina().setMotivosRetiros(lovMotivosRetiros.get(indiceUnicoElemento));
               } else {
                  RequestContext.getCurrentInstance().update("formularioDialogos:MotivoRetiroExperienciaLaboralDialogo");
                  RequestContext.getCurrentInstance().execute("PF('MotivoRetiroExperienciaLaboralDialogo').show()");
               }
            } else {
               RequestContext.getCurrentInstance().execute("PF('errorListaDialogo').show()");
            }
         }
      }
   }

   //METODOS LISTA DE VALORES
   public void requerirLOV(String tipoLov) {
      if (vTipoBusqueda.equals("Nomina")) {
         if (tipoLov.equals("CARGO") && lovCargos == null) {
            lovCargos = administrarBusquedaAvanzada.lovCargos();
            if (lovCargos != null) {
//               modificarInfoR(lovCargos.size());
               RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroC");
            }
         } else if (tipoLov.equals("ESTRUCTURA") && lovEstructuras == null) {
            lovEstructuras = administrarBusquedaAvanzada.lovEstructuras();
            if (lovEstructuras != null) {
//               modificarInfoR(lovEstructuras.size());
               RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroE");
            }
         } else if (tipoLov.equals("JEFE") && lovJefe == null) {
            lovJefe = administrarBusquedaAvanzada.lovJefe();
            if (lovJefe != null) {
//               modificarInfoR(lovJefe.size());
               RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroJ");
            }
         } else if (tipoLov.equals("MOTIVOCARGO") && lovMotivosCargos == null) {
            lovMotivosCargos = administrarBusquedaAvanzada.lovMotivosCargos();
            if (lovMotivosCargos != null) {
//               modificarInfoR(lovMotivosCargos.size());
               RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroMC");
            }
         } else if (tipoLov.equals("PAPEL") && lovPapeles == null) {
            lovPapeles = administrarBusquedaAvanzada.lovPapeles();
            if (lovPapeles != null) {
//               modificarInfoR(lovPapeles.size());
               RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroPC");
            }
         } else if (tipoLov.equals("MOTIVOLOCALIZACION") && lovMotivosLocalizaciones == null) {
            lovMotivosLocalizaciones = administrarBusquedaAvanzada.lovMotivosLocalizaciones();
            if (lovMotivosLocalizaciones != null) {
//               modificarInfoR(lovMotivosLocalizaciones.size());
               RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroMCC");
            }
         } else if (tipoLov.equals("TIPOSUELDO") && lovTiposSueldos == null) {
            lovTiposSueldos = administrarBusquedaAvanzada.lovTiposSueldos();
            if (lovTiposSueldos != null) {
//               modificarInfoR(lovTiposSueldos.size());
               RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroTSS");
            }
         } else if (tipoLov.equals("MOTIVOSUELDO") && lovMotivosSueldos == null) {
            lovMotivosSueldos = administrarBusquedaAvanzada.lovMotivosSueldos();
            if (lovMotivosSueldos != null) {
//               modificarInfoR(lovMotivosSueldos.size());
               RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroMSS");
            }
         } else if (tipoLov.equals("TIPOCONTRATO") && lovTiposContratos == null) {
            lovTiposContratos = administrarBusquedaAvanzada.lovTiposContratos();
            if (lovTiposContratos != null) {
//               modificarInfoR(lovTiposContratos.size());
               RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroTCFC");
            }
         } else if (tipoLov.equals("MOTIVOCONTRATO") && lovMotivosContratos == null) {
            lovMotivosContratos = administrarBusquedaAvanzada.lovMotivosContratos();
            if (lovMotivosContratos != null) {
//               modificarInfoR(lovMotivosContratos.size());
               RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroMFC");
            }
         } else if (tipoLov.equals("TIPOTRABAJADOR") && lovTiposTrabajadores == null) {
            lovTiposTrabajadores = administrarBusquedaAvanzada.lovTiposTrabajadores();
            if (lovTiposTrabajadores != null) {
//               modificarInfoR(lovTiposTrabajadores.size());
               RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroTTTT");
            }
         } else if (tipoLov.equals("REFORMALABORAL") && lovReformasLaborales == null) {
            lovReformasLaborales = administrarBusquedaAvanzada.lovReformasLaborales();
            if (lovReformasLaborales != null) {
//               modificarInfoR(lovReformasLaborales.size());
               RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroRLTS");
            }
         } else if (tipoLov.equals("CONTRATO") && lovContratos == null) {
            lovContratos = administrarBusquedaAvanzada.lovContratos();
            if (lovContratos != null) {
//               modificarInfoR(lovContratos.size());
               RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroLLL");
            }
         } else if (tipoLov.equals("UBICACION") && lovUbicaciones == null) {
            lovUbicaciones = administrarBusquedaAvanzada.lovUbicaciones();
            if (lovUbicaciones != null) {
//               modificarInfoR(lovUbicaciones.size());
               RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroUUG");
            }
         } else if (tipoLov.equals("TERCEROSUCURSAL") && lovTercerosSucursales == null) {
            lovTercerosSucursales = administrarBusquedaAvanzada.lovTercerosSucursales();
            if (lovTercerosSucursales != null) {
//               modificarInfoR(lovTercerosSucursales.size());
               RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroTSA");
            }
         } else if (tipoLov.equals("TIPOENTIDAD") && lovTiposEntidades == null) {
            lovTiposEntidades = administrarBusquedaAvanzada.lovTiposEntidades();
            if (lovTiposEntidades != null) {
//               modificarInfoR(lovTiposEntidades.size());
               RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroTEA");
            }
         } else if (tipoLov.equals("ESTADOAFILIACION") && lovEstadosAfiliaciones == null) {
            lovEstadosAfiliaciones = administrarBusquedaAvanzada.lovEstadosAfiliaciones();
            if (lovEstadosAfiliaciones != null) {
//               modificarInfoR(lovEstadosAfiliaciones.size());
               RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroEA");
            }
         } else if (tipoLov.equals("FORMAPAGO") && lovFormasPagos == null) {
            lovFormasPagos = administrarBusquedaAvanzada.lovFormasPagos();
            if (lovFormasPagos != null) {
//               modificarInfoR(lovFormasPagos.size());
               RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroPFP");
            }
         } else if (tipoLov.equals("SUCURSAL") && lovSucursales == null) {
            lovSucursales = administrarBusquedaAvanzada.lovSucursales();
            if (lovSucursales != null) {
//               modificarInfoR(lovSucursales.size());
               RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroSFP");
            }
         } else if (tipoLov.equals("MOTIVOMVR") && lovMotivosMvrs == null) {
            lovMotivosMvrs = administrarBusquedaAvanzada.lovMotivosMvrs();
            if (lovMotivosMvrs != null) {
//               modificarInfoR(lovMotivosMvrs.size());
               RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroMMVR");
            }
         } else if (tipoLov.equals("NORMALABORAL") && lovNormasLaborales == null) {
            lovNormasLaborales = administrarBusquedaAvanzada.lovNormasLaborales();
            if (lovNormasLaborales != null) {
//               modificarInfoR(lovNormasLaborales.size());
               RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroNLNL");
            }
         } else if (tipoLov.equals("JORNADALABORAL") && lovJornadasLaborales == null) {
            lovJornadasLaborales = administrarBusquedaAvanzada.lovJornadasLaborales();
            if (lovJornadasLaborales != null) {
//               modificarInfoR(lovJornadasLaborales.size());
               RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroJLJL");
            }
         } else if (tipoLov.equals("MOTIVORETIRO") && lovMotivosRetiros == null) {
            lovMotivosRetiros = administrarBusquedaAvanzada.lovMotivosRetiros();
            if (lovMotivosRetiros != null) {
//               modificarInfoR(lovMotivosRetiros.size());
               RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroMRFR");
            }
         }
      } else if (vTipoBusqueda.equals("Personal")) {
         if (tipoLov.equals("CIUDAD") && lovCiudades == null) {
            lovCiudades = administrarBusquedaAvanzada.lovCiudades();
            if (lovCiudades != null) {
//               modificarInfoR(lovCiudades.size());
               RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroCDDP");
            }
         } else if (tipoLov.equals("ESTADOCIVIL") && lovEstructuras == null) {
            lovEstadosCiviles = administrarBusquedaAvanzada.lovEstadosCiviles();
            if (lovEstadosCiviles != null) {
//               modificarInfoR(lovEstadosCiviles.size());
               RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroECEC");
            }
         } else if (tipoLov.equals("IDIOMA") && lovIdiomas == null) {
            lovIdiomas = administrarBusquedaAvanzada.lovIdiomas();
            if (lovIdiomas != null) {
//               modificarInfoR(lovIdiomas.size());
               RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroII");
            }
         } else if (tipoLov.equals("TIPOINDICADOR") && lovTiposIndicadores == null) {
            lovTiposIndicadores = administrarBusquedaAvanzada.lovTiposIndicadores();
            if (lovTiposIndicadores != null) {
//               modificarInfoR(lovTiposIndicadores.size());
               RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroTIC");
            }
         } else if (tipoLov.equals("INDICADOR") && lovIndicadores == null) {
            lovIndicadores = administrarBusquedaAvanzada.lovIndicadores();
            if (lovIndicadores != null) {
//               modificarInfoR(lovIndicadores.size());
               RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroIC");
            }
         } else if (tipoLov.equals("PROFESION") && lovProfesiones == null) {
            lovProfesiones = administrarBusquedaAvanzada.lovProfesiones();
            if (lovProfesiones != null) {
//               modificarInfoR(lovProfesiones.size());
               RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroPEF");
            }
         } else if (tipoLov.equals("INSTITUCION") && lovInstituciones == null) {
            lovInstituciones = administrarBusquedaAvanzada.lovInstitucioneses();
            if (lovInstituciones != null) {
//               modificarInfoR(lovInstituciones.size());
               RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroIEF");
            }
         } else if (tipoLov.equals("CURSO") && lovCursos == null) {
            lovCursos = administrarBusquedaAvanzada.lovCursos();
            if (lovCursos != null) {
//               modificarInfoR(lovCursos.size());
               RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroCENF");
            }
         } else if (tipoLov.equals("SECTORECONOMICO") && lovSectoresEconomicos == null) {
            lovSectoresEconomicos = administrarBusquedaAvanzada.lovSectoresEconomicos();
            System.out.println("SECTOR ECONOMICO: " + lovSectoresEconomicos);
            if (lovSectoresEconomicos != null) {
//               modificarInfoR(lovSectoresEconomicos.size());
               RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroSEEL");
            }
         } else if (tipoLov.equals("PROYECTO") && lovProyectos == null) {
            lovProyectos = administrarBusquedaAvanzada.lovProyectos();
            if (lovProyectos != null) {
//               modificarInfoR(lovProyectos.size());
               RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroPP");
            }
         } else if (tipoLov.equals("ROL") && lovRoles == null) {
            lovRoles = administrarBusquedaAvanzada.lovRoles();
            if (lovRoles != null) {
//               modificarInfoR(lovRoles.size());
               RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroPRYP");
            }
         } else if (tipoLov.equals("MOTIVORETIRO") && lovMotivosRetiros == null) {
            lovMotivosRetiros = administrarBusquedaAvanzada.lovMotivosRetiros();
            if (lovMotivosRetiros != null) {
//               modificarInfoR(lovMotivosRetiros.size());
               RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroMREL");
            }
         } else if (tipoLov.equals("CARGO") && lovCargos == null) {
            lovCargos = administrarBusquedaAvanzada.lovCargos();
            if (lovCargos != null) {
//               modificarInfoR(lovCargos.size());
               RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroCCP");
            }
         }
      }
      //RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroECC");
      //RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroCNDP");
      //RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroIENF");
   }

   public void requerirLovColumnasBusqueda() {
      lovColumnasEscenarios = administrarBusquedaAvanzada.buscarColumnasEscenarios();
//      modificarInfoR(lovColumnasEscenarios.size()
      RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroCB");
   }

   //METODO ACEPTAR - PARAMETROS NOMIMA
   public void actualizarParametroNomina(String tipoLov) {
      aceptar = true;
      if (tipoLov.equals("CARGO")) {
         parametros.getParametrosBusquedaNomina().getVigenciaCargo().setCargo(cargoSeleccionado);
         cargoSeleccionado = null;
         filtroLovCargos = null;
      } else if (tipoLov.equals("ESTRUCTURA")) {
         parametros.getParametrosBusquedaNomina().getVigenciaCargo().setEstructura(estructuraSeleccionada);
         estructuraSeleccionada = null;
         filtroLovEstructuras = null;
      } else if (tipoLov.equals("JEFE")) {
         parametros.getParametrosBusquedaNomina().getVigenciaCargo().setEmpleadojefe(jefeSeleccionado);
         jefeSeleccionado = null;
         filtroLovJefe = null;
      } else if (tipoLov.equals("MOTIVOCARGO")) {
         parametros.getParametrosBusquedaNomina().getVigenciaCargo().setMotivocambiocargo(motivoCargoSeleccionado);
         motivoCargoSeleccionado = null;
         filtroLovMotivosCargos = null;
      } else if (tipoLov.equals("PAPEL")) {
         parametros.getParametrosBusquedaNomina().getVigenciaCargo().setPapel(papelSeleccionado);
         papelSeleccionado = null;
         filtroLovPapeles = null;
      } else if (tipoLov.equals("LOCALIZACION")) {
         parametros.getParametrosBusquedaNomina().getVigenciaLocalizacion().setLocalizacion(estructuraSeleccionada);
         estructuraSeleccionada = null;
         filtroLovEstructuras = null;
      } else if (tipoLov.equals("MOTIVOLOCALIZACION")) {
         parametros.getParametrosBusquedaNomina().getVigenciaLocalizacion().setMotivo(motivoLocalizacionSeleccionado);
         motivoLocalizacionSeleccionado = null;
         filtroLovMotivosLocalizaciones = null;
      } else if (tipoLov.equals("TIPOSUELDO")) {
         parametros.getParametrosBusquedaNomina().getVigenciaSueldo().setTiposueldo(tipoSueldoSeleccionado);
         tipoSueldoSeleccionado = null;
         filtroLovTiposSueldos = null;
      } else if (tipoLov.equals("MOTIVOSUELDO")) {
         parametros.getParametrosBusquedaNomina().getVigenciaSueldo().setMotivocambiosueldo(motivoSueldoSeleccionado);
         motivoSueldoSeleccionado = null;
         filtroLovMotivosSueldos = null;
      } else if (tipoLov.equals("TIPOCONTRATO")) {
         parametros.getParametrosBusquedaNomina().getVigenciaTipoContrato().setTipocontrato(tipoContratoSeleccionado);
         tipoContratoSeleccionado = null;
         filtroLovTiposContratos = null;
      } else if (tipoLov.equals("MOTIVOCONTRATO")) {
         parametros.getParametrosBusquedaNomina().getVigenciaTipoContrato().setMotivocontrato(motivoContratoSeleccionado);
         motivoContratoSeleccionado = null;
         filtroLovMotivosContratos = null;
      } else if (tipoLov.equals("TIPOTRABAJADOR")) {
         parametros.getParametrosBusquedaNomina().getVigenciaTipoTrabajador().setTipotrabajador(tipoTrabajadorSeleccionado);
         tipoTrabajadorSeleccionado = null;
         filtroLovTiposTrabajadores = null;
      } else if (tipoLov.equals("REFORMALABORAL")) {
         parametros.getParametrosBusquedaNomina().getVigenciaReformasLaboral().setReformalaboral(reformaLaboralSeleccionada);
         reformaLaboralSeleccionada = null;
         filtroLovReformasLaborales = null;
      } else if (tipoLov.equals("CONTRATO")) {
         parametros.getParametrosBusquedaNomina().getVigenciaContrato().setContrato(contratoSeleccionado);
         contratoSeleccionado = null;
         filtroLovContratos = null;
      } else if (tipoLov.equals("UBICACION")) {
         parametros.getParametrosBusquedaNomina().getVigenciaUbicacion().setUbicacion(ubicacionSeleccionado);
         ubicacionSeleccionado = null;
         filtroLovUbicaciones = null;
      } else if (tipoLov.equals("TERCEROSUCURSAL")) {
         parametros.getParametrosBusquedaNomina().getVigenciaAfiliacion().setTercerosucursal(terceroSucursalSeleccionado);
         terceroSucursalSeleccionado = null;
         filtroLovTercerosSucursales = null;
      } else if (tipoLov.equals("TIPOENTIDAD")) {
         parametros.getParametrosBusquedaNomina().getVigenciaAfiliacion().setTipoentidad(tipoEntidadSeleccionado);
         tipoEntidadSeleccionado = null;
         filtroLovTiposEntidades = null;
      } else if (tipoLov.equals("ESTADOAFILIACION")) {
         parametros.getParametrosBusquedaNomina().getVigenciaAfiliacion().setEstadoafiliacion(estadoAfiliacionSeleccionado);
         estadoAfiliacionSeleccionado = null;
         filtroLovEstadosAfiliaciones = null;
      } else if (tipoLov.equals("FORMAPAGO")) {
         parametros.getParametrosBusquedaNomina().getVigenciaFormaPago().setFormapago(formaPagoSeleccionado);
         formaPagoSeleccionado = null;
         filtroLovFormasPagos = null;
      } else if (tipoLov.equals("SUCURSAL")) {
         parametros.getParametrosBusquedaNomina().getVigenciaFormaPago().setSucursal(sucursalSeleccionado);
         sucursalSeleccionado = null;
         filtroLovSucursales = null;
      } else if (tipoLov.equals("MOTIVOMVR")) {
         parametros.getParametrosBusquedaNomina().getMvrs().setMotivo(motivoMvrSeleccionado);
         motivoMvrSeleccionado = null;
         filtroLovMotivosMvrs = null;
      } else if (tipoLov.equals("NORMALABORAL")) {
         parametros.getParametrosBusquedaNomina().getVigenciaNormaEmpleado().setNormalaboral(normaLaboralSeleccionado);
         normaLaboralSeleccionado = null;
         filtroLovNormasLaborales = null;
      } else if (tipoLov.equals("JORNADALABORAL")) {
         parametros.getParametrosBusquedaNomina().getVigenciaJornada().setJornadatrabajo(jornadaLaboralSeleccionado);
         jornadaLaboralSeleccionado = null;
         filtroLovJornadasLaborales = null;
      } else if (tipoLov.equals("MOTIVORETIRO")) {
         parametros.getParametrosBusquedaNomina().setMotivosRetiros(motivoRetiroSeleccionado);
         motivoRetiroSeleccionado = null;
         filtroLovMotivosRetiros = null;
      }
   }

   //METODO CANCELAR - PARAMETROS NOMIMA
   public void cancelarParametroNomina(String tipoLov) {
      aceptar = true;
      if (tipoLov.equals("CARGO")) {
         cargoSeleccionado = null;
         filtroLovCargos = null;
      } else if (tipoLov.equals("ESTRUCTURA") || tipoLov.equals("LOCALIZACION")) {
         estructuraSeleccionada = null;
         filtroLovEstructuras = null;
      } else if (tipoLov.equals("JEFE")) {
         jefeSeleccionado = null;
         filtroLovJefe = null;
      } else if (tipoLov.equals("MOTIVOCARGO")) {
         motivoCargoSeleccionado = null;
         filtroLovMotivosCargos = null;
      } else if (tipoLov.equals("PAPEL")) {
         papelSeleccionado = null;
         filtroLovPapeles = null;
      } else if (tipoLov.equals("MOTIVOLOCALIZACION")) {
         motivoLocalizacionSeleccionado = null;
         filtroLovMotivosLocalizaciones = null;
      } else if (tipoLov.equals("TIPOSUELDO")) {
         tipoSueldoSeleccionado = null;
         filtroLovTiposSueldos = null;
      } else if (tipoLov.equals("MOTIVOSUELDO")) {
         motivoSueldoSeleccionado = null;
         filtroLovMotivosSueldos = null;
      } else if (tipoLov.equals("TIPOCONTRATO")) {
         tipoContratoSeleccionado = null;
         filtroLovTiposContratos = null;
      } else if (tipoLov.equals("MOTIVOCONTRATO")) {
         motivoContratoSeleccionado = null;
         filtroLovMotivosContratos = null;
      } else if (tipoLov.equals("TIPOTRABAJADOR")) {
         tipoTrabajadorSeleccionado = null;
         filtroLovTiposTrabajadores = null;
      } else if (tipoLov.equals("REFORMALABORAL")) {
         reformaLaboralSeleccionada = null;
         filtroLovReformasLaborales = null;
      } else if (tipoLov.equals("CONTRATO")) {
         contratoSeleccionado = null;
         filtroLovContratos = null;
      } else if (tipoLov.equals("UBICACION")) {
         ubicacionSeleccionado = null;
         filtroLovUbicaciones = null;
      } else if (tipoLov.equals("TERCEROSUCURSAL")) {
         terceroSucursalSeleccionado = null;
         filtroLovTercerosSucursales = null;
      } else if (tipoLov.equals("TIPOENTIDAD")) {
         tipoEntidadSeleccionado = null;
         filtroLovTiposEntidades = null;
      } else if (tipoLov.equals("ESTADOAFILIACION")) {
         estadoAfiliacionSeleccionado = null;
         filtroLovEstadosAfiliaciones = null;
      } else if (tipoLov.equals("FORMAPAGO")) {
         formaPagoSeleccionado = null;
         filtroLovFormasPagos = null;
      } else if (tipoLov.equals("SUCURSAL")) {
         sucursalSeleccionado = null;
         filtroLovSucursales = null;
      } else if (tipoLov.equals("MOTIVOMVR")) {
         motivoMvrSeleccionado = null;
         filtroLovMotivosMvrs = null;
      } else if (tipoLov.equals("NORMALABORAL")) {
         normaLaboralSeleccionado = null;
         filtroLovNormasLaborales = null;
      } else if (tipoLov.equals("JORNADALABORAL")) {
         jornadaLaboralSeleccionado = null;
         filtroLovJornadasLaborales = null;
      } else if (tipoLov.equals("MOTIVORETIRO")) {
         motivoRetiroSeleccionado = null;
         filtroLovMotivosRetiros = null;
      }
   }

   //METODO ACEPTAR - PARAMETROS PERSONAL
   public void actualizarParametroPersonal(String tipoLov) {
      aceptar = true;
      if (tipoLov.equals("CIUDADNACIMIENTO")) {
         parametros.getParametrosBusquedaPersonal().getEmpleado().getPersona().setCiudadnacimiento(ciudadSeleccionado);
         ciudadSeleccionado = null;
         filtroLovCiudades = null;
      } else if (tipoLov.equals("CIUDADDOCUMENTO")) {
         parametros.getParametrosBusquedaPersonal().getEmpleado().getPersona().setCiudaddocumento(ciudadSeleccionado);
         ciudadSeleccionado = null;
         filtroLovCiudades = null;
      } else if (tipoLov.equals("ESTADOCIVIL")) {
         parametros.getParametrosBusquedaPersonal().setEstadoCivil(estadoCivilSeleccionado);
         estadoCivilSeleccionado = null;
         filtroLovEstadosCiviles = null;
      } else if (tipoLov.equals("IDIOMA")) {
         parametros.getParametrosBusquedaPersonal().getIdiomaPersona().setIdioma(idiomaSeleccionado);
         idiomaSeleccionado = null;
         filtroLovIdiomas = null;
      } else if (tipoLov.equals("TIPOINDICADOR")) {
         parametros.getParametrosBusquedaPersonal().getVigenciaIndicador().setTipoindicador(tipoIndicadorSeleccionado);
         tipoIndicadorSeleccionado = null;
         filtroLovTiposIndicadores = null;
      } else if (tipoLov.equals("INDICADOR")) {
         parametros.getParametrosBusquedaPersonal().getVigenciaIndicador().setIndicador(indicadorSeleccionado);
         indicadorSeleccionado = null;
         filtroLovIndicadores = null;
      } else if (tipoLov.equals("PROFESION")) {
         parametros.getParametrosBusquedaPersonal().getVigenciaFormal().setProfesion(profesionSeleccionado);
         profesionSeleccionado = null;
         filtroLovProfesiones = null;
      } else if (tipoLov.equals("INSTITUCIONF")) {
         parametros.getParametrosBusquedaPersonal().getVigenciaFormal().setInstitucion(institucionSeleccionado);
         institucionSeleccionado = null;
         filtroLovInstituciones = null;
      } else if (tipoLov.equals("INSTITUCIONNF")) {
         parametros.getParametrosBusquedaPersonal().getVigenciaNoFormal().setInstitucion(institucionSeleccionado);
         institucionSeleccionado = null;
         filtroLovInstituciones = null;
      } else if (tipoLov.equals("CURSO")) {
         parametros.getParametrosBusquedaPersonal().getVigenciaNoFormal().setCurso(cursoSeleccionado);
         cursoSeleccionado = null;
         filtroLovCursos = null;
      } else if (tipoLov.equals("SECTORECONOMICO")) {
         parametros.getParametrosBusquedaPersonal().getHvExperienciaLaboral().setSectoreconomico(sectorEconomicoSeleccionado);
         sectorEconomicoSeleccionado = null;
         filtroLovSectoresEconomicos = null;
      } else if (tipoLov.equals("MOTIVORETIRO")) {
         parametros.getParametrosBusquedaPersonal().getHvExperienciaLaboral().setMotivoretiro(motivoRetiroSeleccionado);
         motivoRetiroSeleccionado = null;
         filtroLovMotivosRetiros = null;
      } else if (tipoLov.equals("PROYECTO")) {
         parametros.getParametrosBusquedaPersonal().getVigenciaProyecto().setProyecto(proyectoSeleccionado);
         proyectoSeleccionado = null;
         filtroLovProyectos = null;
      } else if (tipoLov.equals("ROL")) {
         parametros.getParametrosBusquedaPersonal().getVigenciaProyecto().setPryRol(rolSeleccionado);
         rolSeleccionado = null;
         filtroLovRoles = null;
      } else if (tipoLov.equals("CARGO")) {
         parametros.getParametrosBusquedaPersonal().setCargo(cargoSeleccionado);
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
         ciudadSeleccionado = null;
         filtroLovCiudades = null;
      } else if (tipoLov.equals("CIUDADDOCUMENTO")) {
         ciudadSeleccionado = null;
         filtroLovCiudades = null;
      } else if (tipoLov.equals("ESTADOCIVIL")) {
         estadoCivilSeleccionado = null;
         filtroLovEstadosCiviles = null;
      } else if (tipoLov.equals("IDIOMA")) {
         idiomaSeleccionado = null;
         filtroLovIdiomas = null;
      } else if (tipoLov.equals("TIPOINDICADOR")) {
         tipoIndicadorSeleccionado = null;
         filtroLovTiposIndicadores = null;
      } else if (tipoLov.equals("INDICADOR")) {
         indicadorSeleccionado = null;
         filtroLovIndicadores = null;
      } else if (tipoLov.equals("PROFESION")) {
         profesionSeleccionado = null;
         filtroLovProfesiones = null;
      } else if (tipoLov.equals("INSTITUCION")) {
         institucionSeleccionado = null;
         filtroLovInstituciones = null;
      } else if (tipoLov.equals("CURSO")) {
         cursoSeleccionado = null;
         filtroLovCursos = null;
      } else if (tipoLov.equals("SECTORECONOMICO")) {
         sectorEconomicoSeleccionado = null;
         filtroLovSectoresEconomicos = null;
      } else if (tipoLov.equals("MOTIVORETIRO")) {
         motivoRetiroSeleccionado = null;
         filtroLovMotivosRetiros = null;
      } else if (tipoLov.equals("PROYECTO")) {
         proyectoSeleccionado = null;
         filtroLovProyectos = null;
      } else if (tipoLov.equals("ROL")) {
         parametros.getParametrosBusquedaPersonal().getVigenciaProyecto().setPryRol(rolSeleccionado);
         rolSeleccionado = null;
         filtroLovRoles = null;
      } else if (tipoLov.equals("CARGO")) {
         cargoSeleccionado = null;
         filtroLovCargos = null;
      } else if (tipoLov.equals("COLUMNASBUSQUEDA")) {
         columnasEsSeleccionadas = null;
         filtradoColumnasEscenarios = null;
      }
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
      if (!query.isEmpty()) {
         queryEmpleado = queryEmpleado + query;
         System.out.println("SUPER QUERY: " + queryEmpleado);
         listaCodigosEmpleado = administrarBusquedaAvanzada.ejecutarQueryBusquedaAvanzadaPorModulosCodigo(queryEmpleado);
         String columnas = convertirListaAString();
         listaResultadoBusqueda = administrarBusquedaAvanzada.obtenerQVWEmpleadosCorteParaEmpleadoCodigo(listaCodigosEmpleado, columnas);
         FacesContext context = FacesContext.getCurrentInstance();
         ControladorColumnasDinamicas controladorColumnasDinamicas = (ControladorColumnasDinamicas) context.getApplication().evaluateExpressionGet(context, "#{controladorColumnasDinamicas}", ControladorColumnasDinamicas.class);
         controladorColumnasDinamicas.updateColumns(columnas);
      }
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
         if (this.parametros.getParametrosBusquedaNomina().getVigenciaCargo().getCargo() != null) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("CARGO", "CARGO", this.parametros.getParametrosBusquedaNomina().getVigenciaCargo().getCargo().getSecuencia().toString());
            listaParametrosQueryModulos.add(parametro);
         }
         if (this.parametros.getParametrosBusquedaNomina().getVigenciaCargo().getEstructura() != null) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("CARGO", "ESTRUCTURA", this.parametros.getParametrosBusquedaNomina().getVigenciaCargo().getEstructura().getSecuencia().toString());
            ParametrosQueryBusquedaAvanzada parametro2 = new ParametrosQueryBusquedaAvanzada("CARGO", "CENTROCOSTO", this.parametros.getParametrosBusquedaNomina().getVigenciaCargo().getEstructura().getCentrocosto().getSecuencia().toString());
            listaParametrosQueryModulos.add(parametro);
            listaParametrosQueryModulos.add(parametro2);
         }
         if (this.parametros.getParametrosBusquedaNomina().getVigenciaCargo().getEmpleadojefe() != null && this.parametros.getParametrosBusquedaNomina().getVigenciaCargo().getEmpleadojefe().getSecuencia() != null) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("CARGO", "EMPLEADOJEFE", this.parametros.getParametrosBusquedaNomina().getVigenciaCargo().getEmpleadojefe().getSecuencia().toString());
            listaParametrosQueryModulos.add(parametro);
         }
         if (this.parametros.getParametrosBusquedaNomina().getVigenciaCargo().getMotivocambiocargo() != null) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("CARGO", "MOTIVOCAMBIOCARGO", this.parametros.getParametrosBusquedaNomina().getVigenciaCargo().getMotivocambiocargo().getSecuencia().toString());
            listaParametrosQueryModulos.add(parametro);
         }
         if (this.parametros.getParametrosBusquedaNomina().getVigenciaCargo().getPapel() != null) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("CARGO", "PAPEL", this.parametros.getParametrosBusquedaNomina().getVigenciaCargo().getPapel().getSecuencia().toString());
            listaParametrosQueryModulos.add(parametro);
         }
      }
   }

   public void cargueParametrosModuloCentroCosto() {
      FacesContext c = FacesContext.getCurrentInstance();
      TabView t = (TabView) c.getViewRoot().findComponent("form:opcionesCentroCosto");
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
         if (this.parametros.getParametrosBusquedaNomina().getVigenciaLocalizacion().getLocalizacion() != null) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("CENTROCOSTO", "LOCALIZACION", this.parametros.getParametrosBusquedaNomina().getVigenciaLocalizacion().getLocalizacion().getSecuencia().toString());
            listaParametrosQueryModulos.add(parametro);
         }
         if (this.parametros.getParametrosBusquedaNomina().getVigenciaLocalizacion().getMotivo() != null) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("CENTROCOSTO", "MOTIVOLOCALIZACION", this.parametros.getParametrosBusquedaNomina().getVigenciaLocalizacion().getMotivo().getSecuencia().toString());
            listaParametrosQueryModulos.add(parametro);
         }
      }
   }

   public void cargueParametrosModuloSueldo() {
      FacesContext c = FacesContext.getCurrentInstance();
      TabView t = (TabView) c.getViewRoot().findComponent("form:opcionesSueldo");
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
         if (this.parametros.getParametrosBusquedaNomina().getVigenciaSueldo().getTiposueldo() != null) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("SUELDO", "TIPOSUELDO", this.parametros.getParametrosBusquedaNomina().getVigenciaSueldo().getTiposueldo().getSecuencia().toString());
            listaParametrosQueryModulos.add(parametro);
         }
         if (this.parametros.getParametrosBusquedaNomina().getVigenciaSueldo().getMotivocambiosueldo() != null) {
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
         if (this.parametros.getParametrosBusquedaNomina().getVigenciaTipoContrato().getTipocontrato() != null) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("FECHACONTRATO", "TIPOCONTRATO", this.parametros.getParametrosBusquedaNomina().getVigenciaTipoContrato().getTipocontrato().getSecuencia().toString());
            listaParametrosQueryModulos.add(parametro);
         }
         if (this.parametros.getParametrosBusquedaNomina().getVigenciaTipoContrato().getMotivocontrato() != null) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("FECHACONTRATO", "MOTIVOCONTRATO", this.parametros.getParametrosBusquedaNomina().getVigenciaTipoContrato().getMotivocontrato().getSecuencia().toString());
            listaParametrosQueryModulos.add(parametro);
         }
      }
   }

   public void cargueParametrosModuloTipoTrabajador() {
      FacesContext c = FacesContext.getCurrentInstance();
      TabView t = (TabView) c.getViewRoot().findComponent("form:opcionesTipoTrabajador");
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
         if (this.parametros.getParametrosBusquedaNomina().getVigenciaTipoTrabajador().getTipotrabajador() != null) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("TIPOTRABAJADOR", "TIPOTRABAJADOR", this.parametros.getParametrosBusquedaNomina().getVigenciaTipoTrabajador().getTipotrabajador().getSecuencia().toString());
            listaParametrosQueryModulos.add(parametro);
         }
      }
   }

   public void cargueParametrosModuloTipoSalario() {
      FacesContext c = FacesContext.getCurrentInstance();
      TabView t = (TabView) c.getViewRoot().findComponent("form:opcionesTipoSalario");
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
         if (this.parametros.getParametrosBusquedaNomina().getVigenciaReformasLaboral().getReformalaboral() != null) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("TIPOSALARIO", "REFORMA", this.parametros.getParametrosBusquedaNomina().getVigenciaReformasLaboral().getReformalaboral().getSecuencia().toString());
            listaParametrosQueryModulos.add(parametro);
         }
      }
   }

   public void cargueParametrosModuloNormaLaboral() {
      FacesContext c = FacesContext.getCurrentInstance();
      TabView t = (TabView) c.getViewRoot().findComponent("form:opcionesNormaLaboral");
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
         if (this.parametros.getParametrosBusquedaNomina().getVigenciaNormaEmpleado().getNormalaboral() != null) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("NORMALABORAL", "NORMA", this.parametros.getParametrosBusquedaNomina().getVigenciaNormaEmpleado().getNormalaboral().getSecuencia().toString());
            listaParametrosQueryModulos.add(parametro);
         }
      }
   }

   public void cargueParametrosModuloLegislacionLaboral() {
      FacesContext c = FacesContext.getCurrentInstance();
      TabView t = (TabView) c.getViewRoot().findComponent("form:opcionesLegislacionLaboral");
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
         if (this.parametros.getParametrosBusquedaNomina().getVigenciaContrato().getContrato() != null) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("LEGISLACIONLABORAL", "CONTRATO", this.parametros.getParametrosBusquedaNomina().getVigenciaContrato().getContrato().getSecuencia().toString());
            listaParametrosQueryModulos.add(parametro);
         }
      }
   }

   public void cargueParametrosModuloUbicacionGeografica() {
      FacesContext c = FacesContext.getCurrentInstance();
      TabView t = (TabView) c.getViewRoot().findComponent("form:opcionesUbicacion");
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
         if (this.parametros.getParametrosBusquedaNomina().getVigenciaUbicacion().getUbicacion() != null) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("UBICACION", "UBICACION", this.parametros.getParametrosBusquedaNomina().getVigenciaUbicacion().getUbicacion().getSecuencia().toString());
            listaParametrosQueryModulos.add(parametro);
         }
      }
   }

   public void cargueParametrosModuloAfiliaciones() {
      FacesContext c = FacesContext.getCurrentInstance();
      TabView t = (TabView) c.getViewRoot().findComponent("form:opcionesAfiliacion");
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
         if (this.parametros.getParametrosBusquedaNomina().getVigenciaAfiliacion().getTercerosucursal() != null) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("AFILIACIONES", "TERCERO", this.parametros.getParametrosBusquedaNomina().getVigenciaAfiliacion().getTercerosucursal().getSecuencia().toString());
            listaParametrosQueryModulos.add(parametro);
         }
         if (this.parametros.getParametrosBusquedaNomina().getVigenciaAfiliacion().getTipoentidad() != null) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("AFILIACIONES", "TIPOENTIDAD", this.parametros.getParametrosBusquedaNomina().getVigenciaAfiliacion().getTipoentidad().getSecuencia().toString());
            listaParametrosQueryModulos.add(parametro);
         }
         if (this.parametros.getParametrosBusquedaNomina().getVigenciaAfiliacion().getEstadoafiliacion() != null) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("AFILIACIONES", "ESTADO", this.parametros.getParametrosBusquedaNomina().getVigenciaAfiliacion().getEstadoafiliacion().getSecuencia().toString());
            listaParametrosQueryModulos.add(parametro);
         }
      }
   }

   public void cargueParametrosModuloFormaPago() {
      FacesContext c = FacesContext.getCurrentInstance();
      TabView t = (TabView) c.getViewRoot().findComponent("form:opcionesFormaPago");
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
         if (this.parametros.getParametrosBusquedaNomina().getVigenciaFormaPago().getFormapago() != null) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("FORMAPAGO", "FORMAPAGO", this.parametros.getParametrosBusquedaNomina().getVigenciaFormaPago().getFormapago().getSecuencia().toString());
            listaParametrosQueryModulos.add(parametro);
         }
         if (this.parametros.getParametrosBusquedaNomina().getVigenciaFormaPago().getSucursal() != null && this.parametros.getParametrosBusquedaNomina().getVigenciaFormaPago().getSucursal().getSecuencia() != null) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("FORMAPAGO", "SUCURSAL", this.parametros.getParametrosBusquedaNomina().getVigenciaFormaPago().getSucursal().getSecuencia().toString());
            listaParametrosQueryModulos.add(parametro);
         }

      }
   }

   public void cargueParametrosModuloMVR() {
      FacesContext c = FacesContext.getCurrentInstance();
      TabView t = (TabView) c.getViewRoot().findComponent("form:opcionesMvrs");
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
         if (this.parametros.getParametrosBusquedaNomina().getMvrs().getMotivo() != null) {
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
         if (this.parametros.getParametrosBusquedaNomina().getMotivosRetiros() != null) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("FECHARETIRO", "MOTIVO", this.parametros.getParametrosBusquedaNomina().getMotivosRetiros().getSecuencia().toString());
            listaParametrosQueryModulos.add(parametro);
         }
      }
   }

   public void cargueParametrosModuloJornadaLaboral() {
      FacesContext c = FacesContext.getCurrentInstance();
      TabView t = (TabView) c.getViewRoot().findComponent("form:opcionesJornadaLaboral");
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
      if (t.getActiveIndex() == 1) {

         ParametrosQueryBusquedaAvanzada parametroInicial = new ParametrosQueryBusquedaAvanzada("DATOSPERSONALES", "NN", "NN");
         listaParametrosQueryModulos.add(parametroInicial);

         if (this.parametros.getParametrosBusquedaPersonal().getEmpleado().getPersona().getNumerodocumento() != null) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("DATOSPERSONALES", "NUMERODOCUMENTO", this.parametros.getParametrosBusquedaPersonal().getEmpleado().getPersona().getNumerodocumento().toString());
            listaParametrosQueryModulos.add(parametro);
         }
         if (this.parametros.getParametrosBusquedaPersonal().getEmpleado().getPersona().getCiudaddocumento() != null) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("DATOSPERSONALES", "CIUDADDOCUMENTO", this.parametros.getParametrosBusquedaPersonal().getEmpleado().getPersona().getCiudaddocumento().getSecuencia().toString());
            listaParametrosQueryModulos.add(parametro);
         }
         if (this.parametros.getParametrosBusquedaPersonal().getEmpleado().getPersona().getSexo() != null && !this.parametros.getParametrosBusquedaPersonal().getEmpleado().getPersona().getSexo().isEmpty()) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("DATOSPERSONALES", "SEXO", this.parametros.getParametrosBusquedaPersonal().getEmpleado().getPersona().getSexo().toString());
            listaParametrosQueryModulos.add(parametro);
            System.out.println("sexo : " + this.parametros.getParametrosBusquedaPersonal().getEmpleado().getPersona().getSexo());
         }
         if (this.parametros.getParametrosBusquedaPersonal().getEmpleado().getPersona().getCiudadnacimiento() != null) {
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
         if (this.parametros.getParametrosBusquedaPersonal().getEstadoCivil() != null) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("ESTADOCIVIL", "ESTADOCIVIL", this.parametros.getParametrosBusquedaPersonal().getEstadoCivil().getSecuencia().toString());
            listaParametrosQueryModulos.add(parametro);
         }
      }
   }

   public void cargueParametrosModuloIdioma() {
      FacesContext c = FacesContext.getCurrentInstance();
      TabView t = (TabView) c.getViewRoot().findComponent("form:opcionesIdioma");
      if (t.getActiveIndex() == 1) {

         ParametrosQueryBusquedaAvanzada parametroInicial = new ParametrosQueryBusquedaAvanzada("IDIOMA", "NN", "NN");
         listaParametrosQueryModulos.add(parametroInicial);

         if (this.parametros.getParametrosBusquedaPersonal().getIdiomaPersona().getIdioma() != null) {
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
         if (this.parametros.getParametrosBusquedaPersonal().getVigenciaIndicador().getTipoindicador() != null) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("CENSOS", "TIPOINDICADOR", this.parametros.getParametrosBusquedaPersonal().getVigenciaIndicador().getTipoindicador().getSecuencia().toString());
            listaParametrosQueryModulos.add(parametro);
         }
         if (this.parametros.getParametrosBusquedaPersonal().getVigenciaIndicador().getIndicador() != null) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("CENSOS", "INDICADOR", this.parametros.getParametrosBusquedaPersonal().getVigenciaIndicador().getIndicador().getSecuencia().toString());
            listaParametrosQueryModulos.add(parametro);
         }
      }
   }

   public void cargueParametrosModuloEducacionFormal() {
      FacesContext c = FacesContext.getCurrentInstance();
      TabView t = (TabView) c.getViewRoot().findComponent("form:opcionesEducacionFormal");
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
         if (this.parametros.getParametrosBusquedaPersonal().getVigenciaFormal().getInstitucion() != null) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("EDUCACIONFORMAL", "INSTITUCION", this.parametros.getParametrosBusquedaPersonal().getVigenciaFormal().getInstitucion().getSecuencia().toString());
            listaParametrosQueryModulos.add(parametro);
         }
         if (this.parametros.getParametrosBusquedaPersonal().getVigenciaFormal().getProfesion() != null) {
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
         if (this.parametros.getParametrosBusquedaPersonal().getVigenciaNoFormal().getInstitucion() != null) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("EDUCACIONNOFORMAL", "INSTITUCION", this.parametros.getParametrosBusquedaPersonal().getVigenciaNoFormal().getInstitucion().getSecuencia().toString());
            listaParametrosQueryModulos.add(parametro);
         }
         if (this.parametros.getParametrosBusquedaPersonal().getVigenciaNoFormal().getCurso() != null) {
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
      if (t.getActiveIndex() == 1) {

         ParametrosQueryBusquedaAvanzada parametroInicial = new ParametrosQueryBusquedaAvanzada("CARGOPOSTULARSE", "NN", "NN");
         listaParametrosQueryModulos.add(parametroInicial);

         if (this.parametros.getParametrosBusquedaPersonal().getCargo() != null) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("CARGOPOSTULARSE", "CARGO", this.parametros.getParametrosBusquedaPersonal().getCargo().getSecuencia().toString());
            listaParametrosQueryModulos.add(parametro);
         }
      }
   }

   public void cargueParametrosModuloProyecto() {
      FacesContext c = FacesContext.getCurrentInstance();
      TabView t = (TabView) c.getViewRoot().findComponent("form:opcionesProyecto");
      if (t.getActiveIndex() == 1) {

         ParametrosQueryBusquedaAvanzada parametroInicial = new ParametrosQueryBusquedaAvanzada("PROYECTO", "NN", "NN");
         listaParametrosQueryModulos.add(parametroInicial);

         if (this.parametros.getParametrosBusquedaPersonal().getVigenciaProyecto().getProyecto() != null) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("PROYECTO", "PROYECTO", this.parametros.getParametrosBusquedaPersonal().getVigenciaProyecto().getProyecto().getSecuencia().toString());
            listaParametrosQueryModulos.add(parametro);
         }
         if (this.parametros.getParametrosBusquedaPersonal().getVigenciaProyecto().getPryRol() != null) {
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
         if (this.parametros.getParametrosBusquedaPersonal().getHvExperienciaLaboral().getSectoreconomico() != null) {
            ParametrosQueryBusquedaAvanzada parametro = new ParametrosQueryBusquedaAvanzada("EXPERIENCIALABORAL", "SECTORECONOMICO", this.parametros.getParametrosBusquedaPersonal().getHvExperienciaLaboral().getSectoreconomico().getSecuencia().toString());
            listaParametrosQueryModulos.add(parametro);
         }
         if (this.parametros.getParametrosBusquedaPersonal().getHvExperienciaLaboral().getMotivoretiro() != null) {
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
      String columnas = "SECUENCIA, CODIGOEMPLEADO, NOMBRE, PRIMERAPELLIDO, SEGUNDOAPELLIDO";
      if (columnasEsSeleccionadas != null && !columnasEsSeleccionadas.isEmpty()) {
         for (ColumnasEscenarios columna : columnasEsSeleccionadas) {
            columnas += "," + columna.getDescripcion();
         }
      }
      return columnas;
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

   public void setSeleccionColumnasEscenarios(List<ColumnasEscenarios> seleccionColumnasEscenarios) {
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

//   public String getInfoRegistro() {
//      return infoRegistro;
//   }
//
//   public void setInfoRegistros(String infoRegistro) {
//      this.infoRegistro = infoRegistro;
//   }
   
   public boolean isAceptar() {
      return aceptar;
   }

   public void setAceptar(boolean aceptar) {
      this.aceptar = aceptar;
   }
}
