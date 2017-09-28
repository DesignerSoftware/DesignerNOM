/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.Demandas;
import InterfacePersistencia.PersistenciaDemandasInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 * Clase Stateless. <br>
 * Clase encargada de realizar operaciones sobre la tabla 'Demandas' de la base
 * de datos
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaDemandas implements PersistenciaDemandasInterface {

   private static Logger log = Logger.getLogger(PersistenciaDemandas.class);

   /**
    * Atributo EntityManager. Representa la comunicación con la base de datos
    */
   /* @PersistenceContext(unitName = "DesignerRHN-ejbPU")
    private EntityManager em;*/
   @Override
   public void crear(EntityManager em, Demandas demandas) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(demandas);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaDemandas.crear:  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void editar(EntityManager em, Demandas demandas) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(demandas);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaDemandas.crear:  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void borrar(EntityManager em, Demandas demandas) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.remove(em.merge(demandas));
         tx.commit();
      } catch (Exception e) {
         if (tx.isActive()) {
            tx.rollback();
         }
         log.error("Error PersistenciaDemandas.borrar:  ", e);
      }
   }

   @Override
   public List<Demandas> demandasPersona(EntityManager em, BigInteger secuenciaEmpl) {
      try {
         em.clear();
         String sql = "SELECT * FROM DEMANDAS WHERE EMPLEADO = ?";
         Query queryFinal = em.createNativeQuery(sql, Demandas.class);
         queryFinal.setParameter(1, secuenciaEmpl);
         List<Demandas> listaDemandas = queryFinal.getResultList();
         return listaDemandas;
//            }
      } catch (Exception e) {
         log.error("Error PersistenciaDemandas.demandasPersona ", e);
         return null;
      }
   }

   @Override
   public String primeraDemanda(EntityManager em, BigInteger secuenciaEmpl) {
      String demanda;
      try {
         em.clear();
         String sql = "SELECT substr(B.DESCRIPCION,1,30)\n"
                 + " FROM DEMANDAS A, MOTIVOSDEMANDAS B\n"
                 + " WHERE A.EMPLEADO = (select secuencia from empleados where persona=?)\n"
                 + " AND A.MOTIVO = B.SECUENCIA\n"
                 + " AND A.SECUENCIA = (SELECT MAX(SECUENCIA) FROM DEMANDAS V WHERE V.EMPLEADO = A.EMPLEADO) AND ROWNUM = 1";
         Query queryFinal = em.createNativeQuery(sql);
         queryFinal.setParameter(1, secuenciaEmpl);
         demanda = (String) queryFinal.getSingleResult();
         if (demanda == null) {
            return demanda;
         }
      } catch (Exception e) {
         if (e.getMessage().contains("did not retrieve any entities")) {
            log.trace("Error Persistencia.PersistenciaDemandas.primeraDemanda(): " + e);
         } else {
            log.error("Error Persistencia.PersistenciaDemandas.primeraDemanda():  ", e);
         }
      }
      return "";
   }

}
