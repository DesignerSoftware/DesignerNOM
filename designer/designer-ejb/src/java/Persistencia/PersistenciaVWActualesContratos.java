/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.VWActualesContratos;
import InterfacePersistencia.PersistenciaVWActualesContratosInterface;
import java.math.BigInteger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.Query;

/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la vista 'VWActualesContratos'
 * de la base de datos.
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaVWActualesContratos implements PersistenciaVWActualesContratosInterface {

   private static Logger log = Logger.getLogger(PersistenciaVWActualesContratos.class);

   public VWActualesContratos buscarContrato(EntityManager em, BigInteger secuencia) {

      try {
         em.clear();
         Query query = em.createQuery("SELECT vw FROM VWActualesContratos vw WHERE vw.empleado.secuencia=:secuencia");
         query.setParameter("secuencia", secuencia);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         VWActualesContratos actualesContratos = (VWActualesContratos) query.getSingleResult();
         return actualesContratos;
      } catch (Exception e) {
         if (e.getMessage().contains("did not retrieve any entities")) {
            log.error("Error: (PersistenciaVWActualesContratos.buscarContrato)" + e);
         } else {
            log.error("Error: (PersistenciaVWActualesContratos.buscarContrato) ", e);
         }
         return null;
      }
   }
}
