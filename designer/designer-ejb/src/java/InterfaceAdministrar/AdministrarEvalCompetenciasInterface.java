/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.EvalCompetencias;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author user
 */
@Local
public interface AdministrarEvalCompetenciasInterface {

    /**
     * Método encargado de obtener el Entity Manager el cual tiene asociado la
     * sesion del usuario que utiliza el aplicativo.
     *
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);

    /**
     * Método encargado de modificar EvalCompetencias.
     *
     * @param listEvalCompetencias Lista de las EvalCompetencias que se van a
     * modificar.
     */
    public void modificarEvalCompetencias(List<EvalCompetencias> listEvalCompetencias);

    /**
     * Método encargado de modificar EvalCompetencias.
     *
     * @param listEvalCompetencias Lista de las EvalCompetencias que se van a
     * modificar.
     */
    public void borrarEvalCompetencias(List<EvalCompetencias> listEvalCompetencias);

    /**
     * Método encargado de crear EvalCompetencias.
     *
     * @param listEvalCompetencias Lista de las EvalCompetencias que se van a
     * crear.
     */
    public void crearEvalCompetencias(List<EvalCompetencias> listEvalCompetencias);

    /**
     * Método encargado de recuperar una EvalCompetencia dada su secuencia.
     *
     * @param secEvalCompetencias Secuencia de la EvalCompetencias.
     * @return Retorna un EvalCompetencias cuya secuencia coincida con el valor
     * del parámetro.
     */
    public EvalCompetencias consultarEvalCompetencia(BigInteger secEvalCompetencias);

    /**
     * Método encargado de recuperar todos las EvalCompetencias.
     *
     * @return Retorna una lista de EvalCompetencias.
     */
    public List<EvalCompetencias> consultarEvalCompetencias();

    /**
     * Método encargado de validar si existe una relación entre un
     * EvalCompetencia específica y algún CompetenciaCargo. Adémas de la
     * revisión, cuenta cuantas relaciones existen.
     *
     * @param secEvalCompetencias Secuencia de una EvalCompetencia.
     * @return Retorna el número de CompetenciaCargo relacionados con una
     * Enfermedad cuya secuencia coincide con el parámetro.
     */
    public BigInteger verificarCompetenciasCargos(BigInteger secEvalCompetencias);
}
