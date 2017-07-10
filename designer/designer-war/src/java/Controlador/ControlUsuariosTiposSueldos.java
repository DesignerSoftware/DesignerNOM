/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import ControlNavegacion.ControlListaNavegacion;
import Entidades.TiposSueldos;
import Entidades.Usuarios;
import Entidades.UsuariosTiposSueldos;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarRastrosInterface;
import InterfaceAdministrar.AdministrarTiposSueldosInterface;
import InterfaceAdministrar.AdministrarUsuariosInterface;
import InterfaceAdministrar.AdministrarUsuariosTiposSueldosInterface;
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
@Named(value = "controlUsuariosTiposSueldos")
@SessionScoped
public class ControlUsuariosTiposSueldos implements Serializable {

    @EJB
    AdministrarUsuariosTiposSueldosInterface administrarUsuariosTiposSueldos;
    @EJB
    AdministrarRastrosInterface administrarRastros;
    @EJB
    AdministrarUsuariosInterface administrarUsuarios;
    @EJB
    AdministrarTiposSueldosInterface administrarTiposSueldos;

    private List<UsuariosTiposSueldos> listUsuariosTS;
    private List<UsuariosTiposSueldos> listUsuariosTSFiltrar;
    private List<UsuariosTiposSueldos> listUsuariosTSCrear;
    private List<UsuariosTiposSueldos> listUsuariosTSModificar;
    private List<UsuariosTiposSueldos> listUsuariosTSBorrar;
    private UsuariosTiposSueldos usuariotsSeleccionado;
    private UsuariosTiposSueldos nuevoUsuarioTS;
    private UsuariosTiposSueldos duplicarUsuarioTS;
    private UsuariosTiposSueldos editarUsuarioTS;
    //lov usuarios
    private List<Usuarios> lovUsuarios;
    private List<Usuarios> lovUsuariosFiltrar;
    private Usuarios usuarioSeleccionado;
    //lov tipossueldos
    private List<TiposSueldos> lovTiposSueldos;
    private List<TiposSueldos> lovTiposSueldosFiltrar;
    private TiposSueldos tipoSueldoSeleccionado;
//otros
    private String mensajeValidacion;
    private String altoTabla;
    private String infoRegistroUsuariosTS, infoRegistroUsuario, infoRegistroTS;
    private int cualCelda, tipoLista, tipoActualizacion, k, bandera;
    private BigInteger l;
    private boolean aceptar, guardado, activarLov;
    private Column columnausuario, columnats;
    private String paginaAnterior = "nominaf";
    private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

    public ControlUsuariosTiposSueldos() {
        listUsuariosTSBorrar = new ArrayList<UsuariosTiposSueldos>();
        listUsuariosTSCrear = new ArrayList<UsuariosTiposSueldos>();
        listUsuariosTSModificar = new ArrayList<UsuariosTiposSueldos>();
        nuevoUsuarioTS = new UsuariosTiposSueldos();
        nuevoUsuarioTS.setUsuario(new Usuarios());
        nuevoUsuarioTS.setTiposueldo(new TiposSueldos());
        duplicarUsuarioTS = new UsuariosTiposSueldos();
        editarUsuarioTS = new UsuariosTiposSueldos();
        guardado = true;
        aceptar = true;
        activarLov = true;
        listUsuariosTS = null;
        altoTabla = "315";
        mapParametros.put("paginaAnterior", paginaAnterior);
    }

     @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarTiposSueldos.obtenerConexion(ses.getId());
            administrarUsuarios.obtenerConexion(ses.getId());
            administrarUsuariosTiposSueldos.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
            listUsuariosTS = null;
            getListUsuariosTS();
            if (listUsuariosTS != null) {
                if (!listUsuariosTS.isEmpty()) {
                    usuariotsSeleccionado = listUsuariosTS.get(0);
                }
            }
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }
    
    public void recibirPaginaEntrante(String pagina) {
        paginaAnterior = pagina;
        listUsuariosTS = null;
        getListUsuariosTS();
        if (listUsuariosTS != null) {
            if (!listUsuariosTS.isEmpty()) {
                usuariotsSeleccionado = listUsuariosTS.get(0);
            }
        }
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
        String pagActual = "usuariotiposueldo";
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
        lovTiposSueldos = null;
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
            listUsuariosTSFiltrar = null;
            tipoLista = 0;
        }
    }

    public void eventofiltrar() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
        contarRegistros();
    }

    public void cambiarIndice(UsuariosTiposSueldos usuariots, int celda) {
        usuariotsSeleccionado = usuariots;
        cualCelda = celda;
        usuariotsSeleccionado.getSecuencia();
        if (cualCelda == 0) {
            habilitarBotonLov();
            usuariotsSeleccionado.getUsuario().getAlias();
        } else if (cualCelda == 1) {
            habilitarBotonLov();
            usuariotsSeleccionado.getTiposueldo().getDescripcion();
        }
    }

    public void editarCelda() {
        if (usuariotsSeleccionado != null) {
            editarUsuarioTS = usuariotsSeleccionado;
            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarUsuario");
                RequestContext.getCurrentInstance().execute("PF('editarUsuario').show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarTS");
                RequestContext.getCurrentInstance().execute("PF('editarTS').show()");
                cualCelda = -1;
            }
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void asignarIndex(UsuariosTiposSueldos usuariots, int dlg, int LND) {
        usuariotsSeleccionado = usuariots;
        tipoActualizacion = LND;
        if (dlg == 0) {
            lovUsuarios = null;
            getLovUsuarios();
            contarRegistrosUsuarios();
            RequestContext.getCurrentInstance().update("formularioDialogos:usuariosDialogo");
            RequestContext.getCurrentInstance().execute("PF('usuariosDialogo').show()");
        } else if (dlg == 1) {
            lovTiposSueldos = null;
            getLovTiposSueldos();
            contarRegistrosTS();
            RequestContext.getCurrentInstance().update("formularioDialogos:tiposSueldosDialogo");
            RequestContext.getCurrentInstance().execute("PF('tiposSueldosDialogo').show()");
        }
    }

    public void listaValoresBoton() {
        if (usuariotsSeleccionado != null) {
            RequestContext context = RequestContext.getCurrentInstance();
            if (cualCelda == 0) {
                lovUsuarios = null;
                getLovUsuarios();
                contarRegistrosUsuarios();
                RequestContext.getCurrentInstance().update("formularioDialogos:usuariosDialogo");
                RequestContext.getCurrentInstance().execute("PF('usuariosDialogo').show()");
                tipoActualizacion = 0;
            } else if (cualCelda == 1) {
                lovTiposSueldos = null;
                getLovTiposSueldos();
                contarRegistrosTS();
                RequestContext.getCurrentInstance().update("formularioDialogos:tiposSueldosDialogo");
                RequestContext.getCurrentInstance().execute("PF('tiposSueldosDialogo').show()");
                tipoActualizacion = 0;
            }
        }
    }

    public void modificarUsuariosTS(UsuariosTiposSueldos usuariosts) {
        usuariotsSeleccionado = usuariosts;
        if (!listUsuariosTSCrear.contains(usuariotsSeleccionado)) {
            if (listUsuariosTSModificar.isEmpty()) {
                listUsuariosTSModificar.add(usuariotsSeleccionado);
            } else if (!listUsuariosTSModificar.contains(usuariotsSeleccionado)) {
                listUsuariosTSModificar.add(usuariotsSeleccionado);
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
        exporter.export(context, tabla, "UsuariosTiposSueldosPDF", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosUsuariosExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "UsuariosTiposSueldosXLS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void limpiarNuevoUsuarioTS() {
        nuevoUsuarioTS = new UsuariosTiposSueldos();
    }

    public void limpiarDuplicarUsuarioTS() {
        duplicarUsuarioTS = new UsuariosTiposSueldos();

    }

    public void guardarYSalir() {
        guardarCambiosUsuario();
        salir();
    }

    public void guardarCambiosUsuario() {
        try {
            if (guardado == false) {
                if (!listUsuariosTSBorrar.isEmpty()) {
                    administrarUsuariosTiposSueldos.borrarUsuariosTS(listUsuariosTSBorrar);
                    listUsuariosTSBorrar.clear();
                }
                if (!listUsuariosTSCrear.isEmpty()) {
                    administrarUsuariosTiposSueldos.crearUsuariosTS(listUsuariosTSCrear);
                    listUsuariosTSCrear.clear();
                }
                if (!listUsuariosTSModificar.isEmpty()) {
                    administrarUsuariosTiposSueldos.modificarUsuariosTS(listUsuariosTSModificar);
                    listUsuariosTSModificar.clear();
                }
                listUsuariosTS = null;
                getListUsuariosTS();
                contarRegistros();
                RequestContext.getCurrentInstance().update("form:datosUsuarios");
                guardado = true;
                FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                RequestContext.getCurrentInstance().update("form:growl");
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
                k = 0;
                usuariotsSeleccionado = null;
            }
        } catch (Exception e) {
            FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado, intente nuevamente.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }
    }

    public void borrarUsuarioTS() {
        if (usuariotsSeleccionado != null) {
            if (!listUsuariosTSModificar.isEmpty() && listUsuariosTSModificar.contains(usuariotsSeleccionado)) {
                int modIndex = listUsuariosTSModificar.indexOf(usuariotsSeleccionado);
                listUsuariosTSModificar.remove(modIndex);
                listUsuariosTSBorrar.add(usuariotsSeleccionado);
            } else if (!listUsuariosTSCrear.isEmpty() && listUsuariosTSCrear.contains(usuariotsSeleccionado)) {
                int crearIndex = listUsuariosTSCrear.indexOf(usuariotsSeleccionado);
                listUsuariosTSCrear.remove(crearIndex);
            } else {
                listUsuariosTSBorrar.add(usuariotsSeleccionado);
            }
            listUsuariosTS.remove(usuariotsSeleccionado);

            if (tipoLista == 1) {
                listUsuariosTSFiltrar.remove(usuariotsSeleccionado);
            }
            usuariotsSeleccionado = null;
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:datosUsuarios");
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void actualizarTiposSueldos() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            usuariotsSeleccionado.setTiposueldo(tipoSueldoSeleccionado);
            if (!listUsuariosTSCrear.contains(usuariotsSeleccionado)) {
                if (listUsuariosTSModificar.isEmpty()) {
                    listUsuariosTSModificar.add(usuariotsSeleccionado);
                } else if (!listUsuariosTSModificar.contains(usuariotsSeleccionado)) {
                    listUsuariosTSModificar.add(usuariotsSeleccionado);
                }
            }
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosUsuarios");
        } else if (tipoActualizacion == 1) {
            nuevoUsuarioTS.setTiposueldo(tipoSueldoSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaUsuario");
        } else if (tipoActualizacion == 2) {
            duplicarUsuarioTS.setTiposueldo(tipoSueldoSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarUsuario");
        }
        lovTiposSueldosFiltrar = null;
        tipoSueldoSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;

        RequestContext.getCurrentInstance().update("formularioDialogos:tiposSueldosDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovTS");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarTS");
        context.reset("formularioDialogos:lovTS:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovTS').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('tiposSueldosDialogo').hide()");
    }

    public void cancelarCambioTiposSueldos() {
        lovTiposSueldosFiltrar = null;
        tipoSueldoSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("formularioDialogos:tiposSueldosDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovTS");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarTS");
        context.reset("formularioDialogos:lovTS:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovTS').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('tiposSueldosDialogo').hide()");
    }

    public void actualizarUsuarios() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            usuariotsSeleccionado.setUsuario(usuarioSeleccionado);
            if (!listUsuariosTSCrear.contains(usuariotsSeleccionado)) {
                if (listUsuariosTSModificar.isEmpty()) {
                    listUsuariosTSModificar.add(usuariotsSeleccionado);
                } else if (!listUsuariosTSModificar.contains(usuariotsSeleccionado)) {
                    listUsuariosTSModificar.add(usuariotsSeleccionado);
                }
            }
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosUsuarios");
        } else if (tipoActualizacion == 1) {
            nuevoUsuarioTS.setUsuario(usuarioSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaUsuario");
        } else if (tipoActualizacion == 2) {
            duplicarUsuarioTS.setUsuario(usuarioSeleccionado);
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
        if (nuevoUsuarioTS.getUsuario() == null) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            pasa++;
        }
        if (nuevoUsuarioTS.getTiposueldo() == null) {
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
                listUsuariosTSFiltrar = null;
                tipoLista = 0;
            }
            k++;
            l = BigInteger.valueOf(k);
            nuevoUsuarioTS.setSecuencia(l);
            listUsuariosTSCrear.add(nuevoUsuarioTS);
            listUsuariosTS.add(nuevoUsuarioTS);
            usuariotsSeleccionado = nuevoUsuarioTS;
            contarRegistros();
            nuevoUsuarioTS = new UsuariosTiposSueldos();
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
        if (usuariotsSeleccionado != null) {
            duplicarUsuarioTS = new UsuariosTiposSueldos();
            duplicarUsuarioTS.setUsuario(usuariotsSeleccionado.getUsuario());
            duplicarUsuarioTS.setTiposueldo(usuariotsSeleccionado.getTiposueldo());
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
        duplicarUsuarioTS.setSecuencia(l);
        if (duplicarUsuarioTS.getUsuario() == null) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            pasa++;
        }
        if (duplicarUsuarioTS.getTiposueldo() == null) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            pasa++;
        }

        if (pasa == 0) {
            guardado = false;
//            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                columnausuario = (Column) c.getViewRoot().findComponent("form:datosUsuarios:columnausuario");
                columnausuario.setFilterStyle("display: none; visibility: hidden;");
                columnats = (Column) c.getViewRoot().findComponent("form:datosUsuarios:columnats");
                columnats.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosUsuarios");
                altoTabla = "315";
                bandera = 0;
                listUsuariosTSFiltrar = null;
                tipoLista = 0;
            }
            listUsuariosTS.add(duplicarUsuarioTS);
            listUsuariosTSCrear.add(duplicarUsuarioTS);
            usuariotsSeleccionado = duplicarUsuarioTS;
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:datosUsuarios");
            duplicarUsuarioTS = new UsuariosTiposSueldos();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarUsuario");
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroUsuario').hide()");
        } else {
            RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevaUsuario");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevaUsuario').show()");
        }
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (usuariotsSeleccionado != null) {
            int resultado = administrarRastros.obtenerTabla(usuariotsSeleccionado.getSecuencia(), "USUARIOSTIPOSSUELDOS");
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
        } else if (administrarRastros.verificarHistoricosTabla("USUARIOSTIPOSSUELDOS")) {
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
            listUsuariosTSFiltrar = null;
            tipoLista = 0;
        }
        listUsuariosTSBorrar.clear();
        listUsuariosTSCrear.clear();
        listUsuariosTSModificar.clear();
        usuariotsSeleccionado = null;
        k = 0;
        listUsuariosTS = null;
        getListUsuariosTS();
        contarRegistros();
        guardado = true;
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
            listUsuariosTSFiltrar = null;
            tipoLista = 0;
        }
        listUsuariosTSBorrar.clear();
        listUsuariosTSCrear.clear();
        listUsuariosTSModificar.clear();
        usuariotsSeleccionado = null;
        k = 0;
        listUsuariosTS = null;
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
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroTS");
    }

    public void habilitarBotonLov() {
        activarLov = false;
        RequestContext.getCurrentInstance().update("form:listaValores");
    }

    public void deshabilitarBotonLov() {
        activarLov = true;
        RequestContext.getCurrentInstance().update("form:listaValores");
    }

    ////GETS Y SETS////
    public List<UsuariosTiposSueldos> getListUsuariosTS() {
        if (listUsuariosTS == null) {
            listUsuariosTS = administrarUsuariosTiposSueldos.consultarUsuariosTS();
        }
        return listUsuariosTS;
    }

    public void setListUsuariosTS(List<UsuariosTiposSueldos> listUsuariosTS) {
        this.listUsuariosTS = listUsuariosTS;
    }

    public List<UsuariosTiposSueldos> getListUsuariosTSFiltrar() {
        return listUsuariosTSFiltrar;
    }

    public void setListUsuariosTSFiltrar(List<UsuariosTiposSueldos> listUsuariosTSFiltrar) {
        this.listUsuariosTSFiltrar = listUsuariosTSFiltrar;
    }

    public UsuariosTiposSueldos getUsuariotsSeleccionado() {
        return usuariotsSeleccionado;
    }

    public void setUsuariotsSeleccionado(UsuariosTiposSueldos usuariotsSeleccionado) {
        this.usuariotsSeleccionado = usuariotsSeleccionado;
    }

    public UsuariosTiposSueldos getNuevoUsuarioTS() {
        return nuevoUsuarioTS;
    }

    public void setNuevoUsuarioTS(UsuariosTiposSueldos nuevoUsuarioTS) {
        this.nuevoUsuarioTS = nuevoUsuarioTS;
    }

    public UsuariosTiposSueldos getDuplicarUsuarioTS() {
        return duplicarUsuarioTS;
    }

    public void setDuplicarUsuarioTS(UsuariosTiposSueldos duplicarUsuarioTS) {
        this.duplicarUsuarioTS = duplicarUsuarioTS;
    }

    public UsuariosTiposSueldos getEditarUsuarioTS() {
        return editarUsuarioTS;
    }

    public void setEditarUsuarioTS(UsuariosTiposSueldos editarUsuarioTS) {
        this.editarUsuarioTS = editarUsuarioTS;
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

    public List<TiposSueldos> getLovTiposSueldos() {
        if (lovTiposSueldos == null) {
            lovTiposSueldos = administrarTiposSueldos.listaTiposSueldos();
        }
        return lovTiposSueldos;
    }

    public void setLovTiposSueldos(List<TiposSueldos> lovTiposSueldos) {
        this.lovTiposSueldos = lovTiposSueldos;
    }

    public List<TiposSueldos> getLovTiposSueldosFiltrar() {
        return lovTiposSueldosFiltrar;
    }

    public void setLovTiposSueldosFiltrar(List<TiposSueldos> lovTiposSueldosFiltrar) {
        this.lovTiposSueldosFiltrar = lovTiposSueldosFiltrar;
    }

    public TiposSueldos getTipoSueldoSeleccionado() {
        return tipoSueldoSeleccionado;
    }

    public void setTipoSueldoSeleccionado(TiposSueldos tipoSueldoSeleccionado) {
        this.tipoSueldoSeleccionado = tipoSueldoSeleccionado;
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

    public String getInfoRegistroUsuariosTS() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosUsuarios");
        infoRegistroUsuariosTS = String.valueOf(tabla.getRowCount());
        return infoRegistroUsuariosTS;
    }

    public void setInfoRegistroUsuariosTS(String infoRegistroUsuariosTS) {
        this.infoRegistroUsuariosTS = infoRegistroUsuariosTS;
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

    public String getInfoRegistroTS() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovTS");
        infoRegistroTS = String.valueOf(tabla.getRowCount());
        return infoRegistroTS;
    }

    public void setInfoRegistroTS(String infoRegistroTS) {
        this.infoRegistroTS = infoRegistroTS;
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
