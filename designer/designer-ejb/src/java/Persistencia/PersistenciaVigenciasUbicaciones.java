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
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
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
            System.out.println("Persistencia.PersistenciaVigenciasUbicaciones.crear()" + e.getMessage());
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
            System.out.println("Persistencia.PersistenciaVigenciasUbicaciones.editar()" + e.getMessage());
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
            System.out.println("Persistencia.PersistenciaVigenciasUbicaciones.borrar()" + e.getMessage());
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
            System.out.println("Persistencia.PersistenciaVigenciasUbicaciones.buscarVigenciaUbicacion()" + e.getMessage());
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
            System.out.println("Persistencia.PersistenciaVigenciasUbicaciones.buscarVigenciasUbicaciones()" + e.getMessage());
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
            System.out.println("Persistencia.PersistenciaVigenciasUbicaciones.buscarVigenciaUbicacionesEmpleado()" + e.getMessage());
            return null;
        }
    }
}
