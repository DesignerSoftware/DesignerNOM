/**
 * Documentación a cargo de Andres Pineda
 */
package InterfaceAdministrar;

import Entidades.EscalafonesSalariales;
import Entidades.GruposSalariales;
import Entidades.TiposTrabajadores;
import java.math.BigInteger;
import java.util.List;

/**
 * Interface encargada de determinar las operaciones lógicas necesarias para la
 * pantalla 'EscalafonSalarial'.
 *
 * @author AndresPineda
 */
public interface AdministrarEscalafonesSalarialesInterface {

    /**
     * Método encargado de obtener el Entity Manager el cual tiene asociado la
     * sesion del usuario que utiliza el aplicativo.
     *
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);

    /**
     * Método encargado de recuperar todos los EscalafonesSalariales.
     *
     * @return Retorna una lista de EscalafonesSalariales.
     */
    public List<EscalafonesSalariales> listaEscalafonesSalariales();

    /**
     * Método encargado de crear EscalafonesSalariales.
     *
     * @param listaES Lista de los EscalafonesSalariales que se van a crear.
     */
    public void crearEscalafonesSalariales(List<EscalafonesSalariales> listaES);

    /**
     * Método encargado de editar EscalafonesSalariales.
     *
     * @param listaES Lista de los EscalafonesSalariales que se van a modificar.
     */
    public void editarEscalafonesSalariales(List<EscalafonesSalariales> listaES);

    /**
     * Método encargado de borrar EscalafonesSalariales.
     *
     * @param listaES Lista de los EscalafonesSalariales que se van a eliminar.
     */
    public void borrarEscalafonesSalariales(List<EscalafonesSalariales> listaES);

    /**
     * Método encargado de recuperar todos los GruposSalariales dados para la
     * secuencia de un EscalafonSalarial.
     *
     * @param secEscalafon Secuencia del EscalafonSalarial
     * @return Retorna una lista de GruposSalariales.
     */
    public List<GruposSalariales> listaGruposSalarialesParaEscalafonSalarial(BigInteger secEscalafon);

    /**
     * Método encargado de crear GruposSalariales.
     *
     * @param listaGS Lista de los GruposSalariales que se van a crear.
     */
    public void crearGruposSalariales(List<GruposSalariales> listaGS);

    /**
     * Método encargado de editar GruposSalariales.
     *
     * @param listaGS Lista de los GruposSalariales que se van a modificar.
     */
    public void editarGruposSalariales(List<GruposSalariales> listaGS);

    /**
     * Método encargado de borrar GruposSalariales.
     *
     * @param listaGS Lista de los GruposSalariales que se van a eliminar.
     */
    public void borrarGruposSalariales(List<GruposSalariales> listaGS);

    /**
     * Método encargado de recuperar todos los TiposTrabajadores.
     *
     * @return Retorna una lista de TiposTrabajadores.
     */
    public List<TiposTrabajadores> lovTiposTrabajadores();

}
