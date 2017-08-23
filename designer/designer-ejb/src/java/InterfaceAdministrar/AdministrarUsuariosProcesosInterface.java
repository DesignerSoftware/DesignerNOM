/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.UsuariosProcesos;
import java.util.List;

/**
 *
 * @author user
 */
public interface AdministrarUsuariosProcesosInterface {

    public void obtenerConexion(String idSesion);

    public List<UsuariosProcesos> consultarUsuariosProcesos();

    public void modificarUsuariosProcesos(List<UsuariosProcesos> listaUsuarios);

    public void borrarUsuariosProcesos(List<UsuariosProcesos> listaUsuarios);

    public void crearUsuariosProcesos(List<UsuariosProcesos> listaUsuarios);
}
