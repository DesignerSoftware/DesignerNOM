/**
 * Documentación a cargo de AndresPineda
 */
package InterfaceAdministrar;

import Entidades.Cargos;
import Entidades.Ciudades;
import Entidades.DetallesEmpresas;
import Entidades.Empleados;
import Entidades.Empresas;
import Entidades.Personas;
import java.math.BigInteger;
import java.util.List;

/**
 * Interface encargada de determinar las operaciones lógicas necesarias para la
 * pantalla 'DetalleEmpresa'.
 *
 * @author AndresPineda
 */
public interface AdministrarDetallesEmpresasInterface {

    /**
     * Método encargado de recuperar los DetallesEmpresas asociados a la
     * secuencia de una Empresa, si la secuencia se encuentra nula obtiene todos
     * los registros de la tabla DetallesEmpresas
     *
     * @param secEmpresa
     * @return Retorna una lista de DetallesEmpresas.
     */
    public List<DetallesEmpresas> listaDetallesEmpresasPorSecuencia(BigInteger secEmpresa);

    /**
     * Método encargado de crear DetallesEmpresas.
     *
     * @param listaDE Lista de los DetallesEmpresas que se van a crear.
     */
    public void crearDetalleEmpresa(List<DetallesEmpresas> listaDE);

    /**
     * Método encargado de editar DetallesEmpresas.
     *
     * @param listaDE Lista de los DetallesEmpresas que se van a modificar.
     */
    public void editarDetalleEmpresa(List<DetallesEmpresas> listaDE);

    /**
     * Método encargado de borrar DetallesEmpresas.
     *
     * @param listaDE Lista de los DetallesEmpresas que se van a eliminar.
     */
    public void borrarDetalleEmpresa(List<DetallesEmpresas> listaDE);

    /**
     * Método encargado de recuperar todas las Ciudades.
     *
     * @return Retorna una lista de Ciudades.
     */
    public List<Ciudades> lovCiudades();

    /**
     * Método encargado de recuperar todas los Empleados.
     *
     * @return Retorna una lista de Empleados.
     */
    public List<Empleados> lovEmpleados();

    /**
     * Método encargado de recuperar todas las Personas.
     *
     * @return Retorna una lista de Personas.
     */
    public List<Personas> lovPersonas();

    /**
     * Método encargado de recuperar todas los Cargos.
     *
     * @return Retorna una lista de Cargos.
     */
    public List<Cargos> lovCargos();

    /**
     * Método encargado de recuperar todas las Empresas.
     *
     * @return Retorna una lista de Empresas.
     */
    public List<Empresas> lovEmpresas();

    /**
     * Método encargado de recuperar una Empresa por medio la la secuencia dada
     * por parametro
     *
     * @param secEmpresa Secuencia de la Empresa
     * @return Empresa referenciada por la secuencia dada
     */
    public Empresas empresaActual(BigInteger secEmpresa);

    /**
     * Método encargado de obtener el Entity Manager el cual tiene asociado la
     * sesion del usuario que utiliza el aplicativo.
     *
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
}
