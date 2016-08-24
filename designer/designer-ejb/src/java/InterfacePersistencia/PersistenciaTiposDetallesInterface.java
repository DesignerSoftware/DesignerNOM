/**
 * Documentación a cargo de AndresPineda
 */
package InterfacePersistencia;

import Entidades.TiposDetalles;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la
 * tabla 'TiposDetalles' de la base de datos.
 *
 * @author AndresPineda
 */
public interface PersistenciaTiposDetallesInterface {

    /**
     * Método encargado de insertar un TipoDetalle en la base de datos.
     *
     * @param tiposDetalles TipoDetalle que se quiere crear.
     */
    public void crear(EntityManager em, TiposDetalles tiposDetalles);

    /**
     * Método encargado de modificar un TipoDetalle de la base de datos. Este
     * método recibe la información del parámetro para hacer un 'merge' con la
     * información de la base de datos.
     *
     * @param tiposDetalles TipoDetalle con los cambios que se van a realizar.
     */
    public void editar(EntityManager em, TiposDetalles tiposDetalles);

    /**
     * Método encargado de eliminar de la base de datos el TipoDetalle que entra
     * por parámetro.
     *
     * @param tiposDetalles TipoDetalle que se quiere eliminar.
     */
    public void borrar(EntityManager em, TiposDetalles tiposDetalles);

    /**
     * Método encargado de buscar todos los TiposDetalles existentes en la base
     * de datos.
     *
     * @return Retorna una lista de TiposDetalles.
     */
    public List<TiposDetalles> buscarTiposDetalles(EntityManager em);

    /**
     * Método encargado de buscar el TipoDetalle con la secuencia dada por
     * parámetro.
     *
     * @param secuencia Secuencia del TipoDetalle que se quiere encontrar.
     * @return Retorna el TipoDetalle identificado con la secuencia dada por
     * parámetro.
     */
    public TiposDetalles buscarTiposDetallesSecuencia(EntityManager em, BigInteger secuencia);

}
