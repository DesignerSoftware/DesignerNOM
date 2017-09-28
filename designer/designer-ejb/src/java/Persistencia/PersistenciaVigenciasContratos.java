/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.VigenciasContratos;
import InterfacePersistencia.PersistenciaVigenciasContratosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;

@Stateless
public class PersistenciaVigenciasContratos implements PersistenciaVigenciasContratosInterface {

   private static Logger log = Logger.getLogger(PersistenciaVigenciasContratos.class);

   @Override
   public boolean crear(EntityManager em, VigenciasContratos vigenciasContratos) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.persist(vigenciasContratos);
         tx.commit();
         return true;
      } catch (Exception e) {
         log.error("PersistenciaVigenciasContratos.crear()  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
         return false;
      }
   }

   @Override
   public void editar(EntityManager em, VigenciasContratos vigenciasContratos) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(vigenciasContratos);
         tx.commit();
      } catch (Exception e) {
         log.error("PersistenciaVigenciasContratos.editar():  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void borrar(EntityManager em, VigenciasContratos vigenciasContratos) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.remove(em.merge(vigenciasContratos));
         tx.commit();
      } catch (Exception e) {
         log.error("PersistenciaVigenciasContratos.borrar():  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public List<VigenciasContratos> buscarVigenciasContratos(EntityManager em) {
      try {
         em.clear();
         CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
         cq.select(cq.from(VigenciasContratos.class));
         return em.createQuery(cq).getResultList();
      } catch (Exception e) {
         log.error("PersistenciaVigenciasContratos.buscarVigenciasContratos():  ", e);
         return null;
      }
   }

   @Override
   public List<VigenciasContratos> buscarVigenciaContratoEmpleado(EntityManager em, BigInteger secuencia) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT vc FROM VigenciasContratos vc WHERE vc.empleado.secuencia = :secuenciaEmpl ORDER BY vc.fechainicial DESC", VigenciasContratos.class);
         query.setParameter("secuenciaEmpl", secuencia);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         List<VigenciasContratos> vigenciasC = query.getResultList();
         return vigenciasC;
      } catch (Exception e) {
         log.error("Error en Persistencia Vigencias Contratos  ", e);
         return null;
      }
   }

   @Override
   public VigenciasContratos buscarVigenciaContratoSecuencia(EntityManager em, BigInteger secuencia) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT v FROM VigenciasContratos v WHERE v.secuencia = :secuencia").setParameter("secuencia", secuencia);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         VigenciasContratos vigenciaC = (VigenciasContratos) query.getSingleResult();
         return vigenciaC;
      } catch (Exception e) {
         log.error("PersistenciaVigenciasContratos.buscarVigenciaContratoSecuencia():  ", e);
         return null;
      }
   }
}
