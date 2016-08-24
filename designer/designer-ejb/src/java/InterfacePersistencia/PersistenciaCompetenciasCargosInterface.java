/**
 * Documentación a cargo de AndresPineda
 */
package InterfacePersistencia;

import Entidades.Competenciascargos;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la
 * tabla 'ProcesosProductivos' de la base de datos.
 *
 * @author AndresPineda
 */
public interface PersistenciaCompetenciasCargosInterface {

    /**
     * Método encargado de insertar una CompetenciaCargo en la base de datos.
     *
     * @param competenciascargos CompetenciaCargo que se quiere crear.
     */
    public void crear(EntityManager em,Competenciascargos competenciascargos);

    /**
     * Método encargado de modificar una CompetenciaCargo de la base de datos.
     * Este método recibe la información del parámetro para hacer un 'merge' con
     * la información de la base de datos.
     *
     * @param competenciascargos CompetenciaCargo con los cambios que se van a realizar.
     */
    public void editar(EntityManager em,Competenciascargos competenciascargos);

    /**
     * Método encargado de eliminar de la base de datos la CompetenciaCargo que
     * entra por parámetro.
     *
     * @param competenciascargos CompetenciaCargo que se quiere eliminar.
     */
    public void borrar(EntityManager em,Competenciascargos competenciascargos);

    /**
     * Método encargado de buscar todos las CompetenciasCargos existentes en la
     * base de datos.
     *
     * @return Retorna una lista de Competenciascargos.
     */
    public List<Competenciascargos> buscarCompetenciasCargos(EntityManager em);

    /**
     * Método encargado de buscar la CompetenciaCargo con la secuencia dada por
     * parámetro.
     *
     * @param secuencia Secuencia del CompetenciaCargo que se quiere encontrar.
     * @return Retorna la CompetenciaCargo identificado con la secuencia dada
     * por parámetro.
     */
    public Competenciascargos buscarCompetenciasCargosSecuencia(EntityManager em,BigInteger secuencia);

    /**
     * Método encargado de buscar todos las Competenciascargos existentes en la
     * base de datos asociadas a un Cargo dado por parametro.
     *
     * @param secCargo Secuencia Cargo
     * @return Retorna una lista de Competenciascargos.
     */
    public List<Competenciascargos> buscarCompetenciasCargosParaSecuenciaCargo(EntityManager em,BigInteger secCargo);
}
