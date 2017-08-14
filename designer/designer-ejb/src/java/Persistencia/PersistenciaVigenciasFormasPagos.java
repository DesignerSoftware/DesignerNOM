/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.VigenciasFormasPagos;
import InterfacePersistencia.PersistenciaVigenciasFormasPagosInterface;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;

/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la tabla 'VigenciasFormasPagos'
 * de la base de datos.
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaVigenciasFormasPagos implements PersistenciaVigenciasFormasPagosInterface {

   private static Logger log = Logger.getLogger(PersistenciaVigenciasFormasPagos.class);

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     *
     * @param em
     * @return
     */
    /*    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
     private EntityManager em;
     */
    @Override
    public boolean crear(EntityManager em, VigenciasFormasPagos vigenciasFormasPagos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(vigenciasFormasPagos);
            tx.commit();
            return true;
        } catch (Exception e) {
            log.error("Persistencia.PersistenciaVigenciasFormasPagos.crear()" + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
            return false;
        }
    }

    @Override
    public void editar(EntityManager em, VigenciasFormasPagos vigenciasFormasPagos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(vigenciasFormasPagos);
            tx.commit();
        } catch (Exception e) {
            log.error("Persistencia.PersistenciaVigenciasFormasPagos.editar()" + e.getMessage());
                if (tx.isActive()) {
                    tx.rollback();
                }
        }
    }

    @Override
    public void borrar(EntityManager em, VigenciasFormasPagos vigenciasFormasPagos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(vigenciasFormasPagos));
            tx.commit();
        } catch (Exception e) {
            log.error("Persistencia.PersistenciaVigenciasFormasPagos.borrar()" + e.getMessage());
                if (tx.isActive()) {
                    tx.rollback();
                }
        }
    }

    @Override
    public VigenciasFormasPagos buscarVigenciaFormaPago(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            return em.find(VigenciasFormasPagos.class, secuencia);
        } catch (Exception e) {
            log.error("Error en la persistencia vigencias formas pagos ERROR :" + e.getMessage());
            return null;
        }
    }

    @Override
    public List<VigenciasFormasPagos> buscarVigenciasFormasPagosPorEmpleado(EntityManager em, BigInteger secEmpleado) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT vfp FROM VigenciasFormasPagos vfp WHERE vfp.empleado.secuencia = :secuenciaEmpl ORDER BY vfp.fechavigencia DESC");
            query.setParameter("secuenciaEmpl", secEmpleado);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<VigenciasFormasPagos> vigenciasNormasEmpleados = query.getResultList();
            return vigenciasNormasEmpleados;
        } catch (Exception e) {
            log.error("Error en Persistencia Vigencias Formas Pagos Por Empleados " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<VigenciasFormasPagos> buscarVigenciasFormasPagos(EntityManager em) {
        em.clear();
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(VigenciasFormasPagos.class));
        return em.createQuery(cq).getResultList();
    }

    @Override
    public BigDecimal buscarPeriodicidadPorEmpl(EntityManager em, BigInteger secEmpleado) {
        try {
            em.clear();
            String sql = "SELECT FORMAPAGO FROM VIGENCIASFORMASPAGOS WHERE EMPLEADO = e.getMessage() ";
            Query query = em.createNativeQuery(sql);
            query.setParameter("1", secEmpleado);
            BigDecimal periodicidad = (BigDecimal) query.getSingleResult();
            log.warn("buscarPeriodicidadPorEmpl: " + periodicidad);
            return periodicidad;
        } catch (Exception e) {
            log.error("Error en Persistencia Vigencias Formas Pagos.buscarVigenciaFormaPagoPorEmpl() " + e.getMessage());
            return null;
        }
    }
}
