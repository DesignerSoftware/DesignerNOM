/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.TiposDescansos;
import InterfacePersistencia.PersistenciaTiposDescansosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la tabla 'TiposDescansos' de la
 * base de datos.
 *
 * @author AndresPineda
 */
@Stateless
public class PersistenciaTiposDescansos implements PersistenciaTiposDescansosInterface {

   private static Logger log = Logger.getLogger(PersistenciaTiposDescansos.class);

    /*    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
     private EntityManager em;*/
    @Override
    public void crear(EntityManager em, TiposDescansos tiposDescansos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(tiposDescansos);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaTiposDescansos.crear:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, TiposDescansos tiposDescansos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(tiposDescansos);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaTiposDescansos.editar:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, TiposDescansos tiposDescansos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(tiposDescansos));
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaTiposDescansos.borrar:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public List<TiposDescansos> consultarTiposDescansos(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT t FROM TiposDescansos t");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<TiposDescansos> listaTiposDescansos = query.getResultList();
            return listaTiposDescansos;
        } catch (Exception e) {
            log.error("Error buscarTiposDescansos PersistenciaTiposDescansos ", e);
            return null;
        }
    }

    @Override
    public TiposDescansos consultarTipoDescanso(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT tp FROM TiposDescansos tp WHERE tp.secuencia = :secuencia");
            query.setParameter("secuencia", secuencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            TiposDescansos tiposDescansos = (TiposDescansos) query.getSingleResult();
            return tiposDescansos;
        } catch (Exception e) {
            log.error("Error buscarTiposDescansosSecuencia PersistenciaTiposDescansos ", e);
            TiposDescansos tiposDescansos = null;
            return tiposDescansos;
        }
    }

    public BigInteger contarVigenciasJornadasTipoDescanso(EntityManager em, BigInteger secuencia) {
        BigInteger retorno = new BigInteger("-1");
        try {
            em.clear();
            String sqlQuery = "SELECT COUNT(*)FROM vigenciasjornadas WHERE tipodescanso=?";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            retorno = new BigInteger(query.getSingleResult().toString());
            log.warn("PersistenciaTiposDescansos contarVigenciasJornadasTipoDescanso contador" + retorno);
            return retorno;
        } catch (Exception e) {
            log.error("PersistenciaTiposDescansos.contarVigenciasJornadasTipoDescanso():  ", e);
            return retorno;
        }
    }
}
