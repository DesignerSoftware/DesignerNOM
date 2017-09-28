/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.Formulascontratos;
import InterfacePersistencia.PersistenciaFormulasContratosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Clase Stateless. <br>
 * Clase encargada de realizar operaciones sobre la tabla 'FormulasContratos' de
 * la base de datos.
 *
 * @author Andres Pineda.
 */
@Stateless
public class PersistenciaFormulasContratos implements PersistenciaFormulasContratosInterface {

   private static Logger log = Logger.getLogger(PersistenciaFormulasContratos.class);

   /**
    * Atributo EntityManager. Representa la comunicación con la base de datos
    */
   /* @PersistenceContext(unitName = "DesignerRHN-ejbPU")
     private EntityManager em;*/
   @Override
   public void crear(EntityManager em, Formulascontratos formulascontratos) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.persist(formulascontratos);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaFormulasContratos.crear:  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void editar(EntityManager em, Formulascontratos formulascontratos) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(formulascontratos);
         tx.commit();
      } catch (Exception e) {
         if (tx.isActive()) {
            tx.rollback();
         }
         log.error("Error PersistenciaFormulasContratos.editar:  ", e);
      }
   }

   @Override
   public void borrar(EntityManager em, Formulascontratos formulascontratos) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.remove(em.merge(formulascontratos));
         tx.commit();

      } catch (Exception e) {
         if (tx.isActive()) {
            tx.rollback();
         }
         log.error("Error PersistenciaFormulasContratos.borrar:  ", e);
      }
   }

   @Override
   public List<Formulascontratos> formulasContratosParaFormulaSecuencia(EntityManager em, BigInteger secuencia) {
      try {
         em.clear();
         Query queryFinal = em.createQuery("SELECT fc FROM Formulascontratos fc WHERE fc.formula.secuencia=:secuencia");
         queryFinal.setParameter("secuencia", secuencia);
         queryFinal.setHint("javax.persistence.cache.storeMode", "REFRESH");
         List<Formulascontratos> formulascontratos = queryFinal.getResultList();
         return formulascontratos;
      } catch (Exception e) {
         log.error("Error PersistenciaFormulasContratos.formulasContratosParaFormulaSecuencia :  ", e);
         return null;
      }
   }

   @Override
   public List<Formulascontratos> formulasContratosParaContratoSecuencia(EntityManager em, BigInteger secuencia) {
      try {
         em.clear();
         Query queryFinal = em.createQuery("SELECT fc FROM Formulascontratos fc WHERE fc.contrato.secuencia=:secuencia");
         queryFinal.setParameter("secuencia", secuencia);
         queryFinal.setHint("javax.persistence.cache.storeMode", "REFRESH");
         List<Formulascontratos> formulascontratos = queryFinal.getResultList();
         return formulascontratos;
      } catch (Exception e) {
         log.error("Error PersistenciaFormulasContratos.formulasContratosParaFormulaSecuencia :  ", e);
         return null;
      }
   }

   public Formulascontratos formulasContratosParaContratoFormulasContratosEntidades(EntityManager em, BigInteger secuencia) {
      try {
         em.clear();
         Query queryFinal = em.createQuery("SELECT fc FROM Formulascontratos fc WHERE fc.secuencia=:secuencia");
         queryFinal.setParameter("secuencia", secuencia);
         queryFinal.setHint("javax.persistence.cache.storeMode", "REFRESH");
         Formulascontratos formulascontratos = (Formulascontratos) queryFinal.getSingleResult();
         return formulascontratos;
      } catch (Exception e) {
         log.error("Error PersistenciaFormulasContratos.formulasContratosParaContratoFormulasContratosEntidades :  ", e);
         return null;
      }
   }

}
