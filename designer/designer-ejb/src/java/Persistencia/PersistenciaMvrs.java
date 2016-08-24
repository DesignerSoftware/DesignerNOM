/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.Mvrs;
import InterfacePersistencia.PersistenciaMvrsInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la tabla 'Mvrs' de la base de
 * datos.
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaMvrs implements PersistenciaMvrsInterface {

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
//    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
//    private EntityManager em;
    @Override
    public void crear(EntityManager em, Mvrs mvrs) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(mvrs);
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
    public void editar(EntityManager em, Mvrs mvrs) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(mvrs);
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
    public void borrar(EntityManager em, Mvrs mvrs) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(mvrs));
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
    public List<Mvrs> buscarMvrs(EntityManager em) {
        em.clear();
        List<Mvrs> mvrs = (List<Mvrs>) em.createQuery("SELECT m FROM Mvrs m").getResultList();
        return mvrs;
    }

    @Override
    public Mvrs buscarMvrSecuencia(EntityManager em, BigInteger secuencia) {

        try {
            em.clear();
            Query query = em.createQuery("SELECT mvrs FROM Mvrs mvrs WHERE mvrs.secuencia = :secuencia");
            query.setParameter("secuencia", secuencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            Mvrs mvrs = (Mvrs) query.getSingleResult();
            return mvrs;
        } catch (Exception e) {
            Mvrs mvrs = null;
            return mvrs;
        }
    }

    @Override
    public List<Mvrs> buscarMvrsEmpleado(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT mvrs FROM Mvrs mvrs WHERE mvrs.empleado.secuencia = :secuencia");
            query.setParameter("secuencia", secuencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<Mvrs> mvrs = (List<Mvrs>) query.getResultList();
            return mvrs;
        } catch (Exception e) {
            List<Mvrs> mvrs = null;
            return mvrs;
        }
    }
}
