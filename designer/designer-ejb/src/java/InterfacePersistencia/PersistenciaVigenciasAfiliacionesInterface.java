/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.VigenciasAfiliaciones;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la tabla 'VigenciasAfiliaciones' 
 * de la base de datos.
 * @author AndresPineda
 */
public interface PersistenciaVigenciasAfiliacionesInterface {
    /**
     * Método encargado de insertar una VigenciaAfiliacion en la base de datos.
     * @param vigenciasAfiliaciones VigenciaAfiliacion que se quiere crear.
     */
    public boolean crear(EntityManager em, VigenciasAfiliaciones vigenciasAfiliaciones);
    /**
     * Método encargado de modificar una VigenciaAfiliacion de la base de datos.
     * Este método recibe la información del parámetro para hacer un 'merge' con la 
     * información de la base de datos.
     * @param vigenciasAfiliaciones VigenciaAfiliacion con los cambios que se van a realizar.
     */
    public void editar(EntityManager em, VigenciasAfiliaciones vigenciasAfiliaciones);
    /**
     * Método encargado de eliminar de la base de datos la VigenciaAfiliacion que entra por parámetro.
     * @param vigenciasAfiliaciones VigenciaAfiliacion que se quiere eliminar.
     */
    public void borrar(EntityManager em, VigenciasAfiliaciones vigenciasAfiliaciones);
    /**
     * Método encargado de buscar todas las VigenciasAfiliaciones existentes en la base de datos.
     * @return Retorna una lista de VigenciasAfiliaciones.
     */
    public List<VigenciasAfiliaciones> buscarVigenciasAfiliaciones(EntityManager em );
    /**
     * Método encargado de buscar la VigenciaAfiliacion con la secuencia dada por parámetro.
     * @param secuencia Secuencia de la VigenciaAfiliacion que se quiere encontrar.
     * @return Retorna la VigenciaAfiliacion identificada con la secuencia dada por parámetro.
     */
    public VigenciasAfiliaciones buscarVigenciasAfiliacionesSecuencia(EntityManager em, BigInteger secuencia);
    /**
     * Método encargado de buscar las VigenciasAfiliaciones de un Empleado específico.
     * @param secuencia Secuencia del Empleado.
     * @return Retorna las VigenciasAfiliaciones, odenadas descendentemente por la fechaInicial, del Empleado cuya secuencia coincide 
     * con la secuencia dada por parámetro.
     */
    public List<VigenciasAfiliaciones> buscarVigenciasAfiliacionesEmpleado(EntityManager em, BigInteger secuencia);
    /**
     * Método encargado de buscar las VigenciaAfiliacion asociadas a una VigenciaSueldo específica.
     * @param secuencia Secuencia de la vigenciaSueldo.
     * @return Retorna la lista de VigenciasAfiliaciones que estan asociadas con la VigenciaSueldo
     * cuya secuencia coincide con la secuencia dada por parámetro. 
     */
    public List<VigenciasAfiliaciones> buscarVigenciasAfiliacionesVigenciaSecuencia(EntityManager em, BigInteger secuencia);
}
