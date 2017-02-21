/**
 * Documentación a cargo de AndresPineda
 */
package Persistencia;

import Entidades.Competenciascargos;
import InterfacePersistencia.PersistenciaCompetenciasCargosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 * Clase Stateless. <br>
 * Clase encargada de realizar operaciones sobre la tabla 'CompetenciasCargos'
 * de la base de datos.
 *
 * @author AndresPineda
 */
@Stateless
public class PersistenciaCompetenciasCargos implements PersistenciaCompetenciasCargosInterface {

   /**
    * Atributo EntityManager. Representa la comunicación con la base de datos.
    */
   /*@PersistenceContext(unitName = "DesignerRHN-ejbPU")
    private EntityManager em;*/
   @Override
   public void crear(EntityManager em, Competenciascargos competenciascargos) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.persist(competenciascargos);
         tx.commit();
      } catch (Exception e) {
         System.out.println("Error PersistenciaCompetenciasCargos.crear: " + e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void editar(EntityManager em, Competenciascargos competenciascargos) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.persist(competenciascargos);
         tx.commit();
      } catch (Exception e) {
         System.out.println("Error PersistenciaCompetenciasCargos.editar: " + e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void borrar(EntityManager em, Competenciascargos competenciascargos) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.remove(em.merge(competenciascargos));
         tx.commit();
      } catch (Exception e) {
         if (tx.isActive()) {
            tx.rollback();
         }
         System.out.println("Error PersistenciaCompetenciasCargos.borrar: " + e);
      }
   }

   @Override
   public List<Competenciascargos> buscarCompetenciasCargos(EntityManager em) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT cc FROM Competenciascargos cc ORDER BY cc.peso ASC");
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         List<Competenciascargos> competenciascargos = query.getResultList();
         return competenciascargos;
      } catch (Exception e) {
         System.out.println("Error buscarCompetenciasCargos PersistenciaCompetenciasCargos : " + e.toString());
         return null;
      }
   }

   @Override
   public Competenciascargos buscarCompetenciasCargosSecuencia(EntityManager em, BigInteger secuencia) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT cc FROM Competenciascargos cc WHERE cc.secuencia = :secuencia");
         query.setParameter("secuencia", secuencia);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         Competenciascargos competenciascargos = (Competenciascargos) query.getSingleResult();
         return competenciascargos;
      } catch (Exception e) {
         System.out.println("Error buscarCompetenciasCargosSecuencia PersistenciaCompetenciasCargos : " + e.toString());
         Competenciascargos competenciascargos = null;
         return competenciascargos;
      }
   }

   @Override
   public List<Competenciascargos> buscarCompetenciasCargosParaSecuenciaCargo(EntityManager em, BigInteger secCargo) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT cc FROM Competenciascargos cc WHERE cc.cargo.secuencia=:secCargo");
         query.setParameter("secCargo", secCargo);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         List<Competenciascargos> competenciascargos = query.getResultList();
         return competenciascargos;
      } catch (Exception e) {
         System.out.println("Error buscarCompetenciasCargosParaSecuenciaCargo PersistenciaCompetenciasCargos : " + e.toString());
         return null;
      }
   }
}
