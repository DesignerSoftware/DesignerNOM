/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.Evalconvocatorias;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
public interface PersistenciaEvalConvocatoriasInterface {

    public void crear(EntityManager em, Evalconvocatorias evalconvocatoria);

    public void editar(EntityManager em, Evalconvocatorias evalconvocatoria);

    public void borrar(EntityManager em, Evalconvocatorias evalconvocatoria);

    /**
     * Método encargado de buscar el EvalEvaluador con la secEvalEvaluadores
     * dada por parámetro.
     *
     * @param secEvalEvaluadores Identificador único del EvalEvaluador que se
     * quiere encontrar.
     * @return Retorna el EvalEvaluador identificada con el secEvalEvaluadores
     * dada por parámetro.
     */
    public List<Evalconvocatorias> consultarEvalConvocatorias(EntityManager em, BigInteger secuenciaEmpleado);

    /**
     * Método encargado de buscar todos los EvalEvaluadores existentes,
     * ordenados por código, en la base de datos.
     *
     * @return Retorna una lista de EvalEvaluadores.
     */
    public List<Evalconvocatorias> consultarEvalConvocatorias(EntityManager em);
}
