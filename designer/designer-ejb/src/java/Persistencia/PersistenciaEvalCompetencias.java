/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import InterfacePersistencia.PersistenciaEvalCompetenciasInterface;
import Entidades.EvalCompetencias;
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
public class PersistenciaEvalCompetencias implements PersistenciaEvalCompetenciasInterface {

   private static Logger log = Logger.getLogger(PersistenciaEvalCompetencias.class);

   /**
    * Atributo EntityManager. Representa la comunicación con la base de datos
    */
   /*@PersistenceContext(unitName = "DesignerRHN-ejbPU")
     private EntityManager em;*/
   public void crear(EntityManager em, EvalCompetencias evalCompetencias) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(evalCompetencias);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaEvalCompetencias.crear:  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   public void editar(EntityManager em, EvalCompetencias evalCompetencias) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(evalCompetencias);
         tx.commit();
      } catch (Exception e) {
         if (tx.isActive()) {
            tx.rollback();
         }
         log.error("Error PersistenciaEvalCompetencias.editar:  ", e);
      }
   }

   public void borrar(EntityManager em, EvalCompetencias evalCompetencias) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.remove(em.merge(evalCompetencias));
         tx.commit();

      } catch (Exception e) {
         if (tx.isActive()) {
            tx.rollback();
         }
         log.error("Error PersistenciaEvalCompetencias.borrar:  ", e);
      }
   }

   public EvalCompetencias buscarEvalCompetencia(EntityManager em, BigInteger secuenciaTE) {
      try {
         em.clear();
         return em.find(EvalCompetencias.class, secuenciaTE);
      } catch (Exception e) {
         log.error("PersistenciaEvalCompetencias.buscarEvalCompetencia() e:  ", e);
         return null;
      }
   }

   public List<EvalCompetencias> buscarEvalCompetencias(EntityManager em) {
      em.clear();
      Query query = em.createQuery("SELECT ec FROM EvalCompetencias ec  ORDER BY ec.codigo ASC ");
      query.setHint("javax.persistence.cache.storeMode", "REFRESH");
      List<EvalCompetencias> listMotivosDemandas = query.getResultList();
      return listMotivosDemandas;

   }

   public BigInteger contadorCompetenciasCargos(EntityManager em, BigInteger secuencia) {
      BigInteger retorno = null;
      try {
         em.clear();
         String sqlQuery = " SELECT COUNT(*)FROM competenciascargos ca, evalcompetencias ec WHERE ca.evalcompetencia= ec.secuencia AND ec.secuencia = ?";
         Query query = em.createNativeQuery(sqlQuery);
         query.setParameter(1, secuencia);
         retorno = new BigInteger(query.getSingleResult().toString());
         log.warn("Contador PERSISTENCIAEVALCOMPETENCIAS ContadorCompetenciasCargos Retorno : " + retorno);
         return retorno;
      } catch (Exception e) {
         log.error("Error PERSISTENCIAEVALCOMPETENCIAS ContadorCompetenciasCargos.  ", e);
         return retorno;
      }
   }

}
