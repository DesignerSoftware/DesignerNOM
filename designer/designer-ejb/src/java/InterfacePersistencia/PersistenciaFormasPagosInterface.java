/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.FormasPagos;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la tabla 'FormasPagos' 
 * de la base de datos.
 * @author betelgeuse
 */
@Local
public interface PersistenciaFormasPagosInterface {
    /**
     * Método encargado de insertar una FormaPago en la base de datos.
     * @param formasPagos FormaPago que se quiere crear.
     */
    public void crear(EntityManager em,FormasPagos formasPagos);
    /**
     * Método encargado de modificar una FormaPago de la base de datos.
     * Este método recibe la información del parámetro para hacer un 'merge' con la 
     * información de la base de datos.
     * @param formasPagos FormaPago con los cambios que se van a realizar.
     */
    public void editar(EntityManager em,FormasPagos formasPagos);
    /**
     * Método encargado de eliminar de la base de datos la FormaPago que entra por parámetro.
     * @param formasPagos FormaPago con los cambios que se van a realizar.
     */
    public void borrar(EntityManager em,FormasPagos formasPagos);
    /**
     * Método encargado de buscar la FormaPago con la secuencia dada por parámetro.
     * @param secuencia Secuencia de la FormaPago que se quiere encontrar.
     * @return Retorna la FormaPago identificada con la secuencia dada por parámetro.
     */
    public FormasPagos buscarFormasPagos(EntityManager em,BigInteger secuencia);
    /**
     * Método encargado de buscar las FormasPagos de un empleado.
     * @param secEmpleado Secuencia del empleado al cual se le van a buscar las FormasPagos.
     * @return Retorna una lista de FormasPagos asociadas a un empleado.
     */
    public List<FormasPagos> buscarFormasPagosPorEmpleado(EntityManager em,BigInteger secEmpleado);
    /**
     * Método encargado de buscar todas las FormasPagos existentes en la base de datos.
     * @return Retorna una lista de FormasPagos.
     */
    public List<FormasPagos> buscarFormasPagos(EntityManager em);
}
