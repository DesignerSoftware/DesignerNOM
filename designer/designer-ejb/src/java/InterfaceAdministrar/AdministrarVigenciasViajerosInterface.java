/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Empleados;
import Entidades.Tiposviajeros;
import Entidades.VigenciasViajeros;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author user
 */
@Local
public interface AdministrarVigenciasViajerosInterface {

    /**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
    
    public void modificarVigenciasViajeros(List<VigenciasViajeros> listaVigenciasViajeros);

    public void borrarVigenciasViajeros(List<VigenciasViajeros> listaVigenciasViajeros);

    public void crearVigenciasViajeros(List<VigenciasViajeros> listaVigenciasViajeros);

    public List<VigenciasViajeros> consultarVigenciasViajeros();

    public VigenciasViajeros consultarTipoViajero(BigInteger secVigenciasViajeros);

    public List<VigenciasViajeros> consultarVigenciasViajerosPorEmpleado(BigInteger secVigenciasViajeros);

    public List<Tiposviajeros> consultarLOVTiposViajeros();

    public Empleados consultarEmpleado(BigInteger secuencia);
}
