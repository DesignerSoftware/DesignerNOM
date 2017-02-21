/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.HVHojasDeVida;
import Entidades.HvReferencias;
import InterfacePersistencia.PersistenciaHvReferencias1Interface;
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
public class PersistenciaHvReferencias1 implements PersistenciaHvReferencias1Interface {

   @Override
   public void crear(EntityManager em, HvReferencias hvReferencias) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(hvReferencias);
         tx.commit();
      } catch (Exception e) {
         System.out.println("Error PersistenciaHvReferencias.crear: " + e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void editar(EntityManager em, HvReferencias hvReferencias) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(hvReferencias);
         tx.commit();
      } catch (Exception e) {
         System.out.println("Error PersistenciaHvReferencias.editar: " + e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void borrar(EntityManager em, HvReferencias hvReferencias) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.remove(em.merge(hvReferencias));
         tx.commit();

      } catch (Exception e) {
         if (tx.isActive()) {
            tx.rollback();
         }
         System.out.println("Error PersistenciaHvReferencias.borrar: " + e);
      }
   }

   @Override
   public List<HvReferencias> buscarHvReferencias(EntityManager em) {
      em.clear();
      Query query = em.createQuery("SELECT te FROM HvReferencias te  ");
      query.setHint("javax.persistence.cache.storeMode", "REFRESH");
      List<HvReferencias> listHvReferencias = query.getResultList();
      return listHvReferencias;

   }

   @Override
   public List<HvReferencias> referenciasPersonalesPersona(EntityManager em, BigInteger secuenciaHV) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT COUNT(hvr) FROM HvReferencias hvr WHERE hvr.hojadevida.secuencia = :secuenciaHV");
         query.setParameter("secuenciaHV", secuenciaHV);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         Long resultado = (Long) query.getSingleResult();
         if (resultado > 0) {
            Query queryFinal = em.createQuery("SELECT hvr FROM HvReferencias hvr WHERE hvr.hojadevida.secuencia = :secuenciaHV and hvr.tipo = 'PERSONALES'");
            queryFinal.setParameter("secuenciaHV", secuenciaHV);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<HvReferencias> listaReferenciasPersonales = queryFinal.getResultList();
            return listaReferenciasPersonales;
         }
         return null;
      } catch (Exception e) {
         System.out.println("Error PersistenciasHvReferencias.referenciasPersonalesPersona" + e);
         return null;
      }
   }

   @Override
   public List<HVHojasDeVida> consultarHvHojaDeVidaPorPersona(EntityManager em, BigInteger secPersona) {
      try {
         em.clear();
         String sql = "SELECT * FROM HVHOJASDEVIDA hv , PERSONAS p WHERE p.secuencia= hv.persona AND p.secuencia = ?";
         System.out.println("PersistenciaHvReferencias secuencia empleado hoja de vida " + secPersona);
         Query query = em.createNativeQuery(sql, HVHojasDeVida.class);
         query.setParameter(1, secPersona);
         List<HVHojasDeVida> hvHojasDeVIda = query.getResultList();
         return hvHojasDeVIda;
      } catch (Exception e) {
         System.err.println("Error en Persistencia HVREFERENCIAS buscarHvHojaDeVidaPorPersona " + e);
         return null;
      }
   }

   @Override
   public List<HvReferencias> consultarHvReferenciasFamiliarPorPersona(EntityManager em, BigInteger secEmpleado) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT hr FROM  HvReferencias hr , Personas e WHERE e.secuencia  = hr.hojadevida.persona.secuencia AND e.secuencia = :secuenciaEmpl AND hr.tipo='FAMILIARES' ORDER BY hr.nombrepersona ");
         query.setParameter("secuenciaEmpl", secEmpleado);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         List<HvReferencias> listHvReferencias = query.getResultList();
         return listHvReferencias;
      } catch (Exception e) {
         System.out.println("Error en Persistencia HvRefencias 1  " + e);
         return null;
      }
   }

   @Override
   public HvReferencias buscarHvReferencia(EntityManager em, BigInteger secHvReferencias) {
      try {
         em.clear();
         return em.find(HvReferencias.class, secHvReferencias);
      } catch (Exception e) {
         return null;
      }
   }

   @Override
   public List<HvReferencias> contarReferenciasFamiliaresPersona(EntityManager em, BigInteger secuenciaHV) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT COUNT(hvr) FROM HvReferencias hvr WHERE hvr.hojadevida.secuencia = :secuenciaHV");
         query.setParameter("secuenciaHV", secuenciaHV);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         Long resultado = (Long) query.getSingleResult();
         if (resultado > 0) {
            Query queryFinal = em.createQuery("SELECT hvr FROM HvReferencias hvr WHERE hvr.hojadevida.secuencia = :secuenciaHV and hvr.tipo = 'FAMILIARES'");
            queryFinal.setParameter("secuenciaHV", secuenciaHV);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<HvReferencias> listaReferenciasPersonales = queryFinal.getResultList();
            return listaReferenciasPersonales;
         }
         return null;
      } catch (Exception e) {
         System.out.println("Error PersistenciasHvReferencias.referenciasPersonalesPersona" + e);
         return null;
      }
   }
}
