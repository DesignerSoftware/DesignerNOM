/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.GruposConceptos;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la tabla 'GruposConceptos' 
 * de la base de datos.
 * @author AndresPineda
 */
public interface PersistenciaGruposConceptosInterface {
    
    /**
     * Método encargado de insertar un contrato en la base de datos.
     * @param gruposConceptos GrupoConcepto que se quiere crear.
     */
    public void crear(EntityManager em,GruposConceptos gruposConceptos);
    /**
     * Método encargado de modificar un Contrato de la base de datos.
     * Este método recibe la información del parámetro para hacer un 'merge' con la 
     * información de la base de datos.
     * @param gruposConceptos GrupoConcepto con los cambios que se van a realizar.
     */
    public void editar(EntityManager em,GruposConceptos gruposConceptos);
    /**
     * Método encargado de eliminar de la base de datos el Contrato que entra por parámetro.
     * @param gruposConceptos GrupoConcepto que se quiere eliminar.
     */
    public void borrar(EntityManager em,GruposConceptos gruposConceptos);   
    /**
     * Método encargado de buscar todos los GruposConceptos existentes en la base de datos.
     * @return Retorna una lista de GruposConceptos.
     */
    public List<GruposConceptos> buscarGruposConceptos(EntityManager em);
    /**
     * Método encargado de buscar el GrupoConcepto con la secuencia dada por parámetro.
     * @param secuencia Secuencia del GrupoConcepto que se quiere encontrar.
     * @return Retorna el GrupoConcepto identificado con la secuencia dada por parámetro.
     */
    public GruposConceptos buscarGruposConceptosSecuencia(EntityManager em,BigInteger secuencia);
    
}
