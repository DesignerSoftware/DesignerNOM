/**
 * Documentación a cargo de Andres Pineda
 */
package InterfaceAdministrar;

import Entidades.Formulas;
import Entidades.FormulasProcesos;
import Entidades.Operandos;
import Entidades.OperandosLogs;
import Entidades.Procesos;
import Entidades.Tipospagos;
import java.math.BigInteger;
import java.util.List;

/**
 * Interface encargada de determinar las operaciones lógicas necesarias para la
 * pantalla 'Proceso'.
 *
 * @author AndresPineda
 */
public interface AdministrarProcesosInterface {
	/**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
    /**
     * Método encargado de recuperar todos los Procesos.
     *
     * @return Retorna una lista de Procesos.
     */
    public List<Procesos> listaProcesos();

    /**
     * Método encargado de crear Procesos.
     *
     * @param listaP Lista de los Procesos que se van a crear.
     */
    public void crearProcesos(List<Procesos> listaP);

    /**
     * Método encargado de editar Procesos.
     *
     * @param listaP Lista de los Procesos que se van a modificar.
     */
    public void editarProcesos(List<Procesos> listaP);

    /**
     * Método encargado de borrar Procesos.
     *
     * @param listaP Lista de los Procesos que se van a eliminar.
     */
    public void borrarProcesos(List<Procesos> listaP);

    /**
     * Método encargado de recuperar todos las FormulasProcesos para un Proceso
     * dado por parametro.
     *
     * @param secuencia Secuencia del Proceso
     * @return Retorna una lista de FormulasProcesos.
     */
    public List<FormulasProcesos> listaFormulasProcesosParaProcesoSecuencia(BigInteger secuencia);

    /**
     * Método encargado de crear FormulasProcesos.
     *
     * @param listaFP Lista de los FormulasProcesos que se van a crear.
     */
    public void crearFormulasProcesos(List<FormulasProcesos> listaFP);

    /**
     * Método encargado de editar FormulasProcesos.
     *
     * @param listaFP Lista de los FormulasProcesos que se van a modificar.
     */
    public void editarFormulasProcesos(List<FormulasProcesos> listaFP);

    /**
     * Método encargado de borrar FormulasProcesos.
     *
     * @param listaFP Lista de los FormulasProcesos que se van a eliminar.
     */
    public void borrarFormulasProcesos(List<FormulasProcesos> listaFP);

    /**
     * Método encargado de recuperar todos los OperandosLogs para un Proceso
     * dado por parametro.
     *
     * @param secuencia Secuencia del Proceso
     * @return Retorna una lista de OperandosLogs.
     */
    public List<OperandosLogs> listaOperandosLogsParaProcesoSecuencia(BigInteger secuencia);

    /**
     * Método encargado de crear OperandosLogs.
     *
     * @param listaOL Lista de los OperandosLogs que se van a crear.
     */
    public void crearOperandosLogs(List<OperandosLogs> listaOL);

    /**
     * Método encargado de editar OperandosLogs.
     *
     * @param listaOL Lista de los OperandosLogs que se van a modificar.
     */
    public void editarOperandosLogs(List<OperandosLogs> listaOL);

    /**
     * Método encargado de borrar OperandosLogs.
     *
     * @param listaOL Lista de los OperandosLogs que se van a eliminar.
     */
    public void borrarOperandosLogs(List<OperandosLogs> listaOL);

    /**
     * Método encargado de recuperar todos las Formulas.
     *
     * @return Retorna una lista de Formulas.
     */
    public List<Formulas> lovFormulas();

    /**
     * Método encargado de recuperar todos los Tipospagos.
     *
     * @return Retorna una lista de Tipospagos.
     */
    public List<Tipospagos> lovTiposPagos();

    /**
     * Método encargado de recuperar todos los Operandos.
     *
     * @return Retorna una lista de Operandos.
     */
    public List<Operandos> lovOperandos();
    
    public String clonarProceso(String descripcionN, short codigoN, short codigoO);

}
