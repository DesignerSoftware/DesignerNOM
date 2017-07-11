/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import ControlNavegacion.ControlListaNavegacion;
import Entidades.Inforeportes;
import Entidades.Usuarios;
import Entidades.UsuariosInforeportes;
import InterfaceAdministrar.AdministrarRastrosInterface;
import InterfaceAdministrar.AdministrarUsuariosInfoReportesInterface;
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
import org.primefaces.context.RequestContext;

/**
 *
 * @author user
 */
@Named(value = "controlUsuariosInfoReportes")
@SessionScoped
public class ControlUsuariosInfoReportes implements Serializable {

    @EJB
    AdministrarUsuariosInfoReportesInterface administrarUsuariosIR;
    @EJB
    AdministrarRastrosInterface administrarRastros;

//tabla de arriba
    private List<Usuarios> listaUsuarios;
    private List<Usuarios> listaUsuariosFiltrar;
    private Usuarios usuarioSeleccionado;
    private Usuarios editarUsuario;
//tabla de abajo
    private List<UsuariosInforeportes> listaUsuariosIR;
    private List<UsuariosInforeportes> listaUsuariosIRFiltrar;
    private List<UsuariosInforeportes> listaUsuariosIRCrear;
    private List<UsuariosInforeportes> listaUsuariosIRModificar;
    private List<UsuariosInforeportes> listaUsuariosIRBorrar;
    private UsuariosInforeportes usuarioIRSeleccionado;
    private UsuariosInforeportes nuevoUsuarioIR;
    private UsuariosInforeportes duplicarUsuarioIR;
    private UsuariosInforeportes editarUsuarioIR;
    //lov UsuariosIR
    private List<UsuariosInforeportes> lovUsuariosIR;
    private List<UsuariosInforeportes> lovUsuariosIRFiltrar;
    private UsuariosInforeportes usuarioIRLovSeleccionado;
    // lov IR
    private List<Inforeportes> lovIR;
    private List<Inforeportes> lovIRFiltrar;
    private Inforeportes IRLovSeleccionado;
    //Columnas Usuario
    private Column alias, perfil, nombre;
    //Columnas Usuario InfoReportes
    private Column codigo, reporte, modulo, tipo;
    //otros
    private int cualCelda, cualCeldaIR, tipoLista, tipoActualizacion, k, bandera;
    private BigInteger l;
    private boolean aceptar, guardado;
    private String mensajeValidacion, altoTabla, altoTablaIR;
    private boolean activarLov;
    private int cualTabla;
    private String infoRegistroUsuario, infoRegistroUsuarioIR, infoRegistroLovIR, infoRegistroLovUsuariosIR;
    private String paginaAnterior = "nominaf";
    private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

    public ControlUsuariosInfoReportes() {
        listaUsuarios = null;
        lovIR = null;
        lovUsuariosIR = null;
        listaUsuariosIRBorrar = new ArrayList<UsuariosInforeportes>();
        listaUsuariosIRCrear = new ArrayList<UsuariosInforeportes>();
        listaUsuariosIRModificar = new ArrayList<UsuariosInforeportes>();
        editarUsuario = new Usuarios();
        editarUsuarioIR = new UsuariosInforeportes();
        nuevoUsuarioIR = new UsuariosInforeportes();
        duplicarUsuarioIR = new UsuariosInforeportes();
        guardado = true;
        altoTabla = "60";
        altoTablaIR = "190";
        cualTabla = -1;
        cualCelda = -1;
        cualCeldaIR = -1;
        usuarioSeleccionado = null;
        usuarioIRSeleccionado = null;
        activarLov = true;
        mapParametros.put("paginaAnterior", paginaAnterior);

    }

    public void limpiarListasValor() {
        lovIR = null;
        lovUsuariosIR = null;
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarUsuariosIR.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
            listaUsuarios = null;
            getListaUsuarios();
            if (listaUsuarios != null) {
                if (!listaUsuarios.isEmpty()) {
                    usuarioSeleccionado = listaUsuarios.get(0);
                }
            }
            listaUsuariosIR = null;
            getListaUsuariosIR();

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
    }

    public void navegar(String pag) {
        FacesContext fc = FacesContext.getCurrentInstance();
        ControlListaNavegacion controlListaNavegacion = (ControlListaNavegacion) fc.getApplication().evaluateExpressionGet(fc, "#{controlListaNavegacion}", ControlListaNavegacion.class);
        String pagActual = "usuarioinforeporte";
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

    public void cambiarIndice(Usuarios usu, int celda) {
        usuarioSeleccionado = usu;
        cualCelda = celda;
        usuarioSeleccionado.getSecuencia();
        cualTabla = 1;
        if (cualCelda == 0) {
            usuarioSeleccionado.getAlias();
        } else if (cualCelda == 1) {
            usuarioSeleccionado.getPerfil().getDescripcion();
        } else if (cualCelda == 2) {
            usuarioSeleccionado.getPersona().getNombreCompleto();
        }
    }

    public void cambiarIndiceIR(UsuariosInforeportes usu, int celda) {
        usuarioIRSeleccionado = usu;
        cualCeldaIR = celda;
        usuarioIRSeleccionado.getSecuencia();
        cualTabla = 1;
        if (cualCeldaIR == 0) {
            usuarioIRSeleccionado.getInforeporte().getCodigo();
        } else if (cualCeldaIR == 1) {
            usuarioIRSeleccionado.getInforeporte().getNombre();
        } else if (cualCeldaIR == 2) {
            usuarioIRSeleccionado.getInforeporte().getModulo().getNombre();
        } else if (cualCeldaIR == 3) {
            usuarioIRSeleccionado.getInforeporte().getTipo();
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
            codigo = (Column) c.getViewRoot().findComponent("form:datosUsuariosIR:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            reporte = (Column) c.getViewRoot().findComponent("form:datosUsuariosIR:reporte");
            reporte.setFilterStyle("display: none; visibility: hidden;");
            modulo = (Column) c.getViewRoot().findComponent("form:datosUsuariosIR:modulo");
            modulo.setFilterStyle("display: none; visibility: hidden;");
            tipo = (Column) c.getViewRoot().findComponent("form:datosUsuariosIR:tipo");
            tipo.setFilterStyle("display: none; visibility: hidden;");
            bandera = 0;
            listaUsuariosFiltrar = null;
            altoTabla = "60";
            altoTabla = "190";
            RequestContext.getCurrentInstance().update("form:datosUsuarios");
            RequestContext.getCurrentInstance().update("form:datosUsuariosIR");
            tipoLista = 0;
        }

        listaUsuariosIRBorrar.clear();
        listaUsuariosIRCrear.clear();
        listaUsuariosIRModificar.clear();
        usuarioSeleccionado = null;
        contarRegistrosUsuarios();
        contarRegistrosUsuariosIR();
        k = 0;
        listaUsuarios = null;
        getListaUsuarios();
        if (listaUsuarios != null) {
            if (!listaUsuarios.isEmpty()) {
                usuarioSeleccionado = listaUsuarios.get(0);
            }
        }
        listaUsuariosIR = null;
        getListaUsuariosIR();
        guardado = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosUsuarios");
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
            codigo = (Column) c.getViewRoot().findComponent("form:datosUsuariosIR:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            reporte = (Column) c.getViewRoot().findComponent("form:datosUsuariosIR:reporte");
            reporte.setFilterStyle("display: none; visibility: hidden;");
            modulo = (Column) c.getViewRoot().findComponent("form:datosUsuariosIR:modulo");
            modulo.setFilterStyle("display: none; visibility: hidden;");
            tipo = (Column) c.getViewRoot().findComponent("form:datosUsuariosIR:tipo");
            tipo.setFilterStyle("display: none; visibility: hidden;");

            RequestContext.getCurrentInstance().update("form:datosUsuarios");
            RequestContext.getCurrentInstance().update("form:datosUsuariosIR");
            bandera = 0;
            listaUsuariosFiltrar = null;
            tipoLista = 0;
            altoTabla = "60";
            altoTablaIR = "190";
        }
        listaUsuariosIRBorrar.clear();
        listaUsuariosIRCrear.clear();
        listaUsuariosIRModificar.clear();
        usuarioIRSeleccionado = null;
        k = 0;
        listaUsuariosIR = null;
        guardado = true;
        navegar("atras");
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            altoTabla = "40";
            altoTablaIR = "170";
            alias = (Column) c.getViewRoot().findComponent("form:datosUsuarios:alias");
            alias.setFilterStyle("width: 85% !important;");
            perfil = (Column) c.getViewRoot().findComponent("form:datosUsuarios:perfil");
            perfil.setFilterStyle("width: 85% !important;");
            nombre = (Column) c.getViewRoot().findComponent("form:datosUsuarios:nombre");
            nombre.setFilterStyle("width: 85% !important;");

            codigo = (Column) c.getViewRoot().findComponent("form:datosUsuariosIR:codigo");
            codigo.setFilterStyle("width: 85% !important;");
            reporte = (Column) c.getViewRoot().findComponent("form:datosUsuariosIR:reporte");
            reporte.setFilterStyle("width: 85% !important;");
            modulo = (Column) c.getViewRoot().findComponent("form:datosUsuariosIR:modulo");
            modulo.setFilterStyle("width: 85% !important;");
            tipo = (Column) c.getViewRoot().findComponent("form:datosUsuariosIR:tipo");
            tipo.setFilterStyle("width: 85% !important;");
            RequestContext.getCurrentInstance().update("form:datosUsuarios");
            RequestContext.getCurrentInstance().update("form:datosUsuariosIR");
            bandera = 1;
        } else if (bandera == 1) {
            System.out.println("Desactivar");
            altoTabla = "60";
            altoTablaIR = "190";
            alias = (Column) c.getViewRoot().findComponent("form:datosUsuarios:alias");
            alias.setFilterStyle("display: none; visibility: hidden;");
            perfil = (Column) c.getViewRoot().findComponent("form:datosUsuarios:perfil");
            perfil.setFilterStyle("display: none; visibility: hidden;");
            nombre = (Column) c.getViewRoot().findComponent("form:datosUsuarios:nombre");
            nombre.setFilterStyle("display: none; visibility: hidden;");
            codigo = (Column) c.getViewRoot().findComponent("form:datosUsuariosIR:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            reporte = (Column) c.getViewRoot().findComponent("form:datosUsuariosIR:reporte");
            reporte.setFilterStyle("display: none; visibility: hidden;");
            modulo = (Column) c.getViewRoot().findComponent("form:datosUsuariosIR:modulo");
            modulo.setFilterStyle("display: none; visibility: hidden;");
            tipo = (Column) c.getViewRoot().findComponent("form:datosUsuariosIR:tipo");
            tipo.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosUsuarios");
            RequestContext.getCurrentInstance().update("form:datosUsuariosIR");
            bandera = 0;
            listaUsuariosFiltrar = null;
            tipoLista = 0;
        }
    }

    public void eventoFiltrarUsuarios() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
        contarRegistrosUsuarios();
    }

    public void eventoFiltrarUsuariosIR() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
        contarRegistrosUsuariosIR();
    }

    public void contarRegistrosUsuarios() {
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
    }

    public void contarRegistrosUsuariosIR() {
        RequestContext.getCurrentInstance().update("form:informacionRegistroIR");
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
            editarUsuarioIR = usuarioIRSeleccionado;
            if (cualCeldaIR == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editCodigo");
                RequestContext.getCurrentInstance().execute("PF('editCodigo').show()");
                cualCeldaIR = -1;
            } else if (cualCeldaIR == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editReporte");
                RequestContext.getCurrentInstance().execute("PF('editReporte').show()");
                cualCeldaIR = -1;
            } else if (cualCeldaIR == 2) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editModulo");
                RequestContext.getCurrentInstance().execute("PF('editModulo').show()");
                cualCeldaIR = -1;
            } else if (cualCeldaIR == 3) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editTipo");
                RequestContext.getCurrentInstance().execute("PF('editTipo').show()");
                cualCeldaIR = -1;
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
    public void modificarUsuariosIR(UsuariosInforeportes usuIR) {
        usuarioIRSeleccionado = usuIR;
        if (!listaUsuariosIRCrear.contains(usuarioIRSeleccionado)) {
            if (listaUsuariosIRModificar.isEmpty()) {
                listaUsuariosIRModificar.add(usuarioIRSeleccionado);
            } else if (!listaUsuariosIRModificar.contains(usuarioIRSeleccionado)) {
                listaUsuariosIRModificar.add(usuarioIRSeleccionado);
            }
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
        RequestContext.getCurrentInstance().update("form:datosUsuariosIR");
    }

    public void borrarUsuarioInfoReportes() {
        if (usuarioIRSeleccionado != null) {
            if (!listaUsuariosIRModificar.isEmpty() && listaUsuariosIRModificar.contains(usuarioIRSeleccionado)) {
                listaUsuariosIRModificar.remove(listaUsuariosIRModificar.indexOf(usuarioIRSeleccionado));
                listaUsuariosIRBorrar.add(usuarioIRSeleccionado);
            } else if (!listaUsuariosIRCrear.isEmpty() && listaUsuariosIRCrear.contains(usuarioIRSeleccionado)) {
                listaUsuariosIRCrear.remove(listaUsuariosIRCrear.indexOf(usuarioIRSeleccionado));
            } else {
                listaUsuariosIRBorrar.add(usuarioIRSeleccionado);
            }
            listaUsuariosIR.remove(usuarioIRSeleccionado);
            if (tipoLista == 1) {
                listaUsuariosIRFiltrar.remove(usuarioIRSeleccionado);
            }
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosUsuariosIR");
            contarRegistrosUsuarios();
            usuarioIRSeleccionado = null;
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void revisarDialogoGuardar() {
        if (!listaUsuariosIRBorrar.isEmpty() || !listaUsuariosIRCrear.isEmpty() || !listaUsuariosIRModificar.isEmpty()) {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:confirmarGuardar");
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
        }
    }

    public void guardarUsuariosIR() {
        RequestContext context = RequestContext.getCurrentInstance();
        try {
            if (guardado == false) {
                if (!listaUsuariosIRBorrar.isEmpty()) {
                    administrarUsuariosIR.borrar(listaUsuariosIRBorrar);
                    listaUsuariosIRBorrar.clear();
                }
                if (!listaUsuariosIRModificar.isEmpty()) {
                    administrarUsuariosIR.editar(listaUsuariosIRModificar);
                    listaUsuariosIRModificar.clear();
                }
                if (!listaUsuariosIRCrear.isEmpty()) {
                    administrarUsuariosIR.crear(listaUsuariosIRCrear);
                    listaUsuariosIRCrear.clear();
                }
                listaUsuariosIR = null;
                getListaUsuariosIR();
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
                k = 0;
                FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                RequestContext.getCurrentInstance().update("form:growl");
                contarRegistrosUsuarios();
                usuarioIRSeleccionado = null;
            }
            guardado = true;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosUsuariosIR");
        } catch (Exception e) {
            System.out.println("Error guardarCambios : " + e.toString());
            FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado, intente nuevamente.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }

    }

    public void agregarNuevoUsuarioIR() {
        int contador = 0;
        int duplicados = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        mensajeValidacion = " ";

        if (nuevoUsuarioIR.getInforeporte() == null) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            contador++;
        }
        if (nuevoUsuarioIR.getUsuario() == null) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            contador++;
        }

        for (int i = 0; i < listaUsuariosIR.size(); i++) {
            if (listaUsuariosIR.get(i).getUsuario().equals(nuevoUsuarioIR.getUsuario()) && listaUsuariosIR.get(i).getInforeporte().equals(nuevoUsuarioIR.getInforeporte())) {
                duplicados++;
            }
        }

        if (contador == 0) {
            if (duplicados == 0) {
                if (bandera == 1) {
                    FacesContext c = FacesContext.getCurrentInstance();
                    codigo = (Column) c.getViewRoot().findComponent("form:datosUsuariosIR:codigo");
                    codigo.setFilterStyle("display: none; visibility: hidden;");
                    reporte = (Column) c.getViewRoot().findComponent("form:datosUsuariosIR:reporte");
                    reporte.setFilterStyle("display: none; visibility: hidden;");
                    modulo = (Column) c.getViewRoot().findComponent("form:datosUsuariosIR:modulo");
                    modulo.setFilterStyle("display: none; visibility: hidden;");
                    tipo = (Column) c.getViewRoot().findComponent("form:datosUsuariosIR:tipo");
                    tipo.setFilterStyle("display: none; visibility: hidden;");
                    bandera = 0;
                    listaUsuariosIRFiltrar = null;
                    tipoLista = 0;
                    altoTablaIR = "190";
                    RequestContext.getCurrentInstance().update("form:datosUsuariosIR");
                }
                k++;
                l = BigInteger.valueOf(k);
                nuevoUsuarioIR.setSecuencia(l);
                if (usuarioSeleccionado != null) {
                    nuevoUsuarioIR.setUsuario(usuarioSeleccionado);
                }
                listaUsuariosIRCrear.add(nuevoUsuarioIR);
                listaUsuariosIR.add(nuevoUsuarioIR);
                contarRegistrosUsuariosIR();
                usuarioIRSeleccionado = nuevoUsuarioIR;
                usuarioIRSeleccionado = new UsuariosInforeportes();
                RequestContext.getCurrentInstance().update("form:datosUsuariosIR");
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
                RequestContext.getCurrentInstance().execute("PF('nuevoRegistroTiposCursos').hide()");
            } else {
                RequestContext.getCurrentInstance().update("formularioDialogos:existeCodigo");
                RequestContext.getCurrentInstance().execute("PF('existeCodigo').show()");
            }
        } else {
            RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevoSector");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevoSector').show()");
        }
    }

    public void limpiarNuevoUsuarioI() {
        nuevoUsuarioIR = new UsuariosInforeportes();
    }

    public void duplicandoUsuariosIR() {
        if (usuarioIRSeleccionado != null) {
            duplicarUsuarioIR = new UsuariosInforeportes();
            k++;
            l = BigInteger.valueOf(k);

            duplicarUsuarioIR.setSecuencia(l);
            duplicarUsuarioIR.setUsuario(usuarioIRSeleccionado.getUsuario());
            duplicarUsuarioIR.setInforeporte(usuarioIRSeleccionado.getInforeporte());

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTE");
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroTiposCursos').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void confirmarDuplicar() {
        int contador = 0;
        int duplicados = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        mensajeValidacion = " ";

        if (duplicarUsuarioIR.getInforeporte() == null) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            contador++;
        }
        if (duplicarUsuarioIR.getUsuario() == null) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            contador++;
        }

        for (int i = 0; i < listaUsuariosIR.size(); i++) {
            if (listaUsuariosIR.get(i).getUsuario().equals(duplicarUsuarioIR.getUsuario()) && listaUsuariosIR.get(i).getInforeporte().equals(duplicarUsuarioIR.getInforeporte())) {
                duplicados++;
            }
        }

        if (contador == 0) {
            if (duplicados == 0) {
                listaUsuariosIR.add(duplicarUsuarioIR);
                listaUsuariosIRCrear.add(duplicarUsuarioIR);
                usuarioIRSeleccionado = duplicarUsuarioIR;
                contarRegistrosUsuarios();
                RequestContext.getCurrentInstance().update("form:datosUsuariosIR");
                if (guardado == true) {
                    guardado = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }

                if (bandera == 1) {
                    FacesContext c = FacesContext.getCurrentInstance();
                    //CERRAR FILTRADO
                    codigo = (Column) c.getViewRoot().findComponent("form:datosUsuariosIR:codigo");
                    codigo.setFilterStyle("display: none; visibility: hidden;");
                    reporte = (Column) c.getViewRoot().findComponent("form:datosUsuariosIR:reporte");
                    reporte.setFilterStyle("display: none; visibility: hidden;");
                    modulo = (Column) c.getViewRoot().findComponent("form:datosUsuariosIR:modulo");
                    modulo.setFilterStyle("display: none; visibility: hidden;");
                    tipo = (Column) c.getViewRoot().findComponent("form:datosUsuariosIR:tipo");
                    tipo.setFilterStyle("display: none; visibility: hidden;");
                    bandera = 0;
                    listaUsuariosIRFiltrar = null;
                    RequestContext.getCurrentInstance().update("form:datosUsuariosIR");
                    tipoLista = 0;
                    altoTablaIR = "190";
                }
                duplicarUsuarioIR = new UsuariosInforeportes();
                RequestContext.getCurrentInstance().update("formularioDialogos:duplicarRegistroTiposCursos");
                RequestContext.getCurrentInstance().execute("PF('duplicarRegistroTiposCursos').hide()");
            } else {
                RequestContext.getCurrentInstance().update("formularioDialogos:existeCodigo");
                RequestContext.getCurrentInstance().execute("PF('existeCodigo').show()");
            }
        } else {
            RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevoSector");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevoSector').show()");
        }
    }

    public void limpiarDuplicarUsuariosIR() {
        duplicarUsuarioIR = new UsuariosInforeportes();
    }

    public void asignarIndex(UsuariosInforeportes usuIR, int dlg, int LND) {
        usuarioIRSeleccionado = usuIR;
        tipoActualizacion = LND;
        if(dlg == 0){
            
        }
    }

    /*
        public void exportPDF() throws IOException {
            DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosUsuariosIRExportar");
            FacesContext context = FacesContext.getCurrentInstance();
            Exporter exporter = new ExportarPDF();
            exporter.export(context, tabla, "TIPOSCURSOS", false, false, "UTF-8", null, null);
            context.responseComplete();
        }

        public void exportXLS() throws IOException {
            DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosUsuariosIRExportar");
            FacesContext context = FacesContext.getCurrentInstance();
            Exporter exporter = new ExportarXLS();
            exporter.export(context, tabla, "TIPOSCURSOS", false, false, "UTF-8", null, null);
            context.responseComplete();
        }

        public void verificarRastro() {
            RequestContext context = RequestContext.getCurrentInstance();
            System.out.println("lol");
            if (usuarioIRSeleccionado != null) {
                System.out.println("lol 2");
                int resultado = administrarRastros.obtenerTabla(usuarioIRSeleccionado.getSecuencia(), "TIPOSCURSOS"); //En ENCARGATURAS lo cambia por el nombre de su tabla
                System.out.println("resultado: " + resultado);
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
            } else if (administrarRastros.verificarHistoricosTabla("TIPOSCURSOS")) { // igual acá
                RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
            } else {
                RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
            }
        }


        //*/
    ///////GETS Y SETS//////
    public List<Usuarios> getListaUsuarios() {
        if (listaUsuarios == null) {
            listaUsuarios = administrarUsuariosIR.listaUsuarios();
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

    public List<UsuariosInforeportes> getListaUsuariosIR() {
        if (usuarioIRSeleccionado != null) {
            if (listaUsuariosIR == null) {
                listaUsuariosIR = administrarUsuariosIR.listaUsuariosIR(usuarioIRSeleccionado.getSecuencia());
            }
        }
        return listaUsuariosIR;
    }

    public void setListaUsuariosIR(List<UsuariosInforeportes> listaUsuariosIR) {
        this.listaUsuariosIR = listaUsuariosIR;
    }

    public List<UsuariosInforeportes> getListaUsuariosIRFiltrar() {
        return listaUsuariosIRFiltrar;
    }

    public void setListaUsuariosIRFiltrar(List<UsuariosInforeportes> listaUsuariosIRFiltrar) {
        this.listaUsuariosIRFiltrar = listaUsuariosIRFiltrar;
    }

    public UsuariosInforeportes getUsuarioIRSeleccionado() {
        return usuarioIRSeleccionado;
    }

    public void setUsuarioIRSeleccionado(UsuariosInforeportes usuarioIRSeleccionado) {
        this.usuarioIRSeleccionado = usuarioIRSeleccionado;
    }

    public UsuariosInforeportes getNuevoUsuarioIR() {
        return nuevoUsuarioIR;
    }

    public void setNuevoUsuarioIR(UsuariosInforeportes nuevoUsuarioIR) {
        this.nuevoUsuarioIR = nuevoUsuarioIR;
    }

    public UsuariosInforeportes getDuplicarUsuarioIR() {
        return duplicarUsuarioIR;
    }

    public void setDuplicarUsuarioIR(UsuariosInforeportes duplicarUsuarioIR) {
        this.duplicarUsuarioIR = duplicarUsuarioIR;
    }

    public UsuariosInforeportes getEditarUsuarioIR() {
        return editarUsuarioIR;
    }

    public void setEditarUsuarioIR(UsuariosInforeportes editarUsuarioIR) {
        this.editarUsuarioIR = editarUsuarioIR;
    }

    public List<UsuariosInforeportes> getLovUsuariosIR() {
        return lovUsuariosIR;
    }

    public void setLovUsuariosIR(List<UsuariosInforeportes> lovUsuariosIR) {
        this.lovUsuariosIR = lovUsuariosIR;
    }

    public List<UsuariosInforeportes> getLovUsuariosIRFiltrar() {
        return lovUsuariosIRFiltrar;
    }

    public void setLovUsuariosIRFiltrar(List<UsuariosInforeportes> lovUsuariosIRFiltrar) {
        this.lovUsuariosIRFiltrar = lovUsuariosIRFiltrar;
    }

    public UsuariosInforeportes getUsuarioIRLovSeleccionado() {
        return usuarioIRLovSeleccionado;
    }

    public void setUsuarioIRLovSeleccionado(UsuariosInforeportes usuarioIRLovSeleccionado) {
        this.usuarioIRLovSeleccionado = usuarioIRLovSeleccionado;
    }

    public List<Inforeportes> getLovIR() {
        return lovIR;
    }

    public void setLovIR(List<Inforeportes> lovIR) {
        this.lovIR = lovIR;
    }

    public List<Inforeportes> getLovIRFiltrar() {
        return lovIRFiltrar;
    }

    public void setLovIRFiltrar(List<Inforeportes> lovIRFiltrar) {
        this.lovIRFiltrar = lovIRFiltrar;
    }

    public Inforeportes getIRLovSeleccionado() {
        return IRLovSeleccionado;
    }

    public void setIRLovSeleccionado(Inforeportes IRLovSeleccionado) {
        this.IRLovSeleccionado = IRLovSeleccionado;
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

    public String getAltoTablaIR() {
        return altoTablaIR;
    }

    public void setAltoTablaIR(String altoTablaIR) {
        this.altoTablaIR = altoTablaIR;
    }

    public boolean isActivarLov() {
        return activarLov;
    }

    public void setActivarLov(boolean activarLov) {
        this.activarLov = activarLov;
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

    public String getInfoRegistroUsuarioIR() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosUsuariosIR");
        infoRegistroUsuarioIR = String.valueOf(tabla.getRowCount());
        return infoRegistroUsuarioIR;
    }

    public void setInfoRegistroUsuarioIR(String infoRegistroUsuarioIR) {
        this.infoRegistroUsuarioIR = infoRegistroUsuarioIR;
    }

    public String getInfoRegistroLovIR() {
        return infoRegistroLovIR;
    }

    public void setInfoRegistroLovIR(String infoRegistroLovIR) {
        this.infoRegistroLovIR = infoRegistroLovIR;
    }

    public String getInfoRegistroLovUsuariosIR() {
        return infoRegistroLovUsuariosIR;
    }

    public void setInfoRegistroLovUsuariosIR(String infoRegistroLovUsuariosIR) {
        this.infoRegistroLovUsuariosIR = infoRegistroLovUsuariosIR;
    }

}
