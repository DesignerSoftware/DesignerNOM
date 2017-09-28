package Controlador;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import Entidades.TiposDias;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarTiposDiasInterface;
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
public class ControlTiposDias implements Serializable {

   private static Logger log = Logger.getLogger(ControlTiposDias.class);

   @EJB
   AdministrarTiposDiasInterface administrarTiposDias;
   @EJB
   AdministrarRastrosInterface administrarRastros;
   private List<TiposDias> listTiposDias;
   private List<TiposDias> filtrarTiposDias;
   private List<TiposDias> crearTiposDias;
   private List<TiposDias> modificarTiposDias;
   private List<TiposDias> borrarTiposDias;
   private TiposDias nuevoTipoDia;
   private TiposDias duplicarTipoDia;
   private TiposDias editarTipoDia;
   private TiposDias tipoDiaSeleccionado;
   //otros
   private int cualCelda, tipoLista, index, tipoActualizacion, k, bandera;
   private BigInteger l;
   private boolean aceptar, guardado;
   //AutoCompletar
   private boolean permitirIndex;
   //RASTRO
   private BigInteger secRegistro;
   private Column codigo, descripcion, tipo;
   //borrado
   private int registrosBorrados;
   private String mensajeValidacion;
   private BigInteger verificarDiasLaborales;
   private BigInteger verificarExtrasRecargos;
   private Integer backUpCodigo;
   private String backUpDescripcion;
   private int tamano;
   private String infoRegistro;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

   public ControlTiposDias() {
      listTiposDias = null;
      crearTiposDias = new ArrayList<TiposDias>();
      modificarTiposDias = new ArrayList<TiposDias>();
      borrarTiposDias = new ArrayList<TiposDias>();
      permitirIndex = true;
      editarTipoDia = new TiposDias();
      nuevoTipoDia = new TiposDias();
      duplicarTipoDia = new TiposDias();
      guardado = true;
      tamano = 270;
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
      String pagActual = "tipodia";
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
         administrarTiposDias.obtenerConexion(ses.getId());
         administrarRastros.obtenerConexion(ses.getId());
      } catch (Exception e) {
         log.error("Error postconstruct " + this.getClass().getName() + ":  ", e);
         log.error("Causa: " + e.getCause());
      }
   }

   public void recibirAtras(String atras) {
      paginaAnterior = atras;
      log.info("ControlTiposDias pagina anterior : " + paginaAnterior);
   }

   public String redireccionarAtras() {
      return paginaAnterior;
   }

   public void eventoFiltrar() {
      try {
         log.info("\n ENTRE A CONTROLTIPOSDIAS EVENTOFILTRAR \n");
         if (tipoLista == 0) {
            tipoLista = 1;
         }
         RequestContext context = RequestContext.getCurrentInstance();
         infoRegistro = "Cantidad de registros: " + filtrarTiposDias.size();
         RequestContext.getCurrentInstance().update("form:informacionRegistro");
      } catch (Exception e) {
         log.warn("Error CONTROLTIPOSDIAS EVENTOFILTRAR ERROR===" + e.getMessage());
      }
   }

   public void cambiarIndice(int indice, int celda) {
      log.error("TIPO LISTA = " + tipoLista);

      if (permitirIndex == true) {
         index = indice;
         cualCelda = celda;
         if (cualCelda == 0) {
            if (tipoLista == 0) {
               backUpCodigo = listTiposDias.get(indice).getCodigo();
            } else {
               backUpCodigo = filtrarTiposDias.get(indice).getCodigo();
            }
         }
         if (cualCelda == 1) {
            if (tipoLista == 0) {
               backUpDescripcion = listTiposDias.get(indice).getDescripcion();
            } else {
               backUpDescripcion = filtrarTiposDias.get(indice).getDescripcion();
            }
         }

         secRegistro = listTiposDias.get(index).getSecuencia();

      }
      log.info("Indice: " + index + " Celda: " + cualCelda);
   }

   public void asignarIndex(Integer indice, int LND, int dig) {
      try {
         log.info("\n ENTRE A CONTROLTIPOSDIAS ASIGNAR INDEX \n");
         index = indice;
         if (LND == 0) {
            tipoActualizacion = 0;
         } else if (LND == 1) {
            tipoActualizacion = 1;
            log.info("TIPO ACTUALIZACION : " + tipoActualizacion);
         } else if (LND == 2) {
            tipoActualizacion = 2;
         }

      } catch (Exception e) {
         log.warn("Error CONTROLTIPOSDIAS ASIGNAR INDEX ERROR =" + e.getMessage());
      }
   }

   public void activarAceptar() {
      aceptar = false;
   }

   public void listaValoresBoton() {
   }

   public void cancelarModificacion() {
      if (bandera == 1) {
         FacesContext c = FacesContext.getCurrentInstance();
         //CERRAR FILTRADO
         codigo = (Column) c.getViewRoot().findComponent("form:datosTipoDia:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosTipoDia:descripcion");
         descripcion.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosTipoDia");
         tipo = (Column) c.getViewRoot().findComponent("form:datosTipoDia:tipo");
         tipo.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosTipoDia");
         bandera = 0;
         filtrarTiposDias = null;
         tipoLista = 0;
      }

      borrarTiposDias.clear();
      crearTiposDias.clear();
      modificarTiposDias.clear();
      index = -1;
      secRegistro = null;
      k = 0;
      listTiposDias = null;
      guardado = true;
      permitirIndex = true;
      getListTiposDias();
      RequestContext context = RequestContext.getCurrentInstance();
      if (listTiposDias == null || listTiposDias.isEmpty()) {
         infoRegistro = "Cantidad de registros: 0 ";
      } else {
         infoRegistro = "Cantidad de registros: " + listTiposDias.size();
      }
      RequestContext.getCurrentInstance().update("form:datosTipoDia");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void salir() {
      limpiarListasValor();
      if (bandera == 1) {
         FacesContext c = FacesContext.getCurrentInstance();
         //CERRAR FILTRADO
         codigo = (Column) c.getViewRoot().findComponent("form:datosTipoDia:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosTipoDia:descripcion");
         descripcion.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosTipoDia");
         tipo = (Column) c.getViewRoot().findComponent("form:datosTipoDia:tipo");
         tipo.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosTipoDia");
         bandera = 0;
         filtrarTiposDias = null;
         tipoLista = 0;
      }

      borrarTiposDias.clear();
      crearTiposDias.clear();
      modificarTiposDias.clear();
      index = -1;
      secRegistro = null;
      k = 0;
      listTiposDias = null;
      guardado = true;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:datosTipoDia");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void activarCtrlF11() {
      if (bandera == 0) {
         FacesContext c = FacesContext.getCurrentInstance();
         tamano = 250;
         codigo = (Column) c.getViewRoot().findComponent("form:datosTipoDia:codigo");
         codigo.setFilterStyle("width: 85% !important;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosTipoDia:descripcion");
         descripcion.setFilterStyle("width: 85% !important;");
         tipo = (Column) c.getViewRoot().findComponent("form:datosTipoDia:tipo");
         tipo.setFilterStyle("width: 85% !important;");
         RequestContext.getCurrentInstance().update("form:datosTipoDia");
         log.info("Activar");
         bandera = 1;
      } else if (bandera == 1) {
         FacesContext c = FacesContext.getCurrentInstance();
         tamano = 270;
         log.info("Desactivar");
         codigo = (Column) c.getViewRoot().findComponent("form:datosTipoDia:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosTipoDia:descripcion");
         descripcion.setFilterStyle("display: none; visibility: hidden;");
         tipo = (Column) c.getViewRoot().findComponent("form:datosTipoDia:tipo");
         tipo.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosTipoDia");
         bandera = 0;
         filtrarTiposDias = null;
         tipoLista = 0;
      }
   }

   public void mostrarInfo(int indice, int celda) {
      if (permitirIndex == true) {
         RequestContext context = RequestContext.getCurrentInstance();

         index = indice;
         cualCelda = celda;
         secRegistro = listTiposDias.get(index).getSecuencia();
         log.info("Tipo = " + listTiposDias.get(index).getTipo());
         if (tipoLista == 0) {
            if (!crearTiposDias.contains(listTiposDias.get(indice))) {
               if (modificarTiposDias.isEmpty()) {
                  modificarTiposDias.add(listTiposDias.get(indice));
               } else if (!modificarTiposDias.contains(listTiposDias.get(indice))) {
                  modificarTiposDias.add(listTiposDias.get(indice));
               }

               if (guardado == true) {
                  guardado = false;
               }
            } else if (guardado == true) {
               guardado = false;
            }
         } else if (!crearTiposDias.contains(filtrarTiposDias.get(indice))) {
            if (modificarTiposDias.isEmpty()) {
               modificarTiposDias.add(filtrarTiposDias.get(indice));
            } else if (!modificarTiposDias.contains(filtrarTiposDias.get(indice))) {
               modificarTiposDias.add(filtrarTiposDias.get(indice));
            }

            if (guardado == true) {
               guardado = false;
            }
         } else if (guardado == true) {
            guardado = false;
         }
         RequestContext.getCurrentInstance().update("form:datosTipoDia");
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      }
      log.info("Indice: " + index + " Celda: " + cualCelda);

   }

   public void modificandoTipoDia(int indice, String confirmarCambio, String valorConfirmar) {
      log.error("ENTRE A MODIFICAR TIPOSDIAS");
      index = indice;
      boolean banderaTamano = false;
      int contador = 0;
      int contadorGuardar = 0;
      boolean banderita = false;
      Integer a;
      a = null;
      RequestContext context = RequestContext.getCurrentInstance();
      log.error("TIPO LISTA = " + tipoLista);
      if (confirmarCambio.equalsIgnoreCase("N")) {
         log.error("ENTRE A MODIFICARTIPODIA, CONFIRMAR CAMBIO ES N");
         if (tipoLista == 0) {
            if (!crearTiposDias.contains(listTiposDias.get(indice))) {
               if (listTiposDias.get(indice).getCodigo() == a) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  banderita = false;
                  listTiposDias.get(indice).setCodigo(backUpCodigo);
               } else {
                  for (int j = 0; j < listTiposDias.size(); j++) {
                     if (j != indice) {
                        if (listTiposDias.get(indice).getCodigo().equals(listTiposDias.get(j).getCodigo())) {
                           contador++;
                        }
                     }
                  }
                  if (contador > 0) {
                     mensajeValidacion = "CODIGOS REPETIDOS";
                     listTiposDias.get(indice).setCodigo(backUpCodigo);
                     banderita = false;
                  } else {
                     contadorGuardar++;
                  }

               }
               if (listTiposDias.get(indice).getDescripcion().isEmpty()) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  banderita = false;
                  listTiposDias.get(indice).setDescripcion(backUpDescripcion);
               } else if (listTiposDias.get(indice).getDescripcion().equals(" ")) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  listTiposDias.get(indice).setDescripcion(backUpDescripcion);
                  banderita = false;
               } else {
                  contadorGuardar++;
               }
               if (contadorGuardar == 2) {
                  if (modificarTiposDias.isEmpty()) {
                     modificarTiposDias.add(listTiposDias.get(indice));
                  } else if (!modificarTiposDias.contains(listTiposDias.get(indice))) {
                     modificarTiposDias.add(listTiposDias.get(indice));
                  }
                  if (guardado == true) {
                     guardado = false;
                  }

               } else if (banderaTamano == false) {
                  RequestContext.getCurrentInstance().update("form:validacionModificar");
                  RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");
               }
               index = -1;
               secRegistro = null;
            } else {
               if (listTiposDias.get(indice).getCodigo() == a) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  banderita = false;
                  listTiposDias.get(indice).setCodigo(backUpCodigo);
               } else {
                  for (int j = 0; j < listTiposDias.size(); j++) {
                     if (j != indice) {
                        if (listTiposDias.get(indice).getCodigo().equals(listTiposDias.get(j).getCodigo())) {
                           contador++;
                        }
                     }
                  }
                  if (contador > 0) {
                     mensajeValidacion = "CODIGOS REPETIDOS";
                     listTiposDias.get(indice).setCodigo(backUpCodigo);
                     banderita = false;
                  } else {
                     contadorGuardar++;
                  }

               }
               if (listTiposDias.get(indice).getDescripcion().isEmpty()) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  banderita = false;
                  listTiposDias.get(indice).setDescripcion(backUpDescripcion);
               } else if (listTiposDias.get(indice).getDescripcion().equals(" ")) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  listTiposDias.get(indice).setDescripcion(backUpDescripcion);
                  banderita = false;
               } else {
                  contadorGuardar++;
               }
               if (contadorGuardar == 2) {
                  if (guardado == true) {
                     guardado = false;
                  }
               } else if (banderaTamano == false) {
                  RequestContext.getCurrentInstance().update("form:validacionModificar");
                  RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");
               }
               index = -1;
               secRegistro = null;
            }
         } else if (!crearTiposDias.contains(filtrarTiposDias.get(indice))) {
            if (filtrarTiposDias.get(indice).getCodigo() == a) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               banderita = false;
               filtrarTiposDias.get(indice).setCodigo(backUpCodigo);
            } else {
               for (int j = 0; j < listTiposDias.size(); j++) {
                  if (j != indice) {
                     if (filtrarTiposDias.get(indice).getCodigo() == listTiposDias.get(j).getCodigo()) {
                        contador++;
                     }
                  }
               }
               for (int j = 0; j < filtrarTiposDias.size(); j++) {
                  if (j != indice) {
                     if (filtrarTiposDias.get(indice).getCodigo() == filtrarTiposDias.get(j).getCodigo()) {
                        contador++;
                     }
                  }
               }
               if (contador > 0) {
                  mensajeValidacion = "CODIGOS REPETIDOS";
                  filtrarTiposDias.get(indice).setCodigo(backUpCodigo);
                  banderita = false;
               } else {
                  contadorGuardar++;
               }

            }
            if (filtrarTiposDias.get(indice).getDescripcion().isEmpty()) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               filtrarTiposDias.get(indice).setDescripcion(backUpDescripcion);
            } else if (filtrarTiposDias.get(indice).getDescripcion().equals(" ")) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               filtrarTiposDias.get(indice).setDescripcion(backUpDescripcion);
               banderita = false;
            } else {
               contadorGuardar++;
            }

            if (contadorGuardar == 2) {
               if (modificarTiposDias.isEmpty()) {
                  modificarTiposDias.add(filtrarTiposDias.get(indice));
               } else if (!modificarTiposDias.contains(filtrarTiposDias.get(indice))) {
                  modificarTiposDias.add(filtrarTiposDias.get(indice));
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
            if (filtrarTiposDias.get(indice).getCodigo() == a) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               banderita = false;
               filtrarTiposDias.get(indice).setCodigo(backUpCodigo);
            } else {
               for (int j = 0; j < listTiposDias.size(); j++) {
                  if (j != indice) {
                     if (filtrarTiposDias.get(indice).getCodigo() == listTiposDias.get(j).getCodigo()) {
                        contador++;
                     }
                  }
               }
               for (int j = 0; j < filtrarTiposDias.size(); j++) {
                  if (j != indice) {
                     if (filtrarTiposDias.get(indice).getCodigo() == filtrarTiposDias.get(j).getCodigo()) {
                        contador++;
                     }
                  }
               }
               if (contador > 0) {
                  mensajeValidacion = "CODIGOS REPETIDOS";
                  filtrarTiposDias.get(indice).setCodigo(backUpCodigo);
                  banderita = false;
               } else {
                  contadorGuardar++;
               }

            }
            if (filtrarTiposDias.get(indice).getDescripcion().isEmpty()) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               filtrarTiposDias.get(indice).setDescripcion(backUpDescripcion);
            } else if (filtrarTiposDias.get(indice).getDescripcion().equals(" ")) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               filtrarTiposDias.get(indice).setDescripcion(backUpDescripcion);
               banderita = false;
            } else {
               contadorGuardar++;
            }

            if (contadorGuardar == 2) {

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
         RequestContext.getCurrentInstance().update("form:datosTipoDia");
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      }

   }

   public void borrandoTiposDias() {

      if (index >= 0) {
         if (tipoLista == 0) {
            log.info("Entro a borrandoTiposDias");
            if (!modificarTiposDias.isEmpty() && modificarTiposDias.contains(listTiposDias.get(index))) {
               int modIndex = modificarTiposDias.indexOf(listTiposDias.get(index));
               modificarTiposDias.remove(modIndex);
               borrarTiposDias.add(listTiposDias.get(index));
            } else if (!crearTiposDias.isEmpty() && crearTiposDias.contains(listTiposDias.get(index))) {
               int crearIndex = crearTiposDias.indexOf(listTiposDias.get(index));
               crearTiposDias.remove(crearIndex);
            } else {
               borrarTiposDias.add(listTiposDias.get(index));
            }
            listTiposDias.remove(index);
         }
         if (tipoLista == 1) {
            log.info("borrandoTiposDias");
            if (!modificarTiposDias.isEmpty() && modificarTiposDias.contains(filtrarTiposDias.get(index))) {
               int modIndex = modificarTiposDias.indexOf(filtrarTiposDias.get(index));
               modificarTiposDias.remove(modIndex);
               borrarTiposDias.add(filtrarTiposDias.get(index));
            } else if (!crearTiposDias.isEmpty() && crearTiposDias.contains(filtrarTiposDias.get(index))) {
               int crearIndex = crearTiposDias.indexOf(filtrarTiposDias.get(index));
               crearTiposDias.remove(crearIndex);
            } else {
               borrarTiposDias.add(filtrarTiposDias.get(index));
            }
            int VCIndex = listTiposDias.indexOf(filtrarTiposDias.get(index));
            listTiposDias.remove(VCIndex);
            filtrarTiposDias.remove(index);

         }
         RequestContext context = RequestContext.getCurrentInstance();
         infoRegistro = "Cantidad de registros: " + listTiposDias.size();
         RequestContext.getCurrentInstance().update("form:informacionRegistro");
         RequestContext.getCurrentInstance().update("form:datosTipoDia");
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         index = -1;
         secRegistro = null;

         if (guardado == true) {
            guardado = false;
         }
      }

   }

   public void verificarBorrado() {
      try {
         if (tipoLista == 0) {
            verificarDiasLaborales = administrarTiposDias.verificarDiasLaborales(listTiposDias.get(index).getSecuencia());
            verificarExtrasRecargos = administrarTiposDias.verificarExtrasRecargos(listTiposDias.get(index).getSecuencia());
         } else {
            verificarDiasLaborales = administrarTiposDias.verificarDiasLaborales(filtrarTiposDias.get(index).getSecuencia());
            verificarExtrasRecargos = administrarTiposDias.verificarExtrasRecargos(filtrarTiposDias.get(index).getSecuencia());
         }

         if (!verificarDiasLaborales.equals(new BigInteger("0")) || !verificarExtrasRecargos.equals(new BigInteger("0"))) {
            log.info("Borrado>0");

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:validacionBorrar");
            RequestContext.getCurrentInstance().execute("PF('validacionBorrar').show()");
            index = -1;

            verificarDiasLaborales = new BigInteger("-1");
            verificarExtrasRecargos = new BigInteger("-1");

         } else {
            log.info("Borrado==0");
            borrandoTiposDias();
         }
      } catch (Exception e) {
         log.error("ERROR CONTROLTIPOSDIAS verificarBorrado ERROR  ", e);
      }
   }

   public void revisarDialogoGuardar() {

      if (!borrarTiposDias.isEmpty() || !crearTiposDias.isEmpty() || !modificarTiposDias.isEmpty()) {
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:confirmarGuardar");
         RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
      }

   }

   public void guardandoTiposDias() {
      RequestContext context = RequestContext.getCurrentInstance();

      if (guardado == false) {
         log.info("Realizando TiposDias");
         if (!borrarTiposDias.isEmpty()) {
            administrarTiposDias.borrarTiposDias(borrarTiposDias);

            //mostrarBorrados
            registrosBorrados = borrarTiposDias.size();
            RequestContext.getCurrentInstance().update("form:mostrarBorrados");
            RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
            borrarTiposDias.clear();
         }
         if (!crearTiposDias.isEmpty()) {
            administrarTiposDias.crearTiposDias(crearTiposDias);
            crearTiposDias.clear();
         }
         if (!modificarTiposDias.isEmpty()) {
            administrarTiposDias.modificarTiposDias(modificarTiposDias);
            modificarTiposDias.clear();
         }
         log.info("Se guardaron los datos con exito");
         listTiposDias = null;
         guardado = true;
         FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
         RequestContext.getCurrentInstance().update("form:datosTipoDia");
         k = 0;
      }
      index = -1;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");

   }

   public void editarCelda() {
      if (index >= 0) {
         if (tipoLista == 0) {
            editarTipoDia = listTiposDias.get(index);
         }
         if (tipoLista == 1) {
            editarTipoDia = filtrarTiposDias.get(index);
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

   public void agregarNuevoTiposDias() {
      log.info("agregarNuevoTiposDias");
      int contador = 0;
      int duplicados = 0;

      Integer a = 0;
      a = null;
      mensajeValidacion = " ";
      RequestContext context = RequestContext.getCurrentInstance();
      if (nuevoTipoDia.getCodigo() == a) {
         mensajeValidacion = " *Codigo \n";
         log.info("Mensaje validacion : " + mensajeValidacion);
      } else {
         log.info("codigo en Motivo Cambio Cargo: " + nuevoTipoDia.getCodigo());

         for (int x = 0; x < listTiposDias.size(); x++) {
            if (listTiposDias.get(x).getCodigo().equals(nuevoTipoDia.getCodigo())) {
               duplicados++;
            }
         }
         log.info("Antes del if Duplicados eses igual  : " + duplicados);

         if (duplicados > 0) {
            mensajeValidacion = " *Que NO hayan codigos repetidos \n";
            log.info("Mensaje validacion : " + mensajeValidacion);
         } else {
            log.info("bandera");
            contador++;
         }
      }
      log.info("NUEVA DESCRIPCION : " + nuevoTipoDia.getDescripcion());
      if (nuevoTipoDia.getDescripcion() == null) {
         mensajeValidacion = mensajeValidacion + " *Descripción \n";
         log.info("Mensaje validacion : " + mensajeValidacion);

      } else if (nuevoTipoDia.getDescripcion().isEmpty()) {
         mensajeValidacion = mensajeValidacion + " *Descripción \n";
         log.info("Mensaje validacion : " + mensajeValidacion);

      } else {
         log.info("bandera");
         contador++;

      }

      log.info("contador " + contador);

      log.info("NUEVO TIPO : " + nuevoTipoDia.getTipo());
      if (nuevoTipoDia.getTipo() == null) {
         nuevoTipoDia.setTipo("ORD");
      }
      if (contador == 2) {
         if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            //CERRAR FILTRADO
            log.info("Desactivar");
            codigo = (Column) c.getViewRoot().findComponent("form:datosTipoDia:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTipoDia:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTipoDia");
            bandera = 0;
            filtrarTiposDias = null;
            tipoLista = 0;
         }
         log.info("Despues de la bandera");

         k++;
         l = BigInteger.valueOf(k);
         nuevoTipoDia.setSecuencia(l);

         crearTiposDias.add(nuevoTipoDia);

         listTiposDias.add(nuevoTipoDia);
         nuevoTipoDia = new TiposDias();
         RequestContext.getCurrentInstance().update("form:datosTipoDia");
         infoRegistro = "Cantidad de registros: " + listTiposDias.size();
         RequestContext.getCurrentInstance().update("form:informacionRegistro");
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }

         RequestContext.getCurrentInstance().execute("PF('nuevoRegistroTiposReemplazos').hide()");
         index = -1;
         secRegistro = null;

      } else {
         RequestContext.getCurrentInstance().update("form:validacionNuevaCentroCosto");
         RequestContext.getCurrentInstance().execute("PF('validacionNuevaCentroCosto').show()");
         contador = 0;
      }
   }

   public void limpiarNuevoTiposDias() {
      log.info("limpiarNuevoTiposDias");
      nuevoTipoDia = new TiposDias();
      secRegistro = null;
      index = -1;

   }

   //------------------------------------------------------------------------------
   public void duplicandoTiposDias() {
      log.info("duplicandoTiposDias");
      if (index >= 0) {
         duplicarTipoDia = new TiposDias();
         k++;
         l = BigInteger.valueOf(k);

         if (tipoLista == 0) {
            duplicarTipoDia.setSecuencia(l);
            duplicarTipoDia.setCodigo(listTiposDias.get(index).getCodigo());
            duplicarTipoDia.setDescripcion(listTiposDias.get(index).getDescripcion());
            duplicarTipoDia.setTipo(listTiposDias.get(index).getTipo());
         }
         if (tipoLista == 1) {
            duplicarTipoDia.setSecuencia(l);
            duplicarTipoDia.setCodigo(filtrarTiposDias.get(index).getCodigo());
            duplicarTipoDia.setDescripcion(filtrarTiposDias.get(index).getDescripcion());
            duplicarTipoDia.setTipo(filtrarTiposDias.get(index).getTipo());
         }

         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTTR");
         RequestContext.getCurrentInstance().execute("PF('duplicarRegistroTiposReemplazos').show()");
         index = -1;
         secRegistro = null;
      }
   }

   public void confirmarDuplicar() {
      log.error("ESTOY EN CONFIRMAR DUPLICARTIPOSDIAS");
      int contador = 0;
      mensajeValidacion = " ";
      int duplicados = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      Integer a = 0;
      a = null;

      if (duplicarTipoDia.getCodigo() == a) {
         mensajeValidacion = mensajeValidacion + "   *Codigo \n";
         log.info("Mensaje validacion : " + mensajeValidacion);
      } else {
         for (int x = 0; x < listTiposDias.size(); x++) {
            if (listTiposDias.get(x).getCodigo() == duplicarTipoDia.getCodigo()) {
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
      if (duplicarTipoDia.getDescripcion() == null) {
         mensajeValidacion = mensajeValidacion + "   * Descripción \n";
         log.info("Mensaje validacion : " + mensajeValidacion);

      } else if (duplicarTipoDia.getDescripcion().isEmpty()) {
         mensajeValidacion = mensajeValidacion + "   *Descripción \n";
         log.info("Mensaje validacion : " + mensajeValidacion);

      } else {
         log.info("Bandera : ");
         contador++;
      }

      if (contador == 2) {

         log.info("Datos Duplicando: " + duplicarTipoDia.getSecuencia() + "  " + duplicarTipoDia.getCodigo());
         if (crearTiposDias.contains(duplicarTipoDia)) {
            log.info("Ya lo contengo.");
         }
         listTiposDias.add(duplicarTipoDia);
         crearTiposDias.add(duplicarTipoDia);
         RequestContext.getCurrentInstance().update("form:datosTipoDia");
         infoRegistro = "Cantidad de registros: " + listTiposDias.size();
         RequestContext.getCurrentInstance().update("form:informacionRegistro");
         index = -1;
         secRegistro = null;
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         if (bandera == 1) {
            //CERRAR FILTRADO
            FacesContext c = FacesContext.getCurrentInstance();
            codigo = (Column) c.getViewRoot().findComponent("form:datosTipoDia:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTipoDia:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTipoDia");
            bandera = 0;
            filtrarTiposDias = null;
            tipoLista = 0;
         }
         duplicarTipoDia = new TiposDias();
         RequestContext.getCurrentInstance().execute("PF('duplicarRegistroTiposReemplazos').hide()");

      } else {
         contador = 0;
         RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
         RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
      }
   }

   public void limpiarDuplicarTiposDias() {
      duplicarTipoDia = new TiposDias();
   }

   public void exportPDF() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTipoDiaExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "TIPOSDIAS", false, false, "UTF-8", null, null);
      context.responseComplete();
      index = -1;
      secRegistro = null;
   }

   public void exportXLS() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTipoDiaExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "TIPOSDIAS", false, false, "UTF-8", null, null);
      context.responseComplete();
      index = -1;
      secRegistro = null;
   }

   public void verificarRastro() {
      RequestContext context = RequestContext.getCurrentInstance();
      log.info("lol");
      if (!listTiposDias.isEmpty()) {
         if (secRegistro != null) {
            log.info("lol 2");
            int resultado = administrarRastros.obtenerTabla(secRegistro, "TIPOSDIAS"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
      } else if (administrarRastros.verificarHistoricosTabla("TIPOSDIAS")) { // igual acá
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
      index = -1;
   }

   //--------///////////////////////---------------------*****//*/*/*/*/*/-****----
   public List<TiposDias> getListTiposDias() {
      if (listTiposDias == null) {
         listTiposDias = administrarTiposDias.mostrarTiposDias();
      }
      RequestContext context = RequestContext.getCurrentInstance();
      if (listTiposDias == null || listTiposDias.isEmpty()) {
         infoRegistro = "Cantidad de registros: 0 ";
      } else {
         infoRegistro = "Cantidad de registros: " + listTiposDias.size();
      }
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
      return listTiposDias;
   }

   public void setListTiposDias(List<TiposDias> listTiposDias) {
      this.listTiposDias = listTiposDias;
   }

   public List<TiposDias> getFiltrarTiposDias() {
      return filtrarTiposDias;
   }

   public void setFiltrarTiposDias(List<TiposDias> filtrarTiposDias) {
      this.filtrarTiposDias = filtrarTiposDias;
   }

   public TiposDias getNuevoTipoDia() {
      return nuevoTipoDia;
   }

   public void setNuevoTipoDia(TiposDias nuevoTipoDia) {
      this.nuevoTipoDia = nuevoTipoDia;
   }

   public TiposDias getDuplicarTipoDia() {
      return duplicarTipoDia;
   }

   public void setDuplicarTipoDia(TiposDias duplicarTipoDia) {
      this.duplicarTipoDia = duplicarTipoDia;
   }

   public TiposDias getEditarTipoDia() {
      return editarTipoDia;
   }

   public void setEditarTipoDia(TiposDias editarTipoDia) {
      this.editarTipoDia = editarTipoDia;
   }

   public boolean isGuardado() {
      return guardado;
   }

   public void setGuardado(boolean guardado) {
      this.guardado = guardado;
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

   public TiposDias getTipoDiaSeleccionado() {
      return tipoDiaSeleccionado;
   }

   public void setTipoDiaSeleccionado(TiposDias tipoDiaSeleccionado) {
      this.tipoDiaSeleccionado = tipoDiaSeleccionado;
   }

   public int getTamano() {
      return tamano;
   }

   public void setTamano(int tamano) {
      this.tamano = tamano;
   }

   public String getInfoRegistro() {
      return infoRegistro;
   }

   public void setInfoRegistro(String infoRegistro) {
      this.infoRegistro = infoRegistro;
   }

}
