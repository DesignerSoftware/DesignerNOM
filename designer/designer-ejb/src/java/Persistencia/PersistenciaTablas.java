/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.Tablas;
import InterfacePersistencia.PersistenciaTablasInterface;
import java.math.BigInteger;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
import org.apache.log4j.Logger;
import javax.persistence.Query;
import org.eclipse.persistence.exceptions.DatabaseException;

@Stateless
public class PersistenciaTablas implements PersistenciaTablasInterface {

    private static Logger log = Logger.getLogger(PersistenciaTablas.class);

    public List<Tablas> consultarTablas(EntityManager em) {
        try {
            em.clear();
            Query query = em.createNativeQuery("SELECT * FROM Tablas t ORDER BY t.nombre", Tablas.class);
            List<Tablas> lista = query.getResultList();
            return lista;
        } catch (Exception e) {
            log.error("ERROR: PersistenciaTablas.consultarTablas() Error:  ", e);
            return null;
        }
    }

    @Override
    public List<Tablas> buscarTablas(EntityManager em, BigInteger secuenciaMod) {
        //log.warn("PersistenciaTablas.buscarTablas() secuenciaMod : " + secuenciaMod);
        try {
            em.clear();
            Query query = em.createQuery("select t from Tablas t where t.modulo.secuencia = :secuenciaMod "
                    + " and EXISTS (SELECT p FROM Pantallas p where t = p.tabla)"
                    + " and t.tipo in ('SISTEMA','CONFIGURACION') "
                    + "order by t.nombre");
            query.setParameter("secuenciaMod", secuenciaMod);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            //log.warn("PersistenciaTablas.buscarTablas() query : " + query);
            List<Tablas> tablas = (List<Tablas>) query.getResultList();
            return tablas;
        } catch (Exception e) {
            log.error("PersistenciaTablas.buscarTablas():  ", e);
            return null;
        }
    }

    @Override
    public String crear(EntityManager em, Tablas pantalla) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(pantalla);
            tx.commit();
            return "EXITO";
        } catch (Exception e) {
            log.error("Error PersistenciaTablas.crear:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
            if (e instanceof PersistenceException || e instanceof SQLIntegrityConstraintViolationException || e instanceof DatabaseException) {
                log.error("Error PersistenciaTablas.crear:  ", e);
                return e.toString();
            } else {
                return "Ha ocurrido un error al Crear la tabla";
            }
        }
    }

    @Override
    public String editar(EntityManager em, Tablas pantalla) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(pantalla);
            tx.commit();
            return "EXITO";
        } catch (Exception e) {
            log.error("Error PersistenciaTablas.editar:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
            if (e instanceof PersistenceException || e instanceof SQLIntegrityConstraintViolationException || e instanceof DatabaseException) {
                log.error("Error PersistenciaTablas.editar:  ", e);
                return e.toString();
            } else {
                return "Ha ocurrido un error al Editar la tabla";
            }
        }
    }

    @Override
    public String borrar(EntityManager em, Tablas pantalla) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(pantalla));
            tx.commit();
            return "EXITO";
        } catch (Exception e) {
            log.error("Error PersistenciaTablas.borrar:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
            if (e instanceof PersistenceException || e instanceof SQLIntegrityConstraintViolationException || e instanceof DatabaseException) {
                log.error("Error PersistenciaTablas.borrar:  ", e);
                return e.toString();
            } else {
                return "Ha ocurrido un error al Borrar la tabla";
            }
        }
    }
}
