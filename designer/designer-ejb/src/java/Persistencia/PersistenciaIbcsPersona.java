package Persistencia;

import Entidades.IbcsPersona;
import InterfacePersistencia.PersistenciaIbcsPersonaInterface;
import java.math.BigInteger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.Query;

/**
 *
 * @author -Felipphe-
 */
@Stateless
public class PersistenciaIbcsPersona implements PersistenciaIbcsPersonaInterface {

   private static Logger log = Logger.getLogger(PersistenciaIbcsPersona.class);

    @Override
    public IbcsPersona buscarIbcPersona(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT i FROM IbcsPersona i WHERE i.persona.secuencia = :secuencia");
            query.setParameter("secuencia", secuencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            IbcsPersona ibcPersona = (IbcsPersona) query.getSingleResult();
            return ibcPersona;
        } catch (Exception e) {
            log.error("Error: (PersistenciaIbcsPersona.buscarIbcPersona) ", e);
            return null;
        }
    }
}
