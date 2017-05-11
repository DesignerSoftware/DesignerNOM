/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfaceAdministrar;

import java.math.BigDecimal;
import java.util.Map;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.fill.AsynchronousFilllListener;

/**
 * Interface encargada de determinar las operaciones lógicas necesarias para
 * realizar reportes.
 *
 * @author betelgeuse
 */
public interface AdministarReportesInterface {

   /**
    * Método encargado de obtener el Entity Manager el cual tiene asociado la
    * sesion del usuario que utiliza el aplicativo.
    *
    * @param idSesion Identificador se la sesion.
    */
   public void obtenerConexion(String idSesion);

   /**
    * Método encargado de recuperar los datos de conexión del EntityManager el
    * cual tiene el usuario asociado a un perfil del aplicativo.
    */
   public void consultarDatosConexion();

   /**
    * Método encargado de generar el reporte que el usuario ha seleccionado.
    *
    * @param nombreReporte Nombre del reporte.
    * @param tipoReporte Tipo de reporte.
    * @param asistenteReporte
    * @return Retorna la ubicacion del reporte generado.
    */
   public String generarReporte(String nombreReporte, String tipoReporte, AsynchronousFilllListener asistenteReporte);

   public String generarReporte(String nombreReporte, String tipoReporte);

   public String generarReporte(String nombreReporte, String tipoReporte, Map paramEmpl);

   public void iniciarLlenadoReporte(String nombreReporte, AsynchronousFilllListener asistenteReporte);

   public String crearArchivoReporte(JasperPrint print, String tipoReporte);

   public void cancelarReporte();

   public String generarReporteCifraControl(String nombreReporte, String tipoReporte, Map paramFechas);

   public String generarReporteGlobal(String nombreReporte, String tipoReporte, Map paramGlobal);
   
   public String generarReporteResumido(String nombreReporte, String tipoReporte, Map paramResumido);

   public String generarReporteFuncionesCargo(String nombreReporte, String tipoReporte, Map parametros);

   public String generarReporteHojaVida(String nombreReporte, String tipoReporte, Map parametros);

   public String generarReportePlanta1(String nombreReporte, String tipoReporte);

   public String generarReporteSegUsuarios(String nombreReporte, String tipoReporte);

   public String generarReporteHistoricosUsuarios(String nombreReporte, String tipoReporte, Map parametros);

   public String generarReportePantallas(String nombreReporte, String tipoReporte);

   public String generarReporteObjetos(String nombreReporte, String tipoReporte);
}
