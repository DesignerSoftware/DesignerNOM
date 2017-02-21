/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.Evalplanillas;
import InterfacePersistencia.PersistenciaEvalPlanillasInterface;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 *
 * @author user
 */
@Stateless
public class PersistenciaEvalPlanillas implements PersistenciaEvalPlanillasInterface {

   @Override
   public void crear(EntityManager em, Evalplanillas evalplanilla) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(evalplanilla);
         tx.commit();
      } catch (Exception e) {
         System.out.println("Error PersistenciaEvalPlanillas.crear: " + e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void editar(EntityManager em, Evalplanillas evalplanilla) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(evalplanilla);
         tx.commit();
      } catch (Exception e) {
         System.out.println("Error PersistenciaEvalPlanillas.editar: " + e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void borrar(EntityManager em, Evalplanillas evalplanilla) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.remove(em.merge(evalplanilla));
         tx.commit();

      } catch (Exception e) {
         if (tx.isActive()) {
            tx.rollback();
         }
         System.out.println("Error PersistenciaEvalPlanillas.borrar: " + e);
      }
   }

   @Override
   public List<Evalplanillas> consultarEvalPlanilla(EntityManager em) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT ep FROM Evalplanillas ep ORDER BY ep.codigo ");
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         List<Evalplanillas> evalPlanilla = query.getResultList();
         return evalPlanilla;
      } catch (Exception e) {
         System.out.println("Error Persistencia.PersistenciaEvalPlanillas.consultarEvalPlanilla(): " + e);
         return null;
      }
   }

}
