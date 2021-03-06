/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.VigenciasJornadas;
import InterfacePersistencia.PersistenciaVigenciasJornadasInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;

@Stateless
public class PersistenciaVigenciasJornadas implements PersistenciaVigenciasJornadasInterface {

   private static Logger log = Logger.getLogger(PersistenciaVigenciasJornadas.class);

   @Override
   public boolean crear(EntityManager em, VigenciasJornadas vigenciasJornadas) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.persist(vigenciasJornadas);
         tx.commit();
         return true;
      } catch (Exception e) {
          log.error("PersistenciaVigenciasJornadas.crear():  ", e);
            if (tx.isActive()) {
               tx.rollback();
            }
         return false;
      }
   }

   @Override
   public void editar(EntityManager em, VigenciasJornadas vigenciasJornadas) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(vigenciasJornadas);
         tx.commit();
      } catch (Exception e) {
          log.error("PersistenciaVigenciasJornadas.editar():  ", e);
            if (tx.isActive()) {
               tx.rollback();
            }
      }
   }

   @Override
   public void borrar(EntityManager em, VigenciasJornadas vigenciasJornadas) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.remove(em.merge(vigenciasJornadas));
         tx.commit();
      } catch (Exception e) {
          log.error("PersistenciaVigenciasJornadas.borrar():  ", e);
            if (tx.isActive()) {
               tx.rollback();
            }
      }
   }

   @Override
   public List<VigenciasJornadas> buscarVigenciasJornadas(EntityManager em) {
      try {
         em.clear();
         CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
         cq.select(cq.from(VigenciasJornadas.class));
         return em.createQuery(cq).getResultList();
      } catch (Exception e) {
         log.error("Error buscarVigenciasJornadas ", e);
         return null;
      }
   }

   @Override
   public List<VigenciasJornadas> buscarVigenciasJornadasEmpleado(EntityManager em, BigInteger secuencia) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT vj FROM VigenciasJornadas vj WHERE vj.empleado.secuencia = :secuenciaEmpl ORDER BY vj.fechavigencia DESC");
         query.setParameter("secuenciaEmpl", secuencia);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         List<VigenciasJornadas> vigenciasJornadas = query.getResultList();
         return vigenciasJornadas;
      } catch (Exception e) {
         log.error("Error en buscarVigenciasJornadasEmpleado  ", e);
         return null;
      }
   }

   @Override
   public VigenciasJornadas buscarVigenciasJornadasSecuencia(EntityManager em, BigInteger secuencia) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT v FROM VigenciasJornadas v WHERE v.secuencia = :secuencia").setParameter("secuencia", secuencia);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         VigenciasJornadas vigenciasJornadas = (VigenciasJornadas) query.getSingleResult();
         return vigenciasJornadas;
      } catch (Exception e) {
         log.error("Error buscarVigenciasJornadasSecuencia Persistencia VL ", e);
         return null;
      }
   }
}
