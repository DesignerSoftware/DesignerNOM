/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.TiposJornadas;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la tabla 'TiposJornadas' 
 * de la base de datos.
 * @author Andres Pineda.
 */
public interface PersistenciaTiposJornadasInterface {
    /**
     * Método encargado de insertar un TipoJornada en la base de datos.
     * @param tiposJornadas TipoJornada que se quiere crear.
     */
    public void crear(EntityManager em, TiposJornadas tiposJornadas);
    /**
     * Método encargado de modificar un TipoJornada de la base de datos.
     * Este método recibe la información del parámetro para hacer un 'merge' con la 
     * información de la base de datos.
     * @param tiposJornadas TipoJornada con los cambios que se van a realizar.
     */
    public void editar(EntityManager em, TiposJornadas tiposJornadas);
    /**
     * Método encargado de eliminar de la base de datos el TipoJornada que entra por parámetro.
     * @param tiposJornadas TipoJornada que se quiere eliminar.
     */
    public void borrar(EntityManager em, TiposJornadas tiposJornadas);
    /**
     * Método encargado de buscar el TipoJornada con la secuencia dada por parámetro.
     * @param secuencia Secuencia del TipoJornada que se quiere encontrar.
     * @return Retorna el TipoJornada identificado con la secuencia dada por parámetro.
     */
    public TiposJornadas buscarTipoJornada(EntityManager em, BigInteger secuencia);
    /**
     * Método encargado de buscar todos los TiposJornadas existentes en la base de datos, ordenados
     * descendentemente por código.
     * @return Retorna una lista de TipoJornadas.
     */
    public List<TiposJornadas> buscarTiposJornadas(EntityManager em );
}
