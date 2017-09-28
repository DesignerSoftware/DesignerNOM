/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.TiposPensionados;
import InterfacePersistencia.PersistenciaTiposPensionadosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

@Stateless
public class PersistenciaTiposPensionados implements PersistenciaTiposPensionadosInterface {

   private static Logger log = Logger.getLogger(PersistenciaTiposPensionados.class);

   @Override
    public void crear(EntityManager em, TiposPensionados tiposPensionados) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(tiposPensionados);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaTiposPensionados.crear:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, TiposPensionados tiposPensionados) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(tiposPensionados);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaTiposPensionados.editar:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, TiposPensionados tiposPensionados) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(tiposPensionados));
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaTiposPensionados.borrar:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public List<TiposPensionados> consultarTiposPensionados(EntityManager em) {
        try {
            em.clear();
            String sql = "SELECT * FROM  TiposPensionados";
            Query query = em.createNativeQuery(sql, TiposPensionados.class);
            //query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<TiposPensionados> listaTiposPensionados = query.getResultList();
            return listaTiposPensionados;
        } catch (Exception e) {
            log.error("PersistenciaTiposPensionados.consultarTiposPensionados():  ", e);
            return null;
        }
    }

    @Override
    public TiposPensionados consultarTipoPensionado(EntityManager em, BigInteger secuencia) {

        try {
            em.clear();
            Query query = em.createQuery("SELECT tp FROM TiposPensionados tp WHERE tp.secuencia = :secuencia");
            query.setParameter("secuencia", secuencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            TiposPensionados tipoP = (TiposPensionados) query.getSingleResult();
            return tipoP;
        } catch (Exception e) {
            log.error("Error buscarTipoPensionSecuencia PersistenciaTiposPensionados  ", e);
            TiposPensionados tipoP = null;
            return tipoP;
        }
    }

    @Override
    public BigInteger contarPensionadosTipoPension(EntityManager em, BigInteger secuencia) {
        BigInteger retorno = new BigInteger("-1");
        try {
            em.clear();
            String sqlQuery = "SELECT COUNT(*)FROM pensionados WHERE tipopensionado=?";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            retorno = new BigInteger(query.getSingleResult().toString());
            return retorno;
        } catch (Exception e) {
            log.error("Error PersistenciaMotivosRetiros   contarRetiradosClasePension.  ", e);
            return retorno;
        }
    }
}
