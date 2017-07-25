/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.VigenciasLocalizaciones;
import InterfacePersistencia.PersistenciaVigenciasLocalizacionesInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;

@Stateless
public class PersistenciaVigenciasLocalizaciones implements PersistenciaVigenciasLocalizacionesInterface {

   private static Logger log = Logger.getLogger(PersistenciaVigenciasLocalizaciones.class);

   @Override
   public boolean crear(EntityManager em, VigenciasLocalizaciones vigenciasLocalizaciones) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.persist(vigenciasLocalizaciones);
         tx.commit();
         return true;
      } catch (Exception e) {
          log.error("Persistencia.PersistenciaVigenciasLocalizaciones.crear()" + e.getMessage());
            if (tx.isActive()) {
               tx.rollback();
            }
         return false;
      }
   }

   @Override
   public void editar(EntityManager em, VigenciasLocalizaciones vigenciasLocalizaciones) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(vigenciasLocalizaciones);
         tx.commit();
      } catch (Exception e) {
          log.error("Persistencia.PersistenciaVigenciasLocalizaciones.editar() " + e.getMessage());
            if (tx.isActive()) {
               tx.rollback();
            }
      }
   }

   @Override
   public void borrar(EntityManager em, VigenciasLocalizaciones vigenciasLocalizaciones) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.remove(em.merge(vigenciasLocalizaciones));
         tx.commit();
      } catch (Exception e) {
         log.error("La vigencia no exite o esta reservada por lo cual no puede ser modificada: " + e);
         try {
            if (tx.isActive()) {
               tx.rollback();
            }
         } catch (Exception ex) {
            log.error("No se puede hacer rollback porque no hay una transacción");
         }
      }
   }

   @Override
   public List<VigenciasLocalizaciones> buscarVigenciasLocalizaciones(EntityManager em) {
      try {
         em.clear();
         CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
         cq.select(cq.from(VigenciasLocalizaciones.class));
         return em.createQuery(cq).getResultList();
      } catch (Exception e) {
         log.error("Error buscarVigenciasLocalizaciones" + e.getMessage());
         return null;
      }
   }

   @Override
   public List<VigenciasLocalizaciones> buscarVigenciasLocalizacionesEmpleado(EntityManager em, BigInteger secuencia) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT vl FROM VigenciasLocalizaciones vl WHERE vl.empleado.secuencia = :secuenciaEmpl ORDER BY vl.fechavigencia DESC");
         query.setParameter("secuenciaEmpl", secuencia);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         List<VigenciasLocalizaciones> vigenciasLocalizaciones = query.getResultList();
         return vigenciasLocalizaciones;
      } catch (Exception e) {
         log.error("Error en Persistencia VigenciasLocalizaciones " + e.getMessage());
         return null;
      }
   }

   @Override
   public VigenciasLocalizaciones buscarVigenciasLocalizacionesSecuencia(EntityManager em, BigInteger secuencia) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT v FROM VigenciasLocalizaciones v WHERE v.secuencia = :secuencia").setParameter("secuencia", secuencia);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         VigenciasLocalizaciones vigenciasLocalizaciones = (VigenciasLocalizaciones) query.getSingleResult();
         return vigenciasLocalizaciones;
      } catch (Exception e) {
         log.error("Error buscarVigenciasLocalizacionesSecuencia Persistencia VigenciasLocalizaciones" + e.getMessage());
         return null;
      }
   }
}
