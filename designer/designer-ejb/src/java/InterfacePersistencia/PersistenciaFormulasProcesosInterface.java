/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.FormulasProcesos;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la tabla 'FormulasProcesos' 
 * de la base de datos.
 * @author Andres Pineda.
 */
public interface PersistenciaFormulasProcesosInterface {
    /**
     * Método encargado de insertar una FormulaProceso en la base de datos.
     * @param formulasProcesos FormulaProceso que se quiere crear.
     */
    public void crear(EntityManager em,FormulasProcesos formulasProcesos);
    /**
     * Método encargado de modificar una FormulaProceso de la base de datos.
     * Este método recibe la información del parámetro para hacer un 'merge' con la 
     * información de la base de datos.
     * @param formulasProcesos FormulaProceso con los cambios que se van a realizar.
     */
    public void editar(EntityManager em,FormulasProcesos formulasProcesos);
    /**
     * Método encargado de eliminar de la base de datos la FormulaProceso que entra por parámetro.
     * @param formulasProcesos FormulaProceso que se quiere eliminar.
     */
    public void borrar(EntityManager em,FormulasProcesos formulasProcesos);
    /**
     * Método encargado de buscar las FormulasProcesos asociadas a una Fórmula específica.
     * @param secuencia Secuencia de la fórmula.
     * @return Retorna una lista de FormulasProcesos cuya fórmula tiene como secuencia el valor dado por parámetro.
     */
    public List<FormulasProcesos> formulasProcesosParaFormulaSecuencia(EntityManager em,BigInteger secuencia);
    /**
     * Método encargado de buscar las FormulasProcesos asociadas a una Fórmula específica.
     * @param secuencia Secuencia del proceso.
     * @return Retorna una lista de FormulasProcesos cuya proceso tiene como secuencia el valor dado por parámetro.
     */
     public List<FormulasProcesos> formulasProcesosParaProcesoSecuencia(EntityManager em,BigInteger secuencia);
}
