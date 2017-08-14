/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.EvalDimensiones;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarEvalDimensionesInterface;
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
public class ControlEvalDimensiones implements Serializable {

   private static Logger log = Logger.getLogger(ControlEvalDimensiones.class);

   @EJB
   AdministrarEvalDimensionesInterface administrarEvalDimensiones;
   @EJB
   AdministrarRastrosInterface administrarRastros;
   private List<EvalDimensiones> listEvalDimensiones;
   private List<EvalDimensiones> filtrarEvalDimensiones;
   private List<EvalDimensiones> crearEvalDimensiones;
   private List<EvalDimensiones> modificarEvalDimensiones;
   private List<EvalDimensiones> borrarEvalDimensiones;
   private EvalDimensiones nuevoEvalDimension;
   private EvalDimensiones duplicarEvalDimension;
   private EvalDimensiones editarEvalDimension;
   private EvalDimensiones evalDimensionSeleccionada;
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
   private int tamano;
   private String mensajeValidacion;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

   public ControlEvalDimensiones() {

      listEvalDimensiones = null;
      crearEvalDimensiones = new ArrayList<EvalDimensiones>();
      modificarEvalDimensiones = new ArrayList<EvalDimensiones>();
      borrarEvalDimensiones = new ArrayList<EvalDimensiones>();
      permitirIndex = true;
      guardado = true;
      editarEvalDimension = new EvalDimensiones();
      nuevoEvalDimension = new EvalDimensiones();
      duplicarEvalDimension = new EvalDimensiones();
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
      String pagActual = "evaldimencion";
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
         administrarEvalDimensiones.obtenerConexion(ses.getId());
         administrarRastros.obtenerConexion(ses.getId());
      } catch (Exception e) {
         log.error("Error postconstruct " + this.getClass().getName() + ": " + e);
         log.error("Causa: " + e.getCause());
      }
   }

   public void eventoFiltrar() {
      try {
         log.info("\n ENTRE A ControlMotivosLocalizaciones.eventoFiltrar \n");
         if (tipoLista == 0) {
            tipoLista = 1;
         }
         RequestContext context = RequestContext.getCurrentInstance();
         infoRegistro = "Cantidad de registros: " + filtrarEvalDimensiones.size();
         RequestContext.getCurrentInstance().update("form:informacionRegistro");
      } catch (Exception e) {
         log.warn("Error ControlMotivosLocalizaciones eventoFiltrar ERROR===" + e.getMessage());
      }
   }

   private String backupDescripcion;
   private Integer backupCodigo;

   public void cambiarIndice(int indice, int celda) {
      log.error("TIPO LISTA = " + tipoLista);

      if (permitirIndex == true) {
         index = indice;
         cualCelda = celda;
         secRegistro = listEvalDimensiones.get(index).getSecuencia();
         if (tipoLista == 0) {
            backupCodigo = listEvalDimensiones.get(index).getCodigo();
            backupDescripcion = listEvalDimensiones.get(index).getDescripcion();
         } else if (tipoLista == 1) {
            backupCodigo = filtrarEvalDimensiones.get(index).getCodigo();
            backupDescripcion = filtrarEvalDimensiones.get(index).getDescripcion();
         }
      }
      log.info("Indice: " + index + " Celda: " + cualCelda);
   }

   public void asignarIndex(Integer indice, int LND, int dig) {
      try {
         log.info("\n ENTRE A ControlEvalDimensiones.asignarIndex \n");
         index = indice;
         RequestContext context = RequestContext.getCurrentInstance();
         if (LND == 0) {
            tipoActualizacion = 0;
         } else if (LND == 1) {
            tipoActualizacion = 1;
            log.info("Tipo Actualizacion: " + tipoActualizacion);
         } else if (LND == 2) {
            tipoActualizacion = 2;
         }

      } catch (Exception e) {
         log.warn("Error ControlEvalDimensiones.asignarIndex ERROR======" + e.getMessage());
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
         codigo = (Column) c.getViewRoot().findComponent("form:datosEvalDimension:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosEvalDimension:descripcion");
         descripcion.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosEvalDimension");
         bandera = 0;
         filtrarEvalDimensiones = null;
         tipoLista = 0;
      }

      borrarEvalDimensiones.clear();
      crearEvalDimensiones.clear();
      modificarEvalDimensiones.clear();
      index = -1;
      secRegistro = null;
      k = 0;
      listEvalDimensiones = null;
      guardado = true;
      permitirIndex = true;
      getListEvalDimensiones();
      RequestContext context = RequestContext.getCurrentInstance();
      if (listEvalDimensiones == null || listEvalDimensiones.isEmpty()) {
         infoRegistro = "Cantidad de registros: 0 ";
      } else {
         infoRegistro = "Cantidad de registros: " + listEvalDimensiones.size();
      }
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
      RequestContext.getCurrentInstance().update("form:datosEvalDimension");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void salir() {
      limpiarListasValor();
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 1) {
         //CERRAR FILTRADO
         codigo = (Column) c.getViewRoot().findComponent("form:datosEvalDimension:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosEvalDimension:descripcion");
         descripcion.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosEvalDimension");
         bandera = 0;
         filtrarEvalDimensiones = null;
         tipoLista = 0;
      }

      borrarEvalDimensiones.clear();
      crearEvalDimensiones.clear();
      modificarEvalDimensiones.clear();
      index = -1;
      secRegistro = null;
      k = 0;
      listEvalDimensiones = null;
      guardado = true;
      permitirIndex = true;
      getListEvalDimensiones();
      RequestContext context = RequestContext.getCurrentInstance();
      if (listEvalDimensiones == null || listEvalDimensiones.isEmpty()) {
         infoRegistro = "Cantidad de registros: 0 ";
      } else {
         infoRegistro = "Cantidad de registros: " + listEvalDimensiones.size();
      }
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
      RequestContext.getCurrentInstance().update("form:datosEvalDimension");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void activarCtrlF11() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 0) {
         tamano = 250;
         codigo = (Column) c.getViewRoot().findComponent("form:datosEvalDimension:codigo");
         codigo.setFilterStyle("width: 85% !important");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosEvalDimension:descripcion");
         descripcion.setFilterStyle("width: 85% !important");
         RequestContext.getCurrentInstance().update("form:datosEvalDimension");
         log.info("Activar");
         bandera = 1;
      } else if (bandera == 1) {
         tamano = 270;
         log.info("Desactivar");
         codigo = (Column) c.getViewRoot().findComponent("form:datosEvalDimension:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosEvalDimension:descripcion");
         descripcion.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosEvalDimension");
         bandera = 0;
         filtrarEvalDimensiones = null;
         tipoLista = 0;
      }
   }

   public void modificarEvalDimensiones(int indice, String confirmarCambio, String valorConfirmar) {
      log.error("ENTRE A MODIFICAR EVALDIMENSIONES");
      int contador = 0;
      boolean banderita = false;
      boolean banderita1 = false;

      RequestContext context = RequestContext.getCurrentInstance();
      log.error("TIPO LISTA = " + tipoLista);
      if (confirmarCambio.equalsIgnoreCase("N")) {
         log.error("ENTRE A MODIFICAR EMPRESAS, CONFIRMAR CAMBIO ES N");
         if (tipoLista == 0) {
            if (!crearEvalDimensiones.contains(listEvalDimensiones.get(indice))) {

               log.info("backupCodigo : " + backupCodigo);
               log.info("backupDescripcion : " + backupDescripcion);

               if (listEvalDimensiones.get(indice).getCodigo() == null) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  banderita = false;
                  listEvalDimensiones.get(indice).setCodigo(backupCodigo);
               } else {
                  for (int j = 0; j < listEvalDimensiones.size(); j++) {
                     if (j != indice) {
                        if (listEvalDimensiones.get(indice).getCodigo() == listEvalDimensiones.get(j).getCodigo()) {
                           contador++;
                        }
                     }
                  }

                  if (contador > 0) {
                     banderita = false;
                     mensajeValidacion = "CODIGOS REPETIDOS";
                     banderita = false;
                     listEvalDimensiones.get(indice).setCodigo(backupCodigo);
                  } else {
                     banderita = true;
                  }

               }
               if (listEvalDimensiones.get(indice).getDescripcion().isEmpty()) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  banderita1 = false;
                  listEvalDimensiones.get(indice).setDescripcion(backupDescripcion);
               } else if (listEvalDimensiones.get(indice).getDescripcion().equals(" ")) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  banderita1 = false;
                  listEvalDimensiones.get(indice).setDescripcion(backupDescripcion);

               } else {
                  banderita1 = true;
               }

               if (banderita == true && banderita1 == true) {
                  if (modificarEvalDimensiones.isEmpty()) {
                     modificarEvalDimensiones.add(listEvalDimensiones.get(indice));
                  } else if (!modificarEvalDimensiones.contains(listEvalDimensiones.get(indice))) {
                     modificarEvalDimensiones.add(listEvalDimensiones.get(indice));
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
               RequestContext.getCurrentInstance().update("form:datosEvalDimension");
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            } else {

               log.info("backupCodigo : " + backupCodigo);
               log.info("backupDescripcion : " + backupDescripcion);

               if (listEvalDimensiones.get(indice).getCodigo() == null) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  banderita = false;
                  listEvalDimensiones.get(indice).setCodigo(backupCodigo);
               } else {
                  for (int j = 0; j < listEvalDimensiones.size(); j++) {
                     if (j != indice) {
                        if (listEvalDimensiones.get(indice).getCodigo() == listEvalDimensiones.get(j).getCodigo()) {
                           contador++;
                        }
                     }
                  }

                  if (contador > 0) {
                     mensajeValidacion = "CODIGOS REPETIDOS";
                     banderita = false;
                     listEvalDimensiones.get(indice).setCodigo(backupCodigo);
                  } else {
                     banderita = true;
                  }

               }
               if (listEvalDimensiones.get(indice).getDescripcion().isEmpty()) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  banderita1 = false;
                  listEvalDimensiones.get(indice).setDescripcion(backupDescripcion);
               } else if (listEvalDimensiones.get(indice).getDescripcion().equals(" ")) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  banderita1 = false;
                  listEvalDimensiones.get(indice).setDescripcion(backupDescripcion);

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
               RequestContext.getCurrentInstance().update("form:datosEvalDimension");
               RequestContext.getCurrentInstance().update("form:ACEPTAR");

            }
         } else if (!crearEvalDimensiones.contains(filtrarEvalDimensiones.get(indice))) {
            if (filtrarEvalDimensiones.get(indice).getCodigo() == null) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               banderita = false;
               filtrarEvalDimensiones.get(indice).setCodigo(backupCodigo);
            } else {
               for (int j = 0; j < filtrarEvalDimensiones.size(); j++) {
                  if (j != indice) {
                     if (filtrarEvalDimensiones.get(indice).getCodigo() == listEvalDimensiones.get(j).getCodigo()) {
                        contador++;
                     }
                  }
               }
               for (int j = 0; j < listEvalDimensiones.size(); j++) {
                  if (j != indice) {
                     if (filtrarEvalDimensiones.get(indice).getCodigo() == listEvalDimensiones.get(j).getCodigo()) {
                        contador++;
                     }
                  }
               }
               if (contador > 0) {
                  mensajeValidacion = "CODIGOS REPETIDOS";
                  banderita = false;
                  filtrarEvalDimensiones.get(indice).setCodigo(backupCodigo);

               } else {
                  banderita = true;
               }

            }

            if (filtrarEvalDimensiones.get(indice).getDescripcion().isEmpty()) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               banderita1 = false;
               filtrarEvalDimensiones.get(indice).setDescripcion(backupDescripcion);
            }
            if (filtrarEvalDimensiones.get(indice).getDescripcion().equals(" ")) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               banderita1 = false;
               filtrarEvalDimensiones.get(indice).setDescripcion(backupDescripcion);
            }

            if (banderita == true && banderita1 == true) {
               if (modificarEvalDimensiones.isEmpty()) {
                  modificarEvalDimensiones.add(filtrarEvalDimensiones.get(indice));
               } else if (!modificarEvalDimensiones.contains(filtrarEvalDimensiones.get(indice))) {
                  modificarEvalDimensiones.add(filtrarEvalDimensiones.get(indice));
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
            if (filtrarEvalDimensiones.get(indice).getCodigo() == null) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               banderita = false;
               filtrarEvalDimensiones.get(indice).setCodigo(backupCodigo);
            } else {
               for (int j = 0; j < filtrarEvalDimensiones.size(); j++) {
                  if (j != indice) {
                     if (filtrarEvalDimensiones.get(indice).getCodigo() == listEvalDimensiones.get(j).getCodigo()) {
                        contador++;
                     }
                  }
               }
               for (int j = 0; j < listEvalDimensiones.size(); j++) {
                  if (j != indice) {
                     if (filtrarEvalDimensiones.get(indice).getCodigo() == listEvalDimensiones.get(j).getCodigo()) {
                        contador++;
                     }
                  }
               }
               if (contador > 0) {
                  mensajeValidacion = "CODIGOS REPETIDOS";
                  banderita = false;
                  filtrarEvalDimensiones.get(indice).setCodigo(backupCodigo);

               } else {
                  banderita = true;
               }

            }

            if (filtrarEvalDimensiones.get(indice).getDescripcion().isEmpty()) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               banderita1 = false;
               filtrarEvalDimensiones.get(indice).setDescripcion(backupDescripcion);
            }
            if (filtrarEvalDimensiones.get(indice).getDescripcion().equals(" ")) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               banderita1 = false;
               filtrarEvalDimensiones.get(indice).setDescripcion(backupDescripcion);
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
         RequestContext.getCurrentInstance().update("form:datosEvalDimension");
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      }

   }

   private BigInteger verificarTablasAuxilios;

   public void verificarBorrado() {
      try {
         log.info("ESTOY EN VERIFICAR BORRADO tipoLista " + tipoLista);
         log.info("secuencia borrado : " + listEvalDimensiones.get(index).getSecuencia());
         if (tipoLista == 0) {
            log.info("secuencia borrado : " + listEvalDimensiones.get(index).getSecuencia());
            verificarTablasAuxilios = administrarEvalDimensiones.verificarEvalPlanillas(listEvalDimensiones.get(index).getSecuencia());
         } else {
            log.info("secuencia borrado : " + filtrarEvalDimensiones.get(index).getSecuencia());
            verificarTablasAuxilios = administrarEvalDimensiones.verificarEvalPlanillas(filtrarEvalDimensiones.get(index).getSecuencia());
         }
         if (!verificarTablasAuxilios.equals(new BigInteger("0"))) {
            log.info("Borrado>0");

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:validacionBorrar");
            RequestContext.getCurrentInstance().execute("PF('validacionBorrar').show()");
            index = -1;

            verificarTablasAuxilios = new BigInteger("-1");

         } else {
            log.info("Borrado==0");
            borrandoEvalDimensiones();
         }
      } catch (Exception e) {
         log.error("ERROR ControlTiposCertificados verificarBorrado ERROR " + e);
      }
   }

   public void borrandoEvalDimensiones() {

      if (index >= 0) {

         if (tipoLista == 0) {
            log.info("Entro a borrardatosEvalDimensiones");
            if (!modificarEvalDimensiones.isEmpty() && modificarEvalDimensiones.contains(listEvalDimensiones.get(index))) {
               int modIndex = modificarEvalDimensiones.indexOf(listEvalDimensiones.get(index));
               modificarEvalDimensiones.remove(modIndex);
               borrarEvalDimensiones.add(listEvalDimensiones.get(index));
            } else if (!crearEvalDimensiones.isEmpty() && crearEvalDimensiones.contains(listEvalDimensiones.get(index))) {
               int crearIndex = crearEvalDimensiones.indexOf(listEvalDimensiones.get(index));
               crearEvalDimensiones.remove(crearIndex);
            } else {
               borrarEvalDimensiones.add(listEvalDimensiones.get(index));
            }
            listEvalDimensiones.remove(index);
         }
         if (tipoLista == 1) {
            log.info("borrardatosEvalDimensiones ");
            if (!modificarEvalDimensiones.isEmpty() && modificarEvalDimensiones.contains(filtrarEvalDimensiones.get(index))) {
               int modIndex = modificarEvalDimensiones.indexOf(filtrarEvalDimensiones.get(index));
               modificarEvalDimensiones.remove(modIndex);
               borrarEvalDimensiones.add(filtrarEvalDimensiones.get(index));
            } else if (!crearEvalDimensiones.isEmpty() && crearEvalDimensiones.contains(filtrarEvalDimensiones.get(index))) {
               int crearIndex = crearEvalDimensiones.indexOf(filtrarEvalDimensiones.get(index));
               crearEvalDimensiones.remove(crearIndex);
            } else {
               borrarEvalDimensiones.add(filtrarEvalDimensiones.get(index));
            }
            int VCIndex = listEvalDimensiones.indexOf(filtrarEvalDimensiones.get(index));
            listEvalDimensiones.remove(VCIndex);
            filtrarEvalDimensiones.remove(index);

         }
         RequestContext context = RequestContext.getCurrentInstance();
         infoRegistro = "Cantidad de registros: " + listEvalDimensiones.size();
         RequestContext.getCurrentInstance().update("form:informacionRegistro");
         RequestContext.getCurrentInstance().update("form:datosEvalDimension");
         index = -1;
         secRegistro = null;

         if (guardado == true) {
            guardado = false;
         }
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      }

   }

   /*public void verificarBorrado() {
     log.info("Estoy en verificarBorrado");
     try {
     borradoVC = administrarMotivosCambiosCargos.verificarBorradoVC(listMotivosLocalizaciones.get(index).getSecuencia());
     if (borradoVC.intValue() == 0) {
     log.info("Borrado==0");
     borrarMotivosCambiosCargos();
     }
     if (borradoVC.intValue() != 0) {
     log.info("Borrado>0");

     RequestContext context = RequestContext.getCurrentInstance();
     RequestContext.getCurrentInstance().update("form:validacionBorrar");
     RequestContext.getCurrentInstance().execute("PF('validacionBorrar').show()");
     index = -1;
     borradoVC = new Long(-1);
     }

     } catch (Exception e) {
     log.error("ERROR ControlTiposEntidades verificarBorrado ERROR " + e);
     }
     }*/
   public void guardarEvalDimensiones() {
      RequestContext context = RequestContext.getCurrentInstance();

      if (guardado == false) {
         log.info("Realizando Operaciones EvalDimensiones");
         if (!borrarEvalDimensiones.isEmpty()) {
            administrarEvalDimensiones.borrarEvalDimensiones(borrarEvalDimensiones);
            //mostrarBorrados
            registrosBorrados = borrarEvalDimensiones.size();
            RequestContext.getCurrentInstance().update("form:mostrarBorrados");
            RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
            borrarEvalDimensiones.clear();
         }
         if (!crearEvalDimensiones.isEmpty()) {
            administrarEvalDimensiones.crearEvalDimensiones(crearEvalDimensiones);
            crearEvalDimensiones.clear();
         }
         if (!modificarEvalDimensiones.isEmpty()) {
            administrarEvalDimensiones.modificarEvalDimensiones(modificarEvalDimensiones);
            modificarEvalDimensiones.clear();
         }
         log.info("Se guardaron los datos con exito");
         listEvalDimensiones = null;
         FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
         RequestContext.getCurrentInstance().update("form:datosEvalDimension");
         k = 0;
         guardado = true;
      }
      index = -1;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");

   }

   public void editarCelda() {
      if (index >= 0) {
         if (tipoLista == 0) {
            editarEvalDimension = listEvalDimensiones.get(index);
         }
         if (tipoLista == 1) {
            editarEvalDimension = filtrarEvalDimensiones.get(index);
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

   public void agregarNuevoEvalDimension() {
      log.info("Agregar EvalDimensiones");
      int contador = 0;
      int duplicados = 0;

      Integer a = 0;
      a = null;
      mensajeValidacion = " ";
      RequestContext context = RequestContext.getCurrentInstance();
      if (nuevoEvalDimension.getCodigo() == a) {
         mensajeValidacion = " *Codigo \n";
         log.info("Mensaje validacion : " + mensajeValidacion);
      } else {
         log.info("codigo en Motivo Cambio Cargo: " + nuevoEvalDimension.getCodigo());

         for (int x = 0; x < listEvalDimensiones.size(); x++) {
            if (listEvalDimensiones.get(x).getCodigo().equals(nuevoEvalDimension.getCodigo())) {
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
      if (nuevoEvalDimension.getDescripcion() == (null)) {
         mensajeValidacion = mensajeValidacion + " *Descripcion \n";
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
            codigo = (Column) c.getViewRoot().findComponent("form:datosEvalDimension:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosEvalDimension:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosEvalDimension");
            bandera = 0;
            filtrarEvalDimensiones = null;
            tipoLista = 0;
         }
         log.info("Despues de la bandera");

         //AGREGAR REGISTRO A LA LISTA VIGENCIAS CARGOS EMPLEADO.
         k++;
         l = BigInteger.valueOf(k);
         nuevoEvalDimension.setSecuencia(l);

         crearEvalDimensiones.add(nuevoEvalDimension);

         listEvalDimensiones.add(nuevoEvalDimension);
         nuevoEvalDimension = new EvalDimensiones();

         RequestContext.getCurrentInstance().update("form:datosEvalDimension");
         infoRegistro = "Cantidad de registros: " + listEvalDimensiones.size();
         RequestContext.getCurrentInstance().update("form:informacionRegistro");
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         log.info("Despues de la bandera guardado");

         RequestContext.getCurrentInstance().execute("PF('nuevoRegistroEvalDimensiones').hide()");
         index = -1;
         secRegistro = null;
         log.info("Despues de nuevoRegistroEvalDimensiones");

      } else {
         RequestContext.getCurrentInstance().update("form:validacionNuevaCentroCosto");
         RequestContext.getCurrentInstance().execute("PF('validacionNuevaCentroCosto').show()");
         contador = 0;
      }
   }

   public void limpiarNuevoEvalDimension() {
      log.info("limpiarnuevoEvalDimensiones");
      nuevoEvalDimension = new EvalDimensiones();
      secRegistro = null;
      index = -1;

   }

   //------------------------------------------------------------------------------
   public void duplicarEvalDimensiones() {
      log.info("duplicarEvalDimensiones");
      if (index >= 0) {
         duplicarEvalDimension = new EvalDimensiones();
         k++;
         l = BigInteger.valueOf(k);

         if (tipoLista == 0) {
            duplicarEvalDimension.setSecuencia(l);
            duplicarEvalDimension.setCodigo(listEvalDimensiones.get(index).getCodigo());
            duplicarEvalDimension.setDescripcion(listEvalDimensiones.get(index).getDescripcion());
         }
         if (tipoLista == 1) {
            duplicarEvalDimension.setSecuencia(l);
            duplicarEvalDimension.setCodigo(filtrarEvalDimensiones.get(index).getCodigo());
            duplicarEvalDimension.setDescripcion(filtrarEvalDimensiones.get(index).getDescripcion());
         }

         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarEvalsDimensiones");
         RequestContext.getCurrentInstance().execute("PF('duplicarRegistroEvalDimensiones').show()");
         index = -1;
         secRegistro = null;
      }
   }

   public void confirmarDuplicar() {
      log.error("ESTOY EN CONFIRMAR DUPLICAR EvalDimensiones");
      int contador = 0;
      mensajeValidacion = " ";
      int duplicados = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      Integer a = 0;
      a = null;
      log.error("ConfirmarDuplicar codigo " + duplicarEvalDimension.getCodigo());
      log.error("ConfirmarDuplicar descripcion " + duplicarEvalDimension.getDescripcion());

      if (duplicarEvalDimension.getCodigo() == a) {
         mensajeValidacion = mensajeValidacion + "   *Codigo \n";
         log.info("Mensaje validacion : " + mensajeValidacion);
      } else {
         for (int x = 0; x < listEvalDimensiones.size(); x++) {
            if (listEvalDimensiones.get(x).getCodigo().equals(duplicarEvalDimension.getCodigo())) {
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
      if (duplicarEvalDimension.getDescripcion().isEmpty()) {
         mensajeValidacion = mensajeValidacion + "   *Descripcion \n";
         log.info("Mensaje validacion : " + mensajeValidacion);

      } else {
         log.info("Bandera : ");
         contador++;
      }

      if (contador == 2) {

         log.info("Datos Duplicando: " + duplicarEvalDimension.getSecuencia() + "  " + duplicarEvalDimension.getCodigo());
         if (crearEvalDimensiones.contains(duplicarEvalDimension)) {
            log.info("Ya lo contengo.");
         }
         listEvalDimensiones.add(duplicarEvalDimension);
         crearEvalDimensiones.add(duplicarEvalDimension);
         RequestContext.getCurrentInstance().update("form:datosEvalDimension");
         infoRegistro = "Cantidad de registros: " + listEvalDimensiones.size();
         RequestContext.getCurrentInstance().update("form:informacionRegistro");
         index = -1;
         secRegistro = null;
         if (guardado == true) {
            guardado = false;
         }
         if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            //CERRAR FILTRADO
            codigo = (Column) c.getViewRoot().findComponent("form:datosEvalDimension:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosEvalDimension:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosEvalDimension");
            bandera = 0;
            filtrarEvalDimensiones = null;
            tipoLista = 0;
         }
         duplicarEvalDimension = new EvalDimensiones();
         RequestContext.getCurrentInstance().execute("PF('duplicarRegistroEvalDimensiones').hide()");

         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      } else {
         contador = 0;
         RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
         RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
      }
   }

   public void limpiarduplicarEvalDimensiones() {
      duplicarEvalDimension = new EvalDimensiones();
   }

   public void exportPDF() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosEvalDimensionExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "Dimensiones", false, false, "UTF-8", null, null);
      context.responseComplete();
      index = -1;
      secRegistro = null;
   }

   public void exportXLS() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosEvalDimensionExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "Dimensiones", false, false, "UTF-8", null, null);
      context.responseComplete();
      index = -1;
      secRegistro = null;
   }

   public void verificarRastro() {
      RequestContext context = RequestContext.getCurrentInstance();
      log.info("lol");
      if (!listEvalDimensiones.isEmpty()) {
         if (secRegistro != null) {
            log.info("lol 2");
            int resultado = administrarRastros.obtenerTabla(secRegistro, "EVALDIMENSIONES"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
      } else if (administrarRastros.verificarHistoricosTabla("EVALDIMENSIONES")) { // igual acá
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
      index = -1;
   }
   //------*************/////////////////*****************----------*/*/*/-*/-*

   public List<EvalDimensiones> getListEvalDimensiones() {
      if (listEvalDimensiones == null) {
         listEvalDimensiones = administrarEvalDimensiones.consultarEvalDimensiones();
      }
      RequestContext context = RequestContext.getCurrentInstance();
      if (listEvalDimensiones == null || listEvalDimensiones.isEmpty()) {
         infoRegistro = "Cantidad de registros: 0 ";
      } else {
         infoRegistro = "Cantidad de registros: " + listEvalDimensiones.size();
      }
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
      return listEvalDimensiones;
   }

   public void setListEvalDimensiones(List<EvalDimensiones> listEvalDimensiones) {
      this.listEvalDimensiones = listEvalDimensiones;
   }

   public List<EvalDimensiones> getFiltrarEvalDimensiones() {
      return filtrarEvalDimensiones;
   }

   public void setFiltrarEvalDimensiones(List<EvalDimensiones> filtrarEvalDimensiones) {
      this.filtrarEvalDimensiones = filtrarEvalDimensiones;
   }

   public EvalDimensiones getNuevoEvalDimension() {
      return nuevoEvalDimension;
   }

   public void setNuevoEvalDimension(EvalDimensiones nuevoEvalDimension) {
      this.nuevoEvalDimension = nuevoEvalDimension;
   }

   public EvalDimensiones getDuplicarEvalDimension() {
      return duplicarEvalDimension;
   }

   public void setDuplicarEvalDimension(EvalDimensiones duplicarEvalDimension) {
      this.duplicarEvalDimension = duplicarEvalDimension;
   }

   public EvalDimensiones getEditarEvalDimension() {
      return editarEvalDimension;
   }

   public void setEditarEvalDimension(EvalDimensiones editarEvalDimension) {
      this.editarEvalDimension = editarEvalDimension;
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

   public BigInteger getSecRegistro() {
      return secRegistro;
   }

   public void setSecRegistro(BigInteger secRegistro) {
      this.secRegistro = secRegistro;
   }

   public boolean isGuardado() {
      return guardado;
   }

   public void setGuardado(boolean guardado) {
      this.guardado = guardado;
   }

   public EvalDimensiones getEvalDimensionSeleccionada() {
      return evalDimensionSeleccionada;
   }

   public void setEvalDimensionSeleccionada(EvalDimensiones evalDimensionSeleccionada) {
      this.evalDimensionSeleccionada = evalDimensionSeleccionada;
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
