/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import InterfacePersistencia.PersistenciaErroresLiquidacionesInterface;
import Entidades.ErroresLiquidacion;
import Entidades.VigenciasLocalizaciones;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
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
         System.out.println("\n ERROR EN PersistenciaErroresLiquidaciones consultarErroresLiquidacionPorEmpleado ERROR : " + e);
         return null;
      }
   }

   public List<ErroresLiquidacion> consultarErroresLiquidacion(EntityManager em) {
      try {
         System.out.println("PersistenciaErroresLiquidaciones.consultarErroresLiquidacion()");
         em.clear();
         String SqlQuery = "SELECT ER.* FROM erroresliquidacion ER where exists (select 'x' from empleados e where e.secuencia = ER.empleado)";
         Query query = em.createNativeQuery(SqlQuery, ErroresLiquidacion.class);
         List<ErroresLiquidacion> listaErroresLiquidacionEmpleado = query.getResultList();
         if (listaErroresLiquidacionEmpleado != null) {
            System.out.println("listaErroresLiquidacionEmpleado.size() : " + listaErroresLiquidacionEmpleado.size());
            em.clear();
            String SqlQuery2 = "SELECT ER.EMPLEADO, VL.* FROM erroresliquidacion ER, VIGENCIASLOCALIZACIONES VL \n"
                    + "WHERE VL.EMPLEADO = ER.EMPLEADO \n"
                    + "and VL.FECHAVIGENCIA = (SELECT MAX(FECHAVIGENCIA) FROM VIGENCIASLOCALIZACIONES VLI WHERE VLI.EMPLEADO = VL.EMPLEADO)"
                    + "and exists (select 'x' from empleados e where e.secuencia=er.empleado)";
            Query query2 = em.createNativeQuery(SqlQuery2, VigenciasLocalizaciones.class);
            List<VigenciasLocalizaciones> listaVigenciasLocalizaciones = query2.getResultList();
            if (listaVigenciasLocalizaciones != null) {
               if (!listaVigenciasLocalizaciones.isEmpty()) {
                  System.out.println("listaVigenciasLocalizaciones.size() : " + listaVigenciasLocalizaciones.size());
                  for (int i = 0; i < listaErroresLiquidacionEmpleado.size(); i++) {
                     try {
                        for (int j = 0; j < listaVigenciasLocalizaciones.size(); j++) {
                           if (listaErroresLiquidacionEmpleado.get(i).getEmpleado().getSecuencia().equals(listaVigenciasLocalizaciones.get(j).getEmpleado().getSecuencia())) {
                              listaErroresLiquidacionEmpleado.get(i).setVigenciaLocalizacion(listaVigenciasLocalizaciones.get(j));
                              listaVigenciasLocalizaciones.remove(listaVigenciasLocalizaciones.get(j));
                              System.out.println("Ya " + i);
                              break;
                           }
                        }
                     } catch (Exception e) {
                        System.out.println("Error en la posicion: " + i + " , listaErroresLiquidacionEmpleado.get(i) : " + listaErroresLiquidacionEmpleado.get(i));
                     }
                  }
               }
            }
         } else {
            System.out.println("listaErroresLiquidacionEmpleado : " + listaErroresLiquidacionEmpleado);
         }
         return listaErroresLiquidacionEmpleado;
      } catch (Exception e) {
         System.out.println("\n ERROR EN PersistenciaErroresLiquidaciones consultarErroresLiquidacion ERROR : " + e);
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
         System.out.println("BorrarTotosErroresLiquidaciones 1");
         query.registerStoredProcedureParameter(1, BigInteger.class, ParameterMode.OUT);
         System.out.println("BorrarTotosErroresLiquidaciones 2");
         query.setParameter(1, n1);
         System.out.println("BorrarTotosErroresLiquidaciones 3");
         query.execute();
         System.out.println("BorrarTotosErroresLiquidaciones 4");
         query.hasMoreResults();
         System.out.println("BorrarTotosErroresLiquidaciones 5");
         n1 = (BigInteger) query.getOutputParameterValue(1);
         System.out.println("BorrarTotosErroresLiquidaciones 6");
         tx.commit();
         n = n1.intValue();
         System.out.println("PersistenciaErroresLiquidaciones.BorrarTotosErroresLiquidaciones() n: _" + n + "_");
      } catch (Exception e) {
         System.out.println("Error PersistenciaErroresLiquidaciones.BorrarTotosErroresLiquidaciones. " + e);
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
            System.err.println("Error PersistenciaErroresLiquidaciones.borrar: " + e);
         }
      }
   }
}
