/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.VigenciasLocalizaciones;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la tabla 'VigenciasLocalizaciones' 
 * de la base de datos.
 * @author AndresPineda
 */
public interface PersistenciaVigenciasLocalizacionesInterface {
    /**
     * Método encargado de insertar una VigenciaLocalizacion en la base de datos.
     * @param vigenciasLocalizaciones VigenciaLocalizacion que se quiere crear.
     */
    public boolean crear(EntityManager em, VigenciasLocalizaciones vigenciasLocalizaciones);
    /**
     * Método encargado de modificar una VigenciaLocalizacion de la base de datos.
     * Este método recibe la información del parámetro para hacer un 'merge' con la 
     * información de la base de datos.
     * @param vigenciasLocalizaciones VigenciaLocalizacion con los cambios que se van a realizar.
     */
    public void editar(EntityManager em, VigenciasLocalizaciones vigenciasLocalizaciones);
    /**
     * Método encargado de eliminar de la base de datos la VigenciaLocalizacion que entra por parámetro.
     * @param vigenciasLocalizaciones VigenciaLocalizacion que se quiere eliminar.
     */
    public void borrar(EntityManager em, VigenciasLocalizaciones vigenciasLocalizaciones);
    /**
     * Método encargado de buscar todas las VigenciasLocalizaciones existentes en la base de datos.
     * @return Retorna una lista de VigenciasLocalizaciones.
     */
    public List<VigenciasLocalizaciones> buscarVigenciasLocalizaciones(EntityManager em );
    /**
     * Método encargado de buscar las VigenciasLocalizaciones de un Empleado específico.
     * @param secuencia Secuencia del Empleado.
     * @return Retorna las VigenciasLocalizaciones, odenadas descendentemente por la fechaInicial, del Empleado cuya secuencia coincide 
     * con la secuencia dada por parámetro.
     */
    public List<VigenciasLocalizaciones> buscarVigenciasLocalizacionesEmpleado(EntityManager em, BigInteger secuencia);
    /**
     * Método encargado de buscar la VigenciaLocalizacion con la secuencia dada por parámetro.
     * @param secuencia Secuencia de la VigenciaLocalizacion que se quiere encontrar.
     * @return Retorna la VigenciaLocalizacion identificada con la secuencia dada por parámetro.
     */
    public VigenciasLocalizaciones buscarVigenciasLocalizacionesSecuencia(EntityManager em, BigInteger secuencia);
    
}
