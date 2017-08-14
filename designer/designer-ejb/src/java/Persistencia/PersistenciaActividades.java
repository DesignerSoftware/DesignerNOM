/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.Actividades;
import InterfacePersistencia.PersistenciaActividadesInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;

/**
 * Clase Stateless. <br>
 * Clase encargada de realizar operaciones sobre la tabla 'Actividades' de la
 * base de datos
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaActividades implements PersistenciaActividadesInterface {

   private static Logger log = Logger.getLogger(PersistenciaActividades.class);

   /**
    * Atributo EntityManager. Representa la comunicación con la base de datos
    *
    * @param em
    */
   /*@PersistenceContext(unitName = "DesignerRHN-ejbPU")
     private EntityManager em;*/
   @Override
   public void crear(EntityManager em, Actividades actividades) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(actividades);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaActividades.crear: " + e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void editar(EntityManager em, Actividades actividades) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(actividades);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaActividades.editar: " + e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void borrar(EntityManager em, Actividades actividades) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.remove(em.merge(actividades));
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaActividades.borrar: " + e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public List<Actividades> buscarActividades(EntityManager em) {
      try {
         em.clear();
         CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
         cq.select(cq.from(Actividades.class));
         return em.createQuery(cq).getResultList();
      } catch (Exception e) {
         log.error("Error buscarActividades PersistenciaActividades : " + e.toString());
         return null;
      }
   }

   public BigInteger contarBienNecesidadesActividad(EntityManager em, BigInteger secuencia) {
      BigInteger retorno = new BigInteger("-1");
      try {
         em.clear();
         String sqlQuery = "SELECT COUNT(*)FROM biennecesidades WHERE actividad = ?";
         Query query = em.createNativeQuery(sqlQuery);
         query.setParameter(1, secuencia);
         retorno = new BigInteger(query.getSingleResult().toString());
         log.warn("Contador PersistenciaActividades contarTiposLegalizaciones persistencia " + retorno);
         return retorno;
      } catch (Exception e) {
         log.error("Error PersistenciaActividades contarTiposLegalizaciones. " + e);
         return retorno;
      }
   }

   @Override
   public BigInteger contarParametrosInformesActividad(EntityManager em, BigInteger secuencia) {
      BigInteger retorno = new BigInteger("-1");
      try {
         em.clear();
         String sqlQuery = "SELECT COUNT(*)FROM parametrosinformes WHERE actividadbienestar = ?";
         Query query = em.createNativeQuery(sqlQuery);
         query.setParameter(1, secuencia);
         retorno = new BigInteger(query.getSingleResult().toString());
         log.warn("Contador PersistenciaActividades contarTiposLegalizaciones persistencia " + retorno);
         return retorno;
      } catch (Exception e) {
         log.error("Error PersistenciaActividades contarTiposLegalizaciones. " + e);
         return retorno;
      }
   }
}
