/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.CortesProcesos;
import InterfacePersistencia.PersistenciaCortesProcesosInterface;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Clase Stateless. <br>
 * Clase encargada de realizar operaciones sobre la tabla 'CortesProcesos' de la
 * base de datos
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaCortesProcesos implements PersistenciaCortesProcesosInterface {

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos
     */
    /* @PersistenceContext(unitName = "DesignerRHN-ejbPU")
     private EntityManager em;*/
    @Override
    public void crear(EntityManager em, CortesProcesos corteProceso) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(corteProceso);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaCortesProcesos.crear: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, CortesProcesos corteProceso) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(corteProceso);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaCortesProcesos.editar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, CortesProcesos corteProceso) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(corteProceso));
            tx.commit();

        } catch (Exception e) {
            try {
                if (tx.isActive()) {
                    tx.rollback();
                }
            } catch (Exception ex) {
                System.out.println("Error PersistenciaCortesProcesos.borrar: " + e);
            }
        }
    }

    @Override
    public CortesProcesos buscarCorteProcesoSecuencia(EntityManager em, BigInteger secuencia) {
        em.clear();
        return em.find(CortesProcesos.class, secuencia);
    }

    @Override
    public List<CortesProcesos> buscarCortesProcesos(EntityManager em) {
        em.clear();
        javax.persistence.criteria.CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(CortesProcesos.class));
        return em.createQuery(cq).getResultList();
    }

    @Override
    public List<CortesProcesos> cortesProcesosComprobante(EntityManager em, BigInteger secuenciaComprobante) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT cp FROM CortesProcesos cp WHERE cp.comprobante.secuencia = :secuenciaComprobante");
            query.setParameter("secuenciaComprobante", secuenciaComprobante);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<CortesProcesos> listCortesProcesos = query.getResultList();
            return listCortesProcesos;
        } catch (Exception e) {
            System.out.println("Error: (PersistenciaCortesProcesos.cortesProcesosComprobante)" + e);
            return null;
        }
    }

    @Override
    public Integer contarLiquidacionesCerradas(EntityManager em, BigInteger secProceso, String fechaDesde, String fechaHasta) {
        try {
            em.clear();
            String sqlQuery = "SELECT nvl(COUNT(CP.SECUENCIA),0)\n"
                    + "       FROM CORTESPROCESOS CP, empleados e, comprobantes co\n"
                    + "       WHERE e.secuencia=cp.empleado\n"
                    + "       AND co.secuencia=cp.comprobante\n"
                    + "       AND co.fecha  between  To_date( ?, 'dd/mm/yyyy') and To_date( ?, 'dd/mm/yyyy')\n"
                    + "       AND CP.proceso = ?";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, fechaDesde);
            query.setParameter(2, fechaHasta);
            query.setParameter(3, secProceso);
            BigDecimal conteo = (BigDecimal) query.getSingleResult();
            Integer conteoLiquidacionCerradas = conteo.intValueExact();
            return conteoLiquidacionCerradas;
        } catch (Exception e) {
            System.out.println("Error contarLiquidacionesCerradas. " + e);
            return null;
        }
    }

    @Override
    public void eliminarComprobante(EntityManager em, Short codigoProceso, String fechaDesde, String fechaHasta) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            String sqlQuery = "call CORTESPROCESOS_PKG.ELIMINARCOMPROBANTE(?, To_date( ?, 'dd/mm/yyyy'), To_date( ?, 'dd/mm/yyyy'))";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, codigoProceso);
            query.setParameter(2, fechaDesde);
            query.setParameter(3, fechaHasta);
            query.executeUpdate();
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error cerrarLiquidacion. " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void eliminarCPconUndoCierre(EntityManager em, BigInteger proceso, BigInteger rfEmpleado, Date fechaCorte) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            System.out.println(this.getClass().getName() + " Entro en eliminarCPconUndoCierre()");
            System.out.println("proceso : " + proceso);
            System.out.println("rfEmpleado : " + rfEmpleado);
            System.out.println("fechaCorte : " + fechaCorte);
            DateFormat formatoF = new SimpleDateFormat("ddMMyyyy");
            String fecha = formatoF.format(fechaCorte);
            System.out.println("fecha : " + fecha);
            String sqlQuery = "call CORTESPROCESOS_PKG.UndoCierre(" + proceso + ", " + rfEmpleado + ", To_date( '" + fecha + "', 'ddMMyyyy'))";
            System.out.println("sqlQuery : " + sqlQuery);
            Query query = em.createNativeQuery(sqlQuery);
//            query.setParameter(1, proceso);
//            query.setParameter(2, rfEmpleado);
//            query.setParameter(3, fecha);
            query.executeUpdate();
            tx.commit();
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + " Error eliminarCPconUndoCierre() : " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public CortesProcesos buscarComprobante(EntityManager em, BigInteger secuenciaEmpleado) {
        try {
            em.clear();
            Query query = em.createNativeQuery("SELECT cp.* from cortesprocesos cp, procesos p\n"
                    + "where cp.empleado=?\n"
                    + "and cp.proceso=p.secuencia\n"
                    + "and p.codigo in (1,2,9,10)\n"
                    + "and cp.corte= (select max(cpi.corte) from cortesprocesos cpi, procesos pi where cpi.empleado=? and cpi.proceso=pi.secuencia and pi.codigo in (1,2,9,10))\n"
                    + "and ROWNUM <=1 \n"
                    + "order by cp.comprobante desc", CortesProcesos.class);
            query.setParameter(1, secuenciaEmpleado);
            query.setParameter(2, secuenciaEmpleado);
            CortesProcesos actualComprobante = (CortesProcesos) query.getSingleResult();
            return actualComprobante;
        } catch (Exception e) {
            System.out.println("Error: (PersistenciaCortesProcesos.buscarComprobante)" + e);
            return null;
        }
    }
}
