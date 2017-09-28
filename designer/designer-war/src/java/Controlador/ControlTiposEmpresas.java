/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.TiposEmpresas;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarTiposEmpresasInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import ControlNavegacion.ControlListaNavegacion;
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
public class ControlTiposEmpresas implements Serializable {

   private static Logger log = Logger.getLogger(ControlTiposEmpresas.class);

   @EJB
   AdministrarTiposEmpresasInterface administrarTiposEmpresas;
   @EJB
   AdministrarRastrosInterface administrarRastros;

   private List<TiposEmpresas> listTiposEmpresas;
   private List<TiposEmpresas> filtrarTiposEmpresas;
   private List<TiposEmpresas> crearTiposEmpresas;
   private List<TiposEmpresas> modificarTiposEmpresas;
   private List<TiposEmpresas> borrarTiposEmpresas;
   private TiposEmpresas nuevoTiposEmpresas;
   private TiposEmpresas duplicarTiposEmpresas;
   private TiposEmpresas editarTiposEmpresas;
   private TiposEmpresas tiposEmpresasSeleccionado;
   //otros
   private int cualCelda, tipoLista, tipoActualizacion, k, bandera;
   private BigInteger l;
   private boolean aceptar, guardado;
   //AutoCompletar
   private boolean permitirIndex, activarLov;
   //RASTRO
   private Column codigo, descripcion;
   //borrado
   private int registrosBorrados;
   private String mensajeValidacion;
   //filtrado table
   private int tamano;
   private String infoRegistro;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

   public ControlTiposEmpresas() {
      listTiposEmpresas = null;
      crearTiposEmpresas = new ArrayList<TiposEmpresas>();
      modificarTiposEmpresas = new ArrayList<TiposEmpresas>();
      borrarTiposEmpresas = new ArrayList<TiposEmpresas>();
      permitirIndex = true;
      editarTiposEmpresas = new TiposEmpresas();
      nuevoTiposEmpresas = new TiposEmpresas();
      duplicarTiposEmpresas = new TiposEmpresas();
      guardado = true;
      tamano = 270;
      activarLov = true;
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
         administrarTiposEmpresas.obtenerConexion(ses.getId());
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
      String pagActual = "tipoempresa";
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

   public void eventoFiltrar() {
      try {
         if (tipoLista == 0) {
            tipoLista = 1;
         }
         contarRegistros();
      } catch (Exception e) {
         log.warn("Error ControlTiposEmpresas eventoFiltrar ERROR===" + e.getMessage());
      }
   }

   public void cambiarIndice(TiposEmpresas tipo, int celda) {

      if (permitirIndex == true) {
         tiposEmpresasSeleccionado = tipo;
         cualCelda = celda;
         tiposEmpresasSeleccionado.getSecuencia();
         if (cualCelda == 0) {
            tiposEmpresasSeleccionado.getCodigo();

         } else if (cualCelda == 1) {
            tiposEmpresasSeleccionado.getDescripcion();
         }
      }
   }

   public void activarAceptar() {
      aceptar = false;
   }

   public void cancelarModificacion() {
      if (bandera == 1) {
         //CERRAR FILTRADO
         FacesContext c = FacesContext.getCurrentInstance();
         codigo = (Column) c.getViewRoot().findComponent("form:datosTiposEmpresas:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposEmpresas:descripcion");
         descripcion.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosTiposEmpresas");
         bandera = 0;
         filtrarTiposEmpresas = null;
         tipoLista = 0;
         tamano = 270;
      }
      borrarTiposEmpresas.clear();
      crearTiposEmpresas.clear();
      modificarTiposEmpresas.clear();
      tiposEmpresasSeleccionado = null;
      k = 0;
      listTiposEmpresas = null;
      guardado = true;
      permitirIndex = true;
      getListTiposEmpresas();
      contarRegistros();
      RequestContext.getCurrentInstance().update("form:datosTiposEmpresas");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void salir() {
      limpiarListasValor();
      if (bandera == 1) {
         FacesContext c = FacesContext.getCurrentInstance();
         codigo = (Column) c.getViewRoot().findComponent("form:datosTiposEmpresas:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposEmpresas:descripcion");
         descripcion.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosTiposEmpresas");
         bandera = 0;
         filtrarTiposEmpresas = null;
         tipoLista = 0;
         tamano = 270;
      }
      borrarTiposEmpresas.clear();
      crearTiposEmpresas.clear();
      modificarTiposEmpresas.clear();
      tiposEmpresasSeleccionado = null;
      k = 0;
      listTiposEmpresas = null;
      guardado = true;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:datosTiposEmpresas");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      navegar("atras");
   }

   public void activarCtrlF11() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 0) {
         tamano = 250;
         codigo = (Column) c.getViewRoot().findComponent("form:datosTiposEmpresas:codigo");
         codigo.setFilterStyle("width: 85% !important;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposEmpresas:descripcion");
         descripcion.setFilterStyle("width: 85% !important;");
         RequestContext.getCurrentInstance().update("form:datosTiposEmpresas");
         log.info("Activar");
         bandera = 1;
      } else if (bandera == 1) {
         log.info("Desactivar");
         tamano = 270;
         codigo = (Column) c.getViewRoot().findComponent("form:datosTiposEmpresas:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposEmpresas:descripcion");
         descripcion.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosTiposEmpresas");
         bandera = 0;
         filtrarTiposEmpresas = null;
         tipoLista = 0;
      }
   }

   public void modificarTiposEmpresas(TiposEmpresas tipo) {
      tiposEmpresasSeleccionado = tipo;

      RequestContext context = RequestContext.getCurrentInstance();
      if (!crearTiposEmpresas.contains(tiposEmpresasSeleccionado)) {
         if (modificarTiposEmpresas.isEmpty()) {
            modificarTiposEmpresas.add(tiposEmpresasSeleccionado);
         } else if (!modificarTiposEmpresas.contains(tiposEmpresasSeleccionado)) {
            modificarTiposEmpresas.add(tiposEmpresasSeleccionado);
         }
         if (guardado == true) {
            guardado = false;

         }
      }
      RequestContext.getCurrentInstance().update("form:datosTiposEmpresas");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");

   }

   public void borrandoTiposEmpresas() {
      if (tiposEmpresasSeleccionado != null) {
         log.info("Entro a borrandoTiposEmpresas");
         if (!modificarTiposEmpresas.isEmpty() && modificarTiposEmpresas.contains(tiposEmpresasSeleccionado)) {
            int modIndex = modificarTiposEmpresas.indexOf(tiposEmpresasSeleccionado);
            modificarTiposEmpresas.remove(modIndex);
            borrarTiposEmpresas.add(tiposEmpresasSeleccionado);
         } else if (!crearTiposEmpresas.isEmpty() && crearTiposEmpresas.contains(tiposEmpresasSeleccionado)) {
            int crearIndex = crearTiposEmpresas.indexOf(tiposEmpresasSeleccionado);
            crearTiposEmpresas.remove(crearIndex);
         } else {
            borrarTiposEmpresas.add(tiposEmpresasSeleccionado);
         }
         listTiposEmpresas.remove(tiposEmpresasSeleccionado);
         if (tipoLista == 1) {
            filtrarTiposEmpresas.remove(tiposEmpresasSeleccionado);
         }
         tiposEmpresasSeleccionado = null;
         contarRegistros();
         if (guardado == true) {
            guardado = false;
         }
         RequestContext.getCurrentInstance().update("form:datosTiposEmpresas");
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistros').show()");
      }
   }

   public void revisarDialogoGuardar() {
      if (!borrarTiposEmpresas.isEmpty() || !crearTiposEmpresas.isEmpty() || !modificarTiposEmpresas.isEmpty()) {
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:confirmarGuardar");
         RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
      }
   }

   public void guardarYSalir() {
      guardarTiposEmpresas();
      salir();
   }

   public void guardarTiposEmpresas() {
      RequestContext context = RequestContext.getCurrentInstance();

      if (guardado == false) {
         if (!borrarTiposEmpresas.isEmpty()) {
            administrarTiposEmpresas.borrarTiposEmpresas(borrarTiposEmpresas);
            registrosBorrados = borrarTiposEmpresas.size();
            RequestContext.getCurrentInstance().update("form:mostrarBorrados");
            RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
            borrarTiposEmpresas.clear();
         }
         if (!modificarTiposEmpresas.isEmpty()) {
            administrarTiposEmpresas.modificarTiposEmpresas(modificarTiposEmpresas);
            modificarTiposEmpresas.clear();
         }
         if (!crearTiposEmpresas.isEmpty()) {
            administrarTiposEmpresas.crearTiposEmpresas(crearTiposEmpresas);
            crearTiposEmpresas.clear();
         }
         log.info("Se guardaron los datos con exito");
         listTiposEmpresas = null;
         RequestContext.getCurrentInstance().update("form:datosTiposEmpresas");
         k = 0;
         guardado = true;
         FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      }
      RequestContext.getCurrentInstance().update("form:ACEPTAR");

   }

   public void editarCelda() {
      if (tiposEmpresasSeleccionado != null) {
         editarTiposEmpresas = tiposEmpresasSeleccionado;
         if (cualCelda == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editCodigo");
            RequestContext.getCurrentInstance().execute("PF('editCodigo').show()");
            cualCelda = -1;
         } else if (cualCelda == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editDescripcion");
            RequestContext.getCurrentInstance().execute("PF('editDescripcion').show()");
            cualCelda = -1;
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void agregarNuevoTiposEmpresas() {
      log.info("agregarNuevoTiposEmpresas");
      int contador = 0;
      int duplicados = 0;
      Integer a = 0;
      a = null;
      mensajeValidacion = " ";
      if (nuevoTiposEmpresas.getCodigo() == a) {
         mensajeValidacion = "El campo código no puede estar vacío";
      } else {
         for (int x = 0; x < listTiposEmpresas.size(); x++) {
            if (listTiposEmpresas.get(x).getCodigo() == nuevoTiposEmpresas.getCodigo()) {
               duplicados++;
            }
         }
         if (duplicados > 0) {
            mensajeValidacion = "El código ingresado ya existe. Por favor ingrese un código válido";
         } else {
            contador++;
         }
      }
      if (nuevoTiposEmpresas.getDescripcion() == null) {
         mensajeValidacion = "El campo descripción no puede estar vacío";
      } else if (nuevoTiposEmpresas.getDescripcion().isEmpty()) {
         mensajeValidacion = "El campo descripción no puede estar vacío";
      } else {
         contador++;
      }

      if (contador == 2) {
         if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            log.info("Desactivar");
            codigo = (Column) c.getViewRoot().findComponent("form:datosTiposEmpresas:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposEmpresas:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTiposEmpresas");
            bandera = 0;
            filtrarTiposEmpresas = null;
            tipoLista = 0;
         }
         k++;
         l = BigInteger.valueOf(k);
         nuevoTiposEmpresas.setSecuencia(l);
         crearTiposEmpresas.add(nuevoTiposEmpresas);
         listTiposEmpresas.add(nuevoTiposEmpresas);
         tiposEmpresasSeleccionado = nuevoTiposEmpresas;
         contarRegistros();
         nuevoTiposEmpresas = new TiposEmpresas();
         RequestContext.getCurrentInstance().update("form:datosTiposEmpresas");

         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         RequestContext.getCurrentInstance().execute("PF('nuevoRegistroTiposEmpresas').hide()");
      } else {
         RequestContext.getCurrentInstance().update("form:validacionNuevoTipoEmpresa");
         RequestContext.getCurrentInstance().execute("PF('validacionNuevoTipoEmpresa').show()");
         contador = 0;
      }
   }

   public void limpiarNuevoTiposEmpresas() {
      log.info("limpiarNuevoTiposEmpresas");
      nuevoTiposEmpresas = new TiposEmpresas();
   }

   //------------------------------------------------------------------------------
   public void duplicandoTiposEmpresas() {
      log.info("duplicandoTiposEmpresas");
      if (tiposEmpresasSeleccionado != null) {
         duplicarTiposEmpresas = new TiposEmpresas();
         k++;
         l = BigInteger.valueOf(k);
         duplicarTiposEmpresas.setSecuencia(l);
         duplicarTiposEmpresas.setCodigo(tiposEmpresasSeleccionado.getCodigo());
         duplicarTiposEmpresas.setDescripcion(tiposEmpresasSeleccionado.getDescripcion());
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTE");
         RequestContext.getCurrentInstance().execute("PF('duplicarRegistroTiposEmpresas').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro'),show()");
      }
   }

   public void confirmarDuplicar() {
      int contador = 0;
      mensajeValidacion = " ";
      int duplicados = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      Integer a = 0;
      a = null;
      log.error("ConfirmarDuplicar codigo " + duplicarTiposEmpresas.getCodigo());
      log.error("ConfirmarDuplicar Descripcion " + duplicarTiposEmpresas.getDescripcion());

      if (duplicarTiposEmpresas.getCodigo() == a) {
         mensajeValidacion = "El campo código no puede estar vacío";
      } else {
         for (int x = 0; x < listTiposEmpresas.size(); x++) {
            if (listTiposEmpresas.get(x).getCodigo() == duplicarTiposEmpresas.getCodigo()) {
               duplicados++;
            }
         }
         if (duplicados > 0) {
            mensajeValidacion = "El código ingresado ya existe. Por favor ingrese un código válido";
         } else {
            contador++;
            duplicados = 0;
         }
      }
      if (duplicarTiposEmpresas.getDescripcion() == null) {
         mensajeValidacion = "El campo descripción no puede estar vacío";
      } else if (duplicarTiposEmpresas.getDescripcion().isEmpty()) {
         mensajeValidacion = "El campo descripción no puede estar vacío";
      } else {
         log.info("bandera");
         contador++;
      }

      if (contador == 2) {
         listTiposEmpresas.add(duplicarTiposEmpresas);
         crearTiposEmpresas.add(duplicarTiposEmpresas);
         tiposEmpresasSeleccionado = duplicarTiposEmpresas;
         RequestContext.getCurrentInstance().update("form:datosTiposEmpresas");
         if (guardado == true) {
            guardado = false;
         }
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         contarRegistros();

         if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            codigo = (Column) c.getViewRoot().findComponent("form:datosTiposEmpresas:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposEmpresas:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTiposEmpresas");
            bandera = 0;
            filtrarTiposEmpresas = null;
            tipoLista = 0;
         }
         duplicarTiposEmpresas = new TiposEmpresas();
         RequestContext.getCurrentInstance().execute("PF('duplicarRegistroTiposEmpresas').hide()");

      } else {
         contador = 0;
         RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
         RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
      }
   }

   public void limpiarDuplicarTiposEmpresas() {
      duplicarTiposEmpresas = new TiposEmpresas();
   }

   public void exportPDF() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTiposEmpresasExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "TIPOSEMPRESAS", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void exportXLS() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTiposEmpresasExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "TIPOSEMPRESAS", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void verificarRastro() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tiposEmpresasSeleccionado != null) {
         int resultado = administrarRastros.obtenerTabla(tiposEmpresasSeleccionado.getSecuencia(), "TIPOSEMPRESAS"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
      } else if (administrarRastros.verificarHistoricosTabla(
              "TIPOSEMPRESAS")) { // igual acá
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
   }

   public void contarRegistros() {
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
   }

//*/*/*/*/*/*/*/*/*/*-/-*//-*/-*/*/*-*/-*/-*/*/*/*/*/---/*/*/*/*/-*/-*/-*/-*/-*/
   public List<TiposEmpresas> getListTiposEmpresas() {
      if (listTiposEmpresas == null) {
         log.info("ControlTiposEmpresas getListTiposEmpresas");
         listTiposEmpresas = administrarTiposEmpresas.consultarTiposEmpresas();
      }
      return listTiposEmpresas;
   }

   public void setListTiposEmpresas(List<TiposEmpresas> listTiposEmpresas) {
      this.listTiposEmpresas = listTiposEmpresas;
   }

   public List<TiposEmpresas> getFiltrarTiposEmpresas() {
      return filtrarTiposEmpresas;
   }

   public void setFiltrarTiposEmpresas(List<TiposEmpresas> filtrarTiposEmpresas) {
      this.filtrarTiposEmpresas = filtrarTiposEmpresas;
   }

   public TiposEmpresas getNuevoTiposEmpresas() {
      return nuevoTiposEmpresas;
   }

   public void setNuevoTiposEmpresas(TiposEmpresas nuevoTiposEmpresas) {
      this.nuevoTiposEmpresas = nuevoTiposEmpresas;
   }

   public TiposEmpresas getDuplicarTiposEmpresas() {
      return duplicarTiposEmpresas;
   }

   public void setDuplicarTiposEmpresas(TiposEmpresas duplicarTiposEmpresas) {
      this.duplicarTiposEmpresas = duplicarTiposEmpresas;
   }

   public TiposEmpresas getEditarTiposEmpresas() {
      return editarTiposEmpresas;
   }

   public void setEditarTiposEmpresas(TiposEmpresas editarTiposEmpresas) {
      this.editarTiposEmpresas = editarTiposEmpresas;
   }

   public int getRegistrosBorrados() {
      return registrosBorrados;
   }

   public void setRegistrosBorrados(int registrosBorrados) {
      this.registrosBorrados = registrosBorrados;
   }

   public String getMensajeValidacion() {
      return mensajeValidacion;
   }

   public void setMensajeValidacion(String mensajeValidacion) {
      this.mensajeValidacion = mensajeValidacion;
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

   public TiposEmpresas getTiposEmpresasSeleccionado() {
      return tiposEmpresasSeleccionado;
   }

   public void setTiposEmpresasSeleccionado(TiposEmpresas tiposEmpresasSeleccionado) {
      this.tiposEmpresasSeleccionado = tiposEmpresasSeleccionado;
   }

   public String getInfoRegistro() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosTiposEmpresas");
      infoRegistro = String.valueOf(tabla.getRowCount());
      return infoRegistro;
   }

   public void setInfoRegistro(String infoRegistro) {
      this.infoRegistro = infoRegistro;
   }

   public boolean isAceptar() {
      return aceptar;
   }

   public void setAceptar(boolean aceptar) {
      this.aceptar = aceptar;
   }

   public boolean isActivarLov() {
      return activarLov;
   }

   public void setActivarLov(boolean activarLov) {
      this.activarLov = activarLov;
   }

}
