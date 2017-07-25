/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.Idiomas;
import InterfacePersistencia.PersistenciaIdiomasInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la tabla 'Idiomas' de la base
 * de datos.
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaIdiomas implements PersistenciaIdiomasInterface {

   private static Logger log = Logger.getLogger(PersistenciaIdiomas.class);

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
    /*@PersistenceContext(unitName = "DesignerRHN-ejbPU")
    private EntityManager em;*/

    @Override
    public void crear(EntityManager em, Idiomas idiomas) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(idiomas);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaIdiomas.crear: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, Idiomas idiomas) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(idiomas);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaIdiomas.editar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, Idiomas idiomas) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(idiomas));
            tx.commit();

        } catch (Exception e) {
        log.error("Error PersistenciaIdiomas.borrar: " + e.getMessage());
                if (tx.isActive()) {
                    tx.rollback();
                }
        }
    }

    public Idiomas buscarIdioma(EntityManager em, BigInteger secuenciaI) {
        try {
            em.clear();
            return em.find(Idiomas.class, secuenciaI);
        } catch (Exception e) {
            log.error("Persistencia.PersistenciaIdiomas.buscarIdioma()" + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Idiomas> buscarIdiomas(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT i FROM Idiomas i");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<Idiomas> idioma = (List<Idiomas>) query.getResultList();
            return idioma;
        } catch (Exception e) {
            log.error("Error buscarIdiomas PersistenciaIdiomas : " + e.toString());
            return null;
        }
    }

    public BigInteger contadorIdiomasPersonas(EntityManager em, BigInteger secuencia) {
        BigInteger retorno = new BigInteger("-1");
        try {
            em.clear();
            String sqlQuery = "SELECT COUNT(*)FROM idiomaspersonas WHERE idioma = ?";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            retorno = new BigInteger(query.getSingleResult().toString());
            return retorno;
        } catch (Exception e) {
            log.error("Error PersistenciaIdiomas contadorIdiomasPersonas. " + e);
            return retorno;
        }
    }
}
