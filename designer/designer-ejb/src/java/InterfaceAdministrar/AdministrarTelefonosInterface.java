/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Ciudades;
import Entidades.Empleados;
import Entidades.Personas;
import Entidades.Telefonos;
import Entidades.TiposTelefonos;
import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author Administrator
 */
public interface AdministrarTelefonosInterface {
    /**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
    
    public List<Telefonos> telefonosPersona(BigInteger secPersona);
    public Personas encontrarPersona(BigInteger secPersona);
    public List<TiposTelefonos>  lovTiposTelefonos();
    public List<Ciudades>  lovCiudades();
    public void modificarTelefono(List<Telefonos> listaTelefonosModificar);
    public void borrarTelefono(Telefonos telefonos);
    public void crearTelefono(Telefonos telefonos);
    public Empleados empleadoActual(BigInteger secuencia);
}
