/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import InterfacePersistencia.PersistenciaIBCSInterface;
import Entidades.Ibcs;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la tabla 'IBCS' de la base de
 * datos.
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaIBCS implements PersistenciaIBCSInterface {

   private static Logger log = Logger.getLogger(PersistenciaIBCS.class);

   /**
    * Atributo EntityManager. Representa la comunicación con la base de datos.
    */
   /*@PersistenceContext(unitName = "DesignerRHN-ejbPU")
     private EntityManager em;*/
   @Override
   public void crear(EntityManager em, Ibcs ibcs) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(ibcs);
         tx.commit();
      } catch (Exception e) {
         log.error("La vigencia no exite o esta reservada por lo cual no puede ser modificada:  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
         log.error("No se puede hacer rollback porque no hay una transacción");
      }
   }

   @Override
   public void editar(EntityManager em, Ibcs ibcs) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(ibcs);
         tx.commit();
      } catch (Exception e) {
         log.error("La vigencia no exite o esta reservada por lo cual no puede ser modificada:  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
         log.error("No se puede hacer rollback porque no hay una transacción");
      }
   }

   @Override
   public void borrar(EntityManager em, Ibcs ibcs) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.remove(em.merge(ibcs));
         tx.commit();
      } catch (Exception e) {
         log.error("La vigencia no exite o esta reservada por lo cual no puede ser modificada:  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
         log.error("No se puede hacer rollback porque no hay una transacción");
      }
   }

   @Override
   public Ibcs buscarIbcs(EntityManager em, BigInteger secuencia) {
      try {
         em.clear();
         return em.find(Ibcs.class, secuencia);
      } catch (Exception e) {
         log.error("Error en la persistenciaIBCS formas pagos ERROR :  ", e);
         return null;
      }
   }

   @Override
   public List<Ibcs> buscarIbcsPorEmpleado(EntityManager em, BigInteger secEmpleado) {
      log.warn("PersistenciaIBCS.buscarIbcsPorEmpleado()");
      log.warn("empleado en buscar IBC por empleado : " + secEmpleado);
      try {
         em.clear();
         String sql = "SELECT * FROM Ibcs ib WHERE ib.empleado = ? ORDER BY ib.fechainicial DESC";
         Query query = em.createNativeQuery(sql, Ibcs.class);
         query.setParameter(1, secEmpleado);
         List<Ibcs> ibcs = query.getResultList();
         return ibcs;
      } catch (Exception e) {
         log.error("Error en PersistenciaIBCS Por Empleados ERROR:  ", e);
         return null;
      }
   }
}
