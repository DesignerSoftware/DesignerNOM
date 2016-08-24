/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.VWActualesUbicaciones;
import java.math.BigInteger;
import javax.persistence.EntityManager;
/**
 * Interface encargada de determinar las operaciones que se realizan sobre la vista 'VWActualesUbicaciones' 
 * de la base de datos.
 * @author betelgeuse
 */
public interface PersistenciaVWActualesUbicacionesInterface {
    /**
     * Método encargado de buscar la Ubicación actual de un empleado, para esto se realiza la consulta
     * sobre la vista VWActualesUbicaciones.
     * El término actual está dado por la fechaHasta de las liquidaciones.
     * @param secuencia Secuencia del empleado.
     * @return Retorna una VWActualesUbicaciones con la información de la Ubicación actual de un empleado.
     */
    public VWActualesUbicaciones buscarUbicacion(EntityManager em, BigInteger secuencia);
}
