/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import ControlNavegacion.ControlListaNavegacion;
import Entidades.Contratos;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarContratosInterface;
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
@Named(value = "controlContrato")
@SessionScoped
public class ControlContrato implements Serializable {

    private static Logger log = Logger.getLogger(ControlContrato.class);

    @EJB
    AdministrarContratosInterface administrarContratos;
    @EJB
    AdministrarRastrosInterface administrarRastros;

    private List<Contratos> listContratos;
    private List<Contratos> filtrarContratos;
    private List<Contratos> crearContratos;
    private List<Contratos> modificarContratos;
    private List<Contratos> borrarContratos;
    private Contratos nuevoContrato;
    private Contratos duplicarContrato;
    private Contratos editarContrato;
    private Contratos contratoSeleccionado;
    //otros
    private int cualCelda, tipoLista, tipoActualizacion, k, bandera;
    private BigInteger l;
    private boolean aceptar, guardado;
    private Column codigo, descripcion, estado, convencion;
    private int registrosBorrados;
    private String mensajeValidacion;
    private int tamano;
    private String infoRegistro;
    private String paginaAnterior = "nominaf";
    private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

    public ControlContrato() {
        listContratos = null;
        crearContratos = new ArrayList<Contratos>();
        modificarContratos = new ArrayList<Contratos>();
        borrarContratos = new ArrayList<Contratos>();
        editarContrato = new Contratos();
        nuevoContrato = new Contratos();
        duplicarContrato = new Contratos();
        guardado = true;
        tamano = 310;
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
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarContratos.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
            listContratos = null;
            getListContratos();
            if (listContratos != null) {
                if (!listContratos.isEmpty()) {
                    contratoSeleccionado = listContratos.get(0);
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
        String pagActual = "contrato";
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

    public void cambiarIndice(Contratos td, int celda) {
        contratoSeleccionado = td;
        cualCelda = celda;
        contratoSeleccionado.getSecuencia();
        if (cualCelda == 0) {
            contratoSeleccionado.getCodigo();
        } else if (cualCelda == 1) {
            contratoSeleccionado.getDescripcion();
        } else if (cualCelda == 2) {
            contratoSeleccionado.getEstado();
        } else if (cualCelda == 3) {
            contratoSeleccionado.getConvencion();
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void cancelarModificacion() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 1) {
            //CERRAR FILTRADO
            codigo = (Column) c.getViewRoot().findComponent("form:datosContratos:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosContratos:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            estado = (Column) c.getViewRoot().findComponent("form:datosContratos:estado");
            estado.setFilterStyle("display: none; visibility: hidden;");
            convencion = (Column) c.getViewRoot().findComponent("form:datosContratos:convencion");
            convencion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosContratos");
            bandera = 0;
            filtrarContratos = null;
            tipoLista = 0;
            tamano = 310;
        }

        borrarContratos.clear();
        crearContratos.clear();
        modificarContratos.clear();
        contratoSeleccionado = null;
        k = 0;
        listContratos = null;
        guardado = true;
        getListContratos();
        contarRegistros();
        RequestContext.getCurrentInstance().update("form:datosContratos");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void salir() {
        limpiarListasValor();
        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            codigo = (Column) c.getViewRoot().findComponent("form:datosContratos:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosContratos:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            estado = (Column) c.getViewRoot().findComponent("form:datosContratos:estado");
            estado.setFilterStyle("display: none; visibility: hidden;");
            convencion = (Column) c.getViewRoot().findComponent("form:datosContratos:convencion");
            convencion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosContratos");
            bandera = 0;
            filtrarContratos = null;
            tipoLista = 0;
            tamano = 310;
        }

        borrarContratos.clear();
        crearContratos.clear();
        modificarContratos.clear();
        contratoSeleccionado = null;
        k = 0;
        listContratos = null;
        guardado = true;
        getListContratos();
        RequestContext context = RequestContext.getCurrentInstance();
        contarRegistros();
        RequestContext.getCurrentInstance().update("form:datosContratos");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        navegar("atras");
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            tamano = 290;
            codigo = (Column) c.getViewRoot().findComponent("form:datosContratos:codigo");
            codigo.setFilterStyle("width: 85% !important;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosContratos:descripcion");
            descripcion.setFilterStyle("width: 85% !important;");
            estado = (Column) c.getViewRoot().findComponent("form:datosContratos:estado");
            estado.setFilterStyle("width: 85% !important;");
            convencion = (Column) c.getViewRoot().findComponent("form:datosContratos:convencion");
            convencion.setFilterStyle("width: 85% !important;");
            RequestContext.getCurrentInstance().update("form:datosContratos");
            bandera = 1;
        } else if (bandera == 1) {
            tamano = 310;
            codigo = (Column) c.getViewRoot().findComponent("form:datosContratos:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosContratos:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            estado = (Column) c.getViewRoot().findComponent("form:datosContratos:estado");
            estado.setFilterStyle("display: none; visibility: hidden;");
            convencion = (Column) c.getViewRoot().findComponent("form:datosContratos:convencion");
            convencion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosContratos");
            bandera = 0;
            filtrarContratos = null;
            tipoLista = 0;
        }
    }

    public void modificarContratos(Contratos td) {
        contratoSeleccionado = td;
        if (!crearContratos.contains(contratoSeleccionado)) {
            if (modificarContratos.isEmpty()) {
                modificarContratos.add(contratoSeleccionado);
            } else if (!modificarContratos.contains(contratoSeleccionado)) {
                modificarContratos.add(contratoSeleccionado);
            }
            guardado = false;

            RequestContext.getCurrentInstance().update("form:datosContratos");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
    }

    public void borrandoContratos() {

        if (contratoSeleccionado != null) {
            if (!modificarContratos.isEmpty() && modificarContratos.contains(contratoSeleccionado)) {
                int modIndex = modificarContratos.indexOf(contratoSeleccionado);
                modificarContratos.remove(modIndex);
                borrarContratos.add(contratoSeleccionado);
            } else if (!crearContratos.isEmpty() && crearContratos.contains(contratoSeleccionado)) {
                int crearIndex = crearContratos.indexOf(contratoSeleccionado);
                crearContratos.remove(crearIndex);
            } else {
                borrarContratos.add(contratoSeleccionado);
            }
            listContratos.remove(contratoSeleccionado);
            if (tipoLista == 1) {
                filtrarContratos.remove(contratoSeleccionado);
            }
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:datosContratos");
            contratoSeleccionado = null;
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void revisarDialogoGuardar() {
        if (!borrarContratos.isEmpty() || !crearContratos.isEmpty() || !modificarContratos.isEmpty()) {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:confirmarGuardar");
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
        }
    }

    public void guardarContratos() {
        try {
            RequestContext context = RequestContext.getCurrentInstance();
            if (guardado == false) {
                if (!borrarContratos.isEmpty()) {
                    administrarContratos.borrarContrato(borrarContratos);
                    borrarContratos.clear();
                }
                if (!modificarContratos.isEmpty()) {
                    administrarContratos.modificarContrato(modificarContratos);
                    modificarContratos.clear();
                }
                if (!crearContratos.isEmpty()) {
                    administrarContratos.crearContrato(crearContratos);
                    crearContratos.clear();
                }
                listContratos = null;
                getListContratos();
                RequestContext.getCurrentInstance().update("form:datosContratos");
                FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                RequestContext.getCurrentInstance().update("form:growl");
                k = 0;
                guardado = true;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        } catch (Exception e) {
            log.warn("Error guardarCambios : " + e.toString());
            FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado, intente nuevamente.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }
    }

    public void guardarSalir(){
        guardarContratos();
        salir();
    }
    
    public void editarCelda() {
        if (contratoSeleccionado != null) {
            editarContrato = contratoSeleccionado;
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
                RequestContext.getCurrentInstance().update("formularioDialogos:editEstado");
                RequestContext.getCurrentInstance().execute("PF('editEstado').show()");
                cualCelda = -1;
            } else if (cualCelda == 3) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editConvencion");
                RequestContext.getCurrentInstance().execute("PF('editConvencion').show()");
                cualCelda = -1;
            }
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void agregarNuevoContratos() {
        int contador = 0;
        int duplicados = 0;
        mensajeValidacion = " ";
        if (nuevoContrato.getDescripcion() == null || nuevoContrato.getDescripcion().equals(" ")) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
        } else {
            contador++;
        }
        if (nuevoContrato.getEstado() == null || nuevoContrato.getEstado().equals(" ")) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
        } else {
            contador++;
        }
        if (nuevoContrato.getCodigo() == null) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
        } else {
            contador++;
        }
        if (nuevoContrato.getCodigo() != null) {
            for (int i = 0; i < listContratos.size(); i++) {
                if (nuevoContrato.getCodigo().equals(listContratos.get(i).getCodigo())) {
                    duplicados++;
                }
            }
            if (duplicados == 0) {
                contador++;
            } else {
                mensajeValidacion = "Existe un registro con código ingresado. Por favor ingrese un código válido";
            }
        } else {
            contador++;
        }

        if (contador == 4) {
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                tamano = 310;
                codigo = (Column) c.getViewRoot().findComponent("form:datosContratos:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosContratos:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                estado = (Column) c.getViewRoot().findComponent("form:datosContratos:estado");
                estado.setFilterStyle("display: none; visibility: hidden;");
                convencion = (Column) c.getViewRoot().findComponent("form:datosContratos:convencion");
                convencion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosContratos");
                bandera = 0;
                filtrarContratos = null;
                tipoLista = 0;
            }
            k++;
            l = BigInteger.valueOf(k);
            nuevoContrato.setSecuencia(l);
            crearContratos.add(nuevoContrato);
            listContratos.add(0, nuevoContrato);
            contratoSeleccionado = nuevoContrato;
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:datosContratos");
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().execute("PF('nuevoRegistroContratos').hide()");
            nuevoContrato = new Contratos();

        } else {
            RequestContext.getCurrentInstance().update("form:validacionNuevaCentroCosto");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevaCentroCosto').show()");
            contador = 0;
        }
    }

    public void limpiarNuevoContratos() {
        nuevoContrato = new Contratos();
    }

    public void duplicandoContratos() {
        if (contratoSeleccionado != null) {
            duplicarContrato = new Contratos();
            k++;
            l = BigInteger.valueOf(k);
            duplicarContrato.setSecuencia(l);
            duplicarContrato.setCodigo(contratoSeleccionado.getCodigo());
            duplicarContrato.setDescripcion(contratoSeleccionado.getDescripcion());
            duplicarContrato.setEstado(contratoSeleccionado.getEstado());
            duplicarContrato.setDescripcion(contratoSeleccionado.getDescripcion());
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTE");
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroContratos').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void confirmarDuplicar() {
        int contador = 0;
        mensajeValidacion = " ";
        int duplicados = 0;
        if (duplicarContrato.getDescripcion() == null || duplicarContrato.getDescripcion().equals(" ")) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
        } else {
            contador++;
        }
        if (duplicarContrato.getEstado() == null || duplicarContrato.getEstado().equals(" ")) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
        } else {
            contador++;
        }
        if (duplicarContrato.getCodigo() == null) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
        } else {
            contador++;
        }
        if (duplicarContrato.getCodigo() != null) {
            for (int i = 0; i < listContratos.size(); i++) {
                if (duplicarContrato.getCodigo().equals(listContratos.get(i).getCodigo())) {
                    duplicados++;
                }
            }
            if (duplicados == 0) {
                contador++;
            } else {
                mensajeValidacion = "Existe un registro con código ingresado. Por favor ingrese un código válido";
            }
        } else {
            contador++;
        }
        if (contador == 4) {
            k++;
            l = BigInteger.valueOf(k);
            duplicarContrato.setSecuencia(l);
            crearContratos.add(duplicarContrato);
            listContratos.add(0, duplicarContrato);
            contratoSeleccionado = duplicarContrato;
            RequestContext.getCurrentInstance().update("form:datosContratos");
            guardado = false;
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                tamano = 310;
                codigo = (Column) c.getViewRoot().findComponent("form:datosContratos:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosContratos:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                estado = (Column) c.getViewRoot().findComponent("form:datosContratos:estado");
                estado.setFilterStyle("display: none; visibility: hidden;");
                convencion = (Column) c.getViewRoot().findComponent("form:datosContratos:convencion");
                convencion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosContratos");
                bandera = 0;
                filtrarContratos = null;
                tipoLista = 0;
            }
            duplicarContrato = new Contratos();
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroContratos').hide()");

        } else {
            contador = 0;
            RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
            RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
        }
    }

    public void limpiarDuplicarContratos() {
        duplicarContrato = new Contratos();
    }
    
    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosContratosExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "CONTRATOS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosContratosExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "CONTRATOS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (contratoSeleccionado != null) {
            int resultado = administrarRastros.obtenerTabla(contratoSeleccionado.getSecuencia(), "CONTRATOS"); //En ENCARGATURAS lo cambia por el Descripcion de su tabla
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
        } else if (administrarRastros.verificarHistoricosTabla("CONTRATOS")) { // igual acá
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
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

    //////////SETTERS Y GETTERS/////
    public List<Contratos> getListContratos() {
        if(listContratos == null){
            listContratos = administrarContratos.consultarContratos();
        }
        return listContratos;
    }

    public void setListContratos(List<Contratos> listContratos) {
        this.listContratos = listContratos;
    }

    public List<Contratos> getFiltrarContratos() {
        return filtrarContratos;
    }

    public void setFiltrarContratos(List<Contratos> filtrarContratos) {
        this.filtrarContratos = filtrarContratos;
    }

    public Contratos getNuevoContrato() {
        return nuevoContrato;
    }

    public void setNuevoContrato(Contratos nuevoContrato) {
        this.nuevoContrato = nuevoContrato;
    }

    public Contratos getDuplicarContrato() {
        return duplicarContrato;
    }

    public void setDuplicarContrato(Contratos duplicarContrato) {
        this.duplicarContrato = duplicarContrato;
    }

    public Contratos getEditarContrato() {
        return editarContrato;
    }

    public void setEditarContrato(Contratos editarContrato) {
        this.editarContrato = editarContrato;
    }

    public Contratos getContratoSeleccionado() {
        return contratoSeleccionado;
    }

    public void setContratoSeleccionado(Contratos contratoSeleccionado) {
        this.contratoSeleccionado = contratoSeleccionado;
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
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosContratos");
        infoRegistro = String.valueOf(tabla.getRowCount());
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

    public int getTamano() {
        return tamano;
    }

    public void setTamano(int tamano) {
        this.tamano = tamano;
    }

}
