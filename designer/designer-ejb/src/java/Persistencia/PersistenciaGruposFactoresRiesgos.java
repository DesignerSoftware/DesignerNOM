/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.GruposFactoresRiesgos;
import InterfacePersistencia.PersistenciaGruposFactoresRiesgosInterface;
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
public class PersistenciaGruposFactoresRiesgos implements PersistenciaGruposFactoresRiesgosInterface {

   private static Logger log = Logger.getLogger(PersistenciaGruposFactoresRiesgos.class);

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
   /* @PersistenceContext(unitName = "DesignerRHN-ejbPU")
    private EntityManager em;*/

    public void crear(EntityManager em,GruposFactoresRiesgos grupoFactoresRiesgos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(grupoFactoresRiesgos);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaGruposFactoresRiesgos.crear:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    public void editar(EntityManager em,GruposFactoresRiesgos grupoFactoresRiesgos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(grupoFactoresRiesgos);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            log.error("Error PersistenciaGruposFactoresRiesgos.editar:  ", e);
        }
    }

    public void borrar(EntityManager em,GruposFactoresRiesgos grupoFactoresRiesgos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(grupoFactoresRiesgos));
            tx.commit();
        } catch (Exception e) {
                if (tx.isActive()) {
                    tx.rollback();
                }
                log.error("Error PersistenciaGruposFactoresRiesgos.borrar:  ", e);
        }
    }

    public List<GruposFactoresRiesgos> consultarGruposFactoresRiesgos(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT t FROM GruposFactoresRiesgos t ORDER BY t.codigo  ASC");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<GruposFactoresRiesgos> evalActividades = query.getResultList();
            return evalActividades;
        } catch (Exception e) {
            log.error("Error buscarGruposFactoresRiesgos ERROR:  ", e);
            return null;
        }
    }

    public GruposFactoresRiesgos consultarGrupoFactorRiesgo(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT t FROM GruposFactoresRiesgos t WHERE t.secuencia =:secuencia");
            query.setParameter("secuencia", secuencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            GruposFactoresRiesgos grupoFactoresRiesgos = (GruposFactoresRiesgos) query.getSingleResult();
            return grupoFactoresRiesgos;
        } catch (Exception e) {
            log.error("Error buscarGrupoFactorRiesgoSecuencia  ", e);
            GruposFactoresRiesgos grupoFactoresRiesgos = null;
            return grupoFactoresRiesgos;
        }
    }

    public BigInteger contarSoProActividadesGrupoFactorRiesgo(EntityManager em, BigInteger secuencia) {
        BigInteger retorno = new BigInteger("-1");
        try {
            em.clear();
            String sqlQuery = "SELECT COUNT(*)FROM soprogactividades WHERE factorriesgo = ?";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            retorno = new BigInteger(query.getSingleResult().toString());
            log.warn("Contador PersistenciaGruposFactoresRiesgos contarSoProActividadesGrupoFactorRiesgo Retorno " + retorno);
            return retorno;
        } catch (Exception e) {
            log.error("Error PersistenciaGruposFactoresRiesgos contarSoProActividadesGrupoFactorRiesgo ERROR :  ", e);
            return retorno;
        }
    }

    public BigInteger contarSoIndicadoresGrupoFactorRiesgo(EntityManager em, BigInteger secuencia) {
        BigInteger retorno = new BigInteger("-1");
        try {
            em.clear();
            String sqlQuery = "SELECT COUNT(*)FROM soindicadores WHERE factorriesgo = ?";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            retorno = new BigInteger(query.getSingleResult().toString());
            log.warn("Contador PersistenciaGruposFactoresRiesgos contarSoIndicadoresGrupoFactorRiesgo Retorno " + retorno);
            return retorno;
        } catch (Exception e) {
            log.error("Error PersistenciaGruposFactoresRiesgos contarSoIndicadoresGrupoFactorRiesgo ERROR :  ", e);
            return retorno;
        }
    }

    public BigInteger contarFactoresRiesgoGrupoFactorRiesgo(EntityManager em, BigInteger secuencia) {
        BigInteger retorno = new BigInteger("-1");
        try {
            em.clear();
            String sqlQuery = "SELECT COUNT(*)FROM factoresriesgos WHERE grupo = ?";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            retorno = new BigInteger(query.getSingleResult().toString());
            log.warn("Contador PersistenciaGruposFactoresRiesgos contarFactoresRiesgoGrupoFactorRiesgo Retorno " + retorno);
            return retorno;
        } catch (Exception e) {
            log.error("Error PersistenciaGruposFactoresRiesgos contarFactoresRiesgoGrupoFactorRiesgo ERROR :  ", e);
            return retorno;
        }
    }
}
