/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.VigenciasTiposTrabajadores;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la
 * tabla 'VigenciasTiposTrabajadores' de la base de datos.
 *
 * @author betelgeuse
 */
public interface PersistenciaVigenciasTiposTrabajadoresInterface {

    /**
     * Método encargado de insertar una VigenciaTipoTrabajador en la base de
     * datos.
     *
     * @param vigenciasTiposTrabajadores VigenciaTipoTrabajador que se quiere
     * crear.
     */
    public void crear(EntityManager em, VigenciasTiposTrabajadores vigenciasTiposTrabajadores);

    /**
     * Método encargado de modificar una VigenciaTipoTrabajador de la base de
     * datos. Este método recibe la información del parámetro para hacer un
     * 'merge' con la información de la base de datos.
     *
     * @param vigenciasTiposTrabajadores VigenciaTipoTrabajador con los cambios
     * que se van a realizar.
     */
    public void editar(EntityManager em, VigenciasTiposTrabajadores vigenciasTiposTrabajadores);

    /**
     * Método encargado de eliminar de la base de datos la
     * VigenciaTipoTrabajador que entra por parámetro.
     *
     * @param vigenciasTiposTrabajadores VigenciaTipoTrabajador que se quiere
     * eliminar.
     */
    public void borrar(EntityManager em, VigenciasTiposTrabajadores vigenciasTiposTrabajadores);

    /**
     * Método encargado de buscar todas las VigenciasTiposTrabajadores
     * existentes en la base de datos.
     *
     * @return Retorna una lista de VigenciasTiposTrabajadores.
     */
    public List<VigenciasTiposTrabajadores> buscarVigenciasTiposTrabajadores(EntityManager em);

    /**
     * Método encargado de buscar las VigenciasTiposTrabajadores de un Empleado
     * específico.
     *
     * @param secuencia Secuencia del Empleado.
     * @return Retorna las VigenciasTiposTrabajadores, odenadas descendentemente
     * por la fechaVigencia, del Empleado cuya secuencia coincide con la
     * secuencia dada por parámetro.
     */
    public List<VigenciasTiposTrabajadores> buscarVigenciasTiposTrabajadoresEmpleado(EntityManager em, BigInteger secuencia);

    /**
     * Método encargado de buscar la VigenciaTipoTrabajador con la secuencia
     * dada por parámetro.
     *
     * @param secuencia Secuencia de la VigenciaTipoTrabajador que se quiere
     * encontrar.
     * @return Retorna la VigenciaTipoTrabajador identificada con la secuencia
     * dada por parámetro.
     */
    public VigenciasTiposTrabajadores buscarVigenciasTiposTrabajadoresSecuencia(EntityManager em, BigInteger secuencia);

    public List<VigenciasTiposTrabajadores> buscarEmpleados(EntityManager em);

    public VigenciasTiposTrabajadores buscarVigenciaTipoTrabajadorRestriccionUN(EntityManager em, BigInteger empleado, Date fechaVigencia, BigInteger tipoTrabajador);
}
