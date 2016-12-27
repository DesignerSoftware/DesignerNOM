/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.Empleados;
import Entidades.Papeles;
import Entidades.VigenciasCargos;
import InterfacePersistencia.PersistenciaVigenciasCargosInterface;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
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
         System.out.println("Error PersistenciaVigenciasCargos.crear: " + e.toString());
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
         System.out.println("Error PersistenciaVigenciasCargos.editar: " + e);
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
            System.out.println("Error PersistenciaVigenciasCargos.borrar: " + e);
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
         System.out.println("buscarVigenciaCargoXEmpleado() secuenciaEmpl : " + secuenciaEmpl);
         System.out.println("buscarVigenciaCargoXEmpleado() secEmpresa : " + secEmpresa);
         em.clear();
         String sqlString = "SELECT v.* FROM Empleados e, VigenciasCargos v, Empresas em \n"
                 + "WHERE v.empleado = e.secuencia \n"
                 + "AND em.secuencia = " + secEmpresa + " \n"
                 + "AND e.secuencia = " + secuenciaEmpl + " \n"
                 + "AND e.empresa = em.secuencia";
         System.out.println("sqlString : " + sqlString);
//            String sqlString = "SELECT v FROM Empleados e, VigenciasCargos v, Empresas em WHERE v.empleado.secuencia = e.secuencia "
//                    + "AND e.empresa.secuencia = em.secuencia AND e.secuencia = " + secuenciaEmpl + " AND em.secuencia =" + secEmpresa +"";
//            Query query = em.createNativeQuery("SELECT VC.* FROM EMPLEADOS E, VIGENCIASCARGOS VC, EMPRESAS EM WHERE VC.EMPLEADO = E.SECUENCIA AND E.EMPRESA = EM.SECUENCIA AND E.SECUENCIA = ?  AND EM.SECUENCIA = ?");
         Query query = em.createNativeQuery(sqlString, VigenciasCargos.class);

//         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         VigenciasCargos vigCargo = (VigenciasCargos) query.getSingleResult();
         System.out.println("buscarVigenciaCargoXEmpleado() secCargo : " + vigCargo);

         return vigCargo;
      } catch (Exception e) {
         System.err.println(this.getClass().getName() + "buscarVigenciaCargoXEmpleado catch() ERROR : " + e);
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
          System.out.println("error en buscarVigenciasCargosEmpleado : " + e.toString());
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
         System.out.println(this.getClass().getName() + ".adicionaEmplJefeCambiosMasivos() Ya ejecuto");
      } catch (Exception e) {
         System.err.println(this.getClass().getName() + ".adicionaEmplJefeCambiosMasivos() ERROR: " + e);
         e.printStackTrace();
         if (tx.isActive()) {
            tx.rollback();
         }
      } finally {
         tx.commit();
      }
   }
}
