/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Empleados;
import Entidades.Empresas;
import Entidades.Estructuras;
import Entidades.EvalEvaluadores;
import Entidades.Evalconvocatorias;
import Entidades.Evalplanillas;
import Entidades.Inforeportes;
import Entidades.ParametrosReportes;
import InterfaceAdministrar.AdministarReportesInterface;
import InterfaceAdministrar.AdministrarNReporteEvalCompetenciaInterface;
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
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author user
 */
@ManagedBean
@SessionScoped
public class controlNReporteEvalCompetencia implements Serializable {

   private static Logger log = Logger.getLogger(controlNReporteEvalCompetencia.class);

   @EJB
   AdministrarNReporteEvalCompetenciaInterface administrarNReporteEvalCompetencia;
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
   private List<Empleados> filtrarListEmpleados;
   //EMPRESAS
   private List<Empresas> lovEmpresas;
   private Empresas empresaSeleccionada;
   private List<Empresas> filtrarListEmpresas;
   //ESTRUCTURAS
   private List<Estructuras> lovEstructuras;
   private Estructuras estructuraSeleccionada;
   private List<Estructuras> filtrarListEstructuras;
   //PLANILLA
   private List<Evalplanillas> lovPlanilla;
   private Evalplanillas planillaSeleccionada;
   private List<Evalplanillas> filtrarListPlanilla;
   //CONVOCATORIA
   private List<Evalconvocatorias> lovConvocatoria;
   private Evalconvocatorias convocatoriaSeleccionada;
   private List<Evalconvocatorias> filtrarListConvocatoria;
   //EVALUADOR
   private List<EvalEvaluadores> lovEvaluador;
   private EvalEvaluadores evaluadorSeleccionado;
   private List<EvalEvaluadores> filtrarListEvaluador;
   //COLUMNS
   private Column codigoIR, reporteIR;
   //GENERAR    
   private String reporteGenerar;
   //OTROS
   private int bandera;
   private boolean aceptar;
   private int casilla;
   private int posicionReporte;
   private String requisitosReporte;
   private String estructura, empresa, planilla, convocatoria, evaluador;
   private boolean permitirIndex, cambiosReporte;
   private String color, decoracion;
   private String color2, decoracion2;
   private int casillaInforReporte;
   //INPUT    
   private InputText empleadoDesdeParametro, empleadoHastaParametro, estructuraParametro, planillaParametro;
   private InputText empresaParametro, convocatoriaParametro, evaluadorParametro;
   //ALTO SCROLL TABLA
   private String altoTabla;
   //EXPORTAR REPORTE
   private String pathReporteGenerado;
   private String nombreReporte, tipoReporte;
   //DATE
   private Date fechaConvocatoria;
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
   private String infoRegistroEmpleadoDesde, infoRegistroEmpleadoHasta, infoRegistroPlanilla, infoRegistroConvocatoria, infoRegistroEstructura;
   private String infoRegistroLovReportes, infoRegistro, infoRegistroEvaluador, infoRegistroEmpresa;
   //para Recordar
   private DataTable tabla;
   private int tipoLista;
   private String paginaAnterior;
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

   private ListasRecurrentes listasRecurrentes;

   public controlNReporteEvalCompetencia() {
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
      casillaInforReporte = -1;
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
      lovEstructuras = null;
      lovConvocatoria = null;
      lovEvaluador = null;
      lovPlanilla = null;
      tipoLista = 0;
      empleadoSeleccionado = new Empleados();
      empresaSeleccionada = new Empresas();
      planillaSeleccionada = new Evalplanillas();
      convocatoriaSeleccionada = new Evalconvocatorias();
      estructuraSeleccionada = new Estructuras();
      evaluadorSeleccionado = new EvalEvaluadores();
      permitirIndex = true;
      altoTabla = "140";
      cabezeraVisor = null;
      estadoReporte = false;
      log.info(this.getClass().getName() + " fin del Constructor()");
      paginaAnterior = "nominaf";
      mapParametros.put("paginaAnterior", paginaAnterior);
   }

   public void limpiarListasValor() {
      lovConvocatoria = null;
      lovEmpleados = null;
      lovEmpresas = null;
      lovEstructuras = null;
      lovEvaluador = null;
      lovInforeportes = null;
      lovPlanilla = null;
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
         administarReportes.obtenerConexion(ses.getId());
         administrarNReporteEvalCompetencia.obtenerConexion(ses.getId());
         log.info(this.getClass().getName() + " fin de iniciarAdministradores()");
      } catch (Exception e) {
         log.error("Error postconstruct controlNReporteEvalCompetencia" + e);
         log.info("Causa: " + e.getMessage());
      }
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
      String pagActual = "nreporteevalcompetencias";
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

   public void iniciarPagina() {
      log.info(this.getClass().getName() + ".iniciarPagina()");
      activoMostrarTodos = true;
      activoBuscarReporte = false;
      activarEnvio = true;
      getListaIR();
   }

//TOOLTIP
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
               if (parametroDeReporte.getLocalizacion().getSecuencia() == null) {
                  parametroDeReporte.setLocalizacion(null);
               }
               if (parametroDeReporte.getConvocatoria().getSecuencia() == null) {
                  parametroDeReporte.setConvocatoria(null);
               }
               if (parametroDeReporte.getEvalplanilla().getSecuencia() == null) {
                  parametroDeReporte.setEvalplanilla(null);
               }
               administrarNReporteEvalCompetencia.modificarParametrosReportes(parametroDeReporte);
            }
            if (!listaInfoReportesModificados.isEmpty()) {
               administrarNReporteEvalCompetencia.guardarCambiosInfoReportes(listaInfoReportesModificados);
            }
            cambiosReporte = true;
            FacesMessage msg = new FacesMessage("Información", "Los datos se guardaron con Éxito.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         } else {
            parametroDeReporte.setFechaconvocatorias(fechaConvocatoria);
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

   public void editarCelda() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (casilla >= 1) {
         switch (casilla) {
            case 1:
               RequestContext.getCurrentInstance().update("formDialogos:editarFechaConvocatoria");
               RequestContext.getCurrentInstance().execute("PF('editarFechaConvocatoria').show()");
               break;
            case 2:
               RequestContext.getCurrentInstance().update("formDialogos:editarEmpleadoDesde");
               RequestContext.getCurrentInstance().execute("PF('editarEmpleadoDesde').show()");
               break;
            case 3:
               RequestContext.getCurrentInstance().update("formDialogos:editarEmpleadoHasta");
               RequestContext.getCurrentInstance().execute("PF('editarEmpleadoHasta').show()");
               break;
            case 4:
               RequestContext.getCurrentInstance().update("formDialogos:editarPlanilla");
               RequestContext.getCurrentInstance().execute("PF('editarPlanilla').show()");
               break;
            case 5:
               RequestContext.getCurrentInstance().update("formDialogos:editarEstructura");
               RequestContext.getCurrentInstance().execute("PF('editarEstructura').show()");
               break;
            case 6:
               RequestContext.getCurrentInstance().update("formDialogos:editarConvocatoria");
               RequestContext.getCurrentInstance().execute("PF('editarConvocatoria').show()");
               break;
            case 7:
               RequestContext.getCurrentInstance().update("formDialogos:editarEvaluador");
               RequestContext.getCurrentInstance().execute("PF('editarEvaluador').show()");
               break;
            case 8:
               RequestContext.getCurrentInstance().update("formDialogos:editarEmpresa");
               RequestContext.getCurrentInstance().execute("PF('editarEmpresa').show()");
               break;
            default:
               break;
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

   public void listaValoresBoton() {
      RequestContext context = RequestContext.getCurrentInstance();
      switch (casilla) {
         case 2:
            if ((lovEmpleados == null) || lovEmpleados.isEmpty()) {
               lovEmpleados = null;
            }
            RequestContext.getCurrentInstance().update("formDialogos:empleadoDesdeDialogo");
            RequestContext.getCurrentInstance().execute("PF('empleadoDesdeDialogo').show()");
            contarRegistrosEmpeladoD();
            break;
         case 3:
            RequestContext.getCurrentInstance().update("formDialogos:empleadoHastaDialogo");
            RequestContext.getCurrentInstance().execute("PF('empleadoHastaDialogo').show()");
            contarRegistrosEmpeladoH();
            break;
         case 4:
            RequestContext.getCurrentInstance().update("formDialogos:planillaDialogo");
            RequestContext.getCurrentInstance().execute("PF('planillaDialogo').show()");
            contarRegistrosPlanilla();
            break;
         case 5:
            RequestContext.getCurrentInstance().update("formDialogos:estructuraDialogo");
            RequestContext.getCurrentInstance().execute("PF('estructuraDialogo').show()");
            contarRegistrosEstructura();
            break;
         case 6:
            RequestContext.getCurrentInstance().update("formDialogos:convocatoriaDialogo");
            RequestContext.getCurrentInstance().execute("PF('convocatoriaDialogo').show()");
            contarRegistrosConvocatoria();
            break;
         case 7:
            RequestContext.getCurrentInstance().update("formDialogos:evaluadorDialogo");
            RequestContext.getCurrentInstance().execute("PF('evaluadorDialogo').show()");
            contarRegistrosEvaluador();
            break;
         case 8:
            RequestContext.getCurrentInstance().update("formDialogos:empresaDialogo");
            RequestContext.getCurrentInstance().execute("PF('empresaDialogo').show()");
            contarRegistrosEmpresa();
            break;
         default:
            break;
      }
   }

   public void activarCtrlF11() {
      log.info(this.getClass().getName() + ".activarCtrlF11()");
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 0) {
         altoTabla = "120";
         codigoIR = (Column) c.getViewRoot().findComponent("form:reportesEvalCompetencia:codigoIR");
         codigoIR.setFilterStyle("width: 85% !important");
         reporteIR = (Column) c.getViewRoot().findComponent("form:reportesEvalCompetencia:reporteIR");
         reporteIR.setFilterStyle("width: 85% !important");
         RequestContext.getCurrentInstance().update("form:reportesEvalCompetencia");
         bandera = 1;
      } else if (bandera == 1) {
         cerrarFiltrado();
         defaultPropiedadesParametrosReporte();
      }
   }

   public void cerrarFiltrado() {
      FacesContext c = FacesContext.getCurrentInstance();
      log.info("Desactivar");
      altoTabla = "140";
      codigoIR = (Column) c.getViewRoot().findComponent("form:reportesEvalCompetencia:codigoIR");
      codigoIR.setFilterStyle("display: none; visibility: hidden;");
      reporteIR = (Column) c.getViewRoot().findComponent("form:reportesEvalCompetencia:reporteIR");
      reporteIR.setFilterStyle("display: none; visibility: hidden;");
      RequestContext.getCurrentInstance().update("form:reportesEvalCompetencia");
      bandera = 0;
      tipoLista = 0;
      filtrarListIRU = null;
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
      lovEstructuras = null;
      lovConvocatoria = null;
      lovPlanilla = null;
      lovEvaluador = null;
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
      getParametroDeInforme();
      getListaIR();
      activoMostrarTodos = true;
      activoBuscarReporte = false;
      reporteSeleccionado = null;
      RequestContext.getCurrentInstance().update("form:MOSTRARTODOS");
      RequestContext.getCurrentInstance().update("form:BUSCARREPORTE");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      RequestContext.getCurrentInstance().update("form:ENVIOCORREO");
      RequestContext.getCurrentInstance().update("form:reportesEvalCompetencia");
      RequestContext.getCurrentInstance().update("formParametros:fechaConvocatoriaParametro");
      RequestContext.getCurrentInstance().update("formParametros:empleadoDesdeParametro");
      RequestContext.getCurrentInstance().update("formParametros:empleadoHastaParametro");
      RequestContext.getCurrentInstance().update("formParametros:planillaParametro");
      RequestContext.getCurrentInstance().update("formParametros:estructuraParametro");
      RequestContext.getCurrentInstance().update("formParametros:convocatoriaParametro");
      RequestContext.getCurrentInstance().update("formParametros:evaluadorParametro");
      RequestContext.getCurrentInstance().update("formParametros:empresaParametro");
   }

   public void cancelarYSalir() {
      log.info(this.getClass().getName() + ".cancelarYSalir()");
      cancelarModificaciones();
      salir();
   }

   //EVENTO FILTRAR
   public void eventoFiltrar() {
      log.info("Estoy Controlador.ControlNReporteNomina.eventoFiltrar()");
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

   public void seleccionRegistro() {
      RequestContext context = RequestContext.getCurrentInstance();
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
      log.info("reporteSeleccionado.getLocalizacion(): " + reporteSeleccionado.getLocalizacion());
      if (reporteSeleccionado.getLocalizacion().equals("SI")) {
         estructuraParametro = (InputText) FacesContext.getCurrentInstance().getViewRoot().findComponent("formParametros:estructuraParametro");
         //estructuraParametro.setStyle("position: absolute; top: 20px; left: 625px;height: 10px; font-size: 11px;width: 180px; color: red;");
         estructuraParametro.setStyle(estructuraParametro.getStyle() + "color: red;");
         RequestContext.getCurrentInstance().update("formParametros:estructuraParametro");
      }
      RequestContext.getCurrentInstance().update("formParametros");
      // RequestContext.getCurrentInstance().update("form:reportesEvalCompetencia");
   }

   public void requisitosParaReporte() {
      log.info(this.getClass().getName() + ".requisitosParaReporte()");
      RequestContext context = RequestContext.getCurrentInstance();
      requisitosReporte = "";
      if (reporteSeleccionado.getFecdesde().equals("SI")) {
         requisitosReporte = requisitosReporte + "- Fecha Desde -";
      }
      if (reporteSeleccionado.getEmdesde().equals("SI")) {
         requisitosReporte = requisitosReporte + "- Empleado Desde -";
      }
      if (reporteSeleccionado.getEmhasta().equals("SI")) {
         requisitosReporte = requisitosReporte + "- Empleado Hasta -";
      }
      if (reporteSeleccionado.getLocalizacion().equals("SI")) {
         requisitosReporte = requisitosReporte + "- Estructura -";
      }
      if (!requisitosReporte.isEmpty()) {
         RequestContext.getCurrentInstance().update("formDialogos:requisitosReporte");
         RequestContext.getCurrentInstance().execute("PF('requisitosReporte').show()");
      }
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
      log.info(this.getClass().getName() + ".generarReporte()");
      guardarCambios();
      reporteSeleccionado = reporte;
      seleccionRegistro();
//        RequestContext.getCurrentInstance().execute("PF('generandoReporte').show()");
      generarDocumentoReporte();
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
               log.info("ControlNReporteNomina reportFinished ERROR: " + e.toString());
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
               pathReporteGenerado = "ControlNReportePersonal reportFillError Error: " + e.toString() + "\n" + e.getCause().toString();
            } else {
               pathReporteGenerado = "ControlNReportePersonal reportFillError Error: " + e.toString();
            }
            estadoReporte = true;
            resultadoReporte = "Se estallo";
         }
      };
   }

   public void exportarReporte() throws IOException {
      try {
         log.info(this.getClass().getName() + ".exportarReporte()");
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
         log.warn("Error en exportar Reporte : " + e.getMessage());
         RequestContext.getCurrentInstance().update("formDialogos:errorGenerandoReporte");
         RequestContext.getCurrentInstance().execute("PF('errorGenerandoReporte').show()");
      }
   }

   public void validarDescargaReporte() {
      log.info(this.getClass().getName() + ".validarDescargaReporte()");
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().execute("PF('generandoReporte').hide()");
      if (pathReporteGenerado != null && !pathReporteGenerado.startsWith("Error:")) {
         log.info("validar descarga reporte - ingreso al if 1");
         if (!tipoReporte.equals("PDF")) {
            log.info("validar descarga reporte - ingreso al if 2");
            RequestContext.getCurrentInstance().execute("PF('descargarReporte').show()");
         } else {
            log.info("validar descarga reporte - ingreso al if 2 else");
            FileInputStream fis;
            try {
               log.info("pathReporteGenerado : " + pathReporteGenerado);
               fis = new FileInputStream(new File(pathReporteGenerado));
               log.info("fis : " + fis);
               reporte = new DefaultStreamedContent(fis, "application/pdf");
            } catch (FileNotFoundException ex) {
               RequestContext.getCurrentInstance().update("formDialogos:errorGenerandoReporte");
               RequestContext.getCurrentInstance().execute("PF('errorGenerandoReporte').show()");
               reporte = null;
            }
            if (reporte != null) {
               log.info("validar descarga reporte - ingreso al if 3");
               if (reporteSeleccionado != null) {
                  log.info("validar descarga reporte - ingreso al if 4");
                  //cabezeraVisor = "Reporte - " + reporteSeleccionado.getNombre();
                  cabezeraVisor = "Reporte - " + nombreReporte;
               } else {
                  log.info("validar descarga reporte - ingreso al if 4 else ");
                  cabezeraVisor = "Reporte - ";
               }
               RequestContext.getCurrentInstance().update("formDialogos:verReportePDF");
               RequestContext.getCurrentInstance().execute("PF('verReportePDF').show()");
            }
            pathReporteGenerado = null;
         }
      } else {
         log.info("validar descarga reporte - ingreso al if 1 else");
         RequestContext.getCurrentInstance().update("formDialogos:errorGenerandoReporte");
         RequestContext.getCurrentInstance().execute("PF('errorGenerandoReporte').show()");
      }
   }

   public void generarDocumentoReporte() {
      RequestContext context = RequestContext.getCurrentInstance();
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
         RequestContext.getCurrentInstance().update("formDialogos:errorGenerandoReporte");
         RequestContext.getCurrentInstance().execute("PF('errorGenerandoReporte').show()");
         log.info("generando reporte - ingreso al if else");
         log.info("Reporte Seleccionado es nulo");
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

   public void defaultPropiedadesParametrosReporte() {
      log.info(this.getClass().getName() + ".defaultPropiedadesParametrosReporte()");
      color = "black";
      decoracion = "none";
      color2 = "black";
      decoracion2 = "none";
   }

   public void reiniciarStreamedContent() {
      log.info(this.getClass().getName() + ".reiniciarStreamedContent()");
      reporte = null;
   }

   public void generandoReport() {
      log.info("Controlador.controlNReporteEvalCompetencia.generandoReport()");
      RequestContext.getCurrentInstance().execute("PF('generandoReporte').show()");
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
         RequestContext.getCurrentInstance().update("form:reportesEvalCompetencia");
      } else {
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().execute("PF('confirmarGuardarSinSalida').show()");
      }
   }

   public void cancelarRequisitosReporte() {
      log.info(this.getClass().getName() + ".cancelarRequisitosReporte()");
      requisitosReporte = "";
   }

   public void guardarYSalir() {
      log.info(this.getClass().getName() + ".guardarYSalir()");
      guardarCambios();
      salir();
   }

   public void activarAceptar() {
      log.info(this.getClass().getName() + ".activarAceptar()");
      aceptar = false;
   }

   public void actualizarEmplDesde() {
      log.info(this.getClass().getName() + ".actualizarEmplDesde()");
      permitirIndex = true;
      parametroDeReporte.setCodigoempleadodesde(empleadoSeleccionado.getCodigoempleado());
      parametroModificacion = parametroDeReporte;
      cambiosReporte = false;
      empleadoSeleccionado = null;
      aceptar = true;
      filtrarListEmpleados = null;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formDialogos:lovEmpleadoDesde:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovEmpleadoDesde').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('EmpleadoDesdeDialogo').hide()");

      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      RequestContext.getCurrentInstance().update("formParametros:empleadoDesdeParametro");
   }

   public void cancelarCambioEmplDesde() {
      log.info(this.getClass().getName() + ".cancelarCambioEmplDesde()");
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
      log.info(this.getClass().getName() + ".actualizarEmplHasta()");
      permitirIndex = true;
      parametroDeReporte.setCodigoempleadohasta(empleadoSeleccionado.getCodigoempleado());
      parametroModificacion = parametroDeReporte;
      cambiosReporte = false;
      empleadoSeleccionado = null;
      aceptar = true;
      filtrarListEmpleados = null;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formDialogos:lovEmpleadoHasta:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovEmpleadoHasta').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('EmpleadoHastaDialogo').hide()");

      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      RequestContext.getCurrentInstance().update("formParametros:empleadoHastaParametro");
   }

   public void cancelarCambioEmplHasta() {
      log.info(this.getClass().getName() + ".cancelarCambioEmplHasta()");
      empleadoSeleccionado = null;
      aceptar = true;
      filtrarListEmpleados = null;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formDialogos:lovEmpleadoHasta:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovEmpleadoHasta').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('EmpleadoHastaDialogo').hide()");
   }

   public void actualizarPlanilla() {
      log.info("Controlador.controlNReporteEvalCompetencia.actualizarPlanilla()");
      permitirIndex = true;
      parametroDeReporte.setEvalplanilla(planillaSeleccionada);
      parametroModificacion = parametroDeReporte;
      cambiosReporte = false;
      planillaSeleccionada = null;
      aceptar = true;
      filtrarListPlanilla = null;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formDialogos:lovPlanilla:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovPlanilla').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('PlanillaDialogo').hide()");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      RequestContext.getCurrentInstance().update("formParametros:planillaParametro");
   }

   public void cancelarCambioPlanilla() {
      log.info("Controlador.controlNReporteEvalCompetencia.cancelarCambioPlanilla()");
      planillaSeleccionada = null;
      aceptar = true;
      filtrarListPlanilla = null;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formDialogos:lovPlanilla:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovPlanilla').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('PlanillaDialogo').hide()");
   }

   public void actualizarConvocatoria() {
      log.info("Controlador.controlNReporteEvalCompetencia.actualizarConvocatoria()");
      permitirIndex = true;
      parametroDeReporte.setConvocatoria(convocatoriaSeleccionada);
      parametroModificacion = parametroDeReporte;
      cambiosReporte = false;
      convocatoriaSeleccionada = null;
      aceptar = true;
      filtrarListConvocatoria = null;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formDialogos:lovConvocatoria:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovConvocatoria').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('ConvocatoriaDialogo').hide()");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      RequestContext.getCurrentInstance().update("formParametros:convocatoriaParametro");

   }

   public void cancelarConvocatoria() {
      log.info("Controlador.controlNReporteEvalCompetencia.cancelarConvocatoria()");
      convocatoriaSeleccionada = null;
      aceptar = true;
      filtrarListConvocatoria = null;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formDialogos:lovConvocatoria:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovConvocatoria').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('ConvocatoriaDialogo').hide()");
   }

   public void actualizarEmpresa() {
      log.info("Controlador.controlNReporteEvalCompetencia.actualizarEmpresa()");
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
      filtrarListEmpresas = null;

   }

   public void cancelarEmpresa() {
      log.info(this.getClass().getName() + ".cancelarEmpresa()");
      empresaSeleccionada = null;
      aceptar = true;
      filtrarListEmpresas = null;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formDialogos:lovEmpresa:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovEmpresa').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('EmpresaDialogo').hide()");
   }

   public void actualizarEstructura() {
      log.info(this.getClass().getName() + ".actualizarEstructura()");
      parametroDeReporte.setLocalizacion(estructuraSeleccionada);
      parametroModificacion = parametroDeReporte;
      cambiosReporte = false;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formDialogos:lovEstructura:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovEstructura').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('EstructuraDialogo').hide()");

      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      RequestContext.getCurrentInstance().update("formParametros:estructuraParametro");
      estructuraSeleccionada = null;
      aceptar = true;
      filtrarListEstructuras = null;
      permitirIndex = true;

   }

   public void cancelarEstructura() {
      log.info(this.getClass().getName() + ".cancelarEstructura()");
      estructuraSeleccionada = null;
      aceptar = true;
      filtrarListEstructuras = null;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formDialogos:lovEstructura:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovEstructura').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('EstructuraDialogo').hide()");
   }

   public void actualizarEvaluador() {
      log.info("Controlador.controlNReporteEvalCompetencia.actualizarEvaluador()");
      parametroDeReporte.setEvaluador(evaluadorSeleccionado.getDescripcion());
      parametroModificacion = parametroDeReporte;
      cambiosReporte = false;
      evaluadorSeleccionado = null;
      aceptar = true;
      filtrarListEvaluador = null;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formDialogos:lovEvaluador:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovEvaluador').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('EvaluadorDialogo').hide()");

      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      RequestContext.getCurrentInstance().update("formParametros:evaluadorParametro");

   }

   public void cancelarEvaluador() {
      log.info("Controlador.controlNReporteEvalCompetencia.cancelarEvaluador()");
      evaluadorSeleccionado = null;
      aceptar = true;
      filtrarListEvaluador = null;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formDialogos:lovEvaluador:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovEvaluador').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('EvaluadorDialogo').hide()");
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
      RequestContext.getCurrentInstance().update("form:reportesEvalCompetencia");
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
      log.info(this.getClass().getName() + ".autocompletarGeneral()");
      RequestContext context = RequestContext.getCurrentInstance();
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

      if (campoConfirmar.equalsIgnoreCase("ESTRUCTURA")) {
         if (!valorConfirmar.isEmpty()) {
            parametroDeReporte.getLocalizacion().setNombre(estructura);
            for (int i = 0; i < lovEstructuras.size(); i++) {
               if (lovEstructuras.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               parametroDeReporte.setLocalizacion(lovEstructuras.get(indiceUnicoElemento));
               parametroModificacion = parametroDeReporte;
               lovEstructuras.clear();
               getLovEstructuras();
               cambiosReporte = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            } else {
               permitirIndex = false;
               RequestContext.getCurrentInstance().update("form:EstructuraDialogo");
               RequestContext.getCurrentInstance().execute("PF('EstructuraDialogo').show()");
            }
         } else {
            parametroDeReporte.setLocalizacion(new Estructuras());
            parametroModificacion = parametroDeReporte;
            lovEstructuras.clear();
            getLovEstructuras();
            cambiosReporte = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
      }
      if (campoConfirmar.equalsIgnoreCase("PLANILLA")) {
         if (!valorConfirmar.isEmpty()) {
            parametroDeReporte.getEvalplanilla().setDescripcion(planilla);
            for (int i = 0; i < lovPlanilla.size(); i++) {
               if (lovPlanilla.get(i).getDescripcion().startsWith(valorConfirmar)) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               parametroDeReporte.setEvalplanilla(lovPlanilla.get(indiceUnicoElemento));
               parametroModificacion = parametroDeReporte;
               lovPlanilla.clear();
               getLovPlanilla();
               cambiosReporte = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            } else {
               permitirIndex = false;
               RequestContext.getCurrentInstance().update("formDialogos:PlanillaDialogo");
               RequestContext.getCurrentInstance().execute("PF('PlanillaDialogo').show()");
            }
         } else {
            parametroDeReporte.setEvalplanilla(new Evalplanillas());
            parametroModificacion = parametroDeReporte;
            lovPlanilla.clear();
            getLovPlanilla();
            cambiosReporte = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
      }
      if (campoConfirmar.equalsIgnoreCase("CONVOCATORIA")) {
         if (!valorConfirmar.isEmpty()) {
            parametroDeReporte.getConvocatoria().setCodigo(convocatoria);
            for (int i = 0; i < lovConvocatoria.size(); i++) {
               if (lovConvocatoria.get(i).getCodigo().startsWith(valorConfirmar)) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               parametroDeReporte.setConvocatoria(lovConvocatoria.get(indiceUnicoElemento));
               parametroModificacion = parametroDeReporte;
               lovConvocatoria.clear();
               getLovConvocatoria();
               cambiosReporte = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            } else {
               permitirIndex = false;
               RequestContext.getCurrentInstance().update("formDialogos:ConvocatoriaDialogo");
               RequestContext.getCurrentInstance().execute("PF('ConvocatoriaDialogo').show()");
            }
         } else {
            parametroDeReporte.setConvocatoria(new Evalconvocatorias());
            parametroModificacion = parametroDeReporte;
            lovConvocatoria.clear();
            getLovConvocatoria();
            cambiosReporte = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
      }
      if (campoConfirmar.equalsIgnoreCase("EVALUADOR")) {
         if (!valorConfirmar.isEmpty()) {
            parametroDeReporte.getEvaluador();
            for (int i = 0; i < lovEvaluador.size(); i++) {
               if (lovEvaluador.get(i).getDescripcion().startsWith(valorConfirmar)) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               parametroDeReporte.setEvaluador(evaluador);
               parametroModificacion = parametroDeReporte;
               lovEvaluador.clear();
               getLovEvaluador();
               cambiosReporte = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            } else {
               permitirIndex = false;
               RequestContext.getCurrentInstance().update("formDialogos:EvaluadorDialogo");
               RequestContext.getCurrentInstance().execute("PF('EvaluadorDialogo').show()");
            }
         } else {
            parametroDeReporte.setEvalplanilla(new Evalplanillas());
            parametroModificacion = parametroDeReporte;
            lovPlanilla.clear();
            getLovEvaluador();
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
               fechaConvocatoria = parametroDeReporte.getFechaconvocatorias();
               break;
            case 2:
               emplDesde = parametroDeReporte.getCodigoempleadodesde();
               break;
            case 3:
               emplHasta = parametroDeReporte.getCodigoempleadohasta();
               break;
            case 4:
               planilla = parametroDeReporte.getEvalplanilla().getDescripcion();
               break;
            case 5:
               estructura = parametroDeReporte.getLocalizacion().getNombre();
               break;
            case 6:
               convocatoria = parametroDeReporte.getConvocatoria().getCodigo();
               break;
            case 7:
               evaluador = parametroDeReporte.getEvaluador();
               break;
            case 8:
               empresa = parametroDeReporte.getEmpresa().getNombre();
               break;
            default:
               break;
         }
      }
   }

   //MODIFICAR PARAMETRO REPORTE
   public void modificarParametroInforme() {
      if (parametroDeReporte.getFechaconvocatorias() != null) {
         log.info("Entre al segundo if");
         parametroModificacion = parametroDeReporte;
         cambiosReporte = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
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

   //CONTARREGISTROS
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

   public void contarRegistrosPlanilla() {
      RequestContext.getCurrentInstance().update("formDialogos:infoRegistroPlanilla");
   }

   public void contarRegistrosEstructura() {
      RequestContext.getCurrentInstance().update("formDialogos:infoRegistroEstructura");
   }

   public void contarRegistrosConvocatoria() {
      RequestContext.getCurrentInstance().update("formDialogos:infoRegistroConvocatoria");
   }

   public void contarRegistrosEvaluador() {
      RequestContext.getCurrentInstance().update("formDialogos:infoRegistroEvaluador");
   }

   public void contarRegistrosEmpresa() {
      RequestContext.getCurrentInstance().update("formDialogos:infoRegistroEmpresa");
   }

   public void mostrarDialogoNuevaFecha() {
      getParametroDeInforme();
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
   public ParametrosReportes getParametroDeInforme() {
      try {
         if (parametroDeReporte == null) {
            parametroDeReporte = new ParametrosReportes();
            parametroDeReporte = administrarNReporteEvalCompetencia.parametrosDeReporte();
         }
         if (parametroDeReporte.getEvalplanilla() == null) {
            parametroDeReporte.setEvalplanilla(new Evalplanillas());
         }
         if (parametroDeReporte.getLocalizacion() == null) {
            parametroDeReporte.setLocalizacion(new Estructuras());
         }
         if (parametroDeReporte.getConvocatoria() == null) {
            parametroDeReporte.setConvocatoria(new Evalconvocatorias());
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

   public void setParametroDeInforme(ParametrosReportes parametroDeReporte) {
      this.parametroDeReporte = parametroDeReporte;
   }

   public List<Inforeportes> getListaIR() {
      try {
         if (listaIR == null) {
            listaIR = administrarNReporteEvalCompetencia.listInforeportesUsuario();
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

   public List<Inforeportes> getFiltrarListIRU() {
      return filtrarListIRU;
   }

   public void setFiltrarListIRU(List<Inforeportes> filtrarListIRU) {
      this.filtrarListIRU = filtrarListIRU;
   }

   public Inforeportes getReporteSeleccionadoLOV() {
      return reporteSeleccionadoLOV;
   }

   public void setReporteSeleccionadoLOV(Inforeportes inforreporteSeleccionadoLov) {
      this.reporteSeleccionadoLOV = inforreporteSeleccionadoLov;
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

   public List<Empleados> getLovEmpleados() {
      if (lovEmpleados == null) {
         if (listasRecurrentes.getLovEmpleadosActivos().isEmpty()) {
            lovEmpleados = administrarNReporteEvalCompetencia.listEmpleados();
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

   public void setLovEmpleados(List<Empleados> listEmpleados) {
      this.lovEmpleados = listEmpleados;
   }

   public List<Empresas> getLovEmpresas() {
      if (lovEmpresas == null || lovEmpresas.isEmpty()) {
         lovEmpresas = administrarNReporteEvalCompetencia.listEmpresas();
      }
      return lovEmpresas;
   }

   public void setLovEmpresas(List<Empresas> listEmpresas) {
      this.lovEmpresas = listEmpresas;
   }

   public List<Estructuras> getLovEstructuras() {
      if (lovEstructuras == null || lovEstructuras.isEmpty()) {
         lovEstructuras = administrarNReporteEvalCompetencia.listEstructuras();
      }
      return lovEstructuras;
   }

   public void setLovEstructuras(List<Estructuras> listEstructuras) {
      this.lovEstructuras = listEstructuras;
   }

   public Empleados getEmpleadoSeleccionado() {
      return empleadoSeleccionado;
   }

   public void setEmpleadoSeleccionado(Empleados empleadoSeleccionado) {
      this.empleadoSeleccionado = empleadoSeleccionado;
   }

   public Empresas getEmpresaSeleccionada() {
      return empresaSeleccionada;
   }

   public void setEmpresaSeleccionada(Empresas empresaSeleccionada) {
      this.empresaSeleccionada = empresaSeleccionada;
   }

   public List<Evalplanillas> getLovPlanilla() {
      if (lovPlanilla == null || lovPlanilla.isEmpty()) {
         lovPlanilla = administrarNReporteEvalCompetencia.listPlanillas();
      }
      return lovPlanilla;
   }

   public void setLovPlanilla(List<Evalplanillas> lovPlanilla) {
      this.lovPlanilla = lovPlanilla;
   }

   public List<Evalplanillas> getFiltrarListPlanilla() {
      return filtrarListPlanilla;
   }

   public void setFiltrarListPlanilla(List<Evalplanillas> filtrarListPlanilla) {
      this.filtrarListPlanilla = filtrarListPlanilla;
   }

   public List<Evalconvocatorias> getLovConvocatoria() {
      if (lovConvocatoria == null || lovConvocatoria.isEmpty()) {
         lovConvocatoria = administrarNReporteEvalCompetencia.listConvocatorias();
      }
      return lovConvocatoria;
   }

   public void setLovConvocatoria(List<Evalconvocatorias> lovConvocatoria) {
      this.lovConvocatoria = lovConvocatoria;
   }

   public List<Evalconvocatorias> getFiltrarListConvocatoria() {
      return filtrarListConvocatoria;
   }

   public void setFiltrarListConvocatoria(List<Evalconvocatorias> filtrarListConvocatoria) {
      this.filtrarListConvocatoria = filtrarListConvocatoria;
   }

   public List<EvalEvaluadores> getLovEvaluador() {
      if (lovEvaluador == null || lovEvaluador.isEmpty()) {
         lovEvaluador = administrarNReporteEvalCompetencia.listEvaluadores();
      }
      return lovEvaluador;
   }

   public void setLovEvaluador(List<EvalEvaluadores> lovEvaluador) {
      this.lovEvaluador = lovEvaluador;
   }

   public List<EvalEvaluadores> getFiltrarListEvaluador() {
      return filtrarListEvaluador;
   }

   public void setFiltrarListEvaluador(List<EvalEvaluadores> filtrarListEvaluador) {
      this.filtrarListEvaluador = filtrarListEvaluador;
   }

   public Estructuras getEstructuraSeleccionada() {
      return estructuraSeleccionada;
   }

   public void setEstructuraSeleccionada(Estructuras estructuraSeleccionada) {
      this.estructuraSeleccionada = estructuraSeleccionada;
   }

   public List<Empleados> getFiltrarListEmpleados() {
      return filtrarListEmpleados;
   }

   public void setFiltrarListEmpleados(List<Empleados> filtrarListEmpleados) {
      this.filtrarListEmpleados = filtrarListEmpleados;
   }

   public List<Empresas> getFiltrarListEmpresas() {
      return filtrarListEmpresas;
   }

   public void setFiltrarListEmpresas(List<Empresas> filtrarListEmpresas) {
      this.filtrarListEmpresas = filtrarListEmpresas;
   }

   public List<Estructuras> getFiltrarListEstructuras() {
      return filtrarListEstructuras;
   }

   public void setFiltrarListEstructuras(List<Estructuras> filtrarListEstructuras) {
      this.filtrarListEstructuras = filtrarListEstructuras;
   }

   public String getAltoTabla() {
      return altoTabla;
   }

   public List<Inforeportes> getListaInfoReportesModificados() {
      return listaInfoReportesModificados;
   }

   public void setListaInfoReportesModificados(List<Inforeportes> listaInfoReportesModificados) {
      this.listaInfoReportesModificados = listaInfoReportesModificados;
   }

   public boolean isCambiosReporte() {
      return cambiosReporte;
   }

   public void setCambiosReporte(boolean cambiosReporte) {
      this.cambiosReporte = cambiosReporte;
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

   public StreamedContent getReporte() {
      return reporte;
   }

   public String getCabezeraVisor() {
      return cabezeraVisor;
   }

   public String getPathReporteGenerado() {
      return pathReporteGenerado;
   }

   public String getInfoRegistro() {
      FacesContext c = FacesContext.getCurrentInstance();
      tabla = (DataTable) c.getViewRoot().findComponent("form:reportesEvalCompetencia");
      infoRegistro = String.valueOf(tabla.getRowCount());
      return infoRegistro;
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

   public String getInfoRegistroPlanilla() {
      FacesContext c = FacesContext.getCurrentInstance();
      tabla = (DataTable) c.getViewRoot().findComponent("formDialogos:lovPlanilla");
      infoRegistroPlanilla = String.valueOf(tabla.getRowCount());
      return infoRegistroPlanilla;
   }

   public String getInfoRegistroConvocatoria() {
      FacesContext c = FacesContext.getCurrentInstance();
      tabla = (DataTable) c.getViewRoot().findComponent("formDialogos:lovConvocatoria");
      infoRegistroConvocatoria = String.valueOf(tabla.getRowCount());
      return infoRegistroConvocatoria;
   }

   public String getInfoRegistroLovReportes() {
      FacesContext c = FacesContext.getCurrentInstance();
      tabla = (DataTable) c.getViewRoot().findComponent("formDialogos:lovReportes");
      infoRegistroLovReportes = String.valueOf(tabla.getRowCount());
      return infoRegistroLovReportes;
   }

   public String getInfoRegistroEvaluador() {
      FacesContext c = FacesContext.getCurrentInstance();
      tabla = (DataTable) c.getViewRoot().findComponent("formDialogos:lovEvaluador");
      infoRegistroEvaluador = String.valueOf(tabla.getRowCount());
      return infoRegistroEvaluador;
   }

   public String getInfoRegistroEmpresa() {
      FacesContext c = FacesContext.getCurrentInstance();
      tabla = (DataTable) c.getViewRoot().findComponent("formDialogos:lovEmpresa");
      infoRegistroEmpresa = String.valueOf(tabla.getRowCount());
      return infoRegistroEmpresa;
   }

   public String getInfoRegistroEstructura() {
      FacesContext c = FacesContext.getCurrentInstance();
      tabla = (DataTable) c.getViewRoot().findComponent("formDialogos:lovEstructura");
      infoRegistroEstructura = String.valueOf(tabla.getRowCount());
      return infoRegistroEstructura;
   }

   public List<Inforeportes> getLovInforeportes() {
      if (lovInforeportes == null || lovInforeportes.isEmpty()) {
         lovInforeportes = administrarNReporteEvalCompetencia.listInforeportesUsuario();
      }
      return lovInforeportes;
   }

   public void setLovInforeportes(List<Inforeportes> lovInforeportes) {
      this.lovInforeportes = lovInforeportes;
   }

   public List<Inforeportes> getFiltrarReportes() {
      return filtrarReportes;
   }

   public void setFiltrarReportes(List<Inforeportes> filtrarReportes) {
      this.filtrarReportes = filtrarReportes;
   }

   public List<Inforeportes> getFiltrarLovInforeportes() {
      return filtrarLovInforeportes;
   }

   public void setFiltrarLovInforeportes(List<Inforeportes> filtrarLovInforeportes) {
      this.filtrarLovInforeportes = filtrarLovInforeportes;
   }

   public Inforeportes getReporteSeleccionado() {
      return reporteSeleccionado;
   }

   public void setReporteSeleccionado(Inforeportes inforreporteSeleccionado) {
      this.reporteSeleccionado = inforreporteSeleccionado;
   }

   public boolean isActivarEnvio() {
      return activarEnvio;
   }

   public void setActivarEnvio(boolean activarEnvio) {
      this.activarEnvio = activarEnvio;
   }

   public Evalplanillas getPlanillaSeleccionada() {
      return planillaSeleccionada;
   }

   public void setPlanillaSeleccionada(Evalplanillas planillaSeleccionada) {
      this.planillaSeleccionada = planillaSeleccionada;
   }

   public Evalconvocatorias getConvocatoriaSeleccionada() {
      return convocatoriaSeleccionada;
   }

   public void setConvocatoriaSeleccionada(Evalconvocatorias convocatoriaSeleccionada) {
      this.convocatoriaSeleccionada = convocatoriaSeleccionada;
   }

   public EvalEvaluadores getEvaluadorSeleccionado() {
      return evaluadorSeleccionado;
   }

   public void setEvaluadorSeleccionado(EvalEvaluadores evaluadorSeleccionado) {
      this.evaluadorSeleccionado = evaluadorSeleccionado;
   }

   public ParametrosReportes getParametroDeReporte() {
      return parametroDeReporte;
   }

   public void setParametroDeReporte(ParametrosReportes parametroDeReporte) {
      this.parametroDeReporte = parametroDeReporte;
   }

}
