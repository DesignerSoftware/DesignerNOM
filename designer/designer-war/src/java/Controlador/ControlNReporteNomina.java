/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Asociaciones;
import Entidades.Empleados;
import Entidades.Empresas;
import Entidades.Estructuras;
import Entidades.GruposConceptos;
import Entidades.Inforeportes;
import Entidades.ParametrosReportes;
import Entidades.Procesos;
import Entidades.Terceros;
import Entidades.TiposAsociaciones;
import Entidades.TiposTrabajadores;
import Entidades.UbicacionesGeograficas;
import InterfaceAdministrar.AdministarReportesInterface;
import InterfaceAdministrar.AdministrarNReportesNominaInterface;
import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import ControlNavegacion.ControlListaNavegacion;
import ControlNavegacion.ListasRecurrentes;
import java.math.BigInteger;
import java.util.Map;
import java.util.LinkedHashMap;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
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
import org.primefaces.component.inputtext.InputText;
import org.primefaces.component.selectcheckboxmenu.SelectCheckboxMenu;
import org.primefaces.component.selectonemenu.SelectOneMenu;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author AndresPineda
 */
@ManagedBean
@SessionScoped
public class ControlNReporteNomina implements Serializable {

   private static Logger log = Logger.getLogger(ControlNReporteNomina.class);

   @EJB
   AdministrarNReportesNominaInterface administrarNReportesNomina;
   @EJB
   AdministarReportesInterface administarReportes;
//PARAMETROS REPORTES
   private ParametrosReportes parametroDeReporte;
   private ParametrosReportes parametroModificacion;
   private ParametrosReportes parametroFecha;
   //INFOREPORTES
   private List<Inforeportes> listaIR;
   private Inforeportes reporteSeleccionado;
   private List<Inforeportes> filtrarListIRU;
   private List<Inforeportes> listaInfoReportesModificados;
   //INFOREPORTES LOV
   private List<Inforeportes> lovInforeportes;
   private Inforeportes reporteSeleccionadoLOV;
   private List<Inforeportes> filtrarLovInforeportes;
   private List<Inforeportes> filtrarReportes;
   //EMPLEADOS
   private List<Empleados> lovEmpleados;
   private Empleados empleadoSeleccionado;
   private List<Empleados> lovEmpleadosFiltrar;
   //GRUPO
   private List<GruposConceptos> lovGruposConceptos;
   private GruposConceptos grupoCSeleccionado;
   private List<GruposConceptos> lovGruposConceptosFiltrar;
   //UBICACION GEOGRAFICA
   private List<UbicacionesGeograficas> lovUbicacionesGeograficas;
   private UbicacionesGeograficas ubicacionesGSeleccionado;
   private List<UbicacionesGeograficas> lovUbicacionesGeograficasFiltrar;
   //TIPOS ASOCIACIONES
   private List<TiposAsociaciones> lovTiposAsociaciones;
   private TiposAsociaciones tiposASeleccionado;
   private List<TiposAsociaciones> lovTiposAsociacionesFiltrar;
   //EMPRESAS
   private List<Empresas> lovEmpresas;
   private Empresas empresaSeleccionada;
   private List<Empresas> lovEmpresasFiltrar;
   //ASOCIACIONES
   private List<Asociaciones> lovAsociaciones;
   private Asociaciones asociacionSeleccionado;
   private List<Asociaciones> lovAsociacionesFiltrar;
   //TERCEROS
   private List<Terceros> lovTerceros;
   private Terceros terceroSeleccionado;
   private List<Terceros> lovTercerosFiltrar;
   //PROCESOS
   private List<Procesos> lovProcesos;
   private Procesos procesoSeleccionado;
   private List<Procesos> lovProcesosFiltrar;
   //TIPOS TRABAJADORES
   private List<TiposTrabajadores> lovTiposTrabajadores;
   private TiposTrabajadores tipoTSeleccionado;
   private List<TiposTrabajadores> lovTiposTrabajadoresFiltrar;
   //ESTRUCTURAS
   private List<Estructuras> lovEstructuras;
   private Estructuras estructuraSeleccionada;
   private List<Estructuras> lovEstructurasFiltrar;
   //COLUMNS
   private Column codigoIR, reporteIR;
   private SelectCheckboxMenu tipoIR;
   //GENERAR    
   private String reporteGenerar;
   //OTROS
   private int bandera;
   private boolean aceptar;
   private int casilla;
   private int posicionReporte;
   private String requisitosReporte;
   private String grupo, ubiGeo, tipoAso, estructura, empresa, tipoTrabajador, tercero, proceso, asociacion, estado;
   private boolean permitirIndex, cambiosReporte;
   private String color, decoracion;
   private String color2, decoracion2;
   private int casillaInforReporte;
   //INPUT    
   private InputText empleadoDesdeParametro, empleadoHastaParametro, estructuraParametro, tipoTrabajadorParametro, terceroParametro, grupoParametro;
   //private InputText empresaParametro,  procesoParametro, notasParametro, asociacionParametro, ubicacionGeograficaParametro, tipoAsociacionParametro;
   private SelectOneMenu estadoParametro;
   //ALTO SCROLL TABLA
   private String altoTabla;
   //EXPORTAR REPORTE
   private String pathReporteGenerado;
   private String nombreReporte, tipoReporte;
   //DATE
   private Date fechaDesde, fechaHasta;
   private BigDecimal emplDesde, emplHasta;
   //MOSTRAR TODOS
   private boolean activoMostrarTodos, activoBuscarReporte, activarEnvio;
   //VISUALIZAR REPORTE PDF
   private StreamedContent reporte;
   private String cabezeraVisor;
   //Listener reporte
   private AsynchronousFilllListener asistenteReporte;
   //BANDERAS
   private boolean estadoReporte;
   private String resultadoReporte;
   //FileInputStream prueba;
   //
   private String infoRegistroEmpleadoDesde, infoRegistroEmpleadoHasta, infoRegistroGrupoConcepto, infoRegistroUbicacion, infoRegistroTipoAsociacion, infoRegistroEmpresa, infoRegistroAsociacion, infoRegistroTercero, infoRegistroProceso, infoRegistroTipoTrabajador, infoRegistroEstructura;
   private String infoRegistroLovReportes, infoRegistro;
   //para Recordar
   private DataTable tabla;
   private int tipoLista;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<>();
   private ExternalContext externalContext;
   private String userAgent;
   private boolean activarLov;
   private Map<BigInteger, Object> mapTipos = new LinkedHashMap<>();
   private ListasRecurrentes listasRecurrentes;

   public ControlNReporteNomina() {
      FacesContext fc = FacesContext.getCurrentInstance();
      ControlListaNavegacion controlListaNavegacion = (ControlListaNavegacion) fc.getApplication().evaluateExpressionGet(fc, "#{controlListaNavegacion}", ControlListaNavegacion.class);
      listasRecurrentes = controlListaNavegacion.getListasRecurrentes();
      activoMostrarTodos = true;
      activoBuscarReporte = false;
      activarEnvio = true;
      color = "black";
      decoracion = "none";
      color2 = "black";
      decoracion2 = "none";
      casillaInforReporte = -1;
      reporteSeleccionadoLOV = null;
      reporteSeleccionado = null;
      cambiosReporte = true;
      listaInfoReportesModificados = new ArrayList<>();
      parametroDeReporte = null;
      listaIR = null;
      bandera = 0;
      aceptar = true;
      casilla = -1;
      parametroModificacion = new ParametrosReportes();
      parametroFecha = new ParametrosReportes();
      reporteGenerar = "";
      requisitosReporte = "";
      posicionReporte = -1;
      lovInforeportes = null;
      lovAsociaciones = null;
//        lovEmpleados = null;
      lovEmpleados = new ArrayList<>();
      lovEmpresas = null;
      lovEstructuras = null;
      lovGruposConceptos = null;
      lovProcesos = null;
      lovTerceros = null;
      lovTiposAsociaciones = null;
      lovTiposTrabajadores = null;
      lovUbicacionesGeograficas = null;
      tipoLista = 0;
      empleadoSeleccionado = new Empleados();
      empresaSeleccionada = new Empresas();
      grupoCSeleccionado = new GruposConceptos();
      ubicacionesGSeleccionado = new UbicacionesGeograficas();
      tiposASeleccionado = new TiposAsociaciones();
      estructuraSeleccionada = new Estructuras();
      tipoTSeleccionado = new TiposTrabajadores();
      terceroSeleccionado = new Terceros();
      procesoSeleccionado = new Procesos();
      asociacionSeleccionado = new Asociaciones();
      permitirIndex = true;
      altoTabla = "160";
      //prueba = new FileInputStream(new File("C:\\Users\\Administrador\\Documents\\Guia JasperReport.pdf"));
      //reporte = new DefaultStreamedContent(prueba, "application/pdf");
      //reporte = new DefaultStreamedContent();
      cabezeraVisor = null;
      estadoReporte = false;
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
      String pagActual = "nreportenomina";

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
      lovAsociaciones = null;
      lovEmpleados = null;
      lovEmpresas = null;
      lovEstructuras = null;
      lovGruposConceptos = null;
      lovInforeportes = null;
      lovProcesos = null;
      lovTerceros = null;
      lovTiposAsociaciones = null;
      lovTiposTrabajadores = null;
      lovUbicacionesGeograficas = null;
   }

   @PreDestroy
   public void destruyendoce() {
      log.info(this.getClass().getName() + ".destruyendoce() @Destroy");
   }

   @PostConstruct
   public void iniciarAdministradores() {
      try {
         FacesContext contexto = FacesContext.getCurrentInstance();
         HttpSession ses = (HttpSession) contexto.getExternalContext().getSession(false);
         externalContext = contexto.getExternalContext();
         userAgent = externalContext.getRequestHeaderMap().get("User-Agent");
         administarReportes.obtenerConexion(ses.getId());
         administrarNReportesNomina.obtenerConexion(ses.getId());
      } catch (Exception e) {
         log.error("Error postconstruct controlNReporteNomina" + e);
         log.info("Causa: " + e.getMessage());
      }
   }

   public void iniciarPagina() {
      activoMostrarTodos = true;
      activoBuscarReporte = false;
      activarEnvio = true;
      getListaIR();
   }

//TOOLTIP
   public void guardarCambios() {
      RequestContext context = RequestContext.getCurrentInstance();
      try {
         if (!cambiosReporte) {
            if (parametroDeReporte.getUsuario() != null) {

               if (parametroDeReporte.getCodigoempleadodesde() == null) {
                  parametroDeReporte.setCodigoempleadodesde(null);
               }
               if (parametroDeReporte.getCodigoempleadohasta() == null) {
                  parametroDeReporte.setCodigoempleadohasta(null);
               }
               if (parametroDeReporte.getGrupo().getSecuencia() == null) {
                  parametroDeReporte.setGrupo(null);
               }
               if (parametroDeReporte.getUbicaciongeografica().getSecuencia() == null) {
                  parametroDeReporte.setUbicaciongeografica(null);
               }
               if (parametroDeReporte.getEmpresa().getSecuencia() == null) {
                  parametroDeReporte.setEmpresa(null);
               }
               if (parametroDeReporte.getLocalizacion().getSecuencia() == null) {
                  parametroDeReporte.setLocalizacion(null);
               }
               if (parametroDeReporte.getTipotrabajador().getSecuencia() == null) {
                  parametroDeReporte.setTipotrabajador(null);
               }
               if (parametroDeReporte.getTercero().getSecuencia() == null) {
                  parametroDeReporte.setTercero(null);
               }
               if (parametroDeReporte.getProceso().getSecuencia() == null) {
                  parametroDeReporte.setProceso(null);
               }
               if (parametroDeReporte.getAsociacion().getSecuencia() == null) {
                  parametroDeReporte.setAsociacion(null);
               }
               if (parametroDeReporte.getTipoasociacion().getSecuencia() == null) {
                  parametroDeReporte.setTipoasociacion(null);
               }
               administrarNReportesNomina.modificarParametrosReportes(parametroDeReporte);
            }
            log.info("listaInfoReportesModificados: " + listaInfoReportesModificados);
            if (!listaInfoReportesModificados.isEmpty()) {
               if (!mapTipos.isEmpty()) {
                  listaInfoReportesModificados.get(0).setTipo((String) mapTipos.get(listaInfoReportesModificados.get(0).getSecuencia()));
               }
               administrarNReportesNomina.guardarCambiosInfoReportes(listaInfoReportesModificados);
               listaInfoReportesModificados.clear();
            }
            cambiosReporte = true;
            FacesMessage msg = new FacesMessage("Información", "Los datos se guardaron con Éxito.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         } else if (parametroDeReporte.getFechadesde().after(parametroDeReporte.getFechahasta())) {
            fechaDesde = parametroDeReporte.getFechadesde();
            fechaHasta = parametroDeReporte.getFechahasta();
            log.info("parametroDeReporte.getFechadesde: " + parametroDeReporte.getFechadesde());
            log.info("parametroDeReporte.getFechahasta: " + parametroDeReporte.getFechahasta());
            parametroDeReporte.setFechadesde(fechaDesde);
            parametroDeReporte.setFechahasta(fechaHasta);
            RequestContext.getCurrentInstance().update("formParametros");
            RequestContext.getCurrentInstance().execute("PF('errorFechas').show()");
         }
      } catch (Exception e) {
         log.warn("Error en guardar Cambios Controlador : " + e.toString());
         FacesMessage msg = new FacesMessage("Información", "ha ocurrido un error en el guardado, intente nuevamente");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      }
   }

   public void editarCelda() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (casilla >= 1) {
         if (casilla == 1) {
            RequestContext.getCurrentInstance().update("formDialogos:editarFechaDesde");
            RequestContext.getCurrentInstance().execute("PF('editarFechaDesde').show()");
         }
         if (casilla == 2) {
            RequestContext.getCurrentInstance().update("formDialogos:empleadoDesde");
            RequestContext.getCurrentInstance().execute("PF('empleadoDesde').show()");
         }
         if (casilla == 4) {
            RequestContext.getCurrentInstance().update("formDialogos:grupoDesde");
            RequestContext.getCurrentInstance().execute("PF('grupoDesde').show()");
         }
         if (casilla == 5) {
            RequestContext.getCurrentInstance().update("formDialogos:ubicacionGeografica");
            RequestContext.getCurrentInstance().execute("PF('ubicacionGeografica').show()");
         }
         if (casilla == 6) {
            RequestContext.getCurrentInstance().update("formDialogos:tipoAsociacion");
            RequestContext.getCurrentInstance().execute("PF('tipoAsociacion').show()");
         }
         if (casilla == 7) {
            RequestContext.getCurrentInstance().update("formDialogos:editarFechaHasta");
            RequestContext.getCurrentInstance().execute("PF('editarFechaHasta').show()");
         }
         if (casilla == 8) {
            RequestContext.getCurrentInstance().update("formDialogos:empleadoHasta");
            RequestContext.getCurrentInstance().execute("PF('empleadoHasta').show()");
         }
         if (casilla == 10) {
            RequestContext.getCurrentInstance().update("formDialogos:empresa");
            RequestContext.getCurrentInstance().execute("PF('empresa').show()");
         }
         if (casilla == 11) {
            RequestContext.getCurrentInstance().update("formDialogos:estructura");
            RequestContext.getCurrentInstance().execute("PF('estructura').show()");
         }
         if (casilla == 12) {
            RequestContext.getCurrentInstance().update("formDialogos:tipoTrabajador");
            RequestContext.getCurrentInstance().execute("PF('tipoTrabajador').show()");
         }
         if (casilla == 13) {
            RequestContext.getCurrentInstance().update("formDialogos:tercero");
            RequestContext.getCurrentInstance().execute("PF('tercero').show()");
         }
         if (casilla == 14) {
            RequestContext.getCurrentInstance().update("formDialogos:proceso");
            RequestContext.getCurrentInstance().execute("PF('proceso').show()");
         }
         if (casilla == 15) {
            RequestContext.getCurrentInstance().update("formDialogos:notas");
            RequestContext.getCurrentInstance().execute("PF('notas').show()");
         }
         if (casilla == 16) {
            RequestContext.getCurrentInstance().update("formDialogos:asociacion");
            RequestContext.getCurrentInstance().execute("PF('asociacion').show()");
         }
         casilla = -1;
      }
      if (casillaInforReporte >= 1) {
         if (casillaInforReporte == 1) {
            RequestContext.getCurrentInstance().update("formParametros:infoReporteCodigoD");
            RequestContext.getCurrentInstance().execute("PF('infoReporteCodigoD').show()");
         }
         if (casillaInforReporte == 2) {
            RequestContext.getCurrentInstance().update("formParametros:infoReporteNombreD");
            RequestContext.getCurrentInstance().execute("PF('infoReporteNombreD').show()");
         }
         casillaInforReporte = -1;
      }
   }

   public void listaValoresBoton() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (casilla == 2) {
         lovEmpleados = null;
         cargarListaEmpleados();
         RequestContext.getCurrentInstance().update("formularioEmpleadoDesde:EmpleadoDesdeDialogo");
         RequestContext.getCurrentInstance().execute("PF('EmpleadoDesdeDialogo').show()");
         contarRegistrosEmpeladoD();
      }
      if (casilla == 4) {
         lovGruposConceptos = null;
         cargarListaGruposConcepto();
         RequestContext.getCurrentInstance().update("formularioGrupoConcepto:GruposConceptosDialogo");
         RequestContext.getCurrentInstance().execute("PF('GruposConceptosDialogo').show()");
         contarRegistrosGrupo();
      }
      if (casilla == 5) {
         lovUbicacionesGeograficas = null;
         cargarListaUbicacionesGeograficas();
         RequestContext.getCurrentInstance().update("formularioUbicacion:UbiGeograficaDialogo");
         RequestContext.getCurrentInstance().execute("PF('UbiGeograficaDialogo').show()");
         contarRegistrosUbicacion();
      }
      if (casilla == 6) {
         lovTiposAsociaciones = null;
         getLovTiposAsociaciones();
         RequestContext.getCurrentInstance().update("formularioTipoAsociacion:TipoAsociacionDialogo");
         RequestContext.getCurrentInstance().execute("PF('TipoAsociacionDialogo').show()");
         contarRegistrosTipoAsociacion();
      }
      if (casilla == 8) {
         lovEmpleados = null;
         cargarListaEmpleados();
         RequestContext.getCurrentInstance().update("formularioEmpleadoHasta:EmpleadoHastaDialogo");
         RequestContext.getCurrentInstance().execute("PF('EmpleadoHastaDialogo').show()");
         contarRegistrosEmpeladoH();
      }
      if (casilla == 10) {
         lovEmpresas = null;
         getLovEmpresas();
         RequestContext.getCurrentInstance().update("formularioEmpresa:EmpresaDialogo");
         RequestContext.getCurrentInstance().execute("PF('EmpresaDialogo').show()");
         contarRegistrosEmpresa();
      }
      if (casilla == 11) {
         lovEstructuras = null;
         cargarListaEstructuras();
         RequestContext.getCurrentInstance().update("formularioEstructura:EstructuraDialogo");
         RequestContext.getCurrentInstance().execute("PF('EstructuraDialogo').show()");
         contarRegistrosEstructura();
      }
      if (casilla == 12) {
         lovTiposTrabajadores = null;
         cargarListaTiposTrabajadores();
         RequestContext.getCurrentInstance().update("formularioTipoTrabajador:TipoTrabajadorDialogo");
         RequestContext.getCurrentInstance().execute("PF('TipoTrabajadorDialogo').show()");
         contarRegistrosTipoTrabajador();
      }
      if (casilla == 13) {
         lovTerceros = null;
         cargarListaTerceros();
         RequestContext.getCurrentInstance().update("formularioTercero:TerceroDialogo");
         RequestContext.getCurrentInstance().execute("PF('TerceroDialogo').show()");
         contarRegistrosTercero();
      }
      if (casilla == 14) {
         lovProcesos = null;
         cargarListaProcesos();
         RequestContext.getCurrentInstance().update("formularioProceso:ProcesoDialogo");
         RequestContext.getCurrentInstance().execute("PF('ProcesoDialogo').show()");
         contarRegistrosProceso();
      }
      if (casilla == 16) {
         lovAsociaciones = null;
         cargarListaAsociaciones();
         RequestContext.getCurrentInstance().update("formularioAsociacion:AsociacionDialogo");
         RequestContext.getCurrentInstance().execute("PF('AsociacionDialogo').show()");
         contarRegistrosAsociacion();
      }
   }

   public void activarCtrlF11() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 0) {
         altoTabla = "140";
         codigoIR = (Column) c.getViewRoot().findComponent("form:reportesNomina:codigoIR");
         codigoIR.setFilterStyle("width: 85% !important");
         reporteIR = (Column) c.getViewRoot().findComponent("form:reportesNomina:reporteIR");
         reporteIR.setFilterStyle("width: 85% !important");
         tipoIR = (SelectCheckboxMenu) c.getViewRoot().findComponent("form:reportesNomina:tipo");
         tipoIR.setRendered(true);
         RequestContext.getCurrentInstance().update("form:reportesNomina:tipo");
         RequestContext.getCurrentInstance().update("form:reportesNomina");
         bandera = 1;
      } else if (bandera == 1) {
         cerrarFiltrado();
         defaultPropiedadesParametrosReporte();
      }
   }

   public void cerrarFiltrado() {
      FacesContext c = FacesContext.getCurrentInstance();
      altoTabla = "160";
      codigoIR = (Column) c.getViewRoot().findComponent("form:reportesNomina:codigoIR");
      codigoIR.setFilterStyle("display: none; visibility: hidden;");
      reporteIR = (Column) c.getViewRoot().findComponent("form:reportesNomina:reporteIR");
      reporteIR.setFilterStyle("display: none; visibility: hidden;");
      tipoIR = (SelectCheckboxMenu) c.getViewRoot().findComponent("form:reportesNomina:tipo");
      tipoIR.setRendered(false);
      tipoIR.setSelectedValues(null);
      tipoIR.resetValue();
      RequestContext.getCurrentInstance().update("form:reportesNomina:tipo");
      RequestContext.getCurrentInstance().update("form:reportesNomina");
      bandera = 0;
      tipoLista = 0;
      filtrarListIRU = null;
   }

   public void salir() {
      limpiarListasValor();
      if (bandera == 1) {
         cerrarFiltrado();
      }
      listaIR = null;
      parametroDeReporte = null;
      parametroModificacion = null;
      lovAsociaciones = null;
      lovEmpleados = null;
      lovEmpresas = null;
      lovEstructuras = null;
      lovGruposConceptos = null;
      lovProcesos = null;
      lovTerceros = null;
      lovTiposAsociaciones = null;
      lovTiposTrabajadores = null;
      lovUbicacionesGeograficas = null;
      casilla = -1;
      listaInfoReportesModificados.clear();
      cambiosReporte = true;
      reporteSeleccionado = null;
      reporteSeleccionadoLOV = null;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      navegar("atras");
   }

   public void cancelarModificaciones() {
      if (bandera == 1) {
         cerrarFiltrado();
      }
      defaultPropiedadesParametrosReporte();
      listaIR = null;
      parametroDeReporte = null;
      parametroModificacion = null;
      casilla = -1;
      listaInfoReportesModificados.clear();
      cambiosReporte = true;
      getParametroDeInforme();
      getListaIR();
      activoMostrarTodos = true;
      activoBuscarReporte = false;
      reporteSeleccionado = null;
      RequestContext.getCurrentInstance().update("form:MOSTRARTODOS");
      RequestContext.getCurrentInstance().update("form:BUSCARREPORTE");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      RequestContext.getCurrentInstance().update("form:ENVIOCORREO");
      RequestContext.getCurrentInstance().update("form:reportesNomina");
      RequestContext.getCurrentInstance().update("formParametros:fechaDesdeParametro");
      RequestContext.getCurrentInstance().update("formParametros:empleadoDesdeParametro");
      RequestContext.getCurrentInstance().update("formParametros:estadoParametro");
      RequestContext.getCurrentInstance().update("formParametros:grupoParametro");
      RequestContext.getCurrentInstance().update("formParametros:ubicacionGeograficaParametro");
      RequestContext.getCurrentInstance().update("formParametros:tipoAsociacionParametro");
      RequestContext.getCurrentInstance().update("formParametros:fechaHastaParametro");
      RequestContext.getCurrentInstance().update("formParametros:empleadoHastaParametro");
      RequestContext.getCurrentInstance().update("formParametros:tipoPersonalParametro");
      RequestContext.getCurrentInstance().update("formParametros:empresaParametro");
      RequestContext.getCurrentInstance().update("formParametros:estructuraParametro");
      RequestContext.getCurrentInstance().update("formParametros:tipoTrabajadorParametro");
      RequestContext.getCurrentInstance().update("formParametros:terceroParametro");
      RequestContext.getCurrentInstance().update("formParametros:notasParametro");
      RequestContext.getCurrentInstance().update("formParametros:asociacionParametro");
   }

   public void cancelarYSalir() {
      cancelarModificaciones();
      salir();
   }

   //EVENTO FILTRAR
   public void eventoFiltrar() {
      contarRegistros();
   }

   public void activarEnvioCorreo() {
      if (reporteSeleccionado != null) {
         activarEnvio = false;
      } else {
         activarEnvio = true;
      }
      RequestContext.getCurrentInstance().update("form:ENVIOCORREO");
   }

   public void seleccionRegistro() {
      try {
         activarEnvioCorreo();
         defaultPropiedadesParametrosReporte();
         if (reporteSeleccionado.getFecdesde().equals("SI")) {
            color = "red";
            RequestContext.getCurrentInstance().update("formParametros");
         }
         if (reporteSeleccionado.getFechasta().equals("SI")) {
            color2 = "red";
            RequestContext.getCurrentInstance().update("formParametros");
         }
         if (reporteSeleccionado.getEmdesde().equals("SI")) {
            empleadoDesdeParametro = (InputText) FacesContext.getCurrentInstance().getViewRoot().findComponent("formParametros:empleadoDesdeParametro");
            //empleadoDesdeParametro.setStyle("position: absolute; top: 41px; left: 150px; height: 10px; font-size: 11px; width: 90px; color: red;");
            if (!empleadoDesdeParametro.getStyle().contains(" color: red;")) {
               empleadoDesdeParametro.setStyle(empleadoDesdeParametro.getStyle() + " color: red;");
            }
         } else {
            empleadoDesdeParametro = (InputText) FacesContext.getCurrentInstance().getViewRoot().findComponent("formParametros:empleadoDesdeParametro");
            try {
               if (empleadoDesdeParametro.getStyle().contains(" color: red;")) {
                  empleadoDesdeParametro.setStyle(empleadoDesdeParametro.getStyle().replace(" color: red;", ""));
               }
            } catch (Exception e) {
               e.printStackTrace();
            }
         }
         RequestContext.getCurrentInstance().update("formParametros:empleadoDesdeParametro");
         if (reporteSeleccionado.getEmhasta().equals("SI")) {
            empleadoHastaParametro = (InputText) FacesContext.getCurrentInstance().getViewRoot().findComponent("formParametros:empleadoHastaParametro");
            empleadoHastaParametro.setStyle(empleadoHastaParametro.getStyle() + "color: red;");
            RequestContext.getCurrentInstance().update("formParametros:empleadoHastaParametro");
         }
         if (reporteSeleccionado.getGrupo() != null && reporteSeleccionado.getGrupo().equals("SI")) {
            grupoParametro = (InputText) FacesContext.getCurrentInstance().getViewRoot().findComponent("formParametros:grupoParametro");
            grupoParametro.setStyle(grupoParametro.getStyle() + "color: red;");
            RequestContext.getCurrentInstance().update("formParametros:grupoParametro");
         }
         if (reporteSeleccionado.getLocalizacion().equals("SI")) {
            estructuraParametro = (InputText) FacesContext.getCurrentInstance().getViewRoot().findComponent("formParametros:estructuraParametro");
            estructuraParametro.setStyle(estructuraParametro.getStyle() + "color: red;");
            RequestContext.getCurrentInstance().update("formParametros:estructuraParametro");
         }
         if (reporteSeleccionado.getTipotrabajador().equals("SI")) {
            tipoTrabajadorParametro = (InputText) FacesContext.getCurrentInstance().getViewRoot().findComponent("formParametros:tipoTrabajadorParametro");
            tipoTrabajadorParametro.setStyle(tipoTrabajadorParametro.getStyle() + "color: red;");
            RequestContext.getCurrentInstance().update("formParametros:tipoTrabajadorParametro");
         }
         if (reporteSeleccionado.getTercero().equals("SI")) {
            terceroParametro = (InputText) FacesContext.getCurrentInstance().getViewRoot().findComponent("formParametros:terceroParametro");
            terceroParametro.setStyle(terceroParametro.getStyle() + "color: red;");
            RequestContext.getCurrentInstance().update("formParametros:terceroParametro");
         }
         if (reporteSeleccionado.getEstado().equals("SI")) {
            estadoParametro = (SelectOneMenu) FacesContext.getCurrentInstance().getViewRoot().findComponent("formParametros:estadoParametro");
            estadoParametro.setStyleClass("selectOneMenuNReporteN");
            RequestContext.getCurrentInstance().update("formParametros:estadoParametro");
         }
         RequestContext.getCurrentInstance().update("formParametros");
         // RequestContext.getCurrentInstance().update("form:reportesNomina");
      } catch (Exception ex) {
         log.warn("Error en selecccion Registro : " + ex.getMessage());
      }
   }

   public void requisitosParaReporte() {
      requisitosReporte = "";
      if (reporteSeleccionado.getFecdesde().equals("SI")) {
         requisitosReporte = requisitosReporte + "- Fecha Desde -";
      }
      if (reporteSeleccionado.getFechasta().equals("SI")) {
         requisitosReporte = requisitosReporte + "- Fecha Hasta -";
      }
      if (reporteSeleccionado.getEmdesde().equals("SI")) {
         requisitosReporte = requisitosReporte + "- Empleado Desde -";
      }
      if (reporteSeleccionado.getEmhasta().equals("SI")) {
         requisitosReporte = requisitosReporte + "- Empleado Hasta -";
      }
      if (reporteSeleccionado.getGrupo().equals("SI")) {
         requisitosReporte = requisitosReporte + "- Grupo -";
      }
      if (reporteSeleccionado.getLocalizacion().equals("SI")) {
         requisitosReporte = requisitosReporte + "- Estructura -";
      }
      if (reporteSeleccionado.getTipotrabajador().equals("SI")) {
         requisitosReporte = requisitosReporte + "- Tipo Trabajador -";
      }
      if (reporteSeleccionado.getTercero().equals("SI")) {
         requisitosReporte = requisitosReporte + "- Tercero -";
      }
      if (reporteSeleccionado.getEstado().equals("SI")) {
         requisitosReporte = requisitosReporte + "- Estado -";
      }
      if (!requisitosReporte.isEmpty()) {
         RequestContext.getCurrentInstance().update("formDialogos:requisitosReporte");
         RequestContext.getCurrentInstance().execute("PF('requisitosReporte').show()");
      }
   }

   public void modificacionTipoReporte(Inforeportes reporte, String tipo) {
      reporteSeleccionado = reporte;
      reporteSeleccionado.setTipo(tipo);
      cambiosReporte = false;
      if (listaInfoReportesModificados.isEmpty() || !listaInfoReportesModificados.contains(reporteSeleccionado)) {
         listaInfoReportesModificados.add(reporteSeleccionado);
         mapTipos.put(reporteSeleccionado.getSecuencia(), reporteSeleccionado.getTipo());
      }
      int n = listaInfoReportesModificados.indexOf(reporteSeleccionado);
      listaInfoReportesModificados.get(n).setTipo(tipo);
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void generarReporte(Inforeportes reporte) {
      try {
         guardarCambios();
         reporteSeleccionado = reporte;
         seleccionRegistro();
         generarDocumentoReporte();
      } catch (Exception e) {
         log.warn("Error en generarReporte : " + e.getMessage());
         RequestContext.getCurrentInstance().update("formDialogos:errorGenerandoReporte");
         RequestContext.getCurrentInstance().execute("PF('errorGenerandoReporte').show()");
      }
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
               /*
                     * final RequestContext currentInstance =
                     * RequestContext.getCurrentInstance();
                     * Renderer.instance().render(template);
                     * RequestContext.setCurrentInstance(currentInstance)
                */
               // RequestContext.getCurrentInstance().execute("PF('formDialogos:generandoReporte");
               //generarArchivoReporte(jp);
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
               pathReporteGenerado = "ControlNReportePersonal reportFillError Error: " + e.toString() + "\n" + e.getCause().toString();
            } else {
               pathReporteGenerado = "ControlNReportePersonal reportFillError Error: " + e.toString();
            }
            estadoReporte = true;
            resultadoReporte = "Se estallo";
         }
      };
   }

   public void exportarReporte() throws IOException {
      try {
         if (pathReporteGenerado != null && !pathReporteGenerado.startsWith("Error:")) {
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
            RequestContext.getCurrentInstance().execute("PF('generandoReporte').hide()");
            RequestContext.getCurrentInstance().update("formDialogos:errorGenerandoReporte");
            RequestContext.getCurrentInstance().execute("PF('errorGenerandoReporte').show()");
         }
      } catch (Exception e) {
         log.warn("Error en exportarReporte :" + e.getMessage());
         RequestContext.getCurrentInstance().execute("PF('generandoReporte').hide()");
         RequestContext.getCurrentInstance().update("formDialogos:errorGenerandoReporte");
         RequestContext.getCurrentInstance().execute("PF('errorGenerandoReporte').show()");
      }
   }

   public void validarDescargaReporte() {
      try {
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().execute("PF('generandoReporte').hide()");
         if (pathReporteGenerado != null && !pathReporteGenerado.startsWith("Error:")) {
            if (!tipoReporte.equals("PDF")) {
               RequestContext.getCurrentInstance().execute("PF('descargarReporte').show()");
            } else {
               FileInputStream fis;
               try {
                  log.info("pathReporteGenerado : " + pathReporteGenerado);
                  fis = new FileInputStream(new File(pathReporteGenerado));
                  log.info("fis : " + fis);
                  reporte = new DefaultStreamedContent(fis, "application/pdf");
               } catch (FileNotFoundException ex) {
                  RequestContext.getCurrentInstance().update("formDialogos:errorGenerandoReporte");
                  RequestContext.getCurrentInstance().execute("PF('errorGenerandoReporte').show()");
                  reporte = null;
               }
               if (reporte != null) {
                  log.info("userAgent: " + userAgent);
                  if (reporteSeleccionado != null) {
                     log.info("validar descarga reporte - ingreso al if 4");
                     if (userAgent.toUpperCase().contains("Mobile".toUpperCase()) || userAgent.toUpperCase().contains("Tablet".toUpperCase()) || userAgent.toUpperCase().contains("Android".toUpperCase())) {
                        context.update("formDialogos:descargarReporte");
                        context.execute("PF('descargarReporte').show();");
                     } else {
                        RequestContext.getCurrentInstance().update("formDialogos:verReportePDF");
                        RequestContext.getCurrentInstance().execute("PF('verReportePDF').show()");
                     }
                     cabezeraVisor = "Reporte - " + nombreReporte;
                  } else {
                     cabezeraVisor = "Reporte - ";
                  }
               }
            }
         } else {
            RequestContext.getCurrentInstance().execute("PF('generandoReporte').hide()");
            RequestContext.getCurrentInstance().update("formDialogos:errorGenerandoReporte");
            RequestContext.getCurrentInstance().execute("PF('errorGenerandoReporte').show()");
         }
      } catch (Exception e) {
         log.warn("Error en validarDescargarReporte : " + e.getMessage());
         RequestContext.getCurrentInstance().execute("PF('generandoReporte').hide()");
         RequestContext.getCurrentInstance().update("formDialogos:errorGenerandoReporte");
         RequestContext.getCurrentInstance().execute("PF('errorGenerandoReporte').show()");
      }
   }

   public void generarDocumentoReporte() {
      try {
         RequestContext context = RequestContext.getCurrentInstance();
         if (reporteSeleccionado != null) {
            nombreReporte = reporteSeleccionado.getNombrereporte();
            tipoReporte = reporteSeleccionado.getTipo();

            if (nombreReporte != null && tipoReporte != null) {
               pathReporteGenerado = administarReportes.generarReporte(nombreReporte, tipoReporte);
            }
            if (pathReporteGenerado != null) {
               validarDescargaReporte();
            } else {
               RequestContext.getCurrentInstance().execute("PF('generandoReporte').hide()");
               RequestContext.getCurrentInstance().update("formDialogos:errorGenerandoReporte");
               RequestContext.getCurrentInstance().execute("PF('errorGenerandoReporte').show()");
            }
         } else {
            log.info("Reporte Seleccionado es nulo");
         }
      } catch (Exception e) {
         log.warn("Error en generarDocumentoReporte : " + e.getMessage());
         RequestContext.getCurrentInstance().execute("PF('generandoReporte').hide()");
         RequestContext.getCurrentInstance().update("formDialogos:errorGenerandoReporte");
         RequestContext.getCurrentInstance().execute("PF('errorGenerandoReporte').show()");
      }
   }

   public void dispararDialogoGuardarCambios() {
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");

   }

   public void cancelarReporte() {
      administarReportes.cancelarReporte();
   }

   public void defaultPropiedadesParametrosReporte() {
      color = "black";
      decoracion = "none";
      color2 = "black";
      decoracion2 = "none";
   }

   public void reiniciarStreamedContent() {
      reporte = null;
   }

   public void mostrarDialogoBuscarReporte() {
      try {
         if (cambiosReporte == true) {
            contarRegistrosLovReportes();
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioReportes:ReportesDialogo");
            RequestContext.getCurrentInstance().execute("PF('ReportesDialogo').show()");
         } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardarSinSalida').show()");
         }
      } catch (Exception e) {
         log.warn("Error mostrarDialogoBuscarReporte : " + e.toString());
      }
   }

   public void mostrarTodos() {
      if (cambiosReporte == true) {
         defaultPropiedadesParametrosReporte();
         listaIR.clear();
         for (int i = 0; i < lovInforeportes.size(); i++) {
            listaIR.add(lovInforeportes.get(i));
         }
         reporteSeleccionado = null;
         contarRegistros();
         RequestContext context = RequestContext.getCurrentInstance();
         activoBuscarReporte = false;
         activoMostrarTodos = true;
         RequestContext.getCurrentInstance().update("form:MOSTRARTODOS");
         RequestContext.getCurrentInstance().update("form:BUSCARREPORTE");
         RequestContext.getCurrentInstance().update("form:reportesNomina");
      } else {
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().execute("PF('confirmarGuardarSinSalida').show()");
      }
   }

   public void cancelarRequisitosReporte() {
      requisitosReporte = "";
   }

   public void guardarYSalir() {
      guardarCambios();
      salir();
   }

   public void activarAceptar() {
      aceptar = false;
   }

   public void actualizarEmplDesde() {
      permitirIndex = true;
      parametroDeReporte.setCodigoempleadodesde(empleadoSeleccionado.getCodigoempleado());
      parametroModificacion = parametroDeReporte;
      cambiosReporte = false;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formularioEmpleadoDesde:lovEmpleadoDesde:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovEmpleadoDesde').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('EmpleadoDesdeDialogo').hide()");

      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      RequestContext.getCurrentInstance().update("formParametros:empleadoDesdeParametro");
      empleadoSeleccionado = null;
      aceptar = true;
      lovEmpleadosFiltrar = null;

   }

   public void cancelarCambioEmplDesde() {
      empleadoSeleccionado = null;
      aceptar = true;
      lovEmpleadosFiltrar = null;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formularioEmpleadoDesde:lovEmpleadoDesde:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovEmpleadoDesde').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('EmpleadoDesdeDialogo').hide()");
   }

   public void actualizarEmplHasta() {
      permitirIndex = true;
      parametroDeReporte.setCodigoempleadohasta(empleadoSeleccionado.getCodigoempleado());
      parametroModificacion = parametroDeReporte;
      cambiosReporte = false;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formularioEmpleadoHasta:lovEmpleadoHasta:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovEmpleadoHasta').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('EmpleadoHastaDialogo').hide()");

      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      RequestContext.getCurrentInstance().update("formParametros:empleadoHastaParametro");
      empleadoSeleccionado = null;
      aceptar = true;
      lovEmpleadosFiltrar = null;
   }

   public void cancelarCambioEmplHasta() {
      empleadoSeleccionado = null;
      aceptar = true;
      lovEmpleadosFiltrar = null;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formularioEmpleadoHasta:lovEmpleadoHasta:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovEmpleadoHasta').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('EmpleadoHastaDialogo').hide()");
   }

   public void actualizarGrupo() {
      permitirIndex = true;
      parametroDeReporte.setGrupo(grupoCSeleccionado);
      parametroModificacion = parametroDeReporte;
      cambiosReporte = false;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formularioGrupoConcepto:lovGruposConceptos:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovGruposConceptos').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('GruposConceptosDialogo').hide()");

      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      RequestContext.getCurrentInstance().update("formParametros:grupoParametro");
      grupoCSeleccionado = null;
      aceptar = true;
      lovGruposConceptosFiltrar = null;

   }

   public void cancelarCambioGrupo() {
      grupoCSeleccionado = null;
      aceptar = true;
      lovGruposConceptosFiltrar = null;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formularioGrupoConcepto:lovGruposConceptos:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovGruposConceptos').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('GruposConceptosDialogo').hide()");
   }

   public void actualizarUbicacionGeografica() {
      permitirIndex = true;
      parametroDeReporte.setUbicaciongeografica(ubicacionesGSeleccionado);
      parametroModificacion = parametroDeReporte;
      cambiosReporte = false;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formularioUbicacion:lovUbicacionGeografica:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovUbicacionGeografica').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('UbiGeograficaDialogo').hide()");

      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      RequestContext.getCurrentInstance().update("formParametros:ubicacionGeograficaParametro");
      ubicacionesGSeleccionado = null;
      aceptar = true;
      lovUbicacionesGeograficasFiltrar = null;

   }

   public void cancelarCambioUbicacionGeografica() {
      ubicacionesGSeleccionado = null;
      aceptar = true;
      lovUbicacionesGeograficasFiltrar = null;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formularioUbicacion:lovUbicacionGeografica:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovUbicacionGeografica').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('UbiGeograficaDialogo').hide()");
   }

   public void actualizarTipoAsociacion() {
      permitirIndex = true;
      parametroDeReporte.setTipoasociacion(tiposASeleccionado);
      parametroModificacion = parametroDeReporte;
      cambiosReporte = false;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formularioTipoAsociacion:lovTipoAsociacion:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovTipoAsociacion').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('TipoAsociacionDialogo').hide()");

      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      RequestContext.getCurrentInstance().update("formParametros:tipoAsociacionParametro");
      tiposASeleccionado = null;
      aceptar = true;
      lovTiposAsociacionesFiltrar = null;

   }

   public void cancelarTipoAsociacion() {
      tiposASeleccionado = null;
      aceptar = true;
      lovTiposAsociacionesFiltrar = null;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formularioTipoAsociacion:lovTipoAsociacion:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovTipoAsociacion').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('TipoAsociacionDialogo').hide()");
   }

   public void actualizarEmpresa() {
      permitirIndex = true;
      parametroDeReporte.setEmpresa(empresaSeleccionada);
      parametroModificacion = parametroDeReporte;
      cambiosReporte = false;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formularioEmpresa:lovEmpresa:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovEmpresa').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('EmpresaDialogo').hide()");

      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      RequestContext.getCurrentInstance().update("formParametros:empresaParametro");
      empresaSeleccionada = null;
      aceptar = true;
      lovEmpresasFiltrar = null;

   }

   public void cancelarEmpresa() {
      empresaSeleccionada = null;
      aceptar = true;
      lovEmpresasFiltrar = null;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formularioEmpresa:lovEmpresa:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovEmpresa').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('EmpresaDialogo').hide()");
   }

   public void actualizarEstructura() {
      parametroDeReporte.setLocalizacion(estructuraSeleccionada);
      parametroModificacion = parametroDeReporte;
      cambiosReporte = false;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formularioEstructura:lovEstructura:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovEstructura').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('EstructuraDialogo').hide()");

      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      RequestContext.getCurrentInstance().update("formParametros:estructuraParametro");
      estructuraSeleccionada = null;
      aceptar = true;
      lovEstructurasFiltrar = null;
      permitirIndex = true;

   }

   public void cancelarEstructura() {
      estructuraSeleccionada = null;
      aceptar = true;
      lovEstructurasFiltrar = null;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formularioEstructura:lovEstructura:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovEstructura').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('EstructuraDialogo').hide()");
   }

   public void actualizarTipoTrabajador() {
      parametroDeReporte.setTipotrabajador(tipoTSeleccionado);
      parametroModificacion = parametroDeReporte;
      cambiosReporte = false;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formularioTipoTrabajador:lovTipoTrabajador:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovTipoTrabajador').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('TipoTrabajadorDialogo').hide()");

      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      RequestContext.getCurrentInstance().update("formParametros:tipoTrabajadorParametro");
      tipoTSeleccionado = null;
      aceptar = true;
      lovTiposTrabajadoresFiltrar = null;
      permitirIndex = true;

   }

   public void cancelarTipoTrabajador() {
      tipoTSeleccionado = null;
      aceptar = true;
      lovTiposTrabajadoresFiltrar = null;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formularioTipoTrabajador:lovTipoTrabajador:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovTipoTrabajador').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('TipoTrabajadorDialogo').hide()");
   }

   public void actualizarTercero() {
      permitirIndex = true;
      parametroDeReporte.setTercero(terceroSeleccionado);
      parametroModificacion = parametroDeReporte;
      cambiosReporte = false;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formularioTercero:lovTercero:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovTercero').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('TerceroDialogo').hide()");

      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      RequestContext.getCurrentInstance().update("formParametros:terceroParametro");
      terceroSeleccionado = null;
      aceptar = true;
      lovTercerosFiltrar = null;

   }

   public void cancelarTercero() {
      terceroSeleccionado = null;
      aceptar = true;
      lovTercerosFiltrar = null;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formularioTercero:lovTercero:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovTercero').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('TerceroDialogo').hide()");
   }

   public void actualizarProceso() {
      permitirIndex = true;
      parametroDeReporte.setProceso(procesoSeleccionado);
      parametroModificacion = parametroDeReporte;
      cambiosReporte = false;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formularioProceso:lovProceso:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovProceso').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('ProcesoDialogo').hide()");

      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      RequestContext.getCurrentInstance().update("formParametros:procesoParametro");
      procesoSeleccionado = null;
      aceptar = true;
      lovProcesosFiltrar = null;

   }

   public void cancelarProceso() {
      procesoSeleccionado = null;
      aceptar = true;
      lovProcesosFiltrar = null;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formularioProceso:lovProceso:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovProceso').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('ProcesoDialogo').hide()");
   }

   public void actualizarAsociacion() {
      permitirIndex = true;
      parametroDeReporte.setAsociacion(asociacionSeleccionado);
      parametroModificacion = parametroDeReporte;
      cambiosReporte = false;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formularioAsociacion:lovAsociacion:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovAsociacion').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('AsociacionDialogo').hide()");

      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      RequestContext.getCurrentInstance().update("formParametros:asociacionParametro");
      asociacionSeleccionado = null;
      aceptar = true;
      lovAsociacionesFiltrar = null;

   }

   public void cancelarAsociacion() {
      asociacionSeleccionado = null;
      aceptar = true;
      lovAsociacionesFiltrar = null;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formularioAsociacion:lovAsociacion:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovAsociacion').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('AsociacionDialogo').hide()");
   }

   public void cancelarSeleccionInforeporte() {
      filtrarListIRU = null;
      reporteSeleccionadoLOV = null;
      aceptar = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formularioReportes:lovReportesDialogo:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovReportesDialogo').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('ReportesDialogo').hide()");
   }

   public void actualizarSeleccionInforeporte() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (bandera == 1) {
         cerrarFiltrado();
      }
      defaultPropiedadesParametrosReporte();
      listaIR.clear();
      listaIR.add(reporteSeleccionadoLOV);
      filtrarListIRU = null;
      filtrarLovInforeportes = null;
      aceptar = true;
      activoBuscarReporte = false;
      activoMostrarTodos = false;
      reporteSeleccionado = reporteSeleccionadoLOV;
      reporteSeleccionadoLOV = null;
      RequestContext.getCurrentInstance().update("form:MOSTRARTODOS");
      RequestContext.getCurrentInstance().update("form:BUSCARREPORTE");
      context.reset("formularioReportes:lovReportesDialogo:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovReportesDialogo').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('ReportesDialogo').hide()");
      RequestContext.getCurrentInstance().update("form:reportesNomina");
      contarRegistros();
   }

   //AUTOCOMPLETAR
   public void autocompletarGeneral(String campoConfirmar, String valorConfirmar) {
      RequestContext context = RequestContext.getCurrentInstance();
      int indiceUnicoElemento = -1;
      int coincidencias = 0;
      if (campoConfirmar.equalsIgnoreCase("GRUPO")) {
         if (!valorConfirmar.isEmpty()) {
            parametroDeReporte.getGrupo().setDescripcion(grupo);
            for (int i = 0; i < lovGruposConceptos.size(); i++) {
               if (lovGruposConceptos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               parametroDeReporte.setGrupo(lovGruposConceptos.get(indiceUnicoElemento));
               parametroModificacion = parametroDeReporte;
               lovGruposConceptos.clear();
               getLovGruposConceptos();
               cambiosReporte = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            } else {
               permitirIndex = false;
               RequestContext.getCurrentInstance().update("formularioGrupoConcepto:GruposConceptosDialogo");
               RequestContext.getCurrentInstance().execute("PF('GruposConceptosDialogo').show()");
            }
         } else {
            parametroDeReporte.setGrupo(new GruposConceptos());
            parametroModificacion = parametroDeReporte;
            lovGruposConceptos.clear();
            getLovGruposConceptos();
            cambiosReporte = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
      }
      if (campoConfirmar.equalsIgnoreCase("UBIGEO")) {
         if (!valorConfirmar.isEmpty()) {
            parametroDeReporte.getUbicaciongeografica().setDescripcion(ubiGeo);
            for (int i = 0; i < lovUbicacionesGeograficas.size(); i++) {
               if (lovUbicacionesGeograficas.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               parametroDeReporte.setUbicaciongeografica(lovUbicacionesGeograficas.get(indiceUnicoElemento));
               parametroModificacion = parametroDeReporte;
               lovUbicacionesGeograficas.clear();
               getLovUbicacionesGeograficas();
               cambiosReporte = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            } else {
               permitirIndex = false;
               RequestContext.getCurrentInstance().update("formularioUbicacion:UbiGeograficaDialogo");
               RequestContext.getCurrentInstance().execute("PF('UbiGeograficaDialogo').show()");
            }
         } else {
            parametroDeReporte.setUbicaciongeografica(new UbicacionesGeograficas());
            parametroModificacion = parametroDeReporte;
            lovUbicacionesGeograficas.clear();
            getLovUbicacionesGeograficas();
            cambiosReporte = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
      }
      if (campoConfirmar.equalsIgnoreCase("EMPRESA")) {
         if (!valorConfirmar.isEmpty()) {
            parametroDeReporte.getEmpresa().setNombre(empresa);
            for (int i = 0; i < lovEmpresas.size(); i++) {
               if (lovEmpresas.get(i).getNombre().startsWith(valorConfirmar)) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               parametroDeReporte.setEmpresa(lovEmpresas.get(indiceUnicoElemento));
               parametroModificacion = parametroDeReporte;
               lovEmpresas.clear();
               getLovEmpresas();
               cambiosReporte = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            } else {
               permitirIndex = false;
               RequestContext.getCurrentInstance().update("formularioEmpresa:EmpresaDialogo");
               RequestContext.getCurrentInstance().execute("PF('EmpresaDialogo').show()");
            }
         } else {
            parametroDeReporte.setEmpresa(new Empresas());
            parametroModificacion = parametroDeReporte;
            lovEmpresas.clear();
            getLovEmpresas();
            cambiosReporte = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
      }
      if (campoConfirmar.equalsIgnoreCase("TIPOASO")) {
         if (!valorConfirmar.isEmpty()) {
            parametroDeReporte.getTipoasociacion().setDescripcion(tipoAso);
            for (int i = 0; i < lovTiposAsociaciones.size(); i++) {
               if (lovTiposAsociaciones.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               parametroDeReporte.setTipoasociacion(lovTiposAsociaciones.get(indiceUnicoElemento));
               parametroModificacion = parametroDeReporte;
               lovTiposAsociaciones.clear();
               getLovTiposAsociaciones();
               cambiosReporte = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            } else {
               permitirIndex = false;
               RequestContext.getCurrentInstance().update("formularioTipoAsociacion:TipoAsociacionDialogo");
               RequestContext.getCurrentInstance().execute("PF('TipoAsociacionDialogo').show()");
            }
         } else {
            parametroDeReporte.setTipoasociacion(new TiposAsociaciones());
            parametroModificacion = parametroDeReporte;
            lovTiposAsociaciones.clear();
            getLovTiposAsociaciones();
            cambiosReporte = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
      }
      if (campoConfirmar.equalsIgnoreCase("ESTRUCTURA")) {
         if (!valorConfirmar.isEmpty()) {
            parametroDeReporte.getLocalizacion().setNombre(estructura);
            for (int i = 0; i < lovEstructuras.size(); i++) {
               if (lovEstructuras.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               parametroDeReporte.setLocalizacion(lovEstructuras.get(indiceUnicoElemento));
               parametroModificacion = parametroDeReporte;
               lovEstructuras.clear();
               getLovEstructuras();
               cambiosReporte = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            } else {
               permitirIndex = false;
               RequestContext.getCurrentInstance().update("formularioEstructura:EstructuraDialogo");
               RequestContext.getCurrentInstance().execute("PF('EstructuraDialogo').show()");
            }
         } else {
            parametroDeReporte.setLocalizacion(new Estructuras());
            parametroModificacion = parametroDeReporte;
            cambiosReporte = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
      }
      if (campoConfirmar.equalsIgnoreCase("TIPOTRABAJADOR")) {
         if (!valorConfirmar.isEmpty()) {
            parametroDeReporte.getTipotrabajador().setNombre(tipoTrabajador);
            for (int i = 0; i < lovTiposTrabajadores.size(); i++) {
               if (lovTiposTrabajadores.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               parametroDeReporte.setTipotrabajador(lovTiposTrabajadores.get(indiceUnicoElemento));
               parametroModificacion = parametroDeReporte;
               lovTiposTrabajadores.clear();
               getLovTiposTrabajadores();
               cambiosReporte = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            } else {
               permitirIndex = false;
               RequestContext.getCurrentInstance().update("formularioTipoTrabajador:TipoTrabajadorDialogo");
               RequestContext.getCurrentInstance().execute("PF('TipoTrabajadorDialogo').show()");
            }
         } else {
            parametroDeReporte.setTipotrabajador(new TiposTrabajadores());
            parametroModificacion = parametroDeReporte;
            cambiosReporte = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
      }
      if (campoConfirmar.equalsIgnoreCase("TERCERO")) {
         if (!valorConfirmar.isEmpty()) {
            parametroDeReporte.getTercero().setNombre(tercero);
            for (int i = 0; i < lovTerceros.size(); i++) {
               if (lovTerceros.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               parametroDeReporte.setTercero(lovTerceros.get(indiceUnicoElemento));
               parametroModificacion = parametroDeReporte;
               lovTerceros.clear();
               getLovTerceros();
               cambiosReporte = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            } else {
               permitirIndex = false;
               RequestContext.getCurrentInstance().update("formularioTercero:TerceroDialogo");
               RequestContext.getCurrentInstance().execute("PF('TerceroDialogo').show()");
            }
         } else {
            parametroDeReporte.setTercero(new Terceros());
            parametroModificacion = parametroDeReporte;
            lovTerceros.clear();
            getLovTerceros();
            cambiosReporte = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
      }
      if (campoConfirmar.equalsIgnoreCase("PROCESO")) {
         if (!valorConfirmar.isEmpty()) {
            parametroDeReporte.getProceso().setDescripcion(proceso);
            for (int i = 0; i < lovProcesos.size(); i++) {
               if (lovProcesos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               parametroDeReporte.setProceso(lovProcesos.get(indiceUnicoElemento));
               parametroModificacion = parametroDeReporte;
               lovProcesos.clear();
               getLovProcesos();
               cambiosReporte = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            } else {
               permitirIndex = false;
               RequestContext.getCurrentInstance().update("formularioProceso:ProcesoDialogo");
               RequestContext.getCurrentInstance().execute("PF('ProcesoDialogo').show()");
            }
         } else {
            parametroDeReporte.setProceso(new Procesos());
            parametroModificacion = parametroDeReporte;
            lovProcesos.clear();
            getLovProcesos();
            cambiosReporte = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
      }
      if (campoConfirmar.equalsIgnoreCase("ASOCIACION")) {
         if (!valorConfirmar.isEmpty()) {
            parametroDeReporte.getAsociacion().setDescripcion(asociacion);
            for (int i = 0; i < lovAsociaciones.size(); i++) {
               if (lovAsociaciones.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               parametroDeReporte.setAsociacion(lovAsociaciones.get(indiceUnicoElemento));
               parametroModificacion = parametroDeReporte;
               lovAsociaciones.clear();
               getLovAsociaciones();
               cambiosReporte = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            } else {
               permitirIndex = false;
               RequestContext.getCurrentInstance().update("formularioAsociacion:AsociacionDialogo");
               RequestContext.getCurrentInstance().execute("PF('AsociacionDialogo').show()");
            }
         } else {
            parametroDeReporte.setAsociacion(new Asociaciones());
            parametroModificacion = parametroDeReporte;
            lovAsociaciones.clear();
            getLovAsociaciones();
            cambiosReporte = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
      }
   }

   public void modificarParametroEmpleadoDesde(BigDecimal empldesde) {
      if (empldesde.equals("") || empldesde == null) {
         parametroDeReporte.setCodigoempleadodesde(BigDecimal.valueOf(0));
      }
      if (empldesde.equals(BigDecimal.valueOf(0))) {
         parametroDeReporte.setCodigoempleadodesde(BigDecimal.valueOf(0));
      }
      cambiosReporte = false;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void modificarParametroEmpleadoHasta(BigDecimal emphasta) {
      String h = "999999999999999999999999999999";
      BigDecimal b = new BigDecimal(h);
      System.out.println("emplHasta: " + emplHasta);
      if (emplHasta.equals("") || emplHasta == null) {
         parametroDeReporte.setCodigoempleadodesde(b);
      }
      if (emphasta.equals(b)) {
         parametroDeReporte.setCodigoempleadodesde(b);
      }
      cambiosReporte = false;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   //POSICION CELDA
   public void posicionCelda(int i) {
      casilla = i;
      if (permitirIndex == true) {
         switch (casilla) {
            case 1:
               deshabilitarBotonLov();
               fechaDesde = parametroDeReporte.getFechadesde();
               break;
            case 2:
               habilitarBotonLov();
               emplDesde = parametroDeReporte.getCodigoempleadodesde();
               break;
            case 4:
               habilitarBotonLov();
               grupo = parametroDeReporte.getGrupo().getDescripcion();
               break;
            case 5:
               habilitarBotonLov();
               ubiGeo = parametroDeReporte.getUbicaciongeografica().getDescripcion();
               break;
            case 6:
               habilitarBotonLov();
               tipoAso = parametroDeReporte.getTipoasociacion().getDescripcion();
               break;
            case 7:
               deshabilitarBotonLov();
               fechaHasta = parametroDeReporte.getFechahasta();
               break;
            case 8:
               habilitarBotonLov();
               emplHasta = parametroDeReporte.getCodigoempleadohasta();
               break;
            case 10:
               habilitarBotonLov();
               empresa = parametroDeReporte.getEmpresa().getNombre();
               break;
            case 11:
               habilitarBotonLov();
               estructura = parametroDeReporte.getLocalizacion().getNombre();
               break;
            case 12:
               habilitarBotonLov();
               tipoTrabajador = parametroDeReporte.getTipotrabajador().getNombre();
               break;
            case 13:
               habilitarBotonLov();
               tercero = parametroDeReporte.getTercero().getNombre();
               break;
            case 14:
               habilitarBotonLov();
               proceso = parametroDeReporte.getProceso().getDescripcion();
               break;
            case 15:
               deshabilitarBotonLov();
               parametroDeReporte.getMensajedesprendible();
               break;
            case 16:
               habilitarBotonLov();
               asociacion = parametroDeReporte.getAsociacion().getDescripcion();
               break;
            default:
               break;
         }

      }
   }

   //DIALOGOS PARAMETROS
   public void dialogosParametros(int pos) {
      log.info(this.getClass().getName() + ".dialogosParametros()");
      RequestContext context = RequestContext.getCurrentInstance();
      if (pos == 2) {
         lovEmpleados = null;
         cargarListaEmpleados();
         contarRegistrosEmpeladoD();
         RequestContext.getCurrentInstance().update("formularioEmpleadoDesde:EmpleadoDesdeDialogo");
         RequestContext.getCurrentInstance().execute("PF('EmpleadoDesdeDialogo').show()");
      }
      if (pos == 4) {
         lovGruposConceptos = null;
         cargarListaGruposConcepto();
         RequestContext.getCurrentInstance().update("formularioGrupoConcepto:GruposConceptosDialogo");
         RequestContext.getCurrentInstance().execute("PF('GruposConceptosDialogo').show()");
         contarRegistrosGrupo();
      }
      if (pos == 5) {
         lovUbicacionesGeograficas = null;
         cargarListaUbicacionesGeograficas();
         RequestContext.getCurrentInstance().update("formularioUbicacion:UbiGeograficaDialogo");
         RequestContext.getCurrentInstance().execute("PF('UbiGeograficaDialogo').show()");
         contarRegistrosUbicacion();
      }
      if (pos == 6) {
         lovTiposAsociaciones = null;
         getLovTiposAsociaciones();
         RequestContext.getCurrentInstance().update("formularioTipoAsociacion:TipoAsociacionDialogo");
         RequestContext.getCurrentInstance().execute("PF('TipoAsociacionDialogo').show()");
         contarRegistrosTipoAsociacion();
      }
      if (pos == 8) {
         lovEmpleados = null;
         cargarListaEmpleados();
         contarRegistrosEmpeladoH();
         RequestContext.getCurrentInstance().update("formularioEmpleadoHasta:EmpleadoHastaDialogo");
         RequestContext.getCurrentInstance().execute("PF('EmpleadoHastaDialogo').show()");
      }
      if (pos == 10) {
         lovEmpresas = null;
         getLovEmpresas();
         RequestContext.getCurrentInstance().update("formularioEmpresa:EmpresaDialogo");
         RequestContext.getCurrentInstance().execute("PF('EmpresaDialogo').show()");
         contarRegistrosEmpresa();
      }
      if (pos == 11) {
         lovEstructuras = null;
         cargarListaEstructuras();
         RequestContext.getCurrentInstance().update("formularioEstructura:EstructuraDialogo");
         RequestContext.getCurrentInstance().execute("PF('EstructuraDialogo').show()");
         contarRegistrosEstructura();
      }
      if (pos == 12) {
         lovTiposTrabajadores = null;
         cargarListaTiposTrabajadores();
         RequestContext.getCurrentInstance().update("formularioTipoTrabajador:TipoTrabajadorDialogo");
         RequestContext.getCurrentInstance().execute("PF('TipoTrabajadorDialogo').show()");
         contarRegistrosTipoTrabajador();
      }
      if (pos == 13) {
         lovTerceros = null;
         cargarListaTerceros();
         RequestContext.getCurrentInstance().update("formularioTercero:TerceroDialogo");
         RequestContext.getCurrentInstance().execute("PF('TerceroDialogo').show()");
         contarRegistrosTercero();
      }
      if (pos == 14) {
         lovProcesos = null;
         cargarListaProcesos();
         RequestContext.getCurrentInstance().update("formularioProceso:ProcesoDialogo");
         RequestContext.getCurrentInstance().execute("PF('ProcesoDialogo').show()");
         contarRegistrosProceso();
      }
      if (pos == 16) {
         lovAsociaciones = null;
         cargarListaAsociaciones();
         RequestContext.getCurrentInstance().update("formularioAsociacion:AsociacionDialogo");
         RequestContext.getCurrentInstance().execute("PF('AsociacionDialogo').show()");
         contarRegistrosAsociacion();
      }

   }

   //MODIFICAR PARAMETRO REPORTE
   public void modificarParametroInforme() {
      log.info("parametroDeReporte.getFechadesde(): " + parametroDeReporte.getFechadesde());
      log.info("parametroDeReporte.getFechahasta(): " + parametroDeReporte.getFechahasta());
      if (parametroDeReporte.getFechadesde() != null && parametroDeReporte.getFechahasta() != null) {
         if (parametroDeReporte.getFechadesde().before(parametroDeReporte.getFechahasta())) {
            parametroModificacion = parametroDeReporte;
            cambiosReporte = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         } else {
            cambiosReporte = true;
         }
      }
   }

   public void mostrarDialogoGenerarReporte() {
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("formDialogos:reporteAGenerar");
      RequestContext.getCurrentInstance().execute("PF('reporteAGenerar').show()");
   }

   public void cancelarGenerarReporte() {
      reporteGenerar = "";
      posicionReporte = -1;
   }

   public void mostrarDialogoNuevaFecha() {
      getParametroDeInforme();
      if (parametroDeReporte.getFechadesde() == null && parametroDeReporte.getFechahasta() == null) {
         RequestContext.getCurrentInstance().update("formDialogos:nuevoRegistroFechas");
         RequestContext.getCurrentInstance().execute("PF('nuevoRegistroFechas').show()");
      }
   }

   public void agregarFecha() {
      int contador = 0;
      if (parametroFecha.getFechadesde() == null) {
         contador++;
      }
      if (parametroFecha.getFechahasta() == null) {
         contador++;
      }

      if (contador == 0) {
         parametroDeReporte.setFechadesde(parametroFecha.getFechadesde());
         parametroDeReporte.setFechahasta(parametroFecha.getFechahasta());
         RequestContext.getCurrentInstance().update("formParametros:fechaDesdeParametro");
         RequestContext.getCurrentInstance().update("formParametros:fechaHastaParametro");
         aceptar = false;
         RequestContext.getCurrentInstance().execute("form:ACEPTAR");
         RequestContext.getCurrentInstance().execute("PF('nuevoRegistroFechas').hide()");
      } else {
         RequestContext.getCurrentInstance().update("formDialogos:validacionRegistroFechas");
         RequestContext.getCurrentInstance().execute("PF('validacionRegistroFechas').show()");
      }

   }

   public void limpiarNuevaFecha() {
      parametroFecha.setFechadesde(null);
      parametroFecha.setFechahasta(null);
   }

   //CONTARREGISTROS
   public void contarRegistros() {
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
   }

   public void contarRegistrosLovReportes() {
      RequestContext.getCurrentInstance().update("formularioReportes:infoRegistroReportes");
   }

   public void contarRegistrosEmpeladoD() {
      RequestContext.getCurrentInstance().update("formularioEmpleadoDesde:infoRegistroEmpleadoDesde");
   }

   public void contarRegistrosEmpeladoH() {
      RequestContext.getCurrentInstance().update("formularioEmpleadoHasta:infoRegistroEmpleadoHasta");
   }

   public void contarRegistrosGrupo() {
      RequestContext.getCurrentInstance().update("formularioGrupoConcepto:infoRegistroGrupoConcepto");
   }

   public void contarRegistrosUbicacion() {
      RequestContext.getCurrentInstance().update("formularioUbicacion:infoRegistroUbicacion");
   }

   public void contarRegistrosTipoAsociacion() {
      RequestContext.getCurrentInstance().update("formularioTipoAsociacion:infoRegistroTipoAsociacion");
   }

   public void contarRegistrosEmpresa() {
      RequestContext.getCurrentInstance().update("formularioEmpresa:infoRegistroEmpresa");
   }

   public void contarRegistrosAsociacion() {
      RequestContext.getCurrentInstance().update("formularioAsociacion:infoRegistroAsociacion");
   }

   public void contarRegistrosTercero() {
      RequestContext.getCurrentInstance().update("formularioTercero:infoRegistroTercero");
   }

   public void contarRegistrosProceso() {
      RequestContext.getCurrentInstance().update("formularioProceso:infoRegistroProceso");
   }

   public void contarRegistrosEstructura() {
      RequestContext.getCurrentInstance().update("formularioEstructura:infoRegistroEstructura");
   }

   public void contarRegistrosTipoTrabajador() {
      RequestContext.getCurrentInstance().update("formularioTipoTrabajador:infoRegistroTipoTrabajador");

   }

   private static abstract class FacesContextWrapper extends FacesContext {

      protected static void setCurrentInstance(FacesContext facesContext) {
         FacesContext.setCurrentInstance(facesContext);
      }
   }

   public void cargarListaEmpleados() {
      if (lovEmpleados == null) {
         if (listasRecurrentes.getLovEmpleadosActivos().isEmpty()) {
            lovEmpleados = administrarNReportesNomina.listEmpleados();
            if (lovEmpleados != null) {
               log.warn("GUARDANDO lovEmpleadosActivos en Listas recurrentes");
               listasRecurrentes.setLovEmpleadosActivos(lovEmpleados);
            }
         } else {
            lovEmpleados = new ArrayList<Empleados>(listasRecurrentes.getLovEmpleadosActivos());
            log.warn("CONSULTANDO lovEmpleadosActivos de Listas recurrentes");
         }
      }
   }

   public void cargarListaGruposConcepto() {
      if (lovGruposConceptos == null) {
         if (listasRecurrentes.getLovGruposConceptos().isEmpty()) {
            lovGruposConceptos = administrarNReportesNomina.listGruposConcetos();
            if (lovGruposConceptos != null) {
               listasRecurrentes.setLovGruposConceptos(lovGruposConceptos);
            }
         } else {
            lovGruposConceptos = new ArrayList<GruposConceptos>(listasRecurrentes.getLovGruposConceptos());
         }
      }
   }

   public void cargarListaUbicacionesGeograficas() {
      if (lovUbicacionesGeograficas == null) {
         lovUbicacionesGeograficas = administrarNReportesNomina.listUbicacionesGeograficas();
      }
   }

   public void cargarListaEstructuras() {
      if (lovEstructuras == null) {
         lovEstructuras = administrarNReportesNomina.listEstructuras();
      }
   }

   public void cargarListaTiposTrabajadores() {
      if (lovTiposTrabajadores == null) {
         if (listasRecurrentes.getLovTiposTrabajadores().isEmpty()) {
            lovTiposTrabajadores = administrarNReportesNomina.listTiposTrabajadores();
            if (lovTiposTrabajadores != null) {
               listasRecurrentes.setLovTiposTrabajadores(lovTiposTrabajadores);
            }
         } else {
            lovTiposTrabajadores = new ArrayList<TiposTrabajadores>(listasRecurrentes.getLovTiposTrabajadores());
         }
      }
   }

   public void cargarListaTerceros() {
      if (lovTerceros == null) {
         if (empresa != null) {
            lovTerceros = administrarNReportesNomina.listTercerosSecEmpresa(this.getParametroDeInforme().getEmpresa().getSecuencia());
         } else {
            lovTerceros = administrarNReportesNomina.listTerceros();
         }
      }
   }

   public void cargarListaProcesos() {
      if (lovProcesos == null) {
         lovProcesos = administrarNReportesNomina.listProcesos();
      }
   }

   public void cargarListaAsociaciones() {
      if (lovAsociaciones == null) {
         lovAsociaciones = administrarNReportesNomina.listAsociaciones();
      }
   }

   public void habilitarBotonLov() {
      activarLov = false;
      RequestContext.getCurrentInstance().update("form:listaValores");
   }

   public void deshabilitarBotonLov() {
      activarLov = true;
      RequestContext.getCurrentInstance().update("form:listaValores");
   }

   //GETTER AND SETTER
   public ParametrosReportes getParametroDeInforme() {
      try {
         if (parametroDeReporte == null) {
            parametroDeReporte = new ParametrosReportes();
            parametroDeReporte = administrarNReportesNomina.parametrosDeReporte();
         }
         if (parametroDeReporte.getGrupo() == null) {
            parametroDeReporte.setGrupo(new GruposConceptos());
         }
         if (parametroDeReporte.getUbicaciongeografica() == null) {
            parametroDeReporte.setUbicaciongeografica(new UbicacionesGeograficas());
         }
         if (parametroDeReporte.getTipoasociacion() == null) {
            parametroDeReporte.setTipoasociacion(new TiposAsociaciones());
         }
         if (parametroDeReporte.getLocalizacion() == null) {
            parametroDeReporte.setLocalizacion(new Estructuras());
         }
         if (parametroDeReporte.getTipotrabajador() == null) {
            parametroDeReporte.setTipotrabajador(new TiposTrabajadores());
         }
         if (parametroDeReporte.getTercero() == null) {
            parametroDeReporte.setTercero(new Terceros());
         }
         if (parametroDeReporte.getProceso() == null) {
            parametroDeReporte.setProceso(new Procesos());
         }
         if (parametroDeReporte.getAsociacion() == null) {
            parametroDeReporte.setAsociacion(new Asociaciones());
         }
         if (parametroDeReporte.getEmpresa() == null) {
            parametroDeReporte.setEmpresa(new Empresas());
         }

         return parametroDeReporte;
      } catch (Exception e) {
         log.warn("Error getParametroDeInforme : " + e);
         return null;
      }
   }

   public void setParametroDeInforme(ParametrosReportes parametroDeReporte) {
      this.parametroDeReporte = parametroDeReporte;
   }

   public List<Inforeportes> getListaIR() {
      try {
         if (listaIR == null) {
            listaIR = administrarNReportesNomina.listInforeportesUsuario();
         }
         return listaIR;
      } catch (Exception e) {
         log.warn("Error getListInforeportesUsuario : " + e);
         return null;
      }
   }

   public void setListaIR(List<Inforeportes> listaIR) {
      this.listaIR = listaIR;
   }

   public List<Inforeportes> getFiltrarListIRU() {
      return filtrarListIRU;
   }

   public void setFiltrarListIRU(List<Inforeportes> filtrarListIRU) {
      this.filtrarListIRU = filtrarListIRU;
   }

   public Inforeportes getReporteSeleccionadoLOV() {
      return reporteSeleccionadoLOV;
   }

   public void setReporteSeleccionadoLOV(Inforeportes inforreporteSeleccionadoLov) {
      this.reporteSeleccionadoLOV = inforreporteSeleccionadoLov;
   }

   public boolean isAceptar() {
      return aceptar;
   }

   public void setAceptar(boolean aceptar) {
      this.aceptar = aceptar;
   }

   public ParametrosReportes getParametroModificacion() {
      return parametroModificacion;
   }

   public void setParametroModificacion(ParametrosReportes parametroModificacion) {
      this.parametroModificacion = parametroModificacion;
   }

   public String getReporteGenerar() {
      if (posicionReporte != -1) {
         reporteGenerar = listaIR.get(posicionReporte).getNombre();
      }
      return reporteGenerar;
   }

   public void setReporteGenerar(String reporteGenerar) {
      this.reporteGenerar = reporteGenerar;
   }

   public String getRequisitosReporte() {
      return requisitosReporte;
   }

   public void setRequisitosReporte(String requisitosReporte) {
      this.requisitosReporte = requisitosReporte;
   }

   public List<Empleados> getLovEmpleados() {
      return lovEmpleados;
   }

   public void setLovEmpleados(List<Empleados> listEmpleados) {
      this.lovEmpleados = listEmpleados;
   }

   public List<Empresas> getLovEmpresas() {
      if (lovEmpresas == null) {
         lovEmpresas = administrarNReportesNomina.listEmpresas();
      }
      return lovEmpresas;
   }

   public void setLovEmpresas(List<Empresas> listEmpresas) {
      this.lovEmpresas = listEmpresas;
   }

   public List<GruposConceptos> getLovGruposConceptos() {
      return lovGruposConceptos;
   }

   public void setLovGruposConceptos(List<GruposConceptos> listGruposConceptos) {
      this.lovGruposConceptos = listGruposConceptos;
   }

   public List<UbicacionesGeograficas> getLovUbicacionesGeograficas() {
      return lovUbicacionesGeograficas;
   }

   public void setLovUbicacionesGeograficas(List<UbicacionesGeograficas> listUbicacionesGeograficas) {
      this.lovUbicacionesGeograficas = listUbicacionesGeograficas;
   }

   public List<TiposAsociaciones> getLovTiposAsociaciones() {
      if (lovTiposAsociaciones == null) {
         lovTiposAsociaciones = administrarNReportesNomina.listTiposAsociaciones();
      }
      return lovTiposAsociaciones;
   }

   public void setLovTiposAsociaciones(List<TiposAsociaciones> listTiposAsociaciones) {
      this.lovTiposAsociaciones = listTiposAsociaciones;
   }

   public List<Estructuras> getLovEstructuras() {
      return lovEstructuras;
   }

   public void setLovEstructuras(List<Estructuras> listEstructuras) {
      this.lovEstructuras = listEstructuras;
   }

   public List<TiposTrabajadores> getLovTiposTrabajadores() {
      return lovTiposTrabajadores;
   }

   public void setLovTiposTrabajadores(List<TiposTrabajadores> listTiposTrabajadores) {
      this.lovTiposTrabajadores = listTiposTrabajadores;
   }

   public List<Terceros> getLovTerceros() {
      return lovTerceros;
   }

   public void setLovTerceros(List<Terceros> listTerceros) {
      this.lovTerceros = listTerceros;
   }

   public List<Procesos> getLovProcesos() {
      return lovProcesos;
   }

   public void setLovProcesos(List<Procesos> listProcesos) {
      this.lovProcesos = listProcesos;
   }

   public List<Asociaciones> getLovAsociaciones() {
      return lovAsociaciones;
   }

   public void setLovAsociaciones(List<Asociaciones> listAsociaciones) {
      this.lovAsociaciones = listAsociaciones;
   }

   public Empleados getEmpleadoSeleccionado() {
      return empleadoSeleccionado;
   }

   public void setEmpleadoSeleccionado(Empleados empleadoSeleccionado) {
      this.empleadoSeleccionado = empleadoSeleccionado;
   }

   public Empresas getEmpresaSeleccionada() {
      return empresaSeleccionada;
   }

   public void setEmpresaSeleccionada(Empresas empresaSeleccionada) {
      this.empresaSeleccionada = empresaSeleccionada;
   }

   public GruposConceptos getGrupoCSeleccionado() {
      return grupoCSeleccionado;
   }

   public void setGrupoCSeleccionado(GruposConceptos grupoCSeleccionado) {
      this.grupoCSeleccionado = grupoCSeleccionado;
   }

   public Estructuras getEstructuraSeleccionada() {
      return estructuraSeleccionada;
   }

   public void setEstructuraSeleccionada(Estructuras estructuraSeleccionada) {
      this.estructuraSeleccionada = estructuraSeleccionada;
   }

   public TiposTrabajadores getTipoTSeleccionado() {
      return tipoTSeleccionado;
   }

   public void setTipoTSeleccionado(TiposTrabajadores tipoTSeleccionado) {
      this.tipoTSeleccionado = tipoTSeleccionado;
   }

   public Terceros getTerceroSeleccionado() {
      return terceroSeleccionado;
   }

   public void setTerceroSeleccionado(Terceros terceroSeleccionado) {
      this.terceroSeleccionado = terceroSeleccionado;
   }

   public Procesos getProcesoSeleccionado() {
      return procesoSeleccionado;
   }

   public void setProcesoSeleccionado(Procesos procesoSeleccionado) {
      this.procesoSeleccionado = procesoSeleccionado;
   }

   public Asociaciones getAsociacionSeleccionado() {
      return asociacionSeleccionado;
   }

   public void setAsociacionSeleccionado(Asociaciones asociacionSeleccionado) {
      this.asociacionSeleccionado = asociacionSeleccionado;
   }

   public List<Empleados> getLovEmpleadosFiltrar() {
      return lovEmpleadosFiltrar;
   }

   public void setLovEmpleadosFiltrar(List<Empleados> lovEmpleadosFiltrar) {
      this.lovEmpleadosFiltrar = lovEmpleadosFiltrar;
   }

   public List<Empresas> getLovEmpresasFiltrar() {
      return lovEmpresasFiltrar;
   }

   public void setLovEmpresasFiltrar(List<Empresas> lovEmpresasFiltrar) {
      this.lovEmpresasFiltrar = lovEmpresasFiltrar;
   }

   public List<GruposConceptos> getLovGruposConceptosFiltrar() {
      return lovGruposConceptosFiltrar;
   }

   public void setLovGruposConceptosFiltrar(List<GruposConceptos> lovGruposConceptosFiltrar) {
      this.lovGruposConceptosFiltrar = lovGruposConceptosFiltrar;
   }

   public List<UbicacionesGeograficas> getLovUbicacionesGeograficasFiltrar() {
      return lovUbicacionesGeograficasFiltrar;
   }

   public void setLovUbicacionesGeograficasFiltrar(List<UbicacionesGeograficas> lovUbicacionesGeograficasFiltrar) {
      this.lovUbicacionesGeograficasFiltrar = lovUbicacionesGeograficasFiltrar;
   }

   public List<TiposAsociaciones> getLovTiposAsociacionesFiltrar() {
      return lovTiposAsociacionesFiltrar;
   }

   public void setLovTiposAsociacionesFiltrar(List<TiposAsociaciones> lovTiposAsociacionesFiltrar) {
      this.lovTiposAsociacionesFiltrar = lovTiposAsociacionesFiltrar;
   }

   public List<Estructuras> getLovEstructurasFiltrar() {
      return lovEstructurasFiltrar;
   }

   public void setLovEstructurasFiltrar(List<Estructuras> lovEstructurasFiltrar) {
      this.lovEstructurasFiltrar = lovEstructurasFiltrar;
   }

   public List<Terceros> getLovTercerosFiltrar() {
      return lovTercerosFiltrar;
   }

   public void setLovTercerosFiltrar(List<Terceros> lovTercerosFiltrar) {
      this.lovTercerosFiltrar = lovTercerosFiltrar;
   }

   public List<Procesos> getLovProcesosFiltrar() {
      return lovProcesosFiltrar;
   }

   public void setLovProcesosFiltrar(List<Procesos> lovProcesosFiltrar) {
      this.lovProcesosFiltrar = lovProcesosFiltrar;
   }

   public List<Asociaciones> getLovAsociacionesFiltrar() {
      return lovAsociacionesFiltrar;
   }

   public void setLovAsociacionesFiltrar(List<Asociaciones> lovAsociacionesFiltrar) {
      this.lovAsociacionesFiltrar = lovAsociacionesFiltrar;
   }

   public UbicacionesGeograficas getUbicacionesGSeleccionado() {
      return ubicacionesGSeleccionado;
   }

   public void setUbicacionesGSeleccionado(UbicacionesGeograficas ubicacionesGSeleccionado) {
      this.ubicacionesGSeleccionado = ubicacionesGSeleccionado;
   }

   public TiposAsociaciones getTiposASeleccionado() {
      return tiposASeleccionado;
   }

   public void setTiposASeleccionado(TiposAsociaciones tiposASeleccionado) {
      this.tiposASeleccionado = tiposASeleccionado;
   }

   public List<TiposTrabajadores> getLovTiposTrabajadoresFiltrar() {
      return lovTiposTrabajadoresFiltrar;
   }

   public void setLovTiposTrabajadoresFiltrar(List<TiposTrabajadores> lovTiposTrabajadoresFiltrar) {
      this.lovTiposTrabajadoresFiltrar = lovTiposTrabajadoresFiltrar;
   }

   public String getAltoTabla() {
      return altoTabla;
   }

   public List<Inforeportes> getListaInfoReportesModificados() {
      return listaInfoReportesModificados;
   }

   public void setListaInfoReportesModificados(List<Inforeportes> listaInfoReportesModificados) {
      this.listaInfoReportesModificados = listaInfoReportesModificados;
   }

   public boolean isCambiosReporte() {
      return cambiosReporte;
   }

   public void setCambiosReporte(boolean cambiosReporte) {
      this.cambiosReporte = cambiosReporte;
   }

   public String getColor() {
      return color;
   }

   public void setColor(String color) {
      this.color = color;
   }

   public String getDecoracion() {
      return decoracion;
   }

   public void setDecoracion(String decoracion) {
      this.decoracion = decoracion;
   }

   public String getColor2() {
      return color2;
   }

   public void setColor2(String color) {
      this.color2 = color;
   }

   public String getDecoracion2() {
      return decoracion2;
   }

   public void setDecoracion2(String decoracion) {
      this.decoracion2 = decoracion;
   }

   public boolean isActivoMostrarTodos() {
      return activoMostrarTodos;
   }

   public void setActivoMostrarTodos(boolean activoMostrarTodos) {
      this.activoMostrarTodos = activoMostrarTodos;
   }

   public boolean isActivoBuscarReporte() {
      return activoBuscarReporte;
   }

   public void setActivoBuscarReporte(boolean activoBuscarReporte) {
      this.activoBuscarReporte = activoBuscarReporte;
   }

   public StreamedContent getReporte() {
      return reporte;
   }

   public String getCabezeraVisor() {
      return cabezeraVisor;
   }

   public String getPathReporteGenerado() {
      return pathReporteGenerado;
   }

   public String getInfoRegistro() {
      FacesContext c = FacesContext.getCurrentInstance();
      tabla = (DataTable) c.getViewRoot().findComponent("form:reportesNomina");
      infoRegistro = String.valueOf(tabla.getRowCount());
      return infoRegistro;
   }

   public String getInfoRegistroEmpleadoDesde() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioEmpleadoDesde:lovEmpleadoDesde");
      if (lovEmpleadosFiltrar != null) {
         if (lovEmpleadosFiltrar.size() == 1) {
            empleadoSeleccionado = lovEmpleadosFiltrar.get(0);
            aceptar = false;
            RequestContext.getCurrentInstance().execute("PF('lovEmpleadoDesde').unselectAllRows();PF('lovEmpleadoDesde').selectRow(0);");
         } else {
            empleadoSeleccionado = null;
            RequestContext.getCurrentInstance().execute("PF('lovEmpleadoDesde').unselectAllRows();");
         }
      } else {
         empleadoSeleccionado = null;
         aceptar = true;
      }
      infoRegistroEmpleadoDesde = String.valueOf(tabla.getRowCount());
      return infoRegistroEmpleadoDesde;
   }

   public String getInfoRegistroEmpleadoHasta() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioEmpleadoHasta:lovEmpleadoHasta");
      if (lovEmpleadosFiltrar != null) {
         if (lovEmpleadosFiltrar.size() == 1) {
            empleadoSeleccionado = lovEmpleadosFiltrar.get(0);
            aceptar = false;
            RequestContext.getCurrentInstance().execute("PF('lovEmpleadoHasta').unselectAllRows();PF('lovEmpleadoHasta').selectRow(0);");
         } else {
            empleadoSeleccionado = null;
            RequestContext.getCurrentInstance().execute("PF('lovEmpleadoHasta').unselectAllRows();");
         }
      } else {
         empleadoSeleccionado = null;
         aceptar = true;
      }
      infoRegistroEmpleadoHasta = String.valueOf(tabla.getRowCount());
      return infoRegistroEmpleadoHasta;
   }

   public String getInfoRegistroGrupoConcepto() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioGrupoConcepto:lovGruposConceptos");
      if (lovGruposConceptosFiltrar != null) {
         if (lovGruposConceptosFiltrar.size() == 1) {
            grupoCSeleccionado = lovGruposConceptosFiltrar.get(0);
            aceptar = false;
            RequestContext.getCurrentInstance().execute("PF('lovGruposConceptos').unselectAllRows();PF('lovGruposConceptos').selectRow(0);");
         } else {
            grupoCSeleccionado = null;
            RequestContext.getCurrentInstance().execute("PF('lovGruposConceptos').unselectAllRows();");
         }
      } else {
         grupoCSeleccionado = null;
         aceptar = true;
      }
      infoRegistroGrupoConcepto = String.valueOf(tabla.getRowCount());
      return infoRegistroGrupoConcepto;
   }

   public String getInfoRegistroUbicacion() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioUbicacion:lovUbicacionGeografica");
      if (lovUbicacionesGeograficasFiltrar != null) {
         if (lovUbicacionesGeograficasFiltrar.size() == 1) {
            ubicacionesGSeleccionado = lovUbicacionesGeograficasFiltrar.get(0);
            aceptar = false;
            RequestContext.getCurrentInstance().execute("PF('lovUbicacionGeografica').unselectAllRows();PF('lovUbicacionGeografica').selectRow(0);");
         } else {
            ubicacionesGSeleccionado = null;
            RequestContext.getCurrentInstance().execute("PF('lovUbicacionGeografica').unselectAllRows();");
         }
      } else {
         ubicacionesGSeleccionado = null;
         aceptar = true;
      }
      infoRegistroUbicacion = String.valueOf(tabla.getRowCount());
      return infoRegistroUbicacion;
   }

   public String getInfoRegistroTipoAsociacion() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioTipoAsociacion:lovTipoAsociacion");
      if (lovTiposAsociacionesFiltrar != null) {
         if (lovTiposAsociacionesFiltrar.size() == 1) {
            tiposASeleccionado = lovTiposAsociacionesFiltrar.get(0);
            aceptar = false;
            RequestContext.getCurrentInstance().execute("PF('lovTipoAsociacion').unselectAllRows();PF('lovTipoAsociacion').selectRow(0);");
         } else {
            tiposASeleccionado = null;
            RequestContext.getCurrentInstance().execute("PF('lovTipoAsociacion').unselectAllRows();");
         }
      } else {
         tiposASeleccionado = null;
         aceptar = true;
      }
      infoRegistroTipoAsociacion = String.valueOf(tabla.getRowCount());
      return infoRegistroTipoAsociacion;
   }

   public String getInfoRegistroEmpresa() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioEmpresa:lovEmpresa");
      if (lovEmpresasFiltrar != null) {
         if (lovEmpresasFiltrar.size() == 1) {
            empresaSeleccionada = lovEmpresasFiltrar.get(0);
            aceptar = false;
            RequestContext.getCurrentInstance().execute("PF('lovEmpresa').unselectAllRows();PF('lovEmpresa').selectRow(0);");
         } else {
            empresaSeleccionada = null;
            RequestContext.getCurrentInstance().execute("PF('lovEmpresa').unselectAllRows();");
         }
      } else {
         empresaSeleccionada = null;
         aceptar = true;
      }
      infoRegistroEmpresa = String.valueOf(tabla.getRowCount());
      return infoRegistroEmpresa;
   }

   public String getInfoRegistroAsociacion() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioAsociacion:lovAsociacion");
      if (lovAsociacionesFiltrar != null) {
         if (lovAsociacionesFiltrar.size() == 1) {
            asociacionSeleccionado = lovAsociacionesFiltrar.get(0);
            aceptar = false;
            RequestContext.getCurrentInstance().execute("PF('lovAsociacion').unselectAllRows();PF('lovAsociacion').selectRow(0);");
         } else {
            asociacionSeleccionado = null;
            RequestContext.getCurrentInstance().execute("PF('lovAsociacion').unselectAllRows();");
         }
      } else {
         asociacionSeleccionado = null;
         aceptar = true;
      }
      infoRegistroAsociacion = String.valueOf(tabla.getRowCount());
      return infoRegistroAsociacion;
   }

   public String getInfoRegistroTercero() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioTercero:lovTercero");
      if (lovTercerosFiltrar != null) {
         if (lovTercerosFiltrar.size() == 1) {
            terceroSeleccionado = lovTercerosFiltrar.get(0);
            aceptar = false;
            RequestContext.getCurrentInstance().execute("PF('lovTercero').unselectAllRows();PF('lovTercero').selectRow(0);");
         } else {
            terceroSeleccionado = null;
            RequestContext.getCurrentInstance().execute("PF('lovTercero').unselectAllRows();");
         }
      } else {
         terceroSeleccionado = null;
         aceptar = true;
      }
      infoRegistroTercero = String.valueOf(tabla.getRowCount());
      return infoRegistroTercero;
   }

   public String getInfoRegistroProceso() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioProceso:lovProceso");
      if (lovProcesosFiltrar != null) {
         if (lovProcesosFiltrar.size() == 1) {
            procesoSeleccionado = lovProcesosFiltrar.get(0);
            aceptar = false;
            RequestContext.getCurrentInstance().execute("PF('lovProceso').unselectAllRows();PF('lovProceso').selectRow(0);");
         } else {
            procesoSeleccionado = null;
            RequestContext.getCurrentInstance().execute("PF('lovProceso').unselectAllRows();");
         }
      } else {
         procesoSeleccionado = null;
         aceptar = true;
      }
      infoRegistroProceso = String.valueOf(tabla.getRowCount());
      return infoRegistroProceso;
   }

   public String getInfoRegistroTipoTrabajador() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioTipoTrabajador:lovTipoTrabajador");
      if (lovTiposTrabajadoresFiltrar != null) {
         if (lovTiposTrabajadoresFiltrar.size() == 1) {
            tipoTSeleccionado = lovTiposTrabajadoresFiltrar.get(0);
            aceptar = false;
            RequestContext.getCurrentInstance().execute("PF('lovTipoTrabajador').unselectAllRows();PF('lovTipoTrabajador').selectRow(0);");
         } else {
            tipoTSeleccionado = null;
            RequestContext.getCurrentInstance().execute("PF('lovTipoTrabajador').unselectAllRows();");
         }
      } else {
         tipoTSeleccionado = null;
         aceptar = true;
      }
      infoRegistroTipoTrabajador = String.valueOf(tabla.getRowCount());
      return infoRegistroTipoTrabajador;
   }

   public String getInfoRegistroEstructura() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioEstructura:lovEstructura");
      if (lovEstructurasFiltrar != null) {
         if (lovEstructurasFiltrar.size() == 1) {
            estructuraSeleccionada = lovEstructurasFiltrar.get(0);
            aceptar = false;
            RequestContext.getCurrentInstance().execute("PF('lovEstructura').unselectAllRows();PF('lovEstructura').selectRow(0);");
         } else {
            estructuraSeleccionada = null;
            RequestContext.getCurrentInstance().execute("PF('lovEstructura').unselectAllRows();");
         }
      } else {
         estructuraSeleccionada = null;
         aceptar = true;
      }
      infoRegistroEstructura = String.valueOf(tabla.getRowCount());
      return infoRegistroEstructura;
   }

   public String getInfoRegistroReportes() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioReportes:lovReportesDialogo");
      if (filtrarLovInforeportes != null) {
         if (filtrarLovInforeportes.size() == 1) {
            reporteSeleccionadoLOV = filtrarLovInforeportes.get(0);
            aceptar = false;
            RequestContext.getCurrentInstance().execute("PF('lovReportesDialogo').unselectAllRows();PF('lovReportesDialogo').selectRow(0);");
         } else {
            reporteSeleccionadoLOV = null;
            RequestContext.getCurrentInstance().execute("PF('lovReportesDialogo').unselectAllRows();");
         }
      } else {
         reporteSeleccionadoLOV = null;
         aceptar = true;
      }
      infoRegistroLovReportes = String.valueOf(tabla.getRowCount());
      return infoRegistroLovReportes;

   }

   public List<Inforeportes> getLovInforeportes() {
      if (lovInforeportes == null || lovInforeportes.isEmpty()) {
         lovInforeportes = administrarNReportesNomina.listInforeportesUsuario();
      }
      return lovInforeportes;
   }

   public void setLovInforeportes(List<Inforeportes> lovInforeportes) {
      this.lovInforeportes = lovInforeportes;
   }

   public List<Inforeportes> getFiltrarReportes() {
      return filtrarReportes;
   }

   public void setFiltrarReportes(List<Inforeportes> filtrarReportes) {
      this.filtrarReportes = filtrarReportes;
   }

   public List<Inforeportes> getFiltrarLovInforeportes() {
      return filtrarLovInforeportes;
   }

   public void setFiltrarLovInforeportes(List<Inforeportes> filtrarLovInforeportes) {
      this.filtrarLovInforeportes = filtrarLovInforeportes;
   }

   public Inforeportes getReporteSeleccionado() {
      return reporteSeleccionado;
   }

   public void setReporteSeleccionado(Inforeportes inforreporteSeleccionado) {
      this.reporteSeleccionado = inforreporteSeleccionado;
   }

   public boolean isActivarEnvio() {
      return activarEnvio;
   }

   public void setActivarEnvio(boolean activarEnvio) {
      this.activarEnvio = activarEnvio;
   }

   public boolean isActivarLov() {
      return activarLov;
   }

   public void setActivarLov(boolean activarLov) {
      this.activarLov = activarLov;
   }

   public ParametrosReportes getParametroFecha() {
      return parametroFecha;
   }

   public void setParametroFecha(ParametrosReportes parametroFecha) {
      this.parametroFecha = parametroFecha;
   }
}
