/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.EvalDimensiones;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author user
 */
@Local
public interface AdministrarEvalDimensionesInterface {

    /**
     * Método encargado de obtener el Entity Manager el cual tiene asociado la
     * sesion del usuario que utiliza el aplicativo.
     *
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);

    /**
     * Método encargado de modificar EvalDimensiones.
     *
     * @param listaEvalDimensiones Lista EvalDimensiones que se van a modificar.
     */
    public void modificarEvalDimensiones(List<EvalDimensiones> listaEvalDimensiones);

    /**
     * Método encargado de borrar EvalDimensiones.
     *
     * @param listaEvalDimensiones Lista EvalDimensiones que se van a borrar.
     */
    public void borrarEvalDimensiones(List<EvalDimensiones> listaEvalDimensiones);

    /**
     * Método encargado de crear EvalDimensiones.
     *
     * @param listaEvalDimensiones Lista EvalDimensiones que se van a crear.
     */
    public void crearEvalDimensiones(List<EvalDimensiones> listaEvalDimensiones);

    /**
     * Método encargado de recuperar un EvalDimension dada su secuencia.
     *
     * @param secEvalDimensiones Secuencia del EvalDimension.
     * @return Retorna la EvalDimension cuya secuencia coincida con el valor del
     * parámetro.
     */
    public EvalDimensiones consultarEvalDimension(BigInteger secEvalDimensiones);

    /**
     * Metodo encargado de traer todas las EvalDimensiones de la base de datos.
     *
     * @return Lista de EvalDimensiones.
     */
    public List<EvalDimensiones> consultarEvalDimensiones();

    /**
     * Método encargado de contar la cantidad de EvalPlanillas relacionadas con
     * un EvalDimension específico.
     *
     * @param secEvalDimension Secuencia del EvalDimension.
     * @return Retorna un número indicando la cantidad de EvalPlanillas cuya
     * secuencia coincide con el valor del parámetro.
     */
    public BigInteger verificarEvalPlanillas(BigInteger secEvalDimension);
}
