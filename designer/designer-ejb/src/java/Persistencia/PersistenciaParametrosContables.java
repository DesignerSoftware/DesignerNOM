package Persistencia;

import Entidades.ParametrosContables;
import InterfacePersistencia.PersistenciaParametrosContablesInterface;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 *
 * @author Administrador
 */
@Stateless
public class PersistenciaParametrosContables implements PersistenciaParametrosContablesInterface { 

    @Override
    public void crear(EntityManager em, ParametrosContables parametrosContables) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(parametrosContables);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaParametrosContables.crear: " + e.toString());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, ParametrosContables parametrosContables) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(parametrosContables);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaParametrosContables.editar: " + e.toString());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, ParametrosContables parametrosContables) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(parametrosContables));
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaParametrosContables.borrar: " + e.toString());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }
 
    @Override 
    public List<ParametrosContables> buscarParametrosContablesUsuarioBD(EntityManager em, String usuarioBD) {
        try {
            Query query = em.createQuery("SELECT p FROM ParametrosContables p WHERE p.usuario =:usuarioBD");
            query.setParameter("usuarioBD", usuarioBD);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<ParametrosContables> parametro =  query.getResultList();
            return parametro;
        } catch (Exception e) {
            System.out.println("Error PersistenciaParametrosContables.buscarParametroContable: " + e.toString());
            return null;
        }
    }
}
