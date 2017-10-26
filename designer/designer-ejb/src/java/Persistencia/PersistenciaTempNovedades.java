/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.TempNovedades;
import InterfacePersistencia.PersistenciaTempNovedadesInterface;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

@Stateless
public class PersistenciaTempNovedades implements PersistenciaTempNovedadesInterface {

   private static Logger log = Logger.getLogger(PersistenciaTempNovedades.class);

   @Override
   public void crear(EntityManager em, TempNovedades tempNovedades) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(tempNovedades);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaTempNovedades.crear:  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void editar(EntityManager em, TempNovedades tempNovedades) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(tempNovedades);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaTempNovedades.editar:  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void borrar(EntityManager em, TempNovedades tempNovedades) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.remove(em.merge(tempNovedades));
         tx.commit();

      } catch (Exception e) {
         log.error("Error PersistenciaTempNovedades.borrar:  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void borrarRegistrosTempNovedades(EntityManager em, String usuarioBD) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         Query query = em.createQuery("DELETE FROM TempNovedades t WHERE t.usuariobd =:usuarioBD AND t.estado = 'N'");
         query.setParameter("usuarioBD", usuarioBD);
         //query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         query.executeUpdate();
         tx.commit();
      } catch (Exception e) {
         log.error("No se pudo borrar el registro (borrarRegistrosTempNovedades) :  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public List<TempNovedades> obtenerTempNovedades(EntityManager em, String usuarioBD) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT t FROM TempNovedades t "
                 + "WHERE t.usuariobd = :usuarioBD AND t.estado = 'N'");
         query.setParameter("usuarioBD", usuarioBD);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         List<TempNovedades> listTNovedades = query.getResultList();
         return listTNovedades;
      } catch (Exception e) {
         log.error("PersistenciaTempNovedades.obtenerTempNovedades():  ", e);
         return null;
      }
   }

   @Override
   public List<String> obtenerDocumentosSoporteCargados(EntityManager em, String usuarioBD) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT t.documentosoporte FROM TempNovedades t "
                 + "WHERE t.usuariobd = :usuarioBD AND t.estado = 'C'");
         query.setParameter("usuarioBD", usuarioBD);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         List<String> listDocumentosSoporte = query.getResultList();
         return listDocumentosSoporte;
      } catch (Exception e) {
         log.error("PersistenciaTempNovedades.obtenerDocumentosSoporteCargados(): ", e);
         return null;
      }
   }

   @Override
   public void cargarTempNovedades(EntityManager em, String fechaReporte, String nombreCortoFormula, String usarFormula) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         String sqlQuery = "call TEMPNOVEDADES_PKG.INSERTARNOVEDAD(To_date(?, 'dd/mm/yyyy'), ?, ?)";
         Query query = em.createNativeQuery(sqlQuery);
         query.setParameter(1, fechaReporte);
         query.setParameter(2, nombreCortoFormula);
         query.setParameter(3, usarFormula);
         query.executeUpdate();
         tx.commit();
      } catch (Exception e) {
         log.error("PersistenciaTempNovedades.cargarTempNovedades() :  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void reversarTempNovedades(EntityManager em, String usuarioBD, String documentoSoporte) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         Query query = em.createNativeQuery("DELETE FROM TempNovedades t WHERE t.usuariobd = ? "
                 + "AND t.estado = 'C' "
                 + "AND t.documentosoporte = ?");
         query.setParameter(1, usuarioBD);
         query.setParameter(2, documentoSoporte);
//         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         query.executeUpdate();
         tx.commit();
      } catch (Exception e) {
         log.error("PersistenciaTempNovedades.reversarTempNovedades():  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }
}
