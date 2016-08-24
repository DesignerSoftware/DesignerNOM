/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfaceAdministrar;

import Entidades.Empleados;
import Entidades.VWAcumulados;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;

/**
 * Interface encargada de determinar las operaciones lógicas necesarias para la
 * pantalla 'EmplAcumulados'.
 *
 * @author betelgeuse.
 */
@Local
public interface AdministrarEmplAcumuladosInterface {

    /**
     * Método encargado de obtener el Entity Manager el cual tiene asociado la
     * sesion del usuario que utiliza el aplicativo.
     *
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);

    /**
     * Metodo encargado de recuperar las VWAcumulados asociadas a un Empleado
     * Específico.
     *
     * @param secEmpleado Secuencia del Empleado.
     * @return Lista de VWAcumulados.
     */
    public List<VWAcumulados> consultarVWAcumuladosEmpleado(BigInteger secEmpleado);

    /**
     * Metodo encargado de recuperar un Empleado dada su secuencia.
     *
     * @param secEmpleado Secuencia del Empleado
     * @return Retorna el Empleado cuya secuencia coincida con el valor dado por
     * parámetro.
     */
    public Empleados consultarEmpleado(BigInteger secEmpleado);
}
