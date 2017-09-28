/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Empleados;
import Entidades.Empresas;
import Entidades.Inforeportes;
import Entidades.ParametrosReportes;
import InterfaceAdministrar.AdministarReportesInterface;
import InterfaceAdministrar.AdministrarNReporteCapacitacionInterface;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import ControlNavegacion.ControlListaNavegacion;
import ControlNavegacion.ListasRecurrentes;
import java.util.Map;
import java.util.LinkedHashMap;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.fill.AsynchronousFilllListener;
import org.apache.log4j.Logger;
import org.primefaces.component.column.Column;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.inputtext.InputText;
import org.primefaces.component.selectonemenu.SelectOneMenu;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author user
 */
@ManagedBean
@SessionScoped
public class ControlNReporteCapacitacion implements Serializable {

   private static Logger log = Logger.getLogger(ControlNReporteCapacitacion.class);

   @EJB
   AdministrarNReporteCapacitacionInterface administrarNReporteCapacitacion;
   @EJB
   AdministarReportesInterface administarReportes;

   //PARAMETROS REPORTES
   private ParametrosReportes parametroDeReporte;
   private ParametrosReportes parametroModificacion;
   private ParametrosReportes parametroFecha;
   //INFOREPORTES
   private List<Inforeportes> listaIR;
   private Inforeportes reporteSeleccionado;
   private List<Inforeportes> filtrarListIRU;
   private List<Inforeportes> listaInfoReportesModificados;
   //INFOREPORTES LOV
   private List<Inforeportes> lovInforeportes;
   private Inforeportes reporteSeleccionadoLOV;
   private List<Inforeportes> filtrarLovInforeportes;
   private List<Inforeportes> filtrarReportes;
   //EMPLEADOS
   private List<Empleados> lovEmpleados;
   private Empleados empleadoSeleccionado;
   private List<Empleados> lovEmpleadosFiltrar;
   //EMPRESAS
   private List<Empresas> lovEmpresas;
   private Empresas empresaSeleccionada;
   private List<Empresas> lovEmpresasFiltrar;
//GENERAR    
   private String reporteGenerar;
   //OTROS
   private int bandera;
   private boolean aceptar;
   private int casilla;
   private int posicionReporte;
   private String requisitosReporte;
   private String empresa, estado;
   private boolean permitirIndex, cambiosReporte;
   private String color, decoracion;
   private String color2, decoracion2;
   //INPUT    
   private InputText empleadoDesdeParametro, empleadoHastaParametro;
   //private InputText empresaParametro,  procesoParametro, notasParametro, asociacionParametro, ubicacionGeograficaParametro, tipoAsociacionParametro;
   private SelectOneMenu estadoParametro;
   //ALTO SCROLL TABLA
   private String altoTabla;
   //EXPORTAR REPORTE
   private String pathReporteGenerado;
   private String nombreReporte, tipoReporte;
   //DATE
   private Date fechaDesde, fechaHasta;
   private BigDecimal emplDesde, emplHasta;
   //MOSTRAR TODOS
   private boolean activoMostrarTodos, activoBuscarReporte, activarEnvio;
   //VISUALIZAR REPORTE PDF
   private StreamedContent reporte;
   private String cabezeraVisor;
   //Listener reporte
   private AsynchronousFilllListener asistenteReporte;
   //BANDERAS
   private boolean estadoReporte;
   private String resultadoReporte;
   //FileInputStream prueba;
   //
   private String infoRegistro, infoRegistroLovReportes, infoRegistroEmpleadoDesde, infoRegistroEmpleadoHasta, infoRegistroEmpresa;
   private DataTable tabla;
   private int tipoLista;
   private int casillaInforReporte;
   private Column codigoIR, reporteIR;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();
   private ExternalContext externalContext;
   private String userAgent;
   private ListasRecurrentes listasRecurrentes;

   public ControlNReporteCapacitacion() {
      FacesContext fc = FacesContext.getCurrentInstance();
      ControlListaNavegacion controlListaNavegacion = (ControlListaNavegacion) fc.getApplication().evaluateExpressionGet(fc, "#{controlListaNavegacion}", ControlListaNavegacion.class);
      listasRecurrentes = controlListaNavegacion.getListasRecurrentes();
      log.info(this.getClass().getName() + ".Constructor()");
      activoMostrarTodos = true;
      activoBuscarReporte = false;
      activarEnvio = true;
      color = "black";
      decoracion = "none";
      color2 = "black";
      decoracion2 = "none";
      reporteSeleccionadoLOV = null;
      reporteSeleccionado = null;
      cambiosReporte = true;
      listaInfoReportesModificados = new ArrayList<>();
      parametroDeReporte = null;
      listaIR = null;
      bandera = 0;
      aceptar = true;
      casilla = -1;
      parametroModificacion = new ParametrosReportes();
      parametroFecha = new ParametrosReportes();
      reporteGenerar = "";
      requisitosReporte = "";
      posicionReporte = -1;
      lovInforeportes = null;
      lovEmpleados = new ArrayList<>();
      lovEmpresas = null;
      tipoLista = 0;
      empleadoSeleccionado = new Empleados();
      empresaSeleccionada = new Empresas();
      permitirIndex = true;
      altoTabla = "200";
      cabezeraVisor = null;
      estadoReporte = false;
      casillaInforReporte = -1;
      log.info(this.getClass().getName() + " fin del Constructor()");
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
      String pagActual = "nreportecapacitacion";

      if (pag.equals("atras")) {
         pag = paginaAnterior;
         paginaAnterior = "nominaf";
         controlListaNavegacion.quitarPagina(pagActual, this.getClass().getSimpleName());
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
      lovEmpleados = null;
      lovEmpresas = null;
      lovInforeportes = null;
   }

   @PreDestroy
   public void destruyendoce() {
      log.info(this.getClass().getName() + ".destruyendoce() @Destroy");
   }

   @PostConstruct
   public void iniciarAdministradores() {
      log.info(this.getClass().getName() + ".iniciarAdministradores()");
      try {
         FacesContext contexto = FacesContext.getCurrentInstance();
         HttpSession ses = (HttpSession) contexto.getExternalContext().getSession(false);
         externalContext = contexto.getExternalContext();
         userAgent = externalContext.getRequestHeaderMap().get("User-Agent");
         administarReportes.obtenerConexion(ses.getId());
         administrarNReporteCapacitacion.obtenerConexion(ses.getId());
         log.info(this.getClass().getName() + " fin de iniciarAdministradores()");
      } catch (Exception e) {
         log.error("Error postconstruct controlNReporteNomina" + e);
         log.info("Causa: " + e.getMessage());
      }
   }

   public void iniciarPagina() {
      log.info(this.getClass().getName() + ".iniciarPagina()");
      activoMostrarTodos = true;
      activoBuscarReporte = false;
      activarEnvio = true;
      getListaIR();
   }

   //TOOLTIP
   //GUARDAR CAMBIOS
   public void guardarCambios() {
      log.info(this.getClass().getName() + ".guardarCambios()");
      RequestContext context = RequestContext.getCurrentInstance();
      log.info("cambiosReporte " + cambiosReporte);
      try {
         if (!cambiosReporte) {
            log.info("Entre a if (cambiosReporte == false)");
            if (parametroDeReporte.getUsuario() != null) {

               if (parametroDeReporte.getCodigoempleadodesde() == null) {
                  parametroDeReporte.setCodigoempleadodesde(null);
               }
               if (parametroDeReporte.getCodigoempleadohasta() == null) {
                  parametroDeReporte.setCodigoempleadohasta(null);
               }
               if (parametroDeReporte.getEmpresa().getSecuencia() == null) {
                  parametroDeReporte.setEmpresa(null);
               }
               administrarNReporteCapacitacion.modificarParametrosReportes(parametroDeReporte);
            }
            if (!listaInfoReportesModificados.isEmpty()) {
               administrarNReporteCapacitacion.guardarCambiosInfoReportes(listaInfoReportesModificados);
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
         log.warn("Error en guardar Cambios Controlador : " + e.toString());
         FacesMessage msg = new FacesMessage("Información", "ha ocurrido un error en el guardado, intente nuevamente");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      }
   }

   //EDITAR
   public void editarCelda() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (casilla >= 1) {
         if (casilla == 1) {
            RequestContext.getCurrentInstance().update("formDialogos:editarFechaDesde");
            RequestContext.getCurrentInstance().execute("PF('editarFechaDesde').show()");
         }
         if (casilla == 2) {
            RequestContext.getCurrentInstance().update("formDialogos:editarFechaHasta");
            RequestContext.getCurrentInstance().execute("PF('editarFechaHasta').show()");
         }
         if (casilla == 3) {
            RequestContext.getCurrentInstance().update("formDialogos:empleadoDesde");
            RequestContext.getCurrentInstance().execute("PF('empleadoDesde').show()");
         }
         if (casilla == 4) {
            RequestContext.getCurrentInstance().update("formDialogos:empleadoHasta");
            RequestContext.getCurrentInstance().execute("PF('empleadoHasta').show()");
         }
         if (casilla == 5) {
            RequestContext.getCurrentInstance().update("formDialogos:empresa");
            RequestContext.getCurrentInstance().execute("PF('empresa').show()");
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

   //BOTON LISTA VALORES
   public void listaValoresBoton() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (casilla == 3) {
         if ((lovEmpleados == null) || lovEmpleados.isEmpty()) {
            lovEmpleados = null;
         }
         RequestContext.getCurrentInstance().update("formDialogos:EmpleadoDesdeDialogo");
         RequestContext.getCurrentInstance().execute("PF('EmpleadoDesdeDialogo').show()");
         contarRegistrosEmpeladoD();
      }
      if (casilla == 4) {
         RequestContext.getCurrentInstance().update("formDialogos:EmpleadoHastaDialogo");
         RequestContext.getCurrentInstance().execute("PF('EmpleadoHastaDialogo').show()");
         contarRegistrosEmpeladoH();
      }
      if (casilla == 5) {
         RequestContext.getCurrentInstance().update("formDialogos:EmpresaDialogo");
         RequestContext.getCurrentInstance().execute("PF('EmpresaDialogo').show()");
         contarRegistrosEmpresa();
      }
   }

   public void activarCtrlF11() {
      log.info(this.getClass().getName() + ".activarCtrlF11()");
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 0) {
         log.info("Ingrese if");
         altoTabla = "180";
         codigoIR = (Column) c.getViewRoot().findComponent("form:reportesCapacitacion:codigoIR");
         codigoIR.setFilterStyle("width: 85% !important");
         reporteIR = (Column) c.getViewRoot().findComponent("form:reportesCapacitacion:reporteIR");
         reporteIR.setFilterStyle("width: 85% !important");
         RequestContext.getCurrentInstance().update("form:reportesCapacitacion");
         bandera = 1;
      } else if (bandera == 1) {
         log.info("Ingrese else if");
         cerrarFiltrado();
         defaultPropiedadesParametrosReporte();
      }
   }

   public void cerrarFiltrado() {
      FacesContext c = FacesContext.getCurrentInstance();
      log.info("Desactivar");
      altoTabla = "200";
      codigoIR = (Column) c.getViewRoot().findComponent("form:reportesCapacitacion:codigoIR");
      codigoIR.setFilterStyle("display: none; visibility: hidden;");
      reporteIR = (Column) c.getViewRoot().findComponent("form:reportesCapacitacion:reporteIR");
      reporteIR.setFilterStyle("display: none; visibility: hidden;");
      RequestContext.getCurrentInstance().update("form:reportesCapacitacion");
      bandera = 0;
      tipoLista = 0;
      filtrarListIRU = null;
   }

   public void guardarYSalir() {
      log.info(this.getClass().getName() + ".guardarYSalir()");
      guardarCambios();
      salir();
   }

   public void salir() {
      limpiarListasValor();
      log.info(this.getClass().getName() + ".salir()");
      if (bandera == 1) {
         cerrarFiltrado();
      }
      listaIR = null;
      parametroDeReporte = null;
      parametroModificacion = null;
      lovEmpleados = null;
      lovEmpresas = null;
      casilla = -1;
      listaInfoReportesModificados.clear();
      cambiosReporte = true;
      reporteSeleccionado = null;
      reporteSeleccionadoLOV = null;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      navegar("atras");
   }

   public void cancelarModificaciones() {
      log.info(this.getClass().getName() + ".cancelarModificaciones()");
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
      getParametroDeReporte();
      getListaIR();
      activoMostrarTodos = true;
      activoBuscarReporte = false;
      reporteSeleccionado = null;
      RequestContext.getCurrentInstance().update("form:MOSTRARTODOS");
      RequestContext.getCurrentInstance().update("form:BUSCARREPORTE");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      RequestContext.getCurrentInstance().update("form:ENVIOCORREO");
      RequestContext.getCurrentInstance().update("form:reportesCapacitacion");
      RequestContext.getCurrentInstance().update("formParametros:fechaDesdeParametro");
      RequestContext.getCurrentInstance().update("formParametros:fechaHastaParametro");
      RequestContext.getCurrentInstance().update("formParametros:empleadoDesdeParametro");
      RequestContext.getCurrentInstance().update("formParametros:empleadoHastaParametro");
//        RequestContext.getCurrentInstance().update("formParametros:estadoParametro");
//        RequestContext.getCurrentInstance().update("formParametros:empresaParametro");
   }

   public void cancelarYSalir() {
//      log.info(this.getClass().getName() + ".cancelarYSalir()");
//      cancelarModificaciones();
      salir();
   }

   //EVENTO FILTRAR
   public void eventoFiltrar() {
      contarRegistros();
   }

   public void activarEnvioCorreo() {
      if (reporteSeleccionado != null) {
         activarEnvio = false;
      } else {
         activarEnvio = true;
      }
      RequestContext.getCurrentInstance().update("form:ENVIOCORREO");
   }

   public void activarAceptar() {
      log.info(this.getClass().getName() + ".activarAceptar()");
      aceptar = false;
   }

   //METODOS GENERACION REPORTES
   public void defaultPropiedadesParametrosReporte() {
      log.info(this.getClass().getName() + ".defaultPropiedadesParametrosReporte()");
      color = "black";
      decoracion = "none";
      color2 = "black";
      decoracion2 = "none";
   }

   public void seleccionRegistro() {
      try {
         activarEnvioCorreo();
         defaultPropiedadesParametrosReporte();
         if (reporteSeleccionado.getFecdesde().equals("SI")) {
            color = "red";
            RequestContext.getCurrentInstance().update("formParametros");
         }
         log.info("reporteSeleccionado.getFechasta(): " + reporteSeleccionado.getFechasta());
         if (reporteSeleccionado.getFechasta().equals("SI")) {
            color2 = "red";
            RequestContext.getCurrentInstance().update("formParametros");
         }
         log.info("reporteSeleccionado.getEmdesde(): " + reporteSeleccionado.getEmdesde());
         if (reporteSeleccionado.getEmdesde().equals("SI")) {
            empleadoDesdeParametro = (InputText) FacesContext.getCurrentInstance().getViewRoot().findComponent("formParametros:empleadoDesdeParametro");
            //empleadoDesdeParametro.setStyle("position: absolute; top: 41px; left: 150px; height: 10px; font-size: 11px; width: 90px; color: red;");
            if (!empleadoDesdeParametro.getStyle().contains(" color: red;")) {
               empleadoDesdeParametro.setStyle(empleadoDesdeParametro.getStyle() + " color: red;");
            }
         } else {
            try {
               log.info("empleadoDesdeParametro: " + empleadoDesdeParametro);
               if (empleadoDesdeParametro.getStyle().contains(" color: red;")) {

                  log.info("reeemplazarr " + empleadoDesdeParametro.getStyle().replace(" color: red;", ""));
                  empleadoDesdeParametro.setStyle(empleadoDesdeParametro.getStyle().replace(" color: red;", ""));
               }
            } catch (Exception e) {
               e.printStackTrace();
            }
         }
         RequestContext.getCurrentInstance().update("formParametros:empleadoDesdeParametro");

         log.info("reporteSeleccionado.getEmhasta(): " + reporteSeleccionado.getEmhasta());
         if (reporteSeleccionado.getEmhasta().equals("SI")) {
            empleadoHastaParametro = (InputText) FacesContext.getCurrentInstance().getViewRoot().findComponent("formParametros:empleadoHastaParametro");
            //empleadoHastaParametro.setStyle("position: absolute; top: 41px; left: 330px; height: 10px; font-size: 11px; width: 90px; color: red;");
            empleadoHastaParametro.setStyle(empleadoHastaParametro.getStyle() + "color: red;");
            RequestContext.getCurrentInstance().update("formParametros:empleadoHastaParametro");
         }
         RequestContext.getCurrentInstance().update("formParametros");
      } catch (Exception ex) {
         log.warn("Error en selecccion Registro : " + ex.getMessage());
      }
   }

   public void requisitosParaReporte() {
      log.info(this.getClass().getName() + ".requisitosParaReporte()");
      RequestContext context = RequestContext.getCurrentInstance();
      requisitosReporte = "";
      if (reporteSeleccionado.getFecdesde().equals("SI")) {
         requisitosReporte = requisitosReporte + "- Fecha Desde -";
      }
      if (reporteSeleccionado.getFechasta().equals("SI")) {
         requisitosReporte = requisitosReporte + "- Fecha Hasta -";
      }
      if (reporteSeleccionado.getEmdesde().equals("SI")) {
         requisitosReporte = requisitosReporte + "- Empleado Desde -";
      }
      if (reporteSeleccionado.getEmhasta().equals("SI")) {
         requisitosReporte = requisitosReporte + "- Empleado Hasta -";
      }
      if (!requisitosReporte.isEmpty()) {
         RequestContext.getCurrentInstance().update("formDialogos:requisitosReporte");
         RequestContext.getCurrentInstance().execute("PF('requisitosReporte').show()");
      }
   }

   public void cancelarRequisitosReporte() {
      log.info(this.getClass().getName() + ".cancelarRequisitosReporte()");
      requisitosReporte = "";
   }

   public void modificacionTipoReporte(Inforeportes reporte) {
      log.info(this.getClass().getName() + ".modificacionTipoReporte()");
      reporteSeleccionado = reporte;
      cambiosReporte = false;
      if (listaInfoReportesModificados.isEmpty()) {
         listaInfoReportesModificados.add(reporteSeleccionado);
      } else if ((!listaInfoReportesModificados.isEmpty()) && (!listaInfoReportesModificados.contains(reporteSeleccionado))) {
         listaInfoReportesModificados.add(reporteSeleccionado);
      } else {
         int posicion = listaInfoReportesModificados.indexOf(reporteSeleccionado);
         listaInfoReportesModificados.set(posicion, reporteSeleccionado);
      }
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void generarReporte(Inforeportes reporte) {
      try {
         guardarCambios();
         reporteSeleccionado = reporte;
         seleccionRegistro();
         generarDocumentoReporte();
      } catch (Exception e) {
         log.warn("Error en generarReporte : " + e.getMessage());
         RequestContext.getCurrentInstance().execute("PF('generandoReporte').hide()");
         RequestContext.getCurrentInstance().update("formDialogos:errorGenerandoReporte");
         RequestContext.getCurrentInstance().execute("PF('errorGenerandoReporte').show()");
      }
   }

   public AsynchronousFilllListener listener() {
      log.info(this.getClass().getName() + ".listener()");
      return new AsynchronousFilllListener() {
         //RequestContext context = c;

         @Override
         public void reportFinished(JasperPrint jp) {
            log.info(this.getClass().getName() + ".listener().reportFinished()");
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
               log.info("ControlNReporteCapacitacion reportFinished ERROR: " + e.toString());
            }
         }

         @Override
         public void reportCancelled() {
            log.info(this.getClass().getName() + ".listener().reportCancelled()");
            estadoReporte = true;
            resultadoReporte = "Cancelación";
         }

         @Override
         public void reportFillError(Throwable e) {
            log.info(this.getClass().getName() + ".listener().reportFillError()");
            if (e.getCause() != null) {
               pathReporteGenerado = "ControlNReporteCapacitacion reportFillError Error: " + e.toString() + "\n" + e.getCause().toString();
            } else {
               pathReporteGenerado = "ControlNReporteCapacitacion reportFillError Error: " + e.toString();
            }
            estadoReporte = true;
            resultadoReporte = "Se estallo";
         }
      };
   }

   public void exportarReporte() throws IOException {
      try {
         if (pathReporteGenerado != null || pathReporteGenerado.startsWith("Error:")) {
            File reporteF = new File(pathReporteGenerado);
            FacesContext ctx = FacesContext.getCurrentInstance();
            FileInputStream fis = new FileInputStream(reporteF);
            byte[] bytes = new byte[1024];
            int read;
            if (!ctx.getResponseComplete()) {
               String fileName = reporteF.getName();
               HttpServletResponse response = (HttpServletResponse) ctx.getExternalContext().getResponse();
               response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
               ServletOutputStream out = response.getOutputStream();

               while ((read = fis.read(bytes)) != -1) {
                  out.write(bytes, 0, read);
               }
               out.flush();
               out.close();
               ctx.responseComplete();
            }
         }
      } catch (Exception e) {
         log.warn("Error en exportarReporte : " + e.getMessage());
         RequestContext.getCurrentInstance().execute("PF('generandoReporte').hide()");
         RequestContext.getCurrentInstance().update("formDialogos:errorGenerandoReporte");
         RequestContext.getCurrentInstance().execute("PF('errorGenerandoReporte').show()");
      }
   }

   public void generarDocumentoReporte() {
      try {
         if (reporteSeleccionado != null) {
            log.info("generando reporte - ingreso al if");
            nombreReporte = reporteSeleccionado.getNombrereporte();
            tipoReporte = reporteSeleccionado.getTipo();

            if (nombreReporte != null && tipoReporte != null) {
               log.info("generando reporte - ingreso al 2 if");
               pathReporteGenerado = administarReportes.generarReporte(nombreReporte, tipoReporte);
            }
            if (pathReporteGenerado != null) {
               log.info("generando reporte - ingreso al 3 if");
               validarDescargaReporte();
            } else {
               log.info("generando reporte - ingreso al 3 if else");
               RequestContext.getCurrentInstance().execute("PF('generandoReporte').hide()");
               RequestContext.getCurrentInstance().update("formDialogos:errorGenerandoReporte");
               RequestContext.getCurrentInstance().execute("PF('errorGenerandoReporte').show()");
            }
         } else {
            log.info("generando reporte - ingreso al if else");
            log.info("Reporte Seleccionado es nulo");
         }
      } catch (Exception e) {
         log.warn("Error generarDocumentoReprote : " + e.getMessage());
         RequestContext.getCurrentInstance().execute("PF('generandoReporte').hide()");
         RequestContext.getCurrentInstance().update("formDialogos:errorGenerandoReporte");
         RequestContext.getCurrentInstance().execute("PF('errorGenerandoReporte').show()");
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
                  log.info("pathReporteGenerado : " + pathReporteGenerado);
                  fis = new FileInputStream(new File(pathReporteGenerado));
                  log.info("fis : " + fis);
                  reporte = new DefaultStreamedContent(fis, "application/pdf");
               } catch (FileNotFoundException ex) {
                  RequestContext.getCurrentInstance().execute("PF('generandoReporte').hide()");
                  RequestContext.getCurrentInstance().update("formDialogos:errorGenerandoReporte");
                  RequestContext.getCurrentInstance().execute("PF('errorGenerandoReporte').show()");
                  reporte = null;
               }
               if (reporte != null) {
                  if (reporteSeleccionado != null) {
                     log.info("userAgent " + userAgent);
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
               pathReporteGenerado = null;
            }
         } else {
            log.info("validar descarga reporte - ingreso al if 1 else");
            RequestContext.getCurrentInstance().execute("PF('generandoReporte').hide()");
            RequestContext.getCurrentInstance().update("formDialogos:errorGenerandoReporte");
            RequestContext.getCurrentInstance().execute("PF('errorGenerandoReporte').show()");
         }
      } catch (Exception e) {
         log.warn("Error en validarDescargarReporte : " + e.getMessage());
      }

   }

   public void dispararDialogoGuardarCambios() {
      log.info(this.getClass().getName() + ".dispararDialogoGuardarCambios()");
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");

   }

   public void cancelarReporte() {
      log.info(this.getClass().getName() + ".cancelarReporte()");
      administarReportes.cancelarReporte();
   }

   public void reiniciarStreamedContent() {
      log.info(this.getClass().getName() + ".reiniciarStreamedContent()");
      reporte = null;
   }

   public void mostrarDialogoBuscarReporte() {
      log.info(this.getClass().getName() + ".mostrarDialogoBuscarReporte()");
      try {
         if (cambiosReporte == true) {
            contarRegistrosLovReportes();
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formDialogos:ReportesDialogo");
            RequestContext.getCurrentInstance().execute("PF('ReportesDialogo').show()");
         } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardarSinSalida').show()");
         }
      } catch (Exception e) {
         log.warn("Error mostrarDialogoBuscarReporte : " + e.toString());
      }
   }

   public void mostrarTodos() {
      log.info(this.getClass().getName() + ".mostrarTodos()");
      if (cambiosReporte == true) {
         defaultPropiedadesParametrosReporte();
         listaIR.clear();
         for (int i = 0; i < lovInforeportes.size(); i++) {
            listaIR.add(lovInforeportes.get(i));
         }
         RequestContext context = RequestContext.getCurrentInstance();
         activoBuscarReporte = false;
         activoMostrarTodos = true;
         RequestContext.getCurrentInstance().update("form:MOSTRARTODOS");
         RequestContext.getCurrentInstance().update("form:BUSCARREPORTE");
         RequestContext.getCurrentInstance().update("form:reportesCapacitacion");
      } else {
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().execute("PF('confirmarGuardarSinSalida').show()");
      }
   }

   public void actualizarEmplDesde() {
      log.info(this.getClass().getName() + ".actualizarEmplDesde()");
      permitirIndex = true;
      parametroDeReporte.setCodigoempleadodesde(empleadoSeleccionado.getCodigoempleado());
      parametroModificacion = parametroDeReporte;
      cambiosReporte = false;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formDialogos:lovEmpleadoDesde:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovEmpleadoDesde').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('EmpleadoDesdeDialogo').hide()");

      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      RequestContext.getCurrentInstance().update("formParametros:empleadoDesdeParametro");
      empleadoSeleccionado = null;
      aceptar = true;
      lovEmpleadosFiltrar = null;

   }

   public void cancelarCambioEmplDesde() {
      log.info(this.getClass().getName() + ".cancelarCambioEmplDesde()");
      empleadoSeleccionado = null;
      aceptar = true;
      lovEmpleadosFiltrar = null;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formDialogos:lovEmpleadoDesde:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovEmpleadoDesde').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('EmpleadoDesdeDialogo').hide()");
   }

   public void actualizarEmplHasta() {
      log.info(this.getClass().getName() + ".actualizarEmplHasta()");
      permitirIndex = true;
      parametroDeReporte.setCodigoempleadohasta(empleadoSeleccionado.getCodigoempleado());
      parametroModificacion = parametroDeReporte;
      cambiosReporte = false;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formDialogos:lovEmpleadoHasta:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovEmpleadoHasta').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('EmpleadoHastaDialogo').hide()");

      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      RequestContext.getCurrentInstance().update("formParametros:empleadoHastaParametro");
      empleadoSeleccionado = null;
      aceptar = true;
      lovEmpleadosFiltrar = null;
   }

   public void cancelarCambioEmplHasta() {
      log.info(this.getClass().getName() + ".cancelarCambioEmplHasta()");
      empleadoSeleccionado = null;
      aceptar = true;
      lovEmpleadosFiltrar = null;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("form:lovEmpleadoHasta:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovEmpleadoHasta').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('EmpleadoHastaDialogo').hide()");
   }

   public void actualizarEmpresa() {
      log.info(this.getClass().getName() + ".actualizarEmpresa()");
      permitirIndex = true;
      parametroDeReporte.setEmpresa(empresaSeleccionada);
      parametroModificacion = parametroDeReporte;
      cambiosReporte = false;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formDialogos:lovEmpresa:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovEmpresa').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('EmpresaDialogo').hide()");

      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      RequestContext.getCurrentInstance().update("formParametros:empresaParametro");
      empresaSeleccionada = null;
      aceptar = true;
      lovEmpresasFiltrar = null;

   }

   public void cancelarEmpresa() {
      log.info(this.getClass().getName() + ".cancelarEmpresa()");
      empresaSeleccionada = null;
      aceptar = true;
      lovEmpresasFiltrar = null;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formDialogos:lovEmpresa:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovEmpresa').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('EmpresaDialogo').hide()");
   }

   public void actualizarSeleccionInforeporte() {
      log.info(this.getClass().getName() + ".actualizarSeleccionInforeporte()");
      RequestContext context = RequestContext.getCurrentInstance();
      if (bandera == 1) {
         cerrarFiltrado();
      }
      defaultPropiedadesParametrosReporte();
      listaIR.clear();
      listaIR.add(reporteSeleccionadoLOV);
      filtrarListIRU = null;
      filtrarLovInforeportes = null;
      aceptar = true;
      activoBuscarReporte = true;
      activoMostrarTodos = false;
      reporteSeleccionado = null;
      reporteSeleccionadoLOV = null;
      RequestContext.getCurrentInstance().update("form:MOSTRARTODOS");
      RequestContext.getCurrentInstance().update("form:BUSCARREPORTE");
      context.reset("formDialogos:lovReportesDialogo:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovReportesDialogo').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('ReportesDialogo').hide()");
      RequestContext.getCurrentInstance().update("form:reportesCapacitacion");
      contarRegistros();
   }

   public void cancelarSeleccionInforeporte() {
      log.info(this.getClass().getName() + ".cancelarSeleccionInforeporte()");
      filtrarListIRU = null;
      reporteSeleccionadoLOV = null;
      aceptar = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formDialogos:lovReportesDialogo:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovReportesDialogo').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('ReportesDialogo').hide()");
   }

   //AUTOCOMPLETAR
   public void autocompletarGeneral(String campoConfirmar, String valorConfirmar) {
      int indiceUnicoElemento = -1;
      int coincidencias = 0;
      if (campoConfirmar.equalsIgnoreCase("EMPRESA")) {
         if (!valorConfirmar.isEmpty()) {
            parametroDeReporte.getEmpresa().setNombre(empresa);
            for (int i = 0; i < lovEmpresas.size(); i++) {
               if (lovEmpresas.get(i).getNombre().startsWith(valorConfirmar)) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               parametroDeReporte.setEmpresa(lovEmpresas.get(indiceUnicoElemento));
               parametroModificacion = parametroDeReporte;
               lovEmpresas.clear();
               getLovEmpresas();
               cambiosReporte = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            } else {
               permitirIndex = false;
               RequestContext.getCurrentInstance().update("form:EmpresaDialogo");
               RequestContext.getCurrentInstance().execute("PF('EmpresaDialogo').show()");
            }
         } else {
            parametroDeReporte.setEmpresa(new Empresas());
            parametroModificacion = parametroDeReporte;
            lovEmpresas.clear();
            getLovEmpresas();
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

   //POSICION CELDA
   public void posicionCelda(int i) {
      log.info(this.getClass().getName() + ".posicionCelda()");
      casilla = i;
      if (permitirIndex == true) {
         casillaInforReporte = -1;
         switch (casilla) {
            case 1:
               fechaDesde = parametroDeReporte.getFechadesde();
               break;
            case 2:
               fechaHasta = parametroDeReporte.getFechahasta();
               break;
            case 3:
               emplDesde = parametroDeReporte.getCodigoempleadodesde();
               break;
            case 4:
               emplHasta = parametroDeReporte.getCodigoempleadohasta();
               break;
            case 5:
               empresa = parametroDeReporte.getEmpresa().getNombre();
               break;
            case 6:
               estado = parametroDeReporte.getEstadosolucionnodo();
               break;
            default:
               break;
         }
      }
   }

   //MODIFICAR PARAMETRO REPORTE
   public void modificarParametroInforme() {
      if (parametroDeReporte.getFechadesde() != null && parametroDeReporte.getFechahasta() != null) {
         if (parametroDeReporte.getFechadesde().before(parametroDeReporte.getFechahasta())) {
            log.info("Entre al segundo if");
            parametroModificacion = parametroDeReporte;
            cambiosReporte = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         } else {
            log.info("entre a else del segundo if");
            cambiosReporte = true;
         }
      }
   }

   public void mostrarDialogoGenerarReporte() {
      log.info(this.getClass().getName() + ".mostrarDialogoGenerarReporte()");
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("formDialogos:reporteAGenerar");
      RequestContext.getCurrentInstance().execute("PF('reporteAGenerar').show()");
   }

   public void cancelarGenerarReporte() {
      log.info(this.getClass().getName() + ".cancelarGenerarReporte()");
      reporteGenerar = "";
      posicionReporte = -1;
   }

   //CONTAR REGISTROS
   public void contarRegistros() {
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
   }

   public void contarRegistrosLovReportes() {
      RequestContext.getCurrentInstance().update("formDialogos:infoRegistroReportes");
   }

   public void contarRegistrosEmpeladoD() {
      RequestContext.getCurrentInstance().update("formDialogos:infoRegistroEmpleadoDesde");
   }

   public void contarRegistrosEmpeladoH() {
      RequestContext.getCurrentInstance().update("formDialogos:infoRegistroEmpleadoHasta");
   }

   public void contarRegistrosEmpresa() {
      RequestContext.getCurrentInstance().update("formDialogos:infoRegistroEmpresa");
   }
//
//    private static abstract class FacesContextWrapper extends FacesContext {
//
//        protected static void setCurrentInstance(FacesContext facesContext) {
//            FacesContext.setCurrentInstance(facesContext);
//        }
//    }

   public void mostrarDialogoNuevaFecha() {
      getParametroDeReporte();
      if (parametroDeReporte.getFechadesde() == null && parametroDeReporte.getFechahasta() == null) {
         RequestContext.getCurrentInstance().update("formDialogos:nuevoRegistroFechas");
         RequestContext.getCurrentInstance().execute("PF('nuevoRegistroFechas').show()");
      }
   }

   public void agregarFecha() {
      int contador = 0;
      if (parametroFecha.getFechadesde() == null) {
         contador++;
      }
      if (parametroFecha.getFechahasta() == null) {
         contador++;
      }

      if (contador == 0) {
         parametroDeReporte.setFechadesde(parametroFecha.getFechadesde());
         parametroDeReporte.setFechahasta(parametroFecha.getFechahasta());
         RequestContext.getCurrentInstance().update("formParametros:fechaDesdeParametro");
         RequestContext.getCurrentInstance().update("formParametros:fechaHastaParametro");
         aceptar = false;
         RequestContext.getCurrentInstance().execute("form:ACEPTAR");
         RequestContext.getCurrentInstance().execute("PF('nuevoRegistroFechas').hide()");
      } else {
         RequestContext.getCurrentInstance().update("formDialogos:validacionRegistroFechas");
         RequestContext.getCurrentInstance().execute("PF('validacionRegistroFechas').show()");
      }

   }

   public void limpiarNuevaFecha() {
      parametroFecha.setFechadesde(null);
      parametroFecha.setFechahasta(null);
   }

   //GETTER AND SETTER
   public ParametrosReportes getParametroDeReporte() {
      try {
         if (parametroDeReporte == null) {
            parametroDeReporte = new ParametrosReportes();
            parametroDeReporte = administrarNReporteCapacitacion.parametrosDeReporte();
         }
         if (parametroDeReporte.getEmpresa() == null) {
            parametroDeReporte.setEmpresa(new Empresas());
         }

         return parametroDeReporte;
      } catch (Exception e) {
         log.warn("Error getParametroDeInforme : " + e);
         return null;
      }
   }

   public void setParametroDeReporte(ParametrosReportes parametroDeReporte) {
      this.parametroDeReporte = parametroDeReporte;
   }

   public ParametrosReportes getParametroModificacion() {
      return parametroModificacion;
   }

   public void setParametroModificacion(ParametrosReportes parametroModificacion) {
      this.parametroModificacion = parametroModificacion;
   }

   public List<Inforeportes> getListaIR() {
      try {
         if (listaIR == null) {
            listaIR = administrarNReporteCapacitacion.listInforeportesUsuario();
         }
         return listaIR;
      } catch (Exception e) {
         log.warn("Error getListInforeportesUsuario : " + e);
         return null;
      }
   }

   public void setListaIR(List<Inforeportes> listaIR) {
      this.listaIR = listaIR;
   }

   public Inforeportes getReporteSeleccionado() {
      return reporteSeleccionado;
   }

   public void setReporteSeleccionado(Inforeportes reporteSeleccionado) {
      this.reporteSeleccionado = reporteSeleccionado;
   }

   public List<Inforeportes> getFiltrarListIRU() {
      return filtrarListIRU;
   }

   public void setFiltrarListIRU(List<Inforeportes> filtrarListIRU) {
      this.filtrarListIRU = filtrarListIRU;
   }

   public List<Inforeportes> getListaInfoReportesModificados() {
      return listaInfoReportesModificados;
   }

   public void setListaInfoReportesModificados(List<Inforeportes> listaInfoReportesModificados) {
      this.listaInfoReportesModificados = listaInfoReportesModificados;
   }

   public List<Inforeportes> getLovInforeportes() {
      return lovInforeportes;
   }

   public void setLovInforeportes(List<Inforeportes> lovInforeportes) {
      this.lovInforeportes = lovInforeportes;
   }

   public Inforeportes getReporteSeleccionadoLOV() {
      return reporteSeleccionadoLOV;
   }

   public void setReporteSeleccionadoLOV(Inforeportes reporteSeleccionadoLOV) {
      this.reporteSeleccionadoLOV = reporteSeleccionadoLOV;
   }

   public List<Inforeportes> getFiltrarLovInforeportes() {
      return filtrarLovInforeportes;
   }

   public void setFiltrarLovInforeportes(List<Inforeportes> filtrarLovInforeportes) {
      this.filtrarLovInforeportes = filtrarLovInforeportes;
   }

   public List<Inforeportes> getFiltrarReportes() {
      return filtrarReportes;
   }

   public void setFiltrarReportes(List<Inforeportes> filtrarReportes) {
      this.filtrarReportes = filtrarReportes;
   }

   public List<Empleados> getLovEmpleados() {
      if (lovEmpleados == null) {
         if (listasRecurrentes.getLovEmpleadosActivos().isEmpty()) {
            lovEmpleados = administrarNReporteCapacitacion.listEmpleados();
            if (lovEmpleados != null) {
               log.warn("GUARDANDO lovEmpleadosActivos en Listas recurrentes");
               listasRecurrentes.setLovEmpleadosActivos(lovEmpleados);
            }
         } else {
            lovEmpleados = new ArrayList<Empleados>(listasRecurrentes.getLovEmpleadosActivos());
            log.warn("CONSULTANDO lovEmpleadosActivos de Listas recurrentes");
         }
      }
      return lovEmpleados;
   }

   public void setLovEmpleados(List<Empleados> lovEmpleados) {
      this.lovEmpleados = lovEmpleados;
   }

   public Empleados getEmpleadoSeleccionado() {
      return empleadoSeleccionado;
   }

   public void setEmpleadoSeleccionado(Empleados empleadoSeleccionado) {
      this.empleadoSeleccionado = empleadoSeleccionado;
   }

   public List<Empleados> getLovEmpleadosFiltrar() {
      return lovEmpleadosFiltrar;
   }

   public void setLovEmpleadosFiltrar(List<Empleados> lovEmpleadosFiltrar) {
      this.lovEmpleadosFiltrar = lovEmpleadosFiltrar;
   }

   public List<Empresas> getLovEmpresas() {
      if (lovEmpresas == null || lovEmpresas.isEmpty()) {
         lovEmpresas = administrarNReporteCapacitacion.listEmpresas();
      }
      return lovEmpresas;
   }

   public void setLovEmpresas(List<Empresas> lovEmpresas) {
      this.lovEmpresas = lovEmpresas;
   }

   public Empresas getEmpresaSeleccionada() {
      return empresaSeleccionada;
   }

   public void setEmpresaSeleccionada(Empresas empresaSeleccionada) {
      this.empresaSeleccionada = empresaSeleccionada;
   }

   public List<Empresas> getLovEmpresasFiltrar() {
      return lovEmpresasFiltrar;
   }

   public void setLovEmpresasFiltrar(List<Empresas> lovEmpresasFiltrar) {
      this.lovEmpresasFiltrar = lovEmpresasFiltrar;
   }

   public boolean isAceptar() {
      return aceptar;
   }

   public void setAceptar(boolean aceptar) {
      this.aceptar = aceptar;
   }

   public String getAltoTabla() {
      return altoTabla;
   }

   public void setAltoTabla(String altoTabla) {
      this.altoTabla = altoTabla;
   }

   public String getPathReporteGenerado() {
      return pathReporteGenerado;
   }

   public void setPathReporteGenerado(String pathReporteGenerado) {
      this.pathReporteGenerado = pathReporteGenerado;
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

   public StreamedContent getReporte() {
      return reporte;
   }

   public void setReporte(StreamedContent reporte) {
      this.reporte = reporte;
   }

   public String getInfoRegistro() {
      FacesContext c = FacesContext.getCurrentInstance();
      tabla = (DataTable) c.getViewRoot().findComponent("form:reportesCapacitacion");
      infoRegistro = String.valueOf(tabla.getRowCount());
      return infoRegistro;
   }

   public String getInfoRegistroReportes() {
      FacesContext c = FacesContext.getCurrentInstance();
      tabla = (DataTable) c.getViewRoot().findComponent("formDialogos:lovReportesDialogo");
      infoRegistroLovReportes = String.valueOf(tabla.getRowCount());
      return infoRegistroLovReportes;
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

   public String getInfoRegistroEmpresa() {
      FacesContext c = FacesContext.getCurrentInstance();
      tabla = (DataTable) c.getViewRoot().findComponent("formDialogos:lovEmpresa");
      infoRegistroEmpresa = String.valueOf(tabla.getRowCount());
      return infoRegistroEmpresa;
   }

   public void setInfoRegistroEmpresa(String infoRegistroEmpresa) {
      this.infoRegistroEmpresa = infoRegistroEmpresa;
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

   public boolean isCambiosReporte() {
      return cambiosReporte;
   }

   public void setCambiosReporte(boolean cambiosReporte) {
      this.cambiosReporte = cambiosReporte;
   }

   public String getCabezeraVisor() {
      return cabezeraVisor;
   }

   public void setCabezeraVisor(String cabezeraVisor) {
      this.cabezeraVisor = cabezeraVisor;
   }

   public ParametrosReportes getParametroFecha() {
      return parametroFecha;
   }

   public void setParametroFecha(ParametrosReportes parametroFecha) {
      this.parametroFecha = parametroFecha;
   }

}
