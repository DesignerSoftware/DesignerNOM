/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.TiposDotaciones;
import InterfacePersistencia.PersistenciaTiposDotacionesInterface;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import javax.ejb.Stateless;
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
public class PersistenciaTiposDotaciones implements PersistenciaTiposDotacionesInterface {

    private static Logger log = Logger.getLogger(PersistenciaTiposDotaciones.class);

    @Override
    public String crear(EntityManager em, TiposDotaciones tipodotacion) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(tipodotacion);
            tx.commit();
            return "EXITO";
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            if (e instanceof PersistenceException || e instanceof SQLIntegrityConstraintViolationException || e instanceof DatabaseException) {
                log.error("Error PersistenciaTiposDotaciones.crear:  ", e);
                return e.getMessage();
            } else {
                return "Ha ocurrido un error al crear el tipo dotación";
            }
        }
    }

    @Override
    public String editar(EntityManager em, TiposDotaciones tipodotacion) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(tipodotacion);
            tx.commit();
            return "EXITO";
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            if (e instanceof PersistenceException || e instanceof SQLIntegrityConstraintViolationException || e instanceof DatabaseException) {
                log.error("Error PersistenciaTiposDotaciones.editar:  ", e);
                return e.getMessage();
            } else {
                return "Ha ocurrido un error al editar el tipo dotación";
            }
        }
    }

    @Override
    public String borrar(EntityManager em, TiposDotaciones tipodotacion) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(tipodotacion));
            tx.commit();
            return "EXITO";
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            if (e instanceof PersistenceException || e instanceof SQLIntegrityConstraintViolationException || e instanceof DatabaseException) {
                log.error("Error PersistenciaTiposDotaciones.borrar:  ", e);
                return e.getMessage();
            } else {
                return "Ha ocurrido un error al borrar el tipo dotación";
            }
        }
    }

    @Override
    public List<TiposDotaciones> consultarTiposDotaciones(EntityManager em) {
        try {
            em.clear();
            String sql="SELECT * FROM TIPOSDOTACIONES ORDER BY CODIGO";
            Query query = em.createNativeQuery(sql, TiposDotaciones.class);
            List<TiposDotaciones> tiposdotaciones = query.getResultList();
            return tiposdotaciones;
        } catch (Exception e) {
            log.error("PersistenciaTiposPagos.consultarTiposDotaciones():  ", e);
            return null;
        }
    }

}
