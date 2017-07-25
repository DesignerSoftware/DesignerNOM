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
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

@Stateless
public class PersistenciaTiposPagos implements PersistenciaTiposPagosInterface {

   private static Logger log = Logger.getLogger(PersistenciaTiposPagos.class);

    @Override
    public void crear(EntityManager em, Tipospagos tipospagos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(tipospagos);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaTiposPagos.crear: " + e.getMessage());
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
            log.error("Error PersistenciaTiposPagos.editar: " + e.getMessage());
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
            log.error("Error PersistenciaTiposPagos.borrar: " + e.getMessage());
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
            log.error("Persistencia.PersistenciaTiposPagos.consultarTiposPagos()" + e.getMessage());
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
            log.error("Error buscarTipoPagoSecuencia" + e.getMessage());
            return null;
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
            return retorno;
        } catch (Exception e) {
            log.error("Persistencia.PersistenciaTiposPagos.contarProcesosTipoPago()"+ e.getMessage());
            return retorno;
        }
    }
}
