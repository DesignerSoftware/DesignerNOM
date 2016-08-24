/**
 * Documentación a cargo de Andres Pineda
 */
package InterfacePersistencia;

import Entidades.ClasesCategorias;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la
 * tabla 'ClasesCategorias' de la base de datos.
 *
 * @author Andres Pineda
 */
public interface PersistenciaClasesCategoriasInterface {

    /**
     * Método encargado de insertar una ClaseCategoria en la base de datos.
     *
     * @param clasesCategorias Categoria que se quiere crear.
     */
    public void crear(EntityManager em,ClasesCategorias clasesCategorias);

    /**
     * Método encargado de modificar una ClaseCategoria de la base de datos.
     * Este método recibe la información del parámetro para hacer un 'merge' con
     * la información de la base de datos.
     *
     * @param clasesCategorias Categoria con los cambios que se van a realizar.
     */
    public void editar(EntityManager em,ClasesCategorias clasesCategorias);

    /**
     * Método encargado de eliminar de la base de datos la ClaseCategoria que
     * entra por parámetro.
     *
     * @param clasesCategorias Categoria que se quiere eliminar.
     */
    public void borrar(EntityManager em,ClasesCategorias clasesCategorias);

    /**
     * Método encargado de buscar todos las ClasesCategorias existentes en la
     * base de datos.
     *
     * @return Retorna una lista de ClasesCategorias
     */
    public List<ClasesCategorias> consultarClasesCategorias(EntityManager em);

    /**
     * Método encargado de buscar una ClaseCategoria con la secClaseCategoria
     * dada por parámetro.
     *
     * @param secClaseCategoria secCategoria de la ClaseCategoria que se quiere
     * encontrar.
     * @return Retorna el secClaseCategoria identificada con la secCategoria
     * dada por parámetro.
     */
    public ClasesCategorias consultarClaseCategoria(EntityManager em,BigInteger secClaseCategoria);

    /**
     * Metodo encargado de contar cuantas Categorias estar relacionadas con la
     * secClaseCategoria de la ClaseCategoria
     *
     * @param secClaseCategoria Secuencia de la ClaseCategoria
     * @return el numero de relaciones que tiene esa secuencia
     */
    public BigInteger contarCategoriasClaseCategoria(EntityManager em,BigInteger secClaseCategoria);

}
