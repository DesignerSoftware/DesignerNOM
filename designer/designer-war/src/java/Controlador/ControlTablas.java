/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import ControlNavegacion.ControlListaNavegacion;
import Entidades.Modulos;
import Entidades.Tablas;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarRastrosInterface;
import InterfaceAdministrar.AdministrarTablasInterface;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.primefaces.component.column.Column;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.export.Exporter;
import org.primefaces.context.RequestContext;

/**
 *
 * @author user
 */
@Named(value = "controlTablas")
@SessionScoped
public class ControlTablas implements Serializable {

    private static Logger log = Logger.getLogger(ControlTablas.class);

    @EJB
    AdministrarTablasInterface administrarTablas;
    @EJB
    AdministrarRastrosInterface administrarRastros;

    private List<Tablas> listaTablas;
    private List<Tablas> listaTablasFiltrar;
    private List<Tablas> listaTablasModificar;
    private Tablas editarTabla;
    private Tablas tablaSeleccionado;
    //
    private List<Modulos> lovModulos;
    private List<Modulos> lovModulosFiltrar;
    private Modulos moduloSeleccionado;
    private int cualCelda, tipoLista, tipoActualizacion, k, bandera;
    private BigInteger l;
    private boolean aceptar, guardado;
    private Column tipo, nombre, modulo, descripcion;
    private String altoTabla;
    //
    private String infoRegistro, infoRegistroModulos;
    private String mensajeValidacion;
    private String paginaAnterior = "nominaf";
    private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();
    private String msgError;
    private boolean activarLov;

    public ControlTablas() {
        listaTablas = null;
        listaTablasModificar = new ArrayList<Tablas>();
        editarTabla = new Tablas();
        guardado = true;
        mapParametros.put("paginaAnterior", paginaAnterior);
        altoTabla = "315";
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

    public void navegar(String pag) {
        FacesContext fc = FacesContext.getCurrentInstance();
        ControlListaNavegacion controlListaNavegacion = (ControlListaNavegacion) fc.getApplication().evaluateExpressionGet(fc, "#{controlListaNavegacion}", ControlListaNavegacion.class);
        String pagActual = "tabla";
        if (pag.equals("atras")) {
            pag = paginaAnterior;
            paginaAnterior = "nominaf";
            controlListaNavegacion.quitarPagina(pagActual, this.getClass().getSimpleName());
        } else {
            controlListaNavegacion.guardarNavegacion(pagActual, pag);
            fc.getApplication().getNavigationHandler().handleNavigation(fc, null, pag);
        }
        limpiarListasValor();
    }

    public void limpiarListasValor() {
        lovModulos = null;
    }

    @PreDestroy
    public void destruyendose() {
        log.info(this.getClass().getName() + ".destruyendose() @Destroy");
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarTablas.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
            listaTablas = null;
            getListaTablas();
            if (listaTablas != null) {
                tablaSeleccionado = listaTablas.get(0);
            }

        } catch (Exception e) {
            log.error("Error postconstruct " + this.getClass().getName() + ":  ", e);
            log.error("Causa: " + e.getCause());
        }
    }

    public void cambiarIndice(Tablas tabla, int celda) {
        tablaSeleccionado = tabla;
        cualCelda = celda;
        tablaSeleccionado.getSecuencia();
        if (cualCelda == 0) {
            tablaSeleccionado.getNombre();
            deshabilitarBotonLov();
        } else if (cualCelda == 1) {
            tablaSeleccionado.getModulo().getNombre();
            habilitarBotonLov();
        } else if (cualCelda == 2) {
            tablaSeleccionado.getDescripcion();
            deshabilitarBotonLov();
        } else if (cualCelda == 3) {
            tablaSeleccionado.getTipo();
            deshabilitarBotonLov();
        }
    }

    public void asignarIndex(Tablas tabla, int dlg, int lnd) {
        tablaSeleccionado = tabla;
        tipoActualizacion = lnd;
        if (dlg == 0) {
            lovModulos = null;
            getLovModulos();
            contarRegistrosModulos();
            RequestContext.getCurrentInstance().update("formularioDialogos:modulosDialogo");
            RequestContext.getCurrentInstance().execute("PF('modulosDialogo').show()");
        }

    }

    public void listaValoresBoton() {
        if (cualCelda == 1) {
            lovModulos = null;
            getLovModulos();
            RequestContext.getCurrentInstance().update("formularioDialogos:modulosDialogo");
            RequestContext.getCurrentInstance().execute("PF('modulosDialogo').show()");
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void cancelarModificacion() {
        if (bandera == 1) {
            //CERRAR FILTRADO
            tipo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTablas:tipo");
            tipo.setFilterStyle("display: none; visibility: hidden;");
            nombre = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTablas:nombre");
            nombre.setFilterStyle("display: none; visibility: hidden;");
            modulo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTablas:modulo");
            modulo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTablas:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTablas");
            bandera = 0;
            listaTablasFiltrar = null;
            tipoLista = 0;
            altoTabla = "315";
        }

        listaTablasModificar.clear();
        tablaSeleccionado = null;
        k = 0;
        listaTablas = null;
        guardado = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosTablas");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void eventoFiltrar() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
        contarRegistros();
    }

    public void activarCtrlF11() {
        if (bandera == 0) {
            altoTabla = "295";
            tipo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTablas:tipo");
            tipo.setFilterStyle("width: 85% !important;");
            nombre = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTablas:nombre");
            nombre.setFilterStyle("width: 85% !important;");
            modulo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTablas:modulo");
            modulo.setFilterStyle("width: 85% !important;");
            descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTablas:descripcion");
            descripcion.setFilterStyle("width: 85% !important;");
            RequestContext.getCurrentInstance().update("form:datosTablas");
            bandera = 1;
        } else if (bandera == 1) {
            altoTabla = "315";
            tipo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTablas:tipo");
            tipo.setFilterStyle("display: none; visibility: hidden;");
            nombre = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTablas:nombre");
            nombre.setFilterStyle("display: none; visibility: hidden;");
            modulo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTablas:modulo");
            modulo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTablas:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTablas");
            bandera = 0;
            listaTablasFiltrar = null;
            tipoLista = 0;
        }
    }

    public void seleccionarComboBox(Tablas tabla, String estado) {
        tablaSeleccionado = tabla;
        if (estado.equals("CONFIGURACION")) {
            tablaSeleccionado.setTipo("CONFIGURACION");
        } else if (estado.equals("CONSTRUCCION")) {
            tablaSeleccionado.setTipo("CONSTRUCCION");
        } else if (estado.equals("DATOS")) {
            tablaSeleccionado.setTipo("DATOS");
        } else if (estado.equals("SISTEMA")) {
            tablaSeleccionado.setTipo("SISTEMA");
        } else if (estado.equals("POR ASIGNAR")) {
            tablaSeleccionado.setTipo("POR ASIGNAR");
        } else if (estado.equals("NULL")) {
            tablaSeleccionado.setTipo(null);
        }
        modificarTablas(tablaSeleccionado);
    }

    public void modificarTablas(Tablas tabla) {
        tablaSeleccionado = tabla;
        if (listaTablasModificar.isEmpty()) {
            listaTablasModificar.add(tablaSeleccionado);
        } else if (!listaTablasModificar.contains(tablaSeleccionado)) {
            listaTablasModificar.add(tablaSeleccionado);
        }
        guardado = false;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("form:datosTablas");
    }

    public void revisarDialogoGuardar() {
        if (!listaTablasModificar.isEmpty()) {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:confirmarGuardar");
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
        }
    }

    public void guardarTablas() {
        RequestContext context = RequestContext.getCurrentInstance();
        try {
            if (guardado == false) {
                msgError = "";
                if (!listaTablasModificar.isEmpty()) {
                    for (int i = 0; i < listaTablasModificar.size(); i++) {
                        msgError = administrarTablas.modificarTablas(listaTablasModificar.get(i));
                    }
                }
                listaTablasModificar.clear();

                if (msgError.equals("EXITO")) {
                    listaTablas = null;
                    getListaTablas();
                    k = 0;
                    FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                    RequestContext.getCurrentInstance().update("form:growl");
                    contarRegistros();
                    guardado = true;
                    tablaSeleccionado = null;
                    RequestContext.getCurrentInstance().update("form:datosTablas");
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                } else {
                    RequestContext.getCurrentInstance().update("formularioDialogos:errorGuardadoBD");
                    RequestContext.getCurrentInstance().execute("PF('errorGuardadoBD').show()");
                }

            }
        } catch (Exception e) {
            log.warn("Error guardarCambios : " + e.toString());
            FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado, intente nuevamente.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }

    }

    public void editarCelda() {
        if (tablaSeleccionado != null) {
            editarTabla = tablaSeleccionado;
            RequestContext context = RequestContext.getCurrentInstance();
            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editNombre");
                RequestContext.getCurrentInstance().execute("PF('editNombre').show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editModulo");
                RequestContext.getCurrentInstance().execute("PF('editModulo').show()");
                cualCelda = -1;
            } else if (cualCelda == 2) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editDescripcion");
                RequestContext.getCurrentInstance().execute("PF('editDescripcion').show()");
                cualCelda = -1;
            }

        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void limpiarExportar() {
        editarTabla = new Tablas();
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTablasExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "MotivosDefinitivas", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTablasExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "MotivosDefinitivas", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void verificarRastro() {
        if (tablaSeleccionado != null) {
            int resultado = administrarRastros.obtenerTabla(tablaSeleccionado.getSecuencia(), "OBJETOSDB"); //En ENCARGATURAS lo cambia por el nombre de su tabla
            if (resultado == 1) {
                RequestContext.getCurrentInstance().execute("PF('errorTablas').show()");
            } else if (resultado == 2) {
                RequestContext.getCurrentInstance().execute("PF('confirmarRastro').show()");
            } else if (resultado == 3) {
                RequestContext.getCurrentInstance().execute("PF('errorRegistroRastro').show()");
            } else if (resultado == 4) {
                RequestContext.getCurrentInstance().execute("PF('errorTablaConRastro').show()");
            } else if (resultado == 5) {
                RequestContext.getCurrentInstance().execute("PF('errorTablaSinRastro').show()");
            }
        } else if (administrarRastros.verificarHistoricosTabla("OBJETOSDB")) { // igual acá
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
    }

    public void salir() {
        limpiarListasValor();
        if (bandera == 1) {
            tipo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTablas:tipo");
            tipo.setFilterStyle("display: none; visibility: hidden;");
            nombre = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTablas:nombre");
            nombre.setFilterStyle("display: none; visibility: hidden;");
            modulo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTablas:modulo");
            modulo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTablas:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTablas");
            bandera = 0;
            listaTablasFiltrar = null;
            tipoLista = 0;
            altoTabla = "315";
        }
        listaTablasModificar.clear();
        tablaSeleccionado = null;
        k = 0;
        listaTablas = null;
        guardado = true;
        RequestContext.getCurrentInstance().update("form:datosTablas");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        navegar("atras");
    }

    public void contarRegistros() {
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
    }

    public void contarRegistrosModulos() {
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroModulos");
    }

    public void habilitarBotonLov() {
        activarLov = false;
        RequestContext.getCurrentInstance().update("form:listaValores");
    }

    public void deshabilitarBotonLov() {
        activarLov = true;
        RequestContext.getCurrentInstance().update("form:listaValores");

    }

    public void actualizarModulo() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            tablaSeleccionado.setModulo(moduloSeleccionado);
            if (listaTablasModificar.isEmpty()) {
                listaTablasModificar.add(tablaSeleccionado);
            } else if (!listaTablasModificar.contains(tablaSeleccionado)) {
                listaTablasModificar.add(tablaSeleccionado);
            }
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            deshabilitarBotonLov();
            RequestContext.getCurrentInstance().update("form:datosTablas");
        }
        lovModulosFiltrar = null;
        moduloSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        RequestContext.getCurrentInstance().update("formularioDialogos:modulosDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovModulos");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarM");
        context.reset("formularioDialogos:lovModulos:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovModulos').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('modulosDialogo').hide()");
    }

    public void cancelarCambioModulo() {
        lovModulosFiltrar = null;
        moduloSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        RequestContext.getCurrentInstance().update("formularioDialogos:modulosDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovModulos");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarM");
        RequestContext.getCurrentInstance().reset("formularioDialogos:lovModulos:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovModulos').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('modulosDialogo').hide()");
    }

    ////SET Y GET////
    public List<Tablas> getListaTablas() {
        if (listaTablas == null) {
            listaTablas = administrarTablas.consultarTablas();
        }
        return listaTablas;
    }

    public void setListaTablas(List<Tablas> listaTablas) {
        this.listaTablas = listaTablas;
    }

    public List<Tablas> getListaTablasFiltrar() {
        return listaTablasFiltrar;
    }

    public void setListaTablasFiltrar(List<Tablas> listaTablasFiltrar) {
        this.listaTablasFiltrar = listaTablasFiltrar;
    }

    public Tablas getEditarTabla() {
        return editarTabla;
    }

    public void setEditarTabla(Tablas editarTabla) {
        this.editarTabla = editarTabla;
    }

    public Tablas getTablaSeleccionado() {
        return tablaSeleccionado;
    }

    public void setTablaSeleccionado(Tablas tablaSeleccionado) {
        this.tablaSeleccionado = tablaSeleccionado;
    }

    public List<Modulos> getLovModulos() {
        if (lovModulos == null) {
            lovModulos = administrarTablas.consultarModulos();
        }
        return lovModulos;
    }

    public void setLovModulos(List<Modulos> lovModulos) {
        this.lovModulos = lovModulos;
    }

    public List<Modulos> getLovModulosFiltrar() {
        return lovModulosFiltrar;
    }

    public void setLovModulosFiltrar(List<Modulos> lovModulosFiltrar) {
        this.lovModulosFiltrar = lovModulosFiltrar;
    }

    public Modulos getModuloSeleccionado() {
        return moduloSeleccionado;
    }

    public void setModuloSeleccionado(Modulos moduloSeleccionado) {
        this.moduloSeleccionado = moduloSeleccionado;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public void setAceptar(boolean aceptar) {
        this.aceptar = aceptar;
    }

    public boolean isGuardado() {
        return guardado;
    }

    public void setGuardado(boolean guardado) {
        this.guardado = guardado;
    }

    public String getAltoTabla() {
        return altoTabla;
    }

    public void setAltoTabla(String altoTabla) {
        this.altoTabla = altoTabla;
    }

    public String getInfoRegistro() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosTablas");
        infoRegistro = String.valueOf(tabla.getRowCount());
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

    public String getMensajeValidacion() {
        return mensajeValidacion;
    }

    public void setMensajeValidacion(String mensajeValidacion) {
        this.mensajeValidacion = mensajeValidacion;
    }

    public String getMsgError() {
        return msgError;
    }

    public void setMsgError(String msgError) {
        this.msgError = msgError;
    }

    public boolean isActivarLov() {
        return activarLov;
    }

    public void setActivarLov(boolean activarLov) {
        this.activarLov = activarLov;
    }

    public String getInfoRegistroModulos() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovModulos");
        infoRegistroModulos = String.valueOf(tabla.getRowCount());
        return infoRegistroModulos;
    }

    public void setInfoRegistroModulos(String infoRegistroModulos) {
        this.infoRegistroModulos = infoRegistroModulos;
    }

}
