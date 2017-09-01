/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.ActualUsuario;
import Entidades.Empresas;
import Entidades.InterconInfor;
import Entidades.ParametrosContables;
import Entidades.ParametrosEstructuras;
import Entidades.Procesos;
import Entidades.SolucionesNodos;
import Entidades.UsuariosInterfases;
import excepciones.ExcepcionBD;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

/**
 *
 * @author user
 */
public interface AdministrarInterfaseContableInforInterface {

    public void obtenerConexion(String idSesion);

    public List<ParametrosContables> obtenerParametrosContablesUsuarioBD(String usuarioBD);

    public void modificarParametroContable(ParametrosContables parametro);

    public void borrarParametroContable(List<ParametrosContables> listPC);

    public void crearParametroContable(ParametrosContables parametro);

    public void borrarRegistroGenerado(List<SolucionesNodos> listBorrar);

    public void borrarRegistroIntercon(List<InterconInfor> listBorrar);

    public List<SolucionesNodos> obtenerSolucionesNodosParametroContable(Date fechaInicial, Date fechaFinal);

    public List<InterconInfor> obtenerInterconInforParametroContable(Date fechaInicial, Date fechaFinal);

    public List<Procesos> lovProcesos();

    public List<Empresas> lovEmpresas();

    public ActualUsuario obtenerActualUsuario();

    public Date obtenerMaxFechaContabilizaciones();

    public Date obtenerMaxFechaInterconInfor();

    public ParametrosEstructuras parametrosLiquidacion();

    public void actualizarFlagInterconInfor(Date fechaInicial, Date fechaFinal, Short empresa);

    public Date buscarFechaHastaVWActualesFechas();

    public Date buscarFechaDesdeVWActualesFechas();

    public void ejecutarPKGUbicarnuevointercon_Infor(BigInteger secuencia, Date fechaIni, Date fechaFin, BigInteger proceso);

    public void actualizarFlagInterconInforProcesoDeshacer(Date fechaInicial, Date fechaFinal, BigInteger proceso);

    public void eliminarInterconInfor(Date fechaInicial, Date fechaFinal, Short empresa, BigInteger proceso);

    public int contarProcesosContabilizadosInterconInfor(Date fechaInicial, Date fechaFinal);

    public void cerrarProcesoContabilizacion(Date fechaIni, Date fechaFin, Short empresa, BigInteger proceso);

    public Integer obtenerContadorFlagGeneradoFechasInfor(Date fechaIni, Date fechaFin);

    public void ejecutarPKGRecontabilizacion(Date fechaIni, Date fechaFin) throws ExcepcionBD;

    public String obtenerDescripcionProcesoArchivo(BigInteger proceso);

    public String obtenerPathServidorWeb();

    public String obtenerPathProceso();

    public String ejecutarPKGCrearArchivoPlano(Date fechaIni, Date fechaFin, BigInteger CodigoEmpresa, BigInteger proceso, String nombreArchivo);

    public UsuariosInterfases obtenerUsuarioInterfaseContabilizacion();

}
