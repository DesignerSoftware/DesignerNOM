/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.MotivosDefinitivas;
import InterfacePersistencia.PersistenciaMotivosDefinitivasInterface;
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
 * Clase encargada de realizar operaciones sobre la tabla 'MotivosDefinitivas'
 * de la base de datos.
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaMotivosDefinitivas implements PersistenciaMotivosDefinitivasInterface {

    private static Logger log = Logger.getLogger(PersistenciaMotivosDefinitivas.class);

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
//    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
//    private EntityManager em;
    @Override
    public String crear(EntityManager em, MotivosDefinitivas motivosDefinitivas) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(motivosDefinitivas);
            tx.commit();
            return "EXITO";
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            if (e instanceof PersistenceException || e instanceof SQLIntegrityConstraintViolationException || e instanceof DatabaseException) {
                log.error("Error PersistenciaMotivosDefinitivas.crear:  ", e);
                return e.toString();
            } else {
                return "Ha ocurrido un error al Crear el Motivo Definitiva";
            }
        }
    }

    @Override
    public String editar(EntityManager em, MotivosDefinitivas motivosDefinitivas) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(motivosDefinitivas);
            tx.commit();
            return "EXITO";
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            if (e instanceof PersistenceException || e instanceof SQLIntegrityConstraintViolationException || e instanceof DatabaseException) {
                log.error("Error PersistenciaMotivosDefinitivas.editar:  ", e);
                return e.toString();
            } else {
                return "Ha ocurrido un error al Editar el Motivo Definitiva";
            }
        }
    }

    @Override
    public String borrar(EntityManager em, MotivosDefinitivas motivosDefinitivas) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(motivosDefinitivas));
            tx.commit();
            return "EXITO";
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            if (e instanceof PersistenceException || e instanceof SQLIntegrityConstraintViolationException || e instanceof DatabaseException) {
                log.error("Error PersistenciaMotivosDefinitivas.borrar:  ", e);
                return e.toString();
            } else {
                return "Ha ocurrido un error al Borrar el Motivo Definitiva";
            }
        }
    }

    @Override
    public List<MotivosDefinitivas> buscarMotivosDefinitivas(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT md FROM MotivosDefinitivas md ORDER BY md.codigo ASC");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<MotivosDefinitivas> motivoD = query.getResultList();
            return motivoD;
        } catch (Exception e) {
            log.error("Error PersistenciaMotivosDefinitivfas.buscarMotivosDefinitivas ", e);
            return null;
        }
    }

    @Override
    public MotivosDefinitivas buscarMotivoDefinitiva(EntityManager em, BigInteger secuenciaME) {
        try {
            em.clear();
            return em.find(MotivosDefinitivas.class, secuenciaME);
        } catch (Exception e) {
            log.error("PersistenciaMotivosDefinitivas.buscarMotivoDefinitiva():  ", e);
            return null;
        }
    }

    @Override
    public BigInteger contadorNovedadesSistema(EntityManager em, BigInteger secuencia) {
        BigInteger retorno;
        try {
            em.clear();
            String sqlQuery = "SELECT COUNT(*)FROM novedadessistema WHERE motivodefinitiva = ? ";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            retorno = new BigInteger(query.getSingleResult().toString());
            return retorno;
        } catch (Exception e) {
            log.error("ERROR PERSISTENCIAMOTIVOSDEFINITIVAS CONTADORNOVEDADESSISTEMA  ERROR =  ", e);
            retorno = new BigInteger("-1");
            return retorno;
        }
    }

    @Override
    public BigInteger contadorParametrosCambiosMasivos(EntityManager em, BigInteger secuencia) {
        BigInteger retorno;
        try {
            String sqlQuery = "SELECT COUNT(*)FROM parametroscambiosmasivos WHERE retimotivodefinitiva =  ? ";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            retorno = new BigInteger(query.getSingleResult().toString());
            return retorno;
        } catch (Exception e) {
            log.error("ERROR PERSISTENCIAMOTIVOSDEFINITIVAS CONTADORPARAMETROSCAMBIOSMASIVOS  ERROR =  ", e);
            retorno = new BigInteger("-1");
            return retorno;
        }
    }
}
