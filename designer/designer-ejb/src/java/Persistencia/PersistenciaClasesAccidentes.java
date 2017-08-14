/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import InterfacePersistencia.PersistenciaClasesAccidentesInterface;
import Entidades.ClasesAccidentes;
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
public class PersistenciaClasesAccidentes implements PersistenciaClasesAccidentesInterface {

   private static Logger log = Logger.getLogger(PersistenciaClasesAccidentes.class);

   /**
    * Atributo EntityManager. Representa la comunicación con la base de datos
    */
   /*@PersistenceContext(unitName = "DesignerRHN-ejbPU")
     private EntityManager em;*/
   public void crear(EntityManager em, ClasesAccidentes clasesAccidentes) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(clasesAccidentes);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaClasesAccidentes.crear: " + e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   public void editar(EntityManager em, ClasesAccidentes clasesAccidentes) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(clasesAccidentes);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaClasesAccidentes.crear: " + e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   public void borrar(EntityManager em, ClasesAccidentes clasesAccidentes) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.remove(em.merge(clasesAccidentes));
         tx.commit();

      } catch (Exception e) {
         if (tx.isActive()) {
            tx.rollback();
         }
         log.error("Error PersistenciaClasesAccidentes.borrar: " + e);
      }
   }

   public ClasesAccidentes buscarClaseAccidente(EntityManager em, BigInteger secuenciaCA) {
      try {
         em.clear();
         return em.find(ClasesAccidentes.class, secuenciaCA);
      } catch (Exception e) {
         return null;
      }
   }

   public List<ClasesAccidentes> buscarClasesAccidentes(EntityManager em) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT l FROM ClasesAccidentes  l ORDER BY l.codigo ASC ");
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         List<ClasesAccidentes> listClasesAccidentes = query.getResultList();
         return listClasesAccidentes;
      } catch (Exception e) {
         log.error("ERROR PERSISTENCIACLASESACCIDENTES BUSCARCLASESACCIDENTES ERROR : " + e);
         return null;
      }

   }

   public BigInteger contadorSoAccidentesMedicos(EntityManager em, BigInteger secuencia) {
      BigInteger retorno = new BigInteger("-1");
      try {
         em.clear();
         String sqlQuery = "SELECT COUNT(*)FROM soaccidentesmedicos WHERE claseaccidente =?";
         Query query = em.createNativeQuery(sqlQuery);
         query.setParameter(1, secuencia);
         retorno = new BigInteger(query.getSingleResult().toString());
         log.warn("Contador PERSISTENCIACLASESACCIDENTES  contadorSoAccidentesMedicos  " + retorno);
         return retorno;
      } catch (Exception e) {
         log.error("Error PERSISTENCIACLASESACCIDENTES   contadorSoAccidentesMedicos. " + e);
         return retorno;
      }
   }
}
