/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.Errores;
import InterfacePersistencia.PersistenciaErroresInterfaz;
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
public class PersistenciaErrores implements PersistenciaErroresInterfaz {

    private static Logger log = Logger.getLogger(PersistenciaTiposAccidentes.class);

    @Override
    public String crear(EntityManager em, Errores tiposAnexos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(tiposAnexos);
            tx.commit();
            return "EXITO";
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            if (e instanceof PersistenceException || e instanceof SQLIntegrityConstraintViolationException || e instanceof DatabaseException) {
                log.error("Error PersistenciaErrores.crear:  ", e);
                return e.toString();
            } else {
                return "Ha ocurrido un error al Crear";
            }
        }
    }

    @Override
    public String editar(EntityManager em, Errores errores) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(errores);
            tx.commit();
            return "EXITO";
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            if (e instanceof PersistenceException || e instanceof SQLIntegrityConstraintViolationException || e instanceof DatabaseException) {
                log.error("Error PersistenciaErrores.editar:  ", e);
                return e.toString();
            } else {
                return "Ha ocurrido un error al Editar el Anexo";
            }
        }
    }

    @Override
    public String borrar(EntityManager em, Errores errores) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(errores));
            tx.commit();
            return "EXITO";
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            if (e instanceof PersistenceException || e instanceof SQLIntegrityConstraintViolationException || e instanceof DatabaseException) {
                log.error("Error PersistenciaErrores.borrar:  ", e);
                return e.toString();
            } else {
                return "Ha ocurrido un error al Borrar el Anexo";
            }
        }
    }

    @Override
    public Errores buscarErrores(EntityManager em, BigInteger secerrores) {
       try {
            em.clear();
            return em.find(Errores.class, secerrores);
        } catch (Exception e) {
            log.error("Error en la persistenciaErroresERROR :  ", e);
            return null;
        }
    }

    @Override
    public List<Errores> buscarErrores(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT ta FROM Errores  ta ORDER BY ta.codigo ASC");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<Errores> listErrores = (List<Errores>) query.getResultList();
            return listErrores;

        } catch (Exception e) {
            log.error("PersistenciaErrores.buscarErrores():  ", e);
            return null;
        }
    }

}
