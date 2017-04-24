/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Actividades;
import Entidades.Empleados;
import Entidades.Inforeportes;
import Entidades.ParametrosReportes;
import Entidades.ParametrosReportes;
import InterfaceAdministrar.AdministarReportesInterface;
import InterfaceAdministrar.AdministrarNReporteBienestarInterface;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import ControlNavegacion.ControlListaNavegacion;
import java.util.Map;
import java.util.LinkedHashMap;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.fill.AsynchronousFilllListener;
import org.primefaces.component.column.Column;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.inputtext.InputText;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author user
 */
@ManagedBean
@SessionScoped
public class ControlNReporteBienestar implements Serializable {

   @EJB
   AdministrarNReporteBienestarInterface administrarNReporteBienestar;
   @EJB
   AdministarReportesInterface administarReportes;
   //
   private ParametrosReportes parametroDeReporte;
   private List<Inforeportes> listaIR;
   private List<Inforeportes> filtrarListInforeportesUsuario;
   private Inforeportes inforreporteSeleccionado;
   //LOV INFOREPORTES
   private List<Inforeportes> lovInforeportes;
   private List<Inforeportes> filtrarLovInforeportes;
   private Inforeportes infoReporteLovSeleccionado;

   private String infoRegistro;
   //
   private String reporteGenerar;
   private int bandera;
   private boolean aceptar;
   private Column codigoIR, reporteIR, tipoIR;
   private int casilla;
   private ParametrosReportes parametroModificacion;
   private int tipoLista;
   private int posicionReporte;
   private String requisitosReporte;
   //
   private String actividad;
   private boolean permitirIndex, cambiosReporte;
   private InputText empleadoDesdeParametroL, empleadoHastaParametroL;
   //
   private List<Empleados> listEmpleados;
   private List<Empleados> filtrarListEmpleados;
   private Empleados empleadoSeleccionado;
   private List<Actividades> listActividades;
   private Actividades actividadSeleccionada;
   private List<Actividades> filtrarListActividades;
   //ALTO SCROLL TABLA
   private String altoTabla;
   private int indice;
   //EXPORTAR REPORTE
   private StreamedContent file;
   //
   private List<Inforeportes> listaInfoReportesModificados;
   //
   private String color, decoracion;
   private String color2, decoracion2;
   //
   private int casillaInforReporte;
   //
   private Date fechaDesde, fechaHasta;
   private BigDecimal emplDesde, emplHasta;
   //
   private boolean activoMostrarTodos, activoBuscarReporte, activarEnvio;
   //
   private String infoRegistroActividad, infoRegistroEmpleadoDesde, infoRegistroEmpleadoHasta, infoRegistroReportes;
   //EXPORTAR REPORTE
   private String pathReporteGenerado = null;
   private String nombreReporte, tipoReporte;
//
   private boolean estadoReporte;
   private String resultadoReporte;
   //VISUALIZAR REPORTE PDF
   private StreamedContent reporte;
   private String cabezeraVisor;
   private DataTable tabla;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();
   private ExternalContext externalContext;
   private String userAgent;

   public ControlNReporteBienestar() {
      activoMostrarTodos = true;
      activoBuscarReporte = false;
      activarEnvio = true;
      color = "black";
      decoracion = "none";
      color2 = "black";
      decoracion2 = "none";
      casillaInforReporte = -1;
      cambiosReporte = true;
      listaInfoReportesModificados = new ArrayList<>();
      altoTabla = "185";
      parametroDeReporte = null;
      listaIR = null;
      filtrarListInforeportesUsuario = null;
      bandera = 0;
      aceptar = true;
      casilla = -1;
      parametroModificacion = new ParametrosReportes();
      tipoLista = 0;
      reporteGenerar = "";
      requisitosReporte = "";
      posicionReporte = -1;
      permitirIndex = true;
      listEmpleados = null;
      empleadoSeleccionado = new Empleados();
      listActividades = null;
      actividadSeleccionada = new Actividades();
      mapParametros.put("paginaAnterior", paginaAnterior);
   }

   public void recibirPaginaEntrante(String pagina) {
      paginaAnterior = pagina;
      //inicializarCosas(); Inicializar cosas de ser necesario
   }

   public void recibirParametros(Map<String, Object> map) {
      mapParametros = map;
      paginaAnterior = (String) mapParametros.get("paginaAnterior");
      //inicializarCosas(); Inicializar cosas de ser necesario
   }

   //Reemplazar la funcion volverAtras, retornarPagina, Redirigir.....Atras.etc
   public void navegar(String pag) {
      FacesContext fc = FacesContext.getCurrentInstance();
      ControlListaNavegacion controlListaNavegacion = (ControlListaNavegacion) fc.getApplication().evaluateExpressionGet(fc, "#{controlListaNavegacion}", ControlListaNavegacion.class);
      /*if (pag.equals("atras")) {
         pag = paginaAnterior;
         paginaAnterior = "nominaf";
         controlListaNavegacion.quitarPagina(pagActual);

      } else {
         */
String pagActual = "nreportebienestar";
         
         
         


         
         
         
         
         
         
         if (pag.equals("atras")) {
         pag = paginaAnterior;
         paginaAnterior = "nominaf";
         controlListaNavegacion.quitarPagina(pagActual);
      } else {
	controlListaNavegacion.guardarNavegacion(pagActual, pag);
         fc.getApplication().getNavigationHandler().handleNavigation(fc, null, pag);
//Map<String, Object> mapParaEnviar = new LinkedHashMap<String, Object>();
         //mapParaEnviar.put("paginaAnterior", pagActual);
         //mas Parametros
//         if (pag.equals("rastrotabla")) {
//           ControlRastro controlRastro = (ControlRastro) fc.getApplication().evaluateExpressionGet(fc, "#{controlRastro}", ControlRastro.class);
         //           controlRastro.recibirDatosTabla(conceptoSeleccionado.getSecuencia(), "Conceptos", pagActual);
         //      } else if (pag.equals("rastrotablaH")) {
         //       ControlRastro controlRastro = (ControlRastro) fc.getApplication().evaluateExpressionGet(fc, "#{controlRastro}", ControlRastro.class);
         //     controlRastro.historicosTabla("Conceptos", pagActual);
         //   pag = "rastrotabla";
         //}
      }
      limpiarListasValor();

   }

   public void limpiarListasValor() {

   }

   @PostConstruct
   public void inicializarAdministrador() {
      try {
         FacesContext x = FacesContext.getCurrentInstance();
         HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
         externalContext = x.getExternalContext();
         userAgent = externalContext.getRequestHeaderMap().get("User-Agent");
         administrarNReporteBienestar.obtenerConexion(ses.getId());
         administarReportes.obtenerConexion(ses.getId());
      } catch (Exception e) {
         System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
         System.out.println("Causa: " + e.getCause());
      }
   }

   public void iniciarPagina() {
      activoMostrarTodos = true;
      activoBuscarReporte = false;
      activarEnvio = true;
      getListaIR();
   }

   public void requisitosParaReporte() {
      if (inforreporteSeleccionado.getFecdesde().equals("SI")) {
         requisitosReporte = requisitosReporte + "- Fecha Desde -";
      }
      if (inforreporteSeleccionado.getFechasta().equals("SI")) {
         requisitosReporte = requisitosReporte + "- Fecha Hasta -";
      }
      if (inforreporteSeleccionado.getEmdesde().equals("SI")) {
         requisitosReporte = requisitosReporte + "- Empleado Desde -";
      }
      if (inforreporteSeleccionado.getEmhasta().equals("SI")) {
         requisitosReporte = requisitosReporte + "- Empleado Hasta -";
      }
      if (!requisitosReporte.isEmpty()) {
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("formDialogos:requisitosReporte");
         RequestContext.getCurrentInstance().execute("PF('requisitosReporte').show()");
      }
   }

   public void seleccionRegistro() {
      defaultPropiedadesParametrosReporte();
      activarEnvioCorreo();
      if (inforreporteSeleccionado.getFecdesde().equals("SI")) {
         color = "red";
         decoracion = "underline";
         RequestContext.getCurrentInstance().update("formParametros");
      }
      if (inforreporteSeleccionado.getFechasta().equals("SI")) {
         color2 = "red";
         decoracion2 = "underline";
         RequestContext.getCurrentInstance().update("formParametros");
      }
      System.out.println("reporteSeleccionado.getEmdesde(): " + inforreporteSeleccionado.getEmdesde());
      if (inforreporteSeleccionado.getEmdesde().equals("SI")) {
         empleadoDesdeParametroL = (InputText) FacesContext.getCurrentInstance().getViewRoot().findComponent("formParametros:empleadoDesdeParametro");
         //empleadoDesdeParametro.setStyle("position: absolute; top: 41px; left: 150px; height: 10px; font-size: 11px; width: 90px; color: red;");
         if (!empleadoDesdeParametroL.getStyle().contains(" color: red;")) {
            empleadoDesdeParametroL.setStyle(empleadoDesdeParametroL.getStyle() + " color: red;");
         }
      } else {
         try {
            if (empleadoDesdeParametroL.getStyle().contains(" color: red;")) {

               System.out.println("reeemplazarr " + empleadoDesdeParametroL.getStyle().replace(" color: red;", ""));
               empleadoDesdeParametroL.setStyle(empleadoDesdeParametroL.getStyle().replace(" color: red;", ""));
            }
         } catch (Exception e) {
            e.printStackTrace();
         }
      }
      RequestContext.getCurrentInstance().update("formParametros:empleadoDesdeParametro");
      System.out.println("inforreporteSeleccionado.getEmhasta(): " + inforreporteSeleccionado.getEmhasta());
      if (inforreporteSeleccionado.getEmhasta().equals("SI")) {
         empleadoDesdeParametroL = (InputText) FacesContext.getCurrentInstance().getViewRoot().findComponent("formParametros:empleadoHastaParametro");
         empleadoDesdeParametroL.setStyle(empleadoDesdeParametroL.getStyle() + "color: red;");
         RequestContext.getCurrentInstance().update("formParametros:empleadoHastaParametro");
      }

      if (inforreporteSeleccionado.getEmhasta().equals("SI")) {
         empleadoHastaParametroL = (InputText) FacesContext.getCurrentInstance().getViewRoot().findComponent("formParametros:empleadoHastaParametroL");
//            empleadoHastaParametroL.setStyle("position: absolute; top: 40px; left: 400px;height: 10px;width: 100px; text-decoration: underline; color: red;");
         RequestContext.getCurrentInstance().update("formParametros:empleadoHastaParametroL");
      }
   }

   public void dispararDialogoGuardarCambios() {
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");

   }

   public void guardarYSalir() {
      guardarCambios();
      salir();
   }

   public void guardarCambios() {
      try {
         if (cambiosReporte == false) {
            if (parametroDeReporte.getUsuario() != null) {
               if (parametroDeReporte.getCodigoempleadodesde() == null) {
                  parametroDeReporte.setCodigoempleadodesde(null);
               }
               if (parametroDeReporte.getCodigoempleadohasta() == null) {
                  parametroDeReporte.setCodigoempleadohasta(null);
               }
               if (parametroDeReporte.getActividadbienestar().getSecuencia() == null) {
                  parametroDeReporte.setActividadbienestar(null);
               }
               administrarNReporteBienestar.modificarParametrosReportes(parametroDeReporte);
            }
            if (!listaInfoReportesModificados.isEmpty()) {
               administrarNReporteBienestar.guardarCambiosInfoReportes(listaInfoReportesModificados);
            }
            cambiosReporte = true;
            FacesMessage msg = new FacesMessage("Información", "Los datos se guardaron con Éxito.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         } else {
            parametroDeReporte.setFechadesde(fechaDesde);
            parametroDeReporte.setFechahasta(fechaHasta);
            RequestContext.getCurrentInstance().update("formParametros");
            RequestContext.getCurrentInstance().execute("PF('errorFechas').show()");
         }

      } catch (Exception e) {
         System.out.println("Error en guardar Cambios Controlador : " + e.toString());
         FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado, intente nuevamente");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      }
   }

   public void posicionCelda(int i) {
      if (permitirIndex) {
         casilla = i;
//            casillaInforReporte = -1;
         switch (casilla) {
            case 1:
               fechaDesde = parametroDeReporte.getFechadesde();
               break;
            case 2:
               emplDesde = parametroDeReporte.getCodigoempleadodesde();
               break;
            case 3:
               fechaHasta = parametroDeReporte.getFechahasta();
               break;
            case 4:
               emplHasta = parametroDeReporte.getCodigoempleadohasta();
               break;
            case 5:
               actividad = parametroDeReporte.getActividadbienestar().getDescripcion();
               break;
            default:
               break;
         }
      }
   }

   public void editarCelda() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (casilla >= 1) {
         if (casilla == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaDesde");
            RequestContext.getCurrentInstance().execute("PF('editarFechaDesde').show()");
         }
         if (casilla == 2) {
            RequestContext.getCurrentInstance().update("formularioDialogos:empleadoDesde");
            RequestContext.getCurrentInstance().execute("PF('empleadoDesde').show()");
         }

         if (casilla == 3) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaHasta");
            RequestContext.getCurrentInstance().execute("PF('editarFechaHasta').show()");
         }
         if (casilla == 4) {
            RequestContext.getCurrentInstance().update("formularioDialogos:empleadoHasta");
            RequestContext.getCurrentInstance().execute("PF('empleadoHasta').show()");
         }
         if (casilla == 5) {
            RequestContext.getCurrentInstance().update("formularioDialogos:actividad");
            RequestContext.getCurrentInstance().execute("PF('actividad').show()");
         }
         casilla = -1;
      }
      if (casillaInforReporte >= 1) {
         if (casillaInforReporte == 1) {
            RequestContext.getCurrentInstance().update("formParametros:infoReporteCodigoD");
            RequestContext.getCurrentInstance().execute("PF('infoReporteCodigoD').show()");
         }
         if (casillaInforReporte == 2) {
            RequestContext.getCurrentInstance().update("formParametros:infoReporteNombreD");
            RequestContext.getCurrentInstance().execute("PF('infoReporteNombreD').show()");
         }
         casillaInforReporte = -1;
      }
   }

   public void activarAceptar() {
      aceptar = false;
   }

   public void activarEnvioCorreo() {
      if (inforreporteSeleccionado != null) {
         activarEnvio = false;
      } else {
         activarEnvio = true;
      }
   }

   public void generarReporte(Inforeportes reporte) {
      try {
         inforreporteSeleccionado = reporte;
         seleccionRegistro();
         RequestContext.getCurrentInstance().execute("PF('generandoReporte').show()");
         generarDocumentoReporte();
      } catch (Exception e) {
         System.out.println("error en generarReporte : " + e.getMessage());
      }
   }

   public void generarDocumentoReporte() {
      try {
         RequestContext context = RequestContext.getCurrentInstance();
         if (inforreporteSeleccionado != null) {
            nombreReporte = inforreporteSeleccionado.getNombrereporte();
            tipoReporte = inforreporteSeleccionado.getTipo();

            if (nombreReporte != null && tipoReporte != null) {
               pathReporteGenerado = administarReportes.generarReporte(nombreReporte, tipoReporte);
            }
            if (pathReporteGenerado != null) {
               validarDescargaReporte();
            } else {
               RequestContext.getCurrentInstance().execute("PF('generandoReporte').hide()");
               RequestContext.getCurrentInstance().update("formDialogos:errorGenerandoReporte");
               RequestContext.getCurrentInstance().execute("PF('errorGenerandoReporte').show()");
            }
         } else {
            System.out.println("Reporte Seleccionado es nulo");
         }
      } catch (Exception e) {
         System.out.println("Error en generarDocumentoReporte : " + e.getMessage());
      }

   }

   public void validarDescargaReporte() {
      try {
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().execute("PF('generandoReporte').hide()");
         if (pathReporteGenerado != null && !pathReporteGenerado.startsWith("Error:")) {
            if (!tipoReporte.equals("PDF")) {
               RequestContext.getCurrentInstance().execute("PF('descargarReporte').show()");
            } else {
               FileInputStream fis;
               try {
                  fis = new FileInputStream(new File(pathReporteGenerado));
                  reporte = new DefaultStreamedContent(fis, "application/pdf");
               } catch (FileNotFoundException ex) {
                  System.out.println(ex.getCause());
                  reporte = null;
               }
               if (reporte != null) {
                  if (inforreporteSeleccionado != null) {
                     System.out.println("userAgent " + userAgent);
                     if (userAgent.toUpperCase().contains("Mobile".toUpperCase()) || userAgent.toUpperCase().contains("Tablet".toUpperCase()) || userAgent.toUpperCase().contains("Android".toUpperCase())) {
                        context.update("formDialogos:descargarReporte");
                        context.execute("PF('descargarReporte').show();");
                     } else {
                        RequestContext.getCurrentInstance().update("formDialogos:verReportePDF");
                        RequestContext.getCurrentInstance().execute("PF('verReportePDF').show()");
                     }
                     cabezeraVisor = "Reporte - " + nombreReporte;
                  } else {
                     cabezeraVisor = "Reporte - ";
                  }
               }
            }
         } else {
            System.out.println("validar descarga reporte - ingreso al if 1 else");
            RequestContext.getCurrentInstance().update("formDialogos:errorGenerandoReporte");
            RequestContext.getCurrentInstance().execute("PF('errorGenerandoReporte').show()");
         }
      } catch (Exception e) {
         System.out.println("error en validarDescargarReporte : " + e.getMessage());
      }
   }

   public void reiniciarStreamedContent() {
      System.out.println(this.getClass().getName() + ".reiniciarStreamedContent()");
      reporte = null;
   }

   public void cancelarReporte() {
      System.out.println(this.getClass().getName() + ".cancelarReporte()");
      administarReportes.cancelarReporte();
   }

   public AsynchronousFilllListener listener() {
      System.out.println(this.getClass().getName() + ".listener()");
      return new AsynchronousFilllListener() {
         //RequestContext context = c;

         @Override
         public void reportFinished(JasperPrint jp) {
            System.out.println(this.getClass().getName() + ".listener().reportFinished()");
            try {
               estadoReporte = true;
               resultadoReporte = "Exito";
               /*
                     * final RequestContext currentInstance =
                     * RequestContext.getCurrentInstance();
                     * Renderer.instance().render(template);
                     * RequestContext.setCurrentInstance(currentInstance)
                */
               // RequestContext.getCurrentInstance().execute("PF('formDialogos:generandoReporte");
               //generarArchivoReporte(jp);
            } catch (Exception e) {
               System.out.println("ControlNReportePersonal reportFinished ERROR: " + e.toString());
            }
         }

         @Override
         public void reportCancelled() {
            System.out.println(this.getClass().getName() + ".listener().reportCancelled()");
            estadoReporte = true;
            resultadoReporte = "Cancelación";
         }

         @Override
         public void reportFillError(Throwable e) {
            System.out.println(this.getClass().getName() + ".listener().reportFillError()");
            if (e.getCause() != null) {
               pathReporteGenerado = "ControlNReportePersonal reportFillError Error: " + e.toString() + "\n" + e.getCause().toString();
            } else {
               pathReporteGenerado = "ControlNReportePersonal reportFillError Error: " + e.toString();
            }
            estadoReporte = true;
            resultadoReporte = "Se estallo";
         }

      };
   }

   public void generarArchivoReporte(JasperPrint print) {
      System.out.println(this.getClass().getName() + ".generarArchivoReporte()");
      if (print != null && tipoReporte != null) {
         pathReporteGenerado = administarReportes.crearArchivoReporte(print, tipoReporte);
         validarDescargaReporte();
      }
   }

   public void mostrarDialogoGenerarReporte() {
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("formDialogos:reporteAGenerar");
      RequestContext.getCurrentInstance().execute("PF('reporteAGenerar').show()");
   }

   public void cancelarGenerarReporte() {
      reporteGenerar = "";
      posicionReporte = -1;
   }

   public void mostrarDialogoBuscarReporte() {
      System.out.println("Controlador.ControlNReporteBienestar.mostrarDialogoBuscarReporte()");
      try {
         System.out.println("Ingrese al try");
         if (cambiosReporte == true) {
            contarRegistrosLovReportes();
            contarRegistros();
            RequestContext.getCurrentInstance().update("formDialogos:ReportesDialogo");
            RequestContext.getCurrentInstance().execute("PF('ReportesDialogo').show()");
         } else {
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardarSinSalida').show()");
         }
      } catch (Exception e) {
         System.out.println("Error mostrarDialogoBuscarReporte : " + e.toString());
      }
   }

   public void eventoFiltrar() {
      contarRegistros();
   }

   public void contarRegistros() {
      System.out.println("Controlador.ControlNReporteBienestar.contarRegistros()");
      RequestContext.getCurrentInstance().update("form:infoRegistro");
   }

   public void contarRegistrosLovReportes() {
      System.out.println("Controlador.ControlNReporteBienestar.contarRegistrosLovReportes()");
      RequestContext.getCurrentInstance().update("formDialogos:infoRegistroReportes");
   }

   public void contarRegistrosEmpleadoD() {
      RequestContext.getCurrentInstance().update("formDialogos:infoRegistroEmpleadoDesde");
   }

   public void contarRegistrosEmpleadoH() {
      RequestContext.getCurrentInstance().update("formDialogos:infoRegistroEmpleadoHasta");
   }

   public void contarRegistrosActividad() {
      RequestContext.getCurrentInstance().update("formDialogos:infoRegistroActividad");
   }

   public void actualizarSeleccionInforeporte() {
      if (bandera == 1) {
         cerrarFiltrado();
      }
      defaultPropiedadesParametrosReporte();
      RequestContext context = RequestContext.getCurrentInstance();
      listaIR.clear();
      listaIR.add(inforreporteSeleccionado);
      filtrarListInforeportesUsuario = null;
      filtrarLovInforeportes = null;
      inforreporteSeleccionado = new Inforeportes();
      infoReporteLovSeleccionado = null;
      filtrarLovInforeportes = null;
      aceptar = true;
      activoBuscarReporte = true;
      activoMostrarTodos = false;
      activarEnvioCorreo();
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
      RequestContext.getCurrentInstance().update("form:MOSTRARTODOS");
      RequestContext.getCurrentInstance().update("form:BUSCARREPORTE");
      context.reset("formDialogos:lovReportesDialogo:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovReportesDialogo').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('ReportesDialogo').hide()");
      RequestContext.getCurrentInstance().update(":form:reportesBienestar");
   }

   public void cancelarSeleccionInforeporte() {
      filtrarListInforeportesUsuario = null;
      inforreporteSeleccionado = new Inforeportes();
      aceptar = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formDialogos:lovReportesDialogo:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovReportesDialogo').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('ReportesDialogo').hide()");
   }

   public void salir() {
      limpiarListasValor();
      if (bandera == 1) {
         cerrarFiltrado();
      }
      listEmpleados = null;
      listaIR = null;
      listActividades = null;
      parametroDeReporte = null;
      parametroModificacion = null;
      casilla = -1;
      actividadSeleccionada = null;
      listaInfoReportesModificados.clear();
      cambiosReporte = true;
      empleadoSeleccionado = null;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      navegar("atras");
   }

   public void cancelarYSalir() {
      cancelarModificaciones();
      salir();
   }

   public void cancelarModificaciones() {
      if (bandera == 1) {
         cerrarFiltrado();
      }
      defaultPropiedadesParametrosReporte();
      listaIR = null;
      parametroDeReporte = null;
      parametroModificacion = null;
      casilla = -1;
      listaInfoReportesModificados.clear();
      cambiosReporte = true;
      activoBuscarReporte = false;
      activoMostrarTodos = true;
      activarEnvioCorreo();
      getParametroDeReporte();
      getListaIR();
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
      RequestContext.getCurrentInstance().update("form:MOSTRARTODOS");
      RequestContext.getCurrentInstance().update("form:BUSCARREPORTE");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      RequestContext.getCurrentInstance().update("form:reportesBienestar");
      RequestContext.getCurrentInstance().update("formParametros:fechaDesdeParametroL");
      RequestContext.getCurrentInstance().update("formParametros:empleadoDesdeParametroL");
      RequestContext.getCurrentInstance().update("formParametros:fechaHastaParametroL");
      RequestContext.getCurrentInstance().update("formParametros:empleadoHastaParametroL");
      RequestContext.getCurrentInstance().update("formParametros:estadoParametroL");
      RequestContext.getCurrentInstance().update("formParametros:actividadParametroL");
   }

   public void modificacionTipoReporte(Inforeportes reporte) {
      inforreporteSeleccionado = reporte;
      cambiosReporte = false;
      if (listaInfoReportesModificados.isEmpty()) {
         listaInfoReportesModificados.add(inforreporteSeleccionado);
      } else if ((!listaInfoReportesModificados.isEmpty()) && (!listaInfoReportesModificados.contains(inforreporteSeleccionado))) {
         listaInfoReportesModificados.add(inforreporteSeleccionado);
      } else {
         int posicion = listaInfoReportesModificados.indexOf(inforreporteSeleccionado);
         listaInfoReportesModificados.set(posicion, inforreporteSeleccionado);
      }
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void mostrarDialogosListas() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (casilla == 2) {
         if ((listEmpleados == null) || listEmpleados.isEmpty()) {
            listEmpleados = null;
         }
         RequestContext.getCurrentInstance().update("formDialogos:EmpleadoDesdeDialogo");
         RequestContext.getCurrentInstance().execute("PF('EmpleadoDesdeDialogo').show()");
      }
      if (casilla == 4) {
         RequestContext.getCurrentInstance().update("formDialogos:EmpleadoHastaDialogo");
         RequestContext.getCurrentInstance().execute("PF('EmpleadoHastaDialogo').show()");
      }
      if (casilla == 5) {
         RequestContext.getCurrentInstance().update("formDialogos:ActividadDialogo");
         RequestContext.getCurrentInstance().execute("PF('ActividadDialogo').show()");
      }
   }

   public void actualizarActividad() {
      permitirIndex = true;
      parametroDeReporte.setActividadbienestar(actividadSeleccionada);
      parametroModificacion = parametroDeReporte;
      actividadSeleccionada = null;
      aceptar = true;
      filtrarListActividades = null;
      cambiosReporte = false;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formDialogos:lovActividad:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovActividad').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('ActividadDialogo').hide()");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      RequestContext.getCurrentInstance().update("formParametros:actividadParametroL");
   }

   public void cancelarCambioActividad() {
      actividadSeleccionada = null;
      aceptar = true;
      filtrarListActividades = null;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formDialogos:lovActividad:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovActividad').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('ActividadDialogo').hide()");
   }

   public void actualizarEmplDesde() {
      permitirIndex = true;
      parametroDeReporte.setCodigoempleadodesde(empleadoSeleccionado.getCodigoempleado());
      parametroModificacion = parametroDeReporte;
      empleadoSeleccionado = null;
      aceptar = true;
      filtrarListEmpleados = null;
      cambiosReporte = false;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formDialogos:lovEmpleadoDesde:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovEmpleadoDesde').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('EmpleadoDesdeDialogo').hide()");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      RequestContext.getCurrentInstance().update("formParametros:empleadoDesdeParametroL");
   }

   public void cancelarCambioEmplDesde() {
      empleadoSeleccionado = null;
      aceptar = true;
      filtrarListEmpleados = null;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formDialogos:lovEmpleadoDesde:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovEmpleadoDesde').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('EmpleadoDesdeDialogo').hide()");
   }

   public void actualizarEmplHasta() {
      permitirIndex = true;
      parametroDeReporte.setCodigoempleadohasta(empleadoSeleccionado.getCodigoempleado());
      parametroModificacion = parametroDeReporte;
      empleadoSeleccionado = null;
      aceptar = true;
      filtrarListEmpleados = null;
      cambiosReporte = false;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formDialogos:lovEmpleadoHasta:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovEmpleadoHasta').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('EmpleadoHastaDialogo').hide()");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      RequestContext.getCurrentInstance().update("formParametros:empleadoHastaParametroL");
   }

   public void cancelarCambioEmplHasta() {
      empleadoSeleccionado = null;
      aceptar = true;
      filtrarListEmpleados = null;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formDialogos:lovEmpleadoHasta:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovEmpleadoHasta').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('EmpleadoHastaDialogo').hide()");
   }

   public void autocompletarGeneral(String campoConfirmar, String valorConfirmar) {
      RequestContext context = RequestContext.getCurrentInstance();
      int indiceUnicoElemento = -1;
      int coincidencias = 0;
      if (campoConfirmar.equalsIgnoreCase("ACTIVIDAD")) {
         if (!valorConfirmar.isEmpty()) {
            parametroDeReporte.getActividadbienestar().setDescripcion(actividad);
            for (int i = 0; i < listActividades.size(); i++) {
               if (listActividades.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               parametroDeReporte.setActividadbienestar(listActividades.get(indiceUnicoElemento));
               parametroModificacion = parametroDeReporte;
               listActividades.clear();
               getListActividades();
               cambiosReporte = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            } else {
               permitirIndex = false;
               RequestContext.getCurrentInstance().update("formDialogos:ActividadDialogo");
               RequestContext.getCurrentInstance().execute("PF('ActividadDialogo').show()");
            }
         } else {
            listActividades.clear();
            getListActividades();
            parametroDeReporte.setActividadbienestar(new Actividades());
            parametroModificacion = parametroDeReporte;
            cambiosReporte = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
      }
   }

   public void modificarParametroEmpleadoDesde(BigDecimal empldesde) {
      if (empldesde.equals(BigDecimal.valueOf(0))) {
         parametroDeReporte.setCodigoempleadodesde(BigDecimal.valueOf(0));
      }
      cambiosReporte = false;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void modificarParametroEmpleadoHasta(BigDecimal emphasta) {
      String h = "99999999999999999999999999";
      BigDecimal b = new BigDecimal(h);
      if (emphasta.equals(b)) {
         parametroDeReporte.setCodigoempleadodesde(b);
      }
      cambiosReporte = false;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void activarCtrlF11() {
      if (bandera == 0) {
         altoTabla = "165";
         codigoIR = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:reportesBienestar:codigoIR");
         codigoIR.setFilterStyle("width: 85% !important;");
         reporteIR = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:reportesBienestar:reporteIR");
         reporteIR.setFilterStyle("width: 85% !important;");
         tipoIR = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:reportesBienestar:tipoIR");
         tipoIR.setFilterStyle("width: 85% !important;");
         RequestContext.getCurrentInstance().update("form:reportesBienestar");
         tipoLista = 1;
         bandera = 1;
      } else if (bandera == 1) {
         cerrarFiltrado();
      }

   }

   public void cerrarFiltrado() {
      altoTabla = "185";
      defaultPropiedadesParametrosReporte();
      codigoIR = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:reportesBienestar:codigoIR");
      codigoIR.setFilterStyle("display: none; visibility: hidden;");
      reporteIR = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:reportesBienestar:reporteIR");
      reporteIR.setFilterStyle("display: none; visibility: hidden;");
      tipoIR = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:reportesBienestar:tipoIR");
      tipoIR.setFilterStyle("display: none; visibility: hidden;");
      RequestContext.getCurrentInstance().update("form:reportesBienestar");
      bandera = 0;
      filtrarListInforeportesUsuario = null;
      tipoLista = 0;
   }

   public void reporteSeleccionado(int i) {
      System.out.println("Posicion del reporte : " + i);
   }

   public void defaultPropiedadesParametrosReporte() {
      color = "black";
      decoracion = "none";
      color2 = "black";
      decoracion2 = "none";
   }

   public void modificarParametroInforme() {
      if (parametroDeReporte.getFechadesde() != null && parametroDeReporte.getFechahasta() != null) {
         if (parametroDeReporte.getFechadesde().before(parametroDeReporte.getFechahasta())) {
            System.out.println("Entre al segundo if");
            parametroModificacion = parametroDeReporte;
            cambiosReporte = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         } else {
            System.out.println("entre a else del segundo if");
            cambiosReporte = true;
         }
      }
   }

   public void mostrarTodos() {
      if (cambiosReporte == true) {
         defaultPropiedadesParametrosReporte();
         listaIR.clear();
         for (int i = 0; i < lovInforeportes.size(); i++) {
            listaIR.add(lovInforeportes.get(i));
         }
         contarRegistros();
         RequestContext context = RequestContext.getCurrentInstance();
         activoBuscarReporte = false;
         activoMostrarTodos = true;
         activarEnvioCorreo();
         RequestContext.getCurrentInstance().update("form:MOSTRARTODOS");
         RequestContext.getCurrentInstance().update("form:BUSCARREPORTE");
         RequestContext.getCurrentInstance().update("form:reportesBienestar");
      } else {
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().execute("PF('confirmarGuardarSinSalida').show()");
      }
   }

   public void exportarReporte() throws IOException {
      if (pathReporteGenerado != null && pathReporteGenerado.startsWith("Error:")) {
         File reporte = new File(pathReporteGenerado);
         FacesContext ctx = FacesContext.getCurrentInstance();
         FileInputStream fis = new FileInputStream(reporte);
         byte[] bytes = new byte[1024];
         int read;
         if (!ctx.getResponseComplete()) {
            String fileName = reporte.getName();
            HttpServletResponse response = (HttpServletResponse) ctx.getExternalContext().getResponse();
            response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
            ServletOutputStream out = response.getOutputStream();

            while ((read = fis.read(bytes)) != -1) {
               out.write(bytes, 0, read);
            }
            out.flush();
            out.close();
            System.out.println("\nDescargado\n");
            ctx.responseComplete();
         }
      } else {
         System.out.println("validar descarga reporte - ingreso al if 1 else");
         RequestContext.getCurrentInstance().update("formDialogos:errorGenerandoReporte");
         RequestContext.getCurrentInstance().execute("PF('errorGenerandoReporte').show()");
      }
   }

   public void cancelarRequisitosReporte() {
      requisitosReporte = "";
   }

   //GETTER AND SETTER
   public ParametrosReportes getParametroDeReporte() {
      try {
         if (parametroDeReporte == null) {
            parametroDeReporte = new ParametrosReportes();
            parametroDeReporte = administrarNReporteBienestar.parametrosDeReporte();
         }
         if (parametroDeReporte.getActividadbienestar() == null) {
            parametroDeReporte.setActividadbienestar(new Actividades());
         }
         return parametroDeReporte;
      } catch (Exception e) {
         System.out.println("Error getParametroDeInforme : " + e);
         return null;
      }
   }

   public void setParametroDeReporte(ParametrosReportes parametroDeReporte) {
      this.parametroDeReporte = parametroDeReporte;
   }

   public List<Inforeportes> getListaIR() {
      try {
         if (listaIR == null) {
            listaIR = administrarNReporteBienestar.listInforeportesUsuario();
         }
         return listaIR;
      } catch (Exception e) {
         System.out.println("Error getListInforeportesUsuario : " + e);
         return null;
      }
   }

   public void setListaIR(List<Inforeportes> listaIR) {
      this.listaIR = listaIR;
   }

   public List<Inforeportes> getFiltrarListInforeportesUsuario() {
      return filtrarListInforeportesUsuario;
   }

   public void setFiltrarListInforeportesUsuario(List<Inforeportes> filtrarListInforeportesUsuario) {
      this.filtrarListInforeportesUsuario = filtrarListInforeportesUsuario;
   }

   public Inforeportes getInforreporteSeleccionado() {
      return inforreporteSeleccionado;
   }

   public void setInforreporteSeleccionado(Inforeportes inforreporteSeleccionado) {
      this.inforreporteSeleccionado = inforreporteSeleccionado;
   }

   public boolean isAceptar() {
      return aceptar;
   }

   public void setAceptar(boolean aceptar) {
      this.aceptar = aceptar;
   }

   public ParametrosReportes getParametroModificacion() {
      return parametroModificacion;
   }

   public void setParametroModificacion(ParametrosReportes parametroModificacion) {
      this.parametroModificacion = parametroModificacion;
   }

   public String getReporteGenerar() {
      if (posicionReporte != -1) {
         reporteGenerar = listaIR.get(posicionReporte).getNombre();
      }
      return reporteGenerar;
   }

   public void setReporteGenerar(String reporteGenerar) {
      this.reporteGenerar = reporteGenerar;
   }

   public String getRequisitosReporte() {
      return requisitosReporte;
   }

   public void setRequisitosReporte(String requisitosReporte) {
      this.requisitosReporte = requisitosReporte;
   }

   public List<Empleados> getListEmpleados() {
      if (listEmpleados == null || listEmpleados.isEmpty()) {
         listEmpleados = administrarNReporteBienestar.listEmpleados();
      }
      return listEmpleados;
   }

   public void setListEmpleados(List<Empleados> listEmpleados) {
      this.listEmpleados = listEmpleados;
   }

   public List<Empleados> getFiltrarListEmpleados() {
      return filtrarListEmpleados;
   }

   public void setFiltrarListEmpleados(List<Empleados> filtrarListEmpleados) {
      this.filtrarListEmpleados = filtrarListEmpleados;
   }

   public Empleados getEmpleadoSeleccionado() {
      return empleadoSeleccionado;
   }

   public void setEmpleadoSeleccionado(Empleados empleadoSeleccionado) {
      this.empleadoSeleccionado = empleadoSeleccionado;
   }

   public List<Actividades> getListActividades() {
      if (listActividades == null || listActividades.isEmpty()) {
         listActividades = administrarNReporteBienestar.listActividades();
      }
      return listActividades;
   }

   public void setListActividades(List<Actividades> listActividades) {
      this.listActividades = listActividades;
   }

   public Actividades getActividadSeleccionada() {
      return actividadSeleccionada;
   }

   public void setActividadSeleccionada(Actividades actividadSeleccionada) {
      this.actividadSeleccionada = actividadSeleccionada;
   }

   public List<Actividades> getFiltrarListActividades() {
      return filtrarListActividades;
   }

   public void setFiltrarListActividades(List<Actividades> filtrarListActividades) {
      this.filtrarListActividades = filtrarListActividades;
   }

   public boolean isCambiosReporte() {
      return cambiosReporte;
   }

   public void setCambiosReporte(boolean cambiosReporte) {
      this.cambiosReporte = cambiosReporte;
   }

   public List<Inforeportes> getListaInfoReportesModificados() {
      return listaInfoReportesModificados;
   }

   public void setListaInfoReportesModificados(List<Inforeportes> listaInfoReportesModificados) {
      this.listaInfoReportesModificados = listaInfoReportesModificados;
   }

   public String getAltoTabla() {
      return altoTabla;
   }

   public void setAltoTabla(String altoTabla) {
      this.altoTabla = altoTabla;
   }

   public StreamedContent getFile() {
      return file;
   }

   public void setFile(StreamedContent file) {
      this.file = file;
   }

   public String getColor() {
      return color;
   }

   public void setColor(String color) {
      this.color = color;
   }

   public String getDecoracion() {
      return decoracion;
   }

   public void setDecoracion(String decoracion) {
      this.decoracion = decoracion;
   }

   public String getColor2() {
      return color2;
   }

   public void setColor2(String color) {
      this.color2 = color;
   }

   public String getDecoracion2() {
      return decoracion2;
   }

   public void setDecoracion2(String decoracion) {
      this.decoracion2 = decoracion;
   }

   public boolean isActivoMostrarTodos() {
      return activoMostrarTodos;
   }

   public void setActivoMostrarTodos(boolean activoMostrarTodos) {
      this.activoMostrarTodos = activoMostrarTodos;
   }

   public boolean isActivoBuscarReporte() {
      return activoBuscarReporte;
   }

   public void setActivoBuscarReporte(boolean activoBuscarReporte) {
      this.activoBuscarReporte = activoBuscarReporte;
   }

   public boolean isActivarEnvio() {
      return activarEnvio;
   }

   public void setActivarEnvio(boolean activarEnvio) {
      this.activarEnvio = activarEnvio;
   }

   public String getInfoRegistro() {
      FacesContext c = FacesContext.getCurrentInstance();
      tabla = (DataTable) c.getViewRoot().findComponent("form:reportesBienestar");
      infoRegistro = String.valueOf(tabla.getRowCount());
      return infoRegistro;
   }

   public String getInfoRegistroActividad() {
      FacesContext c = FacesContext.getCurrentInstance();
      tabla = (DataTable) c.getViewRoot().findComponent("formDialogos:lovActividad");
      infoRegistroActividad = String.valueOf(tabla.getRowCount());
      return infoRegistroActividad;
   }

   public String getInfoRegistroEmpleadoDesde() {
      FacesContext c = FacesContext.getCurrentInstance();
      tabla = (DataTable) c.getViewRoot().findComponent("formDialogos:lovEmpleadoDesde");
      infoRegistroEmpleadoDesde = String.valueOf(tabla.getRowCount());
      return infoRegistroEmpleadoDesde;
   }

   public String getInfoRegistroEmpleadoHasta() {
      FacesContext c = FacesContext.getCurrentInstance();
      tabla = (DataTable) c.getViewRoot().findComponent("formDialogos:lovEmpleadoHasta");
      infoRegistroEmpleadoHasta = String.valueOf(tabla.getRowCount());
      return infoRegistroEmpleadoHasta;
   }

   public String getInfoRegistroReportes() {
      FacesContext c = FacesContext.getCurrentInstance();
      tabla = (DataTable) c.getViewRoot().findComponent("formDialogos:lovReportesDialogo");
      infoRegistroReportes = String.valueOf(tabla.getRowCount());
      return infoRegistroReportes;
   }

   public List<Inforeportes> getLovInforeportes() {
      lovInforeportes = administrarNReporteBienestar.listInforeportesUsuario();
      return lovInforeportes;
   }

   public void setLovInforeportes(List<Inforeportes> lovInforeportes) {
      this.lovInforeportes = lovInforeportes;
   }

   public List<Inforeportes> getFiltrarLovInforeportes() {
      return filtrarLovInforeportes;
   }

   public void setFiltrarLovInforeportes(List<Inforeportes> filtrarLovInforeportes) {
      this.filtrarLovInforeportes = filtrarLovInforeportes;
   }

   public Inforeportes getInfoReporteLovSeleccionado() {
      return infoReporteLovSeleccionado;
   }

   public void setInfoReporteLovSeleccionado(Inforeportes infoReporteLovSeleccionado) {
      this.infoReporteLovSeleccionado = infoReporteLovSeleccionado;
   }

   public String getPathReporteGenerado() {
      return pathReporteGenerado;
   }

   public void setPathReporteGenerado(String pathReporteGenerado) {
      this.pathReporteGenerado = pathReporteGenerado;
   }

   public String getCabezeraVisor() {
      return cabezeraVisor;
   }

   public void setCabezeraVisor(String cabezeraVisor) {
      this.cabezeraVisor = cabezeraVisor;
   }

}
