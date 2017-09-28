/**
 * Documentación a cargo de AndresPineda
 */
package Persistencia;

import Entidades.DetallesCargos;
import InterfacePersistencia.PersistenciaDetallesCargosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 * Clase Stateless. <br>
 * Clase encargada de realizar operaciones sobre la tabla 'DetallesCargos' de la
 * base de datos.
 *
 * @author AndresPineda
 */
@Stateless
public class PersistenciaDetallesCargos implements PersistenciaDetallesCargosInterface {

   private static Logger log = Logger.getLogger(PersistenciaDetallesCargos.class);

   /**
    * Atributo EntityManager. Representa la comunicación con la base de datos.
    */
   /* @PersistenceContext(unitName = "DesignerRHN-ejbPU")
    private EntityManager em;*/
   @Override
   public void crear(EntityManager em, DetallesCargos detallesCargos) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.persist(detallesCargos);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaVigenciasCargos.crear:  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void editar(EntityManager em, DetallesCargos detallesCargos) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(detallesCargos);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaVigenciasCargos.editar:  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void borrar(EntityManager em, DetallesCargos detallesCargos) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.remove(em.merge(detallesCargos));
         tx.commit();

      } catch (Exception e) {
         if (tx.isActive()) {
            tx.rollback();
         }
         log.error("Error PersistenciaVigenciasCargos.borrar:  ", e);
      }
   }

   @Override
   public List<DetallesCargos> buscarDetallesCargos(EntityManager em) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT cc FROM DetallesCargos cc ORDER BY cc.peso ASC");
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         List<DetallesCargos> detallesCargos = query.getResultList();
         return detallesCargos;
      } catch (Exception e) {
         log.error("Error buscarDetallesCargos PersistenciaDetallesCargos :  ", e);
         return null;
      }
   }

   @Override
   public DetallesCargos buscarDetallesCargosSecuencia(EntityManager em, BigInteger secuencia) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT dc FROM DetallesCargos dc WHERE dc.secuencia = :secuencia");
         query.setParameter("secuencia", secuencia);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         DetallesCargos detallesCargos = (DetallesCargos) query.getSingleResult();
         return detallesCargos;
      } catch (Exception e) {
         log.error("Error buscarDetallesCargosSecuencia PersistenciaDetallesCargos :  ", e);
         DetallesCargos detallesCargos = null;
         return detallesCargos;
      }
   }

   @Override
   public DetallesCargos buscarDetalleCargoParaSecuenciaTipoDetalle(EntityManager em, BigInteger secTipoDetalle, BigInteger secCargo) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT dc FROM DetallesCargos dc WHERE dc.tipodetalle.secuencia=:secTipoDetalle AND dc.cargo.secuencia=:secCargo");
         query.setParameter("secTipoDetalle", secTipoDetalle);
         query.setParameter("secCargo", secCargo);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         DetallesCargos detallesCargos = (DetallesCargos) query.getSingleResult();
         return detallesCargos;
      } catch (Exception e) {
         log.error("Error buscarDetalleCargoParaSecuenciaTipoDetalle PersistenciaDetallesCargos :  ", e);
         return null;
      }
   }

   @Override
   public List<DetallesCargos> buscarDetallesCargosDeCargoSecuencia(EntityManager em, BigInteger secuencia) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT dc FROM DetallesCargos dc WHERE dc.cargo.secuencia=:secuencia");
         query.setParameter("secuencia", secuencia);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         List<DetallesCargos> detallesCargos = query.getResultList();
         return detallesCargos;
      } catch (Exception e) {
         log.error("Error buscarDetallesCargosDeCargoSecuencia PersistenciaDetallesCargos :  ", e);
         List<DetallesCargos> detallesCargos = null;
         return detallesCargos;
      }
   }

}
