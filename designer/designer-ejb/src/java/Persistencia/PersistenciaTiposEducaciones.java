/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.TiposEducaciones;
import InterfacePersistencia.PersistenciaTiposEducacionesInterface;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la tabla 'TiposEducaciones' de
 * la base de datos.
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaTiposEducaciones implements PersistenciaTiposEducacionesInterface {

   private static Logger log = Logger.getLogger(PersistenciaTiposEducaciones.class);

    @Override
    public List<TiposEducaciones> tiposEducaciones(EntityManager em) {
        try {
            em.clear();
            String sql = "SELECT * FROM TiposEducaciones  ORDER BY codigo";
            Query query = em.createNativeQuery(sql, TiposEducaciones.class);
            List<TiposEducaciones> tiposEducaciones = query.getResultList();
            return tiposEducaciones;
        } catch (Exception e) {
            log.error("error en PersistenciaTiposEducaciones.tiposeducaciones  ", e);
            return null;
        }
    }

    @Override
    public void crear(EntityManager em, TiposEducaciones tipoEducacion) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(tipoEducacion);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaTiposEducaciones.crear:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, TiposEducaciones tipoEducacion) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(tipoEducacion));
            tx.commit();

        } catch (Exception e) {
            log.error("Error PersistenciaTiposEducaciones.borrar:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, TiposEducaciones tipoEducacion) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(tipoEducacion);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaTiposEducaciones.editar:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }
}
