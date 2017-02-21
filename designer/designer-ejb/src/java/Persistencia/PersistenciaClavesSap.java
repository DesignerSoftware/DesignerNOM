/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.ClavesSap;
import InterfacePersistencia.PersistenciaClavesSapInterface;
import java.math.BigInteger;
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
public class PersistenciaClavesSap implements PersistenciaClavesSapInterface {

   public void crear(EntityManager em, ClavesSap clavesap) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         System.out.println("PersitenciaClavesSap sencuencia " + clavesap.getSecuencia());
         System.out.println("PersitenciaClavesSap Clave Ajuste sencuencia " + clavesap.getClaveAjuste().getSecuencia());
         tx.begin();
         em.merge(clavesap);
         tx.commit();
      } catch (Exception e) {
         System.out.println("Error PersistenciaClavesSap.crear: " + e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   public void editar(EntityManager em, ClavesSap clavesap) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(clavesap);
         tx.commit();
      } catch (Exception e) {
         System.out.println("Error PersistenciaClavesSap.editar: " + e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   public void borrar(EntityManager em, ClavesSap clavesap) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.remove(em.merge(clavesap));
         tx.commit();
      } catch (Exception e) {
         if (tx.isActive()) {
            tx.rollback();
         }
         System.out.println("Error PersistenciaClavesSap.borrar: " + e);
      }
   }

   public List<ClavesSap> consultarClavesSap(EntityManager em) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT cp FROM ClavesSap cp");
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         List<ClavesSap> claseP = query.getResultList();
         return claseP;
      } catch (Exception e) {
         System.out.println("Error buscarClasePennsion PersistenciaClavesSap");
         List<ClavesSap> claseP = null;
         return claseP;
      }
   }

   public BigInteger contarClavesContablesCreditoClaveSap(EntityManager em, BigInteger secuencia) {
      BigInteger retorno = new BigInteger("-1");
      try {
         String sqlQuery = "select count(*)from conceptos where clavecontablecredito = ?";
         Query query = em.createNativeQuery(sqlQuery);
         query.setParameter(1, secuencia);
         retorno = new BigInteger(query.getSingleResult().toString());
         System.err.println("Contador PersistenciaClavesSap  contarClavesContablesCreditoClaveSap  " + retorno);
         return retorno;
      } catch (Exception e) {
         System.out.println("Error PersistenciaClavesSap   contarClavesContablesCreditoClaveSap. " + e);
         return retorno;
      }
   }

   public BigInteger contarClavesContablesDebitoClaveSap(EntityManager em, BigInteger secuencia) {
      BigInteger retorno = new BigInteger("-1");
      try {
         String sqlQuery = "select count(*)from conceptos where clavecontabledebito = ?";
         Query query = em.createNativeQuery(sqlQuery);
         query.setParameter(1, secuencia);
         retorno = new BigInteger(query.getSingleResult().toString());
         System.err.println("Contador PersistenciaClavesSap  contarClavesContablesCreditoClaveSap  " + retorno);
         return retorno;
      } catch (Exception e) {
         System.out.println("Error PersistenciaClavesSap   contarClavesContablesCreditoClaveSap. " + e);
         return retorno;
      }
   }
}
