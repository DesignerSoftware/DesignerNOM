/**
 * Documentación a cargo de AndresPineda
 */
package Persistencia;

import Entidades.SueldosMercados;
import InterfacePersistencia.PersistenciaSueldosMercadosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;


@Stateless
public class PersistenciaSueldosMercados implements PersistenciaSueldosMercadosInterface {

   private static Logger log = Logger.getLogger(PersistenciaSueldosMercados.class);

   
    @Override
    public void crear(EntityManager em, SueldosMercados sueldosMercados) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(sueldosMercados);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaSueldosMercados.crear: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, SueldosMercados sueldosMercados) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(sueldosMercados);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaSueldosMercados.editar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, SueldosMercados sueldosMercados) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(sueldosMercados));
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaSueldosMercados.borrar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public List<SueldosMercados> buscarSueldosMercados(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT sm FROM SueldosMercados sm ORDER BY sm.sueldomax ASC");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<SueldosMercados> sueldosMercados = query.getResultList();
            return sueldosMercados;
        } catch (Exception e) {
            log.error("Error buscarSueldosMercados PersistenciaSueldosMercados : " + e.getMessage());
            return null;
        }
    }

    @Override
    public SueldosMercados buscarSueldosMercadosSecuencia(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT sm FROM SueldosMercados sm WHERE sm.secuencia = :secuencia");
            query.setParameter("secuencia", secuencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            SueldosMercados sueldosMercados = (SueldosMercados) query.getSingleResult();
            return sueldosMercados;
        } catch (Exception e) {
            log.error("Error buscarSueldosMercadosSecuencia PersistenciaSueldosMercados : " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<SueldosMercados> buscarSueldosMercadosPorSecuenciaCargo(EntityManager em, BigInteger secCargo) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT sm FROM SueldosMercados sm WHERE sm.cargo.secuencia=:secCargo");
            query.setParameter("secCargo", secCargo);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<SueldosMercados> sueldosMercados = query.getResultList();
            return sueldosMercados;
        } catch (Exception e) {
            log.error("Error buscarSueldosMercadosPorSecuenciaCargo PersistenciaSueldosMercados : " + e.getMessage());
            return null;
        }
    }

}
