/**
 * Documentación a cargo de AndresPineda
 */
package InterfacePersistencia;

import Entidades.VigenciasMonedasBases;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la
 * tabla 'Circulares' de la base de datos.
 *
 * @author AndresPineda
 */
public interface PersistenciaVigenciasMonedasBasesInterface {

    /**
     * Método encargado de insertar una VigenciaMonedaBase en la base de datos.
     *
     * @param monedasBases VigenciaMonedaBase que se quiere crear.
     */
    public void crear(EntityManager em, VigenciasMonedasBases monedasBases);

    /**
     * Método encargado de modificar una VigenciaMonedaBase de la base de datos. Este
     * método recibe la información del parámetro para hacer un 'merge' con la
     * información de la base de datos.
     *
     * @param monedasBases VigenciaMonedaBase con los cambios que se van a realizar.
     */
    public void editar(EntityManager em, VigenciasMonedasBases monedasBases);

    /**
     * Método encargado de eliminar de la base de datos la VigenciaMonedaBase que entra
     * por parámetro.
     *
     * @param monedasBases VigenciaMonedaBase que se quiere eliminar.
     */
    public void borrar(EntityManager em, VigenciasMonedasBases monedasBases);

    /**
     * Método encargado de buscar todas las VigenciasMonedasBases existentes en la base de
     * datos.
     *
     * @return Retorna una lista de VigenciasMonedasBases.
     */
    public List<VigenciasMonedasBases> buscarVigenciasMonedasBases(EntityManager em );

    /**
     * Método encargado de buscar la VigenciaMonedaBase con la secuencia dada por
     * parámetro.
     *
     * @param secuencia Secuencia de la VigenciaMonedaBase que se quiere encontrar.
     * @return Retorna la VigenciaMonedaBase identificado con la secuencia dada por
     * parámetro.
     */
    public VigenciasMonedasBases buscarVigenciaMonedaBaseSecuencia(EntityManager em, BigInteger secuencia);

    /**
     * Método encargado de buscar todos las VigenciasMonedasBases existentes en la base de
     * datos asociadas a la secuencia de una Empresa.
     *
     * @param secuencia Secuencia de la empresa
     * @return Retorna una lista de VigenciasMonedasBases.
     */
    public List<VigenciasMonedasBases> buscarVigenciasMonedasBasesPorSecuenciaEmpresa(EntityManager em, BigInteger secuencia);

}
