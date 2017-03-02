/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Ciudades;
import Entidades.Pantallas;
import Entidades.Perfiles;
import Entidades.Personas;
import Entidades.TiposDocumentos;
import Entidades.Usuarios;
import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author Administrador
 */
public interface AdministrarUsuariosInterface {

    public void obtenerConexion(String idSesion);

    public List<Usuarios> consultarUsuarios();

    public void crearUsuariosBD(String alias);

    public void CrearUsuarioPerfilBD(String alias, String perfil);

    public List<Personas> consultarPersonas();

    public List<Perfiles> consultarPerfiles();

    public List<Pantallas> consultarPantallas();

    public void modificarUsuarios(List<Usuarios> listaUsuarios);

    public void borrarUsuarios(List<Usuarios> listaUsuarios);

    public void crearUsuarios(List<Usuarios> listaUsuarios);

    public Integer eliminarUsuariosBD(String alias);

    public Integer eliminarUsuarioTotalBD(String alias);

    public void clonarUsuariosBD(BigInteger usuarioOrigen, BigInteger usuarioDestino);

    public void desbloquearUsuariosBD(String alias);

    public void restaurarUsuariosBD(String alias, String fecha);

    public List<Ciudades> lovCiudades();

    public List<TiposDocumentos> consultarTiposDocumentos();

    public void crearPersona(Personas persona);
}
