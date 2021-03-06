/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.Encargaturas;
import InterfacePersistencia.PersistenciaEncargaturasInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Query;

/**
 * Clase Stateless. <br>
 * Clase encargada de realizar operaciones sobre la tabla 'Encargaturas' de la
 * base de datos.
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaEncargaturas implements PersistenciaEncargaturasInterface {

   private static Logger log = Logger.getLogger(PersistenciaEncargaturas.class);

   /*@PersistenceContext(unitName = "DesignerRHN-ejbPU")
     private EntityManager em;*/
   private Long contarReemplazoPersona(EntityManager em, BigInteger secuenciaEmpleado) {
      Long resultado = null;
      try {
         em.clear();
         Query query = em.createQuery("SELECT COUNT(e) FROM Encargaturas e WHERE e.empleado.secuencia = :secuenciaEmpleado");
         query.setParameter("secuenciaEmpleado", secuenciaEmpleado);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         resultado = (Long) query.getSingleResult();
         return resultado;
      } catch (Exception e) {
         log.error("PersistenciaEncargaturas.contarReemplazoPersona()");
         e.printStackTrace();
         return resultado;
      }
   }

   /**
    * Atributo EntityManager. Representa la comunicación con la base de datos
    */
   @Override
   public List<Encargaturas> reemplazoPersona(EntityManager em, BigInteger secuenciaEmpleado) {
      log.warn(this.getClass().getName() + ".reemplazoPersona()");
      Long resultado = this.contarReemplazoPersona(em, secuenciaEmpleado);
      if (resultado != null && resultado > 0) {
         try {
            /*em.clear();
                 Query query = em.createQuery("SELECT COUNT(e) FROM Encargaturas e WHERE e.empleado.secuencia = :secuenciaEmpleado");
                 query.setParameter("secuenciaEmpleado", secuenciaEmpleado);
                 query.setHint("javax.persistence.cache.storeMode", "REFRESH");
                 Long resultado = (Long) query.getSingleResult();*/
            Query queryFinal = em.createQuery("SELECT e FROM Encargaturas e WHERE e.empleado.secuencia = :secuenciaEmpleado and e.fechainicial = (SELECT MAX(en.fechainicial) FROM Encargaturas en WHERE en.empleado.secuencia = :secuenciaEmpleado)");
            queryFinal.setParameter("secuenciaEmpleado", secuenciaEmpleado);
            queryFinal.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<Encargaturas> listaEncargaturas = queryFinal.getResultList();
            return listaEncargaturas;
         } catch (Exception e) {
            log.error("Error PersistenciaEncargaturas.reemplazoPersona ", e);
            return null;
         }
      } else {
         log.error("el conteo no proporciono datos válidos");
         return null;
      }
   }

   @Override
   public void crear(EntityManager em, Encargaturas encargaturas) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(encargaturas);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaEncargaturas.crear:  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
         log.error("la transaccion se cerro");
      }
   }

   @Override
   public void editar(EntityManager em, Encargaturas encargaturas) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(encargaturas);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaEncargaturas.editar:  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
         log.error("se cerró la transacción");
      }
   }

   @Override
   public void borrar(EntityManager em, Encargaturas encargaturas) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.remove(em.merge(encargaturas));
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaEncargaturas.borrar:  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
         log.error("Se cerro la transacción");
      }
   }

   @Override
   public List<Encargaturas> buscarEncargaturas(EntityManager em) {
      try {
         em.clear();
         javax.persistence.criteria.CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
         cq.select(cq.from(Encargaturas.class));
         return em.createQuery(cq).getResultList();
      } catch (Exception e) {
         log.error("error en buscarEncargaturas");
         e.printStackTrace();
         return null;
      }
   }

   @Override
   public List<Encargaturas> encargaturasEmpleado(EntityManager em, BigInteger secuenciaEmpleado) {
      try {
         em.clear();
         String sql = "SELECT * FROM ENCARGATURAS WHERE EMPLEADO = ? ORDER BY FECHAINICIAL";
         Query query = em.createNativeQuery(sql, Encargaturas.class);
         query.setParameter(1, secuenciaEmpleado);
         List<Encargaturas> listaEncargaturas = query.getResultList();
         return listaEncargaturas;
      } catch (Exception e) {
         log.error("Error PersistenciaEncargaturas.encargaturasEmpleado ", e);
         return null;
      }
   }

   @Override
   public String primeraEncargatura(EntityManager em, BigInteger secuenciaEmpleado) {
      String reemplazo;
      try {
         em.clear();
         String sql = "select t.nombre||' '||to_char(e.fechainicial,'dd-mm-yyyy')\n"
                 + " from encargaturas e, tiposreemplazos t\n"
                 + " where e.tiporeemplazo = t.secuencia\n"
                 + " and e.empleado = (select secuencia from empleados where persona=?) \n"
                 + " and e.fechainicial = (select max(ei.fechainicial) from encargaturas ei where ei.empleado = e.empleado) AND ROWNUM = 1";
         Query query = em.createNativeQuery(sql);
         query.setParameter(1, secuenciaEmpleado);
         if (query.getSingleResult() == null) {
            reemplazo = " ";
         } else {
            reemplazo = (String) query.getSingleResult();
         }
         return reemplazo;
      } catch (NoResultException e) {
         if (e.getMessage().contains("did not retrieve any entities")) {
            log.trace("PersistenciaEncargaturas.primeraEncargatura() e: " + e);
         } else {
            log.error("PersistenciaEncargaturas.primeraEncargatura() e:  ", e);
         }
         return " ";
      }
   }

}
