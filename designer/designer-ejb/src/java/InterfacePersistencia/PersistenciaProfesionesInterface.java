/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.Profesiones;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la tabla 'Profesiones' 
 * de la base de datos.
 * @author betelgeuse
 */
public interface PersistenciaProfesionesInterface {
    /**
     * Método encargado de buscar todas las Profesiones existentes en la base de datos, ordenadas por descripción.
     * @return Retorna una lista de Profesiones ordenadas por descripción.
     */
    public List<Profesiones> profesiones(EntityManager em);
}
