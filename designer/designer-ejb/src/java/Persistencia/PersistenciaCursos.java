/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;


import Entidades.Cursos;
import InterfacePersistencia.PersistenciaCursosInterface;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Clase Stateless. <br>
 * Clase encargada de realizar operaciones sobre la tabla 'Cursos'
 * de la base de datos
 * @author betelgeuse
 */
@Stateless
public class PersistenciaCursos implements PersistenciaCursosInterface{
    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos
     */
    /*@PersistenceContext(unitName = "DesignerRHN-ejbPU")
    private EntityManager em;*/

    @Override
        public List<Cursos> cursos(EntityManager em) {
        try {em.clear();
            Query query = em.createQuery("SELECT c FROM Cursos c ORDER BY c.nombre");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<Cursos> cursos = query.getResultList();
            return cursos;
        } catch (Exception e) {
            return null;
        }
    }
}


