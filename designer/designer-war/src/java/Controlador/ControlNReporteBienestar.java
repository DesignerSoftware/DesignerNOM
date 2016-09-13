/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import utilidadesUI.PrimefacesContextUI;
import Entidades.Actividades;
import Entidades.Empleados;
import Entidades.Inforeportes;
import Entidades.ParametrosInformes;
import InterfaceAdministrar.AdministarReportesInterface;
import InterfaceAdministrar.AdministrarNReporteBienestarInterface;
import java.io.File;
import java.io.FileInputStream;
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
import org.primefaces.component.column.Column;
import org.primefaces.component.inputtext.InputText;
import org.primefaces.context.RequestContext;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author user
 */
@ManagedBean
@SessionScoped
public class ControlNReporteBienestar implements Serializable {

    @EJB
    AdministrarNReporteBienestarInterface administrarNReporteBienestar;
    @EJB
    AdministarReportesInterface administarReportes;
    ///
    private ParametrosInformes parametroDeInforme;
    private List<Inforeportes> listaIR;
    private List<Inforeportes> filtrarListInforeportesUsuario;
    private List<Inforeportes> listaIRRespaldo;
    private List<Inforeportes> lovInforeportes;
    private List<Inforeportes> filtrarLovInforeportes;
    private String infoRegistro;
    //
    private String reporteGenerar;
    private Inforeportes inforreporteSeleccionado;
    private int bandera;
    private boolean aceptar;
    private Column codigoIR, reporteIR, tipoIR;
    private int casilla;
    private ParametrosInformes parametroModificacion;
    private int tipoLista;
    private int posicionReporte;
    private String requisitosReporte;
    //
    private String actividad;
    private boolean permitirIndex, cambiosReporte;
    private InputText empleadoDesdeParametroL, empleadoHastaParametroL;
    //
    private List<Empleados> listEmpleados;
    private List<Empleados> filtrarListEmpleados;
    private Empleados empleadoSeleccionado;
    private List<Actividades> listActividades;
    private Actividades actividadSeleccionada;
    private List<Actividades> filtrarListActividades;
    //
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
    private boolean activoMostrarTodos, activoBuscarReporte;

    //
    private String infoRegistroActividad, infoRegistroEmpleadoDesde, infoRegistroEmpleadoHasta, infoRegistroReportes;

    public ControlNReporteBienestar() {
        activoMostrarTodos = true;
        activoBuscarReporte = false;
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
        filtrarListInforeportesUsuario = null;
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
        listActividades = null;
        actividadSeleccionada = new Actividades();
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarNReporteBienestar.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void iniciarPagina() {
        activoMostrarTodos = true;
        activoBuscarReporte = false;
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

    public void seleccionRegistro() {
        int indiceSeleccion = 0;
        if (tipoLista == 0) {
            indiceSeleccion = listaIR.indexOf(actualInfoReporteTabla);
        }
        if (tipoLista == 1) {
            indiceSeleccion = filtrarListInforeportesUsuario.indexOf(actualInfoReporteTabla);
        }
        resaltoParametrosParaReporte(indiceSeleccion);
    }

    public void cambiarIndexInforeporte(int i, int c) {
        casillaInforReporte = c;
        casilla = -1;
        if (tipoLista == 0) {
            setActualInfoReporteTabla(listaIR.get(i));
        }
        if (tipoLista == 1) {
            setActualInfoReporteTabla(filtrarListInforeportesUsuario.get(i));
        }
        resaltoParametrosParaReporte(i);
    }

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
                    if (parametroDeInforme.getActividadbienestar().getSecuencia() == null) {
                        parametroDeInforme.setActividadbienestar(null);
                    }
                    administrarNReporteBienestar.modificarParametrosInformes(parametroDeInforme);
                }
            }
            if (!listaInfoReportesModificados.isEmpty()) {
                administrarNReporteBienestar.guardarCambiosInfoReportes(listaInfoReportesModificados);
            }
            cambiosReporte = true;
            RequestContext context = RequestContext.getCurrentInstance();
            getListaIR();
            if (listaIR != null) {
                infoRegistro = "Cantidad de registros : " + listaIR.size();
            } else {
                infoRegistro = "Cantidad de registros : 0";
            }
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            FacesMessage msg = new FacesMessage("Información", "Los datos se guardaron con Éxito.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        } catch (Exception e) {
            System.out.println("Error en guardar Cambios Controlador : " + e.toString());
            FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado, intente nuevamente");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
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
                actividad = parametroDeInforme.getActividadbienestar().getDescripcion();
            }
        }
    }

    public void editarCelda() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (casilla >= 1) {
            if (casilla == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaDesde");
                PrimefacesContextUI.ejecutar("PF('editarFechaDesde').show()");
            }
            if (casilla == 2) {
                RequestContext.getCurrentInstance().update("formularioDialogos:empleadoDesde");
                PrimefacesContextUI.ejecutar("PF('empleadoDesde').show()");
            }

            if (casilla == 3) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaHasta");
                PrimefacesContextUI.ejecutar("PF('editarFechaHasta').show()");
            }
            if (casilla == 4) {
                RequestContext.getCurrentInstance().update("formularioDialogos:empleadoHasta");
                PrimefacesContextUI.ejecutar("PF('empleadoHasta').show()");
            }
            if (casilla == 5) {
                RequestContext.getCurrentInstance().update("formularioDialogos:actividad");
                PrimefacesContextUI.ejecutar("PF('actividad').show()");
            }
            casilla = -1;
        }
        if (casillaInforReporte >= 1) {
            System.out.println("actualinforeporte : " + actualInfoReporteTabla.getCodigo());
            System.out.println("actualinforeporte : " + actualInfoReporteTabla.getNombre());
            if (casillaInforReporte == 1) {
                RequestContext.getCurrentInstance().update("formParametros:infoReporteCodigoD");
                PrimefacesContextUI.ejecutar("PF('infoReporteCodigoD').show()");
            }
            if (casillaInforReporte == 2) {
                RequestContext.getCurrentInstance().update("formParametros:infoReporteNombreD");
                PrimefacesContextUI.ejecutar("PF('infoReporteNombreD').show()");
            }
            casillaInforReporte = -1;
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void generarReporte(int i) {
        if (cambiosReporte == true) {
            if (tipoLista == 0) {
                reporteGenerar = listaIR.get(i).getNombre();
                posicionReporte = i;
            }
            if (tipoLista == 1) {
                if (listaIR.contains(filtrarListInforeportesUsuario.get(i))) {
                    int posicion = listaIR.indexOf(filtrarListInforeportesUsuario.get(i));
                    reporteGenerar = listaIR.get(posicion).getNombre();
                    posicionReporte = posicion;
                }
            }
            mostrarDialogoGenerarReporte();
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            PrimefacesContextUI.ejecutar("PF('confirmarGuardarSinSalida').show()");
        }
    }

    public void mostrarDialogoGenerarReporte() {
        defaultPropiedadesParametrosReporte();
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("formDialogos:reporteAGenerar");
        PrimefacesContextUI.ejecutar("PF('reporteAGenerar').show()");
    }

    public void cancelarGenerarReporte() {
        reporteGenerar = "";
        posicionReporte = -1;
        if (bandera == 1) {
            altoTabla = "185";
            codigoIR = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:reportesBienestar:codigoIR");
            codigoIR.setFilterStyle("display: none; visibility: hidden;");
            reporteIR = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:reportesBienestar:reporteIR");
            reporteIR.setFilterStyle("display: none; visibility: hidden;");
            tipoIR = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:reportesBienestar:tipoIR");
            tipoIR.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:reportesBienestar");
            bandera = 0;
            filtrarListInforeportesUsuario = null;
            tipoLista = 0;
        }
    }

    public void mostrarDialogoBuscarReporte() {
        try {
            if (cambiosReporte == true) {
                listaIR = administrarNReporteBienestar.listInforeportesUsuario();
                if (listaIR != null) {
                    infoRegistroReportes = "Cantidad de registros : " + listaIR.size();
                } else {
                    infoRegistroReportes = "Cantidad de registros : 0";
                }
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:ReportesDialogo");
                PrimefacesContextUI.ejecutar("PF('ReportesDialogo').show()");
            } else {
                RequestContext context = RequestContext.getCurrentInstance();
                PrimefacesContextUI.ejecutar("PF('confirmarGuardarSinSalida').show()");
            }
        } catch (Exception e) {
            System.out.println("Error mostrarDialogoBuscarReporte : " + e.toString());
        }
    }

    public void eventoFiltrar() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
        RequestContext context = RequestContext.getCurrentInstance();
        infoRegistro = "Cantidad de Registros: " + filtrarListInforeportesUsuario.size();
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
    }

    public void actualizarSeleccionInforeporte() {
        if (bandera == 1) {
            altoTabla = "185";
            codigoIR = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:reportesBienestar:codigoIR");
            codigoIR.setFilterStyle("display: none; visibility: hidden;");
            reporteIR = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:reportesBienestar:reporteIR");
            reporteIR.setFilterStyle("display: none; visibility: hidden;");
            tipoIR = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:reportesBienestar:tipoIR");
            tipoIR.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:reportesBienestar");
            bandera = 0;
            filtrarListInforeportesUsuario = null;
            tipoLista = 0;
        }
        RequestContext context = RequestContext.getCurrentInstance();
        listaIR = new ArrayList<Inforeportes>();
        listaIR.add(inforreporteSeleccionado);
        filtrarListInforeportesUsuario = null;
        inforreporteSeleccionado = new Inforeportes();
        lovInforeportes.clear();
        getLovInforeportes();
        filtrarLovInforeportes = null;
        aceptar = true;
        actualInfoReporteTabla = listaIR.get(0);
        activoBuscarReporte = true;
        activoMostrarTodos = false;

        infoRegistro = "Cantidad de registros : " + listaIR.size();

        RequestContext.getCurrentInstance().update("form:informacionRegistro");
        RequestContext.getCurrentInstance().update("form:MOSTRARTODOS");
        RequestContext.getCurrentInstance().update("form:BUSCARREPORTE");/*
        RequestContext.getCurrentInstance().update("form:ReportesDialogo");
        RequestContext.getCurrentInstance().update("form:lovReportesDialogo");
        RequestContext.getCurrentInstance().update("form:aceptarR");*/
        context.reset("form:lovReportesDialogo:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovReportesDialogo').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('ReportesDialogo').hide()");
        RequestContext.getCurrentInstance().update(":form:reportesBienestar");
    }

    public void cancelarSeleccionInforeporte() {
        filtrarListInforeportesUsuario = null;
        inforreporteSeleccionado = new Inforeportes();
        aceptar = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovReportesDialogo:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovReportesDialogo').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('ReportesDialogo').hide()");
    }

    public void salir() {
        if (bandera == 1) {
            altoTabla = "185";
            codigoIR = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:reportesBienestar:codigoIR");
            codigoIR.setFilterStyle("display: none; visibility: hidden;");
            reporteIR = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:reportesBienestar:reporteIR");
            reporteIR.setFilterStyle("display: none; visibility: hidden;");
            tipoIR = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:reportesBienestar:tipoIR");
            tipoIR.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:reportesBienestar");
            bandera = 0;
            filtrarListInforeportesUsuario = null;
            tipoLista = 0;
        }
        listEmpleados = null;
        listaIR = null;
        listActividades = null;
        parametroDeInforme = null;
        parametroModificacion = null;
        listaIRRespaldo = null;
        casilla = -1;
        actividadSeleccionada = null;
        listaInfoReportesModificados.clear();
        cambiosReporte = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void cancelarYSalir() {
        cancelarModificaciones();
        salir();
    }

    public void cancelarModificaciones() {
        if (bandera == 1) {
            defaultPropiedadesParametrosReporte();
            codigoIR = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:reportesBienestar:codigoIR");
            codigoIR.setFilterStyle("display: none; visibility: hidden;");
            reporteIR = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:reportesBienestar:reporteIR");
            reporteIR.setFilterStyle("display: none; visibility: hidden;");
            tipoIR = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:reportesBienestar:tipoIR");
            tipoIR.setFilterStyle("display: none; visibility: hidden;");
            altoTabla = "185";
            RequestContext.getCurrentInstance().update("form:reportesBienestar");
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
        getListaIR();
        if (listaIR != null) {
            infoRegistro = "Cantidad de registros : " + listaIR.size();
        } else {
            infoRegistro = "Cantidad de registros : 0";
        }
        refrescarParametros();
        if (listaIR.size() > 0) {
            actualInfoReporteTabla = listaIR.get(0);
        }
        RequestContext context = RequestContext.getCurrentInstance();
        activoBuscarReporte = false;
        activoMostrarTodos = true;
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
        RequestContext.getCurrentInstance().update("form:MOSTRARTODOS");
        RequestContext.getCurrentInstance().update("form:BUSCARREPORTE");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("form:reportesBienestar");
    }

    public void modificacionTipoReporte(int i) {
        cambiosReporte = false;
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
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void mostrarDialogosListas() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (casilla == 2) {
            RequestContext.getCurrentInstance().update("form:EmpleadoDesdeDialogo");
            PrimefacesContextUI.ejecutar("PF('EmpleadoDesdeDialogo').show()");
        }
        if (casilla == 4) {
            RequestContext.getCurrentInstance().update("form:EmpleadoHastaDialogo");
            PrimefacesContextUI.ejecutar("PF('EmpleadoHastaDialogo').show()");
        }
        if (casilla == 5) {
            RequestContext.getCurrentInstance().update("form:ActividadDialogo");
            PrimefacesContextUI.ejecutar("PF('ActividadDialogo').show()");
        }
    }

    public void actualizarActividad() {
        permitirIndex = true;
        parametroDeInforme.setActividadbienestar(actividadSeleccionada);
        parametroModificacion = parametroDeInforme;
        actividadSeleccionada = null;
        aceptar = true;
        filtrarListActividades = null;
        cambiosReporte = false;
        RequestContext context = RequestContext.getCurrentInstance();
        /*
        RequestContext.getCurrentInstance().update("form:ActividadDialogo");
        RequestContext.getCurrentInstance().update("form:lovActividad");
        RequestContext.getCurrentInstance().update("form:aceptarAct");*/
        context.reset("form:lovActividad:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovActividad').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('ActividadDialogo').hide()");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("formParametros:actividadParametroL");
    }

    public void cancelarCambioActividad() {
        actividadSeleccionada = null;
        aceptar = true;
        filtrarListActividades = null;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovActividad:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovActividad').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('ActividadDialogo').hide()");
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
        RequestContext.getCurrentInstance().update("form:EmpleadoDesdeDialogo");
        RequestContext.getCurrentInstance().update("form:lovEmpleadoDesde");
        RequestContext.getCurrentInstance().update("form:aceptarED");
                */
        context.reset("form:lovEmpleadoDesde:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovEmpleadoDesde').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('EmpleadoDesdeDialogo').hide()");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("formParametros:empleadoDesdeParametroL");
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
        RequestContext.getCurrentInstance().update("form:EmpleadoHastaDialogo");
        RequestContext.getCurrentInstance().update("form:lovEmpleadoHasta");
        RequestContext.getCurrentInstance().update("form:aceptarEH");*/
        context.reset("form:lovEmpleadoHasta:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovEmpleadoHasta').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('EmpleadoHastaDialogo').hide()");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("formParametros:empleadoHastaParametroL");
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
        if (campoConfirmar.equalsIgnoreCase("ACTIVIDAD")) {
            if (!valorConfirmar.isEmpty()) {
                parametroDeInforme.getActividadbienestar().setDescripcion(actividad);
                for (int i = 0; i < listActividades.size(); i++) {
                    if (listActividades.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    parametroDeInforme.setActividadbienestar(listActividades.get(indiceUnicoElemento));
                    parametroModificacion = parametroDeInforme;
                    listActividades.clear();
                    getListActividades();
                    cambiosReporte = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                } else {
                    permitirIndex = false;
                    RequestContext.getCurrentInstance().update("form:ActividadDialogo");
                    PrimefacesContextUI.ejecutar("PF('ActividadDialogo').show()");
                }
            } else {
                listActividades.clear();
                getListActividades();
                parametroDeInforme.setActividadbienestar(new Actividades());
                parametroModificacion = parametroDeInforme;
                cambiosReporte = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
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
                    listEmpleados.clear();
                    getListEmpleados();
                    cambiosReporte = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                } else {
                    permitirIndex = false;
                    RequestContext.getCurrentInstance().update("form:ActividadDialogo");
                    PrimefacesContextUI.ejecutar("PF('ActividadDialogo').show()");
                }
            } else {
                parametroDeInforme.setCodigoempleadodesde(new BigDecimal("0"));
                parametroModificacion = parametroDeInforme;
                listEmpleados.clear();
                getListEmpleados();
                cambiosReporte = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
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
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                } else {
                    permitirIndex = false;
                    RequestContext.getCurrentInstance().update("form:ActividadDialogo");
                    PrimefacesContextUI.ejecutar("PF('ActividadDialogo').show()");
                }
            } else {
                parametroDeInforme.setCodigoempleadohasta(new BigDecimal("9999999999999999999999"));
                parametroModificacion = parametroDeInforme;
                listEmpleados.clear();
                getListEmpleados();
                cambiosReporte = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        }
    }

    public void refrescarParametros() {
        defaultPropiedadesParametrosReporte();
        parametroDeInforme = null;
        parametroModificacion = null;
        listEmpleados = null;
        listActividades = null;
        listEmpleados = administrarNReporteBienestar.listEmpleados();
        listActividades = administrarNReporteBienestar.listActividades();
        parametroDeInforme = administrarNReporteBienestar.parametrosDeReporte();
        if (parametroDeInforme.getActividadbienestar() == null) {
            parametroDeInforme.setActividadbienestar(new Actividades());
        }
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("formParametros:fechaDesdeParametroL");
        RequestContext.getCurrentInstance().update("formParametros:empleadoDesdeParametroL");
        RequestContext.getCurrentInstance().update("formParametros:fechaHastaParametroL");
        RequestContext.getCurrentInstance().update("formParametros:empleadoHastaParametroL");
        RequestContext.getCurrentInstance().update("formParametros:estadoParametroL");
        RequestContext.getCurrentInstance().update("formParametros:actividadParametroL");
    }

    public void activarCtrlF11() {
        if (bandera == 0) {
            altoTabla = "165";
            codigoIR = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:reportesBienestar:codigoIR");
            codigoIR.setFilterStyle("width: 85%;");
            reporteIR = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:reportesBienestar:reporteIR");
            reporteIR.setFilterStyle("width: 85%;");
            tipoIR = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:reportesBienestar:tipoIR");
            tipoIR.setFilterStyle("width: 85%;");
            RequestContext.getCurrentInstance().update("form:reportesBienestar");
            tipoLista = 1;
            bandera = 1;
        } else if (bandera == 1) {
            altoTabla = "185";
            defaultPropiedadesParametrosReporte();
            codigoIR = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:reportesBienestar:codigoIR");
            codigoIR.setFilterStyle("display: none; visibility: hidden;");
            reporteIR = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:reportesBienestar:reporteIR");
            reporteIR.setFilterStyle("display: none; visibility: hidden;");
            tipoIR = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:reportesBienestar:tipoIR");
            tipoIR.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:reportesBienestar");
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
        RequestContext.getCurrentInstance().update("formParametros");

        empleadoDesdeParametroL = (InputText) FacesContext.getCurrentInstance().getViewRoot().findComponent("formParametros:empleadoDesdeParametroL");
        empleadoDesdeParametroL.setStyle("position: absolute; top: 40px; left: 260px;height: 15px;width: 90px;");
        RequestContext.getCurrentInstance().update("formParametros:empleadoDesdeParametroL");

        empleadoHastaParametroL = (InputText) FacesContext.getCurrentInstance().getViewRoot().findComponent("formParametros:empleadoHastaParametroL");
        empleadoHastaParametroL.setStyle("position: absolute; top: 40px; left: 400px;height: 15px;width: 90px;");
        RequestContext.getCurrentInstance().update("formParametros:empleadoHastaParametroL");

    }

    public void modificarParametroInforme() {
        if (parametroDeInforme.getCodigoempleadodesde() != null && parametroDeInforme.getCodigoempleadohasta() != null
                && parametroDeInforme.getFechadesde() != null && parametroDeInforme.getFechahasta() != null) {
            if (parametroDeInforme.getFechadesde().before(parametroDeInforme.getFechahasta())) {
                parametroModificacion = parametroDeInforme;
                cambiosReporte = false;
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            } else {
                parametroDeInforme.setFechadesde(fechaDesde);
                parametroDeInforme.setFechahasta(fechaHasta);
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("formParametros");
                PrimefacesContextUI.ejecutar("PF('errorFechas').show()");
            }
        } else {
            parametroDeInforme.setCodigoempleadodesde(emplDesde);
            parametroDeInforme.setCodigoempleadohasta(emplHasta);
            parametroDeInforme.setFechadesde(fechaDesde);
            parametroDeInforme.setFechahasta(fechaHasta);
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formParametros");
            PrimefacesContextUI.ejecutar("PF('errorRegNew').show()");
        }
    }

    public void resaltoParametrosParaReporte(int i) {
        Inforeportes reporteS = new Inforeportes();
        if (tipoLista == 0) {
            reporteS = listaIR.get(i);
        }
        if (tipoLista == 1) {
            reporteS = filtrarListInforeportesUsuario.get(i);
        }
        defaultPropiedadesParametrosReporte();
        if (reporteS.getFecdesde().equals("SI")) {
            color = "red";
            decoracion = "underline";
            RequestContext.getCurrentInstance().update("formParametros");
        }
        if (reporteS.getFechasta().equals("SI")) {
            color2 = "red";
            decoracion2 = "underline";
            RequestContext.getCurrentInstance().update("formParametros");
        }
        if (reporteS.getEmdesde().equals("SI")) {
            empleadoDesdeParametroL = (InputText) FacesContext.getCurrentInstance().getViewRoot().findComponent("formParametros:empleadoDesdeParametroL");
            empleadoDesdeParametroL.setStyle("position: absolute; top: 40px; left: 260px;height: 10px;width: 100px;text-decoration: underline; color: red;");
            RequestContext.getCurrentInstance().update("formParametros:empleadoDesdeParametroL");
        }
        if (reporteS.getEmhasta().equals("SI")) {
            empleadoHastaParametroL = (InputText) FacesContext.getCurrentInstance().getViewRoot().findComponent("formParametros:empleadoHastaParametroL");
            empleadoHastaParametroL.setStyle("position: absolute; top: 40px; left: 400px;height: 10px;width: 100px; text-decoration: underline; color: red;");
            RequestContext.getCurrentInstance().update("formParametros:empleadoHastaParametroL");
        }
    }

    public void mostrarTodos() {
        if (cambiosReporte == true) {
            defaultPropiedadesParametrosReporte();
            listaIR = null;
            getListaIR();
            if (listaIR.size() > 0) {
                actualInfoReporteTabla = listaIR.get(0);
            }
            RequestContext context = RequestContext.getCurrentInstance();
            activoBuscarReporte = false;
            activoMostrarTodos = true;
            RequestContext.getCurrentInstance().update("form:MOSTRARTODOS");
            RequestContext.getCurrentInstance().update("form:BUSCARREPORTE");
            RequestContext.getCurrentInstance().update("form:reportesBienestar");
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            PrimefacesContextUI.ejecutar("PF('confirmarGuardarSinSalida').show()");
        }
    }

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
            RequestContext.getCurrentInstance().update("formDialogos:requisitosReporte");
            PrimefacesContextUI.ejecutar("PF('requisitosReporte').show()");
        }
    }

    public void generarReporte() throws IOException {
        String nombreReporte, tipoReporte;
        String pathReporteGenerado = null;
        if (tipoLista == 0) {
            nombreReporte = listaIR.get(indice).getNombrereporte();
            tipoReporte = listaIR.get(indice).getTipo();
        } else {
            nombreReporte = filtrarListInforeportesUsuario.get(indice).getNombrereporte();
            tipoReporte = filtrarListInforeportesUsuario.get(indice).getTipo();
        }
        if (nombreReporte != null && tipoReporte != null) {
            pathReporteGenerado = administarReportes.generarReporte(nombreReporte, tipoReporte);
        }
        if (pathReporteGenerado != null) {
            exportarReporte(pathReporteGenerado);
        }
    }

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

    public void cancelarRequisitosReporte() {
        requisitosReporte = "";
    }

    public ParametrosInformes getParametroDeInforme() {
        try {
            if (parametroDeInforme == null) {
                parametroDeInforme = new ParametrosInformes();
                parametroDeInforme = administrarNReporteBienestar.parametrosDeReporte();
            }

            if (parametroDeInforme.getActividadbienestar() == null) {
                parametroDeInforme.setActividadbienestar(new Actividades());
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
                listaIR = new ArrayList<Inforeportes>();
                listaIR = administrarNReporteBienestar.listInforeportesUsuario();
            }
            listaIRRespaldo = listaIR;
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

            listEmpleados = administrarNReporteBienestar.listEmpleados();

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

    public List<Actividades> getListActividades() {
        try {
            listActividades = administrarNReporteBienestar.listActividades();
            return listActividades;
        } catch (Exception e) {
            System.out.println("Error getListActividades : " + e.toString());
            return null;
        }
    }

    public void setListActividades(List<Actividades> listActividades) {
        this.listActividades = listActividades;
    }

    public Actividades getActividadSeleccionada() {
        return actividadSeleccionada;
    }

    public void setActividadSeleccionada(Actividades actividadSeleccionada) {
        this.actividadSeleccionada = actividadSeleccionada;
    }

    public List<Actividades> getFiltrarListActividades() {
        return filtrarListActividades;
    }

    public void setFiltrarListActividades(List<Actividades> filtrarListActividades) {
        this.filtrarListActividades = filtrarListActividades;
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

    public String getInfoRegistroActividad() {
        getListActividades();
        if (listActividades != null) {
            infoRegistroActividad = "Cantidad de registros : " + listActividades.size();
        } else {
            infoRegistroActividad = "Cantidad de registros : 0";
        }
        return infoRegistroActividad;
    }

    public void setInfoRegistroActividad(String infoRegistroActividad) {
        this.infoRegistroActividad = infoRegistroActividad;
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

    public String getInfoRegistroReportes() {
        return infoRegistroReportes;
    }

    public void setInfoRegistroReportes(String infoRegistroReportes) {
        this.infoRegistroReportes = infoRegistroReportes;
    }

    public List<Inforeportes> getLovInforeportes() {
        lovInforeportes = administrarNReporteBienestar.listInforeportesUsuario();
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

    public String getInfoRegistro() {
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

}
