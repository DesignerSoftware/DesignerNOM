/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Contratos;
import Entidades.Formulas;
import Entidades.Formulascontratos;
import Entidades.Periodicidades;
import Entidades.Terceros;
import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author PROYECTO01
 */
public interface AdministrarFormulaContratoInterface {

    public List<Formulascontratos> listFormulasContratosParaFormula(BigInteger secuencia);

    public void crearFormulasContratos(List<Formulascontratos> listaFC);

    public void borrarFormulasContratos(List<Formulascontratos> listaFC);

    public void editarFormulasContratos(List<Formulascontratos> listaFC);

    public List<Contratos> listContratos();

    public List<Periodicidades> listPeriodicidades();

    public List<Terceros> listTerceros();

    public Formulas actualFormula(BigInteger secuencia);

    /**
     * Método encargado de obtener el Entity Manager el cual tiene asociado la
     * sesion del usuario que utiliza el aplicativo.
     *
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);

}
