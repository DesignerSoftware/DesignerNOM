/**
 * Documentación a cargo de Andres Pineda
 */
package InterfacePersistencia;

import Entidades.SubCategorias;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la
 * tabla 'Escalafones' de la base de datos.
 *
 * @author Andres Pineda
 */
public interface PersistenciaSubCategoriasInterface {

    /**
     * Método encargado de insertar una SubCategoria en la base de datos.
     *
     * @param subCategorias SubCategoria que se quiere crear.
     */
    public void crear(EntityManager em, SubCategorias subCategorias);

    /**
     * Método encargado de modificar una SubCategoria de la base de datos. Este
     * método recibe la información del parámetro para hacer un 'merge' con la
     * información de la base de datos.
     *
     * @param subCategorias SubCategoria con los cambios que se van a realizar.
     */
    public void editar(EntityManager em, SubCategorias subCategorias);

    /**
     * Método encargado de eliminar de la base de datos la SubCategoria que
     * entra por parámetro.
     *
     * @param subCategorias SubCategoria que se quiere eliminar.
     */
    public void borrar(EntityManager em, SubCategorias subCategorias);

    /**
     * Método encargado de buscar todos las SubCategorias existentes en la base
     * de datos.
     *
     * @return Retorna una lista de SubCategorias
     */
    public List<SubCategorias> consultarSubCategorias(EntityManager em);

    /**
     * Método encargado de buscar una secSubCategoria con la secEscalafon dada
     * por parámetro.
     *
     * @param secSubCategoria secSubCategoria de la SubCategoria que se quiere
     * encontrar.
     * @return Retorna la SubCategoria identificado con la secSubCategoria dada
     * por parámetro.
     */
    public SubCategorias consultarSubCategoria(EntityManager em, BigInteger secSubCategoria);

    /**
     * *
     * Metodo encargado de contar cuantos Escalafones hay relacionados con la
     * secuencia
     *
     * @param secSubCategoria Secuencia de la SubCategoria
     * @return Cuantos Escalafones estan relacionados con la secuencia de la
     * SubCategoria
     */
    public BigInteger contarEscalafones(EntityManager em, BigInteger secSubCategoria);
}
