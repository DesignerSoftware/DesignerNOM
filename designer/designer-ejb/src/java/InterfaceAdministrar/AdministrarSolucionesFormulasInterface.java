/**
 * Documentación a cargo de Andres Pineda
 */
package InterfaceAdministrar;

import Entidades.Empleados;
import Entidades.Novedades;
import Entidades.SolucionesFormulas;
import java.math.BigInteger;
import java.util.List;

/**
 * Interface encargada de determinar las operaciones lógicas necesarias para la
 * pantalla 'SolucionFormula'.
 *
 * @author betelgeuse
 */
public interface AdministrarSolucionesFormulasInterface {

    /**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
    
    /**
     * Método encargado de recuperar los datos de SolucionesFormulas con
     * respecto a los parametro de la Secuencia del Empleado y la Secuencia de
     * la Novedad.
     *
     * @param secEmpleado Secuencia del Empleado referenciado.
     * @param secNovedad Secuencia de la Novedad referenciada.
     * @return Retorna la lista de SolucionesFormulas.
     */
    public List<SolucionesFormulas> listaSolucionesFormulaParaEmpleadoYNovedad(BigInteger secEmpleado, BigInteger secNovedad);

    /**
     * Método encargado de recuperar un Empleado con respecto al codigo del
     * mismo.
     *
     * @param codEmpleado Codigo Empleado a buscar.
     * @return Retorna el Empleado referenciado por el codigo.
     */
    public Empleados empleadoActual(BigInteger codEmpleado);

    /**
     * Método encargado de recuperar una Novedad con respecto a la secuencia de
     * la misma.
     *
     * @param secNovedad Secuencia Novedad a buscar.
     * @return Retorna la Novedad referenciado por la secuencia.
     */
    public Novedades novedadActual(BigInteger secNovedad);
    
    public void borrarSolucionesFormulas(List<SolucionesFormulas> listaBorrar);
}
