/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.Aficiones;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import InterfacePersistencia.PersistenciaAficionesInterface;
import java.math.BigInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Query;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;

/**
 * Clase Stateless. <br>
 * Clase encargada de realizar operaciones sobre la tabla 'Aficiones' de la base
 * de datos
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaAficiones implements PersistenciaAficionesInterface {

   /**
    * Atributo EntityManager. Representa la comunicación con la base de datos
    */
   /*@PersistenceContext(unitName = "DesignerRHN-ejbPU")
     private EntityManager em;*/
   /**
    * Atributo Aficiones que representa la afición con el máximo código.
    */
   Aficiones maximo = new Aficiones();

   @Override
   public void crear(EntityManager em, Aficiones aficiones) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(aficiones);
         tx.commit();
      } catch (Exception e) {
         System.out.println("Error PersistenciaAficiones.crear: " + e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void editar(EntityManager em, Aficiones aficiones) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(aficiones);
         tx.commit();
      } catch (Exception e) {
         System.out.println("Error PersistenciaAficiones.editar: " + e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void borrar(EntityManager em, Aficiones aficiones) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.remove(em.merge(aficiones));
         tx.commit();
      } catch (Exception e) {
         System.out.println("Error PersistenciaAficiones.borrar: " + e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public Aficiones buscarAficion(EntityManager em, BigInteger secuencia) {
      em.clear();
      return em.find(Aficiones.class, secuencia);
   }

   @Override
   public List<Aficiones> buscarAficiones(EntityManager em) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT af FROM Aficiones af ORDER BY af.codigo");
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         List<Aficiones> aficiones = (List<Aficiones>) query.getResultList();
         return aficiones;
      } catch (Exception e) {
         List<Aficiones> aficiones = null;
         System.out.println("Persistencia.PersistenciaAficiones.buscarAficiones() e: " + e);
         return aficiones;
      }
   }

   @Override
   public short maximoCodigoAficiones(EntityManager em) {
      Short max = new Short("0");
      try {
         em.clear();
         Query query = em.createQuery("SELECT af FROM Aficiones af "
                 + "WHERE af.codigo=(SELECT MAX(afi.codigo) FROM Aficiones afi)");
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         maximo = (Aficiones) query.getSingleResult();
         max = maximo.getCodigo();
      } catch (Exception e) {
         System.out.println("Persistencia.PersistenciaAficiones.maximoCodigoAficiones() e: " + e);
      }
      return max;
   }

   @Override
   public Aficiones buscarAficionCodigo(EntityManager em, Short cod) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT af FROM Aficiones af WHERE af.codigo=:cod");
         query.setParameter("cod", cod);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         Aficiones aficiones = (Aficiones) query.getSingleResult();
         return aficiones;
      } catch (Exception e) {
         System.out.println("Persistencia.PersistenciaAficiones.buscarAficionCodigo() e: " + e);
         return null;
      }
   }
}
