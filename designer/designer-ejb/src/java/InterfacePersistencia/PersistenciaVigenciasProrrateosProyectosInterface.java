/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.VigenciasProrrateosProyectos;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la tabla 'VigenciasProrrateosProyectos' 
 * de la base de datos.
 * @author AndresPineda
 */
public interface PersistenciaVigenciasProrrateosProyectosInterface {
    /**
     * Método encargado de insertar una VigenciaProrrateoProyecto en la base de datos.
     * @param vigenciasProrrateosProyectos VigenciaProrrateoProyecto que se quiere crear.
     */
    public void crear(EntityManager em, VigenciasProrrateosProyectos vigenciasProrrateosProyectos);
    /**
     * Método encargado de modificar una VigenciaProrrateoProyecto de la base de datos.
     * Este método recibe la información del parámetro para hacer un 'merge' con la 
     * información de la base de datos.
     * @param vigenciasProrrateosProyectos VigenciaProrrateoProyecto con los cambios que se van a realizar.
     */
    public void editar(EntityManager em, VigenciasProrrateosProyectos vigenciasProrrateosProyectos);
    /**
     * Método encargado de eliminar de la base de datos la VigenciaProrrateoProyecto que entra por parámetro.
     * @param vigenciasProrrateosProyectos VigenciaProrrateoProyecto que se quiere eliminar.
     */
    public void borrar(EntityManager em, VigenciasProrrateosProyectos vigenciasProrrateosProyectos);
    /**
     * Método encargado de buscar todas las VigenciasProrrateosProyectos existentes en la base de datos.
     * @return Retorna una lista de VigenciasProrrateosProyectos.
     */
    public List<VigenciasProrrateosProyectos> buscarVigenciasProrrateosProyectos(EntityManager em );
    /**
     * Método encargado de buscar las VigenciasProrrateosProyectos de un Empleado específico.
     * @param secuencia Secuencia del Empleado.
     * @return Retorna las VigenciasProrrateosProyectos, odenadas descendentemente por la fechaInicial, del Empleado cuya secuencia coincide 
     * con la secuencia dada por parámetro.
     */
    public List<VigenciasProrrateosProyectos> buscarVigenciasProrrateosProyectosEmpleado(EntityManager em, BigInteger secuencia);
    /**
     * Método encargado de buscar la VigenciaProrrateoProyecto con la secuencia dada por parámetro.
     * @param secuencia Secuencia de la VigenciaProrrateoProyecto que se quiere encontrar.
     * @return Retorna la VigenciaProrrateoProyecto identificada con la secuencia dada por parámetro.
     */
    public VigenciasProrrateosProyectos buscarVigenciasProrrateosProyectosSecuencia(EntityManager em, BigInteger secuencia);
    /**
     * Método encargado de buscar las VigenciaProrrateoProyecto asociadas a una VigenciaLocalizacion específica.
     * @param secuencia Secuencia de la VigenciaLocalizacion.
     * @return Retorna la lista de VigenciasProrrateosProyectos que estan asociadas con la VigenciaLocalizacion
     * cuya secuencia coincide con la secuencia dada por parámetro. 
     */
    public List<VigenciasProrrateosProyectos> buscarVigenciasProrrateosProyectosVigenciaSecuencia(EntityManager em, BigInteger secuencia);
    
}
