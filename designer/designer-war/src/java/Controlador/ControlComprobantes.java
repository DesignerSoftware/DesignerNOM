package Controlador;

import Entidades.DetallesFormulas;
import Entidades.Parametros;
import Entidades.ParametrosEstructuras;
import Entidades.SolucionesNodos;
import Exportar.ExportarPDFTablasAnchas;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarComprobantesInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import ControlNavegacion.ControlListaNavegacion;
import java.util.Map;
import java.util.LinkedHashMap;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.primefaces.component.column.Column;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.export.Exporter;
import org.primefaces.context.RequestContext;

@ManagedBean
@SessionScoped
public class ControlComprobantes implements Serializable {

   @EJB
   AdministrarComprobantesInterface administrarComprobantes;

   private List<Parametros> listaParametros;
   private Parametros parametroActual;
   private ParametrosEstructuras parametroEstructura;
   private List<Parametros> listaParametrosLOV;
   private List<Parametros> filtradoListaParametrosLOV;
   private Parametros parametroSeleccionado;
   //SOLUCIONES NODOS EMPLEADO
   private List<SolucionesNodos> listaSolucionesNodosEmpleado;
   private List<SolucionesNodos> filtradolistaSolucionesNodosEmpleado;
   private SolucionesNodos solucionNodoEmpleadoSeleccionada;
   private SolucionesNodos editarSolucionNodo;
   //SOLUCIONES NODOS EMPLEADOR
   private List<SolucionesNodos> listaSolucionesNodosEmpleador;
   private List<SolucionesNodos> filtradolistaSolucionesNodosEmpleador;
   private SolucionesNodos solucionNodoEmpleadorSeleccionada;
   //REGISTRO ACTUAL
   private int registroActual, tablaActual, index;
   //OTROS
   private boolean aceptar, mostrarTodos;
   private Locale locale = new Locale("es", "CO");
   private NumberFormat nf = NumberFormat.getCurrencyInstance(locale);
   //SUBTOTALES
   private BigDecimal subtotalPago, subtotalDescuento, subtotalPasivo, subtotalGasto, neto;
   private String pago, descuento, pasivo, gasto, netoTotal;
   //FILTRADO
   private Column codigoSNE, descipcionSNE, unidadSNE, pagoSNE, descuentoSNE, terceroSNE, fechaHastaSNE, debitoSNE, centroCostoDSNE, creditoSNE, centroCostoCSNE, saldoSNE, fechaDesdeSNE, fechaPagoSNE, FechaModificacioSNE;
   private Column codigoSNER, descipcionSNER, unidadSNER, pasivoSNER, gastoSNER, terceroSNER, fechaHastaSNER, debitoSNER, centroCostoDSNER, creditoSNER, centroCostoCSNER, saldoSNER, fechaDesdeSNER, fechaPagoSNER, FechaModificacioSNER;
   private int bandera;
   private String altoScrollSolucionesNodosEmpleado, altoScrollSolucionesNodosEmpleador;
   //PARCIALES 
   private String parcialesSolucionNodos;
   //DETALLES FORMULAS
   private List<DetallesFormulas> listaDetallesFormulas;
   //FORMATO FECHAS
   private SimpleDateFormat formatoFecha;
   private boolean estadoBtnArriba, estadoBtnAbajo;
   //
   private String infoRegistroEmpleado, infoRegistroComprobante, infoRegistroComprobanteEmpleador, infoRegistroComprobanteEmpleado;
   private Parametros editarParametros;
   private int tipoLista, cualCelda;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

   public ControlComprobantes() {
      registroActual = 0;
      aceptar = true;
      mostrarTodos = true;
      subtotalPago = new BigDecimal(0);
      subtotalDescuento = new BigDecimal(0);
      subtotalPasivo = new BigDecimal(0);
      subtotalGasto = new BigDecimal(0);
      bandera = 0;
      altoScrollSolucionesNodosEmpleado = "95";
      altoScrollSolucionesNodosEmpleador = "95";
      listaDetallesFormulas = null;
      formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
      estadoBtnArriba = false;
      estadoBtnAbajo = false;
      parametroActual = null;
      pasivo = "0";
      gasto = "0";
      pago = "0";
      descuento = "0";
      netoTotal = "0";
      listaParametrosLOV = new ArrayList<Parametros>();
      editarParametros = new Parametros();
      tipoLista = 0;
      solucionNodoEmpleadoSeleccionada = null;
      solucionNodoEmpleadorSeleccionada = null;
      editarSolucionNodo = new SolucionesNodos();
      registroActual = 0;
      listaSolucionesNodosEmpleado = null;
      listaSolucionesNodosEmpleador = null;
      listaParametros = new ArrayList<Parametros>();
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
String pagActual = "comprobante";
         
         
         


         
         
         
         
         
         
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
         administrarComprobantes.obtenerConexion(ses.getId());
      } catch (Exception e) {
         System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
         System.out.println("Causa: " + e.getCause());
      }
   }

   public void refrescar() {
      solucionNodoEmpleadoSeleccionada = null;
      solucionNodoEmpleadorSeleccionada = null;
      listaParametros.clear();
      listaSolucionesNodosEmpleado = null;
      listaSolucionesNodosEmpleador = null;
//      getListaSolucionesNodosEmpleado();
//      getListaSolucionesNodosEmpleador();
      getListaParametros();
      RequestContext context = RequestContext.getCurrentInstance();
      context.update("form:datosSolucionesNodosEmpleado");
      context.update("form:datosSolucionesNodosEmpleador");
   }

   public void anteriorEmpleado() {
      if (registroActual > 0) {
         registroActual--;
         parametroActual = listaParametros.get(registroActual);
         listaSolucionesNodosEmpleado = null;
         listaSolucionesNodosEmpleador = null;
         getListaSolucionesNodosEmpleado();
         getListaSolucionesNodosEmpleador();
         if (registroActual == 0) {
            estadoBtnArriba = true;
         }
         if (registroActual < (listaParametros.size() - 1)) {
            estadoBtnAbajo = false;
         }
         RequestContext context = RequestContext.getCurrentInstance();
         context.update("form:panelInf");
         context.update("form:datosSolucionesNodosEmpleado");
         context.update("form:datosSolucionesNodosEmpleador");
         context.update("form:btnArriba");
         context.update("form:btnAbajo");
         context.update("form:pagocomprobante");
         context.update("form:descuentocomprobante");
         context.update("form:netocomprobante");
         context.update("form:pasivocomprobantes");
         context.update("form:gastocomprobantes");
      }
      contarRegistrosComprobanteEmpleado();
      contarRegistrosComprobanteEmpleador();
   }

   public void siguienteEmpleado() {
      if (registroActual < (listaParametros.size() - 1)) {
         registroActual++;
         parametroActual = listaParametros.get(registroActual);
         listaSolucionesNodosEmpleado = null;
         listaSolucionesNodosEmpleador = null;
         getListaSolucionesNodosEmpleado();
         getListaSolucionesNodosEmpleador();
         if (registroActual > 0) {
            estadoBtnArriba = false;
         }
         if (registroActual == (listaParametros.size() - 1)) {
            estadoBtnAbajo = true;
         }
         RequestContext context = RequestContext.getCurrentInstance();
         context.update("form:panelInf");
         context.update("form:datosSolucionesNodosEmpleado");
         context.update("form:datosSolucionesNodosEmpleador");
         context.update("form:btnArriba");
         context.update("form:btnAbajo");
         context.update("form:pagocomprobante");
         context.update("form:descuentocomprobante");
         context.update("form:netocomprobante");
         context.update("form:pasivocomprobantes");
         context.update("form:gastocomprobantes");
      }
      contarRegistrosComprobanteEmpleado();
      contarRegistrosComprobanteEmpleador();
   }

   public void seleccionarEmpleado() {
      listaParametros.clear();
      listaParametros.add(parametroSeleccionado);
      parametroActual = parametroSeleccionado;
      registroActual = 0;
      filtradoListaParametrosLOV = null;
      parametroSeleccionado = null;
      aceptar = true;
      mostrarTodos = false;
      listaSolucionesNodosEmpleado = null;
      listaSolucionesNodosEmpleador = null;
      getListaSolucionesNodosEmpleado();
      getListaSolucionesNodosEmpleador();
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:panelInf");
      RequestContext.getCurrentInstance().update("form:datosSolucionesNodosEmpleado");
      RequestContext.getCurrentInstance().update("form:datosSolucionesNodosEmpleador");

      RequestContext.getCurrentInstance().update("formularioDialogos:buscarEmpleadoDialogo");
      RequestContext.getCurrentInstance().update("formularioDialogos:lovEmpleados");
      RequestContext.getCurrentInstance().update("formularioDialogos:aceptarP");
      context.reset("formularioDialogos:lovEmpleados:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovEmpleados').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('buscarEmpleadoDialogo').hide()");
   }

   public void cancelarSeleccionEmpleado() {
      filtradoListaParametrosLOV = null;
      parametroSeleccionado = null;
      aceptar = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formularioDialogos:lovEmpleados:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovEmpleados').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('buscarEmpleadoDialogo').hide()");
   }

   public void mostarTodosEmpleados() {
      registroActual = 0;
      listaParametros.clear();
      parametroActual = null;
      listaParametrosLOV = null;
      getParametroActual();
      listaSolucionesNodosEmpleado = null;
      listaSolucionesNodosEmpleador = null;
      getListaSolucionesNodosEmpleado();
      getListaSolucionesNodosEmpleador();
      contarRegistrosComprobanteEmpleado();
      contarRegistrosComprobanteEmpleador();
      mostrarTodos = true;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:panelInf");
      RequestContext.getCurrentInstance().update("form:datosSolucionesNodosEmpleado");
      RequestContext.getCurrentInstance().update("form:datosSolucionesNodosEmpleador");
   }

   //CTRL + F11 ACTIVAR/DESACTIVAR
   public void activarCtrlF11() {
      if (bandera == 0) {
         //SOLUCIONES NODOS EMPLEADO
         codigoSNE = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleado:codigoSNE");
         codigoSNE.setFilterStyle("width: 85% !important");
         descipcionSNE = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleado:descipcionSNE");
         descipcionSNE.setFilterStyle("width: 85% !important");
         unidadSNE = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleado:unidadSNE");
         unidadSNE.setFilterStyle("width: 85% !important");
         pagoSNE = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleado:pagoSNE");
         pagoSNE.setFilterStyle("width: 85% !important");
         descuentoSNE = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleado:descuentoSNE");
         descuentoSNE.setFilterStyle("width: 85% !important");
         terceroSNE = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleado:terceroSNE");
         terceroSNE.setFilterStyle("width: 85% !important");
         fechaHastaSNE = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleado:fechaHastaSNE");
         fechaHastaSNE.setFilterStyle("width: 85% !important");
         debitoSNE = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleado:debitoSNE");
         debitoSNE.setFilterStyle("width: 85% !important");
         centroCostoDSNE = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleado:centroCostoDSNE");
         centroCostoDSNE.setFilterStyle("width: 85% !important");
         creditoSNE = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleado:creditoSNE");
         creditoSNE.setFilterStyle("width: 85% !important");
         centroCostoCSNE = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleado:centroCostoCSNE");
         centroCostoCSNE.setFilterStyle("width: 85% !important");
         saldoSNE = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleado:saldoSNE");
         saldoSNE.setFilterStyle("width: 85% !important");
         fechaDesdeSNE = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleado:fechaDesdeSNE");
         fechaDesdeSNE.setFilterStyle("width: 85% !important");
         fechaPagoSNE = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleado:fechaPagoSNE");
         fechaPagoSNE.setFilterStyle("width: 85% !important");
         FechaModificacioSNE = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleado:FechaModificacioSNE");
         FechaModificacioSNE.setFilterStyle("width: 85% !important");

         //SOLUCIONES NODOS EMPLEADOR
         codigoSNER = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleador:codigoSNER");
         codigoSNER.setFilterStyle("width: 85% !important");
         descipcionSNER = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleador:descipcionSNER");
         descipcionSNER.setFilterStyle("width: 85% !important");
         unidadSNER = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleador:unidadSNER");
         unidadSNER.setFilterStyle("width: 85% !important");
         pasivoSNER = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleador:pasivoSNER");
         pasivoSNER.setFilterStyle("width: 85% !important");
         gastoSNER = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleador:gastoSNER");
         gastoSNER.setFilterStyle("width: 85% !important");
         terceroSNER = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleador:terceroSNER");
         terceroSNER.setFilterStyle("width: 85% !important");
         fechaHastaSNER = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleador:fechaHastaSNER");
         fechaHastaSNER.setFilterStyle("width: 85% !important");
         debitoSNER = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleador:debitoSNER");
         debitoSNER.setFilterStyle("width: 85% !important");
         centroCostoDSNER = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleador:centroCostoDSNER");
         centroCostoDSNER.setFilterStyle("width: 85% !important");
         creditoSNER = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleador:creditoSNER");
         creditoSNER.setFilterStyle("width: 85% !important");
         centroCostoCSNER = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleador:centroCostoCSNER");
         centroCostoCSNER.setFilterStyle("width: 85% !important");
         saldoSNER = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleador:saldoSNER");
         saldoSNER.setFilterStyle("width: 85% !important");
         fechaDesdeSNER = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleador:fechaDesdeSNER");
         fechaDesdeSNER.setFilterStyle("width: 85% !important");
         fechaPagoSNER = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleador:fechaPagoSNER");
         fechaPagoSNER.setFilterStyle("width: 85% !important");
         FechaModificacioSNER = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleador:FechaModificacioSNER");
         FechaModificacioSNER.setFilterStyle("width: 85% !important");

         altoScrollSolucionesNodosEmpleado = "75";
         altoScrollSolucionesNodosEmpleador = "75";
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:datosSolucionesNodosEmpleado");
         RequestContext.getCurrentInstance().update("form:datosSolucionesNodosEmpleador");
         bandera = 1;

      } else if (bandera == 1) {
         //SOLUCIONES NODOS EMPLEADO
         codigoSNE = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleado:codigoSNE");
         codigoSNE.setFilterStyle("display: none; visibility: hidden;");
         descipcionSNE = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleado:descipcionSNE");
         descipcionSNE.setFilterStyle("display: none; visibility: hidden;");
         unidadSNE = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleado:unidadSNE");
         unidadSNE.setFilterStyle("display: none; visibility: hidden;");
         pagoSNE = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleado:pagoSNE");
         pagoSNE.setFilterStyle("display: none; visibility: hidden;");
         descuentoSNE = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleado:descuentoSNE");
         descuentoSNE.setFilterStyle("display: none; visibility: hidden;");
         terceroSNE = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleado:terceroSNE");
         terceroSNE.setFilterStyle("display: none; visibility: hidden;");
         fechaHastaSNE = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleado:fechaHastaSNE");
         fechaHastaSNE.setFilterStyle("display: none; visibility: hidden;");
         debitoSNE = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleado:debitoSNE");
         debitoSNE.setFilterStyle("display: none; visibility: hidden;");
         centroCostoDSNE = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleado:centroCostoDSNE");
         centroCostoDSNE.setFilterStyle("display: none; visibility: hidden;");
         creditoSNE = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleado:creditoSNE");
         creditoSNE.setFilterStyle("display: none; visibility: hidden;");
         centroCostoCSNE = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleado:centroCostoCSNE");
         centroCostoCSNE.setFilterStyle("display: none; visibility: hidden;");
         saldoSNE = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleado:saldoSNE");
         saldoSNE.setFilterStyle("display: none; visibility: hidden;");
         fechaDesdeSNE = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleado:fechaDesdeSNE");
         fechaDesdeSNE.setFilterStyle("display: none; visibility: hidden;");
         fechaPagoSNE = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleado:fechaPagoSNE");
         fechaPagoSNE.setFilterStyle("display: none; visibility: hidden;");
         FechaModificacioSNE = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleado:FechaModificacioSNE");
         FechaModificacioSNE.setFilterStyle("display: none; visibility: hidden;");

         //SOLUCIONES NODOS EMPLEADOR
         codigoSNER = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleador:codigoSNER");
         codigoSNER.setFilterStyle("display: none; visibility: hidden;");
         descipcionSNER = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleador:descipcionSNER");
         descipcionSNER.setFilterStyle("display: none; visibility: hidden;");
         unidadSNER = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleador:unidadSNER");
         unidadSNER.setFilterStyle("display: none; visibility: hidden;");
         pasivoSNER = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleador:pasivoSNER");
         pasivoSNER.setFilterStyle("display: none; visibility: hidden;");
         gastoSNER = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleador:gastoSNER");
         gastoSNER.setFilterStyle("display: none; visibility: hidden;");
         terceroSNER = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleador:terceroSNER");
         terceroSNER.setFilterStyle("display: none; visibility: hidden;");
         fechaHastaSNER = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleador:fechaHastaSNER");
         fechaHastaSNER.setFilterStyle("display: none; visibility: hidden;");
         debitoSNER = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleador:debitoSNER");
         debitoSNER.setFilterStyle("display: none; visibility: hidden;");
         centroCostoDSNER = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleador:centroCostoDSNER");
         centroCostoDSNER.setFilterStyle("display: none; visibility: hidden;");
         creditoSNER = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleador:creditoSNER");
         creditoSNER.setFilterStyle("display: none; visibility: hidden;");
         centroCostoCSNER = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleador:centroCostoCSNER");
         centroCostoCSNER.setFilterStyle("display: none; visibility: hidden;");
         saldoSNER = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleador:saldoSNER");
         saldoSNER.setFilterStyle("display: none; visibility: hidden;");
         fechaDesdeSNER = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleador:fechaDesdeSNER");
         fechaDesdeSNER.setFilterStyle("display: none; visibility: hidden;");
         fechaPagoSNER = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleador:fechaPagoSNER");
         fechaPagoSNER.setFilterStyle("display: none; visibility: hidden;");
         FechaModificacioSNER = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleador:FechaModificacioSNER");
         FechaModificacioSNER.setFilterStyle("display: none; visibility: hidden;");

         altoScrollSolucionesNodosEmpleado = "95";
         altoScrollSolucionesNodosEmpleador = "95";
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:datosSolucionesNodosEmpleado");
         RequestContext.getCurrentInstance().update("form:datosSolucionesNodosEmpleador");
         bandera = 0;
         filtradolistaSolucionesNodosEmpleado = null;
         filtradolistaSolucionesNodosEmpleador = null;
      }
   }

   public void exportPDF() throws IOException {
      DataTable tabla;
      Exporter exporter = new ExportarPDFTablasAnchas();
      FacesContext context = FacesContext.getCurrentInstance();
      tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosSolucionesNodosEmpleadoExportar");
      exporter.export(context, tabla, "SolucionesNodosEmpleadoPDF", false, false, "UTF-8", null, null);
      context.responseComplete();
      tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosSolucionesNodosEmpleadorExportar");
      exporter.export(context, tabla, "SolucionesNodosEmpleadorPDF", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void exportXLS() throws IOException {
      DataTable tabla;
      Exporter exporter = new ExportarXLS();
      FacesContext context = FacesContext.getCurrentInstance();
      tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosSolucionesNodosEmpleadoExportar");
      exporter.export(context, tabla, "SolucionesNodosEmpleadoXLS", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   //PARCIALES CONCEPTO
   public void parcialSolucionNodo(SolucionesNodos solucionNodo, int celda) {
      solucionNodoEmpleadoSeleccionada = solucionNodo;
      cualCelda = celda;
      solucionNodoEmpleadoSeleccionada.getSecuencia();
      if (cualCelda == 0) {
         parcialesSolucionNodos = solucionNodoEmpleadoSeleccionada.getParciales();
      }
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("formularioDialogos:parcialesConcepto");
      RequestContext.getCurrentInstance().execute("PF('parcialesConcepto').show();");
   }

   public void parcialSolucionNodoEmpleador(SolucionesNodos solucionNodoEmpleador, int celda) {
      solucionNodoEmpleadorSeleccionada = solucionNodoEmpleador;
      cualCelda = celda;
      solucionNodoEmpleadorSeleccionada.getSecuencia();
      if (cualCelda == 0) {
         parcialesSolucionNodos = solucionNodoEmpleadorSeleccionada.getParciales();
      }
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("formularioDialogos:parcialesConcepto");
      RequestContext.getCurrentInstance().execute("PF('parcialesConcepto').show();");
   }

   public void cambiarIndiceEmpleador(SolucionesNodos solucionNodoEmpleador, int celda) {
      solucionNodoEmpleadorSeleccionada = solucionNodoEmpleador;
      cualCelda = celda;
      solucionNodoEmpleadorSeleccionada.getSecuencia();
      if (cualCelda == 1) {
         solucionNodoEmpleadorSeleccionada.getCodigoconcepto();
      } else if (cualCelda == 2) {
         solucionNodoEmpleadorSeleccionada.getNombreconcepto();
      } else if (cualCelda == 3) {
         solucionNodoEmpleadorSeleccionada.getUnidades();
      } else if (cualCelda == 4) {
         solucionNodoEmpleadorSeleccionada.getPasivo();
      } else if (cualCelda == 5) {
         solucionNodoEmpleadorSeleccionada.getGasto();
      } else if (cualCelda == 6) {
         solucionNodoEmpleadorSeleccionada.getNombretercero();
      } else if (cualCelda == 7) {
         solucionNodoEmpleadorSeleccionada.getFechahasta();
      } else if (cualCelda == 8) {
         solucionNodoEmpleadorSeleccionada.getCodigocuentad();
      } else if (cualCelda == 9) {
         solucionNodoEmpleadorSeleccionada.getNombrecentrocostod();
      } else if (cualCelda == 10) {
         solucionNodoEmpleadorSeleccionada.getCodigocuentac();
      } else if (cualCelda == 11) {
         solucionNodoEmpleadorSeleccionada.getNombrecentrocostoc();
      } else if (cualCelda == 12) {
         solucionNodoEmpleadorSeleccionada.getSaldo();
      } else if (cualCelda == 13) {
         solucionNodoEmpleadorSeleccionada.getFechadesde();
      } else if (cualCelda == 14) {
         solucionNodoEmpleadorSeleccionada.getFechapago();
      } else if (cualCelda == 15) {
         solucionNodoEmpleadorSeleccionada.getUltimamodificacion();
      }
   }

   public void cambiarIndice(SolucionesNodos solucionNodo, int celda) {
      solucionNodoEmpleadoSeleccionada = solucionNodo;
      cualCelda = celda;
      solucionNodoEmpleadoSeleccionada.getSecuencia();
      if (cualCelda == 1) {
         solucionNodoEmpleadoSeleccionada.getCodigoconcepto();
      } else if (cualCelda == 2) {
         solucionNodoEmpleadoSeleccionada.getNombreconcepto();
      } else if (cualCelda == 3) {
         solucionNodoEmpleadoSeleccionada.getUnidades();
      } else if (cualCelda == 4) {
         solucionNodoEmpleadoSeleccionada.getPago();
      } else if (cualCelda == 5) {
         solucionNodoEmpleadoSeleccionada.getDescuento();
      } else if (cualCelda == 6) {
         solucionNodoEmpleadoSeleccionada.getNombretercero();
      } else if (cualCelda == 7) {
         solucionNodoEmpleadoSeleccionada.getFechahasta();
      } else if (cualCelda == 8) {
         solucionNodoEmpleadoSeleccionada.getCodigocuentad();
      } else if (cualCelda == 9) {
         solucionNodoEmpleadoSeleccionada.getNombrecentrocostod();
      } else if (cualCelda == 10) {
         solucionNodoEmpleadoSeleccionada.getCodigocuentad();
      } else if (cualCelda == 11) {
         solucionNodoEmpleadoSeleccionada.getNombrecentrocostoc();
      } else if (cualCelda == 12) {
         solucionNodoEmpleadoSeleccionada.getSaldo();
      } else if (cualCelda == 13) {
         solucionNodoEmpleadoSeleccionada.getFechadesde();
      } else if (cualCelda == 14) {
         solucionNodoEmpleadoSeleccionada.getFechapago();
      } else if (cualCelda == 15) {
         solucionNodoEmpleadoSeleccionada.getUltimamodificacion();
      }
   }

   public void verDetallesFormula() {
      listaDetallesFormulas = null;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("formularioDialogos:detallesFormulas");
      RequestContext.getCurrentInstance().execute("PF('detallesFormulas').show();");
   }

   public void salir() {
      limpiarListasValor();
      parametroActual = null;
      listaSolucionesNodosEmpleado = null;
      listaSolucionesNodosEmpleador = null;
      parametroEstructura = null;
      registroActual = 0;
      estadoBtnArriba = false;
      estadoBtnAbajo = false;
      navegar("atras");
   }

   public void activarAceptar() {
      aceptar = false;
   }

   public void contarRegistrosEmpleado() {
      RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroEmpleado");
   }

   public void contarRegistrosComprobanteEmpleador() {
      RequestContext.getCurrentInstance().update("form:infoRegistroComprobanteEmpleador");
   }

   public void contarRegistrosComprobanteEmpleado() {
      RequestContext.getCurrentInstance().update("form:infoRegistroComprobanteEmpleado");
   }

   public void eventoFiltrarComprobanteEmpleado() {
      solucionNodoEmpleadoSeleccionada = null;
      contarRegistrosComprobanteEmpleado();
   }

   public void eventoFiltrarComprobanteEmpleador() {
      solucionNodoEmpleadorSeleccionada = null;
      contarRegistrosComprobanteEmpleador();
   }

   public void editarCelda() {
      if (tablaActual == 0) {
         if (solucionNodoEmpleadoSeleccionada != null) {
            RequestContext context = RequestContext.getCurrentInstance();
            if (cualCelda == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editarCodEmpleado");
               RequestContext.getCurrentInstance().execute("PF('editarCodEmpleado').show()");
               cualCelda = -1;
            } else if (cualCelda == 2) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editarDescConcepto");
               RequestContext.getCurrentInstance().execute("PF('editarDescConcepto').show()");
               cualCelda = -1;
            } else if (cualCelda == 3) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editarUnd");
               RequestContext.getCurrentInstance().execute("PF('editarUnd').show()");
               cualCelda = -1;
            } else if (cualCelda == 4) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editarPago");
               RequestContext.getCurrentInstance().execute("PF('editarPago').show()");
               cualCelda = -1;
            } else if (cualCelda == 5) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editarDescuento");
               RequestContext.getCurrentInstance().execute("PF('editarDescuento').show()");
               cualCelda = -1;
            } else if (cualCelda == 6) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editarTercero");
               RequestContext.getCurrentInstance().execute("PF('editarTercero').show()");
               cualCelda = -1;
            } else if (cualCelda == 7) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaHasta");
               RequestContext.getCurrentInstance().execute("PF('editarFechaHasta').show()");
               cualCelda = -1;
            } else if (cualCelda == 8) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editarDebito");
               RequestContext.getCurrentInstance().execute("PF('editarDebito').show()");
               cualCelda = -1;
            } else if (cualCelda == 9) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editarCCDebito");
               RequestContext.getCurrentInstance().execute("PF('editarCCDebito').show()");
               cualCelda = -1;
            } else if (cualCelda == 10) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editarCredito");
               RequestContext.getCurrentInstance().execute("PF('editarCredito').show()");
               cualCelda = -1;
            } else if (cualCelda == 11) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editarCCCredito");
               RequestContext.getCurrentInstance().execute("PF('editarCCCredito').show()");
               cualCelda = -1;
            } else if (cualCelda == 12) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editarSaldo");
               RequestContext.getCurrentInstance().execute("PF('editarSaldo').show()");
               cualCelda = -1;
            } else if (cualCelda == 13) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaDesde");
               RequestContext.getCurrentInstance().execute("PF('editarFechaDesde').show()");
               cualCelda = -1;
            } else if (cualCelda == 14) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaPago");
               RequestContext.getCurrentInstance().execute("PF('editarFechaPago').show()");
               cualCelda = -1;
            } else if (cualCelda == 15) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaModificacion");
               RequestContext.getCurrentInstance().execute("PF('formularioDialogos:editarFechaModificacion').show()");
               cualCelda = -1;
            } // } 
         } else {
            RequestContext.getCurrentInstance().execute("formularioDialogos:seleccionarRegistro').show()");
         }
      } else if (tablaActual == 1) {
         RequestContext context = RequestContext.getCurrentInstance();
         if (cualCelda == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarCodEmpleadoEmpleador");
            RequestContext.getCurrentInstance().execute("PF('editarCodEmpleadoEmpleador').show()");
            cualCelda = -1;
         } else if (cualCelda == 2) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarDescConceptoEmpleador");
            RequestContext.getCurrentInstance().execute("PF('editarDescConceptoEmpleador').show()");
            cualCelda = -1;
         } else if (cualCelda == 3) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarUndEmpleador");
            RequestContext.getCurrentInstance().execute("PF('editarUndEmpleador').show()");
            cualCelda = -1;
         } else if (cualCelda == 4) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarPasivo");
            RequestContext.getCurrentInstance().execute("PF('editarPasivo').show()");
            cualCelda = -1;
         } else if (cualCelda == 5) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarGasto");
            RequestContext.getCurrentInstance().execute("PF('editarGasto').show()");
            cualCelda = -1;
         } else if (cualCelda == 6) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarTerceroEmpleador");
            RequestContext.getCurrentInstance().execute("PF('editarTerceroEmpleador').show()");
            cualCelda = -1;
         } else if (cualCelda == 7) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaHastaEmpleador");
            RequestContext.getCurrentInstance().execute("PF('editarFechaHastaEmpleador').show()");
            cualCelda = -1;
         } else if (cualCelda == 8) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarDebitoEmpleador");
            RequestContext.getCurrentInstance().execute("PF('editarDebitoEmpleador').show()");
            cualCelda = -1;
         } else if (cualCelda == 9) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarCCDebitoEmpleador");
            RequestContext.getCurrentInstance().execute("PF('editarCCDebitoEmpleador').show()");
            cualCelda = -1;
         } else if (cualCelda == 10) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarCreditoEmpleador");
            RequestContext.getCurrentInstance().execute("PF('editarCreditoEmpleador').show()");
            cualCelda = -1;
         } else if (cualCelda == 11) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarCCCreditoEmpleador");
            RequestContext.getCurrentInstance().execute("PF('editarCCCreditoEmpleador').show()");
            cualCelda = -1;
         } else if (cualCelda == 12) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarSaldoEmpleador");
            RequestContext.getCurrentInstance().execute("PF('editarSaldoEmpleador').show()");
            cualCelda = -1;
         } else if (cualCelda == 13) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaDesdeEmpleador");
            RequestContext.getCurrentInstance().execute("PF('editarFechaDesdeEmpleador').show()");
            cualCelda = -1;
         } else if (cualCelda == 14) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaPagoEmpleador");
            RequestContext.getCurrentInstance().execute("PF('editarFechaPagoEmpleador').show()");
            cualCelda = -1;
         } else if (cualCelda == 15) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaModificacionEmpleador");
            RequestContext.getCurrentInstance().execute("PF('formularioDialogos:editarFechaModificacionEmpleador').show()");
            cualCelda = -1;
         } // } 
      } else {
         RequestContext.getCurrentInstance().execute("formularioDialogos:seleccionarRegistro').show()");
      }
   }

   public void posicionOtro() {
      FacesContext context = FacesContext.getCurrentInstance();
      Map<String, String> map = context.getExternalContext().getRequestParameterMap();
      String celda = map.get("celda"); // name attribute of node 
      String registro = map.get("registro"); // type attribute of node 
      int indice = Integer.parseInt(registro);
      int columna = Integer.parseInt(celda);
      solucionNodoEmpleadoSeleccionada = listaSolucionesNodosEmpleado.get(indice);
      cambiarIndice(solucionNodoEmpleadoSeleccionada, columna);
   }

   public void posicionOtroEmpleador() {
      FacesContext context = FacesContext.getCurrentInstance();
      Map<String, String> mapEm = context.getExternalContext().getRequestParameterMap();
      String celda = mapEm.get("celda"); // name attribute of node 
      String registro = mapEm.get("registro"); // type attribute of node 
      int indice = Integer.parseInt(registro);
      int columna = Integer.parseInt(celda);
      solucionNodoEmpleadorSeleccionada = listaSolucionesNodosEmpleador.get(indice);
      cambiarIndice(solucionNodoEmpleadorSeleccionada, columna);

   }

   //GETTER AND SETTER
   public List<Parametros> getListaParametros() {
      listaParametros = administrarComprobantes.consultarParametrosComprobantesActualUsuario();
      if (listaParametros.isEmpty()) {
         estadoBtnArriba = true;
         estadoBtnAbajo = true;
      } else {
         estadoBtnArriba = false;
         estadoBtnAbajo = false;
      }
      return listaParametros;
   }

   public void setListaParametros(List<Parametros> listaParametros) {
      this.listaParametros = listaParametros;
   }

   public Parametros getParametroActual() {
      if (parametroActual == null) {
         getListaParametros();
         if (listaParametros != null && !listaParametros.isEmpty()) {
            parametroActual = listaParametros.get(registroActual);
         }
      }
      return parametroActual;
   }

   public void setParametroActual(Parametros parametroActual) {
      this.parametroActual = parametroActual;
   }

   public ParametrosEstructuras getParametroEstructura() {
      if (parametroEstructura == null) {
         parametroEstructura = administrarComprobantes.consultarParametroEstructuraActualUsuario();
      }
      return parametroEstructura;
   }

   public void setParametroEstructura(ParametrosEstructuras parametroEstructura) {
      this.parametroEstructura = parametroEstructura;
   }

   public List<Parametros> getListaParametrosLOV() {
      if (listaParametrosLOV == null || listaParametrosLOV.isEmpty()) {
         listaParametrosLOV = administrarComprobantes.consultarParametrosComprobantesActualUsuario();
      }
      return listaParametrosLOV;
   }

   public void setListaParametrosLOV(List<Parametros> listaParametrosLOV) {
      this.listaParametrosLOV = listaParametrosLOV;
   }

   public Parametros getParametroSeleccionado() {
      return parametroSeleccionado;
   }

   public void setParametroSeleccionado(Parametros parametroSeleccionado) {
      this.parametroSeleccionado = parametroSeleccionado;
   }

   public List<Parametros> getFiltradoListaParametrosLOV() {
      return filtradoListaParametrosLOV;
   }

   public void setFiltradoListaParametrosLOV(List<Parametros> filtradoListaParametrosLOV) {
      this.filtradoListaParametrosLOV = filtradoListaParametrosLOV;
   }

   public boolean isAceptar() {
      return aceptar;
   }

   public boolean isMostrarTodos() {
      return mostrarTodos;
   }

   public List<SolucionesNodos> getListaSolucionesNodosEmpleado() {
      if (parametroActual != null) {
         if (listaSolucionesNodosEmpleado == null) {
            listaSolucionesNodosEmpleado = administrarComprobantes.consultarSolucionesNodosEmpleado(parametroActual.getEmpleado().getSecuencia());
            if (listaSolucionesNodosEmpleado != null) {
               subtotalPago = new BigDecimal(0);
               subtotalDescuento = new BigDecimal(0);
               for (int i = 0; i < listaSolucionesNodosEmpleado.size(); i++) {
                  if (listaSolucionesNodosEmpleado.get(i).getTipo().equals("PAGO")) {
                     subtotalPago = subtotalPago.add(listaSolucionesNodosEmpleado.get(i).getValor());
                  } else {
                     subtotalDescuento = subtotalDescuento.add(listaSolucionesNodosEmpleado.get(i).getValor());
                  }
               }
               neto = subtotalPago.subtract(subtotalDescuento);
               pago = nf.format(subtotalPago);
               descuento = nf.format(subtotalDescuento);
               netoTotal = nf.format(neto);
            }
         }
      }
      return listaSolucionesNodosEmpleado;
   }

   public void setListaSolucionesNodosEmpleado(List<SolucionesNodos> listaSolucionesNodosEmpleado) {
      this.listaSolucionesNodosEmpleado = listaSolucionesNodosEmpleado;
   }

   public List<SolucionesNodos> getFiltradolistaSolucionesNodosEmpleado() {
      return filtradolistaSolucionesNodosEmpleado;
   }

   public void setFiltradolistaSolucionesNodosEmpleado(List<SolucionesNodos> filtradolistaSolucionesNodosEmpleado) {
      this.filtradolistaSolucionesNodosEmpleado = filtradolistaSolucionesNodosEmpleado;
   }

   public List<SolucionesNodos> getListaSolucionesNodosEmpleador() {
      if (parametroActual != null) {
         if (listaSolucionesNodosEmpleador == null) {
            if (parametroActual.getEmpleado().getSecuencia() != null) {
               listaSolucionesNodosEmpleador = administrarComprobantes.consultarSolucionesNodosEmpleador(parametroActual.getEmpleado().getSecuencia());
               if (listaSolucionesNodosEmpleador != null) {
                  subtotalPasivo = new BigDecimal(0);
                  subtotalGasto = new BigDecimal(0);
                  for (int i = 0; i < listaSolucionesNodosEmpleador.size(); i++) {
                     if (listaSolucionesNodosEmpleador.get(i).getTipo().equals("PASIVO")) {
                        subtotalPasivo = subtotalPasivo.add(listaSolucionesNodosEmpleador.get(i).getValor());
                     } else if (listaSolucionesNodosEmpleador.get(i).getTipo().equals("GASTO")) {
                        subtotalGasto = subtotalGasto.add(listaSolucionesNodosEmpleador.get(i).getValor());
                     }
                  }
                  pasivo = nf.format(subtotalPasivo);
                  gasto = nf.format(subtotalGasto);
               }
            }
         }
      }

      return listaSolucionesNodosEmpleador;
   }

   public void setListaSolucionesNodosEmpleador(List<SolucionesNodos> listaSolucionesNodosEmpleador) {
      this.listaSolucionesNodosEmpleador = listaSolucionesNodosEmpleador;
   }

   public List<SolucionesNodos> getFiltradolistaSolucionesNodosEmpleador() {
      return filtradolistaSolucionesNodosEmpleador;
   }

   public void setFiltradolistaSolucionesNodosEmpleador(List<SolucionesNodos> filtradolistaSolucionesNodosEmpleador) {
      this.filtradolistaSolucionesNodosEmpleador = filtradolistaSolucionesNodosEmpleador;
   }

   public String getPago() {
      return pago;
   }

   public void setPago(String pago) {
      this.pago = pago;
   }

   public String getDescuento() {
      return descuento;
   }

   public void setNeto(BigDecimal neto) {
      this.neto = neto;
   }

   public void setDescuento(String descuento) {
      this.descuento = descuento;
   }

   public void setPasivo(String pasivo) {
      this.pasivo = pasivo;
   }

   public void setGasto(String gasto) {
      this.gasto = gasto;
   }

   public void setNetoTotal(String netoTotal) {
      this.netoTotal = netoTotal;
   }

   public String getPasivo() {
      return pasivo;
   }

   public String getGasto() {
      return gasto;
   }

   public String getNetoTotal() {
      return netoTotal;
   }

   public String getAltoScrollSolucionesNodosEmpleado() {
      return altoScrollSolucionesNodosEmpleado;
   }

   public String getAltoScrollSolucionesNodosEmpleador() {
      return altoScrollSolucionesNodosEmpleador;
   }

   public String getParcialesSolucionNodos() {
      return parcialesSolucionNodos;
   }

   public List<DetallesFormulas> getListaDetallesFormulas() {
      if (listaDetallesFormulas == null) {
         BigInteger secEmpleado = null, secProceso = null, secHistoriaFormula, secFormula = null;
         String fechaDesde = null, fechaHasta = null;
         if (tablaActual == 0) {
            if (listaSolucionesNodosEmpleado != null && !listaSolucionesNodosEmpleado.isEmpty()) {
               secFormula = listaSolucionesNodosEmpleado.get(index).getFormula();//
               fechaDesde = formatoFecha.format(listaSolucionesNodosEmpleado.get(index).getFechadesde()); //
               fechaHasta = formatoFecha.format(listaSolucionesNodosEmpleado.get(index).getFechahasta()); //
               secEmpleado = listaSolucionesNodosEmpleado.get(index).getEmpleado(); //
               secProceso = listaSolucionesNodosEmpleado.get(index).getProceso();//
            }
         } else if (tablaActual == 1) {
            if (listaSolucionesNodosEmpleador != null && !listaSolucionesNodosEmpleador.isEmpty()) {
               secFormula = listaSolucionesNodosEmpleador.get(index).getFormula();  //
               fechaDesde = formatoFecha.format(listaSolucionesNodosEmpleador.get(index)); // 
               fechaHasta = formatoFecha.format(listaSolucionesNodosEmpleador.get(index)); //
               secEmpleado = listaSolucionesNodosEmpleador.get(index).getEmpleado(); //
               secProceso = listaSolucionesNodosEmpleador.get(index).getProceso(); //
            }
         }
         if (secFormula != null && fechaDesde != null) {
            secHistoriaFormula = administrarComprobantes.consultarHistoriaFormulaFormula(secFormula, fechaDesde);
            listaDetallesFormulas = administrarComprobantes.consultarDetallesFormulasEmpleado(secEmpleado, fechaDesde, fechaHasta, secProceso, secHistoriaFormula);
         }
      }
      return listaDetallesFormulas;
   }

   public void setListaDetallesFormulas(List<DetallesFormulas> listaDetallesFormulas) {
      this.listaDetallesFormulas = listaDetallesFormulas;
   }

   public boolean isEstadoBtnArriba() {
      return estadoBtnArriba;
   }

   public boolean isEstadoBtnAbajo() {
      return estadoBtnAbajo;
   }

   public Parametros getEditarParametros() {
      return editarParametros;
   }

   public void setEditarParametros(Parametros editarParametros) {
      this.editarParametros = editarParametros;
   }

   public SolucionesNodos getSolucionNodoEmpleadoSeleccionada() {
      return solucionNodoEmpleadoSeleccionada;
   }

   public void setSolucionNodoEmpleadoSeleccionada(SolucionesNodos solucionNodoEmpleadoSeleccionada) {
      this.solucionNodoEmpleadoSeleccionada = solucionNodoEmpleadoSeleccionada;
   }

   public SolucionesNodos getEditarSolucionNodo() {
      return editarSolucionNodo;
   }

   public void setEditarSolucionNodo(SolucionesNodos editarSolucionNodo) {
      this.editarSolucionNodo = editarSolucionNodo;
   }

   public SolucionesNodos getSolucionNodoEmpleadorSeleccionada() {
      return solucionNodoEmpleadorSeleccionada;
   }

   public void setSolucionNodoEmpleadorSeleccionada(SolucionesNodos solucionNodoEmpleadorSeleccionada) {
      this.solucionNodoEmpleadorSeleccionada = solucionNodoEmpleadorSeleccionada;
   }

   public String getInfoRegistroComprobanteEmpleador() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosSolucionesNodosEmpleador");
      infoRegistroComprobanteEmpleador = String.valueOf(tabla.getRowCount());
      return infoRegistroComprobanteEmpleador;
   }

   public void setInfoRegistroComprobanteEmpleador(String infoRegistroComprobanteEmpleador) {
      this.infoRegistroComprobanteEmpleador = infoRegistroComprobanteEmpleador;
   }

   public String getInfoRegistroEmpleado() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovEmpleados");
      infoRegistroEmpleado = String.valueOf(tabla.getRowCount());
      return infoRegistroEmpleado;
   }

   public void setInfoRegistroEmpleado(String infoRegistroEmpleado) {
      this.infoRegistroEmpleado = infoRegistroEmpleado;
   }

   public String getInfoRegistroComprobante() {
      infoRegistroComprobante = (registroActual + 1) + " / " + listaParametros.size();
      return infoRegistroComprobante;
   }

   public void setInfoRegistroComprobante(String infoRegistroComprobante) {
      this.infoRegistroComprobante = infoRegistroComprobante;
   }

   public String getInfoRegistroComprobanteEmpleado() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosSolucionesNodosEmpleado");
      infoRegistroComprobanteEmpleado = String.valueOf(tabla.getRowCount());
      return infoRegistroComprobanteEmpleado;
   }

   public void setInfoRegistroComprobanteEmpleado(String infoRegistroComprobanteEmpleado) {
      this.infoRegistroComprobanteEmpleado = infoRegistroComprobanteEmpleado;
   }

}
