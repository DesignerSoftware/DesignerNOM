/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.VWActualesTiposContratos;
import InterfacePersistencia.PersistenciaVWActualesTiposContratosInterface;
import java.math.BigInteger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
/**
 * Clase Stateless.<br> 
 * Clase encargada de realizar operaciones sobre la vista 'VWActualesTiposCargos'
 * de la base de datos.
 * @author betelgeuse
 */
@Stateless
public class PersistenciaVWActualesTiposCargos implements PersistenciaVWActualesTiposContratosInterface {
    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
    /*@PersistenceContext(unitName = "DesignerRHN-ejbPU")
    private EntityManager em;*/

    public VWActualesTiposContratos buscarTiposContratosEmpleado(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT vw FROM VWActualesTiposContratos vw WHERE vw.empleado.secuencia=:secuencia");
            query.setParameter("secuencia", secuencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            VWActualesTiposContratos actualesTiposContratos = (VWActualesTiposContratos) query.getSingleResult();
            return actualesTiposContratos;
        } catch (Exception e) {
            System.out.println("Error PersistenciaVWActualesTiposCargos.buscarTiposContratosEmpleado: " + e.getMessage());
            return null;
        }
    }
}
