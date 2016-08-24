/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Asociaciones;
import Entidades.Empleados;
import Entidades.Empresas;
import Entidades.Estructuras;
import Entidades.GruposConceptos;
import Entidades.Inforeportes;
import Entidades.ParametrosInformes;
import Entidades.Procesos;
import Entidades.SucursalesPila;
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
public interface AdministrarNReportesSeguridadInterface {

    public void obtenerConexion(String idSesion);

    public ParametrosInformes parametrosDeReporte();

    public List<Inforeportes> listInforeportesUsuario();

    public List<Empresas> listEmpresas();

    public List<GruposConceptos> listGrupos();

    public void modificarParametrosInformes(ParametrosInformes parametroInforme);

    public List<Terceros> listTerceros();

    public List<TiposTrabajadores> listTiposTrabajadores();

    public List<Estructuras> listEstructuras();

    public List<SucursalesPila> listSucursales(BigInteger secuenciaEmpresa);

    public List<Empleados> listEmpleados();

    public void guardarCambiosInfoReportes(List<Inforeportes> listaIR);

}
