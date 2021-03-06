/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.Evalconvocatorias;
import InterfacePersistencia.PersistenciaEvalConvocatoriasInterface;
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
public class PersistenciaEvalConvocatorias implements PersistenciaEvalConvocatoriasInterface {

   private static Logger log = Logger.getLogger(PersistenciaEvalConvocatorias.class);

   @Override
   public void crear(EntityManager em, Evalconvocatorias evalconvocatoria) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(evalconvocatoria);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaEvalConvocatorias.crear:  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void editar(EntityManager em, Evalconvocatorias evalconvocatoria) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(evalconvocatoria);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaEvalConvocatorias.editar:  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void borrar(EntityManager em, Evalconvocatorias evalconvocatoria) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.remove(em.merge(evalconvocatoria));
         tx.commit();

      } catch (Exception e) {
         if (tx.isActive()) {
            tx.rollback();
         }
         log.error("Error PersistenciaEvalConvocatorias.borrar:  ", e);
      }
   }

   @Override
   public List<Evalconvocatorias> consultarEvalConvocatorias(EntityManager em, BigInteger secuenciaEmpleado) {
      try {
         em.clear();
         String sql = "SELECT * FROM EVALCONVOCATORIAS WHERE EXISTS(SELECT 'X' FROM EVALRESULTADOSCONV B \n"
                 + "WHERE B.EMPLEADO= ? AND EVALCONVOCATORIAS.SECUENCIA=B.EVALCONVOCATORIA)";
         Query query = em.createNativeQuery(sql, Evalconvocatorias.class);
         query.setParameter(1, secuenciaEmpleado);
         List<Evalconvocatorias> evalconvocatorias = query.getResultList();
         return evalconvocatorias;
      } catch (Exception e) {
         log.error("Error en PersistenciaEvalConvocatorias.consultarEvalConvocatorias ERROR:  ", e);
         return null;
      }
   }

   @Override
   public List<Evalconvocatorias> consultarEvalConvocatorias(EntityManager em) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT ec FROM Evalconvocatorias ec ORDER BY ec.codigo ");
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         List<Evalconvocatorias> evalConvocatorias = query.getResultList();
         return evalConvocatorias;
      } catch (Exception e) {
         log.error("Error Persistencia.PersistenciaEvalConvocatorias.consultarEvalConvocatorias():  ", e);
         return null;
      }
   }

}
