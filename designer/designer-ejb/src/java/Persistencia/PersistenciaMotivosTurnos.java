package Persistencia;

import Entidades.MotivosTurnos;
import InterfacePersistencia.PersistenciaMotivosTurnosInterface;
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
public class PersistenciaMotivosTurnos implements PersistenciaMotivosTurnosInterface {

   private static Logger log = Logger.getLogger(PersistenciaMotivosTurnos.class);

    @Override
    public void crear(EntityManager em, MotivosTurnos motivosTurnos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(motivosTurnos);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaMotivosTurnos.crear: " + e.toString());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, MotivosTurnos motivosTurnos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(motivosTurnos);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaMotivosTurnos.editar: " + e.toString());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, MotivosTurnos motivosTurnos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(motivosTurnos));
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaMotivosTurnos.borrar: " + e.toString());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public List<MotivosTurnos> consultarMotivosTurnos(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT m FROM MotivosTurnos m ORDER BY m.codigo ASC ");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<MotivosTurnos> motivosTurnos = query.getResultList();
            return motivosTurnos;
        } catch (Exception e) {
            log.error("Error PersistenciaMotivosTurnos.consultarMotivosTurnos :" + e.toString());
            return null;
        }

    }

    @Override
    public MotivosTurnos consultarMotivoTurnoPorSecuencia(EntityManager em, BigInteger secuencia) {

        try {
            em.clear();
            Query query = em.createQuery("SELECT m FROM MotivosTurnos m WHERE m.secuencia = :secuencia");
            query.setParameter("secuencia", secuencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            MotivosTurnos motivosTurnos = (MotivosTurnos) query.getSingleResult();
            return motivosTurnos;
        } catch (Exception e) {
            log.error("Error PersistenciaMotivosTurnos.consultarMotivoTurnoPorSecuencia : " + e.toString());
            MotivosTurnos motivosTurnos = null;
            return motivosTurnos;
        }

    }
}
