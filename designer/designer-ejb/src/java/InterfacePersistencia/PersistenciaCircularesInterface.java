/**
 * Documentación a cargo de AndresPineda
 */
package InterfacePersistencia;

import Entidades.Circulares;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la
 * tabla 'Circulares' de la base de datos.
 *
 * @author AndresPineda
 */
public interface PersistenciaCircularesInterface {

    /**
     * Método encargado de insertar una Circular en la base de datos.
     *
     * @param circulares Circular que se quiere crear.
     */
    public void crear(EntityManager em,Circulares circulares);

    /**
     * Método encargado de modificar una Circular de la base de datos. Este
     * método recibe la información del parámetro para hacer un 'merge' con la
     * información de la base de datos.
     *
     * @param circulares Circular con los cambios que se van a realizar.
     */
    public void editar(EntityManager em,Circulares circulares);

    /**
     * Método encargado de eliminar de la base de datos la Circular que entra
     * por parámetro.
     *
     * @param circulares Circular que se quiere eliminar.
     */
    public void borrar(EntityManager em,Circulares circulares);

    /**
     * Método encargado de buscar todas las Circulares existentes en la base
     * de datos.
     *
     * @return Retorna una lista de Circulares.
     */
    public List<Circulares> buscarCirculares(EntityManager em);

    /**
     * Método encargado de buscar la Circular con la secCircular dada por
     * parámetro.
     *
     * @param secuencia Secuencia de la Circular que se quiere encontrar.
     * @return Retorna la Circular identificado con la secCircular dada
     * por parámetro.
     */
    public Circulares buscarCircularSecuencia(EntityManager em,BigInteger secuencia);

    /**
     * Método encargado de buscar todos las Circulares existentes en la base
     * de datos asociadas a la secuencia de una Empresa.
     * @param secuencia Secuencia de la empresa
     * @return Retorna una lista de Circulares.
     */
    public List<Circulares> buscarCircularesPorSecuenciaEmpresa(EntityManager em,BigInteger secuencia);

}
