/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.TiposEntidades;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la
 * tabla 'TiposEntidades' de la base de datos.
 *
 * @author betelgeuse
 */
public interface PersistenciaTiposEntidadesInterface {

    /**
     * Método encargado de insertar un TipoEntidad en la base de datos.
     *
     * @param tiposEntidades TipoEntidad que se quiere crear.
     */
    public void crear(EntityManager em, TiposEntidades tiposEntidades);

    /**
     * Método encargado de modificar un TipoEntidad de la base de datos. Este
     * método recibe la información del parámetro para hacer un 'merge' con la
     * información de la base de datos.
     *
     * @param tiposEntidades TipoEntidad con los cambios que se van a realizar.
     */
    public void editar(EntityManager em, TiposEntidades tiposEntidades);

    /**
     * Método encargado de eliminar de la base de datos el TipoEntidad que entra
     * por parámetro.
     *
     * @param tiposEntidades TipoEntidad que se quiere eliminar.
     */
    public void borrar(EntityManager em, TiposEntidades tiposEntidades);

    /**
     * Método encargado de buscar todos los TiposEntidades existentes en la base
     * de datos.
     *
     * @return Retorna una lista de TiposEntidades.
     */
    public List<TiposEntidades> buscarTiposEntidades(EntityManager em);

    /**
     * Método encargado de buscar el TipoEntidad con la secTipoEntidad dada por
     * parámetro.
     *
     * @param secTipoEntidad Secuencia del TipoEntidad que se quiere encontrar.
     * @return Retorna el TipoEntidad identificado con la secTipoEntidad dada
     * por parámetro.
     */
    public TiposEntidades buscarTiposEntidadesSecuencia(EntityManager em, BigInteger secTipoEntidad);

    /**
     * Método encargado de verificar si existe un TipoEntidad cuya
     * secTipoEntidad coincide con la dada por parámetro y esta asociada con
     * alguna VigenciaAfiliacion.
     *
     * @param secTipoEntidad Secuencia del TipoEntidad.
     * @return Retorna el numero de VigenciasAfiliaciones que tienen asociadas
     * el TipoEntidad. Si no encuentra ningún dato, retorna null.
     */
    public BigInteger verificarBorrado(EntityManager em, BigInteger secTipoEntidad);

    /**
     * Método encargado de verificar si existe un TipoEntidad cuya
     * secTipoEntidad coincide con la dada por parámetro y esta asociada con
     * alguna FormulaContratoEntidad.
     *
     * @param secTipoEntidad Secuencia del TipoEntidad.
     * @return Retorna el numero de FormulasContratosEntidades que tienen
     * asociadas el TipoEntidad. Si no encuentra ningún dato, retorna null.
     */
    public BigInteger verificarBorradoFCE(EntityManager em, BigInteger secTipoEntidad);

    /**
     * Método encargado de buscar todos los TiposEntidades, si y solo si existe
     * un GrupoTipoEntidad con código entre 1 y 8 asociado a uno de los
     * TiposEntidades.
     *
     * @return Retorna una lista de TiposEntidades si se cumplen las condiciones
     * anteriormente mencionadas, de lo contrario retorna null.
     */
    public List<TiposEntidades> buscarTiposEntidadesIBCS(EntityManager em);

    /**
     * Método encargado de buscar los TiposEntidades con la secuencia del
     * GrupoTipoEntidad.
     *
     * @param secuencia Secuencia del GrupoTipoEntidad.
     * @return Retorna los TiposEntidades identificado con la secuencia del
     * GrupoTipoEntidad.
     */
    public List<TiposEntidades> buscarTiposEntidadesPorSecuenciaGrupo(EntityManager em, BigInteger secuencia);

    public TiposEntidades buscarTipoEntidadPorCodigo(EntityManager em, Short codigo);

    public List<TiposEntidades> buscarTiposEntidadesParametroAutoliq(EntityManager em);

}
