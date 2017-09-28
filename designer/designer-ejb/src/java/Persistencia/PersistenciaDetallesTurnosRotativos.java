package Persistencia;

import Entidades.DetallesTurnosRotativos;
import InterfacePersistencia.PersistenciaDetallesTurnosRotativosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 *
 * @author Administrador
 */
@Stateless
public class PersistenciaDetallesTurnosRotativos implements PersistenciaDetallesTurnosRotativosInterface {

   private static Logger log = Logger.getLogger(PersistenciaDetallesTurnosRotativos.class);

    @Override
    public void crear(EntityManager em, DetallesTurnosRotativos rotativos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(rotativos);
            tx.commit();
        } catch (Exception e) {
            log.error("Error crear PersistenciaDetallesTurnosRotativos:  ", e);
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
            log.error("Error editar PersistenciaDetallesTurnosRotativos:  ", e);
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
            log.error("Error borrar PersistenciaDetallesTurnosRotativos:  ", e);
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
            log.error("Error buscarDetalleTurnoRotativoPorSecuencia PersistenciaDetallesTurnosRotativos:  ", e);
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
            log.error("Error buscarDetallesTurnosRotativos PersistenciaDetallesTurnosRotativos:  ", e);
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
            log.error("Error buscarDetallesTurnosRotativosPorTurnoRotativo PersistenciaDetallesTurnosRotativos:  ", e);
            return null;
        }
    }
}
