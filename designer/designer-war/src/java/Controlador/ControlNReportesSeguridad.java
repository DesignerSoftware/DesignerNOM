/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Empleados;
import Entidades.Empresas;
import Entidades.Estructuras;
import Entidades.GruposConceptos;
import Entidades.Inforeportes;
import Entidades.ParametrosReportes;
import Entidades.SucursalesPila;
import Entidades.Terceros;
import Entidades.TiposTrabajadores;
import InterfaceAdministrar.AdministarReportesInterface;
import InterfaceAdministrar.AdministrarNReportesSeguridadInterface;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
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
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.fill.AsynchronousFilllListener;
import org.primefaces.component.column.Column;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.inputtext.InputText;
import org.primefaces.component.selectonemenu.SelectOneMenu;
import org.primefaces.component.selectcheckboxmenu.SelectCheckboxMenu;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author user
 */
@ManagedBean
@SessionScoped
public class ControlNReportesSeguridad implements Serializable {

    @EJB
    AdministrarNReportesSeguridadInterface administrarNReportesSeguridad;
    @EJB
    AdministarReportesInterface administarReportes;
    private ParametrosReportes parametroDeReporte;
    private ParametrosReportes nuevoParametroInforme;
    private ParametrosReportes parametroFecha;
    private List<Inforeportes> listaIR;
    private Inforeportes reporteSeleccionado;
    private Inforeportes reportetipo;
    private List<Inforeportes> filtrarListIRU;
    private String reporteGenerar;
    private int bandera;
    private boolean aceptar;
    private Column codigoIR, reporteIR;
    private SelectCheckboxMenu tipoIR;
    private int casilla;
    private ParametrosReportes parametroModificacion;
    private int posicionReporte;
    private String requisitosReporte;
    private InputText empleadoDesdeParametro, empleadoHastaParametro, estructuraParametro, sucursalParametro, empresaParametro, tipoTrabajadorParametro, terceroParametro, grupoParametro;
    private SelectOneMenu estadoParametro;
    //LOV'S
    private List<Inforeportes> lovInforeportes;
    private Inforeportes reporteSeleccionadoLOV;
    private List<Inforeportes> filtrarLovInforeportes;
    private List<Inforeportes> filtrarReportes;
    private List<Inforeportes> ListaTipos;
    private List<Empleados> lovEmpleados;
    private Empleados empleadoSeleccionado;
    private List<Empleados> filtrarLovEmpleados;
    private List<GruposConceptos> lovGruposConceptos;
    private GruposConceptos grupoCSeleccionado;
    private List<GruposConceptos> filtrarLovGruposConceptos;
    private List<Empresas> lovEmpresas;
    private Empresas empresaSeleccionada;
    private List<Empresas> filtrarLovEmpresas;
    private List<Terceros> lovTerceros;
    private Terceros terceroSeleccionado;
    private List<Terceros> filtrarLovTerceros;
    private TiposTrabajadores tipoTSeleccionado;
    private List<TiposTrabajadores> filtrarLovTiposTrabajadores;
    private List<Estructuras> lovEstructuras;
    private Estructuras estructuraSeleccionada;
    private List<Estructuras> filtrarLovEstructuras;
    private List<SucursalesPila> lovSucursales;
    private SucursalesPila sucursalSeleccionada;
    private List<SucursalesPila> filtrarLovSucursales;
    //EXPORTAR REPORTE
    private String pathReporteGenerado;
    private String nombreReporte, tipoReporte;
    private List<Inforeportes> listaInfoReportesModificados;
    private String color, decoracion;
    private String color2, decoracion2;
    private int casillaInforReporte;
    private Date fechaDesde, fechaHasta;
    private BigDecimal emplDesde, emplHasta;
    private boolean activoMostrarTodos, activoBuscarReporte, activarEnvio;
    //VISUALIZAR REPORTE PDF
    private StreamedContent reporte;
    private String cabezeraVisor;
    //Listener reporte
    private AsynchronousFilllListener asistenteReporte;
    //BANDERAS
    private boolean estadoReporte;
    private String resultadoReporte;
    private String infoRegistroEmpleadoDesde, infoRegistroEmpleadoHasta, infoRegistroEmpresa, infoRegistroTercero, infoRegistroSucursal;
    private String infoRegistroLovReportes, infoRegistro;
    private DataTable tabla;
    private int tipoLista;
    private String grupo, estructura, empresa, tipoTrabajador, tercero, proceso, sucursal, empleado;
    private String altoTabla;
    private boolean permitirIndex, cambiosReporte;
    private BigInteger auxiliar;
    private String paginaAnterior = "nominaf";
    private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();
    private ExternalContext externalContext;
    private String userAgent;
    private boolean activarLov;

    /**
     * Creates a new instance of controlNReportesPila
     */
    public ControlNReportesSeguridad() {
        activoMostrarTodos = true;
        activoBuscarReporte = false;
        activarEnvio = true;
        pathReporteGenerado = null;
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
        lovEmpleados = null;
        lovEmpresas = null;
        lovEstructuras = null;
        lovGruposConceptos = null;
        lovTerceros = null;
        lovSucursales = null;
        tipoLista = 0;

        empleadoSeleccionado = new Empleados();
        empresaSeleccionada = new Empresas();
        grupoCSeleccionado = new GruposConceptos();
        sucursalSeleccionada = new SucursalesPila();
        estructuraSeleccionada = new Estructuras();
        tipoTSeleccionado = new TiposTrabajadores();
        terceroSeleccionado = new Terceros();
        permitirIndex = true;
        altoTabla = "175";
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
        String pagActual = "nreporteseguridad";
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
        lovEmpleados = null;
        lovEmpresas = null;
        lovEstructuras = null;
        lovGruposConceptos = null;
        lovInforeportes = null;
        lovSucursales = null;
        lovTerceros = null;
    }

    @PostConstruct
    public void iniciarAdministrador() {
        try {
            FacesContext contexto = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) contexto.getExternalContext().getSession(false);
            externalContext = contexto.getExternalContext();
            userAgent = externalContext.getRequestHeaderMap().get("User-Agent");
            administarReportes.obtenerConexion(ses.getId());
            administrarNReportesSeguridad.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct controlNReporteNomina" + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void iniciarPagina() {
        activoMostrarTodos = true;
        activoBuscarReporte = false;
        activarEnvio = true;
        getListaIR();
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
        if (reporteSeleccionado.getTercero().equals("SI")) {
            requisitosReporte = requisitosReporte + "- Tercero -";
        }
        if (!requisitosReporte.isEmpty()) {
            RequestContext.getCurrentInstance().update("formDialogos:requisitosReporte");
            RequestContext.getCurrentInstance().execute("PF('requisitosReporte').show()");
        }
    }

    public void cancelarRequisitosReporte() {
        requisitosReporte = "";
    }

    private void activarEnvioCorreo() {
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
            System.out.println("reporteSeleccionado.getEmdesde(): " + reporteSeleccionado.getEmdesde());
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
                //empleadoHastaParametro.setStyle("position: absolute; top: 41px; left: 330px; height: 10px; font-size: 11px; width: 90px; color: red;");
                empleadoHastaParametro.setStyle(empleadoHastaParametro.getStyle() + "color: red;");
                RequestContext.getCurrentInstance().update("formParametros:empleadoHastaParametro");
            }
            if (reporteSeleccionado.getTercero().equals("SI")) {
                terceroParametro = (InputText) FacesContext.getCurrentInstance().getViewRoot().findComponent("formParametros:terceroParametro");
                //terceroParametro.setStyle("position: absolute; top: 66px; left: 625px; height: 10px; font-size: 11px; width: 180px; color: red;");
                terceroParametro.setStyle(terceroParametro.getStyle() + "color: red;");
                RequestContext.getCurrentInstance().update("formParametros:terceroParametro");
            }
            RequestContext.getCurrentInstance().update("formParametros");
        } catch (Exception ex) {
            System.out.println("Error en selecccion Registro : " + ex.getMessage());
        }
    }

    public void dispararDialogoGuardarCambios() {
        RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");

    }

    public void guardarYSalir() {
        guardarCambios();
        salir();
    }

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
                    if (parametroDeReporte.getEmpresa().getSecuencia() == null) {
                        parametroDeReporte.setEmpresa(null);
                    }
                    if (parametroDeReporte.getTercero().getSecuencia() == null) {
                        parametroDeReporte.setTercero(null);
                    }
                    if (parametroDeReporte.getEmpresa().getSecuencia() == null) {
                        parametroDeReporte.setEmpresa(null);
                    }
                    if (parametroDeReporte.getSucursalPila().getSecuencia() == null) {
                        parametroDeReporte.setSucursalPila(null);
                    }
                    administrarNReportesSeguridad.modificarParametrosReportes(parametroDeReporte);
                }
                if (!listaInfoReportesModificados.isEmpty()) {
                    administrarNReportesSeguridad.guardarCambiosInfoReportes(listaInfoReportesModificados);
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
                parametroDeReporte.setFechadesde(fechaDesde);
                parametroDeReporte.setFechahasta(fechaHasta);
                RequestContext.getCurrentInstance().update("formParametros");
                RequestContext.getCurrentInstance().execute("PF('errorFechas').show()");
            }
        } catch (Exception e) {
            System.out.println("Error en guardar Cambios Controlador : " + e.toString());
            FacesMessage msg = new FacesMessage("Información", "ha ocurrido un error en el guardado, intente nuevamente");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
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
            switch (casilla) {
                case 1:
                    deshabilitarBotonLov();
                    fechaDesde = parametroDeReporte.getFechadesde();
                    break;
                case 2:
                    deshabilitarBotonLov();
                    fechaHasta = parametroDeReporte.getFechahasta();
                    break;
                case 3:
                    habilitarBotonLov();
                    emplDesde = parametroDeReporte.getCodigoempleadodesde();
                    break;
                case 4:
                    habilitarBotonLov();
                    emplHasta = parametroDeReporte.getCodigoempleadohasta();
                    break;
                case 5:
                    habilitarBotonLov();
                    tercero = parametroDeReporte.getTercero().getNombre();
                    break;
                case 6:
                    habilitarBotonLov();
                    empresa = parametroDeReporte.getEmpresa().getNombre();
                    break;
                case 8:
                    habilitarBotonLov();
                    sucursal = parametroDeReporte.getSucursalPila().getDescripcion();
                    break;
                default:
                    break;
            }
        }
        casillaInforReporte = -1;
    }

    public void editarCelda() {
        System.out.println(this.getClass().getName() + ".editarCelda()");
        RequestContext context = RequestContext.getCurrentInstance();
        if (casilla >= 1) {
            if (casilla == 1) {
                RequestContext.getCurrentInstance().update("formDialogos:editarFechaDesde");
                RequestContext.getCurrentInstance().execute("PF('editarFechaDesde').show()");
            }
            if (casilla == 2) {
                RequestContext.getCurrentInstance().update("formDialogos:editarFechaHasta");
                RequestContext.getCurrentInstance().execute("PF('editarFechaHasta').show()");
            }
            if (casilla == 3) {
                RequestContext.getCurrentInstance().update("formDialogos:empleadoDesde");
                RequestContext.getCurrentInstance().execute("PF('empleadoDesde').show()");
            }
            if (casilla == 4) {
                RequestContext.getCurrentInstance().update("formDialogos:empleadoHasta");
                RequestContext.getCurrentInstance().execute("PF('empleadoHasta').show()");
            }
            if (casilla == 5) {
                RequestContext.getCurrentInstance().update("formDialogos:tercero");
                RequestContext.getCurrentInstance().execute("PF('tercero').show()");
            }
            if (casilla == 6) {
                RequestContext.getCurrentInstance().update("formDialogos:empresa");
                RequestContext.getCurrentInstance().execute("PF('empresa').show()");
            }
            if (casilla == 8) {
                RequestContext.getCurrentInstance().update("formDialogos:sucursal");
                RequestContext.getCurrentInstance().execute("PF('sucursal').show()");
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

    public void autocompletarGeneral(String campoConfirmar, String valorConfirmar) {
        System.out.println(this.getClass().getName() + ".autocompletarGeneral()");
        RequestContext context = RequestContext.getCurrentInstance();
        int indiceUnicoElemento = -1;
        int coincidencias = 0;
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
                    RequestContext.getCurrentInstance().update("formDialogos:TerceroDialogo");
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
        if (campoConfirmar.equalsIgnoreCase("SUCURSAL")) {
            if (!valorConfirmar.isEmpty()) {
                parametroDeReporte.getSucursalPila().setDescripcion(sucursal);
                for (int i = 0; i < lovSucursales.size(); i++) {
                    if (lovSucursales.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    parametroDeReporte.setSucursalPila(lovSucursales.get(indiceUnicoElemento));
                    parametroModificacion = parametroDeReporte;
                    lovSucursales.clear();
                    getLovSucursales();
                    cambiosReporte = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                } else {
                    permitirIndex = false;
                    RequestContext.getCurrentInstance().update("formDialogos:SucursalDialogo");
                    RequestContext.getCurrentInstance().execute("PF('SucursalDialogo').show()");
                }
            } else {
                parametroDeReporte.setSucursalPila(new SucursalesPila());
                parametroModificacion = parametroDeReporte;
                lovSucursales.clear();
                getLovSucursales();
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

    public void listaValoresBoton() {
        System.out.println(this.getClass().getName() + ".listaValoresBoton()");
        RequestContext context = RequestContext.getCurrentInstance();
        if (casilla == 3) {
            lovEmpleados = null;
            cargarListaEmpleados();
            contarRegistrosEmpleadoD();
            RequestContext.getCurrentInstance().update("formDialogos:EmpleadoDesdeDialogo");
            RequestContext.getCurrentInstance().execute("PF('EmpleadoDesdeDialogo').show()");
        }
        if (casilla == 4) {
            lovEmpleados = null;
            cargarListaEmpleados();
            contarRegistrosEmpleadoH();
            RequestContext.getCurrentInstance().update("formDialogos:EmpleadoHastaDialogo");
            RequestContext.getCurrentInstance().execute("PF('EmpleadoHastaDialogo').show()");
        }
        if (casilla == 5) {
            lovTerceros = null;
            cargarLovTerceros();
            contarRegistrosTercero();
            RequestContext.getCurrentInstance().update("formDialogos:TerceroDialogo");
            RequestContext.getCurrentInstance().execute("PF('TerceroDialogo').show()");
        }
        if (casilla == 6) {
            lovEmpresas = null;
            cargarLovEmpresas();
            contarRegistrosEmpresa();
            RequestContext.getCurrentInstance().update("formDialogos:EmpresaDialogo");
            RequestContext.getCurrentInstance().execute("PF('EmpresaDialogo').show()");
        }
        if (casilla == 8) {
            lovSucursales = null;
            cargarLovSucursales();
            contarRegistrosSucursales();
            RequestContext.getCurrentInstance().update("formDialogos:sucursalDialogo");
            RequestContext.getCurrentInstance().execute("PF('sucursalDialogo').show()");
        }
    }

    public void dialogosParametros(int pos) {
        RequestContext context = RequestContext.getCurrentInstance();
        if (pos == 3) {
            lovEmpleados = null;
            cargarListaEmpleados();
            contarRegistrosEmpleadoD();
            RequestContext.getCurrentInstance().update("formDialogos:EmpleadoDesdeDialogo");
            RequestContext.getCurrentInstance().execute("PF('EmpleadoDesdeDialogo').show()");
        }
        if (pos == 4) {
            lovEmpleados = null;
            cargarListaEmpleados();
            contarRegistrosEmpleadoH();
            RequestContext.getCurrentInstance().update("formDialogos:EmpleadoHastaDialogo");
            RequestContext.getCurrentInstance().execute("PF('EmpleadoHastaDialogo').show()");
        }
        if (pos == 5) {
            lovTerceros = null;
            cargarLovTerceros();
            contarRegistrosTercero();
            RequestContext.getCurrentInstance().update("formDialogos:TerceroDialogo");
            RequestContext.getCurrentInstance().execute("PF('TerceroDialogo').show()");
        }
        if (pos == 6) {
            lovEmpresas = null;
            cargarLovEmpresas();
            contarRegistrosEmpresa();
            RequestContext.getCurrentInstance().update("formDialogos:EmpresaDialogo");
            RequestContext.getCurrentInstance().execute("PF('EmpresaDialogo').show()");
        }

        if (pos == 8) {
            lovSucursales = null;
            cargarLovSucursales();
            RequestContext.getCurrentInstance().update("formDialogos:sucursalDialogo");
            RequestContext.getCurrentInstance().execute("PF('sucursalDialogo').show()");
            contarRegistrosSucursales();
        }

    }

    public void activarAceptar() {
        aceptar = false;
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
        activoBuscarReporte = true;
        activoMostrarTodos = false;
        reporteSeleccionado = reporteSeleccionadoLOV;
        reporteSeleccionadoLOV = null;
        contarRegistros();
        RequestContext.getCurrentInstance().update("form:MOSTRARTODOS");
        RequestContext.getCurrentInstance().update("form:BUSCARREPORTE");
        context.reset("formDialogos:lovReportesDialogo:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovReportesDialogo').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('ReportesDialogo').hide()");
        RequestContext.getCurrentInstance().update("form:reportesSeguridad");
    }

    public void cancelarSeleccionInforeporte() {
        filtrarListIRU = null;
        reporteSeleccionadoLOV = null;
        aceptar = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formDialogos:lovReportesDialogo:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovReportesDialogo').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('ReportesDialogo').hide()");
    }

    public void actualizarEmplDesde() {
        permitirIndex = true;
        parametroDeReporte.setCodigoempleadodesde(empleadoSeleccionado.getCodigoempleado());
        parametroModificacion = parametroDeReporte;
        cambiosReporte = false;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formDialogos:lovEmpleadoDesde:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovEmpleadoDesde').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('EmpleadoDesdeDialogo').hide()");

        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("formParametros:empleadoDesdeParametro");
        empleadoSeleccionado = null;
        aceptar = true;
        filtrarLovEmpleados = null;

    }

    public void cancelarCambioEmplDesde() {
        empleadoSeleccionado = null;
        aceptar = true;
        filtrarLovEmpleados = null;
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
        cambiosReporte = false;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formDialogos:lovEmpleadoHasta:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovEmpleadoHasta').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('EmpleadoHastaDialogo').hide()");

        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("formParametros:empleadoHastaParametro");
        empleadoSeleccionado = null;
        aceptar = true;
        filtrarLovEmpleados = null;
    }

    public void cancelarCambioEmplHasta() {
        empleadoSeleccionado = null;
        aceptar = true;
        filtrarLovEmpleados = null;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formDialogos:lovEmpleadoHasta:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovEmpleadoHasta').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('EmpleadoHastaDialogo').hide()");
    }

    public void actualizarEmpresa() {
        RequestContext context = RequestContext.getCurrentInstance();
        permitirIndex = true;
        parametroDeReporte.setEmpresa(empresaSeleccionada);
        parametroModificacion = parametroDeReporte;
        cambiosReporte = false;
        System.out.println("cambio de empresa");
        empresaSeleccionada = null;
        aceptar = true;
        filtrarLovEmpresas = null;
        context.reset("formDialogos:lovEmpresa:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovEmpresa').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('EmpresaDialogo').hide()");
        RequestContext.getCurrentInstance().update("formDialogos:EmpresaDialogo");
        RequestContext.getCurrentInstance().update("formDialogos:lovEmpresa");
        RequestContext.getCurrentInstance().update("formDialogos:aceptarE");
        RequestContext.getCurrentInstance().update("formParametros:empresaParametro");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void cancelarEmpresa() {
        empresaSeleccionada = null;
        aceptar = true;
        filtrarLovEmpresas = null;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formDialogos:lovEmpresa:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovEmpresa').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('EmpresaDialogo').hide()");
    }

    public void actualizarTercero() {
        permitirIndex = true;
        parametroDeReporte.setTercero(terceroSeleccionado);
        parametroModificacion = parametroDeReporte;
        cambiosReporte = false;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formDialogos:lovTercero:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovTercero').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('TerceroDialogo').hide()");

        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("formParametros:terceroParametro");
        terceroSeleccionado = null;
        aceptar = true;
        filtrarLovTerceros = null;

    }

    public void cancelarTercero() {
        terceroSeleccionado = null;
        aceptar = true;
        filtrarLovTerceros = null;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formDialogos:lovTercero:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovTercero').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('TerceroDialogo').hide()");
    }

    public void actualizarSucursal() {
        permitirIndex = true;
        parametroDeReporte.setSucursalPila(sucursalSeleccionada);
        parametroModificacion = parametroDeReporte;
        cambiosReporte = false;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formDialogos:lovSucursal:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovSucursal').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('sucursalDialogo').hide()");

        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("formParametros:sucursalParametro");
        sucursalSeleccionada = null;
        aceptar = true;
        filtrarLovSucursales = null;

    }

    public void cancelarSucursal() {
        sucursalSeleccionada = null;
        aceptar = true;
        filtrarLovSucursales = null;
        lovSucursales.clear();
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formDialogos:lovSucursal:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovSucursal').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('sucursalDialogo').hide()");
    }

    public void mostrarDialogoGenerarReporte() {
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("formDialogos:reporteAGenerar");
        RequestContext.getCurrentInstance().execute("PF('reporteAGenerar').show()");
    }

    public void cancelarGenerarReporte() {
        System.out.println(this.getClass().getName() + ".cancelarGenerarReporte()");
        reporteGenerar = "";
        posicionReporte = -1;
    }

    public void mostrarDialogoBuscarReporte() {
        System.out.println(this.getClass().getName() + ".mostrarDialogoBuscarReporte()");
        reporteSeleccionadoLOV = null;
        try {
            if (cambiosReporte == true) {
                cargarLovReportes();
                contarRegistrosLovReportes();
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("formDialogos:ReportesDialogo");
                RequestContext.getCurrentInstance().execute("PF('ReportesDialogo').show()");
            } else {
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().execute("PF('confirmarGuardarSinSalida').show()");
            }
        } catch (Exception e) {
            System.out.println("Error mostrarDialogoBuscarReporte : " + e.toString());
        }
    }

    public void salir() {
        limpiarListasValor();
        System.out.println(this.getClass().getName() + ".salir()");
        if (bandera == 1) {
            cerrarFiltrado();
        }
        listaIR = null;
        parametroDeReporte = null;
        parametroModificacion = null;
        lovSucursales = null;
        lovEmpleados = null;
        lovEmpresas = null;
        lovEstructuras = null;
        lovGruposConceptos = null;
        lovTerceros = null;
        casilla = -1;
        listaInfoReportesModificados.clear();
        cambiosReporte = true;
        reporteSeleccionado = null;
        reporteSeleccionadoLOV = null;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        navegar("atras");
    }

    public void mostrarTodos() {
        System.out.println(this.getClass().getName() + ".mostrarTodos()");
        if (cambiosReporte == true) {
            defaultPropiedadesParametrosReporte();
            listaIR.clear();
            for (int i = 0; i < lovInforeportes.size(); i++) {
                listaIR.add(lovInforeportes.get(i));
            }
            reporteSeleccionado = null;
            activoBuscarReporte = false;
            activoMostrarTodos = true;
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:MOSTRARTODOS");
            RequestContext.getCurrentInstance().update("form:BUSCARREPORTE");
            RequestContext.getCurrentInstance().update("form:reportesSeguridad");
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardarSinSalida').show()");
        }
    }

    public void cancelarYSalir() {
        System.out.println(this.getClass().getName() + ".cancelarYSalir()");
        cancelarModificaciones();
        salir();
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
        getParametroDeReporte();
        getListaIR();
        activoMostrarTodos = true;
        activoBuscarReporte = false;
        reporteSeleccionado = null;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:MOSTRARTODOS");
        RequestContext.getCurrentInstance().update("form:BUSCARREPORTE");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("form:ENVIOCORREO");
        RequestContext.getCurrentInstance().update("form:reportesSeguridad");
        RequestContext.getCurrentInstance().update("formParametros:fechaDesdeParametroL");
        RequestContext.getCurrentInstance().update("formParametros:empleadoDesdeParametro");
        RequestContext.getCurrentInstance().update("formParametros:fechaHastaParametroL");
        RequestContext.getCurrentInstance().update("formParametros:empleadoHastaParametro");
        RequestContext.getCurrentInstance().update("formParametros:empresaParametro");
        RequestContext.getCurrentInstance().update("formParametros:terceroParametro");
        RequestContext.getCurrentInstance().update("formParametros:sucursalParametro");
    }

    public void activarCtrlF11() {
        System.out.println(this.getClass().getName() + ".activarCtrlF11()");
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            altoTabla = "155";
            codigoIR = (Column) c.getViewRoot().findComponent("form:reportesSeguridad:codigoIR");
            codigoIR.setFilterStyle("width: 85% !important");
            reporteIR = (Column) c.getViewRoot().findComponent("form:reportesSeguridad:reporteIR");
            reporteIR.setFilterStyle("width: 85% !important");
            tipoIR = (SelectCheckboxMenu) c.getViewRoot().findComponent("form:reportesSeguridad:tipo");
            tipoIR.setRendered(true);
            RequestContext.getCurrentInstance().update("form:reportesSeguridad:tipo");
            RequestContext.getCurrentInstance().update("form:reportesSeguridad");
            bandera = 1;
        } else if (bandera == 1) {
            cerrarFiltrado();
            defaultPropiedadesParametrosReporte();
        }
    }

    public void cerrarFiltrado() {
        FacesContext c = FacesContext.getCurrentInstance();
        altoTabla = "175";
        codigoIR = (Column) c.getViewRoot().findComponent("form:reportesSeguridad:codigoIR");
        codigoIR.setFilterStyle("display: none; visibility: hidden;");
        reporteIR = (Column) c.getViewRoot().findComponent("form:reportesSeguridad:reporteIR");
        reporteIR.setFilterStyle("display: none; visibility: hidden;");
        tipoIR = (SelectCheckboxMenu) c.getViewRoot().findComponent("form:reportesSeguridad:tipo");
        tipoIR.setRendered(false);
        tipoIR.setSelectedValues(null);
        tipoIR.resetValue();
        RequestContext.getCurrentInstance().update("form:reportesSeguridad:tipo");
        bandera = 0;
        tipoLista = 0;
        filtrarListIRU = null;
        RequestContext.getCurrentInstance().update("form:reportesSeguridad");        
    }

    public void modificacionTipoReporte(Inforeportes reporte) {
        System.out.println("Controlador.ControlNReportesSeguridad.modificacionTipoReporte()");
        reporteSeleccionado = reporte;
        cambiosReporte = false;
        if (reporteSeleccionado.getEstadoTipo().equals("PLANO")) {
            reporteSeleccionado.setEstadoTipo("DELIMITED");
            tipoReporte = reporteSeleccionado.getEstadoTipo();
        }
        if (listaInfoReportesModificados.isEmpty()) {
            listaInfoReportesModificados.add(reporteSeleccionado);
        } else if (!listaInfoReportesModificados.contains(reporteSeleccionado)) {
            listaInfoReportesModificados.add(reporteSeleccionado);
        } else {
            int posicion = listaInfoReportesModificados.indexOf(reporteSeleccionado);
            listaInfoReportesModificados.add(reporteSeleccionado);
            listaInfoReportesModificados.set(posicion, reporteSeleccionado);
        }
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void defaultPropiedadesParametrosReporte() {
        color = "black";
        decoracion = "none";
        color2 = "black";
        decoracion2 = "none";

    }

    public void generarDocumentoReporte() {
        try {
            if (reporteSeleccionado != null) {
                System.out.println("reporteSeleccionado.getEstadoTipo(): " + reporteSeleccionado.getEstadoTipo());
                nombreReporte = reporteSeleccionado.getNombrereporte();
                if (reporteSeleccionado.getEstadoTipo().equals("PLANO")) {
                    reporteSeleccionado.setEstadoTipo("DELIMITED");
                    tipoReporte = reporteSeleccionado.getEstadoTipo();
                } else {
                    tipoReporte = reporteSeleccionado.getEstadoTipo();
                }
                System.out.println("reporteSeleccionado.getEstadoTipo(): " + reporteSeleccionado.getEstadoTipo());
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
            System.out.println("error en generarDocumentoReporte : " + e.getMessage());
            RequestContext.getCurrentInstance().execute("PF('generandoReporte').hide()");
            RequestContext.getCurrentInstance().update("formDialogos:errorGenerandoReporte");
            RequestContext.getCurrentInstance().execute("PF('errorGenerandoReporte').show()");
        }
    }

    public void generarReporte(Inforeportes reporte) {
        try {
            guardarCambios();
            reporteSeleccionado = reporte;
            seleccionRegistro();
            generarDocumentoReporte();
        } catch (Exception e) {
            System.out.println("error en generarReporte : " + e.getMessage());
            RequestContext.getCurrentInstance().update("formDialogos:errorGenerandoReporte");
            RequestContext.getCurrentInstance().execute("PF('errorGenerandoReporte').show()");
        }
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
            System.out.println("Error en exportarReporte : " + e.getMessage());
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
                        fis = new FileInputStream(new File(pathReporteGenerado));
                        reporte = new DefaultStreamedContent(fis, "application/pdf");
                    } catch (FileNotFoundException ex) {
                        RequestContext.getCurrentInstance().update("formDialogos:errorGenerandoReporte");
                        RequestContext.getCurrentInstance().execute("PF('errorGenerandoReporte').show()");
                        reporte = null;
                    }
                    if (reporte != null) {
                        if (reporteSeleccionado != null) {
                            System.out.println("userAgent " + userAgent);
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
            System.out.println("error en validarDescargarReporte : " + e.getMessage());
            RequestContext.getCurrentInstance().execute("PF('generandoReporte').hide()");
            RequestContext.getCurrentInstance().update("formDialogos:errorGenerandoReporte");
            RequestContext.getCurrentInstance().execute("PF('errorGenerandoReporte').show()");
        }
    }

    public void reiniciarStreamedContent() {
        reporte = null;
    }

    public void cancelarReporte() {
        System.out.println(this.getClass().getName() + ".cancelarReporte()");
        administarReportes.cancelarReporte();
    }

//    public void recordarSeleccion() {
//        System.out.println(this.getClass().getName() + ".recordarSeleccion()");
//        if (reporteSeleccionado != null) {
//            FacesContext c = FacesContext.getCurrentInstance();
//            tabla = (DataTable) c.getViewRoot().findComponent("form:reportesSeguridad");
//            tabla.setSelection(reporteSeleccionado);
//        }
//    }
///EVENTO FILTRAR    
    public void eventoFiltrar() {
        contarRegistros();
    }

    public void contarRegistros() {
        RequestContext.getCurrentInstance().update("form:infoRegistro");
    }

    public void contarRegistrosEmpleadoD() {
        RequestContext.getCurrentInstance().update("formDialogos:infoRegistroEmpleadoDesde");
    }

    public void contarRegistrosEmpleadoH() {
        RequestContext.getCurrentInstance().update("formDialogos:infoRegistroEmpleadoHasta");
    }

    public void contarRegistrosEmpresa() {
        RequestContext.getCurrentInstance().update("formDialogos:infoRegistroEmpresa");
    }

    public void contarRegistrosTercero() {
        RequestContext.getCurrentInstance().update("formDialogos:infoRegistroTercero");
    }

    public void contarRegistrosLovReportes() {
        RequestContext.getCurrentInstance().update("formDialogos:infoRegistroReportes");
    }

    public void contarRegistrosSucursales() {
        RequestContext.getCurrentInstance().update("formDialogos:infoRegistroSucursal");
    }

    public void habilitarBotonLov() {
        activarLov = false;
        RequestContext.getCurrentInstance().update("form:listaValores");
    }

    public void deshabilitarBotonLov() {
        activarLov = true;
        RequestContext.getCurrentInstance().update("form:listaValores");
    }

    public void cargarListaEmpleados() {
        if (lovEmpleados == null) {
            lovEmpleados = administrarNReportesSeguridad.listEmpleados();
        }
    }

    public void cargarLovTerceros() {
        if (lovTerceros == null) {
            lovTerceros = administrarNReportesSeguridad.listTerceros();
        }
    }

    public void cargarLovEmpresas() {
        if (lovEmpresas == null) {
            lovEmpresas = administrarNReportesSeguridad.listEmpresas();
        }
    }

    public void cargarLovSucursales() {
        System.out.println("Controlador.ControlNReportesSeguridad.cargarLovSucursales()");
        if (lovSucursales == null) {
            if (parametroDeReporte.getEmpresa().getSecuencia() != null && !parametroDeReporte.getEmpresa().getSecuencia().equals("")) {
                auxiliar = parametroDeReporte.getEmpresa().getSecuencia();
                lovSucursales = administrarNReportesSeguridad.listSucursalesPorEmpresa(auxiliar);
            } else {
                lovSucursales = administrarNReportesSeguridad.listSucursales();
            }
        }
    }

    public void cargarLovReportes() {
        if (lovInforeportes == null) {
            lovInforeportes = administrarNReportesSeguridad.listInforeportesUsuario();
        }
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

////sets y gets
    public ParametrosReportes getParametroDeReporte() {
        try {
            if (parametroDeReporte == null) {
                parametroDeReporte = new ParametrosReportes();
                parametroDeReporte = administrarNReportesSeguridad.parametrosDeReporte();
            }
            if (parametroDeReporte.getTercero() == null) {
                parametroDeReporte.setTercero(new Terceros());
            }
            if (parametroDeReporte.getEmpresa() == null) {
                parametroDeReporte.setEmpresa(new Empresas());
            }
            if (parametroDeReporte.getSucursalPila() == null) {
                parametroDeReporte.setSucursalPila(new SucursalesPila());
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
            listaIR = administrarNReportesSeguridad.listInforeportesUsuario();
        }
        return listaIR;
    }

    public void setListaIR(List<Inforeportes> listaIR) {
        this.listaIR = listaIR;
    }

    public List<Inforeportes> getLovInforeportes() {
        return lovInforeportes;
    }

    public void setLovInforeportes(List<Inforeportes> lovInforeportes) {
        this.lovInforeportes = lovInforeportes;
    }

    public List<Empleados> getLovEmpleados() {
        return lovEmpleados;
    }

    public void setLovEmpleados(List<Empleados> lovEmpleados) {
        this.lovEmpleados = lovEmpleados;
    }

    public List<GruposConceptos> getLovGruposConceptos() {
        return lovGruposConceptos;
    }

    public void setLovGruposConceptos(List<GruposConceptos> lovGruposConceptos) {
        this.lovGruposConceptos = lovGruposConceptos;
    }

    public List<Empresas> getLovEmpresas() {
        return lovEmpresas;
    }

    public void setLovEmpresas(List<Empresas> lovEmpresas) {
        this.lovEmpresas = lovEmpresas;
    }

    public List<Terceros> getLovTerceros() {

        return lovTerceros;
    }

    public void setLovTerceros(List<Terceros> lovTerceros) {
        this.lovTerceros = lovTerceros;
    }

    public List<Estructuras> getLovEstructuras() {
        if (lovEstructuras == null) {
            lovEstructuras = administrarNReportesSeguridad.listEstructuras();
        }
        return lovEstructuras;
    }

    public void setLovEstructuras(List<Estructuras> lovEstructuras) {
        this.lovEstructuras = lovEstructuras;
    }

    public List<SucursalesPila> getLovSucursales() {
        return lovSucursales;
    }

    public void setLovSucursales(List<SucursalesPila> lovSucursales) {
        this.lovSucursales = lovSucursales;
    }

    public List<Inforeportes> getListaInfoReportesModificados() {
        return listaInfoReportesModificados;
    }

    public void setListaInfoReportesModificados(List<Inforeportes> listaInfoReportesModificados) {
        this.listaInfoReportesModificados = listaInfoReportesModificados;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public void setAceptar(boolean aceptar) {
        this.aceptar = aceptar;
    }

    public Inforeportes getReporteSeleccionadoLOV() {
        return reporteSeleccionadoLOV;
    }

    public void setReporteSeleccionadoLOV(Inforeportes reporteSeleccionadoLOV) {
        this.reporteSeleccionadoLOV = reporteSeleccionadoLOV;
    }

    public Empresas getEmpresaSeleccionada() {
        return empresaSeleccionada;
    }

    public void setEmpresaSeleccionada(Empresas empresaSeleccionada) {
        this.empresaSeleccionada = empresaSeleccionada;
    }

    public SucursalesPila getSucursalSeleccionada() {
        return sucursalSeleccionada;
    }

    public void setSucursalSeleccionada(SucursalesPila sucursalSeleccionada) {
        this.sucursalSeleccionada = sucursalSeleccionada;
    }

    public ParametrosReportes getNuevoParametroInforme() {
        return nuevoParametroInforme;
    }

    public void setNuevoParametroInforme(ParametrosReportes nuevoParametroInforme) {
        this.nuevoParametroInforme = nuevoParametroInforme;
    }

    public List<Inforeportes> getFiltrarListIRU() {
        return filtrarListIRU;
    }

    public void setFiltrarListIRU(List<Inforeportes> filtrarListIRU) {
        this.filtrarListIRU = filtrarListIRU;
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

    public List<Empleados> getFiltrarLovEmpleados() {
        return filtrarLovEmpleados;
    }

    public void setFiltrarLovEmpleados(List<Empleados> filtrarLovEmpleados) {
        this.filtrarLovEmpleados = filtrarLovEmpleados;
    }

    public List<GruposConceptos> getFiltrarLovGruposConceptos() {
        return filtrarLovGruposConceptos;
    }

    public void setFiltrarLovGruposConceptos(List<GruposConceptos> filtrarLovGruposConceptos) {
        this.filtrarLovGruposConceptos = filtrarLovGruposConceptos;
    }

    public List<Empresas> getFiltrarLovEmpresas() {
        return filtrarLovEmpresas;
    }

    public void setFiltrarLovEmpresas(List<Empresas> filtrarLovEmpresas) {
        this.filtrarLovEmpresas = filtrarLovEmpresas;
    }

    public List<Terceros> getFiltrarLovTerceros() {
        return filtrarLovTerceros;
    }

    public void setFiltrarLovTerceros(List<Terceros> filtrarLovTerceros) {
        this.filtrarLovTerceros = filtrarLovTerceros;
    }

    public List<TiposTrabajadores> getFiltrarLovTiposTrabajadores() {
        return filtrarLovTiposTrabajadores;
    }

    public void setFiltrarLovTiposTrabajadores(List<TiposTrabajadores> filtrarLovTiposTrabajadores) {
        this.filtrarLovTiposTrabajadores = filtrarLovTiposTrabajadores;
    }

    public List<Estructuras> getFiltrarLovEstructuras() {
        return filtrarLovEstructuras;
    }

    public void setFiltrarLovEstructuras(List<Estructuras> filtrarLovEstructuras) {
        this.filtrarLovEstructuras = filtrarLovEstructuras;
    }

    public List<SucursalesPila> getFiltrarLovSucursales() {
        return filtrarLovSucursales;
    }

    public void setFiltrarLovSucursales(List<SucursalesPila> filtrarLovSucursales) {
        this.filtrarLovSucursales = filtrarLovSucursales;
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

    public String getInfoRegistroTercero() {
        FacesContext c = FacesContext.getCurrentInstance();
        tabla = (DataTable) c.getViewRoot().findComponent("formDialogos:lovTercero");
        infoRegistroTercero = String.valueOf(tabla.getRowCount());
        return infoRegistroTercero;
    }

    public String getInfoRegistroSucursal() {
        FacesContext c = FacesContext.getCurrentInstance();
        tabla = (DataTable) c.getViewRoot().findComponent("formDialogos:lovSucursal");
        infoRegistroSucursal = String.valueOf(tabla.getRowCount());
        return infoRegistroSucursal;
    }

    public String getInfoRegistroLovReportes() {
        FacesContext c = FacesContext.getCurrentInstance();
        tabla = (DataTable) c.getViewRoot().findComponent("formDialogos:lovReportesDialogo");
        infoRegistroLovReportes = String.valueOf(tabla.getRowCount());
        return infoRegistroLovReportes;
    }

    public String getInfoRegistro() {
        FacesContext c = FacesContext.getCurrentInstance();
        tabla = (DataTable) c.getViewRoot().findComponent("form:reportesSeguridad");
        infoRegistro = String.valueOf(tabla.getRowCount());
        return infoRegistro;
    }

    public Terceros getTerceroSeleccionado() {
        return terceroSeleccionado;
    }

    public void setTerceroSeleccionado(Terceros terceroSeleccionado) {
        this.terceroSeleccionado = terceroSeleccionado;
    }

    public TiposTrabajadores getTipoTSeleccionado() {
        return tipoTSeleccionado;
    }

    public void setTipoTSeleccionado(TiposTrabajadores tipoTSeleccionado) {
        this.tipoTSeleccionado = tipoTSeleccionado;
    }

    public String getTipoReporte() {
        return tipoReporte;
    }

    public void setTipoReporte(String tipoReporte) {
        this.tipoReporte = tipoReporte;
    }

    public String getAltoTabla() {
        return altoTabla;
    }

    public void setAltoTabla(String altoTabla) {
        this.altoTabla = altoTabla;
    }

    public Empleados getEmpleadoSeleccionado() {
        return empleadoSeleccionado;
    }

    public void setEmpleadoSeleccionado(Empleados empleadoSeleccionado) {
        this.empleadoSeleccionado = empleadoSeleccionado;
    }

    public Inforeportes getReporteSeleccionado() {
        return reporteSeleccionado;
    }

    public void setReporteSeleccionado(Inforeportes reporteSeleccionado) {
        this.reporteSeleccionado = reporteSeleccionado;
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

    public String getCabezeraVisor() {
        return cabezeraVisor;
    }

    public void setCabezeraVisor(String cabezeraVisor) {
        this.cabezeraVisor = cabezeraVisor;
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

    public String getColor2() {
        return color2;
    }

    public void setColor2(String color2) {
        this.color2 = color2;
    }

    public String getDecoracion() {
        return decoracion;
    }

    public void setDecoracion(String decoracion) {
        this.decoracion = decoracion;
    }

    public String getDecoracion2() {
        return decoracion2;
    }

    public void setDecoracion2(String decoracion2) {
        this.decoracion2 = decoracion2;
    }

    public String getReporteGenerar() {
        return reporteGenerar;
    }

    public void setReporteGenerar(String reporteGenerar) {
        this.reporteGenerar = reporteGenerar;
    }

    public StreamedContent getReporte() {
        return reporte;
    }

    public void setReporte(StreamedContent reporte) {
        this.reporte = reporte;
    }

    public String getRequisitosReporte() {
        return requisitosReporte;
    }

    public void setRequisitosReporte(String requisitosReporte) {
        this.requisitosReporte = requisitosReporte;
    }

    public String getResultadoReporte() {
        return resultadoReporte;
    }

    public void setResultadoReporte(String resultadoReporte) {
        this.resultadoReporte = resultadoReporte;
    }

    public String getPathReporteGenerado() {
        return pathReporteGenerado;
    }

    public void setPathReporteGenerado(String pathReporteGenerado) {
        this.pathReporteGenerado = pathReporteGenerado;
    }

    public AsynchronousFilllListener getAsistenteReporte() {
        return asistenteReporte;
    }

    public void setAsistenteReporte(AsynchronousFilllListener asistenteReporte) {
        this.asistenteReporte = asistenteReporte;
    }

    public String getProceso() {
        return proceso;
    }

    public void setProceso(String proceso) {
        this.proceso = proceso;
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
