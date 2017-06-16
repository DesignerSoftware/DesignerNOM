/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Tipospagos;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarTiposPagosInterface;
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
public class ControlTiposPagos implements Serializable {

   @EJB
   AdministrarTiposPagosInterface administrarTiposPagos;
   @EJB
   AdministrarRastrosInterface administrarRastros;

   private List<Tipospagos> listTiposPagos;
   private List<Tipospagos> filtrarTiposPagos;
   private List<Tipospagos> crearTiposPagos;
   private List<Tipospagos> modificarTiposPagos;
   private List<Tipospagos> borrarTiposPagos;
   private Tipospagos nuevoTiposPagos;
   private Tipospagos duplicarTiposPagos;
   private Tipospagos editarTiposPagos;
   private Tipospagos tipospagosSeleccionado;
   private int cualCelda, tipoLista, tipoActualizacion, k, bandera;
   private BigInteger l;
   private boolean aceptar, guardado;
   private boolean permitirIndex;
   private Column codigo, descripcion;
   private int registrosBorrados;
   private String mensajeValidacion;
   private int tamano;
   private Integer backUpCodigo;
   private String backUpDescripcion;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

   private String infoRegistro;

   public ControlTiposPagos() {
      listTiposPagos = null;
      crearTiposPagos = new ArrayList<Tipospagos>();
      modificarTiposPagos = new ArrayList<Tipospagos>();
      borrarTiposPagos = new ArrayList<Tipospagos>();
      permitirIndex = true;
      editarTiposPagos = new Tipospagos();
      nuevoTiposPagos = new Tipospagos();
      duplicarTiposPagos = new Tipospagos();
      guardado = true;
      tamano = 320;
      mapParametros.put("paginaAnterior", paginaAnterior);
   }

   public void limpiarListasValor() {

   }

   @PostConstruct
   public void inicializarAdministrador() {
      try {
         System.out.println("ControlTiposPagos PostConstruct ");
         FacesContext x = FacesContext.getCurrentInstance();
         HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
         administrarTiposPagos.obtenerConexion(ses.getId());
         administrarRastros.obtenerConexion(ses.getId());
      } catch (Exception e) {
         System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
         System.out.println("Causa: " + e.getCause());
      }
   }

   public void recibirPaginaEntrante(String pagina) {
      paginaAnterior = pagina;
      listTiposPagos = null;
      getListTiposPagos();
      if (listTiposPagos != null) {
         if (!listTiposPagos.isEmpty()) {
            tipospagosSeleccionado = listTiposPagos.get(0);
         }
      }

   }

   public void recibirParametros(Map<String, Object> map) {
      mapParametros = map;
      paginaAnterior = (String) mapParametros.get("paginaAnterior");
   }

   public void navegar(String pag) {
      FacesContext fc = FacesContext.getCurrentInstance();
      ControlListaNavegacion controlListaNavegacion = (ControlListaNavegacion) fc.getApplication().evaluateExpressionGet(fc, "#{controlListaNavegacion}", ControlListaNavegacion.class);
      String pagActual = "tipopago";
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

   public void eventoFiltrar() {
      if (tipoLista == 0) {
         tipoLista = 1;
      }
      contarRegistros();
   }

   public void contarRegistros() {
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
   }

   public void cambiarIndice(Tipospagos tipopago, int celda) {
      tipospagosSeleccionado = tipopago;
      cualCelda = celda;
      tipospagosSeleccionado.getSecuencia();
      if (cualCelda == 0) {
         backUpCodigo = tipospagosSeleccionado.getCodigo();
      } else if (cualCelda == 1) {
         backUpDescripcion = tipospagosSeleccionado.getDescripcion();
      }
   }

   public void activarAceptar() {
      aceptar = false;
   }

   public void cancelarModificacion() {
      if (bandera == 1) {
         FacesContext c = FacesContext.getCurrentInstance();
         codigo = (Column) c.getViewRoot().findComponent("form:datosTiposPagos:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposPagos:descripcion");
         descripcion.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosTiposPagos");
         bandera = 0;
         filtrarTiposPagos = null;
         tipoLista = 0;
         tamano = 320;
      }
      borrarTiposPagos.clear();
      crearTiposPagos.clear();
      modificarTiposPagos.clear();
      tipospagosSeleccionado = null;
      k = 0;
      listTiposPagos = null;
      guardado = true;
      permitirIndex = true;
      getListTiposPagos();
      contarRegistros();
      RequestContext.getCurrentInstance().update("form:datosTiposPagos");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void salir() {
      limpiarListasValor();
      if (bandera == 1) {
         FacesContext c = FacesContext.getCurrentInstance();
         codigo = (Column) c.getViewRoot().findComponent("form:datosTiposPagos:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposPagos:descripcion");
         descripcion.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosTiposPagos");
         bandera = 0;
         filtrarTiposPagos = null;
         tipoLista = 0;
         tamano = 320;
      }
      borrarTiposPagos.clear();
      crearTiposPagos.clear();
      modificarTiposPagos.clear();
      tipospagosSeleccionado = null;
      k = 0;
      listTiposPagos = null;
      guardado = true;
      permitirIndex = true;
      RequestContext.getCurrentInstance().update("form:datosTiposPagos");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      navegar("atras");
   }

   public void activarCtrlF11() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 0) {
         tamano = 300;
         codigo = (Column) c.getViewRoot().findComponent("form:datosTiposPagos:codigo");
         codigo.setFilterStyle("width: 85% !important;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposPagos:descripcion");
         descripcion.setFilterStyle("width: 85% !important;");
         RequestContext.getCurrentInstance().update("form:datosTiposPagos");
         System.out.println("Activar");
         bandera = 1;
      } else if (bandera == 1) {
         System.out.println("Desactivar");
         tamano = 320;
         codigo = (Column) c.getViewRoot().findComponent("form:datosTiposPagos:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposPagos:descripcion");
         descripcion.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosTiposPagos");
         bandera = 0;
         filtrarTiposPagos = null;
         tipoLista = 0;
      }
   }

   public void modificarTiposPagos(Tipospagos tipopago) {
      tipospagosSeleccionado = tipopago;
      if (!crearTiposPagos.contains(tipospagosSeleccionado)) {
         if (modificarTiposPagos.isEmpty()) {
            modificarTiposPagos.add(tipospagosSeleccionado);
         } else if (!modificarTiposPagos.contains(tipospagosSeleccionado)) {
            modificarTiposPagos.add(tipospagosSeleccionado);
         }
         guardado = false;
         RequestContext.getCurrentInstance().update("form:datosTiposPagos");
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      }
   }

   public void borrandoTiposPagos() {
      if (tipospagosSeleccionado != null) {
         if (!modificarTiposPagos.isEmpty() && modificarTiposPagos.contains(tipospagosSeleccionado)) {
            int modIndex = modificarTiposPagos.indexOf(tipospagosSeleccionado);
            modificarTiposPagos.remove(modIndex);
            borrarTiposPagos.add(tipospagosSeleccionado);
         } else if (!crearTiposPagos.isEmpty() && crearTiposPagos.contains(tipospagosSeleccionado)) {
            int crearIndex = crearTiposPagos.indexOf(tipospagosSeleccionado);
            crearTiposPagos.remove(crearIndex);
         } else {
            borrarTiposPagos.add(tipospagosSeleccionado);
         }
         listTiposPagos.remove(tipospagosSeleccionado);
         if (tipoLista == 1) {
            filtrarTiposPagos.remove(tipospagosSeleccionado);
         }
         contarRegistros();
         tipospagosSeleccionado = null;
         guardado = false;
         RequestContext.getCurrentInstance().update("form:datosTiposPagos");
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      }
   }

   public void verificarBorrado() {
      BigInteger contarRetiradosClasePension;
      try {
         contarRetiradosClasePension = administrarTiposPagos.contarProcesosTipoPago(tipospagosSeleccionado.getSecuencia());
         if (contarRetiradosClasePension.equals(new BigInteger("0"))) {
            borrandoTiposPagos();
         } else {
            RequestContext.getCurrentInstance().update("form:validacionBorrar");
            RequestContext.getCurrentInstance().execute("PF('validacionBorrar').show()");
            contarRetiradosClasePension = new BigInteger("-1");
         }
      } catch (Exception e) {
         System.err.println("ERROR ControlTiposPagos verificarBorrado ERROR " + e);
      }
   }

   public void revisarDialogoGuardar() {
      if (!borrarTiposPagos.isEmpty() || !crearTiposPagos.isEmpty() || !modificarTiposPagos.isEmpty()) {
         RequestContext.getCurrentInstance().update("form:confirmarGuardar");
         RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
      }
   }

   public void guardarYSalir() {
      guardarTiposPagos();
      salir();
   }

   public void guardarTiposPagos() {
      if (guardado == false) {
         if (!borrarTiposPagos.isEmpty()) {
            administrarTiposPagos.borrarTiposPagos(borrarTiposPagos);
            registrosBorrados = borrarTiposPagos.size();
            RequestContext.getCurrentInstance().update("form:mostrarBorrados");
            RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
            borrarTiposPagos.clear();
         }
         if (!modificarTiposPagos.isEmpty()) {
            administrarTiposPagos.modificarTiposPagos(modificarTiposPagos);
            modificarTiposPagos.clear();
         }
         if (!crearTiposPagos.isEmpty()) {
            administrarTiposPagos.crearTiposPagos(crearTiposPagos);
            crearTiposPagos.clear();
         }
         listTiposPagos = null;
         tipospagosSeleccionado = null;
         RequestContext.getCurrentInstance().update("form:datosTiposPagos");
         k = 0;
         guardado = true;
         FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      }
      RequestContext.getCurrentInstance().update("form:ACEPTAR");

   }

   public void editarCelda() {
      if (tipospagosSeleccionado != null) {
         editarTiposPagos = tipospagosSeleccionado;
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

   public void agregarNuevoTiposPagos() {
      int contador = 0;
      int duplicados = 0;
      Integer a = 0;
      a = null;
      mensajeValidacion = " ";
      if (nuevoTiposPagos.getCodigo() == a) {
         mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
      } else {
         for (int x = 0; x < listTiposPagos.size(); x++) {
            if (listTiposPagos.get(x).getCodigo().equals(nuevoTiposPagos.getCodigo())) {
               duplicados++;
            }
         }
         if (duplicados > 0) {
            mensajeValidacion = "El código ingresado ya está en uso. Por favor ingrese un código válido";
         } else {
            contador++;
         }
      }
      if (nuevoTiposPagos.getDescripcion() == null) {
         mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
      } else if (nuevoTiposPagos.getDescripcion().isEmpty()) {
         mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
      } else {
         contador++;
      }

      if (contador == 2) {
         if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            codigo = (Column) c.getViewRoot().findComponent("form:datosTiposPagos:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposPagos:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTiposPagos");
            bandera = 0;
            filtrarTiposPagos = null;
            tipoLista = 0;
         }
         k++;
         l = BigInteger.valueOf(k);
         nuevoTiposPagos.setSecuencia(l);
         crearTiposPagos.add(nuevoTiposPagos);
         listTiposPagos.add(nuevoTiposPagos);
         tipospagosSeleccionado = nuevoTiposPagos;
         nuevoTiposPagos = new Tipospagos();
         RequestContext.getCurrentInstance().update("form:datosTiposPagos");
         contarRegistros();
         guardado = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         RequestContext.getCurrentInstance().execute("PF('nuevoRegistroTiposPagos').hide()");
      } else {
         RequestContext.getCurrentInstance().update("form:validacionNuevaCentroCosto");
         RequestContext.getCurrentInstance().execute("PF('validacionNuevaCentroCosto').show()");
         contador = 0;
      }
   }

   public void limpiarNuevoTiposPagos() {
      nuevoTiposPagos = new Tipospagos();
   }

   public void duplicandoTiposPagos() {
      if (tipospagosSeleccionado != null) {
         duplicarTiposPagos = new Tipospagos();
         k++;
         l = BigInteger.valueOf(k);
         if (tipoLista == 0) {
            duplicarTiposPagos.setSecuencia(l);
            duplicarTiposPagos.setCodigo(tipospagosSeleccionado.getCodigo());
            duplicarTiposPagos.setDescripcion(tipospagosSeleccionado.getDescripcion());
         }
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTE");
         RequestContext.getCurrentInstance().execute("PF('duplicarRegistroTiposPagos').show()");
      }
   }

   public void confirmarDuplicar() {
      int contador = 0;
      mensajeValidacion = " ";
      int duplicados = 0;
      Integer a = 0;
      a = null;

      if (duplicarTiposPagos.getCodigo() == a) {
         mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
      } else {
         for (int x = 0; x < listTiposPagos.size(); x++) {
            if (listTiposPagos.get(x).getCodigo().equals(duplicarTiposPagos.getCodigo())) {
               duplicados++;
            }
         }
         if (duplicados > 0) {
            mensajeValidacion = "El código ingresado ya está en uso. Por favor ingrese un código válido";
         } else {
            contador++;
            duplicados = 0;
         }
      }
      if (duplicarTiposPagos.getDescripcion() == null) {
         mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
      } else if (duplicarTiposPagos.getDescripcion().isEmpty()) {
         mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
      } else {
         contador++;
      }
      if (contador == 2) {
         listTiposPagos.add(duplicarTiposPagos);
         crearTiposPagos.add(duplicarTiposPagos);
         tipospagosSeleccionado = duplicarTiposPagos;
         RequestContext.getCurrentInstance().update("form:datosTiposPagos");
         guardado = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         contarRegistros();
         if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            codigo = (Column) c.getViewRoot().findComponent("form:datosTiposPagos:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposPagos:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTiposPagos");
            bandera = 0;
            filtrarTiposPagos = null;
            tipoLista = 0;
         }
         duplicarTiposPagos = new Tipospagos();
         RequestContext.getCurrentInstance().execute("PF('duplicarRegistroTiposPagos').hide()");
      } else {
         contador = 0;
         RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
         RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
      }
   }

   public void limpiarDuplicarTiposPagos() {
      duplicarTiposPagos = new Tipospagos();
   }

   public void exportPDF() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTiposPagosExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "TIPOSPAGOS", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void exportXLS() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTiposPagosExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "TIPOSPAGOS", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void verificarRastro() {
      if (tipospagosSeleccionado != null) {
         int resultado = administrarRastros.obtenerTabla(tipospagosSeleccionado.getSecuencia(), "TIPOSPAGOS");
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
      } else if (administrarRastros.verificarHistoricosTabla("TIPOSPAGOS")) {
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
   }

   //*/*/*/*/*/*/*/*/*/*-/-*//-*/-*/*/*-*/-*/-*/*/*/*/*/---/*/*/*/*/-*/-*/-*/-*/-*/
   public List<Tipospagos> getListTiposPagos() {
      if (listTiposPagos == null) {
         listTiposPagos = administrarTiposPagos.consultarTiposPagos();
      }
      return listTiposPagos;
   }

   public void setListTiposPagos(List<Tipospagos> listTiposPagos) {
      this.listTiposPagos = listTiposPagos;
   }

   public List<Tipospagos> getFiltrarTiposPagos() {
      return filtrarTiposPagos;
   }

   public void setFiltrarTiposPagos(List<Tipospagos> filtrarTiposPagos) {
      this.filtrarTiposPagos = filtrarTiposPagos;
   }

   public Tipospagos getNuevoTiposPagos() {
      return nuevoTiposPagos;
   }

   public void setNuevoTiposPagos(Tipospagos nuevoTiposPagos) {
      this.nuevoTiposPagos = nuevoTiposPagos;
   }

   public Tipospagos getDuplicarTiposPagos() {
      return duplicarTiposPagos;
   }

   public void setDuplicarTiposPagos(Tipospagos duplicarTiposPagos) {
      this.duplicarTiposPagos = duplicarTiposPagos;
   }

   public Tipospagos getEditarTiposPagos() {
      return editarTiposPagos;
   }

   public void setEditarTiposPagos(Tipospagos editarTiposPagos) {
      this.editarTiposPagos = editarTiposPagos;
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

   public Tipospagos getTiposPagosSeleccionado() {
      return tipospagosSeleccionado;
   }

   public void setTiposPagosSeleccionado(Tipospagos clasesPensionesSeleccionado) {
      this.tipospagosSeleccionado = clasesPensionesSeleccionado;
   }

   public String getInfoRegistro() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosTiposPagos");
      infoRegistro = String.valueOf(tabla.getRowCount());
      return infoRegistro;
   }

   public void setInfoRegistro(String infoRegistro) {
      this.infoRegistro = infoRegistro;
   }

}
