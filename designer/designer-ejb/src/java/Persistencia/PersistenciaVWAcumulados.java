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
//import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Clase Stateless.<br> 
 * Clase encargada de realizar operaciones sobre la vista 'VWAcumulados'
 * de la base de datos.
 * @author betelgeuse
 */
@Stateless
public class PersistenciaVWAcumulados implements PersistenciaVWAcumuladosInterface {
    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
/*    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
    private EntityManager em;
*/

    @Override
    public List<VWAcumulados> buscarAcumuladosPorEmpleado(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT vwa FROM VWAcumulados vwa WHERE vwa.empleado.secuencia = :secuenciaEmpl ORDER BY vwa.fechaPago DESC");
            query.setParameter("secuenciaEmpl", secuencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<VWAcumulados> VWAcumuladosPorEmpleado = query.getResultList();
            return VWAcumuladosPorEmpleado;
        } catch (Exception e) {
            System.out.println("Error en Persistencia VWAcumulados " + e.getMessage());
            return null;
        }
    }
}
