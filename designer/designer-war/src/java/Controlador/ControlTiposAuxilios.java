package Controlador;

import Entidades.TiposAuxilios;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarRastrosInterface;
import InterfaceAdministrar.AdministrarTiposAuxiliosInterface;
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
public class ControlTiposAuxilios implements Serializable {

    private static Logger log = Logger.getLogger(ControlTiposAuxilios.class);

    @EJB
    AdministrarTiposAuxiliosInterface administrarTiposAuxilios;
    @EJB
    AdministrarRastrosInterface administrarRastros;
    private List<TiposAuxilios> listTiposAuxilios;
    private List<TiposAuxilios> filtrarTiposAuxilios;
    private List<TiposAuxilios> crearTiposAuxilios;
    private List<TiposAuxilios> modificarTiposAuxilios;
    private List<TiposAuxilios> borrarTiposAuxilios;
    private TiposAuxilios nuevoTipoAuxilios;
    private TiposAuxilios duplicarTipoAuxilio;
    private TiposAuxilios editarTipoAuxilio;
    private TiposAuxilios tipoAuxilioTablaSeleccionado;
    private int cualCelda, tipoLista, k, bandera;
    private BigInteger l;
    private boolean aceptar, guardado;
    private Column codigo, descripcion;
    private int registrosBorrados;
    private String mensajeValidacion;
    private String infoRegistro;
    private BigInteger verificarTablasAuxilios;
    private String altoTabla;
    private String msgError;
    private String paginaAnterior = "nominaf";
    private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

    public ControlTiposAuxilios() {
        altoTabla = "310";
        listTiposAuxilios = null;
        crearTiposAuxilios = new ArrayList<TiposAuxilios>();
        modificarTiposAuxilios = new ArrayList<TiposAuxilios>();
        borrarTiposAuxilios = new ArrayList<TiposAuxilios>();
        editarTipoAuxilio = new TiposAuxilios();
        nuevoTipoAuxilios = new TiposAuxilios();
        duplicarTipoAuxilio = new TiposAuxilios();
        guardado = true;
        mapParametros.put("paginaAnterior", paginaAnterior);
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
        String pagActual = "tipoauxilio";
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
        log.info(this.getClass().getName() + ".inicializarAdministrador() @PostConstruct");
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarTiposAuxilios.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
            listTiposAuxilios = null;
            getListTiposAuxilios();
            if (listTiposAuxilios != null) {
                if (!listTiposAuxilios.isEmpty()) {
                    tipoAuxilioTablaSeleccionado = listTiposAuxilios.get(0);
                }
            }
        } catch (Exception e) {
            log.error("Error postconstruct " + this.getClass().getName() + ":  ", e);
            log.error("Causa: " + e.getCause());
        }
    }

    public void eventoFiltrar() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
        contarRegistros();
    }

    public void cambiarIndice(TiposAuxilios tipoAux, int celda) {
        tipoAuxilioTablaSeleccionado = tipoAux;
        cualCelda = celda;
        tipoAuxilioTablaSeleccionado.getSecuencia();
        if (cualCelda == 0) {
            tipoAuxilioTablaSeleccionado.getCodigo();
        } else if (cualCelda == 1) {
            tipoAuxilioTablaSeleccionado.getDescripcion();
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void cancelarModificacion() {
        if (bandera == 1) {
            altoTabla = "310";
            FacesContext c = FacesContext.getCurrentInstance();
            codigo = (Column) c.getViewRoot().findComponent("form:datosTipoAuxilio:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTipoAuxilio:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTipoAuxilio");
            bandera = 0;
            filtrarTiposAuxilios = null;
            tipoLista = 0;
        }

        borrarTiposAuxilios.clear();
        crearTiposAuxilios.clear();
        modificarTiposAuxilios.clear();
        tipoAuxilioTablaSeleccionado = null;
        k = 0;
        listTiposAuxilios = null;
        getListTiposAuxilios();
        contarRegistros();
        guardado = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosTipoAuxilio");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }
    
    public void salir() {
        if (bandera == 1) {
            altoTabla = "310";
            FacesContext c = FacesContext.getCurrentInstance();
            codigo = (Column) c.getViewRoot().findComponent("form:datosTipoAuxilio:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTipoAuxilio:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTipoAuxilio");
            bandera = 0;
            filtrarTiposAuxilios = null;
            tipoLista = 0;
        }

        borrarTiposAuxilios.clear();
        crearTiposAuxilios.clear();
        modificarTiposAuxilios.clear();
        tipoAuxilioTablaSeleccionado = null;
        k = 0;
        listTiposAuxilios = null;
        getListTiposAuxilios();
        contarRegistros();
        guardado = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosTipoAuxilio");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        navegar("atras");
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            altoTabla = "290";
            codigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoAuxilio:codigo");
            codigo.setFilterStyle("width: 85% !important;");
            descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoAuxilio:descripcion");
            descripcion.setFilterStyle("width: 85% !important;");
            RequestContext.getCurrentInstance().update("form:datosTipoAuxilio");
            bandera = 1;
        } else if (bandera == 1) {
            altoTabla = "310";
            codigo = (Column) c.getViewRoot().findComponent("form:datosTipoAuxilio:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTipoAuxilio:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTipoAuxilio");
            bandera = 0;
            filtrarTiposAuxilios = null;
            tipoLista = 0;
        }
    }

    public void modificandoTipoAuxilio(TiposAuxilios tipoAux) {
        tipoAuxilioTablaSeleccionado = tipoAux;
        if (!crearTiposAuxilios.contains(tipoAuxilioTablaSeleccionado)) {
            if (modificarTiposAuxilios.isEmpty()) {
                modificarTiposAuxilios.add(tipoAuxilioTablaSeleccionado);
            } else if (!modificarTiposAuxilios.contains(tipoAuxilioTablaSeleccionado)) {
                modificarTiposAuxilios.add(tipoAuxilioTablaSeleccionado);
            }
        }
        guardado = false;
        RequestContext.getCurrentInstance().update("form:datosTipoAuxilio");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");

    }

    public void verificarBorrado() {
        try {
            verificarTablasAuxilios = administrarTiposAuxilios.contarTablasAuxiliosTiposAuxilios(tipoAuxilioTablaSeleccionado.getSecuencia());
            if (!verificarTablasAuxilios.equals(new BigInteger("0"))) {
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:validacionBorrar");
                RequestContext.getCurrentInstance().execute("PF('validacionBorrar').show()");
                verificarTablasAuxilios = new BigInteger("-1");
            } else {
                borrandoTiposAuxilios();
            }
        } catch (Exception e) {
            log.error("ERROR ControlTiposCertificados verificarBorrado ERROR  ", e);
        }
    }

    public void borrandoTiposAuxilios() {
        if (tipoAuxilioTablaSeleccionado != null) {
            if (!modificarTiposAuxilios.isEmpty() && modificarTiposAuxilios.contains(tipoAuxilioTablaSeleccionado)) {
                int modIndex = modificarTiposAuxilios.indexOf(tipoAuxilioTablaSeleccionado);
                modificarTiposAuxilios.remove(modIndex);
                borrarTiposAuxilios.add(tipoAuxilioTablaSeleccionado);
            } else if (!crearTiposAuxilios.isEmpty() && crearTiposAuxilios.contains(tipoAuxilioTablaSeleccionado)) {
                int crearIndex = crearTiposAuxilios.indexOf(tipoAuxilioTablaSeleccionado);
                crearTiposAuxilios.remove(crearIndex);
            } else {
                borrarTiposAuxilios.add(tipoAuxilioTablaSeleccionado);
            }
            listTiposAuxilios.remove(tipoAuxilioTablaSeleccionado);
            if (tipoLista == 1) {
                filtrarTiposAuxilios.remove(tipoAuxilioTablaSeleccionado);

            }
            tipoAuxilioTablaSeleccionado = null;
            contarRegistros();
            guardado = false;
            RequestContext.getCurrentInstance().update("form:datosTipoAuxilio");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void revisarDialogoGuardar() {
        if (!borrarTiposAuxilios.isEmpty() || !crearTiposAuxilios.isEmpty() || !modificarTiposAuxilios.isEmpty()) {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:confirmarGuardar");
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
        }
    }

    public void guardarTiposAuxilio() {
        try {
            if (guardado == false) {
                msgError = "";
                if (!borrarTiposAuxilios.isEmpty()) {
                    for (int i = 0; i < borrarTiposAuxilios.size(); i++) {
                        msgError = administrarTiposAuxilios.borrarTiposAuxilios(borrarTiposAuxilios.get(i));
                    }
                    //mostrarBorrados
                    registrosBorrados = borrarTiposAuxilios.size();
                    RequestContext.getCurrentInstance().update("form:mostrarBorrados");
                    RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
                    borrarTiposAuxilios.clear();
                }
                if (!crearTiposAuxilios.isEmpty()) {
                    for (int i = 0; i < crearTiposAuxilios.size(); i++) {
                        msgError = administrarTiposAuxilios.crearTiposAuxilios(crearTiposAuxilios.get(i));
                    }
                    crearTiposAuxilios.clear();
                }
                if (!modificarTiposAuxilios.isEmpty()) {
                    for (int i = 0; i < modificarTiposAuxilios.size(); i++) {
                        msgError = administrarTiposAuxilios.modificarTiposAuxilios(modificarTiposAuxilios.get(i));
                    }
                    modificarTiposAuxilios.clear();
                }
                if (msgError.equals("EXITO")) {
                    listTiposAuxilios = null;
                    getListTiposAuxilios();
                    guardado = true;
                    RequestContext.getCurrentInstance().update("form:datosTipoAuxilio");
                    k = 0;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                    FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                    RequestContext.getCurrentInstance().update("form:growl");
                } else {
                    RequestContext.getCurrentInstance().update("formularioDialogos:errorGuardadoBD");
                    RequestContext.getCurrentInstance().execute("PF('errorGuardadoBD').show()");
                }
            }
        } catch (Exception e) {
            log.warn("Error guardarTiposAuxilio : " + e.toString());
            FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }
    }

    public void editarCelda() {
        if (tipoAuxilioTablaSeleccionado != null) {
            editarTipoAuxilio = tipoAuxilioTablaSeleccionado;
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

    public void agregarNuevoTiposAuxilios() {
        int contador = 0;
        int duplicados = 0;

        Integer a = 0;
        a = null;
        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();
        if (nuevoTipoAuxilios.getCodigo() == a) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
        } else {
            for (int x = 0; x < listTiposAuxilios.size(); x++) {
                if (listTiposAuxilios.get(x).getCodigo() == nuevoTipoAuxilios.getCodigo()) {
                    duplicados++;
                }
            }
            if (duplicados > 0) {
                mensajeValidacion = "Existe un registro con el código ingresado. Por favor ingrese un código válido";
            } else {
                contador++;
            }
        }
        if (nuevoTipoAuxilios.getDescripcion() == (null)) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
        } else {
            contador++;
        }
        if (contador == 2) {
            if (bandera == 1) {
                altoTabla = "310";
                FacesContext c = FacesContext.getCurrentInstance();
                codigo = (Column) c.getViewRoot().findComponent("form:datosTipoAuxilio:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosTipoAuxilio:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosTipoAuxilio");
                bandera = 0;
                filtrarTiposAuxilios = null;
                tipoLista = 0;
            }
            k++;
            l = BigInteger.valueOf(k);
            nuevoTipoAuxilios.setSecuencia(l);
            crearTiposAuxilios.add(nuevoTipoAuxilios);
            listTiposAuxilios.add(0, nuevoTipoAuxilios);
            tipoAuxilioTablaSeleccionado = nuevoTipoAuxilios;
            RequestContext.getCurrentInstance().update("form:datosTipoAuxilio");
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            contarRegistros();
            RequestContext.getCurrentInstance().execute("PF('nuevoRegistroTiposReemplazos').hide()");
            nuevoTipoAuxilios = new TiposAuxilios();
        } else {
            RequestContext.getCurrentInstance().update("form:validacionNuevaCentroCosto");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevaCentroCosto').show()");
            contador = 0;
        }
    }

    public void limpiarNuevoTiposAuxilios() {
        nuevoTipoAuxilios = new TiposAuxilios();
        tipoAuxilioTablaSeleccionado = null;

    }

    //------------------------------------------------------------------------------
    public void duplicandoTiposAuxilios() {
        if (tipoAuxilioTablaSeleccionado != null) {
            duplicarTipoAuxilio = new TiposAuxilios();
            duplicarTipoAuxilio.setCodigo(tipoAuxilioTablaSeleccionado.getCodigo());
            duplicarTipoAuxilio.setDescripcion(tipoAuxilioTablaSeleccionado.getDescripcion());
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTTR");
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroTiposReemplazos').show()");
        }
    }

    public void confirmarDuplicar() {
        int contador = 0;
        mensajeValidacion = " ";
        int duplicados = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        Integer a = 0;
        a = null;

        if (duplicarTipoAuxilio.getCodigo() == a) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
        } else {
            for (int x = 0; x < listTiposAuxilios.size(); x++) {
                if (listTiposAuxilios.get(x).getCodigo() == duplicarTipoAuxilio.getCodigo()) {
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
        if (duplicarTipoAuxilio.getDescripcion() == null) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
        } else {
            contador++;
        }

        if (contador == 2) {
            k++;
            l = BigInteger.valueOf(k);
            duplicarTipoAuxilio.setSecuencia(l);
            listTiposAuxilios.add(0, duplicarTipoAuxilio);
            crearTiposAuxilios.add(duplicarTipoAuxilio);
            tipoAuxilioTablaSeleccionado = duplicarTipoAuxilio;
            guardado = false;
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:datosTipoAuxilio");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            if (bandera == 1) {
                altoTabla = "310";
                FacesContext c = FacesContext.getCurrentInstance();
                codigo = (Column) c.getViewRoot().findComponent("form:datosTipoAuxilio:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosTipoAuxilio:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosTipoAuxilio");
                bandera = 0;
                filtrarTiposAuxilios = null;
                tipoLista = 0;
            }
            duplicarTipoAuxilio = new TiposAuxilios();
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroTiposReemplazos').hide()");
        } else {
            contador = 0;
            RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
            RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
        }
    }

    public void limpiarDuplicarTiposAuxilios() {
        duplicarTipoAuxilio = new TiposAuxilios();
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTipoAuxilioExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "TIPOSAUXILIOS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTipoAuxilioExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "TIPOSAUXILIOS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (!listTiposAuxilios.isEmpty()) {
            if (tipoAuxilioTablaSeleccionado != null) {
                int resultado = administrarRastros.obtenerTabla(tipoAuxilioTablaSeleccionado.getSecuencia(), "TIPOSAUXILIOS"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
            } else {
                RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
            }
        } else if (administrarRastros.verificarHistoricosTabla("TIPOSAUXILIOS")) { // igual acá
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
    }

    public void contarRegistros() {
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
    }

    //--------///////////////////////---------------------*****//*/*/*/*/*/-****----
    public List<TiposAuxilios> getListTiposAuxilios() {
        if (listTiposAuxilios == null) {
            listTiposAuxilios = administrarTiposAuxilios.consultarTiposAuxilios();
        }
        return listTiposAuxilios;
    }

    public void setListTiposAuxilios(List<TiposAuxilios> listTiposAuxilios) {
        this.listTiposAuxilios = listTiposAuxilios;
    }

    public List<TiposAuxilios> getFiltrarTiposAuxilios() {
        return filtrarTiposAuxilios;
    }

    public void setFiltrarTiposAuxilios(List<TiposAuxilios> filtrarTiposAuxilios) {
        this.filtrarTiposAuxilios = filtrarTiposAuxilios;
    }

    public TiposAuxilios getNuevoTipoAuxilios() {
        return nuevoTipoAuxilios;
    }

    public void setNuevoTipoAuxilios(TiposAuxilios nuevoTipoAuxilios) {
        this.nuevoTipoAuxilios = nuevoTipoAuxilios;
    }

    public TiposAuxilios getDuplicarTipoAuxilio() {
        return duplicarTipoAuxilio;
    }

    public void setDuplicarTipoAuxilio(TiposAuxilios duplicarTipoAuxilio) {
        this.duplicarTipoAuxilio = duplicarTipoAuxilio;
    }

    public TiposAuxilios getEditarTipoAuxilio() {
        return editarTipoAuxilio;
    }

    public void setEditarTipoAuxilio(TiposAuxilios editarTipoAuxilio) {
        this.editarTipoAuxilio = editarTipoAuxilio;
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

    public String getInfoRegistro() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosTipoAuxilio");
        infoRegistro = String.valueOf(tabla.getRowCount());
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

    public TiposAuxilios getTipoAuxilioTablaSeleccionado() {
        return tipoAuxilioTablaSeleccionado;
    }

    public void setTipoAuxilioTablaSeleccionado(TiposAuxilios tipoAuxilioTablaSeleccionado) {
        this.tipoAuxilioTablaSeleccionado = tipoAuxilioTablaSeleccionado;
    }

    public String getAltoTabla() {
        return altoTabla;
    }

    public void setAltoTabla(String altoTabla) {
        this.altoTabla = altoTabla;
    }

    public String getMsgError() {
        return msgError;
    }

    public void setMsgError(String msgError) {
        this.msgError = msgError;
    }
}