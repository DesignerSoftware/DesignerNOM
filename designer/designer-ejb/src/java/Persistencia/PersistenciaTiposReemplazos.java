/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.TiposReemplazos;
import InterfacePersistencia.PersistenciaTiposReemplazosInterface;
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
public class PersistenciaTiposReemplazos implements PersistenciaTiposReemplazosInterface {

    private static Logger log = Logger.getLogger(PersistenciaTiposReemplazos.class);

    @Override
    public String crear(EntityManager em, TiposReemplazos tiposReemplazos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(tiposReemplazos);
            tx.commit();
            return "EXITO";
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            if (e instanceof PersistenceException || e instanceof SQLIntegrityConstraintViolationException || e instanceof DatabaseException) {
                log.error("Error PersistenciaTiposReemplazos.crear:  ", e);
                return e.toString();
            } else {
                return "Ha ocurrido un error al crear el Tipo Reemplazo";
            }
        }
    }

    @Override
    public String editar(EntityManager em, TiposReemplazos tiposReemplazos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(tiposReemplazos);
            tx.commit();
            return "EXITO";
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            if (e instanceof PersistenceException || e instanceof SQLIntegrityConstraintViolationException || e instanceof DatabaseException) {
                log.error("Error PersistenciaTiposReemplazos.editar:  ", e);
                return e.toString();
            } else {
                return "Ha ocurrido un error al crear el editar Reemplazo";
            }
        }
    }

    @Override
    public String borrar(EntityManager em, TiposReemplazos tiposReemplazos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(tiposReemplazos));
            tx.commit();
            return "EXITO";
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            if (e instanceof PersistenceException || e instanceof SQLIntegrityConstraintViolationException || e instanceof DatabaseException) {
                log.error("Error PersistenciaTiposReemplazos.borrar:  ", e);
                return e.toString();
            } else {
                return "Ha ocurrido un error al borrar el Tipo Reemplazo";
            }
        }
    }

    @Override
    public TiposReemplazos buscarTipoReemplazo(EntityManager em, BigInteger secuenciaTR) {
        try {
            em.clear();
            return em.find(TiposReemplazos.class, secuenciaTR);
        } catch (Exception e) {
            log.error("PersistenciaTiposReemplazos.buscarTipoReemplazo():  ", e);
            return null;
        }
    }

    @Override
    public List<TiposReemplazos> buscarTiposReemplazos(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT tR FROM TiposReemplazos tR ORDER BY tr.codigo");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<TiposReemplazos> tiposReemplazos = query.getResultList();
            return tiposReemplazos;
        } catch (Exception e) {
            log.error("PersistenciaTiposReemplazos.buscarTiposReemplazos():  ", e);
            return null;
        }
    }

    @Override
    public BigInteger contadorEncargaturas(EntityManager em, BigInteger secuencia) {
        BigInteger retorno = new BigInteger("-1");
        try {
            em.clear();
            String sqlQuery = " SELECT COUNT(*) FROM encargaturas WHERE tiporeemplazo = ?";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            retorno = new BigInteger(query.getSingleResult().toString());
            return retorno;
        } catch (Exception e) {
            log.error("PersistenciaTiposReemplazos.contadorEncargaturas():  ", e);
            return retorno;
        }
    }

    @Override
    public BigInteger contadorProgramacionesTiempos(EntityManager em, BigInteger secuencia) {
        BigInteger retorno = new BigInteger("-1");
        try {
            em.clear();
            String sqlQuery = "SELECT COUNT(*) FROM programacionestiempos  WHERE tiporeemplazo = ?";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            retorno = new BigInteger(query.getSingleResult().toString());
            return retorno;
        } catch (Exception e) {
            log.error("PersistenciaTiposReemplazos.contadorProgramacionesTiempos():  ", e);
            return retorno;
        }
    }

    @Override
    public BigInteger contadorReemplazos(EntityManager em, BigInteger secuencia) {
        BigInteger retorno = new BigInteger("-1");
        try {
            em.clear();
            String sqlQuery = "SELECT COUNT(*) FROM reemplazos WHERE tiporeemplazo = ?";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            retorno = new BigInteger(query.getSingleResult().toString());
            return retorno;
        } catch (Exception e) {
            log.error("PersistenciaTiposReemplazos.contadorReemplazos():  ", e);
            return retorno;
        }
    }
}
