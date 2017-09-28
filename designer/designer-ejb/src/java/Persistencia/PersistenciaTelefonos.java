/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.Telefonos;
import InterfacePersistencia.PersistenciaTelefonosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;

@Stateless
public class PersistenciaTelefonos implements PersistenciaTelefonosInterface {

   private static Logger log = Logger.getLogger(PersistenciaTelefonos.class);

   @Override
   public boolean crear(EntityManager em, Telefonos telefonos) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.persist(telefonos);
         tx.commit();
         return true;
      } catch (Exception e) {
         log.error("PersistenciaTelefonos.crear():  ", e);
         e.printStackTrace();
         if (tx.isActive()) {
            tx.rollback();
         }
         return false;
      }
   }

   @Override
   public void editar(EntityManager em, Telefonos telefonos) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(telefonos);
         tx.commit();
      } catch (Exception e) {
         log.error("PersistenciaTelefonos.editar():  ", e);
         e.printStackTrace();
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void borrar(EntityManager em, Telefonos telefonos) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.remove(em.merge(telefonos));
         tx.commit();
      } catch (Exception e) {
         log.error("PersistenciaTelefonos.borrar():  ", e);
         e.printStackTrace();
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public Telefonos buscarTelefono(EntityManager em, BigInteger secuencia) {
      try {
         em.clear();
         return em.find(Telefonos.class, secuencia);
      } catch (Exception e) {
         log.error("PersistenciaTelefonos.buscarTelefono():  ", e);
         e.printStackTrace();
         return null;
      }
   }

   @Override
   public List<Telefonos> buscarTelefonos(EntityManager em) {
      try {
         em.clear();
         CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
         cq.select(cq.from(Telefonos.class));
         return em.createQuery(cq).getResultList();
      } catch (Exception e) {
         log.error("PersistenciaTelefonos.buscarTelefonos():  ", e);
         return null;
      }
   }

   @Override
   public List<Telefonos> telefonosPersona(EntityManager em, BigInteger secuenciaPersona) {
      try {
         em.clear();
         String consulta = "SELECT t "
                 + "FROM Telefonos t "
                 + "WHERE t.persona.secuencia = :secuenciaPersona "
                 + "ORDER BY t.fechavigencia DESC";
         Query query = em.createQuery(consulta);
         query.setParameter("secuenciaPersona", secuenciaPersona);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         List<Telefonos> listaTelefonos = query.getResultList();
         return listaTelefonos;
      } catch (Exception e) {
         log.error(this.getClass().getName() + "telefonosPersona():  ", e);
         e.printStackTrace();
         return null;
      }
   }

   @Override
   public Telefonos telefonoActual(EntityManager em, BigInteger secuenciaPersona) {
      try {
         em.clear();
         String sql = "SELECT * FROM TELEFONOS WHERE FECHAVIGENCIA =(SELECT MAX(FECHAVIGENCIA) FROM TELEFONOS WHERE PERSONA = ? )";
         Query query = em.createNativeQuery(sql, Telefonos.class);
         query.setParameter(1, secuenciaPersona);
         Telefonos telefono = (Telefonos) query.getSingleResult();
         return telefono;
      } catch (Exception e) {
         log.error("Error en direccionActualPersona :  ", e);
         return null;
      }
   }

   @Override
   public String consultarUltimoTelefono(EntityManager em, BigInteger secuenciaPersona) {
      String telefono;
      try {
         em.clear();
         String sql = "SELECT substr(B.NOMBRE || '   ' || A.NUMEROTELEFONO,1,25) \n"
                 + "   FROM  Telefonos A, TIPOSTelefonos B \n"
                 + "   WHERE A.PERSONA = ? \n"
                 + "   AND   A.TIPOTelefono = B.SECUENCIA \n"
                 + "   AND A.secuencia = (select max(T.secuencia) from telefonos T where T.persona=A.PERSONA)";
         Query query = em.createNativeQuery(sql);
         query.setParameter(1, secuenciaPersona);
         telefono = (String) query.getSingleResult();
         return telefono;
      } catch (Exception e) {
         if (e.getMessage().contains("did not retrieve any entities")) {
            log.trace("PersistenciaTelefonos.consultarUltimoTelefono(): " + e);
         } else {
            log.error("PersistenciaTelefonos.consultarUltimoTelefono():  ", e);
         }
         return "";
      }
   }
}
