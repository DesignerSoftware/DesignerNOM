/**
 * Documentación a cargo de AndresPineda
 */
package InterfacePersistencia;

import Entidades.DetallesCargos;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la
 * tabla 'ProcesosProductivos' de la base de datos.
 *
 * @author AndresPineda
 */
public interface PersistenciaDetallesCargosInterface {

    /**
     * Método encargado de insertar un DetalleCargo en la base de datos.
     *
     * @param detallesCargos DetalleCargo que se quiere crear.
     */
    public void crear(EntityManager em,DetallesCargos detallesCargos);

    /**
     * Método encargado de modificar un DetalleCargo de la base de datos. Este
     * método recibe la información del parámetro para hacer un 'merge' con la
     * información de la base de datos.
     *
     * @param detallesCargos DetalleCargo con los cambios que se van a realizar.
     */
    public void editar(EntityManager em,DetallesCargos detallesCargos);

    /**
     * Método encargado de eliminar de la base de datos el DetalleCargo que
     * entra por parámetro.
     *
     * @param detallesCargos DetalleCargo que se quiere eliminar.
     */
    public void borrar(EntityManager em,DetallesCargos detallesCargos);

    /**
     * Método encargado de buscar todos los DetallesCargos existentes en la base
     * de datos.
     *
     * @return Retorna una lista de DetallesCargos.
     */
    public List<DetallesCargos> buscarDetallesCargos(EntityManager em);

    /**
     * Método encargado de buscar el DetalleCargo con la secuencia dada por
     * parámetro.
     *
     * @param secuencia Secuencia del DetalleCargo que se quiere encontrar.
     * @return Retorna el DetalleCargo identificado con la secuencia dada por
     * parámetro.
     */
    public DetallesCargos buscarDetallesCargosSecuencia(EntityManager em,BigInteger secuencia);

    /**
     * Método encargado de buscar el DetalleCargo asociado a un TipoDetalle dado
     * por parametro y a un Cargo dado por parametro.
     *
     * @param secTipoDetalle Secuencia del TipoDetalle
     * @param secCargo Secuencia del Cargo
     * @return Retorna el DetalleCargo asociado al TipoDetalle y al Cargo dado
     * por parametro
     */
    public DetallesCargos buscarDetalleCargoParaSecuenciaTipoDetalle(EntityManager em,BigInteger secTipoDetalle, BigInteger secCargo);
    /**
     * Método encargado de buscar los DetallesCargos asociados a un Cargo
     *
     * @param secuencia Secuencia del Cargo
     * @return Retorna una lista de DetallesCargos
     */
    public List<DetallesCargos> buscarDetallesCargosDeCargoSecuencia(EntityManager em,BigInteger secuencia);

}
