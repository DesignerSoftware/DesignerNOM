package Persistencia;

import Entidades.VWActualesSets;
import InterfacePersistencia.PersistenciaVWActualesSetsInterface;
import java.math.BigInteger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.Query;

@Stateless
public class PersistenciaVWActualesSets implements PersistenciaVWActualesSetsInterface {

   private static Logger log = Logger.getLogger(PersistenciaVWActualesSets.class);

   @Override
   public VWActualesSets buscarSetEmpleado(EntityManager em, BigInteger secuencia) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT vw FROM VWActualesSets vw WHERE vw.empleado.secuencia = :secuencia");
         query.setParameter("secuencia", secuencia);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         VWActualesSets actualSet = (VWActualesSets) query.getSingleResult();
         return actualSet;
      } catch (Exception e) {
         if (e.getMessage().contains("did not retrieve any entities")) {
            log.error("Error: (PersistenciaVWActualesSets.buscarSetEmpleado)" + e);
         } else {
            log.error("Error: (PersistenciaVWActualesSets.buscarSetEmpleado) ", e);
         }
         return null;
      }
   }
}
