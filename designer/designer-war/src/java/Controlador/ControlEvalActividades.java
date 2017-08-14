/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.EvalActividades;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarEvalActividadesInterface;
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
public class ControlEvalActividades implements Serializable {

   private static Logger log = Logger.getLogger(ControlEvalActividades.class);

   @EJB
   AdministrarEvalActividadesInterface administrarEvalActividades;
   @EJB
   AdministrarRastrosInterface administrarRastros;

   private List<EvalActividades> listEvalActividades;
   private List<EvalActividades> filtrarEvalActividades;
   private List<EvalActividades> crearEvalActividades;
   private List<EvalActividades> modificarEvalActividades;
   private List<EvalActividades> borrarEvalActividades;
   private EvalActividades nuevoEvalActividades;
   private EvalActividades duplicarEvalActividades;
   private EvalActividades editarEvalActividades;
   private EvalActividades evalActividadSeleccionada;
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
   private Integer backupCodigo;
   private String backupDescripcion;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

   public ControlEvalActividades() {
      listEvalActividades = null;
      crearEvalActividades = new ArrayList<EvalActividades>();
      modificarEvalActividades = new ArrayList<EvalActividades>();
      borrarEvalActividades = new ArrayList<EvalActividades>();
      permitirIndex = true;
      editarEvalActividades = new EvalActividades();
      nuevoEvalActividades = new EvalActividades();
      duplicarEvalActividades = new EvalActividades();
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
      String pagActual = "evalactividad";
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
         administrarEvalActividades.obtenerConexion(ses.getId());
         administrarRastros.obtenerConexion(ses.getId());
      } catch (Exception e) {
         log.error("Error postconstruct " + this.getClass().getName() + ": " + e);
         log.error("Causa: " + e.getCause());
      }
   }

   public void eventoFiltrar() {
      try {
         log.info("\n ENTRE A ControlEvalActividades.eventoFiltrar \n");
         if (tipoLista == 0) {
            tipoLista = 1;
         }
         RequestContext context = RequestContext.getCurrentInstance();
         infoRegistro = "Cantidad de registros: " + filtrarEvalActividades.size();
         RequestContext.getCurrentInstance().update("form:informacionRegistro");
      } catch (Exception e) {
         log.warn("Error ControlEvalActividades eventoFiltrar ERROR===" + e.getMessage());
      }
   }

   public void cambiarIndice(int indice, int celda) {
      log.error("TIPO LISTA = " + tipoLista);

      if (permitirIndex == true) {
         index = indice;
         cualCelda = celda;
         secRegistro = listEvalActividades.get(index).getSecuencia();
         if (tipoLista == 0) {
            backupCodigo = listEvalActividades.get(index).getCodigo();
            backupDescripcion = listEvalActividades.get(index).getDescripcion();
         } else if (tipoLista == 1) {
            backupCodigo = filtrarEvalActividades.get(index).getCodigo();
            backupDescripcion = filtrarEvalActividades.get(index).getDescripcion();
         }
      }
      log.info("Indice: " + index + " Celda: " + cualCelda);
   }

   public void asignarIndex(Integer indice, int LND, int dig) {
      try {
         log.info("\n ENTRE A ControlEvalActividades.asignarIndex \n");
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
         log.warn("Error ControlEvalActividades.asignarIndex ERROR======" + e.getMessage());
      }
   }

   public void activarAceptar() {
      aceptar = false;
   }

   public void listaValoresBoton() {
   }
   private String infoRegistro;

   public void cancelarModificacion() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 1) {
         //CERRAR FILTRADO
         codigo = (Column) c.getViewRoot().findComponent("form:datosEvalActividades:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosEvalActividades:descripcion");
         descripcion.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosEvalActividades");
         bandera = 0;
         filtrarEvalActividades = null;
         tipoLista = 0;
      }

      borrarEvalActividades.clear();
      crearEvalActividades.clear();
      modificarEvalActividades.clear();
      index = -1;
      secRegistro = null;
      k = 0;
      listEvalActividades = null;
      guardado = true;
      permitirIndex = true;
      getListEvalActividades();
      RequestContext context = RequestContext.getCurrentInstance();
      if (listEvalActividades == null || listEvalActividades.isEmpty()) {
         infoRegistro = "Cantidad de registros: 0 ";
      } else {
         infoRegistro = "Cantidad de registros: " + listEvalActividades.size();
      }
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
      RequestContext.getCurrentInstance().update("form:datosEvalActividades");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void salir() {
      limpiarListasValor();
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 1) {
         //CERRAR FILTRADO
         codigo = (Column) c.getViewRoot().findComponent("form:datosEvalActividades:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosEvalActividades:descripcion");
         descripcion.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosEvalActividades");
         bandera = 0;
         filtrarEvalActividades = null;
         tipoLista = 0;
      }

      borrarEvalActividades.clear();
      crearEvalActividades.clear();
      modificarEvalActividades.clear();
      index = -1;
      secRegistro = null;
      k = 0;
      listEvalActividades = null;
      guardado = true;
      permitirIndex = true;
      getListEvalActividades();
      RequestContext context = RequestContext.getCurrentInstance();
      if (listEvalActividades == null || listEvalActividades.isEmpty()) {
         infoRegistro = "Cantidad de registros: 0 ";
      } else {
         infoRegistro = "Cantidad de registros: " + listEvalActividades.size();
      }
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
      RequestContext.getCurrentInstance().update("form:datosEvalActividades");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void activarCtrlF11() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 0) {
         tamano = 250;
         codigo = (Column) c.getViewRoot().findComponent("form:datosEvalActividades:codigo");
         codigo.setFilterStyle("width: 85% !important");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosEvalActividades:descripcion");
         descripcion.setFilterStyle("width: 85% !important");
         RequestContext.getCurrentInstance().update("form:datosEvalActividades");
         log.info("Activar");
         bandera = 1;
      } else if (bandera == 1) {
         log.info("Desactivar");
         tamano = 270;
         codigo = (Column) c.getViewRoot().findComponent("form:datosEvalActividades:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosEvalActividades:descripcion");
         descripcion.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosEvalActividades");
         bandera = 0;
         filtrarEvalActividades = null;
         tipoLista = 0;
      }
   }

   public void modificarEvalActividades(int indice, String confirmarCambio, String valorConfirmar) {
      log.error("ENTRE A MODIFICAR SUB CATEGORIA");
      index = indice;

      int contador = 0;
      boolean banderita = false;
      boolean banderita1 = false;

      RequestContext context = RequestContext.getCurrentInstance();
      log.error("TIPO LISTA = " + tipoLista);
      if (confirmarCambio.equalsIgnoreCase("N")) {
         log.error("ENTRE A MODIFICAR EMPRESAS, CONFIRMAR CAMBIO ES N");
         if (tipoLista == 0) {
            if (!crearEvalActividades.contains(listEvalActividades.get(indice))) {

               log.info("backupCodigo : " + backupCodigo);
               log.info("backupDescripcion : " + backupDescripcion);

               if (listEvalActividades.get(indice).getCodigo() == null) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  banderita = false;
                  listEvalActividades.get(indice).setCodigo(backupCodigo);
               } else {
                  for (int j = 0; j < listEvalActividades.size(); j++) {
                     if (j != indice) {
                        if (listEvalActividades.get(indice).getCodigo() == listEvalActividades.get(j).getCodigo()) {
                           contador++;
                        }
                     }
                  }

                  if (contador > 0) {
                     banderita = false;
                     mensajeValidacion = "CODIGOS REPETIDOS";
                     banderita = false;
                     listEvalActividades.get(indice).setCodigo(backupCodigo);
                  } else {
                     banderita = true;
                  }

               }
               if (listEvalActividades.get(indice).getDescripcion().isEmpty()) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  banderita1 = false;
                  listEvalActividades.get(indice).setDescripcion(backupDescripcion);
               } else if (listEvalActividades.get(indice).getDescripcion() == null) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  banderita1 = false;
                  listEvalActividades.get(indice).setDescripcion(backupDescripcion);

               } else {
                  banderita1 = true;
               }

               if (banderita == true && banderita1 == true) {
                  if (modificarEvalActividades.isEmpty()) {
                     modificarEvalActividades.add(listEvalActividades.get(indice));
                  } else if (!modificarEvalActividades.contains(listEvalActividades.get(indice))) {
                     modificarEvalActividades.add(listEvalActividades.get(indice));
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
               RequestContext.getCurrentInstance().update("form:datosEvalActividades");
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            } else {

               log.info("backupCodigo : " + backupCodigo);
               log.info("backupDescripcion : " + backupDescripcion);

               if (listEvalActividades.get(indice).getCodigo() == null) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  banderita = false;
                  listEvalActividades.get(indice).setCodigo(backupCodigo);
               } else {
                  for (int j = 0; j < listEvalActividades.size(); j++) {
                     if (j != indice) {
                        if (listEvalActividades.get(indice).getCodigo() == listEvalActividades.get(j).getCodigo()) {
                           contador++;
                        }
                     }
                  }

                  if (contador > 0) {
                     mensajeValidacion = "CODIGOS REPETIDOS";
                     banderita = false;
                     listEvalActividades.get(indice).setCodigo(backupCodigo);
                  } else {
                     banderita = true;
                  }

               }
               if (listEvalActividades.get(indice).getDescripcion().isEmpty()) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  banderita1 = false;
                  listEvalActividades.get(indice).setDescripcion(backupDescripcion);
               } else if (listEvalActividades.get(indice).getDescripcion() == null) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  banderita1 = false;
                  listEvalActividades.get(indice).setDescripcion(backupDescripcion);

               } else {
                  banderita1 = true;
               }

               if (banderita == true && banderita1 == true) {
                  if (guardado == true) {
                     guardado = false;
                  }
               } else {
                  RequestContext.getCurrentInstance().update("form:validacionModificar");
                  RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");

               }
               index = -1;
               secRegistro = null;
               RequestContext.getCurrentInstance().update("form:datosEvalActividades");
               RequestContext.getCurrentInstance().update("form:ACEPTAR");

            }
         } else if (!crearEvalActividades.contains(filtrarEvalActividades.get(indice))) {
            if (filtrarEvalActividades.get(indice).getCodigo() == null) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               banderita = false;
               filtrarEvalActividades.get(indice).setCodigo(backupCodigo);
            } else {
               for (int j = 0; j < filtrarEvalActividades.size(); j++) {
                  if (j != indice) {
                     if (filtrarEvalActividades.get(indice).getCodigo() == listEvalActividades.get(j).getCodigo()) {
                        contador++;
                     }
                  }
               }
               for (int j = 0; j < listEvalActividades.size(); j++) {
                  if (j != indice) {
                     if (filtrarEvalActividades.get(indice).getCodigo() == listEvalActividades.get(j).getCodigo()) {
                        contador++;
                     }
                  }
               }
               if (contador > 0) {
                  mensajeValidacion = "CODIGOS REPETIDOS";
                  banderita = false;
                  filtrarEvalActividades.get(indice).setCodigo(backupCodigo);

               } else {
                  banderita = true;
               }

            }

            if (filtrarEvalActividades.get(indice).getDescripcion().isEmpty()) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               banderita1 = false;
               filtrarEvalActividades.get(indice).setDescripcion(backupDescripcion);
            }
            if (filtrarEvalActividades.get(indice).getDescripcion() == null) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               banderita1 = false;
               filtrarEvalActividades.get(indice).setDescripcion(backupDescripcion);
            }

            if (banderita == true && banderita1 == true) {
               if (modificarEvalActividades.isEmpty()) {
                  modificarEvalActividades.add(filtrarEvalActividades.get(indice));
               } else if (!modificarEvalActividades.contains(filtrarEvalActividades.get(indice))) {
                  modificarEvalActividades.add(filtrarEvalActividades.get(indice));
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
            if (filtrarEvalActividades.get(indice).getCodigo() == null) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               banderita = false;
               filtrarEvalActividades.get(indice).setCodigo(backupCodigo);
            } else {
               for (int j = 0; j < filtrarEvalActividades.size(); j++) {
                  if (j != indice) {
                     if (filtrarEvalActividades.get(indice).getCodigo() == listEvalActividades.get(j).getCodigo()) {
                        contador++;
                     }
                  }
               }
               for (int j = 0; j < listEvalActividades.size(); j++) {
                  if (j != indice) {
                     if (filtrarEvalActividades.get(indice).getCodigo() == listEvalActividades.get(j).getCodigo()) {
                        contador++;
                     }
                  }
               }
               if (contador > 0) {
                  mensajeValidacion = "CODIGOS REPETIDOS";
                  banderita = false;
                  filtrarEvalActividades.get(indice).setCodigo(backupCodigo);

               } else {
                  banderita = true;
               }

            }

            if (filtrarEvalActividades.get(indice).getDescripcion().isEmpty()) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               banderita1 = false;
               filtrarEvalActividades.get(indice).setDescripcion(backupDescripcion);
            }
            if (filtrarEvalActividades.get(indice).getDescripcion() == null) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               banderita1 = false;
               filtrarEvalActividades.get(indice).setDescripcion(backupDescripcion);
            }

            if (banderita == true && banderita1 == true) {
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
         RequestContext.getCurrentInstance().update("form:datosEvalActividades");
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      }

   }

   public void borrandoEvalActividades() {

      if (index >= 0) {
         if (tipoLista == 0) {
            log.info("Entro a borrandoEvalActividades");
            if (!modificarEvalActividades.isEmpty() && modificarEvalActividades.contains(listEvalActividades.get(index))) {
               int modIndex = modificarEvalActividades.indexOf(listEvalActividades.get(index));
               modificarEvalActividades.remove(modIndex);
               borrarEvalActividades.add(listEvalActividades.get(index));
            } else if (!crearEvalActividades.isEmpty() && crearEvalActividades.contains(listEvalActividades.get(index))) {
               int crearIndex = crearEvalActividades.indexOf(listEvalActividades.get(index));
               crearEvalActividades.remove(crearIndex);
            } else {
               borrarEvalActividades.add(listEvalActividades.get(index));
            }
            listEvalActividades.remove(index);
         }
         if (tipoLista == 1) {
            log.info("borrandoEvalActividades ");
            if (!modificarEvalActividades.isEmpty() && modificarEvalActividades.contains(filtrarEvalActividades.get(index))) {
               int modIndex = modificarEvalActividades.indexOf(filtrarEvalActividades.get(index));
               modificarEvalActividades.remove(modIndex);
               borrarEvalActividades.add(filtrarEvalActividades.get(index));
            } else if (!crearEvalActividades.isEmpty() && crearEvalActividades.contains(filtrarEvalActividades.get(index))) {
               int crearIndex = crearEvalActividades.indexOf(filtrarEvalActividades.get(index));
               crearEvalActividades.remove(crearIndex);
            } else {
               borrarEvalActividades.add(filtrarEvalActividades.get(index));
            }
            int VCIndex = listEvalActividades.indexOf(filtrarEvalActividades.get(index));
            listEvalActividades.remove(VCIndex);
            filtrarEvalActividades.remove(index);

         }
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:datosEvalActividades");
         infoRegistro = "Cantidad de registros: " + listEvalActividades.size();
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
      BigInteger contarCapBuzonesEvalActividad;
      BigInteger contarCapNecesidadesEvalActividad;
      BigInteger contarEvalPlanesDesarrollosEvalActividad;

      try {
         log.error("Control Secuencia de ControlEvalActividades ");
         if (tipoLista == 0) {
            contarCapBuzonesEvalActividad = administrarEvalActividades.contarCapBuzonesEvalActividad(listEvalActividades.get(index).getSecuencia());
            contarCapNecesidadesEvalActividad = administrarEvalActividades.contarCapNecesidadesEvalActividad(listEvalActividades.get(index).getSecuencia());
            contarEvalPlanesDesarrollosEvalActividad = administrarEvalActividades.contarEvalPlanesDesarrollosEvalActividad(listEvalActividades.get(index).getSecuencia());
         } else {
            contarCapBuzonesEvalActividad = administrarEvalActividades.contarCapBuzonesEvalActividad(filtrarEvalActividades.get(index).getSecuencia());
            contarCapNecesidadesEvalActividad = administrarEvalActividades.contarCapNecesidadesEvalActividad(filtrarEvalActividades.get(index).getSecuencia());
            contarEvalPlanesDesarrollosEvalActividad = administrarEvalActividades.contarEvalPlanesDesarrollosEvalActividad(filtrarEvalActividades.get(index).getSecuencia());
         }
         if (contarCapBuzonesEvalActividad.equals(new BigInteger("0")) && contarCapNecesidadesEvalActividad.equals(new BigInteger("0")) && contarEvalPlanesDesarrollosEvalActividad.equals(new BigInteger("0"))) {
            log.info("Borrado==0");
            borrandoEvalActividades();
         } else {
            log.info("Borrado>0");

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:validacionBorrar");
            RequestContext.getCurrentInstance().execute("PF('validacionBorrar').show()");
            index = -1;
            contarCapBuzonesEvalActividad = new BigInteger("-1");
            contarCapNecesidadesEvalActividad = new BigInteger("-1");
            contarEvalPlanesDesarrollosEvalActividad = new BigInteger("-1");

         }
      } catch (Exception e) {
         log.error("ERROR ControlEvalActividades verificarBorrado ERROR " + e);
      }
   }

   public void revisarDialogoGuardar() {

      if (!borrarEvalActividades.isEmpty() || !crearEvalActividades.isEmpty() || !modificarEvalActividades.isEmpty()) {
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:confirmarGuardar");
         RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
      }

   }

   public void guardarEvalActividades() {
      RequestContext context = RequestContext.getCurrentInstance();

      if (guardado == false) {
         log.info("Realizando guardarEvalActividades");
         if (!borrarEvalActividades.isEmpty()) {
            administrarEvalActividades.borrarEvalActividades(borrarEvalActividades);
            //mostrarBorrados
            registrosBorrados = borrarEvalActividades.size();
            RequestContext.getCurrentInstance().update("form:mostrarBorrados");
            RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
            borrarEvalActividades.clear();
         }
         if (!modificarEvalActividades.isEmpty()) {
            administrarEvalActividades.modificarEvalActividades(modificarEvalActividades);
            modificarEvalActividades.clear();
         }
         if (!crearEvalActividades.isEmpty()) {
            administrarEvalActividades.crearEvalActividades(crearEvalActividades);
            crearEvalActividades.clear();
         }
         log.info("Se guardaron los datos con exito");
         listEvalActividades = null;
         FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
         RequestContext.getCurrentInstance().update("form:datosEvalActividades");
         k = 0;
         guardado = true;
      }
      index = -1;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");

   }

   public void editarCelda() {
      if (index >= 0) {
         if (tipoLista == 0) {
            editarEvalActividades = listEvalActividades.get(index);
         }
         if (tipoLista == 1) {
            editarEvalActividades = filtrarEvalActividades.get(index);
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

   public void agregarNuevoEvalActividades() {
      log.info("agregarNuevoEvalActividades");
      int contador = 0;
      int duplicados = 0;

      Integer a = 0;
      a = null;
      mensajeValidacion = " ";
      RequestContext context = RequestContext.getCurrentInstance();
      if (nuevoEvalActividades.getCodigo() == a) {
         mensajeValidacion = " *Codigo \n";
         log.info("Mensaje validacion : " + mensajeValidacion);
      } else {
         log.info("codigo en Motivo Cambio Cargo: " + nuevoEvalActividades.getCodigo());

         for (int x = 0; x < listEvalActividades.size(); x++) {
            if (listEvalActividades.get(x).getCodigo() == nuevoEvalActividades.getCodigo()) {
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
      if (nuevoEvalActividades.getDescripcion() == null) {
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
            codigo = (Column) c.getViewRoot().findComponent("form:datosEvalActividades:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosEvalActividades:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosEvalActividades");
            bandera = 0;
            filtrarEvalActividades = null;
            tipoLista = 0;
         }
         log.info("Despues de la bandera");

         k++;
         l = BigInteger.valueOf(k);
         nuevoEvalActividades.setSecuencia(l);

         crearEvalActividades.add(nuevoEvalActividades);

         listEvalActividades.add(nuevoEvalActividades);
         nuevoEvalActividades = new EvalActividades();
         RequestContext.getCurrentInstance().update("form:datosEvalActividades");
         infoRegistro = "Cantidad de registros: " + listEvalActividades.size();
         RequestContext.getCurrentInstance().update("form:informacionRegistro");
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }

         RequestContext.getCurrentInstance().execute("PF('nuevoRegistroEvalActividades').hide()");
         index = -1;
         secRegistro = null;

      } else {
         RequestContext.getCurrentInstance().update("form:validacionNuevaCentroCosto");
         RequestContext.getCurrentInstance().execute("PF('validacionNuevaCentroCosto').show()");
         contador = 0;
      }
   }

   public void limpiarNuevoEvalActividades() {
      log.info("limpiarNuevoEvalActividades");
      nuevoEvalActividades = new EvalActividades();
      secRegistro = null;
      index = -1;

   }

   //------------------------------------------------------------------------------
   public void duplicandoEvalActividades() {
      log.info("duplicandoEvalActividades");
      if (index >= 0) {
         duplicarEvalActividades = new EvalActividades();
         k++;
         l = BigInteger.valueOf(k);

         if (tipoLista == 0) {
            duplicarEvalActividades.setSecuencia(l);
            duplicarEvalActividades.setCodigo(listEvalActividades.get(index).getCodigo());
            duplicarEvalActividades.setDescripcion(listEvalActividades.get(index).getDescripcion());
         }
         if (tipoLista == 1) {
            duplicarEvalActividades.setSecuencia(l);
            duplicarEvalActividades.setCodigo(filtrarEvalActividades.get(index).getCodigo());
            duplicarEvalActividades.setDescripcion(filtrarEvalActividades.get(index).getDescripcion());
         }

         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTE");
         RequestContext.getCurrentInstance().execute("PF('duplicarRegistroEvalActividades').show()");
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
      log.error("ConfirmarDuplicar codigo " + duplicarEvalActividades.getCodigo());
      log.error("ConfirmarDuplicar Descripcion " + duplicarEvalActividades.getDescripcion());

      if (duplicarEvalActividades.getCodigo() == a) {
         mensajeValidacion = mensajeValidacion + "   *Codigo \n";
         log.info("Mensaje validacion : " + mensajeValidacion);
      } else {
         for (int x = 0; x < listEvalActividades.size(); x++) {
            if (listEvalActividades.get(x).getCodigo() == duplicarEvalActividades.getCodigo()) {
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
      if (duplicarEvalActividades.getDescripcion() == null) {
         mensajeValidacion = mensajeValidacion + "   *Descripcion \n";
         log.info("Mensaje validacion : " + mensajeValidacion);

      } else {
         log.info("Bandera : ");
         contador++;
      }

      if (contador == 2) {

         log.info("Datos Duplicando: " + duplicarEvalActividades.getSecuencia() + "  " + duplicarEvalActividades.getCodigo());
         if (crearEvalActividades.contains(duplicarEvalActividades)) {
            log.info("Ya lo contengo.");
         }
         listEvalActividades.add(duplicarEvalActividades);
         crearEvalActividades.add(duplicarEvalActividades);
         RequestContext.getCurrentInstance().update("form:datosEvalActividades");
         index = -1;
         secRegistro = null;
         if (guardado == true) {
            guardado = false;
         }
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         infoRegistro = "Cantidad de registros: " + listEvalActividades.size();
         RequestContext.getCurrentInstance().update("form:informacionRegistro");
         if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            //CERRAR FILTRADO
            codigo = (Column) c.getViewRoot().findComponent("form:datosEvalActividades:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosEvalActividades:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosEvalActividades");
            bandera = 0;
            filtrarEvalActividades = null;
            tipoLista = 0;
         }
         duplicarEvalActividades = new EvalActividades();
         RequestContext.getCurrentInstance().execute("PF('duplicarRegistroEvalActividades').hide()");

      } else {
         contador = 0;
         RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
         RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
      }
   }

   public void limpiarDuplicarEvalActividades() {
      duplicarEvalActividades = new EvalActividades();
   }

   public void exportPDF() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosEvalActividadesExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "EVALACTIVIDADES", false, false, "UTF-8", null, null);
      context.responseComplete();
      index = -1;
      secRegistro = null;
   }

   public void exportXLS() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosEvalActividadesExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "EVALACTIVIDADES", false, false, "UTF-8", null, null);
      context.responseComplete();
      index = -1;
      secRegistro = null;
   }

   public void verificarRastro() {
      RequestContext context = RequestContext.getCurrentInstance();
      log.info("lol");
      if (!listEvalActividades.isEmpty()) {
         if (secRegistro != null) {
            log.info("lol 2");
            int resultado = administrarRastros.obtenerTabla(secRegistro, "EVALACTIVIDADES"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
      } else if (administrarRastros.verificarHistoricosTabla("EVALACTIVIDADES")) { // igual acá
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
      index = -1;
   }

   //*/*/*/*/*/*/*/*/*/*-/-*//-*/-*/*/*-*/-*/-*/*/*/*/*/---/*/*/*/*/-*/-*/-*/-*/-*/
   public List<EvalActividades> getListEvalActividades() {
      if (listEvalActividades == null) {
         listEvalActividades = administrarEvalActividades.consultarEvalActividades();
      }
      RequestContext context = RequestContext.getCurrentInstance();
      if (listEvalActividades == null || listEvalActividades.isEmpty()) {
         infoRegistro = "Cantidad de registros: 0 ";
      } else {
         infoRegistro = "Cantidad de registros: " + listEvalActividades.size();
      }
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
      return listEvalActividades;
   }

   public void setListEvalActividades(List<EvalActividades> listEvalActividades) {
      this.listEvalActividades = listEvalActividades;
   }

   public List<EvalActividades> getFiltrarEvalActividades() {
      return filtrarEvalActividades;
   }

   public void setFiltrarEvalActividades(List<EvalActividades> filtrarEvalActividades) {
      this.filtrarEvalActividades = filtrarEvalActividades;
   }

   public EvalActividades getNuevoEvalActividades() {
      return nuevoEvalActividades;
   }

   public void setNuevoEvalActividades(EvalActividades nuevoEvalActividades) {
      this.nuevoEvalActividades = nuevoEvalActividades;
   }

   public EvalActividades getDuplicarEvalActividades() {
      return duplicarEvalActividades;
   }

   public void setDuplicarEvalActividades(EvalActividades duplicarEvalActividades) {
      this.duplicarEvalActividades = duplicarEvalActividades;
   }

   public EvalActividades getEditarEvalActividades() {
      return editarEvalActividades;
   }

   public void setEditarEvalActividades(EvalActividades editarEvalActividades) {
      this.editarEvalActividades = editarEvalActividades;
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

   public EvalActividades getEvalActividadSeleccionada() {
      return evalActividadSeleccionada;
   }

   public void setEvalActividadSeleccionada(EvalActividades evalActividadSeleccionada) {
      this.evalActividadSeleccionada = evalActividadSeleccionada;
   }

   public String getInfoRegistro() {
      return infoRegistro;
   }

   public void setInfoRegistro(String infoRegistro) {
      this.infoRegistro = infoRegistro;
   }

}
