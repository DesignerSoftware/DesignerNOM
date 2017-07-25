/**
 * Documentación a cargo de AndresPineda
 */
package Persistencia;

import Entidades.VigenciasMonedasBases;
import InterfacePersistencia.PersistenciaVigenciasMonedasBasesInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

@Stateless
public class PersistenciaVigenciasMonedasBases implements PersistenciaVigenciasMonedasBasesInterface {

   private static Logger log = Logger.getLogger(PersistenciaVigenciasMonedasBases.class);

    @Override
    public void crear(EntityManager em, VigenciasMonedasBases monedasBases) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(monedasBases);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaVigenciasMonedasBases.crear: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, VigenciasMonedasBases monedasBases) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(monedasBases);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaVigenciasMonedasBases.editar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, VigenciasMonedasBases monedasBases) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(monedasBases));
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaVigenciasMonedasBases.borrar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public List<VigenciasMonedasBases> buscarVigenciasMonedasBases(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT c FROM VigenciasMonedasBases c");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<VigenciasMonedasBases> monedasBases = query.getResultList();
            return monedasBases;
        } catch (Exception e) {
            log.error("Error buscarVigenciasMonedasBases PersistenciaVigenciasMonedasBases : " + e.getMessage());
            return null;
        }
    }

    @Override
    public VigenciasMonedasBases buscarVigenciaMonedaBaseSecuencia(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT c FROM VigenciasMonedasBases c WHERE c.secuencia =:secuencia");
            query.setParameter("secuencia", secuencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            VigenciasMonedasBases monedasBases = (VigenciasMonedasBases) query.getSingleResult();
            return monedasBases;
        } catch (Exception e) {
            log.error("Error buscarVigenciaMonedaBaseSecuencia  PersistenciaVigenciasMonedasBases : " + e.getMessage());
            VigenciasMonedasBases monedasBases = null;
            return monedasBases;
        }
    }

    @Override
    public List<VigenciasMonedasBases> buscarVigenciasMonedasBasesPorSecuenciaEmpresa(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT c FROM VigenciasMonedasBases c WHERE c.empresa.secuencia =:secuencia");
            query.setParameter("secuencia", secuencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<VigenciasMonedasBases> monedasBases = query.getResultList();
            return monedasBases;
        } catch (Exception e) {
            log.error("Error buscarVigenciasMonedasBasesPorSecuenciaEmpresa  PersistenciaVigenciasMonedasBases : " + e.getMessage());
            List<VigenciasMonedasBases> monedasBases = null;
            return monedasBases;
        }
    }
}
