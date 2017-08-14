/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Empleados;
import Entidades.NovedadesSistema;
import Entidades.Vacaciones;
import InterfaceAdministrar.AdministrarDetalleVacacionInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import ControlNavegacion.ControlListaNavegacion;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarNovedadesSistemaInterface;
import InterfaceAdministrar.AdministrarNovedadesVacacionesInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.GregorianCalendar;
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

/**
 *
 * @author user
 */
@ManagedBean
@SessionScoped
public class ControlDetallesVacaciones implements Serializable {

   private static Logger log = Logger.getLogger(ControlDetallesVacaciones.class);

   @EJB
   AdministrarRastrosInterface administrarRastros;
   @EJB
   AdministrarDetalleVacacionInterface administrarDetalleVacacion;
   @EJB
   AdministrarNovedadesSistemaInterface administrarNovedadesSistema;
   @EJB
   AdministrarNovedadesVacacionesInterface administrarNovedadesVacaciones;

   private List<NovedadesSistema> listaNovedadesSistema;
   private List<NovedadesSistema> filtrarrNovedadesSistema;
   private List<NovedadesSistema> modificarNovedadesSistema;
//
   private NovedadesSistema novedadSistemaSeleccionada;
   private NovedadesSistema editarNovedadSistema;
//
   private List<Vacaciones> lovPeriodos;
   private List<Vacaciones> lovPeriodosFiltrar;
   private Vacaciones periodoSeleccionado;
//
   private int cualCelda, tipoLista, tipoActualizacion, k, bandera;
   private BigInteger l;
   private Empleados empleado;
   private boolean aceptar, guardado, activarLOV;
   //
   private Column colFechaIni, colPeriodo, colDias, colFechaRegr, colSubtipo, colAdelanto, colFechaPago, colValorPago, colDiasAplaz;
   private String infoRegistro, infoRegistroPeriodo;
   private int tamano;
   //
   private BigInteger secuenciaVacacion;

   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();
   private String nombreEmpl, codEmpl;
   private Date fechaContratacionE;
   private BigInteger periodicidadCodigoDos;

   /**
    * Creates a new instance of ControlDetallesVacaciones
    */
   public ControlDetallesVacaciones() {
      listaNovedadesSistema = null;
      filtrarrNovedadesSistema = null;
      novedadSistemaSeleccionada = null;
      modificarNovedadesSistema = new ArrayList<NovedadesSistema>();
      lovPeriodos = null;
      lovPeriodosFiltrar = null;
      periodoSeleccionado = null;
      cualCelda = -1;
      tipoLista = 0;
      tipoActualizacion = 0;
      k = 0;
      bandera = 0;
      empleado = null;
      aceptar = false;
      guardado = true;
      activarLOV = true;
      paginaAnterior = "nominaf";
      tamano = 290;
      mapParametros.put("paginaAnterior", paginaAnterior);
      periodicidadCodigoDos = BigInteger.valueOf(19847);
   }

   public void limpiarListasValor() {
      lovPeriodos = null;
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
         administrarDetalleVacacion.obtenerConexion(ses.getId());
         administrarRastros.obtenerConexion(ses.getId());
         administrarNovedadesSistema.obtenerConexion(ses.getId());
         administrarNovedadesVacaciones.obtenerConexion(ses.getId());
      } catch (Exception e) {
         log.error("Error postconstruct " + this.getClass().getName() + ": " + e);
         log.error("Causa: " + e.getCause());
      }
   }

   public void recibirVariasCosas(String pagina, Empleados empl, BigInteger secVacacion) {
      paginaAnterior = pagina;
      secuenciaVacacion = secVacacion;
      empleado = empl;
      fechaContratacionE = administrarNovedadesVacaciones.obtenerFechaContratacionEmpleado(empleado.getSecuencia());
      if (empleado != null) {
         nombreEmpl = empleado.getPersona().getNombreCompleto();
         codEmpl = empleado.getCodigoempleadoSTR();
      }
      listaNovedadesSistema = null;
      getListaNovedadesSistema();
      log.info("ListaNovedades : " + listaNovedadesSistema);
      if (listaNovedadesSistema != null) {
         if (!listaNovedadesSistema.isEmpty()) {
            novedadSistemaSeleccionada = listaNovedadesSistema.get(0);
         }
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
      String pagActual = "detallevacacion";
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

   public String redirigirPaginaAnterior() {
      return paginaAnterior;
   }

   public void editarCelda() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (novedadSistemaSeleccionada != null) {
         editarNovedadSistema = novedadSistemaSeleccionada;
         if (cualCelda == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editFechaInicial");
            RequestContext.getCurrentInstance().execute("PF('editFechaInicial').show()");
            cualCelda = -1;
         } else if (cualCelda == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editPeriodo");
            RequestContext.getCurrentInstance().execute("PF('editPeriodo').show()");
            cualCelda = -1;
         } else if (cualCelda == 2) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editDias");
            RequestContext.getCurrentInstance().execute("PF('editDias').show()");
            cualCelda = -1;
         } else if (cualCelda == 3) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editFechaSiguiente");
            RequestContext.getCurrentInstance().execute("PF('editFechaSiguiente').show()");
            cualCelda = -1;
         } else if (cualCelda == 4) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editSubtipo");
            RequestContext.getCurrentInstance().execute("PF('editSubtipo').show()");
            cualCelda = -1;
         } else if (cualCelda == 5) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editAdelantoHasta");
            RequestContext.getCurrentInstance().execute("PF('editAdelantoHasta').show()");
            cualCelda = -1;
         } else if (cualCelda == 6) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editFechaPago");
            RequestContext.getCurrentInstance().execute("PF('editFechaPago').show()");
         } else if (cualCelda == 7) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarValorPagado");
            RequestContext.getCurrentInstance().execute("PF('editarValorPagado').show()");
         } else if (cualCelda == 8) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarDA");
            RequestContext.getCurrentInstance().execute("PF('editarDA').show()");
         }

      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void eventoFiltrar() {
      if (tipoLista == 0) {
         tipoLista = 1;
      }
      contarRegistros();
   }

   public void contarRegistros() {
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
   }

   public void contarRegistrosPeriodo() {
      RequestContext.getCurrentInstance().update("formularioDialogos:informacionRegistroPeriodo");
   }

   public void activarBotonLOV() {
      activarLOV = false;
      RequestContext.getCurrentInstance().update("form:listaValores");
   }

   public void anularBotonLOV() {
      activarLOV = true;
      RequestContext.getCurrentInstance().update("form:listaValores");
   }

   public void activarCtrlF11() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 0) {
         tamano = 270;
         colFechaIni = (Column) c.getViewRoot().findComponent("form:datosDetallesVacaciones:colFechaIni");
         colFechaIni.setFilterStyle("width: 85% !important;");
         colPeriodo = (Column) c.getViewRoot().findComponent("form:datosDetallesVacaciones:colPeriodo");
         colPeriodo.setFilterStyle("width: 85% !important;");
         colDias = (Column) c.getViewRoot().findComponent("form:datosDetallesVacaciones:colDias");
         colDias.setFilterStyle("width: 85% !important;");
         colFechaRegr = (Column) c.getViewRoot().findComponent("form:datosDetallesVacaciones:colFechaRegr");
         colFechaRegr.setFilterStyle("width: 85% !important;");
         colSubtipo = (Column) c.getViewRoot().findComponent("form:datosDetallesVacaciones:colSubtipo");
         colSubtipo.setFilterStyle("width: 85% !important;");
         colAdelanto = (Column) c.getViewRoot().findComponent("form:datosDetallesVacaciones:colAdelanto");
         colAdelanto.setFilterStyle("width: 85% !important;");
         colFechaPago = (Column) c.getViewRoot().findComponent("form:datosDetallesVacaciones:colFechaPago");
         colFechaPago.setFilterStyle("width: 85% !important;");
         colValorPago = (Column) c.getViewRoot().findComponent("form:datosDetallesVacaciones:colValorPago");
         colValorPago.setFilterStyle("width: 85% !important;");
         colDiasAplaz = (Column) c.getViewRoot().findComponent("form:datosDetallesVacaciones:colDiasAplaz");
         colDiasAplaz.setFilterStyle("width: 85% !important;");
         RequestContext.getCurrentInstance().update("form:datosDetallesVacaciones");
         filtrarrNovedadesSistema = null;
         bandera = 1;
         tipoLista = 1;
      } else if (bandera == 1) {
         tamano = 290;
         colFechaIni = (Column) c.getViewRoot().findComponent("form:datosDetallesVacaciones:colFechaIni");
         colFechaIni.setFilterStyle("display: none; visibility: hidden;");
         colPeriodo = (Column) c.getViewRoot().findComponent("form:datosDetallesVacaciones:colPeriodo");
         colPeriodo.setFilterStyle("display: none; visibility: hidden;");
         colDias = (Column) c.getViewRoot().findComponent("form:datosDetallesVacaciones:colDias");
         colDias.setFilterStyle("display: none; visibility: hidden;");
         colFechaRegr = (Column) c.getViewRoot().findComponent("form:datosDetallesVacaciones:colFechaRegr");
         colFechaRegr.setFilterStyle("display: none; visibility: hidden;");
         colSubtipo = (Column) c.getViewRoot().findComponent("form:datosDetallesVacaciones:colSubtipo");
         colSubtipo.setFilterStyle("display: none; visibility: hidden;");
         colAdelanto = (Column) c.getViewRoot().findComponent("form:datosDetallesVacaciones:colAdelanto");
         colAdelanto.setFilterStyle("display: none; visibility: hidden;");
         colFechaPago = (Column) c.getViewRoot().findComponent("form:datosDetallesVacaciones:colFechaPago");
         colFechaPago.setFilterStyle("display: none; visibility: hidden;");
         colValorPago = (Column) c.getViewRoot().findComponent("form:datosDetallesVacaciones:colValorPago");
         colValorPago.setFilterStyle("display: none; visibility: hidden;");
         colDiasAplaz = (Column) c.getViewRoot().findComponent("form:datosDetallesVacaciones:colDiasAplaz");
         colDiasAplaz.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosDetallesVacaciones");
         filtrarrNovedadesSistema = null;
         bandera = 0;
         tipoLista = 0;
      }
   }

   public void posicionOtro() {
      FacesContext context = FacesContext.getCurrentInstance();
      Map<String, String> map = context.getExternalContext().getRequestParameterMap();
      String celda = map.get("celda"); // name attribute of node 
      String registro = map.get("registro"); // type attribute of node 
      int indice = Integer.parseInt(registro);
      int columna = Integer.parseInt(celda);
      novedadSistemaSeleccionada = listaNovedadesSistema.get(indice);
      cambiarIndice(novedadSistemaSeleccionada, columna);
   }

   public void exportPDF() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:detallesNovedadesExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "DetallesVacaciones", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void exportXLS() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:detallesNovedadesExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "DetallesVacaciones", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void limpiarExportar() {
//        String cero = "0";
      novedadSistemaSeleccionada = new NovedadesSistema();
//        novedadSistemaSeleccionada.setSubtipo("TIEMPO");
//        novedadSistemaSeleccionada.setTipo("VACACION");
//        novedadSistemaSeleccionada.setVacacion(new Vacaciones());
//        novedadSistemaSeleccionada.setVacadiasaplazados(Short.valueOf(cero));
//        novedadSistemaSeleccionada.setDias(BigInteger.valueOf(0));
   }

   public void cambiarIndice(NovedadesSistema novedadS, int celda) {
      novedadSistemaSeleccionada = novedadS;
      cualCelda = celda;
      novedadSistemaSeleccionada.getSecuencia();
      if (cualCelda == 0) {
         anularBotonLOV();
         novedadSistemaSeleccionada.getFechainicialdisfrute();
      } else if (cualCelda == 1) {
         activarBotonLOV();
         novedadSistemaSeleccionada.getVacacion().getPeriodo();
      } else if (cualCelda == 2) {
         anularBotonLOV();
         novedadSistemaSeleccionada.getDias();
      } else if (cualCelda == 3) {
         anularBotonLOV();
         novedadSistemaSeleccionada.getFechasiguientefinvaca();
      } else if (cualCelda == 5) {
         anularBotonLOV();
         novedadSistemaSeleccionada.getAdelantapagohasta();
      } else if (cualCelda == 6) {
         anularBotonLOV();
         novedadSistemaSeleccionada.getFechapago();
      } else if (cualCelda == 7) {
         anularBotonLOV();
         novedadSistemaSeleccionada.getValorpagado();
      } else if (cualCelda == 8) {
         anularBotonLOV();
         novedadSistemaSeleccionada.getVacadiasaplazados();
      }
   }

   public void asignarIndex(NovedadesSistema novedadsis, int dlg) {
      novedadSistemaSeleccionada = novedadsis;
      if (dlg == 1) {
         lovPeriodos = null;
         getLovPeriodos();
         contarRegistrosPeriodo();
         RequestContext.getCurrentInstance().update("formularioDialogos:periodosDialogo");
         RequestContext.getCurrentInstance().execute("PF('periodosDialogo').show()");
      }
   }

   public void listaValoresBoton() {
      if (cualCelda == 1) {
         lovPeriodos = null;
         getLovPeriodos();
         contarRegistrosPeriodo();
         RequestContext.getCurrentInstance().update("formularioDialogos:periodosDialogo");
         RequestContext.getCurrentInstance().execute("PF('periodosDialogo').show()");
      }
   }

   public void guardarCambiosNovedades() {
      if (guardado == false) {

         if (!modificarNovedadesSistema.isEmpty()) {
            for (int i = 0; i < modificarNovedadesSistema.size(); i++) {
               if (modificarNovedadesSistema.get(i).getVacacion() == null) {
                  modificarNovedadesSistema.get(i).setVacacion(new Vacaciones());
               }
               if (modificarNovedadesSistema.get(i).getFechasiguientefinvaca() == null) {
                  modificarNovedadesSistema.get(i).setFechasiguientefinvaca(null);
               }
               if (modificarNovedadesSistema.get(i).getFechapago() == null) {
                  modificarNovedadesSistema.get(i).setFechapago(null);
               }
               if (modificarNovedadesSistema.get(i).getAdelantapagohasta() == null) {
                  modificarNovedadesSistema.get(i).setAdelantapagohasta(null);
               }
               if (modificarNovedadesSistema.get(i).getVacadiasaplazados() == null) {
                  modificarNovedadesSistema.get(i).setVacadiasaplazados(null);
               }
               administrarNovedadesSistema.modificarNovedades(modificarNovedadesSistema.get(i));
            }
            modificarNovedadesSistema.clear();
         }
         log.info("Se guardaron los datos con exito");
         listaNovedadesSistema = null;
         getListaNovedadesSistema();
         contarRegistros();
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:datosDetallesVacaciones");
         guardado = true;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
         //  k = 0;
      }
   }

   public void modificarNovedadSistema(NovedadesSistema ns) {
      novedadSistemaSeleccionada = ns;
      if (modificarNovedadesSistema.isEmpty()) {
         modificarNovedadesSistema.add(novedadSistemaSeleccionada);
      } else if (!modificarNovedadesSistema.contains(novedadSistemaSeleccionada)) {
         modificarNovedadesSistema.add(novedadSistemaSeleccionada);
      }
      guardado = false;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      RequestContext.getCurrentInstance().update("form:datosDetallesVacaciones");
   }

   public void activarAceptar() {
      aceptar = false;
   }

   public void mostrarDialogoProceso() {
      RequestContext.getCurrentInstance().execute("PF('proceso').show()");
   }

   public void verificarRastro() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (novedadSistemaSeleccionada != null) {
         int result = administrarRastros.obtenerTabla(novedadSistemaSeleccionada.getSecuencia(), "NOVEDADESSISTEMA");
         if (result == 1) {
            RequestContext.getCurrentInstance().execute("PF('errorObjetosDB').show()");
         } else if (result == 2) {
            RequestContext.getCurrentInstance().execute("PF('confirmarRastro').show()");
         } else if (result == 3) {
            RequestContext.getCurrentInstance().execute("PF('errorRegistroRastro').show()");
         } else if (result == 4) {
            RequestContext.getCurrentInstance().execute("PF('errorTablaConRastro').show()");
         } else if (result == 5) {
            RequestContext.getCurrentInstance().execute("PF('errorTablaSinRastro').show()");
         }
      } else if (administrarRastros.verificarHistoricosTabla("NOVEDADESSISTEMA")) {
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
   }

   public void cancelarModificacion() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 1) {
         colFechaIni = (Column) c.getViewRoot().findComponent("form:datosDetallesVacaciones:colFechaIni");
         colFechaIni.setFilterStyle("display: none; visibility: hidden;");
         colPeriodo = (Column) c.getViewRoot().findComponent("form:datosDetallesVacaciones:colPeriodo");
         colPeriodo.setFilterStyle("display: none; visibility: hidden;");
         colDias = (Column) c.getViewRoot().findComponent("form:datosDetallesVacaciones:colDias");
         colDias.setFilterStyle("display: none; visibility: hidden;");
         colFechaRegr = (Column) c.getViewRoot().findComponent("form:datosDetallesVacaciones:colFechaRegr");
         colFechaRegr.setFilterStyle("display: none; visibility: hidden;");
         colSubtipo = (Column) c.getViewRoot().findComponent("form:datosDetallesVacaciones:colSubtipo");
         colSubtipo.setFilterStyle("display: none; visibility: hidden;");
         colAdelanto = (Column) c.getViewRoot().findComponent("form:datosDetallesVacaciones:colAdelanto");
         colAdelanto.setFilterStyle("display: none; visibility: hidden;");
         colFechaPago = (Column) c.getViewRoot().findComponent("form:datosDetallesVacaciones:colFechaPago");
         colFechaPago.setFilterStyle("display: none; visibility: hidden;");
         colValorPago = (Column) c.getViewRoot().findComponent("form:datosDetallesVacaciones:colValorPago");
         colValorPago.setFilterStyle("display: none; visibility: hidden;");
         colDiasAplaz = (Column) c.getViewRoot().findComponent("form:datosDetallesVacaciones:colDiasAplaz");
         colDiasAplaz.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosDetallesVacaciones");
         filtrarrNovedadesSistema = null;
         bandera = 0;
         tipoLista = 0;
      }
      aceptar = true;
      guardado = true;
      tipoLista = 0;
//        diasTotales = BigInteger.valueOf(0);
//        diasAplazadosTotal = Short.parseShort(cero);
      tamano = 290;
      modificarNovedadesSistema.clear();
      listaNovedadesSistema = null;
      getListaNovedadesSistema();
      lovPeriodos = null;
      novedadSistemaSeleccionada = null;
      anularBotonLOV();
      RequestContext.getCurrentInstance().update("form:datosDetallesVacaciones");
   }

   public void salir() {
      FacesContext c = FacesContext.getCurrentInstance();
      limpiarListasValor();
      if (bandera == 1) {
         colFechaIni = (Column) c.getViewRoot().findComponent("form:datosDetallesVacaciones:colFechaIni");
         colFechaIni.setFilterStyle("display: none; visibility: hidden;");
         colPeriodo = (Column) c.getViewRoot().findComponent("form:datosDetallesVacaciones:colPeriodo");
         colPeriodo.setFilterStyle("display: none; visibility: hidden;");
         colDias = (Column) c.getViewRoot().findComponent("form:datosDetallesVacaciones:colDias");
         colDias.setFilterStyle("display: none; visibility: hidden;");
         colFechaRegr = (Column) c.getViewRoot().findComponent("form:datosDetallesVacaciones:colFechaRegr");
         colFechaRegr.setFilterStyle("display: none; visibility: hidden;");
         colSubtipo = (Column) c.getViewRoot().findComponent("form:datosDetallesVacaciones:colSubtipo");
         colSubtipo.setFilterStyle("display: none; visibility: hidden;");
         colAdelanto = (Column) c.getViewRoot().findComponent("form:datosDetallesVacaciones:colAdelanto");
         colAdelanto.setFilterStyle("display: none; visibility: hidden;");
         colFechaPago = (Column) c.getViewRoot().findComponent("form:datosDetallesVacaciones:colFechaPago");
         colFechaPago.setFilterStyle("display: none; visibility: hidden;");
         colValorPago = (Column) c.getViewRoot().findComponent("form:datosDetallesVacaciones:colValorPago");
         colValorPago.setFilterStyle("display: none; visibility: hidden;");
         colDiasAplaz = (Column) c.getViewRoot().findComponent("form:datosDetallesVacaciones:colDiasAplaz");
         colDiasAplaz.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosDetallesVacaciones");
         filtrarrNovedadesSistema = null;
         bandera = 0;
         tipoLista = 0;
      }
      listaNovedadesSistema = null;
      aceptar = true;
      guardado = true;
      tipoLista = 0;
//        diasTotales = BigInteger.valueOf(0);
//        diasAplazadosTotal = Short.parseShort(cero);
      tamano = 290;
      modificarNovedadesSistema.clear();
      novedadSistemaSeleccionada = null;
      RequestContext.getCurrentInstance().update("form:datosDetallesVacaciones");
      navegar("atras");
   }

   public void actualizarPeriodos() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoActualizacion == 0) {
         novedadSistemaSeleccionada.getVacacion().setPeriodo(periodoSeleccionado.getPeriodo());
         if (modificarNovedadesSistema.isEmpty()) {
            modificarNovedadesSistema.add(novedadSistemaSeleccionada);
         } else if (!modificarNovedadesSistema.contains(novedadSistemaSeleccionada)) {
            modificarNovedadesSistema.add(novedadSistemaSeleccionada);
         }
         guardado = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         RequestContext.getCurrentInstance().update("form:datosDetallesVacaciones");
      }
      lovPeriodosFiltrar = null;
      periodoSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;
      context.reset("formularioDialogos:LOVPeriodos:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('LOVPeriodos').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('periodosDialogo').hide()");
      RequestContext.getCurrentInstance().update("formularioDialogos:LOVPeriodos");
      RequestContext.getCurrentInstance().update("formularioDialogos:periodosDialogo");
      RequestContext.getCurrentInstance().update("formularioDialogos:aceptarP");
   }

   public void cancelarCambioPeriodos() {
      lovPeriodosFiltrar = null;
      periodoSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formularioDialogos:LOVPeriodos:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('LOVPeriodos').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('periodosDialogo').hide()");
      RequestContext.getCurrentInstance().update("formularioDialogos:LOVPeriodos");
      RequestContext.getCurrentInstance().update("formularioDialogos:periodosDialogo");
      RequestContext.getCurrentInstance().update("formularioDialogos:aceptarP");

   }

   public void validaciones(NovedadesSistema novedads) {
      novedadSistemaSeleccionada = novedads;
      RequestContext context = RequestContext.getCurrentInstance();
      BigDecimal jornada = administrarNovedadesVacaciones.validarJornadaVacaciones(empleado.getSecuencia(), novedadSistemaSeleccionada.getFechainicialdisfrute());
      int diaSemana1;
      String diaSemanaStr = "";
      GregorianCalendar c = new GregorianCalendar();
      c.setTime(novedadSistemaSeleccionada.getFechainicialdisfrute());
      diaSemana1 = c.get(Calendar.DAY_OF_WEEK);
      switch (diaSemana1) {
         case 1:
            diaSemanaStr = "DOM";
            break;
         case 2:
            diaSemanaStr = "LUN";
            break;
         case 3:
            diaSemanaStr = "MAR";
            break;
         case 4:
            diaSemanaStr = "MIE";
            break;
         case 5:
            diaSemanaStr = "JUE";
            break;
         case 6:
            diaSemanaStr = "VIE";
            break;
         case 7:
            diaSemanaStr = "SAB";
            break;
      }
      if (novedadSistemaSeleccionada.getFechainicialdisfrute().before(fechaContratacionE)) {
         RequestContext.getCurrentInstance().update("formularioDialogos:validacion1");
         novedadSistemaSeleccionada.setFechainicialdisfrute(null);
         RequestContext.getCurrentInstance().update("form:datosDetallesVacaciones");
         RequestContext.getCurrentInstance().execute("PF('validacion1').show()");
      } else if (administrarNovedadesVacaciones.validarFestivoVacaciones(novedadSistemaSeleccionada.getFechainicialdisfrute(), jornada)) {
         RequestContext.getCurrentInstance().execute("PF('diaFestivo').show()");
      } else if (administrarNovedadesVacaciones.validarDiaLaboralVacaciones(jornada, diaSemanaStr) == true) {
         RequestContext.getCurrentInstance().execute("PF('diaNoLaboral').show()");
      } else {
         modificarNovedadSistema(novedadSistemaSeleccionada);
      }
   }

   public void diasMayor(BigInteger dias) {
      RequestContext context = RequestContext.getCurrentInstance();
      BigInteger backup = dias;

      if (novedadSistemaSeleccionada.getDias().compareTo(BigInteger.valueOf(15)) == 1) { //// si el resultado es uno e sporque nueva novedad . get dias es mayor a 15
         RequestContext.getCurrentInstance().update("formularioDialogos:diasMayor");
         novedadSistemaSeleccionada.setDias(BigInteger.ZERO);
         RequestContext.getCurrentInstance().update("form:datosDetallesVacaciones");
         RequestContext.getCurrentInstance().execute("PF('diasMayor').show()");
      } else {
         novedadSistemaSeleccionada.setDias(dias);
         fechaSiguiente();
         fechaAdelantoHasta(novedadSistemaSeleccionada.getFechasiguientefinvaca());
         RequestContext.getCurrentInstance().update("form:datosDetallesVacaciones");
      }
   }

   public Date fechaSiguiente() {
      Date fechaSiguiente = new Date();
      BigDecimal jornada = administrarNovedadesVacaciones.validarJornadaVacaciones(empleado.getSecuencia(), novedadSistemaSeleccionada.getFechainicialdisfrute());
      fechaSiguiente = administrarNovedadesVacaciones.fechaSiguiente(novedadSistemaSeleccionada.getFechainicialdisfrute(), novedadSistemaSeleccionada.getDias(), jornada);
      novedadSistemaSeleccionada.setFechasiguientefinvaca(fechaSiguiente);
      return fechaSiguiente;
   }

   public Date fechaAdelantoHasta(Date fechaRegreso) {
      Calendar cd = Calendar.getInstance();
      cd.setTime(fechaRegreso);
      cd.add(Calendar.DAY_OF_MONTH, -1);
      Date fechaAdelantoH = cd.getTime();
      novedadSistemaSeleccionada.setAdelantapagohasta(fechaAdelantoH);
      fechaslimite(fechaRegreso);
      return fechaAdelantoH;
   }

   public void fechaslimite(Date fecharegreso) {
      Date fechalimite = administrarNovedadesVacaciones.anteriorFechaLimite(fecharegreso, periodicidadCodigoDos);
      Date siguientelimite = administrarNovedadesVacaciones.despuesFechaLimite(fecharegreso, periodicidadCodigoDos);
   }

   //////////////////Gets y Sets
   public List<Vacaciones> getLovPeriodos() {
      if (lovPeriodos == null) {
         lovPeriodos = administrarDetalleVacacion.periodosEmpleado(empleado.getSecuencia());
      }
      return lovPeriodos;
   }

   public void setLovPeriodos(List<Vacaciones> lovPeriodos) {
      this.lovPeriodos = lovPeriodos;
   }

   public List<NovedadesSistema> getListaNovedadesSistema() {
      if (listaNovedadesSistema == null) {
         if (secuenciaVacacion != null) {
            if (empleado != null) {
               listaNovedadesSistema = administrarDetalleVacacion.novedadsistemaPorEmpleadoYVacacion(empleado.getSecuencia(), secuenciaVacacion);
            }
         }
      }
      return listaNovedadesSistema;
   }

   public void setListaNovedadesSistema(List<NovedadesSistema> listaNovedadesSistema) {
      this.listaNovedadesSistema = listaNovedadesSistema;
   }

   public List<NovedadesSistema> getFiltrarrNovedadesSistema() {
      return filtrarrNovedadesSistema;
   }

   public void setFiltrarrNovedadesSistema(List<NovedadesSistema> filtrarrNovedadesSistema) {
      this.filtrarrNovedadesSistema = filtrarrNovedadesSistema;
   }

   public NovedadesSistema getNovedadSistemaSeleccionada() {
      return novedadSistemaSeleccionada;
   }

   public void setNovedadSistemaSeleccionada(NovedadesSistema novedadSistemaSeleccionada) {
      this.novedadSistemaSeleccionada = novedadSistemaSeleccionada;
   }

   public List<Vacaciones> getLovPeriodosFiltrar() {
      return lovPeriodosFiltrar;
   }

   public void setLovPeriodosFiltrar(List<Vacaciones> lovPeriodosFiltrar) {
      this.lovPeriodosFiltrar = lovPeriodosFiltrar;
   }

   public Vacaciones getPeriodoSeleccionado() {
      return periodoSeleccionado;
   }

   public void setPeriodoSeleccionado(Vacaciones periodoSeleccionado) {
      this.periodoSeleccionado = periodoSeleccionado;
   }

   public Empleados getEmpleado() {
      return empleado;
   }

   public void setEmpleado(Empleados empleado) {
      this.empleado = empleado;
   }

   public boolean isAceptar() {
      return aceptar;
   }

   public void setAceptar(boolean aceptar) {
      this.aceptar = aceptar;
   }

   public String getInfoRegistro() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosDetallesVacaciones");
      infoRegistro = String.valueOf(tabla.getRowCount());
      return infoRegistro;
   }

   public void setInfoRegistro(String infoRegistro) {
      this.infoRegistro = infoRegistro;
   }

   public String getInfoRegistroPeriodo() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:LOVPeriodos");
      infoRegistroPeriodo = String.valueOf(tabla.getRowCount());
      return infoRegistroPeriodo;
   }

   public void setInfoRegistroPeriodo(String infoRegistroPeriodo) {
      this.infoRegistroPeriodo = infoRegistroPeriodo;
   }

   public int getTamano() {
      return tamano;
   }

   public void setTamano(int tamano) {
      this.tamano = tamano;
   }

   public boolean isActivarLOV() {
      return activarLOV;
   }

   public void setActivarLOV(boolean activarLOV) {
      this.activarLOV = activarLOV;
   }

   public boolean isGuardado() {
      return guardado;
   }

   public void setGuardado(boolean guardado) {
      this.guardado = guardado;
   }

   public String getPaginaAnterior() {
      return paginaAnterior;
   }

   public void setPaginaAnterior(String paginaAnterior) {
      this.paginaAnterior = paginaAnterior;
   }

   public String getNombreEmpl() {
      return nombreEmpl;
   }

   public void setNombreEmpl(String nombreEmpl) {
      this.nombreEmpl = nombreEmpl;
   }

   public String getCodEmpl() {
      return codEmpl;
   }

   public void setCodEmpl(String codEmpl) {
      this.codEmpl = codEmpl;
   }

   public NovedadesSistema getEditarNovedadSistema() {
      return editarNovedadSistema;
   }

   public void setEditarNovedadSistema(NovedadesSistema editarNovedadSistema) {
      this.editarNovedadSistema = editarNovedadSistema;
   }

}
