package Persistencia;

import Entidades.EersDetalles;
import InterfacePersistencia.PersistenciaEersDetallesInterface;
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
public class PersistenciaEersDetalles implements PersistenciaEersDetallesInterface{

    @Override
    public List<EersDetalles> buscarEersDetallesPorEersCabecera(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT e FROM EersDetalles e WHERE e.eercabecera.secuencia=:secuencia");
            query.setParameter("secuencia", secuencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<EersDetalles> eersCabeceras = query.getResultList();
            return eersCabeceras;
        } catch (Exception e) {
            System.out.println("Error buscarEersDetallesPorEersCabecera PersistenciaEersDetalles " + e.toString());
            return null;
        }
    }
}
