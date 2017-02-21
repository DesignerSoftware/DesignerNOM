/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.Cargos;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import InterfacePersistencia.PersistenciaCargosInterface;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 * Clase Stateless. <br> Clase encargada de realizar operaciones sobre la tabla
 * 'Cargos' de la base de datos
 *
 * @author Betelgeuse
 */
@Stateless
public class PersistenciaCargos implements PersistenciaCargosInterface {

   /**
    * Atributo EntityManager. Representa la comunicación con la base de datos
    */
   /*
     * @PersistenceContext(unitName = "DesignerRHN-ejbPU") private EntityManager
     * em;
    */
   @Override
   public void crear(EntityManager em, Cargos cargos) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.persist(cargos);
         tx.commit();
      } catch (Exception e) {
         System.out.println("Error PersistenciaCargos.crear: " + e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void editar(EntityManager em, Cargos cargos) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(cargos);
         tx.commit();
      } catch (Exception e) {
         System.out.println("Error PersistenciaCargos.editar: " + e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void borrar(EntityManager em, Cargos cargos) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.remove(em.merge(cargos));
         tx.commit();

      } catch (Exception e) {
         if (tx.isActive()) {
            tx.rollback();
         }
         System.out.println("Error PersistenciaCargos.borrar: " + e);
      }
   }

   @Override
   public Cargos buscarCargoSecuencia(EntityManager em, BigInteger secuencia) {
      try {
         em.clear();
         BigInteger in;
         in = (BigInteger) secuencia;
         return em.find(Cargos.class, in);
      } catch (Exception e) {
         System.out.println("Persistencia.PersistenciaCargos.buscarCargoSecuencia() e: " + e);
         return null;
      }
   }

   @Override
   public List<Cargos> consultarCargos(EntityManager em) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT c FROM Cargos c ORDER BY c.nombre");
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         List<Cargos> cargos = query.getResultList();
         return cargos;
      } catch (Exception e) {
         System.out.println("Error PersistenciaCargos.consultarCargos: " + e);
         return null;
      }
   }

   @Override
   public List<Cargos> cargosSalario(EntityManager em) {
      try {
         em.clear();
         List<Cargos> listaCargosSalario = consultarCargos(em);
         if (listaCargosSalario != null) {
            for (int i = 0; i < listaCargosSalario.size(); i++) {
               System.out.println("Secuencia: " + listaCargosSalario.get(i).getSecuencia());
               String sqlQuery2 = "SELECT cargos_pkg.capturarsalario(?, sysdate) FROM DUAL";
               Query query2 = em.createNativeQuery(sqlQuery2).setParameter(1, listaCargosSalario.get(i).getSecuencia());
               listaCargosSalario.get(i).setSueldoCargo((BigDecimal) query2.getSingleResult());
            }
         }
         return listaCargosSalario;
      } catch (Exception e) {
         System.out.println("PersistenciaCargos: Fallo el nativeQuery.cargosSalario " + e);
         return null;
      }
   }

   @Override
   public List<Cargos> buscarCargosPorSecuenciaEmpresa(EntityManager em, BigInteger secEmpresa) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT c FROM Cargos c  WHERE c.empresa.secuencia=:secEmpresa");
         query.setParameter("secEmpresa", secEmpresa);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         List<Cargos> cargos = query.getResultList();
         return cargos;
      } catch (Exception e) {
         System.out.println("Error buscarCargosPorSecuenciaEmpresa PersistenciaCargos : " + e.toString());
         return null;
      }
   }

   @Override
   public List<Cargos> lovCargos(EntityManager em) {
      try {
         em.clear();
         String sql = "SELECT ALL CARGOS.NOMBRE, CARGOS.SECUENCIA FROM CARGOS";
         Query query = em.createNativeQuery(sql, Cargos.class);
         List<Cargos> cargos = query.getResultList();
         return cargos;
      } catch (Exception e) {
         System.out.println("Error PersistenciaCargos.lovCargos: " + e);
         return null;
      }
   }
}
