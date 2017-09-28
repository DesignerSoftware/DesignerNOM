/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.SoActosInseguros;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarRastrosInterface;
import InterfaceAdministrar.AdministrarSoActosInsegurosInterface;
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
 * @author John Pineda
 */
@ManagedBean
@SessionScoped
public class ControlSoActosInseguros implements Serializable {

   private static Logger log = Logger.getLogger(ControlSoActosInseguros.class);

   @EJB
   AdministrarSoActosInsegurosInterface administrarSoActosInseguros;
   @EJB
   AdministrarRastrosInterface administrarRastros;
   private List<SoActosInseguros> listSoActosInseguros;
   private List<SoActosInseguros> filtrarSoActosInseguros;
   private List<SoActosInseguros> crearSoActosInseguros;
   private List<SoActosInseguros> modificarSoActosInseguros;
   private List<SoActosInseguros> borrarSoActosInseguros;
   private SoActosInseguros nuevaSoActoInseguro;
   private SoActosInseguros duplicarSoActoInseguro;
   private SoActosInseguros editarSoActoInseguro;
   private SoActosInseguros soActoInseguroSeleccionado;
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
   private BigInteger verificarSoAccidentesMedicos;
   private int tamano;

   private String backUpCodigo;
   private String backUpDescripcion;
   private String infoRegistro;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

   public ControlSoActosInseguros() {
      listSoActosInseguros = null;
      crearSoActosInseguros = new ArrayList<SoActosInseguros>();
      modificarSoActosInseguros = new ArrayList<SoActosInseguros>();
      borrarSoActosInseguros = new ArrayList<SoActosInseguros>();
      permitirIndex = true;
      editarSoActoInseguro = new SoActosInseguros();
      nuevaSoActoInseguro = new SoActosInseguros();
      duplicarSoActoInseguro = new SoActosInseguros();
      guardado = true;
      tamano = 270;
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
         administrarSoActosInseguros.obtenerConexion(ses.getId());
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
      String pagActual = "actoinseguro";
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
         log.info("\n EVENTO FILTRAR \n");
         if (tipoLista == 0) {
            tipoLista = 1;
         }
         RequestContext context = RequestContext.getCurrentInstance();
         infoRegistro = "Cantidad de registros: " + filtrarSoActosInseguros.size();
         RequestContext.getCurrentInstance().update("form:informacionRegistro");
      } catch (Exception e) {
         log.warn("Error EVENTO FILTRAR ERROR===" + e.getMessage());
      }
   }

   public void cambiarIndice(int indice, int celda) {
      log.error("TIPO LISTA = " + tipoLista);

      if (permitirIndex == true) {
         index = indice;
         cualCelda = celda;
         if (cualCelda == 0) {
            if (tipoLista == 0) {
               backUpCodigo = listSoActosInseguros.get(index).getCodigo();
            } else {
               backUpCodigo = filtrarSoActosInseguros.get(index).getCodigo();
            }
         }
         if (cualCelda == 1) {
            if (tipoLista == 0) {
               backUpDescripcion = listSoActosInseguros.get(index).getDescripcion();
            } else {
               backUpDescripcion = filtrarSoActosInseguros.get(index).getDescripcion();
            }
         }
         secRegistro = listSoActosInseguros.get(index).getSecuencia();

      }
      log.info("Indice: " + index + " Celda: " + cualCelda);
   }

   public void asignarIndex(Integer indice, int LND, int dig) {
      try {
         log.info("\n ENTRE CONTROLSOACTOSINSEGUROS  AsignarIndex \n");
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
         log.warn("Error CONTROLSOACTOSINSEGUROS asignarIndex ERROR = ", e);
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
         codigo = (Column) c.getViewRoot().findComponent("form:datosSoCondicionesAmbientalesP:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosSoCondicionesAmbientalesP:descripcion");
         descripcion.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosSoCondicionesAmbientalesP");
         bandera = 0;
         filtrarSoActosInseguros = null;
         tipoLista = 0;
      }

      borrarSoActosInseguros.clear();
      crearSoActosInseguros.clear();
      modificarSoActosInseguros.clear();
      index = -1;
      secRegistro = null;
      k = 0;
      listSoActosInseguros = null;
      guardado = true;
      permitirIndex = true;
      getListSoActosInseguros();
      RequestContext context = RequestContext.getCurrentInstance();
      if (listSoActosInseguros == null || listSoActosInseguros.isEmpty()) {
         infoRegistro = "Cantidad de registros: 0 ";
      } else {
         infoRegistro = "Cantidad de registros: " + listSoActosInseguros.size();
      }
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      RequestContext.getCurrentInstance().update("form:datosSoCondicionesAmbientalesP");
   }

   public void salir() {
      limpiarListasValor();
      if (bandera == 1) {
         FacesContext c = FacesContext.getCurrentInstance();
         //CERRAR FILTRADO
         codigo = (Column) c.getViewRoot().findComponent("form:datosSoCondicionesAmbientalesP:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosSoCondicionesAmbientalesP:descripcion");
         descripcion.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosSoCondicionesAmbientalesP");
         bandera = 0;
         filtrarSoActosInseguros = null;
         tipoLista = 0;
      }

      borrarSoActosInseguros.clear();
      crearSoActosInseguros.clear();
      modificarSoActosInseguros.clear();
      index = -1;
      secRegistro = null;
      k = 0;
      listSoActosInseguros = null;
      guardado = true;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      RequestContext.getCurrentInstance().update("form:datosSoCondicionesAmbientalesP");
   }

   public void activarCtrlF11() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 0) {
         tamano = 250;
         codigo = (Column) c.getViewRoot().findComponent("form:datosSoCondicionesAmbientalesP:codigo");
         codigo.setFilterStyle("width: 85% !important;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosSoCondicionesAmbientalesP:descripcion");
         descripcion.setFilterStyle("width: 85% !important;");
         RequestContext.getCurrentInstance().update("form:datosSoCondicionesAmbientalesP");
         log.info("Activar");
         bandera = 1;
      } else if (bandera == 1) {
         log.info("Desactivar");
         tamano = 270;
         codigo = (Column) c.getViewRoot().findComponent("form:datosSoCondicionesAmbientalesP:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosSoCondicionesAmbientalesP:descripcion");
         descripcion.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosSoCondicionesAmbientalesP");
         bandera = 0;
         filtrarSoActosInseguros = null;
         tipoLista = 0;
      }
   }

   public void modificandoSoActosInseguros(int indice, String confirmarCambio, String valorConfirmar) {
      log.error("SO ACTOS INSEGUROS");
      index = indice;

      int contador = 0;
      boolean banderita = false;
      Short a;
      a = null;
      RequestContext context = RequestContext.getCurrentInstance();
      log.error("TIPO LISTA = " + tipoLista);
      if (confirmarCambio.equalsIgnoreCase("N")) {
         log.error("MODIFICANDO SO ACTO INSEGURO CONFIRMAR CAMBIO = N");
         if (tipoLista == 0) {
            if (!crearSoActosInseguros.contains(listSoActosInseguros.get(indice))) {
               if (listSoActosInseguros.get(indice).getCodigo() == null) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  banderita = false;
                  listSoActosInseguros.get(indice).setCodigo(backUpCodigo);
               } else if (listSoActosInseguros.get(indice).getCodigo().isEmpty()) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  listSoActosInseguros.get(indice).setCodigo(backUpCodigo);
                  banderita = false;
               } else if (listSoActosInseguros.get(indice).getCodigo().equals(" ")) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  listSoActosInseguros.get(indice).setCodigo(backUpCodigo);
                  banderita = false;
               } else {
                  for (int j = 0; j < listSoActosInseguros.size(); j++) {
                     if (j != indice) {
                        if (listSoActosInseguros.get(indice).getCodigo().equals(listSoActosInseguros.get(j).getCodigo())) {
                           contador++;
                        }
                     }
                  }
                  if (contador > 0) {
                     mensajeValidacion = "CODIGOS REPETIDOS";
                     listSoActosInseguros.get(indice).setCodigo(backUpCodigo);
                     banderita = false;
                  } else {
                     banderita = true;
                  }

               }
               if (listSoActosInseguros.get(indice).getDescripcion().isEmpty()) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  listSoActosInseguros.get(indice).setDescripcion(backUpDescripcion);
                  banderita = false;
               }
               if (listSoActosInseguros.get(indice).getDescripcion().equals(" ")) {
                  listSoActosInseguros.get(indice).setDescripcion(backUpDescripcion);
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  banderita = false;
               }

               if (banderita == true) {
                  if (modificarSoActosInseguros.isEmpty()) {
                     modificarSoActosInseguros.add(listSoActosInseguros.get(indice));
                  } else if (!modificarSoActosInseguros.contains(listSoActosInseguros.get(indice))) {
                     modificarSoActosInseguros.add(listSoActosInseguros.get(indice));
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
               if (listSoActosInseguros.get(indice).getCodigo() == null) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  banderita = false;
                  listSoActosInseguros.get(indice).setCodigo(backUpCodigo);
               } else if (listSoActosInseguros.get(indice).getCodigo().isEmpty()) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  listSoActosInseguros.get(indice).setCodigo(backUpCodigo);
                  banderita = false;
               } else if (listSoActosInseguros.get(indice).getCodigo().equals(" ")) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  listSoActosInseguros.get(indice).setCodigo(backUpCodigo);
                  banderita = false;
               } else {
                  for (int j = 0; j < listSoActosInseguros.size(); j++) {
                     if (j != indice) {
                        if (listSoActosInseguros.get(indice).getCodigo().equals(listSoActosInseguros.get(j).getCodigo())) {
                           contador++;
                        }
                     }
                  }
                  if (contador > 0) {
                     mensajeValidacion = "CODIGOS REPETIDOS";
                     listSoActosInseguros.get(indice).setCodigo(backUpCodigo);
                     banderita = false;
                  } else {
                     banderita = true;
                  }

               }
               if (listSoActosInseguros.get(indice).getDescripcion().isEmpty()) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  listSoActosInseguros.get(indice).setDescripcion(backUpDescripcion);
                  banderita = false;
               }
               if (listSoActosInseguros.get(indice).getDescripcion().equals(" ")) {
                  listSoActosInseguros.get(indice).setDescripcion(backUpDescripcion);
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
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
         } else if (!crearSoActosInseguros.contains(filtrarSoActosInseguros.get(indice))) {
            if (filtrarSoActosInseguros.get(indice).getCodigo().isEmpty()) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               banderita = false;
               filtrarSoActosInseguros.get(indice).setCodigo(backUpCodigo);
            }
            if (filtrarSoActosInseguros.get(indice).getCodigo().equals(" ")) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               filtrarSoActosInseguros.get(indice).setCodigo(backUpCodigo);
               banderita = false;
            } else {
               for (int j = 0; j < listSoActosInseguros.size(); j++) {
                  if (filtrarSoActosInseguros.get(indice).getCodigo().equals(listSoActosInseguros.get(j).getCodigo())) {
                     contador++;
                  }
               }

               for (int j = 0; j < filtrarSoActosInseguros.size(); j++) {
                  if (j != indice) {
                     if (filtrarSoActosInseguros.get(indice).getCodigo().equals(filtrarSoActosInseguros.get(j).getCodigo())) {
                        contador++;
                     }
                  }
               }
               if (contador > 0) {
                  filtrarSoActosInseguros.get(indice).setCodigo(backUpCodigo);
                  mensajeValidacion = "CODIGOS REPETIDOS";
                  banderita = false;
               } else {
                  banderita = true;
               }

            }

            if (filtrarSoActosInseguros.get(indice).getDescripcion().isEmpty()) {
               filtrarSoActosInseguros.get(indice).setDescripcion(backUpDescripcion);
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               banderita = false;
            }
            if (filtrarSoActosInseguros.get(indice).getDescripcion().equals(" ")) {
               filtrarSoActosInseguros.get(indice).setDescripcion(backUpDescripcion);
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               banderita = false;
            }

            if (banderita == true) {
               if (modificarSoActosInseguros.isEmpty()) {
                  modificarSoActosInseguros.add(filtrarSoActosInseguros.get(indice));
               } else if (!modificarSoActosInseguros.contains(filtrarSoActosInseguros.get(indice))) {
                  modificarSoActosInseguros.add(filtrarSoActosInseguros.get(indice));
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
            if (filtrarSoActosInseguros.get(indice).getCodigo().isEmpty()) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               banderita = false;
               filtrarSoActosInseguros.get(indice).setCodigo(backUpCodigo);
            }
            if (filtrarSoActosInseguros.get(indice).getCodigo().equals(" ")) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               filtrarSoActosInseguros.get(indice).setCodigo(backUpCodigo);
               banderita = false;
            } else {
               for (int j = 0; j < listSoActosInseguros.size(); j++) {
                  if (filtrarSoActosInseguros.get(indice).getCodigo().equals(listSoActosInseguros.get(j).getCodigo())) {
                     contador++;
                  }
               }

               for (int j = 0; j < filtrarSoActosInseguros.size(); j++) {
                  if (j != indice) {
                     if (filtrarSoActosInseguros.get(indice).getCodigo().equals(filtrarSoActosInseguros.get(j).getCodigo())) {
                        contador++;
                     }
                  }
               }
               if (contador > 0) {
                  filtrarSoActosInseguros.get(indice).setCodigo(backUpCodigo);
                  mensajeValidacion = "CODIGOS REPETIDOS";
                  banderita = false;
               } else {
                  banderita = true;
               }

            }

            if (filtrarSoActosInseguros.get(indice).getDescripcion().isEmpty()) {
               filtrarSoActosInseguros.get(indice).setDescripcion(backUpDescripcion);
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               banderita = false;
            }
            if (filtrarSoActosInseguros.get(indice).getDescripcion().equals(" ")) {
               filtrarSoActosInseguros.get(indice).setDescripcion(backUpDescripcion);
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
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
         RequestContext.getCurrentInstance().update("form:datosSoCondicionesAmbientalesP");
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      }

   }

   public void borrandoSoActosInseguros() {

      RequestContext context = RequestContext.getCurrentInstance();

      if (index >= 0) {
         if (tipoLista == 0) {
            log.info("borrandoSoActosInseguros");
            if (!modificarSoActosInseguros.isEmpty() && modificarSoActosInseguros.contains(listSoActosInseguros.get(index))) {
               int modIndex = modificarSoActosInseguros.indexOf(listSoActosInseguros.get(index));
               modificarSoActosInseguros.remove(modIndex);
               borrarSoActosInseguros.add(listSoActosInseguros.get(index));
            } else if (!crearSoActosInseguros.isEmpty() && crearSoActosInseguros.contains(listSoActosInseguros.get(index))) {
               int crearIndex = crearSoActosInseguros.indexOf(listSoActosInseguros.get(index));
               crearSoActosInseguros.remove(crearIndex);
            } else {
               borrarSoActosInseguros.add(listSoActosInseguros.get(index));
            }
            listSoActosInseguros.remove(index);
         }
         if (tipoLista == 1) {
            log.info("borrandoSoActosInseguros");
            if (!modificarSoActosInseguros.isEmpty() && modificarSoActosInseguros.contains(filtrarSoActosInseguros.get(index))) {
               int modIndex = modificarSoActosInseguros.indexOf(filtrarSoActosInseguros.get(index));
               modificarSoActosInseguros.remove(modIndex);
               borrarSoActosInseguros.add(filtrarSoActosInseguros.get(index));
            } else if (!crearSoActosInseguros.isEmpty() && crearSoActosInseguros.contains(filtrarSoActosInseguros.get(index))) {
               int crearIndex = crearSoActosInseguros.indexOf(filtrarSoActosInseguros.get(index));
               crearSoActosInseguros.remove(crearIndex);
            } else {
               borrarSoActosInseguros.add(filtrarSoActosInseguros.get(index));
            }
            int VCIndex = listSoActosInseguros.indexOf(filtrarSoActosInseguros.get(index));
            listSoActosInseguros.remove(VCIndex);
            filtrarSoActosInseguros.remove(index);

         }
         RequestContext.getCurrentInstance().update("form:datosSoCondicionesAmbientalesP");
         infoRegistro = "Cantidad de registros: " + listSoActosInseguros.size();
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
      log.info("verificarBorrado");
      try {
         if (tipoLista == 0) {
            verificarSoAccidentesMedicos = administrarSoActosInseguros.verificarSoAccidentesMedicos(listSoActosInseguros.get(index).getSecuencia());
         } else {
            verificarSoAccidentesMedicos = administrarSoActosInseguros.verificarSoAccidentesMedicos(filtrarSoActosInseguros.get(index).getSecuencia());
         }
         if (verificarSoAccidentesMedicos.equals(new BigInteger("0"))) {
            log.info("Borrado==0");
            borrandoSoActosInseguros();
         } else {
            log.info("Borrado>0");

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:validacionBorrar");
            RequestContext.getCurrentInstance().execute("PF('validacionBorrar').show()");
            index = -1;
            verificarSoAccidentesMedicos = new BigInteger("-1");
         }
      } catch (Exception e) {
         log.error("ERROR CLASES ACCIDENTES verificarBorrado ERROR  ", e);
      }
   }

   public void revisarDialogoGuardar() {

      if (!borrarSoActosInseguros.isEmpty() || !crearSoActosInseguros.isEmpty() || !modificarSoActosInseguros.isEmpty()) {
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:confirmarGuardar");
         RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
      }

   }

   public void guardandoSoActosInseguros() {
      RequestContext context = RequestContext.getCurrentInstance();

      if (guardado == false) {
         log.info("REALIZANDO SO ACTPS INSEGUROS");
         if (!borrarSoActosInseguros.isEmpty()) {
            administrarSoActosInseguros.borrarSoActosInseguros(borrarSoActosInseguros);
            //mostrarBorrados
            registrosBorrados = borrarSoActosInseguros.size();
            RequestContext.getCurrentInstance().update("form:mostrarBorrados");
            RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
            borrarSoActosInseguros.clear();
         }
         if (!crearSoActosInseguros.isEmpty()) {
            administrarSoActosInseguros.crearSoActosInseguros(crearSoActosInseguros);
            crearSoActosInseguros.clear();
         }
         if (!modificarSoActosInseguros.isEmpty()) {
            administrarSoActosInseguros.modificarSoActosInseguros(modificarSoActosInseguros);
            modificarSoActosInseguros.clear();
         }
         log.info("Se guardaron los datos con exito");
         listSoActosInseguros = null;

         FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
         RequestContext.getCurrentInstance().update("form:datosActividades");
         RequestContext.getCurrentInstance().update("form:datosSoCondicionesAmbientalesP");
         k = 0;
         if (guardado == false) {
            guardado = true;
         }
      }
      index = -1;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");

   }

   public void editarCelda() {
      if (index >= 0) {
         if (tipoLista == 0) {
            editarSoActoInseguro = listSoActosInseguros.get(index);
         }
         if (tipoLista == 1) {
            editarSoActoInseguro = filtrarSoActosInseguros.get(index);
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

   public void agregarNuevoSoActoInseguro() {
      log.info("agregarNuevoSoActoInseguro");
      int contador = 0;
      int duplicados = 0;

      Short a = 0;
      a = null;
      mensajeValidacion = " ";
      RequestContext context = RequestContext.getCurrentInstance();
      if (nuevaSoActoInseguro.getCodigo() == null) {
         mensajeValidacion = " *Codigo \n";
         log.info("Mensaje validacion : " + mensajeValidacion);
      } else {
         log.info("codigo en Motivo Cambio Cargo: " + nuevaSoActoInseguro.getCodigo());

         for (int x = 0; x < listSoActosInseguros.size(); x++) {
            if (listSoActosInseguros.get(x).getCodigo().equals(nuevaSoActoInseguro.getCodigo())) {
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
      if (nuevaSoActoInseguro.getDescripcion() == (null)) {
         mensajeValidacion = mensajeValidacion + " *Descripción \n";
         log.info("Mensaje validacion : " + mensajeValidacion);

      } else {
         log.info("bandera");
         contador++;

      }

      log.info("contador " + contador);

      if (contador == 2) {
         FacesContext c = FacesContext.getCurrentInstance();
         if (bandera == 1) {
            //CERRAR FILTRADO
            log.info("Desactivar");
            codigo = (Column) c.getViewRoot().findComponent("form:datosSoCondicionesAmbientalesP:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosSoCondicionesAmbientalesP:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosSoCondicionesAmbientalesP");
            bandera = 0;
            filtrarSoActosInseguros = null;
            tipoLista = 0;
         }
         log.info("Despues de la bandera");

         k++;
         l = BigInteger.valueOf(k);
         nuevaSoActoInseguro.setSecuencia(l);

         crearSoActosInseguros.add(nuevaSoActoInseguro);

         listSoActosInseguros.add(nuevaSoActoInseguro);
         nuevaSoActoInseguro = new SoActosInseguros();
         infoRegistro = "Cantidad de registros: " + listSoActosInseguros.size();
         RequestContext.getCurrentInstance().update("form:informacionRegistro");
         RequestContext.getCurrentInstance().update("form:datosSoCondicionesAmbientalesP");
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }

         RequestContext.getCurrentInstance().execute("PF('nuevoRegistroSoCondicionesAmbientalesPeligrosaP').hide()");
         index = -1;
         secRegistro = null;

      } else {
         RequestContext.getCurrentInstance().update("form:validacionNuevaCentroCosto");
         RequestContext.getCurrentInstance().execute("PF('validacionNuevaCentroCosto').show()");
         contador = 0;
      }
   }

   public void limpiarNuevoaSoActoInseguro() {
      log.info("limpiarNuevoaSoActoInseguro");
      nuevaSoActoInseguro = new SoActosInseguros();
      secRegistro = null;
      index = -1;

   }

   //------------------------------------------------------------------------------
   public void duplicandoSoActoInseguro() {
      log.info("duplicandoSoActoInseguro");
      if (index >= 0) {
         duplicarSoActoInseguro = new SoActosInseguros();
         k++;
         l = BigInteger.valueOf(k);

         if (tipoLista == 0) {
            duplicarSoActoInseguro.setSecuencia(l);
            duplicarSoActoInseguro.setCodigo(listSoActosInseguros.get(index).getCodigo());
            duplicarSoActoInseguro.setDescripcion(listSoActosInseguros.get(index).getDescripcion());
         }
         if (tipoLista == 1) {
            duplicarSoActoInseguro.setSecuencia(l);
            duplicarSoActoInseguro.setCodigo(filtrarSoActosInseguros.get(index).getCodigo());
            duplicarSoActoInseguro.setDescripcion(filtrarSoActosInseguros.get(index).getDescripcion());
         }

         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarRCAP");
         RequestContext.getCurrentInstance().execute("PF('duplicarRegistroSoCondicionesAmbientalesP').show()");
         index = -1;
         secRegistro = null;
      }
   }

   public void confirmarDuplicar() {
      log.error("CONFIRMAR DUPLICAR SO ACTOS INSEGUROS");
      int contador = 0;
      mensajeValidacion = " ";
      int duplicados = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      Short a = 0;
      a = null;
      log.error("ConfirmarDuplicar codigo " + duplicarSoActoInseguro.getCodigo());
      log.error("ConfirmarDuplicar Descripcion " + duplicarSoActoInseguro.getDescripcion());

      if (duplicarSoActoInseguro.getCodigo() == null || duplicarSoActoInseguro.getCodigo().equals(" ") || duplicarSoActoInseguro.getCodigo().isEmpty()) {
         mensajeValidacion = mensajeValidacion + "   *Codigo \n";
         log.info("Mensaje validacion : " + mensajeValidacion);

      } else {
         for (int x = 0; x < listSoActosInseguros.size(); x++) {
            if (listSoActosInseguros.get(x).getCodigo().equals(duplicarSoActoInseguro.getCodigo())) {
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
      if (duplicarSoActoInseguro.getDescripcion() == null) {
         mensajeValidacion = mensajeValidacion + "   *Descripción \n";
         log.info("Mensaje validacion : " + mensajeValidacion);

      } else {
         log.info("Bandera : ");
         contador++;
      }

      if (contador == 2) {

         log.info("Datos Duplicando: " + duplicarSoActoInseguro.getSecuencia() + "  " + duplicarSoActoInseguro.getCodigo());
         if (crearSoActosInseguros.contains(duplicarSoActoInseguro)) {
            log.info("Ya lo contengo.");
         }
         listSoActosInseguros.add(duplicarSoActoInseguro);
         crearSoActosInseguros.add(duplicarSoActoInseguro);
         RequestContext.getCurrentInstance().update("form:datosSoCondicionesAmbientalesP");
         index = -1;
         secRegistro = null;
         if (guardado == true) {
            guardado = false;
         }
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         infoRegistro = "Cantidad de registros: " + listSoActosInseguros.size();
         RequestContext.getCurrentInstance().update("form:informacionRegistro");
         if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            //CERRAR FILTRADO
            codigo = (Column) c.getViewRoot().findComponent("form:datosSoCondicionesAmbientalesP:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosSoCondicionesAmbientalesP:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosSoCondicionesAmbientalesP");
            bandera = 0;
            filtrarSoActosInseguros = null;
            tipoLista = 0;
         }
         duplicarSoActoInseguro = new SoActosInseguros();
         RequestContext.getCurrentInstance().execute("PF('duplicarRegistroSoCondicionesAmbientalesP').hide()");

      } else {
         contador = 0;
         RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
         RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
      }
   }

   public void limpiarDuplicarSoActosInseguros() {
      duplicarSoActoInseguro = new SoActosInseguros();
   }

   public void exportPDF() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosSoCondicionesAmbientalesPExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "ACTOSINSEGUROS", false, false, "UTF-8", null, null);
      context.responseComplete();
      index = -1;
      secRegistro = null;
   }

   public void exportXLS() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosSoCondicionesAmbientalesPExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "ACTOSINSEGUROS", false, false, "UTF-8", null, null);
      context.responseComplete();
      index = -1;
      secRegistro = null;
   }

   public void verificarRastro() {
      RequestContext context = RequestContext.getCurrentInstance();
      log.info("lol");
      if (!listSoActosInseguros.isEmpty()) {
         if (secRegistro != null) {
            log.info("lol 2");
            int resultado = administrarRastros.obtenerTabla(secRegistro, "SOACTOSINSEGUROS"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
      } else if (administrarRastros.verificarHistoricosTabla("SOACTOSINSEGUROS")) { // igual acá
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
      index = -1;
   }

   //--------///////////////////////---------------------*****//*/*/*/*/*/-****----
   public List<SoActosInseguros> getListSoActosInseguros() {
      if (listSoActosInseguros == null) {
         listSoActosInseguros = administrarSoActosInseguros.consultarSoActosInseguros();
      }
      RequestContext context = RequestContext.getCurrentInstance();
      if (listSoActosInseguros == null || listSoActosInseguros.isEmpty()) {
         infoRegistro = "Cantidad de registros: 0 ";
      } else {
         infoRegistro = "Cantidad de registros: " + listSoActosInseguros.size();
      }
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
      return listSoActosInseguros;
   }

   public void setListSoActosInseguros(List<SoActosInseguros> listSoActosInseguros) {
      this.listSoActosInseguros = listSoActosInseguros;
   }

   public List<SoActosInseguros> getFiltrarSoActosInseguros() {
      return filtrarSoActosInseguros;
   }

   public void setFiltrarSoActosInseguros(List<SoActosInseguros> filtrarSoActosInseguros) {
      this.filtrarSoActosInseguros = filtrarSoActosInseguros;
   }

   public List<SoActosInseguros> getModificarSoActosInseguros() {
      return modificarSoActosInseguros;
   }

   public void setModificarSoActosInseguros(List<SoActosInseguros> modificarSoActosInseguros) {
      this.modificarSoActosInseguros = modificarSoActosInseguros;
   }

   public SoActosInseguros getNuevaSoActoInseguro() {
      return nuevaSoActoInseguro;
   }

   public void setNuevaSoActoInseguro(SoActosInseguros nuevaSoActoInseguro) {
      this.nuevaSoActoInseguro = nuevaSoActoInseguro;
   }

   public SoActosInseguros getDuplicarSoActoInseguro() {
      return duplicarSoActoInseguro;
   }

   public void setDuplicarSoActoInseguro(SoActosInseguros duplicarSoActoInseguro) {
      this.duplicarSoActoInseguro = duplicarSoActoInseguro;
   }

   public SoActosInseguros getEditarSoActoInseguro() {
      return editarSoActoInseguro;
   }

   public void setEditarSoActoInseguro(SoActosInseguros editarSoActoInseguro) {
      this.editarSoActoInseguro = editarSoActoInseguro;
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

   public SoActosInseguros getSoActoInseguroSeleccionado() {
      return soActoInseguroSeleccionado;
   }

   public void setSoActoInseguroSeleccionado(SoActosInseguros soActoInseguroSeleccionado) {
      this.soActoInseguroSeleccionado = soActoInseguroSeleccionado;
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
