/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import ControlNavegacion.ControlListaNavegacion;
import Entidades.Inforeportes;
import Entidades.ObjetosBloques;
import Entidades.ObjetosDB;
import Entidades.Perfiles;
import Entidades.PermisosObjetosDB;
import Entidades.PermisosPantallas;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministarReportesInterface;
import InterfaceAdministrar.AdministrarPerfilesInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.fill.AsynchronousFilllListener;
import org.apache.log4j.Logger;
import org.primefaces.component.column.Column;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.export.Exporter;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@Named(value = "controlPerfiles")
@SessionScoped
public class ControlPerfiles implements Serializable {

   private static Logger log = Logger.getLogger(ControlPerfiles.class);

    @EJB
    AdministrarRastrosInterface administrarRastros;
    @EJB
    AdministrarPerfilesInterface administrarPerfiles;
    @EJB
    AdministarReportesInterface administarReportes;

    ///perfiles
    private List<Perfiles> listaPerfiles;
    private List<Perfiles> listaPerfilesFiltrar;
    private List<Perfiles> listaPerfilesCrear;
    private List<Perfiles> listaPerfilesBorrar;
    private List<Perfiles> listaPerfilesModificar;
    private Perfiles perfilSeleccionado;
    private Perfiles nuevoPerfil;
    private Perfiles duplicarPerfil;
    private Perfiles editarPerfil;
    //PermisosPantallas
    private List<PermisosPantallas> listaPermisosPantallas;
    private List<PermisosPantallas> listaPermisosPantallasFiltrar;
    private List<PermisosPantallas> listaPermisosPantallasCrear;
    private List<PermisosPantallas> listaPermisosPantallasBorrar;
    private List<PermisosPantallas> listaPermisosPantallasModificar;
    private PermisosPantallas permisosPantallaSeleccionado;
    private PermisosPantallas nuevoPermisosPantalla;
    private PermisosPantallas duplicarPermisosPantalla;
    private PermisosPantallas editarPermisosPantallas;
    private List<PermisosPantallas> lovPermisosPantallas;
    private List<PermisosPantallas> lovPermisosPantallasFiltrar;
    private PermisosPantallas permisoPantallaLovSeleccionado;
    //PermisosObjetosDb
    private List<PermisosObjetosDB> listaPermisosObjetosDB;
    private List<PermisosObjetosDB> listaPermisosObjetosDBFiltrar;
    private List<PermisosObjetosDB> listaPermisosObjetosDBCrear;
    private List<PermisosObjetosDB> listaPermisosObjetosDBBorrar;
    private List<PermisosObjetosDB> listaPermisosObjetosDBModificar;
    private PermisosObjetosDB permisosObjetosDBSeleccionado;
    private PermisosObjetosDB nuevoPermisosObjetosDB;
    private PermisosObjetosDB duplicarPermisosObjetosDB;
    private PermisosObjetosDB editarPermisosObjetosDB;
    private List<PermisosObjetosDB> lovPermisosObjetosDB;
    private List<PermisosObjetosDB> lovPermisosObjetosDBFiltrar;
    private PermisosObjetosDB permisoLovObjetosDBSeleccionado;
    //Otros
    private int tipoActualizacion, k;
    private int cualTabla;
    private int cualCelda, cualCeldaPantalla, cualCeldaObjetos;
    private int bandera, tipoLista;
    private BigInteger l;
    private boolean aceptar, guardado;
    private String altoTabla, altoTablaPantalla, altoTablaObjeto, mensajeValidacion, tablaImprimir, nombreArchivo;
    private Column codigo, descripcion, contrasena;
    private Column nompantalla, nombloque, nomobjeto, tipo, comentario;
    private Column nomobjetop, tipop, modulo, clasificacion;
    private String infoRegistroPerfil, infoRegistroPantallas, infoRegistroObjetos, infoRegistroAlias;
    private String infoRegistroPantallaLov, infoRegistroObjetoLov, infoRegistroPantallaDialogo, infoRegistroBuscarObjeto;
    private boolean activarLov, activarMtodos, activarMtodos2;
    private String paginaAnterior = "nominaf";
    private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();
    //CLONAR
    private List<Perfiles> lovPerfilAlias;
    private List<Perfiles> lovPerfilAliasFiltrar;
    private Perfiles perfilAliasSeleccionado;
    private Perfiles auxclon;

    private boolean estadoReporte;
    private String resultadoReporte;
    private StreamedContent reporte;
    private String pathReporteGenerado = null;
    private String nombreReporte, tipoReporte;
    private String cabezeraVisor;
    private String userAgent;
    private ExternalContext externalContext;

    /**
     * Creates a new instance of ControlPerfiles
     */
    public ControlPerfiles() {
        altoTabla = "56";
        altoTablaObjeto = "50";
        altoTablaPantalla = "50";
        perfilSeleccionado = null;
        permisosPantallaSeleccionado = null;
        permisosObjetosDBSeleccionado = null;
        listaPerfilesCrear = new ArrayList<Perfiles>();
        listaPerfilesBorrar = new ArrayList<Perfiles>();
        listaPerfilesModificar = new ArrayList<Perfiles>();
        nuevoPerfil = new Perfiles();
        duplicarPerfil = new Perfiles();
        editarPerfil = new Perfiles();

        listaPermisosObjetosDBCrear = new ArrayList<PermisosObjetosDB>();
        listaPermisosObjetosDBBorrar = new ArrayList<PermisosObjetosDB>();
        listaPermisosObjetosDBModificar = new ArrayList<PermisosObjetosDB>();
        nuevoPermisosObjetosDB = new PermisosObjetosDB();
        duplicarPermisosObjetosDB = new PermisosObjetosDB();
        editarPermisosObjetosDB = new PermisosObjetosDB();

        listaPermisosPantallasCrear = new ArrayList<PermisosPantallas>();
        listaPermisosPantallasBorrar = new ArrayList<PermisosPantallas>();
        listaPermisosPantallasModificar = new ArrayList<PermisosPantallas>();
        nuevoPermisosPantalla = new PermisosPantallas();
        nuevoPermisosPantalla.setObjetofrm(new ObjetosBloques());
        nuevoPermisosPantalla.setPerfil(new Perfiles());
        duplicarPermisosPantalla = new PermisosPantallas();
        duplicarPermisosPantalla.setObjetofrm(new ObjetosBloques());
        duplicarPermisosPantalla.setPerfil(new Perfiles());
        editarPermisosPantallas = new PermisosPantallas();

        lovPermisosPantallas = null;
        lovPermisosObjetosDB = null;
        cualCelda = -1;
        cualCeldaObjetos = -1;
        cualCeldaPantalla = -1;
        bandera = 0;
        tipoLista = 0;
        k = 0;
        aceptar = true;
        guardado = true;
        activarLov = true;
        activarMtodos = true;
        activarMtodos2 = true;
        mapParametros.put("paginaAnterior", paginaAnterior);
        auxclon = new Perfiles();
        lovPerfilAlias = null;
    }

    public void limpiarListasValor() {
        lovPermisosObjetosDB = null;
        lovPermisosPantallas = null;
        lovPerfilAlias = null;
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarPerfiles.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
            administarReportes.obtenerConexion(ses.getId());
            externalContext = x.getExternalContext();
            userAgent = externalContext.getRequestHeaderMap().get("User-Agent");
        } catch (Exception e) {
            log.error("Error postconstruct " + this.getClass().getName() + ": " + e);
            log.error("Causa: " + e.getCause());
        }
    }

    public void recibirPaginaEntrante(String pagina) {
        paginaAnterior = pagina;
        listaPerfiles = null;
        getListaPerfiles();
        if (listaPerfiles != null) {
            if (!listaPerfiles.isEmpty()) {
                perfilSeleccionado = listaPerfiles.get(0);
            }
        }
        listaPermisosPantallas = null;
        getListaPermisosPantallas();
        listaPermisosObjetosDB = null;
        getListaPermisosObjetosDB();

    }

    public void recibirParametros(Map<String, Object> map) {
        mapParametros = map;
        paginaAnterior = (String) mapParametros.get("paginaAnterior");
        listaPerfiles = null;
        getListaPerfiles();
        if (listaPerfiles != null) {
            if (!listaPerfiles.isEmpty()) {
                perfilSeleccionado = listaPerfiles.get(0);
            }
        }
        listaPermisosPantallas = null;
        getListaPermisosPantallas();
        listaPermisosObjetosDB = null;
        getListaPermisosObjetosDB();
    }

    public void navegar(String pag) {
        FacesContext fc = FacesContext.getCurrentInstance();
        ControlListaNavegacion controlListaNavegacion = (ControlListaNavegacion) fc.getApplication().evaluateExpressionGet(fc, "#{controlListaNavegacion}", ControlListaNavegacion.class);
        String pagActual = "perfil";
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

    public String redirigir() {
        return paginaAnterior;
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            altoTabla = "36";
            altoTablaPantalla = "30";
            altoTablaObjeto = "30";
            codigo = (Column) c.getViewRoot().findComponent("form:datosPerfiles:codigo");
            codigo.setFilterStyle("width: 85% !important;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosPerfiles:descripcion");
            descripcion.setFilterStyle("width: 85% !important;");
            contrasena = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosPerfiles:password");
            contrasena.setFilterStyle("width: 85% !important;");
            //columnas tabla permisos pantallas
            nompantalla = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosPantallas:nompantalla");
            nompantalla.setFilterStyle("width: 85% !important;");
            nombloque = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosPantallas:nombloque");
            nombloque.setFilterStyle("width: 85% !important;");
            nomobjeto = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosPantallas:nomobjeto");
            nomobjeto.setFilterStyle("width: 85% !important;");
            tipo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosPantallas:tipo");
            tipo.setFilterStyle("width: 85% !important;");
            comentario = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosPantallas:comentario");
            comentario.setFilterStyle("width: 85% !important;");
            //columnas tabla permisos objetos
            nomobjetop = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosObjetos:nomobjetop");
            nomobjetop.setFilterStyle("width: 85% !important;");
            tipop = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosObjetos:tipop");
            tipop.setFilterStyle("width: 85% !important;");
            modulo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosObjetos:modulo");
            modulo.setFilterStyle("width: 85% !important;");
            clasificacion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosObjetos:clasificacion");
            clasificacion.setFilterStyle("width: 85% !important;");
            RequestContext.getCurrentInstance().update("form:datosPerfiles");
            RequestContext.getCurrentInstance().update("form:datosPantallas");
            RequestContext.getCurrentInstance().update("form:datosObjetos");
            bandera = 1;
        } else if (bandera == 1) {
            log.info("Desactivar");
            altoTabla = "56";
            altoTablaPantalla = "50";
            altoTablaObjeto = "50";
            codigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosPerfiles:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosPerfiles:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            contrasena = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosPerfiles:password");
            contrasena.setFilterStyle("display: none; visibility: hidden;");
            //columnas tabla permisos pantallas
            nompantalla = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosPantallas:nompantalla");
            nompantalla.setFilterStyle("display: none; visibility: hidden;");
            nombloque = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosPantallas:nombloque");
            nombloque.setFilterStyle("display: none; visibility: hidden;");
            nomobjeto = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosPantallas:nomobjeto");
            nomobjeto.setFilterStyle("display: none; visibility: hidden;");
            tipo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosPantallas:tipo");
            tipo.setFilterStyle("display: none; visibility: hidden;");
            comentario = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosPantallas:comentario");
            comentario.setFilterStyle("display: none; visibility: hidden;");
            //columnas tabla permisos objetos

            nomobjetop = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosObjetos:nomobjetop");
            nomobjetop.setFilterStyle("display: none; visibility: hidden;");
            tipop = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosObjetos:tipop");
            tipop.setFilterStyle("display: none; visibility: hidden;");
            modulo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosObjetos:modulo");
            modulo.setFilterStyle("display: none; visibility: hidden;");
            clasificacion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosObjetos:clasificacion");
            clasificacion.setFilterStyle("display: none; visibility: hidden;");

            RequestContext.getCurrentInstance().update("form:datosPantallas");
            RequestContext.getCurrentInstance().update("form:datosPerfiles");
            RequestContext.getCurrentInstance().update("form:datosObjetos");
            bandera = 0;
            listaPerfilesFiltrar = null;
            listaPermisosPantallasFiltrar = null;
            listaPermisosObjetosDBFiltrar = null;
            tipoLista = 0;
        }
    }

    public void modificarPerfil(Perfiles perfil) {
        perfilSeleccionado = perfil;
        if (!listaPerfilesCrear.contains(perfilSeleccionado)) {
            if (listaPerfilesModificar.isEmpty()) {
                listaPerfilesModificar.add(perfilSeleccionado);
            } else if (!listaPerfilesModificar.contains(perfilSeleccionado)) {
                listaPerfilesModificar.add(perfilSeleccionado);
            }
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
        RequestContext.getCurrentInstance().update("form:datosPerfiles");
    }

    public void modificarPantalla(PermisosPantallas permisop) {
        permisosPantallaSeleccionado = permisop;
        if (!listaPermisosPantallasCrear.contains(permisosPantallaSeleccionado)) {
            if (listaPermisosPantallasModificar.isEmpty()) {
                listaPermisosPantallasModificar.add(permisosPantallaSeleccionado);
            } else if (!listaPermisosPantallasModificar.contains(permisosPantallaSeleccionado)) {
                listaPermisosPantallasModificar.add(permisosPantallaSeleccionado);
            }
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
        RequestContext.getCurrentInstance().update("form:datosPantallas");
    }

    public void modificarObjetoDB(PermisosObjetosDB permisobj) {
        permisosObjetosDBSeleccionado = permisobj;
        if (!listaPermisosObjetosDBCrear.contains(permisosObjetosDBSeleccionado)) {
            if (listaPermisosObjetosDBModificar.isEmpty()) {
                listaPermisosObjetosDBModificar.add(permisosObjetosDBSeleccionado);
            } else if (!listaPermisosObjetosDBModificar.contains(permisosObjetosDBSeleccionado)) {
                listaPermisosObjetosDBModificar.add(permisosObjetosDBSeleccionado);
            }
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
        RequestContext.getCurrentInstance().update("form:datosObjetos");
    }

    public void seleccionarTipo(String tipo, PermisosPantallas perpantalla) {
        permisosPantallaSeleccionado = perpantalla;
        if (tipo.equals("BOTON")) {
            permisosPantallaSeleccionado.setTipo("BOTON");
        } else if (tipo.equals("CASILLA")) {
            permisosPantallaSeleccionado.setTipo("CASILLA");
        } else if (tipo.equals("LISTA")) {
            permisosPantallaSeleccionado.setTipo("LISTA");
        } else if (tipo.equals("TEXTO")) {
            permisosPantallaSeleccionado.setTipo("TEXTO");
        } else if (tipo.equals("ELEMENTO MOSTRADO")) {
            permisosPantallaSeleccionado.setTipo("MOSTRADO");
        }

        if (!listaPermisosPantallasCrear.contains(permisosPantallaSeleccionado)) {
            if (listaPermisosPantallasModificar.isEmpty()) {
                listaPermisosPantallasModificar.add(permisosPantallaSeleccionado);
            } else if (!listaPermisosPantallasModificar.contains(permisosPantallaSeleccionado)) {
                listaPermisosPantallasModificar.add(permisosPantallaSeleccionado);
            }
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
        RequestContext.getCurrentInstance().update("form:datosPantallas");
    }

    public void seleccionarTipoNuevo(String tipo, int tipoNuevo) {

        if (tipoNuevo == 1) {
            if (tipo.equals("BOTON")) {
                nuevoPermisosPantalla.setTipo("BOTON");
            } else if (tipo.equals("CASILLA")) {
                nuevoPermisosPantalla.setTipo("CASILLA");
            } else if (tipo.equals("LISTA")) {
                nuevoPermisosPantalla.setTipo("LISTA");
            } else if (tipo.equals("TEXTO")) {
                nuevoPermisosPantalla.setTipo("TEXTO");
            } else if (tipo.equals("ELEMENTO MOSTRADO")) {
                nuevoPermisosPantalla.setTipo("MOSTRADO");
            }
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTipo");
        } else if (tipoNuevo == 2) {
            if (tipo.equals("BOTON")) {
                duplicarPermisosPantalla.setTipo("BOTON");
            } else if (tipo.equals("CASILLA")) {
                duplicarPermisosPantalla.setTipo("CASILLA");
            } else if (tipo.equals("LISTA")) {
                duplicarPermisosPantalla.setTipo("LISTA");
            } else if (tipo.equals("TEXTO")) {
                duplicarPermisosPantalla.setTipo("TEXTO");
            } else if (tipo.equals("ELEMENTO MOSTRADO")) {
                duplicarPermisosPantalla.setTipo("MOSTRADO");
            }
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTipo");
        }
    }

    public void borrarPerfiles() {
        if (perfilSeleccionado != null) {
            if (!listaPerfilesModificar.isEmpty() && listaPerfilesModificar.contains(perfilSeleccionado)) {
                listaPerfilesModificar.remove(listaPerfilesModificar.indexOf(perfilSeleccionado));
                listaPerfilesBorrar.add(perfilSeleccionado);
            } else if (!listaPerfilesCrear.isEmpty() && listaPerfilesCrear.contains(perfilSeleccionado)) {
                listaPerfilesCrear.remove(listaPerfilesCrear.indexOf(perfilSeleccionado));
            } else {
                listaPerfilesBorrar.add(perfilSeleccionado);
            }
            listaPerfiles.remove(perfilSeleccionado);
            if (tipoLista == 1) {
                listaPerfilesFiltrar.remove(perfilSeleccionado);
            }
            RequestContext.getCurrentInstance().update("form:datosPerfiles");
            contarRegistrosPerfiles();
            perfilSeleccionado = null;
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void borrarPantallas() {
        if (permisosPantallaSeleccionado != null) {
            if (!listaPermisosPantallasModificar.isEmpty() && listaPermisosPantallasModificar.contains(permisosPantallaSeleccionado)) {
                listaPermisosPantallasModificar.remove(listaPermisosPantallasModificar.indexOf(permisosPantallaSeleccionado));
                listaPermisosPantallasBorrar.add(permisosPantallaSeleccionado);
            } else if (!listaPermisosPantallasCrear.isEmpty() && listaPermisosPantallasCrear.contains(permisosPantallaSeleccionado)) {
                listaPermisosPantallasCrear.remove(listaPermisosPantallasCrear.indexOf(permisosPantallaSeleccionado));
            } else {
                listaPermisosPantallasBorrar.add(permisosPantallaSeleccionado);
            }
            listaPermisosPantallas.remove(permisosPantallaSeleccionado);
            if (tipoLista == 1) {
                listaPermisosPantallasFiltrar.remove(permisosPantallaSeleccionado);
            }
            RequestContext.getCurrentInstance().update("form:datosPantallas");
            contarRegistrosPermisosPantallas();
            permisosPantallaSeleccionado = null;
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void borrarObjetoDB() {
        if (permisosObjetosDBSeleccionado != null) {
            if (!listaPermisosObjetosDBModificar.isEmpty() && listaPermisosObjetosDBModificar.contains(permisosObjetosDBSeleccionado)) {
                listaPermisosObjetosDBModificar.remove(listaPermisosObjetosDBModificar.indexOf(permisosObjetosDBSeleccionado));
                listaPermisosObjetosDBBorrar.add(permisosObjetosDBSeleccionado);
            } else if (!listaPermisosPantallasCrear.isEmpty() && listaPermisosObjetosDBCrear.contains(permisosObjetosDBSeleccionado)) {
                listaPermisosObjetosDBCrear.remove(listaPermisosObjetosDBCrear.indexOf(permisosObjetosDBSeleccionado));
            } else {
                listaPermisosObjetosDBBorrar.add(permisosObjetosDBSeleccionado);
            }
            listaPermisosObjetosDB.remove(permisosObjetosDBSeleccionado);
            if (tipoLista == 1) {
                listaPermisosObjetosDBFiltrar.remove(permisosObjetosDBSeleccionado);
            }
            RequestContext.getCurrentInstance().update("form:datosObjetos");
            contarRegistrosPermisosObjetos();
            permisosObjetosDBSeleccionado = null;
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }

    }

    public void revisarDialogoGuardar() {
        if (!listaPerfilesBorrar.isEmpty() || !listaPerfilesCrear.isEmpty() || !listaPerfilesModificar.isEmpty()) {
            RequestContext.getCurrentInstance().update("form:confirmarGuardar");
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
        }
    }

    public void cambiarIndice(Perfiles perfil, int celda) {
        cualTabla = 1;
        perfilSeleccionado = perfil;
        cualCelda = celda;
        perfilSeleccionado.getSecuencia();
        tablaImprimir = ":formExportar:datosPerfilesExportar";
        nombreArchivo = "PerfilesXML";
        if (cualCelda == 0) {
            perfilSeleccionado.getCodigo();
        } else if (cualCelda == 1) {
            perfilSeleccionado.getDescripcion();
        } else if (cualCelda == 2) {
            perfilSeleccionado.getPwd();
        }
        cualCeldaObjetos = -1;
        cualCeldaPantalla = -1;
        listaPermisosPantallas = null;
        listaPermisosPantallas = administrarPerfiles.consultarPermisosPantallas(perfilSeleccionado.getSecuencia());
        listaPermisosObjetosDB = null;
        listaPermisosObjetosDB = administrarPerfiles.consultarPermisosObjetos(perfilSeleccionado.getSecuencia());
        contarRegistrosPermisosPantallas();
        RequestContext.getCurrentInstance().update("form:datosPantallas");
        contarRegistrosPermisosObjetos();
        RequestContext.getCurrentInstance().update("form:datosObjetos");
    }

    public void cambiarIndicePantalla(PermisosPantallas perpantalla, int celda) {
        cualTabla = 2;
        tablaImprimir = ":formExportar:datosPerPantallasExportar";
        nombreArchivo = "PermisosPantallasXML";
        permisosPantallaSeleccionado = perpantalla;
        cualCeldaPantalla = celda;
        permisosPantallaSeleccionado.getSecuencia();
        if (cualCeldaPantalla == 0) {
            permisosPantallaSeleccionado.getObjetofrm().getBloque().getPantalla().getNombre();
            habilitarBotonLov();
        } else if (cualCeldaPantalla == 1) {
            permisosPantallaSeleccionado.getObjetofrm().getBloque().getNombre();
            habilitarBotonLov();
        } else if (cualCeldaPantalla == 2) {
            permisosPantallaSeleccionado.getObjetofrm().getNombre();
            habilitarBotonLov();
        } else if (cualCeldaPantalla == 3) {
            permisosPantallaSeleccionado.getTipo();
            habilitarBotonLov();
        } else if (cualCeldaPantalla == 4) {
            permisosPantallaSeleccionado.getObjetofrm().getComentario();
            deshabilitarBotonLov();
        }
        cualCelda = -1;
        cualCeldaObjetos = -1;
    }

    public void cambiarIndiceObjeto(PermisosObjetosDB perobjeto, int celda) {
        cualTabla = 3;
        permisosObjetosDBSeleccionado = perobjeto;
        cualCeldaObjetos = celda;
        tablaImprimir = ":formExportar:datosPerObjetosExportar";
        nombreArchivo = "PermisosObjetosDBXML";
        permisosObjetosDBSeleccionado.getSecuencia();
        if (cualCeldaObjetos == 0) {
            permisosObjetosDBSeleccionado.getObjetodb().getNombre();
            habilitarBotonLov();
        } else if (cualCeldaObjetos == 1) {
            permisosObjetosDBSeleccionado.getObjetodb().getTipo();
            deshabilitarBotonLov();
        } else if (cualCeldaObjetos == 2) {
            permisosObjetosDBSeleccionado.getObjetodb().getModulo();
            deshabilitarBotonLov();
        } else if (cualCeldaObjetos == 3) {
            permisosObjetosDBSeleccionado.getObjetodb().getClasificacion();
            deshabilitarBotonLov();
        }
        cualCelda = -1;
        cualCeldaPantalla = -1;
    }

    public void deshabilitarBotonLov() {
        activarLov = true;
        RequestContext.getCurrentInstance().update("form:listaValores");
    }

    public void habilitarBotonLov() {
        activarLov = false;
        RequestContext.getCurrentInstance().update("form:listaValores");
    }

    public void guardarYSalir() {
        guardadoGeneral();
        salir();
    }

    public void cancelarYSalir() {
        cancelarModificacion();
        salir();
    }

    public void guardadoGeneral() {
        try {
            if (guardado == false) {
                guardarCambiosPerfiles();
                guardarCambiosPantallas();
                guardarCambiosObjetosDB();
                FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                RequestContext.getCurrentInstance().update("form:growl");
            } else {
                log.info("no hay datos para guardar");
                FacesMessage msg = new FacesMessage("Información", "No se han realizado cambios a guardar");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                RequestContext.getCurrentInstance().update("form:growl");
            }
        } catch (Exception e) {
            log.warn("Error guardadoGeneral " + e.toString());
            FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado , Por favor intente nuevamente.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }

    }

    public void guardarCambiosPerfiles() {
        if (!listaPerfilesBorrar.isEmpty()) {
            administrarPerfiles.borrarPerfiles(listaPerfilesBorrar);
            listaPerfilesBorrar.clear();
        }
        if (!listaPerfilesCrear.isEmpty()) {
            administrarPerfiles.crearPerfiles(listaPerfilesCrear);
            listaPerfilesCrear.clear();
        }
        if (!listaPerfilesModificar.isEmpty()) {
            administrarPerfiles.modificarPerfiles(listaPerfilesModificar);
            listaPerfilesModificar.clear();
        }
        listaPerfiles = null;
        getListaPerfiles();
        RequestContext.getCurrentInstance().update("form:datosPerfiles");
        contarRegistrosPerfiles();
        k = 0;
        guardado = true;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
//        perfilSeleccionado = null;
    }

    public void guardarCambiosPantallas() {
        if (!listaPermisosPantallasBorrar.isEmpty()) {
            administrarPerfiles.borrarPermisoPantalla(listaPermisosPantallasBorrar);
            listaPermisosPantallasBorrar.clear();
        }
        if (!listaPermisosPantallasCrear.isEmpty()) {
            administrarPerfiles.crearPermisoPantalla(listaPermisosPantallasCrear);
            listaPermisosPantallasCrear.clear();
        }
        if (!listaPermisosPantallasModificar.isEmpty()) {
            administrarPerfiles.editarPermisoPantalla(listaPermisosPantallasModificar);
            listaPermisosPantallasModificar.clear();
        }
        listaPermisosPantallas = null;
        getListaPermisosPantallas();
        RequestContext.getCurrentInstance().update("form:datosPantallas");
        contarRegistrosPermisosPantallas();
        k = 0;
        guardado = true;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        permisosPantallaSeleccionado = null;
    }

    public void guardarCambiosObjetosDB() {
        if (!listaPermisosObjetosDBBorrar.isEmpty()) {
            administrarPerfiles.borrarPermisoObjeto(listaPermisosObjetosDBBorrar);
            listaPermisosObjetosDBBorrar.clear();
        }
        if (!listaPermisosObjetosDBCrear.isEmpty()) {
            administrarPerfiles.crearPermisoObjeto(listaPermisosObjetosDBCrear);
            listaPermisosObjetosDBCrear.clear();
        }
        if (!listaPermisosObjetosDBModificar.isEmpty()) {
            administrarPerfiles.editarPermisoObjeto(listaPermisosObjetosDBModificar);
            listaPermisosObjetosDBModificar.clear();
        }
        listaPermisosObjetosDB = null;
        getListaPermisosObjetosDB();
        RequestContext.getCurrentInstance().update("form:datosObjetos");
        contarRegistrosPermisosObjetos();
        k = 0;
        guardado = true;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        permisosObjetosDBSeleccionado = null;
    }

    public void cancelarModificacion() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 1) {
            codigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosPerfiles:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosPerfiles:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            contrasena = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosPerfiles:password");
            contrasena.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosPerfiles");

            nompantalla = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosPantallas:nompantalla");
            nompantalla.setFilterStyle("display: none; visibility: hidden;");
            nombloque = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosPantallas:nombloque");
            nombloque.setFilterStyle("display: none; visibility: hidden;");
            nomobjeto = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosPantallas:nomobjeto");
            nomobjeto.setFilterStyle("display: none; visibility: hidden;");
            tipo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosPantallas:tipo");
            tipo.setFilterStyle("display: none; visibility: hidden;");
            comentario = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosPantallas:comentario");
            comentario.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosPantallas");

            nomobjetop = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosObjetos:nomobjetop");
            nomobjetop.setFilterStyle("display: none; visibility: hidden;");
            tipop = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosObjetos:tipop");
            tipop.setFilterStyle("display: none; visibility: hidden;");
            modulo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosObjetos:modulo");
            modulo.setFilterStyle("display: none; visibility: hidden;");
            clasificacion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosObjetos:clasificacion");
            clasificacion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosObjetos");

            bandera = 0;
            listaPerfilesFiltrar = null;
            listaPermisosPantallasFiltrar = null;
            listaPermisosObjetosDBFiltrar = null;
            tipoLista = 0;
            altoTabla = "56";
            altoTablaPantalla = "50";
            altoTablaObjeto = "50";
        }

        listaPerfilesBorrar.clear();
        listaPerfilesCrear.clear();
        listaPerfilesModificar.clear();
        listaPerfiles = null;
        getListaPerfiles();
        contarRegistrosPerfiles();
        perfilSeleccionado = null;

        listaPermisosPantallasCrear.clear();
        listaPermisosPantallasBorrar.clear();
        listaPermisosPantallasModificar.clear();
        listaPermisosPantallas = null;
        getListaPermisosPantallas();
        contarRegistrosPermisosPantallas();
        permisosPantallaSeleccionado = null;

        listaPermisosObjetosDBCrear.clear();
        listaPermisosObjetosDBBorrar.clear();
        listaPermisosObjetosDBModificar.clear();
        listaPermisosObjetosDB = null;
        getListaPermisosObjetosDB();
        permisosObjetosDBSeleccionado = null;

        auxclon = new Perfiles();
        lovPerfilAlias = null;
        k = 0;
        guardado = true;
        RequestContext.getCurrentInstance().update("form:datosPerfiles");
        RequestContext.getCurrentInstance().update("form:datosPantallas");
        RequestContext.getCurrentInstance().update("form:datosObjetos");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void salir() {
        limpiarListasValor();
        if (bandera == 1) {
            codigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosPerfiles:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosPerfiles:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            contrasena = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosPerfiles:password");
            contrasena.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosPerfiles");

            nompantalla = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosPantallas:nompantalla");
            nompantalla.setFilterStyle("display: none; visibility: hidden;");
            nombloque = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosPantallas:nombloque");
            nombloque.setFilterStyle("display: none; visibility: hidden;");
            nomobjeto = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosPantallas:nomobjeto");
            nomobjeto.setFilterStyle("display: none; visibility: hidden;");
            tipo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosPantallas:tipo");
            tipo.setFilterStyle("display: none; visibility: hidden;");
            comentario = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosPantallas:comentario");
            comentario.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosPantallas");

            nomobjetop = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosObjetos:nomobjetop");
            nomobjetop.setFilterStyle("display: none; visibility: hidden;");
            tipop = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosObjetos:tipop");
            tipop.setFilterStyle("display: none; visibility: hidden;");
            modulo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosObjetos:modulo");
            modulo.setFilterStyle("display: none; visibility: hidden;");
            clasificacion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosObjetos:clasificacion");
            clasificacion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosObjetos");

            bandera = 0;
            listaPerfilesFiltrar = null;
            listaPermisosPantallasFiltrar = null;
            listaPermisosObjetosDBFiltrar = null;
            tipoLista = 0;
            altoTabla = "56";
            altoTablaPantalla = "50";
            altoTablaObjeto = "50";
        }

        listaPerfilesBorrar.clear();
        listaPerfilesCrear.clear();
        listaPerfilesModificar.clear();
        listaPermisosObjetosDBBorrar.clear();
        listaPermisosObjetosDBCrear.clear();
        listaPermisosObjetosDBModificar.clear();
        listaPermisosPantallasBorrar.clear();
        listaPermisosPantallasCrear.clear();
        listaPermisosPantallasModificar.clear();
        //
        perfilSeleccionado = null;
        permisosObjetosDBSeleccionado = null;
        permisosPantallaSeleccionado = null;
        k = 0;
        auxclon = new Perfiles();
        lovPerfilAlias = null;
        listaPerfiles = null;
        listaPermisosPantallas = null;
        listaPermisosObjetosDB = null;
        guardado = true;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        navegar("atras");
    }

    public void editarCelda() {
        if (cualTabla == 1) {
            if (perfilSeleccionado != null) {
                editarPerfil = perfilSeleccionado;
                if (cualCelda == 0) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarCodigo");
                    RequestContext.getCurrentInstance().execute("PF('editarCodigo').show()");
                    cualCelda = -1;
                } else if (cualCelda == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarDesc");
                    RequestContext.getCurrentInstance().execute("PF('editarDesc').show()");
                    cualCelda = -1;
                }
            }
        } else if (cualTabla == 2) {
            if (permisosPantallaSeleccionado != null) {
                editarPermisosPantallas = permisosPantallaSeleccionado;
                if (cualCeldaPantalla == 0) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarNomPantalla");
                    RequestContext.getCurrentInstance().execute("PF('editarNomPantalla').show()");
                    cualCelda = -1;
                } else if (cualCeldaPantalla == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarNomBloque");
                    RequestContext.getCurrentInstance().execute("PF('editarNomBloque').show()");
                    cualCelda = -1;
                } else if (cualCeldaPantalla == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarNomObjeto");
                    RequestContext.getCurrentInstance().execute("PF('editarNomObjeto').show()");
                    cualCelda = -1;
                } else if (cualCeldaPantalla == 4) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarComentario");
                    RequestContext.getCurrentInstance().execute("PF('editarComentario').show()");
                    cualCelda = -1;
                }
            }

        } else if (cualTabla == 3) {
            if (permisosObjetosDBSeleccionado != null) {
                editarPermisosObjetosDB = permisosObjetosDBSeleccionado;
                if (cualCelda == 0) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarNomObjetoP");
                    RequestContext.getCurrentInstance().execute("PF('editarNomObjetoP').show()");
                    cualCelda = -1;
                } else if (cualCeldaObjetos == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarTipoP");
                    RequestContext.getCurrentInstance().execute("PF('editarTipoP').show()");
                    cualCelda = -1;
                } else if (cualCeldaObjetos == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarModulo");
                    RequestContext.getCurrentInstance().execute("PF('editarModulo').show()");
                    cualCelda = -1;
                } else if (cualCeldaObjetos == 3) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarClasificacion");
                    RequestContext.getCurrentInstance().execute("PF('editarClasificacion').show()");
                    cualCelda = -1;
                }
            }
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }

    }

    public void mostrarDialogoNuevoPerfil() {
        RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroPerfil");
        RequestContext.getCurrentInstance().execute("PF('NuevoRegistroPerfil').show()");
    }

    public void mostrarDialogoNuevoPerPantalla() {
        if (perfilSeleccionado != null) {
            RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroPerPantallas");
            RequestContext.getCurrentInstance().execute("PF('NuevoRegistroPerPantallas').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('validacionInsertarPerfil').show()");
        }
    }

    public void mostrarDialogoNuevoPerObjeto() {
        if (perfilSeleccionado != null) {
            RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroPerObjetos");
            RequestContext.getCurrentInstance().execute("PF('NuevoRegistroPerObjetos').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('validacionInsertarPerfil').show()");
        }
    }

    public void mostrarDialogoElegirTabla() {
        RequestContext.getCurrentInstance().update("formularioDialogos:elegirTabla");
        RequestContext.getCurrentInstance().execute("PF('elegirTabla').show()");
    }

    public void agregarNuevoPerfil() {
        int contador = 0;
        int duplicados = 0;
        mensajeValidacion = " ";

        if (nuevoPerfil.getDescripcion().equals(" ") || nuevoPerfil.getDescripcion().equals("")) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            contador++;
        }
        if (nuevoPerfil.getCodigo() == null) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            contador++;
        }

        for (int i = 0; i < listaPerfiles.size(); i++) {
            if (listaPerfiles.get(i).getCodigo() == nuevoPerfil.getCodigo()) {
                RequestContext.getCurrentInstance().update("formularioDialogos:existeCodigo");
                RequestContext.getCurrentInstance().execute("PF('existeCodigo').show()");
                duplicados++;
            }
            if (listaPerfiles.get(i).getDescripcion() == nuevoPerfil.getDescripcion()) {
                RequestContext.getCurrentInstance().update("formularioDialogos:existeNombre");
                RequestContext.getCurrentInstance().execute("PF('existeNombre').show()");
                duplicados++;
            }
        }

        if (contador != 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevoPerfil");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevoPerfil').show()");
        }

        if (contador == 0 && duplicados == 0) {
            if (bandera == 1) {
                altoTabla = "56";
                FacesContext c = FacesContext.getCurrentInstance();
                codigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosPerfiles:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosPerfiles:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                contrasena = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosPerfiles:password");
                contrasena.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosPerfiles");
                bandera = 0;
                listaPerfilesFiltrar = null;
                tipoLista = 0;
            }
            k++;
            l = BigInteger.valueOf(k);
            nuevoPerfil.setSecuencia(l);
            listaPerfilesCrear.add(nuevoPerfil);
            listaPerfiles.add(nuevoPerfil);
            perfilSeleccionado = nuevoPerfil;
            nuevoPerfil = new Perfiles();
            RequestContext context = RequestContext.getCurrentInstance();
            contarRegistrosPerfiles();
            RequestContext.getCurrentInstance().update("form:datosPerfiles");
            RequestContext.getCurrentInstance().execute("PF('NuevoRegistroPerfil').hide()");
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
    }

    public void agregarPermisoPantalla() {
        int contador = 0;
        int duplicados = 0;
        mensajeValidacion = " ";
        if (nuevoPermisosPantalla.getObjetofrm().getBloque().getPantalla().getNombre().equals(" ") || nuevoPermisosPantalla.getObjetofrm().getBloque().getPantalla().getNombre().equals("")) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            contador++;
        }
        if (nuevoPermisosPantalla.getObjetofrm().getBloque().getNombre().equals(" ") || nuevoPermisosPantalla.getObjetofrm().getBloque().getNombre().equals("")) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            contador++;
        }
        if (nuevoPermisosPantalla.getObjetofrm().getNombre().equals(" ") || nuevoPermisosPantalla.getObjetofrm().getNombre().equals("")) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            contador++;
        }
        if (nuevoPermisosPantalla.getObjetofrm().getComentario().equals(" ") || nuevoPermisosPantalla.getObjetofrm().getComentario().equals("")) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            contador++;
        }
        for (int i = 0; i < listaPermisosPantallas.size(); i++) {
            if (listaPermisosPantallas.get(i).getObjetofrm().getSecuencia() == nuevoPermisosPantalla.getObjetofrm().getSecuencia()) {
                duplicados++;
            }
            if (listaPermisosPantallas.get(i).getPerfil().getSecuencia() == nuevoPermisosPantalla.getPerfil().getSecuencia()) {
                duplicados++;
            }
            if (listaPermisosPantallas.get(i).getObjetofrm().getBloque().getNombre().equals(nuevoPermisosPantalla.getObjetofrm().getBloque().getNombre())) {
                duplicados++;
            }
            if (listaPermisosPantallas.get(i).getObjetofrm().getBloque().getPantalla().getNombre().equals(nuevoPermisosPantalla.getObjetofrm().getBloque().getPantalla().getNombre())) {
                duplicados++;
            }
            if (listaPermisosPantallas.get(i).getObjetofrm().getNombre().equals(nuevoPermisosPantalla.getObjetofrm().getNombre())) {
                duplicados++;
            }
        }
        if (contador != 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevoPerPantalla");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevoPerPantalla').show()");
        }
        if (duplicados != 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:existePantalla");
            RequestContext.getCurrentInstance().execute("PF('existePantalla').show()");
        }
        if (contador == 0 && duplicados == 0) {
            if (bandera == 1) {
                altoTablaPantalla = "50";
                FacesContext c = FacesContext.getCurrentInstance();
                nompantalla = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosPantallas:nompantalla");
                nompantalla.setFilterStyle("display: none; visibility: hidden;");
                nombloque = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosPantallas:nombloque");
                nombloque.setFilterStyle("display: none; visibility: hidden;");
                nomobjeto = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosPantallas:nomobjeto");
                nomobjeto.setFilterStyle("display: none; visibility: hidden;");
                tipo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosPantallas:tipo");
                tipo.setFilterStyle("display: none; visibility: hidden;");
                comentario = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosPantallas:comentario");
                comentario.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosPantallas");
                bandera = 0;
                listaPermisosPantallasFiltrar = null;
                tipoLista = 0;
            }
            k++;
            l = BigInteger.valueOf(k);
            nuevoPermisosPantalla.setSecuencia(l);
            listaPermisosPantallasCrear.add(nuevoPermisosPantalla);
            listaPermisosPantallas.add(nuevoPermisosPantalla);
            permisosPantallaSeleccionado = nuevoPermisosPantalla;
            nuevoPermisosPantalla = new PermisosPantallas();
            contarRegistrosPermisosPantallas();
            RequestContext.getCurrentInstance().update("form:datosPantallas");
            RequestContext.getCurrentInstance().execute("PF('NuevoRegistroPerPantallas').hide()");
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
    }

    public void agregarPermisoObjeto() {
        int contador = 0;
        int duplicados = 0;
        mensajeValidacion = " ";

        if (nuevoPermisosObjetosDB.getObjetodb().getNombre().equals(" ") || nuevoPermisosObjetosDB.getObjetodb().getNombre().equals("")) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            contador++;
        }

        if (nuevoPermisosObjetosDB.getObjetodb().getTipo().equals(" ") || nuevoPermisosObjetosDB.getObjetodb().getTipo().equals("")) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            contador++;
        }

        if (nuevoPermisosObjetosDB.getObjetodb().getModulo().getNombre().equals(" ") || nuevoPermisosObjetosDB.getObjetodb().getModulo().getNombre().equals("")) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            contador++;
        }

        if (nuevoPermisosObjetosDB.getObjetodb().getClasificacion().equals(" ") || nuevoPermisosObjetosDB.getObjetodb().getClasificacion().equals("")) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            contador++;
        }

        for (int i = 0; i < listaPermisosObjetosDB.size(); i++) {
            if (listaPermisosObjetosDB.get(i).getObjetodb().getSecuencia() == nuevoPermisosObjetosDB.getObjetodb().getSecuencia()) {
                duplicados++;
            }
            if (listaPermisosObjetosDB.get(i).getPerfil().getSecuencia() == nuevoPermisosObjetosDB.getPerfil().getSecuencia()) {
                duplicados++;
            }
            if (listaPermisosObjetosDB.get(i).getObjetodb().getNombre().equals(nuevoPermisosObjetosDB.getObjetodb().getNombre())) {
                duplicados++;
            }
            if (listaPermisosObjetosDB.get(i).getObjetodb().getTipo().equals(nuevoPermisosObjetosDB.getObjetodb().getTipo())) {
                duplicados++;
            }
            if (listaPermisosObjetosDB.get(i).getObjetodb().getModulo().getNombre().equals(nuevoPermisosObjetosDB.getObjetodb().getModulo().getNombre())) {
                duplicados++;
            }
            if (listaPermisosObjetosDB.get(i).getObjetodb().getClasificacion().equals(nuevoPermisosObjetosDB.getObjetodb().getClasificacion())) {
                duplicados++;
            }
        }

        if (contador != 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevoPerPantalla");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevoPerPantalla').show()");
        }
        if (duplicados != 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:existeObjeto");
            RequestContext.getCurrentInstance().execute("PF('existeObjeto').show()");
        }

        if (contador == 0 && duplicados == 0) {
            if (bandera == 1) {
                altoTablaObjeto = "50";
                FacesContext c = FacesContext.getCurrentInstance();
                nomobjetop = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosObjetos:nomobjetop");
                nomobjetop.setFilterStyle("display: none; visibility: hidden;");
                tipop = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosObjetos:tipop");
                tipop.setFilterStyle("display: none; visibility: hidden;");
                modulo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosObjetos:modulo");
                modulo.setFilterStyle("display: none; visibility: hidden;");
                clasificacion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosObjetos:clasificacion");
                clasificacion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosObjetos");
                bandera = 0;
                listaPermisosObjetosDBFiltrar = null;
                tipoLista = 0;
            }
            k++;
            l = BigInteger.valueOf(k);
            nuevoPermisosObjetosDB.setSecuencia(l);
            listaPermisosObjetosDBCrear.add(nuevoPermisosObjetosDB);
            listaPermisosObjetosDB.add(nuevoPermisosObjetosDB);
            permisosObjetosDBSeleccionado = nuevoPermisosObjetosDB;
            nuevoPermisosObjetosDB = new PermisosObjetosDB();
            contarRegistrosPermisosObjetos();
            RequestContext.getCurrentInstance().update("form:datosObjetos");
            RequestContext.getCurrentInstance().execute("PF('NuevoRegistroPerObjetos').hide()");
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
    }

    public void limpiarNuevoPerfil() {
        nuevoPerfil = new Perfiles();
    }

    public void limpiarNuevoPermisoPantalla() {
        nuevoPermisosPantalla = new PermisosPantallas();
        nuevoPermisosPantalla.setObjetofrm(new ObjetosBloques());
        nuevoPermisosPantalla.setPerfil(new Perfiles());
    }

    public void limpiarNuevoPermisoObjetos() {
        nuevoPermisosObjetosDB = new PermisosObjetosDB();
        nuevoPermisosObjetosDB.setPerfil(new Perfiles());
        nuevoPermisosObjetosDB.setObjetodb(new ObjetosDB());
    }

    public void duplicandoPerfil() {
        if (perfilSeleccionado != null) {
            duplicarPerfil = new Perfiles();
            duplicarPerfil.setCodigo(perfilSeleccionado.getCodigo());
            duplicarPerfil.setDescripcion(perfilSeleccionado.getDescripcion());
            duplicarPerfil.setPwd(perfilSeleccionado.getPwd());
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarPerfil");
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroPerfil').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void confirmarDuplicarPerfil() {
        int contador = 0;
        int duplicados = 0;
        mensajeValidacion = " ";

        if (duplicarPerfil.getDescripcion().equals(" ") || duplicarPerfil.getDescripcion().equals("")) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            contador++;
        }
        if (duplicarPerfil.getCodigo() == null) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            contador++;
        }

        for (int i = 0; i < listaPerfiles.size(); i++) {
            if (listaPerfiles.get(i).getCodigo() == duplicarPerfil.getCodigo()) {
                RequestContext.getCurrentInstance().update("formularioDialogos:existeCodigo");
                RequestContext.getCurrentInstance().execute("PF('existeCodigo').show()");
                duplicados++;
            }
            if (listaPerfiles.get(i).getDescripcion() == duplicarPerfil.getDescripcion()) {
                RequestContext.getCurrentInstance().update("formularioDialogos:existeNombre");
                RequestContext.getCurrentInstance().execute("PF('existeNombre').show()");
                duplicados++;
            }
        }

        if (contador != 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevoPerfil");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevoPerfil').show()");
        }

        if (contador == 0 && duplicados == 0) {
            if (bandera == 1) {
                altoTabla = "56";
                FacesContext c = FacesContext.getCurrentInstance();
                codigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosPerfiles:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosPerfiles:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                contrasena = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosPerfiles:password");
                contrasena.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosPerfiles");
                bandera = 0;
                listaPerfilesFiltrar = null;
                tipoLista = 0;
            }
            k++;
            l = BigInteger.valueOf(k);
            duplicarPerfil.setSecuencia(l);
            listaPerfiles.add(duplicarPerfil);
            listaPerfilesCrear.add(duplicarPerfil);
            perfilSeleccionado = duplicarPerfil;
            contarRegistrosPerfiles();
            RequestContext.getCurrentInstance().update("form:datosPerfiles");
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroPerfil').hide()");
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            duplicarPerfil = new Perfiles();
        }
    }

    public void duplicandoPermisosPantallas() {
        if (permisosPantallaSeleccionado != null) {
            duplicarPermisosPantalla = new PermisosPantallas();
            duplicarPermisosPantalla.setObjetofrm(permisosPantallaSeleccionado.getObjetofrm());
            duplicarPermisosPantalla.setPerfil(permisosPantallaSeleccionado.getPerfil());
            duplicarPermisosPantalla.setTipo(permisosPantallaSeleccionado.getTipo());
            duplicarPermisosPantalla.setS(permisosPantallaSeleccionado.getS());
            duplicarPermisosPantalla.setI(permisosPantallaSeleccionado.getI());
            duplicarPermisosPantalla.setD(permisosPantallaSeleccionado.getD());
            duplicarPermisosPantalla.setU(permisosPantallaSeleccionado.getU());
            duplicarPermisosPantalla.setE(permisosPantallaSeleccionado.getE());
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarPermisoPantalla");
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroPerPantalla').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void confirmarDuplicarPermisosPantallas() {
        int contador = 0;
        int duplicados = 0;
        mensajeValidacion = " ";

        if (duplicarPermisosPantalla.getObjetofrm().getBloque().getPantalla().getNombre().equals(" ") || duplicarPermisosPantalla.getObjetofrm().getBloque().getPantalla().getNombre().equals("")) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            contador++;
        }

        if (duplicarPermisosPantalla.getObjetofrm().getBloque().getNombre().equals(" ") || duplicarPermisosPantalla.getObjetofrm().getBloque().getNombre().equals("")) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            contador++;
        }

        if (duplicarPermisosPantalla.getObjetofrm().getNombre().equals(" ") || duplicarPermisosPantalla.getObjetofrm().getNombre().equals("")) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            contador++;
        }

        if (duplicarPermisosPantalla.getObjetofrm().getComentario().equals(" ") || duplicarPermisosPantalla.getObjetofrm().getComentario().equals("")) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            contador++;
        }

        for (int i = 0; i < listaPermisosPantallas.size(); i++) {
            if (listaPermisosPantallas.get(i).getObjetofrm().getSecuencia() == duplicarPermisosPantalla.getObjetofrm().getSecuencia()) {
                RequestContext.getCurrentInstance().update("formularioDialogos:existeCodigo");
                RequestContext.getCurrentInstance().execute("PF('existeCodigo').show()");
                duplicados++;
            }
            if (listaPermisosPantallas.get(i).getPerfil().getSecuencia() == duplicarPermisosPantalla.getPerfil().getSecuencia()) {
                RequestContext.getCurrentInstance().update("formularioDialogos:existeCodigo");
                RequestContext.getCurrentInstance().execute("PF('existeCodigo').show()");
                duplicados++;
            }
        }

        if (contador != 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevoPerPantalla");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevoPerPantalla').show()");
        }

        if (contador == 0 && duplicados == 0) {
            if (bandera == 1) {
                altoTabla = "50";
                FacesContext c = FacesContext.getCurrentInstance();
                nompantalla = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosPantallas:nompantalla");
                nompantalla.setFilterStyle("display: none; visibility: hidden;");
                nombloque = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosPantallas:nombloque");
                nombloque.setFilterStyle("display: none; visibility: hidden;");
                nomobjeto = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosPantallas:nomobjeto");
                nomobjeto.setFilterStyle("display: none; visibility: hidden;");
                tipo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosPantallas:tipo");
                tipo.setFilterStyle("display: none; visibility: hidden;");
                comentario = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosPantallas:comentario");
                comentario.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosPantallas");
                bandera = 0;
                listaPermisosPantallasFiltrar = null;
                tipoLista = 0;
            }
            k++;
            l = BigInteger.valueOf(k);
            duplicarPermisosPantalla.setSecuencia(l);
            listaPermisosPantallasCrear.add(duplicarPermisosPantalla);
            listaPermisosPantallas.add(duplicarPermisosPantalla);
            permisosPantallaSeleccionado = duplicarPermisosPantalla;
            duplicarPermisosPantalla = new PermisosPantallas();
            contarRegistrosPermisosPantallas();
            RequestContext.getCurrentInstance().update("form:datosPantallas");
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroPerPantalla').hide()");
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
    }

    public void duplicandoPermisosObjetos() {
        if (permisosObjetosDBSeleccionado != null) {
            duplicarPermisosObjetosDB = new PermisosObjetosDB();
            duplicarPermisosObjetosDB.setPerfil(permisosObjetosDBSeleccionado.getPerfil());
            duplicarPermisosObjetosDB.setObjetodb(permisosObjetosDBSeleccionado.getObjetodb());
            duplicarPermisosObjetosDB.setS(permisosObjetosDBSeleccionado.getS());
            duplicarPermisosObjetosDB.setI(permisosObjetosDBSeleccionado.getI());
            duplicarPermisosObjetosDB.setD(permisosObjetosDBSeleccionado.getD());
            duplicarPermisosObjetosDB.setU(permisosObjetosDBSeleccionado.getU());
            duplicarPermisosObjetosDB.setE(permisosObjetosDBSeleccionado.getE());
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarPermisoObjeto");
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroPerObjeto').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void confirmarDuplicarPermisosObjetos() {
        int contador = 0;
        int duplicados = 0;
        mensajeValidacion = " ";

        if (duplicarPermisosObjetosDB.getObjetodb().getNombre().equals(" ") || duplicarPermisosObjetosDB.getObjetodb().getNombre().equals("")) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            contador++;
        }

        if (duplicarPermisosObjetosDB.getObjetodb().getTipo().equals(" ") || duplicarPermisosObjetosDB.getObjetodb().getTipo().equals("")) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            contador++;
        }

        if (duplicarPermisosObjetosDB.getObjetodb().getModulo().getNombre().equals(" ") || duplicarPermisosObjetosDB.getObjetodb().getModulo().getNombre().equals("")) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            contador++;
        }

        if (duplicarPermisosObjetosDB.getObjetodb().getClasificacion().equals(" ") || duplicarPermisosObjetosDB.getObjetodb().getClasificacion().equals("")) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            contador++;
        }

        for (int i = 0; i < listaPermisosObjetosDB.size(); i++) {
            if (listaPermisosObjetosDB.get(i).getObjetodb().getSecuencia() == duplicarPermisosObjetosDB.getObjetodb().getSecuencia()) {
                duplicados++;
            }
            if (listaPermisosObjetosDB.get(i).getPerfil().getSecuencia() == duplicarPermisosObjetosDB.getPerfil().getSecuencia()) {
                duplicados++;
            }
            if (listaPermisosObjetosDB.get(i).getObjetodb().getNombre().equals(duplicarPermisosObjetosDB.getObjetodb().getNombre())) {
                duplicados++;
            }
            if (listaPermisosObjetosDB.get(i).getObjetodb().getTipo().equals(duplicarPermisosObjetosDB.getObjetodb().getTipo())) {
                duplicados++;
            }
            if (listaPermisosObjetosDB.get(i).getObjetodb().getModulo().getNombre().equals(duplicarPermisosObjetosDB.getObjetodb().getModulo().getNombre())) {
                duplicados++;
            }
            if (listaPermisosObjetosDB.get(i).getObjetodb().getClasificacion().equals(duplicarPermisosObjetosDB.getObjetodb().getClasificacion())) {
                duplicados++;
            }
        }

        if (contador != 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevoPerPantalla");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevoPerPantalla').show()");
        }
        if (duplicados != 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:existeObjeto");
            RequestContext.getCurrentInstance().execute("PF('existeObjeto').show()");
        }

        if (contador == 0 && duplicados == 0) {
            if (bandera == 1) {
                altoTablaObjeto = "50";
                FacesContext c = FacesContext.getCurrentInstance();
                nomobjetop = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosObjetos:nomobjetop");
                nomobjetop.setFilterStyle("display: none; visibility: hidden;");
                tipop = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosObjetos:tipop");
                tipop.setFilterStyle("display: none; visibility: hidden;");
                modulo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosObjetos:modulo");
                modulo.setFilterStyle("display: none; visibility: hidden;");
                clasificacion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosObjetos:clasificacion");
                clasificacion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosObjetos");
                bandera = 0;
                listaPermisosObjetosDBFiltrar = null;
                tipoLista = 0;
            }
            k++;
            l = BigInteger.valueOf(k);
            duplicarPermisosObjetosDB.setSecuencia(l);
            listaPermisosObjetosDBCrear.add(duplicarPermisosObjetosDB);
            listaPermisosObjetosDB.add(duplicarPermisosObjetosDB);
            permisosObjetosDBSeleccionado = duplicarPermisosObjetosDB;
            duplicarPermisosObjetosDB = new PermisosObjetosDB();
            contarRegistrosPermisosObjetos();
            RequestContext.getCurrentInstance().update("form:datosObjetos");
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroPerObjetos').hide()");
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
    }

    public void elegirDuplicar() {
        if (cualTabla == 1) {
            duplicandoPerfil();
        } else if (cualTabla == 2) {
            duplicandoPermisosPantallas();
        } else if (cualTabla == 3) {
            duplicandoPermisosObjetos();
        }
    }

    public void elegirBorrar() {
        if (cualTabla == 1) {
            borrarPerfiles();
        } else if (cualTabla == 2) {
            borrarPantallas();
        } else if (cualTabla == 3) {
            borrarObjetoDB();
        }
    }

    public void limpiarDuplicarPerfiles() {
        duplicarPerfil = new Perfiles();
    }

    public void limpiarDuplicarPermisosPantallas() {
        duplicarPermisosPantalla = new PermisosPantallas();
    }

    public void limpiarDuplicarPermisosObjetos() {
        duplicarPermisosObjetosDB = new PermisosObjetosDB();
    }

    public void exportPDF() throws IOException {
        if (cualTabla == 1) {
            DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosPerfilesExportar");
            FacesContext context = FacesContext.getCurrentInstance();
            Exporter exporter = new ExportarPDF();
            exporter.export(context, tabla, "PERFILES", false, false, "UTF-8", null, null);
            context.responseComplete();
        } else if (cualTabla == 2) {
            DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosPerPantallasExportar");
            FacesContext context = FacesContext.getCurrentInstance();
            Exporter exporter = new ExportarPDF();
            exporter.export(context, tabla, "PERMISOSPANTALLAS", false, false, "UTF-8", null, null);
            context.responseComplete();
        } else if (cualTabla == 3) {
            DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosPerObjetosExportar");
            FacesContext context = FacesContext.getCurrentInstance();
            Exporter exporter = new ExportarPDF();
            exporter.export(context, tabla, "PERMISOSOBJETOS", false, false, "UTF-8", null, null);
            context.responseComplete();
        }

    }

    public void exportXLS() throws IOException {
        if (cualTabla == 1) {
            DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosPerfilesExportar");
            FacesContext context = FacesContext.getCurrentInstance();
            Exporter exporter = new ExportarXLS();
            exporter.export(context, tabla, "PERFILES", false, false, "UTF-8", null, null);
            context.responseComplete();
        } else if (cualTabla == 2) {
            DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosPerPantallasExportar");
            FacesContext context = FacesContext.getCurrentInstance();
            Exporter exporter = new ExportarXLS();
            exporter.export(context, tabla, "PERMISOSPANTALLAS", false, false, "UTF-8", null, null);
            context.responseComplete();
        } else if (cualTabla == 3) {
            DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosPerObjetosExportar");
            FacesContext context = FacesContext.getCurrentInstance();
            Exporter exporter = new ExportarXLS();
            exporter.export(context, tabla, "PERMISOSOBJETOS", false, false, "UTF-8", null, null);
            context.responseComplete();
        }
    }

    public void verificarRastro() {
        if (cualTabla == 1) {
            if (perfilSeleccionado != null) {
                int resultado = administrarRastros.obtenerTabla(perfilSeleccionado.getSecuencia(), "PERFILES");
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
            } else if (administrarRastros.verificarHistoricosTabla("PERFILES")) {
                RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
            } else {
                RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
            }
        } else if (cualTabla == 2) {
            if (permisosPantallaSeleccionado != null) {
                int resultado = administrarRastros.obtenerTabla(permisosPantallaSeleccionado.getSecuencia(), "PERMISOSPANTALLAS");
                if (resultado == 1) {
                    RequestContext.getCurrentInstance().execute("PF('errorObjetosDBPantallas').show()");
                } else if (resultado == 2) {
                    RequestContext.getCurrentInstance().execute("PF('confirmarRastroPantallas').show()");
                } else if (resultado == 3) {
                    RequestContext.getCurrentInstance().execute("PF('errorRegistroRastro').show()");
                } else if (resultado == 4) {
                    RequestContext.getCurrentInstance().execute("PF('errorTablaConRastro').show()");
                } else if (resultado == 5) {
                    RequestContext.getCurrentInstance().execute("PF('errorTablaSinRastro').show()");
                }
            } else if (administrarRastros.verificarHistoricosTabla("PERMISOSPANTALLAS")) {
                RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistoricoPantallas').show()");
            } else {
                RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
            }
        } else if (cualTabla == 3) {
            if (permisosObjetosDBSeleccionado != null) {
                int resultado = administrarRastros.obtenerTabla(permisosObjetosDBSeleccionado.getSecuencia(), "PERMISOSOBJETOSDB");
                if (resultado == 1) {
                    RequestContext.getCurrentInstance().execute("PF('errorObjetosDBObjetos').show()");
                } else if (resultado == 2) {
                    RequestContext.getCurrentInstance().execute("PF('confirmarRastroObjetos').show()");
                } else if (resultado == 3) {
                    RequestContext.getCurrentInstance().execute("PF('errorRegistroRastro').show()");
                } else if (resultado == 4) {
                    RequestContext.getCurrentInstance().execute("PF('errorTablaConRastro').show()");
                } else if (resultado == 5) {
                    RequestContext.getCurrentInstance().execute("PF('errorTablaSinRastro').show()");
                }
            } else if (administrarRastros.verificarHistoricosTabla("PERMISOSOBJETOSDB")) {
                RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistoricoObjetos').show()");
            } else {
                RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
            }
        }
    }

    public void eventoFiltrar() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
        contarRegistrosPerfiles();
    }

    public void recrearPerfil() {
        try {
            Perfiles perfilaux = administrarPerfiles.consultarPerfilUsuario();
            if (perfilaux.getDescripcion().equals("ADMINISTRADOR")) {
                administrarPerfiles.ejcutarPKGEliminarPerfil(perfilSeleccionado.getDescripcion());
                administrarPerfiles.ejcutarPKGRecrearPerfil(perfilSeleccionado.getDescripcion(), perfilSeleccionado.getPwd());
                RequestContext.getCurrentInstance().execute("PF('operacionEnProceso').hide()");
                RequestContext.getCurrentInstance().execute("PF('perfilRecreado').show()");
            } else {
                RequestContext.getCurrentInstance().execute("PF('operacionEnProceso').hide()");
                RequestContext.getCurrentInstance().execute("PF('sinPrivilegios').show()");
            }
        } catch (Exception e) {
            log.warn("Error en RecrearPerfil : " + e.getMessage());
        }
    }

    public void asignarIndexPantallas(PermisosPantallas perpantalla, int dlg, int lnd) {
        permisosPantallaSeleccionado = perpantalla;
        tipoActualizacion = lnd;
        if (dlg == 0) {
            lovPermisosPantallas = null;
            cargarLovPermisosPantallas();
            contarRegistroLovPantallas();
            RequestContext.getCurrentInstance().update("formularioDialogos:permisosPantallasDialogo");
            RequestContext.getCurrentInstance().execute("PF('permisosPantallasDialogo').show()");
        }
    }

    public void asignarIndexObjetos(PermisosObjetosDB perobjetos, int dlg, int lnd) {
        permisosObjetosDBSeleccionado = perobjetos;
        tipoActualizacion = lnd;
        if (dlg == 0) {
            lovPermisosObjetosDB = null;
            cargarLovPermisosObjetosDB();
            contarRegistroLovObjetos();
            RequestContext.getCurrentInstance().update("formularioDialogos:permisosObjetosDialogo");
            RequestContext.getCurrentInstance().execute("PF('permisosObjetosDialogo').show()");
        }
    }

    public void listaValoresBoton() {
        if (cualTabla == 2) {
            lovPermisosPantallas = null;
            cargarLovPermisosPantallas();
            contarRegistroLovPantallas();
            RequestContext.getCurrentInstance().update("formularioDialogos:permisosPantallasDialogo");
            RequestContext.getCurrentInstance().execute("PF('permisosPantallasDialogo').show()");
        } else if (cualTabla == 3) {
            lovPermisosObjetosDB = null;
            cargarLovPermisosObjetosDB();
            contarRegistroLovObjetos();
            RequestContext.getCurrentInstance().update("formularioDialogos:permisosObjetosDialogo");
            RequestContext.getCurrentInstance().execute("PF('permisosObjetosDialogo').show()");
        }
    }

    public void actualizarPantalla() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            permisosPantallaSeleccionado.setObjetofrm(permisoPantallaLovSeleccionado.getObjetofrm());
            permisosPantallaSeleccionado.setPerfil(permisoPantallaLovSeleccionado.getPerfil());
            if (!listaPermisosPantallasCrear.contains(permisosPantallaSeleccionado)) {
                if (listaPermisosPantallasModificar.isEmpty()) {
                    listaPermisosPantallasModificar.add(permisosPantallaSeleccionado);
                } else if (!listaPermisosPantallasModificar.contains(permisosPantallaSeleccionado)) {
                    listaPermisosPantallasModificar.add(permisosPantallaSeleccionado);
                }
            }
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosPantallas");
        } else if (tipoActualizacion == 1) {
            nuevoPermisosPantalla.setObjetofrm(permisoPantallaLovSeleccionado.getObjetofrm());
            nuevoPermisosPantalla.setPerfil(permisoPantallaLovSeleccionado.getPerfil());
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoPermisoPantalla");
        } else if (tipoActualizacion == 2) {
            duplicarPermisosPantalla.setObjetofrm(permisoPantallaLovSeleccionado.getObjetofrm());
            duplicarPermisosPantalla.setPerfil(permisoPantallaLovSeleccionado.getPerfil());
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarPermisoPantalla");
        }
        lovPermisosPantallasFiltrar = null;
        permisoPantallaLovSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        RequestContext.getCurrentInstance().update("formularioDialogos:permisosPantallasDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovPermisosP");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarD");
        context.reset("formularioDialogos:lovPermisosP:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovPermisosP').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('permisosPantallasDialogo').hide()");
    }

    public void cancelarCambioPantalla() {
        lovPermisosPantallasFiltrar = null;
        permisoPantallaLovSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        RequestContext.getCurrentInstance().update("formularioDialogos:permisosPantallasDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovPermisosP");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarD");
        RequestContext.getCurrentInstance().reset("formularioDialogos:lovPermisosP:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovPermisosP').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('permisosPantallasDialogo').hide()");
    }

    public void actualizarObjeto() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            permisosObjetosDBSeleccionado.setObjetodb(permisoLovObjetosDBSeleccionado.getObjetodb());
            permisosObjetosDBSeleccionado.setPerfil(permisoLovObjetosDBSeleccionado.getPerfil());
            if (!listaPermisosObjetosDBCrear.contains(permisosObjetosDBSeleccionado)) {
                if (listaPermisosObjetosDBModificar.isEmpty()) {
                    listaPermisosObjetosDBModificar.add(permisosObjetosDBSeleccionado);
                } else if (!listaPermisosObjetosDBModificar.contains(permisosObjetosDBSeleccionado)) {
                    listaPermisosObjetosDBModificar.add(permisosObjetosDBSeleccionado);
                }
            }
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosObjetos");
        } else if (tipoActualizacion == 1) {
            nuevoPermisosObjetosDB.setObjetodb(permisoLovObjetosDBSeleccionado.getObjetodb());
            nuevoPermisosObjetosDB.setPerfil(permisoLovObjetosDBSeleccionado.getPerfil());
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoPermisoObjeto");
        } else if (tipoActualizacion == 2) {
            duplicarPermisosObjetosDB.setObjetodb(permisoLovObjetosDBSeleccionado.getObjetodb());
            duplicarPermisosObjetosDB.setPerfil(permisoLovObjetosDBSeleccionado.getPerfil());
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarPermisoObjeto");
        }
        lovPermisosObjetosDBFiltrar = null;
        permisoPantallaLovSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        RequestContext.getCurrentInstance().update("formularioDialogos:permisosPantallasDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovPermisosP");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarD");
        RequestContext.getCurrentInstance().reset("formularioDialogos:lovPermisosP:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovPermisosP').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('permisosPantallasDialogo').hide()");
    }

    public void cancelarCambioObjeto() {
        lovPermisosObjetosDBFiltrar = null;
        permisoLovObjetosDBSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        RequestContext.getCurrentInstance().update("formularioDialogos:permisosObjetosDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovObjetosP");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarO");
        RequestContext.getCurrentInstance().reset("formularioDialogos:lovObjetosP:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovObjetosP').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('permisosObjetosDialogo').hide()");
    }

    public void actualizarPantallaDialogo() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (!listaPermisosPantallas.isEmpty()) {
            listaPermisosPantallas.clear();
            listaPermisosPantallas.add(permisoPantallaLovSeleccionado);
        }
        aceptar = true;
        lovPermisosPantallasFiltrar = null;
        permisoPantallaLovSeleccionado = null;
        tipoActualizacion = -1;
        cualCeldaPantalla = -1;
        activarMtodos = false;
        contarRegistrosPermisosPantallas();
        RequestContext.getCurrentInstance().update("formularioDialogos:permisosPantallasLov");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovPermisosDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarDL");
        RequestContext.getCurrentInstance().reset("formularioDialogos:lovPermisosDialogo:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovPermisosDialogo').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('permisosPantallasLov').hide()");
        RequestContext.getCurrentInstance().update("form:datosPantallas");
        RequestContext.getCurrentInstance().update("form:btnMostrarTodos");
    }

    public void cancelarCambioPantallaDialogo() {
        lovPermisosPantallasFiltrar = null;
        permisoPantallaLovSeleccionado = null;
        tipoActualizacion = -1;
        cualCeldaPantalla = -1;
        RequestContext.getCurrentInstance().update("formularioDialogos:permisosPantallasLov");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovPermisosDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarDL");
        RequestContext.getCurrentInstance().reset("formularioDialogos:lovPermisosDialogo:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovPermisosDialogo').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('permisosPantallasLov').hide()");
    }

    public void mostrarTodasPantallas() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (!listaPermisosPantallas.isEmpty()) {
            listaPermisosPantallas.clear();
        }
        if (lovPermisosPantallas != null) {
            for (int i = 0; i < lovPermisosPantallas.size(); i++) {
                listaPermisosPantallas.add(lovPermisosPantallas.get(i));
            }
        }
        lovPermisosPantallasFiltrar = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        activarMtodos = true;
        contarRegistrosPermisosPantallas();
        RequestContext.getCurrentInstance().update("form:datosPantallas");
        RequestContext.getCurrentInstance().update("form:btnMostrarTodos");
    }

    public void actualizarBuscarObjetoDialogo() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (!listaPermisosObjetosDB.isEmpty()) {
            listaPermisosObjetosDB.clear();
            listaPermisosObjetosDB.add(permisoLovObjetosDBSeleccionado);
        }
        aceptar = true;
        lovPermisosObjetosDBFiltrar = null;
        permisoLovObjetosDBSeleccionado = null;
        tipoActualizacion = -1;
        cualCeldaObjetos = -1;
        activarMtodos2 = false;
        contarRegistrosPermisosObjetos();
        RequestContext.getCurrentInstance().update("formularioDialogos:buscarPermisosObjetosDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovBuscarObjetosP");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarObd");
        context.reset("formularioDialogos:lovBuscarObjetosP:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovBuscarObjetosP').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('buscarPermisosObjetosDialogo').hide()");
        RequestContext.getCurrentInstance().update("form:datosObjetos");
        RequestContext.getCurrentInstance().update("form:btnMostrarTodos2");
    }

    public void cancelarCambioBuscarObjetoDialogo() {
        lovPermisosObjetosDBFiltrar = null;
        permisoLovObjetosDBSeleccionado = null;
        tipoActualizacion = -1;
        cualCeldaObjetos = -1;
        RequestContext.getCurrentInstance().update("formularioDialogos:buscarPermisosObjetosDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovBuscarObjetosP");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarObd");
        RequestContext.getCurrentInstance().reset("formularioDialogos:lovBuscarObjetosP:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovBuscarObjetosP').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('buscarPermisosObjetosDialogo').hide()");
    }

    public void mostrarTodosObjetos() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (!listaPermisosObjetosDB.isEmpty()) {
            listaPermisosObjetosDB.clear();
        }
        if (lovPermisosObjetosDB != null) {
            for (int i = 0; i < lovPermisosObjetosDB.size(); i++) {
                listaPermisosObjetosDB.add(lovPermisosObjetosDB.get(i));
            }
        }
        lovPermisosObjetosDBFiltrar = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        activarMtodos2 = true;
        contarRegistrosPermisosObjetos();
        RequestContext.getCurrentInstance().update("form:datosObjetos");
        RequestContext.getCurrentInstance().update("form:btnMostrarTodos2");
    }

    public void contarRegistrosPerfiles() {
        RequestContext.getCurrentInstance().update("form:infoRegistroPerfil");
    }

    public void contarRegistrosPermisosPantallas() {
        RequestContext.getCurrentInstance().update("form:infoRegistroPerPantallas");
    }

    public void contarRegistrosPermisosObjetos() {
        RequestContext.getCurrentInstance().update("form:infoRegistroPerObjetos");
    }

    public void contarRegistroLovPantallas() {
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroPantallaLov");
    }

    public void contarRegistroPantallasDialogo() {
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroPantallaDialogo");
    }

    public void contarRegistroLovObjetos() {
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroObjetoLov");
    }

    public void contarRegistroLovBuscarObjetos() {
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroBuscarObjetoLov");
    }

    public void abrirLista(int listaV) {
        if (perfilSeleccionado != null) {
            if (listaV == 0) {
                lovPermisosPantallas = null;
                cargarLovPermisosPantallas();
                contarRegistroPantallasDialogo();
                RequestContext.getCurrentInstance().update("formularioDialogos:permisosPantallasLov");
                RequestContext.getCurrentInstance().execute("PF('permisosPantallasLov').show()");
            } else if (listaV == 1) {
                lovPermisosObjetosDB = null;
                cargarLovPermisosObjetosDB();
                contarRegistroLovBuscarObjetos();
                RequestContext.getCurrentInstance().update("formularioDialogos:buscarPermisosObjetosDialogo");
                RequestContext.getCurrentInstance().execute("PF('buscarPermisosObjetosDialogo').show()");
            }
        } else {
            RequestContext.getCurrentInstance().update("formularioDialogos:validacionInsertarPerfil");
            RequestContext.getCurrentInstance().execute("PF('validacionInsertarPerfil').show()");
        }
    }

    public void asignarAliasClon() {
        auxclon = perfilAliasSeleccionado;
        lovPerfilAliasFiltrar = null;
        perfilAliasSeleccionado = null;
        RequestContext.getCurrentInstance().update("form:aliasNombreClon");
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("formularioDialogos:aliasDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:LOVPerfilesAlias");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarAP");
        context.reset("formularioDialogos:LOVPerfilesAlias:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVPerfilesAlias').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('aliasDialogo').hide()");
    }

    public void cancelarCambioAlias() {
        lovPerfilAliasFiltrar = null;
        perfilAliasSeleccionado = null;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("formularioDialogos:aliasDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:LOVPerfilesAlias");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarAP");
        context.reset("formularioDialogos:LOVPerfilesAlias:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVPerfilesAlias').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('aliasDialogo').hide()");
    }

    public void lovPefiles() {
        lovPerfilAlias = null;
        getLovPerfilAlias();
        contarRegistrosAlias();
        RequestContext.getCurrentInstance().update("formularioDialogos:aliasDialogo");
        RequestContext.getCurrentInstance().execute("PF('aliasDialogo').show()");
    }

    public void contarRegistrosAlias() {
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroAlias");
    }

    public void clonarPermisosPantallas() {
        try {
            if (auxclon.getDescripcion().equals(" ") || auxclon.getDescripcion().isEmpty()) {
                RequestContext.getCurrentInstance().update("formularioDialogos:validacionClon");
                RequestContext.getCurrentInstance().execute("PF('validacionClon').show()");
            } else if (!auxclon.getDescripcion().equals("")) {
                administrarPerfiles.clonarPantallas(auxclon.getDescripcion());
                RequestContext.getCurrentInstance().execute("PF('pantallasClonadas').show()");
            }
        } catch (Exception e) {
            log.info("Controlador.ControlPerfiles.clonarPermisosPantallas()" + e.getMessage());
            RequestContext.getCurrentInstance().execute("PF('errorClonarPantalla').show()");
        }
    }

    public void clonarPermisosObjetos() {
        try {
            if (auxclon.getDescripcion().equals(" ") || auxclon.getDescripcion().isEmpty()) {
                RequestContext.getCurrentInstance().update("formularioDialogos:validacionClon");
                RequestContext.getCurrentInstance().execute("PF('validacionClon').show()");
            } else if (!auxclon.getDescripcion().equals("")) {
                administrarPerfiles.clonarPermisosObjetos(auxclon.getDescripcion());
                RequestContext.getCurrentInstance().execute("PF('permisosClonados').show()");
            }
        } catch (Exception e) {
            log.info("Controlador.ControlPerfiles.clonarPermisosObjetos()" + e.getMessage());
            RequestContext.getCurrentInstance().execute("PF('errorClonarPermisos').show()");
        }
    }

    public void cargarLovPermisosPantallas() {
        if (lovPermisosPantallas == null) {
            if (perfilSeleccionado != null) {
                lovPermisosPantallas = administrarPerfiles.consultarPermisosPantallas(perfilSeleccionado.getSecuencia());
            }
        }
    }

    public void cargarLovPermisosObjetosDB() {
        if (lovPermisosObjetosDB == null) {
            if (perfilSeleccionado != null) {
                lovPermisosObjetosDB = administrarPerfiles.consultarPermisosObjetos(perfilSeleccionado.getSecuencia());
            }
        }
    }

    /// métodos para generar reporte////////
    public AsynchronousFilllListener listener() {
        log.info(this.getClass().getName() + ".listener()");
        return new AsynchronousFilllListener() {
            //RequestContext context = c;

            @Override
            public void reportFinished(JasperPrint jp) {
                log.info(this.getClass().getName() + ".listener().reportFinished()");
                try {
                    estadoReporte = true;
                    resultadoReporte = "Exito";
                    //  RequestContext.getCurrentInstance().execute("PF('formularioDialogos:generandoReporte");
//                    generarArchivoReporte(jp);
                } catch (Exception e) {
                    log.info("ControlNReporteNomina reportFinished ERROR: " + e.toString());
                }
            }

            @Override
            public void reportCancelled() {
                log.info(this.getClass().getName() + ".listener().reportCancelled()");
                estadoReporte = true;
                resultadoReporte = "Cancelación";
            }

            @Override
            public void reportFillError(Throwable e) {
                log.info(this.getClass().getName() + ".listener().reportFillError()");
                if (e.getCause() != null) {
                    pathReporteGenerado = "ControlInterfaseContableTotal reportFillError Error: " + e.toString() + "\n" + e.getCause().toString();
                } else {
                    pathReporteGenerado = "ControlInterfaseContableTotal reportFillError Error: " + e.toString();
                }
                estadoReporte = true;
                resultadoReporte = "Se estallo";
            }
        };
    }

    public void validarDescargaReportePantallas() {
        try {
            log.info(this.getClass().getName() + ".validarDescargaReporte()");
            RequestContext.getCurrentInstance().execute("PF('generandoReporte').show()");
            RequestContext context = RequestContext.getCurrentInstance();
            nombreReporte = "segpantallas";
            tipoReporte = "PDF";
            log.info("nombre reporte : " + nombreReporte);
            log.info("tipo reporte: " + tipoReporte);

            pathReporteGenerado = administarReportes.generarReportePantallas(nombreReporte, tipoReporte);
            RequestContext.getCurrentInstance().execute("PF('generandoReporte').hide()");
            if (pathReporteGenerado != null && !pathReporteGenerado.startsWith("Error:")) {
                log.info("validar descarga reporte - ingreso al if 1");
                if (tipoReporte.equals("PDF")) {
                    log.info("validar descarga reporte - ingreso al if 2 else");
                    FileInputStream fis;
                    try {
                        log.info("pathReporteGenerado : " + pathReporteGenerado);
                        fis = new FileInputStream(new File(pathReporteGenerado));
                        log.info("fis : " + fis);
                        reporte = new DefaultStreamedContent(fis, "application/pdf");
                        log.info("reporte despues de esto : " + reporte);
                        if (reporte != null) {
                            log.info("userAgent: " + userAgent);
                            log.info("validar descarga reporte - ingreso al if 4");
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
                        log.warn("Error en validarDescargarReporte : " + ex.getMessage());
                        RequestContext.getCurrentInstance().update("formularioDialogos:errorGenerandoReporte");
                        RequestContext.getCurrentInstance().execute("PF('errorGenerandoReporte').show()");
                        reporte = null;
                    }
                }
            } else {
                log.info("validar descarga reporte - ingreso al if 1 else");
                RequestContext.getCurrentInstance().update("formularioDialogos:errorGenerandoReporte");
                RequestContext.getCurrentInstance().execute("PF('errorGenerandoReporte').show()");
            }
        } catch (Exception e) {
            log.warn("Error en validar descargar Reporte " + e.toString());
        }
    }

    public void validarDescargaReporteObjetos() {
        try {
            log.info(this.getClass().getName() + ".validarDescargaReporte()");
            RequestContext.getCurrentInstance().execute("PF('generandoReporte').show()");
            RequestContext context = RequestContext.getCurrentInstance();
            nombreReporte = "segobjetos";
            tipoReporte = "PDF";
            log.info("nombre reporte : " + nombreReporte);
            log.info("tipo reporte: " + tipoReporte);

            pathReporteGenerado = administarReportes.generarReporteObjetos(nombreReporte, tipoReporte);
            RequestContext.getCurrentInstance().execute("PF('generandoReporte').hide()");
            if (pathReporteGenerado != null && !pathReporteGenerado.startsWith("Error:")) {
                log.info("validar descarga reporte - ingreso al if 1");
                if (tipoReporte.equals("PDF")) {
                    log.info("validar descarga reporte - ingreso al if 2 else");
                    FileInputStream fis;
                    try {
                        log.info("pathReporteGenerado : " + pathReporteGenerado);
                        fis = new FileInputStream(new File(pathReporteGenerado));
                        log.info("fis : " + fis);
                        reporte = new DefaultStreamedContent(fis, "application/pdf");
                        log.info("reporte despues de esto : " + reporte);
                        if (reporte != null) {
                            log.info("userAgent: " + userAgent);
                            log.info("validar descarga reporte - ingreso al if 4");
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
                        log.info("validar descarga reporte - ingreso al catch 1");
                        log.info(ex);
                        reporte = null;
                    }
                }
            } else {
                log.info("validar descarga reporte - ingreso al if 1 else");
                RequestContext.getCurrentInstance().update("formularioDialogos:errorGenerandoReporte");
                RequestContext.getCurrentInstance().execute("PF('errorGenerandoReporte').show()");
            }
        } catch (Exception e) {
            log.warn("Error en validar descargar Reporte " + e.toString());
        }
    }

    public void exportarReporte() throws IOException {
        try {
            log.info("Controlador.ControlInterfaseContableTotal.exportarReporte()   path generado : " + pathReporteGenerado);
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
            log.warn("Error en exportarReporte :" + e.getMessage());
            RequestContext.getCurrentInstance().update("formularioDialogos:errorGenerandoReporte");
            RequestContext.getCurrentInstance().execute("PF('errorGenerandoReporte').show()");
        }
    }

    public void limpiarExportar() {
        if (cualTabla == 1) {
            limpiarNuevoPerfil();
        } else if (cualTabla == 2) {
            limpiarNuevoPermisoPantalla();
        } else if (cualTabla == 3) {
            limpiarNuevoPermisoObjetos();
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    ///////////////////sets y gets/////////////////
    public List<Perfiles> getListaPerfiles() {
        if (listaPerfiles == null) {
            listaPerfiles = administrarPerfiles.consultarPerfiles();
        }
        return listaPerfiles;
    }

    public void setListaPerfiles(List<Perfiles> listaPerfiles) {
        this.listaPerfiles = listaPerfiles;
    }

    public List<Perfiles> getListaPerfilesFiltrar() {
        return listaPerfilesFiltrar;
    }

    public void setListaPerfilesFiltrar(List<Perfiles> listaPerfilesFiltrar) {
        this.listaPerfilesFiltrar = listaPerfilesFiltrar;
    }

    public Perfiles getPerfilSeleccionado() {
        return perfilSeleccionado;
    }

    public void setPerfilSeleccionado(Perfiles perfilSeleccionado) {
        this.perfilSeleccionado = perfilSeleccionado;
    }

    public Perfiles getDuplicarPerfil() {
        return duplicarPerfil;
    }

    public void setDuplicarPerfil(Perfiles duplicarPerfil) {
        this.duplicarPerfil = duplicarPerfil;
    }

    public Perfiles getEditarPerfil() {
        return editarPerfil;
    }

    public void setEditarPerfil(Perfiles editarPerfil) {
        this.editarPerfil = editarPerfil;
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

    public String getInfoRegistroPerfil() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosPerfiles");
        infoRegistroPerfil = String.valueOf(tabla.getRowCount());
        return infoRegistroPerfil;
    }

    public void setInfoRegistroPerfil(String infoRegistroPerfil) {
        this.infoRegistroPerfil = infoRegistroPerfil;
    }

    public boolean isActivarLov() {
        return activarLov;
    }

    public void setActivarLov(boolean activarLov) {
        this.activarLov = activarLov;
    }

    public String getPaginaAnterior() {
        return paginaAnterior;
    }

    public void setPaginaAnterior(String paginaAnterior) {
        this.paginaAnterior = paginaAnterior;
    }

    public Perfiles getNuevoPerfil() {
        return nuevoPerfil;
    }

    public void setNuevoPerfil(Perfiles nuevoPerfil) {
        this.nuevoPerfil = nuevoPerfil;
    }

    public List<PermisosPantallas> getListaPermisosPantallas() {
        if (listaPermisosPantallas == null) {
            if (perfilSeleccionado != null) {
                listaPermisosPantallas = administrarPerfiles.consultarPermisosPantallas(perfilSeleccionado.getSecuencia());
            } else {
                listaPermisosPantallas = new ArrayList<PermisosPantallas>();
            }
        }
        return listaPermisosPantallas;
    }

    public void setListaPermisosPantallas(List<PermisosPantallas> listaPermisosPantallas) {
        this.listaPermisosPantallas = listaPermisosPantallas;
    }

    public PermisosPantallas getPermisosPantallaSeleccionado() {
        return permisosPantallaSeleccionado;
    }

    public void setPermisosPantallaSeleccionado(PermisosPantallas permisosPantallaSeleccionado) {
        this.permisosPantallaSeleccionado = permisosPantallaSeleccionado;
    }

    public PermisosPantallas getNuevoPermisosPantalla() {
        return nuevoPermisosPantalla;
    }

    public void setNuevoPermisosPantalla(PermisosPantallas nuevoPermisosPantalla) {
        this.nuevoPermisosPantalla = nuevoPermisosPantalla;
    }

    public PermisosPantallas getDuplicarPermisosPantalla() {
        return duplicarPermisosPantalla;
    }

    public void setDuplicarPermisosPantalla(PermisosPantallas duplicarPermisosPantalla) {
        this.duplicarPermisosPantalla = duplicarPermisosPantalla;
    }

    public PermisosPantallas getEditarPermisosPantallas() {
        return editarPermisosPantallas;
    }

    public void setEditarPermisosPantallas(PermisosPantallas editarPermisosPantallas) {
        this.editarPermisosPantallas = editarPermisosPantallas;
    }

    public List<PermisosObjetosDB> getListaPermisosObjetosDB() {
        if (listaPermisosObjetosDB == null) {
            if (perfilSeleccionado != null) {
                listaPermisosObjetosDB = administrarPerfiles.consultarPermisosObjetos(perfilSeleccionado.getSecuencia());
            } else {
                listaPermisosObjetosDB = new ArrayList<PermisosObjetosDB>();
            }
        }
        return listaPermisosObjetosDB;
    }

    public void setListaPermisosObjetosDB(List<PermisosObjetosDB> listaPermisosObjetosDB) {
        this.listaPermisosObjetosDB = listaPermisosObjetosDB;
    }

    public PermisosObjetosDB getPermisosObjetosDBSeleccionado() {
        return permisosObjetosDBSeleccionado;
    }

    public void setPermisosObjetosDBSeleccionado(PermisosObjetosDB permisosObjetosDBSeleccionado) {
        this.permisosObjetosDBSeleccionado = permisosObjetosDBSeleccionado;
    }

    public PermisosObjetosDB getNuevoPermisosObjetosDB() {
        return nuevoPermisosObjetosDB;
    }

    public void setNuevoPermisosObjetosDB(PermisosObjetosDB nuevoPermisosObjetosDB) {
        this.nuevoPermisosObjetosDB = nuevoPermisosObjetosDB;
    }

    public PermisosObjetosDB getDuplicarPermisosObjetosDB() {
        return duplicarPermisosObjetosDB;
    }

    public void setDuplicarPermisosObjetosDB(PermisosObjetosDB duplicarPermisosObjetosDB) {
        this.duplicarPermisosObjetosDB = duplicarPermisosObjetosDB;
    }

    public PermisosObjetosDB getEditarPermisosObjetosDB() {
        return editarPermisosObjetosDB;
    }

    public void setEditarPermisosObjetosDB(PermisosObjetosDB editarPermisosObjetosDB) {
        this.editarPermisosObjetosDB = editarPermisosObjetosDB;
    }

    public String getAltoTablaPantalla() {
        return altoTablaPantalla;
    }

    public void setAltoTablaPantalla(String altoTablaPantalla) {
        this.altoTablaPantalla = altoTablaPantalla;
    }

    public String getAltoTablaObjeto() {
        return altoTablaObjeto;
    }

    public void setAltoTablaObjeto(String altoTablaObjeto) {
        this.altoTablaObjeto = altoTablaObjeto;
    }

    public String getInfoRegistroPantallas() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosPantallas");
        infoRegistroPantallas = String.valueOf(tabla.getRowCount());
        return infoRegistroPantallas;
    }

    public void setInfoRegistroPantallas(String infoRegistroPantallas) {
        this.infoRegistroPantallas = infoRegistroPantallas;
    }

    public String getInfoRegistroObjetos() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosObjetos");
        infoRegistroObjetos = String.valueOf(tabla.getRowCount());
        return infoRegistroObjetos;
    }

    public void setInfoRegistroObjetos(String infoRegistroObjetos) {
        this.infoRegistroObjetos = infoRegistroObjetos;
    }

    public String getMensajeValidacion() {
        return mensajeValidacion;
    }

    public void setMensajeValidacion(String mensajeValidacion) {
        this.mensajeValidacion = mensajeValidacion;
    }

    public List<PermisosPantallas> getListaPermisosPantallasFiltrar() {
        return listaPermisosPantallasFiltrar;
    }

    public void setListaPermisosPantallasFiltrar(List<PermisosPantallas> listaPermisosPantallasFiltrar) {
        this.listaPermisosPantallasFiltrar = listaPermisosPantallasFiltrar;
    }

    public List<PermisosObjetosDB> getListaPermisosObjetosDBFiltrar() {
        return listaPermisosObjetosDBFiltrar;
    }

    public void setListaPermisosObjetosDBFiltrar(List<PermisosObjetosDB> listaPermisosObjetosDBFiltrar) {
        this.listaPermisosObjetosDBFiltrar = listaPermisosObjetosDBFiltrar;
    }

    public List<PermisosPantallas> getLovPermisosPantallas() {
        return lovPermisosPantallas;
    }

    public void setLovPermisosPantallas(List<PermisosPantallas> lovPermisosPantallas) {
        this.lovPermisosPantallas = lovPermisosPantallas;
    }

    public List<PermisosPantallas> getLovPermisosPantallasFiltrar() {
        return lovPermisosPantallasFiltrar;
    }

    public void setLovPermisosPantallasFiltrar(List<PermisosPantallas> lovPermisosPantallasFiltrar) {
        this.lovPermisosPantallasFiltrar = lovPermisosPantallasFiltrar;
    }

    public PermisosPantallas getPermisoPantallaLovSeleccionado() {
        return permisoPantallaLovSeleccionado;
    }

    public void setPermisoPantallaLovSeleccionado(PermisosPantallas permisoPantallaLovSeleccionado) {
        this.permisoPantallaLovSeleccionado = permisoPantallaLovSeleccionado;
    }

    public String getInfoRegistroPantallaLov() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovPermisosP");
        infoRegistroPantallaLov = String.valueOf(tabla.getRowCount());
        return infoRegistroPantallaLov;
    }

    public void setInfoRegistroPantallaLov(String infoRegistroPantallaLov) {
        this.infoRegistroPantallaLov = infoRegistroPantallaLov;
    }

    public List<PermisosObjetosDB> getLovPermisosObjetosDB() {
        return lovPermisosObjetosDB;
    }

    public void setLovPermisosObjetosDB(List<PermisosObjetosDB> lovPermisosObjetosDB) {
        this.lovPermisosObjetosDB = lovPermisosObjetosDB;
    }

    public List<PermisosObjetosDB> getLovPermisosObjetosDBFiltrar() {
        return lovPermisosObjetosDBFiltrar;
    }

    public void setLovPermisosObjetosDBFiltrar(List<PermisosObjetosDB> lovPermisosObjetosDBFiltrar) {
        this.lovPermisosObjetosDBFiltrar = lovPermisosObjetosDBFiltrar;
    }

    public PermisosObjetosDB getPermisoLovObjetosDBSeleccionado() {
        return permisoLovObjetosDBSeleccionado;
    }

    public void setPermisoLovObjetosDBSeleccionado(PermisosObjetosDB permisoLovObjetosDBSeleccionado) {
        this.permisoLovObjetosDBSeleccionado = permisoLovObjetosDBSeleccionado;
    }

    public String getInfoRegistroObjetoLov() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovObjetosP");
        infoRegistroObjetoLov = String.valueOf(tabla.getRowCount());
        return infoRegistroObjetoLov;
    }

    public void setInfoRegistroObjetoLov(String infoRegistroObjetoLov) {
        this.infoRegistroObjetoLov = infoRegistroObjetoLov;
    }

    public boolean isActivarMtodos() {
        return activarMtodos;
    }

    public void setActivarMtodos(boolean activarMtodos) {
        this.activarMtodos = activarMtodos;
    }

    public boolean isActivarMtodos2() {
        return activarMtodos2;
    }

    public void setActivarMtodos2(boolean activarMtodos2) {
        this.activarMtodos2 = activarMtodos2;
    }

    public String getInfoRegistroPantallaDialogo() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovPermisosDialogo");
        infoRegistroPantallaDialogo = String.valueOf(tabla.getRowCount());
        return infoRegistroPantallaDialogo;
    }

    public void setInfoRegistroPantallaDialogo(String infoRegistroPantallaDialogo) {
        this.infoRegistroPantallaDialogo = infoRegistroPantallaDialogo;
    }

    public String getInfoRegistroBuscarObjeto() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovBuscarObjetosP");
        infoRegistroBuscarObjeto = String.valueOf(tabla.getRowCount());
        return infoRegistroBuscarObjeto;
    }

    public void setInfoRegistroBuscarObjeto(String infoRegistroBuscarObjeto) {
        this.infoRegistroBuscarObjeto = infoRegistroBuscarObjeto;
    }

    public Perfiles getAuxclon() {
        return auxclon;
    }

    public void setAuxclon(Perfiles auxclon) {
        this.auxclon = auxclon;
    }

    public List<Perfiles> getLovPerfilAlias() {
        if (lovPerfilAlias == null) {
            lovPerfilAlias = administrarPerfiles.consultarPerfiles();
        }
        return lovPerfilAlias;
    }

    public void setLovPerfilAlias(List<Perfiles> lovPerfilAlias) {
        this.lovPerfilAlias = lovPerfilAlias;
    }

    public List<Perfiles> getLovPerfilAliasFiltrar() {
        return lovPerfilAliasFiltrar;
    }

    public void setLovPerfilAliasFiltrar(List<Perfiles> lovPerfilAliasFiltrar) {
        this.lovPerfilAliasFiltrar = lovPerfilAliasFiltrar;
    }

    public Perfiles getPerfilAliasSeleccionado() {
        return perfilAliasSeleccionado;
    }

    public void setPerfilAliasSeleccionado(Perfiles perfilAliasSeleccionado) {
        this.perfilAliasSeleccionado = perfilAliasSeleccionado;
    }

    public String getInfoRegistroAlias() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:LOVPerfilesAlias");
        infoRegistroAlias = String.valueOf(tabla.getRowCount());
        return infoRegistroAlias;
    }

    public void setInfoRegistroAlias(String infoRegistroAlias) {
        this.infoRegistroAlias = infoRegistroAlias;
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

    public String getTipoReporte() {
        return tipoReporte;
    }

    public void setTipoReporte(String tipoReporte) {
        this.tipoReporte = tipoReporte;
    }

    public String getCabezeraVisor() {
        return cabezeraVisor;
    }

    public void setCabezeraVisor(String cabezeraVisor) {
        this.cabezeraVisor = cabezeraVisor;
    }

}
