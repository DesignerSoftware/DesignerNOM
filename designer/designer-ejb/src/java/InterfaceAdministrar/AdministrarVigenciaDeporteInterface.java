/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Deportes;
import Entidades.Empleados;
import Entidades.Personas;
import Entidades.VigenciasDeportes;
import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author user
 */
public interface AdministrarVigenciaDeporteInterface {

   /**
    * Método encargado de obtener el Entity Manager el cual tiene asociado la
    * sesion del usuario que utiliza el aplicativo.
    *
    * @param idSesion Identificador se la sesion.
    */
   public void obtenerConexion(String idSesion);

   public List<VigenciasDeportes> listVigenciasDeportesPersona(BigInteger secuenciaP);

   public void crearVigenciasDeportes(List<VigenciasDeportes> listaVD);

   public void editarVigenciasDeportes(List<VigenciasDeportes> listaVD);

   public void borrarVigenciasDeportes(List<VigenciasDeportes> listaVD);

   public List<Deportes> listDeportes();

   public Empleados empleadoActual(BigInteger secuenciaP);

   public Personas obtenerPersonaPorEmpleado(BigInteger secEmpleado);
}
