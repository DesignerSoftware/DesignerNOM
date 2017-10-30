
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Asociaciones;
import Entidades.Empleados;
import Entidades.Empresas;
import Entidades.Estructuras;
import Entidades.GruposConceptos;
import Entidades.Inforeportes;
import Entidades.ParametrosReportes;
import Entidades.Procesos;
import Entidades.Terceros;
import Entidades.TiposAsociaciones;
import Entidades.TiposTrabajadores;
import Entidades.UbicacionesGeograficas;
import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author user
 */
public interface AdministrarNReportesNominaInterface {

    /**
     * Método encargado de obtener el Entity Manager el cual tiene asociado la
     * sesion del usuario que utiliza el aplicativo.
     *
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);

    /**
     *
     * @return
     */
    public ParametrosReportes parametrosDeReporte();
    public String usuarioActual();

    /**
     *
     * @return
     */
    public List<Inforeportes> listInforeportesUsuario();

    /**
     *
     * @return
     */
    public List<Empresas> listEmpresas();

    /**
     *
     * @return
     */
    public List<GruposConceptos> listGruposConcetos();

    /**
     *
     * @param parametroInforme
     */
    public void modificarParametrosReportes(ParametrosReportes parametroInforme);

    public List<Asociaciones> listAsociaciones();

    /**
     *
     * @return
     */
    public List<Procesos> listProcesos();

    /**
     *
     * @param secEmpresa
     * @return
     */
    public List<Terceros> listTercerosSecEmpresa(BigInteger secEmpresa);

    /**
     *
     * @return
     */
    public List<Terceros> listTerceros();

    /**
     *
     * @return
     */
    public List<TiposTrabajadores> listTiposTrabajadores();

    /**
     *
     * @return
     */
    public List<Estructuras> listEstructuras();

    /**
     *
     * @return
     */
    public List<TiposAsociaciones> listTiposAsociaciones();

    /**
     *
     * @return
     */
    public List<UbicacionesGeograficas> listUbicacionesGeograficas();

    /**
     *
     * @return
     */
    public List<Empleados> listEmpleados();

    public void guardarCambiosInfoReportes(List<Inforeportes> listaIR);

}
