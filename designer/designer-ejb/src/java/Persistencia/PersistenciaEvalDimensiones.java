/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import InterfacePersistencia.PersistenciaEvalDimensionesInterface;
import javax.persistence.EntityManager;
import Entidades.EvalDimensiones;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 *
 * @author user
 */
@Stateless
public class PersistenciaEvalDimensiones implements PersistenciaEvalDimensionesInterface {

   /**
    * Atributo EntityManager. Representa la comunicación con la base de datos
    */
   /*@PersistenceContext(unitName = "DesignerRHN-ejbPU")
     private EntityManager em;*/
   public void crear(EntityManager em, EvalDimensiones evalDimensiones) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(evalDimensiones);
         tx.commit();
      } catch (Exception e) {
         System.out.println("Error PersistenciaEvalDimensiones.crear: " + e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   public void editar(EntityManager em, EvalDimensiones evalDimensiones) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(evalDimensiones);
         tx.commit();
      } catch (Exception e) {
         System.out.println("Error PersistenciaEvalDimensiones.editar: " + e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   public void borrar(EntityManager em, EvalDimensiones evalDimensiones) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.remove(em.merge(evalDimensiones));
         tx.commit();

      } catch (Exception e) {
         System.out.println("Error PersistenciaEvalDimensiones.borrar: " + e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   public EvalDimensiones buscarEvalDimension(EntityManager em, BigInteger secuencia) {
      try {
         em.clear();
         return em.find(EvalDimensiones.class, secuencia);
      } catch (Exception e) {
         System.out.println("Persistencia.PersistenciaEvalDimensiones.buscarEvalDimension() e: " + e);
         return null;
      }
   }

   public List<EvalDimensiones> buscarEvalDimensiones(EntityManager em) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT ed FROM EvalDimensiones ed ORDER BY ed.descripcion ");
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         List<EvalDimensiones> evalDimensiones = query.getResultList();
         return evalDimensiones;
      } catch (Exception e) {
         System.err.println("PERSISTENCIAEVALDIMENSIONES BUSCAREVALDIMENSIONES ERROR : " + e);
         return null;
      }
   }

   public BigInteger contradorEvalPlanillas(EntityManager em, BigInteger secuencia) {
      BigInteger retorno;
      try {
         em.clear();
         String sqlQuery = "SELECT COUNT(*)FROM evaldimensiones ev, evalplanillas ep  WHERE ep.dimension=ev.secuencia AND ev.secuencia = ? ";
         Query query = em.createNativeQuery(sqlQuery);
         query.setParameter(1, secuencia);
         retorno = new BigInteger(query.getSingleResult().toString());
         System.out.println("PERSISTENCIAEVALDIMENSIONES contradorEvalPlanillas = " + retorno);
         return retorno;
      } catch (Exception e) {
         System.err.println("ERROR PERSISTENCIAEVALDIMENSIONES contradorEvalPlanillas  ERROR = " + e);
         retorno = new BigInteger("-1");
         return retorno;
      }
   }
}
