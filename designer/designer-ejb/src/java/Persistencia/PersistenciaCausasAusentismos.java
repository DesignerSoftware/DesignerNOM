/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.Causasausentismos;
import InterfacePersistencia.PersistenciaCausasAusentismosInterface;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
/**
 * Clase Stateless. <br>
 * Clase encargada de realizar operaciones sobre la tabla 'CausasAusentismos' de la base de datos
 * @author Betelgeuse
 */
@Stateless
public class PersistenciaCausasAusentismos implements PersistenciaCausasAusentismosInterface {

   private static Logger log = Logger.getLogger(PersistenciaCausasAusentismos.class);
    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos
     */
    /*@PersistenceContext(unitName = "DesignerRHN-ejbPU")
    private EntityManager em;*/

    @Override
    public void crear(EntityManager em,Causasausentismos causasAusentismos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(causasAusentismos);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaCausasAusentismos.crear:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em,Causasausentismos causasAusentismos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(causasAusentismos);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaCausasAusentismos.editar:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em,Causasausentismos causasAusentismos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(causasAusentismos));
            tx.commit();

        } catch (Exception e) {
            try {
                if (tx.isActive()) {
                    tx.rollback();
                }
            } catch (Exception ex) {
                log.error("Error PersistenciaCausasAusentismos.borrar:  ", e);
            }
        }
    }

    @Override
    public List<Causasausentismos> buscarCausasAusentismos(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT ca FROM Causasausentismos ca ORDER BY ca.codigo");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<Causasausentismos> todasCausasAusentismos = query.getResultList();
            return todasCausasAusentismos;
        } catch (Exception e) {
            log.error("Error: (todasCausas) ", e);
            return null;
        }
    }

}
