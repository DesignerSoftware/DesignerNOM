/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Empleados;
import Entidades.GruposInfAdicionales;
import Entidades.InformacionesAdicionales;
import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author user
 */
public interface AdministrarEmplInformacionAdicionalInterface {

    public List<InformacionesAdicionales> listInformacionesAdicionalesEmpleado(BigInteger secuencia);

    public void borrarInformacionAdicional(List<InformacionesAdicionales> listInfoA);

    public void crearInformacionAdicional(List<InformacionesAdicionales> listInfoA);

    public void modificarInformacionAdicional(List<InformacionesAdicionales> listInfoA);

    public List<GruposInfAdicionales> listGruposInfAdicionales();

    public Empleados empleadoActual(BigInteger secuencia);

    /**
     * Método encargado de obtener el Entity Manager el cual tiene asociado la
     * sesion del usuario que utiliza el aplicativo.
     *
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);

}
