/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.Indicadores;
import InterfacePersistencia.PersistenciaIndicadoresInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la tabla 'Indicadores' de la
 * base de datos.
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaIndicadores implements PersistenciaIndicadoresInterface {

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
//    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
//    private EntityManager em;

    @Override
    public void crear(EntityManager em, Indicadores indicadores) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(indicadores);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaIndicadores.crear: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, Indicadores indicadores) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(indicadores);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaIndicadores.editar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, Indicadores indicadores) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(indicadores));
            tx.commit();

        } catch (Exception e) {
        System.out.println("Error PersistenciaIndicadores.borrar: " + e.getMessage());
                if (tx.isActive()) {
                    tx.rollback();
                }
        }
    }

    @Override
    public List<Indicadores> buscarIndicadores(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT i FROM Indicadores i");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<Indicadores> indicadores = (List<Indicadores>) query.getResultList();
            return indicadores;
        } catch (Exception e) {
            System.out.println("Error buscarIndicadores PersistenciaIndicadores : " + e.toString());
            return null;
        }
    }

    @Override
    public Indicadores buscarIndicadoresSecuencia(EntityManager em, BigInteger secuencia) {

        try {
            em.clear();
            Query query = em.createQuery("SELECT te FROM Indicadores te WHERE te.secuencia = :secuencia");
            query.setParameter("secuencia", secuencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            Indicadores indicadores = (Indicadores) query.getSingleResult();
            return indicadores;
        } catch (Exception e) {
            System.out.println("Error buscarIndicadoresSecuencia PersistenciaIndicadores : " + e.toString());
            Indicadores indicadores = null;
            return indicadores;
        }
    }
}
