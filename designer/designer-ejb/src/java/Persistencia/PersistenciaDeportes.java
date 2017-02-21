/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.Deportes;
import InterfacePersistencia.PersistenciaDeportesInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;

/**
 * Clase Stateless. <br>
 * Clase encargada de realizar operaciones sobre la tabla 'Deportes' de la base
 * de datos.
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaDeportes implements PersistenciaDeportesInterface {

   /**
    * Atributo EntityManager. Representa la comunicación con la base de datos
    */
   @Override
   public void crear(EntityManager em, Deportes deportes) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(deportes);
         tx.commit();
      } catch (Exception e) {
         System.out.println("Error PersistenciaDeportes.crear: " + e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void editar(EntityManager em, Deportes deportes) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(deportes);
         tx.commit();
      } catch (Exception e) {
         System.out.println("Error PersistenciaDeportes.editar: " + e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void borrar(EntityManager em, Deportes deportes) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.remove(em.merge(deportes));
         tx.commit();

      } catch (Exception e) {
         if (tx.isActive()) {
            tx.rollback();
         }
         System.out.println("Error PersistenciaDeportes.borrar: " + e);
      }
   }

   @Override
   public Deportes buscarDeporte(EntityManager em, BigInteger secuencia) {
      try {
         em.clear();
         return em.find(Deportes.class, secuencia);
      } catch (Exception e) {
         System.out.println("Error buscarDeporte PersistenciaDeportes : " + e.toString());
         return null;
      }
   }

   @Override
   public List<Deportes> buscarDeportes(EntityManager em) {
      try {
         em.clear();
         CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
         cq.select(cq.from(Deportes.class));
         return em.createQuery(cq).getResultList();
      } catch (Exception e) {
         System.out.println("Error buscarDeportes PersistenciaDeportes");
         return null;
      }
   }

   @Override
   public BigInteger contadorParametrosInformes(EntityManager em, BigInteger secuencia) {
      BigInteger retorno;
      try {
         em.clear();
         String sqlQuery = "SELECT COUNT (*) FROM parametrosinformes WHERE deporte =  ?";
         Query query = em.createNativeQuery(sqlQuery);
         query.setParameter(1, secuencia);
         retorno = new BigInteger(query.getSingleResult().toString());
         System.out.println("PERSISTENCIADEPORTES contadorParametrosInformes = " + retorno);
         return retorno;
      } catch (Exception e) {
         System.err.println("ERROR PERSISTENCIADEPORTES contadorParametrosInformes  ERROR = " + e);
         retorno = new BigInteger("-1");
         return retorno;
      }

   }

   @Override
   public BigInteger contadorDeportesPersonas(EntityManager em, BigInteger secuencia) {
      BigInteger retorno;
      try {
         em.clear();
         String sqlQuery = "SELECT COUNT(*) FROM deportespersonas WHERE deporte = ?";
         Query query = em.createNativeQuery(sqlQuery);
         query.setParameter(1, secuencia);
         retorno = new BigInteger(query.getSingleResult().toString());
         System.out.println("PERSISTENCIADEPORTES contadorDeportesPersonas = " + retorno);
         return retorno;
      } catch (Exception e) {
         System.err.println("ERROR PERSISTENCIADEPORTES contadorDeportesPersonas  ERROR = " + e);
         retorno = new BigInteger("-1");
         return retorno;
      }
   }

   @Override
   public BigInteger verificarBorradoVigenciasDeportes(EntityManager em, BigInteger secuencia) {
      BigInteger retorno;
      try {
         em.clear();
         String sqlQuery = "SELECT count(*) FROM VigenciasDeportes  WHERE  deporte   =?";
         Query query = em.createNativeQuery(sqlQuery);
         query.setParameter(1, secuencia);
         retorno = new BigInteger(query.getSingleResult().toString());
         System.out.println("PERSISTENCIADEPORTES verificarBorradoVigenciasDeportes = " + retorno);
         return retorno;
      } catch (Exception e) {
         System.err.println("ERROR PERSISTENCIADEPORTES verificarBorradoVigenciasDeportes  ERROR = " + e);
         retorno = new BigInteger("-1");
         return retorno;
      }
   }
}
