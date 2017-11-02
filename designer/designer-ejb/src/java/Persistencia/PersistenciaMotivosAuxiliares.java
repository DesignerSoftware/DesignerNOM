/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.MotivosAuxiliares;
import InterfacePersistencia.PersistenciaMotivosAuxiliaresInterface;
import java.math.BigInteger;
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
public class PersistenciaMotivosAuxiliares implements PersistenciaMotivosAuxiliaresInterface {

    private static Logger log = Logger.getLogger(PersistenciaMotivosAuxiliares.class);

    @Override
    public String crear(EntityManager em, MotivosAuxiliares motivoAux) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(motivoAux);
            tx.commit();
            return "EXITO";
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            if (e instanceof PersistenceException || e instanceof SQLIntegrityConstraintViolationException || e instanceof DatabaseException) {
                log.error("Error PersistenciaMotivosAuxiliares.crear:  ", e);
                return e.toString();
            } else {
                return "Ha ocurrido un error al Crear el Motivo Auxiliar";
            }
        }
    }

    @Override
    public String editar(EntityManager em, MotivosAuxiliares motivoAux) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(motivoAux);
            tx.commit();
            return "EXITO";
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            if (e instanceof PersistenceException || e instanceof SQLIntegrityConstraintViolationException || e instanceof DatabaseException) {
                log.error("Error PersistenciaMotivosAuxiliares.editar:  ", e);
                return e.toString();
            } else {
                return "Ha ocurrido un error al Editar el Motivo Auxiliar";
            }
        }
    }

    @Override
    public String borrar(EntityManager em, MotivosAuxiliares motivoAux) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(motivoAux));
            tx.commit();
            return "EXITO";
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            if (e instanceof PersistenceException || e instanceof SQLIntegrityConstraintViolationException || e instanceof DatabaseException) {
                log.error("Error PersistenciaMotivosAuxiliares.borrar:  ", e);
                return e.toString();
            } else {
                return "Ha ocurrido un error al Borrar el Motivo Auxiliar";
            }
        }
    }

    @Override
    public List<MotivosAuxiliares> buscarMotivosAuxiliares(EntityManager em) {
        try {
            em.clear();
            Query query = em.createNativeQuery("SELECT * FROM MOTIVOSAUXILIARES ORDER BY CODIGO");
            List<MotivosAuxiliares> lista = query.getResultList();
            return lista;
        } catch (Exception e) {
            log.error("Error PersistenciaMotivosAuxiliares.buscarMotivosAuxiliares:  ", e);
            return null;
        }
    }

}
