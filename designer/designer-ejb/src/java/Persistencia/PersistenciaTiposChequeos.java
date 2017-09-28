/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import InterfacePersistencia.PersistenciaTiposChequeosInterface;
import Entidades.TiposChequeos;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

@Stateless
public class PersistenciaTiposChequeos implements PersistenciaTiposChequeosInterface {

   private static Logger log = Logger.getLogger(PersistenciaTiposChequeos.class);

    @Override
    public void crear(EntityManager em, TiposChequeos tiposChequeos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(tiposChequeos);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaTiposChequeos.crear:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, TiposChequeos tiposChequeos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(tiposChequeos);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaTiposChequeos.editar:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, TiposChequeos tiposChequeos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(tiposChequeos));
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaTiposChequeos.borrar:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public TiposChequeos buscarTipoChequeo(EntityManager em, BigInteger secuenciaTC) {
        try {
            em.clear();
            return em.find(TiposChequeos.class, secuenciaTC);
        } catch (Exception e) {
            log.error("PersistenciaTiposChequeos.buscarTipoChequeo():  ", e);
            return null;
        }
    }

    @Override
    public List<TiposChequeos> buscarTiposChequeos(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT tc FROM TiposChequeos tc ORDER BY tc.codigo ASC ");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<TiposChequeos> listMotivosDemandas = query.getResultList();
            return listMotivosDemandas;
        } catch (Exception e) {
            log.error("PersistenciaTiposChequeos.buscarTiposChequeos():  ", e);
            return null;
        }
    }

    @Override
    public BigInteger contadorChequeosMedicos(EntityManager em, BigInteger secuencia) {
        BigInteger retorno;
        try {
            em.clear();
            String sqlQuery = " SELECT COUNT(*)FROM chequeosmedicos cm WHERE cm.tipochequeo = ? ";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            retorno = new BigInteger(query.getSingleResult().toString());
            return retorno;
        } catch (Exception e) {
            log.error("PersistenciaTiposChequeos.contadorChequeosMedicos():  ", e);
            retorno = new BigInteger("-1");
            return retorno;
        }
    }

    @Override
    public BigInteger contadorTiposExamenesCargos(EntityManager em, BigInteger secuencia) {
        BigInteger retorno;
        try {
            em.clear();
            log.warn("Persistencia secuencia borrado " + secuencia);
            String sqlQuery = " SELECT COUNT(*)FROM tiposexamenescargos cm WHERE cm.tipochequeo = ? ";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            retorno = new BigInteger(query.getSingleResult().toString());
            log.warn("PERSISTENCIAJUZGADOS CONTADORCHEQUEOSMEDICOS = " + retorno);
            return retorno;
        } catch (Exception e) {
            log.error("PersistenciaTiposChequeos.contadorTiposExamenesCargos():  ", e);
            retorno = new BigInteger("-1");
            return retorno;
        }
    }
}
