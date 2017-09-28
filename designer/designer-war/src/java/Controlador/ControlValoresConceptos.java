/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.ValoresConceptos;
import Entidades.Conceptos;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarValoresConceptosInterface;
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
public class ControlValoresConceptos implements Serializable {

   private static Logger log = Logger.getLogger(ControlValoresConceptos.class);

   @EJB
   AdministrarValoresConceptosInterface administrarValoresConceptos;
   @EJB
   AdministrarRastrosInterface administrarRastros;
   private List<ValoresConceptos> listValoresConceptos;
   private List<ValoresConceptos> filtrarValoresConceptos;
   private List<ValoresConceptos> crearValoresConceptos;
   private List<ValoresConceptos> modificarValoresConceptos;
   private List<ValoresConceptos> borrarValoresConceptos;
   private ValoresConceptos nuevoValoresConceptos;
   private ValoresConceptos duplicarValoresConceptos;
   private ValoresConceptos editarValoresConceptos;
   private ValoresConceptos valorConceptoSeleccionado;
   //otros
   private int cualCelda, tipoLista, index, tipoActualizacion, k, bandera;
   private BigInteger l;
   private boolean aceptar, guardado;
   //AutoCompletar
   private boolean permitirIndex;
   //RASTRO
   private BigInteger secRegistro;
   private Column codigo, personafir, cargo;
   //borrado
   private int registrosBorrados;
   private String mensajeValidacion;
   //filtrado table
   private int tamano;
   private BigInteger backupCodigo;
   private Integer backupValorVoluntario;
   private String backupPais;
   //---------------------------------
   private String backupConcepto;
   private List<Conceptos> lovConceptos;
   private List<Conceptos> filtradoConceptos;
   private Conceptos conceptolovSeleccionado;
   private String nuevoYduplicarCompletarPersona;
   //--------------------------------------
   private String nuevoYduplicarCompletarCargo;

   //---------------------------------
   private List<ValoresConceptos> lovValoresConceptos;
   private List<ValoresConceptos> filterValoresConceptosBoton;
   private ValoresConceptos conceptoSoporteSeleccionado;
   private String paginaAnterior;
   private Map<String, Object> mapParametros;

   public ControlValoresConceptos() {
      lovValoresConceptos = null;
      listValoresConceptos = null;
      crearValoresConceptos = new ArrayList<ValoresConceptos>();
      modificarValoresConceptos = new ArrayList<ValoresConceptos>();
      borrarValoresConceptos = new ArrayList<ValoresConceptos>();
      permitirIndex = true;
      editarValoresConceptos = new ValoresConceptos();
      nuevoValoresConceptos = new ValoresConceptos();
      nuevoValoresConceptos.setConcepto(new Conceptos());
      duplicarValoresConceptos = new ValoresConceptos();
      duplicarValoresConceptos.setConcepto(new Conceptos());
      lovConceptos = null;
      filtradoConceptos = null;
      guardado = true;
      tamano = 270;
      aceptar = true;
      paginaAnterior = "nominaf";
      mapParametros = new LinkedHashMap<String, Object>();
      mapParametros.put("paginaAnterior", paginaAnterior);
   }

   public void limpiarListasValor() {
      lovConceptos = null;
      lovValoresConceptos = null;
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
         administrarValoresConceptos.obtenerConexion(ses.getId());
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

   public void navegar(String pag) {
      FacesContext fc = FacesContext.getCurrentInstance();
      ControlListaNavegacion controlListaNavegacion = (ControlListaNavegacion) fc.getApplication().evaluateExpressionGet(fc, "#{controlListaNavegacion}", ControlListaNavegacion.class);
      String pagActual = "valoresconceptos";
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
         //           controlRastro.recibirDatosTabla(conceptolovSeleccionado.getSecuencia(), "Conceptos", pagActual);
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
         log.info("\n ENTRE A ControlValoresConceptos.eventoFiltrar \n");
         if (tipoLista == 0) {
            tipoLista = 1;
         }
         RequestContext context = RequestContext.getCurrentInstance();
         infoRegistro = "Cantidad de registros: " + filtrarValoresConceptos.size();
         RequestContext.getCurrentInstance().update("form:informacionRegistro");
      } catch (Exception e) {
         log.warn("Error ControlValoresConceptos eventoFiltrar ERROR===" + e.getMessage());
      }
   }

   public void cambiarIndice(int indice, int celda) {
      log.error("TIPO LISTA = " + tipoLista);

      if (permitirIndex == true) {
         index = indice;
         cualCelda = celda;
         secRegistro = listValoresConceptos.get(index).getSecuencia();
         if (tipoLista == 0) {
            if (cualCelda == 0) {
               //conceptocodigo
               backupCodigo = listValoresConceptos.get(index).getConcepto().getCodigo();
            }
            if (cualCelda == 1) {
               backupConcepto = listValoresConceptos.get(index).getConcepto().getDescripcion();
               log.info("CONCEPTO : " + backupConcepto);

            }
            if (cualCelda == 2) {
               backupValorVoluntario = listValoresConceptos.get(index).getValorunitario();
               log.info("VALOR VOLUNTARIO : " + backupConcepto);

            }

         } else if (tipoLista == 1) {
            if (cualCelda == 0) {
               backupCodigo = filtrarValoresConceptos.get(index).getConcepto().getCodigo();
            }
            if (cualCelda == 1) {
               backupConcepto = filtrarValoresConceptos.get(index).getConcepto().getDescripcion();
               log.info("CONCEPTO : " + backupConcepto);
            }
            if (cualCelda == 2) {
               backupValorVoluntario = filtrarValoresConceptos.get(index).getValorunitario();
               log.info("VALOR VOLUNTARIO : " + backupConcepto);

            }
         }

      }
      log.info("Indice: " + index + " Celda: " + cualCelda);
   }

   public void asignarIndex(Integer indice, int LND, int dig) {
      try {
         RequestContext context = RequestContext.getCurrentInstance();
         log.info("\n ENTRE A ControlValoresConceptos.asignarIndex \n");
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
            RequestContext.getCurrentInstance().update("form:personasDialogo");
            RequestContext.getCurrentInstance().execute("PF('personasDialogo').show()");
            dig = -1;
         }
         if (dig == 1) {
            RequestContext.getCurrentInstance().update("form:personasDialogo");
            RequestContext.getCurrentInstance().execute("PF('personasDialogo').show()");
            dig = -1;
         }

      } catch (Exception e) {
         log.warn("Error ControlValoresConceptos.asignarIndex ERROR======" + e.getMessage());
      }
   }

   public void activarAceptar() {
      aceptar = false;
   }

   public void listaValoresBoton() {
      if (index >= 0) {

         if (cualCelda == 0) {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:personasDialogo");
            RequestContext.getCurrentInstance().execute("PF('personasDialogo').show()");
         }
         if (cualCelda == 1) {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:personasDialogo");
            RequestContext.getCurrentInstance().execute("PF('personasDialogo').show()");
         }
      }
   }

   public void cancelarModificacion() {
      if (bandera == 1) {
         FacesContext c = FacesContext.getCurrentInstance();
         //CERRAR FILTRADO
         codigo = (Column) c.getViewRoot().findComponent("form:datosValoresConceptos:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         personafir = (Column) c.getViewRoot().findComponent("form:datosValoresConceptos:personafir");
         personafir.setFilterStyle("display: none; visibility: hidden;");
         cargo = (Column) c.getViewRoot().findComponent("form:datosValoresConceptos:cargo");
         cargo.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosValoresConceptos");
         bandera = 0;
         filtrarValoresConceptos = null;
         tipoLista = 0;
      }

      borrarValoresConceptos.clear();
      crearValoresConceptos.clear();
      modificarValoresConceptos.clear();
      index = -1;
      secRegistro = null;
      k = 0;
      listValoresConceptos = null;
      guardado = true;
      permitirIndex = true;
      getListValoresConceptos();
      RequestContext context = RequestContext.getCurrentInstance();
      if (listValoresConceptos == null || listValoresConceptos.isEmpty()) {
         infoRegistro = "Cantidad de registros: 0 ";
      } else {
         infoRegistro = "Cantidad de registros: " + listValoresConceptos.size();
      }
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
      RequestContext.getCurrentInstance().update("form:datosValoresConceptos");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void salir() {
      limpiarListasValor();
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 1) {
         //CERRAR FILTRADO
         codigo = (Column) c.getViewRoot().findComponent("form:datosValoresConceptos:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         personafir = (Column) c.getViewRoot().findComponent("form:datosValoresConceptos:personafir");
         personafir.setFilterStyle("display: none; visibility: hidden;");
         cargo = (Column) c.getViewRoot().findComponent("form:datosValoresConceptos:cargo");
         cargo.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosValoresConceptos");
         bandera = 0;
         filtrarValoresConceptos = null;
         tipoLista = 0;
      }

      borrarValoresConceptos.clear();
      crearValoresConceptos.clear();
      modificarValoresConceptos.clear();
      index = -1;
      secRegistro = null;
      k = 0;
      listValoresConceptos = null;
      guardado = true;
      permitirIndex = true;
      getListValoresConceptos();
      RequestContext context = RequestContext.getCurrentInstance();
      if (listValoresConceptos == null || listValoresConceptos.isEmpty()) {
         infoRegistro = "Cantidad de registros: 0 ";
      } else {
         infoRegistro = "Cantidad de registros: " + listValoresConceptos.size();
      }
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
      RequestContext.getCurrentInstance().update("form:datosValoresConceptos");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      navegar("atras");
   }

   public void activarCtrlF11() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 0) {
         tamano = 250;
         codigo = (Column) c.getViewRoot().findComponent("form:datosValoresConceptos:codigo");
         codigo.setFilterStyle("width: 85% !important;");
         personafir = (Column) c.getViewRoot().findComponent("form:datosValoresConceptos:personafir");
         personafir.setFilterStyle("width: 85% !important");
         cargo = (Column) c.getViewRoot().findComponent("form:datosValoresConceptos:cargo");
         cargo.setFilterStyle("width: 85% !important;");
         RequestContext.getCurrentInstance().update("form:datosValoresConceptos");
         log.info("Activar");
         bandera = 1;
      } else if (bandera == 1) {
         log.info("Desactivar");
         tamano = 270;
         codigo = (Column) c.getViewRoot().findComponent("form:datosValoresConceptos:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         personafir = (Column) c.getViewRoot().findComponent("form:datosValoresConceptos:personafir");
         personafir.setFilterStyle("display: none; visibility: hidden;");
         cargo = (Column) c.getViewRoot().findComponent("form:datosValoresConceptos:cargo");
         cargo.setFilterStyle("display: none; visibility: hidden;");

         RequestContext.getCurrentInstance().update("form:datosValoresConceptos");
         bandera = 0;
         filtrarValoresConceptos = null;
         tipoLista = 0;
      }
   }

   public void actualizarConceptos() {
      RequestContext context = RequestContext.getCurrentInstance();
      log.info("concepto seleccionado : " + conceptolovSeleccionado.getDescripcion());
      log.info("tipo Actualizacion : " + tipoActualizacion);
      log.info("tipo Lista : " + tipoLista);
      int contadorConcepto = 0;
      if (tipoActualizacion == 0) {
         if (tipoLista == 0) {
            for (int i = 0; i < listValoresConceptos.size(); i++) {
               if (conceptolovSeleccionado.getSecuencia().equals(lovConceptos.get(i).getSecuencia())) {
                  contadorConcepto++;
               }
            }
            if (contadorConcepto == 0) {
               listValoresConceptos.get(index).setConcepto(conceptolovSeleccionado);

               if (!crearValoresConceptos.contains(listValoresConceptos.get(index))) {
                  if (modificarValoresConceptos.isEmpty()) {
                     modificarValoresConceptos.add(listValoresConceptos.get(index));
                  } else if (!modificarValoresConceptos.contains(listValoresConceptos.get(index))) {
                     modificarValoresConceptos.add(listValoresConceptos.get(index));
                  }
               }
               if (guardado == true) {
                  guardado = false;
               }
               permitirIndex = true;
               log.info("ACTUALIZAR CONCEPTO SELECCIONADO : " + conceptolovSeleccionado.getDescripcion());
               RequestContext.getCurrentInstance().update("form:datosValoresConceptos");
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            } else {
               mensajeValidacion = "CONCEPTO REPETIDO";
               RequestContext.getCurrentInstance().update("form:validacionModificar");
               RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");
            }
         } else {
            for (int i = 0; i < filtrarValoresConceptos.size(); i++) {
               if (conceptolovSeleccionado.getSecuencia().equals(filtrarValoresConceptos.get(i).getSecuencia())) {
                  contadorConcepto++;
               }
            }
            if (contadorConcepto == 0) {
               filtrarValoresConceptos.get(index).setConcepto(conceptolovSeleccionado);
               if (!crearValoresConceptos.contains(filtrarValoresConceptos.get(index))) {
                  if (modificarValoresConceptos.isEmpty()) {
                     modificarValoresConceptos.add(filtrarValoresConceptos.get(index));
                  } else if (!modificarValoresConceptos.contains(filtrarValoresConceptos.get(index))) {
                     modificarValoresConceptos.add(filtrarValoresConceptos.get(index));
                  }
               }
               if (guardado == true) {
                  guardado = false;
               }
               permitirIndex = true;
               log.info("ACTUALIZAR CONCEPTO SELECCIONADO : " + conceptolovSeleccionado.getDescripcion());
               RequestContext.getCurrentInstance().update("form:datosValoresConceptos");
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            } else {
               mensajeValidacion = "CONCEPTO REPETIDO";
               RequestContext.getCurrentInstance().update("form:validacionModificar");
               RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");
            }

         }
      } else if (tipoActualizacion == 1) {
         log.info("ACTUALIZAR CONCEPTO NUEVO DEPARTAMENTO: " + conceptolovSeleccionado.getDescripcion());
         nuevoValoresConceptos.setConcepto(conceptolovSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevoPersona");
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevoCodigo");
      } else if (tipoActualizacion == 2) {
         log.info("ACTUALIZAR CONCEPTO DUPLICAR DEPARTAMENO: " + conceptolovSeleccionado.getDescripcion());
         duplicarValoresConceptos.setConcepto(conceptolovSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarPersona");
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCodigo");
      }
      filtradoConceptos = null;
      conceptolovSeleccionado = null;
      aceptar = true;
      secRegistro = null;
      tipoActualizacion = -1;
      cualCelda = -1;
      cambioValoresConceptos = true;
      context.reset("form:lovConceptos:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovConceptos').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('personasDialogo').hide()");
      RequestContext.getCurrentInstance().update("form:lovConceptos");
   }

   public void cancelarCambioConceptos() {
      filtradoConceptos = null;
      conceptolovSeleccionado = null;
      aceptar = true;
      index = -1;
      secRegistro = null;
      tipoActualizacion = -1;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("form:lovConceptos:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovConceptos').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('personasDialogo').hide()");
   }

   public void modificarValoresConceptos(int indice, String confirmarCambio, String valorConfirmar) {
      log.error("ENTRE A MODIFICAR SUB CATEGORIA");
      index = indice;
      int coincidencias = 0;
      int contador = 0;
      boolean banderita = false;
      boolean banderita1 = false;
      int contadorConceptos = 0;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      log.error("TIPO LISTA = " + tipoLista);
      if (confirmarCambio.equalsIgnoreCase("N")) {
         log.error("ENTRE A MODIFICAR EMPRESAS, CONFIRMAR CAMBIO ES N");
         if (tipoLista == 0) {
            if (!crearValoresConceptos.contains(listValoresConceptos.get(indice))) {

               log.info("backupCodigo : " + backupValorVoluntario);

               if (listValoresConceptos.get(indice).getValorunitario() == null) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  banderita = false;
                  listValoresConceptos.get(indice).setValorunitario(backupValorVoluntario);

               } else {
                  /*for (int j = 0; j < listValoresConceptos.size(); j++) {
                         if (j != indice) {
                         if (listValoresConceptos.get(indice).getValorunitario() == listValoresConceptos.get(j).getValorunitario()) {
                         contador++;
                         }
                         }
                         }

                         if (contador > 0) {
                         mensajeValidacion = "CODIGOS REPETIDOS";
                         banderita = false;
                         listValoresConceptos.get(indice).setValorunitario(backupValorVoluntario);
                         } else {*/
                  banderita = true;

               }

               if (banderita == true) {
                  if (modificarValoresConceptos.isEmpty()) {
                     modificarValoresConceptos.add(listValoresConceptos.get(indice));
                  } else if (!modificarValoresConceptos.contains(listValoresConceptos.get(indice))) {
                     modificarValoresConceptos.add(listValoresConceptos.get(indice));
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
               RequestContext.getCurrentInstance().update("form:datosValoresConceptos");
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            } else {

               log.info("backupCodigo : " + backupCodigo);

               if (listValoresConceptos.get(indice).getValorunitario() == null) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  banderita = false;
                  listValoresConceptos.get(indice).setValorunitario(backupValorVoluntario);
               } else {
                  for (int j = 0; j < listValoresConceptos.size(); j++) {
                     if (j != indice) {
                        if (listValoresConceptos.get(indice).getValorunitario() == listValoresConceptos.get(j).getValorunitario()) {
                           contador++;
                        }
                     }
                  }

                  if (contador > 0) {
                     mensajeValidacion = "CODIGOS REPETIDOS";
                     banderita = false;
                     listValoresConceptos.get(indice).setValorunitario(backupValorVoluntario);
                  } else {
                     banderita = true;
                  }

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
               RequestContext.getCurrentInstance().update("form:datosValoresConceptos");
               RequestContext.getCurrentInstance().update("form:ACEPTAR");

            }
         } else if (!crearValoresConceptos.contains(filtrarValoresConceptos.get(indice))) {
            if (filtrarValoresConceptos.get(indice).getValorunitario() == null) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               banderita = false;
               filtrarValoresConceptos.get(indice).setValorunitario(backupValorVoluntario);
            } else {
               for (int j = 0; j < filtrarValoresConceptos.size(); j++) {
                  if (j != indice) {
                     if (filtrarValoresConceptos.get(indice).getValorunitario() == listValoresConceptos.get(j).getValorunitario()) {
                        contador++;
                     }
                  }
               }
               for (int j = 0; j < listValoresConceptos.size(); j++) {
                  if (j != indice) {
                     if (filtrarValoresConceptos.get(indice).getValorunitario() == listValoresConceptos.get(j).getValorunitario()) {
                        contador++;
                     }
                  }
               }
               if (contador > 0) {
                  mensajeValidacion = "CODIGOS REPETIDOS";
                  banderita = false;
                  filtrarValoresConceptos.get(indice).setValorunitario(backupValorVoluntario);

               } else {
                  banderita = true;
               }

            }

            if (banderita == true && banderita1 == true) {
               if (modificarValoresConceptos.isEmpty()) {
                  modificarValoresConceptos.add(filtrarValoresConceptos.get(indice));
               } else if (!modificarValoresConceptos.contains(filtrarValoresConceptos.get(indice))) {
                  modificarValoresConceptos.add(filtrarValoresConceptos.get(indice));
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
            if (filtrarValoresConceptos.get(indice).getValorunitario() == null) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               banderita = false;
               filtrarValoresConceptos.get(indice).setValorunitario(backupValorVoluntario);
            } else {
               for (int j = 0; j < filtrarValoresConceptos.size(); j++) {
                  if (j != indice) {
                     if (filtrarValoresConceptos.get(indice).getValorunitario() == listValoresConceptos.get(j).getValorunitario()) {
                        contador++;
                     }
                  }
               }
               for (int j = 0; j < listValoresConceptos.size(); j++) {
                  if (j != indice) {
                     if (filtrarValoresConceptos.get(indice).getValorunitario() == listValoresConceptos.get(j).getValorunitario()) {
                        contador++;
                     }
                  }
               }
               if (contador > 0) {
                  mensajeValidacion = "CODIGOS REPETIDOS";
                  banderita = false;
                  filtrarValoresConceptos.get(indice).setValorunitario(backupValorVoluntario);

               } else {
                  banderita = true;
               }

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
         RequestContext.getCurrentInstance().update("form:datosValoresConceptos");
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      } else if (confirmarCambio.equalsIgnoreCase("CONCEPTO")) {
         log.info("MODIFICANDO NORMA LABORAL : " + listValoresConceptos.get(indice).getConcepto().getDescripcion());
         if (!listValoresConceptos.get(indice).getConcepto().getDescripcion().equals("")) {
            if (tipoLista == 0) {
               listValoresConceptos.get(indice).getConcepto().setDescripcion(backupConcepto);
            } else {
               listValoresConceptos.get(indice).getConcepto().setDescripcion(backupConcepto);
            }

            for (int i = 0; i < lovConceptos.size(); i++) {
               if (lovConceptos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }

            if (coincidencias == 1) {
               if (tipoLista == 0) {
                  for (int j = 0; j < listValoresConceptos.size(); j++) {
                     if (j != indice) {
                        if (listValoresConceptos.get(j).getConcepto().getSecuencia().equals(lovConceptos.get(indiceUnicoElemento).getSecuencia())) {
                           contadorConceptos++;
                        }
                     }
                     if (contadorConceptos != 0) {
                        listValoresConceptos.get(indice).setConcepto(lovConceptos.get(indiceUnicoElemento));
                     } else {
                        mensajeValidacion = "CONCEPTO REPETIDO";
                        RequestContext.getCurrentInstance().update("form:validacionModificar");
                        RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");
                     }
                     lovConceptos.clear();
                     lovConceptos = null;
                     //getListaTiposFamiliares();
                  }
               } else {
                  for (int j = 0; j < filtrarValoresConceptos.size(); j++) {
                     if (j != indice) {
                        if (filtrarValoresConceptos.get(j).getConcepto().getSecuencia().equals(lovConceptos.get(indiceUnicoElemento).getSecuencia())) {
                           contadorConceptos++;
                        }
                     }
                     if (contadorConceptos != 0) {
                        filtrarValoresConceptos.get(indice).setConcepto(lovConceptos.get(indiceUnicoElemento));
                     } else {
                        mensajeValidacion = "CONCEPTO REPETIDO";
                        RequestContext.getCurrentInstance().update("form:validacionModificar");
                        RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");
                     }
                     lovConceptos.clear();
                     lovConceptos = null;
                     //getListaTiposFamiliares();
                  }
               }

            } else {
               permitirIndex = false;
               RequestContext.getCurrentInstance().update("form:personasDialogo");
               RequestContext.getCurrentInstance().execute("PF('personasDialogo').show()");
               tipoActualizacion = 0;
            }
         } else {
            if (backupConcepto != null) {
               if (tipoLista == 0) {
                  listValoresConceptos.get(index).getConcepto().setDescripcion(backupConcepto);
               } else {
                  filtrarValoresConceptos.get(index).getConcepto().setDescripcion(backupConcepto);
               }
            }
            tipoActualizacion = 0;
            log.info("PAIS ANTES DE MOSTRAR DIALOGO PERSONA : " + backupConcepto);
            RequestContext.getCurrentInstance().update("form:personasDialogo");
            RequestContext.getCurrentInstance().execute("PF('personasDialogo').show()");
         }

         if (coincidencias == 1) {
            if (tipoLista == 0) {
               if (!crearValoresConceptos.contains(listValoresConceptos.get(indice))) {

                  if (modificarValoresConceptos.isEmpty()) {
                     modificarValoresConceptos.add(listValoresConceptos.get(indice));
                  } else if (!modificarValoresConceptos.contains(listValoresConceptos.get(indice))) {
                     modificarValoresConceptos.add(listValoresConceptos.get(indice));
                  }
                  if (guardado == true) {
                     guardado = false;
                  }
               }
               index = -1;
               secRegistro = null;
            } else {
               if (!crearValoresConceptos.contains(filtrarValoresConceptos.get(indice))) {

                  if (modificarValoresConceptos.isEmpty()) {
                     modificarValoresConceptos.add(filtrarValoresConceptos.get(indice));
                  } else if (!modificarValoresConceptos.contains(filtrarValoresConceptos.get(indice))) {
                     modificarValoresConceptos.add(filtrarValoresConceptos.get(indice));
                  }
                  if (guardado == true) {
                     guardado = false;
                  }
               }
               index = -1;
               secRegistro = null;
            }
         }

         RequestContext.getCurrentInstance().update("form:datosValoresConceptos");
         RequestContext.getCurrentInstance().update("form:ACEPTAR");

      }

   }

   public void modificarValoresConceptosCodigo(int indice, String confirmarCambio, BigInteger valorConfirmar) {
      log.error("ENTRE A MODIFICAR SUB CATEGORIA");
      index = indice;
      int coincidencias = 0;
      int contador = 0;
      boolean banderita = false;
      boolean banderita1 = false;
      boolean banderita2 = false;
      int indiceUnicoElemento = 0;
      int contadorConceptos = 0;

      RequestContext context = RequestContext.getCurrentInstance();
      log.error("TIPO LISTA = " + tipoLista);

      if (confirmarCambio.equalsIgnoreCase("CODIGOCONCEPTO")) {
         log.info("MODIFICANDO CODIGO CONCEPTO: " + listValoresConceptos.get(indice).getConcepto().getCodigo());
         if (!listValoresConceptos.get(indice).getConcepto().getDescripcion().equals("")) {
            if (tipoLista == 0) {
               listValoresConceptos.get(indice).getConcepto().setCodigo(backupCodigo);
            } else {
               filtrarValoresConceptos.get(indice).getConcepto().setCodigo(backupCodigo);
            }

            for (int i = 0; i < lovConceptos.size(); i++) {
               if (lovConceptos.get(i).getCodigo().equals(valorConfirmar)) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }

            if (coincidencias == 1) {
               if (tipoLista == 0) {
                  for (int j = 0; j < listValoresConceptos.size(); j++) {
                     if (j != indice) {
                        if (listValoresConceptos.get(j).getConcepto().getSecuencia().equals(lovConceptos.get(indiceUnicoElemento).getSecuencia())) {
                           contadorConceptos++;
                        }
                     }
                     if (contadorConceptos != 0) {
                        listValoresConceptos.get(indice).setConcepto(lovConceptos.get(indiceUnicoElemento));
                     } else {
                        mensajeValidacion = "CONCEPTO REPETIDO";
                        RequestContext.getCurrentInstance().update("form:validacionModificar");
                        RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");
                     }
                     lovConceptos.clear();
                     lovConceptos = null;
                     //getListaTiposFamiliares();
                  }
               } else {
                  for (int j = 0; j < filtrarValoresConceptos.size(); j++) {
                     if (j != indice) {
                        if (filtrarValoresConceptos.get(j).getConcepto().getSecuencia().equals(lovConceptos.get(indiceUnicoElemento).getSecuencia())) {
                           contadorConceptos++;
                        }
                     }
                     if (contadorConceptos != 0) {
                        filtrarValoresConceptos.get(indice).setConcepto(lovConceptos.get(indiceUnicoElemento));
                     } else {
                        mensajeValidacion = "CONCEPTO REPETIDO";
                        RequestContext.getCurrentInstance().update("form:validacionModificar");
                        RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");
                     }
                     lovConceptos.clear();
                     lovConceptos = null;
                     //getListaTiposFamiliares();
                  }
               }

            } else {
               permitirIndex = false;
               RequestContext.getCurrentInstance().update("form:personasDialogo");
               RequestContext.getCurrentInstance().execute("PF('personasDialogo').show()");
               tipoActualizacion = 0;
            }
         } else {
            if (backupConcepto != null) {
               if (tipoLista == 0) {
                  listValoresConceptos.get(index).getConcepto().setCodigo(backupCodigo);
               } else {
                  filtrarValoresConceptos.get(index).getConcepto().setCodigo(backupCodigo);
               }
            }
            tipoActualizacion = 0;
            log.info("CODIGO DIALOGO CONCEPTO : " + backupCodigo);
            RequestContext.getCurrentInstance().update("form:personasDialogo");
            RequestContext.getCurrentInstance().execute("PF('personasDialogo').show()");
         }

         if (coincidencias == 1) {
            if (tipoLista == 0) {
               if (!crearValoresConceptos.contains(listValoresConceptos.get(indice))) {

                  if (modificarValoresConceptos.isEmpty()) {
                     modificarValoresConceptos.add(listValoresConceptos.get(indice));
                  } else if (!modificarValoresConceptos.contains(listValoresConceptos.get(indice))) {
                     modificarValoresConceptos.add(listValoresConceptos.get(indice));
                  }
                  if (guardado == true) {
                     guardado = false;
                  }
               }
               index = -1;
               secRegistro = null;
            } else {
               if (!crearValoresConceptos.contains(filtrarValoresConceptos.get(indice))) {

                  if (modificarValoresConceptos.isEmpty()) {
                     modificarValoresConceptos.add(filtrarValoresConceptos.get(indice));
                  } else if (!modificarValoresConceptos.contains(filtrarValoresConceptos.get(indice))) {
                     modificarValoresConceptos.add(filtrarValoresConceptos.get(indice));
                  }
                  if (guardado == true) {
                     guardado = false;
                  }
               }
               index = -1;
               secRegistro = null;
            }
         }
         cambioValoresConceptos = true;
         RequestContext.getCurrentInstance().update("form:datosValoresConceptos");
         RequestContext.getCurrentInstance().update("form:ACEPTAR");

      }

   }

   public void borrandoValoresConceptos() {

      if (index >= 0) {
         if (tipoLista == 0) {
            log.info("Entro a borrandoValoresConceptos");
            if (!modificarValoresConceptos.isEmpty() && modificarValoresConceptos.contains(listValoresConceptos.get(index))) {
               int modIndex = modificarValoresConceptos.indexOf(listValoresConceptos.get(index));
               modificarValoresConceptos.remove(modIndex);
               borrarValoresConceptos.add(listValoresConceptos.get(index));
            } else if (!crearValoresConceptos.isEmpty() && crearValoresConceptos.contains(listValoresConceptos.get(index))) {
               int crearIndex = crearValoresConceptos.indexOf(listValoresConceptos.get(index));
               crearValoresConceptos.remove(crearIndex);
            } else {
               borrarValoresConceptos.add(listValoresConceptos.get(index));
            }
            listValoresConceptos.remove(index);
         }
         if (tipoLista == 1) {
            log.info("borrandoValoresConceptos ");
            if (!modificarValoresConceptos.isEmpty() && modificarValoresConceptos.contains(filtrarValoresConceptos.get(index))) {
               int modIndex = modificarValoresConceptos.indexOf(filtrarValoresConceptos.get(index));
               modificarValoresConceptos.remove(modIndex);
               borrarValoresConceptos.add(filtrarValoresConceptos.get(index));
            } else if (!crearValoresConceptos.isEmpty() && crearValoresConceptos.contains(filtrarValoresConceptos.get(index))) {
               int crearIndex = crearValoresConceptos.indexOf(filtrarValoresConceptos.get(index));
               crearValoresConceptos.remove(crearIndex);
            } else {
               borrarValoresConceptos.add(filtrarValoresConceptos.get(index));
            }
            int VCIndex = listValoresConceptos.indexOf(filtrarValoresConceptos.get(index));
            listValoresConceptos.remove(VCIndex);
            filtrarValoresConceptos.remove(index);

         }
         RequestContext context = RequestContext.getCurrentInstance();
         if (listValoresConceptos == null || listValoresConceptos.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
         } else {
            infoRegistro = "Cantidad de registros: " + listValoresConceptos.size();
         }
         RequestContext.getCurrentInstance().update("form:informacionRegistro");
         RequestContext.getCurrentInstance().update("form:datosValoresConceptos");
         index = -1;
         secRegistro = null;

         if (guardado == true) {
            guardado = false;
         }
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         cambioValoresConceptos = true;
      }

   }
   private BigInteger nuevoYduplicarCompletarCodigoConcepto;

   public void valoresBackupAutocompletar(int tipoNuevo, String valorCambio) {
      log.info("1...");
      if (valorCambio.equals("CODIGOCONCEPTO")) {
         if (tipoNuevo == 1) {
            nuevoYduplicarCompletarCodigoConcepto = nuevoValoresConceptos.getConcepto().getCodigo();
         } else if (tipoNuevo == 2) {
            nuevoYduplicarCompletarCodigoConcepto = duplicarValoresConceptos.getConcepto().getCodigo();
         }
         log.info("CARGO : " + nuevoYduplicarCompletarCodigoConcepto);
      } else if (valorCambio.equals("CONCEPTO")) {
         if (tipoNuevo == 1) {
            nuevoYduplicarCompletarPersona = nuevoValoresConceptos.getConcepto().getDescripcion();
         } else if (tipoNuevo == 2) {
            nuevoYduplicarCompletarPersona = duplicarValoresConceptos.getConcepto().getDescripcion();
         }

         log.info("CONCEPTO : " + nuevoYduplicarCompletarPersona);
      }

   }

   public void autocompletarNuevoCodigoBigInteger(String confirmarCambio, BigInteger valorConfirmar, int tipoNuevo) {

      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();

      if (confirmarCambio.equalsIgnoreCase("CODIGOCONCEPTO")) {
         log.info(" nueva Operando    Entro al if 'Centro costo'");
         log.info("NUEVO PERSONA :-------> " + nuevoValoresConceptos.getConcepto().getCodigo());

         if (nuevoValoresConceptos.getConcepto().getCodigo() != null) {
            log.info("ENTRO DONDE NO TENIA QUE ENTRAR");
            log.info("valorConfirmar: " + valorConfirmar);
            log.info("nuevoYduplicarCompletarPersona: " + nuevoYduplicarCompletarCodigoConcepto);
            nuevoValoresConceptos.getConcepto().setCodigo(nuevoYduplicarCompletarCodigoConcepto);
            getLovConceptos();
            for (int i = 0; i < lovConceptos.size(); i++) {
               if (lovConceptos.get(i).getCodigo().equals(valorConfirmar)) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            log.info("Coincidencias: " + coincidencias);
            if (coincidencias == 1) {
               nuevoValoresConceptos.setConcepto(lovConceptos.get(indiceUnicoElemento));
               lovConceptos = null;
               log.error("PERSONA GUARDADA :-----> " + nuevoValoresConceptos.getConcepto().getDescripcion());
            } else {
               //listValoresConceptos.get(index).getConcepto().setCodigo(backupCodigo);
               RequestContext.getCurrentInstance().update("form:personasDialogo");
               RequestContext.getCurrentInstance().execute("PF('personasDialogo').show()");
               tipoActualizacion = tipoNuevo;
            }
         } else {
            nuevoValoresConceptos.getConcepto().setCodigo(nuevoYduplicarCompletarCodigoConcepto);
            log.info("valorConfirmar cuando es vacio: " + valorConfirmar);
            nuevoValoresConceptos.setConcepto(new Conceptos());
         }
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevoPersona");
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevoCodigo");
      }

   }

   public void autocompletarNuevo(String confirmarCambio, String valorConfirmar, int tipoNuevo) {

      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();

      if (confirmarCambio.equalsIgnoreCase("CONCEPTO")) {
         log.info(" nueva Operando    Entro al if 'Centro costo'");
         log.info("NUEVO PERSONA :-------> " + nuevoValoresConceptos.getConcepto().getDescripcion());

         if (!nuevoValoresConceptos.getConcepto().getDescripcion().equals("")) {
            log.info("ENTRO DONDE NO TENIA QUE ENTRAR");
            log.info("valorConfirmar: " + valorConfirmar);
            log.info("nuevoYduplicarCompletarPersona: " + nuevoYduplicarCompletarPersona);
            nuevoValoresConceptos.getConcepto().setDescripcion(nuevoYduplicarCompletarPersona);
            for (int i = 0; i < lovConceptos.size(); i++) {
               if (lovConceptos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            log.info("Coincidencias: " + coincidencias);
            if (coincidencias == 1) {
               nuevoValoresConceptos.setConcepto(lovConceptos.get(indiceUnicoElemento));
               lovConceptos = null;
               log.error("PERSONA GUARDADA :-----> " + nuevoValoresConceptos.getConcepto().getDescripcion());
            } else {
               RequestContext.getCurrentInstance().update("form:personasDialogo");
               RequestContext.getCurrentInstance().execute("PF('personasDialogo').show()");
               tipoActualizacion = tipoNuevo;
            }
         } else {
            nuevoValoresConceptos.getConcepto().setDescripcion(nuevoYduplicarCompletarPersona);
            log.info("valorConfirmar cuando es vacio: " + valorConfirmar);
            nuevoValoresConceptos.setConcepto(new Conceptos());
            nuevoValoresConceptos.getConcepto().setDescripcion(" ");
            log.info("NUEVA NORMA LABORAL" + nuevoValoresConceptos.getConcepto().getDescripcion());
         }
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevoPersona");
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevoCodigo");

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
      RequestContext.getCurrentInstance().update("form:personasDialogo");
      RequestContext.getCurrentInstance().execute("PF('personasDialogo').show()");
   }

   public void asignarVariableOperandos(int tipoNuevo) {
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

   public void autocompletarDuplicadoCodigoBigInteger(String confirmarCambio, BigInteger valorConfirmar, int tipoNuevo) {
      log.info("DUPLICAR entrooooooooooooooooooooooooooooooooooooooooooooooooooooooo!!!");
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      if (confirmarCambio.equalsIgnoreCase("CODIGOCONCEPTO")) {
         log.info("DUPLICAR valorConfirmar : " + valorConfirmar);
         log.info("DUPLICAR CIUDAD bkp : " + nuevoYduplicarCompletarCodigoConcepto);

         if (duplicarValoresConceptos.getConcepto().getCodigo() != null) {
            log.info("DUPLICAR ENTRO DONDE NO TENIA QUE ENTRAR");
            log.info("DUPLICAR valorConfirmar: " + valorConfirmar);
            log.info("DUPLICAR nuevoYduplicarCompletarCodigoConcepto: " + nuevoYduplicarCompletarCodigoConcepto);
            duplicarValoresConceptos.getConcepto().setCodigo(nuevoYduplicarCompletarCodigoConcepto);
            for (int i = 0; i < lovConceptos.size(); i++) {
               if (lovConceptos.get(i).getCodigo().equals(valorConfirmar)) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            log.info("Coincidencias: " + coincidencias);
            if (coincidencias == 1) {
               duplicarValoresConceptos.setConcepto(lovConceptos.get(indiceUnicoElemento));
               lovConceptos = null;
            } else {
               RequestContext.getCurrentInstance().update("form:personasDialogo");
               RequestContext.getCurrentInstance().execute("PF('personasDialogo').show()");
               tipoActualizacion = tipoNuevo;
            }
         } else if (tipoNuevo == 2) {
            //duplicarValoresConceptos.getEmpresa().setNombre(nuevoYduplicarCompletarPais);
            log.info("DUPLICAR valorConfirmar cuando es vacio: " + valorConfirmar);
            log.info("DUPLICAR INDEX : " + index);
            duplicarValoresConceptos.setConcepto(new Conceptos());

            log.info("DUPLICAR PERSONA  : " + duplicarValoresConceptos.getConcepto().getCodigo());
            log.info("nuevoYduplicarCompletarPERSONA : " + nuevoYduplicarCompletarCodigoConcepto);
            if (tipoLista == 0) {
               listValoresConceptos.get(index).getConcepto().setCodigo(nuevoYduplicarCompletarCodigoConcepto);
               log.error("tipo lista" + tipoLista);
               log.error("Secuencia Parentesco " + listValoresConceptos.get(index).getConcepto().getSecuencia());
            } else if (tipoLista == 1) {
               filtrarValoresConceptos.get(index).getConcepto().setCodigo(nuevoYduplicarCompletarCodigoConcepto);
            }

         }
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarPersona");
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCodigo");
      }
   }

   public void autocompletarDuplicado(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
      log.info("DUPLICAR entrooooooooooooooooooooooooooooooooooooooooooooooooooooooo!!!");
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      if (confirmarCambio.equalsIgnoreCase("CONCEPTO")) {
         log.info("DUPLICAR valorConfirmar : " + valorConfirmar);
         log.info("DUPLICAR CIUDAD bkp : " + nuevoYduplicarCompletarPersona);

         if (!duplicarValoresConceptos.getConcepto().getDescripcion().equals("")) {
            log.info("DUPLICAR ENTRO DONDE NO TENIA QUE ENTRAR");
            log.info("DUPLICAR valorConfirmar: " + valorConfirmar);
            log.info("DUPLICAR nuevoTipoCCAutoCompletar: " + nuevoYduplicarCompletarPersona);
            duplicarValoresConceptos.getConcepto().setDescripcion(nuevoYduplicarCompletarPersona);
            for (int i = 0; i < lovConceptos.size(); i++) {
               if (lovConceptos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            log.info("Coincidencias: " + coincidencias);
            if (coincidencias == 1) {
               duplicarValoresConceptos.setConcepto(lovConceptos.get(indiceUnicoElemento));
               lovConceptos = null;
            } else {
               RequestContext.getCurrentInstance().update("form:personasDialogo");
               RequestContext.getCurrentInstance().execute("PF('personasDialogo').show()");
               tipoActualizacion = tipoNuevo;
            }
         } else if (tipoNuevo == 2) {
            //duplicarValoresConceptos.getEmpresa().setNombre(nuevoYduplicarCompletarPais);
            log.info("DUPLICAR valorConfirmar cuando es vacio: " + valorConfirmar);
            log.info("DUPLICAR INDEX : " + index);
            duplicarValoresConceptos.setConcepto(new Conceptos());
            duplicarValoresConceptos.getConcepto().setDescripcion(" ");

            log.info("DUPLICAR PERSONA  : " + duplicarValoresConceptos.getConcepto().getDescripcion());
            log.info("nuevoYduplicarCompletarPERSONA : " + nuevoYduplicarCompletarPersona);
            if (tipoLista == 0) {
               listValoresConceptos.get(index).getConcepto().setDescripcion(nuevoYduplicarCompletarPersona);
               log.error("tipo lista" + tipoLista);
               log.error("Secuencia Parentesco " + listValoresConceptos.get(index).getConcepto().getSecuencia());
            } else if (tipoLista == 1) {
               filtrarValoresConceptos.get(index).getConcepto().setDescripcion(nuevoYduplicarCompletarPersona);
            }

         }

         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCodigo");
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarPersona");
      }
   }

   public void revisarDialogoGuardar() {

      if (!borrarValoresConceptos.isEmpty() || !crearValoresConceptos.isEmpty() || !modificarValoresConceptos.isEmpty()) {
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:confirmarGuardar");
         RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
      }

   }

   public void guardarValoresConceptos() {
      RequestContext context = RequestContext.getCurrentInstance();

      if (guardado == false) {
         log.info("Realizando guardarValoresConceptos");
         if (!borrarValoresConceptos.isEmpty()) {
            administrarValoresConceptos.borrarValoresConceptos(borrarValoresConceptos);
            //mostrarBorrados
            registrosBorrados = borrarValoresConceptos.size();
            RequestContext.getCurrentInstance().update("form:mostrarBorrados");
            RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
            borrarValoresConceptos.clear();
         }
         if (!modificarValoresConceptos.isEmpty()) {
            administrarValoresConceptos.modificarValoresConceptos(modificarValoresConceptos);
            modificarValoresConceptos.clear();
         }
         if (!crearValoresConceptos.isEmpty()) {
            administrarValoresConceptos.crearValoresConceptos(crearValoresConceptos);
            crearValoresConceptos.clear();
         }
         log.info("Se guardaron los datos con exito");
         listValoresConceptos = null;
         FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
         RequestContext.getCurrentInstance().update("form:datosValoresConceptos");
         k = 0;
         guardado = true;

      }
      index = -1;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");

   }

   public void editarCelda() {
      if (index >= 0) {
         if (tipoLista == 0) {
            editarValoresConceptos = listValoresConceptos.get(index);
         }
         if (tipoLista == 1) {
            editarValoresConceptos = filtrarValoresConceptos.get(index);
         }

         RequestContext context = RequestContext.getCurrentInstance();
         log.info("Entro a editar... valor celda: " + cualCelda);
         if (cualCelda == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editPais");
            RequestContext.getCurrentInstance().execute("PF('editPais').show()");
            cualCelda = -1;
         } else if (cualCelda == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editConceptos");
            RequestContext.getCurrentInstance().execute("PF('editConceptos').show()");
            cualCelda = -1;
         } else if (cualCelda == 2) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editOperandos");
            RequestContext.getCurrentInstance().execute("PF('editOperandos').show()");
            cualCelda = -1;
         }

      }
      index = -1;
      secRegistro = null;
   }

   public void agregarNuevoValoresConceptos() {
      log.info("agregarNuevoValoresConceptos");
      int contador = 0;
      int duplicados = 0;

      Integer a = 0;
      a = null;
      mensajeValidacion = " ";
      RequestContext context = RequestContext.getCurrentInstance();

      if (nuevoValoresConceptos.getConcepto().getDescripcion() == null || nuevoValoresConceptos.getConcepto().getDescripcion().isEmpty()) {
         mensajeValidacion = mensajeValidacion + " *Concepto \n";
         log.info("Mensaje validacion : " + mensajeValidacion);

      } else {
         log.info("bandera");
         contador++;//3

      }

      if (nuevoValoresConceptos.getValorunitario() == null) {
         mensajeValidacion += " *Valor Voluntario \n";
         log.info("Mensaje validacion : " + mensajeValidacion);
      } else {
         contador++;//1
      }

      log.info("contador " + contador);
      BigInteger contarValoresConceptos = administrarValoresConceptos.contarConceptoValorConcepto(nuevoValoresConceptos.getConcepto().getSecuencia());

      if (contador == 2 && contarValoresConceptos.equals(new BigInteger("0"))) {
         if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            //CERRAR FILTRADO
            log.info("Desactivar");
            codigo = (Column) c.getViewRoot().findComponent("form:datosValoresConceptos:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            personafir = (Column) c.getViewRoot().findComponent("form:datosValoresConceptos:personafir");
            personafir.setFilterStyle("display: none; visibility: hidden;");
            cargo = (Column) c.getViewRoot().findComponent("form:datosValoresConceptos:cargo");
            cargo.setFilterStyle("display: none; visibility: hidden;");
            bandera = 0;
            filtrarValoresConceptos = null;
            tipoLista = 0;
         }
         log.info("Despues de la bandera");

         k++;
         l = BigInteger.valueOf(k);
         nuevoValoresConceptos.setSecuencia(l);
         crearValoresConceptos.add(nuevoValoresConceptos);

         listValoresConceptos.add(nuevoValoresConceptos);
         nuevoValoresConceptos = new ValoresConceptos();
         nuevoValoresConceptos.setConcepto(new Conceptos());
         RequestContext.getCurrentInstance().update("form:datosValoresConceptos");

         infoRegistro = "Cantidad de registros: " + listValoresConceptos.size();
         RequestContext.getCurrentInstance().update("form:informacionRegistro");
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }

         RequestContext.getCurrentInstance().execute("PF('nuevoRegistroValoresConceptos').hide()");
         index = -1;
         secRegistro = null;
         cambioValoresConceptos = true;

      } else {
         if (contarValoresConceptos.intValue() > 0) {
            mensajeValidacion = "El CONCEPTO elegido ya fue insertado";
         }
         RequestContext.getCurrentInstance().update("form:validacionNuevaCentroCosto");
         RequestContext.getCurrentInstance().execute("PF('validacionNuevaCentroCosto').show()");
         contador = 0;
      }
   }

   public void limpiarNuevoValoresConceptos() {
      log.info("limpiarNuevoValoresConceptos");
      nuevoValoresConceptos = new ValoresConceptos();
      nuevoValoresConceptos.setConcepto(new Conceptos());
      secRegistro = null;
      index = -1;

   }

   //------------------------------------------------------------------------------
   public void cargarNuevoValoresConceptos() {
      log.info("cargarNuevoValoresConceptos");

      duplicarValoresConceptos = new ValoresConceptos();
      duplicarValoresConceptos.setConcepto(new Conceptos());
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().execute("PF('nuevoRegistroValoresConceptos').show()");

   }

   public void duplicandoValoresConceptos() {
      log.info("duplicandoValoresConceptos");
      if (index >= 0) {
         duplicarValoresConceptos = new ValoresConceptos();
         duplicarValoresConceptos.setConcepto(new Conceptos());
         k++;
         l = BigInteger.valueOf(k);

         if (tipoLista == 0) {
            duplicarValoresConceptos.setSecuencia(l);
            duplicarValoresConceptos.setConcepto(listValoresConceptos.get(index).getConcepto());
            duplicarValoresConceptos.setValorunitario(listValoresConceptos.get(index).getValorunitario());
         }
         if (tipoLista == 1) {
            duplicarValoresConceptos.setSecuencia(l);
            duplicarValoresConceptos.setConcepto(filtrarValoresConceptos.get(index).getConcepto());
            duplicarValoresConceptos.setValorunitario(filtrarValoresConceptos.get(index).getValorunitario());

         }

         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTE");
         RequestContext.getCurrentInstance().execute("PF('duplicarRegistroValoresConceptos').show()");
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

      if (duplicarValoresConceptos.getConcepto().getDescripcion() == null || duplicarValoresConceptos.getConcepto().getDescripcion().isEmpty()) {
         mensajeValidacion = mensajeValidacion + "   *Concepto \n";
         log.info("Mensaje validacion : " + mensajeValidacion);

      } else {
         log.info("Bandera : ");
         contador++;
      }
      if (duplicarValoresConceptos.getValorunitario() == null) {
         mensajeValidacion = " *Codigo Voluntario \n";
         log.info("Mensaje validacion : " + mensajeValidacion);
      } else {
         contador++;//1
      }

      BigInteger contarConceptosOperandos = administrarValoresConceptos.contarConceptoValorConcepto(duplicarValoresConceptos.getConcepto().getSecuencia());

      if (contador == 2 && contarConceptosOperandos.equals(new BigInteger("0"))) {
         if (crearValoresConceptos.contains(duplicarValoresConceptos)) {
            log.info("Ya lo contengo.");
         }
         listValoresConceptos.add(duplicarValoresConceptos);
         crearValoresConceptos.add(duplicarValoresConceptos);
         RequestContext.getCurrentInstance().update("form:datosValoresConceptos");
         index = -1;
         log.info("--------------DUPLICAR------------------------");
         log.info("PERSONA : " + duplicarValoresConceptos.getConcepto().getDescripcion());
         log.info("--------------DUPLICAR------------------------");

         secRegistro = null;
         if (guardado == true) {
            guardado = false;
         }
         infoRegistro = "Cantidad de registros: " + listValoresConceptos.size();
         RequestContext.getCurrentInstance().update("form:informacionRegistro");
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            //CERRAR FILTRADO
            codigo = (Column) c.getViewRoot().findComponent("form:datosValoresConceptos:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            personafir = (Column) c.getViewRoot().findComponent("form:datosValoresConceptos:personafir");
            personafir.setFilterStyle("display: none; visibility: hidden;");
            cargo = (Column) c.getViewRoot().findComponent("form:datosValoresConceptos:cargo");
            cargo.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosValoresConceptos");
            bandera = 0;
            filtrarValoresConceptos = null;
            tipoLista = 0;
         }
         duplicarValoresConceptos = new ValoresConceptos();
         duplicarValoresConceptos.setConcepto(new Conceptos());

         RequestContext.getCurrentInstance().execute("PF('duplicarRegistroValoresConceptos').hide()");
         index = -1;
      } else {
         if (contarConceptosOperandos.intValue() > 0) {
            mensajeValidacion = "El CONCEPTO elegido ya fue insertado";
         }
         contador = 0;

         RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
         RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
      }
   }

   public void limpiarDuplicarValoresConceptos() {
      duplicarValoresConceptos = new ValoresConceptos();
      duplicarValoresConceptos.setConcepto(new Conceptos());
   }

   public void exportPDF() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosValoresConceptosExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "VALORESCONCEPTOS", false, false, "UTF-8", null, null);
      context.responseComplete();
      index = -1;
      secRegistro = null;
   }

   public void exportXLS() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosValoresConceptosExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "VALORESCONCEPTOS", false, false, "UTF-8", null, null);
      context.responseComplete();
      index = -1;
      secRegistro = null;
   }

   public void verificarRastro() {
      RequestContext context = RequestContext.getCurrentInstance();
      log.info("lol");
      if (!listValoresConceptos.isEmpty()) {
         if (secRegistro != null) {
            log.info("lol 2");
            int resultado = administrarRastros.obtenerTabla(secRegistro, "VALORESCONCEPTOS"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
      } else if (administrarRastros.verificarHistoricosTabla("VALORESCONCEPTOS")) { // igual acá
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
      index = -1;
   }
   private boolean cambioValoresConceptos = false;

   public void llamadoDialogoBuscarConceptos() {
      RequestContext context = RequestContext.getCurrentInstance();
      try {
         if (guardado == false) {
            // banderaSeleccionCentrosCostosPorEmpresa = true;
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");

         } else {
            lovValoresConceptos = null;
            getLovValoresConceptos();
            index = -1;
            RequestContext.getCurrentInstance().update("formularioDialogos:lovCentrosCostos");
            RequestContext.getCurrentInstance().execute("PF('buscarCentrosCostosDialogo').show()");

         }
      } catch (Exception e) {
         log.error("ERROR LLAMADO DIALOGO BUSCAR CENTROS COSTOS  ", e);
      }

   }

   public void seleccionConceptoSoporte() {
      try {
         RequestContext context = RequestContext.getCurrentInstance();

         if (guardado == true) {
            listValoresConceptos.clear();
            log.error("seleccionCentrosCostosPorEmpresa " + conceptoSoporteSeleccionado.getConcepto().getDescripcion());
            listValoresConceptos.add(conceptoSoporteSeleccionado);
            log.error("listCentrosCostosPorEmpresa tamaño " + listValoresConceptos.size());
            log.error("listCentrosCostosPorEmpresa nombre " + listValoresConceptos.get(0).getConcepto().getDescripcion());
            conceptoSoporteSeleccionado = null;
            filterValoresConceptosBoton = null;
            aceptar = true;
            RequestContext.getCurrentInstance().update("form:datosValoresConceptos");
            RequestContext.getCurrentInstance().execute("PF('buscarCentrosCostosDialogo').hide()");
            context.reset("formularioDialogos:lovCentrosCostos:globalFilter");
         } else {
            RequestContext.getCurrentInstance().update("form:confirmarGuardarConceptos");
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardarConceptos').show()");
         }

      } catch (Exception e) {
         log.warn("Error CONTROLBETACENTROSCOSTOS.seleccionaVigencia ERROR====" + e.getMessage());
      }
   }

   public void cancelarSeleccionConceptoSoporte() {
      try {
         RequestContext context = RequestContext.getCurrentInstance();
         conceptoSoporteSeleccionado = null;
         filterValoresConceptosBoton = null;
         aceptar = true;
         index = -1;
         tipoActualizacion = -1;
         RequestContext.getCurrentInstance().update("form:aceptarNCC");

      } catch (Exception e) {
         log.warn("Error CONTROLBETACENTROSCOSTOS.cancelarSeleccionVigencia ERROR====" + e.getMessage());
      }
   }

   public List<ValoresConceptos> getLovValoresConceptos() {
      if (lovValoresConceptos == null) {
         lovValoresConceptos = listValoresConceptos;
      }
      return lovValoresConceptos;
   }

   public void setLovValoresConceptos(List<ValoresConceptos> lovValoresConceptos) {
      this.lovValoresConceptos = lovValoresConceptos;
   }

   public List<ValoresConceptos> getFilterValoresConceptosBoton() {
      return filterValoresConceptosBoton;
   }

   public void setFilterValoresConceptosBoton(List<ValoresConceptos> filterValoresConceptosBoton) {
      this.filterValoresConceptosBoton = filterValoresConceptosBoton;
   }

   public boolean isAceptar() {
      return aceptar;
   }

   public void setAceptar(boolean aceptar) {
      this.aceptar = aceptar;
   }

   public ValoresConceptos getConceptoSoporteSeleccionado() {
      return conceptoSoporteSeleccionado;
   }

   public void setConceptoSoporteSeleccionado(ValoresConceptos conceptoSoporteSeleccionado) {
      this.conceptoSoporteSeleccionado = conceptoSoporteSeleccionado;
   }
   private String infoRegistro;

   //*/*/*/*/*/*/*/*/*/*-/-*//-*/-*/*/*-*/-*/-*/*/*/*/*/---/*/*/*/*/-*/-*/-*/-*/-*/
   public List<ValoresConceptos> getListValoresConceptos() {
      if (listValoresConceptos == null) {
         listValoresConceptos = administrarValoresConceptos.consultarValoresConceptos();
      }
      RequestContext context = RequestContext.getCurrentInstance();
      if (listValoresConceptos == null || listValoresConceptos.isEmpty()) {
         infoRegistro = "Cantidad de registros: 0 ";
      } else {
         infoRegistro = "Cantidad de registros: " + listValoresConceptos.size();
      }
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
      return listValoresConceptos;
   }

   public void setListValoresConceptos(List<ValoresConceptos> listValoresConceptos) {
      this.listValoresConceptos = listValoresConceptos;
   }

   public List<ValoresConceptos> getFiltrarValoresConceptos() {
      return filtrarValoresConceptos;
   }

   public void setFiltrarValoresConceptos(List<ValoresConceptos> filtrarValoresConceptos) {
      this.filtrarValoresConceptos = filtrarValoresConceptos;
   }

   public ValoresConceptos getNuevoValoresConceptos() {
      return nuevoValoresConceptos;
   }

   public void setNuevoValoresConceptos(ValoresConceptos nuevoValoresConceptos) {
      this.nuevoValoresConceptos = nuevoValoresConceptos;
   }

   public ValoresConceptos getDuplicarValoresConceptos() {
      return duplicarValoresConceptos;
   }

   public void setDuplicarValoresConceptos(ValoresConceptos duplicarValoresConceptos) {
      this.duplicarValoresConceptos = duplicarValoresConceptos;
   }

   public ValoresConceptos getEditarValoresConceptos() {
      return editarValoresConceptos;
   }

   public void setEditarValoresConceptos(ValoresConceptos editarValoresConceptos) {
      this.editarValoresConceptos = editarValoresConceptos;
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
         lovConceptos = administrarValoresConceptos.consultarLOVConceptos();
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

   public Conceptos getConceptolovSeleccionado() {
      return conceptolovSeleccionado;
   }

   public void setConceptolovSeleccionado(Conceptos conceptolovSeleccionado) {
      this.conceptolovSeleccionado = conceptolovSeleccionado;
   }

   public ValoresConceptos getValorConceptoSeleccionado() {
      return valorConceptoSeleccionado;
   }

   public void setValorConceptoSeleccionado(ValoresConceptos valorConceptoSeleccionado) {
      this.valorConceptoSeleccionado = valorConceptoSeleccionado;
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

}
