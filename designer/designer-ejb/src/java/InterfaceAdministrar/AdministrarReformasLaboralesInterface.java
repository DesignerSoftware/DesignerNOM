/**
 * Documentación a cargo de Andres Pineda
 */
package InterfaceAdministrar;

import Entidades.DetallesReformasLaborales;
import Entidades.ReformasLaborales;
import java.math.BigInteger;
import java.util.List;

/**
 * Interface encargada de determinar las operaciones lógicas necesarias para la
 * pantalla 'ReformaLaboral'.
 *
 * @author AndresPineda
 */
public interface AdministrarReformasLaboralesInterface {
	/**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
    /**
     * Método encargado de recuperar todas las ReformasLaborales.
     *
     * @return Retorna una lista de ReformasLaborales.
     */
    public List<ReformasLaborales> listaReformasLaborales();

    /**
     * Método encargado de crear una ReformaLaboral.
     *
     * @param listaRL Lista de las ReformasLaborales que se van a crear.
     */
    public void crearReformaLaboral(List<ReformasLaborales> listaRL);

    /**
     * Método encargado de crear una ReformaLaboral.
     *
     * @param listaRL Lista de las ReformasLaborales que se van a crear.
     */
    public void editarReformaLaboral(List<ReformasLaborales> listaRL);

    /**
     * Método encargado de crear una ReformaLaboral.
     *
     * @param listaRL Lista de las ReformasLaborales que se van a crear.
     */
    public void borrarReformaLaboral(List<ReformasLaborales> listaRL);

    /**
     * Método encargado de recuperar todos los DetallesReformasLaborales para una ReformaLaboral
     *
     * @param secuencia Secuencia ReformaLaboral
     * @return Retorna una lista de DetallesReformasLaborales.
     */
    public List<DetallesReformasLaborales> listaDetalleReformasLaborales(BigInteger secuencia);

    /**
     * Método encargado de crear un DetalleReformaLaboral.
     *
     * @param listaDRL Lista de los DetallesReformasLaborales que se van a crear.
     */
    public void crearDetalleReformaLaboral(List<DetallesReformasLaborales> listaDRL);

    /**
     * Método encargado de crear un DetalleReformaLaboral.
     *
     * @param listaDRL Lista de los DetallesReformasLaborales que se van a crear.
     */
    public void editarDetalleReformaLaboral(List<DetallesReformasLaborales> listaDRL);

    /**
     * Método encargado de crear un DetalleReformaLaboral.
     *
     * @param listaDRL Lista de los DetallesReformasLaborales que se van a crear.
     */
    public void borrarDetalleReformaLaboral(List<DetallesReformasLaborales> listaDRL);
    
    public String clonarReformaLaboral(String nuevoNombre, short codigoNuevo, short codOrigen);

}
