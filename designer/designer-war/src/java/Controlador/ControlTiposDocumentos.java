/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.TiposDocumentos;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarTiposDocumentosInterface;
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

@ManagedBean
@SessionScoped
public class ControlTiposDocumentos implements Serializable {

    private static Logger log = Logger.getLogger(ControlTiposDocumentos.class);

    @EJB
    AdministrarTiposDocumentosInterface administrarTiposDocumentos;
    @EJB
    AdministrarRastrosInterface administrarRastros;
    private List<TiposDocumentos> listTiposDocumentos;
    private List<TiposDocumentos> filtrarTiposDocumentos;
    private List<TiposDocumentos> crearTiposDocumentos;
    private List<TiposDocumentos> modificarTiposDocumentos;
    private List<TiposDocumentos> borrarTiposDocumentos;
    private TiposDocumentos nuevoTiposDocumentos;
    private TiposDocumentos duplicarTiposDocumentos;
    private TiposDocumentos editarTiposDocumentos;
    private TiposDocumentos tiposDocumentosSeleccionado;
    //otros
    private int cualCelda, tipoLista, tipoActualizacion, k, bandera;
    private BigInteger l;
    private boolean aceptar, guardado;
    private Column codigo, descripcion;
    private int registrosBorrados;
    private String mensajeValidacion;
    private int tamano;
    private String infoRegistro;
    private String msgError;
    private String paginaAnterior = "nominaf";
    private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

    public ControlTiposDocumentos() {
        listTiposDocumentos = null;
        crearTiposDocumentos = new ArrayList<TiposDocumentos>();
        modificarTiposDocumentos = new ArrayList<TiposDocumentos>();
        borrarTiposDocumentos = new ArrayList<TiposDocumentos>();
        editarTiposDocumentos = new TiposDocumentos();
        nuevoTiposDocumentos = new TiposDocumentos();
        duplicarTiposDocumentos = new TiposDocumentos();
        guardado = true;
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
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarTiposDocumentos.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
            listTiposDocumentos = null;
            getListTiposDocumentos();
            if (listTiposDocumentos != null) {
                if (!listTiposDocumentos.isEmpty()) {
                    tiposDocumentosSeleccionado = listTiposDocumentos.get(0);
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
        String pagActual = "tipodocumento";
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

    public void cambiarIndice(TiposDocumentos td, int celda) {
        tiposDocumentosSeleccionado = td;
        cualCelda = celda;
        tiposDocumentosSeleccionado.getSecuencia();
        if (cualCelda == 0) {
            tiposDocumentosSeleccionado.getNombrecorto();
        } else if (cualCelda == 1) {
            tiposDocumentosSeleccionado.getNombrelargo();
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void cancelarModificacion() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 1) {
            //CERRAR FILTRADO
            codigo = (Column) c.getViewRoot().findComponent("form:datosTiposDocumentos:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposDocumentos:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTiposDocumentos");
            bandera = 0;
            filtrarTiposDocumentos = null;
            tipoLista = 0;
            tamano = 310;
        }

        borrarTiposDocumentos.clear();
        crearTiposDocumentos.clear();
        modificarTiposDocumentos.clear();
        tiposDocumentosSeleccionado = null;
        k = 0;
        listTiposDocumentos = null;
        guardado = true;
        getListTiposDocumentos();
        contarRegistros();
        RequestContext.getCurrentInstance().update("form:datosTiposDocumentos");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void salir() {
        limpiarListasValor();
        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            codigo = (Column) c.getViewRoot().findComponent("form:datosTiposDocumentos:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposDocumentos:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTiposDocumentos");
            bandera = 0;
            filtrarTiposDocumentos = null;
            tipoLista = 0;
            tamano = 310;
        }

        borrarTiposDocumentos.clear();
        crearTiposDocumentos.clear();
        modificarTiposDocumentos.clear();
        tiposDocumentosSeleccionado = null;
        k = 0;
        listTiposDocumentos = null;
        guardado = true;
        getListTiposDocumentos();
        RequestContext context = RequestContext.getCurrentInstance();
        contarRegistros();
        RequestContext.getCurrentInstance().update("form:datosTiposDocumentos");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        navegar("atras");
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            tamano = 290;
            codigo = (Column) c.getViewRoot().findComponent("form:datosTiposDocumentos:codigo");
            codigo.setFilterStyle("width: 85% !important;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposDocumentos:descripcion");
            descripcion.setFilterStyle("width: 85% !important;");
            RequestContext.getCurrentInstance().update("form:datosTiposDocumentos");
            bandera = 1;
        } else if (bandera == 1) {
            tamano = 310;
            codigo = (Column) c.getViewRoot().findComponent("form:datosTiposDocumentos:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposDocumentos:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTiposDocumentos");
            bandera = 0;
            filtrarTiposDocumentos = null;
            tipoLista = 0;
        }
    }

    public void modificarTiposDocumentos(TiposDocumentos td) {
        tiposDocumentosSeleccionado = td;
        if (!crearTiposDocumentos.contains(tiposDocumentosSeleccionado)) {
            if (modificarTiposDocumentos.isEmpty()) {
                modificarTiposDocumentos.add(tiposDocumentosSeleccionado);
            } else if (!modificarTiposDocumentos.contains(tiposDocumentosSeleccionado)) {
                modificarTiposDocumentos.add(tiposDocumentosSeleccionado);
            }
            guardado = false;

            RequestContext.getCurrentInstance().update("form:datosTiposDocumentos");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
    }

    public void borrandoTiposDocumentos() {

        if (tiposDocumentosSeleccionado != null) {
            if (!modificarTiposDocumentos.isEmpty() && modificarTiposDocumentos.contains(tiposDocumentosSeleccionado)) {
                int modIndex = modificarTiposDocumentos.indexOf(tiposDocumentosSeleccionado);
                modificarTiposDocumentos.remove(modIndex);
                borrarTiposDocumentos.add(tiposDocumentosSeleccionado);
            } else if (!crearTiposDocumentos.isEmpty() && crearTiposDocumentos.contains(tiposDocumentosSeleccionado)) {
                int crearIndex = crearTiposDocumentos.indexOf(tiposDocumentosSeleccionado);
                crearTiposDocumentos.remove(crearIndex);
            } else {
                borrarTiposDocumentos.add(tiposDocumentosSeleccionado);
            }
            listTiposDocumentos.remove(tiposDocumentosSeleccionado);
            if (tipoLista == 1) {
                filtrarTiposDocumentos.remove(tiposDocumentosSeleccionado);
            }
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:datosTiposDocumentos");
            tiposDocumentosSeleccionado = null;
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void verificarBorrado() {
        BigInteger contarCodeudoresTipoDocumento;
        BigInteger contarPersonasTipoDocumento;
        try {
            if (tipoLista == 0) {
                contarCodeudoresTipoDocumento = administrarTiposDocumentos.contarCodeudoresTipoDocumento(tiposDocumentosSeleccionado.getSecuencia());
                contarPersonasTipoDocumento = administrarTiposDocumentos.contarPersonasTipoDocumento(tiposDocumentosSeleccionado.getSecuencia());
            } else {
                contarCodeudoresTipoDocumento = administrarTiposDocumentos.contarCodeudoresTipoDocumento(tiposDocumentosSeleccionado.getSecuencia());
                contarPersonasTipoDocumento = administrarTiposDocumentos.contarPersonasTipoDocumento(tiposDocumentosSeleccionado.getSecuencia());
            }
            if (contarCodeudoresTipoDocumento.equals(new BigInteger("0")) && contarPersonasTipoDocumento.equals(new BigInteger("0"))) {
                borrandoTiposDocumentos();
            } else {
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:validacionBorrar");
                RequestContext.getCurrentInstance().execute("PF('validacionBorrar').show()");
                contarCodeudoresTipoDocumento = new BigInteger("-1");
                contarPersonasTipoDocumento = new BigInteger("-1");
            }
        } catch (Exception e) {
            log.error("ERROR ControlTiposDocumentos verificarBorrado ERROR  ", e);
        }
    }

    public void revisarDialogoGuardar() {
        if (!borrarTiposDocumentos.isEmpty() || !crearTiposDocumentos.isEmpty() || !modificarTiposDocumentos.isEmpty()) {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:confirmarGuardar");
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
        }
    }

    public void guardarTiposDocumentos() {
        try {
            RequestContext context = RequestContext.getCurrentInstance();
            if (guardado == false) {
                msgError = "";
                if (!borrarTiposDocumentos.isEmpty()) {
                    for (int i = 0; i < borrarTiposDocumentos.size(); i++) {
                        msgError = administrarTiposDocumentos.borrarTiposDocumentos(borrarTiposDocumentos.get(i));
                    }
                    registrosBorrados = borrarTiposDocumentos.size();
                    RequestContext.getCurrentInstance().update("form:mostrarBorrados");
                    RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
                    borrarTiposDocumentos.clear();
                }
                if (!modificarTiposDocumentos.isEmpty()) {
                    for (int i = 0; i < modificarTiposDocumentos.size(); i++) {
                        msgError = administrarTiposDocumentos.modificarTiposDocumentos(modificarTiposDocumentos.get(i));
                    }
                    modificarTiposDocumentos.clear();
                }
                if (!crearTiposDocumentos.isEmpty()) {
                    for (int i = 0; i < crearTiposDocumentos.size(); i++) {
                        msgError = administrarTiposDocumentos.crearTiposDocumentos(crearTiposDocumentos.get(i));
                    }
                    crearTiposDocumentos.clear();
                }
                if (msgError.equals("EXITO")) {
                    listTiposDocumentos = null;
                    getListTiposDocumentos();
                    RequestContext.getCurrentInstance().update("form:datosTiposDocumentos");
                    FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                    RequestContext.getCurrentInstance().update("form:growl");
                    k = 0;
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
        if (tiposDocumentosSeleccionado != null) {
            editarTiposDocumentos = tiposDocumentosSeleccionado;
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

    public void agregarNuevoTiposDocumentos() {
        int contador = 0;
        int duplicados = 0;

        mensajeValidacion = " ";
        if (nuevoTiposDocumentos.getNombrecorto() != null) {
            for (int i = 0; i < listTiposDocumentos.size(); i++) {
                if (nuevoTiposDocumentos.getNombrecorto().equals(listTiposDocumentos.get(i).getNombrecorto())) {
                    duplicados++;
                }
            }
            if (duplicados == 0) {
                contador++;
            } else {
                mensajeValidacion = "Existe un registro con el nombre corto ingresado. Por favor ingrese otro nombre corto";
            }
        } else {
            contador++;
        }

        if (nuevoTiposDocumentos.getNombrelargo() == null || nuevoTiposDocumentos.getNombrelargo().equals(" ")) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
        } else {
            contador++;
        }
        if (nuevoTiposDocumentos.getNombrecorto() == null || nuevoTiposDocumentos.getNombrecorto().equals(" ")) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
        } else {
            contador++;
        }

        if (contador == 3) {
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                tamano = 310;
                codigo = (Column) c.getViewRoot().findComponent("form:datosTiposDocumentos:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposDocumentos:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosTiposDocumentos");
                bandera = 0;
                filtrarTiposDocumentos = null;
                tipoLista = 0;
            }
            k++;
            l = BigInteger.valueOf(k);
            nuevoTiposDocumentos.setSecuencia(l);
            crearTiposDocumentos.add(nuevoTiposDocumentos);
            listTiposDocumentos.add(0, nuevoTiposDocumentos);
            tiposDocumentosSeleccionado = nuevoTiposDocumentos;
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:datosTiposDocumentos");
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().execute("PF('nuevoRegistroTiposDocumentos').hide()");
            nuevoTiposDocumentos = new TiposDocumentos();

        } else {
            RequestContext.getCurrentInstance().update("form:validacionNuevaCentroCosto");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevaCentroCosto').show()");
            contador = 0;
        }
    }

    public void limpiarNuevoTiposDocumentos() {
        nuevoTiposDocumentos = new TiposDocumentos();
    }

    //------------------------------------------------------------------------------
    public void duplicandoTiposDocumentos() {
        if (tiposDocumentosSeleccionado != null) {
            duplicarTiposDocumentos = new TiposDocumentos();
            k++;
            l = BigInteger.valueOf(k);
            duplicarTiposDocumentos.setSecuencia(l);
            duplicarTiposDocumentos.setNombrecorto(tiposDocumentosSeleccionado.getNombrecorto());
            duplicarTiposDocumentos.setNombrelargo(tiposDocumentosSeleccionado.getNombrelargo());
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTE");
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroTiposDocumentos').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void confirmarDuplicar() {
        int contador = 0;
        mensajeValidacion = " ";
        int duplicados = 0;
        if (duplicarTiposDocumentos.getNombrecorto() != null) {
            for (int i = 0; i < listTiposDocumentos.size(); i++) {
                if (duplicarTiposDocumentos.getNombrecorto().equals(listTiposDocumentos.get(i).getNombrecorto())) {
                    duplicados++;
                }
            }
            if (duplicados == 0) {
                contador++;
            } else {
                mensajeValidacion = "Existe un registro con el nombre corto ingresado. Por favor ingrese otro nombre corto";
            }
        } else {
            contador++;
        }

        if (duplicarTiposDocumentos.getNombrelargo() == null || duplicarTiposDocumentos.getNombrelargo().equals(" ")) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
        } else {
            contador++;
        }

        if (duplicarTiposDocumentos.getNombrecorto() == null || duplicarTiposDocumentos.getNombrecorto().equals(" ")) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
        } else {
            contador++;
        }

        if (contador == 3) {
            k++;
            l = BigInteger.valueOf(k);
            duplicarTiposDocumentos.setSecuencia(l);
            crearTiposDocumentos.add(duplicarTiposDocumentos);
            listTiposDocumentos.add(0, duplicarTiposDocumentos);
            tiposDocumentosSeleccionado = duplicarTiposDocumentos;
            RequestContext.getCurrentInstance().update("form:datosTiposDocumentos");
            guardado = false;
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                tamano = 310;
                codigo = (Column) c.getViewRoot().findComponent("form:datosTiposDocumentos:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposDocumentos:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosTiposDocumentos");
                bandera = 0;
                filtrarTiposDocumentos = null;
                tipoLista = 0;
            }
            duplicarTiposDocumentos = new TiposDocumentos();
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroTiposDocumentos').hide()");

        } else {
            contador = 0;
            RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
            RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
        }
    }

    public void limpiarDuplicarTiposDocumentos() {
        duplicarTiposDocumentos = new TiposDocumentos();
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTiposDocumentosExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "TIPOSDOCUMENTOS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTiposDocumentosExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "TIPOSDOCUMENTOS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tiposDocumentosSeleccionado != null) {
            int resultado = administrarRastros.obtenerTabla(tiposDocumentosSeleccionado.getSecuencia(), "TIPOSDOCUMENTOS"); //En ENCARGATURAS lo cambia por el Descripcion de su tabla
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
        } else if (administrarRastros.verificarHistoricosTabla("TIPOSDOCUMENTOS")) { // igual acá
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
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

    ///////////////SETS Y GETS ////////////////////
    public List<TiposDocumentos> getListTiposDocumentos() {
        if (listTiposDocumentos == null) {
            listTiposDocumentos = administrarTiposDocumentos.consultarTiposDocumentos();
        }
        return listTiposDocumentos;
    }

    public void setListTiposDocumentos(List<TiposDocumentos> listTiposDocumentos) {
        this.listTiposDocumentos = listTiposDocumentos;
    }

    public List<TiposDocumentos> getFiltrarTiposDocumentos() {
        return filtrarTiposDocumentos;
    }

    public void setFiltrarTiposDocumentos(List<TiposDocumentos> filtrarTiposDocumentos) {
        this.filtrarTiposDocumentos = filtrarTiposDocumentos;
    }

    public TiposDocumentos getNuevoTiposDocumentos() {
        return nuevoTiposDocumentos;
    }

    public void setNuevoTiposDocumentos(TiposDocumentos nuevoTiposDocumentos) {
        this.nuevoTiposDocumentos = nuevoTiposDocumentos;
    }

    public TiposDocumentos getDuplicarTiposDocumentos() {
        return duplicarTiposDocumentos;
    }

    public void setDuplicarTiposDocumentos(TiposDocumentos duplicarTiposDocumentos) {
        this.duplicarTiposDocumentos = duplicarTiposDocumentos;
    }

    public TiposDocumentos getEditarTiposDocumentos() {
        return editarTiposDocumentos;
    }

    public void setEditarTiposDocumentos(TiposDocumentos editarTiposDocumentos) {
        this.editarTiposDocumentos = editarTiposDocumentos;
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

    public TiposDocumentos getTiposDocumentosSeleccionado() {
        return tiposDocumentosSeleccionado;
    }

    public void setTiposDocumentosSeleccionado(TiposDocumentos tiposDocumentosSeleccionado) {
        this.tiposDocumentosSeleccionado = tiposDocumentosSeleccionado;
    }

    public String getInfoRegistro() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosTiposDocumentos");
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

    public boolean isAceptar() {
        return aceptar;
    }

    public void setAceptar(boolean aceptar) {
        this.aceptar = aceptar;
    }
}
