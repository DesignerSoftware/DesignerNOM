/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Empresas;
import Entidades.Modulos;
import Entidades.Pantallas;
import Entidades.Tablas;
import java.util.List;

/**
 *
 * @author user
 */
public interface AdministrarPantallasInterface {

    public void obtenerConexion(String idSesion);

    public String modificarPantallas(Pantallas pantalla);

    public String crearPantallas(Pantallas pantalla);

    public String borrarPantallas(Pantallas pantalla);

    public List<Pantallas> consultarPantallas();

    public List<Empresas> consultarEmpresas();

    public List<Tablas> consultarTablas();
}
