/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import InterfacePersistencia.PersistenciaTiposExamenesInterface;
import Entidades.TiposExamenes;
import java.math.BigInteger;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import org.eclipse.persistence.exceptions.DatabaseException;

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
    public String crear(EntityManager em, TiposExamenes tiposExamenes) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(tiposExamenes);
            tx.commit();
            return "EXITO";
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            if (e instanceof PersistenceException || e instanceof SQLIntegrityConstraintViolationException || e instanceof DatabaseException) {
                log.error("Error PersitenciaTiposExamenes.crear:  ", e);
                return e.toString();
            } else {
                return "Ha ocurrido un error al crear el Tipo Exámen";
            }
        }
    }

    @Override
    public String editar(EntityManager em, TiposExamenes tiposExamenes) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(tiposExamenes);
            tx.commit();
            return "EXITO";
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            if (e instanceof PersistenceException || e instanceof SQLIntegrityConstraintViolationException || e instanceof DatabaseException) {
                log.error("Error PersitenciaTiposExamenes.editar:  ", e);
                return e.toString();
            } else {
                return "Ha ocurrido un error al editar el Tipo Exámen";
            }
        }
    }

    @Override
    public String borrar(EntityManager em, TiposExamenes tiposExamenes) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(tiposExamenes));
            tx.commit();
            return "EXITO";
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            if (e instanceof PersistenceException || e instanceof SQLIntegrityConstraintViolationException || e instanceof DatabaseException) {
                log.error("Error PersitenciaTiposExamenes.borrar:  ", e);
                return e.toString();
            } else {
                return "Ha ocurrido un error al borrar el Tipo Exámen";
            }
        }
    }

    @Override
    public TiposExamenes buscarTipoExamen(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            return em.find(TiposExamenes.class, secuencia);
        } catch (Exception e) {
            log.error("Persistencia.PersitenciaTiposExamenes.buscarTipoExamen():  ", e);
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
            log.error("Persistencia.PersitenciaTiposExamenes.buscarTiposExamenes():  ", e);
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
            log.error("Error PersistenciaTiposExamenes contadorTiposExamenesCargos.  ", e);
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
            log.error("Error PersistenciaTiposExamenes   contadorVigenciasExamenesMedicos.  ", e);
            return retorno;
        }
    }
}
