/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.SectoresEconomicos;
import InterfacePersistencia.PersistenciaSectoresEconomicosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Clase Stateless. <br> 
 * Clase encargada de realizar operaciones sobre la tabla 'SectoresEconomicos'
 * de la base de datos.
 * @author betelgeuse
 */
@Stateless
public class PersistenciaSectoresEconomicos implements PersistenciaSectoresEconomicosInterface{
    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
//    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
//    private EntityManager em;

    @Override
    public void crear(EntityManager em, SectoresEconomicos sectoresEconomicos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(sectoresEconomicos);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaSectoresEconomicos.crear: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, SectoresEconomicos sectoresEconomicos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(sectoresEconomicos);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaSectoresEconomicos.editar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, SectoresEconomicos sectoresEconomicos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(sectoresEconomicos));
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaSectoresEconomicos.borrar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public List<SectoresEconomicos> buscarSectoresEconomicos(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT se FROM SectoresEconomicos se");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<SectoresEconomicos> sectoresEconomicos = (List<SectoresEconomicos>) query.getResultList();
            return sectoresEconomicos;
        } catch (Exception e) {
            System.out.println("Error buscarSectoresEconomicos PersistenciaSectoresEconomicos : "+e.toString());
            return null;
        }
    }

    @Override
    public SectoresEconomicos buscarSectoresEconomicosSecuencia(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT se FROM SectoresEconomicos se WHERE se.secuencia = :secuencia");
            query.setParameter("secuencia", secuencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            SectoresEconomicos sectoresEconomicos = (SectoresEconomicos) query.getSingleResult();
            return sectoresEconomicos;
        } catch (Exception e) {
            System.out.println("Error buscarSectoresEconomicosSecuencia PersistenciaSectoresEconomicos : "+e.toString());
            SectoresEconomicos sectoresEconomicos = null;
            return sectoresEconomicos;
        }
    }
}
