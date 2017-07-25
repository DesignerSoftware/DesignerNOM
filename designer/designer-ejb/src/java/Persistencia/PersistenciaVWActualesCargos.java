/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.VWActualesCargos;
import InterfacePersistencia.PersistenciaVWActualesCargosInterface;
import java.math.BigInteger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.Query;

/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la vista 'VWActualesCargos' de
 * la base de datos.
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaVWActualesCargos implements PersistenciaVWActualesCargosInterface {

   private static Logger log = Logger.getLogger(PersistenciaVWActualesCargos.class);

    @Override
    public VWActualesCargos buscarCargoEmpleado(EntityManager entity, BigInteger secuencia) {
        try {
            entity.clear();
            Query query = entity.createQuery("SELECT vw FROM VWActualesCargos vw WHERE vw.empleado.secuencia=:secuencia");
            query.setParameter("secuencia", secuencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            VWActualesCargos vwActualesCargos = (VWActualesCargos) query.getSingleResult();
            return vwActualesCargos;
        } catch (Exception e) {
            log.error("Error: PersistenciaVWActualesCargos.buscarCargoEmpleado " + e.getMessage());
            return null;
        }
    }

    @Override  
    public Long conteoCodigosEmpleados(EntityManager em, BigInteger secEstructura) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT COUNT (em.codigoempleado) FROM VWActualesCargos vc, Empleados em WHERE vc.empleado.secuencia = em.secuencia AND vc.estructura.secuencia = :secEstructura AND EXISTS (SELECT vt FROM VWActualesTiposTrabajadores vt, TiposTrabajadores tt WHERE vt.empleado.secuencia = em.secuencia AND vt.tipoTrabajador.secuencia  = tt.secuencia AND tt.tipo = 'ACTIVO')");
            query.setParameter("secEstructura", secEstructura);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            Long conteo = (Long) query.getSingleResult();
            return conteo;
        } catch (Exception e) {
            log.error("Error conteoCodigosEmpleados PersistenciaVWActualesCargos: " + e.getMessage());
            return null;
        }
    }

}
