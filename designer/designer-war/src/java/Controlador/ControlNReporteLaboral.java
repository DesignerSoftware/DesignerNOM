/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Cargos;
import Entidades.Empleados;
import Entidades.Empresas;
import Entidades.Inforeportes;
import Entidades.ParametrosReportes;
import InterfaceAdministrar.AdministarReportesInterface;
import InterfaceAdministrar.AdministrarInforeportesInterface;
import InterfaceAdministrar.AdministrarNReporteLaboralInterface;
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
import org.primefaces.component.column.Column;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.inputtext.InputText;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author AndresPineda
 */
@ManagedBean
@SessionScoped
public class ControlNReporteLaboral implements Serializable {

    @EJB
    AdministrarNReporteLaboralInterface administrarNReporteLaboral;
    @EJB
    AdministarReportesInterface administarReportes;
    @EJB
    AdministrarInforeportesInterface administrarInforeportes;
    //////
    private ParametrosReportes parametroDeReporte;
    private ParametrosReportes nuevoParametroReporte;
    private ParametrosReportes parametroFecha;
    private List<Inforeportes> listaIR;
    private List<Inforeportes> filtrarListInforeportesUsuario;
    private List<Inforeportes> listaIRRespaldo;
    private List<Inforeportes> lovInforeportes;
    private List<Inforeportes> filtrarLovInforeportes;
    private String reporteGenerar;
    private Inforeportes inforreporteSeleccionado;
    private Inforeportes lovReporteSeleccionado;
    private int bandera;
    private boolean aceptar;
    private Column codigoIR, reporteIR, tipoIR;
    private int casilla;
    private ParametrosReportes parametroModificacion;
    private int tipoLista;
    private int posicionReporte;
    private String requisitosReporte;
    private List<Cargos> listCargos;
    private List<Cargos> filtrarListCargos;
    private Cargos cargoSeleccionado;
    private String cargoActual, empresa;
    private boolean permitirIndex, cambiosReporte;
    private InputText empleadoDesdeParametroL, empleadoHastaParametroL, cargoParametroL, empresaParametroL;
    private List<Empresas> listEmpresas;
    private List<Empleados> listEmpleados;
    private List<Empresas> filtrarListEmpresas;
    private List<Empleados> filtrarListEmpleados;
    private Empresas empresaSeleccionada;
    private Empleados empleadoSeleccionado;
    //ALTO SCROLL TABLA
    private String altoTabla;
    private int indice;
    //EXPORTAR REPORTE
    private StreamedContent file;
    //
    private List<Inforeportes> listaInfoReportesModificados;
    //
    private Inforeportes actualInfoReporteTabla;
    //
    private String color, decoracion;
    private String color2, decoracion2;
    //
    private int casillaInforReporte;
    //
    private Date fechaDesde, fechaHasta;
    private BigDecimal emplDesde, emplHasta;
    //
    private boolean activoMostrarTodos, activoBuscarReporte, activarEnvio;
    private String infoRegistroReportes, infoRegistroCargo, infoRegistroEmpleadoDesde, infoRegistroEmpleadoHasta, infoRegistroEmpresa;
    private String infoRegistro;
    private String nombreReporte, tipoReporte;
    private String pathReporteGenerado;
//
    //VISUALIZAR REPORTE PDF
    private StreamedContent reporte;
    private String cabezeraVisor;
    private DataTable tabla;
    private String paginaAnterior = "nominaf";
    private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();
    private ExternalContext externalContext;
    private String userAgent;
    private boolean activarLov;

    public ControlNReporteLaboral() {
        activoMostrarTodos = true;
        activoBuscarReporte = false;
        activarEnvio = true;
        color = "black";
        decoracion = "none";
        color2 = "black";
        decoracion2 = "none";
        casillaInforReporte = -1;
        actualInfoReporteTabla = new Inforeportes();
        cambiosReporte = true;
        listaInfoReportesModificados = new ArrayList<>();
        altoTabla = "185";
        parametroDeReporte = null;
        listaIR = null;
        listaIRRespaldo = new ArrayList<>();
        filtrarListCargos = null;
        bandera = 0;
        aceptar = true;
        casilla = -1;
        parametroModificacion = new ParametrosReportes();
        parametroFecha = new ParametrosReportes();
        tipoLista = 0;
        reporteGenerar = "";
        requisitosReporte = "";
        posicionReporte = -1;
        listCargos = null;
        permitirIndex = true;
        listEmpleados = null;
        listEmpresas = null;
        listaIR = null;
        empresaSeleccionada = new Empresas();
        empleadoSeleccionado = new Empleados();
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
        String pagActual = "nreportelaboral";
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

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            externalContext = x.getExternalContext();
            userAgent = externalContext.getRequestHeaderMap().get("User-Agent");
            administrarNReporteLaboral.obtenerConexion(ses.getId());
            administarReportes.obtenerConexion(ses.getId());
            administrarInforeportes.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getMessage());
        }
    }

    public void iniciarPagina() {
        activoMostrarTodos = true;
        activoBuscarReporte = false;
        listaIR = null;
        getListaIR();
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
        if (!requisitosReporte.isEmpty()) {
            RequestContext.getCurrentInstance().update("formDialogos:requisitosReporte");
            RequestContext.getCurrentInstance().execute("PF('requisitosReporte').show()");
        }
    }

    public void cancelarRequisitosReporte() {
        requisitosReporte = "";
    }

    public void activarEnvioCorreo() {
        if (inforreporteSeleccionado != null) {
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
            if (!empleadoDesdeParametroL.getStyle().contains(" color: red;")) {
                empleadoDesdeParametroL.setStyle(empleadoDesdeParametroL.getStyle() + " color: red;");
            }
        } else {
            try {
                if (empleadoDesdeParametroL.getStyle().contains(" color: red;")) {

                    empleadoDesdeParametroL.setStyle(empleadoDesdeParametroL.getStyle().replace(" color: red;", ""));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        RequestContext.getCurrentInstance().update("formParametros:empleadoDesdeParametroL");

        if (inforreporteSeleccionado.getEmhasta().equals("SI")) {
            empleadoHastaParametroL = (InputText) FacesContext.getCurrentInstance().getViewRoot().findComponent("formParametros:empleadoHastaParametroL");
            empleadoHastaParametroL.setStyle(empleadoHastaParametroL.getStyle() + "color: red;");
            RequestContext.getCurrentInstance().update("formParametros:empleadoHastaParametroL");
        }
        RequestContext.getCurrentInstance().update("formParametros");
    }

    public void dispararDialogoGuardarCambios() {
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
    }

    public void guardarYSalir() {
        guardarCambios();
        salir();
    }

    public void limpiarListasValor() {
    }

    public void guardarCambios() {
        try {
            if (cambiosReporte == false) {
                if (parametroDeReporte.getUsuario() != null) {
                    if (parametroDeReporte.getCodigoempleadodesde() == null) {
                        parametroDeReporte.setCodigoempleadodesde(null);
                    }
                    if (parametroDeReporte.getCodigoempleadohasta() == null) {
                        parametroDeReporte.setCodigoempleadohasta(null);
                    }

                    if (parametroDeReporte.getCargo().getSecuencia() == null) {
                        parametroDeReporte.setCargo(null);
                    }

                    if (parametroDeReporte.getEmpresa().getSecuencia() == null) {
                        parametroDeReporte.setEmpresa(null);
                    }
                    administrarNReporteLaboral.modificarParametrosReportes(parametroDeReporte);
                }

                if (!listaInfoReportesModificados.isEmpty()) {
                    administrarNReporteLaboral.guardarCambiosInfoReportes(listaInfoReportesModificados);
                    listaInfoReportesModificados.clear();
                }
                cambiosReporte = true;
                FacesMessage msg = new FacesMessage("Información", "Los datos se guardaron con Éxito.");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                RequestContext.getCurrentInstance().update("form:growl");
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        } catch (Exception e) {
            System.out.println("Error en guardar Cambios Controlador : " + e.toString());
            FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado, intente nuevamente");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }
    }

    public void posicionCelda(int i) {
        casilla = i;
        if (permitirIndex) {
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
                    cargoActual = parametroDeReporte.getCargo().getNombre();
                    break;
                case 6:
                    habilitarBotonLov();
                    empresa = parametroDeReporte.getEmpresa().getNombre();
                    break;
                default:
                    break;
            }
            casillaInforReporte = -1;
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
                RequestContext.getCurrentInstance().update("formDialogos:cargo");
                RequestContext.getCurrentInstance().execute("PF('cargo').show()");
            }
            if (casilla == 6) {
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

    public void activarAceptar() {
        aceptar = false;
    }

    public void generarReporte(Inforeportes reporte) {
        try {
            guardarCambios();
            inforreporteSeleccionado = reporte;
            seleccionRegistro();
            generarDocumentoReporte();
        } catch (Exception e) {
            System.out.println("error en generarReporte : " + e.getMessage());
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
                System.out.println("Reporte Seleccionado es nulo");
            }
        } catch (Exception e) {
            System.out.println("error generarDocumentoReporte() " + e.getMessage());
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
                        System.out.println("pathReporteGenerado : " + pathReporteGenerado);
                        fis = new FileInputStream(new File(pathReporteGenerado));
                        System.out.println("fis : " + fis);
                        reporte = new DefaultStreamedContent(fis, "application/pdf");
                    } catch (FileNotFoundException ex) {
                        RequestContext.getCurrentInstance().execute("PF('generandoReporte').hide()");
                        RequestContext.getCurrentInstance().update("formDialogos:errorGenerandoReporte");
                        RequestContext.getCurrentInstance().execute("PF('errorGenerandoReporte').show()");
                        reporte = null;
                    }
                    if (reporte != null) {
                        if (inforreporteSeleccionado != null) {
                            System.out.println("User Agent" + userAgent);
                            if (userAgent.toUpperCase().contains("Mobile".toUpperCase()) || userAgent.toUpperCase().contains("Tablet".toUpperCase()) || userAgent.toUpperCase().contains("Android".toUpperCase())) {
                                context.update("formDialogos:descargarReporte");
                                context.execute("PF('descargarReporte').show();");
                            } else {
                                RequestContext.getCurrentInstance().update("formDialogos:verReportePDF");
                                RequestContext.getCurrentInstance().execute("PF('verReportePDF').show()");
                            }
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
            RequestContext.getCurrentInstance().execute("PF('generandoReporte').hide()");
            RequestContext.getCurrentInstance().update("formDialogos:errorGenerandoReporte");
            RequestContext.getCurrentInstance().execute("PF('errorGenerandoReporte').show()");
        }
    }

    public void mostrarDialogoGenerarReporte() {
        defaultPropiedadesParametrosReporte();
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
            if (cambiosReporte == true) {
                lovInforeportes = null;
                cargarLovReportes();
                contarRegistros();
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("formDialogos:ReportesDialogo");
                RequestContext.getCurrentInstance().execute("PF('ReportesDialogo').show()");
            } else {
                RequestContext.getCurrentInstance().execute("PF('confirmarGuardarSinSalida').show()");
            }
        } catch (Exception e) {
            System.out.println("Error mostrarDialogoBuscarReporte : " + e.toString());
        }
    }

    public void salir() {
        if (bandera == 1) {
            cerrarFiltrado();
        }
        listCargos = null;
        listEmpleados = null;
        listEmpresas = null;
        listaIR = null;
        parametroDeReporte = null;
        parametroModificacion = null;
        casilla = -1;
        cargoSeleccionado = null;
        listaInfoReportesModificados.clear();
        cambiosReporte = true;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        navegar("atras");
    }

    public void cancelarYSalir() {
        cancelarModificaciones();
        salir();
    }

    public void cancelarModificaciones() {
        if (bandera == 1) {
            cerrarFiltrado();
        }
        defaultPropiedadesParametrosReporte();
        listaIR = null;
        casilla = -1;
        listaInfoReportesModificados.clear();
        cambiosReporte = true;
        getParametroDeReporte();
        getListaIR();
        activoMostrarTodos = true;
        activoBuscarReporte = false;
        parametroDeReporte = null;
        parametroModificacion = null;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("formParametros:fechaDesdeParametroL");
        RequestContext.getCurrentInstance().update("formParametros:empleadoDesdeParametroL");
        RequestContext.getCurrentInstance().update("formParametros:fechaHastaParametroL");
        RequestContext.getCurrentInstance().update("formParametros:empleadoHastaParametroL");
        RequestContext.getCurrentInstance().update("formParametros:cargoParametroL");
        RequestContext.getCurrentInstance().update("formParametros:tipoPersonalParametroL");
        RequestContext.getCurrentInstance().update("formParametros:empresaParametroL");
        RequestContext.getCurrentInstance().update("form:MOSTRARTODOS");
        RequestContext.getCurrentInstance().update("form:BUSCARREPORTE");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("form:ENVIOCORREO");
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
        RequestContext.getCurrentInstance().update("form:reportesLaboral");
    }

    public void modificacionTipoReporte(Inforeportes reporte) {
        inforreporteSeleccionado = reporte;
        cambiosReporte = false;
        if (listaInfoReportesModificados.isEmpty()) {
            listaInfoReportesModificados.add(inforreporteSeleccionado);
        } else if (!listaInfoReportesModificados.contains(inforreporteSeleccionado)) {
            listaInfoReportesModificados.add(inforreporteSeleccionado);
        } else {
            int posicion = listaInfoReportesModificados.indexOf(inforreporteSeleccionado);
            listaInfoReportesModificados.set(posicion, inforreporteSeleccionado);
        }
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void seleccionarTipoR(String tipo, Inforeportes reporte) {
        System.out.println("inforreporteSeleccionado: " + inforreporteSeleccionado);
        System.out.println("reporte: " + reporte);
        System.out.println("tipo: " + tipo);
        switch (tipo) {
            case "PDF":
                inforreporteSeleccionado.setTipo("PDF");
                break;
            case "XHTML":
                inforreporteSeleccionado.setTipo("XHTML");
                break;
            case "XLS":
                inforreporteSeleccionado.setTipo("XLS");
                break;
            case "XLSX":
                inforreporteSeleccionado.setTipo("XLSX");
                break;
            case "CSV":
                inforreporteSeleccionado.setTipo("CSV");
                break;
            default:
                break;
        }
        if (listaInfoReportesModificados.isEmpty()) {
            listaInfoReportesModificados.add(inforreporteSeleccionado);
        } else if (!listaInfoReportesModificados.contains(inforreporteSeleccionado)) {
            listaInfoReportesModificados.add(inforreporteSeleccionado);
        }
        cambiosReporte = false;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void actualizarSeleccionInforeporte() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (bandera == 1) {
            cerrarFiltrado();
        }
        defaultPropiedadesParametrosReporte();
        listaIR.clear();
        listaIR.add(lovReporteSeleccionado);
        filtrarListInforeportesUsuario = null;
        filtrarLovInforeportes = null;
        aceptar = true;
        activoBuscarReporte = true;
        activoMostrarTodos = false;
        inforreporteSeleccionado = lovReporteSeleccionado;
        lovReporteSeleccionado = null;
        RequestContext.getCurrentInstance().update("form:MOSTRARTODOS");
        RequestContext.getCurrentInstance().update("form:BUSCARREPORTE");
        context.reset("formDialogos:lovReportesDialogo:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovReportesDialogo').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('ReportesDialogo').hide()");
        RequestContext.getCurrentInstance().update("form:reportesLaboral");
        contarRegistros();
    }

    public void cancelarSeleccionInforeporte() {
        filtrarListInforeportesUsuario = null;
        lovReporteSeleccionado = null;
        aceptar = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formDialogos:lovReportesDialogo:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovReportesDialogo').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('ReportesDialogo').hide()");
    }

    public void cargarLovCargos() {
        if (listCargos == null) {
            listCargos = administrarNReporteLaboral.listCargos();
        }
    }

    public void cargarLovEmpresas() {
        if (listEmpresas == null) {
            listEmpresas = administrarNReporteLaboral.listEmpresas();
        }
    }

    public void cargarLovEmpleados() {
        if (listEmpleados == null) {
            listEmpleados = administrarNReporteLaboral.listEmpleados();
        }
    }

    public void listaValoresBoton() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (casilla == 2) {
            listEmpleados = null;
            cargarLovEmpleados();
            RequestContext.getCurrentInstance().update("formDialogos:EmpleadoDesdeDialogo");
            RequestContext.getCurrentInstance().execute("PF('EmpleadoDesdeDialogo').show()");
        }
        if (casilla == 4) {
            listEmpleados = null;
            cargarLovEmpleados();
            RequestContext.getCurrentInstance().update("formDialogos:EmpleadoHastaDialogo");
            RequestContext.getCurrentInstance().execute("PF('EmpleadoHastaDialogo').show()");
        }
        if (casilla == 5) {
            listCargos = null;
            cargarLovCargos();
            RequestContext.getCurrentInstance().update("formDialogos:CargoDialogo");
            RequestContext.getCurrentInstance().execute("PF('CargoDialogo').show()");
        }
        if (casilla == 6) {
            listEmpresas = null;
            cargarLovEmpresas();
            RequestContext.getCurrentInstance().update("formDialogos:EmpresaDialogo");
            RequestContext.getCurrentInstance().execute("PF('EmpresaDialogo').show()");
        }
    }

    public void mostrarDialogosListas(int celda) {
        RequestContext context = RequestContext.getCurrentInstance();
        casilla = celda;
        if (casilla == 2) {
            listEmpleados = null;
            cargarLovEmpleados();
            contarRegistrosEmpeladoD();
            RequestContext.getCurrentInstance().update("formDialogos:EmpleadoDesdeDialogo");
            RequestContext.getCurrentInstance().execute("PF('EmpleadoDesdeDialogo').show()");
        }
        if (casilla == 4) {
            listEmpleados = null;
            cargarLovEmpleados();
            contarRegistrosEmpeladoH();
            RequestContext.getCurrentInstance().update("formDialogos:EmpleadoHastaDialogo");
            RequestContext.getCurrentInstance().execute("PF('EmpleadoHastaDialogo').show()");
        }
        if (casilla == 5) {
            listCargos = null;
            cargarLovCargos();
            contarRegistrosCargo();
            RequestContext.getCurrentInstance().update("formDialogos:CargoDialogo");
            RequestContext.getCurrentInstance().execute("PF('CargoDialogo').show()");
        }
        if (casilla == 6) {
            listEmpresas = null;
            cargarLovEmpresas();
            contarRegistrosEmpresa();
            RequestContext.getCurrentInstance().update("formDialogos:EmpresaDialogo");
            RequestContext.getCurrentInstance().execute("PF('EmpresaDialogo').show()");
        }
    }

    public void actualizarSeleccionCargo() {
        permitirIndex = true;
        parametroDeReporte.setCargo(cargoSeleccionado);
        parametroModificacion = parametroDeReporte;
        filtrarListCargos = null;
        cargoSeleccionado = null;
        cambiosReporte = false;
        aceptar = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formDialogos:lovCargoDialogo:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovCargoDialogo').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('CargoDialogo').hide()");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("formParametros:cargoParametroL");
    }

    public void cancelarSeleccionCargo() {
        filtrarListCargos = null;
        cargoSeleccionado = null;
        aceptar = true;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formDialogos:lovCargoDialogo:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovCargoDialogo').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('CargoDialogo').hide()");
    }

    public void actualizarEmpresa() {
        permitirIndex = true;
        parametroDeReporte.setEmpresa(empresaSeleccionada);
        parametroModificacion = parametroDeReporte;
        empresaSeleccionada = null;
        aceptar = true;
        filtrarListEmpresas = null;
        cambiosReporte = false;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formDialogos:lovEmpresa:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovEmpresa').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('EmpresaDialogo').hide()");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("formParametros:empresaParametroL");
    }

    public void cancelarEmpresa() {
        empresaSeleccionada = null;
        aceptar = true;
        filtrarListEmpresas = null;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formDialogos:lovEmpresa:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovEmpresa').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('EmpresaDialogo').hide()");
    }

    public void actualizarEmpleadoDesde() {
        parametroDeReporte.setCodigoempleadodesde(empleadoSeleccionado.getCodigoempleado());
        parametroModificacion = parametroDeReporte;
        empleadoSeleccionado = null;
        filtrarListEmpleados = null;
        aceptar = true;
        permitirIndex = true;
        cambiosReporte = false;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formDialogos:lovEmpleadoDesde:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovEmpleadoDesde').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('EmpleadoDesdeDialogo').hide()");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("formParametros:empleadoDesdeParametroL");

    }

    public void cancelarEmpleadoDesde() {
        empleadoSeleccionado = null;
        aceptar = true;
        filtrarListEmpleados = null;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formDialogos:lovEmpleadoDesde:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovEmpleadoDesde').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('EmpleadoDesdeDialogo').hide()");
    }

    public void actualizarEmpleadoHasta() {
        parametroDeReporte.setCodigoempleadohasta(empleadoSeleccionado.getCodigoempleado());
        parametroModificacion = parametroDeReporte;
        empleadoSeleccionado = null;
        filtrarListEmpleados = null;
        cambiosReporte = false;
        aceptar = true;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formDialogos:lovEmpleadoHasta:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovEmpleadoHasta').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('EmpleadoHastaDialogo').hide()");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("formParametros:empleadoHastaParametroL");
    }

    public void cancelarEmpleadoHasta() {
        empleadoSeleccionado = null;
        aceptar = true;
        filtrarListEmpleados = null;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formDialogos:lovEmpleadoHasta:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovEmpleadoHasta').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('EmpleadoHastaDialogo').hide()");
    }

    public void autocompletarGeneral(String campoConfirmar, String valorConfirmar) {
        RequestContext context = RequestContext.getCurrentInstance();
        int indiceUnicoElemento = -1;
        int coincidencias = 0;
        if (campoConfirmar.equalsIgnoreCase("EMPRESA")) {
            if (!valorConfirmar.isEmpty()) {
                parametroDeReporte.getEmpresa().setNombre(empresa);
                for (int i = 0; i < listEmpresas.size(); i++) {
                    if (listEmpresas.get(i).getNombre().startsWith(valorConfirmar)) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    parametroDeReporte.setEmpresa(listEmpresas.get(indiceUnicoElemento));
                    parametroModificacion = parametroDeReporte;
                    listEmpresas.clear();
                    getListEmpresas();
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
                listEmpresas.clear();
                getListEmpresas();
                cambiosReporte = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        }
        if (campoConfirmar.equalsIgnoreCase("CARGO")) {
            if (!valorConfirmar.isEmpty()) {
                parametroDeReporte.getCargo().setNombre(cargoActual);
                for (int i = 0; i < listCargos.size(); i++) {
                    if (listCargos.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    parametroDeReporte.setCargo(listCargos.get(indiceUnicoElemento));
                    parametroModificacion = parametroDeReporte;
                    listCargos.clear();
                    getListCargos();
                    cambiosReporte = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                } else {
                    permitirIndex = false;
                    RequestContext.getCurrentInstance().update("formDialogos:CargoDialogo");
                    RequestContext.getCurrentInstance().execute("PF('CargoDialogo').show()");
                }
            } else {
                parametroDeReporte.setCargo(new Cargos());
                parametroModificacion = parametroDeReporte;
                listCargos.clear();
                getListCargos();
                cambiosReporte = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        }
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

    public void mostrarTodos() {
        if (cambiosReporte == true) {
            defaultPropiedadesParametrosReporte();
            listaIR.clear();
            for (int i = 0; i < lovInforeportes.size(); i++) {
                listaIR.add(lovInforeportes.get(i));
            }
            inforreporteSeleccionado = null;
            contarRegistros();
            activoBuscarReporte = false;
            activoMostrarTodos = true;
            RequestContext.getCurrentInstance().update("form:MOSTRARTODOS");
            RequestContext.getCurrentInstance().update("form:BUSCARREPORTE");
            RequestContext.getCurrentInstance().update("form:reportesLaboral");
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardarSinSalida').show()");
        }
    }

    public void eventoFiltrar() {
        contarRegistros();
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            altoTabla = "165";
            codigoIR = (Column) c.getViewRoot().findComponent("form:reportesLaboral:codigoIR");
            codigoIR.setFilterStyle("width: 85% !important;");
            reporteIR = (Column) c.getViewRoot().findComponent("form:reportesLaboral:reporteIR");
            reporteIR.setFilterStyle("width: 85% !important;");
            tipoIR = (Column) c.getViewRoot().findComponent("form:reportesLaboral:tipoIR");
            tipoIR.setFilterStyle("width: 85% !important;");
            RequestContext.getCurrentInstance().update("form:reportesLaboral");
            tipoLista = 1;
            bandera = 1;
        } else if (bandera == 1) {
            cerrarFiltrado();
        }

    }

    private void cerrarFiltrado() {
        FacesContext c = FacesContext.getCurrentInstance();
        altoTabla = "185";
        defaultPropiedadesParametrosReporte();
        codigoIR = (Column) c.getViewRoot().findComponent("form:reportesLaboral:codigoIR");
        codigoIR.setFilterStyle("display: none; visibility: hidden;");
        reporteIR = (Column) c.getViewRoot().findComponent("form:reportesLaboral:reporteIR");
        reporteIR.setFilterStyle("display: none; visibility: hidden;");
        tipoIR = (Column) c.getViewRoot().findComponent("form:reportesLaboral:tipoIR");
        tipoIR.setFilterStyle("display: none; visibility: hidden;");
        RequestContext.getCurrentInstance().update("form:reportesLaboral");
        bandera = 0;
        filtrarListInforeportesUsuario = null;
        tipoLista = 0;
    }

    public void defaultPropiedadesParametrosReporte() {
        color = "black";
        decoracion = "none";
        color2 = "black";
        decoracion2 = "none";
    }

    public void modificarParametroInforme() {
        if (parametroDeReporte.getCodigoempleadodesde() != null && parametroDeReporte.getCodigoempleadohasta() != null
                && parametroDeReporte.getFechadesde() != null && parametroDeReporte.getFechahasta() != null) {
            if (parametroDeReporte.getFechadesde() != null && parametroDeReporte.getFechahasta() != null) {
                if (parametroDeReporte.getFechadesde().before(parametroDeReporte.getFechahasta())) {
                    parametroModificacion = parametroDeReporte;
                    cambiosReporte = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                } else {
                    cambiosReporte = true;
                }
            }
        } else {
            System.out.println("empleadoDesde, empleado hasta, fechadesde, fechahasta estan nulas");
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
                RequestContext.getCurrentInstance().update("formDialogos:errorGenerandoReporte");
                RequestContext.getCurrentInstance().execute("PF('errorGenerandoReporte').show()");
            }
        } catch (Exception e) {
            System.out.println("error en exportarReporte() " + e.getMessage());
            RequestContext.getCurrentInstance().update("formDialogos:errorGenerandoReporte");
            RequestContext.getCurrentInstance().execute("PF('errorGenerandoReporte').show()");
        }

    }

    public void cargarLovReportes() {
        if (lovInforeportes == null) {
            lovInforeportes = administrarNReporteLaboral.listInforeportesUsuario();
        }
    }

    public void cancelarReporte() {
        administarReportes.cancelarReporte();
    }

    public void reiniciarStreamedContent() {
        reporte = null;
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

    public void contarRegistrosCargo() {
        RequestContext.getCurrentInstance().update("formDialogos:infoRegistroCargo");
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

    //GET & SET
    public ParametrosReportes getParametroDeReporte() {
        try {
            if (parametroDeReporte == null) {
                parametroDeReporte = new ParametrosReportes();
                parametroDeReporte = administrarNReporteLaboral.parametrosDeReporte();
            }
            if (parametroDeReporte.getCargo() == null) {
                parametroDeReporte.setCargo(new Cargos());
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

    public void setParametroDeReporte(ParametrosReportes parametroDeReporte) {
        this.parametroDeReporte = parametroDeReporte;
    }

    public List<Inforeportes> getListaIR() {
        if (listaIR == null) {
            listaIR = administrarNReporteLaboral.listInforeportesUsuario();
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

    public ParametrosReportes getNuevoParametroReporte() {
        return nuevoParametroReporte;
    }

    public void setNuevoParametroReporte(ParametrosReportes nuevoParametroReporte) {
        this.nuevoParametroReporte = nuevoParametroReporte;
    }

    public ParametrosReportes getParametroModificacion() {
        return parametroModificacion;
    }

    public void setParametroModificacion(ParametrosReportes parametroModificacion) {
        this.parametroModificacion = parametroModificacion;
    }

    public List<Inforeportes> getListaIRRespaldo() {
        return listaIRRespaldo;
    }

    public void setListaIRRespaldo(List<Inforeportes> listaIRRespaldo) {
        this.listaIRRespaldo = listaIRRespaldo;
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

    public List<Cargos> getListCargos() {
        return listCargos;
    }

    public void setListCargos(List<Cargos> listCargos) {
        this.listCargos = listCargos;
    }

    public List<Cargos> getFiltrarListCargos() {
        return filtrarListCargos;
    }

    public void setFiltrarListCargos(List<Cargos> filtrarListCargos) {
        this.filtrarListCargos = filtrarListCargos;
    }

    public Cargos getCargoSeleccionado() {
        return cargoSeleccionado;
    }

    public void setCargoSeleccionado(Cargos cargoSeleccionado) {
        this.cargoSeleccionado = cargoSeleccionado;
    }

    public List<Empresas> getListEmpresas() {
        return listEmpresas;
    }

    public void setListEmpresas(List<Empresas> listEmpresas) {
        this.listEmpresas = listEmpresas;
    }

    public List<Empleados> getListEmpleados() {
        return listEmpleados;
    }

    public void setListEmpleados(List<Empleados> listEmpleados) {
        this.listEmpleados = listEmpleados;
    }

    public List<Empresas> getFiltrarListEmpresas() {
        return filtrarListEmpresas;
    }

    public void setFiltrarListEmpresas(List<Empresas> filtrarListEmpresas) {
        this.filtrarListEmpresas = filtrarListEmpresas;
    }

    public List<Empleados> getFiltrarListEmpleados() {
        return filtrarListEmpleados;
    }

    public void setFiltrarListEmpleados(List<Empleados> filtrarListEmpleados) {
        this.filtrarListEmpleados = filtrarListEmpleados;
    }

    public Empresas getEmpresaSeleccionada() {
        return empresaSeleccionada;
    }

    public void setEmpresaSeleccionada(Empresas empresaSeleccionada) {
        this.empresaSeleccionada = empresaSeleccionada;
    }

    public Empleados getEmpleadoSeleccionado() {
        return empleadoSeleccionado;
    }

    public void setEmpleadoSeleccionado(Empleados empleadoSeleccionado) {
        this.empleadoSeleccionado = empleadoSeleccionado;
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

    public Inforeportes getActualInfoReporteTabla() {
        return actualInfoReporteTabla;
    }

    public void setActualInfoReporteTabla(Inforeportes actualInfoReporteTabla) {
        this.actualInfoReporteTabla = actualInfoReporteTabla;
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

    public String getInfoRegistroReportes() {
        FacesContext c = FacesContext.getCurrentInstance();
        tabla = (DataTable) c.getViewRoot().findComponent("formDialogos:lovReportesDialogo");
        infoRegistroReportes = String.valueOf(tabla.getRowCount());
        return infoRegistroReportes;
    }

    public String getInfoRegistroCargo() {
        FacesContext c = FacesContext.getCurrentInstance();
        tabla = (DataTable) c.getViewRoot().findComponent("formDialogos:lovCargoDialogo");
        infoRegistroCargo = String.valueOf(tabla.getRowCount());
        return infoRegistroCargo;
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

    public String getInfoRegistroEmpresa() {
        FacesContext c = FacesContext.getCurrentInstance();
        tabla = (DataTable) c.getViewRoot().findComponent("formDialogos:lovEmpresa");
        infoRegistroEmpresa = String.valueOf(tabla.getRowCount());
        return infoRegistroEmpresa;
    }

    public String getInfoRegistro() {
        FacesContext c = FacesContext.getCurrentInstance();
        tabla = (DataTable) c.getViewRoot().findComponent("form:reportesLaboral");
        infoRegistro = String.valueOf(tabla.getRowCount());
        return infoRegistro;
    }

    public List<Inforeportes> getLovInforeportes() {
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

    public Inforeportes getLovReporteSeleccionado() {
        return lovReporteSeleccionado;
    }

    public void setLovReporteSeleccionado(Inforeportes lovReporteSeleccionado) {
        this.lovReporteSeleccionado = lovReporteSeleccionado;
    }

    public Column getReporteIR() {
        return reporteIR;
    }

    public void setReporteIR(Column reporteIR) {
        this.reporteIR = reporteIR;
    }

    public String getCargoActual() {
        return cargoActual;
    }

    public void setCargoActual(String cargoActual) {
        this.cargoActual = cargoActual;
    }

    public InputText getCargoParametroL() {
        return cargoParametroL;
    }

    public void setCargoParametroL(InputText cargoParametroL) {
        this.cargoParametroL = cargoParametroL;
    }

    public String getNombreReporte() {
        return nombreReporte;
    }

    public void setNombreReporte(String nombreReporte) {
        this.nombreReporte = nombreReporte;
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
