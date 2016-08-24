/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.EstadosAfiliaciones;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * 
 * @author betelgeuse
 */
public interface PersistenciaEstadosAfiliacionesInterface {
    /**
     * Método encargado de insertar un contrato en la base de datos.
     * @param afiliaciones EstadoAfiliacion que se quiere crear.
     */
    public void crear(EntityManager em,EstadosAfiliaciones afiliaciones);
    /**
     * Método encargado de modificar un EstadoAfiliacion de la base de datos.
     * Este método recibe la información del parámetro para hacer un 'merge' con la 
     * información de la base de datos.
     * @param afiliaciones EstadoAfiliacion con los cambios que se van a realizar.
     */
    public void editar(EntityManager em,EstadosAfiliaciones afiliaciones);
    /**
     * Método encargado de eliminar de la base de datos el EstadoAfiliacion que entra por parámetro.
     * @param afiliaciones EstadoAfiliacion que se quiere eliminar.
     */
    public void borrar(EntityManager em,EstadosAfiliaciones afiliaciones);
    /**
     * Método encargado de buscar el EstadoAfiliacion con la secuencia dada por parámetro.
     * @param secuencia Identificador único del EstadoAfiliacion que se quiere encontrar.
     * @return Retorna el EstadoAfiliacion identificado con la secuencia dada por parámetro.
     */
    public EstadosAfiliaciones buscarEstadoAfiliacion(EntityManager em,BigInteger secuencia);
    /**
     * Método encargado de buscar todos los EstadosAfiliaciones existentes en la base de datos.
     * @return Retorna una lista de EstadosAfiliaciones.
     */
    public List<EstadosAfiliaciones> buscarEstadosAfiliaciones(EntityManager em);
}
