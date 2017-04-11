/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Clasesausentismos;
import Entidades.Tiposausentismos;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarClasesAusentismosInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import ControlNavegacion.ControlListaNavegacion;
import java.util.Map;
import java.util.LinkedHashMap;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
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
public class ControlClasesAusentismos implements Serializable {

   @EJB
   AdministrarClasesAusentismosInterface administrarClasesAusentismos;
   @EJB
   AdministrarRastrosInterface administrarRastros;

   private List<Clasesausentismos> listClasesAusentismos;
   private List<Clasesausentismos> filtrarClasesAusentismos;
   private List<Clasesausentismos> crearClasesAusentismos;
   private List<Clasesausentismos> modificarClasesAusentismos;
   private List<Clasesausentismos> borrarClasesAusentismos;
   private Clasesausentismos nuevoClasesAusentismos;
   private Clasesausentismos duplicarClasesAusentismos;
   private Clasesausentismos editarClasesAusentismos;
   private Clasesausentismos clasesAusentismoSeleccionado;
   //otros
   private int cualCelda, tipoLista, tipoActualizacion, k, bandera;
   private BigInteger l;
   private boolean aceptar, guardado, activarBotonLov;
   //AutoCompletar
   private boolean permitirIndex;
   //RASTRO
   private Column codigo, descripcion, personafir;
   //borrado
   private int registrosBorrados;
   private String mensajeValidacion;
   //filtrado table
   private int tamano;
   private Integer backupCodigo;
   private String backupDescripcion;
   private String backupPais;
   //---------------------------------
   private String backupCiudad;
   private List<Tiposausentismos> listaTiposausentismos;
   private List<Tiposausentismos> filtradoTiposausentismos;
   private Tiposausentismos centrocostoSeleccionado;
   private String nuevoYduplicarCompletarPersona;
   //--------------------------------------
   private String backupTipo;
   private String infoRegistro, infoRegistroTiposAusentismos;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

   public ControlClasesAusentismos() {
      listClasesAusentismos = null;
      crearClasesAusentismos = new ArrayList<Clasesausentismos>();
      modificarClasesAusentismos = new ArrayList<Clasesausentismos>();
      borrarClasesAusentismos = new ArrayList<Clasesausentismos>();
      permitirIndex = true;
      editarClasesAusentismos = new Clasesausentismos();
      nuevoClasesAusentismos = new Clasesausentismos();
      nuevoClasesAusentismos.setTipo(new Tiposausentismos());
      duplicarClasesAusentismos = new Clasesausentismos();
      duplicarClasesAusentismos.setTipo(new Tiposausentismos());
      listaTiposausentismos = null;
      filtradoTiposausentismos = null;
      guardado = true;
      aceptar = true;
      tamano = 270;
      activarBotonLov = true;
      mapParametros.put("paginaAnterior", paginaAnterior);
   }

   public void recibirPaginaEntrante(String pagina) {
      paginaAnterior = pagina;
      getListClasesAusentismos();
      listaTiposausentismos = null;
      getListaTiposausentismos();
      if (listClasesAusentismos != null) {
         if (!listClasesAusentismos.isEmpty()) {
            clasesAusentismoSeleccionado = listClasesAusentismos.get(0);
         }
      }
   }

   public void recibirParametros(Map<String, Object> map) {
      mapParametros = map;
      paginaAnterior = (String) mapParametros.get("paginaAnterior");
      getListClasesAusentismos();
      listaTiposausentismos = null;
      getListaTiposausentismos();
      if (listClasesAusentismos != null) {
         if (!listClasesAusentismos.isEmpty()) {
            clasesAusentismoSeleccionado = listClasesAusentismos.get(0);
         }
      }
   }

   //Reemplazar la funcion volverAtras, retornarPagina, Redirigir.....Atras.etc
   public void navegar(String pag) {
      FacesContext fc = FacesContext.getCurrentInstance();
      ControlListaNavegacion controlListaNavegacion = (ControlListaNavegacion) fc.getApplication().evaluateExpressionGet(fc, "#{controlListaNavegacion}", ControlListaNavegacion.class);
      if (pag.equals("atras")) {
         pag = paginaAnterior;
         paginaAnterior = "nominaf";
         controlListaNavegacion.quitarPagina();
         System.out.println("navegar('Atras') : " + pag);
      } else {
         String pagActual = "claseausentismo";
         //Map<String, Object> mapParaEnviar = new LinkedHashMap<String, Object>();
         //mapParametros.put("paginaAnterior", pagActual);
         //mas Parametros
//         if (pag.equals("rastrotabla")) {
//           ControlRastro controlRastro = (ControlRastro) fc.getApplication().evaluateExpressionGet(fc, "#{controlRastro}", ControlRastro.class);
         //           controlRastro.recibirDatosTabla(conceptoSeleccionado.getSecuencia(), "Conceptos", pagActual);
         //      } else if (pag.equals("rastrotablaH")) {
         //       ControlRastro controlRastro = (ControlRastro) fc.getApplication().evaluateExpressionGet(fc, "#{controlRastro}", ControlRastro.class);
         //     controlRastro.historicosTabla("Conceptos", pagActual);
         //   pag = "rastrotabla";
         //}
         controlListaNavegacion.guardarNavegacion(pagActual, pag);
      }
      limpiarListasValor();fc.getApplication().getNavigationHandler().handleNavigation(fc, null, pag);
   }

  public void limpiarListasValor() {

   }

   @PostConstruct
   public void inicializarAdministrador() {
      try {
         FacesContext x = FacesContext.getCurrentInstance();
         HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
         administrarClasesAusentismos.obtenerConexion(ses.getId());
         administrarRastros.obtenerConexion(ses.getId());
      } catch (Exception e) {
         System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
         System.out.println("Causa: " + e.getCause());
      }
   }

   public String redirigir() {
      return paginaAnterior;
   }

   public void cambiarIndice(Clasesausentismos clase, int celda) {
      System.err.println("TIPO LISTA = " + tipoLista);

      if (permitirIndex == true) {
         clasesAusentismoSeleccionado = clase;
         cualCelda = celda;
         clasesAusentismoSeleccionado.getSecuencia();
         if (tipoLista == 0) {
            deshabilitarBotonLov();
            backupCodigo = clasesAusentismoSeleccionado.getCodigo();
            backupDescripcion = clasesAusentismoSeleccionado.getDescripcion();
            if (cualCelda == 2) {
               habilitarBotonLov();
               backupTipo = clasesAusentismoSeleccionado.getTipo().getDescripcion();
            } else {
               deshabilitarBotonLov();
            }

         } else if (tipoLista == 1) {
            backupCodigo = clasesAusentismoSeleccionado.getCodigo();
            backupDescripcion = clasesAusentismoSeleccionado.getDescripcion();
            if (cualCelda == 2) {
               habilitarBotonLov();
               backupTipo = clasesAusentismoSeleccionado.getTipo().getDescripcion();
            } else {
               deshabilitarBotonLov();
            }

         }

      }
   }

   public void asignarIndex(Clasesausentismos clase, int LND, int dig) {
      try {
         RequestContext context = RequestContext.getCurrentInstance();
         System.out.println("\n ENTRE A ControlClasesAusentismos.asignarIndex \n");
         clasesAusentismoSeleccionado = clase;
         tipoActualizacion = LND;
         if (dig == 2) {
            contarRegistroTiposAusentismos();
            RequestContext.getCurrentInstance().update("form:tiposAusentismosDialogo");
            RequestContext.getCurrentInstance().execute("PF('tiposAusentismosDialogo').show()");
         }
      } catch (Exception e) {
         System.out.println("ERROR ControlClasesAusentismos.asignarIndex ERROR======" + e.getMessage());
      }
   }

   public void activarAceptar() {
      aceptar = false;
   }

   public void listaValoresBoton() {
      if (clasesAusentismoSeleccionado != null) {

         if (cualCelda == 2) {
            RequestContext context = RequestContext.getCurrentInstance();
            habilitarBotonLov();
            RequestContext.getCurrentInstance().update("form:tiposAusentismosDialogo");
            RequestContext.getCurrentInstance().execute("PF('tiposAusentismosDialogo').show()");
         }
      }
   }

   public void cancelarModificacion() {
      if (bandera == 1) {
         FacesContext c = FacesContext.getCurrentInstance();
         //CERRAR FILTRADO
         codigo = (Column) c.getViewRoot().findComponent("form:datosClasesAusentismos:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosClasesAusentismos:descripcion");
         descripcion.setFilterStyle("display: none; visibility: hidden;");
         personafir = (Column) c.getViewRoot().findComponent("form:datosClasesAusentismos:personafir");
         personafir.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosClasesAusentismos");
         bandera = 0;
         filtrarClasesAusentismos = null;
         tipoLista = 0;
      }

      borrarClasesAusentismos.clear();
      crearClasesAusentismos.clear();
      modificarClasesAusentismos.clear();
      clasesAusentismoSeleccionado = null;
      k = 0;
      listClasesAusentismos = null;
      guardado = true;
      permitirIndex = true;
      getListClasesAusentismos();
      RequestContext context = RequestContext.getCurrentInstance();
      contarRegistros();
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
      RequestContext.getCurrentInstance().update("form:datosClasesAusentismos");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void salir() {  limpiarListasValor();
      if (bandera == 1) {
         FacesContext c = FacesContext.getCurrentInstance();
         //CERRAR FILTRADO
         codigo = (Column) c.getViewRoot().findComponent("form:datosClasesAusentismos:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosClasesAusentismos:descripcion");
         descripcion.setFilterStyle("display: none; visibility: hidden;");
         personafir = (Column) c.getViewRoot().findComponent("form:datosClasesAusentismos:personafir");
         personafir.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosClasesAusentismos");
         bandera = 0;
         filtrarClasesAusentismos = null;
         tipoLista = 0;
      }

      borrarClasesAusentismos.clear();
      crearClasesAusentismos.clear();
      modificarClasesAusentismos.clear();
      clasesAusentismoSeleccionado = null;
      k = 0;
      listClasesAusentismos = null;
      guardado = true;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:datosClasesAusentismos");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void activarCtrlF11() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 0) {
         tamano = 250;
         codigo = (Column) c.getViewRoot().findComponent("form:datosClasesAusentismos:codigo");
         codigo.setFilterStyle("width:85% !important");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosClasesAusentismos:descripcion");
         descripcion.setFilterStyle("width:85% !important");
         personafir = (Column) c.getViewRoot().findComponent("form:datosClasesAusentismos:personafir");
         personafir.setFilterStyle("width:85% !important");
         RequestContext.getCurrentInstance().update("form:datosClasesAusentismos");
         System.out.println("Activar");
         bandera = 1;
      } else if (bandera == 1) {
         System.out.println("Desactivar");
         tamano = 270;
         codigo = (Column) c.getViewRoot().findComponent("form:datosClasesAusentismos:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosClasesAusentismos:descripcion");
         descripcion.setFilterStyle("display: none; visibility: hidden;");
         personafir = (Column) c.getViewRoot().findComponent("form:datosClasesAusentismos:personafir");
         personafir.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosClasesAusentismos");
         bandera = 0;
         filtrarClasesAusentismos = null;
         tipoLista = 0;
      }
   }

   public void actualizarTiposausentismos() {
      RequestContext context = RequestContext.getCurrentInstance();
      System.out.println("centrocosto seleccionado : " + centrocostoSeleccionado.getDescripcion());
      System.out.println("tipo Actualizacion : " + tipoActualizacion);
      System.out.println("tipo Lista : " + tipoLista);

      if (tipoActualizacion == 0) {
         if (tipoLista == 0) {
            clasesAusentismoSeleccionado.setTipo(centrocostoSeleccionado);

            if (!crearClasesAusentismos.contains(clasesAusentismoSeleccionado)) {
               if (modificarClasesAusentismos.isEmpty()) {
                  modificarClasesAusentismos.add(clasesAusentismoSeleccionado);
               } else if (!modificarClasesAusentismos.contains(clasesAusentismoSeleccionado)) {
                  modificarClasesAusentismos.add(clasesAusentismoSeleccionado);
               }
            }
         } else {
            clasesAusentismoSeleccionado.setTipo(centrocostoSeleccionado);

            if (!crearClasesAusentismos.contains(clasesAusentismoSeleccionado)) {
               if (modificarClasesAusentismos.isEmpty()) {
                  modificarClasesAusentismos.add(clasesAusentismoSeleccionado);
               } else if (!modificarClasesAusentismos.contains(clasesAusentismoSeleccionado)) {
                  modificarClasesAusentismos.add(clasesAusentismoSeleccionado);
               }
            }
         }
         if (guardado == true) {
            guardado = false;
         }
         permitirIndex = true;
         System.out.println("ACTUALIZAR PAIS PAIS SELECCIONADO : " + centrocostoSeleccionado.getDescripcion());
         RequestContext.getCurrentInstance().update("form:datosClasesAusentismos");
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      } else if (tipoActualizacion == 1) {
         System.out.println("ACTUALIZAR PAIS NUEVO DEPARTAMENTO: " + centrocostoSeleccionado.getDescripcion());
         nuevoClasesAusentismos.setTipo(centrocostoSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevoPersona");
      } else if (tipoActualizacion == 2) {
         System.out.println("ACTUALIZAR PAIS DUPLICAR DEPARTAMENO: " + centrocostoSeleccionado.getDescripcion());
         duplicarClasesAusentismos.setTipo(centrocostoSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarPersona");
      }
      filtradoTiposausentismos = null;
      centrocostoSeleccionado = null;
      aceptar = true;
      clasesAusentismoSeleccionado = null;
      clasesAusentismoSeleccionado = null;
      tipoActualizacion = -1;
      cualCelda = -1;
      context.reset("form:lovTiposausentismos:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovTiposausentismos').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('tiposAusentismosDialogo').hide()");
      //RequestContext.getCurrentInstance().update("form:lovTiposausentismos");
   }

   public void cancelarCambioTiposausentismos() {
      if (clasesAusentismoSeleccionado != null) {
         clasesAusentismoSeleccionado.getTipo().setDescripcion(backupTipo);
      }
      clasesAusentismoSeleccionado = null;
      filtradoTiposausentismos = null;
      centrocostoSeleccionado = null;
      clasesAusentismoSeleccionado = null;
      tipoActualizacion = -1;
      permitirIndex = true;
      aceptar = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("form:lovTiposausentismos:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovTiposausentismos').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('form:tiposAusentismosDialogo').hide()");
   }

   public void modificarClasesAusentismos(Clasesausentismos clase, String confirmarCambio, String valorConfirmar) {
      System.err.println("ENTRE A MODIFICAR SUB CATEGORIA");
      clasesAusentismoSeleccionado = clase;
      int coincidencias = 0;
      int contador = 0;
      boolean banderita = false;
      boolean banderita1 = false;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      if (confirmarCambio.equalsIgnoreCase("N")) {
         if (tipoLista == 0) {
            if (!crearClasesAusentismos.contains(clasesAusentismoSeleccionado)) {

               if (clasesAusentismoSeleccionado.getCodigo() == null) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  banderita = false;
                  clasesAusentismoSeleccionado.setCodigo(backupCodigo);

               } else //                        for (int j = 0; j < listClasesAusentismos.size(); j++) {
               //                            if (clasesAusentismoSeleccionado.getCodigo() == listClasesAusentismos.get(j).getCodigo()) {
               //                                contador++;
               //                            }
               //                        }
                if (contador > 0) {
                     mensajeValidacion = "CODIGOS REPETIDOS";
                     banderita = false;
                     clasesAusentismoSeleccionado.setCodigo(backupCodigo);
                  } else {
                     banderita = true;
                  }
               if (clasesAusentismoSeleccionado.getDescripcion().isEmpty()) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  banderita1 = false;
                  clasesAusentismoSeleccionado.setDescripcion(backupDescripcion);
               } else if (clasesAusentismoSeleccionado.getDescripcion().equals(" ")) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  banderita1 = false;
                  clasesAusentismoSeleccionado.setDescripcion(backupDescripcion);

               } else {
                  banderita1 = true;
               }

               if (banderita == true && banderita1 == true) {
                  if (modificarClasesAusentismos.isEmpty()) {
                     modificarClasesAusentismos.add(clasesAusentismoSeleccionado);
                  } else if (!modificarClasesAusentismos.contains(clasesAusentismoSeleccionado)) {
                     modificarClasesAusentismos.add(clasesAusentismoSeleccionado);
                  }
                  if (guardado == true) {
                     guardado = false;
                  }

               } else {
                  RequestContext.getCurrentInstance().update("form:validacionModificar");
                  RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");

               }
               clasesAusentismoSeleccionado = null;
               clasesAusentismoSeleccionado = null;
               RequestContext.getCurrentInstance().update("form:datosClasesAusentismos");
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            } else {

               if (clasesAusentismoSeleccionado.getCodigo() == null) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  banderita = false;
                  clasesAusentismoSeleccionado.setCodigo(backupCodigo);
               } else //                        for (int j = 0; j < listClasesAusentismos.size(); j++) {
               //                            if (clasesAusentismoSeleccionado.getCodigo() == listClasesAusentismos.get(j).getCodigo()) {
               //                                contador++;
               //                            }
               //                        }
                if (contador > 0) {
                     mensajeValidacion = "CODIGOS REPETIDOS";
                     banderita = false;
                     clasesAusentismoSeleccionado.setCodigo(backupCodigo);
                  } else {
                     banderita = true;
                  }
               if (clasesAusentismoSeleccionado.getDescripcion().isEmpty()) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  banderita1 = false;
                  clasesAusentismoSeleccionado.setDescripcion(backupDescripcion);
               } else if (clasesAusentismoSeleccionado.getDescripcion().equals(" ")) {
                  mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                  banderita1 = false;
                  clasesAusentismoSeleccionado.setDescripcion(backupDescripcion);

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
               clasesAusentismoSeleccionado = null;
               clasesAusentismoSeleccionado = null;
               RequestContext.getCurrentInstance().update("form:datosClasesAusentismos");
               RequestContext.getCurrentInstance().update("form:ACEPTAR");

            }
         } else if (!crearClasesAusentismos.contains(clasesAusentismoSeleccionado)) {
            if (clasesAusentismoSeleccionado.getCodigo() == null) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               banderita = false;
               clasesAusentismoSeleccionado.setCodigo(backupCodigo);
            } else //                        for (int j = 0; j < filtrarClasesAusentismos.size(); j++) {
            //                            if (clasesAusentismoSeleccionado.getCodigo() == listClasesAusentismos.get(j).getCodigo()) {
            //                                contador++;
            //                            }
            //                        }
             if (contador > 0) {
                  mensajeValidacion = "CODIGOS REPETIDOS";
                  banderita = false;
                  clasesAusentismoSeleccionado.setCodigo(backupCodigo);

               } else {
                  banderita = true;
               }

            if (clasesAusentismoSeleccionado.getDescripcion().isEmpty()) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               banderita1 = false;
               clasesAusentismoSeleccionado.setDescripcion(backupDescripcion);
            }
            if (clasesAusentismoSeleccionado.getDescripcion().equals(" ")) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               banderita1 = false;
               clasesAusentismoSeleccionado.setDescripcion(backupDescripcion);
            }

            if (banderita == true && banderita1 == true) {
               if (modificarClasesAusentismos.isEmpty()) {
                  modificarClasesAusentismos.add(clasesAusentismoSeleccionado);
               } else if (!modificarClasesAusentismos.contains(clasesAusentismoSeleccionado)) {
                  modificarClasesAusentismos.add(clasesAusentismoSeleccionado);
               }
               if (guardado == true) {
                  guardado = false;
               }

            } else {
               RequestContext.getCurrentInstance().update("form:validacionModificar");
               RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");
            }
            clasesAusentismoSeleccionado = null;
            clasesAusentismoSeleccionado = null;
         } else {
            if (clasesAusentismoSeleccionado.getCodigo() == null) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               banderita = false;
               clasesAusentismoSeleccionado.setCodigo(backupCodigo);
            } else {
               for (int j = 0; j < filtrarClasesAusentismos.size(); j++) {
                  if (clasesAusentismoSeleccionado.getCodigo() == listClasesAusentismos.get(j).getCodigo()) {
                     contador++;
                  }
               }
               for (int j = 0; j < listClasesAusentismos.size(); j++) {
                  if (clasesAusentismoSeleccionado.getCodigo() == listClasesAusentismos.get(j).getCodigo()) {
                     contador++;
                  }
               }
               if (contador > 0) {
                  mensajeValidacion = "CODIGOS REPETIDOS";
                  banderita = false;
                  clasesAusentismoSeleccionado.setCodigo(backupCodigo);

               } else {
                  banderita = true;
               }

            }

            if (clasesAusentismoSeleccionado.getDescripcion().isEmpty()) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               banderita1 = false;
               clasesAusentismoSeleccionado.setDescripcion(backupDescripcion);
            }
            if (clasesAusentismoSeleccionado.getDescripcion().equals(" ")) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               banderita1 = false;
               clasesAusentismoSeleccionado.setDescripcion(backupDescripcion);
            }

            if (banderita == true && banderita1 == true) {
               if (guardado == true) {
                  guardado = false;
               }

            } else {
               RequestContext.getCurrentInstance().update("form:validacionModificar");
               RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");
            }
            clasesAusentismoSeleccionado = null;
            clasesAusentismoSeleccionado = null;
         }
         RequestContext.getCurrentInstance().update("form:datosClasesAusentismos");
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      } else if (confirmarCambio.equalsIgnoreCase("PERSONAS")) {
         System.out.println("MODIFICANDO NORMA LABORAL : " + clasesAusentismoSeleccionado.getTipo().getDescripcion());
         if (!clasesAusentismoSeleccionado.getTipo().getDescripcion().equals("")) {
            if (tipoLista == 0) {
               clasesAusentismoSeleccionado.getTipo().setDescripcion(backupTipo);
            } else {
               clasesAusentismoSeleccionado.getTipo().setDescripcion(backupTipo);
            }

            for (int i = 0; i < listaTiposausentismos.size(); i++) {
               if (listaTiposausentismos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }

            if (coincidencias == 1) {
               if (tipoLista == 0) {
                  clasesAusentismoSeleccionado.setTipo(listaTiposausentismos.get(indiceUnicoElemento));
               } else {
                  clasesAusentismoSeleccionado.setTipo(listaTiposausentismos.get(indiceUnicoElemento));
               }
               listaTiposausentismos.clear();
               listaTiposausentismos = null;
               //getListaTiposFamiliares();

            } else {
               permitirIndex = false;
               RequestContext.getCurrentInstance().update("form:tiposAusentismosDialogo");
               RequestContext.getCurrentInstance().execute("PF('tiposAusentismosDialogo').show()");
               tipoActualizacion = 0;
            }
         } else {
            if (backupTipo != null) {
               if (tipoLista == 0) {
                  clasesAusentismoSeleccionado.getTipo().setDescripcion(backupTipo);
               } else {
                  clasesAusentismoSeleccionado.getTipo().setDescripcion(backupTipo);
               }
            }
            tipoActualizacion = 0;
            System.out.println("PAIS ANTES DE MOSTRAR DIALOGO PERSONA : " + backupTipo);
            RequestContext.getCurrentInstance().update("form:tiposAusentismosDialogo");
            RequestContext.getCurrentInstance().execute("PF('tiposAusentismosDialogo').show()");
         }

         if (coincidencias == 1) {
            if (tipoLista == 0) {
               if (!crearClasesAusentismos.contains(clasesAusentismoSeleccionado)) {

                  if (modificarClasesAusentismos.isEmpty()) {
                     modificarClasesAusentismos.add(clasesAusentismoSeleccionado);
                  } else if (!modificarClasesAusentismos.contains(clasesAusentismoSeleccionado)) {
                     modificarClasesAusentismos.add(clasesAusentismoSeleccionado);
                  }
                  if (guardado == true) {
                     guardado = false;
                  }
               }
               clasesAusentismoSeleccionado = null;
               clasesAusentismoSeleccionado = null;
            } else {
               if (!crearClasesAusentismos.contains(clasesAusentismoSeleccionado)) {

                  if (modificarClasesAusentismos.isEmpty()) {
                     modificarClasesAusentismos.add(clasesAusentismoSeleccionado);
                  } else if (!modificarClasesAusentismos.contains(clasesAusentismoSeleccionado)) {
                     modificarClasesAusentismos.add(clasesAusentismoSeleccionado);
                  }
                  if (guardado == true) {
                     guardado = false;
                  }
               }
               clasesAusentismoSeleccionado = null;
               clasesAusentismoSeleccionado = null;
            }
         }

         RequestContext.getCurrentInstance().update("form:datosClasesAusentismos");
         RequestContext.getCurrentInstance().update("form:ACEPTAR");

      }
   }

   public void verificarBorrado() {
      BigInteger contarSoAusentismosClaseAusentismo;
      BigInteger contarCausasAusentismosClaseAusentismo;

      System.out.println("Estoy en verificarBorrado");
      try {
         System.err.println("Control Secuencia de ControlTiposFamiliares ");
         if (tipoLista == 0) {
            contarCausasAusentismosClaseAusentismo = administrarClasesAusentismos.contarCausasAusentismosClaseAusentismo(clasesAusentismoSeleccionado.getSecuencia());
            contarSoAusentismosClaseAusentismo = administrarClasesAusentismos.contarSoAusentismosClaseAusentismo(clasesAusentismoSeleccionado.getSecuencia());
         } else {
            contarCausasAusentismosClaseAusentismo = administrarClasesAusentismos.contarCausasAusentismosClaseAusentismo(clasesAusentismoSeleccionado.getSecuencia());
            contarSoAusentismosClaseAusentismo = administrarClasesAusentismos.contarSoAusentismosClaseAusentismo(clasesAusentismoSeleccionado.getSecuencia());
         }
         if (contarCausasAusentismosClaseAusentismo.equals(new BigInteger("0")) && contarSoAusentismosClaseAusentismo.equals(new BigInteger("0"))) {
            System.out.println("Borrado==0");
            borrandoClasesAusentismos();
         } else {
            System.out.println("Borrado>0");

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:validacionBorrar");
            RequestContext.getCurrentInstance().execute("PF('validacionBorrar').show()");
            clasesAusentismoSeleccionado = null;

            contarCausasAusentismosClaseAusentismo = new BigInteger("-1");
            contarSoAusentismosClaseAusentismo = new BigInteger("-1");

         }
      } catch (Exception e) {
         System.err.println("ERROR ControlTiposFamiliares verificarBorrado ERROR " + e);
      }
   }

   public void borrandoClasesAusentismos() {

      if (clasesAusentismoSeleccionado != null) {
         System.out.println("Entro a borrandoClasesAusentismos");
         if (!modificarClasesAusentismos.isEmpty() && modificarClasesAusentismos.contains(clasesAusentismoSeleccionado)) {
            int modIndex = modificarClasesAusentismos.indexOf(clasesAusentismoSeleccionado);
            modificarClasesAusentismos.remove(modIndex);
            borrarClasesAusentismos.add(clasesAusentismoSeleccionado);
         } else if (!crearClasesAusentismos.isEmpty() && crearClasesAusentismos.contains(clasesAusentismoSeleccionado)) {
            int crearIndex = crearClasesAusentismos.indexOf(clasesAusentismoSeleccionado);
            crearClasesAusentismos.remove(crearIndex);
         } else {
            borrarClasesAusentismos.add(clasesAusentismoSeleccionado);
         }
         listClasesAusentismos.remove(clasesAusentismoSeleccionado);
         if (tipoLista == 1) {
            filtrarClasesAusentismos.remove(clasesAusentismoSeleccionado);

         }
         RequestContext context = RequestContext.getCurrentInstance();
         contarRegistros();
         RequestContext.getCurrentInstance().update("form:informacionRegistro");

         RequestContext.getCurrentInstance().update("form:datosClasesAusentismos");
         clasesAusentismoSeleccionado = null;

         if (guardado == true) {
            guardado = false;
         }
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      }

   }

   public void valoresBackupAutocompletar(int tipoNuevo, String valorCambio) {
      System.out.println("1...");
      if (valorCambio.equals("PERSONA")) {
         if (tipoNuevo == 1) {
            nuevoYduplicarCompletarPersona = nuevoClasesAusentismos.getTipo().getDescripcion();
         } else if (tipoNuevo == 2) {
            nuevoYduplicarCompletarPersona = duplicarClasesAusentismos.getTipo().getDescripcion();
         }

         System.out.println("PERSONA : " + nuevoYduplicarCompletarPersona);
      }
   }

   public void autocompletarNuevo(String confirmarCambio, String valorConfirmar, int tipoNuevo) {

      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      if (confirmarCambio.equalsIgnoreCase("PERSONA")) {
         System.out.println(" nueva Ciudad    Entro al if 'Centro costo'");
         System.out.println("NUEVO PERSONA :-------> " + nuevoClasesAusentismos.getTipo().getDescripcion());

         if (!nuevoClasesAusentismos.getTipo().getDescripcion().equals("")) {
            System.out.println("ENTRO DONDE NO TENIA QUE ENTRAR");
            System.out.println("valorConfirmar: " + valorConfirmar);
            System.out.println("nuevoYduplicarCompletarPersona: " + nuevoYduplicarCompletarPersona);
            nuevoClasesAusentismos.getTipo().setDescripcion(nuevoYduplicarCompletarPersona);
            getListaTiposausentismos();
            for (int i = 0; i < listaTiposausentismos.size(); i++) {
               if (listaTiposausentismos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            System.out.println("Coincidencias: " + coincidencias);
            if (coincidencias == 1) {
               nuevoClasesAusentismos.setTipo(listaTiposausentismos.get(indiceUnicoElemento));
               listaTiposausentismos = null;
               System.err.println("PERSONA GUARDADA :-----> " + nuevoClasesAusentismos.getTipo().getDescripcion());
            } else {
               RequestContext.getCurrentInstance().update("form:tiposAusentismosDialogo");
               RequestContext.getCurrentInstance().execute("PF('tiposAusentismosDialogo').show()");
               tipoActualizacion = tipoNuevo;
            }
         } else {
            nuevoClasesAusentismos.getTipo().setDescripcion(nuevoYduplicarCompletarPersona);
            System.out.println("valorConfirmar cuando es vacio: " + valorConfirmar);
            nuevoClasesAusentismos.setTipo(new Tiposausentismos());
            nuevoClasesAusentismos.getTipo().setDescripcion(" ");
            System.out.println("NUEVA NORMA LABORAL" + nuevoClasesAusentismos.getTipo().getDescripcion());
         }
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevoPersona");
      }
   }

   public void asignarVariableTiposausentismos(int tipoNuevo) {
      if (tipoNuevo == 0) {
         tipoActualizacion = 1;
      }
      if (tipoNuevo == 1) {
         tipoActualizacion = 2;
      }
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:tiposAusentismosDialogo");
      RequestContext.getCurrentInstance().execute("PF('tiposAusentismosDialogo').show()");
   }

   public void asignarVariableCiudades(int tipoNuevo) {
      if (tipoNuevo == 0) {
         tipoActualizacion = 1;
      }
      if (tipoNuevo == 1) {
         tipoActualizacion = 2;
      }
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:cargosDialogo");
      RequestContext.getCurrentInstance().execute("PF('cargosDialogo').show()");
   }

   public void autocompletarDuplicado(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      if (confirmarCambio.equalsIgnoreCase("PERSONA")) {
         System.out.println("DUPLICAR valorConfirmar : " + valorConfirmar);
         System.out.println("DUPLICAR CIUDAD bkp : " + nuevoYduplicarCompletarPersona);

         if (!duplicarClasesAusentismos.getTipo().getDescripcion().equals("")) {
            System.out.println("DUPLICAR ENTRO DONDE NO TENIA QUE ENTRAR");
            System.out.println("DUPLICAR valorConfirmar: " + valorConfirmar);
            System.out.println("DUPLICAR nuevoTipoCCAutoCompletar: " + nuevoYduplicarCompletarPersona);
            duplicarClasesAusentismos.getTipo().setDescripcion(nuevoYduplicarCompletarPersona);
            for (int i = 0; i < listaTiposausentismos.size(); i++) {
               if (listaTiposausentismos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            System.out.println("Coincidencias: " + coincidencias);
            if (coincidencias == 1) {
               duplicarClasesAusentismos.setTipo(listaTiposausentismos.get(indiceUnicoElemento));
               listaTiposausentismos = null;
            } else {
               RequestContext.getCurrentInstance().update("form:tiposAusentismosDialogo");
               RequestContext.getCurrentInstance().execute("PF('tiposAusentismosDialogo').show()");
               tipoActualizacion = tipoNuevo;
            }
         } else if (tipoNuevo == 2) {
            duplicarClasesAusentismos.setTipo(new Tiposausentismos());
            duplicarClasesAusentismos.getTipo().setDescripcion(" ");

            System.out.println("DUPLICAR PERSONA  : " + duplicarClasesAusentismos.getTipo().getDescripcion());
            System.out.println("nuevoYduplicarCompletarPERSONA : " + nuevoYduplicarCompletarPersona);
            if (tipoLista == 0) {
               clasesAusentismoSeleccionado.getTipo().setDescripcion(nuevoYduplicarCompletarPersona);
               System.err.println("tipo lista" + tipoLista);
               System.err.println("Secuencia Parentesco " + clasesAusentismoSeleccionado.getTipo().getSecuencia());
            } else if (tipoLista == 1) {
               clasesAusentismoSeleccionado.getTipo().setDescripcion(nuevoYduplicarCompletarPersona);
            }

         }
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarPersona");
      }
   }

   public void revisarDialogoGuardar() {

      if (!borrarClasesAusentismos.isEmpty() || !crearClasesAusentismos.isEmpty() || !modificarClasesAusentismos.isEmpty()) {
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:confirmarGuardar");
         RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
      }

   }

   public void guardarClasesAusentismos() {
      RequestContext context = RequestContext.getCurrentInstance();

      if (guardado == false) {
         System.out.println("Realizando guardarClasesAusentismos");
         if (!borrarClasesAusentismos.isEmpty()) {
            administrarClasesAusentismos.borrarClasesAusentismos(borrarClasesAusentismos);
            //mostrarBorrados
            registrosBorrados = borrarClasesAusentismos.size();
            RequestContext.getCurrentInstance().update("form:mostrarBorrados");
            RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
            borrarClasesAusentismos.clear();
         }
         if (!modificarClasesAusentismos.isEmpty()) {
            administrarClasesAusentismos.modificarClasesAusentismos(modificarClasesAusentismos);
            modificarClasesAusentismos.clear();
         }
         if (!crearClasesAusentismos.isEmpty()) {
            administrarClasesAusentismos.crearClasesAusentismos(crearClasesAusentismos);
            crearClasesAusentismos.clear();
         }
         System.out.println("Se guardaron los datos con exito");
         listClasesAusentismos = null;
         FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
         RequestContext.getCurrentInstance().update("form:datosClasesAusentismos");
         k = 0;
         guardado = true;
      }
      clasesAusentismoSeleccionado = null;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");

   }

   public void editarCelda() {
      if (clasesAusentismoSeleccionado != null) {
         if (tipoLista == 0) {
            editarClasesAusentismos = clasesAusentismoSeleccionado;
         }
         if (tipoLista == 1) {
            editarClasesAusentismos = clasesAusentismoSeleccionado;
         }

         RequestContext context = RequestContext.getCurrentInstance();
         System.out.println("Entro a editar... valor celda: " + cualCelda);
         if (cualCelda == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editPais");
            RequestContext.getCurrentInstance().execute("PF('editPais').show()");
            cualCelda = -1;
         } else if (cualCelda == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editSubtituloFirma");
            RequestContext.getCurrentInstance().execute("PF('editSubtituloFirma').show()");
            cualCelda = -1;
         } else if (cualCelda == 2) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editTiposausentismos");
            RequestContext.getCurrentInstance().execute("PF('editTiposausentismos').show()");
            cualCelda = -1;
         } else if (cualCelda == 3) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editCiudades");
            RequestContext.getCurrentInstance().execute("PF('editCiudades').show()");
            cualCelda = -1;
         }

      } else {
         RequestContext.getCurrentInstance().execute("formularioDialogos:seleccionarRegistro').show()");
      }
   }

   public void agregarNuevoClasesAusentismos() {
      System.out.println("agregarNuevoClasesAusentismos");
      int contador = 0;
      int duplicados = 0;

      Integer a = 0;
      a = null;
      mensajeValidacion = " ";
      RequestContext context = RequestContext.getCurrentInstance();
      if (nuevoClasesAusentismos.getCodigo() == null) {
         mensajeValidacion = " *Codigo \n";
         System.out.println("Mensaje validacion : " + mensajeValidacion);
      } else {
         System.out.println("codigo en Motivo Cambio Cargo: " + nuevoClasesAusentismos.getCodigo());

         for (int x = 0; x < listClasesAusentismos.size(); x++) {
            if (listClasesAusentismos.get(x).getCodigo().equals(nuevoClasesAusentismos.getCodigo())) {
               duplicados++;
            }
         }
         System.out.println("Antes del if Duplicados eses igual  : " + duplicados);

         if (duplicados > 0) {
            mensajeValidacion = " *Que NO Hayan Codigos Repetidos \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);
         } else {
            System.out.println("bandera");
            contador++;//1
         }
      }
      if (nuevoClasesAusentismos.getDescripcion() == null) {
         mensajeValidacion = mensajeValidacion + "   *Descripción \n";
         System.out.println("Mensaje validacion : " + mensajeValidacion);

      } else if (nuevoClasesAusentismos.getDescripcion().isEmpty()) {
         mensajeValidacion = mensajeValidacion + "   *Descripción \n";
         System.out.println("Mensaje validacion : " + mensajeValidacion);

      } else {
         System.out.println("Bandera : ");
         contador++;
      }

      if (nuevoClasesAusentismos.getTipo().getDescripcion() == null) {
         mensajeValidacion = mensajeValidacion + "   *Tipo Ausentismo \n";
         System.out.println("Mensaje validacion : " + mensajeValidacion);

      } else if (nuevoClasesAusentismos.getTipo().getDescripcion().isEmpty()) {
         mensajeValidacion = mensajeValidacion + "   *Tipo Ausentismo \n";
         System.out.println("Mensaje validacion : " + mensajeValidacion);

      } else {
         System.out.println("Bandera : ");
         contador++;
      }

      System.out.println("contador " + contador);

      if (contador == 3) {
         if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            //CERRAR FILTRADO
            System.out.println("Desactivar");
            codigo = (Column) c.getViewRoot().findComponent("form:datosClasesAusentismos:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosClasesAusentismos:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            personafir = (Column) c.getViewRoot().findComponent("form:datosClasesAusentismos:personafir");
            personafir.setFilterStyle("display: none; visibility: hidden;");
            bandera = 0;
            filtrarClasesAusentismos = null;
            tipoLista = 0;
         }
         System.out.println("Despues de la bandera");

         k++;
         l = BigInteger.valueOf(k);
         nuevoClasesAusentismos.setSecuencia(l);

         crearClasesAusentismos.add(nuevoClasesAusentismos);
         listClasesAusentismos.add(nuevoClasesAusentismos);
         clasesAusentismoSeleccionado = nuevoClasesAusentismos;
         nuevoClasesAusentismos = new Clasesausentismos();
         nuevoClasesAusentismos.setTipo(new Tiposausentismos());
         contarRegistros();
         RequestContext.getCurrentInstance().update("form:informacionRegistro");

         RequestContext.getCurrentInstance().update("form:datosClasesAusentismos");
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }

         RequestContext.getCurrentInstance().execute("PF('nuevoRegistroClasesAusentismos').hide()");

      } else {
         RequestContext.getCurrentInstance().update("form:validacionNuevoTipoAusentismo");
         RequestContext.getCurrentInstance().execute("PF('validacionNuevoTipoAusentismo').show()");
         contador = 0;
      }
   }

   public void limpiarNuevoClasesAusentismos() {
      System.out.println("limpiarNuevoClasesAusentismos");
      nuevoClasesAusentismos = new Clasesausentismos();
      nuevoClasesAusentismos.setTipo(new Tiposausentismos());

   }

   //------------------------------------------------------------------------------
   public void cargarNuevoClasesAusentismos() {
      System.out.println("cargarNuevoClasesAusentismos");

      duplicarClasesAusentismos = new Clasesausentismos();
      duplicarClasesAusentismos.setTipo(new Tiposausentismos());
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().execute("PF('nuevoRegistroClasesAusentismos').show()");

   }

   public void duplicandoClasesAusentismos() {
      System.out.println("duplicandoClasesAusentismos");
      if (clasesAusentismoSeleccionado != null) {
         duplicarClasesAusentismos = new Clasesausentismos();
         duplicarClasesAusentismos.setTipo(new Tiposausentismos());
         k++;
         l = BigInteger.valueOf(k);

         if (tipoLista == 0) {
            duplicarClasesAusentismos.setSecuencia(l);
            duplicarClasesAusentismos.setCodigo(clasesAusentismoSeleccionado.getCodigo());
            duplicarClasesAusentismos.setDescripcion(clasesAusentismoSeleccionado.getDescripcion());
            duplicarClasesAusentismos.setTipo(clasesAusentismoSeleccionado.getTipo());
         }
         if (tipoLista == 1) {
            duplicarClasesAusentismos.setSecuencia(l);
            duplicarClasesAusentismos.setCodigo(clasesAusentismoSeleccionado.getCodigo());
            duplicarClasesAusentismos.setDescripcion(clasesAusentismoSeleccionado.getDescripcion());
            duplicarClasesAusentismos.setTipo(clasesAusentismoSeleccionado.getTipo());

         }

         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTE");
         RequestContext.getCurrentInstance().execute("PF('duplicarRegistroClasesAusentismos').show()");
      }
   }

   public void confirmarDuplicar() {
      System.err.println("ESTOY EN CONFIRMAR DUPLICAR TIPOS EMPRESAS");
      int contador = 0;
      mensajeValidacion = " ";
      int duplicados = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      Integer a = 0;
      a = null;
      System.err.println("ConfirmarDuplicar codigo " + duplicarClasesAusentismos.getCodigo());

      if (duplicarClasesAusentismos.getCodigo() == null) {
         mensajeValidacion = mensajeValidacion + "   *Codigo \n";
         System.out.println("Mensaje validacion : " + mensajeValidacion);
      } else {
         for (int x = 0; x < listClasesAusentismos.size(); x++) {
            if (listClasesAusentismos.get(x).getCodigo().equals(duplicarClasesAusentismos.getCodigo())) {
               duplicados++;
            }
         }
         if (duplicados > 0) {
            mensajeValidacion = " *Que NO Existan Codigo Repetidos \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);
         } else {
            System.out.println("bandera");
            contador++;
            duplicados = 0;
         }
      }

      if (duplicarClasesAusentismos.getDescripcion() == null) {
         mensajeValidacion = mensajeValidacion + "   *Descripcion \n";
         System.out.println("Mensaje validacion : " + mensajeValidacion);

      } else if (duplicarClasesAusentismos.getDescripcion().isEmpty()) {
         mensajeValidacion = mensajeValidacion + "   *Descripcion \n";
         System.out.println("Mensaje validacion : " + mensajeValidacion);

      } else {
         System.out.println("Bandera : ");
         contador++;
      }

      if (duplicarClasesAusentismos.getTipo().getDescripcion() == null) {
         mensajeValidacion = mensajeValidacion + "   *Tipo Ausentismo \n";
         System.out.println("Mensaje validacion : " + mensajeValidacion);

      } else if (duplicarClasesAusentismos.getTipo().getDescripcion().isEmpty()) {
         mensajeValidacion = mensajeValidacion + "   *Tipo Ausentismo \n";
         System.out.println("Mensaje validacion : " + mensajeValidacion);

      } else {
         System.out.println("Bandera : ");
         contador++;
      }

      if (contador == 3) {

         System.out.println("Datos Duplicando: " + duplicarClasesAusentismos.getSecuencia() + "  " + duplicarClasesAusentismos.getCodigo());
         if (crearClasesAusentismos.contains(duplicarClasesAusentismos)) {
            System.out.println("Ya lo contengo.");
         }
         listClasesAusentismos.add(duplicarClasesAusentismos);
         crearClasesAusentismos.add(duplicarClasesAusentismos);
         clasesAusentismoSeleccionado = duplicarClasesAusentismos;
         RequestContext.getCurrentInstance().update("form:datosClasesAusentismos");
         if (guardado == true) {
            guardado = false;
         }
         contarRegistros();
         RequestContext.getCurrentInstance().update("form:informacionRegistro");

         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            //CERRAR FILTRADO
            codigo = (Column) c.getViewRoot().findComponent("form:datosClasesAusentismos:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosClasesAusentismos:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            personafir = (Column) c.getViewRoot().findComponent("form:datosClasesAusentismos:personafir");
            personafir.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosClasesAusentismos");
            bandera = 0;
            filtrarClasesAusentismos = null;
            tipoLista = 0;
         }
         duplicarClasesAusentismos = new Clasesausentismos();
         duplicarClasesAusentismos.setTipo(new Tiposausentismos());

         RequestContext.getCurrentInstance().execute("duplicarRegistroClasesAusentismos').hide()");

      } else {
         contador = 0;
         RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
         RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
      }
   }

   public void limpiarDuplicarClasesAusentismos() {
      duplicarClasesAusentismos = new Clasesausentismos();
      duplicarClasesAusentismos.setTipo(new Tiposausentismos());
   }

   public void exportPDF() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosClasesAusentismosExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "CLASESAUSENTISMOS", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void exportXLS() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosClasesAusentismosExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "CLASESAUSENTISMOS", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void verificarRastro() {
      RequestContext context = RequestContext.getCurrentInstance();
      System.out.println("lol");
      if (clasesAusentismoSeleccionado != null) {
         System.out.println("lol 2");
         int resultado = administrarRastros.obtenerTabla(clasesAusentismoSeleccionado.getSecuencia(), "CLASESAUSENTISMOS"); //En ENCARGATURAS lo cambia por el nombre de su tabla
         System.out.println("resultado: " + resultado);
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
      } else if (administrarRastros.verificarHistoricosTabla("CLASESAUSENTISMOS")) { // igual acá
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
   }

   public void contarRegistroTiposAusentismos() {
      RequestContext.getCurrentInstance().update("form:infoRegistroTiposAusentismos");
   }

   public void eventoFiltrar() {
      try {
         System.out.println("\n ENTRE A ControlClasesAusentismos.eventoFiltrar \n");
         if (tipoLista == 0) {
            tipoLista = 1;
         }
         RequestContext context = RequestContext.getCurrentInstance();
         contarRegistros();
      } catch (Exception e) {
         System.out.println("ERROR ControlClasesAusentismos eventoFiltrar ERROR===" + e.getMessage());
      }
   }

   public void contarRegistros() {
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
   }

   public void habilitarBotonLov() {
      activarBotonLov = false;
      RequestContext.getCurrentInstance().update("form:listaValores");
   }

   public void deshabilitarBotonLov() {
      activarBotonLov = true;
      RequestContext.getCurrentInstance().update("form:listaValores");
   }

   //*/*/*/*/*/*/*/*/*/*-/-*//-*/-  GETS Y SETS */*/*-*/-*/-*/*/*/*/*/---/*/*/*/*/-*/-*/-*/-*/-*/
   public List<Clasesausentismos> getListClasesAusentismos() {
      if (listClasesAusentismos == null) {
         listClasesAusentismos = administrarClasesAusentismos.consultarClasesAusentismos();
      }
      return listClasesAusentismos;
   }

   public void setListClasesAusentismos(List<Clasesausentismos> listClasesAusentismos) {
      this.listClasesAusentismos = listClasesAusentismos;
   }

   public List<Clasesausentismos> getFiltrarClasesAusentismos() {
      return filtrarClasesAusentismos;
   }

   public void setFiltrarClasesAusentismos(List<Clasesausentismos> filtrarClasesAusentismos) {
      this.filtrarClasesAusentismos = filtrarClasesAusentismos;
   }

   public Clasesausentismos getNuevoClasesAusentismos() {
      return nuevoClasesAusentismos;
   }

   public void setNuevoClasesAusentismos(Clasesausentismos nuevoClasesAusentismos) {
      this.nuevoClasesAusentismos = nuevoClasesAusentismos;
   }

   public Clasesausentismos getDuplicarClasesAusentismos() {
      return duplicarClasesAusentismos;
   }

   public void setDuplicarClasesAusentismos(Clasesausentismos duplicarClasesAusentismos) {
      this.duplicarClasesAusentismos = duplicarClasesAusentismos;
   }

   public Clasesausentismos getEditarClasesAusentismos() {
      return editarClasesAusentismos;
   }

   public void setEditarClasesAusentismos(Clasesausentismos editarClasesAusentismos) {
      this.editarClasesAusentismos = editarClasesAusentismos;
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

   public List<Tiposausentismos> getListaTiposausentismos() {
      if (listaTiposausentismos == null) {
         listaTiposausentismos = administrarClasesAusentismos.consultarLOVTiposAusentismos();
      }
      return listaTiposausentismos;
   }

   public void setListaTiposausentismos(List<Tiposausentismos> listaTiposausentismos) {
      this.listaTiposausentismos = listaTiposausentismos;
   }

   public List<Tiposausentismos> getFiltradoTiposausentismos() {
      return filtradoTiposausentismos;
   }

   public void setFiltradoTiposausentismos(List<Tiposausentismos> filtradoTiposausentismos) {
      this.filtradoTiposausentismos = filtradoTiposausentismos;
   }

   public Tiposausentismos getTipoSeleccionado() {
      return centrocostoSeleccionado;
   }

   public void setTipoSeleccionado(Tiposausentismos centrocostoSeleccionado) {
      this.centrocostoSeleccionado = centrocostoSeleccionado;
   }

   public Clasesausentismos getClasesAusentismoSeleccionado() {
      return clasesAusentismoSeleccionado;
   }

   public void setClasesAusentismoSeleccionado(Clasesausentismos clasesAusentismoSeleccionado) {
      this.clasesAusentismoSeleccionado = clasesAusentismoSeleccionado;
   }

   public String getInfoRegistro() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosClasesAusentismos");
      infoRegistro = String.valueOf(tabla.getRowCount());
      return infoRegistro;
   }

   public void setInfoRegistro(String infoRegistro) {
      this.infoRegistro = infoRegistro;
   }

   public String getInfoRegistroTiposAusentismos() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovTiposausentismos");
      infoRegistroTiposAusentismos = String.valueOf(tabla.getRowCount());
      return infoRegistroTiposAusentismos;
   }

   public void setInfoRegistroTiposAusentismos(String infoRegistroTiposAusentismos) {
      this.infoRegistroTiposAusentismos = infoRegistroTiposAusentismos;
   }

   public boolean isAceptar() {
      return aceptar;
   }

   public void setAceptar(boolean aceptar) {
      this.aceptar = aceptar;
   }

   public boolean isActivarBotonLov() {
      return activarBotonLov;
   }

   public void setActivarBotonLov(boolean activarBotonLov) {
      this.activarBotonLov = activarBotonLov;
   }

   public String getPaginaAnterior() {
      return paginaAnterior;
   }

   public void setPaginaAnterior(String paginaAnterior) {
      this.paginaAnterior = paginaAnterior;
   }
}
