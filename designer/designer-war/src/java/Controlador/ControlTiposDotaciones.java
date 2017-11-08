/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import ControlNavegacion.ControlListaNavegacion;
import Entidades.TiposDotaciones;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarRastrosInterface;
import InterfaceAdministrar.AdministrarTiposDotacionesInterface;
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
@Named(value = "controlTiposDotaciones")
@SessionScoped
public class ControlTiposDotaciones implements Serializable {

    private static Logger log = Logger.getLogger(ControlTiposDotaciones.class);

    @EJB
    AdministrarTiposDotacionesInterface administrarTiposDotaciones;
    @EJB
    AdministrarRastrosInterface administrarRastros;

    private List<TiposDotaciones> listTiposDotaciones;
    private List<TiposDotaciones> filtrarTiposDotaciones;
    private List<TiposDotaciones> crearTiposDotaciones;
    private List<TiposDotaciones> modificarTiposDotaciones;
    private List<TiposDotaciones> borrarTiposDotaciones;
    private TiposDotaciones nuevoTiposDotaciones;
    private TiposDotaciones duplicarTiposDotaciones;
    private TiposDotaciones editarTiposDotaciones;
    private TiposDotaciones tipodotacionSeleccionado;
    private int cualCelda, tipoLista, k, bandera;
    private BigInteger l;
    private boolean aceptar, guardado;
    private Column codigo, descripcion;
    private int registrosBorrados;
    private String mensajeValidacion, msgError;
    private int tamano;
    private String paginaAnterior = "nominaf";
    private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

    private String infoRegistro;

    public ControlTiposDotaciones() {
        listTiposDotaciones = null;
        crearTiposDotaciones = new ArrayList<TiposDotaciones>();
        modificarTiposDotaciones = new ArrayList<TiposDotaciones>();
        borrarTiposDotaciones = new ArrayList<TiposDotaciones>();
        editarTiposDotaciones = new TiposDotaciones();
        nuevoTiposDotaciones = new TiposDotaciones();
        duplicarTiposDotaciones = new TiposDotaciones();
        guardado = true;
        tamano = 320;
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
            log.info("ControlTiposDotaciones PostConstruct ");
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarTiposDotaciones.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            log.error("Error postconstruct " + this.getClass().getName() + ":  ", e);
            log.error("Causa: " + e.getCause());
        }
    }

    public void recibirPaginaEntrante(String pagina) {
        paginaAnterior = pagina;
        listTiposDotaciones = null;
        getListTiposDotaciones();
        if (listTiposDotaciones != null) {
            if (!listTiposDotaciones.isEmpty()) {
                tipodotacionSeleccionado = listTiposDotaciones.get(0);
            }
        }

    }

    public void recibirParametros(Map<String, Object> map) {
        mapParametros = map;
        paginaAnterior = (String) mapParametros.get("paginaAnterior");
    }

    public void navegar(String pag) {
        FacesContext fc = FacesContext.getCurrentInstance();
        ControlListaNavegacion controlListaNavegacion = (ControlListaNavegacion) fc.getApplication().evaluateExpressionGet(fc, "#{controlListaNavegacion}", ControlListaNavegacion.class);
        String pagActual = "tipodotacion";
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

    public void cambiarIndice(TiposDotaciones tipodotacion, int celda) {
        tipodotacionSeleccionado = tipodotacion;
        cualCelda = celda;
        tipodotacionSeleccionado.getSecuencia();
        if (cualCelda == 0) {
            tipodotacionSeleccionado.getCodigo();
        } else if (cualCelda == 1) {
            tipodotacionSeleccionado.getNombre();
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void cancelarModificacion() {
        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            codigo = (Column) c.getViewRoot().findComponent("form:datosTiposDotaciones:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposDotaciones:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTiposDotaciones");
            bandera = 0;
            filtrarTiposDotaciones = null;
            tipoLista = 0;
            tamano = 320;
        }
        borrarTiposDotaciones.clear();
        crearTiposDotaciones.clear();
        modificarTiposDotaciones.clear();
        tipodotacionSeleccionado = null;
        k = 0;
        listTiposDotaciones = null;
        guardado = true;
        getListTiposDotaciones();
        contarRegistros();
        RequestContext.getCurrentInstance().update("form:datosTiposDotaciones");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void salir() {
        limpiarListasValor();
        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            codigo = (Column) c.getViewRoot().findComponent("form:datosTiposDotaciones:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposDotaciones:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTiposDotaciones");
            bandera = 0;
            filtrarTiposDotaciones = null;
            tipoLista = 0;
            tamano = 320;
        }
        borrarTiposDotaciones.clear();
        crearTiposDotaciones.clear();
        modificarTiposDotaciones.clear();
        tipodotacionSeleccionado = null;
        k = 0;
        listTiposDotaciones = null;
        guardado = true;
        RequestContext.getCurrentInstance().update("form:datosTiposDotaciones");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        navegar("atras");
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            tamano = 300;
            codigo = (Column) c.getViewRoot().findComponent("form:datosTiposDotaciones:codigo");
            codigo.setFilterStyle("width: 85% !important;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposDotaciones:descripcion");
            descripcion.setFilterStyle("width: 85% !important;");
            RequestContext.getCurrentInstance().update("form:datosTiposDotaciones");
            log.info("Activar");
            bandera = 1;
        } else if (bandera == 1) {
            log.info("Desactivar");
            tamano = 320;
            codigo = (Column) c.getViewRoot().findComponent("form:datosTiposDotaciones:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposDotaciones:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTiposDotaciones");
            bandera = 0;
            filtrarTiposDotaciones = null;
            tipoLista = 0;
        }
    }

    public void modificarTiposDotaciones(TiposDotaciones tipodotacion) {
        tipodotacionSeleccionado = tipodotacion;
        if (!crearTiposDotaciones.contains(tipodotacionSeleccionado)) {
            if (modificarTiposDotaciones.isEmpty()) {
                modificarTiposDotaciones.add(tipodotacionSeleccionado);
            } else if (!modificarTiposDotaciones.contains(tipodotacionSeleccionado)) {
                modificarTiposDotaciones.add(tipodotacionSeleccionado);
            }
            guardado = false;
            RequestContext.getCurrentInstance().update("form:datosTiposDotaciones");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
    }

    public void borrandoTiposDotaciones() {
        if (tipodotacionSeleccionado != null) {
            if (!modificarTiposDotaciones.isEmpty() && modificarTiposDotaciones.contains(tipodotacionSeleccionado)) {
                int modIndex = modificarTiposDotaciones.indexOf(tipodotacionSeleccionado);
                modificarTiposDotaciones.remove(modIndex);
                borrarTiposDotaciones.add(tipodotacionSeleccionado);
            } else if (!crearTiposDotaciones.isEmpty() && crearTiposDotaciones.contains(tipodotacionSeleccionado)) {
                int crearIndex = crearTiposDotaciones.indexOf(tipodotacionSeleccionado);
                crearTiposDotaciones.remove(crearIndex);
            } else {
                borrarTiposDotaciones.add(tipodotacionSeleccionado);
            }
            listTiposDotaciones.remove(tipodotacionSeleccionado);
            if (tipoLista == 1) {
                filtrarTiposDotaciones.remove(tipodotacionSeleccionado);
            }
            contarRegistros();
            tipodotacionSeleccionado = null;
            guardado = false;
            RequestContext.getCurrentInstance().update("form:datosTiposDotaciones");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
    }

    public void revisarDialogoGuardar() {
        if (!borrarTiposDotaciones.isEmpty() || !crearTiposDotaciones.isEmpty() || !modificarTiposDotaciones.isEmpty()) {
            RequestContext.getCurrentInstance().update("form:confirmarGuardar");
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
        }
    }

    public void guardarYSalir() {
        guardarTiposDotaciones();
        salir();
    }

    public void guardarTiposDotaciones() {
        try {
            if (guardado == false) {
                msgError = "";
                if (!borrarTiposDotaciones.isEmpty()) {
                    for (int i = 0; i < borrarTiposDotaciones.size(); i++) {
                        msgError = administrarTiposDotaciones.borrarTiposDotaciones(borrarTiposDotaciones.get(i));
                    }
                    registrosBorrados = borrarTiposDotaciones.size();
                    RequestContext.getCurrentInstance().update("form:mostrarBorrados");
                    RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
                    borrarTiposDotaciones.clear();
                }
                if (!modificarTiposDotaciones.isEmpty()) {
                    for (int i = 0; i < modificarTiposDotaciones.size(); i++) {
                        msgError = administrarTiposDotaciones.modificarTiposDotaciones(modificarTiposDotaciones.get(i));
                    }
                    modificarTiposDotaciones.clear();
                }
                if (!crearTiposDotaciones.isEmpty()) {
                    for (int i = 0; i < crearTiposDotaciones.size(); i++) {
                        msgError = administrarTiposDotaciones.crearTiposDotaciones(crearTiposDotaciones.get(i));
                    }
                    crearTiposDotaciones.clear();
                }

                if (msgError.equals("EXITO")) {

                    listTiposDotaciones = null;
                    tipodotacionSeleccionado = null;
                    RequestContext.getCurrentInstance().update("form:datosTiposDotaciones");
                    k = 0;
                    guardado = true;
                    FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                    RequestContext.getCurrentInstance().update("form:growl");
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
        if (tipodotacionSeleccionado != null) {
            editarTiposDotaciones = tipodotacionSeleccionado;
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

    public void agregarNuevoTiposDotaciones() {
        int contador = 0;
        int duplicados = 0;
        Integer a = 0;
        a = null;
        mensajeValidacion = " ";
        if (nuevoTiposDotaciones.getCodigo() == a) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
        } else {
            for (int x = 0; x < listTiposDotaciones.size(); x++) {
                if (listTiposDotaciones.get(x).getCodigo().equals(nuevoTiposDotaciones.getCodigo())) {
                    duplicados++;
                }
            }
            if (duplicados > 0) {
                mensajeValidacion = "El código ingresado ya está en uso. Por favor ingrese un código válido";
            } else {
                contador++;
            }
        }
        if (nuevoTiposDotaciones.getNombre() == null) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
        } else if (nuevoTiposDotaciones.getNombre().isEmpty()) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
        } else {
            contador++;
        }

        if (contador == 2) {
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                codigo = (Column) c.getViewRoot().findComponent("form:datosTiposDotaciones:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposDotaciones:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosTiposDotaciones");
                bandera = 0;
                filtrarTiposDotaciones = null;
                tipoLista = 0;
            }
            k++;
            l = BigInteger.valueOf(k);
            nuevoTiposDotaciones.setSecuencia(l);
            crearTiposDotaciones.add(nuevoTiposDotaciones);
            listTiposDotaciones.add(nuevoTiposDotaciones);
            tipodotacionSeleccionado = nuevoTiposDotaciones;
            nuevoTiposDotaciones = new TiposDotaciones();
            RequestContext.getCurrentInstance().update("form:datosTiposDotaciones");
            contarRegistros();
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().execute("PF('nuevoRegistroTiposDotaciones').hide()");
        } else {
            RequestContext.getCurrentInstance().update("form:validacionNuevaCentroCosto");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevaCentroCosto').show()");
            contador = 0;
        }
    }

    public void limpiarNuevoTiposDotaciones() {
        nuevoTiposDotaciones = new TiposDotaciones();
    }

    public void duplicandoTiposDotaciones() {
        if (tipodotacionSeleccionado != null) {
            duplicarTiposDotaciones = new TiposDotaciones();
            k++;
            l = BigInteger.valueOf(k);
            if (tipoLista == 0) {
                duplicarTiposDotaciones.setSecuencia(l);
                duplicarTiposDotaciones.setCodigo(tipodotacionSeleccionado.getCodigo());
                duplicarTiposDotaciones.setNombre(tipodotacionSeleccionado.getNombre());
            }
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTE");
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroTiposDotaciones').show()");
        }
    }

    public void confirmarDuplicar() {
        int contador = 0;
        mensajeValidacion = " ";
        int duplicados = 0;
        Integer a = 0;
        a = null;

        if (duplicarTiposDotaciones.getCodigo() == a) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
        } else {
            for (int x = 0; x < listTiposDotaciones.size(); x++) {
                if (listTiposDotaciones.get(x).getCodigo().equals(duplicarTiposDotaciones.getCodigo())) {
                    duplicados++;
                }
            }
            if (duplicados > 0) {
                mensajeValidacion = "El código ingresado ya está en uso. Por favor ingrese un código válido";
            } else {
                contador++;
                duplicados = 0;
            }
        }
        if (duplicarTiposDotaciones.getNombre() == null) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
        } else if (duplicarTiposDotaciones.getNombre().isEmpty()) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
        } else {
            contador++;
        }
        if (contador == 2) {
            listTiposDotaciones.add(duplicarTiposDotaciones);
            crearTiposDotaciones.add(duplicarTiposDotaciones);
            tipodotacionSeleccionado = duplicarTiposDotaciones;
            RequestContext.getCurrentInstance().update("form:datosTiposDotaciones");
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            contarRegistros();
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                codigo = (Column) c.getViewRoot().findComponent("form:datosTiposDotaciones:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposDotaciones:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosTiposDotaciones");
                bandera = 0;
                filtrarTiposDotaciones = null;
                tipoLista = 0;
            }
            duplicarTiposDotaciones = new TiposDotaciones();
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroTiposDotaciones').hide()");
        } else {
            contador = 0;
            RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
            RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
        }
    }

    public void limpiarDuplicarTiposDotaciones() {
        duplicarTiposDotaciones = new TiposDotaciones();
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTiposDotacionesExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "TIPOSDOTACIONES", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTiposDotacionesExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "TIPOSDOTACIONES", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void verificarRastro() {
        if (tipodotacionSeleccionado != null) {
            int resultado = administrarRastros.obtenerTabla(tipodotacionSeleccionado.getSecuencia(), "TIPOSDOTACIONES");
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
        } else if (administrarRastros.verificarHistoricosTabla("TIPOSDOTACIONES")) {
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
    }

    //*/*/*/*/*/*/*/*/*/*-/-*//-*/-*/*/*-*/-*/-*/*/*/*/*/---/*/*/*/*/-*/-*/-*/-*/-*/
    public List<TiposDotaciones> getListTiposDotaciones() {
        if (listTiposDotaciones == null) {
            listTiposDotaciones = administrarTiposDotaciones.consultarTiposDotaciones();
        }
        return listTiposDotaciones;
    }

    public void setListTiposDotaciones(List<TiposDotaciones> listTiposDotaciones) {
        this.listTiposDotaciones = listTiposDotaciones;
    }

    public List<TiposDotaciones> getFiltrarTiposDotaciones() {
        return filtrarTiposDotaciones;
    }

    public void setFiltrarTiposDotaciones(List<TiposDotaciones> filtrarTiposDotaciones) {
        this.filtrarTiposDotaciones = filtrarTiposDotaciones;
    }

    public TiposDotaciones getNuevoTiposDotaciones() {
        return nuevoTiposDotaciones;
    }

    public void setNuevoTiposDotaciones(TiposDotaciones nuevoTiposDotaciones) {
        this.nuevoTiposDotaciones = nuevoTiposDotaciones;
    }

    public TiposDotaciones getDuplicarTiposDotaciones() {
        return duplicarTiposDotaciones;
    }

    public void setDuplicarTiposDotaciones(TiposDotaciones duplicarTiposDotaciones) {
        this.duplicarTiposDotaciones = duplicarTiposDotaciones;
    }

    public TiposDotaciones getEditarTiposDotaciones() {
        return editarTiposDotaciones;
    }

    public void setEditarTiposDotaciones(TiposDotaciones editarTiposDotaciones) {
        this.editarTiposDotaciones = editarTiposDotaciones;
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

    public TiposDotaciones getTiposDotacionesSeleccionado() {
        return tipodotacionSeleccionado;
    }

    public void setTiposDotacionesSeleccionado(TiposDotaciones clasesPensionesSeleccionado) {
        this.tipodotacionSeleccionado = clasesPensionesSeleccionado;
    }

    public String getInfoRegistro() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosTiposDotaciones");
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
