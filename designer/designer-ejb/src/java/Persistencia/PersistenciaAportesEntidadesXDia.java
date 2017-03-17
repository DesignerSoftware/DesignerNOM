/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.AportesEntidadesXDia;
import InterfacePersistencia.PersistenciaAportesEntidadesXDiaInterface;
import java.math.BigDecimal;
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
public class PersistenciaAportesEntidadesXDia implements PersistenciaAportesEntidadesXDiaInterface {

    @Override
    public void crear(EntityManager em, AportesEntidadesXDia aporteEntidad) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(aporteEntidad);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaAportesEntidadesXDia.crear : " + e.toString());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, AportesEntidadesXDia aporteEntidad) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(aporteEntidad);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaAportesEntidadesXDia.editar : " + e.toString());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, AportesEntidadesXDia aporteEntidad) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(aporteEntidad));
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaAportesEntidadesXDia.borrar : " + e.toString());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public List<AportesEntidadesXDia> consultarAportesEntidadesXDia(EntityManager em) {
        try {
            em.clear();
            String sql = "SELECT * FROM APORTESENTIDADESXDIA ORDER BY DIA ASC";
            Query query = em.createNativeQuery(sql, AportesEntidadesXDia.class);
            List<AportesEntidadesXDia> aportesEntidades = query.getResultList();
            return aportesEntidades;
        } catch (Exception e) {
            System.out.println("Error PersistenciaAportesEntidadesXDia.consultarAportesEntidadesXDia : " + e.toString());
            return null;
        }
    }

    @Override
    public List<AportesEntidadesXDia> consultarAportesEntidadesPorEmpleadoMesYAnio(EntityManager em, BigInteger secEmpleado, short mes, short ano) {
        try {
            em.clear();
            String sql = "SELECT * FROM APORTESENTIDADESXDIA WHERE EMPLEADO = ? AND MES = ? AND ANO = ? ORDER BY DIA ASC";
            Query query = em.createNativeQuery(sql, AportesEntidadesXDia.class);
            query.setParameter(1, secEmpleado);
            query.setParameter(2, mes);
            query.setParameter(3, ano);
            List<AportesEntidadesXDia> aportesEntidadesDia = query.getResultList();
            return aportesEntidadesDia;
        } catch (Exception e) {
            System.out.println("Error PersistenciaAportesEntidadesXDia.consultarAportesEntidadesXDia : " + e.toString());
            return null;
        }
    }

    @Override
    public BigDecimal cosultarTarifa(EntityManager em, BigInteger secEmpresa, BigInteger secEmpleado, short mes, short ano, BigInteger secTipoEntidad) {
        try {
            em.clear();
            String sql = "SELECT TE.CODIGO\n"
                    + "      FROM APORTESENTIDADES AE, TIPOSENTIDADES TE\n"
                    + "      WHERE AE.TIPOENTIDAD = TE.SECUENCIA \n"
                    + "      AND AE.EMPRESA = ? \n"
                    + "      AND AE.MES = ? \n"
                    + "      AND AE.ANO = ? \n"
                    + "      AND AE.EMPLEADO = ? \n"
                    + "      AND TE.CODIGO IN (1,2,3,14 )\n"
                    + "      AND TE.SECUENCIA = ?";
            Query query = em.createNativeQuery(sql);
            query.setParameter(1, secEmpresa);
            query.setParameter(2, mes);
            query.setParameter(3, ano);
            query.setParameter(4, secEmpleado);
            query.setParameter(5, secTipoEntidad);
            BigDecimal tarifa = (BigDecimal) query.getSingleResult();
            return tarifa;
        } catch (Exception e) {
            System.out.println("Error PersistenciaAportesEntidadesXDia.cosultarTarifa : " + e.toString());
            return null;
        }
    }

}
