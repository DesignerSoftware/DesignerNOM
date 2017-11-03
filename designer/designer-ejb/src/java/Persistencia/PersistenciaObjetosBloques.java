/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.BloquesPantallas;
import Entidades.ObjetosBloques;
import InterfacePersistencia.PersistenciaObjetosBloquesInterface;
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
public class PersistenciaObjetosBloques implements PersistenciaObjetosBloquesInterface {

    private static Logger log = Logger.getLogger(PersistenciaObjetosDB.class);

    @Override
    public List<ObjetosBloques> consultarObjetosBloques(EntityManager em) {
        try {
            em.clear();
            String sql = "SELECT * FROM OBJETOSBLOQUES";
            Query query = em.createNativeQuery(sql, ObjetosBloques.class);
            List<ObjetosBloques> todosObjetos = query.getResultList();
            return todosObjetos;
        } catch (Exception e) {
            log.error("Error: PersistenciaObjetosBloques consultarObjetosBloques ERROR  ", e);
            return null;
        }
    }

    @Override
    public List<BloquesPantallas> consultarBloquesPantallas(EntityManager em) {
        try {
            em.clear();
            String sql="SELECT * FROM BLOQUESPANTALLAS";
            Query query = em.createNativeQuery(sql,BloquesPantallas.class);
            List<BloquesPantallas> listaBloquesPantallas = query.getResultList();
            return listaBloquesPantallas;
        } catch (Exception e) {
            log.error("Error: PersistenciaObjetosBloques consultarBloquesPantallas ERROR  ", e);
            return null;
        }
    }

    @Override
    public String crear(EntityManager em, ObjetosBloques objeto) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(objeto);
            tx.commit();
            return "EXITO";
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            if (e instanceof PersistenceException || e instanceof SQLIntegrityConstraintViolationException || e instanceof DatabaseException) {
                log.error("Error PersistenciaObjetosBloques.crear:  ", e);
                return e.toString();
            } else {
                return "Ha ocurrido un error al Crear el ObjetoBloque";
            }
        }
    }

    @Override
    public String editar(EntityManager em, ObjetosBloques objeto) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(objeto);
            tx.commit();
            return "EXITO";
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            if (e instanceof PersistenceException || e instanceof SQLIntegrityConstraintViolationException || e instanceof DatabaseException) {
                log.error("Error PersistenciaObjetosBloques.editar:  ", e);
                return e.toString();
            } else {
                return "Ha ocurrido un error al Editar el ObjetoBloque";
            }
        }
    }

    @Override
    public String borrar(EntityManager em, ObjetosBloques objeto) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(objeto));
            tx.commit();
            return "EXITO";
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            if (e instanceof PersistenceException || e instanceof SQLIntegrityConstraintViolationException || e instanceof DatabaseException) {
                log.error("Error PersistenciaObjetosBloques.borrar:  ", e);
                return e.toString();
            } else {
                return "Ha ocurrido un error al Borrar el ObjetoBloque";
            }
        }
    }

}
