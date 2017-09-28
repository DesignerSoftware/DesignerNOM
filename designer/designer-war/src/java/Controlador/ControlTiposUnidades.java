/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.TiposUnidades;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarTiposUnidadesInterface;
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
public class ControlTiposUnidades implements Serializable {

   private static Logger log = Logger.getLogger(ControlTiposUnidades.class);

   @EJB
   AdministrarTiposUnidadesInterface administrarTiposUnidades;
   @EJB
   AdministrarRastrosInterface administrarRastros;

   private List<TiposUnidades> listTiposUnidades;
   private List<TiposUnidades> filtrarTiposUnidades;
   private List<TiposUnidades> crearTiposUnidades;
   private List<TiposUnidades> modificarTiposUnidades;
   private List<TiposUnidades> borrarTiposUnidades;
   private TiposUnidades nuevoTiposUnidades;
   private TiposUnidades duplicarTiposUnidades;
   private TiposUnidades editarTiposUnidades;
   private TiposUnidades tiposUnidadesSeleccionado;
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
   private boolean activarLov;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();
   private String infoRegistro;

   public ControlTiposUnidades() {
      listTiposUnidades = null;
      crearTiposUnidades = new ArrayList<TiposUnidades>();
      modificarTiposUnidades = new ArrayList<TiposUnidades>();
      borrarTiposUnidades = new ArrayList<TiposUnidades>();
      permitirIndex = true;
      editarTiposUnidades = new TiposUnidades();
      nuevoTiposUnidades = new TiposUnidades();
      duplicarTiposUnidades = new TiposUnidades();
      guardado = true;
      activarLov = true;
      tamano = 270;
      mapParametros.put("paginaAnterior", paginaAnterior);
      log.info("controlTiposUnidades Constructor");
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
         log.info("ControlTiposUnidades PostConstruct ");
         FacesContext x = FacesContext.getCurrentInstance();
         HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
         administrarTiposUnidades.obtenerConexion(ses.getId());
         administrarRastros.obtenerConexion(ses.getId());
      } catch (Exception e) {
         log.error("Error postconstruct " + this.getClass().getName() + ":  ", e);
         log.error("Causa: " + e.getCause());
      }
   }

   public void recibirPaginaEntrante(String pagina) {
      paginaAnterior = pagina;
      listTiposUnidades = null;
      getListTiposUnidades();
      if (listTiposUnidades != null) {
         if (!listTiposUnidades.isEmpty()) {
            tiposUnidadesSeleccionado = listTiposUnidades.get(0);
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
      String pagActual = "tipounidad";
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

   public void eventoFiltrar() {
      if (tipoLista == 0) {
         tipoLista = 1;
      }
      contarRegistros();
   }

   public void cambiarIndice(TiposUnidades unidades, int celda) {
      if (permitirIndex == true) {
         tiposUnidadesSeleccionado = unidades;
         cualCelda = celda;
         if (cualCelda == 0) {
            tiposUnidadesSeleccionado.getCodigo();
         } else if (cualCelda == 1) {
            tiposUnidadesSeleccionado.getNombre();
         }
         tiposUnidadesSeleccionado.getSecuencia();
      }
   }

   public void activarAceptar() {
      aceptar = false;
   }

   public void listaValoresBoton() {
   }

   public void cancelarModificacion() {
      if (bandera == 1) {
         //CERRAR FILTRADO
         FacesContext c = FacesContext.getCurrentInstance();
         codigo = (Column) c.getViewRoot().findComponent("form:datosTiposUnidades:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposUnidades:descripcion");
         descripcion.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosTiposUnidades");
         bandera = 0;
         filtrarTiposUnidades = null;
         tipoLista = 0;
         tamano = 270;
      }

      borrarTiposUnidades.clear();
      crearTiposUnidades.clear();
      modificarTiposUnidades.clear();
      tiposUnidadesSeleccionado = null;
      k = 0;
      listTiposUnidades = null;
      guardado = true;
      permitirIndex = true;
      getListTiposUnidades();
      RequestContext context = RequestContext.getCurrentInstance();
      contarRegistros();
      RequestContext.getCurrentInstance().update("form:datosTiposUnidades");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void salir() {
      limpiarListasValor();
      if (bandera == 1) {
         //CERRAR FILTRADO
         FacesContext c = FacesContext.getCurrentInstance();
         codigo = (Column) c.getViewRoot().findComponent("form:datosTiposUnidades:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposUnidades:descripcion");
         descripcion.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosTiposUnidades");
         bandera = 0;
         filtrarTiposUnidades = null;
         tipoLista = 0;
         tamano = 270;
      }

      borrarTiposUnidades.clear();
      crearTiposUnidades.clear();
      modificarTiposUnidades.clear();
      tiposUnidadesSeleccionado = null;
      k = 0;
      listTiposUnidades = null;
      guardado = true;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:datosTiposUnidades");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      navegar("atras");
   }

   public void activarCtrlF11() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 0) {
         tamano = 250;
         codigo = (Column) c.getViewRoot().findComponent("form:datosTiposUnidades:codigo");
         codigo.setFilterStyle("width: 85% !important;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposUnidades:descripcion");
         descripcion.setFilterStyle("width: 85% !important;");
         RequestContext.getCurrentInstance().update("form:datosTiposUnidades");
         log.info("Activar");
         bandera = 1;
      } else if (bandera == 1) {
         log.info("Desactivar");
         tamano = 270;
         codigo = (Column) c.getViewRoot().findComponent("form:datosTiposUnidades:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposUnidades:descripcion");
         descripcion.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosTiposUnidades");
         bandera = 0;
         filtrarTiposUnidades = null;
         tipoLista = 0;
      }
   }

   public void modificarTiposUnidades(TiposUnidades tipounidad) {
      tiposUnidadesSeleccionado = tipounidad;
      if (!crearTiposUnidades.contains(tiposUnidadesSeleccionado)) {
         if (modificarTiposUnidades.isEmpty()) {
            modificarTiposUnidades.add(tiposUnidadesSeleccionado);
         } else if (!modificarTiposUnidades.contains(tiposUnidadesSeleccionado)) {
            modificarTiposUnidades.add(tiposUnidadesSeleccionado);
         }
         guardado = false;
      }
      RequestContext.getCurrentInstance().update("form:datosTiposUnidades");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void borrandoTiposUnidades() {

      if (tiposUnidadesSeleccionado != null) {
         log.info("Entro a borrandoTiposUnidades");
         if (!modificarTiposUnidades.isEmpty() && modificarTiposUnidades.contains(tiposUnidadesSeleccionado)) {
            int modIndex = modificarTiposUnidades.indexOf(tiposUnidadesSeleccionado);
            modificarTiposUnidades.remove(modIndex);
            borrarTiposUnidades.add(tiposUnidadesSeleccionado);
         } else if (!crearTiposUnidades.isEmpty() && crearTiposUnidades.contains(tiposUnidadesSeleccionado)) {
            int crearIndex = crearTiposUnidades.indexOf(tiposUnidadesSeleccionado);
            crearTiposUnidades.remove(crearIndex);
         } else {
            borrarTiposUnidades.add(tiposUnidadesSeleccionado);
         }
         listTiposUnidades.remove(tiposUnidadesSeleccionado);
         if (tipoLista == 1) {
            filtrarTiposUnidades.remove(tiposUnidadesSeleccionado);
         }
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:datosTiposUnidades");
         contarRegistros();

         tiposUnidadesSeleccionado = null;

         if (guardado == true) {
            guardado = false;
         }
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }

   }

   public void verificarBorrado() {
      log.info("Estoy en verificarBorrado");
      BigInteger contarUnidadesTipoUnidad;

      try {
         log.error("Control Secuencia de ControlTiposUnidades ");
         if (tipoLista == 0) {
            contarUnidadesTipoUnidad = administrarTiposUnidades.contarUnidadesTipoUnidad(tiposUnidadesSeleccionado.getSecuencia());
         } else {
            contarUnidadesTipoUnidad = administrarTiposUnidades.contarUnidadesTipoUnidad(tiposUnidadesSeleccionado.getSecuencia());
         }
         if (contarUnidadesTipoUnidad.equals(new BigInteger("0"))) {
            log.info("Borrado==0");
            borrandoTiposUnidades();
         } else {
            log.info("Borrado>0");

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:validacionBorrar");
            RequestContext.getCurrentInstance().execute("PF('validacionBorrar').show()");
            contarUnidadesTipoUnidad = new BigInteger("-1");

         }
      } catch (Exception e) {
         log.error("ERROR ControlTiposUnidades verificarBorrado ERROR  ", e);
      }
   }

   public void revisarDialogoGuardar() {

      if (!borrarTiposUnidades.isEmpty() || !crearTiposUnidades.isEmpty() || !modificarTiposUnidades.isEmpty()) {
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:confirmarGuardar");
         RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
      }

   }

   public void guardarTiposUnidades() {
      RequestContext context = RequestContext.getCurrentInstance();

      if (guardado == false) {
         log.info("Realizando guardarTiposUnidades");
         if (!borrarTiposUnidades.isEmpty()) {
            administrarTiposUnidades.borrarTiposUnidades(borrarTiposUnidades);
            //mostrarBorrados
            registrosBorrados = borrarTiposUnidades.size();
            RequestContext.getCurrentInstance().update("form:mostrarBorrados");
            RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
            borrarTiposUnidades.clear();
         }
         if (!modificarTiposUnidades.isEmpty()) {
            administrarTiposUnidades.modificarTiposUnidades(modificarTiposUnidades);
            modificarTiposUnidades.clear();
         }
         if (!crearTiposUnidades.isEmpty()) {
            administrarTiposUnidades.crearTiposUnidades(crearTiposUnidades);
            crearTiposUnidades.clear();
         }
         log.info("Se guardaron los datos con exito");
         listTiposUnidades = null;
         RequestContext.getCurrentInstance().update("form:datosTiposUnidades");
         k = 0;
         guardado = true;
         FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      }
      RequestContext.getCurrentInstance().update("form:ACEPTAR");

   }

   public void editarCelda() {
      if (tiposUnidadesSeleccionado != null) {
         editarTiposUnidades = tiposUnidadesSeleccionado;

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

   public void agregarNuevoTiposUnidades() {
      log.info("agregarNuevoTiposUnidades");
      int contador = 0;
      int duplicados = 0;

      Integer a = 0;
      a = null;
      mensajeValidacion = " ";
      if (nuevoTiposUnidades.getCodigo() == a) {
         mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
      } else {

         for (int x = 0; x < listTiposUnidades.size(); x++) {
            if (listTiposUnidades.get(x).getCodigo().equals(nuevoTiposUnidades.getCodigo())) {
               duplicados++;
            }
         }

         if (duplicados > 0) {
            mensajeValidacion = "El código ingresado está en uso. Por favor ingrese un código válido";
         } else {
            contador++;
         }
      }
      if (nuevoTiposUnidades.getNombre() == null) {
         mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
      } else if (nuevoTiposUnidades.getNombre().isEmpty()) {
         mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
      } else {
         log.info("bandera");
         contador++;
      }
      if (contador == 2) {
         if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            //CERRAR FILTRADO
            log.info("Desactivar");
            codigo = (Column) c.getViewRoot().findComponent("form:datosTiposUnidades:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposUnidades:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTiposUnidades");
            bandera = 0;
            filtrarTiposUnidades = null;
            tipoLista = 0;
         }
         k++;
         l = BigInteger.valueOf(k);
         nuevoTiposUnidades.setSecuencia(l);
         crearTiposUnidades.add(nuevoTiposUnidades);
         listTiposUnidades.add(nuevoTiposUnidades);
         tiposUnidadesSeleccionado = nuevoTiposUnidades;
         RequestContext.getCurrentInstance().update("form:datosTiposUnidades");
         contarRegistros();
         nuevoTiposUnidades = new TiposUnidades();
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         RequestContext.getCurrentInstance().execute("PF('nuevoRegistroTiposUnidades').hide()");
      } else {
         RequestContext.getCurrentInstance().update("form:validacionNuevoTipoUnidad");
         RequestContext.getCurrentInstance().execute("PF('validacionNuevoTipoUnidad').show()");
         contador = 0;
      }
   }

   public void limpiarNuevoTiposUnidades() {
      nuevoTiposUnidades = new TiposUnidades();
   }

   //------------------------------------------------------------------------------
   public void duplicandoTiposUnidades() {
      log.info("duplicandoTiposUnidades");
      if (tiposUnidadesSeleccionado != null) {
         duplicarTiposUnidades = new TiposUnidades();
         k++;
         l = BigInteger.valueOf(k);

         duplicarTiposUnidades.setSecuencia(l);
         duplicarTiposUnidades.setCodigo(tiposUnidadesSeleccionado.getCodigo());
         duplicarTiposUnidades.setNombre(tiposUnidadesSeleccionado.getNombre());

         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTE");
         RequestContext.getCurrentInstance().execute("PF('duplicarRegistroTiposUnidades').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void confirmarDuplicar() {
      log.error("ESTOY EN CONFIRMAR DUPLICAR TIPOS EMPRESAS");
      int contador = 0;
      mensajeValidacion = " ";
      int duplicados = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      Integer a = 0;
      a = null;

      if (duplicarTiposUnidades.getCodigo() == a) {
         mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
      } else {
         for (int x = 0; x < listTiposUnidades.size(); x++) {
            if (listTiposUnidades.get(x).getCodigo().equals(duplicarTiposUnidades.getCodigo())) {
               duplicados++;
            }
         }
         if (duplicados > 0) {
            mensajeValidacion = "El código ingresado está en uso. Por favor ingrese un código válido";
         } else {
            log.info("bandera");
            contador++;
            duplicados = 0;
         }
      }
      if (duplicarTiposUnidades.getNombre() == null) {
         mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
      } else if (duplicarTiposUnidades.getNombre().isEmpty()) {
         mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
      } else {
         log.info("bandera");
         contador++;

      }

      if (contador == 2) {

         log.info("Datos Duplicando: " + duplicarTiposUnidades.getSecuencia() + "  " + duplicarTiposUnidades.getCodigo());
         if (crearTiposUnidades.contains(duplicarTiposUnidades)) {
            log.info("Ya lo contengo.");
         }
         listTiposUnidades.add(duplicarTiposUnidades);
         crearTiposUnidades.add(duplicarTiposUnidades);
         RequestContext.getCurrentInstance().update("form:datosTiposUnidades");
         tiposUnidadesSeleccionado = duplicarTiposUnidades;
         if (guardado == true) {
            guardado = false;
         }
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         contarRegistros();

         if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            //CERRAR FILTRADO
            codigo = (Column) c.getViewRoot().findComponent("form:datosTiposUnidades:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposUnidades:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTiposUnidades");
            bandera = 0;
            filtrarTiposUnidades = null;
            tipoLista = 0;
         }
         duplicarTiposUnidades = new TiposUnidades();
         RequestContext.getCurrentInstance().execute("PF('duplicarRegistroTiposUnidades').hide()");

      } else {
         contador = 0;
         RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
         RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
      }
   }

   public void limpiarDuplicarTiposUnidades() {
      duplicarTiposUnidades = new TiposUnidades();
   }

   public void exportPDF() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTiposUnidadesExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "TIPOSUNIDADES", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void exportXLS() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTiposUnidadesExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "TIPOSUNIDADES", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void verificarRastro() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tiposUnidadesSeleccionado != null) {
         int resultado = administrarRastros.obtenerTabla(tiposUnidadesSeleccionado.getSecuencia(), "TIPOSUNIDADES"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
      } else if (administrarRastros.verificarHistoricosTabla("TIPOSUNIDADES")) { // igual acá
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
   }

   public void contarRegistros() {
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
   }

   //*/*/*/*/*/*/*/*/*/*-/-*//-*/-*/*/*-*/-*/-*/*/*/*/*/---/*/*/*/*/-*/-*/-*/-*/-*/
   public List<TiposUnidades> getListTiposUnidades() {
      if (listTiposUnidades == null) {
         listTiposUnidades = administrarTiposUnidades.consultarTiposUnidades();
      }
      return listTiposUnidades;
   }

   public void setListTiposUnidades(List<TiposUnidades> listTiposUnidades) {
      this.listTiposUnidades = listTiposUnidades;
   }

   public List<TiposUnidades> getFiltrarTiposUnidades() {
      return filtrarTiposUnidades;
   }

   public void setFiltrarTiposUnidades(List<TiposUnidades> filtrarTiposUnidades) {
      this.filtrarTiposUnidades = filtrarTiposUnidades;
   }

   public TiposUnidades getNuevoTiposUnidades() {
      return nuevoTiposUnidades;
   }

   public void setNuevoTiposUnidades(TiposUnidades nuevoTiposUnidades) {
      this.nuevoTiposUnidades = nuevoTiposUnidades;
   }

   public TiposUnidades getDuplicarTiposUnidades() {
      return duplicarTiposUnidades;
   }

   public void setDuplicarTiposUnidades(TiposUnidades duplicarTiposUnidades) {
      this.duplicarTiposUnidades = duplicarTiposUnidades;
   }

   public TiposUnidades getEditarTiposUnidades() {
      return editarTiposUnidades;
   }

   public void setEditarTiposUnidades(TiposUnidades editarTiposUnidades) {
      this.editarTiposUnidades = editarTiposUnidades;
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

   public TiposUnidades getTiposUnidadesSeleccionado() {
      return tiposUnidadesSeleccionado;
   }

   public void setTiposUnidadesSeleccionado(TiposUnidades clasesPensionesSeleccionado) {
      this.tiposUnidadesSeleccionado = clasesPensionesSeleccionado;
   }

   public String getInfoRegistro() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosTiposUnidades");
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
