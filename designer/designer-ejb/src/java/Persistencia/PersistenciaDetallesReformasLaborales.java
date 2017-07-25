/**
 * Documentación a cargo de AndresPineda
 */
package Persistencia;

import Entidades.DetallesReformasLaborales;
import InterfacePersistencia.PersistenciaDetallesReformasLaboralesInterface;
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
 * Clase encargada de realizar operaciones sobre la tabla
 * 'DetallesReformasLaborales' de la base de datos.
 *
 * @author AndresPineda
 */
@Stateless
public class PersistenciaDetallesReformasLaborales implements PersistenciaDetallesReformasLaboralesInterface {

   private static Logger log = Logger.getLogger(PersistenciaDetallesReformasLaborales.class);

   /**
    * Atributo EntityManager. Representa la comunicación con la base de datos.
    */
   /*@PersistenceContext(unitName = "DesignerRHN-ejbPU")
    private EntityManager em;*/
   @Override
   public void crear(EntityManager em, DetallesReformasLaborales detallesReformasLaborales) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.persist(detallesReformasLaborales);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaDetallesReformasLaborales.crear: " + e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void editar(EntityManager em, DetallesReformasLaborales detallesReformasLaborales) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(detallesReformasLaborales);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaDetallesReformasLaborales.crear: " + e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void borrar(EntityManager em, DetallesReformasLaborales detallesReformasLaborales) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.remove(em.merge(detallesReformasLaborales));
         tx.commit();

      } catch (Exception e) {
         if (tx.isActive()) {
            tx.rollback();
         }
         log.error("Error PersistenciaDetallesReformasLaborales.borrar: " + e);
      }
   }

   @Override
   public List<DetallesReformasLaborales> buscarDetallesReformasLaborales(EntityManager em) {
      em.clear();
      Query query = em.createQuery("SELECT d FROM DetallesReformasLaborales d");
      query.setHint("javax.persistence.cache.storeMode", "REFRESH");
      List<DetallesReformasLaborales> detallesReformasLaborales = (List<DetallesReformasLaborales>) query.getResultList();
      return detallesReformasLaborales;
   }

   @Override
   public DetallesReformasLaborales buscarDetalleReformaSecuencia(EntityManager em, BigInteger secuencia) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT d FROM DetallesReformasLaborales d WHERE d.secuencia = :secuencia");
         query.setParameter("secuencia", secuencia);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         DetallesReformasLaborales detallesReformasLaborales = (DetallesReformasLaborales) query.getSingleResult();
         return detallesReformasLaborales;
      } catch (Exception e) {
         log.error("Error buscarDetalleReformaSecuencia PersistenciaDetallesReformasLaborales : " + e.toString());
         DetallesReformasLaborales detallesReformasLaborales = null;
         return detallesReformasLaborales;
      }
   }

   @Override
   public List<DetallesReformasLaborales> buscarDetalleReformasParaReformaSecuencia(EntityManager em, BigInteger secuencia) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT d FROM DetallesReformasLaborales d WHERE d.reformalaboral.secuencia=:secuencia ORDER BY d.factor ASC");
         query.setParameter("secuencia", secuencia);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         List<DetallesReformasLaborales> detallesReformasLaborales = (List<DetallesReformasLaborales>) query.getResultList();
         return detallesReformasLaborales;
      } catch (Exception e) {
         log.error("Error buscarDetalleReformasParaReformaSecuencia PersistenciaDetallesReformasLaborales : " + e.toString());
         List<DetallesReformasLaborales> detallesReformasLaborales = null;
         return detallesReformasLaborales;
      }
   }

   public String clonarReformaLaboral(EntityManager em, String nuevoNombre, short codigoNuevo, short codOrigen) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         log.error("nuevoNombre : " + nuevoNombre);
         log.error("codigoNuevo : " + codigoNuevo);
         log.error("codOrigen : " + codOrigen);
         StoredProcedureQuery query = em.createStoredProcedureQuery("REFORMASLABORALES_PKG.CLONARREFORMALABORAL");
         query.registerStoredProcedureParameter(1, String.class, ParameterMode.INOUT);
         query.registerStoredProcedureParameter(2, short.class, ParameterMode.IN);
         query.registerStoredProcedureParameter(3, BigInteger.class, ParameterMode.IN);

         query.setParameter(1, nuevoNombre);
         query.setParameter(2, codigoNuevo);
         query.setParameter(3, codOrigen);

         query.execute();
         query.hasMoreResults();
         String strRetorno = (String) query.getOutputParameterValue(1);
         log.error("PersistenciaDetallesReformasLaborales.clonarReformaLaboral() Ya clono strRetorno:_" + strRetorno + "_");
         return strRetorno;
      } catch (Exception e) {
         log.error("ERROR: " + this.getClass().getName() + ".clonarReformaLaboral()");
         e.printStackTrace();
         if (tx.isActive()) {
            tx.rollback();
         }
         return "ERROR EN LA TRANSACCION DESDE EL SISTEMA";
      } finally {
         tx.commit();
      }
   }
}
