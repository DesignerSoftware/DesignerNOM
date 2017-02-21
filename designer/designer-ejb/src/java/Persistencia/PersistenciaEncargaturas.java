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
import javax.persistence.EntityTransaction;
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
         System.out.println("Persistencia.PersistenciaEncargaturas.contarReemplazoPersona()");
         e.printStackTrace();
         return resultado;
      }
   }

   /**
    * Atributo EntityManager. Representa la comunicación con la base de datos
    */
   @Override
   public List<Encargaturas> reemplazoPersona(EntityManager em, BigInteger secuenciaEmpleado) {
      System.out.println(this.getClass().getName() + ".reemplazoPersona()");
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
            System.out.println("Error PersistenciaEncargaturas.reemplazoPersona" + e);
            return null;
         }
      } else {
         System.out.println("el conteo no proporciono datos válidos");
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
         System.out.println("Error PersistenciaEncargaturas.crear: " + e);
         if (tx.isActive()) {
            tx.rollback();
         }
         System.out.println("la transaccion se cerro");
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
         System.out.println("Error PersistenciaEncargaturas.editar: " + e);
         if (tx.isActive()) {
            tx.rollback();
         }
         System.out.println("se cerró la transacción");
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
         System.out.println("Error PersistenciaEncargaturas.borrar: " + e);
         if (tx.isActive()) {
            tx.rollback();
         }
         System.out.println("Se cerro la transacción");
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
         System.out.println("error en buscarEncargaturas");
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
         System.out.println("Error PersistenciaEncargaturas.encargaturasEmpleado" + e);
         return null;
      }
   }

   @Override
   public String primeraEncargatura(EntityManager em, BigInteger secuenciaEmpleado) {
      String reemplazo;
      try {
         em.clear();
         String sql = " select t.nombre||' '||to_char(e.fechainicial,'dd-mm-yyyy')\n"
                 + "	 from encargaturas e, tiposreemplazos t\n"
                 + "	 where e.tiporeemplazo = t.secuencia\n"
                 + "	 and e.empleado = (select secuencia from empleados where persona=?)\n"
                 + "   and e.fechainicial = (select max(ei.fechainicial) from encargaturas ei where ei.empleado = e.empleado)";
         Query query = em.createNativeQuery(sql);
         query.setParameter(1, secuenciaEmpleado);
         reemplazo = (String) query.getSingleResult();
         return reemplazo;

      } catch (Exception e) {
         System.out.println("Persistencia.PersistenciaEncargaturas.primeraEncargatura() e: " + e);
         e.printStackTrace();
         reemplazo = "";
         return reemplazo;
      }
   }

}
