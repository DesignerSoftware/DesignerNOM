/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfaceAdministrar;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.fill.AsynchronousFilllListener;

/**
 * Interface encargada de determinar las operaciones lógicas necesarias para realizar reportes. 
 * @author betelgeuse
 */
public interface AdministarReportesInterface {
    /**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
    /**
     * Método encargado de recuperar los datos de conexión del EntityManager el cual tiene
     * el usuario asociado a un perfil del aplicativo.
     */
    public void consultarDatosConexion();
    /**
     * Método encargado de generar el reporte que el usuario ha seleccionado.
     * @param nombreReporte Nombre del reporte.
     * @param tipoReporte Tipo de reporte.
     * @return Retorna la ubicacion del reporte generado.
     */
    public String generarReporte(String nombreReporte, String tipoReporte, AsynchronousFilllListener asistenteReporte);
    public String generarReporte(String nombreReporte, String tipoReporte);
    public void iniciarLlenadoReporte(String nombreReporte, AsynchronousFilllListener asistenteReporte);
    public String crearArchivoReporte(JasperPrint print, String tipoReporte);
    public void cancelarReporte();
}
