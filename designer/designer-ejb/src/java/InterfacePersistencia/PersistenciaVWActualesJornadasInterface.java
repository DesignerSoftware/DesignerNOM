/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.VWActualesJornadas;
import java.math.BigInteger;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la vista 'VWActualesJornadas' 
 * de la base de datos.
 * @author betelgeuse
 */
public interface PersistenciaVWActualesJornadasInterface {
    /**
     * Método encargado de buscar la Jornada actual de un empleado, para esto se realiza la consulta
     * sobre la vista VWActualesJornadas.
     * El término actual está dado por la fechaHasta de las liquidaciones.
     * @param secuencia Secuencia del empleado.
     * @return Retorna una VWActualesJornadas con la información de la Jornada actual de un empleado.
     */
    public VWActualesJornadas buscarJornada(EntityManager em, BigInteger secuencia);
}
