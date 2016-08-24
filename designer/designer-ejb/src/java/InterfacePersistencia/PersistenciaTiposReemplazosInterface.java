/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.TiposReemplazos;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la
 * tabla 'TiposReemplazos' de la base de datos.
 *
 * @author betelgeuse
 */
public interface PersistenciaTiposReemplazosInterface {

    /**
     * Método encargado de insertar un TipoReemplazo en la base de datos.
     *
     * @param tiposReemplazos TipoReemplazo que se quiere crear.
     */
    public void crear(EntityManager em, TiposReemplazos tiposReemplazos);

    /**
     * Método encargado de modificar un TipoReemplazo de la base de datos. Este
     * método recibe la información del parámetro para hacer un 'merge' con la
     * información de la base de datos.
     *
     * @param tiposReemplazos TiposReemplazos con los cambios que se van a
     * realizar.
     */
    public void editar(EntityManager em, TiposReemplazos tiposReemplazos);

    /**
     * Método encargado de eliminar de la base de datos un TipoReemplazo que
     * entra por parámetro.
     *
     * @param tiposReemplazos TiposReemplazos que se quiere eliminar.
     */
    public void borrar(EntityManager em, TiposReemplazos tiposReemplazos);

    /**
     * Método encargado de buscar un TipoReemplazo con la secTiposReemplazos dada por
     * parámetro.
     *
     * @param secTiposReemplazos Secuencia del TipoReemplazo que se quiere encontrar.
     * @return Retorna la Moneda identificada con la secTiposReemplazos dada por
     * parámetro.
     */
    public TiposReemplazos buscarTipoReemplazo(EntityManager em, BigInteger secTiposReemplazos);

    /**
     * Método encargado de buscar todos los TiposReemplazos existentes en la
     * base de datos, ordenados por código.
     *
     * @return Retorna una lista de TiposReemplazos ordenados por código.
     */
    public List<TiposReemplazos> buscarTiposReemplazos(EntityManager em );

    /**
     * Método encargado de revisar si existe una relacion entre un TipoReemplazo
     * específica y algúna Encagatura. Adémas de la revisión, cuenta cuantas
     * relaciones existen.
     *
     * @param secTiposReemplazos Secuencia de la Moneda.
     * @return Retorna el número de proyectos relacionados con el TipoReemplazo
     * cuya secTiposReemplazos coincide con el parámetro.
     */
    public BigInteger contadorEncargaturas(EntityManager em, BigInteger secTiposReemplazos);

    /**
     * Método encargado de revisar si existe una relacion entre un TipoReemplazo
     * específica y algúna contadorProgramacionesTiempos. Adémas de la revisión,
     * cuenta cuantas relaciones existen.
     *
     * @param secTiposReemplazos Secuencia de la Moneda.
     * @return Retorna el número de proyectos relacionados con el TipoReemplazo
     * cuya secTiposReemplazos coincide con el parámetro.
     */
    public BigInteger contadorProgramacionesTiempos(EntityManager em, BigInteger secTiposReemplazos);

    /**
     * Método encargado de revisar si existe una relacion entre un TipoReemplazo
     * específica y algúna contadorReemplazos. Adémas de la revisión, cuenta
     * cuantas relaciones existen.
     *
     * @param secTiposReemplazos Secuencia de la Moneda.
     * @return Retorna el número de proyectos relacionados con el TipoReemplazo
     * cuya secTiposReemplazos coincide con el parámetro.
     */
    public BigInteger contadorReemplazos(EntityManager em, BigInteger secTiposReemplazos);
}
