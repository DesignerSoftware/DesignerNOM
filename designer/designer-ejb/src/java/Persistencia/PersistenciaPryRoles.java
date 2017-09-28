/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.PryRoles;
import InterfacePersistencia.PersistenciaPryRolesInterface;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
/**
 * Clase Stateless. <br> 
 * Clase encargada de realizar operaciones sobre la tabla 'PryRoles'
 * de la base de datos.
 * @author betelgeuse
 */
@Stateless
public class PersistenciaPryRoles implements PersistenciaPryRolesInterface {

   private static Logger log = Logger.getLogger(PersistenciaPryRoles.class);
    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
//    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
//    private EntityManager em;

    @Override
    public List<PryRoles> pryroles(EntityManager em) {
        try {
            em.clear();
            String sql ="SELECT * FROM PRY_ROLES ORDER BY CODIGO ASC";
            Query query = em.createNativeQuery(sql, PryRoles.class);
            List<PryRoles> pryroles = query.getResultList();
            return pryroles;
        } catch (Exception e) {
            log.error("PersistenciaPryRoles.pryroles():  ", e);
            return null;
        }
    }

    @Override
    public void crear(EntityManager em, PryRoles pryrol) {
       em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(pryrol);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaPryRoles.crear:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, PryRoles pryrol) {
      em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(pryrol);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaPryRoles.editar:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, PryRoles pryrol) {
         em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(pryrol));
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaPryRoles.borrar:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }
}
