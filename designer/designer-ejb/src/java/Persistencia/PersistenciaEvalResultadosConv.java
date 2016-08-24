/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.EvalResultadosConv;
import InterfacePersistencia.PersistenciaEvalResultadosConvInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
/**
 * Clase Stateless. <br>
 * Clase encargada de realizar operaciones sobre la tabla 'EvalResultadosConv'
 * de la base de datos.
 * @author betelgeuse
 */
@Stateless
public class PersistenciaEvalResultadosConv implements PersistenciaEvalResultadosConvInterface{
    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
    /*@PersistenceContext(unitName = "DesignerRHN-ejbPU")
    private EntityManager em;*/

    @Override
    public List<EvalResultadosConv> pruebasAplicadasPersona(EntityManager em,BigInteger secuenciaEmpleado) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT COUNT(er) FROM EvalResultadosConv er WHERE er.empleado.secuencia = :secuenciaEmpleado");
            query.setParameter("secuenciaEmpleado", secuenciaEmpleado);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            Long resultado = (Long) query.getSingleResult();
            if (resultado > 0) {
                Query queryFinal = em.createQuery("SELECT er FROM EvalResultadosConv er WHERE er.empleado.secuencia = :secuenciaEmpleado and er.fechaperiododesde = (SELECT MAX(ere.fechaperiododesde) FROM EvalResultadosConv ere WHERE ere.empleado.secuencia = :secuenciaEmpleado)");
                queryFinal.setParameter("secuenciaEmpleado", secuenciaEmpleado);
                query.setHint("javax.persistence.cache.storeMode", "REFRESH");
                List<EvalResultadosConv> listaPruebasAplicadas = queryFinal.getResultList();
                return listaPruebasAplicadas;
            }
            return null;
        } catch (Exception e) {
            System.out.println("Error PersistenciaEvalResultadosConv.pruebasAplicadasPersona" + e);
            return null;
        }
    }
}
