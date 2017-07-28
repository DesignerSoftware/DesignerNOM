/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.ActualUsuario;
import Entidades.AportesCorrecciones;
import Entidades.AportesEntidades;
import Entidades.Empleados;
import Entidades.Empresas;
import Entidades.ParametrosAutoliq;
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
 * @author Administrador
 */
public interface AdministrarParametroAutoliqInterface {

    public void obtenerConexion(String idSesion);

    public List<ParametrosAutoliq> consultarParametrosAutoliq();

    public void crearParametrosAutoliq(List<ParametrosAutoliq> listaPA);

    public void editarParametrosAutoliq(List<ParametrosAutoliq> listaPA);

    public void borrarParametrosAutoliq(List<ParametrosAutoliq> listaPA);

    public List<AportesEntidades> consultarAportesEntidadesPorParametroAutoliq(BigInteger empresa, short mes, short ano);

    public void crearAportesEntidades(List<AportesEntidades> listaAE);

    public void editarAportesEntidades(List<AportesEntidades> listAE);

    public void borrarAportesEntidades(List<AportesEntidades> listAE);

    public List<TiposTrabajadores> lovTiposTrabajadores();

    public List<Empleados> lovEmpleados();

    public List<TiposEntidades> lovTiposEntidades();

    public List<Terceros> lovTerceros();

    public List<Empresas> lovEmpresas();

    public String borrarAportesEntidadesProcesoAutomatico(BigInteger empresa, short mes, short ano);

    public ActualUsuario obtenerActualUsuario();

    public ParametrosEstructuras buscarParametroEstructura(String usuario);

    public ParametrosInformes buscarParametroInforme(String usuario);

    public void modificarParametroEstructura(ParametrosEstructuras parametro);

    public void modificarParametroInforme(ParametrosInformes parametro);

    public String ejecutarPKGActualizarNovedades(short ano, short mes, BigInteger secuencia);

    public String ejecutarPKGInsertar(Date fechaIni, Date fechaFin, BigInteger secTipoTrabajador, BigInteger secuenciaEmpresa);

    public String ejecutarPKGAcumularDiferencia(short ano, short mes, BigInteger secuencia);
    
    public List<AportesEntidades> consultarAportesEntidadesPorEmpleado(BigInteger secEmpleado, short mes, short ano);

}
