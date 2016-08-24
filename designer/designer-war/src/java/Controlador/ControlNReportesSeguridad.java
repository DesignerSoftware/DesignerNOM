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
import Entidades.ParametrosInformes;
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
    private ParametrosInformes parametroDeInforme;
    private ParametrosInformes nuevoParametroInforme;
    private List<Inforeportes> listaIR;
    private Inforeportes reporteSeleccionado;
    private List<Inforeportes> filtrarListIRU;
    private String reporteGenerar;
    private int bandera;
    private boolean aceptar;
    private Column codigoIR, reporteIR;
    private int casilla;
    private ParametrosInformes parametroModificacion;
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
    private List<TiposTrabajadores> listValTiposTrabajadores;
    private TiposTrabajadores tipoTSeleccionado;
    private List<TiposTrabajadores> filtrarListTiposTrabajadores;
    private List<Estructuras> listValEstructuras;
    private Estructuras estructuraSeleccionada;
    private List<Estructuras> filtrarListEstructuras;
    private List<SucursalesPila> listValSucursales;
    private SucursalesPila sucursalSeleccionada;
    private List<SucursalesPila> filtrarListSucursales;
    //EXPORTAR REPORTE
    private String pathReporteGenerado = null;
    private String nombreReporte, tipoReporte;
    private List<Inforeportes> listaInfoReportesModificados;
    private String color, decoracion;
    private String color2, decoracion2;
    private int casillaInforReporte;
    private Date fechaDesde, fechaHasta;
    private BigDecimal emplDesde, emplHasta;
    private boolean activoMostrarTodos, activoBuscarReporte;
    //VISUALIZAR REPORTE PDF
    private StreamedContent reporte;
    private String cabezeraVisor;
    //Listener reporte
    private AsynchronousFilllListener asistenteReporte;
    //BANDERAS
    private boolean estadoReporte;
    private String resultadoReporte;
    private String infoRegistroEmpleadoDesde, infoRegistroEmpleadoHasta, infoRegistroEmpresa,infoRegistroTercero,infoRegistroSucursal;
    private String infoRegistroLovReportes, infoRegistro;
    private DataTable tabla;
    private int tipoLista;
    private String grupo, estructura, empresa, tipoTrabajador, tercero, proceso, sucursal,empleado;
    private String altoTabla;
    private boolean permitirIndex, cambiosReporte;
    private BigInteger auxiliar;

    /**
     * Creates a new instance of controlNReportesPila
     */
    public ControlNReportesSeguridad() {
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
        listValEmpleados = null;
        listValEmpresas = null;
        listValEstructuras = null;
        listValGruposConceptos = null;
        listValTerceros = null;
        listValTiposTrabajadores = null;
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
        altoTabla = "140";
        cabezeraVisor = null;
        estadoReporte = false;
    }

    @PostConstruct
    public void iniciarAdministrador(){
    try {
            FacesContext contexto = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) contexto.getExternalContext().getSession(false);
            administarReportes.obtenerConexion(ses.getId());
            administrarNReportesSeguridad.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct controlNReporteNomina" + e);
            System.out.println("Causa: " + e.getCause());
        }    
    }
    
    public void iniciarPagina() {
        System.out.println(this.getClass().getName() + ".iniciarPagina()");
        activoMostrarTodos = true;
        activoBuscarReporte = false;
        listaIR = null;
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
        if (!requisitosReporte.isEmpty()) {
            context.update("formDialogos:requisitosReporte");
            context.execute("requisitosReporte.show()");
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
//        RequestContext.getCurrentInstance().update("formParametros");
//        context.update("form:reportesSeguridad");
         System.out.println("reporte seleccionado : " + reporteSeleccionado.getSecuencia());
    }
    
    public void dispararDialogoGuardarCambios() {
        System.out.println(this.getClass().getName() + ".dispararDialogoGuardarCambios()");
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("confirmarGuardar.show()");

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
                    if (parametroDeInforme.getEmpresa().getSecuencia() == null) {
                        parametroDeInforme.setEmpresa(null);
                    }
                    if (parametroDeInforme.getTercero().getSecuencia() == null) {
                        parametroDeInforme.setTercero(null);
                    }
                    if (parametroDeInforme.getSucursalPila().getSecuencia() == null) {
                        parametroDeInforme.setSucursalPila(null);
                    }
                    administrarNReportesSeguridad.modificarParametrosInformes(parametroDeInforme);
                }
                if (!listaInfoReportesModificados.isEmpty()) {
                    administrarNReportesSeguridad.guardarCambiosInfoReportes(listaInfoReportesModificados);
                }
                cambiosReporte = true;
                FacesMessage msg = new FacesMessage("Información", "Los datos se guardaron con Éxito.");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                RequestContext.getCurrentInstance().update("form:growl");
                context.update("form:ACEPTAR");
            }
        } catch (Exception e) {
            System.out.println("Error en guardar Cambios Controlador : " + e.toString());
            FacesMessage msg = new FacesMessage("Información", "ha ocurrido un error en el guardado, intente nuevamente");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
            context.update("form:ACEPTAR");
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
                context.update("form:ACEPTAR");
            } else {
                parametroDeInforme.setFechadesde(fechaDesde);
                parametroDeInforme.setFechahasta(fechaHasta);
                RequestContext context = RequestContext.getCurrentInstance();
                context.update("formParametros");
                context.execute("errorFechas.show()");
            }
        } else {
            parametroDeInforme.setCodigoempleadodesde(emplDesde);
            parametroDeInforme.setCodigoempleadohasta(emplHasta);
            parametroDeInforme.setFechadesde(fechaDesde);
            parametroDeInforme.setFechahasta(fechaHasta);
            parametroDeInforme.getEmpresa().setNombre(empresa);
            parametroDeInforme.getSucursalPila().setDescripcion(sucursal);
            parametroDeInforme.getTercero().setNombre(tercero);
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("formParametros");
            context.execute("errorRegNew.show()");
        }
    }
    
    public void posicionCelda(int i) {
        System.out.println(this.getClass().getName() + ".posicionCelda()");
        casilla = i;
        if (permitirIndex == true) {
            if(casilla == 0){
            fechaDesde = parametroDeInforme.getFechadesde();
            }
            else if(casilla ==1){
            fechaHasta = parametroDeInforme.getFechahasta();
            }else if(casilla ==2){
            emplDesde = parametroDeInforme.getCodigoempleadodesde();
            } else if(casilla ==3){
            emplHasta = parametroDeInforme.getCodigoempleadohasta();
            } else if(casilla ==4){
            tercero = parametroDeInforme.getTercero().getNombre();
            } else if(casilla ==5){
            empresa = parametroDeInforme.getEmpresa().getNombre();
            }
            else if(casilla == 7){
            sucursal = parametroDeInforme.getSucursalPila().getDescripcion();
            }
        }
            casillaInforReporte = -1;
    }
    
    public void editarCelda() {
        System.out.println(this.getClass().getName() + ".editarCelda()");
        RequestContext context = RequestContext.getCurrentInstance();
            if (casilla == 0) {
                context.update("formDialogos:editarFechaDesde");
                context.execute("editarFechaDesde.show()");
            }
            if (casilla == 1) {
                context.update("formDialogos:editarFechaHasta");
                context.execute("editarFechaHasta.show()");
            }
            if (casilla == 2) {
                context.update("formDialogos:empleadoDesde");
                context.execute("empleadoDesde.show()");
            }
            if (casilla == 3) {
                context.update("formDialogos:empleadoHasta");
                context.execute("empleadoHasta.show()");
            }
            if (casilla == 4) {
                context.update("formDialogos:tercero");
                context.execute("tercero.show()");
            }
            if (casilla == 5) {
                context.update("formDialogos:empresa");
                context.execute("empresa.show()");
            }
            if (casilla == 7) {
                context.update("formDialogos:sucursal");
                context.execute("sucursal.show()");
            }
            casilla = -1;
            
        if (casillaInforReporte >= 1) {
            if (casillaInforReporte == 1) {
                context.update("formParametros:infoReporteCodigoD");
                context.execute("infoReporteCodigoD.show()");
            }
            if (casillaInforReporte == 2) {
                context.update("formParametros:infoReporteNombreD");
                context.execute("infoReporteNombreD.show()");
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
                    context.update("form:ACEPTAR");
                } else {
                    permitirIndex = false;
                    context.update("form:EmpresaDialogo");
                    context.execute("EmpresaDialogo.show()");
                }
            } else {
                parametroDeInforme.setEmpresa(new Empresas());
                parametroModificacion = parametroDeInforme;
                listValEmpresas.clear();
                getListValEmpresas();
                cambiosReporte = false;
                context.update("form:ACEPTAR");
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
                    context.update("form:ACEPTAR");
                } else {
                    permitirIndex = false;
                    context.update("form:TerceroDialogo");
                    context.execute("TerceroDialogo.show()");
                }
            } else {
                parametroDeInforme.setTercero(new Terceros());
                parametroModificacion = parametroDeInforme;
                listValTerceros.clear();
                getListValTerceros();
                cambiosReporte = false;
                context.update("form:ACEPTAR");
            }
        }
        if (campoConfirmar.equalsIgnoreCase("SUCURSAL")) {
            if (!valorConfirmar.isEmpty()) {
                parametroDeInforme.getSucursalPila().setDescripcion(tercero);
                for (int i = 0; i < listValSucursales.size(); i++) {
                    if (listValSucursales.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    parametroDeInforme.setSucursalPila(listValSucursales.get(indiceUnicoElemento));
                    parametroModificacion = parametroDeInforme;
                    listValSucursales.clear();
                    getListValSucursales();
                    cambiosReporte = false;
                    context.update("form:ACEPTAR");
                } else {
                    permitirIndex = false;
                    context.update("form:SucursalDialogo");
                    context.execute("SucursalDialogo.show()");
                }
            } else {
                parametroDeInforme.setSucursalPila(new SucursalesPila());
                parametroModificacion = parametroDeInforme;
                listValSucursales.clear();
                getListValSucursales();
                cambiosReporte = false;
                context.update("form:ACEPTAR");
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
                    context.update("form:ACEPTAR");
                } else {
                    System.out.println("Entre al else");
                    permitirIndex = false;
                    context.update("form:EmpleadoDesdeDialogo");
                    context.execute("EmpleadoDesdeDialogo.show()");
                }
            } else {
                parametroDeInforme.setCodigoempleadodesde(new BigDecimal("0"));
                parametroModificacion = parametroDeInforme;
                listValEmpleados.clear();
                getListValEmpleados();
                cambiosReporte = false;
                context.update("form:ACEPTAR");
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
                        context.update("form:ACEPTAR");
                    } else {
                        permitirIndex = false;
                        if ((listValEmpleados == null) || listValEmpleados.isEmpty()) {
                            listValEmpleados = null;
                        }
                        context.update("form:EmpleadoHastaDialogo");
                        context.execute("EmpleadoHastaDialogo.show()");
                    }
                } else {
                    System.out.println("Entre al else en  ControlNReporteNomina.autocompletarGeneral");
                    parametroDeInforme.setCodigoempleadohasta(new BigDecimal("9999999999999999999999"));
                    parametroModificacion = parametroDeInforme;
                    listValEmpleados.clear();
                    getListValEmpleados();
                    cambiosReporte = false;
                    context.update("form:ACEPTAR");
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
            context.update("form:EmpleadoDesdeDialogo");
            context.execute("EmpleadoDesdeDialogo.show()");
            modificarInfoRegistroEmpleadoD(listValEmpleados.size());
        }
        if (casilla == 3) {
            context.update("form:EmpleadoHastaDialogo");
            context.execute("EmpleadoHastaDialogo.show()");
            modificarInfoRegistroEmpleadoH(listValEmpleados.size());
        }
        if (casilla == 4) {
            context.update("form:TerceroDialogo");
            context.execute("TerceroDialogo.show()");
            modificarInfoRegistroTercero(listValTerceros.size());
        }
        if (casilla == 6) {
            context.update("form:EmpresaDialogo");
            context.execute("EmpresaDialogo.show()");
            modificarInfoRegistroEmpresa(listValEmpresas.size());
        }
        if (casilla == 7) {
            context.update("form:sucursalDialogo");
            context.execute("sucursalDialogo.show()");
            modificarInfoRegistroSucursales(listValSucursales.size());
        }
    }

    public void dialogosParametros(int pos) {
        System.out.println(this.getClass().getName() + ".dialogosParametros()");
        RequestContext context = RequestContext.getCurrentInstance();
        if (pos == 2) {
            if ((listValEmpleados == null) || listValEmpleados.isEmpty()) {
                listValEmpleados = null;
            }
            context.update("form:EmpleadoDesdeDialogo");
            context.execute("EmpleadoDesdeDialogo.show()");
            modificarInfoRegistroEmpleadoD(listValEmpleados.size());
        }
        if (pos == 3) {
            context.update("form:EmpleadoHastaDialogo");
            context.execute("EmpleadoHastaDialogo.show()");
            modificarInfoRegistroEmpleadoH(listValEmpleados.size());
        }
        if (pos == 4) {
            context.update("form:TerceroDialogo");
            context.execute("TerceroDialogo.show()");
            modificarInfoRegistroTercero(listValTerceros.size());
        }
        if (pos == 5) {
            context.update("form:EmpresaDialogo");
            context.execute("EmpresaDialogo.show()");
            modificarInfoRegistroEmpresa(listValEmpresas.size());
        }
        if (pos == 7) {
            context.update("form:sucursalDialogo");
            context.execute("sucursalDialogo.show()");
            modificarInfoRegistroSucursales(listValSucursales.size());
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
        context.execute("lovEmpleadoDesde.clearFilters()");
        context.execute("EmpleadoDesdeDialogo.hide()");

        context.update("form:ACEPTAR");
        context.update("formParametros:empleadoDesdeParametro");
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
        context.execute("lovEmpleadoDesde.clearFilters()");
        context.execute("EmpleadoDesdeDialogo.hide()");
    }

    public void actualizarEmplHasta() {
        System.out.println(this.getClass().getName() + ".actualizarEmplHasta()");
        permitirIndex = true;
        parametroDeInforme.setCodigoempleadohasta(empleadoSeleccionado.getCodigoempleado());
        parametroModificacion = parametroDeInforme;
        cambiosReporte = false;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovEmpleadoHasta:globalFilter");
        context.execute("lovEmpleadoHasta.clearFilters()");
        context.execute("EmpleadoHastaDialogo.hide()");

        context.update("form:ACEPTAR");
        context.update("formParametros:empleadoHastaParametro");
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
        context.execute("lovEmpleadoHasta.clearFilters()");
        context.execute("EmpleadoHastaDialogo.hide()");
    }
 
    public void actualizarEmpresa() {
        System.out.println(this.getClass().getName() + ".actualizarEmpresa()");
        RequestContext context = RequestContext.getCurrentInstance();
        permitirIndex = true;
        parametroDeInforme.setEmpresa(empresaSeleccionada);
        parametroModificacion = parametroDeInforme;
        cambiosReporte = false;
        auxiliar = empresaSeleccionada.getSecuencia();
        listValSucursales = null;
        getListValSucursales();
        context.update("form:sucursalDialogo");
        System.out.println("cambio de empresa");
        empresaSeleccionada = null;
        aceptar = true;
        filtrarListEmpresas = null;
        context.reset("form:lovEmpresa:globalFilter");
        context.execute("lovEmpresa.clearFilters()");
        context.execute("EmpresaDialogo.hide()");
        context.update("form:EmpresaDialogo");
        context.update("form:lovEmpresa");
        context.update("form:aceptarE");
        context.update("formParametros:empresaParametro");

//        context.update("form:ACEPTAR");

    }

    public void cancelarEmpresa() {
        System.out.println(this.getClass().getName() + ".cancelarEmpresa()");
        empresaSeleccionada = null;
        aceptar = true;
        filtrarListEmpresas = null;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovEmpresa:globalFilter");
        context.execute("lovEmpresa.clearFilters()");
        context.execute("EmpresaDialogo.hide()");
    }

    public void actualizarTercero() {
        System.out.println(this.getClass().getName() + ".actualizarTercero()");
        permitirIndex = true;
        parametroDeInforme.setTercero(terceroSeleccionado);
        parametroModificacion = parametroDeInforme;
        cambiosReporte = false;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovTercero:globalFilter");
        context.execute("lovTercero.clearFilters()");
        context.execute("TerceroDialogo.hide()");

        context.update("form:ACEPTAR");
        context.update("formParametros:terceroParametro");
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
        context.execute("lovTercero.clearFilters()");
        context.execute("TerceroDialogo.hide()");
    }
 
     public void actualizarSucursal() {
        System.out.println(this.getClass().getName() + ".actualziarSucursal()");
        permitirIndex = true;
        parametroDeInforme.setSucursalPila(sucursalSeleccionada);
        parametroModificacion = parametroDeInforme;
        cambiosReporte = false;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovSucursal:globalFilter");
        context.execute("lovSucursal.clearFilters()");
        context.execute("sucursalDialogo.hide()");

        context.update("form:ACEPTAR");
        context.update("formParametros:sucursalParametro");
        sucursalSeleccionada = null;
        aceptar = true;
        filtrarListSucursales = null;

    }

    public void cancelarSucursal() {
        System.out.println(this.getClass().getName() + ".cancelarSucursal()");
        sucursalSeleccionada = null;
        aceptar = true;
        filtrarListSucursales = null;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
    context.reset("form:lovSucursal:globalFilter");
        context.execute("lovSucursal.clearFilters()");
        context.execute("sucursalDialogo.hide()");
    }
    
 
    public void mostrarDialogoGenerarReporte() {
        System.out.println(this.getClass().getName() + ".mostrarDialogoGenerarReporte()");
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("formDialogos:reporteAGenerar");
        context.execute("reporteAGenerar.show()");
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
                context.update("form:ReportesDialogo");
                context.execute("ReportesDialogo.show()");
            } else {
                RequestContext context = RequestContext.getCurrentInstance();
                context.execute("confirmarGuardarSinSalida.show()");
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
            codigoIR = (Column) c.getViewRoot().findComponent("form:reportesSeguridad:codigoIR");
            codigoIR.setFilterStyle("display: none; visibility: hidden;");
            reporteIR = (Column) c.getViewRoot().findComponent("form:reportesSeguridad:reporteIR");
            reporteIR.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:reportesSeguridad");
            bandera = 0;
            filtrarListIRU = null;
        }
        listaIR = null;
        parametroDeInforme = null;
        parametroModificacion = null;
        listValSucursales = null;
        listValEmpleados = null;
        listValEmpresas = null;
        listValEstructuras = null;
        listValGruposConceptos = null;
        listValTerceros = null;
        listValTiposTrabajadores = null;
        casilla = -1;
        listaInfoReportesModificados.clear();
        cambiosReporte = true;
        reporteSeleccionado = null;
        reporteSeleccionadoLOV = null;
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("form:ACEPTAR");
    }

    public void actualizarSeleccionInforeporte() {
        System.out.println(this.getClass().getName() + ".actualizarSeleccionInforeporte()");
        RequestContext context = RequestContext.getCurrentInstance();
        if (bandera == 1) {
            altoTabla = "140";
            FacesContext c = FacesContext.getCurrentInstance();
            codigoIR = (Column) c.getViewRoot().findComponent("form:reportesSeguridad:codigoIR");
            codigoIR.setFilterStyle("display: none; visibility: hidden;");
            reporteIR = (Column) c.getViewRoot().findComponent("form:reportesSeguridad:reporteIR");
            reporteIR.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:reportesSeguridad");
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
        context.update("form:MOSTRARTODOS");
        context.update("form:BUSCARREPORTE");
        context.reset("form:lovReportesDialogo:globalFilter");
        context.execute("lovReportesDialogo.clearFilters()");
        context.execute("ReportesDialogo.hide()");
        context.update("form:reportesSeguridad");
    }

    public void cancelarSeleccionInforeporte() {
        System.out.println(this.getClass().getName() + ".cancelarSeleccionInforeporte()");
        filtrarListIRU = null;
        reporteSeleccionadoLOV = null;
        aceptar = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovReportesDialogo:globalFilter");
        context.execute("lovReportesDialogo.clearFilters()");
        context.execute("ReportesDialogo.hide()");
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
            context.update("form:MOSTRARTODOS");
            context.update("form:BUSCARREPORTE");
            context.update("form:reportesSeguridad");
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("confirmarGuardarSinSalida.show()");
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
            codigoIR = (Column) c.getViewRoot().findComponent("form:reportesSeguridad:codigoIR");
            codigoIR.setFilterStyle("display: none; visibility: hidden;");
            reporteIR = (Column) c.getViewRoot().findComponent("form:reportesSeguridad:reporteIR");
            reporteIR.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:reportesSeguridad");
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
        context.update("form:MOSTRARTODOS");
        context.update("form:BUSCARREPORTE");
        context.update("form:ACEPTAR");
        context.update("form:reportesSeguridad");
        context.update("formParametros:fechaDesdeParametro");
        context.update("formParametros:empleadoDesdeParametro");
        context.update("formParametros:fechaHastaParametro");
        context.update("formParametros:empleadoHastaParametro");
        context.update("formParametros:empresaParametro");
        context.update("formParametros:terceroParametro");
        context.update("formParametros:sucursalParametro");
    }
    
    public void activarCtrlF11() {
        System.out.println(this.getClass().getName() + ".activarCtrlF11()");
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            altoTabla = "118";
            codigoIR = (Column) c.getViewRoot().findComponent("form:reportesSeguridad:codigoIR");
            codigoIR.setFilterStyle("width: 96%");
            reporteIR = (Column) c.getViewRoot().findComponent("form:reportesSeguridad:reporteIR");
            reporteIR.setFilterStyle("width: 96%");
            RequestContext.getCurrentInstance().update("form:reportesSeguridad");
            bandera = 1;
        } else if (bandera == 1) {
            System.out.println("Desactivar");
            altoTabla = "140";
            codigoIR = (Column) c.getViewRoot().findComponent("form:reportesSeguridad:codigoIR");
            codigoIR.setFilterStyle("display: none; visibility: hidden;");
            reporteIR = (Column) c.getViewRoot().findComponent("form:reportesSeguridad:reporteIR");
            reporteIR.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:reportesSeguridad");
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
        context.update("form:ACEPTAR");
    }
    
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
                context.execute("validarDescargaReporte();");
            } else {
                System.out.println("generando reporte - ingreso al 3 if else");
                context.execute("generandoReporte.hide();");
                context.update("formDialogos:errorGenerandoReporte");
                context.execute("errorGenerandoReporte.show();");
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
        context.execute("generandoReporte.show();");
        context.execute("generarDocumentoReporte();");
     
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
                    // context.execute("formDialogos:generandoReporte");
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
        context.execute("generandoReporte.hide();");
        if (pathReporteGenerado != null && !pathReporteGenerado.startsWith("Error:")) {
            System.out.println("validar descarga reporte - ingreso al if 1");
            if (!tipoReporte.equals("PDF")) {
                System.out.println("validar descarga reporte - ingreso al if 2");
                context.execute("descargarReporte.show();");
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
                    context.update("formDialogos:verReportePDF");
                    context.execute("verReportePDF.show();");
                }
                //pathReporteGenerado = null;
            }
        } else {
            System.out.println("validar descarga reporte - ingreso al if 1 else");
            context.update("formDialogos:errorGenerandoReporte");
            context.execute("errorGenerandoReporte.show();");
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
            tabla = (DataTable) c.getViewRoot().findComponent("form:reportesSeguridad");
            tabla.setSelection(reporteSeleccionado);
        }   
}
 
///EVENTO FILTRAR    
  public void eventoFiltrar() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
        reporteSeleccionado = null;
        modificarInfoRegistroReportes(filtrarListIRU.size());
    }

    public void eventoFiltrarEmpeladoD() {
        modificarInfoRegistroEmpleadoD(filtrarListEmpleados.size());
    }

    public void eventoFiltrarEmpeladoH() {
        modificarInfoRegistroEmpleadoH(filtrarListEmpleados.size());
    }  
    
     public void eventoFiltrarEmpresa() {
        modificarInfoRegistroEmpresa(filtrarListEmpresas.size());
    }
    
    public void eventoFiltrarTercero() {
        modificarInfoRegistroTercero(filtrarListTerceros.size());
    } 
    
      public void eventoFiltrarLovReportes() {
        modificarInfoRegistroLovReportes(filtrarLovInforeportes.size());
    }
      
    public void eventoFiltrarSucursales() {
        modificarInfoRegistroSucursales(filtrarListSucursales.size());
    }
     
    private void modificarInfoRegistroReportes(int valor) {
        infoRegistro = String.valueOf(valor);
        RequestContext.getCurrentInstance().update("form:infoRegistro");
    }

    private void modificarInfoRegistroEmpleadoD(int valor) {
        infoRegistroEmpleadoDesde = String.valueOf(valor);
        RequestContext.getCurrentInstance().update("form:infoRegistroEmpleadoDesde");
    }

    private void modificarInfoRegistroEmpleadoH(int valor) {
        infoRegistroEmpleadoHasta = String.valueOf(valor);
        RequestContext.getCurrentInstance().update("form:infoRegistroEmpleadoHasta");
    }
    
    private void modificarInfoRegistroEmpresa(int valor) {
        infoRegistroEmpresa = String.valueOf(valor);
        RequestContext.getCurrentInstance().update("form:infoRegistroEmpresa");
    }
    
    private void modificarInfoRegistroTercero(int valor) {
        infoRegistroTercero = String.valueOf(valor);
        RequestContext.getCurrentInstance().update("form:infoRegistroTercero");
    }

     private void modificarInfoRegistroLovReportes(int valor) {
        infoRegistroLovReportes = String.valueOf(valor);
        RequestContext.getCurrentInstance().update("form:infoRegistroReportes");
    }

     
      private void modificarInfoRegistroSucursales(int valor) {
        infoRegistroSucursal = String.valueOf(valor);
        RequestContext.getCurrentInstance().update("form:infoRegistroSucursal");
    }

    
////sets y gets
     public ParametrosInformes getParametroDeInforme() {
        System.out.println(this.getClass().getName() + ".getParametroDeInforme()");
        try {
            if (parametroDeInforme == null) {
                parametroDeInforme = new ParametrosInformes();
                parametroDeInforme = administrarNReportesSeguridad.parametrosDeReporte();
            }
            if (parametroDeInforme.getTercero() == null) {
                parametroDeInforme.setTercero(new Terceros());
            }
            if (parametroDeInforme.getEmpresa() == null) {
                parametroDeInforme.setEmpresa(new Empresas());
            }
            if (parametroDeInforme.getSucursalPila()== null) {
                parametroDeInforme.setSucursalPila(new SucursalesPila());
            }
            
            return parametroDeInforme;
        } catch (Exception e) {
            System.out.println("Error getParametroDeInforme : " + e);
            return null;
        }
    }    
    
     public void setParametroDeInforme(ParametrosInformes parametroDeInforme) {
        this.parametroDeInforme = parametroDeInforme;
    } 

    public List<Inforeportes> getListaIR() {
       try {
            if (listaIR == null) {
                listaIR = administrarNReportesSeguridad.listInforeportesUsuario();
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

    public List<Inforeportes> getListValInforeportes() {
        if (listValInforeportes == null || listValInforeportes.isEmpty()) {
            listValInforeportes = administrarNReportesSeguridad.listInforeportesUsuario();
        }
        return listValInforeportes;
    }

    public void setListValInforeportes(List<Inforeportes> listValInforeportes) {
        this.listValInforeportes = listValInforeportes;
    }

    public List<Empleados> getListValEmpleados() {
        if (listValEmpleados == null || listValEmpleados.isEmpty()) {
            listValEmpleados = administrarNReportesSeguridad.listEmpleados();
        }
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
        if (listValEmpresas == null || listValEmpresas.isEmpty()) {
            listValEmpresas = administrarNReportesSeguridad.listEmpresas();
        }
        return listValEmpresas;
    }

    public void setListValEmpresas(List<Empresas> listValEmpresas) {
        this.listValEmpresas = listValEmpresas;
    }

    public List<Terceros> getListValTerceros() {
        if (listValTerceros == null || listValTerceros.isEmpty()) {
            listValTerceros = administrarNReportesSeguridad.listTerceros();
        }
        return listValTerceros;
    }

    public void setListValTerceros(List<Terceros> listValTerceros) {
        this.listValTerceros = listValTerceros;
    }

    public List<TiposTrabajadores> getListValTiposTrabajadores() {
        if (listValTiposTrabajadores == null || listValTiposTrabajadores.isEmpty()) {
            listValTiposTrabajadores = administrarNReportesSeguridad.listTiposTrabajadores();
        }
        return listValTiposTrabajadores;
    }

    public void setListValTiposTrabajadores(List<TiposTrabajadores> listValTiposTrabajadores) {
        this.listValTiposTrabajadores = listValTiposTrabajadores;
    }

    public List<Estructuras> getListValEstructuras() {
              if (listValEstructuras == null || listValEstructuras.isEmpty()) {
            listValEstructuras = administrarNReportesSeguridad.listEstructuras();
        }
        return listValEstructuras;
    }

    public void setListValEstructuras(List<Estructuras> listValEstructuras) {
        this.listValEstructuras = listValEstructuras;
    }

    public List<SucursalesPila> getListValSucursales() {
       if(listValSucursales == null || listValSucursales.isEmpty()){
           listValSucursales= administrarNReportesSeguridad.listSucursales(auxiliar);
       }
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

    public ParametrosInformes getNuevoParametroInforme() {
        return nuevoParametroInforme;
    }

    public void setNuevoParametroInforme(ParametrosInformes nuevoParametroInforme) {
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
        return infoRegistroEmpleadoDesde;
    }

    public void setInfoRegistroEmpleadoDesde(String infoRegistroEmpleadoDesde) {
        this.infoRegistroEmpleadoDesde = infoRegistroEmpleadoDesde;
    }

    public String getInfoRegistroEmpleadoHasta() {
        return infoRegistroEmpleadoHasta;
    }

    public void setInfoRegistroEmpleadoHasta(String infoRegistroEmpleadoHasta) {
        this.infoRegistroEmpleadoHasta = infoRegistroEmpleadoHasta;
    }

    public String getInfoRegistroEmpresa() {
        return infoRegistroEmpresa;
    }

    public void setInfoRegistroEmpresa(String infoRegistroEmpresa) {
        this.infoRegistroEmpresa = infoRegistroEmpresa;
    }

    public String getInfoRegistroTercero() {
        return infoRegistroTercero;
    }

    public void setInfoRegistroTercero(String infoRegistroTercero) {
        this.infoRegistroTercero = infoRegistroTercero;
    }

    public String getInfoRegistroSucursal() {
        return infoRegistroSucursal;
    }

    public void setInfoRegistroSucursal(String infoRegistroSucursal) {
        this.infoRegistroSucursal = infoRegistroSucursal;
    }

    public String getInfoRegistroLovReportes() {
        return infoRegistroLovReportes;
    }

    public void setInfoRegistroLovReportes(String infoRegistroLovReportes) {
        this.infoRegistroLovReportes = infoRegistroLovReportes;
    }

    public String getInfoRegistro() {
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
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
   
    
    
}
