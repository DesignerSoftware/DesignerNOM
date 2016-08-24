/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.EvalEvaluadores;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author user
 */
@Local
public interface AdministrarEvalEvaluadoresInterface {

    /**
     * Método encargado de obtener el Entity Manager el cual tiene asociado la
     * sesion del usuario que utiliza el aplicativo.
     *
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);

    /**
     * Método encargado de modificar EvalEvaluadores.
     *
     * @param listaEvalEvaluadores Lista EvalEvaluadores que se van a modificar.
     */
    public void modificarEvalEvaluadores(List<EvalEvaluadores> listaEvalEvaluadores);

    /**
     * Método encargado de borrar EvalEvaluadores.
     *
     * @param listaEvalEvaluadores Lista EvalEvaluadores que se van a borrar.
     */
    public void borrarEvalEvaluadores(List<EvalEvaluadores> listaEvalEvaluadores);

    /**
     * Método encargado de crear EvalEvaluadores.
     *
     * @param listaEvalEvaluadores Lista EvalEvaluadores que se van a crear.
     */
    public void crearEvalEvaluadores(List<EvalEvaluadores> listaEvalEvaluadores);

    /**
     * Método encargado de recuperar un EvalEvaluador dada su secuencia.
     *
     * @param secEvalEvaluadores Secuencia del EvalEvaluador.
     * @return Retorna una EvalEvaluador cuya secuencia coincida con el valor
     * del parámetro.
     */
    public EvalEvaluadores consultarEvalEvaluador(BigInteger secEvalEvaluadores);

    /**
     * Método encargado de recuperar todos las EvalEvaluadores.
     *
     * @return Retorna una lista de EvalEvaluadores.
     */
    public List<EvalEvaluadores> consultarEvalEvaluadores();

    /**
     * Método encargado de validar si existe una relación entre un
     * EvalEvaluadores específica y algún EvalPruebas. Adémas de la revisión,
     * cuenta cuantas relaciones existen.
     *
     * @param secEvalEvaluadores Secuencia de una EvalEvaluadores.
     * @return Retorna el número de EvalPruebas relacionados con una Enfermedad
     * cuya secuencia coincide con el parámetro.
     */
    public BigInteger verificarEvalPruebas(BigInteger secEvalEvaluadores);
}
