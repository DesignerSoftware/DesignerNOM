/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.SectoresEvaluaciones;
import InterfacePersistencia.PersistenciaSectoresEvaluacionesInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 *
 * @author user
 */
@Stateless
public class PersistenciaSectoresEvaluaciones implements PersistenciaSectoresEvaluacionesInterface {

   private static Logger log = Logger.getLogger(PersistenciaSectoresEvaluaciones.class);

   /**
    * Atributo EntityManager. Representa la comunicación con la base de datos.
    */
//    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
//    private EntityManager em;
   @Override
   public void crear(EntityManager em, SectoresEvaluaciones sectoresEvaluaciones) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(sectoresEvaluaciones);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaSectoresEvaluaciones.crear:  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void editar(EntityManager em, SectoresEvaluaciones sectoresEvaluaciones) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(sectoresEvaluaciones);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaSectoresEvaluaciones.editar:  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void borrar(EntityManager em, SectoresEvaluaciones sectoresEvaluaciones) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.remove(em.merge(sectoresEvaluaciones));
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaSectoresEvaluaciones.borrar:  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public List<SectoresEvaluaciones> consultarSectoresEvaluaciones(EntityManager em) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT l FROM SectoresEvaluaciones  l ORDER BY l.codigo ASC ");
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         List<SectoresEvaluaciones> listTiposViajeros = query.getResultList();
         return listTiposViajeros;
      } catch (Exception e) {
         log.error("ERROR PersistenciaTiposViajeros ConsultarTiposViajeros ERROR :  ", e);
         return null;
      }

   }

   @Override
   public SectoresEvaluaciones consultarSectorEvaluacion(EntityManager em, BigInteger secuencia) {

      try {
         em.clear();
         Query query = em.createQuery("SELECT mr FROM SectoresEvaluaciones mr WHERE mr.secuencia = :secuencia");
         query.setParameter("secuencia", secuencia);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         SectoresEvaluaciones motivoR = (SectoresEvaluaciones) query.getSingleResult();
         return motivoR;
      } catch (Exception e) {
         log.error("PersistenciaSectoresEvaluaciones.consultarSectorEvaluacion():  ", e);
         return null;
      }

   }
}
