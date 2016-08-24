/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.Conexiones;
import java.math.BigInteger;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan para recuperar información de la conexión que esta
 * guardada en la base de datos. 
 * de la base de datos.
 * @author betelgeuse
 */
public interface PersistenciaConexionesInterface {
    /**
     * Método encargado de crear o modificar una Conexión.
     * Este método realiza un 'merge' basado en la secuencia de la conexión de manera que si esta no existe se crea y si 
     * existe se modifica. 
     * @param conexion Conexión a crear o modificar. 
     * @param em EntityManager encargado de realizar la conexión a la base de datos.
     */
    public void crear_Modificar(Conexiones conexion, EntityManager em);
    /**
     * Método encargado de buscar el SID(Sistem ID) asignado al usuario que esta usando el aplicativo.
     * El SID se le asigna al usuario que realiza la operacion de login.
     * @param em EntityManager encargado de realizar la conexión a la base de datos.
     * @return Retorna el SID del usuario que esta usando el aplicativo.
     */
    public BigInteger buscarSID(EntityManager em);
    /**
     * Método encargado de verificar si una Conexión tiene asignado un código SID. En caso de no tenerla, se le asigna
     * el SID del usuario que esta usando el aplicativo actualmente. <br>
     * El SID no debe cambiar, si la conexión no tiene secuencia la base de datos la asignara una de lo contrario se 
     * realiza un merge donde a la conexión indicada es modificada.
     * @param em EntityManager encargado de realizar la conexión a la base de datos.
     * @param conexion Conexión a la cual se la va a verificar el SID.
     */
    public void verificarSID(EntityManager em, Conexiones conexion);
}
