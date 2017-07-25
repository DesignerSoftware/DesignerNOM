/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.Sets;
import InterfacePersistencia.PersistenciaSetsInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 * Clase Stateless. <br>
 * Clase encargada de realizar operaciones sobre la tabla 'Sets' de la base de
 * datos.
 *
 * @author AndresPineda
 */
@Stateless
public class PersistenciaSets implements PersistenciaSetsInterface {

   private static Logger log = Logger.getLogger(PersistenciaSets.class);

   /**
    * Atributo EntityManager. Representa la comunicación con la base de datos.
    *
    * @param em
    * @return
    */
//    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
//    private EntityManager em;
   @Override
   public boolean crear(EntityManager em, Sets sets) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.persist(sets);
         tx.commit();
         return true;
      } catch (Exception e) {
         log.error("Persistencia.PersistenciaSets.crear()" + e.getMessage());
         if (tx.isActive()) {
            tx.rollback();
         }
      }
      return false;
   }

   @Override
   public void editar(EntityManager em, Sets sets) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(sets);
         tx.commit();
      } catch (Exception e) {
         log.error("Persistencia.PersistenciaSets.editar()" + e.getMessage());
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void borrar(EntityManager em, Sets sets) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.remove(em.merge(sets));
         tx.commit();
      } catch (Exception e) {
         log.error("Persistencia.PersistenciaSets.borrar()" + e.getMessage());
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public List<Sets> buscarSets(EntityManager em) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT s FROM Sets s");
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         List<Sets> setsLista = (List<Sets>) query.getResultList();
         return setsLista;
      } catch (Exception e) {
         log.error("Persistencia.PersistenciaSets.buscarSets()" + e.getMessage());
         return null;
      }
   }

   @Override
   public Sets buscarSetSecuencia(EntityManager em, BigInteger secuencia) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT e FROM Sets e WHERE e.secuencia = :secuencia");
         query.setParameter("secuencia", secuencia);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         Sets sets = (Sets) query.getSingleResult();
         return sets;
      } catch (Exception e) {
         log.error("Persistencia.PersistenciaSets.buscarSetSecuencia() " + e.getMessage());
         return null;
      }
   }

   @Override
   public List<Sets> buscarSetsEmpleado(EntityManager em, BigInteger secEmpleado) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT st FROM Sets st WHERE st.empleado.secuencia = :secuenciaEmpl ORDER BY st.fechainicial DESC");
         query.setParameter("secuenciaEmpl", secEmpleado);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         List<Sets> setsE = query.getResultList();
         return setsE;
      } catch (Exception e) {
         log.error("Error en Persistencia Sets " + e.getMessage());
         return null;
      }
   }
}
