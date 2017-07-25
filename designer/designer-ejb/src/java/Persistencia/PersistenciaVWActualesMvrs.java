package Persistencia;

import InterfacePersistencia.PersistenciaVWActualesMvrsInterface;
import java.math.BigDecimal;
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
public class PersistenciaVWActualesMvrs implements PersistenciaVWActualesMvrsInterface {

   private static Logger log = Logger.getLogger(PersistenciaVWActualesMvrs.class);

    @Override
    public BigDecimal buscarActualMVR(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            Query query = em.createNativeQuery("SELECT sum(VALOR) FROM VWACTUALESMvrs a WHERE a.empleado = ?");
            query.setParameter(1, secuencia);
            BigDecimal valorMVR = (BigDecimal) query.getSingleResult();
            return valorMVR;
        } catch (Exception e) {
            log.error("Persistencia.PersistenciaVWActualesMvrs.buscarActualMVR()" + e.getMessage());
            return null;
        }
    }
}
