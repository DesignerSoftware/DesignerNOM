package Administrar;

import Entidades.ActualUsuario;
import Entidades.Empleados;
import Entidades.Empresas;
import Entidades.InterconDynamics;
import Entidades.ParametrosContables;
import Entidades.ParametrosEstructuras;
import Entidades.Procesos;
import Entidades.SolucionesNodos;
import InterfaceAdministrar.AdministrarInterfaseContableDynamicsPLInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaActualUsuarioInterface;
import InterfacePersistencia.PersistenciaContabilizacionesInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaEmpresasInterface;
import InterfacePersistencia.PersistenciaGeneralesInterface;
import InterfacePersistencia.PersistenciaInterconDynamicsInterface;
import InterfacePersistencia.PersistenciaParametrosContablesInterface;
import InterfacePersistencia.PersistenciaParametrosEstructurasInterface;
import InterfacePersistencia.PersistenciaProcesosInterface;
import InterfacePersistencia.PersistenciaSolucionesNodosInterface;
import InterfacePersistencia.PersistenciaUsuariosInterfasesInterface;
import InterfacePersistencia.PersistenciaVWActualesFechasInterface;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

/**
 *
 * @author Administrador
 */
@Stateful
public class AdministrarInterfaseContableDynamicsPL implements AdministrarInterfaseContableDynamicsPLInterface {

   private static Logger log = Logger.getLogger(AdministrarInterfaseContableDynamicsPL.class);

   @EJB
   PersistenciaParametrosContablesInterface persistenciaParametrosContables;
   @EJB
   PersistenciaInterconDynamicsInterface persistenciaInterconDynamics;
   @EJB
   PersistenciaSolucionesNodosInterface persistenciaSolucionesNodos;
   @EJB
   PersistenciaActualUsuarioInterface persistenciaActualUsuario;
   @EJB
   PersistenciaEmpresasInterface persistenciaEmpresas;
   @EJB
   PersistenciaProcesosInterface persistenciaProcesos;
   @EJB
   PersistenciaParametrosEstructurasInterface persistenciaParametrosEstructuras;
   @EJB
   PersistenciaVWActualesFechasInterface persistenciaVWActualesFechas;
   @EJB
   PersistenciaGeneralesInterface persistenciaGenerales;
   @EJB
   PersistenciaEmpleadoInterface persistenciaEmpleados;
   @EJB
   PersistenciaContabilizacionesInterface persistenciaContabilizaciones;
   @EJB
   PersistenciaUsuariosInterfasesInterface persistenciaUsuariosInterfases;
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

   @Override
   public void obtenerConexion(String idSesion) { idSesionBck = idSesion;
      try {
         emf = administrarSesiones.obtenerConexionSesionEMF(idSesion);
      } catch (Exception e) {
         log.fatal(this.getClass().getSimpleName() + " obtenerConexion ERROR: " + e);
      }
   }

   @Override
   public List<ParametrosContables> obtenerParametrosContablesUsuarioBD(String usuarioBD) {
      try {
         List<ParametrosContables> parametro = persistenciaParametrosContables.buscarParametrosContablesUsuarioBD(getEm(), usuarioBD);
         if (parametro != null) {
            for (int i = 0; i < parametro.size(); i++) {
               Empresas empresa = persistenciaEmpresas.consultarEmpresaPorCodigo(getEm(), parametro.get(i).getEmpresaCodigo());
               if (empresa != null) {
                  parametro.get(i).setEmpresaRegistro(empresa);
               }
               if (parametro.get(i).getProceso() == null) {
                  parametro.get(i).setProceso(new Procesos());
               }
            }
         }
         return parametro;
      } catch (Exception e) {
         log.warn("Error obtenerParametroContableUsuarioBD Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public void modificarParametroContable(ParametrosContables parametro) {
      try {
         if (parametro.getProceso().getSecuencia() == null) {
            parametro.setProceso(null);
         }
         persistenciaParametrosContables.editar(getEm(), parametro);
      } catch (Exception e) {
         log.warn("Error modificarParametroContable Admi : " + e.toString());

      }
   }

   @Override
   public void borrarParametroContable(List<ParametrosContables> listPC) {
      try {
         for (int i = 0; i < listPC.size(); i++) {
            if (listPC.get(i).getProceso().getSecuencia() == null) {
               listPC.get(i).setProceso(null);
            }
            persistenciaParametrosContables.borrar(getEm(), listPC.get(i));
         }
      } catch (Exception e) {
         log.warn("Error borrarParametroContable Admi : " + e.toString());
      }
   }

   @Override
   public void crearParametroContable(ParametrosContables parametro) {
      try {
         if (parametro.getProceso().getSecuencia() == null) {
            parametro.setProceso(null);
         }
         persistenciaParametrosContables.crear(getEm(), parametro);
      } catch (Exception e) {
         log.warn("Error modificarParametroContable Admi : " + e.toString());

      }
   }

   @Override
   public List<SolucionesNodos> obtenerSolucionesNodosParametroContable(Date fechaInicial, Date fechaFinal) {
      try {
         return persistenciaSolucionesNodos.buscarSolucionesNodosParaParametroContable_Dynamics(getEm(), fechaInicial, fechaFinal);
      } catch (Exception e) {
         log.warn("Error obtenerSolucionesNodosParametroContable Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<InterconDynamics> obtenerInterconDynamicsParametroContable(Date fechaInicial, Date fechaFinal) {
      try {
         return persistenciaInterconDynamics.buscarInterconDynamicParametroContable(getEm(), fechaInicial, fechaFinal);
      } catch (Exception e) {
         log.warn("Error obtenerInterconDynamicsParametroContable Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<Procesos> lovProcesos() {
      try {
         return persistenciaProcesos.buscarProcesos(getEm());
      } catch (Exception e) {
         log.warn("Error lovProcesos Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<Empresas> lovEmpresas() {
      try {
         return persistenciaEmpresas.buscarEmpresas(getEm());
      } catch (Exception e) {
         log.warn("Error lovEmpresas Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public ActualUsuario obtenerActualUsuario() {
      try {
         return persistenciaActualUsuario.actualUsuarioBD(getEm());
      } catch (Exception e) {
         log.warn("Error obtenerActualUsuario Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public ParametrosEstructuras parametrosLiquidacion() {
      try {
         String usuarioBD = persistenciaActualUsuario.actualAliasBD(getEm());
         return persistenciaParametrosEstructuras.buscarParametro(getEm(), usuarioBD);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public Date buscarFechaHastaVWActualesFechas() {
      try {
         return persistenciaVWActualesFechas.actualFechaHasta(getEm());
      } catch (Exception e) {
         log.warn("Error buscarFechaHastaVWActualesFechas Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public Date buscarFechaDesdeVWActualesFechas() {
      try {
         return persistenciaVWActualesFechas.actualFechaDesde(getEm());
      } catch (Exception e) {
         log.warn("Error buscarFechaDesdeVWActualesFechas Admi : " + e.toString());
         return null;
      }
   }

   //@Override
   public String obtenerDescripcionProcesoArchivo(BigInteger proceso) {
      try {
         return persistenciaProcesos.obtenerDescripcionProcesoPorSecuencia(getEm(), proceso);
      } catch (Exception e) {
         log.warn("Error obtenerDescripcionProcesoArchivo Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public String obtenerPathServidorWeb() {
      try {
         return persistenciaGenerales.obtenerPathServidorWeb(getEm());
      } catch (Exception e) {
         log.warn("Error obtenerPathServidorWeb Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public String obtenerPathProceso() {
      try {
         return persistenciaGenerales.obtenerPathProceso(getEm());
      } catch (Exception e) {
         log.warn("Error obtenerPathProceso Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public int contarProcesosContabilizadosInterconDynamics(Date fechaInicial, Date fechaFinal) {
      try {
         return persistenciaInterconDynamics.contarProcesosContabilizadosInterconDynamics(getEm(), fechaInicial, fechaFinal);
      } catch (Exception e) {
         log.warn("Error contarProcesosContabilizadosInterconDynamics Admi : " + e.toString());
         return -1;
      }
   }

   @Override
   public void cerrarProcesoContable(Date fechaInicial, Date fechaFinal, BigInteger proceso, BigDecimal emplDesde, BigDecimal emplHasta) {
      try {
         persistenciaInterconDynamics.cerrarProcesoContabilizacion_PL(getEm(), fechaInicial, fechaFinal, proceso, emplDesde, emplHasta);
      } catch (Exception e) {
         log.warn("Error cerrarProcesoContable Admi : " + e.toString());
      }
   }

   @Override
   public List<Empleados> buscarEmpleadosEmpresa() {
      try {
         return persistenciaEmpleados.buscarEmpleadosActivos(getEm());
      } catch (Exception e) {
         log.warn("Error buscarEmpleadosEmpresa Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public void ejecutarPKGCrearArchivoPlano(Date fechaIni, Date fechaFin, BigInteger proceso, String descripcionProceso, String nombreArchivo, BigDecimal emplDesde, BigDecimal emplHasta) {
      try {
         persistenciaInterconDynamics.ejecutarPKGCrearArchivoPlano_PL(getEm(), fechaIni, fechaFin, proceso, descripcionProceso, nombreArchivo, emplDesde, emplHasta);
      } catch (Exception e) {
         log.warn("Error ejecutarPKGCrearArchivoPlano Admi : " + e.toString());
      }
   }

   @Override
   public Integer conteoContabilizacionesDynamics(Date fechaIni, Date fechaFin) {
      try {
         return persistenciaContabilizaciones.obtenerContadorFlagGeneradoFechasDynamics(getEm(), fechaIni, fechaFin);
      } catch (Exception e) {
         log.warn("Error conteoContabilizacionesDynamics Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public void ejecutarPKGRecontabilizar(Date fechaIni, Date fechaFin) {
      try {
         persistenciaInterconDynamics.ejecutarPKGRecontabilizar(getEm(), fechaIni, fechaFin);
      } catch (Exception e) {
         log.warn("Error ejecutarPKGRecontabilizar Admi : " + e.toString());
      }
   }

   @Override
   public void actualizarFlagContabilizacionDeshacerDynamics(Date fechaIni, Date fechaFin, BigInteger proceso, BigDecimal emplDesde, BigDecimal emplHasta) {
      try {
         persistenciaInterconDynamics.actualizarFlagContabilizacionDeshacerDynamics(getEm(), fechaIni, fechaFin, proceso, emplDesde, emplHasta);
      } catch (Exception e) {
         log.warn("Error actualizarFlagContabilizacionDeshacerDynamics Admi : " + e.toString());
      }
   }

   @Override
   public void deleteInterconDynamics(Date fechaIni, Date fechaFin, BigInteger proceso, BigDecimal emplDesde, BigDecimal emplHasta) {
      try {
         persistenciaInterconDynamics.deleteInterconDynamics(getEm(), fechaIni, fechaFin, proceso, emplDesde, emplHasta);
      } catch (Exception e) {
         log.warn("Error deleteInterconDynamics Admi : " + e.toString());
      }
   }

   @Override
   public void actualizarFlagContabilizacionDeshacerDynamics_NOT_EXITS(Date fechaIni, Date fechaFin, BigInteger proceso, BigDecimal emplDesde, BigDecimal emplHasta) {
      try {
         persistenciaInterconDynamics.actualizarFlagContabilizacionDeshacerDynamics_NOT_EXITS(getEm(), fechaIni, fechaFin, proceso, emplDesde, emplHasta);
      } catch (Exception e) {
         log.warn("Error actualizarFlagContabilizacionDeshacerDynamics Admi : " + e.toString());
      }
   }

   @Override
   public void ejecutarPKGUbicarnuevointercon_DYNAMICS(BigInteger secuencia, Date fechaIni, Date fechaFin, BigInteger proceso, BigDecimal emplDesde, BigDecimal emplHasta) {
      try {
         persistenciaInterconDynamics.ejecutarPKGUbicarnuevointercon_PLIN(getEm(), secuencia, fechaIni, fechaFin, proceso, emplDesde, emplHasta);
      } catch (Exception e) {
         log.warn("Error ejecutarPKGUbicarnuevointercon_DYNAMICS Admi : " + e.toString());
      }
   }

   @Override
   public void anularComprobantesCerrados(Date fechaIni, Date fechaFin, BigInteger proceso) {
      try {
         persistenciaInterconDynamics.anularComprobantesCerrados_PL(getEm(), fechaIni, fechaFin, proceso);
      } catch (Exception e) {
         log.warn("Error anularComprobantesCerrados Admi : " + e.toString());

      }
   }

   @Override
   public Date obtenerFechaMaxContabilizaciones() {
      try {
         return persistenciaContabilizaciones.obtenerFechaMaximaContabilizaciones(getEm());
      } catch (Exception e) {
         log.warn("Error obtenerFechaMaxContabilizaciones Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public Date obtenerFechaMaxInterconDynamics() {
      try {
         return persistenciaInterconDynamics.obtenerFechaContabilizacionMaxInterconDynamics(getEm());
      } catch (Exception e) {
         log.warn("Error obtenerFechaMaxInterconDynamics Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public void actionProcesarDatosDYNAMICSPL(short codigoEmpresa) {
      try {
         persistenciaInterconDynamics.actionProcesarDatosDYNAMICSPL(getEm(), codigoEmpresa);
      } catch (Exception e) {
         log.warn("Error actionProcesarDatosDYNAMICSPL Admi : " + e.toString());
      }
   }

   @Override
   public void actionRespuestaDYNAMICSPL(short codigoEmpresa) {
      try {
         persistenciaInterconDynamics.actionRespuestaDYNAMICSPL(getEm(), codigoEmpresa);
      } catch (Exception e) {
         log.warn("Error actionProcesarDatosDYNAMICSPL Admi : " + e.toString());
      }
   }
}
