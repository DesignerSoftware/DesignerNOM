/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.PryPlataformas;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la
 * tabla 'PryPlataformas' de la base de datos.
 *
 * @author betelgeuse
 */
public interface PersistenciaPryPlataformasInterface {

    /**
     * Método encargado de insertar un PryPlataforma en la base de datos.
     *
     * @param plataformas PryPlataforma que se quiere crear.
     */
    public void crear(EntityManager em, PryPlataformas plataformas);

    /**
     * Método encargado de modificar un PryPlataforma de la base de datos. Este
     * método recibe la información del parámetro para hacer un 'merge' con la
     * información de la base de datos.
     *
     * @param plataformas PryPlataforma con los cambios que se van a realizar.
     */
    public void editar(EntityManager em, PryPlataformas plataformas);

    /**
     * Método encargado de eliminar de la base de datos el PryPlataforma que
     * entra por parámetro.
     *
     * @param plataformas PryPlataforma que se quiere eliminar.
     */
    public void borrar(EntityManager em, PryPlataformas plataformas);

    /**
     * Método encargado de buscar todos los PryPlataformas existentes en la base
     * de datos.
     *
     * @return Retorna una lista de PryPlataformas.
     */
    public List<PryPlataformas> buscarPryPlataformas(EntityManager em);

    /**
     * Método encargado de buscar el PryPlataforma con la secuencia dada por
     * parámetro.
     *
     * @param secuencia Secuencia del PryPlataforma que se quiere encontrar.
     * @return Retorna el PryPlataforma identificado con la secuencia dada por
     * parámetro.
     */
    public PryPlataformas buscarPryPlataformaSecuencia(EntityManager em, BigInteger secuencia);

    public BigInteger contadorProyectos(EntityManager em, BigInteger secuencia);
}
