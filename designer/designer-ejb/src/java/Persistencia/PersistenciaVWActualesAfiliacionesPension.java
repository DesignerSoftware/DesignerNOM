/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.VWActualesAfiliacionesPension;
import InterfacePersistencia.PersistenciaVWActualesAfiliacionesPensionInterface;
import java.math.BigInteger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.Query;

/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la vista
 * 'VWActualesAfiliacionesPension' de la base de datos.
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaVWActualesAfiliacionesPension implements PersistenciaVWActualesAfiliacionesPensionInterface {

   private static Logger log = Logger.getLogger(PersistenciaVWActualesAfiliacionesPension.class);

   public VWActualesAfiliacionesPension buscarAfiliacionPension(EntityManager em, BigInteger secuencia) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT vw FROM VWActualesAfiliacionesPension vw WHERE vw.empleado.secuencia=:secuencia");
         query.setParameter("secuencia", secuencia);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         VWActualesAfiliacionesPension vwActualesAfiliacionesPension = (VWActualesAfiliacionesPension) query.getSingleResult();
         return vwActualesAfiliacionesPension;
      } catch (Exception e) {
         if (e.getMessage().contains("did not retrieve any entities")) {
            log.trace("PersistenciaVWActualesAfiliacionesPension.buscarAfiliacionPension(): " + e);
         } else {
            log.error("PersistenciaVWActualesAfiliacionesPension.buscarAfiliacionPension():  ", e);
         }
         return null;
      }
   }
}
