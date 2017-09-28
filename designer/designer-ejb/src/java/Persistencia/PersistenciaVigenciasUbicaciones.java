/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.VigenciasUbicaciones;
import InterfacePersistencia.PersistenciaVigenciasUbicacionesInterface;
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
 * Clase encargada de realizar operaciones sobre la tabla 'VigenciasUbicaciones'
 * de la base de datos.
 *
 * @author AndresPineda
 */
@Stateless
public class PersistenciaVigenciasUbicaciones implements PersistenciaVigenciasUbicacionesInterface {

   private static Logger log = Logger.getLogger(PersistenciaVigenciasUbicaciones.class);

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     *
     * @param em
     * @param vigenciaUbicacion
     * @return
     */
    /*    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
     private EntityManager em;
     */
    @Override
    public boolean crear(EntityManager em, VigenciasUbicaciones vigenciaUbicacion) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(vigenciaUbicacion);
            tx.commit();
            return true;
        } catch (Exception e) {
            log.error("PersistenciaVigenciasUbicaciones.crear():  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
            return false;
        }
    }

    @Override
    public void editar(EntityManager em, VigenciasUbicaciones vigenciaUbicacion) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(vigenciaUbicacion);
            tx.commit();
        } catch (Exception e) {
            log.error("PersistenciaVigenciasUbicaciones.editar():  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, VigenciasUbicaciones vigenciaUbicacion) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(vigenciaUbicacion));
            tx.commit();
        } catch (Exception e) {
            log.error("PersistenciaVigenciasUbicaciones.borrar():  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public VigenciasUbicaciones buscarVigenciaUbicacion(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            return em.find(VigenciasUbicaciones.class, secuencia);
        } catch (Exception e) {
            log.error("PersistenciaVigenciasUbicaciones.buscarVigenciaUbicacion():  ", e);
            return null;
        }
    }

    @Override
    public List<VigenciasUbicaciones> buscarVigenciasUbicaciones(EntityManager em) {
        try {
            em.clear();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(VigenciasUbicaciones.class));
            return em.createQuery(cq).getResultList();
        } catch (Exception e) {
            log.error("PersistenciaVigenciasUbicaciones.buscarVigenciasUbicaciones():  ", e);
            return null;
        }
    }

    @Override
    public List<VigenciasUbicaciones> buscarVigenciaUbicacionesEmpleado(EntityManager em, BigInteger secEmpleado) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT vu FROM VigenciasUbicaciones vu WHERE vu.empleado.secuencia = :secuenciaEmpl ORDER BY vu.fechavigencia DESC");
            query.setParameter("secuenciaEmpl", secEmpleado);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<VigenciasUbicaciones> vigenciasUbicaciones = query.getResultList();
            return vigenciasUbicaciones;
        } catch (Exception e) {
            log.error("PersistenciaVigenciasUbicaciones.buscarVigenciaUbicacionesEmpleado():  ", e);
            return null;
        }
    }
}
