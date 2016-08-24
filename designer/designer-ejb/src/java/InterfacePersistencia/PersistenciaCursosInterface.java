/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.Cursos;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la tabla 'Cursos' 
 * de la base de datos.
 * @author betelgeuse
 */
public interface PersistenciaCursosInterface {
    /**
     * Método encargado de buscar todos los Cursos existentes en la base de datos.
     * @return Retorna una lista de Cursos.
     */
    public List<Cursos> cursos(EntityManager em);
}
