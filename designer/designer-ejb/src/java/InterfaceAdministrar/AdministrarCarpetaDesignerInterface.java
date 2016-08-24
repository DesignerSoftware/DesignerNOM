/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfaceAdministrar;

import Entidades.Aficiones;
import Entidades.Modulos;
import Entidades.Pantallas;
import Entidades.Tablas;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;

/**
 * Interface encargada de determinar las operaciones lógicas necesarias para la pantalla 'CarpetaDesigner'.
 * @author Felipe Triviño.
 */
public interface AdministrarCarpetaDesignerInterface {
    /**
     * Método encargado de recuperar todos los Modulos para la tabla 'Modulos' de la pantalla.
     * @return Retorna una lista de Modulos.
     */
    public List<Modulos> consultarModulos();
    /**
     * Método encargado de recuperar las tablas del sistema asociadas a un modulo para 
     * la tabla 'Tablas del Sistema' de la Pantalla.
     * @param secuenciaMod Secuencia del Modulo
     * @return Retorna una lista de Tablas.
     */
    public List<Tablas> consultarTablas(BigInteger secuenciaMod);
    /**
     * Método encargado de recuperar una pantalla asociada a una tabla específica.
     * @param secuenciaTab Secuencia de la tabla.
     * @return Retorna una Pantalla.
     */
    public Pantallas consultarPantalla(BigInteger secuenciaTab);
    /**
     * Método encargado de recuperar todas las Aficiones.
     * @return Retorna una lista de Modulos.
     */
    public List<Aficiones> consultarAficiones();
    /**
     * Método encargado de recuperar una Afición dada su secuencia.
     * @param secuencia Secuencia de la Afición.
     * @return Retorna La Afición cuya secuencia coincide con el valor del parámetro.
     */
    public Aficiones consultarAficion(BigInteger secuencia);    
    /**
     * Método encargado de recuperar una Afición dado su código.
     * @param cod Código de la Afición.
     * @return Retorna La Afición cuya secuencia coincide con el valor del parámetro.
     */
    public Aficiones consultarAficionCodigo(Short cod);
    /**
     * Método encargado de sugerir un Código para la aficion que se va a crear.
     * Este método busca el mayor código registrado en la base de datos y le suma 1 
     * para sugerir un código consecutivo.
     * @return Retorna el valor del código sugerido.
     */
    public Integer sugerirCodigoAficiones();
    /**
     * Método encargado de crear una Afición.
     * @param aficion Afición que se van a crear.
     */
    public void crearAficion(Aficiones aficion);
    /**
     * Método encargado de editar Aficiones.
     * @param listAficiones Lista de las Aficiones que se van a modificar.
     */
    public void modificarAficiones(List<Aficiones> listAficiones);
    /**
     * Método encargado de borrar una Aficion.
     * @param aficion Aficion que se van a eliminar.
     */
    public void borrarAficion(Aficiones aficion);
    
	/**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
}
