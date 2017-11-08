/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.ValoresConceptos;
import Entidades.Conceptos;
import InterfaceAdministrar.AdministrarValoresConceptosInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import ControlNavegacion.ControlListaNavegacion;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import java.io.IOException;
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
public class ControlValoresConceptos implements Serializable {

    private static Logger log = Logger.getLogger(ControlValoresConceptos.class);

    @EJB
    AdministrarValoresConceptosInterface administrarValoresConceptos;
    @EJB
    AdministrarRastrosInterface administrarRastros;
    private List<ValoresConceptos> listValoresConceptos;
    private List<ValoresConceptos> filtrarValoresConceptos;
    private List<ValoresConceptos> crearValoresConceptos;
    private List<ValoresConceptos> modificarValoresConceptos;
    private List<ValoresConceptos> borrarValoresConceptos;
    private ValoresConceptos nuevoValoresConceptos;
    private ValoresConceptos duplicarValoresConceptos;
    private ValoresConceptos editarValoresConceptos;
    private ValoresConceptos valorConceptoSeleccionado;
    //otros
    private int cualCelda, tipoLista, tipoActualizacion, k, bandera;
    private BigInteger l;
    private boolean aceptar, guardado, activarLov;
    private Column descripcion, naturaleza, valor;
    //borrado
    private int registrosBorrados;
    private String mensajeValidacion;
    //filtrado table
    private int tamano;
    //---------------------------------
    private List<Conceptos> lovConceptos;
    private List<Conceptos> filtradoConceptos;
    private Conceptos conceptolovSeleccionado;
    private String paginaAnterior;
    private String msgError;
    private String infoRegistro, infoRegistroLovConcepto;
    private Map<String, Object> mapParametros;

    public ControlValoresConceptos() {
        listValoresConceptos = null;
        crearValoresConceptos = new ArrayList<ValoresConceptos>();
        modificarValoresConceptos = new ArrayList<ValoresConceptos>();
        borrarValoresConceptos = new ArrayList<ValoresConceptos>();
        editarValoresConceptos = new ValoresConceptos();
        nuevoValoresConceptos = new ValoresConceptos();
        nuevoValoresConceptos.setConcepto(new Conceptos());
        duplicarValoresConceptos = new ValoresConceptos();
        duplicarValoresConceptos.setConcepto(new Conceptos());
        lovConceptos = null;
        filtradoConceptos = null;
        guardado = true;
        tamano = 330;
        aceptar = true;
        paginaAnterior = "nominaf";
        mapParametros = new LinkedHashMap<String, Object>();
        mapParametros.put("paginaAnterior", paginaAnterior);
        activarLov = true;
    }

    public void limpiarListasValor() {
        lovConceptos = null;
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
            administrarValoresConceptos.obtenerConexion(ses.getId());
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

    public void navegar(String pag) {
        FacesContext fc = FacesContext.getCurrentInstance();
        ControlListaNavegacion controlListaNavegacion = (ControlListaNavegacion) fc.getApplication().evaluateExpressionGet(fc, "#{controlListaNavegacion}", ControlListaNavegacion.class);
        String pagActual = "valoresconceptos";
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

    public void contarRegistrosLOV() {
        RequestContext.getCurrentInstance().update("form:infoRegistroLov");
    }

    public void cambiarIndice(ValoresConceptos valorc, int celda) {
        valorConceptoSeleccionado = valorc;
        cualCelda = celda;
        valorConceptoSeleccionado.getSecuencia();
        if (cualCelda == 0) {
            valorConceptoSeleccionado.getConcepto().getDescripcion();
            habilitarBotonLov();
        } else if (cualCelda == 1) {
            valorConceptoSeleccionado.getConcepto().getNaturalezaConcepto();
            habilitarBotonLov();
        } else if (cualCelda == 2) {
            valorConceptoSeleccionado.getValorunitario();
            deshabilitarBotonLov();
        }
    }

    public void asignarIndex(ValoresConceptos valorc, int LND, int dig) {
        try {
            valorConceptoSeleccionado = valorc;
            tipoActualizacion = LND;
            if (dig == 0) {
                lovConceptos = null;
                getLovConceptos();
                contarRegistrosLOV();
                RequestContext.getCurrentInstance().update("form:conceptosDialogo");
                RequestContext.getCurrentInstance().execute("PF('conceptosDialogo').show()");
                dig = -1;
            }
        } catch (Exception e) {
            log.warn("Error ControlValoresConceptos.asignarIndex ERROR======" + e.getMessage());
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void listaValoresBoton() {
        if (valorConceptoSeleccionado != null) {
            if (cualCelda == 0) {
                RequestContext context = RequestContext.getCurrentInstance();
                lovConceptos = null;
                getLovConceptos();
                contarRegistrosLOV();
                RequestContext.getCurrentInstance().update("form:conceptosDialogo");
                RequestContext.getCurrentInstance().execute("PF('conceptosDialogo').show()");
            }
        }
    }

    public void cancelarModificacion() {
        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            descripcion = (Column) c.getViewRoot().findComponent("form:datosValoresConceptos:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            naturaleza = (Column) c.getViewRoot().findComponent("form:datosValoresConceptos:naturaleza");
            naturaleza.setFilterStyle("display: none; visibility: hidden;");
            valor = (Column) c.getViewRoot().findComponent("form:datosValoresConceptos:valor");
            valor.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosValoresConceptos");
            bandera = 0;
            filtrarValoresConceptos = null;
            tipoLista = 0;
            tamano = 330;
        }

        borrarValoresConceptos.clear();
        crearValoresConceptos.clear();
        modificarValoresConceptos.clear();
        valorConceptoSeleccionado = null;
        k = 0;
        listValoresConceptos = null;
        guardado = true;
        getListValoresConceptos();
        contarRegistros();
        RequestContext.getCurrentInstance().update("form:datosValoresConceptos");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void salir() {
        limpiarListasValor();
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 1) {
            descripcion = (Column) c.getViewRoot().findComponent("form:datosValoresConceptos:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            naturaleza = (Column) c.getViewRoot().findComponent("form:datosValoresConceptos:naturaleza");
            naturaleza.setFilterStyle("display: none; visibility: hidden;");
            valor = (Column) c.getViewRoot().findComponent("form:datosValoresConceptos:valor");
            valor.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosValoresConceptos");
            bandera = 0;
            filtrarValoresConceptos = null;
            tipoLista = 0;
            tamano = 330;
        }

        borrarValoresConceptos.clear();
        crearValoresConceptos.clear();
        modificarValoresConceptos.clear();
        valorConceptoSeleccionado = null;
        k = 0;
        listValoresConceptos = null;
        guardado = true;
        getListValoresConceptos();
        contarRegistros();
        RequestContext.getCurrentInstance().update("form:datosValoresConceptos");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        navegar("atras");
    }

    public void guardarSalir() {
        guardarValoresConceptos();
        salir();
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            tamano = 310;
            descripcion = (Column) c.getViewRoot().findComponent("form:datosValoresConceptos:descripcion");
            descripcion.setFilterStyle("width: 85% !important;");
            naturaleza = (Column) c.getViewRoot().findComponent("form:datosValoresConceptos:naturaleza");
            naturaleza.setFilterStyle("width: 85% !important");
            valor = (Column) c.getViewRoot().findComponent("form:datosValoresConceptos:valor");
            valor.setFilterStyle("width: 85% !important;");
            RequestContext.getCurrentInstance().update("form:datosValoresConceptos");
            bandera = 1;
        } else if (bandera == 1) {
            tamano = 330;
            descripcion = (Column) c.getViewRoot().findComponent("form:datosValoresConceptos:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            naturaleza = (Column) c.getViewRoot().findComponent("form:datosValoresConceptos:naturaleza");
            naturaleza.setFilterStyle("display: none; visibility: hidden;");
            valor = (Column) c.getViewRoot().findComponent("form:datosValoresConceptos:valor");
            valor.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosValoresConceptos");
            bandera = 0;
            filtrarValoresConceptos = null;
            tipoLista = 0;
        }
    }

    public void actualizarConceptos() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            valorConceptoSeleccionado.setConcepto(conceptolovSeleccionado);
            if (!crearValoresConceptos.contains(valorConceptoSeleccionado)) {
                if (modificarValoresConceptos.isEmpty()) {
                    modificarValoresConceptos.add(valorConceptoSeleccionado);
                } else if (!modificarValoresConceptos.contains(valorConceptoSeleccionado)) {
                    modificarValoresConceptos.add(valorConceptoSeleccionado);
                }
            }
            guardado = false;
            RequestContext.getCurrentInstance().update("form:datosValoresConceptos");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        } else if (tipoActualizacion == 1) {
            nuevoValoresConceptos.setConcepto(conceptolovSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoValorConcepto");
        } else if (tipoActualizacion == 2) {
            duplicarValoresConceptos.setConcepto(conceptolovSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarValorConcepto");
        }
        filtradoConceptos = null;
        conceptolovSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        RequestContext.getCurrentInstance().update("form:lovConceptos");
        RequestContext.getCurrentInstance().update("form:conceptosDialogo");
        RequestContext.getCurrentInstance().update("form:aceptarPer");
        context.reset("form:lovConceptos:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovConceptos').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('conceptosDialogo').hide()");

    }

    public void cancelarCambioConceptos() {
        filtradoConceptos = null;
        conceptolovSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:lovConceptos");
        RequestContext.getCurrentInstance().update("form:conceptosDialogo");
        RequestContext.getCurrentInstance().update("form:aceptarPer");
        context.reset("form:lovConceptos:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovConceptos').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('conceptosDialogo').hide()");
    }

    public void modificarValoresConceptos(ValoresConceptos valorc) {
        valorConceptoSeleccionado = valorc;
        if (!crearValoresConceptos.contains(valorConceptoSeleccionado)) {
            if (modificarValoresConceptos.isEmpty()) {
                modificarValoresConceptos.add(valorConceptoSeleccionado);
            } else if (!modificarValoresConceptos.contains(valorConceptoSeleccionado)) {
                modificarValoresConceptos.add(valorConceptoSeleccionado);
            }
            guardado = false;
        }
        RequestContext.getCurrentInstance().update("form:datosValoresConceptos");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void borrandoValoresConceptos() {

        if (valorConceptoSeleccionado != null) {
            if (!modificarValoresConceptos.isEmpty() && modificarValoresConceptos.contains(valorConceptoSeleccionado)) {
                int modIndex = modificarValoresConceptos.indexOf(valorConceptoSeleccionado);
                modificarValoresConceptos.remove(modIndex);
                borrarValoresConceptos.add(valorConceptoSeleccionado);
            } else if (!crearValoresConceptos.isEmpty() && crearValoresConceptos.contains(valorConceptoSeleccionado)) {
                int crearIndex = crearValoresConceptos.indexOf(valorConceptoSeleccionado);
                crearValoresConceptos.remove(crearIndex);
            } else {
                borrarValoresConceptos.add(valorConceptoSeleccionado);
            }
            listValoresConceptos.remove(valorConceptoSeleccionado);
            if (tipoLista == 1) {
                filtrarValoresConceptos.remove(valorConceptoSeleccionado);

            }
            valorConceptoSeleccionado = null;
            guardado = false;
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:datosValoresConceptos");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }

    }

    public void revisarDialogoGuardar() {
        if (!borrarValoresConceptos.isEmpty() || !crearValoresConceptos.isEmpty() || !modificarValoresConceptos.isEmpty()) {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:confirmarGuardar");
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
        }
    }

    public void guardarValoresConceptos() {
        try {
            if (guardado == false) {
                msgError = "";
                if (!borrarValoresConceptos.isEmpty()) {
                    for (int i = 0; i < borrarValoresConceptos.size(); i++) {
                        msgError = administrarValoresConceptos.borrarValoresConceptos(borrarValoresConceptos.get(i));
                    }
                    borrarValoresConceptos.clear();
                }
                if (!modificarValoresConceptos.isEmpty()) {
                    for (int i = 0; i < modificarValoresConceptos.size(); i++) {
                        msgError = administrarValoresConceptos.modificarValoresConceptos(modificarValoresConceptos.get(i));
                    }
                    modificarValoresConceptos.clear();
                }
                if (!crearValoresConceptos.isEmpty()) {
                    for (int i = 0; i < crearValoresConceptos.size(); i++) {
                        msgError = administrarValoresConceptos.crearValoresConceptos(crearValoresConceptos.get(i));
                    }
                    crearValoresConceptos.clear();
                }

                if (msgError.equals("EXITO")) {
                    listValoresConceptos = null;
                    getListValoresConceptos();
                    contarRegistros();
                    k = 0;
                    valorConceptoSeleccionado = null;
                    FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                    RequestContext.getCurrentInstance().update("form:growl");
                    RequestContext.getCurrentInstance().update("form:datosValoresConceptos");
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
        if (valorConceptoSeleccionado != null) {
            editarValoresConceptos = valorConceptoSeleccionado;
            RequestContext context = RequestContext.getCurrentInstance();
            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editPais");
                RequestContext.getCurrentInstance().execute("PF('editPais').show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editConceptos");
                RequestContext.getCurrentInstance().execute("PF('editConceptos').show()");
                cualCelda = -1;
            } else if (cualCelda == 2) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editOperandos");
                RequestContext.getCurrentInstance().execute("PF('editOperandos').show()");
                cualCelda = -1;
            }

        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void agregarNuevoValoresConceptos() {
        int contador = 0;
        int duplicados = 0;

        Integer a = 0;
        a = null;
        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();

        if (nuevoValoresConceptos.getConcepto().getDescripcion() == null || nuevoValoresConceptos.getConcepto().getDescripcion().isEmpty()) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
        } else {
            for (int i = 0; i < listValoresConceptos.size(); i++) {
                if (nuevoValoresConceptos.getConcepto() == listValoresConceptos.get(i).getConcepto()) {
                    duplicados++;
                }
            }
            if (duplicados > 0) {
                mensajeValidacion = "El código ingresado ya está registrado. Por favor ingrese un código válido";
            } else {
                contador++;
            }
        }
        if (nuevoValoresConceptos.getValorunitario() == null) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
        } else {
            contador++;
        }

        if (duplicados > 0) {
            mensajeValidacion = "Existe un registro asociado al concepto ingresado. Por favor ingrese un concepto válido.";
        }

        if (contador == 2) {
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                descripcion = (Column) c.getViewRoot().findComponent("form:datosValoresConceptos:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                naturaleza = (Column) c.getViewRoot().findComponent("form:datosValoresConceptos:naturaleza");
                naturaleza.setFilterStyle("display: none; visibility: hidden;");
                valor = (Column) c.getViewRoot().findComponent("form:datosValoresConceptos:valor");
                valor.setFilterStyle("display: none; visibility: hidden;");
                bandera = 0;
                filtrarValoresConceptos = null;
                tipoLista = 0;
                tamano = 330;
            }
            k++;
            l = BigInteger.valueOf(k);
            nuevoValoresConceptos.setSecuencia(l);
            crearValoresConceptos.add(nuevoValoresConceptos);
            listValoresConceptos.add(0, nuevoValoresConceptos);
            valorConceptoSeleccionado = nuevoValoresConceptos;
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:datosValoresConceptos");
            RequestContext.getCurrentInstance().execute("PF('nuevoRegistroValoresConceptos').hide()");
            nuevoValoresConceptos = new ValoresConceptos();
            nuevoValoresConceptos.setConcepto(new Conceptos());
        } else {
            RequestContext.getCurrentInstance().update("form:validacionNuevaCentroCosto");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevaCentroCosto').show()");
            contador = 0;
        }
    }

    public void limpiarNuevoValoresConceptos() {
        nuevoValoresConceptos = new ValoresConceptos();
        nuevoValoresConceptos.setConcepto(new Conceptos());
    }

    public void duplicandoValoresConceptos() {
        if (valorConceptoSeleccionado != null) {
            duplicarValoresConceptos = new ValoresConceptos();
            duplicarValoresConceptos.setConcepto(new Conceptos());
            k++;
            l = BigInteger.valueOf(k);
            duplicarValoresConceptos.setSecuencia(l);
            duplicarValoresConceptos.setConcepto(valorConceptoSeleccionado.getConcepto());
            duplicarValoresConceptos.setValorunitario(valorConceptoSeleccionado.getValorunitario());
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarValorConcepto");
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroValoresConceptos').show()");
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

        if (duplicarValoresConceptos.getConcepto().getDescripcion() == null || duplicarValoresConceptos.getConcepto().getDescripcion().isEmpty()) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
        } else {
            for (int i = 0; i < listValoresConceptos.size(); i++) {
                if (duplicarValoresConceptos.getConcepto() == listValoresConceptos.get(i).getConcepto()) {
                    duplicados++;
                }
            }
            if (duplicados > 0) {
                mensajeValidacion = "El código ingresado ya está registrado. Por favor ingrese un código válido";
            } else {
                contador++;
            }
        }
        if (duplicarValoresConceptos.getValorunitario() == null) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
        } else {
            contador++;//1
        }

        if (duplicados > 0) {
            mensajeValidacion = "Existe un registro asociado al concepto ingresado. Por favor ingrese un concepto válido.";
        }

        if (contador == 2) {
            crearValoresConceptos.add(duplicarValoresConceptos);
            listValoresConceptos.add(0, duplicarValoresConceptos);
            valorConceptoSeleccionado = duplicarValoresConceptos;
            contarRegistros();
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosValoresConceptos");
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                descripcion = (Column) c.getViewRoot().findComponent("form:datosValoresConceptos:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                naturaleza = (Column) c.getViewRoot().findComponent("form:datosValoresConceptos:naturaleza");
                naturaleza.setFilterStyle("display: none; visibility: hidden;");
                valor = (Column) c.getViewRoot().findComponent("form:datosValoresConceptos:valor");
                valor.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosValoresConceptos");
                bandera = 0;
                filtrarValoresConceptos = null;
                tipoLista = 0;
                tamano = 330;
            }
            duplicarValoresConceptos = new ValoresConceptos();
            duplicarValoresConceptos.setConcepto(new Conceptos());
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroValoresConceptos').hide()");
        } else {
            RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
            RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
        }
    }

    public void limpiarDuplicarValoresConceptos() {
        duplicarValoresConceptos = new ValoresConceptos();
        duplicarValoresConceptos.setConcepto(new Conceptos());
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosValoresConceptosExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "VALORESCONCEPTOS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosValoresConceptosExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "VALORESCONCEPTOS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (valorConceptoSeleccionado != null) {
            int resultado = administrarRastros.obtenerTabla(valorConceptoSeleccionado.getSecuencia(), "VALORESCONCEPTOS"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
        } else if (administrarRastros.verificarHistoricosTabla("VALORESCONCEPTOS")) { // igual acá
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
    }

    public void habilitarBotonLov() {
        activarLov = false;
        RequestContext.getCurrentInstance().update("form:listaValores");
    }

    public void deshabilitarBotonLov() {
        activarLov = true;
        RequestContext.getCurrentInstance().update("form:listaValores");
    }

/////////////GETS Y SETS/////////////
    public List<ValoresConceptos> getListValoresConceptos() {
        if (listValoresConceptos == null) {
            listValoresConceptos = administrarValoresConceptos.consultarValoresConceptos();
        }
        return listValoresConceptos;
    }

    public void setListValoresConceptos(List<ValoresConceptos> listValoresConceptos) {
        this.listValoresConceptos = listValoresConceptos;
    }

    public List<ValoresConceptos> getFiltrarValoresConceptos() {
        return filtrarValoresConceptos;
    }

    public void setFiltrarValoresConceptos(List<ValoresConceptos> filtrarValoresConceptos) {
        this.filtrarValoresConceptos = filtrarValoresConceptos;
    }

    public ValoresConceptos getNuevoValoresConceptos() {
        return nuevoValoresConceptos;
    }

    public void setNuevoValoresConceptos(ValoresConceptos nuevoValoresConceptos) {
        this.nuevoValoresConceptos = nuevoValoresConceptos;
    }

    public ValoresConceptos getValorConceptoSeleccionado() {
        return valorConceptoSeleccionado;
    }

    public void setValorConceptoSeleccionado(ValoresConceptos valorConceptoSeleccionado) {
        this.valorConceptoSeleccionado = valorConceptoSeleccionado;
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

    public String getMensajeValidacion() {
        return mensajeValidacion;
    }

    public void setMensajeValidacion(String mensajeValidacion) {
        this.mensajeValidacion = mensajeValidacion;
    }

    public List<Conceptos> getLovConceptos() {
        if (lovConceptos == null) {
            lovConceptos = administrarValoresConceptos.consultarLOVConceptos();
        }
        return lovConceptos;
    }

    public void setLovConceptos(List<Conceptos> lovConceptos) {
        this.lovConceptos = lovConceptos;
    }

    public List<Conceptos> getFiltradoConceptos() {
        return filtradoConceptos;
    }

    public void setFiltradoConceptos(List<Conceptos> filtradoConceptos) {
        this.filtradoConceptos = filtradoConceptos;
    }

    public Conceptos getConceptolovSeleccionado() {
        return conceptolovSeleccionado;
    }

    public void setConceptolovSeleccionado(Conceptos conceptolovSeleccionado) {
        this.conceptolovSeleccionado = conceptolovSeleccionado;
    }

    public String getMsgError() {
        return msgError;
    }

    public void setMsgError(String msgError) {
        this.msgError = msgError;
    }

    public String getInfoRegistro() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosValoresConceptos");
        infoRegistro = String.valueOf(tabla.getRowCount());
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

    public String getInfoRegistroLovConcepto() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovConceptos");
        infoRegistroLovConcepto = String.valueOf(tabla.getRowCount());
        return infoRegistroLovConcepto;
    }

    public void setInfoRegistroLovConcepto(String infoRegistroLovConcepto) {
        this.infoRegistroLovConcepto = infoRegistroLovConcepto;
    }

    public int getTamano() {
        return tamano;
    }

    public void setTamano(int tamano) {
        this.tamano = tamano;
    }

    public ValoresConceptos getDuplicarValoresConceptos() {
        return duplicarValoresConceptos;
    }

    public void setDuplicarValoresConceptos(ValoresConceptos duplicarValoresConceptos) {
        this.duplicarValoresConceptos = duplicarValoresConceptos;
    }

    public ValoresConceptos getEditarValoresConceptos() {
        return editarValoresConceptos;
    }

    public void setEditarValoresConceptos(ValoresConceptos editarValoresConceptos) {
        this.editarValoresConceptos = editarValoresConceptos;
    }

    public boolean isActivarLov() {
        return activarLov;
    }

    public void setActivarLov(boolean activarLov) {
        this.activarLov = activarLov;
    }
}
