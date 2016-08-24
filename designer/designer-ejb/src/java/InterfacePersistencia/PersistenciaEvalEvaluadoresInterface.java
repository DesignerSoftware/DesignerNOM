/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.EvalEvaluadores;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la tabla 'EvalEvaluadores' 
 * de la base de datos.
 * @author betelgeuse
 */
@Local
public interface PersistenciaEvalEvaluadoresInterface {
    /**
     * Método encargado de insertar un EvalEvaluador en la base de datos.
     * @param evalEvaluadores EvalEvaluador que se quiere crear.
     */
    public void crear(EntityManager em,EvalEvaluadores evalEvaluadores);
    /**
     * Método encargado de modificar un EvalEvaluador de la base de datos.
     * Este método recibe la información del parámetro para hacer un 'merge' con la 
     * información de la base de datos.
     * @param evalEvaluadores EvalEvaluador con los cambios que se van a realizar.
     */
    public void editar(EntityManager em,EvalEvaluadores evalEvaluadores);
    /**
     * Método encargado de eliminar de la base de datos el EvalEvaluador que entra por parámetro.
     * @param evalEvaluadores EvalEvaluador que se quiere eliminar.
     */
    public void borrar(EntityManager em,EvalEvaluadores evalEvaluadores);
    /**
     * Método encargado de buscar el EvalEvaluador con la secEvalEvaluadores dada por parámetro.
     * @param secEvalEvaluadores Identificador único del EvalEvaluador que se quiere encontrar.
     * @return Retorna el EvalEvaluador identificada con el secEvalEvaluadores dada por parámetro.
     */
    public EvalEvaluadores buscarEvalEvaluador(EntityManager em,BigInteger secEvalEvaluadores);
    /**
     * Método encargado de buscar todos los EvalEvaluadores existentes, ordenados por código, en la base de datos.
     * @return Retorna una lista de EvalEvaluadores.
     */
    public List<EvalEvaluadores> buscarEvalEvaluadores(EntityManager em);
    /**
     * Método encargado de contar las relaciones de un EvalEvaluador específico con una EvalPrueba.
     * @param secEvalEvaluadores Secuencia del EvalEvaluador
     * @return Retorna el número de EvalPruebas cuyo EvalEvaluador tiene como secEvalEvaluadores el valor dado por parámetro.
     */
    public BigInteger verificarBorradoEvalPruebas(EntityManager em,BigInteger secEvalEvaluadores);
}
