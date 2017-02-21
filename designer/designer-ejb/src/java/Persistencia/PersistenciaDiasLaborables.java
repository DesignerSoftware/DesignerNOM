/**
 * Documentación a cargo de Andres Pineda
 */
package Persistencia;

import Entidades.DiasLaborables;
import InterfacePersistencia.PersistenciaDiasLaborablesInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 * Clase Stateless. <br>
 * Clase encargada de realizar operaciones sobre la tabla 'DiasLaborables' de la
 * base de datos
 *
 * @author Andrés Pineda
 */
@Stateless
public class PersistenciaDiasLaborables implements PersistenciaDiasLaborablesInterface {

   /**
    * Atributo EntityManager. Representa la comunicación con la base de datos
    */
   /*@PersistenceContext(unitName = "DesignerRHN-ejbPU")
    private EntityManager em;*/
   @Override
   public void crear(EntityManager em, DiasLaborables diasLaborables) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.persist(diasLaborables);
         tx.commit();
      } catch (Exception e) {
         System.out.println("Error PersistenciaDiasLaborables.crear: " + e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void editar(EntityManager em, DiasLaborables diasLaborables) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(diasLaborables);
         tx.commit();
      } catch (Exception e) {
         System.out.println("Error PersistenciaDiasLaborables.editar: " + e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void borrar(EntityManager em, DiasLaborables diasLaborables) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.remove(em.merge(diasLaborables));
         tx.commit();

      } catch (Exception e) {
         if (tx.isActive()) {
            tx.rollback();
         }
         System.out.println("Error PersistenciaDiasLaborables.borrar: " + e);
      }
   }

   @Override
   public DiasLaborables buscarDiaLaborableSecuencia(EntityManager em, BigInteger secDiaLaboral) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT dl FROM DiasLaborables dl WHERE dl.secuencia =:secuencia");
         query.setParameter("secuencia", secDiaLaboral);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         DiasLaborables diasLaborables = (DiasLaborables) query.getSingleResult();
         return diasLaborables;
      } catch (Exception e) {
         System.out.println("Persistencia.PersistenciaDiasLaborables.buscarDiaLaborableSecuencia() e: " + e);
         DiasLaborables diasLaborables = null;
         return diasLaborables;
      }
   }

   @Override
   public List<DiasLaborables> diasLaborables(EntityManager em) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT dl FROM DiasLaborables dl");
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         List<DiasLaborables> diasLaborables = query.getResultList();
         return diasLaborables;
      } catch (Exception e) {
         System.out.println("Persistencia.PersistenciaDiasLaborables.diasLaborables() e: " + e);
         return null;
      }
   }

   @Override
   public List<DiasLaborables> diasLaborablesParaSecuenciaTipoContrato(EntityManager em, BigInteger secTipoContrato) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT dl FROM DiasLaborables dl WHERE dl.tipocontrato.secuencia=:secuencia");
         query.setParameter("secuencia", secTipoContrato);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         List<DiasLaborables> diasLaborables = query.getResultList();
         return diasLaborables;
      } catch (Exception e) {
         System.out.println("Persistencia.PersistenciaDiasLaborables.diasLaborablesParaSecuenciaTipoContrato() e: " + e);
         return null;
      }
   }

}
