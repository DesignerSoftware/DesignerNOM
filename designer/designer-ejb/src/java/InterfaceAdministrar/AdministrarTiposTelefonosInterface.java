/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.TiposTelefonos;
import java.util.List;

/**
 *
 * @author user
 */
public interface AdministrarTiposTelefonosInterface {
    
    /**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
    
    public List<TiposTelefonos> tiposTelefonos();
    public void modificarTipoTelefono(List<TiposTelefonos> listaTiposTelefonosModificar);
    public void borrarTipoTelefono(TiposTelefonos tipoTelefono);
    public void crearTipoTelefono(TiposTelefonos tipoTelefono);
    
}
