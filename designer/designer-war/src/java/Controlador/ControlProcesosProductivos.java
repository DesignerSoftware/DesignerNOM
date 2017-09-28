/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.ProcesosProductivos;
import Entidades.CentrosCostos;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarProcesosProductivosInterface;
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
import ControlNavegacion.ListasRecurrentes;
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
public class ControlProcesosProductivos implements Serializable {

   private static Logger log = Logger.getLogger(ControlProcesosProductivos.class);

   @EJB
   AdministrarProcesosProductivosInterface administrarProcesosProductivos;
   @EJB
   AdministrarRastrosInterface administrarRastros;
   private List<ProcesosProductivos> listProcesosProductivos;
   private List<ProcesosProductivos> filtrarProcesosProductivos;
   private List<ProcesosProductivos> crearProcesosProductivos;
   private List<ProcesosProductivos> modificarProcesosProductivos;
   private List<ProcesosProductivos> borrarProcesosProductivos;
   private ProcesosProductivos nuevoProcesosProductivos;
   private ProcesosProductivos duplicarProcesosProductivos;
   private ProcesosProductivos editarProcesosProductivos;
   private ProcesosProductivos procesoProductivoSeleccionado;
   //otros
   private int cualCelda, tipoLista, index, tipoActualizacion, k, bandera;
   private BigInteger l;
   private boolean aceptar, guardado;
   //AutoCompletar
   private boolean permitirIndex;
   //RASTRO
   private BigInteger secRegistro;
   private Column codigo, descripcion, personafir;
   //borrado
   private int registrosBorrados;
   private String mensajeValidacion;
   //filtrado table
   private int tamano;
   private Integer backupCodigo;
   private String backupDescripcion;
   private String backupPais;
   //---------------------------------
   private String backupCiudad;
   private List<CentrosCostos> listaCentrosCostos;
   private List<CentrosCostos> filtradoCentrosCostos;
   private CentrosCostos centrocostoSeleccionado;
   private String nuevoYduplicarCompletarPersona;
   //--------------------------------------
   private String backupBanco;
   private String infoRegistro;
   private String infoRegistroCentroCostos;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();
   private ListasRecurrentes listasRecurrentes;

   public ControlProcesosProductivos() {
      FacesContext fc = FacesContext.getCurrentInstance();
      ControlListaNavegacion controlListaNavegacion = (ControlListaNavegacion) fc.getApplication().evaluateExpressionGet(fc, "#{controlListaNavegacion}", ControlListaNavegacion.class);
      listasRecurrentes = controlListaNavegacion.getListasRecurrentes();
      listProcesosProductivos = null;
      crearProcesosProductivos = new ArrayList<ProcesosProductivos>();
      modificarProcesosProductivos = new ArrayList<ProcesosProductivos>();
      borrarProcesosProductivos = new ArrayList<ProcesosProductivos>();
      permitirIndex = true;
      editarProcesosProductivos = new ProcesosProductivos();
      nuevoProcesosProductivos = new ProcesosProductivos();
      nuevoProcesosProductivos.setCentrocosto(new CentrosCostos());
      duplicarProcesosProductivos = new ProcesosProductivos();
      duplicarProcesosProductivos.setCentrocosto(new CentrosCostos());
      listaCentrosCostos = null;
      filtradoCentrosCostos = null;
      guardado = true;
      tamano = 270;
      aceptar = true;
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
         administrarProcesosProductivos.obtenerConexion(ses.getId());
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
      String pagActual = "procesoproductivo";
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
         log.info("\n ENTRE A ControlProcesosProductivos.eventoFiltrar \n");
         if (tipoLista == 0) {
            tipoLista = 1;
         }
         RequestContext context = RequestContext.getCurrentInstance();
         infoRegistro = "Cantidad de registros: " + filtrarProcesosProductivos.size();
         RequestContext.getCurrentInstance().update("form:informacionRegistro");
      } catch (Exception e) {
         log.warn("Error ControlProcesosProductivos eventoFiltrar ERROR===" + e.getMessage());
      }
   }

   public void cambiarIndice(int indice, int celda) {
      log.error("TIPO LISTA = " + tipoLista);

      if (permitirIndex == true) {
         index = indice;
         cualCelda = celda;
         secRegistro = listProcesosProductivos.get(index).getSecuencia();
         if (tipoLista == 0) {
            if (cualCelda == 0) {
               backupCodigo = listProcesosProductivos.get(index).getCodigo();
            }
            if (cualCelda == 1) {
               backupDescripcion = listProcesosProductivos.get(index).getDescripcion();
               log.info("DESCRIPCION : " + backupDescripcion);
            }
            if (cualCelda == 2) {
               backupBanco = listProcesosProductivos.get(index).getCentrocosto().getNombre();
               log.info("BANCO : " + backupBanco);
            }

         } else if (tipoLista == 1) {
            if (cualCelda == 0) {
               backupCodigo = filtrarProcesosProductivos.get(index).getCodigo();
            }
            if (cualCelda == 1) {
               backupDescripcion = filtrarProcesosProductivos.get(index).getDescripcion();
               log.info("DESCRIPCION : " + backupDescripcion);
            }
            if (cualCelda == 2) {
               backupBanco = filtrarProcesosProductivos.get(index).getCentrocosto().getNombre();
               log.info("BANCO : " + backupBanco);
            }

         }

      }
      log.info("Indice: " + index + " Celda: " + cualCelda);
   }

   public void asignarIndex(Integer indice, int LND, int dig) {
      try {
         RequestContext context = RequestContext.getCurrentInstance();
         log.info("\n ENTRE A ControlProcesosProductivos.asignarIndex \n");
         index = indice;
         if (LND == 0) {
            tipoActualizacion = 0;
         } else if (LND == 1) {
            tipoActualizacion = 1;
            log.info("Tipo Actualizacion: " + tipoActualizacion);
         } else if (LND == 2) {
            tipoActualizacion = 2;
         }
         if (dig == 2) {
            RequestContext.getCurrentInstance().update("form:personasDialogo");
            RequestContext.getCurrentInstance().execute("PF('personasDialogo').show()");
            dig = -1;
         }
      } catch (Exception e) {
         log.warn("Error ControlProcesosProductivos.asignarIndex ERROR======" + e.getMessage());
      }
   }

   public void activarAceptar() {
      aceptar = false;
   }

   public void listaValoresBoton() {
      if (index >= 0) {

         if (cualCelda == 2) {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:personasDialogo");
            RequestContext.getCurrentInstance().execute("PF('personasDialogo').show()");
         }
      }
   }

   public void cancelarModificacion() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 1) {
         //CERRAR FILTRADO
         codigo = (Column) c.getViewRoot().findComponent("form:datosProcesosProductivos:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosProcesosProductivos:descripcion");
         descripcion.setFilterStyle("display: none; visibility: hidden;");
         personafir = (Column) c.getViewRoot().findComponent("form:datosProcesosProductivos:personafir");
         personafir.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosProcesosProductivos");
         bandera = 0;
         filtrarProcesosProductivos = null;
         tipoLista = 0;
      }

      borrarProcesosProductivos.clear();
      crearProcesosProductivos.clear();
      modificarProcesosProductivos.clear();
      index = -1;
      secRegistro = null;
      k = 0;
      listProcesosProductivos = null;
      guardado = true;
      permitirIndex = true;
      getListProcesosProductivos();
      RequestContext context = RequestContext.getCurrentInstance();
      if (listProcesosProductivos == null || listProcesosProductivos.isEmpty()) {
         infoRegistro = "Cantidad de registros: 0 ";
      } else {
         infoRegistro = "Cantidad de registros: " + listProcesosProductivos.size();
      }
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
      RequestContext.getCurrentInstance().update("form:datosProcesosProductivos");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void salir() {
      limpiarListasValor();
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 1) {
         //CERRAR FILTRADO
         codigo = (Column) c.getViewRoot().findComponent("form:datosProcesosProductivos:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosProcesosProductivos:descripcion");
         descripcion.setFilterStyle("display: none; visibility: hidden;");
         personafir = (Column) c.getViewRoot().findComponent("form:datosProcesosProductivos:personafir");
         personafir.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosProcesosProductivos");
         bandera = 0;
         filtrarProcesosProductivos = null;
         tipoLista = 0;
      }

      borrarProcesosProductivos.clear();
      crearProcesosProductivos.clear();
      modificarProcesosProductivos.clear();
      index = -1;
      secRegistro = null;
      k = 0;
      listProcesosProductivos = null;
      guardado = true;
      permitirIndex = true;
      getListProcesosProductivos();
      RequestContext context = RequestContext.getCurrentInstance();
      if (listProcesosProductivos == null || listProcesosProductivos.isEmpty()) {
         infoRegistro = "Cantidad de registros: 0 ";
      } else {
         infoRegistro = "Cantidad de registros: " + listProcesosProductivos.size();
      }
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
      RequestContext.getCurrentInstance().update("form:datosProcesosProductivos");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void activarCtrlF11() {
      FacesContext c = FacesContext.getCurrentInstance();

      if (bandera == 0) {
         tamano = 250;
         codigo = (Column) c.getViewRoot().findComponent("form:datosProcesosProductivos:codigo");
         codigo.setFilterStyle("width: 85% !important;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosProcesosProductivos:descripcion");
         descripcion.setFilterStyle("width: 85% !important;");
         personafir = (Column) c.getViewRoot().findComponent("form:datosProcesosProductivos:personafir");
         personafir.setFilterStyle("width: 85% !important;");
         RequestContext.getCurrentInstance().update("form:datosProcesosProductivos");
         log.info("Activar");
         bandera = 1;
      } else if (bandera == 1) {
         log.info("Desactivar");
         tamano = 270;
         codigo = (Column) c.getViewRoot().findComponent("form:datosProcesosProductivos:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosProcesosProductivos:descripcion");
         descripcion.setFilterStyle("display: none; visibility: hidden;");
         personafir = (Column) c.getViewRoot().findComponent("form:datosProcesosProductivos:personafir");
         personafir.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosProcesosProductivos");
         bandera = 0;
         filtrarProcesosProductivos = null;
         tipoLista = 0;
      }
   }

   public void actualizarCentrosCostos() {
      RequestContext context = RequestContext.getCurrentInstance();
      log.info("centrocosto seleccionado : " + centrocostoSeleccionado.getNombre());
      log.info("tipo Actualizacion : " + tipoActualizacion);
      log.info("tipo Lista : " + tipoLista);

      if (tipoActualizacion == 0) {
         if (tipoLista == 0) {
            listProcesosProductivos.get(index).setCentrocosto(centrocostoSeleccionado);

            if (!crearProcesosProductivos.contains(listProcesosProductivos.get(index))) {
               if (modificarProcesosProductivos.isEmpty()) {
                  modificarProcesosProductivos.add(listProcesosProductivos.get(index));
               } else if (!modificarProcesosProductivos.contains(listProcesosProductivos.get(index))) {
                  modificarProcesosProductivos.add(listProcesosProductivos.get(index));
               }
            }
         } else {
            filtrarProcesosProductivos.get(index).setCentrocosto(centrocostoSeleccionado);

            if (!crearProcesosProductivos.contains(filtrarProcesosProductivos.get(index))) {
               if (modificarProcesosProductivos.isEmpty()) {
                  modificarProcesosProductivos.add(filtrarProcesosProductivos.get(index));
               } else if (!modificarProcesosProductivos.contains(filtrarProcesosProductivos.get(index))) {
                  modificarProcesosProductivos.add(filtrarProcesosProductivos.get(index));
               }
            }
         }
         if (guardado == true) {
            guardado = false;
         }
         permitirIndex = true;
         log.info("ACTUALIZAR PAIS PAIS SELECCIONADO : " + centrocostoSeleccionado.getNombre());
         RequestContext.getCurrentInstance().update("form:datosProcesosProductivos");
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      } else if (tipoActualizacion == 1) {
         log.info("ACTUALIZAR PAIS NUEVO DEPARTAMENTO: " + centrocostoSeleccionado.getNombre());
         nuevoProcesosProductivos.setCentrocosto(centrocostoSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevoPersona");
      } else if (tipoActualizacion == 2) {
         log.info("ACTUALIZAR PAIS DUPLICAR DEPARTAMENO: " + centrocostoSeleccionado.getNombre());
         duplicarProcesosProductivos.setCentrocosto(centrocostoSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarPersona");
      }
      filtradoCentrosCostos = null;
      centrocostoSeleccionado = null;
      aceptar = true;
      index = -1;
      secRegistro = null;
      tipoActualizacion = -1;
      cualCelda = -1;
      context.reset("form:lovCentrosCostos:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovCentrosCostos').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('personasDialogo').hide()");
      //RequestContext.getCurrentInstance().update("form:lovCentrosCostos");
      //RequestContext.getCurrentInstance().update("form:datosHvEntrevista");
   }

   public void cancelarCambioCentrosCostos() {
      listProcesosProductivos.get(index).getCentrocosto().setNombre(backupBanco);
      filtradoCentrosCostos = null;
      centrocostoSeleccionado = null;
      aceptar = true;
      index = -1;
      secRegistro = null;
      tipoActualizacion = -1;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("form:lovCentrosCostos:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovCentrosCostos').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('personasDialogo').hide()");
   }

   public void modificarProcesosProductivos(int indice, String confirmarCambio, String valorConfirmar) {
      log.error("ENTRE A MODIFICAR SUB CATEGORIA");
      index = indice;
      int coincidencias = 0;
      int contador = 0;
      boolean banderita = false;
      boolean banderita1 = false;
      boolean banderita2 = false;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      log.error("TIPO LISTA = " + tipoLista);
      if (confirmarCambio.equalsIgnoreCase("N")) {
         log.error("ENTRE A MODIFICAR EMPRESAS, CONFIRMAR CAMBIO ES N");
         if (tipoLista == 0) {
            if (!crearProcesosProductivos.contains(listProcesosProductivos.get(indice))) {

               log.info("backupCodigo : " + backupCodigo);
               log.info("backupDescripcion : " + backupDescripcion);

               if (listProcesosProductivos.get(indice).getCodigo() == null) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  banderita = false;
                  listProcesosProductivos.get(indice).setCodigo(backupCodigo);

               } else {
                  for (int j = 0; j < listProcesosProductivos.size(); j++) {
                     if (j != indice) {
                        if (listProcesosProductivos.get(indice).getCodigo() == listProcesosProductivos.get(j).getCodigo()) {
                           contador++;
                        }
                     }
                  }

                  if (contador > 0) {
                     mensajeValidacion = "CODIGOS REPETIDOS";
                     banderita = false;
                     listProcesosProductivos.get(indice).setCodigo(backupCodigo);
                  } else {
                     banderita = true;
                  }

               }
               if (listProcesosProductivos.get(indice).getDescripcion().isEmpty()) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  banderita1 = false;
                  listProcesosProductivos.get(indice).setDescripcion(backupDescripcion);
               } else if (listProcesosProductivos.get(indice).getDescripcion().equals(" ")) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  banderita1 = false;
                  listProcesosProductivos.get(indice).setDescripcion(backupDescripcion);

               } else {
                  banderita1 = true;
               }

               if (banderita == true && banderita1 == true) {
                  if (modificarProcesosProductivos.isEmpty()) {
                     modificarProcesosProductivos.add(listProcesosProductivos.get(indice));
                  } else if (!modificarProcesosProductivos.contains(listProcesosProductivos.get(indice))) {
                     modificarProcesosProductivos.add(listProcesosProductivos.get(indice));
                  }
                  if (guardado == true) {
                     guardado = false;
                  }

               } else {
                  RequestContext.getCurrentInstance().update("form:validacionModificar");
                  RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");

               }
               index = -1;
               secRegistro = null;
               RequestContext.getCurrentInstance().update("form:datosProcesosProductivos");
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            } else {

               log.info("backupCodigo : " + backupCodigo);
               log.info("backupDescripcion : " + backupDescripcion);

               if (listProcesosProductivos.get(indice).getCodigo() == null) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  banderita = false;
                  listProcesosProductivos.get(indice).setCodigo(backupCodigo);
               } else {
                  for (int j = 0; j < listProcesosProductivos.size(); j++) {
                     if (j != indice) {
                        if (listProcesosProductivos.get(indice).getCodigo() == listProcesosProductivos.get(j).getCodigo()) {
                           contador++;
                        }
                     }
                  }

                  if (contador > 0) {
                     mensajeValidacion = "CODIGOS REPETIDOS";
                     banderita = false;
                     listProcesosProductivos.get(indice).setCodigo(backupCodigo);
                  } else {
                     banderita = true;
                  }

               }
               if (listProcesosProductivos.get(indice).getDescripcion().isEmpty()) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  banderita1 = false;
                  listProcesosProductivos.get(indice).setDescripcion(backupDescripcion);
               } else if (listProcesosProductivos.get(indice).getDescripcion().equals(" ")) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  banderita1 = false;
                  listProcesosProductivos.get(indice).setDescripcion(backupDescripcion);

               } else {
                  banderita1 = true;
               }

               if (banderita == true && banderita1 == true) {
                  if (guardado == true) {
                     guardado = false;
                  }
               } else {
                  RequestContext.getCurrentInstance().update("form:validacionModificar");
                  RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");

               }
               index = -1;
               secRegistro = null;
               RequestContext.getCurrentInstance().update("form:datosProcesosProductivos");
               RequestContext.getCurrentInstance().update("form:ACEPTAR");

            }
         } else if (!crearProcesosProductivos.contains(filtrarProcesosProductivos.get(indice))) {
            if (filtrarProcesosProductivos.get(indice).getCodigo() == null) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               banderita = false;
               filtrarProcesosProductivos.get(indice).setCodigo(backupCodigo);
            } else {
               for (int j = 0; j < filtrarProcesosProductivos.size(); j++) {
                  if (j != indice) {
                     if (filtrarProcesosProductivos.get(indice).getCodigo() == listProcesosProductivos.get(j).getCodigo()) {
                        contador++;
                     }
                  }
               }
               for (int j = 0; j < listProcesosProductivos.size(); j++) {
                  if (j != indice) {
                     if (filtrarProcesosProductivos.get(indice).getCodigo() == listProcesosProductivos.get(j).getCodigo()) {
                        contador++;
                     }
                  }
               }
               if (contador > 0) {
                  mensajeValidacion = "CODIGOS REPETIDOS";
                  banderita = false;
                  filtrarProcesosProductivos.get(indice).setCodigo(backupCodigo);

               } else {
                  banderita = true;
               }

            }

            if (filtrarProcesosProductivos.get(indice).getDescripcion().isEmpty()) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               banderita1 = false;
               filtrarProcesosProductivos.get(indice).setDescripcion(backupDescripcion);
            }
            if (filtrarProcesosProductivos.get(indice).getDescripcion().equals(" ")) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               banderita1 = false;
               filtrarProcesosProductivos.get(indice).setDescripcion(backupDescripcion);
            }

            if (banderita == true && banderita1 == true) {
               if (modificarProcesosProductivos.isEmpty()) {
                  modificarProcesosProductivos.add(filtrarProcesosProductivos.get(indice));
               } else if (!modificarProcesosProductivos.contains(filtrarProcesosProductivos.get(indice))) {
                  modificarProcesosProductivos.add(filtrarProcesosProductivos.get(indice));
               }
               if (guardado == true) {
                  guardado = false;
               }

            } else {
               RequestContext.getCurrentInstance().update("form:validacionModificar");
               RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");
            }
            index = -1;
            secRegistro = null;
         } else {
            if (filtrarProcesosProductivos.get(indice).getCodigo() == null) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               banderita = false;
               filtrarProcesosProductivos.get(indice).setCodigo(backupCodigo);
            } else {
               for (int j = 0; j < filtrarProcesosProductivos.size(); j++) {
                  if (j != indice) {
                     if (filtrarProcesosProductivos.get(indice).getCodigo() == listProcesosProductivos.get(j).getCodigo()) {
                        contador++;
                     }
                  }
               }
               for (int j = 0; j < listProcesosProductivos.size(); j++) {
                  if (j != indice) {
                     if (filtrarProcesosProductivos.get(indice).getCodigo() == listProcesosProductivos.get(j).getCodigo()) {
                        contador++;
                     }
                  }
               }
               if (contador > 0) {
                  mensajeValidacion = "CODIGOS REPETIDOS";
                  banderita = false;
                  filtrarProcesosProductivos.get(indice).setCodigo(backupCodigo);

               } else {
                  banderita = true;
               }

            }

            if (filtrarProcesosProductivos.get(indice).getDescripcion().isEmpty()) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               banderita1 = false;
               filtrarProcesosProductivos.get(indice).setDescripcion(backupDescripcion);
            }
            if (filtrarProcesosProductivos.get(indice).getDescripcion().equals(" ")) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               banderita1 = false;
               filtrarProcesosProductivos.get(indice).setDescripcion(backupDescripcion);
            }

            if (banderita == true && banderita1 == true) {
               if (guardado == true) {
                  guardado = false;
               }

            } else {
               RequestContext.getCurrentInstance().update("form:validacionModificar");
               RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");
            }
            index = -1;
            secRegistro = null;
         }
         RequestContext.getCurrentInstance().update("form:datosProcesosProductivos");
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      } else if (confirmarCambio.equalsIgnoreCase("PERSONAS")) {
         log.info("MODIFICANDO NORMA LABORAL : " + listProcesosProductivos.get(indice).getCentrocosto().getNombre());
         if (!listProcesosProductivos.get(indice).getCentrocosto().getNombre().equals("")) {
            if (tipoLista == 0) {
               listProcesosProductivos.get(indice).getCentrocosto().setNombre(backupBanco);
            } else {
               listProcesosProductivos.get(indice).getCentrocosto().setNombre(backupBanco);
            }

            for (int i = 0; i < listaCentrosCostos.size(); i++) {
               if (listaCentrosCostos.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }

            if (coincidencias == 1) {
               if (tipoLista == 0) {
                  listProcesosProductivos.get(indice).setCentrocosto(listaCentrosCostos.get(indiceUnicoElemento));
               } else {
                  filtrarProcesosProductivos.get(indice).setCentrocosto(listaCentrosCostos.get(indiceUnicoElemento));
               }
               listaCentrosCostos.clear();
               listaCentrosCostos = null;
               //getListaTiposFamiliares();

            } else {
               permitirIndex = false;
               RequestContext.getCurrentInstance().update("form:personasDialogo");
               RequestContext.getCurrentInstance().execute("PF('personasDialogo').show()");
               tipoActualizacion = 0;
            }
         } else {
            if (backupBanco != null) {
               if (tipoLista == 0) {
                  listProcesosProductivos.get(index).getCentrocosto().setNombre(backupBanco);
               } else {
                  filtrarProcesosProductivos.get(index).getCentrocosto().setNombre(backupBanco);
               }
            }
            tipoActualizacion = 0;
            log.info("PAIS ANTES DE MOSTRAR DIALOGO PERSONA : " + backupBanco);
            RequestContext.getCurrentInstance().update("form:personasDialogo");
            RequestContext.getCurrentInstance().execute("PF('personasDialogo').show()");
         }

         if (coincidencias == 1) {
            if (tipoLista == 0) {
               if (!crearProcesosProductivos.contains(listProcesosProductivos.get(indice))) {

                  if (modificarProcesosProductivos.isEmpty()) {
                     modificarProcesosProductivos.add(listProcesosProductivos.get(indice));
                  } else if (!modificarProcesosProductivos.contains(listProcesosProductivos.get(indice))) {
                     modificarProcesosProductivos.add(listProcesosProductivos.get(indice));
                  }
                  if (guardado == true) {
                     guardado = false;
                  }
               }
               index = -1;
               secRegistro = null;
            } else {
               if (!crearProcesosProductivos.contains(filtrarProcesosProductivos.get(indice))) {

                  if (modificarProcesosProductivos.isEmpty()) {
                     modificarProcesosProductivos.add(filtrarProcesosProductivos.get(indice));
                  } else if (!modificarProcesosProductivos.contains(filtrarProcesosProductivos.get(indice))) {
                     modificarProcesosProductivos.add(filtrarProcesosProductivos.get(indice));
                  }
                  if (guardado == true) {
                     guardado = false;
                  }
               }
               index = -1;
               secRegistro = null;
            }
         }

         RequestContext.getCurrentInstance().update("form:datosProcesosProductivos");
         RequestContext.getCurrentInstance().update("form:ACEPTAR");

      }
   }

   public void verificarBorrado() {
      BigInteger contarUnidadesProducidasProcesoProductivo;
      BigInteger contarTarifasProductosProcesoProductivo;
      BigInteger contarCargosProcesoProductivo;

      log.info("Estoy en verificarBorrado");
      try {
         log.error("Control Secuencia de ControlTiposFamiliares ");
         if (tipoLista == 0) {
            contarCargosProcesoProductivo = administrarProcesosProductivos.contarCargosProcesoProductivo(listProcesosProductivos.get(index).getSecuencia());
            contarTarifasProductosProcesoProductivo = administrarProcesosProductivos.contarTarifasProductosProcesoProductivo(listProcesosProductivos.get(index).getSecuencia());
            contarUnidadesProducidasProcesoProductivo = administrarProcesosProductivos.contarUnidadesProducidasProcesoProductivo(listProcesosProductivos.get(index).getSecuencia());
         } else {
            contarCargosProcesoProductivo = administrarProcesosProductivos.contarCargosProcesoProductivo(filtrarProcesosProductivos.get(index).getSecuencia());
            contarTarifasProductosProcesoProductivo = administrarProcesosProductivos.contarTarifasProductosProcesoProductivo(filtrarProcesosProductivos.get(index).getSecuencia());
            contarUnidadesProducidasProcesoProductivo = administrarProcesosProductivos.contarUnidadesProducidasProcesoProductivo(filtrarProcesosProductivos.get(index).getSecuencia());
         }
         if (contarCargosProcesoProductivo.equals(new BigInteger("0")) && contarTarifasProductosProcesoProductivo.equals(new BigInteger("0")) && contarUnidadesProducidasProcesoProductivo.equals(new BigInteger("0"))) {
            log.info("Borrado==0");
            borrandoProcesosProductivos();
         } else {
            log.info("Borrado>0");

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:validacionBorrar");
            RequestContext.getCurrentInstance().execute("PF('validacionBorrar').show()");
            index = -1;

            contarCargosProcesoProductivo = new BigInteger("-1");
            contarTarifasProductosProcesoProductivo = new BigInteger("-1");
            contarUnidadesProducidasProcesoProductivo = new BigInteger("-1");

         }
      } catch (Exception e) {
         log.error("ERROR ControlTiposFamiliares verificarBorrado ERROR  ", e);
      }
   }

   public void borrandoProcesosProductivos() {

      if (index >= 0) {
         if (tipoLista == 0) {
            log.info("Entro a borrandoProcesosProductivos");
            if (!modificarProcesosProductivos.isEmpty() && modificarProcesosProductivos.contains(listProcesosProductivos.get(index))) {
               int modIndex = modificarProcesosProductivos.indexOf(listProcesosProductivos.get(index));
               modificarProcesosProductivos.remove(modIndex);
               borrarProcesosProductivos.add(listProcesosProductivos.get(index));
            } else if (!crearProcesosProductivos.isEmpty() && crearProcesosProductivos.contains(listProcesosProductivos.get(index))) {
               int crearIndex = crearProcesosProductivos.indexOf(listProcesosProductivos.get(index));
               crearProcesosProductivos.remove(crearIndex);
            } else {
               borrarProcesosProductivos.add(listProcesosProductivos.get(index));
            }
            listProcesosProductivos.remove(index);
         }
         if (tipoLista == 1) {
            log.info("borrandoProcesosProductivos ");
            if (!modificarProcesosProductivos.isEmpty() && modificarProcesosProductivos.contains(filtrarProcesosProductivos.get(index))) {
               int modIndex = modificarProcesosProductivos.indexOf(filtrarProcesosProductivos.get(index));
               modificarProcesosProductivos.remove(modIndex);
               borrarProcesosProductivos.add(filtrarProcesosProductivos.get(index));
            } else if (!crearProcesosProductivos.isEmpty() && crearProcesosProductivos.contains(filtrarProcesosProductivos.get(index))) {
               int crearIndex = crearProcesosProductivos.indexOf(filtrarProcesosProductivos.get(index));
               crearProcesosProductivos.remove(crearIndex);
            } else {
               borrarProcesosProductivos.add(filtrarProcesosProductivos.get(index));
            }
            int VCIndex = listProcesosProductivos.indexOf(filtrarProcesosProductivos.get(index));
            listProcesosProductivos.remove(VCIndex);
            filtrarProcesosProductivos.remove(index);

         }
         RequestContext context = RequestContext.getCurrentInstance();
         if (listProcesosProductivos == null || listProcesosProductivos.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
         } else {
            infoRegistro = "Cantidad de registros: " + listProcesosProductivos.size();
         }
         RequestContext.getCurrentInstance().update("form:informacionRegistro");
         RequestContext.getCurrentInstance().update("form:datosProcesosProductivos");
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
      if (valorCambio.equals("PERSONA")) {
         if (tipoNuevo == 1) {
            nuevoYduplicarCompletarPersona = nuevoProcesosProductivos.getCentrocosto().getNombre();
         } else if (tipoNuevo == 2) {
            nuevoYduplicarCompletarPersona = duplicarProcesosProductivos.getCentrocosto().getNombre();
         }

         log.info("PERSONA : " + nuevoYduplicarCompletarPersona);
      }
   }

   public void autocompletarNuevo(String confirmarCambio, String valorConfirmar, int tipoNuevo) {

      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      if (confirmarCambio.equalsIgnoreCase("PERSONA")) {
         log.info(" nueva Ciudad    Entro al if 'Centro costo'");
         log.info("NUEVO PERSONA :-------> " + nuevoProcesosProductivos.getCentrocosto().getNombre());

         if (!nuevoProcesosProductivos.getCentrocosto().getNombre().equals("")) {
            log.info("ENTRO DONDE NO TENIA QUE ENTRAR");
            log.info("valorConfirmar: " + valorConfirmar);
            log.info("nuevoYduplicarCompletarPersona: " + nuevoYduplicarCompletarPersona);
            nuevoProcesosProductivos.getCentrocosto().setNombre(nuevoYduplicarCompletarPersona);
            for (int i = 0; i < listaCentrosCostos.size(); i++) {
               if (listaCentrosCostos.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            log.info("Coincidencias: " + coincidencias);
            if (coincidencias == 1) {
               nuevoProcesosProductivos.setCentrocosto(listaCentrosCostos.get(indiceUnicoElemento));
               listaCentrosCostos = null;
               log.error("PERSONA GUARDADA :-----> " + nuevoProcesosProductivos.getCentrocosto().getNombre());
            } else {
               RequestContext.getCurrentInstance().update("form:personasDialogo");
               RequestContext.getCurrentInstance().execute("PF('personasDialogo').show()");
               tipoActualizacion = tipoNuevo;
            }
         } else {
            nuevoProcesosProductivos.getCentrocosto().setNombre(nuevoYduplicarCompletarPersona);
            log.info("valorConfirmar cuando es vacio: " + valorConfirmar);
            nuevoProcesosProductivos.setCentrocosto(new CentrosCostos());
            nuevoProcesosProductivos.getCentrocosto().setNombre(" ");
            log.info("NUEVA NORMA LABORAL" + nuevoProcesosProductivos.getCentrocosto().getNombre());
         }
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevoPersona");
      }
   }

   public void asignarVariableCentrosCostos(int tipoNuevo) {
      if (tipoNuevo == 0) {
         tipoActualizacion = 1;
      }
      if (tipoNuevo == 1) {
         tipoActualizacion = 2;
      }
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:personasDialogo");
      RequestContext.getCurrentInstance().execute("PF('personasDialogo').show()");
   }

   public void asignarVariableCiudades(int tipoNuevo) {
      if (tipoNuevo == 0) {
         tipoActualizacion = 1;
      }
      if (tipoNuevo == 1) {
         tipoActualizacion = 2;
      }
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:cargosDialogo");
      RequestContext.getCurrentInstance().execute("PF('cargosDialogo').show()");
   }

   public void autocompletarDuplicado(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
      log.info("DUPLICAR entrooooooooooooooooooooooooooooooooooooooooooooooooooooooo!!!");
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      if (confirmarCambio.equalsIgnoreCase("PERSONA")) {
         log.info("DUPLICAR valorConfirmar : " + valorConfirmar);
         log.info("DUPLICAR CIUDAD bkp : " + nuevoYduplicarCompletarPersona);

         if (!duplicarProcesosProductivos.getCentrocosto().getNombre().equals("")) {
            log.info("DUPLICAR ENTRO DONDE NO TENIA QUE ENTRAR");
            log.info("DUPLICAR valorConfirmar: " + valorConfirmar);
            log.info("DUPLICAR nuevoTipoCCAutoCompletar: " + nuevoYduplicarCompletarPersona);
            duplicarProcesosProductivos.getCentrocosto().setNombre(nuevoYduplicarCompletarPersona);
            for (int i = 0; i < listaCentrosCostos.size(); i++) {
               if (listaCentrosCostos.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            log.info("Coincidencias: " + coincidencias);
            if (coincidencias == 1) {
               duplicarProcesosProductivos.setCentrocosto(listaCentrosCostos.get(indiceUnicoElemento));
               listaCentrosCostos = null;
            } else {
               RequestContext.getCurrentInstance().update("form:personasDialogo");
               RequestContext.getCurrentInstance().execute("PF('personasDialogo').show()");
               tipoActualizacion = tipoNuevo;
            }
         } else if (tipoNuevo == 2) {
            //duplicarProcesosProductivos.getEmpresa().setNombre(nuevoYduplicarCompletarPais);
            log.info("DUPLICAR valorConfirmar cuando es vacio: " + valorConfirmar);
            log.info("DUPLICAR INDEX : " + index);
            duplicarProcesosProductivos.setCentrocosto(new CentrosCostos());
            duplicarProcesosProductivos.getCentrocosto().setNombre(" ");

            log.info("DUPLICAR PERSONA  : " + duplicarProcesosProductivos.getCentrocosto().getNombre());
            log.info("nuevoYduplicarCompletarPERSONA : " + nuevoYduplicarCompletarPersona);
            if (tipoLista == 0) {
               listProcesosProductivos.get(index).getCentrocosto().setNombre(nuevoYduplicarCompletarPersona);
               log.error("tipo lista" + tipoLista);
               log.error("Secuencia Parentesco " + listProcesosProductivos.get(index).getCentrocosto().getSecuencia());
            } else if (tipoLista == 1) {
               filtrarProcesosProductivos.get(index).getCentrocosto().setNombre(nuevoYduplicarCompletarPersona);
            }

         }
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarPersona");
      }
   }

   /*public void verificarBorrado() {
     log.info("Estoy en verificarBorrado");
     BigInteger contarBienProgramacionesDepartamento;
     BigInteger contarCapModulosDepartamento;
     BigInteger contarCiudadesDepartamento;
     BigInteger contarSoAccidentesMedicosDepartamento;

     try {
     log.error("Control Secuencia de ControlProcesosProductivos ");
     if (tipoLista == 0) {
     contarBienProgramacionesDepartamento = administrarProcesosProductivos.contarBienProgramacionesDepartamento(listProcesosProductivos.get(index).getSecuencia());
     contarCapModulosDepartamento = administrarProcesosProductivos.contarCapModulosDepartamento(listProcesosProductivos.get(index).getSecuencia());
     contarCiudadesDepartamento = administrarProcesosProductivos.contarCiudadesDepartamento(listProcesosProductivos.get(index).getSecuencia());
     contarSoAccidentesMedicosDepartamento = administrarProcesosProductivos.contarSoAccidentesMedicosDepartamento(listProcesosProductivos.get(index).getSecuencia());
     } else {
     contarBienProgramacionesDepartamento = administrarProcesosProductivos.contarBienProgramacionesDepartamento(filtrarProcesosProductivos.get(index).getSecuencia());
     contarCapModulosDepartamento = administrarProcesosProductivos.contarCapModulosDepartamento(filtrarProcesosProductivos.get(index).getSecuencia());
     contarCiudadesDepartamento = administrarProcesosProductivos.contarCiudadesDepartamento(filtrarProcesosProductivos.get(index).getSecuencia());
     contarSoAccidentesMedicosDepartamento = administrarProcesosProductivos.contarSoAccidentesMedicosDepartamento(filtrarProcesosProductivos.get(index).getSecuencia());
     }
     if (contarBienProgramacionesDepartamento.equals(new BigInteger("0"))
     && contarCapModulosDepartamento.equals(new BigInteger("0"))
     && contarCiudadesDepartamento.equals(new BigInteger("0"))
     && contarSoAccidentesMedicosDepartamento.equals(new BigInteger("0"))) {
     log.info("Borrado==0");
     borrandoProcesosProductivos();
     } else {
     log.info("Borrado>0");

     RequestContext context = RequestContext.getCurrentInstance();
     RequestContext.getCurrentInstance().update("form:validacionBorrar");
     RequestContext.getCurrentInstance().execute("PF('validacionBorrar').show()");
     index = -1;
     contarBienProgramacionesDepartamento = new BigInteger("-1");
     contarCapModulosDepartamento = new BigInteger("-1");
     contarCiudadesDepartamento = new BigInteger("-1");
     contarSoAccidentesMedicosDepartamento = new BigInteger("-1");

     }
     } catch (Exception e) {
     log.error("ERROR ControlProcesosProductivos verificarBorrado ERROR  ", e);
     }
     }
    */
   public void revisarDialogoGuardar() {

      if (!borrarProcesosProductivos.isEmpty() || !crearProcesosProductivos.isEmpty() || !modificarProcesosProductivos.isEmpty()) {
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:confirmarGuardar");
         RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
      }

   }

   public void guardarProcesosProductivos() {
      RequestContext context = RequestContext.getCurrentInstance();

      if (guardado == false) {
         log.info("Realizando guardarProcesosProductivos");
         if (!borrarProcesosProductivos.isEmpty()) {
            administrarProcesosProductivos.borrarProcesosProductivos(borrarProcesosProductivos);
            //mostrarBorrados
            registrosBorrados = borrarProcesosProductivos.size();
            RequestContext.getCurrentInstance().update("form:mostrarBorrados");
            RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
            borrarProcesosProductivos.clear();
         }
         if (!modificarProcesosProductivos.isEmpty()) {
            administrarProcesosProductivos.modificarProcesosProductivos(modificarProcesosProductivos);
            modificarProcesosProductivos.clear();
         }
         if (!crearProcesosProductivos.isEmpty()) {
            administrarProcesosProductivos.crearProcesosProductivos(crearProcesosProductivos);
            crearProcesosProductivos.clear();
         }
         listProcesosProductivos = null;
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
            editarProcesosProductivos = listProcesosProductivos.get(index);
         }
         if (tipoLista == 1) {
            editarProcesosProductivos = filtrarProcesosProductivos.get(index);
         }

         RequestContext context = RequestContext.getCurrentInstance();
         log.info("Entro a editar... valor celda: " + cualCelda);
         if (cualCelda == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editPais");
            RequestContext.getCurrentInstance().execute("PF('editPais').show()");
            cualCelda = -1;
         } else if (cualCelda == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editSubtituloFirma");
            RequestContext.getCurrentInstance().execute("PF('editSubtituloFirma').show()");
            cualCelda = -1;
         } else if (cualCelda == 2) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editCentrosCostos");
            RequestContext.getCurrentInstance().execute("PF('editCentrosCostos').show()");
            cualCelda = -1;
         } else if (cualCelda == 3) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editCiudades");
            RequestContext.getCurrentInstance().execute("PF('editCiudades').show()");
            cualCelda = -1;
         }

      }
      index = -1;
      secRegistro = null;
   }

   public void agregarNuevoProcesosProductivos() {
      log.info("agregarNuevoProcesosProductivos");
      int contador = 0;
      int duplicados = 0;

      Integer a = 0;
      a = null;
      mensajeValidacion = " ";
      RequestContext context = RequestContext.getCurrentInstance();
      if (nuevoProcesosProductivos.getCodigo() == null) {
         mensajeValidacion = " *Codigo \n";
         log.info("Mensaje validacion : " + mensajeValidacion);
      } else {
         log.info("codigo en Motivo Cambio Cargo: " + nuevoProcesosProductivos.getCodigo());

         for (int x = 0; x < listProcesosProductivos.size(); x++) {
            if (listProcesosProductivos.get(x).getCodigo().equals(nuevoProcesosProductivos.getCodigo())) {
               duplicados++;
            }
         }
         log.info("Antes del if Duplicados eses igual  : " + duplicados);

         if (duplicados > 0) {
            mensajeValidacion = " *Que NO Hayan Codigos Repetidos \n";
            log.info("Mensaje validacion : " + mensajeValidacion);
         } else {
            log.info("bandera");
            contador++;//1
         }
      }
      if (nuevoProcesosProductivos.getDescripcion() == null) {
         mensajeValidacion = mensajeValidacion + " *Descripcion \n";
         log.info("Mensaje validacion : " + mensajeValidacion);

      } else {
         log.info("bandera");
         contador++;//2

      }

      if (nuevoProcesosProductivos.getCentrocosto().getNombre() == null) {
         mensajeValidacion = mensajeValidacion + " *Centro Costo \n";
         log.info("Mensaje validacion : " + mensajeValidacion);

      } else {
         log.info("bandera");
         contador++;//4

      }

      log.info("contador " + contador);

      if (contador == 3) {
         if (bandera == 1) {
            //CERRAR FILTRADO
            FacesContext c = FacesContext.getCurrentInstance();

            log.info("Desactivar");
            codigo = (Column) c.getViewRoot().findComponent("form:datosProcesosProductivos:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosProcesosProductivos:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            personafir = (Column) c.getViewRoot().findComponent("form:datosProcesosProductivos:personafir");
            personafir.setFilterStyle("display: none; visibility: hidden;");
            bandera = 0;
            filtrarProcesosProductivos = null;
            tipoLista = 0;
         }
         log.info("Despues de la bandera");

         k++;
         l = BigInteger.valueOf(k);
         nuevoProcesosProductivos.setSecuencia(l);

         crearProcesosProductivos.add(nuevoProcesosProductivos);

         listProcesosProductivos.add(nuevoProcesosProductivos);
         nuevoProcesosProductivos = new ProcesosProductivos();
         nuevoProcesosProductivos.setCentrocosto(new CentrosCostos());
         RequestContext.getCurrentInstance().update("form:datosProcesosProductivos");

         infoRegistro = "Cantidad de registros: " + listProcesosProductivos.size();

         RequestContext.getCurrentInstance().update("form:informacionRegistro");
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }

         RequestContext.getCurrentInstance().execute("PF('nuevoRegistroProcesosProductivos').hide()");
         index = -1;
         secRegistro = null;

      } else {
         RequestContext.getCurrentInstance().update("form:validacionNuevaCentroCosto");
         RequestContext.getCurrentInstance().execute("PF('validacionNuevaCentroCosto').show()");
         contador = 0;
      }
   }

   public void limpiarNuevoProcesosProductivos() {
      log.info("limpiarNuevoProcesosProductivos");
      nuevoProcesosProductivos = new ProcesosProductivos();
      nuevoProcesosProductivos.setCentrocosto(new CentrosCostos());
      secRegistro = null;
      index = -1;

   }

   //------------------------------------------------------------------------------
   public void cargarNuevoProcesosProductivos() {
      log.info("cargarNuevoProcesosProductivos");

      duplicarProcesosProductivos = new ProcesosProductivos();
      duplicarProcesosProductivos.setCentrocosto(new CentrosCostos());
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().execute("PF('nuevoRegistroProcesosProductivos').show()");

   }

   public void duplicandoProcesosProductivos() {
      log.info("duplicandoProcesosProductivos");
      if (index >= 0) {
         duplicarProcesosProductivos = new ProcesosProductivos();
         duplicarProcesosProductivos.setCentrocosto(new CentrosCostos());
         k++;
         l = BigInteger.valueOf(k);

         if (tipoLista == 0) {
            duplicarProcesosProductivos.setSecuencia(l);
            duplicarProcesosProductivos.setCodigo(listProcesosProductivos.get(index).getCodigo());
            duplicarProcesosProductivos.setDescripcion(listProcesosProductivos.get(index).getDescripcion());
            duplicarProcesosProductivos.setCentrocosto(listProcesosProductivos.get(index).getCentrocosto());
         }
         if (tipoLista == 1) {
            duplicarProcesosProductivos.setSecuencia(l);
            duplicarProcesosProductivos.setCodigo(filtrarProcesosProductivos.get(index).getCodigo());
            duplicarProcesosProductivos.setDescripcion(filtrarProcesosProductivos.get(index).getDescripcion());
            duplicarProcesosProductivos.setCentrocosto(filtrarProcesosProductivos.get(index).getCentrocosto());

         }

         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTE");
         RequestContext.getCurrentInstance().execute("PF('duplicarRegistroProcesosProductivos').show()");
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
      log.error("ConfirmarDuplicar codigo " + duplicarProcesosProductivos.getCodigo());

      if (duplicarProcesosProductivos.getCodigo() == null) {
         mensajeValidacion = mensajeValidacion + "   *Codigo \n";
         log.info("Mensaje validacion : " + mensajeValidacion);
      } else {
         for (int x = 0; x < listProcesosProductivos.size(); x++) {
            if (listProcesosProductivos.get(x).getCodigo().equals(duplicarProcesosProductivos.getCodigo())) {
               duplicados++;
            }
         }
         if (duplicados > 0) {
            mensajeValidacion = " *Que NO Existan Codigo Repetidos \n";
            log.info("Mensaje validacion : " + mensajeValidacion);
         } else {
            log.info("bandera");
            contador++;
            duplicados = 0;
         }
      }

      if (duplicarProcesosProductivos.getDescripcion() == null) {
         mensajeValidacion = mensajeValidacion + "   *Descripcion \n";
         log.info("Mensaje validacion : " + mensajeValidacion);

      } else {
         log.info("Bandera : ");
         contador++;
      }

      if (duplicarProcesosProductivos.getCentrocosto().getNombre() == null) {
         mensajeValidacion = mensajeValidacion + "   *Centro Costo \n";
         log.info("Mensaje validacion : " + mensajeValidacion);

      } else {
         log.info("Bandera : ");
         contador++;
      }

      if (contador == 3) {

         log.info("Datos Duplicando: " + duplicarProcesosProductivos.getSecuencia() + "  " + duplicarProcesosProductivos.getCodigo());
         if (crearProcesosProductivos.contains(duplicarProcesosProductivos)) {
            log.info("Ya lo contengo.");
         }
         listProcesosProductivos.add(duplicarProcesosProductivos);
         crearProcesosProductivos.add(duplicarProcesosProductivos);
         RequestContext.getCurrentInstance().update("form:datosProcesosProductivos");
         index = -1;
         log.info("--------------DUPLICAR------------------------");
         log.info("CODIGO : " + duplicarProcesosProductivos.getCodigo());
         log.info("EMPRESA: " + duplicarProcesosProductivos.getDescripcion());
         log.info("CARGO : " + duplicarProcesosProductivos.getCentrocosto().getNombre());
         log.info("--------------DUPLICAR------------------------");

         secRegistro = null;
         if (guardado == true) {
            guardado = false;
         }
         RequestContext.getCurrentInstance().update("form:ACEPTAR");

         infoRegistro = "Cantidad de registros: " + listProcesosProductivos.size();
         RequestContext.getCurrentInstance().update("form:informacionRegistro");
         if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();

            //CERRAR FILTRADO
            codigo = (Column) c.getViewRoot().findComponent("form:datosProcesosProductivos:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosProcesosProductivos:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            personafir = (Column) c.getViewRoot().findComponent("form:datosProcesosProductivos:personafir");
            personafir.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosProcesosProductivos");
            bandera = 0;
            filtrarProcesosProductivos = null;
            tipoLista = 0;
         }
         duplicarProcesosProductivos = new ProcesosProductivos();
         duplicarProcesosProductivos.setCentrocosto(new CentrosCostos());

         RequestContext.getCurrentInstance().execute("PF('duplicarRegistroProcesosProductivos').hide()");

      } else {
         contador = 0;
         RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
         RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
      }
   }

   public void limpiarDuplicarProcesosProductivos() {
      duplicarProcesosProductivos = new ProcesosProductivos();
      duplicarProcesosProductivos.setCentrocosto(new CentrosCostos());
   }

   public void exportPDF() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosProcesosProductivosExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "PROCESOSPRODUCTIVOS", false, false, "UTF-8", null, null);
      context.responseComplete();
      index = -1;
      secRegistro = null;
   }

   public void exportXLS() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosProcesosProductivosExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "PROCESOSPRODUCTIVOS", false, false, "UTF-8", null, null);
      context.responseComplete();
      index = -1;
      secRegistro = null;
   }

   public void verificarRastro() {
      RequestContext context = RequestContext.getCurrentInstance();
      log.info("lol");
      if (!listProcesosProductivos.isEmpty()) {
         if (secRegistro != null) {
            log.info("lol 2");
            int resultado = administrarRastros.obtenerTabla(secRegistro, "PROCESOSPRODUCTIVOS"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
      } else if (administrarRastros.verificarHistoricosTabla("PROCESOSPRODUCTIVOS")) { // igual acá
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
      index = -1;
   }

   //*/*/*/*/*/*/*/*/*/*-/-*//-*/-*/*/*-*/-*/-*/*/*/*/*/---/*/*/*/*/-*/-*/-*/-*/-*/
   public List<ProcesosProductivos> getListProcesosProductivos() {
      if (listProcesosProductivos == null) {
         listProcesosProductivos = administrarProcesosProductivos.consultarProcesosProductivos();
      }
      RequestContext context = RequestContext.getCurrentInstance();
      if (listProcesosProductivos == null || listProcesosProductivos.isEmpty()) {
         infoRegistro = "Cantidad de registros: 0 ";
      } else {
         infoRegistro = "Cantidad de registros: " + listProcesosProductivos.size();
      }
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
      return listProcesosProductivos;
   }

   public void setListProcesosProductivos(List<ProcesosProductivos> listProcesosProductivos) {
      this.listProcesosProductivos = listProcesosProductivos;
   }

   public List<ProcesosProductivos> getFiltrarProcesosProductivos() {
      return filtrarProcesosProductivos;
   }

   public void setFiltrarProcesosProductivos(List<ProcesosProductivos> filtrarProcesosProductivos) {
      this.filtrarProcesosProductivos = filtrarProcesosProductivos;
   }

   public ProcesosProductivos getNuevoProcesosProductivos() {
      return nuevoProcesosProductivos;
   }

   public void setNuevoProcesosProductivos(ProcesosProductivos nuevoProcesosProductivos) {
      this.nuevoProcesosProductivos = nuevoProcesosProductivos;
   }

   public ProcesosProductivos getDuplicarProcesosProductivos() {
      return duplicarProcesosProductivos;
   }

   public void setDuplicarProcesosProductivos(ProcesosProductivos duplicarProcesosProductivos) {
      this.duplicarProcesosProductivos = duplicarProcesosProductivos;
   }

   public ProcesosProductivos getEditarProcesosProductivos() {
      return editarProcesosProductivos;
   }

   public void setEditarProcesosProductivos(ProcesosProductivos editarProcesosProductivos) {
      this.editarProcesosProductivos = editarProcesosProductivos;
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

   public List<CentrosCostos> getListaCentrosCostos() {
      if (listaCentrosCostos == null) {
         if (listasRecurrentes.getLovCentrosCostos().isEmpty()) {
            listaCentrosCostos = administrarProcesosProductivos.consultarLOVCentrosCostos();
            if (listaCentrosCostos != null) {
               listasRecurrentes.setLovCentrosCostos(listaCentrosCostos);
            }
         } else {
            listaCentrosCostos = new ArrayList<CentrosCostos>(listasRecurrentes.getLovCentrosCostos());
         }
      }
      if (listaCentrosCostos == null || listaCentrosCostos.isEmpty()) {
         infoRegistroCentroCostos = "Cantidad de registros: 0 ";
      } else {
         infoRegistroCentroCostos = "Cantidad de registros: " + listaCentrosCostos.size();
      }
      RequestContext.getCurrentInstance().update("form:infoRegistroCentroCostos");
      return listaCentrosCostos;
   }

   public void setListaCentrosCostos(List<CentrosCostos> listaCentrosCostos) {
      this.listaCentrosCostos = listaCentrosCostos;
   }

   public List<CentrosCostos> getFiltradoCentrosCostos() {
      return filtradoCentrosCostos;
   }

   public void setFiltradoCentrosCostos(List<CentrosCostos> filtradoCentrosCostos) {
      this.filtradoCentrosCostos = filtradoCentrosCostos;
   }

   public CentrosCostos getCentrocostoSeleccionado() {
      return centrocostoSeleccionado;
   }

   public void setCentrocostoSeleccionado(CentrosCostos centrocostoSeleccionado) {
      this.centrocostoSeleccionado = centrocostoSeleccionado;
   }

   public ProcesosProductivos getProcesoProductivoSeleccionado() {
      return procesoProductivoSeleccionado;
   }

   public void setProcesoProductivoSeleccionado(ProcesosProductivos procesoProductivoSeleccionado) {
      this.procesoProductivoSeleccionado = procesoProductivoSeleccionado;
   }

   public String getInfoRegistro() {
      return infoRegistro;
   }

   public void setInfoRegistro(String infoRegistro) {
      this.infoRegistro = infoRegistro;
   }

   public String getInfoRegistroCentroCostos() {
      return infoRegistroCentroCostos;
   }

   public void setInfoRegistroCentroCostos(String infoRegistroCentroCostos) {
      this.infoRegistroCentroCostos = infoRegistroCentroCostos;
   }

   public boolean isAceptar() {
      return aceptar;
   }

   public void setAceptar(boolean aceptar) {
      this.aceptar = aceptar;
   }

}
