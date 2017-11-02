/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.UsuariosContratos;
import java.util.List;

/**
 *
 * @author user
 */
public interface AdministrarUsuariosContratosInterface {

    public void obtenerConexion(String idSesion);

    public List<UsuariosContratos> consultarUsuariosC();

    public String modificarUsuarioC(UsuariosContratos usuarioC);

    public String borrarUsuarioC(UsuariosContratos usuarioC);

    public String crearUsuarioC(UsuariosContratos usuarioC);

}
