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
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
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
            System.out.println("Error PersistenciaIbcsAutoliquidaciones.crear: " + e.getMessage());
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
            System.out.println("Error PersistenciaIbcsAutoliquidaciones.editar: " + e.getMessage());
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
            System.out.println("Error PersistenciaIbcsAutoliquidaciones.borrar: " + e.getMessage());
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
            System.out.println("Error buscarIbcsAutoliquidaciones PersistenciaIbcsAutoliquidaciones : " + e.toString());
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
            System.out.println("Error buscarIbcAutoliquidacionSecuencia PersistenciaIbcsAutoliquidaciones : " + e.toString());
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
            System.out.println("Error buscarIbcsAutoliquidacionesTipoEntidadEmpleado PersistenciaIbcsAutoliquidaciones : " + e.toString());
            return null;
        }
    }
}
