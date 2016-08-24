/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Empleados;
import Entidades.Idiomas;
import Entidades.IdiomasPersonas;
import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author user
 */
public interface AdministrarIdiomaPersonaInterface {
    	/**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);

    public void crearIdiomasPersonas(List<IdiomasPersonas> listaID);

    public void borrarIdiomasPersonas(List<IdiomasPersonas> listaID);

    public void editarIdiomasPersonas(List<IdiomasPersonas> listaID);

    public List<IdiomasPersonas> listIdiomasPersonas(BigInteger secuencia);

    public List<Idiomas> listIdiomas();

    public Empleados empleadoActual(BigInteger secuencia);
}
