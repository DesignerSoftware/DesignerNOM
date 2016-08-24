/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Aficiones;
import Entidades.Empleados;
import Entidades.VigenciasAficiones;
import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author user
 */
public interface AdministrarVigenciaAficionInterface {

    /**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
    
    public List<VigenciasAficiones> listVigenciasAficionesPersona(BigInteger secuenciaP);

    public void crearVigenciasAficiones(List<VigenciasAficiones> listVA);

    public void editarVigenciasAficiones(List<VigenciasAficiones> listVA);

    public void borrarVigenciasAficiones(List<VigenciasAficiones> listVA);

    public List<Aficiones> listAficiones();

    public Empleados empleadoActual(BigInteger secuencia);
}
