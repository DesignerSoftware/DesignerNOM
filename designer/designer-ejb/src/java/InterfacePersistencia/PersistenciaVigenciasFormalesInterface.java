/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.VigenciasFormales;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la tabla 'VigenciasFormales' 
 * de la base de datos.
 * @author betelgeuse
 */
public interface PersistenciaVigenciasFormalesInterface {
    /**
     * Método encargado de insertar una VigenciaFormal en la base de datos.
     * @param vigenciasFormales VigenciaFormal que se quiere crear.
     */
    public void crear(EntityManager em, VigenciasFormales vigenciasFormales);
    /**
     * Método encargado de modificar una VigenciaFormal de la base de datos.
     * Este método recibe la información del parámetro para hacer un 'merge' con la 
     * información de la base de datos.
     * @param vigenciasFormales VigenciaFormal con los cambios que se van a realizar.
     */
    public void editar(EntityManager em, VigenciasFormales vigenciasFormales);
    /**
     * Método encargado de eliminar de la base de datos la VigenciaFormal que entra por parámetro.
     * @param vigenciasFormales VigenciaFormal que se quiere eliminar.
     */
    public void borrar(EntityManager em, VigenciasFormales vigenciasFormales);
    /**
     * Método encargado de buscar todas las VigenciasFormales existentes en la base de datos.
     * @return Retorna una lista de VigenciasFormales.
     */
    public List<VigenciasFormales> buscarVigenciasFormales(EntityManager em );
    /**
     * Método encargado de buscar las VigenciasFormales de una Persona específica.
     * @param secuencia Secuencia de la Persona.
     * @return Retorna las VigenciasFormales, odenadas descendentemente por la fechaVigencia, de la Persona cuya secuencia coincide 
     * con la secuencia dada por parámetro.
     */
    public List<VigenciasFormales> vigenciasFormalesPersona(EntityManager em, BigInteger secuencia);
    /**
     * Método encargado de buscar las últimas VigenciasFormales de una Persona específica.
     * @param secuencia Secuencia de la Persona.
     * @return Retorna una lista con las últimas VigenciasFormales registradas, odenadas descendentemente por la fechaVigencia, de la Persona cuya secuencia coincide 
     * con la secuencia dada por parámetro.
     */
    public List<VigenciasFormales> educacionPersona(EntityManager em, BigInteger secuencia);
    
    public String primeraVigenciaFormal(EntityManager em,BigInteger secuencia);
    
}
