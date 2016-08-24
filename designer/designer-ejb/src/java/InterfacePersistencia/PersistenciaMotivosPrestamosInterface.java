/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.MotivosPrestamos;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la
 * tabla 'MotivosPrestamos' de la base de datos.
 *
 * @author John Pineda.
 */
public interface PersistenciaMotivosPrestamosInterface {

    /**
     * Método encargado de insertar un MotivoPrestamo en la base de datos.
     *
     * @param motivosPrestamos MotivoPrestamo que se quiere crear.
     */
    public void crear(EntityManager em, MotivosPrestamos motivosPrestamos);

    /**
     * Método encargado de modificar un MotivoPrestamo de la base de datos. Este
     * método recibe la información del parámetro para hacer un 'merge' con la
     * información de la base de datos.
     *
     * @param motivosPrestamos MotivoPrestamo con los cambios que se van a
     * realizar.
     */
    public void editar(EntityManager em, MotivosPrestamos motivosPrestamos);

    /**
     * Método encargado de eliminar de la base de datos el MotivoPrestamo que
     * entra por parámetro.
     *
     * @param motivosPrestamos MotivoPrestamo que se quiere eliminar.
     */
    public void borrar(EntityManager em, MotivosPrestamos motivosPrestamos);

    /**
     * Método encargado de buscar el MotivoPrestamo con la secMotivosPrestamos dada por
     * parámetro.
     *
     * @param secMotivosPrestamos Secuencia del MotivoPrestamo que se quiere encontrar.
     * @return Retorna el MotivoPrestamo identificado con la secMotivosPrestamos dada por
     * parámetro.
     */
    public MotivosPrestamos buscarMotivoPrestamo(EntityManager em, BigInteger secMotivosPrestamos);

    /**
     * Método encargado de buscar todos los MotivosPrestamos existentes en la
     * base de datos.
     *
     * @return Retorna una lista de MotivosPrestamos.
     */
    public List<MotivosPrestamos> buscarMotivosPrestamos(EntityManager em);

    /**
     * Método encargado de contar los EersPrestamos que están asociados a un
     * MotivoPrestamo específico.
     *
     * @param secMotivosPrestamos Secuencia del MotivoPrestamo.
     * @return Retorna la cantidad de EersPrestamos cuyo MotivoPrestamo tiene
     * como secMotivosPrestamos el valor dado por parámetro.
     */
    public BigInteger contadorEersPrestamos(EntityManager em, BigInteger secMotivosPrestamos);
}
