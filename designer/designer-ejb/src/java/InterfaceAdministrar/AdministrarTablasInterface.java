/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Modulos;
import Entidades.Tablas;
import java.util.List;

/**
 *
 * @author user
 */
public interface AdministrarTablasInterface {
    public void obtenerConexion(String idSesion);

    public String modificarTablas(Tablas tabla);

    public String crearTablas(Tablas tabla);

    public String borrarTablas(Tablas tabla);

    public List<Modulos> consultarModulos(); 
    
    public List<Tablas> consultarTablas(); 
}
