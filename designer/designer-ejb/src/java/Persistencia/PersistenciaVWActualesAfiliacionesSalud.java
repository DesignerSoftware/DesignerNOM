/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.VWActualesAfiliacionesSalud;
import InterfacePersistencia.PersistenciaVWActualesAfiliacionesSaludInterface;
import java.math.BigInteger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
/**
 * Clase Stateless.<br> 
 * Clase encargada de realizar operaciones sobre la vista 'VWActualesAfiliacionesSalud'
 * de la base de datos.
 * @author betelgeuse
 */
@Stateless
public class PersistenciaVWActualesAfiliacionesSalud implements PersistenciaVWActualesAfiliacionesSaludInterface {
    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
/*    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
    private EntityManager em;
*/

    public VWActualesAfiliacionesSalud buscarAfiliacionSalud(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT vw FROM VWActualesAfiliacionesSalud vw WHERE vw.empleado.secuencia=:secuencia");
            query.setParameter("secuencia", secuencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            VWActualesAfiliacionesSalud vwActualesAfiliacionesSalud = (VWActualesAfiliacionesSalud) query.getSingleResult();
            return vwActualesAfiliacionesSalud;
        } catch (Exception e) {
            System.out.println("Persistencia.PersistenciaVWActualesAfiliacionesSalud.buscarAfiliacionSalud()" + e.getMessage());
            return null;
        }        
    }
}
