/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.ConceptosRetroactivos;
import Entidades.Conceptos;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarConceptosRetroactivosInterface;
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
public class ControlConceptosRetroactivos implements Serializable {

   private static Logger log = Logger.getLogger(ControlConceptosRetroactivos.class);

   @EJB
   AdministrarConceptosRetroactivosInterface administrarConceptosRetroactivos;
   @EJB
   AdministrarRastrosInterface administrarRastros;
   private List<ConceptosRetroactivos> listConceptosRetroactivos;
   private List<ConceptosRetroactivos> filtrarConceptosRetroactivos;
   private List<ConceptosRetroactivos> crearConceptosRetroactivos;
   private List<ConceptosRetroactivos> modificarConceptosRetroactivos;
   private List<ConceptosRetroactivos> borrarConceptosRetroactivos;
   private ConceptosRetroactivos nuevoConceptosRetroactivos;
   private ConceptosRetroactivos duplicarConceptosRetroactivos;
   private ConceptosRetroactivos editarConceptosRetroactivos;
   private ConceptosRetroactivos sucursalSeleccionada;
   //otros
   private int cualCelda, tipoLista, index, tipoActualizacion, k, bandera;
   private BigInteger l;
   private boolean aceptar, guardado;
   //AutoCompletar
   private boolean permitirIndex;
   //RASTRO
   private BigInteger secRegistro;
   private Column codigo, descripcion, personafir, cargo;
   //borrado
   private int registrosBorrados;
   private String mensajeValidacion;
   //filtrado table
   private int tamano;

   //---------------------------------
   private String backupConcepto;
   private List<Conceptos> lovConceptos;
   private List<Conceptos> filtradoConceptos;
   private Conceptos conceptoSeleccionado;
   private String nuevoYduplicarCompletarConcepto;
   //--------------------------------------
   private String backupConceptoRetro;
   private List<Conceptos> lovConceptosRetro;
   private List<Conceptos> filtradoConceptosRetro;
   private Conceptos conceptoRetroSeleccionado;
   private String nuevoYduplicarCompletarConceptoRetro;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

   public ControlConceptosRetroactivos() {
      listConceptosRetroactivos = null;
      crearConceptosRetroactivos = new ArrayList<ConceptosRetroactivos>();
      modificarConceptosRetroactivos = new ArrayList<ConceptosRetroactivos>();
      borrarConceptosRetroactivos = new ArrayList<ConceptosRetroactivos>();
      permitirIndex = true;
      editarConceptosRetroactivos = new ConceptosRetroactivos();
      nuevoConceptosRetroactivos = new ConceptosRetroactivos();
      nuevoConceptosRetroactivos.setConcepto(new Conceptos());
      nuevoConceptosRetroactivos.setConceptoRetroActivo(new Conceptos());
      duplicarConceptosRetroactivos = new ConceptosRetroactivos();
      duplicarConceptosRetroactivos.setConcepto(new Conceptos());
      duplicarConceptosRetroactivos.setConceptoRetroActivo(new Conceptos());
      lovConceptos = null;
      filtradoConceptos = null;
      lovConceptosRetro = null;
      filtradoConceptosRetro = null;
      guardado = true;
      tamano = 270;
      aceptar = true;
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
      String pagActual = "conceptoretroactivo";
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
      lovConceptos = null;
      lovConceptosRetro = null;
   }

   @PostConstruct
   public void inicializarAdministrador() {
      try {
         FacesContext x = FacesContext.getCurrentInstance();
         HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
         administrarConceptosRetroactivos.obtenerConexion(ses.getId());
         administrarRastros.obtenerConexion(ses.getId());
      } catch (Exception e) {
         log.error("Error postconstruct " + this.getClass().getName() + ": " + e);
         log.error("Causa: " + e.getCause());
      }
   }

   public void eventoFiltrar() {
      try {
         log.info("\n ENTRE A ControlConceptosRetroactivos.eventoFiltrar \n");
         if (tipoLista == 0) {
            tipoLista = 1;
         }
         RequestContext context = RequestContext.getCurrentInstance();
         infoRegistro = "Cantidad de registros: " + filtrarConceptosRetroactivos.size();
         RequestContext.getCurrentInstance().update("form:informacionRegistro");
      } catch (Exception e) {
         log.warn("Error ControlConceptosRetroactivos eventoFiltrar ERROR===" + e.getMessage());
      }
   }

   public void cambiarIndice(int indice, int celda) {
      log.error("TIPO LISTA = " + tipoLista);

      if (permitirIndex == true) {
         index = indice;
         cualCelda = celda;
         secRegistro = listConceptosRetroactivos.get(index).getSecuencia();
         if (tipoLista == 0) {

            if (cualCelda == 0) {
               backupConcepto = listConceptosRetroactivos.get(index).getConcepto().getDescripcion();
               log.info("BANCO : " + backupConcepto);

            }
            if (cualCelda == 1) {
               backupConceptoRetro = listConceptosRetroactivos.get(index).getConceptoRetroActivo().getDescripcion();
               log.info("CIUDAD : " + backupConceptoRetro);

            }

         } else if (tipoLista == 1) {

            if (cualCelda == 0) {
               backupConcepto = filtrarConceptosRetroactivos.get(index).getConcepto().getDescripcion();
               log.info("BANCO : " + backupConcepto);

            }
            if (cualCelda == 1) {
               backupConceptoRetro = filtrarConceptosRetroactivos.get(index).getConceptoRetroActivo().getDescripcion();
               log.info("CIUDAD : " + backupConceptoRetro);

            }
         }

      }
      log.info("Indice: " + index + " Celda: " + cualCelda);
   }

   public void asignarIndex(Integer indice, int LND, int dig) {
      try {
         RequestContext context = RequestContext.getCurrentInstance();
         log.info("\n ENTRE A ControlConceptosRetroactivos.asignarIndex \n");
         index = indice;
         if (LND == 0) {
            tipoActualizacion = 0;
         } else if (LND == 1) {
            tipoActualizacion = 1;
            log.info("Tipo Actualizacion: " + tipoActualizacion);
         } else if (LND == 2) {
            tipoActualizacion = 2;
         }
         if (dig == 0) {
            RequestContext.getCurrentInstance().update("form:conceptosDialogo");
            RequestContext.getCurrentInstance().execute("PF('conceptosDialogo').show()");
            dig = -1;
         }
         if (dig == 1) {
            RequestContext.getCurrentInstance().update("form:conceptosRetroDialogo");
            RequestContext.getCurrentInstance().execute("PF('conceptosRetroDialogo').show()");
            dig = -1;
         }
      } catch (Exception e) {
         log.warn("Error ControlConceptosRetroactivos.asignarIndex ERROR======" + e.getMessage());
      }
   }

   public void activarAceptar() {
      aceptar = false;
   }

   public void listaValoresBoton() {
      if (index >= 0) {

         if (cualCelda == 0) {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:conceptosDialogo");
            RequestContext.getCurrentInstance().execute("PF('conceptosDialogo').show()");
         }
         if (cualCelda == 1) {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:conceptosRetroDialogo");
            RequestContext.getCurrentInstance().execute("PF('conceptosRetroDialogo').show()");
         }
      }
   }
   private String infoRegistro;

   public void cancelarModificacion() {
      if (bandera == 1) {
         //CERRAR FILTRADO
         FacesContext c = FacesContext.getCurrentInstance();
         codigo = (Column) c.getViewRoot().findComponent("form:datosConceptosRetroactivos:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosConceptosRetroactivos:descripcion");
         descripcion.setFilterStyle("display: none; visibility: hidden;");
         personafir = (Column) c.getViewRoot().findComponent("form:datosConceptosRetroactivos:personafir");
         personafir.setFilterStyle("display: none; visibility: hidden;");
         cargo = (Column) c.getViewRoot().findComponent("form:datosConceptosRetroactivos:cargo");
         cargo.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosConceptosRetroactivos");
         bandera = 0;
         filtrarConceptosRetroactivos = null;
         tipoLista = 0;
      }

      borrarConceptosRetroactivos.clear();
      crearConceptosRetroactivos.clear();
      modificarConceptosRetroactivos.clear();
      index = -1;
      secRegistro = null;
      k = 0;
      listConceptosRetroactivos = null;
      guardado = true;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      if (listConceptosRetroactivos == null || listConceptosRetroactivos.isEmpty()) {
         infoRegistro = "Cantidad de registros: 0 ";
      } else {
         infoRegistro = "Cantidad de registros: " + listConceptosRetroactivos.size();
      }
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
      RequestContext.getCurrentInstance().update("form:datosConceptosRetroactivos");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void salir() {
      limpiarListasValor();
      if (bandera == 1) {
         //CERRAR FILTRADO
         FacesContext c = FacesContext.getCurrentInstance();
         codigo = (Column) c.getViewRoot().findComponent("form:datosConceptosRetroactivos:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosConceptosRetroactivos:descripcion");
         descripcion.setFilterStyle("display: none; visibility: hidden;");
         personafir = (Column) c.getViewRoot().findComponent("form:datosConceptosRetroactivos:personafir");
         personafir.setFilterStyle("display: none; visibility: hidden;");
         cargo = (Column) c.getViewRoot().findComponent("form:datosConceptosRetroactivos:cargo");
         cargo.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosConceptosRetroactivos");
         bandera = 0;
         filtrarConceptosRetroactivos = null;
         tipoLista = 0;
      }

      borrarConceptosRetroactivos.clear();
      crearConceptosRetroactivos.clear();
      modificarConceptosRetroactivos.clear();
      index = -1;
      secRegistro = null;
      k = 0;
      listConceptosRetroactivos = null;
      guardado = true;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      if (listConceptosRetroactivos == null || listConceptosRetroactivos.isEmpty()) {
         infoRegistro = "Cantidad de registros: 0 ";
      } else {
         infoRegistro = "Cantidad de registros: " + listConceptosRetroactivos.size();
      }
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
      RequestContext.getCurrentInstance().update("form:datosConceptosRetroactivos");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      navegar("atras");
   }

   public void activarCtrlF11() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 0) {
         tamano = 250;
         codigo = (Column) c.getViewRoot().findComponent("form:datosConceptosRetroactivos:codigo");
         codigo.setFilterStyle("width: 85% !important");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosConceptosRetroactivos:descripcion");
         descripcion.setFilterStyle("width: 85% !important");
         personafir = (Column) c.getViewRoot().findComponent("form:datosConceptosRetroactivos:personafir");
         personafir.setFilterStyle("width: 85% !important");
         cargo = (Column) c.getViewRoot().findComponent("form:datosConceptosRetroactivos:cargo");
         cargo.setFilterStyle("width: 85% !important");
         RequestContext.getCurrentInstance().update("form:datosConceptosRetroactivos");
         log.info("Activar");
         bandera = 1;
      } else if (bandera == 1) {
         log.info("Desactivar");
         tamano = 270;
         codigo = (Column) c.getViewRoot().findComponent("form:datosConceptosRetroactivos:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosConceptosRetroactivos:descripcion");
         descripcion.setFilterStyle("display: none; visibility: hidden;");
         personafir = (Column) c.getViewRoot().findComponent("form:datosConceptosRetroactivos:personafir");
         personafir.setFilterStyle("display: none; visibility: hidden;");
         cargo = (Column) c.getViewRoot().findComponent("form:datosConceptosRetroactivos:cargo");
         cargo.setFilterStyle("display: none; visibility: hidden;");

         RequestContext.getCurrentInstance().update("form:datosConceptosRetroactivos");
         bandera = 0;
         filtrarConceptosRetroactivos = null;
         tipoLista = 0;
      }
   }

   public void actualizarConceptos() {
      RequestContext context = RequestContext.getCurrentInstance();
      log.info("banco seleccionado : " + conceptoSeleccionado.getDescripcion());
      log.info("tipo Actualizacion : " + tipoActualizacion);
      log.info("tipo Lista : " + tipoLista);
      log.error("banco seleccionado : " + conceptoSeleccionado.getDescripcion());
      log.error("tipo Actualizacion : " + tipoActualizacion);
      log.error("tipo Lista : " + tipoLista);

      if (tipoActualizacion == 0) {
         if (tipoLista == 0) {
            listConceptosRetroactivos.get(index).setConcepto(conceptoSeleccionado);

            if (!crearConceptosRetroactivos.contains(listConceptosRetroactivos.get(index))) {
               if (modificarConceptosRetroactivos.isEmpty()) {
                  modificarConceptosRetroactivos.add(listConceptosRetroactivos.get(index));
               } else if (!modificarConceptosRetroactivos.contains(listConceptosRetroactivos.get(index))) {
                  modificarConceptosRetroactivos.add(listConceptosRetroactivos.get(index));
               }
            }
         } else {
            filtrarConceptosRetroactivos.get(index).setConcepto(conceptoSeleccionado);

            if (!crearConceptosRetroactivos.contains(filtrarConceptosRetroactivos.get(index))) {
               if (modificarConceptosRetroactivos.isEmpty()) {
                  modificarConceptosRetroactivos.add(filtrarConceptosRetroactivos.get(index));
               } else if (!modificarConceptosRetroactivos.contains(filtrarConceptosRetroactivos.get(index))) {
                  modificarConceptosRetroactivos.add(filtrarConceptosRetroactivos.get(index));
               }
            }
         }
         if (guardado == true) {
            guardado = false;
         }
         permitirIndex = true;
         log.info("ACTUALIZAR PAIS PAIS SELECCIONADO : " + conceptoSeleccionado.getDescripcion());
         RequestContext.getCurrentInstance().update("form:datosConceptosRetroactivos");
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      } else if (tipoActualizacion == 1) {
         log.info("ACTUALIZAR PAIS NUEVO DEPARTAMENTO: " + conceptoSeleccionado.getDescripcion());
         nuevoConceptosRetroactivos.setConcepto(conceptoSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevoPersona");
      } else if (tipoActualizacion == 2) {
         log.info("ACTUALIZAR PAIS DUPLICAR DEPARTAMENO: " + conceptoSeleccionado.getDescripcion());
         duplicarConceptosRetroactivos.setConcepto(conceptoSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarPersona");
      }
      filtradoConceptos = null;
      conceptoSeleccionado = null;
      aceptar = true;
      index = -1;
      secRegistro = null;
      tipoActualizacion = -1;
      cualCelda = -1;
      context.reset("form:lovConceptos:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovConceptos').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('conceptosDialogo').hide()");
      //RequestContext.getCurrentInstance().update("form:lovConceptos");
      //RequestContext.getCurrentInstance().update("form:datosHvEntrevista");
   }

   public void actualizarConceptosRetro() {
      RequestContext context = RequestContext.getCurrentInstance();
      log.info("ciudad seleccionado : " + conceptoRetroSeleccionado.getDescripcion());
      log.info("tipo Actualizacion : " + tipoActualizacion);
      log.info("tipo Lista : " + tipoLista);

      if (tipoActualizacion == 0) {
         if (tipoLista == 0) {
            listConceptosRetroactivos.get(index).setConceptoRetroActivo(conceptoRetroSeleccionado);

            if (!crearConceptosRetroactivos.contains(listConceptosRetroactivos.get(index))) {
               if (modificarConceptosRetroactivos.isEmpty()) {
                  modificarConceptosRetroactivos.add(listConceptosRetroactivos.get(index));
               } else if (!modificarConceptosRetroactivos.contains(listConceptosRetroactivos.get(index))) {
                  modificarConceptosRetroactivos.add(listConceptosRetroactivos.get(index));
               }
            }
         } else {
            filtrarConceptosRetroactivos.get(index).setConceptoRetroActivo(conceptoRetroSeleccionado);

            if (!crearConceptosRetroactivos.contains(filtrarConceptosRetroactivos.get(index))) {
               if (modificarConceptosRetroactivos.isEmpty()) {
                  modificarConceptosRetroactivos.add(filtrarConceptosRetroactivos.get(index));
               } else if (!modificarConceptosRetroactivos.contains(filtrarConceptosRetroactivos.get(index))) {
                  modificarConceptosRetroactivos.add(filtrarConceptosRetroactivos.get(index));
               }
            }
         }
         if (guardado == true) {
            guardado = false;
         }
         permitirIndex = true;
         log.info("ACTUALIZAR PAIS CONCEPTOSRETRO SELECCIONADO : " + conceptoRetroSeleccionado.getDescripcion());
         RequestContext.getCurrentInstance().update("form:datosConceptosRetroactivos");
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      } else if (tipoActualizacion == 1) {
         log.info("ACTUALIZAR PAIS NUEVO DEPARTAMENTO: " + conceptoRetroSeleccionado.getDescripcion());
         nuevoConceptosRetroactivos.setConceptoRetroActivo(conceptoRetroSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevoCargo");
      } else if (tipoActualizacion == 2) {
         log.info("ACTUALIZAR PAIS DUPLICAR DEPARTAMENO: " + conceptoRetroSeleccionado.getDescripcion());
         duplicarConceptosRetroactivos.setConceptoRetroActivo(conceptoRetroSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCargo");
      }
      filtradoConceptos = null;
      conceptoSeleccionado = null;
      aceptar = true;
      index = -1;
      secRegistro = null;
      tipoActualizacion = -1;
      cualCelda = -1;
      context.reset("form:lovConceptosRetro:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovConceptosRetro').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('conceptosRetroDialogo').hide()");
      //RequestContext.getCurrentInstance().update("form:lovConceptosRetro");
      //RequestContext.getCurrentInstance().update("form:datosHvEntrevista");
   }

   public void cancelarCambioConceptos() {
      listConceptosRetroactivos.get(index).getConcepto().setDescripcion(backupConcepto);
      filtradoConceptos = null;
      conceptoSeleccionado = null;
      aceptar = true;
      index = -1;
      secRegistro = null;
      tipoActualizacion = -1;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("form:lovConceptos:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovConceptos').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('conceptosDialogo').hide()");
   }

   public void cancelarCambioConceptosRetro() {
      filtradoConceptosRetro = null;
      conceptoRetroSeleccionado = null;
      aceptar = true;
      index = -1;
      secRegistro = null;
      tipoActualizacion = -1;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("form:lovConceptosRetro:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovConceptosRetro').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('conceptosRetroDialogo').hide()");
   }

   public void modificarConceptosRetroactivos(int indice, String confirmarCambio, String valorConfirmar) {
      log.error("ENTRE A MODIFICAR SUB CATEGORIA");
      index = indice;
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      log.error("TIPO LISTA = " + tipoLista);
      if (confirmarCambio.equalsIgnoreCase("CONCEPTOS")) {
         log.info("MODIFICANDO NORMA LABORAL : " + listConceptosRetroactivos.get(indice).getConcepto().getDescripcion());
         if (!listConceptosRetroactivos.get(indice).getConcepto().getDescripcion().equals("")) {
            if (tipoLista == 0) {
               listConceptosRetroactivos.get(indice).getConcepto().setDescripcion(backupConcepto);
            } else {
               listConceptosRetroactivos.get(indice).getConcepto().setDescripcion(backupConcepto);
            }

            for (int i = 0; i < lovConceptos.size(); i++) {
               if (lovConceptos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }

            if (coincidencias == 1) {
               if (tipoLista == 0) {
                  listConceptosRetroactivos.get(indice).setConcepto(lovConceptos.get(indiceUnicoElemento));
               } else {
                  filtrarConceptosRetroactivos.get(indice).setConcepto(lovConceptos.get(indiceUnicoElemento));
               }
               lovConceptos.clear();
               lovConceptos = null;
               //getListaTiposFamiliares();

            } else {
               permitirIndex = false;
               RequestContext.getCurrentInstance().update("form:conceptosDialogo");
               RequestContext.getCurrentInstance().execute("PF('conceptosDialogo').show()");
               tipoActualizacion = 0;
            }
         } else {
            if (backupConcepto != null) {
               if (tipoLista == 0) {
                  listConceptosRetroactivos.get(index).getConcepto().setDescripcion(backupConcepto);
               } else {
                  filtrarConceptosRetroactivos.get(index).getConcepto().setDescripcion(backupConcepto);
               }
            }
            tipoActualizacion = 0;
            log.info("PAIS ANTES DE MOSTRAR DIALOGO CONCEPTOS : " + backupConcepto);
            RequestContext.getCurrentInstance().update("form:conceptosDialogo");
            RequestContext.getCurrentInstance().execute("PF('conceptosDialogo').show()");
         }

         if (coincidencias == 1) {
            if (tipoLista == 0) {
               if (!crearConceptosRetroactivos.contains(listConceptosRetroactivos.get(indice))) {

                  if (modificarConceptosRetroactivos.isEmpty()) {
                     modificarConceptosRetroactivos.add(listConceptosRetroactivos.get(indice));
                  } else if (!modificarConceptosRetroactivos.contains(listConceptosRetroactivos.get(indice))) {
                     modificarConceptosRetroactivos.add(listConceptosRetroactivos.get(indice));
                  }
                  if (guardado == true) {
                     guardado = false;
                  }
               }
               index = -1;
               secRegistro = null;
            } else {
               if (!crearConceptosRetroactivos.contains(filtrarConceptosRetroactivos.get(indice))) {

                  if (modificarConceptosRetroactivos.isEmpty()) {
                     modificarConceptosRetroactivos.add(filtrarConceptosRetroactivos.get(indice));
                  } else if (!modificarConceptosRetroactivos.contains(filtrarConceptosRetroactivos.get(indice))) {
                     modificarConceptosRetroactivos.add(filtrarConceptosRetroactivos.get(indice));
                  }
                  if (guardado == true) {
                     guardado = false;
                  }
               }
               index = -1;
               secRegistro = null;
            }
         }

         RequestContext.getCurrentInstance().update("form:datosConceptosRetroactivos");
         RequestContext.getCurrentInstance().update("form:ACEPTAR");

      } else if (confirmarCambio.equalsIgnoreCase("CONCEPTOSRETRO")) {
         log.info("MODIFICANDO CONCEPTOSRETRO: " + listConceptosRetroactivos.get(indice).getConceptoRetroActivo().getDescripcion());
         if (!listConceptosRetroactivos.get(indice).getConcepto().getDescripcion().equals("")) {
            if (tipoLista == 0) {
               listConceptosRetroactivos.get(indice).getConceptoRetroActivo().setDescripcion(backupConceptoRetro);
            } else {
               listConceptosRetroactivos.get(indice).getConceptoRetroActivo().setDescripcion(backupConceptoRetro);
            }

            for (int i = 0; i < lovConceptosRetro.size(); i++) {
               if (lovConceptosRetro.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }

            if (coincidencias == 1) {
               if (tipoLista == 0) {
                  listConceptosRetroactivos.get(indice).setConceptoRetroActivo(lovConceptosRetro.get(indiceUnicoElemento));
               } else {
                  filtrarConceptosRetroactivos.get(indice).setConceptoRetroActivo(lovConceptosRetro.get(indiceUnicoElemento));
               }
               lovConceptosRetro.clear();
               lovConceptosRetro = null;
               //getListaTiposFamiliares();

            } else {
               permitirIndex = false;
               RequestContext.getCurrentInstance().update("form:conceptosRetroDialogo");
               RequestContext.getCurrentInstance().execute("PF('conceptosRetroDialogo').show()");
               tipoActualizacion = 0;
            }
         } else {
            if (backupConceptoRetro != null) {
               if (tipoLista == 0) {
                  listConceptosRetroactivos.get(index).getConceptoRetroActivo().setDescripcion(backupConceptoRetro);
               } else {
                  filtrarConceptosRetroactivos.get(index).getConceptoRetroActivo().setDescripcion(backupConceptoRetro);
               }
            }
            tipoActualizacion = 0;
            log.info("PAIS ANTES DE MOSTRAR DIALOGO CONCEPTOSRETRO : " + backupConceptoRetro);
            RequestContext.getCurrentInstance().update("form:conceptosDialogo");
            RequestContext.getCurrentInstance().execute("PF('conceptosDialogo').show()");
         }

         if (coincidencias == 1) {
            if (tipoLista == 0) {
               if (!crearConceptosRetroactivos.contains(listConceptosRetroactivos.get(indice))) {

                  if (modificarConceptosRetroactivos.isEmpty()) {
                     modificarConceptosRetroactivos.add(listConceptosRetroactivos.get(indice));
                  } else if (!modificarConceptosRetroactivos.contains(listConceptosRetroactivos.get(indice))) {
                     modificarConceptosRetroactivos.add(listConceptosRetroactivos.get(indice));
                  }
                  if (guardado == true) {
                     guardado = false;
                  }
               }
               index = -1;
               secRegistro = null;
            } else {
               if (!crearConceptosRetroactivos.contains(filtrarConceptosRetroactivos.get(indice))) {

                  if (modificarConceptosRetroactivos.isEmpty()) {
                     modificarConceptosRetroactivos.add(filtrarConceptosRetroactivos.get(indice));
                  } else if (!modificarConceptosRetroactivos.contains(filtrarConceptosRetroactivos.get(indice))) {
                     modificarConceptosRetroactivos.add(filtrarConceptosRetroactivos.get(indice));
                  }
                  if (guardado == true) {
                     guardado = false;
                  }
               }
               index = -1;
               secRegistro = null;
            }
         }

         RequestContext.getCurrentInstance().update("form:datosConceptosRetroactivos");
         RequestContext.getCurrentInstance().update("form:ACEPTAR");

      }

   }

   public void borrandoConceptosRetroactivos() {

      if (index >= 0) {
         if (tipoLista == 0) {
            log.info("Entro a borrandoConceptosRetroactivos");
            if (!modificarConceptosRetroactivos.isEmpty() && modificarConceptosRetroactivos.contains(listConceptosRetroactivos.get(index))) {
               int modIndex = modificarConceptosRetroactivos.indexOf(listConceptosRetroactivos.get(index));
               modificarConceptosRetroactivos.remove(modIndex);
               borrarConceptosRetroactivos.add(listConceptosRetroactivos.get(index));
            } else if (!crearConceptosRetroactivos.isEmpty() && crearConceptosRetroactivos.contains(listConceptosRetroactivos.get(index))) {
               int crearIndex = crearConceptosRetroactivos.indexOf(listConceptosRetroactivos.get(index));
               crearConceptosRetroactivos.remove(crearIndex);
            } else {
               borrarConceptosRetroactivos.add(listConceptosRetroactivos.get(index));
            }
            listConceptosRetroactivos.remove(index);
         }
         if (tipoLista == 1) {
            log.info("borrandoConceptosRetroactivos ");
            if (!modificarConceptosRetroactivos.isEmpty() && modificarConceptosRetroactivos.contains(filtrarConceptosRetroactivos.get(index))) {
               int modIndex = modificarConceptosRetroactivos.indexOf(filtrarConceptosRetroactivos.get(index));
               modificarConceptosRetroactivos.remove(modIndex);
               borrarConceptosRetroactivos.add(filtrarConceptosRetroactivos.get(index));
            } else if (!crearConceptosRetroactivos.isEmpty() && crearConceptosRetroactivos.contains(filtrarConceptosRetroactivos.get(index))) {
               int crearIndex = crearConceptosRetroactivos.indexOf(filtrarConceptosRetroactivos.get(index));
               crearConceptosRetroactivos.remove(crearIndex);
            } else {
               borrarConceptosRetroactivos.add(filtrarConceptosRetroactivos.get(index));
            }
            int VCIndex = listConceptosRetroactivos.indexOf(filtrarConceptosRetroactivos.get(index));
            listConceptosRetroactivos.remove(VCIndex);

            filtrarConceptosRetroactivos.remove(index);

         }
         RequestContext context = RequestContext.getCurrentInstance();
         if (listConceptosRetroactivos == null || listConceptosRetroactivos.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
         } else {
            infoRegistro = "Cantidad de registros: " + listConceptosRetroactivos.size();
         }
         RequestContext.getCurrentInstance().update("form:informacionRegistro");
         RequestContext.getCurrentInstance().update("form:datosConceptosRetroactivos");
         index = -1;
         secRegistro = null;

         if (guardado == true) {
            guardado = false;
         }
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      }

   }

   public void valoresBackupAutocompletar(int tipoNuevo, String valorCambio) {
      log.info("1...");
      if (valorCambio.equals("CONCEPTOS")) {
         if (tipoNuevo == 1) {
            nuevoYduplicarCompletarConcepto = nuevoConceptosRetroactivos.getConcepto().getDescripcion();
         } else if (tipoNuevo == 2) {
            nuevoYduplicarCompletarConcepto = duplicarConceptosRetroactivos.getConcepto().getDescripcion();
         }

         log.info("CONCEPTOS : " + nuevoYduplicarCompletarConcepto);
      } else if (valorCambio.equals("CONCEPTOSRETRO")) {
         if (tipoNuevo == 1) {
            nuevoYduplicarCompletarConceptoRetro = nuevoConceptosRetroactivos.getConceptoRetroActivo().getDescripcion();
         } else if (tipoNuevo == 2) {
            nuevoYduplicarCompletarConceptoRetro = duplicarConceptosRetroactivos.getConceptoRetroActivo().getDescripcion();
         }
         log.info("CONCEPTOSRETRO : " + nuevoYduplicarCompletarConceptoRetro);
      }

   }

   public void autocompletarNuevo(String confirmarCambio, String valorConfirmar, int tipoNuevo) {

      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      if (confirmarCambio.equalsIgnoreCase("CONCEPTOS")) {
         log.info(" nueva ConceptoRetro    Entro al if 'Centro costo'");
         log.info("NUEVO CONCEPTOS :-------> " + nuevoConceptosRetroactivos.getConcepto().getDescripcion());

         if (!nuevoConceptosRetroactivos.getConcepto().getDescripcion().equals("")) {
            log.info("ENTRO DONDE NO TENIA QUE ENTRAR");
            log.info("valorConfirmar: " + valorConfirmar);
            log.info("nuevoYduplicarCompletarPersona: " + nuevoYduplicarCompletarConcepto);
            nuevoConceptosRetroactivos.getConcepto().setDescripcion(nuevoYduplicarCompletarConcepto);
            for (int i = 0; i < lovConceptos.size(); i++) {
               if (lovConceptos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            log.info("Coincidencias: " + coincidencias);
            if (coincidencias == 1) {
               nuevoConceptosRetroactivos.setConcepto(lovConceptos.get(indiceUnicoElemento));
               lovConceptos = null;
               log.error("CONCEPTOS GUARDADA :-----> " + nuevoConceptosRetroactivos.getConcepto().getDescripcion());
            } else {
               RequestContext.getCurrentInstance().update("form:conceptosDialogo");
               RequestContext.getCurrentInstance().execute("PF('conceptosDialogo').show()");
               tipoActualizacion = tipoNuevo;
            }
         } else {
            nuevoConceptosRetroactivos.getConcepto().setDescripcion(nuevoYduplicarCompletarConcepto);
            log.info("valorConfirmar cuando es vacio: " + valorConfirmar);
            nuevoConceptosRetroactivos.setConcepto(new Conceptos());
            nuevoConceptosRetroactivos.getConcepto().setDescripcion(" ");
            log.info("NUEVA NORMA LABORAL" + nuevoConceptosRetroactivos.getConcepto().getDescripcion());
         }
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevoPersona");
      } else if (confirmarCambio.equalsIgnoreCase("CONCEPTOSRETRO")) {
         log.info(" nueva ConceptoRetro    Entro al if 'Centro costo'");
         log.info("NUEVO CONCEPTOS :-------> " + nuevoConceptosRetroactivos.getConceptoRetroActivo().getDescripcion());

         if (!nuevoConceptosRetroactivos.getConceptoRetroActivo().getDescripcion().equals("")) {
            log.info("ENTRO DONDE NO TENIA QUE ENTRAR");
            log.info("valorConfirmar: " + valorConfirmar);
            log.info("nuevoYduplicarCompletarPersona: " + nuevoYduplicarCompletarConceptoRetro);
            nuevoConceptosRetroactivos.getConceptoRetroActivo().setDescripcion(nuevoYduplicarCompletarConceptoRetro);
            for (int i = 0; i < lovConceptosRetro.size(); i++) {
               if (lovConceptosRetro.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            log.info("Coincidencias: " + coincidencias);
            if (coincidencias == 1) {
               nuevoConceptosRetroactivos.setConceptoRetroActivo(lovConceptosRetro.get(indiceUnicoElemento));
               lovConceptosRetro = null;
               log.error("CONCEPTOSRETRO GUARDADA :-----> " + nuevoConceptosRetroactivos.getConceptoRetroActivo().getDescripcion());
            } else {
               RequestContext.getCurrentInstance().update("form:conceptosRetroDialogo");
               RequestContext.getCurrentInstance().execute("PF('conceptosRetroDialogo').show()");
               tipoActualizacion = tipoNuevo;
            }
         } else {
            nuevoConceptosRetroactivos.getConceptoRetroActivo().setDescripcion(nuevoYduplicarCompletarConceptoRetro);
            log.info("valorConfirmar cuando es vacio: " + valorConfirmar);
            nuevoConceptosRetroactivos.setConceptoRetroActivo(new Conceptos());
            nuevoConceptosRetroactivos.getConceptoRetroActivo().setDescripcion(" ");
            log.info("NUEVO CONCEPTOSRETRO " + nuevoConceptosRetroactivos.getConceptoRetroActivo().getDescripcion());
         }
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevoCargo");
      }

   }

   public void asignarVariableConceptos(int tipoNuevo) {
      if (tipoNuevo == 0) {
         tipoActualizacion = 1;
      }
      if (tipoNuevo == 1) {
         tipoActualizacion = 2;
      }
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:conceptosDialogo");
      RequestContext.getCurrentInstance().execute("PF('conceptosDialogo').show()");
   }

   public void asignarVariableConceptosRetro(int tipoNuevo) {
      if (tipoNuevo == 0) {
         tipoActualizacion = 1;
      }
      if (tipoNuevo == 1) {
         tipoActualizacion = 2;
      }
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:conceptosRetroDialogo");
      RequestContext.getCurrentInstance().execute("PF('conceptosRetroDialogo').show()");
   }

   public void autocompletarDuplicado(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
      log.info("DUPLICAR entrooooooooooooooooooooooooooooooooooooooooooooooooooooooo!!!");
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      if (confirmarCambio.equalsIgnoreCase("CONCEPTOS")) {
         log.info("DUPLICAR valorConfirmar : " + valorConfirmar);
         log.info("DUPLICAR CIUDAD bkp : " + nuevoYduplicarCompletarConcepto);

         if (!duplicarConceptosRetroactivos.getConcepto().getDescripcion().equals("")) {
            log.info("DUPLICAR ENTRO DONDE NO TENIA QUE ENTRAR");
            log.info("DUPLICAR valorConfirmar: " + valorConfirmar);
            log.info("DUPLICAR nuevoTipoCCAutoCompletar: " + nuevoYduplicarCompletarConcepto);
            duplicarConceptosRetroactivos.getConcepto().setDescripcion(nuevoYduplicarCompletarConcepto);
            for (int i = 0; i < lovConceptos.size(); i++) {
               if (lovConceptos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            log.info("Coincidencias: " + coincidencias);
            if (coincidencias == 1) {
               duplicarConceptosRetroactivos.setConcepto(lovConceptos.get(indiceUnicoElemento));
               lovConceptos = null;
            } else {
               RequestContext.getCurrentInstance().update("form:conceptosDialogo");
               RequestContext.getCurrentInstance().execute("PF('conceptosDialogo').show()");
               tipoActualizacion = tipoNuevo;
            }
         } else if (tipoNuevo == 2) {
            //duplicarConceptosRetroactivos.getEmpresa().setDescripcion(nuevoYduplicarCompletarPais);
            log.info("DUPLICAR valorConfirmar cuando es vacio: " + valorConfirmar);
            log.info("DUPLICAR INDEX : " + index);
            duplicarConceptosRetroactivos.setConcepto(new Conceptos());
            duplicarConceptosRetroactivos.getConcepto().setDescripcion(" ");

            log.info("DUPLICAR CONCEPTOS  : " + duplicarConceptosRetroactivos.getConcepto().getDescripcion());
            log.info("nuevoYduplicarCompletarCONCEPTOS : " + nuevoYduplicarCompletarConcepto);
            if (tipoLista == 0) {
               listConceptosRetroactivos.get(index).getConcepto().setDescripcion(nuevoYduplicarCompletarConcepto);
               log.error("tipo lista" + tipoLista);
               log.error("Secuencia Parentesco " + listConceptosRetroactivos.get(index).getConcepto().getSecuencia());
            } else if (tipoLista == 1) {
               filtrarConceptosRetroactivos.get(index).getConcepto().setDescripcion(nuevoYduplicarCompletarConcepto);
            }

         }
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarPersona");
      } else if (confirmarCambio.equalsIgnoreCase("CONCEPTOSRETRO")) {
         log.info("DUPLICAR valorConfirmar : " + valorConfirmar);
         log.info("DUPLICAR CIUDAD bkp : " + nuevoYduplicarCompletarConceptoRetro);

         if (!duplicarConceptosRetroactivos.getConceptoRetroActivo().getDescripcion().equals("")) {
            log.info("DUPLICAR ENTRO DONDE NO TENIA QUE ENTRAR");
            log.info("DUPLICAR valorConfirmar: " + valorConfirmar);
            log.info("DUPLICAR nuevoTipoCCAutoCompletar: " + nuevoYduplicarCompletarConceptoRetro);
            duplicarConceptosRetroactivos.getConceptoRetroActivo().setDescripcion(nuevoYduplicarCompletarConceptoRetro);
            for (int i = 0; i < lovConceptosRetro.size(); i++) {
               if (lovConceptosRetro.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            log.info("Coincidencias: " + coincidencias);
            if (coincidencias == 1) {
               duplicarConceptosRetroactivos.setConceptoRetroActivo(lovConceptosRetro.get(indiceUnicoElemento));
               lovConceptosRetro = null;
               getLovConceptosRetro();
            } else {
               RequestContext.getCurrentInstance().update("form:conceptosRetroDialogo");
               RequestContext.getCurrentInstance().execute("PF('conceptosRetroDialogo').show()");
               tipoActualizacion = tipoNuevo;
            }
         } else if (tipoNuevo == 2) {
            //duplicarConceptosRetroactivos.getEmpresa().setDescripcion(nuevoYduplicarCompletarPais);
            log.info("DUPLICAR valorConfirmar cuando es vacio: " + valorConfirmar);
            log.info("DUPLICAR INDEX : " + index);
            duplicarConceptosRetroactivos.setConceptoRetroActivo(new Conceptos());
            duplicarConceptosRetroactivos.getConceptoRetroActivo().setDescripcion(" ");

            log.info("DUPLICAR CONCEPTOSRETRO  : " + duplicarConceptosRetroactivos.getConceptoRetroActivo().getDescripcion());
            log.info("nuevoYduplicarCompletarCONCEPTOSRETRO : " + nuevoYduplicarCompletarConceptoRetro);
            if (tipoLista == 0) {
               listConceptosRetroactivos.get(index).getConceptoRetroActivo().setDescripcion(nuevoYduplicarCompletarConceptoRetro);
               log.error("tipo lista" + tipoLista);
               log.error("Secuencia Parentesco " + listConceptosRetroactivos.get(index).getConceptoRetroActivo().getSecuencia());
            } else if (tipoLista == 1) {
               filtrarConceptosRetroactivos.get(index).getConceptoRetroActivo().setDescripcion(nuevoYduplicarCompletarConceptoRetro);
            }

         }
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCargo");
      }
   }

   public void revisarDialogoGuardar() {

      if (!borrarConceptosRetroactivos.isEmpty() || !crearConceptosRetroactivos.isEmpty() || !modificarConceptosRetroactivos.isEmpty()) {
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:confirmarGuardar");
         RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
      }

   }

   public void guardarConceptosRetroactivos() {
      RequestContext context = RequestContext.getCurrentInstance();

      if (guardado == false) {
         log.info("Realizando guardarConceptosRetroactivos");
         if (!borrarConceptosRetroactivos.isEmpty()) {
            administrarConceptosRetroactivos.borrarConceptosRetroactivos(borrarConceptosRetroactivos);
            //mostrarBorrados
            registrosBorrados = borrarConceptosRetroactivos.size();
            RequestContext.getCurrentInstance().update("form:mostrarBorrados");
            RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
            borrarConceptosRetroactivos.clear();
         }
         if (!modificarConceptosRetroactivos.isEmpty()) {
            administrarConceptosRetroactivos.modificarConceptosRetroactivos(modificarConceptosRetroactivos);
            modificarConceptosRetroactivos.clear();
         }
         if (!crearConceptosRetroactivos.isEmpty()) {
            administrarConceptosRetroactivos.crearConceptosRetroactivos(crearConceptosRetroactivos);
            crearConceptosRetroactivos.clear();
         }
         log.info("Se guardaron los datos con exito");
         listConceptosRetroactivos = null;
         k = 0;
         guardado = true;
         FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      }
      index = -1;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");

   }

   public void editarCelda() {
      if (index >= 0) {
         if (tipoLista == 0) {
            editarConceptosRetroactivos = listConceptosRetroactivos.get(index);
         }
         if (tipoLista == 1) {
            editarConceptosRetroactivos = filtrarConceptosRetroactivos.get(index);
         }

         RequestContext context = RequestContext.getCurrentInstance();
         log.info("Entro a editar... valor celda: " + cualCelda);
         if (cualCelda == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editConceptos");
            RequestContext.getCurrentInstance().execute("PF('editConceptos').show()");
            cualCelda = -1;
         } else if (cualCelda == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editConceptosRetro");
            RequestContext.getCurrentInstance().execute("PF('editConceptosRetro').show()");
            cualCelda = -1;
         }

      }
      index = -1;
      secRegistro = null;
   }

   public void agregarNuevoConceptosRetroactivos() {
      log.info("agregarNuevoConceptosRetroactivos");
      int contador = 0;
      int duplicados = 0;

      Integer a = 0;
      a = null;
      mensajeValidacion = " ";
      RequestContext context = RequestContext.getCurrentInstance();

      if (nuevoConceptosRetroactivos.getConcepto().getDescripcion() == null) {
         mensajeValidacion = mensajeValidacion + " *Concepto \n";
         log.info("Mensaje validacion : " + mensajeValidacion);

      } else {
         log.info("bandera");
         contador++;//3

      }

      if (nuevoConceptosRetroactivos.getConceptoRetroActivo().getDescripcion() == null) {
         mensajeValidacion = mensajeValidacion + " *Concepto Retro \n";
         log.info("Mensaje validacion : " + mensajeValidacion);

      } else {
         log.info("bandera");
         contador++;//4

      }

      log.info("contador " + contador);

      if (contador == 4) {
         if (bandera == 1) {
            //CERRAR FILTRADO
            FacesContext c = FacesContext.getCurrentInstance();
            log.info("Desactivar");
            codigo = (Column) c.getViewRoot().findComponent("form:datosConceptosRetroactivos:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosConceptosRetroactivos:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            personafir = (Column) c.getViewRoot().findComponent("form:datosConceptosRetroactivos:personafir");
            personafir.setFilterStyle("display: none; visibility: hidden;");
            cargo = (Column) c.getViewRoot().findComponent("form:datosConceptosRetroactivos:cargo");
            cargo.setFilterStyle("display: none; visibility: hidden;");
            bandera = 0;
            filtrarConceptosRetroactivos = null;
            tipoLista = 0;
         }
         log.info("Despues de la bandera");

         k++;
         l = BigInteger.valueOf(k);
         nuevoConceptosRetroactivos.setSecuencia(l);

         crearConceptosRetroactivos.add(nuevoConceptosRetroactivos);

         listConceptosRetroactivos.add(nuevoConceptosRetroactivos);
         nuevoConceptosRetroactivos = new ConceptosRetroactivos();
         nuevoConceptosRetroactivos.setConceptoRetroActivo(new Conceptos());
         nuevoConceptosRetroactivos.setConcepto(new Conceptos());
         RequestContext.getCurrentInstance().update("form:datosConceptosRetroactivos");
         infoRegistro = "Cantidad de registros: " + listConceptosRetroactivos.size();
         RequestContext.getCurrentInstance().update("form:informacionRegistro");
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }

         RequestContext.getCurrentInstance().execute("PF('nuevoRegistroConceptosRetroactivos').hide()");
         RequestContext.getCurrentInstance().update("nuevoRegistroConceptosRetroactivos').hide()");
         index = -1;
         secRegistro = null;

      } else {
         RequestContext.getCurrentInstance().update("form:validacionNuevaCentroCosto");
         RequestContext.getCurrentInstance().execute("PF('validacionNuevaCentroCosto').show()");
         contador = 0;
      }
   }

   public void limpiarNuevoConceptosRetroactivos() {
      log.info("limpiarNuevoConceptosRetroactivos");
      nuevoConceptosRetroactivos = new ConceptosRetroactivos();
      nuevoConceptosRetroactivos.setConcepto(new Conceptos());
      nuevoConceptosRetroactivos.setConceptoRetroActivo(new Conceptos());
      secRegistro = null;
      index = -1;

   }

   //------------------------------------------------------------------------------
   public void cargarNuevoConceptosRetroactivos() {
      log.info("cargarNuevoConceptosRetroactivos");

      duplicarConceptosRetroactivos = new ConceptosRetroactivos();
      duplicarConceptosRetroactivos.setConcepto(new Conceptos());
      duplicarConceptosRetroactivos.setConceptoRetroActivo(new Conceptos());
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().execute("PF('nuevoRegistroConceptosRetroactivos').show()");
      RequestContext.getCurrentInstance().update("formularioDialogos:nuevaTipoempresa");

   }

   public void duplicandoConceptosRetroactivos() {
      log.info("duplicandoConceptosRetroactivos");
      if (index >= 0) {
         duplicarConceptosRetroactivos = new ConceptosRetroactivos();
         duplicarConceptosRetroactivos.setConcepto(new Conceptos());
         duplicarConceptosRetroactivos.setConceptoRetroActivo(new Conceptos());
         k++;
         l = BigInteger.valueOf(k);

         if (tipoLista == 0) {
            duplicarConceptosRetroactivos.setSecuencia(l);
            duplicarConceptosRetroactivos.setConcepto(listConceptosRetroactivos.get(index).getConcepto());
            duplicarConceptosRetroactivos.setConceptoRetroActivo(listConceptosRetroactivos.get(index).getConceptoRetroActivo());
         }
         if (tipoLista == 1) {
            duplicarConceptosRetroactivos.setSecuencia(l);
            duplicarConceptosRetroactivos.setConcepto(filtrarConceptosRetroactivos.get(index).getConcepto());
            duplicarConceptosRetroactivos.setConceptoRetroActivo(filtrarConceptosRetroactivos.get(index).getConceptoRetroActivo());

         }

         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTE");
         RequestContext.getCurrentInstance().execute("PF('duplicarRegistroConceptosRetroactivos').show()");
         index = -1;
         secRegistro = null;
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

      if (duplicarConceptosRetroactivos.getConcepto().getDescripcion() == null) {
         mensajeValidacion = mensajeValidacion + "   *Concepto \n";
         log.info("Mensaje validacion : " + mensajeValidacion);

      } else {
         log.info("Bandera : ");
         contador++;
      }
      if (duplicarConceptosRetroactivos.getConceptoRetroActivo().getDescripcion() == null) {
         mensajeValidacion = mensajeValidacion + "   *Conceptro Retro \n";
         log.info("Mensaje validacion : " + mensajeValidacion);

      } else {
         log.info("Bandera : ");
         contador++;
      }

      if (contador == 4) {

         if (crearConceptosRetroactivos.contains(duplicarConceptosRetroactivos)) {
            log.info("Ya lo contengo.");
         }
         listConceptosRetroactivos.add(duplicarConceptosRetroactivos);
         crearConceptosRetroactivos.add(duplicarConceptosRetroactivos);
         RequestContext.getCurrentInstance().update("form:datosConceptosRetroactivos");
         index = -1;
         log.info("--------------DUPLICAR------------------------");

         log.info("CONCEPTOS : " + duplicarConceptosRetroactivos.getConcepto().getDescripcion());
         log.info("CONCEPTOSRETRO : " + duplicarConceptosRetroactivos.getConceptoRetroActivo().getDescripcion());
         log.info("--------------DUPLICAR------------------------");

         secRegistro = null;
         if (guardado == true) {
            guardado = false;
         }
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         infoRegistro = "Cantidad de registros: " + listConceptosRetroactivos.size();
         RequestContext.getCurrentInstance().update("form:informacionRegistro");
         if (bandera == 1) {
            //CERRAR FILTRADO
            FacesContext c = FacesContext.getCurrentInstance();
            codigo = (Column) c.getViewRoot().findComponent("form:datosConceptosRetroactivos:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosConceptosRetroactivos:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            personafir = (Column) c.getViewRoot().findComponent("form:datosConceptosRetroactivos:personafir");
            personafir.setFilterStyle("display: none; visibility: hidden;");
            cargo = (Column) c.getViewRoot().findComponent("form:datosConceptosRetroactivos:cargo");
            cargo.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosConceptosRetroactivos");
            bandera = 0;
            filtrarConceptosRetroactivos = null;
            tipoLista = 0;
         }
         duplicarConceptosRetroactivos = new ConceptosRetroactivos();
         duplicarConceptosRetroactivos.setConceptoRetroActivo(new Conceptos());
         duplicarConceptosRetroactivos.setConcepto(new Conceptos());

         RequestContext.getCurrentInstance().execute("duplicarRegistroConceptosRetroactivos').hide()");
         RequestContext.getCurrentInstance().update("duplicarRegistroConceptosRetroactivos').hide()");

      } else {
         contador = 0;
         RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
         RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
      }
   }

   public void limpiarDuplicarConceptosRetroactivos() {
      duplicarConceptosRetroactivos = new ConceptosRetroactivos();
      duplicarConceptosRetroactivos.setConcepto(new Conceptos());
      duplicarConceptosRetroactivos.setConceptoRetroActivo(new Conceptos());
   }

   public void exportPDF() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosConceptosRetroactivosExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "CONCEPTOSRETROACTIVOS", false, false, "UTF-8", null, null);
      context.responseComplete();
      index = -1;
      secRegistro = null;
   }

   public void exportXLS() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosConceptosRetroactivosExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "CONCEPTOSRETROACTIVOS", false, false, "UTF-8", null, null);
      context.responseComplete();
      index = -1;
      secRegistro = null;
   }

   public void verificarRastro() {
      RequestContext context = RequestContext.getCurrentInstance();
      log.info("lol");
      if (!listConceptosRetroactivos.isEmpty()) {
         if (secRegistro != null) {
            log.info("lol 2");
            int resultado = administrarRastros.obtenerTabla(secRegistro, "CONCEPTOSRETROACTIVOS"); //En ENCARGATURAS lo cambia por el nombre de su tabla
            log.info("resultado: " + resultado);
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
         } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
         }
      } else if (administrarRastros.verificarHistoricosTabla("CONCEPTOSRETROACTIVOS")) { // igual acá
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
      index = -1;
   }

   //*/*/*/*/*/*/*/*/*/*-/-*//-*/-*/*/*-*/-*/-*/*/*/*/*/---/*/*/*/*/-*/-*/-*/-*/-*/
   public List<ConceptosRetroactivos> getListConceptosRetroactivos() {
      if (listConceptosRetroactivos == null) {
         listConceptosRetroactivos = administrarConceptosRetroactivos.consultarConceptosRetroactivos();
      }
      RequestContext context = RequestContext.getCurrentInstance();
      if (listConceptosRetroactivos == null || listConceptosRetroactivos.isEmpty()) {
         infoRegistro = "Cantidad de registros: 0 ";
      } else {
         infoRegistro = "Cantidad de registros: " + listConceptosRetroactivos.size();
      }
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
      return listConceptosRetroactivos;
   }

   public void setListConceptosRetroactivos(List<ConceptosRetroactivos> listConceptosRetroactivos) {
      this.listConceptosRetroactivos = listConceptosRetroactivos;
   }

   public List<ConceptosRetroactivos> getFiltrarConceptosRetroactivos() {
      return filtrarConceptosRetroactivos;
   }

   public void setFiltrarConceptosRetroactivos(List<ConceptosRetroactivos> filtrarConceptosRetroactivos) {
      this.filtrarConceptosRetroactivos = filtrarConceptosRetroactivos;
   }

   public ConceptosRetroactivos getNuevoConceptosRetroactivos() {
      return nuevoConceptosRetroactivos;
   }

   public void setNuevoConceptosRetroactivos(ConceptosRetroactivos nuevoConceptosRetroactivos) {
      this.nuevoConceptosRetroactivos = nuevoConceptosRetroactivos;
   }

   public ConceptosRetroactivos getDuplicarConceptosRetroactivos() {
      return duplicarConceptosRetroactivos;
   }

   public void setDuplicarConceptosRetroactivos(ConceptosRetroactivos duplicarConceptosRetroactivos) {
      this.duplicarConceptosRetroactivos = duplicarConceptosRetroactivos;
   }

   public ConceptosRetroactivos getEditarConceptosRetroactivos() {
      return editarConceptosRetroactivos;
   }

   public void setEditarConceptosRetroactivos(ConceptosRetroactivos editarConceptosRetroactivos) {
      this.editarConceptosRetroactivos = editarConceptosRetroactivos;
   }

   public BigInteger getSecRegistro() {
      return secRegistro;
   }

   public void setSecRegistro(BigInteger secRegistro) {
      this.secRegistro = secRegistro;
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
   private String infoRegistroConceptos;

   public List<Conceptos> getLovConceptos() {
      if (lovConceptos == null) {
         lovConceptos = administrarConceptosRetroactivos.consultarLOVConceptos();
      }

      RequestContext context = RequestContext.getCurrentInstance();
      if (lovConceptos == null || lovConceptos.isEmpty()) {
         infoRegistroConceptos = "Cantidad de registros: 0 ";
      } else {
         infoRegistroConceptos = "Cantidad de registros: " + lovConceptos.size();
      }
      RequestContext.getCurrentInstance().update("form:infoRegistroConceptos");
      return lovConceptos;
   }

   public void setLovConceptos(List<Conceptos> lovConceptos) {
      this.lovConceptos = lovConceptos;
   }

   public List<Conceptos> getFiltradoConceptos() {
      return filtradoConceptos;
   }

   public void setFiltradoConceptos(List<Conceptos> filtradoConceptos) {
      this.filtradoConceptos = filtradoConceptos;
   }

   public Conceptos getConceptoSeleccionado() {
      return conceptoSeleccionado;
   }

   public void setConceptoSeleccionado(Conceptos bancoSeleccionado) {
      this.conceptoSeleccionado = bancoSeleccionado;
   }
   private String infoRegistroConceptosRetro;

   public List<Conceptos> getLovConceptosRetro() {
      if (lovConceptosRetro == null) {
         lovConceptosRetro = administrarConceptosRetroactivos.consultarLOVConceptosRetro();
      }

      RequestContext context = RequestContext.getCurrentInstance();
      if (lovConceptosRetro == null || lovConceptosRetro.isEmpty()) {
         infoRegistroConceptosRetro = "Cantidad de registros: 0 ";
      } else {
         infoRegistroConceptosRetro = "Cantidad de registros: " + lovConceptosRetro.size();
      }
      RequestContext.getCurrentInstance().update("form:infoRegistroConceptosRetro");
      return lovConceptosRetro;
   }

   public void setLovConceptosRetro(List<Conceptos> lovConceptosRetro) {
      this.lovConceptosRetro = lovConceptosRetro;
   }

   public List<Conceptos> getFiltradoConceptosRetro() {
      return filtradoConceptosRetro;
   }

   public void setFiltradoConceptosRetro(List<Conceptos> filtradoConceptosRetro) {
      this.filtradoConceptosRetro = filtradoConceptosRetro;
   }

   public Conceptos getConceptoRetroActivoSeleccionado() {
      return conceptoRetroSeleccionado;
   }

   public void setConceptoRetroActivoSeleccionado(Conceptos cargoSeleccionado) {
      this.conceptoRetroSeleccionado = cargoSeleccionado;
   }

   public ConceptosRetroactivos getSucursalSeleccionada() {
      return sucursalSeleccionada;
   }

   public void setSucursalSeleccionada(ConceptosRetroactivos sucursalSeleccionada) {
      this.sucursalSeleccionada = sucursalSeleccionada;
   }

   public String getInfoRegistro() {
      return infoRegistro;
   }

   public void setInfoRegistro(String infoRegistro) {
      this.infoRegistro = infoRegistro;
   }

   public String getInfoRegistroConceptos() {
      return infoRegistroConceptos;
   }

   public void setInfoRegistroConceptos(String infoRegistroConceptos) {
      this.infoRegistroConceptos = infoRegistroConceptos;
   }

   public String getInfoRegistroConceptosRetro() {
      return infoRegistroConceptosRetro;
   }

   public void setInfoRegistroConceptosRetro(String infoRegistroConceptosRetro) {
      this.infoRegistroConceptosRetro = infoRegistroConceptosRetro;
   }

   public boolean isAceptar() {
      return aceptar;
   }

   public void setAceptar(boolean aceptar) {
      this.aceptar = aceptar;
   }

}
