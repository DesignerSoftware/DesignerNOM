/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.VWActualesReformasLaborales;
import java.math.BigInteger;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la vista 'VWActualesReformasLaborales' 
 * de la base de datos.
 * @author betelgeuse
 */
public interface PersistenciaVWActualesReformasLaboralesInterface {
    /**
     * Método encargado de buscar la Reforma Laboral actual de un empleado, para esto se realiza la consulta
     * sobre la vista VWActualesAfiliacionesPension.
     * El término actual está dado por la fechaHasta de las liquidaciones.
     * @param secuencia Secuencia del empleado.
     * @return Retorna una VWActualAfiliacionPension con la información de  la Reforma Laboral actual de un empleado.
     */
    public VWActualesReformasLaborales buscarReformaLaboral(EntityManager em, BigInteger secuencia);
}
