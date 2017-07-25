/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.VWVacaPendientesEmpleados;
import InterfacePersistencia.PersistenciaVWVacaPendientesEmpleadosInterface;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 * Clase Stateless.<br> Clase encargada de realizar operaciones sobre la vista
 * 'VWVacaPendientesEmpleados' de la base de datos.
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaVWVacaPendientesEmpleados implements PersistenciaVWVacaPendientesEmpleadosInterface {

   private static Logger log = Logger.getLogger(PersistenciaVWVacaPendientesEmpleados.class);

    @Override
    public void crear(EntityManager em, VWVacaPendientesEmpleados vacaP) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(vacaP);
            tx.commit();
        } catch (Exception e) {
            log.error("Persistencia.PersistenciaVWVacaPendientesEmpleados.crear()" + e.getMessage());
                if (tx.isActive()) {
                    tx.rollback();
                }
        }
    }

    @Override
    public void editar(EntityManager em, VWVacaPendientesEmpleados vacaP) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(vacaP);
            tx.commit();
        } catch (Exception e) {
            log.error("Persistencia.PersistenciaVWVacaPendientesEmpleados.editar()" + e.getMessage());
                if (tx.isActive()) {
                    tx.rollback();
                }
        }
    }

    @Override
    public void borrar(EntityManager em, VWVacaPendientesEmpleados vacaP) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(vacaP));
            tx.commit();
        } catch (Exception e) {
            log.error("Persistencia.PersistenciaVWVacaPendientesEmpleados.borrar()" + e.getMessage());
                if (tx.isActive()) {
                    tx.rollback();
                }
        }
    }

    @Override
    public List<VWVacaPendientesEmpleados> vacaEmpleadoPendientes(EntityManager em, BigInteger secuencia,Date fechaingreso) {
        List<VWVacaPendientesEmpleados> listaVacaPendientesEmpleados = null;
        try {
            em.clear();
            String script = "SELECT vwv FROM VWVacaPendientesEmpleados vwv WHERE vwv.empleado = :empleado AND vwv.diaspendientes > 0 and vwv.inicialcausacion >= :fechaingreso ORDER BY vwv.finalcausacion desc";
            Query query = em.createQuery(script);
            query.setParameter("empleado", secuencia);
            query.setParameter("fechaingreso", fechaingreso);
            listaVacaPendientesEmpleados = query.getResultList();
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
        } catch (Exception e) {
            log.error("PersistenciaVWVacaPendientesEmpleados.buscarVacaPendientesEmpleados." + e.getMessage());
        } finally {
            return listaVacaPendientesEmpleados;
        }
    }

    @Override
    public List<VWVacaPendientesEmpleados> vacaEmpleadoDisfrutadas(EntityManager em, BigInteger sec,Date fechaingreso) {
        List<VWVacaPendientesEmpleados> listaVacaPendientesEmpleados = null;
        try {
            em.clear();
            String script = "SELECT vwv FROM VWVacaPendientesEmpleados vwv WHERE vwv.empleado = :empleado AND vwv.diaspendientes <= 0 and vwv.inicialcausacion >= :fechaingreso ORDER BY vwv.finalcausacion desc ";
            Query query = em.createQuery(script);
            query.setParameter("empleado", sec);
            query.setParameter("fechaingreso", fechaingreso);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            listaVacaPendientesEmpleados = query.getResultList();
        } catch (Exception e) {
            log.error("PersistenciaVWVacaPendientesEmpleados.buscarVacaPendientesEmpleadosDisfrutadas" + e.getMessage());
            log.error(e);
        } finally {
            return listaVacaPendientesEmpleados;
        }
    }

    @Override
    public List<VWVacaPendientesEmpleados> buscarVacaPendientesEmpleados(EntityManager em, BigInteger secuenciaEmpleado) {
        List<VWVacaPendientesEmpleados> listaVacaPendientesEmpleados = null;
        try {
            em.clear();
            listaVacaPendientesEmpleados = em.createQuery("SELECT v FROM VWVacaPendientesEmpleados v WHERE v.empleado = :empleado").setParameter("empleado", secuenciaEmpleado).getResultList();
        } catch (Exception e) {
            log.error("PersistenciaVWVacaPendientesEmpleados.buscarVacaPendientesEmpleados." + e.getMessage());
            log.error(e);
        } finally {
            return listaVacaPendientesEmpleados;
        }
    }

    @Override
    public List<VWVacaPendientesEmpleados> vacaEmpleadoPendientesAnterioresContratos(EntityManager em, BigInteger secuencia) {
       List<VWVacaPendientesEmpleados> listaVacaPendientesEmpleados = null;
        try {
            em.clear();
            String script = "SELECT vwv FROM VWVacaPendientesEmpleados vwv WHERE vwv.empleado = :empleado AND vwv.diaspendientes > 0 ORDER BY vwv.finalcausacion desc";
            Query query = em.createQuery(script).setParameter("empleado", secuencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            listaVacaPendientesEmpleados = query.getResultList();
        } catch (Exception e) {
            log.error("PersistenciaVWVacaPendientesEmpleados.vacaEmpleadoPendientesAnterioresContratos." + e.getMessage());
        } finally {
            return listaVacaPendientesEmpleados;
        }
    }

    @Override
    public List<VWVacaPendientesEmpleados> vacaEmpleadoDisfrutadasAnterioresContratos(EntityManager em, BigInteger secuencia) {
          List<VWVacaPendientesEmpleados> listaVacaPendientesEmpleados = null;
        try {
            em.clear();
            String script = "SELECT vwv FROM VWVacaPendientesEmpleados vwv WHERE vwv.empleado = :empleado AND vwv.diaspendientes <= 0 ORDER BY vwv.finalcausacion desc ";
            Query query = em.createQuery(script).setParameter("empleado", secuencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            listaVacaPendientesEmpleados = query.getResultList();
        } catch (Exception e) {
            log.error("PersistenciaVWVacaPendientesEmpleados.vacaEmpleadoDisfrutadasAnterioresContratos" + e.getMessage());
            log.error(e);
        } finally {
            return listaVacaPendientesEmpleados;
        }
    }
}
