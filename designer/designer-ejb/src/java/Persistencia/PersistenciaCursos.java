/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.Cursos;
import InterfacePersistencia.PersistenciaCursosInterface;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 * Clase Stateless. <br>
 * Clase encargada de realizar operaciones sobre la tabla 'Cursos' de la base de
 * datos
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaCursos implements PersistenciaCursosInterface {

   private static Logger log = Logger.getLogger(PersistenciaCursos.class);

   /**
    * Atributo EntityManager. Representa la comunicación con la base de datos
    */
   /*@PersistenceContext(unitName = "DesignerRHN-ejbPU")
    private EntityManager em;*/
   @Override
   public List<Cursos> cursos(EntityManager em) {
      try {
         em.clear();
         String sql = "SELECT * FROM CURSOS ORDER BY CODIGO ASC";
         Query query = em.createNativeQuery(sql, Cursos.class);
         List<Cursos> cursos = query.getResultList();
         return cursos;
      } catch (Exception e) {
         return null;
      }
   }

   @Override
   public void crear(EntityManager em, Cursos curso) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(curso);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaCursos.crear: " + e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void borrar(EntityManager em, Cursos curso) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.remove(em.merge(curso));
         tx.commit();

      } catch (Exception e) {
         if (tx.isActive()) {
            tx.rollback();
         }
         log.error("Error PersistenciaCursos.borrar: " + e);
      }
   }

   @Override
   public void editar(EntityManager em, Cursos curso) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(curso);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaCursos.editar: " + e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }
}
