package Persistencia;

import Entidades.VWActualesIBCS;
import InterfacePersistencia.PersistenciaVWActualesIBCSInterface;
import java.math.BigInteger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.Query;

@Stateless
public class PersistenciaVWActualesIBCS implements PersistenciaVWActualesIBCSInterface {

   private static Logger log = Logger.getLogger(PersistenciaVWActualesIBCS.class);

   @Override
   public VWActualesIBCS buscarIbcEmpleado(EntityManager em, BigInteger secuencia) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT vw FROM VWActualesIBCS vw WHERE vw.empleado.secuencia = :secuencia");
         query.setParameter("secuencia", secuencia);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         VWActualesIBCS actualIbc = (VWActualesIBCS) query.getSingleResult();
         return actualIbc;
      } catch (Exception e) {
         if (e.getMessage().contains("did not retrieve any entities")) {
            log.trace("Error: (PersistenciaVWActualesIBCS.buscarIbcEmpleado)" + e);
         } else {
            log.error("Error: (PersistenciaVWActualesIBCS.buscarIbcEmpleado) ", e);
         }
         return null;
      }
   }
}
