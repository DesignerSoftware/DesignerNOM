/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.Periodicidades;
import InterfacePersistencia.PersistenciaPeriodicidadesInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 * Clase Stateless. <br>
 * Clase encargada de realizar operaciones sobre la tabla 'Periodicidades' de la
 * base de datos.
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaPeriodicidades implements PersistenciaPeriodicidadesInterface {

   private static Logger log = Logger.getLogger(PersistenciaPeriodicidades.class);

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
//    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
//    private EntityManager em;
    @Override
    public void crear(EntityManager em, Periodicidades periodicidades) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(periodicidades);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaPeriodicidades.crear:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, Periodicidades periodicidades) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(periodicidades);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaPeriodicidades.editar:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, Periodicidades periodicidades) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(periodicidades));
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaPeriodicidades.borrar:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public boolean verificarCodigoPeriodicidad(EntityManager em, BigInteger codigoPeriodicidad) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT COUNT(p) FROM Periodicidades p WHERE p.codigo = :codigo");
            query.setParameter("codigo", codigoPeriodicidad);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            Long resultado = (Long) query.getSingleResult();
            return resultado > 0;
        } catch (Exception e) {
            log.error("Exepcion:  ", e);
            return false;
        }
    }

    @Override
    public Periodicidades consultarPeriodicidad(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            return em.find(Periodicidades.class, secuencia);
        } catch (Exception e) {
            log.error("Error en la persistencia vigencias formas pagos ERROR :  ", e);
            return null;
        }
    }

    @Override
    public List<Periodicidades> consultarPeriodicidades(EntityManager em) {
        try{
        em.clear();
        Query query = em.createQuery("SELECT m FROM Periodicidades m");
        query.setHint("javax.persistence.cache.storeMode", "REFRESH");
        List<Periodicidades> lista = query.getResultList();
        return lista;
        }catch(Exception e){
            log.error("PersistenciaPeriodicidades.consultarPeriodicidades():  ", e);
            return null;
        }
    }

    @Override
    public BigInteger contarCPCompromisosPeriodicidad(EntityManager em, BigInteger secuencia) {
        BigInteger retorno = new BigInteger("-1");
        try {
            em.clear();
            String sqlQuery = "SELECT COUNT(*)FROM cpcompromisos WHERE periodicidad = ?";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            retorno = new BigInteger(query.getSingleResult().toString());
            return retorno;
        } catch (Exception e) {
            log.error("Error PERSISTENCIAPERIODICIDADES  contarCPCompromisosPeriodicidad.  ", e);
            return retorno;
        }
    }

    @Override
    public BigInteger contarDetallesPeriodicidadesPeriodicidad(EntityManager em, BigInteger secuencia) {
        BigInteger retorno = new BigInteger("-1");
        try {
            em.clear();
            String sqlQuery = "SELECT COUNT(*)FROM detallesperiodicidades WHERE periodicidad = ?";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            retorno = new BigInteger(query.getSingleResult().toString());
            return retorno;
        } catch (Exception e) {
            log.error("Error PERSISTENCIAPERIODICIDADES  contarDetallesPeriodicidadesPeriodicidad  ", e);
            return retorno;
        }
    }

    @Override
    public BigInteger contarEersPrestamosDtosPeriodicidad(EntityManager em, BigInteger secuencia) {
        BigInteger retorno = new BigInteger("-1");
        try {
            em.clear();
            String sqlQuery = "SELECT COUNT(*)FROM eersprestamosdtos WHERE periodicidad = ?";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            retorno = new BigInteger(query.getSingleResult().toString());
            return retorno;
        } catch (Exception e) {
            log.error("Error PERSISTENCIAPERIODICIDADES  contarEersPrestamosDtosPeriodicidad  ", e);
            return retorno;
        }
    }

    @Override
    public BigInteger contarEmpresasPeriodicidad(EntityManager em, BigInteger secuencia) {
        BigInteger retorno = new BigInteger("-1");
        try {
            em.clear();
            String sqlQuery = "SELECT COUNT(*)FROM empresas WHERE minimaperiodicidad = ?";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            retorno = new BigInteger(query.getSingleResult().toString());
            return retorno;
        } catch (Exception e) {
            log.error("Error PERSISTENCIAPERIODICIDADES  contarEmpresasPeriodicidad  ", e);
            return retorno;
        }
    }

    @Override
    public BigInteger contarFormulasAseguradasPeriodicidad(EntityManager em, BigInteger secuencia) {
        BigInteger retorno = new BigInteger("-1");
        try {
            em.clear();
            String sqlQuery = "SELECT COUNT(*)FROM formulasaseguradas WHERE periodicidad = ?";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            retorno = new BigInteger(query.getSingleResult().toString());
            return retorno;
        } catch (Exception e) {
            log.error("Error PERSISTENCIAPERIODICIDADES  contarFormulasAseguradasPeriodicidad  ", e);
            return retorno;
        }
    }

    @Override
    public BigInteger contarFormulasContratosPeriodicidad(EntityManager em, BigInteger secuencia) {
        BigInteger retorno = new BigInteger("-1");
        try {
            em.clear();
            String sqlQuery = "SELECT COUNT(*)FROM formulascontratos WHERE periodicidad = ?";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            retorno = new BigInteger(query.getSingleResult().toString());
            return retorno;
        } catch (Exception e) {
            log.error("Error PERSISTENCIAPERIODICIDADES  contarFormulasContratosPeriodicidad  ", e);
            return retorno;
        }
    }

    @Override
    public BigInteger contarGruposProvisionesPeriodicidad(EntityManager em, BigInteger secuencia) {
        BigInteger retorno = new BigInteger("-1");
        try {
            em.clear();
            String sqlQuery = "SELECT COUNT(*)FROM gruposprovisiones WHERE periodicidadcorte = ?";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            retorno = new BigInteger(query.getSingleResult().toString());
            return retorno;
        } catch (Exception e) {
            log.error("Error PERSISTENCIAPERIODICIDADES  contarGruposProvisionesPeriodicidad  ", e);
            return retorno;
        }
    }

    @Override
    public BigInteger contarNovedadesPeriodicidad(EntityManager em, BigInteger secuencia) {
        BigInteger retorno = new BigInteger("-1");
        try {
            em.clear();
            String sqlQuery = "SELECT COUNT(*)FROM novedades WHERE periodicidad = ?";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            retorno = new BigInteger(query.getSingleResult().toString());
            return retorno;
        } catch (Exception e) {
            log.error("Error PERSISTENCIAPERIODICIDADES  contarNovedadesPeriodicidad  ", e);
            return retorno;
        }
    }

    @Override
    public BigInteger contarParametrosCambiosMasivosPeriodicidad(EntityManager em, BigInteger secuencia) {
        BigInteger retorno = new BigInteger("-1");
        try {
            em.clear();
            String sqlQuery = "SELECT COUNT(*)FROM parametroscambiosmasivos WHERE noveperiodicidad = ?";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            retorno = new BigInteger(query.getSingleResult().toString());
            return retorno;
        } catch (Exception e) {
            log.error("Error PERSISTENCIAPERIODICIDADES  contarParametrosCambiosMasivosPeriodicidad  ", e);
            return retorno;
        }
    }

    @Override
    public BigInteger contarVigenciasFormasPagosPeriodicidad(EntityManager em, BigInteger secuencia) {
        BigInteger retorno = new BigInteger("-1");
        try {
            em.clear();
            String sqlQuery = "SELECT COUNT(*)FROM vigenciasformaspagos WHERE formapago = ?";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            retorno = new BigInteger(query.getSingleResult().toString());
            return retorno;
        } catch (Exception e) {
            log.error("Error PERSISTENCIAPERIODICIDADES  contarVigenciasFormasPagosPeriodicidad  ", e);
            return retorno;
        }
    }

}
