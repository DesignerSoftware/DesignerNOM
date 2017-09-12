/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Administrar;

import Entidades.Deportes;
import InterfaceAdministrar.AdministrarDeportesInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaDeportesInterface;
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
 * 'Deportes'.
 *
 * @author betelgeuse
 */
@Stateful
public class AdministrarDeportes implements AdministrarDeportesInterface {

   private static Logger log = Logger.getLogger(AdministrarDeportes.class);

   //--------------------------------------------------------------------------
   //ATRIBUTOS
   //--------------------------------------------------------------------------    
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaDeportes'.
    */
   @EJB
   PersistenciaDeportesInterface persistenciaDeportes;
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

   @Override
   public void obtenerConexion(String idSesion) { idSesionBck = idSesion;
      try {
         emf = administrarSesiones.obtenerConexionSesionEMF(idSesion);
      } catch (Exception e) {
         log.fatal(this.getClass().getSimpleName() + " obtenerConexion ERROR: " + e);
      }
   }

   //--------------------------------------------------------------------------
   //MÉTODOS
   //--------------------------------------------------------------------------  
   @Override
   public void modificarDeportes(List<Deportes> listDeportesModificadas) {
      try {
         for (int i = 0; i < listDeportesModificadas.size(); i++) {
            log.warn("Administrar Modificando...");
            Deportes deporteSeleccionado = listDeportesModificadas.get(i);
            persistenciaDeportes.editar(getEm(), deporteSeleccionado);
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".modificarDeportes() ERROR: " + e);
      }
   }

   @Override
   public void borrarDeportes(List<Deportes> listaDeportes) {
      try {
         for (int i = 0; i < listaDeportes.size(); i++) {
            log.warn("Borrando...");
            persistenciaDeportes.borrar(getEm(), listaDeportes.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".borrarDeportes() ERROR: " + e);
      }
   }

   @Override
   public void crearDeportes(List<Deportes> listaDeportes) {
      try {
         for (int i = 0; i < listaDeportes.size(); i++) {
            log.warn("Creando...");
            persistenciaDeportes.crear(getEm(), listaDeportes.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".crearDeportes() ERROR: " + e);
      }
   }

   @Override
   public List<Deportes> consultarDeportes() {
      try {
         return persistenciaDeportes.buscarDeportes(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarDeportes() ERROR: " + e);
         return null;
      }
   }

   @Override
   public Deportes consultarDeporte(BigInteger secDeportes) {
      try {
         return persistenciaDeportes.buscarDeporte(getEm(), secDeportes);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarDeporte() ERROR: " + e);
         return null;
      }
   }

   @Override
   public BigInteger contarVigenciasDeportesDeporte(BigInteger secDeporte) {
      try {
         return persistenciaDeportes.verificarBorradoVigenciasDeportes(getEm(), secDeporte);
      } catch (Exception e) {
         log.error("ERROR ADMINISTRARDEPORTES verificarBorradoVigenciasDeportes ERROR :" + e);
         return null;
      }
   }

   @Override
   public BigInteger contarPersonasDeporte(BigInteger secDeporte) {
      try {
         return persistenciaDeportes.contadorDeportesPersonas(getEm(), secDeporte);
      } catch (Exception e) {
         log.error("ERROR ADMINISTRARDEPORTES contadorDeportesPersonas ERROR :" + e);
         return null;
      }
   }

   @Override
   public BigInteger contarParametrosInformesDeportes(BigInteger secDeporte) {
      try {
         return persistenciaDeportes.contadorParametrosInformes(getEm(), secDeporte);
      } catch (Exception e) {
         log.error("ERROR ADMINISTRARDEPORTES contadorParametrosInformes ERROR :" + e);
         return null;
      }
   }
}
