package Persistencia;

import Entidades.DetallesTurnosRotativos;
import InterfacePersistencia.PersistenciaDetallesTurnosRotativosInterface;
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
public class PersistenciaDetallesTurnosRotativos implements PersistenciaDetallesTurnosRotativosInterface{

    @Override
    public void crear(EntityManager em, DetallesTurnosRotativos rotativos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(rotativos);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error crear PersistenciaDetallesTurnosRotativos " + e.toString());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, DetallesTurnosRotativos rotativos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(rotativos);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error editar PersistenciaDetallesTurnosRotativos " + e.toString());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, DetallesTurnosRotativos rotativos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(rotativos));
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error borrar PersistenciaDetallesTurnosRotativos " + e.toString());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public DetallesTurnosRotativos buscarDetalleTurnoRotativoPorSecuencia(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT dtr FROM DetallesTurnosRotativos dtr WHERE dtr.secuencia = :secuencia");
            query.setParameter("secuencia", secuencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            DetallesTurnosRotativos rotativos = (DetallesTurnosRotativos) query.getSingleResult();
            return rotativos;
        } catch (Exception e) {
            System.out.println("Error buscarDetalleTurnoRotativoPorSecuencia PersistenciaDetallesTurnosRotativos " + e.toString());
            return null;
        }
    }

    @Override
    public List<DetallesTurnosRotativos> buscarDetallesTurnosRotativos(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT dtr FROM DetallesTurnosRotativos dtr");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<DetallesTurnosRotativos> rotativos = query.getResultList();
            return rotativos;
        } catch (Exception e) {
            System.out.println("Error buscarDetallesTurnosRotativos PersistenciaDetallesTurnosRotativos " + e.toString());
            return null;
        }
    }

    @Override
    public List<DetallesTurnosRotativos> buscarDetallesTurnosRotativosPorTurnoRotativo(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT dtr FROM DetallesTurnosRotativos dtr WHERE dtr.turnorotativo.secuencia=:secuencia");
            query.setParameter("secuencia", secuencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<DetallesTurnosRotativos> rotativos = query.getResultList();
            return rotativos;
        } catch (Exception e) {
            System.out.println("Error buscarDetallesTurnosRotativosPorTurnoRotativo PersistenciaDetallesTurnosRotativos " + e.toString());
            return null;
        }
    }
}
