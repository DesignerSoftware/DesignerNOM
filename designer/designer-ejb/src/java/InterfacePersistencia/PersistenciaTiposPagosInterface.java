/**
 * Documentación a cargo de AndresPineda
 */
package InterfacePersistencia;

import Entidades.Tipospagos;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la
 * tabla 'Procesos' de la base de datos.
 *
 * @author AndresPineda
 */
public interface PersistenciaTiposPagosInterface {

    /**
     * Método encargado de insertar un Tipopago en la base de datos.
     *
     * @param tipospagos Tipopago que se quiere crear.
     */
    public void crear(EntityManager em, Tipospagos tipospagos);

    /**
     * Método encargado de modificar un Tipopago de la base de datos. Este
     * método recibe la información del parámetro para hacer un 'merge' con la
     * información de la base de datos.
     *
     * @param tipospagos Tipopago con los cambios que se van a realizar.
     */
    public void editar(EntityManager em, Tipospagos tipospagos);

    /**
     * Método encargado de eliminar de la base de datos el Tipopago que entra
     * por parámetro.
     *
     * @param tipospagos Tipopago que se quiere eliminar.
     */
    public void borrar(EntityManager em, Tipospagos tipospagos);

    /**
     * Método encargado de buscar todos los Tipospagos existentes en la base de
     * datos.
     *
     * @return Retorna una lista de Tipospagos.
     */
    public List<Tipospagos> consultarTiposPagos(EntityManager em );

    /**
     * Método encargado de buscar el Tipopago con la secuencia dada por
     * parámetro.
     *
     * @param secuencia Secuencia del Tipopago que se quiere encontrar.
     * @return Retorna el Tipopago identificado con la secuencia dada por
     * parámetro.
     */
    public Tipospagos consultarTipoPago(EntityManager em, BigInteger secuencia);

    public BigInteger contarProcesosTipoPago(EntityManager em, BigInteger secuencia);
}
