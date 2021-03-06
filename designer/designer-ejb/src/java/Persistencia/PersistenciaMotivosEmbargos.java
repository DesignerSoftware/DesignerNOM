/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.MotivosEmbargos;
import InterfacePersistencia.PersistenciaMotivosEmbargosInterface;
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
 * Clase encargada de realizar operaciones sobre la tabla 'MotivosEmbargos' de
 * la base de datos.
 *
 * @author John Pineda
 */
@Stateless
public class PersistenciaMotivosEmbargos implements PersistenciaMotivosEmbargosInterface {

    private static Logger log = Logger.getLogger(PersistenciaMotivosEmbargos.class);

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos
     */
//    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
//    private EntityManager em;
    @Override
    public String crear(EntityManager em, MotivosEmbargos motivosEmbargos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(motivosEmbargos);
            tx.commit();
            return "EXITO";
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            if (e instanceof PersistenceException || e instanceof SQLIntegrityConstraintViolationException || e instanceof DatabaseException) {
                log.error("Error PersistenciaMotivosEmbargos.crear:  ", e);
                return e.toString();
            } else {
                return "Ha ocurrido un error al Crear el Motivo Embargo";
            }
        }
    }

    @Override
    public String editar(EntityManager em, MotivosEmbargos motivosEmbargos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(motivosEmbargos);
            tx.commit();
            return "EXITO";
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            if (e instanceof PersistenceException || e instanceof SQLIntegrityConstraintViolationException || e instanceof DatabaseException) {
                log.error("Error PersistenciaMotivosEmbargos.editar:  ", e);
                return e.toString();
            } else {
                return "Ha ocurrido un error al Editar el Motivo Embargo";
            }
        }
    }

    @Override
    public String borrar(EntityManager em, MotivosEmbargos motivosEmbargos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(motivosEmbargos));
            tx.commit();
            return "EXITO";
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            if (e instanceof PersistenceException || e instanceof SQLIntegrityConstraintViolationException || e instanceof DatabaseException) {
                log.error("Error PersistenciaMotivosEmbargos.borrar:  ", e);
                return e.toString();
            } else {
                return "Ha ocurrido un error al Borrar el Motivo Embargo";
            }
        }
    }

    @Override
    public MotivosEmbargos buscarMotivoEmbargo(EntityManager em, BigInteger secuenciaME) {
        try {
            em.clear();
            return em.find(MotivosEmbargos.class, secuenciaME);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<MotivosEmbargos> buscarMotivosEmbargos(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT m FROM MotivosEmbargos m ORDER BY m.codigo ASC");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<MotivosEmbargos> listaMotivosEmbargos = query.getResultList();
            return listaMotivosEmbargos;
        } catch (Exception e) {
            log.error("PersistenciaMotivosEmbargos.buscarMotivosEmbargos():  ", e);
            return null;
        }
    }

    @Override
    public BigInteger contadorEersPrestamos(EntityManager em, BigInteger secuencia) {
        BigInteger retorno;
        try {
            em.clear();
            String sqlQuery = " SELECT COUNT(*)FROM eersprestamos eer WHERE eer.motivoembargo = ? ";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            retorno = new BigInteger(query.getSingleResult().toString());
            return retorno;
        } catch (Exception e) {
            log.error("ERROR PERSISTENCIAMOTIVOSEMBARGOS CONTADOREERSPRESTAMOS  ERROR =  ", e);
            retorno = new BigInteger("-1");
            return retorno;
        }
    }

    @Override
    public BigInteger contadorEmbargos(EntityManager em, BigInteger secuencia) {
        BigInteger retorno;
        try {
            em.clear();
            String sqlQuery = " SELECT COUNT(*)FROM  embargos emb WHERE emb.motivo = ? ";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            retorno = new BigInteger(query.getSingleResult().toString());
            return retorno;
        } catch (Exception e) {
            log.error("ERROR PERSISTENCIAMOTIVOSEMBARGOS CONTADOREMBARGOS  ERROR =  ", e);
            retorno = new BigInteger("-1");
            return retorno;
        }
    }
}
