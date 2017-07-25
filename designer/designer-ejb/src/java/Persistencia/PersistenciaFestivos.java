/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.Festivos;
import InterfacePersistencia.PersistenciaFestivosInterface;
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
public class PersistenciaFestivos implements PersistenciaFestivosInterface {

   private static Logger log = Logger.getLogger(PersistenciaFestivos.class);

   /**
    * Atributo EntityManager. Representa la comunicación con la base de datos.
    */
   /*@PersistenceContext(unitName = "DesignerRHN-ejbPU")
     private EntityManager em;*/
   public void crear(EntityManager em, Festivos festivos) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(festivos);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaFestivos.crear: " + e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   public void editar(EntityManager em, Festivos festivos) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(festivos);
         tx.commit();
      } catch (Exception e) {
         if (tx.isActive()) {
            tx.rollback();
         }
         log.error("Error PersistenciaFestivos.editar: " + e);
      }
   }

   public void borrar(EntityManager em, Festivos festivos) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.remove(em.merge(festivos));
         tx.commit();

      } catch (Exception e) {
         log.error("Error PersistenciaFestivos.borrar: " + e);
         log.error("Persistencia.PersistenciaFestivos.borrar() e: " + e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   public List<Festivos> consultarFestivosPais(EntityManager em, BigInteger secPais) {
      try {
         em.clear();
         String sql = "SELECT * FROM FESTIVOS WHERE PAIS = ?";
         Query query = em.createNativeQuery(sql, Festivos.class);
         query.setParameter(1, secPais);
         List<Festivos> listFestivos = query.getResultList();
         return listFestivos;
      } catch (Exception e) {
         log.error("Error en PERSISTENCIAFESTIVOS CONSULTARFESTIVOSPAIS : " + e);
         return null;
      }
   }

   public Festivos consultarPais(EntityManager em, BigInteger secPais) {
      try {
         em.clear();
         return em.find(Festivos.class, secPais);
      } catch (Exception e) {
         log.error("Persistencia.PersistenciaFestivos.consultarPais() e: " + e);
         return null;
      }
   }
}
