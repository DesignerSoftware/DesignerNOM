/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.FormulasConceptos;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;
/**
 * Interface encargada de determinar las operaciones que se realizan sobre la tabla 'FormulasConceptos' 
 * de la base de datos.
 * @author betelgeuse
 */
public interface PersistenciaFormulasConceptosInterface {
    /**
     * Método encargado de insertar una FormulaConcepto en la base de datos.
     * @param conceptos FormulaConcepto que se quiere crear.
     */
    public void crear(EntityManager em,FormulasConceptos conceptos);
    /**
     * Método encargado de modificar una FormulaConcepto de la base de datos.
     * Este método recibe la información del parámetro para hacer un 'merge' con la 
     * información de la base de datos.
     * @param conceptos FormulaConcepto con los cambios que se van a realizar.
     */
    public void editar(EntityManager em,FormulasConceptos conceptos);
    /**
     * Método encargado de eliminar de la base de datos la FormulaConcepto que entra por parámetro.
     * @param conceptos FormulaConcepto que se quiere eliminar.
     */
    public void borrar(EntityManager em,FormulasConceptos conceptos);
    /**
     * Método encargado de buscar todas las FormulasConceptos existentes en la base de datos.
     * @return Retorna una lista de FormulasConceptos.
     */
    public List<FormulasConceptos> buscarFormulasConceptos(EntityManager em);
    /**
     * Método encargado de verificar si existe al menos una FormulaConcepto asociada a un concepto especifico.
     * @param secConcepto Secuencia del Concepto al que se le realiza la verificación.
     * @return Retorna true si hay una o más formulas asociadas al concepto cuya secuencia es igual a la que entra como parámetro.
     */
    public boolean verificarExistenciaConceptoFormulasConcepto(EntityManager em,BigInteger secConcepto);
    /**
     * Método encargado de buscar todas las FormulasConceptos de un concepto especifico.
     * @param secConcepto Secuencia del concepto asociado a las FormulasConceptos.
     * @return Retorna una lista de todas las FormulasConceptos de un concepto.
     */
    public List<FormulasConceptos> formulasConcepto(EntityManager em,BigInteger secConcepto); 
    /**
     * Método encargado de verificar que una FormulaConcepto esté asociada con un concepto al momento de hace el cargue de archivos.
     * @param secConcepto Secuencia del concepto 
     * @param secFormula Secuencia de la FormulaConcepto
     * @return Retorna true si existe una relación entre la fórmula y el concepto
     */
    public boolean verificarFormulaCargue_Concepto(EntityManager em,BigInteger secConcepto, BigInteger secFormula);
    /**
     * Método encargado de contar la cantidad de formulasConceptos, con un concepto específico.
     * @param secConcepto Secuencia del concepto.
     * @return Retorna el numero de formulasConceptos cuyo concepto tiene como secuencia la dada por parámetro.
     */
    public Long comportamientoConceptoAutomaticoSecuenciaConcepto(EntityManager em,BigInteger secConcepto);
    /**
     * Método encargado de contar la cantidad formulasConceptos, con un concepto específico, y que tienen asociada
     * una formulaNovedad.
     * @param secConcepto Secuencia del concepto.
     * @return Retorna el numero de formulasConceptos cuyo concepto tiene como secuencia el valor dado por parámetro y 
     * además están asociadas con alguna formulaNovedad.
     */
    public Long comportamientoConceptoSemiAutomaticoSecuenciaConcepto(EntityManager em,BigInteger secConcepto);
    /**
     * Método encargado de buscar las formulasConceptos, con una fórmula específica.
     * @param secFormula Secuencia de la fórmula.
     * @return Retorna una lista de FormulasConceptos cuya fórmula tiene como secuencia el valor del parámetro.
     */
    public List<FormulasConceptos> formulasConceptosParaFormulaSecuencia(EntityManager em,BigInteger secFormula);
}
