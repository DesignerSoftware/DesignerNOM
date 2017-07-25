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
            log.error("Error PersistenciaLiquidacionesLogs.crear: " + e.getMessage());
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
            log.error("Error PersistenciaLiquidacionesLogs.editar: " + e.getMessage());
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
        log.error("Error PersistenciaLiquidacionesLogs.borrar: " + e.getMessage());
                if (tx.isActive()) {
                    tx.rollback();
                }
        }
    }

    public List<LiquidacionesLogs> consultarLiquidacionesLogs(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT l FROM LiquidacionesLogs l WHERE EXISTS (SELECT 'x' FROM Empleados e WHERE e.secuencia = l.empleado.secuencia)");
//            Query query = em.createQuery("SELECT l FROM LiquidacionesLogs l");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<LiquidacionesLogs> listMotivosDemandas = query.getResultList();
            return listMotivosDemandas;
        } catch (Exception e) {
            log.error("PERSISTENCIALIQUIDACIONESLOGS consultarLiquidacionesLogs ERROR : " + e.getMessage());
            return null;
        }
    }

    public List<LiquidacionesLogs> consultarLiquidacionesLogsPorEmpleado(EntityManager em, BigInteger secEmpleado) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT cce FROM LiquidacionesLogs cce WHERE cce.empleado.secuencia = :secuenciaEmpleado ORDER BY cce.empleado.persona.nombre ASC");
            query.setParameter("secuenciaEmpleado", secEmpleado);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<LiquidacionesLogs> listaLiquidacionesLogsPorCiudad = query.getResultList();
            return listaLiquidacionesLogsPorCiudad;
        } catch (Exception e) {
            log.error("PERSISTENCIALIQUIDACIONESLOGS consultarLiquidacionesLogsPorEmpleado ERROR " + e.getMessage());
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
            log.error("PERSISTENCIALIQUIDACIONESLOGS consultarLiquidacionesLogsPorOperando ERROR " + e.getMessage());
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
            log.error("PERSISTENCIALIQUIDACIONESLOGS consultarLiquidacionesLogsPorProceso ERROR " + e.getMessage());
            return null;
        }
    }

}
