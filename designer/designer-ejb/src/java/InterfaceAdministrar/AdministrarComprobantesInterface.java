/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfaceAdministrar;

import Entidades.DetallesFormulas;
import Entidades.Parametros;
import Entidades.ParametrosEstructuras;
import Entidades.SolucionesNodos;
import java.math.BigInteger;
import java.util.List;

/**
 * Interface encargada de determinar las operaciones lógicas necesarias para la
 * pantalla 'Comprobantes'.
 *
 * @author betelgeuse
 */
public interface AdministrarComprobantesInterface {

    /**
     * Método encargado de obtener el Entity Manager el cual tiene asociado la
     * sesion del usuario que utiliza el aplicativo.
     *
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);

    /**
     * Método encargado de recuperar los Parámetros del usuario que esta usando
     * la sesión del aplicativo.
     *
     * @return Retorna una lista con los Parámetros asociados al usuario.
     */
    public List<Parametros> consultarParametrosComprobantesActualUsuario();

    /**
     * Método encargado de recuperar el ParámetroEstructura del usuario que esta
     * usando la sesión del aplicativo.
     *
     * @return Retorna un ParámetroEstructura asociado al usuario.
     */
    public ParametrosEstructuras consultarParametroEstructuraActualUsuario();

    /**
     * Método encargado de recuperar las SolucionesNodos que se tendrán en
     * cuenta para generar el comprobante de un empleado específico.
     *
     * @param secEmpleado Secuencia del empleado.
     * @return Retorna la lista de SolucionesNodos que completan el comprobante
     * de un empleado.
     */
    public List<SolucionesNodos> consultarSolucionesNodosEmpleado(BigInteger secEmpleado);

    /**
     * Método encargado de recuperar las SolucionesNodos que se tendrán en
     * cuenta para generar el comprobante de un empleador específico.
     *
     * @param secEmpleado Secuencia del empleador.
     * @return Retorna la lista de SolucionesNodos que completan el comprobante
     * de un empleador.
     */
    public List<SolucionesNodos> consultarSolucionesNodosEmpleador(BigInteger secEmpleado);

    /**
     * Método encargado de recibir la información de la formula que afecta en
     * los cálculos de un empleado específico en un momento definido.
     *
     * @param secEmpleado Secuencia del empleado participante.
     * @param fechaDesde Fecha inicial del rango.
     * @param fechaHasta Fecha final del rango.
     * @param secProceso Secuencia del proceso realizado.
     * @param secHistoriaFormula Secuencia de la HistoriaFormula.
     * @return Retorna una lista de DetallesFormulas con la información
     * requerida.
     */
    public List<DetallesFormulas> consultarDetallesFormulasEmpleado(BigInteger secEmpleado, String fechaDesde, String fechaHasta, BigInteger secProceso, BigInteger secHistoriaFormula);

    /**
     * Método encargado de recuperar la secuencia de la última HistoriaFormula a
     * una fecha dada y que está asociada con una formula especifica.
     *
     * @param secFormula Secuencia de la formula de la que se quiere conocer la
     * HistoriaFormula.
     * @param fechaDesde Fecha a la cual se quiere saber, la ultima
     * HistoriaFormula hecha.
     * @return Retorna la secuencia de la ultima HistoriaFormula de una formula
     * a una fecha específica.
     */
    public BigInteger consultarHistoriaFormulaFormula(BigInteger secFormula, String fechaDesde);
}
