/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.Escenarios;
import Entidades.Usuarios;
import Entidades.UsuariosEscenarios;
import InterfacePersistencia.PersistenciaUsuariosEscenariosInterface;
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
public class PersistenciaUsuariosEscenarios implements PersistenciaUsuariosEscenariosInterface {

    private static Logger log = Logger.getLogger(PersistenciaUsuariosEscenarios.class);

    @Override
    public String crear(EntityManager em, UsuariosEscenarios usuarioE) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(usuarioE);
            tx.commit();
            return "EXITO";
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            if (e instanceof PersistenceException || e instanceof SQLIntegrityConstraintViolationException || e instanceof DatabaseException) {
                log.error("Error PersistenciaUsuariosEscenarios.crear :  ", e);
                return e.toString();
            } else {
                return "Ha ocurrido un error al crear el usuarioEscenario";
            }
        }
    }

    @Override
    public String editar(EntityManager em, UsuariosEscenarios usuarioE) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(usuarioE);
            tx.commit();
            return "EXITO";
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            if (e instanceof PersistenceException || e instanceof SQLIntegrityConstraintViolationException || e instanceof DatabaseException) {
                log.error("Error PersistenciaUsuariosEscenarios.editar :  ", e);
                return e.toString();
            } else {
                return "Ha ocurrido un error al editar el usuarioEscenario";
            }
        }
    }

    @Override
    public String borrar(EntityManager em, UsuariosEscenarios usuarioE) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(usuarioE));
            tx.commit();
            return "EXITO";
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            if (e instanceof PersistenceException || e instanceof SQLIntegrityConstraintViolationException || e instanceof DatabaseException) {
                log.error("Error PersistenciaUsuariosEscenarios.borrar :  ", e);
                return e.toString();
            } else {
                return "Ha ocurrido un error al borrar el usuarioEscenario";
            }
        }
    }

    @Override
    public List<UsuariosEscenarios> listaUsuariosEscenarios(EntityManager em) {
        try {
            em.clear();
            String sql = "SELECT * FROM USUARIOSESCENARIOS ";
            Query query = em.createNativeQuery(sql, UsuariosEscenarios.class);
            List<UsuariosEscenarios> lista = query.getResultList();
            return lista;
        } catch (Exception e) {
            log.error("Error PersistenciaUsuariosEscenarios.listaUsuariosEscenarios() :  ", e);
            return null;
        }
    }

    @Override
    public List<Escenarios> lovEscenarios(EntityManager em) {
        try {
            em.clear();
            String sql = "SELECT * FROM ESCENARIOS";
            Query query = em.createNativeQuery(sql, Escenarios.class);
            List<Escenarios> lista = query.getResultList();
            return lista;
        } catch (Exception e) {
            log.error("Error PersistenciaUsuariosEscenarios.lovEscenarios() :  ", e);
            return null;
        }
    }

    @Override
    public List<Usuarios> listaUsuarios(EntityManager em) {
        try {
            em.clear();
            String sql = "SELECT * FROM USUARIOS WHERE PERFIL IS NOT NULL ORDER BY ALIAS";
            Query query = em.createNativeQuery(sql, Usuarios.class);
            List<Usuarios> lista = query.getResultList();
            return lista;
        } catch (Exception e) {
            log.error("Error PersistenciaUsuariosEscenarios.listaUsuarios() :  ", e);
            return null;
        }
    }

}
