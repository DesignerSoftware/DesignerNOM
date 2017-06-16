/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.TiposTallas;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarTiposTallasInterface;
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
public class ControlTiposTallas implements Serializable {

   @EJB
   AdministrarTiposTallasInterface administrarTiposTallas;
   @EJB
   AdministrarRastrosInterface administrarRastros;

   private List<TiposTallas> listTiposTallas;
   private List<TiposTallas> filtrarTiposTallas;
   private List<TiposTallas> crearTiposTallas;
   private List<TiposTallas> modificarTiposTallas;
   private List<TiposTallas> borrarTiposTallas;
   private TiposTallas nuevoTiposTallas;
   private TiposTallas duplicarTiposTallas;
   private TiposTallas editarTiposTallas;
   private TiposTallas tiposTallasSeleccionado;
   private int cualCelda, tipoLista, tipoActualizacion, k, bandera;
   private BigInteger l;
   private boolean aceptar, guardado;
   private boolean permitirIndex;
   private Column codigo, descripcion;
   private int registrosBorrados;
   private String mensajeValidacion;
   private int tamano;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();
   private String infoRegistro;
   private boolean activarLov;

   public ControlTiposTallas() {
      listTiposTallas = null;
      crearTiposTallas = new ArrayList<TiposTallas>();
      modificarTiposTallas = new ArrayList<TiposTallas>();
      borrarTiposTallas = new ArrayList<TiposTallas>();
      permitirIndex = true;
      editarTiposTallas = new TiposTallas();
      nuevoTiposTallas = new TiposTallas();
      duplicarTiposTallas = new TiposTallas();
      guardado = true;
      tamano = 330;
      mapParametros.put("paginaAnterior", paginaAnterior);
      activarLov = true;
   }

   public void limpiarListasValor() {

   }

   @PostConstruct
   public void inicializarAdministrador() {
      try {
         System.out.println("ControlTiposTallas PostConstruct ");
         FacesContext x = FacesContext.getCurrentInstance();
         HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
         administrarTiposTallas.obtenerConexion(ses.getId());
         administrarRastros.obtenerConexion(ses.getId());
      } catch (Exception e) {
         System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
         System.out.println("Causa: " + e.getCause());
      }
   }

   public void recibirPaginaEntrante(String pagina) {
      paginaAnterior = pagina;
      listTiposTallas = null;
      getListTiposTallas();
      if (listTiposTallas != null) {
         if (!listTiposTallas.isEmpty()) {
            tiposTallasSeleccionado = listTiposTallas.get(0);
         }
      }
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
      String pagActual = "tipotalla";
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

   public void cambiarIndice(TiposTallas tipoT, int celda) {
      if (permitirIndex == true) {
         tiposTallasSeleccionado = tipoT;
         cualCelda = celda;
         if (cualCelda == 0) {
            tiposTallasSeleccionado.getCodigo();
         } else if (cualCelda == 1) {
            tiposTallasSeleccionado.getDescripcion();
         }
         tiposTallasSeleccionado.getSecuencia();
      }
   }

   public void activarAceptar() {
      aceptar = false;
   }

   public void cancelarModificacion() {
      if (bandera == 1) {
         //CERRAR FILTRADO
         FacesContext c = FacesContext.getCurrentInstance();
         codigo = (Column) c.getViewRoot().findComponent("form:datosTiposTallas:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposTallas:descripcion");
         descripcion.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosTiposTallas");
         bandera = 0;
         filtrarTiposTallas = null;
         tipoLista = 0;
         tamano = 330;
      }
      borrarTiposTallas.clear();
      crearTiposTallas.clear();
      modificarTiposTallas.clear();
      tiposTallasSeleccionado = null;
      k = 0;
      listTiposTallas = null;
      guardado = true;
      permitirIndex = true;
      getListTiposTallas();
      contarRegistros();
      RequestContext.getCurrentInstance().update("form:datosTiposTallas");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void salir() {
      limpiarListasValor();
      if (bandera == 1) {
         FacesContext c = FacesContext.getCurrentInstance();
         codigo = (Column) c.getViewRoot().findComponent("form:datosTiposTallas:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposTallas:descripcion");
         descripcion.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosTiposTallas");
         bandera = 0;
         filtrarTiposTallas = null;
         tipoLista = 0;
         tamano = 330;
      }
      borrarTiposTallas.clear();
      crearTiposTallas.clear();
      modificarTiposTallas.clear();
      tiposTallasSeleccionado = null;
      k = 0;
      listTiposTallas = null;
      guardado = true;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:datosTiposTallas");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      navegar("atras");
   }

   public void activarCtrlF11() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 0) {
         tamano = 310;
         codigo = (Column) c.getViewRoot().findComponent("form:datosTiposTallas:codigo");
         codigo.setFilterStyle("width: 85% !important;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposTallas:descripcion");
         descripcion.setFilterStyle("width: 85% !important;");
         RequestContext.getCurrentInstance().update("form:datosTiposTallas");
         System.out.println("Activar");
         bandera = 1;
      } else if (bandera == 1) {
         tamano = 330;
         codigo = (Column) c.getViewRoot().findComponent("form:datosTiposTallas:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposTallas:descripcion");
         descripcion.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosTiposTallas");
         bandera = 0;
         filtrarTiposTallas = null;
         tipoLista = 0;
      }
   }

   public void modificarTiposTallas(TiposTallas tipoT) {
      tiposTallasSeleccionado = tipoT;
      if (!crearTiposTallas.contains(tiposTallasSeleccionado)) {
         if (modificarTiposTallas.isEmpty()) {
            modificarTiposTallas.add(tiposTallasSeleccionado);
         } else if (!modificarTiposTallas.contains(tiposTallasSeleccionado)) {
            modificarTiposTallas.add(tiposTallasSeleccionado);
         }
         guardado = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      }
      RequestContext.getCurrentInstance().update("form:datosTiposTallas");
   }

   public void borrandoTiposTallas() {
      if (tiposTallasSeleccionado != null) {
         System.out.println("Entro a borrandoTiposTallas");
         if (!modificarTiposTallas.isEmpty() && modificarTiposTallas.contains(tiposTallasSeleccionado)) {
            int modIndex = modificarTiposTallas.indexOf(tiposTallasSeleccionado);
            modificarTiposTallas.remove(modIndex);
            borrarTiposTallas.add(tiposTallasSeleccionado);
         } else if (!crearTiposTallas.isEmpty() && crearTiposTallas.contains(tiposTallasSeleccionado)) {
            int crearIndex = crearTiposTallas.indexOf(tiposTallasSeleccionado);
            crearTiposTallas.remove(crearIndex);
         } else {
            borrarTiposTallas.add(tiposTallasSeleccionado);
         }
         listTiposTallas.remove(tiposTallasSeleccionado);
         if (tipoLista == 1) {
            filtrarTiposTallas.remove(tiposTallasSeleccionado);
         }
         tiposTallasSeleccionado = null;
         RequestContext.getCurrentInstance().update("form:datosTiposTallas");
         contarRegistros();
         guardado = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void verificarBorrado() {
      BigInteger contarElementosTipoTalla;
      BigInteger contarVigenciasTallasTipoTalla;
      try {
         contarElementosTipoTalla = administrarTiposTallas.contarElementosTipoTalla(tiposTallasSeleccionado.getSecuencia());
         contarVigenciasTallasTipoTalla = administrarTiposTallas.contarVigenciasTallasTipoTalla(tiposTallasSeleccionado.getSecuencia());
         if (contarElementosTipoTalla.equals(new BigInteger("0"))
                 && contarVigenciasTallasTipoTalla.equals(new BigInteger("0"))) {
            borrandoTiposTallas();
         } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:validacionBorrar");
            RequestContext.getCurrentInstance().execute("PF('validacionBorrar').show()");
            contarElementosTipoTalla = new BigInteger("-1");
         }
      } catch (Exception e) {
         System.err.println("ERROR ControlTiposTallas verificarBorrado ERROR " + e);
      }
   }

   public void revisarDialogoGuardar() {
      if (!borrarTiposTallas.isEmpty() || !crearTiposTallas.isEmpty() || !modificarTiposTallas.isEmpty()) {
         RequestContext.getCurrentInstance().update("form:confirmarGuardar");
         RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
      }
   }

   public void guardarYSalir() {
      guardarTiposTallas();
      salir();
   }

   public void guardarTiposTallas() {
      if (guardado == false) {
         System.out.println("Realizando guardarTiposTallas");
         if (!borrarTiposTallas.isEmpty()) {
            administrarTiposTallas.borrarTiposTallas(borrarTiposTallas);
            registrosBorrados = borrarTiposTallas.size();
            RequestContext.getCurrentInstance().update("form:mostrarBorrados");
            RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
            borrarTiposTallas.clear();
         }
         if (!modificarTiposTallas.isEmpty()) {
            administrarTiposTallas.modificarTiposTallas(modificarTiposTallas);
            modificarTiposTallas.clear();
         }
         if (!crearTiposTallas.isEmpty()) {
            administrarTiposTallas.crearTiposTallas(crearTiposTallas);
            crearTiposTallas.clear();
         }
         listTiposTallas = null;
         RequestContext.getCurrentInstance().update("form:datosTiposTallas");
         k = 0;
         guardado = true;
         FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      }
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void editarCelda() {
      if (tiposTallasSeleccionado != null) {
         editarTiposTallas = tiposTallasSeleccionado;
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

   public void agregarNuevoTiposTallas() {
      int contador = 0;
      int duplicados = 0;
      Integer a = 0;
      a = null;
      mensajeValidacion = " ";
      RequestContext context = RequestContext.getCurrentInstance();
      if (nuevoTiposTallas.getCodigo() == a) {
         mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
      } else {
         for (int x = 0; x < listTiposTallas.size(); x++) {
            if (listTiposTallas.get(x).getCodigo().equals(nuevoTiposTallas.getCodigo())) {
               duplicados++;
            }
         }
         if (duplicados > 0) {
            mensajeValidacion = "El código ingresado está en uso. Por favor ingrese un código válido";
         } else {
            contador++;
         }
      }
      if (nuevoTiposTallas.getDescripcion() == null) {
         mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
      } else if (nuevoTiposTallas.getDescripcion().isEmpty()) {
         mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
      } else {
         contador++;
      }
      if (contador == 2) {
         if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            //CERRAR FILTRADO
            System.out.println("Desactivar");
            codigo = (Column) c.getViewRoot().findComponent("form:datosTiposTallas:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposTallas:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTiposTallas");
            bandera = 0;
            filtrarTiposTallas = null;
            tipoLista = 0;
         }
         k++;
         l = BigInteger.valueOf(k);
         nuevoTiposTallas.setSecuencia(l);
         crearTiposTallas.add(nuevoTiposTallas);
         listTiposTallas.add(nuevoTiposTallas);
         tiposTallasSeleccionado = nuevoTiposTallas;
         nuevoTiposTallas = new TiposTallas();
         RequestContext.getCurrentInstance().update("form:datosTiposTallas");
         contarRegistros();
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         RequestContext.getCurrentInstance().execute("PF('nuevoRegistroTiposTallas').hide()");
      } else {
         RequestContext.getCurrentInstance().update("form:validacionNuevaCentroCosto");
         RequestContext.getCurrentInstance().execute("PF('validacionNuevaCentroCosto').show()");
         contador = 0;
      }
   }

   public void limpiarNuevoTiposTallas() {
      nuevoTiposTallas = new TiposTallas();
   }

   public void duplicandoTiposTallas() {
      if (tiposTallasSeleccionado != null) {
         duplicarTiposTallas = new TiposTallas();
         k++;
         l = BigInteger.valueOf(k);

         duplicarTiposTallas.setSecuencia(l);
         duplicarTiposTallas.setCodigo(tiposTallasSeleccionado.getCodigo());
         duplicarTiposTallas.setDescripcion(tiposTallasSeleccionado.getDescripcion());
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTE");
         RequestContext.getCurrentInstance().execute("PF('duplicarRegistroTiposTallas').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void confirmarDuplicar() {
      int contador = 0;
      mensajeValidacion = " ";
      int duplicados = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      Integer a = 0;
      a = null;

      if (duplicarTiposTallas.getCodigo() == a) {
         mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
      } else {
         for (int x = 0; x < listTiposTallas.size(); x++) {
            if (listTiposTallas.get(x).getCodigo().equals(duplicarTiposTallas.getCodigo())) {
               duplicados++;
            }
         }
         if (duplicados > 0) {
            mensajeValidacion = "El código ingresado está en uso. Por favor ingrese un código válido";
         } else {
            contador++;
            duplicados = 0;
         }
      }
      if (duplicarTiposTallas.getDescripcion() == null) {
         mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
      } else if (duplicarTiposTallas.getDescripcion().isEmpty()) {
         mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
      } else {
         contador++;
      }
      if (contador == 2) {
         listTiposTallas.add(duplicarTiposTallas);
         crearTiposTallas.add(duplicarTiposTallas);
         tiposTallasSeleccionado = duplicarTiposTallas;
         RequestContext.getCurrentInstance().update("form:datosTiposTallas");
         guardado = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         contarRegistros();

         if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            codigo = (Column) c.getViewRoot().findComponent("form:datosTiposTallas:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposTallas:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTiposTallas");
            bandera = 0;
            filtrarTiposTallas = null;
            tipoLista = 0;
         }
         duplicarTiposTallas = new TiposTallas();
         RequestContext.getCurrentInstance().execute("PF('duplicarRegistroTiposTallas').hide()");
      } else {
         contador = 0;
         RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
         RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
      }
   }

   public void limpiarDuplicarTiposTallas() {
      duplicarTiposTallas = new TiposTallas();
   }

   public void exportPDF() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTiposTallasExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "TIPOSTALLAS", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void exportXLS() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTiposTallasExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "TIPOSTALLAS", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void verificarRastro() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tiposTallasSeleccionado != null) {
         int resultado = administrarRastros.obtenerTabla(tiposTallasSeleccionado.getSecuencia(), "TIPOSTALLAS"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
      } else if (administrarRastros.verificarHistoricosTabla("TIPOSTALLAS")) { // igual acá
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
   }

   public void contarRegistros() {
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
   }

   //*/*/*/*/*/*/*/*/*/*-/-*//-*/-*/*/*-*/-*/-*/*/*/*/*/---/*/*/*/*/-*/-*/-*/-*/-*/
   public List<TiposTallas> getListTiposTallas() {
      if (listTiposTallas == null) {
         listTiposTallas = administrarTiposTallas.consultarTiposTallas();
      }
      return listTiposTallas;
   }

   public void setListTiposTallas(List<TiposTallas> listTiposTallas) {
      this.listTiposTallas = listTiposTallas;
   }

   public List<TiposTallas> getFiltrarTiposTallas() {
      return filtrarTiposTallas;
   }

   public void setFiltrarTiposTallas(List<TiposTallas> filtrarTiposTallas) {
      this.filtrarTiposTallas = filtrarTiposTallas;
   }

   public TiposTallas getNuevoTiposTallas() {
      return nuevoTiposTallas;
   }

   public void setNuevoTiposTallas(TiposTallas nuevoTiposTallas) {
      this.nuevoTiposTallas = nuevoTiposTallas;
   }

   public TiposTallas getDuplicarTiposTallas() {
      return duplicarTiposTallas;
   }

   public void setDuplicarTiposTallas(TiposTallas duplicarTiposTallas) {
      this.duplicarTiposTallas = duplicarTiposTallas;
   }

   public TiposTallas getEditarTiposTallas() {
      return editarTiposTallas;
   }

   public void setEditarTiposTallas(TiposTallas editarTiposTallas) {
      this.editarTiposTallas = editarTiposTallas;
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

   public TiposTallas getTiposTallasSeleccionado() {
      return tiposTallasSeleccionado;
   }

   public void setTiposTallasSeleccionado(TiposTallas clasesPensionesSeleccionado) {
      this.tiposTallasSeleccionado = clasesPensionesSeleccionado;
   }

   public String getInfoRegistro() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosTiposTallas");
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
