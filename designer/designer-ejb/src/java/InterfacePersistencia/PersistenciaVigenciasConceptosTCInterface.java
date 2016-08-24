/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.VigenciasConceptosTC;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la tabla 'VigenciasConceptosTC' 
 * de la base de datos.
 * @author betelgeuse
 */
public interface PersistenciaVigenciasConceptosTCInterface {
    /**
     * Método encargado de insertar una VigenciaConceptoTC en la base de datos.
     * @param vigenciasConceptosTC VigenciaConceptoTC que se quiere crear.
     */
    public void crear(EntityManager em, VigenciasConceptosTC vigenciasConceptosTC);
    /**
     * Método encargado de modificar una VigenciaConceptoTC de la base de datos.
     * Este método recibe la información del parámetro para hacer un 'merge' con la 
     * información de la base de datos.
     * @param vigenciasConceptosTC VigenciaConceptoTC con los cambios que se van a realizar.
     */
    public void editar(EntityManager em, VigenciasConceptosTC vigenciasConceptosTC);
    /**
     * Método encargado de eliminar de la base de datos la VigenciaConceptoTC que entra por parámetro.
     * @param vigenciasConceptosTC VigenciaConceptoTC que se quiere eliminar.
     */
    public void borrar(EntityManager em, VigenciasConceptosTC vigenciasConceptosTC);
    /**
     * Método encargado de buscar las VigenciasConceptosTC asociadas a un Concepto específico.
     * @param secuencia Secuencia del Concepto.
     * @return Retorna una lista de VigenciasConceptosTC cuyo Concepto tiene como secuencia la dada por parámetro.
     */
    public List<VigenciasConceptosTC> listVigenciasConceptosTCPorConcepto(EntityManager em, BigInteger secuencia);
    /**
     * Método encargado de verificar la existencia de al menos una VigenciaConceptoTC asociada a un Concepto específico
     * y con un TipoContrato definido.
     * @param secuenciaC Secuencia del Concepto.
     * @param secuenciaTC Secuencia del TipoContrato.
     * @return Retorna True si existe al menos una VigenciasConceptosTC cuyo Concepto tenga como secuencia la dada por el parámetro "secuenciaC"
     * y cuyo TipoContrato tenga como secuencia el parámetro "secuenciaTC".
     */
    public boolean verificacionZonaTipoContrato(EntityManager em, BigInteger secuenciaC, BigInteger secuenciaTC);
}
