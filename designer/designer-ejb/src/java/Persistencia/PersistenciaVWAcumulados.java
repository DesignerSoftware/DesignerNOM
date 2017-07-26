/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import InterfacePersistencia.PersistenciaVWAcumuladosInterface;
import Entidades.VWAcumulados;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.Query;

/**
 * Clase Stateless.<br> 
 * Clase encargada de realizar operaciones sobre la vista 'VWAcumulados'
 * de la base de datos.
 * @author betelgeuse
 */
@Stateless
public class PersistenciaVWAcumulados implements PersistenciaVWAcumuladosInterface {

   private static Logger log = Logger.getLogger(PersistenciaVWAcumulados.class);
   /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
    /*    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
    private EntityManager em;
     */
    @Override
    public List<VWAcumulados> buscarAcumuladosPorEmpleado(EntityManager em, BigInteger secEmpleado) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT vwa FROM VWAcumulados vwa WHERE vwa.empleado.secuencia = :secuenciaEmpl ORDER BY vwa.fechaPago DESC");
            query.setParameter("secuenciaEmpl", secEmpleado);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<VWAcumulados> VWAcumuladosPorEmpleado = query.getResultList();
            return VWAcumuladosPorEmpleado;
        } catch (Exception e) {
            log.error("Error en Persistencia VWAcumulados " + e.getMessage());
            return null;
        }
    }

    @Override
    public Long getTotalRegistros(EntityManager em, BigInteger secuencia) {
        Long count;
        try {
            em.clear();
            Query query = em.createQuery("SELECT count(vwa) FROM VWAcumulados vwa WHERE vwa.empleado.secuencia = :secuenciaEmpl ORDER BY vwa.fechaPago DESC");
            query.setParameter("secuenciaEmpl", secuencia);
            List lista = query.getResultList();
            count = (Long) lista.get(0);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            return count;
        } catch (Exception e) {
            log.error("Error en getTotalRegistros :" + e.getMessage());
            count = Long.valueOf(0);
            return count;
        }
    }
}
