/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.VWAcumulados;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la
 * vista 'VWAcumulados' de la base de datos.
 *
 * @author betelgeuse
 */
@Local
public interface PersistenciaVWAcumuladosInterface {

    /**
     * Método encargado de buscar los acumulados de un empleado, para esto se
     * realiza la consulta sobre la vista VWAcumulados. El término actual está
     * dado por la fechaHasta de las liquidaciones.
     *
     * @param secuencia Secuencia del empleado.
     * @return Retorna una lista de VWAcumulados con la información de los
     * Acumulados de un empleado.
     */
    public List<VWAcumulados> buscarAcumuladosPorEmpleado(EntityManager em, BigInteger secuencia);
}
