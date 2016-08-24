/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.VigenciasNoFormales;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la tabla 'VigenciasNoFormales' 
 * de la base de datos.
 * @author betelgeuse
 */
public interface PersistenciaVigenciasNoFormalesInterface {
    /**
     * Método encargado de insertar una VigenciaNoFormal en la base de datos.
     * @param vigenciasNoFormales VigenciaNoFormal que se quiere crear.
     */
    public void crear(EntityManager em, VigenciasNoFormales vigenciasNoFormales);
    /**
     * Método encargado de modificar una VigenciaNoFormal de la base de datos.
     * Este método recibe la información del parámetro para hacer un 'merge' con la 
     * información de la base de datos.
     * @param vigenciasNoFormales VigenciaNoFormal con los cambios que se van a realizar.
     */
    public void editar(EntityManager em, VigenciasNoFormales vigenciasNoFormales);
    /**
     * Método encargado de eliminar de la base de datos la VigenciaNoFormal que entra por parámetro.
     * @param vigenciasNoFormales VigenciaNoFormal que se quiere eliminar.
     */
    public void borrar(EntityManager em, VigenciasNoFormales vigenciasNoFormales);
    /**
     * Método encargado de buscar todas las VigenciasNoFormales existentes en la base de datos.
     * @return Retorna una lista de VigenciasNoFormales.
     */
    public List<VigenciasNoFormales> buscarVigenciasNoFormales(EntityManager em );
    /**
     * Método encargado de buscar las VigenciasNoFormales de una Persona específica.
     * @param secuencia Secuencia de la Persona.
     * @return Retorna las VigenciasNoFormales, odenadas descendentemente por la fechaVigencia, de la Persona cuya secuencia coincide 
     * con la secuencia dada por parámetro.
     */
    public List<VigenciasNoFormales> vigenciasNoFormalesPersona(EntityManager em, BigInteger secuencia);
}
