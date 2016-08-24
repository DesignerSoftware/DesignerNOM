/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.EstadosCiviles;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la
 * tabla 'EstadosCiviles' de la base de datos.
 *
 * @author betelgeuse
 */
public interface PersistenciaEstadosCivilesInterface {

    /**
     * Método encargado de insertar un EstadoCivil en la base de datos.
     *
     * @param estadosCiviles EstadoCivil que se quiere crear.
     */
    public void crear(EntityManager em,EstadosCiviles estadosCiviles);

    /**
     * Método encargado de modificar un EstadoCivil de la base de datos. Este
     * método recibe la información del parámetro para hacer un 'merge' con la
     * información de la base de datos.
     *
     * @param estadosCiviles EstadoCivil con los cambios que se van a realizar.
     */
    public void editar(EntityManager em,EstadosCiviles estadosCiviles);

    /**
     * Método encargado de eliminar de la base de datos el EstadoCivil que entra
     * por parámetro.
     *
     * @param estadosCiviles EstadoCivil que se quiere eliminar.
     */
    public void borrar(EntityManager em,EstadosCiviles estadosCiviles);

    /**
     * Método encargado de buscar el EstadoCivil con la secEstadoCivil dada por
     * parámetro.
     *
     * @param secEstadoCivil Identificador único del EstadoCivil que se quiere
     * encontrar.
     * @return Retorna el EstadoCivil identificado con la secEstadoCivil dada
     * por parámetro.
     */
    public EstadosCiviles buscarEstadoCivil(EntityManager em,BigInteger secEstadoCivil);

    /**
     * Método encargado de buscar todos los EstadosCiviles existentes en la base
     * de datos.
     *
     * @return Retorna una lista de EstadosCiviles
     */
    public List<EstadosCiviles> consultarEstadosCiviles(EntityManager em);

    /**
     * Método encargado de revisar si existe una relacion entre un EstadoCivil
     * específica y algúna VigenciaEstadoCivil. Adémas de la revisión, cuenta
     * cuantas relaciones existen.
     *
     * @param secEstadoCivil secEstadoCivil del EstadoCivil.
     * @return Retorna el número de VigenciasEstadosCiviles relacionados con el
     * EstadoCivil cuya secEstadoCivil coincide con el parámetro.
     */
    public BigInteger contadorVigenciasEstadosCiviles(EntityManager em,BigInteger secEstadoCivil);
}
