/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.TiposIndices;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarTiposIndicesInterface;
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
 * @author user
 */
@ManagedBean
@SessionScoped
public class ControlTiposIndices implements Serializable {

    private static Logger log = Logger.getLogger(ControlTiposIndices.class);

    @EJB
    AdministrarTiposIndicesInterface administrarTiposIndices;
    @EJB
    AdministrarRastrosInterface administrarRastros;

    private List<TiposIndices> listTiposIndices;
    private List<TiposIndices> filtrarTiposIndices;
    private List<TiposIndices> crearTiposIndices;
    private List<TiposIndices> modificarTiposIndices;
    private List<TiposIndices> borrarTiposIndices;
    private TiposIndices nuevoTiposIndices;
    private TiposIndices duplicarTiposIndices;
    private TiposIndices editarTiposIndices;
    private TiposIndices tipoIndiceSeleccionado;
    //otros
    private int cualCelda, tipoLista, tipoActualizacion, k, bandera;
    private BigInteger l;
    private boolean aceptar, guardado;
    //RASTRO
    private Column codigo, descripcion;
    //borrado
    private int registrosBorrados;
    private String mensajeValidacion;
    private int tamano;
    private String paginaAnterior = "nominaf";
    private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();
    private String msgError;
    private String infoRegistro;

    public ControlTiposIndices() {
        listTiposIndices = null;
        crearTiposIndices = new ArrayList<TiposIndices>();
        modificarTiposIndices = new ArrayList<TiposIndices>();
        borrarTiposIndices = new ArrayList<TiposIndices>();
        editarTiposIndices = new TiposIndices();
        nuevoTiposIndices = new TiposIndices();
        duplicarTiposIndices = new TiposIndices();
        guardado = true;
        tamano = 330;
        mapParametros.put("paginaAnterior", paginaAnterior);
    }

    public void limpiarListasValor() {

    }

    @PreDestroy
    public void destruyendoce() {
        log.info(this.getClass().getName() + ".destruyendoce() @Destroy");
    }

    @PostConstruct
    public void inicializarAdministrador() {
        log.info(this.getClass().getName() + ".inicializarAdministrador() @PostConstruct");
        try {
            log.info("ControlTiposIndices PostConstruct ");
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarTiposIndices.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
            listTiposIndices = null;
            getListTiposIndices();
            if (listTiposIndices != null) {
                if (!listTiposIndices.isEmpty()) {
                    tipoIndiceSeleccionado = listTiposIndices.get(0);
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
        String pagActual = "tipoindice";
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

    public void cambiarIndice(TiposIndices tipoI, int celda) {
        tipoIndiceSeleccionado = tipoI;
        cualCelda = celda;
        tipoIndiceSeleccionado.getSecuencia();
        if (cualCelda == 0) {
            tipoIndiceSeleccionado.getCodigo();
        } else if (cualCelda == 1) {
            tipoIndiceSeleccionado.getDescripcion();
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void cancelarModificacion() {
        if (bandera == 1) {
            //CERRAR FILTRADO
            FacesContext c = FacesContext.getCurrentInstance();
            codigo = (Column) c.getViewRoot().findComponent("form:datosTiposIndices:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposIndices:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTiposIndices");
            bandera = 0;
            filtrarTiposIndices = null;
            tipoLista = 0;
            tamano = 330;
        }

        borrarTiposIndices.clear();
        crearTiposIndices.clear();
        modificarTiposIndices.clear();
        tipoIndiceSeleccionado = null;
        k = 0;
        listTiposIndices = null;
        guardado = true;
        getListTiposIndices();
        contarRegistros();
        RequestContext.getCurrentInstance().update("form:datosTiposIndices");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void contarRegistros() {
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
    }

    public void salir() {
        limpiarListasValor();
        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            codigo = (Column) c.getViewRoot().findComponent("form:datosTiposIndices:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposIndices:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTiposIndices");
            bandera = 0;
            filtrarTiposIndices = null;
            tipoLista = 0;
            tamano = 330;
        }
        borrarTiposIndices.clear();
        crearTiposIndices.clear();
        modificarTiposIndices.clear();
        tipoIndiceSeleccionado = null;
        k = 0;
        listTiposIndices = null;
        guardado = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosTiposIndices");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        navegar("atras");
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            tamano = 310;
            codigo = (Column) c.getViewRoot().findComponent("form:datosTiposIndices:codigo");
            codigo.setFilterStyle("width: 85% !important;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposIndices:descripcion");
            descripcion.setFilterStyle("width: 85% !important;");
            RequestContext.getCurrentInstance().update("form:datosTiposIndices");
            bandera = 1;
        } else if (bandera == 1) {
            tamano = 330;
            codigo = (Column) c.getViewRoot().findComponent("form:datosTiposIndices:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposIndices:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTiposIndices");
            bandera = 0;
            filtrarTiposIndices = null;
            tipoLista = 0;
        }
    }

    public void modificarTiposIndices(TiposIndices tipoI) {
        tipoIndiceSeleccionado = tipoI;
        if (!crearTiposIndices.contains(tipoIndiceSeleccionado)) {
            if (modificarTiposIndices.isEmpty()) {
                modificarTiposIndices.add(tipoIndiceSeleccionado);
            } else if (!modificarTiposIndices.contains(tipoIndiceSeleccionado)) {
                modificarTiposIndices.add(tipoIndiceSeleccionado);
            }
        }
        guardado = false;
        RequestContext.getCurrentInstance().update("form:datosTiposIndices");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void borrandoTiposIndices() {
        if (tipoIndiceSeleccionado != null) {
            if (!modificarTiposIndices.isEmpty() && modificarTiposIndices.contains(tipoIndiceSeleccionado)) {
                int modIndex = modificarTiposIndices.indexOf(tipoIndiceSeleccionado);
                modificarTiposIndices.remove(modIndex);
                borrarTiposIndices.add(tipoIndiceSeleccionado);
            } else if (!crearTiposIndices.isEmpty() && crearTiposIndices.contains(tipoIndiceSeleccionado)) {
                int crearIndex = crearTiposIndices.indexOf(tipoIndiceSeleccionado);
                crearTiposIndices.remove(crearIndex);
            } else {
                borrarTiposIndices.add(tipoIndiceSeleccionado);
            }
            listTiposIndices.remove(tipoIndiceSeleccionado);
            if (tipoLista == 1) {
                filtrarTiposIndices.remove(tipoIndiceSeleccionado);

            }
            contarRegistros();
            tipoIndiceSeleccionado = null;
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosTiposIndices");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistros').show()");
        }

    }

    public void verificarBorrado() {
        BigInteger contarIndicesTipoIndice;
        try {
            contarIndicesTipoIndice = administrarTiposIndices.contarIndicesTipoIndice(tipoIndiceSeleccionado.getSecuencia());
            if (contarIndicesTipoIndice.equals(new BigInteger("0"))) {
                borrandoTiposIndices();
            } else {
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:validacionBorrar");
                RequestContext.getCurrentInstance().execute("PF('validacionBorrar').show()");
                contarIndicesTipoIndice = new BigInteger("-1");
            }
        } catch (Exception e) {
            log.error("ERROR ControlTiposIndices verificarBorrado ERROR  ", e);
        }
    }

    public void revisarDialogoGuardar() {
        if (!borrarTiposIndices.isEmpty() || !crearTiposIndices.isEmpty() || !modificarTiposIndices.isEmpty()) {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:confirmarGuardar");
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
        }
    }

    public void guardarTiposIndices() {
        try {
            RequestContext context = RequestContext.getCurrentInstance();
            if (guardado == false) {
                msgError = "";
                if (!borrarTiposIndices.isEmpty()) {
                    for (int i = 0; i < borrarTiposIndices.size(); i++) {
                        msgError = administrarTiposIndices.borrarTiposIndices(borrarTiposIndices.get(i));
                    }
                    registrosBorrados = borrarTiposIndices.size();
                    RequestContext.getCurrentInstance().update("form:mostrarBorrados");
                    RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
                    borrarTiposIndices.clear();
                }
                if (!modificarTiposIndices.isEmpty()) {
                    for (int i = 0; i < modificarTiposIndices.size(); i++) {
                        msgError = administrarTiposIndices.modificarTiposIndices(modificarTiposIndices.get(i));
                    }
                    modificarTiposIndices.clear();
                }
                if (!crearTiposIndices.isEmpty()) {
                    for (int i = 0; i < crearTiposIndices.size(); i++) {
                        msgError = administrarTiposIndices.crearTiposIndices(crearTiposIndices.get(i));
                    }
                    crearTiposIndices.clear();
                }

                if (msgError.equals("EXITO")) {
                    listTiposIndices = null;
                    RequestContext.getCurrentInstance().update("form:datosTiposIndices");
                    k = 0;
                    guardado = true;
                    FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                    RequestContext.getCurrentInstance().update("form:growl");

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
        if (tipoIndiceSeleccionado != null) {
            editarTiposIndices = tipoIndiceSeleccionado;
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

    public void agregarNuevoTiposIndices() {
        int contador = 0;
        int duplicados = 0;
        Integer a = 0;
        a = null;
        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();
        if (nuevoTiposIndices.getCodigo() == a) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
        } else {

            for (int x = 0; x < listTiposIndices.size(); x++) {
                if (listTiposIndices.get(x).getCodigo() == nuevoTiposIndices.getCodigo()) {
                    duplicados++;
                }
            }
            if (duplicados > 0) {
                mensajeValidacion = "Existe un registro con el código ingresado. Por favor ingrese un código válido";
            } else {
                contador++;
            }
        }
        if (nuevoTiposIndices.getDescripcion() == null) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
        } else if (nuevoTiposIndices.getDescripcion().isEmpty()) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
        } else {
            contador++;
        }
        if (contador == 2) {
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                codigo = (Column) c.getViewRoot().findComponent("form:datosTiposIndices:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposIndices:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosTiposIndices");
                bandera = 0;
                filtrarTiposIndices = null;
                tipoLista = 0;
            }
            k++;
            l = BigInteger.valueOf(k);
            nuevoTiposIndices.setSecuencia(l);
            crearTiposIndices.add(nuevoTiposIndices);
            listTiposIndices.add(nuevoTiposIndices);
            tipoIndiceSeleccionado = nuevoTiposIndices;
            RequestContext.getCurrentInstance().update("form:datosTiposIndices");
            contarRegistros();
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().execute("PF('nuevoRegistroTiposIndices').hide()");
            nuevoTiposIndices = new TiposIndices();
        } else {
            RequestContext.getCurrentInstance().update("form:validacionNuevaCentroCosto");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevaCentroCosto').show()");
            contador = 0;
        }
    }

    public void limpiarNuevoTiposIndices() {
        nuevoTiposIndices = new TiposIndices();
    }

    //------------------------------------------------------------------------------
    public void duplicandoTiposIndices() {
        if (tipoIndiceSeleccionado != null) {
            duplicarTiposIndices = new TiposIndices();
            k++;
            l = BigInteger.valueOf(k);
            duplicarTiposIndices.setSecuencia(l);
            duplicarTiposIndices.setCodigo(tipoIndiceSeleccionado.getCodigo());
            duplicarTiposIndices.setDescripcion(tipoIndiceSeleccionado.getDescripcion());
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTE");
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroTiposIndices').show()");
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
        if (duplicarTiposIndices.getCodigo() == a) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
        } else {
            for (int x = 0; x < listTiposIndices.size(); x++) {
                if (listTiposIndices.get(x).getCodigo() == duplicarTiposIndices.getCodigo()) {
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
        if (duplicarTiposIndices.getDescripcion() == null) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
        } else if (duplicarTiposIndices.getDescripcion().isEmpty()) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
        } else {
            contador++;
        }

        if (contador == 2) {
            listTiposIndices.add(duplicarTiposIndices);
            crearTiposIndices.add(duplicarTiposIndices);
            RequestContext.getCurrentInstance().update("form:datosTiposIndices");
            tipoIndiceSeleccionado = duplicarTiposIndices;
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            contarRegistros();
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                codigo = (Column) c.getViewRoot().findComponent("form:datosTiposIndices:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposIndices:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosTiposIndices");
                bandera = 0;
                filtrarTiposIndices = null;
                tipoLista = 0;
            }
            duplicarTiposIndices = new TiposIndices();
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroTiposIndices').hide()");

        } else {
            contador = 0;
            RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
            RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
        }
    }

    public void limpiarDuplicarTiposIndices() {
        duplicarTiposIndices = new TiposIndices();
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTiposIndicesExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "TIPOSINDICES", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTiposIndicesExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "TIPOSINDICES", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoIndiceSeleccionado != null) {
            int resultado = administrarRastros.obtenerTabla(tipoIndiceSeleccionado.getSecuencia(), "TIPOSINDICES"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
        } else if (administrarRastros.verificarHistoricosTabla("TIPOSINDICES")) { // igual acá
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
    }

    ////////////////SETS Y GETS////////////////////
    public List<TiposIndices> getListTiposIndices() {
        if (listTiposIndices == null) {
            listTiposIndices = administrarTiposIndices.consultarTiposIndices();
        }
        return listTiposIndices;
    }

    public void setListTiposIndices(List<TiposIndices> listTiposIndices) {
        this.listTiposIndices = listTiposIndices;
    }

    public List<TiposIndices> getFiltrarTiposIndices() {
        return filtrarTiposIndices;
    }

    public void setFiltrarTiposIndices(List<TiposIndices> filtrarTiposIndices) {
        this.filtrarTiposIndices = filtrarTiposIndices;
    }

    public TiposIndices getNuevoTiposIndices() {
        return nuevoTiposIndices;
    }

    public void setNuevoTiposIndices(TiposIndices nuevoTiposIndices) {
        this.nuevoTiposIndices = nuevoTiposIndices;
    }

    public TiposIndices getDuplicarTiposIndices() {
        return duplicarTiposIndices;
    }

    public void setDuplicarTiposIndices(TiposIndices duplicarTiposIndices) {
        this.duplicarTiposIndices = duplicarTiposIndices;
    }

    public TiposIndices getEditarTiposIndices() {
        return editarTiposIndices;
    }

    public void setEditarTiposIndices(TiposIndices editarTiposIndices) {
        this.editarTiposIndices = editarTiposIndices;
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

    public boolean isGuardado() {
        return guardado;
    }

    public void setGuardado(boolean guardado) {
        this.guardado = guardado;
    }

    public int getTamano() {
        return tamano;
    }

    public void setTamano(int tamano) {
        this.tamano = tamano;
    }

    public TiposIndices getTipoIndiceSeleccionado() {
        return tipoIndiceSeleccionado;
    }

    public void setTipoIndiceSeleccionado(TiposIndices tipoIndiceSeleccionado) {
        this.tipoIndiceSeleccionado = tipoIndiceSeleccionado;
    }

    public String getInfoRegistro() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosTiposIndices");
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
