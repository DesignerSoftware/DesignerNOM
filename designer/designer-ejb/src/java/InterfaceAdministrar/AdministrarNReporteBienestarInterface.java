/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Actividades;
import Entidades.Empleados;
import Entidades.Inforeportes;
import Entidades.ParametrosInformes;
import java.util.List;

/**
 *
 * @author user
 */
public interface AdministrarNReporteBienestarInterface {
    	/**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
    public ParametrosInformes parametrosDeReporte();
    public List<Inforeportes> listInforeportesUsuario();
    public void modificarParametrosInformes(ParametrosInformes parametroInforme);
    public List<Actividades> listActividades();
    public List<Empleados> listEmpleados();
    public void guardarCambiosInfoReportes(List<Inforeportes> listaIR);
    
}
