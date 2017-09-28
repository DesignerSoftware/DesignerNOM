/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.TiposConstantes;
import InterfacePersistencia.PersistenciaTiposConstantesInterface;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

@Stateless
public class PersistenciaTiposConstantes implements PersistenciaTiposConstantesInterface {

   private static Logger log = Logger.getLogger(PersistenciaTiposConstantes.class);

   @Override
   public void crear(EntityManager em, TiposConstantes tiposConstantes) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(tiposConstantes);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaTiposConstantes.crear:  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void editar(EntityManager em, TiposConstantes tiposConstantes) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(tiposConstantes);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaTiposConstantes.editar:  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void borrar(EntityManager em, TiposConstantes tiposConstantes) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.remove(em.merge(tiposConstantes));
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaTiposConstantes.borrar:  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public List<TiposConstantes> tiposConstantes(EntityManager em, BigInteger secuenciaOperando) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT DISTINCT tf FROM TiposConstantes tf, Operandos op WHERE tf.operando.secuencia =:secuenciaOperando and op.tipo = 'CONSTANTE' ORDER BY tf.fechafinal DESC");
         query.setParameter("secuenciaOperando", secuenciaOperando);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         List<TiposConstantes> tiposConstantes = query.getResultList();
         List<TiposConstantes> tiposConstantesResult = new ArrayList<TiposConstantes>(tiposConstantes);
         return tiposConstantesResult;
      } catch (Exception e) {
         log.error("PersistenciaTiposConstantes.tiposConstantes():  ", e);
         return null;
      }
   }
}
