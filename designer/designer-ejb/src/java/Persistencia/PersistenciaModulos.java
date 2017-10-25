/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.Modulos;
import InterfacePersistencia.PersistenciaModulosInterface;
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
 * Clase encargada de realizar operaciones sobre la tabla 'Modulos' de la base
 * de datos.
 *
 * @author -Felipphe- Felipe Triviño
 */
@Stateless
public class PersistenciaModulos implements PersistenciaModulosInterface {

    private static Logger log = Logger.getLogger(PersistenciaModulos.class);

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
//    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
//    private EntityManager em;
    @Override
    public String crear(EntityManager em, Modulos modulos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(modulos);
            tx.commit();
            return "EXITO";
        } catch (Exception e) {
            log.error("Error PersistenciaModulos.crear:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
            if (e instanceof PersistenceException || e instanceof SQLIntegrityConstraintViolationException || e instanceof DatabaseException) {
                log.error("Error PersistenciaModulos.crear:  ", e);
                return e.toString();
            } else {
                return "Ha ocurrido un error al Crear el módulo";
            }
        }
    }

    @Override
    public String editar(EntityManager em, Modulos modulos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(modulos);
            tx.commit();
            return "EXITO";
        } catch (Exception e) {
            log.error("Error PersistenciaModulos.editar:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
            if (e instanceof PersistenceException || e instanceof SQLIntegrityConstraintViolationException || e instanceof DatabaseException) {
                log.error("Error PersistenciaModulos.editar:  ", e);
                return e.toString();
            } else {
                return "Ha ocurrido un error al editar el módulo";
            }
        }
    }

    @Override
    public String borrar(EntityManager em, Modulos modulos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(modulos));
            tx.commit();
            return "EXITO";
        } catch (Exception e) {
            log.error("Error PersistenciaModulos.borrar:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
            if (e instanceof PersistenceException || e instanceof SQLIntegrityConstraintViolationException || e instanceof DatabaseException) {
                log.error("Error PersistenciaModulos.borrar:  ", e);
                return e.toString();
            } else {
                return "Ha ocurrido un error al Borrar el módulo";
            }
        }
    }

    @Override
    public Modulos buscarModulos(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            return em.find(Modulos.class, secuencia);
        } catch (Exception e) {
            log.error("PersistenciaModulos.buscarModulos():  ", e);
            return null;
        }
    }

    @Override
    public List<Modulos> buscarModulos(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT m FROM Modulos m");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<Modulos> lista = query.getResultList();
            return lista;
        } catch (Exception e) {
            log.error("Error buscarModulos PersistenciaModulos :  ", e);
            return null;
        }
    }

    @Override
    public Modulos buscarModulosPorSecuencia(EntityManager em, BigInteger secModulo) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT m FROM Modulos m where m.secuencia = :secModulo");
            query.setParameter("secModulo", secModulo);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            Modulos modu = (Modulos) query.getSingleResult();
            return modu;
        } catch (Exception e) {
            log.error("Error buscarModulos PersistenciaModulos :  ", e);
            return null;
        }
    }

    @Override
    public List<Modulos> listaModulos(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT m FROM Modulos m");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<Modulos> listaModulos = (List<Modulos>) query.getResultList();
            return listaModulos;
        } catch (Exception e) {
            log.error("Error PersistenciaModulos listaModulos :  ", e);
            return null;
        }
    }

}
