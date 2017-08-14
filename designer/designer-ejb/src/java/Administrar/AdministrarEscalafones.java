/**
 * Documentación a cargo de Andres Pineda
 */
package Administrar;

import Entidades.Categorias;
import Entidades.Escalafones;
import Entidades.SubCategorias;
import InterfaceAdministrar.AdministrarEscalafonesInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaCategoriasInterface;
import InterfacePersistencia.PersistenciaEscalafonesInterface;
import InterfacePersistencia.PersistenciaSubCategoriasInterface;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

/**
 * Clase Stateful. <br>
 * Clase encargada de realizar las operaciones lógicas para la pantalla
 * 'Escalafon'.
 *
 * @author AndresPineda
 */
@Stateless
public class AdministrarEscalafones implements AdministrarEscalafonesInterface {

   private static Logger log = Logger.getLogger(AdministrarEscalafones.class);

   //--------------------------------------------------------------------------
   //ATRIBUTOS
   //--------------------------------------------------------------------------    
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaEscalafones'.
    */
   @EJB
   PersistenciaEscalafonesInterface persistenciaEscalafones;
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaCategorias'.
    */
   @EJB
   PersistenciaCategoriasInterface persistenciaCategorias;
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaSubCategorias'.
    */
   @EJB
   PersistenciaSubCategoriasInterface persistenciaSubCategorias;
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

   //--------------------------------------------------------------------------
   //MÉTODOS
   //--------------------------------------------------------------------------    
   //@Override
   public List<Escalafones> listaEscalafones() {
      try {
         return persistenciaEscalafones.buscarEscalafones(getEm());
      } catch (Exception e) {
         log.warn("Error listaEscalafones Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public void crearEscalafones(List<Escalafones> listaE) {
      try {
         for (int i = 0; i < listaE.size(); i++) {
            persistenciaEscalafones.crear(getEm(), listaE.get(i));
         }
      } catch (Exception e) {
         log.warn("Error crearEscalafones Admi : " + e.toString());
      }
   }

   @Override
   public void editarEscalafones(List<Escalafones> listaE) {
      try {
         for (int i = 0; i < listaE.size(); i++) {
            persistenciaEscalafones.editar(getEm(), listaE.get(i));
         }
      } catch (Exception e) {
         log.warn("Error editarEscalafones Admi : " + e.toString());
      }
   }

   @Override
   public void borrarEscalafones(List<Escalafones> listaE) {
      try {
         for (int i = 0; i < listaE.size(); i++) {
            persistenciaEscalafones.borrar(getEm(), listaE.get(i));
         }
      } catch (Exception e) {
         log.warn("Error borrarEscalafones Admi : " + e.toString());
      }
   }

   @Override
   public List<Categorias> lovCategorias() {
      try {
         return persistenciaCategorias.buscarCategorias(getEm());
      } catch (Exception e) {
         log.warn("Error lovCategorias Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<SubCategorias> lovSubCategorias() {
      try {
         return persistenciaSubCategorias.consultarSubCategorias(getEm());
      } catch (Exception e) {
         log.warn("Error lovSubCategorias Admi : " + e.toString());
         return null;
      }
   }
}
