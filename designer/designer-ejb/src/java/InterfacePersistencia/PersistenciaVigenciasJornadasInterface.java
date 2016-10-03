/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.VigenciasJornadas;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la tabla 'VigenciasJornadas' 
 * de la base de datos.
 * @author AndresPineda
 */
public interface PersistenciaVigenciasJornadasInterface {
    /**
     * Método encargado de insertar una VigenciaJornada en la base de datos.
     * @param vigenciasJornadas VigenciaJornada que se quiere crear.
     */
    public boolean crear(EntityManager em, VigenciasJornadas vigenciasJornadas);
    /**
     * Método encargado de modificar una VigenciaJornada de la base de datos.
     * Este método recibe la información del parámetro para hacer un 'merge' con la 
     * información de la base de datos.
     * @param vigenciasJornadas VigenciaJornada con los cambios que se van a realizar.
     */
    public void editar(EntityManager em, VigenciasJornadas vigenciasJornadas);
    /**
     * Método encargado de eliminar de la base de datos la VigenciaJornada que entra por parámetro.
     * @param vigenciasJornadas VigenciaJornada que se quiere eliminar.
     */
    public void borrar(EntityManager em, VigenciasJornadas vigenciasJornadas);
    /**
     * Método encargado de buscar todas las VigenciasJornadas existentes en la base de datos.
     * @return Retorna una lista de VigenciasJornadas.
     */
    public List<VigenciasJornadas> buscarVigenciasJornadas(EntityManager em );
    /**
     * Método encargado de buscar las VigenciasJornadas de un Empleado específico.
     * @param secuencia Secuencia del Empleado.
     * @return Retorna las VigenciasJornadas, odenadas descendentemente por la fechaVigencia, del Empleado cuya secuencia coincide 
     * con la secuencia dada por parámetro.
     */
    public List<VigenciasJornadas> buscarVigenciasJornadasEmpleado(EntityManager em, BigInteger secuencia);
    /**
     * Método encargado de buscar la VigenciaJornada con la secuencia dada por parámetro.
     * @param secuencia Secuencia de la VigenciaJornada que se quiere encontrar.
     * @return Retorna la VigenciaJornada identificada con la secuencia dada por parámetro.
     */
    public VigenciasJornadas buscarVigenciasJornadasSecuencia(EntityManager em, BigInteger secuencia);
    
}
