/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import InterfacePersistencia.PersistenciaRelacionesIncapacidadesInterface;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
/**
 * Clase Stateless. <br> 
 * Clase encargada de realizar operaciones sobre la tabla 'RelacionesIncapacidades'
 * de la base de datos.
 * @author betelgeuse
 */
@Stateless
public class PersistenciaRelacionesIncapacidades implements PersistenciaRelacionesIncapacidadesInterface {
    
    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
//    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
//    private EntityManager em;

    //Trae las relaciones en base al ausentismo seleccionado
    @Override
    public String relaciones(EntityManager em, BigInteger secuenciaAusentismo) {
        try {
            em.clear();
            String sqlQuery = ("SELECT COUNT(ri.ano||'.'||ri.mes) FROM RelacionesIncapacidades ri, Soausentismos so WHERE ri.soausentismo = so.secuencia AND so.secuencia = ?");
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuenciaAusentismo);
            BigDecimal resultadoConteo = (BigDecimal) query.getSingleResult();
            int resultado = resultadoConteo.intValueExact();
            if (resultado > 0) {
                sqlQuery = ("SELECT ri.ano||'.'||ri.mes FROM RelacionesIncapacidades ri, Soausentismos so WHERE ri.soausentismo = so.secuencia AND so.secuencia = ?");
                query = em.createNativeQuery(sqlQuery);
                query.setParameter(1, secuenciaAusentismo);
                String relacion = (String) query.getSingleResult();
                return relacion;
            } else {
                return null;
            }
        } catch (Exception e) {
            System.out.println("Error: ( Relaciones)" + e.toString());
            return null;
        }
    }
}
