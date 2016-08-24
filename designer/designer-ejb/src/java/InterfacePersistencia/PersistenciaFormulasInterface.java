/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.Formulas;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la tabla 'Formulas' 
 * de la base de datos.
 * @author betelgeuse
 */
public interface PersistenciaFormulasInterface {
    /**
     * Método encargado de insertar una Formula en la base de datos.
     * @param formulas Formula que se quiere crear.
     */
    public void crear(EntityManager em,Formulas formulas);
    /**
     * Método encargado de modificar una Formula de la base de datos.
     * Este método recibe la información del parámetro para hacer un 'merge' con la 
     * información de la base de datos.
     * @param formulas Formula con los cambios que se van a realizar.
     */
    public void editar(EntityManager em,Formulas formulas);
    /**
     * Método encargado de eliminar de la base de datos la Formula que entra por parámetro.
     * @param formulas Formula que se quiere eliminar.
     */
    public void borrar(EntityManager em,Formulas formulas);
    /**
     * Método encargado de buscar la Formula con la secuencia dada por parámetro.
     * @param secuencia Identificador único de la Formula que se quiere encontrar.
     * @return Retorna la Formula identificada con la secuencia dada por parámetro.
     */
    public Formulas buscarFormula(EntityManager em,BigInteger secuencia);
    /**
     * Método encargado de buscar todas las Formulas existentes en la base de datos.
     * @return Retorna una lista de Formulas.
     */
    public List<Formulas> buscarFormulas(EntityManager em);
    /**
     * Método encargado de recuperar de la base de datos las formulas habilitadas para hacer cargue 
     * a la hora de subir un archivo plano.
     * @return Retorna una lista de Formulas.
     */
    public List<Formulas> buscarFormulasCarge(EntityManager em);
    /**
     * Método encargado de traer de la base de datos la Formula por default para cargar un archivo plano.
     * @return Retorna la Formula por default 'LIQNOV'.
     */
    public Formulas buscarFormulaCargeInicial(EntityManager em);
    /**
     * Método encargado de traer todas las formulas registradas en la base de datos ordenadas por el nombre. 
     * @return Retorna una lista de Formulas ordenadas por nombre.
     */
    public List<Formulas> lovFormulas(EntityManager em);
    /**
     * Método encargado de clonar una fórmula existente.
     * @param nombreCortoOrigen Nombre corto de la fórmula que se va a clonar.
     * @param nombreCortoClon Nombre corto de la nueva fórmula.
     * @param nombreLargoClon Nombre largo de la nueva fórmula.
     * @param observacionClon Descripción corta de la fórmula.
     */
    public void clonarFormulas(EntityManager em,String nombreCortoOrigen, String nombreCortoClon, String nombreLargoClon, String observacionClon);
    /**
     * Método encargado de volver una fórmula un operando, de esta forma se puede usar en otras fórmulas.
     * @param secFormula Secuencia de la formula que van a convertir en operando.
     */
    public void operandoFormulas(EntityManager em,BigInteger secFormula);
}
