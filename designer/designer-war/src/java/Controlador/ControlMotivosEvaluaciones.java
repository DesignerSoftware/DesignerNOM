/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import ControlNavegacion.ControlListaNavegacion;
import Entidades.MotivosEvaluaciones;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarMotivosEvaluacionesInterface;
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
@Named(value = "controlMotivosEvaluaciones")
@SessionScoped
public class ControlMotivosEvaluaciones implements Serializable {

    private static Logger log = Logger.getLogger(ControlMotivosEvaluaciones.class);

    @EJB
    AdministrarMotivosEvaluacionesInterface administrarMotivosEvaluaciones;
    @EJB
    AdministrarRastrosInterface administrarRastros;

    private List<MotivosEvaluaciones> listMotivosEvaluaciones;
    private List<MotivosEvaluaciones> filtrarMotivosEvaluaciones;
    private List<MotivosEvaluaciones> crearMotivosEvaluaciones;
    private List<MotivosEvaluaciones> modificarMotivosEvaluaciones;
    private List<MotivosEvaluaciones> borrarMotivosEvaluaciones;
    private MotivosEvaluaciones nuevoMotivoEvaluacion;
    private MotivosEvaluaciones duplicarMotivoEvaluacion;
    private MotivosEvaluaciones editarMotivoEvaluacion;
    private MotivosEvaluaciones motivoEvaluacionSeleccionado;
    //otros
    private int cualCelda, tipoLista, tipoActualizacion, k, bandera;
    private BigInteger l;
    private boolean aceptar, guardado;
    private Column codigo, descripcion;
    private String altoTabla;
    //borrado
    private String infoRegistro;
    private String mensajeValidacion;
    private String paginaAnterior = "nominaf";
    private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();
    private String msgError;

    public ControlMotivosEvaluaciones() {
        listMotivosEvaluaciones = null;
        crearMotivosEvaluaciones = new ArrayList<MotivosEvaluaciones>();
        modificarMotivosEvaluaciones = new ArrayList<MotivosEvaluaciones>();
        borrarMotivosEvaluaciones = new ArrayList<MotivosEvaluaciones>();
        editarMotivoEvaluacion = new MotivosEvaluaciones();
        nuevoMotivoEvaluacion = new MotivosEvaluaciones();
        duplicarMotivoEvaluacion = new MotivosEvaluaciones();
        guardado = true;
        mapParametros.put("paginaAnterior", paginaAnterior);
        altoTabla = "335";
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
        String pagActual = "motivoevaluacion";
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
            administrarMotivosEvaluaciones.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
            listMotivosEvaluaciones = null;
            getListMotivosEvaluaciones();
            if (listMotivosEvaluaciones != null) {
                motivoEvaluacionSeleccionado = listMotivosEvaluaciones.get(0);
            }

        } catch (Exception e) {
            log.error("Error postconstruct " + this.getClass().getName() + ":  ", e);
            log.error("Causa: " + e.getCause());
        }
    }

    public void cambiarIndice(MotivosEvaluaciones motivo, int celda) {

        motivoEvaluacionSeleccionado = motivo;
        cualCelda = celda;
        motivoEvaluacionSeleccionado.getSecuencia();
        if (cualCelda == 0) {
            motivoEvaluacionSeleccionado.getCodigo();
        } else if (cualCelda == 1) {
            motivoEvaluacionSeleccionado.getDescripcion();
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void cancelarModificacion() {
        if (bandera == 1) {
            //CERRAR FILTRADO
            codigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosMotivosAusentismo:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosMotivosAusentismo:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosMotivosAusentismo");
            bandera = 0;
            filtrarMotivosEvaluaciones = null;
            tipoLista = 0;
            altoTabla = "335";
        }

        borrarMotivosEvaluaciones.clear();
        crearMotivosEvaluaciones.clear();
        modificarMotivosEvaluaciones.clear();
        motivoEvaluacionSeleccionado = null;
        k = 0;
        listMotivosEvaluaciones = null;
        guardado = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosMotivosAusentismo");
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
            codigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosMotivosAusentismo:codigo");
            codigo.setFilterStyle("width: 85% !important;");
            descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosMotivosAusentismo:descripcion");
            descripcion.setFilterStyle("width: 85% !important;");
            RequestContext.getCurrentInstance().update("form:datosMotivosAusentismo");
            bandera = 1;
        } else if (bandera == 1) {
            altoTabla = "335";
            codigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosMotivosAusentismo:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosMotivosAusentismo:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosMotivosAusentismo");
            bandera = 0;
            filtrarMotivosEvaluaciones = null;
            tipoLista = 0;
        }
    }

    public void modificandoMotivoEvaluacion(MotivosEvaluaciones motivo) {
        motivoEvaluacionSeleccionado = motivo;
        if (!crearMotivosEvaluaciones.contains(motivoEvaluacionSeleccionado)) {
            if (modificarMotivosEvaluaciones.isEmpty()) {
                modificarMotivosEvaluaciones.add(motivoEvaluacionSeleccionado);
            } else if (!modificarMotivosEvaluaciones.contains(motivoEvaluacionSeleccionado)) {
                modificarMotivosEvaluaciones.add(motivoEvaluacionSeleccionado);
            }
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
        RequestContext.getCurrentInstance().update("form:datosMotivosAusentismo");
    }

    public void borrandoMotivoEvaluacion() {

        if (motivoEvaluacionSeleccionado != null) {
            if (!modificarMotivosEvaluaciones.isEmpty() && modificarMotivosEvaluaciones.contains(motivoEvaluacionSeleccionado)) {
                int modIndex = modificarMotivosEvaluaciones.indexOf(motivoEvaluacionSeleccionado);
                modificarMotivosEvaluaciones.remove(modIndex);
                borrarMotivosEvaluaciones.add(motivoEvaluacionSeleccionado);
            } else if (!crearMotivosEvaluaciones.isEmpty() && crearMotivosEvaluaciones.contains(motivoEvaluacionSeleccionado)) {
                int crearIndex = crearMotivosEvaluaciones.indexOf(motivoEvaluacionSeleccionado);
                crearMotivosEvaluaciones.remove(crearIndex);
            } else {
                borrarMotivosEvaluaciones.add(motivoEvaluacionSeleccionado);
            }
            listMotivosEvaluaciones.remove(motivoEvaluacionSeleccionado);
            if (tipoLista == 1) {
                filtrarMotivosEvaluaciones.remove(motivoEvaluacionSeleccionado);
            }
            motivoEvaluacionSeleccionado = null;
            RequestContext context = RequestContext.getCurrentInstance();
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosMotivosAusentismo");

        }

    }

    public void revisarDialogoGuardar() {
        if (!borrarMotivosEvaluaciones.isEmpty() || !crearMotivosEvaluaciones.isEmpty() || !modificarMotivosEvaluaciones.isEmpty()) {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:confirmarGuardar");
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
        }
    }

    public void guardarMotivoEvaluacions() {
        RequestContext context = RequestContext.getCurrentInstance();
        try {
            if (guardado == false) {
                msgError = "";
                if (!borrarMotivosEvaluaciones.isEmpty()) {
                    for (int i = 0; i < borrarMotivosEvaluaciones.size(); i++) {
                        msgError = administrarMotivosEvaluaciones.borrarMotivosEvaluaciones(borrarMotivosEvaluaciones.get(i));
                    }
                    borrarMotivosEvaluaciones.clear();
                }
                if (!crearMotivosEvaluaciones.isEmpty()) {
                    for (int i = 0; i < crearMotivosEvaluaciones.size(); i++) {
                        msgError = administrarMotivosEvaluaciones.crearMotivosEvaluaciones(crearMotivosEvaluaciones.get(i));
                    }
                    crearMotivosEvaluaciones.clear();
                }
                if (!modificarMotivosEvaluaciones.isEmpty()) {
                    for (int i = 0; i < modificarMotivosEvaluaciones.size(); i++) {
                        msgError = administrarMotivosEvaluaciones.modificarMotivosEvaluaciones(modificarMotivosEvaluaciones.get(i));
                    }
                }
                modificarMotivosEvaluaciones.clear();

                if (msgError.equals("EXITO")) {
                    listMotivosEvaluaciones = null;
                    getListMotivosEvaluaciones();
                    k = 0;
                    FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                    RequestContext.getCurrentInstance().update("form:growl");
                    contarRegistros();
                    guardado = true;
                    motivoEvaluacionSeleccionado = null;
                    RequestContext.getCurrentInstance().update("form:datosMotivosAusentismo");
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                } else {
                    RequestContext.getCurrentInstance().update("formularioDialogos:errorGuardado");
                    RequestContext.getCurrentInstance().execute("PF('errorGuardado').show()");
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
        if (motivoEvaluacionSeleccionado != null) {
            editarMotivoEvaluacion = motivoEvaluacionSeleccionado;
            RequestContext context = RequestContext.getCurrentInstance();
            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editCodigo");
                RequestContext.getCurrentInstance().execute("PF('editCodigo').show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editDescripcion");
                RequestContext.getCurrentInstance().execute("PF('editDescripcion').show()");
                cualCelda = -1;
            }
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void agregarNuevoMotivoEvaluacion() {
        int contador = 0;
        int duplicados = 0;
        Integer a = 0;
        a = null;
        mensajeValidacion = " ";
        if (nuevoMotivoEvaluacion.getCodigo() == a) {
            mensajeValidacion = " Los campos marcados con asterisco son obligatorios ";
        } else {
            for (int i = 0; i < listMotivosEvaluaciones.size(); i++) {
                if (listMotivosEvaluaciones.get(i).getCodigo() == nuevoMotivoEvaluacion.getCodigo()) {
                    duplicados++;
                }
            }
            if (duplicados > 0) {
                mensajeValidacion = "El código ingresado ya está registrado. Por favor ingrese un código válido";
            } else {
                contador++;
            }
        }
        if (nuevoMotivoEvaluacion.getDescripcion() == (null)) {
            mensajeValidacion = " Los campos marcados con asterisco son obligatorios\n";
        } else {
            contador++;
        }

        if (contador == 2) {
            if (bandera == 1) {
                codigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosMotivosAusentismo:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosMotivosAusentismo:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosMotivosAusentismo");
                bandera = 0;
                filtrarMotivosEvaluaciones = null;
                tipoLista = 0;
                altoTabla = "335";
            }
            k++;
            l = BigInteger.valueOf(k);
            nuevoMotivoEvaluacion.setSecuencia(l);
            crearMotivosEvaluaciones.add(nuevoMotivoEvaluacion);
            listMotivosEvaluaciones.add(0, nuevoMotivoEvaluacion);
            motivoEvaluacionSeleccionado = nuevoMotivoEvaluacion;
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:datosMotivosAusentismo");
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().execute("PF('nuevoRegistroMotivoEvaluacion').hide()");
            nuevoMotivoEvaluacion = new MotivosEvaluaciones();

        } else {
            RequestContext.getCurrentInstance().update("form:validacionNuevaCentroCosto");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevaCentroCosto').show()");
            contador = 0;
        }
    }

    public void limpiarNuevoMotivoEvaluacion() {
        nuevoMotivoEvaluacion = new MotivosEvaluaciones();
    }

    public void duplicandoMotivoEvaluacion() {
        if (motivoEvaluacionSeleccionado != null) {
            duplicarMotivoEvaluacion = new MotivosEvaluaciones();
            k++;
            l = BigInteger.valueOf(k);
            duplicarMotivoEvaluacion.setSecuencia(l);
            duplicarMotivoEvaluacion.setCodigo(motivoEvaluacionSeleccionado.getCodigo());
            duplicarMotivoEvaluacion.setDescripcion(motivoEvaluacionSeleccionado.getDescripcion());
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTTR");
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroMotivosEvaluaciones').show()");
        } else {
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

        if (duplicarMotivoEvaluacion.getCodigo() == a) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
        } else {
            for (int x = 0; x < listMotivosEvaluaciones.size(); x++) {
                if (listMotivosEvaluaciones.get(x).getCodigo() == duplicarMotivoEvaluacion.getCodigo()) {
                    duplicados++;
                }
            }
            if (duplicados > 0) {
                mensajeValidacion = "El código ingresado ya está registrado. Por favor ingrese un código válido";
            } else {
                log.info("bandera");
                contador++;
                duplicados = 0;
            }
        }
        if (duplicarMotivoEvaluacion.getDescripcion() == null) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
        } else {
            contador++;
        }
        if (contador == 2) {
            listMotivosEvaluaciones.add(0, duplicarMotivoEvaluacion);
            crearMotivosEvaluaciones.add(duplicarMotivoEvaluacion);
            motivoEvaluacionSeleccionado = duplicarMotivoEvaluacion;
            RequestContext.getCurrentInstance().update("form:datosMotivosAusentismo");
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            if (bandera == 1) {
                codigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosMotivosAusentismo:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosMotivosAusentismo:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosMotivosAusentismo");
                bandera = 0;
                filtrarMotivosEvaluaciones = null;
                tipoLista = 0;
                altoTabla = "335";
            }
            duplicarMotivoEvaluacion = new MotivosEvaluaciones();
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroMotivosEvaluaciones').hide()");

        } else {
            contador = 0;
            RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
            RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
        }
    }

    public void limpiarDuplicarMotivoEvaluacion() {
        duplicarMotivoEvaluacion = new MotivosEvaluaciones();
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosMotivosAusentismoExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "MotivosEvaluaciones", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosMotivosAusentismoExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "MotivosEvaluaciones", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void verificarRastro() {
        if (motivoEvaluacionSeleccionado != null) {
            int resultado = administrarRastros.obtenerTabla(motivoEvaluacionSeleccionado.getSecuencia(), "MOTIVOSAUSENTISMOS"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
        } else if (administrarRastros.verificarHistoricosTabla("MOTIVOSAUSENTISMOS")) { // igual acá
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
    }

    public void salir() {
        limpiarListasValor();
        if (bandera == 1) {
            codigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosMotivosAusentismo:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosMotivosAusentismo:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosMotivosAusentismo");
            bandera = 0;
            filtrarMotivosEvaluaciones = null;
            tipoLista = 0;
            altoTabla = "335";
        }
        borrarMotivosEvaluaciones.clear();
        crearMotivosEvaluaciones.clear();
        modificarMotivosEvaluaciones.clear();
        motivoEvaluacionSeleccionado = null;
        k = 0;
        listMotivosEvaluaciones = null;
        guardado = true;
        RequestContext.getCurrentInstance().update("form:datosMotivosAusentismo");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        navegar("atras");
    }

    public void contarRegistros() {
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
    }

    ///////////GETS Y SETS////////////
    public List<MotivosEvaluaciones> getListMotivosEvaluaciones() {
        if (listMotivosEvaluaciones == null) {
            listMotivosEvaluaciones = administrarMotivosEvaluaciones.consultarMotivosEvaluaciones();
        }
        return listMotivosEvaluaciones;
    }

    public void setListMotivosEvaluaciones(List<MotivosEvaluaciones> listMotivosEvaluaciones) {
        this.listMotivosEvaluaciones = listMotivosEvaluaciones;
    }

    public List<MotivosEvaluaciones> getFiltrarMotivosEvaluaciones() {
        return filtrarMotivosEvaluaciones;
    }

    public void setFiltrarMotivosEvaluaciones(List<MotivosEvaluaciones> filtrarMotivosEvaluaciones) {
        this.filtrarMotivosEvaluaciones = filtrarMotivosEvaluaciones;
    }

    public MotivosEvaluaciones getNuevoMotivoEvaluacion() {
        return nuevoMotivoEvaluacion;
    }

    public void setNuevoMotivoEvaluacion(MotivosEvaluaciones nuevoMotivoEvaluacion) {
        this.nuevoMotivoEvaluacion = nuevoMotivoEvaluacion;
    }

    public MotivosEvaluaciones getDuplicarMotivoEvaluacion() {
        return duplicarMotivoEvaluacion;
    }

    public void setDuplicarMotivoEvaluacion(MotivosEvaluaciones duplicarMotivoEvaluacion) {
        this.duplicarMotivoEvaluacion = duplicarMotivoEvaluacion;
    }

    public MotivosEvaluaciones getEditarMotivoEvaluacion() {
        return editarMotivoEvaluacion;
    }

    public void setEditarMotivoEvaluacion(MotivosEvaluaciones editarMotivoEvaluacion) {
        this.editarMotivoEvaluacion = editarMotivoEvaluacion;
    }

    public MotivosEvaluaciones getMotivoEvaluacionSeleccionado() {
        return motivoEvaluacionSeleccionado;
    }

    public void setMotivoEvaluacionSeleccionado(MotivosEvaluaciones motivoEvaluacionSeleccionado) {
        this.motivoEvaluacionSeleccionado = motivoEvaluacionSeleccionado;
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
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosMotivosAusentismo");
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

}
