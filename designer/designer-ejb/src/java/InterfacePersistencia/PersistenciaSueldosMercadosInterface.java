/**
 * Documentación a cargo de AndresPineda
 */
package InterfacePersistencia;

import Entidades.SueldosMercados;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la
 * tabla 'ProcesosProductivos' de la base de datos.
 *
 * @author AndresPineda
 */
public interface PersistenciaSueldosMercadosInterface {

    /**
     * Método encargado de insertar un SueldoMercado en la base de datos.
     *
     * @param sueldosMercados SueldoMercado que se quiere crear.
     */
    public void crear(EntityManager em, SueldosMercados sueldosMercados);

    /**
     * Método encargado de modificar un SueldoMercado de la base de datos. Este
     * método recibe la información del parámetro para hacer un 'merge' con la
     * información de la base de datos.
     *
     * @param sueldosMercados SueldoMercado con los cambios que se van a
     * realizar.
     */
    public void editar(EntityManager em, SueldosMercados sueldosMercados);

    /**
     * Método encargado de eliminar de la base de datos el SueldoMercado que
     * entra por parámetro.
     *
     * @param sueldosMercados SueldoMercado que se quiere eliminar.
     */
    public void borrar(EntityManager em, SueldosMercados sueldosMercados);

    /**
     * Método encargado de buscar todos los SueldosMercados existentes en la
     * base de datos.
     *
     * @return Retorna una lista de SueldosMercados.
     */
    public List<SueldosMercados> buscarSueldosMercados(EntityManager em);

    /**
     * Método encargado de buscar el SueldoMercado con la secuencia dada por
     * parámetro.
     *
     * @param secuencia Secuencia del SueldoMercado que se quiere encontrar.
     * @return Retorna el SueldoMercado identificado con la secuencia dada por
     * parámetro.
     */
    public SueldosMercados buscarSueldosMercadosSecuencia(EntityManager em, BigInteger secuencia);

    /**
     * Método encargado de buscar los SueldosMercados existentes para un Cargo
     * dado por parametro.
     *
     * @param secCargo Secuencia del Cargo que se quiere encontrar.
     * @return Retorna una lista de SueldosMercados.
     */
    public List<SueldosMercados> buscarSueldosMercadosPorSecuenciaCargo(EntityManager em, BigInteger secCargo);
}
