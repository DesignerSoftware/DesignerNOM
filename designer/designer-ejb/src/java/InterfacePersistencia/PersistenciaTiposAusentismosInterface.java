/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.Tiposausentismos;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la
 * tabla 'TiposAusentismos' de la base de datos.
 *
 * @author betelgeuse
 */
public interface PersistenciaTiposAusentismosInterface {

    /**
     * Método encargado de insertar un TipoAusentismo en la base de datos.
     *
     * @param tiposAusentismos TipoAusentismo que se quiere crear.
     */
    public void crear(EntityManager em, Tiposausentismos tiposAusentismos);

    /**
     * Método encargado de modificar un TipoAusentismo de la base de datos. Este
     * método recibe la información del parámetro para hacer un 'merge' con la
     * información de la base de datos.
     *
     * @param tiposAusentismos TipoAusentismo con los cambios que se van a
     * realizar.
     */
    public void editar(EntityManager em, Tiposausentismos tiposAusentismos);

    /**
     * Método encargado de eliminar de la base de datos el TipoAusentismo que
     * entra por parámetro.
     *
     * @param tiposAusentismos TipoAusentismo que se quiere eliminar.
     */
    public void borrar(EntityManager em, Tiposausentismos tiposAusentismos);

    /**
     * Método encargado de buscar todos los TiposAusentismos existentes en la
     * base de datos, ordenados por código.
     *
     * @return Retorna una lista de TiposAusentismos ordenados por código.
     */
    public List<Tiposausentismos> consultarTiposAusentismos(EntityManager em);

    /**
     * Método encargado de buscar una TipoAusentismo con la secTipoAusentismo
     * dada por parámetro.
     *
     * @param secTiposAusentismos secTiposAusentismos de la TipoAusentismo que
     * se quiere encontrar.
     * @return Retorna el secTiposAusentismos identificada con la secCategoria
     * dada por parámetro.
     */
    public Tiposausentismos consultarTipoAusentismo(EntityManager em, BigInteger secTiposAusentismos);

    /**
     * Metodo encargado de contar cuantas Categorias estar relacionadas con la
     * secTiposAusentismos de la TipoAusentismo
     *
     * @param secTiposAusentismos Secuencia de la TipoAusentismo
     * @return el numero de relaciones que tiene esa secuencia
     */
    public BigInteger contarClasesAusentimosTipoAusentismo(EntityManager em, BigInteger secTiposAusentismos);

    /**
     * Metodo encargado de contar cuantas Categorias estar relacionadas con la
     * secTiposAusentismos de la TipoAusentismo
     *
     * @param secTiposAusentismos Secuencia de la TipoAusentismo
     * @return el numero de relaciones que tiene esa secuencia
     */
    public BigInteger contarSOAusentimosTipoAusentismo(EntityManager em, BigInteger secTiposAusentismos);
}
