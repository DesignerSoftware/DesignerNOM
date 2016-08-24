/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Cargos;
import Entidades.Empleados;
import Entidades.Empresas;
import Entidades.Inforeportes;
import Entidades.ParametrosInformes;
import java.util.List;

/**
 *
 * @author AndresPineda
 */
public interface AdministrarNReporteLaboralInterface {
    	/**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
    /**
     * Obtiene los parametros de reporte laboral
     * @return Objeto ParametrosInformes para los reportes
     */
    public ParametrosInformes parametrosDeReporte();
    /**
     * Obtiene la lista de inforeportes para los reportes laborales
     * @return Lista de Inforeportes para el usuario actual
     */
    public List<Inforeportes> listInforeportesUsuario();
    /**
     * Obtiene la lista de cargos
     * @return Lista de Cargos
     */
    public List<Cargos> listCargos();
    
    public void modificarParametrosInformes(ParametrosInformes parametroInforme);
    
    public List<Empleados> listEmpleados();
    public List<Empresas> listEmpresas();
    
    public void guardarCambiosInfoReportes(List<Inforeportes> listaIR);
    
}
