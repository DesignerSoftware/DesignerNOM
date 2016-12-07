/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Empleados;
import Entidades.Empresas;
import Entidades.Estructuras;
import Entidades.Inforeportes;
import Entidades.ParametrosReportes;
import java.util.List;

/**
 *
 * @author user
 */
public interface AdministrarNReporteCapacitacionInterface {

    /**
     * Método encargado de obtener el Entity Manager el cual tiene asociado la
     * sesion del usuario que utiliza el aplicativo.
     *
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);

    public ParametrosReportes parametrosDeReporte();

    public void modificarParametrosReportes(ParametrosReportes parametroInforme);

    /**
     *
     * @return
     */
    public List<Inforeportes> listInforeportesUsuario();

    public List<Empresas> listEmpresas();

    public List<Empleados> listEmpleados();

    public List<Estructuras> listEstructuras();

    public void guardarCambiosInfoReportes(List<Inforeportes> listaIR);
}
