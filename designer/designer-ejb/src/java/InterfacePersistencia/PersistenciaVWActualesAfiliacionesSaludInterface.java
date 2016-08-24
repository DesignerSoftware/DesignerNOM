/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.VWActualesAfiliacionesSalud;
import java.math.BigInteger;
import javax.persistence.EntityManager;
/**
 * Interface encargada de determinar las operaciones que se realizan sobre la vista 'VWActualesAfiliacionesSalud' 
 * de la base de datos.
 * @author betelgeuse
 */
public interface PersistenciaVWActualesAfiliacionesSaludInterface {
    /**
     * Método encargado de buscar la afiliación a salud actual de un empleado, para esto se realiza la consulta
     * sobre la vista VWActualesAfiliacionesSalud.
     * El término "actual" está dado por la 'fechaHasta' de las liquidaciones.
     * @param secuencia Secuencia del empleado.
     * @return Retorna una VWActualesAfiliacionesSalud con la información de la afiliación a salud actual de un empleado.
     */
    public VWActualesAfiliacionesSalud buscarAfiliacionSalud(EntityManager em, BigInteger secuencia);
}
