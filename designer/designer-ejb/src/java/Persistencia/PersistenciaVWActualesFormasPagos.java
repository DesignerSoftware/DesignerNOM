/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.VWActualesFormasPagos;
import InterfacePersistencia.PersistenciaVWActualesFormasPagosInterface;
import java.math.BigInteger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
/**
 * Clase Stateless.<br> 
 * Clase encargada de realizar operaciones sobre la vista 'VWActualesFormasPagos'
 * de la base de datos.
 * @author betelgeuse
 */
@Stateless
public class PersistenciaVWActualesFormasPagos implements PersistenciaVWActualesFormasPagosInterface{

    public VWActualesFormasPagos buscarFormaPago(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT vw FROM VWActualesFormasPagos vw WHERE vw.empleado.secuencia=:secuencia");
            query.setParameter("secuencia", secuencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            VWActualesFormasPagos actualesFormasPagos = (VWActualesFormasPagos) query.getSingleResult();
            return actualesFormasPagos;
        } catch (Exception e) {
            System.out.println("Persistencia.PersistenciaVWActualesFormasPagos.buscarFormaPago()" + e.getMessage());
            return null;
        }
    }
}
