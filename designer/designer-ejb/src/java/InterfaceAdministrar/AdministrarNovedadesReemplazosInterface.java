/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Cargos;
import Entidades.Empleados;
import Entidades.Encargaturas;
import Entidades.Estructuras;
import Entidades.MotivosReemplazos;
import Entidades.TiposReemplazos;
import java.math.BigInteger;
import java.util.List;

public interface AdministrarNovedadesReemplazosInterface {
	/**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
    public List<Encargaturas> encargaturasEmpleado(BigInteger secEmpleado);

    public Empleados encontrarEmpleado(BigInteger secEmpleado);

    public List<Empleados> lovEmpleados();
    
    public List<Cargos> lovCargos();
    
    public List<TiposReemplazos> lovTiposReemplazos();

    public List<MotivosReemplazos> lovMotivosReemplazos();

    public List<Estructuras> lovEstructuras();


    public void modificarEncargatura(List<Encargaturas> listaEncargaturasModificar);

    public void borrarEncargaturas(Encargaturas encargaturas);

    public void crearEncargaturas(Encargaturas encargaturas);
}
