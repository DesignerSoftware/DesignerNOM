/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.FormulasConceptos;
import Entidades.FormulasConceptosAux;
import InterfacePersistencia.PersistenciaFormulasConceptosInterface;
import java.math.BigDecimal;
import java.math.BigInteger;
//import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
//import javax.persistence.PersistenceContext;
import javax.persistence.Query;
//import javax.persistence.criteria.CriteriaQuery;

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
    *
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
         em.clear();
         String sqlQuery = "SELECT FC.* FROM FormulasConceptos FC, CONCEPTOS C, FORMULAS F \n"
                 + "WHERE FC.CONCEPTO = C.SECUENCIA \n"
                 + "AND FC.FORMULA = F.SECUENCIA";
         System.out.println("sqlQuery : " + sqlQuery);
         Query query = em.createNativeQuery(sqlQuery, FormulasConceptos.class);
         List<FormulasConceptos> resultado = query.getResultList();

         if (resultado != null) {
            if (!resultado.isEmpty()) {
               System.out.println("resultado.size() : " + resultado.size());
               for (int i = 0; i < resultado.size(); i++) {
                  em.clear();
                  String sqlQuery2 = "SELECT FC.SECUENCIA, C.CODIGO CODIGOCONCEPTO, C.DESCRIPCION NOMBRECONCEPTO, \n"
                          + "(select NIT from empresas where secuencia = C.EMPRESA) NITEMPRESA, \n"
                          + "(select NOMBRE from empresas where secuencia = C.EMPRESA) NOMBREEMPRESA, \n"
                          + "(select NOMBRELARGO from formulas where secuencia = " + resultado.get(i).getFormula() + ") NOMBREFORMULA \n"
                          + "FROM FormulasConceptos FC, CONCEPTOS C \n"
                          + "WHERE FC.CONCEPTO = C.SECUENCIA \n"
                          + "AND FC.SECUENCIA = " + resultado.get(i).getSecuencia();
                  if (i == 0) {
                     System.out.println("sqlQuery2 : " + sqlQuery2);
                  }
                  Query query2 = em.createNativeQuery(sqlQuery2, FormulasConceptosAux.class);
                  FormulasConceptosAux resultado2 = (FormulasConceptosAux) query2.getSingleResult();
                  resultado.get(i).setCodigoConcepto(resultado2.getCodigoConcepto());
                  resultado.get(i).setNitEmpresa(resultado2.getNitEmpresa());
                  resultado.get(i).setNombreConcepto(resultado2.getNombreConcepto());
                  resultado.get(i).setNombreEmpresa(resultado2.getNombreEmpresa());
                  resultado.get(i).setNombreFormula(resultado2.getNombreFormula());
               }
            }
         }
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
         String txtQuery = "SELECT COUNT(fc) FROM FormulasConceptos fc WHERE fc.concepto = ?";
         Query query = em.createNamedQuery(txtQuery);
         query.setParameter(1, secuencia);
         BigDecimal resultado = (BigDecimal) query.getSingleResult();
         return resultado.compareTo(BigDecimal.ZERO) > 0;
      } catch (Exception e) {
         System.out.println("Exepcion: " + e);
         return false;
      }
   }

   @Override
   public List<FormulasConceptos> formulasConceptosXSecConcepto(EntityManager em, BigInteger secuencia) {
      try {
         em.clear();
         String sqlQuery = "SELECT FC.* FROM FormulasConceptos FC, CONCEPTOS C, EMPRESAS E, FORMULAS F \n"
                 + "WHERE FC.CONCEPTO = C.SECUENCIA \n"
                 + "AND C.EMPRESA = E.SECUENCIA \n"
                 + "AND FC.CONCEPTO = " + secuencia + "\n"
                 + "AND FC.FORMULA = F.SECUENCIA";
         System.out.println("sqlQuery : " + sqlQuery);
         Query query = em.createNativeQuery(sqlQuery, FormulasConceptos.class);
         List<FormulasConceptos> resultado = query.getResultList();
         System.out.println("resultado : " + resultado);
         if (resultado != null) {
            if (!resultado.isEmpty()) {
               System.out.println("resultado.size() : " + resultado.size());
               for (int i = 0; i < resultado.size(); i++) {
                  em.clear();
                  String sqlQuery2 = "SELECT FC.SECUENCIA, C.CODIGO CODIGOCONCEPTO, C.DESCRIPCION NOMBRECONCEPTO, \n"
                          + "(select NIT from empresas where secuencia = C.EMPRESA) NITEMPRESA, \n"
                          + "(select NOMBRE from empresas where secuencia = C.EMPRESA) NOMBREEMPRESA, \n"
                          + "(select NOMBRELARGO from formulas where secuencia = " + resultado.get(i).getFormula() + ") NOMBREFORMULA \n"
                          + "FROM FormulasConceptos FC, CONCEPTOS C \n"
                          + "WHERE FC.CONCEPTO = C.SECUENCIA \n"
                          + "AND FC.SECUENCIA = " + resultado.get(i).getSecuencia();
                  Query query2 = em.createNativeQuery(sqlQuery2, FormulasConceptosAux.class);
                  FormulasConceptosAux resultado2 = (FormulasConceptosAux) query2.getSingleResult();
                  resultado.get(i).setCodigoConcepto(resultado2.getCodigoConcepto());
                  resultado.get(i).setNitEmpresa(resultado2.getNitEmpresa());
                  resultado.get(i).setNombreConcepto(resultado2.getNombreConcepto());
                  resultado.get(i).setNombreEmpresa(resultado2.getNombreEmpresa());
                  resultado.get(i).setNombreFormula(resultado2.getNombreFormula());
               }
            }
         }
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
         String txtQuery = "SELECT COUNT(fc) FROM FormulasConceptos fc WHERE fc.concepto = ? AND fc.formula = ? ";
         Query query = em.createNamedQuery(txtQuery);
         query.setParameter(1, secuencia);
         query.setParameter(2, secFormula);
         BigDecimal resultado = (BigDecimal) query.getSingleResult();
         return resultado.compareTo(BigDecimal.ZERO) > 0;
      } catch (Exception e) {
         System.out.println("Exepcion verificarFormulaCargue_Concepto: " + e);
         return false;
      }
   }

   @Override
   public Long comportamientoConceptoAutomaticoSecuenciaConcepto(EntityManager em, BigInteger secuencia) {
      try {
         em.clear();
         String txtquery = "SELECT COUNT(*) FROM FormulasConceptos f, conceptos c WHERE f.concepto=c.secuencia and c.secuencia = ? ";
         Query query = em.createNativeQuery(txtquery);
         query.setParameter(1, secuencia);
         BigDecimal resTemp = (BigDecimal) query.getSingleResult();
         Long resultado = resTemp.longValue();
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
         String txtQuery = "SELECT COUNT(*) FROM FormulasConceptos f, FormulasNovedades fn WHERE fn.formula=f.formula AND f.concepto = ?";
         Query query = em.createNativeQuery(txtQuery);
         query.setParameter(1, secuencia);
         BigDecimal resTemp = (BigDecimal) query.getSingleResult();
         Long resultado = resTemp.longValue();
         return resultado;
      } catch (Exception e) {
         System.out.println("Error Persistencia comportamientoConceptoAutomaticoSecuenciaConcepto : " + e.toString());
         return null;
      }
   }

   @Override
   public List<FormulasConceptos> formulasConceptosParaFormulaSecuencia(EntityManager em, BigInteger secuencia) {
//        List<FormulasConceptos> resultado = new ArrayList<>();
      List<FormulasConceptos> resultado;
      try {
         em.clear();
         String sqlQuery = "SELECT FC.* FROM FormulasConceptos FC, CONCEPTOS C \n"
                 + "WHERE FC.CONCEPTO = C.SECUENCIA \n"
                 + "AND FC.FORMULA = " + secuencia + " \n";
         System.out.println("sqlQuery : " + sqlQuery);
         Query query = em.createNativeQuery(sqlQuery, FormulasConceptos.class);
         resultado = query.getResultList();
         System.out.println("resultado : " + resultado);

         if (resultado != null) {
            if (!resultado.isEmpty()) {
               System.out.println("resultado.size() : " + resultado.size());
               for (int i = 0; i < resultado.size(); i++) {
                  em.clear();
                  String sqlQuery2 = "SELECT FC.SECUENCIA, C.CODIGO CODIGOCONCEPTO, C.DESCRIPCION NOMBRECONCEPTO, \n"
                          + "(select NIT from empresas where secuencia = C.EMPRESA) NITEMPRESA, \n"
                          + "(select NOMBRE from empresas where secuencia = C.EMPRESA) NOMBREEMPRESA, \n"
                          + "(select NOMBRELARGO from formulas where secuencia = " + resultado.get(i).getFormula() + ") NOMBREFORMULA \n"
                          + "FROM FormulasConceptos FC, CONCEPTOS C \n"
                          + "WHERE FC.CONCEPTO = C.SECUENCIA \n"
                          + "AND FC.SECUENCIA = " + resultado.get(i).getSecuencia();

                  Query query2 = em.createNativeQuery(sqlQuery2, FormulasConceptosAux.class);

                  FormulasConceptosAux resultado2 = (FormulasConceptosAux) query2.getSingleResult();
                  resultado.get(i).setCodigoConcepto(resultado2.getCodigoConcepto());
                  resultado.get(i).setNitEmpresa(resultado2.getNitEmpresa());
                  resultado.get(i).setNombreConcepto(resultado2.getNombreConcepto());
                  resultado.get(i).setNombreEmpresa(resultado2.getNombreEmpresa());
                  resultado.get(i).setNombreFormula(resultado2.getNombreFormula());
               }
            }
         }
         return resultado;
      } catch (Exception e) {
         System.out.println("Error Persistencia formulasConceptosParaFormulaSecuencia : " + e.toString());
         return null;
      }
   }
}
