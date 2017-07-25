/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.InterconInfor;
import InterfacePersistencia.PersistenciaInterconInforInterface;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import org.apache.log4j.Logger;

/**
 *
 * @author user
 */
@Stateless
@LocalBean
public class PersistenciaInterconInfor implements PersistenciaInterconInforInterface {

   private static Logger log = Logger.getLogger(PersistenciaInterconInfor.class);

   @Override
   public void crear(EntityManager em, InterconInfor interconSapBO) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(interconSapBO);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaInterconInfor.crear: " + e.toString());
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void editar(EntityManager em, InterconInfor interconSapBO) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(interconSapBO);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaInterconInfor.editar: " + e.toString());
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void borrar(EntityManager em, InterconInfor interconSapBO) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.remove(em.merge(interconSapBO));
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaInterconInfor.borrar: " + e.toString());
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public InterconInfor buscarInterconInforSecuencia(EntityManager em, BigInteger secuencia) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT i FROM InterconSapBO i WHERE i.secuencia =:secuencia");
         query.setParameter("secuencia", secuencia);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         InterconInfor intercon = (InterconInfor) query.getSingleResult();
         return intercon;
      } catch (Exception e) {
         log.error("Error PersistenciaInterconInfor.buscarInterconInforSecuencia: " + e.toString());
         return null;
      }
   }

   @Override
   public List<InterconInfor> buscarInterconInforParametroContable(EntityManager em, Date fechaInicial, Date fechaFinal) {
      try {
         log.error("Entre al metodo intercon");
         em.clear();
         String sql = "select * from INTERCON_Infor i where fechacontabilizacion between \n"
                 + " ? and ? and FLAG = 'CONTABILIZADO' AND SALIDA <> 'NETO'\n"
                 + " and exists (select 'x' from empleados e where e.secuencia=i.empleado)";
         Query query = em.createNativeQuery(sql, InterconInfor.class);
         query.setParameter(1, fechaInicial);
         query.setParameter(2, fechaFinal);
         List<InterconInfor> intercon = query.getResultList();
         if (intercon != null) {
            log.error("Lista InterconSapBO intercon : " + intercon.size());
         } else {
            log.error("Lista Nula InterconSapBO");
         }
         return intercon;
      } catch (Exception e) {
         log.error("Error PersistenciaInterconInfor.buscarInterconInforParametroContable: " + e.toString());
         return null;
      }
   }

   @Override
   public Date obtenerFechaMaxInterconInfor(EntityManager em) {
      try {
         em.clear();
         String sql = "select max(i.fechacontabilizacion) from intercon_Infor i where flag = 'ENVIADO'";
         Query query = em.createNativeQuery(sql);
         Date fecha = (Date) (query.getSingleResult());
         return fecha;
      } catch (Exception e) {
         log.error("Error PersistenciaInterconInfor.obtenerFechaMaxInterconInfor: " + e.toString());
         return null;
      }
   }

   @Override
   public void actualizarFlagProcesoAnularInterfaseContableInfor(EntityManager em, Date fechaIni, Date fechaFin) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         String sql = "UPDATE intercon_Infor SET FLAG = 'CONTABILIZADO'\n"
                 + "		  WHERE FECHACONTABILIZACION BETWEEN ? AND ?\n"
                 + "		  AND FLAG = 'ENVIADO'";
         Query query = em.createNativeQuery(sql);
         query.setParameter(1, fechaIni);
         query.setParameter(2, fechaFin);
         query.executeUpdate();
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaInterconInfor.actualizarFlagProcesoAnularInterfaseContableInfor : " + e.toString());
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void ejeuctarPKGUbicarnuevointercon_Infor(EntityManager em, BigInteger secuencia, Date fechaIni, Date fechaFin, BigInteger proceso) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         String sql = "call INTERFASEInfor$PKG.Ubicarnuevointercon_InforV8(?,?,?,?)";
         Query query = em.createNativeQuery(sql);
         query.setParameter(1, secuencia);
         query.setParameter(2, fechaIni);
         query.setParameter(3, fechaFin);
         query.setParameter(4, proceso);
         query.executeUpdate();
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaInterconInfor.ejeuctarPKGUbicarnuevointercon_InforV8 : " + e.toString());
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void ejecutarDeleteInterconInfor(EntityManager em, Date fechaIni, Date fechaFin, BigInteger proceso) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         String sql = "DELETE INTERCON_Infor WHERE FECHACONTABILIZACION BETWEEN ? AND ?\n"
                 + "		    AND FLAG='CONTABILIZADO'\n"
                 + "        AND nvl(INTERCON_Infor.PROCESO,0) = NVL(?,nvl(INTERCON_Infor.PROCESO,0))\n"
                 + "        and exists (select 'x' from empleados e where e.secuencia=intercon_Infor.empleado)";
         Query query = em.createNativeQuery(sql);
         query.setParameter(1, fechaIni);
         query.setParameter(2, fechaFin);
         query.setParameter(3, proceso);
         query.executeUpdate();
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaInterconInfor.ejecutarDeleteInterconInforV8 : " + e.toString());
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void cerrarProcesoLiquidacion(EntityManager em, Date fechaIni, Date fechaFin, BigInteger proceso) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         String sql = "UPDATE INTERCON_Infor I SET I.FLAG='ENVIADO'\n"
                 + "     WHERE  \n"
                 + "     I.FECHACONTABILIZACION BETWEEN ? AND ?\n"
                 + "     and nvl(i.proceso,0) = nvl(?,nvl(i.proceso,0))\n"
                 + "     and exists (select 'x' from empleados e where e.secuencia=i.empleado)";
         Query query = em.createNativeQuery(sql);
         query.setParameter(1, fechaIni);
         query.setParameter(2, fechaFin);
         query.setParameter(3, proceso);
         query.executeUpdate();
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaInterconInfor.cerrarProcesoLiquidacion : " + e.toString());
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void ejecutarPKGRecontabilizacion(EntityManager em, Date fechaIni, Date fechaFin) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         String sql = "call INTERFASEInfor$PKG.Recontabilizacion(?,?)";
         Query query = em.createNativeQuery(sql);
         query.setParameter(1, fechaIni);
         query.setParameter(2, fechaFin);
         query.executeUpdate();
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaInterconInfor.ejecutarPKGRecontabilizacion : " + e.toString());
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public int contarProcesosContabilizadosInterconInfor(EntityManager em, Date fechaInicial, Date fechaFinal) {
      try {
         em.clear();
         String sql = "select COUNT(*) INTERCON_Infor from  i where\n"
                 + " i.fechacontabilizacion between ? and ? \n"
                 + " and i.flag = 'CONTABILIZADO'";
         Query query = em.createNativeQuery(sql);
         query.setParameter(1, fechaInicial);
         query.setParameter(2, fechaFinal);
         BigDecimal contador = (BigDecimal) query.getSingleResult();
         if (contador != null) {
            return contador.intValue();
         } else {
            return 0;
         }
      } catch (Exception e) {
         log.error("Error PersistenciaInterconInfor.contarProcesosContabilizadosInterconInfor. " + e.toString());
         return -1;
      }
   }

   @Override
   public void ejecutarPKGCrearArchivoPlanoInfor(EntityManager em, Date fechaIni, Date fechaFin, BigInteger proceso, String descripcionProceso, String nombreArchivo) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         String sql = "call INTERFASEInfor$PKG.archivo_planoV8(?,?,?,?,?)";
         Query query = em.createNativeQuery(sql);
         query.setParameter(1, fechaIni);
         query.setParameter(2, fechaFin);
         query.setParameter(3, proceso);
         query.setParameter(4, descripcionProceso);
         query.setParameter(5, nombreArchivo);
         query.executeUpdate();
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaInterconInfor.ejecutarPKGCrearArchivoPlanoSAPV8 : " + e.toString());
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void actualizarFlagInterconInforProcesoDeshacer(EntityManager em, Date fechaInicial, Date fechaFinal, BigInteger proceso) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         String sqlQuery = "UPDATE CONTABILIZACIONES SET FLAG='GENERADO' WHERE FLAG='CONTABILIZADO'\n"
                 + " AND FECHAGENERACION BETWEEN ? AND ? \n"
                 + " and exists (select 'x' from vwfempleados e, solucionesnodos sn where sn.empleado=e.secuencia and sn.secuencia=contabilizaciones.solucionnodo\n"
                 + " and sn.proceso=nvl(?,sn.proceso))";
         Query query = em.createNativeQuery(sqlQuery);
         query.setParameter(1, fechaInicial);
         query.setParameter(2, fechaFinal);
         query.setParameter(3, proceso);
         query.executeUpdate();
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaInterconInfor.actualizarFlagInterconSapBoProcesoDeshacer. " + e.toString());
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void cerrarProcesoContabilizacion(EntityManager em, Date fechaInicial, Date fechaFinal, Short empresa, BigInteger proceso) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         String sql = "UPDATE INTERCON_Infor I SET I.FLAG='ENVIADO' WHERE  \n"
                 + "     I.FECHACONTABILIZACION BETWEEN ? AND ?\n"
                 + "     and nvl(i.proceso,0) = nvl(?,nvl(i.proceso,0))\n"
                 + "     and i.empresa_codigo=?"
                 + "   and exists (select 'x' from empleados e where e.secuencia=i.empleado)";
         Query query = em.createNativeQuery(sql);
         query.setParameter(1, fechaInicial);
         query.setParameter(2, fechaFinal);
         query.setParameter(3, proceso);
         query.setParameter(4, empresa);
         query.executeUpdate();
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaInterconInfor.cerrarProcesoContabilizacion. " + e.toString());
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void eliminarInterconInfor(EntityManager em, Date fechaInicial, Date fechaFinal, Short empresa, BigInteger proceso) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         String sqlQuery = "DELETE INTERCON_Infor \n"
                 + " WHERE FECHACONTABILIZACION BETWEEN ? AND ?\n"
                 + " AND FLAG='CONTABILIZADO'\n"
                 + " and INTERCON_Infor.empresa_codigo=?\n"
                 + " AND nvl(INTERCON_Infor.PROCESO,0) = NVL(?,nvl(INTERCON_Infor.PROCESO,0))\n"
                 + " and exists (select 'x' from empleados e where e.secuencia=INTERCON_Infor.empleado)";
         Query query = em.createNativeQuery(sqlQuery);
         query.setParameter(1, fechaInicial);
         query.setParameter(2, fechaFinal);
         query.setParameter(3, empresa);
         query.setParameter(4, proceso);
         query.executeUpdate();
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaInterconInfor.eliminarInterconSapBO. " + e.toString());
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void actualizarFlagInterconInfor(EntityManager em, Date fechaInicial, Date fechaFinal, Short empresa) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         String sqlQuery = "UPDATE INTERCON_Infor SET FLAG = 'CONTABILIZADO' \n"
                 + " WHERE FECHACONTABILIZACION BETWEEN ? AND ? \n"
                 + " AND FLAG = 'ENVIADO' \n"
                 + " AND INTERCON_Infor.empresa_codigo=? \n"
                 + " AND EXISTS (SELECT 'X' FROM EMPLEADOS E WHERE E.SECUENCIA=INTERCON_Infor.EMPLEADO)";
         Query query = em.createNativeQuery(sqlQuery);
         query.setParameter(1, fechaInicial);
         query.setParameter(2, fechaFinal);
         query.setParameter(3, empresa);
         query.executeUpdate();
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaInterconInfor.actualizarFlagInterconSapBO. " + e.toString());
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

}
