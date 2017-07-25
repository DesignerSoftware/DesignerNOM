/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.VWActualesJornadas;
import InterfacePersistencia.PersistenciaVWActualesJornadasInterface;
import java.math.BigInteger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.Query;
/**
 * Clase Stateless.<br> 
 * Clase encargada de realizar operaciones sobre la vista 'VWActualesJornadas'
 * de la base de datos.
 * @author betelgeuse
 */
@Stateless
public class PersistenciaVWActualesJornadas implements PersistenciaVWActualesJornadasInterface {

   private static Logger log = Logger.getLogger(PersistenciaVWActualesJornadas.class);


    public VWActualesJornadas buscarJornada(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT vw FROM VWActualesJornadas vw WHERE vw.empleado.secuencia=:secuencia");
            query.setParameter("secuencia", secuencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            VWActualesJornadas actualesJornadas = (VWActualesJornadas) query.getSingleResult();
            return actualesJornadas;
        } catch (Exception e) {
            log.error("Persistencia.PersistenciaVWActualesJornadas.buscarJornada()" + e.getMessage());
            return null;
        }
    }
}
