/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Administrar;

import Entidades.AdiestramientosNF;
import InterfaceAdministrar.AdministrarAdiestramientosNFInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaAdiestramientosNFInterface;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

/**
 * Clase Stateful. <br>
 * Clase encargada de realizar las operaciones lógicas para la pantalla
 * 'AdiestramientosNF'.
 *
 * @author betelgeuse
 */


@Stateful
public class AdministrarAdiestramientosNF implements AdministrarAdiestramientosNFInterface {

   private static Logger log = Logger.getLogger(AdministrarAdiestramientosNF.class);
   //--------------------------------------------------------------------------
   //ATRIBUTOS
   //--------------------------------------------------------------------------    
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaAdiestramientosNF'.
    */
   @EJB
   PersistenciaAdiestramientosNFInterface persistenciaAdiestramientosNF;
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
   public List<AdiestramientosNF> consultarAdiestramientosNF() {
      try {
         return persistenciaAdiestramientosNF.adiestramientosNF(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarAdiestramientosNF() ERROR: " + e);
         return null;
      }
   }
}
