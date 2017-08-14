/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.ClavesSap;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarClavesSapInterface;
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
public class ControlClavesSap implements Serializable {

   private static Logger log = Logger.getLogger(ControlClavesSap.class);

   @EJB
   AdministrarClavesSapInterface administrarClavesSap;
   @EJB
   AdministrarRastrosInterface administrarRastros;
   private List<ClavesSap> listClavesSap;
   private List<ClavesSap> filtrarClavesSap;
   private List<ClavesSap> crearClavesSap;
   private List<ClavesSap> modificarClavesSap;
   private List<ClavesSap> borrarClavesSap;
   private ClavesSap nuevoClavesSap;
   private ClavesSap duplicarClavesSap;
   private ClavesSap editarClavesSap;
   private ClavesSap claveSapSeleccionada;
   //otros
   private int cualCelda, tipoLista, index, tipoActualizacion, k, bandera;
   private BigInteger l;
   private boolean aceptar, guardado;
   //AutoCompletar
   private boolean permitirIndex;
   //RASTRO
   private BigInteger secRegistro;
   private Column clave,
           claveajuste,
           clasificacion,
           naturaleza;
   //borrado
   private int registrosBorrados;
   private String mensajeValidacion;
   //autocompletar
   private String tipoIndice;
   private List<ClavesSap> lovClavesSap;
   private List<ClavesSap> filtradoClavesSap;
   private ClavesSap claveAjusteSeleccionada;
   private String nuevoParentesco;
   private String infoRegistro;
   private String infoRegistroParentesco;
   private int tamano;
   private String backUpClave;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

   public ControlClavesSap() {
      listClavesSap = null;
      crearClavesSap = new ArrayList<ClavesSap>();
      modificarClavesSap = new ArrayList<ClavesSap>();
      borrarClavesSap = new ArrayList<ClavesSap>();
      permitirIndex = true;
      guardado = true;
      editarClavesSap = new ClavesSap();
      nuevoClavesSap = new ClavesSap();
      nuevoClavesSap.setClaveAjuste(new ClavesSap());
      duplicarClavesSap = new ClavesSap();
      duplicarClavesSap.setClaveAjuste(new ClavesSap());
      lovClavesSap = null;
      filtradoClavesSap = null;
      tipoLista = 0;
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
      String pagActual = "clavesap";
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
      lovClavesSap = null;
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
         administrarClavesSap.obtenerConexion(ses.getId());
         administrarRastros.obtenerConexion(ses.getId());
      } catch (Exception e) {
         log.error("Error postconstruct " + this.getClass().getName() + ": " + e);
         log.error("Causa: " + e.getCause());
      }
   }

   public void eventoFiltrar() {
      try {
         log.info("\n ENTRE A ControlClavesSap.eventoFiltrar \n");
         if (tipoLista == 0) {
            tipoLista = 1;
         }
         RequestContext context = RequestContext.getCurrentInstance();
         infoRegistro = "Cantidad de registros: " + filtrarClavesSap.size();
         RequestContext.getCurrentInstance().update("form:informacionRegistro");
      } catch (Exception e) {
         log.warn("Error ControlClavesSap eventoFiltrar ERROR===" + e.getMessage());
      }
   }

   public void cambiarIndice(int indice, int celda) {
      log.error("TIPO LISTA = " + tipoLista);

      if (permitirIndex == true) {
         index = indice;
         cualCelda = celda;
         secRegistro = listClavesSap.get(index).getSecuencia();

         if (cualCelda == 0) {
            if (tipoLista == 0) {
               backUpClave = listClavesSap.get(index).getClave();
            } else {
               backUpClave = filtrarClavesSap.get(index).getClave();
            }
         }

         if (cualCelda == 1) {
            if (tipoLista == 0) {
               tipoIndice = listClavesSap.get(index).getClaveAjuste().getClave();
            } else {
               tipoIndice = filtrarClavesSap.get(index).getClaveAjuste().getClave();
            }
            log.info("Cambiar Indice tipoIndice : " + tipoIndice);
         }

      }
      log.info("Indice: " + index + " Celda: " + cualCelda);
   }

   public void asignarIndex(Integer indice, int LND, int dig) {
      try {
         log.info("\n ENTRE A ControlClavesSap.asignarIndex \n");
         RequestContext context = RequestContext.getCurrentInstance();

         index = indice;
         if (LND == 0) {
            tipoActualizacion = 0;
         } else if (LND == 1) {
            tipoActualizacion = 1;
            log.info("Tipo Actualizacion: " + tipoActualizacion);
         } else if (LND == 2) {
            tipoActualizacion = 2;
         }
         if (dig == 1) {
            llamarDialogoClavesAjustes();
            dig = -1;
         }

      } catch (Exception e) {
         log.warn("Error ControlClavesSap.asignarIndex ERROR======" + e.getMessage());
      }
   }

   public void activarAceptar() {
      aceptar = false;
   }

   public void listaValoresBoton() {
      if (index >= 0) {
         RequestContext context = RequestContext.getCurrentInstance();

         if (cualCelda == 1) {
            RequestContext.getCurrentInstance().update("form:clavesajustesDialogo");
            RequestContext.getCurrentInstance().execute("PF('clavesajustesDialogo').show()");
            tipoActualizacion = 0;
         }

      }
   }

   public void cancelarModificacion() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 1) {
         //CERRAR FILTRADO

         clave = (Column) c.getViewRoot().findComponent("form:datosClavesSap:clave");
         clave.setFilterStyle("display: none; visibility: hidden;");
         claveajuste = (Column) c.getViewRoot().findComponent("form:datosClavesSap:claveajuste");
         claveajuste.setFilterStyle("display: none; visibility: hidden;");
         clasificacion = (Column) c.getViewRoot().findComponent("form:datosClavesSap:clasificacion");
         clasificacion.setFilterStyle("display: none; visibility: hidden;");
         naturaleza = (Column) c.getViewRoot().findComponent("form:datosClavesSap:naturaleza");
         naturaleza.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosClavesSap");
         bandera = 0;
         filtrarClavesSap = null;
         tipoLista = 0;
      }

      borrarClavesSap.clear();
      crearClavesSap.clear();
      modificarClavesSap.clear();
      index = -1;
      secRegistro = null;
      k = 0;
      tamano = 270;
      listClavesSap = null;
      guardado = true;
      permitirIndex = true;
      getListClavesSap();
      RequestContext context = RequestContext.getCurrentInstance();
      if (listClavesSap == null || listClavesSap.isEmpty()) {
         infoRegistro = "Cantidad de registros: 0 ";
      } else {
         infoRegistro = "Cantidad de registros: " + listClavesSap.size();
      }
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
      RequestContext.getCurrentInstance().update("form:datosClavesSap");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void salir() {
      limpiarListasValor();
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 1) {
         //CERRAR FILTRADO
         clave = (Column) c.getViewRoot().findComponent("form:datosClavesSap:clave");
         clave.setFilterStyle("display: none; visibility: hidden;");
         claveajuste = (Column) c.getViewRoot().findComponent("form:datosClavesSap:claveajuste");
         claveajuste.setFilterStyle("display: none; visibility: hidden;");
         clasificacion = (Column) c.getViewRoot().findComponent("form:datosClavesSap:clasificacion");
         clasificacion.setFilterStyle("display: none; visibility: hidden;");
         naturaleza = (Column) c.getViewRoot().findComponent("form:datosClavesSap:naturaleza");
         naturaleza.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosClavesSap");
         bandera = 0;
         filtrarClavesSap = null;
         tipoLista = 0;
      }

      borrarClavesSap.clear();
      crearClavesSap.clear();
      modificarClavesSap.clear();
      index = -1;
      secRegistro = null;
      k = 0;
      listClavesSap = null;
      guardado = true;
      permitirIndex = true;
      getListClavesSap();
      RequestContext context = RequestContext.getCurrentInstance();
      if (listClavesSap == null || listClavesSap.isEmpty()) {
         infoRegistro = "Cantidad de registros: 0 ";
      } else {
         infoRegistro = "Cantidad de registros: " + listClavesSap.size();
      }
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
      RequestContext.getCurrentInstance().update("form:datosClavesSap");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void activarCtrlF11() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 0) {
         tamano = 250;
         clave = (Column) c.getViewRoot().findComponent("form:datosClavesSap:clave");
         clave.setFilterStyle("width: 85% !important;");
         claveajuste = (Column) c.getViewRoot().findComponent("form:datosClavesSap:claveajuste");
         claveajuste.setFilterStyle("width: 85% !important;");
         clasificacion = (Column) c.getViewRoot().findComponent("form:datosClavesSap:clasificacion");
         clasificacion.setFilterStyle("width: 85% !important;");
         naturaleza = (Column) c.getViewRoot().findComponent("form:datosClavesSap:naturaleza");
         naturaleza.setFilterStyle("width: 85% !important;");
         RequestContext.getCurrentInstance().update("form:datosClavesSap");
         log.info("Activar");
         bandera = 1;
      } else if (bandera == 1) {
         tamano = 270;
         log.info("Desactivar");
         clave = (Column) c.getViewRoot().findComponent("form:datosClavesSap:clave");
         clave.setFilterStyle("display: none; visibility: hidden;");
         claveajuste = (Column) c.getViewRoot().findComponent("form:datosClavesSap:claveajuste");
         claveajuste.setFilterStyle("display: none; visibility: hidden;");
         clasificacion = (Column) c.getViewRoot().findComponent("form:datosClavesSap:clasificacion");
         clasificacion.setFilterStyle("display: none; visibility: hidden;");
         naturaleza = (Column) c.getViewRoot().findComponent("form:datosClavesSap:naturaleza");
         naturaleza.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosClavesSap");
         bandera = 0;
         filtrarClavesSap = null;
         tipoLista = 0;
      }
   }

   /*   public void modificandoHvReferencia(int indice, String confirmarCambio, String valorConfirmar) {
     log.error("ENTRE A MODIFICAR HV Referencia");
     index = indice;

     int contador = 0;
     boolean banderita = false;
     Short a;
     a = null;
     RequestContext context = RequestContext.getCurrentInstance();
     log.error("TIPO LISTA = " + tipoLista);
     if (confirmarCambio.equalsIgnoreCase("N")) {
     log.error("ENTRE A MODIFICAR HvReferencia, CONFIRMAR CAMBIO ES N");
     if (tipoLista == 0) {
     if (!crearClavesSapFamiliares.contains(listClavesSap.get(indice))) {

     if (listClavesSap.get(indice).getNombrepersona().isEmpty()) {
     mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
     banderita = false;
     } else if (listClavesSap.get(indice).getNombrepersona().equals(" ")) {
     mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
     banderita = false;
     } else {
     banderita = true;
     }

     if (banderita == true) {
     if (modificarClavesSapFamiliares.isEmpty()) {
     modificarClavesSapFamiliares.add(listClavesSap.get(indice));
     } else if (!modificarClavesSapFamiliares.contains(listClavesSap.get(indice))) {
     modificarClavesSapFamiliares.add(listClavesSap.get(indice));
     }
     if (guardado == true) {
     guardado = false;
     }

     } else {
     RequestContext.getCurrentInstance().update("form:validacionModificar");
     RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");
     cancelarModificacion();
     }
     index = -1;
     secRegistro = null;
     }
     } else {

     if (!crearClavesSapFamiliares.contains(filtrarClavesSap.get(indice))) {
     if (filtrarClavesSap.get(indice).getNombrepersona().isEmpty()) {
     mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
     banderita = false;
     }
     if (filtrarClavesSap.get(indice).getNombrepersona().equals(" ")) {
     mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
     banderita = false;
     }

     if (banderita == true) {
     if (modificarClavesSapFamiliares.isEmpty()) {
     modificarClavesSapFamiliares.add(filtrarClavesSap.get(indice));
     } else if (!modificarClavesSapFamiliares.contains(filtrarClavesSap.get(indice))) {
     modificarClavesSapFamiliares.add(filtrarClavesSap.get(indice));
     }
     if (guardado == true) {
     guardado = false;
     }

     } else {
     RequestContext.getCurrentInstance().update("form:validacionModificar");
     RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");
     cancelarModificacion();
     }
     index = -1;
     secRegistro = null;
     }

     }
     RequestContext.getCurrentInstance().update("form:datosClavesSap");
     }

     }
    */
   public void mostrarInfo(int indice, int celda) {
      if (permitirIndex == true) {
         RequestContext context = RequestContext.getCurrentInstance();

         index = indice;
         cualCelda = celda;
         secRegistro = listClavesSap.get(index).getSecuencia();
         if (cualCelda == 2) {
            if (tipoLista == 0) {
               if (listClavesSap.get(indice).getClasificacionTransient() == null) {
                  listClavesSap.get(indice).setClasificacion(null);
               } else if (listClavesSap.get(indice).getClasificacionTransient().equalsIgnoreCase("DEUDORES")) {
                  listClavesSap.get(indice).setClasificacion("D");
               } else if (listClavesSap.get(indice).getClasificacionTransient().equalsIgnoreCase("STOCKS")) {
                  listClavesSap.get(indice).setClasificacion("S");
               } else if (listClavesSap.get(indice).getClasificacionTransient().equalsIgnoreCase("ACREEDOR")) {
                  listClavesSap.get(indice).setClasificacion("K");
               } else if (listClavesSap.get(indice).getClasificacionTransient().equalsIgnoreCase("ACTVOS FIJOS")) {
                  listClavesSap.get(indice).setClasificacion("A");
               } else if (listClavesSap.get(indice).getClasificacionTransient().equalsIgnoreCase("INVENTARIOS")) {
                  listClavesSap.get(indice).setClasificacion("M");
               }
            } else if (filtrarClavesSap.get(indice).getClasificacionTransient() == null) {
               filtrarClavesSap.get(indice).setClasificacion(null);
            } else if (filtrarClavesSap.get(indice).getClasificacionTransient().equalsIgnoreCase("DEUDORES")) {
               filtrarClavesSap.get(indice).setClasificacion("D");
            } else if (filtrarClavesSap.get(indice).getClasificacionTransient().equalsIgnoreCase("STOCKS")) {
               filtrarClavesSap.get(indice).setClasificacion("S");
            } else if (filtrarClavesSap.get(indice).getClasificacionTransient().equalsIgnoreCase("ACREEDOR")) {
               filtrarClavesSap.get(indice).setClasificacion("K");
            } else if (filtrarClavesSap.get(indice).getClasificacionTransient().equalsIgnoreCase("ACTVOS FIJOS")) {
               filtrarClavesSap.get(indice).setClasificacion("A");
            } else if (filtrarClavesSap.get(indice).getClasificacionTransient().equalsIgnoreCase("INVENTARIOS")) {
               filtrarClavesSap.get(indice).setClasificacion("M");
            }
         } else if (cualCelda == 3) {
            if (tipoLista == 0) {
               log.info("NaturalezaTransient : " + listClavesSap.get(indice).getNaturalezaTransient());
               log.info("Naturaleza : " + listClavesSap.get(indice).getNaturaleza());

               if (listClavesSap.get(indice).getNaturalezaTransient() == null) {
                  listClavesSap.get(indice).setNaturaleza(null);
                  log.info("3NaturalezaTransient : " + listClavesSap.get(indice).getNaturalezaTransient());
                  log.info("3Naturaleza : " + listClavesSap.get(indice).getNaturaleza());
               } else if (listClavesSap.get(indice).getNaturalezaTransient().equalsIgnoreCase("DEBITO")) {
                  listClavesSap.get(indice).setNaturaleza("D");
                  log.info("1NaturalezaTransient : " + listClavesSap.get(indice).getNaturalezaTransient());
                  log.info("1Naturaleza : " + listClavesSap.get(indice).getNaturaleza());
               } else if (listClavesSap.get(indice).getNaturalezaTransient().equalsIgnoreCase("CREDITO")) {
                  listClavesSap.get(indice).setNaturaleza("C");
                  log.info("2NaturalezaTransient : " + listClavesSap.get(indice).getNaturalezaTransient());
                  log.info("2Naturaleza : " + listClavesSap.get(indice).getNaturaleza());
               }
            } else if (filtrarClavesSap.get(indice).getNaturalezaTransient() == null) {
               filtrarClavesSap.get(indice).setNaturaleza(null);
               log.info("3NaturalezaTransient : " + filtrarClavesSap.get(indice).getNaturalezaTransient());
               log.info("3Naturaleza : " + filtrarClavesSap.get(indice).getNaturaleza());
            } else if (filtrarClavesSap.get(indice).getNaturalezaTransient().equalsIgnoreCase("DEBITO")) {
               filtrarClavesSap.get(indice).setNaturaleza("D");
               log.info("1NaturalezaTransient : " + filtrarClavesSap.get(indice).getNaturalezaTransient());
               log.info("1Naturaleza : " + filtrarClavesSap.get(indice).getNaturaleza());
            } else if (filtrarClavesSap.get(indice).getNaturalezaTransient().equalsIgnoreCase("CREDITO")) {
               filtrarClavesSap.get(indice).setNaturaleza("C");
               log.info("2NaturalezaTransient : " + filtrarClavesSap.get(indice).getNaturalezaTransient());
               log.info("2Naturaleza : " + filtrarClavesSap.get(indice).getNaturaleza());
            }
            RequestContext.getCurrentInstance().update("form:datosClavesSap");
         }
         if (tipoLista == 0) {
            if (!crearClavesSap.contains(listClavesSap.get(indice))) {
               if (modificarClavesSap.isEmpty()) {
                  modificarClavesSap.add(listClavesSap.get(indice));
               } else if (!modificarClavesSap.contains(listClavesSap.get(indice))) {
                  modificarClavesSap.add(listClavesSap.get(indice));
               }
               if (guardado == true) {
                  guardado = false;
                  RequestContext.getCurrentInstance().update("form:ACEPTAR");
               }
            } else if (guardado == true) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
         } else if (!crearClavesSap.contains(filtrarClavesSap.get(indice))) {
            if (modificarClavesSap.isEmpty()) {
               modificarClavesSap.add(filtrarClavesSap.get(indice));
            } else if (!modificarClavesSap.contains(filtrarClavesSap.get(indice))) {
               modificarClavesSap.add(filtrarClavesSap.get(indice));
            }
            if (guardado == true) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
         } else if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         RequestContext.getCurrentInstance().update("form:datosClavesSap");
      }
      log.info("Indice: " + index + " Celda: " + cualCelda);

   }

   /**
    *
    * @param indice donde se encuentra posicionado
    * @param confirmarCambio nombre de la columna
    * @param valorConfirmar valor ingresado
    */
   public void modificarClaveSap(int indice, String confirmarCambio, String valorConfirmar) {
      index = indice;
      int coincidencias = 0;
      int clavesRepetidas = 0;
      int indiceUnicoElemento = 0, pass = 0;
      int contador = 0;
      BigInteger contadorBD;
      mensajeValidacion = " ";
      RequestContext context = RequestContext.getCurrentInstance();
      log.error("ENTRE A MODIFICAR");
      if (confirmarCambio.equalsIgnoreCase("N")) {
         log.error("ENTRE A MODIFICAR HvReferencia, CONFIRMAR CAMBIO ES N");
         if (tipoLista == 0) {
            if (!crearClavesSap.contains(listClavesSap.get(index))) {
               if (listClavesSap.get(index).getClave() == null) {
                  pass++;
               } else {
                  for (int i = 0; i < listClavesSap.size(); i++) {
                     if (i != index) {
                        if (listClavesSap.get(i).getClave() != null) {
                           if (listClavesSap.get(index).getClave().equals(listClavesSap.get(i).getClave())) {
                              contador++;
                           }
                        }
                     }
                  }
               }
               if (contador > 0) {
                  mensajeValidacion = "Claves Repetidas";
                  listClavesSap.get(index).setClave(backUpClave);
               } else {
                  pass++;
               }

               if (pass == 1) {
                  if (modificarClavesSap.isEmpty()) {
                     modificarClavesSap.add(listClavesSap.get(index));
                  } else if (!modificarClavesSap.contains(listClavesSap.get(index))) {
                     modificarClavesSap.add(listClavesSap.get(index));
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
               if (listClavesSap.get(index).getClave() == null) {
                  pass++;
               } else {
                  for (int i = 0; i < listClavesSap.size(); i++) {
                     if (i != index) {
                        if (listClavesSap.get(i).getClave() != null) {
                           if (listClavesSap.get(index).getClave().equals(listClavesSap.get(i).getClave())) {
                              contador++;
                           }
                        }
                     }
                  }
               }
               if (contador > 0) {
                  mensajeValidacion = "Claves Repetidas";
                  listClavesSap.get(index).setClave(backUpClave);
               } else {
                  pass++;
               }
               if (pass == 1) {

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
         } else if (!crearClavesSap.contains(filtrarClavesSap.get(index))) {
            if (filtrarClavesSap.get(index).getClave() == null) {
               pass++;
            } else {
               for (int i = 0; i < filtrarClavesSap.size(); i++) {
                  if (i != index) {
                     if (filtrarClavesSap.get(i).getClave() != null) {
                        if (filtrarClavesSap.get(index).getClave().equals(filtrarClavesSap.get(i).getClave())) {
                           contador++;
                        }
                     }
                  }
               }
            }
            if (contador > 0) {
               mensajeValidacion = "Claves Repetidas";
               filtrarClavesSap.get(index).setClave(backUpClave);
            } else {
               pass++;
            }
            if (pass == 1) {
               if (modificarClavesSap.isEmpty()) {
                  modificarClavesSap.add(filtrarClavesSap.get(index));
               } else if (!modificarClavesSap.contains(filtrarClavesSap.get(index))) {
                  modificarClavesSap.add(filtrarClavesSap.get(index));
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
            if (filtrarClavesSap.get(index).getClave() == null) {
               pass++;
            } else {
               for (int i = 0; i < filtrarClavesSap.size(); i++) {
                  if (i != index) {
                     if (filtrarClavesSap.get(i).getClave() != null) {
                        if (filtrarClavesSap.get(index).getClave().equals(filtrarClavesSap.get(i).getClave())) {
                           contador++;
                        }
                     }
                  }
               }
            }
            if (contador > 0) {
               mensajeValidacion = "Claves Repetidas";
               filtrarClavesSap.get(index).setClave(backUpClave);
            } else {
               pass++;
            }
            if (pass == 1) {

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
         RequestContext.getCurrentInstance().update("form:datosClavesSap");
      } else if (confirmarCambio.equalsIgnoreCase("CLAVESAJUSTES")) {

         log.info("Entre a ControlClavesSap modificiarClavesSap");
         if (!listClavesSap.get(index).getClaveAjuste().getClave().equals("")) {

            if (tipoLista == 0) {
               listClavesSap.get(index).getClaveAjuste().setClave(tipoIndice);

            } else {
               filtrarClavesSap.get(index).getClaveAjuste().setClave(tipoIndice);
            }

            for (int i = 0; i < lovClavesSap.size(); i++) {
               if (lovClavesSap.get(i).getClave() != null) {
                  if (lovClavesSap.get(i).getClave().startsWith(valorConfirmar.toUpperCase())) {
                     log.info("Entre a ControlClavesSap modificiarClavesSap TRUE " + i + " " + lovClavesSap.get(i).getClave());
                     indiceUnicoElemento = i;
                     coincidencias++;
                  }
               }
            }

            log.info("coincidencias : " + coincidencias);
            log.info("indiceUnicoElemento : " + indiceUnicoElemento);
            if (coincidencias == 1) {
               if (tipoLista == 0) {
                  if (listClavesSap.get(index).getClave().equals(lovClavesSap.get(indiceUnicoElemento).getClave())) {
                     clavesRepetidas++;
                  }
                  if (clavesRepetidas == 0) {
                     listClavesSap.get(index).setClaveAjuste(lovClavesSap.get(indiceUnicoElemento));
                     log.info("1. Clave Ajuste Agregada : " + listClavesSap.get(index).getClaveAjuste().getClave());
                     log.info("1. index : " + index);
                     if (!crearClavesSap.contains(listClavesSap.get(index))) {
                        log.info("2. Clave Ajuste Agregada : " + listClavesSap.get(index).getClaveAjuste().getClave());
                        log.info("2. index : " + index);

                        if (modificarClavesSap.isEmpty()) {
                           modificarClavesSap.add(listClavesSap.get(index));
                        } else if (!modificarClavesSap.contains(listClavesSap.get(index))) {
                           modificarClavesSap.add(listClavesSap.get(index));
                        }
                        log.info("3. Clave Ajuste Agregada : " + listClavesSap.get(index).getClaveAjuste().getClave());
                        log.info("3. index : " + index);
                        if (guardado == true) {
                           guardado = false;
                        }
                        RequestContext.getCurrentInstance().update("form:datosClavesSap");
                     } else {
                        if (guardado == true) {
                           guardado = false;
                        }
                        RequestContext.getCurrentInstance().update("form:datosClavesSap");
                     }
                     index = -1;
                     secRegistro = null;
                  } else {
                     listClavesSap.get(index).getClaveAjuste().setClave(tipoIndice);
                     mensajeValidacion = "La clave ajuste no puede ser la misma que la clave";
                     RequestContext.getCurrentInstance().update("form:validacionModificar");
                     RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");

                  }
               } else {
                  if (filtrarClavesSap.get(index).getClave().equals(lovClavesSap.get(indiceUnicoElemento).getClave())) {
                     clavesRepetidas++;
                  }
                  if (clavesRepetidas == 0) {
                     filtrarClavesSap.get(index).setClaveAjuste(lovClavesSap.get(indiceUnicoElemento));
                     if (!crearClavesSap.contains(filtrarClavesSap.get(index))) {
                        if (modificarClavesSap.isEmpty()) {
                           modificarClavesSap.add(filtrarClavesSap.get(index));
                        } else if (!modificarClavesSap.contains(filtrarClavesSap.get(index))) {
                           modificarClavesSap.add(filtrarClavesSap.get(index));
                        }
                        if (guardado == true) {
                           guardado = false;
                        }
                     } else if (guardado == true) {
                        guardado = false;
                     }
                     index = -1;
                     secRegistro = null;
                  } else {
                     filtrarClavesSap.get(index).getClaveAjuste().setClave(tipoIndice);
                     mensajeValidacion = "La clave ajuste no puede ser la misma que la clave";
                     RequestContext.getCurrentInstance().update("form:validacionModificar");
                     RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");

                  }
               }
               //listaClavesSap.clear();
               //listaClavesSap = null;
               //getListaClavesSap();

            } else {
               permitirIndex = false;
               // RequestContext.getCurrentInstance().update("form:clavesajustesDialogo");
               //RequestContext.getCurrentInstance().execute("PF('clavesajustesDialogo').show()");
               llamarDialogoClavesAjustes();
               tipoActualizacion = 0;
            }
         } else {
            log.info("PUSE UN VACIO");
            listClavesSap.get(index).getClaveAjuste().setClave(tipoIndice);
            listClavesSap.get(index).setClaveAjuste(new ClavesSap());
            coincidencias = 1;
         }
         RequestContext.getCurrentInstance().update("form:datosClavesSap");

      }
      RequestContext.getCurrentInstance().update("form:datosClavesSap");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void llamarDialogoClavesAjustes() {
      lovClavesSap = null;
      getLovClavesSap();
      /*for (int i = 0; i < lovClavesSap.size(); i++) {
         if (listClavesSap.get(index).getClave().equals(lovClavesSap.get(i).getClave())) {
         log.info("listClavesSap.get(index).getClave()" + listClavesSap.get(index).getClave());
         log.info("lovClavesSap.get(i).getClave())" + lovClavesSap.get(i).getClave());
         listClavesSap.add(listClavesSap.get(index));
         lovClavesSap.remove(i);
         }
         }*/
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:clavesajustesDialogo");
      RequestContext.getCurrentInstance().update("form:lovClavesSap");
      RequestContext.getCurrentInstance().execute("PF('clavesajustesDialogo').show()");
   }

   public void actualizarClaveAjuste() {
      RequestContext context = RequestContext.getCurrentInstance();
      int clavesRepetidas = 0;
      if (tipoActualizacion == 0) {
         if (tipoLista == 0) {
            /**
             * *
             *
             */
            if (listClavesSap.get(index).getClave().equals(claveAjusteSeleccionada.getClave())) {
               clavesRepetidas++;
            }
            if (clavesRepetidas == 0) {
               listClavesSap.get(index).setClaveAjuste(claveAjusteSeleccionada);
               if (!crearClavesSap.contains(listClavesSap.get(index))) {
                  if (modificarClavesSap.isEmpty()) {
                     modificarClavesSap.add(listClavesSap.get(index));
                  } else if (!modificarClavesSap.contains(listClavesSap.get(index))) {
                     modificarClavesSap.add(listClavesSap.get(index));
                  }
               }
               if (guardado == true) {
                  guardado = false;
               }
               permitirIndex = true;
               // RequestContext.getCurrentInstance().update("form:datosClavesSap");
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            } else {
               listClavesSap.get(index).getClaveAjuste().setClave(tipoIndice);
               mensajeValidacion = "La clave ajuste no puede ser la misma que la clave";
               RequestContext.getCurrentInstance().update("form:validacionModificar");
               RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");

            }
         } else {
            if (filtrarClavesSap.get(index).getClave().equals(claveAjusteSeleccionada.getClave())) {
               clavesRepetidas++;
            }
            if (clavesRepetidas == 0) {
               filtrarClavesSap.get(index).setClaveAjuste(claveAjusteSeleccionada);

               if (!crearClavesSap.contains(filtrarClavesSap.get(index))) {
                  if (modificarClavesSap.isEmpty()) {
                     modificarClavesSap.add(filtrarClavesSap.get(index));
                  } else if (!modificarClavesSap.contains(filtrarClavesSap.get(index))) {
                     modificarClavesSap.add(filtrarClavesSap.get(index));
                  }
               }
               if (guardado == true) {
                  guardado = false;
               }
               permitirIndex = true;
               // RequestContext.getCurrentInstance().update("form:datosClavesSap");
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            } else {
               filtrarClavesSap.get(index).getClaveAjuste().setClave(tipoIndice);
               mensajeValidacion = "La clave ajuste no puede ser la misma que la clave";
               RequestContext.getCurrentInstance().update("form:validacionModificar");
               RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");

            }
         }

      } else if (tipoActualizacion == 1) {
         nuevoClavesSap.setClaveAjuste(claveAjusteSeleccionada);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevooHvReferenciaLab");
      } else if (tipoActualizacion == 2) {
         duplicarClavesSap.setClaveAjuste(claveAjusteSeleccionada);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarRRL");
      }
      filtradoClavesSap = null;
      claveAjusteSeleccionada = null;
      aceptar = true;
      index = -1;
      secRegistro = null;
      tipoActualizacion = -1;
      cualCelda = -1;
      context.reset("form:lovClavesSap:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovClavesSap').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('clavesajustesDialogo').hide()");
      //RequestContext.getCurrentInstance().update("form:lovClavesSap");
      RequestContext.getCurrentInstance().update("form:datosClavesSap");
   }

   public void cancelarCambioClavesSap() {
      if (tipoActualizacion == 0) {
         if (index >= 0) {
            listClavesSap.get(index).getClaveAjuste().setClave(tipoIndice);
         }
      }
      log.info("LIsta : " + lovClavesSap.size());
      log.info("LIst : " + listClavesSap.size());
      filtradoClavesSap = null;
      claveAjusteSeleccionada = null;
      aceptar = true;
      index = -1;
      secRegistro = null;
      tipoActualizacion = -1;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("form:lovClavesSap:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovClavesSap').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('clavesajustesDialogo').hide()");
   }

   public void borrandoClavesSap() {

      if (index >= 0) {
         if (tipoLista == 0) {
            log.info("Entro a borrandoClavesSap");
            if (!modificarClavesSap.isEmpty() && modificarClavesSap.contains(listClavesSap.get(index))) {
               int modIndex = modificarClavesSap.indexOf(listClavesSap.get(index));
               modificarClavesSap.remove(modIndex);
               borrarClavesSap.add(listClavesSap.get(index));
            } else if (!crearClavesSap.isEmpty() && crearClavesSap.contains(listClavesSap.get(index))) {
               int crearIndex = crearClavesSap.indexOf(listClavesSap.get(index));
               crearClavesSap.remove(crearIndex);
            } else {
               borrarClavesSap.add(listClavesSap.get(index));
            }
            listClavesSap.remove(index);
         }
         if (tipoLista == 1) {
            log.info("borrandoClavesSap ");
            if (!modificarClavesSap.isEmpty() && modificarClavesSap.contains(filtrarClavesSap.get(index))) {
               int modIndex = modificarClavesSap.indexOf(filtrarClavesSap.get(index));
               modificarClavesSap.remove(modIndex);
               borrarClavesSap.add(filtrarClavesSap.get(index));
            } else if (!crearClavesSap.isEmpty() && crearClavesSap.contains(filtrarClavesSap.get(index))) {
               int crearIndex = crearClavesSap.indexOf(filtrarClavesSap.get(index));
               crearClavesSap.remove(crearIndex);
            } else {
               borrarClavesSap.add(filtrarClavesSap.get(index));
            }
            int VCIndex = listClavesSap.indexOf(filtrarClavesSap.get(index));
            listClavesSap.remove(VCIndex);
            filtrarClavesSap.remove(index);

         }
         RequestContext context = RequestContext.getCurrentInstance();
         if (listClavesSap == null || listClavesSap.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
         } else {
            infoRegistro = "Cantidad de registros: " + listClavesSap.size();
         }
         RequestContext.getCurrentInstance().update("form:informacionRegistro");
         RequestContext.getCurrentInstance().update("form:datosClavesSap");
         index = -1;
         secRegistro = null;

         if (guardado == true) {
            guardado = false;
         }
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      }

   }

   public void verificarBorrado() {

      log.info("Estoy en verificarBorrado");
      BigInteger contarClavesContablesCreditoClaveSap;
      BigInteger contarResultadosClavesSapDetallesIndice;
      try {
         log.error("Control Secuencia de ControlClavesSap ");
         contarClavesContablesCreditoClaveSap = administrarClavesSap.contarClavesContablesCreditoClaveSap(listClavesSap.get(index).getSecuencia());
         contarResultadosClavesSapDetallesIndice = administrarClavesSap.contarClavesContablesDebitoClaveSap(listClavesSap.get(index).getSecuencia());

         if (contarClavesContablesCreditoClaveSap.equals(new BigInteger("0"))
                 && contarResultadosClavesSapDetallesIndice.equals(new BigInteger("0"))) {
            log.info("Borrado==0");
            borrandoClavesSap();
         } else {
            log.info("Borrado>0");

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:validacionBorrar");
            RequestContext.getCurrentInstance().execute("PF('validacionBorrar').show()");
            index = -1;

         }
      } catch (Exception e) {
         log.error("ERROR ControlClavesSap verificarBorrado ERROR " + e);
      }

   }

   public void revisarDialogoGuardar() {

      if (!borrarClavesSap.isEmpty() || !crearClavesSap.isEmpty() || !modificarClavesSap.isEmpty()) {
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:confirmarGuardar");
         RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
      }

   }

   public void guardarClaveSap() {
      RequestContext context = RequestContext.getCurrentInstance();

      if (guardado == false) {
         log.info("Realizando guardarClaveSap");
         if (!borrarClavesSap.isEmpty()) {
            for (int i = 0; i < borrarClavesSap.size(); i++) {
               log.info("Borrando...");
               if (borrarClavesSap.get(i).getClaveAjuste().getSecuencia() == null) {
                  borrarClavesSap.get(i).setClaveAjuste(null);
               }
            }
            administrarClavesSap.borrarClavesSap(borrarClavesSap);
            //mostrarBorrados
            registrosBorrados = borrarClavesSap.size();
            RequestContext.getCurrentInstance().update("form:mostrarBorrados");
            RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
            borrarClavesSap.clear();
         }
         if (!crearClavesSap.isEmpty()) {
            for (int i = 0; i < crearClavesSap.size(); i++) {

               if (crearClavesSap.get(i).getClaveAjuste().getSecuencia() == null) {
                  crearClavesSap.get(i).setClaveAjuste(null);
               }
            }
            log.info("Creando...");
            administrarClavesSap.crearClavesSap(crearClavesSap);
            crearClavesSap.clear();
         }
         if (!modificarClavesSap.isEmpty()) {
            for (int i = 0; i < modificarClavesSap.size(); i++) {
               if (modificarClavesSap.get(i).getClaveAjuste().getSecuencia() == null) {
                  modificarClavesSap.get(i).setClaveAjuste(null);
               }
            }
            administrarClavesSap.modificarClavesSap(modificarClavesSap);
            modificarClavesSap.clear();
         }
         log.info("Se guardaron los datos con exito");
         listClavesSap = null;
         RequestContext.getCurrentInstance().update("form:datosClavesSap");
         FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");

         k = 0;
      }
      index = -1;
      guardado = true;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");

   }

   public void editarCelda() {
      if (index >= 0) {
         if (tipoLista == 0) {
            editarClavesSap = listClavesSap.get(index);
         }
         if (tipoLista == 1) {
            editarClavesSap = filtrarClavesSap.get(index);
         }
         RequestContext context = RequestContext.getCurrentInstance();
         log.info("Entro a editar... valor celda: " + cualCelda);
         if (cualCelda == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editClaveD");
            RequestContext.getCurrentInstance().execute("PF('editClaveD').show()");
            cualCelda = -1;
         } else if (cualCelda == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editClaveAjusteD");
            RequestContext.getCurrentInstance().execute("PF('editClaveAjusteD').show()");
            cualCelda = -1;

         }

      }
      index = -1;
      secRegistro = null;
   }

   public void valoresBackupAutocompletar(int tipoNuevo, String Campo) {

      if (Campo.equalsIgnoreCase("CLAVESAJUSTES")) {
         if (tipoNuevo == 1) {
            nuevoParentesco = nuevoClavesSap.getClaveAjuste().getClave();
         } else if (tipoNuevo == 2) {
            nuevoParentesco = duplicarClavesSap.getClaveAjuste().getClave();
         }
      }
      log.error("NUEVO PARENTESCO " + nuevoParentesco);
   }

   public void autocompletarNuevoyDuplicado(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      if (confirmarCambio.equalsIgnoreCase("CLAVESAJUSTES")) {
         if (!nuevoClavesSap.getClaveAjuste().getClave().equals("")) {
            if (tipoNuevo == 1) {
               nuevoClavesSap.getClaveAjuste().setClave(nuevoParentesco);
            }
            for (int i = 0; i < lovClavesSap.size(); i++) {
               if (lovClavesSap.get(i).getClave() != null) {
                  if (lovClavesSap.get(i).getClave().startsWith(valorConfirmar.toUpperCase())) {
                     indiceUnicoElemento = i;
                     coincidencias++;
                  }
               }
            }
         } else {

            if (tipoNuevo == 1) {
               nuevoClavesSap.setClaveAjuste(new ClavesSap());
               coincidencias = 1;
            }
            coincidencias = 1;
         }
         if (coincidencias == 1) {
            if (tipoNuevo == 1) {
               nuevoClavesSap.setClaveAjuste(lovClavesSap.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoNombreSucursal");
            }
            lovClavesSap.clear();
            lovClavesSap = null;
            getLovClavesSap();
         } else {
            RequestContext.getCurrentInstance().update("form:clavesajustesDialogo");
            RequestContext.getCurrentInstance().execute("PF('clavesajustesDialogo').show()");
            tipoActualizacion = tipoNuevo;
            if (tipoNuevo == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoNombreSucursal");
            } else if (tipoNuevo == 2) {
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarNombreSucursal");
            }
         }
      }
   }

   public void autocompletarDuplicado(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      if (confirmarCambio.equalsIgnoreCase("CLAVESAJUSTES")) {
         if (!duplicarClavesSap.getClaveAjuste().getClave().equals("")) {
            if (tipoNuevo == 2) {
               duplicarClavesSap.getClaveAjuste().setClave(nuevoParentesco);
            }
            for (int i = 0; i < lovClavesSap.size(); i++) {
               if (lovClavesSap.get(i).getClave() != null) {
                  if (lovClavesSap.get(i).getClave().startsWith(valorConfirmar.toUpperCase())) {
                     indiceUnicoElemento = i;
                     coincidencias++;
                  }
               }
            }
            if (coincidencias == 1) {
               if (tipoNuevo == 2) {
                  duplicarClavesSap.setClaveAjuste(lovClavesSap.get(indiceUnicoElemento));
                  RequestContext.getCurrentInstance().update("formularioDialogos:duplicarNombreSucursal");

               }
               lovClavesSap.clear();
               lovClavesSap = null;
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarNombreSucursal");
               getLovClavesSap();
            } else {
               RequestContext.getCurrentInstance().update("form:clavesajustesDialogo");
               RequestContext.getCurrentInstance().execute("PF('clavesajustesDialogo').show()");
               tipoActualizacion = tipoNuevo;
               if (tipoNuevo == 2) {
                  RequestContext.getCurrentInstance().update("formularioDialogos:duplicarNombreSucursal");
               }
            }
         } else if (tipoNuevo == 2) {
            duplicarClavesSap.setClaveAjuste(new ClavesSap());
            log.info("NUEVO PARENTESCO " + nuevoParentesco);
            if (tipoLista == 0) {
               if (index >= 0) {
                  listClavesSap.get(index).getClaveAjuste().setClave(nuevoParentesco);
                  log.error("tipo lista" + tipoLista);
                  log.error("Secuencia Parentesco " + listClavesSap.get(index).getClaveAjuste().getSecuencia());
               }
            } else if (tipoLista == 1) {
               filtrarClavesSap.get(index).getClaveAjuste().setClave(nuevoParentesco);
            }

         }

         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarNombreSucursal");

      }
   }

   public void asignarVariableClavesSapNuevo(int tipoNuevo) {
      lovClavesSap = getListClavesSap();
      if (tipoNuevo == 0) {
         tipoActualizacion = 1;
      }
      if (tipoNuevo == 1) {
         tipoActualizacion = 2;
      }
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:clavesajustesDialogo");
      RequestContext.getCurrentInstance().execute("PF('clavesajustesDialogo').show()");
   }

   public void agregarNuevoClavesSap() {
      log.info("agregarNuevoClavesSap");
      int contador = 0;
      int contador1 = 0;
      int pass = 0;
      BigInteger contadorBD;
      Short a = 0;
      a = null;
      mensajeValidacion = " ";
      RequestContext context = RequestContext.getCurrentInstance();

      if (nuevoClavesSap.getClave() == null) {
         pass++;
      } else {
         for (int i = 0; i < listClavesSap.size(); i++) {
            if (listClavesSap.get(i).getClave() != null) {
               if (listClavesSap.get(i).getClave().equalsIgnoreCase(nuevoClavesSap.getClave())) {
                  contador++;
               }
            }
         }
         if (contador > 0) {
            mensajeValidacion = mensajeValidacion + "Claves Repetidas";
         } else {
            pass++;
         }
      }
      if (nuevoClavesSap.getClaveAjuste().getClave() == null) {
         pass++;
      } else if (nuevoClavesSap.getClave() != null) {
         if (nuevoClavesSap.getClaveAjuste().getClave().equalsIgnoreCase(nuevoClavesSap.getClave())) {
            contador1++;
         }

         if (contador1 > 0) {
            mensajeValidacion = mensajeValidacion + "*Clave Ajuste no puede ser igual a la clave";
            contador1 = 0;
         } else {
            pass++;
         }
      }
      if (pass == 2) {
         FacesContext c = FacesContext.getCurrentInstance();
         if (bandera == 1) {
            //CERRAR FILTRADO
            log.info("Desactivar");
            clave = (Column) c.getViewRoot().findComponent("form:datosClavesSap:clave");
            clave.setFilterStyle("display: none; visibility: hidden;");
            claveajuste = (Column) c.getViewRoot().findComponent("form:datosClavesSap:claveajuste");
            claveajuste.setFilterStyle("display: none; visibility: hidden;");
            clasificacion = (Column) c.getViewRoot().findComponent("form:datosClavesSap:clasificacion");
            clasificacion.setFilterStyle("display: none; visibility: hidden;");
            naturaleza = (Column) c.getViewRoot().findComponent("form:datosClavesSap:naturaleza");
            naturaleza.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosClavesSap");
            bandera = 0;
            filtrarClavesSap = null;
            tipoLista = 0;
         }
         log.info("Despues de la bandera");

         k++;
         l = BigInteger.valueOf(k);
         nuevoClavesSap.setSecuencia(l);
         log.info("Nueva Secuencia " + nuevoClavesSap.getSecuencia().toString());
         log.info("Nueva Secuencia Clave Ajuste" + nuevoClavesSap.getClaveAjuste().getSecuencia().toString());
         crearClavesSap.add(nuevoClavesSap);
         listClavesSap.add(nuevoClavesSap);
         log.info("tamaño en agregarnuevoclavesap : " + listClavesSap.size());
         nuevoClavesSap = new ClavesSap();
         nuevoClavesSap.setClaveAjuste(new ClavesSap());
         RequestContext.getCurrentInstance().update("form:datosClavesSap");
         infoRegistro = "Cantidad de registros: " + listClavesSap.size();
         RequestContext.getCurrentInstance().update("form:informacionRegistro");
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }

         RequestContext.getCurrentInstance().execute("PF('nuevoRegistroClavesSap').hide()");
         index = -1;
         secRegistro = null;

      } else {
         RequestContext.getCurrentInstance().update("form:validacionNuevaCentroCosto");
         RequestContext.getCurrentInstance().execute("PF('validacionNuevaCentroCosto').show()");
         contador = 0;
      }
   }

   public void limpiarNuevoClavesSap() {
      log.info("limpiarNuevoClavesSap");
      nuevoClavesSap = new ClavesSap();
      nuevoClavesSap.setClaveAjuste(new ClavesSap());
      secRegistro = null;
      index = -1;

   }

   //------------------------------------------------------------------------------
   public void duplicandoClavesSap() {
      log.info("duplicandoClavesSap");
      if (index >= 0) {
         duplicarClavesSap = new ClavesSap();
         k++;
         l = BigInteger.valueOf(k);

         if (tipoLista == 0) {
            duplicarClavesSap.setSecuencia(l);
            duplicarClavesSap.setClave(listClavesSap.get(index).getClave());
            duplicarClavesSap.setClaveAjuste(listClavesSap.get(index).getClaveAjuste());
         }
         if (tipoLista == 1) {
            duplicarClavesSap.setSecuencia(l);
            duplicarClavesSap.setClave(filtrarClavesSap.get(index).getClave());
            duplicarClavesSap.setClaveAjuste(filtrarClavesSap.get(index).getClaveAjuste());
         }

         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarRRL");
         RequestContext.getCurrentInstance().execute("PF('duplicarRegistroClavesSap').show()");
         secRegistro = null;
      }
   }

   public void confirmarDuplicar() {
      log.error("ESTOY EN CONFIRMAR DUPLICAR CLAVES_SAP");
      int contador = 0;
      int contador1 = 0;
      int pass = 0;
      BigInteger contadorBD;
      mensajeValidacion = " ";
      RequestContext context = RequestContext.getCurrentInstance();
      if (duplicarClavesSap.getClave() == null) {
         pass++;
      } else {
         for (int i = 0; i < listClavesSap.size(); i++) {
            if (listClavesSap.get(i).getClave() != null) {
               if (listClavesSap.get(i).getClave().equalsIgnoreCase(duplicarClavesSap.getClave())) {
                  contador++;
               }
            }
         }
         if (contador > 0) {
            mensajeValidacion = mensajeValidacion + "Claves Repetidas";
         } else {
            pass++;
         }
      }
      if (duplicarClavesSap.getClaveAjuste().getClave() == null) {
         pass++;
      } else if (duplicarClavesSap.getClave() != null) {
         if (duplicarClavesSap.getClaveAjuste().getClave().equalsIgnoreCase(duplicarClavesSap.getClave())) {
            contador1++;
         }

         if (contador1 > 0) {
            mensajeValidacion = mensajeValidacion + "*Clave Ajuste no puede ser igual a la clave";
            contador1 = 0;
         } else {
            pass++;
         }
      }
      if (pass == 2) {

         if (crearClavesSap.contains(duplicarClavesSap)) {
            log.info("Ya lo contengo.");
         } else {
            crearClavesSap.add(duplicarClavesSap);
         }
         listClavesSap.add(duplicarClavesSap);
         RequestContext.getCurrentInstance().update("form:datosClavesSap");
         index = -1;
         secRegistro = null;

         if (guardado == true) {
            guardado = false;
         }
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         infoRegistro = "Cantidad de registros: " + listClavesSap.size();
         RequestContext.getCurrentInstance().update("form:informacionRegistro");

         if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            //CERRAR FILTRADO
            clave = (Column) c.getViewRoot().findComponent("form:datosClavesSap:clave");
            clave.setFilterStyle("display: none; visibility: hidden;");
            claveajuste = (Column) c.getViewRoot().findComponent("form:datosClavesSap:claveajuste");
            claveajuste.setFilterStyle("display: none; visibility: hidden;");
            clasificacion = (Column) c.getViewRoot().findComponent("form:datosClavesSap:clasificacion");
            clasificacion.setFilterStyle("display: none; visibility: hidden;");
            naturaleza = (Column) c.getViewRoot().findComponent("form:datosClavesSap:naturaleza");
            naturaleza.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosClavesSap");
            bandera = 0;
            filtrarClavesSap = null;
            tipoLista = 0;
         }
         RequestContext.getCurrentInstance().execute("PF('duplicarRegistroClavesSap').hide()");
         duplicarClavesSap = new ClavesSap();

      } else {
         contador = 0;
         RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
         RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
      }
   }

   public void limpiarDuplicarClavesSap() {
      duplicarClavesSap = new ClavesSap();
      duplicarClavesSap.setClaveAjuste(new ClavesSap());
   }

   public void exportPDF() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosClavesSapExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "CLAVES_SAP", false, false, "UTF-8", null, null);
      context.responseComplete();
      index = -1;
      secRegistro = null;
   }

   public void exportXLS() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosClavesSapExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "CLAVES_SAP", false, false, "UTF-8", null, null);
      context.responseComplete();
      index = -1;
      secRegistro = null;
   }

   public void verificarRastro() {
      RequestContext context = RequestContext.getCurrentInstance();
      log.info("lol");
      if (!listClavesSap.isEmpty()) {
         if (secRegistro != null) {
            log.info("lol 2");
            int resultado = administrarRastros.obtenerTabla(secRegistro, "CLAVES_SAP"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
      } else if (administrarRastros.verificarHistoricosTabla("CLAVES_SAP")) { // igual acá
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
      index = -1;
   }

   //*/*/*/*/*/*/*/*/*/*-/-*//-*/-*/*/*-*/-*/-*/*/*/*/*/---/*/*/*/*/-*/-*/-*/-*/-*/
   public List<ClavesSap> getListClavesSap() {
      if (listClavesSap == null) {
         listClavesSap = administrarClavesSap.consultarClavesSap();
      }
      if (listClavesSap != null) {
         for (int z = 0; z < listClavesSap.size(); z++) {
            if (listClavesSap.get(z).getClaveAjuste() == null) {
               listClavesSap.get(z).setClaveAjuste(new ClavesSap());
            }
         }
      }
      for (int z = 0; z < listClavesSap.size(); z++) {
         if (listClavesSap.get(z).getClaveAjuste() == null) {
            listClavesSap.get(z).setClaveAjuste(new ClavesSap());
         }
      }
      RequestContext context = RequestContext.getCurrentInstance();
      if (listClavesSap == null || listClavesSap.isEmpty()) {
         infoRegistro = "Cantidad de registros: 0 ";
      } else {
         infoRegistro = "Cantidad de registros: " + listClavesSap.size();
      }
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
      return listClavesSap;
   }

   public void setListClavesSap(List<ClavesSap> listClavesSap) {
      this.listClavesSap = listClavesSap;
   }

   public List<ClavesSap> getFiltrarClavesSap() {
      return filtrarClavesSap;
   }

   public void setFiltrarClavesSap(List<ClavesSap> filtrarClavesSap) {
      this.filtrarClavesSap = filtrarClavesSap;
   }

   public List<ClavesSap> getCrearClavesSap() {
      return crearClavesSap;
   }

   public void setCrearClavesSap(List<ClavesSap> crearClavesSap) {
      this.crearClavesSap = crearClavesSap;
   }

   public ClavesSap getNuevoClavesSap() {
      return nuevoClavesSap;
   }

   public void setNuevoClavesSap(ClavesSap nuevoClavesSap) {
      this.nuevoClavesSap = nuevoClavesSap;
   }

   public ClavesSap getDuplicarClavesSap() {
      return duplicarClavesSap;
   }

   public void setDuplicarClavesSap(ClavesSap duplicarClavesSap) {
      this.duplicarClavesSap = duplicarClavesSap;
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

   public List<ClavesSap> getLovClavesSap() {
      if (lovClavesSap == null) {
         lovClavesSap = listClavesSap;
      }
      RequestContext context = RequestContext.getCurrentInstance();
      if (lovClavesSap == null || lovClavesSap.isEmpty()) {
         infoRegistroParentesco = "Cantidad de registros: 0 ";
      } else {
         infoRegistroParentesco = "Cantidad de registros: " + lovClavesSap.size();
      }
      RequestContext.getCurrentInstance().update("form:infoRegistroParentesco");
      return lovClavesSap;
   }

   public void setLovClavesSap(List<ClavesSap> lovClavesSap) {
      this.lovClavesSap = lovClavesSap;
   }

   public List<ClavesSap> getFiltradoClavesSap() {
      return filtradoClavesSap;
   }

   public void setFiltradoClavesSap(List<ClavesSap> filtradoClavesSap) {
      this.filtradoClavesSap = filtradoClavesSap;
   }

   public String getMensajeValidacion() {
      return mensajeValidacion;
   }

   public void setMensajeValidacion(String mensajeValidacion) {
      this.mensajeValidacion = mensajeValidacion;
   }

   public ClavesSap getEditarClavesSap() {
      return editarClavesSap;
   }

   public void setEditarClavesSap(ClavesSap editarClavesSap) {
      this.editarClavesSap = editarClavesSap;
   }

   public boolean isGuardado() {
      return guardado;
   }

   public void setGuardado(boolean guardado) {
      this.guardado = guardado;
   }

   public ClavesSap getClavesSapSeleccionada() {
      return claveSapSeleccionada;
   }

   public void setClavesSapSeleccionada(ClavesSap hvReferencia1Seleccionada) {
      this.claveSapSeleccionada = hvReferencia1Seleccionada;
   }

   public boolean isAceptar() {
      return aceptar;
   }

   public void setAceptar(boolean aceptar) {
      this.aceptar = aceptar;
   }

   public String getInfoRegistro() {
      return infoRegistro;
   }

   public void setInfoRegistro(String infoRegistro) {
      this.infoRegistro = infoRegistro;
   }

   public String getInfoRegistroParentesco() {
      return infoRegistroParentesco;
   }

   public void setInfoRegistroParentesco(String infoRegistroParentesco) {
      this.infoRegistroParentesco = infoRegistroParentesco;
   }

   public int getTamano() {
      return tamano;
   }

   public void setTamano(int tamano) {
      this.tamano = tamano;
   }

   public ClavesSap getClaveAjusteSeleccionada() {
      return claveAjusteSeleccionada;
   }

   public void setClaveAjusteSeleccionada(ClavesSap claveAjusteSeleccionada) {
      this.claveAjusteSeleccionada = claveAjusteSeleccionada;
   }

}
