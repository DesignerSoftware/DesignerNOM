/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.VigenciasRetenciones;
import Entidades.Retenciones;
import Exportar.ExportarPDFTablasAnchas;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarRastrosInterface;
import InterfaceAdministrar.AdministrarRetencionesInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import ControlNavegacion.ControlListaNavegacion;
import Exportar.ExportarPDF;
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
public class ControlRetencion implements Serializable {

    private static Logger log = Logger.getLogger(ControlRetencion.class);

    @EJB
    AdministrarRetencionesInterface administrarRetenciones;
    @EJB
    AdministrarRastrosInterface administrarRastros;

    //Lista Vigencias Retenciones (Arriba)
    private List<VigenciasRetenciones> listaVigenciasRetenciones;
    private List<VigenciasRetenciones> filtradoListaVigenciasRetenciones;
    private List<VigenciasRetenciones> listaVigenciasRetencionesModificar;
    private List<VigenciasRetenciones> listaVigenciasRetencionesCrear;
    private List<VigenciasRetenciones> listaVigenciasRetencionesBorrar;
    private VigenciasRetenciones vigenciaRetencionSeleccionado;
    public VigenciasRetenciones nuevoVigenciasRetenciones;
    public VigenciasRetenciones duplicarVigenciasRetenciones;
    private VigenciasRetenciones editarVigenciasRetenciones;
    //Lista Retenciones
    private List<Retenciones> listaRetenciones;
    private List<Retenciones> filtradoListaRetenciones;
    private Retenciones retencionSeleccionado;
    private int registroActual;
    private boolean aceptar, mostrarTodos;
    private String altoScrollVigenciasRetenciones, altoScrollRetenciones;
    private int k;
    private BigInteger l;
    private String mensajeValidacion;
    private List<Retenciones> listaRetencionesCrear;
    public Retenciones nuevoRetencion;
    public Retenciones duplicarRetencion;
    private List<Retenciones> listaRetencionesModificar;
    private List<Retenciones> listaRetencionesBorrar;
    private int tipoActualizacion;
    private int bandera;
    private Retenciones editarRetenciones;
    private int cualCelda, tipoLista;
    private int cualCeldaD;
    private int tipoListaD;
    private boolean guardado;
    private boolean cambiosPagina;
    private Column vCodigo, vFechaVigencia, vUvt;
    private Column rValorMinimo, rValorMaximo, rPorcentaje, rValor, rAdicionarUvt;
    private String tablaImprimir, nombreArchivo;
    private int m;
    private BigInteger n;
    private BigInteger secuenciaVigenciaRetencion;
    private Date fechaParametro;
    private String paginaAnterior = "nominaf";
    private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();
    private String msgError;
    private String infoRegistroVigencias, infoRegistroRetenciones;
    private Integer cualTabla;

    public ControlRetencion() {
        cualTabla = 0;
        bandera = 0;
        registroActual = 0;
        mostrarTodos = true;
        altoScrollVigenciasRetenciones = "125";
        altoScrollRetenciones = "145";
        listaVigenciasRetencionesBorrar = new ArrayList<VigenciasRetenciones>();
        listaVigenciasRetencionesCrear = new ArrayList<VigenciasRetenciones>();
        listaVigenciasRetencionesModificar = new ArrayList<VigenciasRetenciones>();
        listaRetencionesBorrar = new ArrayList<Retenciones>();
        listaRetencionesCrear = new ArrayList<Retenciones>();
        listaRetencionesModificar = new ArrayList<Retenciones>();
        tablaImprimir = ":formExportar:datosVigenciasRetencionesExportar";
        nombreArchivo = "VigenciasRetencionesXML";
        nuevoVigenciasRetenciones = new VigenciasRetenciones();
        nuevoRetencion = new Retenciones();
        m = 0;
        cambiosPagina = true;
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
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarRetenciones.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
            listaVigenciasRetenciones = null;
            getListaVigenciasRetenciones();
            if (listaVigenciasRetenciones != null) {
                if (!listaVigenciasRetenciones.isEmpty()) {
                    vigenciaRetencionSeleccionado = listaVigenciasRetenciones.get(0);
                    secuenciaVigenciaRetencion = vigenciaRetencionSeleccionado.getSecuencia();
                    listaRetenciones = null;
                    getListaRetenciones();
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
        String pagActual = "retencion";
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

    //CREAR Vigencia Retencion
    public void agregarNuevoVigencia() {
        int pasa = 0;
        int pasar = 0;

        mensajeValidacion = new String();

        RequestContext context = RequestContext.getCurrentInstance();

        if (nuevoVigenciasRetenciones.getCodigo() == null) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            pasa++;
        }
        if (nuevoVigenciasRetenciones.getFechavigencia() == null) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            pasa++;
        }

        if (nuevoVigenciasRetenciones.getUvt() == null) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            pasa++;
        }

        for (int i = 0; i < listaVigenciasRetenciones.size(); i++) {
            if (nuevoVigenciasRetenciones.getCodigo() == listaVigenciasRetenciones.get(i).getCodigo()) {
                RequestContext.getCurrentInstance().update("formularioDialogos:codigos");
                RequestContext.getCurrentInstance().execute("PF('codigos').show()");
                pasar++;
            }
        }

        if (pasa != 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevo");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevo').show()");
        }

        if (pasa == 0 && pasar == 0) {
            if (bandera == 1) {
                vCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasRetenciones:vCodigo");
                vCodigo.setFilterStyle("display: none; visibility: hidden;");
                vFechaVigencia = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasRetenciones:vFechaVigencia");
                vFechaVigencia.setFilterStyle("display: none; visibility: hidden;");
                vUvt = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasRetenciones:vUvt");
                vUvt.setFilterStyle("display: none; visibility: hidden;");
                altoScrollVigenciasRetenciones = "125";
                RequestContext.getCurrentInstance().update("form:datosVigenciasRetenciones");
                bandera = 0;
                filtradoListaVigenciasRetenciones = null;
                tipoLista = 0;
            }
            k++;
            l = BigInteger.valueOf(k);
            nuevoVigenciasRetenciones.setSecuencia(l);
            cambiosPagina = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            listaVigenciasRetencionesCrear.add(nuevoVigenciasRetenciones);
            listaVigenciasRetenciones.add(0, nuevoVigenciasRetenciones);
            vigenciaRetencionSeleccionado = nuevoVigenciasRetenciones;
            RequestContext.getCurrentInstance().update("form:datosVigenciasRetenciones");
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().execute("PF('NuevoRegistroVigenciasRetenciones').hide()");
            RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroVigenciasRetenciones");
            nuevoVigenciasRetenciones = new VigenciasRetenciones();
        }
    }

    //EVENTO FILTRAR
    public void eventoFiltrar() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
        contarRegistros();
    }

    //EVENTO FILTRAR
    public void eventoFiltrarD() {
        if (tipoListaD == 0) {
            tipoListaD = 1;
        }
        contarRegistrosRetenciones();
    }

    public void contarRegistros() {
        RequestContext.getCurrentInstance().update("form:infoRegistroVigencias");
    }

    public void contarRegistrosRetenciones() {
        RequestContext.getCurrentInstance().update("form:infoRegistroRetenciones");
    }

    //Ubicacion Celda Arriba 
    public void cambiarVigencia() {
        secuenciaVigenciaRetencion = vigenciaRetencionSeleccionado.getSecuencia();
        listaRetenciones = null;
        getListaRetenciones();
        contarRegistrosRetenciones();
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosRetenciones");
    }

    //BORRAR RETENCION / TABLA RETENCION
    public void borrar() {

        if (vigenciaRetencionSeleccionado != null && cualTabla == 0) {
            if (listaRetenciones.isEmpty()) {
                if (!listaVigenciasRetencionesModificar.isEmpty() && listaVigenciasRetencionesModificar.contains(vigenciaRetencionSeleccionado)) {
                    int modIndex = listaVigenciasRetencionesModificar.indexOf(vigenciaRetencionSeleccionado);
                    listaVigenciasRetencionesModificar.remove(modIndex);
                    listaVigenciasRetencionesBorrar.add(vigenciaRetencionSeleccionado);
                } else if (!listaVigenciasRetencionesCrear.isEmpty() && listaVigenciasRetencionesCrear.contains(vigenciaRetencionSeleccionado)) {
                    int crearIndex = listaVigenciasRetencionesCrear.indexOf(vigenciaRetencionSeleccionado);
                    listaVigenciasRetencionesCrear.remove(crearIndex);
                } else {
                    listaVigenciasRetencionesBorrar.add(vigenciaRetencionSeleccionado);
                }
                listaVigenciasRetenciones.remove(vigenciaRetencionSeleccionado);

                if (tipoLista == 1) {
                    filtradoListaVigenciasRetenciones.remove(vigenciaRetencionSeleccionado);
                }

                RequestContext context = RequestContext.getCurrentInstance();
                vigenciaRetencionSeleccionado = null;
                contarRegistros();
                RequestContext.getCurrentInstance().update("form:datosVigenciasRetenciones");
                cambiosPagina = false;
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            } else {
                log.info("No se puede borrar porque tiene registros en la tabla de abajo");
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("formularioDialogos:registro");
                RequestContext.getCurrentInstance().execute("PF('registro').show()");
            }
        } else if (retencionSeleccionado != null && cualTabla == 1) {

            if (!listaRetencionesModificar.isEmpty() && listaRetencionesModificar.contains(retencionSeleccionado)) {
                int modIndex = listaRetencionesModificar.indexOf(retencionSeleccionado);
                listaRetencionesModificar.remove(modIndex);
                listaRetencionesBorrar.add(retencionSeleccionado);
            } else if (!listaRetencionesCrear.isEmpty() && listaRetencionesCrear.contains(retencionSeleccionado)) {
                int crearIndex = listaRetencionesCrear.indexOf(retencionSeleccionado);
                listaRetencionesCrear.remove(crearIndex);
            } else {
                listaRetencionesBorrar.add(retencionSeleccionado);
            }
            listaRetenciones.remove(retencionSeleccionado);

            if (tipoListaD == 1) {
                filtradoListaRetenciones.remove(retencionSeleccionado);
            }

            RequestContext context = RequestContext.getCurrentInstance();
            retencionSeleccionado = null;
            contarRegistrosRetenciones();
            RequestContext.getCurrentInstance().update("form:datosRetenciones");
            cambiosPagina = false;
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
    }

    public void activarCtrlF11() {
        if (bandera == 0) {
            vCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasRetenciones:vCodigo");
            vCodigo.setFilterStyle("width: 85% !important;");
            vFechaVigencia = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasRetenciones:vFechaVigencia");
            vFechaVigencia.setFilterStyle("width: 85% !important;");
            vUvt = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasRetenciones:vUvt");
            vUvt.setFilterStyle("width: 85% !important;");
            altoScrollVigenciasRetenciones = "105";
            rValorMinimo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRetenciones:rValorMinimo");
            rValorMinimo.setFilterStyle("width: 85% !important;");
            rValorMaximo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRetenciones:rValorMaximo");
            rValorMaximo.setFilterStyle("width: 85% !important;");
            rPorcentaje = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRetenciones:rPorcentaje");
            rPorcentaje.setFilterStyle("width: 85% !important;");
            rValor = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRetenciones:rValor");
            rValor.setFilterStyle("");
            rAdicionarUvt = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRetenciones:rAdicionarUvt");
            rAdicionarUvt.setFilterStyle("");
            altoScrollRetenciones = "125";
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosRetenciones");
            bandera = 1;
            tipoListaD = 1;
            tipoLista = 1;
            RequestContext.getCurrentInstance().update("form:datosVigenciasRetenciones");

        } else if (bandera == 1) {
            vCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasRetenciones:vCodigo");
            vCodigo.setFilterStyle("display: none; visibility: hidden;");
            vFechaVigencia = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasRetenciones:vFechaVigencia");
            vFechaVigencia.setFilterStyle("display: none; visibility: hidden;");
            vUvt = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasRetenciones:vUvt");
            vUvt.setFilterStyle("display: none; visibility: hidden;");
            altoScrollVigenciasRetenciones = "125";
            bandera = 0;
            tipoLista = 0;
            filtradoListaVigenciasRetenciones = null;
            rValorMinimo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRetenciones:rValorMinimo");
            rValorMinimo.setFilterStyle("display: none; visibility: hidden;");
            rValorMaximo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRetenciones:rValorMaximo");
            rValorMaximo.setFilterStyle("display: none; visibility: hidden;");
            rPorcentaje = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRetenciones:rPorcentaje");
            rPorcentaje.setFilterStyle("display: none; visibility: hidden;");
            rValor = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRetenciones:rValor");
            rValor.setFilterStyle("display: none; visibility: hidden;");
            rAdicionarUvt = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRetenciones:rAdicionarUvt");
            rAdicionarUvt.setFilterStyle("display: none; visibility: hidden;");
            filtradoListaRetenciones = null;
            altoScrollRetenciones = "145";
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosVigenciasRetenciones");
            RequestContext.getCurrentInstance().update("form:datosRetenciones");
            bandera = 0;
            tipoListaD = 0;
        }
    }

    public void salir() {
        limpiarListasValor();
        if (bandera == 1) {
            vCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasRetenciones:vCodigo");
            vCodigo.setFilterStyle("display: none; visibility: hidden;");
            vFechaVigencia = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasRetenciones:vFechaVigencia");
            vFechaVigencia.setFilterStyle("display: none; visibility: hidden;");
            vUvt = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasRetenciones:vUvt");
            vUvt.setFilterStyle("display: none; visibility: hidden;");
            altoScrollVigenciasRetenciones = "125";
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosVigenciasRetenciones");
            bandera = 0;
            filtradoListaVigenciasRetenciones = null;
            altoScrollVigenciasRetenciones = "125";
            bandera = 0;
            filtradoListaVigenciasRetenciones = null;
            tipoLista = 0;
        }
        if (bandera == 1) {
            rValorMinimo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRetenciones:rValorMinimo");
            rValorMinimo.setFilterStyle("display: none; visibility: hidden;");
            rValorMaximo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRetenciones:rValorMaximo");
            rValorMaximo.setFilterStyle("display: none; visibility: hidden;");
            rPorcentaje = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRetenciones:rPorcentaje");
            rPorcentaje.setFilterStyle("display: none; visibility: hidden;");
            rValor = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRetenciones:rValor");
            rValor.setFilterStyle("display: none; visibility: hidden;");
            rAdicionarUvt = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRetenciones:rAdicionarUvt");
            rAdicionarUvt.setFilterStyle("display: none; visibility: hidden;");
            altoScrollRetenciones = "145";
            bandera = 0;
            tipoListaD = 0;
            filtradoListaRetenciones = null;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosRetenciones");
            bandera = 0;
            filtradoListaRetenciones = null;
            tipoListaD = 0;
        }
        listaVigenciasRetencionesBorrar.clear();
        listaVigenciasRetencionesCrear.clear();
        listaVigenciasRetencionesModificar.clear();
        vigenciaRetencionSeleccionado = null;
        listaVigenciasRetenciones = null;
        listaRetencionesBorrar.clear();
        listaRetencionesCrear.clear();
        listaRetencionesModificar.clear();
        retencionSeleccionado = null;
        listaRetenciones = null;
        guardado = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosVigenciasRetenciones");
        RequestContext.getCurrentInstance().update("form:datosRetenciones");
        navegar("atras");
    }

    //CANCELAR MODIFICACIONES
    public void cancelarModificacion() {
        if (bandera == 1) {
            vCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasRetenciones:vCodigo");
            vCodigo.setFilterStyle("display: none; visibility: hidden;");
            vFechaVigencia = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasRetenciones:vFechaVigencia");
            vFechaVigencia.setFilterStyle("display: none; visibility: hidden;");
            vUvt = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasRetenciones:vUvt");
            vUvt.setFilterStyle("display: none; visibility: hidden;");
            altoScrollVigenciasRetenciones = "125";
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosVigenciasRetenciones");
            bandera = 0;
            filtradoListaVigenciasRetenciones = null;
            altoScrollVigenciasRetenciones = "125";
            bandera = 0;
            filtradoListaVigenciasRetenciones = null;
            tipoLista = 0;
        }
        if (bandera == 1) {
            rValorMinimo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRetenciones:rValorMinimo");
            rValorMinimo.setFilterStyle("display: none; visibility: hidden;");
            rValorMaximo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRetenciones:rValorMaximo");
            rValorMaximo.setFilterStyle("display: none; visibility: hidden;");
            rPorcentaje = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRetenciones:rPorcentaje");
            rPorcentaje.setFilterStyle("display: none; visibility: hidden;");
            rValor = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRetenciones:rValor");
            rValor.setFilterStyle("display: none; visibility: hidden;");
            rAdicionarUvt = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRetenciones:rAdicionarUvt");
            rAdicionarUvt.setFilterStyle("display: none; visibility: hidden;");
            altoScrollRetenciones = "145";
            RequestContext.getCurrentInstance().update("form:datosRetenciones");
            bandera = 0;
            filtradoListaRetenciones = null;
            tipoListaD = 0;
        }
        listaVigenciasRetencionesBorrar.clear();
        listaVigenciasRetencionesCrear.clear();
        listaVigenciasRetencionesModificar.clear();
        vigenciaRetencionSeleccionado = null;
        listaVigenciasRetenciones = null;
        getListaVigenciasRetenciones();
        contarRegistros();
        listaRetencionesBorrar.clear();
        listaRetencionesCrear.clear();
        listaRetencionesModificar.clear();
        listaRetenciones = null;
        retencionSeleccionado = null;
        guardado = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosVigenciasRetenciones");
        RequestContext.getCurrentInstance().update("form:datosRetenciones");
    }

    //GUARDAR
    public void guardarCambiosRetenciones() {
        try {

            if (guardado == false) {
                msgError = "";
                if (!listaVigenciasRetencionesBorrar.isEmpty()) {
                    for (int i = 0; i < listaVigenciasRetencionesBorrar.size(); i++) {
                        if (listaVigenciasRetencionesBorrar.get(i).getUvt() == null) {
                            listaVigenciasRetencionesBorrar.get(i).setUvt(null);
                        }
                        msgError = administrarRetenciones.borrarVigenciaRetencion(listaVigenciasRetencionesBorrar.get(i));
                        listaVigenciasRetencionesBorrar.clear();
                    }
                }
                if (!listaVigenciasRetencionesCrear.isEmpty()) {
                    for (int i = 0; i < listaVigenciasRetencionesCrear.size(); i++) {
                        if (listaVigenciasRetencionesCrear.get(i).getUvt() == null) {
                            listaVigenciasRetencionesCrear.get(i).setUvt(null);
                        }
                        msgError = administrarRetenciones.crearVigenciaRetencion(listaVigenciasRetencionesCrear.get(i));
                    }
                    listaVigenciasRetencionesCrear.clear();
                }
                if (!listaVigenciasRetencionesModificar.isEmpty()) {
                    for (int i = 0; i < listaVigenciasRetencionesModificar.size(); i++) {
                        if (listaVigenciasRetencionesModificar.get(i).getUvt() == null) {
                            listaVigenciasRetencionesModificar.get(i).setUvt(null);
                        }
                        msgError = administrarRetenciones.modificarVigenciaRetencion(listaVigenciasRetencionesModificar.get(i));
                    }
                    listaVigenciasRetencionesModificar.clear();
                }
                if (!listaRetencionesBorrar.isEmpty()) {
                    for (int i = 0; i < listaRetencionesBorrar.size(); i++) {
                        if (listaRetencionesBorrar.get(i).getAdicionauvt() == null) {
                            listaRetencionesBorrar.get(i).setAdicionauvt(null);
                        }
                        msgError = administrarRetenciones.borrarRetencion(listaRetencionesBorrar.get(i));
                    }
                    listaRetencionesBorrar.clear();
                }
                if (!listaRetencionesCrear.isEmpty()) {
                    for (int i = 0; i < listaRetencionesCrear.size(); i++) {
                        if (listaRetencionesCrear.get(i).getAdicionauvt() == null) {
                            listaRetencionesCrear.get(i).setAdicionauvt(null);
                        }
                        msgError = administrarRetenciones.crearRetencion(listaRetencionesCrear.get(i));
                    }
                    listaRetencionesCrear.clear();
                }
                if (!listaRetencionesModificar.isEmpty()) {
                    for (int i = 0; i < listaRetencionesModificar.size(); i++) {
                        if (listaRetencionesModificar.get(i).getAdicionauvt() == null) {
                            listaRetencionesModificar.get(i).setAdicionauvt(null);
                        }
                        msgError = administrarRetenciones.modificarRetencion(listaRetencionesModificar.get(i));
                    }
                    listaRetencionesModificar.clear();
                }

                if (msgError.equals("EXITO")) {

                    listaVigenciasRetenciones = null;
                    listaRetenciones = null;
                    guardado = true;
                    getListaVigenciasRetenciones();
                    contarRegistros();
                    contarRegistrosRetenciones();
                    RequestContext.getCurrentInstance().update("form:growl");
                    RequestContext.getCurrentInstance().update("form:datosVigenciasRetenciones");
                    RequestContext.getCurrentInstance().update("form:datosRetenciones");
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                    FacesMessage msg = new FacesMessage("Información", "Se han guardado los datos exitosamente.");
                    FacesContext.getCurrentInstance().addMessage(null, msg);
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

//CREAR Retencion
    public void agregarNuevoRetencion() {
        int pasa = 0;
        int pasar = 0;
        mensajeValidacion = new String();

        RequestContext context = RequestContext.getCurrentInstance();

        if (nuevoRetencion.getValorminimo() == null) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            pasa++;
        }

        if (nuevoRetencion.getValormaximo() == null) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            pasa++;
        }

        if (nuevoRetencion.getPorcentaje() == null) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            pasa++;
        }

        if (nuevoRetencion.getValor() == null) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            pasa++;
        }

        if (nuevoRetencion.getValorminimo() != null && nuevoRetencion.getValormaximo() != null) {
            if (nuevoRetencion.getValorminimo().compareTo(nuevoRetencion.getValormaximo()) == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:valores");
                RequestContext.getCurrentInstance().execute("PF('valores').show()");
                pasar++;
            }
        }

        if (nuevoRetencion.getValorminimo() != null && nuevoRetencion.getValormaximo() != null) {
            if (nuevoRetencion.getValorminimo().compareTo(nuevoRetencion.getValormaximo()) == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:valores2");
                RequestContext.getCurrentInstance().execute("PF('valores2').show()");
                pasar++;
            }
        }

        if (nuevoRetencion.getValor() != null) {
            if (nuevoRetencion.getValor().compareTo(BigDecimal.ZERO) == -1) {
                log.info("Valor es menor a 0");
                RequestContext.getCurrentInstance().update("formularioDialogos:valores3");
                RequestContext.getCurrentInstance().execute("PF('valores3').show()");
                pasar++;
            }
        }

        if (pasa != 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevo");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevo').show()");
        }

        if (pasa == 0 && pasar == 0) {
            if (bandera == 1) {
                rValorMinimo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRetenciones:rValorMinimo");
                rValorMinimo.setFilterStyle("display: none; visibility: hidden;");
                rValorMaximo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRetenciones:rValorMaximo");
                rValorMaximo.setFilterStyle("display: none; visibility: hidden;");
                rPorcentaje = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRetenciones:rPorcentaje");
                rPorcentaje.setFilterStyle("display: none; visibility: hidden;");
                rValor = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRetenciones:rValor");
                rValor.setFilterStyle("display: none; visibility: hidden;");
                rAdicionarUvt = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRetenciones:rAdicionarUvt");
                rAdicionarUvt.setFilterStyle("display: none; visibility: hidden;");
                altoScrollRetenciones = "145";
                RequestContext.getCurrentInstance().update("form:datosRetenciones");
                bandera = 0;
                filtradoListaRetenciones = null;
                tipoListaD = 0;
            }
            k++;
            l = BigInteger.valueOf(k);
            nuevoRetencion.setSecuencia(l);
            nuevoRetencion.setVigencia(vigenciaRetencionSeleccionado);
            cambiosPagina = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            listaRetencionesCrear.add(nuevoRetencion);
            listaRetenciones.add(0, nuevoRetencion);
            retencionSeleccionado = nuevoRetencion;
            contarRegistrosRetenciones();
            RequestContext.getCurrentInstance().update("form:datosRetenciones");
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().execute("PF('NuevoRegistroRetenciones').hide()");
            nuevoRetencion = new Retenciones();
            RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroRetenciones");
        }
    }

    public void confirmarDuplicar() {
        int pasa = 0;
        for (int i = 0; i < listaVigenciasRetenciones.size(); i++) {
            if (duplicarVigenciasRetenciones.getCodigo() == listaVigenciasRetenciones.get(i).getCodigo()) {
                RequestContext.getCurrentInstance().update("formularioDialogos:valores");
                RequestContext.getCurrentInstance().execute("PF('valores').show()");
                pasa++;
            }
        }

        if (pasa == 0) {
            listaVigenciasRetenciones.add(duplicarVigenciasRetenciones);
            listaVigenciasRetencionesCrear.add(0, duplicarVigenciasRetenciones);
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:datosVigenciasRetenciones");
            vigenciaRetencionSeleccionado = duplicarVigenciasRetenciones;
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            if (bandera == 1) {
                vCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasRetenciones:vCodigo");
                vCodigo.setFilterStyle("display: none; visibility: hidden;");
                vFechaVigencia = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasRetenciones:vFechaVigencia");
                vFechaVigencia.setFilterStyle("display: none; visibility: hidden;");
                vUvt = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasRetenciones:vUvt");
                vUvt.setFilterStyle("display: none; visibility: hidden;");
                altoScrollVigenciasRetenciones = "125";
                RequestContext.getCurrentInstance().update("form:datosVigenciasRetenciones");
                bandera = 0;
                filtradoListaVigenciasRetenciones = null;
                tipoLista = 0;
            }
        }
        duplicarVigenciasRetenciones = new VigenciasRetenciones();
    }

    public void confirmarDuplicarD() {

        listaRetenciones.add(duplicarRetencion);
        listaRetencionesCrear.add(duplicarRetencion);
        retencionSeleccionado = duplicarRetencion;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosRetenciones");
        guardado = false;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        if (bandera == 1) {
            rValorMinimo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRetenciones:rValorMinimo");
            rValorMinimo.setFilterStyle("display: none; visibility: hidden;");
            rValorMaximo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRetenciones:rValorMaximo");
            rValorMaximo.setFilterStyle("display: none; visibility: hidden;");
            rPorcentaje = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRetenciones:rPorcentaje");
            rPorcentaje.setFilterStyle("display: none; visibility: hidden;");
            rValor = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRetenciones:rValor");
            rValor.setFilterStyle("display: none; visibility: hidden;");
            rAdicionarUvt = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRetenciones:rAdicionarUvt");
            rAdicionarUvt.setFilterStyle("display: none; visibility: hidden;");
            altoScrollRetenciones = "145";
            RequestContext.getCurrentInstance().update("form:datosRetenciones");
            bandera = 0;
            filtradoListaRetenciones = null;
            tipoListaD = 0;
        }

        duplicarRetencion = new Retenciones();
    }

    //DUPLICAR VIGENCIAS RETENCIONES/RETENCIONES
    public void duplicarE() {
        if (cualTabla == 0) {
            duplicarVigenciasRetenciones = new VigenciasRetenciones();
            k++;
            l = BigInteger.valueOf(k);

            duplicarVigenciasRetenciones.setSecuencia(l);
            duplicarVigenciasRetenciones.setCodigo(vigenciaRetencionSeleccionado.getCodigo());
            duplicarVigenciasRetenciones.setFechavigencia(vigenciaRetencionSeleccionado.getFechavigencia());
            duplicarVigenciasRetenciones.setUvt(vigenciaRetencionSeleccionado.getUvt());

            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarVigenciaRetencion");
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroVigenciasRetenciones').show()");
        } else if (cualTabla == 1) {
            duplicarRetencion = new Retenciones();
            m++;
            n = BigInteger.valueOf(m);
            duplicarRetencion.setSecuencia(n);
            duplicarRetencion.setValorminimo(retencionSeleccionado.getValorminimo());
            duplicarRetencion.setValormaximo(retencionSeleccionado.getValormaximo());
            duplicarRetencion.setPorcentaje(retencionSeleccionado.getPorcentaje());
            duplicarRetencion.setValor(retencionSeleccionado.getValor());
            duplicarRetencion.setAdicionauvt(retencionSeleccionado.getAdicionauvt());
            duplicarRetencion.setVigencia(retencionSeleccionado.getVigencia());
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarRetencion");
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroRetencion').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    //MOSTRAR DATOS CELDA
    public void editarCelda() {
        if (vigenciaRetencionSeleccionado != null && cualTabla == 0) {
            editarVigenciasRetenciones = vigenciaRetencionSeleccionado;
            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarCodigoVR");
                RequestContext.getCurrentInstance().execute("PF('editarCodigoVR').show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaVigenciaVR");
                RequestContext.getCurrentInstance().execute("PF('editarFechaVigenciaVR').show()");
                cualCelda = -1;
            } else if (cualCelda == 2) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarUvtVR");
                RequestContext.getCurrentInstance().execute("PF('editarUvtVR').show()");
                cualCelda = -1;
            }
        } else if (retencionSeleccionado != null && cualTabla == 1) {
            editarRetenciones = retencionSeleccionado;
            if (cualCeldaD == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarValorMinimoR");
                RequestContext.getCurrentInstance().execute("PF('editarValorMinimoR').show()");
                cualCeldaD = -1;
            } else if (cualCeldaD == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarValorMaximoR");
                RequestContext.getCurrentInstance().execute("PF('editarValorMaximoR').show()");
                cualCeldaD = -1;
            } else if (cualCeldaD == 2) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarPorcentajeR");
                RequestContext.getCurrentInstance().execute("PF('editarPorcentajeR').show()");
                cualCeldaD = -1;
            } else if (cualCeldaD == 3) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarValorR");
                RequestContext.getCurrentInstance().execute("PF('editarValorR').show()");
                cualCeldaD = -1;
            } else if (cualCeldaD == 4) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarAdicionaR");
                RequestContext.getCurrentInstance().execute("PF('editarAdicionaR').show()");
                cualCeldaD = -1;
            }
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void dialogoVigencias() {
        cualTabla = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroVigenciasRetenciones");
        RequestContext.getCurrentInstance().execute("PF('NuevoRegistroVigenciasRetenciones').show()");
    }

    public void dialogoRetenciones() {
        cualTabla = 1;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroRetenciones");
        RequestContext.getCurrentInstance().execute("PF('NuevoRegistroRetenciones').show()");
    }

    public boolean validarFechasRegistro(int i) {
        fechaParametro = new Date();
        fechaParametro.setYear(0);
        fechaParametro.setMonth(1);
        fechaParametro.setDate(1);
        boolean retorno = true;
        if (i == 0) {
            VigenciasRetenciones auxiliar = null;
            auxiliar = vigenciaRetencionSeleccionado;
        }
        return retorno;
    }

    //AUTOCOMPLETAR Vigencias
    public void modificarVigenciasRetenciones(VigenciasRetenciones vr) {
        vigenciaRetencionSeleccionado = vr;
        if (!listaVigenciasRetencionesCrear.contains(vigenciaRetencionSeleccionado)) {
            if (listaVigenciasRetencionesModificar.isEmpty()) {
                listaVigenciasRetencionesModificar.add(vigenciaRetencionSeleccionado);
            } else if (!listaVigenciasRetencionesModificar.contains(vigenciaRetencionSeleccionado)) {
                listaVigenciasRetencionesModificar.add(vigenciaRetencionSeleccionado);
            }
        }
        guardado = false;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("form:datosVigenciasRetenciones");
    }

    //AUTOCOMPLETAR Retenciones
    public void modificarRetenciones(Retenciones retencion) {
        retencionSeleccionado = retencion;
        if (!listaRetencionesCrear.contains(retencionSeleccionado)) {
            if (listaRetencionesModificar.isEmpty()) {
                listaRetencionesModificar.add(retencionSeleccionado);
            } else if (!listaRetencionesModificar.contains(retencionSeleccionado)) {
                listaRetencionesModificar.add(retencionSeleccionado);
            }
        }
        guardado = false;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("form:datosRetenciones");

    }

    //UBICACION CELDA
    public void cambiarIndice(VigenciasRetenciones vr, int celda) {
        vigenciaRetencionSeleccionado = vr;
        cualCelda = celda;
        cualTabla = 0;
        vigenciaRetencionSeleccionado.getSecuencia();
        tablaImprimir = ":formExportar:datosVigenciasRetencionesExportar";
        nombreArchivo = "VigenciasRetencionesXML";
        if (cualCelda == 0) {
            vigenciaRetencionSeleccionado.getCodigo();
        } else if (cualCelda == 1) {
            vigenciaRetencionSeleccionado.getFechavigencia();
        } else if (cualCelda == 2) {
            vigenciaRetencionSeleccionado.getUvt();
        }
        cambiarVigencia();
    }

    //UBICACION CELDA
    public void cambiarIndiceD(Retenciones retencion, int celda) {
        retencionSeleccionado = retencion;
        cualCeldaD = celda;
        cualTabla = 1;
        tablaImprimir = ":formExportar:datosRetencionesExportar";
        nombreArchivo = "RetencionesXML";
        retencionSeleccionado.getSecuencia();
        if (cualCeldaD == 0) {
            retencionSeleccionado.getValorminimo();
        } else if (cualCeldaD == 1) {
            retencionSeleccionado.getValormaximo();
        } else if (cualCeldaD == 2) {
            retencionSeleccionado.getPorcentaje();
        } else if (cualCeldaD == 3) {
            retencionSeleccionado.getValor();
        } else if (cualCeldaD == 4) {
            retencionSeleccionado.getAdicionauvt();
        }
    }

    //EXPORTAR
    public void exportPDF() throws IOException {
        if (cualTabla == 0) {
            DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosVigenciasRetencionesExportar");
            FacesContext context = FacesContext.getCurrentInstance();
            Exporter exporter = new ExportarPDF();
            exporter.export(context, tabla, "VigenciasRetencionesPDF", false, false, "UTF-8", null, null);
            context.responseComplete();
        } else if (cualTabla == 1) {
            DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosRetencionesExportar");
            FacesContext context = FacesContext.getCurrentInstance();
            Exporter exporter = new ExportarPDF();
            exporter.export(context, tabla, "RetencionesPDF", false, false, "UTF-8", null, null);
            context.responseComplete();
        }
    }

    public void exportXLS() throws IOException {
        if (cualTabla == 0) {
            DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosVigenciasRetencionesExportar");
            FacesContext context = FacesContext.getCurrentInstance();
            Exporter exporter = new ExportarXLS();
            exporter.export(context, tabla, "VigenciasRetencionesXLS", false, false, "UTF-8", null, null);
            context.responseComplete();
        } else if (cualTabla == 1) {
            DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosRetencionesExportar");
            FacesContext context = FacesContext.getCurrentInstance();
            Exporter exporter = new ExportarXLS();
            exporter.export(context, tabla, "RetencionesXLS", false, false, "UTF-8", null, null);
            context.responseComplete();
        }
    }

    public void limpiarNuevoVigencia() {
        nuevoVigenciasRetenciones = new VigenciasRetenciones();
    }

    public void limpiarNuevoRetencion() {
        nuevoRetencion = new Retenciones();
    }

    public void limpiarduplicarVigencia() {
        duplicarVigenciasRetenciones = new VigenciasRetenciones();
    }

    public void limpiarduplicarRetencion() {
        duplicarRetencion = new Retenciones();
    }

    public void verificarRastro() {
        if (cualTabla == 0) {
            if (vigenciaRetencionSeleccionado != null) {
                int resultado = administrarRastros.obtenerTabla(vigenciaRetencionSeleccionado.getSecuencia(), "VIGENCIASRETENCIONES");
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
            } else if (administrarRastros.verificarHistoricosTabla("VIGENCIASRETENCIONES")) {
                RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
            } else {
                RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
            }
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            if (retencionSeleccionado != null) {
                log.info("NF2");
                int resultadoNF = administrarRastros.obtenerTabla(retencionSeleccionado.getSecuencia(), "RETENCIONES");
                log.info("resultado: " + resultadoNF);
                if (resultadoNF == 1) {
                    RequestContext.getCurrentInstance().execute("PF('errorObjetosDBNF').show()");
                } else if (resultadoNF == 2) {
                    RequestContext.getCurrentInstance().execute("PF('confirmarRastroNF').show()");
                } else if (resultadoNF == 3) {
                    RequestContext.getCurrentInstance().execute("PF('errorRegistroRastroNF').show()");
                } else if (resultadoNF == 4) {
                    RequestContext.getCurrentInstance().execute("PF('errorTablaConRastroNF').show()");
                } else if (resultadoNF == 5) {
                    RequestContext.getCurrentInstance().execute("PF('errorTablaSinRastroNF').show()");
                }
            } else if (administrarRastros.verificarHistoricosTabla("RETENCIONES")) {
                RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistoricoNF').show()");
            } else {
                RequestContext.getCurrentInstance().execute("PF('errorRastroHistoricoNF').show()");
            }
        }

    }
    
    public void guardarSalir(){
        guardarCambiosRetenciones();
        salir();
    }

    //Getter & Setter
    public List<VigenciasRetenciones> getListaVigenciasRetenciones() {
        if (listaVigenciasRetenciones == null) {
            listaVigenciasRetenciones = administrarRetenciones.consultarVigenciasRetenciones();
        }
        return listaVigenciasRetenciones;
    }

    public void setListaVigenciasRetenciones(List<VigenciasRetenciones> listaVigenciasRetenciones) {
        this.listaVigenciasRetenciones = listaVigenciasRetenciones;
    }

    public List<VigenciasRetenciones> getFiltradoListaVigenciasRetenciones() {
        return filtradoListaVigenciasRetenciones;
    }

    public void setFiltradoListaVigenciasRetenciones(List<VigenciasRetenciones> filtradoListaVigenciasRetenciones) {
        this.filtradoListaVigenciasRetenciones = filtradoListaVigenciasRetenciones;
    }

    public VigenciasRetenciones getVigenciaRetencionSeleccionado() {
        return vigenciaRetencionSeleccionado;
    }

    public void setVigenciaRetencionSeleccionado(VigenciasRetenciones vigenciaRetencionSeleccionado) {
        this.vigenciaRetencionSeleccionado = vigenciaRetencionSeleccionado;
    }

    public int getRegistroActual() {
        return registroActual;
    }

    public void setRegistroActual(int registroActual) {
        this.registroActual = registroActual;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public void setAceptar(boolean aceptar) {
        this.aceptar = aceptar;
    }

    public List<Retenciones> getListaRetenciones() {
        if (listaRetenciones == null && vigenciaRetencionSeleccionado != null) {
            listaRetenciones = administrarRetenciones.consultarRetenciones(secuenciaVigenciaRetencion);
        }
        return listaRetenciones;
    }

    public void setListaRetenciones(List<Retenciones> listaRetenciones) {
        this.listaRetenciones = listaRetenciones;
    }

    public List<Retenciones> getFiltradoListaRetenciones() {
        return filtradoListaRetenciones;
    }

    public void setFiltradoListaRetenciones(List<Retenciones> filtradoListaRetenciones) {
        this.filtradoListaRetenciones = filtradoListaRetenciones;
    }

    public Retenciones getRetencionSeleccionado() {
        return retencionSeleccionado;
    }

    public void setRetencionSeleccionado(Retenciones retencionSeleccionado) {
        this.retencionSeleccionado = retencionSeleccionado;
    }

    public String getAltoScrollVigenciasRetenciones() {
        return altoScrollVigenciasRetenciones;
    }

    public void setAltoScrollVigenciasRetenciones(String altoScrollVigenciasRetenciones) {
        this.altoScrollVigenciasRetenciones = altoScrollVigenciasRetenciones;
    }

    public String getAltoScrollRetenciones() {
        return altoScrollRetenciones;
    }

    public void setAltoScrollRetenciones(String altoScrollRetenciones) {
        this.altoScrollRetenciones = altoScrollRetenciones;
    }

    public VigenciasRetenciones getEditarVigenciasRetenciones() {
        return editarVigenciasRetenciones;
    }

    public void setEditarVigenciasRetenciones(VigenciasRetenciones editarVigenciasRetenciones) {
        this.editarVigenciasRetenciones = editarVigenciasRetenciones;
    }

    public Retenciones getEditarRetenciones() {
        return editarRetenciones;
    }

    public void setEditarRetenciones(Retenciones editarRetenciones) {
        this.editarRetenciones = editarRetenciones;
    }

    public String getTablaImprimir() {
        return tablaImprimir;
    }

    public void setTablaImprimir(String tablaImprimir) {
        this.tablaImprimir = tablaImprimir;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public VigenciasRetenciones getNuevoVigenciasRetenciones() {
        return nuevoVigenciasRetenciones;
    }

    public void setNuevoVigenciasRetenciones(VigenciasRetenciones nuevoVigenciasRetenciones) {
        this.nuevoVigenciasRetenciones = nuevoVigenciasRetenciones;
    }

    public Retenciones getNuevoRetencion() {
        return nuevoRetencion;
    }

    public void setNuevoRetencion(Retenciones nuevoRetencion) {
        this.nuevoRetencion = nuevoRetencion;
    }

    public String getMensajeValidacion() {
        return mensajeValidacion;
    }

    public void setMensajeValidacion(String mensajeValidacion) {
        this.mensajeValidacion = mensajeValidacion;
    }

    public VigenciasRetenciones getDuplicarVigenciasRetenciones() {
        return duplicarVigenciasRetenciones;
    }

    public void setDuplicarVigenciasRetenciones(VigenciasRetenciones duplicarVigenciasRetenciones) {
        this.duplicarVigenciasRetenciones = duplicarVigenciasRetenciones;
    }

    public Retenciones getDuplicarRetencion() {
        return duplicarRetencion;
    }

    public void setDuplicarRetencion(Retenciones duplicarRetencion) {
        this.duplicarRetencion = duplicarRetencion;
    }

    public boolean isCambiosPagina() {
        return cambiosPagina;
    }

    public void setCambiosPagina(boolean cambiosPagina) {
        this.cambiosPagina = cambiosPagina;
    }

    public boolean isMostrarTodos() {
        return mostrarTodos;
    }

    public void setMostrarTodos(boolean mostrarTodos) {
        this.mostrarTodos = mostrarTodos;
    }

    public boolean isGuardado() {
        return guardado;
    }

    public void setGuardado(boolean guardado) {
        this.guardado = guardado;
    }

    public String getMsgError() {
        return msgError;
    }

    public void setMsgError(String msgError) {
        this.msgError = msgError;
    }

    public String getInfoRegistroVigencias() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosVigenciasRetenciones");
        infoRegistroVigencias = String.valueOf(tabla.getRowCount());
        return infoRegistroVigencias;
    }

    public void setInfoRegistroVigencias(String infoRegistroVigencias) {
        this.infoRegistroVigencias = infoRegistroVigencias;
    }

    public String getInfoRegistroRetenciones() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosRetenciones");
        infoRegistroRetenciones = String.valueOf(tabla.getRowCount());
        return infoRegistroRetenciones;
    }

    public void setInfoRegistroRetenciones(String infoRegistroRetenciones) {
        this.infoRegistroRetenciones = infoRegistroRetenciones;
    }
    
}
