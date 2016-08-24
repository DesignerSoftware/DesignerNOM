/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.Sucursales;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la
 * tabla 'Sucursales' de la base de datos.
 *
 * @author betelgeuse
 */

public interface PersistenciaSucursalesInterface {

    /**
     * Método encargado de insertar una Sucursal en la base de datos.
     *
     * @param sucursales Sucursal que se quiere crear.
     */
    public void crear(EntityManager em, Sucursales sucursales);

    /**
     * Método encargado de modificar una Sucursal de la base de datos. Este
     * método recibe la información del parámetro para hacer un 'merge' con la
     * información de la base de datos.
     *
     * @param sucursales Sucursal con los cambios que se van a realizar.
     */
    public void editar(EntityManager em, Sucursales sucursales);

    /**
     * Método encargado de eliminar de la base de datos la Sucursal que entra
     * por parámetro.
     *
     * @param sucursales Sucursal que se quiere eliminar.
     */
    public void borrar(EntityManager em, Sucursales sucursales);

    /**
     * Método encargado de buscar la Sucursal con la secuencia dada por
     * parámetro.
     *
     * @param secuencia Secuencia de la Sucursal que se quiere encontrar.
     * @return Retorna la Sucursal identificada con la secuencia dada por
     * parámetro.
     */
    public Sucursales buscarSucursal(EntityManager em, BigInteger secuencia);

    /**
     * Método encargado de buscar todas las Sucursales existentes en la base de
     * datos.
     *
     * @return Retorna una lista de Sucursales.
     */
    public List<Sucursales> consultarSucursales(EntityManager em);

    public BigInteger contarVigenciasFormasPagosSucursal(EntityManager em, BigInteger secuencia);
}
