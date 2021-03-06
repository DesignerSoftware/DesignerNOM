/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Administrar;

import Entidades.Generales;
import InterfaceAdministrar.AdministarReportesInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaActualUsuarioInterface;
import InterfacePersistencia.PersistenciaConfiguracionCorreoInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaGeneralesInterface;
import Reportes.IniciarReporteInterface;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;
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

    private static Logger log = Logger.getLogger(AdministarReportes.class);

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
    private EntityManagerFactory emf;
    private EntityManager em;
    private String idSesionBck;

    private EntityManager getEm() {
        try {
            if (this.emf != null) {
                if (this.em != null) {
                    if (this.em.isOpen()) {
                        this.em.close();
                    }
                }
            } else {
                this.emf = administrarSesiones.obtenerConexionSesionEMF(idSesionBck);
            }
            this.em = emf.createEntityManager();
        } catch (Exception e) {
            log.fatal(this.getClass().getSimpleName() + " getEm() ERROR : " + e);
        }
        return this.em;
    }
    private Generales general;

    //--------------------------------------------------------------------------
    //MÉTODOS
    //--------------------------------------------------------------------------
    @Override
    public void obtenerConexion(String idSesion) {
        idSesionBck = idSesion;
        try {
            emf = administrarSesiones.obtenerConexionSesionEMF(idSesion);
        } catch (Exception e) {
            log.fatal(this.getClass().getSimpleName() + " obtenerConexion ERROR: " + e);
        }
    }

    @Override
    public void consultarDatosConexion() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        conexion = em.unwrap(java.sql.Connection.class);
        em.getTransaction().commit();
        try {
        } catch (Exception e) {
            log.fatal(this.getClass().getSimpleName() + " consultarDatosConexion ERROR: " + e);
        }
    }

    @Override
    public String generarReporte(String nombreReporte, String tipoReporte, AsynchronousFilllListener asistenteReporte) throws Exception {
        general = persistenciaGenerales.obtenerRutas(getEm());
        String nombreUsuario = persistenciaActualUsuario.actualAliasBD(getEm());
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
            try {
                reporte.llenarReporte(nombreReporte, rutaReporte, asistenteReporte);
            } catch (Exception e) {
                log.warn("error en generar reporte, entra al catch : " + e.getMessage());
                throw e;
            }
            return pathReporteGenerado;
        }
        return pathReporteGenerado;
    }

    @Override
    public String generarReporte(String nombreReporte, String tipoReporte) {
        try {
            return generarReporte(nombreReporte, tipoReporte, new HashMap());
        } catch (Exception e) {
            log.fatal(this.getClass().getSimpleName() + " generarReporte ERROR: " + e);
            return null;
        }
    }

    @Override
    public String generarReporte(String nombreReporte, String tipoReporte, Map paramEmpl) {
        try {
            general = persistenciaGenerales.obtenerRutas(getEm());
            String nombreUsuario = persistenciaActualUsuario.actualAliasBD(getEm());
            String pathReporteGenerado = null;
            log.warn("general:  " + general);
            log.warn("nombreusuario: " + nombreUsuario);
            log.warn("Tipo Reporte: " + tipoReporte);
            if (general != null && nombreUsuario != null) {
                SimpleDateFormat formato = new SimpleDateFormat("ddMMyyyyhhmmss");
                String fechaActual = formato.format(new Date());
                String nombreArchivo = "JR" + nombreReporte + nombreUsuario + fechaActual;
                String rutaReporte = general.getPathreportes();
                String rutaGenerado = general.getUbicareportes();
                log.warn("general.getPathreportes() : " + general.getPathreportes());
                log.warn("general.getUbicareportes() : " + general.getUbicareportes());
                String extension = "";
                System.out.println("tipoReporte: " + tipoReporte);
                switch (tipoReporte) {
                    case "PDF":
                        extension = ".pdf";
                        break;
                    case "XLSX":
                        extension = ".xlsx";
                        break;
                    case "XLS":
                        extension = ".xls";
                        break;
                    case "CSV":
                        extension = ".csv";
                        break;
                    case "HTML":
                        extension = ".html";
                        break;
                    case "DOCX":
                        extension = ".docx";
                        break;
                    case "DELIMITED":
                        extension = ".txt";
                        break;
                    case "TXT":
                        extension = ".txt";
                        break;
                    default:
                        extension = "";
                        break;
                }
                nombreArchivo = nombreArchivo + extension;
                consultarDatosConexion();
                log.warn("conexion: " + conexion);
                if (conexion != null && !conexion.isClosed()) {
                    pathReporteGenerado = reporte.ejecutarReporte(nombreReporte, rutaReporte, rutaGenerado, nombreArchivo, tipoReporte, conexion, paramEmpl);
                    //conexion.close();
                    return pathReporteGenerado;
                }
                return pathReporteGenerado;
            }
            log.warn("pathReporteGenerado: " + pathReporteGenerado);
            log.warn("Sali sin Errores");
            return pathReporteGenerado;
        } catch (SQLException ex) {
            log.warn("Error AdministrarReporte.generarReporte: " + ex);
            return null;
        }
    }

    @Override
    public void iniciarLlenadoReporte(String nombreReporte, AsynchronousFilllListener asistenteReporte) throws Exception {
        try {
            if (general == null) {
                general = persistenciaGenerales.obtenerRutas(getEm());
            }
            String rutaReporte = general.getPathreportes();
            reporte.llenarReporte(nombreReporte, rutaReporte, asistenteReporte);
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public String crearArchivoReporte(JasperPrint print, String tipoReporte) {
        try {
            String nombreUsuario = persistenciaActualUsuario.actualAliasBD(getEm());
            String pathReporteGenerado = null;
            if (general != null && nombreUsuario != null) {
                SimpleDateFormat formato = new SimpleDateFormat("ddMMyyyyhhmmss");
                String fechaActual = formato.format(new Date());
                String nombreArchivo = "JR" + nombreUsuario + fechaActual;
                String rutaGenerado = general.getUbicareportes();
                pathReporteGenerado = reporte.crearArchivoReporte(rutaGenerado, nombreArchivo, tipoReporte, print);
            }
            return pathReporteGenerado;
        } catch (Exception e) {
            log.fatal(this.getClass().getSimpleName() + " crearArchivoReporte ERROR: " + e);
            return null;
        }
    }

    @Override
    public void cancelarReporte() {
        try {
            reporte.cancelarReporte();
        } catch (Exception e) {
            log.fatal(this.getClass().getSimpleName() + " cancelarReporte ERROR: " + e);
        }
    }

    @Override
    public String generarReporteCifraControl(String nombreReporte, String tipoReporte, Map paramFechas) {
        try {
            general = persistenciaGenerales.obtenerRutas(getEm());
            String nombreUsuario = persistenciaActualUsuario.actualAliasBD(getEm());
            String pathReporteGenerado = null;
            log.warn("general:  " + general);
            log.warn("nombreusuario: " + nombreUsuario);
            if (general != null && nombreUsuario != null) {
                SimpleDateFormat formato = new SimpleDateFormat("ddMMyyyyhhmmss");
                String fechaActual = formato.format(new Date());
                String nombreArchivo = "JR" + nombreReporte + nombreUsuario + fechaActual;
                String rutaReporte = general.getPathreportes();
                String rutaGenerado = general.getUbicareportes();
                log.warn("general.getPathreportes() : " + general.getPathreportes());
                log.warn("general.getUbicareportes() : " + general.getUbicareportes());
                if (tipoReporte.equals("PDF")) {
                    nombreArchivo = nombreArchivo + ".pdf";
                }
                consultarDatosConexion();
                log.warn("conexion: " + conexion);
                if (conexion != null && !conexion.isClosed()) {
                    pathReporteGenerado = reporte.ejecutarReporteCifraControl(nombreReporte, rutaReporte, rutaGenerado, nombreArchivo, tipoReporte, conexion, paramFechas);
                    //conexion.close();
                    return pathReporteGenerado;
                }
                return pathReporteGenerado;
            }
            log.warn("pathReporteGenerado: " + pathReporteGenerado);
            log.warn("Sali sin Errores");
            return pathReporteGenerado;
        } catch (SQLException ex) {
            log.warn("Error AdministrarReporte.generarReporte: " + ex);
            return null;
        }
    }

    @Override
    public String generarReporteGlobal(String nombreReporte, String tipoReporte, Map paramGlobal) {
        try {
            general = persistenciaGenerales.obtenerRutas(getEm());
            String nombreUsuario = persistenciaActualUsuario.actualAliasBD(getEm());
            String pathReporteGenerado = null;
            log.warn("general:  " + general);
            log.warn("nombreusuario: " + nombreUsuario);
            if (general != null && nombreUsuario != null) {
                SimpleDateFormat formato = new SimpleDateFormat("ddMMyyyyhhmmss");
                String fechaActual = formato.format(new Date());
                String nombreArchivo = "JR" + nombreReporte + nombreUsuario + fechaActual;
                String rutaReporte = general.getPathreportes();
                String rutaGenerado = general.getUbicareportes();
                log.warn("general.getPathreportes() : " + general.getPathreportes());
                log.warn("general.getUbicareportes() : " + general.getUbicareportes());
                if (tipoReporte.equals("PDF")) {
                    nombreArchivo = nombreArchivo + ".pdf";
                }
                consultarDatosConexion();
                log.warn("conexion: " + conexion);
                if (conexion != null && !conexion.isClosed()) {
                    pathReporteGenerado = reporte.ejecutarReporteEstadisGlobal(nombreReporte, rutaReporte, rutaGenerado, nombreArchivo, tipoReporte, conexion, paramGlobal);
                    //conexion.close();
                    return pathReporteGenerado;
                }
                return pathReporteGenerado;
            }
            log.warn("pathReporteGenerado: " + pathReporteGenerado);
            log.warn("Sali sin Errores");
            return pathReporteGenerado;
        } catch (SQLException ex) {
            log.warn("Error AdministrarReporte.generarReporteGlobal: " + ex);
            return null;
        }
    }

    @Override
    public String generarReporteResumido(String nombreReporte, String tipoReporte, Map paramResumido) {
        try {
            general = persistenciaGenerales.obtenerRutas(getEm());
            String nombreUsuario = persistenciaActualUsuario.actualAliasBD(getEm());
            String pathReporteGenerado = null;
            log.warn("general:  " + general);
            log.warn("nombreusuario: " + nombreUsuario);
            if (general != null && nombreUsuario != null) {
                SimpleDateFormat formato = new SimpleDateFormat("ddMMyyyyhhmmss");
                String fechaActual = formato.format(new Date());
                String nombreArchivo = "JR" + nombreReporte + nombreUsuario + fechaActual;
                String rutaReporte = general.getPathreportes();
                String rutaGenerado = general.getUbicareportes();
                log.warn("general.getPathreportes() : " + general.getPathreportes());
                log.warn("general.getUbicareportes() : " + general.getUbicareportes());
                if (tipoReporte.equals("PDF")) {
                    nombreArchivo = nombreArchivo + ".pdf";
                }
                consultarDatosConexion();
                log.warn("conexion: " + conexion);
                if (conexion != null && !conexion.isClosed()) {
                    pathReporteGenerado = reporte.ejecutarReporteEstadisResumido(nombreReporte, rutaReporte, rutaGenerado, nombreArchivo, tipoReporte, conexion, paramResumido);
                    //conexion.close();
                    return pathReporteGenerado;
                }
                return pathReporteGenerado;
            }
            log.warn("pathReporteGenerado: " + pathReporteGenerado);
            log.warn("Sali sin Errores");
            return pathReporteGenerado;
        } catch (SQLException ex) {
            log.warn("Error AdministrarReporte.generarReporteResumido: " + ex);
            return null;
        }
    }

    @Override
    public String generarReporteFuncionesCargo(String nombreReporte, String tipoReporte, Map parametros) {
        try {
            general = persistenciaGenerales.obtenerRutas(getEm());
            String nombreUsuario = persistenciaActualUsuario.actualAliasBD(getEm());
            String pathReporteGenerado = null;
            log.warn("general:  " + general);
            log.warn("nombreusuario: " + nombreUsuario);
            if (general != null && nombreUsuario != null) {
                SimpleDateFormat formato = new SimpleDateFormat("ddMMyyyyhhmmss");
                String fechaActual = formato.format(new Date());
                String nombreArchivo = "JR" + nombreReporte + nombreUsuario + fechaActual;
                String rutaReporte = general.getPathreportes();
                String rutaGenerado = general.getUbicareportes();
                log.warn("general.getPathreportes() : " + general.getPathreportes());
                log.warn("general.getUbicareportes() : " + general.getUbicareportes());
                if (tipoReporte.equals("PDF")) {
                    nombreArchivo = nombreArchivo + ".pdf";
                }
                consultarDatosConexion();
                log.warn("conexion: " + conexion);
                if (conexion != null && !conexion.isClosed()) {
                    pathReporteGenerado = reporte.ejecutarReporteCifraControl(nombreReporte, rutaReporte, rutaGenerado, nombreArchivo, tipoReporte, conexion, parametros);
                    //conexion.close();
                    return pathReporteGenerado;
                }
                return pathReporteGenerado;
            }
            log.warn("pathReporteGenerado: " + pathReporteGenerado);
            log.warn("Sali sin Errores");
            return pathReporteGenerado;
        } catch (SQLException ex) {
            log.warn("Error AdministrarReporte.generarReporteFuncionesCargo: " + ex);
            return null;
        }
    }

    @Override
    public String generarReporteHojaVida(String nombreReporte, String tipoReporte, Map parametros) {
        try {
            general = persistenciaGenerales.obtenerRutas(getEm());
            String nombreUsuario = persistenciaActualUsuario.actualAliasBD(getEm());
            String pathReporteGenerado = null;
            log.warn("general:  " + general);
            log.warn("nombreusuario: " + nombreUsuario);
            if (general != null && nombreUsuario != null) {
                SimpleDateFormat formato = new SimpleDateFormat("ddMMyyyyhhmmss");
                String fechaActual = formato.format(new Date());
                String nombreArchivo = "JR" + nombreReporte + nombreUsuario + fechaActual;
                String rutaReporte = general.getPathreportes();
                String rutaGenerado = general.getUbicareportes();
                log.warn("general.getPathreportes() : " + general.getPathreportes());
                log.warn("general.getUbicareportes() : " + general.getUbicareportes());
                if (tipoReporte.equals("PDF")) {
                    nombreArchivo = nombreArchivo + ".pdf";
                }
                consultarDatosConexion();
                log.warn("conexion: " + conexion);
                if (conexion != null && !conexion.isClosed()) {
                    pathReporteGenerado = reporte.ejecutarReporteHojaVida(nombreReporte, rutaReporte, rutaGenerado, nombreArchivo, tipoReporte, conexion, parametros);
                    //conexion.close();
                    return pathReporteGenerado;
                }
                return pathReporteGenerado;
            }
            log.warn("pathReporteGenerado: " + pathReporteGenerado);
            log.warn("Sali sin Errores");
            return pathReporteGenerado;
        } catch (SQLException ex) {
            log.warn("Error AdministrarReporte.generarReporteFuncionesCargo: " + ex);
            return null;
        }
    }

    @Override
    public String generarReportePlanta1(String nombreReporte, String tipoReporte) {
        try {
            general = persistenciaGenerales.obtenerRutas(getEm());
            String nombreUsuario = persistenciaActualUsuario.actualAliasBD(getEm());
            String pathReporteGenerado = null;
            log.warn("general:  " + general);
            log.warn("nombreusuario: " + nombreUsuario);
            if (general != null && nombreUsuario != null) {
                SimpleDateFormat formato = new SimpleDateFormat("ddMMyyyyhhmmss");
                String fechaActual = formato.format(new Date());
                String nombreArchivo = "JR" + nombreReporte + nombreUsuario + fechaActual;
                String rutaReporte = general.getPathreportes();
                String rutaGenerado = general.getUbicareportes();
                log.warn("general.getPathreportes() : " + general.getPathreportes());
                log.warn("general.getUbicareportes() : " + general.getUbicareportes());
                if (tipoReporte.equals("PDF")) {
                    nombreArchivo = nombreArchivo + ".pdf";
                }
                consultarDatosConexion();
                log.warn("conexion: " + conexion);
                if (conexion != null && !conexion.isClosed()) {
                    pathReporteGenerado = reporte.ejecutarReportePlanta1(nombreReporte, rutaReporte, rutaGenerado, nombreArchivo, tipoReporte, conexion);
                    //conexion.close();
                    return pathReporteGenerado;
                }
                return pathReporteGenerado;
            }
            log.warn("pathReporteGenerado: " + pathReporteGenerado);
            log.warn("Sali sin Errores");
            return pathReporteGenerado;
        } catch (SQLException ex) {
            log.warn("Error AdministrarReporte.generarReporteFuncionesCargo: " + ex);
            return null;
        }
    }

    @Override
    public String generarReporteSegUsuarios(String nombreReporte, String tipoReporte) {
        try {
            general = persistenciaGenerales.obtenerRutas(getEm());
            String nombreUsuario = persistenciaActualUsuario.actualAliasBD(getEm());
            String pathReporteGenerado = null;
            log.warn("general:  " + general);
            log.warn("nombreusuario: " + nombreUsuario);
            if (general != null && nombreUsuario != null) {
                SimpleDateFormat formato = new SimpleDateFormat("ddMMyyyyhhmmss");
                String fechaActual = formato.format(new Date());
                String nombreArchivo = "JR" + nombreReporte + nombreUsuario + fechaActual;
                String rutaReporte = general.getPathreportes();
                String rutaGenerado = general.getUbicareportes();
                log.warn("general.getPathreportes() : " + general.getPathreportes());
                log.warn("general.getUbicareportes() : " + general.getUbicareportes());
                if (tipoReporte.equals("PDF")) {
                    nombreArchivo = nombreArchivo + ".pdf";
                }
                consultarDatosConexion();
                log.warn("conexion: " + conexion);
                if (conexion != null && !conexion.isClosed()) {
                    pathReporteGenerado = reporte.ejecutarReporteSegUsuarios(nombreReporte, rutaReporte, rutaGenerado, nombreArchivo, tipoReporte, conexion);
                    //conexion.close();
                    return pathReporteGenerado;
                }
                return pathReporteGenerado;
            }
            log.warn("pathReporteGenerado: " + pathReporteGenerado);
            log.warn("Sali sin Errores");
            return pathReporteGenerado;
        } catch (SQLException ex) {
            log.warn("Error AdministrarReporte.generarReporteSegUsuarios: " + ex);
            return null;
        }
    }

    @Override
    public String generarReporteHistoricosUsuarios(String nombreReporte, String tipoReporte, Map parametros) {
        try {
            general = persistenciaGenerales.obtenerRutas(getEm());
            String nombreUsuario = persistenciaActualUsuario.actualAliasBD(getEm());
            String pathReporteGenerado = null;
            log.warn("general:  " + general);
            log.warn("nombreusuario: " + nombreUsuario);
            if (general != null && nombreUsuario != null) {
                SimpleDateFormat formato = new SimpleDateFormat("ddMMyyyyhhmmss");
                String fechaActual = formato.format(new Date());
                String nombreArchivo = "JR" + nombreReporte + nombreUsuario + fechaActual;
                String rutaReporte = general.getPathreportes();
                String rutaGenerado = general.getUbicareportes();
                log.warn("general.getPathreportes() : " + general.getPathreportes());
                log.warn("general.getUbicareportes() : " + general.getUbicareportes());
                if (tipoReporte.equals("PDF")) {
                    nombreArchivo = nombreArchivo + ".pdf";
                }
                consultarDatosConexion();
                log.warn("conexion: " + conexion);
                if (conexion != null && !conexion.isClosed()) {
                    pathReporteGenerado = reporte.ejecutarReporteHistoricosUsuarios(nombreReporte, rutaReporte, rutaGenerado, nombreArchivo, tipoReporte, conexion, parametros);
                    //conexion.close();
                    return pathReporteGenerado;
                }
                return pathReporteGenerado;
            }
            log.warn("pathReporteGenerado: " + pathReporteGenerado);
            log.warn("Sali sin Errores");
            return pathReporteGenerado;
        } catch (SQLException ex) {
            log.warn("Error AdministrarReporte.generarReporteFuncionesCargo: " + ex);
            return null;
        }
    }

    @Override
    public String generarReportePantallas(String nombreReporte, String tipoReporte) {
        try {
            general = persistenciaGenerales.obtenerRutas(getEm());
            String nombreUsuario = persistenciaActualUsuario.actualAliasBD(getEm());
            String pathReporteGenerado = null;
            log.warn("general:  " + general);
            log.warn("nombreusuario: " + nombreUsuario);
            if (general != null && nombreUsuario != null) {
                SimpleDateFormat formato = new SimpleDateFormat("ddMMyyyyhhmmss");
                String fechaActual = formato.format(new Date());
                String nombreArchivo = "JR" + nombreReporte + nombreUsuario + fechaActual;
                String rutaReporte = general.getPathreportes();
                String rutaGenerado = general.getUbicareportes();
                log.warn("general.getPathreportes() : " + general.getPathreportes());
                log.warn("general.getUbicareportes() : " + general.getUbicareportes());
                if (tipoReporte.equals("PDF")) {
                    nombreArchivo = nombreArchivo + ".pdf";
                }
                consultarDatosConexion();
                log.warn("conexion: " + conexion);
                if (conexion != null && !conexion.isClosed()) {
                    pathReporteGenerado = reporte.ejecutarReportePantallas(nombreReporte, rutaReporte, rutaGenerado, nombreArchivo, tipoReporte, conexion);
                    //conexion.close();
                    return pathReporteGenerado;
                }
                return pathReporteGenerado;
            }
            log.warn("pathReporteGenerado: " + pathReporteGenerado);
            log.warn("Sali sin Errores");
            return pathReporteGenerado;
        } catch (SQLException ex) {
            log.warn("Error AdministrarReporte.generarReportePantallas: " + ex);
            return null;
        }
    }

    @Override
    public String generarReporteObjetos(String nombreReporte, String tipoReporte) {
        try {
            general = persistenciaGenerales.obtenerRutas(getEm());
            String nombreUsuario = persistenciaActualUsuario.actualAliasBD(getEm());
            String pathReporteGenerado = null;
            log.warn("general:  " + general);
            log.warn("nombreusuario: " + nombreUsuario);
            if (general != null && nombreUsuario != null) {
                SimpleDateFormat formato = new SimpleDateFormat("ddMMyyyyhhmmss");
                String fechaActual = formato.format(new Date());
                String nombreArchivo = "JR" + nombreReporte + nombreUsuario + fechaActual;
                String rutaReporte = general.getPathreportes();
                String rutaGenerado = general.getUbicareportes();
                log.warn("general.getPathreportes() : " + general.getPathreportes());
                log.warn("general.getUbicareportes() : " + general.getUbicareportes());
                if (tipoReporte.equals("PDF")) {
                    nombreArchivo = nombreArchivo + ".pdf";
                }
                consultarDatosConexion();
                log.warn("conexion: " + conexion);
                if (conexion != null && !conexion.isClosed()) {
                    pathReporteGenerado = reporte.ejecutarReporteObjetos(nombreReporte, rutaReporte, rutaGenerado, nombreArchivo, tipoReporte, conexion);
                    //conexion.close();
                    return pathReporteGenerado;
                }
                return pathReporteGenerado;
            }
            log.warn("pathReporteGenerado: " + pathReporteGenerado);
            log.warn("Sali sin Errores");
            return pathReporteGenerado;
        } catch (SQLException ex) {
            log.warn("Error AdministrarReporte.generarReporteObjetos: " + ex);
            return null;
        }
    }
}
