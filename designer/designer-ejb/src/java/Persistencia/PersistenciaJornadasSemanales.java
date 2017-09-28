package Persistencia;

import Entidades.JornadasSemanales;
import InterfacePersistencia.PersistenciaJornadasSemanalesInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

@Stateless
public class PersistenciaJornadasSemanales implements PersistenciaJornadasSemanalesInterface {

   private static Logger log = Logger.getLogger(PersistenciaJornadasSemanales.class);

    @Override
    public void crear(EntityManager em, JornadasSemanales jornadasSemanales) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(jornadasSemanales);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaJornadasSemanales.crear:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, JornadasSemanales jornadasSemanales) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(jornadasSemanales);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaJornadasSemanales.crear:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, JornadasSemanales jornadasSemanales) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(jornadasSemanales));
            tx.commit();

        } catch (Exception e) {
            log.error("Error PersistenciaJornadasSemanales.borrar:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public List<JornadasSemanales> buscarJornadasSemanales(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT j FROM JornadasSemanales j");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<JornadasSemanales> jornadasSemanales = (List<JornadasSemanales>) query.getResultList();
            return jornadasSemanales;
        } catch (Exception e) {
            log.error("Error buscarJornadasSemanales PersistenciaJornadasSemanales ", e);
            return null;
        }
    }

    @Override
    public JornadasSemanales buscarJornadaSemanalSecuencia(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT jl FROM JornadasSemanales jl WHERE jl.secuencia = :secuencia");
            query.setParameter("secuencia", secuencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            JornadasSemanales jornadasSemanales = (JornadasSemanales) query.getSingleResult();
            return jornadasSemanales;
        } catch (Exception e) {
            log.error("Error buscarJornadaSemanalSecuencia PersistenciaJornadasSemanales ", e);
            JornadasSemanales jornadasSemanales = null;
            return jornadasSemanales;
        }

    }

    @Override
    public List<JornadasSemanales> buscarJornadasSemanalesPorJornadaLaboral(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT jl FROM JornadasSemanales jl WHERE jl.jornadalaboral.secuencia = :secuencia");
            query.setParameter("secuencia", secuencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<JornadasSemanales> jornadasSemanales = query.getResultList();
            return jornadasSemanales;
        } catch (Exception e) {
            log.error("Error buscarJornadasSemanalesPorJornadaLaboral PersistenciaJornadasSemanales ", e);
            List<JornadasSemanales> jornadasSemanales = null;
            return jornadasSemanales;
        }

    }

}
