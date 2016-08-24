/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.DetallesFormulas;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la entidad 'DetallesFormulas',
 * la cual no es un mapeo de la base de datos sino una Entidad para albergar un resultado.
 * de la base de datos.
 * @author betelgeuse
 */
public interface PersistenciaDetallesFormulasInterface {
    /**
     * Método encargado de recibir la información de una formula comunicándose con una entidad virtual. 
     * @param secEmpleado Secuencia del empleado participante
     * @param fechaDesde Fecha inicial del rango
     * @param fechaHasta Fecha final del rango
     * @param secProceso Secuencia del proceso realizado
     * @param secHistoriaFormula Secuencia de la HistoriaFormula
     * @return Retorna una lista de DetallesFormulas
     */
    public List<DetallesFormulas> detallesFormula(EntityManager em,BigInteger secEmpleado, String fechaDesde, String fechaHasta, BigInteger secProceso, BigInteger secHistoriaFormula);
}
