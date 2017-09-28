/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.ConceptosJuridicos;
import InterfacePersistencia.PersistenciaConceptosJuridicosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 * Clase Stateless. <br>
 * Clase encargada de realizar operaciones sobre la tabla 'ConceptosJuridicos'
 * de la base de datos.
 *
 * @author Andres Pineda.
 */
@Stateless
public class PersistenciaConceptosJuridicos implements PersistenciaConceptosJuridicosInterface {

   private static Logger log = Logger.getLogger(PersistenciaConceptosJuridicos.class);

   /**
    * Atributo EntityManager. Representa la comunicación con la base de datos
    */
   /* @PersistenceContext(unitName = "DesignerRHN-ejbPU")
     private EntityManager em;*/
   @Override
   public void crear(EntityManager em, ConceptosJuridicos conceptosJuridicos) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(conceptosJuridicos);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaConceptosJuridicos.crear:  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void editar(EntityManager em, ConceptosJuridicos conceptosJuridicos) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(conceptosJuridicos);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaConceptosJuridicos.crear:  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void borrar(EntityManager em, ConceptosJuridicos conceptosJuridicos) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.remove(em.merge(conceptosJuridicos));
         tx.commit();

      } catch (Exception e) {
         if (tx.isActive()) {
            tx.rollback();
         }
         log.error("Error PersistenciaConceptosJuridicos.borrar:  ", e);
      }
   }

   @Override
   public List<ConceptosJuridicos> buscarConceptosJuridicos(EntityManager em) {
      try {
         em.clear();
         List<ConceptosJuridicos> conceptosJuridicos = (List<ConceptosJuridicos>) em.createQuery("SELECT c FROM ConceptosJuridicos c").getResultList();
         return conceptosJuridicos;
      } catch (Exception e) {
         log.error("Error buscarConceptosJuridicos PersistenciaConceptosJuridicos :  ", e);
         return null;
      }
   }

   @Override
   public ConceptosJuridicos buscarConceptosJuridicosSecuencia(EntityManager em, BigInteger secuencia) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT cj FROM ConceptosJuridicos cj WHERE cj.secuencia = :secuencia");
         query.setParameter("secuencia", secuencia);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         ConceptosJuridicos conceptosJuridicos = (ConceptosJuridicos) query.getSingleResult();
         return conceptosJuridicos;
      } catch (Exception e) {
         log.error("Error buscarConceptosJuridicosSecuencia PersistenciaConceptosJuridicos :  ", e);
         ConceptosJuridicos conceptosJuridicos = null;
         return conceptosJuridicos;
      }
   }

   @Override
   public List<ConceptosJuridicos> buscarConceptosJuridicosEmpresa(EntityManager em, BigInteger secuencia) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT cj FROM ConceptosJuridicos cj WHERE cj.empresa.secuencia = :secuencia");
         query.setParameter("secuencia", secuencia);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         List<ConceptosJuridicos> conceptosJuridicos = (List<ConceptosJuridicos>) query.getResultList();
         return conceptosJuridicos;
      } catch (Exception e) {
         log.error("Error buscarConceptosJuridicosEmpresa PersistenciaConceptosJuridicos :  ", e);
         List<ConceptosJuridicos> conceptosJuridicos = null;
         return conceptosJuridicos;
      }
   }
}
