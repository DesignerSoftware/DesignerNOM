/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.AportesCorrecciones;
import InterfacePersistencia.PersistenciaAportesCorreccionesInterface;
import java.math.BigInteger;
import java.util.Date;
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
public class PersistenciaAportesCorrecciones implements PersistenciaAportesCorreccionesInterface {

   private static Logger log = Logger.getLogger(PersistenciaAportesCorrecciones.class);

    @Override
    public void crear(EntityManager em, AportesCorrecciones aportesCorrecciones) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(aportesCorrecciones);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaAportesCorrecciones.crear :  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, AportesCorrecciones aportesCorrecciones) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(aportesCorrecciones);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaAportesCorrecciones.editar :  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, AportesCorrecciones aportesCorrecciones) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(aportesCorrecciones));
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaAportesCorrecciones.borrar :  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public List<AportesCorrecciones> consultarAportesEntidades(EntityManager em) {
        try {
            em.clear();
            String sql = " select * from aportescorrecciones a where exists (select 'x' from empleados e where e.secuencia=a.empleado)";
            Query query = em.createNativeQuery(sql, AportesCorrecciones.class);
            List<AportesCorrecciones> aportesCorrecciones = query.getResultList();
            return aportesCorrecciones;
        } catch (Exception e) {
            log.error("Error PersistenciaAportesCorrecciones.consultarAportesEntidades :  ", e);
            return null;
        }
    }

    @Override
    public List<AportesCorrecciones> consultarLovAportesEntidades(EntityManager em) {
        try {
            em.clear();
            String sql = "SELECT E.SECUENCIA,A.ANO,A.MES, CONCAT(CONCAT(CONCAT(CONCAT(PRIMERAPELLIDO,' '),SEGUNDOAPELLIDO),' '),NOMBRE) NOMBREEMPLEADO, e.codigoempleado \n"
                    + " FROM  EMPLEADOS E, PERSONAS P,APORTESCORRECCIONES A \n"
                    + " WHERE E.PERSONA = P.SECUENCIA";
            Query query = em.createNativeQuery(sql, AportesCorrecciones.class);
            List<AportesCorrecciones> aportesCorrecciones = query.getResultList();
            return aportesCorrecciones;
        } catch (Exception e) {
            log.error("Error PersistenciaAportesCorrecciones.consultarLovAportesEntidades :  ", e);
            return null;
        }
    }

    @Override
    public void borrarAportesCorreccionesProcesoAutomatico(EntityManager em, BigInteger secEmpresa, short mes, short ano) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            String sqlQuery = "call APORTESENTIDADES_PKG.ELIMINARCORRECCION(?, ?, ?)";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, ano);
            query.setParameter(2, mes);
            query.setParameter(3, secEmpresa);
            query.executeUpdate();
            tx.commit();
        } catch (Exception e) {
            log.error("Error en PersistenciaAportesCorrecciones.borrarAportesEntidadesProcesoAutomatico:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public String ejecutarPKGInsertarCorreccion(EntityManager em, Date fechaIni, Date fechaFin, BigInteger tipoTrabajador, BigInteger secEmpresa) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            String sqlQuery = "call APORTESENTIDADES_PKG.INSERTARCORRECCION(?, ?, ?, ?)";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, fechaIni);
            query.setParameter(2, fechaFin);
            query.setParameter(3, tipoTrabajador);
            query.setParameter(4, secEmpresa);
            query.executeUpdate();
            tx.commit();
            return "PROCESO_EXITOSO";
        } catch (Exception e) {
            log.error("Error en PersistenciaAportesCorrecciones.ejecutarPKGInsertarCorreccion:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
            return "ERROR_PERSISTENCIA";
        }
    }

    @Override
    public String ejecutarPKGActualizarNovedadesCorreccion(EntityManager em, BigInteger secEmpresa, short mes, short ano) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            String sqlQuery = "call APORTESENTIDADES_PKG.ACTUALIZARNOVEDADESCORRECCION(?, ?, ?)";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, ano);
            query.setParameter(2, mes);
            query.setParameter(3, secEmpresa);
            query.executeUpdate();
            tx.commit();
            return "PROCESO_EXITOSO";
        } catch (Exception e) {
            log.error("Error en PersistenciaAportesCorrecciones.ejecutarPKGActualizarNovedadesCorreccion:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
            return "ERROR_PERSISTENCIA";
        }
    }

    @Override
    public String ejecutarIncrementarCorreccion(EntityManager em, BigInteger secEmpresa, short mes, short ano) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            String sqlQuery = "call APORTESENTIDADES_PKG.INCREMENTARCORRECCION(?, ?, ?)";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, ano);
            query.setParameter(2, mes);
            query.setParameter(3, secEmpresa);
            query.executeUpdate();
            tx.commit();
            return "PROCESO_EXITOSO";
        } catch (Exception e) {
            log.error("Error en PersistenciaAportesCorrecciones.ejecutarIncrementarCorreccion:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
            return "ERROR_PERSISTENCIA";
        }
    }

    @Override
    public String ejecutarPKGIdentificaCorreccion(EntityManager em, BigInteger secEmpresa, short mes, short ano) {
         em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            String sqlQuery = "call APORTESENTIDADES_PKG.IDENTIFICACORRECCION(?, ?, ?)";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, ano);
            query.setParameter(2, mes);
            query.setParameter(3, secEmpresa);
            query.executeUpdate();
            tx.commit();
            return "PROCESO_EXITOSO";
        } catch (Exception e) {
            log.error("Error en PersistenciaAportesCorrecciones.ejecutarPKGIdentificaCorreccion:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
            return "ERROR_PERSISTENCIA";
        }
    }

}
