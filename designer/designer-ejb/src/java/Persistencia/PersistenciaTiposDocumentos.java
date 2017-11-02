/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.TiposDocumentos;
import InterfacePersistencia.PersistenciaTiposDocumentosInterface;
import java.math.BigInteger;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import org.eclipse.persistence.exceptions.DatabaseException;

/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la tabla 'MotivosContratos' de
 * la base de datos.
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaTiposDocumentos implements PersistenciaTiposDocumentosInterface {

    private static Logger log = Logger.getLogger(PersistenciaTiposDocumentos.class);

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
    /*    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
    private EntityManager em;
     */
    @Override
    public String crear(EntityManager em, TiposDocumentos tiposDocumentos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(tiposDocumentos);
            tx.commit();
            return "EXITO";
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            if (e instanceof PersistenceException || e instanceof SQLIntegrityConstraintViolationException || e instanceof DatabaseException) {
                log.error("Error PersistenciaTiposDocumentos.crear:  ", e);
                return e.toString();
            } else {
                return "Ha ocurrido un error al crear el Tipo Documento";
            }
        }
    }

    @Override
    public String editar(EntityManager em, TiposDocumentos tiposDocumentos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(tiposDocumentos);
            tx.commit();
            return "EXITO";
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            if (e instanceof PersistenceException || e instanceof SQLIntegrityConstraintViolationException || e instanceof DatabaseException) {
                log.error("Error PersistenciaTiposDocumentos.editar:  ", e);
                return e.toString();
            } else {
                return "Ha ocurrido un error al editar el Tipo Documento";
            }
        }
    }

    @Override
    public String borrar(EntityManager em, TiposDocumentos tiposDocumentos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(tiposDocumentos));
            tx.commit();
            return "EXITO";
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            if (e instanceof PersistenceException || e instanceof SQLIntegrityConstraintViolationException || e instanceof DatabaseException) {
                log.error("Error PersistenciaTiposDocumentos.borrar:  ", e);
                return e.toString();
            } else {
                return "Ha ocurrido un error al borrar el Tipo Documento";
            }
        }
    }

    @Override
    public List<TiposDocumentos> consultarTiposDocumentos(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT td FROM TiposDocumentos td ORDER BY td.nombrecorto");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<TiposDocumentos> listaTiposDocumentos = query.getResultList();
            return listaTiposDocumentos;
        } catch (Exception e) {
            log.error("Error PersistenciaTiposDocumentos.ciudades  ", e);
            return null;
        }
    }

    @Override
    public TiposDocumentos consultarTipoDocumento(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT tp FROM TiposDocumentos tp WHERE tp.secuencia = :secuencia");
            query.setParameter("secuencia", secuencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            TiposDocumentos tiposDescansos = (TiposDocumentos) query.getSingleResult();
            return tiposDescansos;
        } catch (Exception e) {
            log.error("Error buscarTiposDocumentosSecuencia PersistenciaTiposDocumentos ", e);
            return null;
        }
    }

    @Override
    public BigInteger contarCodeudoresTipoDocumento(EntityManager em, BigInteger secuencia) {
        BigInteger retorno = new BigInteger("-1");
        try {
            em.clear();
            String sqlQuery = "SELECT COUNT(*)FROM codeudores WHERE tipodocumento=?";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            retorno = new BigInteger(query.getSingleResult().toString());
            return retorno;
        } catch (Exception e) {
            log.error("Error PersistenciaTiposDocumentos contarCodeudoresTipoDocumento.  ", e);
            return retorno;
        }
    }

    @Override
    public BigInteger contarPersonasTipoDocumento(EntityManager em, BigInteger secuencia) {
        BigInteger retorno = new BigInteger("-1");
        try {
            em.clear();
            String sqlQuery = "SELECT COUNT(*)FROM personas WHERE tipodocumento=?";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            retorno = new BigInteger(query.getSingleResult().toString());
            return retorno;
        } catch (Exception e) {
            log.error("Error PersistenciaTiposDocumentos contarCodeudoresTipoDocumento.  ", e);
            return retorno;
        }
    }
}
