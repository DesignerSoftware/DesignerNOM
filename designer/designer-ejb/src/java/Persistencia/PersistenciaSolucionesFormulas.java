/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.SolucionesFormulas;
import InterfacePersistencia.PersistenciaSolucionesFormulasInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la tabla 'SolucionesFormulas'
 * de la base de datos.
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaSolucionesFormulas implements PersistenciaSolucionesFormulasInterface {

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
//    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
//    private EntityManager em;

    @Override
    public int validarNovedadesNoLiquidadas(EntityManager em, BigInteger secNovedad) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT COUNT(sf) FROM SolucionesFormulas sf WHERE sf.novedad.secuencia = :secNovedad");
            query.setParameter("secNovedad", secNovedad);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            Long resultado = (Long) query.getSingleResult();
            if (resultado > 0) {
                return 1;
            }
            return 0;
        } catch (Exception e) {
            System.out.println("Exepcion: (validarNovedadesNoLiquidadas) " + e);
            return 1;
        }
    }

    @Override
    public List<SolucionesFormulas> listaSolucionesFormulasParaEmpleadoYNovedad(EntityManager em, BigInteger secEmpleado, BigInteger secNovedad) {
        try {
            em.clear();
            List<SolucionesFormulas> lista = null;
            if (secNovedad == null) {
                Query query = em.createQuery("SELECT sf FROM SolucionesFormulas sf WHERE sf.solucionnodo.empleado.secuencia =:secEmpleado");
                query.setParameter("secEmpleado", secEmpleado);
                query.setHint("javax.persistence.cache.storeMode", "REFRESH");
                lista = query.getResultList();
            } else {
                Query query = em.createQuery("SELECT sf FROM SolucionesFormulas sf WHERE sf.solucionnodo.empleado.secuencia =:secEmpleado AND sf.novedad.secuencia =:secNovedad");
                query.setParameter("secEmpleado", secEmpleado);
                query.setParameter("secNovedad", secNovedad);
                query.setHint("javax.persistence.cache.storeMode", "REFRESH");
                lista = query.getResultList();
            }
            return lista;
        } catch (Exception e) {
            System.out.println("Error listaSolucionesFormulasParaEmpleadoYNovedad PersistenciaSolucionhesFormulas : " + e.toString());
            return null;
        }
    }
}
