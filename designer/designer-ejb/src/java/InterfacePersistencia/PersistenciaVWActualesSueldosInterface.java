/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import java.math.BigDecimal;
import java.math.BigInteger;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la vista 'VWActualesSueldos' 
 * de la base de datos.
 * @author betelgeuse
 */
public interface PersistenciaVWActualesSueldosInterface {
    /**
     * Método encargado de buscar el sueldo activo y actual de un empleado, para esto se realiza la consulta
     * sobre la vista VWActualesSueldos y se suman los campos 'Valor' de un solo empleado.
     * El término actual está dado por la fechaHasta de las liquidaciones.
     * @param secuencia Secuencia del empleado.
     * @return Retorna el sueldo activo y actual de un empleado.
     */
    public BigDecimal buscarSueldoActivo(EntityManager em, BigInteger secuencia);
}
