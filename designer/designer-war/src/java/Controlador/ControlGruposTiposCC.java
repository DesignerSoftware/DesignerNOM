/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.GruposTiposCC;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarGruposTiposCCInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import ControlNavegacion.ControlListaNavegacion;
import java.util.Map;
import java.util.LinkedHashMap;
import javax.faces.application.FacesMessage;
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
@Named(value = "controlGruposTiposCC")
@SessionScoped
public class ControlGruposTiposCC implements Serializable {

   private static Logger log = Logger.getLogger(ControlGruposTiposCC.class);

   @EJB
   AdministrarGruposTiposCCInterface administrarGrupos;
   @EJB
   AdministrarRastrosInterface administrarRastros;

   private List<GruposTiposCC> listGruposTiposCC;
   private List<GruposTiposCC> listGruposTiposCCFiltrar;
   private List<GruposTiposCC> listGruposTiposCCCrear;
   private List<GruposTiposCC> listGruposTiposCCModificar;
   private List<GruposTiposCC> listGruposTiposCCBorrar;
   private GruposTiposCC nuevoGrupoTipoCC;
   private GruposTiposCC duplicarGrupoTipoCC;
   private GruposTiposCC editarGrupoTipoCC;
   private GruposTiposCC grupoTipoCCSeleccionado;
   private int cualCelda, tipoLista, tipoActualizacion, k, bandera;
   private BigDecimal l;
   private boolean aceptar, guardado;
   private boolean permitirIndex;
   private int registroBorrados;
   private String mensajeValidacion;
   private int altoTabla;
   private boolean activarLov;
   private String infoRegistro;
   private Column codigo, descripcion;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

   /**
    * Creates a new instance of ControlGruposTiposCC
    */
   public ControlGruposTiposCC() {
      listGruposTiposCC = null;
      listGruposTiposCCCrear = new ArrayList<GruposTiposCC>();
      listGruposTiposCCModificar = new ArrayList<GruposTiposCC>();
      listGruposTiposCCBorrar = new ArrayList<GruposTiposCC>();
      permitirIndex = true;
      editarGrupoTipoCC = new GruposTiposCC();
      nuevoGrupoTipoCC = new GruposTiposCC();
      duplicarGrupoTipoCC = new GruposTiposCC();
      guardado = true;
      altoTabla = 270;
      cualCelda = -1;
      activarLov = true;
      grupoTipoCCSeleccionado = null;
      mapParametros.put("paginaAnterior", paginaAnterior);
   }

   public void recibirPaginaEntrante(String pagina) {
      paginaAnterior = pagina;
      paginaAnterior = (String) mapParametros.get("paginaAnterior");
      listGruposTiposCC = null;
      getListGruposTiposCC();
      if (listGruposTiposCC != null) {
         grupoTipoCCSeleccionado = listGruposTiposCC.get(0);
      }
   }

   public void recibirParametros(Map<String, Object> map) {
      mapParametros = map;
      paginaAnterior = (String) mapParametros.get("paginaAnterior");
      getListGruposTiposCC();
      if (listGruposTiposCC != null) {
         grupoTipoCCSeleccionado = listGruposTiposCC.get(0);
      }
   }

   //Reemplazar la funcion volverAtras, retornarPagina, Redirigir.....Atras.etc
   public void navegar(String pag) {
      FacesContext fc = FacesContext.getCurrentInstance();
      ControlListaNavegacion controlListaNavegacion = (ControlListaNavegacion) fc.getApplication().evaluateExpressionGet(fc, "#{controlListaNavegacion}", ControlListaNavegacion.class);
      String pagActual = "grupotipocc";
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
         administrarRastros.obtenerConexion(ses.getId());
         administrarGrupos.obtenerConexion(ses.getId());
      } catch (Exception e) {
         log.error("Error postconstruct " + this.getClass().getName() + ":  ", e);
         log.error("Causa: " + e.getCause());
      }
   }

   public String retornarPagina() {
      return paginaAnterior;
   }

   public void cambiarIndice(GruposTiposCC grupo, int celda) {
      if (permitirIndex == true) {
         grupoTipoCCSeleccionado = grupo;
         cualCelda = celda;
         grupoTipoCCSeleccionado.getSecuencia();
         if (cualCelda == 0) {
            grupoTipoCCSeleccionado.getCodigo();
         } else if (cualCelda == 1) {
            grupoTipoCCSeleccionado.getDescripcion();
         }
      }
   }

   public void activarAceptar() {
      aceptar = false;
   }

   public void cancelarModificacion() {
      if (bandera == 1) {
         FacesContext c = FacesContext.getCurrentInstance();
         codigo = (Column) c.getViewRoot().findComponent("form:datosGruposTiposCC:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosGruposTiposCC:descripcion");
         descripcion.setFilterStyle("display: none; visibility: hidden;");
         bandera = 0;
         listGruposTiposCCFiltrar = null;
         altoTabla = 270;
         RequestContext.getCurrentInstance().update("form:datosGruposTiposCC");
         tipoLista = 0;
      }

      listGruposTiposCCBorrar.clear();
      listGruposTiposCCCrear.clear();
      listGruposTiposCCModificar.clear();
      grupoTipoCCSeleccionado = null;
      contarRegistros();
      k = 0;
      listGruposTiposCC = null;
      guardado = true;
      permitirIndex = true;
      RequestContext.getCurrentInstance().update("form:datosGruposTiposCC");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void salir() {
      limpiarListasValor();
      if (bandera == 1) {
         FacesContext c = FacesContext.getCurrentInstance();
         codigo = (Column) c.getViewRoot().findComponent("form:datosGruposTiposCC:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosGruposTiposCC:descripcion");
         descripcion.setFilterStyle("display: none; visibility: hidden;");
         bandera = 0;
         listGruposTiposCCFiltrar = null;
         tipoLista = 0;
         altoTabla = 270;
      }
      listGruposTiposCCBorrar.clear();
      listGruposTiposCCCrear.clear();
      listGruposTiposCCModificar.clear();
      grupoTipoCCSeleccionado = null;
      k = 0;
      listGruposTiposCC = null;
      guardado = true;
      navegar("atras");
   }

   public void activarCtrlF11() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 0) {
         altoTabla = 250;
         codigo = (Column) c.getViewRoot().findComponent("form:datosGruposTiposCC:codigo");
         codigo.setFilterStyle("width: 85% !important;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosGruposTiposCC:descripcion");
         descripcion.setFilterStyle("width: 85% !important;");
         RequestContext.getCurrentInstance().update("form:datosGruposTiposCC");
         bandera = 1;
      } else if (bandera == 1) {
         log.info("Desactivar");
         altoTabla = 270;
         codigo = (Column) c.getViewRoot().findComponent("form:datosGruposTiposCC:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosGruposTiposCC:descripcion");
         descripcion.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosGruposTiposCC");
         bandera = 0;
         listGruposTiposCCFiltrar = null;
         tipoLista = 0;
      }
   }

   public void modificarGruposTipo(GruposTiposCC grupot) {
      if (!listGruposTiposCCCrear.contains(grupoTipoCCSeleccionado)) {
         if (listGruposTiposCCModificar.isEmpty()) {
            listGruposTiposCCModificar.add(grupoTipoCCSeleccionado);
         } else if (!listGruposTiposCCModificar.contains(grupoTipoCCSeleccionado)) {
            listGruposTiposCCModificar.add(grupoTipoCCSeleccionado);
         }
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
      }
      RequestContext.getCurrentInstance().update("form:datosGruposTiposCC");
   }

   public void borrarGruposTipo() {
      if (grupoTipoCCSeleccionado != null) {
         if (!listGruposTiposCCModificar.isEmpty() && listGruposTiposCCModificar.contains(grupoTipoCCSeleccionado)) {
            listGruposTiposCCModificar.remove(listGruposTiposCCModificar.indexOf(grupoTipoCCSeleccionado));
            listGruposTiposCCBorrar.add(grupoTipoCCSeleccionado);
         } else if (!listGruposTiposCCCrear.isEmpty() && listGruposTiposCCCrear.contains(grupoTipoCCSeleccionado)) {
            listGruposTiposCCCrear.remove(listGruposTiposCCCrear.indexOf(grupoTipoCCSeleccionado));
         } else {
            listGruposTiposCCBorrar.add(grupoTipoCCSeleccionado);
         }
         listGruposTiposCC.remove(grupoTipoCCSeleccionado);
         if (tipoLista == 1) {
            listGruposTiposCCFiltrar.remove(grupoTipoCCSeleccionado);
         }
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:datosGruposTiposCC");
         contarRegistros();
         grupoTipoCCSeleccionado = null;
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
      if (!listGruposTiposCCBorrar.isEmpty() || !listGruposTiposCCModificar.isEmpty() || !listGruposTiposCCCrear.isEmpty()) {
         RequestContext.getCurrentInstance().update("form:confirmarGuardar");
         RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
      }
   }

   public void guardarGruposTipo() {
      RequestContext context = RequestContext.getCurrentInstance();
      try {
         if (guardado == false) {
            if (!listGruposTiposCCBorrar.isEmpty()) {
               administrarGrupos.borrarGrupo(listGruposTiposCCBorrar);
               registroBorrados = listGruposTiposCCBorrar.size();
               RequestContext.getCurrentInstance().update("form:mostrarBorrados");
               RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
               listGruposTiposCCBorrar.clear();
            }
            if (!listGruposTiposCCModificar.isEmpty()) {
               administrarGrupos.editarGrupo(listGruposTiposCCModificar);
               listGruposTiposCCModificar.clear();
            }
            if (!listGruposTiposCCCrear.isEmpty()) {
               administrarGrupos.crearGrupo(listGruposTiposCCCrear);
               listGruposTiposCCCrear.clear();
            }
            listGruposTiposCC = null;
            getListGruposTiposCC();
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            k = 0;
            FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
            contarRegistros();
            grupoTipoCCSeleccionado = null;
         }
         guardado = true;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         RequestContext.getCurrentInstance().update("form:datosGruposTiposCC");
      } catch (Exception e) {
         log.warn("Error guardarCambios : " + e.toString());
         FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado, intente nuevamente.");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      }
   }

   public void editarCelda() {
      if (grupoTipoCCSeleccionado != null) {
         editarGrupoTipoCC = grupoTipoCCSeleccionado;

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

   public void agregarNuevoGrupoTipo() {
      if (bandera == 1) {
         FacesContext c = FacesContext.getCurrentInstance();
         log.info("Desactivar");
         codigo = (Column) c.getViewRoot().findComponent("form:datosGruposTiposCC:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosGruposTiposCC:descripcion");
         descripcion.setFilterStyle("display: none; visibility: hidden;");
         bandera = 0;
         listGruposTiposCCFiltrar = null;
         tipoLista = 0;
         altoTabla = 270;
         RequestContext.getCurrentInstance().update("form:datosGruposTiposCC");
      }
      k++;
      l = BigDecimal.valueOf(k);
      nuevoGrupoTipoCC.setSecuencia(l);
      listGruposTiposCCCrear.add(nuevoGrupoTipoCC);
      listGruposTiposCC.add(nuevoGrupoTipoCC);
      contarRegistros();
      grupoTipoCCSeleccionado = nuevoGrupoTipoCC;
      nuevoGrupoTipoCC = new GruposTiposCC();
      RequestContext.getCurrentInstance().update("form:datosGruposTiposCC");
      if (guardado == true) {
         guardado = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      }
      RequestContext.getCurrentInstance().execute("PF('nuevoRegistroGruposTipoCC').hide()");
   }

   public void limpiarNuevoGrupoTipo() {
      nuevoGrupoTipoCC = new GruposTiposCC();
   }

   public void duplicandoGruposTipo() {
      if (grupoTipoCCSeleccionado != null) {
         duplicarGrupoTipoCC = new GruposTiposCC();
         k++;
         l = BigDecimal.valueOf(k);

         duplicarGrupoTipoCC.setSecuencia(l);
         duplicarGrupoTipoCC.setCodigo(grupoTipoCCSeleccionado.getCodigo());
         duplicarGrupoTipoCC.setDescripcion(grupoTipoCCSeleccionado.getDescripcion());

         if (tipoLista == 1) {
            altoTabla = 270;
         }
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarGT");
         RequestContext.getCurrentInstance().execute("PF('duplicarRegistroGruposTipoCC').show()");

      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void confirmarDuplicar() {
      int contador = 0;

      for (int i = 0; i < listGruposTiposCC.size(); i++) {
         if (duplicarGrupoTipoCC.getCodigo() == listGruposTiposCC.get(i).getCodigo()) {
            RequestContext.getCurrentInstance().update("formularioDialogos:existeCodigo");
            RequestContext.getCurrentInstance().execute("PF('existeCodigo').show()");
            contador++;
         }
      }

      if (contador == 0) {
         listGruposTiposCC.add(duplicarGrupoTipoCC);
         listGruposTiposCCCrear.add(duplicarGrupoTipoCC);
         grupoTipoCCSeleccionado = duplicarGrupoTipoCC;
         contarRegistros();
         RequestContext.getCurrentInstance().update("form:datosGruposTiposCC");
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }

         if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            codigo = (Column) c.getViewRoot().findComponent("form:datosGruposTiposCC:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosGruposTiposCC:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            bandera = 0;
            listGruposTiposCCFiltrar = null;
            RequestContext.getCurrentInstance().update("form:datosGruposTiposCC");
            tipoLista = 0;
         }
         duplicarGrupoTipoCC = new GruposTiposCC();
      }

      RequestContext.getCurrentInstance().update("formularioDialogos:duplicarRegistroGruposTipoCC");
      RequestContext.getCurrentInstance().execute("PF('duplicarRegistroGruposTipoCC').hide()");
   }

   public void limpiarDuplicarNuevoGrupoTipo() {
      duplicarGrupoTipoCC = new GruposTiposCC();
   }

   public void exportPDF() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosGruposTipoCCExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "GRUPOSTIPOCC", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void exportXLS() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosGruposTipoCCExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "GRUPOSTIPOCC", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void verificarRastro() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (grupoTipoCCSeleccionado != null) {
         int resultado = administrarRastros.obtenerTabla(grupoTipoCCSeleccionado.getSecuencia().toBigInteger(), "GRUPOSTIPOCC"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
      } else if (administrarRastros.verificarHistoricosTabla("GRUPOSTIPOCC")) {
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
      RequestContext.getCurrentInstance().update("form:infoRegistro");
   }

   ///////SETS Y GETS //////////
   public List<GruposTiposCC> getListGruposTiposCC() {
      if (listGruposTiposCC == null) {
         listGruposTiposCC = administrarGrupos.consultarGrupos();
      }

      return listGruposTiposCC;
   }

   public void setListGruposTiposCC(List<GruposTiposCC> listGruposTiposCC) {
      this.listGruposTiposCC = listGruposTiposCC;
   }

   public List<GruposTiposCC> getListGruposTiposCCFiltrar() {
      return listGruposTiposCCFiltrar;
   }

   public void setListGruposTiposCCFiltrar(List<GruposTiposCC> listGruposTiposCCFiltrar) {
      this.listGruposTiposCCFiltrar = listGruposTiposCCFiltrar;
   }

   public GruposTiposCC getNuevoGrupoTipoCC() {
      return nuevoGrupoTipoCC;
   }

   public void setNuevoGrupoTipoCC(GruposTiposCC nuevoGrupoTipoCC) {
      this.nuevoGrupoTipoCC = nuevoGrupoTipoCC;
   }

   public GruposTiposCC getDuplicarGrupoTipoCC() {
      return duplicarGrupoTipoCC;
   }

   public void setDuplicarGrupoTipoCC(GruposTiposCC duplicarGrupoTipoCC) {
      this.duplicarGrupoTipoCC = duplicarGrupoTipoCC;
   }

   public GruposTiposCC getEditarGrupoTipoCC() {
      return editarGrupoTipoCC;
   }

   public void setEditarGrupoTipoCC(GruposTiposCC editarGrupoTipoCC) {
      this.editarGrupoTipoCC = editarGrupoTipoCC;
   }

   public GruposTiposCC getGrupoTipoCCSeleccionado() {
      return grupoTipoCCSeleccionado;
   }

   public void setGrupoTipoCCSeleccionado(GruposTiposCC grupoTipoCCSeleccionado) {
      this.grupoTipoCCSeleccionado = grupoTipoCCSeleccionado;
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

   public String getMensajeValidacion() {
      return mensajeValidacion;
   }

   public void setMensajeValidacion(String mensajeValidacion) {
      this.mensajeValidacion = mensajeValidacion;
   }

   public int getAltoTabla() {
      return altoTabla;
   }

   public void setAltoTabla(int altoTabla) {
      this.altoTabla = altoTabla;
   }

   public boolean isActivarLov() {
      return activarLov;
   }

   public void setActivarLov(boolean activarLov) {
      this.activarLov = activarLov;
   }

   public String getInfoRegistro() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosGruposTiposCC");
      infoRegistro = String.valueOf(tabla.getRowCount());
      return infoRegistro;
   }

   public void setInfoRegistro(String infoRegistro) {
      this.infoRegistro = infoRegistro;
   }

   public int getRegistroBorrados() {
      return registroBorrados;
   }

   public void setRegistroBorrados(int registroBorrados) {
      this.registroBorrados = registroBorrados;
   }

}
