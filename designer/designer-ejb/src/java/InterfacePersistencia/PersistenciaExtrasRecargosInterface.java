/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.ExtrasRecargos;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la tabla 'ExtrasRecargos' 
 * de la base de datos.
 * @author Andres Pineda.
 */
public interface PersistenciaExtrasRecargosInterface {
    /**
     * Método encargado de insertar un ExtraRecargo en la base de datos.
     * @param extrasRecargos ExtraRecargo que se quiere crear.
     */
    public void crear(EntityManager em,ExtrasRecargos extrasRecargos);
    /**
     * Método encargado de modificar un ExtraRecargo de la base de datos.
     * Este método recibe la información del parámetro para hacer un 'merge' con la 
     * información de la base de datos.
     * @param extrasRecargos ExtraRecargo con los cambios que se van a realizar.
     */
    public void editar(EntityManager em,ExtrasRecargos extrasRecargos);
    /**
     * Método encargado de eliminar de la base de datos el ExtraRecargo que entra por parámetro.
     * @param extrasRecargos ExtraRecargo que se quiere eliminar.
     */
    public void borrar(EntityManager em,ExtrasRecargos extrasRecargos);
    /**
     * Método encargado de buscar el ExtraRecargo con la secuencia dada por parámetro.
     * @param secuencia Secuencia del ExtraRecargo que se quiere encontrar.
     * @return Retorna el ExtraRecargo identificado con la secuencia dada por parámetro.
     */
    public ExtrasRecargos buscarExtraRecargo(EntityManager em,BigInteger secuencia);
    /**
     * Método encargado de buscar todos los ExtrasRecargos existentes en la base de datos, ordenados por código.
     * @return Retorna una lista de ExtraRecargos.
     */
    public List<ExtrasRecargos> buscarExtrasRecargos(EntityManager em);
}
