/**
 * Documentación a cargo de AndresPineda
 */
package InterfacePersistencia;

import Entidades.TEFormulasConceptos;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la
 * tabla 'TEFormulasConceptos' de la base de datos.
 *
 * @author AndresPineda
 */
public interface PersistenciaTEFormulasConceptosInterface {

    /**
     * Método encargado de insertar un TEFormulaConcepto en la base de datos.
     *
     * @param tEFormulasConceptos TEFormulaConcepto que se quiere crear.
     */
    public void crear(EntityManager em, TEFormulasConceptos tEFormulasConceptos);

    /**
     * Método encargado de modificar un TEFormulaConcepto de la base de datos.
     * Este método recibe la información del parámetro para hacer un 'merge' con
     * la información de la base de datos.
     *
     * @param tEFormulasConceptos TEFormulaConcepto con los cambios que se
     * van a realizar.
     */
    public void editar(EntityManager em, TEFormulasConceptos tEFormulasConceptos);

    /**
     * Método encargado de eliminar de la base de datos el TEFormulaConcepto
     * que entra por parámetro.
     *
     * @param tEFormulasConceptos TEFormulaConcepto que se quiere eliminar.
     */
    public void borrar(EntityManager em, TEFormulasConceptos tEFormulasConceptos);

    /**
     * Método encargado de buscar todos los TEFormulasConceptos existentes en
     * la base de datos.
     *
     * @return Retorna una lista de TEFormulasConceptos.
     */
    public List<TEFormulasConceptos> buscarTEFormulasConceptos(EntityManager em);

    /**
     * Método encargado de buscar el TEFormulaConcepto con la secuencia dada por
     * parámetro.
     *
     * @param secTEFormula Secuencia del TEFormulaConcepto que se quiere encontrar.
     * @return Retorna el TEFormulaConcepto identificado con la secuencia dada
     * por parámetro.
     */
    public TEFormulasConceptos buscarTEFormulaConceptoSecuencia(EntityManager em, BigInteger secTEFormula);

    /**
     * Método encargado de buscar todos los TEFormulasConceptos existentes en
     * la base de datos para un TSGrupoTipoEntidad dado por parametro.
     *
     * @param secTSGrupo Secuencia del TSGrupoTipoEntidad.
     * @return Retorna una lista de TEFormulasConceptos para el TSGrupoTipoEntidad
     * dado por parametro.
     */
    public List<TEFormulasConceptos> buscarTEFormulasConceptosPorSecuenciaTSGrupoTipoEntidad(EntityManager em, BigInteger secTSGrupo);

}
