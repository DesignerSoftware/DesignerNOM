/**
 * Documentación a cargo de AndresPineda
 */
package Persistencia;

import Entidades.TEFormulasConceptos;
import InterfacePersistencia.PersistenciaTEFormulasConceptosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

@Stateless
public class PersistenciaTEFormulasConceptos implements PersistenciaTEFormulasConceptosInterface {

   private static Logger log = Logger.getLogger(PersistenciaTEFormulasConceptos.class);

   @Override
   public void crear(EntityManager em, TEFormulasConceptos tEFormulasConceptos) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.persist(tEFormulasConceptos);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaTEFormulasConceptos.crear:  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void editar(EntityManager em, TEFormulasConceptos tEFormulasConceptos) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(tEFormulasConceptos);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaTEFormulasConceptos.editar:  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void borrar(EntityManager em, TEFormulasConceptos tEFormulasConceptos) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.remove(em.merge(tEFormulasConceptos));
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaTEFormulasConceptos.borrar:  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public List<TEFormulasConceptos> buscarTEFormulasConceptos(EntityManager em) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT t FROM TEFormulasConceptos t ORDER BY t.secuencia ASC");
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         List<TEFormulasConceptos> tEFormulasConceptos = (List<TEFormulasConceptos>) query.getResultList();
         return tEFormulasConceptos;
      } catch (Exception e) {
         log.error("Error buscarTEFormulasConceptos PersistenciaTEFormulasConceptos :  ", e);
         return null;
      }
   }

   @Override
   public TEFormulasConceptos buscarTEFormulaConceptoSecuencia(EntityManager em, BigInteger secTEFormula) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT t FROM TEFormulasConceptos t WHERE t.secuencia = :secTEFormula");
         query.setParameter("secTEFormula", secTEFormula);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         TEFormulasConceptos tEFormulasConceptos = (TEFormulasConceptos) query.getSingleResult();
         return tEFormulasConceptos;
      } catch (Exception e) {
         log.error("Error buscarTEFormulaConceptoSecuencia PersistenciaTEFormulasConceptos :  ", e);
         return null;
      }
   }

   @Override
   public List<TEFormulasConceptos> buscarTEFormulasConceptosPorSecuenciaTSGrupoTipoEntidad(EntityManager em, BigInteger secTSGrupo) {
      try {
         log.error("PersistenciaTEFormulasConceptos.buscarTEFormulasConceptosPorSecuenciaTSGrupoTipoEntidad() secTSGrupo: " + secTSGrupo);
         em.clear();
         Query query = em.createNativeQuery("SELECT t.* FROM TEFormulasConceptos t, EMPRESAS E  WHERE t.tsgrupotipoentidad = ? AND E.SECUENCIA = t.EMPRESA", TEFormulasConceptos.class);
         query.setParameter(1, secTSGrupo);
         List<TEFormulasConceptos> tEFormulasConceptos = query.getResultList();
         if (tEFormulasConceptos != null) {
            log.warn("PersistenciaTEFormulasConceptos.buscarTEFormulasConceptosPorSecuenciaTSGrupoTipoEntidad() : " + tEFormulasConceptos.size());
         } else {
            log.warn("PersistenciaTEFormulasConceptos.buscarTEFormulasConceptosPorSecuenciaTSGrupoTipoEntidad() : NULL");
         }
         return tEFormulasConceptos;
      } catch (Exception e) {
         log.error("Error buscarTEFormulasConceptosPorSecuenciaTSGrupoTipoEntidad PersistenciaTEFormulasConceptos :  ", e);
         return null;
      }
   }
}
