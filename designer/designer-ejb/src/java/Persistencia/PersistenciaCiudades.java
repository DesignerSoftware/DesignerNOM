/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.Ciudades;
import InterfacePersistencia.PersistenciaCiudadesInterface;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 * Clase Stateless. <br>
 * Clase encargada de realizar operaciones sobre la tabla 'Ciudades' de la base
 * de datos
 *
 * @author Betelgeuse
 */
@Stateless
public class PersistenciaCiudades implements PersistenciaCiudadesInterface {

   private static Logger log = Logger.getLogger(PersistenciaCiudades.class);

   /**
    * Atributo EntityManager. Representa la comunicación con la base de datos
    */
   /*@PersistenceContext(unitName = "DesignerRHN-ejbPU")
     private EntityManager em;*/
   @Override
   public void crear(EntityManager em, Ciudades ciudades) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(ciudades);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaCiudades.crear:  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void editar(EntityManager em, Ciudades ciudades) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(ciudades);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaCiudades.editar:  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public boolean borrar(EntityManager em, Ciudades ciudades) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.remove(em.merge(ciudades));
         tx.commit();
         return true;
      } catch (Exception e) {
         try {
            if (tx.isActive()) {
               tx.rollback();
            }
         } catch (Exception ex) {
            log.error("Error PersistenciaCiudades.borrar:  ", e);
         }
         return false;
      }
   }

   @Override
   public List<Ciudades> consultarCiudades(EntityManager em) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT c FROM Ciudades c ORDER BY c.nombre");
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         List<Ciudades> ciudades = query.getResultList();
         if (ciudades == null) {
            ciudades = new ArrayList<Ciudades>();
         }
         return ciudades;
      } catch (Exception e) {
         log.error("PersistenciaCiudades.consultarCiudades() e:  ", e);
         return new ArrayList<Ciudades>();
      }
   }

   @Override
   public List<Ciudades> lovCiudades(EntityManager em) {
      try {
         em.clear();
         String sql = "SELECT  SECUENCIA,  NOMBRE FROM  CIUDADES ORDER BY NOMBRE";
         Query query = em.createNativeQuery(sql, Ciudades.class);
         List<Ciudades> ciudades = query.getResultList();
         return ciudades;
      } catch (Exception e) {
         log.error("PersistenciaCiudades.lovCiudades() e:  ", e);
         return null;
      }
   }

   @Override
   public List<Ciudades> consultarCiudadesPorDepto(EntityManager em, BigInteger secDepto) {
      try {
         em.clear();
         String sql = "SELECT * FROM CIUDADES WHERE DEPARTAMENTO = ? ORDER BY NOMBRE";
         Query query = em.createNativeQuery(sql, Ciudades.class);
         query.setParameter(1, secDepto);
         List<Ciudades> ciudades = query.getResultList();
         return ciudades;
      } catch (Exception e) {
         log.error("PersistenciaCiudades.consultarCiudadesPorDepto():  ", e);
         return null;
      }
   }

}
