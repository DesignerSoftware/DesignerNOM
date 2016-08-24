/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.Eventos;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la
 * tabla 'Eventos' de la base de datos.
 *
 * @author betelgeuse
 */
public interface PersistenciaEventosInterface {

    /**
     * Método encargado de insertar un Evento en la base de datos.
     *
     * @param eventos Evento que se quiere crear.
     */
    public void crear(EntityManager em,Eventos eventos);

    /**
     * Método encargado de modificar un Evento de la base de datos. Este método
     * recibe la información del parámetro para hacer un 'merge' con la
     * información de la base de datos.
     *
     * @param eventos Evento con los cambios que se van a realizar.
     */
    public void editar(EntityManager em,Eventos eventos);

    /**
     * Método encargado de eliminar de la base de datos el Evento que entra por
     * parámetro.
     *
     * @param eventos Evento que se quiere eliminar.
     */
    public void borrar(EntityManager em,Eventos eventos);

    /**
     * Método encargado de buscar el Evento con la secEventos dada por
     * parámetro.
     *
     * @param secEventos Secuencia del Evento que se quiere encontrar.
     * @return Retorna el Evento identificado con la secEventos dada por
     * parámetro.
     */
    public Eventos buscarEvento(EntityManager em,BigInteger secEventos);

    /**
     * Método encargado de buscar todos los Eventos existentes en la base de
     * datos.
     *
     * @return Retorna una lista de Eventos.
     */
    public List<Eventos> buscarEventos(EntityManager em);

    /**
     * Método encargado de revisar si existe una relacion entre un Evento
     * específica y algúna VigenciaEvento. Adémas de la revisión, cuenta cuantas
     * relaciones existen.
     *
     * @param secEventos Secuencia del Evento.
     * @return Retorna el número de VigenciasEventos relacionados con el Evento
     * cuya secEventos coincide con el parámetro.
     */
    public BigInteger contadorVigenciasEventos(EntityManager em,BigInteger secEventos);
}
