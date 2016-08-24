/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.VigenciasProyectos;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la tabla 'VigenciasProyectos' 
 * de la base de datos.
 * @author AndresPineda
 */
public interface PersistenciaVigenciasProyectosInterface {
    /**
     * Método encargado de insertar una VigenciaProyecto en la base de datos.
     * @param vigenciasProyectos VigenciaProyecto que se quiere crear.
     */
    public void crear(EntityManager em, VigenciasProyectos vigenciasProyectos);
    /**
     * Método encargado de modificar una VigenciaProyecto de la base de datos.
     * Este método recibe la información del parámetro para hacer un 'merge' con la 
     * información de la base de datos.
     * @param vigenciasProyectos VigenciaProyecto con los cambios que se van a realizar.
     */
    public void editar(EntityManager em, VigenciasProyectos vigenciasProyectos);
    /**
     * Método encargado de eliminar de la base de datos la VigenciaProyecto que entra por parámetro.
     * @param vigenciasProyectos VigenciaProyecto que se quiere eliminar.
     */
    public void borrar(EntityManager em, VigenciasProyectos vigenciasProyectos);
    /**
     * Método encargado de buscar todas las VigenciasProyectos existentes en la base de datos.
     * @return Retorna una lista de VigenciasProyectos.
     */
    public List<VigenciasProyectos> buscarVigenciasProyectos(EntityManager em );
    /**
     * Método encargado de buscar las VigenciasProyectos de un Empleado específico.
     * @param secuencia Secuencia del Empleado.
     * @return Retorna las VigenciasProyectos, odenadas descendentemente por la fechaInicial, del Empleado cuya secuencia coincide 
     * con la secuencia dada por parámetro.
     */
    public List<VigenciasProyectos> vigenciasProyectosEmpleado(EntityManager em, BigInteger secuencia);
    /**
     * Método encargado de buscar las últimas VigenciasProyectos registradas de un Empleado específico.
     * @param secuencia Secuencia del Empleado.
     * @return Retorna las VigenciasProyectos cuya fechaInicial es la mayor fechaInicial para el Empleado cuya secuencia coincide 
     * con la secuencia dada por parámetro.
     */
    public List<VigenciasProyectos> proyectosEmpleado(EntityManager em, BigInteger secuencia);
}
