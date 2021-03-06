/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.TiposSueldos;
import InterfacePersistencia.PersistenciaTiposSueldosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

@Stateless
public class PersistenciaTiposSueldos implements PersistenciaTiposSueldosInterface {

   private static Logger log = Logger.getLogger(PersistenciaTiposSueldos.class);

    @Override
    public void crear(EntityManager em, TiposSueldos tiposSueldos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(tiposSueldos);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaTiposSueldos.crear:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, TiposSueldos tiposSueldos) {

        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(tiposSueldos);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaTiposSueldos.editar:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, TiposSueldos tiposSueldos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(tiposSueldos));
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaTiposSueldos.borrar:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public List<TiposSueldos> buscarTiposSueldos(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT t FROM TiposSueldos t ORDER BY t.codigo ASC");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<TiposSueldos> tiposSueldos = (List<TiposSueldos>) query.getResultList();
            return tiposSueldos;
        } catch (Exception e) {
            log.error("Error buscar lista tipos sueldos ", e);
            return null;
        }
    }

    @Override
    public List<TiposSueldos> buscarTiposSueldosParaUsuarioConectado(EntityManager em) {
        try {
            em.clear();
            String sql = "SELECT v.*  FROM TIPOSSUELDOS V  where exists (select 'x' from usuariostipossueldos uts, usuarios u where u.alias=user and uts.usuario = u.secuencia and uts.tiposueldo=v.secuencia) ORDER BY V.DESCRIPCION";
            Query query = em.createNativeQuery(sql, TiposSueldos.class);
            List<TiposSueldos> tiposSueldos = query.getResultList();
            return tiposSueldos;
        } catch (Exception e) {
            log.error("Error buscarTiposSueldosParaUsuarioConectado PersistenciaTiposSueldos :  ", e);
            return null;
        }
    }

    @Override
    public TiposSueldos buscarTipoSueldoSecuencia(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT t FROM TiposSueldos t WHERE t.secuencia = :secuencia");
            query.setParameter("secuencia", secuencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            TiposSueldos tiposSueldos = (TiposSueldos) query.getSingleResult();
            return tiposSueldos;
        } catch (Exception e) {
            log.error("Error buscar tipo sueldo por secuencia ", e);
            TiposSueldos tiposSueldos = null;
            return tiposSueldos;
        }
    }
}
