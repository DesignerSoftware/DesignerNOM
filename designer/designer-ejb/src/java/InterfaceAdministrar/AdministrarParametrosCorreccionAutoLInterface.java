/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.ActualUsuario;
import Entidades.AportesCorrecciones;
import Entidades.Empleados;
import Entidades.Empresas;
import Entidades.ParametrosCorreccionesAutoL;
import Entidades.ParametrosEstructuras;
import Entidades.ParametrosInformes;
import Entidades.Terceros;
import Entidades.TiposEntidades;
import Entidades.TiposTrabajadores;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

/**
 *
 * @author user
 */
public interface AdministrarParametrosCorreccionAutoLInterface {

    public void obtenerConexion(String idSesion);

    public List<ParametrosCorreccionesAutoL> consultarParametrosCorreccionesAutoliq();

    public void crearParametrosCorreccionAutoliq(List<ParametrosCorreccionesAutoL> listaPCA);

    public void editarParametrosCorreccionAutoliq(List<ParametrosCorreccionesAutoL> listaPCA);

    public void borrarParametrosCorreccionAutoliq(List<ParametrosCorreccionesAutoL> listaPCA);

    public void crearAportesCorrecciones(List<AportesCorrecciones> listaAC);

    public void editarAportesCorrecciones(List<AportesCorrecciones> listAC);

    public void borrarAportesCorrecciones(List<AportesCorrecciones> listAC);

    public List<AportesCorrecciones> consultarAportesCorrecciones();

    public List<AportesCorrecciones> consultarLovAportesCorrecciones();

    public List<TiposTrabajadores> lovTiposTrabajadores();

    public List<Empleados> lovEmpleados();

    public List<TiposEntidades> lovTiposEntidades();

    public List<Terceros> lovTerceros();

    public List<Empresas> lovEmpresas();

    public ActualUsuario obtenerActualUsuario();

    public ParametrosEstructuras buscarParametroEstructura(String usuario);

    public ParametrosInformes buscarParametroInforme(String usuario);

    public void modificarParametroEstructura(ParametrosEstructuras parametro);

    public void modificarParametroInforme(ParametrosInformes parametro);

    public void borrarAportesCorreccionesProcesoAutomatico(BigInteger empresa, short mes, short ano);

    public String ejecutarPKGActualizarNovedadesCorreccion(short ano, short mes, BigInteger secuencia);
    
    public String ejecutarPKGIdentificaCorreccion(short ano,short mes,BigInteger secuenciaEmpresa);

    public String ejecutarPKGInsertarCorreccion(Date fechaIni, Date fechaFin, BigInteger secTipoTrabajador, BigInteger secuenciaEmpresa);

}
