/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.GruposConceptos;
import InterfacePersistencia.PersistenciaGruposConceptosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la tabla 'GruposConceptos'
 * de la base de datos.
 * @author AndresPineda
 */
@Stateless
public class PersistenciaGruposConceptos implements PersistenciaGruposConceptosInterface{
    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos
     */
    /*@PersistenceContext(unitName = "DesignerRHN-ejbPU")
    private EntityManager em;*/

    @Override
    public void crear(EntityManager em,GruposConceptos gruposConceptos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(gruposConceptos);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaVigenciasCargos.crear: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em,GruposConceptos gruposConceptos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(gruposConceptos);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaVigenciasCargos.crear: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em,GruposConceptos gruposConceptos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(gruposConceptos));
            tx.commit();

        } catch (Exception e) {
            try {
                if (tx.isActive()) {
                    tx.rollback();
                }
            } catch (Exception ex) {
                System.out.println("Error PersistenciaVigenciasCargos.borrar: " + e);
            }
        }
    }
   
    @Override
    public List<GruposConceptos> buscarGruposConceptos(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT e FROM GruposConceptos e ORDER BY e.codigo ASC");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<GruposConceptos> gruposConceptos = (List<GruposConceptos>) query.getResultList();
            return gruposConceptos;
        } catch (Exception e) {
            System.out.println("error buscarGruposConceptos PersistenciaGruposConceptos");
            return null;
        }
    }

    @Override
    public GruposConceptos buscarGruposConceptosSecuencia(EntityManager em,BigInteger secuencia) {
        GruposConceptos gruposConceptos;
        try {
            em.clear();
            Query query = em.createQuery("SELECT e FROM GruposConceptos e WHERE e.secuencia = :secuencia");
            query.setParameter("secuencia", secuencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            gruposConceptos = (GruposConceptos) query.getSingleResult();
            return gruposConceptos;
        } catch (Exception e) {
            gruposConceptos = null;
            System.out.println("Error buscarGruposConceptosSecuencia PersistenciaGruposConceptos");
            return gruposConceptos;
        }
    }
}
