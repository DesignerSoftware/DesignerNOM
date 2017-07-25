/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.VigenciasCuentas;
import InterfacePersistencia.PersistenciaVigenciasCuentasInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la tabla 'VigenciasCuentas' de
 * la base de datos.
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaVigenciasCuentas implements PersistenciaVigenciasCuentasInterface {

   private static Logger log = Logger.getLogger(PersistenciaVigenciasCuentas.class);

   /**
    * Atributo EntityManager. Representa la comunicación con la base de datos.
    */
   /*    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
    private EntityManager em;
    */

   @Override
   public void crear(EntityManager em, VigenciasCuentas vigenciasCuentas) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.persist(vigenciasCuentas);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaVigenciasCuentas.crear: " + e.getMessage());
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void editar(EntityManager em, VigenciasCuentas vigenciasCuentas) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(vigenciasCuentas);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaVigenciasCuentas.editar: " + e.getMessage());
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void borrar(EntityManager em, VigenciasCuentas vigenciasCuentas) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.remove(em.merge(vigenciasCuentas));
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaVigenciasCuentas.borrar: " + e.getMessage());
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public List<VigenciasCuentas> buscarVigenciasCuentas(EntityManager em) {
      em.clear();
      Query query = em.createQuery("SELECT v FROM VigenciasCuentas v");
      query.setHint("javax.persistence.cache.storeMode", "REFRESH");
      List<VigenciasCuentas> vigenciasCuentas = (List<VigenciasCuentas>) query.getResultList();
      return vigenciasCuentas;
   }

   @Override
   public VigenciasCuentas buscarVigenciaCuentaSecuencia(EntityManager em, BigInteger secuencia) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT vc FROM VigenciasCuentas vc WHERE vc.secuencia = :secuencia");
         query.setParameter("secuencia", secuencia);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         VigenciasCuentas vigenciasCuentas = (VigenciasCuentas) query.getSingleResult();
         return vigenciasCuentas;
      } catch (Exception e) {
          log.error("Persistencia.PersistenciaVigenciasCuentas.buscarVigenciaCuentaSecuencia() + e.getMessage()");
         return null;
      }
   }

   @Override
   public List<VigenciasCuentas> buscarVigenciasCuentasPorCredito(EntityManager em, BigInteger secuencia) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT vc FROM VigenciasCuentas vc WHERE vc.cuentac.secuencia =:secuencia");
         query.setParameter("secuencia", secuencia);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         List<VigenciasCuentas> vigenciasCuentas = (List<VigenciasCuentas>) query.getResultList();
         return vigenciasCuentas;
      } catch (Exception e) {
         log.error("Error buscarVigenciasCuentasPorCredito Persistencia : " + e.getMessage());
         return null;
      }
   }

   @Override
   public List<VigenciasCuentas> buscarVigenciasCuentasPorDebito(EntityManager em, BigInteger secuencia) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT vc FROM VigenciasCuentas vc WHERE vc.cuentad.secuencia =:secuencia");
         query.setParameter("secuencia", secuencia);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         List<VigenciasCuentas> vigenciasCuentas = (List<VigenciasCuentas>) query.getResultList();
         return vigenciasCuentas;
      } catch (Exception e) {
         log.error("Error buscarVigenciasCuentasPorDebito Persistencia : " + e.getMessage());
         return null;
      }
   }

   @Override
   public List<VigenciasCuentas> buscarVigenciasCuentasPorConcepto(EntityManager em, BigInteger secuencia) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT vc FROM VigenciasCuentas vc WHERE vc.concepto.secuencia=:secuencia");
         query.setParameter("secuencia", secuencia);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         List<VigenciasCuentas> vigenciasCuentas = (List<VigenciasCuentas>) query.getResultList();
         return vigenciasCuentas;
      } catch (Exception e) {
         log.error("Error buscarVigenciasCuentasPorConcepto Persistencia : " + e.getMessage());
        return null;
      }
   }
}
