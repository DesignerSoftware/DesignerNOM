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
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 *
 * @author user
 */
@Stateless
public class PersistenciaFestivos implements PersistenciaFestivosInterface {

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
         System.out.println("Error PersistenciaFestivos.crear: " + e);
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
         System.out.println("Error PersistenciaFestivos.editar: " + e);
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
         System.out.println("Error PersistenciaFestivos.borrar: " + e);
         System.out.println("Persistencia.PersistenciaFestivos.borrar() e: " + e);
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
         System.err.println("Error en PERSISTENCIAFESTIVOS CONSULTARFESTIVOSPAIS : " + e);
         return null;
      }
   }

   public Festivos consultarPais(EntityManager em, BigInteger secPais) {
      try {
         em.clear();
         return em.find(Festivos.class, secPais);
      } catch (Exception e) {
         System.out.println("Persistencia.PersistenciaFestivos.consultarPais() e: " + e);
         return null;
      }
   }
}
