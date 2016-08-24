/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfaceAdministrar;

import Entidades.AdiestramientosNF;
import java.util.List;

/**
 * Interface encargada de determinar las operaciones lógicas necesarias para la pantalla 'AdiestramientosNF'.
 * @author betelgeuse
 */
public interface AdministrarAdiestramientosNFInterface {
    /**
     * Método encargado de recuperar todos los AdiestramientosNF.
     * @return Retorna una lista de AdiestramientosNF.
     */
    public List<AdiestramientosNF> consultarAdiestramientosNF();
    /**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
}
