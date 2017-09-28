/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.Terceros;
import InterfacePersistencia.PersistenciaTercerosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

@Stateless
public class PersistenciaTerceros implements PersistenciaTercerosInterface {

   private static Logger log = Logger.getLogger(PersistenciaTerceros.class);

    @Override
    public void crear(EntityManager em, Terceros terceros) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(terceros);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaTerceros.crear:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, Terceros terceros) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(terceros);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaTerceros.editar:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, Terceros terceros) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(terceros));
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaTerceros.borrar:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public List<Terceros> buscarTerceros(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT t FROM Terceros t, Empresas e, TercerosSucursales ts  WHERE t.secuencia = ts.tercero.secuencia AND t.empresa.secuencia = e.secuencia");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<Terceros> terceros = (List<Terceros>) query.getResultList();
            return terceros;
        } catch (Exception e) {
            log.error("PersistenciaTerceros.buscarTerceros():  ", e);
            return null;
        }
    }
    
    @Override
    public List<Terceros> todosTerceros(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT t FROM Terceros t ORDER BY t.nombre");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<Terceros> terceros = (List<Terceros>) query.getResultList();
            return terceros;
        } catch (Exception e) {
            log.error("PersistenciaTerceros.todosTerceros():  ", e);
            return null;
        }
    }

    @Override
    public List<Terceros> buscarTercerosParametrosAutoliq(EntityManager em) {
        try {
            em.clear();
            String sql = "SELECT * FROM TERCEROS t\n"
                    + "   where exists (select 'x' from tercerossucursales ts\n"
                    + "   where ts.tercero=t.secuencia)";
            Query query = em.createNativeQuery(sql, Terceros.class);
            List<Terceros> terceros = (List<Terceros>) query.getResultList();
            return terceros;
        } catch (Exception e) {
            log.error("Error buscarTercerosParametrosAutoliq PersistenciaTercerosInterface :  ", e);
            return null;
        }
    }

    @Override
    public Terceros buscarTercerosSecuencia(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT t FROM Terceros t WHERE t.secuencia = :secuencia");
            query.setParameter("secuencia", secuencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            Terceros terceros = (Terceros) query.getSingleResult();
            return terceros;
        } catch (Exception e) {
            log.error("PersistenciaTerceros.buscarTercerosSecuencia():  ", e);
            return null;
        }
    }

    @Override
    public boolean verificarTerceroPorNit(EntityManager em, BigInteger nit) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT COUNT(t) FROM Terceros t WHERE t.nit = :nit");
            query.setParameter("nit", nit);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            Long resultado = (Long) query.getSingleResult();
            return resultado > 0;
        } catch (Exception e) {
            log.error("PersistenciaTerceros.verificarTerceroPorNit():  ", e);
            return false;
        }
    }

    @Override
    public boolean verificarTerceroParaEmpresaEmpleado(EntityManager em, BigInteger nit, BigInteger secEmpresa) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT COUNT(t) FROM Terceros t "
                    + "WHERE t.nit = :nit AND t.empresa.secuencia = :secEmpresa");
            query.setParameter("nit", nit);
            query.setParameter("secEmpresa", secEmpresa);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            Long resultado = (Long) query.getSingleResult();
            return resultado > 0;
        } catch (Exception e) {
            log.error("PersistenciaTerceros.verificarTerceroParaEmpresaEmpleado():  ", e);
            return false;
        }
    }

    @Override
    public List<Terceros> lovTerceros(EntityManager em, BigInteger secEmpresa) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT t FROM Terceros t WHERE t.empresa.secuencia = :secEmpresa ORDER BY t.nombre");
            query.setParameter("secEmpresa", secEmpresa);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<Terceros> listaTerceros = query.getResultList();
            return listaTerceros;
        } catch (Exception e) {
            log.error("PersistenciaTerceros.lovTerceros():  ", e);
            return null;
        }
    }

    @Override
    public List<Terceros> tercerosEmbargos(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT t.nit, t.nombre, e.nombre FROM Terceros t, Empresas e Where e.secuencia=t.empresa ORDER BY t.nombre");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<Terceros> listaTerceros = query.getResultList();
            return listaTerceros;
        } catch (Exception e) {
            log.error("PersistenciaTerceros.tercerosEmbargos():  ", e);
            return null;
        }
    }

    @Override
    public String buscarCodigoSSPorSecuenciaTercero(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            String retorno = null;
            Query query = em.createQuery("SELECT t FROM Terceros t WHERE t.secuencia=:secuencia");
            query.setParameter("secuencia", secuencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            Terceros tercero = (Terceros) query.getSingleResult();
            if (tercero != null) {
                retorno = tercero.getCodigoss();
            }
            return retorno;
        } catch (Exception e) {
            log.error("Error buscarCodigoSSPorSecuenciaTercero PersistenciaTerceros :  ", e);
            return null;
        }
    }

    @Override
    public String buscarCodigoSPPorSecuenciaTercero(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            String retorno = null;
            Query query = em.createQuery("SELECT t FROM Terceros t WHERE t.secuencia=:secuencia");
            query.setParameter("secuencia", secuencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            Terceros tercero = (Terceros) query.getSingleResult();
            if (tercero != null) {
                retorno = tercero.getCodigosp();
            }
            return retorno;
        } catch (Exception e) {
            log.error("Error buscarCodigoSPPorSecuenciaTercero PersistenciaTerceros :  ", e);
            return null;
        }
    }

    @Override
    public String buscarCodigoSCPorSecuenciaTercero(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            String retorno = null;
            Query query = em.createQuery("SELECT t FROM Terceros t WHERE t.secuencia=:secuencia");
            query.setParameter("secuencia", secuencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            Terceros tercero = (Terceros) query.getSingleResult();
            if (tercero != null) {
                retorno = tercero.getCodigosc();
            }
            return retorno;
        } catch (Exception e) {
            log.error("Error buscarCodigoSCPorSecuenciaTercero PersistenciaTerceros :  ", e);
            return null;
        }
    }

    @Override
    public Terceros buscarTerceroPorCodigo(EntityManager em, Long codigo) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT t FROM Terceros t WHERE t.codigo = :codigo");
            query.setParameter("codigo", codigo);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            Terceros tercero = (Terceros) query.getSingleResult();
            return tercero;
        } catch (Exception e) {
            log.error("Error buscarTerceroPorCodigo PersistenciaTerceros :  ", e);
            return null;
        }
    }
}
