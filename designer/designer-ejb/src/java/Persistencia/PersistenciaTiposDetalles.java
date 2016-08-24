/**
 * Documentación a cargo de AndresPineda
 */
package Persistencia;

import Entidades.TiposDetalles;
import InterfacePersistencia.PersistenciaTiposDetallesInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Clase Stateless. <br>
 * Clase encargada de realizar operaciones sobre la tabla 'TiposDetalles' de la
 * base de datos.
 *
 * @author AndresPineda
 */
@Stateless
public class PersistenciaTiposDetalles implements PersistenciaTiposDetallesInterface {

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
/*    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
    private EntityManager em;
*/

    @Override
    public void crear(EntityManager em, TiposDetalles tiposDetalles) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(tiposDetalles);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaTiposDetalles.crear: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, TiposDetalles tiposDetalles) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(tiposDetalles);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaTiposDetalles.editar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, TiposDetalles tiposDetalles) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(tiposDetalles));
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaTiposDetalles.borrar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public List<TiposDetalles> buscarTiposDetalles(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT td FROM TiposDetalles td ORDER BY td.codigo ASC");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<TiposDetalles> tiposDetalles = query.getResultList();
            return tiposDetalles;
        } catch (Exception e) {
            System.out.println("Error buscarTiposDetalles PersistenciaTiposDetalles : " + e.toString());
            return null;
        }
    }

    @Override
    public TiposDetalles buscarTiposDetallesSecuencia(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT td FROM TiposDetalles td WHERE td.secuencia =:secuencia");
            query.setParameter("secuencia", secuencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            TiposDetalles tiposDetalles = (TiposDetalles) query.getSingleResult();
            return tiposDetalles;
        } catch (Exception e) {
            System.out.println("Error buscarTiposDetallesSecuencia PersistenciaTiposDetalles : " + e.toString());
            TiposDetalles tiposDetalles = null;
            return tiposDetalles;
        }
    }
}
