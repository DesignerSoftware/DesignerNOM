package Administrar;

import Entidades.CambiosMasivos;
import Entidades.Causasausentismos;
import Entidades.CentrosCostos;
import Entidades.Clasesausentismos;
import Entidades.Conceptos;
import Entidades.Empleados;
import Entidades.Estructuras;
import Entidades.Formulas;
import Entidades.MotivosCambiosSueldos;
import Entidades.MotivosDefinitivas;
import Entidades.MotivosRetiros;
import Entidades.Papeles;
import Entidades.Parametros;
import Entidades.ParametrosCambiosMasivos;
import Entidades.Periodicidades;
import Entidades.Terceros;
import Entidades.TercerosSucursales;
import Entidades.TiposEntidades;
import Entidades.TiposSueldos;
import Entidades.Tiposausentismos;
import Entidades.Unidades;
import InterfaceAdministrar.AdministrarCambiosMasivosInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaActualUsuarioInterface;
import InterfacePersistencia.PersistenciaCausasAusentismosInterface;
import InterfacePersistencia.PersistenciaCentrosCostosInterface;
import InterfacePersistencia.PersistenciaClasesAusentismosInterface;
import InterfacePersistencia.PersistenciaConceptosInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaEstructurasInterface;
import InterfacePersistencia.PersistenciaFormulasInterface;
import InterfacePersistencia.PersistenciaIBCSInterface;
import InterfacePersistencia.PersistenciaMotivosCambiosSueldosInterface;
import InterfacePersistencia.PersistenciaMotivosDefinitivasInterface;
import InterfacePersistencia.PersistenciaMotivosRetirosInterface;
import InterfacePersistencia.PersistenciaNovedadesSistemaInterface;
import InterfacePersistencia.PersistenciaPapelesInterface;
import InterfacePersistencia.PersistenciaParametrosInterface;
import InterfacePersistencia.PersistenciaPeriodicidadesInterface;
import InterfacePersistencia.PersistenciaRetiradosInterface;
import InterfacePersistencia.PersistenciaSoausentismosInterface;
import InterfacePersistencia.PersistenciaTercerosInterface;
import InterfacePersistencia.PersistenciaTercerosSucursalesInterface;
import InterfacePersistencia.PersistenciaTiposAusentismosInterface;
import InterfacePersistencia.PersistenciaTiposEntidadesInterface;
import InterfacePersistencia.PersistenciaTiposSueldosInterface;
import InterfacePersistencia.PersistenciaUnidadesInterface;
import InterfacePersistencia.PersistenciaVacacionesInterface;
import InterfacePersistencia.PersistenciaVigenciasCargosInterface;
import InterfacePersistencia.PersistenciaVigenciasSueldosInterface;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

@Stateful
public class AdministrarCambiosMasivos implements AdministrarCambiosMasivosInterface {

   private static Logger log = Logger.getLogger(AdministrarCambiosMasivos.class);

   private EntityManagerFactory emf;
   private EntityManager em;
   private String idSesionBck;

   private EntityManager getEm() {
      try {
         if (this.emf != null) {
            if (this.em != null) {
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
   AdministrarSesionesInterface administrarSesiones;
   @EJB
   PersistenciaParametrosInterface persistenciaParametros;
   @EJB
   PersistenciaActualUsuarioInterface persistenciaActualUsuario;
   @EJB
   PersistenciaEstructurasInterface persistenciaEstructuras;
   @EJB
   PersistenciaMotivosRetirosInterface persistenciaMotivosRetiros;
   @EJB
   PersistenciaMotivosDefinitivasInterface persistenciaMotivosDefinitivas;
   @EJB
   PersistenciaTiposEntidadesInterface persistenciaTiposEntidades;
   @EJB
   PersistenciaTercerosSucursalesInterface persistenciaTercerosSucursales;
   @EJB
   PersistenciaCentrosCostosInterface persistenciaCentrosCostos;
   @EJB
   PersistenciaPeriodicidadesInterface persistenciaPeriodicidades;
   @EJB
   PersistenciaConceptosInterface persistenciaConceptos;
   @EJB
   PersistenciaFormulasInterface persistenciaFormulas;
   @EJB
   PersistenciaTercerosInterface persistenciaTerceros;
   @EJB
   PersistenciaMotivosCambiosSueldosInterface persistenciaMotivosCambiosSueldos;
   @EJB
   PersistenciaTiposSueldosInterface persistenciaTiposSueldos;
   @EJB
   PersistenciaTiposAusentismosInterface persistenciaTiposAusentismos;
   @EJB
   PersistenciaUnidadesInterface persistenciaUnidades;
   @EJB
   PersistenciaEmpleadoInterface persistenciaEmpleados;
   @EJB
   PersistenciaIBCSInterface persistenciaIBCS;
   @EJB
   PersistenciaPapelesInterface persistenciaPapeles;
   @EJB
   PersistenciaCausasAusentismosInterface persistenciaCausasAusentismos;
   @EJB
   PersistenciaClasesAusentismosInterface persistenciaClasesAusentismos;
   @EJB
   PersistenciaVacacionesInterface persistenciaVacaciones;
   @EJB
   PersistenciaRetiradosInterface persistenciaRetirados;
   @EJB
   PersistenciaVigenciasSueldosInterface persistenciaVigenciasSueldos;
   @EJB
   PersistenciaNovedadesSistemaInterface persistenciaNovedadesSistema;
   @EJB
   PersistenciaVigenciasCargosInterface persistenciaVigenciasCargos;
   @EJB
   PersistenciaSoausentismosInterface persistenciaSoausentismos;

   @Override
   public void obtenerConexion(String idSesion) {
      idSesionBck = idSesion;
      log.warn("Administrar.AdministrarCambiosMasivos.obtenerConexion()");
      try {
         emf = administrarSesiones.obtenerConexionSesionEMF(idSesion);
      } catch (Exception e) {
         log.fatal(this.getClass().getSimpleName() + " obtenerConexion ERROR: " + e);
      }
   }

   @Override
   public List<Parametros> consultarEmpleadosParametros() {
      log.warn("Administrar.AdministrarCambiosMasivos.consultarEmpleadosParametros()");
      try {
         String usuarioBD = persistenciaActualUsuario.actualAliasBD(getEm());
         return persistenciaParametros.empleadosParametros(getEm(), usuarioBD);
      } catch (Exception e) {
         log.warn("ERROR Administrar.AdministrarCambiosMasivos.consultarEmpleadosParametros()");
         log.warn("ERROR : " + e);
         return null;
      }
   }

   @Override
   public List<CambiosMasivos> consultarUltimosCambiosMasivos() {
      try {
         return persistenciaParametros.listcambiosmasivos(getEm());
      } catch (Exception e) {
         log.warn("ERROR Administrar.AdministrarCambiosMasivos.consultarUltimosCambiosMasivos()");
         log.warn("ERROR : " + e);
         return null;
      }
   }

   @Override
   public ParametrosCambiosMasivos consultarParametrosCambiosMasivos() {
      log.warn("Administrar.AdministrarCambiosMasivos.consultarParametrosCambiosMasivos()");
      try {
         String usuarioBD = persistenciaActualUsuario.actualAliasBD(getEm());
         ParametrosCambiosMasivos parametro = persistenciaParametros.parametrosCambiosMasivos(getEm(), usuarioBD);
         if (parametro == null) {
            parametro = new ParametrosCambiosMasivos();
            parametro.setSecuencia(new BigInteger("0"));
            parametro.setUsuarioBD(usuarioBD);

            parametro.setAfiliaTerceroSucursal(null);
            parametro.setAfiliaTipoEntidad(null);
            parametro.setCargoEstructura(null);
            parametro.setLocaliEstructura(null);
            parametro.setNoveConcepto(null);
            parametro.setNoveFormula(null);
            parametro.setNovePeriodicidad(null);
            parametro.setNoveTercero(null);
            parametro.setRetiMotivoDefinitiva(null);
            parametro.setRetiMotivoRetiro(null);
            parametro.setSueldoMotivoCambioSueldo(null);
            parametro.setSueldoTipoSueldo(null);
            parametro.setSueldoUnidadPago(null);
         }
         return parametro;
      } catch (Exception e) {
         log.warn("ERROR Administrar.AdministrarCambiosMasivos.consultarParametrosCambiosMasivos()");
         log.warn("ERROR : " + e);
         return null;
      }
   }

   public boolean actualizarParametroCM(ParametrosCambiosMasivos parametro) {
      try {
         return persistenciaParametros.actualizarParametroCambioMasivo(getEm(), parametro);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".actualizarParametroCM() ERROR: " + e);
         return false;
      }
   }

   public boolean comprobarConceptoManual(BigInteger secuenciaConcepto) {
      try {
         return persistenciaConceptos.verificarConceptoManual(getEm(), secuenciaConcepto);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".comprobarConceptoManual() ERROR: " + e);
         return false;
      }
   }

   @Override
   public List<Estructuras> consultarLovCargos_Estructuras() {
      try {
         return persistenciaEstructuras.buscarEstructuras(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarLovCargos_Estructuras() ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<MotivosDefinitivas> consultarLovMotivosDefinitivas() {
      try {
         return persistenciaMotivosDefinitivas.buscarMotivosDefinitivas(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarLovMotivosDefinitivas() ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<MotivosRetiros> consultarLovMotivosRetiros() {
      try {
         return persistenciaMotivosRetiros.consultarMotivosRetiros(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarLovMotivosRetiros() ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<TiposEntidades> consultarLovTiposEntidades() {
      try {
         return persistenciaTiposEntidades.buscarTiposEntidades(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarLovTiposEntidades() ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<TercerosSucursales> consultarLovTercerosSucursales() {
      try {
         return persistenciaTercerosSucursales.buscarTercerosSucursales(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarLovTercerosSucursales() ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<CentrosCostos> consultarLovCentrosCostos() {
      try {
         return persistenciaCentrosCostos.buscarCentrosCostosCM(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarLovCentrosCostos() ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<Periodicidades> consultarLovPeriodicidades() {
      try {
         return persistenciaPeriodicidades.consultarPeriodicidades(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarLovPeriodicidades() ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<Conceptos> consultarLovConceptos() {
      try {
         return persistenciaConceptos.buscarConceptosLovNovedades(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarLovConceptos() ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<Formulas> consultarLovFormulas() {
      try {
         return persistenciaFormulas.buscarFormulas(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarLovFormulas() ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<Terceros> consultarLovTerceros() {
      try {
         return persistenciaTerceros.buscarTerceros(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarLovTerceros() ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<MotivosCambiosSueldos> consultarLovMotivosCambiosSueldos() {
      try {
         return persistenciaMotivosCambiosSueldos.buscarMotivosCambiosSueldos(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarLovMotivosCambiosSueldos() ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<TiposSueldos> consultarLovTiposSueldos() {
      try {
         return persistenciaTiposSueldos.buscarTiposSueldos(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarLovTiposSueldos() ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<Tiposausentismos> consultarLovTiposausentismos() {
      try {
         return persistenciaTiposAusentismos.consultarTiposAusentismos(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarLovTiposausentismos() ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<Unidades> consultarLovUnidades() {
      try {
         return persistenciaUnidades.consultarUnidades(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarLovUnidades() ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<Empleados> consultarLovEmpleados() {
      try {
         return persistenciaEmpleados.buscarEmpleadosActivos(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarLovEmpleados() ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<Papeles> consultarLovPapeles() {
      try {
         return persistenciaPapeles.consultarPapeles(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarLovPapeles() ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<Causasausentismos> consultarLovCausasausentismos() {
      try {
         return persistenciaCausasAusentismos.buscarCausasAusentismos(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarLovCausasausentismos() ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<Clasesausentismos> consultarLovClasesausentismos() {
      try {
         return persistenciaClasesAusentismos.buscarClasesAusentismos(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarLovClasesausentismos() ERROR: " + e);
         return null;
      }
   }

   @Override
   public void adicionaEstructuraCM2(BigInteger secEstructura, Date fechaCambio) {
      try {
         log.warn("Administrar.AdministrarCambiosMasivos.adicionaEstructuraCM2()");
         persistenciaEstructuras.adicionaEstructuraCambiosMasivos(getEm(), secEstructura, fechaCambio);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".adicionaEstructuraCM2() ERROR: " + e);
      }
   }

   public void undoAdicionaEstructuraCM2(BigInteger secEstructura, Date fechaCambio) {
      try {
         log.warn("Administrar.AdministrarCambiosMasivos.undoAdicionaEstructuraCM2()");
         persistenciaEstructuras.undoAdicionaEstructuraCambiosMasivos(getEm(), secEstructura, fechaCambio);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".undoAdicionaEstructuraCM2() ERROR: " + e);
      }
   }

   public void adicionaVacacionCM2(BigInteger ndias, Date fechaCambio, Date fechaPago) {
      try {
         log.warn("Administrar.AdministrarCambiosMasivos.adicionaVacacionCM2()");
         persistenciaVacaciones.adicionaVacacionCambiosMasivos(getEm(), ndias, fechaCambio, fechaPago);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".adicionaVacacionCM2() ERROR: " + e);
      }
   }

   public void undoAdicionaVacacionCM2(BigInteger ndias, Date fechaCambio, Date fechaPago) {
      try {
         log.warn("Administrar.AdministrarCambiosMasivos.undoAdicionaVacacionCM2()");
         persistenciaVacaciones.undoAdicionaVacacionCambiosMasivos(getEm(), ndias, fechaCambio, fechaPago);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".undoAdicionaVacacionCM2() ERROR: " + e);
      }
   }

   public void adicionaRetiroCM2(String indemniza, BigInteger secMotivoDefinitiva, BigInteger secMotivoRetiro, Date fechaCambio) {
      try {
         log.warn("Administrar.AdministrarCambiosMasivos.adicionaRetiroCM2()");
         persistenciaRetirados.adicionaRetiroCambiosMasivos(getEm(), indemniza, BigInteger.TEN, BigInteger.ZERO, fechaCambio);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".adicionaRetiroCM2() ERROR: " + e);
      }
   }

   public void undoAdicionaRetiroCM2(String indemniza, BigInteger secMotivoDefinitiva, BigInteger secMotivoRetiro, Date fechaCambio) {
      try {
         log.warn("Administrar.AdministrarCambiosMasivos.undoAdicionaRetiroCM2()");
         persistenciaRetirados.undoAdicionaRetiroCambiosMasivos(getEm(), indemniza, BigInteger.TEN, BigInteger.ZERO, fechaCambio);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".undoAdicionaRetiroCM2() ERROR: " + e);
      }
   }

   public void adicionaAfiliacionCM2(BigInteger secTipoEntidad, BigInteger secTerceroSuc, Date fechaCambio) {
      try {
         log.warn("Administrar.AdministrarCambiosMasivos.adicionaAfiliacionCM2()");
         persistenciaTercerosSucursales.adicionaAfiliacionCambiosMasivos(getEm(), secTipoEntidad, secTerceroSuc, fechaCambio);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".adicionaAfiliacionCM2() ERROR: " + e);
      }
   }

   public void undoAdicionaAfiliacionCM2(BigInteger secTipoEntidad, BigInteger secTerceroSuc, Date fechaCambio) {
      try {
         log.warn("Administrar.AdministrarCambiosMasivos.undoAdicionaAfiliacionCM2()");
         persistenciaTercerosSucursales.undoAdicionaAfiliacionCambiosMasivos(getEm(), secTipoEntidad, secTerceroSuc, fechaCambio);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".undoAdicionaAfiliacionCM2() ERROR: " + e);
      }
   }

   public void adicionaLocalizacionCM2(BigInteger secEstructura, Date fechaCambio) {
      try {
         log.warn("Administrar.AdministrarCambiosMasivos.adicionaLocalizacionCM2()");
         persistenciaEstructuras.adicionaLocalizacionCambiosMasivos(getEm(), secEstructura, fechaCambio);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".adicionaLocalizacionCM2() ERROR: " + e);
      }
   }

   public void undoAdicionaLocalizacionCM2(BigInteger secEstructura, Date fechaCambio) {
      try {
         log.warn("Administrar.AdministrarCambiosMasivos.undoAdicionaLocalizacionCM2()");
         persistenciaEstructuras.undoAdicionaLocalizacionCambiosMasivos(getEm(), secEstructura, fechaCambio);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".undoAdicionaLocalizacionCM2() ERROR: " + e);
      }
   }

   public void adicionaSueldoCM2(BigInteger secMotivoCS, BigInteger secTipoSueldo, BigInteger secUnidad, BigInteger valor, Date fechaCambio) {
      try {
         log.warn("Administrar.AdministrarCambiosMasivos.adicionaSueldoCM2()");
         persistenciaVigenciasSueldos.adicionaSueldoCambiosMasivos(getEm(), secMotivoCS, secTipoSueldo, secUnidad, valor, fechaCambio);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".adicionaSueldoCM2() ERROR: " + e);
      }
   }

   public void undoAdicionaSueldoCM2(BigInteger secMotivoCS, BigInteger secTipoSueldo, BigInteger secUnidad, BigInteger valor, Date fechaCambio) {
      try {
         log.warn("Administrar.AdministrarCambiosMasivos.undoAdicionaSueldoCM2()");
         persistenciaVigenciasSueldos.undoAdicionaSueldoCambiosMasivos(getEm(), secMotivoCS, secTipoSueldo, secUnidad, valor, fechaCambio);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".undoAdicionaSueldoCM2() ERROR: " + e);
      }
   }

   public void adicionaNovedadCM2(String tipo, BigInteger secConcepto, BigInteger secPeriodicidad,
           BigInteger secTercero, BigInteger secFormula, BigInteger valor,
           BigInteger saldo, Date fechaCambioInicial, Date fechaCambioFinal,
           BigInteger unidadParteEntera, BigInteger unidadParteFraccion) {
      try {
         log.warn("Administrar.AdministrarCambiosMasivos.adicionaNovedadCM2()");
         persistenciaNovedadesSistema.adicionaNovedadCambiosMasivos(getEm(), tipo, secConcepto, secPeriodicidad, secTercero, secFormula, valor, saldo, fechaCambioInicial, fechaCambioFinal, unidadParteEntera, unidadParteFraccion);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".adicionaNovedadCM2() ERROR: " + e);
      }
   }

   public void undoAdicionaNovedadCM2(String tipo, BigInteger secConcepto, BigInteger secPeriodicidad,
           BigInteger secTercero, BigInteger secFormula, BigInteger valor,
           BigInteger saldo, Date fechaCambioInicial, Date fechaCambioFinal) {
      try {
         log.warn("Administrar.AdministrarCambiosMasivos.undoAdicionaNovedadCM2()");
         persistenciaNovedadesSistema.undoAdicionaNovedadCambiosMasivos(getEm(), tipo, secConcepto, secPeriodicidad, secTercero, secFormula, valor, saldo, fechaCambioInicial, fechaCambioFinal);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".undoAdicionaNovedadCM2() ERROR: " + e);
      }
   }

   public void adicionaReingresoCM2(Date fechaIni, Date fechaFin) {
      try {
         log.warn("Administrar.AdministrarCambiosMasivos.adicionaReingresoCM2()");
         persistenciaRetirados.adicionaReingresoCambiosMasivos(getEm(), fechaIni, fechaFin);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".adicionaReingresoCM2() ERROR: " + e);
      }
   }

   public void adicionaEmplJefeCM2(BigInteger secEmpleado, Date fechaCambio) {
      try {
         log.warn("Administrar.AdministrarCambiosMasivos.adicionaEmplJefeCM2()");
         persistenciaVigenciasCargos.adicionaEmplJefeCambiosMasivos(getEm(), secEmpleado, fechaCambio);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".adicionaEmplJefeCM2() ERROR: " + e);
      }
   }

   public void adicionaPapelCM2(BigInteger secPapel, Date fechaCambio) {
      try {
         log.warn("Administrar.AdministrarCambiosMasivos.adicionaPapelCM2()");
         persistenciaPapeles.adicionaPapelCambiosMasivos(getEm(), secPapel, fechaCambio);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".adicionaPapelCM2() ERROR: " + e);
      }
   }

   public void adicionaAusentismoCM2(BigInteger secTipo, BigInteger secClase, BigInteger secCausa, BigInteger dias,
           BigInteger horas, Date fechaIniAusen, Date fechaFinAusen, Date fechaExpedicion,
           Date fechaIpago, Date fechaPago, BigInteger porcent, BigInteger baseliq, String forma) {
      try {
         log.warn("Administrar.AdministrarCambiosMasivos.adicionaAusentismoCM2()");
         persistenciaSoausentismos.adicionaAusentismoCambiosMasivos(getEm(), secTipo, secClase, secCausa, dias, horas, fechaIniAusen, fechaFinAusen, fechaExpedicion, fechaIpago, fechaPago, porcent, baseliq, forma);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".adicionaAusentismoCM2() ERROR: " + e);
      }
   }

   public void undoAdicionaAusentismoCM2(BigInteger secTipo, BigInteger secClase, BigInteger secCausa, BigInteger dias, Date fechaIniAusen, Date fechaFinAusen) {
      try {
         log.warn("Administrar.AdministrarCambiosMasivos.undoAdicionaAusentismoCM2()");
         persistenciaSoausentismos.undoAdicionaAusentismoCambiosMasivos(getEm(), secTipo, secClase, secCausa, dias, fechaIniAusen, fechaFinAusen);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".undoAdicionaAusentismoCM2() ERROR: " + e);
      }
   }

}
