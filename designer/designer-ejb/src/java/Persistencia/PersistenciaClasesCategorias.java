/**
 * Documentación a cargo de Andres Pineda
 */
package Persistencia;

import Entidades.ClasesCategorias;
import InterfacePersistencia.PersistenciaClasesCategoriasInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la tabla 'ClasesCategorias' de
 * la base de datos.
 *
 * @author Andres Pineda
 */
@Stateless
public class PersistenciaClasesCategorias implements PersistenciaClasesCategoriasInterface {

   private static Logger log = Logger.getLogger(PersistenciaClasesCategorias.class);

   /**
    * Atributo EntityManager. Representa la comunicación con la base de datos
    */
   /*@PersistenceContext(unitName = "DesignerRHN-ejbPU")
     private EntityManager em;*/
   @Override
   public void crear(EntityManager em, ClasesCategorias clasesCategorias) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(clasesCategorias);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaClasesCategorias.crear:  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void editar(EntityManager em, ClasesCategorias clasesCategorias) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(clasesCategorias);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaClasesCategorias.editar:  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void borrar(EntityManager em, ClasesCategorias clasesCategorias) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.remove(em.merge(clasesCategorias));
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaClasesCategorias.borrar:  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public List<ClasesCategorias> consultarClasesCategorias(EntityManager em) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT td FROM ClasesCategorias td ORDER BY td.codigo DESC");
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         List<ClasesCategorias> clasesCategorias = query.getResultList();
         return clasesCategorias;
      } catch (Exception e) {
         log.error("Error PersistenciaTiposDias buscarTiposDias :  ", e);
         return null;
      }
   }

   @Override
   public ClasesCategorias consultarClaseCategoria(EntityManager em, BigInteger secClaseCategoria) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT cc FROM ClasesCategorias cc WHERE cc.secuencia=:secuencia");
         query.setParameter("secuencia", secClaseCategoria);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         ClasesCategorias clasesCategorias = (ClasesCategorias) query.getSingleResult();
         return clasesCategorias;
      } catch (Exception e) {
         return null;
      }
   }

   @Override
   public BigInteger contarCategoriasClaseCategoria(EntityManager em, BigInteger secuencia) {
      BigInteger retorno = new BigInteger("-1");
      try {
         em.clear();
         String sqlQuery = "SELECT COUNT(*)FROM categorias WHERE clasecategoria = ?";
         Query query = em.createNativeQuery(sqlQuery);
         query.setParameter(1, secuencia);
         retorno = new BigInteger(query.getSingleResult().toString());
         log.error("Contador PersistenciaClasesCategorias contarCategoriasClaseCategoria Retorno " + retorno);
         return retorno;
      } catch (Exception e) {
         log.error("Error PersistenciaClasesCategorias contarCategoriasClaseCategoria ERROR :  ", e);
         return retorno;
      }
   }
}
