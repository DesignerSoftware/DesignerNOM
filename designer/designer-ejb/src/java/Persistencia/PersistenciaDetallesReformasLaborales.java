/**
 * Documentación a cargo de AndresPineda
 */
package Persistencia;

import Entidades.DetallesReformasLaborales;
import InterfacePersistencia.PersistenciaDetallesReformasLaboralesInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Clase Stateless. <br>
 * Clase encargada de realizar operaciones sobre la tabla
 * 'DetallesReformasLaborales' de la base de datos.
 *
 * @author AndresPineda
 */
@Stateless
public class PersistenciaDetallesReformasLaborales implements PersistenciaDetallesReformasLaboralesInterface {

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
    /*@PersistenceContext(unitName = "DesignerRHN-ejbPU")
    private EntityManager em;*/

    @Override
    public void crear(EntityManager em,DetallesReformasLaborales detallesReformasLaborales) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(detallesReformasLaborales);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaDetallesReformasLaborales.crear: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em,DetallesReformasLaborales detallesReformasLaborales) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(detallesReformasLaborales);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaDetallesReformasLaborales.crear: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em,DetallesReformasLaborales detallesReformasLaborales) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(detallesReformasLaborales));
            tx.commit();

        } catch (Exception e) {
            try {
                if (tx.isActive()) {
                    tx.rollback();
                }
            } catch (Exception ex) {
                System.out.println("Error PersistenciaDetallesReformasLaborales.borrar: " + e);
            }
        }
    }

    @Override
    public List<DetallesReformasLaborales> buscarDetallesReformasLaborales(EntityManager em) {
        em.clear();
        Query query = em.createQuery("SELECT d FROM DetallesReformasLaborales d");
        query.setHint("javax.persistence.cache.storeMode", "REFRESH");
        List<DetallesReformasLaborales> detallesReformasLaborales = (List<DetallesReformasLaborales>) query.getResultList();
        return detallesReformasLaborales;
    }

    @Override
    public DetallesReformasLaborales buscarDetalleReformaSecuencia(EntityManager em,BigInteger secuencia) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT d FROM DetallesReformasLaborales d WHERE d.secuencia = :secuencia");
            query.setParameter("secuencia", secuencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            DetallesReformasLaborales detallesReformasLaborales = (DetallesReformasLaborales) query.getSingleResult();
            return detallesReformasLaborales;
        } catch (Exception e) {
            System.out.println("Error buscarDetalleReformaSecuencia PersistenciaDetallesReformasLaborales : " + e.toString());
            DetallesReformasLaborales detallesReformasLaborales = null;
            return detallesReformasLaborales;
        }
    }

    @Override 
    public List<DetallesReformasLaborales> buscarDetalleReformasParaReformaSecuencia(EntityManager em,BigInteger secuencia) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT d FROM DetallesReformasLaborales d WHERE d.reformalaboral.secuencia=:secuencia ORDER BY d.factor ASC");
            query.setParameter("secuencia", secuencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<DetallesReformasLaborales> detallesReformasLaborales = (List<DetallesReformasLaborales>) query.getResultList();
            return detallesReformasLaborales;
        } catch (Exception e) {
            System.out.println("Error buscarDetalleReformasParaReformaSecuencia PersistenciaDetallesReformasLaborales : " + e.toString());
            List<DetallesReformasLaborales> detallesReformasLaborales = null;
            return detallesReformasLaborales;
        }
    }
}
