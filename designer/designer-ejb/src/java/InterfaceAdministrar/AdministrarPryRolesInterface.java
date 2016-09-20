/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.PryRoles;
import java.util.List;

/**
 *
 * @author user
 */
public interface AdministrarPryRolesInterface {
    	/**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
    public List<PryRoles> PryRoles();
    public List<PryRoles> lovPryRoles();
    public void modificarPryRoles(List<PryRoles> listaPryRoles);
    public void borrarPryRoles(List<PryRoles> listaPryRoles);
    public void crearPryRoles(List<PryRoles> listaPryRoles);
    
}
