/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.VigenciasConceptosTT;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la tabla 'VigenciasConceptosTT' 
 * de la base de datos.
 * @author betelgeuse
 */
public interface PersistenciaVigenciasConceptosTTInterface {
    /**
     * Método encargado de insertar una VigenciaConceptoTT en la base de datos.
     * @param vigenciasConceptosTT VigenciaConceptoTT que se quiere crear.
     */
    public void crear(EntityManager em, VigenciasConceptosTT vigenciasConceptosTT);
    /**
     * Método encargado de modificar una VigenciaConceptoTT de la base de datos.
     * Este método recibe la información del parámetro para hacer un 'merge' con la 
     * información de la base de datos.
     * @param vigenciasConceptosTT VigenciaConceptoTT con los cambios que se van a realizar.
     */
    public void editar(EntityManager em, VigenciasConceptosTT vigenciasConceptosTT);
    /**
     * Método encargado de eliminar de la base de datos la VigenciaConceptoTT que entra por parámetro.
     * @param vigenciasConceptosTT VigenciaConceptoTT que se quiere eliminar.
     */
    public void borrar(EntityManager em, VigenciasConceptosTT vigenciasConceptosTT);
    /**
     * Método encargado de buscar las VigenciasConceptosTT asociadas a un Concepto específico.
     * @param secuencia Secuencia del Concepto.
     * @return Retorna una lista de VigenciasConceptosTT cuyo Concepto tiene como secuencia la dada por parámetro.
     */
    public List<VigenciasConceptosTT> listVigenciasConceptosTTPorConcepto(EntityManager em, BigInteger secuencia);
    /**
     * Método encargado de verificar la existencia de al menos una VigenciaConceptoTT asociada a un Concepto específico
     * y con un TipoTrabajador definido.
     * @param secuenciaC Secuencia del Concepto.
     * @param secuenciaTT Secuencia del TipoTrabajador.
     * @return Retorna True si existe al menos una VigenciasConceptosTT cuyo Concepto tenga como secuencia la dada por el parámetro "secuenciaC"
     * y cuyo TipoTrabajador tenga como secuencia el parámetro "secuenciaTT".
     */
    public boolean verificacionZonaTipoTrabajador(EntityManager em, BigInteger secuenciaC, BigInteger secuenciaTT);
}
