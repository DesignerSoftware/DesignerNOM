/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.Empleados;
import Entidades.VigenciasTiposContratos;
import InterfacePersistencia.PersistenciaVigenciasTiposContratosInterface;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;

/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la tabla
 * 'VigenciasTiposContratos' de la base de datos.
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaVigenciasTiposContratos implements PersistenciaVigenciasTiposContratosInterface {

   @Override
   public boolean crear(EntityManager em, VigenciasTiposContratos vigenciasTiposContratos) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.persist(vigenciasTiposContratos);
         tx.commit();
         return true;
      } catch (Exception e) {
         System.out.println("Persistencia.PersistenciaVigenciasTiposContratos.crear()" + e.getMessage());
         if (tx.isActive()) {
            tx.rollback();
         }
         return false;
      }
   }

   @Override
   public void editar(EntityManager em, VigenciasTiposContratos vigenciasTiposContratos) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(vigenciasTiposContratos);
         tx.commit();
      } catch (Exception e) {
         System.out.println("Persistencia.PersistenciaVigenciasTiposContratos.editar()" + e.getMessage());
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void borrar(EntityManager em, VigenciasTiposContratos vigenciasTiposContratos) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.remove(em.merge(vigenciasTiposContratos));
         tx.commit();
      } catch (Exception e) {
         System.out.println("Persistencia.PersistenciaVigenciasTiposContratos.borrar()" + e.getMessage());
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public VigenciasTiposContratos buscarVigenciaTipoContrato(EntityManager em, BigInteger secuencia) {
      try {
         em.clear();
         return em.find(VigenciasTiposContratos.class, secuencia);
      } catch (Exception e) {
         System.out.println("Persistencia.PersistenciaVigenciasTiposContratos.buscarVigenciaTipoContrato()" + e.getMessage());
         return null;
      }
   }

   @Override
   public List<VigenciasTiposContratos> buscarVigenciasTiposContratos(EntityManager em) {
      try {
         em.clear();
         CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
         cq.select(cq.from(VigenciasTiposContratos.class));
         return em.createQuery(cq).getResultList();
      } catch (Exception e) {
         System.out.println("Persistencia.PersistenciaVigenciasTiposContratos.buscarVigenciasTiposContratos()" + e.getMessage());
         return null;
      }
   }

   @Override
   public List<VigenciasTiposContratos> buscarVigenciaTipoContratoEmpleado(EntityManager em, BigInteger secuencia) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT vtc FROM VigenciasTiposContratos vtc WHERE vtc.empleado.secuencia = :secuenciaEmpl ORDER BY vtc.fechavigencia DESC");
         query.setParameter("secuenciaEmpl", secuencia);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         List<VigenciasTiposContratos> vigenciasTiposContratos = (List<VigenciasTiposContratos>) query.getResultList();
         return vigenciasTiposContratos;
      } catch (Exception e) {
         System.out.println("Persistencia.PersistenciaVigenciasTiposContratos.buscarVigenciaTipoContratoEmpleado()" + e.getMessage());
         return null;
      }
   }

   @Override
   public Date fechaMaxContratacion(EntityManager em, Empleados secuencia) {
      try {
         em.clear();
         Date fechaContratacion;
         Query query = em.createQuery("SELECT vwac.fechaVigencia FROM VWActualesTiposContratos vwac WHERE vwac.empleado =:empleado");
         query.setParameter("empleado", secuencia);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         fechaContratacion = (Date) query.getSingleResult();
         return fechaContratacion;
      } catch (Exception e) {
         System.out.println("Error fechaMaxContratacion PersistenciaTiposContratos : " + e.getMessage());
         return null;
      }
   }

   @Override
   public Date fechaFinalContratacionVacaciones(EntityManager em, BigInteger secuencia) {
      try {
         em.clear();
         Date fecha;
         Query query2 = em.createQuery("SELECT vtc.fechavigencia FROM VigenciasTiposContratos vtc WHERE vtc.empleado.secuencia =:secuencia AND vtc.fechavigencia = (SELECT MIN(vtci.fechavigencia) FROM VigenciasTiposContratos vtci WHERE vtci.empleado.secuencia = vtc.empleado.secuencia AND vtci.fechavigencia <= (SELECT vaf.fechaHastaCausado FROM VWActualesFechas vaf))");
         query2.setParameter("secuencia", secuencia);
         query2.setHint("javax.persistence.cache.storeMode", "REFRESH");
         fecha = (Date) query2.getSingleResult();
         return fecha;
      } catch (Exception e) {
         System.out.println("Error fechaFinalContratacionVacaciones PersistenciaTiposContratos : " + e.getMessage());
         return null;
      }
   }
}
