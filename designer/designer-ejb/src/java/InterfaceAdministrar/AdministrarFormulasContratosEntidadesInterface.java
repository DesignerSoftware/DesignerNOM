/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.FormulasContratosEntidades;
import Entidades.Formulas;
import Entidades.Formulascontratos;
import Entidades.TiposEntidades;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author user
 */
@Local
public interface AdministrarFormulasContratosEntidadesInterface {

    /**
     * Método encargado de obtener el Entity Manager el cual tiene asociado la
     * sesion del usuario que utiliza el aplicativo.
     *
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);

    public void modificarFormulasContratosEntidades(List<FormulasContratosEntidades> listaFormulasContratosEntidades);

    public void borrarFormulasContratosEntidades(List<FormulasContratosEntidades> listaFormulasContratosEntidades);

    public void crearFormulasContratosEntidades(List<FormulasContratosEntidades> listaFormulasContratosEntidades);

    public List<FormulasContratosEntidades> consultarFormulasContratosEntidades();

    public List<FormulasContratosEntidades> consultarFormulasContratosEntidadesPorFormulaContrato(BigInteger secFormulaContrato);

    public List<TiposEntidades> consultarLOVTiposEntidades();

    public Formulascontratos consultarFormulaDeFormulasContratosEntidades(BigInteger secFormulaContrato);
}
