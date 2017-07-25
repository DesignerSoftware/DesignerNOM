package Persistencia;

import Entidades.Contabilizaciones;
import InterfacePersistencia.PersistenciaContabilizacionesInterface;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 *
 * @author Administrador
 */
@Stateless
public class PersistenciaContabilizaciones implements PersistenciaContabilizacionesInterface {

   private static Logger log = Logger.getLogger(PersistenciaContabilizaciones.class);

   @Override
   public void crear(EntityManager em, Contabilizaciones contabilizaciones) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.persist(contabilizaciones);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaContabilizaciones.crear: " + e.toString());
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void editar(EntityManager em, Contabilizaciones contabilizaciones) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(contabilizaciones);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaContabilizaciones.editar: " + e.toString());
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void borrar(EntityManager em, Contabilizaciones contabilizaciones) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.remove(em.merge(contabilizaciones));
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaContabilizaciones.borrar: " + e.toString());
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public List<Contabilizaciones> buscarContabilizaciones(EntityManager em) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT c FROM Contabilizaciones c");
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         List<Contabilizaciones> lista = query.getResultList();
         return lista;
      } catch (Exception e) {
         log.error("Error buscarContabilizaciones PersistenciaContabilizaciones : " + e.toString());
         return null;
      }
   }

   @Override
   public Date obtenerFechaMaximaContabilizaciones(EntityManager em) {
      try {
         em.clear();
         String sql = "select max(fechageneracion) from contabilizaciones "
                 + " where flag = 'CONTABILIZADO' and "
                 + " exists (select 'x' from solucionesnodos sn, empleados e\n"
                 + " where e.secuencia=sn.empleado and sn.secuencia=contabilizaciones.solucionnodo)";
         Query query = em.createNativeQuery(sql);
         Date fecha = (Date) query.getSingleResult();
         return fecha;
      } catch (Exception e) {
         log.error("Error obtenerFechaMaximaContabilizaciones PersistenciaContabilizaciones : " + e.toString());
         return null;
      }
   }

   @Override
   public Date obtenerFechaMaximaContabilizacionesSAPBOV8(EntityManager em) {
      try {
         em.clear();
         String sql = "select max(fechageneracion) from contabilizaciones where flag = 'CONTABILIZADO'";
         Query query = em.createNativeQuery(sql);
         Date fecha = (Date) query.getSingleResult();
         return fecha;
      } catch (Exception e) {
         log.error("Error obtenerFechaMaximaContabilizacionesSAPBOV8 PersistenciaContabilizaciones : " + e.toString());
         return null;
      }
   }

   @Override
   public void actualizarFlahInterconContableSAPBO(EntityManager em, Date fechaIni, Date fechaFin, BigInteger proceso) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         String sql = "UPDATE CONTABILIZACIONES SET FLAG='GENERADO' WHERE FLAG='CONTABILIZADO'\n"
                 + "		 AND FECHAGENERACION BETWEEN ? AND ? \n"
                 + "		 AND  EXISTS \n"
                 + "         (SELECT 'X'  FROM INTERCON_SAPBO IT \n"
                 + "         WHERE IT.CONTABILIZACION = CONTABILIZACIONES.SECUENCIA \n"
                 + "         and FECHACONTABILIZACION BETWEEN ? AND ?\n"
                 + "         AND FLAG = 'CONTABILIZADO' AND nvl(IT.PROCESO,0) = NVL(?,nvl(IT.PROCESO,0))\n"
                 + "         and exists (select 'x' from empleados e where e.secuencia=it.empleado))";
         Query query = em.createNativeQuery(sql);
         query.setParameter(1, fechaIni);
         query.setParameter(2, fechaFin);
         query.setParameter(3, fechaIni);
         query.setParameter(4, fechaFin);
         query.setParameter(5, proceso);
         query.executeUpdate();
         tx.commit();
      } catch (Exception e) {
         log.error("Error actualizarFlahInterconContableSAPBOV8 PersistenciaContabilizaciones : " + e.toString());
         e.printStackTrace();
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void actualizarFlahInterconContableSAPBOV8(EntityManager em, Date fechaIni, Date fechaFin, BigInteger proceso) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         String sql = "UPDATE CONTABILIZACIONES SET FLAG='GENERADO' WHERE FLAG='CONTABILIZADO'\n"
                 + "		 AND FECHAGENERACION BETWEEN ? AND ? \n"
                 + "		 AND  EXISTS \n"
                 + "         (SELECT 'X'  FROM INTERCON_SAPBO IT \n"
                 + "         WHERE IT.CONTABILIZACION = CONTABILIZACIONES.SECUENCIA \n"
                 + "         and FECHACONTABILIZACION BETWEEN ? AND ?\n"
                 + "         AND FLAG = 'CONTABILIZADO' AND nvl(IT.PROCESO,0) = NVL(?,nvl(IT.PROCESO,0))\n"
                 + "         and exists (select 'x' from empleados e where e.secuencia=it.empleado))";
         Query query = em.createNativeQuery(sql);
         query.setParameter(1, fechaIni);
         query.setParameter(2, fechaFin);
         query.setParameter(3, fechaIni);
         query.setParameter(4, fechaFin);
         query.setParameter(5, proceso);
         query.executeUpdate();
         tx.commit();
      } catch (Exception e) {
         log.error("Error actualizarFlahInterconContableSAPBOV8 PersistenciaContabilizaciones : " + e.toString());
         e.printStackTrace();
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public Integer obtenerContadorFlagGeneradoFechasSAP(EntityManager em, Date fechaIni, Date fechaFin) {
      try {
         Integer conteo = null;
         em.clear();
         String sql = "select count(*) from contabilizaciones c where \n"
                 + "	c.flag = 'GENERADO' and \n"
                 + "	c.fechageneracion  between ? and ?";
         Query query = em.createNativeQuery(sql);
         query.setParameter(1, fechaIni);
         query.setParameter(2, fechaFin);
         BigDecimal valor = (BigDecimal) query.getSingleResult();
         if (valor != null) {
            conteo = valor.intValue();
            return conteo;
         } else {
            return conteo;
         }
      } catch (Exception e) {
         log.error("Error obtenerContadorFlagCerradosFechasSAP PersistenciaContabilizaciones : " + e.toString());
         e.printStackTrace();
         return null;
      }
   }

   @Override
   public Integer obtenerContadorFlagGeneradoFechasTotal(EntityManager em, Date fechaIni, Date fechaFin) {
      try {
         Integer conteo = null;
         em.clear();
         String sql = "select count(*) from contabilizaciones c\n"
                 + "	where  c.flag = 'GENERADO' and \n"
                 + "	c.fechageneracion  between ? and ? \n"
                 + "	and exists (select 'x' from solucionesnodos sn, empleados e\n"
                 + "	where e.secuencia=sn.empleado and c.solucionnodo=sn.secuencia)";
         Query query = em.createNativeQuery(sql);
         query.setParameter(1, fechaIni);
         query.setParameter(2, fechaFin);
         BigDecimal valor = (BigDecimal) query.getSingleResult();
         if (valor != null) {
            conteo = valor.intValue();
            return conteo;
         } else {
            return conteo;
         }
      } catch (Exception e) {
         log.error("Error obtenerContadorFlagGeneradoFechasTotal PersistenciaContabilizaciones : " + e.toString());
         e.printStackTrace();
         return null;
      }
   }

   @Override
   public Integer obtenerContadorFlagGeneradoFechasDynamics(EntityManager em, Date fechaIni, Date fechaFin) {
      try {
         Integer conteo = null;
         em.clear();
         String sql = "select count(*) from contabilizaciones c where \n"
                 + "	c.flag = 'GENERADO' and \n"
                 + "	c.fechageneracion  between ? and ?";
         Query query = em.createNativeQuery(sql);
         query.setParameter(1, fechaIni);
         query.setParameter(2, fechaFin);
         BigDecimal valor = (BigDecimal) query.getSingleResult();
         if (valor != null) {
            conteo = valor.intValue();
            return conteo;
         } else {
            return conteo;
         }
      } catch (Exception e) {
         log.error("Error obtenerContadorFlagCerradosFechasSAP PersistenciaContabilizaciones : " + e.toString());
         e.printStackTrace();
         return null;
      }
   }
}
