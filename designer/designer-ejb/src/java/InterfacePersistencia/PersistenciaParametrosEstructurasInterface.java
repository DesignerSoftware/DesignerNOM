/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.ParametrosEstructuras;
import java.math.BigInteger;
import javax.persistence.EntityManager;
/**
 * Interface encargada de determinar las operaciones que se realizan sobre la tabla 'ParametrosEstructuras' 
 * de la base de datos.
 * @author betelgeuse
 */
public interface PersistenciaParametrosEstructurasInterface {
    /**
     * Método encargado de modificar un ParametroEstructura de la base de datos.
     * Este método recibe la información del parámetro para hacer un 'merge' con la 
     * información de la base de datos.
     * @param parametroEstructura ParametroEstructura con los cambios que se van a realizar.
     */
    public void editar(EntityManager em, ParametrosEstructuras parametroEstructura);
    /**
     * Método encargado de buscar el ParametroEstructura del usuario que está usando el aplicativo.
     * @param usuarioBD Alias del usuario que está usando el aplicativo.
     * @return Retorna el ParametroEstructura del usuario con el alias dado por parámetro.
     */
    public ParametrosEstructuras buscarParametro(EntityManager em, String usuarioBD);
    /**
     * Método encargado de buscar la secuencia de la empresa asociada al ParametroEstructura del usuario
     * que está usando el aplicativo.
     * @param usuarioBD Alias del usuario que está usando el aplicativo.
     * @return Retorna la secuencia de la empresa.
     */
    public BigInteger buscarEmpresaParametros(EntityManager em, String usuarioBD);
    /**
     * Método encargado de buscar la secuencia de la empresa asociada al ParametroEstructura del usuario
     * que está usando el aplicativo, consultando directamente la base de datos.
     * @param em EntityManager usado para establecer la conexión.
     * @return Retorna la secuencia de la empresa.
     */
    public BigInteger buscarEmpresaParametros(EntityManager em) ;
    
    /**
     * Método encargado de contar los ParametrosEstructuras del usuario que está usando el aplicativo y
     * tengan como proceso el Proceso con secuencia igual a la que entra por parámetro.
     * @param secProceso Secuencia del proceso por el cual se van a contar los ParametrosEstructuras.
     * @return Retorna un Integer con el número de ParametrosEstructuras con el proceso dado por parámetro.
     */
    public Integer empleadosParametrizados(EntityManager em, BigInteger secProceso);
    /**
     * Método encargado de contar el tamaño del rango de fechas dado por parámetro.
     * Es decir cuenta cuantos días hay entre la fecha inicial y la fecha final.
     * Este método se usa para saber si al liquidar se están liquidando 30 días o menos.
     * @param fechaInicial Fecha inicial del rango.
     * @param fechaFinal Fecha final del rango
     * @return Retorna un Integer con el número de días de diferencia entre la fecha inicial y la fecha final.
     */
    public Integer diasDiferenciaFechas(EntityManager em, String fechaInicial, String fechaFinal);
    /**
     * Método encargado de adicionar los empleados a liquidar, teniendo en cuenta los datos del ParametroEstructura
     * cuya secuencia es igual al parámetro que se recibe.
     * @param secParametroEstructura Secuencia del ParametroEstructura.
     */
    public void adicionarEmpleados(EntityManager em, BigInteger secParametroEstructura);
}
