/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.EstadosAfiliaciones;
import InterfacePersistencia.PersistenciaEstadosAfiliacionesInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.criteria.CriteriaQuery;

/**
 * Clase Stateless. <br>
 * Clase encargada de realizar operaciones sobre la tabla 'EstadosAfiliaciones'
 * de la base de datos.
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaEstadosAfiliaciones implements PersistenciaEstadosAfiliacionesInterface {

   private static Logger log = Logger.getLogger(PersistenciaEstadosAfiliaciones.class);

   /**
    * Atributo EntityManager. Representa la comunicación con la base de datos
    */
   /*@PersistenceContext(unitName = "DesignerRHN-ejbPU")
    private EntityManager em;*/

   @Override
   public void crear(EntityManager em, EstadosAfiliaciones afiliaciones) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(afiliaciones);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaEstadosAfiliaciones.crear:  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void editar(EntityManager em, EstadosAfiliaciones afiliaciones) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(afiliaciones);
         tx.commit();
      } catch (Exception e) {
         if (tx.isActive()) {
            tx.rollback();
         }
         log.error("Error PersistenciaEstadosAfiliaciones.editar:  ", e);
      }
   }

   @Override
   public void borrar(EntityManager em, EstadosAfiliaciones afiliaciones) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.remove(em.merge(afiliaciones));
         tx.commit();

      } catch (Exception e) {
         try {
            if (tx.isActive()) {
               tx.rollback();
            }
         } catch (Exception ex) {
            log.error("Error PersistenciaEstadosAfiliaciones.borrar:  ", e);
         }
      }
   }

   @Override
   public EstadosAfiliaciones buscarEstadoAfiliacion(EntityManager em, BigInteger secuencia) {
      try {
         em.clear();
         return em.find(EstadosAfiliaciones.class, secuencia);
      } catch (Exception e) {
         log.error("Error buscarbanco persistencia bancos :  ", e);
         return null;
      }
   }

   @Override
   public List<EstadosAfiliaciones> buscarEstadosAfiliaciones(EntityManager em) {
      try {
         em.clear();
         CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
         cq.select(cq.from(EstadosAfiliaciones.class));
         return em.createQuery(cq).getResultList();
      } catch (Exception e) {
         log.error("Error buscarBancos persistencia bancos  ", e);
         return null;
      }
   }
}
