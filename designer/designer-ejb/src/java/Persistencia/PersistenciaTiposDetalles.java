/**
 * Documentación a cargo de AndresPineda
 */
package Persistencia;

import Entidades.TiposDetalles;
import InterfacePersistencia.PersistenciaTiposDetallesInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

@Stateless
public class PersistenciaTiposDetalles implements PersistenciaTiposDetallesInterface {

   private static Logger log = Logger.getLogger(PersistenciaTiposDetalles.class);

    @Override
    public void crear(EntityManager em, TiposDetalles tiposDetalles) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(tiposDetalles);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaTiposDetalles.crear: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, TiposDetalles tiposDetalles) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(tiposDetalles);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaTiposDetalles.editar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, TiposDetalles tiposDetalles) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(tiposDetalles));
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaTiposDetalles.borrar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public List<TiposDetalles> buscarTiposDetalles(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT td FROM TiposDetalles td ORDER BY td.codigo ASC");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<TiposDetalles> tiposDetalles = query.getResultList();
            return tiposDetalles;
        } catch (Exception e) {
            log.error("Error buscarTiposDetalles PersistenciaTiposDetalles : " + e.getMessage());
            return null;
        }
    }

    @Override
    public TiposDetalles buscarTiposDetallesSecuencia(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT td FROM TiposDetalles td WHERE td.secuencia =:secuencia");
            query.setParameter("secuencia", secuencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            TiposDetalles tiposDetalles = (TiposDetalles) query.getSingleResult();
            return tiposDetalles;
        } catch (Exception e) {
            log.error("Error buscarTiposDetallesSecuencia PersistenciaTiposDetalles : " + e.getMessage());
            TiposDetalles tiposDetalles = null;
            return tiposDetalles;
        }
    }
}
