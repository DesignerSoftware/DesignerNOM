/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.TiposFunciones;
import InterfacePersistencia.PersistenciaTiposFuncionesInterface;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class PersistenciaTiposFunciones implements PersistenciaTiposFuncionesInterface {

   private static Logger log = Logger.getLogger(PersistenciaTiposFunciones.class);

   @Override
   public void crear(EntityManager em, TiposFunciones tiposFunciones) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(tiposFunciones);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaTiposFunciones.crear:  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void editar(EntityManager em, TiposFunciones tiposFunciones) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(tiposFunciones);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaTiposFunciones.editar:  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void borrar(EntityManager em, TiposFunciones tiposFunciones) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.remove(em.merge(tiposFunciones));
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaTiposFunciones.borrar:  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public List<TiposFunciones> tiposFunciones(EntityManager em, BigInteger secuenciaOperando) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT DISTINCT tf FROM TiposFunciones tf, Operandos op WHERE tf.operando.secuencia =:secuenciaOperando and op.tipo='FUNCION'");
         query.setParameter("secuenciaOperando", secuenciaOperando);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         List<TiposFunciones> tiposFunciones = query.getResultList();
         List<TiposFunciones> tiposFuncionesResult = new ArrayList<TiposFunciones>(tiposFunciones);
         return tiposFuncionesResult;
      } catch (Exception e) {
         log.error("PersistenciaTiposFunciones.tiposFunciones():  ", e);
         return null;
      }
   }
}
