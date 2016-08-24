/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.ReformasLaborales;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la tabla 'ReformasLaborales' 
 * de la base de datos.
 * @author AndresPineda
 */
public interface PersistenciaReformasLaboralesInterface {    
    /**
     * Método encargado de insertar una ReformaLaboral en la base de datos.
     * @param reformaLaboral ReformaLaboral que se quiere crear.
     */
    public void crear(EntityManager em, ReformasLaborales reformaLaboral);
    /**
     * Método encargado de modificar una ReformaLaboral de la base de datos.
     * Este método recibe la información del parámetro para hacer un 'merge' con la 
     * información de la base de datos.
     * @param reformaLaboral ReformaLaboral con los cambios que se van a realizar.
     */
    public void editar(EntityManager em, ReformasLaborales reformaLaboral);
    /**
     * Método encargado de eliminar de la base de datos la ReformaLaboral que entra por parámetro.
     * @param reformaLaboral ReformaLaboral que se quiere eliminar.
     */
    public void borrar(EntityManager em, ReformasLaborales reformaLaboral);
    /**
     * Método encargado de buscar todas las ReformasLaborales existentes en la base de datos.
     * @return Retorna una lista de ReformasLaborales.
     */
    public List<ReformasLaborales> buscarReformasLaborales(EntityManager em);
    /**
     * Método encargado de buscar la ReformaLaboral con la secuencia dada por parámetro.
     * @param secuencia Secuencia de la ReformaLaboral que se quiere encontrar.
     * @return Retorna la ReformaLaboral identificada con la secuencia dada por parámetro.
     */
    public ReformasLaborales buscarReformaSecuencia(EntityManager em, BigInteger secuencia);
    
    public String obtenerCheckIntegralReformaLaboral(EntityManager em, BigInteger secuencia);
}
