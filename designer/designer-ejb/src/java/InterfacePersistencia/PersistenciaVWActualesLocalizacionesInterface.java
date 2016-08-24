/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.VWActualesLocalizaciones;
import java.math.BigInteger;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la vista 'VWActualesLocalizaciones' 
 * de la base de datos.
 * @author betelgeuse
 */
public interface PersistenciaVWActualesLocalizacionesInterface {
    /**
     * Método encargado de buscar la localización actual de un empleado,
     * para esto se realiza la consulta sobre la vista VWActualesLocalizaciones. 
     * El término actual está dado por la fechaHasta de las liquidaciones.
     * @param secuencia Secuencia del empleado.
     * @return Retorna una VWActualesLocalizaciones con la información de la localización actual de un empleado.
     */
    public VWActualesLocalizaciones buscarLocalizacion(EntityManager em, BigInteger secuencia);
}
