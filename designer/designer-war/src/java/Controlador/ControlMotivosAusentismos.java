/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import ControlNavegacion.ControlListaNavegacion;
import Entidades.MotivosAusentismos;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarMotivosAusentismosInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
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
@Named(value = "controlMotivosAusentismos")
@SessionScoped
public class ControlMotivosAusentismos implements Serializable {

    private static Logger log = Logger.getLogger(ControlMotivosAusentismos.class);

    @EJB
    AdministrarMotivosAusentismosInterface administrarMotivosAusentismos;
    @EJB
    AdministrarRastrosInterface administrarRastros;

    private List<MotivosAusentismos> listMotivosAusentismos;
    private List<MotivosAusentismos> filtrarMotivosAusentismos;
    private List<MotivosAusentismos> crearMotivosAusentismos;
    private List<MotivosAusentismos> modificarMotivosAusentismos;
    private List<MotivosAusentismos> borrarMotivosAusentismos;
    private MotivosAusentismos nuevoMotivoAusentismo;
    private MotivosAusentismos duplicarMotivoAusentismo;
    private MotivosAusentismos editarMotivoAusentismo;
    private MotivosAusentismos motivoAusentismoSeleccionado;
    //otros
    private int cualCelda, tipoLista, tipoActualizacion, k, bandera;
    private BigInteger l;
    private boolean aceptar, guardado;
    private Column codigo, descripcion, remunerado;
    private String altoTabla;
    //borrado
    private String infoRegistro;
    private String mensajeValidacion;
    private String paginaAnterior = "nominaf";
    private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();
    private String msgError;

    public ControlMotivosAusentismos() {
        listMotivosAusentismos = null;
        crearMotivosAusentismos = new ArrayList<MotivosAusentismos>();
        modificarMotivosAusentismos = new ArrayList<MotivosAusentismos>();
        borrarMotivosAusentismos = new ArrayList<MotivosAusentismos>();
        editarMotivoAusentismo = new MotivosAusentismos();
        nuevoMotivoAusentismo = new MotivosAusentismos();
        nuevoMotivoAusentismo.setRemunerado("S");
        duplicarMotivoAusentismo = new MotivosAusentismos();
        duplicarMotivoAusentismo.setRemunerado("S");
        guardado = true;
        mapParametros.put("paginaAnterior", paginaAnterior);
        altoTabla = "335";
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
        String pagActual = "motivoausentismo";
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
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarMotivosAusentismos.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
            listMotivosAusentismos = null;
            getListMotivosAusentismos();
            if (listMotivosAusentismos != null) {
                motivoAusentismoSeleccionado = listMotivosAusentismos.get(0);
            }

        } catch (Exception e) {
            log.error("Error postconstruct " + this.getClass().getName() + ":  ", e);
            log.error("Causa: " + e.getCause());
        }
    }

    public void cambiarIndice(MotivosAusentismos motivo, int celda) {

        motivoAusentismoSeleccionado = motivo;
        cualCelda = celda;
        motivoAusentismoSeleccionado.getSecuencia();
        if (cualCelda == 0) {
            motivoAusentismoSeleccionado.getCodigo();
        } else if (cualCelda == 1) {
            motivoAusentismoSeleccionado.getDescripcion();
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void cancelarModificacion() {
        if (bandera == 1) {
            //CERRAR FILTRADO
            codigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosMotivosAusentismo:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosMotivosAusentismo:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            remunerado = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosMotivosAusentismo:remunerado");
            remunerado.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosMotivosAusentismo");
            bandera = 0;
            filtrarMotivosAusentismos = null;
            tipoLista = 0;
            altoTabla = "335";
        }

        borrarMotivosAusentismos.clear();
        crearMotivosAusentismos.clear();
        modificarMotivosAusentismos.clear();
        motivoAusentismoSeleccionado = null;
        k = 0;
        listMotivosAusentismos = null;
        guardado = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosMotivosAusentismo");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void eventoFiltrar() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
        contarRegistros();
    }

    public void activarCtrlF11() {
        if (bandera == 0) {
            altoTabla = "315";
            codigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosMotivosAusentismo:codigo");
            codigo.setFilterStyle("width: 85% !important;");
            descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosMotivosAusentismo:descripcion");
            descripcion.setFilterStyle("width: 85% !important;");
            remunerado = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosMotivosAusentismo:remunerado");
            remunerado.setFilterStyle("width: 85% !important;");
            RequestContext.getCurrentInstance().update("form:datosMotivosAusentismo");
            bandera = 1;
        } else if (bandera == 1) {
            altoTabla = "335";
            codigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosMotivosAusentismo:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosMotivosAusentismo:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            remunerado = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosMotivosAusentismo:remunerado");
            remunerado.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosMotivosAusentismo");
            bandera = 0;
            filtrarMotivosAusentismos = null;
            tipoLista = 0;
        }
    }

    public void modificandoMotivoAusentismo(MotivosAusentismos motivo) {
        motivoAusentismoSeleccionado = motivo;
        if (!crearMotivosAusentismos.contains(motivoAusentismoSeleccionado)) {
            if (modificarMotivosAusentismos.isEmpty()) {
                modificarMotivosAusentismos.add(motivoAusentismoSeleccionado);
            } else if (!modificarMotivosAusentismos.contains(motivoAusentismoSeleccionado)) {
                modificarMotivosAusentismos.add(motivoAusentismoSeleccionado);
            }
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
        RequestContext.getCurrentInstance().update("form:datosMotivosAusentismo");
    }
    
    
    public void borrandoMotivoAusentismo() {

        if (motivoAusentismoSeleccionado != null) {
            if (!modificarMotivosAusentismos.isEmpty() && modificarMotivosAusentismos.contains(motivoAusentismoSeleccionado)) {
                int modIndex = modificarMotivosAusentismos.indexOf(motivoAusentismoSeleccionado);
                modificarMotivosAusentismos.remove(modIndex);
                borrarMotivosAusentismos.add(motivoAusentismoSeleccionado);
            } else if (!crearMotivosAusentismos.isEmpty() && crearMotivosAusentismos.contains(motivoAusentismoSeleccionado)) {
                int crearIndex = crearMotivosAusentismos.indexOf(motivoAusentismoSeleccionado);
                crearMotivosAusentismos.remove(crearIndex);
            } else {
                borrarMotivosAusentismos.add(motivoAusentismoSeleccionado);
            }
            listMotivosAusentismos.remove(motivoAusentismoSeleccionado);
            if (tipoLista == 1) {
                filtrarMotivosAusentismos.remove(motivoAusentismoSeleccionado);
            }
            motivoAusentismoSeleccionado = null;
            RequestContext context = RequestContext.getCurrentInstance();
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosMotivosAusentismo");

        }

    }

    public void revisarDialogoGuardar() {
        if (!borrarMotivosAusentismos.isEmpty() || !crearMotivosAusentismos.isEmpty() || !modificarMotivosAusentismos.isEmpty()) {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:confirmarGuardar");
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
        }
    }

    public void guardarMotivoAusentismos() {
        RequestContext context = RequestContext.getCurrentInstance();
        try {
            if (guardado == false) {
                msgError = "";
                if (!borrarMotivosAusentismos.isEmpty()) {
                    for (int i = 0; i < borrarMotivosAusentismos.size(); i++) {
                        msgError = administrarMotivosAusentismos.borrarMotivosAusentismos(borrarMotivosAusentismos.get(i));
                    }
                    borrarMotivosAusentismos.clear();
                }
                if (!crearMotivosAusentismos.isEmpty()) {
                    for (int i = 0; i < crearMotivosAusentismos.size(); i++) {
                        msgError = administrarMotivosAusentismos.crearMotivosAusentismos(crearMotivosAusentismos.get(i));
                    }
                    crearMotivosAusentismos.clear();
                }
                if (!modificarMotivosAusentismos.isEmpty()) {
                    for (int i = 0; i < modificarMotivosAusentismos.size(); i++) {
                        msgError = administrarMotivosAusentismos.modificarMotivosAusentismos(modificarMotivosAusentismos.get(i));
                    }
                }
                modificarMotivosAusentismos.clear();

                if (msgError.equals("EXITO")) {
                    listMotivosAusentismos = null;
                    getListMotivosAusentismos();
                    k = 0;
                    FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                    RequestContext.getCurrentInstance().update("form:growl");
                    contarRegistros();
                    guardado = true;
                    motivoAusentismoSeleccionado = null;
                    RequestContext.getCurrentInstance().update("form:datosMotivosAusentismo");
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                } else {
                    RequestContext.getCurrentInstance().update("formularioDialogos:errorGuardado");
                    RequestContext.getCurrentInstance().execute("PF('errorGuardado').show()");
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
        if (motivoAusentismoSeleccionado != null) {
            editarMotivoAusentismo = motivoAusentismoSeleccionado;
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
    
     
     public void agregarNuevoMotivoAusentismo(){
     int contador = 0;
        int duplicados = 0;
        Integer a = 0;
        a = null;
        mensajeValidacion = " ";    
        if (nuevoMotivoAusentismo.getCodigo() == a) {
            mensajeValidacion = " Los campos marcados con asterisco son obligatorios ";
        } else {
            for (int i = 0; i < listMotivosAusentismos.size(); i++) {
                if (listMotivosAusentismos.get(i).getCodigo() == nuevoMotivoAusentismo.getCodigo()) {
                    duplicados++;
                }
            }
            if (duplicados > 0) {
                mensajeValidacion = "El código ingresado ya está registrado. Por favor ingrese un código válido";
            } else {
                contador++;
            }
        }
        if (nuevoMotivoAusentismo.getDescripcion()== (null)) {
            mensajeValidacion = " Los campos marcados con asterisco son obligatorios\n";
        } else {
            contador++;
        }
        if (nuevoMotivoAusentismo.getRemunerado()== null) {
            nuevoMotivoAusentismo.setRemunerado("S");
        }
        
        if (contador == 2) {
            if (bandera == 1) {
                codigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosMotivosAusentismo:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosMotivosAusentismo:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                remunerado = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosMotivosAusentismo:remunerado");
                remunerado.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosMotivosAusentismo");
                bandera = 0;
                filtrarMotivosAusentismos = null;
                tipoLista = 0;
                altoTabla = "335";
            }
            k++;
            l = BigInteger.valueOf(k);
            nuevoMotivoAusentismo.setSecuencia(l);
            crearMotivosAusentismos.add(nuevoMotivoAusentismo);
            listMotivosAusentismos.add(0, nuevoMotivoAusentismo);
            motivoAusentismoSeleccionado = nuevoMotivoAusentismo;
            RequestContext.getCurrentInstance().update("form:datosMotivosAusentismo");
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().execute("PF('nuevoRegistroMotivoAusentismo').hide()");
            nuevoMotivoAusentismo = new MotivosAusentismos();

        } else {
            RequestContext.getCurrentInstance().update("form:validacionNuevaCentroCosto");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevaCentroCosto').show()");
            contador = 0;
        }
     }
     
     public void limpiarNuevoMotivoAusentismo() {
        nuevoMotivoAusentismo = new MotivosAusentismos();
    }

    public void duplicandoMotivoAusentismo() {
        if (motivoAusentismoSeleccionado != null) {
            duplicarMotivoAusentismo = new MotivosAusentismos();
            k++;
            l = BigInteger.valueOf(k);
            duplicarMotivoAusentismo.setSecuencia(l);
            duplicarMotivoAusentismo.setCodigo(motivoAusentismoSeleccionado.getCodigo());
            duplicarMotivoAusentismo.setDescripcion(motivoAusentismoSeleccionado.getDescripcion());
            duplicarMotivoAusentismo.setRemunerado(motivoAusentismoSeleccionado.getRemunerado());
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTTR");
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroMotivosAusentismos').show()");
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

        if (duplicarMotivoAusentismo.getCodigo() == a) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
        } else {
            for (int x = 0; x < listMotivosAusentismos.size(); x++) {
                if (listMotivosAusentismos.get(x).getCodigo() == duplicarMotivoAusentismo.getCodigo()) {
                    duplicados++;
                }
            }
            if (duplicados > 0) {
                mensajeValidacion = "El código ingresado ya está registrado. Por favor ingrese un código válido";
            } else {
                log.info("bandera");
                contador++;
                duplicados = 0;
            }
        }
        if (duplicarMotivoAusentismo.getDescripcion()== null) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
        } else {
            contador++;
        }
        if (duplicarMotivoAusentismo.getRemunerado() == null) {
            duplicarMotivoAusentismo.setRemunerado("S");
        }
        if (contador == 2) {
            listMotivosAusentismos.add(0, duplicarMotivoAusentismo);
            crearMotivosAusentismos.add(duplicarMotivoAusentismo);
            motivoAusentismoSeleccionado = duplicarMotivoAusentismo;
            RequestContext.getCurrentInstance().update("form:datosMotivosAusentismo");
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            if (bandera == 1) {
                codigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosMotivosAusentismo:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosMotivosAusentismo:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                remunerado = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosMotivosAusentismo:remunerado");
                remunerado.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosMotivosAusentismo");
                bandera = 0;
                filtrarMotivosAusentismos = null;
                tipoLista = 0;
                altoTabla = "335";
            }
            duplicarMotivoAusentismo = new MotivosAusentismos();
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroMotivosAusentismos').hide()");

        } else {
            contador = 0;
            RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
            RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
        }
    }

    public void limpiarDuplicarMotivoAusentismo() {
        duplicarMotivoAusentismo = new MotivosAusentismos();
    }
    
    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosMotivosAusentismoExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "MotivosAusentismos", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosMotivosAusentismoExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "MotivosAusentismos", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void verificarRastro() {
        if (motivoAusentismoSeleccionado != null) {
            int resultado = administrarRastros.obtenerTabla(motivoAusentismoSeleccionado.getSecuencia(), "MOTIVOSAUSENTISMOS"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
        } else if (administrarRastros.verificarHistoricosTabla("MOTIVOSAUSENTISMOS")) { // igual acá
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
    }

    public void salir() {
        limpiarListasValor();
        if (bandera == 1) {
            codigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosMotivosAusentismo:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosMotivosAusentismo:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            remunerado = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosMotivosAusentismo:remunerado");
            remunerado.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosMotivosAusentismo");
            bandera = 0;
            filtrarMotivosAusentismos = null;
            tipoLista = 0;
            altoTabla = "335";
        }
        borrarMotivosAusentismos.clear();
        crearMotivosAusentismos.clear();
        modificarMotivosAusentismos.clear();
        motivoAusentismoSeleccionado = null;
        k = 0;
        listMotivosAusentismos = null;
        guardado = true;
        RequestContext.getCurrentInstance().update("form:datosMotivosAusentismo");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        navegar("atras");
    }

    public void contarRegistros() {
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
    }
    
         
    ///////////GETS Y SETS////////////
    public List<MotivosAusentismos> getListMotivosAusentismos() {
        if (listMotivosAusentismos == null) {
            listMotivosAusentismos = administrarMotivosAusentismos.consultarMotivosAusentismos();
        }
        return listMotivosAusentismos;
    }

    public void setListMotivosAusentismos(List<MotivosAusentismos> listMotivosAusentismos) {
        this.listMotivosAusentismos = listMotivosAusentismos;
    }

    public List<MotivosAusentismos> getFiltrarMotivosAusentismos() {
        return filtrarMotivosAusentismos;
    }

    public void setFiltrarMotivosAusentismos(List<MotivosAusentismos> filtrarMotivosAusentismos) {
        this.filtrarMotivosAusentismos = filtrarMotivosAusentismos;
    }

    public MotivosAusentismos getNuevoMotivoAusentismo() {
        return nuevoMotivoAusentismo;
    }

    public void setNuevoMotivoAusentismo(MotivosAusentismos nuevoMotivoAusentismo) {
        this.nuevoMotivoAusentismo = nuevoMotivoAusentismo;
    }

    public MotivosAusentismos getDuplicarMotivoAusentismo() {
        return duplicarMotivoAusentismo;
    }

    public void setDuplicarMotivoAusentismo(MotivosAusentismos duplicarMotivoAusentismo) {
        this.duplicarMotivoAusentismo = duplicarMotivoAusentismo;
    }

    public MotivosAusentismos getEditarMotivoAusentismo() {
        return editarMotivoAusentismo;
    }

    public void setEditarMotivoAusentismo(MotivosAusentismos editarMotivoAusentismo) {
        this.editarMotivoAusentismo = editarMotivoAusentismo;
    }

    public MotivosAusentismos getMotivoAusentismoSeleccionado() {
        return motivoAusentismoSeleccionado;
    }

    public void setMotivoAusentismoSeleccionado(MotivosAusentismos motivoAusentismoSeleccionado) {
        this.motivoAusentismoSeleccionado = motivoAusentismoSeleccionado;
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

    public String getAltoTabla() {
        return altoTabla;
    }

    public void setAltoTabla(String altoTabla) {
        this.altoTabla = altoTabla;
    }

    public String getInfoRegistro() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosMotivosAusentismo");
        infoRegistro = String.valueOf(tabla.getRowCount());
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

    public String getMensajeValidacion() {
        return mensajeValidacion;
    }

    public void setMensajeValidacion(String mensajeValidacion) {
        this.mensajeValidacion = mensajeValidacion;
    }

    public String getMsgError() {
        return msgError;
    }

    public void setMsgError(String msgError) {
        this.msgError = msgError;
    }

}
