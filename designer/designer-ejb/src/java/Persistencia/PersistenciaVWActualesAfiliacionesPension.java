/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.VWActualesAfiliacionesPension;
import InterfacePersistencia.PersistenciaVWActualesAfiliacionesPensionInterface;
import java.math.BigInteger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
/**
 * Clase Stateless.<br> 
 * Clase encargada de realizar operaciones sobre la vista 'VWActualesAfiliacionesPension'
 * de la base de datos.
 * @author betelgeuse
 */
@Stateless
public class PersistenciaVWActualesAfiliacionesPension implements PersistenciaVWActualesAfiliacionesPensionInterface {

    public VWActualesAfiliacionesPension buscarAfiliacionPension(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT vw FROM VWActualesAfiliacionesPension vw WHERE vw.empleado.secuencia=:secuencia");
            query.setParameter("secuencia", secuencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            VWActualesAfiliacionesPension vwActualesAfiliacionesPension = (VWActualesAfiliacionesPension) query.getSingleResult();
            return vwActualesAfiliacionesPension;
        } catch (Exception e) {
            System.out.println("Persistencia.PersistenciaVWActualesAfiliacionesPension.buscarAfiliacionPension()" + e.getMessage());
            return null;
        }
    }
}
