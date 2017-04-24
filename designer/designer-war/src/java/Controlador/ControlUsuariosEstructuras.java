/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Administrar.AdministrarUsuariosEstructurasInterface;
import ControlNavegacion.ControlListaNavegacion;
import Entidades.Empresas;
import Entidades.Estructuras;
import Entidades.ObjetosDB;
import Entidades.Usuarios;
import Entidades.UsuariosEstructuras;
import Entidades.UsuariosFiltros;
import Entidades.UsuariosVistas;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarRastrosInterface;
import InterfaceAdministrar.AdministrarUsuariosFiltrosInterface;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.math.BigDecimal;
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
@Named(value = "controlUsuariosEstructuras")
@SessionScoped
public class ControlUsuariosEstructuras implements Serializable {

    @EJB
    AdministrarUsuariosEstructurasInterface administrarUsuariosEstructuras;
    @EJB
    AdministrarRastrosInterface administrarRastros;
    @EJB
    AdministrarUsuariosFiltrosInterface administrarUsuariosFiltros;

    //lista tabla superior
    private List<UsuariosEstructuras> listaUsuariosEstructuras;
    private List<UsuariosEstructuras> listaUsuariosEstructurasFiltrar;
    private List<UsuariosEstructuras> listaUsuariosEstructurasCrear;
    private List<UsuariosEstructuras> listaUsuariosEstructurasModificar;
    private List<UsuariosEstructuras> listaUsuariosEstructurasBorrar;
    private UsuariosEstructuras usuarioEstructuraSeleccionado;
    private UsuariosEstructuras nuevoUsuarioEstructura;
    private UsuariosEstructuras duplicarUsuarioEstructura;
    private UsuariosEstructuras editarUsuarioEstructura;
    //lovs tabla superior
    private List<Usuarios> lovUsuarios;
    private List<Usuarios> lovUsuariosFiltrar;
    private Usuarios lovUsuarioSeleccionado;
    private List<Empresas> lovEmpresas;
    private List<Empresas> lovEmpresasFiltrar;
    private Empresas lovEmpresaSeleccionado;
    private List<Estructuras> lovEstructuras;
    private List<Estructuras> lovEstructurasFiltrar;
    private Estructuras lovEstructuraSeleccionado;
    //Lista tabla inferior  
    private List<UsuariosFiltros> listaUsuariosFiltros;
    private List<UsuariosFiltros> listaUsuariosFiltrosFiltrar;
    private List<UsuariosFiltros> listaUsuariosFiltrosCrear;
    private List<UsuariosFiltros> listaUsuariosFiltrosModificar;
    private List<UsuariosFiltros> listaUsuariosFiltrosBorrar;
    private UsuariosFiltros usuariosFiltroSeleccionado;
    private UsuariosFiltros nuevoUsuarioFiltro;
    private UsuariosFiltros duplicarUsuarioFiltro;
    private UsuariosFiltros editarrUsuarioFiltro;

    //lovs tabla inferior
    private List<UsuariosVistas> lovUsuariosVistas;
    private List<UsuariosVistas> lovUsuariosVistasFiltrar;
    private UsuariosVistas lovUsuarioFiltroSeleccionado;
    //
    private Map<String, Object> mapParametros;
    private String paginaAnterior;
    private String altoTabla, altoTabla2, mensajeValidacion;
    //otros
    private int cualCelda, tipoLista, tipoActualizacion, k, bandera;
    private BigInteger l;
    private boolean aceptar, guardado, activarLov;
    private Column usuario, empresa, estructura, vista, descvista;
    private String tablaImprimir, nombreArchivo;
    private Usuarios usuarioParametro;
    private String infoRegistro, infoRegistroUsuarios, infoRegistroEstructuras, infoRegistroEmpresas, infoRegistroVista, infoRegistroLovvistas;
    private int cualtabla;

    public ControlUsuariosEstructuras() {
        listaUsuariosEstructuras = null;
        listaUsuariosEstructurasBorrar = new ArrayList<UsuariosEstructuras>();
        listaUsuariosEstructurasCrear = new ArrayList<UsuariosEstructuras>();
        listaUsuariosEstructurasModificar = new ArrayList<UsuariosEstructuras>();
        lovEmpresas = null;
        lovEmpresaSeleccionado = null;
        lovUsuarios = null;
        lovUsuarioSeleccionado = null;
        lovEstructuras = null;
        lovEstructuraSeleccionado = null;

        nuevoUsuarioEstructura = new UsuariosEstructuras();
        nuevoUsuarioEstructura.setEmpresa(new Empresas());
        nuevoUsuarioEstructura.setEstructura(new Estructuras());
        nuevoUsuarioEstructura.setUsuario(new Usuarios());
        duplicarUsuarioEstructura = new UsuariosEstructuras();
        editarUsuarioEstructura = new UsuariosEstructuras();
        usuarioEstructuraSeleccionado = null;

        altoTabla = "60";
        altoTabla2 = "190";
        aceptar = true;
        tipoLista = 0;
        cualCelda = -1;
        k = 0;
        guardado = true;
        paginaAnterior = "nominaf";
        mapParametros = new LinkedHashMap<String, Object>();
        mapParametros.put("paginaAnterior", paginaAnterior);
        activarLov = true;
        mensajeValidacion = "";

        listaUsuariosFiltros = null;
        nuevoUsuarioFiltro = new UsuariosFiltros();
        nuevoUsuarioFiltro.setUsuarioestructura(new UsuariosEstructuras());
        nuevoUsuarioFiltro.setUsuariovista(new UsuariosVistas());
        duplicarUsuarioFiltro = new UsuariosFiltros();
        editarrUsuarioFiltro = new UsuariosFiltros();
        usuariosFiltroSeleccionado = null;
        listaUsuariosFiltrosCrear = new ArrayList<UsuariosFiltros>();
        listaUsuariosFiltrosModificar = new ArrayList<UsuariosFiltros>();
        listaUsuariosFiltrosBorrar = new ArrayList<UsuariosFiltros>();
        lovUsuariosVistas = null;
        cualtabla = 0;
    }

    public void limpiarListasValor() {

    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarUsuariosEstructuras.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
            administrarUsuariosFiltros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void recibirPaginaEntrante(String pagina) {
        paginaAnterior = pagina;
    }

    public void recibirUsuario(String pagina, Usuarios usuario) {
        paginaAnterior = pagina;
        usuarioParametro = usuario;
        listaUsuariosEstructuras = null;
        getListaUsuariosEstructuras();
        if (listaUsuariosEstructuras != null) {
            if (!listaUsuariosEstructuras.isEmpty()) {
                usuarioEstructuraSeleccionado = listaUsuariosEstructuras.get(0);
            }
        }
    }

    public void recibirParametros(Map<String, Object> map) {
        mapParametros = map;
        paginaAnterior = (String) mapParametros.get("paginaAnterior");
        //inicializarCosas(); Inicializar cosas de ser necesario
    }

    public void navegar(String pag) {
        FacesContext fc = FacesContext.getCurrentInstance();
        ControlListaNavegacion controlListaNavegacion = (ControlListaNavegacion) fc.getApplication().evaluateExpressionGet(fc, "#{controlListaNavegacion}", ControlListaNavegacion.class);
        /*if (pag.equals("atras")) {
            pag = paginaAnterior;
            paginaAnterior = "nominaf";
            controlListaNavegacion.quitarPagina(pagActual);
         
        } else {
            */
String pagActual = "usuario";
            
            // mapParametros.put("paginaAnterior", pagActual);
            


            
            
            
            
            
            
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

    public void activarAceptar() {
        aceptar = false;
    }

    public void activarCtrlF11() {
        System.out.println("TipoLista= " + tipoLista);
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            usuario = (Column) c.getViewRoot().findComponent("form:datosUsuarios:usuario");
            usuario.setFilterStyle("width: 85% !important");
            empresa = (Column) c.getViewRoot().findComponent("form:datosUsuarios:empresa");
            empresa.setFilterStyle("width: 85% !important");
            estructura = (Column) c.getViewRoot().findComponent("form:datosUsuarios:estructura");
            estructura.setFilterStyle("width: 85% !important");
            vista = (Column) c.getViewRoot().findComponent("form:datosUsuariosVistas:vista");
            vista.setFilterStyle("width: 85% !important");
            descvista = (Column) c.getViewRoot().findComponent("form:datosUsuariosVistas:descvista");
            descvista.setFilterStyle("width: 85% !important");
            altoTabla = "40";
            altoTabla2 = "170";
            RequestContext.getCurrentInstance().update("form:datosUsuarios");
            RequestContext.getCurrentInstance().update("form:datosUsuariosVistas");
            bandera = 1;
            tipoLista = 1;
        } else if (bandera == 1) {
            usuario = (Column) c.getViewRoot().findComponent("form:datosUsuarios:usuario");
            usuario.setFilterStyle("display: none; visibility: hidden;");
            empresa = (Column) c.getViewRoot().findComponent("form:datosUsuarios:empresa");
            empresa.setFilterStyle("display: none; visibility: hidden;");
            estructura = (Column) c.getViewRoot().findComponent("form:datosUsuarios:estructura");
            estructura.setFilterStyle("display: none; visibility: hidden;");
            vista = (Column) c.getViewRoot().findComponent("form:datosUsuariosVistas:vista");
            vista.setFilterStyle("display: none; visibility: hidden;");
            descvista = (Column) c.getViewRoot().findComponent("form:datosUsuariosVistas:descvista");
            descvista.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosUsuarios");
            RequestContext.getCurrentInstance().update("form:datosUsuariosVistas");
            altoTabla = "60";
            altoTabla2 = "190";
            bandera = 0;
            listaUsuariosEstructurasFiltrar = null;
            listaUsuariosFiltrosFiltrar = null;
            tipoLista = 0;
        }
    }

    public void cargarInfo(UsuariosEstructuras usuarioE){
        usuarioEstructuraSeleccionado = usuarioE;
        listaUsuariosFiltros = null;
        getListaUsuariosFiltros();
        contarRegistrosVistas();
        RequestContext.getCurrentInstance().update("form:datosUsuariosVistas");
    }
    
    public void eventofiltrar() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
        contarRegistros();
    }

    public void eventofiltrarVistas() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
        contarRegistrosVistas();
    }

    public void cambiarIndice(UsuariosEstructuras usuarioe, int celda) {
        usuarioEstructuraSeleccionado = usuarioe;
        cualCelda = celda;
        usuarioEstructuraSeleccionado.getSecuencia();
        cualtabla = 0;
        if (cualCelda == 0) {
            habilitarBotonLov();
            usuarioEstructuraSeleccionado.getUsuario().getAlias();
        } else if (cualCelda == 1) {
            habilitarBotonLov();
            usuarioEstructuraSeleccionado.getEmpresa().getNombre();
        } else if (cualCelda == 2) {
            habilitarBotonLov();
            usuarioEstructuraSeleccionado.getEstructura().getNombre();
        }
        cargarInfo(usuarioEstructuraSeleccionado);
    }

    public void cambiarIndiceVista(UsuariosFiltros usuariov, int celda) {
        usuariosFiltroSeleccionado = usuariov;
        cualCelda = celda;
        usuariosFiltroSeleccionado.getSecuencia();
        cualtabla = 1;
        if (cualCelda == 0) {
            habilitarBotonLov();
            usuariosFiltroSeleccionado.getUsuariovista().getDescripcion();
        } else if (cualCelda == 1) {
            deshabilitarBotonLov();
            usuariosFiltroSeleccionado.getUsuariovista().getObjetodb().getNombre();
        }
    }

    public void editarCelda() {
        if (usuarioEstructuraSeleccionado != null) {
            editarUsuarioEstructura = usuarioEstructuraSeleccionado;
            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarUsuario");
                RequestContext.getCurrentInstance().execute("PF('editarUsuario').show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarEmpresa");
                RequestContext.getCurrentInstance().execute("PF('editarEmpresa').show()");
                cualCelda = -1;
            } else if (cualCelda == 2) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarEstructura");
                RequestContext.getCurrentInstance().execute("PF('editarEstructura').show()");
                cualCelda = -1;
            }
        } else if (usuariosFiltroSeleccionado != null) {
            editarrUsuarioFiltro = usuariosFiltroSeleccionado;
            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarVista");
                RequestContext.getCurrentInstance().execute("PF('editarVista').show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarDescVista");
                RequestContext.getCurrentInstance().execute("PF('editarDescVista').show()");
                cualCelda = -1;
            }
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void asignarIndex(UsuariosEstructuras usuarioe, int dlg, int LND) {
        usuarioEstructuraSeleccionado = usuarioe;
        tipoActualizacion = LND;
        if (dlg == 0) {
            lovUsuarios = null;
            contarRegistrosUsuarios();
            RequestContext.getCurrentInstance().update("formularioDialogos:usuariosDialogo");
            RequestContext.getCurrentInstance().execute("PF('usuariosDialogo').show()");
        } else if (dlg == 1) {
            lovEmpresas = null;
            contarRegistrosEmpresas();
            RequestContext.getCurrentInstance().update("formularioDialogos:empresasDialogo");
            RequestContext.getCurrentInstance().execute("PF('empresasDialogo').show()");
        } else if (dlg == 2) {
            lovEstructuras = null;
            contarRegistrosEstructuras();
            RequestContext.getCurrentInstance().update("formularioDialogos:estructurasDialogo");
            RequestContext.getCurrentInstance().execute("PF('estructurasDialogo').show()");
        }
    }

    public void asignarIndexVistas(UsuariosFiltros usuariov, int dlg, int LND) {
        usuariosFiltroSeleccionado = usuariov;
        tipoActualizacion = LND;
        if (dlg == 0) {
            lovUsuariosVistas = null;
            contarRegistrosLovVistas();
            RequestContext.getCurrentInstance().update("formularioDialogos:vistasDialogo");
            RequestContext.getCurrentInstance().execute("PF('vistasDialogo').show()");
        }
    }

    public void actualizarVista() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            usuariosFiltroSeleccionado.setUsuariovista(lovUsuarioFiltroSeleccionado);
            if (!listaUsuariosFiltrosCrear.contains(usuariosFiltroSeleccionado)) {
                if (listaUsuariosFiltrosModificar.isEmpty()) {
                    listaUsuariosFiltrosModificar.add(usuariosFiltroSeleccionado);
                } else if (!listaUsuariosFiltrosModificar.contains(usuariosFiltroSeleccionado)) {
                    listaUsuariosFiltrosModificar.add(usuariosFiltroSeleccionado);
                }
            }
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosUsuariosVistas");
        } else if (tipoActualizacion == 1) {
            nuevoUsuarioFiltro.setUsuariovista(lovUsuarioFiltroSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoUsuarioFiltro");
        } else if (tipoActualizacion == 2) {
            duplicarUsuarioFiltro.setUsuariovista(lovUsuarioFiltroSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarUsuarioFiltro");
        }
        lovUsuariosVistasFiltrar = null;
        lovUsuarioFiltroSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;

        RequestContext.getCurrentInstance().update("formularioDialogos:vistasDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:LOVVistas");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarV");
        context.reset("formularioDialogos:LOVVistas:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVVistas').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('vistasDialogo').hide()");
    }

    public void cancelarCambioVista() {
        lovUsuariosVistasFiltrar = null;
        lovUsuarioFiltroSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("formularioDialogos:vistasDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:LOVVistas");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarV");
        context.reset("formularioDialogos:LOVVistas:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVVistas').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('vistasDialogo').hide()");
    }

    public void actualizarUsuario() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            usuarioEstructuraSeleccionado.setUsuario(lovUsuarioSeleccionado);
            if (!listaUsuariosEstructurasCrear.contains(usuarioEstructuraSeleccionado)) {
                if (listaUsuariosEstructurasModificar.isEmpty()) {
                    listaUsuariosEstructurasModificar.add(usuarioEstructuraSeleccionado);
                } else if (!listaUsuariosEstructurasModificar.contains(usuarioEstructuraSeleccionado)) {
                    listaUsuariosEstructurasModificar.add(usuarioEstructuraSeleccionado);
                }
            }
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosUsuarios");
        } else if (tipoActualizacion == 1) {
            nuevoUsuarioEstructura.setUsuario(lovUsuarioSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaUsuario");
        } else if (tipoActualizacion == 2) {
            duplicarUsuarioEstructura.setUsuario(lovUsuarioSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarUsuario");
        }
        lovUsuariosFiltrar = null;
        lovUsuarioSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;

        RequestContext.getCurrentInstance().update("formularioDialogos:usuariosDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:LOVUsuarios");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarAU");
        context.reset("formularioDialogos:LOVUsuarios:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVUsuarios').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('usuariosDialogo').hide()");
    }

    public void cancelarCambioUsuario() {
        lovUsuariosFiltrar = null;
        lovUsuarioSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("formularioDialogos:usuariosDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:LOVUsuarios");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarAU");
        context.reset("formularioDialogos:LOVUsuarios:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVUsuarios').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('usuariosDialogo').hide()");
    }

    public void actualizarEmpresa() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            usuarioEstructuraSeleccionado.setEmpresa(lovEmpresaSeleccionado);
            if (!listaUsuariosEstructurasCrear.contains(usuarioEstructuraSeleccionado)) {
                if (listaUsuariosEstructurasModificar.isEmpty()) {
                    listaUsuariosEstructurasModificar.add(usuarioEstructuraSeleccionado);
                } else if (!listaUsuariosEstructurasModificar.contains(usuarioEstructuraSeleccionado)) {
                    listaUsuariosEstructurasModificar.add(usuarioEstructuraSeleccionado);
                }
            }
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosUsuarios");
        } else if (tipoActualizacion == 1) {
            nuevoUsuarioEstructura.setEmpresa(lovEmpresaSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaUsuario");
        } else if (tipoActualizacion == 2) {
            duplicarUsuarioEstructura.setEmpresa(lovEmpresaSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarUsuario");
        }
        lovEmpresasFiltrar = null;
        lovEmpresaSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        RequestContext.getCurrentInstance().update("formularioDialogos:empresasDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:LOVEmpresas");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarE");
        context.reset("formularioDialogos:LOVEmpresas:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVEmpresas').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('empresasDialogo').hide()");
    }

    public void cancelarCambioEmpresa() {
        lovEmpresasFiltrar = null;
        lovEmpresaSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("formularioDialogos:empresasDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:LOVEmpresas");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarE");
        context.reset("formularioDialogos:LOVEmpresas:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVEmpresas').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('empresasDialogo').hide()");
    }

    public void actualizarEstructuras() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            usuarioEstructuraSeleccionado.setEstructura(lovEstructuraSeleccionado);
            if (!listaUsuariosEstructurasCrear.contains(usuarioEstructuraSeleccionado)) {
                if (listaUsuariosEstructurasModificar.isEmpty()) {
                    listaUsuariosEstructurasCrear.add(usuarioEstructuraSeleccionado);
                } else if (!listaUsuariosEstructurasCrear.contains(usuarioEstructuraSeleccionado)) {
                    listaUsuariosEstructurasCrear.add(usuarioEstructuraSeleccionado);
                }
            }
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosUsuarios");
        } else if (tipoActualizacion == 1) {
            nuevoUsuarioEstructura.setEstructura(lovEstructuraSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaUsuario");
        } else if (tipoActualizacion == 2) {
            duplicarUsuarioEstructura.setEstructura(lovEstructuraSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarUsuario");
        }
        lovEstructurasFiltrar = null;
        lovEstructuraSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        RequestContext.getCurrentInstance().update("formularioDialogos:estructurasDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:LOVEstructura");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarES");
        context.reset("formularioDialogos:LOVEstructura:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVEstructura').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('estructurasDialogo').hide()");
    }

    public void cancelarCambioEstructura() {
        lovEstructurasFiltrar = null;
        lovEstructuraSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("formularioDialogos:estructurasDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:LOVEstructura");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarES");
        context.reset("formularioDialogos:LOVEstructura:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVEstructura').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('estructurasDialogo').hide()");
    }

    public void listaValoresBoton() {
        if (cualtabla == 0) {
            if (usuarioEstructuraSeleccionado != null) {
                RequestContext context = RequestContext.getCurrentInstance();
                if (cualCelda == 0) {
                    lovUsuarios = null;
                    getLovUsuarios();
                    RequestContext.getCurrentInstance().update("formularioDialogos:usuariosDialogo");
                    RequestContext.getCurrentInstance().execute("PF('usuariosDialogo').show()");
                    tipoActualizacion = 0;
                } else if (cualCelda == 1) {
                    lovEmpresas = null;
                    getLovEmpresas();
                    RequestContext.getCurrentInstance().update("formularioDialogos:empresasDialogo");
                    RequestContext.getCurrentInstance().execute("PF('empresasDialogo').show()");
                    tipoActualizacion = 0;
                } else if (cualCelda == 3) {
                    lovEstructuras = null;
                    getLovEstructuras();
                    RequestContext.getCurrentInstance().update("formularioDialogos:estructurasDialogo");
                    RequestContext.getCurrentInstance().execute("PF('estructurasDialogo').show()");
                    tipoActualizacion = 0;
                }
            }
        } else if (cualtabla == 1) {
            if (usuariosFiltroSeleccionado != null) {
                if (cualCelda == 0) {
                    lovUsuariosVistas = null;
                    getLovUsuariosVistas();
                    RequestContext.getCurrentInstance().update("formularioDialogos:vistasDialogo");
                    RequestContext.getCurrentInstance().execute("PF('vistasDialogo').show()");
                    tipoActualizacion = 0;
                }
            }
        }

    }

    public void modificarUsuarios(UsuariosEstructuras usuario) {
        usuarioEstructuraSeleccionado = usuario;
        if (!listaUsuariosEstructurasCrear.contains(usuarioEstructuraSeleccionado)) {
            if (listaUsuariosEstructurasModificar.isEmpty()) {
                listaUsuariosEstructurasModificar.add(usuarioEstructuraSeleccionado);
            } else if (!listaUsuariosEstructurasModificar.contains(usuarioEstructuraSeleccionado)) {
                listaUsuariosEstructurasModificar.add(usuarioEstructuraSeleccionado);
            }
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
        RequestContext.getCurrentInstance().update("form:datosUsuarios");
    }

    public void modificarUsuariosVistas(UsuariosFiltros usuariov) {
        usuariosFiltroSeleccionado = usuariov;
        if (!listaUsuariosFiltrosCrear.contains(usuariosFiltroSeleccionado)) {
            if (listaUsuariosFiltrosModificar.isEmpty()) {
                listaUsuariosFiltrosModificar.add(usuariosFiltroSeleccionado);
            } else if (!listaUsuariosFiltrosModificar.contains(usuariosFiltroSeleccionado)) {
                listaUsuariosFiltrosModificar.add(usuariosFiltroSeleccionado);
            }
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
        RequestContext.getCurrentInstance().update("form:datosUsuariosVistas");
    }

    public void exportPDF() throws IOException {
        if (cualtabla == 0) {
            if (usuarioEstructuraSeleccionado != null) {
                DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosUsuariosEstructurasExportar");
                FacesContext context = FacesContext.getCurrentInstance();
                Exporter exporter = new ExportarPDF();
                exporter.export(context, tabla, "UsuariosEstructurasPDF", false, false, "UTF-8", null, null);
                context.responseComplete();
            }
        } else if (cualtabla == 1) {
            if (usuariosFiltroSeleccionado != null) {
                DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosUsuariosVistasExportar");
                FacesContext context = FacesContext.getCurrentInstance();
                Exporter exporter = new ExportarPDF();
                exporter.export(context, tabla, "UsuariosVistasPDF", false, false, "UTF-8", null, null);
                context.responseComplete();
            }
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }

    }

    public void exportXLS() throws IOException {
        if (cualtabla == 0) {
            if (usuarioEstructuraSeleccionado != null) {
                DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosUsuariosEstructurasExportar");
                FacesContext context = FacesContext.getCurrentInstance();
                Exporter exporter = new ExportarXLS();
                exporter.export(context, tabla, "UsuariosEstructurasXLS", false, false, "UTF-8", null, null);
                context.responseComplete();
            }
        } else if (cualtabla == 1) {
            if (usuariosFiltroSeleccionado != null) {
                DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosUsuariosVistasExportar");
                FacesContext context = FacesContext.getCurrentInstance();
                Exporter exporter = new ExportarXLS();
                exporter.export(context, tabla, "UsuariosVistasXLS", false, false, "UTF-8", null, null);
                context.responseComplete();
            }
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void limpiarExportar() {
        if (cualtabla == 0) {
            limpiarNUevoUsuarioEstructura();
            tablaImprimir = ":formExportar:datosUsuarioExportar";
            nombreArchivo = "UsuariosXML";
        } else if (cualtabla == 1) {
            limpiarNuevoUsuarioFiltro();
            tablaImprimir = ":formExportar:datosUsuarioFiltroExportar";
            nombreArchivo = "UsuariosVistasXML";
        } else {
            RequestContext.getCurrentInstance().execute("PF('formularioDialogos:seleccionarRegistro').show()");
        }

    }

    public void limpiarNUevoUsuarioEstructura() {
        nuevoUsuarioEstructura = new UsuariosEstructuras();
        nuevoUsuarioEstructura.setEmpresa(new Empresas());
        nuevoUsuarioEstructura.setEstructura(new Estructuras());
        nuevoUsuarioEstructura.setUsuario(new Usuarios());
    }

    public void limpiarDuplicarUsuarioEstructura() {
        duplicarUsuarioEstructura = new UsuariosEstructuras();
        duplicarUsuarioEstructura.setEmpresa(new Empresas());
        duplicarUsuarioEstructura.setEstructura(new Estructuras());
        duplicarUsuarioEstructura.setUsuario(new Usuarios());
    }

    public void limpiarNuevoUsuarioFiltro() {
        nuevoUsuarioFiltro = new UsuariosFiltros();
        nuevoUsuarioFiltro.setUsuarioestructura(new UsuariosEstructuras());
        nuevoUsuarioFiltro.setUsuariovista(new UsuariosVistas());
    }

    public void limpiarDuplicarUsuarioFiltro() {
        duplicarUsuarioFiltro = new UsuariosFiltros();
        duplicarUsuarioFiltro.setUsuarioestructura(new UsuariosEstructuras());
        duplicarUsuarioFiltro.setUsuariovista(new UsuariosVistas());
    }

    public void guardarCambiosUsuariosE() {
        try {
            if (guardado == false) {
                if (!listaUsuariosEstructurasBorrar.isEmpty()) {
                    administrarUsuariosEstructuras.borrarUsuarioEstructura(listaUsuariosEstructurasBorrar);
                    listaUsuariosEstructurasBorrar.clear();
                }
                if (!listaUsuariosEstructurasCrear.isEmpty()) {
                    administrarUsuariosEstructuras.crearUsuarioEstructura(listaUsuariosEstructurasCrear);
                    listaUsuariosEstructurasCrear.clear();
                }
                if (!listaUsuariosEstructurasModificar.isEmpty()) {
                    administrarUsuariosEstructuras.modificarUsuarioEstructura(listaUsuariosEstructurasModificar);
                    listaUsuariosEstructurasModificar.clear();
                }

                if (!listaUsuariosFiltrosBorrar.isEmpty()) {
                    administrarUsuariosFiltros.borrarUsuarioFiltro(listaUsuariosFiltrosBorrar);
                    listaUsuariosFiltrosBorrar.clear();
                }
                if (!listaUsuariosFiltrosCrear.isEmpty()) {
                    administrarUsuariosFiltros.crearUsuarioFiltro(listaUsuariosFiltrosCrear);
                    listaUsuariosFiltrosCrear.clear();
                }
                if (!listaUsuariosFiltrosModificar.isEmpty()) {
                    administrarUsuariosFiltros.modificarUsuarioFiltro(listaUsuariosFiltrosModificar);
                    listaUsuariosFiltrosModificar.clear();
                }

                listaUsuariosEstructuras = null;
                getListaUsuariosEstructuras();
                contarRegistros();
                listaUsuariosFiltros = null;
                getListaUsuariosFiltros();
                contarRegistrosVistas();
                RequestContext.getCurrentInstance().update("form:datosUsuarios");
                RequestContext.getCurrentInstance().update("form:datosUsuariosVistas");
                guardado = true;
                FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                RequestContext.getCurrentInstance().update("form:growl");
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
                k = 0;
                usuarioEstructuraSeleccionado = null;
                usuariosFiltroSeleccionado = null;
            }
        } catch (Exception e) {
            FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado, intente nuevamente.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }
    }

    public void verificarBorrado(){
        BigDecimal contarUsuariosEstructuras;
        BigDecimal contarUsuariosFiltros;
          try {
            contarUsuariosEstructuras = administrarUsuariosEstructuras.contarUsuariosEstructuras(usuarioEstructuraSeleccionado.getSecuencia());
            contarUsuariosFiltros = administrarUsuariosFiltros.contarUsuariosFiltros(usuarioEstructuraSeleccionado.getSecuencia());
         if (contarUsuariosEstructuras.equals(new BigInteger("0")) && contarUsuariosFiltros.equals(new BigInteger("0"))) {
            borrarUsuarioEstructura();
         } else {
            RequestContext.getCurrentInstance().update("formularioDialogos:validacionBorrar");
            RequestContext.getCurrentInstance().execute("PF('validacionBorrar').show()");
            usuarioEstructuraSeleccionado = null;
            contarUsuariosEstructuras = new BigDecimal("-1");
            contarUsuariosFiltros = new BigDecimal("-1");
         }
      } catch (Exception e) {
         System.err.println("ERROR ControlTiposFamiliares verificarBorrado ERROR " + e);
      }
        
    }
    
    public void borrarRegistro(){
        if(cualtabla == 0){
          verificarBorrado();
        }else if(cualtabla == 1){
            borrarUsuarioFiltro();
        }else{
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show");
        }
    }
    
    public void borrarUsuarioEstructura() {
        if (usuarioEstructuraSeleccionado != null) {
            if (!listaUsuariosEstructurasModificar.isEmpty() && listaUsuariosEstructurasModificar.contains(usuarioEstructuraSeleccionado)) {
                int modIndex = listaUsuariosEstructurasModificar.indexOf(usuarioEstructuraSeleccionado);
                listaUsuariosEstructurasModificar.remove(modIndex);
                listaUsuariosEstructurasBorrar.add(usuarioEstructuraSeleccionado);
            } else if (!listaUsuariosEstructurasCrear.isEmpty() && listaUsuariosEstructurasCrear.contains(usuarioEstructuraSeleccionado)) {
                int crearIndex = listaUsuariosEstructurasCrear.indexOf(usuarioEstructuraSeleccionado);
                listaUsuariosEstructurasCrear.remove(crearIndex);
            } else {
                listaUsuariosEstructurasBorrar.add(usuarioEstructuraSeleccionado);
            }
            listaUsuariosEstructuras.remove(usuarioEstructuraSeleccionado);

            if (tipoLista == 1) {
                listaUsuariosEstructurasFiltrar.remove(usuarioEstructuraSeleccionado);
            }
            usuarioEstructuraSeleccionado = null;
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:datosUsuarios");
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
    }

    public void borrarUsuarioFiltro() {
        if (usuariosFiltroSeleccionado != null) {
            if (!listaUsuariosFiltrosModificar.isEmpty() && listaUsuariosFiltrosModificar.contains(usuariosFiltroSeleccionado)) {
                int modIndex = listaUsuariosFiltrosModificar.indexOf(usuariosFiltroSeleccionado);
                listaUsuariosFiltrosModificar.remove(modIndex);
                listaUsuariosFiltrosBorrar.add(usuariosFiltroSeleccionado);
            } else if (!listaUsuariosFiltrosCrear.isEmpty() && listaUsuariosFiltrosCrear.contains(usuariosFiltroSeleccionado)) {
                int crearIndex = listaUsuariosFiltrosCrear.indexOf(usuariosFiltroSeleccionado);
                listaUsuariosFiltrosCrear.remove(crearIndex);
            } else {
                listaUsuariosFiltrosBorrar.add(usuariosFiltroSeleccionado);
            }
            listaUsuariosEstructuras.remove(usuariosFiltroSeleccionado);

            if (tipoLista == 1) {
                listaUsuariosFiltrosFiltrar.remove(usuariosFiltroSeleccionado);
            }
            usuariosFiltroSeleccionado = null;
            contarRegistrosVistas();
            RequestContext.getCurrentInstance().update("form:datosUsuariosVistas");
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
    }

    public void agregarNuevoUsuarioEstructura() {
        int pasa = 0;
        int duplicados = 0;
        mensajeValidacion = " ";
        if (nuevoUsuarioEstructura.getUsuario().getAlias() == null) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            pasa++;
        }
        if (nuevoUsuarioEstructura.getEstructura().getNombre() == null) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            pasa++;
        }

        for (int i = 0; i < listaUsuariosEstructuras.size(); i++) {
            if (listaUsuariosEstructuras.get(i).getUsuario().getSecuencia() == nuevoUsuarioEstructura.getUsuario().getSecuencia()
                    && listaUsuariosEstructuras.get(i).getEstructura().getSecuencia() == nuevoUsuarioEstructura.getEstructura().getSecuencia()) {
                duplicados++;
            }
            if (listaUsuariosEstructuras.get(i).getEstructura().getSecuencia() == nuevoUsuarioEstructura.getEstructura().getSecuencia()) {
                duplicados++;
            }
            if(listaUsuariosEstructuras.get(i).getEmpresa().getSecuencia() == nuevoUsuarioEstructura.getEmpresa().getSecuencia()){
                duplicados ++;
            }
            
        }
        if (pasa == 0) {
            if (duplicados == 0) {
                if (bandera == 1) {
                    FacesContext c = FacesContext.getCurrentInstance();
                    usuario = (Column) c.getViewRoot().findComponent("form:datosUsuarios:usuario");
                    usuario.setFilterStyle("display: none; visibility: hidden;");
                    empresa = (Column) c.getViewRoot().findComponent("form:datosUsuarios:empresa");
                    empresa.setFilterStyle("display: none; visibility: hidden;");
                    estructura = (Column) c.getViewRoot().findComponent("form:datosUsuarios:estructura");
                    estructura.setFilterStyle("display: none; visibility: hidden;");
                    RequestContext.getCurrentInstance().update("form:datosUsuarios");
                    altoTabla = "60";
                    bandera = 0;
                    listaUsuariosEstructurasFiltrar = null;
                    tipoLista = 0;
                }
                k++;
                l = BigInteger.valueOf(k);
                nuevoUsuarioEstructura.setSecuencia(l);
                listaUsuariosEstructurasCrear.add(nuevoUsuarioEstructura);
                listaUsuariosEstructuras.add(nuevoUsuarioEstructura);
                usuarioEstructuraSeleccionado = nuevoUsuarioEstructura;
                contarRegistros();
                nuevoUsuarioEstructura = new UsuariosEstructuras();
                RequestContext.getCurrentInstance().update("form:datosUsuarios");
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
                RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroUsuario");
                RequestContext.getCurrentInstance().execute("PF('NuevoRegistroUsuario').hide()");
            } else {
                RequestContext.getCurrentInstance().update("formularioDialogos:existeUsuario");
                RequestContext.getCurrentInstance().execute("PF('existeUsuario').show()");
            }
        } else {
            RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevaUsuario");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevaUsuario').show()");
        }
    }

    public void agregarNuevoUsuarioFiltro() {
        int pasa = 0;
        int duplicados = 0;
        mensajeValidacion = " ";
        if (nuevoUsuarioFiltro.getUsuariovista().getDescripcion() == null) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            pasa++;
        }

        for (int i = 0; i < listaUsuariosFiltros.size(); i++) {
            if (listaUsuariosFiltros.get(i).getUsuariovista().getDescripcion().equals(nuevoUsuarioFiltro.getUsuariovista().getDescripcion())) {
                duplicados++;
            }
        }

        if (pasa == 0) {
            if (duplicados == 0) {
                if (bandera == 1) {
                    FacesContext c = FacesContext.getCurrentInstance();
                    vista = (Column) c.getViewRoot().findComponent("form:datosUsuarios:vista");
                    vista.setFilterStyle("display: none; visibility: hidden;");
                    descvista = (Column) c.getViewRoot().findComponent("form:datosUsuarios:descvista");
                    descvista.setFilterStyle("display: none; visibility: hidden;");
                    RequestContext.getCurrentInstance().update("form:datosUsuariosVistas");
                    altoTabla = "190";
                    bandera = 0;
                    listaUsuariosFiltrosFiltrar = null;
                    tipoLista = 0;
                }
                k++;
                l = BigInteger.valueOf(k);
                nuevoUsuarioFiltro.setSecuencia(l);
                nuevoUsuarioFiltro.setUsuarioestructura(usuarioEstructuraSeleccionado);
//                nuevoUsuarioFiltro.setUsuariovista(new UsuariosVistas());
                listaUsuariosFiltrosCrear.add(nuevoUsuarioFiltro);
                listaUsuariosFiltros.add(nuevoUsuarioFiltro);
                usuariosFiltroSeleccionado = nuevoUsuarioFiltro;
                contarRegistrosVistas();
                nuevoUsuarioFiltro = new UsuariosFiltros();
                nuevoUsuarioFiltro.setUsuarioestructura(new UsuariosEstructuras());
                nuevoUsuarioFiltro.setUsuariovista(new UsuariosVistas());
                RequestContext.getCurrentInstance().update("form:datosUsuariosVistas");
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
                RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroUsuarioVista");
                RequestContext.getCurrentInstance().execute("PF('NuevoRegistroUsuarioVista').hide()");
            } else {
                RequestContext.getCurrentInstance().update("formularioDialogos:existeVista");
                RequestContext.getCurrentInstance().execute("PF('existeVista').show()");
            }
        } else {
            RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevaUsuario");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevaUsuario').show()");
        }

    }

    public void duplicandoUsuarioEstructura() {
        if (usuarioEstructuraSeleccionado != null) {
            duplicarUsuarioEstructura = new UsuariosEstructuras();
            duplicarUsuarioEstructura.setUsuario(usuarioEstructuraSeleccionado.getUsuario());
            duplicarUsuarioEstructura.setEmpresa(usuarioEstructuraSeleccionado.getEmpresa());
            duplicarUsuarioEstructura.setEstructura(usuarioEstructuraSeleccionado.getEstructura());
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarUsuario");
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroUsuario').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void confirmarDuplicar() {

        int pasa = 0;
        int duplicados = 0;
        mensajeValidacion = " ";
        if (duplicarUsuarioEstructura.getUsuario().getAlias() == null) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            pasa++;
        }
        if (duplicarUsuarioEstructura.getEstructura().getNombre() == null) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            pasa++;
        }
        for (int i = 0; i < listaUsuariosEstructuras.size(); i++) {
            if (listaUsuariosEstructuras.get(i).getUsuario().getSecuencia() == duplicarUsuarioEstructura.getUsuario().getSecuencia()
                    && listaUsuariosEstructuras.get(i).getEstructura().getSecuencia() == duplicarUsuarioEstructura.getEstructura().getSecuencia()) {
                duplicados++;
            }
            if (listaUsuariosEstructuras.get(i).getEstructura().getSecuencia() == duplicarUsuarioEstructura.getEstructura().getSecuencia()) {
                duplicados++;
            }
            if(listaUsuariosEstructuras.get(i).getEmpresa().getSecuencia() == duplicarUsuarioEstructura.getEmpresa().getSecuencia()){
                duplicados ++;
            }
        }
        if (pasa == 0) {
            if (duplicados == 0) {
                if (bandera == 1) {
                    FacesContext c = FacesContext.getCurrentInstance();
                    usuario = (Column) c.getViewRoot().findComponent("form:datosUsuarios:usuario");
                    usuario.setFilterStyle("display: none; visibility: hidden;");
                    empresa = (Column) c.getViewRoot().findComponent("form:datosUsuarios:empresa");
                    empresa.setFilterStyle("display: none; visibility: hidden;");
                    estructura = (Column) c.getViewRoot().findComponent("form:datosUsuarios:estructura");
                    estructura.setFilterStyle("display: none; visibility: hidden;");
                    RequestContext.getCurrentInstance().update("form:datosUsuarios");
                    altoTabla = "60";
                    bandera = 0;
                    listaUsuariosEstructurasFiltrar = null;
                    tipoLista = 0;
                }
                l = BigInteger.valueOf(k);
                duplicarUsuarioEstructura.setSecuencia(l);
                listaUsuariosEstructuras.add(duplicarUsuarioEstructura);
                listaUsuariosEstructurasCrear.add(duplicarUsuarioEstructura);
                usuarioEstructuraSeleccionado = duplicarUsuarioEstructura;
                contarRegistros();
                RequestContext.getCurrentInstance().update("form:datosUsuarios");
                duplicarUsuarioEstructura = new UsuariosEstructuras();
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
                RequestContext.getCurrentInstance().update("formularioDialogos:duplicarUsuario");
                RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroUsuario').hide()");
            } else {
                RequestContext.getCurrentInstance().update("formularioDialogos:existeUsuario");
                RequestContext.getCurrentInstance().execute("PF('existeUsuario').show()");
            }
        } else {
            RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevaUsuario");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevaUsuario').show()");
        }
    }

    public void duplicandoUsuarioFiltro() {
        if (usuariosFiltroSeleccionado != null) {
            duplicarUsuarioFiltro = new UsuariosFiltros();
            duplicarUsuarioFiltro.setUsuarioestructura(usuariosFiltroSeleccionado.getUsuarioestructura());
            duplicarUsuarioFiltro.setUsuariovista(usuariosFiltroSeleccionado.getUsuariovista());
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarUsuarioFiltro");
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroUsuarioFiltro').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void confirmarDuplicarVista() {
        int pasa = 0;
        int duplicados = 0;
        mensajeValidacion = " ";
        if (duplicarUsuarioFiltro.getUsuariovista().getDescripcion() == null) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            pasa++;
        }

        for (int i = 0; i < listaUsuariosFiltros.size(); i++) {
            if (listaUsuariosFiltros.get(i).getUsuariovista().getDescripcion().equals(duplicarUsuarioFiltro.getUsuariovista().getDescripcion())) {
                duplicados++;
            }
        }

        if (pasa == 0) {
            if (duplicados == 0) {
                if (bandera == 1) {
                    FacesContext c = FacesContext.getCurrentInstance();
                    vista = (Column) c.getViewRoot().findComponent("form:datosUsuarios:vista");
                    vista.setFilterStyle("display: none; visibility: hidden;");
                    descvista = (Column) c.getViewRoot().findComponent("form:datosUsuarios:descvista");
                    descvista.setFilterStyle("display: none; visibility: hidden;");
                    RequestContext.getCurrentInstance().update("form:datosUsuariosVistas");
                    altoTabla = "190";
                    bandera = 0;
                    listaUsuariosFiltrosFiltrar = null;
                    tipoLista = 0;
                }
                k++;
                l = BigInteger.valueOf(k);
                nuevoUsuarioFiltro.setSecuencia(l);
                listaUsuariosFiltrosCrear.add(duplicarUsuarioFiltro);
                listaUsuariosFiltros.add(duplicarUsuarioFiltro);
                usuariosFiltroSeleccionado = duplicarUsuarioFiltro;
                contarRegistrosVistas();
                duplicarUsuarioFiltro = new UsuariosFiltros();
                RequestContext.getCurrentInstance().update("form:datosUsuariosVistas");
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
                RequestContext.getCurrentInstance().update("formularioDialogos:DuplicarRegistroUsuarioFiltro");
                RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroUsuarioFiltro').hide()");
            } else {
                RequestContext.getCurrentInstance().update("formularioDialogos:existeVista");
                RequestContext.getCurrentInstance().execute("PF('existeVista').show()");
            }
        } else {
            RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevaUsuario");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevaUsuario').show()");
        }
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (usuarioEstructuraSeleccionado != null) {
            int resultado = administrarRastros.obtenerTabla(usuarioEstructuraSeleccionado.getSecuencia(), "USUARIOSESTRUCTURAS");
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
        } else if (administrarRastros.verificarHistoricosTabla("USUARIOSESTRUCTURAS")) {
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
    }

    public void verificarRastroUsuariosVistas() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (usuariosFiltroSeleccionado != null) {
            int resultado = administrarRastros.obtenerTabla(usuariosFiltroSeleccionado.getSecuencia(), "USUARIOSFILTROS");
            if (resultado == 1) {
                RequestContext.getCurrentInstance().execute("PF('errorObjetosDBFiltros').show()");
            } else if (resultado == 2) {
                RequestContext.getCurrentInstance().execute("PF('confirmarRastroFiltros').show()");
            } else if (resultado == 3) {
                RequestContext.getCurrentInstance().execute("PF('errorRegistroRastroFiltros').show()");
            } else if (resultado == 4) {
                RequestContext.getCurrentInstance().execute("PF('errorTablaConRastroFiltros').show()");
            } else if (resultado == 5) {
                RequestContext.getCurrentInstance().execute("PF('errorTablaSinRastroFiltros').show()");
            }
        } else if (administrarRastros.verificarHistoricosTabla("USUARIOSFILTROS")) {
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistoricoFiltros').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistoricoFiltros').show()");
        }
    }

    public void llamarRastro() {
        if (cualtabla == 0) {
            verificarRastroUsuariosVistas();
        } else if (cualtabla == 1) {
            verificarRastro();
        }
    }

    public void cancelarModificacion() {
        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            usuario = (Column) c.getViewRoot().findComponent("form:datosUsuarios:usuario");
            usuario.setFilterStyle("display: none; visibility: hidden;");
            empresa = (Column) c.getViewRoot().findComponent("form:datosUsuarios:empresa");
            empresa.setFilterStyle("display: none; visibility: hidden;");
            estructura = (Column) c.getViewRoot().findComponent("form:datosUsuarios:estructura");
            estructura.setFilterStyle("display: none; visibility: hidden;");
            vista = (Column) c.getViewRoot().findComponent("form:datosUsuariosVistas:vista");
            vista.setFilterStyle("display: none; visibility: hidden;");
            descvista = (Column) c.getViewRoot().findComponent("form:datosUsuariosVistas:descvista");
            descvista.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosUsuarios");
            RequestContext.getCurrentInstance().update("form:datosUsuariosVistas");
            altoTabla = "60";
            altoTabla2 = "190";
            bandera = 0;
            listaUsuariosEstructurasFiltrar = null;
            listaUsuariosFiltrosFiltrar = null;
            tipoLista = 0;
        }

        listaUsuariosEstructurasCrear.clear();
        listaUsuariosEstructurasBorrar.clear();
        listaUsuariosEstructurasModificar.clear();
        usuarioEstructuraSeleccionado = null;

        listaUsuariosFiltrosBorrar.clear();
        listaUsuariosFiltrosCrear.clear();
        listaUsuariosFiltrosModificar.clear();
        usuariosFiltroSeleccionado = null;
        k = 0;
        listaUsuariosEstructuras = null;
        getListaUsuariosEstructuras();
        contarRegistros();
        listaUsuariosFiltros = null;
        getListaUsuariosFiltros();
        contarRegistros();
        guardado = true;
        RequestContext.getCurrentInstance().update("form:datosUsuarios");
        RequestContext.getCurrentInstance().update("form:datosUsuariosVistas");
    }

    public void salir() {  limpiarListasValor();
        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            usuario = (Column) c.getViewRoot().findComponent("form:datosUsuarios:usuario");
            usuario.setFilterStyle("display: none; visibility: hidden;");
            empresa = (Column) c.getViewRoot().findComponent("form:datosUsuarios:empresa");
            empresa.setFilterStyle("display: none; visibility: hidden;");
            estructura = (Column) c.getViewRoot().findComponent("form:datosUsuarios:estructura");
            estructura.setFilterStyle("display: none; visibility: hidden;");
            vista = (Column) c.getViewRoot().findComponent("form:datosUsuariosVistas:vista");
            vista.setFilterStyle("display: none; visibility: hidden;");
            descvista = (Column) c.getViewRoot().findComponent("form:datosUsuariosVistas:descvista");
            descvista.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosUsuarios");
            RequestContext.getCurrentInstance().update("form:datosUsuariosVistas");
            altoTabla = "60";
            altoTabla2 = "190";
            bandera = 0;
            listaUsuariosEstructurasFiltrar = null;
            listaUsuariosFiltrosFiltrar = null;
            tipoLista = 0;
        }
        listaUsuariosEstructurasCrear.clear();
        listaUsuariosEstructurasBorrar.clear();
        listaUsuariosEstructurasModificar.clear();
        usuarioEstructuraSeleccionado = null;

        listaUsuariosFiltrosBorrar.clear();
        listaUsuariosFiltrosCrear.clear();
        listaUsuariosFiltrosModificar.clear();
        usuariosFiltroSeleccionado = null;
        k = 0;
        listaUsuariosEstructuras = null;
        listaUsuariosFiltros = null;
        guardado = true;
        RequestContext.getCurrentInstance().update("form:datosUsuarios");
        RequestContext.getCurrentInstance().update("form:datosUsuariosVistas");
        navegar("atras");
    }

    public void crearVistaUsuarioEstructura(){
        try{
          administrarUsuariosEstructuras.crearVistaUsuarioEstructura(usuarioEstructuraSeleccionado.getSecuencia(), usuarioEstructuraSeleccionado.getUsuario().getSecuencia());
            RequestContext.getCurrentInstance().execute("PF('crearVistaUsuarioEstructura').show()");
        }catch(Exception e){
            System.out.println("error Controlador.ControlUsuariosEstructuras.crearVistaUsuarioEstructura()" + e.getMessage() );
            RequestContext.getCurrentInstance().execute("PF('errorCrearVistaUsuarioEstructura').show()");
        }
    }
    
    public void crearFiltroUsuario(){
        try{
           administrarUsuariosFiltros.crearFiltroUsuario(usuariosFiltroSeleccionado.getUsuariovista().getSecuencia());
            RequestContext.getCurrentInstance().execute("PF('crearFiltroUsuario').show()");
        }catch(Exception e){
            System.out.println("error ControlUsuariosEstructuras.crearFiltroUsuario() : " + e.getMessage()); 
            RequestContext.getCurrentInstance().execute("PF('errorCrearFiltroUsuario').show()");
        }
    }
    
    public void contarRegistros() {
        RequestContext.getCurrentInstance().update("form:infoRegistro");
    }

    public void contarRegistrosVistas() {
        RequestContext.getCurrentInstance().update("form:infoRegistroVista");
    }

    public void contarRegistrosLovVistas() {
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroLovVista");
    }

    public void contarRegistrosEmpresas() {
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroEmpresas");
    }

    public void contarRegistrosEstructuras() {
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroEstructuras");
    }

    public void contarRegistrosUsuarios() {
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroUsuarios");
    }

    public void habilitarBotonLov() {
        activarLov = false;
        RequestContext.getCurrentInstance().update("form:listaValores");
    }

    public void deshabilitarBotonLov() {
        activarLov = true;
        RequestContext.getCurrentInstance().update("form:listaValores");
    }

    public void mostrarDialogoInsertarUsuario() {
        RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroUsuario");
        RequestContext.getCurrentInstance().execute("PF('NuevoRegistroUsuario').show()");
    }

    public void mostrarDialogoInsertarVista() {
        RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroUsuarioVista");
        RequestContext.getCurrentInstance().execute("PF('NuevoRegistroUsuarioVista').show()");
    }

    ///////////////GETTERS Y SETTERS/////////////
    public List<UsuariosEstructuras> getListaUsuariosEstructuras() {
        if (listaUsuariosEstructuras == null) {
            listaUsuariosEstructuras = administrarUsuariosEstructuras.consultarUsuariosEstructuras(usuarioParametro.getSecuencia());
        }

        return listaUsuariosEstructuras;
    }

    public void setListaUsuariosEstructuras(List<UsuariosEstructuras> listaUsuariosEstructuras) {
        this.listaUsuariosEstructuras = listaUsuariosEstructuras;
    }

    public List<UsuariosEstructuras> getListaUsuariosEstructurasFiltrar() {
        return listaUsuariosEstructurasFiltrar;
    }

    public void setListaUsuariosEstructurasFiltrar(List<UsuariosEstructuras> listaUsuariosEstructurasFiltrar) {
        this.listaUsuariosEstructurasFiltrar = listaUsuariosEstructurasFiltrar;
    }

    public UsuariosEstructuras getUsuarioEstructuraSeleccionado() {
        return usuarioEstructuraSeleccionado;
    }

    public void setUsuarioEstructuraSeleccionado(UsuariosEstructuras usuarioEstructuraSeleccionado) {
        this.usuarioEstructuraSeleccionado = usuarioEstructuraSeleccionado;
    }

    public UsuariosEstructuras getNuevoUsuarioEstructura() {
        return nuevoUsuarioEstructura;
    }

    public void setNuevoUsuarioEstructura(UsuariosEstructuras nuevoUsuarioEstructura) {
        this.nuevoUsuarioEstructura = nuevoUsuarioEstructura;
    }

    public UsuariosEstructuras getDuplicarUsuarioEstructura() {
        return duplicarUsuarioEstructura;
    }

    public void setDuplicarUsuarioEstructura(UsuariosEstructuras duplicarUsuarioEstructura) {
        this.duplicarUsuarioEstructura = duplicarUsuarioEstructura;
    }

    public UsuariosEstructuras getEditarUsuarioEstructura() {
        return editarUsuarioEstructura;
    }

    public void setEditarUsuarioEstructura(UsuariosEstructuras editarUsuarioEstructura) {
        this.editarUsuarioEstructura = editarUsuarioEstructura;
    }

    public List<Usuarios> getLovUsuarios() {
        if (lovUsuarios == null) {
            lovUsuarios = administrarUsuariosEstructuras.lovUsuarios();
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

    public Usuarios getLovUsuarioSeleccionado() {
        return lovUsuarioSeleccionado;
    }

    public void setLovUsuarioSeleccionado(Usuarios lovUsuarioSeleccionado) {
        this.lovUsuarioSeleccionado = lovUsuarioSeleccionado;
    }

    public List<Empresas> getLovEmpresas() {
        if (lovEmpresas == null) {
            lovEmpresas = administrarUsuariosEstructuras.lovEmpresas();
        }
        return lovEmpresas;
    }

    public void setLovEmpresas(List<Empresas> lovEmpresas) {
        this.lovEmpresas = lovEmpresas;
    }

    public List<Empresas> getLovEmpresasFiltrar() {
        return lovEmpresasFiltrar;
    }

    public void setLovEmpresasFiltrar(List<Empresas> lovEmpresasFiltrar) {
        this.lovEmpresasFiltrar = lovEmpresasFiltrar;
    }

    public Empresas getLovEmpresaSeleccionado() {
        return lovEmpresaSeleccionado;
    }

    public void setLovEmpresaSeleccionado(Empresas lovEmpresaSeleccionado) {
        this.lovEmpresaSeleccionado = lovEmpresaSeleccionado;
    }

    public List<Estructuras> getLovEstructuras() {
        if (lovEstructuras == null) {
            lovEstructuras = administrarUsuariosEstructuras.lovEstructuras();
        }
        return lovEstructuras;
    }

    public void setLovEstructuras(List<Estructuras> lovEstructuras) {
        this.lovEstructuras = lovEstructuras;
    }

    public List<Estructuras> getLovEstructurasFiltrar() {
        return lovEstructurasFiltrar;
    }

    public void setLovEstructurasFiltrar(List<Estructuras> lovEstructurasFiltrar) {
        this.lovEstructurasFiltrar = lovEstructurasFiltrar;
    }

    public Estructuras getLovEstructuraSeleccionado() {
        return lovEstructuraSeleccionado;
    }

    public void setLovEstructuraSeleccionado(Estructuras lovEstructuraSeleccionado) {
        this.lovEstructuraSeleccionado = lovEstructuraSeleccionado;
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

    public String getInfoRegistro() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosUsuarios");
        infoRegistro = String.valueOf(tabla.getRowCount());
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

    public String getInfoRegistroUsuarios() {
        return infoRegistroUsuarios;
    }

    public void setInfoRegistroUsuarios(String infoRegistroUsuarios) {
        this.infoRegistroUsuarios = infoRegistroUsuarios;
    }

    public String getInfoRegistroEstructuras() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:LOVEstructura");
        infoRegistroEstructuras = String.valueOf(tabla.getRowCount());
        return infoRegistroEstructuras;
    }

    public void setInfoRegistroEstructuras(String infoRegistroEstructuras) {
        this.infoRegistroEstructuras = infoRegistroEstructuras;
    }

    public String getInfoRegistroEmpresas() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:LOVEmpresas");
        infoRegistroEmpresas = String.valueOf(tabla.getRowCount());
        return infoRegistroEmpresas;
    }

    public void setInfoRegistroEmpresas(String infoRegistroEmpresas) {
        this.infoRegistroEmpresas = infoRegistroEmpresas;
    }

    public String getAltoTabla() {
        return altoTabla;
    }

    public void setAltoTabla(String altoTabla) {
        this.altoTabla = altoTabla;
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

    public boolean isActivarLov() {
        return activarLov;
    }

    public void setActivarLov(boolean activarLov) {
        this.activarLov = activarLov;
    }

    public String getMensajeValidacion() {
        return mensajeValidacion;
    }

    public void setMensajeValidacion(String mensajeValidacion) {
        this.mensajeValidacion = mensajeValidacion;
    }

    public List<UsuariosFiltros> getListaUsuariosFiltros() {
        if (listaUsuariosFiltros == null) {
            if(usuarioEstructuraSeleccionado != null){
            listaUsuariosFiltros = administrarUsuariosFiltros.consultarUsuariosFiltros(usuarioEstructuraSeleccionado.getSecuencia());
            }
        }
        return listaUsuariosFiltros;
    }

    public void setListaUsuariosFiltros(List<UsuariosFiltros> listaUsuariosFiltros) {
        this.listaUsuariosFiltros = listaUsuariosFiltros;
    }

    public List<UsuariosFiltros> getListaUsuariosFiltrosFiltrar() {
        return listaUsuariosFiltrosFiltrar;
    }

    public void setListaUsuariosFiltrosFiltrar(List<UsuariosFiltros> listaUsuariosFiltrosFiltrar) {
        this.listaUsuariosFiltrosFiltrar = listaUsuariosFiltrosFiltrar;
    }

    public List<UsuariosVistas> getLovUsuariosVistas() {
        if (lovUsuariosVistas == null) {
            lovUsuariosVistas = administrarUsuariosEstructuras.listaUsuariosVistas();
        }
        return lovUsuariosVistas;
    }

    public void setLovUsuariosVistas(List<UsuariosVistas> lovUsuariosVistas) {
        this.lovUsuariosVistas = lovUsuariosVistas;
    }

    public List<UsuariosVistas> getLovUsuariosVistasFiltrar() {
        return lovUsuariosVistasFiltrar;
    }

    public void setLovUsuariosVistasFiltrar(List<UsuariosVistas> lovUsuariosVistasFiltrar) {
        this.lovUsuariosVistasFiltrar = lovUsuariosVistasFiltrar;
    }

    public UsuariosVistas getLovUsuarioFiltroSeleccionado() {
        return lovUsuarioFiltroSeleccionado;
    }

    public void setLovUsuarioFiltroSeleccionado(UsuariosVistas lovUsuarioFiltroSeleccionado) {
        this.lovUsuarioFiltroSeleccionado = lovUsuarioFiltroSeleccionado;
    }

    public String getAltoTabla2() {
        return altoTabla2;
    }

    public void setAltoTabla2(String altoTabla2) {
        this.altoTabla2 = altoTabla2;
    }

    public UsuariosFiltros getNuevoUsuarioFiltro() {
        return nuevoUsuarioFiltro;
    }

    public void setNuevoUsuarioFiltro(UsuariosFiltros nuevoUsuarioFiltro) {
        this.nuevoUsuarioFiltro = nuevoUsuarioFiltro;
    }

    public UsuariosFiltros getDuplicarUsuarioFiltro() {
        return duplicarUsuarioFiltro;
    }

    public void setDuplicarUsuarioFiltro(UsuariosFiltros duplicarUsuarioFiltro) {
        this.duplicarUsuarioFiltro = duplicarUsuarioFiltro;
    }

    public UsuariosFiltros getEditarUsuarioFiltro() {
        return editarrUsuarioFiltro;
    }

    public void setEditarUsuarioFiltro(UsuariosFiltros editarrUsuarioFiltro) {
        this.editarrUsuarioFiltro = editarrUsuarioFiltro;
    }

    public String getInfoRegistroVista() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosUsuariosVistas");
        infoRegistroVista = String.valueOf(tabla.getRowCount());
        return infoRegistroVista;
    }

    public void setInfoRegistroVista(String infoRegistroVista) {
        this.infoRegistroVista = infoRegistroVista;
    }

    public String getInfoRegistroLovvistas() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:LOVVistas");
        infoRegistroLovvistas = String.valueOf(tabla.getRowCount());
        return infoRegistroLovvistas;
    }

    public void setInfoRegistroLovvistas(String infoRegistroLovvistas) {
        this.infoRegistroLovvistas = infoRegistroLovvistas;
    }

    public UsuariosFiltros getUsuariosFiltroSeleccionado() {
        return usuariosFiltroSeleccionado;
    }

    public void setUsuariosFiltroSeleccionado(UsuariosFiltros usuariosFiltroSeleccionado) {
        this.usuariosFiltroSeleccionado = usuariosFiltroSeleccionado;
    }

}
