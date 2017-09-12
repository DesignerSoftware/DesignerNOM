/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Administrar;

import Entidades.Empleados;
import Entidades.VWAcumulados;
import InterfaceAdministrar.AdministrarEmplAcumuladosInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaVWAcumuladosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

/**
 * Clase Stateful. <br>
 * Clase encargada de realizar las operaciones lógicas para la pantalla
 * 'EmplAcumulados'.
 *
 * @author betelgeuse
 */
@Stateful
public class AdministrarEmplAcumulados implements AdministrarEmplAcumuladosInterface {

   private static Logger log = Logger.getLogger(AdministrarEmplAcumulados.class);

   //--------------------------------------------------------------------------
   //ATRIBUTOS
   //--------------------------------------------------------------------------    
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaVWAcumulados'.
    */
   @EJB
   PersistenciaVWAcumuladosInterface persistenciaVWAcumulados;
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaEmpleados'.
    */
   @EJB
   PersistenciaEmpleadoInterface persistenciaEmpleados;
   /**
    * Enterprise JavaBean.<br>
    * Atributo que representa todo lo referente a la conexión del usuario que
    * está usando el aplicativo.
    */
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

   //--------------------------------------------------------------------------
   //MÉTODOS
   //--------------------------------------------------------------------------
   @Override
   public void obtenerConexion(String idSesion) { idSesionBck = idSesion;
      try {
         emf = administrarSesiones.obtenerConexionSesionEMF(idSesion);
      } catch (Exception e) {
         log.fatal(this.getClass().getSimpleName() + " obtenerConexion ERROR: " + e);
      }
   }

   @Override
   public List<VWAcumulados> consultarVWAcumuladosEmpleado(BigInteger secEmpleado) {
      try {
         return persistenciaVWAcumulados.buscarAcumuladosPorEmpleado(getEm(), secEmpleado);
      } catch (Exception e) {
         log.error("ERROR EN ADMINISTRAR EMPLACUMULADOS ERROR " + e);
         return null;
      }
   }

   @Override
   public Empleados consultarEmpleado(BigInteger secEmplado) {
      try {
         return persistenciaEmpleados.buscarEmpleadoSecuencia(getEm(), secEmplado);
      } catch (Exception e) {
         log.error("ERROR Administrar emplAcumulados ERROR : " + e);
         return null;
      }
   }

   @Override
   public Long getTotalRegistros(BigInteger secUsuario) {
      try {
         return persistenciaVWAcumulados.getTotalRegistros(getEm(), secUsuario);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".getTotalRegistros() ERROR: " + e);
         return null;
      }
   }
}
