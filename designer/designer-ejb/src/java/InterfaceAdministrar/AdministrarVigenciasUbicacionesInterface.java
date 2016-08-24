/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Empleados;
import Entidades.UbicacionesGeograficas;
import Entidades.VigenciasUbicaciones;
import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author Administrator
 */
public interface AdministrarVigenciasUbicacionesInterface {

    /**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
    
    public List<VigenciasUbicaciones> vigenciasUbicacionesEmpleado(BigInteger secEmpleado);

    public void modificarVU(List<VigenciasUbicaciones> listVUModificadas);

    public void borrarVU(VigenciasUbicaciones vigenciasUbicaciones);

    public void crearVU(VigenciasUbicaciones vigenciasUbicaciones);

    public Empleados buscarEmpleado(BigInteger secuencia);

    public List<UbicacionesGeograficas> ubicacionesGeograficas();
}
