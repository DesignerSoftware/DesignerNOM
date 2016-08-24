/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.VWActualesNormasEmpleados;
import java.math.BigInteger;
import javax.persistence.EntityManager;
/**
 * Interface encargada de determinar las operaciones que se realizan sobre la vista 'VWActualesNormasEmpleados' 
 * de la base de datos.
 * @author betelgeuse
 */
public interface PersistenciaVWActualesNormasEmpleadosInterface {
    /**
     * Método encargado de buscar la norma actual de un empleado, para esto se realiza la consulta
     * sobre la vista VWActualesNormasEmpleados.
     * El término actual está dado por la fechaHasta de las liquidaciones.
     * @param secuencia Secuencia del empleado.
     * @return Retorna una VWActualesNormasEmpleados con la información de la norma actual de un empleado.
     */
    public VWActualesNormasEmpleados buscarNormaLaboral(EntityManager em, BigInteger secuencia);
}
