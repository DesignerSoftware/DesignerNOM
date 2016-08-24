/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Formulas;
import Entidades.FormulasProcesos;
import Entidades.Procesos;
import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author PROYECTO01
 */
public interface AdministrarFormulaProcesoInterface {

    public List<FormulasProcesos> listFormulasProcesosParaFormula(BigInteger secuencia);

    public void crearFormulasProcesos(List<FormulasProcesos> listFN);

    public void editarFormulasProcesos(List<FormulasProcesos> listFN);

    public void borrarFormulasProcesos(List<FormulasProcesos> listFN);

    public List<Procesos> listProcesos(BigInteger secuencia);

    public Formulas formulaActual(BigInteger secuencia);

    /**
     * Método encargado de obtener el Entity Manager el cual tiene asociado la
     * sesion del usuario que utiliza el aplicativo.
     *
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);

}
