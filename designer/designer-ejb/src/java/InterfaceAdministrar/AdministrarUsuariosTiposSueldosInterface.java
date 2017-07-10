/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.UsuariosTiposSueldos;
import java.util.List;

/**
 *
 * @author user
 */
public interface AdministrarUsuariosTiposSueldosInterface {

    public void obtenerConexion(String idSesion);

    public List<UsuariosTiposSueldos> consultarUsuariosTS();

    public void modificarUsuariosTS(List<UsuariosTiposSueldos> listaUsuarios);

    public void borrarUsuariosTS(List<UsuariosTiposSueldos> listaUsuarios);

    public void crearUsuariosTS(List<UsuariosTiposSueldos> listaUsuarios);

}
