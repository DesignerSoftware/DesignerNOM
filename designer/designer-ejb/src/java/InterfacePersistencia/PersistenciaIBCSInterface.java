/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.Ibcs;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la tabla 'IBCS' 
 * de la base de datos.
 * @author betelgeuse
 */
@Local
public interface PersistenciaIBCSInterface {
    /**
     * Método encargado de insertar un IBCS en la base de datos.
     * @param ibcs que se quiere crear.
     */
    public void crear(EntityManager em,Ibcs ibcs);
    /**
     * Método encargado de modificar un IBCS de la base de datos.
     * Este método recibe la información del parámetro para hacer un 'merge' con la 
     * información de la base de datos.
     * @param ibcs IBCS con los cambios que se van a realizar.
     */
    public void editar(EntityManager em,Ibcs ibcs);
    /**
     * Método encargado de eliminar de la base de datos el IBCS que entra por parámetro.
     * @param ibcs IBCS que se quiere eliminar.
     */
    public void borrar(EntityManager em,Ibcs ibcs);
    /**
     * Método encargado de buscar el IBCS con la secuencia dada por parámetro.
     * @param secuencia Secuencia del IBCS que se quiere encontrar.
     * @return Retorna el IBCS identificado con la secuencia dada por parámetro.
     */
    public Ibcs buscarIbcs(EntityManager em,BigInteger secuencia);
    /**
     * Método encargado de buscar los IBCS de un empleado y los ordena por fechaInicial de forma descendiente.
     * @param secEmpleado Secuencia del empleado al que se le quieren averiguar los IBCS.
     * @return Retorna una lista de IBCS asociados a un empleado.
     */
    public List<Ibcs> buscarIbcsPorEmpleado(EntityManager em,BigInteger secEmpleado);
}
