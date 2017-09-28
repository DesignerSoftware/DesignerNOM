/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.OperandosLogs;
import InterfacePersistencia.PersistenciaOperandosLogsInterface;
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
 * Clase encargada de realizar operaciones sobre la tabla 'OperandosLogs' de la
 * base de datos.
 *
 * @author Andres Pineda.
 */
@Stateless
public class PersistenciaOperandosLogs implements PersistenciaOperandosLogsInterface {

   private static Logger log = Logger.getLogger(PersistenciaOperandosLogs.class);

   /**
    * Atributo EntityManager. Representa la comunicación con la base de datos
    */
//    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
//    private EntityManager em;
   @Override
   public void crear(EntityManager em, OperandosLogs operandos) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(operandos);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaOperandosLogs.crear:  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void editar(EntityManager em, OperandosLogs operandos) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(operandos);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaOperandosLogs.editar:  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void borrar(EntityManager em, OperandosLogs operandos) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.remove(em.merge(operandos));
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaOperandosLogs.borrar:  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public List<OperandosLogs> buscarOperandosLogs(EntityManager em) {
      try {
         em.clear();
         CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
         cq.select(cq.from(OperandosLogs.class));
         return em.createQuery(cq).getResultList();
      } catch (Exception e) {
         log.error("Error buscarOperandos PersistenciaOperandosLogs :  ", e);
         return null;
      }
   }

   @Override
   public List<OperandosLogs> buscarOperandosLogsParaProcesoSecuencia(EntityManager em, BigInteger secProceso) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT ol FROM OperandosLogs ol WHERE ol.proceso.secuencia=:secProceso");
         query.setParameter("secProceso", secProceso);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         List<OperandosLogs> listMotivosDemandas = query.getResultList();
         return listMotivosDemandas;
      } catch (Exception e) {
         log.error("Error buscarOperandosLogsParaProcesoSecuencia (PersistenciaOperandosLogs):  ", e);
         return null;
      }
   }
}
