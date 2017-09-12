package Administrar;

import Entidades.Cargos;
import Entidades.Ciudades;
import Entidades.Comprobantes;
import Entidades.Contratos;
import Entidades.CortesProcesos;
import Entidades.Direcciones;
import Entidades.Empleados;
import Entidades.Empresas;
import Entidades.EstadosCiviles;
import Entidades.Estructuras;
import Entidades.JornadasLaborales;
import Entidades.MetodosPagos;
import Entidades.MotivosCambiosCargos;
import Entidades.MotivosCambiosSueldos;
import Entidades.MotivosContratos;
import Entidades.MotivosLocalizaciones;
import Entidades.NormasLaborales;
import Entidades.Papeles;
import Entidades.Periodicidades;
import Entidades.Personas;
import Entidades.Procesos;
import Entidades.ReformasLaborales;
import Entidades.Sets;
import Entidades.Sucursales;
import Entidades.Telefonos;
import Entidades.TercerosSucursales;
import Entidades.TiposContratos;
import Entidades.TiposDocumentos;
import Entidades.TiposEntidades;
import Entidades.TiposSueldos;
import Entidades.TiposTelefonos;
import Entidades.TiposTrabajadores;
import Entidades.UbicacionesGeograficas;
import Entidades.Unidades;
import Entidades.VWValidaBancos;
import Entidades.VigenciasAfiliaciones;
import Entidades.VigenciasCargos;
import Entidades.VigenciasContratos;
import Entidades.VigenciasEstadosCiviles;
import Entidades.VigenciasFormasPagos;
import Entidades.VigenciasJornadas;
import Entidades.VigenciasLocalizaciones;
import Entidades.VigenciasNormasEmpleados;
import Entidades.VigenciasReformasLaborales;
import Entidades.VigenciasSueldos;
import Entidades.VigenciasTiposContratos;
import Entidades.VigenciasTiposTrabajadores;
import Entidades.VigenciasUbicaciones;
import InterfaceAdministrar.AdministrarPersonaIndividualInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaCargosInterface;
import InterfacePersistencia.PersistenciaCiudadesInterface;
import InterfacePersistencia.PersistenciaComprobantesInterface;
import InterfacePersistencia.PersistenciaCortesProcesosInterface;
import InterfacePersistencia.PersistenciaDetallesEmpresasInterface;
import InterfacePersistencia.PersistenciaDireccionesInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaEmpresasInterface;
import InterfacePersistencia.PersistenciaEstadosCivilesInterface;
import InterfacePersistencia.PersistenciaEstructurasInterface;
import InterfacePersistencia.PersistenciaGeneralesInterface;
import InterfacePersistencia.PersistenciaJornadasLaboralesInterface;
import InterfacePersistencia.PersistenciaMetodosPagosInterface;
import InterfacePersistencia.PersistenciaMotivosCambiosCargosInterface;
import InterfacePersistencia.PersistenciaMotivosCambiosSueldosInterface;
import InterfacePersistencia.PersistenciaMotivosContratosInterface;
import InterfacePersistencia.PersistenciaMotivosLocalizacionesInterface;
import InterfacePersistencia.PersistenciaPapelesInterface;
import InterfacePersistencia.PersistenciaPeriodicidadesInterface;
import InterfacePersistencia.PersistenciaPersonasInterface;
import InterfacePersistencia.PersistenciaPlantillasTTInterface;
import InterfacePersistencia.PersistenciaProcesosInterface;
import InterfacePersistencia.PersistenciaReformasLaboralesInterface;
import InterfacePersistencia.PersistenciaSetsInterface;
import InterfacePersistencia.PersistenciaSucursalesInterface;
import InterfacePersistencia.PersistenciaTelefonosInterface;
import InterfacePersistencia.PersistenciaTercerosInterface;
import InterfacePersistencia.PersistenciaTercerosSucursalesInterface;
import InterfacePersistencia.PersistenciaTiposDocumentosInterface;
import InterfacePersistencia.PersistenciaTiposEntidadesInterface;
import InterfacePersistencia.PersistenciaTiposTelefonosInterface;
import InterfacePersistencia.PersistenciaTiposTrabajadoresInterface;
import InterfacePersistencia.PersistenciaUbicacionesGeograficasInterface;
import InterfacePersistencia.PersistenciaUnidadesInterface;
import InterfacePersistencia.PersistenciaVWActualesFechasInterface;
import InterfacePersistencia.PersistenciaVWValidaBancosInterface;
import InterfacePersistencia.PersistenciaVigenciasAfiliacionesInterface;
import InterfacePersistencia.PersistenciaVigenciasCargosInterface;
import InterfacePersistencia.PersistenciaVigenciasContratosInterface;
import InterfacePersistencia.PersistenciaVigenciasEstadosCivilesInterface;
import InterfacePersistencia.PersistenciaVigenciasFormasPagosInterface;
import InterfacePersistencia.PersistenciaVigenciasJornadasInterface;
import InterfacePersistencia.PersistenciaVigenciasLocalizacionesInterface;
import InterfacePersistencia.PersistenciaVigenciasNormasEmpleadosInterface;
import InterfacePersistencia.PersistenciaVigenciasReformasLaboralesInterface;
import InterfacePersistencia.PersistenciaVigenciasSueldosInterface;
import InterfacePersistencia.PersistenciaVigenciasTiposContratosInterface;
import InterfacePersistencia.PersistenciaVigenciasTiposTrabajadoresInterface;
import InterfacePersistencia.PersistenciaVigenciasUbicacionesInterface;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

/**
 *
 * @author Administrador
 */
@Local
@Stateful
public class AdministrarPersonaIndividual implements AdministrarPersonaIndividualInterface {

   private static Logger log = Logger.getLogger(AdministrarPersonaIndividual.class);

   @EJB
   PersistenciaEmpresasInterface persistenciaEmpresas;
   @EJB
   PersistenciaTiposDocumentosInterface persistenciaTiposDocumentos;
   @EJB
   PersistenciaCiudadesInterface persistenciaCiudades;
   @EJB
   PersistenciaCargosInterface persistenciaCargos;
   @EJB
   PersistenciaMotivosCambiosCargosInterface persistenciaMotivosCambiosCargos;
   @EJB
   PersistenciaTiposEntidadesInterface persistenciaTiposEntidades;
   @EJB
   PersistenciaEstructurasInterface persistenciaEstructuras;
   @EJB
   PersistenciaMotivosLocalizacionesInterface persistenciaMotivosLocalizaciones;
   @EJB
   PersistenciaUbicacionesGeograficasInterface persistenciaUbicacionesGeograficas;
   @EJB
   PersistenciaJornadasLaboralesInterface persistenciaJornadasLaborales;
   @EJB
   PersistenciaMotivosContratosInterface persistenciaMotivosContratos;
//    @EJB
//    PersistenciaTiposContratosInterface persistenciaTiposContratos;
   @EJB
   PersistenciaTiposTrabajadoresInterface persistenciaTiposTrabajadores;
   @EJB
   PersistenciaReformasLaboralesInterface persistenciaReformasLaborales;
//    @EJB
//    PersistenciaNormasLaboralesInterface persistenciaNormasLaborales;
//    @EJB
//    PersistenciaContratosInterface persistenciaContratos;
   @EJB
   PersistenciaMotivosCambiosSueldosInterface persistenciaMotivosCambiosSueldos;
//    @EJB
//    PersistenciaTiposSueldosInterface persistenciaTiposSueldos;
   @EJB
   PersistenciaUnidadesInterface persistenciaUnidades;
   @EJB
   PersistenciaPeriodicidadesInterface persistenciaPeriodicidades;
   @EJB
   PersistenciaSucursalesInterface persistenciaSucursales;
   @EJB
   PersistenciaMetodosPagosInterface persistenciaMetodosPagos;
   @EJB
   PersistenciaTercerosSucursalesInterface persistenciaTercerosSucursales;
   @EJB
   PersistenciaEstadosCivilesInterface persistenciaEstadosCiviles;
   @EJB
   PersistenciaTiposTelefonosInterface persistenciaTiposTelefonos;
   @EJB
   PersistenciaEmpleadoInterface persistenciaEmpleado;
   @EJB
   PersistenciaPersonasInterface persistenciaPersonas;
   @EJB
   PersistenciaPapelesInterface persistenciaPapeles;
   @EJB
   PersistenciaTercerosInterface persistenciaTerceros;
   @EJB
   PersistenciaGeneralesInterface persistenciaGenerales;
   @EJB
   PersistenciaVWValidaBancosInterface persistenciaVWValidaBancos;
   @EJB
   PersistenciaVigenciasCargosInterface persistenciaVigenciasCargos;
   @EJB
   PersistenciaVigenciasLocalizacionesInterface persistenciaVigenciasLocalizaciones;
   @EJB
   PersistenciaVigenciasTiposTrabajadoresInterface persistenciaVigenciasTiposTrabajadores;
   @EJB
   PersistenciaVigenciasReformasLaboralesInterface persistenciaVigenciasReformasLaborales;
   @EJB
   PersistenciaVigenciasSueldosInterface persistenciaVigenciasSueldos;
   @EJB
   PersistenciaVigenciasTiposContratosInterface persistenciaVigenciasTiposContratos;
   @EJB
   PersistenciaVigenciasNormasEmpleadosInterface persistenciaVigenciasNormasEmpleados;
   @EJB
   PersistenciaVigenciasContratosInterface persistenciaVigenciasContratos;
   @EJB
   PersistenciaVigenciasUbicacionesInterface persistenciaVigenciasUbicaciones;
   @EJB
   PersistenciaVigenciasJornadasInterface persistenciaVigenciasJornadas;
   @EJB
   PersistenciaVigenciasFormasPagosInterface persistenciaVigenciasFormasPagos;
   @EJB
   PersistenciaDireccionesInterface persistenciaDirecciones;
   @EJB
   PersistenciaTelefonosInterface persistenciaTelefonos;
   @EJB
   PersistenciaSetsInterface persistenciaSets;
   @EJB
   PersistenciaProcesosInterface persistenciaProcesos;
   @EJB
   PersistenciaComprobantesInterface persistenciaComprobantes;
   @EJB
   PersistenciaCortesProcesosInterface persistenciaCortesProcesos;
   @EJB
   PersistenciaVigenciasEstadosCivilesInterface persistenciaVigenciasEstadosCiviles;
   @EJB
   PersistenciaVigenciasAfiliacionesInterface persistenciaVigenciasAfiliaciones;
   @EJB
   PersistenciaDetallesEmpresasInterface persistenciadetallesEmpresas;
   @EJB
   PersistenciaVWActualesFechasInterface persistenciaVWActualesFechas;
   @EJB
   AdministrarSesionesInterface administrarSesiones;
   private EntityManagerFactory emf;
   private EntityManager em; private String idSesionBck;

   private EntityManager getEm() {
      try {
         if (this.emf != null) { if (this.em != null) {
            if (this.em.isOpen()) {
               this.em.close();
            }
         }
         } else {
            this.emf = administrarSesiones.obtenerConexionSesionEMF(idSesionBck);
         }
         this.em = emf.createEntityManager();
      } catch (Exception e) {
         log.fatal(this.getClass().getSimpleName() + " getEm() ERROR : " + e);
      }
      return this.em;
   }
   @EJB
   PersistenciaPlantillasTTInterface PersistenciaPlantillasTT;

   @Override
   public void obtenerConexion(String idSesion) { idSesionBck = idSesion;
      try {
         emf = administrarSesiones.obtenerConexionSesionEMF(idSesion);
      } catch (Exception e) {
         log.fatal(this.getClass().getSimpleName() + " obtenerConexion ERROR: " + e);
      }
   }

   @Override
   public List<Empresas> lovEmpresas() {
      try {
         return persistenciaEmpresas.buscarEmpresas(getEm());
      } catch (Exception e) {
         log.warn("Error lovEmpresas AdministrarPersonaIndividualPersona : " + e.toString());
         return null;
      }
   }

   @Override
   public Empresas obtenerEmpresa(BigInteger secEmpresa) {
      Empresas empresa = null;
      try {
         return persistenciaEmpresas.buscarEmpresasSecuencia(getEm(), secEmpresa);
      } catch (Exception e) {
         e.printStackTrace();
         return empresa;
      }
   }

   @Override
   public List<TiposDocumentos> lovTiposDocumentos() {
      try {
         return persistenciaTiposDocumentos.consultarTiposDocumentos(getEm());
      } catch (Exception e) {
         log.warn("Error lovTiposDocumentos Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<Ciudades> lovCiudades() {
      try {
         return persistenciaCiudades.consultarCiudades(getEm());
      } catch (Exception e) {
         log.warn("Error lovCiudades Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<Cargos> lovCargos() {
      try {
         return persistenciaCargos.consultarCargos(getEm());
      } catch (Exception e) {
         log.warn("Error lovCargos Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<Cargos> lovCargosXEmpresa(BigInteger secEmpresa) {
      try {
         return persistenciaCargos.buscarCargosPorSecuenciaEmpresa(getEm(), secEmpresa);
      } catch (Exception e) {
         log.warn("Error lovCargos Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<MotivosCambiosCargos> lovMotivosCambiosCargos() {
      try {
         return persistenciaMotivosCambiosCargos.buscarMotivosCambiosCargos(getEm());
      } catch (Exception e) {
         log.warn("Error lovMotivosCambiosCargos Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<Estructuras> lovEstructurasModCargos(BigInteger secEmpresa, Date fechaIngreso) {
      try {
         return persistenciaEstructuras.buscarEstructurasPorEmpresaFechaIngreso(getEm(), secEmpresa, fechaIngreso);
      } catch (Exception e) {
         log.warn("Error lovEstructurasModCargos Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<Estructuras> lovEstructurasModCentroCosto(BigInteger secEmpresa) {
      try {
         return persistenciaEstructuras.buscarEstructurasPorEmpresa(getEm(), secEmpresa);
      } catch (Exception e) {
         log.warn("Error lovEstructurasModCentroCosto Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<MotivosLocalizaciones> lovMotivosLocalizaciones() {
      try {
         return persistenciaMotivosLocalizaciones.buscarMotivosLocalizaciones(getEm());
      } catch (Exception e) {
         log.warn("Error lovMotivosLocalizaciones Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<UbicacionesGeograficas> lovUbicacionesGeograficas(BigInteger secuencia) {
      try {
         return persistenciaUbicacionesGeograficas.consultarUbicacionesGeograficasPorEmpresa(getEm(), secuencia);
      } catch (Exception e) {
         log.warn("Error lovUbicacionesGeograficas Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<JornadasLaborales> lovJornadasLaborales() {
      try {
         return persistenciaJornadasLaborales.buscarJornadasLaborales(getEm());
      } catch (Exception e) {
         log.warn("Error lovJornadasLaborales Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<MotivosContratos> lovMotivosContratos() {
      try {
         return persistenciaMotivosContratos.buscarMotivosContratos(getEm());
      } catch (Exception e) {
         log.warn("Error lovMotivosContratos Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<TiposTrabajadores> lovTiposTrabajadores() {
      try {
         return persistenciaTiposTrabajadores.buscarTiposTrabajadores(getEm());
      } catch (Exception e) {
         log.error("Error AdministrarP..I.. lovTiposTrabajadores() : " + e.toString());
         return null;
      }
   }

   @Override
   public List<TiposSueldos> lovTiposSueldosValidos(BigInteger secTT) {
      try {
         return PersistenciaPlantillasTT.consultarTiposSueldosValidos(getEm(), secTT);
      } catch (Exception e) {
         log.error("Error lovTiposSueldosValidos : " + e.toString());
         return null;
      }
   }

   @Override
   public List<Contratos> lovContratosValidos(BigInteger secTT) {
      try {
         return PersistenciaPlantillasTT.consultarContratosValidos(getEm(), secTT);
      } catch (Exception e) {
         log.error("Error lovContratosValidos : " + e.toString());
         return null;
      }
   }

   @Override
   public List<NormasLaborales> lovNormasLaboralesValidos(BigInteger secTT) {
      try {
         return PersistenciaPlantillasTT.consultarNormasLaboralesValidas(getEm(), secTT);
      } catch (Exception e) {
         log.error("Error lovNormasLaboralesValidos : " + e.toString());
         return null;
      }
   }

   @Override
   public List<ReformasLaborales> lovReformasLaboralesValidos(BigInteger secTT) {
      try {
         return PersistenciaPlantillasTT.consultarReformasLaboralesValidas(getEm(), secTT);
      } catch (Exception e) {
         log.error("Error lovReformasLaboralesValidos : " + e.toString());
         return null;
      }
   }

   @Override
   public List<TiposContratos> lovTiposContratosValidos(BigInteger secTT) {
      try {
         return PersistenciaPlantillasTT.consultarTiposContratosValidos(getEm(), secTT);
      } catch (Exception e) {
         log.error("Error lovTiposContratosValidos : " + e.toString());
         return null;
      }
   }

   @Override
   public List<MotivosCambiosSueldos> lovMotivosCambiosSueldos() {
      try {
         return persistenciaMotivosCambiosSueldos.buscarMotivosCambiosSueldos(getEm());
      } catch (Exception e) {
         log.warn("Error lovMotivosCambiosSueldos Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<Unidades> lovUnidades() {
      try {
         return persistenciaUnidades.consultarUnidades(getEm());
      } catch (Exception e) {
         log.warn("Error lovUnidades Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<Periodicidades> lovPeriodicidades() {
      try {
         return persistenciaPeriodicidades.consultarPeriodicidades(getEm());
      } catch (Exception e) {
         log.warn("Error lovPeriodicidades Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<Sucursales> lovSucursales() {
      try {
         return persistenciaSucursales.consultarSucursales(getEm());
      } catch (Exception e) {
         log.warn("Error lovSucursales Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<MetodosPagos> lovMetodosPagos() {
      try {
         return persistenciaMetodosPagos.buscarMetodosPagos(getEm());
      } catch (Exception e) {
         log.warn("Error lovMetodosPagos Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<TercerosSucursales> lovTercerosSucursales(BigInteger secuencia) {
      try {
         return persistenciaTercerosSucursales.buscarTercerosSucursalesPorEmpresa(getEm(), secuencia);
      } catch (Exception e) {
         log.warn("Error lovTercerosSucursales Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<EstadosCiviles> lovEstadosCiviles() {
      try {
         return persistenciaEstadosCiviles.consultarEstadosCiviles(getEm());
      } catch (Exception e) {
         log.warn("Error lovEstadosCiviles Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<TiposTelefonos> lovTiposTelefonos() {
      try {
         return persistenciaTiposTelefonos.tiposTelefonos(getEm());
      } catch (Exception e) {
         log.warn("Error lovTiposTelefonos Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<Empleados> lovEmpleados() {
      try {
         return persistenciaEmpleado.buscarEmpleadosActivos(getEm());
      } catch (Exception e) {
         log.warn("Error lovEmpleados Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public void crearPersona(Personas persona) {
      try {
         persistenciaPersonas.crear(getEm(), persona);
      } catch (Exception e) {
         log.warn("Error crearPersona Admi : " + e.toString());
      }
   }

   @Override
   public List<Papeles> lovPapeles() {
      try {
         return persistenciaPapeles.consultarPapeles(getEm());
      } catch (Exception e) {
         log.warn("Error lovPapeles Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public TiposEntidades buscarTipoEntidadPorCodigo(Short codigo) {
      try {
         return persistenciaTiposEntidades.buscarTipoEntidadPorCodigo(getEm(), codigo);
      } catch (Exception e) {
         log.warn("Error buscarTipoEntidadPorCodigo Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public String buscarCodigoSSTercero(BigInteger secuencia) {
      try {
         return persistenciaTerceros.buscarCodigoSSPorSecuenciaTercero(getEm(), secuencia);
      } catch (Exception e) {
         log.warn("Error buscarCodigoSSTercero Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public String buscarCodigoSPTercero(BigInteger secuencia) {
      try {
         return persistenciaTerceros.buscarCodigoSPPorSecuenciaTercero(getEm(), secuencia);
      } catch (Exception e) {
         log.warn("Error buscarCodigoSPTercero Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public String buscarCodigoSCTercero(BigInteger secuencia) {
      try {
         return persistenciaTerceros.buscarCodigoSCPorSecuenciaTercero(getEm(), secuencia);
      } catch (Exception e) {
         log.warn("Error buscarCodigoSCTercero Admi : " + e.toString());
         return null;
      }
   }

   //@Override
   @Override
   public BigInteger calcularNumeroEmpleadosEmpresa(BigInteger secuencia) {
      try {
         return persistenciaEmpresas.calcularControlEmpleadosEmpresa(getEm(), secuencia);
      } catch (Exception e) {
         log.warn("Error calcularNumeroEmpleadosEmpresa Admi : " + e.toString());
         return null;
      }
   }

   //@Override
   @Override
   public BigInteger obtenerMaximoEmpleadosEmpresa(BigInteger secuencia) {
      try {
         return persistenciaEmpresas.obtenerMaximoEmpleadosEmpresa(getEm(), secuencia);
      } catch (Exception e) {
         log.warn("Error obtenerMaximoEmpleadosEmpresa Admi : " + e.toString());
         return null;
      }
   }

   public Empleados buscarEmpleadoPorCodigoyEmpresa(BigDecimal codigo, BigInteger empresa) {
      try {
         return persistenciaEmpleado.buscarEmpleadoCodigo_Empresa(getEm(), codigo.toBigInteger(), empresa);
      } catch (Exception e) {
         log.warn("Error buscarEmpleadoPorCodigoyEmpresa Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public Personas buscarPersonaPorNumeroDocumento(BigInteger numeroDocumento) {
      try {
         return persistenciaPersonas.buscarPersonaPorNumeroDocumento(getEm(), numeroDocumento);
      } catch (Exception e) {
         log.warn("Error buscarPersonaPorNumeroDocumento Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public String obtenerPreValidadContabilidad() {
      try {
         return persistenciaGenerales.obtenerPreValidadContabilidad(getEm());
      } catch (Exception e) {
         log.warn("Error obtenerPreValidadContabilidad Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public String obtenerPreValidaBloqueAIngreso() {
      try {
         return persistenciaGenerales.obtenerPreValidaBloqueAIngreso(getEm());
      } catch (Exception e) {
         log.warn("Error obtenerPreValidaBloqueAIngreso Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public VWValidaBancos validarCodigoPrimarioVWValidaBancos(BigInteger documento) {
      try {
         return persistenciaVWValidaBancos.validarDocumentoVWValidaBancos(getEm(), documento);
      } catch (Exception e) {
         log.warn("Error validarCodigoPrimarioVWValidaBancos Admi : " + e.toString());
         return null;
      }
   }

   //@Override
   @Override
   public String validarTipoTrabajadorReformaLaboral(BigInteger tipoTrabajador, BigInteger reformaLaboral) {
      try {
         String validar = persistenciaTiposTrabajadores.plantillaValidarTipoTrabajadorReformaLaboral(getEm(), tipoTrabajador, reformaLaboral);
         if (validar == null) {
            validar = " ";
         }
         return validar;
      } catch (Exception e) {
         log.warn("Error validarTipoTrabajadorReformaLaboral Admi : " + e.toString());
         return " ";
      }
   }

   //@Override
   @Override
   public String validarTipoTrabajadorTipoSueldo(BigInteger tipoTrabajador, BigInteger tipoSueldo) {
      try {
         String retorno = persistenciaTiposTrabajadores.plantillaValidarTipoTrabajadorTipoSueldo(getEm(), tipoTrabajador, tipoSueldo);
         if (retorno == null) {
            retorno = " ";
         }
         return retorno;
      } catch (Exception e) {
         log.warn("Error validarTipoTrabajadorTipoSueldo Admi : " + e.toString());
         return " ";
      }
   }

   //@Override
   @Override
   public String validarTipoTrabajadorTipoContrato(BigInteger tipoTrabajador, BigInteger tipoContrato) {
      try {
         String validar = persistenciaTiposTrabajadores.plantillaValidarTipoTrabajadorTipoContrato(getEm(), tipoTrabajador, tipoContrato);
         if (validar == null) {
            validar = " ";
         }
         return validar;
      } catch (Exception e) {
         log.warn("Error validarTipoTrabajadorTipoContrato Admi : " + e.toString());
         return " ";
      }
   }

   //@Override
   @Override
   public String validarTipoTrabajadorNormaLaboral(BigInteger tipoTrabajador, BigInteger normaLaboral) {
      try {
         String validar = persistenciaTiposTrabajadores.plantillaValidarTipoTrabajadorNormaLaboral(getEm(), tipoTrabajador, normaLaboral);
         if (validar == null) {
            validar = " ";
         }
         return validar;
      } catch (Exception e) {
         log.warn("Error validarTipoTrabajadorNormaLaboral Admi : " + e.toString());
         return " ";
      }
   }

   //@Override
   @Override
   public String validarTipoTrabajadorContrato(BigInteger tipoTrabajador, BigInteger contrato) {
      try {
         String validar = persistenciaTiposTrabajadores.plantillaValidarTipoTrabajadorContrato(getEm(), tipoTrabajador, contrato);
         if (validar == null) {
            validar = " ";
         }
         return validar;
      } catch (Exception e) {
         log.warn("Error validarTipoTrabajadorContrato Admi : " + e.toString());
         return " ";
      }
   }

   //Override
   @Override
   public String obtenerCheckIntegralReformaLaboral(BigInteger reformaLaboral) {
      try {
         return persistenciaReformasLaborales.obtenerCheckIntegralReformaLaboral(getEm(), reformaLaboral);
      } catch (Exception e) {
         log.warn("Error obtenerCheckIntegralReformaLaboral Admi : " + e.toString());
         return null;
      }

   }

   @Override
   public void crearNuevaPersona(Personas persona) {
      try {
         persistenciaPersonas.crear(getEm(), persona);
      } catch (Exception e) {
         log.warn("Error crearNuevaPersona Admi : " + e.toString());
      }
   }

   @Override
   public Personas obtenerUltimoRegistroPersona(BigInteger documento) {
      try {
         return persistenciaPersonas.obtenerUltimaPersonaAlmacenada(getEm(), documento);
      } catch (Exception e) {
         log.warn("Error obtenerUltimoRegistroPersona Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public Empleados crearEmpl_Con_VCargo(BigDecimal codigoEmpleado, BigInteger secPersona, BigInteger secEmpresa, VigenciasCargos vigenciaCargo) {
      try {
         BigInteger secEmpleado = persistenciaEmpleado.crearConVCargo(getEm(), codigoEmpleado, secPersona, secEmpresa, vigenciaCargo.getCargo().getSecuencia(),
                 vigenciaCargo.getEstructura().getSecuencia(), vigenciaCargo.getFechavigencia(), vigenciaCargo.getMotivocambiocargo().getSecuencia());
         Empleados empleado = persistenciaEmpleado.buscarEmpleadoSecuencia(getEm(), secEmpleado);
         return empleado;
      } catch (Exception e) {
         log.error(this.getClass().getName() + " Error crearConVCargo() : " + e.toString());
         return null;
      }
   }

   @Override
   public VigenciasCargos obtenerUltimaVigenciaCargo(BigInteger secEmpleado, BigInteger empresa) {
      try {
         return persistenciaVigenciasCargos.buscarVigenciaCargoXEmpleado(getEm(), secEmpleado, empresa);
      } catch (Exception e) {
         log.warn("Error obtenerUltimoRegistroEmpleado Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public void modificarVigenciaCargo(VigenciasCargos vigencia) {
      try {
         persistenciaVigenciasCargos.editar(getEm(), vigencia);
      } catch (Exception e) {
         log.warn("Error modificarVigenciaCargo Admi : " + e.toString());
      }
   }

   @Override
   public boolean crearVigenciaLocalizacion(VigenciasLocalizaciones vigencia) {
      try {
         return persistenciaVigenciasLocalizaciones.crear(getEm(), vigencia);
      } catch (Exception e) {
         log.warn("Error crearVigenciaLocalizacion Admi : " + e.toString());
         return false;
      }
   }

   @Override
   public boolean crearVigenciaTipoTrabajador(VigenciasTiposTrabajadores vigencia) {
      try {
         return persistenciaVigenciasTiposTrabajadores.crear(getEm(), vigencia);
      } catch (Exception e) {
         log.warn("Error crearVigenciaTipoTrabajador Admi : " + e.toString());
         return false;
      }
   }

   @Override
   public boolean crearVigenciaReformaLaboral(VigenciasReformasLaborales vigencia) {
      try {
         return persistenciaVigenciasReformasLaborales.crear(getEm(), vigencia);
      } catch (Exception e) {
         log.warn("Error crearVigenciaReformaLaboral Admi : " + e.toString());
         return false;
      }
   }

   @Override
   public boolean crearVigenciaSueldo(VigenciasSueldos vigencia) {
      try {
         return persistenciaVigenciasSueldos.crear(getEm(), vigencia);
      } catch (Exception e) {
         log.warn("Error crearVigenciaSueldo Admi : " + e.toString());
         return false;
      }
   }

   @Override
   public boolean crearVigenciaTipoContrato(VigenciasTiposContratos vigencia) {
      try {
         return persistenciaVigenciasTiposContratos.crear(getEm(), vigencia);
      } catch (Exception e) {
         log.warn("Error crearVigenciaTipoContrato Admi : " + e.toString());
         return false;
      }
   }

   @Override
   public boolean crearVigenciaNormaEmpleado(VigenciasNormasEmpleados vigencia) {
      try {
         return persistenciaVigenciasNormasEmpleados.crear(getEm(), vigencia);
      } catch (Exception e) {
         log.warn("Error crearVigenciaNormaEmpleado Admi : " + e.toString());
         return false;
      }
   }

   @Override
   public boolean crearVigenciaContrato(VigenciasContratos vigencia) {
      try {
         return persistenciaVigenciasContratos.crear(getEm(), vigencia);
      } catch (Exception e) {
         log.warn("Error crearVigenciaContrato Admi : " + e.toString());
         return false;
      }
   }

   @Override
   public boolean crearVigenciaUbicacion(VigenciasUbicaciones vigencia) {
      try {
         return persistenciaVigenciasUbicaciones.crear(getEm(), vigencia);
      } catch (Exception e) {
         log.warn("Error crearVigenciaUbicacion Admi : " + e.toString());
         return false;
      }
   }

   @Override
   public boolean crearVigenciaJornada(VigenciasJornadas vigencia) {
      try {
         return persistenciaVigenciasJornadas.crear(getEm(), vigencia);
      } catch (Exception e) {
         log.warn("Error crearVigenciaJornada Admi : " + e.toString());
         return false;
      }
   }

   @Override
   public boolean crearVigenciaFormaPago(VigenciasFormasPagos vigencia) {
      try {
         return persistenciaVigenciasFormasPagos.crear(getEm(), vigencia);
      } catch (Exception e) {
         log.warn("Error crearVigenciaFormaPago Admi : " + e.toString());
         return false;
      }
   }

   /**
    *
    * @param vigencia
    * @return
    */
   @Override
   public boolean crearVigenciaAfiliacion(VigenciasAfiliaciones vigencia) {
      try {
         return persistenciaVigenciasAfiliaciones.crear(getEm(), vigencia);
      } catch (Exception e) {
         log.warn("Error crearVigenciaAfiliacion Admi : " + e.toString());
         return false;
      }
   }

   /**
    *
    * @param estado
    * @return
    */
   @Override
   public boolean crearEstadoCivil(VigenciasEstadosCiviles estado) {
      try {
         return persistenciaVigenciasEstadosCiviles.crear(getEm(), estado);
      } catch (Exception e) {
         log.warn("Error crearEstadoCivil Admi : " + e.toString());
         return false;
      }
   }

   @Override
   public boolean crearDireccion(Direcciones direccion) {
      try {
         return persistenciaDirecciones.crear(getEm(), direccion);
      } catch (Exception e) {
         log.warn("Error crearDireccion Admi : " + e.toString());
         return false;
      }
   }

   @Override
   public boolean crearTelefono(Telefonos telefono) {
      try {
         return persistenciaTelefonos.crear(getEm(), telefono);
      } catch (Exception e) {
         log.warn("Error crearTelefono Admi : " + e.toString());
         return false;
      }
   }

   @Override
   public boolean crearSets(Sets set) {
      try {
         return persistenciaSets.crear(getEm(), set);
      } catch (Exception e) {
         log.warn("Error crearSets Admi : " + e.toString());
         return false;
      }
   }

   @Override
   public Procesos buscarProcesoPorCodigo(short codigo) {
      try {
         return persistenciaProcesos.buscarProcesosPorCodigo(getEm(), codigo);
      } catch (Exception e) {
         log.warn("Error buscarProcesoPorCodigo Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public BigDecimal obtenerNumeroMaximoComprobante() {
      try {
         return persistenciaComprobantes.buscarValorNumeroMaximo(getEm());
      } catch (Exception e) {
         log.warn("Error obtenerNumeroMaximoComprobante Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public boolean crearComprobante(Comprobantes comprobante) {
      try {
         return persistenciaComprobantes.crear(getEm(), comprobante);
      } catch (Exception e) {
         log.warn("Error crearComprobante Admi : " + e.toString());
         return false;
      }
   }

   @Override
   public Comprobantes buscarComprobanteParaPrimerRegistroEmpleado(BigInteger secEmpleado) {
      try {
         return persistenciaComprobantes.buscarComprobanteParaPrimerRegistroEmpleado(getEm(), secEmpleado);
      } catch (Exception e) {
         log.warn("Error buscarComprobanteParaPrimerRegistroEmpleado Admi : " + e.toString());
         return null;
      }
   }

   /**
    *
    * @param corte
    * @return
    */
   @Override
   public boolean crearCortesProcesos(CortesProcesos corte) {
      try {
         return persistenciaCortesProcesos.crear(getEm(), corte);
      } catch (Exception e) {
         log.warn("Error crearCortesProcesos Admi : " + e.toString());
         return false;
      }
   }

   @Override
   public TiposTrabajadores buscarTipoTrabajadorPorCodigo(short codigo) {
      try {
         return persistenciaTiposTrabajadores.buscarTipoTrabajadorCodigoTiposhort(getEm(), codigo);
      } catch (Exception e) {
         log.warn("Error buscarTipoTrabajadorPorCodigo Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public TercerosSucursales consultarARL(BigInteger secEmpresa) {
      try {
         return persistenciadetallesEmpresas.buscarARLPorEmpresa(getEm(), secEmpresa);
      } catch (Exception e) {
         log.warn("Error consultarARL Admin : " + e.toString());
         return null;
      }
   }

   @Override
   public Date consultarFechaHastaCausado() {
      try {
         return persistenciaVWActualesFechas.actualFechaHasta(getEm());
      } catch (Exception e) {
         log.warn("Error consultando fecha en : " + this.getClass().getName());
         return null;
      }
   }

   @Override
   public boolean eliminarEmpleadoCompleto(BigInteger secEmpleado, BigInteger secPersona) {
      try {
         return persistenciaEmpleado.eliminarEmpleadoNominaF(getEm(), secEmpleado, secPersona);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".eliminarEmpleadoCompleto() ERROR: " + e);
         return false;
      }
   }

}
