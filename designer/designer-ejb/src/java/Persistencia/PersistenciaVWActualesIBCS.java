package Persistencia;

import Entidades.VWActualesIBCS;
import InterfacePersistencia.PersistenciaVWActualesIBCSInterface;
import java.math.BigInteger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;

@Stateless
public class PersistenciaVWActualesIBCS implements PersistenciaVWActualesIBCSInterface {

    @Override
    public VWActualesIBCS buscarIbcEmpleado(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT vw FROM VWActualesIBCS vw WHERE vw.empleado.secuencia = :secuencia");
            query.setParameter("secuencia", secuencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            VWActualesIBCS actualIbc = (VWActualesIBCS) query.getSingleResult();
            return actualIbc;
        } catch (Exception e) {
            System.out.println("Error: (PersistenciaVWActualesIBCS.buscarIbcEmpleado)" + e.getMessage());
            return null;
        }
    }
}
