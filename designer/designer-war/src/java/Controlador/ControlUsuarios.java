package Controlador;

import Administrar.AdministrarUsuarios;
import ControlNavegacion.ControlListaNavegacion;
import Entidades.Pantallas;
import Entidades.Perfiles;
import Entidades.Personas;
import Entidades.Usuarios;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarRastrosInterface;
import InterfaceAdministrar.AdministrarUsuariosInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import ControlNavegacion.ControlListaNavegacion;
import Entidades.Ciudades;
import Entidades.Inforeportes;
import Entidades.TiposDocumentos;
import InterfaceAdministrar.AdministarReportesInterface;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.LinkedHashMap;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.persistence.PersistenceException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.fill.AsynchronousFilllListener;
import org.primefaces.component.column.Column;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.export.Exporter;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author Administrador
 */
@ManagedBean
@SessionScoped
public class ControlUsuarios implements Serializable {

    @EJB
    AdministrarUsuariosInterface administrarUsuario;
    @EJB
    AdministrarRastrosInterface administrarRastros;
    @EJB
    AdministarReportesInterface administarReportes;

    private List<Usuarios> listaUsuarios;
    private List<Usuarios> filtrarUsuarios;
    private List<Usuarios> listaUsuariosCrear;
    private String mensajeValidacion;
    private String mensajeV;
    private String mensajeContra;
    private List<Usuarios> listaUsuariosModificar;
    private List<Usuarios> listaUsuariosBorrar;
    //LISTA DE VALORES DE PERSONAS!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    private List<Personas> lovPersonas;
    private List<Personas> lovFiltradoPersonas;
    private Personas personasSeleccionado;
    //LISTA DE VALORES DE PERFILES!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    private List<Perfiles> lovPerfiles;
    private List<Perfiles> lovFiltradoPerfiles;
    private Perfiles perfilesSeleccionado;
    //LISTA DE VALORES DE PANTALLAS!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    private List<Pantallas> lovPantallas;
    private List<Pantallas> lovFiltradoPantallas;
    private Pantallas pantallasSeleccionado;
    //NUEVO, DUPLICADO EDITAR Y SELECCIONADA
    private Usuarios nuevaUsuarios;
    private Usuarios duplicarUsuarios;
    private Usuarios eliminarUsuarios;
    private Usuarios clonarUsuarios;
    private Usuarios editarUsuarios;
    private Usuarios usuariosSeleccionado;
    public String altoTabla;
    public String infoRegistroPersonas, infoRegistroPerfiles, infoRegistroPantallas;
    //CLON
    private List<Usuarios> lovUsuarioAlias;
    private List<Usuarios> lovfiltrarUsuarioAlias;
    private Usuarios usuarioAliasSeleccionado;
    private Usuarios auxClon;
    //AutoCompletar
    private boolean permitirIndex;
    //Tabla a Imprimir
    private String tablaImprimir, nombreArchivo;
    private Column usuarioPersona, usuarioPerfil, usuarioAlias, usuarioPantallaInicio, usuarioActivo;
    private String infoRegistro, infoRegistroAlias;
    private String infoRegistroCiudadDocumento, infoRegistroCiudadNacimiento, infoRegistroTipoDocumento;
    public boolean buscador;
    private Map<String, Object> mapParametros;
    private String paginaAnterior;
    //otros
    private int cualCelda, tipoLista, tipoActualizacion, k, bandera;
    private BigInteger l;
    private boolean aceptar, guardado, activarLov;
    private Personas nuevaPersona;

    private List<Ciudades> lovCiudades;
    private List<Ciudades> lovCiudadesFiltrar;
    private Ciudades ciudadSeleccionada;

    private List<Ciudades> lovCiudadDocumento;
    private List<Ciudades> lovCiudadDocumentoFiltrar;
    private Ciudades ciudadDocumentoSeleccionada;
    private List<TiposDocumentos> lovTiposDocumentos;
    private List<TiposDocumentos> lovTiposDocumentosFiltrar;
    private TiposDocumentos tipoDocumentoSeleccionado;

    private StreamedContent reporte;
    private String pathReporteGenerado = null;
    private String nombreReporte, tipoReporte;
    private Inforeportes segUsuarios;
    private String cabezeraVisor;
    private boolean estadoReporte;
    private String resultadoReporte;
    private String userAgent;
    private ExternalContext externalContext;

    public ControlUsuarios() {

        permitirIndex = true;
        aceptar = true;
        tipoLista = 0;
        listaUsuarios = null;
        listaUsuariosCrear = new ArrayList<Usuarios>();
        listaUsuariosModificar = new ArrayList<Usuarios>();
        listaUsuariosBorrar = new ArrayList<Usuarios>();
        lovPersonas = null;
        lovPerfiles = null;
        lovPantallas = null;
        lovUsuarioAlias = null;

        cualCelda = -1;
        tipoLista = 0;
        nuevaUsuarios = new Usuarios();
        nuevaUsuarios.setPersona(new Personas());
        nuevaUsuarios.setPerfil(new Perfiles());
        nuevaUsuarios.setPantallainicio(new Pantallas());
        duplicarUsuarios = new Usuarios();
        duplicarUsuarios.setPersona(new Personas());
        duplicarUsuarios.setPerfil(new Perfiles());
        duplicarUsuarios.setPantallainicio(new Pantallas());
        eliminarUsuarios = new Usuarios();
        clonarUsuarios = new Usuarios();
        usuariosSeleccionado = null;
        k = 0;
        auxClon = new Usuarios();
        altoTabla = "315";
        guardado = true;
        buscador = false;
        tablaImprimir = ":formExportar:datosUsuariosExportar";
        nombreArchivo = "UsuariosXML";
        paginaAnterior = "nominaf";
        mapParametros = new LinkedHashMap<String, Object>();
        mapParametros.put("paginaAnterior", paginaAnterior);
        activarLov = true;
        nuevaPersona = new Personas();
        nombreReporte = "segusuarios";
        tipoReporte = "PDF";
        estadoReporte = false;

    }

    public void limpiarListasValor() {

    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarUsuario.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
            administarReportes.obtenerConexion(ses.getId());
            externalContext = x.getExternalContext();
            userAgent = externalContext.getRequestHeaderMap().get("User-Agent");
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void recibirPaginaEntrante(String pagina) {
        paginaAnterior = pagina;
        listaUsuarios = null;
        getListaUsuarios();
        if (listaUsuarios != null) {
            if (!listaUsuarios.isEmpty()) {
                usuariosSeleccionado = listaUsuarios.get(0);
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

    //ACTIVAR F11
    public void activarCtrlF11() {
        System.out.println("TipoLista= " + tipoLista);
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            usuarioPersona = (Column) c.getViewRoot().findComponent("form:datosUsuarios:persona");
            usuarioPersona.setFilterStyle("width: 85% !important");
            usuarioPerfil = (Column) c.getViewRoot().findComponent("form:datosUsuarios:perfil");
            usuarioPerfil.setFilterStyle("width: 85% !important");
            usuarioAlias = (Column) c.getViewRoot().findComponent("form:datosUsuarios:alias");
            usuarioAlias.setFilterStyle("width: 85% !important");
            usuarioPantallaInicio = (Column) c.getViewRoot().findComponent("form:datosUsuarios:pantalla");
            usuarioPantallaInicio.setFilterStyle("width: 85% !important");
            altoTabla = "295";
            RequestContext.getCurrentInstance().update("form:datosUsuarios");
            bandera = 1;
            tipoLista = 1;
        } else if (bandera == 1) {
            usuarioPersona = (Column) c.getViewRoot().findComponent("form:datosUsuarios:persona");
            usuarioPersona.setFilterStyle("display: none; visibility: hidden;");
            usuarioPerfil = (Column) c.getViewRoot().findComponent("form:datosUsuarios:perfil");
            usuarioPerfil.setFilterStyle("display: none; visibility: hidden;");
            usuarioAlias = (Column) c.getViewRoot().findComponent("form:datosUsuarios:alias");
            usuarioAlias.setFilterStyle("display: none; visibility: hidden;");
            usuarioPantallaInicio = (Column) c.getViewRoot().findComponent("form:datosUsuarios:pantalla");
            usuarioPantallaInicio.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosUsuarios");
            altoTabla = "315";
            bandera = 0;
            filtrarUsuarios = null;
            tipoLista = 0;
        }
    }

    public void eventofiltrar() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
        contarRegistros();
    }

    public void cambiarIndice(Usuarios usuario, int celda) {
        usuariosSeleccionado = usuario;
        cualCelda = celda;
        tablaImprimir = ":formExportar:datosUsuariosExportar";
        nombreArchivo = "UsuariosXML";
        usuariosSeleccionado.getSecuencia();
        if (cualCelda == 0) {
            habilitarBotonLov();
            usuariosSeleccionado.getPersona().getNombreCompleto();
        } else if (cualCelda == 1) {
            habilitarBotonLov();
            usuariosSeleccionado.getPerfil().getDescripcion();
        } else if (cualCelda == 2) {
            deshabilitarBotonLov();
            usuariosSeleccionado.getAlias();
        } else if (cualCelda == 3) {
            habilitarBotonLov();
            usuariosSeleccionado.getPantallainicio().getNombre();
        }
    }

    public void editarCelda() {
        if (usuariosSeleccionado != null) {
            editarUsuarios = usuariosSeleccionado;
            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarPersona");
                RequestContext.getCurrentInstance().execute("PF('editarPersona').show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarPerfil");
                RequestContext.getCurrentInstance().execute("PF('editarPerfil').show()");
                cualCelda = -1;
            } else if (cualCelda == 2) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarAlias");
                RequestContext.getCurrentInstance().execute("PF('editarAlias').show()");
                cualCelda = -1;
            } else if (cualCelda == 3) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarPantalla");
                RequestContext.getCurrentInstance().execute("PF('editarPantalla').show()");
                cualCelda = -1;
            }
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void asignarIndex(Usuarios usuario, int dlg, int LND) {
        usuariosSeleccionado = usuario;
        tipoActualizacion = LND;
        if (dlg == 0) {
            lovPersonas = null;
            contarRegistrosPersonas();
            RequestContext.getCurrentInstance().update("formularioDialogos:personasDialogo");
            RequestContext.getCurrentInstance().execute("PF('personasDialogo').show()");
        } else if (dlg == 1) {
            lovPerfiles = null;
            contarRegistrosPerfiles();
            RequestContext.getCurrentInstance().update("formularioDialogos:perfilesDialogo");
            RequestContext.getCurrentInstance().execute("PF('perfilesDialogo').show()");
        } else if (dlg == 2) {
            lovPantallas = null;
            contarRegistrosPantallas();
            RequestContext.getCurrentInstance().update("formularioDialogos:pantallasDialogo");
            RequestContext.getCurrentInstance().execute("PF('pantallasDialogo').show()");
        } else if (dlg == 3) {
            lovTiposDocumentos = null;
            cargarLovTiposDocumentos();
            contarRegistrosTipoDocumento();
            RequestContext.getCurrentInstance().update("formularioDialogos:tipoDocumentoDialogo");
            RequestContext.getCurrentInstance().execute("PF('tipoDocumentoDialogo').show()");
        } else if (dlg == 4) {
            lovCiudadDocumento = null;
            cargarLovCiudadesDocumento();
            contarRegistroCiudades();
            RequestContext.getCurrentInstance().update("formularioDialogos:ciudadDocumentoDialogo");
            RequestContext.getCurrentInstance().execute("PF('ciudadDocumentoDialogo').show()");
        } else if (dlg == 5) {
            lovCiudades = null;
            cargarLovCiudades();
            contarRegistroCiudadNacimiento();
            RequestContext.getCurrentInstance().update("formularioDialogos:ciudadNacimientoDialogo");
            RequestContext.getCurrentInstance().execute("PF('ciudadNacimientoDialogo').show()");
        }
    }

    public void actualizarPersonas() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            usuariosSeleccionado.setPersona(personasSeleccionado);
            if (!listaUsuariosCrear.contains(usuariosSeleccionado)) {
                if (listaUsuariosModificar.isEmpty()) {
                    listaUsuariosModificar.add(usuariosSeleccionado);
                } else if (!listaUsuariosModificar.contains(usuariosSeleccionado)) {
                    listaUsuariosModificar.add(usuariosSeleccionado);
                }
            }
            guardado = false;
//            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            permitirIndex = true;
            RequestContext.getCurrentInstance().update("form:datosUsuarios");
        } else if (tipoActualizacion == 1) {
            nuevaUsuarios.setPersona(personasSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaUsuario");
        } else if (tipoActualizacion == 2) {
            duplicarUsuarios.setPersona(personasSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarUsuario");
        }
        lovFiltradoPersonas = null;
        personasSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;

        RequestContext.getCurrentInstance().update("formularioDialogos:personasDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:LOVPersonas");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarPS");
        context.reset("formularioDialogos:LOVPersonas:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVPersonas').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('personasDialogo').hide()");
    }

    public void cancelarCambioPersona() {
        lovFiltradoPersonas = null;
        personasSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("formularioDialogos:personasDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:LOVPersonas");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarPS");
        context.reset("formularioDialogos:LOVPersonas:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVPersonas').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('personasDialogo').hide()");
    }

    public void actualizarPerfiles() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            usuariosSeleccionado.setPerfil(perfilesSeleccionado);
            if (!listaUsuariosCrear.contains(usuariosSeleccionado)) {
                if (listaUsuariosModificar.isEmpty()) {
                    listaUsuariosModificar.add(usuariosSeleccionado);
                } else if (!listaUsuariosModificar.contains(usuariosSeleccionado)) {
                    listaUsuariosModificar.add(usuariosSeleccionado);
                }
            }
            guardado = false;
//            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            permitirIndex = true;
            RequestContext.getCurrentInstance().update("form:datosUsuarios");
        } else if (tipoActualizacion == 1) {
            nuevaUsuarios.setPerfil(perfilesSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaUsuario");
        } else if (tipoActualizacion == 2) {
            duplicarUsuarios.setPerfil(perfilesSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarUsuario");
        }
        lovFiltradoPerfiles = null;
        perfilesSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        RequestContext.getCurrentInstance().update("formularioDialogos:perfilesDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:LOVPerfiles");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarPF");
        context.reset("formularioDialogos:LOVPerfiles:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVPerfiles').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('perfilesDialogo').hide()");
    }

    public void lovUsuarios() {
        lovUsuarioAlias = null;
        getLovUsuarioAlias();
        contarRegistrosAlias();
        RequestContext.getCurrentInstance().update("formularioDialogos:aliasDialogo");
        RequestContext.getCurrentInstance().execute("PF('aliasDialogo').show()");
    }

    public void cancelarCambioAlias() {
        lovFiltradoPantallas = null;
        lovfiltrarUsuarioAlias = null;
        usuarioAliasSeleccionado = null;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("formularioDialogos:aliasDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:LOVUsuariosAlias");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarAU");
        context.reset("formularioDialogos:LOVUsuariosAlias:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVUsuariosAlias').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('aliasDialogo').hide()");
    }

    public void cancelarCambioPerfil() {
        lovFiltradoPerfiles = null;
        perfilesSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("formularioDialogos:perfilesDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:LOVPerfiles");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarPF");
        context.reset("formularioDialogos:LOVPerfiles:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVPerfiles').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('perfilesDialogo').hide()");
    }

    //MOSTRAR L.O.V PANTALLAS
    public void actualizarPantallas() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            usuariosSeleccionado.setPantallainicio(pantallasSeleccionado);
            if (!listaUsuariosCrear.contains(usuariosSeleccionado)) {
                if (listaUsuariosModificar.isEmpty()) {
                    listaUsuariosModificar.add(usuariosSeleccionado);
                } else if (!listaUsuariosModificar.contains(usuariosSeleccionado)) {
                    listaUsuariosModificar.add(usuariosSeleccionado);
                }
            }
            guardado = false;
//            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            permitirIndex = true;
            RequestContext.getCurrentInstance().update("form:datosUsuarios");
        } else if (tipoActualizacion == 1) {
            nuevaUsuarios.setPantallainicio(pantallasSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaUsuario");
        } else if (tipoActualizacion == 2) {
            duplicarUsuarios.setPantallainicio(pantallasSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarUsuario");
        }
        lovFiltradoPantallas = null;
        pantallasSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        RequestContext.getCurrentInstance().update("formularioDialogos:pantallasDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:LOVPantallas");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarPT");
        context.reset("formularioDialogos:LOVPantallas:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVPantallas').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('pantallasDialogo').hide()");
    }

    public void cancelarCambioPantalla() {
        lovFiltradoPantallas = null;
        pantallasSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("formularioDialogos:pantallasDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:LOVPantallas");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarPT");
        context.reset("formularioDialogos:LOVPantallas:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVPantallas').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('pantallasDialogo').hide()");
    }

    public void listaValoresBoton() {
        if (usuariosSeleccionado != null) {
            RequestContext context = RequestContext.getCurrentInstance();
            if (cualCelda == 0) {
                lovPersonas = null;
                getLovPersonas();
                RequestContext.getCurrentInstance().update("formularioDialogos:personasDialogo");
                RequestContext.getCurrentInstance().execute("PF('personasDialogo').show()");
                tipoActualizacion = 0;
            } else if (cualCelda == 1) {
                lovPerfiles = null;
                getLovPerfiles();
                RequestContext.getCurrentInstance().update("formularioDialogos:perfilesDialogo");
                RequestContext.getCurrentInstance().execute("PF('perfilesDialogo').show()");
                tipoActualizacion = 0;
            } else if (cualCelda == 3) {
                lovPantallas = null;
                getLovPantallas();
                RequestContext.getCurrentInstance().update("formularioDialogos:pantallasDialogo");
                RequestContext.getCurrentInstance().execute("PF('pantallasDialogo').show()");
                tipoActualizacion = 0;
            }
        }
    }

    public void modificarUsuarios(Usuarios usuario) {
        usuariosSeleccionado = usuario;
        if (!listaUsuariosCrear.contains(usuariosSeleccionado)) {
            if (listaUsuariosModificar.isEmpty()) {
                listaUsuariosModificar.add(usuariosSeleccionado);
            } else if (!listaUsuariosModificar.contains(usuariosSeleccionado)) {
                listaUsuariosModificar.add(usuariosSeleccionado);
            }
            guardado = false;
//            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
        RequestContext.getCurrentInstance().update("form:datosUsuarios");
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosUsuariosExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "UsuariosPDF", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosUsuariosExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "UsuariosXLS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void limpiarNuevaUsuario() {
        nuevaUsuarios = new Usuarios();
        nuevaUsuarios.setPersona(new Personas());
        nuevaUsuarios.getPersona().setNombreCompleto(" ");
        nuevaUsuarios.setPerfil(new Perfiles());
        nuevaUsuarios.getPerfil().setDescripcion(" ");
        nuevaUsuarios.setPantallainicio(new Pantallas());
        nuevaUsuarios.getPantallainicio().setNombre(" ");
    }

    public void limpiarDuplicarUsuario() {
        duplicarUsuarios = new Usuarios();
        duplicarUsuarios.setPersona(new Personas());
        duplicarUsuarios.getPersona().setNombreCompleto(" ");
        duplicarUsuarios.setPerfil(new Perfiles());
        duplicarUsuarios.getPerfil().setDescripcion(" ");
        duplicarUsuarios.setPantallainicio(new Pantallas());
        duplicarUsuarios.getPantallainicio().setNombre(" ");
    }

    public void guardarYSalir() {
        guardarCambiosUsuario();
        salir();
    }

    public void guardarCambiosUsuario() {
        try {
            if (guardado == false) {
                if (!listaUsuariosBorrar.isEmpty()) {
                    administrarUsuario.borrarUsuarios(listaUsuariosBorrar);
                    listaUsuariosBorrar.clear();
                }
                if (!listaUsuariosCrear.isEmpty()) {
                    administrarUsuario.crearUsuarios(listaUsuariosCrear);
                    listaUsuariosCrear.clear();
                }
                if (!listaUsuariosModificar.isEmpty()) {
                    administrarUsuario.modificarUsuarios(listaUsuariosModificar);
                    listaUsuariosModificar.clear();
                }
                listaUsuarios = null;
                getListaUsuarios();
                contarRegistros();
                RequestContext.getCurrentInstance().update("form:datosUsuarios");
                guardado = true;
                permitirIndex = true;
                FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                RequestContext.getCurrentInstance().update("form:growl");
//                RequestContext.getCurrentInstance().update("form:ACEPTAR");
                k = 0;
                usuariosSeleccionado = null;
            }
        } catch (Exception e) {
            FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado, intente nuevamente.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }
    }

    public void borrarUsuario() {
        if (usuariosSeleccionado != null) {
            if (!listaUsuariosModificar.isEmpty() && listaUsuariosModificar.contains(usuariosSeleccionado)) {
                int modIndex = listaUsuariosModificar.indexOf(usuariosSeleccionado);
                listaUsuariosModificar.remove(modIndex);
                listaUsuariosBorrar.add(usuariosSeleccionado);
            } else if (!listaUsuariosCrear.isEmpty() && listaUsuariosCrear.contains(usuariosSeleccionado)) {
                int crearIndex = listaUsuariosCrear.indexOf(usuariosSeleccionado);
                listaUsuariosCrear.remove(crearIndex);
            } else {
                listaUsuariosBorrar.add(usuariosSeleccionado);
            }
            listaUsuarios.remove(usuariosSeleccionado);

            if (tipoLista == 1) {
                filtrarUsuarios.remove(usuariosSeleccionado);
            }
            usuariosSeleccionado = null;
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:datosUsuarios");
            guardado = false;
//            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
    }

    public void agregarNuevaUsuario() {
        RequestContext context = RequestContext.getCurrentInstance();
        int pasa = 0;
        int pasas = 0;
        mensajeValidacion = " ";
        if (nuevaUsuarios.getAlias() == null) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            pasa++;
        }
        if (nuevaUsuarios.getPersona().getNombreCompleto() == null || nuevaUsuarios.getPersona().getNombreCompleto().equals("")) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            pasa++;
        }
        if (nuevaUsuarios.getPerfil().getDescripcion() == null || nuevaUsuarios.getPerfil().getDescripcion().equals("")) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            pasa++;
        }
        if (nuevaUsuarios.getPantallainicio().getNombre() == null || nuevaUsuarios.getPantallainicio().getNombre().equals("")) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            pasa++;
        }
        if (nuevaUsuarios.getPersona().getNombreCompleto() != null) {
            for (int i = 0; i < listaUsuarios.size(); i++) {
                if (nuevaUsuarios.getPersona().getNombreCompleto().equals(listaUsuarios.get(i).getPersona().getNombreCompleto())) {
                    pasas++;
                    RequestContext.getCurrentInstance().update("formularioDialogos:validacionPersona");
                    RequestContext.getCurrentInstance().execute("PF('validacionPersona').show()");
                }
            }
        }
        if (pasa == 0 && pasas == 0) {
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                usuarioPersona = (Column) c.getViewRoot().findComponent("form:datosUsuarios:persona");
                usuarioPersona.setFilterStyle("display: none; visibility: hidden;");
                usuarioPerfil = (Column) c.getViewRoot().findComponent("form:datosUsuarios:perfil");
                usuarioPerfil.setFilterStyle("display: none; visibility: hidden;");
                usuarioAlias = (Column) c.getViewRoot().findComponent("form:datosUsuarios:alias");
                usuarioAlias.setFilterStyle("display: none; visibility: hidden;");
                usuarioPantallaInicio = (Column) c.getViewRoot().findComponent("form:datosUsuarios:pantallainicio");
                usuarioPantallaInicio.setFilterStyle("display: none; visibility: hidden;");
                altoTabla = "315";
                RequestContext.getCurrentInstance().update("form:datosUsuarios");
                bandera = 0;
                filtrarUsuarios = null;
                tipoLista = 0;
            }
            k++;
            l = BigInteger.valueOf(k);
            nuevaUsuarios.setSecuencia(l);
            if (nuevaUsuarios.isEstadoActivo() == true) {
                nuevaUsuarios.setActivo("S");
            } else if (nuevaUsuarios.isEstadoActivo() == false) {
                nuevaUsuarios.setActivo("N");
            }
            listaUsuariosCrear.add(nuevaUsuarios);
            listaUsuarios.add(nuevaUsuarios);
            usuariosSeleccionado = nuevaUsuarios;
            contarRegistros();
            nuevaUsuarios = new Usuarios();
            RequestContext.getCurrentInstance().update("form:datosUsuarios");
            guardado = false;
//            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroUsuario");
            RequestContext.getCurrentInstance().execute("PF('NuevoRegistroUsuario').hide()");
        } else {
            RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevaUsuario");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevaUsuario').show()");
        }
    }

    public void duplicarUsuario() {
        if (usuariosSeleccionado != null) {
            duplicarUsuarios = new Usuarios();
            duplicarUsuarios.setPersona(usuariosSeleccionado.getPersona());
            duplicarUsuarios.setPerfil(usuariosSeleccionado.getPerfil());
            duplicarUsuarios.setAlias(usuariosSeleccionado.getAlias());
            duplicarUsuarios.setPantallainicio(usuariosSeleccionado.getPantallainicio());
            duplicarUsuarios.setActivo(usuariosSeleccionado.getActivo());
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarUsuario");
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroUsuario').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void confirmarDuplicar() {
        int pasa = 0;
        int pasas = 0;
        k++;
        l = BigInteger.valueOf(k);
        duplicarUsuarios.setSecuencia(l);
        if (duplicarUsuarios.getPersona().getNombreCompleto() != null) {
            for (int i = 0; i < listaUsuarios.size(); i++) {
                if (duplicarUsuarios.getPersona().getNombreCompleto() != null) {
                    if (duplicarUsuarios.getPersona().getNombreCompleto().equals(listaUsuarios.get(i).getPersona().getNombreCompleto())) {
                        pasas++;
                        RequestContext.getCurrentInstance().update("formularioDialogos:validacionPersona");
                        RequestContext.getCurrentInstance().execute("PF('validacionPersona').show()");
                    }
                }
            }
        }
        if (duplicarUsuarios.getAlias() == null) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            pasa++;
        }
        if (duplicarUsuarios.getPersona().getNombreCompleto() == null || duplicarUsuarios.getPersona().getNombreCompleto().equals("")) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            pasa++;
        }
        if (duplicarUsuarios.getPerfil().getDescripcion() == null || duplicarUsuarios.getPerfil().getDescripcion().equals("")) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            pasa++;
        }
        if (duplicarUsuarios.getPantallainicio().getNombre() == null || duplicarUsuarios.getPantallainicio().getNombre().equals("")) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            pasa++;
        }

        if (pasa == 0 && pasas == 1) {
            guardado = false;
//            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                System.out.println("Desactivar");
                usuarioPersona = (Column) c.getViewRoot().findComponent("form:datosUsuarios:persona");
                usuarioPersona.setFilterStyle("display: none; visibility: hidden;");
                usuarioPerfil = (Column) c.getViewRoot().findComponent("form:datosUsuarios:perfil");
                usuarioPerfil.setFilterStyle("display: none; visibility: hidden;");
                usuarioAlias = (Column) c.getViewRoot().findComponent("form:datosUsuarios:alias");
                usuarioAlias.setFilterStyle("display: none; visibility: hidden;");
                usuarioPantallaInicio = (Column) c.getViewRoot().findComponent("form:datosUsuarios:pantallainicio");
                usuarioPantallaInicio.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosUsuarios");
                altoTabla = "315";
                bandera = 0;
                filtrarUsuarios = null;
                System.out.println("TipoLista= " + tipoLista);
                tipoLista = 0;
            }
            listaUsuarios.add(duplicarUsuarios);
            listaUsuariosCrear.add(duplicarUsuarios);
            usuariosSeleccionado = duplicarUsuarios;
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:datosUsuarios");
            duplicarUsuarios = new Usuarios();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarUsuario");
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroUsuario').hide()");
        } else {
            RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevaUsuario");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevaUsuario').show()");
        }
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (usuariosSeleccionado != null) {
            int resultado = administrarRastros.obtenerTabla(usuariosSeleccionado.getSecuencia(), "USUARIOS");
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
        } else if (administrarRastros.verificarHistoricosTabla("USUARIOS")) {
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
    }

    public void cancelarModificacion() {
        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            usuarioPersona = (Column) c.getViewRoot().findComponent("form:datosUsuarios:persona");
            usuarioPersona.setFilterStyle("display: none; visibility: hidden;");
            usuarioPerfil = (Column) c.getViewRoot().findComponent("form:datosUsuarios:perfil");
            usuarioPerfil.setFilterStyle("display: none; visibility: hidden;");
            usuarioAlias = (Column) c.getViewRoot().findComponent("form:datosUsuarios:alias");
            usuarioAlias.setFilterStyle("display: none; visibility: hidden;");
            usuarioPantallaInicio = (Column) c.getViewRoot().findComponent("form:datosUsuarios:pantallainicio");
            usuarioPantallaInicio.setFilterStyle("display: none; visibility: hidden;");
            usuarioActivo = (Column) c.getViewRoot().findComponent("form:datosUsuarios:activo");
            usuarioActivo.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosUsuarios");
            altoTabla = "315";
            bandera = 0;
            filtrarUsuarios = null;
            tipoLista = 0;
        }
        listaUsuariosBorrar.clear();
        listaUsuariosCrear.clear();
        listaUsuariosModificar.clear();
        usuariosSeleccionado = null;
        k = 0;
        listaUsuarios = null;
        lovUsuarioAlias = null;
        auxClon = new Usuarios();
        getListaUsuarios();
        contarRegistros();
        guardado = true;
        permitirIndex = true;
        RequestContext.getCurrentInstance().update("form:datosUsuarios");
        RequestContext.getCurrentInstance().update("form:aliasNombreClon");
    }

    public void salir() {
        limpiarListasValor();
        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            usuarioPersona = (Column) c.getViewRoot().findComponent("form:datosUsuarios:persona");
            usuarioPersona.setFilterStyle("display: none; visibility: hidden;");
            usuarioPerfil = (Column) c.getViewRoot().findComponent("form:datosUsuarios:perfil");
            usuarioPerfil.setFilterStyle("display: none; visibility: hidden;");
            usuarioAlias = (Column) c.getViewRoot().findComponent("form:datosUsuarios:alias");
            usuarioAlias.setFilterStyle("display: none; visibility: hidden;");
            usuarioPantallaInicio = (Column) c.getViewRoot().findComponent("form:datosUsuarios:pantallainicio");
            usuarioPantallaInicio.setFilterStyle("display: none; visibility: hidden;");
            usuarioActivo = (Column) c.getViewRoot().findComponent("form:datosUsuarios:activo");
            usuarioActivo.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosUsuarios");
            altoTabla = "315";
            bandera = 0;
            filtrarUsuarios = null;
            tipoLista = 0;
        }
        listaUsuariosBorrar.clear();
        listaUsuariosCrear.clear();
        listaUsuariosModificar.clear();
        usuariosSeleccionado = null;
        k = 0;
        listaUsuarios = null;
        auxClon = new Usuarios();
        guardado = true;
        permitirIndex = true;
        RequestContext.getCurrentInstance().update("form:datosUsuarios");
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
        RequestContext.getCurrentInstance().update("form:aliasNombreClon");
        navegar("atras");
    }

    public void crearUsuario() {
        try {
            if (usuariosSeleccionado != null) {
                administrarUsuario.crearUsuariosBD(usuariosSeleccionado.getAlias());
                administrarUsuario.CrearUsuarioPerfilBD(usuariosSeleccionado.getAlias(), usuariosSeleccionado.getPerfil().getDescripcion());
                RequestContext.getCurrentInstance().execute("PF('operacionEnProceso').hide()");
                RequestContext.getCurrentInstance().execute("PF('usuarioCreado').show()");
            }
        } catch (PersistenceException e) {
            System.out.println("Controlador.ControlUsuarios.crearUsuario()" + e.getCause());
            System.out.println("Controlador.ControlUsuarios.crearUsuario()" + e.getMessage());
            RequestContext.getCurrentInstance().execute("PF('errorCrearUsuario').show()");
        }
    }

    public void eliminarUsuarioValidacion() {
        if (usuariosSeleccionado != null) {
            eliminarUsuarios.setPersona(usuariosSeleccionado.getPersona());
            eliminarUsuarios.setPerfil(usuariosSeleccionado.getPerfil());
            eliminarUsuarios.setAlias(usuariosSeleccionado.getAlias());
            eliminarUsuarios.setPantallainicio(usuariosSeleccionado.getPantallainicio());
            eliminarUsuarios.setActivo(usuariosSeleccionado.getActivo());
            mensajeV = usuariosSeleccionado.getAlias();
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:validacionEliminar");
            RequestContext.getCurrentInstance().execute("PF('validacionEliminar').show()");
        }
    }

    public void eliminarUsuarioBD() {
        try {
            administrarUsuario.eliminarUsuariosBD(eliminarUsuarios.getAlias());
            RequestContext.getCurrentInstance().execute("PF('operacionEnProceso').hide()");
            RequestContext.getCurrentInstance().execute("PF('usuarioEliminado').show()");
        } catch (PersistenceException pe) {
            System.out.println("Controlador.ControlUsuarios.eliminarUsuarioBD()" + pe.getCause());
            System.out.println("Controlador.ControlUsuarios.eliminarUsuarioBD()" + pe.getMessage());
            RequestContext.getCurrentInstance().execute("PF('errorEliminarUsuario').show()");
        }
    }

    public void asignarAliasClon() {
        auxClon = usuarioAliasSeleccionado;
        usuarioAliasSeleccionado = null;
        lovfiltrarUsuarioAlias = null;
        RequestContext.getCurrentInstance().update("form:aliasNombreClon");
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("formularioDialogos:aliasDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:LOVUsuariosAlias");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarAU");
        context.reset("formularioDialogos:LOVUsuariosAlias:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVUsuariosAlias').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('aliasDialogo').hide()");
    }

    public void usuarioClonarBD() {
        try {
            if (auxClon.getAlias().equals("")) {
                RequestContext.getCurrentInstance().update("formularioDialogos:validacionClon");
                RequestContext.getCurrentInstance().execute("PF('validacionClon').show()");
            } else if (!auxClon.getAlias().equals("")) {
                if (usuariosSeleccionado != null) {
                    administrarUsuario.clonarUsuariosBD(usuariosSeleccionado.getSecuencia(), auxClon.getSecuencia());
                }
                RequestContext.getCurrentInstance().execute("PF('usuarioClonado').show()");
            }
        } catch (Exception e) {
            System.out.println("Controlador.ControlUsuarios.usuarioClonarBD()" + e.getCause());
            System.out.println("Controlador.ControlUsuarios.usuarioClonarBD()" + e.getMessage());
            RequestContext.getCurrentInstance().execute("PF('errorClonarUsuario').show()");
        }
    }

    public void desbloquearUsuario() {
        try {
            administrarUsuario.desbloquearUsuariosBD(usuariosSeleccionado.getAlias());
            RequestContext.getCurrentInstance().execute("PF('usuarioDesbloqueado').show()");
        } catch (Exception e) {
            System.out.println("Controlador.ControlUsuarios.desbloquearUsuario()" + e.getMessage());
            RequestContext.getCurrentInstance().execute("PF('errorDesbloquearUsuario').show()");
        }
    }

    public void resetearUsuario() {
        String fecha = "";
        String ayuda = "";
        Calendar cal = Calendar.getInstance();
        if (cal.get(cal.MONTH) == 0) {
            ayuda = "01";
        }
        if (cal.get(cal.MONTH) == 1) {
            ayuda = "02";
        }
        if (cal.get(cal.MONTH) == 2) {
            ayuda = "03";
        }
        if (cal.get(cal.MONTH) == 3) {
            ayuda = "04";
        }
        if (cal.get(cal.MONTH) == 4) {
            ayuda = "05";
        }
        if (cal.get(cal.MONTH) == 5) {
            ayuda = "06";
        }
        if (cal.get(cal.MONTH) == 6) {
            ayuda = "07";
        }
        if (cal.get(cal.MONTH) == 7) {
            ayuda = "08";
        }
        if (cal.get(cal.MONTH) == 8) {
            ayuda = "09";
        }
        if (cal.get(cal.MONTH) == 9) {
            ayuda = "10";
        }
        if (cal.get(cal.MONTH) == 10) {
            ayuda = "11";
        }
        if (cal.get(cal.MONTH) == 11) {
            ayuda = "12";
        }
        if (cal.get(cal.DATE) < 10) {
            fecha = "0" + cal.get(cal.DATE) + ayuda + cal.get(cal.HOUR_OF_DAY) + cal.get(cal.MINUTE);
        } else if (cal.get(cal.DATE) > 10) {
            fecha = cal.get(cal.DATE) + ayuda + cal.get(cal.HOUR_OF_DAY) + cal.get(cal.MINUTE);
        }
        if (usuariosSeleccionado != null) {
            try {
                administrarUsuario.restaurarUsuariosBD(usuariosSeleccionado.getAlias(), fecha);
                mensajeContra = usuariosSeleccionado.getAlias() + "_" + fecha;
                RequestContext.getCurrentInstance().update("formularioDialogos:contrasenaNueva");
                RequestContext.getCurrentInstance().execute("PF('contrasenaNueva').show()");
            } catch (Exception e) {
                FacesMessage msg = new FacesMessage("Información", "El usuario no pudo ser Restaurado");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                RequestContext.getCurrentInstance().update("form:growl");
            }
        }
    }

    public void actualizarTipoDocumento() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 1) {
            nuevaPersona.setTipodocumento(tipoDocumentoSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoFamiliarP");
        }
        lovTiposDocumentosFiltrar = null;
        tipoDocumentoSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;

        RequestContext.getCurrentInstance().update("formularioDialogos:tipoDocumentoDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovTipoDocumento");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarTD");

        context.reset("formularioDialogos:lovTipoDocumento:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovTipoDocumento').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('tipoDocumentoDialogo').hide()");
    }

    public void cancelarCambioTipoDocumento() {
        lovTiposDocumentosFiltrar = null;
        aceptar = true;
        tipoActualizacion = -1;
        permitirIndex = true;
        tipoDocumentoSeleccionado = null;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("formularioDialogos:tipoDocumentoDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovTipoDocumento");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarTD");

        context.reset("formularioDialogos:lovTipoDocumento:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovTipoDocumento').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('tipoDocumentoDialogo').hide()");
    }

    public void actualizarCiudadDocumento() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 1) {
            nuevaPersona.setCiudaddocumento(ciudadDocumentoSeleccionada);
            RequestContext.getCurrentInstance().update("formularioDialogos:ciudadDocumentoModPersonal");
        }
        lovCiudadDocumentoFiltrar = null;
        ciudadDocumentoSeleccionada = null;
        aceptar = true;
        tipoActualizacion = -1;

        RequestContext.getCurrentInstance().update("formularioDialogos:ciudadDocumentoDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovCiudadDocumento");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarCD");

        context.reset("formularioDialogos:lovCiudadDocumento:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovCiudadDocumento').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('ciudadDocumentoDialogo').hide()");
    }

    public void cancelarCambioCiudadDocumento() {
        lovCiudadDocumentoFiltrar = null;
        aceptar = true;
        tipoActualizacion = -1;
        permitirIndex = true;
        ciudadDocumentoSeleccionada = null;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("formularioDialogos:ciudadDocumentoDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovCiudadDocumento");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarCD");

        context.reset("formularioDialogos:lovCiudadDocumento:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovCiudadDocumento').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('ciudadDocumentoDialogo').hide()");
    }

    public void actualizarCiudadNacimiento() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 1) {
            nuevaPersona.setCiudadnacimiento(ciudadSeleccionada);
            RequestContext.getCurrentInstance().update("formularioDialogos:ciudadNacimientoModPersonal");
        }

        lovCiudadesFiltrar = null;
        ciudadSeleccionada = null;
        aceptar = true;
        tipoActualizacion = -1;

        RequestContext.getCurrentInstance().update("formularioDialogos:ciudadNacimientoDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovCiudadNacimiento");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarCN");

        context.reset("formularioDialogos:lovCiudadDocumento:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovCiudadNacimiento').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('ciudadNacimientoDialogo').hide()");
    }

    public void cancelarCambioCiudadNacimiento() {
        lovCiudadesFiltrar = null;
        aceptar = true;
        tipoActualizacion = -1;
        permitirIndex = true;
        ciudadSeleccionada = null;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("formularioDialogos:ciudadNacimientoDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovCiudadNacimiento");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarCN");

        context.reset("formularioDialogos:lovCiudadDocumento:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovCiudadNacimiento').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('ciudadNacimientoDialogo').hide()");
    }

    public void limpiarPersona() {
        nuevaPersona = new Personas();
        nuevaPersona.setTipodocumento(new TiposDocumentos());
        nuevaPersona.setCiudaddocumento(new Ciudades());
        nuevaPersona.setCiudadnacimiento(new Ciudades());
    }

    public void crearNuevaPersona() {
//        crearPersonas.add(personas);
        try {
            k++;
            l = BigInteger.valueOf(k);
            nuevaPersona.setSecuencia(l);
            administrarUsuario.crearPersona(nuevaPersona);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoFamiliarP");
            RequestContext.getCurrentInstance().execute("PF('nuevoFamiliarPersona').hide()");
            RequestContext.getCurrentInstance().update("formularioDialogos:lovPersonasFamiliares");
            lovPersonas = null;
        } catch (Exception e) {
            System.out.println("error crear persona " + e.getMessage());
        }
    }

    public void contarRegistros() {
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
    }

    public void contarRegistrosPerfiles() {
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroPerfiles");
    }

    public void contarRegistrosPersonas() {
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroPersonas");
    }

    public void contarRegistrosPantallas() {
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroPantallas");
    }

    public void contarRegistrosAlias() {
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroAlias");
    }

    public void habilitarBotonLov() {
        activarLov = false;
        RequestContext.getCurrentInstance().update("form:listaValores");
    }

    public void deshabilitarBotonLov() {
        activarLov = true;
        RequestContext.getCurrentInstance().update("form:listaValores");
    }

    public void cargarLovCiudades() {
        if (lovCiudades == null) {
            lovCiudades = administrarUsuario.lovCiudades();
        }
    }

    public void cargarLovTiposDocumentos() {
        if (lovTiposDocumentos == null) {
            lovTiposDocumentos = administrarUsuario.consultarTiposDocumentos();
        }
    }

    public void cargarLovCiudadesDocumento() {
        if (lovCiudadDocumento == null) {
            lovCiudadDocumento = administrarUsuario.lovCiudades();
        }
    }

    public void contarRegistrosTipoDocumento() {
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroTipoDocumento");
    }

    public void contarRegistroCiudades() {
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroCiudades");
    }

    public void contarRegistroCiudadNacimiento() {
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroCiudadNacimiento");
    }

    public void mostrarDialogoInsertarPersona() {
        RequestContext.getCurrentInstance().update("formularioDialogos:nuevoFamiliarPersona");
        RequestContext.getCurrentInstance().execute("PF('nuevoFamiliarPersona').show()");
    }

    public void mostrarDialogoInsertarUsuario() {
        RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroUsuario");
        RequestContext.getCurrentInstance().execute("PF('NuevoRegistroUsuario').show()");
    }

    public AsynchronousFilllListener listener() {
        System.out.println(this.getClass().getName() + ".listener()");
        return new AsynchronousFilllListener() {
            //RequestContext context = c;

            @Override
            public void reportFinished(JasperPrint jp) {
                System.out.println(this.getClass().getName() + ".listener().reportFinished()");
                try {
                    estadoReporte = true;
                    resultadoReporte = "Exito";
                    //  RequestContext.getCurrentInstance().execute("PF('formularioDialogos:generandoReporte");
//                    generarArchivoReporte(jp);
                } catch (Exception e) {
                    System.out.println("ControlNReporteNomina reportFinished ERROR: " + e.toString());
                }
            }

            @Override
            public void reportCancelled() {
                System.out.println(this.getClass().getName() + ".listener().reportCancelled()");
                estadoReporte = true;
                resultadoReporte = "Cancelación";
            }

            @Override
            public void reportFillError(Throwable e) {
                System.out.println(this.getClass().getName() + ".listener().reportFillError()");
                if (e.getCause() != null) {
                    pathReporteGenerado = "ControlUsuarios reportFillError Error: " + e.toString() + "\n" + e.getCause().toString();
                } else {
                    pathReporteGenerado = "ControlUsuarios reportFillError Error: " + e.toString();
                }
                estadoReporte = true;
                resultadoReporte = "Se estallo";
            }
        };
    }

    public void validarDescargaReporte() {
        try {
            System.out.println(this.getClass().getName() + ".validarDescargaReporte()");
            RequestContext.getCurrentInstance().execute("PF('generandoReporte').show()");
            RequestContext context = RequestContext.getCurrentInstance();
            nombreReporte = "segusuarios";
            tipoReporte = "PDF";
            System.out.println("nombre reporte : " + nombreReporte);
            System.out.println("tipo reporte: " + tipoReporte);
            pathReporteGenerado = null;
            pathReporteGenerado = administarReportes.generarReporteSegUsuarios(nombreReporte, tipoReporte);
            RequestContext.getCurrentInstance().execute("PF('generandoReporte').hide()");
            if (pathReporteGenerado != null && !pathReporteGenerado.startsWith("Error:")) {
                System.out.println("validar descarga reporte - ingreso al if 1");
                if (tipoReporte.equals("PDF")) {
                    System.out.println("validar descarga reporte - ingreso al if 2 else");
                    FileInputStream fis;
                    try {
                        System.out.println("pathReporteGenerado : " + pathReporteGenerado);
                        fis = new FileInputStream(new File(pathReporteGenerado));
                        System.out.println("fis : " + fis);
                        reporte = new DefaultStreamedContent(fis, "application/pdf");
                        System.out.println("reporte despues de esto : " + reporte);
                        if (reporte != null) {
                            System.out.println("userAgent: " + userAgent);
                            System.out.println("validar descarga reporte - ingreso al if 4");
                            if (userAgent.toUpperCase().contains("Mobile".toUpperCase()) || userAgent.toUpperCase().contains("Tablet".toUpperCase()) || userAgent.toUpperCase().contains("Android".toUpperCase())) {
                                context.update("formularioDialogos:descargarReporte");
                                context.execute("PF('descargarReporte').show();");
                            } else {
                                cabezeraVisor = "Reporte - " + nombreReporte;
                                RequestContext.getCurrentInstance().update("formularioDialogos:verReportePDF");
                                RequestContext.getCurrentInstance().execute("PF('verReportePDF').show()");
                            }
                        }
                    } catch (FileNotFoundException ex) {
                        System.out.println("validar descarga reporte - ingreso al catch 1");
                        System.out.println(ex);
                        reporte = null;
                    }
                }
            } else {
                System.out.println("validar descarga reporte - ingreso al if 1 else");
                RequestContext.getCurrentInstance().update("formularioDialogos:errorGenerandoReporte");
                RequestContext.getCurrentInstance().execute("PF('errorGenerandoReporte').show()");
            }
        } catch (Exception e) {
            System.out.println("Error en validar descargar Reporte " + e.toString());
        }
    }

    public void reiniciarStreamedContent() {
        System.out.println(this.getClass().getName() + ".reiniciarStreamedContent()");
        reporte = null;
    }

    public void cancelarReporte() {
        System.out.println(this.getClass().getName() + ".cancelarReporte()");
        administarReportes.cancelarReporte();
    }

     public void exportarReporte() throws IOException {
      try {
          System.out.println("ControlUsuarios.exportarReporte()   path generado : " + pathReporteGenerado);
         if (pathReporteGenerado != null || !pathReporteGenerado.startsWith("Error:")) {
            File reporteF = new File(pathReporteGenerado);
            FacesContext ctx = FacesContext.getCurrentInstance();
            FileInputStream fis = new FileInputStream(reporteF);
            byte[] bytes = new byte[1024];
            int read;
            if (!ctx.getResponseComplete()) {
               String fileName = reporteF.getName();
               HttpServletResponse response = (HttpServletResponse) ctx.getExternalContext().getResponse();
               response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
               ServletOutputStream out = response.getOutputStream();

               while ((read = fis.read(bytes)) != -1) {
                  out.write(bytes, 0, read);
               }
               out.flush();
               out.close();
               ctx.responseComplete();
            }
         } else {
            RequestContext.getCurrentInstance().update("formularioDialogos:errorGenerandoReporte");
            RequestContext.getCurrentInstance().execute("PF('errorGenerandoReporte').show()");
         }
      } catch (Exception e) {
         System.out.println("error en exportarReporte :" + e.getMessage());
      }
   }

    //GETTER AND SETTER
    public List<Usuarios> getListaUsuarios() {
        if (listaUsuarios == null) {
            listaUsuarios = administrarUsuario.consultarUsuarios();
        }

        return listaUsuarios;
    }

    public void setListaUsuarios(List<Usuarios> listaUsuarios) {
        this.listaUsuarios = listaUsuarios;
    }

    public List<Usuarios> getListaUsuariosCrear() {
        return listaUsuariosCrear;
    }

    public void setListaUsuariosCrear(List<Usuarios> listaUsuariosCrear) {
        this.listaUsuariosCrear = listaUsuariosCrear;
    }

    public List<Usuarios> getListaUsuariosModificar() {
        return listaUsuariosModificar;
    }

    public void setListaUsuariosModificar(List<Usuarios> listaUsuariosModificar) {
        this.listaUsuariosModificar = listaUsuariosModificar;
    }

    public List<Usuarios> getListaUsuariosBorrar() {
        return listaUsuariosBorrar;
    }

    public void setListaUsuariosBorrar(List<Usuarios> listaUsuariosBorrar) {
        this.listaUsuariosBorrar = listaUsuariosBorrar;
    }

    public List<Personas> getLovPersonas() {
        if (lovPersonas == null) {
            lovPersonas = administrarUsuario.consultarPersonas();
        }
        return lovPersonas;
    }

    public void setLovPersonas(List<Personas> lovPersonas) {
        this.lovPersonas = lovPersonas;
    }

    public List<Personas> getLovFiltradoPersonas() {
        return lovFiltradoPersonas;
    }

    public void setLovFiltradoPersonas(List<Personas> lovFiltradoPersonas) {
        this.lovFiltradoPersonas = lovFiltradoPersonas;
    }

    public List<Perfiles> getLovPerfiles() {
        if (lovPerfiles == null) {
            lovPerfiles = administrarUsuario.consultarPerfiles();
        }
        return lovPerfiles;
    }

    public void setLovPerfiles(List<Perfiles> lovPerfiles) {
        this.lovPerfiles = lovPerfiles;
    }

    public List<Perfiles> getLovFiltradoPerfiles() {
        return lovFiltradoPerfiles;
    }

    public void setLovFiltradoPerfiles(List<Perfiles> lovFiltradoPerfiles) {
        this.lovFiltradoPerfiles = lovFiltradoPerfiles;
    }

    public List<Pantallas> getLovPantallas() {
        if (lovPantallas == null) {
            lovPantallas = administrarUsuario.consultarPantallas();
        }
        return lovPantallas;
    }

    public void setLovPantallas(List<Pantallas> lovPantallas) {
        this.lovPantallas = lovPantallas;
    }

    public List<Pantallas> getLovFiltradoPantallas() {
        return lovFiltradoPantallas;
    }

    public void setLovFiltradoPantallas(List<Pantallas> lovFiltradoPantallas) {
        this.lovFiltradoPantallas = lovFiltradoPantallas;
    }

    public List<Usuarios> getListaUsuariosClon() {
        return lovUsuarioAlias;
    }

    public void setListaUsuariosClon(List<Usuarios> listaUsuariosClon) {
        this.lovUsuarioAlias = listaUsuariosClon;
    }

    public List<Usuarios> getLovUsuarioAlias() {

        if (lovUsuarioAlias == null) {
            lovUsuarioAlias = administrarUsuario.consultarUsuarios();
        }
        return lovUsuarioAlias;
    }

    public void setLovUsuarioAlias(List<Usuarios> lovUsuarioAlias) {
        this.lovUsuarioAlias = lovUsuarioAlias;
    }

    public List<Usuarios> getLovfiltrarUsuarioAlias() {
        return lovfiltrarUsuarioAlias;
    }

    public void setLovfiltrarUsuarioAlias(List<Usuarios> lovfiltrarUsuarioAlias) {
        this.lovfiltrarUsuarioAlias = lovfiltrarUsuarioAlias;
    }

    public Usuarios getAuxClon() {
        return auxClon;
    }

    public void setAuxClon(Usuarios auxClon) {
        this.auxClon = auxClon;
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

    public List<Usuarios> getFiltrarUsuarios() {
        return filtrarUsuarios;
    }

    public void setFiltrarUsuarios(List<Usuarios> filtrarUsuarios) {
        this.filtrarUsuarios = filtrarUsuarios;
    }

    public Personas getPersonasSeleccionado() {
        return personasSeleccionado;
    }

    public void setPersonasSeleccionado(Personas personasSeleccionado) {
        this.personasSeleccionado = personasSeleccionado;
    }

    public Perfiles getPerfilesSeleccionado() {
        return perfilesSeleccionado;
    }

    public void setPerfilesSeleccionado(Perfiles perfilesSeleccionado) {
        this.perfilesSeleccionado = perfilesSeleccionado;
    }

    public Pantallas getPantallasSeleccionado() {
        return pantallasSeleccionado;
    }

    public void setPantallasSeleccionado(Pantallas pantallasSeleccionado) {
        this.pantallasSeleccionado = pantallasSeleccionado;
    }

    public Usuarios getNuevaUsuarios() {
        return nuevaUsuarios;
    }

    public void setNuevaUsuarios(Usuarios nuevaUsuarios) {
        this.nuevaUsuarios = nuevaUsuarios;
    }

    public Usuarios getDuplicarUsuarios() {
        return duplicarUsuarios;
    }

    public void setDuplicarUsuarios(Usuarios duplicarUsuarios) {
        this.duplicarUsuarios = duplicarUsuarios;
    }

    public Usuarios getEliminarUsuarios() {
        return eliminarUsuarios;
    }

    public void setEliminarUsuarios(Usuarios eliminarUsuarios) {
        this.eliminarUsuarios = eliminarUsuarios;
    }

    public Usuarios getClonarUsuario() {
        return clonarUsuarios;
    }

    public void setClonarUsuario(Usuarios clonarUsuario) {
        this.clonarUsuarios = clonarUsuario;
    }

    public Usuarios getUsuariosSeleccionado() {
        return usuariosSeleccionado;
    }

    public void setUsuariosSeleccionado(Usuarios usuariosSeleccionado) {
        this.usuariosSeleccionado = usuariosSeleccionado;
    }

    public Usuarios getClonarUsuarios() {
        return clonarUsuarios;
    }

    public void setClonarUsuarios(Usuarios clonarUsuarios) {
        this.clonarUsuarios = clonarUsuarios;
    }

    public String getAltoTabla() {
        return altoTabla;
    }

    public void setAltoTabla(String altoTabla) {
        this.altoTabla = altoTabla;
    }

    public String getInfoRegistroPersonas() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:LOVPersonas");
        infoRegistroPersonas = String.valueOf(tabla.getRowCount());
        return infoRegistroPersonas;
    }

    public void setInfoRegistroPersonas(String infoRegistroPersonas) {
        this.infoRegistroPersonas = infoRegistroPersonas;
    }

    public String getInfoRegistroPerfiles() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:LOVPerfiles");
        infoRegistroPerfiles = String.valueOf(tabla.getRowCount());
        return infoRegistroPerfiles;
    }

    public void setInfoRegistroPerfiles(String infoRegistroPerfiles) {
        this.infoRegistroPerfiles = infoRegistroPerfiles;
    }

    public String getInfoRegistroPantallas() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:LOVPantallas");
        infoRegistroPantallas = String.valueOf(tabla.getRowCount());
        return infoRegistroPantallas;
    }

    public void setInfoRegistroPantallas(String infoRegistroPantallas) {
        this.infoRegistroPantallas = infoRegistroPantallas;
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

    public String getInfoRegistro() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosUsuarios");
        infoRegistro = String.valueOf(tabla.getRowCount());
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

    public String getPaginaAnterior() {
        return paginaAnterior;
    }

    public void setPaginaAnterior(String paginaAnterior) {
        this.paginaAnterior = paginaAnterior;
    }

    public String getMensajeValidacion() {
        return mensajeValidacion;
    }

    public void setMensajeValidacion(String mensajeValidacion) {
        this.mensajeValidacion = mensajeValidacion;
    }

    public String getMensajeV() {
        return mensajeV;
    }

    public void setMensajeV(String mensajeV) {
        this.mensajeV = mensajeV;
    }

    public String getMensajeContra() {
        return mensajeContra;
    }

    public void setMensajeContra(String mensajeContra) {
        this.mensajeContra = mensajeContra;
    }

    public Usuarios getEditarUsuarios() {
        return editarUsuarios;
    }

    public void setEditarUsuarios(Usuarios editarUsuarios) {
        this.editarUsuarios = editarUsuarios;
    }

    public String getInfoRegistroAlias() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:LOVUsuariosAlias");
        infoRegistroAlias = String.valueOf(tabla.getRowCount());
        return infoRegistroAlias;
    }

    public void setInfoRegistroAlias(String infoRegistroAlias) {
        this.infoRegistroAlias = infoRegistroAlias;
    }

    public boolean isActivarLov() {
        return activarLov;
    }

    public void setActivarLov(boolean activarLov) {
        this.activarLov = activarLov;
    }

    public Usuarios getUsuarioAliasSeleccionado() {
        return usuarioAliasSeleccionado;
    }

    public void setUsuarioAliasSeleccionado(Usuarios usuarioAliasSeleccionado) {
        this.usuarioAliasSeleccionado = usuarioAliasSeleccionado;
    }

    public Personas getNuevaPersona() {
        return nuevaPersona;
    }

    public void setNuevaPersona(Personas nuevaPersona) {
        this.nuevaPersona = nuevaPersona;
    }

    public List<Ciudades> getLovCiudades() {
        return lovCiudades;
    }

    public void setLovCiudades(List<Ciudades> lovCiudades) {
        this.lovCiudades = lovCiudades;
    }

    public List<Ciudades> getLovCiudadesFiltrar() {
        return lovCiudadesFiltrar;
    }

    public void setLovCiudadesFiltrar(List<Ciudades> lovCiudadesFiltrar) {
        this.lovCiudadesFiltrar = lovCiudadesFiltrar;
    }

    public Ciudades getCiudadSeleccionada() {
        return ciudadSeleccionada;
    }

    public void setCiudadSeleccionada(Ciudades ciudadSeleccionada) {
        this.ciudadSeleccionada = ciudadSeleccionada;
    }

    public List<Ciudades> getLovCiudadDocumento() {
        return lovCiudadDocumento;
    }

    public void setLovCiudadDocumento(List<Ciudades> lovCiudadDocumento) {
        this.lovCiudadDocumento = lovCiudadDocumento;
    }

    public List<Ciudades> getLovCiudadDocumentoFiltrar() {
        return lovCiudadDocumentoFiltrar;
    }

    public void setLovCiudadDocumentoFiltrar(List<Ciudades> lovCiudadDocumentoFiltrar) {
        this.lovCiudadDocumentoFiltrar = lovCiudadDocumentoFiltrar;
    }

    public Ciudades getCiudadDocumentoSeleccionada() {
        return ciudadDocumentoSeleccionada;
    }

    public void setCiudadDocumentoSeleccionada(Ciudades ciudadDocumentoSeleccionada) {
        this.ciudadDocumentoSeleccionada = ciudadDocumentoSeleccionada;
    }

    public String getInfoRegistroCiudadDocumento() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovCiudadDocumento");
        infoRegistroCiudadDocumento = String.valueOf(tabla.getRowCount());
        return infoRegistroCiudadDocumento;
    }

    public void setInfoRegistroCiudadDocumento(String infoRegistroCiudadDocumento) {
        this.infoRegistroCiudadDocumento = infoRegistroCiudadDocumento;
    }

    public String getInfoRegistroCiudadNacimiento() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovCiudadNacimiento");
        infoRegistroCiudadNacimiento = String.valueOf(tabla.getRowCount());
        return infoRegistroCiudadNacimiento;
    }

    public void setInfoRegistroCiudadNacimiento(String infoRegistroCiudadNacimiento) {
        this.infoRegistroCiudadNacimiento = infoRegistroCiudadNacimiento;
    }

    public List<TiposDocumentos> getLovTiposDocumentos() {
        return lovTiposDocumentos;
    }

    public void setLovTiposDocumentos(List<TiposDocumentos> lovTiposDocumentos) {
        this.lovTiposDocumentos = lovTiposDocumentos;
    }

    public List<TiposDocumentos> getLovTiposDocumentosFiltrar() {
        return lovTiposDocumentosFiltrar;
    }

    public void setLovTiposDocumentosFiltrar(List<TiposDocumentos> lovTiposDocumentosFiltrar) {
        this.lovTiposDocumentosFiltrar = lovTiposDocumentosFiltrar;
    }

    public TiposDocumentos getTipoDocumentoSeleccionado() {
        return tipoDocumentoSeleccionado;
    }

    public void setTipoDocumentoSeleccionado(TiposDocumentos tipoDocumentoSeleccionado) {
        this.tipoDocumentoSeleccionado = tipoDocumentoSeleccionado;
    }

    public String getInfoRegistroTipoDocumento() {
        return infoRegistroTipoDocumento;
    }

    public void setInfoRegistroTipoDocumento(String infoRegistroTipoDocumento) {
        this.infoRegistroTipoDocumento = infoRegistroTipoDocumento;
    }

    public StreamedContent getReporte() {
        return reporte;
    }

    public void setReporte(StreamedContent reporte) {
        this.reporte = reporte;
    }

    public String getPathReporteGenerado() {
        return pathReporteGenerado;
    }

    public void setPathReporteGenerado(String pathReporteGenerado) {
        this.pathReporteGenerado = pathReporteGenerado;
    }

    public String getNombreReporte() {
        return nombreReporte;
    }

    public void setNombreReporte(String nombreReporte) {
        this.nombreReporte = nombreReporte;
    }

    public String getCabezeraVisor() {
        return cabezeraVisor;
    }

    public void setCabezeraVisor(String cabezeraVisor) {
        this.cabezeraVisor = cabezeraVisor;
    }

    public boolean isEstadoReporte() {
        return estadoReporte;
    }

    public void setEstadoReporte(boolean estadoReporte) {
        this.estadoReporte = estadoReporte;
    }

    public String getResultadoReporte() {
        return resultadoReporte;
    }

    public void setResultadoReporte(String resultadoReporte) {
        this.resultadoReporte = resultadoReporte;
    }

}
