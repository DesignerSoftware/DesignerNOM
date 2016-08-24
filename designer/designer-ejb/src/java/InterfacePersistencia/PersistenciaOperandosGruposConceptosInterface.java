/**
 * Documentación a cargo de Andres Pineda
 */
package InterfacePersistencia;

import Entidades.OperandosGruposConceptos;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la
 * tabla 'OperandosGruposConceptos' de la base de datos.
 *
 * @author AndresPineda
 */
public interface PersistenciaOperandosGruposConceptosInterface {

    /**
     * Método encargado de insertar un OperandoGrupoConcepto en la base de
     * datos.
     *
     * @param gruposConceptos OperandoGrupoConcepto que se quiere crear.
     */
    public void crear(EntityManager em, OperandosGruposConceptos gruposConceptos);

    /**
     * Método encargado de modificar un OperandoGrupoConcepto de la base de
     * datos. Este método recibe la información del parámetro para hacer un
     * 'merge' con la información de la base de datos.
     *
     * @param gruposConceptos OperandoGrupoConcepto con los cambios que se van a
     * realizar.
     */
    public void editar(EntityManager em, OperandosGruposConceptos gruposConceptos);

    /**
     * Método encargado de eliminar de la base de datos el OperandoGrupoConcepto
     * que entra por parámetro.
     *
     * @param gruposConceptos OperandoGrupoConcepto que se quiere eliminar.
     */
    public void borrar(EntityManager em, OperandosGruposConceptos gruposConceptos);

    /**
     * Método encargado de buscar todos los OperandosGruposConceptos
     *
     * @return Retorna una lista de OperandosGruposConceptos.
     */
    public List<OperandosGruposConceptos> buscarOperandosGruposConceptos(EntityManager em);

    /**
     * Metodo encargado de obtener un OperandoGrupoConcepto con respecto a una
     * secuencia dada por parametro
     *
     * @param secOperando Secuencia del OperandoGrupoConcepto
     * @return Retorna el OperandoGrupoConcepto referenciado con la secuencia
     * dada
     */
    public OperandosGruposConceptos buscarOperandosGruposConceptosPorSecuencia(EntityManager em, BigInteger secOperando);

    /**
     * Metodo encargado de obtener la lista de OperandosGruposConceptos para un Proceso especifico dado por parametro
     *
     * @param secProceso Secuencia del Proceso 
     * @return Retorna la lista de OperandosGruposConceptos del tipo de Proceso dado.
     */
    public List<OperandosGruposConceptos> buscarOperandosGruposConceptosPorProcesoSecuencia(EntityManager em, BigInteger secProceso);

}
