/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.Motivosmvrs;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la
 * tabla 'MotivosMvrs' de la base de datos.
 *
 * @author betelgeuse
 */
public interface PersistenciaMotivosMvrsInterface {

    /**
     * Método encargado de insertar un MotivoMvrs en la base de datos.
     *
     * @param motivosMvrs MotivoMvrs que se quiere crear.
     */
    public void crear(EntityManager em, Motivosmvrs motivosMvrs);

    /**
     * Método encargado de modificar un MotivoMvrs de la base de datos. Este
     * método recibe la información del parámetro para hacer un 'merge' con la
     * información de la base de datos.
     *
     * @param motivosMvrs MotivoMvrs con los cambios que se van a realizar.
     */
    public void editar(EntityManager em, Motivosmvrs motivosMvrs);

    /**
     * Método encargado de eliminar de la base de datos el MotivoMvrs que entra
     * por parámetro.
     *
     * @param motivosMvrs MotivoMvrs que se quiere eliminar.
     */
    public void borrar(EntityManager em, Motivosmvrs motivosMvrs);

    /**
     * Método encargado de buscar el MotivoMvrs con la secMotivosMvrs dada por
     * parámetro.
     *
     * @param secMotivosMvrs Secuencia del MotivoMvrs que se quiere encontrar.
     * @return Retorna el MotivoMvrs identificado con la secMotivosMvrs dada por
     * parámetro.
     */
    public Motivosmvrs buscarMotivosMvrs(EntityManager em, BigInteger secMotivosMvrs);

    /**
     * Método encargado de buscar todos los MotivosMvrs existentes en la base de
     * datos.
     *
     * @return Retorna una lista de MotivosMvrs.
     */
    public List<Motivosmvrs> buscarMotivosMvrs(EntityManager em);
}
