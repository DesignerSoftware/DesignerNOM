/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import ControlNavegacion.ControlListaNavegacion;
import Entidades.Empresas;
import Entidades.Pantallas;
import Entidades.Tablas;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarPantallasInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
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
@Named(value = "controlPantallas")
@SessionScoped
public class ControlPantallas implements Serializable {

    private static Logger log = Logger.getLogger(ControlPantallas.class);

    @EJB
    AdministrarPantallasInterface administrarPantallas;
    @EJB
    AdministrarRastrosInterface administrarRastros;

    private List<Pantallas> listaPantallas;
    private List<Pantallas> listaPantallasFiltrar;
    private List<Pantallas> listaPantallasCrear;
    private List<Pantallas> listaPantallasModificar;
    private List<Pantallas> listaPantallasBorrar;
    private Pantallas nuevaPantalla;
    private Pantallas duplicarPantalla;
    private Pantallas editarPantalla;
    private Pantallas pantallaSeleccionada;
    //lov Empresas
    private List<Empresas> lovEmpresas;
    private List<Empresas> lovEmpresasFiltrar;
    private Empresas empresaSeleccionada;
    //lov Tablas
    private List<Tablas> lovTablas;
    private List<Tablas> lovTablasFiltrar;
    private Tablas tablaSeleccionada;
    private int cualCelda, tipoLista, tipoActualizacion, k, bandera;
    private BigInteger l;
    private boolean aceptar, guardado;
    private Column codigo, nombre, tabla, empresa;
    private String altoTabla;
    //
    private String infoRegistro, infoRegistroEmpresas, infoRegistroTablas;
    private String mensajeValidacion;
    private String paginaAnterior = "nominaf";
    private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();
    private String msgError;
    private boolean activarLov;

    public ControlPantallas() {

        listaPantallas = null;
        listaPantallasCrear = new ArrayList<Pantallas>();
        listaPantallasModificar = new ArrayList<Pantallas>();
        listaPantallasBorrar = new ArrayList<Pantallas>();
        editarPantalla = new Pantallas();
        nuevaPantalla = new Pantallas();
        nuevaPantalla.setEmpresa(new Empresas());
        nuevaPantalla.setTabla(new Tablas());
        duplicarPantalla = new Pantallas();
        duplicarPantalla.setEmpresa(new Empresas());
        duplicarPantalla.setTabla(new Tablas());
        guardado = true;
        mapParametros.put("paginaAnterior", paginaAnterior);
        altoTabla = "335";
        activarLov = true;
        msgError = "";
        lovEmpresas = null;
        lovTablas = null;
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
        String pagActual = "pantalla";
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
        lovEmpresas = null;
        lovTablas = null;
    }

    @PreDestroy
    public void destruyendose() {
        log.info(this.getClass().getName() + ".destruyendoce() @Destroy");
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarPantallas.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
            listaPantallas = null;
            getListaPantallas();
            if (listaPantallas != null) {
                pantallaSeleccionada = listaPantallas.get(0);
            }

        } catch (Exception e) {
            log.error("Error postconstruct " + this.getClass().getName() + ":  ", e);
            log.error("Causa: " + e.getCause());
        }
    }

    public void cambiarIndice(Pantallas pantalla, int celda) {
        pantallaSeleccionada = pantalla;
        pantallaSeleccionada.getSecuencia();
        cualCelda = celda;

        if (cualCelda == 0) {
            pantallaSeleccionada.getCodigo();
            deshabilitarBotonLov();
        } else if (cualCelda == 1) {
            pantallaSeleccionada.getNombre();
            deshabilitarBotonLov();
        } else if (cualCelda == 2) {
            pantallaSeleccionada.getTabla().getDescripcion();
            habilitarBotonLov();
        } else if (cualCelda == 3) {
            pantallaSeleccionada.getEmpresa().getNombre();
            habilitarBotonLov();
        }
    }

    public void asignarIndex(Pantallas pantalla, int dlg, int lnd) {
        pantallaSeleccionada = pantalla;
        tipoActualizacion = lnd;
        if (dlg == 0) {
            lovTablas = null;
            getLovTablas();
            contarRegistrosTablas();
            RequestContext.getCurrentInstance().update("formularioDialogos:tablasDialogo");
            RequestContext.getCurrentInstance().execute("PF('tablasDialogo').show()");
        } else if (dlg == 1) {
            lovEmpresas = null;
            getLovEmpresas();
            contarRegistrosEmpresas();
            RequestContext.getCurrentInstance().update("formularioDialogos:empresasDialogo");
            RequestContext.getCurrentInstance().execute("PF('empresasDialogo').show()");
        }
    }

    public void listaValoresBoton() {
        if (cualCelda == 2) {
            lovTablas = null;
            getLovTablas();
            contarRegistrosTablas();
            RequestContext.getCurrentInstance().update("formularioDialogos:tablasDialogo");
            RequestContext.getCurrentInstance().execute("PF('tablasDialogo').show()");
        } else if (cualCelda == 3) {
            lovEmpresas = null;
            getLovEmpresas();
            contarRegistrosEmpresas();
            RequestContext.getCurrentInstance().update("formularioDialogos:empresasDialogo");
            RequestContext.getCurrentInstance().execute("PF('empresasDialogo').show()");
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void cancelarModificacion() {
        if (bandera == 1) {
            //CERRAR FILTRADO
            codigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosPantallas:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            nombre = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosPantallas:nombre");
            nombre.setFilterStyle("display: none; visibility: hidden;");
            tabla = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosPantallas:tabla");
            tabla.setFilterStyle("display: none; visibility: hidden;");
            empresa = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosPantallas:empresa");
            empresa.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosPantallas");
            bandera = 0;
            listaPantallasFiltrar = null;
            tipoLista = 0;
            altoTabla = "335";
        }

        listaPantallasBorrar.clear();
        listaPantallasCrear.clear();
        listaPantallasModificar.clear();
        pantallaSeleccionada = null;
        k = 0;
        listaPantallas = null;
        guardado = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosPantallas");
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
            altoTabla = "315";
            codigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosPantallas:codigo");
            codigo.setFilterStyle("width: 85% !important;");
            nombre = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosPantallas:nombre");
            nombre.setFilterStyle("width: 85% !important;");
            tabla = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosPantallas:tabla");
            tabla.setFilterStyle("width: 85% !important;");
            empresa = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosPantallas:empresa");
            empresa.setFilterStyle("width: 85% !important;");
            RequestContext.getCurrentInstance().update("form:datosPantallas");
            bandera = 1;
        } else if (bandera == 1) {
            codigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosPantallas:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            nombre = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosPantallas:nombre");
            nombre.setFilterStyle("display: none; visibility: hidden;");
            tabla = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosPantallas:tabla");
            tabla.setFilterStyle("display: none; visibility: hidden;");
            empresa = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosPantallas:empresa");
            empresa.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosPantallas");
            bandera = 0;
            listaPantallasFiltrar = null;
            tipoLista = 0;
            altoTabla = "335";
        }
    }

    public void modificarPantallas(Pantallas pantalla) {
        pantallaSeleccionada = pantalla;
        if (!listaPantallasCrear.contains(pantallaSeleccionada)) {
            if (listaPantallasModificar.isEmpty()) {
                listaPantallasModificar.add(pantallaSeleccionada);
            } else if (!listaPantallasModificar.contains(pantallaSeleccionada)) {
                listaPantallasModificar.add(pantallaSeleccionada);
            }
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
        RequestContext.getCurrentInstance().update("form:datosPantallas");
    }

    public void borrarPantallas() {
        if (pantallaSeleccionada != null) {
            if (!listaPantallasModificar.isEmpty() && listaPantallasModificar.contains(pantallaSeleccionada)) {
                int modIndex = listaPantallasModificar.indexOf(pantallaSeleccionada);
                listaPantallasModificar.remove(modIndex);
                listaPantallasBorrar.add(pantallaSeleccionada);
            } else if (!listaPantallasCrear.isEmpty() && listaPantallasCrear.contains(pantallaSeleccionada)) {
                int crearIndex = listaPantallasCrear.indexOf(pantallaSeleccionada);
                listaPantallasCrear.remove(crearIndex);
            } else {
                listaPantallasBorrar.add(pantallaSeleccionada);
            }
            listaPantallas.remove(pantallaSeleccionada);
            if (tipoLista == 1) {
                listaPantallasFiltrar.remove(pantallaSeleccionada);
            }
            pantallaSeleccionada = null;
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosPantallas");
        }

    }

    public void revisarDialogoGuardar() {
        if (!listaPantallasBorrar.isEmpty() || !listaPantallasCrear.isEmpty() || !listaPantallasModificar.isEmpty()) {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:confirmarGuardar");
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
        }
    }

    public void guardarPantallas() {
        RequestContext context = RequestContext.getCurrentInstance();
        try {
            if (guardado == false) {
                msgError = "";
                if (!listaPantallasBorrar.isEmpty()) {
                    for (int i = 0; i < listaPantallasBorrar.size(); i++) {
                        msgError = administrarPantallas.borrarPantallas(listaPantallasBorrar.get(i));
                    }
                    listaPantallasBorrar.clear();
                }
                if (!listaPantallasCrear.isEmpty()) {
                    for (int i = 0; i < listaPantallasCrear.size(); i++) {
                        msgError = administrarPantallas.crearPantallas(listaPantallasCrear.get(i));
                    }
                    listaPantallasCrear.clear();
                }
                if (!listaPantallasModificar.isEmpty()) {
                    for (int i = 0; i < listaPantallasModificar.size(); i++) {
                        msgError = administrarPantallas.modificarPantallas(listaPantallasModificar.get(i));
                    }
                }
                listaPantallasModificar.clear();

                if (msgError.equals("EXITO")) {
                    listaPantallas = null;
                    getListaPantallas();
                    k = 0;
                    FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                    RequestContext.getCurrentInstance().update("form:growl");
                    contarRegistros();
                    guardado = true;
                    pantallaSeleccionada = null;
                    RequestContext.getCurrentInstance().update("form:datosPantallas");
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
        if (pantallaSeleccionada != null) {
            editarPantalla = pantallaSeleccionada;
            RequestContext context = RequestContext.getCurrentInstance();
            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editCodigo");
                RequestContext.getCurrentInstance().execute("PF('editCodigo').show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editNombre");
                RequestContext.getCurrentInstance().execute("PF('editNombre').show()");
                cualCelda = -1;
            } else if (cualCelda == 2) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editTabla");
                RequestContext.getCurrentInstance().execute("PF('editTabla').show()");
                cualCelda = -1;
            } else if (cualCelda == 3) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editEmpresa");
                RequestContext.getCurrentInstance().execute("PF('editEmpresa').show()");
                cualCelda = -1;
            }
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void agregarNuevaPantalla() {
        int contador = 0;
        int duplicados = 0;
        Integer a = 0;
        a = null;
        mensajeValidacion = " ";
        if (nuevaPantalla.getCodigo() == a) {
            mensajeValidacion = " Los campos marcados con asterisco son obligatorios ";
        } else {
            contador++;
        }

        if (nuevaPantalla.getNombre().equals(null) || nuevaPantalla.getNombre().isEmpty()) {
            mensajeValidacion = " Los campos marcados con asterisco son obligatorios ";
        } else {
            contador++;
        }

        if (nuevaPantalla.getTabla().equals(null)) {
            mensajeValidacion = " Los campos marcados con asterisco son obligatorios ";
        } else {
            contador++;
        }

        if (nuevaPantalla.getEmpresa().equals(null)) {
            mensajeValidacion = " Los campos marcados con asterisco son obligatorios ";
        } else {
            contador++;
        }

        for (int i = 0; i < listaPantallas.size(); i++) {
            if (nuevaPantalla.getEmpresa().equals(listaPantallas.get(i).getEmpresa())) {
                duplicados++;
            }
        }

        if (duplicados > 0) {
            mensajeValidacion = "Existe un registro asociado a la empresa ingresada";
        }

        if (contador == 4) {
            if (bandera == 1) {
                codigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosPantallas:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                nombre = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosPantallas:nombre");
                nombre.setFilterStyle("display: none; visibility: hidden;");
                tabla = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosPantallas:tabla");
                tabla.setFilterStyle("display: none; visibility: hidden;");
                empresa = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosPantallas:empresa");
                empresa.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosPantallas");
                bandera = 0;
                listaPantallasFiltrar = null;
                tipoLista = 0;
                altoTabla = "335";
            }
            k++;
            l = BigInteger.valueOf(k);
            nuevaPantalla.setSecuencia(l);
            listaPantallasCrear.add(nuevaPantalla);
            listaPantallas.add(0, nuevaPantalla);
            pantallaSeleccionada = nuevaPantalla;
            RequestContext.getCurrentInstance().update("form:datosPantallas");
            contarRegistros();
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().execute("PF('nuevoRegistroPantallas').hide()");
            nuevaPantalla = new Pantallas();
        } else {
            RequestContext.getCurrentInstance().update("form:validacionNuevaCentroCosto");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevaCentroCosto').show()");
            contador = 0;
        }
    }

    public void limpiarNuevaPantalla() {
        nuevaPantalla = new Pantallas();
    }

    public void duplicandoPantallas() {
        if (pantallaSeleccionada != null) {
            duplicarPantalla = new Pantallas();
            k++;
            l = BigInteger.valueOf(k);
            duplicarPantalla.setSecuencia(l);
            duplicarPantalla.setCodigo(pantallaSeleccionada.getCodigo());
            duplicarPantalla.setNombre(pantallaSeleccionada.getNombre());
            duplicarPantalla.setTabla(pantallaSeleccionada.getTabla());
            duplicarPantalla.setEmpresa(pantallaSeleccionada.getEmpresa());
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarPantalla");
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroPantallas').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void confirmarDuplicar() {
        int contador = 0;
        int duplicados = 0;
        Integer a = 0;
        a = null;
        mensajeValidacion = " ";
        if (duplicarPantalla.getCodigo() == a) {
            mensajeValidacion = " Los campos marcados con asterisco son obligatorios ";
        } else {
            contador++;
        }

        if (duplicarPantalla.getNombre().equals(null) || duplicarPantalla.getNombre().isEmpty()) {
            mensajeValidacion = " Los campos marcados con asterisco son obligatorios ";
        } else {
            contador++;
        }

        if (duplicarPantalla.getTabla() == null) {
            mensajeValidacion = " Los campos marcados con asterisco son obligatorios ";
        } else {
            contador++;
        }

        if (duplicarPantalla.getEmpresa() == null) {
            mensajeValidacion = " Los campos marcados con asterisco son obligatorios ";
        } else {
            contador++;
        }

        for (int i = 0; i < listaPantallas.size(); i++) {
            if (duplicarPantalla.getEmpresa().equals(listaPantallas.get(i).getEmpresa())) {
                duplicados++;
            }
        }

        if (duplicados > 0) {
            mensajeValidacion = "Existe un registro asociado a la empresa ingresada";
        }

        if (contador == 4) {
            if (bandera == 1) {
                codigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosPantallas:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                nombre = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosPantallas:nombre");
                nombre.setFilterStyle("display: none; visibility: hidden;");
                tabla = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosPantallas:tabla");
                tabla.setFilterStyle("display: none; visibility: hidden;");
                empresa = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosPantallas:empresa");
                empresa.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosPantallas");
                bandera = 0;
                listaPantallasFiltrar = null;
                tipoLista = 0;
                altoTabla = "335";
            }
            k++;
            l = BigInteger.valueOf(k);
            duplicarPantalla.setSecuencia(l);
            listaPantallasCrear.add(duplicarPantalla);
            listaPantallas.add(0, duplicarPantalla);
            pantallaSeleccionada = duplicarPantalla;
            RequestContext.getCurrentInstance().update("form:datosPantallas");
            contarRegistros();
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroPantallas').hide()");
            duplicarPantalla = new Pantallas();
        } else {
            contador = 0;
            RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
            RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
        }
    }

    public void limpiarDuplicarPantallas() {
        duplicarPantalla = new Pantallas();
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosPantallasExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "MotivosDefinitivas", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosPantallasExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "MotivosDefinitivas", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void verificarRastro() {
        if (pantallaSeleccionada != null) {
            int resultado = administrarRastros.obtenerTabla(pantallaSeleccionada.getSecuencia(), "PANTALLAS"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
        } else if (administrarRastros.verificarHistoricosTabla("PANTALLAS")) { // igual acá
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
    }

    public void salir() {
        limpiarListasValor();
        if (bandera == 1) {
            codigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosPantallas:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            nombre = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosPantallas:nombre");
            nombre.setFilterStyle("display: none; visibility: hidden;");
            tabla = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosPantallas:tabla");
            tabla.setFilterStyle("display: none; visibility: hidden;");
            empresa = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosPantallas:empresa");
            empresa.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosPantallas");
            bandera = 0;
            listaPantallasFiltrar = null;
            tipoLista = 0;
            altoTabla = "335";
        }
        listaPantallasBorrar.clear();
        listaPantallasCrear.clear();
        listaPantallasModificar.clear();
        pantallaSeleccionada = null;
        k = 0;
        listaPantallas = null;
        guardado = true;
        RequestContext.getCurrentInstance().update("form:datosPantallas");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        navegar("atras");
    }

    public void contarRegistros() {
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
    }

    public void contarRegistrosTablas() {
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroTablas");
    }

    public void contarRegistrosEmpresas() {
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroEmpresas");
    }

    public void habilitarBotonLov() {
        activarLov = false;
        RequestContext.getCurrentInstance().update("form:listaValores");
    }

    public void deshabilitarBotonLov() {
        activarLov = true;
        RequestContext.getCurrentInstance().update("form:listaValores");

    }

    public void actualizarTabla() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            pantallaSeleccionada.setTabla(tablaSeleccionada);
            if (!listaPantallasCrear.contains(pantallaSeleccionada)) {
                if (listaPantallasModificar.isEmpty()) {
                    listaPantallasModificar.add(pantallaSeleccionada);
                } else if (!listaPantallasModificar.contains(pantallaSeleccionada)) {
                    listaPantallasModificar.add(pantallaSeleccionada);
                }
            }
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            deshabilitarBotonLov();
            RequestContext.getCurrentInstance().update("form:datosPantallas");
        } else if (tipoActualizacion == 1) {
            nuevaPantalla.setTabla(tablaSeleccionada);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoObj");
        } else if (tipoActualizacion == 2) {
            duplicarPantalla.setTabla(tablaSeleccionada);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarPantalla");
        }
        lovTablasFiltrar = null;
        tablaSeleccionada = null;
        aceptar = true;
        tipoActualizacion = -1;
        RequestContext.getCurrentInstance().update("formularioDialogos:tablasDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovTablas");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarM");
        context.reset("formularioDialogos:lovTablas:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovTablas').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('tablasDialogo').hide()");
    }

    public void cancelarCambioTabla() {
        lovTablasFiltrar = null;
        tablaSeleccionada = null;
        aceptar = true;
        tipoActualizacion = -1;
        RequestContext.getCurrentInstance().update("formularioDialogos:tablasDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovTablas");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarM");
        RequestContext.getCurrentInstance().reset("formularioDialogos:lovTablas:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovTablas').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('tablasDialogo').hide()");
    }

    public void actualizarEmpresa() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            pantallaSeleccionada.setEmpresa(empresaSeleccionada);
            if (!listaPantallasCrear.contains(pantallaSeleccionada)) {
                if (listaPantallasModificar.isEmpty()) {
                    listaPantallasModificar.add(pantallaSeleccionada);
                } else if (!listaPantallasModificar.contains(pantallaSeleccionada)) {
                    listaPantallasModificar.add(pantallaSeleccionada);
                }
            }
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            deshabilitarBotonLov();
            RequestContext.getCurrentInstance().update("form:datosPantallas");
        } else if (tipoActualizacion == 1) {
            nuevaPantalla.setEmpresa(empresaSeleccionada);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoObj");
        } else if (tipoActualizacion == 2) {
            nuevaPantalla.setEmpresa(empresaSeleccionada);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarPantalla");
        }
        lovEmpresasFiltrar = null;
        empresaSeleccionada = null;
        aceptar = true;
        tipoActualizacion = -1;
        RequestContext.getCurrentInstance().update("formularioDialogos:empresasDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovEmpresas");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarEM");
        context.reset("formularioDialogos:lovEmpresas:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovEmpresas').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('empresasDialogo').hide()");
    }

    public void cancelarCambioEmpresa() {
        lovEmpresasFiltrar = null;
        empresaSeleccionada = null;
        aceptar = true;
        tipoActualizacion = -1;
        RequestContext.getCurrentInstance().update("formularioDialogos:empresasDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovEmpresas");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarEM");
        RequestContext.getCurrentInstance().reset("formularioDialogos:lovEmpresas:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovEmpresas').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('empresasDialogo').hide()");
    }

    /////SETS Y GETS///////
    public List<Pantallas> getListaPantallas() {
        if (listaPantallas == null) {
            listaPantallas = administrarPantallas.consultarPantallas();
        }
        return listaPantallas;
    }

    public void setListaPantallas(List<Pantallas> listaPantallas) {
        this.listaPantallas = listaPantallas;
    }

    public List<Pantallas> getListaPantallasFiltrar() {
        return listaPantallasFiltrar;
    }

    public void setListaPantallasFiltrar(List<Pantallas> listaPantallasFiltrar) {
        this.listaPantallasFiltrar = listaPantallasFiltrar;
    }

    public Pantallas getNuevaPantalla() {
        return nuevaPantalla;
    }

    public void setNuevaPantalla(Pantallas nuevaPantalla) {
        this.nuevaPantalla = nuevaPantalla;
    }

    public Pantallas getDuplicarPantalla() {
        return duplicarPantalla;
    }

    public void setDuplicarPantalla(Pantallas duplicarPantalla) {
        this.duplicarPantalla = duplicarPantalla;
    }

    public Pantallas getEditarPantalla() {
        return editarPantalla;
    }

    public void setEditarPantalla(Pantallas editarPantalla) {
        this.editarPantalla = editarPantalla;
    }

    public Pantallas getPantallaSeleccionada() {
        return pantallaSeleccionada;
    }

    public void setPantallaSeleccionada(Pantallas pantallaSeleccionada) {
        this.pantallaSeleccionada = pantallaSeleccionada;
    }

    public List<Empresas> getLovEmpresas() {
        if (lovEmpresas == null) {
            lovEmpresas = administrarPantallas.consultarEmpresas();
        }
        return lovEmpresas;
    }

    public void setLovEmpresas(List<Empresas> lovEmpresas) {
        this.lovEmpresas = lovEmpresas;
    }

    public List<Empresas> getLovEmpresasFiltrar() {
        return lovEmpresasFiltrar;
    }

    public void setLovEmpresasFiltrar(List<Empresas> lovEmpresasFiltrar) {
        this.lovEmpresasFiltrar = lovEmpresasFiltrar;
    }

    public Empresas getEmpresaSeleccionada() {
        return empresaSeleccionada;
    }

    public void setEmpresaSeleccionada(Empresas empresaSeleccionada) {
        this.empresaSeleccionada = empresaSeleccionada;
    }

    public List<Tablas> getLovTablas() {
        if (lovTablas == null) {
            lovTablas = administrarPantallas.consultarTablas();
        }
        return lovTablas;
    }

    public void setLovTablas(List<Tablas> lovTablas) {
        this.lovTablas = lovTablas;
    }

    public List<Tablas> getLovTablasFiltrar() {
        return lovTablasFiltrar;
    }

    public void setLovTablasFiltrar(List<Tablas> lovTablasFiltrar) {
        this.lovTablasFiltrar = lovTablasFiltrar;
    }

    public Tablas getTablaSeleccionada() {
        return tablaSeleccionada;
    }

    public void setTablaSeleccionada(Tablas tablaSeleccionada) {
        this.tablaSeleccionada = tablaSeleccionada;
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
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosPantallas");
        infoRegistro = String.valueOf(tabla.getRowCount());
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

    public String getInfoRegistroEmpresas() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovEmpresas");
        infoRegistroEmpresas = String.valueOf(tabla.getRowCount());
        return infoRegistroEmpresas;
    }

    public void setInfoRegistroEmpresas(String infoRegistroEmpresas) {
        this.infoRegistroEmpresas = infoRegistroEmpresas;
    }

    public String getInfoRegistroTablas() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovTablas");
        infoRegistroTablas = String.valueOf(tabla.getRowCount());
        return infoRegistroTablas;
    }

    public void setInfoRegistroTablas(String infoRegistroTablas) {
        this.infoRegistroTablas = infoRegistroTablas;
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

}
