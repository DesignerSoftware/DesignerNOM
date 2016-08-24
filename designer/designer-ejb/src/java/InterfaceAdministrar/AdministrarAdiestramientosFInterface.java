/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfaceAdministrar;

import Entidades.AdiestramientosF;
import java.util.List;

/**
 * Interface encargada de determinar las operaciones lógicas necesarias para la pantalla 'AdiestramientosF'. 
 * @author betelgeuse
 */
public interface AdministrarAdiestramientosFInterface {    
    /**
     * Método encargado de recuperar todos los AdiestramientosF.
     * @return Retorna una lista de AdiestramientosF.
     */
    public List<AdiestramientosF> consultarAdiestramientosF();
    /**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
}
