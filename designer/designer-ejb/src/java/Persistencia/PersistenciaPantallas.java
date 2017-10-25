/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.Pantallas;
import InterfacePersistencia.PersistenciaPantallasInterface;
import java.math.BigInteger;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.*;
import javax.persistence.Query;
import org.eclipse.persistence.exceptions.DatabaseException;

/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la tabla 'Pantallas' de la base
 * de datos.
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaPantallas implements PersistenciaPantallasInterface {

   private static Logger log = Logger.getLogger(PersistenciaPantallas.class);

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
//    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
//    private EntityManager em;
    @Override
    public Pantallas buscarPantalla(EntityManager em, BigInteger secuenciaTab) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT p from Pantallas p where p.tabla.secuencia = :secuenciaTab");
            query.setParameter("secuenciaTab", secuenciaTab);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            Pantallas pantalla = (Pantallas) query.getSingleResult();
            return pantalla;
        } catch (Exception e) {
            log.error("PersistenciaPantallas.buscarPantalla():  ", e);
            return null;
        }
    }

    @Override
    public List<Pantallas> buscarPantallas(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT pan FROM Pantallas pan ORDER BY pan.codigo");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<Pantallas> todosPantallas = query.getResultList();
            return todosPantallas;
        } catch (Exception e) {
            log.error("Error: PersistenciaPantallas consultarPantallas ERROR  ", e);
            return null;
        }
    }

    @Override
    public String buscarIntContable(EntityManager em, BigInteger secEmpresa) {
        try {
            em.clear();
            String sql = "SELECT NOMBRE FROM PANTALLAS WHERE CODIGO = 902 AND EMPRESA = ?";
            Query query = em.createNativeQuery(sql);
            query.setParameter(1, secEmpresa);
            String intcontables = (String) query.getSingleResult();
            return intcontables;
        } catch (Exception e) {
            log.error("Error: PersistenciaPantallas buscarIntContable ERROR  ", e);
            return " ";
        }
    }
    
    @Override
    public String buscarPantallaPorCodigoEmpresa(EntityManager em, BigInteger secEmpresa, Short codigo) {
        String sql;
        String intcontables = " ";
        Query query;
        try {
            em.clear();
            sql = "SELECT NOMBRE FROM PANTALLAS WHERE CODIGO = ? AND EMPRESA = ? ";
            query = em.createNativeQuery(sql);
            query.setParameter(1, codigo);
            query.setParameter(2, secEmpresa);
            intcontables = (String) query.getSingleResult();
            return intcontables;
        } catch (NoResultException nre)  {
            log.error("Error: PersistenciaPantallas buscarPantallaPorCodigoEmpresa ERROR " + nre.getMessage());
            return " ";
        }
    }

    @Override
    public String buscarPantallaPorCodigo(EntityManager em, Short codigo) {
        String sql;
        String intcontables = " ";
        Query query;
        try {
            em.clear();
            sql = "SELECT NOMBRE FROM PANTALLAS WHERE CODIGO = ?";
            query = em.createNativeQuery(sql);
            query.setParameter(1, codigo);
            intcontables = (String) query.getSingleResult();
        } catch (NonUniqueResultException nure) {
            log.error("Error: PersistenciaPantallas buscarPantallaPorCodigo ERROR " + nure.getMessage());
            try {
                em.clear();
                sql = "SELECT NOMBRE FROM PANTALLAS WHERE CODIGO = ? AND ROWNUM <= 1 ";
                query = em.createNativeQuery(sql);
                query.setParameter(1, codigo);
                intcontables = (String) query.getSingleResult();
            } catch (NoResultException nre) {
                intcontables = " ";
            }
        } finally {
            return intcontables;
        }
    }

    @Override
    public String crear(EntityManager em, Pantallas pantalla) {
           em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(pantalla);
            tx.commit();
            return "EXITO";
        } catch (Exception e) {
            log.error("Error PersistenciaPantallas.crear:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
            if (e instanceof PersistenceException || e instanceof SQLIntegrityConstraintViolationException || e instanceof DatabaseException) {
                log.error("Error PersistenciaPantallas.crear:  ", e);
                return e.toString();
            } else {
                return "Ha ocurrido un error al Crear la pantalla";
            }
        }
    }

    @Override
    public String editar(EntityManager em, Pantallas pantalla) {
            em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(pantalla);
            tx.commit();
            return "EXITO";
        } catch (Exception e) {
            log.error("Error PersistenciaPantallas.editar:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
            if (e instanceof PersistenceException || e instanceof SQLIntegrityConstraintViolationException || e instanceof DatabaseException) {
                log.error("Error PersistenciaPantallas.editar:  ", e);
                return e.toString();
            } else {
                return "Ha ocurrido un error al editar la pantalla";
            }
        }
    }

    @Override
    public String borrar(EntityManager em, Pantallas pantalla) {
            em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(pantalla));
            tx.commit();
            return "EXITO";
        } catch (Exception e) {
            log.error("Error PersistenciaPantallas.borrar:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
            if (e instanceof PersistenceException || e instanceof SQLIntegrityConstraintViolationException || e instanceof DatabaseException) {
                log.error("Error PersistenciaPantallas.borrar:  ", e);
                return e.toString();
            } else {
                return "Ha ocurrido un error al borrar la pantalla";
            }
        }
    }

}
