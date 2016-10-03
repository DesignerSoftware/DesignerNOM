/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.VigenciasUbicaciones;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la tabla 'VigenciasUbicaciones' 
 * de la base de datos.
 * @author betelgeuse
 */
public interface PersistenciaVigenciasUbicacionesInterface {
    /**
     * Método encargado de insertar una VigenciaUbicacion en la base de datos.
     * @param vigenciasUbicaciones VigenciaUbicacion que se quiere crear.
     */
    public boolean crear(EntityManager em, VigenciasUbicaciones vigenciasUbicaciones);
    /**
     * Método encargado de modificar una VigenciaUbicacion de la base de datos.
     * Este método recibe la información del parámetro para hacer un 'merge' con la 
     * información de la base de datos.
     * @param vigenciasUbicaciones VigenciaUbicacion con los cambios que se van a realizar.
     */
    public void editar(EntityManager em, VigenciasUbicaciones vigenciasUbicaciones);
    /**
     * Método encargado de eliminar de la base de datos la VigenciaUbicacion que entra por parámetro.
     * @param vigenciasUbicaciones VigenciaUbicacion que se quiere eliminar.
     */
    public void borrar(EntityManager em, VigenciasUbicaciones vigenciasUbicaciones);
    /**
     * Método encargado de buscar la VigenciaUbicacion con la secuencia dada por parámetro.
     * @param secuencia Secuencia de la VigenciaUbicacion que se quiere encontrar.
     * @return Retorna la VigenciaUbicacion identificada con la secuencia dada por parámetro.
     */
    public VigenciasUbicaciones buscarVigenciaUbicacion(EntityManager em, BigInteger secuencia);
    /**
     * Método encargado de buscar todas las VigenciasUbicaciones existentes en la base de datos.
     * @return Retorna una lista de VigenciasUbicaciones.
     */
    public List<VigenciasUbicaciones> buscarVigenciasUbicaciones(EntityManager em );
    /**
     * Método encargado de buscar las VigenciasUbicaciones de un Empleado específico.
     * @param secuencia Secuencia del Empleado.
     * @return Retorna las VigenciasUbicaciones, odenadas descendentemente por la fechaVigencia, del Empleado cuya secuencia coincide 
     * con la secuencia dada por parámetro.
     */
    public List<VigenciasUbicaciones> buscarVigenciaUbicacionesEmpleado(EntityManager em, BigInteger secuencia);
}
