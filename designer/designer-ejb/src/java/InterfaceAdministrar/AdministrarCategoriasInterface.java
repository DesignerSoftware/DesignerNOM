/**
 * Documentación a cargo de Andres Pineda
 */
package InterfaceAdministrar;

import Entidades.Categorias;
import Entidades.ClasesCategorias;
import Entidades.Conceptos;
import Entidades.TiposSueldos;
import java.util.List;

/**
 * Interface encargada de determinar las operaciones lógicas necesarias para la
 * pantalla 'CategoriaEsca'.
 *
 * @author betelgeuse
 */
public interface AdministrarCategoriasInterface {

    /**
     * Método encargado de recuperar todas las Categorias.
     *
     * @return Retorna una lista de Categorias.
     */
    public List<Categorias> listaCategorias();

    /**
     * Método encargado de crear Categorias.
     *
     * @param listaC Lista de las Categorias que se van a crear.
     */
    public void crearCategorias(List<Categorias> listaC);

    /**
     * Método encargado de editar Categorias.
     *
     * @param listaC Lista de las Categorias que se van a modificar.
     */
    public void editarCategorias(List<Categorias> listaC);

    /**
     * Método encargado de borrar Categorias.
     *
     * @param listaC Lista de las Categorias que se van a eliminar.
     */
    public void borrarCategorias(List<Categorias> listaC);

    /**
     * Método encargado de recuperar todos las ClasesCategorias.
     *
     * @return Retorna una lista de ClasesCategorias.
     */
    public List<ClasesCategorias> lovClasesCategorias();

    /**
     * Método encargado de recuperar todos las ClasesCategorias.
     *
     * @return Retorna una lista de ClasesCategorias.
     */
    public List<TiposSueldos> lovTiposSueldos();

    /**
     * Método encargado de recuperar todos las ClasesCategorias.
     *
     * @return Retorna una lista de ClasesCategorias.
     */
    public List<Conceptos> lovConceptos();
    
    /**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);

}
