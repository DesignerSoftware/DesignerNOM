/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.TiposAnexos;
import InterfacePersistencia.PersistenciaTiposAnexosInterface;
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
public class PersistenciaTiposAnexos implements PersistenciaTiposAnexosInterface {

    private static Logger log = Logger.getLogger(PersistenciaTiposAccidentes.class);

    @Override
    public String crear(EntityManager em, TiposAnexos tiposAnexos) {
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
                log.error("Error PersistenciaTiposAnexos.crear:  ", e);
                return e.toString();
            } else {
                return "Ha ocurrido un error al Crear el Anexo";
            }
        }
    }

    @Override
    public String editar(EntityManager em, TiposAnexos tiposAnexos) {
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
                log.error("Error PersistenciaTiposAnexos.editar:  ", e);
                return e.toString();
            } else {
                return "Ha ocurrido un error al Editar el Anexo";
            }
        }
    }

    @Override
    public String borrar(EntityManager em, TiposAnexos tiposAnexos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(tiposAnexos));
            tx.commit();
            return "EXITO";
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            if (e instanceof PersistenceException || e instanceof SQLIntegrityConstraintViolationException || e instanceof DatabaseException) {
                log.error("Error PersistenciaTiposAnexos.borrar:  ", e);
                return e.toString();
            } else {
                return "Ha ocurrido un error al Borrar el Anexo";
            }
        }
    }

    @Override
    public TiposAnexos buscarTiposAnexos(EntityManager em, BigInteger sectiposAnexos) {
        try {
            em.clear();
            return em.find(TiposAnexos.class, sectiposAnexos);
        } catch (Exception e) {
            log.error("Error en la persistenciaTiposAnexosERROR :  ", e);
            return null;
        }
    }

    @Override
    public List<TiposAnexos> buscarTiposAnexos(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT ta FROM TiposAnexos  ta ORDER BY ta.codigo ASC");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<TiposAnexos> listTipoAnexo = (List<TiposAnexos>) query.getResultList();
            return listTipoAnexo;

        } catch (Exception e) {
            log.error("PersistenciaTiposAnexos.buscarTiposAnexos():  ", e);
            return null;
        }
    }

    @Override
    public BigInteger contadorAnexos(EntityManager em, BigInteger sectiposAnexos) {
        BigInteger retorno = new BigInteger("-1");
        try {
            em.clear();
            String sqlQuery = "SELECT COUNT(*) FROM TiposAnexos WHERE tipoanexo = ?";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, sectiposAnexos);
            retorno = (BigInteger) new BigInteger(query.getSingleResult().toString());
            return retorno;
        } catch (Exception e) {
            log.error("PersistenciaTiposAnexos.contadorAnexos():  ", e);
            return retorno;
        }
    }

}
