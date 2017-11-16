/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import ControlNavegacion.ControlListaNavegacion;
import Entidades.Errores;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarErroresInterfaz;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
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
public class ControlErrores implements Serializable {

    private static Logger log = Logger.getLogger(ControlErrores.class);

    @EJB
    AdministrarErroresInterfaz administrarErrores;
    @EJB
    AdministrarRastrosInterface administrarRastros;

    private List<Errores> listErrores;
    private List<Errores> filtrarErrores;
    private List<Errores> crearErrores;
    private List<Errores> modificarErrores;
    private List<Errores> borrarErrores;
    private Errores nuevoError;
    private Errores duplicarError;
    private Errores editarError;
    private Errores ErrorSeleccionado;
    private int cualCelda, tipoLista, index, tipoActualizacion, k, bandera;
    private BigInteger l;
    private Integer backUpCodigo, backUpCodigoOracle;
    private String backUpDescripcion, backUpSolucion;
    private boolean aceptar, guardado;
    private Column codigo, codigoOracle, descripcion, solucion;
    private int registrosBorrados;
    private String mensajeValidacion;
    private Integer a;
    private BigInteger secRegistro;
    private int tamano;
    private String infoRegistro;
    private String msgError;
    private String paginaAnterior = "nominaf";
    private Map<String, Object> mapParametros = new LinkedHashMap<>();

    public ControlErrores() {
        listErrores = null;
        crearErrores = new ArrayList<Errores>();
        modificarErrores = new ArrayList<Errores>();
        borrarErrores = new ArrayList<Errores>();
        editarError = new Errores();
        nuevoError = new Errores();
        duplicarError = new Errores();
        guardado = true;
        mapParametros.put("paginaAnterior", paginaAnterior);
        tamano = 335;
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
        String pagActual = "errores";
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
            administrarErrores.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
            listErrores = null;
            getListErrores();
            if (listErrores != null) {
                ErrorSeleccionado = listErrores.get(0);
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

    public void cambiarIndice(Errores errores, int celda) {

        ErrorSeleccionado = errores;
        cualCelda = celda;
        ErrorSeleccionado.getSecuencia();
        switch (cualCelda) {
            case 0:
                ErrorSeleccionado.getCodigo();
                break;
            case 1:
                ErrorSeleccionado.getCodigooracle();
                break;
            case 3:
                ErrorSeleccionado.getDescripcion();
                break;
            case 4:
                ErrorSeleccionado.getSolucion();
                break;
            default:
                break;
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void cerrarFiltrar() {
        tamano = 335;
        codigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosErrores:codigo");
        codigo.setFilterStyle("display: none; visibility: hidden;");
        codigoOracle = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosErrores:codigoOracle");
        codigoOracle.setFilterStyle("display: none; visibility: hidden;");
        descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosErrores:descripcion");
        descripcion.setFilterStyle("display: none; visibility: hidden;");
        solucion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosErrores:solucion");
        solucion.setFilterStyle("display: none; visibility: hidden;");
        RequestContext.getCurrentInstance().update("form:datosErrores");
        bandera = 0;
        filtrarErrores = null;
        tipoLista = 0;
    }

    public void cancelarModificacion() {
        if (bandera == 1) {
            cerrarFiltrar();
        }
        borrarErrores.clear();
        crearErrores.clear();
        modificarErrores.clear();
        ErrorSeleccionado = null;
        k = 0;
        listErrores = null;
        getListErrores();
        contarRegistros();
        guardado = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosErrores");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void salir() {
        if (bandera == 1) {
            cerrarFiltrar();
        }

        borrarErrores.clear();
        crearErrores.clear();
        modificarErrores.clear();
        ErrorSeleccionado = null;
        k = 0;
        listErrores = null;
        guardado = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosErrores");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        navegar("atras");
    }

    public void activarCtrlF11() {
        if (bandera == 0) {
            tamano = 315;
            codigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosErrores:codigo");
            codigo.setFilterStyle("width: 85% !important");
            codigoOracle = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosErrores:codigoOracle");
            codigoOracle.setFilterStyle("width: 85% !important");
            descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosErrores:descripcion");
            descripcion.setFilterStyle("width: 85% !important");
            solucion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosErrores:solucion");
            solucion.setFilterStyle("width: 85% !important");
            RequestContext.getCurrentInstance().update("form:datosErrores");
            bandera = 1;
        } else if (bandera == 1) {
            cerrarFiltrar();
        }
    }

    public void modificarError(int indice, String confirmarCambio, String valorConfirmar) {
        index = indice;
        int contador = 0;
        boolean banderita = false;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("N")) {
            if (tipoLista == 0) {
                if (!crearErrores.contains(listErrores.get(indice))) {
                    if (listErrores.get(indice).getCodigo() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listErrores.get(indice).setCodigo(backUpCodigo);
                    } else {
                        for (int j = 0; j < listErrores.size(); j++) {
                            if (j != indice) {
                                if (listErrores.get(indice).getCodigo().equals(listErrores.get(j).getCodigo())) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            listErrores.get(indice).setCodigo(backUpCodigo);
                            banderita = false;
                        } else {
                            banderita = true;
                        }

                    }
                    if (listErrores.get(indice).getCodigooracle() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listErrores.get(indice).setCodigooracle(backUpCodigoOracle);
                    }
                    if (listErrores.get(indice).getCodigooracle().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listErrores.get(indice).setCodigooracle(backUpCodigoOracle);
                    }
                    if (listErrores.get(indice).getDescripcion().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listErrores.get(indice).setDescripcion(backUpDescripcion);
                    }
                    if (listErrores.get(indice).getDescripcion().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listErrores.get(indice).setDescripcion(backUpDescripcion);
                    }
                    if (banderita == true) {
                        if (modificarErrores.isEmpty()) {
                            modificarErrores.add(listErrores.get(indice));
                        } else if (!modificarErrores.contains(listErrores.get(indice))) {
                            modificarErrores.add(listErrores.get(indice));
                        }
                        if (guardado) {
                            guardado = false;
                        }

                    } else {
                        RequestContext.getCurrentInstance().update("form:validacionModificar");
                        RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");
                    }
                    index = -1;
                    secRegistro = null;
                } else {
                    if (listErrores.get(indice).getCodigo() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listErrores.get(indice).setCodigo(backUpCodigo);
                    } else {
                        for (int j = 0; j < listErrores.size(); j++) {
                            if (j != indice) {
                                if (listErrores.get(indice).getCodigo().equals(listErrores.get(j).getCodigo())) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            listErrores.get(indice).setCodigo(backUpCodigo);
                            banderita = false;
                        } else {
                            banderita = true;
                        }
                    }
                    if (listErrores.get(indice).getCodigooracle() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listErrores.get(indice).setCodigooracle(backUpCodigoOracle);
                    }
                    if (listErrores.get(indice).getCodigooracle().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listErrores.get(indice).setCodigooracle(backUpCodigoOracle);
                    }
                    if (listErrores.get(indice).getDescripcion().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listErrores.get(indice).setDescripcion(backUpDescripcion);
                    }
                    if (listErrores.get(indice).getDescripcion().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listErrores.get(indice).setDescripcion(backUpDescripcion);
                    }
                    if (banderita == true) {

                        if (guardado) {
                            guardado = false;
                        }
                    } else {
                        RequestContext.getCurrentInstance().update("form:validacionModificar");
                        RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");
                    }
                    index = -1;
                    secRegistro = null;
                }
            } else if (!crearErrores.contains(filtrarErrores.get(indice))) {
                if (filtrarErrores.get(indice).getCodigo() == null) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    filtrarErrores.get(indice).setCodigo(backUpCodigo);
                    banderita = false;
                } else {
                    for (int j = 0; j < filtrarErrores.size(); j++) {
                        if (j != indice) {
                            if (filtrarErrores.get(indice).getCodigo().equals(filtrarErrores.get(j).getCodigo())) {
                                contador++;
                            }
                        }
                    }
                    if (contador > 0) {
                        mensajeValidacion = "CODIGOS REPETIDOS";
                        filtrarErrores.get(indice).setCodigo(backUpCodigo);
                        banderita = false;
                    } else {
                        banderita = true;
                    }
                }
                if (filtrarErrores.get(indice).getCodigooracle() == null) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita = false;
                    filtrarErrores.get(indice).setCodigooracle(backUpCodigoOracle);
                }
                if (filtrarErrores.get(indice).getCodigooracle().equals(" ")) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita = false;
                    filtrarErrores.get(indice).setCodigooracle(backUpCodigoOracle);
                }
                if (filtrarErrores.get(indice).getDescripcion().isEmpty()) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita = false;
                    filtrarErrores.get(indice).setDescripcion(backUpDescripcion);
                }
                if (filtrarErrores.get(indice).getDescripcion().equals(" ")) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita = false;
                    filtrarErrores.get(indice).setDescripcion(backUpDescripcion);
                }
                if (banderita == true) {
                    if (modificarErrores.isEmpty()) {
                        modificarErrores.add(filtrarErrores.get(indice));
                    } else if (!modificarErrores.contains(filtrarErrores.get(indice))) {
                        modificarErrores.add(filtrarErrores.get(indice));
                    }
                    if (guardado) {
                        guardado = false;
                    }
                } else {
                    RequestContext.getCurrentInstance().update("form:validacionModificar");
                    RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");
                }
                index = -1;
                secRegistro = null;
            } else {
                if (filtrarErrores.get(indice).getCodigo() == null) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    filtrarErrores.get(indice).setCodigo(backUpCodigo);
                    banderita = false;
                } else {

                    for (int j = 0; j < filtrarErrores.size(); j++) {
                        if (j != indice) {
                            if (filtrarErrores.get(indice).getCodigo().equals(filtrarErrores.get(j).getCodigo())) {
                                contador++;
                            }
                        }
                    }
                    if (contador > 0) {
                        mensajeValidacion = "CODIGOS REPETIDOS";
                        filtrarErrores.get(indice).setCodigo(backUpCodigo);
                        banderita = false;
                    } else {
                        banderita = true;
                    }
                }

                if (filtrarErrores.get(indice).getCodigooracle() == null) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita = false;
                    filtrarErrores.get(indice).setCodigooracle(backUpCodigoOracle);
                }
                if (filtrarErrores.get(indice).getCodigooracle().equals(" ")) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita = false;
                    filtrarErrores.get(indice).setCodigooracle(backUpCodigoOracle);
                }
                if (filtrarErrores.get(indice).getDescripcion().isEmpty()) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita = false;
                    filtrarErrores.get(indice).setDescripcion(backUpDescripcion);
                }
                if (filtrarErrores.get(indice).getDescripcion().equals(" ")) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita = false;
                    filtrarErrores.get(indice).setDescripcion(backUpDescripcion);
                }
                if (banderita == true) {
                    if (guardado) {
                        guardado = false;
                    }
                } else {
                    RequestContext.getCurrentInstance().update("form:validacionModificar");
                    RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");
                }
                index = -1;
                secRegistro = null;
            }
            RequestContext.getCurrentInstance().update("form:datosErrores");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
    }

    public void borrarErrores() {
        if (ErrorSeleccionado != null) {
            if (!modificarErrores.isEmpty() && modificarErrores.contains(ErrorSeleccionado)) {
                int modIndex = modificarErrores.indexOf(ErrorSeleccionado);
                modificarErrores.remove(modIndex);
                borrarErrores.add(ErrorSeleccionado);
            } else if (!crearErrores.isEmpty() && crearErrores.contains(ErrorSeleccionado)) {
                int crearIndex = crearErrores.indexOf(ErrorSeleccionado);
                crearErrores.remove(crearIndex);
            } else {
                borrarErrores.add(ErrorSeleccionado);
            }
            listErrores.remove(ErrorSeleccionado);
            if (tipoLista == 1) {
                filtrarErrores.remove(ErrorSeleccionado);

            }
            ErrorSeleccionado = null;
            contarRegistros();
            guardado = false;
            RequestContext.getCurrentInstance().update("form:datosErrores");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void revisarDialogoGuardar() {
        if (!borrarErrores.isEmpty() || !crearErrores.isEmpty() || !modificarErrores.isEmpty()) {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:confirmarGuardar");
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
        }
    }

    public void guardarErrores() {
        RequestContext context = RequestContext.getCurrentInstance();
        try {
            if (guardado == false) {
                msgError = "";
                if (!borrarErrores.isEmpty()) {
                    for (int i = 0; i < borrarErrores.size(); i++) {
                        msgError = administrarErrores.borrarErrores(borrarErrores.get(i));
                    }
                    registrosBorrados = borrarErrores.size();
                    RequestContext.getCurrentInstance().update("form:mostrarBorrados");
                    RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
                    borrarErrores.clear();
                }
                if (!crearErrores.isEmpty()) {
                    for (int i = 0; i < crearErrores.size(); i++) {
                        msgError = administrarErrores.crearErrores(crearErrores.get(i));
                    }
                    crearErrores.clear();
                }
                if (!modificarErrores.isEmpty()) {
                    for (int i = 0; i < modificarErrores.size(); i++) {
                        msgError = administrarErrores.modificarErrores(modificarErrores.get(i));
                    }
                    modificarErrores.clear();
                }
                if (msgError.equals("EXITO")) {

                    listErrores = null;
                    getListErrores();
                    k = 0;
                    FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                    RequestContext.getCurrentInstance().update("form:growl");
                    contarRegistros();
                    guardado = true;
                    ErrorSeleccionado = null;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                    RequestContext.getCurrentInstance().update("form:datosErrores");
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
        if (ErrorSeleccionado != null) {
            editarError = ErrorSeleccionado;
            switch (cualCelda) {
                case 0:
                    RequestContext.getCurrentInstance().update("formularioDialogos:editCodigo");
                    RequestContext.getCurrentInstance().execute("PF('editCodigo').show()");
                    cualCelda = -1;
                    break;
                case 1:
                    RequestContext.getCurrentInstance().update("formularioDialogos:editcodigoOracle");
                    RequestContext.getCurrentInstance().execute("PF('editcodigoOracle').show()");
                    cualCelda = -1;
                    break;
                case 2:
                    RequestContext.getCurrentInstance().update("formularioDialogos:editDescripcion");
                    RequestContext.getCurrentInstance().execute("PF('editDescripcion').show()");
                    cualCelda = -1;
                    break;
                case 3:
                    RequestContext.getCurrentInstance().update("formularioDialogos:editSolucion");
                    RequestContext.getCurrentInstance().execute("PF('editSolucion').show()");
                    cualCelda = -1;
                    break;
                default:
                    break;
            }
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void agregarNuevoErrores() {
        log.info("nuevoRegistroErrores");
        int contador = 0;
        int duplicados = 0;
        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();
        if (nuevoError.getCodigo() == a) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios.";
        } else {
            for (int x = 0; x < listErrores.size(); x++) {
                if (listErrores.get(x).getCodigo().equals(nuevoError.getCodigo())) {
                    duplicados++;
                }
            }

            if (duplicados > 0) {
                mensajeValidacion = "El código ingresado ya está registrado. Por favor ingrese un código válido";
            } else {
                contador++;
            }
        }
        if (nuevoError.getCodigooracle() == null || nuevoError.getCodigooracle().equals(" ")) {
            mensajeValidacion = " Los campos marcados con asterisco son obligatorios ";
        } else {
            contador++;
        }
        if (nuevoError.getDescripcion() == null || nuevoError.getDescripcion().isEmpty()) {
            mensajeValidacion = " Los campos marcados con asterisco son obligatorios ";
        } else {
            contador++;
        }

        if (contador == 3) {
            if (bandera == 1) {
                //CERRAR FILTRADO
                cerrarFiltrar();
            }
            k++;
            l = BigInteger.valueOf(k);
            nuevoError.setSecuencia(l);
            crearErrores.add(nuevoError);
            listErrores.add(0, nuevoError);
            ErrorSeleccionado = nuevoError;
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().execute("PF('nuevoRegistroErrores').hide()");
            RequestContext.getCurrentInstance().update("form:datosErrores");
            nuevoError = new Errores();

        } else {
            RequestContext.getCurrentInstance().update("form:validacionNuevaCentroCosto");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevaCentroCosto').show()");
            contador = 0;
        }
    }

    public void limpiarNuevoErrores() {
        log.info("limpiarNuevoErrores()");
        nuevoError = new Errores();
    }

    public void duplicarErrores() {
        if (ErrorSeleccionado != null) {
            duplicarError = new Errores();
            k++;
            l = BigInteger.valueOf(k);
            duplicarError.setSecuencia(l);
            duplicarError.setCodigo(ErrorSeleccionado.getCodigo());
            duplicarError.setCodigooracle(ErrorSeleccionado.getCodigooracle());
            duplicarError.setDescripcion(ErrorSeleccionado.getDescripcion());
            duplicarError.setSolucion(ErrorSeleccionado.getSolucion());
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarError");
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroError').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void confirmarDuplicar() {
        int contador = 0;
        mensajeValidacion = " ";
        int duplicados = 0;
        if (duplicarError.getCodigo() == a) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
        } else {
            for (int x = 0; x < listErrores.size(); x++) {
                if (listErrores.get(x).getCodigo() == duplicarError.getCodigo()) {
                    duplicados++;
                }
            }
            System.out.println("duplicados: " + duplicados);
            if (duplicados > 0) {
                mensajeValidacion = "El código ingresado ya está registrado. Por favor ingrese un código válido";
            } else {
                contador++;
                duplicados = 0;
            }
        }
        if (duplicarError.getCodigooracle() == null || duplicarError.getCodigooracle().equals(" ")) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
        } else {
            contador++;
        }
        if (duplicarError.getDescripcion() == null || duplicarError.getDescripcion().isEmpty()) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
        } else {
            contador++;
        }
        if (contador == 3) {
            listErrores.add(duplicarError);
            crearErrores.add(duplicarError);
            RequestContext.getCurrentInstance().update("form:datosErrores");
            ErrorSeleccionado = duplicarError;
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            if (bandera == 1) {
                cerrarFiltrar();
            }
            duplicarError = new Errores();
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroError').hide()");
        } else {
            contador = 0;
            RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
            RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
        }
    }

    public void limpiarDuplicarErrores() {
        duplicarError = new Errores();
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosErroresExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "ERRORES", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosErroresExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "ERRORES", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (ErrorSeleccionado != null) {
            int resultado = administrarRastros.obtenerTabla(ErrorSeleccionado.getSecuencia(), "ERRORES"); //En ENCARGATURAS lo cambia por el nombre de su tabla
            switch (resultado) {
                case 1:
                    RequestContext.getCurrentInstance().execute("PF('errorObjetosDB').show()");
                    break;
                case 2:
                    RequestContext.getCurrentInstance().execute("PF('confirmarRastro').show()");
                    break;
                case 3:
                    RequestContext.getCurrentInstance().execute("PF('errorRegistroRastro').show()");
                    break;
                case 4:
                    RequestContext.getCurrentInstance().execute("PF('errorTablaConRastro').show()");
                    break;
                case 5:
                    RequestContext.getCurrentInstance().execute("PF('errorTablaSinRastro').show()");
                    break;
                default:
                    break;
            }
        } else if (administrarRastros.verificarHistoricosTabla("ERRORES")) { // igual acá
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
    }

    public void contarRegistros() {
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
    }

    /*-----------------------------------------------------------------------------------------------------------------------*/
    public List<Errores> getListErrores() {
        if (listErrores == null) {
            listErrores = administrarErrores.consultarErrores();
        }
        return listErrores;
    }

    public void setListErrores(List<Errores> listErrores) {
        this.listErrores = listErrores;
    }

    public List<Errores> getFiltrarErrores() {
        return filtrarErrores;
    }

    public void setFiltrarErrores(List<Errores> filtrarErrores) {
        this.filtrarErrores = filtrarErrores;
    }

    public Errores getNuevoError() {
        return nuevoError;
    }

    public void setNuevoError(Errores nuevoError) {
        this.nuevoError = nuevoError;
    }

    public Errores getDuplicarError() {
        return duplicarError;
    }

    public void setDuplicarError(Errores duplicarError) {
        this.duplicarError = duplicarError;
    }

    public Errores getEditarError() {
        return editarError;
    }

    public void setEditarError(Errores editarError) {
        this.editarError = editarError;
    }

    public Errores getErrorSeleccionado() {
        return ErrorSeleccionado;
    }

    public void setErrorSeleccionado(Errores ErrorSeleccionado) {
        this.ErrorSeleccionado = ErrorSeleccionado;
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

    public int getTamano() {
        return tamano;
    }

    public void setTamano(int tamano) {
        this.tamano = tamano;
    }

    public String getInfoRegistro() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosErrores");
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
