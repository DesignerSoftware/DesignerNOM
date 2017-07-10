/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import ControlNavegacion.ControlListaNavegacion;
import Entidades.Contratos;
import Entidades.Usuarios;
import Entidades.UsuariosContratos;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarContratosInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import InterfaceAdministrar.AdministrarUsuariosContratosInterface;
import InterfaceAdministrar.AdministrarUsuariosInterface;
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
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.primefaces.component.column.Column;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.export.Exporter;
import org.primefaces.context.RequestContext;

/**
 *
 * @author user
 */
@Named(value = "controlUsuariosContratos")
@SessionScoped
public class ControlUsuariosContratos implements Serializable {

    @EJB
    AdministrarUsuariosContratosInterface administrarUsuariosContratos;
    @EJB
    AdministrarRastrosInterface administrarRastros;
    @EJB
    AdministrarUsuariosInterface administrarUsuarios;
    @EJB
    AdministrarContratosInterface administrarContratos;

    private List<UsuariosContratos> listUsuariosC;
    private List<UsuariosContratos> listUsuariosCFiltrar;
    private List<UsuariosContratos> listUsuariosCCrear;
    private List<UsuariosContratos> listUsuariosCModificar;
    private List<UsuariosContratos> listUsuariosCBorrar;
    private UsuariosContratos usuarioContratoSeleccionado;
    private UsuariosContratos nuevoUsuarioC;
    private UsuariosContratos duplicarUsuarioC;
    private UsuariosContratos editarUsuarioC;
    //lov usuarios
    private List<Usuarios> lovUsuarios;
    private List<Usuarios> lovUsuariosFiltrar;
    private Usuarios usuarioSeleccionado;
    //lov tipossueldos
    private List<Contratos> lovContratos;
    private List<Contratos> lovContratosFiltrar;
    private Contratos contratoSeleccionado;
//otros
    private String mensajeValidacion;
    private String altoTabla;
    private String infoRegistroUsuariosC, infoRegistroUsuario, infoRegistroContrato;
    private int cualCelda, tipoLista, tipoActualizacion, k, bandera;
    private BigInteger l;
    private boolean aceptar, guardado, activarLov;
    private Column columnausuario, columnats;
    private String paginaAnterior = "nominaf";
    private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

    public ControlUsuariosContratos() {
        listUsuariosCBorrar = new ArrayList<UsuariosContratos>();
        listUsuariosCCrear = new ArrayList<UsuariosContratos>();
        listUsuariosCModificar = new ArrayList<UsuariosContratos>();
        nuevoUsuarioC = new UsuariosContratos();
        nuevoUsuarioC.setUsuario(new Usuarios());
        nuevoUsuarioC.setContrato(new Contratos());
        duplicarUsuarioC = new UsuariosContratos();
        editarUsuarioC = new UsuariosContratos();
        guardado = true;
        aceptar = true;
        activarLov = true;
        listUsuariosC = null;
        altoTabla = "315";
        mapParametros.put("paginaAnterior", paginaAnterior);
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarContratos.obtenerConexion(ses.getId());
            administrarUsuarios.obtenerConexion(ses.getId());
            administrarUsuariosContratos.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
            listUsuariosC = null;
            getListUsuariosC();
            if (listUsuariosC != null) {
                if (!listUsuariosC.isEmpty()) {
                    usuarioContratoSeleccionado = listUsuariosC.get(0);
                }
            }
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
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
        String pagActual = "usuariocontrato";
        if (pag.equals("atras")) {
            pag = paginaAnterior;
            paginaAnterior = "nominaf";
            controlListaNavegacion.quitarPagina(pagActual);
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
        lovContratos = null;
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
            listUsuariosCFiltrar = null;
            tipoLista = 0;
        }
    }

    public void eventofiltrar() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
        contarRegistros();
    }

    public void cambiarIndice(UsuariosContratos usuarioC, int celda) {
        usuarioContratoSeleccionado = usuarioC;
        cualCelda = celda;
        usuarioContratoSeleccionado.getSecuencia();
        if (cualCelda == 0) {
            habilitarBotonLov();
            usuarioContratoSeleccionado.getUsuario().getAlias();
        } else if (cualCelda == 1) {
            habilitarBotonLov();
            usuarioContratoSeleccionado.getContrato().getDescripcion();
        }
    }

    public void editarCelda() {
        if (usuarioContratoSeleccionado != null) {
            editarUsuarioC = usuarioContratoSeleccionado;
            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarUsuario");
                RequestContext.getCurrentInstance().execute("PF('editarUsuario').show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarContrato");
                RequestContext.getCurrentInstance().execute("PF('editarContrato').show()");
                cualCelda = -1;
            }
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void asignarIndex(UsuariosContratos usuariots, int dlg, int LND) {
        usuarioContratoSeleccionado = usuariots;
        tipoActualizacion = LND;
        if (dlg == 0) {
            lovUsuarios = null;
            getLovUsuarios();
            contarRegistrosUsuarios();
            RequestContext.getCurrentInstance().update("formularioDialogos:usuariosDialogo");
            RequestContext.getCurrentInstance().execute("PF('usuariosDialogo').show()");
        } else if (dlg == 1) {
            lovContratos = null;
            getLovContratos();
            contarRegistrosTS();
            RequestContext.getCurrentInstance().update("formularioDialogos:contratosDialogo");
            RequestContext.getCurrentInstance().execute("PF('contratosDialogo').show()");
        }
    }

    public void listaValoresBoton() {
        if (usuarioContratoSeleccionado != null) {
            RequestContext context = RequestContext.getCurrentInstance();
            if (cualCelda == 0) {
                lovUsuarios = null;
                getLovUsuarios();
                contarRegistrosUsuarios();
                RequestContext.getCurrentInstance().update("formularioDialogos:usuariosDialogo");
                RequestContext.getCurrentInstance().execute("PF('usuariosDialogo').show()");
                tipoActualizacion = 0;
            } else if (cualCelda == 1) {
                lovContratos = null;
                getLovContratos();
                contarRegistrosTS();
                RequestContext.getCurrentInstance().update("formularioDialogos:contratosDialogo");
                RequestContext.getCurrentInstance().execute("PF('contratosDialogo').show()");
                tipoActualizacion = 0;
            }
        }
    }

    public void modificarUsuariosTS(UsuariosContratos usuariosts) {
        usuarioContratoSeleccionado = usuariosts;
        if (!listUsuariosCCrear.contains(usuarioContratoSeleccionado)) {
            if (listUsuariosCModificar.isEmpty()) {
                listUsuariosCModificar.add(usuarioContratoSeleccionado);
            } else if (!listUsuariosCModificar.contains(usuarioContratoSeleccionado)) {
                listUsuariosCModificar.add(usuarioContratoSeleccionado);
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
        exporter.export(context, tabla, "UsuariosContratosPDF", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosUsuariosExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "UsuariosContratosXLS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void limpiarNuevoUsuarioContratos() {
        nuevoUsuarioC = new UsuariosContratos();
    }

    public void limpiarDuplicarUsuarioContratos() {
        duplicarUsuarioC = new UsuariosContratos();

    }

    public void guardarYSalir() {
        guardarCambiosUsuario();
        salir();
    }

    public void guardarCambiosUsuario() {
        try {
            if (guardado == false) {
                if (!listUsuariosCBorrar.isEmpty()) {
                    administrarUsuariosContratos.borrarUsuarioC(listUsuariosCBorrar);
                    listUsuariosCBorrar.clear();
                }
                if (!listUsuariosCCrear.isEmpty()) {
                    administrarUsuariosContratos.crearUsuarioC(listUsuariosCCrear);
                    listUsuariosCCrear.clear();
                }
                if (!listUsuariosCModificar.isEmpty()) {
                    administrarUsuariosContratos.modificarUsuarioC(listUsuariosCModificar);
                    listUsuariosCModificar.clear();
                }
                listUsuariosC = null;
                getListUsuariosC();
                contarRegistros();
                RequestContext.getCurrentInstance().update("form:datosUsuarios");
                guardado = true;
                FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                RequestContext.getCurrentInstance().update("form:growl");
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
                k = 0;
                usuarioContratoSeleccionado = null;
            }
        } catch (Exception e) {
            FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado, intente nuevamente.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }
    }

    public void borrarUsuarioTS() {
        if (usuarioContratoSeleccionado != null) {
            if (!listUsuariosCModificar.isEmpty() && listUsuariosCModificar.contains(usuarioContratoSeleccionado)) {
                int modIndex = listUsuariosCModificar.indexOf(usuarioContratoSeleccionado);
                listUsuariosCModificar.remove(modIndex);
                listUsuariosCBorrar.add(usuarioContratoSeleccionado);
            } else if (!listUsuariosCCrear.isEmpty() && listUsuariosCCrear.contains(usuarioContratoSeleccionado)) {
                int crearIndex = listUsuariosCCrear.indexOf(usuarioContratoSeleccionado);
                listUsuariosCCrear.remove(crearIndex);
            } else {
                listUsuariosCBorrar.add(usuarioContratoSeleccionado);
            }
            listUsuariosC.remove(usuarioContratoSeleccionado);

            if (tipoLista == 1) {
                listUsuariosCFiltrar.remove(usuarioContratoSeleccionado);
            }
            usuarioContratoSeleccionado = null;
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:datosUsuarios");
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void actualizarContratos() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            usuarioContratoSeleccionado.setContrato(contratoSeleccionado);
            if (!listUsuariosCCrear.contains(usuarioContratoSeleccionado)) {
                if (listUsuariosCModificar.isEmpty()) {
                    listUsuariosCModificar.add(usuarioContratoSeleccionado);
                } else if (!listUsuariosCModificar.contains(usuarioContratoSeleccionado)) {
                    listUsuariosCModificar.add(usuarioContratoSeleccionado);
                }
            }
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosUsuarios");
        } else if (tipoActualizacion == 1) {
            nuevoUsuarioC.setContrato(contratoSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaUsuario");
        } else if (tipoActualizacion == 2) {
            duplicarUsuarioC.setContrato(contratoSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarUsuario");
        }
        lovContratosFiltrar = null;
        contratoSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;

        RequestContext.getCurrentInstance().update("formularioDialogos:contratosDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovTS");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarTS");
        context.reset("formularioDialogos:lovTS:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovTS').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('contratosDialogo').hide()");
    }

    public void cancelarCambioContratos() {
        lovContratosFiltrar = null;
        contratoSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("formularioDialogos:contratosDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovTS");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarTS");
        context.reset("formularioDialogos:lovTS:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovTS').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('contratosDialogo').hide()");
    }

    public void actualizarUsuarios() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            usuarioContratoSeleccionado.setUsuario(usuarioSeleccionado);
            if (!listUsuariosCCrear.contains(usuarioContratoSeleccionado)) {
                if (listUsuariosCModificar.isEmpty()) {
                    listUsuariosCModificar.add(usuarioContratoSeleccionado);
                } else if (!listUsuariosCModificar.contains(usuarioContratoSeleccionado)) {
                    listUsuariosCModificar.add(usuarioContratoSeleccionado);
                }
            }
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosUsuarios");
        } else if (tipoActualizacion == 1) {
            nuevoUsuarioC.setUsuario(usuarioSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaUsuario");
        } else if (tipoActualizacion == 2) {
            duplicarUsuarioC.setUsuario(usuarioSeleccionado);
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
        mensajeValidacion = " ";
        if (nuevoUsuarioC.getUsuario() == null) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            pasa++;
        }
        if (nuevoUsuarioC.getContrato() == null) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            pasa++;
        }
        if (pasa == 0) {
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                columnausuario = (Column) c.getViewRoot().findComponent("form:datosUsuarios:columnausuario");
                columnausuario.setFilterStyle("display: none; visibility: hidden;");
                columnats = (Column) c.getViewRoot().findComponent("form:datosUsuarios:columnats");
                columnats.setFilterStyle("display: none; visibility: hidden;");
                altoTabla = "315";
                RequestContext.getCurrentInstance().update("form:datosUsuarios");
                bandera = 0;
                listUsuariosCFiltrar = null;
                tipoLista = 0;
            }
            k++;
            l = BigInteger.valueOf(k);
            nuevoUsuarioC.setSecuencia(l);
            listUsuariosCCrear.add(nuevoUsuarioC);
            listUsuariosC.add(nuevoUsuarioC);
            usuarioContratoSeleccionado = nuevoUsuarioC;
            contarRegistros();
            nuevoUsuarioC = new UsuariosContratos();
            RequestContext.getCurrentInstance().update("form:datosUsuarios");
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroUsuario");
            RequestContext.getCurrentInstance().execute("PF('NuevoRegistroUsuario').hide()");
        } else {
            RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevaUsuario");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevaUsuario').show()");
        }
    }

    public void duplicarUsuario() {
        if (usuarioContratoSeleccionado != null) {
            duplicarUsuarioC = new UsuariosContratos();
            duplicarUsuarioC.setUsuario(usuarioContratoSeleccionado.getUsuario());
            duplicarUsuarioC.setContrato(usuarioContratoSeleccionado.getContrato());
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarUsuario");
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroUsuario').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void confirmarDuplicar() {
        int pasa = 0;
        k++;
        l = BigInteger.valueOf(k);
        duplicarUsuarioC.setSecuencia(l);
        if (duplicarUsuarioC.getUsuario() == null) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            pasa++;
        }
        if (duplicarUsuarioC.getContrato() == null) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            pasa++;
        }

        if (pasa == 0) {
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
                listUsuariosCFiltrar = null;
                tipoLista = 0;
            }
            listUsuariosC.add(duplicarUsuarioC);
            listUsuariosCCrear.add(duplicarUsuarioC);
            usuarioContratoSeleccionado = duplicarUsuarioC;
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:datosUsuarios");
            duplicarUsuarioC = new UsuariosContratos();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarUsuario");
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroUsuario').hide()");
        } else {
            RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevaUsuario");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevaUsuario').show()");
        }
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (usuarioContratoSeleccionado != null) {
            int resultado = administrarRastros.obtenerTabla(usuarioContratoSeleccionado.getSecuencia(), "USUARIOSCONTRATOS");
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
            listUsuariosCFiltrar = null;
            tipoLista = 0;
        }
        listUsuariosCBorrar.clear();
        listUsuariosCCrear.clear();
        listUsuariosCModificar.clear();
        usuarioContratoSeleccionado = null;
        k = 0;
        listUsuariosC = null;
        getListUsuariosC();
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
            listUsuariosCFiltrar = null;
            tipoLista = 0;
        }
        listUsuariosCBorrar.clear();
        listUsuariosCCrear.clear();
        listUsuariosCModificar.clear();
        usuarioContratoSeleccionado = null;
        k = 0;
        listUsuariosC = null;
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

    ///////GETS Y SETS //////
    public List<UsuariosContratos> getListUsuariosC() {
        if (listUsuariosC == null) {
            listUsuariosC = administrarUsuariosContratos.consultarUsuariosC();
        }
        return listUsuariosC;
    }

    public void setListUsuariosC(List<UsuariosContratos> listUsuariosC) {
        this.listUsuariosC = listUsuariosC;
    }

    public List<UsuariosContratos> getListUsuariosCFiltrar() {
        return listUsuariosCFiltrar;
    }

    public void setListUsuariosCFiltrar(List<UsuariosContratos> listUsuariosCFiltrar) {
        this.listUsuariosCFiltrar = listUsuariosCFiltrar;
    }

    public UsuariosContratos getUsuariocSeleccionado() {
        return usuarioContratoSeleccionado;
    }

    public void setUsuariocSeleccionado(UsuariosContratos usuarioContratoSeleccionado) {
        this.usuarioContratoSeleccionado = usuarioContratoSeleccionado;
    }

    public UsuariosContratos getNuevoUsuarioC() {
        return nuevoUsuarioC;
    }

    public void setNuevoUsuarioC(UsuariosContratos nuevoUsuarioC) {
        this.nuevoUsuarioC = nuevoUsuarioC;
    }

    public UsuariosContratos getDuplicarUsuarioC() {
        return duplicarUsuarioC;
    }

    public void setDuplicarUsuarioC(UsuariosContratos duplicarUsuarioC) {
        this.duplicarUsuarioC = duplicarUsuarioC;
    }

    public UsuariosContratos getEditarUsuarioC() {
        return editarUsuarioC;
    }

    public void setEditarUsuarioC(UsuariosContratos editarUsuarioC) {
        this.editarUsuarioC = editarUsuarioC;
    }

    public List<Usuarios> getLovUsuarios() {
        if (lovUsuarios == null) {
            lovUsuarios = administrarUsuarios.consultarUsuarios();
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

    public List<Contratos> getLovContratos() {
        if (lovContratos == null) {
            lovContratos = administrarContratos.consultarContratos();
        }
        return lovContratos;
    }

    public void setLovContratos(List<Contratos> lovContratos) {
        this.lovContratos = lovContratos;
    }

    public List<Contratos> getLovContratosFiltrar() {
        return lovContratosFiltrar;
    }

    public void setLovContratosFiltrar(List<Contratos> lovContratosFiltrar) {
        this.lovContratosFiltrar = lovContratosFiltrar;
    }

    public Contratos getContratoSeleccionado() {
        return contratoSeleccionado;
    }

    public void setContratoSeleccionado(Contratos contratoSeleccionado) {
        this.contratoSeleccionado = contratoSeleccionado;
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

    public String getInfoRegistroUsuariosC() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosUsuarios");
        infoRegistroUsuariosC = String.valueOf(tabla.getRowCount());
        return infoRegistroUsuariosC;
    }

    public void setInfoRegistroUsuariosC(String infoRegistroUsuariosC) {
        this.infoRegistroUsuariosC = infoRegistroUsuariosC;
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

    public String getInfoRegistroContrato() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovTS");
        infoRegistroContrato = String.valueOf(tabla.getRowCount());
        return infoRegistroContrato;
    }

    public void setInfoRegistroContrato(String infoRegistroContrato) {
        this.infoRegistroContrato = infoRegistroContrato;
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
