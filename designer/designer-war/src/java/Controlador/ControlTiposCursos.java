/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.TiposCursos;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarTiposCursosInterface;
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
public class ControlTiposCursos implements Serializable {

   private static Logger log = Logger.getLogger(ControlTiposCursos.class);

   @EJB
   AdministrarTiposCursosInterface administrarTiposCursos;
   @EJB
   AdministrarRastrosInterface administrarRastros;

   private List<TiposCursos> listTiposCursos;
   private List<TiposCursos> filtrarTiposCursos;
   private List<TiposCursos> crearTiposCursos;
   private List<TiposCursos> modificarTiposCursos;
   private List<TiposCursos> borrarTiposCursos;
   private TiposCursos nuevoTiposCursos;
   private TiposCursos duplicarTiposCursos;
   private TiposCursos editarTiposCursos;
   private TiposCursos tipoCursoSeleccionado;
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
   //filtrado table
   private int tamano;
   private Integer backUpCodigo;
   private String backUpDescripcion;
   private boolean activarLov;
   private String infoRegistro;
   private DataTable tablaC;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

   public ControlTiposCursos() {
      listTiposCursos = null;
      crearTiposCursos = new ArrayList<TiposCursos>();
      modificarTiposCursos = new ArrayList<TiposCursos>();
      borrarTiposCursos = new ArrayList<TiposCursos>();
      permitirIndex = true;
      editarTiposCursos = new TiposCursos();
      nuevoTiposCursos = new TiposCursos();
      duplicarTiposCursos = new TiposCursos();
      guardado = true;
      tamano = 270;
      cualCelda = -1;
      tipoCursoSeleccionado = null;
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
      try {
         FacesContext x = FacesContext.getCurrentInstance();
         HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
         administrarTiposCursos.obtenerConexion(ses.getId());
         administrarRastros.obtenerConexion(ses.getId());
      } catch (Exception e) {
         log.error("Error postconstruct " + this.getClass().getName() + ":  ", e);
         log.error("Causa: " + e.getCause());
      }
   }

   public void recibirPaginaEntrante(String pagina) {
      paginaAnterior = pagina;
      listTiposCursos = null;
      getListTiposCursos();
      deshabilitarBotonLov();
      if (listTiposCursos != null) {
         tipoCursoSeleccionado = listTiposCursos.get(0);
      }
   }

   public void recibirParametros(Map<String, Object> map) {
      mapParametros = map;
      paginaAnterior = (String) mapParametros.get("paginaAnterior");
      listTiposCursos = null;
      getListTiposCursos();
      deshabilitarBotonLov();
      if (listTiposCursos != null) {
         tipoCursoSeleccionado = listTiposCursos.get(0);
      }
   }

   //Reemplazar la funcion volverAtras, retornarPagina, Redirigir.....Atras.etc
   public void navegar(String pag) {
      FacesContext fc = FacesContext.getCurrentInstance();
      ControlListaNavegacion controlListaNavegacion = (ControlListaNavegacion) fc.getApplication().evaluateExpressionGet(fc, "#{controlListaNavegacion}", ControlListaNavegacion.class);
      String pagActual = "tipocurso";
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

   public String redirigir() {
      return paginaAnterior;
   }

   public void cambiarIndice(TiposCursos tipo, int celda) {
      if (permitirIndex == true) {
         tipoCursoSeleccionado = tipo;
         cualCelda = celda;
         if (cualCelda == 0) {
            tipoCursoSeleccionado.getCodigo();
         } else if (cualCelda == 1) {
            tipoCursoSeleccionado.getDescripcion();
         }
         tipoCursoSeleccionado.getSecuencia();
      }
   }

   public void activarAceptar() {
      aceptar = false;
   }

   public void cancelarModificacion() {
      if (bandera == 1) {
         //CERRAR FILTRADO
         FacesContext c = FacesContext.getCurrentInstance();
         codigo = (Column) c.getViewRoot().findComponent("form:datosTiposCursos:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposCursos:descripcion");
         descripcion.setFilterStyle("display: none; visibility: hidden;");
         bandera = 0;
         filtrarTiposCursos = null;
         tamano = 270;
         RequestContext.getCurrentInstance().update("form:datosTiposCursos");
         tipoLista = 0;
      }

      borrarTiposCursos.clear();
      crearTiposCursos.clear();
      modificarTiposCursos.clear();
      tipoCursoSeleccionado = null;
      contarRegistros();
      k = 0;
      listTiposCursos = null;
      guardado = true;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:datosTiposCursos");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void salir() {
      limpiarListasValor();
      if (bandera == 1) {
         FacesContext c = FacesContext.getCurrentInstance();
         codigo = (Column) c.getViewRoot().findComponent("form:datosTiposCursos:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposCursos:descripcion");
         descripcion.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosTiposCursos");
         bandera = 0;
         filtrarTiposCursos = null;
         tipoLista = 0;
         tamano = 270;
      }
      borrarTiposCursos.clear();
      crearTiposCursos.clear();
      modificarTiposCursos.clear();
      tipoCursoSeleccionado = null;
      k = 0;
      listTiposCursos = null;
      guardado = true;
      navegar("atras");
   }

   public void activarCtrlF11() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 0) {
         tamano = 250;
         codigo = (Column) c.getViewRoot().findComponent("form:datosTiposCursos:codigo");
         codigo.setFilterStyle("width: 85% !important;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposCursos:descripcion");
         descripcion.setFilterStyle("width: 85% !important;");
         RequestContext.getCurrentInstance().update("form:datosTiposCursos");
         bandera = 1;
      } else if (bandera == 1) {
         log.info("Desactivar");
         tamano = 270;
         codigo = (Column) c.getViewRoot().findComponent("form:datosTiposCursos:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposCursos:descripcion");
         descripcion.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosTiposCursos");
         bandera = 0;
         filtrarTiposCursos = null;
         tipoLista = 0;
      }
   }

   public void modificarTiposCursos(TiposCursos tipo) {
      tipoCursoSeleccionado = tipo;
      if (!crearTiposCursos.contains(tipoCursoSeleccionado)) {
         if (modificarTiposCursos.isEmpty()) {
            modificarTiposCursos.add(tipoCursoSeleccionado);
         } else if (!modificarTiposCursos.contains(tipoCursoSeleccionado)) {
            modificarTiposCursos.add(tipoCursoSeleccionado);
         }
         guardado = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      }
      RequestContext.getCurrentInstance().update("form:datosTiposCursos");
   }

   public void borrandoTiposCursos() {
      if (tipoCursoSeleccionado != null) {
         if (!modificarTiposCursos.isEmpty() && modificarTiposCursos.contains(tipoCursoSeleccionado)) {
            modificarTiposCursos.remove(modificarTiposCursos.indexOf(tipoCursoSeleccionado));
            borrarTiposCursos.add(tipoCursoSeleccionado);
         } else if (!crearTiposCursos.isEmpty() && crearTiposCursos.contains(tipoCursoSeleccionado)) {
            crearTiposCursos.remove(crearTiposCursos.indexOf(tipoCursoSeleccionado));
         } else {
            borrarTiposCursos.add(tipoCursoSeleccionado);
         }
         listTiposCursos.remove(tipoCursoSeleccionado);
         if (tipoLista == 1) {
            filtrarTiposCursos.remove(tipoCursoSeleccionado);
         }
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:datosTiposCursos");
         contarRegistros();
         tipoCursoSeleccionado = null;
         guardado = true;

         if (guardado == true) {
            guardado = false;
         }
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void revisarDialogoGuardar() {
      if (!borrarTiposCursos.isEmpty() || !crearTiposCursos.isEmpty() || !modificarTiposCursos.isEmpty()) {
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:confirmarGuardar");
         RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
      }
   }

   public void guardarTiposCursos() {
      RequestContext context = RequestContext.getCurrentInstance();
      try {
         if (guardado == false) {
            if (!borrarTiposCursos.isEmpty()) {
               administrarTiposCursos.borrarTiposCursos(borrarTiposCursos);
               registrosBorrados = borrarTiposCursos.size();
               RequestContext.getCurrentInstance().update("form:mostrarBorrados");
               RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
               borrarTiposCursos.clear();
            }
            if (!modificarTiposCursos.isEmpty()) {
               administrarTiposCursos.modificarTiposCursos(modificarTiposCursos);
               modificarTiposCursos.clear();
            }
            if (!crearTiposCursos.isEmpty()) {
               administrarTiposCursos.crearTiposCursos(crearTiposCursos);
               crearTiposCursos.clear();
            }
            listTiposCursos = null;
            getListTiposCursos();
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            k = 0;
            FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
            contarRegistros();
            tipoCursoSeleccionado = null;
         }
         guardado = true;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         RequestContext.getCurrentInstance().update("form:datosTiposCursos");
      } catch (Exception e) {
         log.warn("Error guardarCambios : " + e.toString());
         FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado, intente nuevamente.");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      }

   }

   public void editarCelda() {
      if (tipoCursoSeleccionado != null) {
         editarTiposCursos = tipoCursoSeleccionado;

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
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void agregarNuevoTiposCursos() {
      log.info("agregarNuevoTiposCursos");
      int contador = 0;
      int duplicados = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      mensajeValidacion = " ";

      if (nuevoTiposCursos.getDescripcion().equals(" ") || nuevoTiposCursos.getDescripcion().equals("")) {
         mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
         contador++;
      }
      if (nuevoTiposCursos.getCodigo() == 0) {
         mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
         contador++;
      }

      for (int i = 0; i < listTiposCursos.size(); i++) {
         if (listTiposCursos.get(i).getCodigo() == nuevoTiposCursos.getCodigo()) {
            RequestContext.getCurrentInstance().update("formularioDialogos:existeCodigo");
            RequestContext.getCurrentInstance().execute("PF('existeCodigo').show()");
            duplicados++;
         }
      }

      if (contador != 0) {
         RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevoSector");
         RequestContext.getCurrentInstance().execute("PF('validacionNuevoSector').show()");
      }

      if (contador == 0 && duplicados == 0) {
         if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            //CERRAR FILTRADO
            log.info("Desactivar");
            codigo = (Column) c.getViewRoot().findComponent("form:datosTiposCursos:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposCursos:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            bandera = 0;
            filtrarTiposCursos = null;
            tipoLista = 0;
            tamano = 270;
            RequestContext.getCurrentInstance().update("form:datosTiposCursos");
         }
         k++;
         l = BigInteger.valueOf(k);
         nuevoTiposCursos.setSecuencia(l);
         crearTiposCursos.add(nuevoTiposCursos);
         listTiposCursos.add(nuevoTiposCursos);
         contarRegistros();
         tipoCursoSeleccionado = nuevoTiposCursos;
         nuevoTiposCursos = new TiposCursos();
         RequestContext.getCurrentInstance().update("form:datosTiposCursos");
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         RequestContext.getCurrentInstance().execute("PF('nuevoRegistroTiposCursos').hide()");
      }
   }

   public void limpiarNuevoTiposCursos() {
      nuevoTiposCursos = new TiposCursos();
   }

   public void duplicandoTiposCursos() {
      if (tipoCursoSeleccionado != null) {
         duplicarTiposCursos = new TiposCursos();
         k++;
         l = BigInteger.valueOf(k);

         if (tipoLista == 0) {
            duplicarTiposCursos.setSecuencia(l);
            duplicarTiposCursos.setCodigo(tipoCursoSeleccionado.getCodigo());
            duplicarTiposCursos.setDescripcion(tipoCursoSeleccionado.getDescripcion());
         }
         if (tipoLista == 1) {
            duplicarTiposCursos.setSecuencia(l);
            duplicarTiposCursos.setCodigo(tipoCursoSeleccionado.getCodigo());
            duplicarTiposCursos.setDescripcion(tipoCursoSeleccionado.getDescripcion());
            tamano = 270;
         }

         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTE");
         RequestContext.getCurrentInstance().execute("PF('duplicarRegistroTiposCursos').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void confirmarDuplicar() {
      RequestContext context = RequestContext.getCurrentInstance();
      int contador = 0;

      for (int i = 0; i < listTiposCursos.size(); i++) {
         if (duplicarTiposCursos.getCodigo() == listTiposCursos.get(i).getCodigo()) {
            RequestContext.getCurrentInstance().update("formularioDialogos:existeCodigo");
            RequestContext.getCurrentInstance().execute("PF('existeCodigo').show()");
            contador++;
         }
      }

      if (contador == 0) {
         listTiposCursos.add(duplicarTiposCursos);
         crearTiposCursos.add(duplicarTiposCursos);
         tipoCursoSeleccionado = duplicarTiposCursos;
         contarRegistros();
         RequestContext.getCurrentInstance().update("form:datosTiposCursos");
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }

         if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            //CERRAR FILTRADO
            codigo = (Column) c.getViewRoot().findComponent("form:datosTiposCursos:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposCursos:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            bandera = 0;
            filtrarTiposCursos = null;
            RequestContext.getCurrentInstance().update("form:datosTiposCursos");
            tipoLista = 0;
         }
         duplicarTiposCursos = new TiposCursos();
      }
      RequestContext.getCurrentInstance().update("formularioDialogos:duplicarRegistroTiposCursos");
      RequestContext.getCurrentInstance().execute("PF('duplicarRegistroTiposCursos').hide()");
   }

   public void limpiarDuplicarTiposCursos() {
      duplicarTiposCursos = new TiposCursos();
   }

   public void exportPDF() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTiposCursosExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "TIPOSCURSOS", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void exportXLS() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTiposCursosExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "TIPOSCURSOS", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void verificarRastro() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoCursoSeleccionado != null) {
         int resultado = administrarRastros.obtenerTabla(tipoCursoSeleccionado.getSecuencia(), "TIPOSCURSOS"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
      } else if (administrarRastros.verificarHistoricosTabla("TIPOSCURSOS")) { // igual acá
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
   }

   public void recordarSeleccion() {
      if (tipoCursoSeleccionado != null) {
         FacesContext c = FacesContext.getCurrentInstance();
         tablaC = (DataTable) c.getViewRoot().findComponent("form:datosTiposCursos");
         tablaC.setSelection(tipoCursoSeleccionado);
      }
   }

   public void eventoFiltrar() {
      try {
         if (tipoLista == 0) {
            tipoLista = 1;
         }
         contarRegistros();
      } catch (Exception e) {
         log.warn("Error ControlTiposCursos eventoFiltrar ERROR===" + e.getMessage());
      }
   }

   public void contarRegistros() {
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
   }

   public void deshabilitarBotonLov() {
      activarLov = true;
   }

   //*/*/*/*/*/*/*/*/*/*-/-*//-*/-*/*/*-*/-*/-*/*/*/*/*/---/*/*/*/*/-*/-*/-*/-*/-*/
   public List<TiposCursos> getListTiposCursos() {
      if (listTiposCursos == null) {
         listTiposCursos = administrarTiposCursos.consultarTiposCursos();
      }
      return listTiposCursos;
   }

   public void setListTiposCursos(List<TiposCursos> listTiposCursos) {
      this.listTiposCursos = listTiposCursos;
   }

   public List<TiposCursos> getFiltrarTiposCursos() {
      return filtrarTiposCursos;
   }

   public void setFiltrarTiposCursos(List<TiposCursos> filtrarTiposCursos) {
      this.filtrarTiposCursos = filtrarTiposCursos;
   }

   public TiposCursos getNuevoTiposCursos() {
      return nuevoTiposCursos;
   }

   public void setNuevoTiposCursos(TiposCursos nuevoTiposCursos) {
      this.nuevoTiposCursos = nuevoTiposCursos;
   }

   public TiposCursos getDuplicarTiposCursos() {
      return duplicarTiposCursos;
   }

   public void setDuplicarTiposCursos(TiposCursos duplicarTiposCursos) {
      this.duplicarTiposCursos = duplicarTiposCursos;
   }

   public TiposCursos getEditarTiposCursos() {
      return editarTiposCursos;
   }

   public void setEditarTiposCursos(TiposCursos editarTiposCursos) {
      this.editarTiposCursos = editarTiposCursos;
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

   public TiposCursos getTiposCursosSeleccionado() {
      return tipoCursoSeleccionado;
   }

   public void setTiposCursosSeleccionado(TiposCursos clasesPensionesSeleccionado) {
      this.tipoCursoSeleccionado = clasesPensionesSeleccionado;
   }

   public String getInfoRegistro() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosTiposCursos");
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
