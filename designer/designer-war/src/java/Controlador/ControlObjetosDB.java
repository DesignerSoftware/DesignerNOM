/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import ControlNavegacion.ControlListaNavegacion;
import Entidades.Modulos;
import Entidades.ObjetosDB;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarObjetosDBInterface;
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
@Named(value = "controlObjetosDB")
@SessionScoped
public class ControlObjetosDB implements Serializable {

    private static Logger log = Logger.getLogger(ControlObjetosDB.class);

    @EJB
    AdministrarObjetosDBInterface administrarObjetosDb;
    @EJB
    AdministrarRastrosInterface administrarRastros;

    private List<ObjetosDB> listaObjetosDB;
    private List<ObjetosDB> listaObjetosDBFiltrar;
    private List<ObjetosDB> listaObjetosDBCrear;
    private List<ObjetosDB> listaObjetosDBModificar;
    private List<ObjetosDB> listaObjetosDBBorrar;
    private ObjetosDB nuevoObjetoDB;
    private ObjetosDB duplicarObjetoDB;
    private ObjetosDB editarObjetoDB;
    private ObjetosDB objetoDBSeleccionado;
    //
    private List<Modulos> lovModulos;
    private List<Modulos> lovModulosFiltrar;
    private Modulos moduloSeleccionado;
    private int cualCelda, tipoLista, tipoActualizacion, k, bandera;
    private BigInteger l;
    private boolean aceptar, guardado;
    private Column tipo, nombre, modulo, descripcion, clasificacion;
    private String altoTabla;
    //
    private String infoRegistro, infoRegistroModulos;
    private String mensajeValidacion;
    private String paginaAnterior = "nominaf";
    private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();
    private String msgError;
    private boolean activarLov;

    public ControlObjetosDB() {
        listaObjetosDB = null;
        listaObjetosDBCrear = new ArrayList<ObjetosDB>();
        listaObjetosDBModificar = new ArrayList<ObjetosDB>();
        listaObjetosDBBorrar = new ArrayList<ObjetosDB>();
        editarObjetoDB = new ObjetosDB();
        nuevoObjetoDB = new ObjetosDB();
        nuevoObjetoDB.setModulo(new Modulos());
        nuevoObjetoDB.setTipo("TABLE");
        duplicarObjetoDB = new ObjetosDB();
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
        lovModulos = null;
    }

    @PreDestroy
    public void destruyendose() {
        log.info(this.getClass().getName() + ".destruyendoce() @Destroy");
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarObjetosDb.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
            listaObjetosDB = null;
            getListaObjetosDB();
            if (listaObjetosDB != null) {
                objetoDBSeleccionado = listaObjetosDB.get(0);
            }

        } catch (Exception e) {
            log.error("Error postconstruct " + this.getClass().getName() + ":  ", e);
            log.error("Causa: " + e.getCause());
        }
    }

    public void cambiarIndice(ObjetosDB objeto, int celda) {
        objetoDBSeleccionado = objeto;
        cualCelda = celda;
        objetoDBSeleccionado.getSecuencia();
        if (cualCelda == 0) {
            objetoDBSeleccionado.getTipo();
            deshabilitarBotonLov();
        } else if (cualCelda == 1) {
            objetoDBSeleccionado.getNombre();
            deshabilitarBotonLov();
        } else if (cualCelda == 2) {
            objetoDBSeleccionado.getModulo().getNombre();
            habilitarBotonLov();
        } else if (cualCelda == 3) {
            objetoDBSeleccionado.getDescripcion();
            deshabilitarBotonLov();
        } else if (cualCelda == 4) {
            objetoDBSeleccionado.getClasificacion();
            deshabilitarBotonLov();
        }
    }

    public void asignarIndex(ObjetosDB objeto, int dlg, int lnd) {
        objetoDBSeleccionado = objeto;
        tipoActualizacion = lnd;
        if (dlg == 0) {
            lovModulos = null;
            getLovModulos();
            RequestContext.getCurrentInstance().update("formularioDialogos:modulosDialogo");
            RequestContext.getCurrentInstance().execute("PF('modulosDialogo').show()");
        }

    }

    public void listaValoresBoton() {
        if (cualCelda == 2) {
            lovModulos = null;
            getLovModulos();
            RequestContext.getCurrentInstance().update("formularioDialogos:modulosDialogo");
            RequestContext.getCurrentInstance().execute("PF('modulosDialogo').show()");
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void cancelarModificacion() {
        if (bandera == 1) {
            //CERRAR FILTRADO
            tipo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosObjetosDB:tipo");
            tipo.setFilterStyle("display: none; visibility: hidden;");
            nombre = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosObjetosDB:nombre");
            nombre.setFilterStyle("display: none; visibility: hidden;");
            modulo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosObjetosDB:modulo");
            modulo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosObjetosDB:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            clasificacion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosObjetosDB:clasificacion");
            clasificacion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosObjetosDB");
            bandera = 0;
            listaObjetosDBFiltrar = null;
            tipoLista = 0;
            altoTabla = "315";
        }

        listaObjetosDBBorrar.clear();
        listaObjetosDBCrear.clear();
        listaObjetosDBModificar.clear();
        objetoDBSeleccionado = null;
        k = 0;
        listaObjetosDB = null;
        guardado = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosObjetosDB");
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
            tipo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosObjetosDB:tipo");
            tipo.setFilterStyle("width: 85% !important;");
            nombre = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosObjetosDB:nombre");
            nombre.setFilterStyle("width: 85% !important;");
            modulo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosObjetosDB:modulo");
            modulo.setFilterStyle("width: 85% !important;");
            descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosObjetosDB:descripcion");
            descripcion.setFilterStyle("width: 85% !important;");
            clasificacion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosObjetosDB:clasificacion");
            clasificacion.setFilterStyle("width: 85% !important;");
            RequestContext.getCurrentInstance().update("form:datosObjetosDB");
            bandera = 1;
        } else if (bandera == 1) {
            altoTabla = "315";
            tipo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosObjetosDB:tipo");
            tipo.setFilterStyle("display: none; visibility: hidden;");
            nombre = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosObjetosDB:nombre");
            nombre.setFilterStyle("display: none; visibility: hidden;");
            modulo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosObjetosDB:modulo");
            modulo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosObjetosDB:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            clasificacion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosObjetosDB:clasificacion");
            clasificacion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosObjetosDB");
            bandera = 0;
            listaObjetosDBFiltrar = null;
            tipoLista = 0;
        }
    }

    public void seleccionarComboBox(ObjetosDB objeto, String estado) {
        objetoDBSeleccionado = objeto;
        if (estado.equals("FUNCTION")) {
            objetoDBSeleccionado.setTipo("FUNCTION");
        } else if (estado.equals("PACKAGE")) {
            objetoDBSeleccionado.setTipo("PACKAGE");
        } else if (estado.equals("PROCEDURE")) {
            objetoDBSeleccionado.setTipo("PROCEDURE");
        } else if (estado.equals("SEQUENCE")) {
            objetoDBSeleccionado.setTipo("SEQUENCE");
        } else if (estado.equals("TABLE")) {
            objetoDBSeleccionado.setTipo("TABLE");
        } else if (estado.equals("VIEW")) {
            objetoDBSeleccionado.setTipo("VIEW");
        } else if (estado.equals("POR ASIGNAR")) {
            objetoDBSeleccionado.setClasificacion("POR ASIGNAR");
        } else if (estado.equals("CONFIGURACION")) {
            objetoDBSeleccionado.setClasificacion("CONFIGURACION");
        } else if (estado.equals("CONSTRUCCION")) {
            objetoDBSeleccionado.setClasificacion("CONSTRUCCION");
        } else if (estado.equals("DATOS")) {
            objetoDBSeleccionado.setClasificacion("DATOS");
        } else if (estado.equals("SISTEMA")) {
            objetoDBSeleccionado.setClasificacion("SISTEMA");
        } else if (estado.equals("NULL")) {
            objetoDBSeleccionado.setClasificacion(null);
        }
        modificarObjetosDB(objetoDBSeleccionado);
    }

    public void modificarObjetosDB(ObjetosDB objeto) {
        objetoDBSeleccionado = objeto;
        if (!listaObjetosDBCrear.contains(objetoDBSeleccionado)) {
            if (listaObjetosDBModificar.isEmpty()) {
                listaObjetosDBModificar.add(objetoDBSeleccionado);
            } else if (!listaObjetosDBModificar.contains(objetoDBSeleccionado)) {
                listaObjetosDBModificar.add(objetoDBSeleccionado);
            }
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
        RequestContext.getCurrentInstance().update("form:datosObjetosDB");
    }

    public void borrarObjetosDB(ObjetosDB objeto) {

        if (objetoDBSeleccionado != null) {
            if (!listaObjetosDBModificar.isEmpty() && listaObjetosDBModificar.contains(objetoDBSeleccionado)) {
                int modIndex = listaObjetosDBModificar.indexOf(objetoDBSeleccionado);
                listaObjetosDBModificar.remove(modIndex);
                listaObjetosDBBorrar.add(objetoDBSeleccionado);
            } else if (!listaObjetosDBCrear.isEmpty() && listaObjetosDBCrear.contains(objetoDBSeleccionado)) {
                int crearIndex = listaObjetosDBCrear.indexOf(objetoDBSeleccionado);
                listaObjetosDBCrear.remove(crearIndex);
            } else {
                listaObjetosDBBorrar.add(objetoDBSeleccionado);
            }
            listaObjetosDB.remove(objetoDBSeleccionado);
            if (tipoLista == 1) {
                listaObjetosDBFiltrar.remove(objetoDBSeleccionado);
            }
            objetoDBSeleccionado = null;
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosObjetosDB");
        }
    }

    public void revisarDialogoGuardar() {
        if (!listaObjetosDBBorrar.isEmpty() || !listaObjetosDBCrear.isEmpty() || !listaObjetosDBModificar.isEmpty()) {
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
                if (!listaObjetosDBBorrar.isEmpty()) {
                    for (int i = 0; i < listaObjetosDBBorrar.size(); i++) {
                        msgError = administrarObjetosDb.borrarObjetosDB(listaObjetosDBBorrar.get(i));
                    }
                    listaObjetosDBBorrar.clear();
                }
                if (!listaObjetosDBCrear.isEmpty()) {
                    for (int i = 0; i < listaObjetosDBCrear.size(); i++) {
                        msgError = administrarObjetosDb.crearObjetosDB(listaObjetosDBCrear.get(i));
                    }
                    listaObjetosDBCrear.clear();
                }
                if (!listaObjetosDBModificar.isEmpty()) {
                    for (int i = 0; i < listaObjetosDBModificar.size(); i++) {
                        msgError = administrarObjetosDb.modificarObjetosDB(listaObjetosDBModificar.get(i));
                    }
                }
                listaObjetosDBModificar.clear();

                if (msgError.equals("EXITO")) {
                    listaObjetosDB = null;
                    getListaObjetosDB();
                    k = 0;
                    FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                    RequestContext.getCurrentInstance().update("form:growl");
                    contarRegistros();
                    guardado = true;
                    objetoDBSeleccionado = null;
                    RequestContext.getCurrentInstance().update("form:datosObjetosDB");
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
        if (objetoDBSeleccionado != null) {
            editarObjetoDB = objetoDBSeleccionado;
            RequestContext context = RequestContext.getCurrentInstance();
            if (cualCelda == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editNombre");
                RequestContext.getCurrentInstance().execute("PF('editNombre').show()");
                cualCelda = -1;
            } else if (cualCelda == 2) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editModulo");
                RequestContext.getCurrentInstance().execute("PF('editModulo').show()");
                cualCelda = -1;
            } else if (cualCelda == 3) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editDescripcion");
                RequestContext.getCurrentInstance().execute("PF('editDescripcion').show()");
                cualCelda = -1;
            }

        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void agregarNuevoObjetoDB() {
        int contador = 0;
        int duplicados = 0;

        Integer a = 0;
        a = null;
        mensajeValidacion = " ";
        if (nuevoObjetoDB.getNombre() == (null) || nuevoObjetoDB.getNombre().equals("")) {
            mensajeValidacion = " Los campos marcados con asterisco son obligatorios";
        } else {
            for (int x = 0; x < listaObjetosDB.size(); x++) {
                if (listaObjetosDB.get(x).getNombre().equals(nuevoObjetoDB.getNombre())) {
                    duplicados++;
                }
            }
            if (duplicados > 0) {
                mensajeValidacion = "El Nombre ingresado ya está registrado. Por favor ingrese un Nombre válido";
            } else {
                contador++;
            }
        }

        if (nuevoObjetoDB.getModulo() == (null)) {
            mensajeValidacion = " Los campos marcados con asterisco son obligatorios";
        } else {
            contador++;
        }
        if (nuevoObjetoDB.getTipo() == null) {
            nuevoObjetoDB.setTipo("FUNCTION");
        }

        if (contador == 2) {
            if (bandera == 1) {
                tipo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosObjetosDB:tipo");
                tipo.setFilterStyle("display: none; visibility: hidden;");
                nombre = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosObjetosDB:nombre");
                nombre.setFilterStyle("display: none; visibility: hidden;");
                modulo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosObjetosDB:modulo");
                modulo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosObjetosDB:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                clasificacion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosObjetosDB:clasificacion");
                clasificacion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosObjetosDB");
                bandera = 0;
                listaObjetosDBFiltrar = null;
                tipoLista = 0;
                altoTabla = "315";
            }
            k++;
            l = BigInteger.valueOf(k);
            nuevoObjetoDB.setSecuencia(l);
            listaObjetosDBCrear.add(nuevoObjetoDB);
            listaObjetosDB.add(0, nuevoObjetoDB);
            objetoDBSeleccionado = nuevoObjetoDB;
            RequestContext.getCurrentInstance().update("form:datosObjetosDB");
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().execute("PF('nuevoRegistroObjetosBD').hide()");
            nuevoObjetoDB = new ObjetosDB();

        } else {
            RequestContext.getCurrentInstance().update("form:validacionNuevaCentroCosto");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevaCentroCosto').show()");
            contador = 0;
        }
    }

    public void limpiarNuevoObjetoDB() {
        nuevoObjetoDB = new ObjetosDB();
    }

    public void duplicandoObjetosDB() {
        if (objetoDBSeleccionado != null) {
            duplicarObjetoDB = new ObjetosDB();
            k++;
            l = BigInteger.valueOf(k);
            duplicarObjetoDB.setSecuencia(l);
            duplicarObjetoDB.setAutorizada(objetoDBSeleccionado.getAutorizada());
            duplicarObjetoDB.setClasificacion(objetoDBSeleccionado.getClasificacion());
            duplicarObjetoDB.setDescripcion(objetoDBSeleccionado.getDescripcion());
            duplicarObjetoDB.setExcluirasignacionperfil(objetoDBSeleccionado.getExcluirasignacionperfil());
            duplicarObjetoDB.setModulo(objetoDBSeleccionado.getModulo());
            duplicarObjetoDB.setNombre(objetoDBSeleccionado.getNombre());
            duplicarObjetoDB.setTipo(objetoDBSeleccionado.getTipo());
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

        if (duplicarObjetoDB.getNombre() == (null) || duplicarObjetoDB.getNombre().equals("")) {
            mensajeValidacion = " Los campos marcados con asterisco son obligatorios";
        } else {
            for (int x = 0; x < listaObjetosDB.size(); x++) {
                if (listaObjetosDB.get(x).getNombre().equals(duplicarObjetoDB.getNombre())) {
                    duplicados++;
                }
            }
            if (duplicados > 0) {
                mensajeValidacion = "El Nombre ingresado ya está registrado. Por favor ingrese un Nombre válido";
            } else {
                contador++;
            }
        }
        if (duplicarObjetoDB.getModulo() == (null)) {
            mensajeValidacion = " Los campos marcados con asterisco son obligatorios";
        } else {
            contador++;
        }
        if (duplicarObjetoDB.getTipo() == null) {
            duplicarObjetoDB.setTipo("FUNCTION");
        }

        if (contador == 2) {
            listaObjetosDB.add(0, duplicarObjetoDB);
            listaObjetosDBCrear.add(duplicarObjetoDB);
            objetoDBSeleccionado = duplicarObjetoDB;
            RequestContext.getCurrentInstance().update("form:datosObjetosDB");
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            if (bandera == 1) {
                tipo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosObjetosDB:tipo");
                tipo.setFilterStyle("display: none; visibility: hidden;");
                nombre = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosObjetosDB:nombre");
                nombre.setFilterStyle("display: none; visibility: hidden;");
                modulo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosObjetosDB:modulo");
                modulo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosObjetosDB:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                clasificacion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosObjetosDB:clasificacion");
                clasificacion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosObjetosDB");
                bandera = 0;
                listaObjetosDBFiltrar = null;
                tipoLista = 0;
                altoTabla = "270";
            }
            duplicarObjetoDB = new ObjetosDB();
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroMotivosDefinitivas').hide()");

        } else {
            contador = 0;
            RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
            RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
        }
    }

    public void limpiarDuplicarObjetosDB() {
        duplicarObjetoDB = new ObjetosDB();
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosObjetosDBExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "MotivosDefinitivas", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosObjetosDBExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "MotivosDefinitivas", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void verificarRastro() {
        if (objetoDBSeleccionado != null) {
            int resultado = administrarRastros.obtenerTabla(objetoDBSeleccionado.getSecuencia(), "OBJETOSDB"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
        } else if (administrarRastros.verificarHistoricosTabla("OBJETOSDB")) { // igual acá
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
    }

    public void salir() {
        limpiarListasValor();
        if (bandera == 1) {
            tipo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosObjetosDB:tipo");
            tipo.setFilterStyle("display: none; visibility: hidden;");
            nombre = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosObjetosDB:nombre");
            nombre.setFilterStyle("display: none; visibility: hidden;");
            modulo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosObjetosDB:modulo");
            modulo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosObjetosDB:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            clasificacion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosObjetosDB:clasificacion");
            clasificacion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosObjetosDB");
            bandera = 0;
            listaObjetosDBFiltrar = null;
            tipoLista = 0;
            altoTabla = "315";
        }
        listaObjetosDBBorrar.clear();
        listaObjetosDBCrear.clear();
        listaObjetosDBModificar.clear();
        objetoDBSeleccionado = null;
        k = 0;
        listaObjetosDB = null;
        guardado = true;
        RequestContext.getCurrentInstance().update("form:datosObjetosDB");
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

    public void actualizarModulo() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            objetoDBSeleccionado.setModulo(moduloSeleccionado);
            if (!listaObjetosDBCrear.contains(objetoDBSeleccionado)) {
                if (listaObjetosDBModificar.isEmpty()) {
                    listaObjetosDBModificar.add(objetoDBSeleccionado);
                } else if (!listaObjetosDBModificar.contains(objetoDBSeleccionado)) {
                    listaObjetosDBModificar.add(objetoDBSeleccionado);
                }
            }
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            deshabilitarBotonLov();
            RequestContext.getCurrentInstance().update("form:datosObjetosDB");
        } else if (tipoActualizacion == 1) {
            nuevoObjetoDB.setModulo(moduloSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoObj");
        } else if (tipoActualizacion == 2) {
            duplicarObjetoDB.setModulo(moduloSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarObj");
        }
        lovModulosFiltrar = null;
        moduloSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        RequestContext.getCurrentInstance().update("formularioDialogos:modulosDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovModulos");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarM");
        context.reset("formularioDialogos:lovModulos:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovModulos').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('modulosDialogo').hide()");
    }

    public void cancelarCambioModulo() {
        lovModulosFiltrar = null;
        moduloSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        RequestContext.getCurrentInstance().update("formularioDialogos:modulosDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovModulos");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarM");
        RequestContext.getCurrentInstance().reset("formularioDialogos:lovModulos:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovModulos').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('modulosDialogo').hide()");
    }

    ////SET Y GET////
    public List<ObjetosDB> getListaObjetosDB() {
        if (listaObjetosDB == null) {
            listaObjetosDB = administrarObjetosDb.consultarObjetosDB();
        }
        return listaObjetosDB;
    }

    public void setListaObjetosDB(List<ObjetosDB> listaObjetosDB) {
        this.listaObjetosDB = listaObjetosDB;
    }

    public List<ObjetosDB> getListaObjetosDBFiltrar() {
        return listaObjetosDBFiltrar;
    }

    public void setListaObjetosDBFiltrar(List<ObjetosDB> listaObjetosDBFiltrar) {
        this.listaObjetosDBFiltrar = listaObjetosDBFiltrar;
    }

    public ObjetosDB getNuevoObjetoDB() {
        return nuevoObjetoDB;
    }

    public void setNuevoObjetoDB(ObjetosDB nuevoObjetoDB) {
        this.nuevoObjetoDB = nuevoObjetoDB;
    }

    public ObjetosDB getDuplicarObjetoDB() {
        return duplicarObjetoDB;
    }

    public void setDuplicarObjetoDB(ObjetosDB duplicarObjetoDB) {
        this.duplicarObjetoDB = duplicarObjetoDB;
    }

    public ObjetosDB getEditarObjetoDB() {
        return editarObjetoDB;
    }

    public void setEditarObjetoDB(ObjetosDB editarObjetoDB) {
        this.editarObjetoDB = editarObjetoDB;
    }

    public ObjetosDB getObjetoDBSeleccionado() {
        return objetoDBSeleccionado;
    }

    public void setObjetoDBSeleccionado(ObjetosDB objetoDBSeleccionado) {
        this.objetoDBSeleccionado = objetoDBSeleccionado;
    }

    public List<Modulos> getLovModulos() {
        if (lovModulos == null) {
            lovModulos = administrarObjetosDb.consultarModulos();
        }
        return lovModulos;
    }

    public void setLovModulos(List<Modulos> lovModulos) {
        this.lovModulos = lovModulos;
    }

    public List<Modulos> getLovModulosFiltrar() {
        return lovModulosFiltrar;
    }

    public void setLovModulosFiltrar(List<Modulos> lovModulosFiltrar) {
        this.lovModulosFiltrar = lovModulosFiltrar;
    }

    public Modulos getModuloSeleccionado() {
        return moduloSeleccionado;
    }

    public void setModuloSeleccionado(Modulos moduloSeleccionado) {
        this.moduloSeleccionado = moduloSeleccionado;
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
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosObjetosDB");
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

    public boolean isActivarLov() {
        return activarLov;
    }

    public void setActivarLov(boolean activarLov) {
        this.activarLov = activarLov;
    }

    public String getInfoRegistroModulos() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovModulos");
        infoRegistroModulos = String.valueOf(tabla.getRowCount());
        return infoRegistroModulos;
    }

    public void setInfoRegistroModulos(String infoRegistroModulos) {
        this.infoRegistroModulos = infoRegistroModulos;
    }

}
