/**
 * Documentación a cargo de Andres Pineda
 */
package Persistencia;

import Entidades.OperandosGruposConceptos;
import InterfacePersistencia.PersistenciaOperandosGruposConceptosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;

/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la tabla
 * 'OperandosGruposConceptos' de la base de datos.
 *
 * @author Andres Pineda.
 */
@Stateless
public class PersistenciaOperandosGruposConceptos implements PersistenciaOperandosGruposConceptosInterface {

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos
     */
//    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
//    private EntityManager em;

    @Override
    public void crear(EntityManager em, OperandosGruposConceptos gruposConceptos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(gruposConceptos);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaMotivosCambiosSueldos.crear: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, OperandosGruposConceptos gruposConceptos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(gruposConceptos);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaMotivosCambiosSueldos.editar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, OperandosGruposConceptos gruposConceptos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(gruposConceptos));
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaMotivosCambiosSueldos.borrar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public List<OperandosGruposConceptos> buscarOperandosGruposConceptos(EntityManager em) {
        try {
            em.clear();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(OperandosGruposConceptos.class));
            return em.createQuery(cq).getResultList();
        } catch (Exception e) {
            System.out.println("Error buscarOperandosGruposConceptos PersistenciaOperandosGruposConceptos : " + e.toString());
            return null;
        }
    }

    @Override
    public OperandosGruposConceptos buscarOperandosGruposConceptosPorSecuencia(EntityManager em, BigInteger secOperando) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT o FROM OperandosGruposConceptos o WHERE o.secuencia=:secOperando");
            query.setParameter("secOperando", secOperando);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            OperandosGruposConceptos gruposConceptos = (OperandosGruposConceptos) query.getSingleResult();
            return gruposConceptos;
        } catch (Exception e) {
            System.out.println("Error buscarOperandosGruposConceptosPorSecuencia (PersistenciaOperandosGruposConceptos) : " + e.toString());
            return null;
        }
    }

    @Override
    public List<OperandosGruposConceptos> buscarOperandosGruposConceptosPorProcesoSecuencia(EntityManager em, BigInteger secProceso) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT o FROM OperandosGruposConceptos o WHERE o.proceso.secuencia=:secuencia");
            query.setParameter("secuencia", secProceso);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<OperandosGruposConceptos> gruposConceptos = query.getResultList();
            return gruposConceptos;
        } catch (Exception e) {
            System.out.println("Error buscarOperandosGruposConceptosPorProcesoSecuencia (PersistenciaOperandosGruposConceptos) : " + e.toString());
            return null;
        }
    }

}
