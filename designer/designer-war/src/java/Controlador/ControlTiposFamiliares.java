/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.TiposFamiliares;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarTiposFamiliaresInterface;
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
public class ControlTiposFamiliares implements Serializable {

   private static Logger log = Logger.getLogger(ControlTiposFamiliares.class);

   @EJB
   AdministrarTiposFamiliaresInterface administrarTiposFamiliares;
   @EJB
   AdministrarRastrosInterface administrarRastros;

   private List<TiposFamiliares> listTiposFamiliares;
   private List<TiposFamiliares> filtrarTiposFamiliares;
   private List<TiposFamiliares> crearTiposFamiliares;
   private List<TiposFamiliares> modificarTiposFamiliares;
   private List<TiposFamiliares> borrarTiposFamiliares;
   private TiposFamiliares nuevoTiposFamiliares;
   private TiposFamiliares duplicarTiposFamiliares;
   private TiposFamiliares editarTiposFamiliares;
   private TiposFamiliares tiposFamiliaresSeleccionado;
   //otros
   private int cualCelda, tipoLista, tipoActualizacion, k, bandera;
   private BigInteger l;
   private boolean aceptar, guardado, activarLov;
   //AutoCompletar
   private boolean permitirIndex;
   //RASTRO
   private Column codigo, descripcion;
   //borrado
   private int registrosBorrados;
   private String mensajeValidacion;
   //filtrado table
   private int tamano;
   private Integer backUpCodigo;
   private String backUpDescripcion;
   private DataTable tablaC;
   private String infoRegistro;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

   public ControlTiposFamiliares() {
      listTiposFamiliares = null;
      crearTiposFamiliares = new ArrayList<TiposFamiliares>();
      modificarTiposFamiliares = new ArrayList<TiposFamiliares>();
      borrarTiposFamiliares = new ArrayList<TiposFamiliares>();
      permitirIndex = true;
      editarTiposFamiliares = new TiposFamiliares();
      nuevoTiposFamiliares = new TiposFamiliares();
      duplicarTiposFamiliares = new TiposFamiliares();
      guardado = true;
      tamano = 270;
      activarLov = true;
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
         log.info("ControlTiposFamiliares PostConstruct ");
         FacesContext x = FacesContext.getCurrentInstance();
         HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
         administrarTiposFamiliares.obtenerConexion(ses.getId());
         administrarRastros.obtenerConexion(ses.getId());
      } catch (Exception e) {
         log.error("Error postconstruct " + this.getClass().getName() + ":  ", e);
         log.error("Causa: " + e.getCause());
      }
   }

   public void recibirParametros(Map<String, Object> map) {
      mapParametros = map;
      paginaAnterior = (String) mapParametros.get("paginaAnterior");
      listTiposFamiliares = null;
      getListTiposFamiliares();
      deshabilitarBotonLov();
      if (listTiposFamiliares != null) {
         tiposFamiliaresSeleccionado = listTiposFamiliares.get(0);
      }
   }

   //Reemplazar la funcion volverAtras, retornarPagina, Redirigir.....Atras.etc
   public void navegar(String pag) {
      FacesContext fc = FacesContext.getCurrentInstance();
      ControlListaNavegacion controlListaNavegacion = (ControlListaNavegacion) fc.getApplication().evaluateExpressionGet(fc, "#{controlListaNavegacion}", ControlListaNavegacion.class);
      String pagActual = "tipofamiliar";
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

   public void recibirPaginaEntrante(String pagina) {
      paginaAnterior = pagina;
      listTiposFamiliares = null;
      getListTiposFamiliares();
      deshabilitarBotonLov();
      if (listTiposFamiliares != null) {
         tiposFamiliaresSeleccionado = listTiposFamiliares.get(0);
      }
   }

   public String redirigirPaginaAnterior() {
      return paginaAnterior;
   }

   public void cambiarIndice(TiposFamiliares tipoFamiliar, int celda) {
      log.error("TIPO LISTA = " + tipoLista);

      if (permitirIndex == true) {
         tiposFamiliaresSeleccionado = tipoFamiliar;
         cualCelda = celda;
         if (tipoLista == 0) {
            deshabilitarBotonLov();
            if (cualCelda == 0) {
               backUpCodigo = tiposFamiliaresSeleccionado.getCodigo();
               log.info(" backUpCodigo : " + backUpCodigo);
            } else if (cualCelda == 1) {
               backUpDescripcion = tiposFamiliaresSeleccionado.getTipo();
               log.info(" backUpDescripcion : " + backUpDescripcion);
            }
            tiposFamiliaresSeleccionado.getSecuencia();
         } else {
            deshabilitarBotonLov();
            if (cualCelda == 0) {
               backUpCodigo = tiposFamiliaresSeleccionado.getCodigo();
               log.info(" backUpCodigo : " + backUpCodigo);

            } else if (cualCelda == 1) {
               backUpDescripcion = tiposFamiliaresSeleccionado.getTipo();
               log.info(" backUpDescripcion : " + backUpDescripcion);

            }
            tiposFamiliaresSeleccionado.getSecuencia();
         }

      }
   }

   public void asignarIndex(TiposFamiliares tipoFamiliar, int LND, int dig) {
      try {
         log.info("\n ENTRE A ControlTiposFamiliares.asignarIndex \n");
         tiposFamiliaresSeleccionado = tipoFamiliar;
         if (LND == 0) {
            tipoActualizacion = 0;
         } else if (LND == 1) {
            tipoActualizacion = 1;
            log.info("Tipo Actualizacion: " + tipoActualizacion);
         } else if (LND == 2) {
            tipoActualizacion = 2;
         }

      } catch (Exception e) {
         log.warn("Error ControlTiposFamiliares.asignarIndex ERROR======" + e.getMessage());
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
         codigo = (Column) c.getViewRoot().findComponent("form:datosTiposFamiliares:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposFamiliares:descripcion");
         descripcion.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosTiposFamiliares");
         bandera = 0;
         filtrarTiposFamiliares = null;
         tipoLista = 0;
         tamano = 270;
      }

      borrarTiposFamiliares.clear();
      crearTiposFamiliares.clear();
      modificarTiposFamiliares.clear();
      tiposFamiliaresSeleccionado = null;
      tiposFamiliaresSeleccionado = null;
      k = 0;
      listTiposFamiliares = null;
      guardado = true;
      permitirIndex = true;
      getListTiposFamiliares();
      RequestContext context = RequestContext.getCurrentInstance();
      contarRegistros();
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
      RequestContext.getCurrentInstance().update("form:datosTiposFamiliares");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void salir() {
      limpiarListasValor();
      if (bandera == 1) {
         //CERRAR FILTRADO
         FacesContext c = FacesContext.getCurrentInstance();
         codigo = (Column) c.getViewRoot().findComponent("form:datosTiposFamiliares:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposFamiliares:descripcion");
         descripcion.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosTiposFamiliares");
         bandera = 0;
         filtrarTiposFamiliares = null;
         tipoLista = 0;
         tamano = 270;
      }
      borrarTiposFamiliares.clear();
      crearTiposFamiliares.clear();
      modificarTiposFamiliares.clear();
      tiposFamiliaresSeleccionado = null;
      k = 0;
      listTiposFamiliares = null;
      getListTiposFamiliares();
      contarRegistros();
      guardado = true;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:datosTiposFamiliares");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      navegar("atras");
   }

   public void activarCtrlF11() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 0) {
         tamano = 250;
         codigo = (Column) c.getViewRoot().findComponent("form:datosTiposFamiliares:codigo");
         codigo.setFilterStyle("width: 85% !important");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposFamiliares:descripcion");
         descripcion.setFilterStyle("width: 85% !important");
         RequestContext.getCurrentInstance().update("form:datosTiposFamiliares");
         log.info("Activar");
         bandera = 1;
      } else if (bandera == 1) {
         log.info("Desactivar");
         tamano = 270;
         codigo = (Column) c.getViewRoot().findComponent("form:datosTiposFamiliares:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposFamiliares:descripcion");
         descripcion.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosTiposFamiliares");
         bandera = 0;
         filtrarTiposFamiliares = null;
         tipoLista = 0;
      }
   }

   public void modificarTiposFamiliares(TiposFamiliares tipoFamiliar, String confirmarCambio, String valorConfirmar) {
      log.error("ENTRE A MODIFICAR SUB CATEGORIA");
      tiposFamiliaresSeleccionado = tipoFamiliar;

      int contador = 0;
      boolean banderita = false;
      Integer a;
      a = null;
      RequestContext context = RequestContext.getCurrentInstance();
      log.error("TIPO LISTA = " + tipoLista);
      if (confirmarCambio.equalsIgnoreCase("N")) {
         log.error("ENTRE A MODIFICAR EMPRESAS, CONFIRMAR CAMBIO ES N");
         if (tipoLista == 0) {
            if (!crearTiposFamiliares.contains(tiposFamiliaresSeleccionado)) {
               if (tiposFamiliaresSeleccionado.getCodigo() == a) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  banderita = false;
                  tiposFamiliaresSeleccionado.setCodigo(backUpCodigo);
               } else {
                  for (int j = 0; j < listTiposFamiliares.size(); j++) {
                     if (tiposFamiliaresSeleccionado.getCodigo().equals(listTiposFamiliares.get(j).getCodigo())) {
                        contador++;
                     }
                  }
                  if (contador > 0) {
                     mensajeValidacion = "CODIGOS REPETIDOS";
                     tiposFamiliaresSeleccionado.setCodigo(backUpCodigo);
                     banderita = false;
                  } else {
                     banderita = true;
                  }

               }
               if (tiposFamiliaresSeleccionado.getTipo().isEmpty()) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  banderita = false;
                  tiposFamiliaresSeleccionado.setTipo(backUpDescripcion);
               }
               if (tiposFamiliaresSeleccionado.getTipo().equals(" ")) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  banderita = false;
                  tiposFamiliaresSeleccionado.setTipo(backUpDescripcion);
               }

               if (banderita == true) {
                  if (modificarTiposFamiliares.isEmpty()) {
                     modificarTiposFamiliares.add(tiposFamiliaresSeleccionado);
                  } else if (!modificarTiposFamiliares.contains(tiposFamiliaresSeleccionado)) {
                     modificarTiposFamiliares.add(tiposFamiliaresSeleccionado);
                  }
                  if (guardado == true) {
                     guardado = false;
                  }

               } else {
                  RequestContext.getCurrentInstance().update("form:validacionModificar");
                  RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");
               }
               tiposFamiliaresSeleccionado = null;
               tiposFamiliaresSeleccionado = null;
            } else {
               if (tiposFamiliaresSeleccionado.getCodigo() == a) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  banderita = false;
                  tiposFamiliaresSeleccionado.setCodigo(backUpCodigo);
               } else {
                  for (int j = 0; j < listTiposFamiliares.size(); j++) {
                     if (tiposFamiliaresSeleccionado.getCodigo().equals(listTiposFamiliares.get(j).getCodigo())) {
                        contador++;
                     }
                  }
                  if (contador > 0) {
                     mensajeValidacion = "CODIGOS REPETIDOS";
                     tiposFamiliaresSeleccionado.setCodigo(backUpCodigo);
                     banderita = false;
                  } else {
                     banderita = true;
                  }

               }
               if (tiposFamiliaresSeleccionado.getTipo().isEmpty()) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  banderita = false;
                  tiposFamiliaresSeleccionado.setTipo(backUpDescripcion);
               }
               if (tiposFamiliaresSeleccionado.getTipo().equals(" ")) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  banderita = false;
                  tiposFamiliaresSeleccionado.setTipo(backUpDescripcion);
               }

               if (banderita == true) {

                  if (guardado == true) {
                     guardado = false;
                  }

               } else {
                  RequestContext.getCurrentInstance().update("form:validacionModificar");
                  RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");
               }
               tiposFamiliaresSeleccionado = null;
               tiposFamiliaresSeleccionado = null;
            }
         } else if (!crearTiposFamiliares.contains(tiposFamiliaresSeleccionado)) {
            if (tiposFamiliaresSeleccionado.getCodigo() == a) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               tiposFamiliaresSeleccionado.setCodigo(backUpCodigo);
               banderita = false;
            } else {

               for (int j = 0; j < filtrarTiposFamiliares.size(); j++) {
                  if (tiposFamiliaresSeleccionado.getCodigo().equals(filtrarTiposFamiliares.get(j).getCodigo())) {
                     contador++;
                  }
               }
               if (contador > 0) {
                  mensajeValidacion = "CODIGOS REPETIDOS";
                  tiposFamiliaresSeleccionado.setCodigo(backUpCodigo);
                  banderita = false;
               } else {
                  banderita = true;
               }

            }

            if (tiposFamiliaresSeleccionado.getTipo().isEmpty()) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               banderita = false;
               tiposFamiliaresSeleccionado.setTipo(backUpDescripcion);
            }
            if (tiposFamiliaresSeleccionado.getTipo().equals(" ")) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               banderita = false;
               tiposFamiliaresSeleccionado.setTipo(backUpDescripcion);
            }

            if (banderita == true) {
               if (modificarTiposFamiliares.isEmpty()) {
                  modificarTiposFamiliares.add(tiposFamiliaresSeleccionado);
               } else if (!modificarTiposFamiliares.contains(tiposFamiliaresSeleccionado)) {
                  modificarTiposFamiliares.add(tiposFamiliaresSeleccionado);
               }
               if (guardado == true) {
                  guardado = false;
               }

            } else {
               RequestContext.getCurrentInstance().update("form:validacionModificar");
               RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");
            }
            tiposFamiliaresSeleccionado = null;
            tiposFamiliaresSeleccionado = null;
         } else {
            if (tiposFamiliaresSeleccionado.getCodigo() == a) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               tiposFamiliaresSeleccionado.setCodigo(backUpCodigo);
               banderita = false;
            } else {

               for (int j = 0; j < filtrarTiposFamiliares.size(); j++) {
                  if (tiposFamiliaresSeleccionado.getCodigo().equals(filtrarTiposFamiliares.get(j).getCodigo())) {
                     contador++;
                  }
               }
               if (contador > 0) {
                  mensajeValidacion = "CODIGOS REPETIDOS";
                  tiposFamiliaresSeleccionado.setCodigo(backUpCodigo);
                  banderita = false;
               } else {
                  banderita = true;
               }

            }

            if (tiposFamiliaresSeleccionado.getTipo().isEmpty()) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               banderita = false;
               tiposFamiliaresSeleccionado.setTipo(backUpDescripcion);
            }
            if (tiposFamiliaresSeleccionado.getTipo().equals(" ")) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               banderita = false;
               tiposFamiliaresSeleccionado.setTipo(backUpDescripcion);
            }

            if (banderita == true) {

               if (guardado == true) {
                  guardado = false;
               }

            } else {
               RequestContext.getCurrentInstance().update("form:validacionModificar");
               RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");
            }
            tiposFamiliaresSeleccionado = null;
            tiposFamiliaresSeleccionado = null;
         }
         RequestContext.getCurrentInstance().update("form:datosTiposFamiliares");
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      }

   }

   public void borrandoTiposFamiliares() {

      if (tiposFamiliaresSeleccionado != null) {
         log.info("Entro a borrandoTiposFamiliares");
         if (!modificarTiposFamiliares.isEmpty() && modificarTiposFamiliares.contains(tiposFamiliaresSeleccionado)) {
            int modIndex = modificarTiposFamiliares.indexOf(tiposFamiliaresSeleccionado);
            modificarTiposFamiliares.remove(modIndex);
            borrarTiposFamiliares.add(tiposFamiliaresSeleccionado);
         } else if (!crearTiposFamiliares.isEmpty() && crearTiposFamiliares.contains(tiposFamiliaresSeleccionado)) {
            int crearIndex = crearTiposFamiliares.indexOf(tiposFamiliaresSeleccionado);
            crearTiposFamiliares.remove(crearIndex);
         } else {
            borrarTiposFamiliares.add(tiposFamiliaresSeleccionado);
         }
         listTiposFamiliares.remove(tiposFamiliaresSeleccionado);

         if (tipoLista == 1) {
            filtrarTiposFamiliares.remove(tiposFamiliaresSeleccionado);
         }
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:datosTiposFamiliares");
         contarRegistros();
         tiposFamiliaresSeleccionado = null;

         if (guardado == true) {
            guardado = false;
         }
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      } else {
         RequestContext.getCurrentInstance().execute("PF('formularioDialogos:seleccionarRegistro').show()");
      }
   }

   public void verificarBorrado() {
      log.info("Estoy en verificarBorrado");
      BigInteger contarHvReferenciasTipoFamiliar;

      try {
         log.error("Control Secuencia de ControlTiposFamiliares ");
         if (tipoLista == 0) {
            contarHvReferenciasTipoFamiliar = administrarTiposFamiliares.contarHvReferenciasTipoFamiliar(tiposFamiliaresSeleccionado.getSecuencia());
         } else {
            contarHvReferenciasTipoFamiliar = administrarTiposFamiliares.contarHvReferenciasTipoFamiliar(tiposFamiliaresSeleccionado.getSecuencia());
         }
         if (contarHvReferenciasTipoFamiliar.equals(new BigInteger("0"))) {
            log.info("Borrado==0");
            borrandoTiposFamiliares();
         } else {
            log.info("Borrado>0");

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:validacionBorrar");
            RequestContext.getCurrentInstance().execute("PF('validacionBorrar').show()");
            tiposFamiliaresSeleccionado = null;
            contarHvReferenciasTipoFamiliar = new BigInteger("-1");

         }
      } catch (Exception e) {
         log.error("ERROR ControlTiposFamiliares verificarBorrado ERROR  ", e);
      }
   }

   public void revisarDialogoGuardar() {

      if (!borrarTiposFamiliares.isEmpty() || !crearTiposFamiliares.isEmpty() || !modificarTiposFamiliares.isEmpty()) {
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:confirmarGuardar");
         RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
      }

   }

   public void guardarYSalir() {
      guardarTiposFamiliares();
      salir();
   }

   public void guardarTiposFamiliares() {
      RequestContext context = RequestContext.getCurrentInstance();

      if (guardado == false) {
         log.info("Realizando guardarTiposFamiliares");
         if (!borrarTiposFamiliares.isEmpty()) {
            administrarTiposFamiliares.borrarTiposFamiliares(borrarTiposFamiliares);
            //mostrarBorrados
            registrosBorrados = borrarTiposFamiliares.size();
            RequestContext.getCurrentInstance().update("form:mostrarBorrados");
            RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
            borrarTiposFamiliares.clear();
         }
         if (!modificarTiposFamiliares.isEmpty()) {
            administrarTiposFamiliares.modificarTiposFamiliares(modificarTiposFamiliares);
            modificarTiposFamiliares.clear();
         }
         if (!crearTiposFamiliares.isEmpty()) {
            administrarTiposFamiliares.crearTiposFamiliares(crearTiposFamiliares);
            crearTiposFamiliares.clear();
         }
         log.info("Se guardaron los datos con exito");
         listTiposFamiliares = null;
         getListTiposFamiliares();
         contarRegistros();
         RequestContext.getCurrentInstance().update("form:datosTiposFamiliares");
         k = 0;
         guardado = true;
         FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      }
      tiposFamiliaresSeleccionado = null;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");

   }

   public void editarCelda() {
      if (tiposFamiliaresSeleccionado != null) {
         if (tipoLista == 0) {
            editarTiposFamiliares = tiposFamiliaresSeleccionado;
         }
         if (tipoLista == 1) {
            editarTiposFamiliares = tiposFamiliaresSeleccionado;
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

      } else {
         RequestContext.getCurrentInstance().execute("PF('formularioDialogos:seleccionarRegistro').show()");
      }
   }

   public void agregarNuevoTiposFamiliares() {
      log.info("agregarNuevoTiposFamiliares");
      int contador = 0;
      int duplicados = 0;

      Integer a = 0;
      a = null;
      mensajeValidacion = " ";
      RequestContext context = RequestContext.getCurrentInstance();
      if (nuevoTiposFamiliares.getCodigo() == a) {
         mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
      } else {
         for (int x = 0; x < listTiposFamiliares.size(); x++) {
            if (listTiposFamiliares.get(x).getCodigo().equals(nuevoTiposFamiliares.getCodigo())) {
               duplicados++;
            }
         }
         if (duplicados > 0) {
            mensajeValidacion = " *Existe un registro con el código ingresado, Por favor ingrese un código válido";
         } else {
            contador++;
         }
      }
      if (nuevoTiposFamiliares.getTipo() == null) {
         mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
      } else if (nuevoTiposFamiliares.getTipo().isEmpty()) {
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
            codigo = (Column) c.getViewRoot().findComponent("form:datosTiposFamiliares:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposFamiliares:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTiposFamiliares");
            bandera = 0;
            filtrarTiposFamiliares = null;
            tipoLista = 0;
         }

         k++;
         l = BigInteger.valueOf(k);
         nuevoTiposFamiliares.setSecuencia(l);
         crearTiposFamiliares.add(nuevoTiposFamiliares);
         listTiposFamiliares.add(nuevoTiposFamiliares);
         tiposFamiliaresSeleccionado = nuevoTiposFamiliares;
         nuevoTiposFamiliares = new TiposFamiliares();
         RequestContext.getCurrentInstance().update("form:datosTiposFamiliares");
         contarRegistros();

         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }

         RequestContext.getCurrentInstance().execute("PF('nuevoRegistroTiposFamiliares').hide()");

      } else {
         RequestContext.getCurrentInstance().update("form:validacionNuevaCentroCosto");
         RequestContext.getCurrentInstance().execute("PF('validacionNuevaCentroCosto').show()");
         contador = 0;
      }
   }

   public void limpiarNuevoTiposFamiliares() {
      log.info("limpiarNuevoTiposFamiliares");
      nuevoTiposFamiliares = new TiposFamiliares();

   }

   //------------------------------------------------------------------------------
   public void duplicandoTiposFamiliares() {
      log.info("duplicandoTiposFamiliares");
      if (tiposFamiliaresSeleccionado != null) {
         duplicarTiposFamiliares = new TiposFamiliares();
         k++;
         l = BigInteger.valueOf(k);

         if (tipoLista == 0) {
            duplicarTiposFamiliares.setSecuencia(l);
            duplicarTiposFamiliares.setCodigo(tiposFamiliaresSeleccionado.getCodigo());
            duplicarTiposFamiliares.setTipo(tiposFamiliaresSeleccionado.getTipo());
         }
         if (tipoLista == 1) {
            duplicarTiposFamiliares.setSecuencia(l);
            duplicarTiposFamiliares.setCodigo(tiposFamiliaresSeleccionado.getCodigo());
            duplicarTiposFamiliares.setTipo(tiposFamiliaresSeleccionado.getTipo());
         }

         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTE");
         RequestContext.getCurrentInstance().execute("PF('duplicarRegistroTiposFamiliares').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('formularioDialogos:seleccionarRegistro').show()");
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

      if (duplicarTiposFamiliares.getCodigo() == a) {
         mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
      } else {
         for (int x = 0; x < listTiposFamiliares.size(); x++) {
            if (listTiposFamiliares.get(x).getCodigo().equals(duplicarTiposFamiliares.getCodigo())) {
               duplicados++;
            }
         }
         if (duplicados > 0) {
            mensajeValidacion = " *Existe un registro con el código ingresado, Por favor ingrese un código válido";
         } else {
            contador++;
            duplicados = 0;
         }
      }
      if (duplicarTiposFamiliares.getTipo() == null) {
         mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
      } else if (duplicarTiposFamiliares.getTipo().isEmpty()) {
         mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
      } else {
         log.info("bandera");
         contador++;
      }
      if (contador == 2) {
         listTiposFamiliares.add(duplicarTiposFamiliares);
         crearTiposFamiliares.add(duplicarTiposFamiliares);
         tiposFamiliaresSeleccionado = duplicarTiposFamiliares;
         RequestContext.getCurrentInstance().update("form:datosTiposFamiliares");
         if (guardado == true) {
            guardado = false;
         }
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         contarRegistros();
         if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            //CERRAR FILTRADO
            codigo = (Column) c.getViewRoot().findComponent("form:datosTiposFamiliares:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposFamiliares:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTiposFamiliares");
            bandera = 0;
            filtrarTiposFamiliares = null;
            tipoLista = 0;
         }
         duplicarTiposFamiliares = new TiposFamiliares();
         RequestContext.getCurrentInstance().execute("PF('duplicarRegistroTiposFamiliares').hide()");

      } else {
         contador = 0;
         RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
         RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
      }
   }

   public void limpiarDuplicarTiposFamiliares() {
      duplicarTiposFamiliares = new TiposFamiliares();
   }

   public void exportPDF() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTiposFamiliaresExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "TIPOSFAMILIARES", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void exportXLS() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTiposFamiliaresExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "TIPOSFAMILIARES", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void verificarRastro() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (!listTiposFamiliares.isEmpty()) {
         if (tiposFamiliaresSeleccionado != null) {
            int resultado = administrarRastros.obtenerTabla(tiposFamiliaresSeleccionado.getSecuencia(), "TIPOSFAMILIARES"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
      } else if (administrarRastros.verificarHistoricosTabla("TIPOSFAMILIARES")) { // igual acá
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
   }

   public void eventoFiltrar() {
      try {
         if (tipoLista == 0) {
            tipoLista = 1;
         }
         contarRegistros();
      } catch (Exception e) {
         log.warn("Error ControlTiposFamiliares eventoFiltrar ERROR===" + e.getMessage());
      }
   }

   public void contarRegistros() {
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
   }

   public void habilitarBotonLov() {
      activarLov = false;
      RequestContext.getCurrentInstance().update("form:listaValores");
   }

   public void deshabilitarBotonLov() {
      activarLov = true;
      RequestContext.getCurrentInstance().update("form:listaValores");
   }

   public void recordarSeleccion() {
      if (tiposFamiliaresSeleccionado != null) {
         FacesContext c = FacesContext.getCurrentInstance();
         tablaC = (DataTable) c.getViewRoot().findComponent("form:datosTiposFamiliares");
         tablaC.setSelection(tiposFamiliaresSeleccionado);
      }
   }

   //*/*/*/*/*/*/*/*/*/*-/-*//-*/-*/*/*-*/-*/-*/*/*/*/*/---/*/*/*/*/-*/-*/-*/-*/-*/
   public List<TiposFamiliares> getListTiposFamiliares() {
      if (listTiposFamiliares == null) {
         listTiposFamiliares = administrarTiposFamiliares.consultarTiposFamiliares();
      }
      return listTiposFamiliares;
   }

   public void setListTiposFamiliares(List<TiposFamiliares> listTiposFamiliares) {
      this.listTiposFamiliares = listTiposFamiliares;
   }

   public List<TiposFamiliares> getFiltrarTiposFamiliares() {
      return filtrarTiposFamiliares;
   }

   public void setFiltrarTiposFamiliares(List<TiposFamiliares> filtrarTiposFamiliares) {
      this.filtrarTiposFamiliares = filtrarTiposFamiliares;
   }

   public TiposFamiliares getNuevoTiposFamiliares() {
      return nuevoTiposFamiliares;
   }

   public void setNuevoTiposFamiliares(TiposFamiliares nuevoTiposFamiliares) {
      this.nuevoTiposFamiliares = nuevoTiposFamiliares;
   }

   public TiposFamiliares getDuplicarTiposFamiliares() {
      return duplicarTiposFamiliares;
   }

   public void setDuplicarTiposFamiliares(TiposFamiliares duplicarTiposFamiliares) {
      this.duplicarTiposFamiliares = duplicarTiposFamiliares;
   }

   public TiposFamiliares getEditarTiposFamiliares() {
      return editarTiposFamiliares;
   }

   public void setEditarTiposFamiliares(TiposFamiliares editarTiposFamiliares) {
      this.editarTiposFamiliares = editarTiposFamiliares;
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

   public TiposFamiliares getTiposFamiliaresSeleccionado() {
      return tiposFamiliaresSeleccionado;
   }

   public void setTiposFamiliaresSeleccionado(TiposFamiliares clasesPensionesSeleccionado) {
      this.tiposFamiliaresSeleccionado = clasesPensionesSeleccionado;
   }

   public String getInfoRegistro() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosTiposFamiliares");
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
