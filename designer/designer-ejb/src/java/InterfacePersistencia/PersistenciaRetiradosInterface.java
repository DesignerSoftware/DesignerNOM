/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.Retirados;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la tabla 'Retirados' 
 * de la base de datos.
 * @author AndresPineda
 */
public interface PersistenciaRetiradosInterface {    
    /**
     * Método encargado de insertar un Retirado en la base de datos.
     * @param retirados Retirado que se quiere crear.
     */
    public void crear(EntityManager em, Retirados retirados);
    /**
     * Método encargado de modificar un Retirado de la base de datos.
     * Este método recibe la información del parámetro para hacer un 'merge' con la 
     * información de la base de datos.
     * @param retirados Retirado con los cambios que se van a realizar.
     */
    public void editar(EntityManager em, Retirados retirados);
    /**
     * Método encargado de eliminar de la base de datos el Retirado que entra por parámetro.
     * @param retirados Retirado que se quiere eliminar.
     */
    public void borrar(EntityManager em, Retirados retirados);
    /**
     * Método encargado de buscar el Retirado con la secuencia dada por parámetro.
     * @param secuencia Secuencia del Retirado que se quiere encontrar.
     * @return Retorna el Retirado identificado con la secuencia dada por parámetro.
     */
    public Retirados buscarRetiroSecuencia(EntityManager em, BigInteger secuencia);
    /**
     * Método encargado de buscar todos los Retirados existentes en la base de datos.
     * @return Retorna una lista de Retirados.
     */
    public List<Retirados> buscarRetirados(EntityManager em);
    /**
     * Método encargado de buscar los Retiros de un Empleado.
     * @param secEmpleado Secuencia del empleado.
     * @return Retorna una lista de Retirados que tienen la información de los retiros del empleado.
     */
    public List<Retirados> buscarRetirosEmpleado(EntityManager em, BigInteger secEmpleado);
    /**
     * Método encargado de buscar el Retirado de una vigenciatipotrabajador.
     * @param secVigencia Secuencia de la vigenciatipotrabajador.
     * @return Retorna el Retirado asociado a la vigenciatipotrabajador cuya secuencia coincide con el parámetro.
     */
    public Retirados buscarRetiroVigenciaSecuencia(EntityManager em, BigInteger secVigencia);
    
    public void adicionaRetiroCambiosMasivos(EntityManager em, String indemniza, BigInteger secMotivoDefinitiva, BigInteger secMotivoRetiro, Date fechaCambio);
    
    public void undoAdicionaRetiroCambiosMasivos(EntityManager em, String indemniza, BigInteger secMotivoDefinitiva, BigInteger secMotivoRetiro, Date fechaCambio);
    
    public void adicionaReingresoCambiosMasivos(EntityManager em, Date fechaInicio, Date fechaFin);
    
}
