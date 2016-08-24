package Persistencia;

import Entidades.VigenciasProrrateos;
import InterfacePersistencia.PersistenciaVigenciasProrrateosInterface;
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
 * Clase encargada de realizar operaciones sobre la tabla 'VigenciasProrrateos'
 * de la base de datos.
 *
 * @author AndresPineda
 */
@Stateless
public class PersistenciaVigenciasProrrateos implements PersistenciaVigenciasProrrateosInterface {

    @Override
    public void crear(EntityManager em, VigenciasProrrateos vigenciasProrrateos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(vigenciasProrrateos);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaVigenciasProrrateos.crear: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, VigenciasProrrateos vigenciasProrrateos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(vigenciasProrrateos);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaVigenciasProrrateos.editar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, VigenciasProrrateos vigenciasProrrateos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(vigenciasProrrateos));
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaVigenciasProrrateos.borrar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public List<VigenciasProrrateos> buscarVigenciasProrrateos(EntityManager em) {
        try {
            em.clear();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(VigenciasProrrateos.class));
            return em.createQuery(cq).getResultList();
        } catch (Exception e) {
            System.out.println("Error buscarVigenciasProrrateos PersistenciaVigenciasProrrateos");
            return null;
        }
    }

    @Override
    public List<VigenciasProrrateos> buscarVigenciasProrrateosEmpleado(EntityManager em, BigInteger secEmpleado) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT vp FROM VigenciasProrrateos vp WHERE vp.viglocalizacion.empleado.secuencia = :secuenciaEmpl ORDER BY vp.fechainicial DESC");
            query.setParameter("secuenciaEmpl", secEmpleado);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<VigenciasProrrateos> vigenciasProrrateos = query.getResultList();
            return vigenciasProrrateos;
        } catch (Exception e) {
            System.out.println("Error en Persistencia VigenciasProrrateos " + e);
            return null;
        }
    }

    @Override
    public VigenciasProrrateos buscarVigenciaProrrateoSecuencia(EntityManager em, BigInteger secVP) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT v FROM VigenciasProrrateos v WHERE v.secuencia = :secuencia").setParameter("secuencia", secVP);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            VigenciasProrrateos vigenciasProrrateos = (VigenciasProrrateos) query.getSingleResult();
            return vigenciasProrrateos;
        } catch (Exception e) {
            System.out.println("Error buscarVigenciaProrrateoSecuencia PersistenciaVigenciasProrrateos");
            return null;
        }
    }

    @Override
    public List<VigenciasProrrateos> buscarVigenciasProrrateosVigenciaSecuencia(EntityManager em, BigInteger secVigencia) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT vp FROM VigenciasProrrateos vp WHERE vp.viglocalizacion.secuencia = :secVigencia");
            query.setParameter("secVigencia", secVigencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<VigenciasProrrateos> vigenciasProrrateos = query.getResultList();
            return vigenciasProrrateos;
        } catch (Exception e) {
            System.out.println("Error buscarVigenciasProrrateosVigenciaSecuencia PersistenciaVigenciasProrrateos");
            return null;
        }
    }
}
