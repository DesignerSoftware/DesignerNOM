/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.IbcsAutoliquidaciones;
import InterfacePersistencia.PersistenciaIbcsAutoliquidacionesInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la tabla
 * 'IbcsAutoliquidaciones' de la base de datos.
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaIbcsAutoliquidaciones implements PersistenciaIbcsAutoliquidacionesInterface {

   private static Logger log = Logger.getLogger(PersistenciaIbcsAutoliquidaciones.class);

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
    /*@PersistenceContext(unitName = "DesignerRHN-ejbPU")
    private EntityManager em;*/

    @Override
    public void crear(EntityManager em, IbcsAutoliquidaciones autoliquidaciones) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(autoliquidaciones);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaIbcsAutoliquidaciones.crear:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, IbcsAutoliquidaciones autoliquidaciones) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(autoliquidaciones);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaIbcsAutoliquidaciones.editar:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, IbcsAutoliquidaciones autoliquidaciones) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(autoliquidaciones));
            tx.commit();

        } catch (Exception e) {
            log.error("Error PersistenciaIbcsAutoliquidaciones.borrar:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public List<IbcsAutoliquidaciones> buscarIbcsAutoliquidaciones(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT i FROM IbcsAutoliquidaciones i");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<IbcsAutoliquidaciones> autoliquidaciones = (List<IbcsAutoliquidaciones>) query.getResultList();
            return autoliquidaciones;
        } catch (Exception e) {
            log.error("Error buscarIbcsAutoliquidaciones PersistenciaIbcsAutoliquidaciones :  ", e);
            return null;
        }
    }

    @Override
    public IbcsAutoliquidaciones buscarIbcAutoliquidacionSecuencia(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT ibc FROM IbcsAutoliquidaciones ibc WHERE ibc.secuencia = :secuencia");
            query.setParameter("secuencia", secuencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            IbcsAutoliquidaciones autoliquidaciones = (IbcsAutoliquidaciones) query.getSingleResult();
            return autoliquidaciones;
        } catch (Exception e) {
            log.error("Error buscarIbcAutoliquidacionSecuencia PersistenciaIbcsAutoliquidaciones :  ", e);
            IbcsAutoliquidaciones autoliquidaciones = null;
            return autoliquidaciones;
        }
    }

    @Override
    public List<IbcsAutoliquidaciones> buscarIbcsAutoliquidacionesTipoEntidadEmpleado(EntityManager em, BigInteger secuenciaTE, BigInteger secuenciaEmpl) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT ibc FROM IbcsAutoliquidaciones ibc WHERE ibc.tipoentidad.secuencia = :secuenciaTE AND ibc.empleado.secuencia = :secuenciaEmpl");
            query.setParameter("secuenciaTE", secuenciaTE);
            query.setParameter("secuenciaEmpl", secuenciaEmpl);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<IbcsAutoliquidaciones> autoliquidaciones = (List<IbcsAutoliquidaciones>) query.getResultList();
            return autoliquidaciones;
        } catch (Exception e) {
            log.error("Error buscarIbcsAutoliquidacionesTipoEntidadEmpleado PersistenciaIbcsAutoliquidaciones :  ", e);
            return null;
        }
    }
}
