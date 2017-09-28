/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.VigenciasCargos;
import InterfacePersistencia.PersistenciaVigenciasCargosInterface;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.ParameterMode;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.criteria.CriteriaQuery;

/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la tabla 'VigenciasCargos' de
 * la base de datos.
 *
 * @author betelgeuse
 */
@Local
@Stateless
public class PersistenciaVigenciasCargos implements PersistenciaVigenciasCargosInterface {

   private static Logger log = Logger.getLogger(PersistenciaVigenciasCargos.class);

   /**
    * Atributo EntityManager. Representa la comunicación con la base de datos.
    *
    * @param em
    */
   /*@PersistenceContext(unitName = "DesignerRHN-ejbPU")
     private EntityManager emr;*/
   //private UserTransaction utx;
   @Override
   public void crear(EntityManager em, VigenciasCargos vigenciasCargos) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(vigenciasCargos);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaVigenciasCargos.crear:  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void editar(EntityManager em, VigenciasCargos vigenciasCargos) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(vigenciasCargos);
         tx.commit();
      } catch (Exception e) {
         if (tx.isActive()) {
            tx.rollback();
         }
         log.error("Error PersistenciaVigenciasCargos.editar:  ", e);
      }
   }

   @Override
   public void borrar(EntityManager em, VigenciasCargos vigenciasCargos) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.remove(em.merge(vigenciasCargos));
         tx.commit();

      } catch (Exception e) {
         try {
            if (tx.isActive()) {
               tx.rollback();
            }
         } catch (Exception ex) {
            log.error("Error PersistenciaVigenciasCargos.borrar:  ", e);
         }
      }
   }

   @Override
   public VigenciasCargos buscarVigenciaCargo(EntityManager em, BigInteger secuencia) {
      try {
         em.clear();
         return em.find(VigenciasCargos.class, secuencia);
      } catch (Exception e) {
         return null;
      }
   }

   @Override
   public VigenciasCargos buscarVigenciaCargoXEmpleado(EntityManager em, BigInteger secuenciaEmpl, BigInteger secEmpresa) {
      try {
         em.clear();
         String sqlString = "SELECT v.* FROM Empleados e, VigenciasCargos v, Empresas em \n"
                 + "WHERE v.empleado = e.secuencia \n"
                 + "AND em.secuencia = " + secEmpresa + " \n"
                 + "AND e.secuencia = " + secuenciaEmpl + " \n"
                 + "AND e.empresa = em.secuencia";
         Query query = em.createNativeQuery(sqlString, VigenciasCargos.class);
         VigenciasCargos vigCargo = (VigenciasCargos) query.getSingleResult();
         return vigCargo;
      } catch (Exception e) {
         log.error(this.getClass().getName() + "buscarVigenciaCargoXEmpleado catch() ERROR :  ", e);
         return null;
      }
   }

   @Override
   public List<VigenciasCargos> buscarVigenciasCargos(EntityManager em) {
      em.clear();
      CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
      cq.select(cq.from(VigenciasCargos.class));
      return em.createQuery(cq).getResultList();
   }

   @Override
   public List<VigenciasCargos> buscarVigenciasCargosEmpleado(EntityManager em, BigInteger secEmpleado) {
      try {
         em.clear();
         String sql="SELECT * FROM VIGENCIASCARGOS WHERE EMPLEADO = ? ORDER BY FECHAVIGENCIA DESC";
         Query query2 = em.createNativeQuery(sql, VigenciasCargos.class);
         query2.setParameter(1, secEmpleado);
         List<VigenciasCargos> vigenciasCargos = (List<VigenciasCargos>) query2.getResultList();
         return vigenciasCargos;
      } catch (Exception e) {
          log.error("error en buscarVigenciasCargosEmpleado :  ", e);
         List<VigenciasCargos> vigenciasCargos = null;
         return vigenciasCargos;
      }
   }
   
   @Override
   public void adicionaEmplJefeCambiosMasivos(EntityManager em, BigInteger secEmpl, Date fechaCambio) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         StoredProcedureQuery query = em.createStoredProcedureQuery("CAMBIOSMASIVOS_PKG.AdicionaEmpljefe");
         query.registerStoredProcedureParameter(1, BigInteger.class, ParameterMode.IN);
         query.registerStoredProcedureParameter(2, Date.class, ParameterMode.IN);

         query.setParameter(1, secEmpl);
         query.setParameter(2, fechaCambio);
         query.execute();
      } catch (Exception e) {
         log.error(this.getClass().getName() + ".adicionaEmplJefeCambiosMasivos() ERROR:  ", e);
         e.printStackTrace();
         if (tx.isActive()) {
            tx.rollback();
         }
      } finally {
         tx.commit();
      }
   }
}
