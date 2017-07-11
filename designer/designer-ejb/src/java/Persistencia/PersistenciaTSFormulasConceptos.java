/**
 * Documentación a cargo de AndresPineda
 */
package Persistencia;

import Entidades.TSFormulasConceptos;
import InterfacePersistencia.PersistenciaTSFormulasConceptosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

@Stateless
public class PersistenciaTSFormulasConceptos implements PersistenciaTSFormulasConceptosInterface {

   @Override
   public void crear(EntityManager em, TSFormulasConceptos tSFormulasConceptos) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.persist(tSFormulasConceptos);
         tx.commit();
      } catch (Exception e) {
         System.out.println("Error PersistenciaTSFormulasConceptos.crear: " + e.getMessage());
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void editar(EntityManager em, TSFormulasConceptos tSFormulasConceptos) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(tSFormulasConceptos);
         tx.commit();
      } catch (Exception e) {
         System.out.println("Error PersistenciaTSFormulasConceptos.editar: " + e.getMessage());
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void borrar(EntityManager em, TSFormulasConceptos tSFormulasConceptos) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.remove(em.merge(tSFormulasConceptos));
         tx.commit();
      } catch (Exception e) {
         System.out.println("Error PersistenciaTSFormulasConceptos.borrar: " + e.getMessage());
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public List<TSFormulasConceptos> buscarTSFormulasConceptos(EntityManager em) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT t FROM TSFormulasConceptos t ORDER BY t.secuencia ASC");
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         List<TSFormulasConceptos> tSFormulasConceptos = (List<TSFormulasConceptos>) query.getResultList();
         return tSFormulasConceptos;
      } catch (Exception e) {
         System.out.println("Error buscarTSFormulasConceptos PersistenciaTSFormulasConceptos : " + e.toString());
         return null;
      }
   }

   @Override
   public TSFormulasConceptos buscarTSFormulaConceptoSecuencia(EntityManager em, BigInteger secTSFormula) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT t FROM TSFormulasConceptos t WHERE t.secuencia = :secTSFormula");
         query.setParameter("secTSFormula", secTSFormula);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         TSFormulasConceptos tSFormulasConceptos = (TSFormulasConceptos) query.getSingleResult();
         return tSFormulasConceptos;
      } catch (Exception e) {
         System.out.println("Error buscarTSFormulaConceptoSecuencia PersistenciaTSFormulasConceptos : " + e.toString());
         return null;
      }
   }

   @Override
   public List<TSFormulasConceptos> buscarTSFormulasConceptosPorSecuenciaTipoSueldo(EntityManager em, BigInteger secTipoSueldo) {
      System.out.println("PersistenciaTSFormulasConceptos.buscarTSFormulasConceptosPorSecuenciaTipoSueldo() secTipoSueldo: " + secTipoSueldo);
      try {
         em.clear();
         Query query = em.createNativeQuery("SELECT t.* FROM TSFormulasConceptos t, EMPRESAS E WHERE\n"
                 + " t.tiposueldo = ? AND E.SECUENCIA = t.EMPRESA", TSFormulasConceptos.class);
         query.setParameter(1, secTipoSueldo);
         List<TSFormulasConceptos> tEFormulasConceptos = (List<TSFormulasConceptos>) query.getResultList();
         System.out.println("PersistenciaTSFormulasConceptos.buscarTSFormulasConceptosPorSecuenciaTipoSueldo()2");
         if (tEFormulasConceptos != null) {
            if (!tEFormulasConceptos.isEmpty()) {
               System.out.println("tEFormulasConceptos: " + tEFormulasConceptos);
               System.out.println("tEFormulasConceptos.get(0).getTiposueldo(): " + tEFormulasConceptos.get(0).getTiposueldo());
               System.out.println("tEFormulasConceptos.get(0).getConcepto(): " + tEFormulasConceptos.get(0).getConcepto());
               System.out.println("tEFormulasConceptos.get(0).getEmpresa(): " + tEFormulasConceptos.get(0).getEmpresa());
               System.out.println("tEFormulasConceptos.get(0).getFormula(): " + tEFormulasConceptos.get(0).getFormula());
            }
         }
         return tEFormulasConceptos;
      } catch (Exception e) {
         System.out.println("Error buscarTSFormulasConceptosPorSecuenciaTipoSueldo PersistenciaTSFormulasConceptos : " + e.toString());
         return null;
      }
   }
}
