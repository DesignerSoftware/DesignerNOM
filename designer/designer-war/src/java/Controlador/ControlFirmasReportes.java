/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Cargos;
import Entidades.FirmasReportes;
import Entidades.Empresas;
import Entidades.Personas;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarFirmasReportesInterface;
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
public class ControlFirmasReportes implements Serializable {

   private static Logger log = Logger.getLogger(ControlFirmasReportes.class);

   @EJB
   AdministrarFirmasReportesInterface administrarFirmasReportes;
   @EJB
   AdministrarRastrosInterface administrarRastros;

   private List<FirmasReportes> listFirmasReportes;
   private List<FirmasReportes> filtrarFirmasReportes;
   private List<FirmasReportes> crearFirmasReportes;
   private List<FirmasReportes> modificarFirmasReportes;
   private List<FirmasReportes> borrarFirmasReportes;
   private FirmasReportes nuevoFirmasReportes;
   private FirmasReportes duplicarFirmasReportes;
   private FirmasReportes editarFirmasReportes;
   private FirmasReportes firmaReporteSeleccionada;
   private int cualCelda, tipoLista, tipoActualizacion, k, bandera;
   private BigInteger l;
   private boolean aceptar, guardado;
   private boolean permitirIndex;
   private Column codigo, descripcion, pais, subTituloFirma, personafir, cargo;
   private int registrosBorrados;
   private String mensajeValidacion;
   private int tamano;
   private Integer backupCodigo;
   private String backupDescripcion;
   private String backupPais;
   private String backupSubtituloFirma;
   private String backupEmpresas;
   private List<Empresas> lovEmpresas;
   private List<Empresas> filtradoEmpresas;
   private Empresas empresaSeleccionado;
   private List<Personas> lovPersonas;
   private List<Personas> filtradoPersonas;
   private Personas personaSeleccionado;
   private List<Cargos> lovCargos;
   private List<Cargos> filtradoCargos;
   private Cargos cargoSeleccionado;
   private String infoRegistro;
   private String infoLOVCargo;
   private String infoLOVPersona;
   private String infoLOVEmpresa;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();
   private boolean activarLov;

   public ControlFirmasReportes() {
      listFirmasReportes = null;
      crearFirmasReportes = new ArrayList<FirmasReportes>();
      modificarFirmasReportes = new ArrayList<FirmasReportes>();
      borrarFirmasReportes = new ArrayList<FirmasReportes>();
      permitirIndex = true;
      editarFirmasReportes = new FirmasReportes();
      nuevoFirmasReportes = new FirmasReportes();
      nuevoFirmasReportes.setEmpresa(new Empresas());
      nuevoFirmasReportes.setPersonafirma(new Personas());
      nuevoFirmasReportes.setCargofirma(new Cargos());
      duplicarFirmasReportes = new FirmasReportes();
      duplicarFirmasReportes.setEmpresa(new Empresas());
      duplicarFirmasReportes.setPersonafirma(new Personas());
      duplicarFirmasReportes.setCargofirma(new Cargos());
      lovEmpresas = null;
      filtradoEmpresas = null;
      lovPersonas = null;
      filtradoPersonas = null;
      lovCargos = null;
      filtradoCargos = null;
      guardado = true;
      tamano = 330;
      aceptar = true;
      mapParametros.put("paginaAnterior", paginaAnterior);
      activarLov = true;
   }

   public void recibirPaginaEntrante(String pagina) {
      paginaAnterior = pagina;
      listFirmasReportes = null;
      getListFirmasReportes();
      if (listFirmasReportes != null) {
         if (!listFirmasReportes.isEmpty()) {
            firmaReporteSeleccionada = listFirmasReportes.get(0);
         }
      }
   }

   public void recibirParametros(Map<String, Object> map) {
      mapParametros = map;
      paginaAnterior = (String) mapParametros.get("paginaAnterior");
   }

   //Reemplazar la funcion volverAtras, retornarPagina, Redirigir.....Atras.etc
   public void navegar(String pag) {
      FacesContext fc = FacesContext.getCurrentInstance();
      ControlListaNavegacion controlListaNavegacion = (ControlListaNavegacion) fc.getApplication().evaluateExpressionGet(fc, "#{controlListaNavegacion}", ControlListaNavegacion.class);
      String pagActual = "firmareporte";
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
      lovCargos = null;
      lovEmpresas = null;
      lovPersonas = null;
   }

   @PostConstruct
   public void inicializarAdministrador() {
      try {
         FacesContext x = FacesContext.getCurrentInstance();
         HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
         administrarFirmasReportes.obtenerConexion(ses.getId());
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

   public void cambiarIndice(FirmasReportes firma, int celda) {

      if (permitirIndex == true) {
         firmaReporteSeleccionada = firma;
         cualCelda = celda;
         firmaReporteSeleccionada.getSecuencia();
         if (cualCelda == 0) {
            firmaReporteSeleccionada.getCodigo();
            deshabilitarBotonLov();
         } else if (cualCelda == 1) {
            firmaReporteSeleccionada.getDescripcion();
            deshabilitarBotonLov();
         } else if (cualCelda == 2) {
            firmaReporteSeleccionada.getEmpresa().getNombre();
            habilitarLov();
         } else if (cualCelda == 3) {
            deshabilitarBotonLov();
            firmaReporteSeleccionada.getSubtitulofirma();
         } else if (cualCelda == 4) {
            firmaReporteSeleccionada.getPersonafirma().getNombre();
            habilitarLov();
         } else if (cualCelda == 5) {
            habilitarLov();
            firmaReporteSeleccionada.getCargofirma().getNombre();
         }
      }
   }

   public void asignarIndex(FirmasReportes firma, int LND, int dig) {
      firmaReporteSeleccionada = firma;
      tipoActualizacion = LND;
      if (dig == 2) {
         lovEmpresas = null;
         getLovEmpresas();
         contarRegistrosEmpresas();
         RequestContext.getCurrentInstance().update("form:empresasDialogo");
         RequestContext.getCurrentInstance().execute("PF('empresasDialogo').show()");
         dig = -1;
      }
      if (dig == 4) {
         lovPersonas = null;
         getLovPersonas();
         contarRegistrosPersonas();
         RequestContext.getCurrentInstance().update("form:personasDialogo");
         RequestContext.getCurrentInstance().execute("PF('personasDialogo').show()");
         dig = -1;
      }
      if (dig == 5) {
         lovCargos = null;
         getLovCargos();
         contarRegistrosCargos();
         RequestContext.getCurrentInstance().update("form:cargosDialogo");
         RequestContext.getCurrentInstance().execute("PF('cargosDialogo').show()");
         dig = -1;
      }
   }

   public void activarAceptar() {
      aceptar = false;
   }

   public void listaValoresBoton() {
      if (firmaReporteSeleccionada != null) {
         if (cualCelda == 2) {
            lovEmpresas = null;
            getLovEmpresas();
            contarRegistrosEmpresas();
            RequestContext.getCurrentInstance().update("form:empresasDialogo");
            RequestContext.getCurrentInstance().execute("PF('empresasDialogo').show()");
         }
         if (cualCelda == 4) {
            lovPersonas = null;
            getLovPersonas();
            contarRegistrosPersonas();
            RequestContext.getCurrentInstance().update("form:personasDialogo");
            RequestContext.getCurrentInstance().execute("PF('personasDialogo').show()");
         }
         if (cualCelda == 5) {
            lovCargos = null;
            getLovCargos();
            contarRegistrosCargos();
            RequestContext.getCurrentInstance().update("form:cargosDialogo");
            RequestContext.getCurrentInstance().execute("PF('cargosDialogo').show()");
         }
      }
   }

   public void cancelarModificacion() {
      if (bandera == 1) {
         FacesContext c = FacesContext.getCurrentInstance();
         codigo = (Column) c.getViewRoot().findComponent("form:datosFirmasReportes:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosFirmasReportes:descripcion");
         descripcion.setFilterStyle("display: none; visibility: hidden;");
         pais = (Column) c.getViewRoot().findComponent("form:datosFirmasReportes:pais");
         pais.setFilterStyle("display: none; visibility: hidden;");
         subTituloFirma = (Column) c.getViewRoot().findComponent("form:datosFirmasReportes:subTituloFirma");
         subTituloFirma.setFilterStyle("display: none; visibility: hidden;");
         personafir = (Column) c.getViewRoot().findComponent("form:datosFirmasReportes:personafir");
         personafir.setFilterStyle("display: none; visibility: hidden;");
         cargo = (Column) c.getViewRoot().findComponent("form:datosFirmasReportes:cargo");
         cargo.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosFirmasReportes");
         bandera = 0;
         filtrarFirmasReportes = null;
         tipoLista = 0;
      }
      borrarFirmasReportes.clear();
      crearFirmasReportes.clear();
      modificarFirmasReportes.clear();
      firmaReporteSeleccionada = null;
      firmaReporteSeleccionada = null;
      k = 0;
      listFirmasReportes = null;
      guardado = true;
      permitirIndex = true;
      getListFirmasReportes();
      contarRegistros();
      RequestContext.getCurrentInstance().update("form:datosFirmasReportes");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void salir() {
      limpiarListasValor();
      if (bandera == 1) {
         FacesContext c = FacesContext.getCurrentInstance();
         //CERRAR FILTRADO
         codigo = (Column) c.getViewRoot().findComponent("form:datosFirmasReportes:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosFirmasReportes:descripcion");
         descripcion.setFilterStyle("display: none; visibility: hidden;");
         pais = (Column) c.getViewRoot().findComponent("form:datosFirmasReportes:pais");
         pais.setFilterStyle("display: none; visibility: hidden;");
         subTituloFirma = (Column) c.getViewRoot().findComponent("form:datosFirmasReportes:subTituloFirma");
         subTituloFirma.setFilterStyle("display: none; visibility: hidden;");
         personafir = (Column) c.getViewRoot().findComponent("form:datosFirmasReportes:personafir");
         personafir.setFilterStyle("display: none; visibility: hidden;");
         cargo = (Column) c.getViewRoot().findComponent("form:datosFirmasReportes:cargo");
         cargo.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosFirmasReportes");
         bandera = 0;
         filtrarFirmasReportes = null;
         tipoLista = 0;
      }

      borrarFirmasReportes.clear();
      crearFirmasReportes.clear();
      modificarFirmasReportes.clear();
      firmaReporteSeleccionada = null;
      firmaReporteSeleccionada = null;
      k = 0;
      listFirmasReportes = null;
      guardado = true;
      permitirIndex = true;
      getListFirmasReportes();
      contarRegistros();
      RequestContext.getCurrentInstance().update("form:datosFirmasReportes");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void activarCtrlF11() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 0) {
         tamano = 310;
         codigo = (Column) c.getViewRoot().findComponent("form:datosFirmasReportes:codigo");
         codigo.setFilterStyle("width: 85% !important");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosFirmasReportes:descripcion");
         descripcion.setFilterStyle("width: 85% !important");
         pais = (Column) c.getViewRoot().findComponent("form:datosFirmasReportes:pais");
         pais.setFilterStyle("width: 85% !important");
         subTituloFirma = (Column) c.getViewRoot().findComponent("form:datosFirmasReportes:subTituloFirma");
         subTituloFirma.setFilterStyle("width: 85% !important");
         personafir = (Column) c.getViewRoot().findComponent("form:datosFirmasReportes:personafir");
         personafir.setFilterStyle("width: 85% !important");
         cargo = (Column) c.getViewRoot().findComponent("form:datosFirmasReportes:cargo");
         cargo.setFilterStyle("width: 85% !important");
         RequestContext.getCurrentInstance().update("form:datosFirmasReportes");
         bandera = 1;
      } else if (bandera == 1) {
         tamano = 330;
         codigo = (Column) c.getViewRoot().findComponent("form:datosFirmasReportes:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosFirmasReportes:descripcion");
         descripcion.setFilterStyle("display: none; visibility: hidden;");
         pais = (Column) c.getViewRoot().findComponent("form:datosFirmasReportes:pais");
         pais.setFilterStyle("display: none; visibility: hidden;");
         subTituloFirma = (Column) c.getViewRoot().findComponent("form:datosFirmasReportes:subTituloFirma");
         subTituloFirma.setFilterStyle("display: none; visibility: hidden;");
         personafir = (Column) c.getViewRoot().findComponent("form:datosFirmasReportes:personafir");
         personafir.setFilterStyle("display: none; visibility: hidden;");
         cargo = (Column) c.getViewRoot().findComponent("form:datosFirmasReportes:cargo");
         cargo.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosFirmasReportes");
         bandera = 0;
         filtrarFirmasReportes = null;
         tipoLista = 0;
      }
   }

   public void actualizarEmpresa() {
      if (tipoActualizacion == 0) {
         firmaReporteSeleccionada.setEmpresa(empresaSeleccionado);
         if (!crearFirmasReportes.contains(firmaReporteSeleccionada)) {
            if (modificarFirmasReportes.isEmpty()) {
               modificarFirmasReportes.add(firmaReporteSeleccionada);
            } else if (!modificarFirmasReportes.contains(firmaReporteSeleccionada)) {
               modificarFirmasReportes.add(firmaReporteSeleccionada);
            }
         }
         guardado = false;
         permitirIndex = true;
         RequestContext.getCurrentInstance().update("form:datosFirmasReportes");
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      } else if (tipoActualizacion == 1) {
         nuevoFirmasReportes.setEmpresa(empresaSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevoPais");
      } else if (tipoActualizacion == 2) {
         duplicarFirmasReportes.setEmpresa(empresaSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarPais");
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

   public void actualizarPersonas() {
      if (tipoActualizacion == 0) {
         firmaReporteSeleccionada.setPersonafirma(personaSeleccionado);
         if (!crearFirmasReportes.contains(firmaReporteSeleccionada)) {
            if (modificarFirmasReportes.isEmpty()) {
               modificarFirmasReportes.add(firmaReporteSeleccionada);
            } else if (!modificarFirmasReportes.contains(firmaReporteSeleccionada)) {
               modificarFirmasReportes.add(firmaReporteSeleccionada);
            }
         }
         guardado = false;
         permitirIndex = true;
         RequestContext.getCurrentInstance().update("form:datosFirmasReportes");
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      } else if (tipoActualizacion == 1) {
         nuevoFirmasReportes.setPersonafirma(personaSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevoPersona");
      } else if (tipoActualizacion == 2) {
         duplicarFirmasReportes.setPersonafirma(personaSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarPersona");
      }

      filtradoPersonas = null;
      personaSeleccionado = null;
      aceptar = true;

      RequestContext.getCurrentInstance().update("form:personasDialogo");
      RequestContext.getCurrentInstance().update("form:lovPersonas");
      RequestContext.getCurrentInstance().update("form:aceptarPer");
      RequestContext.getCurrentInstance().reset("form:lovPersonas:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovPersonas').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('personasDialogo').hide()");

   }

   public void actualizarCargos() {
      if (tipoActualizacion == 0) {
         firmaReporteSeleccionada.setCargofirma(cargoSeleccionado);
         if (!crearFirmasReportes.contains(firmaReporteSeleccionada)) {
            if (modificarFirmasReportes.isEmpty()) {
               modificarFirmasReportes.add(firmaReporteSeleccionada);
            } else if (!modificarFirmasReportes.contains(firmaReporteSeleccionada)) {
               modificarFirmasReportes.add(firmaReporteSeleccionada);
            }
         }
         guardado = false;
         permitirIndex = true;
         RequestContext.getCurrentInstance().update("form:datosFirmasReportes");
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      } else if (tipoActualizacion == 1) {
         nuevoFirmasReportes.setCargofirma(cargoSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevoCargo");
      } else if (tipoActualizacion == 2) {
         duplicarFirmasReportes.setCargofirma(cargoSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCargo");
      }

      filtradoCargos = null;
      cargoSeleccionado = null;
      aceptar = true;

      RequestContext.getCurrentInstance().update("form:cargosDialogo");
      RequestContext.getCurrentInstance().update("form:lovCargos");
      RequestContext.getCurrentInstance().update("form:aceptarCar");
      RequestContext.getCurrentInstance().reset("form:lovCargos:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovCargos').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('cargosDialogo').hide()");
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

   public void cancelarCambioPersonas() {
      filtradoPersonas = null;
      personaSeleccionado = null;
      aceptar = true;

      RequestContext.getCurrentInstance().update("form:personasDialogo");
      RequestContext.getCurrentInstance().update("form:lovPersonas");
      RequestContext.getCurrentInstance().update("form:aceptarPer");
      RequestContext.getCurrentInstance().reset("form:lovPersonas:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovPersonas').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('personasDialogo').hide()");
   }

   public void cancelarCambioCargos() {
      filtradoCargos = null;
      cargoSeleccionado = null;
      aceptar = true;

      RequestContext.getCurrentInstance().update("form:cargosDialogo");
      RequestContext.getCurrentInstance().update("form:lovCargos");
      RequestContext.getCurrentInstance().update("form:aceptarCar");
      RequestContext.getCurrentInstance().reset("form:lovCargos:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovCargos').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('cargosDialogo').hide()");
   }

   public void modificarFirmasReportes(FirmasReportes firmaR) {
      firmaReporteSeleccionada = firmaR;
      if (!crearFirmasReportes.contains(firmaReporteSeleccionada)) {
         if (modificarFirmasReportes.isEmpty()) {
            modificarFirmasReportes.add(firmaReporteSeleccionada);
         } else if (!modificarFirmasReportes.contains(firmaReporteSeleccionada)) {
            modificarFirmasReportes.add(firmaReporteSeleccionada);
         }
         guardado = false;
         RequestContext.getCurrentInstance().update("form:datosFirmasReportes");
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      }
   }

   public void borrandoFirmasReportes() {
      if (firmaReporteSeleccionada != null) {
         if (!modificarFirmasReportes.isEmpty() && modificarFirmasReportes.contains(firmaReporteSeleccionada)) {
            int modIndex = modificarFirmasReportes.indexOf(firmaReporteSeleccionada);
            modificarFirmasReportes.remove(modIndex);
            borrarFirmasReportes.add(firmaReporteSeleccionada);
         } else if (!crearFirmasReportes.isEmpty() && crearFirmasReportes.contains(firmaReporteSeleccionada)) {
            int crearIndex = crearFirmasReportes.indexOf(firmaReporteSeleccionada);
            crearFirmasReportes.remove(crearIndex);
         } else {
            borrarFirmasReportes.add(firmaReporteSeleccionada);
         }
         listFirmasReportes.remove(firmaReporteSeleccionada);
         if (tipoLista == 1) {
            filtrarFirmasReportes.remove(firmaReporteSeleccionada);
         }
         RequestContext.getCurrentInstance().update("form:datosFirmasReportes");
         contarRegistros();
         firmaReporteSeleccionada = null;
         guardado = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      }

   }

   public void revisarDialogoGuardar() {
      if (!borrarFirmasReportes.isEmpty() || !crearFirmasReportes.isEmpty() || !modificarFirmasReportes.isEmpty()) {
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:confirmarGuardar");
         RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
      }
   }

   public void guardarFirmasReportes() {
      RequestContext context = RequestContext.getCurrentInstance();

      if (guardado == false) {
         log.info("Realizando guardarFirmasReportes");
         if (!borrarFirmasReportes.isEmpty()) {
            administrarFirmasReportes.borrarFirmasReportes(borrarFirmasReportes);
            registrosBorrados = borrarFirmasReportes.size();
            RequestContext.getCurrentInstance().update("form:mostrarBorrados");
            RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
            borrarFirmasReportes.clear();
         }
         if (!modificarFirmasReportes.isEmpty()) {
            administrarFirmasReportes.modificarFirmasReportes(modificarFirmasReportes);
            modificarFirmasReportes.clear();
         }
         if (!crearFirmasReportes.isEmpty()) {
            administrarFirmasReportes.crearFirmasReportes(crearFirmasReportes);
            crearFirmasReportes.clear();
         }
         log.info("Se guardaron los datos con exito");
         listFirmasReportes = null;
         FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
         RequestContext.getCurrentInstance().update("form:datosFirmasReportes");
         k = 0;
         guardado = true;
      }
      firmaReporteSeleccionada = null;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");

   }

   public void editarCelda() {
      if (firmaReporteSeleccionada != null) {
         editarFirmasReportes = firmaReporteSeleccionada;
         if (cualCelda == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editCodigo");
            RequestContext.getCurrentInstance().execute("PF('editCodigo').show()");
            cualCelda = -1;
         } else if (cualCelda == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editDescripcion");
            RequestContext.getCurrentInstance().execute("PF('editDescripcion').show()");
            cualCelda = -1;

         } else if (cualCelda == 2) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editPais");
            RequestContext.getCurrentInstance().execute("PF('editPais').show()");
            cualCelda = -1;
         } else if (cualCelda == 3) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editSubtituloFirma");
            RequestContext.getCurrentInstance().execute("PF('editSubtituloFirma').show()");
            cualCelda = -1;
         } else if (cualCelda == 4) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editPersonas");
            RequestContext.getCurrentInstance().execute("PF('editPersonas').show()");
            cualCelda = -1;
         } else if (cualCelda == 5) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editCargos");
            RequestContext.getCurrentInstance().execute("PF('editCargos').show()");
            cualCelda = -1;
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').shiw()");
      }
   }

   public void agregarNuevoFirmasReportes() {
      int contador = 0;
      int duplicados = 0;

      Integer a = 0;
      a = null;
      mensajeValidacion = " ";
      RequestContext context = RequestContext.getCurrentInstance();
      if (nuevoFirmasReportes.getCodigo() == a) {
         mensajeValidacion = " Los campos marcados con asterisco son obligatorios \n";
      } else {
         for (int x = 0; x < listFirmasReportes.size(); x++) {
            if (listFirmasReportes.get(x).getCodigo() == nuevoFirmasReportes.getCodigo()) {
               duplicados++;
            }
         }
         if (duplicados > 0) {
            mensajeValidacion = " El código ingresado está relacionado con otro Registro. Ingrese un código válido \n";
         } else {
            contador++;
         }
      }
      if (nuevoFirmasReportes.getDescripcion() == null || nuevoFirmasReportes.getDescripcion().isEmpty()) {
         mensajeValidacion = mensajeValidacion + "Los campos marcados con asterisco son obligatorios \n";

      } else {
         contador++;

      }
      if (nuevoFirmasReportes.getEmpresa().getNombre() == null || nuevoFirmasReportes.getEmpresa().getNombre().isEmpty()) {
         mensajeValidacion = " Los campos marcados con asterisco son obligatorios \n";

      } else {
         contador++;

      }
      if (nuevoFirmasReportes.getPersonafirma().getNombre() == null || nuevoFirmasReportes.getPersonafirma().getNombre().isEmpty()) {
         mensajeValidacion = " Los campos marcados con asterisco son obligatorios \n";

      } else {
         contador++;
      }

      if (nuevoFirmasReportes.getCargofirma().getNombre() == null || nuevoFirmasReportes.getCargofirma().getNombre().isEmpty()) {
         mensajeValidacion = " Los campos marcados con asterisco son obligatorios \n";

      } else {
         contador++;

      }
      if (nuevoFirmasReportes.getSubtitulofirma() == null || nuevoFirmasReportes.getSubtitulofirma().isEmpty()) {
         mensajeValidacion = mensajeValidacion + " Los campos marcados con asterisco son obligatorios \n";

      } else {
         contador++;
      }

      if (contador == 6) {
         FacesContext c = FacesContext.getCurrentInstance();
         if (bandera == 1) {
            //CERRAR FILTRADO
            log.info("Desactivar");
            codigo = (Column) c.getViewRoot().findComponent("form:datosFirmasReportes:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosFirmasReportes:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            pais = (Column) c.getViewRoot().findComponent("form:datosFirmasReportes:pais");
            pais.setFilterStyle("display: none; visibility: hidden;");
            subTituloFirma = (Column) c.getViewRoot().findComponent("form:datosFirmasReportes:subTituloFirma");
            subTituloFirma.setFilterStyle("display: none; visibility: hidden;");
            personafir = (Column) c.getViewRoot().findComponent("form:datosFirmasReportes:personafir");
            personafir.setFilterStyle("display: none; visibility: hidden;");
            cargo = (Column) c.getViewRoot().findComponent("form:datosFirmasReportes:cargo");
            cargo.setFilterStyle("display: none; visibility: hidden;");
            bandera = 0;
            filtrarFirmasReportes = null;
            tipoLista = 0;
         }
         k++;
         l = BigInteger.valueOf(k);
         nuevoFirmasReportes.setSecuencia(l);
         crearFirmasReportes.add(nuevoFirmasReportes);
         listFirmasReportes.add(nuevoFirmasReportes);
         firmaReporteSeleccionada = nuevoFirmasReportes;
         contarRegistros();
         RequestContext.getCurrentInstance().update("form:datosFirmasReportes");
         guardado = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         RequestContext.getCurrentInstance().execute("PF('nuevoRegistroFirmasReportes').hide()");
         nuevoFirmasReportes = new FirmasReportes();
         nuevoFirmasReportes.setEmpresa(new Empresas());
         nuevoFirmasReportes.setPersonafirma(new Personas());
         nuevoFirmasReportes.setCargofirma(new Cargos());

      } else {
         RequestContext.getCurrentInstance().update("form:validacionNuevaCentroCosto");
         RequestContext.getCurrentInstance().execute("PF('validacionNuevaCentroCosto').show()");
         contador = 0;
      }
   }

   public void limpiarNuevoFirmasReportes() {
      nuevoFirmasReportes = new FirmasReportes();
      nuevoFirmasReportes.setEmpresa(new Empresas());
      nuevoFirmasReportes.setPersonafirma(new Personas());
      nuevoFirmasReportes.setCargofirma(new Cargos());
   }

   //------------------------------------------------------------------------------
   public void duplicandoFirmasReportes() {
      if (firmaReporteSeleccionada != null) {
         duplicarFirmasReportes = new FirmasReportes();
         duplicarFirmasReportes.setEmpresa(new Empresas());
         duplicarFirmasReportes.setPersonafirma(new Personas());
         duplicarFirmasReportes.setCargofirma(new Cargos());
         k++;
         l = BigInteger.valueOf(k);
         duplicarFirmasReportes.setSecuencia(l);
         duplicarFirmasReportes.setCodigo(firmaReporteSeleccionada.getCodigo());
         duplicarFirmasReportes.setDescripcion(firmaReporteSeleccionada.getDescripcion());
         duplicarFirmasReportes.setEmpresa(firmaReporteSeleccionada.getEmpresa());
         duplicarFirmasReportes.setSubtitulofirma(firmaReporteSeleccionada.getSubtitulofirma());
         duplicarFirmasReportes.setPersonafirma(firmaReporteSeleccionada.getPersonafirma());
         duplicarFirmasReportes.setCargofirma(firmaReporteSeleccionada.getCargofirma());
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTE");
         RequestContext.getCurrentInstance().execute("PF('duplicarRegistroFirmasReportes').show()");
      }
   }

   public void confirmarDuplicar() {
      int contador = 0;
      mensajeValidacion = " ";
      int duplicados = 0;
      Integer a = 0;
      a = null;
      if (duplicarFirmasReportes.getCodigo() == a) {
         mensajeValidacion = mensajeValidacion + " Los campos marcados con asterisco son obligatorios \n";
      } else {
         for (int x = 0; x < listFirmasReportes.size(); x++) {
            if (listFirmasReportes.get(x).getCodigo() == duplicarFirmasReportes.getCodigo()) {
               duplicados++;
            }
         }
         if (duplicados > 0) {
            mensajeValidacion = "El código ingresado está relacionado con otro Registro. Ingrese un código válido \n";
         } else {
            contador++;
            duplicados = 0;
         }
      }
      if (duplicarFirmasReportes.getDescripcion() == null || duplicarFirmasReportes.getDescripcion().equals(" ")) {
         mensajeValidacion = "Los campos marcados con asterisco son obligatorios \n";
      } else {
         contador++;
      }
      if (duplicarFirmasReportes.getEmpresa().getNombre() == null || duplicarFirmasReportes.getEmpresa().getNombre().isEmpty()) {
         mensajeValidacion = "Los campos marcados con asterisco son obligatorios \n";
      } else {
         contador++;
      }
      if (duplicarFirmasReportes.getPersonafirma().getNombre() == null || duplicarFirmasReportes.getPersonafirma().getNombre().isEmpty()) {
         mensajeValidacion = "Los campos marcados con asterisco son obligatorios \n";
      } else {
         contador++;
      }
      if (duplicarFirmasReportes.getCargofirma().getNombre() == null || duplicarFirmasReportes.getEmpresa().getNombre().isEmpty()) {
         mensajeValidacion = " Los campos marcados con asterisco son obligatorios \n";
      } else {
         contador++;
      }
      if (duplicarFirmasReportes.getSubtitulofirma() == null || duplicarFirmasReportes.getSubtitulofirma().isEmpty()) {
         mensajeValidacion = "Los campos marcados con asterisco son obligatorios \n";
      } else {
         contador++;
      }
      if (contador == 6) {
         listFirmasReportes.add(duplicarFirmasReportes);
         crearFirmasReportes.add(duplicarFirmasReportes);
         contarRegistros();
         RequestContext.getCurrentInstance().update("form:datosFirmasReportes");
         firmaReporteSeleccionada = duplicarFirmasReportes;
         guardado = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            //CERRAR FILTRADO
            codigo = (Column) c.getViewRoot().findComponent("form:datosFirmasReportes:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosFirmasReportes:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            pais = (Column) c.getViewRoot().findComponent("form:datosFirmasReportes:pais");
            pais.setFilterStyle("display: none; visibility: hidden;");
            subTituloFirma = (Column) c.getViewRoot().findComponent("form:datosFirmasReportes:subTituloFirma");
            subTituloFirma.setFilterStyle("display: none; visibility: hidden;");
            personafir = (Column) c.getViewRoot().findComponent("form:datosFirmasReportes:personafir");
            personafir.setFilterStyle("display: none; visibility: hidden;");
            cargo = (Column) c.getViewRoot().findComponent("form:datosFirmasReportes:cargo");
            cargo.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosFirmasReportes");
            bandera = 0;
            filtrarFirmasReportes = null;
            tipoLista = 0;
         }
         duplicarFirmasReportes = new FirmasReportes();
         duplicarFirmasReportes.setEmpresa(new Empresas());
         duplicarFirmasReportes.setCargofirma(new Cargos());
         duplicarFirmasReportes.setPersonafirma(new Personas());
         RequestContext.getCurrentInstance().execute("PF('duplicarRegistroFirmasReportes').hide()");
      } else {
         contador = 0;
         RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
         RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
      }
   }

   public void limpiarDuplicarFirmasReportes() {
      duplicarFirmasReportes = new FirmasReportes();
      duplicarFirmasReportes.setEmpresa(new Empresas());
      duplicarFirmasReportes.setPersonafirma(new Personas());
      duplicarFirmasReportes.setCargofirma(new Cargos());
   }

   public void exportPDF() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosFirmasReportesExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "FIRMASREPORTES", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void exportXLS() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosFirmasReportesExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "FIRMASREPORTES", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void verificarRastro() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (firmaReporteSeleccionada != null) {
         int resultado = administrarRastros.obtenerTabla(firmaReporteSeleccionada.getSecuencia(), "FIRMASREPORTES"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
      } else if (administrarRastros.verificarHistoricosTabla("FIRMASREPORTES")) { // igual acá
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
   }

   public void contarRegistros() {
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
   }

   public void contarRegistrosEmpresas() {
      RequestContext.getCurrentInstance().update("form:infoLOVEmpresa");
   }

   public void contarRegistrosCargos() {
      RequestContext.getCurrentInstance().update("form:infoLOVCargo");
   }

   public void contarRegistrosPersonas() {
      RequestContext.getCurrentInstance().update("form:infoLOVPersona");
   }

   public void habilitarLov() {
      activarLov = false;
      RequestContext.getCurrentInstance().update("form:listaValores");
   }

   public void deshabilitarBotonLov() {
      activarLov = true;
      RequestContext.getCurrentInstance().update("form:listaValores");
   }

   //*/*/*/*/*/*/*/*/*/*-/-*//-*/-*/*/*-*/-*/-*/*/*/*/*/---/*/*/*/*/-*/-*/-*/-*/-*/
   public List<FirmasReportes> getListFirmasReportes() {
      if (listFirmasReportes == null) {
         listFirmasReportes = administrarFirmasReportes.consultarFirmasReportes();
      }
      return listFirmasReportes;
   }

   public void setListFirmasReportes(List<FirmasReportes> listFirmasReportes) {
      this.listFirmasReportes = listFirmasReportes;
   }

   public List<FirmasReportes> getFiltrarFirmasReportes() {
      return filtrarFirmasReportes;
   }

   public void setFiltrarFirmasReportes(List<FirmasReportes> filtrarFirmasReportes) {
      this.filtrarFirmasReportes = filtrarFirmasReportes;
   }

   public FirmasReportes getNuevoFirmasReportes() {
      return nuevoFirmasReportes;
   }

   public void setNuevoFirmasReportes(FirmasReportes nuevoFirmasReportes) {
      this.nuevoFirmasReportes = nuevoFirmasReportes;
   }

   public FirmasReportes getDuplicarFirmasReportes() {
      return duplicarFirmasReportes;
   }

   public void setDuplicarFirmasReportes(FirmasReportes duplicarFirmasReportes) {
      this.duplicarFirmasReportes = duplicarFirmasReportes;
   }

   public FirmasReportes getEditarFirmasReportes() {
      return editarFirmasReportes;
   }

   public void setEditarFirmasReportes(FirmasReportes editarFirmasReportes) {
      this.editarFirmasReportes = editarFirmasReportes;
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
         lovEmpresas = administrarFirmasReportes.consultarLOVEmpresas();
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

   public List<Personas> getLovPersonas() {
      if (lovPersonas == null) {
         lovPersonas = administrarFirmasReportes.consultarLOVPersonas();
      }
      return lovPersonas;
   }

   public void setLovPersonas(List<Personas> lovPersonas) {
      this.lovPersonas = lovPersonas;
   }

   public List<Personas> getFiltradoPersonas() {
      return filtradoPersonas;
   }

   public void setFiltradoPersonas(List<Personas> filtradoPersonas) {
      this.filtradoPersonas = filtradoPersonas;
   }

   public Personas getPersonaSeleccionado() {
      return personaSeleccionado;
   }

   public void setPersonaSeleccionado(Personas personaSeleccionado) {
      this.personaSeleccionado = personaSeleccionado;
   }

   public List<Cargos> getLovCargos() {
      if (lovCargos == null) {
         lovCargos = administrarFirmasReportes.consultarLOVCargos();
      }
      return lovCargos;
   }

   public void setLovCargos(List<Cargos> lovCargos) {
      this.lovCargos = lovCargos;
   }

   public List<Cargos> getFiltradoCargos() {
      return filtradoCargos;
   }

   public void setFiltradoCargos(List<Cargos> filtradoCargos) {
      this.filtradoCargos = filtradoCargos;
   }

   public Cargos getCargoSeleccionado() {
      return cargoSeleccionado;
   }

   public void setCargoSeleccionado(Cargos cargoSeleccionado) {
      this.cargoSeleccionado = cargoSeleccionado;
   }

   public FirmasReportes getFirmaReporteSeleccionada() {
      return firmaReporteSeleccionada;
   }

   public void setFirmaReporteSeleccionada(FirmasReportes firmaReporteSeleccionada) {
      this.firmaReporteSeleccionada = firmaReporteSeleccionada;
   }

   public String getInfoRegistro() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosFirmasReportes");
      infoRegistro = String.valueOf(tabla.getRowCount());
      return infoRegistro;
   }

   public void setInfoRegistro(String infoRegistro) {
      this.infoRegistro = infoRegistro;
   }

   public String getInfoLOVCargo() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovCargos");
      infoLOVCargo = String.valueOf(tabla.getRowCount());
      return infoLOVCargo;
   }

   public void setInfoLOVCargo(String infoLOVCargo) {
      this.infoLOVCargo = infoLOVCargo;
   }

   public String getInfoLOVPersona() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovPersonas");
      infoLOVPersona = String.valueOf(tabla.getRowCount());
      return infoLOVPersona;
   }

   public void setInfoLOVPersona(String infoLOVPersona) {
      this.infoLOVPersona = infoLOVPersona;
   }

   public String getInfoLOVEmpresa() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovEmpresas");
      infoLOVEmpresa = String.valueOf(tabla.getRowCount());
      return infoLOVEmpresa;
   }

   public void setInfoLOVEmpresa(String infoLOVEmpresa) {
      this.infoLOVEmpresa = infoLOVEmpresa;
   }

   public boolean isAceptar() {
      return aceptar;
   }

   public void setAceptar(boolean aceptar) {
      this.aceptar = aceptar;
   }

   public boolean isActivarLov() {
      return activarLov;
   }

   public void setActivarLov(boolean activarLov) {
      this.activarLov = activarLov;
   }
}
