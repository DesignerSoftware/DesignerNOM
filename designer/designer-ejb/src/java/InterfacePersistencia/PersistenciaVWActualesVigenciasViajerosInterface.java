/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.VWActualesVigenciasViajeros;
import java.math.BigInteger;
import javax.persistence.EntityManager;
/**
 * Interface encargada de determinar las operaciones que se realizan sobre la vista 'VWActualesVigenciasViajeros' 
 * de la base de datos.
 * @author betelgeuse
 */
public interface PersistenciaVWActualesVigenciasViajerosInterface {
   /**
     * Método encargado de buscar el Tipo de Viajero actual de un empleado, para esto se realiza la consulta
     * sobre la vista VWActualesVigenciasViajeros.
     * El término actual está dado por la fechaHasta de las liquidaciones.
     * @param secuencia Secuencia del empleado.
     * @return Retorna una VWActualesVigenciasViajeros con la información del Tipo de Viajero actual de un empleado.
     */
    public VWActualesVigenciasViajeros buscarTipoViajero(EntityManager em, BigInteger secuencia);
}
