/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.Grupostiposentidades;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la
 * tabla 'GruposTiposEntidades' de la base de datos.
 *
 * @author betelgeuse
 */
@Local
public interface PersistenciaGruposTiposEntidadesInterface {

    /**
     * Método encargado de insertar un GrupoTipoEntidad en la base de datos.
     *
     * @param gruposTiposEntidades GrupoTipoEntidad que se quiere crear.
     */
    public void crear(EntityManager em,Grupostiposentidades gruposTiposEntidades);

    /**
     * Método encargado de modificar un GrupoTipoEntidad de la base de datos.
     * Este método recibe la información del parámetro para hacer un 'merge' con
     * la información de la base de datos.
     *
     * @param gruposTiposEntidades GrupoTipoEntidad con los cambios que se van a
     * realizar.
     */
    public void editar(EntityManager em,Grupostiposentidades gruposTiposEntidades);

    /**
     * Método encargado de eliminar de la base de datos el GrupoTipoEntidad que
     * entra por parámetro.
     *
     * @param gruposTiposEntidades GrupoTipoEntidad que se quiere eliminar.
     */
    public void borrar(EntityManager em,Grupostiposentidades gruposTiposEntidades);

    /**
     * Método encargado de buscar el GrupoTipoEntidad con la secuencia dada por
     * parámetro.
     *
     * @param secuencia Secuencia del GrupoTipoEntidad que se quiere encontrar.
     * @return Retorna el GrupoTipoEntidad identificado con la secuencia dada
     * por parámetro.
     */
    public Grupostiposentidades consultarGrupoTipoEntidad(EntityManager em,BigInteger secuencia);

    /**
     * Método encargado de buscar todos los GruposTiposEntidades existentes en
     * la base de datos.
     *
     * @return Retorna una lista de GruposTiposEntidades.
     */
    public List<Grupostiposentidades> consultarGruposTiposEntidades(EntityManager em);

    public BigInteger contarTiposEntidadesGrupoTipoEntidad(EntityManager em,BigInteger secuencia);

    public BigInteger contarTSgruposTiposEntidadesTipoEntidad(EntityManager em,BigInteger secuencia);
}
