/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.TiposIndices;
import InterfacePersistencia.PersistenciaTiposIndicesInterface;
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

@Stateless
public class PersistenciaTiposIndices implements PersistenciaTiposIndicesInterface {

    private static Logger log = Logger.getLogger(PersistenciaTiposIndices.class);

    public String crear(EntityManager em, TiposIndices tiposIndices) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(tiposIndices);
            tx.commit();
            return "EXITO";
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            if (e instanceof PersistenceException || e instanceof SQLIntegrityConstraintViolationException || e instanceof DatabaseException) {
                log.error("Error PersistenciaTiposIndices.crear:  ", e);
                return e.toString();
            } else {
                return "Ha ocurrido un error al crear el Tipo Indice";
            }
        }
    }

    public String editar(EntityManager em, TiposIndices tiposIndices) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(tiposIndices);
            tx.commit();
            return "EXITO";
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            if (e instanceof PersistenceException || e instanceof SQLIntegrityConstraintViolationException || e instanceof DatabaseException) {
                log.error("Error PersistenciaTiposIndices.editar:  ", e);
                return e.toString();
            } else {
                return "Ha ocurrido un error al editar el Tipo Indice";
            }
        }
    }

    public String borrar(EntityManager em, TiposIndices tiposIndices) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(tiposIndices));
            tx.commit();
            return "EXITO";
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            if (e instanceof PersistenceException || e instanceof SQLIntegrityConstraintViolationException || e instanceof DatabaseException) {
                log.error("Error PersistenciaTiposIndices.borrar:  ", e);
                return e.toString();
            } else {
                return "Ha ocurrido un error al borrar el Tipo Indice";
            }
        }
    }

    public List<TiposIndices> consultarTiposIndices(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT t FROM TiposIndices t ORDER BY t.codigo  ASC");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<TiposIndices> evalActividades = query.getResultList();
            return evalActividades;
        } catch (Exception e) {
            log.error("PersistenciaTiposIndices.consultarTiposIndices():  ", e);
            return null;
        }
    }

    public TiposIndices consultarTipoIndice(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT t FROM TiposIndices t WHERE t.secuencia =:secuencia");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            query.setParameter("secuencia", secuencia);
            TiposIndices tiposIndices = (TiposIndices) query.getSingleResult();
            return tiposIndices;
        } catch (Exception e) {
            log.error("PersistenciaTiposIndices.consultarTipoIndice():  ", e);
            return null;
        }
    }

    public BigInteger contarIndicesTipoIndice(EntityManager em, BigInteger secuencia) {
        BigInteger retorno = new BigInteger("-1");
        try {
            em.clear();
            String sqlQuery = "SELECT COUNT(*)FROM indices WHERE tipoindice = ?";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            retorno = new BigInteger(query.getSingleResult().toString());
            log.warn("Contador PersistenciaTiposIndices contarIndicesTipoIndice Retorno " + retorno);
            return retorno;
        } catch (Exception e) {
            log.error("Error PersistenciaTiposIndices contarIndicesTipoIndice ERROR :  ", e);
            return retorno;
        }
    }
}
