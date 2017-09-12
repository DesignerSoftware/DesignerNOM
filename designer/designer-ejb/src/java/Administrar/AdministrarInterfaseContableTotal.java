package Administrar;

import Entidades.ActualUsuario;
import Entidades.Empresas;
import Entidades.InterconTotal;
import Entidades.ParametrosContables;
import Entidades.ParametrosEstructuras;
import Entidades.Procesos;
import Entidades.SolucionesNodos;
import Entidades.UsuariosInterfases;
import InterfaceAdministrar.AdministrarInterfaseContableTotalInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaActualUsuarioInterface;
import InterfacePersistencia.PersistenciaContabilizacionesInterface;
import InterfacePersistencia.PersistenciaEmpresasInterface;
import InterfacePersistencia.PersistenciaGeneralesInterface;
import InterfacePersistencia.PersistenciaInforeportesInterface;
import InterfacePersistencia.PersistenciaInterconTotalInterface;
import InterfacePersistencia.PersistenciaParametrosContablesInterface;
import InterfacePersistencia.PersistenciaParametrosEstructurasInterface;
import InterfacePersistencia.PersistenciaProcesosInterface;
import InterfacePersistencia.PersistenciaSolucionesNodosInterface;
import InterfacePersistencia.PersistenciaTercerosInterface;
import InterfacePersistencia.PersistenciaUsuariosInterfasesInterface;
import InterfacePersistencia.PersistenciaVWActualesFechasInterface;
import excepciones.ExcepcionBD;
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
public class AdministrarInterfaseContableTotal implements AdministrarInterfaseContableTotalInterface {

   private static Logger log = Logger.getLogger(AdministrarInterfaseContableTotal.class);

   @EJB
   PersistenciaParametrosContablesInterface persistenciaParametrosContables;
   @EJB
   PersistenciaInterconTotalInterface persistenciaInterconTotal;
   @EJB
   PersistenciaSolucionesNodosInterface persistenciaSolucionesNodos;
   @EJB
   PersistenciaActualUsuarioInterface persistenciaActualUsuario;
   @EJB
   PersistenciaEmpresasInterface persistenciaEmpresas;
   @EJB
   PersistenciaProcesosInterface persistenciaProcesos;
   @EJB
   AdministrarSesionesInterface administrarSesiones;
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
   @EJB
   PersistenciaInforeportesInterface persistenciaReportes;

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
         List<ParametrosContables> listaParametros = persistenciaParametrosContables.buscarParametrosContablesUsuarioBD(getEm(), usuarioBD);
         int n = 0;
         if (listaParametros == null) {
            n++;
         } else if (listaParametros.isEmpty()) {
            n++;
         }

         if (n > 0) {
            Empresas empresaAux = persistenciaEmpresas.consultarPrimeraEmpresaSinPaquete(getEm());
            ParametrosContables parametro = new ParametrosContables();
            parametro.setSecuencia(BigInteger.ONE);
            parametro.setEmpresaCodigo(empresaAux.getCodigo());
            parametro.setEmpresaRegistro(empresaAux);
            parametro.setUsuario(usuarioBD);
            parametro.setFechafinalcontabilizacion(new Date());
            parametro.setFechainicialcontabilizacion(new Date());
            parametro.setArchivo("NOMINA");
            listaParametros.add(parametro);
         }

         if (listaParametros != null) {
            if (!listaParametros.isEmpty()) {
               for (int i = 0; i < listaParametros.size(); i++) {
                  Empresas empresa = null;
                  if (listaParametros.get(i).getEmpresaRegistro() == null) {
                     empresa = persistenciaEmpresas.consultarEmpresaPorCodigo(getEm(), listaParametros.get(i).getEmpresaCodigo());
                  }
                  if (empresa != null) {
                     listaParametros.get(i).setEmpresaRegistro(empresa);
                  }
                  if (listaParametros.get(i).getProceso() == null) {
                     listaParametros.get(i).setProceso(new Procesos());
                  }
               }
            }
         }
         return listaParametros;
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
            parametro.setProceso(new Procesos());
         }
         persistenciaParametrosContables.crear(getEm(), parametro);
      } catch (Exception e) {
         log.warn("Error modificarParametroContable Admi : " + e.toString());

      }
   }

   @Override
   public List<SolucionesNodos> obtenerSolucionesNodosParametroContable(Date fechaInicial, Date fechaFinal) {
      try {
         return persistenciaSolucionesNodos.buscarSolucionesNodosParaParametroContable(getEm(), fechaInicial, fechaFinal);
      } catch (Exception e) {
         log.warn("Error obtenerSolucionesNodosParametroContable Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<InterconTotal> obtenerInterconTotalParametroContable(Date fechaInicial, Date fechaFinal) {
      try {
         return persistenciaInterconTotal.buscarInterconTotalParaParametroContable(getEm(), fechaInicial, fechaFinal);
      } catch (Exception e) {
         log.warn("Error obtenerInterconTotalParametroContable Admi : " + e.toString());
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
   public Date obtenerFechaMaxContabilizaciones() {
      try {
         return persistenciaContabilizaciones.obtenerFechaMaximaContabilizaciones(getEm());
      } catch (Exception e) {
         log.warn("Error obtenerFechaMaxContabilizaciones Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public Date obtenerFechaMaxInterconTotal() {
      try {
         return persistenciaInterconTotal.obtenerFechaContabilizacionMaxInterconTotal(getEm());
      } catch (Exception e) {
         log.warn("Error obtenerFechaMaxInterconTotal Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public ParametrosEstructuras parametrosLiquidacion() {
      String usuarioBD = persistenciaActualUsuario.actualAliasBD(getEm());
      return persistenciaParametrosEstructuras.buscarParametro(getEm(), usuarioBD);
   }

   @Override
   public void actualizarFlagInterconTotal(Date fechaInicial, Date fechaFinal, Short empresa) {
      try {
         persistenciaInterconTotal.actualizarFlagInterconTotal(getEm(), fechaInicial, fechaFinal, empresa);
      } catch (Exception e) {
         log.warn("Error actualizarFlagInterconTotal Admi : " + e.toString());
      }
   }

   @Override
   public void actualizarFlagInterconTotalProcesoDeshacer(Date fechaInicial, Date fechaFinal, BigInteger proceso) {
      try {
         persistenciaInterconTotal.actualizarFlagInterconTotalProcesoDeshacer(getEm(), fechaInicial, fechaFinal, proceso);
      } catch (Exception e) {
         log.warn("Error actualizarFlagInterconTotalProcesoDeshacer Admi : " + e.toString());
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
   public void ejcutarPKGUbicarnuevointercon_total(BigInteger secuencia, Date fechaInicial, Date fechaFinal, BigInteger proceso) {
      try {
         persistenciaInterconTotal.ejecutarPKGUbicarnuevointercon_total(getEm(), secuencia, fechaInicial, fechaFinal, proceso);
      } catch (Exception e) {
         log.warn("Error ejcutarPKGUbicarnuevointercon_total Admi : " + e.toString());
      }
   }

   @Override
   public void eliminarInterconTotal(Date fechaInicial, Date fechaFinal, Short empresa, BigInteger proceso) {
      try {
         persistenciaInterconTotal.eliminarInterconTotal(getEm(), fechaInicial, fechaFinal, empresa, proceso);
      } catch (Exception e) {
         log.warn("Error eliminarInterconTotal Admi : " + e.toString());
      }
   }

   @Override
   public int contarProcesosContabilizadosInterconTotal(Date fechaInicial, Date fechaFinal) {
      try {
         return persistenciaInterconTotal.contarProcesosContabilizadosInterconTotal(getEm(), fechaInicial, fechaFinal);
      } catch (Exception e) {
         log.warn("Error contarProcesosContabilizadosInterconTotal Admi : " + e.toString());
         return -1;
      }
   }

   @Override
   public void cerrarProcesoContabilizacion(Date fechaInicial, Date fechaFinal, Short empresa, BigInteger proceso) {
      try {
         persistenciaInterconTotal.cerrarProcesoContabilizacion(getEm(), fechaInicial, fechaFinal, empresa, proceso);
      } catch (Exception e) {
         log.warn("Error cerrarProcesoContabilizacion Admi : " + e.toString());
      }
   }

   @Override
   public Integer obtenerContadorFlagGeneradoFechasTotal(Date fechaIni, Date fechaFin) {
      try {
         return persistenciaContabilizaciones.obtenerContadorFlagGeneradoFechasTotal(getEm(), fechaIni, fechaFin);
      } catch (Exception e) {
         log.warn("Error obtenerContadorFlagGeneradoFechasTotal Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public void ejecutarPKGRecontabilizacion(Date fechaIni, Date fechaFin) throws ExcepcionBD {
      try {
         persistenciaInterconTotal.ejecutarPKGRecontabilizacion(getEm(), fechaIni, fechaFin);
      } catch (ExcepcionBD ebd) {
         log.warn("Error ejecutarPKGRecontabilizacion Admi ebd : " + ebd.toString());
         throw ebd;
      } catch (Exception e) {
         log.warn("Error ejecutarPKGRecontabilizacion Admi e : " + e.toString());
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
   public String ejecutarPKGCrearArchivoPlano(int tipoArchivo, Date fechaIni, Date fechaFin, BigInteger proceso, String nombreArchivo) {
      try {
         return persistenciaInterconTotal.ejecutarPKGCrearArchivoPlano(getEm(), tipoArchivo, fechaIni, fechaFin, proceso, nombreArchivo);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".ejecutarPKGCrearArchivoPlano() ERROR: " + e);
         return null;
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

   @Override
   public void borrarRegistroGenerado(List<SolucionesNodos> listBorrar) {
      try {
         for (int i = 0; i < listBorrar.size(); i++) {
            persistenciaSolucionesNodos.borrar(getEm(), listBorrar.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".borrarRegistroGenerado() ERROR: " + e);
      }
   }

   @Override
   public void borrarRegistroIntercon(List<InterconTotal> listBorrar) {
      try {
         for (int i = 0; i < listBorrar.size(); i++) {
            persistenciaInterconTotal.borrar(getEm(), listBorrar.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".borrarRegistroIntercon() ERROR: " + e);
      }
   }
}
