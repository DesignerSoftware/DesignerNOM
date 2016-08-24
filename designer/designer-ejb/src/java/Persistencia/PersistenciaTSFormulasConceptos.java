/**
 * Documentación a cargo de AndresPineda
 */
package Persistencia;

import Entidades.TSFormulasConceptos;
import InterfacePersistencia.PersistenciaTSFormulasConceptosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la tabla 'TSFormulasConceptos'
 * de la base de datos.
 *
 * @author AndresPineda
 */
@Stateless
public class PersistenciaTSFormulasConceptos implements PersistenciaTSFormulasConceptosInterface {

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
/*    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
    private EntityManager em;
*/

    @Override
    public void crear(EntityManager em, TSFormulasConceptos tSFormulasConceptos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(tSFormulasConceptos);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaTSFormulasConceptos.crear: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, TSFormulasConceptos tSFormulasConceptos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(tSFormulasConceptos);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaTSFormulasConceptos.editar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, TSFormulasConceptos tSFormulasConceptos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(tSFormulasConceptos));
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaTSFormulasConceptos.borrar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public List<TSFormulasConceptos> buscarTSFormulasConceptos(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT t FROM TSFormulasConceptos t ORDER BY t.secuencia ASC");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<TSFormulasConceptos> tSFormulasConceptos = (List<TSFormulasConceptos>) query.getResultList();
            return tSFormulasConceptos;
        } catch (Exception e) {
            System.out.println("Error buscarTSFormulasConceptos PersistenciaTSFormulasConceptos : " + e.toString());
            return null;
        }
    }

    @Override
    public TSFormulasConceptos buscarTSFormulaConceptoSecuencia(EntityManager em, BigInteger secTSFormula) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT t FROM TSFormulasConceptos t WHERE t.secuencia = :secTSFormula");
            query.setParameter("secTSFormula", secTSFormula);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            TSFormulasConceptos tSFormulasConceptos = (TSFormulasConceptos) query.getSingleResult();
            return tSFormulasConceptos;
        } catch (Exception e) {
            System.out.println("Error buscarTSFormulaConceptoSecuencia PersistenciaTSFormulasConceptos : " + e.toString());
            TSFormulasConceptos tSFormulasConceptos = null;
            return tSFormulasConceptos;
        }
    }

    @Override
    public List<TSFormulasConceptos> buscarTSFormulasConceptosPorSecuenciaTipoSueldo(EntityManager em, BigInteger secTipoSueldo) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT t FROM TSFormulasConceptos t WHERE t.tiposueldo.secuencia =:secTipoSueldo");
            query.setParameter("secTipoSueldo", secTipoSueldo);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<TSFormulasConceptos> tEFormulasConceptos = (List<TSFormulasConceptos>) query.getResultList();
            return tEFormulasConceptos;
        } catch (Exception e) {
            System.out.println("Error buscarTSFormulasConceptosPorSecuenciaTipoSueldo PersistenciaTSFormulasConceptos : " + e.toString());
            return null;
        }
    }
}
