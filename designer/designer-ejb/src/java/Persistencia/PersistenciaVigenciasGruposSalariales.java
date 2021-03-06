/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.VigenciasGruposSalariales;
import InterfacePersistencia.PersistenciaVigenciasGruposSalarialesInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

@Stateless
public class PersistenciaVigenciasGruposSalariales implements PersistenciaVigenciasGruposSalarialesInterface {

   private static Logger log = Logger.getLogger(PersistenciaVigenciasGruposSalariales.class);

    @Override
    public void crear(EntityManager em, VigenciasGruposSalariales vigenciasGruposSalariales) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(vigenciasGruposSalariales);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaVigenciasGruposSalariales.crear:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, VigenciasGruposSalariales vigenciasGruposSalariales) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(vigenciasGruposSalariales);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaVigenciasGruposSalariales.editar:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, VigenciasGruposSalariales vigenciasGruposSalariales) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(vigenciasGruposSalariales));
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaVigenciasGruposSalariales.borrar:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public List<VigenciasGruposSalariales> buscarVigenciasGruposSalariales(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT v FROM VigenciasGruposSalariales v");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<VigenciasGruposSalariales> vigenciasGruposSalariales = (List<VigenciasGruposSalariales>) query.getResultList();
            return vigenciasGruposSalariales;
        } catch (Exception e) {
            log.error("Error buscarVigenciasGruposSalariales PersistenciaVigenciasGruposSalariales :  ", e);
            return null;
        }
    }

    @Override
    public VigenciasGruposSalariales buscarVigenciaGrupoSalarialSecuencia(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT vgs FROM VigenciasGruposSalariales vgs WHERE vgs.secuencia = :secuencia");
            query.setParameter("secuencia", secuencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            VigenciasGruposSalariales vigenciasGruposSalariales = (VigenciasGruposSalariales) query.getSingleResult();
            return vigenciasGruposSalariales;
        } catch (Exception e) {
            log.error("Error buscarVigenciaGrupoSalarialSecuencia PersistenciaVigenciasGruposSalariales :  ", e);
            return null;
        }
    }

    @Override
    public List<VigenciasGruposSalariales> buscarVigenciaGrupoSalarialSecuenciaGrupoSal(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            String sql = "SELECT * FROM VigenciasGruposSalariales WHERE gruposalarial = ?";
            Query query = em.createNativeQuery(sql, VigenciasGruposSalariales.class);
            query.setParameter(1, secuencia);
            List<VigenciasGruposSalariales> vigenciasGruposSalariales = (List<VigenciasGruposSalariales>) query.getResultList();
            return vigenciasGruposSalariales;
        } catch (Exception e) {
            log.error("Error buscarVigenciaGrupoSalarialSecuencia PersistenciaVigenciasGruposSalariales :  ", e);
            return null;
        }
    }
}
