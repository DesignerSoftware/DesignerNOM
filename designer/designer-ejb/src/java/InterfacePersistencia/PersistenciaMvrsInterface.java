/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.Mvrs;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la tabla 'Mvrs' 
 * de la base de datos.
 * @author betelgeuse
 */
public interface PersistenciaMvrsInterface {
    /**
     * Método encargado de insertar un Mvrs en la base de datos.
     * @param mvrs Mvrs que se quiere crear.
     */
    public void crear(EntityManager em, Mvrs mvrs);
    /**
     * Método encargado de modificar un Mvrs de la base de datos.
     * Este método recibe la información del parámetro para hacer un 'merge' con la 
     * información de la base de datos.
     * @param mvrs Mvrs con los cambios que se van a realizar.
     */
    public void editar(EntityManager em, Mvrs mvrs);
    /**
     * Método encargado de eliminar de la base de datos el Mvrs que entra por parámetro.
     * @param mvrs Mvrs que se quiere eliminar.
     */
    public void borrar(EntityManager em, Mvrs mvrs);
    /**
     * Método encargado de buscar todos los Mvrs existentes en la base de datos.
     * @return Retorna una lista de Mvrs.
     */
    public List<Mvrs> buscarMvrs(EntityManager em);
    /**
     * Método encargado de buscar el Mvrs con la secuencia dada por parámetro.
     * @param secuencia Secuencia del Mvrs que se quiere encontrar.
     * @return Retorna el Mvrs identificado con la secuencia dada por parámetro.
     */
    public Mvrs buscarMvrSecuencia(EntityManager em, BigInteger secuencia);
    /**
     * Método encargado de buscar los Mvrs de una empleado.
     * @param secuencia Secuencia del Empleado asociado a los Mvrs.
     * @return Retorna una lista de Mvrs.
     */
    public List<Mvrs> buscarMvrsEmpleado(EntityManager em, BigInteger secuencia);
    
}
