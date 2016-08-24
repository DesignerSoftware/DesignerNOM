/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.NormasLaborales;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author user
 */
@Local
public interface AdministrarNormasLaboralesInterface {
	/**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
    /**
     * Método encargado de modificar NormasLaborales.
     *
     * @param listaNormasLaborales Lista NormasLaborales que se van a modificar.
     */
    public void modificarNormasLaborales(List<NormasLaborales> listaNormasLaborales);

    /**
     * Método encargado de borrar NormasLaborales.
     *
     * @param listaNormasLaborales Lista NormasLaborales que se van a borrar.
     */
    public void borrarNormasLaborales(List<NormasLaborales> listaNormasLaborales);

    /**
     * Método encargado de crear NormasLaborales.
     *
     * @param listaNormasLaborales Lista NormasLaborales que se van a crear.
     */
    public void crearNormasLaborales(List<NormasLaborales> listaNormasLaborales);

    /**
     * Método encargado de recuperar las NormasLaborales para una tabla de la
     * pantalla.
     *
     * @return Retorna una lista de NormasLaborales.
     */
    public List<NormasLaborales> consultarNormasLaborales();

    /**
     * Método encargado de recuperar una NormasLaborales dada su secuencia.
     *
     * @param secNormasLaborales Secuencia del NormasLaborales
     * @return Retorna una NormasLaborales.
     */
    public NormasLaborales consultarMotivoContrato(BigInteger secNormasLaborales);

    /**
     * Método encargado de consultar si existe una relacion entre una
     * NormasLaborales específica y algún VigenciasNormasEmpleado. Adémas de la
     * revisión, establece cuantas relaciones existen.
     *
     * @param secNormasLaborales Secuencia del NormasLaborales.
     * @return Retorna el número de VigenciasNormasEmpleado relacionados con la
     * MotivoCambioCargo cuya secuencia coincide con el parámetro.
     */
    public BigInteger contarVigenciasNormasEmpleadoNormaLaboral(BigInteger secNormasLaborales);
}
