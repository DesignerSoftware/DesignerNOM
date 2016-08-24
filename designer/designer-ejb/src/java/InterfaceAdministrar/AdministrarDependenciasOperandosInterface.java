/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.DependenciasOperandos;
import Entidades.Operandos;
import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author user
 */
public interface AdministrarDependenciasOperandosInterface {

    /**
     * Método encargado de obtener el Entity Manager el cual tiene asociado la
     * sesion del usuario que utiliza el aplicativo.
     *
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);

    public List<DependenciasOperandos> buscarDependenciasOperandos(BigInteger secuenciaOperando);

    public void borrarDependenciasOperandos(DependenciasOperandos dependenciasOperandos);

    public void crearDependenciasOperandos(DependenciasOperandos dependenciasOperandos);

    public void modificarDependenciasOperandos(DependenciasOperandos dependenciasOperandos);

    public List<Operandos> buscarOperandos();

    public String nombreOperandos(int codigo);

}
