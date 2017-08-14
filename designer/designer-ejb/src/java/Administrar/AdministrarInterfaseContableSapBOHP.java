package Administrar;

import Entidades.ActualUsuario;
import Entidades.Empresas;
import Entidades.InterconSapBO;
import Entidades.ParametrosContables;
import Entidades.ParametrosEstructuras;
import Entidades.Procesos;
import Entidades.SolucionesNodos;
import Entidades.UsuariosInterfases;
import InterfaceAdministrar.AdministrarInterfaseContableSapBOHPInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaActualUsuarioInterface;
import InterfacePersistencia.PersistenciaContabilizacionesInterface;
import InterfacePersistencia.PersistenciaEmpresasInterface;
import InterfacePersistencia.PersistenciaGeneralesInterface;
import InterfacePersistencia.PersistenciaInterconSapBOInterface;
import InterfacePersistencia.PersistenciaParametrosContablesInterface;
import InterfacePersistencia.PersistenciaParametrosEstructurasInterface;
import InterfacePersistencia.PersistenciaProcesosInterface;
import InterfacePersistencia.PersistenciaSolucionesNodosInterface;
import InterfacePersistencia.PersistenciaTercerosInterface;
import InterfacePersistencia.PersistenciaUsuariosInterfasesInterface;
import InterfacePersistencia.PersistenciaVWActualesFechasInterface;
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
public class AdministrarInterfaseContableSapBOHP implements AdministrarInterfaseContableSapBOHPInterface {

   private static Logger log = Logger.getLogger(AdministrarInterfaseContableSapBOHP.class);

   @EJB
   AdministrarSesionesInterface administrarSesiones;
   @EJB
   PersistenciaParametrosContablesInterface persistenciaParametrosContables;
   @EJB
   PersistenciaActualUsuarioInterface persistenciaActualUsuario;
   @EJB
   PersistenciaProcesosInterface persistenciaProcesos;
   @EJB
   PersistenciaEmpresasInterface persistenciaEmpresas;
   @EJB
   PersistenciaSolucionesNodosInterface persistenciaSolucionesNodos;
   @EJB
   PersistenciaInterconSapBOInterface persistenciaInterconSap;
   @EJB
   PersistenciaTercerosInterface persistenciaTerceros;
   @EJB
   PersistenciaContabilizacionesInterface persistenciaContabilizaciones;
   @EJB
   PersistenciaParametrosEstructurasInterface persistenciaParametrosEstructuras;
   @EJB
   PersistenciaVWActualesFechasInterface persistenciaVWActualesFechas;
   @EJB
   PersistenciaUsuariosInterfasesInterface persistenciaUsuariosInterfases;
   @EJB
   PersistenciaGeneralesInterface persistenciaGenerales;

   private EntityManagerFactory emf;
   private EntityManager em;

   private EntityManager getEm() {
      try {
         if (this.em != null) {
            if (this.em.isOpen()) {
               this.em.close();
            }
         }
         this.em = emf.createEntityManager();
      } catch (Exception e) {
         log.fatal(this.getClass().getSimpleName() + " getEm() ERROR : " + e);
      }
      return this.em;
   }

   //@Override
   public void obtenerConexion(String idSesion) {
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
         log.warn("Error obtenerParametrosContablesUsuarioBD Admi : " + e.toString());
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
         return persistenciaSolucionesNodos.buscarSolucionesNodosParaParametroContable_SAP(getEm(), fechaInicial, fechaFinal);
      } catch (Exception e) {
         log.warn("Error obtenerSolucionesNodosParametroContable Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<InterconSapBO> obtenerInterconSapBOHPParametroContable(Date fechaInicial, Date fechaFinal) {
      try {
         return persistenciaInterconSap.buscarInterconSAPBOParametroContable(getEm(), fechaInicial, fechaFinal);
      } catch (Exception e) {
         log.warn("Error obtenerInterconSapBOHPParametroContable Admi : " + e.toString());
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
   public Date obtenerMaxFechaContabilizaciones() {
      try {
         return persistenciaContabilizaciones.obtenerFechaMaximaContabilizacionesSAPBOV8(getEm());
      } catch (Exception e) {
         log.warn("Error obtenerMaxFechaContabilizaciones Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public Date obtenerMaxFechaIntercoSapBO() {
      try {
         return persistenciaInterconSap.obtenerFechaMaxInterconSAPBO(getEm());
      } catch (Exception e) {
         log.warn("Error obtenerMaxFechaIntercoSapBO Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public ParametrosEstructuras parametrosLiquidacion() {
      try {
         String usuarioBD = persistenciaActualUsuario.actualAliasBD(getEm());
         return persistenciaParametrosEstructuras.buscarParametro(getEm(), usuarioBD);
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public void actualizarFlagProcesoAnularInterfaseContableSAPBOHP(Date fechaIni, Date fechaFin) {
      try {
         persistenciaInterconSap.actualizarFlagProcesoAnularInterfaseContableSAPBOV8(getEm(), fechaIni, fechaFin);
      } catch (Exception e) {
         log.warn("Error actualizarFlagProcesoAnularInterfaseContableSAPBOHP Admi : " + e.toString());
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

   @Override
   public void cerrarProcesoLiquidacion(Date fechaIni, Date fechaFin, BigInteger proceso) {
      try {
         persistenciaInterconSap.cerrarProcesoLiquidacion(getEm(), fechaIni, fechaFin, proceso);
      } catch (Exception e) {
         log.warn("Error cerrarProcesoLiquidacion Admi : " + e.toString());
      }
   }

   @Override
   public void cambiarFlagInterconContableSAPBOHP(Date fechaIni, Date fechaFin, BigInteger proceso) {
      try {
         persistenciaContabilizaciones.actualizarFlahInterconContableSAPBOV8(getEm(), fechaIni, fechaFin, proceso);
      } catch (Exception e) {
         log.warn("Error cambiarFlagInterconContableSAPBOHP Admi : " + e.toString());
      }
   }

   @Override
   public void ejecutarDeleteInterconSAP(Date fechaIni, Date fechaFin, BigInteger proceso) {
      try {
         persistenciaInterconSap.ejecutarDeleteInterconSAPBOV8(getEm(), fechaIni, fechaFin, proceso);
      } catch (Exception e) {
         log.warn("Error ejecutarDeleteInterconSAP Admi : " + e.toString());
      }
   }

   @Override
   public void ejeuctarPKGUbicarnuevointercon_SAPBOVHP(BigInteger secuencia, Date fechaIni, Date fechaFin, BigInteger proceso) {
      try {
         persistenciaInterconSap.ejeuctarPKGUbicarnuevointercon_SAPBOVHP(getEm(), secuencia, fechaIni, fechaFin, proceso);
      } catch (Exception e) {
         log.warn("Error ejeuctarPKGUbicarnuevointercon_SAPBOVHP Admi : " + e.toString());
      }
   }

   @Override
   public int contarProcesosContabilizadosInterconSAPBO(Date fechaInicial, Date fechaFinal) {
      try {
         return persistenciaInterconSap.contarProcesosContabilizadosInterconSAPBO(getEm(), fechaInicial, fechaFinal);
      } catch (Exception e) {
         log.warn("Error contarProcesosContabilizadosInterconSAPBO Admi : " + e.toString());
         return -1;
      }
   }

   @Override
   public Integer obtenerContadorFlagGeneradoFechasSAP(Date fechaIni, Date fechaFin) {
      try {
         return persistenciaContabilizaciones.obtenerContadorFlagGeneradoFechasSAP(getEm(), fechaIni, fechaFin);
      } catch (Exception e) {
         log.warn("Error obtenerContadorFlagGeneradoFechasSAP Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public void ejecutarPKGRecontabilizacion(Date fechaIni, Date fechaFin) {
      try {
         persistenciaInterconSap.ejecutarPKGRecontabilizacion(getEm(), fechaIni, fechaFin);
      } catch (Exception e) {
         log.warn("Error ejecutarPKGRecontabilizacion Admi : " + e.toString());
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
   public void ejecutarPKGCrearArchivoPlano(Date fechaIni, Date fechaFin, BigInteger proceso, String descripcion, String nombreArchivo) {
      try {
         persistenciaInterconSap.ejecutarPKGCrearArchivoPlanoSAPHP(getEm(), fechaIni, fechaFin, proceso, descripcion, nombreArchivo);
      } catch (Exception e) {
         log.warn("Error ejecutarPKGCrearArchivoPlano Admi : " + e.toString());
      }
   }

   @Override
   public UsuariosInterfases obtenerUsuarioInterfaseContabilizacion() {
      try {
         return persistenciaUsuariosInterfases.obtenerUsuarioInterfaseContabilidad(getEm());
      } catch (Exception e) {
         log.warn("Error obtenerUsuarioInterfaseContabilizacion Admi : " + e.toString());
         return null;
      }
   }
}
