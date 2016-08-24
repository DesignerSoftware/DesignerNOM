/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.VigenciasEventos;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la tabla 'VigenciasEventos' 
 * de la base de datos.
 * @author betelgeuse
 */
public interface PersistenciaVigenciasEventosInterface {
    /**
     * Método encargado de insertar una VigenciaEvento en la base de datos.
     * @param vigenciasEventos VigenciaEvento que se quiere crear.
     */
    public void crear(EntityManager em, VigenciasEventos vigenciasEventos);
    /**
     * Método encargado de modificar una VigenciaEvento de la base de datos.
     * Este método recibe la información del parámetro para hacer un 'merge' con la 
     * información de la base de datos.
     * @param vigenciasEventos VigenciaEvento con los cambios que se van a realizar.
     */
    public void editar(EntityManager em, VigenciasEventos vigenciasEventos);
    /**
     * Método encargado de eliminar de la base de datos la VigenciaEvento que entra por parámetro.
     * @param vigenciasEventos VigenciaEvento que se quiere eliminar.
     */
    public void borrar(EntityManager em, VigenciasEventos vigenciasEventos);
    /**
     * Método encargado de buscar las últimas VigenciasEventos registradas de un Empleado específico.
     * @param secuencia Secuencia del Empleado.
     * @return Retorna una lista de las VigenciasEstadosCiviles asociadas a un Empleado. 
     */    
    public List<VigenciasEventos> eventosEmpleado(EntityManager em, BigInteger secuencia);
    /**
     * Método encargado de buscar las VigenciasEstadosCiviles registradas de un Empleado específico.
     * @param secuencia Secuencia del Empleado.
     * @return Retorna una lista de las VigenciasEstadosCiviles asociadas a un Empleado. 
     */
    public List<VigenciasEventos> vigenciasEventosSecuenciaEmpleado(EntityManager em, BigInteger secuencia);
}
