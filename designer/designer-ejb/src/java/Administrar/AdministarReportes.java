/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Administrar;

import ClasesAyuda.EnvioCorreo;
import Entidades.ConfiguracionCorreo;
import Entidades.Generales;
import InterfaceAdministrar.AdministarReportesInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaActualUsuarioInterface;
import InterfacePersistencia.PersistenciaConfiguracionCorreoInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaGeneralesInterface;
import Reportes.IniciarReporteInterface;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.fill.AsynchronousFilllListener;

/**
 * Clase Stateful. <br> Clase encargada de realizar las operaciones lógicas para
 * los reportes.
 *
 * @author betelgeuse
 */
@Stateful
public class AdministarReportes implements AdministarReportesInterface {

    //--------------------------------------------------------------------------
    //ATRIBUTOS
    //--------------------------------------------------------------------------    
    /**
     * Enterprise JavaBean.<br> Atributo que representa todo lo referente a la
     * conexión del usuario que está usando el aplicativo.
     */
    @EJB
    AdministrarSesionesInterface administrarSesiones;
    /**
     * Enterprise JavaBeans.<br> Atributo que representa la comunicación con la
     * persistencia 'persistenciaEmpleado'.
     */
    @EJB
    PersistenciaEmpleadoInterface persistenciaEmpleado;
    /**
     * Enterprise JavaBeans.<br> Atributo que representa la comunicación con la
     * persistencia 'persistenciaGenerales'.
     */
    @EJB
    PersistenciaGeneralesInterface persistenciaGenerales;
    /**
     * Enterprise JavaBeans.<br> Atributo que representa la comunicación con la
     * persistencia 'persistenciaActualUsuario'.
     */
    @EJB
    PersistenciaActualUsuarioInterface persistenciaActualUsuario;
    /**
     * Atributo encargado de comunicarse con la interface
     * 'IniciarReporteInterface' para realizar un reporte.
     */
    @EJB
    IniciarReporteInterface reporte;
    /**
     * Enterprise JavaBeans.<br> Atributo que representa la comunicación con la
     * persistencia 'persistenciaConfiguracionCorreo'.
     */
    @EJB
    PersistenciaConfiguracionCorreoInterface persistenciaConfiguracionCorreo;
    /**
     * Atributo que representa la conexión actual al aplicativo.
     */
    private Connection conexion;
    private EntityManager em;
    private Generales general;

    //--------------------------------------------------------------------------
    //MÉTODOS
    //--------------------------------------------------------------------------
    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    @Override
    public void consultarDatosConexion() {
        em.getTransaction().begin();
        conexion = em.unwrap(java.sql.Connection.class);
        em.getTransaction().commit();
        //em.close();
        System.out.println("AdministrarReportes consultarDatosConexion");
    }

    public String generarReporte(String nombreReporte, String tipoReporte, AsynchronousFilllListener asistenteReporte) {
        //try {
        general = persistenciaGenerales.obtenerRutas(em);
        String nombreUsuario = persistenciaActualUsuario.actualAliasBD(em);
        String pathReporteGenerado = null;
        if (general != null && nombreUsuario != null) {
            SimpleDateFormat formato = new SimpleDateFormat("ddMMyyyyhhmmss");
            String fechaActual = formato.format(new Date());
            String nombreArchivo = "JR" + nombreUsuario + fechaActual;
            String rutaReporte = general.getPathreportes();
            String rutaGenerado = general.getUbicareportes();
            if (tipoReporte.equals("PDF")) {
                nombreArchivo = nombreArchivo + ".pdf";
            } else if (tipoReporte.equals("XLSX")) {
                nombreArchivo = nombreArchivo + ".xlsx";
            } else if (tipoReporte.equals("XLS")) {
                nombreArchivo = nombreArchivo + ".xls";
            } else if (tipoReporte.equals("CSV")) {
                nombreArchivo = nombreArchivo + ".csv";
            } else if (tipoReporte.equals("HTML")) {
                nombreArchivo = nombreArchivo + ".html";
            } else if (tipoReporte.equals("DOCX")) {
                nombreArchivo = nombreArchivo + ".rtf";
            }
            //datosConexion();
            // if (conexion != null && !conexion.isClosed()) {
            //pathReporteGenerado = reporte.ejecutarReporte(nombreReporte, rutaReporte, rutaGenerado, nombreArchivo, tipoReporte, null);
            // pathReporteGenerado = reporte.ejecutarReportinho(nombreReporte, rutaReporte, rutaGenerado, nombreArchivo, tipoReporte, null);
            reporte.llenarReporte(nombreReporte, rutaReporte, asistenteReporte);
            //conexion.close();
            // return pathReporteGenerado;
            // }
            return pathReporteGenerado;
        }
        return pathReporteGenerado;
        /*
         * } catch (SQLException ex) { System.out.println("PUM PUM xD"); return
         * null;
         }
         */
    }

    @Override
    public String generarReporte(String nombreReporte, String tipoReporte) {
        System.out.println(this.getClass().getName() + ".generarReporte()");
        try {
            general = persistenciaGenerales.obtenerRutas(em);
            String nombreUsuario = persistenciaActualUsuario.actualAliasBD(em);
            String pathReporteGenerado = null;
            System.out.println("general:  " + general);
            System.out.println("nombreusuario: " + nombreUsuario);
            if (general != null && nombreUsuario != null) {
                SimpleDateFormat formato = new SimpleDateFormat("ddMMyyyyhhmmss");
                String fechaActual = formato.format(new Date());
                String nombreArchivo = "JR" + nombreReporte + nombreUsuario + fechaActual;
                String rutaReporte = general.getPathreportes();
                String rutaGenerado = general.getUbicareportes();
                System.out.println("general.getPathreportes() : " + general.getPathreportes());
                System.out.println("general.getUbicareportes() : " + general.getUbicareportes());
                if (tipoReporte.equals("PDF")) {
                    System.out.println("entró a PDF");
                    nombreArchivo = nombreArchivo + ".pdf";
                } else if (tipoReporte.equals("XLSX")) {
                    nombreArchivo = nombreArchivo + ".xlsx";
                } else if (tipoReporte.equals("XLS")) {
                    nombreArchivo = nombreArchivo + ".xls";
                } else if (tipoReporte.equals("CSV")) {
                    nombreArchivo = nombreArchivo + ".csv";
                } else if (tipoReporte.equals("HTML")) {
                    nombreArchivo = nombreArchivo + ".html";
                } else if (tipoReporte.equals("DOCX")) {
                    nombreArchivo = nombreArchivo + ".rtf";
                }
                consultarDatosConexion();
                System.out.println("conexion: " + conexion);
                if (conexion != null && !conexion.isClosed()) {
                    pathReporteGenerado = reporte.ejecutarReporte(nombreReporte, rutaReporte, rutaGenerado, nombreArchivo, tipoReporte, conexion);
                    //conexion.close();
                    return pathReporteGenerado;
                }
                return pathReporteGenerado;
            }
            System.out.println("pathReporteGenerado: " + pathReporteGenerado);
            System.out.println("Sali sin Errores");
            return pathReporteGenerado;
        } catch (SQLException ex) {
            System.out.println("Error AdministrarReporte.generarReporte: " + ex);
            return null;
        }
    }

    @Override
    public void iniciarLlenadoReporte(String nombreReporte, AsynchronousFilllListener asistenteReporte) {
        if (general == null) {
            general = persistenciaGenerales.obtenerRutas(em);
        }
        String rutaReporte = general.getPathreportes();
        reporte.llenarReporte(nombreReporte, rutaReporte, asistenteReporte);
    }

    public String crearArchivoReporte(JasperPrint print, String tipoReporte) {
        String nombreUsuario = persistenciaActualUsuario.actualAliasBD(em);
        String pathReporteGenerado = null;
        if (general != null && nombreUsuario != null) {
            SimpleDateFormat formato = new SimpleDateFormat("ddMMyyyyhhmmss");
            String fechaActual = formato.format(new Date());
            String nombreArchivo = "JR" + nombreUsuario + fechaActual;
            String rutaGenerado = general.getUbicareportes();
            pathReporteGenerado = reporte.crearArchivoReporte(rutaGenerado, nombreArchivo, tipoReporte, print);
        }
        return pathReporteGenerado;
    }

    public void cancelarReporte() {
        reporte.cancelarReporte();
    }
    }
