/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Empleados;
import Entidades.VigenciasEstadosCiviles;
import Entidades.EstadosCiviles;
import Entidades.Personas;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarVigenciasEstadosCivilesInterface;
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
public class ControlVigenciasEstadosCiviles implements Serializable {

   private static Logger log = Logger.getLogger(ControlVigenciasEstadosCiviles.class);

   @EJB
   AdministrarVigenciasEstadosCivilesInterface administrarVigenciaEstadosCiviles;
   @EJB
   AdministrarRastrosInterface administrarRastros;

   private List<VigenciasEstadosCiviles> listVigenciaEstadoCivilPorEmpleado;
   private List<VigenciasEstadosCiviles> filtrarVigenciaEstadoCivilPorEmplado;
   private List<VigenciasEstadosCiviles> crearVigenciaEstadoCivilPorEmplado;
   private List<VigenciasEstadosCiviles> modificarVigenciaEstadoCivilPorEmplado;
   private List<VigenciasEstadosCiviles> borrarVigenciaEstadoCivilPorEmplado;
   private VigenciasEstadosCiviles nuevoVigenciaEstadoCivil;
   private VigenciasEstadosCiviles duplicarVigenciaEstadoCivil;
   private VigenciasEstadosCiviles editarVigenciaEstadoCivil;
   private VigenciasEstadosCiviles vigenciaSeleccionada;
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
   private String estadoCivil;
   private List<EstadosCiviles> lovEstadosCiviles;
   private List<EstadosCiviles> filtradoEstadosCiviles;
   private EstadosCiviles vigenciaEstadoCivilSeleccionada;
   private String nuevoYduplicarCompletarEstadoCivil;
   //ALTO TABLA
   private String altoTabla;
   private String infoRegistroEC;
   private DataTable tablaC;
   private boolean activarLov;
   private String infoRegistro;
   private BigInteger secuenciaPersona;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

   public ControlVigenciasEstadosCiviles() {
      listVigenciaEstadoCivilPorEmpleado = null;
      crearVigenciaEstadoCivilPorEmplado = new ArrayList<VigenciasEstadosCiviles>();
      modificarVigenciaEstadoCivilPorEmplado = new ArrayList<VigenciasEstadosCiviles>();
      borrarVigenciaEstadoCivilPorEmplado = new ArrayList<VigenciasEstadosCiviles>();
      permitirIndex = true;
      editarVigenciaEstadoCivil = new VigenciasEstadosCiviles();
      nuevoVigenciaEstadoCivil = new VigenciasEstadosCiviles();
      nuevoVigenciaEstadoCivil.setEstadocivil(new EstadosCiviles());
      nuevoVigenciaEstadoCivil.setFechavigencia(new Date());
      duplicarVigenciaEstadoCivil = new VigenciasEstadosCiviles();
      empleadoSeleccionado = null;
      secuenciaEmpleado = null;
      lovEstadosCiviles = null;
      filtradoEstadosCiviles = null;
      guardado = true;
      altoTabla = "270";
      aceptar = true;
      activarLov = true;
      mapParametros.put("paginaAnterior", paginaAnterior);
   }

   public void limpiarListasValor() {
      lovEstadosCiviles = null;
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
         administrarVigenciaEstadosCiviles.obtenerConexion(ses.getId());
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
      String pagActual = "perestadocivil";
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

   public void recibirEmpleado(BigInteger sec, BigInteger secPersona) {

      secuenciaEmpleado = sec;
      secuenciaPersona = secPersona;
      empleadoSeleccionado = null;
      listVigenciaEstadoCivilPorEmpleado = null;
      getEmpleadoSeleccionado();
      getListVigenciaEstadoCivilPorEmpleado();
      deshabilitarBotonLov();
      if (!listVigenciaEstadoCivilPorEmpleado.isEmpty()) {
         if (listVigenciaEstadoCivilPorEmpleado != null) {
            vigenciaSeleccionada = listVigenciaEstadoCivilPorEmpleado.get(0);
         }
      }

   }

   public void mostrarNuevo() {
      log.error("NUEVA FECHA : " + nuevoVigenciaEstadoCivil.getFechavigencia());
   }

   public void mostrarInfo(VigenciasEstadosCiviles vigenciaEstadoCivil, int celda) {
      int contador = 0;
      int fechas = 0;
      mensajeValidacion = " ";
      vigenciaSeleccionada = vigenciaEstadoCivil;
      cualCelda = celda;
      RequestContext context = RequestContext.getCurrentInstance();
      if (permitirIndex == true) {
         if (tipoLista == 0) {
            vigenciaSeleccionada.getSecuencia();
            log.error("MODIFICAR FECHA \n Indice" + vigenciaSeleccionada + "Fecha " + vigenciaSeleccionada.getFechavigencia());
            if (vigenciaSeleccionada.getFechavigencia() == null) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               contador++;
            }
            if (fechas > 0) {
               mensajeValidacion = "FECHAS REPETIDAS";
               contador++;
            }
            if (contador == 0) {
               if (!crearVigenciaEstadoCivilPorEmplado.contains(vigenciaSeleccionada)) {
                  if (modificarVigenciaEstadoCivilPorEmplado.isEmpty()) {
                     modificarVigenciaEstadoCivilPorEmplado.add(vigenciaSeleccionada);
                  } else if (!modificarVigenciaEstadoCivilPorEmplado.contains(vigenciaSeleccionada)) {
                     modificarVigenciaEstadoCivilPorEmplado.add(vigenciaSeleccionada);
                  }
                  if (guardado == true) {
                     guardado = false;
                     RequestContext.getCurrentInstance().update("form:ACEPTAR");
                  }
                  RequestContext.getCurrentInstance().update("form:datosEstadoCivil");

               }
            } else {
               RequestContext.getCurrentInstance().update("form:validacionModificar");
               RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");
               cancelarModificacion();
            }
         } else {

            vigenciaSeleccionada.getSecuencia();
            if (vigenciaSeleccionada.getFechavigencia() == null) {
               mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
               contador++;
            } else {
               for (int j = 0; j < filtrarVigenciaEstadoCivilPorEmplado.size(); j++) {
                  if (vigenciaSeleccionada.getFechavigencia().equals(filtrarVigenciaEstadoCivilPorEmplado.get(j).getFechavigencia())) {
                     fechas++;
                  }
               }
               for (int j = 0; j < listVigenciaEstadoCivilPorEmpleado.size(); j++) {
                  if (vigenciaSeleccionada.getFechavigencia().equals(listVigenciaEstadoCivilPorEmpleado.get(j).getFechavigencia())) {
                     fechas++;
                  }
               }
            }
            if (fechas > 0) {
               mensajeValidacion = "FECHAS REPETIDAS";
               contador++;
            }
            if (contador == 0) {
               if (!crearVigenciaEstadoCivilPorEmplado.contains(vigenciaSeleccionada)) {
                  if (modificarVigenciaEstadoCivilPorEmplado.isEmpty()) {
                     modificarVigenciaEstadoCivilPorEmplado.add(vigenciaSeleccionada);
                  } else if (!modificarVigenciaEstadoCivilPorEmplado.contains(vigenciaSeleccionada)) {
                     modificarVigenciaEstadoCivilPorEmplado.add(vigenciaSeleccionada);
                  }
                  if (guardado == true) {
                     guardado = false;
                     RequestContext.getCurrentInstance().update("form:ACEPTAR");
                  }
                  RequestContext.getCurrentInstance().update("form:datosEstadoCivil");

               }
            } else {
               RequestContext.getCurrentInstance().update("form:validacionModificar");
               RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");
               cancelarModificacion();

            }
//                vigenciaSeleccionada = null;
         }

      }

   }

   public void cambiarIndice(VigenciasEstadosCiviles vigenciaEstadoCivil, int celda) {

      if (permitirIndex == true) {
         vigenciaSeleccionada = vigenciaEstadoCivil;
         vigenciaSeleccionada.getSecuencia();
         cualCelda = celda;
         if (cualCelda == 0) {
            deshabilitarBotonLov();
            vigenciaSeleccionada.getFechavigencia();
         } else if (cualCelda == 1) {
            habilitarBotonLov();
            contarRegistroEC();
            estadoCivil = vigenciaSeleccionada.getEstadocivil().getDescripcion();
         }

      }
   }

   public void asignarIndex(VigenciasEstadosCiviles vigenciaEstadoCivil, int LND, int dig) {
      try {
         RequestContext context = RequestContext.getCurrentInstance();
         vigenciaSeleccionada = vigenciaEstadoCivil;
         if (LND == 0) {
            deshabilitarBotonLov();
            tipoActualizacion = 0;
         } else if (LND == 1) {
            deshabilitarBotonLov();
            tipoActualizacion = 1;
            log.info("Tipo Actualizacion: " + tipoActualizacion);
         } else if (LND == 2) {
            deshabilitarBotonLov();
            tipoActualizacion = 2;
         }
         if (dig == 1) {
            habilitarBotonLov();
            contarRegistroEC();
            RequestContext.getCurrentInstance().update("form:EstadoCivilDialogo");
            RequestContext.getCurrentInstance().execute("PF('EstadoCivilDialogo').show()");
            dig = -1;
         }
      } catch (Exception e) {
         log.warn("Error CONTROLVIGENCIASESTADOSCIVILES.AsignarIndex ERROR======" + e.getMessage());
      }
   }

   public void activarAceptar() {
      aceptar = false;
   }

   public void listaValoresBoton() {
      if (vigenciaSeleccionada != null) {
         if (cualCelda == 1) {
            habilitarBotonLov();
            contarRegistroEC();
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:EstadoCivilDialogo");
            RequestContext.getCurrentInstance().execute("PF('EstadoCivilDialogo').show()");
         }
      }
   }

   public void cancelarModificacion() {
      if (bandera == 1) {
         FacesContext c = FacesContext.getCurrentInstance();
         //CERRAR FILTRADO
         fecha = (Column) c.getViewRoot().findComponent("form:datosEstadoCivil:fecha");
         fecha.setFilterStyle("display: none; visibility: hidden;");
         parentesco = (Column) c.getViewRoot().findComponent("form:datosEstadoCivil:parentesco");
         parentesco.setFilterStyle("display: none; visibility: hidden;");
         altoTabla = "270";
         RequestContext.getCurrentInstance().update("form:datosEstadoCivil");
         bandera = 0;
         filtrarVigenciaEstadoCivilPorEmplado = null;
         tipoLista = 0;
      }

      borrarVigenciaEstadoCivilPorEmplado.clear();
      crearVigenciaEstadoCivilPorEmplado.clear();
      modificarVigenciaEstadoCivilPorEmplado.clear();
      vigenciaSeleccionada = null;
      vigenciaSeleccionada = null;
      k = 0;
      listVigenciaEstadoCivilPorEmpleado = null;
      guardado = true;
      permitirIndex = true;
      getListVigenciaEstadoCivilPorEmpleado();
      deshabilitarBotonLov();
      RequestContext context = RequestContext.getCurrentInstance();
      contarRegistros();
      RequestContext.getCurrentInstance().update("form:infoRegistro");
      RequestContext.getCurrentInstance().update("form:datosEstadoCivil");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void salir() {
      limpiarListasValor();
      if (bandera == 1) {
         FacesContext c = FacesContext.getCurrentInstance();
         //CERRAR FILTRADO
         fecha = (Column) c.getViewRoot().findComponent("form:datosEstadoCivil:fecha");
         fecha.setFilterStyle("display: none; visibility: hidden;");
         parentesco = (Column) c.getViewRoot().findComponent("form:datosEstadoCivil:parentesco");
         parentesco.setFilterStyle("display: none; visibility: hidden;");
         altoTabla = "270";
         RequestContext.getCurrentInstance().update("form:datosEstadoCivil");
         bandera = 0;
         limpiarListasValor();
         filtrarVigenciaEstadoCivilPorEmplado = null;
         tipoLista = 0;
      }

      borrarVigenciaEstadoCivilPorEmplado.clear();
      crearVigenciaEstadoCivilPorEmplado.clear();
      modificarVigenciaEstadoCivilPorEmplado.clear();
      vigenciaSeleccionada = null;
      vigenciaSeleccionada = null;
      k = 0;
      listVigenciaEstadoCivilPorEmpleado = null;
      guardado = true;
      permitirIndex = true;
      getListVigenciaEstadoCivilPorEmpleado();
      RequestContext context = RequestContext.getCurrentInstance();
      contarRegistros();
      RequestContext.getCurrentInstance().update("form:infoRegistro");
      RequestContext.getCurrentInstance().update("form:datosEstadoCivil");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      navegar("atras");
   }

   public void activarCtrlF11() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 0) {
         fecha = (Column) c.getViewRoot().findComponent("form:datosEstadoCivil:fecha");
         fecha.setFilterStyle("width: 85% !important;");
         parentesco = (Column) c.getViewRoot().findComponent("form:datosEstadoCivil:parentesco");
         parentesco.setFilterStyle("width: 85% !important;");
         altoTabla = "250";
         RequestContext.getCurrentInstance().update("form:datosEstadoCivil");
         log.info("Activar");
         bandera = 1;
      } else if (bandera == 1) {
         log.info("Desactivar");
         fecha = (Column) c.getViewRoot().findComponent("form:datosEstadoCivil:fecha");
         fecha.setFilterStyle("display: none; visibility: hidden;");
         parentesco = (Column) c.getViewRoot().findComponent("form:datosEstadoCivil:parentesco");
         parentesco.setFilterStyle("display: none; visibility: hidden;");
         altoTabla = "270";
         RequestContext.getCurrentInstance().update("form:datosEstadoCivil");
         bandera = 0;
         filtrarVigenciaEstadoCivilPorEmplado = null;
         tipoLista = 0;
      }
   }

   public void modificandoVigenciaEstadoCivil(VigenciasEstadosCiviles vigenciaEstadoCivil) {
      vigenciaSeleccionada = vigenciaEstadoCivil;
      if (!crearVigenciaEstadoCivilPorEmplado.contains(vigenciaSeleccionada)) {

         if (modificarVigenciaEstadoCivilPorEmplado.isEmpty()) {
            modificarVigenciaEstadoCivilPorEmplado.add(vigenciaSeleccionada);
         } else if (!modificarVigenciaEstadoCivilPorEmplado.contains(vigenciaSeleccionada)) {
            modificarVigenciaEstadoCivilPorEmplado.add(vigenciaSeleccionada);
         }
         if (guardado == true) {
            guardado = false;
         }
      }
      RequestContext.getCurrentInstance().update("form:datosEstadoCivil");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");

   }

   public void actualizarEstadoCivil() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoActualizacion == 0) {
         if (tipoLista == 0) {
            vigenciaSeleccionada.setEstadocivil(vigenciaEstadoCivilSeleccionada);

            if (!crearVigenciaEstadoCivilPorEmplado.contains(vigenciaSeleccionada)) {
               if (modificarVigenciaEstadoCivilPorEmplado.isEmpty()) {
                  modificarVigenciaEstadoCivilPorEmplado.add(vigenciaSeleccionada);
               } else if (!modificarVigenciaEstadoCivilPorEmplado.contains(vigenciaSeleccionada)) {
                  modificarVigenciaEstadoCivilPorEmplado.add(vigenciaSeleccionada);
               }
            }
         } else {
            vigenciaSeleccionada.setEstadocivil(vigenciaEstadoCivilSeleccionada);

            if (!crearVigenciaEstadoCivilPorEmplado.contains(vigenciaSeleccionada)) {
               if (modificarVigenciaEstadoCivilPorEmplado.isEmpty()) {
                  modificarVigenciaEstadoCivilPorEmplado.add(vigenciaSeleccionada);
               } else if (!modificarVigenciaEstadoCivilPorEmplado.contains(vigenciaSeleccionada)) {
                  modificarVigenciaEstadoCivilPorEmplado.add(vigenciaSeleccionada);
               }
            }
         }
         if (guardado == true) {
            guardado = false;
         }
         permitirIndex = true;
         RequestContext.getCurrentInstance().update("form:datosEstadoCivil");
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      } else if (tipoActualizacion == 1) {
         nuevoVigenciaEstadoCivil.setEstadocivil(vigenciaEstadoCivilSeleccionada);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevoNombreSucursal");
      } else if (tipoActualizacion == 2) {
         duplicarVigenciaEstadoCivil.setEstadocivil(vigenciaEstadoCivilSeleccionada);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarEstadoCivil");
      }
      filtradoEstadosCiviles = null;
      vigenciaEstadoCivilSeleccionada = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;
      context.reset("form:lovTiposFamiliares:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovTiposFamiliares').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('EstadoCivilDialogo').hide()");
      //RequestContext.getCurrentInstance().update("form:lovTiposFamiliares");
      //RequestContext.getCurrentInstance().update("form:datosEstadoCivil");
   }

   public void cancelarCambioEstadoCivil() {
      filtradoEstadosCiviles = null;
      vigenciaEstadoCivilSeleccionada = null;
      aceptar = true;
      vigenciaSeleccionada = null;
      tipoActualizacion = -1;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("form:lovTiposFamiliares:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovTiposFamiliares').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('EstadoCivilDialogo').hide()");
      RequestContext.getCurrentInstance().update("form:EstadoCivilDialogo");
      RequestContext.getCurrentInstance().update("form:lovTiposFamiliares");
      RequestContext.getCurrentInstance().update("form:aceptarS");

   }

   public void valoresBackupAutocompletar(int tipoNuevo) {
      log.info("1...");
      if (tipoNuevo == 1) {
         nuevoYduplicarCompletarEstadoCivil = nuevoVigenciaEstadoCivil.getEstadocivil().getDescripcion();
      } else if (tipoNuevo == 2) {
         nuevoYduplicarCompletarEstadoCivil = duplicarVigenciaEstadoCivil.getEstadocivil().getDescripcion();
      }
      log.info(" valoresBackupAutocompletar nuevoYduplicarCompletarEstadoCivil " + nuevoYduplicarCompletarEstadoCivil);
   }

   public void autocompletarNuevo(String confirmarCambio, String valorConfirmar, int tipoNuevo) {

      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      if (confirmarCambio.equalsIgnoreCase("VIGESTADOSCIVILES")) {
         log.info(" nueva Ciudad    Entro al if 'Centro costo'");
         log.info("NOMBRE CENTRO COSTO: " + nuevoVigenciaEstadoCivil.getEstadocivil().getDescripcion());

         if (!nuevoVigenciaEstadoCivil.getEstadocivil().getDescripcion().equals("")) {
            log.info("ENTRO DONDE NO TENIA QUE ENTRAR");
            log.info("valorConfirmar: " + valorConfirmar);
            log.info("nuevoYduplicarCiudadCompletar: " + nuevoYduplicarCompletarEstadoCivil);
            nuevoVigenciaEstadoCivil.getEstadocivil().setDescripcion(nuevoYduplicarCompletarEstadoCivil);
            getLovEstadosCiviles();
            for (int i = 0; i < lovEstadosCiviles.size(); i++) {
               if (lovEstadosCiviles.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            log.info("Coincidencias: " + coincidencias);
            if (coincidencias == 1) {
               nuevoVigenciaEstadoCivil.setEstadocivil(lovEstadosCiviles.get(indiceUnicoElemento));
               lovEstadosCiviles = null;
               getLovEstadosCiviles();
               log.error("ESTADO CIVIL GUARDADA " + nuevoVigenciaEstadoCivil.getEstadocivil().getDescripcion());
            } else {
               RequestContext.getCurrentInstance().update("form:EstadoCivilDialogo");
               RequestContext.getCurrentInstance().execute("PF('EstadoCivilDialogo').show()");
               tipoActualizacion = tipoNuevo;
            }
         } else {
            nuevoVigenciaEstadoCivil.getEstadocivil().setDescripcion(nuevoYduplicarCompletarEstadoCivil);
            log.info("valorConfirmar cuando es vacio: " + valorConfirmar);
            nuevoVigenciaEstadoCivil.setEstadocivil(new EstadosCiviles());
            nuevoVigenciaEstadoCivil.getEstadocivil().setDescripcion(" ");
            log.info("NUEVA ESTADO CIVIL" + nuevoVigenciaEstadoCivil.getEstadocivil().getDescripcion());
         }
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevoNombreSucursal");
      }

   }

   public void mostrarDialogoEstadosCiviles(int tipoNuevo) {
      if (tipoNuevo == 0) {
         tipoActualizacion = 1;
      }
      if (tipoNuevo == 1) {
         tipoActualizacion = 2;
      }
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:EstadoCivilDialogo");
      RequestContext.getCurrentInstance().execute("PF('EstadoCivilDialogo').show()");
   }

   public void limpiarNuevoEstadoCivil() {
      log.info("\n ENTRE A LIMPIAR NUEVO ESTADO CIVIL  \n");
      nuevoVigenciaEstadoCivil = new VigenciasEstadosCiviles();
      nuevoVigenciaEstadoCivil.setEstadocivil(new EstadosCiviles());
      vigenciaSeleccionada = null;
   }

   public void autocompletarDuplicado(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
      log.info("DUPLICAR entrooooooooooooooooooooooooooooooooooooooooooooooooooooooo!!!");
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      if (confirmarCambio.equalsIgnoreCase("VIGESTADOSCIVILES")) {
         log.info("DUPLICAR valorConfirmar : " + valorConfirmar);
         log.info("DUPLICAR CIUDAD bkp : " + nuevoYduplicarCompletarEstadoCivil);

         if (!duplicarVigenciaEstadoCivil.getEstadocivil().getDescripcion().equals("") || !duplicarVigenciaEstadoCivil.getEstadocivil().getDescripcion().isEmpty()) {
            log.info("DUPLICAR ENTRO DONDE NO TENIA QUE ENTRAR");
            log.info("DUPLICAR valorConfirmar: " + valorConfirmar);
            log.info("DUPLICAR nuevoTipoCCAutoCompletar: " + nuevoYduplicarCompletarEstadoCivil);
            duplicarVigenciaEstadoCivil.getEstadocivil().setDescripcion(nuevoYduplicarCompletarEstadoCivil);
            for (int i = 0; i < lovEstadosCiviles.size(); i++) {
               if (lovEstadosCiviles.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            log.info("Coincidencias: " + coincidencias);
            if (coincidencias == 1) {
               duplicarVigenciaEstadoCivil.setEstadocivil(lovEstadosCiviles.get(indiceUnicoElemento));
               lovEstadosCiviles = null;
               getLovEstadosCiviles();
            } else {
               RequestContext.getCurrentInstance().update("form:EstadoCivilDialogo");
               RequestContext.getCurrentInstance().execute("PF('EstadoCivilDialogo').show()");
               tipoActualizacion = tipoNuevo;
            }
         } else if (tipoNuevo == 2) {
            //duplicarVigenciaEstadoCivil.getEstadocivil().setDescripcion(nuevoYduplicarCompletarNormaLaboral);
            log.info("DUPLICAR valorConfirmar cuando es vacio: " + valorConfirmar);
            log.info("DUPLICAR INDEX : " + vigenciaSeleccionada);
            duplicarVigenciaEstadoCivil.setEstadocivil(new EstadosCiviles());
            duplicarVigenciaEstadoCivil.getEstadocivil().setDescripcion(" ");

            log.info("DUPLICAR Valor ESTADO CIVIL : " + duplicarVigenciaEstadoCivil.getEstadocivil().getDescripcion());
            if (tipoLista == 0) {
               vigenciaSeleccionada.getEstadocivil().setDescripcion(nuevoYduplicarCompletarEstadoCivil);
               log.error("tipo lista" + tipoLista);
               log.error("Secuencia EstadoCivil " + vigenciaSeleccionada.getEstadocivil().getSecuencia());
            } else if (tipoLista == 1) {
               vigenciaSeleccionada.getEstadocivil().setDescripcion(nuevoYduplicarCompletarEstadoCivil);
            }

         }
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarEstadoCivil");
      }
   }

   public void borrandoVigenciasEstadosCiviles() {

      if (vigenciaSeleccionada != null) {
         log.info("Entro a borrandoEvalCompetencias");
         if (!modificarVigenciaEstadoCivilPorEmplado.isEmpty() && modificarVigenciaEstadoCivilPorEmplado.contains(vigenciaSeleccionada)) {
            int modIndex = modificarVigenciaEstadoCivilPorEmplado.indexOf(vigenciaSeleccionada);
            modificarVigenciaEstadoCivilPorEmplado.remove(modIndex);
            borrarVigenciaEstadoCivilPorEmplado.add(vigenciaSeleccionada);
         } else if (!crearVigenciaEstadoCivilPorEmplado.isEmpty() && crearVigenciaEstadoCivilPorEmplado.contains(vigenciaSeleccionada)) {
            int crearIndex = crearVigenciaEstadoCivilPorEmplado.indexOf(vigenciaSeleccionada);
            crearVigenciaEstadoCivilPorEmplado.remove(crearIndex);
         } else {
            borrarVigenciaEstadoCivilPorEmplado.add(vigenciaSeleccionada);
         }
         listVigenciaEstadoCivilPorEmpleado.remove(vigenciaSeleccionada);
         if (tipoLista == 1) {
            filtrarVigenciaEstadoCivilPorEmplado.remove(vigenciaSeleccionada);
         }
         RequestContext context = RequestContext.getCurrentInstance();
         if (guardado == true) {
            guardado = false;
         }
         RequestContext.getCurrentInstance().update("form:datosEstadoCivil");
         contarRegistros();
         RequestContext.getCurrentInstance().update("form:infoRegistro");
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         vigenciaSeleccionada = null;
      } else {
         RequestContext.getCurrentInstance().execute("PF('formularioDialogos:seleccionarRegistro').show()");
      }

   }

   public void revisarDialogoGuardar() {

      if (!borrarVigenciaEstadoCivilPorEmplado.isEmpty() || !crearVigenciaEstadoCivilPorEmplado.isEmpty() || !modificarVigenciaEstadoCivilPorEmplado.isEmpty()) {
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:confirmarGuardar");
         RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
      }
   }

   public void guardarYSalir() {
      guardarVigenciaEstadoCivil();
      salir();
   }

   public void guardarVigenciaEstadoCivil() {
      if (guardado == false) {
         log.info("Realizando guardarEvalCompetencias");
         if (!borrarVigenciaEstadoCivilPorEmplado.isEmpty()) {
            administrarVigenciaEstadosCiviles.borrarVigenciasEstadosCiviles(borrarVigenciaEstadoCivilPorEmplado);
            //mostrarBorrados
            registrosBorrados = borrarVigenciaEstadoCivilPorEmplado.size();
            RequestContext.getCurrentInstance().update("form:mostrarBorrados");
            RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
            borrarVigenciaEstadoCivilPorEmplado.clear();
         }
         if (!crearVigenciaEstadoCivilPorEmplado.isEmpty()) {
            administrarVigenciaEstadosCiviles.crearVigenciasEstadosCiviles(crearVigenciaEstadoCivilPorEmplado);
            crearVigenciaEstadoCivilPorEmplado.clear();
         }
         if (!modificarVigenciaEstadoCivilPorEmplado.isEmpty()) {
            log.info("Modificando...");
            administrarVigenciaEstadosCiviles.modificarVigenciasEstadosCiviles(modificarVigenciaEstadoCivilPorEmplado);
            modificarVigenciaEstadoCivilPorEmplado.clear();
         }
         listVigenciaEstadoCivilPorEmpleado = null;
         getListVigenciaEstadoCivilPorEmpleado();
         contarRegistros();
         RequestContext.getCurrentInstance().update("form:datosEstadoCivil");
         k = 0;
         guardado = true;
         FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      }
      vigenciaSeleccionada = null;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");

   }

   public void editarCelda() {
      if (vigenciaSeleccionada != null) {
         if (tipoLista == 0) {
            editarVigenciaEstadoCivil = vigenciaSeleccionada;
         }
         if (tipoLista == 1) {
            editarVigenciaEstadoCivil = vigenciaSeleccionada;
         }

         RequestContext context = RequestContext.getCurrentInstance();
         if (cualCelda == 0) {
            deshabilitarBotonLov();
            RequestContext.getCurrentInstance().update("formularioDialogos:editarFecha");
            RequestContext.getCurrentInstance().execute("PF('editarFecha').show()");
            cualCelda = -1;
         } else if (cualCelda == 1) {
            habilitarBotonLov();
            RequestContext.getCurrentInstance().update("formularioDialogos:editEstadoCivil");
            RequestContext.getCurrentInstance().execute("PF('editEstadoCivil').show()");
            cualCelda = -1;
         }

      } else {
         RequestContext.getCurrentInstance().execute("PF('formularioDialogos:seleccionarRegistro').show()");
      }
   }

   public void agregarNuevoVigenciaEstadoCivil() {
      log.info("agregarNuevoVigenciaEstadoCivil");
      int contador = 0;
      //nuevoVigenciaEstadoCivil.setEstadocivil(new EstadosCiviles());
      Short a = 0;
      a = null;
      int fechas = 0;
      mensajeValidacion = " ";
      try {
         nuevoVigenciaEstadoCivil.setPersona(administrarVigenciaEstadosCiviles.obtenerPersonaPorEmpleado(empleadoSeleccionado.getSecuencia()));
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".confirmarDuplicar() ERROR:  ", e);
      }
      if (nuevoVigenciaEstadoCivil.getFechavigencia() == null || nuevoVigenciaEstadoCivil.getFechavigencia().equals("")) {
         mensajeValidacion = "Campo Fecha vacío \n";
      } else {
         for (int i = 0; i < listVigenciaEstadoCivilPorEmpleado.size(); i++) {
            if (nuevoVigenciaEstadoCivil.getFechavigencia().equals(listVigenciaEstadoCivilPorEmpleado.get(i).getFechavigencia())) {
               fechas++;
            }
         }
         if (fechas > 0) {
            mensajeValidacion = "Fechas repetidas ";
         } else {
            contador++;
         }
      }
      if (nuevoVigenciaEstadoCivil.getEstadocivil().getSecuencia() == null || nuevoVigenciaEstadoCivil.getEstadocivil().getDescripcion().isEmpty()) {
         mensajeValidacion = "Campo Estado Civil vacío\n";
      } else {
         log.info("Bandera : ");
         contador++;
      }

      log.info("contador " + contador);

      if (contador == 2) {
         if (bandera == 1) {
            //CERRAR FILTRADO
            FacesContext c = FacesContext.getCurrentInstance();
            log.info("Desactivar");
            fecha = (Column) c.getViewRoot().findComponent("form:datosEstadoCivil:fecha");
            fecha.setFilterStyle("display: none; visibility: hidden;");
            parentesco = (Column) c.getViewRoot().findComponent("form:datosEstadoCivil:parentesco");
            parentesco.setFilterStyle("display: none; visibility: hidden;");
            altoTabla = "270";
            RequestContext.getCurrentInstance().update("form:datosEstadoCivil");
            bandera = 0;
            filtrarVigenciaEstadoCivilPorEmplado = null;
            tipoLista = 0;
         }
         log.info("Despues de la bandera");

         k++;
         l = BigInteger.valueOf(k);
         nuevoVigenciaEstadoCivil.setSecuencia(l);
         crearVigenciaEstadoCivilPorEmplado.add(nuevoVigenciaEstadoCivil);
         listVigenciaEstadoCivilPorEmpleado.add(nuevoVigenciaEstadoCivil);
         contarRegistros();
         vigenciaSeleccionada = nuevoVigenciaEstadoCivil;
         nuevoVigenciaEstadoCivil = new VigenciasEstadosCiviles();
         nuevoVigenciaEstadoCivil.setEstadocivil(new EstadosCiviles());
         RequestContext.getCurrentInstance().update("form:datosEstadoCivil");
         RequestContext.getCurrentInstance().update("form:infoRegistro");
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }

         RequestContext.getCurrentInstance().execute("PF('nuevoRegistroEstadoCivil').hide()");

      } else {
         RequestContext.getCurrentInstance().update("form:validacionNuevoEstadoCivil");
         RequestContext.getCurrentInstance().execute("PF('validacionNuevoEstadoCivil').show()");
         contador = 0;
      }
   }

   public void limpiarNuevoVigenciaEstadoCivil() {
      log.info("limpiarNuevoVigenciaEstadoCivil");

      nuevoVigenciaEstadoCivil = new VigenciasEstadosCiviles();
      nuevoVigenciaEstadoCivil.setEstadocivil(new EstadosCiviles());
      nuevoVigenciaEstadoCivil.setFechavigencia(new Date());

   }

   //------------------------------------------------------------------------------
   public void duplicandoVigenciaEstadoCivil() {
      if (vigenciaSeleccionada != null) {
         log.info("duplicandoVigenciaEstadoCivil");
         duplicarVigenciaEstadoCivil = new VigenciasEstadosCiviles();
         duplicarVigenciaEstadoCivil.setPersona(new Personas());
         duplicarVigenciaEstadoCivil.setEstadocivil(new EstadosCiviles());
         k++;
         l = BigInteger.valueOf(k);

         if (tipoLista == 0) {
            duplicarVigenciaEstadoCivil.setSecuencia(l);
            duplicarVigenciaEstadoCivil.setPersona(vigenciaSeleccionada.getPersona());
            duplicarVigenciaEstadoCivil.setFechavigencia(vigenciaSeleccionada.getFechavigencia());
            duplicarVigenciaEstadoCivil.setEstadocivil(vigenciaSeleccionada.getEstadocivil());
         }
         if (tipoLista == 1) {
            duplicarVigenciaEstadoCivil.setSecuencia(l);
            duplicarVigenciaEstadoCivil.setPersona(vigenciaSeleccionada.getPersona());
            duplicarVigenciaEstadoCivil.setFechavigencia(vigenciaSeleccionada.getFechavigencia());
            duplicarVigenciaEstadoCivil.setEstadocivil(vigenciaSeleccionada.getEstadocivil());
            altoTabla = "270";
         }

         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarEvC");
         RequestContext.getCurrentInstance().execute("PF('duplicarRegistroEstadoCivil').show()");

      } else {
         RequestContext.getCurrentInstance().execute("PF('formularioDialogos:seleccionarRegistro').show()");
      }
   }

   public void confirmarDuplicar() {
      int contador = 0;
      mensajeValidacion = " ";
      RequestContext context = RequestContext.getCurrentInstance();
      Short a = 0;
      int fechas = 0;
      a = null;
      if (duplicarVigenciaEstadoCivil.getFechavigencia() == null) {
         mensajeValidacion = mensajeValidacion + "   * Fecha \n";
         log.info("Mensaje validacion : " + mensajeValidacion);
      } else {

         for (int j = 0; j < listVigenciaEstadoCivilPorEmpleado.size(); j++) {
            if (duplicarVigenciaEstadoCivil.getFechavigencia().equals(listVigenciaEstadoCivilPorEmpleado.get(j).getFechavigencia())) {
               log.info("Se repiten");
               fechas++;
            }
         }
         if (fechas > 0) {
            mensajeValidacion = "FECHAS REPETIDAS";
         } else {
            log.info("bandera");
            contador++;
         }

      }
      if (duplicarVigenciaEstadoCivil.getEstadocivil().getDescripcion() == null || duplicarVigenciaEstadoCivil.getEstadocivil().getDescripcion().isEmpty() || duplicarVigenciaEstadoCivil.getEstadocivil().getDescripcion().equals(" ") || duplicarVigenciaEstadoCivil.getEstadocivil().getDescripcion().isEmpty()) {
         mensajeValidacion = mensajeValidacion + "   * Estado Civil \n";
         log.info("Mensaje validacion : " + mensajeValidacion);
      } else {
         log.info("bandera");
         contador++;

      }
      if (duplicarVigenciaEstadoCivil.getPersona().getSecuencia() == null) {
         try {
            duplicarVigenciaEstadoCivil.setPersona(administrarVigenciaEstadosCiviles.obtenerPersonaPorEmpleado(empleadoSeleccionado.getSecuencia()));
         } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".confirmarDuplicar() ERROR:  ", e);
         }
      }
      if (contador == 2) {
         k++;
         l = BigInteger.valueOf(k);
         duplicarVigenciaEstadoCivil.setSecuencia(l);
         log.info("Datos Duplicando: " + duplicarVigenciaEstadoCivil.getSecuencia() + "  " + duplicarVigenciaEstadoCivil.getFechavigencia());
         if (crearVigenciaEstadoCivilPorEmplado.contains(duplicarVigenciaEstadoCivil)) {
            log.info("Ya lo contengo.");
         }
         listVigenciaEstadoCivilPorEmpleado.add(duplicarVigenciaEstadoCivil);
         crearVigenciaEstadoCivilPorEmplado.add(duplicarVigenciaEstadoCivil);
         RequestContext.getCurrentInstance().update("form:datosEstadoCivil");
         vigenciaSeleccionada = duplicarVigenciaEstadoCivil;
         if (guardado == true) {
            guardado = false;
         }
         contarRegistros();
         RequestContext.getCurrentInstance().update("form:infoRegistro");
         if (bandera == 1) {
            //CERRAR FILTRADO
            FacesContext c = FacesContext.getCurrentInstance();
            fecha = (Column) c.getViewRoot().findComponent("form:datosEstadoCivil:fecha");
            fecha.setFilterStyle("display: none; visibility: hidden;");
            parentesco = (Column) c.getViewRoot().findComponent("form:datosEstadoCivil:parentesco");
            parentesco.setFilterStyle("display: none; visibility: hidden;");
            altoTabla = "270";
            RequestContext.getCurrentInstance().update("form:datosEstadoCivil");
            bandera = 0;
            filtrarVigenciaEstadoCivilPorEmplado = null;
            tipoLista = 0;
         }
         duplicarVigenciaEstadoCivil = new VigenciasEstadosCiviles();
         RequestContext.getCurrentInstance().execute("PF('duplicarRegistroEstadoCivil').hide()");

      } else {
         contador = 0;
         fechas = 0;
         RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
         RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
      }
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void limpiarDuplicarVigenciaEstadoCivil() {
      duplicarVigenciaEstadoCivil = new VigenciasEstadosCiviles();
      duplicarVigenciaEstadoCivil.setPersona(new Personas());
      duplicarVigenciaEstadoCivil.setEstadocivil(new EstadosCiviles());
   }

   public void exportPDF() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosEstadoCivilExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "VIGENCIASVIGESTADOSCIVILES", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void exportXLS() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosEstadoCivilExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "VIGENCIASVIGESTADOSCIVILES", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void verificarRastro() {
      RequestContext context = RequestContext.getCurrentInstance();
      log.info("lol");
      if (vigenciaSeleccionada != null) {
         log.info("lol 2");
         int resultado = administrarRastros.obtenerTabla(vigenciaSeleccionada.getSecuencia(), "VIGENCIASESTADOSCIVILES"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
      } else if (administrarRastros.verificarHistoricosTabla("VIGENCIASESTADOSCIVILES")) { // igual acá
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
   }

   public void recordarSeleccion() {
      if (vigenciaSeleccionada != null) {
         FacesContext c = FacesContext.getCurrentInstance();
         tablaC = (DataTable) c.getViewRoot().findComponent("form:datosEstadoCivil");
         tablaC.setSelection(vigenciaSeleccionada);
      }
   }

   public void eventoFiltrar() {

      if (tipoLista == 0) {
         tipoLista = 1;
      }
      contarRegistros();
   }

   public void habilitarBotonLov() {
      activarLov = false;
      RequestContext.getCurrentInstance().update("form:listaValores");
   }

   public void deshabilitarBotonLov() {
      activarLov = true;
      RequestContext.getCurrentInstance().update("form:listaValores");
   }

   public void contarRegistroEC() {
      RequestContext.getCurrentInstance().update("form:infoRegistroEC");
   }

   public void contarRegistros() {
      RequestContext.getCurrentInstance().update("form:infoRegistro");
   }

   //*/*/*/*/*/*/*/*/*/*-/-*//-*/-*/*/*-*/-*/-*/*/*/*/*/---/*/*/*/*/-*/-*/-*/-*/-*/
   public List<VigenciasEstadosCiviles> getListVigenciaEstadoCivilPorEmpleado() {
      if (listVigenciaEstadoCivilPorEmpleado == null) {
         listVigenciaEstadoCivilPorEmpleado = administrarVigenciaEstadosCiviles.consultarVigenciasEstadosCivilesPorEmpleado(secuenciaPersona);
      }
      return listVigenciaEstadoCivilPorEmpleado;
   }

   public void setListVigenciaEstadoCivilPorEmpleado(List<VigenciasEstadosCiviles> listVigenciaEstadoCivilPorEmpleado) {
      this.listVigenciaEstadoCivilPorEmpleado = listVigenciaEstadoCivilPorEmpleado;
   }

   public List<VigenciasEstadosCiviles> getFiltrarVigenciaEstadoCivilPorEmplado() {
      return filtrarVigenciaEstadoCivilPorEmplado;
   }

   public void setFiltrarVigenciaEstadoCivilPorEmplado(List<VigenciasEstadosCiviles> filtrarVigenciaEstadoCivilPorEmplado) {
      this.filtrarVigenciaEstadoCivilPorEmplado = filtrarVigenciaEstadoCivilPorEmplado;
   }

   public VigenciasEstadosCiviles getNuevoVigenciaEstadoCivil() {
      return nuevoVigenciaEstadoCivil;
   }

   public void setNuevoVigenciaEstadoCivil(VigenciasEstadosCiviles nuevoVigenciaEstadoCivil) {
      this.nuevoVigenciaEstadoCivil = nuevoVigenciaEstadoCivil;
   }

   public VigenciasEstadosCiviles getDuplicarVigenciaEstadoCivil() {
      return duplicarVigenciaEstadoCivil;
   }

   public void setDuplicarVigenciaEstadoCivil(VigenciasEstadosCiviles duplicarVigenciaEstadoCivil) {
      this.duplicarVigenciaEstadoCivil = duplicarVigenciaEstadoCivil;
   }

   public VigenciasEstadosCiviles getEditarVigenciaEstadoCivil() {
      return editarVigenciaEstadoCivil;
   }

   public void setEditarVigenciaEstadoCivil(VigenciasEstadosCiviles editarVigenciaEstadoCivil) {
      this.editarVigenciaEstadoCivil = editarVigenciaEstadoCivil;
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
         empleadoSeleccionado = administrarVigenciaEstadosCiviles.consultarEmpleado(secuenciaEmpleado);
      }
      return empleadoSeleccionado;
   }

   public void setEmpleadoSeleccionado(Empleados empleadoSeleccionado) {
      this.empleadoSeleccionado = empleadoSeleccionado;
   }

   public List<EstadosCiviles> getLovEstadosCiviles() {
      if (lovEstadosCiviles == null) {
         lovEstadosCiviles = administrarVigenciaEstadosCiviles.lovEstadosCiviles();
      }
      return lovEstadosCiviles;
   }

   public void setLovEstadosCiviles(List<EstadosCiviles> lovEstadosCiviles) {
      this.lovEstadosCiviles = lovEstadosCiviles;
   }

   public List<EstadosCiviles> getFiltradoEstadosCiviles() {
      return filtradoEstadosCiviles;
   }

   public void setFiltradoEstadosCiviles(List<EstadosCiviles> filtradoEstadosCiviles) {
      this.filtradoEstadosCiviles = filtradoEstadosCiviles;
   }

   public EstadosCiviles getVigenciaEstadoCivilSeleccionada() {
      return vigenciaEstadoCivilSeleccionada;
   }

   public void setVigenciaEstadoCivilSeleccionada(EstadosCiviles vigenciaEstadoCivilSeleccionada) {
      this.vigenciaEstadoCivilSeleccionada = vigenciaEstadoCivilSeleccionada;
   }

   public VigenciasEstadosCiviles getVigenciaSeleccionada() {
      return vigenciaSeleccionada;
   }

   public void setVigenciaSeleccionada(VigenciasEstadosCiviles vigenciaSeleccionada) {
      this.vigenciaSeleccionada = vigenciaSeleccionada;
   }

   public boolean isGuardado() {
      return guardado;
   }

   public void setGuardado(boolean guardado) {
      this.guardado = guardado;
   }

   public String getAltoTabla() {
      return altoTabla;
   }

   public String getInfoRegistro() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosEstadoCivil");
      infoRegistro = String.valueOf(tabla.getRowCount());
      return infoRegistro;
   }

   public void setInfoRegistro(String infoRegistro) {
      this.infoRegistro = infoRegistro;
   }

   public boolean isAceptar() {
      return aceptar;
   }

   public void setAceptar(boolean aceptar) {
      this.aceptar = aceptar;
   }

   public String getInfoRegistroEC() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovTiposFamiliares");
      infoRegistroEC = String.valueOf(tabla.getRowCount());
      return infoRegistroEC;
   }

   public void setInfoRegistroEC(String infoRegistroEC) {
      this.infoRegistroEC = infoRegistroEC;
   }

   public boolean isActivarLov() {
      return activarLov;
   }

   public void setActivarLov(boolean activarLov) {
      this.activarLov = activarLov;
   }

}
