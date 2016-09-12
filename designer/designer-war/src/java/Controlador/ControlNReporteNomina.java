/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.*;
import InterfaceAdministrar.AdministarReportesInterface;
import InterfaceAdministrar.AdministrarNReportesNominaInterface;
import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
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
import utilidadesUI.PrimefacesContextUI;
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
    private ParametrosInformes parametroDeInforme;
    private ParametrosInformes nuevoParametroInforme;
    private List<Inforeportes> listaIR;
    private Inforeportes reporteSeleccionado;
    private List<Inforeportes> filtrarListIRU;
    private String reporteGenerar;
    private int bandera;
    private boolean aceptar;
    private Column codigoIR, reporteIR;
    //tipoIR;
    private int casilla;
    private ParametrosInformes parametroModificacion;
    private int posicionReporte;
    private String requisitosReporte;
    private InputText empleadoDesdeParametro, empleadoHastaParametro, estructuraParametro, tipoTrabajadorParametro, terceroParametro, grupoParametro;
    //private InputText empresaParametro,  procesoParametro, notasParametro, asociacionParametro, ubicacionGeograficaParametro, tipoAsociacionParametro;
    private SelectOneMenu estadoParametro;
    //LOV´s
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
    private List<UbicacionesGeograficas> listValUbicacionesGeograficas;
    private UbicacionesGeograficas ubicacionesGSeleccionado;
    private List<UbicacionesGeograficas> filtrarListUbicacionesGeograficas;
    private List<TiposAsociaciones> listValTiposAsociaciones;
    private TiposAsociaciones tiposASeleccionado;
    private List<TiposAsociaciones> filtrarListTiposAsociaciones;
    private List<Empresas> listValEmpresas;
    private Empresas empresaSeleccionada;
    private List<Empresas> filtrarListEmpresas;
    private List<Asociaciones> listValAsociaciones;
    private Asociaciones asociacionSeleccionado;
    private List<Asociaciones> filtrarListAsociaciones;
    private List<Terceros> listValTerceros;
    private Terceros terceroSeleccionado;
    private List<Terceros> filtrarListTerceros;
    private List<Procesos> listValProcesos;
    private Procesos procesoSeleccionado;
    private List<Procesos> filtrarListProcesos;
    private List<TiposTrabajadores> listValTiposTrabajadores;
    private TiposTrabajadores tipoTSeleccionado;
    private List<TiposTrabajadores> filtrarListTiposTrabajadores;
    private List<Estructuras> listValEstructuras;
    private Estructuras estructuraSeleccionada;
    private List<Estructuras> filtrarListEstructuras;
    ////
    private String grupo, ubiGeo, tipoAso, estructura, empresa, tipoTrabajador, tercero, proceso, asociacion, estado;
    private boolean permitirIndex, cambiosReporte;
    //ALTO SCROLL TABLA
    private String altoTabla;
    //private int indice;
    //EXPORTAR REPORTE
    private String pathReporteGenerado = null;
    private String nombreReporte, tipoReporte;
    //
    private List<Inforeportes> listaInfoReportesModificados;
    //
    private String color, decoracion;
    private String color2, decoracion2;
    //
    private int casillaInforReporte;
    //
    private Date fechaDesde, fechaHasta;
    private BigDecimal emplDesde, emplHasta;
    //
    private boolean activoMostrarTodos, activoBuscarReporte;
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

    public ControlNReporteNomina() {
        System.out.println(this.getClass().getName() + ".Constructor()");
        activoMostrarTodos = true;
        activoBuscarReporte = false;
        color = "black";
        decoracion = "none";
        color2 = "black";
        decoracion2 = "none";
        casillaInforReporte = -1;
        reporteSeleccionadoLOV = null;
        reporteSeleccionado = null;
        cambiosReporte = true;
        listaInfoReportesModificados = new ArrayList<Inforeportes>();
        parametroDeInforme = null;
        listaIR = null;
        bandera = 0;
        aceptar = true;
        casilla = -1;
        parametroModificacion = new ParametrosInformes();
        reporteGenerar = "";
        requisitosReporte = "";
        posicionReporte = -1;
        listValInforeportes = null;
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
        System.out.println(this.getClass().getName() + " fin del Constructor()");
    }

    @PostConstruct
    public void iniciarAdministradores() {
        System.out.println(this.getClass().getName() + ".iniciarAdministradores()");
        try {
            FacesContext contexto = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) contexto.getExternalContext().getSession(false);
            administarReportes.obtenerConexion(ses.getId());
            administrarNReportesNomina.obtenerConexion(ses.getId());

            System.out.println(this.getClass().getName() + " fin de iniciarAdministradores()");
        } catch (Exception e) {
            System.out.println("Error postconstruct controlNReporteNomina" + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void iniciarPagina() {
        System.out.println(this.getClass().getName() + ".iniciarPagina()");
        activoMostrarTodos = true;
        activoBuscarReporte = false;
//        listaIR = null;
        getListaIR();
        if (listaIR != null) {
            reporteSeleccionado = listaIR.get(0);
            modificarInfoRegistroReportes(listaIR.size());
        } else {
            modificarInfoRegistroReportes(0);
        }
        listValEmpleados = new ArrayList<Empleados>();
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
            PrimefacesContextUI.actualizar("formDialogos:requisitosReporte");
            PrimefacesContextUI.ejecutar("PF('requisitosReporte').show()");
        }
    }

    public void seleccionRegistro(Inforeportes reporte) {
        System.out.println(this.getClass().getName() + ".seleccionRegistro()");
        RequestContext context = RequestContext.getCurrentInstance();
        reporteSeleccionado = reporte;

        // Resalto Parametros Para Reporte
        defaultPropiedadesParametrosReporte();
        System.out.println("reporteSeleccionado: " + reporteSeleccionado);
        System.out.println("reporteSeleccionado.getFecdesde(): " + reporteSeleccionado.getFecdesde());
        if (reporteSeleccionado.getFecdesde().equals("SI")) {
            color = "red";
            PrimefacesContextUI.actualizar("formParametros");
        }
        System.out.println("reporteSeleccionado.getFechasta(): " + reporteSeleccionado.getFechasta());
        if (reporteSeleccionado.getFechasta().equals("SI")) {
            color2 = "red";
            PrimefacesContextUI.actualizar("formParametros");
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
                if (empleadoDesdeParametro.getStyle().contains(" color: red;")) {

                    System.out.println("reeemplazarr " + empleadoDesdeParametro.getStyle().replace(" color: red;", ""));
                    empleadoDesdeParametro.setStyle(empleadoDesdeParametro.getStyle().replace(" color: red;", ""));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        PrimefacesContextUI.actualizar("formParametros:empleadoDesdeParametro");

        System.out.println("reporteSeleccionado.getEmhasta(): " + reporteSeleccionado.getEmhasta());
        if (reporteSeleccionado.getEmhasta().equals("SI")) {
            empleadoHastaParametro = (InputText) FacesContext.getCurrentInstance().getViewRoot().findComponent("formParametros:empleadoHastaParametro");
            //empleadoHastaParametro.setStyle("position: absolute; top: 41px; left: 330px; height: 10px; font-size: 11px; width: 90px; color: red;");
            empleadoHastaParametro.setStyle(empleadoHastaParametro.getStyle() + "color: red;");
            PrimefacesContextUI.actualizar("formParametros:empleadoHastaParametro");
        }
        System.out.println("reporteSeleccionado.getGrupo(): " + reporteSeleccionado.getGrupo());
        if (reporteSeleccionado.getGrupo() != null && reporteSeleccionado.getGrupo().equals("SI")) {
            grupoParametro = (InputText) FacesContext.getCurrentInstance().getViewRoot().findComponent("formParametros:grupoParametro");
            //grupoParametro.setStyle("position: absolute; top: 89px; left: 150px; height: 10px; font-size: 11px; width: 130px; color: red;");
            grupoParametro.setStyle(grupoParametro.getStyle() + "color: red;");
            PrimefacesContextUI.actualizar("formParametros:grupoParametro");
        }
        System.out.println("reporteSeleccionado.getLocalizacion(): " + reporteSeleccionado.getLocalizacion());
        if (reporteSeleccionado.getLocalizacion().equals("SI")) {
            estructuraParametro = (InputText) FacesContext.getCurrentInstance().getViewRoot().findComponent("formParametros:estructuraParametro");
            //estructuraParametro.setStyle("position: absolute; top: 20px; left: 625px;height: 10px; font-size: 11px;width: 180px; color: red;");
            estructuraParametro.setStyle(estructuraParametro.getStyle() + "color: red;");
            PrimefacesContextUI.actualizar("formParametros:estructuraParametro");
        }
        System.out.println("reporteSeleccionado.getTipotrabajador(): " + reporteSeleccionado.getTipotrabajador());
        if (reporteSeleccionado.getTipotrabajador().equals("SI")) {
            tipoTrabajadorParametro = (InputText) FacesContext.getCurrentInstance().getViewRoot().findComponent("formParametros:tipoTrabajadorParametro");
            //tipoTrabajadorParametro.setStyle("position: absolute; top: 43px; left: 625px;height: 10px; font-size: 11px; width: 180px; color: red;");
            tipoTrabajadorParametro.setStyle(tipoTrabajadorParametro.getStyle() + "color: red;");
            PrimefacesContextUI.actualizar("formParametros:tipoTrabajadorParametro");
        }
        System.out.println("reporteSeleccionado.getTercero(): " + reporteSeleccionado.getTercero());
        if (reporteSeleccionado.getTercero().equals("SI")) {
            terceroParametro = (InputText) FacesContext.getCurrentInstance().getViewRoot().findComponent("formParametros:terceroParametro");
            //terceroParametro.setStyle("position: absolute; top: 66px; left: 625px; height: 10px; font-size: 11px; width: 180px; color: red;");
            terceroParametro.setStyle(terceroParametro.getStyle() + "color: red;");
            PrimefacesContextUI.actualizar("formParametros:terceroParametro");
        }
        System.out.println("reporteSeleccionado.getEstado(): " + reporteSeleccionado.getEstado());
        if (reporteSeleccionado.getEstado().equals("SI")) {
            estadoParametro = (SelectOneMenu) FacesContext.getCurrentInstance().getViewRoot().findComponent("formParametros:estadoParametro");
            estadoParametro.setStyleClass("selectOneMenuNReporteN");
            PrimefacesContextUI.actualizar("formParametros:estadoParametro");
        }
        PrimefacesContextUI.actualizar("formParametros");
        PrimefacesContextUI.actualizar("form:reportesLaboral");
    }

    public void dispararDialogoGuardarCambios() {
        System.out.println(this.getClass().getName() + ".dispararDialogoGuardarCambios()");
        RequestContext context = RequestContext.getCurrentInstance();
        PrimefacesContextUI.ejecutar("PF('confirmarGuardar').show()");

    }

    public void guardarYSalir() {
        System.out.println(this.getClass().getName() + ".guardarYSalir()");
        guardarCambios();
        salir();
    }

    public void guardarCambios() {
        System.out.println(this.getClass().getName() + ".guardarCambios()");
        RequestContext context = RequestContext.getCurrentInstance();
        System.out.println("cambiosReporte " + cambiosReporte);
        try {
            if (!cambiosReporte) {
                System.out.println("Entre a if (cambiosReporte == false)");
                if (parametroDeInforme.getUsuario() != null) {
                    if (parametroDeInforme.getCodigoempleadodesde() == null) {
                        parametroDeInforme.setCodigoempleadodesde(null);
                    }
                    if (parametroDeInforme.getCodigoempleadohasta() == null) {
                        parametroDeInforme.setCodigoempleadohasta(null);
                    }
                    if (parametroDeInforme.getGrupo().getSecuencia() == null) {
                        parametroDeInforme.setGrupo(null);
                    }
                    if (parametroDeInforme.getUbicaciongeografica().getSecuencia() == null) {
                        parametroDeInforme.setUbicaciongeografica(null);
                    }
                    if (parametroDeInforme.getLocalizacion().getSecuencia() == null) {
                        parametroDeInforme.setLocalizacion(null);
                    }
                    if (parametroDeInforme.getTipotrabajador().getSecuencia() == null) {
                        parametroDeInforme.setTipotrabajador(null);
                    }
                    if (parametroDeInforme.getTercero().getSecuencia() == null) {
                        parametroDeInforme.setTercero(null);
                    }
                    if (parametroDeInforme.getProceso().getSecuencia() == null) {
                        parametroDeInforme.setProceso(null);
                    }
                    if (parametroDeInforme.getAsociacion().getSecuencia() == null) {
                        parametroDeInforme.setAsociacion(null);
                    }
                    if (parametroDeInforme.getTipoasociacion().getSecuencia() == null) {
                        parametroDeInforme.setTipoasociacion(null);
                    }
                    administrarNReportesNomina.modificarParametrosInformes(parametroDeInforme);
                }
                if (!listaInfoReportesModificados.isEmpty()) {
                    administrarNReportesNomina.guardarCambiosInfoReportes(listaInfoReportesModificados);
                }
                cambiosReporte = true;
                FacesMessage msg = new FacesMessage("Información", "Los datos se guardaron con Éxito.");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                PrimefacesContextUI.actualizar("form:growl");
                PrimefacesContextUI.actualizar("form:ACEPTAR");
            }
        } catch (Exception e) {
            System.out.println("Error en guardar Cambios Controlador : " + e.toString());
            FacesMessage msg = new FacesMessage("Información", "ha ocurrido un error en el guardado, intente nuevamente");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            PrimefacesContextUI.actualizar("form:growl");
            PrimefacesContextUI.actualizar("form:ACEPTAR");
        }
    }

    public void modificarParametroInforme() {
        System.out.println(this.getClass().getName() + ".modificarParametroInforme()");

        if (parametroDeInforme.getCodigoempleadodesde() != null && parametroDeInforme.getCodigoempleadohasta() != null
                && parametroDeInforme.getFechadesde() != null && parametroDeInforme.getFechahasta() != null) {
            if (parametroDeInforme.getFechadesde().before(parametroDeInforme.getFechahasta())) {
                parametroModificacion = parametroDeInforme;
                cambiosReporte = false;
                RequestContext context = RequestContext.getCurrentInstance();
                PrimefacesContextUI.actualizar("form:ACEPTAR");
            } else {
                parametroDeInforme.setFechadesde(fechaDesde);
                parametroDeInforme.setFechahasta(fechaHasta);
                RequestContext context = RequestContext.getCurrentInstance();
                PrimefacesContextUI.actualizar("formParametros");
                PrimefacesContextUI.ejecutar("PF('errorFechas').show()");
            }
        } else {
            parametroDeInforme.setCodigoempleadodesde(emplDesde);
            parametroDeInforme.setCodigoempleadohasta(emplHasta);
            parametroDeInforme.setFechadesde(fechaDesde);
            parametroDeInforme.setFechahasta(fechaHasta);
            parametroDeInforme.getGrupo().setDescripcion(grupo);
            parametroDeInforme.getLocalizacion().setNombre(estructura);
            parametroDeInforme.getTipotrabajador().setNombre(tipoTrabajador);
            parametroDeInforme.getTercero().setNombre(tercero);
            parametroDeInforme.setEstadosolucionnodo(estado);
            RequestContext context = RequestContext.getCurrentInstance();
            PrimefacesContextUI.actualizar("formParametros");
            PrimefacesContextUI.ejecutar("PF('errorRegNew').show()");
        }
    }

    public void posicionCelda(int i) {
        System.out.println(this.getClass().getName() + ".posicionCelda()");
        casilla = i;
        if (permitirIndex == true) {
            casillaInforReporte = -1;
            emplDesde = parametroDeInforme.getCodigoempleadodesde();
            fechaDesde = parametroDeInforme.getFechadesde();
            emplHasta = parametroDeInforme.getCodigoempleadohasta();
            fechaHasta = parametroDeInforme.getFechahasta();
            ubiGeo = parametroDeInforme.getUbicaciongeografica().getDescripcion();
            tipoAso = parametroDeInforme.getTipoasociacion().getDescripcion();
            empresa = parametroDeInforme.getEmpresa().getNombre();
            proceso = parametroDeInforme.getProceso().getDescripcion();
            asociacion = parametroDeInforme.getAsociacion().getDescripcion();
            grupo = parametroDeInforme.getGrupo().getDescripcion();
            estructura = parametroDeInforme.getLocalizacion().getNombre();
            tipoTrabajador = parametroDeInforme.getTipotrabajador().getNombre();
            tercero = parametroDeInforme.getTercero().getNombre();
            estado = parametroDeInforme.getEstadosolucionnodo();
        }
    }

    public void editarCelda() {
        System.out.println(this.getClass().getName() + ".editarCelda()");
        RequestContext context = RequestContext.getCurrentInstance();
        if (casilla >= 1) {
            if (casilla == 1) {
                PrimefacesContextUI.actualizar("formDialogos:editarFechaDesde");
                PrimefacesContextUI.ejecutar("PF('editarFechaDesde').show()");
            }
            if (casilla == 2) {
                PrimefacesContextUI.actualizar("formDialogos:empleadoDesde");
                PrimefacesContextUI.ejecutar("PF('empleadoDesde').show()");
            }
            if (casilla == 4) {
                PrimefacesContextUI.actualizar("formDialogos:grupoDesde");
                PrimefacesContextUI.ejecutar("PF('grupoDesde').show()");
            }
            if (casilla == 5) {
                PrimefacesContextUI.actualizar("formDialogos:ubicacionGeografica");
                PrimefacesContextUI.ejecutar("PF('ubicacionGeografica').show()");
            }
            if (casilla == 6) {
                PrimefacesContextUI.actualizar("formDialogos:tipoAsociacion");
                PrimefacesContextUI.ejecutar("PF('tipoAsociacion').show()");
            }
            if (casilla == 7) {
                PrimefacesContextUI.actualizar("formDialogos:editarFechaHasta");
                PrimefacesContextUI.ejecutar("PF('editarFechaHasta').show()");
            }
            if (casilla == 8) {
                PrimefacesContextUI.actualizar("formDialogos:empleadoHasta");
                PrimefacesContextUI.ejecutar("PF('empleadoHasta').show()");
            }
            if (casilla == 10) {
                PrimefacesContextUI.actualizar("formDialogos:empresa");
                PrimefacesContextUI.ejecutar("PF('empresa').show()");
            }
            if (casilla == 11) {
                PrimefacesContextUI.actualizar("formDialogos:estructura");
                PrimefacesContextUI.ejecutar("PF('estructura').show()");
            }
            if (casilla == 12) {
                PrimefacesContextUI.actualizar("formDialogos:tipoTrabajador");
                PrimefacesContextUI.ejecutar("PF('tipoTrabajador').show()");
            }
            if (casilla == 13) {
                PrimefacesContextUI.actualizar("formDialogos:tercero");
                PrimefacesContextUI.ejecutar("PF('tercero').show()");
            }
            if (casilla == 14) {
                PrimefacesContextUI.actualizar("formDialogos:proceso");
                PrimefacesContextUI.ejecutar("PF('proceso').show()");
            }
            if (casilla == 15) {
                PrimefacesContextUI.actualizar("formDialogos:notas");
                PrimefacesContextUI.ejecutar("PF('notas').show()");
            }
            if (casilla == 16) {
                PrimefacesContextUI.actualizar("formDialogos:asociacion");
                PrimefacesContextUI.ejecutar("PF('asociacion').show()");
            }
            casilla = -1;
        }
        if (casillaInforReporte >= 1) {
            if (casillaInforReporte == 1) {
                PrimefacesContextUI.actualizar("formParametros:infoReporteCodigoD");
                PrimefacesContextUI.ejecutar("PF('infoReporteCodigoD').show()");
            }
            if (casillaInforReporte == 2) {
                PrimefacesContextUI.actualizar("formParametros:infoReporteNombreD");
                PrimefacesContextUI.ejecutar("PF('infoReporteNombreD').show()");
            }
            casillaInforReporte = -1;
        }
    }

    public void autocompletarGeneral(String campoConfirmar, String valorConfirmar) {
        System.out.println(this.getClass().getName() + ".autocompletarGeneral()");
        RequestContext context = RequestContext.getCurrentInstance();
        int indiceUnicoElemento = -1;
        int coincidencias = 0;
        if (campoConfirmar.equalsIgnoreCase("GRUPO")) {
            if (!valorConfirmar.isEmpty()) {
                parametroDeInforme.getGrupo().setDescripcion(grupo);
                for (int i = 0; i < listValGruposConceptos.size(); i++) {
                    if (listValGruposConceptos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    parametroDeInforme.setGrupo(listValGruposConceptos.get(indiceUnicoElemento));
                    parametroModificacion = parametroDeInforme;
                    listValGruposConceptos.clear();
                    getListValGruposConceptos();
                    cambiosReporte = false;
                    PrimefacesContextUI.actualizar("form:ACEPTAR");
                } else {
                    permitirIndex = false;
                    PrimefacesContextUI.actualizar("form:GruposConceptosDialogo");
                    PrimefacesContextUI.ejecutar("PF('GruposConceptosDialogo').show()");
                }
            } else {
                parametroDeInforme.setGrupo(new GruposConceptos());
                parametroModificacion = parametroDeInforme;
                listValGruposConceptos.clear();
                getListValGruposConceptos();
                cambiosReporte = false;
                PrimefacesContextUI.actualizar("form:ACEPTAR");
            }
        }
        if (campoConfirmar.equalsIgnoreCase("UBIGEO")) {
            if (!valorConfirmar.isEmpty()) {
                parametroDeInforme.getUbicaciongeografica().setDescripcion(ubiGeo);
                for (int i = 0; i < listValUbicacionesGeograficas.size(); i++) {
                    if (listValUbicacionesGeograficas.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    parametroDeInforme.setUbicaciongeografica(listValUbicacionesGeograficas.get(indiceUnicoElemento));
                    parametroModificacion = parametroDeInforme;
                    listValUbicacionesGeograficas.clear();
                    getListValUbicacionesGeograficas();
                    cambiosReporte = false;
                    PrimefacesContextUI.actualizar("form:ACEPTAR");
                } else {
                    permitirIndex = false;
                    PrimefacesContextUI.actualizar("form:UbiGeograficaDialogo");
                    PrimefacesContextUI.ejecutar("PF('UbiGeograficaDialogo').show()");
                }
            } else {
                parametroDeInforme.setUbicaciongeografica(new UbicacionesGeograficas());
                parametroModificacion = parametroDeInforme;
                listValUbicacionesGeograficas.clear();
                getListValUbicacionesGeograficas();
                cambiosReporte = false;
                PrimefacesContextUI.actualizar("form:ACEPTAR");
            }
        }
        if (campoConfirmar.equalsIgnoreCase("EMPRESA")) {
            if (!valorConfirmar.isEmpty()) {
                parametroDeInforme.getEmpresa().setNombre(empresa);
                for (int i = 0; i < listValEmpresas.size(); i++) {
                    if (listValEmpresas.get(i).getNombre().startsWith(valorConfirmar)) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    parametroDeInforme.setEmpresa(listValEmpresas.get(indiceUnicoElemento));
                    parametroModificacion = parametroDeInforme;
                    listValEmpresas.clear();
                    getListValEmpresas();
                    cambiosReporte = false;
                    PrimefacesContextUI.actualizar("form:ACEPTAR");
                } else {
                    permitirIndex = false;
                    PrimefacesContextUI.actualizar("form:EmpresaDialogo");
                    PrimefacesContextUI.ejecutar("PF('EmpresaDialogo').show()");
                }
            } else {
                parametroDeInforme.setEmpresa(new Empresas());
                parametroModificacion = parametroDeInforme;
                listValEmpresas.clear();
                getListValEmpresas();
                cambiosReporte = false;
                PrimefacesContextUI.actualizar("form:ACEPTAR");
            }
        }
        if (campoConfirmar.equalsIgnoreCase("TIPOASO")) {
            if (!valorConfirmar.isEmpty()) {
                parametroDeInforme.getTipoasociacion().setDescripcion(tipoAso);
                for (int i = 0; i < listValTiposAsociaciones.size(); i++) {
                    if (listValTiposAsociaciones.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    parametroDeInforme.setTipoasociacion(listValTiposAsociaciones.get(indiceUnicoElemento));
                    parametroModificacion = parametroDeInforme;
                    listValTiposAsociaciones.clear();
                    getListValTiposAsociaciones();
                    cambiosReporte = false;
                    PrimefacesContextUI.actualizar("form:ACEPTAR");
                } else {
                    permitirIndex = false;
                    PrimefacesContextUI.actualizar("form:TipoAsociacionDialogo");
                    PrimefacesContextUI.ejecutar("PF('TipoAsociacionDialogo').show()");
                }
            } else {
                parametroDeInforme.setTipoasociacion(new TiposAsociaciones());
                parametroModificacion = parametroDeInforme;
                listValTiposAsociaciones.clear();
                getListValTiposAsociaciones();
                cambiosReporte = false;
                PrimefacesContextUI.actualizar("form:ACEPTAR");
            }
        }
        if (campoConfirmar.equalsIgnoreCase("ESTRUCTURA")) {
            if (!valorConfirmar.isEmpty()) {
                parametroDeInforme.getLocalizacion().setNombre(estructura);
                for (int i = 0; i < listValEstructuras.size(); i++) {
                    if (listValEstructuras.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    parametroDeInforme.setLocalizacion(listValEstructuras.get(indiceUnicoElemento));
                    parametroModificacion = parametroDeInforme;
                    listValEstructuras.clear();
                    getListValEstructuras();
                    cambiosReporte = false;
                    PrimefacesContextUI.actualizar("form:ACEPTAR");
                } else {
                    permitirIndex = false;
                    PrimefacesContextUI.actualizar("form:EstructuraDialogo");
                    PrimefacesContextUI.ejecutar("PF('EstructuraDialogo').show()");
                }
            } else {
                parametroDeInforme.setLocalizacion(new Estructuras());
                parametroModificacion = parametroDeInforme;
                listValEstructuras.clear();
                getListValEstructuras();
                cambiosReporte = false;
                PrimefacesContextUI.actualizar("form:ACEPTAR");
            }
        }
        if (campoConfirmar.equalsIgnoreCase("TIPOTRABAJADOR")) {
            if (!valorConfirmar.isEmpty()) {
                parametroDeInforme.getTipotrabajador().setNombre(tipoTrabajador);
                for (int i = 0; i < listValTiposTrabajadores.size(); i++) {
                    if (listValTiposTrabajadores.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    parametroDeInforme.setTipotrabajador(listValTiposTrabajadores.get(indiceUnicoElemento));
                    parametroModificacion = parametroDeInforme;
                    listValTiposTrabajadores.clear();
                    getListValTiposTrabajadores();
                    cambiosReporte = false;
                    PrimefacesContextUI.actualizar("form:ACEPTAR");
                } else {
                    permitirIndex = false;
                    PrimefacesContextUI.actualizar("form:TipoTrabajadorDialogo");
                    PrimefacesContextUI.ejecutar("PF('TipoTrabajadorDialogo').show()");
                }
            } else {
                parametroDeInforme.setTipotrabajador(new TiposTrabajadores());
                parametroModificacion = parametroDeInforme;
                listValTiposTrabajadores.clear();
                getListValTiposTrabajadores();
                cambiosReporte = false;
                PrimefacesContextUI.actualizar("form:ACEPTAR");
            }
        }
        if (campoConfirmar.equalsIgnoreCase("TERCERO")) {
            if (!valorConfirmar.isEmpty()) {
                parametroDeInforme.getTercero().setNombre(tercero);
                for (int i = 0; i < listValTerceros.size(); i++) {
                    if (listValTerceros.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    parametroDeInforme.setTercero(listValTerceros.get(indiceUnicoElemento));
                    parametroModificacion = parametroDeInforme;
                    listValTerceros.clear();
                    getListValTerceros();
                    cambiosReporte = false;
                    PrimefacesContextUI.actualizar("form:ACEPTAR");
                } else {
                    permitirIndex = false;
                    PrimefacesContextUI.actualizar("form:TerceroDialogo");
                    PrimefacesContextUI.ejecutar("PF('TerceroDialogo').show()");
                }
            } else {
                parametroDeInforme.setTercero(new Terceros());
                parametroModificacion = parametroDeInforme;
                listValTerceros.clear();
                getListValTerceros();
                cambiosReporte = false;
                PrimefacesContextUI.actualizar("form:ACEPTAR");
            }
        }
        if (campoConfirmar.equalsIgnoreCase("PROCESO")) {
            if (!valorConfirmar.isEmpty()) {
                parametroDeInforme.getProceso().setDescripcion(proceso);
                for (int i = 0; i < listValProcesos.size(); i++) {
                    if (listValProcesos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    parametroDeInforme.setProceso(listValProcesos.get(indiceUnicoElemento));
                    parametroModificacion = parametroDeInforme;
                    listValProcesos.clear();
                    getListValProcesos();
                    cambiosReporte = false;
                    PrimefacesContextUI.actualizar("form:ACEPTAR");
                } else {
                    permitirIndex = false;
                    PrimefacesContextUI.actualizar("form:ProcesoDialogo");
                    PrimefacesContextUI.ejecutar("PF('ProcesoDialogo').show()");
                }
            } else {
                parametroDeInforme.setProceso(new Procesos());
                parametroModificacion = parametroDeInforme;
                listValProcesos.clear();
                getListValProcesos();
                cambiosReporte = false;
                PrimefacesContextUI.actualizar("form:ACEPTAR");
            }
        }
        if (campoConfirmar.equalsIgnoreCase("ASOCIACION")) {
            if (!valorConfirmar.isEmpty()) {
                parametroDeInforme.getAsociacion().setDescripcion(asociacion);
                for (int i = 0; i < listValAsociaciones.size(); i++) {
                    if (listValAsociaciones.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    parametroDeInforme.setAsociacion(listValAsociaciones.get(indiceUnicoElemento));
                    parametroModificacion = parametroDeInforme;
                    listValAsociaciones.clear();
                    getListValAsociaciones();
                    cambiosReporte = false;
                    PrimefacesContextUI.actualizar("form:ACEPTAR");
                } else {
                    permitirIndex = false;
                    PrimefacesContextUI.actualizar("form:AsociacionDialogo");
                    PrimefacesContextUI.ejecutar("PF('AsociacionDialogo').show()");
                }
            } else {
                parametroDeInforme.setAsociacion(new Asociaciones());
                parametroModificacion = parametroDeInforme;
                listValAsociaciones.clear();
                getListValAsociaciones();
                cambiosReporte = false;
                PrimefacesContextUI.actualizar("form:ACEPTAR");
            }
        }
        if (campoConfirmar.equalsIgnoreCase("DESDE")) {
            System.out.println("valorConfirmar " + valorConfirmar);
            if (!valorConfirmar.isEmpty() || !"0".equals(valorConfirmar)) {
                parametroDeInforme.getCodigoempleadodesde();
                for (int i = 0; i < listValEmpleados.size(); i++) {
                    if (listValEmpleados.get(i).getCodigoempleadoSTR().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                System.out.println("Coincidencias: " + coincidencias);
                System.out.println("Entre a if (coincidencias == 1 && 0.equals(valorConfirmar))");
                if (coincidencias == 1 && "0".equals(valorConfirmar)) {
                    System.out.println("Entre a if (coincidencias == 1 && 0.equals(valorConfirmar))");
                    parametroDeInforme.setCodigoempleadodesde(listValEmpleados.get(indiceUnicoElemento).getCodigoempleado());
                    parametroModificacion = parametroDeInforme;
                    listValEmpleados.clear();
                    getListValEmpleados();
                    cambiosReporte = false;
                    PrimefacesContextUI.actualizar("form:ACEPTAR");
                } else {
                    System.out.println("Entre al else");
                    permitirIndex = false;
                    PrimefacesContextUI.actualizar("form:EmpleadoDesdeDialogo");
                    PrimefacesContextUI.ejecutar("PF('EmpleadoDesdeDialogo').show()");
                }
            } else {
                parametroDeInforme.setCodigoempleadodesde(new BigDecimal("0"));
                parametroModificacion = parametroDeInforme;
                listValEmpleados.clear();
                getListValEmpleados();
                cambiosReporte = false;
                PrimefacesContextUI.actualizar("form:ACEPTAR");
            }
        }
        if (campoConfirmar.equalsIgnoreCase("HASTA")) {
            System.out.println("ControlNReporteNomina.autocompletarGeneral");
            System.out.println("campoConfirmar.equalsIgnoreCase(HASTA)");
            System.out.println("Codigoempleadohasta: " + parametroDeInforme.getCodigoempleadohasta());
            if (campoConfirmar.equalsIgnoreCase("HASTA")) {
                if (!valorConfirmar.isEmpty()) {
                    parametroDeInforme.setCodigoempleadohasta(emplHasta);
                    parametroDeInforme.getCodigoempleadohasta();
                    for (int i = 0; i < listValEmpleados.size(); i++) {
                        if (listValEmpleados.get(i).getCodigoempleadoSTR().startsWith(valorConfirmar.toUpperCase())) {
                            indiceUnicoElemento = i;
                            coincidencias++;
                        }
                    }
                    if (coincidencias == 1) {
                        parametroDeInforme.setCodigoempleadohasta(listValEmpleados.get(indiceUnicoElemento).getCodigoempleado());
                        parametroModificacion = parametroDeInforme;
                        listValEmpleados.clear();
                        getListValEmpleados();
                        cambiosReporte = false;
                        PrimefacesContextUI.actualizar("form:ACEPTAR");
                    } else {
                        permitirIndex = false;
                        if ((listValEmpleados == null) || listValEmpleados.isEmpty()) {
                            listValEmpleados = null;
                        }
                        PrimefacesContextUI.actualizar("form:EmpleadoHastaDialogo");
                        PrimefacesContextUI.ejecutar("PF('EmpleadoHastaDialogo').show()");
                    }
                } else {
                    System.out.println("Entre al else en  ControlNReporteNomina.autocompletarGeneral");
                    parametroDeInforme.setCodigoempleadohasta(new BigDecimal("9999999999999999999999"));
                    parametroModificacion = parametroDeInforme;
                    listValEmpleados.clear();
                    getListValEmpleados();
                    cambiosReporte = false;
                    PrimefacesContextUI.actualizar("form:ACEPTAR");
                }
            }
        }

    }

    public void listaValoresBoton() {
        System.out.println(this.getClass().getName() + ".listaValoresBoton()");
        RequestContext context = RequestContext.getCurrentInstance();
        if (casilla == 2) {
            if ((listValEmpleados == null) || listValEmpleados.isEmpty()) {
                listValEmpleados = null;
            }
            PrimefacesContextUI.actualizar("form:EmpleadoDesdeDialogo");
            PrimefacesContextUI.ejecutar("PF('EmpleadoDesdeDialogo').show()");
            modificarInfoRegistroEmpleadoD(listValEmpleados.size());
        }
        if (casilla == 4) {
            PrimefacesContextUI.actualizar("form:GruposConceptosDialogo");
            PrimefacesContextUI.ejecutar("PF('GruposConceptosDialogo').show()");
            modificarInfoRegistroGrupo(listValGruposConceptos.size());
        }
        if (casilla == 5) {
            PrimefacesContextUI.actualizar("form:UbiGeograficaDialogo");
            PrimefacesContextUI.ejecutar("PF('UbiGeograficaDialogo').show()");
            modificarInfoRegistroUbicacion(listValUbicacionesGeograficas.size());
        }
        if (casilla == 6) {
            PrimefacesContextUI.actualizar("form:TipoAsociacionDialogo");
            PrimefacesContextUI.ejecutar("PF('TipoAsociacionDialogo').show()");
            modificarInfoRegistroTipoAsociacion(listValTiposAsociaciones.size());
        }
        if (casilla == 8) {
            PrimefacesContextUI.actualizar("form:EmpleadoHastaDialogo");
            PrimefacesContextUI.ejecutar("PF('EmpleadoHastaDialogo').show()");
            modificarInfoRegistroEmpleadoH(listValEmpleados.size());
        }
        if (casilla == 10) {
            PrimefacesContextUI.actualizar("form:EmpresaDialogo");
            PrimefacesContextUI.ejecutar("PF('EmpresaDialogo').show()");
            modificarInfoRegistroEmpresa(listValEmpresas.size());
        }
        if (casilla == 11) {
            PrimefacesContextUI.actualizar("form:EstructuraDialogo");
            PrimefacesContextUI.ejecutar("PF('EstructuraDialogo').show()");
            modificarInfoRegistroEstructura(listValEstructuras.size());
        }
        if (casilla == 12) {
            PrimefacesContextUI.actualizar("form:TipoTrabajadorDialogo");
            PrimefacesContextUI.ejecutar("PF('TipoTrabajadorDialogo').show()");
            modificarInfoRegistroTipoTrabajador(listValTiposTrabajadores.size());
        }
        if (casilla == 13) {
            PrimefacesContextUI.actualizar("form:TerceroDialogo");
            PrimefacesContextUI.ejecutar("PF('TerceroDialogo').show()");
            modificarInfoRegistroTercero(listValTerceros.size());
        }
        if (casilla == 14) {
            PrimefacesContextUI.actualizar("form:ProcesoDialogo");
            PrimefacesContextUI.ejecutar("PF('ProcesoDialogo').show()");
            modificarInfoRegistroProceso(listValProcesos.size());
        }
        if (casilla == 16) {
            PrimefacesContextUI.actualizar("form:AsociacionDialogo");
            PrimefacesContextUI.ejecutar("PF('AsociacionDialogo').show()");
            modificarInfoRegistroAsociacion(listValAsociaciones.size());
        }
    }

    public void dialogosParametros(int pos) {
        System.out.println(this.getClass().getName() + ".dialogosParametros()");
        RequestContext context = RequestContext.getCurrentInstance();
        if (pos == 2) {
            if ((listValEmpleados == null) || listValEmpleados.isEmpty()) {
                listValEmpleados = null;
            }
            PrimefacesContextUI.actualizar("form:EmpleadoDesdeDialogo");
            PrimefacesContextUI.ejecutar("PF('EmpleadoDesdeDialogo').show()");
            modificarInfoRegistroEmpleadoD(listValEmpleados.size());
        }
        if (pos == 4) {
            PrimefacesContextUI.actualizar("form:GruposConceptosDialogo");
            PrimefacesContextUI.ejecutar("PF('GruposConceptosDialogo').show()");
            modificarInfoRegistroGrupo(listValGruposConceptos.size());
        }
        if (pos == 5) {
            PrimefacesContextUI.actualizar("form:UbiGeograficaDialogo");
            PrimefacesContextUI.ejecutar("PF('UbiGeograficaDialogo').show()");
            modificarInfoRegistroUbicacion(listValUbicacionesGeograficas.size());
        }
        if (pos == 6) {
            PrimefacesContextUI.actualizar("form:TipoAsociacionDialogo");
            PrimefacesContextUI.ejecutar("PF('TipoAsociacionDialogo').show()");
            modificarInfoRegistroTipoAsociacion(listValTiposAsociaciones.size());
        }
        if (pos == 8) {
            PrimefacesContextUI.actualizar("form:EmpleadoHastaDialogo");
            PrimefacesContextUI.ejecutar("PF('EmpleadoHastaDialogo').show()");
            modificarInfoRegistroEmpleadoH(listValEmpleados.size());
        }
        if (pos == 10) {
            PrimefacesContextUI.actualizar("form:EmpresaDialogo");
            PrimefacesContextUI.ejecutar("PF('EmpresaDialogo').show()");
            modificarInfoRegistroEmpresa(listValEmpresas.size());
        }
        if (pos == 11) {
            PrimefacesContextUI.actualizar("form:EstructuraDialogo");
            PrimefacesContextUI.ejecutar("PF('EstructuraDialogo').show()");
            modificarInfoRegistroEstructura(listValEstructuras.size());
        }
        if (pos == 12) {
            PrimefacesContextUI.actualizar("form:TipoTrabajadorDialogo");
            PrimefacesContextUI.ejecutar("PF('TipoTrabajadorDialogo').show()");
            modificarInfoRegistroTipoTrabajador(listValTiposTrabajadores.size());
        }
        if (pos == 13) {
            PrimefacesContextUI.actualizar("form:TerceroDialogo");
            PrimefacesContextUI.ejecutar("PF('TerceroDialogo').show()");
            modificarInfoRegistroTercero(listValTerceros.size());
        }
        if (pos == 14) {
            PrimefacesContextUI.actualizar("form:ProcesoDialogo");
            PrimefacesContextUI.ejecutar("PF('ProcesoDialogo').show()");
            modificarInfoRegistroProceso(listValProcesos.size());
        }
        if (pos == 16) {
            PrimefacesContextUI.actualizar("form:AsociacionDialogo");
            PrimefacesContextUI.ejecutar("PF('AsociacionDialogo').show()");
            modificarInfoRegistroAsociacion(listValAsociaciones.size());
        }

    }

    public void activarAceptar() {
        System.out.println(this.getClass().getName() + ".activarAceptar()");
        aceptar = false;
    }

    public void actualizarEmplDesde() {
        System.out.println(this.getClass().getName() + ".actualizarEmplDesde()");
        permitirIndex = true;
        parametroDeInforme.setCodigoempleadodesde(empleadoSeleccionado.getCodigoempleado());
        parametroModificacion = parametroDeInforme;
        cambiosReporte = false;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovEmpleadoDesde:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovEmpleadoDesde').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('EmpleadoDesdeDialogo').hide()");

        PrimefacesContextUI.actualizar("form:ACEPTAR");
        PrimefacesContextUI.actualizar("formParametros:empleadoDesdeParametro");
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
        PrimefacesContextUI.ejecutar("PF('lovEmpleadoDesde').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('EmpleadoDesdeDialogo').hide()");
    }

    public void actualizarEmplHasta() {
        System.out.println(this.getClass().getName() + ".actualizarEmplHasta()");
        permitirIndex = true;
        parametroDeInforme.setCodigoempleadohasta(empleadoSeleccionado.getCodigoempleado());
        parametroModificacion = parametroDeInforme;
        cambiosReporte = false;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovEmpleadoHasta:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovEmpleadoHasta').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('EmpleadoHastaDialogo').hide()");

        PrimefacesContextUI.actualizar("form:ACEPTAR");
        PrimefacesContextUI.actualizar("formParametros:empleadoHastaParametro");
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
        PrimefacesContextUI.ejecutar("PF('lovEmpleadoHasta').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('EmpleadoHastaDialogo').hide()");
    }

    public void actualizarGrupo() {
        System.out.println(this.getClass().getName() + ".actualizarGrupo()");
        permitirIndex = true;
        parametroDeInforme.setGrupo(grupoCSeleccionado);
        parametroModificacion = parametroDeInforme;
        cambiosReporte = false;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovGruposConceptos:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovGruposConceptos').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('GruposConceptosDialogo').hide()");

        PrimefacesContextUI.actualizar("form:ACEPTAR");
        PrimefacesContextUI.actualizar("formParametros:grupoParametro");
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
        PrimefacesContextUI.ejecutar("PF('lovGruposConceptos').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('GruposConceptosDialogo').hide()");
    }

    public void actualizarUbicacionGeografica() {
        System.out.println(this.getClass().getName() + ".actualizarUbicacionGeografica()");
        permitirIndex = true;
        parametroDeInforme.setUbicaciongeografica(ubicacionesGSeleccionado);
        parametroModificacion = parametroDeInforme;
        cambiosReporte = false;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovUbicacionGeografica:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovUbicacionGeografica').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('UbiGeograficaDialogo').hide()");

        PrimefacesContextUI.actualizar("form:ACEPTAR");
        PrimefacesContextUI.actualizar("formParametros:ubicacionGeograficaParametro");
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
        PrimefacesContextUI.ejecutar("PF('lovUbicacionGeografica').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('UbiGeograficaDialogo').hide()");
    }

    public void actualizarTipoAsociacion() {
        System.out.println(this.getClass().getName() + ".actualizarTipoAsociacion()");
        permitirIndex = true;
        parametroDeInforme.setTipoasociacion(tiposASeleccionado);
        parametroModificacion = parametroDeInforme;
        cambiosReporte = false;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovTipoAsociacion:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovTipoAsociacion').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('TipoAsociacionDialogo').hide()");

        PrimefacesContextUI.actualizar("form:ACEPTAR");
        PrimefacesContextUI.actualizar("formParametros:tipoAsociacionParametro");
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
        PrimefacesContextUI.ejecutar("PF('lovTipoAsociacion').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('TipoAsociacionDialogo').hide()");
    }

    public void actualizarEmpresa() {
        System.out.println(this.getClass().getName() + ".actualizarEmpresa()");
        permitirIndex = true;
        parametroDeInforme.setEmpresa(empresaSeleccionada);
        parametroModificacion = parametroDeInforme;
        cambiosReporte = false;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovEmpresa:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovEmpresa').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('EmpresaDialogo').hide()");

        PrimefacesContextUI.actualizar("form:ACEPTAR");
        PrimefacesContextUI.actualizar("formParametros:empresaParametro");
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
        PrimefacesContextUI.ejecutar("PF('lovEmpresa').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('EmpresaDialogo').hide()");
    }

    public void actualizarEstructura() {
        System.out.println(this.getClass().getName() + ".actualizarEstructura()");
        parametroDeInforme.setLocalizacion(estructuraSeleccionada);
        parametroModificacion = parametroDeInforme;
        cambiosReporte = false;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovEstructura:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovEstructura').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('EstructuraDialogo').hide()");

        PrimefacesContextUI.actualizar("form:ACEPTAR");
        PrimefacesContextUI.actualizar("formParametros:estructuraParametro");
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
        PrimefacesContextUI.ejecutar("PF('lovEstructura').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('EstructuraDialogo').hide()");
    }

    public void actualizarTipoTrabajador() {
        System.out.println(this.getClass().getName() + ".actualizarTipoTrabajador()");
        parametroDeInforme.setTipotrabajador(tipoTSeleccionado);
        parametroModificacion = parametroDeInforme;
        cambiosReporte = false;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovTipoTrabajador:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovTipoTrabajador').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('TipoTrabajadorDialogo').hide()");

        PrimefacesContextUI.actualizar("form:ACEPTAR");
        PrimefacesContextUI.actualizar("formParametros:tipoTrabajadorParametro");
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
        PrimefacesContextUI.ejecutar("PF('lovTipoTrabajador').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('TipoTrabajadorDialogo').hide()");
    }

    public void actualizarTercero() {
        System.out.println(this.getClass().getName() + ".actualizarTercero()");
        permitirIndex = true;
        parametroDeInforme.setTercero(terceroSeleccionado);
        parametroModificacion = parametroDeInforme;
        cambiosReporte = false;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovTercero:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovTercero').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('TerceroDialogo').hide()");

        PrimefacesContextUI.actualizar("form:ACEPTAR");
        PrimefacesContextUI.actualizar("formParametros:terceroParametro");
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
        PrimefacesContextUI.ejecutar("PF('lovTercero').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('TerceroDialogo').hide()");
    }

    public void actualizarProceso() {
        System.out.println(this.getClass().getName() + ".actualizarProceso()");
        permitirIndex = true;
        parametroDeInforme.setProceso(procesoSeleccionado);
        parametroModificacion = parametroDeInforme;
        cambiosReporte = false;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovProceso:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovProceso').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('ProcesoDialogo').hide()");

        PrimefacesContextUI.actualizar("form:ACEPTAR");
        PrimefacesContextUI.actualizar("formParametros:procesoParametro");
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
        PrimefacesContextUI.ejecutar("PF('lovProceso').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('ProcesoDialogo').hide()");
    }

    public void actualizarAsociacion() {
        System.out.println(this.getClass().getName() + ".actualizarAsociacion()");
        permitirIndex = true;
        parametroDeInforme.setAsociacion(asociacionSeleccionado);
        parametroModificacion = parametroDeInforme;
        cambiosReporte = false;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovAsociacion:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovAsociacion').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('AsociacionDialogo').hide()");

        PrimefacesContextUI.actualizar("form:ACEPTAR");
        PrimefacesContextUI.actualizar("formParametros:asociacionParametro");
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
        PrimefacesContextUI.ejecutar("PF('lovAsociacion').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('AsociacionDialogo').hide()");
    }

    public void mostrarDialogoGenerarReporte() {
        System.out.println(this.getClass().getName() + ".mostrarDialogoGenerarReporte()");
        RequestContext context = RequestContext.getCurrentInstance();
        PrimefacesContextUI.actualizar("formDialogos:reporteAGenerar");
        PrimefacesContextUI.ejecutar("PF('reporteAGenerar').show()");
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
                modificarInfoRegistroLovReportes(listValInforeportes.size());
                RequestContext context = RequestContext.getCurrentInstance();
                PrimefacesContextUI.actualizar("form:ReportesDialogo");
                PrimefacesContextUI.ejecutar("PF('ReportesDialogo').show()");
            } else {
                RequestContext context = RequestContext.getCurrentInstance();
                PrimefacesContextUI.ejecutar("PF('confirmarGuardarSinSalida').show()");
            }
        } catch (Exception e) {
            System.out.println("Error mostrarDialogoBuscarReporte : " + e.toString());
        }
    }

    public void salir() {
        System.out.println(this.getClass().getName() + ".salir()");
        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            altoTabla = "140";
            codigoIR = (Column) c.getViewRoot().findComponent("form:reportesNomina:codigoIR");
            codigoIR.setFilterStyle("display: none; visibility: hidden;");
            reporteIR = (Column) c.getViewRoot().findComponent("form:reportesNomina:reporteIR");
            reporteIR.setFilterStyle("display: none; visibility: hidden;");
            PrimefacesContextUI.actualizar("form:reportesNomina");
            bandera = 0;
            filtrarListIRU = null;
        }
        listaIR = null;
        parametroDeInforme = null;
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
        PrimefacesContextUI.actualizar("form:ACEPTAR");
    }

    public void actualizarSeleccionInforeporte() {
        System.out.println(this.getClass().getName() + ".actualizarSeleccionInforeporte()");
        RequestContext context = RequestContext.getCurrentInstance();
        if (bandera == 1) {
            altoTabla = "140";
            FacesContext c = FacesContext.getCurrentInstance();
            codigoIR = (Column) c.getViewRoot().findComponent("form:reportesNomina:codigoIR");
            codigoIR.setFilterStyle("display: none; visibility: hidden;");
            reporteIR = (Column) c.getViewRoot().findComponent("form:reportesNomina:reporteIR");
            reporteIR.setFilterStyle("display: none; visibility: hidden;");
            PrimefacesContextUI.actualizar("form:reportesNomina");
            bandera = 0;
            filtrarListIRU = null;
        }
        defaultPropiedadesParametrosReporte();
        listaIR.clear();
        listaIR.add(reporteSeleccionadoLOV);
        filtrarListIRU = null;
        filtrarLovInforeportes = null;
        aceptar = true;
        activoBuscarReporte = true;
        activoMostrarTodos = false;
        reporteSeleccionado = null;
        reporteSeleccionadoLOV = null;
        PrimefacesContextUI.actualizar("form:MOSTRARTODOS");
        PrimefacesContextUI.actualizar("form:BUSCARREPORTE");
        context.reset("form:lovReportesDialogo:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovReportesDialogo').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('ReportesDialogo').hide()");
        PrimefacesContextUI.actualizar("form:reportesNomina");
    }

    public void cancelarSeleccionInforeporte() {
        System.out.println(this.getClass().getName() + ".cancelarSeleccionInforeporte()");
        filtrarListIRU = null;
        reporteSeleccionadoLOV = null;
        aceptar = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovReportesDialogo:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovReportesDialogo').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('ReportesDialogo').hide()");
    }

    public void mostrarTodos() {
        System.out.println(this.getClass().getName() + ".mostrarTodos()");
        if (cambiosReporte == true) {
            defaultPropiedadesParametrosReporte();
            listaIR.clear();
            for (int i = 0; i < listValInforeportes.size(); i++) {
                listaIR.add(listValInforeportes.get(i));
            }
            RequestContext context = RequestContext.getCurrentInstance();
            activoBuscarReporte = false;
            activoMostrarTodos = true;
            PrimefacesContextUI.actualizar("form:MOSTRARTODOS");
            PrimefacesContextUI.actualizar("form:BUSCARREPORTE");
            PrimefacesContextUI.actualizar("form:reportesNomina");
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            PrimefacesContextUI.ejecutar("PF('confirmarGuardarSinSalida').show()");
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
            altoTabla = "140";
            FacesContext c = FacesContext.getCurrentInstance();
            codigoIR = (Column) c.getViewRoot().findComponent("form:reportesNomina:codigoIR");
            codigoIR.setFilterStyle("display: none; visibility: hidden;");
            reporteIR = (Column) c.getViewRoot().findComponent("form:reportesNomina:reporteIR");
            reporteIR.setFilterStyle("display: none; visibility: hidden;");
            PrimefacesContextUI.actualizar("form:reportesNomina");
            bandera = 0;
            filtrarListIRU = null;
        }
        defaultPropiedadesParametrosReporte();
        listaIR = null;
        parametroDeInforme = null;
        parametroModificacion = null;
        casilla = -1;
        listaInfoReportesModificados.clear();
        cambiosReporte = true;
        getParametroDeInforme();
        getListaIR();
        if (listaIR != null) {
            modificarInfoRegistroReportes(listaIR.size());
        } else {
            modificarInfoRegistroReportes(0);
        }
        activoMostrarTodos = true;
        activoBuscarReporte = false;
        reporteSeleccionado = null;
        RequestContext context = RequestContext.getCurrentInstance();
        PrimefacesContextUI.actualizar("form:MOSTRARTODOS");
        PrimefacesContextUI.actualizar("form:BUSCARREPORTE");
        PrimefacesContextUI.actualizar("form:ACEPTAR");
        PrimefacesContextUI.actualizar("form:reportesNomina");
        PrimefacesContextUI.actualizar("formParametros:fechaDesdeParametro");
        PrimefacesContextUI.actualizar("formParametros:empleadoDesdeParametro");
        PrimefacesContextUI.actualizar("formParametros:estadoParametro");
        PrimefacesContextUI.actualizar("formParametros:grupoParametro");
        PrimefacesContextUI.actualizar("formParametros:ubicacionGeograficaParametro");
        PrimefacesContextUI.actualizar("formParametros:tipoAsociacionParametro");
        PrimefacesContextUI.actualizar("formParametros:fechaHastaParametro");
        PrimefacesContextUI.actualizar("formParametros:empleadoHastaParametro");
        PrimefacesContextUI.actualizar("formParametros:tipoPersonalParametro");
        PrimefacesContextUI.actualizar("formParametros:empresaParametro");
        PrimefacesContextUI.actualizar("formParametros:estructuraParametro");
        PrimefacesContextUI.actualizar("formParametros:tipoTrabajadorParametro");
        PrimefacesContextUI.actualizar("formParametros:terceroParametro");
        PrimefacesContextUI.actualizar("formParametros:notasParametro");
        PrimefacesContextUI.actualizar("formParametros:asociacionParametro");
    }

    public void activarCtrlF11() {
        System.out.println(this.getClass().getName() + ".activarCtrlF11()");
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            altoTabla = "120";
            codigoIR = (Column) c.getViewRoot().findComponent("form:reportesNomina:codigoIR");
            codigoIR.setFilterStyle("width: 85%");
            reporteIR = (Column) c.getViewRoot().findComponent("form:reportesNomina:reporteIR");
            reporteIR.setFilterStyle("width: 85%");
            PrimefacesContextUI.actualizar("form:reportesNomina");
            bandera = 1;
        } else if (bandera == 1) {
            System.out.println("Desactivar");
            altoTabla = "140";
            codigoIR = (Column) c.getViewRoot().findComponent("form:reportesNomina:codigoIR");
            codigoIR.setFilterStyle("display: none; visibility: hidden;");
            reporteIR = (Column) c.getViewRoot().findComponent("form:reportesNomina:reporteIR");
            reporteIR.setFilterStyle("display: none; visibility: hidden;");
            PrimefacesContextUI.actualizar("form:reportesNomina");
            bandera = 0;
            tipoLista = 0;
            filtrarListIRU = null;
            defaultPropiedadesParametrosReporte();
        }
    }

    public void modificacionTipoReporte(Inforeportes reporte) {
        System.out.println(this.getClass().getName() + ".modificacionTipoReporte()");
        reporteSeleccionado = reporte;
        cambiosReporte = false;
        if (listaInfoReportesModificados.isEmpty()) {
            listaInfoReportesModificados.add(reporteSeleccionado);
        } else {
            if ((!listaInfoReportesModificados.isEmpty()) && (!listaInfoReportesModificados.contains(reporteSeleccionado))) {
                listaInfoReportesModificados.add(reporteSeleccionado);
            } else {
                int posicion = listaInfoReportesModificados.indexOf(reporteSeleccionado);
                listaInfoReportesModificados.set(posicion, reporteSeleccionado);
            }
        }
        RequestContext context = RequestContext.getCurrentInstance();
        PrimefacesContextUI.actualizar("form:ACEPTAR");
    }

    /*
     * public void reporteSeleccionado(int i) {
     * System.out.println(this.getClass().getName() + ".reporteSeleccionado()");
     * System.out.println("Posicion del reporte : " + i); }
     */
    public void defaultPropiedadesParametrosReporte() {
        System.out.println(this.getClass().getName() + ".defaultPropiedadesParametrosReporte()");
        color = "black";
        decoracion = "none";
        color2 = "black";
        decoracion2 = "none";

    }

    public void cancelarRequisitosReporte() {
        System.out.println(this.getClass().getName() + ".cancelarRequisitosReporte()");
        requisitosReporte = "";
    }

    public ParametrosInformes getParametroDeInforme() {
        System.out.println(this.getClass().getName() + ".getParametroDeInforme()");
        try {
            if (parametroDeInforme == null) {
                parametroDeInforme = new ParametrosInformes();
                parametroDeInforme = administrarNReportesNomina.parametrosDeReporte();
            }
            if (parametroDeInforme.getGrupo() == null) {
                parametroDeInforme.setGrupo(new GruposConceptos());
            }
            if (parametroDeInforme.getUbicaciongeografica() == null) {
                parametroDeInforme.setUbicaciongeografica(new UbicacionesGeograficas());
            }
            if (parametroDeInforme.getTipoasociacion() == null) {
                parametroDeInforme.setTipoasociacion(new TiposAsociaciones());
            }
            if (parametroDeInforme.getLocalizacion() == null) {
                parametroDeInforme.setLocalizacion(new Estructuras());
            }
            if (parametroDeInforme.getTipotrabajador() == null) {
                parametroDeInforme.setTipotrabajador(new TiposTrabajadores());
            }
            if (parametroDeInforme.getTercero() == null) {
                parametroDeInforme.setTercero(new Terceros());
            }
            if (parametroDeInforme.getProceso() == null) {
                parametroDeInforme.setProceso(new Procesos());
            }
            if (parametroDeInforme.getAsociacion() == null) {
                parametroDeInforme.setAsociacion(new Asociaciones());
            }
            if (parametroDeInforme.getEmpresa() == null) {
                parametroDeInforme.setEmpresa(new Empresas());
            }
            return parametroDeInforme;
        } catch (Exception e) {
            System.out.println("Error getParametroDeInforme : " + e);
            return null;
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
                PrimefacesContextUI.ejecutar("PF('validarDescargaReporte();");
            } else {
                System.out.println("generando reporte - ingreso al 3 if else");
                PrimefacesContextUI.ejecutar("PF('generandoReporte.hide();");
                PrimefacesContextUI.actualizar("formDialogos:errorGenerandoReporte");
                PrimefacesContextUI.ejecutar("PF('errorGenerandoReporte.show();");
            }
        } else {
            System.out.println("generando reporte - ingreso al if else");
            System.out.println("Reporte Seleccionado es nulo");
        }
    }

    public void generarReporte(Inforeportes reporte) {
        System.out.println(this.getClass().getName() + ".generarReporte()");
        RequestContext context = RequestContext.getCurrentInstance();
        seleccionRegistro(reporte);
        guardarCambios();
        PrimefacesContextUI.ejecutar("PF('generandoReporte.show();");
        PrimefacesContextUI.ejecutar("PF('generarDocumentoReporte();");
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
     * //PrimefacesContextUI.ejecutar("PF('exportarReporte();"); System.out.println("Pasooo"); /*
     * try { //exportarReporte(); } catch (IOException ex) {
     * Logger.getLogger(ControlNReporteNomina.class.getName()).log(Level.SEVERE,
     * null, ex); }
     */
    //}
    //PrimefacesContextUI.ejecutar("PF('dlg').hide()");
    // PrimefacesContextUI.ejecutar("PF('esperarReporte();");
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
                    // PrimefacesContextUI.ejecutar("PF('formDialogos:generandoReporte");
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

    public void generarArchivoReporte(JasperPrint print) {
        System.out.println(this.getClass().getName() + ".generarArchivoReporte()");
        if (print != null && tipoReporte != null) {
            pathReporteGenerado = administarReportes.crearArchivoReporte(print, tipoReporte);
            validarDescargaReporte();
        }
    }

    public void exportarReporte() throws IOException {
        System.out.println(this.getClass().getName() + ".exportarReporte()");
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
    }

    public void validarDescargaReporte() {
        System.out.println(this.getClass().getName() + ".validarDescargaReporte()");
        RequestContext context = RequestContext.getCurrentInstance();
        PrimefacesContextUI.ejecutar("PF('generandoReporte.hide();");
        if (pathReporteGenerado != null && !pathReporteGenerado.startsWith("Error:")) {
            System.out.println("validar descarga reporte - ingreso al if 1");
            if (!tipoReporte.equals("PDF")) {
                System.out.println("validar descarga reporte - ingreso al if 2");
                PrimefacesContextUI.ejecutar("PF('descargarReporte.show();");
            } else {
                System.out.println("validar descarga reporte - ingreso al if 2 else");
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
                    System.out.println("validar descarga reporte - ingreso al if 3");
                    if (reporteSeleccionado != null) {
                        System.out.println("validar descarga reporte - ingreso al if 4");
                        //cabezeraVisor = "Reporte - " + reporteSeleccionado.getNombre();
                        cabezeraVisor = "Reporte - " + nombreReporte;
                    } else {
                        System.out.println("validar descarga reporte - ingreso al if 4 else ");
                        cabezeraVisor = "Reporte - ";
                    }
                    PrimefacesContextUI.actualizar("formDialogos:verReportePDF");
                    PrimefacesContextUI.ejecutar("PF('verReportePDF.show();");
                }
                //pathReporteGenerado = null;
            }
        } else {
            System.out.println("validar descarga reporte - ingreso al if 1 else");
            PrimefacesContextUI.actualizar("formDialogos:errorGenerandoReporte");
            PrimefacesContextUI.ejecutar("PF('errorGenerandoReporte.show();");
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

    public void recordarSeleccion() {
        System.out.println(this.getClass().getName() + ".recordarSeleccion()");
        if (reporteSeleccionado != null) {
            FacesContext c = FacesContext.getCurrentInstance();
            tabla = (DataTable) c.getViewRoot().findComponent("form:reportesNomina");
            tabla.setSelection(reporteSeleccionado);
        }
    }

    //EVENTO FILTRAR
    public void eventoFiltrar() {
        System.out.println("ControlNReporteNomina.eventoFiltrar");
        System.out.println("Entre al eventoFiltrar");
        System.out.println("tipoLista: " + tipoLista);
        if (tipoLista == 0) {
            tipoLista = 1;
        }
        reporteSeleccionado = null;
        modificarInfoRegistroReportes(filtrarListIRU.size());
        PrimefacesContextUI.actualizar("form:informacionRegistro");
    }

    public void eventoFiltrarEmpeladoD() {
        modificarInfoRegistroEmpleadoD(filtrarListEmpleados.size());
        PrimefacesContextUI.actualizar("form:infoRegistroEmpleadoDesde");
    }

    public void eventoFiltrarEmpeladoH() {
        modificarInfoRegistroEmpleadoH(filtrarListEmpleados.size());
        PrimefacesContextUI.actualizar("form:infoRegistroEmpleadoHasta");
    }

    public void eventoFiltrarGrupo() {
        modificarInfoRegistroGrupo(filtrarListGruposConceptos.size());
        PrimefacesContextUI.actualizar("form:infoRegistroGrupoConcepto");
    }

    public void eventoFiltrarEmpresa() {
        modificarInfoRegistroEmpresa(filtrarListEmpleados.size());
        PrimefacesContextUI.actualizar("form:infoRegistroEmpresa");
    }

    public void eventoFiltrarEstructura() {
        modificarInfoRegistroEstructura(filtrarListEstructuras.size());
        PrimefacesContextUI.actualizar("form:infoRegistroEstructura");
    }

    public void eventoFiltrarTipoTrabajador() {
        modificarInfoRegistroTipoTrabajador(filtrarListTiposTrabajadores.size());
        PrimefacesContextUI.actualizar("form:infoRegistroTipoTrabajador");
    }

    public void eventoFiltrarTercero() {
        modificarInfoRegistroTercero(filtrarListTerceros.size());
        PrimefacesContextUI.actualizar("form:infoRegistroTercero");
    }

    public void eventoFiltrarProceso() {
        modificarInfoRegistroProceso(filtrarListProcesos.size());
        PrimefacesContextUI.actualizar("form:infoRegistroProceso");
    }

    public void eventoFiltrarUbicacion() {
        modificarInfoRegistroUbicacion(filtrarListUbicacionesGeograficas.size());
        PrimefacesContextUI.actualizar("form:infoRegistroUbicacion");
    }

    public void eventoFiltrarTipoAsociacion() {
        modificarInfoRegistroTipoAsociacion(filtrarListTiposAsociaciones.size());
        PrimefacesContextUI.actualizar("form:infoRegistroTipoAsociacion");
    }

    public void eventoFiltrarAsociacion() {
        modificarInfoRegistroAsociacion(filtrarListAsociaciones.size());
        PrimefacesContextUI.actualizar("form:infoRegistroAsociacion");
    }

    public void eventoFiltrarLovReportes() {
        modificarInfoRegistroLovReportes(filtrarLovInforeportes.size());
        PrimefacesContextUI.actualizar("form:infoRegistroReportes");
    }

    private void modificarInfoRegistroReportes(int valor) {
        infoRegistro = String.valueOf(valor);
    }

    private void modificarInfoRegistroEmpleadoD(int valor) {
        infoRegistroEmpleadoDesde = String.valueOf(valor);
    }

    private void modificarInfoRegistroEmpleadoH(int valor) {
        infoRegistroEmpleadoHasta = String.valueOf(valor);
    }

    private void modificarInfoRegistroGrupo(int valor) {
        infoRegistroGrupoConcepto = String.valueOf(valor);
    }

    private void modificarInfoRegistroEmpresa(int valor) {
        infoRegistroEmpresa = String.valueOf(valor);
    }

    private void modificarInfoRegistroEstructura(int valor) {
        infoRegistroEstructura = String.valueOf(valor);
    }

    private void modificarInfoRegistroTipoTrabajador(int valor) {
        infoRegistroTipoTrabajador = String.valueOf(valor);
    }

    private void modificarInfoRegistroTercero(int valor) {
        infoRegistroTercero = String.valueOf(valor);
    }

    private void modificarInfoRegistroProceso(int valor) {
        infoRegistroProceso = String.valueOf(valor);
    }

    private void modificarInfoRegistroUbicacion(int valor) {
        infoRegistroUbicacion = String.valueOf(valor);
    }

    private void modificarInfoRegistroTipoAsociacion(int valor) {
        infoRegistroTipoAsociacion = String.valueOf(valor);
    }

    private void modificarInfoRegistroAsociacion(int valor) {
        infoRegistroAsociacion = String.valueOf(valor);
    }

    private void modificarInfoRegistroLovReportes(int valor) {
        infoRegistroLovReportes = String.valueOf(valor);
    }

    //GETTER AND SETTER
    public void setParametroDeInforme(ParametrosInformes parametroDeInforme) {
        this.parametroDeInforme = parametroDeInforme;
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

    public ParametrosInformes getNuevoParametroInforme() {
        System.out.println(this.getClass().getName() + ".getNuevoParametroInforme()");
        return nuevoParametroInforme;
    }

    public void setNuevoParametroInforme(ParametrosInformes nuevoParametroInforme) {
        System.out.println(this.getClass().getName() + ".setNuevoParametroInforme()");
        this.nuevoParametroInforme = nuevoParametroInforme;
    }

    public ParametrosInformes getParametroModificacion() {
        System.out.println(this.getClass().getName() + ".getParametroModificacion()");
        return parametroModificacion;
    }

    public void setParametroModificacion(ParametrosInformes parametroModificacion) {
        System.out.println(this.getClass().getName() + ".setParametroModificacion()");
        this.parametroModificacion = parametroModificacion;
    }

    public String getReporteGenerar() {
        System.out.println(this.getClass().getName() + ".getReporteGenerar()");
        if (posicionReporte != -1) {
            reporteGenerar = listaIR.get(posicionReporte).getNombre();
        }
        return reporteGenerar;
    }

    public void setReporteGenerar(String reporteGenerar) {
        System.out.println(this.getClass().getName() + ".setReporteGenerar()");
        this.reporteGenerar = reporteGenerar;
    }

    public String getRequisitosReporte() {
        return requisitosReporte;
    }

    public void setRequisitosReporte(String requisitosReporte) {
        this.requisitosReporte = requisitosReporte;
    }

    public List<Empleados> getListValEmpleados() {
        if (listValEmpleados == null || listValEmpleados.isEmpty()) {
            listValEmpleados = administrarNReportesNomina.listEmpleados();
        }
        return listValEmpleados;
    }

    public void setListValEmpleados(List<Empleados> listEmpleados) {
        this.listValEmpleados = listEmpleados;
    }

    public List<Empresas> getListValEmpresas() {
        if (listValEmpresas == null || listValEmpresas.isEmpty()) {
            listValEmpresas = administrarNReportesNomina.listEmpresas();
        }
        return listValEmpresas;
    }

    public void setListValEmpresas(List<Empresas> listEmpresas) {
        this.listValEmpresas = listEmpresas;
    }

    public List<GruposConceptos> getListValGruposConceptos() {
        if (listValGruposConceptos == null || listValGruposConceptos.isEmpty()) {
            listValGruposConceptos = administrarNReportesNomina.listGruposConcetos();
        }
        return listValGruposConceptos;
    }

    public void setListValGruposConceptos(List<GruposConceptos> listGruposConceptos) {
        this.listValGruposConceptos = listGruposConceptos;
    }

    public List<UbicacionesGeograficas> getListValUbicacionesGeograficas() {
        if (listValUbicacionesGeograficas == null || listValUbicacionesGeograficas.isEmpty()) {
            listValUbicacionesGeograficas = administrarNReportesNomina.listUbicacionesGeograficas();
        }
        return listValUbicacionesGeograficas;
    }

    public void setListValUbicacionesGeograficas(List<UbicacionesGeograficas> listUbicacionesGeograficas) {
        this.listValUbicacionesGeograficas = listUbicacionesGeograficas;
    }

    public List<TiposAsociaciones> getListValTiposAsociaciones() {
        if (listValTiposAsociaciones == null || listValTiposAsociaciones.isEmpty()) {
            listValTiposAsociaciones = administrarNReportesNomina.listTiposAsociaciones();
        }
        return listValTiposAsociaciones;
    }

    public void setListValTiposAsociaciones(List<TiposAsociaciones> listTiposAsociaciones) {
        this.listValTiposAsociaciones = listTiposAsociaciones;
    }

    public List<Estructuras> getListValEstructuras() {
        if (listValEstructuras == null || listValEstructuras.isEmpty()) {
            listValEstructuras = administrarNReportesNomina.listEstructuras();
        }
        return listValEstructuras;
    }

    public void setListValEstructuras(List<Estructuras> listEstructuras) {
        this.listValEstructuras = listEstructuras;
    }

    public List<TiposTrabajadores> getListValTiposTrabajadores() {
        if (listValTiposTrabajadores == null || listValTiposTrabajadores.isEmpty()) {
            listValTiposTrabajadores = administrarNReportesNomina.listTiposTrabajadores();
        }
        return listValTiposTrabajadores;
    }

    public void setListValTiposTrabajadores(List<TiposTrabajadores> listTiposTrabajadores) {
        this.listValTiposTrabajadores = listTiposTrabajadores;
    }

    public List<Terceros> getListValTerceros() {
        if (listValTerceros == null || listValTerceros.isEmpty()) {
            listValTerceros = administrarNReportesNomina.listTerceros(this.getParametroDeInforme().getEmpresa().getSecuencia());
        }
        return listValTerceros;
    }

    public void setListValTerceros(List<Terceros> listTerceros) {
        this.listValTerceros = listTerceros;
    }

    public List<Procesos> getListValProcesos() {
        if (listValProcesos == null || listValProcesos.isEmpty()) {
            listValProcesos = administrarNReportesNomina.listProcesos();
        }
        return listValProcesos;
    }

    public void setListValProcesos(List<Procesos> listProcesos) {
        this.listValProcesos = listProcesos;
    }

    public List<Asociaciones> getListValAsociaciones() {
        if (listValAsociaciones == null || listValAsociaciones.isEmpty()) {
            listValAsociaciones = administrarNReportesNomina.listAsociaciones();
        }
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

    /*
     * public static FacesContext getFacesContext(HttpServletRequest request,
     * HttpServletResponse response) { // Get current FacesContext. FacesContext
     * facesContext;
     *
     * System.out.println("Entro a crear un FacesContext"); // Create new
     * Lifecycle. LifecycleFactory lifecycleFactory = (LifecycleFactory)
     * FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY); Lifecycle
     * lifecycle =
     * lifecycleFactory.getLifecycle(LifecycleFactory.DEFAULT_LIFECYCLE);
     *
     * // Create new FacesContext. FacesContextFactory contextFactory =
     * (FacesContextFactory)
     * FactoryFinder.getFactory(FactoryFinder.FACES_CONTEXT_FACTORY);
     * facesContext = contextFactory.getFacesContext(
     * request.getSession().getServletContext(), request, response, lifecycle);
     *
     * // Create new View. UIViewRoot view =
     * facesContext.getApplication().getViewHandler().createView( facesContext,
     * ""); facesContext.setViewRoot(view);
     *
     * // Set current FacesContext.
     * FacesContextWrapper.setCurrentInstance(facesContext);
     *
     * return facesContext; }
     */
    private static abstract class FacesContextWrapper extends FacesContext {

        protected static void setCurrentInstance(FacesContext facesContext) {
            FacesContext.setCurrentInstance(facesContext);
        }
    }

    public String getInfoRegistro() {
        return infoRegistro;
    }

    public String getInfoRegistroEmpleadoDesde() {
        return infoRegistroEmpleadoDesde;
    }

    public String getInfoRegistroEmpleadoHasta() {
        return infoRegistroEmpleadoHasta;
    }

    public String getInfoRegistroGrupoConcepto() {
        return infoRegistroGrupoConcepto;
    }

    public String getInfoRegistroUbicacion() {
        return infoRegistroUbicacion;
    }

    public String getInfoRegistroTipoAsociacion() {
        return infoRegistroTipoAsociacion;
    }

    public String getInfoRegistroEmpresa() {
        return infoRegistroEmpresa;
    }

    public String getInfoRegistroAsociacion() {
        return infoRegistroAsociacion;
    }

    public String getInfoRegistroTercero() {
        return infoRegistroTercero;
    }

    public String getInfoRegistroProceso() {
        return infoRegistroProceso;
    }

    public String getInfoRegistroTipoTrabajador() {
        return infoRegistroTipoTrabajador;
    }

    public String getInfoRegistroEstructura() {
        return infoRegistroEstructura;
    }

    public String getInfoRegistroReportes() {
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
}
