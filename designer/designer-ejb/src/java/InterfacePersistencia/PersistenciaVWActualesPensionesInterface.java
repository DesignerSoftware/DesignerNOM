/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import java.math.BigDecimal;
import java.math.BigInteger;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la vista 'VWActualesPensiones' 
 * de la base de datos.
 * @author betelgeuse
 */
public interface PersistenciaVWActualesPensionesInterface {
    /**
     * Método encargado de buscar el valor de la pensión actual de un empleado, para esto se realiza la consulta
     * sobre la vista VWActualesPensiones en la columna 'Valor'.
     * El término actual está dado por la fechaHasta de las liquidaciones.
     * @param secuencia Secuencia del empleado.
     * @return Retorna el valor de la pensión actual de un empleado.
     */
    public BigDecimal buscarSueldoPensionado(EntityManager em, BigInteger secuencia);
}
