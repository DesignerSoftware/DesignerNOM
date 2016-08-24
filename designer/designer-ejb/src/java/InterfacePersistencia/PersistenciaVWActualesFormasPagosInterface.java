/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.VWActualesFormasPagos;
import java.math.BigInteger;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la vista 'VWActualesFormasPagos' 
 * de la base de datos.
 * @author betelgeuse
 */
public interface PersistenciaVWActualesFormasPagosInterface {
    /**
     * Método encargado de buscar la forma de pago actual de un empleado, para esto se realiza la consulta
     * sobre la vista VWActualesFormasPagos.
     * El término actual está dado por la fechaHasta de las liquidaciones.
     * @param secuencia Secuencia del empleado.
     * @return Retorna una VWActualesFormasPagos con la información de la forma de pago actual de un empleado.
     */
    public VWActualesFormasPagos buscarFormaPago(EntityManager em, BigInteger secuencia);
}
