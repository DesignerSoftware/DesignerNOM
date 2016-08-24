/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.Perfiles;
import java.math.BigInteger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * Interface encargada de determinar las operaciones que se realizan para la conexión inicial al aplicativo. 
 * de la base de datos.
 * @author betelgeuse
 */
public interface PersistenciaConexionInicialInterface {
    /**
     * Método encargado de validar si el usuario existe y esta activo.
     * El que este activo No hace referencia al estado de la sesión
     * @param eManager EntityManager encargado de realizar la conexión a la base de datos.
     * @param usuario Alias del usuario que desea conectarse.
     * @return Retorna True si el usuario existe y esta activo.
     */
    public boolean validarUsuario(EntityManager eManager, String usuario);
    /**
     * Método encargado de crear el EntityManager para comunicarse con la base de datos.
     * @param emf EntityManagerFactory que contiene la propiedades de conexión a la base de datos.
     * @return Retorna el EntityManager conectado a la base de datos.
     */
    public EntityManager validarConexionUsuario(EntityManagerFactory emf);
    /**
     * Método encargado de recuperar la informacion del Rol y contraseña del Rol de un perfil especifico.
     * @param eManager EntityManager encargado las operaciones en la base de datos.
     * @param secPerfil Secuencia del perfil al que el usuario esta asociado.
     * @return Retorna el perfil al que el usuario esta asociado.
     */
    public Perfiles perfilUsuario(EntityManager eManager, BigInteger secPerfil);
    /**
     * Método encargado de recuperar la Secuencia del perfil al que el usuario esta asociado.
     * @param eManager EntityManager encargado las operaciones en la base de datos.
     * @param usuarioBD Alias del usuario que va a hacer login
     * @return Retorna la secuencia del perfil al que el usuario esta asociado.
     */
    public BigInteger usuarioLogin(EntityManager eManager, String usuarioBD);
    /**
     * Método encargado de seleccionar el rol del usuario.
     * @param eManager EntityManager encargado las operaciones en la base de datos.
     * @param rol Rol del usuarios que esta conectándose. (información almacenada en la base de datos)
     * @param pwd Contraseña del Rol. (información almacenada en la base de datos)
     */
    public void setearUsuario(EntityManager eManager, String rol, String pwd);
    /**
     * Método encargado de abrir la conexión inicial para el login.
     * @param eManager EntityManager encargado las operaciones en la base de datos. 
     */
    public void accesoDefault(EntityManager eManager);
    /**
     * Método encargado de cambiar la contraseña de un usuario especifico.
     * @param eManager EntityManager encargado las operaciones en la base de datos.
     * @param usuario Alias del usuario al que se le va a cambiar la contraseña.
     * @param nuevaClave Nueva Contraseña para el ingreso al aplicativo.
     * @return Retorna el número de entidades modificadas o eliminadas, o el codigo del error encontrado.
     */
    public int cambiarClave(EntityManager eManager, String usuario, String nuevaClave);
}
