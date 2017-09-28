/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.Procesos;
import InterfacePersistencia.PersistenciaProcesosInterface;
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
 * Clase Stateless. <br>
 * Clase encargada de realizar operaciones sobre la tabla 'Procesos' de la base
 * de datos.
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaProcesos implements PersistenciaProcesosInterface {

   private static Logger log = Logger.getLogger(PersistenciaProcesos.class);

   /**
    * Atributo EntityManager. Representa la comunicación con la base de datos.
    */
//    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
//    private EntityManager em;
   @Override
   public void crear(EntityManager em, Procesos procesos) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(procesos);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaProcesos.crear:  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void editar(EntityManager em, Procesos procesos) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(procesos);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaProcesos.editar:  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void borrar(EntityManager em, Procesos procesos) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.remove(em.merge(procesos));
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaProcesos.borrar:  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public List<Procesos> buscarProcesos(EntityManager em) {
      try {
         em.clear();
         Query query = em.createNativeQuery("SELECT * FROM PROCESOS ORDER BY codigo ASC", Procesos.class);
         log.warn("buscarProcesos() va a consultar");
//            Query query = em.createQuery("SELECT t FROM Procesos t ORDER BY t.codigo ASC", Procesos.class);
//            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         List<Procesos> procesos = query.getResultList();
//            log.warn("procesos : " + procesos);
         return procesos;
      } catch (Exception e) {
         log.error("Error buscarProcesos :  ", e);
         return null;
      }
   }

   @Override
   public Procesos buscarProcesosSecuencia(EntityManager em, BigInteger secuencia) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT t FROM Procesos t WHERE t.secuencia = :secuencia");
         query.setParameter("secuencia", secuencia);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         Procesos procesos = (Procesos) query.getSingleResult();
         return procesos;
      } catch (Exception e) {
         log.error("Error buscarProcesosSecuencia :  ", e);
         Procesos procesos = null;
         return procesos;
      }
   }

   @Override
   public Procesos buscarProcesosPorCodigo(EntityManager em, short codigo) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT t FROM Procesos t WHERE t.codigo = :codigo");
         query.setParameter("codigo", codigo);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         Procesos procesos = (Procesos) query.getSingleResult();
         return procesos;
      } catch (Exception e) {
         log.error("Error buscarProcesosSecuencia :  ", e);
         Procesos procesos = null;
         return procesos;
      }
   }

   @Override
   public List<Procesos> lovProcesos(EntityManager em) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT p FROM Procesos p ORDER BY p.descripcion");
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         List<Procesos> listaProcesos = query.getResultList();
         return listaProcesos;
      } catch (Exception e) {
         log.error("Error PersistenciaProcesos.lovProcesos:  ", e);
         return null;
      }
   }

   @Override
   public List<Procesos> procesosParametros(EntityManager em, String aut) {
      if (!aut.equals("N") && !aut.equals("S")) {
         aut = "S";
      }
      try {
         em.clear();
         String sqlQuery = "SELECT P.* FROM PROCESOS P\n"
                 + " WHERE EXISTS (SELECT 'x' FROM usuariosprocesos up, usuarios u \n"
                 + " WHERE up.proceso=p.secuencia \n"
                 + " AND u.secuencia = up.usuario \n"
                 + " AND u.alias = user)\n"
                 + " and nvl(p.automatico,'S') = '" + aut + "'";
         Query query = em.createNativeQuery(sqlQuery, Procesos.class);
         List<Procesos> listaProcesosParametros = query.getResultList();
         return listaProcesosParametros;
      } catch (Exception e) {
         log.error("Error en PersistenciaProcesos.procesosParametros:  ", e);
         return null;
      }
   }

   @Override
   public String obtenerDescripcionProcesoPorSecuencia(EntityManager em, BigInteger proceso) {
      try {
         em.clear();
         String desripcion = "";
         if (proceso != null) {
            String sql = "SELECT nvl(descripcion,'TODOS') FROM  procesos  WHERE secuencia =?";
            Query query = em.createNativeQuery(sql);
            query.setParameter(1, proceso);
            desripcion = (String) query.getSingleResult();
         } else {
            desripcion = "TODOS";
         }
         return desripcion;
      } catch (Exception e) {
         log.error("Error  PersistenciaProcesos  obtenerDescripcionProcesoPorSecuencia :  ", e);
         return null;
      }
   }

   public String clonarProceso(EntityManager em, String descripcionNu, short codigoNu, short codigoOri) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         StoredProcedureQuery query = em.createStoredProcedureQuery("PROCESOS_PKG.CLONARPROCESO");
         query.registerStoredProcedureParameter(1, String.class, ParameterMode.INOUT);
         query.registerStoredProcedureParameter(2, short.class, ParameterMode.IN);
         query.registerStoredProcedureParameter(3, short.class, ParameterMode.IN);

         query.setParameter(1, descripcionNu);
         query.setParameter(2, codigoNu);
         query.setParameter(3, codigoOri);

         query.execute();
         query.hasMoreResults();
         String strRetorno = (String) query.getOutputParameterValue(1);
         log.warn("PersistenciaProcesos.clonarProceso() Ya clono strRetorno:_" + strRetorno + "_");
         return strRetorno;
      } catch (Exception e) {
         log.error("ERROR: " + this.getClass().getName() + ".clonarProceso()");
         e.printStackTrace();
         if (tx.isActive()) {
            tx.rollback();
         }
         return "ERROR EJECUTANDO LA TRANSACCION DESDE EL SISTEMA";
      } finally {
         tx.commit();
      }
   }
}
