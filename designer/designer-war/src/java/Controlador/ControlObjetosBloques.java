/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import ControlNavegacion.ControlListaNavegacion;
import Entidades.BloquesPantallas;
import Entidades.ObjetosBloques;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarObjetosBloquesInterface;
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
@Named(value = "controlObjetosBloques")
@SessionScoped
public class ControlObjetosBloques implements Serializable {

    private static Logger log = Logger.getLogger(ControlObjetosBloques.class);

    @EJB
    AdministrarObjetosBloquesInterface administrarObjetosBloques;
    @EJB
    AdministrarRastrosInterface administrarRastros;
    
    private List<ObjetosBloques> listaObjetosBloques;
    private List<ObjetosBloques> listaObjetosBloquesFiltrar;
    private List<ObjetosBloques> listaObjetosBloquesCrear;
    private List<ObjetosBloques> listaObjetosBloquesModificar;
    private List<ObjetosBloques> listaObjetosBloquesBorrar;
    private ObjetosBloques nuevoObjetosBloque;
    private ObjetosBloques duplicarObjetosBloque;
    private ObjetosBloques editarObjetosBloque;
    private ObjetosBloques objetoBloqueSeleccionado;
    //
    private List<BloquesPantallas> lovBloquesPantallas;
    private List<BloquesPantallas> lovBloquesPantallasFiltrar;
    private BloquesPantallas bloquePantallaSeleccionado;
    private int cualCelda, tipoLista, tipoActualizacion, k, bandera;
    private BigInteger l;
    private boolean aceptar, guardado;
    private Column nombre,bloque,comentario;
    private String altoTabla;
    //
    private String infoRegistro, infoRegistroPantallas;
    private String mensajeValidacion;
    private String paginaAnterior = "nominaf";
    private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();
    private String msgError;
    private boolean activarLov;
    
    public ControlObjetosBloques() {
        listaObjetosBloques = null;
        listaObjetosBloquesCrear = new ArrayList<ObjetosBloques>();
        listaObjetosBloquesModificar = new ArrayList<ObjetosBloques>();
        listaObjetosBloquesBorrar = new ArrayList<ObjetosBloques>();
        editarObjetosBloque = new ObjetosBloques();
        nuevoObjetosBloque = new ObjetosBloques();
        nuevoObjetosBloque.setBloque(new BloquesPantallas());
        duplicarObjetosBloque = new ObjetosBloques();
        guardado = true;
        mapParametros.put("paginaAnterior", paginaAnterior);
        altoTabla = "315";
        activarLov = true;
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
        String pagActual = "objetosdb";
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
        lovBloquesPantallas = null;
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
            administrarObjetosBloques.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
            listaObjetosBloques = null;
            getListaObjetosBloques();
//            if (listaObjetosBloques != null) {
//                objetoBloqueSeleccionado = listaObjetosBloques.get(0);
//            }

        } catch (Exception e) {
            log.error("Error postconstruct " + this.getClass().getName() + ":  ", e);
            log.error("Causa: " + e.getCause());
        }
    }

    public void cambiarIndice(ObjetosBloques objeto, int celda) {
        objetoBloqueSeleccionado = objeto;
        cualCelda = celda;
        objetoBloqueSeleccionado.getSecuencia();
        if (cualCelda == 0) {
            objetoBloqueSeleccionado.getNombre();
            deshabilitarBotonLov();
        } else if (cualCelda == 1) {
            objetoBloqueSeleccionado.getBloque().getNombre();
            habilitarBotonLov();
        } else if (cualCelda == 2) {
            objetoBloqueSeleccionado.getComentario();
            deshabilitarBotonLov();
        }
    }

    public void asignarIndex(ObjetosBloques objeto, int dlg, int lnd) {
        objetoBloqueSeleccionado = objeto;
        tipoActualizacion = lnd;
        if (dlg == 0) {
            lovBloquesPantallas = null;
            getLovBloquesPantallas();
            contarRegistrosModulos();
            RequestContext.getCurrentInstance().update("formularioDialogos:bloquesDialogo");
            RequestContext.getCurrentInstance().execute("PF('bloquesDialogo').show()");
        }

    }

    public void listaValoresBoton() {
        if (cualCelda == 2) {
            lovBloquesPantallas = null;
            getLovBloquesPantallas();
            RequestContext.getCurrentInstance().update("formularioDialogos:bloquesDialogo");
            RequestContext.getCurrentInstance().execute("PF('bloquesDialogo').show()");
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void cancelarModificacion() {
        if (bandera == 1) {
            //CERRAR FILTRADO
            nombre = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosObjetosBloques:nombre");
            nombre.setFilterStyle("display: none; visibility: hidden;");
            bloque = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosObjetosBloques:bloque");
            bloque.setFilterStyle("display: none; visibility: hidden;");
            comentario = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosObjetosBloques:comentario");
            comentario.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosObjetosBloques");
            bandera = 0;
            listaObjetosBloquesFiltrar = null;
            tipoLista = 0;
            altoTabla = "315";
        }

        listaObjetosBloquesBorrar.clear();
        listaObjetosBloquesCrear.clear();
        listaObjetosBloquesModificar.clear();
        objetoBloqueSeleccionado = null;
        k = 0;
        listaObjetosBloques = null;
        guardado = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosObjetosBloques");
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
            altoTabla = "295";
            nombre = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosObjetosBloques:nombre");
            nombre.setFilterStyle("width: 85% !important;");
            bloque = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosObjetosBloques:bloque");
            bloque.setFilterStyle("width: 85% !important;");
            comentario = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosObjetosBloques:comentario");
            comentario.setFilterStyle("width: 85% !important;");
            RequestContext.getCurrentInstance().update("form:datosObjetosBloques");
            bandera = 1;
        } else if (bandera == 1) {
            altoTabla = "315";
            nombre = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosObjetosBloques:nombre");
            nombre.setFilterStyle("display: none; visibility: hidden;");
            bloque = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosObjetosBloques:bloque");
            bloque.setFilterStyle("display: none; visibility: hidden;");
            comentario = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosObjetosBloques:comentario");
            comentario.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosObjetosBloques");
            bandera = 0;
            listaObjetosBloquesFiltrar = null;
            tipoLista = 0;
        }
    }

    public void modificarObjetosBloques(ObjetosBloques objeto) {
        objetoBloqueSeleccionado = objeto;
        if (!listaObjetosBloquesCrear.contains(objetoBloqueSeleccionado)) {
            if (listaObjetosBloquesModificar.isEmpty()) {
                listaObjetosBloquesModificar.add(objetoBloqueSeleccionado);
            } else if (!listaObjetosBloquesModificar.contains(objetoBloqueSeleccionado)) {
                listaObjetosBloquesModificar.add(objetoBloqueSeleccionado);
            }
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
        RequestContext.getCurrentInstance().update("form:datosObjetosBloques");
    }

    public void borrarObjetosBloques() {
        if (objetoBloqueSeleccionado != null) {
            if (!listaObjetosBloquesModificar.isEmpty() && listaObjetosBloquesModificar.contains(objetoBloqueSeleccionado)) {
                int modIndex = listaObjetosBloquesModificar.indexOf(objetoBloqueSeleccionado);
                listaObjetosBloquesModificar.remove(modIndex);
                listaObjetosBloquesBorrar.add(objetoBloqueSeleccionado);
            } else if (!listaObjetosBloquesCrear.isEmpty() && listaObjetosBloquesCrear.contains(objetoBloqueSeleccionado)) {
                int crearIndex = listaObjetosBloquesCrear.indexOf(objetoBloqueSeleccionado);
                listaObjetosBloquesCrear.remove(crearIndex);
            } else {
                listaObjetosBloquesBorrar.add(objetoBloqueSeleccionado);
            }
            listaObjetosBloques.remove(objetoBloqueSeleccionado);
            if (tipoLista == 1) {
                listaObjetosBloquesFiltrar.remove(objetoBloqueSeleccionado);
            }
            objetoBloqueSeleccionado = null;
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosObjetosBloques");
        }
    }

    public void revisarDialogoGuardar() {
        if (!listaObjetosBloquesBorrar.isEmpty() || !listaObjetosBloquesCrear.isEmpty() || !listaObjetosBloquesModificar.isEmpty()) {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:confirmarGuardar");
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
        }
    }

    public void guardarObjetosBD() {
        RequestContext context = RequestContext.getCurrentInstance();
        try {
            if (guardado == false) {
                msgError = "";
                if (!listaObjetosBloquesBorrar.isEmpty()) {
                    for (int i = 0; i < listaObjetosBloquesBorrar.size(); i++) {
                        msgError = administrarObjetosBloques.borrar(listaObjetosBloquesBorrar.get(i));
                    }
                    listaObjetosBloquesBorrar.clear();
                }
                if (!listaObjetosBloquesCrear.isEmpty()) {
                    for (int i = 0; i < listaObjetosBloquesCrear.size(); i++) {
                        msgError = administrarObjetosBloques.crear(listaObjetosBloquesCrear.get(i));
                    }
                    listaObjetosBloquesCrear.clear();
                }
                if (!listaObjetosBloquesModificar.isEmpty()) {
                    for (int i = 0; i < listaObjetosBloquesModificar.size(); i++) {
                        msgError = administrarObjetosBloques.modificar(listaObjetosBloquesModificar.get(i));
                    }
                }
                listaObjetosBloquesModificar.clear();

                if (msgError.equals("EXITO")) {
                    listaObjetosBloques = null;
                    getListaObjetosBloques();
                    k = 0;
                    FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                    RequestContext.getCurrentInstance().update("form:growl");
                    contarRegistros();
                    guardado = true;
                    objetoBloqueSeleccionado = null;
                    RequestContext.getCurrentInstance().update("form:datosObjetosBloques");
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
        if (objetoBloqueSeleccionado != null) {
            editarObjetosBloque = objetoBloqueSeleccionado;
            RequestContext context = RequestContext.getCurrentInstance();
            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editNombre");
                RequestContext.getCurrentInstance().execute("PF('editNombre').show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editBloque");
                RequestContext.getCurrentInstance().execute("PF('editBloque').show()");
                cualCelda = -1;
            } else if (cualCelda == 2) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editComentario");
                RequestContext.getCurrentInstance().execute("PF('editComentario').show()");
                cualCelda = -1;
            }

        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void agregarNuevoObjetosBloque() {
        int contador = 0;
        int duplicados = 0;

        Integer a = 0;
        a = null;
        mensajeValidacion = " ";
        if (nuevoObjetosBloque.getNombre() == (null) || nuevoObjetosBloque.getNombre().equals("")) {
            mensajeValidacion = " Los campos marcados con asterisco son obligatorios";
        } else {
            for (int x = 0; x < listaObjetosBloques.size(); x++) {
                if (listaObjetosBloques.get(x).getNombre().equals(nuevoObjetosBloque.getNombre())) {
                    duplicados++;
                }
            }
            if (duplicados > 0) {
                mensajeValidacion = "El Nombre ingresado ya está registrado. Por favor ingrese un Nombre válido";
            } else {
                contador++;
            }
        }

        if (nuevoObjetosBloque.getBloque()== (null)) {
            mensajeValidacion = " Los campos marcados con asterisco son obligatorios";
        } else {
            contador++;
        }

        if (contador == 2) {
            if (bandera == 1) {
                nombre = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosObjetosBloques:nombre");
                nombre.setFilterStyle("display: none; visibility: hidden;");
                bloque = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosObjetosBloques:bloque");
                bloque.setFilterStyle("display: none; visibility: hidden;");
                comentario = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosObjetosBloques:comentario");
                comentario.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosObjetosBloques");
                bandera = 0;
                listaObjetosBloquesFiltrar = null;
                tipoLista = 0;
                altoTabla = "315";
            }
            k++;
            l = BigInteger.valueOf(k);
            nuevoObjetosBloque.setSecuencia(l);
            listaObjetosBloquesCrear.add(nuevoObjetosBloque);
            listaObjetosBloques.add(0, nuevoObjetosBloque);
            objetoBloqueSeleccionado = nuevoObjetosBloque;
            RequestContext.getCurrentInstance().update("form:datosObjetosBloques");
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().execute("PF('nuevoRegistroObjetosBD').hide()");
            nuevoObjetosBloque = new ObjetosBloques();

        } else {
            RequestContext.getCurrentInstance().update("form:validacionNuevaCentroCosto");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevaCentroCosto').show()");
            contador = 0;
        }
    }

    public void limpiarNuevoObjetosBloque() {
        nuevoObjetosBloque = new ObjetosBloques();
    }

    public void duplicandoObjetosBloques() {
        if (objetoBloqueSeleccionado != null) {
            duplicarObjetosBloque = new ObjetosBloques();
            k++;
            l = BigInteger.valueOf(k);
            duplicarObjetosBloque.setSecuencia(l);
            duplicarObjetosBloque.setNombre(objetoBloqueSeleccionado.getNombre());
            duplicarObjetosBloque.setBloque(objetoBloqueSeleccionado.getBloque());
            duplicarObjetosBloque.setComentario(objetoBloqueSeleccionado.getComentario());
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarObj");
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroMotivosDefinitivas').show()");
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

        if (duplicarObjetosBloque.getNombre() == (null) || duplicarObjetosBloque.getNombre().equals("")) {
            mensajeValidacion = " Los campos marcados con asterisco son obligatorios";
        } else {
            for (int x = 0; x < listaObjetosBloques.size(); x++) {
                if (listaObjetosBloques.get(x).getNombre().equals(duplicarObjetosBloque.getNombre())) {
                    duplicados++;
                }
            }
            if (duplicados > 0) {
                mensajeValidacion = "El Nombre ingresado ya está registrado. Por favor ingrese un Nombre válido";
            } else {
                contador++;
            }
        }
        if (duplicarObjetosBloque.getBloque()== (null)) {
            mensajeValidacion = " Los campos marcados con asterisco son obligatorios";
        } else {
            contador++;
        }

        if (contador == 2) {
            listaObjetosBloques.add(0, duplicarObjetosBloque);
            listaObjetosBloquesCrear.add(duplicarObjetosBloque);
            objetoBloqueSeleccionado = duplicarObjetosBloque;
            RequestContext.getCurrentInstance().update("form:datosObjetosBloques");
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            if (bandera == 1) {
                nombre = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosObjetosBloques:nombre");
                nombre.setFilterStyle("display: none; visibility: hidden;");
                bloque = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosObjetosBloques:bloque");
                bloque.setFilterStyle("display: none; visibility: hidden;");
                comentario = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosObjetosBloques:comentario");
                comentario.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosObjetosBloques");
                bandera = 0;
                listaObjetosBloquesFiltrar = null;
                tipoLista = 0;
                altoTabla = "270";
            }
            duplicarObjetosBloque = new ObjetosBloques();
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroMotivosDefinitivas').hide()");

        } else {
            contador = 0;
            RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
            RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
        }
    }

    public void limpiarDuplicarObjetosBloques() {
        duplicarObjetosBloque = new ObjetosBloques();
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosObjetosBloquesExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "MotivosDefinitivas", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosObjetosBloquesExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "MotivosDefinitivas", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void verificarRastro() {
        if (objetoBloqueSeleccionado != null) {
            int resultado = administrarRastros.obtenerTabla(objetoBloqueSeleccionado.getSecuencia(), "OBJETOSDB"); //En ENCARGATURAS lo cambia por el nombre de su tabla
            if (resultado == 1) {
                RequestContext.getCurrentInstance().execute("PF('errorObjetosBloques').show()");
            } else if (resultado == 2) {
                RequestContext.getCurrentInstance().execute("PF('confirmarRastro').show()");
            } else if (resultado == 3) {
                RequestContext.getCurrentInstance().execute("PF('errorRegistroRastro').show()");
            } else if (resultado == 4) {
                RequestContext.getCurrentInstance().execute("PF('errorTablaConRastro').show()");
            } else if (resultado == 5) {
                RequestContext.getCurrentInstance().execute("PF('errorTablaSinRastro').show()");
            }
        } else if (administrarRastros.verificarHistoricosTabla("OBJETOSDB")) { // igual acá
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
    }

    public void salir() {
        limpiarListasValor();
        if (bandera == 1) {
            nombre = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosObjetosBloques:nombre");
            nombre.setFilterStyle("display: none; visibility: hidden;");
            bloque = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosObjetosBloques:bloque");
            bloque.setFilterStyle("display: none; visibility: hidden;");
            comentario = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosObjetosBloques:comentario");
            comentario.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosObjetosBloques");
            bandera = 0;
            listaObjetosBloquesFiltrar = null;
            tipoLista = 0;
            altoTabla = "315";
        }
        listaObjetosBloquesBorrar.clear();
        listaObjetosBloquesCrear.clear();
        listaObjetosBloquesModificar.clear();
        objetoBloqueSeleccionado = null;
        k = 0;
        listaObjetosBloques = null;
        guardado = true;
        RequestContext.getCurrentInstance().update("form:datosObjetosBloques");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        navegar("atras");
    }

    public void contarRegistros() {
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
    }

    public void contarRegistrosModulos() {
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroModulos");
    }

    public void habilitarBotonLov() {
        activarLov = false;
        RequestContext.getCurrentInstance().update("form:listaValores");
    }

    public void deshabilitarBotonLov() {
        activarLov = true;
        RequestContext.getCurrentInstance().update("form:listaValores");

    }

    public void actualizarBloque() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            objetoBloqueSeleccionado.setBloque(bloquePantallaSeleccionado);
            if (!listaObjetosBloquesCrear.contains(objetoBloqueSeleccionado)) {
                if (listaObjetosBloquesModificar.isEmpty()) {
                    listaObjetosBloquesModificar.add(objetoBloqueSeleccionado);
                } else if (!listaObjetosBloquesModificar.contains(objetoBloqueSeleccionado)) {
                    listaObjetosBloquesModificar.add(objetoBloqueSeleccionado);
                }
            }
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            deshabilitarBotonLov();
            RequestContext.getCurrentInstance().update("form:datosObjetosBloques");
        } else if (tipoActualizacion == 1) {
            nuevoObjetosBloque.setBloque(bloquePantallaSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoObj");
        } else if (tipoActualizacion == 2) {
            duplicarObjetosBloque.setBloque(bloquePantallaSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarObj");
        }
        lovBloquesPantallasFiltrar = null;
        bloquePantallaSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        RequestContext.getCurrentInstance().update("formularioDialogos:bloquesDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovBloquesPantallas");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarM");
        context.reset("formularioDialogos:lovBloquesPantallas:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovBloquesPantallas').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('bloquesDialogo').hide()");
    }

    public void cancelarCambioBloque() {
        lovBloquesPantallasFiltrar = null;
        bloquePantallaSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        RequestContext.getCurrentInstance().update("formularioDialogos:bloquesDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovBloquesPantallas");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarM");
        RequestContext.getCurrentInstance().reset("formularioDialogos:lovBloquesPantallas:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovBloquesPantallas').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('bloquesDialogo').hide()");
    }
    
    //////////////GETS Y SETS////////

    public List<ObjetosBloques> getListaObjetosBloques() {
        if(listaObjetosBloques == null){
            listaObjetosBloques = administrarObjetosBloques.consultarObjetosBloques();
        }
        return listaObjetosBloques;
    }

    public void setListaObjetosBloques(List<ObjetosBloques> listaObjetosBloques) {
        this.listaObjetosBloques = listaObjetosBloques;
    }

    public List<ObjetosBloques> getListaObjetosBloquesFiltrar() {
        return listaObjetosBloquesFiltrar;
    }

    public void setListaObjetosBloquesFiltrar(List<ObjetosBloques> listaObjetosBloquesFiltrar) {
        this.listaObjetosBloquesFiltrar = listaObjetosBloquesFiltrar;
    }

    public ObjetosBloques getNuevoObjetosBloque() {
        return nuevoObjetosBloque;
    }

    public void setNuevoObjetosBloque(ObjetosBloques nuevoObjetosBloque) {
        this.nuevoObjetosBloque = nuevoObjetosBloque;
    }

    public ObjetosBloques getDuplicarObjetosBloque() {
        return duplicarObjetosBloque;
    }

    public void setDuplicarObjetosBloque(ObjetosBloques duplicarObjetosBloque) {
        this.duplicarObjetosBloque = duplicarObjetosBloque;
    }

    public ObjetosBloques getEditarObjetosBloque() {
        return editarObjetosBloque;
    }

    public void setEditarObjetosBloque(ObjetosBloques editarObjetosBloque) {
        this.editarObjetosBloque = editarObjetosBloque;
    }

    public ObjetosBloques getObjetoBloqueSeleccionado() {
        return objetoBloqueSeleccionado;
    }

    public void setObjetoBloqueSeleccionado(ObjetosBloques objetoBloqueSeleccionado) {
        this.objetoBloqueSeleccionado = objetoBloqueSeleccionado;
    }

    public List<BloquesPantallas> getLovBloquesPantallas() {
        if(lovBloquesPantallas == null){
            lovBloquesPantallas = administrarObjetosBloques.consultarBloquesPantallas();
        }
        return lovBloquesPantallas;
    }

    public void setLovBloquesPantallas(List<BloquesPantallas> lovBloquesPantallas) {
        this.lovBloquesPantallas = lovBloquesPantallas;
    }

    public List<BloquesPantallas> getLovBloquesPantallasFiltrar() {
        return lovBloquesPantallasFiltrar;
    }

    public void setLovBloquesPantallasFiltrar(List<BloquesPantallas> lovBloquesPantallasFiltrar) {
        this.lovBloquesPantallasFiltrar = lovBloquesPantallasFiltrar;
    }

    public BloquesPantallas getBloquePantallaSeleccionado() {
        return bloquePantallaSeleccionado;
    }

    public void setBloquePantallaSeleccionado(BloquesPantallas bloquePantallaSeleccionado) {
        this.bloquePantallaSeleccionado = bloquePantallaSeleccionado;
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
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosObjetosBloques");
        infoRegistro = String.valueOf(tabla.getRowCount());
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

    public String getInfoRegistroPantallas() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovBloquesPantallas");
        infoRegistroPantallas = String.valueOf(tabla.getRowCount());
        return infoRegistroPantallas;
    }

    public void setInfoRegistroPantallas(String infoRegistroPantallas) {
        this.infoRegistroPantallas = infoRegistroPantallas;
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

    public boolean isActivarLov() {
        return activarLov;
    }

    public void setActivarLov(boolean activarLov) {
        this.activarLov = activarLov;
    }
    
    
}
