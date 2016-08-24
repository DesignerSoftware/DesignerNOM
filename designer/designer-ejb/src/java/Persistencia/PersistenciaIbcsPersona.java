package Persistencia;

import Entidades.IbcsPersona;
import InterfacePersistencia.PersistenciaIbcsPersonaInterface;
import java.math.BigInteger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author -Felipphe-
 */
@Stateless
public class PersistenciaIbcsPersona implements PersistenciaIbcsPersonaInterface {

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
            System.out.println("Error: (PersistenciaIbcsPersona.buscarIbcPersona)" + e);
            return null;
        }
    }
}
