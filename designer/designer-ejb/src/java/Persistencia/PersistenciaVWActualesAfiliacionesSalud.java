/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.VWActualesAfiliacionesSalud;
import InterfacePersistencia.PersistenciaVWActualesAfiliacionesSaludInterface;
import java.math.BigInteger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.Query;

/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la vista
 * 'VWActualesAfiliacionesSalud' de la base de datos.
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaVWActualesAfiliacionesSalud implements PersistenciaVWActualesAfiliacionesSaludInterface {

   private static Logger log = Logger.getLogger(PersistenciaVWActualesAfiliacionesSalud.class);

   /**
    * Atributo EntityManager. Representa la comunicación con la base de datos.
    */
   /*    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
    private EntityManager em;
    */
   public VWActualesAfiliacionesSalud buscarAfiliacionSalud(EntityManager em, BigInteger secuencia) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT vw FROM VWActualesAfiliacionesSalud vw WHERE vw.empleado.secuencia=:secuencia");
         query.setParameter("secuencia", secuencia);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         VWActualesAfiliacionesSalud vwActualesAfiliacionesSalud = (VWActualesAfiliacionesSalud) query.getSingleResult();
         return vwActualesAfiliacionesSalud;
      } catch (Exception e) {
         if (e.getMessage().contains("did not retrieve any entities")) {
            log.trace("PersistenciaVWActualesAfiliacionesSalud.buscarAfiliacionSalud(): " + e);
         } else {
            log.error("PersistenciaVWActualesAfiliacionesSalud.buscarAfiliacionSalud():  ", e);
         }
         return null;
      }
   }
}
