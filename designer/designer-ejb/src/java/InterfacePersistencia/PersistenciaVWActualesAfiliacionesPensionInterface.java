/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.VWActualesAfiliacionesPension;
import java.math.BigInteger;
import javax.persistence.EntityManager;
/**
 * Interface encargada de determinar las operaciones que se realizan sobre la vista 'VWActualesAfiliacionesPension' 
 * de la base de datos.
 * @author betelgeuse
 */
public interface PersistenciaVWActualesAfiliacionesPensionInterface {
    /**
     * Método encargado de buscar la afiliación a pensión actual de un empleado, para esto se realiza la consulta
     * sobre la vista VWActualesAfiliacionesPension.
     * El término actual está dado por la fechaHasta de las liquidaciones.
     * @param secuencia Secuencia del empleado.
     * @return Retorna una VWActualAfiliacionPension con la información de la afiliación a pensión actual de un empleado.
     */
    public VWActualesAfiliacionesPension buscarAfiliacionPension(EntityManager em, BigInteger secuencia);
}
