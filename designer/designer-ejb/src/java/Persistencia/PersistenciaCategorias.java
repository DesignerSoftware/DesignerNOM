/**
 * Documentación a cargo de Andres Pineda
 */
package Persistencia;

import Entidades.Categorias;
import InterfacePersistencia.PersistenciaCategoriasInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;

/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la tabla 'Categorias' de la
 * base de datos.
 *
 * @author Andres Pineda
 */
@Stateless
public class PersistenciaCategorias implements PersistenciaCategoriasInterface {

   private static Logger log = Logger.getLogger(PersistenciaCategorias.class);

   /**
    * Atributo EntityManager. Representa la comunicación con la base de datos
    */
   /*  @PersistenceContext(unitName = "DesignerRHN-ejbPU")
    private EntityManager em;*/
   @Override
   public void crear(EntityManager em, Categorias categorias) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(categorias);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaCategorias.crear:  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void editar(EntityManager em, Categorias categorias) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(categorias);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaCategorias.editar:  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void borrar(EntityManager em, Categorias categorias) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.remove(em.merge(categorias));
         tx.commit();

      } catch (Exception e) {
         log.error("Error PersistenciaCategorias.borrar:  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public List<Categorias> buscarCategorias(EntityManager em) {
      try {
         em.clear();
         CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
         cq.select(cq.from(Categorias.class));
         return em.createQuery(cq).getResultList();
      } catch (Exception e) {
         log.error("PersistenciaCategorias.buscarCategorias() ERROR:  ", e);
         return null;
      }
   }

   @Override
   public Categorias buscarCategoriaSecuencia(EntityManager em, BigInteger secCategoria) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT c FROM Categorias c WHERE c.secuencia=:secuencia");
         query.setParameter("secuencia", secCategoria);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         Categorias categorias = (Categorias) query.getSingleResult();
         return categorias;
      } catch (Exception e) {
         log.error("PersistenciaCategorias.buscarCategoriaSecuencia() ERROR:  ", e);
         return null;
      }
   }
}
