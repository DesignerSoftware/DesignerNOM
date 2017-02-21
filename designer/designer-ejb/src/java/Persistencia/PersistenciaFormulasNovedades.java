/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.FormulasNovedades;
import InterfacePersistencia.PersistenciaFormulasNovedadesInterface;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la tabla 'FormulasNovedades' de
 * la base de datos.
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaFormulasNovedades implements PersistenciaFormulasNovedadesInterface {

   /**
    * Atributo EntityManager. Representa la comunicación con la base de datos
    */
   /*@PersistenceContext(unitName = "DesignerRHN-ejbPU")
    private EntityManager em;*/
   @Override
   public void crear(EntityManager em, FormulasNovedades formulasNovedades) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(formulasNovedades);
         tx.commit();
      } catch (Exception e) {
         System.out.println("Error PersistenciaFormulasNovedades.crear: " + e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void editar(EntityManager em, FormulasNovedades formulasNovedades) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(formulasNovedades);
         tx.commit();
      } catch (Exception e) {
         if (tx.isActive()) {
            tx.rollback();
         }
         System.out.println("Error PersistenciaFormulasNovedades.editar: " + e);
      }
   }

   @Override
   public void borrar(EntityManager em, FormulasNovedades formulasNovedades) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.remove(em.merge(formulasNovedades));
         tx.commit();

      } catch (Exception e) {
            if (tx.isActive()) {
               tx.rollback();
            }
            System.out.println("Error PersistenciaFormulasNovedades.borrar: " + e);
      }
   }

   @Override
   public List<FormulasNovedades> formulasNovedadesParaFormulaSecuencia(EntityManager em, BigInteger secuencia) {
      System.out.println("Persistencia.PersistenciaFormulasNovedades.formulasNovedadesParaFormulaSecuencia() secuencia : " + secuencia);
      String st = "";
      if (secuencia == null) {
         st = "SELECT fn FROM FormulasNovedades fn";
      } else {
         st = "SELECT fn FROM FormulasNovedades fn WHERE fn.formula.secuencia = " + secuencia;
      }
      try {
         em.clear();
         List<FormulasNovedades> formulasNovedades = new ArrayList<FormulasNovedades>();
         Query query = em.createQuery(st);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         formulasNovedades = query.getResultList();
         return formulasNovedades;
      } catch (Exception e) {
         System.out.println("Error PersistenciaFormulasNovedades.formulasNovedadesParaFormulaSecuencia : " + e.toString());
         return null;
      }
   }

   @Override
   public boolean verificarExistenciaFormulasNovedades(EntityManager em, BigInteger secFormula) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT COUNT(fn) FROM FormulasNovedades fn WHERE fn.formula.secuencia = :secFormula");
         query.setParameter("secFormula", secFormula);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         Long resultado = (Long) query.getSingleResult();
         return resultado > 0;
      } catch (Exception e) {
         System.out.println("Exepcion: " + e);
         return false;
      }
   }
}
