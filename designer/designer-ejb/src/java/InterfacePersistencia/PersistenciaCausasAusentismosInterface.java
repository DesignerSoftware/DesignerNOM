/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.Causasausentismos;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la tabla 'CausasAusentismos' 
 * de la base de datos.
 * @author betelgeuse
 */

public interface PersistenciaCausasAusentismosInterface {
    /**
     * Método encargado de insertar una CausasAusentismos en la base de datos.
     * @param causasAusentismos CausasAusentismos que se quiere crear
     */
    public void crear(EntityManager em,Causasausentismos causasAusentismos);
    /**
     * Método encargado de modificar una CausasAusentismos de la base de datos.
     * Este método recibe la información del parámetro para hacer un 'merge' con la 
     * información de la base de datos.
     * @param causasAusentismos CausasAusentismos con los cambios que se van a realizar
     */
    public void editar(EntityManager em,Causasausentismos causasAusentismos);
    /**
     * Método encargado de eliminar de la base de datos la CausasAusentismos que entra por parámetro.
     * @param causasAusentismos CausasAusentismos que se quiere eliminar.
     */
    public void borrar(EntityManager em,Causasausentismos causasAusentismos);
    /**
     * Método encargado de buscar todas las Causasausentismos existentes en la base de datos.
     * @return Retorna una lista de CausasAusentismos.
     */
    public List<Causasausentismos> buscarCausasAusentismos(EntityManager em);
}
