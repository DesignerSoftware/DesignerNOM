/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.PryPlataformas;
import InterfacePersistencia.PersistenciaPryPlataformasInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 * Clase Stateless. <br>
 * Clase encargada de realizar operaciones sobre la tabla 'PryPlataformas' de la
 * base de datos.
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaPryPlataformas implements PersistenciaPryPlataformasInterface {

   private static Logger log = Logger.getLogger(PersistenciaPryPlataformas.class);

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
//    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
//    private EntityManager em;
    @Override
    public void crear(EntityManager em, PryPlataformas plataformas) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(plataformas);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaPryPlataformas.crear:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, PryPlataformas plataformas) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(plataformas);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaPryPlataformas.editar:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, PryPlataformas plataformas) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(plataformas));
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaPryPlataformas.borrar:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public List<PryPlataformas> buscarPryPlataformas(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT p FROM PryPlataformas p");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<PryPlataformas> plataformas = (List<PryPlataformas>) query.getResultList();
            return plataformas;
        } catch (Exception e) {
            log.error("Error buscarPryPlataformas PersistenciaPryPlataformas :  ", e);
            return null;
        }
    }

    @Override
    public PryPlataformas buscarPryPlataformaSecuencia(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT p FROM PryPlataformas p WHERE p.secuencia = :secuencia");
            query.setParameter("secuencia", secuencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            PryPlataformas plataformas = (PryPlataformas) query.getSingleResult();
            return plataformas;
        } catch (Exception e) {
            log.error("Error buscarPryPlataformaSecuencia PersistenciaPryPlataformas :  ", e);
            PryPlataformas plataformas = null;
            return plataformas;
        }
    }

    public BigInteger contadorProyectos(EntityManager em, BigInteger secuencia) {
        BigInteger retorno = new BigInteger("-1");
        try {
            em.clear();
            String sqlQuery = " SELECT COUNT(*) FROM proyectos p , pry_plataformas pp WHERE p.pry_plataforma = pp.secuencia AND pp.secuencia = ?";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            retorno = new BigInteger(query.getSingleResult().toString());
            log.warn("Contador contadorProyectos persistencia " + retorno);
            return retorno;
        } catch (Exception e) {
            log.error("Error PERSISTENCIAPRYPLATAFORMAS contadorProyectos.  ", e);
            return retorno;
        }
    }
}
