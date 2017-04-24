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
    private List<Inforeportes> listaIR;
    private Inforeportes reporteSeleccionado;
    private List<Inforeportes> filtrarListIRU;
    private String reporteGenerar;
    private int bandera;
    private boolean aceptar;
    private Column codigoIR, reporteIR;
    private int casilla;
    private ParametrosReportes parametroModificacion;
    private int posicionReporte;
    private String requisitosReporte;
    private InputText empleadoDesdeParametro, empleadoHastaParametro, estructuraParametro, sucursalParametro, empresaParametro, tipoTrabajadorParametro, terceroParametro, grupoParametro;
    private SelectOneMenu estadoParametro;
    //LOV'S
    private List<Inforeportes> listValInforeportes;
    private Inforeportes reporteSeleccionadoLOV;
    private List<Inforeportes> filtrarLovInforeportes;
    private List<Inforeportes> filtrarReportes;
    private List<Empleados> listValEmpleados;
    private Empleados empleadoSeleccionado;
    private List<Empleados> filtrarListEmpleados;
    private List<GruposConceptos> listValGruposConceptos;
    private GruposConceptos grupoCSeleccionado;
    private List<GruposConceptos> filtrarListGruposConceptos;
    private List<Empresas> listValEmpresas;
    private Empresas empresaSeleccionada;
    private List<Empresas> filtrarListEmpresas;
    private List<Terceros> listValTerceros;
    private Terceros terceroSeleccionado;
    private List<Terceros> filtrarListTerceros;
    private TiposTrabajadores tipoTSeleccionado;
    private List<TiposTrabajadores> filtrarListTiposTrabajadores;
    private List<Estructuras> listValEstructuras;
    private Estructuras estructuraSeleccionada;
    private List<Estructuras> filtrarListEstructuras;
    private List<SucursalesPila> listValSucursales;
    private SucursalesPila sucursalSeleccionada;
    private List<SucursalesPila> filtrarListSucursales;
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
        reporteGenerar = "";
        requisitosReporte = "";
        posicionReporte = -1;
        listValInforeportes = null;
        listValEmpleados = null;
        listValEmpresas = null;
        listValEstructuras = null;
        listValGruposConceptos = null;
        listValTerceros = null;
        listValSucursales = null;
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
        /*if (pag.equals("atras")) {
            pag = paginaAnterior;
            paginaAnterior = "nominaf";
            controlListaNavegacion.quitarPagina(pagActual);
            
        } else {
            */
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
        RequestContext context = RequestContext.getCurrentInstance();
        activarEnvioCorreo();
        defaultPropiedadesParametrosReporte();
        System.out.println("reporteSeleccionado.getFecdesde(): " + reporteSeleccionado.getFecdesde());
        if (reporteSeleccionado.getFecdesde().equals("SI")) {
            color = "red";
            RequestContext.getCurrentInstance().update("formParametros");
        }
        System.out.println("reporteSeleccionado.getFechasta(): " + reporteSeleccionado.getFechasta());
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

        System.out.println("reporteSeleccionado.getEmhasta(): " + reporteSeleccionado.getEmhasta());
        if (reporteSeleccionado.getEmhasta().equals("SI")) {
            empleadoHastaParametro = (InputText) FacesContext.getCurrentInstance().getViewRoot().findComponent("formParametros:empleadoHastaParametro");
            //empleadoHastaParametro.setStyle("position: absolute; top: 41px; left: 330px; height: 10px; font-size: 11px; width: 90px; color: red;");
            empleadoHastaParametro.setStyle(empleadoHastaParametro.getStyle() + "color: red;");
            RequestContext.getCurrentInstance().update("formParametros:empleadoHastaParametro");
        }
        System.out.println("reporteSeleccionado.getTercero(): " + reporteSeleccionado.getTercero());
        if (reporteSeleccionado.getTercero().equals("SI")) {
            terceroParametro = (InputText) FacesContext.getCurrentInstance().getViewRoot().findComponent("formParametros:terceroParametro");
            //terceroParametro.setStyle("position: absolute; top: 66px; left: 625px; height: 10px; font-size: 11px; width: 180px; color: red;");
            terceroParametro.setStyle(terceroParametro.getStyle() + "color: red;");
            RequestContext.getCurrentInstance().update("formParametros:terceroParametro");
        }
        RequestContext.getCurrentInstance().update("formParametros");
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
                    if (parametroDeReporte.getSucursalPila().getSecuencia() == null) {
                        parametroDeReporte.setSucursalPila(null);
                    }
                    administrarNReportesSeguridad.modificarParametrosReportes(parametroDeReporte);
                }
                if (!listaInfoReportesModificados.isEmpty()) {
                    administrarNReportesSeguridad.guardarCambiosInfoReportes(listaInfoReportesModificados);
                }
                cambiosReporte = true;
                FacesMessage msg = new FacesMessage("Información", "Los datos se guardaron con Éxito.");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                RequestContext.getCurrentInstance().update("form:growl");
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            } else {
                parametroDeReporte.setFechadesde(fechaDesde);
                parametroDeReporte.setFechahasta(fechaHasta);
                RequestContext.getCurrentInstance().update("formParametros");
                RequestContext.getCurrentInstance().execute("PF('errorFechas').show()");
            }
        } catch (Exception e) {
            FacesMessage msg = new FacesMessage("Información", "ha ocurrido un error en el guardado, intente nuevamente");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
    }

    public void modificarParametroInforme() {
        if (parametroDeReporte.getCodigoempleadodesde() != null && parametroDeReporte.getCodigoempleadohasta() != null
                && parametroDeReporte.getFechadesde() != null && parametroDeReporte.getFechahasta() != null) {
            if (parametroDeReporte.getFechadesde().before(parametroDeReporte.getFechahasta())) {
                parametroModificacion = parametroDeReporte;
                cambiosReporte = false;
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            } else {
                parametroDeReporte.setFechadesde(fechaDesde);
                parametroDeReporte.setFechahasta(fechaHasta);
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("formParametros");
                RequestContext.getCurrentInstance().execute("PF('errorFechas').show()");
            }
        } else {
            parametroDeReporte.setCodigoempleadodesde(emplDesde);
            parametroDeReporte.setCodigoempleadohasta(emplHasta);
            parametroDeReporte.setFechadesde(fechaDesde);
            parametroDeReporte.setFechahasta(fechaHasta);
            parametroDeReporte.getEmpresa().setNombre(empresa);
            parametroDeReporte.getSucursalPila().setDescripcion(sucursal);
            parametroDeReporte.getTercero().setNombre(tercero);
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formParametros");
            RequestContext.getCurrentInstance().execute("PF('errorRegNew').show()");
        }
    }

    public void posicionCelda(int i) {
        casilla = i;
        if (permitirIndex == true) {
            if (casilla == 0) {
                deshabilitarBotonLov();
                fechaDesde = parametroDeReporte.getFechadesde();
            } else if (casilla == 1) {
                deshabilitarBotonLov();
                fechaHasta = parametroDeReporte.getFechahasta();
            } else if (casilla == 2) {
                habilitarBotonLov();
                emplDesde = parametroDeReporte.getCodigoempleadodesde();
            } else if (casilla == 3) {
                habilitarBotonLov();
                emplHasta = parametroDeReporte.getCodigoempleadohasta();
            } else if (casilla == 4) {
                habilitarBotonLov();
                tercero = parametroDeReporte.getTercero().getNombre();
            } else if (casilla == 5) {
                habilitarBotonLov();
                empresa = parametroDeReporte.getEmpresa().getNombre();
            } else if (casilla == 7) {
                habilitarBotonLov();
                sucursal = parametroDeReporte.getSucursalPila().getDescripcion();
            }
        }
        casillaInforReporte = -1;
    }

    public void editarCelda() {
        System.out.println(this.getClass().getName() + ".editarCelda()");
        RequestContext context = RequestContext.getCurrentInstance();
        if (casilla == 0) {
            RequestContext.getCurrentInstance().update("formDialogos:editarFechaDesde");
            RequestContext.getCurrentInstance().execute("PF('editarFechaDesde').show()");
        }
        if (casilla == 1) {
            RequestContext.getCurrentInstance().update("formDialogos:editarFechaHasta");
            RequestContext.getCurrentInstance().execute("PF('editarFechaHasta').show()");
        }
        if (casilla == 2) {
            RequestContext.getCurrentInstance().update("formDialogos:empleadoDesde");
            RequestContext.getCurrentInstance().execute("PF('empleadoDesde').show()");
        }
        if (casilla == 3) {
            RequestContext.getCurrentInstance().update("formDialogos:empleadoHasta");
            RequestContext.getCurrentInstance().execute("PF('empleadoHasta').show()");
        }
        if (casilla == 4) {
            RequestContext.getCurrentInstance().update("formDialogos:tercero");
            RequestContext.getCurrentInstance().execute("PF('tercero').show()");
        }
        if (casilla == 5) {
            RequestContext.getCurrentInstance().update("formDialogos:empresa");
            RequestContext.getCurrentInstance().execute("PF('empresa').show()");
        }
        if (casilla == 7) {
            RequestContext.getCurrentInstance().update("formDialogos:sucursal");
            RequestContext.getCurrentInstance().execute("PF('sucursal').show()");
        }
        casilla = -1;

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
                    RequestContext.getCurrentInstance().update("formDialogos:EmpresaDialogo");
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
                    RequestContext.getCurrentInstance().update("formDialogos:TerceroDialogo");
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
        if (campoConfirmar.equalsIgnoreCase("SUCURSAL")) {
            if (!valorConfirmar.isEmpty()) {
                parametroDeReporte.getSucursalPila().setDescripcion(tercero);
                for (int i = 0; i < listValSucursales.size(); i++) {
                    if (listValSucursales.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    parametroDeReporte.setSucursalPila(listValSucursales.get(indiceUnicoElemento));
                    parametroModificacion = parametroDeReporte;
                    listValSucursales.clear();
                    getListValSucursales();
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
                listValSucursales.clear();
                getListValSucursales();
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

    public void listaValoresBoton() {
        System.out.println(this.getClass().getName() + ".listaValoresBoton()");
        RequestContext context = RequestContext.getCurrentInstance();
        if (casilla == 2) {
            listValEmpleados = null;
            getListValEmpleados();
            RequestContext.getCurrentInstance().update("formDialogos:EmpleadoDesdeDialogo");
            RequestContext.getCurrentInstance().execute("PF('EmpleadoDesdeDialogo').show()");
            contarRegistrosEmpleadoD();
        }
        if (casilla == 3) {
            listValEmpleados = null;
            getListValEmpleados();
            RequestContext.getCurrentInstance().update("formDialogos:EmpleadoHastaDialogo");
            RequestContext.getCurrentInstance().execute("PF('EmpleadoHastaDialogo').show()");
            contarRegistrosEmpleadoH();
        }
        if (casilla == 4) {
            listValTerceros = null;
            getListValTerceros();
            RequestContext.getCurrentInstance().update("formDialogos:TerceroDialogo");
            RequestContext.getCurrentInstance().execute("PF('TerceroDialogo').show()");
            contarRegistrosTercero();
        }
        if (casilla == 6) {
            listValEmpresas = null;
            getListValEmpresas();
            RequestContext.getCurrentInstance().update("formDialogos:EmpresaDialogo");
            RequestContext.getCurrentInstance().execute("PF('EmpresaDialogo').show()");
            contarRegistrosEmpresa();
        }
        if (casilla == 7) {
            listValSucursales = null;
            getListValSucursales();
            RequestContext.getCurrentInstance().update("formDialogos:sucursalDialogo");
            RequestContext.getCurrentInstance().execute("PF('sucursalDialogo').show()");
            contarRegistrosSucursales();
        }
    }

    public void dialogosParametros(int pos) {
        RequestContext context = RequestContext.getCurrentInstance();
        if (pos == 2) {
            listValEmpleados = null;
            cargarLovEmplDesde();
            contarRegistrosEmpleadoD();
            RequestContext.getCurrentInstance().update("formDialogos:EmpleadoDesdeDialogo");
            RequestContext.getCurrentInstance().execute("PF('EmpleadoDesdeDialogo').show()");
            contarRegistrosEmpleadoD();
        }
        if (pos == 3) {
            listValEmpleados = null;
            cargarLovEmplHasta();
            contarRegistrosEmpleadoH();
            RequestContext.getCurrentInstance().update("formDialogos:EmpleadoHastaDialogo");
            RequestContext.getCurrentInstance().execute("PF('EmpleadoHastaDialogo').show()");
            contarRegistrosEmpleadoH();
        }
        if (pos == 4) {
            listValTerceros = null;
            cargarLovTerceros();
            contarRegistrosTercero();
            RequestContext.getCurrentInstance().update("formDialogos:TerceroDialogo");
            RequestContext.getCurrentInstance().execute("PF('TerceroDialogo').show()");
            contarRegistrosTercero();
        }
        if (pos == 5) {
            listValEmpresas = null;
            cargarLovEmpresas();
            contarRegistrosEmpresa();
            RequestContext.getCurrentInstance().update("formDialogos:EmpresaDialogo");
            RequestContext.getCurrentInstance().execute("PF('EmpresaDialogo').show()");
            contarRegistrosEmpresa();
        }
        if (pos == 7) {
            listValSucursales = null;
            cargarLovReportes();
            contarRegistrosLovReportes();
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
        filtrarListEmpleados = null;

    }

    public void cancelarCambioEmplDesde() {
        empleadoSeleccionado = null;
        aceptar = true;
        filtrarListEmpleados = null;
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
        filtrarListEmpleados = null;
    }

    public void cancelarCambioEmplHasta() {
        empleadoSeleccionado = null;
        aceptar = true;
        filtrarListEmpleados = null;
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
        auxiliar = empresaSeleccionada.getSecuencia();
        listValSucursales = null;
        getListValSucursales();
        RequestContext.getCurrentInstance().update("formDialogos:sucursalDialogo");
        System.out.println("cambio de empresa");
        empresaSeleccionada = null;
        aceptar = true;
        filtrarListEmpresas = null;
        context.reset("formDialogos:lovEmpresa:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovEmpresa').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('EmpresaDialogo').hide()");
        RequestContext.getCurrentInstance().update("formDialogos:EmpresaDialogo");
        RequestContext.getCurrentInstance().update("formDialogos:lovEmpresa");
        RequestContext.getCurrentInstance().update("formDialogos:aceptarE");
        RequestContext.getCurrentInstance().update("formParametros:empresaParametro");

//        RequestContext.getCurrentInstance().update("form:ACEPTAR");
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
        filtrarListTerceros = null;

    }

    public void cancelarTercero() {
        terceroSeleccionado = null;
        aceptar = true;
        filtrarListTerceros = null;
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
        filtrarListSucursales = null;

    }

    public void cancelarSucursal() {
        sucursalSeleccionada = null;
        aceptar = true;
        filtrarListSucursales = null;
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
        try {
            if (cambiosReporte == true) {
                listValInforeportes = null;
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
        listValSucursales = null;
        listValEmpleados = null;
        listValEmpresas = null;
        listValEstructuras = null;
        listValGruposConceptos = null;
        listValTerceros = null;
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
            for (int i = 0; i < listValInforeportes.size(); i++) {
                listaIR.add(listValInforeportes.get(i));
            }
            reporteSeleccionado = null;
            activoBuscarReporte = false;
            activoMostrarTodos = true;
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
        RequestContext.getCurrentInstance().update("form:reportesSeguridad");
        bandera = 0;
        tipoLista = 0;
        filtrarListIRU = null;
    }

    public void modificacionTipoReporte(Inforeportes reporte) {
        System.out.println(this.getClass().getName() + ".modificacionTipoReporte()");
        reporteSeleccionado = reporte;
        cambiosReporte = false;
        if (listaInfoReportesModificados.isEmpty()) {
            listaInfoReportesModificados.add(reporteSeleccionado);
        } else if ((!listaInfoReportesModificados.isEmpty()) && (!listaInfoReportesModificados.contains(reporteSeleccionado))) {
            listaInfoReportesModificados.add(reporteSeleccionado);
        } else {
            int posicion = listaInfoReportesModificados.indexOf(reporteSeleccionado);
            listaInfoReportesModificados.set(posicion, reporteSeleccionado);
        }
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void defaultPropiedadesParametrosReporte() {
        System.out.println(this.getClass().getName() + ".defaultPropiedadesParametrosReporte()");
        color = "black";
        decoracion = "none";
        color2 = "black";
        decoracion2 = "none";

    }

    public void generarDocumentoReporte() {
        try {
            if (reporteSeleccionado != null) {
                System.out.println("generando reporte - ingreso al if");
                nombreReporte = reporteSeleccionado.getNombrereporte();
                tipoReporte = reporteSeleccionado.getTipo();

                if (nombreReporte != null && tipoReporte != null) {
                    System.out.println("generando reporte - ingreso al 2 if");
                    pathReporteGenerado = administarReportes.generarReporte(nombreReporte, tipoReporte);
                }
                if (pathReporteGenerado != null) {
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
        } catch (Exception e) {
            RequestContext.getCurrentInstance().execute("PF('errorGenerandoReporte').show()");
        }
    }

    public void generarReporte(Inforeportes reporte) {
        reporteSeleccionado = reporte;
        seleccionRegistro();
        generarDocumentoReporte();

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
            if (pathReporteGenerado != null) {
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
            }
        } catch (Exception e) {
            System.out.println("Error en exportarReporte : " + e.getMessage());
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
                        System.out.println("validar descarga reporte - ingreso al catch 1");
                        System.out.println(ex.getCause());
                        reporte = null;
                    }
                    if (reporte != null) {
                        if (reporteSeleccionado != null) {
                            System.out.println("userAgent " + userAgent);
                            if (userAgent.toUpperCase().contains("Mobile".toUpperCase()) || userAgent.toUpperCase().contains("Tablet".toUpperCase()) || userAgent.toUpperCase().contains("Tablet".toUpperCase())) {
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
                RequestContext.getCurrentInstance().update("formDialogos:errorGenerandoReporte");
                RequestContext.getCurrentInstance().execute("PF('errorGenerandoReporte').show()");
            }
        } catch (Exception e) {
            System.out.println("Error en validarDescargaReporte  : " + e.getMessage());
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
        contarRegistrosReportes();
    }

    public void contarRegistrosReportes() {
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

    public void cargarLovEmplDesde() {
        if (listValEmpleados == null) {
            listValEmpleados = administrarNReportesSeguridad.listEmpleados();
        }
    }

    public void cargarLovEmplHasta() {
        if (listValEmpleados == null) {
            listValEmpleados = administrarNReportesSeguridad.listEmpleados();
        }

    }

    public void cargarLovTerceros() {
        if (listValTerceros == null) {
            listValTerceros = administrarNReportesSeguridad.listTerceros();
        }
    }

    public void cargarLovEmpresas() {
        if (listValEmpresas == null) {
            listValEmpresas = administrarNReportesSeguridad.listEmpresas();
        }
    }

    public void cargarLovSucursales() {
        if (listValSucursales == null) {
            listValSucursales = administrarNReportesSeguridad.listSucursales(auxiliar);
        }
    }

    public void cargarLovReportes() {
        if (listValInforeportes == null) {
            listValInforeportes = administrarNReportesSeguridad.listInforeportesUsuario();
        }
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

    public List<Inforeportes> getListValInforeportes() {
        return listValInforeportes;
    }

    public void setListValInforeportes(List<Inforeportes> listValInforeportes) {
        this.listValInforeportes = listValInforeportes;
    }

    public List<Empleados> getListValEmpleados() {
        return listValEmpleados;
    }

    public void setListValEmpleados(List<Empleados> listValEmpleados) {
        this.listValEmpleados = listValEmpleados;
    }

    public List<GruposConceptos> getListValGruposConceptos() {
        return listValGruposConceptos;
    }

    public void setListValGruposConceptos(List<GruposConceptos> listValGruposConceptos) {
        this.listValGruposConceptos = listValGruposConceptos;
    }

    public List<Empresas> getListValEmpresas() {
        return listValEmpresas;
    }

    public void setListValEmpresas(List<Empresas> listValEmpresas) {
        this.listValEmpresas = listValEmpresas;
    }

    public List<Terceros> getListValTerceros() {

        return listValTerceros;
    }

    public void setListValTerceros(List<Terceros> listValTerceros) {
        this.listValTerceros = listValTerceros;
    }

    public List<Estructuras> getListValEstructuras() {
        if (listValEstructuras == null) {
            listValEstructuras = administrarNReportesSeguridad.listEstructuras();
        }
        return listValEstructuras;
    }

    public void setListValEstructuras(List<Estructuras> listValEstructuras) {
        this.listValEstructuras = listValEstructuras;
    }

    public List<SucursalesPila> getListValSucursales() {
        return listValSucursales;
    }

    public void setListValSucursales(List<SucursalesPila> listValSucursales) {
        this.listValSucursales = listValSucursales;
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

    public List<Empleados> getFiltrarListEmpleados() {
        return filtrarListEmpleados;
    }

    public void setFiltrarListEmpleados(List<Empleados> filtrarListEmpleados) {
        this.filtrarListEmpleados = filtrarListEmpleados;
    }

    public List<GruposConceptos> getFiltrarListGruposConceptos() {
        return filtrarListGruposConceptos;
    }

    public void setFiltrarListGruposConceptos(List<GruposConceptos> filtrarListGruposConceptos) {
        this.filtrarListGruposConceptos = filtrarListGruposConceptos;
    }

    public List<Empresas> getFiltrarListEmpresas() {
        return filtrarListEmpresas;
    }

    public void setFiltrarListEmpresas(List<Empresas> filtrarListEmpresas) {
        this.filtrarListEmpresas = filtrarListEmpresas;
    }

    public List<Terceros> getFiltrarListTerceros() {
        return filtrarListTerceros;
    }

    public void setFiltrarListTerceros(List<Terceros> filtrarListTerceros) {
        this.filtrarListTerceros = filtrarListTerceros;
    }

    public List<TiposTrabajadores> getFiltrarListTiposTrabajadores() {
        return filtrarListTiposTrabajadores;
    }

    public void setFiltrarListTiposTrabajadores(List<TiposTrabajadores> filtrarListTiposTrabajadores) {
        this.filtrarListTiposTrabajadores = filtrarListTiposTrabajadores;
    }

    public List<Estructuras> getFiltrarListEstructuras() {
        return filtrarListEstructuras;
    }

    public void setFiltrarListEstructuras(List<Estructuras> filtrarListEstructuras) {
        this.filtrarListEstructuras = filtrarListEstructuras;
    }

    public List<SucursalesPila> getFiltrarListSucursales() {
        return filtrarListSucursales;
    }

    public void setFiltrarListSucursales(List<SucursalesPila> filtrarListSucursales) {
        this.filtrarListSucursales = filtrarListSucursales;
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

}
