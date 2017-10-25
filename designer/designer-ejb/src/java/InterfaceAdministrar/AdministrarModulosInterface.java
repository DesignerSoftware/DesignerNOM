/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Modulos;
import java.util.List;

/**
 *
 * @author user
 */
public interface AdministrarModulosInterface {

    public void obtenerConexion(String idSesion);

    public String modificarModulos(Modulos modulo);

    public String crearModulos(Modulos modulo);

    public String borrarModulos(Modulos modulo);

    public List<Modulos> consultarModulos();
}
