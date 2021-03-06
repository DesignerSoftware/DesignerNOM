/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.VigenciasAfiliaciones;
import InterfacePersistencia.PersistenciaVigenciasAfiliacionesInterface;
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
 * Clase encargada de realizar operaciones sobre la tabla
 * 'VigenciasAfiliaciones' de la base de datos.
 *
 * @author AndresPineda
 */
@Stateless
public class PersistenciaVigenciasAfiliaciones implements PersistenciaVigenciasAfiliacionesInterface {

   private static Logger log = Logger.getLogger(PersistenciaVigenciasAfiliaciones.class);

   @Override
   public boolean crear(EntityManager em, VigenciasAfiliaciones vigenciasAfiliaciones) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.persist(vigenciasAfiliaciones);
         tx.commit();
         return true;
      } catch (Exception e) {
          log.error("PersistenciaVigenciasAfiliaciones.crear():  ", e);
            if (tx.isActive()) {
               tx.rollback();
            }
         return false;
      }
   }

   @Override
   public void editar(EntityManager em, VigenciasAfiliaciones vigenciasAfiliaciones) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(vigenciasAfiliaciones);
         tx.commit();
      } catch (Exception e) {
          log.error("PersistenciaVigenciasAfiliaciones.editar():  ", e);
            if (tx.isActive()) {
               tx.rollback();
            }
      }
   }

   @Override
   public void borrar(EntityManager em, VigenciasAfiliaciones vigenciasAfiliaciones) {

      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.remove(em.merge(vigenciasAfiliaciones));
         tx.commit();
      } catch (Exception e) {
          log.error("PersistenciaVigenciasAfiliaciones.borrar():  ", e);
            if (tx.isActive()) {
               tx.rollback();
            }
      }
   }

   @Override
   public List<VigenciasAfiliaciones> buscarVigenciasAfiliaciones(EntityManager em) {
      try {
         em.clear();
         CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
         cq.select(cq.from(VigenciasAfiliaciones.class));
         return em.createQuery(cq).getResultList();
      } catch (Exception e) {
         log.error("Error buscarVigenciaAfiliacion PersistenciaVigenciasAfiliaciones ", e);
         return null;
      }
   }

   @Override
   public List<VigenciasAfiliaciones> buscarVigenciasAfiliacionesEmpleado(EntityManager em, BigInteger secuencia) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT va FROM VigenciasAfiliaciones va WHERE va.empleado.secuencia = :secuenciaEmpl ORDER BY va.fechainicial DESC");
         query.setParameter("secuenciaEmpl", secuencia);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         List<VigenciasAfiliaciones> vigenciasAfiliaciones = query.getResultList();
         return vigenciasAfiliaciones;
      } catch (Exception e) {
         log.error("Error en buscarVigenciasAfiliacionesEmpleado PersistenciaVigenciasAfiliaciones  ", e);
         return null;
      }
   }

   @Override
   public VigenciasAfiliaciones buscarVigenciasAfiliacionesSecuencia(EntityManager em, BigInteger secuencia) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT v FROM VigenciasAfiliaciones v WHERE v.secuencia = :secuencia").setParameter("secuencia", secuencia);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         VigenciasAfiliaciones vigenciasAfiliaciones = (VigenciasAfiliaciones) query.getSingleResult();
         return vigenciasAfiliaciones;
      } catch (Exception e) {
         log.error("Error buscarVigenciasAfiliacionesSecuencia PersistenciaVigenciasAfiliaciones  ", e);
         return null;
      }
   }

   @Override
   public List<VigenciasAfiliaciones> buscarVigenciasAfiliacionesVigenciaSecuencia(EntityManager em, BigInteger secuencia) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT va FROM VigenciasAfiliaciones va WHERE va.vigenciasueldo.secuencia = :secVigencia");
         query.setParameter("secVigencia", secuencia);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         List<VigenciasAfiliaciones> vigenciasAfiliaciones = query.getResultList();
         return vigenciasAfiliaciones;
      } catch (Exception e) {
         log.error("Error buscarVigenciasAfiliacionesSecuencia PersistenciaVigenciasAfiliaciones  ", e);
         return null;
      }
   }
}
