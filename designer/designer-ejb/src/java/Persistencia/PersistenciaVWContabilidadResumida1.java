/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.VWContabilidadDetallada;
import Entidades.VWContabilidadResumida1;
import InterfacePersistencia.PersistenciaVWContabilidadResumida1Interface;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author user
 */
@Stateless
public class PersistenciaVWContabilidadResumida1 implements PersistenciaVWContabilidadResumida1Interface {

    @Override
    public List<VWContabilidadResumida1> buscarContabilidadResumidaParametroContable(EntityManager em, Date fechaInicial, Date fechaFinal, BigInteger Proceso) {
        try {
            em.clear();
            String sql = "SELECT  * FROM VWCONTABILIDADRESUMIDA1\n"
                    + "WHERE FECHACONTABILIZACION \n"
                    + "between ? AND ? \n"
                    + "AND nvl(proceso,0)=nvl(?,nvl(proceso,0))";
            Query query = em.createNativeQuery(sql, VWContabilidadResumida1.class);
            query.setParameter(1, fechaInicial);
            query.setParameter(2, fechaFinal);
            query.setParameter(3, Proceso);
            List<VWContabilidadResumida1> contabilidadResumida = query.getResultList();
            return contabilidadResumida;
        } catch (Exception e) {
            System.out.println("Error PersistenciaVWContabilidadResumida1.buscarContabilidadResumidaParametroContable: " + e.toString());
            return null;
        }
    }

    @Override
    public List<VWContabilidadDetallada> buscarContabilidadDetalladaParametroContable(EntityManager em, Date fechaInicial, Date fechaFinal, BigInteger Proceso) {
        try {
            em.clear();
            String sql = "SELECT  * FROM VWCONTABILIDADDETALLADA\n"
                    + "WHERE FECHACONTABILIZACION \n"
                    + "between ? AND ? \n"
                    + "AND nvl(proceso,0)=nvl(?,nvl(proceso,0))";
            Query query = em.createNativeQuery(sql, VWContabilidadResumida1.class);
            query.setParameter(1, fechaInicial);
            query.setParameter(2, fechaFinal);
            query.setParameter(3, Proceso);
            List<VWContabilidadDetallada> VWContabilidadDetallada = query.getResultList();
            return VWContabilidadDetallada;
        } catch (Exception e) {
            System.out.println("Error PersistenciaVWContabilidadResumida1.buscarContabilidadDetalladaParametroContable: " + e.toString());
            return null;
        }
    }

    @Override
    public Integer abrirPeriodoContable(EntityManager em, Date fechaInicial, Date fechaFinal, BigInteger Proceso) {
        try {
            Integer conteo = null;
            em.clear();
            String sql = "SELECT count(*)\n"
                    + "FROM VWCONTABILIDADDETALLADA c\n"
                    + "WHERE c.flag = 'ENVIADO'\n"
                    + "AND c.fechacontabilizacion  between ? and ? \n"
                    + "AND NVL(C.PROCESO,0) = NVL(?,0)\n"
                    + "AND EXISTS (SELECT 'X' FROM EMPLEADOS E WHERE C.EMPLEADO=E.SECUENCIA)";
            Query query = em.createNativeQuery(sql);
            query.setParameter(1, fechaInicial);
            query.setParameter(2, fechaFinal);
            query.setParameter(3, Proceso);
            BigDecimal valor = (BigDecimal) query.getSingleResult();
            if (valor != null) {
                conteo = valor.intValue();
                return conteo;
            } else {
                conteo = 0;
                return conteo;
            }
        } catch (Exception e) {
            System.out.println("Error abrirPeriodoContable PersistenciaVWContabilidadResumida1 : " + e.toString());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void actualizarPeriodoContable(EntityManager em, Date fechaInicial, Date fechaFinal, BigInteger Proceso) {
        try {
            em.clear();
            String sql = "UPDATE VWCONTABILIDADDETALLADA c\n"
                    + "SET FLAG ='CONTABILIZADO'\n"
                    + "WHERE c.flag = 'ENVIADO'\n"
                    + "AND c.fechacontabilizacion  between ? and ? \n"
                    + "AND NVL(C.PROCESO,0) = NVL(?,0)\n"
                    + "AND EXISTS (SELECT 'X' FROM EMPLEADOS E WHERE C.EMPLEADO=E.SECUENCIA";
            Query query = em.createNativeQuery(sql);
            query.setParameter(1, fechaInicial);
            query.setParameter(2, fechaFinal);
            query.setParameter(3, Proceso);
        } catch (Exception e) {
            System.out.println("Error abrirPeriodoContable PersistenciaVWContabilidadResumida1 : " + e.getMessage());
        }
    }
}
