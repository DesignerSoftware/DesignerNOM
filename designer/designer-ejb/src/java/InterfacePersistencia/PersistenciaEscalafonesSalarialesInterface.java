/**
 * Documentación a cargo de Andres Pineda
 */
package InterfacePersistencia;

import Entidades.EscalafonesSalariales;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la
 * tabla 'EscalafonesSalariales' de la base de datos.
 *
 * @author Andres Pineda
 */
public interface PersistenciaEscalafonesSalarialesInterface {

    /**
     * Método encargado de insertar un EscalafonSalarial en la base de datos.
     *
     * @param escalafonesSalariales EscalafonSalarial que se quiere crear.
     */
    public void crear(EntityManager em,EscalafonesSalariales escalafonesSalariales);

    /**
     * Método encargado de modificar un EscalafonSalarial de la base de datos.
     * Este método recibe la información del parámetro para hacer un 'merge' con
     * la información de la base de datos.
     *
     * @param escalafonesSalariales EscalafonSalarial con los cambios que se van
     * a realizar.
     */
    public void editar(EntityManager em,EscalafonesSalariales escalafonesSalariales);

    /**
     * Método encargado de eliminar de la base de datos el EscalafonSalarial que
     * entra por parámetro.
     *
     * @param escalafonesSalariales EscalafonSalarial que se quiere eliminar.
     */
    public void borrar(EntityManager em,EscalafonesSalariales escalafonesSalariales);

    /**
     * Método encargado de buscar todos los Escalafones existentes en la base de
     * datos.
     *
     * @return Retorna una lista de EscalafonesSalariales
     */
    public List<EscalafonesSalariales> buscarEscalafones(EntityManager em);

    /**
     * Método encargado de buscar un EscalafonSalarial con la secEscalafon dada
     * por parámetro.
     *
     * @param secEscalafon secEscalafon del EscalafonSalarial que se quiere
     * encontrar.
     * @return Retorna el EscalafonSalarial identificado con la secEscalafon
     * dada por parámetro.
     */
    public EscalafonesSalariales buscarEscalafonSecuencia(EntityManager em,BigInteger secEscalafon);

}
