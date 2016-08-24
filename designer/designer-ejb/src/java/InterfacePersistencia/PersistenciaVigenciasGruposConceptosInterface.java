/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.VigenciasGruposConceptos;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la
 * tabla 'VigenciasGruposConceptos' de la base de datos.
 *
 * @author betelgeuse
 */
public interface PersistenciaVigenciasGruposConceptosInterface {

    /**
     * Método encargado de insertar una VigenciaGrupoConcepto en la base de
     * datos.
     *
     * @param vigenciasGruposConceptos VigenciaGrupoConcepto que se quiere
     * crear.
     */
    public void crear(EntityManager em, VigenciasGruposConceptos vigenciasGruposConceptos);

    /**
     * Método encargado de modificar una VigenciaGrupoConcepto de la base de
     * datos. Este método recibe la información del parámetro para hacer un
     * 'merge' con la información de la base de datos.
     *
     * @param vigenciasGruposConceptos VigenciaGrupoConcepto con los cambios que
     * se van a realizar.
     */
    public void editar(EntityManager em, VigenciasGruposConceptos vigenciasGruposConceptos);

    /**
     * Método encargado de eliminar de la base de datos la VigenciaGrupoConcepto
     * que entra por parámetro.
     *
     * @param vigenciasGruposConceptos VigenciaGrupoConcepto que se quiere
     * eliminar.
     */
    public void borrar(EntityManager em, VigenciasGruposConceptos vigenciasGruposConceptos);

    /**
     * Método encargado de buscar las VigenciasGruposConceptos asociadas a un
     * Concepto específico.
     *
     * @param secuencia Secuencia del Concepto.
     * @return Retorna una lista de VigenciasGruposConceptos las cuales están
     * asociadas con el concepto cuya secuencia coincide con la secuencia dada
     * por parámetro.
     */
    public List<VigenciasGruposConceptos> listVigenciasGruposConceptosPorConcepto(EntityManager em, BigInteger secuencia);

    /**
     * Método encargado de verificar la existencia de por lo menos una
     * VigenciaGrupoConcepto asociada a un Concepto específico y que este
     * asociada con el GrupoConcepto uno (NIT).
     *
     * @param secuencia Secuencia del Concepto.
     * @return Retorna True si existe al menos una VigenciaGrupoConcepto cuyo
     * Concepto tenga como secuencia la dada por el parámetro "secuencia".
     */
    public boolean verificacionGrupoUnoConcepto(EntityManager em, BigInteger secuencia);
    
    /**
     * Método encargado de buscar las VigenciasGruposConceptos asociadas a un
     * Grupo Concepto específico.
     *
     * @param secuenciaG Secuencia del Grupo Concepto.
     * @return Retorna una lista de VigenciasGruposConceptos las cuales están
     * asociadas con el grupo concepto cuya secuencia coincide con la secuencia dada
     * por parámetro.
     */
    public List<VigenciasGruposConceptos> listVigenciasGruposConceptosPorGrupoConcepto(EntityManager em, BigInteger secuenciaG);

    }
