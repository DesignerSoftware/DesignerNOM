/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import InterfacePersistencia.PersistenciaErroresLiquidacionesInterface;
import Entidades.ErroresLiquidacion;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 *
 * @author user
 */
@Stateless
public class PersistenciaErroresLiquidaciones implements PersistenciaErroresLiquidacionesInterface {

    public List<ErroresLiquidacion> consultarErroresLiquidacionPorEmpleado(EntityManager em, BigInteger semEmpleado) {
        try {
            em.clear();
            String SqlQuery = "SELECT * FROM erroresliquidacion e WHERE e.empleado = NVL(?,e.empleado)";
            Query query = em.createNativeQuery(SqlQuery, ErroresLiquidacion.class);
            query.setParameter(1, semEmpleado);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<ErroresLiquidacion> listaErroresLiquidacionEmpleado = query.getResultList();
            return listaErroresLiquidacionEmpleado;
        } catch (Exception e) {
            System.out.println("\n ERROR EN PersistenciaErroresLiquidaciones consultarErroresLiquidacion ERROR : " + e);
            return null;
        }
    }

    public void BorrarTotosErroresLiquidaciones(EntityManager em) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        int i = -100;
        try {
            tx.begin();
            String sqlQuery = "call ERRORESLIQUIDACION_pkg.BorrarErroresLiquidacion()";
            Query query = em.createNativeQuery(sqlQuery);
            i = query.executeUpdate();
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaCandados.liquidar. " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    public void borrar(EntityManager em, ErroresLiquidacion erroresLiquidacion) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(erroresLiquidacion));
            tx.commit();

        } catch (Exception e) {
            try {
                if (tx.isActive()) {
                    tx.rollback();
                }
            } catch (Exception ex) {
                System.err.println("Error PersistenciaErroresLiquidaciones.borrar: " + e);
            }
        }
    }
}
