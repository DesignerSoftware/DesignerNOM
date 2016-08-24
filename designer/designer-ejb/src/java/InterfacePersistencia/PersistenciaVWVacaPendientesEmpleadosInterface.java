/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.VWVacaPendientesEmpleados;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la vista 'VWVacaPendientesEmpleados' 
 * de la base de datos.
 * @author betelgeuse
 */
public interface PersistenciaVWVacaPendientesEmpleadosInterface {
    /**
     * Método encargado de insertar una VWVacaPendienteEmpleado en la base de datos.
     * @param vacaP VWVacaPendienteEmpleado que se quiere crear.
     */
    public void crear(EntityManager em, VWVacaPendientesEmpleados vacaP);
    /**
     * Método encargado de modificar una VWVacaPendienteEmpleado de la base de datos.
     * Este método recibe la información del parámetro para hacer un 'merge' con la 
     * información de la base de datos.
     * @param vacaP VWVacaPendienteEmpleado con los cambios que se van a realizar.
     */
    public void editar(EntityManager em, VWVacaPendientesEmpleados vacaP);
    /**
     * Método encargado de eliminar de la base de datos la VWVacaPendienteEmpleado que entra por parámetro.
     * @param vacaP VWVacaPendienteEmpleado que se quiere eliminar.
     */
    public void borrar(EntityManager em, VWVacaPendientesEmpleados vacaP);
    /**
     * Método encargado de buscar las VWVacaPendienteEmpleado de un empleado.
     * @param secuencia Secuencia del empleado.
     * @return Retorna una lista de VWVacaPendienteEmpleado asociadas a un empleado.
     */
    public List<VWVacaPendientesEmpleados> buscarVacaPendientesEmpleados(EntityManager em, BigInteger secuencia);
    /**
     * Método encargado de buscar las VWVacaPendienteEmpleado de un empleado el cual
     * tiene días de vacaciones pendientes.
     * @param secuencia Secuencia del empleado.
     * @return Retorna una lista de VWVacaPendienteEmpleado asociadas a un empleado y cuyo valor en diaspendientes
     * es mayor a cero.
     */
    public List<VWVacaPendientesEmpleados> vacaEmpleadoPendientes(EntityManager em, BigInteger secuencia);
    /**
     * Método encargado de buscar las VWVacaPendienteEmpleado de un empleado el cual
     * NO tiene días de vacaciones pendientes.
     * @param secuencia Secuencia del empleado.
     * @return Retorna una lista de VWVacaPendienteEmpleado asociadas a un empleado y cuyo valor en diaspendientes
     * es igual o menor a cero.
     */
    public List<VWVacaPendientesEmpleados> vacaEmpleadoDisfrutadas(EntityManager em, BigInteger secuencia);
    
}
