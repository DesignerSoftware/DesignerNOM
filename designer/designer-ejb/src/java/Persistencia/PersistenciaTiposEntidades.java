/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.TiposEntidades;
import InterfacePersistencia.PersistenciaTiposEntidadesInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

@Stateless
public class PersistenciaTiposEntidades implements PersistenciaTiposEntidadesInterface {

   private static Logger log = Logger.getLogger(PersistenciaTiposEntidades.class);

    @Override
    public void crear(EntityManager em, TiposEntidades tiposEntidades) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(tiposEntidades);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaTiposEntidades.crear: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, TiposEntidades tiposEntidades) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(tiposEntidades);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaTiposEntidades.editar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, TiposEntidades tiposEntidades) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(tiposEntidades));
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaTiposEntidades.borrar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public List<TiposEntidades> buscarTiposEntidades(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT te FROM TiposEntidades te ORDER BY te.codigo");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<TiposEntidades> tiposEntidades = query.getResultList();
            return tiposEntidades;
        } catch (Exception e) {
            log.error("Persistencia.PersistenciaTiposEntidades.buscarTiposEntidades()" + e.getMessage());
            return null;
        }
    }

    @Override
    public TiposEntidades buscarTiposEntidadesSecuencia(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT te FROM TiposEntidades te WHERE te.secuencia = :secuencia");
            query.setParameter("secuencia", secuencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            TiposEntidades tiposEntidades = (TiposEntidades) query.getSingleResult();
            return tiposEntidades;
        } catch (Exception e) {
            log.error("Persistencia.PersistenciaTiposEntidades.buscarTiposEntidadesSecuencia()" + e.getMessage());
            return null;
        }
    }

    @Override
    public BigInteger verificarBorrado(EntityManager em, BigInteger secTipoEntidad) {
        try {
            em.clear();
            BigInteger retorno = new BigInteger("-1");
            Query query = em.createQuery("SELECT COUNT(va) FROM VigenciasAfiliaciones va WHERE va.tipoentidad.secuencia = :secuencia");
            query.setParameter("secuencia", secTipoEntidad);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            return retorno = new BigInteger(query.getSingleResult().toString());
        } catch (Exception e) {
            log.error("Persistencia.PersistenciaTiposEntidades.verificarBorrado()" + e.getMessage());
            return null;
        }
    }

    @Override
    public BigInteger verificarBorradoFCE(EntityManager em, BigInteger secTipoEntidad) {
        try {
            em.clear();
            BigInteger retorno = new BigInteger("-1");
            Query query = em.createQuery("SELECT COUNT(fce) FROM FormulasContratosEntidades fce WHERE fce.tipoentidad.secuencia = :secuencia");
            query.setParameter("secuencia", secTipoEntidad);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            return retorno = new BigInteger(query.getSingleResult().toString());
        } catch (Exception e) {
            log.error("Persistencia.PersistenciaTiposEntidades.verificarBorradoFCE()" + e.getMessage());
            return null;
        }
    }

    @Override
    public List<TiposEntidades> buscarTiposEntidadesIBCS(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT te FROM TiposEntidades te WHERE EXISTS (SELECT gte FROM Grupostiposentidades gte WHERE gte.secuencia = te.grupo.secuencia AND gte.codigo IN(1,8))");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<TiposEntidades> tiposEntidades = (List<TiposEntidades>) query.getResultList();
            return tiposEntidades;
        } catch (Exception e) {
            log.error("Persistencia.PersistenciaTiposEntidades.buscarTiposEntidadesIBCS()" + e.getMessage());
            return null;
        }
    }

    @Override
    public List<TiposEntidades> buscarTiposEntidadesPorSecuenciaGrupo(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT te FROM TiposEntidades te WHERE te.grupo.secuencia=:secuencia");
            query.setParameter("secuencia", secuencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<TiposEntidades> tiposEntidades = (List<TiposEntidades>) query.getResultList();
            return tiposEntidades;
        } catch (Exception e) {
            log.error("Persistencia.PersistenciaTiposEntidades.buscarTiposEntidadesPorSecuenciaGrupo()" + e.getMessage());
            return null;
        }
    }

    @Override
    public TiposEntidades buscarTipoEntidadPorCodigo(EntityManager em, Short codigo) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT te FROM TiposEntidades te WHERE te.codigo=:codigo");
            query.setParameter("codigo", codigo);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            TiposEntidades tiposEntidades = (TiposEntidades) query.getSingleResult();
            return tiposEntidades;
        } catch (Exception e) {
            log.error("Error buscarTipoEntidadPorCodigo PersistenciaTiposEntidades : " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<TiposEntidades> buscarTiposEntidadesParametroAutoliq(EntityManager em) {
        try {
            em.clear();
            String sql = "SELECT * FROM TIPOSENTIDADES te\n"
                    + "   where exists (select 'x' from grupostiposentidades gte\n"
                    + "   where gte.secuencia=te.grupo\n"
                    + "   and gte.requeridopila='S')";
            Query query = em.createNativeQuery(sql, TiposEntidades.class);
            List<TiposEntidades> tiposEntidades = query.getResultList();
            return tiposEntidades;
        } catch (Exception e) {
            log.error("Error buscarTiposEntidadesParametroAutoliq PersistenciaTiposEntidades : " + e.getMessage());
            return null;
        }
    }
}
