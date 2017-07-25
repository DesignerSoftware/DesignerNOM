/**
 * Documentación a cargo de AndresPineda
 */
package Persistencia;

import Entidades.TSGruposTiposEntidades;
import InterfacePersistencia.PersistenciaTSGruposTiposEntidadesInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

@Stateless
public class PersistenciaTSGruposTiposEntidades implements PersistenciaTSGruposTiposEntidadesInterface {

   private static Logger log = Logger.getLogger(PersistenciaTSGruposTiposEntidades.class);

   @Override
   public void crear(EntityManager em, TSGruposTiposEntidades tSGruposTiposEntidades) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.persist(tSGruposTiposEntidades);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaTSGruposTiposEntidades.crear: " + e.getMessage());
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void editar(EntityManager em, TSGruposTiposEntidades tSGruposTiposEntidades) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(tSGruposTiposEntidades);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaTSGruposTiposEntidades.editar: " + e.getMessage());
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void borrar(EntityManager em, TSGruposTiposEntidades tSGruposTiposEntidades) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.remove(em.merge(tSGruposTiposEntidades));
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaTSGruposTiposEntidades.borrar: " + e.getMessage());
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public List<TSGruposTiposEntidades> buscarTSGruposTiposEntidades(EntityManager em) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT t FROM TSGruposTiposEntidades t ORDER BY t.secuencia ASC");
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         List<TSGruposTiposEntidades> tSGruposTiposEntidades = (List<TSGruposTiposEntidades>) query.getResultList();
         return tSGruposTiposEntidades;
      } catch (Exception e) {
         log.error("Error buscarTSGruposTiposEntidades PersistenciaTSGruposTiposEntidades : " + e.toString());
         return null;
      }
   }

   @Override
   public TSGruposTiposEntidades buscarTSGrupoTipoEntidadSecuencia(EntityManager em, BigInteger secTSGrupo) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT t FROM TSGruposTiposEntidades t WHERE t.secuencia = :secTSGrupo");
         query.setParameter("secTSGrupo", secTSGrupo);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         TSGruposTiposEntidades tSGruposTiposEntidades = (TSGruposTiposEntidades) query.getSingleResult();
         return tSGruposTiposEntidades;
      } catch (Exception e) {
         log.error("Error buscarTSGrupoTipoEntidadSecuencia PersistenciaTSGruposTiposEntidades : " + e.toString());
         return null;
      }
   }

   @Override
   public List<TSGruposTiposEntidades> buscarTSGruposTiposEntidadesPorSecuenciaTipoSueldo(EntityManager em, BigInteger secTipoSueldo) {
      try {
         log.error("PersistenciaTSGruposTiposEntidades.buscarTSGruposTiposEntidadesPorSecuenciaTipoSueldo() secTipoSueldo: " + secTipoSueldo);
         em.clear();
         Query query = em.createQuery("SELECT t FROM TSGruposTiposEntidades t WHERE t.tiposueldo.secuencia =:secTipoSueldo");
         query.setParameter("secTipoSueldo", secTipoSueldo);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         List<TSGruposTiposEntidades> tSGruposTiposEntidades = (List<TSGruposTiposEntidades>) query.getResultList();
         log.error("PersistenciaTSGruposTiposEntidades.buscarTSGruposTiposEntidadesPorSecuenciaTipoSueldo()2");
         return tSGruposTiposEntidades;
      } catch (Exception e) {
         log.error("Error buscarTSGruposTiposEntidadesPorSecuenciaTipoSueldo PersistenciaTSGruposTiposEntidades : " + e.toString());
         return null;
      }
   }

}
