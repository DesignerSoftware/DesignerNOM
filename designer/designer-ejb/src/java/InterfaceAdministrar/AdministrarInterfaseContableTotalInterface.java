package InterfaceAdministrar;

import Entidades.ActualUsuario;
import Entidades.Empresas;
import Entidades.InterconTotal;
import Entidades.ParametrosContables;
import Entidades.ParametrosEstructuras;
import Entidades.Procesos;
import Entidades.SolucionesNodos;
import Entidades.UsuariosInterfases;
import Entidades.VWActualesFechas;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Administrador
 */
public interface AdministrarInterfaseContableTotalInterface {

    public void obtenerConexion(String idSesion);

    public List<ParametrosContables> obtenerParametrosContablesUsuarioBD(String usuarioBD);

    public void modificarParametroContable(ParametrosContables parametro);

    public void crearParametroContable(ParametrosContables parametro);

    public void borrarParametroContable(List<ParametrosContables> listPC);
    
     public void borrarRegistroGenerado(List<SolucionesNodos> listBorrar);
     
      public void borrarRegistroIntercon(List<InterconTotal> listBorrar);

    public List<SolucionesNodos> obtenerSolucionesNodosParametroContable(Date fechaInicial, Date fechaFinal);

    public List<InterconTotal> obtenerInterconTotalParametroContable(Date fechaInicial, Date fechaFinal);

    public List<Procesos> lovProcesos();

    public List<Empresas> lovEmpresas();

    public ActualUsuario obtenerActualUsuario();

    public Date obtenerFechaMaxContabilizaciones();

    public Date obtenerFechaMaxInterconTotal();

    public ParametrosEstructuras parametrosLiquidacion();

    public void actualizarFlagInterconTotal(Date fechaInicial, Date fechaFinal, Short empresa);

    public Date buscarFechaHastaVWActualesFechas();

    public Date buscarFechaDesdeVWActualesFechas();

    public void ejcutarPKGUbicarnuevointercon_total(BigInteger secuencia, Date fechaInicial, Date fechaFinal, BigInteger proceso);

    public void actualizarFlagInterconTotalProcesoDeshacer(Date fechaInicial, Date fechaFinal, BigInteger proceso);

    public void eliminarInterconTotal(Date fechaInicial, Date fechaFinal, Short empresa, BigInteger proceso);

    public int contarProcesosContabilizadosInterconTotal(Date fechaInicial, Date fechaFinal);

    public void cerrarProcesoContabilizacion(Date fechaInicial, Date fechaFinal, Short empresa, BigInteger proceso);

    public Integer obtenerContadorFlagGeneradoFechasTotal(Date fechaIni, Date fechaFin);

    public void ejecutarPKGRecontabilizacion(Date fechaIni, Date fechaFin);

    public String obtenerDescripcionProcesoArchivo(BigInteger proceso);

    public String obtenerPathServidorWeb();

    public String obtenerPathProceso();

    public void ejecutarPKGCrearArchivoPlano(int tipoArchivo, Date fechaIni, Date fechaFin, BigInteger proceso, String nombreArchivo);

    public UsuariosInterfases obtenerUsuarioInterfaseContabilizacion();
}
