/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.ColumnasEscenarios;
import java.util.List;

/**
 *
 * @author AndresPineda
 */
public interface AdministrarConfigurarColumnasInterface {

    public List<ColumnasEscenarios> listaColumnasEscenarios();

    /**
     * Método encargado de obtener el Entity Manager el cual tiene asociado la
     * sesion del usuario que utiliza el aplicativo.
     *
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);

}
