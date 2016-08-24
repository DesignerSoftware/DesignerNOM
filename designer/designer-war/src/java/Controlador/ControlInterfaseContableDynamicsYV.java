/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.ActualUsuario;
import Entidades.Empleados;
import Entidades.Empresas;
import Entidades.InterconDynamics;
import Entidades.ParametrosContables;
import Entidades.ParametrosEstructuras;
import Entidades.Procesos;
import Entidades.SolucionesNodos;
import Entidades.UsuariosInterfases;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarInterfaseContableDynamicsYVInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.math.BigInteger;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.primefaces.component.column.Column;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.export.Exporter;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;

/**
 *
 * @author Administrador
 */
@ManagedBean
@SessionScoped
public class ControlInterfaseContableDynamicsYV implements Serializable {

    @EJB
    AdministrarInterfaseContableDynamicsYVInterface administrarInterfaseDynamicsYV;
    @EJB
    AdministrarRastrosInterface administrarRastros;

    private ActualUsuario actualUsuarioBD;
    //
    private ParametrosContables parametroContableActual;
    private ParametrosContables nuevoParametroContable;
    private List<ParametrosContables> listaParametrosContables;
    private int indexParametroContable;
    private String auxParametroEmpresa, auxParametroProceso;
    private Date auxParametroFechaInicial, auxParametroFechaFinal;
    private String auxParametroEmplDesde, auxParametroEmplHasta;
    private boolean permitirIndexParametro;
    private boolean cambiosParametro;
    private List<ParametrosContables> listParametrosContablesBorrar;
    //
    private List<SolucionesNodos> listaGenerados;
    private List<SolucionesNodos> filtrarListaGenerados;
    private SolucionesNodos generadoTablaSeleccionado;
    private int indexGenerado, cualCeldaGenerado;
    private SolucionesNodos editarGenerado;
    private int tipoListaGenerada, banderaGenerado;
    //
    private List<InterconDynamics> listaInterconDynamics;
    private List<InterconDynamics> filtrarListaInterconDynamics;
    private InterconDynamics interconTablaSeleccionada;
    private int indexIntercon, cualCeldaIntercon;
    private InterconDynamics editarIntercon;
    private int tipoListaIntercon, banderaIntercon;

    private List<Empresas> lovEmpresas;
    private List<Empresas> filtrarLovEmpresas;
    private Empresas empresaSeleccionada;
    private String infoRegistroEmpresa;

    private List<Procesos> lovProcesos;
    private List<Procesos> filtrarLovProcesos;
    private Procesos procesoSeleccionado;
    private String infoRegistroProceso;

    private List<Empleados> lovEmpleados;
    private List<Empleados> filtrarLovEmpleados;
    private Empleados empleadoSeleccionado;
    private String infoRegistroEmpleado;
    //
    private String paginaAnterior;
    //
    private boolean guardado;
    private Date fechaDeParametro;
    private boolean aceptar;
    //
    private boolean estadoBtnArriba, estadoBtnAbajo;
    private int registroActual;
    //
    private boolean activarAgregar, activarOtros;
    private boolean activarEnviar, activarDeshacer;
    //
    private int tipoActualizacion;
    //
    private boolean modificacionParametro;
    //
    private Column genConcepto, genValor, genTercero, genCntDebito, genCntCredito, genEmpleado, genProceso;
    private String altoTablaGenerada;
    private Column interEmpleado, interTercero, interCuenta, interDebito, interCredito, interConcepto, interCentroCosto, interFechaVencimiento, interOriginalDebito, interOriginalCredito, interPreradicacion, interCodAlternativo;
    private String altoTablaIntercon;
    //
    private BigInteger secRegistro, backUpSecRegistro;
    //
    private int tipoPlano;
    //
    private String msnFechasActualizar;
    //
    private int totalCGenerado, totalDGenerado, totalDInter, totalCInter;
    //
    private String msnPaso1;
    //
    private String fechaIniRecon, fechaFinRecon;

    private String rutaArchivo, nombreArchivo, pathProceso;
    //
    private final String server = "192.168.0.16";
    private final String user = "Administrador";
    private final String pass = "Soporte9";

    private FTPClient ftpClient;
    private DefaultStreamedContent download;
    private UsuariosInterfases usuarioInterfaseContabilizacion;

    public ControlInterfaseContableDynamicsYV() {

        msnPaso1 = "";
        totalCGenerado = 0;
        totalDGenerado = 0;
        totalDInter = 0;
        totalCInter = 0;
        activarEnviar = true;
        activarDeshacer = true;
        msnFechasActualizar = "";
        tipoPlano = 1;
        altoTablaGenerada = "75";
        altoTablaIntercon = "75";
        tipoListaGenerada = 0;
        tipoListaIntercon = 0;
        banderaGenerado = 0;
        banderaIntercon = 0;
        modificacionParametro = false;
        listParametrosContablesBorrar = new ArrayList<ParametrosContables>();
        nuevoParametroContable = new ParametrosContables();
        nuevoParametroContable.setEmpresaRegistro(new Empresas());
        nuevoParametroContable.setProceso(new Procesos());
        activarAgregar = true;
        activarOtros = true;
        listaGenerados = null;
        listaInterconDynamics = null;
        generadoTablaSeleccionado = new SolucionesNodos();
        interconTablaSeleccionada = new InterconDynamics();
        indexGenerado = -1;
        indexIntercon = -1;
        cualCeldaGenerado = -1;
        cualCeldaIntercon = -1;
        editarGenerado = new SolucionesNodos();
        editarIntercon = new InterconDynamics();
        registroActual = 0;
        estadoBtnArriba = true;
        estadoBtnAbajo = true;
        aceptar = true;
        cambiosParametro = false;
        permitirIndexParametro = true;
        indexParametroContable = -1;
        guardado = true;
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarInterfaseDynamicsYV.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct ControlVigenciasCargos: " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void recibirPagina(String paginaAnt) {
        paginaAnterior = paginaAnt;
        actualUsuarioBD = null;
        getActualUsuarioBD();
        listaParametrosContables = null;
        getListaParametrosContables();
        parametroContableActual = null;
        getParametroContableActual();
    }

    public String redirigir() {
        return paginaAnterior;
    }

    public void anteriorParametro() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (guardado == true) {
            if (registroActual > 0) {
                registroActual--;
                parametroContableActual = listaParametrosContables.get(registroActual);
                listaGenerados = null;
                listaInterconDynamics = null;
                activarEnviar = true;
                activarDeshacer = true;
                context.update("form:btnEnviar");
                context.update("form:btnDeshacer");
                context.update("form:PLANO");
                totalCGenerado = 0;
                totalDGenerado = 0;
                totalDInter = 0;
                totalCInter = 0;
                if (registroActual == 0) {
                    estadoBtnArriba = true;
                }
                if (registroActual < (listaParametrosContables.size() - 1)) {
                    estadoBtnAbajo = false;
                }

                if (banderaGenerado == 1) {
                    FacesContext c = FacesContext.getCurrentInstance();
                    altoTablaGenerada = "75";
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
                    altoTablaIntercon = "75";
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
                    interFechaVencimiento = (Column) c.getViewRoot().findComponent("form:datosIntercon:interFechaVencimiento");
                    interFechaVencimiento.setFilterStyle("display: none; visibility: hidden;");
                    interOriginalDebito = (Column) c.getViewRoot().findComponent("form:datosIntercon:interOriginalDebito");
                    interOriginalDebito.setFilterStyle("display: none; visibility: hidden;");
                    interOriginalCredito = (Column) c.getViewRoot().findComponent("form:datosIntercon:interOriginalCredito");
                    interOriginalCredito.setFilterStyle("display: none; visibility: hidden;");
                    interPreradicacion = (Column) c.getViewRoot().findComponent("form:datosIntercon:interPreradicacion");
                    interPreradicacion.setFilterStyle("display: none; visibility: hidden;");
                    interCodAlternativo = (Column) c.getViewRoot().findComponent("form:datosIntercon:interCodAlternativo");
                    interCodAlternativo.setFilterStyle("display: none; visibility: hidden;");
                    RequestContext.getCurrentInstance().update("form:datosIntercon");
                    banderaIntercon = 0;
                    filtrarListaInterconDynamics = null;
                    tipoListaIntercon = 0;
                }
                activarEnviar = true;
                activarDeshacer = true;
                context.update("form:PanelTotal");
                context.update("form:panelParametro");
                context.update("form:btnArriba");
                context.update("form:btnAbajo");
            }
        } else {
            context.execute("confirmarGuardarSinSalida.show()");
        }
    }

    public void siguienteParametro() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (guardado == true) {
            if (registroActual < (listaParametrosContables.size() - 1)) {
                registroActual++;
                parametroContableActual = listaParametrosContables.get(registroActual);
                listaGenerados = null;
                listaInterconDynamics = null;
                activarEnviar = true;
                activarDeshacer = true;
                context.update("form:btnEnviar");
                context.update("form:btnDeshacer");
                context.update("form:PLANO");
                totalCGenerado = 0;
                totalDGenerado = 0;
                totalDInter = 0;
                totalCInter = 0;
                if (registroActual > 0) {
                    estadoBtnArriba = false;
                }
                if (registroActual == (listaParametrosContables.size() - 1)) {
                    estadoBtnAbajo = true;
                }

                if (banderaGenerado == 1) {
                    FacesContext c = FacesContext.getCurrentInstance();
                    altoTablaGenerada = "75";
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
                    altoTablaIntercon = "75";
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
                    interFechaVencimiento = (Column) c.getViewRoot().findComponent("form:datosIntercon:interFechaVencimiento");
                    interFechaVencimiento.setFilterStyle("display: none; visibility: hidden;");
                    interOriginalDebito = (Column) c.getViewRoot().findComponent("form:datosIntercon:interOriginalDebito");
                    interOriginalDebito.setFilterStyle("display: none; visibility: hidden;");
                    interOriginalCredito = (Column) c.getViewRoot().findComponent("form:datosIntercon:interOriginalCredito");
                    interOriginalCredito.setFilterStyle("display: none; visibility: hidden;");
                    interPreradicacion = (Column) c.getViewRoot().findComponent("form:datosIntercon:interPreradicacion");
                    interPreradicacion.setFilterStyle("display: none; visibility: hidden;");
                    interCodAlternativo = (Column) c.getViewRoot().findComponent("form:datosIntercon:interCodAlternativo");
                    interCodAlternativo.setFilterStyle("display: none; visibility: hidden;");
                    RequestContext.getCurrentInstance().update("form:datosIntercon");
                    banderaIntercon = 0;
                    filtrarListaInterconDynamics = null;
                    tipoListaIntercon = 0;
                }
                activarEnviar = true;
                activarDeshacer = true;
                context.update("form:PanelTotal");
                context.update("form:panelParametro");
                context.update("form:btnArriba");
                context.update("form:btnAbajo");
            }
        } else {
            context.execute("confirmarGuardarSinSalida.show()");
        }
    }

    public void modificarParametroContable() {
        if (guardado == true) {
            guardado = false;
        }
        cambiosParametro = true;
        modificacionParametro = true;
        indexParametroContable = -1;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void modificarParametroContable(String confirmarCambio, String valorConfirmar) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (valorConfirmar.equals("EMPRESA")) {
            parametroContableActual.getEmpresaRegistro().setNombre(auxParametroEmpresa);
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
                modificacionParametro = true;
                context.update("form:ACEPTAR");
                context.update("form:parametroEmpresa");
            } else {
                permitirIndexParametro = false;
                context.update("form:EmpresaDialogo");
                context.execute("EmpresaDialogo.show()");
            }
        } else if (valorConfirmar.equals("PROCESO")) {
            if (!confirmarCambio.isEmpty()) {
                parametroContableActual.getProceso().setDescripcion(auxParametroProceso);
                for (int i = 0; i < lovProcesos.size(); i++) {
                    if (lovProcesos.get(i).getDescripcion().startsWith(confirmarCambio.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    parametroContableActual.setProceso(lovProcesos.get(indiceUnicoElemento));
                    lovProcesos.clear();
                    getLovProcesos();
                    if (guardado == true) {
                        guardado = false;
                    }
                    cambiosParametro = true;
                    modificacionParametro = true;
                    context.update("form:ACEPTAR");
                    context.update("form:parametroProceso");
                } else {
                    permitirIndexParametro = false;
                    context.update("form:ProcesoDialogo");
                    context.execute("ProcesoDialogo.show()");
                }
            } else {
                parametroContableActual.setProceso(new Procesos());
                if (guardado == true) {
                    guardado = false;
                }
                cambiosParametro = true;
                modificacionParametro = true;
                context.update("form:ACEPTAR");
                context.update("form:parametroProceso");
            }
        } else if (valorConfirmar.equals("EMPLDESDE")) {
            if (!confirmarCambio.isEmpty()) {
                parametroContableActual.setStrCodigoEmpleadoDesde(auxParametroEmplDesde);
                for (int i = 0; i < lovEmpleados.size(); i++) {
                    if (lovEmpleados.get(i).getCodigoempleadoSTR().startsWith(confirmarCambio.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    parametroContableActual.setCodigoempleadodesde(lovEmpleados.get(indiceUnicoElemento).getCodigoempleado());
                    lovEmpleados.clear();
                    getLovEmpleados();
                    if (guardado == true) {
                        guardado = false;
                    }
                    cambiosParametro = true;
                    modificacionParametro = true;
                    context.update("form:ACEPTAR");
                    context.update("form:parametroProceso");
                } else {
                    permitirIndexParametro = false;
                    context.update("formEmplDialogo:EmpleadoDesdeDialogo");
                    context.execute("EmpleadoDesdeDialogo.show()");
                }
            } else {
                parametroContableActual.setCodigoempleadodesde(null);
                if (guardado == true) {
                    guardado = false;
                }
                cambiosParametro = true;
                modificacionParametro = true;
                context.update("form:ACEPTAR");
                context.update("form:parametroEmplDesde");
            }
        } else if (valorConfirmar.equals("EMPLHASTA")) {
            if (!confirmarCambio.isEmpty()) {
                parametroContableActual.setStrCodigoEmpleadoHasta(auxParametroEmplHasta);
                for (int i = 0; i < lovEmpleados.size(); i++) {
                    if (lovEmpleados.get(i).getCodigoempleadoSTR().startsWith(confirmarCambio.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    parametroContableActual.setCodigoempleadohasta(lovEmpleados.get(indiceUnicoElemento).getCodigoempleado());
                    lovEmpleados.clear();
                    getLovEmpleados();
                    if (guardado == true) {
                        guardado = false;
                    }
                    cambiosParametro = true;
                    modificacionParametro = true;
                    context.update("form:ACEPTAR");
                    context.update("form:parametroProceso");
                } else {
                    permitirIndexParametro = false;
                    context.update("formEmplDialogo:EmpleadoHastaDialogo");
                    context.execute("EmpleadoHastaDialogo.show()");
                }
            } else {
                parametroContableActual.setCodigoempleadohasta(null);
                if (guardado == true) {
                    guardado = false;
                }
                cambiosParametro = true;
                modificacionParametro = true;
                context.update("form:ACEPTAR");
                context.update("form:parametroEmplHasta");
            }
        }
    }

    public void modificarFechasParametro(int i) {
        RequestContext context = RequestContext.getCurrentInstance();
        if (parametroContableActual.getFechainicialcontabilizacion() != null && parametroContableActual.getFechafinalcontabilizacion() != null) {
            boolean validacion = validarFechaParametro(0);
            if (validacion == true) {
                cambiarIndiceParametro(i);
                parametroContableActual.setFechacontabilizacion(parametroContableActual.getFechafinalcontabilizacion());
                modificarParametroContable();
                context.update("form:panelParametro");
            } else {
                parametroContableActual.setFechafinalcontabilizacion(auxParametroFechaFinal);
                parametroContableActual.setFechainicialcontabilizacion(auxParametroFechaInicial);
                context.update("form:panelParametro:parametroFechaFinal");
                context.update("form:panelParametro:parametroFechaInicial");
                parametroContableActual.setFechacontabilizacion(parametroContableActual.getFechafinalcontabilizacion());
                context.update("form:panelParametro");
                context.execute("errorFechasParametro.show()");
            }
        } else {
            parametroContableActual.setFechafinalcontabilizacion(auxParametroFechaFinal);
            parametroContableActual.setFechainicialcontabilizacion(auxParametroFechaInicial);
            context.update("form:panelParametro:parametroFechaFinal");
            context.update("form:panelParametro:parametroFechaInicial");
            context.execute("errorFechasNull.show()");
        }
    }

    public boolean validarFechaParametro(int i) {
        fechaDeParametro = new Date();
        fechaDeParametro.setYear(0);
        fechaDeParametro.setMonth(1);
        fechaDeParametro.setDate(1);
        boolean retorno = true;
        if (i == 0) {
            if (parametroContableActual.getFechainicialcontabilizacion().after(fechaDeParametro) && parametroContableActual.getFechafinalcontabilizacion().after(fechaDeParametro)) {
                if (parametroContableActual.getFechafinalcontabilizacion().after(parametroContableActual.getFechainicialcontabilizacion())) {
                    retorno = true;
                } else {
                    retorno = false;
                }
            } else {
                retorno = false;
            }
        }
        if (i == 1) {
            if (nuevoParametroContable.getFechainicialcontabilizacion().after(fechaDeParametro) && nuevoParametroContable.getFechafinalcontabilizacion().after(fechaDeParametro)) {
                if (nuevoParametroContable.getFechafinalcontabilizacion().after(nuevoParametroContable.getFechainicialcontabilizacion())) {
                    retorno = true;
                } else {
                    retorno = false;
                }
            } else {
                retorno = false;
            }
        }
        return retorno;
    }

    public void posicionGenerado() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, String> map = context.getExternalContext().getRequestParameterMap();
        String name = map.get("n"); // name attribute of node
        String type = map.get("t"); // type attribute of node
        int indice = Integer.parseInt(type);
        int columna = Integer.parseInt(name);
        indexGenerado = indice;
        cualCeldaGenerado = columna;
        indexParametroContable = -1;
        indexIntercon = -1;
        if (banderaIntercon == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            altoTablaIntercon = "75";
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
            interFechaVencimiento = (Column) c.getViewRoot().findComponent("form:datosIntercon:interFechaVencimiento");
            interFechaVencimiento.setFilterStyle("display: none; visibility: hidden;");
            interOriginalDebito = (Column) c.getViewRoot().findComponent("form:datosIntercon:interOriginalDebito");
            interOriginalDebito.setFilterStyle("display: none; visibility: hidden;");
            interOriginalCredito = (Column) c.getViewRoot().findComponent("form:datosIntercon:interOriginalCredito");
            interOriginalCredito.setFilterStyle("display: none; visibility: hidden;");
            interPreradicacion = (Column) c.getViewRoot().findComponent("form:datosIntercon:interPreradicacion");
            interPreradicacion.setFilterStyle("display: none; visibility: hidden;");
            interCodAlternativo = (Column) c.getViewRoot().findComponent("form:datosIntercon:interCodAlternativo");
            interCodAlternativo.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosIntercon");
            banderaIntercon = 0;
            filtrarListaInterconDynamics = null;
            tipoListaIntercon = 0;
        }
    }

    public void posicionIntercon() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, String> map = context.getExternalContext().getRequestParameterMap();
        String name = map.get("n"); // name attribute of node
        String type = map.get("t"); // type attribute of node
        int indice = Integer.parseInt(type);
        int columna = Integer.parseInt(name);
        indexIntercon = indice;
        cualCeldaIntercon = columna;
        indexParametroContable = -1;
        indexGenerado = -1;
        if (tipoListaIntercon == 0) {
            secRegistro = listaInterconDynamics.get(indexIntercon).getSecuencia();
        } else {
            secRegistro = filtrarListaInterconDynamics.get(indexIntercon).getSecuencia();
        }
        if (banderaGenerado == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            altoTablaGenerada = "75";
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
    }

    public void cambiarIndiceParametro(int indice) {
        System.out.println("Entre al metodo");
        if (permitirIndexParametro == true) {
            indexParametroContable = indice;
            indexGenerado = -1;
            indexIntercon = -1;
            auxParametroEmpresa = parametroContableActual.getEmpresaRegistro().getNombre();
            auxParametroProceso = parametroContableActual.getProceso().getDescripcion();
            auxParametroFechaFinal = parametroContableActual.getFechafinalcontabilizacion();
            auxParametroFechaInicial = parametroContableActual.getFechainicialcontabilizacion();
            auxParametroEmplDesde = parametroContableActual.getStrCodigoEmpleadoDesde();
            auxParametroEmplHasta = parametroContableActual.getStrCodigoEmpleadoHasta();
            if (banderaGenerado == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                altoTablaGenerada = "75";
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
                altoTablaIntercon = "75";
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
                interFechaVencimiento = (Column) c.getViewRoot().findComponent("form:datosIntercon:interFechaVencimiento");
                interFechaVencimiento.setFilterStyle("display: none; visibility: hidden;");
                interOriginalDebito = (Column) c.getViewRoot().findComponent("form:datosIntercon:interOriginalDebito");
                interOriginalDebito.setFilterStyle("display: none; visibility: hidden;");
                interOriginalCredito = (Column) c.getViewRoot().findComponent("form:datosIntercon:interOriginalCredito");
                interOriginalCredito.setFilterStyle("display: none; visibility: hidden;");
                interPreradicacion = (Column) c.getViewRoot().findComponent("form:datosIntercon:interPreradicacion");
                interPreradicacion.setFilterStyle("display: none; visibility: hidden;");
                interCodAlternativo = (Column) c.getViewRoot().findComponent("form:datosIntercon:interCodAlternativo");
                interCodAlternativo.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosIntercon");
                banderaIntercon = 0;
                filtrarListaInterconDynamics = null;
                tipoListaIntercon = 0;
            }
            System.out.println("indexParametroContable : " + indexParametroContable);
        }
    }

    public String msnPaso1() {
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
            int contador = administrarInterfaseDynamicsYV.contarProcesosContabilizadosInterconDynamics(parametroContableActual.getFechainicialcontabilizacion(), parametroContableActual.getFechafinalcontabilizacion());
            if (contador != -1) {
                if (contador == 0) {
                    context.execute("contadoCeroPerContable.show()");
                } else {
                    context.update("form:paso1CerrarPeriodo");
                    context.execute("paso1CerrarPeriodo.show()");
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
            administrarInterfaseDynamicsYV.cerrarProcesoContable(parametroContableActual.getFechainicialcontabilizacion(), parametroContableActual.getFechafinalcontabilizacion(), parametroContableActual.getProceso().getSecuencia(), parametroContableActual.getCodigoempleadodesde(), parametroContableActual.getCodigoempleadohasta());
            listaGenerados = null;
            if (listaGenerados == null) {
                listaGenerados = administrarInterfaseDynamicsYV.obtenerSolucionesNodosParametroContable(parametroContableActual.getFechainicialcontabilizacion(), parametroContableActual.getFechafinalcontabilizacion());
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
            listaInterconDynamics = null;
            if (listaInterconDynamics == null) {
                listaInterconDynamics = administrarInterfaseDynamicsYV.obtenerInterconDynamicsParametroContable(parametroContableActual.getFechainicialcontabilizacion(), parametroContableActual.getFechafinalcontabilizacion());
                if (listaInterconDynamics != null) {
                    if (listaInterconDynamics.size() > 0) {
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

            context.update("form:btnEnviar");
            context.update("form:btnDeshacer");
            context.update("form:PLANO");

            context.update("form:totalDGenerado");
            context.update("form:totalCGenerado");
            context.update("form:totalDInter");
            context.update("form:totalCInter");
            if (banderaGenerado == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                altoTablaGenerada = "75";
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
                altoTablaIntercon = "75";
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
                interFechaVencimiento = (Column) c.getViewRoot().findComponent("form:datosIntercon:interFechaVencimiento");
                interFechaVencimiento.setFilterStyle("display: none; visibility: hidden;");
                interOriginalDebito = (Column) c.getViewRoot().findComponent("form:datosIntercon:interOriginalDebito");
                interOriginalDebito.setFilterStyle("display: none; visibility: hidden;");
                interOriginalCredito = (Column) c.getViewRoot().findComponent("form:datosIntercon:interOriginalCredito");
                interOriginalCredito.setFilterStyle("display: none; visibility: hidden;");
                interPreradicacion = (Column) c.getViewRoot().findComponent("form:datosIntercon:interPreradicacion");
                interPreradicacion.setFilterStyle("display: none; visibility: hidden;");
                interCodAlternativo = (Column) c.getViewRoot().findComponent("form:datosIntercon:interCodAlternativo");
                interCodAlternativo.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosIntercon");
                banderaIntercon = 0;
                filtrarListaInterconDynamics = null;
                tipoListaIntercon = 0;
            }
            context.update("form:datosGenerados");
            context.update("form:datosIntercon");
        } catch (Exception e) {
            System.out.println("Error finCerrarPeriodoContable Controlador : " + e.toString());
        }
    }

    public void actionBtnGenerarPlano() {
        try {
            guardadoGeneral();
            String descripcionProceso = administrarInterfaseDynamicsYV.obtenerDescripcionProcesoArchivo(parametroContableActual.getProceso().getSecuencia());
            nombreArchivo = "Interfase_DYNAMICS_" + descripcionProceso;
            pathProceso = administrarInterfaseDynamicsYV.obtenerPathProceso();
            administrarInterfaseDynamicsYV.ejecutarPKGCrearArchivoPlano(parametroContableActual.getFechainicialcontabilizacion(), parametroContableActual.getFechafinalcontabilizacion(), parametroContableActual.getProceso().getSecuencia(), descripcionProceso, nombreArchivo, parametroContableActual.getCodigoempleadodesde(), parametroContableActual.getCodigoempleadohasta());
            RequestContext context = RequestContext.getCurrentInstance();
            rutaArchivo = "";
            rutaArchivo = pathProceso + nombreArchivo + ".txt";
            context.update("formularioDialogos:planoGeneradoOK");
            context.execute("planoGeneradoOK.show()");
        } catch (Exception e) {
            System.out.println("Error actionBtnGenerarPlano Control : " + e.toString());
        }
    }

    public void conectarAlFTP() {
        try {
            ftpClient.connect(usuarioInterfaseContabilizacion.getServernameremoto());
            ftpClient.login(usuarioInterfaseContabilizacion.getUsuarioremoto(), usuarioInterfaseContabilizacion.getPasswordremoto());
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        } catch (Exception e) {
            System.out.println("Error en conexion : " + e.toString());
        }
    }

    public void descargarArchivoFTP() throws IOException {
        try {
            usuarioInterfaseContabilizacion = administrarInterfaseDynamicsYV.obtenerUsuarioInterfaseContabilizacion();
            conectarAlFTP();
            int tamPath = pathProceso.length();
            String rutaX = "";
            for (int i = 2; i < tamPath; i++) {
                rutaX = rutaX + pathProceso.charAt(i) + "";
            }
            String remoteFile1 = rutaX + nombreArchivo + ".txt";
            File downloadFile1 = new File(pathProceso + nombreArchivo + ".txt");
            OutputStream outputStream1 = new BufferedOutputStream(new FileOutputStream(downloadFile1));
            boolean success = ftpClient.retrieveFile(remoteFile1, outputStream1);
            outputStream1.close();
            if (success) {
                System.out.println("File #1 has been downloaded successfully.");
            } else {
                System.out.println("Ni mierda !");
            }
            ftpClient.logout();
            File file = new File(pathProceso + nombreArchivo + ".txt");
            InputStream input = new FileInputStream(file);
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            setDownload(new DefaultStreamedContent(input, externalContext.getMimeType(file.getName()), file.getName()));
            RequestContext.getCurrentInstance().execute("planoGeneradoOK.hide()");
        } catch (Exception e) {
            System.out.println("Error descarga : " + e.toString());
        }
    }

    public void cerrarPaginaDescarga() {
        RequestContext.getCurrentInstance().execute("planoGeneradoOK.hide()");
    }

    public void actionBtnRecontabilizar() {
        Integer contador = administrarInterfaseDynamicsYV.conteoContabilizacionesDynamics(parametroContableActual.getFechainicialcontabilizacion(), parametroContableActual.getFechafinalcontabilizacion());
        if (contador != null) {
            if (contador != 0) {
                RequestContext.getCurrentInstance().execute("paso1Recon.show()");
            } else {
                RequestContext.getCurrentInstance().execute("errorRecon.show()");
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
            context.update("form:paso3Recon");
            context.execute("paso3Recon.show()");
        }
    }

    public void finalizarProcesoRecontabilizacion() {
        try {
            guardadoGeneral();
            administrarInterfaseDynamicsYV.ejecutarPKGRecontabilizar(parametroContableActual.getFechainicialcontabilizacion(), parametroContableActual.getFechafinalcontabilizacion());
        } catch (Exception e) {
            System.out.println("Error finalizarProcesoRecontabilizacion Controlador : " + e.toString());
        }

    }

    public void actionBtnDeshacer() {
        try {
            RequestContext context = RequestContext.getCurrentInstance();
            guardadoGeneral();
            administrarInterfaseDynamicsYV.actualizarFlagContabilizacionDeshacerDynamics(parametroContableActual.getFechainicialcontabilizacion(), parametroContableActual.getFechafinalcontabilizacion(), parametroContableActual.getProceso().getSecuencia(), parametroContableActual.getCodigoempleadodesde(), parametroContableActual.getCodigoempleadohasta());
            administrarInterfaseDynamicsYV.deleteInterconDynamics(parametroContableActual.getFechainicialcontabilizacion(), parametroContableActual.getFechafinalcontabilizacion(), parametroContableActual.getProceso().getSecuencia(), parametroContableActual.getCodigoempleadodesde(), parametroContableActual.getCodigoempleadohasta());
            administrarInterfaseDynamicsYV.actualizarFlagContabilizacionDeshacerDynamics_NOT_EXITS(parametroContableActual.getFechainicialcontabilizacion(), parametroContableActual.getFechafinalcontabilizacion(), parametroContableActual.getProceso().getSecuencia(), parametroContableActual.getCodigoempleadodesde(), parametroContableActual.getCodigoempleadohasta());
            listaGenerados = null;
            if (listaGenerados == null) {
                listaGenerados = administrarInterfaseDynamicsYV.obtenerSolucionesNodosParametroContable(parametroContableActual.getFechainicialcontabilizacion(), parametroContableActual.getFechafinalcontabilizacion());
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
            listaInterconDynamics = null;
            if (listaInterconDynamics == null) {
                listaInterconDynamics = administrarInterfaseDynamicsYV.obtenerInterconDynamicsParametroContable(parametroContableActual.getFechainicialcontabilizacion(), parametroContableActual.getFechafinalcontabilizacion());
                if (listaInterconDynamics != null) {
                    if (listaInterconDynamics.size() > 0) {
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

            context.update("form:btnEnviar");
            context.update("form:btnDeshacer");
            context.update("form:PLANO");

            context.update("form:totalDGenerado");
            context.update("form:totalCGenerado");
            context.update("form:totalDInter");
            context.update("form:totalCInter");
            if (banderaGenerado == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                altoTablaGenerada = "75";
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
                altoTablaIntercon = "75";
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
                interFechaVencimiento = (Column) c.getViewRoot().findComponent("form:datosIntercon:interFechaVencimiento");
                interFechaVencimiento.setFilterStyle("display: none; visibility: hidden;");
                interOriginalDebito = (Column) c.getViewRoot().findComponent("form:datosIntercon:interOriginalDebito");
                interOriginalDebito.setFilterStyle("display: none; visibility: hidden;");
                interOriginalCredito = (Column) c.getViewRoot().findComponent("form:datosIntercon:interOriginalCredito");
                interOriginalCredito.setFilterStyle("display: none; visibility: hidden;");
                interPreradicacion = (Column) c.getViewRoot().findComponent("form:datosIntercon:interPreradicacion");
                interPreradicacion.setFilterStyle("display: none; visibility: hidden;");
                interCodAlternativo = (Column) c.getViewRoot().findComponent("form:datosIntercon:interCodAlternativo");
                interCodAlternativo.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosIntercon");
                banderaIntercon = 0;
                filtrarListaInterconDynamics = null;
                tipoListaIntercon = 0;
            }
            context.update("form:datosGenerados");
            context.update("form:datosIntercon");
        } catch (Exception e) {
            System.out.println("Error actionBtnDeshacer Controlador : " + e.toString());
        }
    }

    public void actionBtnEnviar() {
        Date fechaDesde = administrarInterfaseDynamicsYV.buscarFechaDesdeVWActualesFechas();
        Date fechaHasta = administrarInterfaseDynamicsYV.buscarFechaHastaVWActualesFechas();
        RequestContext context = RequestContext.getCurrentInstance();
        if (fechaDesde != null && fechaHasta != null) {
            if ((fechaDesde.before(parametroContableActual.getFechainicialcontabilizacion()) && fechaHasta.after(parametroContableActual.getFechafinalcontabilizacion()))
                    || (fechaDesde.before(parametroContableActual.getFechainicialcontabilizacion()) && fechaHasta.after(parametroContableActual.getFechafinalcontabilizacion()))) {
                context.execute("errorVWActualesFechas.show()");
            } else {
                if (parametroContableActual.getEmpresaRegistro().getSecuencia() != null
                        && parametroContableActual.getFechafinalcontabilizacion() != null
                        && parametroContableActual.getFechainicialcontabilizacion() != null) {
                    guardadoGeneral();
                    administrarInterfaseDynamicsYV.ejecutarPKGUbicarnuevointercon_DYNAMICS(parametroContableActual.getSecuencia(), parametroContableActual.getFechainicialcontabilizacion(), parametroContableActual.getFechafinalcontabilizacion(), parametroContableActual.getProceso().getSecuencia(), parametroContableActual.getCodigoempleadodesde(), parametroContableActual.getCodigoempleadohasta());
                    listaGenerados = null;
                    if (listaGenerados == null) {
                        listaGenerados = administrarInterfaseDynamicsYV.obtenerSolucionesNodosParametroContable(parametroContableActual.getFechainicialcontabilizacion(), parametroContableActual.getFechafinalcontabilizacion());
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
                    listaInterconDynamics = null;
                    if (listaInterconDynamics == null) {
                        listaInterconDynamics = administrarInterfaseDynamicsYV.obtenerInterconDynamicsParametroContable(parametroContableActual.getFechainicialcontabilizacion(), parametroContableActual.getFechafinalcontabilizacion());
                        if (listaInterconDynamics != null) {
                            if (listaInterconDynamics.size() > 0) {
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
                    context.update("form:totalDGenerado");
                    context.update("form:totalCGenerado");
                    context.update("form:totalDInter");
                    context.update("form:totalCInter");

                    context.update("form:btnEnviar");
                    context.update("form:btnDeshacer");
                    context.update("form:PLANO");
                    if (banderaGenerado == 1) {
                        FacesContext c = FacesContext.getCurrentInstance();
                        altoTablaGenerada = "75";
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
                        altoTablaIntercon = "75";
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
                        interFechaVencimiento = (Column) c.getViewRoot().findComponent("form:datosIntercon:interFechaVencimiento");
                        interFechaVencimiento.setFilterStyle("display: none; visibility: hidden;");
                        interOriginalDebito = (Column) c.getViewRoot().findComponent("form:datosIntercon:interOriginalDebito");
                        interOriginalDebito.setFilterStyle("display: none; visibility: hidden;");
                        interOriginalCredito = (Column) c.getViewRoot().findComponent("form:datosIntercon:interOriginalCredito");
                        interOriginalCredito.setFilterStyle("display: none; visibility: hidden;");
                        interPreradicacion = (Column) c.getViewRoot().findComponent("form:datosIntercon:interPreradicacion");
                        interPreradicacion.setFilterStyle("display: none; visibility: hidden;");
                        interCodAlternativo = (Column) c.getViewRoot().findComponent("form:datosIntercon:interCodAlternativo");
                        interCodAlternativo.setFilterStyle("display: none; visibility: hidden;");
                        RequestContext.getCurrentInstance().update("form:datosIntercon");
                        banderaIntercon = 0;
                        filtrarListaInterconDynamics = null;
                        tipoListaIntercon = 0;
                    }
                    context.update("form:datosGenerados");
                    context.update("form:datosIntercon");
                }
            }
        }
    }

    public void anularComprobantesCerrados() {
        try {
            guardadoGeneral();
            administrarInterfaseDynamicsYV.anularComprobantesCerrados(parametroContableActual.getFechainicialcontabilizacion(), parametroContableActual.getFechafinalcontabilizacion(), parametroContableActual.getProceso().getSecuencia());
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
        Date fechaContabilizacion = administrarInterfaseDynamicsYV.obtenerFechaMaxContabilizaciones();
        Date fechaInterconTotal = administrarInterfaseDynamicsYV.obtenerFechaMaxInterconDynamics();
        DateFormat df = DateFormat.getDateInstance(DateFormat.LONG);
        RequestContext context = RequestContext.getCurrentInstance();
        if (fechaContabilizacion != null && fechaInterconTotal != null) {
            if (fechaContabilizacion.compareTo(fechaInterconTotal) == 0) {
                String fecha = df.format(fechaContabilizacion);
                msnFechasActualizar = fecha;
                context.update("form:anteriorContabilizacion");
                context.execute("anteriorContabilizacion.show()");
            } else {
                context.update("form:nuncaContabilizo");
                context.execute("nuncaContabilizo.show()");
            }
        }

    }

    public boolean validarFechasParametros() {
        boolean retorno = false;
        ParametrosEstructuras parametroLiquidacion = administrarInterfaseDynamicsYV.parametrosLiquidacion();
        if ((parametroLiquidacion.getFechadesdecausado().compareTo(parametroContableActual.getFechainicialcontabilizacion()) == 0)
                && (parametroLiquidacion.getFechahastacausado().compareTo(parametroContableActual.getFechafinalcontabilizacion()) == 0)) {
            retorno = true;
        }
        return retorno;
    }

    public void actionBtnActualizar() {
        RequestContext context = RequestContext.getCurrentInstance();
        boolean validar = validarFechasParametros();
        if (validar == true) {
            //guardadoGeneral();

            listaGenerados = null;
            if (listaGenerados == null) {
                listaGenerados = administrarInterfaseDynamicsYV.obtenerSolucionesNodosParametroContable(parametroContableActual.getFechainicialcontabilizacion(), parametroContableActual.getFechafinalcontabilizacion());
                if (listaGenerados != null) {
                    if (listaGenerados.size() > 0) {
                        activarEnviar = false;
                    } else {
                        activarEnviar = true;
                    }
                } else {
                    activarEnviar = true;
                }
                getTotalCGenerado();
                getTotalDGenerado();
            }
            /*
           
            
             */
            listaInterconDynamics = null;
            if (listaInterconDynamics == null) {
                listaInterconDynamics = administrarInterfaseDynamicsYV.obtenerInterconDynamicsParametroContable(parametroContableActual.getFechainicialcontabilizacion(), parametroContableActual.getFechafinalcontabilizacion());
                if (listaInterconDynamics != null) {
                    if (listaInterconDynamics.size() > 0) {
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

            context.update("form:PanelTotal");
            int tam1 = 0;
            int tam2 = 0;
            if (listaGenerados != null) {
                tam1 = listaGenerados.size();
            }
            if (listaInterconDynamics != null) {
                tam2 = listaInterconDynamics.size();
            }
            if (tam1 == 0 && tam2 == 0) {
                context.execute("procesoSinDatos.show()");
            }
            //validarFechasProcesoActualizar();

            System.out.println("I finish");
        } else {
            context.execute("errorFechasParametros.show()");
        }
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
            if (modificacionParametro == true) {
                administrarInterfaseDynamicsYV.modificarParametroContable(parametroContableActual);
                modificacionParametro = false;
            }
            if (!listParametrosContablesBorrar.isEmpty()) {
                for (int i = 0; i < listParametrosContablesBorrar.size(); i++) {
                    administrarInterfaseDynamicsYV.borrarParametroContable(listParametrosContablesBorrar);
                }
                listParametrosContablesBorrar.clear();
            }
            listaParametrosContables = null;
            getListaParametrosContables();
            parametroContableActual = null;
            getParametroContableActual();
            cambiosParametro = false;
            FacesMessage msg = new FacesMessage("Información", "Se guardarón los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
            context.update("form:PanelTotal");
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
            altoTablaGenerada = "75";
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
            altoTablaIntercon = "75";
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
            interFechaVencimiento = (Column) c.getViewRoot().findComponent("form:datosIntercon:interFechaVencimiento");
            interFechaVencimiento.setFilterStyle("display: none; visibility: hidden;");
            interOriginalDebito = (Column) c.getViewRoot().findComponent("form:datosIntercon:interOriginalDebito");
            interOriginalDebito.setFilterStyle("display: none; visibility: hidden;");
            interOriginalCredito = (Column) c.getViewRoot().findComponent("form:datosIntercon:interOriginalCredito");
            interOriginalCredito.setFilterStyle("display: none; visibility: hidden;");
            interPreradicacion = (Column) c.getViewRoot().findComponent("form:datosIntercon:interPreradicacion");
            interPreradicacion.setFilterStyle("display: none; visibility: hidden;");
            interCodAlternativo = (Column) c.getViewRoot().findComponent("form:datosIntercon:interCodAlternativo");
            interCodAlternativo.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosIntercon");
            banderaIntercon = 0;
            filtrarListaInterconDynamics = null;
            tipoListaIntercon = 0;
        }
        totalCGenerado = 0;
        totalDGenerado = 0;
        totalDInter = 0;
        totalCInter = 0;
        activarEnviar = true;
        activarDeshacer = true;
        modificacionParametro = false;
        aceptar = true;
        listParametrosContablesBorrar.clear();
        actualUsuarioBD = null;
        getActualUsuarioBD();
        listaParametrosContables = null;
        getListaParametrosContables();
        parametroContableActual = null;
        listaGenerados = null;
        listaInterconDynamics = null;
        getParametroContableActual();
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("form:btnEnviar");
        context.update("form:btnDeshacer");
        context.update("form:PLANO");
        context.update("form:PanelTotal");
        cambiosParametro = false;
        guardado = true;
        indexParametroContable = -1;
        indexGenerado = -1;
        indexIntercon = -1;
        context.update("form:ACEPTAR");
    }

    public void editarCelda() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (indexParametroContable >= 0) {
            if (indexParametroContable == 0) {
                context.update("formularioDialogos:editarEmpresaParametro");
                context.execute("editarEmpresaParametro.show()");
                indexParametroContable = -1;
            } else if (indexParametroContable == 1) {
                context.update("formularioDialogos:editarProcesoParametro");
                context.execute("editarProcesoParametro.show()");
                indexParametroContable = -1;
            } else if (indexParametroContable == 2) {
                context.update("formularioDialogos:editarFechaInicialParametro");
                context.execute("editarFechaInicialParametro.show()");
                indexParametroContable = -1;
            } else if (indexParametroContable == 3) {
                context.update("formularioDialogos:editarFechaFinalParametro");
                context.execute("editarFechaFinalParametro.show()");
                indexParametroContable = -1;
            } else if (indexParametroContable == 4) {
                context.update("formularioDialogos:editarFechaContaParametro");
                context.execute("editarFechaContaParametro.show()");
                indexParametroContable = -1;
            } else if (indexParametroContable == 5) {
                context.update("formularioDialogos:editarEmplDesdeParametro");
                context.execute("editarEmplDesdeParametro.show()");
                indexParametroContable = -1;
            } else if (indexParametroContable == 6) {
                context.update("formularioDialogos:editarEmplHastaParametro");
                context.execute("editarEmplHastaParametro.show()");
                indexParametroContable = -1;
            } else if (indexParametroContable == 7) {
                context.update("formularioDialogos:editarChequeraParametro");
                context.execute("editarChequeraParametro.show()");
                indexParametroContable = -1;
            } else if (indexParametroContable == 8) {
                context.update("formularioDialogos:editarRespuestaParametro");
                context.execute("editarRespuestaParametro.show()");
                indexParametroContable = -1;
            }

        }
        if (indexGenerado >= 0) {
            if (tipoListaGenerada == 0) {
                editarGenerado = listaGenerados.get(indexGenerado);
            } else {
                editarGenerado = filtrarListaGenerados.get(indexGenerado);
            }
            if (cualCeldaGenerado == 0) {
                context.update("formularioDialogos:editarProcesoGenerado");
                context.execute("editarProcesoGenerado.show()");
                cualCeldaGenerado = -1;
            } else if (cualCeldaGenerado == 1) {
                context.update("formularioDialogos:editarEmpleadoGenerado");
                context.execute("editarEmpleadoGenerado.show()");
                cualCeldaGenerado = -1;
            } else if (cualCeldaGenerado == 2) {
                context.update("formularioDialogos:editarCreditoGenerado");
                context.execute("editarCreditoGenerado.show()");
                cualCeldaGenerado = -1;
            } else if (cualCeldaGenerado == 3) {
                context.update("formularioDialogos:editarDebitoGenerado");
                context.execute("editarDebitoGenerado.show()");
                cualCeldaGenerado = -1;
            } else if (cualCeldaGenerado == 4) {
                context.update("formularioDialogos:editarTerceroGenerado");
                context.execute("editarTerceroGenerado.show()");
                cualCeldaGenerado = -1;
            } else if (cualCeldaGenerado == 5) {
                context.update("formularioDialogos:editarValorGenerado");
                context.execute("editarValorGenerado.show()");
                cualCeldaGenerado = -1;
            } else if (cualCeldaGenerado == 6) {
                context.update("formularioDialogos:editarConceptoGenerado");
                context.execute("editarConceptoGenerado.show()");
                cualCeldaGenerado = -1;
            }
            indexGenerado = -1;
        }
        if (indexIntercon >= 0) {
            if (tipoListaIntercon == 0) {
                editarIntercon = listaInterconDynamics.get(indexIntercon);
            } else {
                editarIntercon = filtrarListaInterconDynamics.get(indexIntercon);
            }
            if (cualCeldaIntercon == 0) {
                context.update("formularioDialogos:editarEmpleadoIntercon");
                context.execute("editarEmpleadoIntercon.show()");
                cualCeldaIntercon = -1;
            } else if (cualCeldaIntercon == 1) {
                context.update("formularioDialogos:editarTerceroIntercon");
                context.execute("editarTerceroIntercon.show()");
                cualCeldaIntercon = -1;
            } else if (cualCeldaIntercon == 2) {
                context.update("formularioDialogos:editarCuentaIntercon");
                context.execute("editarCuentaIntercon.show()");
                cualCeldaIntercon = -1;
            } else if (cualCeldaIntercon == 3) {
                context.update("formularioDialogos:editarDebitoIntercon");
                context.execute("editarDebitoIntercon.show()");
                cualCeldaIntercon = -1;
            } else if (cualCeldaIntercon == 4) {
                context.update("formularioDialogos:editarCreditoIntercon");
                context.execute("editarCreditoIntercon.show()");
                cualCeldaIntercon = -1;
            } else if (cualCeldaIntercon == 5) {
                context.update("formularioDialogos:editarConceptoIntercon");
                context.execute("editarConceptoIntercon.show()");
                cualCeldaIntercon = -1;
            } else if (cualCeldaIntercon == 6) {
                context.update("formularioDialogos:editarCentroCostoIntercon");
                context.execute("editarCentroCostoIntercon.show()");
                cualCeldaIntercon = -1;
            } else if (cualCeldaIntercon == 7) {
                context.update("formularioDialogos:editarFechaVencimientoIntercon");
                context.execute("editarFechaVencimientoIntercon.show()");
                cualCeldaIntercon = -1;
            } else if (cualCeldaIntercon == 8) {
                context.update("formularioDialogos:editarValorOriginalDebitoIntercon");
                context.execute("editarValorOriginalDebitoIntercon.show()");
                cualCeldaIntercon = -1;
            } else if (cualCeldaIntercon == 9) {
                context.update("formularioDialogos:editarValorOriginalCreditoIntercon");
                context.execute("editarValorOriginalCreditoIntercon.show()");
                cualCeldaIntercon = -1;
            } else if (cualCeldaIntercon == 10) {
                context.update("formularioDialogos:editarPreRadicacionIntercon");
                context.execute("editarPreRadicacionIntercon.show()");
                cualCeldaIntercon = -1;
            } else if (cualCeldaIntercon == 11) {
                context.update("formularioDialogos:editarCodAlternativoIntercon");
                context.execute("editarCodAlternativoIntercon.show()");
                cualCeldaIntercon = -1;
            }
            indexIntercon = -1;
        }
    }

    public void salir() {
        if (banderaGenerado == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            altoTablaGenerada = "75";
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
            altoTablaIntercon = "75";
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
            interFechaVencimiento = (Column) c.getViewRoot().findComponent("form:datosIntercon:interFechaVencimiento");
            interFechaVencimiento.setFilterStyle("display: none; visibility: hidden;");
            interOriginalDebito = (Column) c.getViewRoot().findComponent("form:datosIntercon:interOriginalDebito");
            interOriginalDebito.setFilterStyle("display: none; visibility: hidden;");
            interOriginalCredito = (Column) c.getViewRoot().findComponent("form:datosIntercon:interOriginalCredito");
            interOriginalCredito.setFilterStyle("display: none; visibility: hidden;");
            interPreradicacion = (Column) c.getViewRoot().findComponent("form:datosIntercon:interPreradicacion");
            interPreradicacion.setFilterStyle("display: none; visibility: hidden;");
            interCodAlternativo = (Column) c.getViewRoot().findComponent("form:datosIntercon:interCodAlternativo");
            interCodAlternativo.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosIntercon");
            banderaIntercon = 0;
            filtrarListaInterconDynamics = null;
            tipoListaIntercon = 0;
        }
        RequestContext context = RequestContext.getCurrentInstance();
        listParametrosContablesBorrar.clear();
        modificacionParametro = false;
        listaParametrosContables = null;
        getListaParametrosContables();
        parametroContableActual = null;
        listaGenerados = null;
        listaInterconDynamics = null;
        actualUsuarioBD = null;
        cambiosParametro = false;
        guardado = true;
        indexParametroContable = -1;
        indexGenerado = -1;
        indexIntercon = -1;
        activarEnviar = true;
        activarDeshacer = true;
        totalCGenerado = 0;
        totalDGenerado = 0;
        totalDInter = 0;
        totalCInter = 0;
        context.update("form:ACEPTAR");
    }

    public void asignarIndex(Integer indice, int numeroDialogo, int tipoNuevo) {
        tipoActualizacion = tipoNuevo;
        indexParametroContable = indice;
        RequestContext context = RequestContext.getCurrentInstance();
        if (numeroDialogo == 0) {
            context.update("form:EmpresaDialogo");
            context.execute("EmpresaDialogo.show()");
        } else if (numeroDialogo == 1) {
            context.update("form:ProcesoDialogo");
            context.execute("ProcesoDialogo.show()");
        } else if (numeroDialogo == 2) {
            context.update("formEmplDialogo:EmpleadoDesdeDialogo");
            context.execute("EmpleadoDesdeDialogo.show()");
        } else if (numeroDialogo == 3) {
            context.update("formEmplDialogo:EmpleadoHastaDialogo");
            context.execute("EmpleadoHastaDialogo.show()");
        }
    }

    public void actualizarEmpresa() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            parametroContableActual.setEmpresaRegistro(empresaSeleccionada);
            parametroContableActual.setEmpresaCodigo(empresaSeleccionada.getCodigo());
            indexParametroContable = -1;
            if (guardado == true) {
                guardado = false;
            }
            modificacionParametro = true;
            cambiosParametro = true;
            context.update("form:ACEPTAR");
            context.update("form:parametroEmpresa");
        }
        if (tipoActualizacion == 1) {
            nuevoParametroContable.setEmpresaRegistro(empresaSeleccionada);
            nuevoParametroContable.setEmpresaCodigo(empresaSeleccionada.getCodigo());
            context.update("formularioDialogos:nuevaEmpresaParametro");
        }
        empresaSeleccionada = new Empresas();
        filtrarLovEmpresas = null;
        aceptar = true;/*
         context.update("form:EmpresaDialogo");
         context.update("form:lovEmpresa");
         context.update("form:aceptarE");*/

        context.reset("form:lovEmpresa:globalFilter");
        context.execute("lovEmpresa.clearFilters()");
        context.execute("EmpresaDialogo.hide()");
    }

    public void cancelarEmpresa() {
        empresaSeleccionada = new Empresas();
        filtrarLovEmpresas = null;
        indexParametroContable = -1;
        permitirIndexParametro = true;
        aceptar = true;
        tipoActualizacion = -1;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovEmpresa:globalFilter");
        context.execute("lovEmpresa.clearFilters()");
        context.execute("EmpresaDialogo.hide()");
    }

    public void actualizarProceso() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            parametroContableActual.setProceso(procesoSeleccionado);
            indexParametroContable = -1;
            if (guardado == true) {
                guardado = false;
            }
            modificacionParametro = true;
            cambiosParametro = true;
            context.update("form:ACEPTAR");
            context.update("form:parametroProceso");
        }
        if (tipoActualizacion == 1) {
            nuevoParametroContable.setProceso(procesoSeleccionado);
            context.update("formularioDialogos:nuevaProcesoParametro");
        }
        procesoSeleccionado = new Procesos();
        filtrarLovProcesos = null;
        aceptar = true;/*
         context.update("form:ProcesoDialogo");
         context.update("form:lovProceso");
         context.update("form:aceptarP");*/

        context.reset("form:lovProceso:globalFilter");
        context.execute("lovProceso.clearFilters()");
        context.execute("ProcesoDialogo.hide()");
    }

    public void cancelarProceso() {
        aceptar = true;
        procesoSeleccionado = new Procesos();
        filtrarLovProcesos = null;
        indexParametroContable = -1;
        permitirIndexParametro = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovProceso:globalFilter");
        context.execute("lovProceso.clearFilters()");
        context.execute("ProcesoDialogo.hide()");
    }

    public void actualizarEmpleadoDesde() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            parametroContableActual.setCodigoempleadodesde(empleadoSeleccionado.getCodigoempleado());
            indexParametroContable = -1;
            if (guardado == true) {
                guardado = false;
            }
            modificacionParametro = true;
            cambiosParametro = true;
            context.update("form:ACEPTAR");
            context.update("form:parametroEmplDesde");
        }
        if (tipoActualizacion == 1) {
            nuevoParametroContable.setCodigoempleadodesde(empleadoSeleccionado.getCodigoempleado());
            context.update("formularioDialogos:nuevaProcesoEmplDesde");
        }
        empleadoSeleccionado = new Empleados();
        filtrarLovEmpleados = null;
        aceptar = true;/*
         context.update("formEmplDialogo:EmpleadoDesdeDialogo");
         context.update("formEmplDialogo:lovEmpleadoDesde");
         context.update("formEmplDialogo:aceptarED");*/

        context.reset("formEmplDialogo:lovEmpleadoDesde:globalFilter");
        context.execute("lovEmpleadoDesde.clearFilters()");
        context.execute("EmpleadoDesdeDialogo.hide()");
    }

    public void cancelarEmpleadoDesde() {
        aceptar = true;
        empleadoSeleccionado = new Empleados();
        filtrarLovEmpleados = null;
        indexParametroContable = -1;
        permitirIndexParametro = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formEmplDialogo:lovEmpleadoDesde:globalFilter");
        context.execute("lovEmpleadoDesde.clearFilters()");
        context.execute("EmpleadoDesdeDialogo.hide()");
    }

    public void actualizarEmpleadoHasta() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            parametroContableActual.setCodigoempleadohasta(empleadoSeleccionado.getCodigoempleado());
            indexParametroContable = -1;
            if (guardado == true) {
                guardado = false;
            }
            modificacionParametro = true;
            cambiosParametro = true;
            context.update("form:ACEPTAR");
            context.update("form:parametroEmplHasta");
        }
        if (tipoActualizacion == 1) {
            nuevoParametroContable.setCodigoempleadohasta(empleadoSeleccionado.getCodigoempleado());
            context.update("formularioDialogos:nuevaProcesoEmplHasta");
        }
        empleadoSeleccionado = new Empleados();
        filtrarLovEmpleados = null;
        aceptar = true;/*
         context.update("formEmplDialogo:EmpleadoHastaDialogo");
         context.update("formEmplDialogo:lovEmpleadoHasta");
         context.update("formEmplDialogo:aceptarEH");*/

        context.reset("formEmplDialogo:lovEmpleadoHasta:globalFilter");
        context.execute("lovEmpleadoHasta.clearFilters()");
        context.execute("EmpleadoHastaDialogo.hide()");
    }

    public void cancelarEmpleadoHasta() {
        aceptar = true;
        empleadoSeleccionado = new Empleados();
        filtrarLovEmpleados = null;
        indexParametroContable = -1;
        permitirIndexParametro = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formEmplDialogo:lovEmpleadoHasta:globalFilter");
        context.execute("lovEmpleadoHasta.clearFilters()");
        context.execute("EmpleadoHastaDialogo.hide()");
    }

    public void listaValoresBoton() {
        if (indexParametroContable >= 0) {
            RequestContext context = RequestContext.getCurrentInstance();
            if (indexParametroContable == 0) {
                context.update("form:EmpresaDialogo");
                context.execute("EmpresaDialogo.show()");
            }
            if (indexParametroContable == 2) {
                context.update("form:ProcesoDialogo");
                context.execute("ProcesoDialogo.show()");
            }
            if (indexParametroContable == 5) {
                context.update("formEmplDialogo:EmpleadoDesdeDialogo");
                context.execute("EmpleadoDesdeDialogo.show()");
            }
            if (indexParametroContable == 6) {
                context.update("formEmplDialogo:EmpleadoHastaDialogo");
                context.execute("EmpleadoHastaDialogo.show()");
            }
        }
    }

    public void valoresBackupAutocompletar() {
        auxParametroEmpresa = nuevoParametroContable.getEmpresaRegistro().getNombre();
        auxParametroProceso = nuevoParametroContable.getProceso().getDescripcion();
        auxParametroEmplDesde = nuevoParametroContable.getStrCodigoEmpleadoDesde();
        auxParametroEmplHasta = nuevoParametroContable.getStrCodigoEmpleadoHasta();
    }

    public void autocompletarNuevo(String procesoCambio, String confirmarCambio) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (procesoCambio.equals("EMPRESA")) {
            nuevoParametroContable.getEmpresaRegistro().setNombre(auxParametroEmpresa);
            for (int i = 0; i < lovEmpresas.size(); i++) {
                if (lovEmpresas.get(i).getNombre().startsWith(confirmarCambio.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                nuevoParametroContable.setEmpresaRegistro(lovEmpresas.get(indiceUnicoElemento));
                nuevoParametroContable.setEmpresaCodigo(lovEmpresas.get(indiceUnicoElemento).getCodigo());
                lovEmpresas.clear();
                getLovEmpresas();
                context.update("formularioDialogos:nuevaEmpresaParametro");
            } else {
                nuevoParametroContable.getEmpresaRegistro().setNombre(auxParametroEmpresa);
                context.update("formularioDialogos:nuevaEmpresaParametro");
                permitirIndexParametro = false;
                context.update("form:EmpresaDialogo");
                context.execute("EmpresaDialogo.show()");
            }
        } else if (procesoCambio.equals("PROCESO")) {
            if (!confirmarCambio.isEmpty()) {
                nuevoParametroContable.getProceso().setDescripcion(auxParametroProceso);
                for (int i = 0; i < lovProcesos.size(); i++) {
                    if (lovProcesos.get(i).getDescripcion().startsWith(confirmarCambio.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    nuevoParametroContable.setProceso(lovProcesos.get(indiceUnicoElemento));
                    lovProcesos.clear();
                    getLovProcesos();
                    context.update("formularioDialogos:nuevaProcesoParametro");
                } else {
                    nuevoParametroContable.getProceso().setDescripcion(auxParametroProceso);
                    context.update("formularioDialogos:nuevaProcesoParametro");
                    permitirIndexParametro = false;
                    context.update("form:ProcesoDialogo");
                    context.execute("ProcesoDialogo.show()");
                }
            } else {
                nuevoParametroContable.setProceso(new Procesos());
                lovProcesos.clear();
                getLovProcesos();
                context.update("formularioDialogos:nuevaProcesoParametro");
            }
        } else if (procesoCambio.equals("EMPLDESDE")) {
            if (!confirmarCambio.isEmpty()) {
                nuevoParametroContable.setStrCodigoEmpleadoDesde(auxParametroEmplDesde);
                for (int i = 0; i < lovEmpleados.size(); i++) {
                    if (lovEmpleados.get(i).getCodigoempleadoSTR().startsWith(confirmarCambio.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    nuevoParametroContable.setCodigoempleadodesde(lovEmpleados.get(indiceUnicoElemento).getCodigoempleado());
                    lovEmpleados.clear();
                    getLovEmpleados();
                    context.update("formularioDialogos:nuevaProcesoEmplDesde");
                } else {
                    nuevoParametroContable.setStrCodigoEmpleadoDesde(auxParametroEmplDesde);
                    context.update("formularioDialogos:nuevaProcesoEmplDesde");
                    permitirIndexParametro = false;
                    context.update("formEmplDialogo:EmpleadoDesdeDialogo");
                    context.execute("EmpleadoDesdeDialogo.show()");
                }
            } else {
                nuevoParametroContable.setCodigoempleadodesde(null);
                lovEmpleados.clear();
                getLovEmpleados();
                context.update("formularioDialogos:nuevaProcesoEmplDesde");
            }
        } else if (procesoCambio.equals("EMPLHASTA")) {
            if (!confirmarCambio.isEmpty()) {
                nuevoParametroContable.setStrCodigoEmpleadoHasta(auxParametroEmplHasta);
                for (int i = 0; i < lovEmpleados.size(); i++) {
                    if (lovEmpleados.get(i).getCodigoempleadoSTR().startsWith(confirmarCambio.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    nuevoParametroContable.setCodigoempleadohasta(lovEmpleados.get(indiceUnicoElemento).getCodigoempleado());
                    lovEmpleados.clear();
                    getLovEmpleados();
                    context.update("formularioDialogos:nuevaProcesoEmplHasta");
                } else {
                    nuevoParametroContable.setStrCodigoEmpleadoHasta(auxParametroEmplHasta);
                    context.update("formularioDialogos:nuevaProcesoEmplHasta");
                    permitirIndexParametro = false;
                    context.update("formEmplDialogo:EmpleadoHastaDialogo");
                    context.execute("EmpleadoHastaDialogo.show()");
                }
            } else {
                nuevoParametroContable.setCodigoempleadohasta(null);
                lovEmpleados.clear();
                getLovEmpleados();
                context.update("formularioDialogos:nuevaProcesoEmplHasta");
            }
        }
    }

    public void borrarParametroContable() {
        if (modificacionParametro == true) {
            modificacionParametro = false;
        }
        listaParametrosContables.remove(parametroContableActual);
        listParametrosContablesBorrar.add(parametroContableActual);
        parametroContableActual = null;
        getParametroContableActual();
        if (listaParametrosContables != null) {
            int tam = listaParametrosContables.size();
            if (tam == 0 || tam == 1) {
                estadoBtnAbajo = true;
                estadoBtnArriba = true;
            }
            if (tam > 1) {
                estadoBtnAbajo = false;
                estadoBtnArriba = true;
            }
        }
        activarEnviar = true;
        activarDeshacer = true;
        totalCGenerado = 0;
        totalDGenerado = 0;
        totalDInter = 0;
        totalCInter = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("form:PanelTotal");
        context.update("form:btnEnviar");
        context.update("form:btnDeshacer");
        context.update("form:PLANO");
    }

    public void modificarFechaFinalNuevoRegistro() {
        RequestContext context = RequestContext.getCurrentInstance();
        nuevoParametroContable.setFechacontabilizacion(nuevoParametroContable.getFechafinalcontabilizacion());
        context.update("formularioDialogos:nuevaFechaContaParametro");
    }

    public void agregarNuevoParametro() {
        RequestContext context = RequestContext.getCurrentInstance();
        try {
            if (nuevoParametroContable.getEmpresaRegistro().getSecuencia() != null && nuevoParametroContable.getFechafinalcontabilizacion() != null && nuevoParametroContable.getFechainicialcontabilizacion() != null) {
                boolean validar = validarFechaParametro(1);
                if (validar == true) {
                    context.execute("NuevoRegistroPC.hide()");
                    int k = 1;
                    BigInteger var = new BigInteger(String.valueOf(k));
                    nuevoParametroContable.setSecuencia(var);
                    if (nuevoParametroContable.getProceso().getSecuencia() == null) {
                        nuevoParametroContable.setProceso(new Procesos());
                    }
                    administrarInterfaseDynamicsYV.crearParametroContable(nuevoParametroContable);
                    nuevoParametroContable = new ParametrosContables();
                    activarAgregar = true;
                    activarOtros = false;
                    listaParametrosContables = null;
                    getListaParametrosContables();
                    parametroContableActual = null;
                    getParametroContableActual();
                    listaGenerados = null;
                    listaInterconDynamics = null;
                    activarEnviar = true;
                    activarDeshacer = true;
                    totalCGenerado = 0;
                    totalDGenerado = 0;
                    totalDInter = 0;
                    totalCInter = 0;
                    context.update("form:btnEnviar");
                    context.update("form:btnDeshacer");
                    context.update("form:PLANO");
                    if (banderaGenerado == 1) {
                        FacesContext c = FacesContext.getCurrentInstance();
                        altoTablaGenerada = "75";
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
                        altoTablaIntercon = "75";
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
                        interFechaVencimiento = (Column) c.getViewRoot().findComponent("form:datosIntercon:interFechaVencimiento");
                        interFechaVencimiento.setFilterStyle("display: none; visibility: hidden;");
                        interOriginalDebito = (Column) c.getViewRoot().findComponent("form:datosIntercon:interOriginalDebito");
                        interOriginalDebito.setFilterStyle("display: none; visibility: hidden;");
                        interOriginalCredito = (Column) c.getViewRoot().findComponent("form:datosIntercon:interOriginalCredito");
                        interOriginalCredito.setFilterStyle("display: none; visibility: hidden;");
                        interPreradicacion = (Column) c.getViewRoot().findComponent("form:datosIntercon:interPreradicacion");
                        interPreradicacion.setFilterStyle("display: none; visibility: hidden;");
                        interCodAlternativo = (Column) c.getViewRoot().findComponent("form:datosIntercon:interCodAlternativo");
                        interCodAlternativo.setFilterStyle("display: none; visibility: hidden;");
                        RequestContext.getCurrentInstance().update("form:datosIntercon");
                        banderaIntercon = 0;
                        filtrarListaInterconDynamics = null;
                        tipoListaIntercon = 0;
                    }
                    context.update("form:PanelTotal");
                    FacesMessage msg = new FacesMessage("Información", "Se gurdarón los datos con éxito");
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                    RequestContext.getCurrentInstance().update("form:growl");
                } else {
                    context.execute("errorFechasParametro.show()");
                }
            } else {
                context.execute("errorNewRegNull.show()");
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

    public void validarExportPDF() throws IOException {
        if (indexParametroContable >= 0) {
            exportPDF_PC();
        }
        if (indexGenerado >= 0) {
            exportPDF_G();
        }
        if (indexIntercon >= 0) {
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
        indexGenerado = -1;
    }

    public void exportPDF_G() throws IOException {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formExportar:datosGenerarExportar");
        FacesContext context = c;
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "Generados_PDF", false, false, "UTF-8", null, null);
        context.responseComplete();
        indexGenerado = -1;
    }

    public void exportPDF_I() throws IOException {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formExportar:datosInterconExportar");
        FacesContext context = c;
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "InterconDynamics_PDF", false, false, "UTF-8", null, null);
        context.responseComplete();
        indexIntercon = -1;
    }

    public void validarExportXLS() throws IOException {
        if (indexParametroContable >= 0) {
            exportXLS_PC();
        }
        if (indexGenerado >= 0) {
            exportXLS_G();
        }
        if (indexIntercon >= 0) {
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
        indexParametroContable = -1;
    }

    public void exportXLS_G() throws IOException {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formExportar:datosGenerarExportar");
        FacesContext context = c;
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "Generados_XLS", false, false, "UTF-8", null, null);
        context.responseComplete();
        indexGenerado = -1;
    }

    public void exportXLS_I() throws IOException {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formExportar:datosInterconExportar");
        FacesContext context = c;
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "InterconDynamics_XLS", false, false, "UTF-8", null, null);
        context.responseComplete();
        indexIntercon = -1;
    }

    public String validarExportXML() {
        String tabla = "";
        if (indexParametroContable >= 0) {
            tabla = ":formExportar:datosParametroExportar";
        }
        if (indexGenerado >= 0) {
            tabla = ":formExportar:datosGenerarExportar";
        }
        if (indexIntercon >= 0) {
            tabla = ":formExportar:datosInterconExportar";
        }
        return tabla;
    }

    public String validarNombreExportXML() {
        String nombre = "";
        if (indexParametroContable >= 0) {
            nombre = "ParametrosContables_XML";
        }
        if (indexGenerado >= 0) {
            nombre = "Generados_XML";
        }
        if (indexIntercon >= 0) {
            nombre = "InterconDynamics_XML";
        }
        return nombre;
    }

    public void eventoFiltrar() {
        if (indexGenerado >= 0) {
            if (tipoListaGenerada == 0) {
                tipoListaGenerada = 1;
            }
        }
        if (indexIntercon >= 0) {
            if (tipoListaIntercon == 0) {
                tipoListaIntercon = 1;
            }
        }
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (indexGenerado >= 0) {
            if (banderaGenerado == 0) {
                altoTablaGenerada = "53";
                genProceso = (Column) c.getViewRoot().findComponent("form:datosGenerados:genProceso");
                genProceso.setFilterStyle("width: 90px");
                genEmpleado = (Column) c.getViewRoot().findComponent("form:datosGenerados:genEmpleado");
                genEmpleado.setFilterStyle("width: 90px");
                genCntCredito = (Column) c.getViewRoot().findComponent("form:datosGenerados:genCntCredito");
                genCntCredito.setFilterStyle("width: 90px");
                genCntDebito = (Column) c.getViewRoot().findComponent("form:datosGenerados:genCntDebito");
                genCntDebito.setFilterStyle("width: 90px");
                genTercero = (Column) c.getViewRoot().findComponent("form:datosGenerados:genTercero");
                genTercero.setFilterStyle("width: 90px");
                genValor = (Column) c.getViewRoot().findComponent("form:datosGenerados:genValor");
                genValor.setFilterStyle("width: 90px");
                genConcepto = (Column) c.getViewRoot().findComponent("form:datosGenerados:genConcepto");
                genConcepto.setFilterStyle("width: 90px");
                RequestContext.getCurrentInstance().update("form:datosGenerados");
                banderaGenerado = 1;
            } else if (banderaGenerado == 1) {
                altoTablaGenerada = "75";
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
        }
        if (indexIntercon >= 0) {
            if (banderaIntercon == 0) {
                altoTablaIntercon = "53";
                interEmpleado = (Column) c.getViewRoot().findComponent("form:datosIntercon:interEmpleado");
                interEmpleado.setFilterStyle("width: 90px");
                interTercero = (Column) c.getViewRoot().findComponent("form:datosIntercon:interTercero");
                interTercero.setFilterStyle("width: 90px");
                interCuenta = (Column) c.getViewRoot().findComponent("form:datosIntercon:interCuenta");
                interCuenta.setFilterStyle("width: 90px");
                interDebito = (Column) c.getViewRoot().findComponent("form:datosIntercon:interDebito");
                interDebito.setFilterStyle("width: 90px");
                interCredito = (Column) c.getViewRoot().findComponent("form:datosIntercon:interCredito");
                interCredito.setFilterStyle("width: 90px");
                interConcepto = (Column) c.getViewRoot().findComponent("form:datosIntercon:interConcepto");
                interConcepto.setFilterStyle("width: 90px");
                interCentroCosto = (Column) c.getViewRoot().findComponent("form:datosIntercon:interCentroCosto");
                interCentroCosto.setFilterStyle("width: 90px");
                interFechaVencimiento = (Column) c.getViewRoot().findComponent("form:datosIntercon:interFechaVencimiento");
                interFechaVencimiento.setFilterStyle("width: 90px");
                interOriginalDebito = (Column) c.getViewRoot().findComponent("form:datosIntercon:interOriginalDebito");
                interOriginalDebito.setFilterStyle("width: 90px");
                interOriginalCredito = (Column) c.getViewRoot().findComponent("form:datosIntercon:interOriginalCredito");
                interOriginalCredito.setFilterStyle("width: 90px");
                interPreradicacion = (Column) c.getViewRoot().findComponent("form:datosIntercon:interPreradicacion");
                interPreradicacion.setFilterStyle("width: 90px");
                interCodAlternativo = (Column) c.getViewRoot().findComponent("form:datosIntercon:interCodAlternativo");
                interCodAlternativo.setFilterStyle("width: 90px");
                RequestContext.getCurrentInstance().update("form:datosIntercon");
                banderaIntercon = 1;
            } else if (banderaIntercon == 1) {
                altoTablaIntercon = "75";
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
                interFechaVencimiento = (Column) c.getViewRoot().findComponent("form:datosIntercon:interFechaVencimiento");
                interFechaVencimiento.setFilterStyle("display: none; visibility: hidden;");
                interOriginalDebito = (Column) c.getViewRoot().findComponent("form:datosIntercon:interOriginalDebito");
                interOriginalDebito.setFilterStyle("display: none; visibility: hidden;");
                interOriginalCredito = (Column) c.getViewRoot().findComponent("form:datosIntercon:interOriginalCredito");
                interOriginalCredito.setFilterStyle("display: none; visibility: hidden;");
                interPreradicacion = (Column) c.getViewRoot().findComponent("form:datosIntercon:interPreradicacion");
                interPreradicacion.setFilterStyle("display: none; visibility: hidden;");
                interCodAlternativo = (Column) c.getViewRoot().findComponent("form:datosIntercon:interCodAlternativo");
                interCodAlternativo.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosIntercon");
                banderaIntercon = 0;
                filtrarListaInterconDynamics = null;
                tipoListaIntercon = 0;
            }
        }

    }

    public void validarRastro() {
        if (indexIntercon >= 0) {
            verificarRastro();
        }
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (listaInterconDynamics != null) {
            if (secRegistro != null) {
                int resultado = administrarRastros.obtenerTabla(secRegistro, "INTERCON_DYNAMICS");
                backUpSecRegistro = secRegistro;
                secRegistro = null;
                if (resultado == 1) {
                    context.execute("errorObjetosDB.show()");
                } else if (resultado == 2) {
                    context.execute("confirmarRastro.show()");
                } else if (resultado == 3) {
                    context.execute("errorRegistroRastro.show()");
                } else if (resultado == 4) {
                    context.execute("errorTablaConRastro.show()");
                } else if (resultado == 5) {
                    context.execute("errorTablaSinRastro.show()");
                }
            } else {
                context.execute("seleccionarRegistro.show()");
            }
        } else {
            if (administrarRastros.verificarHistoricosTabla("INTERCON_DYNAMICS")) {
                context.execute("confirmarRastroHistorico.show()");
            } else {
                context.execute("errorRastroHistorico.show()");
            }

        }
        indexIntercon = -1;
    }

    public ActualUsuario getActualUsuarioBD() {
        if (actualUsuarioBD == null) {
            actualUsuarioBD = administrarInterfaseDynamicsYV.obtenerActualUsuario();
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
        lovEmpresas = administrarInterfaseDynamicsYV.lovEmpresas();
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
        lovProcesos = administrarInterfaseDynamicsYV.lovProcesos();
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
        getLovEmpresas();
        if (lovEmpresas != null) {
            infoRegistroEmpresa = "Cantidad de registros : " + lovEmpresas.size();
        } else {
            infoRegistroEmpresa = "Cantidad de registros : 0";
        }
        return infoRegistroEmpresa;
    }

    public void setInfoRegistroEmpresa(String infoRegistroEmpresa) {
        this.infoRegistroEmpresa = infoRegistroEmpresa;
    }

    public String getInfoRegistroProceso() {
        getLovProcesos();
        if (lovProcesos != null) {
            infoRegistroProceso = "Cantidad de registros : " + lovProcesos.size();
        } else {
            infoRegistroProceso = "Cantidad de registros : 0";
        }
        return infoRegistroProceso;
    }

    public void setInfoRegistroProceso(String infoRegistroProceso) {
        this.infoRegistroProceso = infoRegistroProceso;
    }

    public List<ParametrosContables> getListaParametrosContables() {
        if (listaParametrosContables == null) {
            getActualUsuarioBD();
            if (actualUsuarioBD.getSecuencia() != null) {
                listaParametrosContables = administrarInterfaseDynamicsYV.obtenerParametrosContablesUsuarioBD(actualUsuarioBD.getAlias());
            }
            if (listaParametrosContables != null) {
                int tam = listaParametrosContables.size();
                if (tam > 0) {
                    registroActual = 0;
                }
                if (tam == 0 || tam == 1) {
                    estadoBtnAbajo = true;
                    estadoBtnArriba = true;
                }
                if (tam > 1) {
                    estadoBtnAbajo = false;
                    estadoBtnArriba = true;
                }
                if (tam == 0) {
                    activarAgregar = false;
                    activarOtros = true;
                } else {
                    activarAgregar = true;
                    activarOtros = false;
                }
            }
        }
        return listaParametrosContables;
    }

    public void setListaParametrosContables(List<ParametrosContables> listaParametrosContables) {
        this.listaParametrosContables = listaParametrosContables;
    }

    public boolean isEstadoBtnArriba() {
        return estadoBtnArriba;
    }

    public void setEstadoBtnArriba(boolean estadoBtnArriba) {
        this.estadoBtnArriba = estadoBtnArriba;
    }

    public boolean isEstadoBtnAbajo() {
        return estadoBtnAbajo;
    }

    public void setEstadoBtnAbajo(boolean estadoBtnAbajo) {
        this.estadoBtnAbajo = estadoBtnAbajo;
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
        getListaGenerados();
        if (listaGenerados != null) {
            if (listaGenerados.size() > 0) {
                generadoTablaSeleccionado = listaGenerados.get(0);
            }
        } else {
            generadoTablaSeleccionado = new SolucionesNodos();
        }
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

    public List<InterconDynamics> getListaInterconDynamics() {
        return listaInterconDynamics;
    }

    public void setListaInterconDynamics(List<InterconDynamics> setListaInterconDynamics) {
        this.listaInterconDynamics = setListaInterconDynamics;
    }

    public List<InterconDynamics> getFiltrarListaInterconDynamics() {
        return filtrarListaInterconDynamics;
    }

    public void setFiltrarListaInterconDynamics(List<InterconDynamics> setFiltrarListaInterconDynamics) {
        this.filtrarListaInterconDynamics = setFiltrarListaInterconDynamics;
    }

    public InterconDynamics getInterconTablaSeleccionada() {
        getListaInterconDynamics();
        if (listaInterconDynamics != null) {
            if (listaInterconDynamics.size() > 0) {
                interconTablaSeleccionada = listaInterconDynamics.get(0);
            }
        } else {
            interconTablaSeleccionada = new InterconDynamics();
        }
        return interconTablaSeleccionada;
    }

    public void setInterconTablaSeleccionada(InterconDynamics interconTablaSeleccionada) {
        this.interconTablaSeleccionada = interconTablaSeleccionada;
    }

    public InterconDynamics getEditarIntercon() {
        return editarIntercon;
    }

    public void setEditarIntercon(InterconDynamics editarIntercon) {
        this.editarIntercon = editarIntercon;
    }

    public String getAltoTablaIntercon() {
        return altoTablaIntercon;
    }

    public void setAltoTablaIntercon(String altoTablaIntercon) {
        this.altoTablaIntercon = altoTablaIntercon;
    }

    public BigInteger getSecRegistro() {
        return secRegistro;
    }

    public void setSecRegistro(BigInteger secRegistro) {
        this.secRegistro = secRegistro;
    }

    public BigInteger getBackUpSecRegistro() {
        return backUpSecRegistro;
    }

    public void setBackUpSecRegistro(BigInteger backUpSecRegistro) {
        this.backUpSecRegistro = backUpSecRegistro;
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

    public int getTotalCGenerado() {
        totalCGenerado = 0;
        getListaGenerados();
        if (listaGenerados != null) {
            for (int i = 0; i < listaGenerados.size(); i++) {
                totalCGenerado = totalCGenerado + listaGenerados.get(i).getValor().intValue();
            }
        }
        return totalCGenerado;
    }

    public void setTotalCGenerado(int totalCGenerado) {
        this.totalCGenerado = totalCGenerado;
    }

    public int getTotalDGenerado() {
        totalDGenerado = 0;
        getListaGenerados();
        if (listaGenerados != null) {
            for (int i = 0; i < listaGenerados.size(); i++) {
                totalDGenerado = totalDGenerado + listaGenerados.get(i).getValor().intValue();
            }
        }
        return totalDGenerado;
    }

    public void setTotalDGenerado(int totalDGenerado) {
        this.totalDGenerado = totalDGenerado;
    }

    public int getTotalDInter() {
        totalDInter = 0;
        getListaInterconDynamics();
        if (listaInterconDynamics != null) {
            for (int i = 0; i < listaInterconDynamics.size(); i++) {
                totalDInter = totalDInter + listaInterconDynamics.get(i).getValord().intValue();
            }
        }
        return totalDInter;
    }

    public void setTotalDInter(int totalDInter) {
        this.totalDInter = totalDInter;
    }

    public int getTotalCInter() {
        totalCInter = 0;
        getListaInterconDynamics();
        if (listaInterconDynamics != null) {
            for (int i = 0; i < listaInterconDynamics.size(); i++) {
                totalCInter = totalCInter + listaInterconDynamics.get(i).getValorc().intValue();
            }
        }
        return totalCInter;
    }

    public void setTotalCInter(int totalCInter) {
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

    public List<Empleados> getLovEmpleados() {
        lovEmpleados = administrarInterfaseDynamicsYV.buscarEmpleadosEmpresa();
        return lovEmpleados;
    }

    public void setLovEmpleados(List<Empleados> lovEmpleados) {
        this.lovEmpleados = lovEmpleados;
    }

    public List<Empleados> getFiltrarLovEmpleados() {
        return filtrarLovEmpleados;
    }

    public void setFiltrarLovEmpleados(List<Empleados> filtrarLovEmpleados) {
        this.filtrarLovEmpleados = filtrarLovEmpleados;
    }

    public Empleados getEmpleadoSeleccionado() {
        return empleadoSeleccionado;
    }

    public void setEmpleadoSeleccionado(Empleados empleadoSeleccionado) {
        this.empleadoSeleccionado = empleadoSeleccionado;
    }

    public String getInfoRegistroEmpleado() {
        return infoRegistroEmpleado;
    }

    public void setInfoRegistroEmpleado(String infoRegistroEmpleado) {
        this.infoRegistroEmpleado = infoRegistroEmpleado;
    }

    public DefaultStreamedContent getDownload() {
        return download;
    }

    public void setDownload(DefaultStreamedContent download) {
        this.download = download;
    }

    public String getRutaArchivo() {
        return rutaArchivo;
    }

    public void setRutaArchivo(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

}
