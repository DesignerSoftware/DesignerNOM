/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.VWActualesReformasLaborales;
import InterfacePersistencia.PersistenciaVWActualesReformasLaboralesInterface;
import java.math.BigInteger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.Query;


@Stateless
public class PersistenciaVWActualesReformasLaborales implements PersistenciaVWActualesReformasLaboralesInterface {

   private static Logger log = Logger.getLogger(PersistenciaVWActualesReformasLaborales.class);
    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
    /*@PersistenceContext(unitName = "DesignerRHN-ejbPU")
    private EntityManager em;*/

    public VWActualesReformasLaborales buscarReformaLaboral(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT vw FROM VWActualesReformasLaborales vw WHERE vw.empleado.secuencia=:secuencia");
            query.setParameter("secuencia", secuencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            VWActualesReformasLaborales vWActualesReformasLaborales = (VWActualesReformasLaborales) query.getSingleResult();
            return vWActualesReformasLaborales;
        } catch (Exception e) {
            log.error("Persistencia.PersistenciaVWActualesReformasLaborales.buscarReformaLaboral()" + e.getMessage());
            return null;
        }
    }
}
