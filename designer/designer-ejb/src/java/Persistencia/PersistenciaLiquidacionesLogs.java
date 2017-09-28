/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import InterfacePersistencia.PersistenciaLiquidacionesLogsInterface;
import Entidades.LiquidacionesLogs;
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
public class PersistenciaLiquidacionesLogs implements PersistenciaLiquidacionesLogsInterface {

    private static Logger log = Logger.getLogger(PersistenciaLiquidacionesLogs.class);

    public void crear(EntityManager em, LiquidacionesLogs liquidacionesLogs) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(liquidacionesLogs);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaLiquidacionesLogs.crear:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    public void editar(EntityManager em, LiquidacionesLogs liquidacionesLogs) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(liquidacionesLogs);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaLiquidacionesLogs.editar:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    public void borrar(EntityManager em, LiquidacionesLogs liquidacionesLogs) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(liquidacionesLogs));
            tx.commit();

        } catch (Exception e) {
            log.error("Error PersistenciaLiquidacionesLogs.borrar:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    public List<LiquidacionesLogs> consultarLiquidacionesLogs(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT l FROM LiquidacionesLogs l WHERE EXISTS (SELECT 'x' FROM Empleados e WHERE e.secuencia = l.empleado.secuencia)");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<LiquidacionesLogs> listMotivosDemandas = query.getResultList();
            return listMotivosDemandas;
        } catch (Exception e) {
            log.error("PERSISTENCIALIQUIDACIONESLOGS consultarLiquidacionesLogs ERROR :  ", e);
            return null;
        }
    }

    public List<LiquidacionesLogs> consultarLiquidacionesLogsPorEmpleado(EntityManager em, BigInteger secEmpleado) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT cce FROM LiquidacionesLogs cce WHERE cce.empleado.secuencia = :secuenciaEmpleado ORDER BY cce.empleado.persona.nombre ASC", LiquidacionesLogs.class);
            query.setParameter("secuenciaEmpleado", secEmpleado);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<LiquidacionesLogs> lista = query.getResultList();
            return lista;
        } catch (Exception e) {
            log.error("PERSISTENCIALIQUIDACIONESLOGS consultarLiquidacionesLogsPorEmpleado ERROR  ", e);
            return null;
        }
    }

    public List<LiquidacionesLogs> consultarLiquidacionesLogsPorOperando(EntityManager em, BigInteger secOperando) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT cce FROM LiquidacionesLogs cce WHERE cce.operando.secuencia = :secuenciaOperando ORDER BY cce.empleado.persona.nombre ASC");
            query.setParameter("secuenciaOperando", secOperando);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<LiquidacionesLogs> listaLiquidacionesLogsPorCiudad = query.getResultList();
            return listaLiquidacionesLogsPorCiudad;
        } catch (Exception e) {
            log.error("PERSISTENCIALIQUIDACIONESLOGS consultarLiquidacionesLogsPorOperando ERROR  ", e);
            return null;
        }
    }

    public List<LiquidacionesLogs> consultarLiquidacionesLogsPorProceso(EntityManager em, BigInteger secProceso) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT cce FROM LiquidacionesLogs cce WHERE cce.proceso.secuencia = :secuenciaProceso ORDER BY cce.empleado.persona.nombre ASC");
            query.setParameter("secuenciaProceso", secProceso);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<LiquidacionesLogs> listaLiquidacionesLogsPorCiudad = query.getResultList();
            return listaLiquidacionesLogsPorCiudad;
        } catch (Exception e) {
            log.error("PERSISTENCIALIQUIDACIONESLOGS consultarLiquidacionesLogsPorProceso ERROR  ", e);
            return null;
        }
    }

    @Override
    public Long getTotalRegistrosLiquidacionesLogsPorEmpleado(EntityManager em, BigInteger secEmpleado) {
        Long count;
        try {
            em.clear();
            Query query = em.createQuery("SELECT count(cce) FROM LiquidacionesLogs cce WHERE cce.empleado.secuencia = :secuenciaEmpleado");
            query.setParameter("secuenciaEmpleado", secEmpleado);
            List lista = query.getResultList();
            count = (Long) lista.get(0);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            return count;
        } catch (Exception e) {
            log.error("Error en getTotalRegistrosLiquidacionesLogsPorEmpleado :  ", e);
            count = Long.valueOf(0);
            return count;
        }
    }

    @Override
    public Long getTotalRegistrosBuscarLiquidacionesLogsPorOperando(EntityManager em, BigInteger secOperando) {
        Long count;
        try {
            em.clear();
            Query query = em.createQuery("SELECT count(cce) FROM LiquidacionesLogs cce WHERE cce.operando.secuencia = :secuenciaOperando");
            query.setParameter("secuenciaOperando", secOperando);

            List lista = query.getResultList();
            count = (Long) lista.get(0);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            return count;
        } catch (Exception e) {
            log.error("Error en getTotalRegistrosBuscarLiquidacionesLogsPorOperando :  ", e);
            count = Long.valueOf(0);
            return count;
        }
    }

    @Override
    public Long getTotalRegistrosBuscarLiquidacionesLogsPorProceso(EntityManager em, BigInteger secProceso) {
        Long count;
        try {
            em.clear();
            Query query = em.createQuery("SELECT count(cce) FROM LiquidacionesLogs cce WHERE cce.proceso.secuencia = :secuenciaProceso");
            query.setParameter("secuenciaProceso", secProceso);
            List lista = query.getResultList();
            count = (Long) lista.get(0);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            return count;
        } catch (Exception e) {
            log.error("Error en getTotalRegistrosBuscarLiquidacionesLogsPorProceso :  ", e);
            count = Long.valueOf(0);
            return count;
        }
    }

}
