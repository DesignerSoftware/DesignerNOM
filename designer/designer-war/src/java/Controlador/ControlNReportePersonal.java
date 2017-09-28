package Controlador;

import Entidades.Aficiones;
import Entidades.Ciudades;
import Entidades.Deportes;
import Entidades.Empleados;
import Entidades.Empresas;
import Entidades.EstadosCiviles;
import Entidades.Estructuras;
import Entidades.Idiomas;
import Entidades.Inforeportes;
import Entidades.ParametrosReportes;
import Entidades.TiposTelefonos;
import Entidades.TiposTrabajadores;
import InterfaceAdministrar.AdministarReportesInterface;
import InterfaceAdministrar.AdministrarInforeportesInterface;
import InterfaceAdministrar.AdministrarNReportePersonalInterface;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
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
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@ManagedBean
@SessionScoped
public class ControlNReportePersonal implements Serializable {

   private static Logger log = Logger.getLogger(ControlNReportePersonal.class);

   @EJB
   AdministrarNReportePersonalInterface administrarNReportePersonal;
   @EJB
   AdministarReportesInterface administarReportes;
   @EJB
   AdministrarInforeportesInterface administrarInforeportes;
   //PARAMETROS REPORTES
   private ParametrosReportes parametroDeReporte;
   private ParametrosReportes parametroModificacion;
   private ParametrosReportes parametroFecha;
   //INFOREPORTES
   private List<Inforeportes> listaIR;
   private Inforeportes reporteSeleccionado;
   private List<Inforeportes> filtrarListInforeportesUsuario;
   private List<Inforeportes> listaInfoReportesModificados;
   //INFOREPORTES LOV
   private List<Inforeportes> lovInforeportes;
   private Inforeportes reporteSeleccionadoLov;
   private List<Inforeportes> filtrarLovInforeportes;
   //EMPLEADOS
   private List<Empleados> lovEmpleados;
   private Empleados empleadoSeleccionado;
   private List<Empleados> lovEmpleadosFiltrar;
   //EMPRESAS
   private List<Empresas> lovEmpresas;
   private Empresas empresaSeleccionada;
   private List<Empresas> lovEmpresasFiltrar;
   //ESTRUCTURAS
   private List<Estructuras> lovEstructuras;
   private Estructuras estructuraSeleccionada;
   private List<Estructuras> lovEstructurasFiltrar;
   //TIPOS TRABAJADORES
   private List<TiposTrabajadores> lovTiposTrabajadores;
   private TiposTrabajadores tipoTSeleccionado;
   private List<TiposTrabajadores> lovTiposTrabajadoresFiltrar;
   //ESTADOS CIVILES
   private List<EstadosCiviles> lovEstadosCiviles;
   private EstadosCiviles estadoCivilSeleccionado;
   private List<EstadosCiviles> lovEstadosCivilesFiltrar;
   //TIPOS TELEFONOS
   private List<TiposTelefonos> lovTiposTelefonos;
   private TiposTelefonos tipoTelefonoSeleccionado;
   private List<TiposTelefonos> lovTiposTelefonosFiltrar;
   //CIUDADES
   private List<Ciudades> lovCiudades;
   private Ciudades ciudadSeleccionada;
   private List<Ciudades> lovListCiudadesFiltrar;
   //DEPORTES
   private List<Deportes> lovDeportes;
   private Deportes deporteSeleccionado;
   private List<Deportes> lovDeportesFiltrar;
   //AFICIONES
   private List<Aficiones> lovAficiones;
   private Aficiones aficionSeleccionada;
   private List<Aficiones> lovAficionesFiltrar;
   //
   private List<Idiomas> lovIdiomas;
   private Idiomas idiomaSeleccionado;
   private List<Idiomas> lovIdiomasFiltrar;

   private ParametrosReportes nuevoParametroInforme;
   //
   private int casilla;
   private int posicionReporte;
   private String requisitosReporte;
   //INPUTS
   private InputText empleadoDesdeParametro, empleadoHastaParametro, estadoCivilParametro, tipoTelefonoParametro, estructuraParametro;
   private InputText ciudadParametro, deporteParametro, aficionParametro, idiomaParametro, tipoTrabajadorParametro, solicitudParametro;
   private InputText jefeDivisionParametro, rodamientoParametro, empresaParametro;

   private String reporteGenerar;
   private int bandera;
   private boolean aceptar;
   private Column codigoIR, reporteIR;
   private SelectCheckboxMenu tipoIR;
   //INFOREGISTRO
   private String infoRegistroEmpleadoDesde, infoRegistroEmpleadoHasta, infoRegistroEmpresa, infoRegistroEstructura;
   private String infoRegistroReportes, infoRegistroTipoTrabajador, infoRegistroEstadoCivil, infoRegistroTipoTelefono;
   private String infoRegistro, infoRegistroCiudad, infoRegistroDeporte, infoRegistroAficion, infoRegistroIdioma, infoRegistroJefe;
   //
   private String solicitud, estadocivil, tipotelefono, jefediv, estructura, empresa, tipoTrabajador, ciudad, deporte, aficiones, idioma, rodamiento;
   private int tipoLista;
   private boolean permitirIndex, cambiosReporte;
   //ALTO SCROLL TABLA
   private String altoTabla;
   //EXPORTAR REPORTE
   private StreamedContent file;
   //
   private String color, decoracion;
   private String color2, decoracion2;
   private int casillaInforReporte;
   private Date fechaDesde, fechaHasta;
   private BigDecimal emplDesde, emplHasta;
   private boolean activoMostrarTodos, activoBuscarReporte, activarEnvio;
   //EXPORTAR REPORTE
   private String pathReporteGenerado = null;
   private String nombreReporte, tipoReporte;
   //BANDERAS
   private boolean estadoReporte;
   private String resultadoReporte;
   //VISUALIZAR REPORTE PDF
   private StreamedContent reporte;
   private String cabezeraVisor;
   //
   private DataTable tabla;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<>();
   private Map<BigInteger, Object> mapTipos = new LinkedHashMap<>();
   private ExternalContext externalContext;
   private String userAgent;
   private boolean activarLov;
   private ListasRecurrentes listasRecurrentes;

   public ControlNReportePersonal() {
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
      reporteSeleccionado = null;
      cambiosReporte = true;
      listaInfoReportesModificados = new ArrayList<>();
      parametroDeReporte = null;
      bandera = 0;
      aceptar = true;
      casilla = -1;
      parametroModificacion = new ParametrosReportes();
      parametroFecha = new ParametrosReportes();
      reporteGenerar = "";
      requisitosReporte = "";
      altoTabla = "140";
      listaIR = null;
      tipoLista = 0;
      posicionReporte = -1;
      //
      lovEmpleados = null;
      lovEmpresas = null;
      lovEstructuras = null;
      lovTiposTrabajadores = null;
      lovAficiones = null;
      lovCiudades = null;
      lovDeportes = null;
      lovEstadosCiviles = null;
      lovIdiomas = null;
      lovTiposTelefonos = null;
      //
      empleadoSeleccionado = new Empleados();
      empresaSeleccionada = new Empresas();
      estructuraSeleccionada = new Estructuras();
      tipoTSeleccionado = new TiposTrabajadores();
      aficionSeleccionada = new Aficiones();
      ciudadSeleccionada = new Ciudades();
      deporteSeleccionado = new Deportes();
      estadoCivilSeleccionado = new EstadosCiviles();
      idiomaSeleccionado = new Idiomas();
      tipoTelefonoSeleccionado = new TiposTelefonos();
      reporteSeleccionadoLov = null;
      //
      permitirIndex = true;
      cabezeraVisor = null;
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
      String pagActual = "nreportepersonal";
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
      lovAficiones = null;
      lovCiudades = null;
      lovDeportes = null;
      lovEmpleados = null;
      lovEmpresas = null;
      lovEstadosCiviles = null;
      lovEstructuras = null;
      lovIdiomas = null;
      lovInforeportes = null;
      lovTiposTelefonos = null;
      lovTiposTrabajadores = null;
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
         externalContext = x.getExternalContext();
         userAgent = externalContext.getRequestHeaderMap().get("User-Agent");
         administrarNReportePersonal.obtenerConexion(ses.getId());
         administarReportes.obtenerConexion(ses.getId());
         administrarInforeportes.obtenerConexion(ses.getId());
      } catch (Exception e) {
         log.error("Error postconstruct " + this.getClass().getName() + ":  ", e);
         log.info("Causa: " + e.getMessage());
      }
   }

   public void iniciarPagina() {
      activoMostrarTodos = true;
      activoBuscarReporte = false;
      activarEnvio = true;
      getListaIR();
   }
//

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
               if (parametroDeReporte.getEstadocivil().getSecuencia() == null) {
                  parametroDeReporte.setEstadocivil(null);
               }
               if (parametroDeReporte.getTipotelefono().getSecuencia() == null) {
                  parametroDeReporte.setTipotelefono(null);
               }
               if (parametroDeReporte.getLocalizacion().getSecuencia() == null) {
                  parametroDeReporte.setLocalizacion(null);
               }
               if (parametroDeReporte.getTipotrabajador().getSecuencia() == null) {
                  parametroDeReporte.setTipotrabajador(null);
               }
               if (parametroDeReporte.getCiudad().getSecuencia() == null) {
                  parametroDeReporte.setCiudad(null);
               }
               if (parametroDeReporte.getDeporte().getSecuencia() == null) {
                  parametroDeReporte.setDeporte(null);
               }
               if (parametroDeReporte.getAficion().getSecuencia() == null) {
                  parametroDeReporte.setAficion(null);
               }
               if (parametroDeReporte.getIdioma().getSecuencia() == null) {
                  parametroDeReporte.setIdioma(null);
               }
               if (parametroDeReporte.getEmpresa().getSecuencia() == null) {
                  parametroDeReporte.setEmpresa(null);
               }
               if (parametroDeReporte.getNombregerente().getSecuencia() == null) {
                  parametroDeReporte.setNombregerente(null);
               }
               administrarNReportePersonal.modificarParametrosReportes(parametroDeReporte);
            }
            cambiosReporte = true;
            FacesMessage msg = new FacesMessage("Información", "Los datos se guardaron con Éxito.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         } else if (parametroDeReporte.getFechadesde().after(parametroDeReporte.getFechahasta())) {
            fechaDesde = parametroDeReporte.getFechadesde();
            fechaHasta = parametroDeReporte.getFechahasta();
            parametroDeReporte.setFechadesde(fechaDesde);
            parametroDeReporte.setFechahasta(fechaHasta);
            RequestContext.getCurrentInstance().update("formParametros");
            RequestContext.getCurrentInstance().execute("PF('errorFechas').show()");
         }
         if (!listaInfoReportesModificados.isEmpty()) {
            if (!mapTipos.isEmpty()) {
               listaInfoReportesModificados.get(0).setTipo((String) mapTipos.get(listaInfoReportesModificados.get(0).getSecuencia()));
            }
            administrarNReportePersonal.guardarCambiosInfoReportes(listaInfoReportesModificados);
            listaInfoReportesModificados.clear();
         }
      } catch (Exception e) {
         log.warn("Error en guardar Cambios Controlador : " + e.toString());
         FacesMessage msg = new FacesMessage("Información", "ha ocurrido un error en el guardado, intente nuevamente");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      }
   }
//
//    public void modificacionTipoReporte(Inforeportes reporte) {
//        log.info(this.getClass().getName() + ".modificacionTipoReporte()");
//        reporteSeleccionado = reporte;
//        log.info("reporteSeleccionado: " + reporteSeleccionado);
//        log.info("reporteSeleccionado.getNombre(): " + reporteSeleccionado.getNombre());
//        log.info("Tipo reporteSeleccionado: " + reporteSeleccionado.getTipo());
//        cambiosReporte = false;
//        if (listaInfoReportesModificados.isEmpty()) {
//            listaInfoReportesModificados.add(reporteSeleccionado);
//        } else if ((!listaInfoReportesModificados.isEmpty()) && (!listaInfoReportesModificados.contains(reporteSeleccionado))) {
//            log.info("Ingrese al else if");
//            listaInfoReportesModificados.add(reporteSeleccionado);
//        } else {
//            log.info("Ingrese al else");
//            int posicion = listaInfoReportesModificados.indexOf(reporteSeleccionado);
//            listaInfoReportesModificados.set(posicion, reporteSeleccionado);
//        }
//        RequestContext.getCurrentInstance().update("form:ACEPTAR");
//        log.info("Saliendo del metodo Tipo reporteSeleccionado: " + reporteSeleccionado.getTipo());
//    }

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

   public void editarCelda() {
      if (casilla >= 1) {
         if (casilla == 1) {
            RequestContext.getCurrentInstance().update("formDialogos:editarFechaDesde");
            RequestContext.getCurrentInstance().execute("PF('editarFechaDesde').show()");
         }
         if (casilla == 2) {
            RequestContext.getCurrentInstance().update("formDialogos:empleadoDesde");
            RequestContext.getCurrentInstance().execute("PF('empleadoDesde').show()");
         }
         if (casilla == 3) {
            RequestContext.getCurrentInstance().update("formDialogos:solicitud");
            RequestContext.getCurrentInstance().execute("PF('solicitud').show()");
         }
         if (casilla == 4) {
            RequestContext.getCurrentInstance().update("formDialogos:estadoCivil");
            RequestContext.getCurrentInstance().execute("PF('estadoCivil').show()");
         }
         if (casilla == 5) {
            RequestContext.getCurrentInstance().update("formDialogos:tipoTelefono");
            RequestContext.getCurrentInstance().execute("PF('tipoTelefono').show()");
         }
         if (casilla == 6) {
            RequestContext.getCurrentInstance().update("formDialogos:jefeDivision");
            RequestContext.getCurrentInstance().execute("PF('jefeDivision').show()");
         }
         if (casilla == 7) {
            RequestContext.getCurrentInstance().update("formDialogos:rodamiento");
            RequestContext.getCurrentInstance().execute("PF('rodamiento').show()");
         }
         if (casilla == 8) {
            RequestContext.getCurrentInstance().update("formDialogos:editarFechaHasta");
            RequestContext.getCurrentInstance().execute("PF('editarFechaHasta').show()");
         }
         if (casilla == 9) {
            RequestContext.getCurrentInstance().update("formDialogos:empleadoHasta");
            RequestContext.getCurrentInstance().execute("PF('empleadoHasta').show()");
         }
         if (casilla == 10) {
            RequestContext.getCurrentInstance().update("formDialogos:estructura");
            RequestContext.getCurrentInstance().execute("PF('estructura').show()");
         }
         if (casilla == 11) {
            RequestContext.getCurrentInstance().update("formDialogos:tipoTrabajador");
            RequestContext.getCurrentInstance().execute("PF('tipoTrabajador').show()");
         }
         if (casilla == 12) {
            RequestContext.getCurrentInstance().update("formDialogos:ciudad");
            RequestContext.getCurrentInstance().execute("PF('ciudad').show()");
         }
         if (casilla == 13) {
            RequestContext.getCurrentInstance().update("formDialogos:deporte");
            RequestContext.getCurrentInstance().execute("PF('deporte').show()");
         }
         if (casilla == 14) {
            RequestContext.getCurrentInstance().update("formDialogos:aficion");
            RequestContext.getCurrentInstance().execute("PF('aficion').show()");
         }
         if (casilla == 15) {
            RequestContext.getCurrentInstance().update("formDialogos:idioma");
            RequestContext.getCurrentInstance().execute("PF('idioma').show()");
         }
         if (casilla == 16) {
            RequestContext.getCurrentInstance().update("formDialogos:empresa");
            RequestContext.getCurrentInstance().execute("PF('empresa').show()");
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
         cargarEmpleados();
         RequestContext.getCurrentInstance().update("form:EmpleadoDesdeDialogo");
         RequestContext.getCurrentInstance().execute("PF('EmpleadoDesdeDialogo').show()");
         contarRegistrosEmpleadoD();
      }
      if (casilla == 4) {
         lovEstadosCiviles = null;
         cargarEstadosCiviles();
         RequestContext.getCurrentInstance().update("form:EstadoCivilDialogo");
         RequestContext.getCurrentInstance().execute("PF('EstadoCivilDialogo').show()");
         contarRegistrosEstadoCivil();
      }
      if (casilla == 5) {
         lovTiposTelefonos = null;
         cargarTiposTelefonos();
         RequestContext.getCurrentInstance().update("form:TipoTelefonoDialogo");
         RequestContext.getCurrentInstance().execute("PF('TipoTelefonoDialogo').show()");
         contarRegistrosTipoTelefono();
      }
      if (casilla == 6) {
         lovEmpleados = null;
         cargarEmpleados();
         RequestContext.getCurrentInstance().update("form:JefeDivisionDialogo");
         RequestContext.getCurrentInstance().execute("PF('JefeDivisionDialogo').show()");
         contarRegistrosJefe();
      }
      if (casilla == 9) {
         lovEmpleados = null;
         cargarEmpleados();
         RequestContext.getCurrentInstance().update("form:EmpleadoHastaDialogo");
         RequestContext.getCurrentInstance().execute("PF('EmpleadoHastaDialogo').show()");
         contarRegistrosEmpleadoH();
      }
      if (casilla == 10) {
         lovEstructuras = null;
         cargarEstructuras();
         RequestContext.getCurrentInstance().update("form:EstructuraDialogo");
         RequestContext.getCurrentInstance().execute("PF('EstructuraDialogo').show()");
         contarRegistrosEstructura();
      }
      if (casilla == 11) {
         lovTiposTrabajadores = null;
         cargarTiposTrabajadores();
         RequestContext.getCurrentInstance().update("form:TipoTrabajadorDialogo");
         RequestContext.getCurrentInstance().execute("PF('TipoTrabajadorDialogo').show()");
         contarRegistrosTipoTrabajador();
      }
      if (casilla == 12) {
         lovCiudades = null;
         cargarCiudades();
         RequestContext.getCurrentInstance().update("form:CiudadDialogo");
         RequestContext.getCurrentInstance().execute("PF('CiudadDialogo').show()");
         contarRegistrosCiudad();
      }
      if (casilla == 13) {
         lovDeportes = null;
         cargarDeporte();
         RequestContext.getCurrentInstance().update("form:DeportesDialogo");
         RequestContext.getCurrentInstance().execute("PF('DeportesDialogo').show()");
         contarRegistrosDeporte();
      }
      if (casilla == 14) {
         lovAficiones = null;
         cargarAficion();
         RequestContext.getCurrentInstance().update("form:AficionesDialogo");
         RequestContext.getCurrentInstance().execute("PF('AficionesDialogo').show()");
         contarRegistrosAficion();
      }
      if (casilla == 15) {
         lovIdiomas = null;
         cargarIdioma();
         RequestContext.getCurrentInstance().update("form:IdiomasDialogo");
         RequestContext.getCurrentInstance().execute("PF('IdiomasDialogo').show()");
         contarRegistrosIdioma();
      }
      if (casilla == 16) {
         lovEmpresas = null;
         cargarEmpresas();
         RequestContext.getCurrentInstance().update("form:EmpresaDialogo");
         RequestContext.getCurrentInstance().execute("PF('EmpresaDialogo').show()");
         contarRegistrosEmpresa();
      }
   }

   public void activarCtrlF11() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 0) {
         altoTabla = "120";
         codigoIR = (Column) c.getViewRoot().findComponent("form:reportesPersonal:codigoIR");
         codigoIR.setFilterStyle("width: 85% !important");
         reporteIR = (Column) c.getViewRoot().findComponent("form:reportesPersonal:reporteIR");
         reporteIR.setFilterStyle("width: 85% !important");
         tipoIR = (SelectCheckboxMenu) c.getViewRoot().findComponent("form:reportesPersonal:tipo");
         tipoIR.setRendered(true);
         RequestContext.getCurrentInstance().update("form:reportesPersonal:tipo");
         RequestContext.getCurrentInstance().update("form:reportesPersonal");
         bandera = 1;
      } else if (bandera == 1) {
         cerrarFiltrado();
         defaultPropiedadesParametrosReporte();
      }
   }

   public void cerrarFiltrado() {
      FacesContext c = FacesContext.getCurrentInstance();
      altoTabla = "140";
      codigoIR = (Column) c.getViewRoot().findComponent("form:reportesPersonal:codigoIR");
      codigoIR.setFilterStyle("display: none; visibility: hidden;");
      reporteIR = (Column) c.getViewRoot().findComponent("form:reportesPersonal:reporteIR");
      reporteIR.setFilterStyle("display: none; visibility: hidden;");
      tipoIR = (SelectCheckboxMenu) c.getViewRoot().findComponent("form:reportesPersonal:tipo");
      tipoIR.setRendered(false);
      tipoIR.setSelectedValues(null);
      tipoIR.resetValue();
      RequestContext.getCurrentInstance().update("form:reportesPersonal:tipo");
      RequestContext.getCurrentInstance().update("form:reportesPersonal");
      bandera = 0;
      tipoLista = 0;
      filtrarListInforeportesUsuario = null;
   }

   public void salir() {
      limpiarListasValor();
      if (bandera == 1) {
         cerrarFiltrado();
      }
      listaIR = null;
      parametroDeReporte = null;
      parametroModificacion = null;
      lovEmpleados = null;
      lovEmpresas = null;
      lovEstructuras = null;
      lovTiposTrabajadores = null;
      lovAficiones = null;
      lovCiudades = null;
      lovDeportes = null;
      lovTiposTelefonos = null;
      lovIdiomas = null;
      lovEstadosCiviles = null;
      casilla = -1;
      listaInfoReportesModificados.clear();
      mapTipos.clear();
      cambiosReporte = true;
      reporteSeleccionado = null;
      reporteSeleccionadoLov = null;
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
      mapTipos.clear();
      cambiosReporte = true;
      getParametroDeReporte();
      getListaIR();
      activoMostrarTodos = true;
      activoBuscarReporte = false;
      reporteSeleccionado = null;
      RequestContext.getCurrentInstance().update("form:MOSTRARTODOS");
      RequestContext.getCurrentInstance().update("form:BUSCARREPORTE");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      RequestContext.getCurrentInstance().update("form:ENVIOCORREO");
      RequestContext.getCurrentInstance().update("form:reportesPersonal");
      RequestContext.getCurrentInstance().update("formParametros:fechaDesdeParametro");
      RequestContext.getCurrentInstance().update("formParametros:empleadoDesdeParametro");
      RequestContext.getCurrentInstance().update("formParametros:solicitudParametro");
      RequestContext.getCurrentInstance().update("formParametros:estadoCivilParametro");
      RequestContext.getCurrentInstance().update("formParametros:tipoTelefonoParametro");
      RequestContext.getCurrentInstance().update("formParametros:jefeDivisionParametro");
      RequestContext.getCurrentInstance().update("formParametros:rodamientoParametro");
      RequestContext.getCurrentInstance().update("formParametros:fondoCumpleParametro");
      RequestContext.getCurrentInstance().update("formParametros:fechaHastaParametro");
      RequestContext.getCurrentInstance().update("formParametros:empleadoHastaParametro");
      RequestContext.getCurrentInstance().update("formParametros:estructuraParametro");
      RequestContext.getCurrentInstance().update("formParametros:tipoTrabajadorParametro");
      RequestContext.getCurrentInstance().update("formParametros:ciudadParametro");
      RequestContext.getCurrentInstance().update("formParametros:deporteParametro");
      RequestContext.getCurrentInstance().update("formParametros:aficionParametro");
      RequestContext.getCurrentInstance().update("formParametros:idiomaParametro");
      RequestContext.getCurrentInstance().update("formParametros:personalParametro");
      RequestContext.getCurrentInstance().update("formParametros:empresaParametro");
   }

   public void seleccionRegistro() {
      try {
         log.info("reporteSeleccionado : " + reporteSeleccionado.getNombrereporte());
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
            if (!empleadoDesdeParametro.getStyle().contains(" color: red;")) {
               empleadoDesdeParametro.setStyle(empleadoDesdeParametro.getStyle() + " color: red;");
            }
         } else {
            try {
               if (empleadoDesdeParametro.getStyle().contains(" color: red;")) {
                  empleadoDesdeParametro.setStyle(empleadoDesdeParametro.getStyle().replace(" color: red;", ""));
               }
            } catch (Exception e) {
               log.warn("Error en seleccionRegistro : " + e.getMessage());
               log.warn("Error en seleccionRegistro : " + e.getCause());
            }
         }
         RequestContext.getCurrentInstance().update("formParametros:empleadoDesdeParametro");
         if (reporteSeleccionado.getEmhasta().equals("SI")) {
            empleadoHastaParametro = (InputText) FacesContext.getCurrentInstance().getViewRoot().findComponent("formParametros:empleadoHastaParametro");
            empleadoHastaParametro.setStyle(empleadoHastaParametro.getStyle() + "color: red;");
            RequestContext.getCurrentInstance().update("formParametros:empleadoHastaParametro");
         }

         if (reporteSeleccionado.getLocalizacion().equals("SI")) {
            solicitudParametro = (InputText) FacesContext.getCurrentInstance().getViewRoot().findComponent("formParametros:solicitudParametro");
            solicitudParametro.setStyle(solicitudParametro.getStyle() + "color: red;");
            RequestContext.getCurrentInstance().update("formParametros:solicitudParametro");
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

         if (reporteSeleccionado.getEstadocivil().equals("SI")) {
            estadoCivilParametro = (InputText) FacesContext.getCurrentInstance().getViewRoot().findComponent("formParametros:estadoCivilParametro");
            estadoCivilParametro.setStyle(estadoCivilParametro.getStyle() + "color: red;");
            RequestContext.getCurrentInstance().update("formParametros:estadoCivilParametro");
         }

         if (reporteSeleccionado.getTipotelefono().equals("SI")) {
            tipoTelefonoParametro = (InputText) FacesContext.getCurrentInstance().getViewRoot().findComponent("formParametros:tipoTelefonoParametro");
            tipoTelefonoParametro.setStyle(tipoTrabajadorParametro.getStyle() + "color: red;");
            RequestContext.getCurrentInstance().update("formParametros:tipoTelefonoParametro");
         }

         if (reporteSeleccionado.getJefedivision().equals("SI")) {
            jefeDivisionParametro = (InputText) FacesContext.getCurrentInstance().getViewRoot().findComponent("formParametros:jefeDivisionParametro");
            jefeDivisionParametro.setStyle(jefeDivisionParametro.getStyle() + "color: red;");
            RequestContext.getCurrentInstance().update("formParametros:jefeDivisionParametro");
         }

         if (reporteSeleccionado.getCiudad().equals("SI")) {
            ciudadParametro = (InputText) FacesContext.getCurrentInstance().getViewRoot().findComponent("formParametros:ciudadParametro");
            ciudadParametro.setStyle(ciudadParametro.getStyle() + "color: red;");
            RequestContext.getCurrentInstance().update("formParametros:ciudadParametro");
         }

         if (reporteSeleccionado.getDeporte().equals("SI")) {
            deporteParametro = (InputText) FacesContext.getCurrentInstance().getViewRoot().findComponent("formParametros:deporteParametro");
            deporteParametro.setStyle(deporteParametro.getStyle() + "color: red;");
            RequestContext.getCurrentInstance().update("formParametros:deporteParametro");
         }

         if (reporteSeleccionado.getAficion().equals("SI")) {
            aficionParametro = (InputText) FacesContext.getCurrentInstance().getViewRoot().findComponent("formParametros:aficionParametro");
            aficionParametro.setStyle(aficionParametro.getStyle() + "color: red;");
            RequestContext.getCurrentInstance().update("formParametros:aficionParametro");
         }

         if (reporteSeleccionado.getIdioma().equals("SI")) {
            idiomaParametro = (InputText) FacesContext.getCurrentInstance().getViewRoot().findComponent("formParametros:idiomaParametro");
            idiomaParametro.setStyle(idiomaParametro.getStyle() + "color: red;");
            RequestContext.getCurrentInstance().update("formParametros:idiomaParametro");
         }
         RequestContext.getCurrentInstance().update("formParametros");
      } catch (Exception ex) {
         log.warn("Error en seleccion Registro .Error : " + ex.getMessage());
      }
   }

   public void requisitosParaReporte() {
      RequestContext context = RequestContext.getCurrentInstance();
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
      if (reporteSeleccionado.getSolicitud().equals("SI")) {
         requisitosReporte = requisitosReporte + "- Observaciones -";
      }
      if (reporteSeleccionado.getTipotelefono().equals("SI")) {
         requisitosReporte = requisitosReporte + "- Tipo Telefono -";
      }
      if (reporteSeleccionado.getJefedivision().equals("SI")) {
         requisitosReporte = requisitosReporte + "- Jefe División RH-";
      }
      if (reporteSeleccionado.getRodamiento().equals("SI")) {
         requisitosReporte = requisitosReporte + "- Rodamiento -";
      }
      if (reporteSeleccionado.getLocalizacion().equals("SI")) {
         requisitosReporte = requisitosReporte + "- Estructura -";
      }
      if (reporteSeleccionado.getTrabajador().equals("SI")) {
         requisitosReporte = requisitosReporte + "- Tipo Trabajador -";
      }
      if (reporteSeleccionado.getCiudad().equals("SI")) {
         requisitosReporte = requisitosReporte + "- Ciudad -";
      }
      if (reporteSeleccionado.getDeporte().equals("SI")) {
         requisitosReporte = requisitosReporte + "- Deporte -";
      }
      if (reporteSeleccionado.getAficion().equals("SI")) {
         requisitosReporte = requisitosReporte + "- Afición -";
      }
      if (reporteSeleccionado.getIdioma().equals("SI")) {
         requisitosReporte = requisitosReporte + "- Idioma -";
      }

      if (!requisitosReporte.isEmpty()) {
         RequestContext.getCurrentInstance().update("formDialogos:requisitosReporte");
         RequestContext.getCurrentInstance().execute("PF('requisitosReporte').show()");
      }
   }

   public void dispararDialogoGuardarCambios() {
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
   }

   public void guardarYSalir() {
      guardarCambios();
      salir();
   }

   public void modificarParametroInforme() {
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

   public void posicionCelda(int i) {
      casilla = i;
      if (permitirIndex == true) {
//            casillaInforReporte = -1;
         switch (casilla) {
            case 1:
               deshabilitarBotonLov();
               fechaDesde = parametroDeReporte.getFechadesde();
               break;
            case 2:
               habilitarBotonLov();
               emplDesde = parametroDeReporte.getCodigoempleadodesde();
               break;
            case 3:
               deshabilitarBotonLov();
               solicitud = parametroDeReporte.getObservaciones();
               break;
            case 4:
               habilitarBotonLov();
               estadocivil = parametroDeReporte.getEstadocivil().getDescripcion();
               break;
            case 5:
               habilitarBotonLov();
               tipotelefono = parametroDeReporte.getTipotelefono().getNombre();
               break;
            case 6:
               habilitarBotonLov();
               jefediv = parametroDeReporte.getNombregerente().getNombreCompleto();
               break;
            case 7:
               deshabilitarBotonLov();
               rodamiento = parametroDeReporte.getRodamineto();
               break;
            case 8:
               deshabilitarBotonLov();
               fechaHasta = parametroDeReporte.getFechahasta();
               break;
            case 9:
               habilitarBotonLov();
               emplHasta = parametroDeReporte.getCodigoempleadohasta();
               break;
            case 10:
               habilitarBotonLov();
               estructura = parametroDeReporte.getLocalizacion().getNombre();
               break;
            case 11:
               habilitarBotonLov();
               tipoTrabajador = parametroDeReporte.getTipotrabajador().getNombre();
               break;
            case 12:
               habilitarBotonLov();
               ciudad = parametroDeReporte.getCiudad().getNombre();
               break;
            case 13:
               habilitarBotonLov();
               deporte = parametroDeReporte.getDeporte().getNombre();
               break;
            case 14:
               habilitarBotonLov();
               aficiones = parametroDeReporte.getAficion().getDescripcion();
               break;
            case 15:
               habilitarBotonLov();
               idioma = parametroDeReporte.getIdioma().getNombre();
               break;
            case 16:
               habilitarBotonLov();
               empresa = parametroDeReporte.getEmpresa().getNombre();
               break;
            default:
               break;
         }
      }
   }

   public void autocompletarGeneral(String campoConfirmar, String valorConfirmar) {
      RequestContext context = RequestContext.getCurrentInstance();
      int indiceUnicoElemento = -1;
      int coincidencias = 0;
      if (campoConfirmar.equalsIgnoreCase("ESTADOCIVIL")) {
         if (!valorConfirmar.isEmpty()) {
            parametroDeReporte.getEstadocivil().setDescripcion(estadocivil);
            for (int i = 0; i < lovEstadosCiviles.size(); i++) {
               if (lovEstadosCiviles.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               parametroDeReporte.setEstadocivil(lovEstadosCiviles.get(indiceUnicoElemento));
               parametroModificacion = parametroDeReporte;
               lovEstadosCiviles.clear();
               getLovEstadosCiviles();
               cambiosReporte = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            } else {
               permitirIndex = false;
               RequestContext.getCurrentInstance().update("form:EstadoCivilDialogo");
               RequestContext.getCurrentInstance().execute("PF('EstadoCivilDialogo').show()");
            }
         } else {
            parametroDeReporte.setEstadocivil(new EstadosCiviles());
            parametroModificacion = parametroDeReporte;
            lovEstadosCiviles.clear();
            getLovEstadosCiviles();
            cambiosReporte = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
      }
      if (campoConfirmar.equalsIgnoreCase("TIPOTELEFONO")) {
         if (!valorConfirmar.isEmpty()) {
            parametroDeReporte.getTipotelefono().setNombre(tipotelefono);
            for (int i = 0; i < lovTiposTelefonos.size(); i++) {
               if (lovTiposTelefonos.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               parametroDeReporte.setTipotelefono(lovTiposTelefonos.get(indiceUnicoElemento));
               parametroModificacion = parametroDeReporte;
               lovTiposTelefonos.clear();
               getLovTiposTelefonos();
               cambiosReporte = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            } else {
               permitirIndex = false;
               RequestContext.getCurrentInstance().update("form:TipoTelefonoDialogo");
               RequestContext.getCurrentInstance().execute("PF('TipoTelefonoDialogo').show()");
            }
         } else {
            parametroDeReporte.setTipotelefono(new TiposTelefonos());
            parametroModificacion = parametroDeReporte;
            lovTiposTelefonos.clear();
            getLovTiposTelefonos();
            cambiosReporte = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
      }
      if (campoConfirmar.equalsIgnoreCase("JEFEDIV")) {
         if (!valorConfirmar.isEmpty()) {
            parametroDeReporte.getNombregerente().setNombreCompleto(jefediv);
            for (int i = 0; i < lovEmpleados.size(); i++) {
               if (lovEmpleados.get(i).getNombreCompleto().startsWith(valorConfirmar)) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               parametroDeReporte.setNombregerente(lovEmpleados.get(indiceUnicoElemento));
               parametroModificacion = parametroDeReporte;
               lovEmpleados.clear();
               getLovEmpleados();
               cambiosReporte = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            } else {
               permitirIndex = false;
               RequestContext.getCurrentInstance().update("form:JefeDivisionDialogo");
               RequestContext.getCurrentInstance().execute("PF('JefeDivisionDialogo').show()");
            }
         } else {
            parametroDeReporte.setNombregerente(new Empleados());
            parametroModificacion = parametroDeReporte;
            lovEmpleados.clear();
            getLovEmpleados();
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
               RequestContext.getCurrentInstance().update("form:EstructuraDialogo");
               RequestContext.getCurrentInstance().execute("PF('EstructuraDialogo').show()");
            }
         } else {
            parametroDeReporte.setLocalizacion(new Estructuras());
            parametroModificacion = parametroDeReporte;
            lovEstructuras.clear();
            getLovEstructuras();
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
               RequestContext.getCurrentInstance().update("form:TipoTrabajadorDialogo");
               RequestContext.getCurrentInstance().execute("PF('TipoTrabajadorDialogo').show()");
            }
         } else {
            parametroDeReporte.setTipotrabajador(new TiposTrabajadores());
            parametroModificacion = parametroDeReporte;
            lovTiposTrabajadores.clear();
            getLovTiposTrabajadores();
            cambiosReporte = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
      }
      if (campoConfirmar.equalsIgnoreCase("CIUDAD")) {
         if (!valorConfirmar.isEmpty()) {
            parametroDeReporte.getCiudad().setNombre(ciudad);
            for (int i = 0; i < lovCiudades.size(); i++) {
               if (lovCiudades.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               parametroDeReporte.setCiudad(lovCiudades.get(indiceUnicoElemento));
               parametroModificacion = parametroDeReporte;
               lovCiudades.clear();
               getLovCiudades();
               cambiosReporte = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            } else {
               permitirIndex = false;
               RequestContext.getCurrentInstance().update("form:CiudadDialogo");
               RequestContext.getCurrentInstance().execute("PF('CiudadDialogo').show()");
            }
         } else {
            parametroDeReporte.setCiudad(new Ciudades());
            parametroModificacion = parametroDeReporte;
            lovCiudades.clear();
            getLovCiudades();
            cambiosReporte = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
      }
      if (campoConfirmar.equalsIgnoreCase("DEPORTE")) {
         if (!valorConfirmar.isEmpty()) {
            parametroDeReporte.getDeporte().setNombre(deporte);
            for (int i = 0; i < lovDeportes.size(); i++) {
               if (lovDeportes.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               parametroDeReporte.setDeporte(lovDeportes.get(indiceUnicoElemento));
               parametroModificacion = parametroDeReporte;
               lovDeportes.clear();
               getLovDeportes();
               cambiosReporte = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            } else {
               permitirIndex = false;
               RequestContext.getCurrentInstance().update("form:DeportesDialogo");
               RequestContext.getCurrentInstance().execute("PF('DeportesDialogo').show()");
            }
         } else {

            parametroDeReporte.setDeporte(new Deportes());
            parametroModificacion = parametroDeReporte;
            lovDeportes.clear();
            getLovDeportes();
            cambiosReporte = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
      }
      if (campoConfirmar.equalsIgnoreCase("AFICION")) {
         if (!valorConfirmar.isEmpty()) {
            parametroDeReporte.getAficion().setDescripcion(aficiones);
            for (int i = 0; i < lovAficiones.size(); i++) {
               if (lovAficiones.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               parametroDeReporte.setAficion(lovAficiones.get(indiceUnicoElemento));
               parametroModificacion = parametroDeReporte;
               lovAficiones.clear();
               getLovAficiones();
               cambiosReporte = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");

            } else {
               permitirIndex = false;
               RequestContext.getCurrentInstance().update("form:AficionesDialogo");
               RequestContext.getCurrentInstance().execute("PF('AficionesDialogo').show()");
            }
         } else {
            parametroDeReporte.setAficion(new Aficiones());
            parametroModificacion = parametroDeReporte;
            lovAficiones.clear();
            getLovAficiones();
            cambiosReporte = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
      }
      if (campoConfirmar.equalsIgnoreCase("IDIOMA")) {
         if (!valorConfirmar.isEmpty()) {
            parametroDeReporte.getIdioma().setNombre(idioma);
            for (int i = 0; i < lovIdiomas.size(); i++) {
               if (lovIdiomas.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               parametroDeReporte.setIdioma(lovIdiomas.get(indiceUnicoElemento));
               parametroModificacion = parametroDeReporte;
               lovIdiomas.clear();
               getLovIdiomas();
               cambiosReporte = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            } else {
               permitirIndex = false;
               RequestContext.getCurrentInstance().update("form:IdiomasDialogo");
               RequestContext.getCurrentInstance().execute("PF('IdiomasDialogo').show()");
            }
         } else {
            parametroDeReporte.setIdioma(new Idiomas());
            parametroModificacion = parametroDeReporte;
            lovIdiomas.clear();
            getLovIdiomas();
            cambiosReporte = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
      }
      if (campoConfirmar.equalsIgnoreCase("EMPRESA")) {
         if (!valorConfirmar.isEmpty()) {
            parametroDeReporte.getEmpresa().setNombre(empresa);
            for (int i = 0; i < lovEmpresas.size(); i++) {
               if (lovEmpresas.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
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
               RequestContext.getCurrentInstance().update("form:EmpresaDialogo");
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
   }

   public void modificarParametroEmpleadoDesde(BigDecimal empldesde) {
      if (empldesde.equals("") || empldesde == null) {
         if (empldesde.equals(BigDecimal.valueOf(0))) {
            parametroDeReporte.setCodigoempleadodesde(BigDecimal.valueOf(0));
         }
      } else {
         parametroDeReporte.setCodigoempleadodesde(empldesde);
      }
      cambiosReporte = false;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void modificarParametroEmpleadoHasta(BigDecimal emphasta) {
      String h = "999999999999999999999999999999";
      BigDecimal b = new BigDecimal(h);
      if (emplHasta.equals("") || emplHasta == null) {
         if (emphasta.equals(b)) {
            parametroDeReporte.setCodigoempleadodesde(b);
         }
      } else {
         parametroDeReporte.setCodigoempleadodesde(emphasta);
      }
      cambiosReporte = false;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void cargarEmpleados() {
      if (lovEmpleados == null) {
         if (listasRecurrentes.getLovEmpleadosActivos().isEmpty()) {
            lovEmpleados = administrarNReportePersonal.listEmpleados();
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

   public void cargarEmpresas() {
      if (lovEmpresas == null) {
         lovEmpresas = administrarNReportePersonal.listEmpresas();
      }
   }

   public void cargarEstructuras() {
      if (lovEstructuras == null) {
         lovEstructuras = administrarNReportePersonal.listEstructuras();
      }
   }

   public void cargarTiposTrabajadores() {
      if (lovTiposTrabajadores == null) {
         if (listasRecurrentes.getLovTiposTrabajadores().isEmpty()) {
            lovTiposTrabajadores = administrarNReportePersonal.listTiposTrabajadores();
            if (lovTiposTrabajadores != null) {
               listasRecurrentes.setLovTiposTrabajadores(lovTiposTrabajadores);
            }
         } else {
            lovTiposTrabajadores = new ArrayList<TiposTrabajadores>(listasRecurrentes.getLovTiposTrabajadores());
         }
      }
   }

   public void cargarEstadosCiviles() {
      if (lovEstadosCiviles == null) {
         lovEstadosCiviles = administrarNReportePersonal.listEstadosCiviles();
      }
   }

   public void cargarTiposTelefonos() {
      if (lovTiposTelefonos == null) {
         lovTiposTelefonos = administrarNReportePersonal.listTiposTelefonos();
      }
   }

   public void cargarCiudades() {
      if (lovCiudades == null) {
         lovCiudades = administrarNReportePersonal.listCiudades();
      }
   }

   public void cargarDeporte() {
      if (lovDeportes == null) {
         lovDeportes = administrarNReportePersonal.listDeportes();
      }
   }

   public void cargarAficion() {
      if (lovAficiones == null) {
         lovAficiones = administrarNReportePersonal.listAficiones();
      }
   }

   public void cargarIdioma() {
      if (lovIdiomas == null) {
         lovIdiomas = administrarNReportePersonal.listIdiomas();
      }
   }

   public void dialogosParametros(int pos) {
      RequestContext context = RequestContext.getCurrentInstance();
      if (pos == 2) {
         lovEmpleados = null;
         cargarEmpleados();
         contarRegistrosEmpleadoD();
         RequestContext.getCurrentInstance().update("form:EmpleadoDesdeDialogo");
         RequestContext.getCurrentInstance().execute("PF('EmpleadoDesdeDialogo').show()");
         contarRegistrosEmpleadoD();
      }
      if (pos == 4) {
         lovEstadosCiviles = null;
         cargarEstadosCiviles();
         contarRegistrosEstadoCivil();
         RequestContext.getCurrentInstance().update("form:EstadoCivilDialogo");
         RequestContext.getCurrentInstance().execute("PF('EstadoCivilDialogo').show()");
         contarRegistrosEstadoCivil();
      }
      if (pos == 5) {
         lovTiposTelefonos = null;
         cargarTiposTelefonos();
         contarRegistrosTipoTelefono();
         RequestContext.getCurrentInstance().update("form:TipoTelefonoDialogo");
         RequestContext.getCurrentInstance().execute("PF('TipoTelefonoDialogo').show()");
         contarRegistrosTipoTelefono();
      }
      if (pos == 6) {
         lovEmpleados = null;
         cargarEmpleados();
         contarRegistrosJefe();
         RequestContext.getCurrentInstance().update("form:JefeDivisionDialogo");
         RequestContext.getCurrentInstance().execute("PF('JefeDivisionDialogo').show()");
         contarRegistrosJefe();
      }
      if (pos == 9) {
         lovEmpleados = null;
         cargarEmpleados();
         contarRegistrosEmpleadoH();
         RequestContext.getCurrentInstance().update("form:EmpleadoHastaDialogo");
         RequestContext.getCurrentInstance().execute("PF('EmpleadoHastaDialogo').show()");
         contarRegistrosEmpleadoH();
      }
      if (pos == 10) {
         lovEstructuras = null;
         cargarEstructuras();
         contarRegistrosEstructura();
         RequestContext.getCurrentInstance().update("form:EstructuraDialogo");
         RequestContext.getCurrentInstance().execute("PF('EstructuraDialogo').show()");
         contarRegistrosEstructura();
      }
      if (pos == 11) {
         lovTiposTrabajadores = null;
         cargarTiposTrabajadores();
         contarRegistrosTipoTrabajador();
         RequestContext.getCurrentInstance().update("form:TipoTrabajadorDialogo");
         RequestContext.getCurrentInstance().execute("PF('TipoTrabajadorDialogo').show()");
         contarRegistrosTipoTrabajador();
      }
      if (pos == 12) {
         lovCiudades = null;
         cargarCiudades();
         contarRegistrosCiudad();
         RequestContext.getCurrentInstance().update("form:CiudadDialogo");
         RequestContext.getCurrentInstance().execute("PF('CiudadDialogo').show()");
         contarRegistrosCiudad();
      }
      if (pos == 13) {
         lovDeportes = null;
         cargarDeporte();
         contarRegistrosDeporte();
         RequestContext.getCurrentInstance().update("form:DeportesDialogo");
         RequestContext.getCurrentInstance().execute("PF('DeportesDialogo').show()");
         contarRegistrosDeporte();
      }
      if (pos == 14) {
         lovAficiones = null;
         cargarAficion();
         contarRegistrosAficion();
         RequestContext.getCurrentInstance().update("form:AficionesDialogo");
         RequestContext.getCurrentInstance().execute("PF('AficionesDialogo').show()");
         contarRegistrosAficion();
      }
      if (pos == 15) {
         lovIdiomas = null;
         cargarIdioma();
         contarRegistrosIdioma();
         RequestContext.getCurrentInstance().update("form:IdiomasDialogo");
         RequestContext.getCurrentInstance().execute("PF('IdiomasDialogo').show()");
         contarRegistrosIdioma();
      }
      if (pos == 16) {
         lovEmpresas = null;
         cargarEmpresas();
         contarRegistrosEmpresa();
         RequestContext.getCurrentInstance().update("form:EmpresaDialogo");
         RequestContext.getCurrentInstance().execute("PF('EmpresaDialogo').show()");
         contarRegistrosEmpresa();
      }

   }

   public void activarAceptar() {
      aceptar = false;
   }

   public void activarEnvioCorreo() {
      activarEnvio = reporteSeleccionado == null;
      RequestContext.getCurrentInstance().update("form:ENVIOCORREO");
   }

   public void actualizarEmplDesde() {
      permitirIndex = true;
      parametroDeReporte.setCodigoempleadodesde(empleadoSeleccionado.getCodigoempleado());
      parametroModificacion = parametroDeReporte;
      cambiosReporte = false;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("form:lovEmpleadoDesde:globalFilter");
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
      context.reset("form:lovEmpleadoDesde:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovEmpleadoDesde').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('EmpleadoDesdeDialogo').hide()");
   }

   public void actualizarEmplHasta() {
      permitirIndex = true;
      parametroDeReporte.setCodigoempleadohasta(empleadoSeleccionado.getCodigoempleado());
      parametroModificacion = parametroDeReporte;
      cambiosReporte = false;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("form:lovEmpleadoHasta:globalFilter");
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
      context.reset("form:lovEmpleadoHasta:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovEmpleadoHasta').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('EmpleadoHastaDialogo').hide()");
   }

   public void actualizarEmpresa() {
      permitirIndex = true;
      parametroDeReporte.setEmpresa(empresaSeleccionada);
      parametroModificacion = parametroDeReporte;
      cambiosReporte = false;
      RequestContext context = RequestContext.getCurrentInstance();
      /*RequestContext.getCurrentInstance().update("form:EmpresaDialogo");
         RequestContext.getCurrentInstance().update("form:lovEmpresa");
         RequestContext.getCurrentInstance().update("form:aceptarEmp");*/
      context.reset("form:lovEmpresa:globalFilter");
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
      context.reset("form:lovEmpresa:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovEmpresa').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('EmpresaDialogo').hide()");
   }

   public void actualizarEstructura() {
      parametroDeReporte.setLocalizacion(estructuraSeleccionada);
      parametroModificacion = parametroDeReporte;
      cambiosReporte = false;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("form:lovEstructura:globalFilter");
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
      context.reset("form:lovEstructura:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovEstructura').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('EstructuraDialogo').hide()");
   }

   public void actualizarTipoTrabajador() {
      parametroDeReporte.setTipotrabajador(tipoTSeleccionado);
      parametroModificacion = parametroDeReporte;
      cambiosReporte = false;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("form:lovTipoTrabajador:globalFilter");
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
      context.reset("form:lovTipoTrabajador:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovTipoTrabajador').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('TipoTrabajadorDialogo').hide()");
   }

   public void actualizarEstadoCivil() {
      permitirIndex = true;
      parametroDeReporte.setEstadocivil(estadoCivilSeleccionado);
      parametroModificacion = parametroDeReporte;
      cambiosReporte = false;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("form:lovEstadoCivil:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovEstadoCivil').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('EstadoCivilDialogo').hide()");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      RequestContext.getCurrentInstance().update("formParametros:estadoCivilParametro");
      estadoCivilSeleccionado = null;
      aceptar = true;
      lovEstadosCivilesFiltrar = null;
   }

   public void cancelarEstadoCivil() {
      estadoCivilSeleccionado = null;
      aceptar = true;
      lovEstadosCivilesFiltrar = null;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("form:lovEstadoCivil:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovEstadoCivil').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('EstadoCivilDialogo').hide()");
   }

   public void actualizarTipoTelefono() {
      permitirIndex = true;
      parametroDeReporte.setTipotelefono(tipoTelefonoSeleccionado);
      parametroModificacion = parametroDeReporte;
      cambiosReporte = false;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("form:lovTipoTelefono:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovTipoTelefono').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('TipoTelefonoDialogo').hide()");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      RequestContext.getCurrentInstance().update("formParametros:tipoTelefonoParametro");
      tipoTelefonoSeleccionado = null;
      aceptar = true;
      lovTiposTelefonosFiltrar = null;
   }

   public void cancelarTipoTelefono() {
      tipoTelefonoSeleccionado = null;
      aceptar = true;
      lovTiposTelefonosFiltrar = null;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("form:lovTipoTelefono:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovTipoTelefono').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('TipoTelefonoDialogo').hide()");
   }

   public void actualizarJefeDivision() {
      permitirIndex = true;
      parametroDeReporte.setNombregerente(empleadoSeleccionado);
      parametroModificacion = parametroDeReporte;
      cambiosReporte = false;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("form:lovJefeD:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovJefeD').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('JefeDivisionDialogo').hide()");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      RequestContext.getCurrentInstance().update("formParametros:jefeDivisionParametro");
      empleadoSeleccionado = null;
      aceptar = true;
      lovEmpleadosFiltrar = null;
   }

   public void cancelarJefeDivision() {
      empleadoSeleccionado = null;
      aceptar = true;
      lovEmpleadosFiltrar = null;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("form:lovJefeD:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovJefeD').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('JefeDivisionDialogo').hide()");
   }

   public void actualizarCiudad() {
      permitirIndex = true;
      parametroDeReporte.setCiudad(ciudadSeleccionada);
      parametroModificacion = parametroDeReporte;
      cambiosReporte = false;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("form:lovCiudades:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovCiudades').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('CiudadDialogo').hide()");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      RequestContext.getCurrentInstance().update("formParametros:ciudadParametro");
      ciudadSeleccionada = null;
      aceptar = true;
      lovListCiudadesFiltrar = null;
   }

   public void cancelarCiudad() {
      ciudadSeleccionada = null;
      aceptar = true;
      lovListCiudadesFiltrar = null;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("form:lovCiudades:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovCiudades').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('CiudadDialogo').hide()");
   }

   public void actualizarDeporte() {
      permitirIndex = true;
      parametroDeReporte.setDeporte(deporteSeleccionado);
      parametroModificacion = parametroDeReporte;
      cambiosReporte = false;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("form:lovDeportes:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovDeportes').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('DeportesDialogo').hide()");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      RequestContext.getCurrentInstance().update("formParametros:deporteParametro");
      deporteSeleccionado = null;
      aceptar = true;
      lovDeportesFiltrar = null;
   }

   public void cancelarDeporte() {
      deporteSeleccionado = null;
      aceptar = true;
      lovDeportesFiltrar = null;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("form:lovDeportes:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovDeportes').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('DeportesDialogo').hide()");
   }

   public void actualizarAficion() {
      permitirIndex = true;
      parametroDeReporte.setAficion(aficionSeleccionada);
      parametroModificacion = parametroDeReporte;
      cambiosReporte = false;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("form:lovAficiones:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovAficiones').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('AficionesDialogo').hide()");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      RequestContext.getCurrentInstance().update("formParametros:aficionParametro");
      aficionSeleccionada = null;
      aceptar = true;
      lovAficionesFiltrar = null;
   }

   public void cancelarAficion() {
      aficionSeleccionada = null;
      aceptar = true;
      lovAficionesFiltrar = null;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("form:lovAficiones:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovAficiones').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('AficionesDialogo').hide()");
   }

   public void actualizarIdioma() {
      permitirIndex = true;
      parametroDeReporte.setIdioma(idiomaSeleccionado);
      parametroModificacion = parametroDeReporte;
      cambiosReporte = false;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("form:lovIdiomas:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovIdiomas').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('IdiomasDialogo').hide()");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      RequestContext.getCurrentInstance().update("formParametros:idiomaParametro");
      idiomaSeleccionado = null;
      aceptar = true;
      lovIdiomasFiltrar = null;
   }

   public void cancelarIdioma() {
      idiomaSeleccionado = null;
      aceptar = true;
      lovIdiomasFiltrar = null;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("form:lovIdiomas:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovIdiomas').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('IdiomasDialogo').hide()");
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

   public void mostrarDialogoBuscarReporte() {
      try {
         RequestContext context = RequestContext.getCurrentInstance();
         if (cambiosReporte == true) {
            lovInforeportes = null;
            contarRegistrosLovReportes();
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:ReportesDialogo");
            RequestContext.getCurrentInstance().execute("PF('ReportesDialogo').show()");
         } else {
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardarSinSalida').show()");
         }
      } catch (Exception e) {
         log.warn("Error mostrarDialogoBuscarReporte : " + e.toString());
      }
   }

   public void actualizarSeleccionInforeporte() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (bandera == 1) {
         cerrarFiltrado();
      }
      defaultPropiedadesParametrosReporte();
      listaIR.clear();
      listaIR.add(reporteSeleccionadoLov);
      filtrarListInforeportesUsuario = null;
      filtrarLovInforeportes = null;
      aceptar = true;
      activoBuscarReporte = true;
      activoMostrarTodos = false;
      reporteSeleccionado = reporteSeleccionadoLov;
      reporteSeleccionadoLov = null;
      RequestContext.getCurrentInstance().update("form:MOSTRARTODOS");
      RequestContext.getCurrentInstance().update("form:BUSCARREPORTE");
      context.reset("form:lovReportesDialogo:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovReportesDialogo').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('ReportesDialogo').hide()");
      RequestContext.getCurrentInstance().update("form:reportesPersonal");
      contarRegistros();
   }

   public void cancelarSeleccionInforeporte() {
      filtrarListInforeportesUsuario = null;
      reporteSeleccionadoLov = null;
      aceptar = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("form:lovReportesDialogo:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovReportesDialogo').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('ReportesDialogo').hide()");
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
         activoBuscarReporte = false;
         activoMostrarTodos = true;
         RequestContext.getCurrentInstance().update("form:MOSTRARTODOS");
         RequestContext.getCurrentInstance().update("form:BUSCARREPORTE");
         RequestContext.getCurrentInstance().update("form:reportesPersonal");
      } else {
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().execute("PF('confirmarGuardarSinSalida').show()");
      }
   }

   public void cancelarYSalir() {
//      cancelarModificaciones();
      salir();
   }

   public void defaultPropiedadesParametrosReporte() {
      color = "black";
      decoracion = "none";
      color2 = "black";
      decoracion2 = "none";
   }

   public void cancelarRequisitosReporte() {
      requisitosReporte = "";
   }

//    public void generarDocumentoReporte() {
//        try {
//            RequestContext context = RequestContext.getCurrentInstance();
//            if (reporteSeleccionado != null) {
//                nombreReporte = reporteSeleccionado.getNombrereporte();
//                tipoReporte = reporteSeleccionado.getTipo();
//                if (nombreReporte != null && tipoReporte != null) {
//                    pathReporteGenerado = administarReportes.generarReporte(nombreReporte, tipoReporte);
//                }
//                if (pathReporteGenerado != null) {
//                    validarDescargaReporte();
//                } else {
//                    RequestContext.getCurrentInstance().execute("PF('generandoReporte').hide()");
//                    RequestContext.getCurrentInstance().update("formDialogos:errorGenerandoReporte");
//                    RequestContext.getCurrentInstance().execute("PF('errorGenerandoReporte').show()");
//                }
//            } else {
//                log.info("Reporte Seleccionado es nulo");
//            }
//        } catch (Exception e) {
//            log.warn("Error en generar documento reporte : " + e.getMessage());
//        }
//
//    }
   public void generarReporte(Inforeportes reporte) {
      try {
         guardarCambios();
         reporteSeleccionado = reporte;
         seleccionRegistro();
         generarDocumentoReporte();
      } catch (Exception e) {
         log.warn("Error en generarReporte : " + e.getMessage());
         RequestContext.getCurrentInstance().execute("PF('generandoReporte').hide()");
         RequestContext.getCurrentInstance().update("formDialogos:errorGenerandoReporte");
         RequestContext.getCurrentInstance().execute("PF('errorGenerandoReporte').show()");
      }
   }

   public AsynchronousFilllListener listener() {
      return new AsynchronousFilllListener() {
         @Override
         public void reportFinished(JasperPrint jp) {
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
               log.info("ControlNReportePersonal reportFinished ERROR: " + e.toString());
            }
         }

         @Override
         public void reportCancelled() {
            estadoReporte = true;
            resultadoReporte = "Cancelación";
         }

         @Override
         public void reportFillError(Throwable e) {
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

   public void generarArchivoReporte(JasperPrint print) {
      if (print != null && tipoReporte != null) {
         pathReporteGenerado = administarReportes.crearArchivoReporte(print, tipoReporte);
         validarDescargaReporte();
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
                  RequestContext.getCurrentInstance().execute("PF('generandoReporte').hide()");
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

   public void reiniciarStreamedContent() {
      reporte = null;
   }

   public void cancelarReporte() {
      administarReportes.cancelarReporte();
   }

   //EVENTO FILTRAR
   public void eventoFiltrar() {
      contarRegistros();
   }

   //CONTAR REGISTROS
   public void contarRegistros() {
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
   }

   public void contarRegistrosEmpleadoD() {
      RequestContext.getCurrentInstance().update("form:infoRegistroEmpleadoDesde");
   }

   public void contarRegistrosEmpleadoH() {
      RequestContext.getCurrentInstance().update("form:infoRegistroEmpleadoHasta");
   }

   public void contarRegistrosEmpresa() {
      RequestContext.getCurrentInstance().update("form:infoRegistroEmpresa");
   }

   public void contarRegistrosEstructura() {
      RequestContext.getCurrentInstance().update("form:infoRegistroEstructura");
   }

   public void contarRegistrosTipoTrabajador() {
      RequestContext.getCurrentInstance().update("form:infoRegistroTipoTrabajador");
   }

   public void contarRegistrosEstadoCivil() {
      RequestContext.getCurrentInstance().update("form:infoRegistroEstadoCivil");
   }

   public void contarRegistrosTipoTelefono() {
      RequestContext.getCurrentInstance().update("form:infoRegistroTipoTelefono");
   }

   public void contarRegistrosCiudad() {
      RequestContext.getCurrentInstance().update("form:infoRegistroCiudad");
   }

   public void contarRegistrosDeporte() {
      RequestContext.getCurrentInstance().update("form:infoRegistroDeporte");
   }

   public void contarRegistrosAficion() {
      RequestContext.getCurrentInstance().update("form:infoRegistroAficion");
   }

   public void contarRegistrosIdioma() {
      RequestContext.getCurrentInstance().update("form:infoRegistroIdioma");
   }

   public void contarRegistrosJefe() {
      RequestContext.getCurrentInstance().update("form:infoRegistroJefe");
   }

   public void contarRegistrosLovReportes() {
      RequestContext.getCurrentInstance().update("form:infoRegistroReportes");
   }

   public void habilitarBotonLov() {
      activarLov = false;
      RequestContext.getCurrentInstance().update("form:listaValores");
   }

   public void deshabilitarBotonLov() {
      activarLov = true;
      RequestContext.getCurrentInstance().update("form:listaValores");
   }

   public void mostrarDialogoNuevaFecha() {
      getParametroDeReporte();
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

   ///////////////////////////////SETS Y GETS/////////////////////////////
   public ParametrosReportes getParametroDeReporte() {
      try {
         if (parametroDeReporte == null) {
            parametroDeReporte = new ParametrosReportes();
            parametroDeReporte = administrarNReportePersonal.parametrosDeReporte();
         }
         if (parametroDeReporte.getEstadocivil() == null) {
            parametroDeReporte.setEstadocivil(new EstadosCiviles());
         }
         if (parametroDeReporte.getTipotelefono() == null) {
            parametroDeReporte.setTipotelefono(new TiposTelefonos());
         }
         if (parametroDeReporte.getLocalizacion() == null) {
            parametroDeReporte.setLocalizacion(new Estructuras());
         }
         if (parametroDeReporte.getTipotrabajador() == null) {
            parametroDeReporte.setTipotrabajador(new TiposTrabajadores());
         }
         if (parametroDeReporte.getCiudad() == null) {
            parametroDeReporte.setCiudad(new Ciudades());
         }
         if (parametroDeReporte.getDeporte() == null) {
            parametroDeReporte.setDeporte(new Deportes());
         }
         if (parametroDeReporte.getAficion() == null) {
            parametroDeReporte.setAficion(new Aficiones());
         }
         if (parametroDeReporte.getIdioma() == null) {
            parametroDeReporte.setIdioma(new Idiomas());
         }
         if (parametroDeReporte.getEmpresa() == null) {
            parametroDeReporte.setEmpresa(new Empresas());
         }
         if (parametroDeReporte.getNombregerente() == null) {
            parametroDeReporte.setNombregerente(new Empleados());
         }
         return parametroDeReporte;
      } catch (Exception e) {
         log.warn("Error getParametroDeInforme : " + e.toString());
         return null;
      }
   }

   public void setParametroDeReporte(ParametrosReportes parametroDeReporte) {
      this.parametroDeReporte = parametroDeReporte;
   }

   public List<Inforeportes> getListaIR() {
      if (listaIR == null) {
         listaIR = administrarNReportePersonal.listInforeportesUsuario();
      }
      return listaIR;
   }

   public void setListaIR(List<Inforeportes> listaIR) {
      this.listaIR = listaIR;
   }

   public List<Inforeportes> getFiltrarListInforeportesUsuario() {
      return filtrarListInforeportesUsuario;
   }

   public void setFiltrarListInforeportesUsuario(List<Inforeportes> filtrarListInforeportesUsuario) {
      this.filtrarListInforeportesUsuario = filtrarListInforeportesUsuario;
   }

   public Inforeportes getReporteSeleccionado() {
      return reporteSeleccionado;
   }

   public void setReporteSeleccionado(Inforeportes reporteSeleccionado) {
      this.reporteSeleccionado = reporteSeleccionado;
   }

   public boolean isAceptar() {
      return aceptar;
   }

   public void setAceptar(boolean aceptar) {
      this.aceptar = aceptar;
   }

   public ParametrosReportes getNuevoParametroInforme() {
      return nuevoParametroInforme;
   }

   public void setNuevoParametroInforme(ParametrosReportes nuevoParametroInforme) {
      this.nuevoParametroInforme = nuevoParametroInforme;
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

   public void setLovEmpleados(List<Empleados> lovEmpleados) {
      this.lovEmpleados = lovEmpleados;
   }

   public List<Empresas> getLovEmpresas() {
      return lovEmpresas;
   }

   public void setLovEmpresas(List<Empresas> lovEmpresas) {
      this.lovEmpresas = lovEmpresas;
   }

   public List<Estructuras> getLovEstructuras() {
      return lovEstructuras;
   }

   public void setLovEstructuras(List<Estructuras> lovEstructuras) {
      this.lovEstructuras = lovEstructuras;
   }

   public List<TiposTrabajadores> getLovTiposTrabajadores() {
      return lovTiposTrabajadores;
   }

   public void setLovTiposTrabajadores(List<TiposTrabajadores> lovTiposTrabajadores) {
      this.lovTiposTrabajadores = lovTiposTrabajadores;
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

   public List<Estructuras> getLovEstructurasFiltrar() {
      return lovEstructurasFiltrar;
   }

   public void setLovEstructurasFiltrar(List<Estructuras> lovEstructurasFiltrar) {
      this.lovEstructurasFiltrar = lovEstructurasFiltrar;
   }

   public List<TiposTrabajadores> getLovTiposTrabajadoresFiltrar() {
      return lovTiposTrabajadoresFiltrar;
   }

   public void setLovTiposTrabajadoresFiltrar(List<TiposTrabajadores> lovTiposTrabajadoresFiltrar) {
      this.lovTiposTrabajadoresFiltrar = lovTiposTrabajadoresFiltrar;
   }

   public List<EstadosCiviles> getLovEstadosCiviles() {
      return lovEstadosCiviles;
   }

   public void setLovEstadosCiviles(List<EstadosCiviles> lovEstadosCiviles) {
      this.lovEstadosCiviles = lovEstadosCiviles;
   }

   public List<TiposTelefonos> getLovTiposTelefonos() {
      return lovTiposTelefonos;
   }

   public void setLovTiposTelefonos(List<TiposTelefonos> lovTiposTelefonos) {
      this.lovTiposTelefonos = lovTiposTelefonos;
   }

   public List<Ciudades> getLovCiudades() {
      return lovCiudades;
   }

   public void setLovCiudades(List<Ciudades> lovCiudades) {
      this.lovCiudades = lovCiudades;
   }

   public List<Deportes> getLovDeportes() {
      return lovDeportes;
   }

   public void setLovDeportes(List<Deportes> lovDeportes) {
      this.lovDeportes = lovDeportes;
   }

   public List<Aficiones> getLovAficiones() {
      return lovAficiones;
   }

   public void setLovAficiones(List<Aficiones> lovAficiones) {
      this.lovAficiones = lovAficiones;
   }

   public List<Idiomas> getLovIdiomas() {
      return lovIdiomas;
   }

   public void setLovIdiomas(List<Idiomas> lovIdiomas) {
      this.lovIdiomas = lovIdiomas;
   }

   public EstadosCiviles getEstadoCivilSeleccionado() {
      return estadoCivilSeleccionado;
   }

   public void setEstadoCivilSeleccionado(EstadosCiviles estadoCivilSeleccionado) {
      this.estadoCivilSeleccionado = estadoCivilSeleccionado;
   }

   public TiposTelefonos getTipoTelefonoSeleccionado() {
      return tipoTelefonoSeleccionado;
   }

   public void setTipoTelefonoSeleccionado(TiposTelefonos tipoTelefonoSeleccionado) {
      this.tipoTelefonoSeleccionado = tipoTelefonoSeleccionado;
   }

   public Ciudades getCiudadSeleccionada() {
      return ciudadSeleccionada;
   }

   public void setCiudadSeleccionada(Ciudades ciudadSeleccionada) {
      this.ciudadSeleccionada = ciudadSeleccionada;
   }

   public Deportes getDeporteSeleccionado() {
      return deporteSeleccionado;
   }

   public void setDeporteSeleccionado(Deportes deporteSeleccionado) {
      this.deporteSeleccionado = deporteSeleccionado;
   }

   public Aficiones getAficionSeleccionada() {
      return aficionSeleccionada;
   }

   public void setAficionSeleccionada(Aficiones aficionSeleccionada) {
      this.aficionSeleccionada = aficionSeleccionada;
   }

   public List<EstadosCiviles> getLovEstadosCivilesFiltrar() {
      return lovEstadosCivilesFiltrar;
   }

   public void setLovEstadosCivilesFiltrar(List<EstadosCiviles> lovEstadosCivilesFiltrar) {
      this.lovEstadosCivilesFiltrar = lovEstadosCivilesFiltrar;
   }

   public List<TiposTelefonos> getLovTiposTelefonosFiltrar() {
      return lovTiposTelefonosFiltrar;
   }

   public void setLovTiposTelefonosFiltrar(List<TiposTelefonos> lovTiposTelefonosFiltrar) {
      this.lovTiposTelefonosFiltrar = lovTiposTelefonosFiltrar;
   }

   public List<Ciudades> getLovListCiudadesFiltrar() {
      return lovListCiudadesFiltrar;
   }

   public void setLovListCiudadesFiltrar(List<Ciudades> lovListCiudadesFiltrar) {
      this.lovListCiudadesFiltrar = lovListCiudadesFiltrar;
   }

   public List<Deportes> getLovDeportesFiltrar() {
      return lovDeportesFiltrar;
   }

   public void setLovDeportesFiltrar(List<Deportes> lovDeportesFiltrar) {
      this.lovDeportesFiltrar = lovDeportesFiltrar;
   }

   public List<Aficiones> getLovAficionesFiltrar() {
      return lovAficionesFiltrar;
   }

   public void setLovAficionesFiltrar(List<Aficiones> lovAficionesFiltrar) {
      this.lovAficionesFiltrar = lovAficionesFiltrar;
   }

   public List<Idiomas> getLovIdiomasFiltrar() {
      return lovIdiomasFiltrar;
   }

   public void setLovIdiomasFiltrar(List<Idiomas> lovIdiomasFiltrar) {
      this.lovIdiomasFiltrar = lovIdiomasFiltrar;
   }

   public Idiomas getIdiomaSeleccionado() {
      return idiomaSeleccionado;
   }

   public void setIdiomaSeleccionado(Idiomas idiomaSeleccionado) {
      this.idiomaSeleccionado = idiomaSeleccionado;
   }

   public boolean isCambiosReporte() {
      return cambiosReporte;
   }

   public void setCambiosReporte(boolean cambiosReporte) {
      this.cambiosReporte = cambiosReporte;
   }

   public List<Inforeportes> getListaInfoReportesModificados() {
      return listaInfoReportesModificados;
   }

   public void setListaInfoReportesModificados(List<Inforeportes> listaInfoReportesModificados) {
      this.listaInfoReportesModificados = listaInfoReportesModificados;
   }

   public String getAltoTabla() {
      return altoTabla;
   }

   public void setAltoTabla(String altoTabla) {
      this.altoTabla = altoTabla;
   }

   public StreamedContent getFile() {
      return file;
   }

   public void setFile(StreamedContent file) {
      this.file = file;
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

   public String getInfoRegistro() {
      FacesContext c = FacesContext.getCurrentInstance();
      tabla = (DataTable) c.getViewRoot().findComponent("form:reportesPersonal");
      infoRegistro = String.valueOf(tabla.getRowCount());
      return infoRegistro;
   }

   public String getInfoRegistroEmpleadoDesde() {
      FacesContext c = FacesContext.getCurrentInstance();
      tabla = (DataTable) c.getViewRoot().findComponent("form:lovEmpleadoDesde");
      infoRegistroEmpleadoDesde = String.valueOf(tabla.getRowCount());
      return infoRegistroEmpleadoDesde;
   }

   public String getInfoRegistroEmpleadoHasta() {
      FacesContext c = FacesContext.getCurrentInstance();
      tabla = (DataTable) c.getViewRoot().findComponent("form:lovEmpleadoHasta");
      infoRegistroEmpleadoHasta = String.valueOf(tabla.getRowCount());
      return infoRegistroEmpleadoHasta;
   }

   public String getInfoRegistroEmpresa() {
      FacesContext c = FacesContext.getCurrentInstance();
      tabla = (DataTable) c.getViewRoot().findComponent("form:lovEmpresa");
      infoRegistroEmpresa = String.valueOf(tabla.getRowCount());
      return infoRegistroEmpresa;
   }

   public String getInfoRegistroEstructura() {

      FacesContext c = FacesContext.getCurrentInstance();
      tabla = (DataTable) c.getViewRoot().findComponent("form:lovEstructura");
      infoRegistroEstructura = String.valueOf(tabla.getRowCount());
      return infoRegistroEstructura;
   }

   public String getInfoRegistroTipoTrabajador() {
      FacesContext c = FacesContext.getCurrentInstance();
      tabla = (DataTable) c.getViewRoot().findComponent("form:lovTipoTrabajador");
      infoRegistroTipoTrabajador = String.valueOf(tabla.getRowCount());
      return infoRegistroTipoTrabajador;
   }

   public String getInfoRegistroEstadoCivil() {
      FacesContext c = FacesContext.getCurrentInstance();
      tabla = (DataTable) c.getViewRoot().findComponent("form:lovEstadoCivil");
      infoRegistroEstadoCivil = String.valueOf(tabla.getRowCount());
      return infoRegistroEstadoCivil;
   }

   public String getInfoRegistroTipoTelefono() {
      FacesContext c = FacesContext.getCurrentInstance();
      tabla = (DataTable) c.getViewRoot().findComponent("form:lovTipoTelefono");
      infoRegistroTipoTelefono = String.valueOf(tabla.getRowCount());
      return infoRegistroTipoTelefono;
   }

   public String getInfoRegistroCiudad() {
      FacesContext c = FacesContext.getCurrentInstance();
      tabla = (DataTable) c.getViewRoot().findComponent("form:lovCiudades");
      infoRegistroCiudad = String.valueOf(tabla.getRowCount());
      return infoRegistroCiudad;
   }

   public String getInfoRegistroDeporte() {
      FacesContext c = FacesContext.getCurrentInstance();
      tabla = (DataTable) c.getViewRoot().findComponent("form:lovDeportes");
      infoRegistroDeporte = String.valueOf(tabla.getRowCount());
      return infoRegistroDeporte;
   }

   public String getInfoRegistroAficion() {
      FacesContext c = FacesContext.getCurrentInstance();
      tabla = (DataTable) c.getViewRoot().findComponent("form:lovAficiones");
      infoRegistroAficion = String.valueOf(tabla.getRowCount());
      return infoRegistroAficion;
   }

   public String getInfoRegistroIdioma() {
      FacesContext c = FacesContext.getCurrentInstance();
      tabla = (DataTable) c.getViewRoot().findComponent("form:lovIdiomas");
      infoRegistroIdioma = String.valueOf(tabla.getRowCount());
      return infoRegistroIdioma;
   }

   public String getInfoRegistroJefe() {
      FacesContext c = FacesContext.getCurrentInstance();
      tabla = (DataTable) c.getViewRoot().findComponent("form:lovJefeD");
      infoRegistroJefe = String.valueOf(tabla.getRowCount());
      return infoRegistroJefe;
   }

   public String getInfoRegistroReportes() {
      FacesContext c = FacesContext.getCurrentInstance();
      tabla = (DataTable) c.getViewRoot().findComponent("form:lovReportesDialogo");
      infoRegistroReportes = String.valueOf(tabla.getRowCount());
      return infoRegistroReportes;
   }

   public List<Inforeportes> getLovInforeportes() {
      lovInforeportes = administrarNReportePersonal.listInforeportesUsuario();
      return lovInforeportes;
   }

   public void setLovInforeportes(List<Inforeportes> lovInforeportes) {
      this.lovInforeportes = lovInforeportes;
   }

   public List<Inforeportes> getFiltrarLovInforeportes() {
      return filtrarLovInforeportes;
   }

   public void setFiltrarLovInforeportes(List<Inforeportes> filtrarLovInforeportes) {
      this.filtrarLovInforeportes = filtrarLovInforeportes;
   }

   public String getPathReporteGenerado() {
      return pathReporteGenerado;
   }

   public void setPathReporteGenerado(String pathReporteGenerado) {
      this.pathReporteGenerado = pathReporteGenerado;
   }

   public StreamedContent getReporte() {
      return reporte;
   }

   public void setReporte(StreamedContent reporte) {
      this.reporte = reporte;
   }

   public String getCabezeraVisor() {
      return cabezeraVisor;
   }

   public void setCabezeraVisor(String cabezeraVisor) {
      this.cabezeraVisor = cabezeraVisor;
   }

   public Inforeportes getReporteSeleccionadoLov() {
      return reporteSeleccionadoLov;
   }

   public void setReporteSeleccionadoLov(Inforeportes reporteSeleccionadoLov) {
      this.reporteSeleccionadoLov = reporteSeleccionadoLov;
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
