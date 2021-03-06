/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.DetallesExtrasRecargos;
import InterfacePersistencia.PersistenciaDetallesExtrasRecargosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 * Clase Stateless. <br>
 * Clase encargada de realizar operaciones sobre la tabla
 * 'DetallesExtrasRecargos' de la base de datos.
 *
 * @author Andres Pineda.
 */
@Stateless
public class PersistenciaDetallesExtrasRecargos implements PersistenciaDetallesExtrasRecargosInterface {

   private static Logger log = Logger.getLogger(PersistenciaDetallesExtrasRecargos.class);

   /**
    * Atributo EntityManager. Representa la comunicación con la base de datos.
    */
   /*@PersistenceContext(unitName = "DesignerRHN-ejbPU")
     private EntityManager em;*/
   @Override
   public void crear(EntityManager em, DetallesExtrasRecargos detallesExtrasRecargos) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(detallesExtrasRecargos);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaDetallesExtrasRecargos.crear:  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void editar(EntityManager em, DetallesExtrasRecargos detallesExtrasRecargos) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(detallesExtrasRecargos);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaDetallesExtrasRecargos.crear:  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void borrar(EntityManager em, DetallesExtrasRecargos detallesExtrasRecargos) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.remove(em.merge(detallesExtrasRecargos));
         tx.commit();

      } catch (Exception e) {
         if (tx.isActive()) {
            tx.rollback();
         }
         log.error("Error PersistenciaDetallesExtrasRecargos.borrar:  ", e);
      }
   }

   @Override
   public DetallesExtrasRecargos buscarDetalleExtraRecargo(EntityManager em, BigInteger secuencia) {
      try {
         em.clear();
         return em.find(DetallesExtrasRecargos.class, secuencia);
      } catch (Exception e) {
         log.error("Error PersistenciaDetallesExtrasRecargos buscarDetalleExtraRecargo :  ", e);
         return null;
      }
   }

   @Override
   public List<DetallesExtrasRecargos> buscaDetallesExtrasRecargos(EntityManager em) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT der FROM DetallesExtrasRecargos der ORDER BY der.codigo DESC");
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         List<DetallesExtrasRecargos> extrasRecargos = query.getResultList();
         return extrasRecargos;
      } catch (Exception e) {
         log.error("Error PersistenciaDetallesExtrasRecargos buscaDetallesExtrasRecargos :  ", e);
         return null;
      }
   }

   @Override
   public List<DetallesExtrasRecargos> buscaDetallesExtrasRecargosPorSecuenciaExtraRecargo(EntityManager em, BigInteger secuencia) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT der FROM DetallesExtrasRecargos der WHERE der.extrarecargo.secuencia=:secuencia");
         query.setParameter("secuencia", secuencia);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         List<DetallesExtrasRecargos> extrasRecargos = query.getResultList();
         return extrasRecargos;
      } catch (Exception e) {
         log.error("Error PersistenciaDetallesExtrasRecargos buscaDetallesExtrasRecargosPorSecuenciaExtraRecargo :  ", e);
         return null;
      }
   }

}
