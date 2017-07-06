/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfaceAdministrar;

import Entidades.Parametros;
import Entidades.ParametrosEstructuras;
import java.math.BigInteger;
import java.util.List;

/**
 * Interface encargada de determinar las operaciones lógicas necesarias para la
 * pantalla 'ATExtraRecargo'.
 *
 * @author betelgeuse.
 */
public interface AdministrarCerrarLiquidacionInterface {

    /**
     * Método encargado de contar los empleados para liquidar, según los
     * ParametrosEstados asociados y autorizados al usuario.
     *
     * @return Retorna la cantidad de empleados que se van a liquitar teniendo
     * en cuenta los ParametrosEstados del usuario. Si encuentra una excepción
     * retorna nulo.
     */
    public Integer contarEmpleadosParaLiquidar();

    /**
     * Método encargado de validar si el usuario, que está usando la sesión,
     * tiene permiso para liquidar.
     *
     * @param usuarioBD Alias del usuario que está usando la sesión del
     * aplicativo.
     * @return Retorna true si el usuario tiene permisos.
     */
    public boolean verificarPermisosLiquidar(String usuarioBD);

    /**
     * Método encargado de consultar el alias del usuario que está usando la
     * sesión actual del aplicativo.
     *
     * @return Retorna el alias del usuario que está usando el aplicativo.
     */
    public String consultarAliasUsuarioBD();

    /**
     * Método encargado de recuperar la información de los parámetros de
     * liquidación para la sesión actual.
     *
     * @return Retorna un ParametroEstructura con la información necesaria para
     * completar los parámetros de liquidación.
     */
    public ParametrosEstructuras consultarParametrosLiquidacion();

    /**
     * Método encargado de recuperar la información requerida de los empleados a
     * los que se les cerrará la liquidación por parte del usuario que esta
     * usando la sesión del aplicativo.
     *
     * @param usuarioBD Alias del usuario que esta usando el aplicativo.
     * @return Retorna una lista de Parámetros con la información requerida para
     * cada uno de los empleados que se van a liquidar.
     */
    public List<Parametros> consultarEmpleadosCerrarLiquidacion(String usuarioBD);

    /**
     * Método encargado de cerrar la liquidación de manera automatica.
     */
    public void cerrarLiquidacionAutomatico();

    /**
     * Método encargado de cerrar la liquidación de manera no automatica.
     */
    public void cerrarLiquidacionNoAutomatico();

    /**
     * Método encargado de recuperar el número de empleados cuyo estado es
     * 'LIQUIDADO' cuando voy a liquidar con un proceso definido.
     *
     * @param secProceso Secuencia del proceso.
     * @return Retorna el número de empleados que voy a liquidar asociados al
     * proceso cuya secuencia coincida con el valor dado por parámetro.
     */
    public Integer consultarConteoProcesoSN(BigInteger secProceso);

    /**
     * Método encargado de recuperar el número de las liquidaciones cerradas con
     * determinado proceso en un rango de fechas dadas.
     *
     * @param secProceso Secuencia del proceso con el que se realiza la
     * liquidación
     * @param fechaDesde Fecha inicial del rango
     * @param fechaHasta Fecha final del rango
     * @return Retorna el numero de liquidaciones que hay en la base de datos y
     * que cumplen con los parámetros de entrada
     */
    public Integer contarLiquidacionesCerradas(BigInteger secProceso, String fechaDesde, String fechaHasta);

    /**
     * Método encargado de abrir la liquidación realizada con un proceso
     * específico en un rango de fechas determinado.
     *
     * @param codigoProceso Código del proceso
     * @param fechaDesde Fecha inicial del rango
     * @param fechaHasta Fecha final del rango
     */
    public String abrirLiquidacion(Short codigoProceso, String fechaDesde, String fechaHasta);
    
    /**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
}
