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
import javax.ejb.EJB;
import ControlNavegacion.ControlListaNavegacion;
import java.util.Map;
import java.util.LinkedHashMap;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.primefaces.component.column.Column;
import org.primefaces.context.RequestContext;

/**
 *
 * @author user
 */
@Named(value = "controlDetallesVacaciones")
@Dependent
public class ControlDetallesVacaciones {

   @EJB
   AdministrarRastrosInterface administrarRastros;
   @EJB
   AdministrarDetalleVacacionInterface administrarDetalleVacacion;

   private List<NovedadesSistema> listaNovedadesSistema;
   private List<NovedadesSistema> filtrarrNovedadesSistema;
   private List<NovedadesSistema> crearNovedadesSistema;
   private List<NovedadesSistema> borrarNovedadesSistema;
   private List<NovedadesSistema> modificarNovedadesSistema;
//
   private NovedadesSistema novedadSistemaSeleccionada;
   private NovedadesSistema nuevaNovedadSistema;
   private NovedadesSistema dupliacarNovedadSistema;
//
   private List<Vacaciones> listaPeriodos;
   private List<Vacaciones> filtradoslistaPeriodos;
   private Vacaciones periodoSeleccionado;
//
   private int cualCelda, tipoLista, tipoActualizacion, k, bandera;
   private BigInteger l;
   private Empleados empleado;
   private boolean aceptar, guardado, activarLOV;
   //
   private Column colFechaIni, colPeriodo, colDias, colFechaRegr, colSubtipo, colAdelanto, colFechaPago, colValorPago, colDiasAplaz;
   //
   private String backPeriodo;
   private Date backFechaInicio;
   //
   private String infoRegistro, infoRegistroPeriodo;
   private int tamano;
   //
   private BigInteger secuenciaVacacion;

   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

   /**
    * Creates a new instance of ControlDetallesVacaciones
    */
   public ControlDetallesVacaciones() {
      listaNovedadesSistema = null;
      filtrarrNovedadesSistema = null;
      novedadSistemaSeleccionada = null;
      crearNovedadesSistema = new ArrayList<NovedadesSistema>();
      borrarNovedadesSistema = new ArrayList<NovedadesSistema>();
      modificarNovedadesSistema = new ArrayList<NovedadesSistema>();
      nuevaNovedadSistema = new NovedadesSistema();
      dupliacarNovedadSistema = null;
      //
      listaPeriodos = null;
      filtradoslistaPeriodos = null;
      periodoSeleccionado = null;
      //
      cualCelda = -1;
      tipoLista = 0;
      tipoActualizacion = 0;
      k = 0;
      bandera = 0;
      empleado = null;
      aceptar = false;
      guardado = true;
      activarLOV = true;
      //
      backPeriodo = "";
      backFechaInicio = null;
      //
      infoRegistro = "0";
      infoRegistroPeriodo = "0";
      paginaAnterior = "nominaf";
      //
      mapParametros.put("paginaAnterior", paginaAnterior);
   }

  public void limpiarListasValor() {

   }

   @PostConstruct
   public void inicializarAdministrador() {
      try {
         FacesContext x = FacesContext.getCurrentInstance();
         HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
         administrarDetalleVacacion.obtenerConexion(ses.getId());
         administrarRastros.obtenerConexion(ses.getId());
      } catch (Exception e) {
         System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
         System.out.println("Causa: " + e.getCause());
      }
   }

   public void recibirVariasCosas(String pagina, Empleados empl, BigInteger secVacacion) {
      secuenciaVacacion = secVacacion;
      empleado = empl;
      paginaAnterior = pagina;
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
      if (pag.equals("atras")) {
         pag = paginaAnterior;
         paginaAnterior = "nominaf";
         controlListaNavegacion.quitarPagina();
      } else {
         String pagActual = "detallesvacaciones";
         //Map<String, Object> mapParaEnviar = new LinkedHashMap<String, Object>();
         //mapParametros.put("paginaAnterior", pagActual);
         //mas Parametros
//         if (pag.equals("rastrotabla")) {
//           ControlRastro controlRastro = (ControlRastro) fc.getApplication().evaluateExpressionGet(fc, "#{controlRastro}", ControlRastro.class);
         //           controlRastro.recibirDatosTabla(conceptoSeleccionado.getSecuencia(), "Conceptos", pagActual);
         //      } else if (pag.equals("rastrotablaH")) {
         //       ControlRastro controlRastro = (ControlRastro) fc.getApplication().evaluateExpressionGet(fc, "#{controlRastro}", ControlRastro.class);
         //     controlRastro.historicosTabla("Conceptos", pagActual);
         //   pag = "rastrotabla";
         //}
         controlListaNavegacion.adicionarPagina(pagActual);
      }
      fc.getApplication().getNavigationHandler().handleNavigation(fc, null, pag);
   }

   public String redirigirPaginaAnterior() {
      return paginaAnterior;
   }

   public void eventoFiltrar() {
      if (tipoLista == 0) {
         tipoLista = 1;
      }
      novedadSistemaSeleccionada = null;
      contarRegistros();
   }

   public void contarRegistros() {
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
   }

   public void contarRegistrosPeriodo() {
      RequestContext.getCurrentInstance().update("form:informacionRegistroPeriodo");
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
//         tamano = 310;
         colFechaIni = (Column) c.getViewRoot().findComponent("form:datosDetalleVaca:colFechaIni");
         colFechaIni.setFilterStyle("width: 85% !important;");
         colPeriodo = (Column) c.getViewRoot().findComponent("form:datosDetalleVaca:colPeriodo");
         colPeriodo.setFilterStyle("width: 85% !important;");
         colDias = (Column) c.getViewRoot().findComponent("form:datosDetalleVaca:colDias");
         colDias.setFilterStyle("width: 85% !important;");
         colFechaRegr = (Column) c.getViewRoot().findComponent("form:datosDetalleVaca:colFechaRegr");
         colFechaRegr.setFilterStyle("width: 85% !important;");
         colSubtipo = (Column) c.getViewRoot().findComponent("form:datosDetalleVaca:colSubtipo");
         colSubtipo.setFilterStyle("width: 85% !important;");
         colAdelanto = (Column) c.getViewRoot().findComponent("form:datosDetalleVaca:colAdelanto");
         colAdelanto.setFilterStyle("width: 85% !important;");
         colFechaPago = (Column) c.getViewRoot().findComponent("form:datosDetalleVaca:colFechaPago");
         colFechaPago.setFilterStyle("width: 85% !important;");
         colValorPago = (Column) c.getViewRoot().findComponent("form:datosDetalleVaca:colValorPago");
         colValorPago.setFilterStyle("width: 85% !important;");
         colDiasAplaz = (Column) c.getViewRoot().findComponent("form:datosDetalleVaca:colDiasAplaz");
         colDiasAplaz.setFilterStyle("width: 85% !important;");
         RequestContext.getCurrentInstance().update("form:datosDetalleVaca");
         filtrarrNovedadesSistema = null;
         bandera = 1;
         tipoLista = 0;
      } else if (bandera == 1) {
//         tamano = 330;
         colFechaIni = (Column) c.getViewRoot().findComponent("form:datosDetalleVaca:colFechaIni");
         colFechaIni.setFilterStyle("display: none; visibility: hidden;");
         colPeriodo = (Column) c.getViewRoot().findComponent("form:datosDetalleVaca:colPeriodo");
         colPeriodo.setFilterStyle("display: none; visibility: hidden;");
         colDias = (Column) c.getViewRoot().findComponent("form:datosDetalleVaca:colDias");
         colDias.setFilterStyle("display: none; visibility: hidden;");
         colFechaRegr = (Column) c.getViewRoot().findComponent("form:datosDetalleVaca:colFechaRegr");
         colFechaRegr.setFilterStyle("display: none; visibility: hidden;");
         colSubtipo = (Column) c.getViewRoot().findComponent("form:datosDetalleVaca:colSubtipo");
         colSubtipo.setFilterStyle("display: none; visibility: hidden;");
         colAdelanto = (Column) c.getViewRoot().findComponent("form:datosDetalleVaca:colAdelanto");
         colAdelanto.setFilterStyle("display: none; visibility: hidden;");
         colFechaPago = (Column) c.getViewRoot().findComponent("form:datosDetalleVaca:colFechaPago");
         colFechaPago.setFilterStyle("display: none; visibility: hidden;");
         colValorPago = (Column) c.getViewRoot().findComponent("form:datosDetalleVaca:colValorPago");
         colValorPago.setFilterStyle("display: none; visibility: hidden;");
         colDiasAplaz = (Column) c.getViewRoot().findComponent("form:datosDetalleVaca:colDiasAplaz");
         colDiasAplaz.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosDetalleVaca");
         filtrarrNovedadesSistema = null;
         bandera = 0;
         tipoLista = 0;
      }
   }

   //Gets y Sets
   public List<Vacaciones> getListaPeriodos() {
      if (listaPeriodos == null) {
         listaPeriodos = administrarDetalleVacacion.periodosEmpleado(empleado.getSecuencia());
      }
      return listaPeriodos;
   }

   public void setListaPeriodos(List<Vacaciones> listaPeriodos) {
      this.listaPeriodos = listaPeriodos;
   }

   public List<NovedadesSistema> getListaNovedadesSistema() {
      if (listaNovedadesSistema == null && empleado.getSecuencia() != null && secuenciaVacacion != null) {
         listaNovedadesSistema = administrarDetalleVacacion.novedadsistemaPorEmpleadoYVacacion(empleado.getSecuencia(), secuenciaVacacion);
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

   public NovedadesSistema getNuevaNovedadSistema() {
      return nuevaNovedadSistema;
   }

   public void setNuevaNovedadSistema(NovedadesSistema nuevaNovedadSistema) {
      this.nuevaNovedadSistema = nuevaNovedadSistema;
   }

   public NovedadesSistema getDupliacarNovedadSistema() {
      return dupliacarNovedadSistema;
   }

   public void setDupliacarNovedadSistema(NovedadesSistema dupliacarNovedadSistema) {
      this.dupliacarNovedadSistema = dupliacarNovedadSistema;
   }

   public List<Vacaciones> getFiltradoslistaPeriodos() {
      return filtradoslistaPeriodos;
   }

   public void setFiltradoslistaPeriodos(List<Vacaciones> filtradoslistaPeriodos) {
      this.filtradoslistaPeriodos = filtradoslistaPeriodos;
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
      return infoRegistro;
   }

   public void setInfoRegistro(String infoRegistro) {
      this.infoRegistro = infoRegistro;
   }

   public String getInfoRegistroPeriodo() {
      return infoRegistroPeriodo;
   }

   public void setInfoRegistroPeriodo(String infoRegistroPeriodo) {
      this.infoRegistroPeriodo = infoRegistroPeriodo;
   }

   public int getTamano() {
      FacesContext c = FacesContext.getCurrentInstance();
      colFechaIni = (Column) c.getViewRoot().findComponent("form:datosDetalleVaca:codigo");
//      colFechaIni.setFilterStyle("width: 85% !important;");
      String estilo = colFechaIni.getFilterStyle();
      System.out.println("getTamano() estilo : " + estilo);
      if (estilo.startsWith("width: 85%")) {
         tamano = 310;
      } else {
         tamano = 330;
      }
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

}
