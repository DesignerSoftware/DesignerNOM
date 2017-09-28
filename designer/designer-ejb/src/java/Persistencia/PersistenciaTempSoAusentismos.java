/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.TempSoAusentismos;
import InterfacePersistencia.PersistenciaTempSoAusentismosInterface;
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
public class PersistenciaTempSoAusentismos implements PersistenciaTempSoAusentismosInterface {

   private static Logger log = Logger.getLogger(PersistenciaTempSoAusentismos.class);

   @Override
   public void crear(EntityManager em, TempSoAusentismos tempAusentismos) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(tempAusentismos);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaTempSoAusentismos.crear:  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void editar(EntityManager em, TempSoAusentismos tempAusentismos) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(tempAusentismos);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaTempSoAusentismos.editar:  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void borrar(EntityManager em, TempSoAusentismos tempAusentismos) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.remove(em.merge(tempAusentismos));
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaTempSoAusentismos.borrar:  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public List<TempSoAusentismos> obtenerTempAusentismos(EntityManager em, String usuarioBD) {
      try {
         String sql = "SELECT * FROM TEMPSOAUSENTISMOS WHERE USUARIOBD = ? AND NVL(ESTADO,'N') <> 'C'";
         em.clear();
         Query query = em.createNativeQuery(sql, TempSoAusentismos.class);
         query.setParameter(1, usuarioBD);
         List<TempSoAusentismos> listTNovedades = query.getResultList();
         return listTNovedades;
      } catch (Exception e) {
         log.error("PersistenciaTempSoAusentismos.obtenerTempAusentismos():  ", e);
         return null;
      }
   }

   @Override
   public List<String> obtenerDocumentosSoporteCargados(EntityManager em, String usuarioBD) {

      try {
         em.clear();
         String sql = "SELECT T.DOCUMENTOSOPORTE FROM TEMPSOAUSENTISMOS T WHERE T.USUARIOBD = ? AND T.ESTADO = 'C'";
         Query query = em.createNativeQuery(sql, TempSoAusentismos.class);
         query.setParameter(1, usuarioBD);
         List<String> listDocumentosSoporte = query.getResultList();
         return listDocumentosSoporte;
      } catch (Exception e) {
         log.error("PersistenciaTempSoAusentismos.obtenerDocumentosSoporteCargados():  ", e);
         return null;
      }
   }

   @Override
   public void cargarTempAusentismos(EntityManager em, String fechaInicial, BigInteger secEmpresa) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         String sqlQuery = "call TEMPSOAUSENTISMOS_PKG.INSERTARAUSENTISMO(To_date(?, 'dd/mm/yyyy'),?)";
         Query query = em.createNativeQuery(sqlQuery);
         query.setParameter(1, fechaInicial);
         query.setParameter(2, secEmpresa);
         query.executeUpdate();
         tx.commit();
      } catch (Exception e) {
         log.error("PersistenciaTempSoAusentismos.cargarTempAusentismos():  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public int reversarTempAusentismos(EntityManager em, String usuarioBD, String documentoSoporte) {

      em.clear();
      String sql = "DELETE FROM TEMPSOAUSENTISMOS WHERE USUARIOBD = ? AND ESTADO = 'C' AND DOCUMENTOSOPORTE = ?";
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         Query query = em.createNativeQuery(sql);
         query.setParameter(1, usuarioBD);
         query.setParameter(2, documentoSoporte);
         query.executeUpdate();
         tx.commit();
         return 1;
      } catch (Exception e) {
         log.error("PersistenciaTempSoAusentismos.reversarTempNovedades():  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
         return 0;
      }

   }

   @Override
   public void borrarRegistrosTempNovedades(EntityManager em, String usuarioBD) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         String sql = "DELETE FROM TEMPSOAUSENTISMOS WHERE USUARIOBD = ? AND ESTADO = 'N'";
         Query query = em.createNativeQuery(sql);
         query.setParameter(1, usuarioBD);
         query.executeUpdate();
         tx.commit();
      } catch (Exception e) {
         log.error("No se pudo borrar el registro (borrarRegistrosTempNovedades) :  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }
}
