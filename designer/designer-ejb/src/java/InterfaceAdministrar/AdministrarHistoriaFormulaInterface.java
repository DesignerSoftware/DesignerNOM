/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.EstructurasFormulas;
import Entidades.Formulas;
import Entidades.Historiasformulas;
import Entidades.Nodos;
import Entidades.Operadores;
import Entidades.Operandos;
import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author PROYECTO01
 */
public interface AdministrarHistoriaFormulaInterface {
    	/**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);

    public List<Historiasformulas> listHistoriasFormulasParaFormula(BigInteger secuencia);

    public void crearHistoriasFormulas(List<Historiasformulas> lista);

    public void editarHistoriasFormulas(List<Historiasformulas> lista);

    public void borrarHistoriasFormulas(List<Historiasformulas> lista);

    public List<Nodos> listNodosDeHistoriaFormula(BigInteger secuencia);

    public void crearNodos(List<Nodos> lista);

    public void borrarNodos(List<Nodos> lista);

    public void editarNodos(List<Nodos> lista);

    public Formulas actualFormula(BigInteger secuencia);

    public List<Operadores> listOperadores();

    public List<Operandos> listOperandos();

    public List<EstructurasFormulas> listEstructurasFormulasParaHistoriaFormula(BigInteger secuencia);

}
