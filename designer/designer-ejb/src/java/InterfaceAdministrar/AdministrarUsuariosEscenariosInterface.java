/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Escenarios;
import Entidades.Usuarios;
import Entidades.UsuariosEscenarios;
import java.util.List;

/**
 *
 * @author user
 */
public interface AdministrarUsuariosEscenariosInterface {

    public void obtenerConexion(String idSesion);

    public List<UsuariosEscenarios> consultarUsuariosEscenarios();

    public String modificarUsuarioC(UsuariosEscenarios usuarioE);

    public String borrarUsuarioC(UsuariosEscenarios usuarioE);

    public String crearUsuarioC(UsuariosEscenarios usuarioE);
    
    public List<Escenarios> lovEscenarios();

    public List<Usuarios> listaUsuarios();
}
