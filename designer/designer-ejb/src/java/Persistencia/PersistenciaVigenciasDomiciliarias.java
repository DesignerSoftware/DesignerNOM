/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.VigenciasDomiciliarias;
import InterfacePersistencia.PersistenciaVigenciasDomiciliariasInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
/**
 * Clase Stateless.<br> 
 * Clase encargada de realizar operaciones sobre la tabla 'VigenciasDomiciliarias'
 * de la base de datos.
 * @author betelgeuse
 */
@Stateless
public class PersistenciaVigenciasDomiciliarias implements PersistenciaVigenciasDomiciliariasInterface{
    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
/*    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
    private EntityManager em;
*/
    
    @Override
    public List<VigenciasDomiciliarias> visitasDomiciliariasPersona(EntityManager em, BigInteger secuenciaPersona) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT COUNT(vd) FROM VigenciasDomiciliarias vd WHERE vd.persona.secuencia = :secuenciaPersona");
            query.setParameter("secuenciaPersona", secuenciaPersona);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            Long resultado = (Long) query.getSingleResult();
            if (resultado > 0) {
                Query queryFinal = em.createQuery("SELECT vd FROM VigenciasDomiciliarias vd WHERE vd.persona.secuencia = :secuenciaPersona and vd.fecha = (SELECT MAX(vdo.fecha) FROM VigenciasDomiciliarias vdo WHERE vdo.persona.secuencia = :secuenciaPersona)");
                queryFinal.setParameter("secuenciaPersona", secuenciaPersona);
                queryFinal.setHint("javax.persistence.cache.storeMode", "REFRESH");
                List<VigenciasDomiciliarias> listaVigenciasDomiciliarias = queryFinal.getResultList();
                return listaVigenciasDomiciliarias;
            }
            return null;
        } catch (Exception e) {
            System.out.println("Error PersistenciaVigenciasDomiciliarias.visitasDomiciliariasPersona" + e);
            return null;
        }
    }
}
