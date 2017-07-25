/**
 * Documentación a cargo de AndresPineda
 */
package Persistencia;

import Entidades.Circulares;
import InterfacePersistencia.PersistenciaCircularesInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 * Clase Stateless. <br>
 * Clase encargada de realizar operaciones sobre la tabla 'Circulares' de la
 * base de datos
 *
 * @author AndresPineda
 */

@Stateless
public class PersistenciaCirculares implements PersistenciaCircularesInterface {

   private static Logger log = Logger.getLogger(PersistenciaCirculares.class);

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos
     */
    /*@PersistenceContext(unitName = "DesignerRHN-ejbPU")
    private EntityManager em;*/

    @Override
    public void crear(EntityManager em,Circulares circulares) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(circulares);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaCentrosCostos.crear: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em,Circulares circulares) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(circulares);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaCentrosCostos.editar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em,Circulares circulares) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(circulares));
            tx.commit();

        } catch (Exception e) {
            try {
                if (tx.isActive()) {
                    tx.rollback();
                }
            } catch (Exception ex) {
                log.error("Error PersistenciaCentrosCostos.borrar: " + e);
            }
        }
    }

    @Override
    public List<Circulares> buscarCirculares(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT c FROM Circulares c");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<Circulares> circulares = query.getResultList();
            return circulares;
        } catch (Exception e) {
            log.error("Error buscarCirculares PersistenciaCirculares : " + e.toString());
            return null;
        }
    }

    @Override
    public Circulares buscarCircularSecuencia(EntityManager em,BigInteger secuencia) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT c FROM Circulares c WHERE c.secuencia =:secuencia");
            query.setParameter("secuencia", secuencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            Circulares circulares = (Circulares) query.getSingleResult();
            return circulares;
        } catch (Exception e) {
            log.error("Error buscarCircularSecuencia  PersistenciaCirculares : " + e.toString());
            Circulares circulares = null;
            return circulares;
        }
    }

    @Override
    public List<Circulares> buscarCircularesPorSecuenciaEmpresa(EntityManager em,BigInteger secuencia) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT c FROM Circulares c WHERE c.empresa.secuencia =:secuencia");
            query.setParameter("secuencia", secuencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<Circulares> circulares = query.getResultList();
            return circulares;
        } catch (Exception e) {
            log.error("Error buscarCircularesPorSecuenciaEmpresa  PersistenciaCirculares : " + e.toString());
            List<Circulares> circulares = null;
            return circulares;
        }
    }
}
