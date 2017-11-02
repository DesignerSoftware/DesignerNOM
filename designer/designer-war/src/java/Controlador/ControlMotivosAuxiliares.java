/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import ControlNavegacion.ControlListaNavegacion;
import Entidades.MotivosAuxiliares;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarMotivosAuxiliaresInterface;
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
@Named(value = "controlMotivosAuxiliares")
@SessionScoped
public class ControlMotivosAuxiliares implements Serializable {
 private static Logger log = Logger.getLogger(ControlMotivosAuxiliares.class);

    @EJB
    AdministrarMotivosAuxiliaresInterface administrarMotivosAuxiliares;
    @EJB
    AdministrarRastrosInterface administrarRastros;
    private List<MotivosAuxiliares> listMotivosAuxiliares;
    private List<MotivosAuxiliares> filtrarMotivosAuxiliares;
    private List<MotivosAuxiliares> crearMotivosAuxiliares;
    private List<MotivosAuxiliares> modificarMotivosAuxiliares;
    private List<MotivosAuxiliares> borrarMotivosAuxiliares;
    private MotivosAuxiliares nuevoMotivoAuxiliar;
    private MotivosAuxiliares duplicarMotivoAuxiliar;
    private MotivosAuxiliares editarMotivoAuxiliar;
    private MotivosAuxiliares motivoAuxiliarSeleccionado;
    private int cualCelda, tipoLista, tipoActualizacion, k, bandera;
    private BigInteger l;
    private boolean aceptar, guardado;
    private Column codigo, descripcion;
    private String altoTabla;
    private String infoRegistro;
    private int registrosBorrados;
    private String mensajeValidacion;
    private String msgError;
    private String paginaAnterior = "nominaf";
    private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();
    private BigInteger verificarEerPrestamos;
    private BigInteger verificarEmbargos;

    public ControlMotivosAuxiliares() {
        listMotivosAuxiliares = null;
        crearMotivosAuxiliares = new ArrayList<MotivosAuxiliares>();
        modificarMotivosAuxiliares = new ArrayList<MotivosAuxiliares>();
        borrarMotivosAuxiliares = new ArrayList<MotivosAuxiliares>();
        editarMotivoAuxiliar = new MotivosAuxiliares();
        nuevoMotivoAuxiliar = new MotivosAuxiliares();
        duplicarMotivoAuxiliar = new MotivosAuxiliares();
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

    //Reemplazar la funcion volverAtras, retornarPagina, Redirigir.....Atras.etc
    public void navegar(String pag) {
        FacesContext fc = FacesContext.getCurrentInstance();
        ControlListaNavegacion controlListaNavegacion = (ControlListaNavegacion) fc.getApplication().evaluateExpressionGet(fc, "#{controlListaNavegacion}", ControlListaNavegacion.class);
        String pagActual = "motivoauxiliar";
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
        log.info(this.getClass().getName() + ".destruyendoce() @Destroy");
    }

    @PostConstruct
    public void inicializarAdministrador() {
        log.info(this.getClass().getName() + ".inicializarAdministrador() @PostConstruct");
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarMotivosAuxiliares.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
            listMotivosAuxiliares = null;
            getListMotivosAuxiliares();
            if (listMotivosAuxiliares != null) {
                motivoAuxiliarSeleccionado = listMotivosAuxiliares.get(0);
            }
        } catch (Exception e) {
            log.error("Error postconstruct " + this.getClass().getName() + ":  ", e);
            log.error("Causa: " + e.getCause());
        }
    }

    public void cambiarIndice(MotivosAuxiliares motivo, int celda) {

        motivoAuxiliarSeleccionado = motivo;
        cualCelda = celda;
        motivoAuxiliarSeleccionado.getSecuencia();
        if (cualCelda == 0) {
            motivoAuxiliarSeleccionado.getCodigo();
        } else if (cualCelda == 1) {
            motivoAuxiliarSeleccionado.getNombre();
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void cancelarModificacion() {
        if (bandera == 1) {
            //CERRAR FILTRADO
            codigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosMotivosAux:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosMotivosAux:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosMotivosAux");
            bandera = 0;
            filtrarMotivosAuxiliares = null;
            tipoLista = 0;
        }

        borrarMotivosAuxiliares.clear();
        crearMotivosAuxiliares.clear();
        modificarMotivosAuxiliares.clear();
        k = 0;
        listMotivosAuxiliares = null;
        motivoAuxiliarSeleccionado = null;
        guardado = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosMotivosAux");
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
            codigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosMotivosAux:codigo");
            codigo.setFilterStyle("width: 85% !important;");
            descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosMotivosAux:descripcion");
            descripcion.setFilterStyle("width: 85% !important;");
            RequestContext.getCurrentInstance().update("form:datosMotivosAux");
            bandera = 1;
        } else if (bandera == 1) {
            altoTabla = "335";
            codigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosMotivosAux:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosMotivosAux:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosMotivosAux");
            bandera = 0;
            filtrarMotivosAuxiliares = null;
            tipoLista = 0;
        }
    }

    public void modificandoMotivoAuxiliar(MotivosAuxiliares motivo) {
        motivoAuxiliarSeleccionado = motivo;
        if (!crearMotivosAuxiliares.contains(motivoAuxiliarSeleccionado)) {
            if (modificarMotivosAuxiliares.isEmpty()) {
                modificarMotivosAuxiliares.add(motivoAuxiliarSeleccionado);
            } else if (!modificarMotivosAuxiliares.contains(motivoAuxiliarSeleccionado)) {
                modificarMotivosAuxiliares.add(motivoAuxiliarSeleccionado);
            }
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosMotivosAux");
        }
    }



    public void borrandoMotivosAuxiliares() {

        if (motivoAuxiliarSeleccionado != null) {
            if (!modificarMotivosAuxiliares.isEmpty() && modificarMotivosAuxiliares.contains(motivoAuxiliarSeleccionado)) {
                int modIndex = modificarMotivosAuxiliares.indexOf(motivoAuxiliarSeleccionado);
                modificarMotivosAuxiliares.remove(modIndex);
                borrarMotivosAuxiliares.add(motivoAuxiliarSeleccionado);
            } else if (!crearMotivosAuxiliares.isEmpty() && crearMotivosAuxiliares.contains(motivoAuxiliarSeleccionado)) {
                int crearIndex = crearMotivosAuxiliares.indexOf(motivoAuxiliarSeleccionado);
                crearMotivosAuxiliares.remove(crearIndex);
            } else {
                borrarMotivosAuxiliares.add(motivoAuxiliarSeleccionado);
            }
            listMotivosAuxiliares.remove(motivoAuxiliarSeleccionado);
            if (tipoLista == 1) {
                filtrarMotivosAuxiliares.remove(motivoAuxiliarSeleccionado);
            }
            motivoAuxiliarSeleccionado = null;
            RequestContext.getCurrentInstance().update("form:datosMotivosAux");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            guardado = false;
        }

    }

    public void revisarDialogoGuardar() {
        if (!borrarMotivosAuxiliares.isEmpty() || !crearMotivosAuxiliares.isEmpty() || !modificarMotivosAuxiliares.isEmpty()) {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:confirmarGuardar");
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
        }

    }

    public void guardarMotivoAuxiliar() {
        RequestContext context = RequestContext.getCurrentInstance();
        try {
            if (guardado == false) {
                msgError = "";
                if (!borrarMotivosAuxiliares.isEmpty()) {
                    for (int i = 0; i < borrarMotivosAuxiliares.size(); i++) {
                        msgError = administrarMotivosAuxiliares.borrarMotivosAuxiliares(borrarMotivosAuxiliares.get(i));
                    }
                    registrosBorrados = borrarMotivosAuxiliares.size();
                    RequestContext.getCurrentInstance().update("form:mostrarBorrados");
                    RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
                    borrarMotivosAuxiliares.clear();
                }
                if (!crearMotivosAuxiliares.isEmpty()) {
                    for (int i = 0; i < crearMotivosAuxiliares.size(); i++) {
                        msgError = administrarMotivosAuxiliares.crearMotivosAuxiliares(crearMotivosAuxiliares.get(i));
                    }
                    crearMotivosAuxiliares.clear();
                }
                if (!modificarMotivosAuxiliares.isEmpty()) {
                    for (int i = 0; i < modificarMotivosAuxiliares.size(); i++) {
                        msgError = administrarMotivosAuxiliares.modificarMotivosAuxiliares(modificarMotivosAuxiliares.get(i));
                    }
                    modificarMotivosAuxiliares.clear();
                }
                if (msgError.equals("EXITO")) {
                    listMotivosAuxiliares = null;
                    getListMotivosAuxiliares();
                    k = 0;
                    FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                    RequestContext.getCurrentInstance().update("form:growl");
                    contarRegistros();
                    guardado = true;
                    motivoAuxiliarSeleccionado = null;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                    RequestContext.getCurrentInstance().update("form:datosMotivosAux");
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
        if (motivoAuxiliarSeleccionado != null) {
            editarMotivoAuxiliar = motivoAuxiliarSeleccionado;
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

    public void agregarNuevoMotivosAuxiliares() {
        int contador = 0;
        int duplicados = 0;
        Integer a = 0;
        a = null;
        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();
        if (nuevoMotivoAuxiliar.getCodigo() == a) {
            mensajeValidacion = " Los campos marcados con asterisco son obligatorios ";
        } else {
            for (int x = 0; x < listMotivosAuxiliares.size(); x++) {
                if (listMotivosAuxiliares.get(x).getCodigo() == nuevoMotivoAuxiliar.getCodigo()) {
                    duplicados++;
                }
            }
            if (duplicados > 0) {
                mensajeValidacion = "El código ingresado ya está registrado. Por favor ingrese un código válido";
            } else {
                contador++;
            }
        }
        if (nuevoMotivoAuxiliar.getNombre() == (null)) {
            mensajeValidacion = " Los campos marcados con asterisco son obligatorios ";
        } else {
            contador++;
        }

        if (contador == 2) {
            if (bandera == 1) {
                //CERRAR FILTRADO
                log.info("Desactivar");
                codigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosMotivosAux:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosMotivosAux:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosMotivosAux");
                bandera = 0;
                filtrarMotivosAuxiliares = null;
                tipoLista = 0;
                altoTabla = "335";
            }
            k++;
            l = BigInteger.valueOf(k);
            nuevoMotivoAuxiliar.setSecuencia(l);
            crearMotivosAuxiliares.add(nuevoMotivoAuxiliar);
            listMotivosAuxiliares.add(0, nuevoMotivoAuxiliar);
            motivoAuxiliarSeleccionado = nuevoMotivoAuxiliar;
            RequestContext.getCurrentInstance().update("form:datosMotivosAux");
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().execute("PF('nuevoRegistroTiposReemplazos').hide()");
            nuevoMotivoAuxiliar = new MotivosAuxiliares();
        } else {
            RequestContext.getCurrentInstance().update("form:validacionNuevaCentroCosto");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevaCentroCosto').show()");
            contador = 0;
        }
    }

    public void limpiarNuevoMotivosAuxiliares() {
        nuevoMotivoAuxiliar = new MotivosAuxiliares();
    }

    //------------------------------------------------------------------------------
    public void duplicandoMotivosAuxiliares() {
        if (motivoAuxiliarSeleccionado != null) {
            duplicarMotivoAuxiliar = new MotivosAuxiliares();
            k++;
            l = BigInteger.valueOf(k);
            duplicarMotivoAuxiliar.setSecuencia(l);
            duplicarMotivoAuxiliar.setCodigo(motivoAuxiliarSeleccionado.getCodigo());
            duplicarMotivoAuxiliar.setNombre(motivoAuxiliarSeleccionado.getNombre());
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTTR");
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroTiposReemplazos').show()");
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

        if (duplicarMotivoAuxiliar.getCodigo() == a) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
        } else {
            for (int x = 0; x < listMotivosAuxiliares.size(); x++) {
                if (listMotivosAuxiliares.get(x).getCodigo() == duplicarMotivoAuxiliar.getCodigo()) {
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
        if (duplicarMotivoAuxiliar.getNombre() == null) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
        } else {
            contador++;
        }

        if (contador == 2) {
            listMotivosAuxiliares.add(0, duplicarMotivoAuxiliar);
            crearMotivosAuxiliares.add(duplicarMotivoAuxiliar);
            motivoAuxiliarSeleccionado = duplicarMotivoAuxiliar;
            RequestContext.getCurrentInstance().update("form:datosMotivosAux");
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            if (bandera == 1) {
                //CERRAR FILTRADO
                codigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosMotivosAux:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosMotivosAux:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosMotivosAux");
                bandera = 0;
                filtrarMotivosAuxiliares = null;
                tipoLista = 0;
                altoTabla = "335";
            }
            duplicarMotivoAuxiliar = new MotivosAuxiliares();
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroTiposReemplazos').hide()");

        } else {
            contador = 0;
            RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
            RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
        }
    }

    public void limpiarDuplicarMotivosAuxiliares() {
        duplicarMotivoAuxiliar = new MotivosAuxiliares();
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosMotivosAuxExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "MotivosAuxiliares", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosMotivosAuxExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "MotivosAuxiliares", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (motivoAuxiliarSeleccionado != null) {
            int resultado = administrarRastros.obtenerTabla(motivoAuxiliarSeleccionado.getSecuencia(), "MOTIVOSAUXILIARES"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
        } else if (administrarRastros.verificarHistoricosTabla("MOTIVOSAUXILIARES")) { // igual acá
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
    }

    public void salir() {
        limpiarListasValor();
        if (bandera == 1) {
            //CERRAR FILTRADO
            codigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosMotivosAux:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosMotivosAux:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosMotivosAux");
            bandera = 0;
            filtrarMotivosAuxiliares = null;
            tipoLista = 0;
        }

        borrarMotivosAuxiliares.clear();
        crearMotivosAuxiliares.clear();
        modificarMotivosAuxiliares.clear();
        k = 0;
        listMotivosAuxiliares = null;
        motivoAuxiliarSeleccionado = null;
        guardado = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosMotivosAux");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        navegar("atras");
    }

    public void contarRegistros() {
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
    }

    //--------///////////////////////---------------------*****//*/*/*/*/*/-****----
    public List<MotivosAuxiliares> getListMotivosAuxiliares() {
        if (listMotivosAuxiliares == null) {
            listMotivosAuxiliares = administrarMotivosAuxiliares.buscarMotivosAuxiliares();
        }
        return listMotivosAuxiliares;
    }

    public void setListMotivosAuxiliares(List<MotivosAuxiliares> listMotivosAuxiliares) {
        this.listMotivosAuxiliares = listMotivosAuxiliares;
    }

    public List<MotivosAuxiliares> getFiltrarMotivosAuxiliares() {
        return filtrarMotivosAuxiliares;
    }

    public void setFiltrarMotivosAuxiliares(List<MotivosAuxiliares> filtrarMotivosAuxiliares) {
        this.filtrarMotivosAuxiliares = filtrarMotivosAuxiliares;
    }

    public MotivosAuxiliares getNuevoMotivoAuxiliar() {
        return nuevoMotivoAuxiliar;
    }

    public void setNuevoMotivoAuxiliar(MotivosAuxiliares nuevoMotivoAuxiliar) {
        this.nuevoMotivoAuxiliar = nuevoMotivoAuxiliar;
    }

    public MotivosAuxiliares getDuplicarMotivoAuxiliar() {
        return duplicarMotivoAuxiliar;
    }

    public void setDuplicarMotivoAuxiliar(MotivosAuxiliares duplicarMotivoAuxiliar) {
        this.duplicarMotivoAuxiliar = duplicarMotivoAuxiliar;
    }

    public MotivosAuxiliares getEditarMotivoAuxiliar() {
        return editarMotivoAuxiliar;
    }

    public void setEditarMotivoAuxiliar(MotivosAuxiliares editarMotivoAuxiliar) {
        this.editarMotivoAuxiliar = editarMotivoAuxiliar;
    }

    public boolean isGuardado() {
        return guardado;
    }

    public void setGuardado(boolean guardado) {
        this.guardado = guardado;
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

    public MotivosAuxiliares getMotivoAuxiliarSeleccionado() {
        return motivoAuxiliarSeleccionado;
    }

    public void setMotivoAuxiliarSeleccionado(MotivosAuxiliares motivoAuxiliarSeleccionado) {
        this.motivoAuxiliarSeleccionado = motivoAuxiliarSeleccionado;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public void setAceptar(boolean aceptar) {
        this.aceptar = aceptar;
    }

    public String getAltoTabla() {
        return altoTabla;
    }

    public void setAltoTabla(String altoTabla) {
        this.altoTabla = altoTabla;
    }

    public String getInfoRegistro() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosMotivosAux");
        infoRegistro = String.valueOf(tabla.getRowCount());
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

    public String getMsgError() {
        return msgError;
    }

    public void setMsgError(String msgError) {
        this.msgError = msgError;
    }

}
