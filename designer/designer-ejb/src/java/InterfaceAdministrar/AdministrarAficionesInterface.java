/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Aficiones;
import java.util.List;

/**
 *
 * @author user
 */
public interface AdministrarAficionesInterface {
    public void obtenerConexion(String idSesion);
    
    public void crearAficiones(List<Aficiones> listaCrear);
    
    public void modificarAficiones(List<Aficiones> listaModificar);
   
    public void borrarAficiones(List<Aficiones> listaBorrar);
    
    public List<Aficiones> consultarAficiones();    
}
