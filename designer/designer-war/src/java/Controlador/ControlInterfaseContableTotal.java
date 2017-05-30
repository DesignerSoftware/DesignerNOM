package Controlador;

import Entidades.ActualUsuario;
import Entidades.Empresas;
import Entidades.Inforeportes;
import Entidades.InterconTotal;
import Entidades.ParametrosContables;
import Entidades.ParametrosEstructuras;
import Entidades.Procesos;
import Entidades.SolucionesNodos;
import Entidades.UsuariosInterfases;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministarReportesInterface;
import InterfaceAdministrar.AdministrarInterfaseContableTotalInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import excepciones.ExcepcionBD;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import ControlNavegacion.ControlListaNavegacion;
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
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.primefaces.component.column.Column;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.export.Exporter;
import org.primefaces.component.inputtext.InputText;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author Administrador
 */
@ManagedBean
@SessionScoped
public class ControlInterfaseContableTotal implements Serializable {

    @EJB
    AdministrarInterfaseContableTotalInterface administrarInterfaseContableTotal;
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
    private List<InterconTotal> listaInterconTotal;
    private List<InterconTotal> listaInterconTotalBorrar;
    private List<InterconTotal> filtrarListaInterconTotal;
    private InterconTotal interconTablaSeleccionada;
    private String infoRegistroContabilizados;
    private int cualCeldaIntercon;
    private InterconTotal editarIntercon;
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
//    private boolean estadoBtnArriba, estadoBtnAbajo;
    private int registroActual;
    //
    private boolean activarAgregar, activarOtros;
    private boolean activarEnviar, activarDeshacer, activarLov;
    //
    private int tipoActualizacion;
    //
    private Column genConcepto, genValor, genTercero, genCntDebito, genCntCredito, genEmpleado, genProceso;
    private String altoTablaGenerada;
    private Column interEmpleado, interTercero, interCuenta, interDebito, interCredito, interConcepto, interCentroCosto;
    private String altoTablaIntercon;
    private int tipoPlano;
    private String msnFechasActualizar;
    private BigDecimal totalCGenerado, totalDGenerado, totalDInter, totalCInter;
    private String msnPaso1;
    private String fechaIniRecon, fechaFinRecon;
    private String rutaArchivo, nombreArchivo, pathProceso;
//    private final String server = "192.168.0.16";
//    private final int port = 21;
//    private final String user = "Administrador";
//    private final String pass = "Soporte9";

//    private FTPClient ftpClient;
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

    public ControlInterfaseContableTotal() {
//        ftpClient = new FTPClient();
        msnPaso1 = " ";
        totalCGenerado = BigDecimal.ZERO;
        totalDGenerado = BigDecimal.ZERO;
        totalDInter = BigDecimal.ZERO;
        totalCInter = BigDecimal.ZERO;
        activarEnviar = true;
        activarDeshacer = true;
        msnFechasActualizar = "";
        tipoPlano = 1;
        altoTablaGenerada = "65";
        altoTablaIntercon = "65";
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
        listaInterconTotal = null;
        listaInterconTotalBorrar = new ArrayList<InterconTotal>();
        interconTablaSeleccionada = new InterconTotal();
        generadoTablaSeleccionado = null;
        interconTablaSeleccionada = null;
        lovEmpresas = null;
        lovProcesos = null;
        cualCeldaGenerado = -1;
        cualCeldaIntercon = -1;
        editarGenerado = new SolucionesNodos();
        editarIntercon = new InterconTotal();
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

    public void navegar(String pag) {
        FacesContext fc = FacesContext.getCurrentInstance();
        ControlListaNavegacion controlListaNavegacion = (ControlListaNavegacion) fc.getApplication().evaluateExpressionGet(fc, "#{controlListaNavegacion}", ControlListaNavegacion.class);
        /*if (pag.equals("atras")) {
            pag = paginaAnterior;
            paginaAnterior = "nominaf";
            controlListaNavegacion.quitarPagina(pagActual);
            
        } else {
         */
        String pagActual = "interfasecontabletotal";
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

    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarInterfaseContableTotal.obtenerConexion(ses.getId());
            administarReportes.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
            externalContext = x.getExternalContext();
            userAgent = externalContext.getRequestHeaderMap().get("User-Agent");
        } catch (Exception e) {
            System.out.println("Error postconstruct ControlInterfaseContableTotal: " + e);
            System.out.println("Causa: " + e.getCause());
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
                System.out.println("valor confirmar igual a proceso");
                System.out.println("parametroContableActual.getProceso().getDescripcion() : " + parametroContableActual.getProceso().getDescripcion());
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
//        if (parametroContableActual.getFechainicialcontabilizacion() != null && parametroContableActual.getFechafinalcontabilizacion() != null) {
//        boolean validacion = validarFechaParametro(0);
//        if (validacion == true) {
        cambiarIndiceParametro(parametroContableActual, c);
        modificarParametroContable();
//        } else {
//            parametroContableActual.setFechafinalcontabilizacion(auxParametroFechaFinal);
//            parametroContableActual.setFechainicialcontabilizacion(auxParametroFechaInicial);
//            RequestContext context = RequestContext.getCurrentInstance();
//            RequestContext.getCurrentInstance().update("form:parametroFechaFinal");
//            RequestContext.getCurrentInstance().update("form:parametroFechaInicial");
//            RequestContext.getCurrentInstance().execute("PF('errorFechasParametro').show()");
//        }
//        } else {
//            parametroContableActual.setFechafinalcontabilizacion(auxParametroFechaFinal);
//            parametroContableActual.setFechainicialcontabilizacion(auxParametroFechaInicial);
//            RequestContext context = RequestContext.getCurrentInstance();
//            RequestContext.getCurrentInstance().update("form:panelParametro:parametroFechaFinal");
//            RequestContext.getCurrentInstance().update("form:panelParametro:parametroFechaInicial");
//            RequestContext.getCurrentInstance().execute("PF('errorFechasNull').show()");
//        }
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
        System.out.println("retorno : " + retorno);
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
        interconTablaSeleccionada = listaInterconTotal.get(indice);
        cualCeldaIntercon = columna;
        parametroContableActual = null;
        generadoTablaSeleccionado = null;
        if (tipoListaIntercon == 0) {
            interconTablaSeleccionada.getSecuencia();
        } else {
            interconTablaSeleccionada.getSecuencia();
        }
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
                auxParametroFechaFinal = parametroContableActual.getFechafinalcontabilizacion();
            } else if (cualCeldaParametroContable == 3) {
                auxParametroFechaInicial = parametroContableActual.getFechainicialcontabilizacion();
            }
        }
    }

    public String msnPaso1() {
        if (parametroContableActual.getProceso() == null) {
            parametroContableActual.setProceso(new Procesos());
        }
        System.out.println("proceso en msnpaso 1 : " + parametroContableActual.getProceso());
        if (parametroContableActual.getProceso().getSecuencia() != null) {
            msnPaso1 = parametroContableActual.getProceso().getDescripcion().toUpperCase();
        } else {
            msnPaso1 = "TODOS";
        }
        System.out.println("msn paso 1 " + msnPaso1);
        return msnPaso1;
    }

    public void inicioCerrarPeriodoContable() {
        try {
            RequestContext context = RequestContext.getCurrentInstance();
            int contador = administrarInterfaseContableTotal.contarProcesosContabilizadosInterconTotal(parametroContableActual.getFechainicialcontabilizacion(), parametroContableActual.getFechafinalcontabilizacion());
            if (contador != -1) {
                if (contador == 0) {
                    RequestContext.getCurrentInstance().execute("PF('contadoCeroPerContable').show()");
                } else {
                    RequestContext.getCurrentInstance().update("form:paso1CerrarPeriodo");
                    RequestContext.getCurrentInstance().execute("PF('paso1CerrarPeriodo').show()");
                }
            }
        } catch (Exception e) {
            System.out.println("Error cerrarPeriodoContable Controlador : " + e.toString());
        }
    }

    public void finCerrarPeriodoContable() {
        try {
            RequestContext context = RequestContext.getCurrentInstance();
            guardadoGeneral();
            administrarInterfaseContableTotal.cerrarProcesoContabilizacion(parametroContableActual.getFechainicialcontabilizacion(), parametroContableActual.getFechafinalcontabilizacion(), parametroContableActual.getEmpresaCodigo(), parametroContableActual.getProceso().getSecuencia());
            listaGenerados = null;
            if (listaGenerados == null) {
                listaGenerados = administrarInterfaseContableTotal.obtenerSolucionesNodosParametroContable(parametroContableActual.getFechainicialcontabilizacion(), parametroContableActual.getFechafinalcontabilizacion());
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
            listaInterconTotal = null;
            if (listaInterconTotal == null) {
                listaInterconTotal = administrarInterfaseContableTotal.obtenerInterconTotalParametroContable(parametroContableActual.getFechainicialcontabilizacion(), parametroContableActual.getFechafinalcontabilizacion());
                if (listaInterconTotal != null) {
                    if (listaInterconTotal.size() > 0) {
                        activarDeshacer = false;
                    } else {
                        activarDeshacer = true;
                    }
                } else {
                    activarDeshacer = true;
                }
                getTotalCInter();
                getTotalDInter();
            }
            contarRegistroContabilizados();
            contarRegistrosGenerados();
            activarDeshacer = true;
            activarEnviar = true;
            RequestContext.getCurrentInstance().update("form:btnEnviar");
            RequestContext.getCurrentInstance().update("form:btnDeshacer");

            RequestContext.getCurrentInstance().update("form:totalDGenerado");
            RequestContext.getCurrentInstance().update("form:totalCGenerado");
            RequestContext.getCurrentInstance().update("form:totalDInter");
            RequestContext.getCurrentInstance().update("form:totalCInter");
            RequestContext.getCurrentInstance().update("form:datosGenerados");
            RequestContext.getCurrentInstance().update("form:datosIntercon");
            RequestContext.getCurrentInstance().execute("PF('operacionEnProceso').hide()");
            RequestContext.getCurrentInstance().execute("PF('paso3CerrarPeriodo').show()");
        } catch (Exception e) {
            System.out.println("Error finCerrarPeriodoContable Controlador : " + e.toString());
        }
    }

    public void actionBtnGenerarPlano() {
        try {
            guardadoGeneral();
            String descripcionProceso = administrarInterfaseContableTotal.obtenerDescripcionProcesoArchivo(parametroContableActual.getProceso().getSecuencia());
            nombreArchivo = "Interfase_Total_" + descripcionProceso;
            pathProceso = administrarInterfaseContableTotal.obtenerPathProceso();
            System.out.println("path proceso :" + pathProceso);
            administrarInterfaseContableTotal.ejecutarPKGCrearArchivoPlano(tipoPlano, parametroContableActual.getFechainicialcontabilizacion(), parametroContableActual.getFechafinalcontabilizacion(), parametroContableActual.getProceso().getSecuencia(), descripcionProceso);
            rutaArchivo = "";
            rutaArchivo = pathProceso + nombreArchivo + ".txt";
            System.out.println("rutaArchivo " + rutaArchivo);
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:planoGeneradoOK");
            RequestContext.getCurrentInstance().execute("PF('planoGeneradoOK').show()");
        } catch (Exception e) {
            System.out.println("Error actionBtnGenerarPlano Control : " + e.toString());
        }
    }

//    public void conectarAlFTP() {
//        try {
//            System.out.println("server remoto : " + usuarioInterfaseContabilizacion.getServernameremoto());
//            System.out.println("usuario remoto : " + usuarioInterfaseContabilizacion.getUsuarioremoto());
//            System.out.println("password remoto : " + usuarioInterfaseContabilizacion.getPasswordremoto());
//
//            ftpClient.connect(usuarioInterfaseContabilizacion.getServernameremoto());
//            ftpClient.login(usuarioInterfaseContabilizacion.getUsuarioremoto(), usuarioInterfaseContabilizacion.getPasswordremoto());
//            ftpClient.enterLocalPassiveMode();
//            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
//        } catch (Exception e) {
//            System.out.println("Error en conexion : " + e.toString());
//        }
//    }
//
//    public void descargarArchivoFTP() throws IOException {
//        try {
//            usuarioInterfaseContabilizacion = administrarInterfaseContableTotal.obtenerUsuarioInterfaseContabilizacion();
//            System.out.println("usuario interfase contabilzación: " + usuarioInterfaseContabilizacion);
//            conectarAlFTP();
//            int tamPath = pathProceso.length();
//            String rutaX = "";
//            for (int i = 2; i < tamPath; i++) {
//                rutaX = rutaX + pathProceso.charAt(i) + "";
//            }
//            System.out.println("rutaX : " + rutaX);
//            String remoteFile1 = rutaX + nombreArchivo + ".txt";
//            File downloadFile1 = new File(pathProceso + nombreArchivo + ".txt");
//            OutputStream outputStream1 = new BufferedOutputStream(new FileOutputStream(downloadFile1));
//            boolean success = ftpClient.retrieveFile(remoteFile1, outputStream1);
//            outputStream1.close();
//            if (success) {
//                System.out.println("Archivo #1 ha sido descargado exitosamente.");
//            } else {
//                System.out.println("Ni mierda !");
//            }
//            ftpClient.logout();
//            File file = new File(pathProceso + nombreArchivo + ".txt");
//            InputStream input = new FileInputStream(file);
//            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
//            setDownload(new DefaultStreamedContent(input, externalContext.getMimeType(file.getName()), file.getName()));
////            RequestContext.getCurrentInstance().execute("PF('planoGeneradoOK').hide()");
//        } catch (Exception e) {
//            System.out.println("Error descarga : " + e.toString());
//        }
//    }
    public void exportarPlano() throws IOException {
        try {
            System.out.println(this.getClass().getName() + ".exportarPlano()");
            System.out.println("path proceso en exportarPlano() : " + pathProceso);
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
            System.out.println("error exportando plano : " + e.getMessage());
        }
    }

    public void cerrarPaginaDescarga() {
        RequestContext.getCurrentInstance().execute("PF('planoGeneradoOK').hide()");
    }

    public void actionBtnRecontabilizar() {
        Integer contador = administrarInterfaseContableTotal.obtenerContadorFlagGeneradoFechasTotal(parametroContableActual.getFechainicialcontabilizacion(), parametroContableActual.getFechafinalcontabilizacion());
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
            administrarInterfaseContableTotal.ejecutarPKGRecontabilizacion(parametroContableActual.getFechainicialcontabilizacion(), parametroContableActual.getFechafinalcontabilizacion());
            RequestContext.getCurrentInstance().execute("PF('operacionEnProceso').hide()");
            RequestContext.getCurrentInstance().execute("PF('RecontabilizacionExitosa').show()");
        } catch (ExcepcionBD ebd) {
            RequestContext.getCurrentInstance().execute("PF('operacionEnProceso').hide()");
            RequestContext.getCurrentInstance().execute("PF('RecontabilizacionExitosa').hide()");
            System.out.println("controlInterfaseTotal. finalizarProcesoRecontabilizacion : " + ebd.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("", ebd.getMessage()));
            RequestContext.getCurrentInstance().update("form:growl");
        } catch (Exception e) {
            System.out.println("Error finalizarProcesoRecontabilizacion Controlador : " + e.toString());
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
            administrarInterfaseContableTotal.actualizarFlagInterconTotalProcesoDeshacer(parametroContableActual.getFechainicialcontabilizacion(), parametroContableActual.getFechafinalcontabilizacion(), parametroContableActual.getProceso().getSecuencia());
            administrarInterfaseContableTotal.eliminarInterconTotal(parametroContableActual.getFechainicialcontabilizacion(), parametroContableActual.getFechafinalcontabilizacion(), parametroContableActual.getEmpresaCodigo(), parametroContableActual.getProceso().getSecuencia());
            listaGenerados = null;
            if (listaGenerados == null) {
                listaGenerados = administrarInterfaseContableTotal.obtenerSolucionesNodosParametroContable(parametroContableActual.getFechainicialcontabilizacion(), parametroContableActual.getFechafinalcontabilizacion());
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
            listaInterconTotal = null;
            if (listaInterconTotal == null) {
                listaInterconTotal = administrarInterfaseContableTotal.obtenerInterconTotalParametroContable(parametroContableActual.getFechainicialcontabilizacion(), parametroContableActual.getFechafinalcontabilizacion());
                if (listaInterconTotal != null) {
                    if (listaInterconTotal.size() > 0) {
                        activarDeshacer = false;
                    } else {
                        activarDeshacer = true;
                    }
                } else {
                    activarDeshacer = true;
                }
                getTotalCInter();
                getTotalDInter();
            }

            contarRegistroContabilizados();
            contarRegistrosGenerados();
            RequestContext.getCurrentInstance().update("form:btnEnviar");
            activarDeshacer = true;
            RequestContext.getCurrentInstance().update("form:btnDeshacer");

            RequestContext.getCurrentInstance().update("form:totalDGenerado");
            RequestContext.getCurrentInstance().update("form:totalCGenerado");
            RequestContext.getCurrentInstance().update("form:totalDInter");
            RequestContext.getCurrentInstance().update("form:totalCInter");
            if (banderaGenerado == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                altoTablaGenerada = "65";
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
                altoTablaIntercon = "65";
                interEmpleado = (Column) c.getViewRoot().findComponent("form:datosIntercon:interEmpleado");
                interEmpleado.setFilterStyle("display: none; visibility: hidden;");
                interTercero = (Column) c.getViewRoot().findComponent("form:datosIntercon:interTercero");
                interTercero.setFilterStyle("display: none; visibility: hidden;");
                interCuenta = (Column) c.getViewRoot().findComponent("form:datosIntercon:interCuenta");
                interCuenta.setFilterStyle("display: none; visibility: hidden;");
                interDebito = (Column) c.getViewRoot().findComponent("form:datosIntercon:interDebito");
                interDebito.setFilterStyle("display: none; visibility: hidden;");
                interCredito = (Column) c.getViewRoot().findComponent("form:datosIntercon:interCredito");
                interCredito.setFilterStyle("display: none; visibility: hidden;");
                interConcepto = (Column) c.getViewRoot().findComponent("form:datosIntercon:interConcepto");
                interConcepto.setFilterStyle("display: none; visibility: hidden;");
                interCentroCosto = (Column) c.getViewRoot().findComponent("form:datosIntercon:interCentroCosto");
                interCentroCosto.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosIntercon");
                banderaIntercon = 0;
                filtrarListaInterconTotal = null;
                tipoListaIntercon = 0;
            }
            RequestContext.getCurrentInstance().update("form:datosGenerados");
            RequestContext.getCurrentInstance().update("form:datosIntercon");
        } catch (Exception e) {
            System.out.println("Error actionBtnDeshacer Controlador : " + e.toString());
        }
    }

    public void actionBtnEnviar() {
        Date fechaDesde = administrarInterfaseContableTotal.buscarFechaDesdeVWActualesFechas();
        Date fechaHasta = administrarInterfaseContableTotal.buscarFechaHastaVWActualesFechas();
        if (fechaDesde != null && fechaHasta != null) {
            if ((fechaDesde.after(parametroContableActual.getFechainicialcontabilizacion()) && fechaHasta.after(parametroContableActual.getFechafinalcontabilizacion()))
                    || (fechaDesde.before(parametroContableActual.getFechainicialcontabilizacion()) && fechaHasta.before(parametroContableActual.getFechafinalcontabilizacion()))) {
                RequestContext.getCurrentInstance().execute("PF('errorVWActualesFechas').show()");
            } else if (parametroContableActual.getEmpresaRegistro().getSecuencia() != null
                    && parametroContableActual.getFechafinalcontabilizacion() != null
                    && parametroContableActual.getFechainicialcontabilizacion() != null) {
                guardadoGeneral();
                administrarInterfaseContableTotal.ejcutarPKGUbicarnuevointercon_total(parametroContableActual.getSecuencia(), parametroContableActual.getFechainicialcontabilizacion(), parametroContableActual.getFechafinalcontabilizacion(), parametroContableActual.getProceso().getSecuencia());
                listaGenerados = null;
                if (listaGenerados == null) {
                    listaGenerados = administrarInterfaseContableTotal.obtenerSolucionesNodosParametroContable(parametroContableActual.getFechainicialcontabilizacion(), parametroContableActual.getFechafinalcontabilizacion());
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
                listaInterconTotal = null;
                if (listaInterconTotal == null) {
                    listaInterconTotal = administrarInterfaseContableTotal.obtenerInterconTotalParametroContable(parametroContableActual.getFechainicialcontabilizacion(), parametroContableActual.getFechafinalcontabilizacion());
                    if (listaInterconTotal != null) {
                        if (listaInterconTotal.size() > 0) {
                            activarDeshacer = false;
                        } else {
                            activarDeshacer = true;
                        }
                    } else {
                        activarDeshacer = true;
                    }
                    getTotalCInter();
                    getTotalDInter();
                }
                contarRegistroContabilizados();
                contarRegistrosGenerados();
                RequestContext.getCurrentInstance().update("form:totalDGenerado");
                RequestContext.getCurrentInstance().update("form:totalCGenerado");
                RequestContext.getCurrentInstance().update("form:totalDInter");
                RequestContext.getCurrentInstance().update("form:totalCInter");
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
            administrarInterfaseContableTotal.actualizarFlagInterconTotal(parametroContableActual.getFechainicialcontabilizacion(), parametroContableActual.getFechafinalcontabilizacion(), parametroContableActual.getEmpresaCodigo());
            FacesMessage msg = new FacesMessage("Información", "Se realizo el proceso con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        } catch (Exception e) {
            System.out.println("Error anularComprobantesCerrados Controlador : " + e.toString());
            FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el proceso de anulacion");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }
    }

    public void validarFechasProcesoActualizar() {
        Date fechaContabilizacion = administrarInterfaseContableTotal.obtenerFechaMaxContabilizaciones();
        Date fechaInterconTotal = administrarInterfaseContableTotal.obtenerFechaMaxInterconTotal();
        DateFormat df = DateFormat.getDateInstance(DateFormat.LONG);
        RequestContext context = RequestContext.getCurrentInstance();
        if (fechaContabilizacion != null && fechaInterconTotal != null) {
            System.out.println("fecha contabilizacion en proceso actualizar" + fechaContabilizacion);
            System.out.println("fecha InterconTotal en proceso actualizar" + fechaInterconTotal);
            if (fechaContabilizacion.equals(fechaInterconTotal) == true) {
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
        ParametrosEstructuras parametroLiquidacion = administrarInterfaseContableTotal.parametrosLiquidacion();
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
            System.out.println("fecha inicial contabilzacion : " + parametroContableActual.getFechainicialcontabilizacion());
            System.out.println("fecha final contabilzacion : " + parametroContableActual.getFechafinalcontabilizacion());
            System.out.println("empresa parametro : " + parametroContableActual.getEmpresaRegistro().getNombre());

            listaGenerados = administrarInterfaseContableTotal.obtenerSolucionesNodosParametroContable(parametroContableActual.getFechainicialcontabilizacion(), parametroContableActual.getFechafinalcontabilizacion());
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

        listaInterconTotal = null;
        if (listaInterconTotal == null) {
            listaInterconTotal = administrarInterfaseContableTotal.obtenerInterconTotalParametroContable(parametroContableActual.getFechainicialcontabilizacion(), parametroContableActual.getFechafinalcontabilizacion());
            if (listaInterconTotal != null) {
                if (listaInterconTotal.size() > 0) {
                } else {
                    activarDeshacer = true;
                }
            } else {
                activarDeshacer = true;
            }
            getTotalCInter();
            getTotalDInter();
            activarDeshacer = false;
            RequestContext.getCurrentInstance().update("form:btnDeshacer");
            RequestContext.getCurrentInstance().update("form:totalDInter");
            RequestContext.getCurrentInstance().update("form:totalCInter");
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
        if (listaInterconTotal != null) {
            tam2 = listaInterconTotal.size();
        }
        if (tam1 == 0 && tam2 == 0) {
            RequestContext.getCurrentInstance().execute("PF('procesoSinDatos').show()");
        }
        System.out.println("I finish");
        System.out.println("terminó proceso actualizar");
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
                System.out.println("entra al if 2");
                parametroContableActual.setProceso(new Procesos());
            }

            administrarInterfaseContableTotal.modificarParametroContable(parametroContableActual);

            if (!listaGeneradosBorrar.isEmpty()) {
                administrarInterfaseContableTotal.borrarRegistroGenerado(listaGeneradosBorrar);
                listaGeneradosBorrar.clear();
            }

            if (!listaInterconTotalBorrar.isEmpty()) {
                administrarInterfaseContableTotal.borrarRegistroIntercon(listaInterconTotalBorrar);
                listaInterconTotalBorrar.clear();
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
            System.out.println("Error guardarCambiosParametro Controlador : " + e.toString());
            FacesMessage msg = new FacesMessage("Información", "Un error ha ocurrido en el guardado, intente nuevamente");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }
    }

    public void cancelarModificaciones() {
        if (banderaGenerado == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            altoTablaGenerada = "65";
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
            altoTablaIntercon = "65";
            interEmpleado = (Column) c.getViewRoot().findComponent("form:datosIntercon:interEmpleado");
            interEmpleado.setFilterStyle("display: none; visibility: hidden;");
            interTercero = (Column) c.getViewRoot().findComponent("form:datosIntercon:interTercero");
            interTercero.setFilterStyle("display: none; visibility: hidden;");
            interCuenta = (Column) c.getViewRoot().findComponent("form:datosIntercon:interCuenta");
            interCuenta.setFilterStyle("display: none; visibility: hidden;");
            interDebito = (Column) c.getViewRoot().findComponent("form:datosIntercon:interDebito");
            interDebito.setFilterStyle("display: none; visibility: hidden;");
            interCredito = (Column) c.getViewRoot().findComponent("form:datosIntercon:interCredito");
            interCredito.setFilterStyle("display: none; visibility: hidden;");
            interConcepto = (Column) c.getViewRoot().findComponent("form:datosIntercon:interConcepto");
            interConcepto.setFilterStyle("display: none; visibility: hidden;");
            interCentroCosto = (Column) c.getViewRoot().findComponent("form:datosIntercon:interCentroCosto");
            interCentroCosto.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosIntercon");
            banderaIntercon = 0;
            filtrarListaInterconTotal = null;
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
        listaInterconTotal = null;
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
                RequestContext.getCurrentInstance().update("formularioDialogos:editarDocContableParametro");
                RequestContext.getCurrentInstance().execute("PF('editarDocContableParametro').show()");
                parametroContableActual = null;
            } else if (cualCeldaParametroContable == 2) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarProcesoParametro");
                RequestContext.getCurrentInstance().execute("PF('editarProcesoParametro').show()");
                parametroContableActual = null;
            } else if (cualCeldaParametroContable == 3) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaInicialParametro");
                RequestContext.getCurrentInstance().execute("PF('editarFechaInicialParametro').show()");
                parametroContableActual = null;
            } else if (cualCeldaParametroContable == 4) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaFinalParametro");
                RequestContext.getCurrentInstance().execute("PF('editarFechaFinalParametro').show()");
                parametroContableActual = null;
            }
        } else if (generadoTablaSeleccionado != null) {
            if (tipoListaGenerada == 0) {
                editarGenerado = generadoTablaSeleccionado;
            } else {
                editarGenerado = generadoTablaSeleccionado;
            }
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
            if (tipoListaIntercon == 0) {
                editarIntercon = interconTablaSeleccionada;
            } else {
                editarIntercon = interconTablaSeleccionada;
            }
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
            altoTablaGenerada = "65";
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
            altoTablaIntercon = "65";
            interEmpleado = (Column) c.getViewRoot().findComponent("form:datosIntercon:interEmpleado");
            interEmpleado.setFilterStyle("display: none; visibility: hidden;");
            interTercero = (Column) c.getViewRoot().findComponent("form:datosIntercon:interTercero");
            interTercero.setFilterStyle("display: none; visibility: hidden;");
            interCuenta = (Column) c.getViewRoot().findComponent("form:datosIntercon:interCuenta");
            interCuenta.setFilterStyle("display: none; visibility: hidden;");
            interDebito = (Column) c.getViewRoot().findComponent("form:datosIntercon:interDebito");
            interDebito.setFilterStyle("display: none; visibility: hidden;");
            interCredito = (Column) c.getViewRoot().findComponent("form:datosIntercon:interCredito");
            interCredito.setFilterStyle("display: none; visibility: hidden;");
            interConcepto = (Column) c.getViewRoot().findComponent("form:datosIntercon:interConcepto");
            interConcepto.setFilterStyle("display: none; visibility: hidden;");
            interCentroCosto = (Column) c.getViewRoot().findComponent("form:datosIntercon:interCentroCosto");
            interCentroCosto.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosIntercon");
            banderaIntercon = 0;
            filtrarListaInterconTotal = null;
            tipoListaIntercon = 0;
        }
        RequestContext context = RequestContext.getCurrentInstance();
        listParametrosContablesBorrar.clear();
        listaParametrosContables = null;
        getListaParametrosContables();
        parametroContableActual = null;
        listaGenerados = null;
        listaInterconTotal = null;
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
            if (guardado == true) {
                guardado = false;
            }
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
//            parametroContableActual.setArchivo(procesoSeleccionado.getDescripcion());
            if (guardado == true) {
                guardado = false;
            }
            cambiosParametro = true;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:parametroProceso");
        }
//        if (tipoActualizacion == 1) {
//            nuevoParametroContable.setProceso(procesoSeleccionado);
////            parametroContableActual.setArchivo(procesoSeleccionado.getDescripcion());
//            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaProcesoParametro");
//        }
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
            if (cualCeldaParametroContable == 2) {
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
        System.out.println("entró a borrar registro generado");
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
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void borrarRegistroContabilizado() {
        System.out.println("entró a borrar registro contabilizado");
        if (interconTablaSeleccionada != null) {
            listaInterconTotalBorrar.add(interconTablaSeleccionada);
            totalCInter = totalCInter.subtract(interconTablaSeleccionada.getValorc());
            totalDInter = totalDInter.subtract(interconTablaSeleccionada.getValord());
            listaInterconTotal.remove(interconTablaSeleccionada);
            if (tipoListaIntercon == 1) {
                filtrarListaInterconTotal.remove(interconTablaSeleccionada);
            }
            System.out.println("totalCInter : " + totalCInter);
            System.out.println("totalDInter : " + totalDInter);
            getTotalCInter();
            getTotalDInter();
            RequestContext.getCurrentInstance().update("form:totalDInter");
            RequestContext.getCurrentInstance().update("form:totalCInter");
            RequestContext.getCurrentInstance().update("form:datosIntercon");
            contarRegistroContabilizados();
            generadoTablaSeleccionado = null;
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

//    public void borrarParametroContable() {
//        System.out.println("entró a borrar parametro contable");
//        listaParametrosContables.remove(parametroContableActual);
//        listParametrosContablesBorrar.add(parametroContableActual);
//        if (listaParametrosContables != null) {
//            int tam = listaParametrosContables.size();
//            if (tam == 0 || tam == 1) {
//                estadoBtnAbajo = true;
//                estadoBtnArriba = true;
//            }
//            if (tam > 1) {
//                estadoBtnAbajo = false;
//                estadoBtnArriba = true;
//            }
//        }
//        activarEnviar = true;
//        activarDeshacer = true;
//        getTotalCGenerado();
//        getTotalDGenerado();
//        getTotalCInter();
//        getTotalDInter();
//        RequestContext context = RequestContext.getCurrentInstance();
//        RequestContext.getCurrentInstance().update("form:PanelTotal");
//        RequestContext.getCurrentInstance().update("form:btnEnviar");
//        RequestContext.getCurrentInstance().update("form:btnDeshacer");
//        RequestContext.getCurrentInstance().update("form:PLANO");
//    }
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
                    administrarInterfaseContableTotal.crearParametroContable(nuevoParametroContable);
                    nuevoParametroContable = new ParametrosContables();
                    activarAgregar = true;
                    activarOtros = false;
                    listaParametrosContables = null;
                    getListaParametrosContables();
                    parametroContableActual = null;
                    getParametroContableActual();
                    listaGenerados = null;
                    listaInterconTotal = null;
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
                        altoTablaGenerada = "65";
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
                        altoTablaIntercon = "65";
                        interEmpleado = (Column) c.getViewRoot().findComponent("form:datosIntercon:interEmpleado");
                        interEmpleado.setFilterStyle("display: none; visibility: hidden;");
                        interTercero = (Column) c.getViewRoot().findComponent("form:datosIntercon:interTercero");
                        interTercero.setFilterStyle("display: none; visibility: hidden;");
                        interCuenta = (Column) c.getViewRoot().findComponent("form:datosIntercon:interCuenta");
                        interCuenta.setFilterStyle("display: none; visibility: hidden;");
                        interDebito = (Column) c.getViewRoot().findComponent("form:datosIntercon:interDebito");
                        interDebito.setFilterStyle("display: none; visibility: hidden;");
                        interCredito = (Column) c.getViewRoot().findComponent("form:datosIntercon:interCredito");
                        interCredito.setFilterStyle("display: none; visibility: hidden;");
                        interConcepto = (Column) c.getViewRoot().findComponent("form:datosIntercon:interConcepto");
                        interConcepto.setFilterStyle("display: none; visibility: hidden;");
                        interCentroCosto = (Column) c.getViewRoot().findComponent("form:datosIntercon:interCentroCosto");
                        interCentroCosto.setFilterStyle("display: none; visibility: hidden;");
                        RequestContext.getCurrentInstance().update("form:datosIntercon");
                        banderaIntercon = 0;
                        filtrarListaInterconTotal = null;
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
            System.out.println("Error Controlador agregarNuevo : " + e.toString());
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
        System.out.println("tipo : " + tipo);
        tipoPlano = tipo;
        System.out.println("tipo Plano despues de modificar : " + tipoPlano);
        guardadoGeneral();
        RequestContext.getCurrentInstance().update("form:tipoPlano");

    }

    public void validarExportPDF() throws IOException {
        if (parametroContableActual != null) {
            exportPDF_PC();
        }
        if (generadoTablaSeleccionado != null) {
            exportPDF_G();
        }
        if (interconTablaSeleccionada != null) {
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
        exporter.export(context, tabla, "InterconTotal_PDF", false, false, "UTF-8", null, null);
        context.responseComplete();
        interconTablaSeleccionada = null;
    }

    public void validarExportXLS() throws IOException {
        if (parametroContableActual != null) {
            exportXLS_PC();
        }
        if (generadoTablaSeleccionado != null) {
            exportXLS_G();
        }
        if (interconTablaSeleccionada != null) {
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
        exporter.export(context, tabla, "InterconTotal_XLS", false, false, "UTF-8", null, null);
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
        if (generadoTablaSeleccionado != null) {
            nombre = "Generados_XML";
        }
        if (interconTablaSeleccionada != null) {
            nombre = "InterconTotal_XML";
        }
        return nombre;
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (banderaGenerado == 0 && banderaIntercon == 0) {
            altoTablaGenerada = "35";
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

            altoTablaIntercon = "35";
            interEmpleado = (Column) c.getViewRoot().findComponent("form:datosIntercon:interEmpleado");
            interEmpleado.setFilterStyle("width: 85% !important;");
            interTercero = (Column) c.getViewRoot().findComponent("form:datosIntercon:interTercero");
            interTercero.setFilterStyle("width: 85% !important;");
            interCuenta = (Column) c.getViewRoot().findComponent("form:datosIntercon:interCuenta");
            interCuenta.setFilterStyle("width: 85% !important;");
            interDebito = (Column) c.getViewRoot().findComponent("form:datosIntercon:interDebito");
            interDebito.setFilterStyle("width: 85% !important;");
            interCredito = (Column) c.getViewRoot().findComponent("form:datosIntercon:interCredito");
            interCredito.setFilterStyle("width: 85% !important;");
            interConcepto = (Column) c.getViewRoot().findComponent("form:datosIntercon:interConcepto");
            interConcepto.setFilterStyle("width: 85% !important;");
            interCentroCosto = (Column) c.getViewRoot().findComponent("form:datosIntercon:interCentroCosto");
            interCentroCosto.setFilterStyle("width: 85% !important;");
            banderaGenerado = 1;
            banderaIntercon = 1;
            RequestContext.getCurrentInstance().update("form:datosIntercon");
            RequestContext.getCurrentInstance().update("form:datosGenerados");
        } else if (banderaGenerado == 1 && banderaIntercon == 1) {
            altoTablaGenerada = "65";
            altoTablaIntercon = "65";
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
            interCredito = (Column) c.getViewRoot().findComponent("form:datosIntercon:interCredito");
            interCredito.setFilterStyle("display: none; visibility: hidden;");
            interConcepto = (Column) c.getViewRoot().findComponent("form:datosIntercon:interConcepto");
            interConcepto.setFilterStyle("display: none; visibility: hidden;");
            interCentroCosto = (Column) c.getViewRoot().findComponent("form:datosIntercon:interCentroCosto");
            interCentroCosto.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosIntercon");
            banderaIntercon = 0;
            filtrarListaInterconTotal = null;
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
            int resultado = administrarRastros.obtenerTabla(interconTablaSeleccionada.getSecuencia(), "INTERCON_TOTAL");
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
        } else if (administrarRastros.verificarHistoricosTabla("INTERCON_TOTAL")) {
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
        System.out.println(this.getClass().getName() + ".listener()");
        return new AsynchronousFilllListener() {
            //RequestContext context = c;

            @Override
            public void reportFinished(JasperPrint jp) {
                System.out.println(this.getClass().getName() + ".listener().reportFinished()");
                try {
                    estadoReporte = true;
                    resultadoReporte = "Exito";
                    //  RequestContext.getCurrentInstance().execute("PF('formularioDialogos:generandoReporte");
//                    generarArchivoReporte(jp);
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
            System.out.println(this.getClass().getName() + ".validarDescargaReporte()");
            RequestContext.getCurrentInstance().execute("PF('generandoReporte').show()");
            RequestContext context = RequestContext.getCurrentInstance();
            nombreReporte = "CifraControl";
            tipoReporte = "PDF";
            System.out.println("nombre reporte : " + nombreReporte);
            System.out.println("tipo reporte: " + tipoReporte);
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            String auxfechainicial = formatoFecha.format(parametroContableActual.getFechainicialcontabilizacion());
            String auxfechafinal = formatoFecha.format(parametroContableActual.getFechafinalcontabilizacion());

            Map paramFechas = new HashMap();
            System.out.println("fecha desde : " + auxfechainicial);
            System.out.println("fecha hasta : " + auxfechafinal);
            paramFechas.put("fechaDesde", auxfechainicial);
            paramFechas.put("fechaHasta", auxfechafinal);

            pathReporteGenerado = administarReportes.generarReporteCifraControl(nombreReporte, tipoReporte, paramFechas);
            RequestContext.getCurrentInstance().execute("PF('generandoReporte').hide()");
            if (pathReporteGenerado != null && !pathReporteGenerado.startsWith("Error:")) {
                System.out.println("validar descarga reporte - ingreso al if 1");
                if (tipoReporte.equals("PDF")) {
                    System.out.println("validar descarga reporte - ingreso al if 2 else");
                    FileInputStream fis;
                    try {
                        System.out.println("pathReporteGenerado : " + pathReporteGenerado);
                        fis = new FileInputStream(new File(pathReporteGenerado));
                        System.out.println("fis : " + fis);
                        reporte = new DefaultStreamedContent(fis, "application/pdf");
                        System.out.println("reporte despues de esto : " + reporte);
                        if (reporte != null) {
                            System.out.println("userAgent: " + userAgent);
                            System.out.println("validar descarga reporte - ingreso al if 4");
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
                        System.out.println("error en validar descargar reporte : " + ex.getMessage());
                        RequestContext.getCurrentInstance().update("formularioDialogos:errorGenerandoReporte");
                        RequestContext.getCurrentInstance().execute("PF('errorGenerandoReporte').show()");
                        reporte = null;
                    }
                }
            } else {
                System.out.println("validar descarga reporte - ingreso al if 1 else");
                RequestContext.getCurrentInstance().update("formularioDialogos:errorGenerandoReporte");
                RequestContext.getCurrentInstance().execute("PF('errorGenerandoReporte').show()");
            }
        } catch (Exception e) {
            System.out.println("Error en validar descargar Reporte");
            RequestContext.getCurrentInstance().execute("PF('errorCifraControl').show()");
        }
    }

    public void reiniciarStreamedContent() {
        System.out.println(this.getClass().getName() + ".reiniciarStreamedContent()");
        reporte = null;
    }

    public void cancelarReporte() {
        System.out.println(this.getClass().getName() + ".cancelarReporte()");
        administarReportes.cancelarReporte();
    }

    public void exportarReporte() throws IOException {
        try {
            System.out.println("Controlador.ControlInterfaseContableTotal.exportarReporte()   path generado : " + pathReporteGenerado);
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
            System.out.println("error en exportarReporte :" + e.getMessage());
        }
    }

    public void cargarLovEmpresas() {
        if (lovEmpresas == null) {
            lovEmpresas = administrarInterfaseContableTotal.lovEmpresas();
        }
    }

    public void cargarLovProcesos() {
        if (lovProcesos == null) {
            lovProcesos = administrarInterfaseContableTotal.lovProcesos();
        }
    }
/////////////////////SETS Y GETS/////////////////////////////

    public ActualUsuario getActualUsuarioBD() {
        if (actualUsuarioBD == null) {
            actualUsuarioBD = administrarInterfaseContableTotal.obtenerActualUsuario();
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
                listaParametrosContables = administrarInterfaseContableTotal.obtenerParametrosContablesUsuarioBD(actualUsuarioBD.getAlias());
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

    public List<InterconTotal> getListaInterconTotal() {
        return listaInterconTotal;
    }

    public void setListaInterconTotal(List<InterconTotal> listaInterconTotal) {
        this.listaInterconTotal = listaInterconTotal;
    }

    public List<InterconTotal> getFiltrarListaInterconTotal() {
        return filtrarListaInterconTotal;
    }

    public void setFiltrarListaInterconTotal(List<InterconTotal> filtrarListaInterconTotal) {
        this.filtrarListaInterconTotal = filtrarListaInterconTotal;
    }

    public InterconTotal getInterconTablaSeleccionada() {
        return interconTablaSeleccionada;
    }

    public void setInterconTablaSeleccionada(InterconTotal interconTablaSeleccionada) {
        this.interconTablaSeleccionada = interconTablaSeleccionada;
    }

    public InterconTotal getEditarIntercon() {
        return editarIntercon;
    }

    public void setEditarIntercon(InterconTotal editarIntercon) {
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
        System.out.println("totalCGenerado : " + totalCGenerado);
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
        System.out.println("totalDGenerado : " + totalDGenerado);
        return totalDGenerado;
    }

    public void setTotalDGenerado(BigDecimal totalDGenerado) {
        this.totalDGenerado = totalDGenerado;
    }

    public BigDecimal getTotalDInter() {
        totalDInter = BigDecimal.ZERO;
        if (listaInterconTotal != null) {
            for (int i = 0; i < listaInterconTotal.size(); i++) {
                totalDInter = totalDInter.add(listaInterconTotal.get(i).getValord());
            }
        }
        System.out.println("totalDInter : " + totalDInter);
        return totalDInter;
    }

    public void setTotalDInter(BigDecimal totalDInter) {
        this.totalDInter = totalDInter;
    }

    public BigDecimal getTotalCInter() {
        totalCInter = BigDecimal.ZERO;
        if (listaInterconTotal != null) {
            for (int i = 0; i < listaInterconTotal.size(); i++) {
                totalCInter = totalCInter.add(listaInterconTotal.get(i).getValorc());
            }
        }
        System.out.println("totalCInter : " + totalCInter);
        return totalCInter;
    }

    public void setTotalCInter(BigDecimal totalCInter) {
        this.totalCInter = totalCInter;
    }

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

}
