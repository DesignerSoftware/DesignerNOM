/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.VWActualesUbicaciones;
import InterfacePersistencia.PersistenciaVWActualesUbicacionesInterface;
import java.math.BigInteger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.Query;

/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la vista
 * 'VWActualesUbicaciones' de la base de datos.
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaVWActualesUbicaciones implements PersistenciaVWActualesUbicacionesInterface {

   private static Logger log = Logger.getLogger(PersistenciaVWActualesUbicaciones.class);

   /**
    * Atributo EntityManager. Representa la comunicación con la base de datos.
    */
//    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
//    private EntityManager em;

   public VWActualesUbicaciones buscarUbicacion(EntityManager em, BigInteger secuencia) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT vw FROM VWActualesUbicaciones vw WHERE vw.empleado.secuencia=:secuencia");
         query.setParameter("secuencia", secuencia);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         VWActualesUbicaciones vWActualesUbicaciones = (VWActualesUbicaciones) query.getSingleResult();
         return vWActualesUbicaciones;
      } catch (Exception e) {
         if (e.getMessage().contains("did not retrieve any entities")) {
            log.error("PersistenciaVWActualesUbicaciones.buscarUbicacion(): " + e);
         } else {
            log.error("PersistenciaVWActualesUbicaciones.buscarUbicacion():  ", e);
         }
         return null;
      }
   }
}
