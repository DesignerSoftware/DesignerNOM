/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfaceAdministrar;

import Entidades.Ciudades;
import java.math.BigInteger;
import java.util.List;

/**
 * Interface encargada de determinar las operaciones lógicas necesarias para la pantalla 'Ciudades'.
 * @author betelgeuse
 */
public interface AdministrarCiudadesInterface {
    /**
     * Método encargado de crear Ciudades.
     * @param listaCiudades Lista de los Ciudades que se van a crear.
     */
    public void crearCiudades(List<Ciudades> listaCiudades);
    /**
     * Método encargado de editar Ciudades.
     * @param listaCiudades Lista de los Ciudades que se van a modificar.
     */
    public void modificarCiudades(List<Ciudades> listaCiudades);
    /**
     * Método encargado de borrar Ciudades.
     * @param listaCiudades Lista de los Ciudades que se van a eliminar.
     */
    public boolean borrarCiudades(List<Ciudades> listaCiudades);
    /**
     * Método encargado de recuperar todas las Ciudades.
     * @return Retorna una lista de Ciudades.
     */
    public List<Ciudades> consultarCiudades();
    
    /**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
    
    public int existeenUbicacionesGeograficas(BigInteger secCiudad);
}
