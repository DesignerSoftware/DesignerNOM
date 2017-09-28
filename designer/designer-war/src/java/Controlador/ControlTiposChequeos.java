/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.TiposChequeos;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarTiposChequeosInterface;
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
public class ControlTiposChequeos implements Serializable {

   private static Logger log = Logger.getLogger(ControlTiposChequeos.class);

   @EJB
   AdministrarTiposChequeosInterface administrarTiposChequeos;
   @EJB
   AdministrarRastrosInterface administrarRastros;

   private List<TiposChequeos> listTiposChequeos;
   private List<TiposChequeos> filtrarTiposChequeos;
   private List<TiposChequeos> crearTiposChequeos;
   private List<TiposChequeos> modificarTiposChequeos;
   private List<TiposChequeos> borrarTiposChequeos;
   private TiposChequeos nuevoTiposChequeos;
   private TiposChequeos duplicarTiposChequeos;
   private TiposChequeos editarTiposChequeos;
   private TiposChequeos tiposChequeosSeleccionado;
   //otros
   private int cualCelda, tipoLista, index, tipoActualizacion, k, bandera;
   private BigInteger l;
   private boolean aceptar, guardado;
   //AutoCompletar
   private boolean permitirIndex;
   //RASTRO
   private BigInteger secRegistro;
   private Column codigo, descripcion;
   //borrado
   private int registrosBorrados;
   private String mensajeValidacion;
   //filtrado table
   private int tamano;
   private Integer backUpCodigo;
   private String backUpDescripcion;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

   public ControlTiposChequeos() {
      listTiposChequeos = null;
      crearTiposChequeos = new ArrayList<TiposChequeos>();
      modificarTiposChequeos = new ArrayList<TiposChequeos>();
      borrarTiposChequeos = new ArrayList<TiposChequeos>();
      permitirIndex = true;
      editarTiposChequeos = new TiposChequeos();
      nuevoTiposChequeos = new TiposChequeos();
      duplicarTiposChequeos = new TiposChequeos();
      guardado = true;
      tamano = 270;
      mapParametros.put("paginaAnterior", paginaAnterior);
      log.info("controlTiposChequeos Constructor");
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
         log.info("ControlTiposChequeos PostConstruct ");
         FacesContext x = FacesContext.getCurrentInstance();
         HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
         administrarTiposChequeos.obtenerConexion(ses.getId());
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
      String pagActual = "tipochequeo";
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

   public String redirigirPaginaAnterior() {
      return paginaAnterior;
   }

   public void eventoFiltrar() {
      try {
         log.info("\n ENTRE A ControlTiposChequeos.eventoFiltrar \n");
         if (tipoLista == 0) {
            tipoLista = 1;
         }
         RequestContext context = RequestContext.getCurrentInstance();
         infoRegistro = "Cantidad de registros: " + filtrarTiposChequeos.size();
         RequestContext.getCurrentInstance().update("form:informacionRegistro");
      } catch (Exception e) {
         log.warn("Error ControlTiposChequeos eventoFiltrar ERROR===" + e.getMessage());
      }
   }

   public void cambiarIndice(int indice, int celda) {
      log.error("TIPO LISTA = " + tipoLista);

      if (permitirIndex == true) {
         index = indice;
         cualCelda = celda;
         if (tipoLista == 0) {
            if (cualCelda == 0) {
               backUpCodigo = listTiposChequeos.get(index).getCodigo();
               log.info(" backUpCodigo : " + backUpCodigo);
            } else if (cualCelda == 1) {
               backUpDescripcion = listTiposChequeos.get(index).getDescripcion();
               log.info(" backUpDescripcion : " + backUpDescripcion);
            }
            secRegistro = listTiposChequeos.get(index).getSecuencia();
         } else {
            if (cualCelda == 0) {
               backUpCodigo = filtrarTiposChequeos.get(index).getCodigo();
               log.info(" backUpCodigo : " + backUpCodigo);

            } else if (cualCelda == 1) {
               backUpDescripcion = filtrarTiposChequeos.get(index).getDescripcion();
               log.info(" backUpDescripcion : " + backUpDescripcion);

            }
            secRegistro = filtrarTiposChequeos.get(index).getSecuencia();
         }

      }
      log.info("Indice: " + index + " Celda: " + cualCelda);
   }

   public void asignarIndex(Integer indice, int LND, int dig) {
      try {
         log.info("\n ENTRE A ControlTiposChequeos.asignarIndex \n");
         index = indice;
         if (LND == 0) {
            tipoActualizacion = 0;
         } else if (LND == 1) {
            tipoActualizacion = 1;
            log.info("Tipo Actualizacion: " + tipoActualizacion);
         } else if (LND == 2) {
            tipoActualizacion = 2;
         }

      } catch (Exception e) {
         log.warn("Error ControlTiposChequeos.asignarIndex ERROR======" + e.getMessage());
      }
   }

   public void activarAceptar() {
      aceptar = false;
   }

   public void listaValoresBoton() {
   }
   private String infoRegistro;

   public void cancelarModificacion() {
      if (bandera == 1) {
         //CERRAR FILTRADO
         FacesContext c = FacesContext.getCurrentInstance();
         codigo = (Column) c.getViewRoot().findComponent("form:datosTiposChequeos:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposChequeos:descripcion");
         descripcion.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosTiposChequeos");
         bandera = 0;
         filtrarTiposChequeos = null;
         tipoLista = 0;
         tamano = 270;
      }

      borrarTiposChequeos.clear();
      crearTiposChequeos.clear();
      modificarTiposChequeos.clear();
      index = -1;
      secRegistro = null;
      k = 0;
      listTiposChequeos = null;
      guardado = true;
      permitirIndex = true;
      getListTiposChequeos();
      RequestContext context = RequestContext.getCurrentInstance();
      if (listTiposChequeos == null || listTiposChequeos.isEmpty()) {
         infoRegistro = "Cantidad de registros: 0 ";
      } else {
         infoRegistro = "Cantidad de registros: " + listTiposChequeos.size();
      }
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
      RequestContext.getCurrentInstance().update("form:datosTiposChequeos");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void salir() {
      limpiarListasValor();
      if (bandera == 1) {
         //CERRAR FILTRADO
         FacesContext c = FacesContext.getCurrentInstance();
         codigo = (Column) c.getViewRoot().findComponent("form:datosTiposChequeos:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposChequeos:descripcion");
         descripcion.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosTiposChequeos");
         bandera = 0;
         filtrarTiposChequeos = null;
         tipoLista = 0;
         tamano = 270;
      }

      borrarTiposChequeos.clear();
      crearTiposChequeos.clear();
      modificarTiposChequeos.clear();
      index = -1;
      secRegistro = null;
      k = 0;
      listTiposChequeos = null;
      guardado = true;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:datosTiposChequeos");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void activarCtrlF11() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 0) {
         tamano = 250;
         codigo = (Column) c.getViewRoot().findComponent("form:datosTiposChequeos:codigo");
         codigo.setFilterStyle("width: 85% !important;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposChequeos:descripcion");
         descripcion.setFilterStyle("width: 85% !important;");
         RequestContext.getCurrentInstance().update("form:datosTiposChequeos");
         log.info("Activar");
         bandera = 1;
      } else if (bandera == 1) {
         log.info("Desactivar");
         tamano = 270;
         codigo = (Column) c.getViewRoot().findComponent("form:datosTiposChequeos:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposChequeos:descripcion");
         descripcion.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosTiposChequeos");
         bandera = 0;
         filtrarTiposChequeos = null;
         tipoLista = 0;
      }
   }

   public void modificarTiposChequeos(int indice, String confirmarCambio, String valorConfirmar) {
      log.error("ENTRE A MODIFICAR SUB CATEGORIA");
      index = indice;

      int contador = 0;
      boolean banderita = false;
      Integer a;
      a = null;
      RequestContext context = RequestContext.getCurrentInstance();
      log.error("TIPO LISTA = " + tipoLista);
      if (confirmarCambio.equalsIgnoreCase("N")) {
         log.error("ENTRE A MODIFICAR EMPRESAS, CONFIRMAR CAMBIO ES N");
         if (tipoLista == 0) {
            if (!crearTiposChequeos.contains(listTiposChequeos.get(indice))) {
               if (listTiposChequeos.get(indice).getCodigo() == a) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  banderita = false;
                  listTiposChequeos.get(indice).setCodigo(backUpCodigo);
               } else {
                  for (int j = 0; j < listTiposChequeos.size(); j++) {
                     if (j != indice) {
                        if (listTiposChequeos.get(indice).getCodigo().equals(listTiposChequeos.get(j).getCodigo())) {
                           contador++;
                        }
                     }
                  }
                  if (contador > 0) {
                     mensajeValidacion = "CODIGOS REPETIDOS";
                     listTiposChequeos.get(indice).setCodigo(backUpCodigo);
                     banderita = false;
                  } else {
                     banderita = true;
                  }

               }
               if (listTiposChequeos.get(indice).getDescripcion().isEmpty()) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  banderita = false;
                  listTiposChequeos.get(indice).setDescripcion(backUpDescripcion);
               }
               if (listTiposChequeos.get(indice).getDescripcion().equals(" ")) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  banderita = false;
                  listTiposChequeos.get(indice).setDescripcion(backUpDescripcion);
               }

               if (banderita == true) {
                  if (modificarTiposChequeos.isEmpty()) {
                     modificarTiposChequeos.add(listTiposChequeos.get(indice));
                  } else if (!modificarTiposChequeos.contains(listTiposChequeos.get(indice))) {
                     modificarTiposChequeos.add(listTiposChequeos.get(indice));
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
               if (listTiposChequeos.get(indice).getCodigo() == a) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  banderita = false;
                  listTiposChequeos.get(indice).setCodigo(backUpCodigo);
               } else {
                  for (int j = 0; j < listTiposChequeos.size(); j++) {
                     if (j != indice) {
                        if (listTiposChequeos.get(indice).getCodigo().equals(listTiposChequeos.get(j).getCodigo())) {
                           contador++;
                        }
                     }
                  }
                  if (contador > 0) {
                     mensajeValidacion = "CODIGOS REPETIDOS";
                     listTiposChequeos.get(indice).setCodigo(backUpCodigo);
                     banderita = false;
                  } else {
                     banderita = true;
                  }

               }
               if (listTiposChequeos.get(indice).getDescripcion().isEmpty()) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  banderita = false;
                  listTiposChequeos.get(indice).setDescripcion(backUpDescripcion);
               }
               if (listTiposChequeos.get(indice).getDescripcion().equals(" ")) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  banderita = false;
                  listTiposChequeos.get(indice).setDescripcion(backUpDescripcion);
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
         } else if (!crearTiposChequeos.contains(filtrarTiposChequeos.get(indice))) {
            if (filtrarTiposChequeos.get(indice).getCodigo() == a) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               filtrarTiposChequeos.get(indice).setCodigo(backUpCodigo);
               banderita = false;
            } else {

               for (int j = 0; j < filtrarTiposChequeos.size(); j++) {
                  if (j != indice) {
                     if (filtrarTiposChequeos.get(indice).getCodigo().equals(filtrarTiposChequeos.get(j).getCodigo())) {
                        contador++;
                     }
                  }
               }
               if (contador > 0) {
                  mensajeValidacion = "CODIGOS REPETIDOS";
                  filtrarTiposChequeos.get(indice).setCodigo(backUpCodigo);
                  banderita = false;
               } else {
                  banderita = true;
               }

            }

            if (filtrarTiposChequeos.get(indice).getDescripcion().isEmpty()) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               banderita = false;
               filtrarTiposChequeos.get(indice).setDescripcion(backUpDescripcion);
            }
            if (filtrarTiposChequeos.get(indice).getDescripcion().equals(" ")) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               banderita = false;
               filtrarTiposChequeos.get(indice).setDescripcion(backUpDescripcion);
            }

            if (banderita == true) {
               if (modificarTiposChequeos.isEmpty()) {
                  modificarTiposChequeos.add(filtrarTiposChequeos.get(indice));
               } else if (!modificarTiposChequeos.contains(filtrarTiposChequeos.get(indice))) {
                  modificarTiposChequeos.add(filtrarTiposChequeos.get(indice));
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
            if (filtrarTiposChequeos.get(indice).getCodigo() == a) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               filtrarTiposChequeos.get(indice).setCodigo(backUpCodigo);
               banderita = false;
            } else {

               for (int j = 0; j < filtrarTiposChequeos.size(); j++) {
                  if (j != indice) {
                     if (filtrarTiposChequeos.get(indice).getCodigo().equals(filtrarTiposChequeos.get(j).getCodigo())) {
                        contador++;
                     }
                  }
               }
               if (contador > 0) {
                  mensajeValidacion = "CODIGOS REPETIDOS";
                  filtrarTiposChequeos.get(indice).setCodigo(backUpCodigo);
                  banderita = false;
               } else {
                  banderita = true;
               }

            }

            if (filtrarTiposChequeos.get(indice).getDescripcion().isEmpty()) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               banderita = false;
               filtrarTiposChequeos.get(indice).setDescripcion(backUpDescripcion);
            }
            if (filtrarTiposChequeos.get(indice).getDescripcion().equals(" ")) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               banderita = false;
               filtrarTiposChequeos.get(indice).setDescripcion(backUpDescripcion);
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
         RequestContext.getCurrentInstance().update("form:datosTiposChequeos");
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      }

   }

   public void borrandoTiposChequeos() {

      if (index >= 0) {
         if (tipoLista == 0) {
            log.info("Entro a borrandoTiposChequeos");
            if (!modificarTiposChequeos.isEmpty() && modificarTiposChequeos.contains(listTiposChequeos.get(index))) {
               int modIndex = modificarTiposChequeos.indexOf(listTiposChequeos.get(index));
               modificarTiposChequeos.remove(modIndex);
               borrarTiposChequeos.add(listTiposChequeos.get(index));
            } else if (!crearTiposChequeos.isEmpty() && crearTiposChequeos.contains(listTiposChequeos.get(index))) {
               int crearIndex = crearTiposChequeos.indexOf(listTiposChequeos.get(index));
               crearTiposChequeos.remove(crearIndex);
            } else {
               borrarTiposChequeos.add(listTiposChequeos.get(index));
            }
            listTiposChequeos.remove(index);
         }
         if (tipoLista == 1) {
            log.info("borrandoTiposChequeos ");
            if (!modificarTiposChequeos.isEmpty() && modificarTiposChequeos.contains(filtrarTiposChequeos.get(index))) {
               int modIndex = modificarTiposChequeos.indexOf(filtrarTiposChequeos.get(index));
               modificarTiposChequeos.remove(modIndex);
               borrarTiposChequeos.add(filtrarTiposChequeos.get(index));
            } else if (!crearTiposChequeos.isEmpty() && crearTiposChequeos.contains(filtrarTiposChequeos.get(index))) {
               int crearIndex = crearTiposChequeos.indexOf(filtrarTiposChequeos.get(index));
               crearTiposChequeos.remove(crearIndex);
            } else {
               borrarTiposChequeos.add(filtrarTiposChequeos.get(index));
            }
            int VCIndex = listTiposChequeos.indexOf(filtrarTiposChequeos.get(index));
            listTiposChequeos.remove(VCIndex);
            filtrarTiposChequeos.remove(index);

         }
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:datosTiposChequeos");
         infoRegistro = "Cantidad de registros: " + listTiposChequeos.size();
         RequestContext.getCurrentInstance().update("form:informacionRegistro");

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
      BigInteger contarChequeosMedicosTipoChequeo;
      BigInteger contarTiposExamenesCargosTipoChequeo;

      try {
         log.error("Control Secuencia de ControlTiposChequeos ");
         if (tipoLista == 0) {
            contarChequeosMedicosTipoChequeo = administrarTiposChequeos.contarChequeosMedicosTipoChequeo(listTiposChequeos.get(index).getSecuencia());
            contarTiposExamenesCargosTipoChequeo = administrarTiposChequeos.contarTiposExamenesCargosTipoChequeo(listTiposChequeos.get(index).getSecuencia());
         } else {
            contarChequeosMedicosTipoChequeo = administrarTiposChequeos.contarChequeosMedicosTipoChequeo(filtrarTiposChequeos.get(index).getSecuencia());
            contarTiposExamenesCargosTipoChequeo = administrarTiposChequeos.contarTiposExamenesCargosTipoChequeo(filtrarTiposChequeos.get(index).getSecuencia());
         }
         if (contarChequeosMedicosTipoChequeo.equals(new BigInteger("0"))
                 && contarTiposExamenesCargosTipoChequeo.equals(new BigInteger("0"))) {
            log.info("Borrado==0");
            borrandoTiposChequeos();
         } else {
            log.info("Borrado>0");

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:validacionBorrar");
            RequestContext.getCurrentInstance().execute("PF('validacionBorrar').show()");
            index = -1;

         }
      } catch (Exception e) {
         log.error("ERROR ControlTiposChequeos verificarBorrado ERROR  ", e);
      }
   }

   public void revisarDialogoGuardar() {

      if (!borrarTiposChequeos.isEmpty() || !crearTiposChequeos.isEmpty() || !modificarTiposChequeos.isEmpty()) {
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:confirmarGuardar");
         RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
      }

   }

   public void guardarTiposChequeos() {
      RequestContext context = RequestContext.getCurrentInstance();

      if (guardado == false) {
         log.info("Realizando guardarTiposChequeos");
         if (!borrarTiposChequeos.isEmpty()) {
            administrarTiposChequeos.borrarTiposChequeos(borrarTiposChequeos);
            //mostrarBorrados
            registrosBorrados = borrarTiposChequeos.size();
            RequestContext.getCurrentInstance().update("form:mostrarBorrados");
            RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
            borrarTiposChequeos.clear();
         }
         if (!modificarTiposChequeos.isEmpty()) {
            administrarTiposChequeos.modificarTiposChequeos(modificarTiposChequeos);
            modificarTiposChequeos.clear();
         }
         if (!crearTiposChequeos.isEmpty()) {
            administrarTiposChequeos.crearTiposChequeos(crearTiposChequeos);
            crearTiposChequeos.clear();
         }
         log.info("Se guardaron los datos con exito");
         listTiposChequeos = null;
         RequestContext.getCurrentInstance().update("form:datosTiposChequeos");
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
            editarTiposChequeos = listTiposChequeos.get(index);
         }
         if (tipoLista == 1) {
            editarTiposChequeos = filtrarTiposChequeos.get(index);
         }

         RequestContext context = RequestContext.getCurrentInstance();
         log.info("Entro a editar... valor celda: " + cualCelda);
         if (cualCelda == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editCodigo");
            RequestContext.getCurrentInstance().execute("PF('editCodigo').show()");
            cualCelda = -1;
         } else if (cualCelda == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editDescripcion");
            RequestContext.getCurrentInstance().execute("PF('editDescripcion').show()");
            cualCelda = -1;
         }

      }
      index = -1;
      secRegistro = null;
   }

   public void agregarNuevoTiposChequeos() {
      log.info("agregarNuevoTiposChequeos");
      int contador = 0;
      int duplicados = 0;

      Integer a = 0;
      a = null;
      mensajeValidacion = " ";
      RequestContext context = RequestContext.getCurrentInstance();
      if (nuevoTiposChequeos.getCodigo() == a) {
         mensajeValidacion = " *Codigo \n";
         log.info("Mensaje validacion : " + mensajeValidacion);
      } else {
         log.info("codigo en Motivo Cambio Cargo: " + nuevoTiposChequeos.getCodigo());

         for (int x = 0; x < listTiposChequeos.size(); x++) {
            if (listTiposChequeos.get(x).getCodigo().equals(nuevoTiposChequeos.getCodigo())) {
               duplicados++;
            }
         }
         log.info("Antes del if Duplicados eses igual  : " + duplicados);

         if (duplicados > 0) {
            mensajeValidacion = " *Que NO Hayan Codigos Repetidos \n";
            log.info("Mensaje validacion : " + mensajeValidacion);
         } else {
            log.info("bandera");
            contador++;
         }
      }
      if (nuevoTiposChequeos.getDescripcion() == null) {
         mensajeValidacion = mensajeValidacion + " *Descripcion \n";
         log.info("Mensaje validacion : " + mensajeValidacion);

      } else if (nuevoTiposChequeos.getDescripcion().isEmpty()) {
         mensajeValidacion = mensajeValidacion + " *Descripcion \n";
         log.info("Mensaje validacion : " + mensajeValidacion);

      } else {
         log.info("bandera");
         contador++;

      }

      log.info("contador " + contador);

      if (contador == 2) {
         if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            //CERRAR FILTRADO
            log.info("Desactivar");
            codigo = (Column) c.getViewRoot().findComponent("form:datosTiposChequeos:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposChequeos:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTiposChequeos");
            bandera = 0;
            filtrarTiposChequeos = null;
            tipoLista = 0;
         }
         log.info("Despues de la bandera");

         k++;
         l = BigInteger.valueOf(k);
         nuevoTiposChequeos.setSecuencia(l);

         crearTiposChequeos.add(nuevoTiposChequeos);

         listTiposChequeos.add(nuevoTiposChequeos);
         nuevoTiposChequeos = new TiposChequeos();
         RequestContext.getCurrentInstance().update("form:datosTiposChequeos");
         infoRegistro = "Cantidad de registros: " + listTiposChequeos.size();
         RequestContext.getCurrentInstance().update("form:informacionRegistro");

         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }

         RequestContext.getCurrentInstance().execute("PF('nuevoRegistroTiposChequeos').hide()");
         index = -1;
         secRegistro = null;

      } else {
         RequestContext.getCurrentInstance().update("form:validacionNuevaCentroCosto");
         RequestContext.getCurrentInstance().execute("PF('validacionNuevaCentroCosto').show()");
         contador = 0;
      }
   }

   public void limpiarNuevoTiposChequeos() {
      log.info("limpiarNuevoTiposChequeos");
      nuevoTiposChequeos = new TiposChequeos();
      secRegistro = null;
      index = -1;

   }

   //------------------------------------------------------------------------------
   public void duplicandoTiposChequeos() {
      log.info("duplicandoTiposChequeos");
      if (index >= 0) {
         duplicarTiposChequeos = new TiposChequeos();
         k++;
         l = BigInteger.valueOf(k);

         if (tipoLista == 0) {
            duplicarTiposChequeos.setSecuencia(l);
            duplicarTiposChequeos.setCodigo(listTiposChequeos.get(index).getCodigo());
            duplicarTiposChequeos.setDescripcion(listTiposChequeos.get(index).getDescripcion());
         }
         if (tipoLista == 1) {
            duplicarTiposChequeos.setSecuencia(l);
            duplicarTiposChequeos.setCodigo(filtrarTiposChequeos.get(index).getCodigo());
            duplicarTiposChequeos.setDescripcion(filtrarTiposChequeos.get(index).getDescripcion());
         }

         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTE");
         RequestContext.getCurrentInstance().execute("PF('duplicarRegistroTiposChequeos').show()");
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
      log.error("ConfirmarDuplicar codigo " + duplicarTiposChequeos.getCodigo());
      log.error("ConfirmarDuplicar Descripcion " + duplicarTiposChequeos.getDescripcion());

      if (duplicarTiposChequeos.getCodigo() == a) {
         mensajeValidacion = mensajeValidacion + "   *Codigo \n";
         log.info("Mensaje validacion : " + mensajeValidacion);
      } else {
         for (int x = 0; x < listTiposChequeos.size(); x++) {
            if (listTiposChequeos.get(x).getCodigo().equals(duplicarTiposChequeos.getCodigo())) {
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
      if (duplicarTiposChequeos.getDescripcion() == null) {
         mensajeValidacion = mensajeValidacion + " *Descripcion \n";
         log.info("Mensaje validacion : " + mensajeValidacion);

      } else if (duplicarTiposChequeos.getDescripcion().isEmpty()) {
         mensajeValidacion = mensajeValidacion + " *Descripcion \n";
         log.info("Mensaje validacion : " + mensajeValidacion);

      } else {
         log.info("bandera");
         contador++;

      }

      if (contador == 2) {

         log.info("Datos Duplicando: " + duplicarTiposChequeos.getSecuencia() + "  " + duplicarTiposChequeos.getCodigo());
         if (crearTiposChequeos.contains(duplicarTiposChequeos)) {
            log.info("Ya lo contengo.");
         }
         listTiposChequeos.add(duplicarTiposChequeos);
         crearTiposChequeos.add(duplicarTiposChequeos);
         RequestContext.getCurrentInstance().update("form:datosTiposChequeos");
         index = -1;
         secRegistro = null;
         if (guardado == true) {
            guardado = false;
         }
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         infoRegistro = "Cantidad de registros: " + listTiposChequeos.size();
         RequestContext.getCurrentInstance().update("form:informacionRegistro");

         if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            //CERRAR FILTRADO
            codigo = (Column) c.getViewRoot().findComponent("form:datosTiposChequeos:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposChequeos:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTiposChequeos");
            bandera = 0;
            filtrarTiposChequeos = null;
            tipoLista = 0;
         }
         duplicarTiposChequeos = new TiposChequeos();
         RequestContext.getCurrentInstance().execute("PF('duplicarRegistroTiposChequeos').hide()");

      } else {
         contador = 0;
         RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
         RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
      }
   }

   public void limpiarDuplicarTiposChequeos() {
      duplicarTiposChequeos = new TiposChequeos();
   }

   public void exportPDF() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTiposChequeosExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "TIPOSCHEQUEOS", false, false, "UTF-8", null, null);
      context.responseComplete();
      index = -1;
      secRegistro = null;
   }

   public void exportXLS() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTiposChequeosExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "TIPOSCHEQUEOS", false, false, "UTF-8", null, null);
      context.responseComplete();
      index = -1;
      secRegistro = null;
   }

   public void verificarRastro() {
      RequestContext context = RequestContext.getCurrentInstance();
      log.info("lol");
      if (!listTiposChequeos.isEmpty()) {
         if (secRegistro != null) {
            log.info("lol 2");
            int resultado = administrarRastros.obtenerTabla(secRegistro, "TIPOSCHEQUEOS"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
      } else if (administrarRastros.verificarHistoricosTabla("TIPOSCHEQUEOS")) { // igual acá
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
      index = -1;
   }

   //*/*/*/*/*/*/*/*/*/*-/-*//-*/-*/*/*-*/-*/-*/*/*/*/*/---/*/*/*/*/-*/-*/-*/-*/-*/
   public List<TiposChequeos> getListTiposChequeos() {
      if (listTiposChequeos == null) {
         log.info("ControlTiposChequeos getListTiposChequeos");
         listTiposChequeos = administrarTiposChequeos.consultarTiposChequeos();
      }
      RequestContext context = RequestContext.getCurrentInstance();
      if (listTiposChequeos == null || listTiposChequeos.isEmpty()) {
         infoRegistro = "Cantidad de registros: 0 ";
      } else {
         infoRegistro = "Cantidad de registros: " + listTiposChequeos.size();
      }
      return listTiposChequeos;
   }

   public void setListTiposChequeos(List<TiposChequeos> listTiposChequeos) {
      this.listTiposChequeos = listTiposChequeos;
   }

   public List<TiposChequeos> getFiltrarTiposChequeos() {
      return filtrarTiposChequeos;
   }

   public void setFiltrarTiposChequeos(List<TiposChequeos> filtrarTiposChequeos) {
      this.filtrarTiposChequeos = filtrarTiposChequeos;
   }

   public TiposChequeos getNuevoTiposChequeos() {
      return nuevoTiposChequeos;
   }

   public void setNuevoTiposChequeos(TiposChequeos nuevoTiposChequeos) {
      this.nuevoTiposChequeos = nuevoTiposChequeos;
   }

   public TiposChequeos getDuplicarTiposChequeos() {
      return duplicarTiposChequeos;
   }

   public void setDuplicarTiposChequeos(TiposChequeos duplicarTiposChequeos) {
      this.duplicarTiposChequeos = duplicarTiposChequeos;
   }

   public TiposChequeos getEditarTiposChequeos() {
      return editarTiposChequeos;
   }

   public void setEditarTiposChequeos(TiposChequeos editarTiposChequeos) {
      this.editarTiposChequeos = editarTiposChequeos;
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

   public TiposChequeos getTiposChequeosSeleccionado() {
      return tiposChequeosSeleccionado;
   }

   public void setTiposChequeosSeleccionado(TiposChequeos clasesPensionesSeleccionado) {
      this.tiposChequeosSeleccionado = clasesPensionesSeleccionado;
   }

   public String getInfoRegistro() {
      return infoRegistro;
   }

   public void setInfoRegistro(String infoRegistro) {
      this.infoRegistro = infoRegistro;
   }

}
