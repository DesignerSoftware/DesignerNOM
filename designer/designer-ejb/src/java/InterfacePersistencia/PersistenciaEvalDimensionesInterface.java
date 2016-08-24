/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.EvalDimensiones;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
@Local
public interface PersistenciaEvalDimensionesInterface {

    /**
     * Método encargado de insertar un EvalDimension en la base de datos.
     *
     * @param evalDimensiones EvalDimensiones que se quiere crear.
     */
    public void crear(EntityManager em,EvalDimensiones evalDimensiones);

    /**
     * Método encargado de modificar un EvalDimension de la base de datos. Este
     * método recibe la información del parámetro para hacer un 'merge' con la
     * información de la base de datos.
     *
     * @param evalDimensiones EvalDimensiones con los cambios que se van a
     * realizar.
     */
    public void editar(EntityManager em,EvalDimensiones evalDimensiones);

    /**
     * Método encargado de eliminar de la base de datos un EvalDiemsion que
     * entra por parámetro.
     *
     * @param evalDimensiones EvalDimensiones que se quiere eliminar.
     */
    public void borrar(EntityManager em,EvalDimensiones evalDimensiones);

    /**
     * Método encargado de buscar un EvalDimension con la secuencia dada por
     * parámetro.
     *
     * @param secEvalDimensiones secEvalDimensiones de un EvalDimension que se
     * quiere encontrar.
     * @return Retorna un EvalDimension identificado con la secuencia dada por
     * parámetro.
     */
    public EvalDimensiones buscarEvalDimension(EntityManager em,BigInteger secEvalDimensiones);

    /**
     * Método encargado de buscar todas las EvalDimensiones existentes en la
     * base de datos.
     *
     * @return Retorna una lista de EvalDimensiones.
     */
    public List<EvalDimensiones> buscarEvalDimensiones(EntityManager em);

    /**
     * Método encargado de revisar si existe una relacion entre una
     * EvalDimension específica y algúna EvalPlanillas. Además de la revisión,
     * cuenta cuantas relaciones existen.
     *
     * @param secEvalDimensiones secEvalDimensiones de la Eval Dimension.
     * @return Retorna el número de proyectos relacionados con una EvalPlanillas
     * cuya secEvalDimensiones coincide con el parámetro.
     */
    public BigInteger contradorEvalPlanillas(EntityManager em,BigInteger secEvalDimensiones);
}
