/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfaceAdministrar;

import Entidades.ConsultasLiquidaciones;
import Entidades.ParametrosEstructuras;
import java.math.BigInteger;
import java.util.List;

/**
 * Interface encargada de determinar las operaciones lógicas necesarias para la pantalla 'Barra'. 
 * @author betelgeuse
 */
public interface AdministrarBarraInterface {
    /**
     * Método encargado de recuperar la cantidad de empleados a liquidar.
     * @return Retorna el número de Empleados a liquidar. 
     */
    public Integer contarEmpleadosParaLiquidar();
    /**
     * Método encargado de recuperar la cantidad de empleados liquidados.
     * @return Retorna el número de Empleados liquidados. 
     */
    public Integer contarEmpleadosLiquidados();
    /**
     * Método encargado de averiguar si un usuario tiene permisos para liquidar.
     * @param usuarioBD Alias del usuario.
     * @return Retorna True si el usuario cuyo Alias coincide con el parámetro tiene permisos para liquidar.
     */
    public boolean verificarPermisosLiquidar(String usuarioBD);
    /**
     * Método encargado de correr el proceso para liquidar nómina.
     */
    public void liquidarNomina();
    /**
     * Método encargado de recuperar el Alias del usuario que está usando el aplicativo.
     * @return Retorna un String con el Alias del usuario.
     */
    public String consultarUsuarioBD();
    /**
     * Método encargado de recuperar en que estado está la liquidación. 
     * @param usuarioBD Alias del usuario que hace la liquidación.
     * @return Retorna un String con el estado de la liquidación.
     */
    public String consultarEstadoLiquidacion(String usuarioBD);
    /**
     * Método encargado de recuperar las fechas, la estructura y el proceso para los parámetros de liquidación.
     * @return Retorna un ParametroEstructura con la parte de la información para los parámetros de liquidación.
     */
    public ParametrosEstructuras consultarParametrosLiquidacion();
    /**
     * Método encargado de inicializar el estado del párametro a liquidar.
     */
    public void inicializarParametrosEstados();
    /**
     * Método encargado de recuperar el progreso de la liquidación.
     * @param totalEmpleadoALiquidar El número total de los empleados que se van a liquidar.
     * @return Retorna un Integer con el progreso de la liquidación.
     */
    public Integer consultarProgresoLiquidacion(Integer totalEmpleadoALiquidar);
    /**
     * Método encargado de cancelar, por medio de la comunicación con la persistencia,
     * la liquidación realizada por un usuario específico.
     * @param usuarioBD Alias del usuario.
     */
    public void cancelarLiquidacion(String usuarioBD);
    /**
     * Metodo encargado de recuperar la liquidaciones cerradas en un rango de fechas determinado.
     * @param fechaInicial Fecha inicial del rango deseado.
     * @param fechaFinal Fecha final del rango deseado.
     * @return Retorna una lista de ConsultasLiquidaciones cerradas y que fueron realizadas en el rango definido.
     */
    public List<ConsultasLiquidaciones> liquidacionesCerradas(String fechaInicial, String fechaFinal);
    /**
     * Método encargado de recuperar las liquidaciones NO cerradas.
     * @return Retorna una lista de ConsultasLiquidaciones realizadas para preNomina 
     */
    public List<ConsultasLiquidaciones> consultarPreNomina();
    /**
     * Método encargado de recuperar la información del campo barraconsultadatos de la tabla 'Empresas', por medio de la persistencia.
     * Recibe como resultado 'S' o 'N' y define si se muestra el proceso despues de liquidar o no
     * @param secuenciaEmpresa Secuencia de la Empresa.
     * @return Retorna un String 'S' si se muestra el proceso despues de liquidar o 'N' de lo contrario.
     */
    public String consultarEstadoConsultaDatos(BigInteger secuenciaEmpresa);
    
    /**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
}
