/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.Cuentas;
import InterfacePersistencia.PersistenciaCuentasInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 * Clase Stateless. <br>
 * Clase encargada de realizar operaciones sobre la tabla 'Cuentas' de la base
 * de datos
 *
 * @author betelgeuse
 */
@Stateful
public class PersistenciaCuentas implements PersistenciaCuentasInterface {

   /**
    * Atributo EntityManager. Representa la comunicación con la base de datos
    */
   /* @PersistenceContext(unitName = "DesignerRHN-ejbPU")
     private EntityManager em;*/
   @Override
   public void crear(EntityManager em, Cuentas cuentas) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.persist(cuentas);
         tx.commit();
      } catch (Exception e) {
         System.out.println("Error PersistenciaCuentas.crear: " + e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void editar(EntityManager em, Cuentas cuentas) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(cuentas);
         tx.commit();
      } catch (Exception e) {
         if (tx.isActive()) {
            tx.rollback();
         }
         System.out.println("Error PersistenciaCuentas.editar: " + e);
      }
   }

   @Override
   public void borrar(EntityManager em, Cuentas cuentas) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.remove(em.merge(cuentas));
         tx.commit();

      } catch (Exception e) {
         if (tx.isActive()) {
            tx.rollback();
         }
         System.out.println("Error PersistenciaCuentas.borrar: " + e);
      }
   }

   @Override
   public List<Cuentas> buscarCuentas(EntityManager em) {
      try {
         em.clear();
         List<Cuentas> cuentas = (List<Cuentas>) em.createQuery("SELECT c FROM Cuentas c").getResultList();
         return cuentas;
      } catch (Exception e) {
         System.out.println("Error buscarCuentas PersistenciaCuentas : " + e.toString());
         return null;
      }
   }

   @Override
   public Cuentas buscarCuentasSecuencia(EntityManager em, BigInteger secuencia) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT c FROM Cuentas c WHERE c.secuencia = :secuencia");
         query.setParameter("secuencia", secuencia);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         Cuentas cuentas = (Cuentas) query.getSingleResult();
         return cuentas;
      } catch (Exception e) {
         System.out.println("Error buscarCuentasSecuencia PersistenciaCuentas : " + e.toString());
         Cuentas cuentas = null;
         return cuentas;
      }
   }

   @Override
   public List<Cuentas> buscarCuentasSecuenciaEmpresa(EntityManager em, BigInteger secuencia) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT c FROM Cuentas c WHERE c.empresa.secuencia = :secuencia");
         query.setParameter("secuencia", secuencia);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         List<Cuentas> cuentas = (List<Cuentas>) query.getResultList();
         return cuentas;
      } catch (Exception e) {
         System.out.println("Error buscarCuentasSecuenciaEmpresa PersistenciaCuentas : " + e.toString());
         List<Cuentas> cuentas = null;
         return cuentas;
      }
   }
}
