/**
 * Documentación a cargo de AndresPineda
 */
package Persistencia;

import Entidades.ProcesosProductivos;
import InterfacePersistencia.PersistenciaProcesosProductivosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 * Clase Stateless. <br>
 * Clase encargada de realizar operaciones sobre la tabla 'ProcesosProductivos'
 * de la base de datos.
 *
 * @author AndresPineda
 */
@Stateless
public class PersistenciaProcesosProductivos implements PersistenciaProcesosProductivosInterface {

   private static Logger log = Logger.getLogger(PersistenciaProcesosProductivos.class);

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
//    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
//    private EntityManager em;

    @Override
    public void crear(EntityManager em, ProcesosProductivos procesos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(procesos);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaPensionados.crear:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, ProcesosProductivos procesos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(procesos);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaPensionados.editar:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, ProcesosProductivos procesos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(procesos));
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaPensionados.borrar:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public List<ProcesosProductivos> consultarProcesosProductivos(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT pp FROM ProcesosProductivos pp ORDER BY pp.codigo ASC");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<ProcesosProductivos> procesos = query.getResultList();
            return procesos;
        } catch (Exception e) {
            log.error("Error buscarProcesosProductivos PersistenciaProcesosProductivos :  ", e);
            return null;
        }
    }

    @Override
    public ProcesosProductivos consultarProcesosProductivos(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT pp FROM ProcesosProductivos pp WHERE pp.secuencia = :secuencia");
            query.setParameter("secuencia", secuencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            ProcesosProductivos procesos = (ProcesosProductivos) query.getSingleResult();
            return procesos;
        } catch (Exception e) {
            log.error("Error buscarProcesosProductivosSecuencia PersistenciaProcesosProductivos :  ", e);
            ProcesosProductivos procesos = null;
            return procesos;
        }
    }

    public BigInteger contarUnidadesProducidasProcesoProductivo(EntityManager em, BigInteger secuencia) {
        BigInteger retorno = new BigInteger("-1");
        try {
            em.clear();
            String sqlQuery = "SELECT COUNT (*)FROM unidadesproducidas WHERE procesoproductivo = ?";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            retorno = new BigInteger(query.getSingleResult().toString());
            return retorno;
        } catch (Exception e) {
            log.error("Error PERSISTENCIASUCURSALES contarVigenciasFormasPagosSucursal :  ", e);
            return retorno;
        }
    }

    public BigInteger contarTarifasProductosProcesoProductivo(EntityManager em, BigInteger secuencia) {
        BigInteger retorno = new BigInteger("-1");
        try {
            em.clear();
            String sqlQuery = "SELECT COUNT (*)FROM tarifasproductos WHERE procesoproductivo = ?";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            retorno = new BigInteger(query.getSingleResult().toString());
            return retorno;
        } catch (Exception e) {
            log.error("Error PERSISTENCIASUCURSALES contarVigenciasFormasPagosSucursal :  ", e);
            return retorno;
        }
    }
    
    public BigInteger contarCargosProcesoProductivo(EntityManager em, BigInteger secuencia) {
        BigInteger retorno = new BigInteger("-1");
        try {
            em.clear();
            String sqlQuery = "SELECT COUNT (*)FROM cargos WHERE procesoproductivo =  ?";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            retorno = new BigInteger(query.getSingleResult().toString());
            return retorno;
        } catch (Exception e) {
            log.error("Error PERSISTENCIASUCURSALES contarVigenciasFormasPagosSucursal :  ", e);
            return retorno;
        }
    }
}
