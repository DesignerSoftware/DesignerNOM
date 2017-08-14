/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import InterfacePersistencia.PersistenciaTiposExamenesInterface;
import Entidades.TiposExamenes;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la tabla 'TiposExamenes' de la
 * base de datos.
 *
 * @author John Pineda
 */
@Stateless
public class PersitenciaTiposExamenes implements PersistenciaTiposExamenesInterface {

   private static Logger log = Logger.getLogger(PersitenciaTiposExamenes.class);

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
    /*    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
     private EntityManager em;
     */
    @Override
    public void crear(EntityManager em, TiposExamenes tiposExamenes) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(tiposExamenes);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersitenciaTiposExamenes.crear: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, TiposExamenes tiposExamenes) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(tiposExamenes);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersitenciaTiposExamenes.editar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, TiposExamenes tiposExamenes) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(tiposExamenes));
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersitenciaTiposExamenes.borrar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public TiposExamenes buscarTipoExamen(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            return em.find(TiposExamenes.class, secuencia);
        } catch (Exception e) {
            log.error("Persistencia.PersitenciaTiposExamenes.buscarTipoExamen()" + e.getMessage());
            return null;
        }
    }

    @Override
    public List<TiposExamenes> buscarTiposExamenes(EntityManager em) {
        try {

            em.clear();
            Query query = em.createQuery("SELECT te FROM TiposExamenes te ORDER BY te.codigo ASC ");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<TiposExamenes> listMotivosDemandas = query.getResultList();
            return listMotivosDemandas;
        } catch (Exception e) {
            log.error("Persistencia.PersitenciaTiposExamenes.buscarTiposExamenes()" + e.getMessage());
            return null;
        }
    }

    @Override
    public BigInteger contadorTiposExamenesCargos(EntityManager em, BigInteger secuencia) {
        BigInteger retorno = new BigInteger("-1");
        try {
            em.clear();
            String sqlQuery = "SELECT COUNT(*)FROM  tiposexamenescargos tec , tiposexamenes te WHERE tec.tipoexamen=te.secuencia AND te.secuencia = ?";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            retorno = (BigInteger) new BigInteger(query.getSingleResult().toString());
            log.warn("Contador contadorTiposExamenesCargos persistencia " + retorno);
            return retorno;
        } catch (Exception e) {
            log.error("Error PersistenciaTiposExamenes contadorTiposExamenesCargos. " + e.getMessage());
            return retorno;
        }
    }

    @Override
    public BigInteger contadorVigenciasExamenesMedicos(EntityManager em, BigInteger secuencia) {
        BigInteger retorno = new BigInteger("-1");
        try {
            em.clear();
            String sqlQuery = "SELECT COUNT(*)FROM  vigenciasexamenesmedicos vem , tiposexamenes te WHERE vem.tipoexamen=te.secuencia  AND te.secuencia = ?";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            retorno = (BigInteger) new BigInteger(query.getSingleResult().toString());
            return retorno;
        } catch (Exception e) {
            log.error("Error PersistenciaTiposExamenes   contadorVigenciasExamenesMedicos. " + e.getMessage());
            return retorno;
        }
    }
}
