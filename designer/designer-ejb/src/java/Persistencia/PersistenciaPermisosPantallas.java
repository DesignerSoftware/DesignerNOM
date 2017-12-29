/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.ObjetosBloques;
import Entidades.Perfiles;
import Entidades.PermisosPantallas;
import InterfacePersistencia.PersistenciaPermisosPantallasInterface;
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

/**
 *
 * @author user
 */
@Stateless
public class PersistenciaPermisosPantallas implements PersistenciaPermisosPantallasInterface {

    private static Logger log = Logger.getLogger(PersistenciaPermisosPantallas.class);

    @Override
    public String crear(EntityManager em, PermisosPantallas permisosp) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(permisosp);
            tx.commit();
            return "EXITO";
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            if (e instanceof PersistenceException || e instanceof SQLIntegrityConstraintViolationException || e instanceof DatabaseException) {
                log.error("Error PersistenciaPermisosPantallas.crear:  ", e);
                return e.toString();
            } else {
                return "Ha ocurrido un error al Crear el Permiso Pantalla";
            }
        }
    }

    @Override
    public String editar(EntityManager em, PermisosPantallas permisosp) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(permisosp);
            tx.commit();
            return "EXITO";
        } catch (Exception e) {
            log.error("Error PersistenciaPermisosPantallas.editar:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
            if (e instanceof PersistenceException || e instanceof SQLIntegrityConstraintViolationException || e instanceof DatabaseException) {
                log.error("Error PersistenciaPermisosPantallas.editar:  ", e);
                return e.toString();
            } else {
                return "Ha ocurrido un error al Editar el Permiso Pantalla";
            }
        }
    }

    @Override
    public String borrar(EntityManager em, PermisosPantallas permisosp) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(permisosp));
            tx.commit();
            return "EXITO";
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            if (e instanceof PersistenceException || e instanceof SQLIntegrityConstraintViolationException || e instanceof DatabaseException) {
                log.error("Error PersistenciaPermisosPantallas.borrar:  ", e);
                return e.toString();
            } else {
                return "Ha ocurrido un error al Borrar el Permiso Pantalla";
            }
        }
    }

    @Override
    public List<PermisosPantallas> consultarPermisosPorPerfil(EntityManager em, BigInteger secPerfil) {
        try {
            em.clear();
            String sql = "SELECT PP.* \n"
                    + "FROM PERMISOSPANTALLAS PP \n"
                    + "WHERE PP.PERFIL = ?";
            Query query = em.createNativeQuery(sql, PermisosPantallas.class);
            query.setParameter(1, secPerfil);
            List<PermisosPantallas> lista = query.getResultList();
            return lista;
        } catch (Exception e) {
            log.error("PersistenciaPermisosPantallas.consultarPermisosPorPerfil():  ", e);
            return null;
        }
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public List<PermisosPantallas> consultarPermisosPorPerfil(EntityManager em) {
        try {
            em.clear();
            String sql = "SELECT * FROM PERMISOSPANTALLAS";
            Query query = em.createNativeQuery(sql, PermisosPantallas.class);
            List<PermisosPantallas> permisoPantallas = query.getResultList();
            return permisoPantallas;
        } catch (Exception e) {
            log.error("Error: PersistenciaPermisosPantallas consultarObjetosBloques ERROR  ", e);
            return null;
        }
    }

    @Override
    public Integer conteo(EntityManager em, BigInteger secPerfil, BigInteger secObjeto) {
        Integer retorno = new Integer("-1");
        try {
            em.clear();
            String sql = "SELECT COUNT(*) FROM PermisosPantallas pp WHERE Perfil = ? and Objetofrm = ?";
            Query query = em.createNativeQuery(sql);
            query.setParameter(1, secPerfil);
            query.setParameter(2, secObjeto);
            retorno = new Integer(query.getSingleResult().toString());
            return retorno;

        } catch (Exception e) {
            log.error("Error: PersistenciaPermisosPantallas consultarObjetosBloques ERROR  ", e);
            return null;
        }

    }
}
