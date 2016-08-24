/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.Procesos;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la
 * tabla 'Procesos' de la base de datos.
 *
 * @author betelgeuse
 */
public interface PersistenciaProcesosInterface {

    /**
     * Método encargado de insertar un Proceso en la base de datos.
     *
     * @param procesos Proceso que se quiere crear.
     */
    public void crear(EntityManager em, Procesos procesos);

    /**
     * Método encargado de modificar un Proceso de la base de datos. Este método
     * recibe la información del parámetro para hacer un 'merge' con la
     * información de la base de datos.
     *
     * @param procesos Proceso con los cambios que se van a realizar.
     */
    public void editar(EntityManager em, Procesos procesos);

    /**
     * Método encargado de eliminar de la base de datos el Proceso que entra por
     * parámetro.
     *
     * @param procesos Proceso que se quiere eliminar.
     */
    public void borrar(EntityManager em, Procesos procesos);

    /**
     * Método encargado de buscar todos los Procesos existentes en la base de
     * datos.
     *
     * @return Retorna una lista de Procesos.
     */
    public List<Procesos> buscarProcesos(EntityManager em);

    /**
     * Método encargado de buscar el Proceso con la secuencia dada por
     * parámetro.
     *
     * @param secuencia Secuencia del Proceso que se quiere encontrar.
     * @return Retorna el Proceso identificado con la secuencia dada por
     * parámetro.
     */
    public Procesos buscarProcesosSecuencia(EntityManager em, BigInteger secuencia);

    /**
     * Método encargado de traer todos los procesos de la base de datos,
     * ordenados por descripción.
     *
     * @return Retorna una lista de Procesos.
     */
    public List<Procesos> lovProcesos(EntityManager em);

    /**
     * Método encargado de buscar los procesos automaticos autorizados para el
     * usuario y que él tiene asociados. Es otra palabras, los procesos que el
     * usuario ha activado.
     *
     * @return Retorna una lista de Procesos asociados al usuario que está
     * usando el aplicativo.
     */
    public List<Procesos> procesosParametros(EntityManager em);

    public Procesos buscarProcesosPorCodigo(EntityManager em, short codigo);

    public String obtenerDescripcionProcesoPorSecuencia(EntityManager em, BigInteger proceso);
}
