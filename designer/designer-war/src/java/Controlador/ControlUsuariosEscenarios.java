/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import ControlNavegacion.ControlListaNavegacion;
import Entidades.Escenarios;
import Entidades.Usuarios;
import Entidades.UsuariosEscenarios;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarRastrosInterface;
import InterfaceAdministrar.AdministrarUsuariosEscenariosInterface;
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
@Named(value = "controlUsuariosEscenarios")
@SessionScoped
public class ControlUsuariosEscenarios implements Serializable {

    private static Logger log = Logger.getLogger(ControlUsuariosEscenarios.class);

    @EJB
    AdministrarUsuariosEscenariosInterface administrarUsuariosEscenarios;
    @EJB
    AdministrarRastrosInterface administrarRastros;

    private List<UsuariosEscenarios> listUsuariosE;
    private List<UsuariosEscenarios> listUsuariosEFiltrar;
    private List<UsuariosEscenarios> listUsuariosECrear;
    private List<UsuariosEscenarios> listUsuariosEModificar;
    private List<UsuariosEscenarios> listUsuariosEBorrar;
    private UsuariosEscenarios usuarioEscenarioSeleccionado;
    private UsuariosEscenarios nuevoUsuarioE;
    private UsuariosEscenarios duplicarUsuarioE;
    private UsuariosEscenarios editarUsuarioE;
    //lov usuarios
    private List<Usuarios> lovUsuarios;
    private List<Usuarios> lovUsuariosFiltrar;
    private Usuarios usuarioSeleccionado;
    //lov tipossueldos
    private List<Escenarios> lovEscenarios;
    private List<Escenarios> lovEscenariosFiltrar;
    private Escenarios escenarioSeleccionado;
//otros
    private String mensajeValidacion;
    private String msgError;
    private String altoTabla;
    private String infoRegistroUsuariosE, infoRegistroUsuario, infoRegistroEscenario;
    private int cualCelda, tipoLista, tipoActualizacion, k, bandera;
    private BigInteger l;
    private boolean aceptar, guardado, activarLov;
    private Column columnausuario, columnats;
    private String paginaAnterior = "nominaf";
    private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

    public ControlUsuariosEscenarios() {

        listUsuariosEBorrar = new ArrayList<UsuariosEscenarios>();
        listUsuariosECrear = new ArrayList<UsuariosEscenarios>();
        listUsuariosEModificar = new ArrayList<UsuariosEscenarios>();
        nuevoUsuarioE = new UsuariosEscenarios();
        nuevoUsuarioE.setUsuario(new Usuarios());
        nuevoUsuarioE.setEscenario(new Escenarios());
        duplicarUsuarioE = new UsuariosEscenarios();
        editarUsuarioE = new UsuariosEscenarios();
        guardado = true;
        aceptar = true;
        activarLov = true;
        listUsuariosE = null;
        altoTabla = "315";
        mapParametros.put("paginaAnterior", paginaAnterior);
    }

    @PreDestroy
    public void destruyendoce() {
        log.info(this.getClass().getName() + ".destruyendoce() @Destroy");
    }

    @PostConstruct
    public void inicializarAdministrador() {
        log.info(this.getClass().getName() + ".inicializarAdministrador() @PostConstruct");
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarUsuariosEscenarios.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
            listUsuariosE = null;
            getListUsuariosE();
            if (listUsuariosE != null) {
                if (!listUsuariosE.isEmpty()) {
                    usuarioEscenarioSeleccionado = listUsuariosE.get(0);
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
        //inicializarCosas(); Inicializar cosas de ser necesario
    }

    public void navegar(String pag) {
        FacesContext fc = FacesContext.getCurrentInstance();
        ControlListaNavegacion controlListaNavegacion = (ControlListaNavegacion) fc.getApplication().evaluateExpressionGet(fc, "#{controlListaNavegacion}", ControlListaNavegacion.class);
        String pagActual = "usuarioescenario";
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

    public void limpiarListasValor() {
        lovEscenarios = null;
        lovUsuarios = null;
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            columnausuario = (Column) c.getViewRoot().findComponent("form:datosUsuarios:columnausuario");
            columnausuario.setFilterStyle("width: 85% !important");
            columnats = (Column) c.getViewRoot().findComponent("form:datosUsuarios:columnats");
            columnats.setFilterStyle("width: 85% !important");
            altoTabla = "295";
            RequestContext.getCurrentInstance().update("form:datosUsuarios");
            bandera = 1;
            tipoLista = 1;
        } else if (bandera == 1) {
            columnausuario = (Column) c.getViewRoot().findComponent("form:datosUsuarios:columnausuario");
            columnausuario.setFilterStyle("display: none; visibility: hidden;");
            columnats = (Column) c.getViewRoot().findComponent("form:datosUsuarios:columnats");
            columnats.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosUsuarios");
            altoTabla = "315";
            bandera = 0;
            listUsuariosEFiltrar = null;
            tipoLista = 0;
        }
    }

    public void eventofiltrar() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
        contarRegistros();
    }

    public void cambiarIndice(UsuariosEscenarios usuarioC, int celda) {
        usuarioEscenarioSeleccionado = usuarioC;
        cualCelda = celda;
        usuarioEscenarioSeleccionado.getSecuencia();
        if (cualCelda == 0) {
            habilitarBotonLov();
            usuarioEscenarioSeleccionado.getUsuario().getAlias();
        } else if (cualCelda == 1) {
            habilitarBotonLov();
            usuarioEscenarioSeleccionado.getEscenario().getDescripcion();
        }
    }

    public void editarCelda() {
        if (usuarioEscenarioSeleccionado != null) {
            editarUsuarioE = usuarioEscenarioSeleccionado;
            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarUsuario");
                RequestContext.getCurrentInstance().execute("PF('editarUsuario').show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarEscenario");
                RequestContext.getCurrentInstance().execute("PF('editarEscenario').show()");
                cualCelda = -1;
            }
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void asignarIndex(UsuariosEscenarios usuariots, int dlg, int LND) {
        usuarioEscenarioSeleccionado = usuariots;
        tipoActualizacion = LND;
        if (dlg == 0) {
            lovUsuarios = null;
            getLovUsuarios();
            contarRegistrosUsuarios();
            RequestContext.getCurrentInstance().update("formularioDialogos:usuariosDialogo");
            RequestContext.getCurrentInstance().execute("PF('usuariosDialogo').show()");
        } else if (dlg == 1) {
            lovEscenarios = null;
            getLovEscenarios();
            contarRegistrosTS();
            RequestContext.getCurrentInstance().update("formularioDialogos:escenariosDialogo");
            RequestContext.getCurrentInstance().execute("PF('escenariosDialogo').show()");
        }
    }

    public void listaValoresBoton() {
        if (usuarioEscenarioSeleccionado != null) {
            RequestContext context = RequestContext.getCurrentInstance();
            if (cualCelda == 0) {
                lovUsuarios = null;
                getLovUsuarios();
                contarRegistrosUsuarios();
                RequestContext.getCurrentInstance().update("formularioDialogos:usuariosDialogo");
                RequestContext.getCurrentInstance().execute("PF('usuariosDialogo').show()");
                tipoActualizacion = 0;
            } else if (cualCelda == 1) {
                lovEscenarios = null;
                getLovEscenarios();
                contarRegistrosTS();
                RequestContext.getCurrentInstance().update("formularioDialogos:escenariosDialogo");
                RequestContext.getCurrentInstance().execute("PF('escenariosDialogo').show()");
                tipoActualizacion = 0;
            }
        }
    }

    public void modificarUsuariosTS(UsuariosEscenarios usuariosts) {
        usuarioEscenarioSeleccionado = usuariosts;
        if (!listUsuariosECrear.contains(usuarioEscenarioSeleccionado)) {
            if (listUsuariosEModificar.isEmpty()) {
                listUsuariosEModificar.add(usuarioEscenarioSeleccionado);
            } else if (!listUsuariosEModificar.contains(usuarioEscenarioSeleccionado)) {
                listUsuariosEModificar.add(usuarioEscenarioSeleccionado);
            }
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
        RequestContext.getCurrentInstance().update("form:datosUsuarios");
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosUsuariosExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "UsuariosEscenariosPDF", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosUsuariosExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "UsuariosEscenariosXLS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void limpiarNuevoUsuarioEscenario() {
        nuevoUsuarioE = new UsuariosEscenarios();
    }

    public void limpiarDuplicarUsuarioEscenario() {
        duplicarUsuarioE = new UsuariosEscenarios();

    }

    public void guardarYSalir() {
        guardarCambiosUsuario();
        salir();
    }

    public void guardarCambiosUsuario() {
        try {
            if (guardado == false) {
                msgError = "";
                if (!listUsuariosEBorrar.isEmpty()) {
                    for (int i = 0; i < listUsuariosEBorrar.size(); i++) {
                        msgError = administrarUsuariosEscenarios.borrarUsuarioC(listUsuariosEBorrar.get(i));
                    }
                    listUsuariosEBorrar.clear();
                }
                if (!listUsuariosECrear.isEmpty()) {
                    for (int i = 0; i < listUsuariosECrear.size(); i++) {
                        msgError = administrarUsuariosEscenarios.crearUsuarioC(listUsuariosECrear.get(i));
                    }
                    listUsuariosECrear.clear();
                }
                if (!listUsuariosEModificar.isEmpty()) {
                    for (int i = 0; i < listUsuariosEModificar.size(); i++) {
                        msgError = administrarUsuariosEscenarios.modificarUsuarioC(listUsuariosEModificar.get(i));
                    }
                    listUsuariosEModificar.clear();
                }
                if (msgError.equals("EXITO")) {
                    listUsuariosE = null;
                    getListUsuariosE();
                    contarRegistros();
                    RequestContext.getCurrentInstance().update("form:datosUsuarios");
                    guardado = true;
                    FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                    RequestContext.getCurrentInstance().update("form:growl");
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                    k = 0;
                    usuarioEscenarioSeleccionado = null;
                } else {
                    RequestContext.getCurrentInstance().update("formularioDialogos:errorGuardadoBD");
                    RequestContext.getCurrentInstance().execute("PF('errorGuardadoBD').show()");
                }
            }
        } catch (Exception e) {
            FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado, intente nuevamente.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }
    }

    public void borrarUsuarioTS() {
        if (usuarioEscenarioSeleccionado != null) {
            if (!listUsuariosEModificar.isEmpty() && listUsuariosEModificar.contains(usuarioEscenarioSeleccionado)) {
                int modIndex = listUsuariosEModificar.indexOf(usuarioEscenarioSeleccionado);
                listUsuariosEModificar.remove(modIndex);
                listUsuariosEBorrar.add(usuarioEscenarioSeleccionado);
            } else if (!listUsuariosECrear.isEmpty() && listUsuariosECrear.contains(usuarioEscenarioSeleccionado)) {
                int crearIndex = listUsuariosECrear.indexOf(usuarioEscenarioSeleccionado);
                listUsuariosECrear.remove(crearIndex);
            } else {
                listUsuariosEBorrar.add(usuarioEscenarioSeleccionado);
            }
            listUsuariosE.remove(usuarioEscenarioSeleccionado);

            if (tipoLista == 1) {
                listUsuariosEFiltrar.remove(usuarioEscenarioSeleccionado);
            }
            usuarioEscenarioSeleccionado = null;
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:datosUsuarios");
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void actualizarEscenarios() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            usuarioEscenarioSeleccionado.setEscenario(escenarioSeleccionado);
            if (!listUsuariosECrear.contains(usuarioEscenarioSeleccionado)) {
                if (listUsuariosEModificar.isEmpty()) {
                    listUsuariosEModificar.add(usuarioEscenarioSeleccionado);
                } else if (!listUsuariosEModificar.contains(usuarioEscenarioSeleccionado)) {
                    listUsuariosEModificar.add(usuarioEscenarioSeleccionado);
                }
            }
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosUsuarios");
        } else if (tipoActualizacion == 1) {
            nuevoUsuarioE.setEscenario(escenarioSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaUsuario");
        } else if (tipoActualizacion == 2) {
            duplicarUsuarioE.setEscenario(escenarioSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarUsuario");
        }
        lovEscenariosFiltrar = null;
        escenarioSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;

        RequestContext.getCurrentInstance().update("formularioDialogos:escenariosDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovTS");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarTS");
        context.reset("formularioDialogos:lovTS:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovTS').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('escenariosDialogo').hide()");
    }

    public void cancelarCambioEscenarios() {
        lovEscenariosFiltrar = null;
        escenarioSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("formularioDialogos:escenariosDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovTS");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarTS");
        context.reset("formularioDialogos:lovTS:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovTS').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('escenariosDialogo').hide()");
    }

    public void actualizarUsuarios() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            usuarioEscenarioSeleccionado.setUsuario(usuarioSeleccionado);
            if (!listUsuariosECrear.contains(usuarioEscenarioSeleccionado)) {
                if (listUsuariosEModificar.isEmpty()) {
                    listUsuariosEModificar.add(usuarioEscenarioSeleccionado);
                } else if (!listUsuariosEModificar.contains(usuarioEscenarioSeleccionado)) {
                    listUsuariosEModificar.add(usuarioEscenarioSeleccionado);
                }
            }
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosUsuarios");
        } else if (tipoActualizacion == 1) {
            nuevoUsuarioE.setUsuario(usuarioSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaUsuario");
        } else if (tipoActualizacion == 2) {
            duplicarUsuarioE.setUsuario(usuarioSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarUsuario");
        }
        lovUsuariosFiltrar = null;
        usuarioSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;

        RequestContext.getCurrentInstance().update("formularioDialogos:usuariosDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovUsuarios");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarU");
        context.reset("formularioDialogos:lovUsuarios:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovUsuarios').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('usuariosDialogo').hide()");
    }

    public void cancelarCambioUsuario() {
        lovUsuariosFiltrar = null;
        usuarioSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("formularioDialogos:usuariosDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovUsuarios");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarU");
        context.reset("formularioDialogos:lovUsuarios:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovUsuarios').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('usuariosDialogo').hide()");
    }

    public void agregarNuevaUsuario() {
        RequestContext context = RequestContext.getCurrentInstance();
        int pasa = 0;
        int duplicados = 0;
        mensajeValidacion = " ";
        if (nuevoUsuarioE.getUsuario() == null) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            pasa++;
        }
        if (nuevoUsuarioE.getEscenario()== null) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            pasa++;
        }

        for (int i = 0; i < listUsuariosE.size(); i++) {
            if (nuevoUsuarioE.getUsuario().equals(listUsuariosE.get(i).getUsuario()) && nuevoUsuarioE.getEscenario().equals(listUsuariosE.get(i).getEscenario())) {
                duplicados++;
            }
        }

        if (pasa == 0) {
            if (duplicados == 0) {
                if (bandera == 1) {
                    FacesContext c = FacesContext.getCurrentInstance();
                    columnausuario = (Column) c.getViewRoot().findComponent("form:datosUsuarios:columnausuario");
                    columnausuario.setFilterStyle("display: none; visibility: hidden;");
                    columnats = (Column) c.getViewRoot().findComponent("form:datosUsuarios:columnats");
                    columnats.setFilterStyle("display: none; visibility: hidden;");
                    altoTabla = "315";
                    RequestContext.getCurrentInstance().update("form:datosUsuarios");
                    bandera = 0;
                    listUsuariosEFiltrar = null;
                    tipoLista = 0;
                }
                k++;
                l = BigInteger.valueOf(k);
                nuevoUsuarioE.setSecuencia(l);
                listUsuariosECrear.add(nuevoUsuarioE);
                listUsuariosE.add(nuevoUsuarioE);
                usuarioEscenarioSeleccionado = nuevoUsuarioE;
                contarRegistros();
                nuevoUsuarioE = new UsuariosEscenarios();
                RequestContext.getCurrentInstance().update("form:datosUsuarios");
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
                RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroUsuario");
                RequestContext.getCurrentInstance().execute("PF('NuevoRegistroUsuario').hide()");
            } else {
                RequestContext.getCurrentInstance().update("formularioDialogos:existeRegistro");
                RequestContext.getCurrentInstance().execute("PF('existeRegistro').show()");
            }
        } else {
            RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevaUsuario");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevaUsuario').show()");
        }
    }

    public void duplicarUsuario() {
        if (usuarioEscenarioSeleccionado != null) {
            duplicarUsuarioE = new UsuariosEscenarios();
            duplicarUsuarioE.setUsuario(usuarioEscenarioSeleccionado.getUsuario());
            duplicarUsuarioE.setEscenario(usuarioEscenarioSeleccionado.getEscenario());
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarUsuario");
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroUsuario').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void confirmarDuplicar() {
        int pasa = 0;
        int duplicados = 0;
        k++;
        l = BigInteger.valueOf(k);
        duplicarUsuarioE.setSecuencia(l);
        if (duplicarUsuarioE.getUsuario() == null) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            pasa++;
        }
        if (duplicarUsuarioE.getEscenario() == null) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            pasa++;
        }

        for (int i = 0; i < listUsuariosE.size(); i++) {
            if (duplicarUsuarioE.getUsuario().equals(listUsuariosE.get(i).getUsuario()) && duplicarUsuarioE.getEscenario().equals(listUsuariosE.get(i).getEscenario())) {
                duplicados++;
            }
        }

        if (pasa == 0) {
            if (duplicados == 0) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
                if (bandera == 1) {
                    FacesContext c = FacesContext.getCurrentInstance();
                    columnausuario = (Column) c.getViewRoot().findComponent("form:datosUsuarios:columnausuario");
                    columnausuario.setFilterStyle("display: none; visibility: hidden;");
                    columnats = (Column) c.getViewRoot().findComponent("form:datosUsuarios:columnats");
                    columnats.setFilterStyle("display: none; visibility: hidden;");
                    RequestContext.getCurrentInstance().update("form:datosUsuarios");
                    altoTabla = "315";
                    bandera = 0;
                    listUsuariosEFiltrar = null;
                    tipoLista = 0;
                }
                listUsuariosE.add(duplicarUsuarioE);
                listUsuariosECrear.add(duplicarUsuarioE);
                usuarioEscenarioSeleccionado = duplicarUsuarioE;
                contarRegistros();
                RequestContext.getCurrentInstance().update("form:datosUsuarios");
                duplicarUsuarioE = new UsuariosEscenarios();
                RequestContext.getCurrentInstance().update("formularioDialogos:duplicarUsuario");
                RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroUsuario').hide()");
            } else {
                RequestContext.getCurrentInstance().update("formularioDialogos:existeRegistro");
                RequestContext.getCurrentInstance().execute("PF('existeRegistro').show()");
            }
        } else {
            RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevaUsuario");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevaUsuario').show()");
        }
    }

    public void revisarDialogoGuardar() {
        if (!listUsuariosEBorrar.isEmpty() || !listUsuariosECrear.isEmpty() || !listUsuariosEModificar.isEmpty()) {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:confirmarGuardar");
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
        }
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (usuarioEscenarioSeleccionado != null) {
            int resultado = administrarRastros.obtenerTabla(usuarioEscenarioSeleccionado.getSecuencia(), "USUARIOSCONTRATOS");
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
        } else if (administrarRastros.verificarHistoricosTabla("USUARIOSCONTRATOS")) {
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
    }

    public void cancelarModificacion() {
        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            columnausuario = (Column) c.getViewRoot().findComponent("form:datosUsuarios:columnausuario");
            columnausuario.setFilterStyle("display: none; visibility: hidden;");
            columnats = (Column) c.getViewRoot().findComponent("form:datosUsuarios:columnats");
            columnats.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosUsuarios");
            altoTabla = "315";
            bandera = 0;
            listUsuariosEFiltrar = null;
            tipoLista = 0;
        }
        listUsuariosEBorrar.clear();
        listUsuariosECrear.clear();
        listUsuariosEModificar.clear();
        usuarioEscenarioSeleccionado = null;
        k = 0;
        listUsuariosE = null;
        getListUsuariosE();
        contarRegistros();
        guardado = true;
        deshabilitarBotonLov();
        RequestContext.getCurrentInstance().update("form:datosUsuarios");
    }

    public void salir() {
        limpiarListasValor();
        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            columnausuario = (Column) c.getViewRoot().findComponent("form:datosUsuarios:columnausuario");
            columnausuario.setFilterStyle("display: none; visibility: hidden;");
            columnats = (Column) c.getViewRoot().findComponent("form:datosUsuarios:columnats");
            columnats.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosUsuarios");
            altoTabla = "315";
            bandera = 0;
            listUsuariosEFiltrar = null;
            tipoLista = 0;
        }
        listUsuariosEBorrar.clear();
        listUsuariosECrear.clear();
        listUsuariosEModificar.clear();
        usuarioEscenarioSeleccionado = null;
        k = 0;
        listUsuariosE = null;
        guardado = true;
        RequestContext.getCurrentInstance().update("form:datosUsuarios");
        navegar("atras");
    }

    public void contarRegistros() {
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
    }

    public void contarRegistrosUsuarios() {
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroUsuarios");
    }

    public void contarRegistrosTS() {
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroC");
    }

    public void habilitarBotonLov() {
        activarLov = false;
        RequestContext.getCurrentInstance().update("form:listaValores");
    }

    public void deshabilitarBotonLov() {
        activarLov = true;
        RequestContext.getCurrentInstance().update("form:listaValores");
    }

    //////////////sets y gets/////////
    public List<UsuariosEscenarios> getListUsuariosE() {
        if (listUsuariosE == null) {
            listUsuariosE = administrarUsuariosEscenarios.consultarUsuariosEscenarios();
        }
        return listUsuariosE;
    }

    public void setListUsuariosE(List<UsuariosEscenarios> listUsuariosE) {
        this.listUsuariosE = listUsuariosE;
    }

    public List<UsuariosEscenarios> getListUsuariosEFiltrar() {
        return listUsuariosEFiltrar;
    }

    public void setListUsuariosEFiltrar(List<UsuariosEscenarios> listUsuariosEFiltrar) {
        this.listUsuariosEFiltrar = listUsuariosEFiltrar;
    }

    public UsuariosEscenarios getUsuarioEscenarioSeleccionado() {
        return usuarioEscenarioSeleccionado;
    }

    public void setUsuarioEscenarioSeleccionado(UsuariosEscenarios usuarioEscenarioSeleccionado) {
        this.usuarioEscenarioSeleccionado = usuarioEscenarioSeleccionado;
    }

    public UsuariosEscenarios getNuevoUsuarioE() {
        return nuevoUsuarioE;
    }

    public void setNuevoUsuarioE(UsuariosEscenarios nuevoUsuarioE) {
        this.nuevoUsuarioE = nuevoUsuarioE;
    }

    public UsuariosEscenarios getDuplicarUsuarioE() {
        return duplicarUsuarioE;
    }

    public void setDuplicarUsuarioE(UsuariosEscenarios duplicarUsuarioE) {
        this.duplicarUsuarioE = duplicarUsuarioE;
    }

    public UsuariosEscenarios getEditarUsuarioE() {
        return editarUsuarioE;
    }

    public void setEditarUsuarioE(UsuariosEscenarios editarUsuarioE) {
        this.editarUsuarioE = editarUsuarioE;
    }

    public List<Usuarios> getLovUsuarios() {
        if (lovUsuarios == null) {
            lovUsuarios = administrarUsuariosEscenarios.listaUsuarios();
        }
        return lovUsuarios;
    }

    public void setLovUsuarios(List<Usuarios> lovUsuarios) {
        this.lovUsuarios = lovUsuarios;
    }

    public List<Usuarios> getLovUsuariosFiltrar() {
        return lovUsuariosFiltrar;
    }

    public void setLovUsuariosFiltrar(List<Usuarios> lovUsuariosFiltrar) {
        this.lovUsuariosFiltrar = lovUsuariosFiltrar;
    }

    public Usuarios getUsuarioSeleccionado() {
        return usuarioSeleccionado;
    }

    public void setUsuarioSeleccionado(Usuarios usuarioSeleccionado) {
        this.usuarioSeleccionado = usuarioSeleccionado;
    }

    public List<Escenarios> getLovEscenarios() {
        if (lovEscenarios == null) {
            lovEscenarios = administrarUsuariosEscenarios.lovEscenarios();
        }
        return lovEscenarios;
    }

    public void setLovEscenarios(List<Escenarios> lovEscenarios) {
        this.lovEscenarios = lovEscenarios;
    }

    public List<Escenarios> getLovEscenariosFiltrar() {
        return lovEscenariosFiltrar;
    }

    public void setLovEscenariosFiltrar(List<Escenarios> lovEscenariosFiltrar) {
        this.lovEscenariosFiltrar = lovEscenariosFiltrar;
    }

    public Escenarios getEscenarioSeleccionado() {
        return escenarioSeleccionado;
    }

    public void setEscenarioSeleccionado(Escenarios escenarioSeleccionado) {
        this.escenarioSeleccionado = escenarioSeleccionado;
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

    public String getAltoTabla() {
        return altoTabla;
    }

    public void setAltoTabla(String altoTabla) {
        this.altoTabla = altoTabla;
    }

    public String getInfoRegistroUsuariosE() {
         FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosUsuarios");
        infoRegistroUsuariosE = String.valueOf(tabla.getRowCount());
        return infoRegistroUsuariosE;
    }

    public void setInfoRegistroUsuariosE(String infoRegistroUsuariosE) {
        this.infoRegistroUsuariosE = infoRegistroUsuariosE;
    }

    public String getInfoRegistroUsuario() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovUsuarios");
        infoRegistroUsuario = String.valueOf(tabla.getRowCount());
        return infoRegistroUsuario;
    }

    public void setInfoRegistroUsuario(String infoRegistroUsuario) {
        this.infoRegistroUsuario = infoRegistroUsuario;
    }

    public String getInfoRegistroEscenario() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovTS");
        infoRegistroEscenario = String.valueOf(tabla.getRowCount());
        return infoRegistroEscenario;
    }

    public void setInfoRegistroEscenario(String infoRegistroEscenario) {
        this.infoRegistroEscenario = infoRegistroEscenario;
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

    public boolean isActivarLov() {
        return activarLov;
    }

    public void setActivarLov(boolean activarLov) {
        this.activarLov = activarLov;
    }

}
