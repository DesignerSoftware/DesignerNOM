package Reportes;

import java.io.File;
import java.io.Serializable;
import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.Stateless;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.JExcelApiExporterParameter;
import net.sf.jasperreports.engine.export.JExcelApiMetadataExporter;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRCsvMetadataExporter;
import net.sf.jasperreports.engine.export.JRCsvMetadataExporterParameter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.JRTextExporter;
import net.sf.jasperreports.engine.export.JRXhtmlExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.fill.AsynchronousFillHandle;
import net.sf.jasperreports.engine.fill.AsynchronousFilllListener;
import net.sf.jasperreports.engine.query.QueryExecuterFactory;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.*;
import net.sf.jasperreports.web.servlets.AsyncJasperPrintAccessor;
import net.sf.jasperreports.web.servlets.JasperPrintAccessor;
import org.apache.log4j.Logger;

@Stateless
public class IniciarReporte implements IniciarReporteInterface, Serializable {

    private static Logger log = Logger.getLogger(IniciarReporte.class);

    Connection conexion = null;
    AsynchronousFillHandle handle;

    @Override
    public void inicarC() {
    }

    @Override
    public String ejecutarReporte(String nombreReporte, String rutaReporte, String rutaGenerado, String nombreArchivo, String tipoReporte, Connection cxn, Map parametrosemp) {
        try {
            log.warn("INICIARREPORTE rutaReporte: " + rutaReporte);
            log.warn("INICIARREPORTE nombreArchivo: " + nombreArchivo);
            log.warn("INICIARREPORTE tipoReporte: " + tipoReporte);
            System.out.println("parametrosemp: " + parametrosemp);
            Map parametros = new HashMap();
            parametros.put("RutaReportes", rutaReporte);
            if (parametrosemp != null && !parametrosemp.isEmpty()) {
//                if (parametrosemp.containsKey("envioMasivo")) {
                    System.out.println("Buenas Ingrese a parametros.put");
                    parametros.put("empleadoDesde", parametrosemp.get("empleadoDesde"));
                    parametros.put("empleadoHasta", parametrosemp.get("empleadoHasta"));
//                }
            }
            System.out.println("parametros antes de generar: " + parametros);
            File archivo = new File(rutaReporte + nombreReporte + ".jasper");
            JasperReport masterReport;
            masterReport = (JasperReport) JRLoader.loadObject(archivo);
            masterReport.setProperty("net.sf.jasperreports.query.executer.factory.plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
            //JRProperties.setProperty( JRQueryExecuterFactory.QUERY_EXECUTER_FACTORY_PREFIX+"plsql","com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
            JasperReportsContext jasperReportsContext = DefaultJasperReportsContext.getInstance();
            JRPropertiesUtil jrPropertiesUtil = JRPropertiesUtil.getInstance(jasperReportsContext);
            jrPropertiesUtil.setProperty(QueryExecuterFactory.QUERY_EXECUTER_FACTORY_PREFIX + "plsql", "net.sf.jasperreports.engine.query.PlSqlQueryExecuterFactory");
            log.warn("INICIARREPORTE creo master ");
            JasperPrint imprimir = JasperFillManager.fillReport(masterReport, parametros, cxn);
            log.warn("INICIARREPORTE lleno reporte ");
            System.out.println("parametros: " + parametros);
            String outFileName = rutaGenerado + nombreArchivo;
            log.warn("INICIARREPORTE outFileName: " + outFileName);
            JRExporter exporter = null;
            switch (tipoReporte) {
                case "PDF":
                    exporter = new JRPdfExporter();
                    break;
                case "XLSX":
                    exporter = new JRXlsxExporter();
                    break;
                case "XLS":
//                    exporter = new JExcelApiMetadataExporter();
//                    exporter.setParameter(JExcelApiExporterParameter.IS_FONT_SIZE_FIX_ENABLED, Boolean.TRUE);
//                    exporter.setParameter(JExcelApiExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
//                    exporter.setParameter(JExcelApiExporterParameter.IS_IGNORE_CELL_BACKGROUND, Boolean.TRUE);
                   JRXlsExporter xlsExporter = new JRXlsExporter();
                   xlsExporter.setExporterInput(new SimpleExporterInput(imprimir));
                   xlsExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(new File(outFileName)));
                   SimpleXlsReportConfiguration con = new SimpleXlsReportConfiguration();
//                   con.setFontSizeFixEnabled(Boolean.TRUE);
//                   con.setWhitePageBackground(Boolean.FALSE);
//                   con.setIgnoreCellBackground(Boolean.TRUE);
                   con.setRemoveEmptySpaceBetweenColumns(Boolean.TRUE);
                   xlsExporter.setConfiguration(con);
                   xlsExporter.exportReport();
                    break;
                case "CSV":
//                    exporter = new JRCsvMetadataExporter();
//                    csvExporter.setParameter(JRCsvMetadataExporterParameter.CHARACTER_ENCODING, "ISO-8859-1");
//                    exporter.setParameter(JRCsvExporterParameter.RECORD_DELIMITER, "\r\n");
                    JRCsvExporter csvExporter = new JRCsvExporter();
                    csvExporter.setExporterInput(new SimpleExporterInput(imprimir));
                    csvExporter.setExporterOutput(new SimpleWriterExporterOutput(new File(outFileName)));
                    SimpleCsvExporterConfiguration config = new SimpleCsvExporterConfiguration();
                    config.setRecordDelimiter("\r\n");
                    csvExporter.setConfiguration(config);
                    csvExporter.exportReport();
                    break;
                case "HTML":
                    exporter = new JRXhtmlExporter();
                    exporter.setParameter(JRHtmlExporterParameter.IS_OUTPUT_IMAGES_TO_DIR, Boolean.FALSE);
                    exporter.setParameter(JRHtmlExporterParameter.IMAGES_URI, "image?image=");
                    break;
                case "RTF":
                    //exporter = new JRDocxExporter();
                    exporter = new JRRtfExporter();
                    //exporter.setParameter(JRDocxExporterParameter., Boolean.FALSE);
                    break;
                case "DELIMITED":
                    JRTextExporter txtexporter = new JRTextExporter();
                    txtexporter.setExporterInput(new SimpleExporterInput(imprimir));
                    txtexporter.setExporterOutput(new SimpleWriterExporterOutput(new File(outFileName)));
                    SimpleTextExporterConfiguration c = new SimpleTextExporterConfiguration();
                    c.setTrimLineRight(Boolean.TRUE);
                    c.setLineSeparator("\r\n");
                    c.isOverrideHints();
                    txtexporter.setConfiguration(c);
                    SimpleTextReportConfiguration conf = new SimpleTextReportConfiguration();
                    conf.getCharHeight();
                    conf.getCharWidth();
                    txtexporter.setConfiguration(conf);
                    txtexporter.exportReport();
                    break;
                case "TXT":
//                    exporter = new JRTextExporter();
//                    exporter.setParameter(JRTextExporterParameter.CHARACTER_WIDTH, new Integer(687).floatValue());
//                    exporter.setParameter(JRTextExporterParameter.CHARACTER_HEIGHT, new Integer(15).floatValue());
                    txtexporter = new JRTextExporter();
                    txtexporter.setExporterInput(new SimpleExporterInput(imprimir));
                    txtexporter.setExporterOutput(new SimpleWriterExporterOutput(new File(outFileName)));
                    c = new SimpleTextExporterConfiguration();
                    c.setTrimLineRight(Boolean.TRUE);
                    c.setLineSeparator("\r\n");
                    txtexporter.setConfiguration(c);                    
                    txtexporter.exportReport();
                    break;
                default:
                    break;
            }
            if (exporter != null) {
                exporter.setParameter(JRExporterParameter.JASPER_PRINT, imprimir);
                exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, outFileName);
                exporter.exportReport();
            }
            return outFileName;
        } catch (Exception e) {
            log.error("Error IniciarReporte.ejecutarReporte: " + e);
            if (e.getCause() != null) {
                return "Error: " + e.toString() + "\n" + e.getCause().toString();
            } else {
                return "Error: " + e.toString();
            }
        }
    }

    public String ejecutarReportinho(String nombreReporte, String rutaReporte, String rutaGenerado, String nombreArchivo, String tipoReporte, Connection cxn) {
        try {
            inicarC();
            File archivo = new File(rutaReporte + nombreReporte + ".jasper");
            JasperReport masterReport;
            JasperPrintAccessor jpAcceso;
            JasperPrint imprimir;
            masterReport = (JasperReport) JRLoader.loadObject(archivo);
            handle = AsynchronousFillHandle.createHandle(masterReport, null, conexion);
            handle.addListener(new AsynchronousFilllListener() {
                public void reportFinished(JasperPrint print) {
                }

                public void reportCancelled() {
                }

                public void reportFillError(Throwable t) {
                }
            });
            //handle.startFill();
            AsyncJasperPrintAccessor acceso = new AsyncJasperPrintAccessor(handle);
            handle.startFill();
            jpAcceso = acceso;
            imprimir = acceso.getFinalJasperPrint();
            String outFileName = rutaGenerado + nombreArchivo;
            JRExporter exporter = null;
            if (tipoReporte.equals("PDF")) {
                exporter = new JRPdfExporter();
            } else if (tipoReporte.equals("XLSX")) {
                exporter = new JRXlsxExporter();
            } else if (tipoReporte.equals("XLS")) {
                exporter = new JExcelApiMetadataExporter();
                exporter.setParameter(JExcelApiExporterParameter.IS_FONT_SIZE_FIX_ENABLED, Boolean.TRUE);
                exporter.setParameter(JExcelApiExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
                exporter.setParameter(JExcelApiExporterParameter.IS_IGNORE_CELL_BACKGROUND, Boolean.TRUE);
            } else if (tipoReporte.equals("CSV")) {
                exporter = new JRCsvMetadataExporter();
                exporter.setParameter(JRCsvMetadataExporterParameter.CHARACTER_ENCODING, "ISO-8859-1");
            } else if (tipoReporte.equals("HTML")) {
                exporter = new JRXhtmlExporter();
                exporter.setParameter(JRHtmlExporterParameter.IS_OUTPUT_IMAGES_TO_DIR, Boolean.FALSE);
            } else if (tipoReporte.equals("DOCX")) {
                //exporter = new JRDocxExporter();
                exporter = new JRRtfExporter();
                //exporter.setParameter(JRDocxExporterParameter., Boolean.FALSE);
            }
            if (exporter != null) {
                exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, outFileName);
                exporter.setParameter(JRExporterParameter.JASPER_PRINT, imprimir);
                exporter.exportReport();
            }
            //handle = null;
            //cerrarConexion();
            return outFileName;
        } catch (Exception e) {
            log.error("Error ejecutarReportinho IniciarReporte.ejecutarReporte: " + e);
            if (e.getCause() != null) {
                return "Error: " + e.toString() + "\n" + e.getCause().toString();
            } else {
                return "Error: " + e.toString();
            }
        }
    }

    @Override
    public void llenarReporte(String nombreReporte, String rutaReporte, AsynchronousFilllListener asistenteReporte) throws Exception {
        try {
            inicarC();
            File archivo = new File(rutaReporte + nombreReporte + ".jasper");
            JasperReport masterReport;
            masterReport = (JasperReport) JRLoader.loadObject(archivo);
            handle = AsynchronousFillHandle.createHandle(masterReport, null, conexion);
            handle.addListener(asistenteReporte);
            handle.startFill();
        } catch (Exception ex) {
            log.error("Error antes de llenar el reporte (llenarReporte)\n" + ex);
            throw ex;
        }
    }

    public String crearArchivoReporte(String rutaGenerado, String nombreArchivo, String tipoReporte, JasperPrint imprimir) {
        JRExporter exporter = null;
        if (tipoReporte.equals("PDF")) {
            exporter = new JRPdfExporter();
            nombreArchivo = nombreArchivo + ".pdf";
        } else if (tipoReporte.equals("XLSX")) {
            exporter = new JRXlsxExporter();
            nombreArchivo = nombreArchivo + ".xlsx";
        } else if (tipoReporte.equals("XLS")) {
            exporter = new JExcelApiMetadataExporter();
            exporter.setParameter(JExcelApiExporterParameter.IS_FONT_SIZE_FIX_ENABLED, Boolean.TRUE);
            exporter.setParameter(JExcelApiExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
            exporter.setParameter(JExcelApiExporterParameter.IS_IGNORE_CELL_BACKGROUND, Boolean.TRUE);
            nombreArchivo = nombreArchivo + ".xls";
        } else if (tipoReporte.equals("CSV")) {
            exporter = new JRCsvMetadataExporter();
            exporter.setParameter(JRCsvMetadataExporterParameter.CHARACTER_ENCODING, "ISO-8859-1");
            nombreArchivo = nombreArchivo + ".csv";
        } else if (tipoReporte.equals("HTML")) {
            exporter = new JRXhtmlExporter();
            exporter.setParameter(JRHtmlExporterParameter.IS_OUTPUT_IMAGES_TO_DIR, Boolean.FALSE);
            nombreArchivo = nombreArchivo + ".html";
        } else if (tipoReporte.equals("DOCX")) {
            //exporter = new JRDocxExporter();
            exporter = new JRRtfExporter();
            nombreArchivo = nombreArchivo + ".rtf";
            //exporter.setParameter(JRDocxExporterParameter., Boolean.FALSE);
        }
        if (exporter != null) {
            String outFileName = rutaGenerado + nombreArchivo;
            try {
                exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, outFileName);
                exporter.setParameter(JRExporterParameter.JASPER_PRINT, imprimir);
                exporter.exportReport();
                return outFileName;
            } catch (Exception e) {
                log.error("Error crear archivo reporte \n" + e);
                if (e.getCause() != null) {
                    return "Error: " + e.toString() + "\n" + e.getCause().toString();
                } else {
                    return "Error: " + e.toString();
                }
            }
        }
        return null;
    }

    @Override
    public void cancelarReporte() {
        try {
            handle.cancellFill();
            handle = null;
        } catch (Exception ex) {
        }
    }

    @Override
    public void cerrarConexion() {
        try {
            conexion.close();
        } catch (Exception e) {
        }
    }

    @Override
    public String ejecutarReporteCifraControl(String nombreReporte, String rutaReporte, String rutaGenerado, String nombreArchivo, String tipoReporte, Connection cxn, Map paramFecha) {
        try {
            log.warn("INICIARREPORTE NombreReporte: " + nombreReporte);
            log.warn("INICIARREPORTE rutaReporte: " + rutaReporte);
            log.warn("INICIARREPORTE rutaGenerado: " + rutaGenerado);
            log.warn("INICIARREPORTE nombreArchivo: " + nombreArchivo);
            log.warn("INICIARREPORTE tipoReporte: " + tipoReporte);
            log.warn("INICIARREPORTE ejecutarCifraControl Map : " + paramFecha);
            File archivo = new File(rutaReporte + nombreReporte + ".jasper");
            JasperReport masterReport;
            masterReport = (JasperReport) JRLoader.loadObject(archivo);
            log.warn("INICIARREPORTE creo master ");
            Map parametros = new HashMap();
            parametros.put("RutaReportes", rutaReporte);
            if (paramFecha != null && !paramFecha.isEmpty()) {
                parametros.put("fechaDesde", paramFecha.get("fechaDesde"));
                parametros.put("fechaHasta", paramFecha.get("fechaHasta"));
            }
            JasperPrint imprimir = JasperFillManager.fillReport(masterReport, parametros, cxn);
            log.warn("INICIARREPORTE lleno reporte ");
            String outFileName = rutaGenerado + nombreArchivo;
            log.warn("INICIARREPORTE outFileName: " + outFileName);
            JRExporter exporter = null;
            if (tipoReporte.equals("PDF")) {
                exporter = new JRPdfExporter();
            }
            if (exporter != null) {
                exporter.setParameter(JRExporterParameter.JASPER_PRINT, imprimir);
                exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, outFileName);
                exporter.exportReport();
            }
            return outFileName;
        } catch (Exception e) {
            log.error("Error IniciarReporte.ejecutarReporte: " + e);
            if (e.getCause() != null) {
                return "Error: " + e.toString() + "\n" + e.getCause().toString();
            } else {
                return "Error: " + e.toString();
            }
        }
    }

    @Override
    public String ejecutarReporteEstadisGlobal(String nombreReporte, String rutaReporte, String rutaGenerado, String nombreArchivo, String tipoReporte, Connection cxn, Map paramGlobal) {
        try {
            log.warn("INICIARREPORTE NombreReporte: " + nombreReporte);
            log.warn("INICIARREPORTE rutaReporte: " + rutaReporte);
            log.warn("INICIARREPORTE rutaGenerado: " + rutaGenerado);
            log.warn("INICIARREPORTE nombreArchivo: " + nombreArchivo);
            log.warn("INICIARREPORTE tipoReporte: " + tipoReporte);
            log.warn("INICIARREPORTE ejecutarReporteEstadisGlobal Map : " + paramGlobal);
            File archivo = new File(rutaReporte + nombreReporte + ".jasper");
            JasperReport masterReport;
            masterReport = (JasperReport) JRLoader.loadObject(archivo);
            log.warn("INICIARREPORTE creo master ");
            Map parametros = new HashMap();
//            parametros.put("RutaReportes", rutaReporte);
            if (paramGlobal != null && !paramGlobal.isEmpty()) {
                parametros.put("TABLA", paramGlobal.get("TABLA"));
                parametros.put("TABLALB", paramGlobal.get("TABLALB"));
            }
            JasperPrint imprimir = JasperFillManager.fillReport(masterReport, parametros, cxn);
            log.warn("INICIARREPORTE lleno reporte ");
            String outFileName = rutaGenerado + nombreArchivo;
            log.warn("INICIARREPORTE outFileName: " + outFileName);
            JRExporter exporter = null;
            if (tipoReporte.equals("PDF")) {
                exporter = new JRPdfExporter();
            }
            if (exporter != null) {
                exporter.setParameter(JRExporterParameter.JASPER_PRINT, imprimir);
                exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, outFileName);
                exporter.exportReport();
            }
            log.warn("fin. " + outFileName);
            return outFileName;
        } catch (Exception e) {
            log.error("Error IniciarReporte.ejecutarReporte: " + e);
            if (e.getCause() != null) {
                return "Error: " + e.toString() + "\n" + e.getCause().toString();
            } else {
                return "Error: " + e.toString();
            }
        }
    }

    @Override
    public String ejecutarReporteEstadisResumido(String nombreReporte, String rutaReporte, String rutaGenerado, String nombreArchivo, String tipoReporte, Connection cxn, Map paramResumido) {
        try {
            File archivo = new File(rutaReporte + nombreReporte + ".jasper");
            JasperReport masterReport;
            masterReport = (JasperReport) JRLoader.loadObject(archivo);
            log.warn("INICIARREPORTE creo master ");
            Map parametros = new HashMap();
            parametros.put("RutaReportes", rutaReporte);
            if (paramResumido != null && !paramResumido.isEmpty()) {
                parametros.put("TABLA", paramResumido.get("TABLA"));
                parametros.put("TABLALB", paramResumido.get("TABLALB"));
                parametros.put("CONJUNTO", paramResumido.get("CONJUNTO"));
            }
            JasperPrint imprimir = JasperFillManager.fillReport(masterReport, parametros, cxn);
            log.warn("INICIARREPORTE lleno reporte ");
            String outFileName = rutaGenerado + nombreArchivo;
            log.warn("INICIARREPORTE outFileName: " + outFileName);
            JRExporter exporter = null;
            if (tipoReporte.equals("PDF")) {
                exporter = new JRPdfExporter();
            }
            if (exporter != null) {
                exporter.setParameter(JRExporterParameter.JASPER_PRINT, imprimir);
                exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, outFileName);
                exporter.exportReport();
            }
            log.warn("fin. " + outFileName);
            return outFileName;
        } catch (Exception e) {
            log.error("Error IniciarReporte.ejecutarReporte: " + e);
            if (e.getCause() != null) {
                return "Error: " + e.toString() + "\n" + e.getCause().toString();
            } else {
                return "Error: " + e.toString();
            }
        }
    }

    @Override
    public String ejecutarReporteFuncionesCargo(String nombreReporte, String rutaReporte, String rutaGenerado, String nombreArchivo, String tipoReporte, Connection cxn, Map param) {
        try {
            File archivo = new File(rutaReporte + nombreReporte + ".jasper");
            JasperReport masterReport;
            masterReport = (JasperReport) JRLoader.loadObject(archivo);
            log.warn("INICIARREPORTE creo master ");
            Map parametros = new HashMap();
            parametros.put("RutaReportes", rutaReporte);
            if (param != null && !param.isEmpty()) {
                parametros.put("estructura", param.get("estructura"));
                parametros.put("cargo", param.get("cargo"));
            }
            JasperPrint imprimir = JasperFillManager.fillReport(masterReport, parametros, cxn);
            log.warn("INICIARREPORTE lleno reporte ");
            String outFileName = rutaGenerado + nombreArchivo;
            JRExporter exporter = null;
            if (tipoReporte.equals("PDF")) {
                exporter = new JRPdfExporter();
            }
            if (exporter != null) {
                exporter.setParameter(JRExporterParameter.JASPER_PRINT, imprimir);
                exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, outFileName);
                exporter.exportReport();
            }
            return outFileName;
        } catch (Exception e) {
            log.error("Error IniciarReporte.ejecutarReporte: " + e);
            log.error("************************************");
            if (e.getCause() != null) {
                return "Error: " + e.toString() + "\n" + e.getCause().toString();
            } else {
                return "Error: " + e.toString();
            }
        }
    }

    @Override
    public String ejecutarReporteHojaVida(String nombreReporte, String rutaReporte, String rutaGenerado, String nombreArchivo, String tipoReporte, Connection cxn, Map param) {
        try {
            File archivo = new File(rutaReporte + nombreReporte + ".jasper");
            JasperReport masterReport;
            masterReport = (JasperReport) JRLoader.loadObject(archivo);
            log.warn("INICIARREPORTE creo master ");
            Map parametros = new HashMap();
//            parametros.put("RutaReportes", rutaReporte);
            if (param != null && !param.isEmpty()) {
                parametros.put("secEmpleado", param.get("secEmpleado"));
            }
            JasperPrint imprimir = JasperFillManager.fillReport(masterReport, parametros, cxn);
            log.warn("INICIARREPORTE lleno reporte ");
            String outFileName = rutaGenerado + nombreArchivo;
            log.warn("INICIARREPORTE outFileName: " + outFileName);
            JRExporter exporter = null;
            if (tipoReporte.equals("PDF")) {
                exporter = new JRPdfExporter();
            }
            if (exporter != null) {
                exporter.setParameter(JRExporterParameter.JASPER_PRINT, imprimir);
                exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, outFileName);
                exporter.exportReport();
            }
            log.warn("fin. " + outFileName);
            return outFileName;
        } catch (Exception e) {
            log.error("Error IniciarReporte.ejecutarReporteHojaVida: " + e);
            if (e.getCause() != null) {
                return "Error: " + e.toString() + "\n" + e.getCause().toString();
            } else {
                return "Error: " + e.toString();
            }
        }
    }

    @Override
    public String ejecutarReportePlanta1(String nombreReporte, String rutaReporte, String rutaGenerado, String nombreArchivo, String tipoReporte, Connection cxn) {
        try {
          
            File archivo = new File(rutaReporte + nombreReporte + ".jasper");
            JasperReport masterReport;
            masterReport = (JasperReport) JRLoader.loadObject(archivo);
            log.warn("INICIARREPORTE creo master ");
            JasperPrint imprimir = JasperFillManager.fillReport(masterReport, null, cxn);
            log.warn("INICIARREPORTE lleno reporte ");
            String outFileName = rutaGenerado + nombreArchivo;
            log.warn("INICIARREPORTE outFileName: " + outFileName);
            JRExporter exporter = null;
            if (tipoReporte.equals("PDF")) {
                exporter = new JRPdfExporter();
            }
            if (exporter != null) {
                exporter.setParameter(JRExporterParameter.JASPER_PRINT, imprimir);
                exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, outFileName);
                exporter.exportReport();
            }
            return outFileName;
        } catch (Exception e) {
            log.error("Error IniciarReporte.ejecutarReportePlanta1: " + e);
            log.error("************************************");
            if (e.getCause() != null) {
                return "Error: " + e.toString() + "\n" + e.getCause().toString();
            } else {
                return "Error: " + e.toString();
            }
        }
    }

    @Override
    public String ejecutarReporteSegUsuarios(String nombreReporte, String rutaReporte, String rutaGenerado, String nombreArchivo, String tipoReporte, Connection cxn) {
        try {
            
            File archivo = new File(rutaReporte + nombreReporte + ".jasper");
            JasperReport masterReport;
            masterReport = (JasperReport) JRLoader.loadObject(archivo);
            log.warn("INICIARREPORTE creo master ");
            Map parametros = new HashMap();
            parametros.put("RutaReportes", rutaReporte);
            JasperPrint imprimir = JasperFillManager.fillReport(masterReport, parametros, cxn);
            log.warn("INICIARREPORTE lleno reporte ");
            String outFileName = rutaGenerado + nombreArchivo;
            log.warn("INICIARREPORTE outFileName: " + outFileName);
            JRExporter exporter = null;
            if (tipoReporte.equals("PDF")) {
                exporter = new JRPdfExporter();
            }
            if (exporter != null) {
                exporter.setParameter(JRExporterParameter.JASPER_PRINT, imprimir);
                exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, outFileName);
                exporter.exportReport();
            }
            return outFileName;
        } catch (Exception e) {
            log.error("Error IniciarReporte.ejecutarReporteSegUsuarios: " + e);
            if (e.getCause() != null) {
                return "Error: " + e.toString() + "\n" + e.getCause().toString();
            } else {
                return "Error: " + e.toString();
            }
        }
    }

    @Override
    public String ejecutarReporteHistoricosUsuarios(String nombreReporte, String rutaReporte, String rutaGenerado, String nombreArchivo, String tipoReporte, Connection cxn, Map param) {
        try {
            
            File archivo = new File(rutaReporte + nombreReporte + ".jasper");
            JasperReport masterReport;
            masterReport = (JasperReport) JRLoader.loadObject(archivo);
            log.warn("INICIARREPORTE creo master ");
            Map parametros = new HashMap();
            parametros.put("RutaReportes", rutaReporte);
            if (param != null && !param.isEmpty()) {
                parametros.put("secuenciaUsuario", param.get("secuenciaUsuario"));
            }
            JasperPrint imprimir = JasperFillManager.fillReport(masterReport, parametros, cxn);
            log.warn("INICIARREPORTE lleno reporte ");
            String outFileName = rutaGenerado + nombreArchivo;
            JRExporter exporter = null;
            if (tipoReporte.equals("PDF")) {
                exporter = new JRPdfExporter();
            }
            if (exporter != null) {
                exporter.setParameter(JRExporterParameter.JASPER_PRINT, imprimir);
                exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, outFileName);
                exporter.exportReport();
            }
            log.warn("fin. " + outFileName);
            return outFileName;
        } catch (Exception e) {
            log.error("Error IniciarReporte.ejecutarReporte: " + e);
            if (e.getCause() != null) {
                return "Error: " + e.toString() + "\n" + e.getCause().toString();
            } else {
                return "Error: " + e.toString();
            }
        }
    }

    @Override
    public String ejecutarReportePantallas(String nombreReporte, String rutaReporte, String rutaGenerado, String nombreArchivo, String tipoReporte, Connection cxn) {
        try {
            File archivo = new File(rutaReporte + nombreReporte + ".jasper");
            JasperReport masterReport;
            masterReport = (JasperReport) JRLoader.loadObject(archivo);
            log.warn("INICIARREPORTE creo master ");
            Map parametros = new HashMap();
            parametros.put("RutaReportes", rutaReporte);
            JasperPrint imprimir = JasperFillManager.fillReport(masterReport, parametros, cxn);
            log.warn("INICIARREPORTE lleno reporte ");
            String outFileName = rutaGenerado + nombreArchivo;
            log.warn("INICIARREPORTE outFileName: " + outFileName);
            JRExporter exporter = null;
            if (tipoReporte.equals("PDF")) {
                exporter = new JRPdfExporter();
            }
            if (exporter != null) {
                exporter.setParameter(JRExporterParameter.JASPER_PRINT, imprimir);
                exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, outFileName);
                exporter.exportReport();
            }
            return outFileName;
        } catch (Exception e) {
            log.error("Error IniciarReporte.ejecutarReportePantallas: " + e);
            if (e.getCause() != null) {
                return "Error: " + e.toString() + "\n" + e.getCause().toString();
            } else {
                return "Error: " + e.toString();
            }
        }
    }

    @Override
    public String ejecutarReporteObjetos(String nombreReporte, String rutaReporte, String rutaGenerado, String nombreArchivo, String tipoReporte, Connection cxn) {
        try {
           
            File archivo = new File(rutaReporte + nombreReporte + ".jasper");
            JasperReport masterReport;
            masterReport = (JasperReport) JRLoader.loadObject(archivo);
            log.warn("INICIARREPORTE creo master ");
            Map parametros = new HashMap();
            parametros.put("RutaReportes", rutaReporte);
            JasperPrint imprimir = JasperFillManager.fillReport(masterReport, parametros, cxn);
            log.warn("INICIARREPORTE lleno reporte ");
            String outFileName = rutaGenerado + nombreArchivo;
            log.warn("INICIARREPORTE outFileName: " + outFileName);
            JRExporter exporter = null;
            if (tipoReporte.equals("PDF")) {
                exporter = new JRPdfExporter();
            }
            if (exporter != null) {
                exporter.setParameter(JRExporterParameter.JASPER_PRINT, imprimir);
                exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, outFileName);
                exporter.exportReport();
            }
            return outFileName;
        } catch (Exception e) {
            log.error("Error IniciarReporte.ejecutarReporteObjetos: " + e);
            if (e.getCause() != null) {
                return "Error: " + e.toString() + "\n" + e.getCause().toString();
            } else {
                return "Error: " + e.toString();
            }
        }
    }

}
