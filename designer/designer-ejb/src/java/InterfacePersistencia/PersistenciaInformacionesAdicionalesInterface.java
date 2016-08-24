/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.InformacionesAdicionales;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la tabla 'InformacionesAdicionales' 
 * de la base de datos.
 * @author betelgeuse
 */
public interface PersistenciaInformacionesAdicionalesInterface {
    /**
     * Método encargado de insertar una InformacionAdicional en la base de datos.
     * @param informacionesAdicionales InformacionAdicional que se quiere crear.
     */
    public void crear(EntityManager em, InformacionesAdicionales informacionesAdicionales);
    /**
     * Método encargado de modificar una InformacionAdicional de la base de datos.
     * Este método recibe la información del parámetro para hacer un 'merge' con la 
     * información de la base de datos.
     * @param informacionesAdicionales InformacionAdicional con los cambios que se van a realizar.
     */
    public void editar(EntityManager em, InformacionesAdicionales informacionesAdicionales);
    /**
     * Método encargado de eliminar de la base de datos la InformacionAdicional que entra por parámetro.
     * @param informacionesAdicionales InformacionAdicional que se quiere eliminar.
     */
    public void borrar(EntityManager em, InformacionesAdicionales informacionesAdicionales);
    /**
     * Método encargado de buscar la InformacionAdicional con la secuencia dada por parámetro.
     * @param secuencia Secuencia de la InformacionAdicional que se quiere encontrar.
     * @return Retorna la InformacionAdicional identificada con la secuencia dada por parámetro. 
     */
    public InformacionesAdicionales buscarinformacionAdicional(EntityManager em, BigInteger secuencia);
    /**
     * Método encargado de buscar todas las InformacionesAdicionales existentes en la base de datos.
     * @return Retorna una lista de InformacionesAdicionales.
     */
    public List<InformacionesAdicionales> buscarinformacionesAdicionales(EntityManager em );
    /**
     * Método encargado de buscar las InformacionesAdicionales actuales de un empleado especifico.
     * @param secuenciaEmpleado Secuencia del empleado a quien pertenecen las InformacionesAdicionales.
     * @return Retorna una lista de InformacionesAdicionales asociadas a un empleado.
     */
    public List<InformacionesAdicionales> informacionAdicionalPersona(EntityManager em, BigInteger secuenciaEmpleado);
    /**
     * Método encargado de buscar todas las InformacionesAdicionales de un empleado especifico.
     * @param secuenciaEmpleado Secuencia del empleado a quien pertenecen las InformacionesAdicionales.
     * @return Retorna una lista de InformacionesAdicionales asociadas a un empleado.
     */
    public List<InformacionesAdicionales> informacionAdicionalEmpleadoSecuencia(EntityManager em, BigInteger secuenciaEmpleado);
}
