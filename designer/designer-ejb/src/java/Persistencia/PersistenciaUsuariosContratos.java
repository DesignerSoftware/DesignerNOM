/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.UsuariosContratos;
import InterfacePersistencia.PersistenciaUsuariosContratosInterface;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import org.eclipse.persistence.exceptions.DatabaseException;

/**
 *
 * @author user
 */
@Stateless
public class PersistenciaUsuariosContratos implements PersistenciaUsuariosContratosInterface {

    private static Logger log = Logger.getLogger(PersistenciaUsuariosContratos.class);

    @Override
    public String crear(EntityManager em, UsuariosContratos usuariots) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(usuariots);
            tx.commit();
            return "EXITO";
        } catch (Exception e) {
            log.error("Error PersistenciaUsuariosContratos.crear:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
            if (e instanceof PersistenceException || e instanceof SQLIntegrityConstraintViolationException || e instanceof DatabaseException) {
                log.error("Error PersistenciaUsuariosContratos.editar :  ", e);
                return e.toString();
            } else {
                return "Ha ocurrido un error al crear el usuarioContrato";
            }
        }
    }

    @Override
    public String editar(EntityManager em, UsuariosContratos usuariots) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(usuariots);
            tx.commit();
            return "EXITO";
        } catch (Exception e) {
            log.error("Error PersistenciaUsuariosContratos.editar:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
            if (e instanceof PersistenceException || e instanceof SQLIntegrityConstraintViolationException || e instanceof DatabaseException) {
                log.error("Error PersistenciaUsuariosContratos.editar :  ", e);
                return e.toString();
            } else {
                return "Ha ocurrido un error al editar el usuarioContrato";
            }
        }
    }

    @Override
    public String borrar(EntityManager em, UsuariosContratos usuariots) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(usuariots));
            tx.commit();
            return "EXITO";
        } catch (Exception e) {
            log.error("Error PersistenciaUsuariosContratos.borrar:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
            if (e instanceof PersistenceException || e instanceof SQLIntegrityConstraintViolationException || e instanceof DatabaseException) {
                log.error("Error PersistenciaUsuariosContratos.borrar :  ", e);
                return e.toString();
            } else {
                return "Ha ocurrido un error al borrar el usuarioContrato";
            }
        }
    }

    @Override
    public List<UsuariosContratos> buscarUsuariosContratos(EntityManager em) {
        try {
            em.clear();
            String sql = "SELECT * FROM USUARIOSCONTRATOS";
            Query query = em.createNativeQuery(sql, UsuariosContratos.class);
            List<UsuariosContratos> usuariosTS = query.getResultList();
            return usuariosTS;
        } catch (Exception e) {
            log.error("Error PersistenciaUsuariosContratos.buscarUsuariosContratos ", e);
            return null;
        }
    }

}
