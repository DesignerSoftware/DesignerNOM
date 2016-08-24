/**
 * Documentación a cargo de Hugo David Sin Gutiérrez && John Steven Pineda Muñoz
 */
package InterfacePersistencia;

import Entidades.Monedas;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la
 * tabla 'Monedas' de la base de datos.
 *
 * @author Viktor
 */
public interface PersistenciaMonedasInterface {


    /**
     * Método encargado de buscar todas las Monedas existentes en la base de
     * datos.
     *
     * @return Retorna una lista de Monedas.
     */
    public List<Monedas> consultarMonedas(EntityManager em);

    /**
     * Método encargado de insertar una Moneda en la base de datos.
     *
     * @param monedas Nibeda que se quiere crear.
     */
    public void crear(EntityManager em, Monedas monedas);

    /**
     * Método encargado de modificar una Moneda de la base de datos. Este método
     * recibe la información del parámetro para hacer un 'merge' con la
     * información de la base de datos.
     *
     * @param monedas Monedas con los cambios que se van a realizar.
     */
    public void editar(EntityManager em, Monedas monedas);

    /**
     * Método encargado de eliminar de la base de datos una Moneda que entra por
     * parámetro.
     *
     * @param monedas Monedas que se quiere eliminar.
     */
    public void borrar(EntityManager em, Monedas monedas);

    /**
     * Método encargado de buscar una Moneda con la secuencia dada por
     * parámetro.
     *
     * @param secuencia Secuencia del Moneda que se quiere encontrar.
     * @return Retorna la Moneda identificado con la secuencia dada por
     * parámetro.
     */
    public Monedas consultarMoneda(EntityManager em, BigInteger secuencia);

    /**
     * Método encargado de revisar si existe una relacion entre una Moneda
     * específica y algún Proyecto. Adémas de la revisión, cuenta cuantas
     * relaciones existen.
     *
     * @param secuencia Secuencia de la Moneda.
     * @return Retorna el número de proyectos relacionados con la moneda cuya
     * secuencia coincide con el parámetro.
     */
    public BigInteger contadorProyectos(EntityManager em, BigInteger secuencia);
}
