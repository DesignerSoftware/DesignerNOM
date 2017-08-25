package Administrar;

import Entidades.CambiosMasivos;
import Entidades.Empleados;
import Entidades.Estructuras;
import Entidades.Parametros;
import Entidades.ParametrosCambiosMasivos;
import Entidades.ParametrosEstructuras;
import Entidades.Procesos;
import Entidades.TiposTrabajadores;
import Entidades.Usuarios;
import InterfaceAdministrar.AdministrarParametrosInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaActualUsuarioInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaEstructurasInterface;
import InterfacePersistencia.PersistenciaParametrosEstadosInterface;
import InterfacePersistencia.PersistenciaParametrosEstructurasInterface;
import InterfacePersistencia.PersistenciaParametrosInterface;
import InterfacePersistencia.PersistenciaProcesosInterface;
import InterfacePersistencia.PersistenciaTiposTrabajadoresInterface;
import InterfacePersistencia.PersistenciaUsuariosInterface;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

@Stateful
public class AdministrarParametros implements AdministrarParametrosInterface {

   private static Logger log = Logger.getLogger(AdministrarParametros.class);

   @EJB
   PersistenciaParametrosEstructurasInterface persistenciaParametrosEstructuras;
   @EJB
   PersistenciaActualUsuarioInterface persistenciaActualUsuario;
   @EJB
   PersistenciaEstructurasInterface persistenciaEstructuras;
   @EJB
   PersistenciaTiposTrabajadoresInterface persistenciaTiposTrabajadores;
   @EJB
   PersistenciaProcesosInterface persistenciaProcesos;
   @EJB
   PersistenciaParametrosInterface persistenciaParametros;
   @EJB
   PersistenciaParametrosEstadosInterface persistenciaParametrosEstados;
   @EJB
   PersistenciaUsuariosInterface persistenciaUsuarios;
   @EJB
   PersistenciaEmpleadoInterface persistenciaEmpleado;

   /**
    * Enterprise JavaBean.<br>
    * Atributo que representa todo lo referente a la conexión del usuario que
    * está usando el aplicativo.
    */
   @EJB
   AdministrarSesionesInterface administrarSesiones;

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

   @Override
   public void obtenerConexion(String idSesion) {
      try {
         emf = administrarSesiones.obtenerConexionSesionEMF(idSesion);
      } catch (Exception e) {
         log.fatal(this.getClass().getSimpleName() + " obtenerConexion ERROR: " + e);
      }
   }

   public Usuarios usuarioActual() {
      try {
         String usuarioBD = persistenciaActualUsuario.actualAliasBD(getEm());
         return persistenciaUsuarios.buscarUsuario(getEm(), usuarioBD);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
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
   public List<Estructuras> lovEstructuras() {
      try {
         return persistenciaEstructuras.estructuras(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   public List<TiposTrabajadores> lovTiposTrabajadores() {
      try {
         return persistenciaTiposTrabajadores.buscarTiposTrabajadores(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<Procesos> lovProcesos(String aut) {
      try {
         return persistenciaProcesos.procesosParametros(getEm(), aut);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<Parametros> empleadosParametros() {
      try {
         String usuarioBD = persistenciaActualUsuario.actualAliasBD(getEm());
         return persistenciaParametros.empleadosParametros(getEm(), usuarioBD);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public String estadoParametro(BigInteger secuenciaParametro) {
      try {
         return persistenciaParametrosEstados.parametrosComprobantes(getEm(), secuenciaParametro);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   public void crearParametroEstructura(ParametrosEstructuras parametroEstructura) {
      try {
         persistenciaParametrosEstructuras.editar(getEm(), parametroEstructura);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   public void eliminarParametros(List<Parametros> listaParametros) {
      try {
         for (int i = 0; i < listaParametros.size(); i++) {
            persistenciaParametros.borrar(getEm(), listaParametros.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   public void crearParametros(List<Parametros> listaParametros) {
      try {
         for (int i = 0; i < listaParametros.size(); i++) {
            persistenciaParametros.crear(getEm(), listaParametros.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void adicionarEmpleados(BigInteger secParametroEstructura) {
      try {
         persistenciaParametrosEstructuras.adicionarEmpleados(getEm(), secParametroEstructura);
      } catch (Exception e) {
         log.warn(this.getClass().getName() + " adicionarEmpleados() Entro al Catch");
         log.warn("Error : " + e);
      }
   }

   public void borrarParametros(BigInteger secParametroEstructura) {
      try {
         persistenciaParametros.borrarParametros(getEm(), secParametroEstructura);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   public Integer empleadosParametrizados(BigInteger secProceso) {
      try {
         return persistenciaParametrosEstructuras.empleadosParametrizados(getEm(), secProceso);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   public Integer diferenciaDias(String fechaInicial, String fechaFinal) {
      try {
         return persistenciaParametrosEstructuras.diasDiferenciaFechas(getEm(), fechaInicial, fechaFinal);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<Empleados> empleadosLov() {
      try {
         return persistenciaEmpleado.buscarEmpleadosActivosPensionados(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   // Para Cambios Masivos : 
//   @Override
//   public List<Parametros> consultarEmpleadosParametros() {
//      log.warn("Administrar.AdministrarCambiosMasivos.consultarEmpleadosParametros()");
//      try {
//         String usuarioBD = persistenciaActualUsuario.actualAliasBD(getEm());
//         return persistenciaParametros.empleadosParametros(getEm(), usuarioBD);
//      } catch (Exception e) {
//         log.warn("ERROR Administrar.AdministrarCambiosMasivos.consultarEmpleadosParametros()");
//         log.warn("ERROR : " + e);
//         return null;
//      }
//   }
   @Override
   public List<CambiosMasivos> consultarUltimosCambiosMasivos() {
      log.warn("Administrar.AdministrarCambiosMasivos.consultarUltimosCambiosMasivos()");
      try {
         return new ArrayList<CambiosMasivos>();
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
         return new ParametrosCambiosMasivos();
      } catch (Exception e) {
         log.warn("ERROR Administrar.AdministrarCambiosMasivos.consultarParametrosCambiosMasivos()");
         log.warn("ERROR : " + e);
         return null;
      }
   }
}
