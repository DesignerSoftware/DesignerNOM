/**
 * Documentación a cargo de Andres Pineda
 */
package Persistencia;

import Entidades.Escalafones;
import InterfacePersistencia.PersistenciaEscalafonesInterface;
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
 * Clase encargada de realizar operaciones sobre la tabla 'Escalafones' de la
 * base de datos.
 *
 * @author Andres Pineda
 */
@Stateless
public class PersistenciaEscalafones implements PersistenciaEscalafonesInterface {

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos
     */
    /* @PersistenceContext(unitName = "DesignerRHN-ejbPU")
     private EntityManager em;*/
    @Override
    public void crear(EntityManager em, Escalafones escalafones) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(escalafones);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaEscalafones.crear: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, Escalafones escalafones) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(escalafones);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaEscalafones.editar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, Escalafones escalafon) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(escalafon));
            tx.commit();

        } catch (Exception e) {
            try {
                if (tx.isActive()) {
                    tx.rollback();
                }
            } catch (Exception ex) {
                System.out.println("Error PersistenciaEscalafones.borrar: " + e);
            }
        }
    }

    //@Override
    public List<Escalafones> buscarEscalafones(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT e FROM Escalafones e");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<Escalafones> lista = query.getResultList();
            return lista;
        } catch (Exception e) {
            System.out.println("Error PersistenciaEscalafones buscarEscalafones : " + e.toString());
            return null;
        }
    }

    @Override
    public Escalafones buscarEscalafonSecuencia(EntityManager em, BigInteger secEscalafon) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT e FROM Escalafones e WHERE e.secuencia=:secuencia");
            query.setParameter("secuencia", secEscalafon);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            Escalafones escalafones = (Escalafones) query.getSingleResult();
            return escalafones;
        } catch (Exception e) {
            return null;
        }
    }
}
