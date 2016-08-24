/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Formulas;
import Entidades.FormulasNovedades;
import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author PROYECTO01
 */
public interface AdministrarFormulaNovedadInterface {

    /**
     * Método encargado de obtener el Entity Manager el cual tiene asociado la
     * sesion del usuario que utiliza el aplicativo.
     *
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);

    public List<FormulasNovedades> listFormulasNovedadesParaFormula(BigInteger secuencia);

    public void crearFormulasNovedades(List<FormulasNovedades> listFN);

    public void editarFormulasNovedades(List<FormulasNovedades> listFN);

    public void borrarFormulasNovedades(List<FormulasNovedades> listFN);

    public List<Formulas> listFormulas(BigInteger secuencia);

    public Formulas formulaActual(BigInteger secuencia);

}
