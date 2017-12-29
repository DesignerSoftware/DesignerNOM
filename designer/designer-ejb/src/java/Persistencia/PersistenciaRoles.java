/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.Roles;
import InterfacePersistencia.PersistenciaRolInterface;
import java.math.BigInteger;
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
public class PersistenciaRoles implements PersistenciaRolInterface {

    private static Logger log = Logger.getLogger(PersistenciaTablas.class);

    @Override
    public List<Roles> consultarRol(EntityManager em) {
        try {
            em.clear();
            Query query = em.createNativeQuery("SELECT * FROM Roles r ORDER BY r.nombre", Roles.class);
            List<Roles> lista = query.getResultList();
            return lista;
        } catch (Exception e) {
            log.error("ERROR: PersistenciaTablas.consultarTablas() Error:  ", e);
            return null;
        }
    }

    @Override
    public List<Roles> buscarRol(EntityManager em, BigInteger secuencia) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String crear(EntityManager em, Roles pantalla) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(pantalla);
            tx.commit();
            return "EXITO";
        } catch (Exception e) {
            log.error("Error PersistenciaRoles.crear:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
            if (e instanceof PersistenceException || e instanceof SQLIntegrityConstraintViolationException || e instanceof DatabaseException) {
                log.error("Error PersistenciaRoles.crear:  ", e);
                return e.toString();
            } else {
                return "Ha ocurrido un error al Crear el Rol";
            }
        }
    }

    @Override
    public String editar(EntityManager em, Roles pantalla) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(pantalla);
            tx.commit();
            return "EXITO";
        } catch (Exception e) {
            log.error("Error PersistenciaRoles.editar:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
            if (e instanceof PersistenceException || e instanceof SQLIntegrityConstraintViolationException || e instanceof DatabaseException) {
                log.error("Error PersistenciaRoles.editar:  ", e);
                return e.toString();
            } else {
                return "Ha ocurrido un error al Editar la tabla";
            }
        }
    }

    @Override
    public String borrar(EntityManager em, Roles rol) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(rol));
            tx.commit();
            return "EXITO";
        } catch (Exception e) {
            log.error("Error PersistenciaRoles.borrar:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
            if (e instanceof PersistenceException || e instanceof SQLIntegrityConstraintViolationException || e instanceof DatabaseException) {
                log.error("Error PersistenciaRoles.borrar:  ", e);
                return e.toString();
            } else {
                return "Ha ocurrido un error al Borrar la tabla";
            }
        }
    }
}
