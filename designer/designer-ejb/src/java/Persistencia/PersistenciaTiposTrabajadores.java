/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.TiposTrabajadores;
import InterfacePersistencia.PersistenciaTiposTrabajadoresInterface;
import java.math.BigDecimal;
//import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.ParameterMode;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;

@Stateless
public class PersistenciaTiposTrabajadores implements PersistenciaTiposTrabajadoresInterface {

   private static Logger log = Logger.getLogger(PersistenciaTiposTrabajadores.class);

   @Override
   public void crear(EntityManager em, TiposTrabajadores tiposTrabajadores) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(tiposTrabajadores);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaTiposTrabajadores.crear: " + e.getMessage());
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void editar(EntityManager em, TiposTrabajadores tiposTrabajadores) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(tiposTrabajadores);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaTiposTrabajadores.editar: " + e.getMessage());
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void borrar(EntityManager em, TiposTrabajadores tiposTrabajadores) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.remove(em.merge(tiposTrabajadores));
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaTiposTrabajadores.borrar: " + e.getMessage());
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public List<TiposTrabajadores> buscarTiposTrabajadores(EntityManager em) {
      try {
         em.clear();
         String sql = "select * from TiposTrabajadores order by codigo";
         Query query = em.createNativeQuery(sql, TiposTrabajadores.class);
         List<TiposTrabajadores> tipoTLista = query.getResultList();
         return tipoTLista;
      } catch (Exception e) {
         log.error("Persistencia.PersistenciaTiposTrabajadores.buscarTiposTrabajadores()" + e.getMessage());
         return null;
      }
   }

   @Override
   public TiposTrabajadores buscarTipoTrabajadorSecuencia(EntityManager em, BigInteger secuencia) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT tt FROM TiposTrabajadores e WHERE tt.secuencia = :secuencia");
         query.setParameter("secuencia", secuencia);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         TiposTrabajadores tipoT = (TiposTrabajadores) query.getSingleResult();
         return tipoT;
      } catch (Exception e) {
         log.error("Persistencia.PersistenciaTiposTrabajadores.buscarTipoTrabajadorSecuencia()" + e.getMessage());
         return null;
      }
   }

   @Override
   public String plantillaValidarTipoTrabajadorReformaLaboral(EntityManager em, BigInteger tipoTrabajador, BigInteger reformaLaboral) {
      try {
         em.clear();
         String sqlQuery = "SELECT tipostrabajadores_PKG.plantillavalidarl(?,?) FROM dual";
         Query query = em.createNativeQuery(sqlQuery);
         query.setParameter(1, tipoTrabajador);
         query.setParameter(2, reformaLaboral);
         String retorno = (String) query.getSingleResult();
         return retorno;
      } catch (Exception e) {
         log.error("Error plantillaValidarTipoTrabajadorReformaLaboral PersistenciaTiposTrabajadores : " + e.getMessage());
         return null;
      }
   }

   @Override
   public String plantillaValidarTipoTrabajadorTipoSueldo(EntityManager em, BigInteger tipoTrabajador, BigInteger tipoSueldo) {
      try {
         em.clear();
         String sqlQuery = "SELECT tipostrabajadores_PKG.plantillavalidats(?,?) FROM dual";
         Query query = em.createNativeQuery(sqlQuery);
         query.setParameter(1, tipoTrabajador);
         query.setParameter(2, tipoSueldo);
         String retorno = (String) query.getSingleResult();
         return retorno;
      } catch (Exception e) {
         log.error("Error plantillaValidarTipoTrabajadorTipoSueldo PersistenciaTiposTrabajadores : " + e.getMessage());
         return null;
      }
   }

   @Override
   public String plantillaValidarTipoTrabajadorTipoContrato(EntityManager em, BigInteger tipoTrabajador, BigInteger tipoContrato) {
      try {
         em.clear();
         String sqlQuery = "SELECT tipostrabajadores_PKG.plantillavalidatc(?,?) FROM dual";
         Query query = em.createNativeQuery(sqlQuery);
         query.setParameter(1, tipoTrabajador);
         query.setParameter(2, tipoContrato);
         String retorno = (String) query.getSingleResult();
         return retorno;
      } catch (Exception e) {
         log.error("Error plantillaValidarTipoTrabajadorTipoContrato PersistenciaTiposTrabajadores : " + e.getMessage());
         return null;
      }
   }

   @Override
   public String plantillaValidarTipoTrabajadorContrato(EntityManager em, BigInteger tipoTrabajador, BigInteger contrato) {
      try {
         em.clear();
         String sqlQuery = "SELECT tipostrabajadores_PKG.plantillavalidall(?,?) FROM dual";
         Query query = em.createNativeQuery(sqlQuery);
         query.setParameter(1, tipoTrabajador);
         query.setParameter(2, contrato);
         String retorno = (String) query.getSingleResult();
         return retorno;
      } catch (Exception e) {
         log.error("Error plantillaValidarTipoTrabajadorContrato PersistenciaTiposTrabajadores : " + e.getMessage());
         return null;
      }
   }

   @Override
   public String plantillaValidarTipoTrabajadorNormaLaboral(EntityManager em, BigInteger tipoTrabajador, BigInteger normaLaboral) {
      try {
         em.clear();
         String sqlQuery = "SELECT tipostrabajadores_PKG.plantillavalidanl(?,?) FROM dual";
         Query query = em.createNativeQuery(sqlQuery);
         query.setParameter(1, tipoTrabajador);
         query.setParameter(2, normaLaboral);
         String retorno = (String) query.getSingleResult();
         return retorno;
      } catch (Exception e) {
         log.error("Error plantillaValidarTipoTrabajadorNormaLaboral PersistenciaTiposTrabajadores : " + e.getMessage());
         return null;
      }
   }

   @Override
   public TiposTrabajadores buscarTipoTrabajadorCodigoTiposhort(EntityManager em, short codigo) {
      try {
         em.clear();
         String sql = "SELECT * FROM TiposTrabajadores WHERE codigo=?";
         Query query = em.createNativeQuery(sql, TiposTrabajadores.class);
         query.setParameter(1, codigo);
         TiposTrabajadores tipoTC = (TiposTrabajadores) query.getSingleResult();
         return tipoTC;
      } catch (Exception e) {
         log.error("Persistencia.PersistenciaTiposTrabajadores.buscarTipoTrabajadorCodigoTiposhort()" + e.getMessage());
         return null;
      }
   }

   @Override
   public String clonarTipoT(EntityManager em, String nombreNuevo, Short codigoNuevo, Short codOrigen) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         StoredProcedureQuery query = em.createStoredProcedureQuery("TIPOSTRABAJADORES_PKG.CLONARTIPOTRABAJADOR");
         query.registerStoredProcedureParameter(1, String.class, ParameterMode.INOUT);
         query.registerStoredProcedureParameter(2, short.class, ParameterMode.IN);
         query.registerStoredProcedureParameter(3, short.class, ParameterMode.IN);

         query.setParameter(1, nombreNuevo);
         query.setParameter(2, codigoNuevo);
         query.setParameter(3, codOrigen);
         query.execute();
         query.hasMoreResults();
         String strRetorno = (String) query.getOutputParameterValue(1);
         log.error("PersistenciaTiposTrabajadores.clonarTipoT() strRetorno : " + strRetorno);
         tx.commit();
         return strRetorno;
      } catch (Exception e) {
         if (tx.isActive()) {
            tx.rollback();
         }
         log.error("PersistenciaTiposTrabajadores.clonarTipoT() ERROR : " + e.getMessage());
         return ("ERROR PersistenciaTiposTrabajadores.clonarTipoT()");
      }
   }

   @Override
   public boolean hayRegistrosSecundarios(EntityManager em, BigInteger secuencia) {
      try {
         em.clear();
         String sql = "SELECT count(*) FROM TIPOSTRABAJADORES TI WHERE SECUENCIA = ? AND (EXISTS\n"
                 + " (SELECT 'X' FROM VIGENCIASCONCEPTOSTT WHERE TIPOTRABAJADOR = TI.SECUENCIA)\n"
                 + " OR EXISTS\n"
                 + " (SELECT 'X' FROM VIGENCIASDIASTT WHERE TIPOTRABAJADOR = TI.SECUENCIA)\n"
                 + " OR EXISTS\n"
                 + " (SELECT 'X' FROM TIPOSTRABCOMPROBANTES WHERE TIPOTRABAJADOR = TI.SECUENCIA)\n"
                 + " OR EXISTS\n"
                 + " (SELECT 'X' FROM VIGENCIASTIPOSTRABAJADORES WHERE TIPOTRABAJADOR = TI.SECUENCIA))";
         Query query = em.createNativeQuery(sql);
         query.setParameter(1, secuencia);
         BigDecimal r = (BigDecimal) query.getSingleResult();
         log.error("Resultado : " + r);
         if (r.intValue() > 0) {
            return true;
         } else {
            return false;
         }
      } catch (Exception e) {
         log.error("PersistenciaTiposTrabajadores.hayRegistrosSecundarios()" + e.getMessage());
         return false;
      }
   }
}
