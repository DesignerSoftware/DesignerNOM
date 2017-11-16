/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.TiposAccidentes;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarTiposAccidentesInterface;
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
public class ControlTiposAccidentes implements Serializable {

    private static Logger log = Logger.getLogger(ControlTiposAccidentes.class);

    @EJB
    AdministrarTiposAccidentesInterface administrarTiposAccidentes;
    @EJB
    AdministrarRastrosInterface administrarRastros;

    private List<TiposAccidentes> listTiposAccidentes;
    private List<TiposAccidentes> filtrarTiposAccidentes;
    private List<TiposAccidentes> crearTiposAccidentes;
    private List<TiposAccidentes> modificarTiposAccidentes;
    private List<TiposAccidentes> borrarTiposAccidentes;
    private TiposAccidentes nuevoTiposAccidentes;
    private TiposAccidentes duplicarTiposAccidentes;
    private TiposAccidentes editarTiposAccidentes;
    private TiposAccidentes tiposAccidentesSeleccionado;
    //otros
    private int cualCelda, tipoLista, index, tipoActualizacion, k, bandera;
    private BigInteger l;
    private boolean aceptar, guardado;
    //AutoCompletar
    private boolean permitirIndex;
    //RASTRO
    private BigInteger secRegistro;
    private Column codigo, descripcion;
    //borrado
    private int registrosBorrados;
    private String mensajeValidacion;
    //filtrado table
    private int tamano;
    private String backUpCodigo;
    private String backUpDescripcion;
    private String msgError;
    private String paginaAnterior = "nominaf";
    private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

    public ControlTiposAccidentes() {
        listTiposAccidentes = null;
        crearTiposAccidentes = new ArrayList<TiposAccidentes>();
        modificarTiposAccidentes = new ArrayList<TiposAccidentes>();
        borrarTiposAccidentes = new ArrayList<TiposAccidentes>();
        permitirIndex = true;
        editarTiposAccidentes = new TiposAccidentes();
        nuevoTiposAccidentes = new TiposAccidentes();
        duplicarTiposAccidentes = new TiposAccidentes();
        guardado = true;
        tamano = 270;
        mapParametros.put("paginaAnterior", paginaAnterior);
        log.info("controlTiposAccidentes Constructor");
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
            log.info("ControlTiposAccidentes PostConstruct ");
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarTiposAccidentes.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
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
        String pagActual = "tipoaccidente";
        if (pag.equals("atras")) {
            pag = paginaAnterior;
            paginaAnterior = "nominaf";
            controlListaNavegacion.quitarPagina(pagActual, this.getClass().getSimpleName());
        } else {
            controlListaNavegacion.guardarNavegacion(pagActual, pag);
            fc.getApplication().getNavigationHandler().handleNavigation(fc, null, pag);
            //Map<String, Object> mapParaEnviar = new LinkedHashMap<String, Object>();
            //mapParaEnviar.put("paginaAnterior", pagActual);
            //mas Parametros
            //         if (pag.equals("rastrotabla")) {
            //           ControlRastro controlRastro = (ControlRastro) fc.getApplication().evaluateExpressionGet(fc, "#{controlRastro}", ControlRastro.class);
            //           controlRastro.recibirDatosTabla(conceptoSeleccionado.getSecuencia(), "Conceptos", pagActual);
            //      } else if (pag.equals("rastrotablaH")) {
            //       ControlRastro controlRastro = (ControlRastro) fc.getApplication().evaluateExpressionGet(fc, "#{controlRastro}", ControlRastro.class);
            //     controlRastro.historicosTabla("Conceptos", pagActual);
            //   pag = "rastrotabla";
            //}
        }
        limpiarListasValor();
    }

    public void eventoFiltrar() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
        contarRegistros();
    }

    public void cambiarIndice(TiposAccidentes tipoaccidente, int celda) {
        tiposAccidentesSeleccionado = tipoaccidente;
        cualCelda = celda;
        tiposAccidentesSeleccionado.getSecuencia();
        if (cualCelda == 0) {
            tiposAccidentesSeleccionado.getCodigo();
        } else if (cualCelda == 1) {
            tiposAccidentesSeleccionado.getNombre();
        }
    }

    public void asignarIndex(Integer indice, int LND, int dig) {
        try {
            log.info("\n ENTRE A ControlTiposAccidentes.asignarIndex \n");
            index = indice;
            if (LND == 0) {
                tipoActualizacion = 0;
            } else if (LND == 1) {
                tipoActualizacion = 1;
                log.info("Tipo Actualizacion: " + tipoActualizacion);
            } else if (LND == 2) {
                tipoActualizacion = 2;
            }

        } catch (Exception e) {
            log.warn("Error ControlTiposAccidentes.asignarIndex ERROR======" + e.getMessage());
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void listaValoresBoton() {
    }
    private String infoRegistro;

    public void cancelarModificacion() {
        if (bandera == 1) {
            codigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTiposAccidentes:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTiposAccidentes:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTiposAccidentes");
            bandera = 0;
            filtrarTiposAccidentes = null;
            tipoLista = 0;
            tamano = 335;
        }
        borrarTiposAccidentes.clear();
        crearTiposAccidentes.clear();
        modificarTiposAccidentes.clear();
        tiposAccidentesSeleccionado = null;
        k = 0;
        listTiposAccidentes = null;
        getListTiposAccidentes();
        contarRegistros();
        guardado = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosTiposAccidentes");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void salir() {
        limpiarListasValor();
        if (bandera == 1) {
            codigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTiposAccidentes:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTiposAccidentes:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTiposAccidentes:descripcion");
            bandera = 0;
            filtrarTiposAccidentes = null;
            tipoLista = 0;
            tamano = 335;

        }
        borrarTiposAccidentes.clear();
        crearTiposAccidentes.clear();
        modificarTiposAccidentes.clear();
        tiposAccidentesSeleccionado = null;
        k = 0;
        listTiposAccidentes = null;
        guardado = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosTiposAccidentes");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        navegar("atras");
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            tamano = 315;
            codigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTiposAccidentes:codigo");
            codigo.setFilterStyle("width: 85% !important");
            descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTiposAccidentes:descripcion");
            descripcion.setFilterStyle("width: 85% !important");
            RequestContext.getCurrentInstance().update("form:datosTiposAccidentes");
            bandera = 1;
        } else if (bandera == 1) {
            tamano = 335;
            codigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTiposAccidentes:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTiposAccidentes:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTiposAccidentes");
            bandera = 0;
            filtrarTiposAccidentes = null;
            tipoLista = 0;
        }
    }

    public void modificarTiposAccidentes(int indice, String confirmarCambio, String valorConfirmar) {
        log.error("ENTRE A MODIFICAR SUB CATEGORIA");
        index = indice;

        int contador = 0;
        boolean banderita = false;
        Integer a;
        a = null;
        RequestContext context = RequestContext.getCurrentInstance();
        log.error("TIPO LISTA = " + tipoLista);
        if (confirmarCambio.equalsIgnoreCase("N")) {
            log.error("ENTRE A MODIFICAR EMPRESAS, CONFIRMAR CAMBIO ES N");
            if (tipoLista == 0) {
                if (!crearTiposAccidentes.contains(listTiposAccidentes.get(indice))) {
                    if (listTiposAccidentes.get(indice).getCodigo() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listTiposAccidentes.get(indice).setCodigo(backUpCodigo);
                    } else {
                        for (int j = 0; j < listTiposAccidentes.size(); j++) {
                            if (j != indice) {
                                if (listTiposAccidentes.get(indice).getCodigo().equals(listTiposAccidentes.get(j).getCodigo())) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            listTiposAccidentes.get(indice).setCodigo(backUpCodigo);
                            banderita = false;
                        } else {
                            banderita = true;
                        }

                    }
                    if (listTiposAccidentes.get(indice).getNombre().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listTiposAccidentes.get(indice).setNombre(backUpDescripcion);
                    }
                    if (listTiposAccidentes.get(indice).getNombre().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listTiposAccidentes.get(indice).setNombre(backUpDescripcion);
                    }

                    if (banderita == true) {
                        if (modificarTiposAccidentes.isEmpty()) {
                            modificarTiposAccidentes.add(listTiposAccidentes.get(indice));
                        } else if (!modificarTiposAccidentes.contains(listTiposAccidentes.get(indice))) {
                            modificarTiposAccidentes.add(listTiposAccidentes.get(indice));
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
                    if (listTiposAccidentes.get(indice).getCodigo() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listTiposAccidentes.get(indice).setCodigo(backUpCodigo);
                    } else {
                        for (int j = 0; j < listTiposAccidentes.size(); j++) {
                            if (j != indice) {
                                if (listTiposAccidentes.get(indice).getCodigo().equals(listTiposAccidentes.get(j).getCodigo())) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            listTiposAccidentes.get(indice).setCodigo(backUpCodigo);
                            banderita = false;
                        } else {
                            banderita = true;
                        }

                    }
                    if (listTiposAccidentes.get(indice).getNombre().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listTiposAccidentes.get(indice).setNombre(backUpDescripcion);
                    }
                    if (listTiposAccidentes.get(indice).getNombre().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listTiposAccidentes.get(indice).setNombre(backUpDescripcion);
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
            } else if (!crearTiposAccidentes.contains(filtrarTiposAccidentes.get(indice))) {
                if (filtrarTiposAccidentes.get(indice).getCodigo() == null) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    filtrarTiposAccidentes.get(indice).setCodigo(backUpCodigo);
                    banderita = false;
                } else {

                    for (int j = 0; j < filtrarTiposAccidentes.size(); j++) {
                        if (j != indice) {
                            if (filtrarTiposAccidentes.get(indice).getCodigo().equals(filtrarTiposAccidentes.get(j).getCodigo())) {
                                contador++;
                            }
                        }
                    }
                    if (contador > 0) {
                        mensajeValidacion = "CODIGOS REPETIDOS";
                        filtrarTiposAccidentes.get(indice).setCodigo(backUpCodigo);
                        banderita = false;
                    } else {
                        banderita = true;
                    }

                }

                if (filtrarTiposAccidentes.get(indice).getNombre().isEmpty()) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita = false;
                    filtrarTiposAccidentes.get(indice).setNombre(backUpDescripcion);
                }
                if (filtrarTiposAccidentes.get(indice).getNombre().equals(" ")) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita = false;
                    filtrarTiposAccidentes.get(indice).setNombre(backUpDescripcion);
                }

                if (banderita == true) {
                    if (modificarTiposAccidentes.isEmpty()) {
                        modificarTiposAccidentes.add(filtrarTiposAccidentes.get(indice));
                    } else if (!modificarTiposAccidentes.contains(filtrarTiposAccidentes.get(indice))) {
                        modificarTiposAccidentes.add(filtrarTiposAccidentes.get(indice));
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
                if (filtrarTiposAccidentes.get(indice).getCodigo() == null) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    filtrarTiposAccidentes.get(indice).setCodigo(backUpCodigo);
                    banderita = false;
                } else {

                    for (int j = 0; j < filtrarTiposAccidentes.size(); j++) {
                        if (j != indice) {
                            if (filtrarTiposAccidentes.get(indice).getCodigo().equals(filtrarTiposAccidentes.get(j).getCodigo())) {
                                contador++;
                            }
                        }
                    }
                    if (contador > 0) {
                        mensajeValidacion = "CODIGOS REPETIDOS";
                        filtrarTiposAccidentes.get(indice).setCodigo(backUpCodigo);
                        banderita = false;
                    } else {
                        banderita = true;
                    }

                }

                if (filtrarTiposAccidentes.get(indice).getNombre().isEmpty()) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita = false;
                    filtrarTiposAccidentes.get(indice).setNombre(backUpDescripcion);
                }
                if (filtrarTiposAccidentes.get(indice).getNombre().equals(" ")) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita = false;
                    filtrarTiposAccidentes.get(indice).setNombre(backUpDescripcion);
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
            RequestContext.getCurrentInstance().update("form:datosTiposAccidentes");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }

    }

    public void borrandoTiposAccidentes() {
        if (tiposAccidentesSeleccionado != null) {
            log.info("Entro a borrandoTiposAccidentes");
            if (!modificarTiposAccidentes.isEmpty() && modificarTiposAccidentes.contains(tiposAccidentesSeleccionado)) {
                int modIndex = modificarTiposAccidentes.indexOf(tiposAccidentesSeleccionado);
                modificarTiposAccidentes.remove(modIndex);
                borrarTiposAccidentes.add(tiposAccidentesSeleccionado);
            } else if (!crearTiposAccidentes.isEmpty() && crearTiposAccidentes.contains(tiposAccidentesSeleccionado)) {
                int crearIndex = crearTiposAccidentes.indexOf(tiposAccidentesSeleccionado);
                crearTiposAccidentes.remove(crearIndex);
            } else {
                borrarTiposAccidentes.add(tiposAccidentesSeleccionado);
            }
            listTiposAccidentes.remove(tiposAccidentesSeleccionado);
            if (tipoLista == 1) {
                filtrarTiposAccidentes.remove(tiposAccidentesSeleccionado);

            }
            tiposAccidentesSeleccionado = null;
            contarRegistros();
            guardado = false;
            RequestContext.getCurrentInstance().update("form:datosTiposAccidentes");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void verificarBorrado() {
        log.info("Estoy en verificarBorrado");
        BigInteger contarAccidentesTipoAccidente;
        BigInteger contarSoAccidentesMedicosTipoAccidente;
        try {
            contarAccidentesTipoAccidente = administrarTiposAccidentes.contarAccidentesTipoAccidente(tiposAccidentesSeleccionado.getSecuencia());
            contarSoAccidentesMedicosTipoAccidente = administrarTiposAccidentes.contarSoAccidentesMedicosTipoAccidente(tiposAccidentesSeleccionado.getSecuencia());
            if (!contarAccidentesTipoAccidente.equals(new BigInteger("0")) || !contarSoAccidentesMedicosTipoAccidente.equals(new BigInteger("0"))) {
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:validacionBorrar");
                RequestContext.getCurrentInstance().execute("PF('validacionBorrar').show()");
                contarAccidentesTipoAccidente = new BigInteger("-1");
                contarSoAccidentesMedicosTipoAccidente = new BigInteger("-1");
            } else {
                borrandoTiposAccidentes();
            }
        } catch (Exception e) {
            log.error("ERROR ControlTiposAccidentes verificarBorrado ERROR  ", e);
        }
    }

    public void revisarDialogoGuardar() {

        if (!borrarTiposAccidentes.isEmpty() || !crearTiposAccidentes.isEmpty() || !modificarTiposAccidentes.isEmpty()) {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:confirmarGuardar");
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
        }
    }

    public void guardarTiposAccidentes() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (guardado == false) {
            log.info("Realizando guardarTiposAccidentes");
            if (!borrarTiposAccidentes.isEmpty()) {
                administrarTiposAccidentes.borrarTiposAccidentes(borrarTiposAccidentes);
                //mostrarBorrados
                registrosBorrados = borrarTiposAccidentes.size();
                RequestContext.getCurrentInstance().update("form:mostrarBorrados");
                RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
                borrarTiposAccidentes.clear();
            }
            if (!modificarTiposAccidentes.isEmpty()) {
                administrarTiposAccidentes.modificarTiposAccidentes(modificarTiposAccidentes);
                modificarTiposAccidentes.clear();
            }
            if (!crearTiposAccidentes.isEmpty()) {
                administrarTiposAccidentes.crearTiposAccidentes(crearTiposAccidentes);
                crearTiposAccidentes.clear();
            }
            log.info("Se guardaron los datos con exito");
            listTiposAccidentes = null;
            RequestContext.getCurrentInstance().update("form:datosTiposAccidentes");
            k = 0;
            guardado = true;
            FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }
        index = -1;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");

    }

    public void editarCelda() {
        if (tiposAccidentesSeleccionado != null) {
            editarTiposAccidentes = tiposAccidentesSeleccionado;
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

    public void agregarNuevoTiposAccidentes() {
        log.info("agregarNuevoTiposAccidentes");
        int contador = 0;
        int duplicados = 0;

        Integer a = 0;
        a = null;
        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();
        if (nuevoTiposAccidentes.getCodigo() == null) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios.";
        } else {
            for (int x = 0; x < listTiposAccidentes.size(); x++) {
                if (listTiposAccidentes.get(x).getCodigo().equals(nuevoTiposAccidentes.getCodigo())) {
                    duplicados++;
                }
            }

            if (duplicados > 0) {
                mensajeValidacion = "El código ingresado ya está registrado. Por favor ingrese un código válido";
            } else {
                contador++;
            }
        }
        if (nuevoTiposAccidentes.getNombre() == (null) || nuevoTiposAccidentes.getNombre().isEmpty()) {
            mensajeValidacion = " Los campos marcados con asterisco son obligatorios ";
        } else {
            contador++;
        }

        if (contador == 2) {
            if (bandera == 1) {
                //CERRAR FILTRADO
                log.info("Desactivar");
                codigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTiposAccidentes:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTiposAccidentes:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosTiposAccidentes");
                bandera = 0;
                filtrarTiposAccidentes = null;
                tipoLista = 0;
                tamano = 335;
            }
            k++;
            l = BigInteger.valueOf(k);
            nuevoTiposAccidentes.setSecuencia(l);
            crearTiposAccidentes.add(nuevoTiposAccidentes);
            listTiposAccidentes.add(0,nuevoTiposAccidentes);
            tiposAccidentesSeleccionado = nuevoTiposAccidentes;
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().execute("PF('nuevoRegistroTiposAccidentes').hide()");
            RequestContext.getCurrentInstance().update("form:datosTiposAccidentes");
            nuevoTiposAccidentes = new TiposAccidentes();

        } else {
            RequestContext.getCurrentInstance().update("form:validacionNuevaCentroCosto");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevaCentroCosto').show()");
            contador = 0;
        }
    }

    public void limpiarNuevoTiposAccidentes() {
        nuevoTiposAccidentes = new TiposAccidentes();
    }

    //------------------------------------------------------------------------------
    public void duplicandoTiposAccidentes() {
        if (tiposAccidentesSeleccionado != null) {
            duplicarTiposAccidentes = new TiposAccidentes();
            k++;
            l = BigInteger.valueOf(k);
            duplicarTiposAccidentes.setSecuencia(l);
            duplicarTiposAccidentes.setCodigo(tiposAccidentesSeleccionado.getCodigo());
            duplicarTiposAccidentes.setNombre(tiposAccidentesSeleccionado.getNombre());
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTE");
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroTiposAccidentes').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void confirmarDuplicar() {
        int contador = 0;
        mensajeValidacion = " ";
        int duplicados = 0;
//        a = null;
        if (duplicarTiposAccidentes.getCodigo() == null) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
        } else {
            for (int x = 0; x < listTiposAccidentes.size(); x++) {
                if (listTiposAccidentes.get(x).getCodigo() == duplicarTiposAccidentes.getCodigo()) {
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
        if (duplicarTiposAccidentes.getNombre() == null || duplicarTiposAccidentes.getNombre().isEmpty()) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
        } else {
            contador++;
        }
        if (contador == 2) {
            listTiposAccidentes.add(0,duplicarTiposAccidentes);
            crearTiposAccidentes.add(duplicarTiposAccidentes);
            RequestContext.getCurrentInstance().update("form:datosTiposAccidentes");
            tiposAccidentesSeleccionado = duplicarTiposAccidentes;
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            if (bandera == 1) {
                //CERRAR FILTRADO
                codigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTiposAccidentes:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTiposAccidentes:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosTiposAccidentes");
                bandera = 0;
                filtrarTiposAccidentes = null;
                tipoLista = 0;
                tamano = 335;
            }
            duplicarTiposAccidentes = new TiposAccidentes();
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroTiposAccidentes').hide()");

        } else {
            contador = 0;
            RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
            RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
        }
    }

    public void limpiarDuplicarTiposAccidentes() {
        duplicarTiposAccidentes = new TiposAccidentes();
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTiposAccidentesExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "TIPOSACCIDENTES", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTiposAccidentesExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "TIPOSACCIDENTES", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
      log.info("lol");
      if (!listTiposAccidentes.isEmpty()) {
         if (tiposAccidentesSeleccionado != null) {
            log.info("lol 2");
            int resultado = administrarRastros.obtenerTabla(tiposAccidentesSeleccionado.getSecuencia(), "TIPOSACCIDENTES"); //En ENCARGATURAS lo cambia por el nombre de su tabla
            log.info("resultado: " + resultado);
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
         } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
         }
        } else if (administrarRastros.verificarHistoricosTabla("TIPOSACCIDENTES")) { // igual acá
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
    }

    public void contarRegistros() {
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
    }

    //*/*/*/*/*/*/*/*/*/*-/-*//-*/-*/*/*-*/-*/-*/*/*/*/*/---/*/*/*/*/-*/-*/-*/-*/-*/
    public List<TiposAccidentes> getListTiposAccidentes() {
        if (listTiposAccidentes == null) {
            listTiposAccidentes = administrarTiposAccidentes.consultarTiposAccidentes();
        }
        return listTiposAccidentes;
    }

    public void setListTiposAccidentes(List<TiposAccidentes> listTiposAccidentes) {
        this.listTiposAccidentes = listTiposAccidentes;
    }

    public List<TiposAccidentes> getFiltrarTiposAccidentes() {
        return filtrarTiposAccidentes;
    }

    public void setFiltrarTiposAccidentes(List<TiposAccidentes> filtrarTiposAccidentes) {
        this.filtrarTiposAccidentes = filtrarTiposAccidentes;
    }

    public TiposAccidentes getNuevoTiposAccidentes() {
        return nuevoTiposAccidentes;
    }

    public void setNuevoTiposAccidentes(TiposAccidentes nuevoTiposAccidentes) {
        this.nuevoTiposAccidentes = nuevoTiposAccidentes;
    }

    public TiposAccidentes getDuplicarTiposAccidentes() {
        return duplicarTiposAccidentes;
    }

    public void setDuplicarTiposAccidentes(TiposAccidentes duplicarTiposAccidentes) {
        this.duplicarTiposAccidentes = duplicarTiposAccidentes;
    }

    public TiposAccidentes getEditarTiposAccidentes() {
        return editarTiposAccidentes;
    }

    public void setEditarTiposAccidentes(TiposAccidentes editarTiposAccidentes) {
        this.editarTiposAccidentes = editarTiposAccidentes;
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

    public TiposAccidentes getTiposAccidentesSeleccionado() {
        return tiposAccidentesSeleccionado;
    }

    public void setTiposAccidentesSeleccionado(TiposAccidentes clasesPensionesSeleccionado) {
        this.tiposAccidentesSeleccionado = clasesPensionesSeleccionado;
    }

    public String getInfoRegistro() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosTiposAccidentes");
        infoRegistro = String.valueOf(tabla.getRowCount());
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

}
