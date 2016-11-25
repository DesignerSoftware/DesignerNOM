/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Bancos;
import Entidades.Ciudades;
import Entidades.Empleados;
import Entidades.Empresas;
import Entidades.Inforeportes;
import Entidades.ParametrosReportes;
import Entidades.Procesos;
import Entidades.TiposTrabajadores;
import java.util.List;

/**
 *
 * @author user
 */
public interface AdministrarReportesBancosInterface {

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

    public List<Empresas> listEmpresas();

    public List<Empleados> listEmpleados();

    public List<TiposTrabajadores> listTiposTrabajadores();

    public List<Procesos> listProcesos();

    public List<Bancos> listBancos();

    public List<Ciudades> listCiudades();

    public void guardarCambiosInfoReportes(List<Inforeportes> listaIR);

}
