/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Empresas;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarEmpresasInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
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
public class ControlPeriodosActivos implements Serializable {

   private static Logger log = Logger.getLogger(ControlPeriodosActivos.class);

   @EJB
   AdministrarEmpresasInterface administrarEmpresas;
   @EJB
   AdministrarRastrosInterface administrarRastros;

   private int cualCelda, tipoLista, tipoActualizacion, k, bandera;
   private BigInteger l;
   private boolean aceptar, guardado;
   //RASTRO
   private String mensajeValidacion;
//EMPRESA
//   private List<Empresas> listaEmpresas;
//   private List<Empresas> filtradoListaEmpresas;

   private Empresas empresaSeleccionada;
//   private int banderaModificacionEmpresa;
   private int indiceEmpresaMostrada;
//LISTA CENTRO COSTO
   private List<Empresas> listaEmpresas;
   private List<Empresas> filtrarEmpresas;
   private List<Empresas> modificarEmpresas;
   private Empresas editarEmpresas;

   private Column fechaInicial, fechaFinal;

   private Empresas backUpEmpresaActual;

   private String infoRegistro;
   private int tamano;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();
   private ListasRecurrentes listasRecurrentes;

   public ControlPeriodosActivos() {
      FacesContext fc = FacesContext.getCurrentInstance();
      ControlListaNavegacion controlListaNavegacion = (ControlListaNavegacion) fc.getApplication().evaluateExpressionGet(fc, "#{controlListaNavegacion}", ControlListaNavegacion.class);
      listasRecurrentes = controlListaNavegacion.getListasRecurrentes();
      empresaSeleccionada = null;
      indiceEmpresaMostrada = 0;
      listaEmpresas = null;
      modificarEmpresas = new ArrayList<Empresas>();
      aceptar = true;
      guardado = true;
      tamano = 260;
      backUpEmpresaActual = new Empresas();
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
         administrarEmpresas.obtenerConexion(ses.getId());
         administrarRastros.obtenerConexion(ses.getId());
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
      String pagActual = "controlperiodoactivo";
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

   public void recibirEmpresa(BigInteger secEmpresa) {
      empresaSeleccionada = null;
      getListaEmpresas();
      if (listaEmpresas != null) {
         if (!listaEmpresas.isEmpty()) {
            if (secEmpresa.equals(new BigInteger("0"))) {
               empresaSeleccionada = listaEmpresas.get(0);
            } else {
               for (int i = 0; i < listaEmpresas.size(); i++) {
                  if (listaEmpresas.get(i).getSecuencia().equals(secEmpresa)) {
                     empresaSeleccionada = listaEmpresas.get(i);
                  }
               }
            }
         }
      }
   }

   public void eventoFiltrar() {
      if (tipoLista == 0) {
         tipoLista = 1;
      }
   }

   public void cambiarIndiceDefault() {
      log.info("ControlPeriodosActivos.cambiarIndiceDefault() empresaSeleccionada.secuencia : " + empresaSeleccionada.getSecuencia());
      backUpEmpresaActual.setControlinicioperiodoactivo(empresaSeleccionada.getControlinicioperiodoactivo());
      backUpEmpresaActual.setControlfinperiodoactivo(empresaSeleccionada.getControlfinperiodoactivo());
   }

   public void cambiarIndice(Empresas empresa, int celda) {
      log.info("ControlPeriodosActivos.cambiarIndice() empresaSeleccionada.secuencia : " + empresaSeleccionada.getSecuencia());
      empresaSeleccionada = empresa;
      cualCelda = celda;
      backUpEmpresaActual.setControlinicioperiodoactivo(empresaSeleccionada.getControlinicioperiodoactivo());
      backUpEmpresaActual.setControlfinperiodoactivo(empresaSeleccionada.getControlfinperiodoactivo());
   }

   public void modificandoFechas(Empresas empresa, int celda, String cambio) {
      empresaSeleccionada = empresa;
      Calendar myCalendar = Calendar.getInstance();
      int contador = 0;
      int mes = 0, anito = 0, mesHoy = 0, anitoHoy = 0;
      mensajeValidacion = " ";
      cualCelda = celda;

      if (cambio.equalsIgnoreCase("INICIO")) {
         if (empresaSeleccionada.getControlinicioperiodoactivo() == null) {
            mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
         } else {
            empresaSeleccionada.getControlfinperiodoactivo();
            myCalendar.setTime(empresaSeleccionada.getControlinicioperiodoactivo());
            myCalendar.set(Calendar.DAY_OF_MONTH, 1);
            Date firstDay = myCalendar.getTime();
            log.error("Primer Dia : " + firstDay);
            empresaSeleccionada.setControlinicioperiodoactivo(firstDay);
            myCalendar.add(Calendar.MONTH, 1);
            myCalendar.set(Calendar.DAY_OF_MONTH, 1);
            myCalendar.add(Calendar.DATE, -1);
            Date lastDay = myCalendar.getTime();
            log.error("Ultimo dia : " + lastDay);
            empresaSeleccionada.setControlfinperiodoactivo(lastDay);
            contador++;
         }
      } else {
         log.error("FINAL");
         if (empresaSeleccionada.getControlfinperiodoactivo() == null) {
            mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
         } else {
            myCalendar.setTime(empresaSeleccionada.getControlfinperiodoactivo());
            myCalendar.set(Calendar.DAY_OF_MONTH, 1);
            Date firstDay = myCalendar.getTime();
            log.error("Primer Dia : " + firstDay);
            empresaSeleccionada.setControlinicioperiodoactivo(firstDay);
            myCalendar.add(Calendar.MONTH, 1);
            myCalendar.set(Calendar.DAY_OF_MONTH, 1);
            myCalendar.set(Calendar.DATE, -1);
            Date lastDay = myCalendar.getTime();
            log.error("Ultimo dia : " + lastDay);
            empresaSeleccionada.setControlfinperiodoactivo(lastDay);
            contador++;
         }
      }
      if (contador == 1) {
         Date hoy = new Date();
         mesHoy = hoy.getMonth();
         anitoHoy = hoy.getYear();
         mes = empresaSeleccionada.getControlfinperiodoactivo().getMonth();
         anito = empresaSeleccionada.getControlfinperiodoactivo().getYear();
         log.error("mesHoy : " + mesHoy);
         log.error("anitoHoy : " + anitoHoy);
         log.error("mes: " + mes);
         log.error("anito: " + anito);
         if ((anito - anitoHoy) != 0 || (mes - mesHoy) != 0) {
            RequestContext.getCurrentInstance().execute("PF('modificacionFechas1').show()");
         }
         if (modificarEmpresas.isEmpty() || !modificarEmpresas.contains(empresaSeleccionada)) {
            modificarEmpresas.add(empresaSeleccionada);
         }
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         RequestContext.getCurrentInstance().update("form:datosEmpresas");
      } else {
         RequestContext.getCurrentInstance().update("form:validacionModificar");
         RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");
      }
      RequestContext.getCurrentInstance().update("form:datosEmpresas");
   }

   public void cancelarYSalir() {
//      cancelarModificacion();
      salir();
   }

   public void cancelarModificacion() {
      try {
         log.info("entre a CONTROLBETACENTROSCOSTOS.cancelarModificacion");
         FacesContext c = FacesContext.getCurrentInstance();
         if (bandera == 1) {
            //CERRAR FILTRADO
            //0
            fechaInicial = (Column) c.getViewRoot().findComponent("form:datosEmpresas:fechaInicial");
            fechaInicial.setFilterStyle("display: none; visibility: hidden;");
            //1
            fechaFinal = (Column) c.getViewRoot().findComponent("form:datosEmpresas:fechaFinal");
            fechaFinal.setFilterStyle("display: none; visibility: hidden;");

            tamano = 260;
            bandera = 0;
            filtrarEmpresas = null;
            tipoLista = 0;
         }

         modificarEmpresas.clear();
         empresaSeleccionada = null;
         k = 0;
         listaEmpresas = null;
         guardado = true;
         getListaEmpresas();
         if (listaEmpresas == null || listaEmpresas.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
         } else {
            infoRegistro = "Cantidad de registros: " + listaEmpresas.size();
         }
         RequestContext.getCurrentInstance().update("form:informacionRegistro");
         RequestContext.getCurrentInstance().update("form:datosEmpresas");
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         RequestContext.getCurrentInstance().update("form:BUSCARCENTROCOSTO");
         RequestContext.getCurrentInstance().update("form:MOSTRARTODOS");
         RequestContext.getCurrentInstance().update("formularioDialogos:aceptarE");
      } catch (Exception E) {
         log.warn("Error CONTROLBETACENTROSCOSTOS.ModificarModificacion ERROR====================" + E.getMessage());
      }
   }

   public void salir() {
      limpiarListasValor();
      try {
         log.info("entre a CONTROLBETACENTROSCOSTOS.Salir");
         FacesContext c = FacesContext.getCurrentInstance();
         if (bandera == 1) {
            fechaInicial = (Column) c.getViewRoot().findComponent("form:datosEmpresas:fechaInicial");
            fechaInicial.setFilterStyle("display: none; visibility: hidden;");
            fechaFinal = (Column) c.getViewRoot().findComponent("form:datosEmpresas:fechaFinal");
            fechaFinal.setFilterStyle("display: none; visibility: hidden;");
            tamano = 260;
            bandera = 0;
            filtrarEmpresas = null;
            tipoLista = 0;
         }
         modificarEmpresas.clear();
         empresaSeleccionada = null;
         k = 0;
         listaEmpresas = null;
         guardado = true;
         RequestContext.getCurrentInstance().update("form:datosEmpresas");
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         RequestContext.getCurrentInstance().update("formularioDialogos:aceptarE");
      } catch (Exception E) {
         log.warn("Error CONTROLBETACENTROSCOSTOS.ModificarModificacion ERROR====================" + E.getMessage());
      }
      navegar("atras");
   }

   public void activarAceptar() {
      aceptar = false;
   }

   public void guardarYSalir() {
      guardarCambiosPeriodo();
      salir();
   }

   public void guardarCambiosPeriodo() {
      if (guardado == false) {
         if (!modificarEmpresas.isEmpty()) {
            administrarEmpresas.editarEmpresas(modificarEmpresas);
            modificarEmpresas.clear();
         }
         listaEmpresas = null;
         k = 0;
         guardado = true;
      }
      aceptar = true;
      listasRecurrentes.limpiarListasEmpleados();
      FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
      FacesContext.getCurrentInstance().addMessage(null, msg);
      RequestContext.getCurrentInstance().update("form:growl");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void cancelarCambios() {
      empresaSeleccionada = backUpEmpresaActual;
      RequestContext.getCurrentInstance().update("formularioDialogos:lovEmpresas");
   }

   public void activarCtrlF11() {
      log.info("\n ENTRE A CONTROLBETACENTROSCOSTOS.activarCtrlF11 \n");

      try {

         FacesContext c = FacesContext.getCurrentInstance();
         if (bandera == 0) {
            tamano = 240;
            log.info("Activar");
            fechaInicial = (Column) c.getViewRoot().findComponent("form:datosEmpresas:fechaInicial");
            fechaInicial.setFilterStyle("width: 85% !important;");
            fechaFinal = (Column) c.getViewRoot().findComponent("form:datosEmpresas:fechaFinal");
            fechaFinal.setFilterStyle("width: 85% !important;");

            RequestContext.getCurrentInstance().update("form:datosEmpresas");
            bandera = 1;
         } else if (bandera == 1) {
            log.info("Desactivar");
            //0
            fechaInicial = (Column) c.getViewRoot().findComponent("form:datosEmpresas:fechaInicial");
            fechaInicial.setFilterStyle("display: none; visibility: hidden;");
            fechaFinal = (Column) c.getViewRoot().findComponent("form:datosEmpresas:fechaFinal");
            fechaFinal.setFilterStyle("display: none; visibility: hidden;");

            tamano = 260;
            RequestContext.getCurrentInstance().update("form:datosEmpresas");
            bandera = 0;
            filtrarEmpresas = null;
            tipoLista = 0;
         }
      } catch (Exception e) {

         log.warn("Error CONTROLBETACENTROSCOSTOS.activarCtrlF11 ERROR====================" + e.getMessage());
      }
   }

   public void editarCelda() {
      try {
         if (empresaSeleccionada != null) {
            log.info("\n ENTRE AeditarCelda TIPOLISTA " + tipoLista);
            editarEmpresas = empresaSeleccionada;

            log.info("CONTROLBETACENTROSCOSTOS: Entro a editar... valor celda: " + cualCelda);
            if (cualCelda == 0) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaInicial");
               RequestContext.getCurrentInstance().execute("PF('editarFechaInicial').show()");
               cualCelda = -1;
            } else if (cualCelda == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaFinal");
               RequestContext.getCurrentInstance().execute("PF('editarFechaFinal').show()");
               cualCelda = -1;
            }
         }
      } catch (Exception E) {
         log.warn("Error CONTROLBETACENTROSCOSTOS.editarCelDa ERROR=====================" + E.getMessage());
      }
   }

   public void exportPDF() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosEmpresasExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "CentroCostos", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   /**
    *
    * @throws IOException
    */
   public void exportXLS() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosEmpresasExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "CentroCostos", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void verificarRastro() {
      if (empresaSeleccionada != null) {
         int resultado = administrarRastros.obtenerTabla(empresaSeleccionada.getSecuencia(), "CENTROSCOSTOS");
         if (resultado == 1) {
            RequestContext.getCurrentInstance().execute("PF('errorObjetosDB').show()");
         } else if (resultado == 2) {
            RequestContext.getCurrentInstance().execute("PF('confirmarRastro').show()");
         } else if (resultado == 3) {
            RequestContext.getCurrentInstance().execute("PF('errorRegistroRastro').show()");
         } else if (resultado == 4) {
            RequestContext.getCurrentInstance().execute("PF('errorTablaConRastro').show()");
         } else if (resultado == 5) {
            RequestContext.getCurrentInstance().execute("PF('errorTablaSinRastro').show()");
         }
//         } else {
//            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
//         }
      } else if (administrarRastros.verificarHistoricosTabla("CENTROSCOSTOS")) {
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
   }

//-----------------------------------------------------------------------------**
   public String getMensajeValidacion() {
      return mensajeValidacion;
   }

   public void setMensajeValidacion(String mensajeValidacion) {
      this.mensajeValidacion = mensajeValidacion;
   }

   public Empresas getEmpresaSeleccionada() {
      return empresaSeleccionada;
   }

   public void setEmpresaSeleccionada(Empresas empresaSeleccionada) {
      this.empresaSeleccionada = empresaSeleccionada;
   }

   public List<Empresas> getListaEmpresas() {
      if (listaEmpresas == null) {
         listaEmpresas = administrarEmpresas.listaEmpresas();
      }
      return listaEmpresas;
   }

   public void setListaEmpresas(List<Empresas> listaEmpresas) {
      this.listaEmpresas = listaEmpresas;
   }

   public void setListEmpresasPorEmpresa(List<Empresas> listEmpresa) {
      this.listaEmpresas = listEmpresa;
   }

   public List<Empresas> getFiltrarEmpresas() {
      return filtrarEmpresas;
   }

   public void setFiltrarEmpresas(List<Empresas> filtrarEmpresas) {
      this.filtrarEmpresas = filtrarEmpresas;
   }

   public Empresas getEditarEmpresas() {
      return editarEmpresas;
   }

   public void setEditarEmpresas(Empresas editarEmpresas) {
      this.editarEmpresas = editarEmpresas;
   }

   public boolean isAceptar() {
      return aceptar;
   }

   public void setAceptar(boolean aceptar) {
      this.aceptar = aceptar;
   }

   public boolean isGuardado() {
      return guardado;
   }

   public void setGuardado(boolean guardado) {
      this.guardado = guardado;
   }

   public int getTamano() {
      return tamano;
   }

   public void setTamano(int tamano) {
      this.tamano = tamano;
   }

   public String getInfoRegistro() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosEmpresas");
      infoRegistro = String.valueOf(tabla.getRowCount());
      return infoRegistro;
   }

   public void setInfoRegistro(String infoRegistro) {
      this.infoRegistro = infoRegistro;
   }

}
