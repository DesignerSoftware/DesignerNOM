/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import InterfacePersistencia.PersistenciaErroresLiquidacionesInterface;
import Entidades.ErroresLiquidacion;
import Entidades.VigenciasLocalizaciones;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.ParameterMode;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;

/**
 *
 * @author user
 */
@Stateless
public class PersistenciaErroresLiquidaciones implements PersistenciaErroresLiquidacionesInterface {

   private static Logger log = Logger.getLogger(PersistenciaErroresLiquidaciones.class);

   public List<ErroresLiquidacion> consultarErroresLiquidacionPorEmpleado(EntityManager em, BigInteger semEmpleado) {
      try {
         em.clear();
         String SqlQuery = "SELECT * FROM erroresliquidacion e WHERE e.empleado = NVL(?,e.empleado)";
         Query query = em.createNativeQuery(SqlQuery, ErroresLiquidacion.class);
         query.setParameter(1, semEmpleado);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         List<ErroresLiquidacion> listaErroresLiquidacionEmpleado = query.getResultList();
         return listaErroresLiquidacionEmpleado;
      } catch (Exception e) {
         log.error("\n ERROR EN PersistenciaErroresLiquidaciones consultarErroresLiquidacionPorEmpleado ERROR :  ", e);
         return null;
      }
   }

   public List<ErroresLiquidacion> consultarErroresLiquidacion(EntityManager em) {
      try {
         log.warn("PersistenciaErroresLiquidaciones.consultarErroresLiquidacion()");
         em.clear();
         String SqlQuery = "SELECT ER.* FROM erroresliquidacion ER where exists (select 'x' from empleados e where e.secuencia = ER.empleado)";
         Query query = em.createNativeQuery(SqlQuery, ErroresLiquidacion.class);
         List<ErroresLiquidacion> listaErroresLiquidacionEmpleado = query.getResultList();
         if (listaErroresLiquidacionEmpleado != null) {
            log.warn("listaErroresLiquidacionEmpleado.size() : " + listaErroresLiquidacionEmpleado.size());
            em.clear();
            String SqlQuery2 = "SELECT ER.EMPLEADO, VL.* FROM erroresliquidacion ER, VIGENCIASLOCALIZACIONES VL \n"
                    + "WHERE VL.EMPLEADO = ER.EMPLEADO \n"
                    + "and VL.FECHAVIGENCIA = (SELECT MAX(FECHAVIGENCIA) FROM VIGENCIASLOCALIZACIONES VLI WHERE VLI.EMPLEADO = VL.EMPLEADO)"
                    + "and exists (select 'x' from empleados e where e.secuencia=er.empleado)";
            Query query2 = em.createNativeQuery(SqlQuery2, VigenciasLocalizaciones.class);
            List<VigenciasLocalizaciones> listaVigenciasLocalizaciones = query2.getResultList();
            if (listaVigenciasLocalizaciones != null) {
               if (!listaVigenciasLocalizaciones.isEmpty()) {
                  log.warn("listaVigenciasLocalizaciones.size() : " + listaVigenciasLocalizaciones.size());
                  for (int i = 0; i < listaErroresLiquidacionEmpleado.size(); i++) {
                     try {
                        for (int j = 0; j < listaVigenciasLocalizaciones.size(); j++) {
                           if (listaErroresLiquidacionEmpleado.get(i).getEmpleado().getSecuencia().equals(listaVigenciasLocalizaciones.get(j).getEmpleado().getSecuencia())) {
                              listaErroresLiquidacionEmpleado.get(i).setVigenciaLocalizacion(listaVigenciasLocalizaciones.get(j));
                              listaVigenciasLocalizaciones.remove(listaVigenciasLocalizaciones.get(j));
                              log.warn("Ya " + i);
                              break;
                           }
                        }
                     } catch (Exception e) {
                        log.error("Error en la posicion: " + i + " , listaErroresLiquidacionEmpleado.get(i) : " + listaErroresLiquidacionEmpleado.get(i));
                     }
                  }
               }
            }
         } else {
            log.error("listaErroresLiquidacionEmpleado : " + listaErroresLiquidacionEmpleado);
         }
         return listaErroresLiquidacionEmpleado;
      } catch (Exception e) {
         log.error("\n ERROR EN PersistenciaErroresLiquidaciones consultarErroresLiquidacion ERROR :  ", e);
         e.printStackTrace();
         return null;
      }
   }

   public int BorrarTotosErroresLiquidaciones(EntityManager em) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      int n = 0;
      BigInteger n1 = null;
      try {
         tx.begin();
         StoredProcedureQuery query = em.createStoredProcedureQuery("ERRORESLIQUIDACION_PKG.BORRARERRORESLIQUIDACION");
         log.warn("BorrarTotosErroresLiquidaciones 1");
         query.registerStoredProcedureParameter(1, BigInteger.class, ParameterMode.OUT);
         log.warn("BorrarTotosErroresLiquidaciones 2");
         query.setParameter(1, n1);
         log.warn("BorrarTotosErroresLiquidaciones 3");
         query.execute();
         log.warn("BorrarTotosErroresLiquidaciones 4");
         query.hasMoreResults();
         log.warn("BorrarTotosErroresLiquidaciones 5");
         n1 = (BigInteger) query.getOutputParameterValue(1);
         log.warn("BorrarTotosErroresLiquidaciones 6");
         tx.commit();
         n = n1.intValue();
         log.warn("PersistenciaErroresLiquidaciones.BorrarTotosErroresLiquidaciones() n: _" + n + "_");
      } catch (Exception e) {
         log.error("Error PersistenciaErroresLiquidaciones.BorrarTotosErroresLiquidaciones.  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
      return n;
   }

   public void borrar(EntityManager em, ErroresLiquidacion erroresLiquidacion) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.remove(em.merge(erroresLiquidacion));
         tx.commit();
      } catch (Exception e) {
         try {
            if (tx.isActive()) {
               tx.rollback();
            }
         } catch (Exception ex) {
            log.error("Error PersistenciaErroresLiquidaciones.borrar:  ", e);
         }
      }
   }
}
