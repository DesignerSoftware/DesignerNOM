/**
 * Documentación a cargo de AndresPineda
 */
package Persistencia;

import Entidades.Tipospagos;
import InterfacePersistencia.PersistenciaTiposPagosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Clase Stateless. <br>
 * Clase encargada de realizar operaciones sobre la tabla 'Tipospagos' de la
 * base de datos.
 *
 * @author AndresPineda
 */
@Stateless
public class PersistenciaTiposPagos implements PersistenciaTiposPagosInterface {

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
/*    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
    private EntityManager em;*/

    @Override
    public void crear(EntityManager em, Tipospagos tipospagos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(tipospagos);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaTiposPagos.crear: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, Tipospagos tipospagos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(tipospagos);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaTiposPagos.editar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, Tipospagos tipospagos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(tipospagos));
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaTiposPagos.borrar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public List<Tipospagos> consultarTiposPagos(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT t FROM Tipospagos t ORDER BY t.codigo  ASC");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<Tipospagos> tipospagos = query.getResultList();
            return tipospagos;
        } catch (Exception e) {
            System.out.println("Error buscarTiposPagos ERROR" + e);
            return null;
        }
    }

    @Override
    public Tipospagos consultarTipoPago(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT t FROM Tipospagos t WHERE t.secuencia =:secuencia");
            query.setParameter("secuencia", secuencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            Tipospagos tipospagos = (Tipospagos) query.getSingleResult();
            return tipospagos;
        } catch (Exception e) {
            System.out.println("Error buscarTipoPagoSecuencia");
            Tipospagos tipospagos = null;
            return tipospagos;
        }
    }

    public BigInteger contarProcesosTipoPago(EntityManager em, BigInteger secuencia) {
        BigInteger retorno = new BigInteger("-1");
        try {
            em.clear();
            String sqlQuery = " SELECT COUNT(*)FROM procesos WHERE tipopago =?";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            retorno = new BigInteger(query.getSingleResult().toString());
            System.out.println("Contador PersistenciaProcesos contarProcesosTipoPago Retorno " + retorno);
            return retorno;
        } catch (Exception e) {
            System.err.println("Error PersistenciaProcesos contarProcesosTipoPago ERROR : " + e);
            return retorno;
        }
    }
}
