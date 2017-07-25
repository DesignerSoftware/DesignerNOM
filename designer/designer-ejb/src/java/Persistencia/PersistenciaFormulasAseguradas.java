/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.FormulasAseguradas;
import InterfacePersistencia.PersistenciaFormulasAseguradasInterface;
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
public class PersistenciaFormulasAseguradas implements PersistenciaFormulasAseguradasInterface {

   private static Logger log = Logger.getLogger(PersistenciaFormulasAseguradas.class);

   /**
    * Atributo EntityManager. Representa la comunicación con la base de datos.
    */
   /*@PersistenceContext(unitName = "DesignerRHN-ejbPU")
     private EntityManager em;*/
   public void crear(EntityManager em, FormulasAseguradas formulasAseguradas) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(formulasAseguradas);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaVigenciasCargos.crear: " + e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   public void editar(EntityManager em, FormulasAseguradas formulasAseguradas) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(formulasAseguradas);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaVigenciasCargos.crear: " + e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   public void borrar(EntityManager em, FormulasAseguradas formulasAseguradas) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.remove(em.merge(formulasAseguradas));
         tx.commit();

      } catch (Exception e) {
         if (tx.isActive()) {
            tx.rollback();
         }
         log.error("Error PersistenciaVigenciasCargos.borrar: " + e);
      }
   }

   public List<FormulasAseguradas> consultarFormulasAseguradas(EntityManager em) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT te FROM FormulasAseguradas te");
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         List<FormulasAseguradas> formulasAseguradas = query.getResultList();
         return formulasAseguradas;
      } catch (Exception e) {
         log.error("Error consultarFormulasAseguradas");
         return null;
      }
   }

   public FormulasAseguradas consultarFormulaAsegurada(EntityManager em, BigInteger secuencia) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT te FROM FormulasAseguradas te WHERE te.secuencia = :secuencia");
         query.setParameter("secuencia", secuencia);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         FormulasAseguradas formulasAseguradas = (FormulasAseguradas) query.getSingleResult();
         return formulasAseguradas;
      } catch (Exception e) {
         log.error("Error consultarFormulasAseguradas");
         FormulasAseguradas formulasAseguradas = null;
         return formulasAseguradas;
      }
   }
}
