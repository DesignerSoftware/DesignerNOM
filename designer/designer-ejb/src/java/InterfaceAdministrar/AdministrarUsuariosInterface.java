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

    public String crearUsuariosBD(String alias);

    public String CrearUsuarioPerfilBD(String alias, String perfil);

    public List<Personas> consultarPersonas();

    public List<Perfiles> consultarPerfiles();

    public List<Pantallas> consultarPantallas();

    public String modificarUsuarios(Usuarios Usuario);

    public String borrarUsuarios(Usuarios Usuario);

    public String crearUsuarios(Usuarios Usuario);

    public String eliminarUsuariosBD(String alias);

    public String eliminarUsuarioTotalBD(String alias);

    public String clonarUsuariosBD(BigInteger usuarioOrigen, BigInteger usuarioDestino);

    public String desbloquearUsuariosBD(String alias);

    public String restaurarUsuariosBD(String alias, String fecha);

    public List<Ciudades> lovCiudades();

    public List<TiposDocumentos> consultarTiposDocumentos();

    public void crearPersona(Personas persona);
}
