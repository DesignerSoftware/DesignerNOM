package Controlador;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import Entidades.ActualUsuario;
import Entidades.Empresas;
import Entidades.ParametrosContables;
import Entidades.ParametrosEstructuras;
import Entidades.Procesos;
import Entidades.SolucionesNodos;
import Entidades.UsuariosInterfases;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import ControlNavegacion.ControlListaNavegacion;
import Entidades.InterconInfor;
import InterfaceAdministrar.AdministarReportesInterface;
import InterfaceAdministrar.AdministrarInterfaseContableInforInterface;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.LinkedHashMap;
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
@Named(value = "controlInterfaseContableInfor")
@SessionScoped
public class ControlInterfaseContableInfor implements Serializable {

    private static Logger log = Logger.getLogger(ControlInterfaseContableInfor.class);

    @EJB
    AdministrarInterfaseContableInforInterface administrarInterfaseContableInfor;
    @EJB
    AdministrarRastrosInterface administrarRastros;
    @EJB
    AdministarReportesInterface administarReportes;

    private ActualUsuario actualUsuarioBD;
    //
    private ParametrosContables parametroContableActual;
    private ParametrosContables nuevoParametroContable;
    private List<ParametrosContables> listaParametrosContables;
    private Date auxParametroFechaInicial, auxParametroFechaFinal;
    private boolean permitirIndexParametro;
    private boolean cambiosParametro;
    private List<ParametrosContables> listParametrosContablesBorrar;
    //
    private List<SolucionesNodos> listaGenerados;
    private List<SolucionesNodos> listaGeneradosBorrar;
    private List<SolucionesNodos> filtrarListaGenerados;
    private SolucionesNodos generadoTablaSeleccionado;
    private int cualCeldaGenerado;
    private SolucionesNodos editarGenerado;
    private int tipoListaGenerada, banderaGenerado;
    private String infoRegistroGenerados;
    //
    private List<InterconInfor> listaInterconInfor;
    private List<InterconInfor> listaInterconSapBorrar;
    private List<InterconInfor> filtrarListaInterconInfor;
    private InterconInfor interconTablaSeleccionada;
    private String infoRegistroContabilizados;
    private int cualCeldaIntercon;
    private InterconInfor editarIntercon;
    private int tipoListaIntercon, banderaIntercon;

    private List<Empresas> lovEmpresas;
    private List<Empresas> filtrarLovEmpresas;
    private Empresas empresaSeleccionada;
    private String infoRegistroEmpresa;

    private List<Procesos> lovProcesos;
    private List<Procesos> filtrarLovProcesos;
    private Procesos procesoSeleccionado;
    private String infoRegistroProceso;
    //
    private StreamedContent reporte;
    private String pathReporteGenerado = null;
    private String nombreReporte, tipoReporte;
    private String cabezeraVisor;
    //
    private boolean guardado;
    private Date fechaDeParametro;
    private boolean aceptar;
    //
    private int registroActual;
    //
    private boolean activarAgregar, activarOtros;
    private boolean activarEnviar, activarDeshacer, activarLov;
    //
    private int tipoActualizacion;
    //
    private Column genConcepto, genValor, genTercero, genCntDebito, genCntCredito, genEmpleado, genProceso;
    private String altoTablaGenerada;
    private Column interEmpleado, interTercero, interCuenta, interDebito, interConcepto, interCentroCosto, interProceso, interProyecto;
    private String altoTablaIntercon;
    private int tipoPlano;
    private String msnFechasActualizar;
    private BigDecimal totalCGenerado, totalDGenerado, totalDInter, totalCInter;
    private String msnPaso1;
    private String fechaIniRecon, fechaFinRecon;
    private String rutaArchivo, nombreArchivo, pathProceso;
    //
    private boolean modificacionParametro;
    //
    private BigInteger secRegistro, backUpSecRegistro;
    //
    private DefaultStreamedContent download;
    private UsuariosInterfases usuarioInterfaseContabilizacion;
    private boolean estadoReporte;
    private String resultadoReporte;
    private int cualTabla;
    private int cualCeldaParametroContable;
    private String paginaAnterior = "nominaf";
    private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();
    private String userAgent;
    private ExternalContext externalContext;
    private String msgError;

    /**
     * Creates a new instance of ControlInterfaseContableInfor
     */
    public ControlInterfaseContableInfor() {
        msnPaso1 = " ";
        totalCGenerado = BigDecimal.ZERO;
        totalDGenerado = BigDecimal.ZERO;
        totalDInter = BigDecimal.ZERO;
        totalCInter = BigDecimal.ZERO;
        activarEnviar = true;
        activarDeshacer = true;
        msnFechasActualizar = "";
        tipoPlano = 1;
        altoTablaGenerada = "80";
        altoTablaIntercon = "95";
        tipoListaGenerada = 0;
        tipoListaIntercon = 0;
        banderaGenerado = 0;
        banderaIntercon = 0;
        listParametrosContablesBorrar = new ArrayList<ParametrosContables>();
        nuevoParametroContable = new ParametrosContables();
        nuevoParametroContable.setEmpresaRegistro(new Empresas());
        nuevoParametroContable.setProceso(new Procesos());
        activarAgregar = true;
        activarOtros = true;
        listaGenerados = null;
        listaGeneradosBorrar = new ArrayList<SolucionesNodos>();
        listaInterconInfor = null;
        listaInterconSapBorrar = new ArrayList<InterconInfor>();
        interconTablaSeleccionada = new InterconInfor();
        generadoTablaSeleccionado = null;
        interconTablaSeleccionada = null;
        lovEmpresas = null;
        lovProcesos = null;
        cualCeldaGenerado = -1;
        cualCeldaIntercon = -1;
        editarGenerado = new SolucionesNodos();
        editarIntercon = new InterconInfor();
        registroActual = 0;
        aceptar = true;
        cambiosParametro = false;
        permitirIndexParametro = true;
        parametroContableActual = null;
        guardado = true;
        activarLov = true;
        nombreReporte = "CifraControl";
        tipoReporte = "PDF";
        estadoReporte = false;
        cualTabla = -1;
        mapParametros.put("paginaAnterior", paginaAnterior);
        msgError = "";
    }

    public void recibirPaginaEntrante(String pagina) {
        paginaAnterior = pagina;
        actualUsuarioBD = null;
        getActualUsuarioBD();
        listaParametrosContables = null;
        getListaParametrosContables();
        parametroContableActual = null;
        getParametroContableActual();
    }

    public void recibirParametros(Map<String, Object> map) {
        mapParametros = map;
        paginaAnterior = (String) mapParametros.get("paginaAnterior");
        actualUsuarioBD = null;
        getActualUsuarioBD();
        listaParametrosContables = null;
        getListaParametrosContables();
        parametroContableActual = null;
        getParametroContableActual();
    }

    //Reemplazar la funcion volverAtras, retornarPagina, Redirigir.....Atras.etc
    public void navegar(String pag) {
        FacesContext fc = FacesContext.getCurrentInstance();
        ControlListaNavegacion controlListaNavegacion = (ControlListaNavegacion) fc.getApplication().evaluateExpressionGet(fc, "#{controlListaNavegacion}", ControlListaNavegacion.class);
        String pagActual = "interfasecontableinfor";
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
        lovEmpresas = null;
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
            administrarInterfaseContableInfor.obtenerConexion(ses.getId());
            administarReportes.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
            externalContext = x.getExternalContext();
            userAgent = externalContext.getRequestHeaderMap().get("User-Agent");
            actualUsuarioBD = null;
            getActualUsuarioBD();
            listaParametrosContables = null;
            getListaParametrosContables();
            parametroContableActual = null;
            getParametroContableActual();
        } catch (Exception e) {
            log.error("Error postconstruct ControlInterfaseContableSapBOV8:  ", e);
            log.error("Causa: " + e.getCause());
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public String redirigir() {
        return paginaAnterior;
    }

    public void modificarParametroContable() {
        guardado = false;
        cambiosParametro = true;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void modificarParametroContable(String confirmarCambio, String valorConfirmar) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (valorConfirmar.equals("EMPRESA")) {
            parametroContableActual.getEmpresaRegistro().setNombre(parametroContableActual.getEmpresaRegistro().getNombre());
            for (int i = 0; i < lovEmpresas.size(); i++) {
                if (lovEmpresas.get(i).getNombre().startsWith(confirmarCambio.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                parametroContableActual.setEmpresaRegistro(lovEmpresas.get(indiceUnicoElemento));
                parametroContableActual.setEmpresaCodigo(lovEmpresas.get(indiceUnicoElemento).getCodigo());
                lovEmpresas.clear();
                getLovEmpresas();
                if (guardado == true) {
                    guardado = false;
                }
                cambiosParametro = true;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
                RequestContext.getCurrentInstance().update("form:parametroEmpresa");
            } else {
                permitirIndexParametro = false;
                lovEmpresas = null;
                cargarLovEmpresas();
                RequestContext.getCurrentInstance().update("form:EmpresaDialogo");
                RequestContext.getCurrentInstance().execute("PF('EmpresaDialogo').show()");
            }
        } else if (valorConfirmar.equals("PROCESO")) {
            if (!confirmarCambio.isEmpty()) {
                parametroContableActual.getProceso().setDescripcion(parametroContableActual.getProceso().getDescripcion());
                for (int i = 0; i < lovProcesos.size(); i++) {
                    if (lovProcesos.get(i).getDescripcion().startsWith(confirmarCambio.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    parametroContableActual.setProceso(lovProcesos.get(indiceUnicoElemento));
                    lovProcesos.clear();
                    cargarLovProcesos();
                    if (guardado == true) {
                        guardado = false;
                    }
                    cambiosParametro = true;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                    RequestContext.getCurrentInstance().update("form:parametroProceso");
                } else {
                    permitirIndexParametro = false;
                    lovProcesos = null;
                    cargarLovProcesos();
                    RequestContext.getCurrentInstance().update("form:ProcesoDialogo");
                    RequestContext.getCurrentInstance().execute("PF('ProcesoDialogo').show()");
                }
            } else {
                parametroContableActual.setProceso(null);
                guardado = false;
                cambiosParametro = true;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
                RequestContext.getCurrentInstance().update("form:parametroProceso");
            }
        }
    }

    public void modificarFechasParametro(ParametrosContables parametroc, int c) {
        parametroContableActual = parametroc;
        cambiarIndiceParametro(parametroContableActual, c);
        modificarParametroContable();
    }

    public boolean validarFechaParametro(int i) {
        fechaDeParametro = new Date();
        fechaDeParametro.setYear(0);
        fechaDeParametro.setMonth(1);
        fechaDeParametro.setDate(1);
        boolean retorno = true;
        if (i == 0) {
            if (parametroContableActual.getFechainicialcontabilizacion().after(fechaDeParametro) && parametroContableActual.getFechainicialcontabilizacion().before(parametroContableActual.getFechafinalcontabilizacion())) {
                retorno = true;
            } else {
                retorno = false;
            }
        }
        if (i == 1) {
            if (nuevoParametroContable.getFechainicialcontabilizacion().after(fechaDeParametro) && nuevoParametroContable.getFechainicialcontabilizacion().before(nuevoParametroContable.getFechafinalcontabilizacion())) {
                retorno = true;
            } else {
                retorno = false;
            }
        }
        return retorno;
    }

    public void posicionGenerado() {
        FacesContext context = FacesContext.getCurrentInstance();
        cualTabla = 1;
        Map<String, String> map = context.getExternalContext().getRequestParameterMap();
        String name = map.get("n"); // name attribute of node
        String type = map.get("t"); // type attribute of node
        int indice = Integer.parseInt(type);
        int columna = Integer.parseInt(name);
        generadoTablaSeleccionado = listaGenerados.get(indice);
        cualCeldaGenerado = columna;
        parametroContableActual = null;
        interconTablaSeleccionada = null;
    }

    public void posicionIntercon() {
        FacesContext context = FacesContext.getCurrentInstance();
        cualTabla = 2;
        Map<String, String> map = context.getExternalContext().getRequestParameterMap();
        String name = map.get("n"); // name attribute of node
        String type = map.get("t"); // type attribute of node
        int indice = Integer.parseInt(type);
        int columna = Integer.parseInt(name);
        interconTablaSeleccionada = listaInterconInfor.get(indice);
        cualCeldaIntercon = columna;
        parametroContableActual = null;
        generadoTablaSeleccionado = null;
        interconTablaSeleccionada.getSecuencia();
    }

    public void cambiarIndiceParametro(ParametrosContables parametroC, int celda) {
        cualCeldaParametroContable = celda;
        if (permitirIndexParametro == true) {
            parametroContableActual = parametroC;
            if (cualCeldaParametroContable == 0) {
                parametroContableActual.getEmpresaRegistro().getNombre();
            } else if (cualCeldaParametroContable == 1) {
                parametroContableActual.getProceso().getDescripcion();
            } else if (cualCeldaParametroContable == 2) {
                parametroContableActual.getDocumentoContable();
            } else if (cualCeldaParametroContable == 3) {
                parametroContableActual.getRespuestaxml();
            } else if (cualCeldaParametroContable == 4) {
                auxParametroFechaFinal = parametroContableActual.getFechafinalcontabilizacion();
            } else if (cualCeldaParametroContable == 5) {
                auxParametroFechaInicial = parametroContableActual.getFechainicialcontabilizacion();
            }
        }
    }

    public String msnPaso1() {
        if (parametroContableActual.getProceso() == null) {
            parametroContableActual.setProceso(new Procesos());
        }
        if (parametroContableActual.getProceso().getSecuencia() != null) {
            msnPaso1 = parametroContableActual.getProceso().getDescripcion().toUpperCase();
        } else {
            msnPaso1 = "TODOS";
        }
        return msnPaso1;
    }

    public void inicioCerrarPeriodoContable() {
        try {
            RequestContext context = RequestContext.getCurrentInstance();
            int contador = administrarInterfaseContableInfor.contarProcesosContabilizadosInterconInfor(parametroContableActual.getFechainicialcontabilizacion(), parametroContableActual.getFechafinalcontabilizacion());
            if (contador != -1) {
                if (contador == 0) {
                    RequestContext.getCurrentInstance().execute("PF('contadoCeroPerContable').show()");
                } else {
                    RequestContext.getCurrentInstance().update("form:paso1CerrarPeriodo");
                    RequestContext.getCurrentInstance().execute("PF('paso1CerrarPeriodo').show()");
                }
            }
        } catch (Exception e) {
            log.warn("Error cerrarPeriodoContable Controlador : " + e.toString());
        }
    }

    public void finCerrarPeriodoContable() {
        try {
            RequestContext context = RequestContext.getCurrentInstance();
            guardadoGeneral();
            administrarInterfaseContableInfor.cerrarProcesoContabilizacion(parametroContableActual.getFechainicialcontabilizacion(), parametroContableActual.getFechafinalcontabilizacion(), parametroContableActual.getEmpresaCodigo(), parametroContableActual.getProceso().getSecuencia());
            listaGenerados = null;
            if (listaGenerados == null) {
                listaGenerados = administrarInterfaseContableInfor.obtenerSolucionesNodosParametroContable(parametroContableActual.getFechainicialcontabilizacion(), parametroContableActual.getFechafinalcontabilizacion());
                if (listaGenerados != null) {
                    if (listaGenerados.size() > 0) {
                        activarEnviar = false;
                    } else {
                        activarDeshacer = true;
                    }

                } else {
                    activarDeshacer = true;
                }
                getTotalCGenerado();
                getTotalDGenerado();
            }
            listaInterconInfor = null;
            if (listaInterconInfor == null) {
                listaInterconInfor = administrarInterfaseContableInfor.obtenerInterconInforParametroContable(parametroContableActual.getFechainicialcontabilizacion(), parametroContableActual.getFechafinalcontabilizacion());
                if (listaInterconInfor != null) {
                    if (listaInterconInfor.size() > 0) {
                        activarDeshacer = false;
                    } else {
                        activarDeshacer = true;
                    }
                } else {
                    activarDeshacer = true;
                }
//                getTotalCInter();
//                getTotalDInter();
            }
            contarRegistroContabilizados();
            contarRegistrosGenerados();
            activarDeshacer = true;
            activarEnviar = true;
            RequestContext.getCurrentInstance().update("form:btnEnviar");
            RequestContext.getCurrentInstance().update("form:btnDeshacer");

            RequestContext.getCurrentInstance().update("form:totalDGenerado");
            RequestContext.getCurrentInstance().update("form:totalCGenerado");
//            RequestContext.getCurrentInstance().update("form:totalDInter");
//            RequestContext.getCurrentInstance().update("form:totalCInter");
            RequestContext.getCurrentInstance().update("form:datosGenerados");
            RequestContext.getCurrentInstance().update("form:datosIntercon");
            RequestContext.getCurrentInstance().execute("PF('operacionEnProceso').hide()");
            RequestContext.getCurrentInstance().execute("PF('paso3CerrarPeriodo').show()");
        } catch (Exception e) {
            log.warn("Error finCerrarPeriodoContable Controlador : " + e.toString());
        }
    }

    public void actionBtnGenerarPlano() {
        try {
            msgError = "";
            guardadoGeneral();
            String descripcionProceso = administrarInterfaseContableInfor.obtenerDescripcionProcesoArchivo(parametroContableActual.getProceso().getSecuencia());
//            nombreArchivo = "Interfase_Infor_" + descripcionProceso;
            nombreArchivo = " ";
            pathProceso = administrarInterfaseContableInfor.obtenerPathProceso();
            log.info("path proceso :" + pathProceso);
            nombreArchivo = administrarInterfaseContableInfor.ejecutarPKGCrearArchivoPlano(parametroContableActual.getFechainicialcontabilizacion(), parametroContableActual.getFechafinalcontabilizacion(), BigInteger.valueOf(Long.parseLong(parametroContableActual.getEmpresaCodigo().toString())), parametroContableActual.getProceso().getSecuencia(), nombreArchivo);
//            msgError = administrarInterfaseContableInfor.ejecutarPKGCrearArchivoPlano(parametroContableActual.getFechainicialcontabilizacion(), parametroContableActual.getFechafinalcontabilizacion(),parametroContableActual.getEmpresaCodigo() ,parametroContableActual.getProceso().getSecuencia());
//            if (msgError.equals("EXITO")) {
            rutaArchivo = "";
            rutaArchivo = pathProceso + nombreArchivo;
            log.info("rutaArchivo " + rutaArchivo);
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:planoGeneradoOK");
            RequestContext.getCurrentInstance().execute("PF('planoGeneradoOK').show()");
//            } else {
//                RequestContext.getCurrentInstance().update("formularioDialogos:errorGenerandoPlano");
//                RequestContext.getCurrentInstance().execute("PF('errorGenerandoPlano').show()");
//            }
        } catch (Exception e) {
            log.warn("Error actionBtnGenerarPlano Control : " + e.toString());
        }
    }

    public void exportarPlano() throws IOException {
        try {
            if (pathProceso != null || !pathProceso.startsWith("Error:")) {
                File planof = new File(rutaArchivo);
                FacesContext ctx = FacesContext.getCurrentInstance();
                FileInputStream fis = new FileInputStream(planof);
                byte[] bytes = new byte[1024];
                int read;
                if (!ctx.getResponseComplete()) {
                    String fileName = planof.getName();
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
            }
        } catch (Exception e) {
            msgError = "";
            msgError = e.getMessage();
            log.warn("Error exportando plano : " + e.getMessage());
            RequestContext.getCurrentInstance().update("formularioDialogos:errorExportandoPlano");
            RequestContext.getCurrentInstance().execute("PF('errorExportandoPlano').show()");
        }
    }

    public void cerrarPaginaDescarga() {
        RequestContext.getCurrentInstance().execute("PF('planoGeneradoOK').hide()");
    }

    public void actionBtnRecontabilizar() {
        Integer contador = administrarInterfaseContableInfor.obtenerContadorFlagGeneradoFechasInfor(parametroContableActual.getFechainicialcontabilizacion(), parametroContableActual.getFechafinalcontabilizacion());
        if (contador != null) {
            if (contador != 0) {
                RequestContext.getCurrentInstance().execute("PF('paso1Recon').show()");
            } else {
                RequestContext.getCurrentInstance().execute("PF('errorRecon').show()");
            }
        }
    }

    public void ejecutarPaso3Recon() {
        if (parametroContableActual.getFechafinalcontabilizacion() != null && parametroContableActual.getFechainicialcontabilizacion() != null) {
            DateFormat df = DateFormat.getDateInstance(DateFormat.LONG);
            String fechaI = df.format(parametroContableActual.getFechainicialcontabilizacion());
            String fechaF = df.format(parametroContableActual.getFechafinalcontabilizacion());
            fechaFinRecon = fechaF;
            fechaIniRecon = fechaI;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:paso3Recon");
            RequestContext.getCurrentInstance().execute("PF('paso3Recon').show()");
        }
    }

    public void finalizarProcesoRecontabilizacion() {
        try {
            guardadoGeneral();
            administrarInterfaseContableInfor.ejecutarPKGRecontabilizacion(parametroContableActual.getFechainicialcontabilizacion(), parametroContableActual.getFechafinalcontabilizacion());
            RequestContext.getCurrentInstance().execute("PF('operacionEnProceso').hide()");
            RequestContext.getCurrentInstance().execute("PF('RecontabilizacionExitosa').show()");
        } catch (Exception e) {
            log.warn("Error finalizarProcesoRecontabilizacion Controlador : " + e.toString());
            RequestContext.getCurrentInstance().execute("PF('operacionEnProceso').hide()");
            RequestContext.getCurrentInstance().execute("PF('RecontabilizacionExitosa').hide()");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("", e.getMessage()));
            RequestContext.getCurrentInstance().update("form:growl");
        }
    }

    public void actionBtnDeshacer() {
        try {
            RequestContext context = RequestContext.getCurrentInstance();
            guardadoGeneral();
            administrarInterfaseContableInfor.actualizarFlagInterconInforProcesoDeshacer(parametroContableActual.getFechainicialcontabilizacion(), parametroContableActual.getFechafinalcontabilizacion(), parametroContableActual.getProceso().getSecuencia());
            administrarInterfaseContableInfor.eliminarInterconInfor(parametroContableActual.getFechainicialcontabilizacion(), parametroContableActual.getFechafinalcontabilizacion(), parametroContableActual.getEmpresaCodigo(), parametroContableActual.getProceso().getSecuencia());
            listaGenerados = null;
            if (listaGenerados == null) {
                listaGenerados = administrarInterfaseContableInfor.obtenerSolucionesNodosParametroContable(parametroContableActual.getFechainicialcontabilizacion(), parametroContableActual.getFechafinalcontabilizacion());
                if (listaGenerados != null) {
                    if (listaGenerados.size() > 0) {
                        activarEnviar = false;
                    } else {
                        activarDeshacer = true;
                    }

                } else {
                    activarDeshacer = true;
                }
                getTotalCGenerado();
                getTotalDGenerado();
            }
            listaInterconInfor = null;
            if (listaInterconInfor == null) {
                listaInterconInfor = administrarInterfaseContableInfor.obtenerInterconInforParametroContable(parametroContableActual.getFechainicialcontabilizacion(), parametroContableActual.getFechafinalcontabilizacion());
                if (listaInterconInfor != null) {
                    if (listaInterconInfor.size() > 0) {
                        activarDeshacer = false;
                    } else {
                        activarDeshacer = true;
                    }
                } else {
                    activarDeshacer = true;
                }
//                getTotalCInter();
//                getTotalDInter();
            }

            contarRegistroContabilizados();
            contarRegistrosGenerados();
            RequestContext.getCurrentInstance().update("form:btnEnviar");
            activarDeshacer = true;
            RequestContext.getCurrentInstance().update("form:btnDeshacer");

            RequestContext.getCurrentInstance().update("form:totalDGenerado");
            RequestContext.getCurrentInstance().update("form:totalCGenerado");
//            RequestContext.getCurrentInstance().update("form:totalDInter");
//            RequestContext.getCurrentInstance().update("form:totalCInter");
            if (banderaGenerado == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                altoTablaGenerada = "80";
                genProceso = (Column) c.getViewRoot().findComponent("form:datosGenerados:genProceso");
                genProceso.setFilterStyle("display: none; visibility: hidden;");
                genEmpleado = (Column) c.getViewRoot().findComponent("form:datosGenerados:genEmpleado");
                genEmpleado.setFilterStyle("display: none; visibility: hidden;");
                genCntCredito = (Column) c.getViewRoot().findComponent("form:datosGenerados:genCntCredito");
                genCntCredito.setFilterStyle("display: none; visibility: hidden;");
                genCntDebito = (Column) c.getViewRoot().findComponent("form:datosGenerados:genCntDebito");
                genCntDebito.setFilterStyle("display: none; visibility: hidden;");
                genTercero = (Column) c.getViewRoot().findComponent("form:datosGenerados:genTercero");
                genTercero.setFilterStyle("display: none; visibility: hidden;");
                genValor = (Column) c.getViewRoot().findComponent("form:datosGenerados:genValor");
                genValor.setFilterStyle("display: none; visibility: hidden;");
                genConcepto = (Column) c.getViewRoot().findComponent("form:datosGenerados:genConcepto");
                genConcepto.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosGenerados");
                banderaGenerado = 0;
                filtrarListaGenerados = null;
                tipoListaGenerada = 0;
            }
            if (banderaIntercon == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                altoTablaIntercon = "95";
                interEmpleado = (Column) c.getViewRoot().findComponent("form:datosIntercon:interEmpleado");
                interEmpleado.setFilterStyle("display: none; visibility: hidden;");
                interTercero = (Column) c.getViewRoot().findComponent("form:datosIntercon:interTercero");
                interTercero.setFilterStyle("display: none; visibility: hidden;");
                interCuenta = (Column) c.getViewRoot().findComponent("form:datosIntercon:interCuenta");
                interCuenta.setFilterStyle("display: none; visibility: hidden;");
                interDebito = (Column) c.getViewRoot().findComponent("form:datosIntercon:interDebito");
                interDebito.setFilterStyle("display: none; visibility: hidden;");
                interConcepto = (Column) c.getViewRoot().findComponent("form:datosIntercon:interConcepto");
                interConcepto.setFilterStyle("display: none; visibility: hidden;");
                interCentroCosto = (Column) c.getViewRoot().findComponent("form:datosIntercon:interCentroCosto");
                interCentroCosto.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosIntercon");
                banderaIntercon = 0;
                filtrarListaInterconInfor = null;
                tipoListaIntercon = 0;
            }
            RequestContext.getCurrentInstance().update("form:datosGenerados");
            RequestContext.getCurrentInstance().update("form:datosIntercon");
        } catch (Exception e) {
            log.warn("Error actionBtnDeshacer Controlador : " + e.toString());
        }
    }

    public void actionBtnEnviar() {
        Date fechaDesde = administrarInterfaseContableInfor.buscarFechaDesdeVWActualesFechas();
        Date fechaHasta = administrarInterfaseContableInfor.buscarFechaHastaVWActualesFechas();
        if (fechaDesde != null && fechaHasta != null) {
            if ((fechaDesde.after(parametroContableActual.getFechainicialcontabilizacion()) && fechaHasta.after(parametroContableActual.getFechafinalcontabilizacion()))
                    || (fechaDesde.before(parametroContableActual.getFechainicialcontabilizacion()) && fechaHasta.before(parametroContableActual.getFechafinalcontabilizacion()))) {
                RequestContext.getCurrentInstance().execute("PF('errorVWActualesFechas').show()");
            } else if (parametroContableActual.getEmpresaRegistro().getSecuencia() != null
                    && parametroContableActual.getFechafinalcontabilizacion() != null
                    && parametroContableActual.getFechainicialcontabilizacion() != null) {
                guardadoGeneral();
                administrarInterfaseContableInfor.ejecutarPKGUbicarnuevointercon_Infor(parametroContableActual.getSecuencia(), parametroContableActual.getFechainicialcontabilizacion(), parametroContableActual.getFechafinalcontabilizacion(), parametroContableActual.getProceso().getSecuencia());
                listaGenerados = null;
                if (listaGenerados == null) {
                    listaGenerados = administrarInterfaseContableInfor.obtenerSolucionesNodosParametroContable(parametroContableActual.getFechainicialcontabilizacion(), parametroContableActual.getFechafinalcontabilizacion());
                    if (listaGenerados != null) {
                        if (listaGenerados.size() > 0) {
                            activarEnviar = false;
                        } else {
                            activarDeshacer = true;
                        }
                    } else {
                        activarDeshacer = true;
                    }
                    getTotalCGenerado();
                    getTotalDGenerado();
                }
                listaInterconInfor = null;
                if (listaInterconInfor == null) {
                    listaInterconInfor = administrarInterfaseContableInfor.obtenerInterconInforParametroContable(parametroContableActual.getFechainicialcontabilizacion(), parametroContableActual.getFechafinalcontabilizacion());
                    if (listaInterconInfor != null) {
                        if (listaInterconInfor.size() > 0) {
                            activarDeshacer = false;
                        } else {
                            activarDeshacer = true;
                        }
                    } else {
                        activarDeshacer = true;
                    }
//                    getTotalCInter();
//                    getTotalDInter();
//                    RequestContext.getCurrentInstance().update("form:totalDInter");
//                    RequestContext.getCurrentInstance().update("form:totalCInter");
                }
                contarRegistroContabilizados();
                contarRegistrosGenerados();
                RequestContext.getCurrentInstance().update("form:totalDGenerado");
                RequestContext.getCurrentInstance().update("form:totalCGenerado");
                activarEnviar = true;
                RequestContext.getCurrentInstance().update("form:btnEnviar");
                RequestContext.getCurrentInstance().update("form:btnDeshacer");
                RequestContext.getCurrentInstance().update("form:datosGenerados");
                RequestContext.getCurrentInstance().update("form:datosIntercon");
            }
        }
    }

    public void anularComprobantesCerrados() {
        try {
            guardadoGeneral();
            administrarInterfaseContableInfor.actualizarFlagInterconInfor(parametroContableActual.getFechainicialcontabilizacion(), parametroContableActual.getFechafinalcontabilizacion(), parametroContableActual.getEmpresaCodigo());
            FacesMessage msg = new FacesMessage("Información", "Se realizo el proceso con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        } catch (Exception e) {
            log.warn("Error anularComprobantesCerrados Controlador : " + e.toString());
            FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el proceso de anulacion");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }
    }

    public void validarFechasProcesoActualizar() {
        Date fechaContabilizacion = administrarInterfaseContableInfor.obtenerMaxFechaContabilizaciones();
        Date fechaInterconSapBo = administrarInterfaseContableInfor.obtenerMaxFechaInterconInfor();
        DateFormat df = DateFormat.getDateInstance(DateFormat.LONG);
        RequestContext context = RequestContext.getCurrentInstance();
        if (fechaContabilizacion != null && fechaInterconSapBo != null) {
            if (fechaContabilizacion.equals(fechaInterconSapBo) == true) {
                String fecha = df.format(fechaContabilizacion);
                msnFechasActualizar = fecha;
                RequestContext.getCurrentInstance().update("form:anteriorContabilizacion");
                RequestContext.getCurrentInstance().execute("PF('anteriorContabilizacion').show()");
            } else {
                RequestContext.getCurrentInstance().update("form:nuncaContabilizo");
                RequestContext.getCurrentInstance().execute("PF('nuncaContabilizo').show()");
            }
        }

    }

    public boolean validarFechasParametros() {
        boolean retorno = false;
        ParametrosEstructuras parametroLiquidacion = administrarInterfaseContableInfor.parametrosLiquidacion();
        if ((parametroLiquidacion.getFechadesdecausado().compareTo(parametroContableActual.getFechainicialcontabilizacion()) == 0)
                && (parametroLiquidacion.getFechahastacausado().compareTo(parametroContableActual.getFechafinalcontabilizacion()) == 0)) {
            retorno = true;
        }
        return retorno;
    }

    public void actionBtnActualizar() {
        RequestContext context = RequestContext.getCurrentInstance();
        listaGenerados = null;
        if (listaGenerados == null) {
            listaGenerados = administrarInterfaseContableInfor.obtenerSolucionesNodosParametroContable(parametroContableActual.getFechainicialcontabilizacion(), parametroContableActual.getFechafinalcontabilizacion());
            if (listaGenerados != null) {
                if (listaGenerados.size() > 0) {
                    activarEnviar = false;
                }
            } else {
                activarEnviar = true;
            }
            RequestContext.getCurrentInstance().update("form:btnEnviar");
            getTotalCGenerado();
            getTotalDGenerado();
            RequestContext.getCurrentInstance().update("form:totalDGenerado");
            RequestContext.getCurrentInstance().update("form:totalCGenerado");
            contarRegistrosGenerados();
        }

        listaInterconInfor = null;
        if (listaInterconInfor == null) {
            listaInterconInfor = administrarInterfaseContableInfor.obtenerInterconInforParametroContable(parametroContableActual.getFechainicialcontabilizacion(), parametroContableActual.getFechafinalcontabilizacion());
            if (listaInterconInfor != null) {
                if (listaInterconInfor.size() > 0) {
                } else {
                    activarDeshacer = true;
                }
            } else {
                activarDeshacer = true;
            }
//            getTotalCInter();
//            getTotalDInter();
            activarDeshacer = false;
            RequestContext.getCurrentInstance().update("form:btnDeshacer");
//            RequestContext.getCurrentInstance().update("form:totalDInter");
//            RequestContext.getCurrentInstance().update("form:totalCInter");
            contarRegistroContabilizados();
        }

        validarFechasProcesoActualizar();
        RequestContext.getCurrentInstance().update("form:datosGenerados");
        RequestContext.getCurrentInstance().update("form:datosIntercon");
        int tam1 = 0;
        int tam2 = 0;
        if (listaGenerados != null) {
            tam1 = listaGenerados.size();
        }
        if (listaInterconInfor != null) {
            tam2 = listaInterconInfor.size();
        }
        if (tam1 == 0 && tam2 == 0) {
            RequestContext.getCurrentInstance().execute("PF('procesoSinDatos').show()");
        }
        log.info("terminó proceso actualizar");
    }

    public void guardarSalir() {
        guardadoGeneral();
        salir();
    }

    public void cancelarSalir() {
        cancelarModificaciones();
        salir();
    }

    public void guardadoGeneral() {
        if (guardado == false) {
            if (cambiosParametro == true) {
                guardarCambiosParametro();
            }
            guardado = true;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
    }

    public void guardarCambiosParametro() {
        RequestContext context = RequestContext.getCurrentInstance();
        try {
            if (parametroContableActual.getArchivo() == null) {
                parametroContableActual.setArchivo("NOMINA");
            }
            if (parametroContableActual.getProceso().getSecuencia() == null) {
                parametroContableActual.setProceso(new Procesos());
            }

            administrarInterfaseContableInfor.modificarParametroContable(parametroContableActual);

            if (!listaGeneradosBorrar.isEmpty()) {
                administrarInterfaseContableInfor.borrarRegistroGenerado(listaGeneradosBorrar);
                listaGeneradosBorrar.clear();
            }

            if (!listaInterconSapBorrar.isEmpty()) {
                administrarInterfaseContableInfor.borrarRegistroIntercon(listaInterconSapBorrar);
                listaInterconSapBorrar.clear();
            }

            listaParametrosContables = null;
            getListaParametrosContables();
            parametroContableActual = null;
            getParametroContableActual();
            cambiosParametro = false;
            FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
            RequestContext.getCurrentInstance().update("form:PanelTotal");
        } catch (Exception e) {
            log.warn("Error guardarCambiosParametro Controlador : " + e.toString());
            FacesMessage msg = new FacesMessage("Información", "Un error ha ocurrido en el guardado, intente nuevamente");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }
    }

    public void cancelarModificaciones() {
        if (banderaGenerado == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            altoTablaGenerada = "80";
            genProceso = (Column) c.getViewRoot().findComponent("form:datosGenerados:genProceso");
            genProceso.setFilterStyle("display: none; visibility: hidden;");
            genEmpleado = (Column) c.getViewRoot().findComponent("form:datosGenerados:genEmpleado");
            genEmpleado.setFilterStyle("display: none; visibility: hidden;");
            genCntCredito = (Column) c.getViewRoot().findComponent("form:datosGenerados:genCntCredito");
            genCntCredito.setFilterStyle("display: none; visibility: hidden;");
            genCntDebito = (Column) c.getViewRoot().findComponent("form:datosGenerados:genCntDebito");
            genCntDebito.setFilterStyle("display: none; visibility: hidden;");
            genTercero = (Column) c.getViewRoot().findComponent("form:datosGenerados:genTercero");
            genTercero.setFilterStyle("display: none; visibility: hidden;");
            genValor = (Column) c.getViewRoot().findComponent("form:datosGenerados:genValor");
            genValor.setFilterStyle("display: none; visibility: hidden;");
            genConcepto = (Column) c.getViewRoot().findComponent("form:datosGenerados:genConcepto");
            genConcepto.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosGenerados");
            banderaGenerado = 0;
            filtrarListaGenerados = null;
            tipoListaGenerada = 0;
        }
        if (banderaIntercon == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            altoTablaIntercon = "95";
            interEmpleado = (Column) c.getViewRoot().findComponent("form:datosIntercon:interEmpleado");
            interEmpleado.setFilterStyle("display: none; visibility: hidden;");
            interTercero = (Column) c.getViewRoot().findComponent("form:datosIntercon:interTercero");
            interTercero.setFilterStyle("display: none; visibility: hidden;");
            interCuenta = (Column) c.getViewRoot().findComponent("form:datosIntercon:interCuenta");
            interCuenta.setFilterStyle("display: none; visibility: hidden;");
            interDebito = (Column) c.getViewRoot().findComponent("form:datosIntercon:interDebito");
            interDebito.setFilterStyle("display: none; visibility: hidden;");
            interConcepto = (Column) c.getViewRoot().findComponent("form:datosIntercon:interConcepto");
            interConcepto.setFilterStyle("display: none; visibility: hidden;");
            interCentroCosto = (Column) c.getViewRoot().findComponent("form:datosIntercon:interCentroCosto");
            interCentroCosto.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosIntercon");
            banderaIntercon = 0;
            filtrarListaInterconInfor = null;
            tipoListaIntercon = 0;
        }
        totalCGenerado = BigDecimal.ZERO;
        totalDGenerado = BigDecimal.ZERO;
        totalDInter = BigDecimal.ZERO;
        totalCInter = BigDecimal.ZERO;
        activarEnviar = true;
        activarDeshacer = true;
        aceptar = true;
        listParametrosContablesBorrar.clear();
        actualUsuarioBD = null;
        getActualUsuarioBD();
        listaParametrosContables = null;
        getListaParametrosContables();
        parametroContableActual = null;
        listaGenerados = null;
        listaInterconInfor = null;
        getParametroContableActual();
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:btnEnviar");
        RequestContext.getCurrentInstance().update("form:btnDeshacer");
        RequestContext.getCurrentInstance().update("form:PLANO");
        RequestContext.getCurrentInstance().update("form:PanelTotal");
        cambiosParametro = false;
        guardado = true;
        parametroContableActual = null;
        generadoTablaSeleccionado = null;
        interconTablaSeleccionada = null;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void editarCelda() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (parametroContableActual != null) {
            if (cualCeldaParametroContable == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarEmpresaParametro");
                RequestContext.getCurrentInstance().execute("PF('editarEmpresaParametro').show()");
                parametroContableActual = null;
            } else if (cualCeldaParametroContable == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarDocumentoParametro");
                RequestContext.getCurrentInstance().execute("PF('editarDocumentoParametro').show()");
                parametroContableActual = null;
            } else if (cualCeldaParametroContable == 2) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarProcesoParametro");
                RequestContext.getCurrentInstance().execute("PF('editarProcesoParametro').show()");
                parametroContableActual = null;
            } else if (cualCeldaParametroContable == 3) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarResXML");
                RequestContext.getCurrentInstance().execute("PF('editarResXML').show()");
                parametroContableActual = null;
            } else if (cualCeldaParametroContable == 4) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaInicialParametro");
                RequestContext.getCurrentInstance().execute("PF('editarFechaInicialParametro').show()");
                parametroContableActual = null;
            } else if (cualCeldaParametroContable == 5) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaFinalParametro");
                RequestContext.getCurrentInstance().execute("PF('editarFechaFinalParametro').show()");
                parametroContableActual = null;
            }
        } else if (generadoTablaSeleccionado != null) {
            editarGenerado = generadoTablaSeleccionado;
            if (cualCeldaGenerado == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarProcesoGenerado");
                RequestContext.getCurrentInstance().execute("PF('editarProcesoGenerado').show()");
                cualCeldaGenerado = -1;
            } else if (cualCeldaGenerado == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarEmpleadoGenerado");
                RequestContext.getCurrentInstance().execute("PF('editarEmpleadoGenerado').show()");
                cualCeldaGenerado = -1;
            } else if (cualCeldaGenerado == 2) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarCreditoGenerado");
                RequestContext.getCurrentInstance().execute("PF('editarCreditoGenerado').show()");
                cualCeldaGenerado = -1;
            } else if (cualCeldaGenerado == 3) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarDebitoGenerado");
                RequestContext.getCurrentInstance().execute("PF('editarDebitoGenerado').show()");
                cualCeldaGenerado = -1;
            } else if (cualCeldaGenerado == 4) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarTerceroGenerado");
                RequestContext.getCurrentInstance().execute("PF('editarTerceroGenerado').show()");
                cualCeldaGenerado = -1;
            } else if (cualCeldaGenerado == 5) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarValorGenerado");
                RequestContext.getCurrentInstance().execute("PF('editarValorGenerado').show()");
                cualCeldaGenerado = -1;
            } else if (cualCeldaGenerado == 6) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarConceptoGenerado");
                RequestContext.getCurrentInstance().execute("PF('editarConceptoGenerado').show()");
                cualCeldaGenerado = -1;
            }
        } else if (interconTablaSeleccionada != null) {
            editarIntercon = interconTablaSeleccionada;
            if (cualCeldaIntercon == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarEmpleadoIntercon");
                RequestContext.getCurrentInstance().execute("PF('editarEmpleadoIntercon').show()");
                cualCeldaIntercon = -1;
            } else if (cualCeldaIntercon == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarTerceroIntercon");
                RequestContext.getCurrentInstance().execute("PF('editarTerceroIntercon').show()");
                cualCeldaIntercon = -1;
            } else if (cualCeldaIntercon == 2) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarCuentaIntercon");
                RequestContext.getCurrentInstance().execute("PF('editarCuentaIntercon').show()");
                cualCeldaIntercon = -1;
            } else if (cualCeldaIntercon == 3) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarDebitoIntercon");
                RequestContext.getCurrentInstance().execute("PF('editarDebitoIntercon').show()");
                cualCeldaIntercon = -1;
            } else if (cualCeldaIntercon == 4) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarCreditoIntercon");
                RequestContext.getCurrentInstance().execute("PF('editarCreditoIntercon').show()");
                cualCeldaIntercon = -1;
            } else if (cualCeldaIntercon == 5) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarConceptoIntercon");
                RequestContext.getCurrentInstance().execute("PF('editarConceptoIntercon').show()");
                cualCeldaIntercon = -1;
            } else if (cualCeldaIntercon == 6) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarCentroCostoIntercon");
                RequestContext.getCurrentInstance().execute("PF('editarCentroCostoIntercon').show()");
                cualCeldaIntercon = -1;
            }
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void salir() {
        limpiarListasValor();
        if (banderaGenerado == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            altoTablaGenerada = "80";
            genProceso = (Column) c.getViewRoot().findComponent("form:datosGenerados:genProceso");
            genProceso.setFilterStyle("display: none; visibility: hidden;");
            genEmpleado = (Column) c.getViewRoot().findComponent("form:datosGenerados:genEmpleado");
            genEmpleado.setFilterStyle("display: none; visibility: hidden;");
            genCntCredito = (Column) c.getViewRoot().findComponent("form:datosGenerados:genCntCredito");
            genCntCredito.setFilterStyle("display: none; visibility: hidden;");
            genCntDebito = (Column) c.getViewRoot().findComponent("form:datosGenerados:genCntDebito");
            genCntDebito.setFilterStyle("display: none; visibility: hidden;");
            genTercero = (Column) c.getViewRoot().findComponent("form:datosGenerados:genTercero");
            genTercero.setFilterStyle("display: none; visibility: hidden;");
            genValor = (Column) c.getViewRoot().findComponent("form:datosGenerados:genValor");
            genValor.setFilterStyle("display: none; visibility: hidden;");
            genConcepto = (Column) c.getViewRoot().findComponent("form:datosGenerados:genConcepto");
            genConcepto.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosGenerados");
            banderaGenerado = 0;
            filtrarListaGenerados = null;
            tipoListaGenerada = 0;
        }
        if (banderaIntercon == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            altoTablaIntercon = "95";
            interEmpleado = (Column) c.getViewRoot().findComponent("form:datosIntercon:interEmpleado");
            interEmpleado.setFilterStyle("display: none; visibility: hidden;");
            interTercero = (Column) c.getViewRoot().findComponent("form:datosIntercon:interTercero");
            interTercero.setFilterStyle("display: none; visibility: hidden;");
            interCuenta = (Column) c.getViewRoot().findComponent("form:datosIntercon:interCuenta");
            interCuenta.setFilterStyle("display: none; visibility: hidden;");
            interDebito = (Column) c.getViewRoot().findComponent("form:datosIntercon:interDebito");
            interDebito.setFilterStyle("display: none; visibility: hidden;");
            interConcepto = (Column) c.getViewRoot().findComponent("form:datosIntercon:interConcepto");
            interConcepto.setFilterStyle("display: none; visibility: hidden;");
            interCentroCosto = (Column) c.getViewRoot().findComponent("form:datosIntercon:interCentroCosto");
            interCentroCosto.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosIntercon");
            banderaIntercon = 0;
            filtrarListaInterconInfor = null;
            tipoListaIntercon = 0;
        }
        RequestContext context = RequestContext.getCurrentInstance();
        listParametrosContablesBorrar.clear();
        listaParametrosContables = null;
        getListaParametrosContables();
        parametroContableActual = null;
        listaGenerados = null;
        listaInterconInfor = null;
        actualUsuarioBD = null;
        cambiosParametro = false;
        guardado = true;
        parametroContableActual = null;
        generadoTablaSeleccionado = null;
        interconTablaSeleccionada = null;
        activarEnviar = true;
        activarDeshacer = true;
        totalCGenerado = BigDecimal.ZERO;
        totalDGenerado = BigDecimal.ZERO;
        totalDInter = BigDecimal.ZERO;
        totalCInter = BigDecimal.ZERO;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void asignarIndex(ParametrosContables paramc, int numeroDialogo, int tipoNuevo) {
        tipoActualizacion = tipoNuevo;
        parametroContableActual = paramc;
        RequestContext context = RequestContext.getCurrentInstance();
        if (numeroDialogo == 0) {
            lovEmpresas = null;
            cargarLovEmpresas();
            RequestContext.getCurrentInstance().update("form:EmpresaDialogo");
            RequestContext.getCurrentInstance().execute("PF('EmpresaDialogo').show()");
        } else if (numeroDialogo == 1) {
            lovProcesos = null;
            cargarLovProcesos();
            RequestContext.getCurrentInstance().update("form:ProcesoDialogo");
            RequestContext.getCurrentInstance().execute("PF('ProcesoDialogo').show()");
        }
    }

    public void actualizarEmpresa() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            parametroContableActual.setEmpresaRegistro(empresaSeleccionada);
            parametroContableActual.setEmpresaCodigo(empresaSeleccionada.getCodigo());
            parametroContableActual = null;
            guardado = false;
            cambiosParametro = true;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:parametroEmpresa");
        }
        if (tipoActualizacion == 1) {
            nuevoParametroContable.setEmpresaRegistro(empresaSeleccionada);
            nuevoParametroContable.setEmpresaCodigo(empresaSeleccionada.getCodigo());
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaEmpresaParametro");
        }
        empresaSeleccionada = new Empresas();
        filtrarLovEmpresas = null;
        aceptar = true;

        RequestContext.getCurrentInstance().update("form:EmpresaDialogo");
        RequestContext.getCurrentInstance().update("form:lovEmpresa");
        RequestContext.getCurrentInstance().update("form:aceptarE");
        context.reset("form:lovEmpresa:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovEmpresa').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('EmpresaDialogo').hide()");

    }

    public void cancelarEmpresa() {
        empresaSeleccionada = new Empresas();
        filtrarLovEmpresas = null;
        parametroContableActual = null;
        permitirIndexParametro = true;
        aceptar = true;
        tipoActualizacion = -1;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:EmpresaDialogo");
        RequestContext.getCurrentInstance().update("form:lovEmpresa");
        RequestContext.getCurrentInstance().update("form:aceptarE");
        context.reset("form:lovEmpresa:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovEmpresa').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('EmpresaDialogo').hide()");
    }

    public void actualizarProceso() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            parametroContableActual.setProceso(procesoSeleccionado);
            guardado = false;
            cambiosParametro = true;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:parametroProceso");
        }
        procesoSeleccionado = null;
        filtrarLovProcesos = null;
        aceptar = true;
        RequestContext.getCurrentInstance().update("form:ProcesoDialogo");
        RequestContext.getCurrentInstance().update("form:lovProceso");
        RequestContext.getCurrentInstance().update("form:aceptarP");
        context.reset("form:lovProceso:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovProceso').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('ProcesoDialogo').hide()");
    }

    public void cancelarProceso() {
        aceptar = true;
        procesoSeleccionado = new Procesos();
        filtrarLovProcesos = null;
        parametroContableActual = null;
        permitirIndexParametro = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:ProcesoDialogo");
        RequestContext.getCurrentInstance().update("form:lovProceso");
        RequestContext.getCurrentInstance().update("form:aceptarP");
        context.reset("form:lovProceso:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovProceso').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('ProcesoDialogo').hide()");
    }

    public void listaValoresBoton() {
        if (parametroContableActual != null) {
            RequestContext context = RequestContext.getCurrentInstance();
            if (cualCeldaParametroContable == 0) {
                lovEmpresas = null;
                cargarLovEmpresas();
                RequestContext.getCurrentInstance().update("form:EmpresaDialogo");
                RequestContext.getCurrentInstance().execute("PF('EmpresaDialogo').show()");
            }
            if (cualCeldaParametroContable == 1) {
                lovProcesos = null;
                cargarLovProcesos();
                RequestContext.getCurrentInstance().update("form:ProcesoDialogo");
                RequestContext.getCurrentInstance().execute("PF('ProcesoDialogo').show()");
            }
        }
    }

    public void borrarRegistro() {
        if (cualTabla == 1) {
            borrarRegistroGenerado();
        } else if (cualTabla == 2) {
            borrarRegistroContabilizado();
        }
    }

    public void borrarRegistroGenerado() {
        if (generadoTablaSeleccionado != null) {
            listaGeneradosBorrar.add(generadoTablaSeleccionado);
            totalCGenerado = totalCGenerado.subtract(generadoTablaSeleccionado.getValor());
            totalDGenerado = totalDGenerado.subtract(generadoTablaSeleccionado.getValor());
            listaGenerados.remove(generadoTablaSeleccionado);
            if (tipoListaGenerada == 1) {
                filtrarListaGenerados.remove(generadoTablaSeleccionado);
            }
            getTotalCGenerado();
            getTotalDGenerado();
            RequestContext.getCurrentInstance().update("form:totalCGenerado");
            RequestContext.getCurrentInstance().update("form:totalDGenerado");
            RequestContext.getCurrentInstance().update("form:datosGenerados");
            contarRegistrosGenerados();
            generadoTablaSeleccionado = null;
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void borrarRegistroContabilizado() {
        if (interconTablaSeleccionada != null) {
            listaInterconSapBorrar.add(interconTablaSeleccionada);
            totalCInter = totalCInter.subtract(interconTablaSeleccionada.getValor());
            totalDInter = totalDInter.subtract(interconTablaSeleccionada.getValor());
            listaInterconInfor.remove(interconTablaSeleccionada);
            if (tipoListaIntercon == 1) {
                filtrarListaInterconInfor.remove(interconTablaSeleccionada);
            }
//            getTotalCInter();
//            getTotalDInter();
//            RequestContext.getCurrentInstance().update("form:totalDInter");
//            RequestContext.getCurrentInstance().update("form:totalCInter");
            RequestContext.getCurrentInstance().update("form:datosIntercon");
            contarRegistroContabilizados();
            generadoTablaSeleccionado = null;
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void agregarNuevoParametro() {
        RequestContext context = RequestContext.getCurrentInstance();
        try {
            if (nuevoParametroContable.getEmpresaRegistro().getSecuencia() != null && nuevoParametroContable.getFechafinalcontabilizacion() != null && nuevoParametroContable.getFechainicialcontabilizacion() != null) {
                boolean validar = validarFechaParametro(1);
                if (validar == true) {
                    RequestContext.getCurrentInstance().execute("PF('NuevoRegistroPC').hide()");
                    int k = 1;
                    BigInteger var = new BigInteger(String.valueOf(k));
                    nuevoParametroContable.setSecuencia(var);
                    if (nuevoParametroContable.getProceso().getSecuencia() == null) {
                        nuevoParametroContable.setProceso(new Procesos());
                    }
                    administrarInterfaseContableInfor.crearParametroContable(nuevoParametroContable);
                    nuevoParametroContable = new ParametrosContables();
                    activarAgregar = true;
                    activarOtros = false;
                    listaParametrosContables = null;
                    getListaParametrosContables();
                    parametroContableActual = null;
                    getParametroContableActual();
                    listaGenerados = null;
                    listaInterconInfor = null;
                    activarEnviar = true;
                    activarDeshacer = true;
                    totalCGenerado = BigDecimal.ZERO;
                    totalDGenerado = BigDecimal.ZERO;
                    totalDInter = BigDecimal.ZERO;
                    totalCInter = BigDecimal.ZERO;
                    RequestContext.getCurrentInstance().update("form:btnEnviar");
                    RequestContext.getCurrentInstance().update("form:btnDeshacer");
                    RequestContext.getCurrentInstance().update("form:PLANO");
                    if (banderaGenerado == 1) {
                        FacesContext c = FacesContext.getCurrentInstance();
                        altoTablaGenerada = "80";
                        genProceso = (Column) c.getViewRoot().findComponent("form:datosGenerados:genProceso");
                        genProceso.setFilterStyle("display: none; visibility: hidden;");
                        genEmpleado = (Column) c.getViewRoot().findComponent("form:datosGenerados:genEmpleado");
                        genEmpleado.setFilterStyle("display: none; visibility: hidden;");
                        genCntCredito = (Column) c.getViewRoot().findComponent("form:datosGenerados:genCntCredito");
                        genCntCredito.setFilterStyle("display: none; visibility: hidden;");
                        genCntDebito = (Column) c.getViewRoot().findComponent("form:datosGenerados:genCntDebito");
                        genCntDebito.setFilterStyle("display: none; visibility: hidden;");
                        genTercero = (Column) c.getViewRoot().findComponent("form:datosGenerados:genTercero");
                        genTercero.setFilterStyle("display: none; visibility: hidden;");
                        genValor = (Column) c.getViewRoot().findComponent("form:datosGenerados:genValor");
                        genValor.setFilterStyle("display: none; visibility: hidden;");
                        genConcepto = (Column) c.getViewRoot().findComponent("form:datosGenerados:genConcepto");
                        genConcepto.setFilterStyle("display: none; visibility: hidden;");
                        RequestContext.getCurrentInstance().update("form:datosGenerados");
                        banderaGenerado = 0;
                        filtrarListaGenerados = null;
                        tipoListaGenerada = 0;
                    }
                    if (banderaIntercon == 1) {
                        FacesContext c = FacesContext.getCurrentInstance();
                        altoTablaIntercon = "95";
                        interEmpleado = (Column) c.getViewRoot().findComponent("form:datosIntercon:interEmpleado");
                        interEmpleado.setFilterStyle("display: none; visibility: hidden;");
                        interTercero = (Column) c.getViewRoot().findComponent("form:datosIntercon:interTercero");
                        interTercero.setFilterStyle("display: none; visibility: hidden;");
                        interCuenta = (Column) c.getViewRoot().findComponent("form:datosIntercon:interCuenta");
                        interCuenta.setFilterStyle("display: none; visibility: hidden;");
                        interDebito = (Column) c.getViewRoot().findComponent("form:datosIntercon:interDebito");
                        interDebito.setFilterStyle("display: none; visibility: hidden;");
                        interConcepto = (Column) c.getViewRoot().findComponent("form:datosIntercon:interConcepto");
                        interConcepto.setFilterStyle("display: none; visibility: hidden;");
                        interCentroCosto = (Column) c.getViewRoot().findComponent("form:datosIntercon:interCentroCosto");
                        interCentroCosto.setFilterStyle("display: none; visibility: hidden;");
                        RequestContext.getCurrentInstance().update("form:datosIntercon");
                        banderaIntercon = 0;
                        filtrarListaInterconInfor = null;
                        tipoListaIntercon = 0;
                    }
                    RequestContext.getCurrentInstance().update("form:PanelTotal");
                    FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                    RequestContext.getCurrentInstance().update("form:growl");
                } else {
                    RequestContext.getCurrentInstance().execute("PF('errorFechasParametro').show()");
                }
            } else {
                RequestContext.getCurrentInstance().execute("PF('errorNewRegNull').show()");
            }
        } catch (Exception e) {
            log.warn("Error Controlador agregarNuevo : " + e.toString());
            FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado, intente nuevamente");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }
    }

    public void limpiarNuevoParametro() {
        nuevoParametroContable = new ParametrosContables();
        nuevoParametroContable.setEmpresaRegistro(new Empresas());
        nuevoParametroContable.setProceso(new Procesos());
    }

    public void modificarTipoPlano(int tipo) {
        tipoPlano = tipo;
        guardadoGeneral();
        RequestContext.getCurrentInstance().update("form:tipoPlano");

    }

    public void validarExportPDF() throws IOException {
        if (parametroContableActual != null) {
            exportPDF_PC();
        }
        if (cualTabla == 1) {
            exportPDF_G();
        }
        if (cualTabla == 2) {
            exportPDF_I();
        }
    }

    public void exportPDF_PC() throws IOException {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formExportar:datosParametroExportar");
        FacesContext context = c;
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "ParametrosContables_PDF", false, false, "UTF-8", null, null);
        context.responseComplete();
        generadoTablaSeleccionado = null;
    }

    public void exportPDF_G() throws IOException {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formExportar:datosGenerarExportar");
        FacesContext context = c;
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "Generados_PDF", false, false, "UTF-8", null, null);
        context.responseComplete();
        generadoTablaSeleccionado = null;
    }

    public void exportPDF_I() throws IOException {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formExportar:datosInterconExportar");
        FacesContext context = c;
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "InterconInfor_PDF", false, false, "UTF-8", null, null);
        context.responseComplete();
        interconTablaSeleccionada = null;
    }

    public void validarExportXLS() throws IOException {
        if (parametroContableActual != null) {
            exportXLS_PC();
        }
        if (cualTabla == 1) {
            exportXLS_G();
        }
        if (cualTabla == 2) {
            exportXLS_I();
        }
    }

    public void exportXLS_PC() throws IOException {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formExportar:datosParametroExportar");
        FacesContext context = c;
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "ParametrosContables_XLS", false, false, "UTF-8", null, null);
        context.responseComplete();
        parametroContableActual = null;
    }

    public void exportXLS_G() throws IOException {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formExportar:datosGenerarExportar");
        FacesContext context = c;
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "Generados_XLS", false, false, "UTF-8", null, null);
        context.responseComplete();
        generadoTablaSeleccionado = null;
    }

    public void exportXLS_I() throws IOException {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formExportar:datosInterconExportar");
        FacesContext context = c;
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "InterconInfor_XLS", false, false, "UTF-8", null, null);
        context.responseComplete();
        interconTablaSeleccionada = null;
    }

    public String validarExportXML() {
        String tabla = "";
        if (parametroContableActual != null) {
            tabla = ":formExportar:datosParametroExportar";
        }
        if (generadoTablaSeleccionado != null) {
            tabla = ":formExportar:datosGenerarExportar";
        }
        if (interconTablaSeleccionada != null) {
            tabla = ":formExportar:datosInterconExportar";
        }
        return tabla;
    }

    public String validarNombreExportXML() {
        String nombre = "";
        if (parametroContableActual != null) {
            nombre = "ParametrosContables_XML";
        }
        if (cualTabla == 1) {
            nombre = "Generados_XML";
        }
        if (cualTabla == 2) {
            nombre = "InterconInfor_XML";
        }
        return nombre;
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (banderaGenerado == 0 && banderaIntercon == 0) {
            altoTablaGenerada = "60";
            genProceso = (Column) c.getViewRoot().findComponent("form:datosGenerados:genProceso");
            genProceso.setFilterStyle("width: 85% !important;");
            genEmpleado = (Column) c.getViewRoot().findComponent("form:datosGenerados:genEmpleado");
            genEmpleado.setFilterStyle("width: 85% !important;");
            genCntCredito = (Column) c.getViewRoot().findComponent("form:datosGenerados:genCntCredito");
            genCntCredito.setFilterStyle("width: 85% !important;");
            genCntDebito = (Column) c.getViewRoot().findComponent("form:datosGenerados:genCntDebito");
            genCntDebito.setFilterStyle("width: 85% !important;");
            genTercero = (Column) c.getViewRoot().findComponent("form:datosGenerados:genTercero");
            genTercero.setFilterStyle("width: 85% !important;");
            genValor = (Column) c.getViewRoot().findComponent("form:datosGenerados:genValor");
            genValor.setFilterStyle("width: 85% !important;");
            genConcepto = (Column) c.getViewRoot().findComponent("form:datosGenerados:genConcepto");
            genConcepto.setFilterStyle("width: 85% !important;");

            altoTablaIntercon = "75";
            interEmpleado = (Column) c.getViewRoot().findComponent("form:datosIntercon:interEmpleado");
            interEmpleado.setFilterStyle("width: 85% !important;");
            interTercero = (Column) c.getViewRoot().findComponent("form:datosIntercon:interTercero");
            interTercero.setFilterStyle("width: 85% !important;");
            interCuenta = (Column) c.getViewRoot().findComponent("form:datosIntercon:interCuenta");
            interCuenta.setFilterStyle("width: 85% !important;");
            interDebito = (Column) c.getViewRoot().findComponent("form:datosIntercon:interDebito");
            interDebito.setFilterStyle("width: 85% !important;");
            interConcepto = (Column) c.getViewRoot().findComponent("form:datosIntercon:interConcepto");
            interConcepto.setFilterStyle("width: 85% !important;");
            interCentroCosto = (Column) c.getViewRoot().findComponent("form:datosIntercon:interCentroCosto");
            interCentroCosto.setFilterStyle("width: 85% !important;");
            banderaGenerado = 1;
            banderaIntercon = 1;
            RequestContext.getCurrentInstance().update("form:datosIntercon");
            RequestContext.getCurrentInstance().update("form:datosGenerados");
        } else if (banderaGenerado == 1 && banderaIntercon == 1) {
            altoTablaGenerada = "80";
            altoTablaIntercon = "95";
            genProceso = (Column) c.getViewRoot().findComponent("form:datosGenerados:genProceso");
            genProceso.setFilterStyle("display: none; visibility: hidden;");
            genEmpleado = (Column) c.getViewRoot().findComponent("form:datosGenerados:genEmpleado");
            genEmpleado.setFilterStyle("display: none; visibility: hidden;");
            genCntCredito = (Column) c.getViewRoot().findComponent("form:datosGenerados:genCntCredito");
            genCntCredito.setFilterStyle("display: none; visibility: hidden;");
            genCntDebito = (Column) c.getViewRoot().findComponent("form:datosGenerados:genCntDebito");
            genCntDebito.setFilterStyle("display: none; visibility: hidden;");
            genTercero = (Column) c.getViewRoot().findComponent("form:datosGenerados:genTercero");
            genTercero.setFilterStyle("display: none; visibility: hidden;");
            genValor = (Column) c.getViewRoot().findComponent("form:datosGenerados:genValor");
            genValor.setFilterStyle("display: none; visibility: hidden;");
            genConcepto = (Column) c.getViewRoot().findComponent("form:datosGenerados:genConcepto");
            genConcepto.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosGenerados");

            interEmpleado = (Column) c.getViewRoot().findComponent("form:datosIntercon:interEmpleado");
            interEmpleado.setFilterStyle("display: none; visibility: hidden;");
            interTercero = (Column) c.getViewRoot().findComponent("form:datosIntercon:interTercero");
            interTercero.setFilterStyle("display: none; visibility: hidden;");
            interCuenta = (Column) c.getViewRoot().findComponent("form:datosIntercon:interCuenta");
            interCuenta.setFilterStyle("display: none; visibility: hidden;");
            interDebito = (Column) c.getViewRoot().findComponent("form:datosIntercon:interDebito");
            interDebito.setFilterStyle("display: none; visibility: hidden;");
            interConcepto = (Column) c.getViewRoot().findComponent("form:datosIntercon:interConcepto");
            interConcepto.setFilterStyle("display: none; visibility: hidden;");
            interCentroCosto = (Column) c.getViewRoot().findComponent("form:datosIntercon:interCentroCosto");
            interCentroCosto.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosIntercon");
            banderaIntercon = 0;
            filtrarListaInterconInfor = null;
            tipoListaIntercon = 0;
            banderaGenerado = 0;
            filtrarListaGenerados = null;
            tipoListaGenerada = 0;
        }
    }

    public void validarRastro() {
        if (interconTablaSeleccionada != null) {
            verificarRastro();
        }
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (interconTablaSeleccionada != null) {
            int resultado = administrarRastros.obtenerTabla(interconTablaSeleccionada.getSecuencia(), "INTERCON_SAPBO");
            interconTablaSeleccionada.getSecuencia();
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
        } else if (administrarRastros.verificarHistoricosTabla("INTERCON_SAPBO")) {
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
    }

    public void contarRegistroEmpresas() {
        RequestContext.getCurrentInstance().update("form:infoRegistroEmpresa");
    }

    public void contarRegistroProcesos() {
        RequestContext.getCurrentInstance().update("form:infoRegistroProceso");
    }

    public void contarRegistroContabilizados() {
        RequestContext.getCurrentInstance().update("form:infoRegistroContabilizados");
    }

    public void contarRegistrosGenerados() {
        RequestContext.getCurrentInstance().update("form:infoRegistroGenerados");
    }

    public void eventoFiltrarGenerados() {
        contarRegistrosGenerados();
    }

    public void eventoFiltrarContabilizados() {
        contarRegistroContabilizados();
    }

    public AsynchronousFilllListener listener() {
        return new AsynchronousFilllListener() {
            //RequestContext context = c;

            @Override
            public void reportFinished(JasperPrint jp) {
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
                estadoReporte = true;
                resultadoReporte = "Cancelación";
            }

            @Override
            public void reportFillError(Throwable e) {
                if (e.getCause() != null) {
                    pathReporteGenerado = "ControlInterfaseContableTotal reportFillError Error: " + e.toString() + "\n" + e.getCause().toString();
                } else {
                    pathReporteGenerado = "ControlInterfaseContableTotal reportFillError Error: " + e.toString();
                }
                estadoReporte = true;
                resultadoReporte = "Se estallo";
            }
        };
    }

    public void validarDescargaReporte() {
        try {
            RequestContext.getCurrentInstance().execute("PF('generandoReporte').show()");
            RequestContext context = RequestContext.getCurrentInstance();
            nombreReporte = "CifraControl";
            tipoReporte = "PDF";
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            String auxfechainicial = formatoFecha.format(parametroContableActual.getFechainicialcontabilizacion());
            String auxfechafinal = formatoFecha.format(parametroContableActual.getFechafinalcontabilizacion());

            Map paramFechas = new HashMap();
            paramFechas.put("fechaDesde", auxfechainicial);
            paramFechas.put("fechaHasta", auxfechafinal);

            pathReporteGenerado = administarReportes.generarReporteCifraControl(nombreReporte, tipoReporte, paramFechas);
            RequestContext.getCurrentInstance().execute("PF('generandoReporte').hide()");
            if (pathReporteGenerado != null && !pathReporteGenerado.startsWith("Error:")) {
                if (tipoReporte.equals("PDF")) {
                    FileInputStream fis;
                    try {
                        fis = new FileInputStream(new File(pathReporteGenerado));
                        reporte = new DefaultStreamedContent(fis, "application/pdf");
                        if (reporte != null) {
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
                        log.warn("Error en valdiar descargar reporte : " + ex.getMessage());
                        RequestContext.getCurrentInstance().update("formularioDialogos:errorGenerandoReporte");
                        RequestContext.getCurrentInstance().execute("PF('errorGenerandoReporte').show()");
                        reporte = null;
                    }
                }
            } else {
                RequestContext.getCurrentInstance().update("formularioDialogos:errorGenerandoReporte");
                RequestContext.getCurrentInstance().execute("PF('errorGenerandoReporte').show()");
            }
        } catch (Exception e) {
            log.warn("Error en validar descargar Reporte");
            RequestContext.getCurrentInstance().execute("PF('errorCifraControl').show()");
        }
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
                RequestContext.getCurrentInstance().update("formularioDialogos:errorGenerandoReporte");
                RequestContext.getCurrentInstance().execute("PF('errorGenerandoReporte').show()");
            }
        } catch (Exception e) {
            log.warn("Error en exportarReporte :" + e.getMessage());
        }
    }

    public void reiniciarStreamedContent() {
        reporte = null;
    }

    public void cancelarReporte() {
        administarReportes.cancelarReporte();
    }

    public void cargarLovEmpresas() {
        if (lovEmpresas == null) {
            lovEmpresas = administrarInterfaseContableInfor.lovEmpresas();
        }
    }

    public void cargarLovProcesos() {
        if (lovProcesos == null) {
            lovProcesos = administrarInterfaseContableInfor.lovProcesos();
        }
    }

    /////////////////////SETS Y GETS/////////////////////////////
    public ActualUsuario getActualUsuarioBD() {
        if (actualUsuarioBD == null) {
            actualUsuarioBD = administrarInterfaseContableInfor.obtenerActualUsuario();
        }
        return actualUsuarioBD;
    }

    public void setActualUsuarioBD(ActualUsuario actualUsuarioBD) {
        this.actualUsuarioBD = actualUsuarioBD;
    }

    public ParametrosContables getParametroContableActual() {
        if (parametroContableActual == null) {
            getListaParametrosContables();
            if (listaParametrosContables != null) {
                if (listaParametrosContables.size() > 0) {
                    parametroContableActual = listaParametrosContables.get(0);
                }
            }
        }
        return parametroContableActual;
    }

    public void setParametroContableActual(ParametrosContables parametroContable) {
        this.parametroContableActual = parametroContable;
    }

    public List<Empresas> getLovEmpresas() {
        return lovEmpresas;
    }

    public void setLovEmpresas(List<Empresas> lovEmpresas) {
        this.lovEmpresas = lovEmpresas;
    }

    public List<Empresas> getFiltrarLovEmpresas() {
        return filtrarLovEmpresas;
    }

    public void setFiltrarLovEmpresas(List<Empresas> filtrarLovEmpresas) {
        this.filtrarLovEmpresas = filtrarLovEmpresas;
    }

    public Empresas getEmpresaSeleccionada() {
        return empresaSeleccionada;
    }

    public void setEmpresaSeleccionada(Empresas empresaSeleccionada) {
        this.empresaSeleccionada = empresaSeleccionada;
    }

    public List<Procesos> getLovProcesos() {
        return lovProcesos;
    }

    public void setLovProcesos(List<Procesos> lovProcesos) {
        this.lovProcesos = lovProcesos;
    }

    public List<Procesos> getFiltrarLovProcesos() {
        return filtrarLovProcesos;
    }

    public void setFiltrarLovProcesos(List<Procesos> filtrarLovProcesos) {
        this.filtrarLovProcesos = filtrarLovProcesos;
    }

    public Procesos getProcesoSeleccionado() {
        return procesoSeleccionado;
    }

    public void setProcesoSeleccionado(Procesos procesoSeleccionado) {
        this.procesoSeleccionado = procesoSeleccionado;
    }

    public String getPaginaAnterior() {
        return paginaAnterior;
    }

    public void setPaginaAnterior(String paginaAnterior) {
        this.paginaAnterior = paginaAnterior;
    }

    public boolean isGuardado() {
        return guardado;
    }

    public void setGuardado(boolean guardado) {
        this.guardado = guardado;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public void setAceptar(boolean aceptar) {
        this.aceptar = aceptar;
    }

    public String getInfoRegistroEmpresa() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovEmpresa");
        infoRegistroEmpresa = String.valueOf(tabla.getRowCount());
        return infoRegistroEmpresa;
    }

    public void setInfoRegistroEmpresa(String infoRegistroEmpresa) {
        this.infoRegistroEmpresa = infoRegistroEmpresa;
    }

    public String getInfoRegistroProceso() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovProceso");
        infoRegistroProceso = String.valueOf(tabla.getRowCount());
        return infoRegistroProceso;
    }

    public void setInfoRegistroProceso(String infoRegistroProceso) {
        this.infoRegistroProceso = infoRegistroProceso;
    }

    public List<ParametrosContables> getListaParametrosContables() {
        if (listaParametrosContables == null) {
            getActualUsuarioBD();
            if (actualUsuarioBD.getSecuencia() != null) {
                listaParametrosContables = administrarInterfaseContableInfor.obtenerParametrosContablesUsuarioBD(actualUsuarioBD.getAlias());
            }
            if (listaParametrosContables != null) {
                if (listaParametrosContables.size() > 0) {
                    registroActual = 0;
                }
                if (listaParametrosContables.size() == 0) {
                    activarAgregar = false;
                    activarOtros = true;
                }
            }
        }
        return listaParametrosContables;
    }

    public void setListaParametrosContables(List<ParametrosContables> listaParametrosContables) {
        this.listaParametrosContables = listaParametrosContables;
    }

    public List<SolucionesNodos> getListaGenerados() {
        return listaGenerados;
    }

    public void setListaGenerados(List<SolucionesNodos> listaGenerados) {
        this.listaGenerados = listaGenerados;
    }

    public List<SolucionesNodos> getFiltrarListaGenerados() {
        return filtrarListaGenerados;
    }

    public void setFiltrarListaGenerados(List<SolucionesNodos> filtrarListaGenerados) {
        this.filtrarListaGenerados = filtrarListaGenerados;
    }

    public SolucionesNodos getGeneradoTablaSeleccionado() {

        return generadoTablaSeleccionado;
    }

    public void setGeneradoTablaSeleccionado(SolucionesNodos generadoTablaSeleccionado) {
        this.generadoTablaSeleccionado = generadoTablaSeleccionado;
    }

    public SolucionesNodos getEditarGenerado() {
        return editarGenerado;
    }

    public void setEditarGenerado(SolucionesNodos editarGenerado) {
        this.editarGenerado = editarGenerado;
    }

    public boolean isActivarAgregar() {
        return activarAgregar;
    }

    public void setActivarAgregar(boolean activarAgregar) {
        this.activarAgregar = activarAgregar;
    }

    public boolean isActivarOtros() {
        return activarOtros;
    }

    public void setActivarOtros(boolean activarOtros) {
        this.activarOtros = activarOtros;
    }

    public ParametrosContables getNuevoParametroContable() {
        return nuevoParametroContable;
    }

    public void setNuevoParametroContable(ParametrosContables nuevoParametroContable) {
        this.nuevoParametroContable = nuevoParametroContable;
    }

    public String getAltoTablaGenerada() {
        return altoTablaGenerada;
    }

    public void setAltoTablaGenerada(String altoTablaGenerada) {
        this.altoTablaGenerada = altoTablaGenerada;
    }

    public List<ParametrosContables> getListParametrosContablesBorrar() {
        return listParametrosContablesBorrar;
    }

    public void setListParametrosContablesBorrar(List<ParametrosContables> listParametrosContablesBorrar) {
        this.listParametrosContablesBorrar = listParametrosContablesBorrar;
    }

    public List<InterconInfor> getListaInterconInfor() {
        return listaInterconInfor;
    }

    public void setListaInterconInfor(List<InterconInfor> listaInterconInfor) {
        this.listaInterconInfor = listaInterconInfor;
    }

    public List<InterconInfor> getFiltrarListaInterconInfor() {
        return filtrarListaInterconInfor;
    }

    public void setFiltrarListaInterconInfor(List<InterconInfor> filtrarListaInterconInfor) {
        this.filtrarListaInterconInfor = filtrarListaInterconInfor;
    }

    public InterconInfor getInterconTablaSeleccionada() {
        return interconTablaSeleccionada;
    }

    public void setInterconTablaSeleccionada(InterconInfor interconTablaSeleccionada) {
        this.interconTablaSeleccionada = interconTablaSeleccionada;
    }

    public InterconInfor getEditarIntercon() {
        return editarIntercon;
    }

    public void setEditarIntercon(InterconInfor editarIntercon) {
        this.editarIntercon = editarIntercon;
    }

    public String getAltoTablaIntercon() {
        return altoTablaIntercon;
    }

    public void setAltoTablaIntercon(String altoTablaIntercon) {
        this.altoTablaIntercon = altoTablaIntercon;
    }

    public int getTipoPlano() {
        return tipoPlano;
    }

    public void setTipoPlano(int tipoPlano) {
        this.tipoPlano = tipoPlano;
    }

    public String getMsnFechasActualizar() {
        return msnFechasActualizar;
    }

    public void setMsnFechasActualizar(String msnFechasActualizar) {
        this.msnFechasActualizar = msnFechasActualizar;
    }

    public boolean isActivarEnviar() {
        return activarEnviar;
    }

    public void setActivarEnviar(boolean activarEnviar) {
        this.activarEnviar = activarEnviar;
    }

    public boolean isActivarDeshacer() {
        return activarDeshacer;
    }

    public void setActivarDeshacer(boolean activarDeshacer) {
        this.activarDeshacer = activarDeshacer;
    }

    public BigDecimal getTotalCGenerado() {
        totalCGenerado = BigDecimal.ZERO;
        if (listaGenerados != null) {
            for (int i = 0; i < listaGenerados.size(); i++) {
                totalCGenerado = totalCGenerado.add(listaGenerados.get(i).getValor());
            }
        }
        return totalCGenerado;
    }

    public void setTotalCGenerado(BigDecimal totalCGenerado) {
        this.totalCGenerado = totalCGenerado;
    }

    public BigDecimal getTotalDGenerado() {
        totalDGenerado = BigDecimal.ZERO;
        if (listaGenerados != null) {
            for (int i = 0; i < listaGenerados.size(); i++) {
                totalDGenerado = totalDGenerado.add(listaGenerados.get(i).getValor());
            }
        }
        return totalDGenerado;
    }

    public void setTotalDGenerado(BigDecimal totalDGenerado) {
        this.totalDGenerado = totalDGenerado;
    }

//    public BigDecimal getTotalDInter() {
//        totalDInter = BigDecimal.ZERO;
//        if (listaInterconInfor != null) {
//            for (int i = 0; i < listaInterconInfor.size(); i++) {
//                totalDInter = totalDInter.add(listaInterconInfor.get(i).getValor());
//            }
//        }
//        return totalDInter;
//    }
//
//    public void setTotalDInter(BigDecimal totalDInter) {
//        this.totalDInter = totalDInter;
//    }
//    public BigDecimal getTotalCInter() {
//        totalCInter = BigDecimal.ZERO;
//        if (listaInterconInfor != null) {
//            for (int i = 0; i < listaInterconInfor.size(); i++) {
//                totalCInter = totalCInter.add(listaInterconInfor.get(i).getValor());
//                System.out.println("totalCInter : " + totalCInter);
//            }
//        }
//        return totalCInter;
//    }
//
//    public void setTotalCInter(BigDecimal totalCInter) {
//        this.totalCInter = totalCInter;
//    }
    public String getFechaIniRecon() {
        return fechaIniRecon;
    }

    public void setFechaIniRecon(String fechaIniRecon) {
        this.fechaIniRecon = fechaIniRecon;
    }

    public String getFechaFinRecon() {
        return fechaFinRecon;
    }

    public void setFechaFinRecon(String fechaFinRecon) {
        this.fechaFinRecon = fechaFinRecon;
    }

    public String getRutaArchivo() {
        return rutaArchivo;
    }

    public void setRutaArchivo(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }

    public void setDownload(DefaultStreamedContent download) {
        this.download = download;
    }

    public DefaultStreamedContent getDownload() throws Exception {
        return download;
    }

    public boolean isActivarLov() {
        return activarLov;
    }

    public void setActivarLov(boolean activarLov) {
        this.activarLov = activarLov;
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

    public String getPathProceso() {
        return pathProceso;
    }

    public void setPathProceso(String pathProceso) {
        this.pathProceso = pathProceso;
    }

    public StreamedContent getReporte() {
        return reporte;
    }

    public void setReporte(StreamedContent reporte) {
        this.reporte = reporte;
    }

    public String getInfoRegistroGenerados() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosGenerados");
        infoRegistroGenerados = String.valueOf(tabla.getRowCount());
        return infoRegistroGenerados;
    }

    public void setInfoRegistroGenerados(String infoRegistroGenerados) {
        this.infoRegistroGenerados = infoRegistroGenerados;
    }

    public String getInfoRegistroContabilizados() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosIntercon");
        infoRegistroContabilizados = String.valueOf(tabla.getRowCount());
        return infoRegistroContabilizados;
    }

    public void setInfoRegistroContabilizados(String infoRegistroContabilizados) {
        this.infoRegistroContabilizados = infoRegistroContabilizados;
    }

    public String getMsgError() {
        return msgError;
    }

    public void setMsgError(String msgError) {
        this.msgError = msgError;
    }

}
