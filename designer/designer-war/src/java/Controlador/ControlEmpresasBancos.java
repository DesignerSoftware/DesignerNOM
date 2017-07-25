/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Ciudades;
import Entidades.EmpresasBancos;
import Entidades.Empresas;
import Entidades.Bancos;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarEmpresasBancosInterface;
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
public class ControlEmpresasBancos implements Serializable {

   private static Logger log = Logger.getLogger(ControlEmpresasBancos.class);

   @EJB
   AdministrarEmpresasBancosInterface administrarEmpresasBancos;
   @EJB
   AdministrarRastrosInterface administrarRastros;

   private List<EmpresasBancos> listEmpresasBancos;
   private List<EmpresasBancos> filtrarEmpresasBancos;
   private List<EmpresasBancos> crearEmpresasBancos;
   private List<EmpresasBancos> modificarEmpresasBancos;
   private List<EmpresasBancos> borrarEmpresasBancos;
   private EmpresasBancos nuevoEmpresasBancos;
   private EmpresasBancos duplicarEmpresasBancos;
   private EmpresasBancos editarEmpresasBancos;
   private EmpresasBancos empresasBancoSeleccionado;
   //otros
   private int cualCelda, tipoLista, tipoActualizacion, k, bandera;
   private BigInteger l;
   private boolean aceptar, guardado;
   //AutoCompletar
   private boolean permitirIndex;
   //RASTRO
   private Column obsoleto, pais, subTituloFirma, personafir, cargo;
   //borrado
   private int registrosBorrados;
   private String mensajeValidacion;
   //filtrado table
   private int tamano;
   private List<Empresas> lovEmpresas;
   private List<Empresas> filtradoEmpresas;
   private Empresas empresaSeleccionado;
   private String nuevoYduplicarCompletarEmpresa;
   //---------------------------------
   private List<Bancos> lovBancos;
   private List<Bancos> filtradoBancos;
   private Bancos bancoSeleccionado;
   private String nuevoYduplicarCompletarPersona;
   //--------------------------------------
   private List<Ciudades> lovCiudades;
   private List<Ciudades> filtradoCiudades;
   private Ciudades ciudadSeleccionada;
   private String nuevoYduplicarCompletarCargo;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();
   private String infoRegistro;
   private String infoRegistroEmpresas;
   private String infoRegistroCiudades;
   private String infoRegistrosBancos;
   private boolean activarLov;

   public ControlEmpresasBancos() {
      listEmpresasBancos = null;
      crearEmpresasBancos = new ArrayList<EmpresasBancos>();
      modificarEmpresasBancos = new ArrayList<EmpresasBancos>();
      borrarEmpresasBancos = new ArrayList<EmpresasBancos>();
      permitirIndex = true;
      editarEmpresasBancos = new EmpresasBancos();
      nuevoEmpresasBancos = new EmpresasBancos();
      nuevoEmpresasBancos.setEmpresa(new Empresas());
      nuevoEmpresasBancos.setBanco(new Bancos());
      nuevoEmpresasBancos.setCiudad(new Ciudades());
      duplicarEmpresasBancos = new EmpresasBancos();
      duplicarEmpresasBancos.setEmpresa(new Empresas());
      duplicarEmpresasBancos.setBanco(new Bancos());
      duplicarEmpresasBancos.setCiudad(new Ciudades());
      lovEmpresas = null;
      filtradoEmpresas = null;
      lovBancos = null;
      filtradoBancos = null;
      lovCiudades = null;
      filtradoCiudades = null;
      guardado = true;
      tamano = 320;
      aceptar = true;
      mapParametros.put("paginaAnterior", paginaAnterior);
      activarLov = true;
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
      String pagActual = "empresabanco";
      if (pag.equals("atras")) {
         pag = paginaAnterior;
         paginaAnterior = "nominaf";
         controlListaNavegacion.quitarPagina(pagActual);
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
      lovBancos = null;
      lovCiudades = null;
      lovEmpresas = null;
   }

   @PostConstruct
   public void inicializarAdministrador() {
      try {
         FacesContext x = FacesContext.getCurrentInstance();
         HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
         administrarEmpresasBancos.obtenerConexion(ses.getId());
         administrarRastros.obtenerConexion(ses.getId());
      } catch (Exception e) {
         log.error("Error postconstruct " + this.getClass().getName() + ": " + e);
         log.error("Causa: " + e.getCause());
      }
   }

   public void eventoFiltrar() {
      if (tipoLista == 0) {
         tipoLista = 1;
      }
      contarRegistros();
   }

   public void cambiarIndice(EmpresasBancos empresab, int celda) {
      if (permitirIndex == true) {
         empresasBancoSeleccionado = empresab;
         cualCelda = celda;
         empresasBancoSeleccionado.getSecuencia();
         if (cualCelda == 0) {
            habilitarLov();
            empresasBancoSeleccionado.getEmpresa().getNombre();
         }
         if (cualCelda == 1) {
            habilitarLov();
            empresasBancoSeleccionado.getBanco().getNombre();
         }
         if (cualCelda == 2) {
            habilitarLov();
            empresasBancoSeleccionado.getCiudad().getNombre();
         }
         if (cualCelda == 3) {
            deshabilitarLov();
            empresasBancoSeleccionado.getNumerocuenta();
         }
      }
   }

   public void asignarIndex(EmpresasBancos empresaB, int LND, int dig) {
      empresasBancoSeleccionado = empresaB;
      tipoActualizacion = LND;
      if (dig == 0) {
         lovEmpresas = null;
         getLovEmpresas();
         contarRegistrosEmpresas();
         RequestContext.getCurrentInstance().update("form:empresasDialogo");
         RequestContext.getCurrentInstance().execute("PF('empresasDialogo').show()");
         dig = -1;
      } else if (dig == 1) {
         lovBancos = null;
         getLovBancos();
         contarRegistrosBancos();
         RequestContext.getCurrentInstance().update("form:bancosDialogo");
         RequestContext.getCurrentInstance().execute("PF('bancosDialogo').show()");
         dig = -1;
      } else if (dig == 2) {
         lovCiudades = null;
         getLovCiudades();
         contarRegistrosCiudades();
         RequestContext.getCurrentInstance().update("form:ciudadesDialogo");
         RequestContext.getCurrentInstance().execute("PF('ciudadesDialogo').show()");
         dig = -1;
      }
   }

   public void activarAceptar() {
      aceptar = false;
   }

   public void listaValoresBoton() {
      if (empresasBancoSeleccionado != null) {
         if (cualCelda == 0) {
            lovEmpresas = null;
            getLovEmpresas();
            contarRegistrosEmpresas();
            RequestContext.getCurrentInstance().update("form:empresasDialogo");
            RequestContext.getCurrentInstance().execute("PF('empresasDialogo').show()");
         }
         if (cualCelda == 1) {
            lovBancos = null;
            getLovBancos();
            contarRegistrosBancos();
            RequestContext.getCurrentInstance().update("form:bancosDialogo");
            RequestContext.getCurrentInstance().execute("PF('bancosDialogo').show()");
         }
         if (cualCelda == 2) {
            lovCiudades = null;
            getLovCiudades();
            contarRegistrosCiudades();
            RequestContext.getCurrentInstance().update("form:ciudadesDialogo");
            RequestContext.getCurrentInstance().execute("PF('ciudadesDialogo').show()");
         }
      }
   }

   public void cancelarModificacion() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 1) {
         obsoleto = (Column) c.getViewRoot().findComponent("form:datosEmpresasBancos:obsoleto");
         obsoleto.setFilterStyle("display: none; visibility: hidden;");
         pais = (Column) c.getViewRoot().findComponent("form:datosEmpresasBancos:pais");
         pais.setFilterStyle("display: none; visibility: hidden;");
         subTituloFirma = (Column) c.getViewRoot().findComponent("form:datosEmpresasBancos:subTituloFirma");
         subTituloFirma.setFilterStyle("display: none; visibility: hidden;");
         personafir = (Column) c.getViewRoot().findComponent("form:datosEmpresasBancos:personafir");
         personafir.setFilterStyle("display: none; visibility: hidden;");
         cargo = (Column) c.getViewRoot().findComponent("form:datosEmpresasBancos:cargo");
         cargo.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosEmpresasBancos");
         bandera = 0;
         filtrarEmpresasBancos = null;
         tipoLista = 0;
      }

      borrarEmpresasBancos.clear();
      crearEmpresasBancos.clear();
      modificarEmpresasBancos.clear();
      empresasBancoSeleccionado = null;
      k = 0;
      listEmpresasBancos = null;
      guardado = true;
      permitirIndex = true;
      contarRegistros();
      RequestContext.getCurrentInstance().update("form:datosEmpresasBancos");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void salir() {
      limpiarListasValor();
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 1) {
         //CERRAR FILTRADO
         obsoleto = (Column) c.getViewRoot().findComponent("form:datosEmpresasBancos:obsoleto");
         obsoleto.setFilterStyle("display: none; visibility: hidden;");
         pais = (Column) c.getViewRoot().findComponent("form:datosEmpresasBancos:pais");
         pais.setFilterStyle("display: none; visibility: hidden;");
         subTituloFirma = (Column) c.getViewRoot().findComponent("form:datosEmpresasBancos:subTituloFirma");
         subTituloFirma.setFilterStyle("display: none; visibility: hidden;");
         personafir = (Column) c.getViewRoot().findComponent("form:datosEmpresasBancos:personafir");
         personafir.setFilterStyle("display: none; visibility: hidden;");
         cargo = (Column) c.getViewRoot().findComponent("form:datosEmpresasBancos:cargo");
         cargo.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosEmpresasBancos");
         bandera = 0;
         filtrarEmpresasBancos = null;
         tipoLista = 0;
      }

      borrarEmpresasBancos.clear();
      crearEmpresasBancos.clear();
      modificarEmpresasBancos.clear();
      empresasBancoSeleccionado = null;
      empresasBancoSeleccionado = null;
      k = 0;
      listEmpresasBancos = null;
      guardado = true;
      permitirIndex = true;
      contarRegistros();
      RequestContext.getCurrentInstance().update("form:datosEmpresasBancos");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void activarCtrlF11() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 0) {
         tamano = 300;
         obsoleto = (Column) c.getViewRoot().findComponent("form:datosEmpresasBancos:obsoleto");
         obsoleto.setFilterStyle("width: 85% !important;");
         pais = (Column) c.getViewRoot().findComponent("form:datosEmpresasBancos:pais");
         pais.setFilterStyle("width: 85% !important;");
         subTituloFirma = (Column) c.getViewRoot().findComponent("form:datosEmpresasBancos:subTituloFirma");
         subTituloFirma.setFilterStyle("width: 85% !important;");
         personafir = (Column) c.getViewRoot().findComponent("form:datosEmpresasBancos:personafir");
         personafir.setFilterStyle("width: 85% !important;");
         cargo = (Column) c.getViewRoot().findComponent("form:datosEmpresasBancos:cargo");
         cargo.setFilterStyle("width: 85% !important;");
         RequestContext.getCurrentInstance().update("form:datosEmpresasBancos");
         log.info("Activar");
         bandera = 1;
      } else if (bandera == 1) {
         log.info("Desactivar");
         tamano = 320;
         obsoleto = (Column) c.getViewRoot().findComponent("form:datosEmpresasBancos:obsoleto");
         obsoleto.setFilterStyle("display: none; visibility: hidden;");
         pais = (Column) c.getViewRoot().findComponent("form:datosEmpresasBancos:pais");
         pais.setFilterStyle("display: none; visibility: hidden;");
         subTituloFirma = (Column) c.getViewRoot().findComponent("form:datosEmpresasBancos:subTituloFirma");
         subTituloFirma.setFilterStyle("display: none; visibility: hidden;");
         personafir = (Column) c.getViewRoot().findComponent("form:datosEmpresasBancos:personafir");
         personafir.setFilterStyle("display: none; visibility: hidden;");
         cargo = (Column) c.getViewRoot().findComponent("form:datosEmpresasBancos:cargo");
         cargo.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosEmpresasBancos");
         bandera = 0;
         filtrarEmpresasBancos = null;
         tipoLista = 0;
      }
   }

   public void actualizarCiudad() {
      if (tipoActualizacion == 0) {
         empresasBancoSeleccionado.setCiudad(ciudadSeleccionada);

         if (!crearEmpresasBancos.contains(empresasBancoSeleccionado)) {
            if (modificarEmpresasBancos.isEmpty()) {
               modificarEmpresasBancos.add(empresasBancoSeleccionado);
            } else if (!modificarEmpresasBancos.contains(empresasBancoSeleccionado)) {
               modificarEmpresasBancos.add(empresasBancoSeleccionado);
            }
         }
         guardado = false;
         permitirIndex = true;
         RequestContext.getCurrentInstance().update("form:datosEmpresasBancos");
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      } else if (tipoActualizacion == 1) {
         nuevoEmpresasBancos.setCiudad(ciudadSeleccionada);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaCiudad");
      } else if (tipoActualizacion == 2) {
         duplicarEmpresasBancos.setCiudad(ciudadSeleccionada);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCiudad");
      }
      filtradoCiudades = null;
      ciudadSeleccionada = null;
      aceptar = true;

      RequestContext.getCurrentInstance().update("form:ciudadesDialogo");
      RequestContext.getCurrentInstance().update("form:lovCiudades");
      RequestContext.getCurrentInstance().update("form:aceptarCar");
      RequestContext.getCurrentInstance().reset("form:lovCiudades:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovCiudades').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('ciudadesDialogo').hide()");
   }

   public void cancelarCambioCiudad() {
      filtradoCiudades = null;
      ciudadSeleccionada = null;
      aceptar = true;
      RequestContext.getCurrentInstance().update("form:ciudadesDialogo");
      RequestContext.getCurrentInstance().update("form:lovCiudades");
      RequestContext.getCurrentInstance().update("form:aceptarCar");
      RequestContext.getCurrentInstance().reset("form:lovCiudades:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovCiudades').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('ciudadesDialogo').hide()");
   }

   public void actualizarBancos() {
      if (tipoActualizacion == 0) {
         empresasBancoSeleccionado.setBanco(bancoSeleccionado);
         if (!crearEmpresasBancos.contains(empresasBancoSeleccionado)) {
            if (modificarEmpresasBancos.isEmpty()) {
               modificarEmpresasBancos.add(empresasBancoSeleccionado);
            } else if (!modificarEmpresasBancos.contains(empresasBancoSeleccionado)) {
               modificarEmpresasBancos.add(empresasBancoSeleccionado);
            }
         }
         guardado = false;
         permitirIndex = true;
         RequestContext.getCurrentInstance().update("form:datosEmpresasBancos");
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      } else if (tipoActualizacion == 1) {
         nuevoEmpresasBancos.setBanco(bancoSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevoPersona");
      } else if (tipoActualizacion == 2) {
         duplicarEmpresasBancos.setBanco(bancoSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarPersona");
      }
      filtradoBancos = null;
      bancoSeleccionado = null;
      aceptar = true;

      RequestContext.getCurrentInstance().update("form:bancosDialogo");
      RequestContext.getCurrentInstance().update("form:lovBancos");
      RequestContext.getCurrentInstance().update("form:aceptarPer");
      RequestContext.getCurrentInstance().reset("form:lovBancos:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovBancos').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('bancosDialogo').hide()");
   }

   public void cancelarCambioBancos() {
      filtradoBancos = null;
      bancoSeleccionado = null;
      aceptar = true;
      RequestContext.getCurrentInstance().update("form:bancosDialogo");
      RequestContext.getCurrentInstance().update("form:lovBancos");
      RequestContext.getCurrentInstance().update("form:aceptarPer");
      RequestContext.getCurrentInstance().reset("form:lovBancos:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovBancos').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('bancosDialogo').hide()");
   }

   public void actualizarEmpresa() {
      if (tipoActualizacion == 0) {
         empresasBancoSeleccionado.setEmpresa(empresaSeleccionado);
         if (!crearEmpresasBancos.contains(empresasBancoSeleccionado)) {
            if (modificarEmpresasBancos.isEmpty()) {
               modificarEmpresasBancos.add(empresasBancoSeleccionado);
            } else if (!modificarEmpresasBancos.contains(empresasBancoSeleccionado)) {
               modificarEmpresasBancos.add(empresasBancoSeleccionado);
            }
         }
         guardado = false;
         permitirIndex = true;
         RequestContext.getCurrentInstance().update("form:datosEmpresasBancos");
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      } else if (tipoActualizacion == 1) {
         nuevoEmpresasBancos.setEmpresa(empresaSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaEmpresa");
      } else if (tipoActualizacion == 2) {
         duplicarEmpresasBancos.setEmpresa(empresaSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarEmpresa");
      }
      filtradoEmpresas = null;
      empresaSeleccionado = null;
      aceptar = true;

      RequestContext.getCurrentInstance().update("form:empresasDialogo");
      RequestContext.getCurrentInstance().update("form:lovEmpresas");
      RequestContext.getCurrentInstance().update("form:aceptarS");
      RequestContext.getCurrentInstance().reset("form:lovEmpresas:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovEmpresas').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('empresasDialogo').hide()");
   }

   public void cancelarCambioEmpresa() {
      filtradoEmpresas = null;
      empresaSeleccionado = null;
      aceptar = true;
      RequestContext.getCurrentInstance().update("form:empresasDialogo");
      RequestContext.getCurrentInstance().update("form:lovEmpresas");
      RequestContext.getCurrentInstance().update("form:aceptarS");
      RequestContext.getCurrentInstance().reset("form:lovEmpresas:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovEmpresas').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('empresasDialogo').hide()");
   }

   public void modificarEmpresasBancos(EmpresasBancos empresab) {
      empresasBancoSeleccionado = empresab;
      if (!crearEmpresasBancos.contains(empresasBancoSeleccionado)) {
         if (modificarEmpresasBancos.isEmpty()) {
            modificarEmpresasBancos.add(empresasBancoSeleccionado);
         } else if (!modificarEmpresasBancos.contains(empresasBancoSeleccionado)) {
            modificarEmpresasBancos.add(empresasBancoSeleccionado);
         }
      }
      guardado = false;
      RequestContext.getCurrentInstance().update("form:datosEmpresasBancos");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void borrandoEmpresasBancos() {

      if (empresasBancoSeleccionado != null) {
         if (tipoLista == 0) {
            log.info("Entro a borrandoEmpresasBancos");
            if (!modificarEmpresasBancos.isEmpty() && modificarEmpresasBancos.contains(empresasBancoSeleccionado)) {
               int modIndex = modificarEmpresasBancos.indexOf(empresasBancoSeleccionado);
               modificarEmpresasBancos.remove(modIndex);
               borrarEmpresasBancos.add(empresasBancoSeleccionado);
            } else if (!crearEmpresasBancos.isEmpty() && crearEmpresasBancos.contains(empresasBancoSeleccionado)) {
               int crearIndex = crearEmpresasBancos.indexOf(empresasBancoSeleccionado);
               crearEmpresasBancos.remove(crearIndex);
            } else {
               borrarEmpresasBancos.add(empresasBancoSeleccionado);
            }
            listEmpresasBancos.remove(empresasBancoSeleccionado);
         }
         if (tipoLista == 1) {
            filtrarEmpresasBancos.remove(empresasBancoSeleccionado);
         }
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:datosEmpresasBancos");
         contarRegistros();
         empresasBancoSeleccionado = null;
         guardado = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      }

   }

   public void revisarDialogoGuardar() {
      if (!borrarEmpresasBancos.isEmpty() || !crearEmpresasBancos.isEmpty() || !modificarEmpresasBancos.isEmpty()) {
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:confirmarGuardar");
         RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
      }

   }

   public void guardarEmpresasBancos() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (guardado == false) {
         if (!borrarEmpresasBancos.isEmpty()) {
            administrarEmpresasBancos.borrarEmpresasBancos(borrarEmpresasBancos);
            //mostrarBorrados
            registrosBorrados = borrarEmpresasBancos.size();
            RequestContext.getCurrentInstance().update("form:mostrarBorrados");
            RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
            borrarEmpresasBancos.clear();
         }
         if (!modificarEmpresasBancos.isEmpty()) {
            administrarEmpresasBancos.modificarEmpresasBancos(modificarEmpresasBancos);
            modificarEmpresasBancos.clear();
         }
         if (!crearEmpresasBancos.isEmpty()) {
            administrarEmpresasBancos.crearEmpresasBancos(crearEmpresasBancos);
            crearEmpresasBancos.clear();
         }
         log.info("Se guardaron los datos con exito");
         listEmpresasBancos = null;
         FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
         RequestContext.getCurrentInstance().update("form:datosEmpresasBancos");
         k = 0;
         guardado = true;
      }
      empresasBancoSeleccionado = null;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void editarCelda() {
      if (empresasBancoSeleccionado != null) {
         editarEmpresasBancos = empresasBancoSeleccionado;
         RequestContext context = RequestContext.getCurrentInstance();
         if (cualCelda == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editPais");
            RequestContext.getCurrentInstance().execute("PF('editPais').show()");
            cualCelda = -1;
         } else if (cualCelda == 3) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editSubtituloFirma");
            RequestContext.getCurrentInstance().execute("PF('editSubtituloFirma').show()");
            cualCelda = -1;
         } else if (cualCelda == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editBancos");
            RequestContext.getCurrentInstance().execute("PF('editBancos').show()");
            cualCelda = -1;
         } else if (cualCelda == 2) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editCiudades");
            RequestContext.getCurrentInstance().execute("PF('editCiudades').show()");
            cualCelda = -1;
         }

      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void agregarNuevoEmpresasBancos() {
      int contador = 0;
      int duplicados = 0;

      Integer a = 0;
      a = null;
      mensajeValidacion = " ";
      RequestContext context = RequestContext.getCurrentInstance();
      if (nuevoEmpresasBancos.getNumerocuenta() == null) {
         mensajeValidacion = " Los campos marcados con asterisco son obligatorios";
      } else {
         for (int x = 0; x < listEmpresasBancos.size(); x++) {
            if (listEmpresasBancos.get(x).getNumerocuenta() == nuevoEmpresasBancos.getNumerocuenta()) {
               duplicados++;
            }
         }
         if (duplicados > 0) {
            mensajeValidacion = "La cuenta ingresada está relacionada con otro registro. Por favor ingrese una cuenta válida";
         } else {
            contador++;//1
         }
      }
      if (nuevoEmpresasBancos.getEmpresa().getNombre() == null) {
         mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
      } else {
         contador++;//2
      }
      if (nuevoEmpresasBancos.getBanco().getNombre() == null) {
         mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
      } else {
         contador++;//3
      }
      if (nuevoEmpresasBancos.getCiudad().getNombre() == null) {
         mensajeValidacion = " Los campos marcados con asterisco son obligatorios";
      } else {
         contador++;//4
      }

      if (contador == 4) {
         if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            //CERRAR FILTRADO
            log.info("Desactivar");
            obsoleto = (Column) c.getViewRoot().findComponent("form:datosEmpresasBancos:obsoleto");
            obsoleto.setFilterStyle("display: none; visibility: hidden;");
            pais = (Column) c.getViewRoot().findComponent("form:datosEmpresasBancos:pais");
            pais.setFilterStyle("display: none; visibility: hidden;");
            subTituloFirma = (Column) c.getViewRoot().findComponent("form:datosEmpresasBancos:subTituloFirma");
            subTituloFirma.setFilterStyle("display: none; visibility: hidden;");
            personafir = (Column) c.getViewRoot().findComponent("form:datosEmpresasBancos:personafir");
            personafir.setFilterStyle("display: none; visibility: hidden;");
            cargo = (Column) c.getViewRoot().findComponent("form:datosEmpresasBancos:cargo");
            cargo.setFilterStyle("display: none; visibility: hidden;");
            bandera = 0;
            filtrarEmpresasBancos = null;
            tipoLista = 0;
         }
         k++;
         l = BigInteger.valueOf(k);
         nuevoEmpresasBancos.setSecuencia(l);
         crearEmpresasBancos.add(nuevoEmpresasBancos);
         listEmpresasBancos.add(nuevoEmpresasBancos);
         empresasBancoSeleccionado = nuevoEmpresasBancos;
         contarRegistros();
         guardado = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         RequestContext.getCurrentInstance().execute("PF('nuevoRegistroEmpresasBancos').hide()");
         nuevoEmpresasBancos = new EmpresasBancos();
         nuevoEmpresasBancos.setEmpresa(new Empresas());
         nuevoEmpresasBancos.setCiudad(new Ciudades());
         nuevoEmpresasBancos.setBanco(new Bancos());
      } else {
         RequestContext.getCurrentInstance().update("form:validacionNuevaCentroCosto");
         RequestContext.getCurrentInstance().execute("PF('validacionNuevaCentroCosto').show()");
         contador = 0;
      }
   }

   public void limpiarNuevoEmpresasBancos() {
      nuevoEmpresasBancos = new EmpresasBancos();
      nuevoEmpresasBancos.setEmpresa(new Empresas());
      nuevoEmpresasBancos.setBanco(new Bancos());
      nuevoEmpresasBancos.setCiudad(new Ciudades());

   }

   public void duplicandoEmpresasBancos() {
      if (empresasBancoSeleccionado != null) {
         duplicarEmpresasBancos = new EmpresasBancos();
         duplicarEmpresasBancos.setEmpresa(new Empresas());
         duplicarEmpresasBancos.setBanco(new Bancos());
         duplicarEmpresasBancos.setCiudad(new Ciudades());
         k++;
         l = BigInteger.valueOf(k);
         duplicarEmpresasBancos.setSecuencia(l);
         duplicarEmpresasBancos.setNumerocuenta(empresasBancoSeleccionado.getNumerocuenta());
         duplicarEmpresasBancos.setTipocuenta(empresasBancoSeleccionado.getTipocuenta());
         duplicarEmpresasBancos.setEmpresa(empresasBancoSeleccionado.getEmpresa());
         duplicarEmpresasBancos.setBanco(empresasBancoSeleccionado.getBanco());
         duplicarEmpresasBancos.setCiudad(empresasBancoSeleccionado.getCiudad());
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTE");
         RequestContext.getCurrentInstance().execute("PF('duplicarRegistroEmpresasBancos').show()");
      }
   }

   public void confirmarDuplicar() {
      int contador = 0;
      mensajeValidacion = " ";
      int duplicados = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      Integer a = 0;
      a = null;
      if (duplicarEmpresasBancos.getNumerocuenta() == null) {
         mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
      } else {
         for (int x = 0; x < listEmpresasBancos.size(); x++) {
            if (listEmpresasBancos.get(x).getNumerocuenta() == duplicarEmpresasBancos.getNumerocuenta()) {
               duplicados++;
            }
         }
         if (duplicados > 0) {
            mensajeValidacion = " La cuenta ingresada está relacionada con otro registro. Por favor ingrese una cuenta válida";
         } else {
            contador++;
            duplicados = 0;
         }
      }

      if (duplicarEmpresasBancos.getEmpresa().getNombre() == null) {
         mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
      } else {
         contador++;
      }
      if (duplicarEmpresasBancos.getBanco().getNombre() == null) {
         mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
      } else {
         contador++;
      }
      if (duplicarEmpresasBancos.getCiudad().getNombre() == null) {
         mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
      } else {
         contador++;
      }

      if (contador == 4) {
         listEmpresasBancos.add(duplicarEmpresasBancos);
         crearEmpresasBancos.add(duplicarEmpresasBancos);
         empresasBancoSeleccionado = duplicarEmpresasBancos;
         RequestContext.getCurrentInstance().update("form:datosEmpresasBancos");
         guardado = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         contarRegistros();
         if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            //CERRAR FILTRADO
            obsoleto = (Column) c.getViewRoot().findComponent("form:datosEmpresasBancos:obsoleto");
            obsoleto.setFilterStyle("display: none; visibility: hidden;");
            pais = (Column) c.getViewRoot().findComponent("form:datosEmpresasBancos:pais");
            pais.setFilterStyle("display: none; visibility: hidden;");
            subTituloFirma = (Column) c.getViewRoot().findComponent("form:datosEmpresasBancos:subTituloFirma");
            subTituloFirma.setFilterStyle("display: none; visibility: hidden;");
            personafir = (Column) c.getViewRoot().findComponent("form:datosEmpresasBancos:personafir");
            personafir.setFilterStyle("display: none; visibility: hidden;");
            cargo = (Column) c.getViewRoot().findComponent("form:datosEmpresasBancos:cargo");
            cargo.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosEmpresasBancos");
            bandera = 0;
            filtrarEmpresasBancos = null;
            tipoLista = 0;
         }
         duplicarEmpresasBancos = new EmpresasBancos();
         duplicarEmpresasBancos.setEmpresa(new Empresas());
         duplicarEmpresasBancos.setCiudad(new Ciudades());
         duplicarEmpresasBancos.setBanco(new Bancos());
         RequestContext.getCurrentInstance().execute("PF('duplicarRegistroEmpresasBancos').hide()");
      } else {
         contador = 0;
         RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
         RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
      }
   }

   public void limpiarDuplicarEmpresasBancos() {
      duplicarEmpresasBancos = new EmpresasBancos();
      duplicarEmpresasBancos.setEmpresa(new Empresas());
      duplicarEmpresasBancos.setBanco(new Bancos());
      duplicarEmpresasBancos.setCiudad(new Ciudades());
   }

   public void exportPDF() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosEmpresasBancosExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "EMPRESASBANCOS", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void exportXLS() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosEmpresasBancosExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "EMPRESASBANCOS", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void verificarRastro() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (empresasBancoSeleccionado != null) {
         int resultado = administrarRastros.obtenerTabla(empresasBancoSeleccionado.getSecuencia(), "EMPRESASBANCOS"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
      } else if (administrarRastros.verificarHistoricosTabla("EMPRESASBANCOS")) { // igual acá
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
   }

   public void contarRegistrosEmpresas() {
      RequestContext.getCurrentInstance().update("form:infoRegistroEmpresas");
   }

   public void contarRegistrosCiudades() {
      RequestContext.getCurrentInstance().update("form:infoRegistroCiudades");
   }

   public void contarRegistrosBancos() {
      RequestContext.getCurrentInstance().update("form:infoRegistrosBancos");
   }

   public void contarRegistros() {
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
   }

   public void habilitarLov() {
      activarLov = false;
      RequestContext.getCurrentInstance().update("form:listaValores");
   }

   public void deshabilitarLov() {
      activarLov = true;
      RequestContext.getCurrentInstance().update("form:listaValores");
   }

   //*/*/*/*/*/*/*/*/*/*-/-*//-*/-*/*/*-*/-*/-*/*/*/*/*/---/*/*/*/*/-*/-*/-*/-*/-*/
   public List<EmpresasBancos> getListEmpresasBancos() {
      if (listEmpresasBancos == null) {
         listEmpresasBancos = administrarEmpresasBancos.consultarEmpresasBancos();
      }
      return listEmpresasBancos;
   }

   public void setListEmpresasBancos(List<EmpresasBancos> listEmpresasBancos) {
      this.listEmpresasBancos = listEmpresasBancos;
   }

   public List<EmpresasBancos> getFiltrarEmpresasBancos() {
      return filtrarEmpresasBancos;
   }

   public void setFiltrarEmpresasBancos(List<EmpresasBancos> filtrarEmpresasBancos) {
      this.filtrarEmpresasBancos = filtrarEmpresasBancos;
   }

   public EmpresasBancos getNuevoEmpresasBancos() {
      return nuevoEmpresasBancos;
   }

   public void setNuevoEmpresasBancos(EmpresasBancos nuevoEmpresasBancos) {
      this.nuevoEmpresasBancos = nuevoEmpresasBancos;
   }

   public EmpresasBancos getDuplicarEmpresasBancos() {
      return duplicarEmpresasBancos;
   }

   public void setDuplicarEmpresasBancos(EmpresasBancos duplicarEmpresasBancos) {
      this.duplicarEmpresasBancos = duplicarEmpresasBancos;
   }

   public EmpresasBancos getEditarEmpresasBancos() {
      return editarEmpresasBancos;
   }

   public void setEditarEmpresasBancos(EmpresasBancos editarEmpresasBancos) {
      this.editarEmpresasBancos = editarEmpresasBancos;
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

   public List<Empresas> getLovEmpresas() {
      if (lovEmpresas == null) {
         lovEmpresas = administrarEmpresasBancos.consultarLOVEmpresas();
      }
      return lovEmpresas;
   }

   public void setLovEmpresas(List<Empresas> lovEmpresas) {
      this.lovEmpresas = lovEmpresas;
   }

   public List<Empresas> getFiltradoEmpresas() {
      return filtradoEmpresas;
   }

   public void setFiltradoEmpresas(List<Empresas> filtradoEmpresas) {
      this.filtradoEmpresas = filtradoEmpresas;
   }

   public Empresas getEmpresaSeleccionado() {
      return empresaSeleccionado;
   }

   public void setEmpresaSeleccionado(Empresas empresaSeleccionado) {
      this.empresaSeleccionado = empresaSeleccionado;
   }

   public List<Bancos> getLovBancos() {
      if (lovBancos == null) {
         lovBancos = administrarEmpresasBancos.consultarLOVBancos();
      }
      return lovBancos;
   }

   public void setLovBancos(List<Bancos> lovBancos) {
      this.lovBancos = lovBancos;
   }

   public List<Bancos> getFiltradoBancos() {
      return filtradoBancos;
   }

   public void setFiltradoBancos(List<Bancos> filtradoBancos) {
      this.filtradoBancos = filtradoBancos;
   }

   public Bancos getBancoSeleccionado() {
      return bancoSeleccionado;
   }

   public void setBancoSeleccionado(Bancos bancoSeleccionado) {
      this.bancoSeleccionado = bancoSeleccionado;
   }

   public List<Ciudades> getLovCiudades() {
      if (lovCiudades == null) {
         lovCiudades = administrarEmpresasBancos.consultarLOVCiudades();
      }
      return lovCiudades;
   }

   public void setLovCiudades(List<Ciudades> lovCiudades) {
      this.lovCiudades = lovCiudades;
   }

   public List<Ciudades> getFiltradoCiudades() {
      return filtradoCiudades;
   }

   public void setFiltradoCiudades(List<Ciudades> filtradoCiudades) {
      this.filtradoCiudades = filtradoCiudades;
   }

   public Ciudades getCiudadSeleccionado() {
      return ciudadSeleccionada;
   }

   public void setCiudadSeleccionado(Ciudades cargoSeleccionado) {
      this.ciudadSeleccionada = cargoSeleccionado;
   }

   public EmpresasBancos getEmpresasBancoSeleccionado() {
      return empresasBancoSeleccionado;
   }

   public void setEmpresasBancoSeleccionado(EmpresasBancos empresasBancoSeleccionado) {
      this.empresasBancoSeleccionado = empresasBancoSeleccionado;
   }

   public boolean isAceptar() {
      return aceptar;
   }

   public void setAceptar(boolean aceptar) {
      this.aceptar = aceptar;
   }

   public String getInfoRegistro() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosEmpresasBancos");
      infoRegistro = String.valueOf(tabla.getRowCount());
      return infoRegistro;
   }

   public void setInfoRegistro(String infoRegistro) {
      this.infoRegistro = infoRegistro;
   }

   public String getInfoRegistroEmpresas() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovEmpresas");
      infoRegistroEmpresas = String.valueOf(tabla.getRowCount());
      return infoRegistroEmpresas;
   }

   public void setInfoRegistroEmpresas(String infoRegistroEmpresas) {
      this.infoRegistroEmpresas = infoRegistroEmpresas;
   }

   public String getInfoRegistrosBancos() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovBancos");
      infoRegistrosBancos = String.valueOf(tabla.getRowCount());
      return infoRegistrosBancos;
   }

   public void setInfoRegistrosBancos(String infoRegistrosBancos) {
      this.infoRegistrosBancos = infoRegistrosBancos;
   }

   public String getInfoRegistroCiudades() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovCiudades");
      infoRegistroCiudades = String.valueOf(tabla.getRowCount());
      return infoRegistroCiudades;
   }

   public void setInfoRegistroCiudades(String infoRegistroCiudades) {
      this.infoRegistroCiudades = infoRegistroCiudades;
   }

   public boolean isActivarLov() {
      return activarLov;
   }

   public void setActivarLov(boolean activarLov) {
      this.activarLov = activarLov;
   }
}
