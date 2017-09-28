/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.EvalActividades;
import InterfacePersistencia.PersistenciaEvalActividadesInterface;
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
public class PersistenciaEvalActividades implements PersistenciaEvalActividadesInterface {

   private static Logger log = Logger.getLogger(PersistenciaEvalActividades.class);

   /**
    * Atributo EntityManager. Representa la comunicación con la base de datos.
    */
   /* @PersistenceContext(unitName = "DesignerRHN-ejbPU")
     private EntityManager em;*/
   public void crear(EntityManager em, EvalActividades evalCompetencias) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(evalCompetencias);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaEvalActividades.crear:  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   public void editar(EntityManager em, EvalActividades evalCompetencias) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(evalCompetencias);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaEvalActividades.editar:  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   public void borrar(EntityManager em, EvalActividades evalCompetencias) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.remove(em.merge(evalCompetencias));
         tx.commit();

      } catch (Exception e) {
         try {
            if (tx.isActive()) {
               tx.rollback();
            }
         } catch (Exception ex) {
            log.error("Error PersistenciaEvalActividades.borrar:  ", e);
         }
      }
   }

   public List<EvalActividades> consultarEvalActividades(EntityManager em) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT t FROM EvalActividades t ORDER BY t.codigo ASC");
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         List<EvalActividades> evalActividades = query.getResultList();
         return evalActividades;
      } catch (Exception e) {
         log.error("Error buscarEvalActividades ERROR:  ", e);
         return null;
      }
   }

   public EvalActividades consultarEvalActividad(EntityManager em, BigInteger secuencia) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT t FROM EvalActividades t WHERE t.secuencia =:secuencia");
         query.setParameter("secuencia", secuencia);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         EvalActividades evalCompetencias = (EvalActividades) query.getSingleResult();
         return evalCompetencias;
      } catch (Exception e) {
         log.error("Error buscarEvalActividadSecuencia  ", e);
         EvalActividades evalCompetencias = null;
         return evalCompetencias;
      }
   }

   public BigInteger contarEvalPlanesDesarrollosEvalActividad(EntityManager em, BigInteger secuencia) {
      BigInteger retorno = new BigInteger("-1");
      try {
         em.clear();
         String sqlQuery = "SELECT COUNT(*)FROM evalplanesdesarrollos WHERE evalactividad =?";
         Query query = em.createNativeQuery(sqlQuery);
         query.setParameter(1, secuencia);
         retorno = new BigInteger(query.getSingleResult().toString());
         log.warn("Contador PersistenciaEvalActividades contarChequeosMedicosEvalActividad Retorno " + retorno);
         return retorno;
      } catch (Exception e) {
         log.error("Error PersistenciaEvalActividades contarChequeosMedicosEvalActividad ERROR :  ", e);
         return retorno;
      }
   }

   public BigInteger contarCapNecesidadesEvalActividad(EntityManager em, BigInteger secuencia) {
      BigInteger retorno = new BigInteger("-1");
      try {
         em.clear();
         String sqlQuery = "SELECT COUNT(*)FROM CAPNECESIDADES WHERE tipoeducacion =?";
         Query query = em.createNativeQuery(sqlQuery);
         query.setParameter(1, secuencia);
         retorno = new BigInteger(query.getSingleResult().toString());
         log.warn("Contador PersistenciaEvalActividades contarCapNecesidadesEvalActividad Retorno " + retorno);
         return retorno;
      } catch (Exception e) {
         log.error("Error PersistenciaEvalActividades contarCapNecesidadesEvalActividad ERROR :  ", e);
         return retorno;
      }
   }

   public BigInteger contarCapBuzonesEvalActividad(EntityManager em, BigInteger secuencia) {
      BigInteger retorno = new BigInteger("-1");
      try {
         em.clear();
         String sqlQuery = "SELECT COUNT(*)FROM CAPBUZONES WHERE tipoeducacion =?";
         Query query = em.createNativeQuery(sqlQuery);
         query.setParameter(1, secuencia);
         retorno = new BigInteger(query.getSingleResult().toString());
         log.warn("Contador PersistenciaEvalActividades contarCapBuzonesEvalActividad Retorno " + retorno);
         return retorno;
      } catch (Exception e) {
         log.error("Error PersistenciaEvalActividades contarCapBuzonesEvalActividad ERROR :  ", e);
         return retorno;
      }
   }
}
