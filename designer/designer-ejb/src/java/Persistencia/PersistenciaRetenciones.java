/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.Retenciones;
import InterfacePersistencia.PersistenciaRetencionesInterface;
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
public class PersistenciaRetenciones implements PersistenciaRetencionesInterface {

    private static Logger log = Logger.getLogger(PersistenciaRetenciones.class);

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
//    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
//    private EntityManager em;
    @Override
    public String crear(EntityManager em, Retenciones retenciones) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(retenciones);
            tx.commit();
            return "EXITO";
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            if (e instanceof PersistenceException || e instanceof SQLIntegrityConstraintViolationException || e instanceof DatabaseException) {
                log.error("Error PersistenciaRetenciones.crear:  ", e);
                return e.getMessage();
            } else {
                return "Ha ocurrido un error al crear la Retención";
            }
        }
    }

    @Override
    public String editar(EntityManager em, Retenciones retenciones) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(retenciones);
            tx.commit();
            return "EXITO";
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            if (e instanceof PersistenceException || e instanceof SQLIntegrityConstraintViolationException || e instanceof DatabaseException) {
                log.error("Error PersistenciaRetenciones.editar:  ", e);
                return e.getMessage();
            } else {
                return "Ha ocurrido un error al editar la  Retención";
            }
        }
    }

    @Override
    public String borrar(EntityManager em, Retenciones retenciones) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(retenciones));
            tx.commit();
            return "EXITO";
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            if (e instanceof PersistenceException || e instanceof SQLIntegrityConstraintViolationException || e instanceof DatabaseException) {
                log.error("Error PersistenciaRetenciones.borrar:  ", e);
                return e.getMessage();
            } else {
                return "Ha ocurrido un error al borrar la Retención";
            }
        }
    }

    @Override
    public List<Retenciones> buscarRetenciones(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT r FROM Retenciones r");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<Retenciones> setsLista = (List<Retenciones>) query.getResultList();
            return setsLista;
        } catch (Exception e) {
            log.error("PersistenciaRetenciones.buscarRetenciones():  ", e);
            return null;
        }
    }

    @Override
    public List<Retenciones> buscarRetencionesVig(EntityManager em, BigInteger secRetencion) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT r FROM Retenciones r WHERE r.vigencia.secuencia = :secRetencion");
            query.setParameter("secRetencion", secRetencion);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<Retenciones> retenciones = query.getResultList();
            return retenciones;
        } catch (Exception e) {
            log.error("PersistenciaRetenciones.buscarRetencionesVig():  ", e);
            return null;
        }
    }
}
