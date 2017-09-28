/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.TiposConclusiones;
import InterfacePersistencia.PersistenciaTiposConclusionesInterface;
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
public class PersistenciaTiposConclusiones implements PersistenciaTiposConclusionesInterface {

   private static Logger log = Logger.getLogger(PersistenciaTiposConclusiones.class);

    public void crear(EntityManager em, TiposConclusiones tiposConclusiones) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(tiposConclusiones);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaTiposConclusiones.crear:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    public void editar(EntityManager em, TiposConclusiones tiposConclusiones) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(tiposConclusiones);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaTiposConclusiones.editar:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    public void borrar(EntityManager em, TiposConclusiones tiposConclusiones) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(tiposConclusiones));
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaTiposConclusiones.borrar:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    public List<TiposConclusiones> consultarTiposConclusiones(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT t FROM TiposConclusiones t ORDER BY t.codigo  ASC");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<TiposConclusiones> tiposConclusiones = query.getResultList();
            return tiposConclusiones;
        } catch (Exception e) {
            log.error("PersistenciaTiposConclusiones.consultarTiposConclusiones():  ", e);
            return null;
        }
    }

    public TiposConclusiones consultarTipoConclusion(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT t FROM TiposConclusiones t WHERE t.secuencia =:secuencia");
            query.setParameter("secuencia", secuencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            TiposConclusiones tiposConclusiones = (TiposConclusiones) query.getSingleResult();
            return tiposConclusiones;
        } catch (Exception e) {
            log.error("PersistenciaTiposConclusiones.consultarTipoConclusion():  ", e);
            return null;
        }
    }

    public BigInteger contarChequeosMedicosTipoConclusion(EntityManager em, BigInteger secuencia) {
        BigInteger retorno = new BigInteger("-1");
        try {
            em.clear();
            String sqlQuery = "SELECT COUNT(*)FROM chequeosmedicos WHERE tipochequeo = ?";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            retorno = new BigInteger(query.getSingleResult().toString());
            return retorno;
        } catch (Exception e) {
            log.error("Error PersistenciaTiposConclusiones contarChequeosMedicosTipoConclusion ERROR :  ", e);
            return retorno;
        }
    }
}
