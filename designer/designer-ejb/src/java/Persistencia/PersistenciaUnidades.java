/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.Unidades;
import InterfacePersistencia.PersistenciaUnidadesInterface;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class PersistenciaUnidades implements PersistenciaUnidadesInterface {


    @Override
    public void crear(EntityManager em, Unidades unidad) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(unidad);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaUnidades.crear: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, Unidades unidad) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(unidad);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaUnidades.editar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, Unidades unidad) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(unidad));
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaUnidades.borrar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public List<Unidades> consultarUnidades(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT c FROM Unidades c ORDER BY c.codigo ASC");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<Unidades> listaUnidades = query.getResultList();
            return listaUnidades;
        } catch (Exception e) {
            System.out.println("Error consultarUnidades PersistenciaUnidades : " + e.getMessage());
            return null;
        }
    }
}
