/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.DetallesReformasLaborales;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la
 * tabla 'ReformasLaborales' de la base de datos.
 *
 * @author AndresPineda
 */
public interface PersistenciaDetallesReformasLaboralesInterface {

    /**
     * Método encargado de insertar una DetalleReformaLaborale en la base de datos.
     *
     * @param detallesReformasLaborales DetalleReformaLaborale que se quiere crear.
     */
    public void crear(EntityManager em,DetallesReformasLaborales detallesReformasLaborales);

    /**
     * Método encargado de insertar una DetalleReformaLaborale en la base de datos.
     *
     * @param detallesReformasLaborales DetalleReformaLaborale que se quiere crear.
     */
    public void editar(EntityManager em,DetallesReformasLaborales detallesReformasLaborales);

    /**
     * Método encargado de insertar una DetalleReformaLaborale en la base de datos.
     *
     * @param detallesReformasLaborales DetalleReformaLaborale que se quiere crear.
     */
    public void borrar(EntityManager em,DetallesReformasLaborales detallesReformasLaborales);

    /**
     * Método encargado de buscar todas las DetallesReformasLaborales existentes en la
     * base de datos.
     *
     * @return Retorna una lista de ReformasLaborales.
     */
    public List<DetallesReformasLaborales> buscarDetallesReformasLaborales(EntityManager em);

    /**
     * Método encargado de buscar una DetalleReformaLaborale con la secuencia dada por
     * parámetro.
     *
     * @param secuencia Secuencia del DetalleReformaLaborale que se quiere encontrar.
     * @return Retorna wl DetalleReformaLaborale identificada con la secuencia dada por
     * parámetro.
     */
    public DetallesReformasLaborales buscarDetalleReformaSecuencia(EntityManager em,BigInteger secuencia);
    
    /**
     * Método encargado de buscar los DetallesReformasLaborales para una ReformaLaboral dada.
     *
     * @param secuencia Secuencia de la  ReformaLaboral
     * @return Retorna los DetallesReformasLaborales para la ReformaLaboral
     */
    public List<DetallesReformasLaborales> buscarDetalleReformasParaReformaSecuencia(EntityManager em,BigInteger secuencia);

}
