/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Roles;
import Entidades.Tablas;
import java.util.List;

/**
 *
 * @author user
 */
public interface AdministrarRolInterface {

    public void obtenerConexion(String idSesion);

    public String modificarRol(Roles rol);

    public String crearRol(Roles rol);

    public String borrarRol(Roles rol);

    public List<Tablas> consultarTablas();
    
    public List<Roles> consultarRol();
}
