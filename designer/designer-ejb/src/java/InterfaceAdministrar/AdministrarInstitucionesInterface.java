/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Instituciones;
import java.util.List;


public interface AdministrarInstitucionesInterface {
    	/**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
 
    public List<Instituciones> Instituciones();
    public List<Instituciones>  lovInstituciones();
    
}
