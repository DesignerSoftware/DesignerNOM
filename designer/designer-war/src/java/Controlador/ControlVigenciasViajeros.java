/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Empleados;
import Entidades.Tiposviajeros;
import Entidades.VigenciasViajeros;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarRastrosInterface;
import InterfaceAdministrar.AdministrarVigenciasViajerosInterface;
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
public class ControlVigenciasViajeros implements Serializable {

   private static Logger log = Logger.getLogger(ControlVigenciasViajeros.class);

   @EJB
   AdministrarVigenciasViajerosInterface administrarVigenciasViajeros;
   @EJB
   AdministrarRastrosInterface administrarRastros;
   private List<VigenciasViajeros> listVigenciasViajerosPorEmpleado;
   private List<VigenciasViajeros> filtrarVigenciasViajerosPorEmpleado;
   private List<VigenciasViajeros> crearVigenciasViajerosPorEmpleado;
   private List<VigenciasViajeros> modificarVigenciasViajerosPorEmpleado;
   private List<VigenciasViajeros> borrarVigenciasViajerosPorEmpleado;
   private VigenciasViajeros nuevoVigenciasViajeros;
   private VigenciasViajeros duplicarVigenciasViajeros;
   private VigenciasViajeros editarVigenciasViajeros;
   private VigenciasViajeros vigenciaSeleccionada;
   //otros
   private int cualCelda, tipoLista, tipoActualizacion, k, bandera;
   private BigInteger l;
   private boolean aceptar, guardado;
   //AutoCompletar
   private boolean permitirIndex;
   //RASTRO
   private Column fecha, parentesco;
   //borrado
   private int registrosBorrados;
   private String mensajeValidacion;
   private BigInteger secuenciaEmpleado;
   //Empleado
   private Empleados empleadoSeleccionado;
   //autocompletar
   private String viajeros;
   private List<Tiposviajeros> lovTiposviajeros;
   private List<Tiposviajeros> filtradoTiposviajeros;
   private Tiposviajeros viajeroSeleccionado;
   private String nuevoYduplicarCompletarViajero;
   private String altoTabla;
   public String infoRegistro;
   public String infoRegistroTiposViajeros;
   //
   private DataTable tablaC;
   //
   private Boolean activarLOV;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

   public ControlVigenciasViajeros() {
      listVigenciasViajerosPorEmpleado = null;
      crearVigenciasViajerosPorEmpleado = new ArrayList<VigenciasViajeros>();
      modificarVigenciasViajerosPorEmpleado = new ArrayList<VigenciasViajeros>();
      borrarVigenciasViajerosPorEmpleado = new ArrayList<VigenciasViajeros>();
      permitirIndex = true;
      editarVigenciasViajeros = new VigenciasViajeros();
      nuevoVigenciasViajeros = new VigenciasViajeros();
      nuevoVigenciasViajeros.setTipoViajero(new Tiposviajeros());
      //duplicarVigenciasViajeros = new VigenciasViajeros();
      empleadoSeleccionado = null;
      bandera = 0;
      //secuenciaEmpleado = null;
      lovTiposviajeros = null;
      filtradoTiposviajeros = null;
      guardado = true;
      aceptar = true;
      altoTabla = "292";
      vigenciaSeleccionada = null;
      activarLOV = true;
      viajeroSeleccionado = null;
      mapParametros.put("paginaAnterior", paginaAnterior);
   }

   public void limpiarListasValor() {
      lovTiposviajeros = null;
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
         administrarVigenciasViajeros.obtenerConexion(ses.getId());
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
      String pagActual = "emplviajero";
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

   public void recibirEmpleado(BigInteger sec) {
      secuenciaEmpleado = sec;
      getListVigenciasViajerosPorEmpleado();
      if (listVigenciasViajerosPorEmpleado != null) {
         vigenciaSeleccionada = listVigenciasViajerosPorEmpleado.get(0);
      }
   }

   public void mostrarInfo(VigenciasViajeros vViajero, int celda) {
      int contador = 0;
      int fechas = 0;
      mensajeValidacion = " ";
      vigenciaSeleccionada = vViajero;
      cualCelda = celda;
      RequestContext context = RequestContext.getCurrentInstance();
      if (permitirIndex) {
         if (vigenciaSeleccionada.getFechavigencia() == null) {
            mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
            contador++;
         } else {
            for (int j = 0; j < listVigenciasViajerosPorEmpleado.size(); j++) {
               if (listVigenciasViajerosPorEmpleado.get(j) != vViajero) {
                  if (vigenciaSeleccionada.getFechavigencia().equals(listVigenciasViajerosPorEmpleado.get(j).getFechavigencia())) {
                     fechas++;
                  }
               }
            }
         }
         if (fechas > 0) {
            RequestContext.getCurrentInstance().update("form:validacionFechas");
            RequestContext.getCurrentInstance().execute("PF('validacionFechas').show()");
            contador++;
         }
         if (contador == 0) {
            if (!crearVigenciasViajerosPorEmpleado.contains(vigenciaSeleccionada)) {
               if (modificarVigenciasViajerosPorEmpleado.isEmpty()) {
                  modificarVigenciasViajerosPorEmpleado.add(vigenciaSeleccionada);
               } else if (!modificarVigenciasViajerosPorEmpleado.contains(vigenciaSeleccionada)) {
                  modificarVigenciasViajerosPorEmpleado.add(vigenciaSeleccionada);
               }
               if (guardado) {
                  guardado = false;
                  RequestContext.getCurrentInstance().update("form:ACEPTAR");
               }
               RequestContext.getCurrentInstance().update("form:datosViajeros");

            }
         } else {
            RequestContext.getCurrentInstance().update("form:validacionModificar");
            RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");
            cancelarModificacion();
         }

      }
   }

   public void cambiarIndice(VigenciasViajeros vViajeros, int celda) {
      RequestContext context = RequestContext.getCurrentInstance();
      if (permitirIndex == true) {
         vigenciaSeleccionada = vViajeros;
         cualCelda = celda;
         if (cualCelda == 1) {
            activarLOV = false;
            RequestContext.getCurrentInstance().update("form:listaValores");
            viajeros = vigenciaSeleccionada.getTipoViajero().getNombre();

         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('datosViajeros.selectRow(" + vViajeros + ", false); datosViajeros').unselectAllRows()");
      }
      RequestContext.getCurrentInstance().update("form:listaValores");
   }

   public void asignarIndex(VigenciasViajeros vViajeros, int LND, int dig) {
      RequestContext context = RequestContext.getCurrentInstance();
      vigenciaSeleccionada = vViajeros;
      activarLOV = false;
      viajeroSeleccionado = null;
      RequestContext.getCurrentInstance().update("form:listaValores");
      try {
         if (LND == 0) {
            tipoActualizacion = 0;
         } else if (LND == 1) {
            tipoActualizacion = 1;
         } else if (LND == 2) {
            tipoActualizacion = 2;
         }
         if (dig == 1) {
            contarRegistrosTV();
            RequestContext.getCurrentInstance().update("form:sucursalesDialogo");
            RequestContext.getCurrentInstance().execute("PF('sucursalesDialogo').show()");
            dig = -1;
         }
      } catch (Exception e) {
         log.warn("Error CONTROLBETAEMPLVIGENCIANORMALABORAL.asignarIndex ERROR======" + e.getMessage());
      }
   }

   public void activarAceptar() {
      aceptar = false;
   }

   public void listaValoresBoton() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (vigenciaSeleccionada == null) {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      } else if (vigenciaSeleccionada != null) {
         if (cualCelda == 1) {
            viajeroSeleccionado = null;
            RequestContext.getCurrentInstance().update("form:sucursalesDialogo");
            RequestContext.getCurrentInstance().execute("PF('sucursalesDialogo').show()");
            tipoActualizacion = 0;
         }
      }
   }

   public void salir() {
      cancelarModificacion();
      navegar("atras");
   }

   public void cancelarModificacion() {
      cerrarFiltrado();
      activarLOV = true;
      RequestContext.getCurrentInstance().update("form:listaValores");
      borrarVigenciasViajerosPorEmpleado.clear();
      crearVigenciasViajerosPorEmpleado.clear();
      modificarVigenciasViajerosPorEmpleado.clear();
      vigenciaSeleccionada = null;
      k = 0;
      listVigenciasViajerosPorEmpleado = null;
      getListVigenciasViajerosPorEmpleado();
      contarRegistrosTV();
      guardado = true;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
      RequestContext.getCurrentInstance().update("form:datosViajeros");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void activarCtrlF11() {
      if (bandera == 0) {
         FacesContext c = FacesContext.getCurrentInstance();
         fecha = (Column) c.getViewRoot().findComponent("form:datosViajeros:fecha");
         fecha.setFilterStyle("width: 85% !important;");
         parentesco = (Column) c.getViewRoot().findComponent("form:datosViajeros:parentesco");
         parentesco.setFilterStyle("width: 85% !important");
         altoTabla = "272";
         RequestContext.getCurrentInstance().update("form:datosViajeros");
         bandera = 1;
      } else if (bandera == 1) {
         cerrarFiltrado();
      }
   }

   private void cerrarFiltrado() {
      FacesContext c = FacesContext.getCurrentInstance();
      fecha = (Column) c.getViewRoot().findComponent("form:datosViajeros:fecha");
      fecha.setFilterStyle("display: none; visibility: hidden;");
      parentesco = (Column) c.getViewRoot().findComponent("form:datosViajeros:parentesco");
      parentesco.setFilterStyle("display: none; visibility: hidden;");
      altoTabla = "292";
      RequestContext.getCurrentInstance().update("form:datosViajeros");
      bandera = 0;
      filtrarVigenciasViajerosPorEmpleado = null;
      tipoLista = 0;
   }

   public void modificandoVigenciasViajeros(VigenciasViajeros vViajeros, String confirmarCambio, String valorConfirmar) {
      vigenciaSeleccionada = vViajeros;
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      int contador = 0;
      boolean banderita = false;
      RequestContext context = RequestContext.getCurrentInstance();
      if (confirmarCambio.equalsIgnoreCase("N")) {
         activarLOV = true;
         RequestContext.getCurrentInstance().update("form:listaValores");
         if (!crearVigenciasViajerosPorEmpleado.contains(vigenciaSeleccionada)) {
            if (vigenciaSeleccionada.getFechavigencia() == null) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               banderita = false;
            } else {
               for (int j = 0; j < listVigenciasViajerosPorEmpleado.size(); j++) {
                  if (listVigenciasViajerosPorEmpleado.get(j) != vViajeros) {
                     if (vigenciaSeleccionada.getFechavigencia().equals(listVigenciasViajerosPorEmpleado.get(j).getFechavigencia())) {
                        contador++;
                     }
                  }
               }
               if (contador > 0) {
                  mensajeValidacion = "FECHAS REPETIDAS";
                  banderita = false;
               } else {
                  banderita = true;
               }
            }

            if (banderita == true) {
               if (modificarVigenciasViajerosPorEmpleado.isEmpty()) {
                  modificarVigenciasViajerosPorEmpleado.add(vigenciaSeleccionada);
               } else if (!modificarVigenciasViajerosPorEmpleado.contains(vigenciaSeleccionada)) {
                  modificarVigenciasViajerosPorEmpleado.add(vigenciaSeleccionada);
               }
               if (guardado) {
                  guardado = false;
                  RequestContext.getCurrentInstance().update("form:ACEPTAR");
               }

            } else {
               RequestContext.getCurrentInstance().update("form:validacionModificar");
               RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");
               cancelarModificacion();
            }
         }

         RequestContext.getCurrentInstance().update("form:datosViajeros");
      } else if (confirmarCambio.equalsIgnoreCase("VIGENCIASVIAJEROS")) {
         activarLOV = false;
         RequestContext.getCurrentInstance().update("form:listaValores");
         if (!vigenciaSeleccionada.getTipoViajero().getNombre().equals("")) {
            vigenciaSeleccionada.getTipoViajero().setNombre(viajeros);
            for (int i = 0; i < lovTiposviajeros.size(); i++) {
               if (lovTiposviajeros.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }

            if (coincidencias == 1) {
               vigenciaSeleccionada.setTipoViajero(lovTiposviajeros.get(indiceUnicoElemento));
               lovTiposviajeros.clear();
               lovTiposviajeros = null;
               //getListaTiposFamiliares();

            } else {
               permitirIndex = false;
               RequestContext.getCurrentInstance().update("form:sucursalesDialogo");
               RequestContext.getCurrentInstance().execute("PF('sucursalesDialogo').show()");
               tipoActualizacion = 0;
            }
         } else {
            vigenciaSeleccionada.getTipoViajero().setNombre(viajeros);
            RequestContext.getCurrentInstance().update("form:sucursalesDialogo");
            RequestContext.getCurrentInstance().execute("PF('sucursalesDialogo').show()");
         }

         if (coincidencias == 1) {
            if (!crearVigenciasViajerosPorEmpleado.contains(vigenciaSeleccionada)) {

               if (modificarVigenciasViajerosPorEmpleado.isEmpty()) {
                  modificarVigenciasViajerosPorEmpleado.add(vigenciaSeleccionada);
               } else if (!modificarVigenciasViajerosPorEmpleado.contains(vigenciaSeleccionada)) {
                  modificarVigenciasViajerosPorEmpleado.add(vigenciaSeleccionada);
               }
            }

            if (guardado) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            RequestContext.getCurrentInstance().update("form:datosViajeros");
         }
      }

   }

   public void actualizarViajero() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoActualizacion == 0) {
         vigenciaSeleccionada.setTipoViajero(viajeroSeleccionado);
         if (!crearVigenciasViajerosPorEmpleado.contains(vigenciaSeleccionada)) {
            if (modificarVigenciasViajerosPorEmpleado.isEmpty()) {
               modificarVigenciasViajerosPorEmpleado.add(vigenciaSeleccionada);
            } else if (!modificarVigenciasViajerosPorEmpleado.contains(vigenciaSeleccionada)) {
               modificarVigenciasViajerosPorEmpleado.add(vigenciaSeleccionada);
            }
         }

         if (guardado) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         permitirIndex = true;
         RequestContext.getCurrentInstance().update("form:datosViajeros");
      } else if (tipoActualizacion == 1) {
         nuevoVigenciasViajeros.setTipoViajero(viajeroSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevoNombreSucursal");
      } else if (tipoActualizacion == 2) {
         duplicarVigenciasViajeros.setTipoViajero(viajeroSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTipoCentroCostos");
      }
      filtradoTiposviajeros = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;
      context.reset("form:lovTiposViajeros:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovTiposViajeros').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('sucursalesDialogo').hide()");
      //RequestContext.getCurrentInstance().update("form:lovTiposViajeros");
      //RequestContext.getCurrentInstance().update("form:datosViajeros");
   }

   public void cancelarCambioVigenciaViajero() {
      filtradoTiposviajeros = null;
      viajeroSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("form:lovTiposViajeros:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovTiposViajeros').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('sucursalesDialogo').hide()");
   }

   public void valoresBackupAutocompletar(int tipoNuevo) {
      if (tipoNuevo == 1) {
         nuevoYduplicarCompletarViajero = nuevoVigenciasViajeros.getTipoViajero().getNombre();
      } else if (tipoNuevo == 2) {
         nuevoYduplicarCompletarViajero = duplicarVigenciasViajeros.getTipoViajero().getNombre();
      }
   }

   public void autocompletarNuevo(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      if (confirmarCambio.equalsIgnoreCase("VIGENCIASVIAJEROS")) {

         if (!nuevoVigenciasViajeros.getTipoViajero().getNombre().equals("")) {
            nuevoVigenciasViajeros.getTipoViajero().setNombre(nuevoYduplicarCompletarViajero);
            getLovTiposviajeros();
            for (int i = 0; i < lovTiposviajeros.size(); i++) {
               if (lovTiposviajeros.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               nuevoVigenciasViajeros.setTipoViajero(lovTiposviajeros.get(indiceUnicoElemento));
               lovTiposviajeros = null;
               getLovTiposviajeros();
            } else {
               RequestContext.getCurrentInstance().update("form:sucursalesDialogo");
               RequestContext.getCurrentInstance().execute("PF('sucursalesDialogo').show()");
               tipoActualizacion = tipoNuevo;
            }
         } else {
            nuevoVigenciasViajeros.getTipoViajero().setNombre(nuevoYduplicarCompletarViajero);
            nuevoVigenciasViajeros.setTipoViajero(new Tiposviajeros());
            nuevoVigenciasViajeros.getTipoViajero().setNombre(" ");
         }
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevoNombreSucursal");
      }
   }

   public void asignarVariableTiposviajeros(int tipoNuevo) {
      if (tipoNuevo == 0) {
         tipoActualizacion = 1;
      }
      if (tipoNuevo == 1) {
         tipoActualizacion = 2;
      }
      viajeroSeleccionado = null;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:sucursalesDialogo");
      RequestContext.getCurrentInstance().execute("PF('sucursalesDialogo').show()");
   }

   public void limpiarNuevoViajero() {
      try {
         nuevoVigenciasViajeros = new VigenciasViajeros();
         nuevoVigenciasViajeros.setTipoViajero(new Tiposviajeros());
      } catch (Exception e) {
         log.warn("Error ControlVigenciasViajeros LIMPIAR NUEVO VIAJERO ERROR :" + e.getMessage());
      }
   }

   public void cargarTiposviajerosNuevoYDuplicar(int tipoNuevo) {
      if (tipoNuevo == 0) {
         tipoActualizacion = 1;
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:sucursalesDialogo");
         RequestContext.getCurrentInstance().execute("PF('sucursalesDialogo').show()");
      } else if (tipoNuevo == 1) {
         activarLOV = false;
         RequestContext.getCurrentInstance().update("form:listaValores");
         tipoActualizacion = 2;
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:sucursalesDialogo");
         RequestContext.getCurrentInstance().execute("PF('sucursalesDialogo').show()");
      }
   }

   public void autocompletarDuplicado(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      if (confirmarCambio.equalsIgnoreCase("VIGENCIASVIAJEROS")) {

         if (!duplicarVigenciasViajeros.getTipoViajero().getNombre().equals("")) {
            duplicarVigenciasViajeros.getTipoViajero().setNombre(nuevoYduplicarCompletarViajero);
            for (int i = 0; i < lovTiposviajeros.size(); i++) {
               if (lovTiposviajeros.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               duplicarVigenciasViajeros.setTipoViajero(lovTiposviajeros.get(indiceUnicoElemento));
               lovTiposviajeros = null;
               getLovTiposviajeros();
            } else {
               RequestContext.getCurrentInstance().update("form:sucursalesDialogo");
               RequestContext.getCurrentInstance().execute("PF('sucursalesDialogo').show()");
               tipoActualizacion = tipoNuevo;
            }
         } else if (tipoNuevo == 2) {
            //duplicarVigenciasViajeros.getTipoViajero().setNombre(nuevoYduplicarCompletarNormaLaboral);
            duplicarVigenciasViajeros.setTipoViajero(new Tiposviajeros());
            duplicarVigenciasViajeros.getTipoViajero().setNombre(" ");
            vigenciaSeleccionada.getTipoViajero().setNombre(nuevoYduplicarCompletarViajero);
         }
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTipoCentroCostos");
      }
   }

   public void borrandoHvEntrevistas() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (vigenciaSeleccionada == null) {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      } else if (vigenciaSeleccionada != null) {
         if (!modificarVigenciasViajerosPorEmpleado.isEmpty() && modificarVigenciasViajerosPorEmpleado.contains(vigenciaSeleccionada)) {
            int modIndex = modificarVigenciasViajerosPorEmpleado.indexOf(vigenciaSeleccionada);
            modificarVigenciasViajerosPorEmpleado.remove(modIndex);
            borrarVigenciasViajerosPorEmpleado.add(vigenciaSeleccionada);
         } else if (!crearVigenciasViajerosPorEmpleado.isEmpty() && crearVigenciasViajerosPorEmpleado.contains(vigenciaSeleccionada)) {
            int crearIndex = crearVigenciasViajerosPorEmpleado.indexOf(vigenciaSeleccionada);
            crearVigenciasViajerosPorEmpleado.remove(crearIndex);
         } else {
            borrarVigenciasViajerosPorEmpleado.add(vigenciaSeleccionada);
         }
         listVigenciasViajerosPorEmpleado.remove(vigenciaSeleccionada);
         if (tipoLista == 1) {
            filtrarVigenciasViajerosPorEmpleado.remove(vigenciaSeleccionada);
         }
         contarRegistros();
         vigenciaSeleccionada = null;
         activarLOV = true;
         RequestContext.getCurrentInstance().update("form:listaValores");
         if (guardado) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         RequestContext.getCurrentInstance().update("form:datosViajeros");
      }
   }

   public void revisarDialogoGuardar() {

      if (!borrarVigenciasViajerosPorEmpleado.isEmpty() || !crearVigenciasViajerosPorEmpleado.isEmpty() || !modificarVigenciasViajerosPorEmpleado.isEmpty()) {
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:confirmarGuardar");
         RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
      }

   }

   public void guardarYSalir() {
      guardarVigenciasViajeros();
      salir();
   }

   public void guardarVigenciasViajeros() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (guardado == false) {
         if (!borrarVigenciasViajerosPorEmpleado.isEmpty()) {
            for (int i = 0; i < borrarVigenciasViajerosPorEmpleado.size(); i++) {
               administrarVigenciasViajeros.borrarVigenciasViajeros(borrarVigenciasViajerosPorEmpleado);
            }
            //mostrarBorrados
            registrosBorrados = borrarVigenciasViajerosPorEmpleado.size();
            RequestContext.getCurrentInstance().update("form:mostrarBorrados");
            RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
            borrarVigenciasViajerosPorEmpleado.clear();
         }
         log.info("crearVigenciasViajerosPorEmpleado: " + crearVigenciasViajerosPorEmpleado);

         if (!crearVigenciasViajerosPorEmpleado.isEmpty()) {
            for (int i = 0; i < crearVigenciasViajerosPorEmpleado.size(); i++) {
               administrarVigenciasViajeros.crearVigenciasViajeros(crearVigenciasViajerosPorEmpleado);
            }
            crearVigenciasViajerosPorEmpleado.clear();
         }

         if (!modificarVigenciasViajerosPorEmpleado.isEmpty()) {
            administrarVigenciasViajeros.modificarVigenciasViajeros(modificarVigenciasViajerosPorEmpleado);
            modificarVigenciasViajerosPorEmpleado.clear();
         }
         listVigenciasViajerosPorEmpleado = null;
         getListVigenciasViajerosPorEmpleado();
         contarRegistrosTV();
         if (listVigenciasViajerosPorEmpleado != null && !listVigenciasViajerosPorEmpleado.isEmpty()) {
            vigenciaSeleccionada = listVigenciasViajerosPorEmpleado.get(0);
            contarRegistros();
         }
         activarLOV = true;
         RequestContext.getCurrentInstance().update("form:datosViajeros");
         RequestContext.getCurrentInstance().update("form:listaValores");
         guardado = true;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         k = 0;
         permitirIndex = true;
         FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      }
      // vigenciaSeleccionada = null;
   }

   public void editarCelda() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (vigenciaSeleccionada == null) {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      } else if (vigenciaSeleccionada != null) {
         editarVigenciasViajeros = vigenciaSeleccionada;
         if (cualCelda == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarFecha");
            RequestContext.getCurrentInstance().execute("PF('editarFecha').show()");
            cualCelda = -1;
         } else if (cualCelda == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editPuntaje");
            RequestContext.getCurrentInstance().execute("PF('editPuntaje').show()");
            cualCelda = -1;
         }

      }
   }

   public void agregarNuevoVigenciasViajeros() {
      int contador = 0;
      int fechas = 0;
      int pasa = 0;
      mensajeValidacion = " ";
      nuevoVigenciasViajeros.setEmpleado(empleadoSeleccionado);
      RequestContext context = RequestContext.getCurrentInstance();
      //vigenciaSeleccionada = null;
      if (nuevoVigenciasViajeros.getFechavigencia() == null || nuevoVigenciasViajeros.getFechavigencia().equals("")) {
         mensajeValidacion = " *Fecha\n";
      } else {
         if (listVigenciasViajerosPorEmpleado != null) {
            for (int i = 0; i < listVigenciasViajerosPorEmpleado.size(); i++) {
               if (nuevoVigenciasViajeros.getFechavigencia().equals(listVigenciasViajerosPorEmpleado.get(i).getFechavigencia())) {
                  fechas++;
               }
            }
         }
         if (fechas > 0) {
            RequestContext.getCurrentInstance().update("form:validacionFechas");
            RequestContext.getCurrentInstance().execute("PF('validacionFechas').show()");
            pasa++;
         } else {
            contador++;
         }
      }
      if (nuevoVigenciasViajeros.getTipoViajero().getSecuencia() == null) {
         mensajeValidacion = mensajeValidacion + "   *Tipo Viajero\n";
      } else {
         contador++;
      }
      if (contador == 2 && pasa == 0) {
         //FacesContext c = FacesContext.getCurrentInstance();
         if (bandera == 1) {
            cerrarFiltrado();
         }
         k++;
         l = BigInteger.valueOf(k);
         nuevoVigenciasViajeros.setSecuencia(l);
         //nuevoVigenciasViajeros.setEmpleado(empleadoSeleccionado);
         crearVigenciasViajerosPorEmpleado.add(nuevoVigenciasViajeros);
         if (listVigenciasViajerosPorEmpleado == null || listVigenciasViajerosPorEmpleado.isEmpty()) {
            listVigenciasViajerosPorEmpleado = new ArrayList<VigenciasViajeros>();
            listVigenciasViajerosPorEmpleado.add(nuevoVigenciasViajeros);
         } else {
            listVigenciasViajerosPorEmpleado.add(nuevoVigenciasViajeros);
         }
         activarLOV = true;
         RequestContext.getCurrentInstance().update("form:listaValores");
         contarRegistros();
         log.info("nuevoVigenciasViajeros: " + nuevoVigenciasViajeros);
         nuevoVigenciasViajeros = new VigenciasViajeros();
         nuevoVigenciasViajeros.setTipoViajero(new Tiposviajeros());

         RequestContext.getCurrentInstance().update("form:datosViajeros");
         if (guardado) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         RequestContext.getCurrentInstance().execute("PF('nuevoRegistroEvalEmpresas').hide()");
         // vigenciaSeleccionada = null;
      } else if (pasa == 0 && contador != 2) {
         RequestContext.getCurrentInstance().update("form:validacionNuevaCentroCosto");
         RequestContext.getCurrentInstance().execute("PF('validacionNuevaCentroCosto').show()");
         contador = 0;
         pasa = 0;
      }
      for (int i = 0; i < crearVigenciasViajerosPorEmpleado.size(); i++) {
         log.info("crearVigenciasViajerosPorEmpleado Poss " + i + " : " + crearVigenciasViajerosPorEmpleado.get(i));
      }
   }

   public void limpiarNuevoVigenciasViajeros() {
      nuevoVigenciasViajeros = new VigenciasViajeros();
      nuevoVigenciasViajeros.setTipoViajero(new Tiposviajeros());

   }

   //------------------------------------------------------------------------------
   public void duplicandoVigenciasViajeros() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (vigenciaSeleccionada == null) {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      } else if (vigenciaSeleccionada != null) {
         duplicarVigenciasViajeros = new VigenciasViajeros();
         k++;
         l = BigInteger.valueOf(k);

         duplicarVigenciasViajeros.setSecuencia(l);
         duplicarVigenciasViajeros.setEmpleado(vigenciaSeleccionada.getEmpleado());
         duplicarVigenciasViajeros.setFechavigencia(vigenciaSeleccionada.getFechavigencia());
         duplicarVigenciasViajeros.setTipoViajero(vigenciaSeleccionada.getTipoViajero());

         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarEvC");
         RequestContext.getCurrentInstance().execute("PF('duplicarRegistroEvalCompetencias').show()");
      }
   }

   public void confirmarDuplicar() {
      int contador = 0;
      mensajeValidacion = " ";
      int pasa = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      Short a = 0;
      int fechas = 0;
      a = null;

      if (duplicarVigenciasViajeros.getFechavigencia() == null) {
         mensajeValidacion = mensajeValidacion + "   * Fecha \n";
      } else {

         for (int j = 0; j < listVigenciasViajerosPorEmpleado.size(); j++) {
            if (duplicarVigenciasViajeros.getFechavigencia().equals(listVigenciasViajerosPorEmpleado.get(j).getFechavigencia())) {
               fechas++;
            }
         }
         if (fechas > 0) {
            RequestContext.getCurrentInstance().update("form:validacionFechas");
            RequestContext.getCurrentInstance().execute("PF('validacionFechas').show()");
            pasa++;

         } else {
            contador++;
         }
      }
      if (duplicarVigenciasViajeros.getTipoViajero().getNombre() == null || duplicarVigenciasViajeros.getTipoViajero().getNombre().isEmpty() || duplicarVigenciasViajeros.getTipoViajero().getNombre().equals(" ")) {
         mensajeValidacion = mensajeValidacion + "   * Tipo Viajero \n";
      } else {
         contador++;
      }
      if (duplicarVigenciasViajeros.getEmpleado().getSecuencia() == null) {
         duplicarVigenciasViajeros.setEmpleado(empleadoSeleccionado);
      }
      if (contador == 2 && pasa == 0) {
         listVigenciasViajerosPorEmpleado.add(duplicarVigenciasViajeros);
         crearVigenciasViajerosPorEmpleado.add(duplicarVigenciasViajeros);
         activarLOV = true;
         RequestContext.getCurrentInstance().update("form:listaValores");
         contarRegistros();
         vigenciaSeleccionada = listVigenciasViajerosPorEmpleado.get(listVigenciasViajerosPorEmpleado.indexOf(duplicarVigenciasViajeros));
         RequestContext.getCurrentInstance().update("form:datosViajeros");
         if (guardado) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         if (bandera == 1) {
            cerrarFiltrado();
         }
         duplicarVigenciasViajeros = new VigenciasViajeros();
         RequestContext.getCurrentInstance().execute("PF('duplicarRegistroEvalCompetencias').hide()");

      } else if (pasa == 0 && contador != 2) {
         RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
         RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
         contador = 0;
         pasa = 0;
      }
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void limpiarDuplicarVigenciasViajeros() {
      duplicarVigenciasViajeros = new VigenciasViajeros();
   }

   public void exportPDF() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosViajerosExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "VIGENCIASVIAJEROS", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void exportXLS() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosViajerosExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "VIGENCIASVIAJEROS", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void verificarRastro() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (vigenciaSeleccionada != null) {
         int resultado = administrarRastros.obtenerTabla(vigenciaSeleccionada.getSecuencia(), "VIGENCIASVIAJEROS"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
      } else if (administrarRastros.verificarHistoricosTabla("VIGENCIASVIAJEROS")) { // igual acá
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
   }

   public void eventoFiltrar() {
      if (tipoLista == 0) {
         tipoLista = 1;
      }
      activarLOV = true;
      RequestContext.getCurrentInstance().update("form:listaValores");
      RequestContext context = RequestContext.getCurrentInstance();
      contarRegistros();
   }

   public void contarRegistros() {
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
   }

   public void contarRegistrosTV() {
      RequestContext.getCurrentInstance().update("form:infoRegistroTiposViajeros");
   }

   public void recordarSeleccion() {
      if (vigenciaSeleccionada != null) {
         FacesContext c = FacesContext.getCurrentInstance();
         tablaC = (DataTable) c.getViewRoot().findComponent("form:datosViajeros");
         tablaC.setSelection(vigenciaSeleccionada);
      } else {
         vigenciaSeleccionada = null;
      }
   }

   public void anularLOV() {
      activarLOV = true;
      RequestContext.getCurrentInstance().update("form:listaValores");
   }

   //*/*/*/*/*/*/*/*/*/*-/-*//-*/-*/*/*-*/-*/-*/*/*/*/*/---/*/*/*/*/-*/-*/-*/-*/-*/
   public List<VigenciasViajeros> getListVigenciasViajerosPorEmpleado() {
      if (listVigenciasViajerosPorEmpleado == null) {
         listVigenciasViajerosPorEmpleado = administrarVigenciasViajeros.consultarVigenciasViajerosPorEmpleado(secuenciaEmpleado);
      }
      return listVigenciasViajerosPorEmpleado;
   }

   public void setListVigenciasViajerosPorEmpleado(List<VigenciasViajeros> listVigenciasViajerosPorEmpleado) {
      this.listVigenciasViajerosPorEmpleado = listVigenciasViajerosPorEmpleado;
   }

   public List<VigenciasViajeros> getFiltrarVigenciasViajerosPorEmpleado() {
      return filtrarVigenciasViajerosPorEmpleado;
   }

   public void setFiltrarVigenciasViajerosPorEmpleado(List<VigenciasViajeros> filtrarVigenciasViajerosPorEmpleado) {
      this.filtrarVigenciasViajerosPorEmpleado = filtrarVigenciasViajerosPorEmpleado;
   }

   public VigenciasViajeros getNuevoVigenciasViajeros() {
      return nuevoVigenciasViajeros;
   }

   public void setNuevoVigenciasViajeros(VigenciasViajeros nuevoVigenciasViajeros) {
      this.nuevoVigenciasViajeros = nuevoVigenciasViajeros;
   }

   public VigenciasViajeros getDuplicarVigenciasViajeros() {
      return duplicarVigenciasViajeros;
   }

   public void setDuplicarVigenciasViajeros(VigenciasViajeros duplicarVigenciasViajeros) {
      this.duplicarVigenciasViajeros = duplicarVigenciasViajeros;
   }

   public VigenciasViajeros getEditarVigenciasViajeros() {
      return editarVigenciasViajeros;
   }

   public void setEditarVigenciasViajeros(VigenciasViajeros editarVigenciasViajeros) {
      this.editarVigenciasViajeros = editarVigenciasViajeros;
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

   public Empleados getEmpleadoSeleccionado() {
      if (empleadoSeleccionado == null) {
         empleadoSeleccionado = administrarVigenciasViajeros.consultarEmpleado(secuenciaEmpleado);
      }
      return empleadoSeleccionado;
   }

   public void setEmpleadoSeleccionado(Empleados empleadoSeleccionado) {
      this.empleadoSeleccionado = empleadoSeleccionado;
   }

   public List<Tiposviajeros> getLovTiposviajeros() {
      if (lovTiposviajeros == null) {
         lovTiposviajeros = administrarVigenciasViajeros.consultarLOVTiposViajeros();
      }
      return lovTiposviajeros;
   }

   public void setLovTiposviajeros(List<Tiposviajeros> lovTiposviajeros) {
      this.lovTiposviajeros = lovTiposviajeros;
   }

   public List<Tiposviajeros> getFiltradoTiposviajeros() {
      return filtradoTiposviajeros;
   }

   public void setFiltradoTiposviajeros(List<Tiposviajeros> filtradoTiposviajeros) {
      this.filtradoTiposviajeros = filtradoTiposviajeros;
   }

   public Tiposviajeros getNormaLaboralSeleccionada() {
      return viajeroSeleccionado;
   }

   public void setNormaLaboralSeleccionada(Tiposviajeros normaLaboralSeleccionada) {
      this.viajeroSeleccionado = normaLaboralSeleccionada;
   }

   public boolean isGuardado() {
      return guardado;
   }

   public void setGuardado(boolean guardado) {
      this.guardado = guardado;
   }

   public VigenciasViajeros getVigenciaSeleccionada() {
      return vigenciaSeleccionada;
   }

   public void setVigenciaSeleccionada(VigenciasViajeros vigenciaSeleccionada) {
      this.vigenciaSeleccionada = vigenciaSeleccionada;
   }

   public String getAltoTabla() {
      return altoTabla;
   }

   public String getInfoRegistro() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosViajeros");
      infoRegistro = String.valueOf(tabla.getRowCount());
      return infoRegistro;
   }

   public boolean isAceptar() {
      return aceptar;
   }

   public void setAceptar(boolean aceptar) {
      this.aceptar = aceptar;
   }

   public String getInfoRegistroTiposViajeros() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovTiposViajeros");
      infoRegistroTiposViajeros = String.valueOf(tabla.getRowCount());
      return infoRegistroTiposViajeros;
   }

   public Boolean getActivarLOV() {
      return activarLOV;
   }

   public void setActivarLOV(Boolean activarLOV) {
      this.activarLOV = activarLOV;
   }
}
