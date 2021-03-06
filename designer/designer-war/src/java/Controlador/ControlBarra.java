package Controlador;

import Entidades.ConsultasLiquidaciones;
import Entidades.ParametrosEstructuras;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarBarraInterface;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import ControlNavegacion.ControlListaNavegacion;
import InterfaceAdministrar.AdministarReportesInterface;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.LinkedHashMap;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.primefaces.component.column.Column;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.export.Exporter;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@ManagedBean
@SessionScoped
public class ControlBarra implements Serializable {

   private static Logger log = Logger.getLogger(ControlBarra.class);

   @EJB
   AdministrarBarraInterface administrarBarra;
   @EJB
   AdministarReportesInterface administarReportes;

   private Integer totalEmpleadosParaLiquidar;
   private Integer totalEmpleadosLiquidados;
   private boolean permisoParaLiquidar;
   private String usuarioBD;
   private Integer barra;
   private ParametrosEstructuras parametroEstructura;
   private boolean empezar, botonLiquidar, botonCancelar, cambioImagen;
   private boolean bandera, preparandoDatos;
   private String horaInicialLiquidacion, horaFinalLiquidacion, mensajeBarra, mensajeEstado, imagenEstado;
   private SimpleDateFormat formato, formatoFecha;
   //LIQUIDACIONES CERRADAS - ABIERTAS
   private List<ConsultasLiquidaciones> liquidacionesCerradas, liquidacionesAbiertas, filtradoLiquidacionesCerradas, filtradoLiquidacionesAbiertas;
   private Column corte, empresa, proceso, totalEmpleados, observacion;
   private Column corteLA, empresaLA, procesoLA, totalEmpleadosLA, observacionLA;
   private String altoScrollLiquidacionesCerradas, altoScrollLiquidacionesAbiertas;
   private int banderaFiltros;
   private boolean liquifinalizada;
   private String infoRegistroCerradas, infoRegistroEnProceso;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();
   private String pathReporteGenerado = null;
   private StreamedContent reporte;
   private String cabezeraVisor;

   public ControlBarra() {
      totalEmpleadosParaLiquidar = 0;
      totalEmpleadosLiquidados = 0;
      barra = 0;
      empezar = false;
      bandera = true;
      preparandoDatos = false;
      botonCancelar = true;
      botonLiquidar = false;
      horaInicialLiquidacion = "--:--:--";
      horaFinalLiquidacion = "--:--:--";
      formato = new SimpleDateFormat("hh:mm:ss a");
      formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
      mensajeEstado = "Oprima el boton liquidar para iniciar.";
      imagenEstado = "nom_parametros_liq.png";
      cambioImagen = true;
      liquidacionesAbiertas = null;
      liquidacionesCerradas = null;
      altoScrollLiquidacionesAbiertas = "125";
      altoScrollLiquidacionesCerradas = "125";
      banderaFiltros = 0;
      liquifinalizada = false;
      infoRegistroCerradas = "0";
      infoRegistroEnProceso = "0";
      mapParametros.put("paginaAnterior", paginaAnterior);
   }

   public void limpiarListasValor() {

   }

   @PreDestroy
   public void destruyendoce() {
      log.info(this.getClass().getName() + ".destruyendoce() @Destroy");
   }
   
   @PostConstruct
   public void inicializarAdministrador() {
      log.info(this.getClass().getName() + ".inicializarAdministrador() @PostConstruct");
      try {
         FacesContext x = FacesContext.getCurrentInstance();
         HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
         administrarBarra.obtenerConexion(ses.getId());
         administarReportes.obtenerConexion(ses.getId());
      } catch (Exception e) {
         log.error("Error postconstruct " + this.getClass().getName() + ":  ", e);
         log.error("Causa: " + e.getCause());
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
      String pagActual = "barra";
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

   public void contarLiquidados() {
      totalEmpleadosLiquidados = administrarBarra.contarEmpleadosLiquidados();
      log.info("totalEmpleadosLiquidados: " + totalEmpleadosLiquidados);
      RequestContext.getCurrentInstance().update("form:empleadosLiquidados");
   }

   public void liquidar() {
      empezar = true;
      liquifinalizada = false;
      usuarioBD = administrarBarra.consultarUsuarioBD();
      permisoParaLiquidar = administrarBarra.verificarPermisosLiquidar(usuarioBD);
      log.info("permisoParaLiquidar : " + permisoParaLiquidar);
      if (permisoParaLiquidar) {
         administrarBarra.inicializarParametrosEstados();
         administrarBarra.liquidarNomina();
         mensajeBarra = "Preparando datos...";
         mensajeEstado = "Un momento mientras se analizan los parametros.";
         imagenEstado = "hand2.png";
         preparandoDatos = true;
         botonLiquidar = true;
         botonCancelar = false;
         Date horaInicio = new Date();
         log.info("Hora Inicio: " + formato.format(horaInicio));
         horaInicialLiquidacion = formato.format(horaInicio);
         horaFinalLiquidacion = "--:--:--";
         RequestContext.getCurrentInstance().execute("PF('barra').start()");
         RequestContext.getCurrentInstance().update("form:liquidar");
         RequestContext.getCurrentInstance().update("form:cancelar");
         RequestContext.getCurrentInstance().update("form:horaF");
         RequestContext.getCurrentInstance().update("form:horaI");
         RequestContext.getCurrentInstance().update("form:estadoLiquidacion");
         RequestContext.getCurrentInstance().update("form:imagen");
      } else {
         RequestContext.getCurrentInstance().execute("PF('permisoLiquidacion').show()");
      }
      log.error("Termino la funcion liquidar()");
   }

   public void limpiarbarra() {
      barra = null;
      empezar = !empezar;
      liquifinalizada = false;
   }

   public void cancelarLiquidacion() {
      RequestContext.getCurrentInstance().execute("PF('barra').cancel()");
      empezar = false;
      liquifinalizada = true;
      administrarBarra.cancelarLiquidacion(usuarioBD);
      Date horaFinal = new Date();
      horaFinalLiquidacion = formato.format(horaFinal);
      mensajeBarra = "Liquidacion Cancelada (" + barra + "%)";
      mensajeEstado = "El proceso de liquidacion fue cancelado.";
      imagenEstado = "cancelarLiquidacion.png";
      botonCancelar = true;
      botonLiquidar = false;
      FacesMessage msg = new FacesMessage("Información", "Liquidación cancelada.");
      FacesContext.getCurrentInstance().addMessage(null, msg);
      RequestContext.getCurrentInstance().update("form:horaF");
      RequestContext.getCurrentInstance().update("form:liquidar");
      RequestContext.getCurrentInstance().update("form:cancelar");
      RequestContext.getCurrentInstance().update("form:estadoLiquidacion");
      RequestContext.getCurrentInstance().update("form:imagen");
      RequestContext.getCurrentInstance().execute("PF('barra').setValue(" + barra + ")");
      RequestContext.getCurrentInstance().update("form:barra");
      RequestContext.getCurrentInstance().update("form:growl");
      consultarEstadoDatos();
      contarRegistrosCerrada();
      contarRegistrosEnProceso();
      empezar = false;
   }

   public void salir() {
      limpiarListasValor();
      totalEmpleadosParaLiquidar = 0;
      totalEmpleadosLiquidados = 0;
      barra = 0;
      mensajeBarra = "";
      empezar = false;
      liquifinalizada = false;
      bandera = true;
      preparandoDatos = false;
      botonCancelar = true;
      botonLiquidar = false;
      horaInicialLiquidacion = "--:--:--";
      horaFinalLiquidacion = "--:--:--";
      formato = new SimpleDateFormat("hh:mm:ss a");
      mensajeEstado = "Oprima el boton liquidar para iniciar.";
      imagenEstado = "nom_parametros_liq.png";
      cambioImagen = true;
      liquidacionesAbiertas = null;
      liquidacionesCerradas = null;
      filtradoLiquidacionesAbiertas = null;
      filtradoLiquidacionesCerradas = null;
      parametroEstructura = null;
      navegar("atras");
   }

   public void consultarDatos() {
      log.info("Entro en consultarDatos() parametroEstructura : " + parametroEstructura);
      if (parametroEstructura != null) {
         liquidacionesCerradas = administrarBarra.liquidacionesCerradas(formatoFecha.format(parametroEstructura.getFechadesdecausado()), formatoFecha.format(parametroEstructura.getFechahastacausado()));
      }
      liquidacionesAbiertas = administrarBarra.consultarPreNomina();
      contarRegistrosCerrada();
      contarRegistrosEnProceso();
      RequestContext.getCurrentInstance().update("form:datosLiquidacionesCerradas");
      RequestContext.getCurrentInstance().update("form:datosLiquidacionesAbiertas");
   }

   public void consultarEstadoDatos() {
      if (parametroEstructura != null && administrarBarra.consultarEstadoConsultaDatos(parametroEstructura.getEstructura().getOrganigrama().getEmpresa().getSecuencia()).equals("S")) {
         consultarDatos();
      }
   }

   //CTRL + F11 ACTIVAR/DESACTIVAR FILTROS
   public void activarCtrlF11() {
      if (liquidacionesAbiertas != null && liquidacionesCerradas != null) {
         if (banderaFiltros == 0) {
            //LIQUIDACIONES CERRADAS
            corte = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosLiquidacionesCerradas:corte");
            corte.setFilterStyle("width: 85% !important;");
            empresa = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosLiquidacionesCerradas:empresa");
            empresa.setFilterStyle("width: 85% !important;");
            proceso = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosLiquidacionesCerradas:proceso");
            proceso.setFilterStyle("width: 85% !important;");
            totalEmpleados = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosLiquidacionesCerradas:totalEmpleados");
            totalEmpleados.setFilterStyle("width: 85% !important;");
            observacion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosLiquidacionesCerradas:observacion");
            observacion.setFilterStyle("width: 85% !important;");

            //LIQUIDACIONES ABIERTAS
            corteLA = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosLiquidacionesAbiertas:corteLA");
            corteLA.setFilterStyle("width: 85% !important;");
            empresaLA = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosLiquidacionesAbiertas:empresaLA");
            empresaLA.setFilterStyle("width: 85% !important;");
            procesoLA = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosLiquidacionesAbiertas:procesoLA");
            procesoLA.setFilterStyle("width: 85% !important;");
            totalEmpleadosLA = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosLiquidacionesAbiertas:totalEmpleadosLA");
            totalEmpleadosLA.setFilterStyle("width: 85% !important;");
            observacionLA = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosLiquidacionesAbiertas:observacionLA");
            observacionLA.setFilterStyle("width: 85% !important;");

            altoScrollLiquidacionesCerradas = "105";
            altoScrollLiquidacionesAbiertas = "105";
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosLiquidacionesCerradas");
            RequestContext.getCurrentInstance().update("form:datosLiquidacionesAbiertas");
            banderaFiltros = 1;

         } else if (banderaFiltros == 1) {
            //LIQUIDACIONES CERRADAS
            corte = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosLiquidacionesCerradas:corte");
            corte.setFilterStyle("display: none; visibility: hidden;");
            empresa = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosLiquidacionesCerradas:empresa");
            empresa.setFilterStyle("display: none; visibility: hidden;");
            proceso = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosLiquidacionesCerradas:proceso");
            proceso.setFilterStyle("display: none; visibility: hidden;");
            totalEmpleados = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosLiquidacionesCerradas:totalEmpleados");
            totalEmpleados.setFilterStyle("display: none; visibility: hidden;");
            observacion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosLiquidacionesCerradas:observacion");
            observacion.setFilterStyle("display: none; visibility: hidden;");

            //LIQUIDACIONES ABIERTAS
            corteLA = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosLiquidacionesAbiertas:corteLA");
            corteLA.setFilterStyle("display: none; visibility: hidden;");
            empresaLA = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosLiquidacionesAbiertas:empresaLA");
            empresaLA.setFilterStyle("display: none; visibility: hidden;");
            procesoLA = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosLiquidacionesAbiertas:procesoLA");
            procesoLA.setFilterStyle("display: none; visibility: hidden;");
            totalEmpleadosLA = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosLiquidacionesAbiertas:totalEmpleadosLA");
            totalEmpleadosLA.setFilterStyle("display: none; visibility: hidden;");
            observacionLA = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosLiquidacionesAbiertas:observacionLA");
            observacionLA.setFilterStyle("display: none; visibility: hidden;");

            altoScrollLiquidacionesCerradas = "125";
            altoScrollLiquidacionesAbiertas = "125";
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosLiquidacionesCerradas");
            RequestContext.getCurrentInstance().update("form:datosLiquidacionesAbiertas");
            banderaFiltros = 0;
            filtradoLiquidacionesAbiertas = null;
            filtradoLiquidacionesCerradas = null;
         }
      }
   }

   public void exportPDF(int tablaExportar) throws IOException {
      DataTable tabla;
      Exporter exporter = new ExportarPDF();
      FacesContext context = FacesContext.getCurrentInstance();
      if (tablaExportar == 0) {
         tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosLiquidacionesCerradasExportar");
         exporter.export(context, tabla, "LiquidacionesCerradasPDF", false, false, "UTF-8", null, null);
      } else {
         tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosLiquidacionesAbiertasExportar");
         exporter.export(context, tabla, "LiquidacionesPrenominaPDF", false, false, "UTF-8", null, null);
      }
      context.responseComplete();
   }

   public void exportXLS(int tablaExportar) throws IOException {
      DataTable tabla;
      Exporter exporter = new ExportarXLS();
      FacesContext context = FacesContext.getCurrentInstance();
      if (tablaExportar == 0) {
         tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosLiquidacionesCerradasExportar");
         exporter.export(context, tabla, "LiquidacionesCerradasXLS", false, false, "UTF-8", null, null);
      } else {
         tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosLiquidacionesAbiertasExportar");
         exporter.export(context, tabla, "LiquidacionesPrenominaXLS", false, false, "UTF-8", null, null);
      }
      context.responseComplete();
   }

   public void contarRegistrosCerrada() {
      RequestContext.getCurrentInstance().update("form:infoRegistroCerradas");
   }

   public void contarRegistrosEnProceso() {
      RequestContext.getCurrentInstance().update("form:infoRegistroEnProceso");
   }

   //GETTER AND SETTER
   public Integer getTotalEmpleadosParaLiquidar() {
      totalEmpleadosParaLiquidar = administrarBarra.contarEmpleadosParaLiquidar();
      if (totalEmpleadosParaLiquidar == 0) {
         botonLiquidar = true;
      } else {
         botonLiquidar = false;
      }
      return totalEmpleadosParaLiquidar;
   }

   public void setTotalEmpleadosParaLiquidar(Integer totalEmpleadosParaLiquidar) {
      this.totalEmpleadosParaLiquidar = totalEmpleadosParaLiquidar;
   }

   public Integer getTotalEmpleadosLiquidados() {
      return totalEmpleadosLiquidados;
   }

   public void setTotalEmpleadosLiquidados(Integer totalEmpleadosLiquidados) {
      this.totalEmpleadosLiquidados = totalEmpleadosLiquidados;
   }

   public Integer getBarra() {
      log.info("Entra GetBarra");
      if (empezar == true) {
         String estado = administrarBarra.consultarEstadoLiquidacion(usuarioBD);
         log.info("Estado: 1" + estado);
         if (preparandoDatos == true) {
            barra = 101;
            RequestContext.getCurrentInstance().update("form:barra");
            if (!estado.equalsIgnoreCase("INICIADO") && !estado.equalsIgnoreCase("EN COLA")) {
               preparandoDatos = false;
               barra = null;
               bandera = true;
               mensajeEstado = "Liquidando personal.";
               RequestContext.getCurrentInstance().update("form:estadoLiquidacion");
            }
         } else if (barra == null) {
            barra = 0;
         } else {
            barra = administrarBarra.consultarProgresoLiquidacion(totalEmpleadosParaLiquidar);
            if (!estado.equalsIgnoreCase("FINALIZADO")) {
               log.info("NO ES FINALIZADO");
               if (barra >= 100) {
                  barra = 100;
                  bandera = false;
               }
               mensajeBarra = "Liquidando... " + barra + "%";
               RequestContext.getCurrentInstance().update("form:barra");
               RequestContext.getCurrentInstance().update("form:estadoLiquidacion");
               log.info("Liquidando... " + barra + "%");
               if (bandera == true) {
                  if (cambioImagen == true) {
                     cambioImagen = false;
                     imagenEstado = "hand2.png";
                  } else {
                     cambioImagen = true;
                     imagenEstado = "hand1.png";
                  }
                  Date horaFinal = new Date();
                  horaFinalLiquidacion = formato.format(horaFinal);
                  RequestContext.getCurrentInstance().update("form:barra");
                  RequestContext.getCurrentInstance().update("form:horaF");
                  RequestContext.getCurrentInstance().update("form:imagen");
                  contarLiquidados();
               }
            } else {
               log.info("ES FINALIZADO");
               if (barra < 100) {
                  log.info("Liquidacion Terminada Parcialmente");
                  RequestContext.getCurrentInstance().execute("PF('barra').cancel()");
                  empezar = false;
                  liquifinalizada = true;
                  Date horaFinal = new Date();
                  horaFinalLiquidacion = formato.format(horaFinal);
                  mensajeBarra = "Liquidacion Finalizada (al " + barra + "%)";
                  mensajeEstado = "Liquidacion terminada parcialmente.";
                  imagenEstado = "hand3.png";
                  botonCancelar = true;
                  botonLiquidar = false;
                  FacesMessage msg = new FacesMessage("Información", "Liquidación terminada parcialmente.");
                  FacesContext.getCurrentInstance().addMessage(null, msg);
                  RequestContext.getCurrentInstance().update("form:horaF");
                  RequestContext.getCurrentInstance().update("form:liquidar");
                  RequestContext.getCurrentInstance().update("form:cancelar");
                  RequestContext.getCurrentInstance().update("form:estadoLiquidacion");
                  RequestContext.getCurrentInstance().update("form:imagen");
                  RequestContext.getCurrentInstance().execute("PF('barra').setValue(" + barra + ")");
                  RequestContext.getCurrentInstance().update("form:barra");
                  RequestContext.getCurrentInstance().update("form:estadoLiquidacion");
                  RequestContext.getCurrentInstance().update("form:growl");
                  liquidacionCompleta();
               } else if (barra >= 100) {
                  barra = 100;
                  mensajeEstado = "Liquidación terminada con exito.";
                  mensajeBarra = "Liquidación Completa (" + barra + "%)";
                  log.info("Esta en teoría completa...Barra: " + barra);
                  liquidacionCompleta();
               }
            }
         }
         log.info("Estado: 2" + estado);
      }
      return barra;
   }

   public void liquidacionCompleta() {
      log.info("Liquidación Completada");
      imagenEstado = "hand3.png";
      empezar = false;
      liquifinalizada = true;
      botonCancelar = true;
      botonLiquidar = false;
      totalEmpleadosLiquidados = administrarBarra.contarEmpleadosLiquidados();
      log.info("totalEmpleadosLiquidados: " + totalEmpleadosLiquidados);
      Date horaFinal = new Date();
      log.info("Hora Final: " + formato.format(horaFinal));
      horaFinalLiquidacion = formato.format(horaFinal);
      RequestContext context = RequestContext.getCurrentInstance();

      context.update("form:horaF");
      context.update("form:empleadosLiquidados");
      context.update("form:liquidar");
      context.update("form:cancelar");
      context.update("form:estadoLiquidacion");

      log.info("1");
      context.update("form:imagen");
      context.update("form:barra");
      context.update("form:panelLiquidacion");
      context.update("form:PanelTotal");
      context.update("form:estadoLiquidacion");

      log.info("2");
      if (totalEmpleadosLiquidados == 1 || totalEmpleadosLiquidados == 0) {
         RequestContext.getCurrentInstance().execute("location.reload(true)");
      }
      log.info("3");
      FacesMessage msg = new FacesMessage("Información", "Liquidación terminada con Éxito.");
      FacesContext.getCurrentInstance().addMessage(null, msg);
      context.update("form:growl");
//      context.execute("PF('barra').stop()");
   }

   public void reiniciarStreamedContent() {
      reporte = null;
   }

   public void validarDescargaReporte() {
      try {
         String nombreReporte = "PreValidacionLiquidacion";
         String tipoReporte = "PDF";
         Map param = new HashMap();

         pathReporteGenerado = administarReportes.generarReporte(nombreReporte, tipoReporte);
         if (pathReporteGenerado != null && !pathReporteGenerado.startsWith("Error:")) {
            log.info("validar descarga reporte - ingreso al if 1");
            if (tipoReporte.equals("PDF")) {
               FileInputStream fis;
               try {
                  fis = new FileInputStream(new File(pathReporteGenerado));
                  reporte = new DefaultStreamedContent(fis, "application/pdf");
                  cabezeraVisor = "Reporte - " + nombreReporte;
                  RequestContext.getCurrentInstance().update("form:verReportePDF");
                  RequestContext.getCurrentInstance().execute("PF('verReportePDF').show()");
                  pathReporteGenerado = null;
               } catch (FileNotFoundException ex) {
                  log.info("validar descarga reporte - ingreso al catch 1 ex: " + ex);
                  RequestContext.getCurrentInstance().execute("form:errorGenerandoReporte");
                  RequestContext.getCurrentInstance().execute("PF('errorGenerandoReporte').show()");
                  reporte = null;
               }
            }
         } else {
            log.info("validar descarga reporte - ingreso al if 1 else");
            RequestContext.getCurrentInstance().execute("form:errorGenerandoReporte");
            RequestContext.getCurrentInstance().execute("PF('errorGenerandoReporte').show()");
         }
      } catch (Exception e) {
         log.warn("Error en validar descargar Reporte" + e.toString());
         RequestContext.getCurrentInstance().execute("form:errorGenerandoReporte");
         RequestContext.getCurrentInstance().execute("PF('errorGenerandoReporte').show()");
      }
   }

   public void setBarra(Integer barra) {
      this.barra = barra;
   }

   public ParametrosEstructuras getParametroEstructura() {
      if (parametroEstructura == null) {
         parametroEstructura = administrarBarra.consultarParametrosLiquidacion();
      }
      return parametroEstructura;
   }

   public String getHoraInicialLiquidacion() {
      return horaInicialLiquidacion;
   }

   public String getHoraFinalLiquidacion() {
      return horaFinalLiquidacion;
   }

   public String getMensajeBarra() {
      return mensajeBarra;
   }

   public boolean isBotonLiquidar() {
      return botonLiquidar;
   }

   public boolean isBotonCancelar() {
      return botonCancelar;
   }

   public String getMensajeEstado() {
      return mensajeEstado;
   }

   public String getImagenEstado() {
      return imagenEstado;
   }

   public List<ConsultasLiquidaciones> getLiquidacionesCerradas() {
      return liquidacionesCerradas;
   }

   public List<ConsultasLiquidaciones> getLiquidacionesAbiertas() {
      return liquidacionesAbiertas;
   }

   public List<ConsultasLiquidaciones> getFiltradoLiquidacionesCerradas() {
      return filtradoLiquidacionesCerradas;
   }

   public void setFiltradoLiquidacionesCerradas(List<ConsultasLiquidaciones> filtradoLiquidacionesCerradas) {
      this.filtradoLiquidacionesCerradas = filtradoLiquidacionesCerradas;
   }

   public List<ConsultasLiquidaciones> getFiltradoLiquidacionesAbiertas() {
      return filtradoLiquidacionesAbiertas;
   }

   public void setFiltradoLiquidacionesAbiertas(List<ConsultasLiquidaciones> filtradoLiquidacionesAbiertas) {
      this.filtradoLiquidacionesAbiertas = filtradoLiquidacionesAbiertas;
   }

   public String getAltoScrollLiquidacionesCerradas() {
      return altoScrollLiquidacionesCerradas;
   }

   public String getAltoScrollLiquidacionesAbiertas() {
      return altoScrollLiquidacionesAbiertas;
   }

   public boolean isLiquifinalizada() {
      return liquifinalizada;
   }

   public void setLiquifinalizada(boolean liquifinalizada) {
      this.liquifinalizada = liquifinalizada;
   }

   public String getInfoRegistroCerradas() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosLiquidacionesCerradas");
      infoRegistroCerradas = String.valueOf(tabla.getRowCount());
      return infoRegistroCerradas;
   }

   public void setInfoRegistroCerradas(String infoRegistroCerradas) {
      this.infoRegistroCerradas = infoRegistroCerradas;
   }

   public String getInfoRegistroEnProceso() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosLiquidacionesAbiertas");
      infoRegistroEnProceso = String.valueOf(tabla.getRowCount());
      return infoRegistroEnProceso;
   }

   public void setInfoRegistroEnProceso(String infoRegistroEnProceso) {
      this.infoRegistroEnProceso = infoRegistroEnProceso;
   }

   public String getPathReporteGenerado() {
      return pathReporteGenerado;
   }

   public void setPathReporteGenerado(String pathReporteGenerado) {
      this.pathReporteGenerado = pathReporteGenerado;
   }

   public StreamedContent getReporte() {
      return reporte;
   }

   public void setReporte(StreamedContent reporte) {
      this.reporte = reporte;
   }

   public String getCabezeraVisor() {
      return cabezeraVisor;
   }

   public void setCabezeraVisor(String cabezeraVisor) {
      this.cabezeraVisor = cabezeraVisor;
   }

}
