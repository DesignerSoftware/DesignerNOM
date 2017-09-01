/**
 * Documentación a cargo de Andres Pineda
 */
package Administrar;

import Entidades.Categorias;
import Entidades.ClasesCategorias;
import Entidades.Conceptos;
import Entidades.TiposSueldos;
import InterfaceAdministrar.AdministrarCategoriasInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaCategoriasInterface;
import InterfacePersistencia.PersistenciaClasesCategoriasInterface;
import InterfacePersistencia.PersistenciaConceptosInterface;
import InterfacePersistencia.PersistenciaTiposSueldosInterface;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

/**
 * Clase Stateful. <br>
 * Clase encargada de realizar las operaciones lógicas para la pantalla
 * 'CategoriaEsca'.
 *
 * @author betelgeuse
 */


@Stateful
public class AdministrarCategorias implements AdministrarCategoriasInterface {

   private static Logger log = Logger.getLogger(AdministrarCategorias.class);

   //--------------------------------------------------------------------------
   //ATRIBUTOS
   //--------------------------------------------------------------------------    
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaClasesCategorias'.
    */
   @EJB
   PersistenciaClasesCategoriasInterface persistenciaClasesCategorias;
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaConceptos'.
    */
   @EJB
   PersistenciaConceptosInterface persistenciaConceptos;
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaTiposSueldos'.
    */
   @EJB
   PersistenciaTiposSueldosInterface persistenciaTiposSueldos;
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaCategorias'.
    */
   @EJB
   PersistenciaCategoriasInterface persistenciaCategorias;

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

   @Override
   public List<Categorias> listaCategorias() {
      try {
         List<Categorias> lista = persistenciaCategorias.buscarCategorias(getEm());
         return lista;
      } catch (Exception e) {
         log.warn("Error listaCategorias Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public void crearCategorias(List<Categorias> listaC) {
      try {
         for (int i = 0; i < listaC.size(); i++) {
            if (listaC.get(i).getConcepto().getSecuencia() == null) {
               listaC.get(i).setConcepto(null);
            }
            persistenciaCategorias.crear(getEm(), listaC.get(i));
         }
      } catch (Exception e) {
         log.warn("Error crearCategorias Admi : " + e.toString());
      }
   }

   @Override
   public void editarCategorias(List<Categorias> listaC) {
      try {
         for (int i = 0; i < listaC.size(); i++) {
            if (listaC.get(i).getConcepto().getSecuencia() == null) {
               listaC.get(i).setConcepto(null);
            }
            persistenciaCategorias.editar(getEm(), listaC.get(i));
         }
      } catch (Exception e) {
         log.warn("Error editarCategorias Admi : " + e.toString());
      }
   }

   @Override
   public void borrarCategorias(List<Categorias> listaC) {
      try {
         for (int i = 0; i < listaC.size(); i++) {
            if (listaC.get(i).getConcepto().getSecuencia() == null) {
               listaC.get(i).setConcepto(null);
            }
            persistenciaCategorias.borrar(getEm(), listaC.get(i));
         }
      } catch (Exception e) {
         log.warn("Error borrarCategorias Admi : " + e.toString());
      }
   }

   @Override
   public List<ClasesCategorias> lovClasesCategorias() {
      try {
         return persistenciaClasesCategorias.consultarClasesCategorias(getEm());
      } catch (Exception e) {
         log.warn("Error lovClasesCategorias Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<TiposSueldos> lovTiposSueldos() {
      try {
         return persistenciaTiposSueldos.buscarTiposSueldos(getEm());
      } catch (Exception e) {
         log.warn("Error lovTiposSueldos Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<Conceptos> lovConceptos() {
      try {
         return persistenciaConceptos.buscarConceptos(getEm());
      } catch (Exception e) {
         log.warn("Error lovConceptos Admi : " + e.toString());
         return null;
      }
   }

}
