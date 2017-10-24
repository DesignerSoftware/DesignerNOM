/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.ClasesPensiones;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarClasesPensionesInterface;
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
import java.util.LinkedHashMap;
import java.util.Map;
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
public class ControlClasesPensiones implements Serializable {

    private static Logger log = Logger.getLogger(ControlClasesPensiones.class);

    @EJB
    AdministrarClasesPensionesInterface administrarClasesPensiones;
    @EJB
    AdministrarRastrosInterface administrarRastros;

    private List<ClasesPensiones> listClasesPensiones;
    private List<ClasesPensiones> filtrarClasesPensiones;
    private List<ClasesPensiones> crearClasesPensiones;
    private List<ClasesPensiones> modificarClasesPensiones;
    private List<ClasesPensiones> borrarClasesPensiones;
    private ClasesPensiones nuevoClasesPensiones;
    private ClasesPensiones duplicarClasesPensiones;
    private ClasesPensiones editarClasesPensiones;
    private ClasesPensiones clasesPensionesSeleccionado;
    private int cualCelda, tipoLista, tipoActualizacion, k, bandera;
    private BigInteger l;
    private boolean aceptar, guardado;
    private Column codigo, descripcion;
    private int registrosBorrados;
    private String mensajeValidacion;
    private int tamano;
    private Integer backUpCodigo;
    private String backUpDescripcion;
    private String infoRegistro;
    private String msgError;

    public ControlClasesPensiones() {
        listClasesPensiones = null;
        crearClasesPensiones = new ArrayList<ClasesPensiones>();
        modificarClasesPensiones = new ArrayList<ClasesPensiones>();
        borrarClasesPensiones = new ArrayList<ClasesPensiones>();
        editarClasesPensiones = new ClasesPensiones();
        nuevoClasesPensiones = new ClasesPensiones();
        duplicarClasesPensiones = new ClasesPensiones();
        guardado = true;
        tamano = 335;
        mapParametros.put("paginaAnterior", paginaAnterior);
    }

    private String paginaAnterior = "nominaf";
    private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

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
            administrarClasesPensiones.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
            listClasesPensiones = null;
            getListClasesPensiones();
            if (listClasesPensiones != null) {
                clasesPensionesSeleccionado = listClasesPensiones.get(0);
            }
        } catch (Exception e) {
            log.error("Error postconstruct " + this.getClass().getName() + ":  ", e);
            log.error("Causa: " + e.getCause());
        }
    }

    public void recibirPaginaEntrante(String pagina) {
        paginaAnterior = pagina;
    }

    public void recibirParametros(Map<String, Object> map) {
        mapParametros = map;
        paginaAnterior = (String) mapParametros.get("paginaAnterior");
        //inicializarCosas(); Inicializar cosas de ser necesario
    }

    public void navegar(String pag) {
        FacesContext fc = FacesContext.getCurrentInstance();
        ControlListaNavegacion controlListaNavegacion = (ControlListaNavegacion) fc.getApplication().evaluateExpressionGet(fc, "#{controlListaNavegacion}", ControlListaNavegacion.class);
        String pagActual = "clasepension";
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

    public void cambiarIndice(ClasesPensiones claseP, int celda) {

        clasesPensionesSeleccionado = claseP;
        cualCelda = celda;
        clasesPensionesSeleccionado.getSecuencia();
        if (cualCelda == 0) {
            clasesPensionesSeleccionado.getCodigo();
        } else if (cualCelda == 1) {
            clasesPensionesSeleccionado.getDescripcion();
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void cancelarModificacion() {
        if (bandera == 1) {
            //CERRAR FILTRADO
            FacesContext c = FacesContext.getCurrentInstance();
            codigo = (Column) c.getViewRoot().findComponent("form:datosClasesPensiones:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosClasesPensiones:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosClasesPensiones");
            bandera = 0;
            filtrarClasesPensiones = null;
            tipoLista = 0;
            tamano = 335;
        }

        borrarClasesPensiones.clear();
        crearClasesPensiones.clear();
        modificarClasesPensiones.clear();
        clasesPensionesSeleccionado = null;
        k = 0;
        listClasesPensiones = null;
        guardado = true;
        getListClasesPensiones();
        RequestContext context = RequestContext.getCurrentInstance();
        if (listClasesPensiones == null || listClasesPensiones.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listClasesPensiones.size();
        }
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
        RequestContext.getCurrentInstance().update("form:datosClasesPensiones");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void salir() {
        limpiarListasValor();
        if (bandera == 1) {
            //CERRAR FILTRADO
            FacesContext c = FacesContext.getCurrentInstance();
            codigo = (Column) c.getViewRoot().findComponent("form:datosClasesPensiones:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosClasesPensiones:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosClasesPensiones");
            bandera = 0;
            filtrarClasesPensiones = null;
            tipoLista = 0;
            tamano = 335;
        }

        borrarClasesPensiones.clear();
        crearClasesPensiones.clear();
        modificarClasesPensiones.clear();
        clasesPensionesSeleccionado = null;
        k = 0;
        listClasesPensiones = null;
        guardado = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosClasesPensiones");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        navegar("atras");
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            tamano = 315;
            codigo = (Column) c.getViewRoot().findComponent("form:datosClasesPensiones:codigo");
            codigo.setFilterStyle("width: 85% !important;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosClasesPensiones:descripcion");
            descripcion.setFilterStyle("width: 85% !important;");
            RequestContext.getCurrentInstance().update("form:datosClasesPensiones");
            bandera = 1;
        } else if (bandera == 1) {
            tamano = 335;
            codigo = (Column) c.getViewRoot().findComponent("form:datosClasesPensiones:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosClasesPensiones:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosClasesPensiones");
            bandera = 0;
            filtrarClasesPensiones = null;
            tipoLista = 0;
        }
    }

    public void modificarClasesPensiones(ClasesPensiones claseP) {
        clasesPensionesSeleccionado = claseP;
        if (!crearClasesPensiones.contains(clasesPensionesSeleccionado)) {
            if (modificarClasesPensiones.isEmpty()) {
                modificarClasesPensiones.add(clasesPensionesSeleccionado);
            } else if (!modificarClasesPensiones.contains(clasesPensionesSeleccionado)) {
                modificarClasesPensiones.add(clasesPensionesSeleccionado);
            }
            guardado = false;
            RequestContext.getCurrentInstance().update("form:datosClasesPensiones");
        }
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void borrandoClasesPensiones() {

        if (clasesPensionesSeleccionado != null) {
            if (!modificarClasesPensiones.isEmpty() && modificarClasesPensiones.contains(clasesPensionesSeleccionado)) {
                int modIndex = modificarClasesPensiones.indexOf(clasesPensionesSeleccionado);
                modificarClasesPensiones.remove(modIndex);
                borrarClasesPensiones.add(clasesPensionesSeleccionado);
            } else if (!crearClasesPensiones.isEmpty() && crearClasesPensiones.contains(clasesPensionesSeleccionado)) {
                int crearIndex = crearClasesPensiones.indexOf(clasesPensionesSeleccionado);
                crearClasesPensiones.remove(crearIndex);
            } else {
                borrarClasesPensiones.add(clasesPensionesSeleccionado);
            }
            listClasesPensiones.remove(clasesPensionesSeleccionado);
            if (tipoLista == 1) {
                filtrarClasesPensiones.remove(clasesPensionesSeleccionado);

            }
            clasesPensionesSeleccionado = null;
            RequestContext context = RequestContext.getCurrentInstance();
            contarRegistros();
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosClasesPensiones");
        }

    }

    public void verificarBorrado() {
        BigInteger contarRetiradosClasePension;
        try {
            contarRetiradosClasePension = administrarClasesPensiones.contarRetiradosClasePension(clasesPensionesSeleccionado.getSecuencia());
            if (contarRetiradosClasePension.equals(new BigInteger("0"))) {
                borrandoClasesPensiones();
            } else {
                RequestContext.getCurrentInstance().update("form:validacionBorrar");
                RequestContext.getCurrentInstance().execute("PF('validacionBorrar').show()");
                contarRetiradosClasePension = new BigInteger("-1");
            }
        } catch (Exception e) {
            log.error("ERROR ControlClasesPensiones verificarBorrado ERROR  ", e);
        }
    }

    public void revisarDialogoGuardar() {

        if (!borrarClasesPensiones.isEmpty() || !crearClasesPensiones.isEmpty() || !modificarClasesPensiones.isEmpty()) {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:confirmarGuardar");
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
        }

    }

    public void guardarClasesPensiones() {
        RequestContext context = RequestContext.getCurrentInstance();
        try {

            if (guardado == false) {
                msgError = "";
                if (!borrarClasesPensiones.isEmpty()) {
                    for (int i = 0; i < borrarClasesPensiones.size(); i++) {
                        msgError = administrarClasesPensiones.borrarClasesPensiones(borrarClasesPensiones.get(i));
                    }
                    registrosBorrados = borrarClasesPensiones.size();
                    RequestContext.getCurrentInstance().update("form:mostrarBorrados");
                    RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
                    borrarClasesPensiones.clear();
                }
                if (!modificarClasesPensiones.isEmpty()) {
                    for (int i = 0; i < modificarClasesPensiones.size(); i++) {
                        msgError = administrarClasesPensiones.modificarClasesPensiones(modificarClasesPensiones.get(i));
                    }
                    modificarClasesPensiones.clear();
                }
                if (!crearClasesPensiones.isEmpty()) {
                    for (int i = 0; i < crearClasesPensiones.size(); i++) {
                        msgError = administrarClasesPensiones.crearClasesPensiones(crearClasesPensiones.get(i));
                    }
                    crearClasesPensiones.clear();
                }
                if (msgError.equals("EXITO")) {

                    listClasesPensiones = null;
                    getListClasesPensiones();
                    k = 0;
                    FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                    RequestContext.getCurrentInstance().update("form:growl");
                    contarRegistros();
                    guardado = true;
                    clasesPensionesSeleccionado = null;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                    RequestContext.getCurrentInstance().update("form:datosClasesPensiones");
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
        if (clasesPensionesSeleccionado != null) {
            editarClasesPensiones = clasesPensionesSeleccionado;
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

    public void agregarNuevoClasesPensiones() {
        int contador = 0;
        int duplicados = 0;

        Integer a = 0;
        a = null;
        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();
        if (nuevoClasesPensiones.getCodigo() == a) {
            mensajeValidacion = " Los campos marcados con asterisco son obligatorios ";
        } else {
            for (int x = 0; x < listClasesPensiones.size(); x++) {
                if (listClasesPensiones.get(x).getCodigo().equals(nuevoClasesPensiones.getCodigo())) {
                    duplicados++;
                }
            }

            if (duplicados > 0) {
                mensajeValidacion = "El código ingresado ya está registrado. Por favor ingrese un código válido";
            } else {
                contador++;
            }
        }
        if (nuevoClasesPensiones.getDescripcion() == null) {
            mensajeValidacion = " Los campos marcados con asterisco son obligatorios ";

        } else if (nuevoClasesPensiones.getDescripcion().isEmpty()) {
            mensajeValidacion = " Los campos marcados con asterisco son obligatorios ";

        } else {
            contador++;
        }

        if (contador == 2) {
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                codigo = (Column) c.getViewRoot().findComponent("form:datosClasesPensiones:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosClasesPensiones:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosClasesPensiones");
                bandera = 0;
                filtrarClasesPensiones = null;
                tipoLista = 0;
                tamano = 335;
            }
            k++;
            l = BigInteger.valueOf(k);
            nuevoClasesPensiones.setSecuencia(l);
            crearClasesPensiones.add(nuevoClasesPensiones);
            listClasesPensiones.add(0, nuevoClasesPensiones);
            clasesPensionesSeleccionado = nuevoClasesPensiones;
            RequestContext.getCurrentInstance().update("form:datosClasesPensiones");
            contarRegistros();
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().execute("PF('nuevoRegistroClasesPensiones').hide()");
            nuevoClasesPensiones = new ClasesPensiones();
        } else {
            RequestContext.getCurrentInstance().update("form:validacionNuevaCentroCosto");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevaCentroCosto').show()");
            contador = 0;
        }
    }

    public void limpiarNuevoClasesPensiones() {
        nuevoClasesPensiones = new ClasesPensiones();
    }

    //------------------------------------------------------------------------------
    public void duplicandoClasesPensiones() {
        if (clasesPensionesSeleccionado != null) {
            duplicarClasesPensiones = new ClasesPensiones();
            k++;
            l = BigInteger.valueOf(k);
            duplicarClasesPensiones.setSecuencia(l);
            duplicarClasesPensiones.setCodigo(clasesPensionesSeleccionado.getCodigo());
            duplicarClasesPensiones.setDescripcion(clasesPensionesSeleccionado.getDescripcion());
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTE");
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroClasesPensiones').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistros;').show()");
        }
    }

    public void confirmarDuplicar() {
        int contador = 0;
        mensajeValidacion = " ";
        int duplicados = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        Integer a = 0;
        a = null;
        if (duplicarClasesPensiones.getCodigo() == a) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
        } else {
            for (int x = 0; x < listClasesPensiones.size(); x++) {
                if (listClasesPensiones.get(x).getCodigo().equals(duplicarClasesPensiones.getCodigo())) {
                    duplicados++;
                }
            }
            if (duplicados > 0) {
                mensajeValidacion = "El código ingresado ya está registrado. Por favor ingrese un código válido";
            } else {
                contador++;
                duplicados = 0;
            }
        }
        if (duplicarClasesPensiones.getDescripcion() == null) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";

        } else if (duplicarClasesPensiones.getDescripcion().isEmpty()) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";

        } else {
            contador++;
        }
        if (contador == 2) {
            listClasesPensiones.add(duplicarClasesPensiones);
            crearClasesPensiones.add(duplicarClasesPensiones);
            clasesPensionesSeleccionado = duplicarClasesPensiones;
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosClasesPensiones");
            contarRegistros();

            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                //CERRAR FILTRADO
                codigo = (Column) c.getViewRoot().findComponent("form:datosClasesPensiones:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosClasesPensiones:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosClasesPensiones");
                bandera = 0;
                filtrarClasesPensiones = null;
                tipoLista = 0;
                tamano = 335;
            }
            duplicarClasesPensiones = new ClasesPensiones();
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroClasesPensiones').hide()");

        } else {
            contador = 0;
            RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
            RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
        }
    }

    public void limpiarDuplicarClasesPensiones() {
        duplicarClasesPensiones = new ClasesPensiones();
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosClasesPensionesExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "CLASESPENSIONES", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosClasesPensionesExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "CLASESPENSIONES", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (clasesPensionesSeleccionado != null) {
            int resultado = administrarRastros.obtenerTabla(clasesPensionesSeleccionado.getSecuencia(), "CLASESPENSIONES"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
        } else if (administrarRastros.verificarHistoricosTabla("CLASESPENSIONES")) { // igual acá
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
    }

    public void contarRegistros() {
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
    }

    //*/*/*/*/*/*/*/*/*/*-/-*//-*/-*/*/*-*/-*/-*/*/*/*/*/---/*/*/*/*/-*/-*/-*/-*/-*/
    public List<ClasesPensiones> getListClasesPensiones() {
        if (listClasesPensiones == null) {
            listClasesPensiones = administrarClasesPensiones.consultarClasesPensiones();
        }
        return listClasesPensiones;
    }

    public void setListClasesPensiones(List<ClasesPensiones> listClasesPensiones) {
        this.listClasesPensiones = listClasesPensiones;
    }

    public List<ClasesPensiones> getFiltrarClasesPensiones() {
        return filtrarClasesPensiones;
    }

    public void setFiltrarClasesPensiones(List<ClasesPensiones> filtrarClasesPensiones) {
        this.filtrarClasesPensiones = filtrarClasesPensiones;
    }

    public ClasesPensiones getNuevoClasesPensiones() {
        return nuevoClasesPensiones;
    }

    public void setNuevoClasesPensiones(ClasesPensiones nuevoClasesPensiones) {
        this.nuevoClasesPensiones = nuevoClasesPensiones;
    }

    public ClasesPensiones getDuplicarClasesPensiones() {
        return duplicarClasesPensiones;
    }

    public void setDuplicarClasesPensiones(ClasesPensiones duplicarClasesPensiones) {
        this.duplicarClasesPensiones = duplicarClasesPensiones;
    }

    public ClasesPensiones getEditarClasesPensiones() {
        return editarClasesPensiones;
    }

    public void setEditarClasesPensiones(ClasesPensiones editarClasesPensiones) {
        this.editarClasesPensiones = editarClasesPensiones;
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

    public ClasesPensiones getClasesPensionesSeleccionado() {
        return clasesPensionesSeleccionado;
    }

    public void setClasesPensionesSeleccionado(ClasesPensiones clasesPensionesSeleccionado) {
        this.clasesPensionesSeleccionado = clasesPensionesSeleccionado;
    }

    public String getInfoRegistro() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosClasesPensiones");
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
