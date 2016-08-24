/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.Encargaturas;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la tabla 'Encargaturas' 
 * de la base de datos.
 * @author betelgeuse
 */
public interface PersistenciaEncargaturasInterface {
    /**
     * Método encargado de recuperar la información referente a la última Encargatura (el último reemplazo) 
     * de un empleado especifico.
     * @param secuenciaEmpleado Secuencia del empleado.
     * @return Retorna una lista de Encargaturas cuya fecha inicial es la mayor encontrada en la base de datos y pertenecen a un empleado.
     */
    public List<Encargaturas> reemplazoPersona(EntityManager em,BigInteger secuenciaEmpleado);
    /**
     * Método encargado de insertar una Encargatura en la base de datos.
     * @param encargaturas Encargatura que se quiere crear.
     */
    public void crear(EntityManager em,Encargaturas encargaturas);
    /**
     * Método encargado de modificar una Encargatura de la base de datos.
     * Este método recibe la información del parámetro para hacer un 'merge' con la 
     * información de la base de datos.
     * @param encargaturas Encargatura con los cambios que se van a realizar.
     */
    public void editar(EntityManager em,Encargaturas encargaturas);
    /**
     * Método encargado de eliminar de la base de datos la Encargatura que entra por parámetro.
     * @param encargaturas Encargatura que se quiere eliminar.
     */
    public void borrar(EntityManager em,Encargaturas encargaturas);
    /**
     * Método encargado de buscar todas las Encargaturas existentes en la base de datos.
     * @return Retorna una lista de Encargaturas.
     */
    public List<Encargaturas> buscarEncargaturas(EntityManager em);
    /**
     * Método encargado de buscar las Encargaturas de un empleado.
     * @param secuenciaEmpleado Secuencia del empleado.
     * @return Retorna una lista de Encargaturas relacionadas con el empleado cuya secuencia es igual al parametro.
     */
    public List<Encargaturas> encargaturasEmpleado(EntityManager em,BigInteger secuenciaEmpleado);
    
}
