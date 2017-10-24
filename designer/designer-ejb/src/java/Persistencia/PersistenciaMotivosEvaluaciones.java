/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.MotivosEvaluaciones;
import InterfacePersistencia.PersistenciaMotivosEvaluacionesInterface;
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
public class PersistenciaMotivosEvaluaciones implements PersistenciaMotivosEvaluacionesInterface {

    private static Logger log = Logger.getLogger(PersistenciaMotivosEvaluaciones.class);

    @Override
    public String crear(EntityManager em, MotivosEvaluaciones motivo) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(motivo);
            tx.commit();
            return "EXITO";
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            if (e instanceof PersistenceException || e instanceof SQLIntegrityConstraintViolationException || e instanceof DatabaseException) {
                log.error("Error PersistenciaMotivosEvaluaciones.crear:  ", e);
                return e.toString();
            } else {
                return "Ha ocurrido un error al Crear el Motivo Evaluación";
            }
        }
    }

    @Override
    public String editar(EntityManager em, MotivosEvaluaciones motivo) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(motivo);
            tx.commit();
            return "EXITO";
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            if (e instanceof PersistenceException || e instanceof SQLIntegrityConstraintViolationException || e instanceof DatabaseException) {
                log.error("Error PersistenciaMotivosEvaluaciones.editar:  ", e);
                return e.toString();
            } else {
                return "Ha ocurrido un error al Editar el Motivo Evaluación";
            }
        }
    }

    @Override
    public String borrar(EntityManager em, MotivosEvaluaciones motivo) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(motivo));
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
                return "Ha ocurrido un error al Borrar el Motivo Evaluación";
            }
        }
    }

    @Override
    public List<MotivosEvaluaciones> buscarMotivosEvaluaciones(EntityManager em) {
        try {
            em.clear();
            String sql = "SELECT * FROM MOTIVOSEVALUACIONES";
            Query query = em.createNativeQuery(sql, MotivosEvaluaciones.class);
            List<MotivosEvaluaciones> lista = query.getResultList();
            return lista;
        } catch (Exception e) {
            log.error("Error PersistenciaMotivosAusentismos.buscarMotivosEvaluaciones ", e);
            return null;
        }
    }

}
