/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.Rubrospresupuestales;
import InterfacePersistencia.PersistenciaRubrosPresupuestalesInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Clase Stateless. <br>
 * Clase encargada de realizar operaciones sobre la tabla 'RubrosPresupuestales'
 * de la base de datos.
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaRubrosPresupuestales implements PersistenciaRubrosPresupuestalesInterface {

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
//    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
//    private EntityManager em;

    @Override
    public void crear(EntityManager em, Rubrospresupuestales rubrospresupuestales) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(rubrospresupuestales);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaRubrosPresupuestales.crear: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, Rubrospresupuestales rubrospresupuestales) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(rubrospresupuestales);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaRubrosPresupuestales.editar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, Rubrospresupuestales rubrospresupuestales) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(rubrospresupuestales);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaRubrosPresupuestales.borrar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public List<Rubrospresupuestales> buscarRubros(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT r FROM Rubrospresupuestales r");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<Rubrospresupuestales> rubrospresupuestales = (List<Rubrospresupuestales>) query.getResultList();
            return rubrospresupuestales;
        } catch (Exception e) {
            System.out.println("Error buscarCuentas PersistenciaCuentas : " + e.toString());
            return null;
        }
    }

    @Override
    public Rubrospresupuestales buscarRubrosSecuencia(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT c FROM Rubrospresupuestales c WHERE c.secuencia = :secuencia");
            query.setParameter("secuencia", secuencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            Rubrospresupuestales cuentas = (Rubrospresupuestales) query.getSingleResult();
            return cuentas;
        } catch (Exception e) {
            System.out.println("Error buscarCuentasSecuencia PersistenciaCuentas : " + e.toString());
            Rubrospresupuestales cuentas = null;
            return cuentas;
        }
    }
}
