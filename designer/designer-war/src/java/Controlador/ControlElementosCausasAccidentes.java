/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.ElementosCausasAccidentes;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarElementosCausasAccidentesInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
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
public class ControlElementosCausasAccidentes implements Serializable {

   private static Logger log = Logger.getLogger(ControlElementosCausasAccidentes.class);

   @EJB
   AdministrarElementosCausasAccidentesInterface administrarElementosCausasAccidentes;
   @EJB
   AdministrarRastrosInterface administrarRastros;

   private List<ElementosCausasAccidentes> listElementosCausasAccidentes;
   private List<ElementosCausasAccidentes> filtrarElementosCausasAccidentes;
   private List<ElementosCausasAccidentes> crearElementosCausasAccidentes;
   private List<ElementosCausasAccidentes> modificarElementosCausasAccidentes;
   private List<ElementosCausasAccidentes> borrarElementosCausasAccidentes;
   private ElementosCausasAccidentes nuevoElementoCausaAccidente;
   private ElementosCausasAccidentes duplicarElementoCausaAccidente;
   private ElementosCausasAccidentes editarElementoCausaAccidente;
   private ElementosCausasAccidentes elementoCausaAccidenteSeleccionado;
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
   private BigDecimal verificarSoAccidentes;
   private BigDecimal verificarSoAccidentesDomesticos;
   private BigDecimal verificarBorradoSoIndicadoresFr;
   private Integer a;
   private int tamano;
   private String infoRegistro;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

   public ControlElementosCausasAccidentes() {
      listElementosCausasAccidentes = null;
      crearElementosCausasAccidentes = new ArrayList<ElementosCausasAccidentes>();
      modificarElementosCausasAccidentes = new ArrayList<ElementosCausasAccidentes>();
      borrarElementosCausasAccidentes = new ArrayList<ElementosCausasAccidentes>();
      permitirIndex = true;
      editarElementoCausaAccidente = new ElementosCausasAccidentes();
      nuevoElementoCausaAccidente = new ElementosCausasAccidentes();
      duplicarElementoCausaAccidente = new ElementosCausasAccidentes();
      a = null;
      tamano = 270;
      guardado = true;
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
      /*if (pag.equals("atras")) {
         pag = paginaAnterior;
         paginaAnterior = "nominaf";
         controlListaNavegacion.quitarPagina(pagActual, this.getClass().getSimpleName());
         
      } else {
       */
      String pagActual = "elementocausaaccidente";

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
         administrarElementosCausasAccidentes.obtenerConexion(ses.getId());
         administrarRastros.obtenerConexion(ses.getId());
      } catch (Exception e) {
         log.error("Error postconstruct " + this.getClass().getName() + ":  ", e);
         log.error("Causa: " + e.getCause());
      }
   }

   public void eventoFiltrar() {
      try {
         log.info("\n EVENTO FILTRAR \n");
         if (tipoLista == 0) {
            tipoLista = 1;
         }
         RequestContext context = RequestContext.getCurrentInstance();
         infoRegistro = "Cantidad de registros: " + filtrarElementosCausasAccidentes.size();
         RequestContext.getCurrentInstance().update("form:informacionRegistro");
      } catch (Exception e) {
         log.warn("Error EVENTO FILTRAR ERROR===" + e.getMessage());
      }
   }

   private Integer backUpCodigo;
   private String backUpDescripcion;

   public void cambiarIndice(int indice, int celda) {
      log.error("TIPO LISTA = " + tipoLista);

      if (permitirIndex == true) {
         index = indice;
         cualCelda = celda;
         if (cualCelda == 0) {
            if (tipoLista == 0) {
               backUpCodigo = listElementosCausasAccidentes.get(indice).getCodigo();
            } else {
               backUpCodigo = filtrarElementosCausasAccidentes.get(indice).getCodigo();
            }
         }
         if (cualCelda == 1) {
            if (tipoLista == 0) {
               backUpDescripcion = listElementosCausasAccidentes.get(indice).getDescripcion();
            } else {
               backUpDescripcion = filtrarElementosCausasAccidentes.get(indice).getDescripcion();
            }
         }
         secRegistro = listElementosCausasAccidentes.get(index).getSecuencia();

      }
      log.info("Indice: " + index + " Celda: " + cualCelda);
   }

   public void asignarIndex(Integer indice, int LND, int dig) {
      try {
         log.info("\n ENTRE CONTROLELEMENTOSCAUSASACCIDENTES  AsignarIndex \n");
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
         log.warn("Error CONTROLELEMENTOSCAUSASACCIDENTES asignarIndex ERROR======" + e.getMessage());
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
         codigo = (Column) c.getViewRoot().findComponent("form:datosElementosCausasAccidentes:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosElementosCausasAccidentes:descripcion");
         descripcion.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosElementosCausasAccidentes");
         bandera = 0;
         filtrarElementosCausasAccidentes = null;
         tipoLista = 0;
      }

      borrarElementosCausasAccidentes.clear();
      crearElementosCausasAccidentes.clear();
      modificarElementosCausasAccidentes.clear();
      index = -1;
      secRegistro = null;
      k = 0;
      listElementosCausasAccidentes = null;
      guardado = true;
      permitirIndex = true;
      getListElementosCausasAccidentes();
      RequestContext context = RequestContext.getCurrentInstance();
      if (listElementosCausasAccidentes == null || listElementosCausasAccidentes.isEmpty()) {
         infoRegistro = "Cantidad de registros: 0 ";
      } else {
         infoRegistro = "Cantidad de registros: " + listElementosCausasAccidentes.size();
      }
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      RequestContext.getCurrentInstance().update("form:datosElementosCausasAccidentes");
   }

   public void salir() {
      limpiarListasValor();
      if (bandera == 1) {
         FacesContext c = FacesContext.getCurrentInstance();
         //CERRAR FILTRADO
         codigo = (Column) c.getViewRoot().findComponent("form:datosElementosCausasAccidentes:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosElementosCausasAccidentes:descripcion");
         descripcion.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosElementosCausasAccidentes");
         bandera = 0;
         filtrarElementosCausasAccidentes = null;
         tipoLista = 0;
      }

      borrarElementosCausasAccidentes.clear();
      crearElementosCausasAccidentes.clear();
      modificarElementosCausasAccidentes.clear();
      index = -1;
      secRegistro = null;
      k = 0;
      listElementosCausasAccidentes = null;
      guardado = true;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      RequestContext.getCurrentInstance().update("form:datosElementosCausasAccidentes");
   }

   public void activarCtrlF11() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 0) {
         tamano = 250;
         codigo = (Column) c.getViewRoot().findComponent("form:datosElementosCausasAccidentes:codigo");
         codigo.setFilterStyle("width: 85% !important");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosElementosCausasAccidentes:descripcion");
         descripcion.setFilterStyle("width: 85% !important");
         RequestContext.getCurrentInstance().update("form:datosElementosCausasAccidentes");
         log.info("Activar");
         bandera = 1;
      } else if (bandera == 1) {
         log.info("Desactivar");
         tamano = 270;
         codigo = (Column) c.getViewRoot().findComponent("form:datosElementosCausasAccidentes:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosElementosCausasAccidentes:descripcion");
         descripcion.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosElementosCausasAccidentes");
         bandera = 0;
         filtrarElementosCausasAccidentes = null;
         tipoLista = 0;
      }
   }

   public void modificandoElementosCausasAccidentes(int indice, String confirmarCambio, String valorConfirmar) {
      log.error("MODIFICAR ELEMENTOS CAUSAS ACCIDENTES");
      index = indice;

      int contador = 0;
      boolean banderita = false;

      RequestContext context = RequestContext.getCurrentInstance();
      log.error("TIPO LISTA = " + tipoLista);
      if (confirmarCambio.equalsIgnoreCase("N")) {
         log.error("MODIFICANDO ELEMENTOS CAUSAS ACCIDENTES  CONFIRMAR CAMBIO = N");
         if (tipoLista == 0) {
            if (!crearElementosCausasAccidentes.contains(listElementosCausasAccidentes.get(indice))) {
               if (listElementosCausasAccidentes.get(indice).getCodigo() == a) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  banderita = false;
                  listElementosCausasAccidentes.get(indice).setCodigo(backUpCodigo);
               } else {
                  for (int j = 0; j < listElementosCausasAccidentes.size(); j++) {
                     if (j != indice) {
                        if (listElementosCausasAccidentes.get(indice).getCodigo().equals(listElementosCausasAccidentes.get(j).getCodigo())) {
                           contador++;
                        }
                     }
                  }
                  if (contador > 0) {
                     mensajeValidacion = "CODIGOS REPETIDOS";
                     listElementosCausasAccidentes.get(indice).setCodigo(backUpCodigo);
                     banderita = false;
                  } else {
                     banderita = true;
                  }

               }
               if (listElementosCausasAccidentes.get(indice).getDescripcion().isEmpty()) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  banderita = false;
                  listElementosCausasAccidentes.get(indice).setDescripcion(backUpDescripcion);
               }
               if (listElementosCausasAccidentes.get(indice).getDescripcion().equals(" ")) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  listElementosCausasAccidentes.get(indice).setDescripcion(backUpDescripcion);
                  banderita = false;
               }

               if (banderita == true) {
                  if (modificarElementosCausasAccidentes.isEmpty()) {
                     modificarElementosCausasAccidentes.add(listElementosCausasAccidentes.get(indice));
                  } else if (!modificarElementosCausasAccidentes.contains(listElementosCausasAccidentes.get(indice))) {
                     modificarElementosCausasAccidentes.add(listElementosCausasAccidentes.get(indice));
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
               if (listElementosCausasAccidentes.get(indice).getCodigo() == a) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  banderita = false;
                  listElementosCausasAccidentes.get(indice).setCodigo(backUpCodigo);
               } else {
                  for (int j = 0; j < listElementosCausasAccidentes.size(); j++) {
                     if (j != indice) {
                        if (listElementosCausasAccidentes.get(indice).getCodigo().equals(listElementosCausasAccidentes.get(j).getCodigo())) {
                           contador++;
                        }
                     }
                  }
                  if (contador > 0) {
                     mensajeValidacion = "CODIGOS REPETIDOS";
                     listElementosCausasAccidentes.get(indice).setCodigo(backUpCodigo);
                     banderita = false;
                  } else {
                     banderita = true;
                  }

               }
               if (listElementosCausasAccidentes.get(indice).getDescripcion().isEmpty()) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  banderita = false;
                  listElementosCausasAccidentes.get(indice).setDescripcion(backUpDescripcion);
               }
               if (listElementosCausasAccidentes.get(indice).getDescripcion().equals(" ")) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  listElementosCausasAccidentes.get(indice).setDescripcion(backUpDescripcion);
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
         } else if (!crearElementosCausasAccidentes.contains(filtrarElementosCausasAccidentes.get(indice))) {
            if (filtrarElementosCausasAccidentes.get(indice).getCodigo() == a) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               banderita = false;
               filtrarElementosCausasAccidentes.get(indice).setCodigo(backUpCodigo);
            } else {
               for (int j = 0; j < listElementosCausasAccidentes.size(); j++) {
                  if (j != indice) {
                     if (filtrarElementosCausasAccidentes.get(indice).getCodigo().equals(filtrarElementosCausasAccidentes.get(j).getCodigo())) {
                        contador++;
                     }
                  }
               }

               if (contador > 0) {
                  mensajeValidacion = "CODIGOS REPETIDOS";
                  banderita = false;
                  banderita = false;
                  filtrarElementosCausasAccidentes.get(indice).setCodigo(backUpCodigo);
               } else {
                  banderita = true;
               }

            }

            if (filtrarElementosCausasAccidentes.get(indice).getDescripcion().isEmpty()) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               filtrarElementosCausasAccidentes.get(indice).setDescripcion(backUpDescripcion);
               banderita = false;
            }
            if (filtrarElementosCausasAccidentes.get(indice).getDescripcion().equals(" ")) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               filtrarElementosCausasAccidentes.get(indice).setDescripcion(backUpDescripcion);
               banderita = false;
            }

            if (banderita == true) {
               if (modificarElementosCausasAccidentes.isEmpty()) {
                  modificarElementosCausasAccidentes.add(filtrarElementosCausasAccidentes.get(indice));
               } else if (!modificarElementosCausasAccidentes.contains(filtrarElementosCausasAccidentes.get(indice))) {
                  modificarElementosCausasAccidentes.add(filtrarElementosCausasAccidentes.get(indice));
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
            if (filtrarElementosCausasAccidentes.get(indice).getCodigo() == a) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               banderita = false;
               filtrarElementosCausasAccidentes.get(indice).setCodigo(backUpCodigo);
            } else {
               for (int j = 0; j < listElementosCausasAccidentes.size(); j++) {
                  if (j != indice) {
                     if (filtrarElementosCausasAccidentes.get(indice).getCodigo().equals(filtrarElementosCausasAccidentes.get(j).getCodigo())) {
                        contador++;
                     }
                  }
               }

               if (contador > 0) {
                  mensajeValidacion = "CODIGOS REPETIDOS";
                  banderita = false;
                  banderita = false;
                  filtrarElementosCausasAccidentes.get(indice).setCodigo(backUpCodigo);
               } else {
                  banderita = true;
               }

            }

            if (filtrarElementosCausasAccidentes.get(indice).getDescripcion().isEmpty()) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               filtrarElementosCausasAccidentes.get(indice).setDescripcion(backUpDescripcion);
               banderita = false;
            }
            if (filtrarElementosCausasAccidentes.get(indice).getDescripcion().equals(" ")) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               filtrarElementosCausasAccidentes.get(indice).setDescripcion(backUpDescripcion);
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
         RequestContext.getCurrentInstance().update("form:datosElementosCausasAccidentes");
      }

   }

   public void borrandoElementosCausasAccidentes() {

      RequestContext context = RequestContext.getCurrentInstance();

      if (index >= 0) {

         if (tipoLista == 0) {
            log.info("borrandoElementosCausasAccidentes");
            if (!modificarElementosCausasAccidentes.isEmpty() && modificarElementosCausasAccidentes.contains(listElementosCausasAccidentes.get(index))) {
               int modIndex = modificarElementosCausasAccidentes.indexOf(listElementosCausasAccidentes.get(index));
               modificarElementosCausasAccidentes.remove(modIndex);
               borrarElementosCausasAccidentes.add(listElementosCausasAccidentes.get(index));
            } else if (!crearElementosCausasAccidentes.isEmpty() && crearElementosCausasAccidentes.contains(listElementosCausasAccidentes.get(index))) {
               int crearIndex = crearElementosCausasAccidentes.indexOf(listElementosCausasAccidentes.get(index));
               crearElementosCausasAccidentes.remove(crearIndex);
            } else {
               borrarElementosCausasAccidentes.add(listElementosCausasAccidentes.get(index));
            }
            listElementosCausasAccidentes.remove(index);
         }
         if (tipoLista == 1) {
            log.info("borrandoElementosCausasAccidentes");
            if (!modificarElementosCausasAccidentes.isEmpty() && modificarElementosCausasAccidentes.contains(filtrarElementosCausasAccidentes.get(index))) {
               int modIndex = modificarElementosCausasAccidentes.indexOf(filtrarElementosCausasAccidentes.get(index));
               modificarElementosCausasAccidentes.remove(modIndex);
               borrarElementosCausasAccidentes.add(filtrarElementosCausasAccidentes.get(index));
            } else if (!crearElementosCausasAccidentes.isEmpty() && crearElementosCausasAccidentes.contains(filtrarElementosCausasAccidentes.get(index))) {
               int crearIndex = crearElementosCausasAccidentes.indexOf(filtrarElementosCausasAccidentes.get(index));
               crearElementosCausasAccidentes.remove(crearIndex);
            } else {
               borrarElementosCausasAccidentes.add(filtrarElementosCausasAccidentes.get(index));
            }
            int VCIndex = listElementosCausasAccidentes.indexOf(filtrarElementosCausasAccidentes.get(index));
            listElementosCausasAccidentes.remove(VCIndex);
            filtrarElementosCausasAccidentes.remove(index);

         }
         RequestContext.getCurrentInstance().update("form:datosElementosCausasAccidentes");
         infoRegistro = "Cantidad de registros: " + listElementosCausasAccidentes.size();
         RequestContext.getCurrentInstance().update("form:informacionRegistro");
         index = -1;
         secRegistro = null;

         if (guardado == true) {
            guardado = false;
         }
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      }

   }

   private BigInteger contadorSoAccidentes;
   private BigInteger contadorSoAccidentesMedicos;
   private BigInteger contadorSoIndicadoresFr;

   public void verificarBorrado() {
      try {
         log.info("ESTOY EN VERIFICAR BORRADO tipoLista " + tipoLista);
         log.info("secuencia borrado : " + listElementosCausasAccidentes.get(index).getSecuencia());
         if (tipoLista == 0) {
            log.info("secuencia borrado : " + listElementosCausasAccidentes.get(index).getSecuencia());
            contadorSoAccidentes = administrarElementosCausasAccidentes.contarSoAccidentesCausa(listElementosCausasAccidentes.get(index).getSecuencia());
            contadorSoAccidentesMedicos = administrarElementosCausasAccidentes.contarSoAccidentesMedicosElementoCausaAccidente(listElementosCausasAccidentes.get(index).getSecuencia());
            contadorSoIndicadoresFr = administrarElementosCausasAccidentes.contarSoIndicadoresFrElementoCausaAccidente(listElementosCausasAccidentes.get(index).getSecuencia());
         } else {
            log.info("secuencia borrado : " + filtrarElementosCausasAccidentes.get(index).getSecuencia());
            contadorSoAccidentes = administrarElementosCausasAccidentes.contarSoAccidentesCausa(filtrarElementosCausasAccidentes.get(index).getSecuencia());
            contadorSoAccidentesMedicos = administrarElementosCausasAccidentes.contarSoAccidentesMedicosElementoCausaAccidente(filtrarElementosCausasAccidentes.get(index).getSecuencia());
            contadorSoIndicadoresFr = administrarElementosCausasAccidentes.contarSoIndicadoresFrElementoCausaAccidente(filtrarElementosCausasAccidentes.get(index).getSecuencia());
         }
         log.info("contadorSoAccidentes " + contadorSoAccidentes.toString());
         log.info("contadorSoAccidentesMedicos " + contadorSoAccidentesMedicos.toString());
         log.info("contadorSoIndicadoresFr " + contadorSoIndicadoresFr.toString());
         if (!contadorSoAccidentes.equals(new BigInteger("0")) || !contadorSoAccidentesMedicos.equals(new BigInteger("0")) || !contadorSoIndicadoresFr.equals(new BigInteger("0"))) {
            log.info("Borrado>0");

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:validacionBorrar");
            RequestContext.getCurrentInstance().execute("PF('validacionBorrar').show()");
            index = -1;

            contadorSoAccidentes = new BigInteger("-1");
            contadorSoAccidentesMedicos = new BigInteger("-1");
            contadorSoIndicadoresFr = new BigInteger("-1");

         } else {
            log.info("Borrado==0");
            borrandoElementosCausasAccidentes();
         }
      } catch (Exception e) {
         log.error("ERROR ControlDepotes verificarBorrado ERROR  ", e);
      }
   }

   public void revisarDialogoGuardar() {

      if (!borrarElementosCausasAccidentes.isEmpty() || !crearElementosCausasAccidentes.isEmpty() || !modificarElementosCausasAccidentes.isEmpty()) {
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:confirmarGuardar");
         RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
      }

   }

   public void guardandoElementosCausasAccidentes() {
      RequestContext context = RequestContext.getCurrentInstance();

      if (guardado == false) {
         log.info("Realizando ELEMENTOS CAUSAS ACCIDENTES");
         if (!borrarElementosCausasAccidentes.isEmpty()) {
            administrarElementosCausasAccidentes.borrarElementosCausasAccidentes(borrarElementosCausasAccidentes);
            registrosBorrados = borrarElementosCausasAccidentes.size();
            RequestContext.getCurrentInstance().update("form:mostrarBorrados");
            RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
            borrarElementosCausasAccidentes.clear();
         }
         if (!crearElementosCausasAccidentes.isEmpty()) {
            administrarElementosCausasAccidentes.crearElementosCausasAccidentes(crearElementosCausasAccidentes);
            crearElementosCausasAccidentes.clear();
         }
         if (!modificarElementosCausasAccidentes.isEmpty()) {
            administrarElementosCausasAccidentes.modificarElementosCausasAccidentes(modificarElementosCausasAccidentes);
            modificarElementosCausasAccidentes.clear();
         }
         log.info("Se guardaron los datos con exito");
         listElementosCausasAccidentes = null;
         FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
         RequestContext.getCurrentInstance().update("form:datosElementosCausasAccidentes");
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
            editarElementoCausaAccidente = listElementosCausasAccidentes.get(index);
         }
         if (tipoLista == 1) {
            editarElementoCausaAccidente = filtrarElementosCausasAccidentes.get(index);
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

   public void agregarNuevoElementoCausaAccidente() {
      log.info("agregarNuevoElementoCausaAccidente");
      int contador = 0;
      int duplicados = 0;

      mensajeValidacion = " ";
      RequestContext context = RequestContext.getCurrentInstance();
      if (nuevoElementoCausaAccidente.getCodigo() == a) {
         mensajeValidacion = " *Codigo \n";
         log.info("Mensaje validacion : " + mensajeValidacion);
      } else {
         log.info("codigo en Motivo Cambio Cargo: " + nuevoElementoCausaAccidente.getCodigo());

         for (int x = 0; x < listElementosCausasAccidentes.size(); x++) {
            if (listElementosCausasAccidentes.get(x).getCodigo() == nuevoElementoCausaAccidente.getCodigo()) {
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
      if (nuevoElementoCausaAccidente.getDescripcion() == (null)) {
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
            codigo = (Column) c.getViewRoot().findComponent("form:datosElementosCausasAccidentes:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosElementosCausasAccidentes:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosElementosCausasAccidentes");
            bandera = 0;
            filtrarElementosCausasAccidentes = null;
            tipoLista = 0;
         }
         log.info("Despues de la bandera");

         k++;
         l = BigInteger.valueOf(k);
         nuevoElementoCausaAccidente.setSecuencia(l);

         crearElementosCausasAccidentes.add(nuevoElementoCausaAccidente);

         listElementosCausasAccidentes.add(nuevoElementoCausaAccidente);
         nuevoElementoCausaAccidente = new ElementosCausasAccidentes();
         RequestContext.getCurrentInstance().update("form:datosElementosCausasAccidentes");
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         infoRegistro = "Cantidad de registros: " + listElementosCausasAccidentes.size();
         RequestContext.getCurrentInstance().update("form:informacionRegistro");

         RequestContext.getCurrentInstance().execute("PF('nuevoRegistroElementoCausaAccidente').hide()");
         index = -1;
         secRegistro = null;

      } else {
         RequestContext.getCurrentInstance().update("form:validacionNuevaCentroCosto");
         RequestContext.getCurrentInstance().execute("PF('validacionNuevaCentroCosto').show()");
         contador = 0;
      }
   }

   public void limpiarNuevoElementoCausaAccidente() {
      log.info("limpiarNuevoElementoCausaAccidente");
      nuevoElementoCausaAccidente = new ElementosCausasAccidentes();
      secRegistro = null;
      index = -1;

   }

   //------------------------------------------------------------------------------
   public void duplicandoElementoCausaAccidente() {
      log.info("duplicandoTiposCertificados");
      if (index >= 0) {
         duplicarElementoCausaAccidente = new ElementosCausasAccidentes();
         k++;
         l = BigInteger.valueOf(k);

         if (tipoLista == 0) {
            duplicarElementoCausaAccidente.setSecuencia(l);
            duplicarElementoCausaAccidente.setCodigo(listElementosCausasAccidentes.get(index).getCodigo());
            duplicarElementoCausaAccidente.setDescripcion(listElementosCausasAccidentes.get(index).getDescripcion());
         }
         if (tipoLista == 1) {
            duplicarElementoCausaAccidente.setSecuencia(l);
            duplicarElementoCausaAccidente.setCodigo(filtrarElementosCausasAccidentes.get(index).getCodigo());
            duplicarElementoCausaAccidente.setDescripcion(filtrarElementosCausasAccidentes.get(index).getDescripcion());
         }

         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarECA");
         RequestContext.getCurrentInstance().execute("PF('duplicarRegistroElementoCausaAccidente').show()");
         index = -1;
         secRegistro = null;
      }
   }

   public void confirmarDuplicar() {
      log.error("CONFIRMAR DUPLICAR ELEMENTOS CAUSAS ACCIDENTES");
      int contador = 0;
      mensajeValidacion = " ";
      int duplicados = 0;
      RequestContext context = RequestContext.getCurrentInstance();

      log.error("ConfirmarDuplicar codigo " + duplicarElementoCausaAccidente.getCodigo());
      log.error("ConfirmarDuplicar Descripcion " + duplicarElementoCausaAccidente.getDescripcion());

      if (duplicarElementoCausaAccidente.getCodigo() == a) {
         mensajeValidacion = mensajeValidacion + "   *Codigo \n";
         log.info("Mensaje validacion : " + mensajeValidacion);
      } else {
         for (int x = 0; x < listElementosCausasAccidentes.size(); x++) {
            if (listElementosCausasAccidentes.get(x).getCodigo() == duplicarElementoCausaAccidente.getCodigo()) {
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
      if (duplicarElementoCausaAccidente.getDescripcion() == null) {
         mensajeValidacion = mensajeValidacion + "   *Descripcion \n";
         log.info("Mensaje validacion : " + mensajeValidacion);

      } else {
         log.info("Bandera : ");
         contador++;
      }

      if (contador == 2) {

         log.info("Datos Duplicando: " + duplicarElementoCausaAccidente.getSecuencia() + "  " + duplicarElementoCausaAccidente.getCodigo());
         if (crearElementosCausasAccidentes.contains(duplicarElementoCausaAccidente)) {
            log.info("Ya lo contengo.");
         }
         listElementosCausasAccidentes.add(duplicarElementoCausaAccidente);
         crearElementosCausasAccidentes.add(duplicarElementoCausaAccidente);
         RequestContext.getCurrentInstance().update("form:datosElementosCausasAccidentes");
         index = -1;
         secRegistro = null;
         if (guardado == true) {
            guardado = false;
         }
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         infoRegistro = "Cantidad de registros: " + listElementosCausasAccidentes.size();
         RequestContext.getCurrentInstance().update("form:informacionRegistro");
         if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            //CERRAR FILTRADO
            codigo = (Column) c.getViewRoot().findComponent("form:datosElementosCausasAccidentes:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosElementosCausasAccidentes:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosElementosCausasAccidentes");
            bandera = 0;
            filtrarElementosCausasAccidentes = null;
            tipoLista = 0;
         }
         duplicarElementoCausaAccidente = new ElementosCausasAccidentes();
         RequestContext.getCurrentInstance().execute("PF('duplicarRegistroElementoCausaAccidente').hide()");

      } else {
         contador = 0;
         RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
         RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
      }
   }

   public void limpiarDuplicarElementosCausasAccidentes() {
      duplicarElementoCausaAccidente = new ElementosCausasAccidentes();
   }

   public void exportPDF() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosElementosCausasAccidentesExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "ELEMENTOSCAUSASACCIDENTES", false, false, "UTF-8", null, null);
      context.responseComplete();
      index = -1;
      secRegistro = null;
   }

   public void exportXLS() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosElementosCausasAccidentesExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "ELEMENTOSCAUSASACCIDENTES", false, false, "UTF-8", null, null);
      context.responseComplete();
      index = -1;
      secRegistro = null;
   }

   public void verificarRastro() {
      RequestContext context = RequestContext.getCurrentInstance();
      log.info("lol");
      if (!listElementosCausasAccidentes.isEmpty()) {
         if (secRegistro != null) {
            log.info("lol 2");
            int resultado = administrarRastros.obtenerTabla(secRegistro, "ELEMENTOSCAUSASACCIDENTES"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
      } else if (administrarRastros.verificarHistoricosTabla("ELEMENTOSCAUSASACCIDENTES")) { // igual acá
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
      index = -1;
   }

   //--------///////////////////////---------------------*****//*/*/*/*/*/-****----
   public List<ElementosCausasAccidentes> getListElementosCausasAccidentes() {
      if (listElementosCausasAccidentes == null) {
         listElementosCausasAccidentes = administrarElementosCausasAccidentes.consultarElementosCausasAccidentes();
      }
      RequestContext context = RequestContext.getCurrentInstance();
      if (listElementosCausasAccidentes == null || listElementosCausasAccidentes.isEmpty()) {
         infoRegistro = "Cantidad de registros: 0 ";
      } else {
         infoRegistro = "Cantidad de registros: " + listElementosCausasAccidentes.size();
      }
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
      return listElementosCausasAccidentes;
   }

   public void setListElementosCausasAccidentes(List<ElementosCausasAccidentes> listElementosCausasAccidentes) {
      this.listElementosCausasAccidentes = listElementosCausasAccidentes;
   }

   public List<ElementosCausasAccidentes> getFiltrarElementosCausasAccidentes() {
      return filtrarElementosCausasAccidentes;
   }

   public void setFiltrarElementosCausasAccidentes(List<ElementosCausasAccidentes> filtrarElementosCausasAccidentes) {
      this.filtrarElementosCausasAccidentes = filtrarElementosCausasAccidentes;
   }

   public ElementosCausasAccidentes getNuevoElementoCausaAccidente() {
      return nuevoElementoCausaAccidente;
   }

   public void setNuevoElementoCausaAccidente(ElementosCausasAccidentes nuevoElementoCausaAccidente) {
      this.nuevoElementoCausaAccidente = nuevoElementoCausaAccidente;
   }

   public ElementosCausasAccidentes getDuplicarElementoCausaAccidente() {
      return duplicarElementoCausaAccidente;
   }

   public void setDuplicarElementoCausaAccidente(ElementosCausasAccidentes duplicarElementoCausaAccidente) {
      this.duplicarElementoCausaAccidente = duplicarElementoCausaAccidente;
   }

   public ElementosCausasAccidentes getEditarElementoCausaAccidente() {
      return editarElementoCausaAccidente;
   }

   public void setEditarElementoCausaAccidente(ElementosCausasAccidentes editarElementoCausaAccidente) {
      this.editarElementoCausaAccidente = editarElementoCausaAccidente;
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

   public ElementosCausasAccidentes getElementoCausaAccidenteSeleccionado() {
      return elementoCausaAccidenteSeleccionado;
   }

   public void setElementoCausaAccidenteSeleccionado(ElementosCausasAccidentes elementoCausaAccidenteSeleccionado) {
      this.elementoCausaAccidenteSeleccionado = elementoCausaAccidenteSeleccionado;
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
