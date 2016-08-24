/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.MetodosPagos;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la
 * tabla 'MetodosPagos' de la base de datos.
 *
 * @author betelgeuse
 */
public interface PersistenciaMetodosPagosInterface {

    /**
     * Método encargado de insertar un MetodoPago en la base de datos.
     *
     * @param metodosPagos MetodoPago que se quiere crear.
     */
    public void crear(EntityManager em, MetodosPagos metodosPagos);

    /**
     * Método encargado de modificar un MetodoPago de la base de datos. Este
     * método recibe la información del parámetro para hacer un 'merge' con la
     * información de la base de datos.
     *
     * @param metodosPagos MetodoPago con los cambios que se van a realizar.
     */
    public void editar(EntityManager em, MetodosPagos metodosPagos);

    /**
     * Método encargado de eliminar de la base de datos el MetodoPago que entra
     * por parámetro.
     *
     * @param metodosPagos MetodoPago que se quiere eliminar.
     */
    public void borrar(EntityManager em, MetodosPagos metodosPagos);

    /**
     * Método encargado de buscar el MetodoPago con la secMetodosPagos dada por
     * parámetro.
     *
     * @param secMetodosPagos secMetodosPagos del MetodoPago que se quiere
     * encontrar.
     * @return Retorna el MetodoPago identificado con la secMetodosPagos dada
     * por parámetro.
     */
    public MetodosPagos buscarMetodosPagosEmpleado(EntityManager em, BigInteger secMetodosPagos);

    /**
     * Método encargado de buscar todos los MetodosPagos existentes en la base
     * de datos.
     *
     * @return Retorna una lista de MetodosPagos.
     */
    public List<MetodosPagos> buscarMetodosPagos(EntityManager em);

    /**
     * Método encargado de revisar si existe una relacion entre una MetodoPago
     * específica y algúna VigenciaFormaPago. Adémas de la revisión, cuenta
     * cuantas relaciones existen.
     *
     * @param secMetodosPagos secMetodosPagos del MetodoPago.
     * @return Retorna el número de VigenciasFormasPagos relacionados con el
     * MetodoPago cuya secMetodosPagos coincide con el parámetro.
     */
    public BigInteger contadorvigenciasformaspagos(EntityManager em, BigInteger secMetodosPagos);
}
