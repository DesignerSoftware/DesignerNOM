/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.Indices;
import Entidades.Usuarios;
import Entidades.UsuariosIndices;
import InterfacePersistencia.PersistenciaUsuariosIndicesInterface;
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
public class PersistenciaUsuariosIndices implements PersistenciaUsuariosIndicesInterface {

    private static Logger log = Logger.getLogger(PersistenciaUsuariosIndices.class);

    @Override
    public String crear(EntityManager em, UsuariosIndices usuarioI) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(usuarioI);
            tx.commit();
            return "EXITO";
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            if (e instanceof PersistenceException || e instanceof SQLIntegrityConstraintViolationException || e instanceof DatabaseException) {
                log.error("Error PersistenciaUsuariosIndices.crear :  ", e);
                return e.toString();
            } else {
                return "Ha ocurrido un error al crear el usuarioIndice";
            }
        }
    }

    @Override
    public String editar(EntityManager em, UsuariosIndices usuarioI) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(usuarioI);
            tx.commit();
            return "EXITO";
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            if (e instanceof PersistenceException || e instanceof SQLIntegrityConstraintViolationException || e instanceof DatabaseException) {
                log.error("Error PersistenciaUsuariosIndices.editar :  ", e);
                return e.toString();
            } else {
                return "Ha ocurrido un error al editar el usuarioIndice";
            }
        }
    }

    @Override
    public String borrar(EntityManager em, UsuariosIndices usuarioI) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(usuarioI));
            tx.commit();
            return "EXITO";
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            if (e instanceof PersistenceException || e instanceof SQLIntegrityConstraintViolationException || e instanceof DatabaseException) {
                log.error("Error PersistenciaUsuariosIndices.borrar :  ", e);
                return e.toString();
            } else {
                return "Ha ocurrido un error al borrar el usuarioIndice";
            }
        }
    }

    @Override
    public List<Indices> lovIndices(EntityManager em) {
       try {
            em.clear();
            String sql = "SELECT * FROM INDICES ORDER BY CODIGO";
            Query query = em.createNativeQuery(sql, Indices.class);
            List<Indices> lista = query.getResultList();
            return lista;
        } catch (Exception e) {
            log.error("Error PersistenciaUsuariosIndices.lovIndices() :  ", e);
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
            log.error("Error PersistenciaUsuariosIndices.listaUsuarios() :  ", e);
            return null;
        }
    }

    @Override
    public List<UsuariosIndices> listaUsuariosIndices(EntityManager em,BigInteger secUsuario) {
        try {
            em.clear();
            String sql = "SELECT * FROM USUARIOSINDICES WHERE USUARIO = ? ORDER BY INDICE";
            Query query = em.createNativeQuery(sql, UsuariosIndices.class);
            query.setParameter(1, secUsuario);
            List<UsuariosIndices> lista = query.getResultList();
            return lista;
        } catch (Exception e) {
            log.error("Error PersistenciaUsuariosIndices.listaUsuariosIndices() :  ", e);
            return null;
        }
    }

}
