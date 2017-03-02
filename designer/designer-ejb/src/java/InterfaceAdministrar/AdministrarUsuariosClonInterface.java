
package InterfaceAdministrar;

import Entidades.Pantallas;
import Entidades.Perfiles;
import Entidades.Personas;
import Entidades.Usuarios;
import java.math.BigInteger;
import java.util.List;

public interface AdministrarUsuariosClonInterface {
    
    public void obtenerConexion(String idSesion);
    public List<Usuarios> consultarUsuarios();
    public void crearUsuariosBD(String alias, String perfil);
    public List<Personas> consultarPersonas();
    public List<Perfiles> consultarPerfiles();
    public List<Pantallas> consultarPantallas();
    public void modificarUsuarios(List<Usuarios> listaUsuarios);
    public void borrarUsuarios(List<Usuarios> listaUsuarios);
    public void crearUsuarios(List<Usuarios> listaUsuarios);
    public void eliminarUsuariosBD(String alias);
    public void clonarUsuariosBD(BigInteger usuarioOrigen, BigInteger usuarioDestino);
    public void desbloquearUsuariosBD(String alias);
    public void restaurarUsuariosBD(String alias, String fecha);
    
}
