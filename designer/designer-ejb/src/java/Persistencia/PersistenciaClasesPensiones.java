/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.ClasesPensiones;
import InterfacePersistencia.PersistenciaClasesPensionesInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Clase Stateless. <br>
 * Clase encargada de realizar operaciones sobre la tabla 'ClasesPensiones' de
 * la base de datos
 *
 * @author Andrés Pineda
 */
@Stateless
public class PersistenciaClasesPensiones implements PersistenciaClasesPensionesInterface {

   /**
    * Atributo EntityManager. Representa la comunicación con la base de datos
    */
   /* @PersistenceContext(unitName = "DesignerRHN-ejbPU")
     private EntityManager em;*/
   @Override
   public void crear(EntityManager em, ClasesPensiones clasesPensiones) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(clasesPensiones);
         tx.commit();
      } catch (Exception e) {
         System.out.println("Error PersistenciaClasesPensiones.crear: " + e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void editar(EntityManager em, ClasesPensiones clasesPensiones) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(clasesPensiones);
         tx.commit();
      } catch (Exception e) {
         System.out.println("Error PersistenciaClasesPensiones.editar: " + e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void borrar(EntityManager em, ClasesPensiones clasesPensiones) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.remove(em.merge(clasesPensiones));
         tx.commit();

      } catch (Exception e) {
         if (tx.isActive()) {
            tx.rollback();
         }
         System.out.println("Error PersistenciaClasesPensiones.borrar: " + e);
      }
   }

   @Override
   public List<ClasesPensiones> consultarClasesPensiones(EntityManager em) {
      try {
         em.clear();
         String sql = "SELECT *  FROM ClasesPensiones";
         //System.out.println("PersistenciaClasesPensiones consultarClasesPensiones ");
         Query query = em.createNativeQuery(sql, ClasesPensiones.class);
         List<ClasesPensiones> clasesPensionesLista = query.getResultList();
         return clasesPensionesLista;
      } catch (Exception e) {
         System.out.println("Error consultarClasesPensiones PersistenciaClasesPensiones");
         return null;
      }
   }

   @Override
   public ClasesPensiones consultarClasePension(EntityManager em, BigInteger secuencia) {

      try {
         em.clear();
         String sql = "SELECT * FROM ClasesPensiones WHERE secuencia = ?";
         Query query = em.createNativeQuery(sql, ClasesPensiones.class);
         query.setParameter(1, secuencia);
         //query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         ClasesPensiones claseP = (ClasesPensiones) query.getSingleResult();
         return claseP;
      } catch (Exception e) {
         System.out.println("Error buscarClasePennsion PersistenciaClasesPensiones");
         ClasesPensiones claseP = null;
         return claseP;
      }
   }

   @Override
   public BigInteger contarRetiradosClasePension(EntityManager em, BigInteger secuencia) {
      BigInteger retorno = new BigInteger("-1");
      try {
         em.clear();
         String sqlQuery = "SELECT COUNT(*)FROM pensionados WHERE clase=?";
         Query query = em.createNativeQuery(sqlQuery);
         query.setParameter(1, secuencia);
         retorno = new BigInteger(query.getSingleResult().toString());
         System.err.println("Contador PersistenciaMotivosRetiros  contarRetiradosClasePension  " + retorno);
         return retorno;
      } catch (Exception e) {
         System.out.println("Error PersistenciaMotivosRetiros   contarRetiradosClasePension. " + e);
         return retorno;
      }
   }
}
