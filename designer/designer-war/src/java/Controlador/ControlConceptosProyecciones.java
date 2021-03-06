/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Conceptos;
import Entidades.ConceptosProyecciones;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarConceptosProyeccionesInterface;
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
public class ControlConceptosProyecciones implements Serializable {

   private static Logger log = Logger.getLogger(ControlConceptosProyecciones.class);

   @EJB
   AdministrarConceptosProyeccionesInterface administrarConceptosProyecciones;
   @EJB
   AdministrarRastrosInterface administrarRastros;
   private List<ConceptosProyecciones> listConceptosProyecciones;
   private List<ConceptosProyecciones> filtrarConceptosProyecciones;
   private List<ConceptosProyecciones> crearConceptosProyecciones;
   private List<ConceptosProyecciones> modificarConceptosProyecciones;
   private List<ConceptosProyecciones> borrarConceptosProyecciones;
   private ConceptosProyecciones nuevoConceptosProyecciones;
   private ConceptosProyecciones duplicarConceptosProyecciones;
   private ConceptosProyecciones editarConceptosProyecciones;
   private ConceptosProyecciones sucursalSeleccionada;
   //otros
   private int cualCelda, tipoLista, index, tipoActualizacion, k, bandera;
   private BigInteger l;
   private boolean aceptar, guardado;
   //AutoCompletar
   private boolean permitirIndex;
   //RASTRO
   private BigInteger secRegistro;
   private Column porcentaje, concepto;
   //borrado
   private int registrosBorrados;
   private String mensajeValidacion;
   //filtrado table
   private int tamano;
   private Short backupPorcentajeProyeccion;

   //--------------------------------------
   private String backupConcepto;
   private List<Conceptos> lovConceptos;
   private List<Conceptos> filtradoConceptos;
   private Conceptos conceptoSeleccionado;
   private String nuevoYduplicarCompletarCargo;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

   public ControlConceptosProyecciones() {
      listConceptosProyecciones = null;
      crearConceptosProyecciones = new ArrayList<ConceptosProyecciones>();
      modificarConceptosProyecciones = new ArrayList<ConceptosProyecciones>();
      borrarConceptosProyecciones = new ArrayList<ConceptosProyecciones>();
      permitirIndex = true;
      editarConceptosProyecciones = new ConceptosProyecciones();
      nuevoConceptosProyecciones = new ConceptosProyecciones();
      nuevoConceptosProyecciones.setConcepto(new Conceptos());
      duplicarConceptosProyecciones = new ConceptosProyecciones();
      duplicarConceptosProyecciones.setConcepto(new Conceptos());
      lovConceptos = null;
      filtradoConceptos = null;
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
      String pagActual = "conceptoproyeccion";
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
      lovConceptos = null;
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
         administrarConceptosProyecciones.obtenerConexion(ses.getId());
         administrarRastros.obtenerConexion(ses.getId());
      } catch (Exception e) {
         log.error("Error postconstruct " + this.getClass().getName() + ":  ", e);
         log.error("Causa: " + e.getCause());
      }
   }

   public void eventoFiltrar() {
      try {
         log.info("\n ENTRE A ControlConceptosProyecciones.eventoFiltrar \n");
         if (tipoLista == 0) {
            tipoLista = 1;
         }
         RequestContext context = RequestContext.getCurrentInstance();
         infoRegistro = "Cantidad de registros: " + filtrarConceptosProyecciones.size();
         RequestContext.getCurrentInstance().update("form:informacionRegistro");
      } catch (Exception e) {
         log.warn("Error ControlConceptosProyecciones eventoFiltrar ERROR===" + e.getMessage());
      }
   }

   public void cambiarIndice(int indice, int celda) {
      log.error("TIPO LISTA = " + tipoLista);

      if (permitirIndex == true) {
         index = indice;
         cualCelda = celda;
         secRegistro = listConceptosProyecciones.get(index).getSecuencia();
         if (tipoLista == 0) {
            if (cualCelda == 0) {
               backupConcepto = listConceptosProyecciones.get(index).getConcepto().getDescripcion();
               log.info("Concepto : " + backupConcepto);

            }
            if (cualCelda == 1) {
               backupPorcentajeProyeccion = listConceptosProyecciones.get(index).getPorcentajeproyeccion();
            }
         } else if (tipoLista == 1) {
            if (cualCelda == 0) {
               backupConcepto = filtrarConceptosProyecciones.get(index).getConcepto().getDescripcion();
               log.info("Concepto : " + backupConcepto);
            }
            if (cualCelda == 0) {
               backupPorcentajeProyeccion = filtrarConceptosProyecciones.get(index).getPorcentajeproyeccion();
            }

         }

      }
      log.info("Indice: " + index + " Celda: " + cualCelda);
   }

   public void asignarIndex(Integer indice, int LND, int dig) {
      try {
         RequestContext context = RequestContext.getCurrentInstance();
         log.info("\n ENTRE A ControlConceptosProyecciones.asignarIndex \n");
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
      } catch (Exception e) {
         log.warn("Error ControlConceptosProyecciones.asignarIndex ERROR======" + e.getMessage());
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
      }
   }
   private String infoRegistro;

   public void cancelarModificacion() {
      if (bandera == 1) {
         //CERRAR FILTRADO
         FacesContext c = FacesContext.getCurrentInstance();
         porcentaje = (Column) c.getViewRoot().findComponent("form:datosConceptosProyecciones:porcentaje");
         porcentaje.setFilterStyle("display: none; visibility: hidden;");
         concepto = (Column) c.getViewRoot().findComponent("form:datosConceptosProyecciones:concepto");
         concepto.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosConceptosProyecciones");
         bandera = 0;
         filtrarConceptosProyecciones = null;
         tipoLista = 0;
      }

      borrarConceptosProyecciones.clear();
      crearConceptosProyecciones.clear();
      modificarConceptosProyecciones.clear();
      index = -1;
      secRegistro = null;
      k = 0;
      listConceptosProyecciones = null;
      guardado = true;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      if (listConceptosProyecciones == null || listConceptosProyecciones.isEmpty()) {
         infoRegistro = "Cantidad de registros: 0 ";
      } else {
         infoRegistro = "Cantidad de registros: " + listConceptosProyecciones.size();
      }
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
      RequestContext.getCurrentInstance().update("form:datosConceptosProyecciones");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void salir() {
      limpiarListasValor();
      if (bandera == 1) {
         //CERRAR FILTRADO
         FacesContext c = FacesContext.getCurrentInstance();
         porcentaje = (Column) c.getViewRoot().findComponent("form:datosConceptosProyecciones:porcentaje");
         porcentaje.setFilterStyle("display: none; visibility: hidden;");
         concepto = (Column) c.getViewRoot().findComponent("form:datosConceptosProyecciones:concepto");
         concepto.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosConceptosProyecciones");
         bandera = 0;
         filtrarConceptosProyecciones = null;
         tipoLista = 0;
      }

      borrarConceptosProyecciones.clear();
      crearConceptosProyecciones.clear();
      modificarConceptosProyecciones.clear();
      index = -1;
      secRegistro = null;
      k = 0;
      listConceptosProyecciones = null;
      guardado = true;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      if (listConceptosProyecciones == null || listConceptosProyecciones.isEmpty()) {
         infoRegistro = "Cantidad de registros: 0 ";
      } else {
         infoRegistro = "Cantidad de registros: " + listConceptosProyecciones.size();
      }
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
      RequestContext.getCurrentInstance().update("form:datosConceptosProyecciones");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void activarCtrlF11() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 0) {
         tamano = 250;
         porcentaje = (Column) c.getViewRoot().findComponent("form:datosConceptosProyecciones:porcentaje");
         porcentaje.setFilterStyle("width: 85% !important");
         concepto = (Column) c.getViewRoot().findComponent("form:datosConceptosProyecciones:concepto");
         concepto.setFilterStyle("width: 85% !important");
         RequestContext.getCurrentInstance().update("form:datosConceptosProyecciones");
         log.info("Activar");
         bandera = 1;
      } else if (bandera == 1) {
         log.info("Desactivar");
         tamano = 270;
         porcentaje = (Column) c.getViewRoot().findComponent("form:datosConceptosProyecciones:porcentaje");
         porcentaje.setFilterStyle("display: none; visibility: hidden;");
         concepto = (Column) c.getViewRoot().findComponent("form:datosConceptosProyecciones:concepto");
         concepto.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosConceptosProyecciones");
         bandera = 0;
         filtrarConceptosProyecciones = null;
         tipoLista = 0;
      }
   }

   public void actualizarConceptos() {
      int duplicados = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      log.info("Concepto seleccionado : " + conceptoSeleccionado.getDescripcion());
      log.info("tipo Actualizacion : " + tipoActualizacion);
      log.info("tipo Lista : " + tipoLista);

      if (tipoActualizacion == 0) {
         for (int i = 0; i < listConceptosProyecciones.size(); i++) {
            if (listConceptosProyecciones.get(i).getConcepto().getDescripcion().equals(conceptoSeleccionado.getDescripcion())) {
               duplicados++;
            }
         }
         if (duplicados == 0) {
            if (tipoLista == 0) {
               listConceptosProyecciones.get(index).setConcepto(conceptoSeleccionado);
               if (!crearConceptosProyecciones.contains(listConceptosProyecciones.get(index))) {
                  if (modificarConceptosProyecciones.isEmpty()) {
                     modificarConceptosProyecciones.add(listConceptosProyecciones.get(index));
                  } else if (!modificarConceptosProyecciones.contains(listConceptosProyecciones.get(index))) {
                     modificarConceptosProyecciones.add(listConceptosProyecciones.get(index));
                  }
               }
            } else {
               mensajeValidacion = "CONCEPTO YA ELEGIDO";
               RequestContext.getCurrentInstance().update("form:validacionModificar");
               RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");
            }

         } else {
            for (int i = 0; i < listConceptosProyecciones.size(); i++) {
               if (listConceptosProyecciones.get(i).getConcepto().getDescripcion().equals(conceptoSeleccionado.getDescripcion())) {
                  duplicados++;
               }
            }
            if (duplicados == 0) {
               filtrarConceptosProyecciones.get(index).setConcepto(conceptoSeleccionado);

               if (!crearConceptosProyecciones.contains(filtrarConceptosProyecciones.get(index))) {
                  if (modificarConceptosProyecciones.isEmpty()) {
                     modificarConceptosProyecciones.add(filtrarConceptosProyecciones.get(index));
                  } else if (!modificarConceptosProyecciones.contains(filtrarConceptosProyecciones.get(index))) {
                     modificarConceptosProyecciones.add(filtrarConceptosProyecciones.get(index));
                  }
               }
            } else {
               mensajeValidacion = "CONCEPTO YA ELEGIDO";
               RequestContext.getCurrentInstance().update("form:validacionModificar");
               RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");
            }

         }
         if (duplicados == 0) {
            if (guardado == true) {
               guardado = false;
            }
         }
         permitirIndex = true;
         log.info("ACTUALIZAR CONCEPTO CONCEPTO SELECCIONADO : " + conceptoSeleccionado.getDescripcion());
         RequestContext.getCurrentInstance().update("form:datosConceptosProyecciones");
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      } else if (tipoActualizacion == 1) {
         log.info("ACTUALIZAR PAIS NUEVO DEPARTAMENTO: " + conceptoSeleccionado.getDescripcion());
         nuevoConceptosProyecciones.setConcepto(conceptoSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevoCargo");
      } else if (tipoActualizacion == 2) {
         log.info("ACTUALIZAR PAIS DUPLICAR DEPARTAMENO: " + conceptoSeleccionado.getDescripcion());
         duplicarConceptosProyecciones.setConcepto(conceptoSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCargo");
      }
      filtradoConceptos = null;
      conceptoSeleccionado = null;
      aceptar = true;
      index = -1;
      secRegistro = null;
      tipoActualizacion = -1;
      cualCelda = -1;
      context.reset("form:lovCiudades:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovCiudades').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('conceptosDialogo').hide()");
      //RequestContext.getCurrentInstance().update("form:lovCiudades");
      //RequestContext.getCurrentInstance().update("form:datosHvEntrevista");
   }

   public void cancelarCambioConceptos() {
      filtradoConceptos = null;
      conceptoSeleccionado = null;
      aceptar = true;
      index = -1;
      secRegistro = null;
      tipoActualizacion = -1;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("form:lovCiudades:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovCiudades').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('conceptosDialogo').hide()");
   }

   public void modificarConceptosProyecciones(int indice, String confirmarCambio, String valorConfirmar) {
      log.error("ENTRE A MODIFICAR SUB CATEGORIA");
      index = indice;
      int coincidencias = 0;
      int contador = 0;
      boolean banderita = false;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      log.error("TIPO LISTA = " + tipoLista);
      if (confirmarCambio.equalsIgnoreCase("N")) {
         log.error("ENTRE A MODIFICAR EMPRESAS, CONFIRMAR CAMBIO ES N");
         if (tipoLista == 0) {
            if (!crearConceptosProyecciones.contains(listConceptosProyecciones.get(indice))) {

               log.info("backupPorcentajeProyeccion : " + backupPorcentajeProyeccion);

               if (listConceptosProyecciones.get(indice).getPorcentajeproyeccion() == null) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  banderita = false;
                  listConceptosProyecciones.get(indice).setPorcentajeproyeccion(backupPorcentajeProyeccion);

               } else if (0 <= listConceptosProyecciones.get(indice).getPorcentajeproyeccion() && listConceptosProyecciones.get(indice).getPorcentajeproyeccion() <= 100) {
                  banderita = true;
               } else {
                  listConceptosProyecciones.get(indice).setPorcentajeproyeccion(backupPorcentajeProyeccion);
                  mensajeValidacion = "Porcentaje debe estar entre 0 y 100";
                  banderita = false;
               }

               if (banderita == true) {
                  if (modificarConceptosProyecciones.isEmpty()) {
                     modificarConceptosProyecciones.add(listConceptosProyecciones.get(indice));
                  } else if (!modificarConceptosProyecciones.contains(listConceptosProyecciones.get(indice))) {
                     modificarConceptosProyecciones.add(listConceptosProyecciones.get(indice));
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
               RequestContext.getCurrentInstance().update("form:datosConceptosProyecciones");
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            } else {
               log.info("backupPorcentajeProyeccion : " + backupPorcentajeProyeccion);

               if (listConceptosProyecciones.get(indice).getPorcentajeproyeccion() == null) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  banderita = false;
                  listConceptosProyecciones.get(indice).setPorcentajeproyeccion(backupPorcentajeProyeccion);

               } else if (0 <= listConceptosProyecciones.get(indice).getPorcentajeproyeccion() && listConceptosProyecciones.get(indice).getPorcentajeproyeccion() <= 100) {
                  banderita = true;
               } else {
                  listConceptosProyecciones.get(indice).setPorcentajeproyeccion(backupPorcentajeProyeccion);
                  mensajeValidacion = "Porcentaje debe estar entre 0 y 100";
                  banderita = false;
               }

               if (banderita == true) {

                  if (guardado == true) {
                     guardado = false;
                  }

               } else {
                  RequestContext.getCurrentInstance().update("form:validacionModificar");
                  RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");

               }
               index = -1;
               secRegistro = null;
               RequestContext.getCurrentInstance().update("form:datosConceptosProyecciones");
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
         } else if (!crearConceptosProyecciones.contains(filtrarConceptosProyecciones.get(indice))) {
            if (filtrarConceptosProyecciones.get(indice).getPorcentajeproyeccion() == null) {
               filtrarConceptosProyecciones.get(indice).setPorcentajeproyeccion(backupPorcentajeProyeccion);
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               banderita = false;
            } else if (0 <= filtrarConceptosProyecciones.get(indice).getPorcentajeproyeccion() && filtrarConceptosProyecciones.get(indice).getPorcentajeproyeccion() <= 100) {
               banderita = true;
            } else {
               filtrarConceptosProyecciones.get(indice).setPorcentajeproyeccion(backupPorcentajeProyeccion);
               mensajeValidacion = "Porcentaje debe estar entre 0 y 100";
               banderita = false;
            }

            if (banderita == true) {
               if (modificarConceptosProyecciones.isEmpty()) {
                  modificarConceptosProyecciones.add(filtrarConceptosProyecciones.get(indice));
               } else if (!modificarConceptosProyecciones.contains(filtrarConceptosProyecciones.get(indice))) {
                  modificarConceptosProyecciones.add(filtrarConceptosProyecciones.get(indice));
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
            if (filtrarConceptosProyecciones.get(indice).getPorcentajeproyeccion() == null) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               banderita = false;
               filtrarConceptosProyecciones.get(indice).setPorcentajeproyeccion(backupPorcentajeProyeccion);
            } else if (0 <= filtrarConceptosProyecciones.get(indice).getPorcentajeproyeccion() && filtrarConceptosProyecciones.get(indice).getPorcentajeproyeccion() <= 100) {
               banderita = true;
            } else {
               filtrarConceptosProyecciones.get(indice).setPorcentajeproyeccion(backupPorcentajeProyeccion);
               mensajeValidacion = "Porcentaje debe estar entre 0 y 100";
               banderita = false;
            }

            if (banderita == true) {
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
         RequestContext.getCurrentInstance().update("form:datosConceptosProyecciones");
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      } else if (confirmarCambio.equalsIgnoreCase("CONCEPTOS")) {
         log.info("MODIFICANDO CONCEPTOS: " + listConceptosProyecciones.get(indice).getConcepto().getDescripcion());
         if (!listConceptosProyecciones.get(indice).getConcepto().getDescripcion().equals("")) {
            if (tipoLista == 0) {
               listConceptosProyecciones.get(indice).getConcepto().setDescripcion(backupConcepto);
            } else {
               listConceptosProyecciones.get(indice).getConcepto().setDescripcion(backupConcepto);
            }

            for (int i = 0; i < lovConceptos.size(); i++) {
               if (lovConceptos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }

            if (coincidencias == 1) {
               if (tipoLista == 0) {
                  listConceptosProyecciones.get(indice).setConcepto(lovConceptos.get(indiceUnicoElemento));
               } else {
                  filtrarConceptosProyecciones.get(indice).setConcepto(lovConceptos.get(indiceUnicoElemento));
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
                  listConceptosProyecciones.get(index).getConcepto().setDescripcion(backupConcepto);
               } else {
                  filtrarConceptosProyecciones.get(index).getConcepto().setDescripcion(backupConcepto);
               }
            }
            tipoActualizacion = 0;
            log.info("PAIS ANTES DE MOSTRAR DIALOGO CONCEPTOS : " + backupConcepto);
            RequestContext.getCurrentInstance().update("form:conceptosDialogo");
            RequestContext.getCurrentInstance().execute("PF('conceptosDialogo').show()");
         }

         if (coincidencias == 1) {
            if (tipoLista == 0) {
               if (!crearConceptosProyecciones.contains(listConceptosProyecciones.get(indice))) {

                  if (modificarConceptosProyecciones.isEmpty()) {
                     modificarConceptosProyecciones.add(listConceptosProyecciones.get(indice));
                  } else if (!modificarConceptosProyecciones.contains(listConceptosProyecciones.get(indice))) {
                     modificarConceptosProyecciones.add(listConceptosProyecciones.get(indice));
                  }
                  if (guardado == true) {
                     guardado = false;
                  }
               }
               index = -1;
               secRegistro = null;
            } else {
               if (!crearConceptosProyecciones.contains(filtrarConceptosProyecciones.get(indice))) {

                  if (modificarConceptosProyecciones.isEmpty()) {
                     modificarConceptosProyecciones.add(filtrarConceptosProyecciones.get(indice));
                  } else if (!modificarConceptosProyecciones.contains(filtrarConceptosProyecciones.get(indice))) {
                     modificarConceptosProyecciones.add(filtrarConceptosProyecciones.get(indice));
                  }
                  if (guardado == true) {
                     guardado = false;
                  }
               }
               index = -1;
               secRegistro = null;
            }
         }

         RequestContext.getCurrentInstance().update("form:datosConceptosProyecciones");
         RequestContext.getCurrentInstance().update("form:ACEPTAR");

      }

   }

   public void borrandoConceptosProyecciones() {

      if (index >= 0) {
         if (tipoLista == 0) {
            log.info("Entro a borrandoConceptosProyecciones");
            if (!modificarConceptosProyecciones.isEmpty() && modificarConceptosProyecciones.contains(listConceptosProyecciones.get(index))) {
               int modIndex = modificarConceptosProyecciones.indexOf(listConceptosProyecciones.get(index));
               modificarConceptosProyecciones.remove(modIndex);
               borrarConceptosProyecciones.add(listConceptosProyecciones.get(index));
            } else if (!crearConceptosProyecciones.isEmpty() && crearConceptosProyecciones.contains(listConceptosProyecciones.get(index))) {
               int crearIndex = crearConceptosProyecciones.indexOf(listConceptosProyecciones.get(index));
               crearConceptosProyecciones.remove(crearIndex);
            } else {
               borrarConceptosProyecciones.add(listConceptosProyecciones.get(index));
            }
            listConceptosProyecciones.remove(index);
         }
         if (tipoLista == 1) {
            log.info("borrandoConceptosProyecciones ");
            if (!modificarConceptosProyecciones.isEmpty() && modificarConceptosProyecciones.contains(filtrarConceptosProyecciones.get(index))) {
               int modIndex = modificarConceptosProyecciones.indexOf(filtrarConceptosProyecciones.get(index));
               modificarConceptosProyecciones.remove(modIndex);
               borrarConceptosProyecciones.add(filtrarConceptosProyecciones.get(index));
            } else if (!crearConceptosProyecciones.isEmpty() && crearConceptosProyecciones.contains(filtrarConceptosProyecciones.get(index))) {
               int crearIndex = crearConceptosProyecciones.indexOf(filtrarConceptosProyecciones.get(index));
               crearConceptosProyecciones.remove(crearIndex);
            } else {
               borrarConceptosProyecciones.add(filtrarConceptosProyecciones.get(index));
            }
            int VCIndex = listConceptosProyecciones.indexOf(filtrarConceptosProyecciones.get(index));
            listConceptosProyecciones.remove(VCIndex);

            filtrarConceptosProyecciones.remove(index);

         }
         RequestContext context = RequestContext.getCurrentInstance();
         if (listConceptosProyecciones == null || listConceptosProyecciones.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
         } else {
            infoRegistro = "Cantidad de registros: " + listConceptosProyecciones.size();
         }
         RequestContext.getCurrentInstance().update("form:informacionRegistro");
         RequestContext.getCurrentInstance().update("form:datosConceptosProyecciones");
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
            nuevoYduplicarCompletarCargo = nuevoConceptosProyecciones.getConcepto().getDescripcion();
         } else if (tipoNuevo == 2) {
            nuevoYduplicarCompletarCargo = duplicarConceptosProyecciones.getConcepto().getDescripcion();
         }
         log.info("CONCEPTOS : " + nuevoYduplicarCompletarCargo);
      }

   }

   public void autocompletarNuevo(String confirmarCambio, String valorConfirmar, int tipoNuevo) {

      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      if (confirmarCambio.equalsIgnoreCase("CONCEPTOS")) {
         log.info(" nueva Ciudad    Entro al if 'Centro costo'");
         log.info("NUEVO PERSONA :-------> " + nuevoConceptosProyecciones.getConcepto().getDescripcion());

         if (!nuevoConceptosProyecciones.getConcepto().getDescripcion().equals("")) {
            log.info("ENTRO DONDE NO TENIA QUE ENTRAR");
            log.info("valorConfirmar: " + valorConfirmar);
            log.info("nuevoYduplicarCompletarPersona: " + nuevoYduplicarCompletarCargo);
            nuevoConceptosProyecciones.getConcepto().setDescripcion(nuevoYduplicarCompletarCargo);
            for (int i = 0; i < lovConceptos.size(); i++) {
               if (lovConceptos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            log.info("Coincidencias: " + coincidencias);
            if (coincidencias == 1) {
               nuevoConceptosProyecciones.setConcepto(lovConceptos.get(indiceUnicoElemento));
               lovConceptos = null;
               log.error("CONCEPTOS GUARDADA :-----> " + nuevoConceptosProyecciones.getConcepto().getDescripcion());
            } else {
               RequestContext.getCurrentInstance().update("form:conceptosDialogo");
               RequestContext.getCurrentInstance().execute("PF('conceptosDialogo').show()");
               tipoActualizacion = tipoNuevo;
            }
         } else {
            nuevoConceptosProyecciones.getConcepto().setDescripcion(nuevoYduplicarCompletarCargo);
            log.info("valorConfirmar cuando es vacio: " + valorConfirmar);
            nuevoConceptosProyecciones.setConcepto(new Conceptos());
            nuevoConceptosProyecciones.getConcepto().setDescripcion(" ");
            log.info("NUEVO CONCEPTOS " + nuevoConceptosProyecciones.getConcepto().getDescripcion());
         }
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevoCargo");
      }

   }

   public void asignarVariableBancos(int tipoNuevo) {
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

   public void autocompletarDuplicado(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
      log.info("DUPLICAR entrooooooooooooooooooooooooooooooooooooooooooooooooooooooo!!!");
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      if (confirmarCambio.equalsIgnoreCase("CONCEPTOS")) {
         log.info("DUPLICAR valorConfirmar : " + valorConfirmar);
         log.info("DUPLICAR CIUDAD bkp : " + nuevoYduplicarCompletarCargo);

         if (!duplicarConceptosProyecciones.getConcepto().getDescripcion().equals("")) {
            log.info("DUPLICAR ENTRO DONDE NO TENIA QUE ENTRAR");
            log.info("DUPLICAR valorConfirmar: " + valorConfirmar);
            log.info("DUPLICAR nuevoTipoCCAutoCompletar: " + nuevoYduplicarCompletarCargo);
            duplicarConceptosProyecciones.getConcepto().setDescripcion(nuevoYduplicarCompletarCargo);
            for (int i = 0; i < lovConceptos.size(); i++) {
               if (lovConceptos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            log.info("Coincidencias: " + coincidencias);
            if (coincidencias == 1) {
               duplicarConceptosProyecciones.setConcepto(lovConceptos.get(indiceUnicoElemento));
               lovConceptos = null;
               getLovConceptos();
            } else {
               RequestContext.getCurrentInstance().update("form:conceptosDialogo");
               RequestContext.getCurrentInstance().execute("PF('conceptosDialogo').show()");
               tipoActualizacion = tipoNuevo;
            }
         } else if (tipoNuevo == 2) {
            //duplicarConceptosProyecciones.getEmpresa().setDescripcion(nuevoYduplicarCompletarPais);
            log.info("DUPLICAR valorConfirmar cuando es vacio: " + valorConfirmar);
            log.info("DUPLICAR INDEX : " + index);
            duplicarConceptosProyecciones.setConcepto(new Conceptos());
            duplicarConceptosProyecciones.getConcepto().setDescripcion(" ");

            log.info("DUPLICAR CONCEPTOS  : " + duplicarConceptosProyecciones.getConcepto().getDescripcion());
            log.info("nuevoYduplicarCompletarCONCEPTOS : " + nuevoYduplicarCompletarCargo);
            if (tipoLista == 0) {
               listConceptosProyecciones.get(index).getConcepto().setDescripcion(nuevoYduplicarCompletarCargo);
               log.error("tipo lista" + tipoLista);
               log.error("Secuencia Parentesco " + listConceptosProyecciones.get(index).getConcepto().getSecuencia());
            } else if (tipoLista == 1) {
               filtrarConceptosProyecciones.get(index).getConcepto().setDescripcion(nuevoYduplicarCompletarCargo);
            }

         }
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCargo");
      }
   }

   public void revisarDialogoGuardar() {

      if (!borrarConceptosProyecciones.isEmpty() || !crearConceptosProyecciones.isEmpty() || !modificarConceptosProyecciones.isEmpty()) {
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:confirmarGuardar");
         RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
      }

   }

   public void guardarConceptosProyecciones() {
      RequestContext context = RequestContext.getCurrentInstance();

      if (guardado == false) {
         log.info("Realizando guardarConceptosProyecciones");
         if (!borrarConceptosProyecciones.isEmpty()) {
            administrarConceptosProyecciones.borrarConceptosProyecciones(borrarConceptosProyecciones);
            //mostrarBorrados
            registrosBorrados = borrarConceptosProyecciones.size();
            RequestContext.getCurrentInstance().update("form:mostrarBorrados");
            RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
            borrarConceptosProyecciones.clear();
         }
         if (!modificarConceptosProyecciones.isEmpty()) {
            administrarConceptosProyecciones.modificarConceptosProyecciones(modificarConceptosProyecciones);
            modificarConceptosProyecciones.clear();
         }
         if (!crearConceptosProyecciones.isEmpty()) {
            administrarConceptosProyecciones.crearConceptosProyecciones(crearConceptosProyecciones);
            crearConceptosProyecciones.clear();
         }
         log.info("Se guardaron los datos con exito");
         listConceptosProyecciones = null;
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
            editarConceptosProyecciones = listConceptosProyecciones.get(index);
         }
         if (tipoLista == 1) {
            editarConceptosProyecciones = filtrarConceptosProyecciones.get(index);
         }

         RequestContext context = RequestContext.getCurrentInstance();
         log.info("Entro a editar... valor celda: " + cualCelda);
         if (cualCelda == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editPorcentaje");
            RequestContext.getCurrentInstance().execute("PF('editPorcentaje').show()");
            cualCelda = -1;
         } else if (cualCelda == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editConcepto");
            RequestContext.getCurrentInstance().execute("PF('editConcepto').show()");
            cualCelda = -1;
         }

      }
      index = -1;
      secRegistro = null;
   }

   public void agregarNuevoConceptosProyecciones() {
      log.info("agregarNuevoConceptosProyecciones");
      int contador = 0;
      int duplicados = 0;

      Integer a = 0;
      a = null;
      mensajeValidacion = " ";
      RequestContext context = RequestContext.getCurrentInstance();
      if (nuevoConceptosProyecciones.getPorcentajeproyeccion() == null) {
         mensajeValidacion = " *Porcentaje \n";
         log.info("Mensaje validacion : " + mensajeValidacion);
      } else if (0 <= nuevoConceptosProyecciones.getPorcentajeproyeccion() && nuevoConceptosProyecciones.getPorcentajeproyeccion() <= 100) {
         contador++;
      } else {
         mensajeValidacion += "*Porcentaje debe estar entre 0 y 100. ";
      }
      if (nuevoConceptosProyecciones.getConcepto().getDescripcion() == null) {
         mensajeValidacion = mensajeValidacion + " *Concepto \n";
         log.info("Mensaje validacion : " + mensajeValidacion);

      } else {
         for (int i = 0; i < listConceptosProyecciones.size(); i++) {
            if (listConceptosProyecciones.get(i).getConcepto().getDescripcion().equals(nuevoConceptosProyecciones.getConcepto().getDescripcion())) {
               duplicados++;
            }
         }
         if (duplicados == 0) {
            log.info("bandera");
            contador++;
         } else {
            mensajeValidacion += "*Concepto ya Insertado.";
         }

      }

      if (contador == 2) {
         if (bandera == 1) {
            //CERRAR FILTRADO
            FacesContext c = FacesContext.getCurrentInstance();
            log.info("Desactivar");
            porcentaje = (Column) c.getViewRoot().findComponent("form:datosConceptosProyecciones:porcentaje");
            porcentaje.setFilterStyle("display: none; visibility: hidden;");
            concepto = (Column) c.getViewRoot().findComponent("form:datosConceptosProyecciones:concepto");
            concepto.setFilterStyle("display: none; visibility: hidden;");
            bandera = 0;
            filtrarConceptosProyecciones = null;
            tipoLista = 0;
         }
         log.info("Despues de la bandera");

         k++;
         l = BigInteger.valueOf(k);
         nuevoConceptosProyecciones.setSecuencia(l);

         crearConceptosProyecciones.add(nuevoConceptosProyecciones);

         listConceptosProyecciones.add(nuevoConceptosProyecciones);
         nuevoConceptosProyecciones = new ConceptosProyecciones();
         nuevoConceptosProyecciones.setConcepto(new Conceptos());
         RequestContext.getCurrentInstance().update("form:datosConceptosProyecciones");
         infoRegistro = "Cantidad de registros: " + listConceptosProyecciones.size();
         RequestContext.getCurrentInstance().update("form:informacionRegistro");
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }

         RequestContext.getCurrentInstance().execute("PF('nuevoRegistroConceptosProyecciones').hide()");
         RequestContext.getCurrentInstance().update("nuevoRegistroConceptosProyecciones').hide()");
         index = -1;
         secRegistro = null;
      } else {
         RequestContext.getCurrentInstance().update("form:validacionNuevaCentroCosto");
         RequestContext.getCurrentInstance().execute("PF('validacionNuevaCentroCosto').show()");
         contador = 0;
      }
   }

   public void limpiarNuevoConceptosProyecciones() {
      log.info("limpiarNuevoConceptosProyecciones");
      nuevoConceptosProyecciones = new ConceptosProyecciones();
      nuevoConceptosProyecciones.setConcepto(new Conceptos());
      secRegistro = null;
      index = -1;

   }

   //------------------------------------------------------------------------------
   public void cargarNuevoConceptosProyecciones() {
      log.info("cargarNuevoConceptosProyecciones");

      duplicarConceptosProyecciones = new ConceptosProyecciones();
      duplicarConceptosProyecciones.setConcepto(new Conceptos());
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().execute("PF('nuevoRegistroConceptosProyecciones').show()");
      RequestContext.getCurrentInstance().update("formularioDialogos:nuevaConcepotProyeccionDG");

   }

   public void duplicandoConceptosProyecciones() {
      log.info("duplicandoConceptosProyecciones");
      if (index >= 0) {
         duplicarConceptosProyecciones = new ConceptosProyecciones();
         duplicarConceptosProyecciones.setConcepto(new Conceptos());
         k++;
         l = BigInteger.valueOf(k);

         if (tipoLista == 0) {
            duplicarConceptosProyecciones.setSecuencia(l);
            duplicarConceptosProyecciones.setPorcentajeproyeccion(listConceptosProyecciones.get(index).getPorcentajeproyeccion());
            duplicarConceptosProyecciones.setConcepto(listConceptosProyecciones.get(index).getConcepto());
         }
         if (tipoLista == 1) {
            duplicarConceptosProyecciones.setSecuencia(l);
            duplicarConceptosProyecciones.setPorcentajeproyeccion(filtrarConceptosProyecciones.get(index).getPorcentajeproyeccion());
            duplicarConceptosProyecciones.setConcepto(filtrarConceptosProyecciones.get(index).getConcepto());

         }

         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTE");
         RequestContext.getCurrentInstance().execute("PF('duplicarRegistroConceptosProyecciones').show()");
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
      log.error("ConfirmarDuplicar porcentaje " + duplicarConceptosProyecciones.getPorcentajeproyeccion());

      if (duplicarConceptosProyecciones.getPorcentajeproyeccion() == null) {
         mensajeValidacion = mensajeValidacion + "   *Porcentaje \n";
         log.info("Mensaje validacion : " + mensajeValidacion);
      } else if (0 <= duplicarConceptosProyecciones.getPorcentajeproyeccion() && duplicarConceptosProyecciones.getPorcentajeproyeccion() <= 100) {
         contador++;
      } else {
         mensajeValidacion += "*Porcentaje debe estar entre 0 y 100. ";
      }
      if (duplicarConceptosProyecciones.getConcepto().getDescripcion() == null) {
         mensajeValidacion = mensajeValidacion + " *Concepto \n";
         log.info("Mensaje validacion : " + mensajeValidacion);

      } else {
         for (int i = 0; i < listConceptosProyecciones.size(); i++) {
            if (listConceptosProyecciones.get(i).getConcepto().getDescripcion().equals(duplicarConceptosProyecciones.getConcepto().getDescripcion())) {
               duplicados++;
            }
         }
         if (duplicados == 0) {
            log.info("bandera");
            contador++;
         } else {
            mensajeValidacion += "*Concepto ya Insertado";
         }

      }

      if (contador == 2) {

         log.info("Datos Duplicando: " + duplicarConceptosProyecciones.getSecuencia() + "  " + duplicarConceptosProyecciones.getPorcentajeproyeccion());
         if (crearConceptosProyecciones.contains(duplicarConceptosProyecciones)) {
            log.info("Ya lo contengo.");
         }
         listConceptosProyecciones.add(duplicarConceptosProyecciones);
         crearConceptosProyecciones.add(duplicarConceptosProyecciones);
         RequestContext.getCurrentInstance().update("form:datosConceptosProyecciones");
         index = -1;
         log.info("--------------DUPLICAR------------------------");
         log.info("CODIGO : " + duplicarConceptosProyecciones.getPorcentajeproyeccion());
         log.info("CONCEPTOS : " + duplicarConceptosProyecciones.getConcepto().getDescripcion());
         log.info("--------------DUPLICAR------------------------");

         secRegistro = null;
         if (guardado == true) {
            guardado = false;
         }
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         infoRegistro = "Cantidad de registros: " + listConceptosProyecciones.size();
         RequestContext.getCurrentInstance().update("form:informacionRegistro");
         if (bandera == 1) {
            //CERRAR FILTRADO
            FacesContext c = FacesContext.getCurrentInstance();
            porcentaje = (Column) c.getViewRoot().findComponent("form:datosConceptosProyecciones:porcentaje");
            porcentaje.setFilterStyle("display: none; visibility: hidden;");
            concepto = (Column) c.getViewRoot().findComponent("form:datosConceptosProyecciones:concepto");
            concepto.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosConceptosProyecciones");
            bandera = 0;
            filtrarConceptosProyecciones = null;
            tipoLista = 0;
         }
         duplicarConceptosProyecciones = new ConceptosProyecciones();
         duplicarConceptosProyecciones.setConcepto(new Conceptos());

         RequestContext.getCurrentInstance().execute("duplicarRegistroConceptosProyecciones').hide()");
         RequestContext.getCurrentInstance().update("duplicarRegistroConceptosProyecciones').hide()");

      } else {
         contador = 0;
         RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
         RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
      }
   }

   public void limpiarDuplicarConceptosProyecciones() {
      duplicarConceptosProyecciones = new ConceptosProyecciones();
      duplicarConceptosProyecciones.setConcepto(new Conceptos());
   }

   public void exportPDF() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosConceptosProyeccionesExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "CONCEPTOSPROYECCIONES", false, false, "UTF-8", null, null);
      context.responseComplete();
      index = -1;
      secRegistro = null;
   }

   public void exportXLS() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosConceptosProyeccionesExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "CONCEPTOSPROYECCIONES", false, false, "UTF-8", null, null);
      context.responseComplete();
      index = -1;
      secRegistro = null;
   }

   public void verificarRastro() {
      RequestContext context = RequestContext.getCurrentInstance();
      log.info("lol");
      if (!listConceptosProyecciones.isEmpty()) {
         if (secRegistro != null) {
            log.info("lol 2");
            int resultado = administrarRastros.obtenerTabla(secRegistro, "CONCEPTOSPROYECCIONES"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
      } else if (administrarRastros.verificarHistoricosTabla("CONCEPTOSPROYECCIONES")) { // igual acá
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
      index = -1;
   }

   //*/*/*/*/*/*/*/*/*/*-/-*//-*/-*/*/*-*/-*/-*/*/*/*/*/---/*/*/*/*/-*/-*/-*/-*/-*/
   public List<ConceptosProyecciones> getListConceptosProyecciones() {
      if (listConceptosProyecciones == null) {
         listConceptosProyecciones = administrarConceptosProyecciones.consultarConceptosProyecciones();
      }
      RequestContext context = RequestContext.getCurrentInstance();
      if (listConceptosProyecciones == null || listConceptosProyecciones.isEmpty()) {
         infoRegistro = "Cantidad de registros: 0 ";
      } else {
         infoRegistro = "Cantidad de registros: " + listConceptosProyecciones.size();
      }
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
      return listConceptosProyecciones;
   }

   public void setListConceptosProyecciones(List<ConceptosProyecciones> listConceptosProyecciones) {
      this.listConceptosProyecciones = listConceptosProyecciones;
   }

   public List<ConceptosProyecciones> getFiltrarConceptosProyecciones() {
      return filtrarConceptosProyecciones;
   }

   public void setFiltrarConceptosProyecciones(List<ConceptosProyecciones> filtrarConceptosProyecciones) {
      this.filtrarConceptosProyecciones = filtrarConceptosProyecciones;
   }

   public ConceptosProyecciones getNuevoConceptosProyecciones() {
      return nuevoConceptosProyecciones;
   }

   public void setNuevoConceptosProyecciones(ConceptosProyecciones nuevoConceptosProyecciones) {
      this.nuevoConceptosProyecciones = nuevoConceptosProyecciones;
   }

   public ConceptosProyecciones getDuplicarConceptosProyecciones() {
      return duplicarConceptosProyecciones;
   }

   public void setDuplicarConceptosProyecciones(ConceptosProyecciones duplicarConceptosProyecciones) {
      this.duplicarConceptosProyecciones = duplicarConceptosProyecciones;
   }

   public ConceptosProyecciones getEditarConceptosProyecciones() {
      return editarConceptosProyecciones;
   }

   public void setEditarConceptosProyecciones(ConceptosProyecciones editarConceptosProyecciones) {
      this.editarConceptosProyecciones = editarConceptosProyecciones;
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
   private String infoRegistroBancos;

   private String infoRegistroConceptos;

   public List<Conceptos> getLovConceptos() {
      if (lovConceptos == null) {
         lovConceptos = administrarConceptosProyecciones.consultarLOVConceptos();
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

   public void setConceptoSeleccionado(Conceptos cargoSeleccionado) {
      this.conceptoSeleccionado = cargoSeleccionado;
   }

   public ConceptosProyecciones getSucursalSeleccionada() {
      return sucursalSeleccionada;
   }

   public void setSucursalSeleccionada(ConceptosProyecciones sucursalSeleccionada) {
      this.sucursalSeleccionada = sucursalSeleccionada;
   }

   public String getInfoRegistro() {
      return infoRegistro;
   }

   public void setInfoRegistro(String infoRegistro) {
      this.infoRegistro = infoRegistro;
   }

   public String getInfoRegistroBancos() {
      return infoRegistroBancos;
   }

   public void setInfoRegistroBancos(String infoRegistroBancos) {
      this.infoRegistroBancos = infoRegistroBancos;
   }

   public String getInfoRegistroConceptos() {
      return infoRegistroConceptos;
   }

   public void setInfoRegistroConceptos(String infoRegistroConceptos) {
      this.infoRegistroConceptos = infoRegistroConceptos;
   }

   public boolean isAceptar() {
      return aceptar;
   }

   public void setAceptar(boolean aceptar) {
      this.aceptar = aceptar;
   }

}
