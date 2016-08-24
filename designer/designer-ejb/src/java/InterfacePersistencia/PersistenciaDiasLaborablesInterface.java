/**
 * Documentación a cargo de Andres Pineda
 */
package InterfacePersistencia;

import Entidades.DiasLaborables;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la
 * tabla 'DiasLaborables' de la base de datos.
 *
 * @author Andrés Pineda
 */
public interface PersistenciaDiasLaborablesInterface {

    /**
     * Método encargado de insertar un DiaLaborable en la base de datos.
     *
     * @param diasLaborables DiaLaborable que se quiere crear.
     */
    public void crear(EntityManager em,DiasLaborables diasLaborables);

    /**
     * Método encargado de modificar un DiaLaborable de la base de datos. Este
     * método recibe la información del parámetro para hacer un 'merge' con la
     * información de la base de datos.
     *
     * @param diasLaborables DiaLaborable con los cambios que se van a realizar.
     */
    public void editar(EntityManager em,DiasLaborables diasLaborables);

    /**
     * Método encargado de eliminar de la base de datos el DiaLaborable que
     * entra por parámetro.
     *
     * @param diasLaborables DiaLaborable que se quiere eliminar.
     */
    public void borrar(EntityManager em,DiasLaborables diasLaborables);

    /**
     * Método encargado de buscar un DiaLaborable asociado a una secuencia
     *
     * @param secDiaLaboral Secuencia del DiaLaborable
     * @return Retorna un DiaLaborable con respecto a la secuencia dada por
     * parametro.
     */
    public DiasLaborables buscarDiaLaborableSecuencia(EntityManager em,BigInteger secDiaLaboral);

    /**
     * Método encargado de buscar todos los DiasLaborables existentes en la base
     * de datos.
     *
     * @return Retorna una lista de Bancos.
     */
    public List<DiasLaborables> diasLaborables(EntityManager em);

    /**
     * Método encargado de buscar todas los DiasLaborables de un TipoContrato
     * asociado a una secuencia.
     *
     * @param secTipoContrato Secuencia del TipoContrato asociado a los
     * DiasLaborables
     * @return Retorna una lista de DiasLaborables.
     */
    public List<DiasLaborables> diasLaborablesParaSecuenciaTipoContrato(EntityManager em,BigInteger secTipoContrato);

}
