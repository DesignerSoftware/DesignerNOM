/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.Profesiones;
import InterfacePersistencia.PersistenciaProfesionesInterface;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
/**
 * Clase Stateless. <br> 
 * Clase encargada de realizar operaciones sobre la tabla 'Profesiones'
 * de la base de datos.
 * @author betelgeuse
 */
@Stateless
public class PersistenciaProfesiones implements PersistenciaProfesionesInterface{
    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
//    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
//    private EntityManager em;

    @Override
        public List<Profesiones> profesiones(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT p FROM Profesiones p ORDER BY p.descripcion");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<Profesiones> profesiones = query.getResultList();
            return profesiones;
        } catch (Exception e) {
            return null;
        }
    }
}
