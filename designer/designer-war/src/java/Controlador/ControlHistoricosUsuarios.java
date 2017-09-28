/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import ControlNavegacion.ControlListaNavegacion;
import Entidades.HistoricosUsuarios;
import Entidades.Perfiles;
import Entidades.Personas;
import Entidades.Usuarios;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministarReportesInterface;
import InterfaceAdministrar.AdministrarHistoricosUsuariosInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.fill.AsynchronousFilllListener;
import org.apache.log4j.Logger;
import org.primefaces.component.column.Column;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.export.Exporter;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author user
 */
@Named(value = "controlHistoricosUsuarios")
@SessionScoped
public class ControlHistoricosUsuarios implements Serializable {

   private static Logger log = Logger.getLogger(ControlHistoricosUsuarios.class);

   @EJB
   AdministrarHistoricosUsuariosInterface administrarHistoricosUsuarios;
   @EJB
   AdministrarRastrosInterface administrarRastros;
   @EJB
   AdministarReportesInterface administarReportes;

   ////lista Históricos usuarios
   private List<HistoricosUsuarios> listaHistoricosUsuarios;
   private List<HistoricosUsuarios> listaHistoricosUsuariosFiltrar;
   private List<HistoricosUsuarios> listaHistoricosUsuariosCrear;
   private List<HistoricosUsuarios> listaHistoricosUsuariosModificar;
   private List<HistoricosUsuarios> listaHistoricosUsuariosBorrar;
   private HistoricosUsuarios historicoUsuarioSeleccionado;
   private HistoricosUsuarios nuevoHistoricoUsuario;
   private HistoricosUsuarios duplicarHistoricoUsuario;
   private HistoricosUsuarios editarHistoricoUsuario;
   //LISTA DE VALORES DE PERSONAS!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
   private List<Personas> lovPersonas;
   private List<Personas> filtradoLovPersonas;
   private Personas personasLovSeleccionado;
   //lov usuarios
   private List<Usuarios> lovUsuarios;
   private List<Usuarios> filtrarUsuariosLov;
   private Usuarios usuarioLovSeleccionado;
   //lov perfiles
   private List<Perfiles> lovPerfiles;
   private List<Perfiles> filtrarPerfilesLov;
   private Perfiles perfilLovSeleccionado;
   //
   private String altoTabla;
   private String infoRegistroPersonas, infoRegistroHistoricos, infoRegistroAlias, infoRegistroPerfiles;
   private String tablaImprimir, nombreArchivo;
   private Column fechainicial, fechafinal, alias, perfil, nombrepersona, descripcion;
   private int cualCelda, tipoLista, tipoActualizacion, k, bandera;
   private BigInteger l;
   private boolean aceptar, guardado, activarLov;
   private Map<String, Object> mapParametros;
   private String paginaAnterior;

   private StreamedContent reporte;
   private String pathReporteGenerado = null;
   private String nombreReporte, tipoReporte;
   private String cabezeraVisor;
   private boolean estadoReporte;
   private String resultadoReporte, mensajeValidacion;
   private Usuarios UsuarioAux;
   private String userAgent;
   private ExternalContext externalContext;

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
         administrarRastros.obtenerConexion(ses.getId());
         administarReportes.obtenerConexion(ses.getId());
         administrarHistoricosUsuarios.obtenerConexion(ses.getId());
         externalContext = x.getExternalContext();
         userAgent = externalContext.getRequestHeaderMap().get("User-Agent");
      } catch (Exception e) {
         log.error("Error postconstruct " + this.getClass().getName() + ":  ", e);
         log.error("Causa: " + e.getCause());
      }
   }

   public ControlHistoricosUsuarios() {
      aceptar = true;
      activarLov = true;
      tipoLista = 0;
      cualCelda = -1;
      k = 0;
      altoTabla = "315";
      guardado = true;
      listaHistoricosUsuarios = null;
      listaHistoricosUsuariosCrear = new ArrayList<HistoricosUsuarios>();
      listaHistoricosUsuariosModificar = new ArrayList<HistoricosUsuarios>();
      listaHistoricosUsuariosBorrar = new ArrayList<HistoricosUsuarios>();
      nuevoHistoricoUsuario = new HistoricosUsuarios();
      nuevoHistoricoUsuario.setUsuario(new Usuarios());
      nuevoHistoricoUsuario.setPerfil(new Perfiles());
      nuevoHistoricoUsuario.setPersona(new Personas());
//        nuevoHistoricoUsuario.setDescripcion("REGISTRO MANUAL");
      duplicarHistoricoUsuario = new HistoricosUsuarios();
      duplicarHistoricoUsuario.setUsuario(new Usuarios());
      duplicarHistoricoUsuario.setPerfil(new Perfiles());
      duplicarHistoricoUsuario.setPersona(new Personas());
      editarHistoricoUsuario = new HistoricosUsuarios();
      lovPersonas = null;
      personasLovSeleccionado = null;
      tablaImprimir = ":formExportar:datosHistoricosUsuariosExportar";
      nombreArchivo = "HistoricoUsuario";
      paginaAnterior = "nominaf";
      mapParametros = new LinkedHashMap<String, Object>();
      mapParametros.put("paginaAnterior", paginaAnterior);
      nombreReporte = "HistoricoUsuario";
      tipoReporte = "PDF";
      estadoReporte = false;
      mensajeValidacion = "";
      lovUsuarios = null;
      usuarioLovSeleccionado = null;
      lovPerfiles = null;
      perfilLovSeleccionado = null;
   }

   public void limpiarListasValor() {
      lovPerfiles = null;
      lovPersonas = null;
      lovUsuarios = null;
   }

   public void recibirPaginaEntrante(String pagina) {
      paginaAnterior = pagina;
   }

   public void recibirParametros(Map<String, Object> map) {
      mapParametros = map;
      paginaAnterior = (String) mapParametros.get("paginaAnterior");
      //inicializarCosas(); Inicializar cosas de ser necesario
   }

   public void navegar(String pag) {
      FacesContext fc = FacesContext.getCurrentInstance();
      ControlListaNavegacion controlListaNavegacion = (ControlListaNavegacion) fc.getApplication().evaluateExpressionGet(fc, "#{controlListaNavegacion}", ControlListaNavegacion.class);
      String pagActual = "historicosusuarios";
      // mapParametros.put("paginaAnterior", pagActual);
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

   public void recibirUsuario(String pagina, Usuarios Usuarioparam) {
      paginaAnterior = pagina;
      UsuarioAux = Usuarioparam;
      listaHistoricosUsuarios = null;
      getListaHistoricosUsuarios();
      if (listaHistoricosUsuarios != null) {
         if (!listaHistoricosUsuarios.isEmpty()) {
            historicoUsuarioSeleccionado = listaHistoricosUsuarios.get(0);
         }
      }
   }

   public String redirigir() {
      return paginaAnterior;
   }

   public void activarAceptar() {
      aceptar = false;
   }

   public void activarCrtlF11() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 0) {
         fechainicial = (Column) c.getViewRoot().findComponent("form:datosHistoricosUsuarios:persona");
         fechainicial.setFilterStyle("width: 85% !important");
         fechafinal = (Column) c.getViewRoot().findComponent("form:datosHistoricosUsuarios:perfil");
         fechafinal.setFilterStyle("width: 85% !important");
         alias = (Column) c.getViewRoot().findComponent("form:datosHistoricosUsuarios:alias");
         alias.setFilterStyle("width: 85% !important");
         perfil = (Column) c.getViewRoot().findComponent("form:datosHistoricosUsuarios:pantalla");
         perfil.setFilterStyle("width: 85% !important");
         nombrepersona = (Column) c.getViewRoot().findComponent("form:datosHistoricosUsuarios:pantalla");
         nombrepersona.setFilterStyle("width: 85% !important");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosHistoricosUsuarios:pantalla");
         descripcion.setFilterStyle("width: 85% !important");
         altoTabla = "295";
         RequestContext.getCurrentInstance().update("form:datosHistoricosUsuarios");
         bandera = 1;
         tipoLista = 1;
      } else if (bandera == 1) {
         fechainicial = (Column) c.getViewRoot().findComponent("form:datosHistoricosUsuarios:persona");
         fechainicial.setFilterStyle("display: none; visibility: hidden;");
         fechafinal = (Column) c.getViewRoot().findComponent("form:datosHistoricosUsuarios:perfil");
         fechafinal.setFilterStyle("display: none; visibility: hidden;");
         alias = (Column) c.getViewRoot().findComponent("form:datosHistoricosUsuarios:alias");
         alias.setFilterStyle("display: none; visibility: hidden;");
         perfil = (Column) c.getViewRoot().findComponent("form:datosHistoricosUsuarios:pantalla");
         perfil.setFilterStyle("display: none; visibility: hidden;");
         nombrepersona = (Column) c.getViewRoot().findComponent("form:datosHistoricosUsuarios:pantalla");
         nombrepersona.setFilterStyle("display: none; visibility: hidden;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosHistoricosUsuarios:pantalla");
         descripcion.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosHistoricosUsuarios");
         altoTabla = "315";
         bandera = 0;
         listaHistoricosUsuariosFiltrar = null;
         tipoLista = 0;
      }
   }

   public void eventofiltrar() {
      if (tipoLista == 0) {
         tipoLista = 1;
      }
      contarRegistros();
   }

   public void cambiarIndice(HistoricosUsuarios histousuario, int celda) {
      historicoUsuarioSeleccionado = histousuario;
      cualCelda = celda;
      tablaImprimir = ":formExportar:datosHistoricosUsuariosExportar";
      nombreArchivo = "UsuariosXML";
      historicoUsuarioSeleccionado.getSecuencia();
      if (cualCelda == 0) {
         deshabilitarBotonLov();
         historicoUsuarioSeleccionado.getFechainicial();
      } else if (cualCelda == 1) {
         deshabilitarBotonLov();
         historicoUsuarioSeleccionado.getFechafinal();
      } else if (cualCelda == 2) {
         deshabilitarBotonLov();
         historicoUsuarioSeleccionado.getUsuario().getAlias();
      } else if (cualCelda == 3) {
         deshabilitarBotonLov();
         historicoUsuarioSeleccionado.getPerfil();
      } else if (cualCelda == 3) {
         habilitarBotonLov();
         historicoUsuarioSeleccionado.getPersona().getNombreCompleto();
      }
   }

   public void editarCelda() {
      if (historicoUsuarioSeleccionado != null) {
         editarHistoricoUsuario = historicoUsuarioSeleccionado;
         if (cualCelda == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaInicial");
            RequestContext.getCurrentInstance().execute("PF('editarFechaInicial').show()");
            cualCelda = -1;
         } else if (cualCelda == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaFinal");
            RequestContext.getCurrentInstance().execute("PF('editarFechaFinal').show()");
            cualCelda = -1;
         } else if (cualCelda == 2) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarAlias");
            RequestContext.getCurrentInstance().execute("PF('editarAlias').show()");
            cualCelda = -1;
         } else if (cualCelda == 3) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarPerfil");
            RequestContext.getCurrentInstance().execute("PF('editarPerfil').show()");
            cualCelda = -1;
         } else if (cualCelda == 4) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarPersona");
            RequestContext.getCurrentInstance().execute("PF('editarPersona').show()");
            cualCelda = -1;
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void asignarIndex(HistoricosUsuarios historicosu, int dlg, int LND) {
      historicoUsuarioSeleccionado = historicosu;
      tipoActualizacion = LND;
      if (dlg == 0) {
         lovPersonas = null;
         contarRegistrosPersonas();
         RequestContext.getCurrentInstance().update("formularioDialogos:personasDialogo");
         RequestContext.getCurrentInstance().execute("PF('personasDialogo').show()");
      } else if (dlg == 1) {
         lovUsuarios = null;
         contarRegistrosAlias();
         RequestContext.getCurrentInstance().update("formularioDialogos:aliasDialogo");
         RequestContext.getCurrentInstance().execute("PF('aliasDialogo').show()");
      } else if (dlg == 2) {
         lovPerfiles = null;
         contarRegistrosPerfiles();
         RequestContext.getCurrentInstance().update("formularioDialogos:perfilesDialogo");
         RequestContext.getCurrentInstance().execute("PF('perfilesDialogo').show()");
      }
   }

   public void actualizarPersonas() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoActualizacion == 0) {
         historicoUsuarioSeleccionado.setPersona(personasLovSeleccionado);
         if (!listaHistoricosUsuariosCrear.contains(historicoUsuarioSeleccionado)) {
            if (listaHistoricosUsuariosModificar.isEmpty()) {
               listaHistoricosUsuariosModificar.add(historicoUsuarioSeleccionado);
            } else if (!listaHistoricosUsuariosModificar.contains(historicoUsuarioSeleccionado)) {
               listaHistoricosUsuariosModificar.add(historicoUsuarioSeleccionado);
            }
         }
         guardado = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         RequestContext.getCurrentInstance().update("form:datosHistoricosUsuarios");
      } else if (tipoActualizacion == 1) {
         nuevoHistoricoUsuario.setPersona(personasLovSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaUsuario");
      } else if (tipoActualizacion == 2) {
         duplicarHistoricoUsuario.setPersona(personasLovSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarUsuario");
      }
      filtradoLovPersonas = null;
      personasLovSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;

      RequestContext.getCurrentInstance().update("formularioDialogos:personasDialogo");
      RequestContext.getCurrentInstance().update("formularioDialogos:LOVPersonas");
      RequestContext.getCurrentInstance().update("formularioDialogos:aceptarPS");
      context.reset("formularioDialogos:LOVPersonas:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('LOVPersonas').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('personasDialogo').hide()");
   }

   public void cancelarCambioPersona() {
      filtradoLovPersonas = null;
      personasLovSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("formularioDialogos:personasDialogo");
      RequestContext.getCurrentInstance().update("formularioDialogos:LOVPersonas");
      RequestContext.getCurrentInstance().update("formularioDialogos:aceptarPS");
      context.reset("formularioDialogos:LOVPersonas:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('LOVPersonas').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('personasDialogo').hide()");
   }

   public void actualizarUsuario() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoActualizacion == 1) {
         nuevoHistoricoUsuario.setUsuario(usuarioLovSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaUsuario");
      } else if (tipoActualizacion == 2) {
         duplicarHistoricoUsuario.setUsuario(usuarioLovSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarUsuario");
      }
      filtradoLovPersonas = null;
      personasLovSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;

      RequestContext.getCurrentInstance().update("formularioDialogos:aliasDialogo");
      RequestContext.getCurrentInstance().update("formularioDialogos:LOVUsuariosAlias");
      RequestContext.getCurrentInstance().update("formularioDialogos:aceptarAU");
      context.reset("formularioDialogos:LOVUsuariosAlias:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('LOVUsuariosAlias').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('aliasDialogo').hide()");
   }

   public void cancelarCambioUsuario() {
      filtradoLovPersonas = null;
      personasLovSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("formularioDialogos:aliasDialogo");
      RequestContext.getCurrentInstance().update("formularioDialogos:LOVUsuariosAlias");
      RequestContext.getCurrentInstance().update("formularioDialogos:aceptarAU");
      context.reset("formularioDialogos:LOVUsuariosAlias:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('LOVUsuariosAlias').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('aliasDialogo').hide()");
   }

   public void actualizarPerfiles() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoActualizacion == 1) {
         nuevoHistoricoUsuario.setPerfil(perfilLovSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaUsuario");
      } else if (tipoActualizacion == 2) {
         duplicarHistoricoUsuario.setPerfil(perfilLovSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarUsuario");
      }
      filtrarPerfilesLov = null;
      perfilLovSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;
      RequestContext.getCurrentInstance().update("formularioDialogos:perfilesDialogo");
      RequestContext.getCurrentInstance().update("formularioDialogos:LOVPerfiles");
      RequestContext.getCurrentInstance().update("formularioDialogos:aceptarPF");
      context.reset("formularioDialogos:LOVPerfiles:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('LOVPerfiles').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('perfilesDialogo').hide()");
   }

   public void cancelarCambioPerfil() {
      filtrarPerfilesLov = null;
      perfilLovSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("formularioDialogos:perfilesDialogo");
      RequestContext.getCurrentInstance().update("formularioDialogos:LOVPerfiles");
      RequestContext.getCurrentInstance().update("formularioDialogos:aceptarPF");
      context.reset("formularioDialogos:LOVPerfiles:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('LOVPerfiles').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('perfilesDialogo').hide()");
   }

   public void listaValoresBoton() {
      if (historicoUsuarioSeleccionado != null) {
         if (cualCelda == 4) {
            lovPersonas = null;
            getLovPersonas();
            RequestContext.getCurrentInstance().update("formularioDialogos:personasDialogo");
            RequestContext.getCurrentInstance().execute("PF('personasDialogo').show()");
            tipoActualizacion = 0;
         }
      }
   }

   public void modificarHistoricosUsuarios(HistoricosUsuarios historicosusuario) {
      historicoUsuarioSeleccionado = historicosusuario;
      if (!listaHistoricosUsuariosCrear.contains(historicoUsuarioSeleccionado)) {
         if (listaHistoricosUsuariosModificar.isEmpty()) {
            listaHistoricosUsuariosModificar.add(historicoUsuarioSeleccionado);
         } else if (!listaHistoricosUsuariosModificar.contains(historicoUsuarioSeleccionado)) {
            listaHistoricosUsuariosModificar.add(historicoUsuarioSeleccionado);
         }
         guardado = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      }
      RequestContext.getCurrentInstance().update("form:datosHistoricosUsuarios");
   }

   public void exportPDF() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosHistoricosUsuariosExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "HistoricosUsuariosPDF", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void exportXLS() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosHistoricosUsuariosExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "HistoricosUsuariosXLS", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void limpiarNuevoHistoricosUsuarios() {
      nuevoHistoricoUsuario = new HistoricosUsuarios();
      nuevoHistoricoUsuario.setUsuario(new Usuarios());
      nuevoHistoricoUsuario.setPerfil(new Perfiles());
      nuevoHistoricoUsuario.setPersona(new Personas());
//        nuevoHistoricoUsuario.setDescripcion("REGISTRO MANUAL");
   }

   public void limpiarDuplicarHistoricosUsuarios() {
      duplicarHistoricoUsuario = new HistoricosUsuarios();
      duplicarHistoricoUsuario.setUsuario(new Usuarios());
      duplicarHistoricoUsuario.setPerfil(new Perfiles());
      duplicarHistoricoUsuario.setPersona(new Personas());
//        duplicarHistoricoUsuario.setDescripcion("REGISTRO MANUAL");
   }

   public void guardarCambiosUsuario() {
      try {
         if (guardado == false) {
            if (!listaHistoricosUsuariosBorrar.isEmpty()) {
               administrarHistoricosUsuarios.borrarHistoricosUsuarios(listaHistoricosUsuariosBorrar);
               listaHistoricosUsuariosBorrar.clear();
            }
            if (!listaHistoricosUsuariosCrear.isEmpty()) {
               administrarHistoricosUsuarios.crearHistoricosUsuarios(listaHistoricosUsuariosCrear);
               listaHistoricosUsuariosCrear.clear();
            }
            if (!listaHistoricosUsuariosModificar.isEmpty()) {
               administrarHistoricosUsuarios.modificarHistoricosUsuarios(listaHistoricosUsuariosModificar);
               listaHistoricosUsuariosModificar.clear();
            }
            listaHistoricosUsuarios = null;
            getListaHistoricosUsuarios();
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:datosHistoricosUsuarios");
            guardado = true;
            FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            k = 0;
            historicoUsuarioSeleccionado = null;
         }
      } catch (Exception e) {
         FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado, intente nuevamente.");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      }
   }

   public void borrarHistoricoUsuario() {
      if (historicoUsuarioSeleccionado != null) {
         if (!listaHistoricosUsuariosModificar.isEmpty() && listaHistoricosUsuariosModificar.contains(historicoUsuarioSeleccionado)) {
            int modIndex = listaHistoricosUsuariosModificar.indexOf(historicoUsuarioSeleccionado);
            listaHistoricosUsuariosModificar.remove(modIndex);
            listaHistoricosUsuariosBorrar.add(historicoUsuarioSeleccionado);
         } else if (!listaHistoricosUsuariosCrear.isEmpty() && listaHistoricosUsuariosCrear.contains(historicoUsuarioSeleccionado)) {
            int crearIndex = listaHistoricosUsuariosCrear.indexOf(historicoUsuarioSeleccionado);
            listaHistoricosUsuariosCrear.remove(crearIndex);
         } else {
            listaHistoricosUsuariosBorrar.add(historicoUsuarioSeleccionado);
         }
         listaHistoricosUsuarios.remove(historicoUsuarioSeleccionado);

         if (tipoLista == 1) {
            listaHistoricosUsuariosFiltrar.remove(historicoUsuarioSeleccionado);
         }
         historicoUsuarioSeleccionado = null;
         contarRegistros();
         RequestContext.getCurrentInstance().update("form:datosHistoricosUsuarios");
         guardado = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      }
   }

   public void agregarNuevoHistoricoUsuario() {
      int pasa = 0;
      mensajeValidacion = " ";
      if (nuevoHistoricoUsuario.getPersona().getNombreCompleto() == null || nuevoHistoricoUsuario.getPersona().getNombreCompleto().equals("")) {
         mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
         pasa++;
      }
      if (nuevoHistoricoUsuario.getPerfil().getDescripcion() == null || nuevoHistoricoUsuario.getPerfil().getDescripcion().equals("")) {
         mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
         pasa++;
      }
      if (nuevoHistoricoUsuario.getUsuario().getAlias() == null || nuevoHistoricoUsuario.getUsuario().getAlias().equals("")) {
         mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
         pasa++;
      }
      if (nuevoHistoricoUsuario.getFechainicial() == null) {
         mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
         pasa++;
      }
      if (pasa == 0) {
         if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            fechainicial = (Column) c.getViewRoot().findComponent("form:datosHistoricosUsuarios:persona");
            fechainicial.setFilterStyle("display: none; visibility: hidden;");
            fechafinal = (Column) c.getViewRoot().findComponent("form:datosHistoricosUsuarios:perfil");
            fechafinal.setFilterStyle("display: none; visibility: hidden;");
            alias = (Column) c.getViewRoot().findComponent("form:datosHistoricosUsuarios:alias");
            alias.setFilterStyle("display: none; visibility: hidden;");
            perfil = (Column) c.getViewRoot().findComponent("form:datosHistoricosUsuarios:pantalla");
            perfil.setFilterStyle("display: none; visibility: hidden;");
            nombrepersona = (Column) c.getViewRoot().findComponent("form:datosHistoricosUsuarios:pantalla");
            nombrepersona.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosHistoricosUsuarios:pantalla");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            altoTabla = "315";
            RequestContext.getCurrentInstance().update("form:datosHistoricosUsuarios");
            bandera = 0;
            listaHistoricosUsuariosFiltrar = null;
            tipoLista = 0;
         }
         k++;
         l = BigInteger.valueOf(k);
         nuevoHistoricoUsuario.setSecuencia(l);
         nuevoHistoricoUsuario.setUsuario(UsuarioAux);
         listaHistoricosUsuariosCrear.add(nuevoHistoricoUsuario);
         listaHistoricosUsuarios.add(nuevoHistoricoUsuario);
         historicoUsuarioSeleccionado = nuevoHistoricoUsuario;
         contarRegistros();
         nuevoHistoricoUsuario = new HistoricosUsuarios();
         RequestContext.getCurrentInstance().update("form:datosHistoricosUsuarios");
         guardado = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroUsuario");
         RequestContext.getCurrentInstance().execute("PF('NuevoRegistroUsuario').hide()");
      } else {
         RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevaUsuario");
         RequestContext.getCurrentInstance().execute("PF('validacionNuevaUsuario').show()");
      }
   }

   public void duplicandoHistoricoUsuario() {
      if (historicoUsuarioSeleccionado != null) {
         duplicarHistoricoUsuario = new HistoricosUsuarios();
         duplicarHistoricoUsuario.setFechainicial(historicoUsuarioSeleccionado.getFechainicial());
         duplicarHistoricoUsuario.setFechafinal(historicoUsuarioSeleccionado.getFechafinal());
         duplicarHistoricoUsuario.setUsuario(historicoUsuarioSeleccionado.getUsuario());
         duplicarHistoricoUsuario.setPerfil(historicoUsuarioSeleccionado.getPerfil());
         duplicarHistoricoUsuario.setPersona(historicoUsuarioSeleccionado.getPersona());
         duplicarHistoricoUsuario.setDescripcion(historicoUsuarioSeleccionado.getDescripcion());
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarUsuario");
         RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroUsuario').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void confirmarDuplicar() {
      int pasa = 0;
      mensajeValidacion = " ";
      if (duplicarHistoricoUsuario.getPersona().getNombreCompleto() == null || duplicarHistoricoUsuario.getPersona().getNombreCompleto().equals("")) {
         mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
         pasa++;
      }
      if (duplicarHistoricoUsuario.getPerfil().getDescripcion() == null || duplicarHistoricoUsuario.getPerfil().getDescripcion().equals("")) {
         mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
         pasa++;
      }
      if (duplicarHistoricoUsuario.getUsuario().getAlias() == null || duplicarHistoricoUsuario.getUsuario().getAlias().equals("")) {
         mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
         pasa++;
      }

      if (duplicarHistoricoUsuario.getFechainicial() == null) {
         mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
         pasa++;
      }

      if (pasa == 0) {
         guardado = false;
         if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            fechainicial = (Column) c.getViewRoot().findComponent("form:datosHistoricosUsuarios:persona");
            fechainicial.setFilterStyle("display: none; visibility: hidden;");
            fechafinal = (Column) c.getViewRoot().findComponent("form:datosHistoricosUsuarios:perfil");
            fechafinal.setFilterStyle("display: none; visibility: hidden;");
            alias = (Column) c.getViewRoot().findComponent("form:datosHistoricosUsuarios:alias");
            alias.setFilterStyle("display: none; visibility: hidden;");
            perfil = (Column) c.getViewRoot().findComponent("form:datosHistoricosUsuarios:pantalla");
            perfil.setFilterStyle("display: none; visibility: hidden;");
            nombrepersona = (Column) c.getViewRoot().findComponent("form:datosHistoricosUsuarios:pantalla");
            nombrepersona.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosHistoricosUsuarios:pantalla");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            altoTabla = "315";
            RequestContext.getCurrentInstance().update("form:datosHistoricosUsuarios");
            bandera = 0;
            listaHistoricosUsuariosFiltrar = null;
            tipoLista = 0;
         }
         listaHistoricosUsuarios.add(duplicarHistoricoUsuario);
         listaHistoricosUsuariosCrear.add(duplicarHistoricoUsuario);
         historicoUsuarioSeleccionado = duplicarHistoricoUsuario;
         contarRegistros();
         RequestContext.getCurrentInstance().update("form:datosHistoricosUsuarios");
         duplicarHistoricoUsuario = new HistoricosUsuarios();
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarUsuario");
         RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroUsuario').hide()");
      } else {
         RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevaUsuario");
         RequestContext.getCurrentInstance().execute("PF('validacionNuevaUsuario').show()");
      }
   }

   public void verificarRastro() {
      if (historicoUsuarioSeleccionado != null) {
         int resultado = administrarRastros.obtenerTabla(historicoUsuarioSeleccionado.getSecuencia(), "HISTORICOSUSUARIOS");
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
      } else if (administrarRastros.verificarHistoricosTabla("HISTORICOSUSUARIOS")) {
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
   }

   public void cancelarModificacion() {
      if (bandera == 1) {
         FacesContext c = FacesContext.getCurrentInstance();
         fechainicial = (Column) c.getViewRoot().findComponent("form:datosHistoricosUsuarios:persona");
         fechainicial.setFilterStyle("display: none; visibility: hidden;");
         fechafinal = (Column) c.getViewRoot().findComponent("form:datosHistoricosUsuarios:perfil");
         fechafinal.setFilterStyle("display: none; visibility: hidden;");
         alias = (Column) c.getViewRoot().findComponent("form:datosHistoricosUsuarios:alias");
         alias.setFilterStyle("display: none; visibility: hidden;");
         perfil = (Column) c.getViewRoot().findComponent("form:datosHistoricosUsuarios:pantalla");
         perfil.setFilterStyle("display: none; visibility: hidden;");
         nombrepersona = (Column) c.getViewRoot().findComponent("form:datosHistoricosUsuarios:pantalla");
         nombrepersona.setFilterStyle("display: none; visibility: hidden;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosHistoricosUsuarios:pantalla");
         descripcion.setFilterStyle("display: none; visibility: hidden;");
         altoTabla = "315";
         RequestContext.getCurrentInstance().update("form:datosHistoricosUsuarios");
         bandera = 0;
         listaHistoricosUsuariosFiltrar = null;
         tipoLista = 0;
      }
      listaHistoricosUsuariosBorrar.clear();
      listaHistoricosUsuariosCrear.clear();
      listaHistoricosUsuariosModificar.clear();
      historicoUsuarioSeleccionado = null;
      k = 0;
      listaHistoricosUsuarios = null;
      getListaHistoricosUsuarios();
      lovPersonas = null;
      contarRegistros();
      guardado = true;
      RequestContext.getCurrentInstance().update("form:datosHistoricosUsuarios");
   }

   public void salir() {
      limpiarListasValor();
      if (bandera == 1) {
         FacesContext c = FacesContext.getCurrentInstance();
         fechainicial = (Column) c.getViewRoot().findComponent("form:datosHistoricosUsuarios:persona");
         fechainicial.setFilterStyle("display: none; visibility: hidden;");
         fechafinal = (Column) c.getViewRoot().findComponent("form:datosHistoricosUsuarios:perfil");
         fechafinal.setFilterStyle("display: none; visibility: hidden;");
         alias = (Column) c.getViewRoot().findComponent("form:datosHistoricosUsuarios:alias");
         alias.setFilterStyle("display: none; visibility: hidden;");
         perfil = (Column) c.getViewRoot().findComponent("form:datosHistoricosUsuarios:pantalla");
         perfil.setFilterStyle("display: none; visibility: hidden;");
         nombrepersona = (Column) c.getViewRoot().findComponent("form:datosHistoricosUsuarios:pantalla");
         nombrepersona.setFilterStyle("display: none; visibility: hidden;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosHistoricosUsuarios:pantalla");
         descripcion.setFilterStyle("display: none; visibility: hidden;");
         altoTabla = "315";
         RequestContext.getCurrentInstance().update("form:datosHistoricosUsuarios");
         bandera = 0;
         listaHistoricosUsuariosFiltrar = null;
         tipoLista = 0;
      }
      listaHistoricosUsuariosBorrar.clear();
      listaHistoricosUsuariosCrear.clear();
      listaHistoricosUsuariosModificar.clear();
      historicoUsuarioSeleccionado = null;
      k = 0;
      listaHistoricosUsuarios = null;
      guardado = true;
      RequestContext.getCurrentInstance().update("form:datosHistoricosUsuarios");
      navegar("atras");
   }

   public void seleccionarDescripcion(String estadoDescripcion, HistoricosUsuarios husuario) {
      historicoUsuarioSeleccionado = husuario;
      if (estadoDescripcion != null) {
         if (estadoDescripcion.equals("REGISTRO INSERTADO")) {
            historicoUsuarioSeleccionado.setDescripcion("REGISTRO INSERTADO");
         } else if (estadoDescripcion.equals("REGISTRO ACTUALIZADO")) {
            historicoUsuarioSeleccionado.setDescripcion("REGISTRO ACTUALIZADO");
         } else if (estadoDescripcion.equals("REGISTRO MANUAL")) {
            historicoUsuarioSeleccionado.setDescripcion("REGISTRO MANUAL");
         } else if (estadoDescripcion.equals("REGISTRO MODIFICADO")) {
            historicoUsuarioSeleccionado.setDescripcion("REGISTRO MODIFICADO");
         }
      } else {
         historicoUsuarioSeleccionado.setDescripcion("REGISTRO MANUAL");
      }
      if (!listaHistoricosUsuariosCrear.contains(historicoUsuarioSeleccionado)) {
         if (listaHistoricosUsuariosModificar.isEmpty()) {
            listaHistoricosUsuariosModificar.add(historicoUsuarioSeleccionado);
         } else if (!listaHistoricosUsuariosModificar.contains(historicoUsuarioSeleccionado)) {
            listaHistoricosUsuariosModificar.add(historicoUsuarioSeleccionado);
         }
      }
      guardado = false;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      RequestContext.getCurrentInstance().update("form:datosHistoricosUsuarios");
   }

   public void seleccionarNuevaDescripcion(String estadoDescripcion, int tipoNuevo) {
      if (tipoNuevo == 1) {
         if (estadoDescripcion != null) {
            if (estadoDescripcion.equals("REGISTRO INSERTADO")) {
               nuevoHistoricoUsuario.setDescripcion("REGISTRO INSERTADO");
            } else if (estadoDescripcion.equals("REGISTRO ACTUALIZADO")) {
               nuevoHistoricoUsuario.setDescripcion("REGISTRO ACTUALIZADO");
            } else if (estadoDescripcion.equals("REGISTRO MANUAL")) {
               nuevoHistoricoUsuario.setDescripcion("REGISTRO MANUAL");
            } else if (estadoDescripcion.equals("REGISTRO MODIFICADO")) {
               nuevoHistoricoUsuario.setDescripcion("REGISTRO MODIFICADO");
            }
         }
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaDescripcion");

      } else if (tipoNuevo == 2) {
         if (estadoDescripcion != null) {
            if (estadoDescripcion.equals("REGISTRO INSERTADO")) {
               duplicarHistoricoUsuario.setDescripcion("REGISTRO INSERTADO");
            } else if (estadoDescripcion.equals("REGISTRO ACTUALIZADO")) {
               duplicarHistoricoUsuario.setDescripcion("REGISTRO ACTUALIZADO");
            } else if (estadoDescripcion.equals("REGISTRO MANUAL")) {
               duplicarHistoricoUsuario.setDescripcion("REGISTRO MANUAL");
            } else if (estadoDescripcion.equals("REGISTRO MODIFICADO")) {
               duplicarHistoricoUsuario.setDescripcion("REGISTRO MODIFICADO");
            }
         }
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarDescripcion");
      }
   }

   public void contarRegistros() {
      RequestContext.getCurrentInstance().update("form:infoRegistro");
   }

   public void contarRegistrosPersonas() {
      RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroPersonas");
   }

   public void contarRegistrosAlias() {
      RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroAlias");
   }

   public void contarRegistrosPerfiles() {
      RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroPerfiles");
   }

   public void habilitarBotonLov() {
      activarLov = false;
      RequestContext.getCurrentInstance().update("form:listaValores");
   }

   public void deshabilitarBotonLov() {
      activarLov = true;
      RequestContext.getCurrentInstance().update("form:listaValores");
   }

   public AsynchronousFilllListener listener() {
      log.info(this.getClass().getName() + ".listener()");
      return new AsynchronousFilllListener() {
         //RequestContext context = c;

         @Override
         public void reportFinished(JasperPrint jp) {
            log.info(this.getClass().getName() + ".listener().reportFinished()");
            try {
               estadoReporte = true;
               resultadoReporte = "Exito";
               //  RequestContext.getCurrentInstance().execute("PF('formularioDialogos:generandoReporte");
//                    generarArchivoReporte(jp);
            } catch (Exception e) {
               log.info("ControlNReporteNomina reportFinished ERROR: " + e.toString());
            }
         }

         @Override
         public void reportCancelled() {
            log.info(this.getClass().getName() + ".listener().reportCancelled()");
            estadoReporte = true;
            resultadoReporte = "Cancelación";
         }

         @Override
         public void reportFillError(Throwable e) {
            log.info(this.getClass().getName() + ".listener().reportFillError()");
            if (e.getCause() != null) {
               pathReporteGenerado = "ControlUsuarios reportFillError Error: " + e.toString() + "\n" + e.getCause().toString();
            } else {
               pathReporteGenerado = "ControlUsuarios reportFillError Error: " + e.toString();
            }
            estadoReporte = true;
            resultadoReporte = "Se estallo";
         }
      };
   }

   public void validarDescargaReporte() {
      try {
         log.info(this.getClass().getName() + ".validarDescargaReporte()");
         RequestContext.getCurrentInstance().execute("PF('generandoReporte').show()");
         RequestContext context = RequestContext.getCurrentInstance();
         BigDecimal aux = new BigDecimal(UsuarioAux.getSecuencia());
         nombreReporte = "HistoricoUsuario";
         tipoReporte = "PDF";
         log.info("nombre reporte : " + nombreReporte);
         log.info("tipo reporte: " + tipoReporte);
         Map param = new HashMap();
         param.put("secuenciaUsuario", aux);
         pathReporteGenerado = administarReportes.generarReporteHistoricosUsuarios(nombreReporte, tipoReporte, param);
         RequestContext.getCurrentInstance().execute("PF('generandoReporte').hide()");
         if (pathReporteGenerado != null && !pathReporteGenerado.startsWith("Error:")) {
            log.info("validar descarga reporte - ingreso al if 1");
            if (tipoReporte.equals("PDF")) {

               log.info("validar descarga reporte - ingreso al if 2 else");
               FileInputStream fis;
               try {
                  log.info("pathReporteGenerado : " + pathReporteGenerado);
                  fis = new FileInputStream(new File(pathReporteGenerado));
                  log.info("fis : " + fis);
                  reporte = new DefaultStreamedContent(fis, "application/pdf");
                  log.info("reporte despues de esto : " + reporte);
                  if (reporte != null) {
                     log.info("userAgent: " + userAgent);
                     log.info("validar descarga reporte - ingreso al if 4");
                     if (userAgent.toUpperCase().contains("Mobile".toUpperCase()) || userAgent.toUpperCase().contains("Tablet".toUpperCase()) || userAgent.toUpperCase().contains("Android".toUpperCase())) {
                        context.update("formularioDialogos:descargarReporte");
                        context.execute("PF('descargarReporte').show();");
                     } else {
                        cabezeraVisor = "Reporte - " + nombreReporte;
                        RequestContext.getCurrentInstance().update("formularioDialogos:verReportePDF");
                        RequestContext.getCurrentInstance().execute("PF('verReportePDF').show()");
                     }
                  }
               } catch (FileNotFoundException ex) {
                  log.warn("Error en validar descargar reporte : " + ex.getMessage());
                  RequestContext.getCurrentInstance().update("formularioDialogos:errorGenerandoReporte");
                  RequestContext.getCurrentInstance().execute("PF('errorGenerandoReporte').show()");
                  reporte = null;
               }
            }
         } else {
            log.info("validar descarga reporte - ingreso al if 1 else");
            RequestContext.getCurrentInstance().update("formularioDialogos:errorGenerandoReporte");
            RequestContext.getCurrentInstance().execute("PF('errorGenerandoReporte').show()");
         }
      } catch (Exception e) {
         log.warn("Error en validar descargar Reporte " + e.toString());
      }
   }

   public void reiniciarStreamedContent() {
      log.info(this.getClass().getName() + ".reiniciarStreamedContent()");
      reporte = null;
   }

   public void cancelarReporte() {
      log.info(this.getClass().getName() + ".cancelarReporte()");
      administarReportes.cancelarReporte();
   }

   public void exportarReporte() throws IOException {
      try {
         log.info("ControlInterfaseContableTotal.exportarReporte()   path generado : " + pathReporteGenerado);
         if (pathReporteGenerado != null || !pathReporteGenerado.startsWith("Error:")) {
            File reporteF = new File(pathReporteGenerado);
            FacesContext ctx = FacesContext.getCurrentInstance();
            FileInputStream fis = new FileInputStream(reporteF);
            byte[] bytes = new byte[1024];
            int read;
            if (!ctx.getResponseComplete()) {
               String fileName = reporteF.getName();
               HttpServletResponse response = (HttpServletResponse) ctx.getExternalContext().getResponse();
               response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
               ServletOutputStream out = response.getOutputStream();

               while ((read = fis.read(bytes)) != -1) {
                  out.write(bytes, 0, read);
               }
               out.flush();
               out.close();
               ctx.responseComplete();
            }
         } else {
            RequestContext.getCurrentInstance().update("formularioDialogos:errorGenerandoReporte");
            RequestContext.getCurrentInstance().execute("PF('errorGenerandoReporte').show()");
         }
      } catch (Exception e) {
         log.warn("Error en exportarReporte :" + e.getMessage());
         RequestContext.getCurrentInstance().update("formularioDialogos:errorGenerandoReporte");
         RequestContext.getCurrentInstance().execute("PF('errorGenerandoReporte').show()");
      }
   }

   public void posicionOtro() {
      FacesContext context = FacesContext.getCurrentInstance();
      Map<String, String> map = context.getExternalContext().getRequestParameterMap();
      String name = map.get("n"); // name attribute of node
      String type = map.get("t"); // type attribute of node
      int indice = Integer.parseInt(type);
      int columna = Integer.parseInt(name);
      historicoUsuarioSeleccionado = listaHistoricosUsuarios.get(indice);
      cambiarIndice(historicoUsuarioSeleccionado, columna);
   }

/////GETS Y SETS/////////
   public List<HistoricosUsuarios> getListaHistoricosUsuarios() {
      if (listaHistoricosUsuarios == null) {
         listaHistoricosUsuarios = administrarHistoricosUsuarios.consultarHistoricosUsuarios(UsuarioAux.getSecuencia());
      }
      return listaHistoricosUsuarios;
   }

   public void setListaHistoricosUsuarios(List<HistoricosUsuarios> listaHistoricosUsuarios) {
      this.listaHistoricosUsuarios = listaHistoricosUsuarios;
   }

   public List<HistoricosUsuarios> getListaHistoricosUsuariosFiltrar() {
      return listaHistoricosUsuariosFiltrar;
   }

   public void setListaHistoricosUsuariosFiltrar(List<HistoricosUsuarios> listaHistoricosUsuariosFiltrar) {
      this.listaHistoricosUsuariosFiltrar = listaHistoricosUsuariosFiltrar;
   }

   public HistoricosUsuarios getHistoricoUsuarioSeleccionado() {
      return historicoUsuarioSeleccionado;
   }

   public void setHistoricoUsuarioSeleccionado(HistoricosUsuarios historicoUsuarioSeleccionado) {
      this.historicoUsuarioSeleccionado = historicoUsuarioSeleccionado;
   }

   public HistoricosUsuarios getNuevoHistoricoUsuario() {
      return nuevoHistoricoUsuario;
   }

   public void setNuevoHistoricoUsuario(HistoricosUsuarios nuevoHistoricoUsuario) {
      this.nuevoHistoricoUsuario = nuevoHistoricoUsuario;
   }

   public HistoricosUsuarios getDuplicarHistoricoUsuario() {
      return duplicarHistoricoUsuario;
   }

   public void setDuplicarHistoricoUsuario(HistoricosUsuarios duplicarHistoricoUsuario) {
      this.duplicarHistoricoUsuario = duplicarHistoricoUsuario;
   }

   public HistoricosUsuarios getEditarHistoricoUsuario() {
      return editarHistoricoUsuario;
   }

   public void setEditarHistoricoUsuario(HistoricosUsuarios editarHistoricoUsuario) {
      this.editarHistoricoUsuario = editarHistoricoUsuario;
   }

   public List<Personas> getLovPersonas() {
      if (lovPersonas == null) {
         lovPersonas = administrarHistoricosUsuarios.lovPersonas();
      }
      return lovPersonas;
   }

   public void setLovPersonas(List<Personas> lovPersonas) {
      this.lovPersonas = lovPersonas;
   }

   public List<Personas> getFiltradoLovPersonas() {
      return filtradoLovPersonas;
   }

   public void setFiltradoLovPersonas(List<Personas> filtradoLovPersonas) {
      this.filtradoLovPersonas = filtradoLovPersonas;
   }

   public Personas getPersonasLovSeleccionado() {
      return personasLovSeleccionado;
   }

   public void setPersonasLovSeleccionado(Personas personasLovSeleccionado) {
      this.personasLovSeleccionado = personasLovSeleccionado;
   }

   public String getAltoTabla() {
      return altoTabla;
   }

   public void setAltoTabla(String altoTabla) {
      this.altoTabla = altoTabla;
   }

   public String getInfoRegistroPersonas() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:LOVPersonas");
      infoRegistroPersonas = String.valueOf(tabla.getRowCount());
      return infoRegistroPersonas;
   }

   public void setInfoRegistroPersonas(String infoRegistroPersonas) {
      this.infoRegistroPersonas = infoRegistroPersonas;
   }

   public String getInfoRegistroHistoricos() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosHistoricosUsuarios");
      infoRegistroHistoricos = String.valueOf(tabla.getRowCount());
      return infoRegistroHistoricos;
   }

   public void setInfoRegistroHistoricos(String infoRegistroHistoricos) {
      this.infoRegistroHistoricos = infoRegistroHistoricos;
   }

   public String getTablaImprimir() {
      return tablaImprimir;
   }

   public void setTablaImprimir(String tablaImprimir) {
      this.tablaImprimir = tablaImprimir;
   }

   public String getNombreArchivo() {
      return nombreArchivo;
   }

   public void setNombreArchivo(String nombreArchivo) {
      this.nombreArchivo = nombreArchivo;
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

   public boolean isActivarLov() {
      return activarLov;
   }

   public void setActivarLov(boolean activarLov) {
      this.activarLov = activarLov;
   }

   public StreamedContent getReporte() {
      return reporte;
   }

   public void setReporte(StreamedContent reporte) {
      this.reporte = reporte;
   }

   public String getPathReporteGenerado() {
      return pathReporteGenerado;
   }

   public void setPathReporteGenerado(String pathReporteGenerado) {
      this.pathReporteGenerado = pathReporteGenerado;
   }

   public String getNombreReporte() {
      return nombreReporte;
   }

   public void setNombreReporte(String nombreReporte) {
      this.nombreReporte = nombreReporte;
   }

   public String getTipoReporte() {
      return tipoReporte;
   }

   public void setTipoReporte(String tipoReporte) {
      this.tipoReporte = tipoReporte;
   }

   public String getCabezeraVisor() {
      return cabezeraVisor;
   }

   public void setCabezeraVisor(String cabezeraVisor) {
      this.cabezeraVisor = cabezeraVisor;
   }

   public boolean isEstadoReporte() {
      return estadoReporte;
   }

   public void setEstadoReporte(boolean estadoReporte) {
      this.estadoReporte = estadoReporte;
   }

   public String getResultadoReporte() {
      return resultadoReporte;
   }

   public void setResultadoReporte(String resultadoReporte) {
      this.resultadoReporte = resultadoReporte;
   }

   public String getMensajeValidacion() {
      return mensajeValidacion;
   }

   public void setMensajeValidacion(String mensajeValidacion) {
      this.mensajeValidacion = mensajeValidacion;
   }

   public List<Usuarios> getLovUsuarios() {
      if (lovUsuarios == null) {
         lovUsuarios = administrarHistoricosUsuarios.lovUsuarios(UsuarioAux.getSecuencia());
      }
      return lovUsuarios;
   }

   public void setLovUsuarios(List<Usuarios> lovUsuarios) {
      this.lovUsuarios = lovUsuarios;
   }

   public List<Usuarios> getFiltrarUsuariosLov() {
      return filtrarUsuariosLov;
   }

   public void setFiltrarUsuariosLov(List<Usuarios> filtrarUsuariosLov) {
      this.filtrarUsuariosLov = filtrarUsuariosLov;
   }

   public Usuarios getUsuarioLovSeleccionado() {
      return usuarioLovSeleccionado;
   }

   public void setUsuarioLovSeleccionado(Usuarios usuarioLovSeleccionado) {
      this.usuarioLovSeleccionado = usuarioLovSeleccionado;
   }

   public List<Perfiles> getLovPerfiles() {
      if (lovPerfiles == null) {
         lovPerfiles = administrarHistoricosUsuarios.lovPerfiles();
      }
      return lovPerfiles;
   }

   public void setLovPerfiles(List<Perfiles> lovPerfiles) {
      this.lovPerfiles = lovPerfiles;
   }

   public Perfiles getPerfilLovSeleccionado() {
      return perfilLovSeleccionado;
   }

   public void setPerfilLovSeleccionado(Perfiles perfilLovSeleccionado) {
      this.perfilLovSeleccionado = perfilLovSeleccionado;
   }

   public String getInfoRegistroAlias() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:LOVUsuariosAlias");
      infoRegistroAlias = String.valueOf(tabla.getRowCount());
      return infoRegistroAlias;
   }

   public void setInfoRegistroAlias(String infoRegistroAlias) {
      this.infoRegistroAlias = infoRegistroAlias;
   }

   public String getInfoRegistroPerfiles() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:LOVPerfiles");
      infoRegistroPerfiles = String.valueOf(tabla.getRowCount());
      return infoRegistroPerfiles;
   }

   public void setInfoRegistroPerfiles(String infoRegistroPerfiles) {
      this.infoRegistroPerfiles = infoRegistroPerfiles;
   }

   public List<Perfiles> getFiltrarPerfilesLov() {
      return filtrarPerfilesLov;
   }

   public void setFiltrarPerfilesLov(List<Perfiles> filtrarPerfilesLov) {
      this.filtrarPerfilesLov = filtrarPerfilesLov;
   }

}
