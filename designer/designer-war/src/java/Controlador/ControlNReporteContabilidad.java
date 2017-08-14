/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Empleados;
import Entidades.Empresas;
import Entidades.Inforeportes;
import Entidades.ParametrosReportes;
import Entidades.Procesos;
import InterfaceAdministrar.AdministarReportesInterface;
import InterfaceAdministrar.AdministrarInforeportesInterface;
import InterfaceAdministrar.AdministrarNReporteContabilidadInterface;
import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import ControlNavegacion.ControlListaNavegacion;
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
public class ControlNReporteContabilidad implements Serializable {

   private static Logger log = Logger.getLogger(ControlNReporteContabilidad.class);

    @EJB
    AdministrarNReporteContabilidadInterface administrarNReporteContabilidad;
    @EJB
    AdministarReportesInterface administarReportes;
    @EJB
    AdministrarInforeportesInterface administrarInforeportes;
    ///
    private ParametrosReportes parametroDeReporte;
    private ParametrosReportes parametroModificacion;
    private ParametrosReportes parametroFecha;
//
    private List<Inforeportes> listaIR;
    private List<Inforeportes> listaInfoReportesModificados;
    private List<Inforeportes> filtrarListInforeportesUsuario;
    private Inforeportes inforreporteSeleccionado;
    //LOV
    private List<Inforeportes> lovInforeportes;
    private Inforeportes reporteLovSeleccionado;
    private List<Inforeportes> filtrarLovInforeportes;
    private List<Inforeportes> filtrarReportes;
    //
    private String reporteGenerar;
    private String requisitosReporte;
    private String proceso, empresa;
    private String altoTabla;
    private String color, decoracion;
    private String color2, decoracion2;
    private String infoRegistroProceso, infoRegistroEmpleadoDesde, infoRegistroEmpleadoHasta, infoRegistroEmpresa, infoRegistroReportes;
    private String infoRegistro;
    private String nombreReporte, tipoReporte;
    private String pathReporteGenerado;
    private String cabezeraVisor;
    //
    private int bandera;
    private int casilla;
    private int posicionReporte;
    private int casillaInforReporte;
    private int tipoLista;
    //
    private boolean aceptar, cambiosReporte;
    private boolean permitirIndex;
    private boolean activoMostrarTodos, activoBuscarReporte, activarEnvio;
    //
    private Column codigoIR, reporteIR;
    private SelectCheckboxMenu tipoIR;
    //
    private InputText empleadoDesdeParametroL, empleadoHastaParametroL;
    //
    private List<Empleados> lovEmpleados;
    private List<Empleados> lovEmpleadosFiltrar;
    private Empleados empleadoSeleccionado;
    //
    private List<Procesos> lovProcesos;
    private Procesos procesoSeleccionado;
    private List<Procesos> lovProcesosFiltrar;
    //
    private List<Empresas> lovEmpresas;
    private Empresas empresaSeleccionada;
    private List<Empresas> lovEmpresasFiltrar;
    //
    private SelectOneMenu estadoParametro;
    //EXPORTAR REPORTE
    private StreamedContent file;
    //Listener reporte
    private AsynchronousFilllListener asistenteReporte;
    //
    private Date fechaDesde, fechaHasta;

    private BigDecimal emplDesde, emplHasta;
    //
    private DataTable tabla;
//
    private boolean estadoReporte;
    private String resultadoReporte;
    private String paginaAnterior = "nominaf";
    private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();
    private ExternalContext externalContext;
    private String userAgent;
    private boolean activarLov;
    private Map<BigInteger, Object> mapTipos = new LinkedHashMap<>();

    public ControlNReporteContabilidad() {
        activoMostrarTodos = true;
        activoBuscarReporte = false;
        activarEnvio = true;
        color = "black";
        decoracion = "none";
        color2 = "black";
        decoracion2 = "none";
        casillaInforReporte = -1;
        inforreporteSeleccionado = null;
        cambiosReporte = true;
        listaInfoReportesModificados = new ArrayList<>();
        altoTabla = "200";
        parametroDeReporte = null;
        listaIR = null;
        bandera = 0;
        aceptar = true;
        casilla = -1;
        parametroModificacion = new ParametrosReportes();
        parametroFecha = new ParametrosReportes();
        tipoLista = 0;
        reporteGenerar = "";
        requisitosReporte = "";
        posicionReporte = -1;
        //
        lovEmpleados = null;
        lovEmpresas = null;
        //
        empleadoSeleccionado = new Empleados();
        empresaSeleccionada = new Empresas();
        reporteLovSeleccionado = null;
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
        String pagActual = "nreportescontabilidad";

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

    }

    public void limpiarListasValor() {
        lovEmpleados = null;
        lovEmpresas = null;
        lovInforeportes = null;
        lovProcesos = null;
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
            administrarNReporteContabilidad.obtenerConexion(ses.getId());
            administarReportes.obtenerConexion(ses.getId());
            administrarInforeportes.obtenerConexion(ses.getId());
        } catch (Exception e) {
            log.info("Controlador.ControlNReporteContabilidad.inicializarAdministrador()");
            log.error("Causa: " + e.getCause());
        }
    }

    public void iniciarPagina() {
        activoMostrarTodos = true;
        activoBuscarReporte = false;
        getListaIR();
    }

    //TOOLTIP
    public void guardarCambios() {
        try {
            if (!cambiosReporte) {
                if (parametroDeReporte.getUsuario() != null) {

                    if (parametroDeReporte.getCodigoempleadodesde() == null) {
                        parametroDeReporte.setCodigoempleadodesde(null);
                    }
                    if (parametroDeReporte.getCodigoempleadohasta() == null) {
                        parametroDeReporte.setCodigoempleadohasta(null);
                    }
                    if (parametroDeReporte.getProceso().getSecuencia() == null) {
                        parametroDeReporte.setProceso(null);
                    }
                    if (parametroDeReporte.getEmpresa().getSecuencia() == null) {
                        parametroDeReporte.setEmpresa(null);
                    }
                    administrarNReporteContabilidad.modificarParametrosReportes(parametroDeReporte);
                }
                if (!listaInfoReportesModificados.isEmpty()) {
                    if (!mapTipos.isEmpty()) {
                        listaInfoReportesModificados.get(0).setTipo((String) mapTipos.get(listaInfoReportesModificados.get(0).getSecuencia()));
                    }
                    administrarNReporteContabilidad.guardarCambiosInfoReportes(listaInfoReportesModificados);
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

            if (casilla == 3) {
                RequestContext.getCurrentInstance().update("formDialogos:editarFechaHasta");
                RequestContext.getCurrentInstance().execute("PF('editarFechaHasta').show()");
            }
            if (casilla == 4) {
                RequestContext.getCurrentInstance().update("formDialogos:empleadoHasta");
                RequestContext.getCurrentInstance().execute("PF('empleadoHasta').show()");
            }
            if (casilla == 5) {
                RequestContext.getCurrentInstance().update("formDialogos:proceso");
                RequestContext.getCurrentInstance().execute("PF('proceso').show()");
            }
            if (casilla == 6) {
                RequestContext.getCurrentInstance().update("formDialogos:empresa");
                RequestContext.getCurrentInstance().execute("PF('empresa').show()");
            }
            casilla = -1;
        }
        if (casillaInforReporte >= 1) {
            if (casillaInforReporte == 1) {
                RequestContext.getCurrentInstance().update("formDialogos:infoReporteCodigoD");
                RequestContext.getCurrentInstance().execute("PF('infoReporteCodigoD').show()");
            }
            if (casillaInforReporte == 2) {
                RequestContext.getCurrentInstance().update("formDialogos:infoReporteNombreD");
                RequestContext.getCurrentInstance().execute("PF('infoReporteNombreD').show()");
            }
            casillaInforReporte = -1;
        }
    }

    public void cargarEmpleados() {
        if (lovEmpleados == null) {
            lovEmpleados = administrarNReporteContabilidad.listEmpleados();
        }
    }

    public void cargarProcesos() {
        if (lovProcesos == null || lovProcesos.isEmpty()) {
            lovProcesos = administrarNReporteContabilidad.listProcesos();
        }
    }

    public void cargarEmpresas() {
        if (lovEmpresas == null || lovEmpresas.isEmpty()) {
            lovEmpresas = administrarNReporteContabilidad.listEmpresas();
        }
    }

    public void mostrarDialogosListas() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (casilla == 2) {
            lovEmpleados = null;
            cargarEmpleados();
            contarRegistrosEmpeladoD();
            RequestContext.getCurrentInstance().update("formDialogos:EmpleadoDesdeDialogo");
            RequestContext.getCurrentInstance().execute("PF('EmpleadoDesdeDialogo').show()");
        }
        if (casilla == 4) {
            lovEmpleados = null;
            cargarEmpleados();
            contarRegistrosEmpeladoH();
            RequestContext.getCurrentInstance().update("formDialogos:EmpleadoHastaDialogo");
            RequestContext.getCurrentInstance().execute("PF('EmpleadoHastaDialogo').show()");
        }
        if (casilla == 5) {
            lovProcesos = null;
            cargarProcesos();
            contarRegistrosProceso();
            RequestContext.getCurrentInstance().update("formDialogos:ProcesoDialogo");
            RequestContext.getCurrentInstance().execute("PF('ProcesoDialogo').show()");
        }
        if (casilla == 6) {
            lovEmpresas = null;
            cargarEmpresas();
            contarRegistrosEmpresa();
            RequestContext.getCurrentInstance().update("formDialogos:EmpresaDialogo");
            RequestContext.getCurrentInstance().execute("PF('EmpresaDialogo').show()");
        }
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            altoTabla = "180";
            codigoIR = (Column) c.getViewRoot().findComponent("form:reportesContabilidad:codigoIR");
            codigoIR.setFilterStyle("width: 85% !important;");
            reporteIR = (Column) c.getViewRoot().findComponent("form:reportesContabilidad:reporteIR");
            reporteIR.setFilterStyle("width: 85% !important;");
            tipoIR = (SelectCheckboxMenu) c.getViewRoot().findComponent("form:reportesContabilidad:tipo");
            tipoIR.setRendered(true);
            RequestContext.getCurrentInstance().update("form:reportesContabilidad:tipo");
            RequestContext.getCurrentInstance().update("form:reportesContabilidad");
            tipoLista = 1;
            bandera = 1;
        } else if (bandera == 1) {
            cerrarFiltrado();
            defaultPropiedadesParametrosReporte();
        }

    }

    private void cerrarFiltrado() {
        FacesContext c = FacesContext.getCurrentInstance();
        altoTabla = "200";
        codigoIR = (Column) c.getViewRoot().findComponent("form:reportesContabilidad:codigoIR");
        codigoIR.setFilterStyle("display: none; visibility: hidden;");
        reporteIR = (Column) c.getViewRoot().findComponent("form:reportesContabilidad:reporteIR");
        reporteIR.setFilterStyle("display: none; visibility: hidden;");
        tipoIR = (SelectCheckboxMenu) c.getViewRoot().findComponent("form:reportesContabilidad:tipo");
        tipoIR.setRendered(false);
        tipoIR.setSelectedValues(null);
        tipoIR.resetValue();
        RequestContext.getCurrentInstance().update("form:reportesContabilidad:tipo");
        RequestContext.getCurrentInstance().update("form:reportesContabilidad");
        bandera = 0;
        filtrarListInforeportesUsuario = null;
        tipoLista = 0;
    }

    public void salir() {
        if (bandera == 1) {
            cerrarFiltrado();
        }
        lovEmpleados = null;
        listaIR = null;
        lovProcesos = null;
        lovEmpresas = null;
        lovInforeportes = null;
        parametroDeReporte = null;
        parametroModificacion = null;
        casilla = -1;
        procesoSeleccionado = null;
        listaInfoReportesModificados.clear();
        cambiosReporte = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        navegar("atras");
    }

    public void cancelarModificaciones() {
        if (bandera == 1) {
            cerrarFiltrado();
        }
        listaIR = null;
        casilla = -1;
        parametroDeReporte = null;
        listaInfoReportesModificados.clear();
        parametroModificacion = null;
        cambiosReporte = true;
        getParametroDeReporte();
        getListaIR();
        activoMostrarTodos = true;
        activoBuscarReporte = false;
        inforreporteSeleccionado = null;
        getParametroDeReporte();
        getListaIR();
        RequestContext.getCurrentInstance().update("form:MOSTRARTODOS");
        RequestContext.getCurrentInstance().update("form:BUSCARREPORTE");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("form:ENVIOCORREO");
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
        RequestContext.getCurrentInstance().update("form:reportesContabilidad");
        RequestContext.getCurrentInstance().update("formParametros:fechaDesdeParametroL");
        RequestContext.getCurrentInstance().update("formParametros:fechaHastaParametroL");
        RequestContext.getCurrentInstance().update("formParametros:empleadoDesdeParametroL");
        RequestContext.getCurrentInstance().update("formParametros:empleadoHastaParametroL");
        RequestContext.getCurrentInstance().update("formParametros:procesoParametroL");
        RequestContext.getCurrentInstance().update("formParametros:empresaParametro");
    }

    public void cancelarYSalir() {
        cancelarModificaciones();
        salir();
    }

    public void eventoFiltrar() {
        contarRegistros();
    }

    private void activarEnvioCorreo() {
        if (inforreporteSeleccionado != null) {
            activarEnvio = false;
        } else {
            activarEnvio = false;
        }
        RequestContext.getCurrentInstance().update("form:ENVIOCORREO");
    }

    public void seleccionRegistro() {
        try {
            activarEnvioCorreo();
            defaultPropiedadesParametrosReporte();
            if (inforreporteSeleccionado.getFecdesde().equals("SI")) {
                color = "red";
                RequestContext.getCurrentInstance().update("formParametros");
            }
            if (inforreporteSeleccionado.getFechasta().equals("SI")) {
                color2 = "red";
                RequestContext.getCurrentInstance().update("formParametros");
            }
            if (inforreporteSeleccionado.getEmdesde().equals("SI")) {
                empleadoDesdeParametroL = (InputText) FacesContext.getCurrentInstance().getViewRoot().findComponent("formParametros:empleadoDesdeParametroL");
                //empleadoDesdeParametro.setStyle("position: absolute; top: 41px; left: 150px; height: 10px; font-size: 11px; width: 90px; color: red;");
                if (!empleadoDesdeParametroL.getStyle().contains(" color: red;")) {
                    empleadoDesdeParametroL.setStyle(empleadoDesdeParametroL.getStyle() + " color: red;");
                }
            } else {
                empleadoDesdeParametroL = (InputText) FacesContext.getCurrentInstance().getViewRoot().findComponent("formParametros:empleadoDesdeParametroL");
                try {
                    if (empleadoDesdeParametroL.getStyle().contains(" color: red;")) {
                        empleadoDesdeParametroL.setStyle(empleadoDesdeParametroL.getStyle().replace(" color: red;", ""));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            RequestContext.getCurrentInstance().update("formParametros:empleadoDesdeParametroL");

            log.info("reporteSeleccionado.getEmhasta(): " + inforreporteSeleccionado.getEmhasta());
            if (inforreporteSeleccionado.getEmhasta().equals("SI")) {
                empleadoHastaParametroL = (InputText) FacesContext.getCurrentInstance().getViewRoot().findComponent("formParametros:empleadoHastaParametroL");
                empleadoHastaParametroL.setStyle(empleadoHastaParametroL.getStyle() + "color: red;");
                RequestContext.getCurrentInstance().update("formParametros:empleadoHastaParametroL");
            }
            RequestContext.getCurrentInstance().update("formParametros");
            // RequestContext.getCurrentInstance().update("form:reportesContabilidad");
        } catch (Exception ex) {
            log.warn("Error en selecccion Registro : " + ex.getMessage());
        }
    }

    public void requisitosParaReporte() {
        requisitosReporte = "";
        if (inforreporteSeleccionado.getFecdesde().equals("SI")) {
            requisitosReporte = requisitosReporte + "- Fecha Desde -";
        }
        if (inforreporteSeleccionado.getFechasta().equals("SI")) {
            requisitosReporte = requisitosReporte + "- Fecha Hasta -";
        }
        if (inforreporteSeleccionado.getEmdesde().equals("SI")) {
            requisitosReporte = requisitosReporte + "- Empleado Desde -";
        }
        if (inforreporteSeleccionado.getEmhasta().equals("SI")) {
            requisitosReporte = requisitosReporte + "- Empleado Hasta -";
        }
        if (!requisitosReporte.isEmpty()) {
            RequestContext.getCurrentInstance().update("formDialogos:requisitosReporte");
            RequestContext.getCurrentInstance().execute("PF('requisitosReporte').show()");
        }
    }

    public void modificacionTipoReporte(Inforeportes reporte, String tipo) {
        inforreporteSeleccionado = reporte;
        inforreporteSeleccionado.setTipo(tipo);
        cambiosReporte = false;
        if (listaInfoReportesModificados.isEmpty() || !listaInfoReportesModificados.contains(inforreporteSeleccionado)) {
            listaInfoReportesModificados.add(inforreporteSeleccionado);
            mapTipos.put(inforreporteSeleccionado.getSecuencia(), inforreporteSeleccionado.getTipo());
        }
        int n = listaInfoReportesModificados.indexOf(inforreporteSeleccionado);
        listaInfoReportesModificados.get(n).setTipo(tipo);
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void generarReporte(Inforeportes reporte) {
        try {
            guardarCambios();
            inforreporteSeleccionado = reporte;
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

    public void generarDocumentoReporte() {
        try {
            RequestContext context = RequestContext.getCurrentInstance();
            if (inforreporteSeleccionado != null) {
                nombreReporte = inforreporteSeleccionado.getNombrereporte();
                tipoReporte = inforreporteSeleccionado.getTipo();

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
                        file = new DefaultStreamedContent(fis, "application/pdf");
                    } catch (FileNotFoundException ex) {
                        RequestContext.getCurrentInstance().update("formDialogos:errorGenerandoReporte");
                        RequestContext.getCurrentInstance().execute("PF('errorGenerandoReporte').show()");
                        file = null;
                    }
                    if (file != null) {
                        log.info("userAgent: " + userAgent);
                        if (inforreporteSeleccionado != null) {
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

    public void dispararDialogoGuardarCambios() {
        RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");

    }

    public void cancelarReporte() {
        log.info(this.getClass().getName() + ".cancelarReporte()");
        administarReportes.cancelarReporte();
    }

    public void defaultPropiedadesParametrosReporte() {
        color = "black";
        decoracion = "none";
        color2 = "black";
        decoracion2 = "none";
    }

    public void reiniciarStreamedContent() {
        log.info(this.getClass().getName() + ".reiniciarStreamedContent()");
        file = null;
    }

    public void mostrarDialogoBuscarReporte() {
        try {
            if (cambiosReporte == true) {
                contarRegistrosLovReportes();
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("formDialogos:ReportesDialogo");
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
            inforreporteSeleccionado = null;
            contarRegistros();
            RequestContext context = RequestContext.getCurrentInstance();
            activoBuscarReporte = false;
            activoMostrarTodos = true;
            RequestContext.getCurrentInstance().update("form:MOSTRARTODOS");
            RequestContext.getCurrentInstance().update("form:BUSCARREPORTE");
            RequestContext.getCurrentInstance().update("form:reportesContabilidad");
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

    public void actualizarEmpresa() {
        log.info(this.getClass().getName() + ".actualizarEmpresa()");
        permitirIndex = true;
        parametroDeReporte.setEmpresa(empresaSeleccionada);
        parametroModificacion = parametroDeReporte;
        cambiosReporte = false;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formDialogos:lovEmpresas:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovEmpresas').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('EmpresaDialogo').hide()");

        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("formParametros:empresaParametro");
        empresaSeleccionada = null;
        aceptar = true;
        lovEmpresasFiltrar = null;

    }

    public void cancelarEmpresa() {
        log.info(this.getClass().getName() + ".cancelarEmpresa()");
        empresaSeleccionada = null;
        aceptar = true;
        lovEmpresasFiltrar = null;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formDialogos:lovEmpresas:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovEmpresas').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('EmpresaDialogo').hide()");
    }

    public void actualizarProceso() {
        permitirIndex = true;
        parametroDeReporte.setProceso(procesoSeleccionado);
        parametroModificacion = parametroDeReporte;
        cambiosReporte = false;
        procesoSeleccionado = null;
        aceptar = true;
        lovProcesosFiltrar = null;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formDialogos:lovProceso:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovProceso').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('ProcesoDialogo').hide()");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("formParametros:procesoParametroL");
    }

    public void cancelarCambioProceso() {
        procesoSeleccionado = null;
        aceptar = true;
        lovProcesosFiltrar = null;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formDialogos:lovProceso:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovProceso').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('ProcesoDialogo').hide()");
    }

    public void actualizarEmplDesde() {
        permitirIndex = true;
        parametroDeReporte.setCodigoempleadodesde(empleadoSeleccionado.getCodigoempleado());
        parametroModificacion = parametroDeReporte;
        empleadoSeleccionado = null;
        aceptar = true;
        lovEmpleadosFiltrar = null;
        cambiosReporte = false;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formDialogos:lovEmpleadoDesde:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovEmpleadoDesde').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('EmpleadoDesdeDialogo').hide()");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("formParametros:empleadoDesdeParametroL");
    }

    public void cancelarCambioEmplDesde() {
        empleadoSeleccionado = null;
        aceptar = true;
        lovEmpleadosFiltrar = null;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formDialogos:lovEmpleadoDesde:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovEmpleadoDesde').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('EmpleadoDesdeDialogo').hide()");
    }

    public void actualizarEmplHasta() {
        permitirIndex = true;
        parametroDeReporte.setCodigoempleadohasta(empleadoSeleccionado.getCodigoempleado());
        parametroModificacion = parametroDeReporte;
        empleadoSeleccionado = null;
        aceptar = true;
        lovEmpleadosFiltrar = null;
        cambiosReporte = false;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formDialogos:lovEmpleadoHasta:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovEmpleadoHasta').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('EmpleadoHastaDialogo').hide()");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("formParametros:empleadoHastaParametroL");
    }

    public void cancelarCambioEmplHasta() {
        empleadoSeleccionado = null;
        aceptar = true;
        lovEmpleadosFiltrar = null;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formDialogos:lovEmpleadoHasta:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovEmpleadoHasta').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('EmpleadoHastaDialogo').hide()");
    }

    public void actualizarSeleccionInforeporte() {
        log.info(this.getClass().getName() + ".actualizarSeleccionInforeporte()");
        RequestContext context = RequestContext.getCurrentInstance();
        if (bandera == 1) {
            cerrarFiltrado();
        }
        defaultPropiedadesParametrosReporte();
        listaIR.clear();
        listaIR.add(reporteLovSeleccionado);
        filtrarListInforeportesUsuario = null;
        filtrarLovInforeportes = null;
        aceptar = true;
        activoBuscarReporte = true;
        activoMostrarTodos = false;
        inforreporteSeleccionado = reporteLovSeleccionado;
        reporteLovSeleccionado = null;
        RequestContext.getCurrentInstance().update("form:MOSTRARTODOS");
        RequestContext.getCurrentInstance().update("form:BUSCARREPORTE");
        context.reset("formDialogos:lovReportesDialogo:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovReportesDialogo').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('ReportesDialogo').hide()");
        RequestContext.getCurrentInstance().update("form:reportesContabilidad");
        contarRegistros();
    }

    public void cancelarSeleccionInforeporte() {
        log.info(this.getClass().getName() + ".cancelarSeleccionInforeporte()");
        filtrarListInforeportesUsuario = null;
        reporteLovSeleccionado = null;
        aceptar = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formDialogos:lovReportesDialogo:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovReportesDialogo').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('ReportesDialogo').hide()");
    }

    public void autocompletarGeneral(String campoConfirmar, String valorConfirmar) {
        RequestContext context = RequestContext.getCurrentInstance();
        int indiceUnicoElemento = -1;
        int coincidencias = 0;
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
                    RequestContext.getCurrentInstance().update("formDialogos:ProcesoDialogo");
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
                    RequestContext.getCurrentInstance().update("formDialogos:EmpresaDialogo");
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
             parametroDeReporte.setCodigoempleadodesde(BigDecimal.valueOf(0));
        }
        if (empldesde.equals(BigDecimal.valueOf(0))) {
            parametroDeReporte.setCodigoempleadodesde(BigDecimal.valueOf(0));
        }
        cambiosReporte = false;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void modificarParametroEmpleadoHasta(BigDecimal emphasta) {
        String h = "99999999999999999999999999";
        BigDecimal b = new BigDecimal(h);
         if (emplHasta.equals("") || emplHasta == null) {
             parametroDeReporte.setCodigoempleadodesde(b);
        }
        if (emphasta.equals(b)) {
            parametroDeReporte.setCodigoempleadodesde(b);
        }
        cambiosReporte = false;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void posicionCelda(int i) {
        if (permitirIndex) {
            casilla = i;
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
                    fechaHasta = parametroDeReporte.getFechahasta();
                    break;
                case 4:
                    habilitarBotonLov();
                    emplHasta = parametroDeReporte.getCodigoempleadohasta();
                    break;
                case 5:
                    habilitarBotonLov();
                    proceso = parametroDeReporte.getProceso().getDescripcion();
                    break;
                default:
                    break;
            }
            casillaInforReporte = -1;
        }
    }

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
        RequestContext.getCurrentInstance().update("formDialogos:reporteAGenerar");
        RequestContext.getCurrentInstance().execute("PF('reporteAGenerar').show()");
    }

    public void cancelarGenerarReporte() {
        reporteGenerar = "";
        posicionReporte = -1;
    }

    public void contarRegistros() {
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
    }

    public void contarRegistrosLovReportes() {
        RequestContext.getCurrentInstance().update("formDialogos:infoRegistroReportes");
    }

    public void contarRegistrosEmpeladoD() {
        RequestContext.getCurrentInstance().update("formDialogos:infoRegistroEmpleadoDesde");
    }

    public void contarRegistrosEmpeladoH() {
        RequestContext.getCurrentInstance().update("formDialogos:infoRegistroEmpleadoHasta");
    }

    public void contarRegistrosProceso() {
        RequestContext.getCurrentInstance().update("formDialogos:infoRegistroProceso");
    }

    public void contarRegistrosEmpresa() {
        RequestContext.getCurrentInstance().update("formDialogos:infoRegistroEmpresa");
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

//******************GETTER AND SETTER***************************
    public ParametrosReportes getParametroDeReporte() {
        try {
            if (parametroDeReporte == null) {
                parametroDeReporte = new ParametrosReportes();
                parametroDeReporte = administrarNReporteContabilidad.parametrosDeReporte();
            }

            if (parametroDeReporte.getProceso() == null) {
                parametroDeReporte.setProceso(new Procesos());
            }
            if (parametroDeReporte.getEmpresa()== null) {
                parametroDeReporte.setEmpresa(new Empresas());
            }
            return parametroDeReporte;
        } catch (Exception e) {
            log.warn("Error getParametroDeInforme : " + e);
            return null;
        }
    }

    public void setParametroDeReporte(ParametrosReportes parametroDeReporte) {
        this.parametroDeReporte = parametroDeReporte;
    }

    public List<Inforeportes> getListaIR() {
        try {
            if (listaIR == null) {
                listaIR = administrarNReporteContabilidad.listInforeportesUsuario();
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

    public List<Inforeportes> getFiltrarListInforeportesUsuario() {
        return filtrarListInforeportesUsuario;
    }

    public void setFiltrarListInforeportesUsuario(List<Inforeportes> filtrarListInforeportesUsuario) {
        this.filtrarListInforeportesUsuario = filtrarListInforeportesUsuario;
    }

    public Inforeportes getInforreporteSeleccionado() {
        return inforreporteSeleccionado;
    }

    public void setInforreporteSeleccionado(Inforeportes inforreporteSeleccionado) {
        this.inforreporteSeleccionado = inforreporteSeleccionado;
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

    public void setLovEmpleados(List<Empleados> lovEmpleados) {
        this.lovEmpleados = lovEmpleados;
    }

    public List<Empleados> getLovEmpleadosFiltrar() {
        return lovEmpleadosFiltrar;
    }

    public void setLovEmpleadosFiltrar(List<Empleados> lovEmpleadosFiltrar) {
        this.lovEmpleadosFiltrar = lovEmpleadosFiltrar;
    }

    public Empleados getEmpleadoSeleccionado() {
        return empleadoSeleccionado;
    }

    public void setEmpleadoSeleccionado(Empleados empleadoSeleccionado) {
        this.empleadoSeleccionado = empleadoSeleccionado;
    }

    public List<Procesos> getLovProcesos() {
        return lovProcesos;
    }

    public void setLovProcesos(List<Procesos> lovProcesos) {
        this.lovProcesos = lovProcesos;
    }

    public Procesos getProcesoSeleccionado() {
        return procesoSeleccionado;
    }

    public void setProcesoSeleccionado(Procesos procesoSeleccionado) {
        this.procesoSeleccionado = procesoSeleccionado;
    }

    public List<Procesos> getLovProcesosFiltrar() {
        return lovProcesosFiltrar;
    }

    public void setLovProcesosFiltrar(List<Procesos> lovProcesosFiltrar) {
        this.lovProcesosFiltrar = lovProcesosFiltrar;
    }

    public List<Empresas> getLovEmpresas() {
        return lovEmpresas;
    }

    public void setLovEmpresas(List<Empresas> lovEmpresas) {
        this.lovEmpresas = lovEmpresas;
    }

    public List<Empresas> getLovEmpresasFiltrar() {
        return lovEmpresasFiltrar;
    }

    public void setLovEmpresasFiltrar(List<Empresas> lovEmpresasFiltrar) {
        this.lovEmpresasFiltrar = lovEmpresasFiltrar;
    }

    public List<Inforeportes> getLovInforeportes() {
        if (lovInforeportes == null || lovInforeportes.isEmpty()) {
            lovInforeportes = administrarNReporteContabilidad.listInforeportesUsuario();
        }
        return lovInforeportes;
    }

    public void setLovInforeportes(List<Inforeportes> lovInforeportes) {
        this.lovInforeportes = lovInforeportes;
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

    public String getInfoRegistroReportes() {
        FacesContext c = FacesContext.getCurrentInstance();
        tabla = (DataTable) c.getViewRoot().findComponent("formDialogos:lovReportesDialogo");
        infoRegistroReportes = String.valueOf(tabla.getRowCount());
        return infoRegistroReportes;
    }

    public String getInfoRegistroProceso() {
        FacesContext c = FacesContext.getCurrentInstance();
        tabla = (DataTable) c.getViewRoot().findComponent("formDialogos:lovProceso");
        infoRegistroProceso = String.valueOf(tabla.getRowCount());
        return infoRegistroProceso;
    }

    public String getInfoRegistroEmpleadoDesde() {
        FacesContext c = FacesContext.getCurrentInstance();
        tabla = (DataTable) c.getViewRoot().findComponent("formDialogos:lovEmpleadoDesde");
        infoRegistroEmpleadoDesde = String.valueOf(tabla.getRowCount());
        return infoRegistroEmpleadoDesde;
    }

    public String getInfoRegistroEmpleadoHasta() {
        FacesContext c = FacesContext.getCurrentInstance();
        tabla = (DataTable) c.getViewRoot().findComponent("formDialogos:lovEmpleadoHasta");
        infoRegistroEmpleadoHasta = String.valueOf(tabla.getRowCount());
        return infoRegistroEmpleadoHasta;
    }

    public String getInfoRegistro() {
        FacesContext c = FacesContext.getCurrentInstance();
        tabla = (DataTable) c.getViewRoot().findComponent("form:reportesContabilidad");
        infoRegistro = String.valueOf(tabla.getRowCount());
        return infoRegistro;
    }

    public String getInfoRegistroEmpresa() {
        FacesContext c = FacesContext.getCurrentInstance();
        tabla = (DataTable) c.getViewRoot().findComponent("formDialogos:lovEmpresas");
        infoRegistroEmpresa = String.valueOf(tabla.getRowCount());
        return infoRegistroEmpresa;
    }

    public void setInfoRegistroEmpresa(String infoRegistroEmpresa) {
        this.infoRegistroEmpresa = infoRegistroEmpresa;
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

    public Inforeportes getReporteLovSeleccionado() {
        return reporteLovSeleccionado;
    }

    public void setReporteLovSeleccionado(Inforeportes reporteLovSeleccionado) {
        this.reporteLovSeleccionado = reporteLovSeleccionado;
    }

    public List<Inforeportes> getFiltrarLovInforeportes() {
        return filtrarLovInforeportes;
    }

    public void setFiltrarLovInforeportes(List<Inforeportes> filtrarLovInforeportes) {
        this.filtrarLovInforeportes = filtrarLovInforeportes;
    }

    public List<Inforeportes> getFiltrarReportes() {
        return filtrarReportes;
    }

    public void setFiltrarReportes(List<Inforeportes> filtrarReportes) {
        this.filtrarReportes = filtrarReportes;
    }

    public Empresas getEmpresaSeleccionada() {
        return empresaSeleccionada;
    }

    public void setEmpresaSeleccionada(Empresas empresaSeleccionada) {
        this.empresaSeleccionada = empresaSeleccionada;
    }

    public String getPathReporteGenerado() {
        return pathReporteGenerado;
    }

    public void setPathReporteGenerado(String pathReporteGenerado) {
        this.pathReporteGenerado = pathReporteGenerado;
    }

    public String getCabezeraVisor() {
        return cabezeraVisor;
    }

    public void setCabezeraVisor(String cabezeraVisor) {
        this.cabezeraVisor = cabezeraVisor;
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
