/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import InterfacePersistencia.PersistenciaEvalEvaluadoresInterface;
import Entidades.EvalEvaluadores;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 * Clase Stateless. <br>
 * Clase encargada de realizar operaciones sobre la tabla 'EvalEvaluadores' de
 * la base de datos.
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaEvalEvaluadores implements PersistenciaEvalEvaluadoresInterface {

   /**
    * Atributo EntityManager. Representa la comunicación con la base de datos
    */
   /*@PersistenceContext(unitName = "DesignerRHN-ejbPU")
     private EntityManager em;*/
   @Override
   public void crear(EntityManager em, EvalEvaluadores evalEvaluadores) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(evalEvaluadores);
         tx.commit();
      } catch (Exception e) {
         System.out.println("Error PersistenciaEvalEvaluadores.crear: " + e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void editar(EntityManager em, EvalEvaluadores evalEvaluadores) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(evalEvaluadores);
         tx.commit();
      } catch (Exception e) {
         System.out.println("Error PersistenciaEvalEvaluadores.crear: " + e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void borrar(EntityManager em, EvalEvaluadores evalEvaluadores) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.remove(em.merge(evalEvaluadores));
         tx.commit();

      } catch (Exception e) {
         if (tx.isActive()) {
            tx.rollback();
         }
         System.out.println("Error PersistenciaEvalEvaluadores.borrar: " + e);
      }
   }

   @Override
   public EvalEvaluadores buscarEvalEvaluador(EntityManager em, BigInteger secuenciaEvalEvaluadores) {
      try {
         em.clear();
         return em.find(EvalEvaluadores.class, secuenciaEvalEvaluadores);
      } catch (Exception e) {
         System.err.println("ERROR PersistenciaMotivosContratos buscarEvalEvaluadores ERROR " + e);
         return null;
      }
   }

   @Override
   public List<EvalEvaluadores> buscarEvalEvaluadores(EntityManager em) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT ev FROM EvalEvaluadores ev ORDER BY ev.codigo ");
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         List<EvalEvaluadores> evalEvaluadores = query.getResultList();
         return evalEvaluadores;
      } catch (Exception e) {
         System.out.println("\n ERROR EN PersistenciaEvalEvaluadoress buscarEvalEvaluadores ERROR" + e);
         return null;
      }
   }

   @Override
   public BigInteger verificarBorradoEvalPruebas(EntityManager em, BigInteger secuencia) {
      BigInteger retorno = new BigInteger("-1");
      try {
         em.clear();
         String sqql = "SELECT COUNT (*) FROM  evalpruebas  WHERE evalevaluador = ?";
         Query query = em.createNativeQuery(sqql);
         query.setParameter(1, secuencia);
         retorno = new BigInteger(query.getSingleResult().toString());
         System.err.println("PersistenciaEvalEvaluadores retorno ==" + retorno.intValue());
      } catch (Exception e) {
         System.err.println("ERROR EN PersistenciaEvalEvaluadores verificarBorrado ERROR :" + e);
      } finally {
         return retorno;
      }
   }
}
