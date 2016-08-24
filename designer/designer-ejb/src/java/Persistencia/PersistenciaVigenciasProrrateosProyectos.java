/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.VigenciasProrrateosProyectos;
import InterfacePersistencia.PersistenciaVigenciasProrrateosProyectosInterface;
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
 * Clase encargada de realizar operaciones sobre la tabla
 * 'VigenciasProrrateosProyectos' de la base de datos.
 *
 * @author AndresPineda
 */
@Stateless
public class PersistenciaVigenciasProrrateosProyectos implements PersistenciaVigenciasProrrateosProyectosInterface {

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
    /*    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
     private EntityManager em;
     */
    @Override
    public void crear(EntityManager em, VigenciasProrrateosProyectos vigenciasProrrateosProyectos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(vigenciasProrrateosProyectos);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaVigenciasProrrateosProyectos.crear: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, VigenciasProrrateosProyectos vigenciasProrrateosProyectos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(vigenciasProrrateosProyectos);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaVigenciasProrrateosProyectos.editar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, VigenciasProrrateosProyectos vigenciasProrrateosProyectos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(vigenciasProrrateosProyectos));
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaVigenciasProrrateosProyectos.borrar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public List<VigenciasProrrateosProyectos> buscarVigenciasProrrateosProyectos(EntityManager em) {
        try {
            em.clear();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(VigenciasProrrateosProyectos.class));
            return em.createQuery(cq).getResultList();
        } catch (Exception e) {
            System.out.println("Error buscarVigenciasProrrateosProyectos PersistenciaVigenciasProrrateosProyectos");
            return null;
        }
    }

    @Override
    public List<VigenciasProrrateosProyectos> buscarVigenciasProrrateosProyectosEmpleado(EntityManager em, BigInteger secEmpleado) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT vpp FROM VigenciasProrrateosProyectos vpp WHERE vpp.vigencialocalizacion.empleado.secuencia = :secuenciaEmpl ORDER BY vpp.fechainicial DESC");
            query.setParameter("secuenciaEmpl", secEmpleado);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<VigenciasProrrateosProyectos> prorrateosProyectos = query.getResultList();
            return prorrateosProyectos;
        } catch (Exception e) {
            System.out.println("Error en buscarVigenciasProrrateosProyectosEmpleado PersistenciaVigenciasProrrateosProyectos " + e);
            return null;
        }
    }

    @Override
    public VigenciasProrrateosProyectos buscarVigenciasProrrateosProyectosSecuencia(EntityManager em, BigInteger secVPP) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT v FROM VigenciasProrrateosProyectos v WHERE v.secuencia = :secuencia").setParameter("secuencia", secVPP);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            VigenciasProrrateosProyectos prorrateosProyectos = (VigenciasProrrateosProyectos) query.getSingleResult();
            return prorrateosProyectos;
        } catch (Exception e) {
            System.out.println("Error buscarVigenciasProrrateosProyectosSecuencia PersistenciaVigenciasProrrateosProyectos");
            return null;
        }
    }

    @Override
    public List<VigenciasProrrateosProyectos> buscarVigenciasProrrateosProyectosVigenciaSecuencia(EntityManager em, BigInteger secVigencia) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT vpp FROM VigenciasProrrateosProyectos vpp WHERE vpp.vigencialocalizacion.secuencia = :secVigencia");
            query.setParameter("secVigencia", secVigencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<VigenciasProrrateosProyectos> prorrateosProyectos = query.getResultList();
            return prorrateosProyectos;
        } catch (Exception e) {
            System.out.println("Error buscarVigenciasProrrateosProyectosVigenciaSecuencia PersistenciaVigenciasProrrateosProyectos");
            return null;
        }
    }
}
