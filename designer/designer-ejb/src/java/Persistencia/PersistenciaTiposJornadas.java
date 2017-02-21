/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.TiposJornadas;
import InterfacePersistencia.PersistenciaTiposJornadasInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class PersistenciaTiposJornadas implements PersistenciaTiposJornadasInterface {

    @Override
    public void crear(EntityManager em, TiposJornadas tiposJornadas) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(tiposJornadas);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaTiposJornadas.crear: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, TiposJornadas tiposJornadas) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(tiposJornadas);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaTiposJornadas.editar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, TiposJornadas tiposJornadas) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(tiposJornadas));
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaTiposJornadas.borrar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public TiposJornadas buscarTipoJornada(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            return em.find(TiposJornadas.class, secuencia);
        } catch (Exception e) {
            System.out.println("Error PersistenciaTiposJornadas buscarTipoJornada : " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<TiposJornadas> buscarTiposJornadas(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT tj FROM TiposJornadas tj ORDER BY tj.codigo DESC");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<TiposJornadas> tiposJornadas = query.getResultList();
            return tiposJornadas;
        } catch (Exception e) {
            System.out.println("Error PersistenciaTiposJornadas buscarTiposJornadas : " + e.getMessage());
            return null;
        }
    }
}
