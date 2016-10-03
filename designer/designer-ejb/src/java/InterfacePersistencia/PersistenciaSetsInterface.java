/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.Sets;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la tabla 'Sets' 
 * de la base de datos.
 * @author AndresPimeda
 */
public interface PersistenciaSetsInterface {
    /**
     * Método encargado de insertar un Set en la base de datos.
     * @param sets Set que se quiere crear.
     */
    public boolean crear(EntityManager em, Sets sets);
    /**
     * Método encargado de modificar un Set de la base de datos.
     * Este método recibe la información del parámetro para hacer un 'merge' con la 
     * información de la base de datos.
     * @param sets Set con los cambios que se van a realizar.
     */
    public void editar(EntityManager em, Sets sets);
    /**
     * Método encargado de eliminar de la base de datos el Set que entra por parámetro.
     * @param sets Set que se quiere eliminar.
     */
    public void borrar(EntityManager em, Sets sets);    
    /**
     * Método encargado de buscar todos los Sets existentes en la base de datos.
     * @return Retorna una lista de Sets.
     */
    public List<Sets> buscarSets(EntityManager em);
    /**
     * Método encargado de buscar el Set con la secuencia dada por parámetro.
     * @param secuencia Secuencia del Set que se quiere encontrar.
     * @return Retorna el Set identificado con la secuencia dada por parámetro.
     */
    public Sets buscarSetSecuencia(EntityManager em, BigInteger secuencia);
    /**
     * Método encargado de buscar los Sets asociados con un empleado específico.
     * @param secEmpleado Secuencia del empleado por el cual se quieren buscar los Sets.
     * @return Retorna una lista de sets ordenados por fechaInicial.
     */
    public List<Sets> buscarSetsEmpleado(EntityManager em, BigInteger secEmpleado);
    
}
