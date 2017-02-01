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
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

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

   public void BorrarTotosErroresLiquidaciones(EntityManager em) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      int i = -100;
      try {
         tx.begin();
         String sqlQuery = "call ERRORESLIQUIDACION_pkg.BorrarErroresLiquidacion()";
         Query query = em.createNativeQuery(sqlQuery);
         i = query.executeUpdate();
         tx.commit();
      } catch (Exception e) {
         System.out.println("Error PersistenciaCandados.liquidar. " + e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
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
