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
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la tabla 'TiposEntidades' de la
 * base de datos.
 *
 * @author AndresPineda
 */
@Stateless
public class PersistenciaTiposEntidades implements PersistenciaTiposEntidadesInterface {

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
    /*    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
     private EntityManager em;
     */
    @Override
    public void crear(EntityManager em, TiposEntidades tiposEntidades) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(tiposEntidades);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaTiposEntidades.crear: " + e);
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
            System.out.println("Error PersistenciaTiposEntidades.editar: " + e);
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
            System.out.println("Error PersistenciaTiposEntidades.borrar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public List<TiposEntidades> buscarTiposEntidades(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT te FROM TiposEntidades te");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<TiposEntidades> tiposEntidades = query.getResultList();
            return tiposEntidades;
        } catch (Exception e) {
            System.out.println("Error buscarTiposEntidades");
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
            System.out.println("Error buscarTiposEntidadesSecuencia");
            TiposEntidades tiposEntidades = null;
            return tiposEntidades;
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
            System.err.println("Error PersistenciaTiposEntidades.verificarBorrado.");
            System.err.println("Exception: " + e);
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
            System.err.println("Error PersistenciaTiposEntidades.verificarBorradoFCE.");
            System.err.println("Exception: " + e);
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
            System.out.println("Error buscarTiposEntidadesIBCS PersistenciaTiposEntidades : " + e.toString());
            List<TiposEntidades> tiposEntidades = null;
            return tiposEntidades;
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
            System.out.println("Error buscarTiposEntidadesPorSecuenciaGrupo PersistenciaTiposEntidades : " + e.toString());
            List<TiposEntidades> tiposEntidades = null;
            return tiposEntidades;
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
            System.out.println("Error buscarTipoEntidadPorCodigo PersistenciaTiposEntidades : " + e.toString());
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
            System.out.println("Error buscarTiposEntidadesParametroAutoliq PersistenciaTiposEntidades : " + e.toString());
            return null;
        }
    }
}
