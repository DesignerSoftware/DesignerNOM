/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.Formulascontratos;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la
 * tabla 'FormulasContratos' de la base de datos.
 *
 * @author Andres Pineda.
 */
public interface PersistenciaFormulasContratosInterface {

    /**
     * Método encargado de insertar una FormulaContrato en la base de datos.
     *
     * @param formulasContratos FormulaContrato que se quiere crear.
     */
    public void crear(EntityManager em,Formulascontratos formulasContratos);

    /**
     * Método encargado de modificar una FormulaContrato de la base de datos.
     * Este método recibe la información del parámetro para hacer un 'merge' con
     * la información de la base de datos.
     *
     * @param formulasContratos FormulaContrato con los cambios que se van a
     * realizar.
     */
    public void editar(EntityManager em,Formulascontratos formulasContratos);

    /**
     * Método encargado de eliminar de la base de datos la FormulaContrato que
     * entra por parámetro.
     *
     * @param formulasContratos FormulaContrato que se quiere eliminar.
     */
    public void borrar(EntityManager em,Formulascontratos formulasContratos);

    /**
     * Método encargado de obtener las FormulasContratos para una Formula
     * específica.
     *
     * @param secuencia Secuencia de la Formula.
     * @return Retorna una liste de Formulascontratos.
     */
    public List<Formulascontratos> formulasContratosParaFormulaSecuencia(EntityManager em,BigInteger secuencia);

    /**
     * Método encargado de obtener las FormulasContratos para un Contrato
     * específico.
     *
     * @param secuencia Secuencia del Contrato.
     * @return Retorna una liste de Formulascontratos.
     */
    public List<Formulascontratos> formulasContratosParaContratoSecuencia(EntityManager em,BigInteger secuencia);

    public Formulascontratos formulasContratosParaContratoFormulasContratosEntidades(EntityManager em,BigInteger secuencia);
}
