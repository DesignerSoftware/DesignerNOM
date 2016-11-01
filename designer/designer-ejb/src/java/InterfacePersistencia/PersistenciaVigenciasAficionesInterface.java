/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.VigenciasAficiones;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la tabla 'VigenciasAficiones' 
 * de la base de datos.
 * @author betelgeuse
 */
public interface PersistenciaVigenciasAficionesInterface {
    /**
     * Método encargado de insertar una VigenciaAficion en la base de datos.
     * @param vigenciasAficiones VigenciaAficion que se quiere crear.
     */
    public void crear(EntityManager em, VigenciasAficiones vigenciasAficiones);
    /**
     * Método encargado de modificar una VigenciaAficion de la base de datos.
     * Este método recibe la información del parámetro para hacer un 'merge' con la 
     * información de la base de datos.
     * @param vigenciasAficiones VigenciaAficion con los cambios que se van a realizar.
     */
    public void editar(EntityManager em, VigenciasAficiones vigenciasAficiones);
    /**
     * Método encargado de eliminar de la base de datos la VigenciaAficion que entra por parámetro.
     * @param vigenciasAficiones VigenciaAficion que se quiere eliminar.
     */
    public void borrar(EntityManager em, VigenciasAficiones vigenciasAficiones);
    /**
     * Método encargado de buscar la VigenciaAficion con la secuencia dada por parámetro.
     * @param secuencia Secuencia de la VigenciaAficion que se quiere encontrar.
     * @return Retorna la VigenciaAficion identificada con la secuencia dada por parámetro.
     */
    public VigenciasAficiones buscarvigenciaAficion(EntityManager em, BigInteger secuencia);
    /**
     * Método encargado de buscar las últimas VigenciasAficiones de una Persona.
     * @param secuencia Secuencia de la Persona.
     * @return Retorna las VigenciasAficiones de la Persona cuya secuencia coincide 
     * con la secuencia dada por parámetro y su fechaInicial es la maxima fecha entre las aficiones de 
     * la Persona.
     */
    public List<VigenciasAficiones> aficionesPersona(EntityManager em, BigInteger secuencia);
    /**
     * Método encargado de buscar las VigenciasAficiones de una Persona.
     * @param secuencia Secuencia de la Persona.
     * @return Retorna las VigenciasAficiones de la Persona cuya secuencia coincide 
     * con la secuencia dada por parámetro.
     */
    public List<VigenciasAficiones> aficionesTotalesSecuenciaPersona(EntityManager em, BigInteger secuencia);
    
    public String primeraAficion(EntityManager em, BigInteger secuencia);
    
}
