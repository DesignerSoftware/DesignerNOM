/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.ConceptosProyecciones;
import InterfacePersistencia.PersistenciaConceptosProyeccionesInterface;
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
public class PersistenciaConceptosProyecciones implements PersistenciaConceptosProyeccionesInterface {

   private static Logger log = Logger.getLogger(PersistenciaConceptosProyecciones.class);

   public void crear(EntityManager em, ConceptosProyecciones contadorProyecciones) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(contadorProyecciones);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaConceptosProyecciones.crear:  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   public void editar(EntityManager em, ConceptosProyecciones contadorProyecciones) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(contadorProyecciones);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaConceptosProyecciones.editar:  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   public void borrar(EntityManager em, ConceptosProyecciones contadorProyecciones) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.remove(em.merge(contadorProyecciones));
         tx.commit();

      } catch (Exception e) {
         if (tx.isActive()) {
            tx.rollback();
         }
         log.error("Error PersistenciaConceptosProyecciones.borrar:  ", e);
      }
   }

   public ConceptosProyecciones buscarConceptoProyeccion(EntityManager em, BigInteger secuencia) {
      try {
         em.clear();
         return em.find(ConceptosProyecciones.class, secuencia);
      } catch (Exception e) {
         log.error("Error buscarDeporte PersistenciaConceptosProyecciones :  ", e);
         return null;
      }
   }

   public List<ConceptosProyecciones> buscarConceptosProyecciones(EntityManager em) {
      try {
         em.clear();
         CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
         cq.select(cq.from(ConceptosProyecciones.class));
         return em.createQuery(cq).getResultList();
      } catch (Exception e) {
         log.error("Error buscarConceptosProyecciones PersistenciaConceptosProyecciones Error :  ", e);
         return null;
      }
   }

}
