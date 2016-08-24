/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import java.math.BigInteger;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la tabla 'ParametrosEstados' 
 * de la base de datos.
 * @author betelgeuse
 */
public interface PersistenciaParametrosEstadosInterface {
    /**
     * Método encargado de contar los ParametrosEstados asociados y autorizados al usuario.
     * @param usuarioBD Alias del usuario que está usando la base de datos.
     * @return Retorna la cantidad de ParametrosEstados del usuario o nulo.
     */
    public Integer empleadosParaLiquidar(EntityManager em, String usuarioBD);
    /**
     * Método encargado de contar los ParametrosEstados asociados, autorizados al usuario y que su estado es 'LIQUIDADO'.
     * @param usuarioBD Alias del usuario que está usando la base de datos.
     * @return Retorna la cantidad de ParametrosEstados del usuario o nulo.
     */
    public Integer empleadosLiquidados(EntityManager em, String usuarioBD);
    /**
     * Método encargado de inicializar los ParametrosEstados asociados y autorizados al usuario.
     * Inicializar en este caso significa colocar el estado de los ParametrosEstados en 'A LIQUIDAR'.
     */
    public void inicializarParametrosEstados(EntityManager em);
    /**
     * Método encargado de buscar el estado de un ParametroEstado cuyo Parámetro es especificado.
     * @param secuenciaParametro Secuencia del Parámetro asociado al ParametroEstado
     * @return Retorna un string con el estado del ParametroEstado asociado al Parámetro deseado.
     */
    public String parametrosComprobantes(EntityManager em, BigInteger secuenciaParametro);
}
