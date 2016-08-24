/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.PryClientes;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la
 * tabla 'PryClientes' de la base de datos.
 *
 * @author Viktor
 */
public interface PersistenciaPryClientesInterface {

    /**
     * Método encargado de insertar un PryCliente en la base de datos.
     *
     * @param pryClientes Moneda que se quiere crear.
     */
    public void crear(EntityManager em, PryClientes pryClientes);

    /**
     * Método encargado de modificar un PryCliente de la base de datos. Este
     * método recibe la información del parámetro para hacer un 'merge' con la
     * información de la base de datos.
     *
     * @param pryClientes PryClientes con los cambios que se van a realizar.
     */
    public void editar(EntityManager em, PryClientes pryClientes);

    /**
     * Método encargado de eliminar de la base de datos un PryCliente que entra
     * por parámetro.
     *
     * @param pryClientes PryClientes que se quiere eliminar.
     */
    public void borrar(EntityManager em, PryClientes pryClientes);

    /**
     * Método encargado de buscar el PryCliente con la secPryClientes dada por
     * parámetro.
     *
     * @param secPryClientes Secuencia del PryCliente que se quiere encontrar.
     * @return Retorna el PryCliente identificado con la secPryClientes dada por
     * parámetro.
     */
    public PryClientes buscarPryCliente(EntityManager em, BigInteger secPryClientes);

    /**
     * Método encargado de revisar si existe una relacion entre una Moneda
     * específica y algún Proyecto. Adémas de la revisión, cuenta cuantas
     * relaciones existen.
     *
     * @param secPryClientes Secuencia del PryCliente.
     * @return Retorna el número de proyectos relacionados con el PryCliente
     * cuya secPryClientes coincide con el parámetro.
     */
    public BigInteger contadorProyectos(EntityManager em, BigInteger secPryClientes);

    /**
     * Método encargado de buscar todos los PryClientes existentes en la base de
     * datos, ordenados por nombre.
     *
     * @return Retorna una lista de PryClientes ordenados por nombre.
     */
    public List<PryClientes> buscarPryClientes(EntityManager em);

}
