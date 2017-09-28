/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.VWActualesTiposContratos;
import InterfacePersistencia.PersistenciaVWActualesTiposContratosInterface;
import java.math.BigInteger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.Query;

/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la vista
 * 'VWActualesTiposCargos' de la base de datos.
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaVWActualesTiposCargos implements PersistenciaVWActualesTiposContratosInterface {

   private static Logger log = Logger.getLogger(PersistenciaVWActualesTiposCargos.class);

   /**
    * Atributo EntityManager. Representa la comunicación con la base de datos.
    */
   /*@PersistenceContext(unitName = "DesignerRHN-ejbPU")
    private EntityManager em;*/
   public VWActualesTiposContratos buscarTiposContratosEmpleado(EntityManager em, BigInteger secuencia) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT vw FROM VWActualesTiposContratos vw WHERE vw.empleado.secuencia=:secuencia");
         query.setParameter("secuencia", secuencia);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         VWActualesTiposContratos actualesTiposContratos = (VWActualesTiposContratos) query.getSingleResult();
         return actualesTiposContratos;
      } catch (Exception e) {
         if (e.getMessage().contains("did not retrieve any entities")) {
            log.trace("Error PersistenciaVWActualesTiposCargos.buscarTiposContratosEmpleado: " + e);
         } else {
            log.error("Error PersistenciaVWActualesTiposCargos.buscarTiposContratosEmpleado:  ", e);
         }
         return null;
      }
   }
}
