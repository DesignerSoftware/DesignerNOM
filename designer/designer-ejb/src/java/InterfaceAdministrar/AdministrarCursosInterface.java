/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfaceAdministrar;

import Entidades.Cursos;
import java.util.List;

/**
 * Interface encargada de determinar las operaciones lógicas necesarias para la
 * pantalla 'Cursos'.
 *
 * @author betelgeuse
 */
public interface AdministrarCursosInterface {

    /**
     * Método encargado de recuperar todos los Cursos.
     *
     * @return Retorna una lista de Cursos.
     */
    public List<Cursos> consultarCursos();

    /**
     * Método encargado de obtener el Entity Manager el cual tiene asociado la
     * sesion del usuario que utiliza el aplicativo.
     *
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
    public void crear (List<Cursos> listaCrear);
    public void editar (List<Cursos> listaEditar);
    public void borrar (List<Cursos> listaBorrar);
}
