/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.TiposAsociaciones;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la tabla 'TiposAsociaciones' 
 * de la base de datos.
 * @author betelgeuse
 */
public interface PersistenciaTiposAsociacionesInterface {
    /**
     * Método encargado de insertar un TipoAsociacion en la base de datos.
     * @param asociaciones TipoAsociacion que se quiere crear.
     */
    public void crear(EntityManager em, TiposAsociaciones asociaciones);
    /**
     * Método encargado de modificar un TipoAsociacion de la base de datos.
     * Este método recibe la información del parámetro para hacer un 'merge' con la 
     * información de la base de datos.
     * @param asociaciones TipoAsociacion con los cambios que se van a realizar.
     */
    public void editar(EntityManager em, TiposAsociaciones asociaciones);
    /**
     * Método encargado de eliminar de la base de datos el TipoAsociacion que entra por parámetro.
     * @param asociaciones TipoAsociacion que se quiere eliminar.
     */
    public void borrar(EntityManager em, TiposAsociaciones asociaciones);
    /**
     * Método encargado de buscar todos los TiposAsociaciones existentes en la base de datos.
     * @return Retorna una lista de TiposAsociaciones.
     */
    public List<TiposAsociaciones> buscarTiposAsociaciones(EntityManager em);
    /**
     * Método encargado de buscar el TipoAsociacion con la secuencia dada por parámetro.
     * @param secuencia Secuencia del TipoAsociacion que se quiere encontrar.
     * @return Retorna el TipoAsociacion identificado con la secuencia dada por parámetro.
     */
    public TiposAsociaciones buscarTiposAsociacionesSecuencia(EntityManager em, BigInteger secuencia);
    
}
