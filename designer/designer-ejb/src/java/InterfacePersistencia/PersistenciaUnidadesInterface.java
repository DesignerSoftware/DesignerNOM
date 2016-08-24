/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.Unidades;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la tabla 'Unidades' 
 * de la base de datos.
 * @author betelgeuse
 */
public interface PersistenciaUnidadesInterface {
    /**
     * Método encargado de insertar una Unidad en la base de datos.
     * @param unidad Unidad que se quiere crear.
     */
    public void crear(EntityManager em, Unidades unidad);
    /**
     * Método encargado de modificar una Unidad de la base de datos.
     * Este método recibe la información del parámetro para hacer un 'merge' con la 
     * información de la base de datos.
     * @param unidad Unidad con los cambios que se van a realizar.
     */
    public void editar(EntityManager em, Unidades unidad);
    /**
     * Método encargado de eliminar de la base de datos la Unidad que entra por parámetro.
     * @param unidad Unidad que se quiere eliminar.
     */
    public void borrar(EntityManager em, Unidades unidad);
    /**
     * Método encargado de buscar todas las Unidades existentes en la base de datos, ordenadas por código.
     * @return Retorna una lista de Unidades ordenadas por código.
     */
    public List<Unidades> consultarUnidades(EntityManager em );
}
