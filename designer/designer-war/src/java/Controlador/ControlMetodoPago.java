/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.MetodosPagos;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarMetodosPagosInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import ControlNavegacion.ControlListaNavegacion;
import java.util.Map;
import java.util.LinkedHashMap;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
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
public class ControlMetodoPago implements Serializable {

   @EJB
   AdministrarMetodosPagosInterface administrarMetodosPagos;
   @EJB
   AdministrarRastrosInterface administrarRastros;

   private List<MetodosPagos> listMetodosPagos;
   private List<MetodosPagos> filtrarMetodosPagos;
   private List<MetodosPagos> borrarMetodosPagos;
   private List<MetodosPagos> crearMetodosPagos;
   private List<MetodosPagos> modificarMetodosPagos;
   private MetodosPagos editarMetodoPago;
   private MetodosPagos nuevoMetodoPago;
   private MetodosPagos duplicarMetodoPago;
   private MetodosPagos metodoPagoSeleccionado;
   //otros
   private int cualCelda, tipoLista, tipoActualizacion, k, bandera;
   private BigInteger l;
   private boolean aceptar, guardado;
   //AutoCompletar
   private boolean permitirIndex;
   //RASTRO
   private Column codigo, descripcion, pago;
   //borrado
   private int registrosBorrados;
   private String mensajeValidacion;
   private Integer a;
   private int tamano;
   private BigInteger verificarVigenciasFormasPagos;
   private boolean activarLov;
   private String infoRegistro;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

   public ControlMetodoPago() {
      listMetodosPagos = null;
      crearMetodosPagos = new ArrayList<MetodosPagos>();
      modificarMetodosPagos = new ArrayList<MetodosPagos>();
      borrarMetodosPagos = new ArrayList<MetodosPagos>();
      permitirIndex = true;
      guardado = true;
      editarMetodoPago = new MetodosPagos();
      nuevoMetodoPago = new MetodosPagos();
      a = null;
      tamano = 270;
      mapParametros.put("paginaAnterior", paginaAnterior);
      activarLov = true;
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
      String pagActual = "metodospagos";
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
         administrarMetodosPagos.obtenerConexion(ses.getId());
         administrarRastros.obtenerConexion(ses.getId());
      } catch (Exception e) {
         System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
         System.out.println("Causa: " + e.getCause());
      }
   }

   public void eventoFiltrar() {
      try {
         System.out.println("\n ENTRE A ControlMotiviosCambiosCargos.eventoFiltrar \n");
         if (tipoLista == 0) {
            tipoLista = 1;
         }
         contarRegistros();
      } catch (Exception e) {
         System.out.println("ERROR ControlMotiviosCambiosCargos eventoFiltrar ERROR===" + e.getMessage());
      }
   }
   private Integer backUpCodigo;
   private String backUpDescripcion;

   public void cambiarIndice(MetodosPagos metodop, int celda) {
      if (permitirIndex == true) {
         metodoPagoSeleccionado = metodop;
         cualCelda = celda;
         metodoPagoSeleccionado.getSecuencia();
         if (cualCelda == 0) {
            metodoPagoSeleccionado.getCodigo();
         } else if (cualCelda == 1) {
            metodoPagoSeleccionado.getDescripcion();
         }
      }

   }

   public void activarAceptar() {
      aceptar = false;
   }

   public void cancelarModificacion() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 1) {
         //CERRAR FILTRADO
         codigo = (Column) c.getViewRoot().findComponent("form:datosMetodoPago:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosMetodoPago:descripcion");
         descripcion.setFilterStyle("display: none; visibility: hidden;");
         pago = (Column) c.getViewRoot().findComponent("form:datosMetodoPago:pago");
         pago.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosMetodoPago");
         bandera = 0;
         filtrarMetodosPagos = null;
         tipoLista = 0;
      }

      borrarMetodosPagos.clear();
      crearMetodosPagos.clear();
      modificarMetodosPagos.clear();
      metodoPagoSeleccionado = null;
      k = 0;
      listMetodosPagos = null;
      guardado = true;
      permitirIndex = true;
      getListMetodosPagos();
      contarRegistros();
      RequestContext.getCurrentInstance().update("form:datosMetodoPago");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void salir() {
      limpiarListasValor();
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 1) {
         //CERRAR FILTRADO
         codigo = (Column) c.getViewRoot().findComponent("form:datosMetodoPago:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosMetodoPago:descripcion");
         descripcion.setFilterStyle("display: none; visibility: hidden;");
         pago = (Column) c.getViewRoot().findComponent("form:datosMetodoPago:pago");
         pago.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosMetodoPago");
         bandera = 0;
         filtrarMetodosPagos = null;
         tipoLista = 0;
      }

      borrarMetodosPagos.clear();
      crearMetodosPagos.clear();
      modificarMetodosPagos.clear();
      metodoPagoSeleccionado = null;
      k = 0;
      listMetodosPagos = null;
      guardado = true;
      permitirIndex = true;
      getListMetodosPagos();
      contarRegistros();
      RequestContext.getCurrentInstance().update("form:datosMetodoPago");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      navegar("atras");
   }

   public void activarCtrlF11() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 0) {

         tamano = 250;
         codigo = (Column) c.getViewRoot().findComponent("form:datosMetodoPago:codigo");
         codigo.setFilterStyle("width: 85% !important;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosMetodoPago:descripcion");
         descripcion.setFilterStyle("width: 85% !important;");
         pago = (Column) c.getViewRoot().findComponent("form:datosMetodoPago:pago");
         pago.setFilterStyle("width: 85% !important;");
         RequestContext.getCurrentInstance().update("form:datosMetodoPago");
         System.out.println("Activar");
         bandera = 1;
      } else if (bandera == 1) {
         tamano = 270;
         System.out.println("Desactivar");
         codigo = (Column) c.getViewRoot().findComponent("form:datosMetodoPago:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosMetodoPago:descripcion");
         descripcion.setFilterStyle("display: none; visibility: hidden;");
         pago = (Column) c.getViewRoot().findComponent("form:datosMetodoPago:pago");
         pago.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosMetodoPago");
         bandera = 0;
         filtrarMetodosPagos = null;
         tipoLista = 0;
      }
   }

   public void modificarMetodosPagos(MetodosPagos metodop) {
      metodoPagoSeleccionado = metodop;
      if (!crearMetodosPagos.contains(metodoPagoSeleccionado)) {
         if (modificarMetodosPagos.isEmpty()) {
            modificarMetodosPagos.add(metodoPagoSeleccionado);
         } else if (!modificarMetodosPagos.contains(metodoPagoSeleccionado)) {
            modificarMetodosPagos.add(metodoPagoSeleccionado);
         }
         guardado = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      }
      RequestContext.getCurrentInstance().update("form:datosMetodoPago");
   }

   public void verificarBorrado() {
      try {
         verificarVigenciasFormasPagos = administrarMetodosPagos.verificarMetodosPagosVigenciasFormasPagos(metodoPagoSeleccionado.getSecuencia());
         if (verificarVigenciasFormasPagos.equals(new BigInteger("0"))) {
            borrandoMetodoPago();
         } else {
            RequestContext.getCurrentInstance().update("form:validacionBorrar");
            RequestContext.getCurrentInstance().execute("PF('validacionBorrar').show()");
            verificarVigenciasFormasPagos = new BigInteger("-1");
         }
      } catch (Exception e) {
         System.err.println("ERROR ControlTiposTallas verificarBorrado ERROR " + e);
      }
   }

   public void borrandoMetodoPago() {
      if (metodoPagoSeleccionado != null) {

         System.out.println("Entro a borrarMetodoPago");
         if (!modificarMetodosPagos.isEmpty() && modificarMetodosPagos.contains(metodoPagoSeleccionado)) {
            int modIndex = modificarMetodosPagos.indexOf(metodoPagoSeleccionado);
            modificarMetodosPagos.remove(modIndex);
            borrarMetodosPagos.add(metodoPagoSeleccionado);
         } else if (!crearMetodosPagos.isEmpty() && crearMetodosPagos.contains(metodoPagoSeleccionado)) {
            int crearIndex = crearMetodosPagos.indexOf(metodoPagoSeleccionado);
            crearMetodosPagos.remove(crearIndex);
         } else {
            borrarMetodosPagos.add(metodoPagoSeleccionado);
         }
         listMetodosPagos.remove(metodoPagoSeleccionado);
         if (tipoLista == 1) {
            filtrarMetodosPagos.remove(metodoPagoSeleccionado);
         }
         contarRegistros();
         RequestContext.getCurrentInstance().update("form:datosMetodoPago");
         metodoPagoSeleccionado = null;
         if (guardado == true) {
            guardado = false;
         }
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      }
   }

   public void guardarYSalir() {
      guardarMetodosPagos();
      salir();
   }

   public void guardarMetodosPagos() {
      if (guardado == false) {
         if (!borrarMetodosPagos.isEmpty()) {
            administrarMetodosPagos.borrarMetodosPagos(borrarMetodosPagos);
            registrosBorrados = borrarMetodosPagos.size();
            RequestContext.getCurrentInstance().update("form:mostrarBorrados");
            RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
            borrarMetodosPagos.clear();
         }
         if (!crearMetodosPagos.isEmpty()) {
            administrarMetodosPagos.crearMetodosPagos(crearMetodosPagos);
            crearMetodosPagos.clear();
         }
         if (!modificarMetodosPagos.isEmpty()) {
            administrarMetodosPagos.modificarMetodosPagos(modificarMetodosPagos);
            modificarMetodosPagos.clear();
         }
         listMetodosPagos = null;
         RequestContext.getCurrentInstance().update("form:datosMetodoPago");
         k = 0;
         FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      }
      guardado = true;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");

   }

   public void editarCelda() {
      if (metodoPagoSeleccionado != null) {
         editarMetodoPago = metodoPagoSeleccionado;
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

   public void agregarNuevoMetodoPago() {
      int contador = 0;
      int duplicados = 0;
      mensajeValidacion = " ";
      RequestContext context = RequestContext.getCurrentInstance();
      System.err.println("1");
      if (nuevoMetodoPago.getCodigo() == a) {
         mensajeValidacion = " Los campos marcados con asterisco son obligatorios";
      } else {
         for (int x = 0; x < listMetodosPagos.size(); x++) {
            if (listMetodosPagos.get(x).getCodigo() == nuevoMetodoPago.getCodigo()) {
               duplicados++;
            }
         }
         if (duplicados > 0) {
            mensajeValidacion = "El código ingresado está en uso. Por favor ingrese un código válido";
         } else {
            contador++;
         }
      }
      if (nuevoMetodoPago.getDescripcion() == null || nuevoMetodoPago.getDescripcion().isEmpty() || nuevoMetodoPago.getDescripcion().equals(" ")) {
         mensajeValidacion = " Los campos marcados con asterisco son obligatorios";
      } else {
         contador++;
      }

      if (contador == 2) {
         if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            System.out.println("Desactivar");
            codigo = (Column) c.getViewRoot().findComponent("form:datosMetodoPago:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosMetodoPago:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            pago = (Column) c.getViewRoot().findComponent("form:datosMetodoPago:pago");
            pago.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosMetodoPago");
            bandera = 0;
            filtrarMetodosPagos = null;
            tipoLista = 0;
         }
         k++;
         l = BigInteger.valueOf(k);
         nuevoMetodoPago.setSecuencia(l);
         crearMetodosPagos.add(nuevoMetodoPago);
         listMetodosPagos.add(nuevoMetodoPago);
         metodoPagoSeleccionado = nuevoMetodoPago;
         contarRegistros();
         RequestContext.getCurrentInstance().update("form:datosMetodoPago");
         nuevoMetodoPago = new MetodosPagos();
         guardado = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         RequestContext.getCurrentInstance().execute("PF('nuevoRegistroMetodosPagos').hide()");

      } else {
         RequestContext.getCurrentInstance().update("form:validacionNuevoMetodoPago");
         RequestContext.getCurrentInstance().execute("PF('validacionNuevoMetodoPago').show()");
         contador = 0;
      }
   }

   public void limpiarNuevoMetodosPagos() {
      nuevoMetodoPago = new MetodosPagos();
   }

   public void duplicarMetodosPagos() {
      if (metodoPagoSeleccionado != null) {
         duplicarMetodoPago = new MetodosPagos();
         k++;
         l = BigInteger.valueOf(k);

         duplicarMetodoPago.setSecuencia(null);
         duplicarMetodoPago.setCodigo(metodoPagoSeleccionado.getCodigo());
         duplicarMetodoPago.setDescripcion(metodoPagoSeleccionado.getDescripcion());
         duplicarMetodoPago.setPago(metodoPagoSeleccionado.getPago());
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarMetodosPagos");
         RequestContext.getCurrentInstance().execute("PF('duplicarRegistroMetodosPagos').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void confirmarDuplicar() {
      int contador = 0;
      int duplicados = 0;
      mensajeValidacion = " ";
      RequestContext context = RequestContext.getCurrentInstance();
      if (duplicarMetodoPago.getCodigo() == a) {
         mensajeValidacion = " Los campos marcados con asterisco son obligatorios";
      } else {
         for (int x = 0; x < listMetodosPagos.size(); x++) {
            if (listMetodosPagos.get(x).getCodigo() == duplicarMetodoPago.getCodigo()) {
               duplicados++;
            }
         }
         if (duplicados > 0) {
            mensajeValidacion = "El código ingresado está en uso. Por favor ingrese un código válido";
         } else {
            contador++;
         }
      }
      if (duplicarMetodoPago.getDescripcion() == null || duplicarMetodoPago.getDescripcion().isEmpty()) {
         mensajeValidacion = " Los campos marcados con asterisco son obligatorios";
      } else {
         contador++;
      }

      if (contador == 2) {
         if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            System.out.println("Desactivar");
            codigo = (Column) c.getViewRoot().findComponent("form:datosMetodoPago:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosMetodoPago:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            pago = (Column) c.getViewRoot().findComponent("form:datosMetodoPago:pago");
            pago.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosMetodoPago");
            bandera = 0;
            filtrarMetodosPagos = null;
            tipoLista = 0;
         }
         k++;
         l = BigInteger.valueOf(k);
         duplicarMetodoPago.setSecuencia(l);
         crearMetodosPagos.add(duplicarMetodoPago);
         listMetodosPagos.add(duplicarMetodoPago);
         metodoPagoSeleccionado = duplicarMetodoPago;
         nuevoMetodoPago = new MetodosPagos();
         contarRegistros();
         RequestContext.getCurrentInstance().update("form:datosMetodoPago");
         guardado = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         RequestContext.getCurrentInstance().execute("PF('duplicarRegistroMetodosPagos').hide()");
      } else {
         RequestContext.getCurrentInstance().update("form:validacionNuevoMetodoPago");
         RequestContext.getCurrentInstance().execute("PF('validacionNuevoMetodoPago').show()");
         contador = 0;
      }
   }

   public void limpiarduplicarMetodoPago() {
      duplicarMetodoPago = new MetodosPagos();
   }

   public void exportPDF() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosMetodoPagoExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "MetodosPagos", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void exportXLS() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosMetodoPagoExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "MetodosPagos", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void verificarRastro() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (metodoPagoSeleccionado != null) {
         int resultado = administrarRastros.obtenerTabla(metodoPagoSeleccionado.getSecuencia(), "METODOSPAGOS"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
      } else if (administrarRastros.verificarHistoricosTabla("METODOSPAGOS")) { // igual acá
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
   }

   public void contarRegistros() {
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
   }
//*-*-*-*-*-*-*-*-*-*-GETS Y SETS*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*

   public List<MetodosPagos> getListMetodosPagos() {
      if (listMetodosPagos == null) {
         listMetodosPagos = administrarMetodosPagos.consultarMetodosPagos();
         for (int i = 0; i < listMetodosPagos.size(); i++) {
            System.out.println(listMetodosPagos.get(i).getSecuencia());
         }
      }
      return listMetodosPagos;
   }

   public void setListMetodosPagos(List<MetodosPagos> listMetodosPagos) {
      this.listMetodosPagos = listMetodosPagos;
   }

   public String getMensajeValidacion() {
      return mensajeValidacion;
   }

   public void setMensajeValidacion(String mensajeValidacion) {
      this.mensajeValidacion = mensajeValidacion;
   }

   public List<MetodosPagos> getFiltrarMetodosPagos() {
      return filtrarMetodosPagos;
   }

   public void setFiltrarMetodosPagos(List<MetodosPagos> filtrarMetodosPagos) {
      this.filtrarMetodosPagos = filtrarMetodosPagos;
   }

   public int getRegistrosBorrados() {
      return registrosBorrados;
   }

   public void setRegistrosBorrados(int registrosBorrados) {
      this.registrosBorrados = registrosBorrados;
   }

   public MetodosPagos getEditarMetodoPago() {
      return editarMetodoPago;
   }

   public void setEditarMetodoPago(MetodosPagos editarMetodoPago) {
      this.editarMetodoPago = editarMetodoPago;
   }

   public MetodosPagos getNuevoMetodoPago() {
      return nuevoMetodoPago;
   }

   public void setNuevoMetodoPago(MetodosPagos nuevoMetodoPago) {
      this.nuevoMetodoPago = nuevoMetodoPago;
   }

   public MetodosPagos getDuplicarMetodoPago() {
      return duplicarMetodoPago;
   }

   public void setDuplicarMetodoPago(MetodosPagos duplicarMetodoPago) {
      this.duplicarMetodoPago = duplicarMetodoPago;
   }

   public boolean isGuardado() {
      return guardado;
   }

   public void setGuardado(boolean guardado) {
      this.guardado = guardado;
   }

   public MetodosPagos getMetodoPagoSeleccionado() {
      return metodoPagoSeleccionado;
   }

   public void setMetodoPagoSeleccionado(MetodosPagos metodoPagoSeleccionado) {
      this.metodoPagoSeleccionado = metodoPagoSeleccionado;
   }

   public int getTamano() {
      return tamano;
   }

   public void setTamano(int tamano) {
      this.tamano = tamano;
   }

   public String getInfoRegistro() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosMetodoPago");
      infoRegistro = String.valueOf(tabla.getRowCount());
      return infoRegistro;
   }

   public void setInfoRegistro(String infoRegistro) {
      this.infoRegistro = infoRegistro;
   }

   public boolean isActivarLov() {
      return activarLov;
   }

   public void setActivarLov(boolean activarLov) {
      this.activarLov = activarLov;
   }

}
