/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import InterfacePersistencia.PersistenciaVWActualesSueldosInterface;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.Query;
/**
 * Clase Stateless.<br> 
 * Clase encargada de realizar operaciones sobre la vista 'VWActualesSueldos'
 * de la base de datos.
 * @author betelgeuse
 */
@Stateless
public class PersistenciaVWActualesSueldos implements PersistenciaVWActualesSueldosInterface {

   private static Logger log = Logger.getLogger(PersistenciaVWActualesSueldos.class);

    @Override
    public BigDecimal buscarSueldoActivo(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT SUM(vw.valor) FROM VWActualesSueldos vw WHERE vw.empleado.secuencia=:secuencia");
            query.setParameter("secuencia", secuencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            BigDecimal vwActualesSueldosValor = (BigDecimal) query.getSingleResult();
            return vwActualesSueldosValor;
        } catch (Exception e) {
            log.error("PersistenciaVWActualesSueldos.buscarSueldoActivo():  ", e);
            return null;
        }
    }
}
