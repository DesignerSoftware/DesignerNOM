package Persistencia;

import Entidades.VWActualesSets;
import InterfacePersistencia.PersistenciaVWActualesSetsInterface;
import java.math.BigInteger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;

@Stateless
public class PersistenciaVWActualesSets implements PersistenciaVWActualesSetsInterface{

    @Override
    public VWActualesSets buscarSetEmpleado(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT vw FROM VWActualesSets vw WHERE vw.empleado.secuencia = :secuencia");
            query.setParameter("secuencia", secuencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            VWActualesSets actualSet = (VWActualesSets) query.getSingleResult();
            return actualSet;
        } catch (Exception e) {
            System.out.println("Error: (PersistenciaVWActualesSets.buscarSetEmpleado)" + e.getMessage());
            return null;
        }
    }
}
