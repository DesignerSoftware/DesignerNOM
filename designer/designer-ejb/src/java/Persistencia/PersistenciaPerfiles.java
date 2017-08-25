package Persistencia;

import Entidades.Perfiles;
import InterfacePersistencia.PersistenciaPerfilesInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 *
 * @author Administrador
 */
@Stateless
public class PersistenciaPerfiles implements PersistenciaPerfilesInterface {

   private static Logger log = Logger.getLogger(PersistenciaPerfiles.class);

   @Override
   public Perfiles consultarPerfil(EntityManager em, BigInteger secuencia) {
      log.warn("PersistenciaPerfiles.consultarPerfil() em.getProperties(): " + em.getProperties());
      log.warn("PersistenciaPerfiles.consultarPerfil() em.isOpen(): " + em.isOpen());
      try {
         em.clear();
         return em.find(Perfiles.class, secuencia);
      } catch (Exception e) {
         log.error("Persistencia.PersistenciaPerfiles.consultarPerfil()" + e.getMessage());
         return null;
      }
   }

   @Override
   public List<Perfiles> consultarPerfiles(EntityManager em) {
      try {
         em.clear();
         javax.persistence.criteria.CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
         cq.select(cq.from(Perfiles.class));
         return em.createQuery(cq).getResultList();
      } catch (Exception e) {
         log.error("Persistencia.PersistenciaPerfiles.consultarPerfiles()" + e.getMessage());
         return null;
      }
   }

   @Override
   public Perfiles consultarPerfilPorUsuario(EntityManager em) {
      try {
         em.clear();
         String sql = "SELECT P.* FROM PERFILES P , VWACTUALUSUARIO U \n"
                 + "WHERE P.SECUENCIA = U.PERFIL";
         Query query = em.createNativeQuery(sql, Perfiles.class);
         Perfiles perfil = (Perfiles) query.getSingleResult();
         return perfil;
      } catch (Exception e) {
         log.error("Error en PersistenciaPerfiles : " + e);
         return null;
      }
   }

   @Override
   public void crear(EntityManager em, Perfiles perfil) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(perfil);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaPerfiles.crear: " + e.getMessage());
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void editar(EntityManager em, Perfiles perfil) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(perfil);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaPerfiles.editar: " + e.getMessage());
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void borrar(EntityManager em, Perfiles perfil) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.remove(em.merge(perfil));
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaPerfiles.borrar: " + e.getMessage());
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public List<Perfiles> consultarPerfilesAdmon(EntityManager em) {
      try {
         em.clear();
         String sql = "SELECT * FROM PERFILES P WHERE p.DESCRIPCION != 'AUDITORIA'";
         Query query = em.createNativeQuery(sql, Perfiles.class);
         List<Perfiles> lista = query.getResultList();
         return lista;
      } catch (Exception e) {
         log.error("Persistencia.PersistenciaPerfiles.consultarPerfilesAdmon()" + e.getMessage());
         return null;
      }
   }

   @Override
   public void ejecutarPKGRecrearPerfil(EntityManager em, String descripcion, String pwd) {
      log.warn("descripcion " + descripcion);
      log.warn("pwd " + pwd);
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         String sqlQuery2 = "call PERFILES_PKG.RecrearPerfil(?,?)";
         Query query2 = em.createNativeQuery(sqlQuery2);
         query2.setParameter(1, descripcion);
         query2.setParameter(2, pwd);
         query2.executeUpdate();
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaPerfiles.ejecutarPKGRecrearPerfil. " + e.toString());
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void ejecutarPKGEliminarPerfil(EntityManager em, String descripcion) {
      log.warn("descripcion " + descripcion);
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         String sqlQuery = "call PERFILES_PKG.EliminarPerfil(?)";
         Query query = em.createNativeQuery(sqlQuery);
         query.setParameter(1, descripcion);
         query.executeUpdate();
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaPerfiles.ejecutarPKGRecrearPerfil. " + e.toString());
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void clonarPantallas(EntityManager em, String nomPerfil) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         String sqlQuery = "call PERFILES_PKG.clonarPantallas(?)";
         Query query = em.createNativeQuery(sqlQuery);
         query.setParameter(1, nomPerfil);
         query.executeUpdate();
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaPerfiles.clonarPantallas. " + e.getMessage());
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void clonarPermisosObjetos(EntityManager em, String nomPerfil) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         String sqlQuery = "call PERFILES_PKG.clonarPermisosObjetos(?)";
         Query query = em.createNativeQuery(sqlQuery);
         query.setParameter(1, nomPerfil);
         query.executeUpdate();
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaPerfiles.clonarPermisosObjetos. " + e.getMessage());
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }
}
