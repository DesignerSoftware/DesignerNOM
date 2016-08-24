/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.VigenciasFormales;
import InterfacePersistencia.PersistenciaVigenciasFormalesInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
//import javax.persistence.PersistenceContext;
//import javax.persistence.PersistenceException;
import javax.persistence.Query;

/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la tabla 'VigenciasFormales' de
 * la base de datos.
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaVigenciasFormales implements PersistenciaVigenciasFormalesInterface {

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     *
     * @param em
     * @param vigenciasFormales
     */
    /*    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
     private EntityManager em;
     */
    @Override
    public void crear(EntityManager em, VigenciasFormales vigenciasFormales) {
        System.out.println(this.getClass().getName() + ".crear()");
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
//            em.merge(vigenciasFormales);
            em.persist(vigenciasFormales);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaVigenciasFormales.crear: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
            System.out.println("la transacción se cerró");
        }
    }

    @Override
    public void editar(EntityManager em, VigenciasFormales vigenciasFormales) {
        System.out.println(this.getClass().getName() + ".editar()");
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(vigenciasFormales);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaVigenciasFormales.editar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
            System.out.println("la transacción se cerró");
        }
    }

    @Override
    public void borrar(EntityManager em, VigenciasFormales vigenciasFormales) {
        System.out.println(this.getClass().getName() + ".borrar()");
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(vigenciasFormales));
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaVigenciasFormales.borrar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
            System.out.println("la transacción se cerró");
        }
    }

    @Override
    public List<VigenciasFormales> buscarVigenciasFormales(EntityManager em) {
        System.out.println(this.getClass().getName() + ".buscarVigenciasFormales()");
        try {
            em.clear();
            javax.persistence.criteria.CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(VigenciasFormales.class));
            return em.createQuery(cq).getResultList();
        } catch (Exception e) {
            System.out.println("error en buscarVigenciasFormales");
            return null;
        }
    }

    private Long contarEducacionPersona(EntityManager em, BigInteger secuencia) {
        System.out.println(this.getClass().getName() + ".contarEducacionPersona()");
        Long resultado = null;
        try {
            em.clear();
            Query query = em.createQuery("SELECT COUNT(vf) FROM VigenciasFormales vf WHERE vf.persona.secuencia = :secuenciaPersona");
            query.setParameter("secuenciaPersona", secuencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            resultado = (Long) query.getSingleResult();
            return resultado;
        } catch (Exception e) {
            System.out.println("error en contarEducacionPersona");
            e.printStackTrace();
            return resultado;
        }
    }

    @Override
    public List<VigenciasFormales> educacionPersona(EntityManager em, BigInteger secuencia) {
        System.out.println(this.getClass().getName() + ".educacionPersona()");
        Long resultado = this.contarEducacionPersona(em, secuencia);
        if (resultado > 0) {
            try {
                /*em.clear();
                 Query query = em.createQuery("SELECT COUNT(vf) FROM VigenciasFormales vf WHERE vf.persona.secuencia = :secuenciaPersona");
                 query.setParameter("secuenciaPersona", secuencia);
                 query.setHint("javax.persistence.cache.storeMode", "REFRESH");
                 Long resultado = (Long) query.getSingleResult();*/
                Query queryFinal = em.createQuery("SELECT vf FROM VigenciasFormales vf WHERE vf.persona.secuencia = :secuenciaPersona and vf.fechavigencia = (SELECT MAX(vfo.fechavigencia) FROM VigenciasFormales vfo WHERE vfo.persona.secuencia = :secuenciaPersona)");
                queryFinal.setParameter("secuenciaPersona", secuencia);
                List<VigenciasFormales> listaVigenciasFormales = queryFinal.getResultList();
                return listaVigenciasFormales;
            } catch (Exception e) {
                System.out.println("Error PersistenciaVigenciasFormales.educacionPersona" + e);
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public List<VigenciasFormales> vigenciasFormalesPersona(EntityManager em, BigInteger secuencia) {
        System.out.println(this.getClass().getName() + ".vigenciasFormalesPersona()");
        try {
            em.clear();
            Query query = em.createQuery("SELECT vF FROM VigenciasFormales vF WHERE vF.persona.secuencia = :secuenciaPersona ORDER BY vF.fechavigencia DESC");
            query.setParameter("secuenciaPersona", secuencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<VigenciasFormales> listaVigenciasFormales = query.getResultList();
            return listaVigenciasFormales;
        } catch (Exception e) {
            System.out.println("Error PersistenciaTelefonos.telefonoPersona" + e);
            return null;
        }
    }
}
