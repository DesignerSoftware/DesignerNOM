/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.Operadores;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la tabla 'Operadores' 
 * de la base de datos.
 * @author Andres Pineda.
 */
public interface PersistenciaOperadoresInterface {
    /**
     * Método encargado de buscar todos los Operadores existentes en la base de datos.
     * @return Retorna una lista de Operadores.
     */
    public List<Operadores> buscarOperadores(EntityManager em);
}
