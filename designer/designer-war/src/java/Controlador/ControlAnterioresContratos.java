/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import ControlNavegacion.ControlListaNavegacion;
import Entidades.AnterioresContratos;
import Entidades.Cargos;
import Entidades.Personas;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarAnterioresContratosInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
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
@Named(value = "controlAnterioresContratos")
@SessionScoped
public class ControlAnterioresContratos implements Serializable {

   private static Logger log = Logger.getLogger(ControlAnterioresContratos.class);

   @EJB
   AdministrarAnterioresContratosInterface administrarAnterioresContratos;
   @EJB
   AdministrarRastrosInterface administrarRastros;

   private List<AnterioresContratos> listAnterioresContratos;
   private List<AnterioresContratos> listAnterioresContratosCrear;
   private List<AnterioresContratos> listAnterioresContratosModificar;
   private List<AnterioresContratos> listAnterioresContratosBorrar;
   private List<AnterioresContratos> listAnterioresContratosFiltrar;
   private AnterioresContratos anteriorContratoSeleccionado;
   private AnterioresContratos nuevoAnteriorContrato;
   private AnterioresContratos duplicarAnteriorContrato;
   private AnterioresContratos editarAnteriorContrato;
   //lovs
   private List<Cargos> lovCargos;
   private List<Cargos> lovCargosFiltrar;
   private Cargos cargoSeleccionado;
   //otros
   private int tipoActualizacion, bandera;
   private Column fechaInicial, fechaFinal, empresa, jefe, telefono, dias, cargo, regimenc;
   private boolean aceptar;
   private boolean guardado;
   private BigInteger l;
   private int k;
   private int cualCelda, tipoLista;
   private Date fechaParametro;
   private Date fechaIni, fechaFin;
   private String altoTabla;
   private String infoRegistro;
   private String infoRegistroCargo;
   private String mensajeValidacion;
   private Personas persona;
   private boolean activarLOV;

   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

   public void limpiarListasValor() {
      lovCargos = null;
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
         administrarAnterioresContratos.obtenerConexion(ses.getId());
         administrarRastros.obtenerConexion(ses.getId());
      } catch (Exception e) {
         log.error("Error postconstruct " + this.getClass().getName() + ": " + e);
         log.error("Causa: " + e.getCause());
      }
   }

   public ControlAnterioresContratos() {
      aceptar = true;
      tipoLista = 0;
      listAnterioresContratosBorrar = new ArrayList<AnterioresContratos>();
      listAnterioresContratosCrear = new ArrayList<AnterioresContratos>();
      listAnterioresContratosModificar = new ArrayList<AnterioresContratos>();
      lovCargos = null;
      cargoSeleccionado = null;
      nuevoAnteriorContrato = new AnterioresContratos();
      nuevoAnteriorContrato.setCargo(new Cargos());
      editarAnteriorContrato = new AnterioresContratos();
      duplicarAnteriorContrato = new AnterioresContratos();
      duplicarAnteriorContrato.setCargo(new Cargos());
      cualCelda = -1;
      altoTabla = "270";
      guardado = true;
      activarLOV = true;
      paginaAnterior = " ";
      mapParametros.put("paginaAnterior", paginaAnterior);
      mensajeValidacion = " ";
   }

   public void recibirPaginaEntrante(String pagina) {
      paginaAnterior = pagina;
      //inicializarCosas(); Inicializar cosas de ser necesario
   }

   public void recibirPersona(String pagina, Personas per) {
      paginaAnterior = pagina;
      persona = per;
      listAnterioresContratos = null;
      getListAnterioresContratos();
      if (listAnterioresContratos != null) {
         if (!listAnterioresContratos.isEmpty()) {
            anteriorContratoSeleccionado = listAnterioresContratos.get(0);
         }
      }
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
      String pagActual = "anteriorescontratos";
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

   public void cancelarModificacion() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 1) {
         fechaInicial = (Column) c.getViewRoot().findComponent("form:datosAnterioresContratosPersona:fechainicial");
         fechaInicial.setFilterStyle("display: none; visibility: hidden;");
         fechaFinal = (Column) c.getViewRoot().findComponent("form:datosAnterioresContratosPersona:fechafinal");
         fechaFinal.setFilterStyle("display: none; visibility: hidden;");
         empresa = (Column) c.getViewRoot().findComponent("form:datosAnterioresContratosPersona:empresa");
         empresa.setFilterStyle("display: none; visibility: hidden;");
         jefe = (Column) c.getViewRoot().findComponent("form:datosAnterioresContratosPersona:jefe");
         jefe.setFilterStyle("display: none; visibility: hidden;");
         telefono = (Column) c.getViewRoot().findComponent("form:datosAnterioresContratosPersona:telefono");
         telefono.setFilterStyle("display: none; visibility: hidden;");
         dias = (Column) c.getViewRoot().findComponent("form:datosAnterioresContratosPersona:dias");
         dias.setFilterStyle("display: none; visibility: hidden;");
         cargo = (Column) c.getViewRoot().findComponent("form:datosAnterioresContratosPersona:cargo");
         cargo.setFilterStyle("display: none; visibility: hidden;");
         regimenc = (Column) c.getViewRoot().findComponent("form:datosAnterioresContratosPersona:regimenc");
         regimenc.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosAnterioresContratosPersona");
         bandera = 0;
         listAnterioresContratosFiltrar = null;
         tipoLista = 0;
         altoTabla = "270";
      }

      listAnterioresContratosBorrar.clear();
      listAnterioresContratosCrear.clear();
      listAnterioresContratosModificar.clear();
      k = 0;
      contarRegistros();
      anteriorContratoSeleccionado = null;
      listAnterioresContratos = null;
      getListAnterioresContratos();
      guardado = true;
      contarRegistros();
      deshabilitarBotonLov();
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      RequestContext.getCurrentInstance().update("form:datosAnterioresContratosPersona");
   }

   public void actualizarCargos() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoActualizacion == 0) {
         anteriorContratoSeleccionado.setCargo(cargoSeleccionado);
         if (!listAnterioresContratosCrear.contains(anteriorContratoSeleccionado)) {
            if (listAnterioresContratosModificar.isEmpty()) {
               listAnterioresContratosModificar.add(anteriorContratoSeleccionado);
            } else if (!listAnterioresContratosModificar.contains(anteriorContratoSeleccionado)) {
               listAnterioresContratosModificar.add(anteriorContratoSeleccionado);
            }
         }
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         RequestContext.getCurrentInstance().update("form:datosAnterioresContratosPersona");
      } else if (tipoActualizacion == 1) {
         nuevoAnteriorContrato.setCargo(cargoSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevoCargo");
      } else if (tipoActualizacion == 2) {
         duplicarAnteriorContrato.setCargo(cargoSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCargo");
      }
      lovCargosFiltrar = null;
      cargoSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      //cualCelda = -1;
      RequestContext.getCurrentInstance().update("formularioDialogos:cargosDialogo");
      RequestContext.getCurrentInstance().update("formularioDialogos:LOVCargos");
      RequestContext.getCurrentInstance().update("formularioDialogos:aceptarC");

      context.reset("formularioDialogos:LOVCargos:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('LOVCargos').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('cargosDialogo').hide()");
   }

   public void cancelarCambioCargos() {
      lovCargosFiltrar = null;
      cargoSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("formularioDialogos:cargosDialogo");
      RequestContext.getCurrentInstance().update("formularioDialogos:LOVCargos");
      RequestContext.getCurrentInstance().update("formularioDialogos:aceptarC");
      context.reset("formularioDialogos:LOVCargos:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('LOVCargos').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('cargosDialogo').hide()");
   }

   public void editarCelda() {
      if (anteriorContratoSeleccionado != null) {
         editarAnteriorContrato = anteriorContratoSeleccionado;
         if (cualCelda == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaInicialD");
            RequestContext.getCurrentInstance().execute("PF('editarFechaInicialD').show()");
            cualCelda = -1;
         } else if (cualCelda == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaFinalD");
            RequestContext.getCurrentInstance().execute("PF('editarFechaFinalD').show()");
            cualCelda = -1;
         } else if (cualCelda == 2) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarEmpresaD");
            RequestContext.getCurrentInstance().execute("PF('editarEmpresaD').show()");
            cualCelda = -1;
         } else if (cualCelda == 3) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarJefeD");
            RequestContext.getCurrentInstance().execute("PF('editarJefeD').show()");
            cualCelda = -1;
         } else if (cualCelda == 4) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarTelefonoD");
            RequestContext.getCurrentInstance().execute("PF('editarTelefonoD').show()");
            cualCelda = -1;
         } else if (cualCelda == 5) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarDiasD");
            RequestContext.getCurrentInstance().execute("PF('editarDiasD').show()");
            cualCelda = -1;
         } else if (cualCelda == 6) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarCargoD");
            RequestContext.getCurrentInstance().execute("PF('editarCargoD').show()");
            cualCelda = -1;
         }
      } else {
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void listaValoresBoton() {
      if (anteriorContratoSeleccionado != null) {
         RequestContext context = RequestContext.getCurrentInstance();
         if (cualCelda == 6) {
            contarRegistrosCargos();
            RequestContext.getCurrentInstance().update("formularioDialogos:cargosDialogo");
            RequestContext.getCurrentInstance().execute("PF('cargosDialogo').show()");
            tipoActualizacion = 0;
         }
      }
   }

   public void borrarAnterioresContratos() {
      if (anteriorContratoSeleccionado != null) {
         if (!listAnterioresContratosModificar.isEmpty() && listAnterioresContratosModificar.contains(anteriorContratoSeleccionado)) {
            listAnterioresContratosModificar.remove(listAnterioresContratosModificar.indexOf(anteriorContratoSeleccionado));
            listAnterioresContratosBorrar.add(anteriorContratoSeleccionado);
         } else if (!listAnterioresContratosCrear.isEmpty() && listAnterioresContratosCrear.contains(anteriorContratoSeleccionado)) {
            listAnterioresContratosCrear.remove(listAnterioresContratosCrear.indexOf(anteriorContratoSeleccionado));
         } else {
            listAnterioresContratosBorrar.add(anteriorContratoSeleccionado);
         }
         listAnterioresContratos.remove(anteriorContratoSeleccionado);
         if (tipoLista == 1) {
            listAnterioresContratosFiltrar.remove(anteriorContratoSeleccionado);
         }
         contarRegistros();
         RequestContext.getCurrentInstance().update("form:datosAnterioresContratosPersona");
         anteriorContratoSeleccionado = null;
         guardado = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void duplicarAT() {
      if (anteriorContratoSeleccionado != null) {
         duplicarAnteriorContrato = new AnterioresContratos();
         k++;
         l = BigInteger.valueOf(k);
         duplicarAnteriorContrato.setSecuencia(l);
         duplicarAnteriorContrato.setPersona(persona);
         duplicarAnteriorContrato.setFechainicial(anteriorContratoSeleccionado.getFechainicial());
         duplicarAnteriorContrato.setFechafinal(anteriorContratoSeleccionado.getFechafinal());
         duplicarAnteriorContrato.setEmpresa(anteriorContratoSeleccionado.getEmpresa());
         duplicarAnteriorContrato.setJefe(anteriorContratoSeleccionado.getJefe());
         duplicarAnteriorContrato.setTelefono(anteriorContratoSeleccionado.getTelefono());
         duplicarAnteriorContrato.setDiasant(anteriorContratoSeleccionado.getDiasant());
         duplicarAnteriorContrato.setCargo(anteriorContratoSeleccionado.getCargo());
         duplicarAnteriorContrato.setTiemporegimencesantias(anteriorContratoSeleccionado.getTiemporegimencesantias());
         RequestContext.getCurrentInstance().update("formularioDialogos:DuplicarRegistroAC");
         RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroAC').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void confirmarDuplicar() {
      int contador = 0;
      int banderaf = 0;

      if (duplicarAnteriorContrato.getFechainicial() == null) {
         mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
         contador++;
      }
      if (duplicarAnteriorContrato.getFechafinal() == null) {
         mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
         contador++;
      }
      if (duplicarAnteriorContrato.getCargo() == null) {
         mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
         contador++;
      }

      if (contador == 0) {
         if (duplicarAnteriorContrato.getFechafinal().before(duplicarAnteriorContrato.getFechainicial())) {
            banderaf++;
         }
         if (banderaf == 0) {
            listAnterioresContratos.add(duplicarAnteriorContrato);
            listAnterioresContratosCrear.add(duplicarAnteriorContrato);
            anteriorContratoSeleccionado = duplicarAnteriorContrato;
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:datosAnterioresContratosPersona");
            if (guardado == true) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            if (bandera == 1) {
               FacesContext c = FacesContext.getCurrentInstance();
               fechaInicial = (Column) c.getViewRoot().findComponent("form:datosAnterioresContratosPersona:fechainicial");
               fechaInicial.setFilterStyle("display: none; visibility: hidden;");
               fechaFinal = (Column) c.getViewRoot().findComponent("form:datosAnterioresContratosPersona:fechafinal");
               fechaFinal.setFilterStyle("display: none; visibility: hidden;");
               empresa = (Column) c.getViewRoot().findComponent("form:datosAnterioresContratosPersona:empresa");
               empresa.setFilterStyle("display: none; visibility: hidden;");
               jefe = (Column) c.getViewRoot().findComponent("form:datosAnterioresContratosPersona:jefe");
               jefe.setFilterStyle("display: none; visibility: hidden;");
               telefono = (Column) c.getViewRoot().findComponent("form:datosAnterioresContratosPersona:telefono");
               telefono.setFilterStyle("display: none; visibility: hidden;");
               dias = (Column) c.getViewRoot().findComponent("form:datosAnterioresContratosPersona:dias");
               dias.setFilterStyle("display: none; visibility: hidden;");
               cargo = (Column) c.getViewRoot().findComponent("form:datosAnterioresContratosPersona:cargo");
               cargo.setFilterStyle("display: none; visibility: hidden;");
               regimenc = (Column) c.getViewRoot().findComponent("form:datosAnterioresContratosPersona:regimenc");
               regimenc.setFilterStyle("display: none; visibility: hidden;");
               altoTabla = "270";
               RequestContext.getCurrentInstance().update("form:datosAnterioresContratosPersona");
               bandera = 0;
               listAnterioresContratosFiltrar = null;
               tipoLista = 0;
            }
            duplicarAnteriorContrato = new AnterioresContratos();
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroAC').hide()");
         } else {
            RequestContext.getCurrentInstance().update("form:errorFechas");
            RequestContext.getCurrentInstance().execute("PF('errorFechas').show()");
         }
      } else {
         RequestContext.getCurrentInstance().update("form:errorRegNew");
         RequestContext.getCurrentInstance().execute("PF('errorRegNew').show()");
      }
   }

   public void limpiarDuplicarAC() {
      duplicarAnteriorContrato = new AnterioresContratos();
      duplicarAnteriorContrato.setCargo(new Cargos());
   }

   public void exportPDF() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosAnterioresContratosExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "AnterioresContratosPDF", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void exportXLS() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosAnterioresContratosExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "AnterioresContratosXLS", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void limpiarNuevoAC() {
      nuevoAnteriorContrato = new AnterioresContratos();
      nuevoAnteriorContrato.setCargo(new Cargos());
   }

   public void activarCtrlF11() {
      log.info("TipoLista= " + tipoLista);
      FacesContext c = FacesContext.getCurrentInstance();

      if (bandera == 0) {
         fechaInicial = (Column) c.getViewRoot().findComponent("form:datosAnterioresContratosPersona:fechainicial");
         fechaInicial.setFilterStyle("width: 85%");
         fechaFinal = (Column) c.getViewRoot().findComponent("form:datosAnterioresContratosPersona:fechafinal");
         fechaFinal.setFilterStyle("width: 85%");
         empresa = (Column) c.getViewRoot().findComponent("form:datosAnterioresContratosPersona:empresa");
         empresa.setFilterStyle("width: 85%");
         jefe = (Column) c.getViewRoot().findComponent("form:datosAnterioresContratosPersona:jefe");
         jefe.setFilterStyle("width: 85%");
         telefono = (Column) c.getViewRoot().findComponent("form:datosAnterioresContratosPersona:telefono");
         telefono.setFilterStyle("width: 85%");
         dias = (Column) c.getViewRoot().findComponent("form:datosAnterioresContratosPersona:dias");
         dias.setFilterStyle("width: 85%");
         cargo = (Column) c.getViewRoot().findComponent("form:datosAnterioresContratosPersona:cargo");
         cargo.setFilterStyle("width: 85%");
         regimenc = (Column) c.getViewRoot().findComponent("form:datosAnterioresContratosPersona:regimenc");
         regimenc.setFilterStyle("width: 85%");
         RequestContext.getCurrentInstance().update("form:datosAnterioresContratosPersona");
         altoTabla = "250";
         bandera = 1;

      } else if (bandera == 1) {
         fechaInicial = (Column) c.getViewRoot().findComponent("form:datosAnterioresContratosPersona:fechainicial");
         fechaInicial.setFilterStyle("display: none; visibility: hidden;");
         fechaFinal = (Column) c.getViewRoot().findComponent("form:datosAnterioresContratosPersona:fechafinal");
         fechaFinal.setFilterStyle("display: none; visibility: hidden;");
         empresa = (Column) c.getViewRoot().findComponent("form:datosAnterioresContratosPersona:empresa");
         empresa.setFilterStyle("display: none; visibility: hidden;");
         jefe = (Column) c.getViewRoot().findComponent("form:datosAnterioresContratosPersona:jefe");
         jefe.setFilterStyle("display: none; visibility: hidden;");
         telefono = (Column) c.getViewRoot().findComponent("form:datosAnterioresContratosPersona:telefono");
         telefono.setFilterStyle("display: none; visibility: hidden;");
         dias = (Column) c.getViewRoot().findComponent("form:datosAnterioresContratosPersona:dias");
         dias.setFilterStyle("display: none; visibility: hidden;");
         cargo = (Column) c.getViewRoot().findComponent("form:datosAnterioresContratosPersona:cargo");
         cargo.setFilterStyle("display: none; visibility: hidden;");
         regimenc = (Column) c.getViewRoot().findComponent("form:datosAnterioresContratosPersona:regimenc");
         regimenc.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosAnterioresContratosPersona");
         bandera = 0;
         listAnterioresContratosFiltrar = null;
         tipoLista = 0;
         altoTabla = "270";
      }
   }

   public void agregarNuevoAC() {
      int contador = 0;
      int banderaf = 0;

      if (nuevoAnteriorContrato.getFechainicial() == null) {
         mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
         contador++;
      }
      if (nuevoAnteriorContrato.getFechafinal() == null) {
         mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
         contador++;
      }
      if (nuevoAnteriorContrato.getCargo() == null) {
         mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
         contador++;
      }

      if (contador == 0) {
         log.info("nuevoAnteriorContrato.getFechafinal() " + nuevoAnteriorContrato.getFechafinal());
         log.info("nuevoAnteriorContrato.getFechafinal() " + nuevoAnteriorContrato.getFechafinal());
         if (nuevoAnteriorContrato.getFechafinal().before(nuevoAnteriorContrato.getFechainicial())) {
            banderaf++;
         }
         if (banderaf == 0) {
            if (bandera == 1) {
               FacesContext c = FacesContext.getCurrentInstance();
               fechaInicial = (Column) c.getViewRoot().findComponent("form:datosAnterioresContratosPersona:fechainicial");
               fechaInicial.setFilterStyle("display: none; visibility: hidden;");
               fechaFinal = (Column) c.getViewRoot().findComponent("form:datosAnterioresContratosPersona:fechafinal");
               fechaFinal.setFilterStyle("display: none; visibility: hidden;");
               empresa = (Column) c.getViewRoot().findComponent("form:datosAnterioresContratosPersona:empresa");
               empresa.setFilterStyle("display: none; visibility: hidden;");
               jefe = (Column) c.getViewRoot().findComponent("form:datosAnterioresContratosPersona:jefe");
               jefe.setFilterStyle("display: none; visibility: hidden;");
               telefono = (Column) c.getViewRoot().findComponent("form:datosAnterioresContratosPersona:telefono");
               telefono.setFilterStyle("display: none; visibility: hidden;");
               dias = (Column) c.getViewRoot().findComponent("form:datosAnterioresContratosPersona:dias");
               dias.setFilterStyle("display: none; visibility: hidden;");
               cargo = (Column) c.getViewRoot().findComponent("form:datosAnterioresContratosPersona:cargo");
               cargo.setFilterStyle("display: none; visibility: hidden;");
               regimenc = (Column) c.getViewRoot().findComponent("form:datosAnterioresContratosPersona:regimenc");
               regimenc.setFilterStyle("display: none; visibility: hidden;");
               RequestContext.getCurrentInstance().update("form:datosAnterioresContratosPersona");
               bandera = 0;
               listAnterioresContratosFiltrar = null;
               tipoLista = 0;
               altoTabla = "270";
            }
            k++;
            l = BigInteger.valueOf(k);
            nuevoAnteriorContrato.setSecuencia(l);
            nuevoAnteriorContrato.setPersona(persona);
            listAnterioresContratosCrear.add(nuevoAnteriorContrato);
            listAnterioresContratos.add(nuevoAnteriorContrato);
            anteriorContratoSeleccionado = nuevoAnteriorContrato;
            nuevoAnteriorContrato = new AnterioresContratos();
            contarRegistros();
            deshabilitarBotonLov();
            RequestContext.getCurrentInstance().update("form:datosAnterioresContratosPersona");
            if (guardado == true) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            RequestContext.getCurrentInstance().execute("PF('NuevoRegistroAC').hide()");
         } else {
            RequestContext.getCurrentInstance().update("form:errorFechas");
            RequestContext.getCurrentInstance().execute("PF('errorFechas').show()");
         }
      } else {
         RequestContext.getCurrentInstance().update("form:errorRegNew");
         RequestContext.getCurrentInstance().execute("PF('errorRegNew').show()");
      }

   }

   public void cambiarIndice(AnterioresContratos ac, int celda) {
      anteriorContratoSeleccionado = ac;
      cualCelda = celda;
      anteriorContratoSeleccionado.getSecuencia();
      if (cualCelda == 0) {
         deshabilitarBotonLov();
         anteriorContratoSeleccionado.getFechainicial();
      } else if (cualCelda == 1) {
         deshabilitarBotonLov();
         anteriorContratoSeleccionado.getFechafinal();
      } else if (cualCelda == 2) {
         deshabilitarBotonLov();
         anteriorContratoSeleccionado.getEmpresa();
      } else if (cualCelda == 3) {
         deshabilitarBotonLov();
         anteriorContratoSeleccionado.getJefe();
      } else if (cualCelda == 4) {
         deshabilitarBotonLov();
         anteriorContratoSeleccionado.getTelefono();
      } else if (cualCelda == 5) {
         deshabilitarBotonLov();
         anteriorContratoSeleccionado.getDiasant();
      } else if (cualCelda == 6) {
         habilitarBotonLov();
         anteriorContratoSeleccionado.getCargo().getNombre();
      }
   }

   public void modificarAC(AnterioresContratos ac) {
      anteriorContratoSeleccionado = ac;
      int banderaf = 0;
      if (anteriorContratoSeleccionado.getFechafinal().before(anteriorContratoSeleccionado.getFechainicial())) {
         banderaf++;
      }
      if (banderaf == 0) {
         if (!listAnterioresContratosCrear.contains(anteriorContratoSeleccionado)) {
            if (listAnterioresContratosModificar.isEmpty()) {
               listAnterioresContratosModificar.add(anteriorContratoSeleccionado);
            } else if (!listAnterioresContratosModificar.contains(anteriorContratoSeleccionado)) {
               listAnterioresContratosModificar.add(anteriorContratoSeleccionado);
            }
            if (guardado == true) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
         }
         RequestContext.getCurrentInstance().update("form:datosAnterioresContratosPersona");
      } else {
         RequestContext.getCurrentInstance().update("form:errorFechas");
         RequestContext.getCurrentInstance().execute("PF('errorFechas').show()");
      }

   }

   public void guardarCambiosAC() {
      if (guardado == false) {
         if (!listAnterioresContratosBorrar.isEmpty()) {
            administrarAnterioresContratos.borrarAnteriorContrato(listAnterioresContratosBorrar);
         }
         listAnterioresContratosBorrar.clear();

         if (!listAnterioresContratosCrear.isEmpty()) {
            administrarAnterioresContratos.crearAnteriorContrato(listAnterioresContratosCrear);
         }
         listAnterioresContratosCrear.clear();

         if (!listAnterioresContratosModificar.isEmpty()) {
            administrarAnterioresContratos.editarAnteriorContrato(listAnterioresContratosModificar);
         }
         listAnterioresContratosModificar.clear();

         listAnterioresContratos = null;
         getListAnterioresContratos();
         contarRegistros();
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         k = 0;
         FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      }
      guardado = true;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      RequestContext.getCurrentInstance().update("form:datosAnterioresContratosPersona");
      anteriorContratoSeleccionado = null;
   }

   public void salir() {
      limpiarListasValor();
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 1) {
         fechaInicial = (Column) c.getViewRoot().findComponent("form:datosAnterioresContratosPersona:fechainicial");
         fechaInicial.setFilterStyle("display: none; visibility: hidden;");
         fechaFinal = (Column) c.getViewRoot().findComponent("form:datosAnterioresContratosPersona:fechafinal");
         fechaFinal.setFilterStyle("display: none; visibility: hidden;");
         empresa = (Column) c.getViewRoot().findComponent("form:datosAnterioresContratosPersona:empresa");
         empresa.setFilterStyle("display: none; visibility: hidden;");
         jefe = (Column) c.getViewRoot().findComponent("form:datosAnterioresContratosPersona:jefe");
         jefe.setFilterStyle("display: none; visibility: hidden;");
         telefono = (Column) c.getViewRoot().findComponent("form:datosAnterioresContratosPersona:telefono");
         telefono.setFilterStyle("display: none; visibility: hidden;");
         dias = (Column) c.getViewRoot().findComponent("form:datosAnterioresContratosPersona:dias");
         dias.setFilterStyle("display: none; visibility: hidden;");
         cargo = (Column) c.getViewRoot().findComponent("form:datosAnterioresContratosPersona:cargo");
         cargo.setFilterStyle("display: none; visibility: hidden;");
         regimenc = (Column) c.getViewRoot().findComponent("form:datosAnterioresContratosPersona:regimenc");
         regimenc.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosAnterioresContratosPersona");
         bandera = 0;
         listAnterioresContratosFiltrar = null;
         tipoLista = 0;
         altoTabla = "270";
      }
      listAnterioresContratosBorrar.clear();
      listAnterioresContratosCrear.clear();
      listAnterioresContratosModificar.clear();
      contarRegistros();
      anteriorContratoSeleccionado = null;
      k = 0;
      listAnterioresContratos = null;
      guardado = true;
      navegar("atras");
   }

   public void activarAceptar() {
      aceptar = false;
   }

   public void asignarIndex(AnterioresContratos ac, int dlg, int LND) {
      anteriorContratoSeleccionado = ac;
      tipoActualizacion = LND;
      if (dlg == 0) {
         lovCargos = null;
         getLovCargos();
         contarRegistrosCargos();
         RequestContext.getCurrentInstance().update("formularioDialogos:cargosDialogo");
         RequestContext.getCurrentInstance().execute("PF('cargosDialogo').show()");
      }
   }

   public void verificarRastro() {
      if (anteriorContratoSeleccionado != null) {
         int resultado = administrarRastros.obtenerTabla(anteriorContratoSeleccionado.getSecuencia(), "ANTERIORESCONTRATOS");
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
      } else if (administrarRastros.verificarHistoricosTabla("ANTERIORESCONTRATOS")) {
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
   }

   public void eventoFiltrar() {
      if (tipoLista == 0) {
         tipoLista = 1;
      }
      contarRegistros();
   }

   public void contarRegistrosCargos() {
      RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroCargo");
   }

   public void contarRegistros() {
      RequestContext.getCurrentInstance().update("form:infoRegistro");
   }

   public void habilitarBotonLov() {
      activarLOV = false;
      RequestContext.getCurrentInstance().update("form:listaValores");
   }

   public void deshabilitarBotonLov() {
      activarLOV = true;
      RequestContext.getCurrentInstance().update("form:listaValores");
   }

   public void guardarSalir() {
      guardarCambiosAC();
      salir();
   }

   public void cancelarSalir() {
      cancelarModificacion();
      salir();
   }

   //////GETS Y SETS////////
   public List<AnterioresContratos> getListAnterioresContratos() {
      if (listAnterioresContratos == null) {
         listAnterioresContratos = administrarAnterioresContratos.listaAnterioresContratos(persona.getSecuencia());
      }

      return listAnterioresContratos;
   }

   public void setListAnterioresContratos(List<AnterioresContratos> listAnterioresContratos) {
      this.listAnterioresContratos = listAnterioresContratos;
   }

   public List<AnterioresContratos> getListAnterioresContratosFiltrar() {
      return listAnterioresContratosFiltrar;
   }

   public void setListAnterioresContratosFiltrar(List<AnterioresContratos> listAnterioresContratosFiltrar) {
      this.listAnterioresContratosFiltrar = listAnterioresContratosFiltrar;
   }

   public AnterioresContratos getAnteriorContratoSeleccionado() {
      return anteriorContratoSeleccionado;
   }

   public void setAnteriorContratoSeleccionado(AnterioresContratos anteriorContratoSeleccionado) {
      this.anteriorContratoSeleccionado = anteriorContratoSeleccionado;
   }

   public AnterioresContratos getNuevoAnteriorContrato() {
      return nuevoAnteriorContrato;
   }

   public void setNuevoAnteriorContrato(AnterioresContratos nuevoAnteriorContrato) {
      this.nuevoAnteriorContrato = nuevoAnteriorContrato;
   }

   public AnterioresContratos getDuplicarAnteriorContrato() {
      return duplicarAnteriorContrato;
   }

   public void setDuplicarAnteriorContrato(AnterioresContratos duplicarAnteriorContrato) {
      this.duplicarAnteriorContrato = duplicarAnteriorContrato;
   }

   public AnterioresContratos getEditarAnteriorContrato() {
      return editarAnteriorContrato;
   }

   public void setEditarAnteriorContrato(AnterioresContratos editarAnteriorContrato) {
      this.editarAnteriorContrato = editarAnteriorContrato;
   }

   public List<Cargos> getLovCargos() {
      if (lovCargos == null) {
         lovCargos = administrarAnterioresContratos.lovCargos();
      }
      return lovCargos;
   }

   public void setLovCargos(List<Cargos> lovCargos) {
      this.lovCargos = lovCargos;
   }

   public List<Cargos> getLovCargosFiltrar() {
      return lovCargosFiltrar;
   }

   public void setLovCargosFiltrar(List<Cargos> lovCargosFiltrar) {
      this.lovCargosFiltrar = lovCargosFiltrar;
   }

   public Cargos getCargoSeleccionado() {
      return cargoSeleccionado;
   }

   public void setCargoSeleccionado(Cargos cargoSeleccionado) {
      this.cargoSeleccionado = cargoSeleccionado;
   }

   public boolean isAceptar() {
      return aceptar;
   }

   public void setAceptar(boolean aceptar) {
      this.aceptar = aceptar;
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

   public void setAltoTabla(String altoTabla) {
      this.altoTabla = altoTabla;
   }

   public String getInfoRegistro() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosAnterioresContratosPersona");
      infoRegistro = String.valueOf(tabla.getRowCount());
      return infoRegistro;
   }

   public void setInfoRegistro(String infoRegistro) {
      this.infoRegistro = infoRegistro;
   }

   public String getInfoRegistroCargo() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:LOVCargos");
      infoRegistroCargo = String.valueOf(tabla.getRowCount());
      return infoRegistroCargo;
   }

   public void setInfoRegistroCargo(String infoRegistroCargo) {
      this.infoRegistroCargo = infoRegistroCargo;
   }

   public boolean isActivarLOV() {
      return activarLOV;
   }

   public void setActivarLOV(boolean activarLOV) {
      this.activarLOV = activarLOV;
   }

   public Personas getPersona() {
      return persona;
   }

   public void setPersona(Personas persona) {
      this.persona = persona;
   }

   public String getMensajeValidacion() {
      return mensajeValidacion;
   }

   public void setMensajeValidacion(String mensajeValidacion) {
      this.mensajeValidacion = mensajeValidacion;
   }

}
