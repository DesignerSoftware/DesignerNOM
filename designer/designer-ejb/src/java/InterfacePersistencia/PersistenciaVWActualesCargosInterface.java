/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.VWActualesCargos;
import java.math.BigInteger;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la
 * vista 'VWActualesCargos' de la base de datos.
 *
 * @author betelgeuse
 */
public interface PersistenciaVWActualesCargosInterface {

    /**
     * Método encargado de buscar el Cargo actual de un empleado, para esto se
     * realiza la consulta sobre la vista VWActualesCargos. El término actual
     * está dado por la fechaHasta de las liquidaciones.
     *
     * @param em EntityManager encargado de la comunicación con la base de
     * datos.
     * @param secuencia Secuencia del empleado.
     * @return Retorna una VWActualesCargos con la información del Cargo actual
     * de un empleado.
     */
    public VWActualesCargos buscarCargoEmpleado(EntityManager em, BigInteger secuencia);

    /**
     * Método encargado de contar la cantidad de empleados que se encuentran
     * registrados con la secuencia de la Estructura dada
     *
     * @param secEstructura Secuencia de la Estructura.
     * @return Conteo de empleados
     */
    public Long conteoCodigosEmpleados(EntityManager em, BigInteger secEstructura);

}
