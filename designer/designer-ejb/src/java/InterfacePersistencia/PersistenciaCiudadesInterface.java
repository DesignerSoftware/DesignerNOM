/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.Ciudades;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la tabla 'Ciudades' 
 * de la base de datos.
 * @author betelgeuse
 */
public interface PersistenciaCiudadesInterface {
    /**
     * Método encargado de insertar una Ciudad en la base de datos.
     * @param ciudades Ciudad que se quiere crear.
     */
    public void crear(EntityManager em,Ciudades ciudades);
    /**
     * Método encargado de modificar una Ciudad de la base de datos.
     * Este método recibe la información del parámetro para hacer un 'merge' con la 
     * información de la base de datos.
     * @param ciudades Ciudad con los cambios que se van a realizar.
     */
    public void editar(EntityManager em,Ciudades ciudades);
    /**
     * Método encargado de eliminar de la base de datos la ciudad que entra por parámetro.
     * @param ciudades Ciudad que se quiere eliminar.
     */
    public boolean borrar(EntityManager em,Ciudades ciudades);
    /**
     * Método encargado de buscar todas las ciudades existentes en la base de datos.
     * @return Retorna una lista de Ciudades.
     */
    public List<Ciudades> consultarCiudades(EntityManager em);
    
    public List<Ciudades> lovCiudades(EntityManager em);
}
