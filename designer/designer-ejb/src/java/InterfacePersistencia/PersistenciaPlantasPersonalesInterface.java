/**
 * Documentación a cargo de AndresPineda
 */
package InterfacePersistencia;

import Entidades.PlantasPersonales;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la
 * tabla 'PlantasPersonales' de la base de datos.
 *
 * @author Andres Pineda.
 */
public interface PersistenciaPlantasPersonalesInterface {

    /**
     * Método encargado de insertar una PlantaPersonal en la base de datos.
     *
     * @param plantasPersonales PlantaPersonal que se quiere crear.
     */
    public void crear(EntityManager em, PlantasPersonales plantasPersonales);

    /**
     * Método encargado de modificar una PlantaPersonal de la base de datos.
     * Este método recibe la información del parámetro para hacer un 'merge' con
     * la información de la base de datos.
     *
     * @param plantasPersonales PlantaPersonal con los cambios que se van a
     * realizar.
     */
    public void editar(EntityManager em, PlantasPersonales plantasPersonales);

    /**
     * Método encargado de eliminar de la base de datos la PlantaPersonal que
     * entra por parámetro.
     *
     * @param plantasPersonales PlantaPersonal que se quiere eliminar.
     */
    public void borrar(EntityManager em, PlantasPersonales plantasPersonales);

    /**
     * Método encargado de buscar todos las PlantasPersonales existentes en la
     * base de datos.
     *
     * @return Retorna una lista de PlantasPersonales.
     */
    public List<PlantasPersonales> consultarPlantasPersonales(EntityManager em);

    /**
     * Método encargado de sumar en la tabla PlantasPersonales la cantidad de
     * veces que se encuentra registrado una secuencia de una Estructura
     *
     * @param secEstructura Secuencia Estructura
     * @return Conteo de la Estructura en la tabla PlantasPersonales
     */
    public BigInteger consultarCantidadEstructuras(EntityManager em, BigInteger secEstructura);

}
