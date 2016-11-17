/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Aficiones;
import Entidades.Ciudades;
import Entidades.Deportes;
import Entidades.Empleados;
import Entidades.Empresas;
import Entidades.EstadosCiviles;
import Entidades.Estructuras;
import Entidades.Idiomas;
import Entidades.Inforeportes;
import Entidades.ParametrosReportes;
import Entidades.TiposTelefonos;
import Entidades.TiposTrabajadores;
import java.util.List;

/**
 *
 * @author user
 */
public interface AdministrarNReportePersonalInterface {

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

    public List<Idiomas> listIdiomas();

    public List<Aficiones> listAficiones();

    public List<Deportes> listDeportes();

    public List<Ciudades> listCiudades();

    public List<TiposTrabajadores> listTiposTrabajadores();

    public List<Estructuras> listEstructuras();

    public List<Empleados> listEmpleados();

    public List<TiposTelefonos> listTiposTelefonos();

    public List<EstadosCiviles> listEstadosCiviles();

    public void guardarCambiosInfoReportes(List<Inforeportes> listaIR);

}
