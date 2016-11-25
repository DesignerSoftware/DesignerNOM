/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Empleados;
import Entidades.Empresas;
import Entidades.Inforeportes;
import Entidades.ParametrosReportes;
import Entidades.Procesos;
import java.util.List;

/**
 *
 * @author user
 */
public interface AdministrarNReporteContabilidadInterface {

    /**
     * Método encargado de obtener el Entity Manager el cual tiene asociado la
     * sesion del usuario que utiliza el aplicativo.
     *
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);

    public ParametrosReportes parametrosDeReporte();

    public List<Inforeportes> listInforeportesUsuario();

    public void modificarParametrosReportes(ParametrosReportes parametroInforme);

    public List<Procesos> listProcesos();

    public List<Empleados> listEmpleados();

    public List<Empresas> listEmpresas();

    public void guardarCambiosInfoReportes(List<Inforeportes> listaIR);

}
