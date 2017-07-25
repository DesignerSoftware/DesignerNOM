/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.GruposSalariales;
import InterfacePersistencia.PersistenciaGruposSalarialesInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la tabla 'GruposSalariales' de
 * la base de datos.
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaGruposSalariales implements PersistenciaGruposSalarialesInterface {

   private static Logger log = Logger.getLogger(PersistenciaGruposSalariales.class);

   /**
    * Atributo EntityManager. Representa la comunicación con la base de datos
    */
   /*@PersistenceContext(unitName = "DesignerRHN-ejbPU")
    private EntityManager em;*/

   @Override
   public void crear(EntityManager em, GruposSalariales gruposSalariales) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(gruposSalariales);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaGruposSalariales.crear: " + e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void editar(EntityManager em, GruposSalariales gruposSalariales) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(gruposSalariales);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaGruposSalariales.crear: " + e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void borrar(EntityManager em, GruposSalariales gruposSalariales) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.remove(em.merge(gruposSalariales));
         tx.commit();
      } catch (Exception e) {
         if (tx.isActive()) {
            tx.rollback();
         }
         log.error("Error PersistenciaGruposSalariales.borrar: " + e);
      }
   }

   @Override
   public List<GruposSalariales> buscarGruposSalariales(EntityManager em) {
      try {
         em.clear();
         String sql = "SELECT * FROM GRUPOSSALARIALES ORDER BY CODIGO";
         Query query = em.createNativeQuery(sql, GruposSalariales.class);
         List<GruposSalariales> gruposSalariales = query.getResultList();
         return gruposSalariales;
      } catch (Exception e) {
         log.error("Error buscarGruposSalariales PersistenciaGruposSalariales : " + e.toString());
         return null;
      }
   }

   @Override
   public GruposSalariales buscarGrupoSalarialSecuencia(EntityManager em, BigInteger secuencia) {

      try {
         em.clear();
         String sql = "SELECT * FROM GruposSalariales WHERE secuencia = ?";
         Query query = em.createNativeQuery(sql, GruposSalariales.class);
         query.setParameter(1, secuencia);
         GruposSalariales gruposSalariales = (GruposSalariales) query.getSingleResult();
         return gruposSalariales;
      } catch (Exception e) {
         log.error("Error buscarGrupoSalarialSecuencia PersistenciaGruposSalariales : " + e.toString());
         GruposSalariales gruposSalariales = null;
         return gruposSalariales;
      }

   }

   @Override
   public List<GruposSalariales> buscarGruposSalarialesParaEscalafonSalarial(EntityManager em, BigInteger secEscalafon) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT gs FROM GruposSalariales gs WHERE gs.escalafonsalarial.secuencia=:secEscalafon");
         query.setParameter("secEscalafon", secEscalafon);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         List<GruposSalariales> gruposSalariales = query.getResultList();
         return gruposSalariales;
      } catch (Exception e) {
         log.error("Error buscarGruposSalarialesParaEscalafonSalarial PersistenciaGruposSalariales : " + e.toString());
         return null;
      }
   }
}
