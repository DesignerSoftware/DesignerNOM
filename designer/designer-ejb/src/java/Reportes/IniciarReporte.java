package Reportes;

import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.Stateless;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.JExcelApiExporterParameter;
import net.sf.jasperreports.engine.export.JExcelApiMetadataExporter;
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
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.web.servlets.AsyncJasperPrintAccessor;
import net.sf.jasperreports.web.servlets.JasperPrintAccessor;

@Stateless
public class IniciarReporte implements IniciarReporteInterface, Serializable {

   Connection conexion = null;
   AsynchronousFillHandle handle;

   @Override
   public void inicarC() {
      System.out.println("inicarC(). NO IMPLEMENTADO. ");
   }

   @Override
   public String ejecutarReporte(String nombreReporte, String rutaReporte, String rutaGenerado, String nombreArchivo, String tipoReporte, Connection cxn, Map parametrosemp) {
      try {
         System.out.println("INICIARREPORTE NombreReporte: " + nombreReporte);
         System.out.println("INICIARREPORTE rutaReporte: " + rutaReporte);
         System.out.println("INICIARREPORTE rutaGenerado: " + rutaGenerado);
         System.out.println("INICIARREPORTE nombreArchivo: " + nombreArchivo);
         System.out.println("INICIARREPORTE tipoReporte: " + tipoReporte);
         File archivo = new File(rutaReporte + nombreReporte + ".jasper");
         JasperReport masterReport;
         masterReport = (JasperReport) JRLoader.loadObject(archivo);
         System.out.println("INICIARREPORTE creo master ");
         Map parametros = new HashMap();
         parametros.put("RutaReportes", rutaReporte);
         if (parametrosemp != null && !parametrosemp.isEmpty()) {
            if (parametrosemp.containsKey("envioMasivo")) {
               parametros.put("empleadoDesde", parametrosemp.get("empleadoDesde"));
               parametros.put("empleadoHasta", parametrosemp.get("empleadoHasta"));
            }
         }
         JasperPrint imprimir = JasperFillManager.fillReport(masterReport, parametros, cxn);
         System.out.println("INICIARREPORTE lleno reporte ");
         String outFileName = rutaGenerado + nombreArchivo;
         System.out.println("INICIARREPORTE outFileName: " + outFileName);
         JRExporter exporter = null;
         switch (tipoReporte) {
            case "PDF":
               exporter = new JRPdfExporter();
               break;
            case "XLSX":
               exporter = new JRXlsxExporter();
               break;
            case "XLS":
               exporter = new JExcelApiMetadataExporter();
               exporter.setParameter(JExcelApiExporterParameter.IS_FONT_SIZE_FIX_ENABLED, Boolean.TRUE);
               exporter.setParameter(JExcelApiExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
               exporter.setParameter(JExcelApiExporterParameter.IS_IGNORE_CELL_BACKGROUND, Boolean.TRUE);
               break;
            case "CSV":
               exporter = new JRCsvMetadataExporter();
               exporter.setParameter(JRCsvMetadataExporterParameter.CHARACTER_ENCODING, "ISO-8859-1");
               break;
            case "HTML":
               exporter = new JRXhtmlExporter();
               exporter.setParameter(JRHtmlExporterParameter.IS_OUTPUT_IMAGES_TO_DIR, Boolean.FALSE);
               break;
            case "DOCX":
               //exporter = new JRDocxExporter();
               exporter = new JRRtfExporter();
               //exporter.setParameter(JRDocxExporterParameter., Boolean.FALSE);
               break;
            case "DELIMITED":
               exporter = new JRTextExporter();
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
      } catch (JRException e) {
         System.out.println("Error IniciarReporte.ejecutarReporte: " + e);
         if (e.getCause() != null) {
            return "INICIARREPORTE Error: " + e.toString() + "\n" + e.getCause().toString();
         } else {
            return "INICIARREPORTE Error: " + e.toString();
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
               System.out.println("Finalizado");
            }

            public void reportCancelled() {
               System.out.println("Cancelado");
            }

            public void reportFillError(Throwable t) {
               System.out.println("Error llenando el reporte");
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
      } catch (JRException e) {
         System.out.println("Error ejecutarReportinho IniciarReporte.ejecutarReporte: " + e);
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
         System.out.println("Inicio Llenado");
      } catch (Exception ex) {
         System.out.println("Error antes de llenar el reporte (llenarReporte)\n" + ex);
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
         } catch (JRException e) {
            System.out.println("Error crear archivo reporte \n" + e);
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
         System.out.println("Reporte cancelado. \n" + ex);
      }
   }

   @Override
   public void cerrarConexion() {
      try {
         conexion.close();
      } catch (Exception e) {
         System.out.println("Error Cerrar Conexión: " + e.getCause());
      }
   }

   @Override
   public String ejecutarReporteCifraControl(String nombreReporte, String rutaReporte, String rutaGenerado, String nombreArchivo, String tipoReporte, Connection cxn, Map paramFecha) {
      try {
         System.out.println("INICIARREPORTE NombreReporte: " + nombreReporte);
         System.out.println("INICIARREPORTE rutaReporte: " + rutaReporte);
         System.out.println("INICIARREPORTE rutaGenerado: " + rutaGenerado);
         System.out.println("INICIARREPORTE nombreArchivo: " + nombreArchivo);
         System.out.println("INICIARREPORTE tipoReporte: " + tipoReporte);
         System.out.println("INICIARREPORTE ejecutarCifraControl Map : " + paramFecha);
         File archivo = new File(rutaReporte + nombreReporte + ".jasper");
         JasperReport masterReport;
         masterReport = (JasperReport) JRLoader.loadObject(archivo);
         System.out.println("INICIARREPORTE creo master ");
         Map parametros = new HashMap();
         parametros.put("RutaReportes", rutaReporte);
         if (paramFecha != null && !paramFecha.isEmpty()) {
            parametros.put("fechaDesde", paramFecha.get("fechaDesde"));
            parametros.put("fechaHasta", paramFecha.get("fechaHasta"));
         }
         JasperPrint imprimir = JasperFillManager.fillReport(masterReport, parametros, cxn);
         System.out.println("INICIARREPORTE lleno reporte ");
         String outFileName = rutaGenerado + nombreArchivo;
         System.out.println("INICIARREPORTE outFileName: " + outFileName);
         JRExporter exporter = null;
         if (tipoReporte.equals("PDF")) {
            exporter = new JRPdfExporter();
         }
         if (exporter != null) {
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, imprimir);
            exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, outFileName);
            exporter.exportReport();
         }
         System.out.println("fin. " + outFileName);
         return outFileName;
      } catch (JRException e) {
         System.out.println("Error IniciarReporte.ejecutarReporte: " + e);
         if (e.getCause() != null) {
            return "INICIARREPORTE Error: " + e.toString() + "\n" + e.getCause().toString();
         } else {
            return "INICIARREPORTE Error: " + e.toString();
         }
      }
   }

   @Override
   public String ejecutarReporteEstadisGlobal(String nombreReporte, String rutaReporte, String rutaGenerado, String nombreArchivo, String tipoReporte, Connection cxn, Map paramGlobal) {
      try {
         System.out.println("INICIARREPORTE NombreReporte: " + nombreReporte);
         System.out.println("INICIARREPORTE rutaReporte: " + rutaReporte);
         System.out.println("INICIARREPORTE rutaGenerado: " + rutaGenerado);
         System.out.println("INICIARREPORTE nombreArchivo: " + nombreArchivo);
         System.out.println("INICIARREPORTE tipoReporte: " + tipoReporte);
         System.out.println("INICIARREPORTE ejecutarReporteEstadisGlobal Map : " + paramGlobal);
         File archivo = new File(rutaReporte + nombreReporte + ".jasper");
         JasperReport masterReport;
         masterReport = (JasperReport) JRLoader.loadObject(archivo);
         System.out.println("INICIARREPORTE creo master ");
         Map parametros = new HashMap();
//            parametros.put("RutaReportes", rutaReporte);
         if (paramGlobal != null && !paramGlobal.isEmpty()) {
            parametros.put("TABLA", paramGlobal.get("TABLA"));
            parametros.put("TABLALB", paramGlobal.get("TABLALB"));
         }
         JasperPrint imprimir = JasperFillManager.fillReport(masterReport, parametros, cxn);
         System.out.println("INICIARREPORTE lleno reporte ");
         String outFileName = rutaGenerado + nombreArchivo;
         System.out.println("INICIARREPORTE outFileName: " + outFileName);
         JRExporter exporter = null;
         if (tipoReporte.equals("PDF")) {
            exporter = new JRPdfExporter();
         }
         if (exporter != null) {
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, imprimir);
            exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, outFileName);
            exporter.exportReport();
         }
         System.out.println("fin. " + outFileName);
         return outFileName;
      } catch (JRException e) {
         System.out.println("Error IniciarReporte.ejecutarReporte: " + e);
         if (e.getCause() != null) {
            return "INICIARREPORTE Error: " + e.toString() + "\n" + e.getCause().toString();
         } else {
            return "INICIARREPORTE Error: " + e.toString();
         }
      }
   }

@Override
   public String ejecutarReporteEstadisResumido(String nombreReporte, String rutaReporte, String rutaGenerado, String nombreArchivo, String tipoReporte, Connection cxn, Map paramResumido) {
      try {
         System.out.println("INICIARREPORTE NombreReporte: " + nombreReporte);
         System.out.println("INICIARREPORTE rutaReporte: " + rutaReporte);
         System.out.println("INICIARREPORTE rutaGenerado: " + rutaGenerado);
         System.out.println("INICIARREPORTE nombreArchivo: " + nombreArchivo);
         System.out.println("INICIARREPORTE tipoReporte: " + tipoReporte);
         System.out.println("INICIARREPORTE ejecutarReporteEstadisResumido Map : " + paramResumido);
         File archivo = new File(rutaReporte + nombreReporte + ".jasper");
         JasperReport masterReport;
         masterReport = (JasperReport) JRLoader.loadObject(archivo);
         System.out.println("INICIARREPORTE creo master ");
         Map parametros = new HashMap();
         parametros.put("RutaReportes", rutaReporte);
         if (paramResumido != null && !paramResumido.isEmpty()) {
            parametros.put("TABLA", paramResumido.get("TABLA"));
            parametros.put("TABLALB", paramResumido.get("TABLALB"));
            parametros.put("CONJUNTO", paramResumido.get("CONJUNTO"));
         }
         JasperPrint imprimir = JasperFillManager.fillReport(masterReport, parametros, cxn);
         System.out.println("INICIARREPORTE lleno reporte ");
         String outFileName = rutaGenerado + nombreArchivo;
         System.out.println("INICIARREPORTE outFileName: " + outFileName);
         JRExporter exporter = null;
         if (tipoReporte.equals("PDF")) {
            exporter = new JRPdfExporter();
         }
         if (exporter != null) {
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, imprimir);
            exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, outFileName);
            exporter.exportReport();
         }
         System.out.println("fin. " + outFileName);
         return outFileName;
      } catch (JRException e) {
         System.out.println("Error IniciarReporte.ejecutarReporte: " + e);
         if (e.getCause() != null) {
            return "INICIARREPORTE Error: " + e.toString() + "\n" + e.getCause().toString();
         } else {
            return "INICIARREPORTE Error: " + e.toString();
         }
      }
   }

   @Override
   public String ejecutarReporteFuncionesCargo(String nombreReporte, String rutaReporte, String rutaGenerado, String nombreArchivo, String tipoReporte, Connection cxn, Map param) {
      try {
         System.out.println("INICIARREPORTE NombreReporte: " + nombreReporte);
         System.out.println("INICIARREPORTE rutaReporte: " + rutaReporte);
         System.out.println("INICIARREPORTE rutaGenerado: " + rutaGenerado);
         System.out.println("INICIARREPORTE nombreArchivo: " + nombreArchivo);
         System.out.println("INICIARREPORTE tipoReporte: " + tipoReporte);
         System.out.println("INICIARREPORTE ejecutarCifraControl Map : " + param);
         File archivo = new File(rutaReporte + nombreReporte + ".jasper");
         JasperReport masterReport;
         masterReport = (JasperReport) JRLoader.loadObject(archivo);
         System.out.println("INICIARREPORTE creo master ");
         Map parametros = new HashMap();
         parametros.put("RutaReportes", rutaReporte);
         if (param != null && !param.isEmpty()) {
            parametros.put("estructura", param.get("estructura"));
            parametros.put("cargo", param.get("cargo"));
         }
         JasperPrint imprimir = JasperFillManager.fillReport(masterReport, parametros, cxn);
         System.out.println("INICIARREPORTE lleno reporte ");
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
      } catch (JRException e) {
         System.out.println("Error IniciarReporte.ejecutarReporte: " + e);
         System.out.println("************************************");
         if (e.getCause() != null) {
            return "INICIARREPORTE Error: " + e.toString() + "\n" + e.getCause().toString();
         } else {
            return "INICIARREPORTE Error: " + e.toString();
         }
      }
   }

   @Override
   public String ejecutarReporteHojaVida(String nombreReporte, String rutaReporte, String rutaGenerado, String nombreArchivo, String tipoReporte, Connection cxn, Map param) {
      try {
         System.out.println("INICIARREPORTE NombreReporte: " + nombreReporte);
         System.out.println("INICIARREPORTE rutaReporte: " + rutaReporte);
         System.out.println("INICIARREPORTE rutaGenerado: " + rutaGenerado);
         System.out.println("INICIARREPORTE nombreArchivo: " + nombreArchivo);
         System.out.println("INICIARREPORTE tipoReporte: " + tipoReporte);
         System.out.println("INICIARREPORTE Parametros : " + param);
         File archivo = new File(rutaReporte + nombreReporte + ".jasper");
         JasperReport masterReport;
         masterReport = (JasperReport) JRLoader.loadObject(archivo);
         System.out.println("INICIARREPORTE creo master ");
         Map parametros = new HashMap();
         parametros.put("RutaReportes", rutaReporte);
         if (param != null && !param.isEmpty()) {
            parametros.put("secEmpleado", param.get("secEmpleado"));
         }
         JasperPrint imprimir = JasperFillManager.fillReport(masterReport, parametros, cxn);
         System.out.println("INICIARREPORTE lleno reporte ");
         String outFileName = rutaGenerado + nombreArchivo;
         System.out.println("INICIARREPORTE outFileName: " + outFileName);
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
      } catch (JRException e) {
         System.out.println("Error IniciarReporte.ejecutarReporte: " + e);
         System.out.println("************************************");
         if (e.getCause() != null) {
            return "INICIARREPORTE Error: " + e.toString() + "\n" + e.getCause().toString();
         } else {
            return "INICIARREPORTE Error: " + e.toString();
         }
      }
   }

   @Override
   public String ejecutarReportePlanta1(String nombreReporte, String rutaReporte, String rutaGenerado, String nombreArchivo, String tipoReporte, Connection cxn) {
      try {
         System.out.println("INICIARREPORTE NombreReporte: " + nombreReporte);
         System.out.println("INICIARREPORTE rutaReporte: " + rutaReporte);
         System.out.println("INICIARREPORTE rutaGenerado: " + rutaGenerado);
         System.out.println("INICIARREPORTE nombreArchivo: " + nombreArchivo);
         System.out.println("INICIARREPORTE tipoReporte: " + tipoReporte);
         File archivo = new File(rutaReporte + nombreReporte + ".jasper");
         JasperReport masterReport;
         masterReport = (JasperReport) JRLoader.loadObject(archivo);
         System.out.println("INICIARREPORTE creo master ");
         JasperPrint imprimir = JasperFillManager.fillReport(masterReport, null, cxn);
         System.out.println("INICIARREPORTE lleno reporte ");
         String outFileName = rutaGenerado + nombreArchivo;
         System.out.println("INICIARREPORTE outFileName: " + outFileName);
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
      } catch (JRException e) {
         System.out.println("Error IniciarReporte.ejecutarReportePlanta1: " + e);
         System.out.println("************************************");
         if (e.getCause() != null) {
            return "INICIARREPORTE Error: " + e.toString() + "\n" + e.getCause().toString();
         } else {
            return "INICIARREPORTE Error: " + e.toString();
         }
      }
   }

   @Override
   public String ejecutarReporteSegUsuarios(String nombreReporte, String rutaReporte, String rutaGenerado, String nombreArchivo, String tipoReporte, Connection cxn) {
      try {
         System.out.println("INICIARREPORTE NombreReporte: " + nombreReporte);
         System.out.println("INICIARREPORTE rutaReporte: " + rutaReporte);
         System.out.println("INICIARREPORTE rutaGenerado: " + rutaGenerado);
         System.out.println("INICIARREPORTE nombreArchivo: " + nombreArchivo);
         System.out.println("INICIARREPORTE tipoReporte: " + tipoReporte);
         File archivo = new File(rutaReporte + nombreReporte + ".jasper");
         JasperReport masterReport;
         masterReport = (JasperReport) JRLoader.loadObject(archivo);
         System.out.println("INICIARREPORTE creo master ");
         Map parametros = new HashMap();
         parametros.put("RutaReportes", rutaReporte);
         JasperPrint imprimir = JasperFillManager.fillReport(masterReport, parametros, cxn);
         System.out.println("INICIARREPORTE lleno reporte ");
         String outFileName = rutaGenerado + nombreArchivo;
         System.out.println("INICIARREPORTE outFileName: " + outFileName);
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
      } catch (JRException e) {
         System.out.println("Error IniciarReporte.ejecutarReporteSegUsuarios: " + e);
         System.out.println("************************************");
         if (e.getCause() != null) {
            return "INICIARREPORTE Error: " + e.toString() + "\n" + e.getCause().toString();
         } else {
            return "INICIARREPORTE Error: " + e.toString();
         }
      }
   }

   @Override
   public String ejecutarReporteHistoricosUsuarios(String nombreReporte, String rutaReporte, String rutaGenerado, String nombreArchivo, String tipoReporte, Connection cxn, Map param) {
      try {
         System.out.println("INICIARREPORTE NombreReporte: " + nombreReporte);
         System.out.println("INICIARREPORTE rutaReporte: " + rutaReporte);
         System.out.println("INICIARREPORTE rutaGenerado: " + rutaGenerado);
         System.out.println("INICIARREPORTE nombreArchivo: " + nombreArchivo);
         System.out.println("INICIARREPORTE tipoReporte: " + tipoReporte);
         System.out.println("INICIARREPORTE Parametros : " + param);
         File archivo = new File(rutaReporte + nombreReporte + ".jasper");
         JasperReport masterReport;
         masterReport = (JasperReport) JRLoader.loadObject(archivo);
         System.out.println("INICIARREPORTE creo master ");
         Map parametros = new HashMap();
         parametros.put("RutaReportes", rutaReporte);
         if (param != null && !param.isEmpty()) {
            parametros.put("secuenciaUsuario", param.get("secuenciaUsuario"));
         }
         JasperPrint imprimir = JasperFillManager.fillReport(masterReport, parametros, cxn);
         System.out.println("INICIARREPORTE lleno reporte ");
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
         System.out.println("fin. " + outFileName);
         return outFileName;
      } catch (JRException e) {
         System.out.println("Error IniciarReporte.ejecutarReporte: " + e);
         System.out.println("************************************");
         if (e.getCause() != null) {
            return "INICIARREPORTE Error: " + e.toString() + "\n" + e.getCause().toString();
         } else {
            return "INICIARREPORTE Error: " + e.toString();
         }
      }
   }

   @Override
   public String ejecutarReportePantallas(String nombreReporte, String rutaReporte, String rutaGenerado, String nombreArchivo, String tipoReporte, Connection cxn) {
      try {
         System.out.println("INICIARREPORTE NombreReporte: " + nombreReporte);
         System.out.println("INICIARREPORTE rutaReporte: " + rutaReporte);
         System.out.println("INICIARREPORTE rutaGenerado: " + rutaGenerado);
         System.out.println("INICIARREPORTE nombreArchivo: " + nombreArchivo);
         System.out.println("INICIARREPORTE tipoReporte: " + tipoReporte);
         File archivo = new File(rutaReporte + nombreReporte + ".jasper");
         JasperReport masterReport;
         masterReport = (JasperReport) JRLoader.loadObject(archivo);
         System.out.println("INICIARREPORTE creo master ");
         Map parametros = new HashMap();
         parametros.put("RutaReportes", rutaReporte);
         JasperPrint imprimir = JasperFillManager.fillReport(masterReport, parametros, cxn);
         System.out.println("INICIARREPORTE lleno reporte ");
         String outFileName = rutaGenerado + nombreArchivo;
         System.out.println("INICIARREPORTE outFileName: " + outFileName);
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
      } catch (JRException e) {
         System.out.println("Error IniciarReporte.ejecutarReportePantallas: " + e);
         System.out.println("************************************");
         if (e.getCause() != null) {
            return "INICIARREPORTE Error: " + e.toString() + "\n" + e.getCause().toString();
         } else {
            return "INICIARREPORTE Error: " + e.toString();
         }
      }
   }

   @Override
   public String ejecutarReporteObjetos(String nombreReporte, String rutaReporte, String rutaGenerado, String nombreArchivo, String tipoReporte, Connection cxn) {
      try {
         System.out.println("INICIARREPORTE NombreReporte: " + nombreReporte);
         System.out.println("INICIARREPORTE rutaReporte: " + rutaReporte);
         System.out.println("INICIARREPORTE rutaGenerado: " + rutaGenerado);
         System.out.println("INICIARREPORTE nombreArchivo: " + nombreArchivo);
         System.out.println("INICIARREPORTE tipoReporte: " + tipoReporte);
         File archivo = new File(rutaReporte + nombreReporte + ".jasper");
         JasperReport masterReport;
         masterReport = (JasperReport) JRLoader.loadObject(archivo);
         System.out.println("INICIARREPORTE creo master ");
         Map parametros = new HashMap();
         parametros.put("RutaReportes", rutaReporte);
         JasperPrint imprimir = JasperFillManager.fillReport(masterReport, parametros, cxn);
         System.out.println("INICIARREPORTE lleno reporte ");
         String outFileName = rutaGenerado + nombreArchivo;
         System.out.println("INICIARREPORTE outFileName: " + outFileName);
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
      } catch (JRException e) {
         System.out.println("Error IniciarReporte.ejecutarReporteObjetos: " + e);
         System.out.println("************************************");
         if (e.getCause() != null) {
            return "INICIARREPORTE Error: " + e.toString() + "\n" + e.getCause().toString();
         } else {
            return "INICIARREPORTE Error: " + e.toString();
         }
      }
   }

}
