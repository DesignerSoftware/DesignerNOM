/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.PryRoles;
import InterfacePersistencia.PersistenciaPryRolesInterface;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
/**
 * Clase Stateless. <br> 
 * Clase encargada de realizar operaciones sobre la tabla 'PryRoles'
 * de la base de datos.
 * @author betelgeuse
 */
@Stateless
public class PersistenciaPryRoles implements PersistenciaPryRolesInterface {
    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
//    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
//    private EntityManager em;

    @Override
    public List<PryRoles> pryroles(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT p FROM PryRoles p ORDER BY p.descripcion ");
            List<PryRoles> pryroles = query.getResultList();
            return pryroles;
        } catch (Exception e) {
            return null;
        }
    }
}
