/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Inforeportes;
import Entidades.Usuarios;
import Entidades.UsuariosInforeportes;
import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author user
 */
public interface AdministrarUsuariosInfoReportesInterface {

    public void obtenerConexion(String idSesion);
    
    public void crear(List<UsuariosInforeportes> lista);

    public void editar(List<UsuariosInforeportes> lista);

    public void borrar(List<UsuariosInforeportes> lista);

    public List<UsuariosInforeportes> listaUsuariosIR(BigInteger secUsuario);

    public List<Inforeportes> lovIR();

    public List<Usuarios> listaUsuarios();

}
