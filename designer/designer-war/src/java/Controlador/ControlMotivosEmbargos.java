/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.MotivosEmbargos;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarMotivosEmbargosInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
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
public class ControlMotivosEmbargos implements Serializable {

    private static Logger log = Logger.getLogger(ControlMotivosEmbargos.class);

    @EJB
    AdministrarMotivosEmbargosInterface administrarMotivosEmbargos;
    @EJB
    AdministrarRastrosInterface administrarRastros;
    private List<MotivosEmbargos> listMotivosEmbargos;
    private List<MotivosEmbargos> filtrarMotivosEmbargos;
    private List<MotivosEmbargos> crearMotivosEmbargos;
    private List<MotivosEmbargos> modificarMotivosEmbargos;
    private List<MotivosEmbargos> borrarMotivosEmbargos;
    private MotivosEmbargos nuevoMotivoEmbargo;
    private MotivosEmbargos duplicarMotivoEmbargo;
    private MotivosEmbargos editarMotivoEmbargo;
    private MotivosEmbargos motivoEmbargoSeleccionado;
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

    public ControlMotivosEmbargos() {
        listMotivosEmbargos = null;
        crearMotivosEmbargos = new ArrayList<MotivosEmbargos>();
        modificarMotivosEmbargos = new ArrayList<MotivosEmbargos>();
        borrarMotivosEmbargos = new ArrayList<MotivosEmbargos>();
        editarMotivoEmbargo = new MotivosEmbargos();
        nuevoMotivoEmbargo = new MotivosEmbargos();
        duplicarMotivoEmbargo = new MotivosEmbargos();
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
        String pagActual = "motivoembargo";
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
            administrarMotivosEmbargos.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
            listMotivosEmbargos = null;
            getListMotivosEmbargos();
            if (listMotivosEmbargos != null) {
                motivoEmbargoSeleccionado = listMotivosEmbargos.get(0);
            }
        } catch (Exception e) {
            log.error("Error postconstruct " + this.getClass().getName() + ":  ", e);
            log.error("Causa: " + e.getCause());
        }
    }

    public void cambiarIndice(MotivosEmbargos motivo, int celda) {

        motivoEmbargoSeleccionado = motivo;
        cualCelda = celda;
        motivoEmbargoSeleccionado.getSecuencia();
        if (cualCelda == 0) {
            motivoEmbargoSeleccionado.getCodigo();
        } else if (cualCelda == 1) {
            motivoEmbargoSeleccionado.getNombre();
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void cancelarModificacion() {
        if (bandera == 1) {
            //CERRAR FILTRADO
            codigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoReemplazo:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoReemplazo:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTipoReemplazo");
            bandera = 0;
            filtrarMotivosEmbargos = null;
            tipoLista = 0;
        }

        borrarMotivosEmbargos.clear();
        crearMotivosEmbargos.clear();
        modificarMotivosEmbargos.clear();
        k = 0;
        listMotivosEmbargos = null;
        motivoEmbargoSeleccionado = null;
        guardado = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosTipoReemplazo");
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
            codigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoReemplazo:codigo");
            codigo.setFilterStyle("width: 85% !important;");
            descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoReemplazo:descripcion");
            descripcion.setFilterStyle("width: 85% !important;");
            RequestContext.getCurrentInstance().update("form:datosTipoReemplazo");
            bandera = 1;
        } else if (bandera == 1) {
            altoTabla = "335";
            codigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoReemplazo:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoReemplazo:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTipoReemplazo");
            bandera = 0;
            filtrarMotivosEmbargos = null;
            tipoLista = 0;
        }
    }

    public void modificandoMotivoEmbargo(MotivosEmbargos motivo) {
        motivoEmbargoSeleccionado = motivo;
        if (!crearMotivosEmbargos.contains(motivoEmbargoSeleccionado)) {
            if (modificarMotivosEmbargos.isEmpty()) {
                modificarMotivosEmbargos.add(motivoEmbargoSeleccionado);
            } else if (!modificarMotivosEmbargos.contains(motivoEmbargoSeleccionado)) {
                modificarMotivosEmbargos.add(motivoEmbargoSeleccionado);
            }
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosTipoReemplazo");
        }
    }

    public void verificarBorrado() {
        try {
            if (motivoEmbargoSeleccionado != null) {
                verificarEerPrestamos = administrarMotivosEmbargos.contarEersPrestamosMotivoEmbargo(motivoEmbargoSeleccionado.getSecuencia());
                verificarEmbargos = administrarMotivosEmbargos.contarEmbargosMotivoEmbargo(motivoEmbargoSeleccionado.getSecuencia());
                if (!verificarEerPrestamos.equals(new BigInteger("0")) || !verificarEmbargos.equals(new BigInteger("0"))) {
                    RequestContext context = RequestContext.getCurrentInstance();
                    RequestContext.getCurrentInstance().update("form:validacionBorrar");
                    RequestContext.getCurrentInstance().execute("PF('validacionBorrar').show()");
                    verificarEerPrestamos = new BigInteger("-1");
                    verificarEmbargos = new BigInteger("-1");
                } else {
                    borrandoMotivosEmbargos();
                }
            } else {
                RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
            }
        } catch (Exception e) {
            log.error("ERROR ControlTiposCertificados verificarBorrado ERROR  ", e);
        }
    }

    public void borrandoMotivosEmbargos() {

        if (motivoEmbargoSeleccionado != null) {
            if (!modificarMotivosEmbargos.isEmpty() && modificarMotivosEmbargos.contains(motivoEmbargoSeleccionado)) {
                int modIndex = modificarMotivosEmbargos.indexOf(motivoEmbargoSeleccionado);
                modificarMotivosEmbargos.remove(modIndex);
                borrarMotivosEmbargos.add(motivoEmbargoSeleccionado);
            } else if (!crearMotivosEmbargos.isEmpty() && crearMotivosEmbargos.contains(motivoEmbargoSeleccionado)) {
                int crearIndex = crearMotivosEmbargos.indexOf(motivoEmbargoSeleccionado);
                crearMotivosEmbargos.remove(crearIndex);
            } else {
                borrarMotivosEmbargos.add(motivoEmbargoSeleccionado);
            }
            listMotivosEmbargos.remove(motivoEmbargoSeleccionado);
            if (tipoLista == 1) {
                filtrarMotivosEmbargos.remove(motivoEmbargoSeleccionado);
            }
            motivoEmbargoSeleccionado = null;
            RequestContext.getCurrentInstance().update("form:datosTipoReemplazo");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            guardado = false;
        }

    }

    public void revisarDialogoGuardar() {
        if (!borrarMotivosEmbargos.isEmpty() || !crearMotivosEmbargos.isEmpty() || !modificarMotivosEmbargos.isEmpty()) {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:confirmarGuardar");
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
        }

    }

    public void guardarMotivoEmbargo() {
        RequestContext context = RequestContext.getCurrentInstance();
        try {
            if (guardado == false) {
                msgError = "";
                if (!borrarMotivosEmbargos.isEmpty()) {
                    for (int i = 0; i < borrarMotivosEmbargos.size(); i++) {
                        msgError = administrarMotivosEmbargos.borrarMotivosEmbargos(borrarMotivosEmbargos.get(i));
                    }
                    registrosBorrados = borrarMotivosEmbargos.size();
                    RequestContext.getCurrentInstance().update("form:mostrarBorrados");
                    RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
                    borrarMotivosEmbargos.clear();
                }
                if (!crearMotivosEmbargos.isEmpty()) {
                    for (int i = 0; i < crearMotivosEmbargos.size(); i++) {
                        msgError = administrarMotivosEmbargos.crearMotivosEmbargos(crearMotivosEmbargos.get(i));
                    }
                    crearMotivosEmbargos.clear();
                }
                if (!modificarMotivosEmbargos.isEmpty()) {
                    for (int i = 0; i < modificarMotivosEmbargos.size(); i++) {
                        msgError = administrarMotivosEmbargos.modificarMotivosEmbargos(modificarMotivosEmbargos.get(i));
                    }
                    modificarMotivosEmbargos.clear();
                }
                if (msgError.equals("EXITO")) {
                    listMotivosEmbargos = null;
                    getListMotivosEmbargos();
                    k = 0;
                    FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                    RequestContext.getCurrentInstance().update("form:growl");
                    contarRegistros();
                    guardado = true;
                    motivoEmbargoSeleccionado = null;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                    RequestContext.getCurrentInstance().update("form:datosTipoReemplazo");
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
        if (motivoEmbargoSeleccionado != null) {
            editarMotivoEmbargo = motivoEmbargoSeleccionado;
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

    public void agregarNuevoMotivosEmbargos() {
        int contador = 0;
        int duplicados = 0;
        Integer a = 0;
        a = null;
        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();
        if (nuevoMotivoEmbargo.getCodigo() == a) {
            mensajeValidacion = " Los campos marcados con asterisco son obligatorios ";
        } else {
            for (int x = 0; x < listMotivosEmbargos.size(); x++) {
                if (listMotivosEmbargos.get(x).getCodigo() == nuevoMotivoEmbargo.getCodigo()) {
                    duplicados++;
                }
            }
            if (duplicados > 0) {
                mensajeValidacion = "El código ingresado ya está registrado. Por favor ingrese un código válido";
            } else {
                contador++;
            }
        }
        if (nuevoMotivoEmbargo.getNombre() == (null)) {
            mensajeValidacion = " Los campos marcados con asterisco son obligatorios ";
        } else {
            contador++;
        }

        if (contador == 2) {
            if (bandera == 1) {
                //CERRAR FILTRADO
                log.info("Desactivar");
                codigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoReemplazo:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoReemplazo:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosTipoReemplazo");
                bandera = 0;
                filtrarMotivosEmbargos = null;
                tipoLista = 0;
                altoTabla = "335";
            }
            k++;
            l = BigInteger.valueOf(k);
            nuevoMotivoEmbargo.setSecuencia(l);
            crearMotivosEmbargos.add(nuevoMotivoEmbargo);
            listMotivosEmbargos.add(0, nuevoMotivoEmbargo);
            motivoEmbargoSeleccionado = nuevoMotivoEmbargo;
            RequestContext.getCurrentInstance().update("form:datosTipoReemplazo");
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().execute("PF('nuevoRegistroTiposReemplazos').hide()");
            nuevoMotivoEmbargo = new MotivosEmbargos();
        } else {
            RequestContext.getCurrentInstance().update("form:validacionNuevaCentroCosto");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevaCentroCosto').show()");
            contador = 0;
        }
    }

    public void limpiarNuevoMotivosEmbargos() {
        nuevoMotivoEmbargo = new MotivosEmbargos();
    }

    //------------------------------------------------------------------------------
    public void duplicandoMotivosEmbargos() {
        if (motivoEmbargoSeleccionado != null) {
            duplicarMotivoEmbargo = new MotivosEmbargos();
            k++;
            l = BigInteger.valueOf(k);
            duplicarMotivoEmbargo.setSecuencia(l);
            duplicarMotivoEmbargo.setCodigo(motivoEmbargoSeleccionado.getCodigo());
            duplicarMotivoEmbargo.setNombre(motivoEmbargoSeleccionado.getNombre());
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

        if (duplicarMotivoEmbargo.getCodigo() == a) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
        } else {
            for (int x = 0; x < listMotivosEmbargos.size(); x++) {
                if (listMotivosEmbargos.get(x).getCodigo() == duplicarMotivoEmbargo.getCodigo()) {
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
        if (duplicarMotivoEmbargo.getNombre() == null) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
        } else {
            contador++;
        }

        if (contador == 2) {
            listMotivosEmbargos.add(0, duplicarMotivoEmbargo);
            crearMotivosEmbargos.add(duplicarMotivoEmbargo);
            motivoEmbargoSeleccionado = duplicarMotivoEmbargo;
            RequestContext.getCurrentInstance().update("form:datosTipoReemplazo");
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            if (bandera == 1) {
                //CERRAR FILTRADO
                codigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoReemplazo:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoReemplazo:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosTipoReemplazo");
                bandera = 0;
                filtrarMotivosEmbargos = null;
                tipoLista = 0;
                altoTabla = "335";
            }
            duplicarMotivoEmbargo = new MotivosEmbargos();
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroTiposReemplazos').hide()");

        } else {
            contador = 0;
            RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
            RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
        }
    }

    public void limpiarDuplicarMotivosEmbargos() {
        duplicarMotivoEmbargo = new MotivosEmbargos();
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTipoReemplazoExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "MotivosEmbargos", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTipoReemplazoExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "MotivosEmbargos", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (motivoEmbargoSeleccionado != null) {
            int resultado = administrarRastros.obtenerTabla(motivoEmbargoSeleccionado.getSecuencia(), "MOTIVOSEMBARGOS"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
        } else if (administrarRastros.verificarHistoricosTabla("MOTIVOSEMBARGOS")) { // igual acá
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
    }

    public void salir() {
        limpiarListasValor();
        if (bandera == 1) {
            //CERRAR FILTRADO
            codigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoReemplazo:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoReemplazo:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTipoReemplazo");
            bandera = 0;
            filtrarMotivosEmbargos = null;
            tipoLista = 0;
        }

        borrarMotivosEmbargos.clear();
        crearMotivosEmbargos.clear();
        modificarMotivosEmbargos.clear();
        k = 0;
        listMotivosEmbargos = null;
        motivoEmbargoSeleccionado = null;
        guardado = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosTipoReemplazo");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        navegar("atras");
    }

    public void contarRegistros() {
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
    }

    //--------///////////////////////---------------------*****//*/*/*/*/*/-****----
    public List<MotivosEmbargos> getListMotivosEmbargos() {
        if (listMotivosEmbargos == null) {
            listMotivosEmbargos = administrarMotivosEmbargos.mostrarMotivosEmbargos();
        }
        return listMotivosEmbargos;
    }

    public void setListMotivosEmbargos(List<MotivosEmbargos> listMotivosEmbargos) {
        this.listMotivosEmbargos = listMotivosEmbargos;
    }

    public List<MotivosEmbargos> getFiltrarMotivosEmbargos() {
        return filtrarMotivosEmbargos;
    }

    public void setFiltrarMotivosEmbargos(List<MotivosEmbargos> filtrarMotivosEmbargos) {
        this.filtrarMotivosEmbargos = filtrarMotivosEmbargos;
    }

    public MotivosEmbargos getNuevoMotivoEmbargo() {
        return nuevoMotivoEmbargo;
    }

    public void setNuevoMotivoEmbargo(MotivosEmbargos nuevoMotivoEmbargo) {
        this.nuevoMotivoEmbargo = nuevoMotivoEmbargo;
    }

    public MotivosEmbargos getDuplicarMotivoEmbargo() {
        return duplicarMotivoEmbargo;
    }

    public void setDuplicarMotivoEmbargo(MotivosEmbargos duplicarMotivoEmbargo) {
        this.duplicarMotivoEmbargo = duplicarMotivoEmbargo;
    }

    public MotivosEmbargos getEditarMotivoEmbargo() {
        return editarMotivoEmbargo;
    }

    public void setEditarMotivoEmbargo(MotivosEmbargos editarMotivoEmbargo) {
        this.editarMotivoEmbargo = editarMotivoEmbargo;
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

    public MotivosEmbargos getMotivoEmbargoSeleccionado() {
        return motivoEmbargoSeleccionado;
    }

    public void setMotivoEmbargoSeleccionado(MotivosEmbargos motivoEmbargoSeleccionado) {
        this.motivoEmbargoSeleccionado = motivoEmbargoSeleccionado;
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
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosTipoReemplazo");
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
