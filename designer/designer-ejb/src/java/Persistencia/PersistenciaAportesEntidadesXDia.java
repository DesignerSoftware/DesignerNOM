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
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 *
 * @author user
 */
@Stateless
public class PersistenciaAportesEntidadesXDia implements PersistenciaAportesEntidadesXDiaInterface {

   private static Logger log = Logger.getLogger(PersistenciaAportesEntidadesXDia.class);

    @Override
    public void crear(EntityManager em, AportesEntidadesXDia aporteEntidad) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(aporteEntidad);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaAportesEntidadesXDia.crear :  ", e);
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
            log.error("Error PersistenciaAportesEntidadesXDia.editar :  ", e);
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
            log.error("Error PersistenciaAportesEntidadesXDia.borrar :  ", e);
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
            log.error("Error PersistenciaAportesEntidadesXDia.consultarAportesEntidadesXDia :  ", e);
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
            log.error("Error PersistenciaAportesEntidadesXDia.consultarAportesEntidadesXDia :  ", e);
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
                    + "      AND TE.CODIGO IN (1,2,3,11,13,14)\n"
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
            log.error("Error PersistenciaAportesEntidadesXDia.cosultarTarifa :  ", e);
            return null;
        }
    }

}
