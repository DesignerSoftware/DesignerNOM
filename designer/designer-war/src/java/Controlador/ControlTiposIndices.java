/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.TiposIndices;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarTiposIndicesInterface;
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
public class ControlTiposIndices implements Serializable {

   private static Logger log = Logger.getLogger(ControlTiposIndices.class);

   @EJB
   AdministrarTiposIndicesInterface administrarTiposIndices;
   @EJB
   AdministrarRastrosInterface administrarRastros;

   private List<TiposIndices> listTiposIndices;
   private List<TiposIndices> filtrarTiposIndices;
   private List<TiposIndices> crearTiposIndices;
   private List<TiposIndices> modificarTiposIndices;
   private List<TiposIndices> borrarTiposIndices;
   private TiposIndices nuevoTiposIndices;
   private TiposIndices duplicarTiposIndices;
   private TiposIndices editarTiposIndices;
   private TiposIndices tiposPensionesSeleccionado;
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

   public ControlTiposIndices() {
      listTiposIndices = null;
      crearTiposIndices = new ArrayList<TiposIndices>();
      modificarTiposIndices = new ArrayList<TiposIndices>();
      borrarTiposIndices = new ArrayList<TiposIndices>();
      permitirIndex = true;
      editarTiposIndices = new TiposIndices();
      nuevoTiposIndices = new TiposIndices();
      duplicarTiposIndices = new TiposIndices();
      guardado = true;
      tamano = 270;
      log.info("controlTiposIndices Constructor");
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
         log.info("ControlTiposIndices PostConstruct ");
         FacesContext x = FacesContext.getCurrentInstance();
         HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
         administrarTiposIndices.obtenerConexion(ses.getId());
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
      String pagActual = "tipoindice";
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
         log.info("\n ENTRE A ControlTiposIndices.eventoFiltrar \n");
         if (tipoLista == 0) {
            tipoLista = 1;
         }
         RequestContext context = RequestContext.getCurrentInstance();
         infoRegistro = "Cantidad de registros: " + filtrarTiposIndices.size();
         RequestContext.getCurrentInstance().update("form:informacionRegistro");
      } catch (Exception e) {
         log.warn("Error ControlTiposIndices eventoFiltrar ERROR===" + e.getMessage());
      }
   }

   public void cambiarIndice(int indice, int celda) {
      log.error("TIPO LISTA = " + tipoLista);

      if (permitirIndex == true) {
         index = indice;
         cualCelda = celda;
         if (tipoLista == 0) {
            if (cualCelda == 0) {
               backUpCodigo = listTiposIndices.get(index).getCodigo();
               log.info(" backUpCodigo : " + backUpCodigo);
            } else if (cualCelda == 1) {
               backUpDescripcion = listTiposIndices.get(index).getDescripcion();
               log.info(" backUpDescripcion : " + backUpDescripcion);
            }
            secRegistro = listTiposIndices.get(index).getSecuencia();
         } else {
            if (cualCelda == 0) {
               backUpCodigo = filtrarTiposIndices.get(index).getCodigo();
               log.info(" backUpCodigo : " + backUpCodigo);

            } else if (cualCelda == 1) {
               backUpDescripcion = filtrarTiposIndices.get(index).getDescripcion();
               log.info(" backUpDescripcion : " + backUpDescripcion);

            }
            secRegistro = filtrarTiposIndices.get(index).getSecuencia();
         }

      }
      log.info("Indice: " + index + " Celda: " + cualCelda);
   }

   public void asignarIndex(Integer indice, int LND, int dig) {
      try {
         log.info("\n ENTRE A ControlTiposIndices.asignarIndex \n");
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
         log.warn("Error ControlTiposIndices.asignarIndex ERROR======" + e.getMessage());
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
         codigo = (Column) c.getViewRoot().findComponent("form:datosTiposIndices:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposIndices:descripcion");
         descripcion.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosTiposIndices");
         bandera = 0;
         filtrarTiposIndices = null;
         tipoLista = 0;
         tamano = 270;
      }

      borrarTiposIndices.clear();
      crearTiposIndices.clear();
      modificarTiposIndices.clear();
      index = -1;
      secRegistro = null;
      k = 0;
      listTiposIndices = null;
      guardado = true;
      permitirIndex = true;
      getListTiposIndices();
      RequestContext context = RequestContext.getCurrentInstance();
      if (listTiposIndices == null || listTiposIndices.isEmpty()) {
         infoRegistro = "Cantidad de registros: 0 ";
      } else {
         infoRegistro = "Cantidad de registros: " + listTiposIndices.size();
      }
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
      RequestContext.getCurrentInstance().update("form:datosTiposIndices");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void salir() {
      limpiarListasValor();
      if (bandera == 1) {
         //CERRAR FILTRADO
         FacesContext c = FacesContext.getCurrentInstance();
         codigo = (Column) c.getViewRoot().findComponent("form:datosTiposIndices:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposIndices:descripcion");
         descripcion.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosTiposIndices");
         bandera = 0;
         filtrarTiposIndices = null;
         tipoLista = 0;
         tamano = 270;
      }

      borrarTiposIndices.clear();
      crearTiposIndices.clear();
      modificarTiposIndices.clear();
      index = -1;
      secRegistro = null;
      k = 0;
      listTiposIndices = null;
      guardado = true;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:datosTiposIndices");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void activarCtrlF11() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 0) {
         tamano = 250;
         codigo = (Column) c.getViewRoot().findComponent("form:datosTiposIndices:codigo");
         codigo.setFilterStyle("width: 85% !important;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposIndices:descripcion");
         descripcion.setFilterStyle("width: 85% !important;");
         RequestContext.getCurrentInstance().update("form:datosTiposIndices");
         log.info("Activar");
         bandera = 1;
      } else if (bandera == 1) {
         log.info("Desactivar");
         tamano = 270;
         codigo = (Column) c.getViewRoot().findComponent("form:datosTiposIndices:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposIndices:descripcion");
         descripcion.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosTiposIndices");
         bandera = 0;
         filtrarTiposIndices = null;
         tipoLista = 0;
      }
   }

   public void modificarTiposIndices(int indice, String confirmarCambio, String valorConfirmar) {
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
            if (!crearTiposIndices.contains(listTiposIndices.get(indice))) {
               if (listTiposIndices.get(indice).getCodigo() == a) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  banderita = false;
                  listTiposIndices.get(indice).setCodigo(backUpCodigo);
               } else {
                  for (int j = 0; j < listTiposIndices.size(); j++) {
                     if (j != indice) {
                        if (listTiposIndices.get(indice).getCodigo() == listTiposIndices.get(j).getCodigo()) {
                           contador++;
                        }
                     }
                  }
                  if (contador > 0) {
                     mensajeValidacion = "CODIGOS REPETIDOS";
                     listTiposIndices.get(indice).setCodigo(backUpCodigo);
                     banderita = false;
                  } else {
                     banderita = true;
                  }

               }
               if (listTiposIndices.get(indice).getDescripcion().isEmpty()) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  banderita = false;
                  listTiposIndices.get(indice).setDescripcion(backUpDescripcion);
               }
               if (listTiposIndices.get(indice).getDescripcion().equals(" ")) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  banderita = false;
                  listTiposIndices.get(indice).setDescripcion(backUpDescripcion);
               }

               if (banderita == true) {
                  if (modificarTiposIndices.isEmpty()) {
                     modificarTiposIndices.add(listTiposIndices.get(indice));
                  } else if (!modificarTiposIndices.contains(listTiposIndices.get(indice))) {
                     modificarTiposIndices.add(listTiposIndices.get(indice));
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
               if (listTiposIndices.get(indice).getCodigo() == a) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  banderita = false;
                  listTiposIndices.get(indice).setCodigo(backUpCodigo);
               } else {
                  for (int j = 0; j < listTiposIndices.size(); j++) {
                     if (j != indice) {
                        if (listTiposIndices.get(indice).getCodigo() == listTiposIndices.get(j).getCodigo()) {
                           contador++;
                        }
                     }
                  }
                  if (contador > 0) {
                     mensajeValidacion = "CODIGOS REPETIDOS";
                     listTiposIndices.get(indice).setCodigo(backUpCodigo);
                     banderita = false;
                  } else {
                     banderita = true;
                  }

               }
               if (listTiposIndices.get(indice).getDescripcion().isEmpty()) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  banderita = false;
                  listTiposIndices.get(indice).setDescripcion(backUpDescripcion);
               }
               if (listTiposIndices.get(indice).getDescripcion().equals(" ")) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  banderita = false;
                  listTiposIndices.get(indice).setDescripcion(backUpDescripcion);
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
         } else if (!crearTiposIndices.contains(filtrarTiposIndices.get(indice))) {
            if (filtrarTiposIndices.get(indice).getCodigo() == a) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               filtrarTiposIndices.get(indice).setCodigo(backUpCodigo);
               banderita = false;
            } else {

               for (int j = 0; j < listTiposIndices.size(); j++) {
                  if (j != indice) {
                     if (filtrarTiposIndices.get(indice).getCodigo() == listTiposIndices.get(j).getCodigo()) {
                        contador++;
                     }
                  }
               }
               if (contador > 0) {
                  mensajeValidacion = "CODIGOS REPETIDOS";
                  filtrarTiposIndices.get(indice).setCodigo(backUpCodigo);
                  banderita = false;
               } else {
                  banderita = true;
               }

            }

            if (filtrarTiposIndices.get(indice).getDescripcion().isEmpty()) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               banderita = false;
               filtrarTiposIndices.get(indice).setDescripcion(backUpDescripcion);
            }
            if (filtrarTiposIndices.get(indice).getDescripcion().equals(" ")) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               banderita = false;
               filtrarTiposIndices.get(indice).setDescripcion(backUpDescripcion);
            }

            if (banderita == true) {
               if (modificarTiposIndices.isEmpty()) {
                  modificarTiposIndices.add(filtrarTiposIndices.get(indice));
               } else if (!modificarTiposIndices.contains(filtrarTiposIndices.get(indice))) {
                  modificarTiposIndices.add(filtrarTiposIndices.get(indice));
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
            if (filtrarTiposIndices.get(indice).getCodigo() == a) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               filtrarTiposIndices.get(indice).setCodigo(backUpCodigo);
               banderita = false;
            } else {

               for (int j = 0; j < listTiposIndices.size(); j++) {
                  if (j != indice) {
                     if (filtrarTiposIndices.get(indice).getCodigo() == listTiposIndices.get(j).getCodigo()) {
                        contador++;
                     }
                  }
               }
               if (contador > 0) {
                  mensajeValidacion = "CODIGOS REPETIDOS";
                  filtrarTiposIndices.get(indice).setCodigo(backUpCodigo);
                  banderita = false;
               } else {
                  banderita = true;
               }

            }

            if (filtrarTiposIndices.get(indice).getDescripcion().isEmpty()) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               banderita = false;
               filtrarTiposIndices.get(indice).setDescripcion(backUpDescripcion);
            }
            if (filtrarTiposIndices.get(indice).getDescripcion().equals(" ")) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               banderita = false;
               filtrarTiposIndices.get(indice).setDescripcion(backUpDescripcion);
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
         RequestContext.getCurrentInstance().update("form:datosTiposIndices");
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      }

   }

   public void borrandoTiposIndices() {

      if (index >= 0) {
         if (tipoLista == 0) {
            log.info("Entro a borrandoTiposIndices");
            if (!modificarTiposIndices.isEmpty() && modificarTiposIndices.contains(listTiposIndices.get(index))) {
               int modIndex = modificarTiposIndices.indexOf(listTiposIndices.get(index));
               modificarTiposIndices.remove(modIndex);
               borrarTiposIndices.add(listTiposIndices.get(index));
            } else if (!crearTiposIndices.isEmpty() && crearTiposIndices.contains(listTiposIndices.get(index))) {
               int crearIndex = crearTiposIndices.indexOf(listTiposIndices.get(index));
               crearTiposIndices.remove(crearIndex);
            } else {
               borrarTiposIndices.add(listTiposIndices.get(index));
            }
            listTiposIndices.remove(index);
         }
         if (tipoLista == 1) {
            log.info("borrandoTiposIndices ");
            if (!modificarTiposIndices.isEmpty() && modificarTiposIndices.contains(filtrarTiposIndices.get(index))) {
               int modIndex = modificarTiposIndices.indexOf(filtrarTiposIndices.get(index));
               modificarTiposIndices.remove(modIndex);
               borrarTiposIndices.add(filtrarTiposIndices.get(index));
            } else if (!crearTiposIndices.isEmpty() && crearTiposIndices.contains(filtrarTiposIndices.get(index))) {
               int crearIndex = crearTiposIndices.indexOf(filtrarTiposIndices.get(index));
               crearTiposIndices.remove(crearIndex);
            } else {
               borrarTiposIndices.add(filtrarTiposIndices.get(index));
            }
            int VCIndex = listTiposIndices.indexOf(filtrarTiposIndices.get(index));
            listTiposIndices.remove(VCIndex);
            filtrarTiposIndices.remove(index);

         }
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:datosTiposIndices");
         infoRegistro = "Cantidad de registros: " + listTiposIndices.size();
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
      BigInteger contarIndicesTipoIndice;

      try {
         log.error("Control Secuencia de ControlTiposIndices ");
         if (tipoLista == 0) {
            contarIndicesTipoIndice = administrarTiposIndices.contarIndicesTipoIndice(listTiposIndices.get(index).getSecuencia());
         } else {
            contarIndicesTipoIndice = administrarTiposIndices.contarIndicesTipoIndice(filtrarTiposIndices.get(index).getSecuencia());
         }
         if (contarIndicesTipoIndice.equals(new BigInteger("0"))) {
            log.info("Borrado==0");
            borrandoTiposIndices();
         } else {
            log.info("Borrado>0");

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:validacionBorrar");
            RequestContext.getCurrentInstance().execute("PF('validacionBorrar').show()");
            index = -1;
            contarIndicesTipoIndice = new BigInteger("-1");

         }
      } catch (Exception e) {
         log.error("ERROR ControlTiposIndices verificarBorrado ERROR  ", e);
      }
   }

   public void revisarDialogoGuardar() {

      if (!borrarTiposIndices.isEmpty() || !crearTiposIndices.isEmpty() || !modificarTiposIndices.isEmpty()) {
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:confirmarGuardar");
         RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
      }

   }

   public void guardarTiposIndices() {
      RequestContext context = RequestContext.getCurrentInstance();

      if (guardado == false) {
         log.info("Realizando guardarTiposIndices");
         if (!borrarTiposIndices.isEmpty()) {
            administrarTiposIndices.borrarTiposIndices(borrarTiposIndices);
            //mostrarBorrados
            registrosBorrados = borrarTiposIndices.size();
            RequestContext.getCurrentInstance().update("form:mostrarBorrados");
            RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
            borrarTiposIndices.clear();
         }
         if (!modificarTiposIndices.isEmpty()) {
            administrarTiposIndices.modificarTiposIndices(modificarTiposIndices);
            modificarTiposIndices.clear();
         }
         if (!crearTiposIndices.isEmpty()) {
            administrarTiposIndices.crearTiposIndices(crearTiposIndices);
            crearTiposIndices.clear();
         }
         log.info("Se guardaron los datos con exito");
         listTiposIndices = null;
         RequestContext.getCurrentInstance().update("form:datosTiposIndices");
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
            editarTiposIndices = listTiposIndices.get(index);
         }
         if (tipoLista == 1) {
            editarTiposIndices = filtrarTiposIndices.get(index);
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

   public void agregarNuevoTiposIndices() {
      log.info("agregarNuevoTiposIndices");
      int contador = 0;
      int duplicados = 0;

      Integer a = 0;
      a = null;
      mensajeValidacion = " ";
      RequestContext context = RequestContext.getCurrentInstance();
      if (nuevoTiposIndices.getCodigo() == a) {
         mensajeValidacion = " *Codigo \n";
         log.info("Mensaje validacion : " + mensajeValidacion);
      } else {
         log.info("codigo en Motivo Cambio Cargo: " + nuevoTiposIndices.getCodigo());

         for (int x = 0; x < listTiposIndices.size(); x++) {
            if (listTiposIndices.get(x).getCodigo() == nuevoTiposIndices.getCodigo()) {
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
      if (nuevoTiposIndices.getDescripcion() == null) {
         mensajeValidacion = mensajeValidacion + " *Nombre \n";
         log.info("Mensaje validacion : " + mensajeValidacion);

      } else if (nuevoTiposIndices.getDescripcion().isEmpty()) {
         mensajeValidacion = mensajeValidacion + " *Nombre \n";
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
            codigo = (Column) c.getViewRoot().findComponent("form:datosTiposIndices:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposIndices:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTiposIndices");
            bandera = 0;
            filtrarTiposIndices = null;
            tipoLista = 0;
         }
         log.info("Despues de la bandera");

         k++;
         l = BigInteger.valueOf(k);
         nuevoTiposIndices.setSecuencia(l);

         crearTiposIndices.add(nuevoTiposIndices);

         listTiposIndices.add(nuevoTiposIndices);
         nuevoTiposIndices = new TiposIndices();
         RequestContext.getCurrentInstance().update("form:datosTiposIndices");
         infoRegistro = "Cantidad de registros: " + listTiposIndices.size();
         RequestContext.getCurrentInstance().update("form:informacionRegistro");

         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }

         RequestContext.getCurrentInstance().execute("PF('nuevoRegistroTiposIndices').hide()");
         index = -1;
         secRegistro = null;

      } else {
         RequestContext.getCurrentInstance().update("form:validacionNuevaCentroCosto");
         RequestContext.getCurrentInstance().execute("PF('validacionNuevaCentroCosto').show()");
         contador = 0;
      }
   }

   public void limpiarNuevoTiposIndices() {
      log.info("limpiarNuevoTiposIndices");
      nuevoTiposIndices = new TiposIndices();
      secRegistro = null;
      index = -1;

   }

   //------------------------------------------------------------------------------
   public void duplicandoTiposIndices() {
      log.info("duplicandoTiposIndices");
      if (index >= 0) {
         duplicarTiposIndices = new TiposIndices();
         k++;
         l = BigInteger.valueOf(k);

         if (tipoLista == 0) {
            duplicarTiposIndices.setSecuencia(l);
            duplicarTiposIndices.setCodigo(listTiposIndices.get(index).getCodigo());
            duplicarTiposIndices.setDescripcion(listTiposIndices.get(index).getDescripcion());
         }
         if (tipoLista == 1) {
            duplicarTiposIndices.setSecuencia(l);
            duplicarTiposIndices.setCodigo(filtrarTiposIndices.get(index).getCodigo());
            duplicarTiposIndices.setDescripcion(filtrarTiposIndices.get(index).getDescripcion());
         }

         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTE");
         RequestContext.getCurrentInstance().execute("PF('duplicarRegistroTiposIndices').show()");
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
      log.error("ConfirmarDuplicar codigo " + duplicarTiposIndices.getCodigo());
      log.error("ConfirmarDuplicar Descripcion " + duplicarTiposIndices.getDescripcion());

      if (duplicarTiposIndices.getCodigo() == a) {
         mensajeValidacion = mensajeValidacion + "   *Codigo \n";
         log.info("Mensaje validacion : " + mensajeValidacion);
      } else {
         for (int x = 0; x < listTiposIndices.size(); x++) {
            if (listTiposIndices.get(x).getCodigo() == duplicarTiposIndices.getCodigo()) {
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
      if (duplicarTiposIndices.getDescripcion() == null) {
         mensajeValidacion = mensajeValidacion + " *Nombre \n";
         log.info("Mensaje validacion : " + mensajeValidacion);

      } else if (duplicarTiposIndices.getDescripcion().isEmpty()) {
         mensajeValidacion = mensajeValidacion + " *Nombre \n";
         log.info("Mensaje validacion : " + mensajeValidacion);

      } else {
         log.info("bandera");
         contador++;

      }

      if (contador == 2) {

         log.info("Datos Duplicando: " + duplicarTiposIndices.getSecuencia() + "  " + duplicarTiposIndices.getCodigo());
         if (crearTiposIndices.contains(duplicarTiposIndices)) {
            log.info("Ya lo contengo.");
         }
         listTiposIndices.add(duplicarTiposIndices);
         crearTiposIndices.add(duplicarTiposIndices);
         RequestContext.getCurrentInstance().update("form:datosTiposIndices");
         index = -1;
         secRegistro = null;
         if (guardado == true) {
            guardado = false;
         }
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         infoRegistro = "Cantidad de registros: " + listTiposIndices.size();
         RequestContext.getCurrentInstance().update("form:informacionRegistro");

         if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            //CERRAR FILTRADO
            codigo = (Column) c.getViewRoot().findComponent("form:datosTiposIndices:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposIndices:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTiposIndices");
            bandera = 0;
            filtrarTiposIndices = null;
            tipoLista = 0;
         }
         duplicarTiposIndices = new TiposIndices();
         RequestContext.getCurrentInstance().execute("PF('duplicarRegistroTiposIndices').hide()");

      } else {
         contador = 0;
         RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
         RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
      }
   }

   public void limpiarDuplicarTiposIndices() {
      duplicarTiposIndices = new TiposIndices();
   }

   public void exportPDF() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTiposIndicesExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "TIPOSINDICES", false, false, "UTF-8", null, null);
      context.responseComplete();
      index = -1;
      secRegistro = null;
   }

   public void exportXLS() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTiposIndicesExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "TIPOSINDICES", false, false, "UTF-8", null, null);
      context.responseComplete();
      index = -1;
      secRegistro = null;
   }

   public void verificarRastro() {
      RequestContext context = RequestContext.getCurrentInstance();
      log.info("lol");
      if (!listTiposIndices.isEmpty()) {
         if (secRegistro != null) {
            log.info("lol 2");
            int resultado = administrarRastros.obtenerTabla(secRegistro, "TIPOSINDICES"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
      } else if (administrarRastros.verificarHistoricosTabla("TIPOSINDICES")) { // igual acá
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
      index = -1;
   }

   //*/*/*/*/*/*/*/*/*/*-/-*//-*/-*/*/*-*/-*/-*/*/*/*/*/---/*/*/*/*/-*/-*/-*/-*/-*/
   public List<TiposIndices> getListTiposIndices() {
      if (listTiposIndices == null) {
         log.info("ControlTiposIndices getListTiposIndices");
         listTiposIndices = administrarTiposIndices.consultarTiposIndices();
      }
      RequestContext context = RequestContext.getCurrentInstance();
      if (listTiposIndices == null || listTiposIndices.isEmpty()) {
         infoRegistro = "Cantidad de registros: 0 ";
      } else {
         infoRegistro = "Cantidad de registros: " + listTiposIndices.size();
      }
      return listTiposIndices;
   }

   public void setListTiposIndices(List<TiposIndices> listTiposIndices) {
      this.listTiposIndices = listTiposIndices;
   }

   public List<TiposIndices> getFiltrarTiposIndices() {
      return filtrarTiposIndices;
   }

   public void setFiltrarTiposIndices(List<TiposIndices> filtrarTiposIndices) {
      this.filtrarTiposIndices = filtrarTiposIndices;
   }

   public TiposIndices getNuevoTiposIndices() {
      return nuevoTiposIndices;
   }

   public void setNuevoTiposIndices(TiposIndices nuevoTiposIndices) {
      this.nuevoTiposIndices = nuevoTiposIndices;
   }

   public TiposIndices getDuplicarTiposIndices() {
      return duplicarTiposIndices;
   }

   public void setDuplicarTiposIndices(TiposIndices duplicarTiposIndices) {
      this.duplicarTiposIndices = duplicarTiposIndices;
   }

   public TiposIndices getEditarTiposIndices() {
      return editarTiposIndices;
   }

   public void setEditarTiposIndices(TiposIndices editarTiposIndices) {
      this.editarTiposIndices = editarTiposIndices;
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

   public TiposIndices getTiposIndicesSeleccionado() {
      return tiposPensionesSeleccionado;
   }

   public void setTiposIndicesSeleccionado(TiposIndices clasesPensionesSeleccionado) {
      this.tiposPensionesSeleccionado = clasesPensionesSeleccionado;
   }

   public String getInfoRegistro() {
      return infoRegistro;
   }

   public void setInfoRegistro(String infoRegistro) {
      this.infoRegistro = infoRegistro;
   }

}
