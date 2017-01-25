/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.IndicesExternos;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarIndicesExternosInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
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
@Named(value = "controlIndicesExternos")
@SessionScoped
public class ControlIndicesExternos implements Serializable {

   @EJB
   AdministrarIndicesExternosInterface administrarIndicesExternos;
   @EJB
   AdministrarRastrosInterface administrarRastros;

   private List<IndicesExternos> listIndices;
   private List<IndicesExternos> listIndicesCrear;
   private List<IndicesExternos> listIndicesBorrar;
   private List<IndicesExternos> listIndicesModificar;
   private List<IndicesExternos> listIndicesFiltrar;
   private IndicesExternos nuevoIndiceExterno;
   private IndicesExternos duplicarIndiceExterno;
   private IndicesExternos editarIndiceExterno;
   private IndicesExternos indiceExternoSeleccionado;
   private int cualCelda, tipoLista, tipoActualizacion, k, bandera;
   private BigInteger l;
   private boolean aceptar, guardado;
   private boolean permitirIndex;
   private Column codigo, descripcion;
   private int registrosBorrados;
   private String mensajeValidacion, paginaanterior;
   private int tamano;
   private boolean activarLov;
   private String infoRegistro;
   private DataTable tablaC;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

   public ControlIndicesExternos() {
      listIndices = null;
      listIndicesCrear = new ArrayList<IndicesExternos>();
      listIndicesModificar = new ArrayList<IndicesExternos>();
      listIndicesBorrar = new ArrayList<IndicesExternos>();
      permitirIndex = true;
      editarIndiceExterno = new IndicesExternos();
      nuevoIndiceExterno = new IndicesExternos();
      duplicarIndiceExterno = new IndicesExternos();
      guardado = true;
      tamano = 270;
      cualCelda = -1;
      indiceExternoSeleccionado = null;
      activarLov = true;
      mapParametros.put("paginaAnterior", paginaAnterior);
   }

   public void recibirPaginaEntrante(String pagina) {
      paginaAnterior = pagina;
      listIndices = null;
      getListIndices();
      deshabilitarBotonLov();
      if (listIndices != null) {
         indiceExternoSeleccionado = listIndices.get(0);
      }
   }

   public void recibirParametros(Map<String, Object> map) {
      mapParametros = map;
      paginaAnterior = (String) mapParametros.get("paginaAnterior");
      listIndices = null;
      getListIndices();
      deshabilitarBotonLov();
      if (listIndices != null) {
         indiceExternoSeleccionado = listIndices.get(0);
      }
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
         String pagActual = "indicesexternos";
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

   @PostConstruct
   public void inicializarAdministrador() {
      try {
         FacesContext x = FacesContext.getCurrentInstance();
         HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
         administrarIndicesExternos.obtenerConexion(ses.getId());
         administrarRastros.obtenerConexion(ses.getId());
      } catch (Exception e) {
         System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
         System.out.println("Causa: " + e.getCause());
      }
   }

   public String redirigir() {
      return paginaanterior;
   }

   public void cambiarIndice(IndicesExternos indiceex, int celda) {
      if (permitirIndex == true) {
         indiceExternoSeleccionado = indiceex;
         cualCelda = celda;
         if (tipoLista == 0) {
            if (cualCelda == 0) {
               indiceExternoSeleccionado.getCodigo();
            } else if (cualCelda == 1) {
               indiceExternoSeleccionado.getDescripcion();
            }
         } else if (cualCelda == 0) {
            indiceExternoSeleccionado.getCodigo();

         } else if (cualCelda == 1) {
            indiceExternoSeleccionado.getDescripcion();
         }
         indiceExternoSeleccionado.getSecuencia();
      }
   }

   public void activarAceptar() {
      aceptar = false;
   }

   public void cancelarModificacion() {
      if (bandera == 1) {
         //CERRAR FILTRADO
         FacesContext c = FacesContext.getCurrentInstance();
         codigo = (Column) c.getViewRoot().findComponent("form:datosIndicesExternos:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosIndicesExternos:descripcion");
         descripcion.setFilterStyle("display: none; visibility: hidden;");
         bandera = 0;
         listIndicesFiltrar = null;
         tamano = 270;
         RequestContext.getCurrentInstance().update("form:datosIndicesExternos");
         tipoLista = 0;
      }

      listIndicesBorrar.clear();
      listIndicesCrear.clear();
      listIndicesModificar.clear();
      indiceExternoSeleccionado = null;
      contarRegistros();
      k = 0;
      listIndices = null;
      guardado = true;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:datosIndicesExternos");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void salir() {
      if (bandera == 1) {
         FacesContext c = FacesContext.getCurrentInstance();
         codigo = (Column) c.getViewRoot().findComponent("form:datosIndicesExternos:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosIndicesExternos:descripcion");
         descripcion.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosIndicesExternos");
         bandera = 0;
         listIndicesFiltrar = null;
         tipoLista = 0;
         tamano = 270;
      }

      listIndicesBorrar.clear();
      listIndicesCrear.clear();
      listIndicesModificar.clear();
      indiceExternoSeleccionado = null;
      k = 0;
      listIndices = null;
      guardado = true;
   }

   public void activarCtrlF11() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 0) {
         tamano = 250;
         codigo = (Column) c.getViewRoot().findComponent("form:datosIndicesExternos:codigo");
         codigo.setFilterStyle("width: 85% !important;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosIndicesExternos:descripcion");
         descripcion.setFilterStyle("width: 85% !important;");
         RequestContext.getCurrentInstance().update("form:datosIndicesExternos");
         bandera = 1;
      } else if (bandera == 1) {
         System.out.println("Desactivar");
         tamano = 270;
         codigo = (Column) c.getViewRoot().findComponent("form:datosIndicesExternos:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosIndicesExternos:descripcion");
         descripcion.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosIndicesExternos");
         bandera = 0;
         listIndicesFiltrar = null;
         tipoLista = 0;
      }
   }

   public void modificarIndicesExternos(IndicesExternos indiceex, String confirmarCambio, String valorConfirmar) {
      indiceExternoSeleccionado = indiceex;
      int contador = 0;
      boolean banderita = false;
      Integer a;
      a = null;
      RequestContext context = RequestContext.getCurrentInstance();
      if (confirmarCambio.equalsIgnoreCase("N")) {
         if (tipoLista == 0) {
            if (!listIndicesCrear.contains(indiceExternoSeleccionado)) {
               if (listIndicesModificar.isEmpty()) {
                  listIndicesModificar.add(indiceExternoSeleccionado);
               } else if (!listIndicesModificar.contains(indiceExternoSeleccionado)) {
                  listIndicesModificar.add(indiceExternoSeleccionado);
               }
               if (guardado == true) {
                  guardado = false;
                  RequestContext.getCurrentInstance().update("form:ACEPTAR");
               }
            }
         } else if (!listIndicesCrear.contains(indiceExternoSeleccionado)) {
            if (!listIndicesCrear.contains(indiceExternoSeleccionado)) {
               if (listIndicesModificar.isEmpty()) {
                  listIndicesModificar.add(indiceExternoSeleccionado);
               } else if (!listIndicesModificar.contains(indiceExternoSeleccionado)) {
                  listIndicesModificar.add(indiceExternoSeleccionado);
               }
               if (guardado == true) {
                  guardado = false;
                  RequestContext.getCurrentInstance().update("form:ACEPTAR");
               }
            }
            RequestContext.getCurrentInstance().update("form:datosIndicesExternos");
         }
      }
   }

   public void borrandoIndicesExternos() {
      if (indiceExternoSeleccionado != null) {
         if (!listIndicesModificar.isEmpty() && listIndicesModificar.contains(indiceExternoSeleccionado)) {
            listIndicesModificar.remove(listIndicesModificar.indexOf(indiceExternoSeleccionado));
            listIndicesBorrar.add(indiceExternoSeleccionado);
         } else if (!listIndicesCrear.isEmpty() && listIndicesCrear.contains(indiceExternoSeleccionado)) {
            listIndicesCrear.remove(listIndicesCrear.indexOf(indiceExternoSeleccionado));
         } else {
            listIndicesBorrar.add(indiceExternoSeleccionado);
         }
         listIndices.remove(indiceExternoSeleccionado);
         if (tipoLista == 1) {
            listIndicesFiltrar.remove(indiceExternoSeleccionado);
         }
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:datosIndicesExternos");
         contarRegistros();
         indiceExternoSeleccionado = null;
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

      if (!listIndicesBorrar.isEmpty() || !listIndicesCrear.isEmpty() || !listIndicesModificar.isEmpty()) {
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:confirmarGuardar");
         RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
      }

   }

   public void guardarIndicesExternos() {
      RequestContext context = RequestContext.getCurrentInstance();
      try {
         if (guardado == false) {
            if (!listIndicesBorrar.isEmpty()) {
               administrarIndicesExternos.borrarIndice(listIndicesBorrar);
               registrosBorrados = listIndicesBorrar.size();
               RequestContext.getCurrentInstance().update("form:mostrarBorrados");
               RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
               listIndicesBorrar.clear();
            }
            if (!listIndicesModificar.isEmpty()) {
               administrarIndicesExternos.modificarIndice(listIndicesModificar);
               listIndicesModificar.clear();
            }
            if (!listIndicesCrear.isEmpty()) {
               administrarIndicesExternos.crearIndice(listIndicesCrear);
               listIndicesCrear.clear();
            }
            listIndices = null;
            getListIndices();
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            k = 0;
            FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
            contarRegistros();
            indiceExternoSeleccionado = null;
         }
         guardado = true;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         RequestContext.getCurrentInstance().update("form:datosIndicesExternos");
      } catch (Exception e) {
         System.out.println("Error guardarCambios : " + e.toString());
         FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado, intente nuevamente.");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      }

   }

   public void editarCelda() {
      if (indiceExternoSeleccionado != null) {
         editarIndiceExterno = indiceExternoSeleccionado;

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

   public void agregarNuevoIndiceExterno() {
      System.out.println("agregarNuevoTiposCursos");
      int contador = 0;
      int duplicados = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      mensajeValidacion = " ";

      if (nuevoIndiceExterno.getCodigo() == 0) {
         mensajeValidacion = mensajeValidacion + " * Codigo \n";
         contador++;
      }

      for (int i = 0; i < listIndices.size(); i++) {
         if (listIndices.get(i).getCodigo() == nuevoIndiceExterno.getCodigo()) {
            RequestContext.getCurrentInstance().update("formularioDialogos:existeCodigo");
            RequestContext.getCurrentInstance().execute("PF('existeCodigo').show()");
            duplicados++;
         }
         if (contador != 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevoIndice");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevoIndice').show()");

         }
      }

      if (contador == 0 && duplicados == 0) {
         if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            //CERRAR FILTRADO
            System.out.println("Desactivar");
            codigo = (Column) c.getViewRoot().findComponent("form:datosIndicesExternos:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosIndicesExternos:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            bandera = 0;
            listIndicesFiltrar = null;
            tipoLista = 0;
            tamano = 270;
            RequestContext.getCurrentInstance().update("form:datosIndicesExternos");
         }
         k++;
         l = BigInteger.valueOf(k);
         nuevoIndiceExterno.setSecuencia(l);
         listIndicesCrear.add(nuevoIndiceExterno);
         listIndices.add(nuevoIndiceExterno);
         contarRegistros();
         indiceExternoSeleccionado = nuevoIndiceExterno;
         nuevoIndiceExterno = new IndicesExternos();
         RequestContext.getCurrentInstance().update("form:datosIndicesExternos");
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         RequestContext.getCurrentInstance().execute("PF('nuevoRegistroIndicesExternos').hide()");
      } else {
         RequestContext.getCurrentInstance().update("form:validacionNuevoIndice");
         RequestContext.getCurrentInstance().execute("PF('validacionNuevoIndice').show()");
      }
   }

   public void limpiarNuevoIndiceExterno() {
      nuevoIndiceExterno = new IndicesExternos();
   }

   public void duplicandoIndicesExternos() {
      if (indiceExternoSeleccionado != null) {
         duplicarIndiceExterno = new IndicesExternos();
         k++;
         l = BigInteger.valueOf(k);

         if (tipoLista == 0) {
            duplicarIndiceExterno.setSecuencia(l);
            duplicarIndiceExterno.setCodigo(indiceExternoSeleccionado.getCodigo());
            duplicarIndiceExterno.setDescripcion(indiceExternoSeleccionado.getDescripcion());
         }
         if (tipoLista == 1) {
            duplicarIndiceExterno.setSecuencia(l);
            duplicarIndiceExterno.setCodigo(indiceExternoSeleccionado.getCodigo());
            duplicarIndiceExterno.setDescripcion(indiceExternoSeleccionado.getDescripcion());
            tamano = 270;
         }

         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTE");
         RequestContext.getCurrentInstance().execute("PF('duplicarRegistrosIndices').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void confirmarDuplicar() {
      RequestContext context = RequestContext.getCurrentInstance();
      int contador = 0;

      for (int i = 0; i < listIndices.size(); i++) {
         if (duplicarIndiceExterno.getCodigo() == listIndices.get(i).getCodigo()) {
            RequestContext.getCurrentInstance().update("formularioDialogos:existeCodigo");
            RequestContext.getCurrentInstance().execute("PF('existeCodigo').show()");
            contador++;
         }
      }

      if (contador == 0) {
         listIndices.add(duplicarIndiceExterno);
         listIndicesCrear.add(duplicarIndiceExterno);
         indiceExternoSeleccionado = duplicarIndiceExterno;
         contarRegistros();
         RequestContext.getCurrentInstance().update("form:datosIndicesExternos");
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }

         if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            //CERRAR FILTRADO
            codigo = (Column) c.getViewRoot().findComponent("form:datosIndicesExternos:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosIndicesExternos:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            bandera = 0;
            listIndicesFiltrar = null;
            RequestContext.getCurrentInstance().update("form:datosIndicesExternos");
            tipoLista = 0;
         }
         duplicarIndiceExterno = new IndicesExternos();
      }
      RequestContext.getCurrentInstance().update("formularioDialogos:duplicarRegistroTiposCursos");
      RequestContext.getCurrentInstance().execute("PF('duplicarRegistroTiposCursos').hide()");
   }

   public void limpiarDuplicarIndicesExternos() {
      duplicarIndiceExterno = new IndicesExternos();
   }

   public void exportPDF() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosIndicesExternosExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "INDICESEXTERNOS", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void exportXLS() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosIndicesExternosExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "INDICESEXTERNOS", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void verificarRastro() {
      RequestContext context = RequestContext.getCurrentInstance();
      System.out.println("lol");
      if (indiceExternoSeleccionado != null) {
         System.out.println("lol 2");
         int resultado = administrarRastros.obtenerTabla(indiceExternoSeleccionado.getSecuencia(), "INDICESEXTERNOS"); //En ENCARGATURAS lo cambia por el nombre de su tabla
         System.out.println("resultado: " + resultado);
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
      } else if (administrarRastros.verificarHistoricosTabla("INDICESEXTERNOS")) { // igual acá
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
   }

   public void recordarSeleccion() {
      if (indiceExternoSeleccionado != null) {
         FacesContext c = FacesContext.getCurrentInstance();
         tablaC = (DataTable) c.getViewRoot().findComponent("form:datosIndicesExternos");
         tablaC.setSelection(indiceExternoSeleccionado);
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

   public void deshabilitarBotonLov() {
      activarLov = true;
   }

   /////////////GETS Y SETS////////////////////////
   public List<IndicesExternos> getListIndices() {
      if (listIndices == null) {
         listIndices = administrarIndicesExternos.consultarIndicesExternos();
      }
      return listIndices;
   }

   public void setListIndices(List<IndicesExternos> listIndices) {
      this.listIndices = listIndices;
   }

   public List<IndicesExternos> getListIndicesFiltrar() {
      return listIndicesFiltrar;
   }

   public void setListIndicesFiltrar(List<IndicesExternos> listIndicesFiltrar) {
      this.listIndicesFiltrar = listIndicesFiltrar;
   }

   public IndicesExternos getNuevoIndiceExterno() {
      return nuevoIndiceExterno;
   }

   public void setNuevoIndiceExterno(IndicesExternos nuevoIndiceExterno) {
      this.nuevoIndiceExterno = nuevoIndiceExterno;
   }

   public IndicesExternos getDuplicarIndiceExterno() {
      return duplicarIndiceExterno;
   }

   public void setDuplicarIndiceExterno(IndicesExternos duplicarIndiceExterno) {
      this.duplicarIndiceExterno = duplicarIndiceExterno;
   }

   public IndicesExternos getEditarIndiceExterno() {
      return editarIndiceExterno;
   }

   public void setEditarIndiceExterno(IndicesExternos editarIndiceExterno) {
      this.editarIndiceExterno = editarIndiceExterno;
   }

   public IndicesExternos getIndiceExternoSeleccionado() {
      return indiceExternoSeleccionado;
   }

   public void setIndiceExternoSeleccionado(IndicesExternos indiceExternoSeleccionado) {
      this.indiceExternoSeleccionado = indiceExternoSeleccionado;
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

   public int getTamano() {
      return tamano;
   }

   public void setTamano(int tamano) {
      this.tamano = tamano;
   }

   public boolean isActivarLov() {
      return activarLov;
   }

   public void setActivarLov(boolean activarLov) {
      this.activarLov = activarLov;
   }

   public String getInfoRegistro() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosIndicesExternos");
      infoRegistro = String.valueOf(tabla.getRowCount());
      return infoRegistro;
   }

   public void setInfoRegistro(String infoRegistro) {
      this.infoRegistro = infoRegistro;
   }

}
