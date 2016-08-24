/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.Demandas;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la tabla 'Demandas' 
 * de la base de datos.
 * @author betelgeuse
 */
public interface PersistenciaDemandasInterface {
    /**
     * Método encargado de insertar una Demanda en la base de datos.
     * @param demandas Demanda que se quiere crear.
     */
    public void crear(EntityManager em,Demandas demandas);
    /**
     * Método encargado de modificar una Demanda de la base de datos.
     * Este método recibe la información del parámetro para hacer un 'merge' con la 
     * información de la base de datos.
     * @param demandas Demanda con los cambios que se van a realizar.
     */
    public void editar(EntityManager em,Demandas demandas);
    /**
     * Método encargado de eliminar de la base de datos la Demanda que entra por parámetro.
     * @param demandas Demanda con los cambios que se van a realizar.
     */
    public void borrar(EntityManager em,Demandas demandas);
    /**
     * Método encargado de buscar todas las Demandas de un empleado especificado.
     * @param secuenciaEmpl Secuencia del empleado asociado a las demandas
     * @return Retorna una lista de Demandas.
     */
    public List<Demandas> demandasPersona(EntityManager em,BigInteger secuenciaEmpl);
}
