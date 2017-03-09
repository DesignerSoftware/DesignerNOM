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
import javax.ejb.EJB;
import ControlNavegacion.ControlListaNavegacion;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
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
import org.primefaces.component.column.Column;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.inputtext.InputText;
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

    @EJB
    AdministrarNReportesNominaInterface administrarNReportesNomina;
    @EJB
    AdministarReportesInterface administarReportes;
//PARAMETROS REPORTES
    private ParametrosReportes parametroDeReporte;
    private ParametrosReportes parametroModificacion;
    //INFOREPORTES
    private List<Inforeportes> listaIR;
    private Inforeportes reporteSeleccionado;
    private List<Inforeportes> filtrarListIRU;
    private List<Inforeportes> listaInfoReportesModificados;
    //INFOREPORTES LOV
    private List<Inforeportes> listValInforeportes;
    private Inforeportes reporteSeleccionadoLOV;
    private List<Inforeportes> filtrarLovInforeportes;
    private List<Inforeportes> filtrarReportes;
    //EMPLEADOS
    private List<Empleados> listValEmpleados;
    private Empleados empleadoSeleccionado;
    private List<Empleados> filtrarListEmpleados;
    //GRUPO
    private List<GruposConceptos> listValGruposConceptos;
    private GruposConceptos grupoCSeleccionado;
    private List<GruposConceptos> filtrarListGruposConceptos;
    //UBICACION GEOGRAFICA
    private List<UbicacionesGeograficas> listValUbicacionesGeograficas;
    private UbicacionesGeograficas ubicacionesGSeleccionado;
    private List<UbicacionesGeograficas> filtrarListUbicacionesGeograficas;
    //TIPOS ASOCIACIONES
    private List<TiposAsociaciones> listValTiposAsociaciones;
    private TiposAsociaciones tiposASeleccionado;
    private List<TiposAsociaciones> filtrarListTiposAsociaciones;
    //EMPRESAS
    private List<Empresas> listValEmpresas;
    private Empresas empresaSeleccionada;
    private List<Empresas> filtrarListEmpresas;
    //ASOCIACIONES
    private List<Asociaciones> listValAsociaciones;
    private Asociaciones asociacionSeleccionado;
    private List<Asociaciones> filtrarListAsociaciones;
    //TERCEROS
    private List<Terceros> listValTerceros;
    private Terceros terceroSeleccionado;
    private List<Terceros> filtrarListTerceros;
    //PROCESOS
    private List<Procesos> listValProcesos;
    private Procesos procesoSeleccionado;
    private List<Procesos> filtrarListProcesos;
    //TIPOS TRABAJADORES
    private List<TiposTrabajadores> listValTiposTrabajadores;
    private TiposTrabajadores tipoTSeleccionado;
    private List<TiposTrabajadores> filtrarListTiposTrabajadores;
    //ESTRUCTURAS
    private List<Estructuras> listValEstructuras;
    private Estructuras estructuraSeleccionada;
    private List<Estructuras> filtrarListEstructuras;
    //COLUMNS
    private Column codigoIR, reporteIR;
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

    public ControlNReporteNomina() {
        System.out.println(this.getClass().getName() + ".Constructor()");
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
        reporteGenerar = "";
        requisitosReporte = "";
        posicionReporte = -1;
        listValInforeportes = null;
        listValAsociaciones = null;
//        listValEmpleados = null;
        listValEmpleados = new ArrayList<>();
        listValEmpresas = null;
        listValEstructuras = null;
        listValGruposConceptos = null;
        listValProcesos = null;
        listValTerceros = null;
        listValTiposAsociaciones = null;
        listValTiposTrabajadores = null;
        listValUbicacionesGeograficas = null;
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
        altoTabla = "140";
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
        if (pag.equals("atras")) {
            pag = paginaAnterior;
            paginaAnterior = "nominaf";
            controlListaNavegacion.quitarPagina();
        } else {
            String pagActual = "nreportenomina";
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
            controlListaNavegacion.adicionarPagina(pagActual);
        }
        fc.getApplication().getNavigationHandler().handleNavigation(fc, null, pag);
    }

    public void limpiarListasValor() {

    }

    @PostConstruct
    public void iniciarAdministradores() {
        System.out.println(this.getClass().getName() + ".iniciarAdministradores()");
        try {
            FacesContext contexto = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) contexto.getExternalContext().getSession(false);
            externalContext = contexto.getExternalContext();
            userAgent = externalContext.getRequestHeaderMap().get("User-Agent");
            administarReportes.obtenerConexion(ses.getId());
            administrarNReportesNomina.obtenerConexion(ses.getId());
            System.out.println(this.getClass().getName() + " fin de iniciarAdministradores()");
        } catch (Exception e) {
            System.out.println("Error postconstruct controlNReporteNomina" + e);
            System.out.println("Causa: " + e.getMessage());
        }
    }

    public void iniciarPagina() {
        System.out.println(this.getClass().getName() + ".iniciarPagina()");
        activoMostrarTodos = true;
        activoBuscarReporte = false;
        activarEnvio = true;
        getListaIR();
    }

//TOOLTIP
    public void guardarCambios() {
        System.out.println(this.getClass().getName() + ".guardarCambios()");
        RequestContext context = RequestContext.getCurrentInstance();
        System.out.println("cambiosReporte " + cambiosReporte);
        try {
            if (!cambiosReporte) {
                System.out.println("Entre a if (cambiosReporte == false)");
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
                if (!listaInfoReportesModificados.isEmpty()) {
                    System.out.println("Controlador.ControlNReporteNomina.guardarCambios().if (!listaInfoReportesModificados.isEmpty())");
                    administrarNReportesNomina.guardarCambiosInfoReportes(listaInfoReportesModificados);
                    listaInfoReportesModificados.clear();
                }
                cambiosReporte = true;
                FacesMessage msg = new FacesMessage("Información", "Los datos se guardarón con Éxito.");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                RequestContext.getCurrentInstance().update("form:growl");
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            } else {
                System.out.println("Ingrese al else");
                if (parametroDeReporte.getFechadesde().after(parametroDeReporte.getFechahasta())) {
                    fechaDesde = parametroDeReporte.getFechadesde();
                    fechaHasta = parametroDeReporte.getFechahasta();
                    System.out.println("parametroDeReporte.getFechadesde: " + parametroDeReporte.getFechadesde());
                    System.out.println("parametroDeReporte.getFechahasta: " + parametroDeReporte.getFechahasta());
                    parametroDeReporte.setFechadesde(fechaDesde);
                    parametroDeReporte.setFechahasta(fechaHasta);
                    RequestContext.getCurrentInstance().update("formParametros");
                    RequestContext.getCurrentInstance().execute("PF('errorFechas').show()");
                }
            }
        } catch (Exception e) {
            System.out.println("Error en guardar Cambios Controlador : " + e.toString());
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
            listValEmpleados = null;
            cargarListaEmpleados();
            RequestContext.getCurrentInstance().update("form:EmpleadoDesdeDialogo");
            RequestContext.getCurrentInstance().execute("PF('EmpleadoDesdeDialogo').show()");
            contarRegistrosEmpeladoD();
        }
        if (casilla == 4) {
            listValGruposConceptos = null;
            cargarListaGruposConcepto();
            RequestContext.getCurrentInstance().update("form:GruposConceptosDialogo");
            RequestContext.getCurrentInstance().execute("PF('GruposConceptosDialogo').show()");
            contarRegistrosGrupo();
        }
        if (casilla == 5) {
            listValUbicacionesGeograficas = null;
            cargarListaUbicacionesGeograficas();
            RequestContext.getCurrentInstance().update("form:UbiGeograficaDialogo");
            RequestContext.getCurrentInstance().execute("PF('UbiGeograficaDialogo').show()");
            contarRegistrosUbicacion();
        }
        if (casilla == 6) {
            listValTiposAsociaciones = null;
            getListValTiposAsociaciones();
            RequestContext.getCurrentInstance().update("form:TipoAsociacionDialogo");
            RequestContext.getCurrentInstance().execute("PF('TipoAsociacionDialogo').show()");
            contarRegistrosTipoAsociacion();
        }
        if (casilla == 8) {
            listValEmpleados = null;
            cargarListaEmpleados();
            RequestContext.getCurrentInstance().update("form:EmpleadoHastaDialogo");
            RequestContext.getCurrentInstance().execute("PF('EmpleadoHastaDialogo').show()");
            contarRegistrosEmpeladoH();
        }
        if (casilla == 10) {
            listValEmpresas = null;
            getListValEmpresas();
            RequestContext.getCurrentInstance().update("form:EmpresaDialogo");
            RequestContext.getCurrentInstance().execute("PF('EmpresaDialogo').show()");
            contarRegistrosEmpresa();
        }
        if (casilla == 11) {
            listValEstructuras = null;
            cargarListaEstructuras();
            RequestContext.getCurrentInstance().update("form:EstructuraDialogo");
            RequestContext.getCurrentInstance().execute("PF('EstructuraDialogo').show()");
            contarRegistrosEstructura();
        }
        if (casilla == 12) {
            listValTiposTrabajadores = null;
            cargarListaTiposTrabajadores();
            RequestContext.getCurrentInstance().update("form:TipoTrabajadorDialogo");
            RequestContext.getCurrentInstance().execute("PF('TipoTrabajadorDialogo').show()");
            contarRegistrosTipoTrabajador();
        }
        if (casilla == 13) {
            listValTerceros = null;
            cargarListaTerceros();
            RequestContext.getCurrentInstance().update("form:TerceroDialogo");
            RequestContext.getCurrentInstance().execute("PF('TerceroDialogo').show()");
            contarRegistrosTercero();
        }
        if (casilla == 14) {
            listValProcesos = null;
            cargarListaProcesos();
            RequestContext.getCurrentInstance().update("form:ProcesoDialogo");
            RequestContext.getCurrentInstance().execute("PF('ProcesoDialogo').show()");
            contarRegistrosProceso();
        }
        if (casilla == 16) {
            listValAsociaciones = null;
            cargarListaAsociaciones();
            RequestContext.getCurrentInstance().update("form:AsociacionDialogo");
            RequestContext.getCurrentInstance().execute("PF('AsociacionDialogo').show()");
            contarRegistrosAsociacion();
        }
    }

    public void activarCtrlF11() {
        System.out.println(this.getClass().getName() + ".activarCtrlF11()");
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            altoTabla = "120";
            codigoIR = (Column) c.getViewRoot().findComponent("form:reportesNomina:codigoIR");
            codigoIR.setFilterStyle("width: 85% !important");
            reporteIR = (Column) c.getViewRoot().findComponent("form:reportesNomina:reporteIR");
            reporteIR.setFilterStyle("width: 85% !important");
            RequestContext.getCurrentInstance().update("form:reportesNomina");
            bandera = 1;
        } else if (bandera == 1) {
            cerrarFiltrado();
            defaultPropiedadesParametrosReporte();
        }
    }

    public void cerrarFiltrado() {
        FacesContext c = FacesContext.getCurrentInstance();
        System.out.println("Desactivar");
        altoTabla = "140";
        codigoIR = (Column) c.getViewRoot().findComponent("form:reportesNomina:codigoIR");
        codigoIR.setFilterStyle("display: none; visibility: hidden;");
        reporteIR = (Column) c.getViewRoot().findComponent("form:reportesNomina:reporteIR");
        reporteIR.setFilterStyle("display: none; visibility: hidden;");
        RequestContext.getCurrentInstance().update("form:reportesNomina");
        bandera = 0;
        tipoLista = 0;
        filtrarListIRU = null;
    }

    public void salir() {
        System.out.println(this.getClass().getName() + ".salir()");
        if (bandera == 1) {
            cerrarFiltrado();
        }
        listaIR = null;
        parametroDeReporte = null;
        parametroModificacion = null;
        listValAsociaciones = null;
        listValEmpleados = null;
        listValEmpresas = null;
        listValEstructuras = null;
        listValGruposConceptos = null;
        listValProcesos = null;
        listValTerceros = null;
        listValTiposAsociaciones = null;
        listValTiposTrabajadores = null;
        listValUbicacionesGeograficas = null;
        casilla = -1;
        listaInfoReportesModificados.clear();
        cambiosReporte = true;
        reporteSeleccionado = null;
        reporteSeleccionadoLOV = null;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void cancelarModificaciones() {
        System.out.println(this.getClass().getName() + ".cancelarModificaciones()");
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
        System.out.println(this.getClass().getName() + ".cancelarYSalir()");
        cancelarModificaciones();
        salir();
    }

    //EVENTO FILTRAR
    public void eventoFiltrar() {
        System.out.println("Estoy Controlador.ControlNReporteNomina.eventoFiltrar()");
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
        RequestContext context = RequestContext.getCurrentInstance();
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
            try {
                System.out.println("empleadoDesdeParametro: " + empleadoDesdeParametro);
                if (empleadoDesdeParametro.getStyle().contains(" color: red;")) {

                    System.out.println("reeemplazarr " + empleadoDesdeParametro.getStyle().replace(" color: red;", ""));
                    empleadoDesdeParametro.setStyle(empleadoDesdeParametro.getStyle().replace(" color: red;", ""));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        RequestContext.getCurrentInstance().update("formParametros:empleadoDesdeParametro");
        if (reporteSeleccionado.getEmhasta().equals("SI")) {
            empleadoHastaParametro = (InputText) FacesContext.getCurrentInstance().getViewRoot().findComponent("formParametros:empleadoHastaParametro");
            //empleadoHastaParametro.setStyle("position: absolute; top: 41px; left: 330px; height: 10px; font-size: 11px; width: 90px; color: red;");
            empleadoHastaParametro.setStyle(empleadoHastaParametro.getStyle() + "color: red;");
            RequestContext.getCurrentInstance().update("formParametros:empleadoHastaParametro");
        }
        if (reporteSeleccionado.getGrupo() != null && reporteSeleccionado.getGrupo().equals("SI")) {
            grupoParametro = (InputText) FacesContext.getCurrentInstance().getViewRoot().findComponent("formParametros:grupoParametro");
            //grupoParametro.setStyle("position: absolute; top: 89px; left: 150px; height: 10px; font-size: 11px; width: 130px; color: red;");
            grupoParametro.setStyle(grupoParametro.getStyle() + "color: red;");
            RequestContext.getCurrentInstance().update("formParametros:grupoParametro");
        }
        if (reporteSeleccionado.getLocalizacion().equals("SI")) {
            estructuraParametro = (InputText) FacesContext.getCurrentInstance().getViewRoot().findComponent("formParametros:estructuraParametro");
            //estructuraParametro.setStyle("position: absolute; top: 20px; left: 625px;height: 10px; font-size: 11px;width: 180px; color: red;");
            estructuraParametro.setStyle(estructuraParametro.getStyle() + "color: red;");
            RequestContext.getCurrentInstance().update("formParametros:estructuraParametro");
        }
        if (reporteSeleccionado.getTipotrabajador().equals("SI")) {
            tipoTrabajadorParametro = (InputText) FacesContext.getCurrentInstance().getViewRoot().findComponent("formParametros:tipoTrabajadorParametro");
            //tipoTrabajadorParametro.setStyle("position: absolute; top: 43px; left: 625px;height: 10px; font-size: 11px; width: 180px; color: red;");
            tipoTrabajadorParametro.setStyle(tipoTrabajadorParametro.getStyle() + "color: red;");
            RequestContext.getCurrentInstance().update("formParametros:tipoTrabajadorParametro");
        }
        if (reporteSeleccionado.getTercero().equals("SI")) {
            terceroParametro = (InputText) FacesContext.getCurrentInstance().getViewRoot().findComponent("formParametros:terceroParametro");
            //terceroParametro.setStyle("position: absolute; top: 66px; left: 625px; height: 10px; font-size: 11px; width: 180px; color: red;");
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
    }

    public void requisitosParaReporte() {
        System.out.println(this.getClass().getName() + ".requisitosParaReporte()");
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

    public void modificacionTipoReporte(Inforeportes reporte) {
        System.out.println(this.getClass().getName() + ".modificacionTipoReporte()");
        reporteSeleccionado = reporte;
        System.out.println("reporteSeleccionado: " + reporteSeleccionado);
        System.out.println("Tipo reporteSeleccionado: " + reporteSeleccionado.getTipo());
        cambiosReporte = false;
        if (listaInfoReportesModificados.isEmpty()) {
            listaInfoReportesModificados.add(reporteSeleccionado);
        } else if (!listaInfoReportesModificados.contains(reporteSeleccionado)) {
            listaInfoReportesModificados.add(reporteSeleccionado);
        } else {
            int posicion = listaInfoReportesModificados.indexOf(reporteSeleccionado);
            listaInfoReportesModificados.set(posicion, reporteSeleccionado);
        }
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void generarReporte(Inforeportes reporte) {
        System.out.println(this.getClass().getName() + ".generarReporte()");
        reporteSeleccionado = reporte;
        seleccionRegistro();
//        generandoReport();
        generarDocumentoReporte();
    }

    public void generandoReport() {
        System.out.println("Controlador.ControlNReporteNomina.generandoReport()");
//        RequestContext.getCurrentInstance().execute("PF('generandoReporte').show()");
    }

    /*
     * public void generarReporte() { /*HttpServletRequest request =
     * (HttpServletRequest)
     * FacesContext.getCurrentInstance().getExternalContext().getRequest();
     * HttpServletResponse response = (HttpServletResponse)
     * FacesContext.getCurrentInstance().getExternalContext().getResponse();
     * FacesContext con = getFacesContext(request, response);
     */
    //System.out.println("Faces: " + con);
    //FacesContext f = FacesContext.getCurrentInstance();
    ///System.out.println("Estado respuesta: " + f.getResponseComplete());
    /*
     * RequestContext context = RequestContext.getCurrentInstance();
     * //System.out.println("Context: " + f); if (tipoLista == 0) {
     * nombreReporte = listaIR.get(indice).getNombrereporte(); tipoReporte =
     * listaIR.get(indice).getTipo(); } else { nombreReporte =
     * filtrarListInforeportesUsuario.get(indice).getNombrereporte();
     * tipoReporte = filtrarListInforeportesUsuario.get(indice).getTipo(); } if
     * (asistenteReporte == null) { asistenteReporte = listener();
     * System.out.println("Creo el listener. :D"); } /*if (nombreReporte != null
     * && tipoReporte != null) { pathReporteGenerado =
     * administarReportes.generarReporte(nombreReporte, tipoReporte,
     * asistenteReporte); }
     */
 /*
     * if (nombreReporte != null) {
     * administarReportes.iniciarLlenadoReporte(nombreReporte,
     * asistenteReporte); } /* if (pathReporteGenerado != null) {
     * //RequestContext.getCurrentInstance().execute("PF('exportarReporte();"); System.out.println("Pasooo"); /*
     * try { //exportarReporte(); } catch (IOException ex) {
     * Logger.getLogger(ControlNReporteNomina.class.getName()).log(Level.SEVERE,
     * null, ex); }
     */
    //}
    //RequestContext.getCurrentInstance().execute("PF('dlg').hide()");
    // RequestContext.getCurrentInstance().execute("PF('esperarReporte();");
    // }
    public AsynchronousFilllListener listener() {
        System.out.println(this.getClass().getName() + ".listener()");
        return new AsynchronousFilllListener() {
            //RequestContext context = c;

            @Override
            public void reportFinished(JasperPrint jp) {
                System.out.println(this.getClass().getName() + ".listener().reportFinished()");
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
                    System.out.println("ControlNReporteNomina reportFinished ERROR: " + e.toString());
                }
            }

            @Override
            public void reportCancelled() {
                System.out.println(this.getClass().getName() + ".listener().reportCancelled()");
                estadoReporte = true;
                resultadoReporte = "Cancelación";
            }

            @Override
            public void reportFillError(Throwable e) {
                System.out.println(this.getClass().getName() + ".listener().reportFillError()");
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

//    public void generarArchivoReporte(JasperPrint print) {
//        System.out.println(this.getClass().getName() + ".generarArchivoReporte()");
//        if (print != null && tipoReporte != null) {
//            pathReporteGenerado = administarReportes.crearArchivoReporte(print, tipoReporte);
//            validarDescargaReporte();
//        }
//    }
    public void exportarReporte() throws IOException {
        System.out.println(this.getClass().getName() + ".exportarReporte()");
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
            System.out.println("validar descarga reporte - ingreso al if 1 else");
            RequestContext.getCurrentInstance().update("formDialogos:errorGenerandoReporte");
            RequestContext.getCurrentInstance().execute("PF('errorGenerandoReporte').show()");
        }
    }

    public void validarDescargaReporte() {
        System.out.println(this.getClass().getName() + ".validarDescargaReporte()");
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().execute("PF('generandoReporte').hide()");
        if (pathReporteGenerado != null && !pathReporteGenerado.startsWith("Error:")) {
            System.out.println("validar descarga reporte - ingreso al if 1");
            if (!tipoReporte.equals("PDF")) {
                System.out.println("validar descarga reporte - ingreso al if 2");
                RequestContext.getCurrentInstance().execute("PF('descargarReporte').show()");
            } else {
                System.out.println("validar descarga reporte - ingreso al if 2 else");
                FileInputStream fis;
                try {
                    System.out.println("pathReporteGenerado : " + pathReporteGenerado);
                    fis = new FileInputStream(new File(pathReporteGenerado));
                    System.out.println("fis : " + fis);
                    reporte = new DefaultStreamedContent(fis, "application/pdf");
                } catch (FileNotFoundException ex) {
                    System.out.println("validar descarga reporte - ingreso al catch 1");
                    System.out.println(ex);
                    reporte = null;
                }
                if (reporte != null) {
                    System.out.println("validar descarga reporte - ingreso al if 3");
                    System.out.println("userAgent: " + userAgent);
                    System.out.println("userAgent.toUpperCase().contains(\"Mobile.toUpperCase(): " + userAgent.toUpperCase().contains("Mobile".toUpperCase()));
                    if (reporteSeleccionado != null) {
                        System.out.println("validar descarga reporte - ingreso al if 4");
                        if (userAgent.toUpperCase().contains("Mobile".toUpperCase()) || userAgent.toUpperCase().contains("Tablet".toUpperCase())) {
                            //System.out.println("Acceso por mobiles.");
                            context.update("formDialogos:descargarReporte");
                            context.execute("PF('descargarReporte').show();");
                        } else {
                            RequestContext.getCurrentInstance().update("formDialogos:verReportePDF");
                            RequestContext.getCurrentInstance().execute("PF('verReportePDF').show()");
                        }
                        cabezeraVisor = "Reporte - " + nombreReporte;
                    } else {
                        System.out.println("validar descarga reporte - ingreso al if 4 else ");
                        cabezeraVisor = "Reporte - ";
                    }
                }
//                pathReporteGenerado = null;
            }
        } else {
            System.out.println("validar descarga reporte - ingreso al if 1 else");
            RequestContext.getCurrentInstance().update("formDialogos:errorGenerandoReporte");
            RequestContext.getCurrentInstance().execute("PF('errorGenerandoReporte').show()");
        }
    }

    public void generarDocumentoReporte() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (reporteSeleccionado != null) {
            System.out.println("generando reporte - ingreso al if");
            nombreReporte = reporteSeleccionado.getNombrereporte();
            tipoReporte = reporteSeleccionado.getTipo();

            if (nombreReporte != null && tipoReporte != null) {
                System.out.println("generando reporte - ingreso al 2 if");
                pathReporteGenerado = administarReportes.generarReporte(nombreReporte, tipoReporte);
            }
            if (pathReporteGenerado != null) {
                System.out.println("generando reporte - ingreso al 3 if");
                validarDescargaReporte();
            } else {
                System.out.println("generando reporte - ingreso al 3 if else");
                RequestContext.getCurrentInstance().execute("PF('generandoReporte.hide();");
                RequestContext.getCurrentInstance().update("formDialogos:errorGenerandoReporte");
                RequestContext.getCurrentInstance().execute("PF('errorGenerandoReporte').show()");
            }
        } else {
            System.out.println("generando reporte - ingreso al if else");
            System.out.println("Reporte Seleccionado es nulo");
        }
    }

    public void dispararDialogoGuardarCambios() {
        System.out.println(this.getClass().getName() + ".dispararDialogoGuardarCambios()");
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");

    }

    public void cancelarReporte() {
        System.out.println(this.getClass().getName() + ".cancelarReporte()");
        administarReportes.cancelarReporte();
    }

    public void defaultPropiedadesParametrosReporte() {
        System.out.println(this.getClass().getName() + ".defaultPropiedadesParametrosReporte()");
        color = "black";
        decoracion = "none";
        color2 = "black";
        decoracion2 = "none";
    }

    public void reiniciarStreamedContent() {
        System.out.println(this.getClass().getName() + ".reiniciarStreamedContent()");
        reporte = null;
    }

    public void mostrarDialogoBuscarReporte() {
        System.out.println(this.getClass().getName() + ".mostrarDialogoBuscarReporte()");
        try {
            if (cambiosReporte == true) {
                contarRegistrosLovReportes();
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:ReportesDialogo");
                RequestContext.getCurrentInstance().execute("PF('ReportesDialogo').show()");
            } else {
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().execute("PF('confirmarGuardarSinSalida').show()");
            }
        } catch (Exception e) {
            System.out.println("Error mostrarDialogoBuscarReporte : " + e.toString());
        }
    }

    public void mostrarTodos() {
        if (cambiosReporte == true) {
            defaultPropiedadesParametrosReporte();
            listaIR.clear();
            for (int i = 0; i < listValInforeportes.size(); i++) {
                listaIR.add(listValInforeportes.get(i));
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
        System.out.println(this.getClass().getName() + ".cancelarRequisitosReporte()");
        requisitosReporte = "";
    }

    public void guardarYSalir() {
        System.out.println(this.getClass().getName() + ".guardarYSalir()");
        guardarCambios();
        salir();
    }

    public void activarAceptar() {
        System.out.println(this.getClass().getName() + ".activarAceptar()");
        aceptar = false;
    }

    public void actualizarEmplDesde() {
        System.out.println(this.getClass().getName() + ".actualizarEmplDesde()");
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
        filtrarListEmpleados = null;

    }

    public void cancelarCambioEmplDesde() {
        System.out.println(this.getClass().getName() + ".cancelarCambioEmplDesde()");
        empleadoSeleccionado = null;
        aceptar = true;
        filtrarListEmpleados = null;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovEmpleadoDesde:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovEmpleadoDesde').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('EmpleadoDesdeDialogo').hide()");
    }

    public void actualizarEmplHasta() {
        System.out.println(this.getClass().getName() + ".actualizarEmplHasta()");
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
        filtrarListEmpleados = null;
    }

    public void cancelarCambioEmplHasta() {
        System.out.println(this.getClass().getName() + ".cancelarCambioEmplHasta()");
        empleadoSeleccionado = null;
        aceptar = true;
        filtrarListEmpleados = null;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovEmpleadoHasta:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovEmpleadoHasta').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('EmpleadoHastaDialogo').hide()");
    }

    public void actualizarGrupo() {
        System.out.println(this.getClass().getName() + ".actualizarGrupo()");
        permitirIndex = true;
        parametroDeReporte.setGrupo(grupoCSeleccionado);
        parametroModificacion = parametroDeReporte;
        cambiosReporte = false;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovGruposConceptos:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovGruposConceptos').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('GruposConceptosDialogo').hide()");

        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("formParametros:grupoParametro");
        grupoCSeleccionado = null;
        aceptar = true;
        filtrarListGruposConceptos = null;

    }

    public void cancelarCambioGrupo() {
        System.out.println(this.getClass().getName() + ".cancelarCambioGrupo()");
        grupoCSeleccionado = null;
        aceptar = true;
        filtrarListGruposConceptos = null;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovGruposConceptos:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovGruposConceptos').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('GruposConceptosDialogo').hide()");
    }

    public void actualizarUbicacionGeografica() {
        System.out.println(this.getClass().getName() + ".actualizarUbicacionGeografica()");
        permitirIndex = true;
        parametroDeReporte.setUbicaciongeografica(ubicacionesGSeleccionado);
        parametroModificacion = parametroDeReporte;
        cambiosReporte = false;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovUbicacionGeografica:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovUbicacionGeografica').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('UbiGeograficaDialogo').hide()");

        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("formParametros:ubicacionGeograficaParametro");
        ubicacionesGSeleccionado = null;
        aceptar = true;
        filtrarListUbicacionesGeograficas = null;

    }

    public void cancelarCambioUbicacionGeografica() {
        System.out.println(this.getClass().getName() + ".cancelarCambioUbicacionGeografica()");
        ubicacionesGSeleccionado = null;
        aceptar = true;
        filtrarListUbicacionesGeograficas = null;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovUbicacionGeografica:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovUbicacionGeografica').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('UbiGeograficaDialogo').hide()");
    }

    public void actualizarTipoAsociacion() {
        System.out.println(this.getClass().getName() + ".actualizarTipoAsociacion()");
        permitirIndex = true;
        parametroDeReporte.setTipoasociacion(tiposASeleccionado);
        parametroModificacion = parametroDeReporte;
        cambiosReporte = false;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovTipoAsociacion:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovTipoAsociacion').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('TipoAsociacionDialogo').hide()");

        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("formParametros:tipoAsociacionParametro");
        tiposASeleccionado = null;
        aceptar = true;
        filtrarListTiposAsociaciones = null;

    }

    public void cancelarTipoAsociacion() {
        System.out.println(this.getClass().getName() + ".cancelarTipoAsociacion()");
        tiposASeleccionado = null;
        aceptar = true;
        filtrarListTiposAsociaciones = null;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovTipoAsociacion:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovTipoAsociacion').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('TipoAsociacionDialogo').hide()");
    }

    public void actualizarEmpresa() {
        System.out.println(this.getClass().getName() + ".actualizarEmpresa()");
        permitirIndex = true;
        parametroDeReporte.setEmpresa(empresaSeleccionada);
        parametroModificacion = parametroDeReporte;
        cambiosReporte = false;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovEmpresa:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovEmpresa').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('EmpresaDialogo').hide()");

        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("formParametros:empresaParametro");
        empresaSeleccionada = null;
        aceptar = true;
        filtrarListEmpresas = null;

    }

    public void cancelarEmpresa() {
        System.out.println(this.getClass().getName() + ".cancelarEmpresa()");
        empresaSeleccionada = null;
        aceptar = true;
        filtrarListEmpresas = null;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovEmpresa:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovEmpresa').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('EmpresaDialogo').hide()");
    }

    public void actualizarEstructura() {
        System.out.println(this.getClass().getName() + ".actualizarEstructura()");
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
        filtrarListEstructuras = null;
        permitirIndex = true;

    }

    public void cancelarEstructura() {
        System.out.println(this.getClass().getName() + ".cancelarEstructura()");
        estructuraSeleccionada = null;
        aceptar = true;
        filtrarListEstructuras = null;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovEstructura:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovEstructura').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('EstructuraDialogo').hide()");
    }

    public void actualizarTipoTrabajador() {
        System.out.println(this.getClass().getName() + ".actualizarTipoTrabajador()");
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
        filtrarListTiposTrabajadores = null;
        permitirIndex = true;

    }

    public void cancelarTipoTrabajador() {
        System.out.println(this.getClass().getName() + ".cancelarTipoTrabajador()");
        tipoTSeleccionado = null;
        aceptar = true;
        filtrarListTiposTrabajadores = null;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovTipoTrabajador:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovTipoTrabajador').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('TipoTrabajadorDialogo').hide()");
    }

    public void actualizarTercero() {
        System.out.println(this.getClass().getName() + ".actualizarTercero()");
        permitirIndex = true;
        parametroDeReporte.setTercero(terceroSeleccionado);
        parametroModificacion = parametroDeReporte;
        cambiosReporte = false;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovTercero:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovTercero').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('TerceroDialogo').hide()");

        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("formParametros:terceroParametro");
        terceroSeleccionado = null;
        aceptar = true;
        filtrarListTerceros = null;

    }

    public void cancelarTercero() {
        System.out.println(this.getClass().getName() + ".cancelarTercero()");
        terceroSeleccionado = null;
        aceptar = true;
        filtrarListTerceros = null;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovTercero:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovTercero').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('TerceroDialogo').hide()");
    }

    public void actualizarProceso() {
        System.out.println(this.getClass().getName() + ".actualizarProceso()");
        permitirIndex = true;
        parametroDeReporte.setProceso(procesoSeleccionado);
        parametroModificacion = parametroDeReporte;
        cambiosReporte = false;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovProceso:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovProceso').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('ProcesoDialogo').hide()");

        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("formParametros:procesoParametro");
        procesoSeleccionado = null;
        aceptar = true;
        filtrarListProcesos = null;

    }

    public void cancelarProceso() {
        System.out.println(this.getClass().getName() + ".cancelarProceso()");
        procesoSeleccionado = null;
        aceptar = true;
        filtrarListProcesos = null;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovProceso:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovProceso').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('ProcesoDialogo').hide()");
    }

    public void actualizarAsociacion() {
        System.out.println(this.getClass().getName() + ".actualizarAsociacion()");
        permitirIndex = true;
        parametroDeReporte.setAsociacion(asociacionSeleccionado);
        parametroModificacion = parametroDeReporte;
        cambiosReporte = false;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovAsociacion:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovAsociacion').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('AsociacionDialogo').hide()");

        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("formParametros:asociacionParametro");
        asociacionSeleccionado = null;
        aceptar = true;
        filtrarListAsociaciones = null;

    }

    public void cancelarAsociacion() {
        System.out.println(this.getClass().getName() + ".cancelarAsociacion()");
        asociacionSeleccionado = null;
        aceptar = true;
        filtrarListAsociaciones = null;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovAsociacion:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovAsociacion').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('AsociacionDialogo').hide()");
    }

    public void cancelarSeleccionInforeporte() {
        System.out.println(this.getClass().getName() + ".cancelarSeleccionInforeporte()");
        filtrarListIRU = null;
        reporteSeleccionadoLOV = null;
        aceptar = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovReportesDialogo:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovReportesDialogo').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('ReportesDialogo').hide()");
    }

    public void actualizarSeleccionInforeporte() {
        System.out.println(this.getClass().getName() + ".actualizarSeleccionInforeporte()");
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
        activoBuscarReporte = true;
        activoMostrarTodos = false;
        reporteSeleccionado = reporteSeleccionadoLOV;
        reporteSeleccionadoLOV = null;
        RequestContext.getCurrentInstance().update("form:MOSTRARTODOS");
        RequestContext.getCurrentInstance().update("form:BUSCARREPORTE");
        context.reset("form:lovReportesDialogo:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovReportesDialogo').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('ReportesDialogo').hide()");
        RequestContext.getCurrentInstance().update("form:reportesNomina");
        contarRegistros();
    }

    //AUTOCOMPLETAR
    public void autocompletarGeneral(String campoConfirmar, String valorConfirmar) {
        System.out.println(this.getClass().getName() + ".autocompletarGeneral()");
        RequestContext context = RequestContext.getCurrentInstance();
        int indiceUnicoElemento = -1;
        int coincidencias = 0;
        if (campoConfirmar.equalsIgnoreCase("GRUPO")) {
            if (!valorConfirmar.isEmpty()) {
                parametroDeReporte.getGrupo().setDescripcion(grupo);
                for (int i = 0; i < listValGruposConceptos.size(); i++) {
                    if (listValGruposConceptos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    parametroDeReporte.setGrupo(listValGruposConceptos.get(indiceUnicoElemento));
                    parametroModificacion = parametroDeReporte;
                    listValGruposConceptos.clear();
                    getListValGruposConceptos();
                    cambiosReporte = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                } else {
                    permitirIndex = false;
                    RequestContext.getCurrentInstance().update("form:GruposConceptosDialogo");
                    RequestContext.getCurrentInstance().execute("PF('GruposConceptosDialogo').show()");
                }
            } else {
                parametroDeReporte.setGrupo(new GruposConceptos());
                parametroModificacion = parametroDeReporte;
                listValGruposConceptos.clear();
                getListValGruposConceptos();
                cambiosReporte = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        }
        if (campoConfirmar.equalsIgnoreCase("UBIGEO")) {
            if (!valorConfirmar.isEmpty()) {
                parametroDeReporte.getUbicaciongeografica().setDescripcion(ubiGeo);
                for (int i = 0; i < listValUbicacionesGeograficas.size(); i++) {
                    if (listValUbicacionesGeograficas.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    parametroDeReporte.setUbicaciongeografica(listValUbicacionesGeograficas.get(indiceUnicoElemento));
                    parametroModificacion = parametroDeReporte;
                    listValUbicacionesGeograficas.clear();
                    getListValUbicacionesGeograficas();
                    cambiosReporte = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                } else {
                    permitirIndex = false;
                    RequestContext.getCurrentInstance().update("form:UbiGeograficaDialogo");
                    RequestContext.getCurrentInstance().execute("PF('UbiGeograficaDialogo').show()");
                }
            } else {
                parametroDeReporte.setUbicaciongeografica(new UbicacionesGeograficas());
                parametroModificacion = parametroDeReporte;
                listValUbicacionesGeograficas.clear();
                getListValUbicacionesGeograficas();
                cambiosReporte = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        }
        if (campoConfirmar.equalsIgnoreCase("EMPRESA")) {
            if (!valorConfirmar.isEmpty()) {
                parametroDeReporte.getEmpresa().setNombre(empresa);
                for (int i = 0; i < listValEmpresas.size(); i++) {
                    if (listValEmpresas.get(i).getNombre().startsWith(valorConfirmar)) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    parametroDeReporte.setEmpresa(listValEmpresas.get(indiceUnicoElemento));
                    parametroModificacion = parametroDeReporte;
                    listValEmpresas.clear();
                    getListValEmpresas();
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
                listValEmpresas.clear();
                getListValEmpresas();
                cambiosReporte = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        }
        if (campoConfirmar.equalsIgnoreCase("TIPOASO")) {
            if (!valorConfirmar.isEmpty()) {
                parametroDeReporte.getTipoasociacion().setDescripcion(tipoAso);
                for (int i = 0; i < listValTiposAsociaciones.size(); i++) {
                    if (listValTiposAsociaciones.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    parametroDeReporte.setTipoasociacion(listValTiposAsociaciones.get(indiceUnicoElemento));
                    parametroModificacion = parametroDeReporte;
                    listValTiposAsociaciones.clear();
                    getListValTiposAsociaciones();
                    cambiosReporte = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                } else {
                    permitirIndex = false;
                    RequestContext.getCurrentInstance().update("form:TipoAsociacionDialogo");
                    RequestContext.getCurrentInstance().execute("PF('TipoAsociacionDialogo').show()");
                }
            } else {
                parametroDeReporte.setTipoasociacion(new TiposAsociaciones());
                parametroModificacion = parametroDeReporte;
                listValTiposAsociaciones.clear();
                getListValTiposAsociaciones();
                cambiosReporte = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        }
        if (campoConfirmar.equalsIgnoreCase("ESTRUCTURA")) {
            if (!valorConfirmar.isEmpty()) {
                parametroDeReporte.getLocalizacion().setNombre(estructura);
                for (int i = 0; i < listValEstructuras.size(); i++) {
                    if (listValEstructuras.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    parametroDeReporte.setLocalizacion(listValEstructuras.get(indiceUnicoElemento));
                    parametroModificacion = parametroDeReporte;
                    listValEstructuras.clear();
                    getListValEstructuras();
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
                cambiosReporte = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        }
        if (campoConfirmar.equalsIgnoreCase("TIPOTRABAJADOR")) {
            if (!valorConfirmar.isEmpty()) {
                parametroDeReporte.getTipotrabajador().setNombre(tipoTrabajador);
                for (int i = 0; i < listValTiposTrabajadores.size(); i++) {
                    if (listValTiposTrabajadores.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    parametroDeReporte.setTipotrabajador(listValTiposTrabajadores.get(indiceUnicoElemento));
                    parametroModificacion = parametroDeReporte;
                    listValTiposTrabajadores.clear();
                    getListValTiposTrabajadores();
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
                cambiosReporte = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        }
        if (campoConfirmar.equalsIgnoreCase("TERCERO")) {
            if (!valorConfirmar.isEmpty()) {
                parametroDeReporte.getTercero().setNombre(tercero);
                for (int i = 0; i < listValTerceros.size(); i++) {
                    if (listValTerceros.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    parametroDeReporte.setTercero(listValTerceros.get(indiceUnicoElemento));
                    parametroModificacion = parametroDeReporte;
                    listValTerceros.clear();
                    getListValTerceros();
                    cambiosReporte = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                } else {
                    permitirIndex = false;
                    RequestContext.getCurrentInstance().update("form:TerceroDialogo");
                    RequestContext.getCurrentInstance().execute("PF('TerceroDialogo').show()");
                }
            } else {
                parametroDeReporte.setTercero(new Terceros());
                parametroModificacion = parametroDeReporte;
                listValTerceros.clear();
                getListValTerceros();
                cambiosReporte = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        }
        if (campoConfirmar.equalsIgnoreCase("PROCESO")) {
            if (!valorConfirmar.isEmpty()) {
                parametroDeReporte.getProceso().setDescripcion(proceso);
                for (int i = 0; i < listValProcesos.size(); i++) {
                    if (listValProcesos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    parametroDeReporte.setProceso(listValProcesos.get(indiceUnicoElemento));
                    parametroModificacion = parametroDeReporte;
                    listValProcesos.clear();
                    getListValProcesos();
                    cambiosReporte = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                } else {
                    permitirIndex = false;
                    RequestContext.getCurrentInstance().update("form:ProcesoDialogo");
                    RequestContext.getCurrentInstance().execute("PF('ProcesoDialogo').show()");
                }
            } else {
                parametroDeReporte.setProceso(new Procesos());
                parametroModificacion = parametroDeReporte;
                listValProcesos.clear();
                getListValProcesos();
                cambiosReporte = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        }
        if (campoConfirmar.equalsIgnoreCase("ASOCIACION")) {
            if (!valorConfirmar.isEmpty()) {
                parametroDeReporte.getAsociacion().setDescripcion(asociacion);
                for (int i = 0; i < listValAsociaciones.size(); i++) {
                    if (listValAsociaciones.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    parametroDeReporte.setAsociacion(listValAsociaciones.get(indiceUnicoElemento));
                    parametroModificacion = parametroDeReporte;
                    listValAsociaciones.clear();
                    getListValAsociaciones();
                    cambiosReporte = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                } else {
                    permitirIndex = false;
                    RequestContext.getCurrentInstance().update("form:AsociacionDialogo");
                    RequestContext.getCurrentInstance().execute("PF('AsociacionDialogo').show()");
                }
            } else {
                parametroDeReporte.setAsociacion(new Asociaciones());
                parametroModificacion = parametroDeReporte;
                listValAsociaciones.clear();
                getListValAsociaciones();
                cambiosReporte = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        }
//        if (campoConfirmar.equalsIgnoreCase("DESDE")) {
//            System.out.println("valorConfirmar " + valorConfirmar);
//            if (!valorConfirmar.isEmpty() || !valorConfirmar.equals(0)) {
//                System.out.println("Entre a if (!valorConfirmar.isEmpty() || !\"0\".equals(valorConfirmar))");
//                parametroDeReporte.getCodigoempleadodesde();
//                for (int i = 0; i < listValEmpleados.size(); i++) {
//                    if (listValEmpleados.get(i).getCodigoempleadoSTR().startsWith(valorConfirmar.toUpperCase())) {
//                        indiceUnicoElemento = i;
//                        coincidencias++;
//                    }
//                }
//                System.out.println("Coincidencias: " + coincidencias);
//                if (coincidencias == 1) {
//                    System.out.println("Entre a if (coincidencias == 1)");
//                    parametroDeReporte.setCodigoempleadodesde(listValEmpleados.get(indiceUnicoElemento).getCodigoempleado());
//                    parametroModificacion = parametroDeReporte;
//                    listValEmpleados.clear();
//                    getListValEmpleados();
//                    cambiosReporte = false;
//                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
//                } else {
//                    System.out.println("Entre al else");
//                    permitirIndex = false;
//                    RequestContext.getCurrentInstance().update("form:EmpleadoDesdeDialogo");
//                    RequestContext.getCurrentInstance().execute("PF('EmpleadoDesdeDialogo').show()");
//                }
//            } else if (valorConfirmar.equals(0)) {
//                parametroDeReporte.setCodigoempleadodesde(new BigDecimal(0));
//                parametroModificacion = parametroDeReporte;
//                //         listValEmpleados.clear();
////          getListValEmpleados();
//                cambiosReporte = false;
//                RequestContext.getCurrentInstance().update("form:ACEPTAR");
//            }
//        }
//        if (campoConfirmar.equalsIgnoreCase("HASTA")) {
//            System.out.println("valorConfirmar " + valorConfirmar);
//            if (!valorConfirmar.isEmpty() && !valorConfirmar.equals("999999999999999999999999999999999")) {
//                System.out.println("Entre a if (!valorConfirmar.isEmpty() || !\"999999999999999999999999999999999\".equals(valorConfirmar))");
//                parametroDeReporte.getCodigoempleadohasta();
//                for (int i = 0; i < listValEmpleados.size(); i++) {
//                    if (listValEmpleados.get(i).getCodigoempleadoSTR().startsWith(valorConfirmar.toUpperCase())) {
//                        indiceUnicoElemento = i;
//                        coincidencias++;
//                    }
//                }
//                System.out.println("Coincidencias: " + coincidencias);
//                if (coincidencias == 1) {
//                    System.out.println("Entre a if (coincidencias == 1 && 0.equals(valorConfirmar))");
//                    parametroDeReporte.setCodigoempleadohasta(listValEmpleados.get(indiceUnicoElemento).getCodigoempleado());
//                    parametroModificacion = parametroDeReporte;
//                    listValEmpleados.clear();
//                    getListValEmpleados();
//                    cambiosReporte = false;
//                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
//                } else {
//                    permitirIndex = false;
//                    RequestContext.getCurrentInstance().update("form:EmpleadoHastaDialogo");
//                    RequestContext.getCurrentInstance().execute("PF('EmpleadoHastaDialogo').show()");
//                }
//            } else if (valorConfirmar.equals("999999999999999999999999999999999")) {
//                System.out.println("Entre al else en  ControlNReporteNomina.autocompletarGeneral");
//                parametroDeReporte.setCodigoempleadohasta(new BigDecimal("999999999999999999999999999999999"));
//                parametroModificacion = parametroDeReporte;
//                cambiosReporte = false;
//                RequestContext.getCurrentInstance().update("form:ACEPTAR");
//            }
//
//        }

    }

    public void modificarParametroEmpleadoDesde(BigDecimal empldesde) {
        if (empldesde.equals(BigDecimal.valueOf(0))) {
            parametroDeReporte.setCodigoempleadodesde(BigDecimal.valueOf(0));
        }
        cambiosReporte = false;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void modificarParametroEmpleadoHasta(BigDecimal emphasta) {
        String h = "99999999999999999999999999";
        BigDecimal b = new BigDecimal(h);
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
            if (casilla == 1) {
                deshabilitarBotonLov();
                fechaDesde = parametroDeReporte.getFechadesde();
            } else if (casilla == 2) {
                habilitarBotonLov();
                emplDesde = parametroDeReporte.getCodigoempleadodesde();
            } else if (casilla == 4) {
                habilitarBotonLov();
                grupo = parametroDeReporte.getGrupo().getDescripcion();
            } else if (casilla == 5) {
                habilitarBotonLov();
                ubiGeo = parametroDeReporte.getUbicaciongeografica().getDescripcion();
            } else if (casilla == 6) {
                habilitarBotonLov();
                tipoAso = parametroDeReporte.getTipoasociacion().getDescripcion();
            } else if (casilla == 7) {
                deshabilitarBotonLov();
                fechaHasta = parametroDeReporte.getFechahasta();
            } else if (casilla == 8) {
                habilitarBotonLov();
                emplHasta = parametroDeReporte.getCodigoempleadohasta();
            } else if (casilla == 10) {
                habilitarBotonLov();
                empresa = parametroDeReporte.getEmpresa().getNombre();
            } else if (casilla == 11) {
                habilitarBotonLov();
                estructura = parametroDeReporte.getLocalizacion().getNombre();
            } else if (casilla == 12) {
                habilitarBotonLov();
                tipoTrabajador = parametroDeReporte.getTipotrabajador().getNombre();
            } else if (casilla == 13) {
                habilitarBotonLov();
                tercero = parametroDeReporte.getTercero().getNombre();
            } else if (casilla == 14) {
                habilitarBotonLov();
                proceso = parametroDeReporte.getProceso().getDescripcion();
            } else if (casilla == 15) {
                deshabilitarBotonLov();
                parametroDeReporte.getMensajedesprendible();
            } else if (casilla == 16) {
                habilitarBotonLov();
                asociacion = parametroDeReporte.getAsociacion().getDescripcion();
            }

        }
    }

    //DIALOGOS PARAMETROS
    public void dialogosParametros(int pos) {
        System.out.println(this.getClass().getName() + ".dialogosParametros()");
        RequestContext context = RequestContext.getCurrentInstance();
        if (pos == 2) {
            listValEmpleados = null;
            cargarListaEmpleados();
            contarRegistrosEmpeladoD();
            RequestContext.getCurrentInstance().update("form:EmpleadoDesdeDialogo");
            RequestContext.getCurrentInstance().execute("PF('EmpleadoDesdeDialogo').show()");
        }
        if (pos == 4) {
            listValGruposConceptos = null;
            cargarListaGruposConcepto();
            RequestContext.getCurrentInstance().update("form:GruposConceptosDialogo");
            RequestContext.getCurrentInstance().execute("PF('GruposConceptosDialogo').show()");
            contarRegistrosGrupo();
        }
        if (pos == 5) {
            listValUbicacionesGeograficas = null;
            cargarListaUbicacionesGeograficas();
            RequestContext.getCurrentInstance().update("form:UbiGeograficaDialogo");
            RequestContext.getCurrentInstance().execute("PF('UbiGeograficaDialogo').show()");
            contarRegistrosUbicacion();
        }
        if (pos == 6) {
            listValTiposAsociaciones = null;
            getListValTiposAsociaciones();
            RequestContext.getCurrentInstance().update("form:TipoAsociacionDialogo");
            RequestContext.getCurrentInstance().execute("PF('TipoAsociacionDialogo').show()");
            contarRegistrosTipoAsociacion();
        }
        if (pos == 8) {
            listValEmpleados = null;
            cargarListaEmpleados();
            contarRegistrosEmpeladoH();
            RequestContext.getCurrentInstance().update("form:EmpleadoHastaDialogo");
            RequestContext.getCurrentInstance().execute("PF('EmpleadoHastaDialogo').show()");
        }
        if (pos == 10) {
            listValEmpresas = null;
            getListValEmpresas();
            RequestContext.getCurrentInstance().update("form:EmpresaDialogo");
            RequestContext.getCurrentInstance().execute("PF('EmpresaDialogo').show()");
            contarRegistrosEmpresa();
        }
        if (pos == 11) {
            listValEstructuras = null;
            cargarListaEstructuras();
            RequestContext.getCurrentInstance().update("form:EstructuraDialogo");
            RequestContext.getCurrentInstance().execute("PF('EstructuraDialogo').show()");
            contarRegistrosEstructura();
        }
        if (pos == 12) {
            listValTiposTrabajadores = null;
            cargarListaTiposTrabajadores();
            RequestContext.getCurrentInstance().update("form:TipoTrabajadorDialogo");
            RequestContext.getCurrentInstance().execute("PF('TipoTrabajadorDialogo').show()");
            contarRegistrosTipoTrabajador();
        }
        if (pos == 13) {
            listValTerceros = null;
            cargarListaTerceros();
            RequestContext.getCurrentInstance().update("form:TerceroDialogo");
            RequestContext.getCurrentInstance().execute("PF('TerceroDialogo').show()");
            contarRegistrosTercero();
        }
        if (pos == 14) {
            listValProcesos = null;
            cargarListaProcesos();
            RequestContext.getCurrentInstance().update("form:ProcesoDialogo");
            RequestContext.getCurrentInstance().execute("PF('ProcesoDialogo').show()");
            contarRegistrosProceso();
        }
        if (pos == 16) {
            listValAsociaciones = null;
            cargarListaAsociaciones();
            RequestContext.getCurrentInstance().update("form:AsociacionDialogo");
            RequestContext.getCurrentInstance().execute("PF('AsociacionDialogo').show()");
            contarRegistrosAsociacion();
        }

    }

    //MODIFICAR PARAMETRO REPORTE
    public void modificarParametroInforme() {
        System.out.println("Controlador.ControlNReporteNomina.modificarParametroInforme()");
        System.out.println("parametroDeReporte.getFechadesde(): " + parametroDeReporte.getFechadesde());
        System.out.println("parametroDeReporte.getFechahasta(): " + parametroDeReporte.getFechahasta());
        if (parametroDeReporte.getFechadesde() != null && parametroDeReporte.getFechahasta() != null) {
            if (parametroDeReporte.getFechadesde().before(parametroDeReporte.getFechahasta())) {
                System.out.println("Entre al segundo if");
                parametroModificacion = parametroDeReporte;
                cambiosReporte = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            } else {
                System.out.println("entre a else del segundo if");
                cambiosReporte = true;
            }
        }
    }

    public void mostrarDialogoGenerarReporte() {
        System.out.println(this.getClass().getName() + ".mostrarDialogoGenerarReporte()");
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("formDialogos:reporteAGenerar");
        RequestContext.getCurrentInstance().execute("PF('reporteAGenerar').show()");
    }

    public void cancelarGenerarReporte() {
        System.out.println(this.getClass().getName() + ".cancelarGenerarReporte()");
        reporteGenerar = "";
        posicionReporte = -1;
    }

    //CONTARREGISTROS
    public void contarRegistros() {
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
    }

    public void contarRegistrosLovReportes() {
        RequestContext.getCurrentInstance().update("form:infoRegistroReportes");
    }

    public void contarRegistrosEmpeladoD() {
        RequestContext.getCurrentInstance().update("form:infoRegistroEmpleadoDesde");
    }

    public void contarRegistrosEmpeladoH() {
        RequestContext.getCurrentInstance().update("form:infoRegistroEmpleadoHasta");
    }

    public void contarRegistrosGrupo() {
        RequestContext.getCurrentInstance().update("form:infoRegistroGrupoConcepto");
    }

    public void contarRegistrosUbicacion() {
        RequestContext.getCurrentInstance().update("form:infoRegistroUbicacion");
    }

    public void contarRegistrosTipoAsociacion() {
        RequestContext.getCurrentInstance().update("form:infoRegistroTipoAsociacion");
    }

    public void contarRegistrosEmpresa() {
        RequestContext.getCurrentInstance().update("form:infoRegistroEmpresa");
    }

    public void contarRegistrosAsociacion() {
        RequestContext.getCurrentInstance().update("form:infoRegistroAsociacion");
    }

    public void contarRegistrosTercero() {
        RequestContext.getCurrentInstance().update("form:infoRegistroTercero");
    }

    public void contarRegistrosProceso() {
        RequestContext.getCurrentInstance().update("form:infoRegistroProceso");
    }

    public void contarRegistrosEstructura() {
        RequestContext.getCurrentInstance().update("form:infoRegistroEstructura");
    }

    public void contarRegistrosTipoTrabajador() {
        RequestContext.getCurrentInstance().update("form:infoRegistroTipoTrabajador");

    }

    private static abstract class FacesContextWrapper extends FacesContext {

        protected static void setCurrentInstance(FacesContext facesContext) {
            FacesContext.setCurrentInstance(facesContext);
        }
    }

    public void cargarListaEmpleados() {
        if (listValEmpleados == null) {
            listValEmpleados = administrarNReportesNomina.listEmpleados();
        }
    }

    public void cargarListaGruposConcepto() {
        if (listValGruposConceptos == null || listValGruposConceptos.isEmpty()) {
            listValGruposConceptos = administrarNReportesNomina.listGruposConcetos();
        }
    }

    public void cargarListaUbicacionesGeograficas() {
        if (listValUbicacionesGeograficas == null || listValUbicacionesGeograficas.isEmpty()) {
            listValUbicacionesGeograficas = administrarNReportesNomina.listUbicacionesGeograficas();
        }
    }

    public void cargarListaEstructuras() {
        if (listValEstructuras == null || listValEstructuras.isEmpty()) {
            listValEstructuras = administrarNReportesNomina.listEstructuras();
        }
    }

    public void cargarListaTiposTrabajadores() {
        if (listValTiposTrabajadores == null || listValTiposTrabajadores.isEmpty()) {
            listValTiposTrabajadores = administrarNReportesNomina.listTiposTrabajadores();
        }
    }

    public void cargarListaTerceros() {
        System.out.println("Controlador.ControlNReporteNomina.cargarListaTerceros()");
        if (listValTerceros == null || listValTerceros.isEmpty()) {
            if (empresa != null) {
                listValTerceros = administrarNReportesNomina.listTercerosSecEmpresa(this.getParametroDeInforme().getEmpresa().getSecuencia());
            } else {
                listValTerceros = administrarNReportesNomina.listTerceros();
            }
        }
    }

    public void cargarListaProcesos() {
        if (listValProcesos == null || listValProcesos.isEmpty()) {
            listValProcesos = administrarNReportesNomina.listProcesos();
        }
    }

    public void cargarListaAsociaciones() {
        if (listValAsociaciones == null || listValAsociaciones.isEmpty()) {
            listValAsociaciones = administrarNReportesNomina.listAsociaciones();
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
            System.out.println("Error getParametroDeInforme : " + e);
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
            System.out.println("Error getListInforeportesUsuario : " + e);
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

    public List<Empleados> getListValEmpleados() {
        return listValEmpleados;
    }

    public void setListValEmpleados(List<Empleados> listEmpleados) {
        this.listValEmpleados = listEmpleados;
    }

    public List<Empresas> getListValEmpresas() {
        if (listValEmpresas == null) {
            listValEmpresas = administrarNReportesNomina.listEmpresas();
        }
        return listValEmpresas;
    }

    public void setListValEmpresas(List<Empresas> listEmpresas) {
        this.listValEmpresas = listEmpresas;
    }

    public List<GruposConceptos> getListValGruposConceptos() {
        return listValGruposConceptos;
    }

    public void setListValGruposConceptos(List<GruposConceptos> listGruposConceptos) {
        this.listValGruposConceptos = listGruposConceptos;
    }

    public List<UbicacionesGeograficas> getListValUbicacionesGeograficas() {
        return listValUbicacionesGeograficas;
    }

    public void setListValUbicacionesGeograficas(List<UbicacionesGeograficas> listUbicacionesGeograficas) {
        this.listValUbicacionesGeograficas = listUbicacionesGeograficas;
    }

    public List<TiposAsociaciones> getListValTiposAsociaciones() {
        if (listValTiposAsociaciones == null) {
            listValTiposAsociaciones = administrarNReportesNomina.listTiposAsociaciones();
        }
        return listValTiposAsociaciones;
    }

    public void setListValTiposAsociaciones(List<TiposAsociaciones> listTiposAsociaciones) {
        this.listValTiposAsociaciones = listTiposAsociaciones;
    }

    public List<Estructuras> getListValEstructuras() {
        return listValEstructuras;
    }

    public void setListValEstructuras(List<Estructuras> listEstructuras) {
        this.listValEstructuras = listEstructuras;
    }

    public List<TiposTrabajadores> getListValTiposTrabajadores() {
        return listValTiposTrabajadores;
    }

    public void setListValTiposTrabajadores(List<TiposTrabajadores> listTiposTrabajadores) {
        this.listValTiposTrabajadores = listTiposTrabajadores;
    }

    public List<Terceros> getListValTerceros() {
        return listValTerceros;
    }

    public void setListValTerceros(List<Terceros> listTerceros) {
        this.listValTerceros = listTerceros;
    }

    public List<Procesos> getListValProcesos() {
        return listValProcesos;
    }

    public void setListValProcesos(List<Procesos> listProcesos) {
        this.listValProcesos = listProcesos;
    }

    public List<Asociaciones> getListValAsociaciones() {
        return listValAsociaciones;
    }

    public void setListValAsociaciones(List<Asociaciones> listAsociaciones) {
        this.listValAsociaciones = listAsociaciones;
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

    public List<Empleados> getFiltrarListEmpleados() {
        return filtrarListEmpleados;
    }

    public void setFiltrarListEmpleados(List<Empleados> filtrarListEmpleados) {
        this.filtrarListEmpleados = filtrarListEmpleados;
    }

    public List<Empresas> getFiltrarListEmpresas() {
        return filtrarListEmpresas;
    }

    public void setFiltrarListEmpresas(List<Empresas> filtrarListEmpresas) {
        this.filtrarListEmpresas = filtrarListEmpresas;
    }

    public List<GruposConceptos> getFiltrarListGruposConceptos() {
        return filtrarListGruposConceptos;
    }

    public void setFiltrarListGruposConceptos(List<GruposConceptos> filtrarListGruposConceptos) {
        this.filtrarListGruposConceptos = filtrarListGruposConceptos;
    }

    public List<UbicacionesGeograficas> getFiltrarListUbicacionesGeograficas() {
        return filtrarListUbicacionesGeograficas;
    }

    public void setFiltrarListUbicacionesGeograficas(List<UbicacionesGeograficas> filtrarListUbicacionesGeograficas) {
        this.filtrarListUbicacionesGeograficas = filtrarListUbicacionesGeograficas;
    }

    public List<TiposAsociaciones> getFiltrarListTiposAsociaciones() {
        return filtrarListTiposAsociaciones;
    }

    public void setFiltrarListTiposAsociaciones(List<TiposAsociaciones> filtrarListTiposAsociaciones) {
        this.filtrarListTiposAsociaciones = filtrarListTiposAsociaciones;
    }

    public List<Estructuras> getFiltrarListEstructuras() {
        return filtrarListEstructuras;
    }

    public void setFiltrarListEstructuras(List<Estructuras> filtrarListEstructuras) {
        this.filtrarListEstructuras = filtrarListEstructuras;
    }

    public List<Terceros> getFiltrarListTerceros() {
        return filtrarListTerceros;
    }

    public void setFiltrarListTerceros(List<Terceros> filtrarListTerceros) {
        this.filtrarListTerceros = filtrarListTerceros;
    }

    public List<Procesos> getFiltrarListProcesos() {
        return filtrarListProcesos;
    }

    public void setFiltrarListProcesos(List<Procesos> filtrarListProcesos) {
        this.filtrarListProcesos = filtrarListProcesos;
    }

    public List<Asociaciones> getFiltrarListAsociaciones() {
        return filtrarListAsociaciones;
    }

    public void setFiltrarListAsociaciones(List<Asociaciones> filtrarListAsociaciones) {
        this.filtrarListAsociaciones = filtrarListAsociaciones;
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

    public List<TiposTrabajadores> getFiltrarListTiposTrabajadores() {
        return filtrarListTiposTrabajadores;
    }

    public void setFiltrarListTiposTrabajadores(List<TiposTrabajadores> filtrarListTiposTrabajadores) {
        this.filtrarListTiposTrabajadores = filtrarListTiposTrabajadores;
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

    public String getInfoRegistroGrupoConcepto() {
        FacesContext c = FacesContext.getCurrentInstance();
        tabla = (DataTable) c.getViewRoot().findComponent("form:lovGruposConceptos");
        infoRegistroGrupoConcepto = String.valueOf(tabla.getRowCount());
        return infoRegistroGrupoConcepto;
    }

    public String getInfoRegistroUbicacion() {
        FacesContext c = FacesContext.getCurrentInstance();
        tabla = (DataTable) c.getViewRoot().findComponent("form:lovUbicacionGeografica");
        infoRegistroUbicacion = String.valueOf(tabla.getRowCount());
        return infoRegistroUbicacion;
    }

    public String getInfoRegistroTipoAsociacion() {
        FacesContext c = FacesContext.getCurrentInstance();
        tabla = (DataTable) c.getViewRoot().findComponent("form:lovTipoAsociacion");
        infoRegistroTipoAsociacion = String.valueOf(tabla.getRowCount());
        return infoRegistroTipoAsociacion;
    }

    public String getInfoRegistroEmpresa() {
        FacesContext c = FacesContext.getCurrentInstance();
        tabla = (DataTable) c.getViewRoot().findComponent("form:lovEmpresa");
        infoRegistroEmpresa = String.valueOf(tabla.getRowCount());
        return infoRegistroEmpresa;
    }

    public String getInfoRegistroAsociacion() {
        FacesContext c = FacesContext.getCurrentInstance();
        tabla = (DataTable) c.getViewRoot().findComponent("form:lovAsociacion");
        infoRegistroAsociacion = String.valueOf(tabla.getRowCount());
        return infoRegistroAsociacion;
    }

    public String getInfoRegistroTercero() {
        FacesContext c = FacesContext.getCurrentInstance();
        tabla = (DataTable) c.getViewRoot().findComponent("form:lovTercero");
        infoRegistroTercero = String.valueOf(tabla.getRowCount());
        return infoRegistroTercero;
    }

    public String getInfoRegistroProceso() {
        FacesContext c = FacesContext.getCurrentInstance();
        tabla = (DataTable) c.getViewRoot().findComponent("form:lovProceso");
        infoRegistroProceso = String.valueOf(tabla.getRowCount());
        return infoRegistroProceso;
    }

    public String getInfoRegistroTipoTrabajador() {
        FacesContext c = FacesContext.getCurrentInstance();
        tabla = (DataTable) c.getViewRoot().findComponent("form:lovTipoTrabajador");
        infoRegistroTipoTrabajador = String.valueOf(tabla.getRowCount());
        return infoRegistroTipoTrabajador;
    }

    public String getInfoRegistroEstructura() {
        FacesContext c = FacesContext.getCurrentInstance();
        tabla = (DataTable) c.getViewRoot().findComponent("form:lovEstructura");
        infoRegistroEstructura = String.valueOf(tabla.getRowCount());
        return infoRegistroEstructura;
    }

    public String getInfoRegistroReportes() {
        FacesContext c = FacesContext.getCurrentInstance();
        tabla = (DataTable) c.getViewRoot().findComponent("form:lovReportesDialogo");
        infoRegistroLovReportes = String.valueOf(tabla.getRowCount());
        return infoRegistroLovReportes;
    }

    public List<Inforeportes> getListValInforeportes() {
        if (listValInforeportes == null || listValInforeportes.isEmpty()) {
            listValInforeportes = administrarNReportesNomina.listInforeportesUsuario();
        }
        return listValInforeportes;
    }

    public void setListValInforeportes(List<Inforeportes> lovInforeportes) {
        this.listValInforeportes = lovInforeportes;
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

}
