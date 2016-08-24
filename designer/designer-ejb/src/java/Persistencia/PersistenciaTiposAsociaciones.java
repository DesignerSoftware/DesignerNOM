/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.TiposAsociaciones;
import InterfacePersistencia.PersistenciaTiposAsociacionesInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Clase Stateless.<br> 
 * Clase encargada de realizar operaciones sobre la tabla 'TiposAsociaciones'
 * de la base de datos.
 * @author betelgeuse
 */
@Stateless
public class PersistenciaTiposAsociaciones implements PersistenciaTiposAsociacionesInterface{
    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
/*    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
    private EntityManager em;
*/

    @Override
    public void crear(EntityManager em, TiposAsociaciones tiposAsociaciones) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(tiposAsociaciones);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaTiposAsociaciones.crear: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, TiposAsociaciones tiposAsociaciones) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(tiposAsociaciones);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaTiposAsociaciones.editar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, TiposAsociaciones tiposAsociaciones) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(tiposAsociaciones);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaTiposAsociaciones.borrar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public List<TiposAsociaciones> buscarTiposAsociaciones(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT t FROM TiposAsociaciones t");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<TiposAsociaciones> tiposAsociaciones = (List<TiposAsociaciones>) query.getResultList();
            return tiposAsociaciones;
        } catch (Exception e) {
            System.out.println("Error buscarTiposAsociaciones");
            return null;
        }
    }

    @Override
    public TiposAsociaciones buscarTiposAsociacionesSecuencia(EntityManager em, BigInteger secuencia) {

        try {
            em.clear();
            Query query = em.createQuery("SELECT t FROM TiposAsociaciones t WHERE t.secuencia = :secuencia");
            query.setParameter("secuencia", secuencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            TiposAsociaciones tiposAsociaciones = (TiposAsociaciones) query.getSingleResult();
            return tiposAsociaciones;
        } catch (Exception e) {
            System.out.println("Error buscarTiposAsociacionesSecuencia");
            TiposAsociaciones tiposAsociaciones = null;
            return tiposAsociaciones;
        }
    }
}
