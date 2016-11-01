/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.VigenciasIndicadores;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la tabla 'VigenciasIndicadores' 
 * de la base de datos.
 * @author betelgeuse
 */
public interface PersistenciaVigenciasIndicadoresInterface {
    /**
     * Método encargado de insertar una VigenciaIndicador en la base de datos.
     * @param vigenciasIndicadores VigenciaIndicador que se quiere crear.
     */
    public void crear(EntityManager em, VigenciasIndicadores vigenciasIndicadores);
    /**
     * Método encargado de modificar una VigenciaIndicador de la base de datos.
     * Este método recibe la información del parámetro para hacer un 'merge' con la 
     * información de la base de datos.
     * @param vigenciasIndicadores VigenciaIndicador con los cambios que se van a realizar.
     */
    public void editar(EntityManager em, VigenciasIndicadores vigenciasIndicadores);
    /**
     * Método encargado de eliminar de la base de datos la VigenciaIndicador que entra por parámetro.
     * @param vigenciasIndicadores VigenciaIndicador que se quiere eliminar.
     */
    public void borrar(EntityManager em, VigenciasIndicadores vigenciasIndicadores);
    /**
     * Método encargado de buscar todas las VigenciasIndicadores existentes en la base de datos.
     * @return Retorna una lista de VigenciasIndicadores.
     */
    public List<VigenciasIndicadores> buscarVigenciasIndicadores(EntityManager em );
    /**
     * Método encargado de buscar la VigenciaIndicador con la secuencia dada por parámetro.
     * @param secuencia Secuencia de la VigenciaIndicador que se quiere encontrar.
     * @return Retorna la VigenciaIndicador identificada con la secuencia dada por parámetro.
     */
    public VigenciasIndicadores buscarVigenciaIndicadorSecuencia(EntityManager em, BigInteger secuencia);
    /**
     * Método encargado de buscar las VigenciasIndicadores de un Empleado específico.
     * @param secuencia Secuencia del Empleado.
     * @return Retorna las VigenciasIndicadores del Empleado cuya secuencia coincide 
     * con la secuencia dada por parámetro.
     */
    public List<VigenciasIndicadores> indicadoresTotalesEmpleadoSecuencia(EntityManager em, BigInteger secuencia);
    /**
     * Método encargado de buscar las ultimas VigenciasIndicadores registradas de un Empleado específico.
     * @param secuencia Secuencia del Empleado.
     * @return Retorna las VigenciasIndicadores cuya fechaInicial es la mayor fecha en la tabla para el Empleado cuya secuencia coincide 
     * con la secuencia dada por parámetro.
     */
    public List<VigenciasIndicadores> ultimosIndicadoresEmpleado(EntityManager em, BigInteger secuencia);
    
    public String primeraVigenciaIndicador(EntityManager em,BigInteger secuencia);
}
