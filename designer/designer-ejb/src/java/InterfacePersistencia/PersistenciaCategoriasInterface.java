/**
 * Documentación a cargo de Andres Pineda
 */
package InterfacePersistencia;

import Entidades.Categorias;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la tabla 'Categorias' 
 * de la base de datos.
 * @author Andres Pineda
 */
public interface PersistenciaCategoriasInterface {

    /**
     * Método encargado de insertar una Categoria en la base de datos.
     * @param categorias Categoria que se quiere crear.
     */
    public void crear(EntityManager em,Categorias categorias);
    /**
     * Método encargado de modificar una Categorias de la base de datos.
     * Este método recibe la información del parámetro para hacer un 'merge' con la 
     * información de la base de datos.
     * @param categorias Categoria con los cambios que se van a realizar.
     */
    public void editar(EntityManager em,Categorias categorias);
    /**
     * Método encargado de eliminar de la base de datos la Categoria que entra por parámetro.
     * @param categorias Categoria que se quiere eliminar.
     */
    public void borrar(EntityManager em,Categorias categorias);
    /**
     * Método encargado de buscar todos las Categorias existentes en la base de datos.
     * @return Retorna una lista de Categorias
     */
    public List<Categorias> buscarCategorias(EntityManager em);
    /**
     * Método encargado de buscar una Categoria con la secCategoria dada por
     * parámetro.
     *
     * @param secCategoria secCategoria de la Categoria que se quiere
     * encontrar.
     * @return Retorna el Categoria identificada con la secCategoria dada
     * por parámetro.
     */
    public Categorias buscarCategoriaSecuencia(EntityManager em,BigInteger secCategoria);
}
