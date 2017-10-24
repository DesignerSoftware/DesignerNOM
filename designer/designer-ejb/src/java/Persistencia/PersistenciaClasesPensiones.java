/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.ClasesPensiones;
import InterfacePersistencia.PersistenciaClasesPensionesInterface;
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
 * Clase Stateless. <br>
 * Clase encargada de realizar operaciones sobre la tabla 'ClasesPensiones' de
 * la base de datos
 *
 * @author Andrés Pineda
 */
@Stateless
public class PersistenciaClasesPensiones implements PersistenciaClasesPensionesInterface {

    private static Logger log = Logger.getLogger(PersistenciaClasesPensiones.class);

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos
     */
    /* @PersistenceContext(unitName = "DesignerRHN-ejbPU")
     private EntityManager em;*/
    @Override
    public String crear(EntityManager em, ClasesPensiones clasesPensiones) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(clasesPensiones);
            tx.commit();
            return "EXITO";
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            if (e instanceof PersistenceException || e instanceof SQLIntegrityConstraintViolationException || e instanceof DatabaseException) {
                log.error("Error PersistenciaClasesPensiones.crear:  ", e);
                return e.toString();
            } else {
                return "Ha ocurrido un error al Crear la clase Pensión";
            }
        }
    }

    @Override
    public String editar(EntityManager em, ClasesPensiones clasesPensiones) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(clasesPensiones);
            tx.commit();
            return "EXITO";
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            if (e instanceof PersistenceException || e instanceof SQLIntegrityConstraintViolationException || e instanceof DatabaseException) {
                log.error("Error PersistenciaClasesPensiones.editar:  ", e);
                return e.toString();
            } else {
                return "Ha ocurrido un error al Editar la clase Pensión";
            }
        }
    }

    @Override
    public String borrar(EntityManager em, ClasesPensiones clasesPensiones) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(clasesPensiones));
            tx.commit();
            return "EXITO";
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            if (e instanceof PersistenceException || e instanceof SQLIntegrityConstraintViolationException || e instanceof DatabaseException) {
                log.error("Error PersistenciaClasesPensiones.borrar:  ", e);
                return e.toString();
            } else {
                return "Ha ocurrido un error al Borrar la clase Pensión";
            }
        }
    }

    @Override
    public List<ClasesPensiones> consultarClasesPensiones(EntityManager em) {
        try {
            em.clear();
            String sql = "SELECT *  FROM ClasesPensiones";
            //log.warn("PersistenciaClasesPensiones consultarClasesPensiones ");
            Query query = em.createNativeQuery(sql, ClasesPensiones.class);
            List<ClasesPensiones> clasesPensionesLista = query.getResultList();
            return clasesPensionesLista;
        } catch (Exception e) {
            log.error("Error consultarClasesPensiones PersistenciaClasesPensiones  ", e);
            return null;
        }
    }

    @Override
    public ClasesPensiones consultarClasePension(EntityManager em, BigInteger secuencia) {

        try {
            em.clear();
            String sql = "SELECT * FROM ClasesPensiones WHERE secuencia = ?";
            Query query = em.createNativeQuery(sql, ClasesPensiones.class);
            query.setParameter(1, secuencia);
            //query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            ClasesPensiones claseP = (ClasesPensiones) query.getSingleResult();
            return claseP;
        } catch (Exception e) {
            log.error("Error buscarClasePennsion PersistenciaClasesPensiones  ", e);
            ClasesPensiones claseP = null;
            return claseP;
        }
    }

    @Override
    public BigInteger contarRetiradosClasePension(EntityManager em, BigInteger secuencia) {
        BigInteger retorno = new BigInteger("-1");
        try {
            em.clear();
            String sqlQuery = "SELECT COUNT(*)FROM pensionados WHERE clase=?";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            retorno = new BigInteger(query.getSingleResult().toString());
            log.warn("Contador PersistenciaMotivosRetiros  contarRetiradosClasePension  " + retorno);
            return retorno;
        } catch (Exception e) {
            log.error("Error PersistenciaMotivosRetiros   contarRetiradosClasePension.  ", e);
            return retorno;
        }
    }
}
