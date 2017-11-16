/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.TiposJornadas;
import InterfacePersistencia.PersistenciaTiposJornadasInterface;
import java.math.BigInteger;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import org.eclipse.persistence.exceptions.DatabaseException;

@Stateless
public class PersistenciaTiposJornadas implements PersistenciaTiposJornadasInterface {

    private static Logger log = Logger.getLogger(PersistenciaTiposJornadas.class);

    @Override
    public String crear(EntityManager em, TiposJornadas tiposJornadas) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(tiposJornadas);
            tx.commit();
            return "EXITO";
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            if (e instanceof PersistenceException || e instanceof SQLIntegrityConstraintViolationException || e instanceof DatabaseException) {
                log.error("Error PersistenciaTiposJornadas.crear:  ", e);
                return e.toString();
            } else {
                return "Ha ocurrido un error al Crear la Jornada";
            }
        }
    }

    @Override
    public String editar(EntityManager em, TiposJornadas tiposJornadas) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(tiposJornadas);
            tx.commit();
            return "EXITO";
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            if (e instanceof PersistenceException || e instanceof SQLIntegrityConstraintViolationException || e instanceof DatabaseException) {
                log.error("Error PersistenciaTiposJornadas.editar:  ", e);
                return e.toString();
            } else {
                return "Ha ocurrido un error al Editar el Jornada";
            }
        }
    }

    @Override
    public String borrar(EntityManager em, TiposJornadas tiposJornadas) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(tiposJornadas));
            tx.commit();
            return "EXITO";
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            if (e instanceof PersistenceException || e instanceof SQLIntegrityConstraintViolationException || e instanceof DatabaseException) {
                log.error("Error PersistenciaTiposJornadas.borrar:  ", e);
                return e.toString();
            } else {
                return "Ha ocurrido un error al Borrar el Jornada";
            }
        }
    }

    @Override
    public TiposJornadas buscarTiposJornadas(EntityManager em, BigInteger sectiposJornadas) {
        try {
            em.clear();
            return em.find(TiposJornadas.class, sectiposJornadas);
        } catch (Exception e) {
            log.error("Error en la persistenciaTiposJornadasERROR :  ", e);
            return null;
        }
    }

    @Override
    public List<TiposJornadas> buscarTiposJornadas(EntityManager em) {
        System.out.println("Persistencia.PersistenciaTiposJornadas.buscarTiposJornadas()");
        try {
            em.clear();
            Query query = em.createQuery("SELECT ta FROM TiposJornadas  ta ORDER BY ta.codigo ASC");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<TiposJornadas> listTipoJornada = (List<TiposJornadas>) query.getResultList();
            return listTipoJornada;

        } catch (Exception e) {
            log.error("PersistenciaTiposJornadas.buscarTiposJornadas():  ", e);
            return null;
        }
    }

}
