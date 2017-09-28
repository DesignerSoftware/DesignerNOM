/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.TiposAccidentes;
import InterfacePersistencia.PersistenciaTiposAccidentesInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 *
 * @author user
 */
@Stateless
public class PersistenciaTiposAccidentes implements PersistenciaTiposAccidentesInterface {

   private static Logger log = Logger.getLogger(PersistenciaTiposAccidentes.class);

    public void crear(EntityManager em, TiposAccidentes tiposAccidentes) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(tiposAccidentes);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaTiposAccidentes.crear:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    public void editar(EntityManager em, TiposAccidentes tiposAccidentes) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(tiposAccidentes);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaTiposAccidentes.crear:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    public void borrar(EntityManager em, TiposAccidentes tiposAccidentes) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(tiposAccidentes));
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaTiposAccidentes.crear:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    public TiposAccidentes buscarTipoAccidente(EntityManager em, BigInteger secuenciaTA) {
        try {
            em.clear();
            return em.find(TiposAccidentes.class, secuenciaTA);
        } catch (Exception e) {
            log.error("PersistenciaTiposAccidentes.buscarTipoAccidente():  ", e);
            return null;
        }
    }

    public List<TiposAccidentes> buscarTiposAccidentes(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT l FROM TiposAccidentes  l ORDER BY l.codigo ASC ");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<TiposAccidentes> listPartesCuerpo = query.getResultList();
            return listPartesCuerpo;
        } catch (Exception e) {
            log.error("PersistenciaTiposAccidentes.buscarTiposAccidentes():  ", e);
            return null;
        }
    }

    public BigInteger contadorSoAccidentesMedicos(EntityManager em, BigInteger secuencia) {
        BigInteger retorno = new BigInteger("-1");
        try {
            em.clear();
            String sqlQuery = "SELECT COUNT(*) FROM soaccidentesmedicos WHERE tipoaccidente = ?";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            retorno = (BigInteger) new BigInteger(query.getSingleResult().toString());
            return retorno;
        } catch (Exception e) {
            log.error("PersistenciaTiposAccidentes.contadorSoAccidentesMedicos():  ", e);
            return retorno;
        }
    }

    public BigInteger contadorAccidentes(EntityManager em, BigInteger secuencia) {
        BigInteger retorno = new BigInteger("-1");
        try {
            em.clear();
            String sqlQuery = "SELECT COUNT(*) FROM accidentes WHERE tipoaccidente = ?";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            retorno = (BigInteger) new BigInteger(query.getSingleResult().toString());
            return retorno;
        } catch (Exception e) {
            log.error("PersistenciaTiposAccidentes.contadorAccidentes():  ", e);
            return retorno;
        }
    }

}
