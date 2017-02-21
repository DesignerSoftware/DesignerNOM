/**
 * Documentación a cargo de AndresPineda
 */
package Persistencia;

import Entidades.TEFormulasConceptos;
import InterfacePersistencia.PersistenciaTEFormulasConceptosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class PersistenciaTEFormulasConceptos implements PersistenciaTEFormulasConceptosInterface {


    @Override
    public void crear(EntityManager em, TEFormulasConceptos tEFormulasConceptos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(tEFormulasConceptos);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaTEFormulasConceptos.crear: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, TEFormulasConceptos tEFormulasConceptos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(tEFormulasConceptos);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaTEFormulasConceptos.editar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, TEFormulasConceptos tEFormulasConceptos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(tEFormulasConceptos));
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaTEFormulasConceptos.borrar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public List<TEFormulasConceptos> buscarTEFormulasConceptos(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT t FROM TEFormulasConceptos t ORDER BY t.secuencia ASC");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<TEFormulasConceptos> tEFormulasConceptos = (List<TEFormulasConceptos>) query.getResultList();
            return tEFormulasConceptos;
        } catch (Exception e) {
            System.out.println("Error buscarTEFormulasConceptos PersistenciaTEFormulasConceptos : " + e.toString());
            return null;
        }
    }

    @Override
    public TEFormulasConceptos buscarTEFormulaConceptoSecuencia(EntityManager em, BigInteger secTEFormula) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT t FROM TEFormulasConceptos t WHERE t.secuencia = :secTEFormula");
            query.setParameter("secTEFormula", secTEFormula);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            TEFormulasConceptos tEFormulasConceptos = (TEFormulasConceptos) query.getSingleResult();
            return tEFormulasConceptos;
        } catch (Exception e) {
            System.out.println("Error buscarTEFormulaConceptoSecuencia PersistenciaTEFormulasConceptos : " + e.toString());
            return null;
        }
    }

    @Override
    public List<TEFormulasConceptos> buscarTEFormulasConceptosPorSecuenciaTSGrupoTipoEntidad(EntityManager em, BigInteger secTSGrupo) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT t FROM TEFormulasConceptos t WHERE t.tsgrupotipoentidad.secuencia =:secTSGrupo");
            query.setParameter("secTSGrupo", secTSGrupo);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<TEFormulasConceptos> tEFormulasConceptos = (List<TEFormulasConceptos>) query.getResultList();
            return tEFormulasConceptos;
        } catch (Exception e) {
            System.out.println("Error buscarTEFormulasConceptosPorSecuenciaTSGrupoTipoEntidad PersistenciaTEFormulasConceptos : " + e.toString());
            return null;
        }
    }
}
