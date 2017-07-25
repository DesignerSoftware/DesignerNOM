/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.TercerosSucursales;
import InterfacePersistencia.PersistenciaTercerosSucursalesInterface;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.ParameterMode;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;

@Stateless
public class PersistenciaTercerosSucursales implements PersistenciaTercerosSucursalesInterface {

   private static Logger log = Logger.getLogger(PersistenciaTercerosSucursales.class);

   @Override
   public void crear(EntityManager em, TercerosSucursales tercerosSucursales) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.persist(tercerosSucursales);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaTercerosSucursales.crear: " + e.getMessage());
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void editar(EntityManager em, TercerosSucursales tercerosSucursales) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(tercerosSucursales);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaTercerosSucursales.editar: " + e.getMessage());
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void borrar(EntityManager em, TercerosSucursales tercerosSucursales) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.remove(em.merge(tercerosSucursales));
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaTercerosSucursales.borrar: " + e.getMessage());
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public List<TercerosSucursales> buscarTercerosSucursales(EntityManager em) {
      try {
         em.clear();
         Query query = em.createNativeQuery("SELECT TS.* FROM TERCEROSSUCURSALES TS, TERCEROS T, EMPRESAS E"
                 + " WHERE TS.TERCERO = T.SECUENCIA AND T.EMPRESA = E.SECUENCIA", TercerosSucursales.class);
         List<TercerosSucursales> tercerosSucursales = query.getResultList();
         return tercerosSucursales;
      } catch (Exception e) {
          log.error("Persistencia.PersistenciaTercerosSucursales.buscarTercerosSucursales()" + e.getMessage());
         return null;
      }
   }

   @Override
   public TercerosSucursales buscarTercerosSucursalesSecuencia(EntityManager em, BigInteger secuencia) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT ts FROM TercerosSucursales ts WHERE ts.secuencia = :secuencia");
         query.setParameter("secuencia", secuencia);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         TercerosSucursales tercerosSucursales = (TercerosSucursales) query.getSingleResult();
         return tercerosSucursales;
      } catch (Exception e) {
          log.error("Persistencia.PersistenciaTercerosSucursales.buscarTercerosSucursalesSecuencia()" + e.getMessage());
         return null;
      }
   }

   @Override
   public List<TercerosSucursales> buscarTercerosSucursalesPorTerceroSecuencia(EntityManager em, BigInteger secuencia) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT ts FROM TercerosSucursales ts WHERE ts.tercero.secuencia = :secuenciaT");
         query.setParameter("secuenciaT", secuencia);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         List<TercerosSucursales> listTercerosS = query.getResultList();
         return listTercerosS;
      } catch (Exception e) {
         log.error("Error buscarTercerosSucursalesPorTerceroSecuencia PersistenciaTerceroSurcusal : " + e.getMessage());
         return null;
      }
   }

   @Override
   public List<TercerosSucursales> buscarTercerosSucursalesPorEmpresa(EntityManager em, BigInteger secuencia) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT ts FROM TercerosSucursales ts WHERE ts.tercero.empresa.secuencia = :secuencia");
         query.setParameter("secuencia", secuencia);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         List<TercerosSucursales> listTercerosS = query.getResultList();
         return listTercerosS;
      } catch (Exception e) {
         log.error("Error buscarTercerosSucursalesPorEmpresa PersistenciaTerceroSurcusal : " + e.getMessage());
         return null;
      }
   }
 
   @Override
   public void adicionaAfiliacionCambiosMasivos(EntityManager em, BigInteger secTipoEntidad, BigInteger secTerceroSuc, Date fechaCambio) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         StoredProcedureQuery query = em.createStoredProcedureQuery("CAMBIOSMASIVOS_PKG.AdicionaAfiliacion");
         query.registerStoredProcedureParameter(1, BigInteger.class, ParameterMode.IN);
         query.registerStoredProcedureParameter(2, BigInteger.class, ParameterMode.IN);
         query.registerStoredProcedureParameter(3, Date.class, ParameterMode.IN);

         query.setParameter(1, secTipoEntidad);
         query.setParameter(2, secTerceroSuc);
         query.setParameter(3, fechaCambio);
         query.execute();
      } catch (Exception e) {
          log.error("Persistencia.PersistenciaTercerosSucursales.adicionaAfiliacionCambiosMasivos()" + e.getMessage());
         e.printStackTrace();
         if (tx.isActive()) {
            tx.rollback();
         }
      } finally {
         tx.commit();
      }
   }

   @Override
   public void undoAdicionaAfiliacionCambiosMasivos(EntityManager em, BigInteger secTipoEntidad, BigInteger secTerceroSuc, Date fechaCambio) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         StoredProcedureQuery query = em.createStoredProcedureQuery("CAMBIOSMASIVOS_PKG.UndoAdicionaAfiliacion");
         query.registerStoredProcedureParameter(1, BigInteger.class, ParameterMode.IN);
         query.registerStoredProcedureParameter(2, BigInteger.class, ParameterMode.IN);
         query.registerStoredProcedureParameter(3, Date.class, ParameterMode.IN);

         query.setParameter(1, secTipoEntidad);
         query.setParameter(2, secTerceroSuc);
         query.setParameter(3, fechaCambio);
         query.execute();
      } catch (Exception e) {
          log.error("Persistencia.PersistenciaTercerosSucursales.undoAdicionaAfiliacionCambiosMasivos()" + e.getMessage());
         e.printStackTrace();
         if (tx.isActive()) {
            tx.rollback();
         }
      } finally {
         tx.commit();
      }
   }
}
