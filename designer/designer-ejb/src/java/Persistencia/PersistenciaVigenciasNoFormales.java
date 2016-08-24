/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.VigenciasNoFormales;
import InterfacePersistencia.PersistenciaVigenciasNoFormalesInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;

/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la tabla 'VigenciasNoFormales'
 * de la base de datos.
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaVigenciasNoFormales implements PersistenciaVigenciasNoFormalesInterface {

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     * @param em
     */
    /*    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
     private EntityManager em;
     */
    @Override
    public void crear(EntityManager em, VigenciasNoFormales vigenciasNoFormales) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            //em.merge(vigenciasNoFormales);
            em.persist(vigenciasNoFormales);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaVigenciasNoFormales.crear: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, VigenciasNoFormales vigenciasNoFormales) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(vigenciasNoFormales);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaVigenciasNoFormales.editar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, VigenciasNoFormales vigenciasNoFormales) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(vigenciasNoFormales));
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaVigenciasNoFormales.borrar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public List<VigenciasNoFormales> buscarVigenciasNoFormales(EntityManager em) {
        em.clear();
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(VigenciasNoFormales.class));
        return em.createQuery(cq).getResultList();
    }

    @Override
    public List<VigenciasNoFormales> vigenciasNoFormalesPersona(EntityManager em, BigInteger secuenciaPersona) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT vNF FROM VigenciasNoFormales vNF WHERE vNF.persona.secuencia = :secuenciaPersona ORDER BY vNF.fechavigencia DESC");
            query.setParameter("secuenciaPersona", secuenciaPersona);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<VigenciasNoFormales> listaVigenciasNoFormales = query.getResultList();
            return listaVigenciasNoFormales;
        } catch (Exception e) {
            System.out.println("Error PersistenciaTelefonos.telefonoPersona" + e);
            return null;
        }
    }
}
