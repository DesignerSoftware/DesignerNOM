/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.TiposFormulas;
import InterfacePersistencia.PersistenciaTiposFormulasInterface;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

@Stateless
public class PersistenciaTiposFormulas implements PersistenciaTiposFormulasInterface {

   private static Logger log = Logger.getLogger(PersistenciaTiposFormulas.class);

   @Override
   public void crear(EntityManager em, TiposFormulas tiposFormulas) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(tiposFormulas);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaTiposFormulas.crear:  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void editar(EntityManager em, TiposFormulas tiposFormulas) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(tiposFormulas);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaTiposFormulas.editar:  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void borrar(EntityManager em, TiposFormulas tiposFormulas) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.remove(em.merge(tiposFormulas));
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaTiposFormulas.borrar:  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public List<TiposFormulas> tiposFormulas(EntityManager em, BigInteger secuenciaOperando) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT DISTINCT tf FROM TiposFormulas tf, Operandos op WHERE tf.operando.secuencia =:secuenciaOperando and op.tipo = 'FORMULA' ORDER BY tf.fechafinal DESC");
         query.setParameter("secuenciaOperando", secuenciaOperando);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         List<TiposFormulas> tiposFormulas = query.getResultList();
         List<TiposFormulas> tiposFormulasResult = new ArrayList<TiposFormulas>(tiposFormulas);

         log.warn(this.getClass().getName() + ".tiposFormulas() tiposFormulasResult: " + tiposFormulasResult);
         return tiposFormulasResult;
      } catch (Exception e) {
         log.error("PersistenciaTiposFormulas.tiposFormulas():  ", e);
         return null;
      }
   }
}
