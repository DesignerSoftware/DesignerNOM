/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Enfoques;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarEnfoquesInterface;
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
public class ControlEnfoques implements Serializable {

   @EJB
   AdministrarEnfoquesInterface administrarEnfoques;
   @EJB
   AdministrarRastrosInterface administrarRastros;

   private List<Enfoques> listEnfoques;
   private List<Enfoques> filtrarEnfoques;
   private List<Enfoques> crearEnfoques;
   private List<Enfoques> modificarEnfoques;
   private List<Enfoques> borrarEnfoques;
   private Enfoques nuevoEnfoque;
   private Enfoques duplicarEnfoque;
   private Enfoques editarEnfoque;
   private Enfoques enfoqueSeleccionado;
   //otros
   private int cualCelda, tipoLista, tipoActualizacion, k, bandera;
   private BigInteger l;
   private boolean aceptar, guardado;
   //AutoCompletar
   private boolean permitirIndex;
   //RASTRO
   private Column codigo, descripcion;
   //borrado
   private int registrosBorrados;
   private String mensajeValidacion;
   private int tamano;
   private boolean activarLov;
   private String infoRegistro;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

   public ControlEnfoques() {
      listEnfoques = null;
      crearEnfoques = new ArrayList<Enfoques>();
      modificarEnfoques = new ArrayList<Enfoques>();
      borrarEnfoques = new ArrayList<Enfoques>();
      permitirIndex = true;
      editarEnfoque = new Enfoques();
      nuevoEnfoque = new Enfoques();
      duplicarEnfoque = new Enfoques();
      tamano = 270;
      guardado = true;
      activarLov = true;
      tipoLista = 0;
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
      if (pag.equals("atras")) {
         pag = paginaAnterior;
         paginaAnterior = "nominaf";
         controlListaNavegacion.quitarPagina();
      } else {
         String pagActual = "enfoque";
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

  public void limpiarListasValor() {

   }

   @PostConstruct
   public void inicializarAdministrador() {
      try {
         FacesContext x = FacesContext.getCurrentInstance();
         HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
         administrarEnfoques.obtenerConexion(ses.getId());
         administrarRastros.obtenerConexion(ses.getId());
      } catch (Exception e) {
         System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
         System.out.println("Causa: " + e.getCause());
      }
   }

   public void cambiarIndice(Enfoques enfoque, int celda) {
      if (permitirIndex == true) {
         enfoqueSeleccionado = enfoque;
         cualCelda = celda;
         enfoqueSeleccionado.getSecuencia();
         if (cualCelda == 0) {
            enfoqueSeleccionado.getCodigo();
         }
         if (cualCelda == 1) {
            enfoqueSeleccionado.getDescripcion();
         }
      }
   }

   public void activarAceptar() {
      aceptar = false;
   }

   public void cancelarModificacion() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 1) {
         codigo = (Column) c.getViewRoot().findComponent("form:datosEnfoque:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosEnfoque:descripcion");
         descripcion.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosEnfoque");
         bandera = 0;
         filtrarEnfoques = null;
         tipoLista = 0;
         tamano = 270;
      }

      borrarEnfoques.clear();
      crearEnfoques.clear();
      modificarEnfoques.clear();
      enfoqueSeleccionado = null;
      k = 0;
      listEnfoques = null;
      guardado = true;
      permitirIndex = true;
      getListEnfoques();
      contarRegistros();
      RequestContext.getCurrentInstance().update("form:datosEnfoque");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void salir() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 1) {
         codigo = (Column) c.getViewRoot().findComponent("form:datosEnfoque:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosEnfoque:descripcion");
         descripcion.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosEnfoque");
         bandera = 0;
         filtrarEnfoques = null;
         tipoLista = 0;
         tamano = 270;
      }
      borrarEnfoques.clear();
      crearEnfoques.clear();
      modificarEnfoques.clear();
      enfoqueSeleccionado = null;
      k = 0;
      listEnfoques = null;
      guardado = true;
      permitirIndex = true;
      getListEnfoques();
      RequestContext context = RequestContext.getCurrentInstance();
      contarRegistros();
      RequestContext.getCurrentInstance().update("form:datosEnfoque");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void activarCtrlF11() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 0) {
         tamano = 250;
         codigo = (Column) c.getViewRoot().findComponent("form:datosEnfoque:codigo");
         codigo.setFilterStyle("width: 85% !important;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosEnfoque:descripcion");
         descripcion.setFilterStyle("width: 85% !important;");
         RequestContext.getCurrentInstance().update("form:datosEnfoque");
         System.out.println("Activar");
         bandera = 1;
      } else if (bandera == 1) {
         System.out.println("Desactivar");
         codigo = (Column) c.getViewRoot().findComponent("form:datosEnfoque:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosEnfoque:descripcion");
         descripcion.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosEnfoque");
         bandera = 0;
         filtrarEnfoques = null;
         tamano = 270;
      }
   }

   public void modificarEnfoques(Enfoques enfoque) {
      enfoqueSeleccionado = enfoque;
      if (!crearEnfoques.contains(enfoqueSeleccionado)) {
         if (modificarEnfoques.isEmpty()) {
            modificarEnfoques.add(enfoqueSeleccionado);
         } else if (!modificarEnfoques.contains(enfoqueSeleccionado)) {
            modificarEnfoques.add(enfoqueSeleccionado);
         }
         if (guardado == true) {
            guardado = false;
         }
      }
      RequestContext.getCurrentInstance().update("form:datosEnfoque");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void borrandoEnfoques() {

      if (enfoqueSeleccionado != null) {
         System.out.println("Entro a borrardatosEnfoques");
         if (!modificarEnfoques.isEmpty() && modificarEnfoques.contains(enfoqueSeleccionado)) {
            int modIndex = modificarEnfoques.indexOf(enfoqueSeleccionado);
            modificarEnfoques.remove(modIndex);
            borrarEnfoques.add(enfoqueSeleccionado);
         } else if (!crearEnfoques.isEmpty() && crearEnfoques.contains(enfoqueSeleccionado)) {
            int crearIndex = crearEnfoques.indexOf(enfoqueSeleccionado);
            crearEnfoques.remove(crearIndex);
         } else {
            borrarEnfoques.add(enfoqueSeleccionado);
         }
         listEnfoques.remove(enfoqueSeleccionado);
         if (tipoLista == 1) {
            filtrarEnfoques.remove(enfoqueSeleccionado);

         }
         RequestContext context = RequestContext.getCurrentInstance();
         enfoqueSeleccionado = null;
         contarRegistros();
         if (guardado == true) {
            guardado = false;
         }
         RequestContext.getCurrentInstance().update("form:datosEnfoque");
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistros').show()");
      }
   }

   public void guardarEnfoques() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (guardado == false) {
         if (!borrarEnfoques.isEmpty()) {
            administrarEnfoques.borrarEnfoques(borrarEnfoques);
            registrosBorrados = borrarEnfoques.size();
            RequestContext.getCurrentInstance().update("form:mostrarBorrados");
            RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
            borrarEnfoques.clear();
         }
         if (!crearEnfoques.isEmpty()) {
            administrarEnfoques.crearEnfoques(crearEnfoques);
            crearEnfoques.clear();
         }
         if (!modificarEnfoques.isEmpty()) {
            administrarEnfoques.modificarEnfoques(modificarEnfoques);
            modificarEnfoques.clear();
         }
         System.out.println("Se guardaron los datos con exito");
         listEnfoques = null;
         FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
         RequestContext.getCurrentInstance().update("form:datosEnfoque");
         k = 0;
         guardado = true;
      }
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void editarCelda() {
      if (enfoqueSeleccionado != null) {
         editarEnfoque = enfoqueSeleccionado;
         RequestContext context = RequestContext.getCurrentInstance();
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
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistros').show()");
      }
   }

   public void agregarNuevoEnfoque() {
      int contador = 0;
      int duplicados = 0;
      Integer a = 0;
      a = null;
      mensajeValidacion = " ";
      RequestContext context = RequestContext.getCurrentInstance();
      if (nuevoEnfoque.getCodigo() == a) {
         mensajeValidacion = " Los campos marcados con asterisco son obligatorios";
      } else {
         for (int x = 0; x < listEnfoques.size(); x++) {
            if (listEnfoques.get(x).getCodigo() == nuevoEnfoque.getCodigo()) {
               duplicados++;
            }
         }

         if (duplicados > 0) {
            mensajeValidacion = "El código ingresado ya existe, por favor ingrese otro ";
         } else {
            contador++;
         }
      }
      if (nuevoEnfoque.getDescripcion() == (null)) {
         mensajeValidacion = " Los campos marcados con asterisco son obligatorios";
      } else {
         System.out.println("bandera");
         contador++;

      }
      if (contador == 2) {
         FacesContext c = FacesContext.getCurrentInstance();
         if (bandera == 1) {
            //CERRAR FILTRADO
            System.out.println("Desactivar");
            codigo = (Column) c.getViewRoot().findComponent("form:datosEnfoque:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosEnfoque:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosEnfoque");
            bandera = 0;
            filtrarEnfoques = null;
            tipoLista = 0;
         }
         k++;
         l = BigInteger.valueOf(k);
         nuevoEnfoque.setSecuencia(l);
         crearEnfoques.add(nuevoEnfoque);
         listEnfoques.add(nuevoEnfoque);
         enfoqueSeleccionado = nuevoEnfoque;
         RequestContext.getCurrentInstance().update("form:datosEnfoque");
         contarRegistros();
         nuevoEnfoque = new Enfoques();
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         System.out.println("Despues de la bandera guardado");

         RequestContext.getCurrentInstance().execute("PF('nuevoRegistroEnfoque').hide()");
      } else {
         RequestContext.getCurrentInstance().update("form:validacionNuevoEnfoque");
         RequestContext.getCurrentInstance().execute("PF('validacionNuevoEnfoque').show()");
      }
   }

   public void limpiarNuevoEnfoque() {
      System.out.println("limpiarnuevoEnfoques");
      nuevoEnfoque = new Enfoques();
   }

   public void duplicarEnfoques() {
      System.out.println("duplicarEnfoques");
      if (enfoqueSeleccionado != null) {
         duplicarEnfoque = new Enfoques();
         k++;
         l = BigInteger.valueOf(k);

         duplicarEnfoque.setSecuencia(l);
         duplicarEnfoque.setCodigo(enfoqueSeleccionado.getCodigo());
         duplicarEnfoque.setDescripcion(enfoqueSeleccionado.getDescripcion());

         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarEnfoques");
         RequestContext.getCurrentInstance().execute("PF('duplicarRegistroEnfoque').show()");
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
      System.err.println("ConfirmarDuplicar codigo " + duplicarEnfoque.getCodigo());
      System.err.println("ConfirmarDuplicar descripcion " + duplicarEnfoque.getDescripcion());

      if (duplicarEnfoque.getCodigo() == a) {
         mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
      } else {
         for (int x = 0; x < listEnfoques.size(); x++) {
            if (listEnfoques.get(x).getCodigo() == duplicarEnfoque.getCodigo()) {
               duplicados++;
            }
         }
         if (duplicados > 0) {
            mensajeValidacion = " El código ingresado ya existe. Por favor ingrese otro código";
         } else {
            System.out.println("bandera");
            contador++;
            duplicados = 0;
         }
      }
      if (duplicarEnfoque.getDescripcion().isEmpty()) {
         mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
      } else {
         System.out.println("Bandera : ");
         contador++;
      }

      if (contador == 2) {
         listEnfoques.add(duplicarEnfoque);
         crearEnfoques.add(duplicarEnfoque);
         RequestContext.getCurrentInstance().update("form:datosEnfoque");
         contarRegistros();
         enfoqueSeleccionado = duplicarEnfoque;
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            //CERRAR FILTRADO
            codigo = (Column) c.getViewRoot().findComponent("form:datosEnfoque:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosEnfoque:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosEnfoque");
            bandera = 0;
            filtrarEnfoques = null;
            tipoLista = 0;
         }
         duplicarEnfoque = new Enfoques();
         RequestContext.getCurrentInstance().execute("PF('duplicarRegistroEnfoque').hide()");

      } else {
         contador = 0;
         RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
         RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
      }
   }

   public void limpiarduplicarEnfoque() {
      duplicarEnfoque = new Enfoques();
   }

   public void exportPDF() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosEnfoqueExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "Enfoque", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void exportXLS() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosEnfoqueExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "Enfoque", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void verificarRastro() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (enfoqueSeleccionado != null) {
         int resultado = administrarRastros.obtenerTabla(enfoqueSeleccionado.getSecuencia(), "ENFOQUES"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
      } else if (administrarRastros.verificarHistoricosTabla("ENFOQUES")) { // igual acá
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
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

   //--/-*/-*/-*/-*/*/*/*/*/****/////-----*****/////-*/-*/*/*/-*/-*/-*/-*/-*/-*/-*/
   public List<Enfoques> getListEnfoques() {
      if (listEnfoques == null) {
         listEnfoques = administrarEnfoques.consultarEnfoques();
      }
      return listEnfoques;
   }

   public void setListEnfoques(List<Enfoques> listEnfoques) {
      this.listEnfoques = listEnfoques;
   }

   public List<Enfoques> getFiltrarEnfoques() {
      return filtrarEnfoques;
   }

   public void setFiltrarEnfoques(List<Enfoques> filtrarEnfoques) {
      this.filtrarEnfoques = filtrarEnfoques;
   }

   public Enfoques getDuplicarEnfoque() {
      return duplicarEnfoque;
   }

   public void setDuplicarEnfoque(Enfoques duplicarEnfoque) {
      this.duplicarEnfoque = duplicarEnfoque;
   }

   public Enfoques getEditarEnfoque() {
      return editarEnfoque;
   }

   public void setEditarEnfoque(Enfoques editarEnfoque) {
      this.editarEnfoque = editarEnfoque;
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

   public Enfoques getNuevoEnfoque() {
      return nuevoEnfoque;
   }

   public void setNuevoEnfoque(Enfoques nuevoEnfoque) {
      this.nuevoEnfoque = nuevoEnfoque;
   }

   public boolean isGuardado() {
      return guardado;
   }

   public void setGuardado(boolean guardado) {
      this.guardado = guardado;
   }

   public Enfoques getEnfoqueSeleccionado() {
      return enfoqueSeleccionado;
   }

   public void setEnfoqueSeleccionado(Enfoques enfoqueSeleccionado) {
      this.enfoqueSeleccionado = enfoqueSeleccionado;
   }

   public int getTamano() {
      return tamano;
   }

   public void setTamano(int tamano) {
      this.tamano = tamano;
   }

   public String getInfoRegistro() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosEnfoque");
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

   public boolean isAceptar() {
      return aceptar;
   }

   public void setAceptar(boolean aceptar) {
      this.aceptar = aceptar;
   }

}
