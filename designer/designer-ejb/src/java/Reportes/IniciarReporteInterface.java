package Reportes;

import java.sql.Connection;
import java.util.Map;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.fill.AsynchronousFilllListener;

/**
 *
 * @author Administrator
 */
public interface IniciarReporteInterface {

    public String ejecutarReportinho(String nombreReporte, String rutaReporte, String rutaGenerado, String nombreArchivo, String tipoReporte, Connection cxn);

    public String ejecutarReporte(String nombreReporte, String rutaReporte, String rutaGenerado, String nombreArchivo, String tipoReporte, Connection cxn, Map parametrosemp);

    public void cerrarConexion();

    public void inicarC();

    public void cancelarReporte();

    public void llenarReporte(String nombreReporte, String rutaReporte, AsynchronousFilllListener asistenteReporte) throws Exception;

    public String crearArchivoReporte(String rutaGenerado, String nombreArchivo, String tipoReporte, JasperPrint imprimir);

    public String ejecutarReporteCifraControl(String nombreReporte, String rutaReporte, String rutaGenerado, String nombreArchivo, String tipoReporte, Connection cxn, Map paramFecha);
    
    public String ejecutarReporteEstadisGlobal(String nombreReporte, String rutaReporte, String rutaGenerado, String nombreArchivo, String tipoReporte, Connection cxn, Map paramGlobal);
    
    public String ejecutarReporteEstadisResumido(String nombreReporte, String rutaReporte, String rutaGenerado, String nombreArchivo, String tipoReporte, Connection cxn, Map paramResumido);

    public String ejecutarReporteFuncionesCargo(String nombreReporte, String rutaReporte, String rutaGenerado, String nombreArchivo, String tipoReporte, Connection cxn, Map param);

    public String ejecutarReporteHojaVida(String nombreReporte, String rutaReporte, String rutaGenerado, String nombreArchivo, String tipoReporte, Connection cxn, Map codEmpl);

    public String ejecutarReportePlanta1(String nombreReporte, String rutaReporte, String rutaGenerado, String nombreArchivo, String tipoReporte, Connection cxn);

    public String ejecutarReporteSegUsuarios(String nombreReporte, String rutaReporte, String rutaGenerado, String nombreArchivo, String tipoReporte, Connection cxn);

    public String ejecutarReporteHistoricosUsuarios(String nombreReporte, String rutaReporte, String rutaGenerado, String nombreArchivo, String tipoReporte, Connection cxn, Map param);

    public String ejecutarReportePantallas(String nombreReporte, String rutaReporte, String rutaGenerado, String nombreArchivo, String tipoReporte, Connection cxn);

    public String ejecutarReporteObjetos(String nombreReporte, String rutaReporte, String rutaGenerado, String nombreArchivo, String tipoReporte, Connection cxn);

}
