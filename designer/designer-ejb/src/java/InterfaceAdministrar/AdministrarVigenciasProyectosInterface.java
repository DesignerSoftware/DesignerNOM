/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Cargos;
import Entidades.Empleados;
import Entidades.Proyectos;
import Entidades.PryRoles;
import Entidades.VigenciasProyectos;
import java.math.BigInteger;
import java.util.List;


public interface AdministrarVigenciasProyectosInterface {
    
    /**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
    
     public void crearVigenciaProyecto(VigenciasProyectos vigenciasProyectos);
     public void borrarVigenciaProyecto(VigenciasProyectos vigenciasProyectos);
     public void modificarVigenciaProyecto(List<VigenciasProyectos> listaVigenciasProyectosModificar);
     public List<Cargos> lovCargos();
     public List<PryRoles> lovPryRoles();
     public List<Proyectos> lovProyectos();
     public Empleados encontrarEmpleado(BigInteger secEmpleado); 
     public List<VigenciasProyectos> vigenciasProyectosEmpleado(BigInteger secEmpleado);
     public Proyectos buscarProyectoPorNombreVigencia(String nombre);
         
}
