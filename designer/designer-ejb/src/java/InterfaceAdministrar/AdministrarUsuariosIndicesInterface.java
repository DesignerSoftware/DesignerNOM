/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Indices;
import Entidades.Usuarios;
import Entidades.UsuariosIndices;
import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author user
 */
public interface AdministrarUsuariosIndicesInterface {

    public void obtenerConexion(String idSesion);

    public String crear(UsuariosIndices usuarioIndice);

    public String editar(UsuariosIndices usuarioIndice);

    public String borrar(UsuariosIndices usuarioIndice);

    public List<Indices> lovIR();

    public List<Usuarios> listaUsuarios();

    public List<UsuariosIndices> listaUsuariosIndices(BigInteger secUsuario);

}
