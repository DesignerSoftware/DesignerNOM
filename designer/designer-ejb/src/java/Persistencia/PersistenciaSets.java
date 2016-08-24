/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.Sets;
import InterfacePersistencia.PersistenciaSetsInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Clase Stateless. <br>
 * Clase encargada de realizar operaciones sobre la tabla 'Sets' de la base de
 * datos.
 *
 * @author AndresPineda
 */
@Stateless
public class PersistenciaSets implements PersistenciaSetsInterface {

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
//    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
//    private EntityManager em;
    @Override
    public void crear(EntityManager em, Sets sets) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(sets);
            tx.commit();
        } catch (Exception e) {
            System.out.println("PersistenciaSets La vigencia no exite o esta reservada por lo cual no puede ser modificada: " + e);
            try {
                if (tx.isActive()) {
                    tx.rollback();
                }
            } catch (Exception ex) {
                System.out.println("No se puede hacer rollback porque no hay una transacción");
            }
        }
    }

    @Override
    public void editar(EntityManager em, Sets sets) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(sets);
            tx.commit();
        } catch (Exception e) {
            System.out.println("La vigencia no exite o esta reservada por lo cual no puede ser modificada: " + e);
            try {
                if (tx.isActive()) {
                    tx.rollback();
                }
            } catch (Exception ex) {
                System.out.println("No se puede hacer rollback porque no hay una transacción");
            }
        }
    }

    @Override
    public void borrar(EntityManager em, Sets sets) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(sets));
            tx.commit();
        } catch (Exception e) {
            System.out.println("La vigencia no exite o esta reservada por lo cual no puede ser modificada: " + e);
            try {
                if (tx.isActive()) {
                    tx.rollback();
                }
            } catch (Exception ex) {
                System.out.println("No se puede hacer rollback porque no hay una transacción");
            }
        }
    }

    @Override
    public List<Sets> buscarSets(EntityManager em) {
        em.clear();
        Query query = em.createQuery("SELECT s FROM Sets s");
        query.setHint("javax.persistence.cache.storeMode", "REFRESH");
        List<Sets> setsLista = (List<Sets>) query.getResultList();
        return setsLista;
    }

    @Override
    public Sets buscarSetSecuencia(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT e FROM Sets e WHERE e.secuencia = :secuencia");
            query.setParameter("secuencia", secuencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            Sets sets = (Sets) query.getSingleResult();
            return sets;
        } catch (Exception e) {
            Sets sets = null;
            return sets;
        }
    }

    @Override
    public List<Sets> buscarSetsEmpleado(EntityManager em, BigInteger secEmpleado) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT st FROM Sets st WHERE st.empleado.secuencia = :secuenciaEmpl ORDER BY st.fechainicial DESC");
            query.setParameter("secuenciaEmpl", secEmpleado);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<Sets> setsE = query.getResultList();
            return setsE;
        } catch (Exception e) {
            System.out.println("Error en Persistencia Sets " + e);
            return null;
        }
    }
}
