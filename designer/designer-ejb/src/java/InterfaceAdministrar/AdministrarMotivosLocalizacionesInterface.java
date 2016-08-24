/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.MotivosLocalizaciones;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author user
 */
@Local
public interface AdministrarMotivosLocalizacionesInterface {
	/**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
    /**
     * Método encargado de modificar MotivosLocalizaciones.
     *
     * @param listaMotivosLocalizaciones Lista MotivosLocalizaciones que se van
     * a modificar.
     */
    public void modificarMotivosLocalizaciones(List<MotivosLocalizaciones> listaMotivosLocalizaciones);

    /**
     * Método encargado de borrar MotivosLocalizaciones.
     *
     * @param listaMotivosLocalizaciones Lista MotivosLocalizaciones que se van
     * a borrar.
     */
    public void borrarMotivosLocalizaciones(List<MotivosLocalizaciones> listaMotivosLocalizaciones);

    /**
     * Método encargado de crear MotivosLocalizaciones.
     *
     * @param listaMotivosLocalizaciones Lista MotivosLocalizaciones que se van
     * a crear.
     */
    public void crearMotivosLocalizaciones(List<MotivosLocalizaciones> listaMotivosLocalizaciones);

    /**
     * Método encargado de recuperar las MotivosLocalizaciones para una tabla de
     * la pantalla.
     *
     * @return Retorna una lista de MotivosLocalizaciones.
     */
    public List<MotivosLocalizaciones> mostrarMotivosCambiosCargos();

    /**
     * Método encargado de recuperar una MotivosLocalizaciones dada su
     * secuencia.
     *
     * @param secMotivosLocalizaciones Secuencia del MotivosLocalizaciones
     * @return Retorna una MotivosLocalizaciones.
     */
    public MotivosLocalizaciones mostrarMotivoCambioCargo(BigInteger secMotivosLocalizaciones);
    
    public BigInteger contarVigenciasLocalizacionesMotivoLocalizacion (BigInteger secMotivoLocalizacion);
}
