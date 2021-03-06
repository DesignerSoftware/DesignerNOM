/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.ObjetosDB;
import InterfacePersistencia.PersistenciaObjetosDBInterface;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
import org.apache.log4j.Logger;
import javax.persistence.Query;
import org.eclipse.persistence.exceptions.DatabaseException;
/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la tabla 'ObjetosDB'
 * de la base de datos.
 * @author betelgeuse
 */
@Stateless
public class PersistenciaObjetosDB implements PersistenciaObjetosDBInterface {

   private static Logger log = Logger.getLogger(PersistenciaObjetosDB.class);
    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
//    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
//    private EntityManager em;

    @Override
    public ObjetosDB obtenerObjetoTabla(EntityManager em, String nombreTabla) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT COUNT(obj) FROM ObjetosDB obj WHERE obj.nombre = :nombreTabla");
            query.setParameter("nombreTabla", nombreTabla);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            Long resultado = (Long) query.getSingleResult();
            if (resultado > 0) {
                Query query2 = em.createQuery("SELECT obj FROM ObjetosDB obj WHERE obj.nombre = :nombreTabla");
                query2.setParameter("nombreTabla", nombreTabla);
                query2.setHint("javax.persistence.cache.storeMode", "REFRESH");
                ObjetosDB objetosDB = (ObjetosDB) query2.getSingleResult();
                return objetosDB;
            } else {
                return null;
            }
        } catch (Exception e) {
            log.error("Excepcion en verificarRastroTabla  ", e);
            return null;
        }
    }
    
    @Override
    public List<ObjetosDB> consultarObjetoDB(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT ob FROM ObjetosDB ob ORDER BY ob.nombre ASC");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<ObjetosDB> todosObjetos = query.getResultList();
            return todosObjetos;
        } catch (Exception e) {
            log.error("Error: PersistenciaObjetosDB consultarObjetoDB ERROR  ", e);
            return null;
        }
        
        
    }

    @Override
    public String crear(EntityManager em, ObjetosDB objeto) {
       em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(objeto);
            tx.commit();
            return "EXITO";
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            if (e instanceof PersistenceException || e instanceof SQLIntegrityConstraintViolationException || e instanceof DatabaseException) {
                log.error("Error PersistenciaObjetosDB.crear:  ", e);
                return e.toString();
            } else {
                return "Ha ocurrido un error al Crear el ObjetoDB";
            }
        }
    }

    @Override
    public String editar(EntityManager em, ObjetosDB objeto) {
       em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(objeto);
            tx.commit();
            return "EXITO";
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            if (e instanceof PersistenceException || e instanceof SQLIntegrityConstraintViolationException || e instanceof DatabaseException) {
                log.error("Error PersistenciaObjetosDB.editar:  ", e);
                return e.toString();
            } else {
                return "Ha ocurrido un error al Editar el ObjetoDB";
            }
        }
    }

    @Override
    public String borrar(EntityManager em, ObjetosDB objeto) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(objeto));
            tx.commit();
            return "EXITO";
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            if (e instanceof PersistenceException || e instanceof SQLIntegrityConstraintViolationException || e instanceof DatabaseException) {
                log.error("Error PersistenciaObjetosDB.borrar:  ", e);
                return e.toString();
            } else {
                return "Ha ocurrido un error al borrar el ObjetoDB";
            }
        }
    }
    
}
