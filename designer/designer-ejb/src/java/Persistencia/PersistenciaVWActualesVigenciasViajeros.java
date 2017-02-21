/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.VWActualesVigenciasViajeros;
import InterfacePersistencia.PersistenciaVWActualesVigenciasViajerosInterface;
import java.math.BigInteger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
/**
 * Clase Stateless.<br> 
 * Clase encargada de realizar operaciones sobre la vista 'VWActualesVigenciasViajeros'
 * de la base de datos.
 * @author betelgeuse
 */
@Stateless
public class PersistenciaVWActualesVigenciasViajeros implements PersistenciaVWActualesVigenciasViajerosInterface{
   
    public VWActualesVigenciasViajeros buscarTipoViajero(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT vw FROM VWActualesVigenciasViajeros vw WHERE vw.empleado.secuencia=:secuencia");
            query.setParameter("secuencia", secuencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            VWActualesVigenciasViajeros vWActualesVigenciasViajeros = (VWActualesVigenciasViajeros) query.getSingleResult();
            return vWActualesVigenciasViajeros;
        } catch (Exception e) {
            System.out.println("Persistencia.PersistenciaVWActualesVigenciasViajeros.buscarTipoViajero()" + e.getMessage());
            return null;
        }
    }
}
