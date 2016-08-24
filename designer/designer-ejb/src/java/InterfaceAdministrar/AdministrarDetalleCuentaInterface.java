/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfaceAdministrar;

import Entidades.Cuentas;
import Entidades.VigenciasCuentas;
import java.math.BigInteger;
import java.util.List;

/**
 * Interface encargada de determinar las operaciones lógicas necesarias para la
 * pantalla 'DetalleConcepto'.
 *
 * @author betelgeuse.
 */
public interface AdministrarDetalleCuentaInterface {

    /**
     * Método encargado de obtener el Entity Manager el cual tiene asociado la
     * sesion del usuario que utiliza el aplicativo.
     *
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);

    /**
     * Método encargado de recuperar las VigenciasCuentas asociadas a una Cuenta
     * de credito.
     *
     * @param secCredito Secuencia de la cuenta de credito.
     * @return Retorna una lista de VigenciasCuentas cuya Cuenta de credito
     * tiene como secuencia el valor dado por parámetro.
     */
    public List<VigenciasCuentas> consultarListaVigenciasCuentasCredito(BigInteger secCredito);

    /**
     * Método encargado de recuperar las VigenciasCuentas asociadas a una Cuenta
     * debito.
     *
     * @param secDebito Secuencia de la cuenta de debito.
     * @return Retorna una lista de VigenciasCuentas cuya Cuenta debito tiene
     * como secuencia el valor dado por parámetro.
     */
    public List<VigenciasCuentas> consultarListaVigenciasCuentasDebito(BigInteger secDebito);

    /**
     * Método encargado de recuperar una Cuenta dada su secuencia.
     *
     * @param secCuenta Secuencia de la Cuenta.
     * @return Retorna la Cuenta cuya secuencia coincida con el valor del
     * parámetro.
     */
    public Cuentas mostrarCuenta(BigInteger secCuenta);
}
