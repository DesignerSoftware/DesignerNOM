

package Persistencia;

import Entidades.EersFlujos;
import InterfacePersistencia.PersistenciaEersFlujosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.Query;

/**
 *
 * @author Administrador
 */
@Stateless
public class PersistenciaEersFlujos implements PersistenciaEersFlujosInterface {

   private static Logger log = Logger.getLogger(PersistenciaEersFlujos.class);

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
            log.error("Error buscarEersFlujosPorEersCabecera PersistenciaEersFlujos  ", e);
            return null;
        }
    }
}
