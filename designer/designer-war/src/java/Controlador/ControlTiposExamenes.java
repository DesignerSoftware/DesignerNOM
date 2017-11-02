/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.TiposExamenes;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarRastrosInterface;
import InterfaceAdministrar.AdministrarTiposExamenesInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import ControlNavegacion.ControlListaNavegacion;
import java.util.Map;
import java.util.LinkedHashMap;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.primefaces.component.column.Column;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.export.Exporter;
import org.primefaces.context.RequestContext;

/**
 *
 * @author John Pineda
 */
@ManagedBean
@SessionScoped
public class ControlTiposExamenes implements Serializable {

    private static Logger log = Logger.getLogger(ControlTiposExamenes.class);

    @EJB
    AdministrarTiposExamenesInterface administrarTiposExamenes;
    @EJB
    AdministrarRastrosInterface administrarRastros;
    private List<TiposExamenes> listTiposExamenes;
    private List<TiposExamenes> filtrarTiposExamenes;
    private List<TiposExamenes> crearTiposExamenes;
    private List<TiposExamenes> modificarTiposExamenes;
    private List<TiposExamenes> borrarTiposExamenes;
    private TiposExamenes nuevoTipoExamen;
    private TiposExamenes duplicarTipoExamen;
    private TiposExamenes editarTipoExamen;
    private TiposExamenes tiposExamenesSeleccionado;
    private int cualCelda, tipoLista, tipoActualizacion, k, bandera;
    private BigInteger l;
    private boolean aceptar, guardado;
    private Column codigo, descripcion, minimoNormal, maximoNormal, diasRecurrencia;
    private int registrosBorrados;
    private String mensajeValidacion;
    private BigInteger tiposExamenesCargos;
    private BigInteger vigenciasExamenesMedicos;
    private int tamano;
    private String infoRegistro;
    private String msgError;
    private String paginaAnterior = "nominaf";
    private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

    public ControlTiposExamenes() {
        listTiposExamenes = null;
        crearTiposExamenes = new ArrayList<TiposExamenes>();
        modificarTiposExamenes = new ArrayList<TiposExamenes>();
        borrarTiposExamenes = new ArrayList<TiposExamenes>();
        guardado = true;
        editarTipoExamen = new TiposExamenes();
        nuevoTipoExamen = new TiposExamenes();
        duplicarTipoExamen = new TiposExamenes();
        tamano = 310;
        mapParametros.put("paginaAnterior", paginaAnterior);
    }

    public void limpiarListasValor() {

    }

    @PreDestroy
    public void destruyendose() {
        log.info(this.getClass().getName() + ".destruyendose() @Destroy");
    }

    @PostConstruct
    public void inicializarAdministrador() {
        log.info(this.getClass().getName() + ".inicializarAdministrador() @PostConstruct");
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarTiposExamenes.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
            listTiposExamenes = null;
            getListTiposExamenes();
            if (listTiposExamenes != null) {
                if (!listTiposExamenes.isEmpty()) {
                    tiposExamenesSeleccionado = listTiposExamenes.get(0);
                }
            }
        } catch (Exception e) {
            log.error("Error postconstruct " + this.getClass().getName() + ":  ", e);
            log.error("Causa: " + e.getCause());
        }
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
        String pagActual = "tipoexamen";
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

    public void eventoFiltrar() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
        contarRegistros();
    }

    public void contarRegistros() {
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
    }

    public void cambiarIndice(TiposExamenes tipoE, int celda) {
        tiposExamenesSeleccionado = tipoE;
        cualCelda = celda;
        tiposExamenesSeleccionado.getSecuencia();
        if (cualCelda == 0) {
            tiposExamenesSeleccionado.getCodigo();
        } else if (cualCelda == 1) {
            tiposExamenesSeleccionado.getNombre();
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void cancelarModificacion() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 1) {
            //CERRAR FILTRADO
            codigo = (Column) c.getViewRoot().findComponent("form:datosTipoExamen:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTipoExamen:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            minimoNormal = (Column) c.getViewRoot().findComponent("form:datosTipoExamen:minimoNormal");
            minimoNormal.setFilterStyle("display: none; visibility: hidden;");
            maximoNormal = (Column) c.getViewRoot().findComponent("form:datosTipoExamen:maximoNormal");
            maximoNormal.setFilterStyle("display: none; visibility: hidden;");
            diasRecurrencia = (Column) c.getViewRoot().findComponent("form:datosTipoExamen:diasRecurrencia");
            diasRecurrencia.setFilterStyle("display: none; visibility: hidden;");
            tamano = 310;
            RequestContext.getCurrentInstance().update("form:datosTipoExamen");
            bandera = 0;
            filtrarTiposExamenes = null;
            tipoLista = 0;
        }

        borrarTiposExamenes.clear();
        crearTiposExamenes.clear();
        modificarTiposExamenes.clear();
        tiposExamenesSeleccionado = null;
        k = 0;
        listTiposExamenes = null;
        guardado = true;
        getListTiposExamenes();
        contarRegistros();
        RequestContext.getCurrentInstance().update("form:datosTipoExamen");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void salir() {
        limpiarListasValor();
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 1) {
            //CERRAR FILTRADO
            codigo = (Column) c.getViewRoot().findComponent("form:datosTipoExamen:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTipoExamen:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            minimoNormal = (Column) c.getViewRoot().findComponent("form:datosTipoExamen:minimoNormal");
            minimoNormal.setFilterStyle("display: none; visibility: hidden;");
            maximoNormal = (Column) c.getViewRoot().findComponent("form:datosTipoExamen:maximoNormal");
            maximoNormal.setFilterStyle("display: none; visibility: hidden;");
            diasRecurrencia = (Column) c.getViewRoot().findComponent("form:datosTipoExamen:diasRecurrencia");
            diasRecurrencia.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTipoExamen");
            bandera = 0;
            filtrarTiposExamenes = null;
            tipoLista = 0;
            tamano = 310;
        }
        borrarTiposExamenes.clear();
        crearTiposExamenes.clear();
        modificarTiposExamenes.clear();
        tiposExamenesSeleccionado = null;
        k = 0;
        listTiposExamenes = null;
        guardado = true;
        getListTiposExamenes();
        contarRegistros();
        RequestContext.getCurrentInstance().update("form:datosTipoExamen");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        navegar("atras");
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            tamano = 290;
            codigo = (Column) c.getViewRoot().findComponent("form:datosTipoExamen:codigo");
            codigo.setFilterStyle("width: 85% !important;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTipoExamen:descripcion");
            descripcion.setFilterStyle("width: 85% !important;");
            minimoNormal = (Column) c.getViewRoot().findComponent("form:datosTipoExamen:minimoNormal");
            minimoNormal.setFilterStyle("width: 85% !important;");
            maximoNormal = (Column) c.getViewRoot().findComponent("form:datosTipoExamen:maximoNormal");
            maximoNormal.setFilterStyle("width: 85% !important;");
            diasRecurrencia = (Column) c.getViewRoot().findComponent("form:datosTipoExamen:diasRecurrencia");
            diasRecurrencia.setFilterStyle("width: 85% !important;");
            RequestContext.getCurrentInstance().update("form:datosTipoExamen");
            bandera = 1;
        } else if (bandera == 1) {
            tamano = 310;
            codigo = (Column) c.getViewRoot().findComponent("form:datosTipoExamen:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTipoExamen:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            minimoNormal = (Column) c.getViewRoot().findComponent("form:datosTipoExamen:minimoNormal");
            minimoNormal.setFilterStyle("display: none; visibility: hidden;");
            maximoNormal = (Column) c.getViewRoot().findComponent("form:datosTipoExamen:maximoNormal");
            maximoNormal.setFilterStyle("display: none; visibility: hidden;");
            diasRecurrencia = (Column) c.getViewRoot().findComponent("form:datosTipoExamen:diasRecurrencia");
            diasRecurrencia.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTipoExamen");
            bandera = 0;
            filtrarTiposExamenes = null;
            tipoLista = 0;
        }
    }

    public void modificarTipoExamen(TiposExamenes tipoE) {
        tiposExamenesSeleccionado = tipoE;
        if (!crearTiposExamenes.contains(tiposExamenesSeleccionado)) {
            if (modificarTiposExamenes.isEmpty()) {
                modificarTiposExamenes.add(tiposExamenesSeleccionado);
            } else if (!modificarTiposExamenes.contains(tiposExamenesSeleccionado)) {
                modificarTiposExamenes.add(tiposExamenesSeleccionado);
            }
            guardado = false;
        }
        RequestContext.getCurrentInstance().update("form:datosTipoExamen");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void borrandoTiposExamenes() {

        if (tiposExamenesSeleccionado != null) {
            if (!modificarTiposExamenes.isEmpty() && modificarTiposExamenes.contains(tiposExamenesSeleccionado)) {
                int modIndex = modificarTiposExamenes.indexOf(tiposExamenesSeleccionado);
                modificarTiposExamenes.remove(modIndex);
                borrarTiposExamenes.add(tiposExamenesSeleccionado);
            } else if (!crearTiposExamenes.isEmpty() && crearTiposExamenes.contains(tiposExamenesSeleccionado)) {
                int crearIndex = crearTiposExamenes.indexOf(tiposExamenesSeleccionado);
                crearTiposExamenes.remove(crearIndex);
            } else {
                borrarTiposExamenes.add(tiposExamenesSeleccionado);
            }
            listTiposExamenes.remove(tiposExamenesSeleccionado);
            if (tipoLista == 1) {
                filtrarTiposExamenes.remove(tiposExamenesSeleccionado);
            }
            contarRegistros();
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosTipoExamen");
            tiposExamenesSeleccionado = null;
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }

    }

    public void verificarBorrado() {
        try {
            tiposExamenesCargos = administrarTiposExamenes.contarTiposExamenesCargosTipoExamen(tiposExamenesSeleccionado.getSecuencia());
            vigenciasExamenesMedicos = administrarTiposExamenes.contarVigenciasExamenesMedicosTipoExamen(tiposExamenesSeleccionado.getSecuencia());
            if (tiposExamenesCargos.equals(new BigInteger("0")) && vigenciasExamenesMedicos.equals(new BigInteger("0"))) {
                borrandoTiposExamenes();
            } else {
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:validacionBorrar");
                RequestContext.getCurrentInstance().execute("PF('validacionBorrar').show()");
                tiposExamenesCargos = new BigInteger("-1");
            }
        } catch (Exception e) {
            log.error("ERROR ControlTiposExamenes verificarBorrado ERROR  ", e);
        }
    }

    public void revisarDialogoGuardar() {
        if (!borrarTiposExamenes.isEmpty() || !crearTiposExamenes.isEmpty() || !modificarTiposExamenes.isEmpty()) {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:confirmarGuardar");
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
        }

    }

    public void guardarTiposExamenes() {
        try {
            if (guardado == false) {
                msgError = "";
                if (!borrarTiposExamenes.isEmpty()) {
                    for (int i = 0; i < borrarTiposExamenes.size(); i++) {
                        msgError = administrarTiposExamenes.borrarTiposExamenes(borrarTiposExamenes.get(i));
                    }
                    registrosBorrados = borrarTiposExamenes.size();
                    RequestContext.getCurrentInstance().update("form:mostrarBorrados");
                    RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
                    borrarTiposExamenes.clear();
                }
                if (!crearTiposExamenes.isEmpty()) {
                    for (int i = 0; i < crearTiposExamenes.size(); i++) {
                        msgError = administrarTiposExamenes.crearTiposExamenes(crearTiposExamenes.get(i));
                    }
                    crearTiposExamenes.clear();
                }
                if (!modificarTiposExamenes.isEmpty()) {
                    for (int i = 0; i < modificarTiposExamenes.size(); i++) {
                        msgError = administrarTiposExamenes.modificarTiposExamenes(modificarTiposExamenes.get(i));
                    }
                    modificarTiposExamenes.clear();
                }
                if (msgError.equals("EXITO")) {
                    listTiposExamenes = null;
                    getListTiposExamenes();
                    RequestContext.getCurrentInstance().update("form:datosTipoExamen");
                    k = 0;
                    FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                    RequestContext.getCurrentInstance().update("form:growl");
                    guardado = true;
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
        if (tiposExamenesSeleccionado != null) {
            editarTipoExamen = tiposExamenesSeleccionado;

            RequestContext context = RequestContext.getCurrentInstance();
            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editCodigo");
                RequestContext.getCurrentInstance().execute("PF('editCodigo').show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editDescripcion");
                RequestContext.getCurrentInstance().execute("PF('editDescripcion').show()");
                cualCelda = -1;

            } else if (cualCelda == 2) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editMinimoNormal");
                RequestContext.getCurrentInstance().execute("PF('editMinimoNormal').show()");
                cualCelda = -1;
            } else if (cualCelda == 3) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editMaximoNormal");
                RequestContext.getCurrentInstance().execute("PF('editMaximoNormal').show()");
                cualCelda = -1;
            } else if (cualCelda == 4) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editDiasRecurrencia");
                RequestContext.getCurrentInstance().execute("PF('editDiasRecurrencia').show()");
                cualCelda = -1;
            }
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void agregarNuevoTiposExamenes() {
        int contador = 0;
        int duplicados = 0;
        Integer a = 0;
        a = null;
        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();
        if (nuevoTipoExamen.getCodigo() == a) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
        } else {
            for (int x = 0; x < listTiposExamenes.size(); x++) {
                if (listTiposExamenes.get(x).getCodigo() == nuevoTipoExamen.getCodigo()) {
                    duplicados++;
                }
            }
            if (duplicados > 0) {
                mensajeValidacion = "Existe un registro con el código ingresado. Por favor ingrese un código válido";
            } else {
                contador++;
            }
        }
        if (nuevoTipoExamen.getNombre() == null || nuevoTipoExamen.getNombre().isEmpty()) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
        } else {
            contador++;
        }

        if (contador == 2) {
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                codigo = (Column) c.getViewRoot().findComponent("form:datosTipoExamen:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosTipoExamen:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                minimoNormal = (Column) c.getViewRoot().findComponent("form:datosTipoExamen:minimoNormal");
                minimoNormal.setFilterStyle("display: none; visibility: hidden;");
                maximoNormal = (Column) c.getViewRoot().findComponent("form:datosTipoExamen:maximoNormal");
                maximoNormal.setFilterStyle("display: none; visibility: hidden;");
                diasRecurrencia = (Column) c.getViewRoot().findComponent("form:datosTipoExamen:diasRecurrencia");
                diasRecurrencia.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosTipoExamen");
                bandera = 0;
                filtrarTiposExamenes = null;
                tipoLista = 0;
            }
            k++;
            l = BigInteger.valueOf(k);
            nuevoTipoExamen.setSecuencia(l);
            crearTiposExamenes.add(nuevoTipoExamen);
            listTiposExamenes.add(0, nuevoTipoExamen);
            tiposExamenesSeleccionado = nuevoTipoExamen;
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:datosTipoExamen");
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().execute("PF('nuevoRegistroTiposExamenes').hide()");
            nuevoTipoExamen = new TiposExamenes();
        } else {
            RequestContext.getCurrentInstance().update("form:validacionNuevaCentroCosto");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevaCentroCosto').show()");
            contador = 0;
        }
    }

    public void limpiarNuevoTiposExamenes() {
        nuevoTipoExamen = new TiposExamenes();
    }

    public void duplicandoTiposExamenes() {
        if (tiposExamenesSeleccionado != null) {
            duplicarTipoExamen = new TiposExamenes();
            k++;
            l = BigInteger.valueOf(k);
            duplicarTipoExamen.setSecuencia(l);
            duplicarTipoExamen.setCodigo(tiposExamenesSeleccionado.getCodigo());
            duplicarTipoExamen.setNombre(tiposExamenesSeleccionado.getNombre());
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTE");
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroTiposExamenes').show()");
        } else{
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void confirmarDuplicar() {
        int contador = 0;
        mensajeValidacion = " ";
        int duplicados = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        Integer a = 0;
        a = null;

        if (duplicarTipoExamen.getCodigo() == a) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
        } else {
            for (int x = 0; x < listTiposExamenes.size(); x++) {
                if (listTiposExamenes.get(x).getCodigo() == duplicarTipoExamen.getCodigo()) {
                    duplicados++;
                }
            }
            if (duplicados > 0) {
                mensajeValidacion = "Existe un registro con el código ingresado. Por favor ingrese un código válido";
            } else {
                contador++;
                duplicados = 0;
            }
        }
        if (duplicarTipoExamen.getNombre() == null || duplicarTipoExamen.getNombre().isEmpty()) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
        } else {
            contador++;
        }

        if (contador == 2) {
            crearTiposExamenes.add(duplicarTipoExamen);
            listTiposExamenes.add(0, duplicarTipoExamen);
            RequestContext.getCurrentInstance().update("form:datosTipoExamen");
            tiposExamenesSeleccionado = duplicarTipoExamen;
            guardado = false;
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                //CERRAR FILTRADO
                codigo = (Column) c.getViewRoot().findComponent("form:datosTipoExamen:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosTipoExamen:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                minimoNormal = (Column) c.getViewRoot().findComponent("form:datosTipoExamen:minimoNormal");
                minimoNormal.setFilterStyle("display: none; visibility: hidden;");
                maximoNormal = (Column) c.getViewRoot().findComponent("form:datosTipoExamen:maximoNormal");
                maximoNormal.setFilterStyle("display: none; visibility: hidden;");
                diasRecurrencia = (Column) c.getViewRoot().findComponent("form:datosTipoExamen:diasRecurrencia");
                diasRecurrencia.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosTipoExamen");
                bandera = 0;
                filtrarTiposExamenes = null;
                tipoLista = 0;
            }
            duplicarTipoExamen = new TiposExamenes();
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroTiposExamenes').hide()");
        } else {
            contador = 0;
            RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
            RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
        }
    }

    public void limpiarDuplicarTiposExamenes() {
        duplicarTipoExamen = new TiposExamenes();
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTipoExamenExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "TIPOSEXAMENES", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTipoExamenExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "TIPOSEXAMENES", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tiposExamenesSeleccionado != null) {
            int resultado = administrarRastros.obtenerTabla(tiposExamenesSeleccionado.getSecuencia(), "TIPOSEXAMENES"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
        } else if (administrarRastros.verificarHistoricosTabla("TIPOSEXAMENES")) { // igual acá
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
    }

    //*/*/*/*/*/*/*/*/*/*-/-*//-*/-*/*/*-*/-*/-*/*/*/*/*/---/*/*/*/*/-*/-*/-*/-*/-*/
    public List<TiposExamenes> getListTiposExamenes() {
        if (listTiposExamenes == null) {
            listTiposExamenes = administrarTiposExamenes.consultarTiposExamenes();
        }
        return listTiposExamenes;
    }

    public void setListTiposExamenes(List<TiposExamenes> listTiposExamenes) {
        this.listTiposExamenes = listTiposExamenes;
    }

    public TiposExamenes getNuevoTipoExamen() {
        return nuevoTipoExamen;
    }

    public void setNuevoTipoExamen(TiposExamenes nuevoTipoExamen) {
        this.nuevoTipoExamen = nuevoTipoExamen;
    }

    public TiposExamenes getDuplicarTipoExamen() {
        return duplicarTipoExamen;
    }

    public void setDuplicarTipoExamen(TiposExamenes duplicarTipoExamen) {
        this.duplicarTipoExamen = duplicarTipoExamen;
    }

    public TiposExamenes getEditarTipoExamen() {
        return editarTipoExamen;
    }

    public void setEditarTipoExamen(TiposExamenes editarTipoExamen) {
        this.editarTipoExamen = editarTipoExamen;
    }

    public int getRegistrosBorrados() {
        return registrosBorrados;
    }

    public void setRegistrosBorrados(int registrosBorrados) {
        this.registrosBorrados = registrosBorrados;
    }

    public String getMensajeValidacion() {
        return mensajeValidacion;
    }

    public void setMensajeValidacion(String mensajeValidacion) {
        this.mensajeValidacion = mensajeValidacion;
    }

    public List<TiposExamenes> getFiltrarTiposExamenes() {
        return filtrarTiposExamenes;
    }

    public void setFiltrarTiposExamenes(List<TiposExamenes> filtrarTiposExamenes) {
        this.filtrarTiposExamenes = filtrarTiposExamenes;
    }

    public boolean isGuardado() {
        return guardado;
    }

    public void setGuardado(boolean guardado) {
        this.guardado = guardado;
    }

    public TiposExamenes getTiposExamenesSeleccionado() {
        return tiposExamenesSeleccionado;
    }

    public void setTiposExamenesSeleccionado(TiposExamenes tiposExamenesSeleccionado) {
        this.tiposExamenesSeleccionado = tiposExamenesSeleccionado;
    }

    public int getTamano() {
        return tamano;
    }

    public void setTamano(int tamano) {
        this.tamano = tamano;
    }

    public String getInfoRegistro() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosTipoExamen");
        infoRegistro = String.valueOf(tabla.getRowCount());
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public void setAceptar(boolean aceptar) {
        this.aceptar = aceptar;
    }

    public String getMsgError() {
        return msgError;
    }

    public void setMsgError(String msgError) {
        this.msgError = msgError;
    }

    
    
}
