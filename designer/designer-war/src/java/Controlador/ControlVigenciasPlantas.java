/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.VigenciasPlantas;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarVigenciasPlantasInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
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
public class ControlVigenciasPlantas implements Serializable {

   private static Logger log = Logger.getLogger(ControlVigenciasPlantas.class);

   @EJB
   AdministrarVigenciasPlantasInterface administrarVigenciasPlantas;
   @EJB
   AdministrarRastrosInterface administrarRastros;
   private List<VigenciasPlantas> listVigenciasPlantas;
   private List<VigenciasPlantas> filtrarVigenciasPlantas;
   private List<VigenciasPlantas> crearVigenciasPlantas;
   private List<VigenciasPlantas> modificarVigenciasPlantas;
   private List<VigenciasPlantas> borrarVigenciasPlantas;
   private VigenciasPlantas nuevoVigenciaPlanta;
   private VigenciasPlantas duplicarVigenciaPlanta;
   private VigenciasPlantas editarVigenciaPlanta;
   private VigenciasPlantas vigenciasPlantasSeleccionado;
   //otros
   private int cualCelda, tipoLista, index, tipoActualizacion, k, bandera;
   private BigInteger l;
   private boolean aceptar, guardado;
   //AutoCompletar
   private boolean permitirIndex;
   //RASTRO
   private BigInteger secRegistro;
   private Column codigo, fecha;
   //borrado
   private int registrosBorrados;
   private String mensajeValidacion;
   private String infoRegistro;
   private int tamano;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

   public ControlVigenciasPlantas() {
      listVigenciasPlantas = null;
      crearVigenciasPlantas = new ArrayList<VigenciasPlantas>();
      modificarVigenciasPlantas = new ArrayList<VigenciasPlantas>();
      borrarVigenciasPlantas = new ArrayList<VigenciasPlantas>();
      permitirIndex = true;
      editarVigenciaPlanta = new VigenciasPlantas();
      nuevoVigenciaPlanta = new VigenciasPlantas();
      duplicarVigenciaPlanta = new VigenciasPlantas();
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
         administrarVigenciasPlantas.obtenerConexion(ses.getId());
         administrarRastros.obtenerConexion(ses.getId());
      } catch (Exception e) {
         log.error("Error postconstruct ControlVigenciasCargos:  ", e);
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
      String pagActual = "vigenciaplanta";
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
         log.info("\n ENTRE A ControlVigenciasPlantas.eventoFiltrar \n");
         if (tipoLista == 0) {
            tipoLista = 1;
         }
         RequestContext context = RequestContext.getCurrentInstance();
         infoRegistro = "Cantidad de registros: " + filtrarVigenciasPlantas.size();
         RequestContext.getCurrentInstance().update("form:informacionRegistro");
      } catch (Exception e) {
         log.warn("Error ControlVigenciasPlantas eventoFiltrar ERROR===" + e.getMessage());
      }
   }

   private Integer backUpCodigo;
   private Date backUpFecha;

   public void cambiarIndice(int indice, int celda) {
      log.error("TIPO LISTA = " + tipoLista);

      if (permitirIndex == true) {
         index = indice;
         cualCelda = celda;
         secRegistro = listVigenciasPlantas.get(index).getSecuencia();
         if (cualCelda == 0) {
            if (tipoLista == 0) {
               backUpCodigo = listVigenciasPlantas.get(index).getCodigo();
            } else {
               backUpCodigo = filtrarVigenciasPlantas.get(index).getCodigo();
            }
         }
         if (cualCelda == 1) {
            if (tipoLista == 0) {
               backUpFecha = listVigenciasPlantas.get(index).getFechavigencia();
            } else {
               backUpFecha = filtrarVigenciasPlantas.get(index).getFechavigencia();
            }
            log.info("ControlVigenciasPlantas indice " + index + " backUpFecha " + backUpFecha);
         }

      }
      log.info("Indice: " + index + " Celda: " + cualCelda);
   }

   public void asignarIndex(Integer indice, int LND, int dig) {
      try {
         log.info("\n ENTRE A ControlVigenciasPlantas.asignarIndex \n");
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
         log.warn("Error ControlVigenciasPlantas.asignarIndex ERROR======" + e.getMessage());
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
         codigo = (Column) c.getViewRoot().findComponent("form:datosVigenciaPlanta:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         fecha = (Column) c.getViewRoot().findComponent("form:datosVigenciaPlanta:fecha");
         fecha.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosVigenciaPlanta");
         bandera = 0;
         filtrarVigenciasPlantas = null;
         tipoLista = 0;
      }

      borrarVigenciasPlantas.clear();
      crearVigenciasPlantas.clear();
      modificarVigenciasPlantas.clear();
      index = -1;
      secRegistro = null;
      k = 0;
      listVigenciasPlantas = null;
      guardado = true;
      permitirIndex = true;
      getListVigenciasPlantas();
      RequestContext context = RequestContext.getCurrentInstance();
      if (listVigenciasPlantas == null || listVigenciasPlantas.isEmpty()) {
         infoRegistro = "Cantidad de registros: 0 ";
      } else {
         infoRegistro = "Cantidad de registros: " + listVigenciasPlantas.size();
      }
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
      RequestContext.getCurrentInstance().update("form:datosVigenciaPlanta");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void salir() {
      limpiarListasValor();
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 1) {
         //CERRAR FILTRADO
         codigo = (Column) c.getViewRoot().findComponent("form:datosVigenciaPlanta:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         fecha = (Column) c.getViewRoot().findComponent("form:datosVigenciaPlanta:fecha");
         fecha.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosVigenciaPlanta");
         bandera = 0;
         filtrarVigenciasPlantas = null;
         tipoLista = 0;
      }

      borrarVigenciasPlantas.clear();
      crearVigenciasPlantas.clear();
      modificarVigenciasPlantas.clear();
      index = -1;
      secRegistro = null;
      k = 0;
      listVigenciasPlantas = null;
      guardado = true;
      permitirIndex = true;
      getListVigenciasPlantas();
      RequestContext context = RequestContext.getCurrentInstance();
      if (listVigenciasPlantas == null || listVigenciasPlantas.isEmpty()) {
         infoRegistro = "Cantidad de registros: 0 ";
      } else {
         infoRegistro = "Cantidad de registros: " + listVigenciasPlantas.size();
      }
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
      RequestContext.getCurrentInstance().update("form:datosVigenciaPlanta");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void activarCtrlF11() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 0) {
         tamano = 250;
         codigo = (Column) c.getViewRoot().findComponent("form:datosVigenciaPlanta:codigo");
         codigo.setFilterStyle("width: 85% !important;");
         fecha = (Column) c.getViewRoot().findComponent("form:datosVigenciaPlanta:fecha");
         fecha.setFilterStyle("width: 85% !important;");
         RequestContext.getCurrentInstance().update("form:datosVigenciaPlanta");
         log.info("Activar");
         bandera = 1;
      } else if (bandera == 1) {
         log.info("Desactivar");
         tamano = 270;
         codigo = (Column) c.getViewRoot().findComponent("form:datosVigenciaPlanta:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         fecha = (Column) c.getViewRoot().findComponent("form:datosVigenciaPlanta:fecha");
         fecha.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosVigenciaPlanta");
         bandera = 0;
         filtrarVigenciasPlantas = null;
         tipoLista = 0;
      }
   }

   public void mostrarInfo(int indice, int celda) {
      int contador = 0;
      int fechas = 0;
      log.info("ControlVigenciasPlantas mostrar info indice : " + indice + "  permitirInxes : " + permitirIndex);
      if (permitirIndex == true) {
         RequestContext context = RequestContext.getCurrentInstance();

         mensajeValidacion = " ";
         index = indice;
         cualCelda = celda;
         log.info("ControlVigenciasPlantas mostrarInfo INDICE : " + index + " cualCelda : " + cualCelda);
         if (tipoLista == 0) {
            secRegistro = listVigenciasPlantas.get(indice).getSecuencia();
            log.error("MODIFICAR FECHA \n Indice" + indice + "Fecha " + listVigenciasPlantas.get(indice).getFechavigencia());
            if (listVigenciasPlantas.get(indice).getFechavigencia() == null) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               listVigenciasPlantas.get(indice).setFechavigencia(backUpFecha);
               log.info("ControlVigenciasPlantas despues de mostrar el error fechaAsignada : " + listVigenciasPlantas.get(indice).getFechavigencia());
            } else {
               for (int j = 0; j < listVigenciasPlantas.size(); j++) {
                  if (j != indice) {
                     if (listVigenciasPlantas.get(indice).getFechavigencia().equals(listVigenciasPlantas.get(j).getFechavigencia())) {
                        fechas++;
                     }
                  }
               }
               if (fechas > 0) {
                  listVigenciasPlantas.get(indice).setFechavigencia(backUpFecha);
                  mensajeValidacion = "FECHAS REPETIDAS";
               } else {
                  contador++;
               }
            }
            if (contador == 1) {
               if (!crearVigenciasPlantas.contains(listVigenciasPlantas.get(indice))) {
                  if (modificarVigenciasPlantas.isEmpty()) {
                     modificarVigenciasPlantas.add(listVigenciasPlantas.get(indice));
                  } else if (!modificarVigenciasPlantas.contains(listVigenciasPlantas.get(indice))) {
                     modificarVigenciasPlantas.add(listVigenciasPlantas.get(indice));
                  }
                  if (guardado == true) {
                     guardado = false;
                     RequestContext.getCurrentInstance().update("form:ACEPTAR");
                  }
                  RequestContext.getCurrentInstance().update("form:datosVigenciaPlanta");

               } else {
                  if (guardado == true) {
                     guardado = false;
                     RequestContext.getCurrentInstance().update("form:ACEPTAR");
                  }
                  RequestContext.getCurrentInstance().update("form:datosVigenciaPlanta");
               }
            } else {
               RequestContext.getCurrentInstance().update("form:validacionModificar");
               RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");
            }
         } else {
            secRegistro = filtrarVigenciasPlantas.get(indice).getSecuencia();
            log.error("MODIFICAR FECHA \n Indice" + indice + "Fecha " + filtrarVigenciasPlantas.get(indice).getFechavigencia());
            if (filtrarVigenciasPlantas.get(indice).getFechavigencia() == null) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               contador++;
               listVigenciasPlantas.get(indice).setFechavigencia(backUpFecha);

            } else {
               for (int j = 0; j < filtrarVigenciasPlantas.size(); j++) {
                  if (j != indice) {
                     if (filtrarVigenciasPlantas.get(indice).getFechavigencia().equals(filtrarVigenciasPlantas.get(j).getFechavigencia())) {
                        fechas++;
                     }
                  }
               }

            }
            if (fechas > 0) {
               mensajeValidacion = "FECHAS REPETIDAS";
               contador++;
               listVigenciasPlantas.get(indice).setFechavigencia(backUpFecha);

            }
            if (contador == 0) {
               if (!crearVigenciasPlantas.contains(filtrarVigenciasPlantas.get(indice))) {
                  if (modificarVigenciasPlantas.isEmpty()) {
                     modificarVigenciasPlantas.add(filtrarVigenciasPlantas.get(indice));
                  } else if (!modificarVigenciasPlantas.contains(filtrarVigenciasPlantas.get(indice))) {
                     modificarVigenciasPlantas.add(filtrarVigenciasPlantas.get(indice));
                  }
                  if (guardado == true) {
                     guardado = false;
                     RequestContext.getCurrentInstance().update("form:ACEPTAR");
                  }
                  RequestContext.getCurrentInstance().update("form:datosVigenciaPlanta");

               } else {
                  if (guardado == true) {
                     guardado = false;
                     RequestContext.getCurrentInstance().update("form:ACEPTAR");
                  }
                  RequestContext.getCurrentInstance().update("form:datosVigenciaPlanta");
               }
            } else {
               RequestContext.getCurrentInstance().update("form:validacionModificar");
               RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");
            }
         }

         index = -1;
         secRegistro = null;
         RequestContext.getCurrentInstance().update("form:datosVigenciaPlanta");
      }
      log.info("Indice: " + index + " Celda: " + cualCelda);

   }

   public void modificarVigenciaPlanta(int indice, String confirmarCambio, String valorConfirmar) {
      log.error("ENTRE A MODIFICAR TIPO EMPRESA");
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
            if (!crearVigenciasPlantas.contains(listVigenciasPlantas.get(indice))) {
               if (listVigenciasPlantas.get(indice).getCodigo() == a) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  banderita = false;
                  listVigenciasPlantas.get(indice).setCodigo(backUpCodigo);
               } else {
                  for (int j = 0; j < listVigenciasPlantas.size(); j++) {
                     if (j != indice) {
                        if (listVigenciasPlantas.get(indice).getCodigo().equals(listVigenciasPlantas.get(j).getCodigo())) {
                           contador++;
                        }
                     }
                  }
                  if (contador > 0) {
                     listVigenciasPlantas.get(indice).setCodigo(backUpCodigo);
                     mensajeValidacion = "CODIGOS REPETIDOS";
                     banderita = false;
                  } else {
                     banderita = true;
                  }

               }
               if (banderita == true) {
                  if (modificarVigenciasPlantas.isEmpty()) {
                     modificarVigenciasPlantas.add(listVigenciasPlantas.get(indice));
                  } else if (!modificarVigenciasPlantas.contains(listVigenciasPlantas.get(indice))) {
                     modificarVigenciasPlantas.add(listVigenciasPlantas.get(indice));
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
               if (listVigenciasPlantas.get(indice).getCodigo() == a) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  banderita = false;
                  listVigenciasPlantas.get(indice).setCodigo(backUpCodigo);
               } else {
                  for (int j = 0; j < listVigenciasPlantas.size(); j++) {
                     if (j != indice) {
                        if (listVigenciasPlantas.get(indice).getCodigo().equals(listVigenciasPlantas.get(j).getCodigo())) {
                           contador++;
                        }
                     }
                  }
                  if (contador > 0) {
                     listVigenciasPlantas.get(indice).setCodigo(backUpCodigo);
                     mensajeValidacion = "CODIGOS REPETIDOS";
                     banderita = false;
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
         } else if (!crearVigenciasPlantas.contains(filtrarVigenciasPlantas.get(indice))) {
            if (filtrarVigenciasPlantas.get(indice).getCodigo() == a) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               banderita = false;
               filtrarVigenciasPlantas.get(indice).setCodigo(backUpCodigo);

            } else {
               for (int j = 0; j < filtrarVigenciasPlantas.size(); j++) {
                  if (j != indice) {
                     if (filtrarVigenciasPlantas.get(indice).getCodigo().equals(filtrarVigenciasPlantas.get(j).getCodigo())) {
                        contador++;
                     }
                  }
               }

               if (contador > 0) {
                  mensajeValidacion = "CODIGOS REPETIDOS";
                  banderita = false;
                  filtrarVigenciasPlantas.get(indice).setCodigo(backUpCodigo);

               } else {
                  banderita = true;
               }

            }
            if (banderita == true) {
               if (modificarVigenciasPlantas.isEmpty()) {
                  modificarVigenciasPlantas.add(filtrarVigenciasPlantas.get(indice));
               } else if (!modificarVigenciasPlantas.contains(filtrarVigenciasPlantas.get(indice))) {
                  modificarVigenciasPlantas.add(filtrarVigenciasPlantas.get(indice));
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
            if (filtrarVigenciasPlantas.get(indice).getCodigo() == a) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               banderita = false;
               filtrarVigenciasPlantas.get(indice).setCodigo(backUpCodigo);

            } else {
               for (int j = 0; j < filtrarVigenciasPlantas.size(); j++) {
                  if (j != indice) {
                     if (filtrarVigenciasPlantas.get(indice).getCodigo().equals(filtrarVigenciasPlantas.get(j).getCodigo())) {
                        contador++;
                     }
                  }
               }
               if (contador > 0) {
                  mensajeValidacion = "CODIGOS REPETIDOS";
                  banderita = false;
                  filtrarVigenciasPlantas.get(indice).setCodigo(backUpCodigo);

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
         RequestContext.getCurrentInstance().update("form:datosVigenciaPlanta");
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      }

   }

   public void borrandoVigenciasPlantas() {

      if (index >= 0) {
         if (tipoLista == 0) {
            log.info("Entro a borrandoVigenciasPlantas");
            if (!modificarVigenciasPlantas.isEmpty() && modificarVigenciasPlantas.contains(listVigenciasPlantas.get(index))) {
               int modIndex = modificarVigenciasPlantas.indexOf(listVigenciasPlantas.get(index));
               modificarVigenciasPlantas.remove(modIndex);
               borrarVigenciasPlantas.add(listVigenciasPlantas.get(index));
            } else if (!crearVigenciasPlantas.isEmpty() && crearVigenciasPlantas.contains(listVigenciasPlantas.get(index))) {
               int crearIndex = crearVigenciasPlantas.indexOf(listVigenciasPlantas.get(index));
               crearVigenciasPlantas.remove(crearIndex);
            } else {
               borrarVigenciasPlantas.add(listVigenciasPlantas.get(index));
            }
            listVigenciasPlantas.remove(index);
         }
         if (tipoLista == 1) {
            log.info("borrandoVigenciasPlantas ");
            if (!modificarVigenciasPlantas.isEmpty() && modificarVigenciasPlantas.contains(filtrarVigenciasPlantas.get(index))) {
               int modIndex = modificarVigenciasPlantas.indexOf(filtrarVigenciasPlantas.get(index));
               modificarVigenciasPlantas.remove(modIndex);
               borrarVigenciasPlantas.add(filtrarVigenciasPlantas.get(index));
            } else if (!crearVigenciasPlantas.isEmpty() && crearVigenciasPlantas.contains(filtrarVigenciasPlantas.get(index))) {
               int crearIndex = crearVigenciasPlantas.indexOf(filtrarVigenciasPlantas.get(index));
               crearVigenciasPlantas.remove(crearIndex);
            } else {
               borrarVigenciasPlantas.add(filtrarVigenciasPlantas.get(index));
            }
            int VCIndex = listVigenciasPlantas.indexOf(filtrarVigenciasPlantas.get(index));
            listVigenciasPlantas.remove(VCIndex);
            filtrarVigenciasPlantas.remove(index);

         }
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:datosVigenciaPlanta");
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
      BigInteger contarPlantasVigenciaPlanta;

      try {
         log.error("Control Secuencia de ControlVigenciasPlantas ");
         if (tipoLista == 0) {
            contarPlantasVigenciaPlanta = administrarVigenciasPlantas.contarPlantasVigenciaPlanta(listVigenciasPlantas.get(index).getSecuencia());
         } else {
            contarPlantasVigenciaPlanta = administrarVigenciasPlantas.contarPlantasVigenciaPlanta(filtrarVigenciasPlantas.get(index).getSecuencia());
         }
         if (contarPlantasVigenciaPlanta.equals(new BigInteger("0"))) {
            log.info("Borrado==0");
            borrandoVigenciasPlantas();
         } else {
            log.info("Borrado>0");

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:validacionBorrar");
            RequestContext.getCurrentInstance().execute("PF('validacionBorrar').show()");
            index = -1;
            contarPlantasVigenciaPlanta = new BigInteger("-1");

         }
      } catch (Exception e) {
         log.error("ERROR ControlVigenciasPlantas verificarBorrado ERROR  ", e);
      }
   }

   public void revisarDialogoGuardar() {

      if (!borrarVigenciasPlantas.isEmpty() || !crearVigenciasPlantas.isEmpty() || !modificarVigenciasPlantas.isEmpty()) {
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:confirmarGuardar");
         RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
      }

   }

   public void guardarVigenciasPlantas() {
      RequestContext context = RequestContext.getCurrentInstance();

      if (guardado == false) {
         log.info("Realizando guardarVigenciasPlantas");
         if (!borrarVigenciasPlantas.isEmpty()) {
            administrarVigenciasPlantas.borrarVigenciasPlantas(borrarVigenciasPlantas);

            //mostrarBorrados
            registrosBorrados = borrarVigenciasPlantas.size();
            RequestContext.getCurrentInstance().update("form:mostrarBorrados");
            RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
            borrarVigenciasPlantas.clear();
         }
         if (!crearVigenciasPlantas.isEmpty()) {
            administrarVigenciasPlantas.crearVigenciasPlantas(crearVigenciasPlantas);
            crearVigenciasPlantas.clear();
         }
         if (!modificarVigenciasPlantas.isEmpty()) {
            administrarVigenciasPlantas.modificarVigenciasPlantas(modificarVigenciasPlantas);
            modificarVigenciasPlantas.clear();
         }
         log.info("Se guardaron los datos con exito");
         listVigenciasPlantas = null;
         RequestContext.getCurrentInstance().update("form:datosVigenciaPlanta");
         FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
         k = 0;
         guardado = true;
      }
      index = -1;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");

   }

   public void editarCelda() {
      if (index >= 0) {
         if (tipoLista == 0) {
            editarVigenciaPlanta = listVigenciasPlantas.get(index);
         }
         if (tipoLista == 1) {
            editarVigenciaPlanta = filtrarVigenciasPlantas.get(index);
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

   public void agregarNuevoVigenciasPlantas() {
      log.info("agregarNuevoVigenciasPlantas");
      int contador = 0;
      int duplicados = 0;
      int duplicadosFechas = 0;
      Integer a = 0;
      a = null;
      mensajeValidacion = " ";
      RequestContext context = RequestContext.getCurrentInstance();
      if (nuevoVigenciaPlanta.getCodigo() == a) {
         mensajeValidacion = " *Codigo \n";
         log.info("Mensaje validacion : " + mensajeValidacion);
      } else {
         log.info("codigo en Motivo Cambio Cargo: " + nuevoVigenciaPlanta.getCodigo());

         for (int x = 0; x < listVigenciasPlantas.size(); x++) {
            if (listVigenciasPlantas.get(x).getCodigo().equals(nuevoVigenciaPlanta.getCodigo())) {
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
      if (nuevoVigenciaPlanta.getFechavigencia() == null) {
         mensajeValidacion = " *Fecha \n";
         log.info("Mensaje validacion : " + mensajeValidacion);
      } else {
         for (int j = 0; j < listVigenciasPlantas.size(); j++) {
            if (nuevoVigenciaPlanta.getFechavigencia().equals(listVigenciasPlantas.get(j).getFechavigencia())) {
               duplicadosFechas++;
            }
         }
         if (duplicadosFechas > 0) {
            mensajeValidacion += "Fechas Repetidas";
         } else {
            contador++;
         }

      }

      log.info("contador " + contador);
      FacesContext c = FacesContext.getCurrentInstance();
      if (contador == 2) {
         if (bandera == 1) {
            //CERRAR FILTRADO
            log.info("Desactivar");
            codigo = (Column) c.getViewRoot().findComponent("form:datosVigenciaPlanta:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            fecha = (Column) c.getViewRoot().findComponent("form:datosVigenciaPlanta:fecha");
            fecha.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosVigenciaPlanta");
            bandera = 0;
            filtrarVigenciasPlantas = null;
            tipoLista = 0;
         }
         log.info("Despues de la bandera");

         k++;
         l = BigInteger.valueOf(k);
         nuevoVigenciaPlanta.setSecuencia(l);

         crearVigenciasPlantas.add(nuevoVigenciaPlanta);

         listVigenciasPlantas.add(nuevoVigenciaPlanta);
         nuevoVigenciaPlanta = new VigenciasPlantas();
         RequestContext.getCurrentInstance().update("form:datosVigenciaPlanta");

         infoRegistro = "Cantidad de registros: " + listVigenciasPlantas.size();

         RequestContext.getCurrentInstance().update("form:informacionRegistro");
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }

         RequestContext.getCurrentInstance().execute("PF('nuevoRegistroVigenciasPlantas').hide()");
         index = -1;
         secRegistro = null;

      } else {
         RequestContext.getCurrentInstance().update("form:validacionNuevaCentroCosto");
         RequestContext.getCurrentInstance().execute("PF('validacionNuevaCentroCosto').show()");
         contador = 0;
      }
   }

   public void limpiarNuevoVigenciasPlantas() {
      log.info("limpiarNuevoVigenciasPlantas");
      nuevoVigenciaPlanta = new VigenciasPlantas();
      secRegistro = null;
      index = -1;

   }

   //------------------------------------------------------------------------------
   public void duplicandoVigenciasPlantas() {
      log.info("duplicandoVigenciasPlantas");
      if (index >= 0) {
         duplicarVigenciaPlanta = new VigenciasPlantas();
         k++;
         l = BigInteger.valueOf(k);

         if (tipoLista == 0) {
            duplicarVigenciaPlanta.setSecuencia(l);
            duplicarVigenciaPlanta.setCodigo(listVigenciasPlantas.get(index).getCodigo());
            duplicarVigenciaPlanta.setFechavigencia(listVigenciasPlantas.get(index).getFechavigencia());
         }
         if (tipoLista == 1) {
            duplicarVigenciaPlanta.setSecuencia(l);
            duplicarVigenciaPlanta.setCodigo(filtrarVigenciasPlantas.get(index).getCodigo());
            duplicarVigenciaPlanta.setFechavigencia(filtrarVigenciasPlantas.get(index).getFechavigencia());
         }

         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTE");
         RequestContext.getCurrentInstance().execute("PF('duplicarRegistroVigenciasPlantas').show()");
         index = -1;
         secRegistro = null;
      }
   }

   public void confirmarDuplicar() {
      log.error("ESTOY EN CONFIRMAR DUPLICAR TIPOS EMPRESAS");
      int contador = 0;
      mensajeValidacion = " ";
      int duplicados = 0;
      int duplicadosFechas = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      Integer a = 0;
      a = null;
      log.error("ConfirmarDuplicar codigo " + duplicarVigenciaPlanta.getCodigo());
      log.error("ConfirmarDuplicar Descripcion " + duplicarVigenciaPlanta.getFechavigencia());

      if (duplicarVigenciaPlanta.getCodigo() == a) {
         mensajeValidacion = mensajeValidacion + "   * Codigo \n";
         log.info("Mensaje validacion : " + mensajeValidacion);
      } else {
         for (int x = 0; x < listVigenciasPlantas.size(); x++) {
            if (listVigenciasPlantas.get(x).getCodigo().equals(duplicarVigenciaPlanta.getCodigo())) {
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
      if (duplicarVigenciaPlanta.getFechavigencia() == null) {
         mensajeValidacion = " *Fecha \n";
         log.info("Mensaje validacion : " + mensajeValidacion);
      } else {
         for (int j = 0; j < listVigenciasPlantas.size(); j++) {
            if (duplicarVigenciaPlanta.getFechavigencia().equals(listVigenciasPlantas.get(j).getFechavigencia())) {
               duplicadosFechas++;
            }
         }
         if (duplicadosFechas > 0) {
            mensajeValidacion += "Fechas Repetidas";
         } else {
            contador++;
         }

      }

      if (contador == 2) {

         log.info("Datos Duplicando: " + duplicarVigenciaPlanta.getSecuencia() + "  " + duplicarVigenciaPlanta.getCodigo());
         if (crearVigenciasPlantas.contains(duplicarVigenciaPlanta)) {
            log.info("Ya lo contengo.");
         }
         listVigenciasPlantas.add(duplicarVigenciaPlanta);
         crearVigenciasPlantas.add(duplicarVigenciaPlanta);
         RequestContext.getCurrentInstance().update("form:datosVigenciaPlanta");
         index = -1;
         secRegistro = null;
         if (guardado == true) {
            guardado = false;
         }

         infoRegistro = "Cantidad de registros: " + listVigenciasPlantas.size();

         RequestContext.getCurrentInstance().update("form:informacionRegistro");
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            //CERRAR FILTRADO
            codigo = (Column) c.getViewRoot().findComponent("form:datosVigenciaPlanta:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            fecha = (Column) c.getViewRoot().findComponent("form:datosVigenciaPlanta:fecha");
            fecha.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosVigenciaPlanta");
            bandera = 0;
            filtrarVigenciasPlantas = null;
            tipoLista = 0;
         }
         duplicarVigenciaPlanta = new VigenciasPlantas();
         RequestContext.getCurrentInstance().execute("PF('duplicarRegistroVigenciasPlantas').hide()");

      } else {
         contador = 0;
         RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
         RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
      }
   }

   public void limpiarDuplicarVigenciasPlantas() {
      duplicarVigenciaPlanta = new VigenciasPlantas();
   }

   public void exportPDF() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosVigenciaPlantaExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "VIGENCIASPLANTAS", false, false, "UTF-8", null, null);
      context.responseComplete();
      index = -1;
      secRegistro = null;
   }

   public void exportXLS() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosVigenciaPlantaExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "VIGENCIASPLANTAS", false, false, "UTF-8", null, null);
      context.responseComplete();
      index = -1;
      secRegistro = null;
   }

   public void verificarRastro() {
      RequestContext context = RequestContext.getCurrentInstance();
      log.info("lol");
      if (!listVigenciasPlantas.isEmpty()) {
         if (secRegistro != null) {
            log.info("lol 2");
            int resultado = administrarRastros.obtenerTabla(secRegistro, "VIGENCIASPLANTAS"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
      } else if (administrarRastros.verificarHistoricosTabla("VIGENCIASPLANTAS")) { // igual acá
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
      index = -1;
   }

   //*/*/*/*/*/*/*/*/*/*-/-*//-*/-*/*/*-*/-*/-*/*/*/*/*/---/*/*/*/*/-*/-*/-*/-*/-*/
   public List<VigenciasPlantas> getListVigenciasPlantas() {
      if (listVigenciasPlantas == null) {
         listVigenciasPlantas = administrarVigenciasPlantas.consultarVigenciasPlantas();
      }
      RequestContext context = RequestContext.getCurrentInstance();
      if (listVigenciasPlantas == null || listVigenciasPlantas.isEmpty()) {
         infoRegistro = "Cantidad de registros: 0 ";
      } else {
         infoRegistro = "Cantidad de registros: " + listVigenciasPlantas.size();
      }
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
      return listVigenciasPlantas;
   }

   public void setListVigenciasPlantas(List<VigenciasPlantas> listVigenciasPlantas) {
      this.listVigenciasPlantas = listVigenciasPlantas;
   }

   public List<VigenciasPlantas> getFiltrarVigenciasPlantas() {
      return filtrarVigenciasPlantas;
   }

   public void setFiltrarVigenciasPlantas(List<VigenciasPlantas> filtrarVigenciasPlantas) {
      this.filtrarVigenciasPlantas = filtrarVigenciasPlantas;
   }

   public VigenciasPlantas getNuevoVigenciaPlanta() {
      return nuevoVigenciaPlanta;
   }

   public void setNuevoVigenciaPlanta(VigenciasPlantas nuevoVigenciaPlanta) {
      this.nuevoVigenciaPlanta = nuevoVigenciaPlanta;
   }

   public VigenciasPlantas getDuplicarVigenciaPlanta() {
      return duplicarVigenciaPlanta;
   }

   public void setDuplicarVigenciaPlanta(VigenciasPlantas duplicarVigenciaPlanta) {
      this.duplicarVigenciaPlanta = duplicarVigenciaPlanta;
   }

   public VigenciasPlantas getEditarVigenciaPlanta() {
      return editarVigenciaPlanta;
   }

   public void setEditarVigenciaPlanta(VigenciasPlantas editarVigenciaPlanta) {
      this.editarVigenciaPlanta = editarVigenciaPlanta;
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

   public VigenciasPlantas getVigenciasPlantasSeleccionado() {
      return vigenciasPlantasSeleccionado;
   }

   public void setVigenciasPlantasSeleccionado(VigenciasPlantas vigenciasPlantasSeleccionado) {
      this.vigenciasPlantasSeleccionado = vigenciasPlantasSeleccionado;
   }

   public String getInfoRegistro() {
      return infoRegistro;
   }

   public void setInfoRegistro(String infoRegistro) {
      this.infoRegistro = infoRegistro;
   }

   public int getTamano() {
      return tamano;
   }

   public void setTamano(int tamano) {
      this.tamano = tamano;
   }

}
