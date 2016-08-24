/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.Familiares;
import InterfacePersistencia.PersistenciaFamiliaresInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
/**
 * Clase Stateless. <br>
 * Clase encargada de realizar operaciones sobre la tabla 'Familiares'
 * de la base de datos.
 * @author betelgeuse
 */
@Stateless
public class PersistenciaFamiliares implements PersistenciaFamiliaresInterface{
    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos
     */
    /*@PersistenceContext(unitName = "DesignerRHN-ejbPU")
    private EntityManager em;*/

    @Override
    public List<Familiares> familiaresPersona(EntityManager em,BigInteger secuenciaPersona) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT COUNT(f) FROM Familiares f WHERE f.persona.secuencia = :secuenciaPersona");
            query.setParameter("secuenciaPersona", secuenciaPersona);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            Long resultado = (Long) query.getSingleResult();
            if (resultado > 0) {
                Query queryFinal = em.createQuery("SELECT f FROM Familiares f WHERE f.persona.secuencia = :secuenciaPersona");
                queryFinal.setParameter("secuenciaPersona", secuenciaPersona);
                query.setHint("javax.persistence.cache.storeMode", "REFRESH");
                List<Familiares> listFamiliares = queryFinal.getResultList();
                return listFamiliares;
            }
            return null;
        } catch (Exception e) {
            System.out.println("Error PersistenciaFamiliares.familiaresPersona" + e);
            return null;
        }
    }
}
