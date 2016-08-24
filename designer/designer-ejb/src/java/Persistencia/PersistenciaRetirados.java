/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.Retirados;
import InterfacePersistencia.PersistenciaRetiradosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;

/**
 * Clase Stateless. <br>
 * Clase encargada de realizar operaciones sobre la tabla 'Retirados' de la base
 * de datos.
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaRetirados implements PersistenciaRetiradosInterface {

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
//    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
//    private EntityManager em;
    @Override
    public void crear(EntityManager em, Retirados retirados) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(retirados);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaRetirados.crear: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, Retirados retirados) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(retirados);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaRetirados.editar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, Retirados retirados) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(retirados));
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaRetirados.borrar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public List<Retirados> buscarRetirados(EntityManager em) {
        em.clear();
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Retirados.class));
        return em.createQuery(cq).getResultList();
    }

    @Override
    public List<Retirados> buscarRetirosEmpleado(EntityManager em, BigInteger secEmpleado) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT r FROM Retirados r WHERE r.vigenciatipotrabajador.empleado.secuencia = :secuenciaEmpl ORDER BY r.fecharetiro DESC");
            query.setParameter("secuenciaEmpl", secEmpleado);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<Retirados> retiros = query.getResultList();
            return retiros;
        } catch (Exception e) {
            System.out.println("Error en Persistencia Retirados " + e);
            return null;
        }
    }

    @Override
    public Retirados buscarRetiroSecuencia(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT r FROM Retirados r WHERE r.secuencia = :secuencia").setParameter("secuencia", secuencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            Retirados retiro = (Retirados) query.getSingleResult();
            return retiro;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Retirados buscarRetiroVigenciaSecuencia(EntityManager em, BigInteger secVigencia) {
        try {
            em.clear();
            String sql = "SELECT * FROM Retirados WHERE vigenciatipotrabajador = ?";
            Query query = em.createNativeQuery(sql, Retirados.class);
            query.setParameter(1, secVigencia);
            //query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            Retirados retiroVigencia = (Retirados) query.getSingleResult();
            return retiroVigencia;
        } catch (Exception e) {
            return new Retirados();
        }
    }
}
