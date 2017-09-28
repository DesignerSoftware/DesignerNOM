/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.GruposViaticos;
import InterfacePersistencia.PersistenciaGruposViaticosInterface;
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
public class PersistenciaGruposViaticos implements PersistenciaGruposViaticosInterface {

   private static Logger log = Logger.getLogger(PersistenciaGruposViaticos.class);

   @Override
   public void crear(EntityManager em, GruposViaticos gruposViaticos) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(gruposViaticos);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaGruposViaticos.crear:  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void editar(EntityManager em, GruposViaticos gruposViaticos) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(gruposViaticos);
         tx.commit();
      } catch (Exception e) {
         if (tx.isActive()) {
            tx.rollback();
         }
         log.error("Error PersistenciaGruposViaticos.editar:  ", e);
      }

   }

   @Override
   public void borrar(EntityManager em, GruposViaticos gruposViaticos) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.remove(em.merge(gruposViaticos));
         tx.commit();

      } catch (Exception e) {
         if (tx.isActive()) {
            tx.rollback();
         }
         log.error("Error PersistenciaGruposViaticos.borrar:  ", e);
      }

   }

   @Override
   public GruposViaticos buscarGrupoViatico(EntityManager em, BigInteger secuenciaGV) {
      try {
         em.clear();
         return em.find(GruposViaticos.class, secuenciaGV);
      } catch (Exception e) {
         log.error("PersistenciaGruposViaticos.buscarGrupoViatico() e:  ", e);
         return null;
      }
   }

   @Override
   public List<GruposViaticos> buscarGruposViaticos(EntityManager em) {
      em.clear();
      Query query = em.createQuery("SELECT ec FROM GruposViaticos ec  ORDER BY ec.codigo ASC");
      query.setHint("javax.persistence.cache.storeMode", "REFRESH");
      List<GruposViaticos> listGruposViaticos = query.getResultList();
      return listGruposViaticos;
   }

   @Override
   public BigInteger contadorCargos(EntityManager em, BigInteger secuencia) {
      BigInteger retorno = new BigInteger("-1");
      try {
         em.clear();
         String sqlQuery = " SELECT COUNT(*) FROM cargos WHERE grupoviatico = ?";
         Query query = em.createNativeQuery(sqlQuery);
         query.setParameter(1, secuencia);
         retorno = new BigInteger(query.getSingleResult().toString());
         log.error("Contador contadorCargos persistencia " + retorno);
         return retorno;
      } catch (Exception e) {
         log.error("persistenciaGruposViativos Error contadorCargos.  ", e);
         return retorno;
      }
   }

   @Override
   public BigInteger contadorPlantas(EntityManager em, BigInteger secuencia) {
      BigInteger retorno = new BigInteger("-1");
      try {
         em.clear();
         String sqlQuery = " SELECT COUNT(*) FROM plantas WHERE grupoviatico = ?";
         Query query = em.createNativeQuery(sqlQuery);
         query.setParameter(1, secuencia);
         retorno = new BigInteger(query.getSingleResult().toString());
         log.error("persistenciaGruposViativos contardorPlantas Contador " + retorno);
         return retorno;
      } catch (Exception e) {
         log.error("persistenciaGruposViativos contardorPlantas Error  ", e);
         return retorno;
      }
   }

   @Override
   public BigInteger contadorTablasViaticos(EntityManager em, BigInteger secuencia) {
      BigInteger retorno = new BigInteger("-1");
      try {
         em.clear();
         String sqlQuery = " SELECT COUNT(*) FROM tablasviaticos  WHERE grupoviatico = ?";
         Query query = em.createNativeQuery(sqlQuery);
         query.setParameter(1, secuencia);
         retorno = new BigInteger(query.getSingleResult().toString());
         log.error("persistenciaGruposViativos  contadorTablasViaticos Contador " + retorno);
         return retorno;
      } catch (Exception e) {
         log.error("persistenciaGruposViativos  contadorTablasViaticos Error :  ", e);
         return retorno;
      }
   }

   @Override
   public BigInteger contadorEersViaticos(EntityManager em, BigInteger secuencia) {
      BigInteger retorno = new BigInteger("-1");
      try {
         em.clear();
         String sqlQuery = " SELECT COUNT(*) FROM eersviaticos WHERE grupoviatico = ?";
         Query query = em.createNativeQuery(sqlQuery);
         query.setParameter(1, secuencia);
         retorno = new BigInteger(query.getSingleResult().toString());
         log.error("persistenciaGruposViativos contadorEersviaticos Contador :" + retorno);
         return retorno;
      } catch (Exception e) {
         log.error("persistenciaGruposViativos contadorEersviaticos Error :  ", e);
         return retorno;
      }
   }
}
