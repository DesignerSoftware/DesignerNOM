/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Empleados;
import Entidades.EstadosCiviles;
import Entidades.VigenciasEstadosCiviles;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author user
 */
@Local
public interface AdministrarVigenciasEstadosCivilesInterface {

    /**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
    
    public List<VigenciasEstadosCiviles> consultarVigenciasEstadosCivilesPorEmpleado(BigInteger secEmpleado);
public List<VigenciasEstadosCiviles> consultarVigenciasEstadosCivilesPorEmpleado();
    public void modificarVigenciasEstadosCiviles(List<VigenciasEstadosCiviles> listaVigenciasEstadosCiviles);

    public void borrarVigenciasEstadosCiviles(List<VigenciasEstadosCiviles> listaVigenciasEstadosCiviles);

    public void crearVigenciasEstadosCiviles(List<VigenciasEstadosCiviles> listaVigenciasEstadosCiviles);

    public Empleados consultarEmpleado(BigInteger secuencia);

    public List<EstadosCiviles> lovEstadosCiviles();
}
