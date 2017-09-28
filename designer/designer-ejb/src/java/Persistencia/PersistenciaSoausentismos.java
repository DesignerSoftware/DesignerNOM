/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.Soausentismos;
import InterfacePersistencia.PersistenciaSoausentismosInterface;
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

/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la tabla 'SoAusentismos' de la
 * base de datos.
 *
 * @author John Pineda.
 */
@Stateless
public class PersistenciaSoausentismos implements PersistenciaSoausentismosInterface {

   private static Logger log = Logger.getLogger(PersistenciaSoausentismos.class);

   /**
    * Atributo EntityManager. Representa la comunicación con la base de datos.
    */
//    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
//    private EntityManager em;
   @Override
   public void crear(EntityManager em, Soausentismos soausentismos) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(soausentismos);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaSoausentismos.crear:  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void editar(EntityManager em, Soausentismos soausentismos) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(soausentismos);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaSoausentismos.editar:  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void borrar(EntityManager em, Soausentismos soausentismos) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.remove(em.merge(soausentismos));
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaSoausentismos.borrar:  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public List<Soausentismos> ausentismosEmpleado(EntityManager em, BigInteger secuenciaEmpleado) {
      try {
         em.clear();
         String sql = "SELECT * FROM SOAUSENTISMOS WHERE EMPLEADO = ?";
         
         Query query = em.createNativeQuery(sql, Soausentismos.class);
         query.setParameter(1, secuenciaEmpleado);
         List<Soausentismos> todosAusentismos = query.getResultList();
         return todosAusentismos;
      } catch (Exception e) {
          log.error("PersistenciaSoausentismos.ausentismosEmpleado():  ", e);
         return null;
      }
   }

   @Override
   public List<Soausentismos> prorrogas(EntityManager em, BigInteger secuenciaEmpleado, BigInteger secuenciaCausa, BigInteger secuenciaAusentismo) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT soa FROM Soausentismos soa WHERE soa.empleado.secuencia= :secuenciaEmpleado AND soa.causa.secuencia= :secuenciaCausa AND soa.secuencia= :secuenciaAusentismo");
         query.setParameter("secuenciaEmpleado", secuenciaEmpleado);
         query.setParameter("secuenciaCausa", secuenciaCausa);
         query.setParameter("secuenciaAusentismo", secuenciaAusentismo);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         List<Soausentismos> prorrogas = query.getResultList();
         return prorrogas;
      } catch (Exception e) {
          log.error("PersistenciaSoausentismos.prorrogas():  ", e);
         return null;
      }
   }

   @Override
   public String prorrogaMostrar(EntityManager em, BigInteger secuenciaProrroga) {
      try {
         em.clear();
         String sqlQuery = ("SELECT nvl(A.NUMEROCERTIFICADO,'Falta # Certificado')||':'||A.fecha||'->'||A.fechafinaus\n"
                 + "FROM SOAUSENTISMOS A\n"
                 + "WHERE A.SECUENCIA = ?");
         Query query = em.createNativeQuery(sqlQuery);
         query.setParameter(1, secuenciaProrroga);
         String resultado = (String) query.getSingleResult();
         return resultado;
      } catch (Exception e) {
          log.error("PersistenciaSoausentismos.prorrogaMostrar():  ", e);
         return null;
      }
   }

   @Override
   public void adicionaAusentismoCambiosMasivos(EntityManager em,
           BigInteger secTipo, BigInteger secClase, BigInteger secCausa, BigInteger dias,
           BigInteger horas, Date fechaIniAusen, Date fechaFinAusen, Date fechaExpedicion,
           Date fechaIpago, Date fechaPago, BigInteger porcent, BigInteger baseliq, String forma) {

      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         StoredProcedureQuery query = em.createStoredProcedureQuery("CAMBIOSMASIVOS_PKG.AdicionaAusentismo");
         query.registerStoredProcedureParameter(1, BigInteger.class, ParameterMode.IN);
         query.registerStoredProcedureParameter(2, BigInteger.class, ParameterMode.IN);
         query.registerStoredProcedureParameter(3, BigInteger.class, ParameterMode.IN);
         query.registerStoredProcedureParameter(4, BigInteger.class, ParameterMode.IN);
         query.registerStoredProcedureParameter(5, BigInteger.class, ParameterMode.IN);
         query.registerStoredProcedureParameter(6, Date.class, ParameterMode.IN);
         query.registerStoredProcedureParameter(7, Date.class, ParameterMode.IN);
         query.registerStoredProcedureParameter(8, Date.class, ParameterMode.IN);
         query.registerStoredProcedureParameter(9, Date.class, ParameterMode.IN);
         query.registerStoredProcedureParameter(10, Date.class, ParameterMode.IN);
         query.registerStoredProcedureParameter(11, BigInteger.class, ParameterMode.IN);
         query.registerStoredProcedureParameter(12, BigInteger.class, ParameterMode.IN);
         query.registerStoredProcedureParameter(13, String.class, ParameterMode.IN);

         query.setParameter(1, secTipo);
         query.setParameter(2, secClase);
         query.setParameter(3, secCausa);
         query.setParameter(4, dias);
         query.setParameter(5, horas);
         query.setParameter(6, fechaIniAusen);
         query.setParameter(7, fechaFinAusen);
         query.setParameter(8, fechaExpedicion);
         query.setParameter(9, fechaIpago);
         query.setParameter(10, fechaPago);
         query.setParameter(11, porcent);
         query.setParameter(12, baseliq);
         query.setParameter(13, forma);
         query.execute();
      } catch (Exception e) {
         log.error(this.getClass().getName() + ".adicionaAusentismoCambiosMasivos() ERROR:  ", e);
         e.printStackTrace();
         if (tx.isActive()) {
            tx.rollback();
         }
      } finally {
         tx.commit();
      }
   }

   @Override
   public void undoAdicionaAusentismoCambiosMasivos(EntityManager em,
           BigInteger secTipo, BigInteger secClase, BigInteger secCausa, BigInteger dias,
           Date fechaIniAusen, Date fechaFinAusen) {

      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         StoredProcedureQuery query = em.createStoredProcedureQuery("CAMBIOSMASIVOS_PKG.UndoAdicionaAusentismo");
         query.registerStoredProcedureParameter(1, BigInteger.class, ParameterMode.IN);
         query.registerStoredProcedureParameter(2, BigInteger.class, ParameterMode.IN);
         query.registerStoredProcedureParameter(3, BigInteger.class, ParameterMode.IN);
         query.registerStoredProcedureParameter(4, BigInteger.class, ParameterMode.IN);
         query.registerStoredProcedureParameter(5, Date.class, ParameterMode.IN);
         query.registerStoredProcedureParameter(6, Date.class, ParameterMode.IN);

         query.setParameter(1, secTipo);
         query.setParameter(2, secClase);
         query.setParameter(3, secCausa);
         query.setParameter(4, dias);
         query.setParameter(5, fechaIniAusen);
         query.setParameter(6, fechaFinAusen);
         query.execute();
      } catch (Exception e) {
         log.error(this.getClass().getName() + ".undoAdicionaAusentismoCambiosMasivos() ERROR:  ", e);
         e.printStackTrace();
         if (tx.isActive()) {
            tx.rollback();
         }
      } finally {
         tx.commit();
      }
   }
}
