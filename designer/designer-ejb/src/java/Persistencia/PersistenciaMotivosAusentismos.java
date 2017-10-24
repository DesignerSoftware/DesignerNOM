/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.MotivosAusentismos;
import InterfacePersistencia.PersistenciaMotivosAusentismosInterface;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import org.apache.log4j.Logger;
import org.eclipse.persistence.exceptions.DatabaseException;

/**
 *
 * @author user
 */
@Stateless
public class PersistenciaMotivosAusentismos implements PersistenciaMotivosAusentismosInterface {

    private static Logger log = Logger.getLogger(PersistenciaMotivosAusentismos.class);

    @Override
    public String crear(EntityManager em, MotivosAusentismos motivoAusentismo) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(motivoAusentismo);
            tx.commit();
            return "EXITO";
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            if (e instanceof PersistenceException || e instanceof SQLIntegrityConstraintViolationException || e instanceof DatabaseException) {
                log.error("Error PersistenciaMotivosAusentismos.crear:  ", e);
                return e.toString();
            } else {
                return "Ha ocurrido un error al Crear el Motivo Ausentismo";
            }
        }
    }

    @Override
    public String editar(EntityManager em, MotivosAusentismos motivoAusentismo) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(motivoAusentismo);
            tx.commit();
            return "EXITO";
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            if (e instanceof PersistenceException || e instanceof SQLIntegrityConstraintViolationException || e instanceof DatabaseException) {
                log.error("Error PersistenciaMotivosAusentismos.editar:  ", e);
                return e.toString();
            } else {
                return "Ha ocurrido un error al Editar el Motivo Ausentismo";
            }
        }
    }

    @Override
    public String borrar(EntityManager em, MotivosAusentismos motivoAusentismo) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(motivoAusentismo));
            tx.commit();
            return "EXITO";
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            if (e instanceof PersistenceException || e instanceof SQLIntegrityConstraintViolationException || e instanceof DatabaseException) {
                log.error("Error PersistenciaMotivosAusentismos.borrar:  ", e);
                return e.toString();
            } else {
                return "Ha ocurrido un error al Borrar el Motivo Ausentismo";
            }
        }
    }

    @Override
    public List<MotivosAusentismos> buscarMotivosAusentismo(EntityManager em) {
        try {
            em.clear();
            String sql = "SELECT * FROM MOTIVOSAUSENTISMOS";
            Query query = em.createNativeQuery(sql, MotivosAusentismos.class);
            List<MotivosAusentismos> lista = query.getResultList();
            return lista;
        } catch (Exception e) {
            log.error("Error PersistenciaMotivosAusentismos.buscarMotivosAusentismos ", e);
            return null;
        }

    }
}
