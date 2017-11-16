/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import ControlNavegacion.ControlListaNavegacion;
import Entidades.TiposAnexos;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarRastrosInterface;
import InterfaceAdministrar.AdministrarTiposAnexosInterface;
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
public class ControlTiposAnexos implements Serializable {

    private static Logger log = Logger.getLogger(ControlTiposAnexos.class);

    @EJB
    AdministrarTiposAnexosInterface administrarTiposAnexos;
    @EJB
    AdministrarRastrosInterface administrarRastros;

    private List<TiposAnexos> listTiposAnexos;
    private List<TiposAnexos> filtrarTiposAnexos;
    private List<TiposAnexos> crearTiposAnexos;
    private List<TiposAnexos> modificarTiposAnexos;
    private List<TiposAnexos> borrarTiposAnexos;
    private TiposAnexos nuevoTipoAnexo;
    private TiposAnexos duplicarTipoAnexo;
    private TiposAnexos editarTipoAnexo;
    private TiposAnexos TipoAnexosSeleccionado;
    private int cualCelda, tipoLista, index, tipoActualizacion, k, bandera;
    private BigInteger l;
    private Integer backUpCodigo;
    private String backUpDescripcion;
    private boolean aceptar, guardado;
    private Column codigo, descripcion;
    private int registrosBorrados;
    private String mensajeValidacion;
    private Integer a;
    private BigInteger secRegistro;
    private int tamano;
    private String infoRegistro;
    private String msgError;
    private String paginaAnterior = "nominaf";
    private Map<String, Object> mapParametros = new LinkedHashMap<>();
    private BigInteger contadorTipoAnexo;

    public ControlTiposAnexos() {
        listTiposAnexos = null;
        crearTiposAnexos = new ArrayList<TiposAnexos>();
        modificarTiposAnexos = new ArrayList<TiposAnexos>();
        borrarTiposAnexos = new ArrayList<TiposAnexos>();
        editarTipoAnexo = new TiposAnexos();
        nuevoTipoAnexo = new TiposAnexos();
        duplicarTipoAnexo = new TiposAnexos();
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
        String pagActual = "tiposanexos";
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
            administrarTiposAnexos.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
            listTiposAnexos = null;
            getListTiposAnexos();
            if (listTiposAnexos != null) {
                TipoAnexosSeleccionado = listTiposAnexos.get(0);
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

    public void cambiarIndice(TiposAnexos tiposAnexos, int celda) {

        TipoAnexosSeleccionado = tiposAnexos;
        cualCelda = celda;
        TipoAnexosSeleccionado.getSecuencia();
        if (cualCelda == 0) {
            TipoAnexosSeleccionado.getCodigo();
        } else if (cualCelda == 1) {
            TipoAnexosSeleccionado.getNombre();
        }

    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void cerrarFiltrar() {
        tamano = 335;
        codigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTiposAnexos:codigo");
        codigo.setFilterStyle("display: none; visibility: hidden;");
        descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTiposAnexos:descripcion");
        descripcion.setFilterStyle("display: none; visibility: hidden;");
        RequestContext.getCurrentInstance().update("form:datosTiposAnexos");
        bandera = 0;
        filtrarTiposAnexos = null;
        tipoLista = 0;
    }

    public void cancelarModificacion() {
        if (bandera == 1) {
            cerrarFiltrar();
        }
        borrarTiposAnexos.clear();
        crearTiposAnexos.clear();
        modificarTiposAnexos.clear();
        TipoAnexosSeleccionado = null;
        k = 0;
        listTiposAnexos = null;
        getListTiposAnexos();
        contarRegistros();
        guardado = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosTiposAnexos");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void salir() {
        if (bandera == 1) {
            cerrarFiltrar();
        }

        borrarTiposAnexos.clear();
        crearTiposAnexos.clear();
        modificarTiposAnexos.clear();
        TipoAnexosSeleccionado = null;
        k = 0;
        listTiposAnexos = null;
        guardado = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosTiposAnexos");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        navegar("atras");
    }

    public void activarCtrlF11() {
        if (bandera == 0) {
            tamano = 315;
            codigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTiposAnexos:codigo");
            codigo.setFilterStyle("width: 85% !important");
            descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTiposAnexos:descripcion");
            descripcion.setFilterStyle("width: 85% !important");
            RequestContext.getCurrentInstance().update("form:datosTiposAnexos");
            bandera = 1;
        } else if (bandera == 1) {
            cerrarFiltrar();
        }
    }

    public void modificarTipoAnexo(int indice, String confirmarCambio, String valorConfirmar) {
        index = indice;
        int contador = 0;
        boolean banderita = false;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("N")) {
            if (tipoLista == 0) {
                if (!crearTiposAnexos.contains(listTiposAnexos.get(indice))) {
                    if (listTiposAnexos.get(indice).getCodigo() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listTiposAnexos.get(indice).setCodigo(backUpCodigo);
                    } else {
                        for (int j = 0; j < listTiposAnexos.size(); j++) {
                            if (j != indice) {
                                if (listTiposAnexos.get(indice).getCodigo().equals(listTiposAnexos.get(j).getCodigo())) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            listTiposAnexos.get(indice).setCodigo(backUpCodigo);
                            banderita = false;
                        } else {
                            banderita = true;
                        }

                    }
                    if (listTiposAnexos.get(indice).getNombre().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listTiposAnexos.get(indice).setNombre(backUpDescripcion);
                    }
                    if (listTiposAnexos.get(indice).getNombre().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listTiposAnexos.get(indice).setNombre(backUpDescripcion);
                    }

                    if (banderita == true) {
                        if (modificarTiposAnexos.isEmpty()) {
                            modificarTiposAnexos.add(listTiposAnexos.get(indice));
                        } else if (!modificarTiposAnexos.contains(listTiposAnexos.get(indice))) {
                            modificarTiposAnexos.add(listTiposAnexos.get(indice));
                        }
                        if (guardado == true) {
                            guardado = false;
                        }

                    } else {
                        RequestContext.getCurrentInstance().update("form:validacionModificar");
                        RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");
                    }
                    index = -1;
                    secRegistro = null;
                } else {
                    if (listTiposAnexos.get(indice).getCodigo() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listTiposAnexos.get(indice).setCodigo(backUpCodigo);
                    } else {
                        for (int j = 0; j < listTiposAnexos.size(); j++) {
                            if (j != indice) {
                                if (listTiposAnexos.get(indice).getCodigo().equals(listTiposAnexos.get(j).getCodigo())) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            listTiposAnexos.get(indice).setCodigo(backUpCodigo);
                            banderita = false;
                        } else {
                            banderita = true;
                        }

                    }
                    if (listTiposAnexos.get(indice).getNombre().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listTiposAnexos.get(indice).setNombre(backUpDescripcion);
                    }
                    if (listTiposAnexos.get(indice).getNombre().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listTiposAnexos.get(indice).setNombre(backUpDescripcion);
                    }

                    if (banderita == true) {

                        if (guardado == true) {
                            guardado = false;
                        }

                    } else {
                        RequestContext.getCurrentInstance().update("form:validacionModificar");
                        RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");
                    }
                    index = -1;
                    secRegistro = null;
                }
            } else if (!crearTiposAnexos.contains(filtrarTiposAnexos.get(indice))) {
                if (filtrarTiposAnexos.get(indice).getCodigo() == null) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    filtrarTiposAnexos.get(indice).setCodigo(backUpCodigo);
                    banderita = false;
                } else {

                    for (int j = 0; j < filtrarTiposAnexos.size(); j++) {
                        if (j != indice) {
                            if (filtrarTiposAnexos.get(indice).getCodigo().equals(filtrarTiposAnexos.get(j).getCodigo())) {
                                contador++;
                            }
                        }
                    }
                    if (contador > 0) {
                        mensajeValidacion = "CODIGOS REPETIDOS";
                        filtrarTiposAnexos.get(indice).setCodigo(backUpCodigo);
                        banderita = false;
                    } else {
                        banderita = true;
                    }

                }

                if (filtrarTiposAnexos.get(indice).getNombre().isEmpty()) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita = false;
                    filtrarTiposAnexos.get(indice).setNombre(backUpDescripcion);
                }
                if (filtrarTiposAnexos.get(indice).getNombre().equals(" ")) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita = false;
                    filtrarTiposAnexos.get(indice).setNombre(backUpDescripcion);
                }

                if (banderita == true) {
                    if (modificarTiposAnexos.isEmpty()) {
                        modificarTiposAnexos.add(filtrarTiposAnexos.get(indice));
                    } else if (!modificarTiposAnexos.contains(filtrarTiposAnexos.get(indice))) {
                        modificarTiposAnexos.add(filtrarTiposAnexos.get(indice));
                    }
                    if (guardado == true) {
                        guardado = false;
                    }

                } else {
                    RequestContext.getCurrentInstance().update("form:validacionModificar");
                    RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");
                }
                index = -1;
                secRegistro = null;
            } else {
                if (filtrarTiposAnexos.get(indice).getCodigo() == null) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    filtrarTiposAnexos.get(indice).setCodigo(backUpCodigo);
                    banderita = false;
                } else {

                    for (int j = 0; j < filtrarTiposAnexos.size(); j++) {
                        if (j != indice) {
                            if (filtrarTiposAnexos.get(indice).getCodigo().equals(filtrarTiposAnexos.get(j).getCodigo())) {
                                contador++;
                            }
                        }
                    }
                    if (contador > 0) {
                        mensajeValidacion = "CODIGOS REPETIDOS";
                        filtrarTiposAnexos.get(indice).setCodigo(backUpCodigo);
                        banderita = false;
                    } else {
                        banderita = true;
                    }

                }

                if (filtrarTiposAnexos.get(indice).getNombre().isEmpty()) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita = false;
                    filtrarTiposAnexos.get(indice).setNombre(backUpDescripcion);
                }
                if (filtrarTiposAnexos.get(indice).getNombre().equals(" ")) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita = false;
                    filtrarTiposAnexos.get(indice).setNombre(backUpDescripcion);
                }

                if (banderita == true) {

                    if (guardado == true) {
                        guardado = false;
                    }

                } else {
                    RequestContext.getCurrentInstance().update("form:validacionModificar");
                    RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");
                }
                index = -1;
                secRegistro = null;
            }
            RequestContext.getCurrentInstance().update("form:datosTiposAnexos");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }

    }

    public void borrarTiposAnexos() {
        if (TipoAnexosSeleccionado != null) {
            if (!modificarTiposAnexos.isEmpty() && modificarTiposAnexos.contains(TipoAnexosSeleccionado)) {
                int modIndex = modificarTiposAnexos.indexOf(TipoAnexosSeleccionado);
                modificarTiposAnexos.remove(modIndex);
                borrarTiposAnexos.add(TipoAnexosSeleccionado);
            } else if (!crearTiposAnexos.isEmpty() && crearTiposAnexos.contains(TipoAnexosSeleccionado)) {
                int crearIndex = crearTiposAnexos.indexOf(TipoAnexosSeleccionado);
                crearTiposAnexos.remove(crearIndex);
            } else {
                borrarTiposAnexos.add(TipoAnexosSeleccionado);
            }
            listTiposAnexos.remove(TipoAnexosSeleccionado);
            if (tipoLista == 1) {
                filtrarTiposAnexos.remove(TipoAnexosSeleccionado);

            }
            TipoAnexosSeleccionado = null;
            contarRegistros();
            guardado = false;
            RequestContext.getCurrentInstance().update("form:datosTiposAnexos");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }

    }

    public void revisarDialogoGuardar() {
        if (!borrarTiposAnexos.isEmpty() || !crearTiposAnexos.isEmpty() || !modificarTiposAnexos.isEmpty()) {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:confirmarGuardar");
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
        }
    }

    public void guardarTiposAnexos() {
        RequestContext context = RequestContext.getCurrentInstance();
        try {
            if (guardado == false) {
                msgError = "";
                if (!borrarTiposAnexos.isEmpty()) {
                    for (int i = 0; i < borrarTiposAnexos.size(); i++) {
                        msgError = administrarTiposAnexos.borrarTiposAnexos(borrarTiposAnexos.get(i));
                    }
                    registrosBorrados = borrarTiposAnexos.size();
                    RequestContext.getCurrentInstance().update("form:mostrarBorrados");
                    RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
                    borrarTiposAnexos.clear();
                }
                if (!crearTiposAnexos.isEmpty()) {
                    for (int i = 0; i < crearTiposAnexos.size(); i++) {
                        msgError = administrarTiposAnexos.crearTiposAnexos(crearTiposAnexos.get(i));
                    }
                    crearTiposAnexos.clear();
                }
                if (!modificarTiposAnexos.isEmpty()) {
                    for (int i = 0; i < modificarTiposAnexos.size(); i++) {
                        msgError = administrarTiposAnexos.modificarTiposAnexos(modificarTiposAnexos.get(i));
                    }
                    modificarTiposAnexos.clear();
                }
                if (msgError.equals("EXITO")) {

                    listTiposAnexos = null;
                    getListTiposAnexos();
                    k = 0;
                    FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                    RequestContext.getCurrentInstance().update("form:growl");
                    contarRegistros();
                    guardado = true;
                    TipoAnexosSeleccionado = null;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                    RequestContext.getCurrentInstance().update("form:datosTiposAnexos");
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
        if (TipoAnexosSeleccionado != null) {
            editarTipoAnexo = TipoAnexosSeleccionado;
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

    public void agregarNuevoTiposAnexos() {
        log.info("nuevoRegistroTiposAnexos");
        int contador = 0;
        int duplicados = 0;
        Integer a = 0;
        a = null;
        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();
        if (nuevoTipoAnexo.getCodigo() == a) {
            System.out.println("entra a codigo");
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios.";
        } else {
            for (int x = 0; x < listTiposAnexos.size(); x++) {
                if (listTiposAnexos.get(x).getCodigo().equals(nuevoTipoAnexo.getCodigo())) {
                    duplicados++;
                }
            }

            if (duplicados > 0) {
                mensajeValidacion = "El código ingresado ya está registrado. Por favor ingrese un código válido";
            } else {
                contador++;
            }
        }
        if (nuevoTipoAnexo.getNombre() == null || nuevoTipoAnexo.getNombre().isEmpty()) {
            System.out.println("entra a nombre");
            mensajeValidacion = " Los campos marcados con asterisco son obligatorios ";
        } else {
            contador++;
        }

        if (contador == 2) {
            if (bandera == 1) {
                //CERRAR FILTRADO
                cerrarFiltrar();
            }
            k++;
            l = BigInteger.valueOf(k);
            nuevoTipoAnexo.setSecuencia(l);
            crearTiposAnexos.add(nuevoTipoAnexo);
            listTiposAnexos.add(0, nuevoTipoAnexo);
            TipoAnexosSeleccionado = nuevoTipoAnexo;
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().execute("PF('nuevoRegistroTiposAnexos').hide()");
            RequestContext.getCurrentInstance().update("form:datosTiposAnexos");
            nuevoTipoAnexo = new TiposAnexos();

        } else {
            RequestContext.getCurrentInstance().update("form:validacionNuevaCentroCosto");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevaCentroCosto').show()");
            contador = 0;
        }
    }

    public void limpiarNuevoTiposAnexos() {
        log.info("limpiarNuevoTiposAnexos()");
        nuevoTipoAnexo = new TiposAnexos();
    }

    //------------------------------------------------------------------------------
    public void duplicarTiposAnexos() {
        if (TipoAnexosSeleccionado != null) {
            duplicarTipoAnexo = new TiposAnexos();
            k++;
            l = BigInteger.valueOf(k);
            duplicarTipoAnexo.setSecuencia(l);
            duplicarTipoAnexo.setCodigo(TipoAnexosSeleccionado.getCodigo());
            duplicarTipoAnexo.setNombre(TipoAnexosSeleccionado.getNombre());
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTipoAnexo");
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroTipoAnexo').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void confirmarDuplicar() {
        int contador = 0;
        mensajeValidacion = " ";
        int duplicados = 0;
        a = null;
        if (duplicarTipoAnexo.getCodigo() == a) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
        } else {
            for (int x = 0; x < listTiposAnexos.size(); x++) {
                if (listTiposAnexos.get(x).getCodigo() == duplicarTipoAnexo.getCodigo()) {
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
        if (duplicarTipoAnexo.getNombre() == null || duplicarTipoAnexo.getNombre().isEmpty()) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
        } else {
            contador++;
        }
        if (contador == 2) {
            listTiposAnexos.add(duplicarTipoAnexo);
            crearTiposAnexos.add(duplicarTipoAnexo);
            RequestContext.getCurrentInstance().update("form:datosTiposAnexos");
            TipoAnexosSeleccionado = duplicarTipoAnexo;
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            if (bandera == 1) {
                cerrarFiltrar();
            }
            duplicarTipoAnexo = new TiposAnexos();
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroTipoAnexo').hide()");
        } else {
            contador = 0;
            RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
            RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
        }
    }

    public void limpiarDuplicarTiposAnexos() {
        duplicarTipoAnexo = new TiposAnexos();
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTiposAnexosExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "TIPOSANEXOS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTiposAnexosExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "TIPOSANEXOS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (TipoAnexosSeleccionado != null) {
            int resultado = administrarRastros.obtenerTabla(TipoAnexosSeleccionado.getSecuencia(), "TIPOSANEXOS"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
        } else if (administrarRastros.verificarHistoricosTabla("TIPOSANEXOS")) { // igual acá
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
    }

    public void contarRegistros() {
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
    }

    /**
     * ******************************************************************************************************
     * @return
     */
    public List<TiposAnexos> getListTiposAnexos() {
        if (listTiposAnexos == null) {
            listTiposAnexos = administrarTiposAnexos.consultarTiposAnexos();
        }
        return listTiposAnexos;
    }

    public void setListTiposAnexos(List<TiposAnexos> listTiposAnexos) {
        this.listTiposAnexos = listTiposAnexos;
    }

    public List<TiposAnexos> getFiltrarTiposAnexos() {
        return filtrarTiposAnexos;
    }

    public void setFiltrarTiposAnexos(List<TiposAnexos> filtrarTiposAnexos) {
        this.filtrarTiposAnexos = filtrarTiposAnexos;
    }

    public TiposAnexos getNuevoTipoAnexo() {
        return nuevoTipoAnexo;
    }

    public void setNuevoTipoAnexo(TiposAnexos nuevoTipoAnexo) {
        this.nuevoTipoAnexo = nuevoTipoAnexo;
    }

    public TiposAnexos getDuplicarTipoAnexo() {
        return duplicarTipoAnexo;
    }

    public void setDuplicarTipoAnexo(TiposAnexos duplicarTipoAnexo) {
        this.duplicarTipoAnexo = duplicarTipoAnexo;
    }

    public TiposAnexos getEditarTipoAnexo() {
        return editarTipoAnexo;
    }

    public void setEditarTipoAnexo(TiposAnexos editarTipoAnexo) {
        this.editarTipoAnexo = editarTipoAnexo;
    }

    public TiposAnexos getTipoAnexosSeleccionado() {
        return TipoAnexosSeleccionado;
    }

    public void setTipoAnexosSeleccionado(TiposAnexos TipoAnexosSeleccionado) {
        this.TipoAnexosSeleccionado = TipoAnexosSeleccionado;
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
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosTiposAnexos");
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
