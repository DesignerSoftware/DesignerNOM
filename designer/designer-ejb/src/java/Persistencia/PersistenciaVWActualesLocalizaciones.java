/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.VWActualesLocalizaciones;
import InterfacePersistencia.PersistenciaVWActualesLocalizacionesInterface;
import java.math.BigInteger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
/**
 * Clase Stateless.<br> 
 * Clase encargada de realizar operaciones sobre la vista 'VWActualesLocalizaciones'
 * de la base de datos.
 * @author betelgeuse
 */
@Stateless
public class PersistenciaVWActualesLocalizaciones implements PersistenciaVWActualesLocalizacionesInterface {
    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
   /* @PersistenceContext(unitName = "DesignerRHN-ejbPU")
    private EntityManager em;*/

    public VWActualesLocalizaciones buscarLocalizacion(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT vw FROM VWActualesLocalizaciones vw WHERE vw.empleado.secuencia=:secuencia");
            query.setParameter("secuencia", secuencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            VWActualesLocalizaciones vwActualesLocalizaciones = (VWActualesLocalizaciones) query.getSingleResult();
            return vwActualesLocalizaciones;
        } catch (Exception e) {
        }
        VWActualesLocalizaciones vwActualesLocalizaciones = null;
        return vwActualesLocalizaciones;
    }
}
