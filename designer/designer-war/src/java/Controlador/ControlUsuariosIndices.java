/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import ControlNavegacion.ControlListaNavegacion;
import Entidades.Indices;
import Entidades.Usuarios;
import Entidades.UsuariosIndices;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarRastrosInterface;
import InterfaceAdministrar.AdministrarUsuariosIndicesInterface;
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
@Named(value = "controlUsuariosIndices")
@SessionScoped
public class ControlUsuariosIndices implements Serializable {

    private static Logger log = Logger.getLogger(ControlUsuariosIndices.class);

    @EJB
    AdministrarUsuariosIndicesInterface administrarUsuariosIndices;
    @EJB
    AdministrarRastrosInterface administrarRastros;

    //tabla de arriba
    private List<Usuarios> listaUsuarios;
    private List<Usuarios> listaUsuariosFiltrar;
    private Usuarios usuarioSeleccionado;
    private Usuarios editarUsuario;
    private Usuarios usuarioAux;
    //tabla de abajo
    private List<UsuariosIndices> listaUsuariosIndices;
    private List<UsuariosIndices> listaUsuariosIndicesFiltrar;
    private List<UsuariosIndices> listaUsuariosIndicesCrear;
    private List<UsuariosIndices> listaUsuariosIndicesModificar;
    private List<UsuariosIndices> listaUsuariosIndicesBorrar;
    private UsuariosIndices usuarioIndicesSeleccionado;
    private UsuariosIndices nuevoUsuarioIndices;
    private UsuariosIndices duplicarUsuarioIndices;
    private UsuariosIndices editarUsuarioIndices;
    //lov UsuariosIndices
    private List<UsuariosIndices> lovUsuariosIndices;
    private List<UsuariosIndices> lovUsuariosIndicesFiltrar;
    private UsuariosIndices usuarioIndiceLovSeleccionado;
    // lov Indices
    private List<Indices> lovIndices;
    private List<Indices> lovIndicesFiltrar;
    private Indices indiceLovSeleccionado;
    //Columnas Usuario
    private Column alias, perfil, nombre;
    private Column codigo, indice;
    //otros
    private int cualCelda, cualCeldaIndices, tipoLista, tipoActualizacion, k, bandera;
    private BigInteger l;
    private boolean aceptar, guardado;
    private String mensajeValidacion, altoTabla, altoTablaIndices;
    private boolean activarLov, mostrarTodos, buscar;
    private int cualTabla;
    private String infoRegistroUsuario, infoRegistroUsuarioIndices, infoRegistroLovIndices, infoRegistroLovUsuariosIndices;
    private String paginaAnterior = "nominaf";
    private String tablaImprimir, nombreArchivo;
    private String msgError;
    private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

    public ControlUsuariosIndices() {
        listaUsuarios = null;
        lovIndices = null;
        lovUsuariosIndices = null;
        listaUsuariosIndicesBorrar = new ArrayList<UsuariosIndices>();
        listaUsuariosIndicesCrear = new ArrayList<UsuariosIndices>();
        listaUsuariosIndicesModificar = new ArrayList<UsuariosIndices>();
        editarUsuario = new Usuarios();
        editarUsuarioIndices = new UsuariosIndices();
        nuevoUsuarioIndices = new UsuariosIndices();
        nuevoUsuarioIndices.setUsuario(new Usuarios());
        nuevoUsuarioIndices.setIndice(new Indices());
        duplicarUsuarioIndices = new UsuariosIndices();
        guardado = true;
        altoTabla = "60";
        altoTablaIndices = "170";
        cualTabla = -1;
        cualCelda = -1;
        cualCeldaIndices = -1;
        usuarioSeleccionado = null;
        usuarioIndicesSeleccionado = null;
        activarLov = true;
        mapParametros.put("paginaAnterior", paginaAnterior);
        mostrarTodos = true;
        buscar = false;
        usuarioAux = new Usuarios();
        tablaImprimir = "";
        nombreArchivo = "";
    }

    public void limpiarListasValor() {
        lovIndices = null;
        lovUsuariosIndices = null;
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
            administrarUsuariosIndices.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
            listaUsuarios = null;
            getListaUsuarios();
            if (listaUsuarios != null) {
                if (!listaUsuarios.isEmpty()) {
                    usuarioSeleccionado = listaUsuarios.get(0);
                }
            }
        } catch (Exception e) {
            log.error("Error postconstruct " + this.getClass().getName() + ":  ", e);
            log.error("Causa: " + e.getCause());
        }
    }

    public void recibirPaginaEntrante(String pagina) {
        paginaAnterior = pagina;
    }

    public void recibirParametros(Map<String, Object> map) {
        mapParametros = map;
        paginaAnterior = (String) mapParametros.get("paginaAnterior");
    }

    public void navegar(String pag) {
        FacesContext fc = FacesContext.getCurrentInstance();
        ControlListaNavegacion controlListaNavegacion = (ControlListaNavegacion) fc.getApplication().evaluateExpressionGet(fc, "#{controlListaNavegacion}", ControlListaNavegacion.class);
        String pagActual = "usuarioindice";
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

    public void cambiarIndice(Usuarios usu, int celda) {
        usuarioSeleccionado = usu;
        cualCelda = celda;
        usuarioSeleccionado.getSecuencia();
        cualTabla = 1;
        deshabilitarBotonLov();
        if (cualCelda == 0) {
            usuarioSeleccionado.getAlias();
        } else if (cualCelda == 1) {
            usuarioSeleccionado.getPerfil().getDescripcion();
        } else if (cualCelda == 2) {
            usuarioSeleccionado.getPersona().getNombreCompleto();
        }
        RequestContext.getCurrentInstance().update("form:datosUsuariosIndices");
        contarRegistrosUsuariosIndices();
        buscar = false;
        RequestContext.getCurrentInstance().update("form:buscar");
    }

    public void cambiarIndiceUsuariosIndices(UsuariosIndices usu, int celda) {
        usuarioIndicesSeleccionado = usu;
        cualCeldaIndices = celda;
        usuarioIndicesSeleccionado.getSecuencia();
        cualTabla = 2;
        if (cualCeldaIndices == 0) {
            habilitarBotonLov();
            usuarioIndicesSeleccionado.getIndice().getCodigo();
        } else if (cualCeldaIndices == 1) {
            deshabilitarBotonLov();
            usuarioIndicesSeleccionado.getIndice().getDescripcion();
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void cancelarModificacion() {
        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();

            alias = (Column) c.getViewRoot().findComponent("form:datosUsuarios:alias");
            alias.setFilterStyle("display: none; visibility: hidden;");
            perfil = (Column) c.getViewRoot().findComponent("form:datosUsuarios:perfil");
            perfil.setFilterStyle("display: none; visibility: hidden;");
            nombre = (Column) c.getViewRoot().findComponent("form:datosUsuarios:nombre");
            nombre.setFilterStyle("display: none; visibility: hidden;");
            codigo = (Column) c.getViewRoot().findComponent("form:datosUsuariosIndices:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            indice = (Column) c.getViewRoot().findComponent("form:datosUsuariosIndices:indice");
            indice.setFilterStyle("display: none; visibility: hidden;");
            bandera = 0;
            listaUsuariosFiltrar = null;
            altoTabla = "60";
            altoTabla = "190";
            RequestContext.getCurrentInstance().update("form:datosUsuarios");
            RequestContext.getCurrentInstance().update("form:datosUsuariosIndices");
            tipoLista = 0;
        }
        RequestContext.getCurrentInstance().execute("PF('datosUsuarios').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('datosUsuariosIndices').clearFilters()");
        listaUsuariosIndicesBorrar.clear();
        listaUsuariosIndicesCrear.clear();
        listaUsuariosIndicesModificar.clear();
        usuarioSeleccionado = null;
        contarRegistrosUsuarios();
        listaUsuarios = null;
        getListaUsuarios();
        usuarioIndicesSeleccionado = null;
        if (listaUsuarios != null) {
            if (!listaUsuarios.isEmpty()) {
                usuarioSeleccionado = listaUsuarios.get(0);
            }
        }
        k = 0;
        guardado = true;
        RequestContext context = RequestContext.getCurrentInstance();
        contarRegistrosUsuariosIndices();
        RequestContext.getCurrentInstance().update("form:datosUsuarios");
        RequestContext.getCurrentInstance().update("form:datosUsuariosIndices");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void salir() {
        limpiarListasValor();
        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            alias = (Column) c.getViewRoot().findComponent("form:datosUsuarios:alias");
            alias.setFilterStyle("display: none; visibility: hidden;");
            perfil = (Column) c.getViewRoot().findComponent("form:datosUsuarios:perfil");
            perfil.setFilterStyle("display: none; visibility: hidden;");
            nombre = (Column) c.getViewRoot().findComponent("form:datosUsuarios:nombre");
            nombre.setFilterStyle("display: none; visibility: hidden;");
            codigo = (Column) c.getViewRoot().findComponent("form:datosUsuariosIndices:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            indice = (Column) c.getViewRoot().findComponent("form:datosUsuariosIndices:indice");
            indice.setFilterStyle("display: none; visibility: hidden;");

            RequestContext.getCurrentInstance().update("form:datosUsuarios");
            RequestContext.getCurrentInstance().update("form:datosUsuariosIndices");
            bandera = 0;
            listaUsuariosFiltrar = null;
            tipoLista = 0;
            altoTabla = "60";
            altoTablaIndices = "170";
        }
        RequestContext.getCurrentInstance().execute("PF('datosUsuarios').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('datosUsuariosIndices').clearFilters()");
        listaUsuariosIndicesBorrar.clear();
        listaUsuariosIndicesCrear.clear();
        listaUsuariosIndicesModificar.clear();
        usuarioIndicesSeleccionado = null;
        usuarioSeleccionado = null;
        k = 0;
        listaUsuariosIndices = null;
        guardado = true;
        navegar("atras");
        mostrarTodos = true;
        buscar = false;
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            altoTabla = "40";
            altoTablaIndices = "150";
            alias = (Column) c.getViewRoot().findComponent("form:datosUsuarios:alias");
            alias.setFilterStyle("width: 85% !important;");
            perfil = (Column) c.getViewRoot().findComponent("form:datosUsuarios:perfil");
            perfil.setFilterStyle("width: 85% !important;");
            nombre = (Column) c.getViewRoot().findComponent("form:datosUsuarios:nombre");
            nombre.setFilterStyle("width: 85% !important;");
            codigo = (Column) c.getViewRoot().findComponent("form:datosUsuariosIndices:codigo");
            codigo.setFilterStyle("width: 85% !important;");
            indice = (Column) c.getViewRoot().findComponent("form:datosUsuariosIndices:indice");
            indice.setFilterStyle("width: 85% !important;");
            RequestContext.getCurrentInstance().update("form:datosUsuarios");
            RequestContext.getCurrentInstance().update("form:datosUsuariosIndices");
            bandera = 1;
        } else if (bandera == 1) {
            altoTabla = "60";
            altoTablaIndices = "170";
            alias = (Column) c.getViewRoot().findComponent("form:datosUsuarios:alias");
            alias.setFilterStyle("display: none; visibility: hidden;");
            perfil = (Column) c.getViewRoot().findComponent("form:datosUsuarios:perfil");
            perfil.setFilterStyle("display: none; visibility: hidden;");
            nombre = (Column) c.getViewRoot().findComponent("form:datosUsuarios:nombre");
            nombre.setFilterStyle("display: none; visibility: hidden;");
            codigo = (Column) c.getViewRoot().findComponent("form:datosUsuariosIndices:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            indice = (Column) c.getViewRoot().findComponent("form:datosUsuariosIndices:indice");
            indice.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosUsuarios");
            RequestContext.getCurrentInstance().update("form:datosUsuariosIndices");
            bandera = 0;
            listaUsuariosFiltrar = null;
            tipoLista = 0;
            RequestContext.getCurrentInstance().execute("PF('datosUsuarios').clearFilters()");
            RequestContext.getCurrentInstance().execute("PF('datosUsuariosIndices').clearFilters()");
        }
    }

    public void eventoFiltrarUsuarios() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
        contarRegistrosUsuarios();
    }

    public void eventoFiltrarUsuariosIndices() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
        contarRegistrosUsuariosIndices();
    }

    public void contarRegistrosUsuarios() {
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
    }

    public void contarRegistrosUsuariosIndices() {
        RequestContext.getCurrentInstance().update("form:informacionRegistroIndices");
    }

    public void contarRegistrosLovUsuariosIndices() {
        RequestContext.getCurrentInstance().update("formularioDialogos:informacionRegistroIndices");
    }

    public void contarRegistrosLovIndices() {
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroIndices");
    }

    public void deshabilitarBotonLov() {
        activarLov = true;
        RequestContext.getCurrentInstance().update("form:listaValores");
    }

    public void habilitarBotonLov() {
        activarLov = false;
        RequestContext.getCurrentInstance().update("form:listaValores");
    }

    public void editarCelda() {
        if (cualTabla == 1) {
            editarUsuario = usuarioSeleccionado;
            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editAlias");
                RequestContext.getCurrentInstance().execute("PF('editAlias').show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {

                RequestContext.getCurrentInstance().update("formularioDialogos:editPerfil");
                RequestContext.getCurrentInstance().execute("PF('editPerfil').show()");
                cualCelda = -1;
            } else if (cualCelda == 2) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editNombre");
                RequestContext.getCurrentInstance().execute("PF('editNombre').show()");
                cualCelda = -1;
            }
        } else if (cualTabla == 2) {
            editarUsuarioIndices = usuarioIndicesSeleccionado;
            if (cualCeldaIndices == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editCodigo");
                RequestContext.getCurrentInstance().execute("PF('editCodigo').show()");
                cualCeldaIndices = -1;
            } else if (cualCeldaIndices == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editIndice");
                RequestContext.getCurrentInstance().execute("PF('editIndice').show()");
                cualCeldaIndices = -1;
            }
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void posicionUsuario() {
        FacesContext context = FacesContext.getCurrentInstance();
        cualTabla = 1;
        Map<String, String> map = context.getExternalContext().getRequestParameterMap();
        String name = map.get("n"); // name attribute of node
        String type = map.get("t"); // type attribute of node
        int indice = Integer.parseInt(type);
        int columna = Integer.parseInt(name);
        usuarioSeleccionado = listaUsuarios.get(indice);
        cualCelda = columna;
        cambiarIndice(usuarioSeleccionado, columna);
    }

    ////métodos tabla usuarios Inforeportes 
    public void modificarUsuariosIndices(UsuariosIndices usuIndices) {
        usuarioIndicesSeleccionado = usuIndices;
        if (!listaUsuariosIndicesCrear.contains(usuarioIndicesSeleccionado)) {
            if (listaUsuariosIndicesModificar.isEmpty()) {
                listaUsuariosIndicesModificar.add(usuarioIndicesSeleccionado);
            } else if (!listaUsuariosIndicesModificar.contains(usuarioIndicesSeleccionado)) {
                listaUsuariosIndicesModificar.add(usuarioIndicesSeleccionado);
            }
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
        RequestContext.getCurrentInstance().update("form:datosUsuariosIndices");
    }

    public void borrarUsuarioIndices() {
        if (usuarioIndicesSeleccionado != null) {
            if (!listaUsuariosIndicesModificar.isEmpty() && listaUsuariosIndicesModificar.contains(usuarioIndicesSeleccionado)) {
                listaUsuariosIndicesModificar.remove(listaUsuariosIndicesModificar.indexOf(usuarioIndicesSeleccionado));
                listaUsuariosIndicesBorrar.add(usuarioIndicesSeleccionado);
            } else if (!listaUsuariosIndicesCrear.isEmpty() && listaUsuariosIndicesCrear.contains(usuarioIndicesSeleccionado)) {
                listaUsuariosIndicesCrear.remove(listaUsuariosIndicesCrear.indexOf(usuarioIndicesSeleccionado));
            } else {
                listaUsuariosIndicesBorrar.add(usuarioIndicesSeleccionado);
            }
            listaUsuariosIndices.remove(usuarioIndicesSeleccionado);
            if (tipoLista == 1) {
                listaUsuariosIndicesFiltrar.remove(usuarioIndicesSeleccionado);
            }
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosUsuariosIndices");
            contarRegistrosUsuariosIndices();
            usuarioIndicesSeleccionado = null;
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void revisarDialogoGuardar() {
        if (!listaUsuariosIndicesBorrar.isEmpty() || !listaUsuariosIndicesCrear.isEmpty() || !listaUsuariosIndicesModificar.isEmpty()) {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:confirmarGuardar");
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
        }
    }

    public void guardarUsuariosIndices() {
        RequestContext context = RequestContext.getCurrentInstance();
        try {
            if (guardado == false) {
                msgError = "";
                if (!listaUsuariosIndicesBorrar.isEmpty()) {
                    for (int i = 0; i < listaUsuariosIndicesBorrar.size(); i++) {
                        msgError = administrarUsuariosIndices.borrar(listaUsuariosIndicesBorrar.get(i));
                    }
                    listaUsuariosIndicesBorrar.clear();
                }
                if (!listaUsuariosIndicesModificar.isEmpty()) {
                    for (int i = 0; i < listaUsuariosIndicesModificar.size(); i++) {
                        msgError = administrarUsuariosIndices.editar(listaUsuariosIndicesModificar.get(i));
                    }
                    listaUsuariosIndicesModificar.clear();
                }
                if (!listaUsuariosIndicesCrear.isEmpty()) {
                    for (int i = 0; i < listaUsuariosIndicesCrear.size(); i++) {
                        msgError = administrarUsuariosIndices.crear(listaUsuariosIndicesCrear.get(i));
                    }
                    listaUsuariosIndicesCrear.clear();
                }

                if (msgError.equals("EXITO")) {
                    k = 0;
                    FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                    RequestContext.getCurrentInstance().update("form:growl");
                    contarRegistrosUsuariosIndices();
                    usuarioIndicesSeleccionado = null;
                    guardado = true;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                    RequestContext.getCurrentInstance().update("form:datosUsuariosIndices");
                } else {
                    RequestContext.getCurrentInstance().update("formularioDialogos:errorGuardadoBD");
                    RequestContext.getCurrentInstance().execute("PF('errorGuardadoBD').show()");
                }
            }
        } catch (Exception e) {
            log.error("Error guardarCambios : " + e.toString());
            FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado, intente nuevamente.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }

    }

    public void agregarNuevoUsuarioIndices() {
        int contador = 0;
        int duplicados = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        mensajeValidacion = " ";

        if (nuevoUsuarioIndices.getIndice() == null) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            contador++;
        }
        if (usuarioSeleccionado == null) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            contador++;
        }

        for (int i = 0; i < listaUsuariosIndices.size(); i++) {
            if (listaUsuariosIndices.get(i).getUsuario().equals(nuevoUsuarioIndices.getUsuario()) && listaUsuariosIndices.get(i).getIndice().equals(nuevoUsuarioIndices.getIndice())) {
                duplicados++;
            }
        }

        if (contador == 0) {
            if (duplicados == 0) {
                if (bandera == 1) {
                    FacesContext c = FacesContext.getCurrentInstance();
                    codigo = (Column) c.getViewRoot().findComponent("form:datosUsuariosIndices:codigo");
                    codigo.setFilterStyle("display: none; visibility: hidden;");
                    indice = (Column) c.getViewRoot().findComponent("form:datosUsuariosIndices:reporte");
                    indice.setFilterStyle("display: none; visibility: hidden;");
                    bandera = 0;
                    listaUsuariosIndicesFiltrar = null;
                    tipoLista = 0;
                    altoTablaIndices = "170";
                    RequestContext.getCurrentInstance().update("form:datosUsuariosIndices");
                }
                k++;
                l = BigInteger.valueOf(k);
                nuevoUsuarioIndices.setSecuencia(l);
                nuevoUsuarioIndices.setUsuario(usuarioSeleccionado);
                listaUsuariosIndicesCrear.add(nuevoUsuarioIndices);
                listaUsuariosIndices.add(0,nuevoUsuarioIndices);
                usuarioIndicesSeleccionado = nuevoUsuarioIndices;
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
                contarRegistrosUsuariosIndices();
                RequestContext.getCurrentInstance().update("form:datosUsuariosIndices");
                RequestContext.getCurrentInstance().execute("PF('nuevoRegistroUsuarioIndices').hide()");
                nuevoUsuarioIndices = new UsuariosIndices();
            } else {
                RequestContext.getCurrentInstance().update("formularioDialogos:existeUsuarioIndices");
                RequestContext.getCurrentInstance().execute("PF('existeUsuarioIndices').show()");
            }
        } else {
            RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevoUsuarioIndices");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevoUsuarioIndices').show()");
        }
    }

    public void limpiarNuevoUsuarioI() {
        nuevoUsuarioIndices = new UsuariosIndices();
    }

    public void limpiarUsuarioAux() {
        usuarioAux = new Usuarios();
    }

    public void exportXML() {
        if (cualTabla == 1) {
            nombreArchivo = "Usuarios";
            tablaImprimir = "formExportar:datosUsuariosExportar";
            limpiarUsuarioAux();
        } else if (cualTabla == 2) {
            nombreArchivo = "UsuariosInfoRetencion";
            tablaImprimir = "formExportar:datosUsuariosIndicesExportar";
            limpiarNuevoUsuarioI();
        }
    }

    public void duplicandoUsuariosIndices() {
        if (usuarioIndicesSeleccionado != null) {
            duplicarUsuarioIndices = new UsuariosIndices();
            duplicarUsuarioIndices.setIndice(new Indices());
            k++;
            l = BigInteger.valueOf(k);

            duplicarUsuarioIndices.setSecuencia(l);
            duplicarUsuarioIndices.setUsuario(usuarioIndicesSeleccionado.getUsuario());
            duplicarUsuarioIndices.setIndice(usuarioIndicesSeleccionado.getIndice());

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarUsuarioIndices");
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroUsuarioIndices').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void confirmarDuplicar() {
        int contador = 0;
        int duplicados = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        mensajeValidacion = " ";

        if (duplicarUsuarioIndices.getIndice() == null) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            contador++;
        }
        if (duplicarUsuarioIndices.getUsuario() == null) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            contador++;
        }

        for (int i = 0; i < listaUsuariosIndices.size(); i++) {
            if (listaUsuariosIndices.get(i).getUsuario().equals(duplicarUsuarioIndices.getUsuario()) && listaUsuariosIndices.get(i).getIndice().equals(duplicarUsuarioIndices.getIndice())) {
                duplicados++;
            }
        }

        if (contador == 0) {
            if (duplicados == 0) {
                listaUsuariosIndicesCrear.add(duplicarUsuarioIndices);
                listaUsuariosIndices.add(0,duplicarUsuarioIndices);
                usuarioIndicesSeleccionado = duplicarUsuarioIndices;
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
                contarRegistrosUsuariosIndices();
                RequestContext.getCurrentInstance().update("form:datosUsuariosIndices");

                if (bandera == 1) {
                    FacesContext c = FacesContext.getCurrentInstance();
                    //CERRAR FILTRADO
                    codigo = (Column) c.getViewRoot().findComponent("form:datosUsuariosIndices:codigo");
                    codigo.setFilterStyle("display: none; visibility: hidden;");
                    indice = (Column) c.getViewRoot().findComponent("form:datosUsuariosIndices:indice");
                    indice.setFilterStyle("display: none; visibility: hidden;");
                    bandera = 0;
                    listaUsuariosIndicesFiltrar = null;
                    RequestContext.getCurrentInstance().update("form:datosUsuariosIndices");
                    tipoLista = 0;
                    altoTablaIndices = "170";
                }
                duplicarUsuarioIndices = new UsuariosIndices();
                RequestContext.getCurrentInstance().update("formularioDialogos:duplicarRegistroUsuarioIndices");
                RequestContext.getCurrentInstance().execute("PF('duplicarRegistroUsuarioIndices').hide()");
            } else {
                RequestContext.getCurrentInstance().update("formularioDialogos:existeUsuarioIndices");
                RequestContext.getCurrentInstance().execute("PF('existeUsuarioIndices').show()");
            }
        } else {
            RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevoUsuarioIndices");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevoUsuarioIndices').show()");
        }
    }

    public void limpiarDuplicarUsuariosIndices() {
        duplicarUsuarioIndices = new UsuariosIndices();
    }

    public void asignarIndex(UsuariosIndices usuIndices, int dlg, int LND) {
        usuarioIndicesSeleccionado = usuIndices;
        tipoActualizacion = LND;
        if (dlg == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:indicesDialogo");
            RequestContext.getCurrentInstance().execute("PF('indicesDialogo').show()");
        }
    }

    public void listaValoresBoton() {
        if (usuarioIndicesSeleccionado != null) {
            if (cualCeldaIndices == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:indicesDialogo");
                RequestContext.getCurrentInstance().execute("PF('indicesDialogo').show()");
            }
        }
    }

    public void actualizarIndices() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            usuarioIndicesSeleccionado.setIndice(indiceLovSeleccionado);
            if (!listaUsuariosIndicesCrear.contains(usuarioIndicesSeleccionado)) {
                if (listaUsuariosIndicesModificar.isEmpty()) {
                    listaUsuariosIndicesModificar.add(usuarioIndicesSeleccionado);
                } else if (!listaUsuariosIndicesModificar.contains(usuarioIndicesSeleccionado)) {
                    listaUsuariosIndicesModificar.add(usuarioIndicesSeleccionado);
                }
            }
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosUsuariosIndices");
        } else if (tipoActualizacion == 1) {
            nuevoUsuarioIndices.setIndice(indiceLovSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoUsuarioIndices");
        } else if (tipoActualizacion == 2) {
            duplicarUsuarioIndices.setIndice(indiceLovSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarUsuarioIndices");
        }
        lovIndices = null;
        indiceLovSeleccionado = null;
        tipoActualizacion = -1;
        cualCeldaIndices = -1;

        RequestContext.getCurrentInstance().update("formularioDialogos:indicesDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovIndices");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarU");
        context.reset("formularioDialogos:lovIndices:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovIndices').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('indicesDialogo').hide()");
    }

    public void cancelarCambioIndice() {
        lovIndices = null;
        indiceLovSeleccionado = null;
        tipoActualizacion = -1;
        cualCeldaIndices = -1;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("formularioDialogos:indicesDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovIndices");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarU");
        context.reset("formularioDialogos:lovIndices:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovIndices').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('indicesDialogo').hide()");
    }

    public void mostrarDialogoBuscar() {
        lovUsuariosIndices = null;
        lovUsuariosIndices = administrarUsuariosIndices.listaUsuariosIndices(usuarioSeleccionado.getSecuencia());
        contarRegistrosLovUsuariosIndices();
        RequestContext.getCurrentInstance().update("formularioDialogos:usuariosIndicesDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovUsuariosIndices");
        RequestContext.getCurrentInstance().execute("PF('usuariosIndicesDialogo').show()");
        RequestContext.getCurrentInstance().execute("PF('operacionEnProceso').hide()");
        buscar = true;
        RequestContext.getCurrentInstance().update("form:buscar");
    }

    public void actualizarUsuarioIndices() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (!listaUsuariosIndices.isEmpty()) {
            listaUsuariosIndices.clear();
            listaUsuariosIndices.add(usuarioIndiceLovSeleccionado);
            usuarioIndicesSeleccionado = listaUsuariosIndices.get(0);
        }
        contarRegistrosUsuariosIndices();
        RequestContext.getCurrentInstance().update("form:datosUsuariosIndices");
        lovUsuariosIndicesFiltrar = null;
        usuarioIndiceLovSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCeldaIndices = -1;
        mostrarTodos = false;
        RequestContext.getCurrentInstance().update("form:mostrarTodos");
        RequestContext.getCurrentInstance().update("formularioDialogos:usuariosIndicesDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovUsuariosIndices");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarUIndices");
        RequestContext.getCurrentInstance().reset("formularioDialogos:lovUsuariosIndices:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovUsuariosIndices').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('usuariosIndicesDialogo').hide()");

    }

    public void cancelarCambioUsuarioIndices() {
        lovUsuariosIndicesFiltrar = null;
        usuarioIndiceLovSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCeldaIndices = -1;
        buscar = false;
        RequestContext.getCurrentInstance().update("form:buscar");
        RequestContext.getCurrentInstance().update("formularioDialogos:usuariosIndicesDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovUsuariosIndices");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarUIndices");
        RequestContext.getCurrentInstance().reset("formularioDialogos:lovUsuariosIndices:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovUsuariosIndices').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('usuariosIndicesDialogo').hide()");
    }

    public void mostrarTodosUsuariosIndices() {
        RequestContext.getCurrentInstance().execute("PF('operacionEnProceso').hide()");
        listaUsuariosIndices = null;
        getListaUsuariosIndices();
        if (listaUsuariosIndices != null) {
            if (!listaUsuariosIndices.isEmpty()) {
                usuarioIndicesSeleccionado = listaUsuariosIndices.get(0);
            }
        }
        contarRegistrosUsuariosIndices();
        RequestContext.getCurrentInstance().update("form:datosUsuariosIndices");
        buscar = false;
        RequestContext.getCurrentInstance().update("form:buscar");
        mostrarTodos = true;
        RequestContext.getCurrentInstance().update("form:mostrarTodos");

    }

    public void exportPDF() throws IOException {
        if (cualTabla == 1) {
            DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosUsuariosExportar");
            FacesContext context = FacesContext.getCurrentInstance();
            Exporter exporter = new ExportarPDF();
            exporter.export(context, tabla, "Usuarios", false, false, "UTF-8", null, null);
            context.responseComplete();
        } else if (cualTabla == 2) {
            DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosUsuariosIndicesExportar");
            FacesContext context = FacesContext.getCurrentInstance();
            Exporter exporter = new ExportarPDF();
            exporter.export(context, tabla, "UsuariosIndices", false, false, "UTF-8", null, null);
            context.responseComplete();
        }
    }

    public void exportXLS() throws IOException {
        if (cualTabla == 1) {
            DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosUsuariosExportar");
            FacesContext context = FacesContext.getCurrentInstance();
            Exporter exporter = new ExportarXLS();
            exporter.export(context, tabla, "Usuarios", false, false, "UTF-8", null, null);
            context.responseComplete();
        } else if (cualTabla == 2) {
            DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosUsuariosIndicesExportar");
            FacesContext context = FacesContext.getCurrentInstance();
            Exporter exporter = new ExportarXLS();
            exporter.export(context, tabla, "UsuariosIndices", false, false, "UTF-8", null, null);
            context.responseComplete();
        }
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (usuarioIndicesSeleccionado != null) {
            int resultado = administrarRastros.obtenerTabla(usuarioIndicesSeleccionado.getSecuencia(), "UsuariosIndices");
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
        } else if (administrarRastros.verificarHistoricosTabla("UsuariosIndices")) { // igual acá
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
    }

    public void guardarySalir() {
        guardarUsuariosIndices();
        salir();
    }

    ////sets y gets////////////
    public List<Usuarios> getListaUsuarios() {
        if (listaUsuarios == null) {
            listaUsuarios = administrarUsuariosIndices.listaUsuarios();
        }
        return listaUsuarios;
    }

    public void setListaUsuarios(List<Usuarios> listaUsuarios) {
        this.listaUsuarios = listaUsuarios;
    }

    public List<Usuarios> getListaUsuariosFiltrar() {
        return listaUsuariosFiltrar;
    }

    public void setListaUsuariosFiltrar(List<Usuarios> listaUsuariosFiltrar) {
        this.listaUsuariosFiltrar = listaUsuariosFiltrar;
    }

    public Usuarios getUsuarioSeleccionado() {
        return usuarioSeleccionado;
    }

    public void setUsuarioSeleccionado(Usuarios usuarioSeleccionado) {
        this.usuarioSeleccionado = usuarioSeleccionado;
    }

    public Usuarios getEditarUsuario() {
        return editarUsuario;
    }

    public void setEditarUsuario(Usuarios editarUsuario) {
        this.editarUsuario = editarUsuario;
    }

    public Usuarios getUsuarioAux() {
        return usuarioAux;
    }

    public void setUsuarioAux(Usuarios usuarioAux) {
        this.usuarioAux = usuarioAux;
    }

    public List<UsuariosIndices> getListaUsuariosIndices() {
        if (usuarioSeleccionado != null) {
            if (listaUsuariosIndices == null) {
                listaUsuariosIndices = administrarUsuariosIndices.listaUsuariosIndices(usuarioSeleccionado.getSecuencia());
            }
        }
        return listaUsuariosIndices;
    }

    public void setListaUsuariosIndices(List<UsuariosIndices> listaUsuariosIndices) {
        this.listaUsuariosIndices = listaUsuariosIndices;
    }

    public List<UsuariosIndices> getListaUsuariosIndicesFiltrar() {
        return listaUsuariosIndicesFiltrar;
    }

    public void setListaUsuariosIndicesFiltrar(List<UsuariosIndices> listaUsuariosIndicesFiltrar) {
        this.listaUsuariosIndicesFiltrar = listaUsuariosIndicesFiltrar;
    }

    public UsuariosIndices getUsuarioIndicesSeleccionado() {
        return usuarioIndicesSeleccionado;
    }

    public void setUsuarioIndicesSeleccionado(UsuariosIndices usuarioIndicesSeleccionado) {
        this.usuarioIndicesSeleccionado = usuarioIndicesSeleccionado;
    }

    public UsuariosIndices getNuevoUsuarioIndices() {
        return nuevoUsuarioIndices;
    }

    public void setNuevoUsuarioIndices(UsuariosIndices nuevoUsuarioIndices) {
        this.nuevoUsuarioIndices = nuevoUsuarioIndices;
    }

    public UsuariosIndices getDuplicarUsuarioIndices() {
        return duplicarUsuarioIndices;
    }

    public void setDuplicarUsuarioIndices(UsuariosIndices duplicarUsuarioIndices) {
        this.duplicarUsuarioIndices = duplicarUsuarioIndices;
    }

    public UsuariosIndices getEditarUsuarioIndices() {
        return editarUsuarioIndices;
    }

    public void setEditarUsuarioIndices(UsuariosIndices editarUsuarioIndices) {
        this.editarUsuarioIndices = editarUsuarioIndices;
    }

    public List<UsuariosIndices> getLovUsuariosIndices() {
        if (usuarioSeleccionado != null) {
            if (lovUsuariosIndices == null) {
                lovUsuariosIndices = administrarUsuariosIndices.listaUsuariosIndices(usuarioSeleccionado.getSecuencia());
            }
        }
        return lovUsuariosIndices;
    }

    public void setLovUsuariosIndices(List<UsuariosIndices> lovUsuariosIndices) {
        this.lovUsuariosIndices = lovUsuariosIndices;
    }

    public List<UsuariosIndices> getLovUsuariosIndicesFiltrar() {
        return lovUsuariosIndicesFiltrar;
    }

    public void setLovUsuariosIndicesFiltrar(List<UsuariosIndices> lovUsuariosIndicesFiltrar) {
        this.lovUsuariosIndicesFiltrar = lovUsuariosIndicesFiltrar;
    }

    public UsuariosIndices getUsuarioIndiceLovSeleccionado() {
        return usuarioIndiceLovSeleccionado;
    }

    public void setUsuarioIndiceLovSeleccionado(UsuariosIndices usuarioIndiceLovSeleccionado) {
        this.usuarioIndiceLovSeleccionado = usuarioIndiceLovSeleccionado;
    }

    public List<Indices> getLovIndices() {
        if (lovIndices == null) {
            lovIndices = administrarUsuariosIndices.lovIR();
        }
        return lovIndices;
    }

    public void setLovIndices(List<Indices> lovIndices) {
        this.lovIndices = lovIndices;
    }

    public List<Indices> getLovIndicesFiltrar() {
        return lovIndicesFiltrar;
    }

    public void setLovIndicesFiltrar(List<Indices> lovIndicesFiltrar) {
        this.lovIndicesFiltrar = lovIndicesFiltrar;
    }

    public Indices getIndiceLovSeleccionado() {
        return indiceLovSeleccionado;
    }

    public void setIndiceLovSeleccionado(Indices indiceLovSeleccionado) {
        this.indiceLovSeleccionado = indiceLovSeleccionado;
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

    public String getAltoTabla() {
        return altoTabla;
    }

    public void setAltoTabla(String altoTabla) {
        this.altoTabla = altoTabla;
    }

    public String getAltoTablaIndices() {
        return altoTablaIndices;
    }

    public void setAltoTablaIndices(String altoTablaIndices) {
        this.altoTablaIndices = altoTablaIndices;
    }

    public boolean isActivarLov() {
        return activarLov;
    }

    public void setActivarLov(boolean activarLov) {
        this.activarLov = activarLov;
    }

    public boolean isMostrarTodos() {
        return mostrarTodos;
    }

    public void setMostrarTodos(boolean mostrarTodos) {
        this.mostrarTodos = mostrarTodos;
    }

    public boolean isBuscar() {
        return buscar;
    }

    public void setBuscar(boolean buscar) {
        this.buscar = buscar;
    }

    public String getInfoRegistroUsuario() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosUsuarios");
        infoRegistroUsuario = String.valueOf(tabla.getRowCount());
        return infoRegistroUsuario;
    }

    public void setInfoRegistroUsuario(String infoRegistroUsuario) {
        this.infoRegistroUsuario = infoRegistroUsuario;
    }

    public String getInfoRegistroUsuarioIndices() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosUsuariosIndices");
        infoRegistroUsuarioIndices = String.valueOf(tabla.getRowCount());
        return infoRegistroUsuarioIndices;
    }

    public void setInfoRegistroUsuarioIndices(String infoRegistroUsuarioIndices) {
        this.infoRegistroUsuarioIndices = infoRegistroUsuarioIndices;
    }

    public String getInfoRegistroLovIndices() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovIndices");
        infoRegistroLovIndices = String.valueOf(tabla.getRowCount());
        return infoRegistroLovIndices;
    }

    public void setInfoRegistroLovIndices(String infoRegistroLovIndices) {
        this.infoRegistroLovIndices = infoRegistroLovIndices;
    }

    public String getInfoRegistroLovUsuariosIndices() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovUsuariosIndices");
        infoRegistroLovUsuariosIndices = String.valueOf(tabla.getRowCount());
        return infoRegistroLovUsuariosIndices;
    }

    public void setInfoRegistroLovUsuariosIndices(String infoRegistroLovUsuariosIndices) {
        this.infoRegistroLovUsuariosIndices = infoRegistroLovUsuariosIndices;
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

    public String getMsgError() {
        return msgError;
    }

    public void setMsgError(String msgError) {
        this.msgError = msgError;
    }

}
