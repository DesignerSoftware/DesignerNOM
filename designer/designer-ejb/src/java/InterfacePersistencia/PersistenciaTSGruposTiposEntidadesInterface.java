/**
 * Documentación a cargo de AndresPineda
 */
package InterfacePersistencia;

import Entidades.TSGruposTiposEntidades;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la
 * tabla 'TSGruposTiposEntidades' de la base de datos.
 *
 * @author AndresPineda
 */
public interface PersistenciaTSGruposTiposEntidadesInterface {

    /**
     * Método encargado de insertar un TSGrupoTipoEntidad en la base de datos.
     *
     * @param tSGruposTiposEntidades TSGrupoTipoEntidad que se quiere crear.
     */
    public void crear(EntityManager em, TSGruposTiposEntidades tSGruposTiposEntidades);

    /**
     * Método encargado de modificar un TSGrupoTipoEntidad de la base de datos.
     * Este método recibe la información del parámetro para hacer un 'merge' con
     * la información de la base de datos.
     *
     * @param tSGruposTiposEntidades TSGrupoTipoEntidad con los cambios que se
     * van a realizar.
     */
    public void editar(EntityManager em, TSGruposTiposEntidades tSGruposTiposEntidades);

    /**
     * Método encargado de eliminar de la base de datos el TSGrupoTipoEntidad
     * que entra por parámetro.
     *
     * @param tSGruposTiposEntidades TSGrupoTipoEntidad que se quiere eliminar.
     */
    public void borrar(EntityManager em, TSGruposTiposEntidades tSGruposTiposEntidades);

    /**
     * Método encargado de buscar todos los TSGruposTiposEntidades existentes en la base
     * de datos.
     *
     * @return Retorna una lista de TSGruposTiposEntidades.
     */
    public List<TSGruposTiposEntidades> buscarTSGruposTiposEntidades(EntityManager em);

    /**
     * Método encargado de buscar el TSGrupoTipoEntidad con la secuencia dada por
     * parámetro.
     *
     * @param secTSGrupo Secuencia del TSGruposTiposEntidades que se quiere encontrar.
     * @return Retorna el TSGrupoTipoEntidad identificado con la secuencia dada por
     * parámetro.
     */
    public TSGruposTiposEntidades buscarTSGrupoTipoEntidadSecuencia(EntityManager em, BigInteger secTSGrupo);

    /**
     * Método encargado de buscar todos los TSGruposTiposEntidades existentes en la base
     * de datos para un TipoSueldo dado por parametro.
     *
     * @param secTipoSueldo Secuencia del TipoSueldo.
     * @return Retorna una lista de TSGruposTiposEntidades para el TipoSueldo dado por parametro.
     */
    public List<TSGruposTiposEntidades> buscarTSGruposTiposEntidadesPorSecuenciaTipoSueldo(EntityManager em, BigInteger secTipoSueldo);

}
