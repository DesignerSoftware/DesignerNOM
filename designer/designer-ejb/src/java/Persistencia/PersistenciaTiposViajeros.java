/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.Tiposviajeros;
import InterfacePersistencia.PersistenciaTiposViajerosInterface;
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
public class PersistenciaTiposViajeros implements PersistenciaTiposViajerosInterface {

   private static Logger log = Logger.getLogger(PersistenciaTiposViajeros.class);

    @Override
    public void crear(EntityManager em, Tiposviajeros subCategorias) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(subCategorias);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaTiposViajeros.crear:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, Tiposviajeros subCategorias) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(subCategorias);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaTiposViajeros.editar:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, Tiposviajeros subCategorias) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(subCategorias));
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaTiposViajeros.borrar:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public List<Tiposviajeros> consultarTiposViajeros(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT l FROM Tiposviajeros  l ORDER BY l.codigo ASC ");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<Tiposviajeros> listTiposViajeros = query.getResultList();
            return listTiposViajeros;
        } catch (Exception e) {
            log.error("ERROR PersistenciaTiposViajeros ConsultarTiposViajeros ERROR :  ", e);
            return null;
        }
    }

    @Override
    public Tiposviajeros consultarSubCategoria(EntityManager em, BigInteger secSubCategoria) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT sc FROM Tiposviajeros sc WHERE sc.secuencia=:secuencia");
            query.setParameter("secuencia", secSubCategoria);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            Tiposviajeros subCategorias = (Tiposviajeros) query.getSingleResult();
            return subCategorias;
        } catch (Exception e) {
            log.error("PersistenciaTiposViajeros.consultarSubCategoria():  ", e);
            return null;
        }
    }

    @Override
    public BigInteger contarVigenciasViajerosTipoViajero(EntityManager em, BigInteger secuencia) {
        BigInteger retorno = new BigInteger("-1");
        try {
            em.clear();
            String sqlQuery = "SELECT COUNT(*)FROM vigenciasviajeros WHERE tipoviajero = ?";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            retorno = new BigInteger(query.getSingleResult().toString());
            log.error("Contador PersistenciaTiposViajeros contarVigenciasViajeros persistencia " + retorno);
            return retorno;
        } catch (Exception e) {
            log.error("Error PersistenciaTiposViajeros contarVigenciasViajeros.  ", e);
            return retorno;
        }
    }

    @Override
    public BigInteger contarTiposLegalizacionesTipoViajero(EntityManager em, BigInteger secuencia) {
        BigInteger retorno = new BigInteger("-1");
        try {
            em.clear();
            String sqlQuery = "SELECT COUNT(*)FROM tiposlegalizaciones WHERE tipoviajero = ?";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            retorno = new BigInteger(query.getSingleResult().toString());
            log.warn("Contador PersistenciaTiposViajeros contarTiposLegalizaciones persistencia " + retorno);
            return retorno;
        } catch (Exception e) {
            log.error("Error PersistenciaTiposViajeros contarTiposLegalizaciones.  ", e);
            return retorno;
        }
    }
}
