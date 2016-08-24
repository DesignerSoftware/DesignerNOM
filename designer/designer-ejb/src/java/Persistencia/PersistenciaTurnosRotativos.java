package Persistencia;

import Entidades.Turnosrotativos;
import InterfacePersistencia.PersistenciaTurnosRotativosInterface;
import java.math.BigInteger;
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
public class PersistenciaTurnosRotativos implements PersistenciaTurnosRotativosInterface {

    @Override
    public void crear(EntityManager em, Turnosrotativos turnosrotativos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(turnosrotativos);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error crear PersistenciaTurnosRotativos " + e.toString());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, Turnosrotativos turnosrotativos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(turnosrotativos);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error editar PersistenciaTurnosRotativos " + e.toString());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, Turnosrotativos turnosrotativos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(turnosrotativos));
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error borrar PersistenciaTurnosRotativos " + e.toString());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public Turnosrotativos buscarTurnoRotativoPorSecuencia(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT tr FROM Turnosrotativos tr WHERE tr.secuencia = :secuencia");
            query.setParameter("secuencia", secuencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            Turnosrotativos turnorotativo = (Turnosrotativos) query.getSingleResult();
            return turnorotativo;
        } catch (Exception e) {
            System.out.println("Error buscarTurnoRotativoPorSecuencia PersistenciaTurnosRotativos " + e.toString());
            return null;
        }
    }

    @Override
    public List<Turnosrotativos> buscarTurnosRotativos(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT tr FROM Turnosrotativos tr ORDER BY tr.codigo ASC");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<Turnosrotativos> turnosrotativos = query.getResultList();
            return turnosrotativos;
        } catch (Exception e) {
            System.out.println("Error buscarTurnosRotativos PersistenciaTurnosRotativos " + e.toString());
            return null;
        }
    }

    @Override
    public List<Turnosrotativos> buscarTurnosRotativosPorCuadrilla(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT tr FROM Turnosrotativos tr WHERE tr.cuadrilla.secuencia=:secuencia ORDER BY tr.codigo ASC");
            query.setParameter("secuencia", secuencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<Turnosrotativos> turnosrotativos = query.getResultList();
            return turnosrotativos;
        } catch (Exception e) {
            System.out.println("Error buscarTurnosRotativosPorCuadrilla PersistenciaTurnosRotativos " + e.toString());
            return null;
        }
    }
}
