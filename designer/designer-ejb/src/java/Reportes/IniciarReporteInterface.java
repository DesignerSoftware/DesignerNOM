package Reportes;

import java.sql.Connection;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.fill.AsynchronousFilllListener;

/**
 *
 * @author Administrator
 */
public interface IniciarReporteInterface {

    public String ejecutarReportinho(String nombreReporte, String rutaReporte, String rutaGenerado, String nombreArchivo, String tipoReporte, Connection cxn);

    public String ejecutarReporte(String nombreReporte, String rutaReporte, String rutaGenerado, String nombreArchivo, String tipoReporte, Connection cxn);

    public void cerrarConexion();

    public void inicarC();

    public void cancelarReporte();

    public void llenarReporte(String nombreReporte, String rutaReporte, AsynchronousFilllListener asistenteReporte);

    public String crearArchivoReporte(String rutaGenerado, String nombreArchivo, String tipoReporte, JasperPrint imprimir);
}
