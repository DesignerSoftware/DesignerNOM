/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.VWActualesPensiones;
import InterfacePersistencia.PersistenciaVWActualesPensionesInterface;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
/**
 * Clase Stateless.<br> 
 * Clase encargada de realizar operaciones sobre la vista 'VWActualesPensiones'
 * de la base de datos.
 * @author betelgeuse
 */
@Stateless
public class PersistenciaVWActualesPensiones implements  PersistenciaVWActualesPensionesInterface{

    @Override
    public BigDecimal buscarSueldoPensionado(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT vw FROM VWActualesPensiones vw WHERE vw.empleado.secuencia=:secuencia");
            query.setParameter("secuencia", secuencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            VWActualesPensiones vWActualesPensiones = (VWActualesPensiones) query.getSingleResult();          
            return vWActualesPensiones.getValor();
        } catch (Exception e) {
            System.out.println("Persistencia.PersistenciaVWActualesPensiones.buscarSueldoPensionado()" + e.getMessage());
            return null;
        }
    }
}
