package Persistencia;

import Entidades.VWActualesMvrs;
import Entidades.VWActualesVigenciasViajeros;
import InterfacePersistencia.PersistenciaVWActualesMvrsInterface;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.util.Locale;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author -Felipphe-
 */
@Stateless
public class PersistenciaVWActualesMvrs implements PersistenciaVWActualesMvrsInterface {

    @Override
    public String buscarActualMVR(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));
            Query query = em.createNativeQuery("SELECT sum(VALOR) FROM VWACTUALESMvrs a WHERE a.empleado = ?");
            query.setParameter(1, secuencia);
            String actualMVR;
            BigDecimal valorMVR = (BigDecimal) query.getSingleResult();
            actualMVR = nf.format(valorMVR);
            return actualMVR;
        } catch (Exception e) {
            System.out.println("Error: (buscarActualMVR) \n" + e);
            return null;
        }
    }
}
