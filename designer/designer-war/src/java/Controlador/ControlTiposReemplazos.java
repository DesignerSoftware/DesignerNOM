/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.TiposReemplazos;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarRastrosInterface;
import InterfaceAdministrar.AdministrarTiposReemplazosInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
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
public class ControlTiposReemplazos implements Serializable {

    private static Logger log = Logger.getLogger(ControlTiposReemplazos.class);

    @EJB
    AdministrarTiposReemplazosInterface administrarTiposReemplazos;
    @EJB
    AdministrarRastrosInterface administrarRastros;
    private List<TiposReemplazos> listTiposReemplazos;
    private List<TiposReemplazos> filtrarTiposReemplazos;
    private List<TiposReemplazos> crearTiposReemplazos;
    private List<TiposReemplazos> modificarTiposReemplazos;
    private List<TiposReemplazos> borrarTiposReemplazos;
    private TiposReemplazos nuevoTipoReemplazo;
    private TiposReemplazos duplicarTipoReemplazo;
    private TiposReemplazos editarTipoReemplazo;
    private TiposReemplazos tiposReemplazosSeleccionado;
    //otros
    private int cualCelda, tipoLista, tipoActualizacion, k, bandera;
    private BigInteger l;
    private boolean aceptar, guardado;
    private Column codigo, descripcion, factorReemplazado;
    //borrado
    private int registrosBorrados;
    private String mensajeValidacion;
    BigDecimal verificarBorrandoEncargaturas;
    BigDecimal verificarBorradoProgramacionesTiempos;
    BigDecimal verificarBorradoReemplazos;
    private String infoRegistro;
    private String msgError;
    //Redireccionamiento de pantallas
    private int tamano;
    private String paginaAnterior = "nominaf";
    private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

    public ControlTiposReemplazos() {
        listTiposReemplazos = null;
        crearTiposReemplazos = new ArrayList<TiposReemplazos>();
        modificarTiposReemplazos = new ArrayList<TiposReemplazos>();
        borrarTiposReemplazos = new ArrayList<TiposReemplazos>();
        editarTipoReemplazo = new TiposReemplazos();
        nuevoTipoReemplazo = new TiposReemplazos();
        duplicarTipoReemplazo = new TiposReemplazos();
        guardado = true;
        tamano = 310;
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
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarTiposReemplazos.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
            listTiposReemplazos = null;
            getListTiposReemplazos();
            if (listTiposReemplazos != null) {
                if (!listTiposReemplazos.isEmpty()) {
                    tiposReemplazosSeleccionado = listTiposReemplazos.get(0);
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
        String pagActual = "tiporeemplazo";
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

    public String redirigir() {
        return paginaAnterior;
    }

    public void eventoFiltrar() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
        contarRegisros();
    }

    public void contarRegisros() {
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
    }

    public void cambiarIndice(TiposReemplazos tipoR, int celda) {
        tiposReemplazosSeleccionado = tipoR;
        cualCelda = celda;
        tiposReemplazosSeleccionado.getSecuencia();
        if (cualCelda == 0) {
            tiposReemplazosSeleccionado.getCodigo();
        }
        if (cualCelda == 1) {
            tiposReemplazosSeleccionado.getNombre();
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void cancelarModificacion() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 1) {
            //CERRAR FILTRADO
            codigo = (Column) c.getViewRoot().findComponent("form:datosTipoReemplazo:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTipoReemplazo:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            factorReemplazado = (Column) c.getViewRoot().findComponent("form:datosTipoReemplazo:factorReemplazado");
            factorReemplazado.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTipoReemplazo");
            bandera = 0;
            filtrarTiposReemplazos = null;
            tipoLista = 0;
            tamano = 310;
        }

        borrarTiposReemplazos.clear();
        crearTiposReemplazos.clear();
        modificarTiposReemplazos.clear();
        tiposReemplazosSeleccionado = null;
        k = 0;
        listTiposReemplazos = null;
        guardado = true;
        getListTiposReemplazos();
        contarRegisros();
        RequestContext.getCurrentInstance().update("form:datosTipoReemplazo");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void salir() {
        limpiarListasValor();
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 1) {
            //CERRAR FILTRADO
            codigo = (Column) c.getViewRoot().findComponent("form:datosTipoReemplazo:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTipoReemplazo:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            factorReemplazado = (Column) c.getViewRoot().findComponent("form:datosTipoReemplazo:factorReemplazado");
            factorReemplazado.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTipoReemplazo");
            bandera = 0;
            filtrarTiposReemplazos = null;
            tipoLista = 0;
            tamano = 310;
        }
        borrarTiposReemplazos.clear();
        crearTiposReemplazos.clear();
        modificarTiposReemplazos.clear();
        tiposReemplazosSeleccionado = null;
        k = 0;
        listTiposReemplazos = null;
        guardado = true;
        getListTiposReemplazos();
        contarRegisros();
        RequestContext.getCurrentInstance().update("form:datosTipoReemplazo");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        navegar("atras");
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            tamano = 290;
            codigo = (Column) c.getViewRoot().findComponent("form:datosTipoReemplazo:codigo");
            codigo.setFilterStyle("width: 85% !important;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTipoReemplazo:descripcion");
            descripcion.setFilterStyle("width: 85% !important;");
            factorReemplazado = (Column) c.getViewRoot().findComponent("form:datosTipoReemplazo:factorReemplazado");
            factorReemplazado.setFilterStyle("width: 85% !important;");
            RequestContext.getCurrentInstance().update("form:datosTipoReemplazo");
            bandera = 1;
        } else if (bandera == 1) {
            tamano = 310;
            codigo = (Column) c.getViewRoot().findComponent("form:datosTipoReemplazo:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTipoReemplazo:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            factorReemplazado = (Column) c.getViewRoot().findComponent("form:datosTipoReemplazo:factorReemplazado");
            factorReemplazado.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTipoReemplazo");
            bandera = 0;
            filtrarTiposReemplazos = null;
            tipoLista = 0;
        }
    }

    public void modificandoTipoReemplazo(TiposReemplazos tipoR) {
        tiposReemplazosSeleccionado = tipoR;
        if (!crearTiposReemplazos.contains(tiposReemplazosSeleccionado)) {
            if (modificarTiposReemplazos.isEmpty()) {
                modificarTiposReemplazos.add(tiposReemplazosSeleccionado);
            } else if (!modificarTiposReemplazos.contains(tiposReemplazosSeleccionado)) {
                modificarTiposReemplazos.add(tiposReemplazosSeleccionado);
            }
        }
        guardado = false;
        RequestContext.getCurrentInstance().update("form:datosTipoReemplazo");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void borrandoTiposReemplazos() {
        if (tiposReemplazosSeleccionado != null) {
            if (!modificarTiposReemplazos.isEmpty() && modificarTiposReemplazos.contains(tiposReemplazosSeleccionado)) {
                int modIndex = modificarTiposReemplazos.indexOf(tiposReemplazosSeleccionado);
                modificarTiposReemplazos.remove(modIndex);
                borrarTiposReemplazos.add(tiposReemplazosSeleccionado);
            } else if (!crearTiposReemplazos.isEmpty() && crearTiposReemplazos.contains(tiposReemplazosSeleccionado)) {
                int crearIndex = crearTiposReemplazos.indexOf(tiposReemplazosSeleccionado);
                crearTiposReemplazos.remove(crearIndex);
            } else {
                borrarTiposReemplazos.add(tiposReemplazosSeleccionado);
            }
            listTiposReemplazos.remove(tiposReemplazosSeleccionado);
            if (tipoLista == 1) {
                filtrarTiposReemplazos.remove(tiposReemplazosSeleccionado);

            }
            contarRegisros();
            RequestContext.getCurrentInstance().update("form:datosTipoReemplazo");
            tiposReemplazosSeleccionado = null;
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }

    }

    public void verificarBorrado() {
        BigInteger verificarBorrandoEncargaturas;
        BigInteger verificarBorradoProgramacionesTiempos;
        BigInteger verificarBorradoReemplazos;
        try {
            verificarBorrandoEncargaturas = administrarTiposReemplazos.contarEncargaturasTipoReemplazo(tiposReemplazosSeleccionado.getSecuencia());
            verificarBorradoProgramacionesTiempos = administrarTiposReemplazos.contarProgramacionesTiemposTipoReemplazo(tiposReemplazosSeleccionado.getSecuencia());
            verificarBorradoReemplazos = administrarTiposReemplazos.contarReemplazosTipoReemplazo(tiposReemplazosSeleccionado.getSecuencia());
            if (verificarBorrandoEncargaturas.equals(new BigInteger("0")) && verificarBorradoProgramacionesTiempos.equals(new BigInteger("0")) && verificarBorradoReemplazos.equals(new BigInteger("0"))) {
                borrandoTiposReemplazos();
            } else {
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:validacionBorrar");
                RequestContext.getCurrentInstance().execute("PF('validacionBorrar').show()");
                verificarBorrandoEncargaturas = new BigInteger("-1");
                verificarBorradoProgramacionesTiempos = new BigInteger("-1");
                verificarBorradoReemplazos = new BigInteger("-1");
            }
        } catch (Exception e) {
            log.error("ERROR ControlTiposCertificados verificarBorrado ERROR  ", e);
        }
    }

    public void revisarDialogoGuardar() {

        if (!borrarTiposReemplazos.isEmpty() || !crearTiposReemplazos.isEmpty() || !modificarTiposReemplazos.isEmpty()) {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:confirmarGuardar");
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
        }

    }

    public void guardarTipoReemplazo() {
        RequestContext context = RequestContext.getCurrentInstance();
        try {
            if (guardado == false) {
                msgError = "";
                if (!borrarTiposReemplazos.isEmpty()) {
                    for (int i = 0; i < borrarTiposReemplazos.size(); i++) {
                        administrarTiposReemplazos.borrarTiposReemplazos(borrarTiposReemplazos.get(i));
                    }
                    //mostrarBorrados
                    registrosBorrados = borrarTiposReemplazos.size();
                    RequestContext.getCurrentInstance().update("form:mostrarBorrados");
                    RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
                    borrarTiposReemplazos.clear();
                }
                if (!crearTiposReemplazos.isEmpty()) {
                    for (int i = 0; i < borrarTiposReemplazos.size(); i++) {
                        administrarTiposReemplazos.crearTiposReemplazos(crearTiposReemplazos.get(i));
                    }
                    crearTiposReemplazos.clear();
                }
                if (!modificarTiposReemplazos.isEmpty()) {
                    for (int i = 0; i < borrarTiposReemplazos.size(); i++) {
                        administrarTiposReemplazos.modificarTiposReemplazos(modificarTiposReemplazos.get(i));
                    }
                    modificarTiposReemplazos.clear();
                }
                if (msgError.equals("EXITO")) {

                    listTiposReemplazos = null;
                    getListTiposReemplazos();
                    contarRegisros();
                    guardado = true;
                    FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                    RequestContext.getCurrentInstance().update("form:growl");
                    RequestContext.getCurrentInstance().update("form:datosTipoReemplazo");
                    k = 0;
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
        if (tiposReemplazosSeleccionado != null) {
            editarTipoReemplazo = tiposReemplazosSeleccionado;
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
                RequestContext.getCurrentInstance().update("formularioDialogos:editarFactorRiesgo");
                RequestContext.getCurrentInstance().execute("PF('editarFactorRiesgo').show()");
                cualCelda = -1;
            }
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void agregarNuevoTiposReemplazos() {
        int contador = 0;
        int duplicados = 0;
        Integer a = 0;
        a = null;
        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();
        if (nuevoTipoReemplazo.getCodigo() == a) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
        } else {
            for (int x = 0; x < listTiposReemplazos.size(); x++) {
                if (listTiposReemplazos.get(x).getCodigo() == nuevoTipoReemplazo.getCodigo()) {
                    duplicados++;
                }
            }
            if (duplicados > 0) {
                mensajeValidacion = "Existe un registro con el código ingresado. Por favor ingrese un código válido";
            } else {
                contador++;
            }
        }
        if (nuevoTipoReemplazo.getNombre() == null || nuevoTipoReemplazo.getNombre().isEmpty()) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
        } else {
            contador++;
        }
        if (contador == 2) {
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                codigo = (Column) c.getViewRoot().findComponent("form:datosTipoReemplazo:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosTipoReemplazo:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                factorReemplazado = (Column) c.getViewRoot().findComponent("form:datosTipoReemplazo:factorReemplazado");
                factorReemplazado.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosTipoReemplazo");
                bandera = 0;
                filtrarTiposReemplazos = null;
                tipoLista = 0;
                tamano = 310;
            }
            k++;
            l = BigInteger.valueOf(k);
            nuevoTipoReemplazo.setSecuencia(l);
            crearTiposReemplazos.add(nuevoTipoReemplazo);
            listTiposReemplazos.add(0, nuevoTipoReemplazo);
            tiposReemplazosSeleccionado = nuevoTipoReemplazo;
            RequestContext.getCurrentInstance().update("form:datosTipoReemplazo");
            contarRegisros();
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().execute("PF('nuevoRegistroTiposReemplazos').hide()");
            nuevoTipoReemplazo = new TiposReemplazos();
        } else {
            RequestContext.getCurrentInstance().update("form:validacionNuevaCentroCosto");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevaCentroCosto').show()");
            contador = 0;
        }
    }

    public void limpiarNuevoTiposReemplazos() {
        nuevoTipoReemplazo = new TiposReemplazos();
    }

    //------------------------------------------------------------------------------
    public void duplicandoTiposReemplazos() {
        if (tiposReemplazosSeleccionado != null) {
            duplicarTipoReemplazo = new TiposReemplazos();
            k++;
            l = BigInteger.valueOf(k);
            duplicarTipoReemplazo.setSecuencia(l);
            duplicarTipoReemplazo.setCodigo(tiposReemplazosSeleccionado.getCodigo());
            duplicarTipoReemplazo.setNombre(tiposReemplazosSeleccionado.getNombre());
            duplicarTipoReemplazo.setFactorreemplazado(tiposReemplazosSeleccionado.getFactorreemplazado());
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
        if (duplicarTipoReemplazo.getCodigo() == a) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
        } else {
            for (int x = 0; x < listTiposReemplazos.size(); x++) {
                if (listTiposReemplazos.get(x).getCodigo() == duplicarTipoReemplazo.getCodigo()) {
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
        if (duplicarTipoReemplazo.getNombre() == null || duplicarTipoReemplazo.getNombre().isEmpty()) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
        } else {
            contador++;
        }
        if (contador == 2) {
            k++;
            l = BigInteger.valueOf(k);
            duplicarTipoReemplazo.setSecuencia(l);
            crearTiposReemplazos.add(duplicarTipoReemplazo);
            listTiposReemplazos.add(0, duplicarTipoReemplazo);
            tiposReemplazosSeleccionado = duplicarTipoReemplazo;
            contarRegisros();
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                //CERRAR FILTRADO
                codigo = (Column) c.getViewRoot().findComponent("form:datosTipoReemplazo:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosTipoReemplazo:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                factorReemplazado = (Column) c.getViewRoot().findComponent("form:datosTipoReemplazo:factorReemplazado");
                factorReemplazado.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosTipoReemplazo");
                bandera = 0;
                filtrarTiposReemplazos = null;
                tipoLista = 0;
                tamano = 310;
            }
            duplicarTipoReemplazo = new TiposReemplazos();
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroTiposReemplazos').hide()");
        } else {
            contador = 0;
            RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
            RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
        }
    }

    public void limpiarDuplicarTiposReemplazos() {
        duplicarTipoReemplazo = new TiposReemplazos();
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTipoReemplazoExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "TIPOSREEMPLAZOS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTipoReemplazoExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "TIPOSREEMPLAZOS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void verificarRastro() {
        if (tiposReemplazosSeleccionado != null) {
            int resultado = administrarRastros.obtenerTabla(tiposReemplazosSeleccionado.getSecuencia(), "TIPOSREEMPLAZOS"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
        } else if (administrarRastros.verificarHistoricosTabla("TIPOSREEMPLAZOS")) { // igual acá
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
    }

    //--------///////////////////////---------------------*****//*/*/*/*/*/-****----
    public List<TiposReemplazos> getListTiposReemplazos() {
        if (listTiposReemplazos == null) {
            listTiposReemplazos = administrarTiposReemplazos.consultarTiposReemplazos();
        }
        return listTiposReemplazos;
    }

    public void setListTiposReemplazos(List<TiposReemplazos> listTiposReemplazos) {
        this.listTiposReemplazos = listTiposReemplazos;
    }

    public List<TiposReemplazos> getFiltrarTiposReemplazos() {
        return filtrarTiposReemplazos;
    }

    public void setFiltrarTiposReemplazos(List<TiposReemplazos> filtrarTiposReemplazos) {
        this.filtrarTiposReemplazos = filtrarTiposReemplazos;
    }

    public TiposReemplazos getNuevoTipoReemplazo() {
        return nuevoTipoReemplazo;
    }

    public void setNuevoTipoReemplazo(TiposReemplazos nuevoTipoReemplazo) {
        this.nuevoTipoReemplazo = nuevoTipoReemplazo;
    }

    public TiposReemplazos getDuplicarTipoReemplazo() {
        return duplicarTipoReemplazo;
    }

    public void setDuplicarTipoReemplazo(TiposReemplazos duplicarTipoReemplazo) {
        this.duplicarTipoReemplazo = duplicarTipoReemplazo;
    }

    public TiposReemplazos getEditarTipoReemplazo() {
        return editarTipoReemplazo;
    }

    public void setEditarTipoReemplazo(TiposReemplazos editarTipoReemplazo) {
        this.editarTipoReemplazo = editarTipoReemplazo;
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

    public TiposReemplazos getTiposReemplazosSeleccionado() {
        return tiposReemplazosSeleccionado;
    }

    public void setTiposReemplazosSeleccionado(TiposReemplazos tiposReemplazosSeleccionado) {
        this.tiposReemplazosSeleccionado = tiposReemplazosSeleccionado;
    }

    public int getTamano() {
        return tamano;
    }

    public void setTamano(int tamano) {
        this.tamano = tamano;
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
