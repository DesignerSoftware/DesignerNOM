/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.TurnosEmpleados;
import InterfacePersistencia.PersistenciaTurnosEmpleadosInterface;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 *
 * @author Administrador
 */
@Stateless
public class PersistenciaTurnosEmpleados implements PersistenciaTurnosEmpleadosInterface {

    @Override
    public Date obtenerFechaInicialMinimaTurnosEmpleados(EntityManager em) {
        try {
            em.clear();
            String sql = "select  min(fechainicial) from turnosempleados where procesado='N'";
            Query query = em.createNativeQuery(sql);
            Date fecha = (Date) query.getSingleResult();
            return fecha;
        } catch (Exception e) {
            System.out.println("Error PersistenciaTurnosEmpleados.obtenerFechaInicialMinimaTurnosEmpleados : " + e.toString());
            return null;
        }
    }

    @Override
    public Date obtenerFechaFinalMaximaTurnosEmpleados(EntityManager em) {
        try {
            em.clear();
            String sql = "select  max(fechafinal) from turnosempleados where procesado='N'";
            Query query = em.createNativeQuery(sql);
            Date fecha = (Date) query.getSingleResult();
            return fecha;
        } catch (Exception e) {
            System.out.println("Error PersistenciaTurnosEmpleados.obtenerFechaFinalMaximaTurnosEmpleados : " + e.toString());
            return null;
        }
    }

    @Override
    public int ejecutarPKG_CONTARNOVEDADESLIQ(EntityManager em, Date fechaDesde, Date fechaHasta, BigDecimal emplDesde, BigDecimal emplHasta) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            String sql = "call TURNOSEMPLEADOS_PKG.CONTARNOVEDADESLIQ(?,?,?,?)";
            Query query = em.createNativeQuery(sql);
            query.setParameter(1, fechaDesde);
            query.setParameter(2, fechaHasta);
            query.setParameter(3, emplDesde);
            query.setParameter(4, emplHasta);
            int dato = query.executeUpdate();
            tx.commit();
            return dato;
        } catch (Exception e) {
            System.out.println("Error PersistenciaTurnosEmpleados.EjecutarPKG_CONTARNOVEDADESLIQ : " + e.toString());
            if (tx.isActive()) {
                tx.rollback();
            }
            return -1;
        }
    }

    @Override
    public void ejecutarPKG_ELIMINARLIQUIDACION(EntityManager em, Date fechaDesde, Date fechaHasta, BigDecimal emplDesde, BigDecimal emplHasta) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            String sql = "call TURNOSEMPLEADOS_PKG.ELIMINARLIQUIDACION(?,?,?,?)";
            Query query = em.createNativeQuery(sql);
            query.setParameter(1, fechaDesde);
            query.setParameter(2, fechaHasta);
            query.setParameter(3, emplDesde);
            query.setParameter(4, emplHasta);
            query.executeUpdate();
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaTurnosEmpleados.EjecutarPKG_ELIMINARLIQUIDACION : " + e.toString());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void crear(EntityManager em, TurnosEmpleados turnosEmpleados) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(turnosEmpleados);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaTurnosEmpleados.crear: " + e.toString());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, TurnosEmpleados turnosEmpleados) {

        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(turnosEmpleados);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaTurnosEmpleados.editar: " + e.toString());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, TurnosEmpleados turnosEmpleados) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(turnosEmpleados));
            tx.commit();

        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            System.out.println("Error PersistenciaTurnosEmpleados.borrar: " + e.toString());
        }
    }

    @Override
    public List<TurnosEmpleados> buscarTurnosEmpleadosPorEmpleado(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT t FROM TurnosEmpleados t WHERE t.empleado.secuencia =:secuencia");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            query.setParameter("secuencia", secuencia);
            List<TurnosEmpleados> lista = query.getResultList();
            return lista;
        } catch (Exception e) {
            System.out.println("Error PersistenciaTurnosEmpleados PersistenciaEmpleados : " + e.toString());
            return null;
        }
    }
}
