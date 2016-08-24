/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.VigenciasConceptosRL;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la tabla 'VigenciasConceptosRL' 
 * de la base de datos.
 * @author betelgeuse
 */
public interface PersistenciaVigenciasConceptosRLInterface {
    /**
     * Método encargado de insertar una VigenciaConceptoRL en la base de datos.
     * @param vigenciasConceptosRL VigenciaConceptoRL que se quiere crear.
     */
    public void crear(EntityManager em, VigenciasConceptosRL vigenciasConceptosRL);
    /**
     * Método encargado de modificar una VigenciaConceptoRL de la base de datos.
     * Este método recibe la información del parámetro para hacer un 'merge' con la 
     * información de la base de datos.
     * @param vigenciasConceptosRL VigenciaConceptoRL con los cambios que se van a realizar.
     */
    public void editar(EntityManager em, VigenciasConceptosRL vigenciasConceptosRL);
    /**
     * Método encargado de eliminar de la base de datos la VigenciaConceptoRL que entra por parámetro.
     * @param vigenciasConceptosRL VigenciaConceptoRL que se quiere eliminar.
     */
    public void borrar(EntityManager em, VigenciasConceptosRL vigenciasConceptosRL);
    /**
     * Método encargado de buscar las VigenciasConceptosRL asociadas a un Concepto específico.
     * @param secuencia Secuencia del Concepto.
     * @return Retorna una lista de VigenciasConceptosRL cuyo Concepto tiene como secuencia la dada por parámetro.
     */
    public List<VigenciasConceptosRL> listVigenciasConceptosRLPorConcepto(EntityManager em, BigInteger secuencia);
    /**
     * Método encargado de verificar la existencia de al menos una VigenciaConceptoRL asociada a un Concepto específico
     * y con un TipoSalario definido.
     * @param secuenciaC Secuencia del Concepto.
     * @param secuenciaTS Secuencia del TipoSalario.
     * @return Retorna True si existe al menos una VigenciasConceptosRL cuyo Concepto tenga como secuencia la dada por el parámetro "secuenciaC"
     * y cuyo TipoSalario tenga como secuencia el parámetro "secuenciaTS".
     */
    public boolean verificacionZonaTipoReformasLaborales(EntityManager em, BigInteger secuenciaC, BigInteger secuenciaTS);
}
