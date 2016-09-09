/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.FormulasConceptos;
import InterfacePersistencia.PersistenciaFormulasConceptosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;

/**
 * Clase Stateless. <br> Clase encargada de realizar operaciones sobre la tabla
 * 'FormulasConceptos' de la base de datos.
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaFormulasConceptos implements PersistenciaFormulasConceptosInterface {

   /**
    * Atributo EntityManager. Representa la comunicación con la base de datos
    * @param em
    */
   /*
     * @PersistenceContext(unitName = "DesignerRHN-ejbPU") private EntityManager em;
    */
   @Override
   public void crear(EntityManager em, FormulasConceptos conceptos) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.persist(conceptos);
         tx.commit();
      } catch (Exception e) {
         System.out.println("Error PersistenciaFormulasConceptos.crear: " + e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void editar(EntityManager em, FormulasConceptos fConceptos) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         System.out.println("Entrando a persistir FormulasConceptos, FormulaConcepto : " + fConceptos);
         tx.begin();
         em.merge(fConceptos);
         tx.commit();
      } catch (Exception e) {
         System.out.println("Error PersistenciaFormulasConceptos.editar : " + e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void borrar(EntityManager em, FormulasConceptos conceptos) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.remove(em.merge(conceptos));
         tx.commit();

      } catch (Exception e) {
         try {
            if (tx.isActive()) {
               tx.rollback();
            }
         } catch (Exception ex) {
            System.out.println("Error PersistenciaFormulasConceptos.borrar: " + e);
         }
      }
   }

   @Override
   public List<FormulasConceptos> buscarFormulasConceptos(EntityManager em) {
      try {
//         em.clear();
//         CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
//         cq.select(cq.from(FormulasConceptos.class));
//         return em.createQuery(cq).getResultList();
         em.clear();
         String sqlQuery = "SELECT FC.CONCEPTO CONCEPTO, FC.FORMULA FORMULA, FC.TIPO TIPO, FC.ORDEN ORDEN, "
                 + "FC.FECHAFINAL FECHAFINAL, FC.FECHAINICIAL FECHAINICIAL, FC.SECUENCIA SECUENCIA, "
                 + "C.CODIGO CODIGOCONCEPTO, \n"
                 + "C.DESCRIPCION NOMBRECONCEPTO, E.NOMBRE NOMBREEMPRESA,\n"
                 + "E.NIT NITEMPRESA, F.NOMBRELARGO NOMBREFORMULA \n"
                 + "FROM FormulasConceptos FC, CONCEPTOS C, EMPRESAS E, FORMULAS F \n"
                 + "WHERE FC.CONCEPTO = C.SECUENCIA \n"
                 + "AND C.EMPRESA = E.SECUENCIA \n"
                 + "AND FC.FORMULA = F.SECUENCIA";
         System.out.println("sqlQuery : " + sqlQuery);
         Query query = em.createNativeQuery(sqlQuery, FormulasConceptos.class);
         List<FormulasConceptos> resultado = query.getResultList();
         return resultado;
      } catch (Exception e) {
         System.out.println("Error buscarFormulasConceptos Persistencia : " + e.toString());
         return null;
      }
   }

   @Override
   public boolean verificarExistenciaConceptoFormulasConcepto(EntityManager em, BigInteger secuencia) {
      System.out.println("buenas verificarExistenciaConceptoFormulasConcepto");
      try {
         em.clear();
         Query query = em.createQuery("SELECT COUNT(fc) FROM FormulasConceptos fc WHERE fc.concepto.secuencia = :secuencia");
         query.setParameter("secuencia", secuencia);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         Long resultado = (Long) query.getSingleResult();
         return resultado > 0;
      } catch (Exception e) {
         System.out.println("Exepcion: " + e);
         return false;
      }
   }

   @Override
   public List<FormulasConceptos> formulasConceptosXSecConcepto(EntityManager em, BigInteger secuencia) {
      try {
//         em.clear();
//         Query query = em.createQuery("SELECT fc FROM FormulasConceptos fc WHERE fc.concepto.secuencia = :secuencia");
//         query.setParameter("secuencia", secuencia);
//         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         System.err.println("Entro en formulasConceptosXSecConcepto() a consultar FormulasConceptos<>");
         em.clear();
         String sqlQuery = "SELECT FC.CONCEPTO CONCEPTO, FC.FORMULA FORMULA, FC.TIPO TIPO, FC.ORDEN ORDEN, "
                 + "FC.FECHAFINAL FECHAFINAL, FC.FECHAINICIAL FECHAINICIAL, FC.SECUENCIA SECUENCIA,"
                 + " C.CODIGO CODIGOCONCEPTO, \n"
                 + "C.DESCRIPCION NOMBRECONCEPTO, E.NOMBRE NOMBREEMPRESA,\n"
                 + "E.NIT NITEMPRESA, F.NOMBRELARGO NOMBREFORMULA \n"
                 + "FROM FormulasConceptos FC, CONCEPTOS C, EMPRESAS E, FORMULAS F \n"
                 + "WHERE FC.CONCEPTO = " + secuencia + " \n"
                 + "AND FC.CONCEPTO = C.SECUENCIA \n"
                 + "AND C.EMPRESA = E.SECUENCIA \n"
                 + "AND FC.FORMULA = F.SECUENCIA";
         System.out.println("sqlQuery : " + sqlQuery);
         Query query = em.createNativeQuery(sqlQuery, FormulasConceptos.class);
//         query.setParameter(1, secuencia);
         List<FormulasConceptos> resultado = query.getResultList();
         System.out.println("Ya consulto formulasConceptosXSecConcepto<> resultado : " + resultado);
         return resultado;
      } catch (Exception e) {
         System.out.println("Error en formulasConcepto() : " + e);
         return null;
      }
   }

   @Override
   public boolean verificarFormulaCargue_Concepto(EntityManager em, BigInteger secuencia, BigInteger secFormula) {
      System.out.println("Entrando a verificarFormulaCargue_Concepto PersistenciaFormulasConceptos");
      try {
         em.clear();
         Query query = em.createQuery("SELECT COUNT(fc) FROM FormulasConceptos fc WHERE fc.concepto.secuencia = :secuencia AND fc.formula.secuencia = :secFormula");
         query.setParameter("secuencia", secuencia);
         query.setParameter("secFormula", secFormula);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         Long resultado = (Long) query.getSingleResult();
         return resultado > 0;
      } catch (Exception e) {
         System.out.println("Exepcion verificarFormulaCargue_Concepto: " + e);
         return false;
      }
   }

   @Override
   public Long comportamientoConceptoAutomaticoSecuenciaConcepto(EntityManager em, BigInteger secuencia) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT COUNT(f.secuencia) FROM FormulasConceptos f WHERE f.concepto.secuencia=:secuencia");
         query.setParameter("secuencia", secuencia);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         Long resultado = (Long) query.getSingleResult();
         return resultado;
      } catch (Exception e) {
         System.out.println("Error Persistencia comportamientoConceptoAutomaticoSecuenciaConcepto : " + e.toString());
         return null;
      }
   }

   @Override
   public Long comportamientoConceptoSemiAutomaticoSecuenciaConcepto(EntityManager em, BigInteger secuencia) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT COUNT(f.secuencia) FROM FormulasConceptos f, FormulasNovedades fn WHERE  fn.formula=f.formula AND f.concepto.secuencia=:secuencia");
         query.setParameter("secuencia", secuencia);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         Long resultado = (Long) query.getSingleResult();
         return resultado;
      } catch (Exception e) {
         System.out.println("Error Persistencia comportamientoConceptoAutomaticoSecuenciaConcepto : " + e.toString());
         return null;
      }
   }

   @Override
   public List<FormulasConceptos> formulasConceptosParaFormulaSecuencia(EntityManager em, BigInteger secuencia) {
      try {
//            em.clear();
//            Query query = em.createQuery("SELECT f FROM FormulasConceptos f WHERE f.formula.secuencia=:secuenciaF");
//            query.setParameter("secuenciaF", secuencia);
//            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
//            List<FormulasConceptos> resultado = query.getResultList();
         System.err.println("Entro en formulasConceptosParaFormulaSecuencia() a consultar FormulasConceptos<>");
         em.clear();
         String sqlQuery = "SELECT FC.CONCEPTO CONCEPTO, FC.FORMULA FORMULA, FC.TIPO TIPO, FC.ORDEN ORDEN, "
                 + "FC.FECHAFINAL FECHAFINAL, FC.FECHAINICIAL FECHAINICIAL, FC.SECUENCIA SECUENCIA, "
                 + "C.CODIGO CODIGOCONCEPTO, \n"
                 + "C.DESCRIPCION NOMBRECONCEPTO, E.NOMBRE NOMBREEMPRESA,\n"
                 + "E.NIT NITEMPRESA, F.NOMBRELARGO NOMBREFORMULA \n"
                 + "FROM FormulasConceptos FC, CONCEPTOS C, EMPRESAS E, FORMULAS F \n"
                 + "WHERE FC.formula = " + secuencia + " \n"
                 + "AND FC.CONCEPTO = C.SECUENCIA \n"
                 + "AND C.EMPRESA = E.SECUENCIA \n"
                 + "AND FC.FORMULA = F.SECUENCIA";
         System.out.println("sqlQuery : " + sqlQuery);
         Query query = em.createNativeQuery(sqlQuery, FormulasConceptos.class);
//         query.setParameter(1, secuencia);
         List<FormulasConceptos> resultado = query.getResultList();
         System.out.println("Ya consulto FormulasConceptos<> resultado : " + resultado);
         return resultado;
      } catch (Exception e) {
         System.out.println("Error Persistencia formulasConceptosParaFormulaSecuencia : " + e.toString());
         return null;
      }
   }
}
