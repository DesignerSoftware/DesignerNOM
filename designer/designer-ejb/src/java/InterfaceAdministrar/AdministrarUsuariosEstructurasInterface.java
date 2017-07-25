/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Empresas;
import Entidades.Estructuras;
import Entidades.Usuarios;
import Entidades.UsuariosEstructuras;
import Entidades.UsuariosVistas;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author user
 */
public interface AdministrarUsuariosEstructurasInterface {

    public void obtenerConexion(String idSesion);

    public List<UsuariosEstructuras> consultarUsuariosEstructuras(BigInteger secUsuario);

    public void crearUsuarioEstructura(List<UsuariosEstructuras> listCrear);

    public void modificarUsuarioEstructura(List<UsuariosEstructuras> listModificar);

    public void borrarUsuarioEstructura(List<UsuariosEstructuras> listBorrar);

    public List<Usuarios> lovUsuarios();

    public List<Estructuras> lovEstructuras();

    public List<Empresas> lovEmpresas();

    public List<UsuariosVistas> listaUsuariosVistas();

    public void crearUsuarioVista(List<UsuariosVistas> listCrear);

    public void modificarUsuarioVista(List<UsuariosVistas> listModificar);

    public void borrarUsuarioVista(List<UsuariosVistas> listBorrar);

    public BigDecimal contarUsuariosEstructuras(BigInteger secUsuario);

    public void crearVistaUsuarioEstructura(BigInteger secUsuarioEstructura, BigInteger secUsuario);
}
