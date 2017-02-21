package Persistencia;

import Entidades.Perfiles;
import InterfacePersistencia.PersistenciaPerfilesInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
//import javax.persistence.Query;

/**
 *
 * @author Administrador
 */
@Stateless
public class PersistenciaPerfiles implements PersistenciaPerfilesInterface {

    @Override
    public Perfiles consultarPerfil(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            return em.find(Perfiles.class, secuencia);
        } catch (Exception e) {
            System.out.println("Persistencia.PersistenciaPerfiles.consultarPerfil()" + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Perfiles> consultarPerfiles(EntityManager em) {
        try{
        em.clear();
        javax.persistence.criteria.CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Perfiles.class));
        return em.createQuery(cq).getResultList();
        }catch(Exception e){
            System.out.println("Persistencia.PersistenciaPerfiles.consultarPerfiles()" + e.getMessage());
            return null;
        }
    }
}
