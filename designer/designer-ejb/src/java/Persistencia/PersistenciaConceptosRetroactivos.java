/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.ConceptosRetroactivos;
import InterfacePersistencia.PersistenciaConceptosRetroactivosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.criteria.CriteriaQuery;

/**
 *
 * @author user
 */
@Stateless
public class PersistenciaConceptosRetroactivos implements PersistenciaConceptosRetroactivosInterface {

   private static Logger log = Logger.getLogger(PersistenciaConceptosRetroactivos.class);

   public void crear(EntityManager em, ConceptosRetroactivos conceptosRetroactivos) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(conceptosRetroactivos);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaConceptosRetroactivos.crear: " + e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   public void editar(EntityManager em, ConceptosRetroactivos conceptosRetroactivos) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(conceptosRetroactivos);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaConceptosRetroactivos.editar: " + e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   public void borrar(EntityManager em, ConceptosRetroactivos conceptosRetroactivos) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.remove(em.merge(conceptosRetroactivos));
         tx.commit();
      } catch (Exception e) {
         if (tx.isActive()) {
            tx.rollback();
         }
         log.error("Error PersistenciaConceptosRetroactivos.borrar: " + e);
      }
   }

   public ConceptosRetroactivos buscarConceptoProyeccion(EntityManager em, BigInteger secuencia) {
      try {
         em.clear();
         return em.find(ConceptosRetroactivos.class, secuencia);
      } catch (Exception e) {
         log.error("Error buscarDeporte PersistenciaConceptosRetroactivos : " + e.toString());
         return null;
      }
   }

   public List<ConceptosRetroactivos> buscarConceptosRetroactivos(EntityManager em) {
      try {
         em.clear();
         CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
         cq.select(cq.from(ConceptosRetroactivos.class));
         return em.createQuery(cq).getResultList();
      } catch (Exception e) {
         log.error("Error buscarConceptosRetroactivos PersistenciaConceptosRetroactivos Error : " + e);
         return null;
      }
   }
}
