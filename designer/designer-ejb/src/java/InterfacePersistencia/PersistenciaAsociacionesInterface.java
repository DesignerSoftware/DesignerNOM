/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.Asociaciones;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la tabla 'Asociaciones' 
 * de la base de datos.
 * @author betelgeuse
 */
public interface PersistenciaAsociacionesInterface { 
    /**
     * Método encargado de insertar una asociación en la base de datos.
     * @param asociaciones Asociación que se quiere crear.
     */
    public void crear(EntityManager em,Asociaciones asociaciones);
    /**
     * Método encargado de modificar una asociación de la base de datos.
     * Este método recibe la información del parámetro para hacer un 'merge' con la 
     * información de la base de datos.
     * @param asociaciones Asociación con los cambios que se van a realizar.
     */
    public void editar(EntityManager em,Asociaciones asociaciones);
    /**
     * Método encargado de eliminar de la base de datos la asociación que entra por parámetro.
     * @param asociaciones Asociación que se quiere eliminar
     */
    public void borrar(EntityManager em,Asociaciones asociaciones);
    /**
     * Método encargado de buscar todas las asociaciones existentes en la base de datos.
     * @return Retorna una lista de asociaciones.
     */
    public List<Asociaciones> buscarAsociaciones(EntityManager em);
    /**
     * Método encargado de buscar la asociación con una secuencia determinada.
     * @param secuencia Secuencia de la asociación que se quiere buscar.
     * @return Retorna la asociación cuya secuencia es igual al que entra por parámetro.
     */
    public Asociaciones buscarAsociacionesSecuencia(EntityManager em,BigInteger secuencia);
    
}
