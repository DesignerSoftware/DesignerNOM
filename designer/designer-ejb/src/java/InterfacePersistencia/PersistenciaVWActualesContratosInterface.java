/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.VWActualesContratos;
import java.math.BigInteger;
import javax.persistence.EntityManager;
/**
 * Interface encargada de determinar las operaciones que se realizan sobre la vista 'VWActualesContratos' 
 * de la base de datos.
 * @author betelgeuse
 */
public interface PersistenciaVWActualesContratosInterface {
    /**
     * Método encargado de buscar el Contrato actual de un empleado, para esto se realiza la consulta
     * sobre la vista VWActualesContratos.
     * El término actual está dado por la fechaHasta de las liquidaciones.
     * @param secuencia Secuencia del empleado.
     * @return Retorna una VWActualesContratos con la información del Contrato actual de un empleado.
     */
    public VWActualesContratos buscarContrato(EntityManager em, BigInteger secuencia);
}
