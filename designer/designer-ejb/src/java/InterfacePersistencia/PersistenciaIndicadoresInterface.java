/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.Indicadores;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la tabla 'Indicadores' 
 * de la base de datos.
 * @author betelgeuse
 */
public interface PersistenciaIndicadoresInterface {
    /**
     * Método encargado de insertar un Indicador en la base de datos.
     * @param indicadores Indicador que se quiere crear.
     */
    public void crear(EntityManager em, Indicadores indicadores);
    /**
     * Método encargado de modificar un Indicador de la base de datos.
     * Este método recibe la información del parámetro para hacer un 'merge' con la 
     * información de la base de datos.
     * @param indicadores Indicador con los cambios que se van a realizar.
     */
    public void editar(EntityManager em, Indicadores indicadores);
    /**
     * Método encargado de eliminar de la base de datos el Indicador que entra por parámetro.
     * @param indicadores Indicador que se quiere eliminar.
     */
    public void borrar(EntityManager em, Indicadores indicadores);
    /**
     * Método encargado de buscar todos los Indicadores existentes en la base de datos.
     * @return Retorna una lista de Indicadores.
     */
    public List<Indicadores> buscarIndicadores(EntityManager em);
    /**
     * Método encargado de buscar el Indicador con la secuencia dada por parámetro.
     * @param secuencia Secuencia del Indicador que se quiere encontrar.
     * @return Retorna el Indicador identificado con la secuencia dada por parámetro.
     */
    public Indicadores buscarIndicadoresSecuencia(EntityManager em, BigInteger secuencia);
    
}
