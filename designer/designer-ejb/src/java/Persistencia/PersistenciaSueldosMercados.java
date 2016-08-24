/**
 * Documentación a cargo de AndresPineda
 */
package Persistencia;

import Entidades.SueldosMercados;
import InterfacePersistencia.PersistenciaSueldosMercadosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Clase Stateless. <br>
 * Clase encargada de realizar operaciones sobre la tabla 'SueldosMercados' de
 * la base de datos.
 *
 * @author AndresPineda
 */
@Stateless
public class PersistenciaSueldosMercados implements PersistenciaSueldosMercadosInterface {

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
//    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
//    private EntityManager em;

    @Override
    public void crear(EntityManager em, SueldosMercados sueldosMercados) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(sueldosMercados);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaSueldosMercados.crear: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, SueldosMercados sueldosMercados) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(sueldosMercados);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaSueldosMercados.editar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, SueldosMercados sueldosMercados) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(sueldosMercados));
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaSueldosMercados.borrar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public List<SueldosMercados> buscarSueldosMercados(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT sm FROM SueldosMercados sm ORDER BY sm.sueldomax ASC");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<SueldosMercados> sueldosMercados = query.getResultList();
            return sueldosMercados;
        } catch (Exception e) {
            System.out.println("Error buscarSueldosMercados PersistenciaSueldosMercados : " + e.toString());
            return null;
        }
    }

    @Override
    public SueldosMercados buscarSueldosMercadosSecuencia(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT sm FROM SueldosMercados sm WHERE sm.secuencia = :secuencia");
            query.setParameter("secuencia", secuencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            SueldosMercados sueldosMercados = (SueldosMercados) query.getSingleResult();
            return sueldosMercados;
        } catch (Exception e) {
            System.out.println("Error buscarSueldosMercadosSecuencia PersistenciaSueldosMercados : " + e.toString());
            SueldosMercados sueldosMercados = null;
            return sueldosMercados;
        }
    }

    @Override
    public List<SueldosMercados> buscarSueldosMercadosPorSecuenciaCargo(EntityManager em, BigInteger secCargo) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT sm FROM SueldosMercados sm WHERE sm.cargo.secuencia=:secCargo");
            query.setParameter("secCargo", secCargo);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<SueldosMercados> sueldosMercados = query.getResultList();
            return sueldosMercados;
        } catch (Exception e) {
            System.out.println("Error buscarSueldosMercadosPorSecuenciaCargo PersistenciaSueldosMercados : " + e.toString());
            return null;
        }
    }

}
