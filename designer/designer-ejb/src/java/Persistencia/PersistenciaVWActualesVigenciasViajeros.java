/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.VWActualesVigenciasViajeros;
import InterfacePersistencia.PersistenciaVWActualesVigenciasViajerosInterface;
import java.math.BigInteger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.Query;

/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la vista
 * 'VWActualesVigenciasViajeros' de la base de datos.
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaVWActualesVigenciasViajeros implements PersistenciaVWActualesVigenciasViajerosInterface {

   private static Logger log = Logger.getLogger(PersistenciaVWActualesVigenciasViajeros.class);

   public VWActualesVigenciasViajeros buscarTipoViajero(EntityManager em, BigInteger secuencia) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT vw FROM VWActualesVigenciasViajeros vw WHERE vw.empleado.secuencia=:secuencia");
         query.setParameter("secuencia", secuencia);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         VWActualesVigenciasViajeros vWActualesVigenciasViajeros = (VWActualesVigenciasViajeros) query.getSingleResult();
         return vWActualesVigenciasViajeros;
      } catch (Exception e) {
         if (e.getMessage().contains("did not retrieve any entities")) {
            log.trace("PersistenciaVWActualesVigenciasViajeros.buscarTipoViajero(): " + e);
         } else {
            log.warn("PersistenciaVWActualesVigenciasViajeros.buscarTipoViajero():  ", e);
         }
         return null;
      }
   }
}
