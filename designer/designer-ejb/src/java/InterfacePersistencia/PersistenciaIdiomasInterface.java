/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.Idiomas;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la
 * tabla 'Idiomas' de la base de datos.
 *
 * @author betelgeuse
 */
public interface PersistenciaIdiomasInterface {

    /**
     * Método encargado de insertar un Idioma en la base de datos.
     *
     * @param idiomas Idioma que se quiere crear.
     */
    public void crear(EntityManager em, Idiomas idiomas);

    /**
     * Método encargado de modificar un Idioma de la base de datos. Este método
     * recibe la información del parámetro para hacer un 'merge' con la
     * información de la base de datos.
     *
     * @param idiomas Idioma con los cambios que se van a realizar.
     */
    public void editar(EntityManager em, Idiomas idiomas);

    /**
     * Método encargado de eliminar de la base de datos el Idioma que entra por
     * parámetro.
     *
     * @param idiomas Idioma que se quiere eliminar.
     */
    public void borrar(EntityManager em, Idiomas idiomas);

    /**
     * Método encargado de buscar todos los Idiomas existentes en la base de
     * datos.
     *
     * @return Retorna una lista de Idiomas.
     */
    public List<Idiomas> buscarIdiomas(EntityManager em);

    /**
     * Método encargado de buscar un Idioma con la secuencia dada por parámetro.
     *
     * @param secIdiomas Secuencia del Idioma que se quiere encontrar.
     * @return Retorna el Idioma identificado con la secuencia dada por
     * parámetro.
     */
    public Idiomas buscarIdioma(EntityManager em, BigInteger secIdiomas);

    /**
     * Método encargado de revisar si existe una relacion entre un Idioma
     * específica y algún IdiomaPersona. Adémas de la revisión, cuenta cuantas
     * relaciones existen.
     *
     * @param secIdiomas Secuencia del Idioma.
     * @return Retorna el número de IdiomasPersonas relacionados con un Idioma
     * cuya secIdiomas coincide con el parámetro.
     */
    public BigInteger contadorIdiomasPersonas(EntityManager em, BigInteger secIdiomas);
}
