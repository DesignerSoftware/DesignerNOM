/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.VigenciasCargos;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
/**
 * Interface encargada de determinar las operaciones que se realizan sobre la tabla 'VigenciasCargos' 
 * de la base de datos.
 * @author betelgeuse
 */
public interface PersistenciaVigenciasCargosInterface {
    /**
     * Método encargado de insertar una VigenciaCargo en la base de datos.
     * @param vigenciasCargos VigenciaCargo que se quiere crear.
     */
    public void crear(EntityManager em, VigenciasCargos vigenciasCargos);
    /**
     * Método encargado de modificar una VigenciaCargo de la base de datos.
     * Este método recibe la información del parámetro para hacer un 'merge' con la 
     * información de la base de datos.
     * @param vigenciasCargos VigenciaCargo con los cambios que se van a realizar.
     */
    public void editar(EntityManager em, VigenciasCargos vigenciasCargos);
    /**
     * Método encargado de eliminar de la base de datos la VigenciaCargo que entra por parámetro.
     * @param vigenciasCargos VigenciaCargo que se quiere eliminar.
     */
    public void borrar(EntityManager em, VigenciasCargos vigenciasCargos);
    /**
     * Método encargado de buscar la VigenciaCargo con la secuencia dada por parámetro.
     * @param em
     * @param secuencia Secuencia de la VigenciaCargo que se quiere encontrar.
     * @return Retorna la VigenciaCargo identificada con la secuencia dada por parámetro.
     */
    public VigenciasCargos buscarVigenciaCargo(EntityManager em, BigInteger secuencia);

    public VigenciasCargos buscarVigenciaCargoXEmpleado(EntityManager em, BigInteger secuenciaEmpl, BigInteger secEmpresa);
    /**
     * Método encargado de buscar todas las VigenciasCargos existentes en la base de datos.
     * @return Retorna una lista de VigenciasCargos.
     */
    public List<VigenciasCargos> buscarVigenciasCargos(EntityManager em );
    /**
     * Método encargado de buscar las VigenciasCargos de un Empleado especifico.
     * @param secuencia Secuencia del Empleado.
     * @return Retorna una lista de VigenciasCargos ordenadas por fechaVigencia y
     * asociadas al Empleado cuya secuencia coincide con la del parámetro.
     */
    public List<VigenciasCargos> buscarVigenciasCargosEmpleado(EntityManager em, BigInteger secuencia);
    
    public void adicionaEmplJefeCambiosMasivos(EntityManager em, BigInteger secEmpl, Date fechaCambio);
}
