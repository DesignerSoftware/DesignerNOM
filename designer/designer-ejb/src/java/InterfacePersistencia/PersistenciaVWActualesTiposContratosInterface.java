/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.VWActualesTiposContratos;
import java.math.BigInteger;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la vista 'VWActualesTiposContratos' 
 * de la base de datos.
 * @author betelgeuse
 */
public interface PersistenciaVWActualesTiposContratosInterface {
    /**
     * Método encargado de buscar el Tipo Contrato actual de un empleado, para esto se realiza la consulta
     * sobre la vista VWActualesTiposContratos.
     * El término actual está dado por la fechaHasta de las liquidaciones.
     * @param secuencia Secuencia del empleado.
     * @return Retorna una VWActualesTiposContratos con la información del Tipo Contrato actual de un empleado.
     */
    public VWActualesTiposContratos buscarTiposContratosEmpleado(EntityManager em, BigInteger secuencia);

}
