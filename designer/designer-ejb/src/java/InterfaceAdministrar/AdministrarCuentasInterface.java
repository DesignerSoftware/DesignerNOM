/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfaceAdministrar;

import Entidades.Cuentas;
import Entidades.Empresas;
import Entidades.Rubrospresupuestales;
import java.math.BigInteger;
import java.util.List;

/**
 * Interface encargada de determinar las operaciones lógicas necesarias para la
 * pantalla 'Cuentas'.
 *
 * @author betelgeuse
 */
public interface AdministrarCuentasInterface {

    /**
     * Método encargado de crear Cuentas.
     *
     * @param listaCuentas Lista de las Cuentas que se van a crear.
     */
    public void crearCuentas(List<Cuentas> listaCuentas);

    /**
     * Método encargado de editar Cuentas.
     *
     * @param listaCuentas Lista de las Cuentas que se van a modificar.
     */
    public void modificarCuentas(List<Cuentas> listaCuentas);

    /**
     * Método encargado de borrar Cuentas.
     *
     * @param listaCuentas Lista de las Cuentas que se van a eliminar.
     */
    public void borrarCuentas(List<Cuentas> listaCuentas);

    /**
     * Método encargado de recuperar las cuentas de una empresa.
     *
     * @param secEmpresa Secuencia de la empresa.
     * @return Retorna una lista de cuentas.
     */
    public List<Cuentas> consultarCuentasEmpresa(BigInteger secEmpresa);

    /**
     * Método encargado de recuperar todas las Empresas.
     *
     * @return Retorna una lista de Empresas.
     */
    public List<Empresas> consultarEmpresas();

    /**
     * Método encargado de recuperar los Rubrospresupuestales necesarios para la
     * lista de valores.
     *
     * @return Retorna una lista de Rubrospresupuestales.
     */
    public List<Rubrospresupuestales> consultarLOVRubros();

    /**
     * Método encargado de obtener el Entity Manager el cual tiene asociado la
     * sesion del usuario que utiliza el aplicativo.
     *
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
}
