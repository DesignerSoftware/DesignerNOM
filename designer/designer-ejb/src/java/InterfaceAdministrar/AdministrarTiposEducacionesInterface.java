/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.TiposEducaciones;
import java.util.List;

/**
 *
 * @author user
 */
public interface AdministrarTiposEducacionesInterface {
 
    /**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
    
    public List<TiposEducaciones>  lovTiposEducaciones();
    public List<TiposEducaciones> TiposEducaciones();
    
}
