/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.VWActualesContratos;
import InterfacePersistencia.PersistenciaVWActualesContratosInterface;
import java.math.BigInteger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
/**
 * Clase Stateless.<br> 
 * Clase encargada de realizar operaciones sobre la vista 'VWActualesContratos'
 * de la base de datos.
 * @author betelgeuse
 */
@Stateless
public class PersistenciaVWActualesContratos implements PersistenciaVWActualesContratosInterface {
    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
/*    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
    private EntityManager em;
*/

    public VWActualesContratos buscarContrato(EntityManager em, BigInteger secuencia) {

        try {
            em.clear();
            Query query = em.createQuery("SELECT vw FROM VWActualesContratos vw WHERE vw.empleado.secuencia=:secuencia");
            query.setParameter("secuencia", secuencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            VWActualesContratos actualesContratos = (VWActualesContratos) query.getSingleResult();
            return actualesContratos;
        } catch (Exception e) {
            System.out.println("Error: (PersistenciaVWActualesContratos.buscarContrato)" + e);
            return null;
        }
    }
}
