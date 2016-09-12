/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Empleados;
import Entidades.Inforeportes;
import Entidades.ParametrosInformes;
import Entidades.Procesos;
import InterfaceAdministrar.AdministarReportesInterface;
import InterfaceAdministrar.AdministrarNReporteContabilidadInterface;
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
public class ControlNReporteContabilidad implements Serializable {

    @EJB
    AdministrarNReporteContabilidadInterface administrarNReporteContabilidad;
    @EJB
    AdministarReportesInterface administarReportes;
    ///
    private ParametrosInformes parametroDeInforme;
    private List<Inforeportes> listaIR;
    private List<Inforeportes> filtrarListInforeportesUsuario;
    private List<Inforeportes> listaIRRespaldo;
    //
    private String reporteGenerar;
    private Inforeportes inforreporteSeleccionado;
    private int bandera;
    private boolean aceptar, cambiosReporte;
    private Column codigoIR, reporteIR, tipoIR;
    private int casilla;
    private ParametrosInformes parametroModificacion;
    private int tipoLista;
    private int posicionReporte;
    private String requisitosReporte;
    //
    private String proceso;
    private boolean permitirIndex;
    private InputText empleadoDesdeParametroL, empleadoHastaParametroL;
    //
    private List<Empleados> listEmpleados;
    private List<Empleados> filtrarListEmpleados;
    private Empleados empleadoSeleccionado;
    private List<Procesos> listProcesos;
    private Procesos procesoSeleccionado;
    private List<Procesos> filtrarListProcesos;
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
    private String infoRegistroProceso, infoRegistroEmpleadoDesde, infoRegistroEmpleadoHasta;
    private String infoRegistro;
    private String nombreReporte, tipoReporte;
    private String pathReporteGenerado;

    public ControlNReporteContabilidad() {

        color = "black";
        decoracion = "none";
        color2 = "black";
        decoracion2 = "none";
        casillaInforReporte = -1;
        actualInfoReporteTabla = new Inforeportes();
        cambiosReporte = true;
        listaInfoReportesModificados = new ArrayList<Inforeportes>();
        altoTabla = "185";
        parametroDeInforme = null;
        listaIR = null;
        listaIRRespaldo = new ArrayList<Inforeportes>();
        bandera = 0;
        aceptar = true;
        casilla = -1;
        parametroModificacion = new ParametrosInformes();
        tipoLista = 0;
        reporteGenerar = "";
        requisitosReporte = "";
        posicionReporte = -1;
        permitirIndex = true;
        listEmpleados = null;
        empleadoSeleccionado = new Empleados();
        listProcesos = null;
        procesoSeleccionado = new Procesos();
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarNReporteContabilidad.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void iniciarPagina() {
        listaIR = null;
        getListaIR();
        if (listaIR != null) {
            infoRegistro = "Cantidad de registros : " + listaIR.size();
        } else {
            infoRegistro = "Cantidad de registros : 0";
        }
    }

    public void requisitosParaReporte() {
        int indiceSeleccion = 0;
        if (tipoLista == 0) {
            indiceSeleccion = listaIR.indexOf(actualInfoReporteTabla);
        }
        if (tipoLista == 1) {
            indiceSeleccion = filtrarListInforeportesUsuario.indexOf(actualInfoReporteTabla);
        }
        parametrosDeReporte(indiceSeleccion);
    }

    public void seleccionRegistro(Inforeportes reporte) {
        Inforeportes reporteS = reporte;

        //reporteS = listaIR.get(reporte);

        defaultPropiedadesParametrosReporte();
        if (reporteS.getFecdesde().equals("SI")) {
            color = "red";
            decoracion = "underline";
            PrimefacesContextUI.actualizar("formParametros");
        }
        if (reporteS.getFechasta().equals("SI")) {
            color2 = "red";
            decoracion2 = "underline";
            PrimefacesContextUI.actualizar("formParametros");
        }
        if (reporteS.getEmdesde().equalsIgnoreCase("SI")) {
            empleadoDesdeParametroL = (InputText) FacesContext.getCurrentInstance().getViewRoot().findComponent("formParametros:empleadoDesdeParametroL");
            //empleadoDesdeParametroL.setStyle("position: absolute; top: 40px; left: 260px;height: 15px;width: 90px;text-decoration: underline; color: red;");
            if (!empleadoDesdeParametroL.getStyle().contains(" color: red;")) {
                empleadoDesdeParametroL.setStyle(empleadoDesdeParametroL.getStyle() + " color: red;");
            }
        } else {
            try {
                if (empleadoDesdeParametroL.getStyle().contains(" color: red;")) {

                    System.out.println("reeemplazarr " + empleadoDesdeParametroL.getStyle().replace(" color: red;", ""));
                    empleadoDesdeParametroL.setStyle(empleadoDesdeParametroL.getStyle().replace(" color: red;", ""));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        PrimefacesContextUI.actualizar("formParametros:empleadoDesdeParametroL");
        System.out.println("reporteSeleccionado.getEmhasta(): " + actualInfoReporteTabla.getEmhasta());
        if (actualInfoReporteTabla.getEmhasta().equals("SI")) {
            empleadoHastaParametroL = (InputText) FacesContext.getCurrentInstance().getViewRoot().findComponent("formParametros:empleadoHastaParametro");
            //empleadoHastaParametro.setStyle("position: absolute; top: 41px; left: 330px; height: 10px; font-size: 11px; width: 90px; color: red;");
            empleadoHastaParametroL.setStyle(empleadoHastaParametroL.getStyle() + "color: red;");
            PrimefacesContextUI.actualizar("formParametros:empleadoHastaParametro");
        }
 
//        if (tipoLista == 0) {
//            indiceSeleccion = listaIR.indexOf(actualInfoReporteTabla);
//        }
//        if (tipoLista == 1) {
//            indiceSeleccion = filtrarListInforeportesUsuario.indexOf(actualInfoReporteTabla);
//        }
//        resaltoParametrosParaReporte(indiceSeleccion);
    }

//    public void cambiarIndexInforeporte(int i, int c) {
//        casillaInforReporte = c;
//        casilla = -1;
//        if (tipoLista == 0) {
//            setActualInfoReporteTabla(listaIR.get(i));
//        }
//        if (tipoLista == 1) {
//            setActualInfoReporteTabla(filtrarListInforeportesUsuario.get(i));
//        }
//        resaltoParametrosParaReporte(i);
//    }

    public void dispararDialogoGuardarCambios() {
        RequestContext context = RequestContext.getCurrentInstance();
        PrimefacesContextUI.ejecutar("PF('confirmarGuardar').show()");

    }

    public void guardarYSalir() {
        guardarCambios();
        salir();
    }

    public void guardarCambios() {
        try {
            if (cambiosReporte == false) {
                if (parametroDeInforme != null) {
                    if (parametroDeInforme.getProceso().getSecuencia() == null) {
                        parametroDeInforme.setProceso(null);
                    }
                    administrarNReporteContabilidad.modificarParametrosInformes(parametroDeInforme);
                }
            }
            if (!listaInfoReportesModificados.isEmpty()) {
                administrarNReporteContabilidad.guardarCambiosInfoReportes(listaInfoReportesModificados);
            }
            cambiosReporte = true;
            RequestContext context = RequestContext.getCurrentInstance();

            getListaIR();
            if (listaIR != null) {
                infoRegistro = "Cantidad de registros : " + listaIR.size();
            } else {
                infoRegistro = "Cantidad de registros : 0";
            }
            PrimefacesContextUI.actualizar("form:informacionRegistro");
            PrimefacesContextUI.actualizar("form:ACEPTAR");
            FacesMessage msg = new FacesMessage("Información", "Los datos se guardaron con Éxito.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            PrimefacesContextUI.actualizar("form:growl");
        } catch (Exception e) {
            System.out.println("Error en guardar Cambios Controlador : " + e.toString());
            FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado, intente nuevamente");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            PrimefacesContextUI.actualizar("form:growl");
        }
    }

    public void posicionCelda(int i) {
        if (permitirIndex) {
            casilla = i;
            casillaInforReporte = -1;
            emplDesde = parametroDeInforme.getCodigoempleadodesde();
            fechaDesde = parametroDeInforme.getFechadesde();
            emplHasta = parametroDeInforme.getCodigoempleadohasta();
            fechaHasta = parametroDeInforme.getFechahasta();
            if (casilla == 5) {
                proceso = parametroDeInforme.getProceso().getDescripcion();
            }
        }
    }

    public void editarCelda() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (casilla >= 1) {
            if (casilla == 1) {
                PrimefacesContextUI.actualizar("formularioDialogos:editarFechaDesde");
                PrimefacesContextUI.ejecutar("PF('editarFechaDesde').show()");
            }
            if (casilla == 2) {
                PrimefacesContextUI.actualizar("formularioDialogos:empleadoDesde");
                PrimefacesContextUI.ejecutar("PF('empleadoDesde').show()");
            }

            if (casilla == 3) {
                PrimefacesContextUI.actualizar("formularioDialogos:editarFechaHasta");
                PrimefacesContextUI.ejecutar("PF('editarFechaHasta').show()");
            }
            if (casilla == 4) {
                PrimefacesContextUI.actualizar("formularioDialogos:empleadoHasta");
                PrimefacesContextUI.ejecutar("PF('empleadoHasta').show()");
            }
            if (casilla == 5) {
                PrimefacesContextUI.actualizar("formularioDialogos:proceso");
                PrimefacesContextUI.ejecutar("PF('proceso').show()");
            }
            casilla = -1;
        }
        if (casillaInforReporte >= 1) {
            if (casillaInforReporte == 1) {
                PrimefacesContextUI.actualizar("formularioDialogos:infoReporteCodigoD");
                PrimefacesContextUI.ejecutar("PF('infoReporteCodigoD').show()");
            }
            if (casillaInforReporte == 2) {
                PrimefacesContextUI.actualizar("formularioDialogos:infoReporteNombreD");
                PrimefacesContextUI.ejecutar("PF('infoReporteNombreD').show()");
            }
            casillaInforReporte = -1;
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void generarReporte(Inforeportes reporte) {
        System.out.println(this.getClass().getName() + ".generarReporte()");
        RequestContext context = RequestContext.getCurrentInstance();
        seleccionRegistro(reporte);
        guardarCambios();
        PrimefacesContextUI.ejecutar("PF('generandoReporte.show();");
        PrimefacesContextUI.ejecutar("PF('generarDocumentoReporte();");
//        if (cambiosReporte == true) {
//            if (tipoLista == 0) {
//                reporteGenerar = listaIR.get(reporte).getNombre();
//                posicionReporte = reporte;
//            }
//            if (tipoLista == 1) {
//                if (listaIR.contains(filtrarListInforeportesUsuario.get(i))) {
//                    int posicion = listaIR.indexOf(filtrarListInforeportesUsuario.get(i));
//                    reporteGenerar = listaIR.get(posicion).getNombre();
//                    posicionReporte = posicion;
//                }
//            }
//            mostrarDialogoGenerarReporte();
//        } else {
//            RequestContext context = RequestContext.getCurrentInstance();
//            PrimefacesContextUI.ejecutar("PF('confirmarGuardarSinSalida').show()");
//        }
    }

    public void generarDocumentoReporte() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (actualInfoReporteTabla != null) {
            System.out.println("generando reporte - ingreso al if");
            nombreReporte = actualInfoReporteTabla.getNombrereporte();
            tipoReporte = actualInfoReporteTabla.getTipo();

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

    public void mostrarDialogoGenerarReporte() {
        defaultPropiedadesParametrosReporte();
        RequestContext context = RequestContext.getCurrentInstance();
        PrimefacesContextUI.actualizar("formDialogos:reporteAGenerar");
        PrimefacesContextUI.ejecutar("PF('reporteAGenerar').show()");
    }

    public void cancelarGenerarReporte() {
        reporteGenerar = "";
        posicionReporte = -1;
        if (bandera == 1) {
            altoTabla = "185";
            codigoIR = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:reportesContabilidad:codigoIR");
            codigoIR.setFilterStyle("display: none; visibility: hidden;");
            reporteIR = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:reportesContabilidad:reporteIR");
            reporteIR.setFilterStyle("display: none; visibility: hidden;");
            tipoIR = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:reportesContabilidad:tipoIR");
            tipoIR.setFilterStyle("display: none; visibility: hidden;");
            PrimefacesContextUI.actualizar("form:reportesContabilidad");
            bandera = 0;
            filtrarListInforeportesUsuario = null;
            tipoLista = 0;
        }
    }

    public void mostrarDialogoBuscarReporte() {
        try {
            listaIR = administrarNReporteContabilidad.listInforeportesUsuario();
            RequestContext context = RequestContext.getCurrentInstance();
            PrimefacesContextUI.actualizar("form:ReportesDialogo");
            PrimefacesContextUI.ejecutar("PF('ReportesDialogo').show()");
        } catch (Exception e) {
            System.out.println("Error mostrarDialogoBuscarReporte : " + e.toString());
        }
    }

    public void salir() {
        if (bandera == 1) {
            altoTabla = "185";
            codigoIR = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:reportesContabilidad:codigoIR");
            codigoIR.setFilterStyle("display: none; visibility: hidden;");
            reporteIR = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:reportesContabilidad:reporteIR");
            reporteIR.setFilterStyle("display: none; visibility: hidden;");
            tipoIR = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:reportesContabilidad:tipoIR");
            tipoIR.setFilterStyle("display: none; visibility: hidden;");
            PrimefacesContextUI.actualizar("form:reportesContabilidad");
            bandera = 0;
            filtrarListInforeportesUsuario = null;
            tipoLista = 0;
        }
        listEmpleados = null;
        listaIR = null;
        listProcesos = null;
        parametroDeInforme = null;
        parametroModificacion = null;
        listaIRRespaldo = null;
        casilla = -1;
        procesoSeleccionado = null;
        listaInfoReportesModificados.clear();
        cambiosReporte = true;
        RequestContext context = RequestContext.getCurrentInstance();
        PrimefacesContextUI.actualizar("form:ACEPTAR");
    }

    public void cancelarYSalir() {
        cancelarModificaciones();
        salir();
    }

    public void cancelarModificaciones() {
        if (bandera == 1) {
            defaultPropiedadesParametrosReporte();
            codigoIR = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:reportesLaboral:codigoIR");
            codigoIR.setFilterStyle("display: none; visibility: hidden;");
            reporteIR = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:reportesLaboral:reporteIR");
            reporteIR.setFilterStyle("display: none; visibility: hidden;");
            tipoIR = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:reportesLaboral:tipoIR");
            tipoIR.setFilterStyle("display: none; visibility: hidden;");
            altoTabla = "185";
            PrimefacesContextUI.actualizar("form:reportesLaboral");
            bandera = 0;
            filtrarListInforeportesUsuario = null;
            tipoLista = 0;
        }
        defaultPropiedadesParametrosReporte();
        listaIR = null;
        listaIRRespaldo = null;
        casilla = -1;
        listaInfoReportesModificados.clear();
        cambiosReporte = true;
        getParametroDeInforme();
        refrescarParametros();
        getListaIR();
        if (listaIR != null) {
            infoRegistro = "Cantidad de registros : " + listaIR.size();
        } else {
            infoRegistro = "Cantidad de registros : 0";
        }
        RequestContext context = RequestContext.getCurrentInstance();
        PrimefacesContextUI.actualizar("form:informacionRegistro");
        PrimefacesContextUI.actualizar("form:ACEPTAR");
        PrimefacesContextUI.actualizar("form:reportesContabilidad");
    }

    public void modificacionTipoReporte(int i) {
        if (tipoLista == 0) {
            if (listaInfoReportesModificados.isEmpty()) {
                listaInfoReportesModificados.add(listaIR.get(i));
            } else {
                if ((!listaInfoReportesModificados.isEmpty()) && (!listaInfoReportesModificados.contains(listaIR.get(i)))) {
                    listaInfoReportesModificados.add(listaIR.get(i));
                } else {
                    int posicion = listaInfoReportesModificados.indexOf(listaIR.get(i));
                    listaInfoReportesModificados.set(posicion, listaIR.get(i));
                }
            }
        }
        if (tipoLista == 1) {
            if (listaInfoReportesModificados.isEmpty()) {
                listaInfoReportesModificados.add(filtrarListInforeportesUsuario.get(i));
            } else {
                if ((!listaInfoReportesModificados.isEmpty()) && (!listaInfoReportesModificados.contains(filtrarListInforeportesUsuario.get(i)))) {
                    listaInfoReportesModificados.add(filtrarListInforeportesUsuario.get(i));
                } else {
                    int posicion = listaInfoReportesModificados.indexOf(filtrarListInforeportesUsuario.get(i));
                    listaInfoReportesModificados.set(posicion, filtrarListInforeportesUsuario.get(i));
                }
            }
        }
        cambiosReporte = false;
        RequestContext context = RequestContext.getCurrentInstance();
        PrimefacesContextUI.actualizar("form:ACEPTAR");
    }

    public void mostrarDialogosListas() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (casilla == 2) {
            PrimefacesContextUI.actualizar("form:EmpleadoDesdeDialogo");
            PrimefacesContextUI.ejecutar("PF('EmpleadoDesdeDialogo').show()");
        }
        if (casilla == 4) {
            PrimefacesContextUI.actualizar("form:EmpleadoHastaDialogo");
            PrimefacesContextUI.ejecutar("PF('EmpleadoHastaDialogo').show()");
        }
        if (casilla == 5) {
            PrimefacesContextUI.actualizar("form:ProcesoDialogo");
            PrimefacesContextUI.ejecutar("PF('ProcesoDialogo').show()");
        }
    }

    public void actualizarProceso() {
        permitirIndex = true;
        parametroDeInforme.setProceso(procesoSeleccionado);
        parametroModificacion = parametroDeInforme;
        procesoSeleccionado = null;
        aceptar = true;
        filtrarListProcesos = null;
        cambiosReporte = false;
        RequestContext context = RequestContext.getCurrentInstance();
        /*
         * PrimefacesContextUI.actualizar("form:ProcesoDialogo");
         * PrimefacesContextUI.actualizar("form:lovProceso"); PrimefacesContextUI.actualizar("form:aceptarPro");
         */
        context.reset("form:lovProceso:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovProceso').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('ProcesoDialogo').hide()");
        PrimefacesContextUI.actualizar("form:ACEPTAR");
        PrimefacesContextUI.actualizar("formParametros:procesoParametroL");
    }

    public void cancelarCambioProceso() {
        procesoSeleccionado = null;
        aceptar = true;
        filtrarListProcesos = null;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovProceso:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovProceso').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('ProcesoDialogo').hide()");
    }

    public void actualizarEmplDesde() {
        permitirIndex = true;
        parametroDeInforme.setCodigoempleadodesde(empleadoSeleccionado.getCodigoempleado());
        parametroModificacion = parametroDeInforme;
        empleadoSeleccionado = null;
        aceptar = true;
        filtrarListEmpleados = null;
        cambiosReporte = false;
        RequestContext context = RequestContext.getCurrentInstance();
        /*
         * PrimefacesContextUI.actualizar("form:EmpleadoDesdeDialogo");
         * PrimefacesContextUI.actualizar("form:lovEmpleadoDesde");
         * PrimefacesContextUI.actualizar("form:aceptarED");
         */
        context.reset("form:lovEmpleadoDesde:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovEmpleadoDesde').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('EmpleadoDesdeDialogo').hide()");
        PrimefacesContextUI.actualizar("form:ACEPTAR");
        PrimefacesContextUI.actualizar("formParametros:empleadoDesdeParametroL");
    }

    public void cancelarCambioEmplDesde() {
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
        permitirIndex = true;
        parametroDeInforme.setCodigoempleadohasta(empleadoSeleccionado.getCodigoempleado());
        parametroModificacion = parametroDeInforme;
        empleadoSeleccionado = null;
        aceptar = true;
        filtrarListEmpleados = null;
        cambiosReporte = false;
        RequestContext context = RequestContext.getCurrentInstance();
        /*
         * PrimefacesContextUI.actualizar("form:EmpleadoHastaDialogo");
         * PrimefacesContextUI.actualizar("form:lovEmpleadoHasta");
         * PrimefacesContextUI.actualizar("form:aceptarEH");
         */
        context.reset("form:lovEmpleadoHasta:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovEmpleadoHasta').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('EmpleadoHastaDialogo').hide()");
        PrimefacesContextUI.actualizar("form:ACEPTAR");
        PrimefacesContextUI.actualizar("formParametros:empleadoHastaParametroL");
    }

    public void cancelarCambioEmplHasta() {
        empleadoSeleccionado = null;
        aceptar = true;
        filtrarListEmpleados = null;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovEmpleadoHasta:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovEmpleadoHasta').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('EmpleadoHastaDialogo').hide()");
    }

    public void autocompletarGeneral(String campoConfirmar, String valorConfirmar) {
        RequestContext context = RequestContext.getCurrentInstance();
        int indiceUnicoElemento = -1;
        int coincidencias = 0;
        if (campoConfirmar.equalsIgnoreCase("PROCESO")) {
            if (!valorConfirmar.isEmpty()) {
                parametroDeInforme.getProceso().setDescripcion(proceso);
                for (int i = 0; i < listProcesos.size(); i++) {
                    if (listProcesos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    parametroDeInforme.setProceso(listProcesos.get(indiceUnicoElemento));
                    parametroModificacion = parametroDeInforme;
                    listProcesos.clear();
                    getListProcesos();
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
                listProcesos.clear();
                getListProcesos();
                cambiosReporte = false;
                PrimefacesContextUI.actualizar("form:ACEPTAR");
            }
        }
        if (campoConfirmar.equalsIgnoreCase("DESDE")) {
            if (!valorConfirmar.isEmpty()) {
                parametroDeInforme.setCodigoempleadodesde(emplDesde);
                for (int i = 0; i < listEmpleados.size(); i++) {
                    if (listEmpleados.get(i).getCodigoempleadoSTR().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    parametroDeInforme.setCodigoempleadodesde(listEmpleados.get(indiceUnicoElemento).getCodigoempleado());
                    parametroModificacion = parametroDeInforme;
                    listProcesos.clear();
                    getListProcesos();
                    cambiosReporte = false;
                    PrimefacesContextUI.actualizar("form:ACEPTAR");
                } else {
                    permitirIndex = false;
                    PrimefacesContextUI.actualizar("form:EmpleadoDesdeDialogo");
                    PrimefacesContextUI.ejecutar("PF('EmpleadoDesdeDialogo').show()");
                }
            } else {
                parametroDeInforme.setCodigoempleadodesde(new BigDecimal("0"));
                parametroModificacion = parametroDeInforme;
                listProcesos.clear();
                getListProcesos();
                cambiosReporte = false;
                PrimefacesContextUI.actualizar("form:ACEPTAR");
            }
        }
        if (campoConfirmar.equalsIgnoreCase("HASTA")) {
            if (!valorConfirmar.isEmpty()) {
                parametroDeInforme.setCodigoempleadohasta(emplHasta);
                for (int i = 0; i < listEmpleados.size(); i++) {
                    if (listEmpleados.get(i).getCodigoempleadoSTR().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    parametroDeInforme.setCodigoempleadohasta(listEmpleados.get(indiceUnicoElemento).getCodigoempleado());
                    parametroModificacion = parametroDeInforme;
                    listEmpleados.clear();
                    getListEmpleados();
                    cambiosReporte = false;
                    PrimefacesContextUI.actualizar("form:ACEPTAR");
                } else {
                    permitirIndex = false;
                    PrimefacesContextUI.actualizar("form:EmpleadoHastaDialogo");
                    PrimefacesContextUI.ejecutar("PF('EmpleadoHastaDialogo').show()");
                }
            } else {
                parametroDeInforme.setCodigoempleadohasta(new BigDecimal("9999999999999999999999"));
                parametroModificacion = parametroDeInforme;
                listEmpleados.clear();
                getListEmpleados();
                cambiosReporte = false;
                PrimefacesContextUI.actualizar("form:ACEPTAR");
            }
        }
    }

    public void refrescarParametros() {
        defaultPropiedadesParametrosReporte();
        parametroDeInforme = null;
        parametroModificacion = null;
        listEmpleados = null;
        listProcesos = null;
        listEmpleados = administrarNReporteContabilidad.listEmpleados();
        listProcesos = administrarNReporteContabilidad.listProcesos();
        parametroDeInforme = administrarNReporteContabilidad.parametrosDeReporte();
        if (parametroDeInforme.getProceso() == null) {
            parametroDeInforme.setProceso(new Procesos());
        }
        RequestContext context = RequestContext.getCurrentInstance();
        PrimefacesContextUI.actualizar("formParametros:fechaDesdeParametroL");
        PrimefacesContextUI.actualizar("formParametros:empleadoDesdeParametroL");
        PrimefacesContextUI.actualizar("formParametros:fechaHastaParametroL");
        PrimefacesContextUI.actualizar("formParametros:empleadoHastaParametroL");
        PrimefacesContextUI.actualizar("formParametros:estadoParametroL");
        PrimefacesContextUI.actualizar("formParametros:procesoParametroL");
    }

    public void activarCtrlF11() {
        if (bandera == 0) {
            altoTabla = "165";
            codigoIR = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:reportesContabilidad:codigoIR");
            codigoIR.setFilterStyle("width: 85%;");
            reporteIR = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:reportesContabilidad:reporteIR");
            reporteIR.setFilterStyle("width: 85%;");
            tipoIR = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:reportesContabilidad:tipoIR");
            tipoIR.setFilterStyle("width: 85%;");
            PrimefacesContextUI.actualizar("form:reportesContabilidad");
            tipoLista = 1;
            bandera = 1;
        } else if (bandera == 1) {
            altoTabla = "185";
            defaultPropiedadesParametrosReporte();
            codigoIR = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:reportesContabilidad:codigoIR");
            codigoIR.setFilterStyle("display: none; visibility: hidden;");
            reporteIR = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:reportesContabilidad:reporteIR");
            reporteIR.setFilterStyle("display: none; visibility: hidden;");
            tipoIR = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:reportesContabilidad:tipoIR");
            tipoIR.setFilterStyle("display: none; visibility: hidden;");
            PrimefacesContextUI.actualizar("form:reportesContabilidad");
            bandera = 0;
            filtrarListInforeportesUsuario = null;
            tipoLista = 0;
        }

    }

    public void reporteSeleccionado(int i) {
        System.out.println("Posicion del reporte : " + i);
    }

    public void defaultPropiedadesParametrosReporte() {

        color = "black";
        decoracion = "none";
        color2 = "black";
        decoracion2 = "none";
        PrimefacesContextUI.actualizar("formParametros");
//
//        empleadoDesdeParametroL = (InputText) FacesContext.getCurrentInstance().getViewRoot().findComponent("formParametros:empleadoDesdeParametroL");
//        empleadoDesdeParametroL.setStyle("position: absolute; top: 40px; left: 260px;height: 15px;width: 90px;");
//        PrimefacesContextUI.actualizar("formParametros:empleadoDesdeParametroL");
//
//        empleadoHastaParametroL = (InputText) FacesContext.getCurrentInstance().getViewRoot().findComponent("formParametros:empleadoHastaParametroL");
//        empleadoHastaParametroL.setStyle("position: absolute; top: 40px; left: 400px;height: 15px;width: 90px;");
//        PrimefacesContextUI.actualizar("formParametros:empleadoHastaParametroL");

    }

    public void modificarParametroInforme() {
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
            RequestContext context = RequestContext.getCurrentInstance();
            PrimefacesContextUI.actualizar("formParametros");
            PrimefacesContextUI.ejecutar("PF('errorRegNew').show()");
        }
    }

//    public void resaltoParametrosParaReporte(Inforeportes reporte) {
//        Inforeportes reporteS = reporte;
//
//        //reporteS = listaIR.get(reporte);
//
//        defaultPropiedadesParametrosReporte();
//        if (reporteS.getFecdesde().equals("SI")) {
//            color = "red";
//            decoracion = "underline";
//            PrimefacesContextUI.actualizar("formParametros");
//        }
//        if (reporteS.getFechasta().equals("SI")) {
//            color2 = "red";
//            decoracion2 = "underline";
//            PrimefacesContextUI.actualizar("formParametros");
//        }
//        if (reporteS.getEmdesde().equalsIgnoreCase("SI")) {
//            empleadoDesdeParametroL = (InputText) FacesContext.getCurrentInstance().getViewRoot().findComponent("formParametros:empleadoDesdeParametroL");
//            //empleadoDesdeParametroL.setStyle("position: absolute; top: 40px; left: 260px;height: 15px;width: 90px;text-decoration: underline; color: red;");
//            if (!empleadoDesdeParametroL.getStyle().contains(" color: red;")) {
//                empleadoDesdeParametroL.setStyle(empleadoDesdeParametroL.getStyle() + " color: red;");
//            }
//        } else {
//            try {
//                if (empleadoDesdeParametroL.getStyle().contains(" color: red;")) {
//
//                    System.out.println("reeemplazarr " + empleadoDesdeParametroL.getStyle().replace(" color: red;", ""));
//                    empleadoDesdeParametroL.setStyle(empleadoDesdeParametroL.getStyle().replace(" color: red;", ""));
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        PrimefacesContextUI.actualizar("formParametros:empleadoDesdeParametroL");
//        
//        if (reporteS.getEmhasta().equalsIgnoreCase("SI")) {
//            empleadoHastaParametroL = (InputText) FacesContext.getCurrentInstance().getViewRoot().findComponent("formParametros:empleadoHastaParametroL");
//            empleadoHastaParametroL.setStyle("position: absolute; top: 40px; left: 400px;height: 15px;width: 90px; text-decoration: underline; color: red;");
//            PrimefacesContextUI.actualizar("formParametros:empleadoHastaParametroL");
//        }
//    }
    public void parametrosDeReporte(int i) {
        String cadena = "";
        Inforeportes reporteS = new Inforeportes();
        if (tipoLista == 0) {
            reporteS = listaIR.get(i);
        }
        if (tipoLista == 1) {
            reporteS = filtrarListInforeportesUsuario.get(i);
        }
        if (reporteS.getFecdesde().equals("SI")) {
            cadena = cadena + "- Fecha Desde -";
        }
        if (reporteS.getFechasta().equals("SI")) {
            cadena = cadena + "- Fecha Hasta -";
        }
        if (reporteS.getEmdesde().equals("SI")) {
            cadena = cadena + "- Empleado Desde -";
        }
        if (reporteS.getEmhasta().equals("SI")) {
            cadena = cadena + "- Empleado Hasta -";
        }
        setRequisitosReporte(cadena);
        if (!requisitosReporte.isEmpty()) {
            RequestContext context = RequestContext.getCurrentInstance();
            PrimefacesContextUI.actualizar("formDialogos:requisitosReporte");
            PrimefacesContextUI.ejecutar("PF('requisitosReporte').show()");
        }
    }

//    public void generarReporte() throws IOException {
//        String nombreReporte, tipoReporte;
//        String pathReporteGenerado = null;
//        if (tipoLista == 0) {
//            nombreReporte = listaIR.get(indice).getNombrereporte();
//            tipoReporte = listaIR.get(indice).getTipo();
//        } else {
//            nombreReporte = filtrarListInforeportesUsuario.get(indice).getNombrereporte();
//            tipoReporte = filtrarListInforeportesUsuario.get(indice).getTipo();
//        }
//        if (nombreReporte != null && tipoReporte != null) {
//            pathReporteGenerado = administarReportes.generarReporte(nombreReporte, tipoReporte);
//        }
//        if (pathReporteGenerado != null) {
//            exportarReporte(pathReporteGenerado);
//        }
//    }
    public void exportarReporte(String rutaReporteGenerado) throws IOException {
        File reporte = new File(rutaReporteGenerado);
        FacesContext ctx = FacesContext.getCurrentInstance();
        FileInputStream fis = new FileInputStream(reporte);
        byte[] bytes = new byte[1024];
        int read;
        if (!ctx.getResponseComplete()) {
            String fileName = reporte.getName();
            HttpServletResponse response = (HttpServletResponse) ctx.getExternalContext().getResponse();
            response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
            ServletOutputStream out = response.getOutputStream();

            while ((read = fis.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            out.flush();
            out.close();
            System.out.println("\nDescargado\n");
            ctx.responseComplete();
        }
    }

    public void eventoFiltrar() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
        RequestContext context = RequestContext.getCurrentInstance();
        infoRegistro = "Cantidad de Registros: " + filtrarListInforeportesUsuario.size();
        PrimefacesContextUI.actualizar("form:informacionRegistro");
    }

    public void cancelarRequisitosReporte() {
        requisitosReporte = "";
    }

    public ParametrosInformes getParametroDeInforme() {
        try {
            if (parametroDeInforme == null) {
                parametroDeInforme = new ParametrosInformes();
                parametroDeInforme = administrarNReporteContabilidad.parametrosDeReporte();
            }

            if (parametroDeInforme.getProceso() == null) {
                parametroDeInforme.setProceso(new Procesos());
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
                //listaIR = new ArrayList<Inforeportes>();
                listaIR = administrarNReporteContabilidad.listInforeportesUsuario();
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

    public ParametrosInformes getParametroModificacion() {
        return parametroModificacion;
    }

    public void setParametroModificacion(ParametrosInformes parametroModificacion) {
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

    public List<Empleados> getListEmpleados() {
        try {
            if (listEmpleados == null) {
                //  listEmpleados = new ArrayList<Empleados>();
                listEmpleados = administrarNReporteContabilidad.listEmpleados();
            }
        } catch (Exception e) {
            System.out.println("Error en getListEmpleados : " + e.toString());
            return null;
        }
        return listEmpleados;
    }

    public void setListEmpleados(List<Empleados> listEmpleados) {
        this.listEmpleados = listEmpleados;
    }

    public List<Empleados> getFiltrarListEmpleados() {
        return filtrarListEmpleados;
    }

    public void setFiltrarListEmpleados(List<Empleados> filtrarListEmpleados) {
        this.filtrarListEmpleados = filtrarListEmpleados;
    }

    public Empleados getEmpleadoSeleccionado() {
        return empleadoSeleccionado;
    }

    public void setEmpleadoSeleccionado(Empleados empleadoSeleccionado) {
        this.empleadoSeleccionado = empleadoSeleccionado;
    }

    public List<Procesos> getListProcesos() {
        try {
            if (listProcesos == null) {
                //listProcesos = new ArrayList<Procesos>();
                listProcesos = administrarNReporteContabilidad.listProcesos();
            }
            return listProcesos;
        } catch (Exception e) {
            System.out.println("Error getListProcesos : " + e.toString());
            return null;
        }
    }

    public void setListProcesos(List<Procesos> listProcesos) {
        this.listProcesos = listProcesos;
    }

    public Procesos getProcesoSeleccionado() {
        return procesoSeleccionado;
    }

    public void setProcesoSeleccionado(Procesos procesoSeleccionado) {
        this.procesoSeleccionado = procesoSeleccionado;
    }

    public List<Procesos> getFiltrarListProcesos() {
        return filtrarListProcesos;
    }

    public void setFiltrarListProcesos(List<Procesos> filtrarListProcesos) {
        this.filtrarListProcesos = filtrarListProcesos;
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

    public Inforeportes getActualInfoReporteTabla() {
        getListaIR();
        if (listaIR != null) {
            int tam = listaIR.size();
            if (tam > 0) {
                actualInfoReporteTabla = listaIR.get(0);
            }
        }
        return actualInfoReporteTabla;
    }

    public void setActualInfoReporteTabla(Inforeportes actualInfoReporteTabla) {
        this.actualInfoReporteTabla = actualInfoReporteTabla;
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

    public String getInfoRegistroProceso() {
        getListProcesos();
        if (listProcesos != null) {
            infoRegistroProceso = "Cantidad de registros : " + listProcesos.size();
        } else {
            infoRegistroProceso = "Cantidad de registros : 0";
        }
        return infoRegistroProceso;
    }

    public void setInfoRegistroProceso(String infoRegistroProceso) {
        this.infoRegistroProceso = infoRegistroProceso;
    }

    public String getInfoRegistroEmpleadoDesde() {
        getListEmpleados();
        if (listEmpleados != null) {
            infoRegistroEmpleadoDesde = "Cantidad de registros : " + listEmpleados.size();
        } else {
            infoRegistroEmpleadoDesde = "Cantidad de registros : 0";
        }
        return infoRegistroEmpleadoDesde;
    }

    public void setInfoRegistroEmpleadoDesde(String infoRegistroEmpleadoDesde) {
        this.infoRegistroEmpleadoDesde = infoRegistroEmpleadoDesde;
    }

    public String getInfoRegistroEmpleadoHasta() {
        getListEmpleados();
        if (listEmpleados != null) {
            infoRegistroEmpleadoHasta = "Cantidad de registros : " + listEmpleados.size();
        } else {
            infoRegistroEmpleadoHasta = "Cantidad de registros : 0";
        }
        return infoRegistroEmpleadoHasta;
    }

    public void setInfoRegistroEmpleadoHasta(String infoRegistroEmpleadoHasta) {
        this.infoRegistroEmpleadoHasta = infoRegistroEmpleadoHasta;
    }

    public String getInfoRegistro() {
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }
}
