

package Persistencia;

import Entidades.EersFlujos;
import InterfacePersistencia.PersistenciaEersFlujosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author Administrador
 */
@Stateless
public class PersistenciaEersFlujos implements PersistenciaEersFlujosInterface{

    @Override
    public List<EersFlujos> buscarEersFlujosPorEersCabecera(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT e FROM EersFlujos e WHERE e.eercabecera.secuencia=:secuencia");
            query.setParameter("secuencia", secuencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<EersFlujos> eersCabeceras = query.getResultList();
            return eersCabeceras;
        } catch (Exception e) {
            System.out.println("Error buscarEersFlujosPorEersCabecera PersistenciaEersFlujos " + e.toString());
            return null;
        }
    }
}
