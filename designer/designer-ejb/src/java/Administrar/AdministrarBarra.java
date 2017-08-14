/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Administrar;

import Entidades.ConsultasLiquidaciones;
import Entidades.ParametrosEstructuras;
import InterfaceAdministrar.AdministrarBarraInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaActualUsuarioInterface;
import InterfacePersistencia.PersistenciaCandadosInterface;
import InterfacePersistencia.PersistenciaConsultasLiquidacionesInterface;
import InterfacePersistencia.PersistenciaEmpresasInterface;
import InterfacePersistencia.PersistenciaParametrosEstadosInterface;
import InterfacePersistencia.PersistenciaParametrosEstructurasInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

/**
 * Clase Stateful. <br> Clase encargada de realizar las operaciones lógicas para
 * la pantalla 'Barra'.
 *
 * @author betelgeuse
 */
@Stateful
public class AdministrarBarra implements AdministrarBarraInterface {

   private static Logger log = Logger.getLogger(AdministrarBarra.class);
   //--------------------------------------------------------------------------
   //ATRIBUTOS
   //--------------------------------------------------------------------------    

   /**
    * Enterprise JavaBeans.<br> Atributo que representa la comunicación con la
    * persistencia 'persistenciaParametrosEstados'.
    */
   @EJB
   PersistenciaParametrosEstadosInterface persistenciaParametrosEstados;
   /**
    * Enterprise JavaBeans.<br> Atributo que representa la comunicación con la
    * persistencia 'persistenciaCandados'.
    */
   @EJB
   PersistenciaCandadosInterface persistenciaCandados;
   /**
    * Enterprise JavaBeans.<br> Atributo que representa la comunicación con la
    * persistencia 'persistenciaActualUsuario'.
    */
   @EJB
   PersistenciaActualUsuarioInterface persistenciaActualUsuario;
   /**
    * Enterprise JavaBeans.<br> Atributo que representa la comunicación con la
    * persistencia 'persistenciaParametrosEstructuras'.
    */
   @EJB
   PersistenciaParametrosEstructurasInterface persistenciaParametrosEstructuras;
   /**
    * Enterprise JavaBeans.<br> Atributo que representa la comunicación con la
    * persistencia 'persistenciaConsultasLiquidaciones'.
    */
   @EJB
   PersistenciaConsultasLiquidacionesInterface persistenciaConsultasLiquidaciones;
   /**
    * Enterprise JavaBeans.<br> Atributo que representa la comunicación con la
    * persistencia 'persistenciaEmpresas'.
    */
   @EJB
   PersistenciaEmpresasInterface persistenciaEmpresas;
   /**
    * Enterprise JavaBean.<br> Atributo que representa todo lo referente a la
    * conexión del usuario que está usando el aplicativo.
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
   //--------------------------------------------------------------------------
   //MÉTODOS
   //--------------------------------------------------------------------------

   @Override
   public void obtenerConexion(String idSesion) {
      try {
         emf = administrarSesiones.obtenerConexionSesionEMF(idSesion);
      } catch (Exception e) {
         log.fatal(this.getClass().getSimpleName() + " obtenerConexion ERROR: " + e);
      }
   }

   @Override
   public Integer contarEmpleadosParaLiquidar() {
      try {
         EntityManager em = emf.createEntityManager();
         Integer i = persistenciaParametrosEstados.empleadosParaLiquidar(em, consultarUsuarioBD());
         em.close();
         return i;
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public Integer contarEmpleadosLiquidados() {
      try {
         return persistenciaParametrosEstados.empleadosLiquidados(getEm(), consultarUsuarioBD());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public boolean verificarPermisosLiquidar(String usuarioBD) {
      try {
         return persistenciaCandados.permisoLiquidar(getEm(), usuarioBD);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return false;
      }
   }

   @Override
   public String consultarUsuarioBD() {
      try {
         return persistenciaActualUsuario.actualAliasBD(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public void liquidarNomina() {
      try {
         persistenciaCandados.liquidar(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public String consultarEstadoLiquidacion(String usuarioBD) {
      try {
         return persistenciaCandados.estadoLiquidacion(getEm(), usuarioBD);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public ParametrosEstructuras consultarParametrosLiquidacion() {
      try {
         return persistenciaParametrosEstructuras.buscarParametro(getEm(), consultarUsuarioBD());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public void inicializarParametrosEstados() {
      try {
         persistenciaParametrosEstados.inicializarParametrosEstados(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public Integer consultarProgresoLiquidacion(Integer totalEmpleadoALiquidar) {
      try {
         return persistenciaCandados.progresoLiquidacion(getEm(), totalEmpleadoALiquidar);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public void cancelarLiquidacion(String usuarioBD) {
      try {
         persistenciaCandados.cancelarLiquidacion(getEm(), usuarioBD);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public List<ConsultasLiquidaciones> liquidacionesCerradas(String fechaInicial, String fechaFinal) {
      try {
         log.warn("AdministrarBarra liquidacionesCerradas() fechaInicial : " + fechaInicial);
         log.warn("AdministrarBarra liquidacionesCerradas() fechaFinal : " + fechaFinal);
         return persistenciaConsultasLiquidaciones.liquidacionesCerradas(getEm(), fechaInicial, fechaFinal);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<ConsultasLiquidaciones> consultarPreNomina() {
      try {
         return persistenciaConsultasLiquidaciones.preNomina(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public String consultarEstadoConsultaDatos(BigInteger secuenciaEmpresa) {
      try {
         return persistenciaEmpresas.estadoConsultaDatos(getEm(), secuenciaEmpresa);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

}
