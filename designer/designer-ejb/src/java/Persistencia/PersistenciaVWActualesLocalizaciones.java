/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.VWActualesLocalizaciones;
import InterfacePersistencia.PersistenciaVWActualesLocalizacionesInterface;
import java.math.BigInteger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;
/**
 * Clase Stateless.<br> 
 * Clase encargada de realizar operaciones sobre la vista 'VWActualesLocalizaciones'
 * de la base de datos.
 * @author betelgeuse
 */
@Stateless
public class PersistenciaVWActualesLocalizaciones implements PersistenciaVWActualesLocalizacionesInterface {

    public VWActualesLocalizaciones buscarLocalizacion(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT vw FROM VWActualesLocalizaciones vw WHERE vw.empleado.secuencia=:secuencia");
            query.setParameter("secuencia", secuencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            VWActualesLocalizaciones vwActualesLocalizaciones = (VWActualesLocalizaciones) query.getSingleResult();
            return vwActualesLocalizaciones;
        } catch (Exception e) {
            System.out.println("Persistencia.PersistenciaVWActualesLocalizaciones.buscarLocalizacion()" + e.getMessage());
            return null;
        }
    }
}
