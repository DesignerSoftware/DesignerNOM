/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import ControlNavegacion.ControlListaNavegacion;
import Entidades.ObjetosBloques;
import Entidades.Perfiles;
import Entidades.PermisosPantallas;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarObjetosBloquesInterface;
import InterfaceAdministrar.AdministrarPerfilesInterface;
import InterfaceAdministrar.AdministrarPermisosPantallasInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
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
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import javax.faces.context.FacesContext;
import javax.inject.Named;
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
@ManagedBean
@SessionScoped
public class ControlPermisosPantallas implements Serializable {

    private static Logger log = Logger.getLogger(ControlPermisosPantallas.class);

    @EJB
    AdministrarPermisosPantallasInterface administrarPermisos;
    @EJB
    AdministrarObjetosBloquesInterface administrarObjetosBloques;
    @EJB
    AdministrarPerfilesInterface administrarPerfiles;
    @EJB
    AdministrarRastrosInterface administrarRastros;

    private List<PermisosPantallas> listaPermisosPantallas;
    private List<PermisosPantallas> listaPermisosPantallasFiltrar;
    private List<PermisosPantallas> listaPermisosPantallasCrear;
    private List<PermisosPantallas> listaPermisosPantallasModificar;
    private List<PermisosPantallas> listaPermisosPantallasBorrar;
    private PermisosPantallas nuevoPermisoPantalla;
    private PermisosPantallas duplicarPermisoPantalla;
    private PermisosPantallas editarPermisoPantalla;
    private PermisosPantallas permisoPantallaSeleccionado;
    //lov objetos bloques
    private List<ObjetosBloques> lovObjetosBloques;
    private List<ObjetosBloques> lovObjetosBloquesFiltrar;
    private ObjetosBloques objetoBloqueSeleccionado;
    // lov perfiles
    private List<Perfiles> lovPerfiles;
    private List<Perfiles> lovPerfilesFiltrar;
    private Perfiles perfilesSeleccionado;
    //
    private int cualCelda, tipoLista, tipoActualizacion, k, bandera;
    private BigInteger l;
    private boolean aceptar, guardado;
    private Column descripcion, nombre, tipoC, select, delete, enable, insert, update;
    private String altoTabla, altoTablaReg, altoTablaH;
    private String perfil, objetofrm, tipo, s, d, e, i, u;
    //
    private String infoRegistro, infoRegistroObjetosBloques, infoRegistroPerfiles;
    private String mensajeValidacion;
    private String paginaAnterior = "nominaf";
    private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();
    private Map<BigInteger, Object> mapTipos = new LinkedHashMap<>();
    private String msgError;
    private boolean activarLov, permitirIndex;
    private DataTable tabla;

    public ControlPermisosPantallas() {
        listaPermisosPantallas = null;
        listaPermisosPantallasBorrar = new ArrayList<PermisosPantallas>();
        listaPermisosPantallasCrear = new ArrayList<PermisosPantallas>();
        listaPermisosPantallasFiltrar = new ArrayList<PermisosPantallas>();
        listaPermisosPantallasModificar = new ArrayList<PermisosPantallas>();
        editarPermisoPantalla = new PermisosPantallas();
        nuevoPermisoPantalla = new PermisosPantallas();
        nuevoPermisoPantalla.setPerfil(new Perfiles());
        nuevoPermisoPantalla.setObjetofrm(new ObjetosBloques());
        duplicarPermisoPantalla = new PermisosPantallas();
        guardado = true;
        mapParametros.put("paginaAnterior", paginaAnterior);
//        altoTabla = "315";
        activarLov = true;
        permitirIndex = true;
        permisoPantallaSeleccionado = null;
    }

    public void recibirPaginaEntrante(String pagina) {
        paginaAnterior = pagina;
        listaPermisosPantallas = null;
        getListaPermisosPantallas();
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
        String pagActual = "permisospantallas";
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
        lovObjetosBloques = null;
        lovPerfiles = null;
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
            administrarPermisos.obtenerConexion(ses.getId());
            administrarPerfiles.obtenerConexion(ses.getId());
            administrarObjetosBloques.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
            listaPermisosPantallas = null;
            getListaPermisosPantallas();
//            if (listaObjetosBloques != null) {
//                objetoBloqueSeleccionado = listaObjetosBloques.get(0);
//            }

        } catch (Exception e) {
            log.error("Error postconstruct " + this.getClass().getName() + ":  ", e);
            log.error("Causa: " + e.getCause());
        }
    }

    public void cambiarIndice(PermisosPantallas objeto, int celda) {
        System.out.println("Controlador.ControlPermisosPantallas.cambiarIndice()");
        if (permitirIndex == true) {
            permisoPantallaSeleccionado = objeto;
            cualCelda = celda;
            switch (cualCelda) {
                case 0:
                    activarLov = false;
                    perfil = permisoPantallaSeleccionado.getPerfil().getDescripcion();
                    break;
                case 1:
                    activarLov = false;
                    objetofrm = permisoPantallaSeleccionado.getObjetofrm().getNombre();
                    break;
                default:
                    activarLov = true;
                    break;
            }
        }
        RequestContext.getCurrentInstance().update("form:listaValores");
    }

    public void recordarSeleccion() {
        System.out.println("Controlador.ControlPermisosPantallas.recordarSeleccion()");
        if (permisoPantallaSeleccionado != null) {
            FacesContext c = FacesContext.getCurrentInstance();
            tabla = (DataTable) c.getViewRoot().findComponent("form:datosPermisosPantallas");
            tabla.setSelection(permisoPantallaSeleccionado);
        }
    }

    public void asignarIndex(PermisosPantallas objeto, int dlg, int lnd) {
        permisoPantallaSeleccionado = objeto;
        tipoActualizacion = lnd;
        if (dlg == 0) {
            lovPerfiles = null;
            getLovPerfiles();
            contarRegistrosPerfil();
            RequestContext.getCurrentInstance().update("formularioDialogos:perfilesDialogo");
            RequestContext.getCurrentInstance().execute("PF('perfilesDialogo').show()");
        } else if (dlg == 1) {
            lovObjetosBloques = null;
            getLovObjetosBloques();
            contarRegistrosObjetosFRM();
            RequestContext.getCurrentInstance().update("formularioDialogos:objetosBloquesDialogo");
            RequestContext.getCurrentInstance().execute("PF('objetosBloquesDialogo').show()");
        }

    }

    public void valoresBackupAutocompletarGeneral(int tipoNuevo, String Campo) {
        if (Campo.equals("PERFIL")) {
            if (tipoNuevo == 1) {
                perfil = nuevoPermisoPantalla.getPerfil().getDescripcion();
            } else if (tipoNuevo == 2) {
                perfil = duplicarPermisoPantalla.getPerfil().getDescripcion();
            }
        }
        if (Campo.equals("OBJETOFRM")) {
            if (tipoNuevo == 1) {
                objetofrm = nuevoPermisoPantalla.getObjetofrm().getNombre();
            } else if (tipoNuevo == 2) {
                objetofrm = duplicarPermisoPantalla.getObjetofrm().getNombre();
            }
        }
    }

    public void autocompletarNuevoyDuplicadoPermiso(String column, String valor, int tipoNuevo) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (column.equalsIgnoreCase("PERFIL")) {
            if (!valor.isEmpty()) {
                if (tipoNuevo == 1) {
                    nuevoPermisoPantalla.getPerfil().setDescripcion(perfil);
                } else if (tipoNuevo == 2) {
                    duplicarPermisoPantalla.getPerfil().setDescripcion(perfil);
                }
                for (int i = 0; i < lovPerfiles.size(); i++) {
                    if (lovPerfiles.get(i).getDescripcion().startsWith(valor.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    if (tipoNuevo == 1) {
                        nuevoPermisoPantalla.setPerfil(lovPerfiles.get(indiceUnicoElemento));
                        RequestContext.getCurrentInstance().update("formularioDialogos:nuevoDescripcion");
                        RequestContext.getCurrentInstance().update("formularioDialogos:nuevoDescripcion");
                    } else if (tipoNuevo == 2) {
                        duplicarPermisoPantalla.setPerfil(lovPerfiles.get(indiceUnicoElemento));
                        RequestContext.getCurrentInstance().update("formularioDialogos:duplicarDescripcion");
                        RequestContext.getCurrentInstance().update("formularioDialogos:duplicarDescripcion");
                    }
                    lovPerfiles = null;
                    getLovPerfiles();
                } else {
                    RequestContext.getCurrentInstance().update("form:PerfilesDialogo");
                    RequestContext.getCurrentInstance().execute("PF('PerfilesDialogo').show()");
                    tipoActualizacion = tipoNuevo;
                    if (tipoNuevo == 1) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:nuevoDescripcion");
                    } else if (tipoNuevo == 2) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:duplicarDescripcion");
                    }
                }
            } else {
                lovPerfiles = null;
                getLovPerfiles();
                if (tipoNuevo == 1) {
                    nuevoPermisoPantalla.setPerfil(new Perfiles());
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoDescripcion");
                } else if (tipoNuevo == 2) {
                    duplicarPermisoPantalla.setPerfil(new Perfiles());
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarDescripcion");
                }
            }
        }
        if (column.equalsIgnoreCase("OBJETOFRM")) {
            if (!valor.isEmpty()) {
                if (tipoNuevo == 1) {
                    nuevoPermisoPantalla.getObjetofrm().setNombre(objetofrm);
                } else if (tipoNuevo == 2) {
                    duplicarPermisoPantalla.getObjetofrm().setNombre(objetofrm);
                }
                for (int i = 0; i < lovObjetosBloques.size(); i++) {
                    if (lovObjetosBloques.get(i).getNombre().startsWith(valor.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    if (tipoNuevo == 1) {
                        nuevoPermisoPantalla.setObjetofrm(lovObjetosBloques.get(indiceUnicoElemento));
                        RequestContext.getCurrentInstance().update("formularioDialogos:nuevoNombre");
                    } else if (tipoNuevo == 2) {
                        duplicarPermisoPantalla.setObjetofrm(lovObjetosBloques.get(indiceUnicoElemento));
                        RequestContext.getCurrentInstance().update("formularioDialogos:duplicarNombre");
                    }
                    lovObjetosBloques = null;
                    getLovObjetosBloques();
                } else {
                    RequestContext.getCurrentInstance().update("form:ObjetoFRMDialogo");
                    RequestContext.getCurrentInstance().execute("PF('ObjetoFRMDialogo').show()");
                    tipoActualizacion = tipoNuevo;
                    if (tipoNuevo == 1) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:nuevoNombre");
                    } else if (tipoNuevo == 2) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:duplicarNombre");
                    }
                }
            } else {
                lovObjetosBloques.clear();
                getLovObjetosBloques();
                if (tipoNuevo == 1) {
                    nuevoPermisoPantalla.setObjetofrm(new ObjetosBloques());
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoNombre");
                } else if (tipoNuevo == 2) {
                    duplicarPermisoPantalla.setObjetofrm(new ObjetosBloques());
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCargoGrupoViatico");
                }
            }
        }
    }

    public void listaValoresBoton() {
        if (cualCelda == 0) {
            lovPerfiles = null;
            getLovPerfiles();
            RequestContext.getCurrentInstance().update("formularioDialogos:perfilesDialogo");
            RequestContext.getCurrentInstance().execute("PF('modulosDialogo').show()");
        } else if (cualCelda == 1) {
            lovObjetosBloques = null;
            getLovObjetosBloques();
            RequestContext.getCurrentInstance().update("formularioDialogos:objetosBloquesDialogo");
            RequestContext.getCurrentInstance().execute("PF('modulosDialogo').show()");
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void cancelarModificacion() {
        RequestContext context = RequestContext.getCurrentInstance();
        restaurarTabla();
        listaPermisosPantallasBorrar.clear();
        listaPermisosPantallasCrear.clear();
        listaPermisosPantallasModificar.clear();
        k = 0;
        listaPermisosPantallas = null;
        permisoPantallaSeleccionado = null;
        guardado = true;
        permitirIndex = true;
        getListaPermisosPantallas();
        contarRegistros();
        context.update("form:datosPermisosPantallas");
        context.execute("PF('datosPermisosPantallas').clearFilters()");
        context.update("form:ACEPTAR");
        context.update("form:listaValores");

    }

    private void restaurarTabla() {
        if (bandera == 1) {
            //CERRAR FILTRADO
            altoTabla = "315";
            descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosPermisosPantallas:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            nombre = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosPermisosPantallas:nombre");
            nombre.setFilterStyle("display: none; visibility: hidden;");
            tipoC = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosPermisosPantallas:tipo");
            tipoC.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosPermisosPantallas");
            bandera = 0;
            listaPermisosPantallasFiltrar = null;
            tipoLista = 0;
        }
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
            descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosPermisosPantallas:descripcion");
            descripcion.setFilterStyle("width: 85% !important;");
            nombre = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosPermisosPantallas:nombre");
            nombre.setFilterStyle("width: 85% !important;");
            tipoC = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosPermisosPantallas:tipo");
            tipoC.setFilterStyle("width: 85% !important;");
            RequestContext.getCurrentInstance().update("form:datosPermisosPantallas");
            bandera = 1;
        } else if (bandera == 1) {
            restaurarTabla();
        }
    }

    public void seleccionarComboBox(PermisosPantallas objeto, String estado) {
        permisoPantallaSeleccionado = objeto;
        switch (estado) {
            case "BOTON":
                permisoPantallaSeleccionado.setTipo("BOTON");
                break;
            case "CASILLA":
                permisoPantallaSeleccionado.setTipo("CASILLA");
                break;
            case "MOSTRADO":
                permisoPantallaSeleccionado.setTipo("MOSTRADO");
                break;
            case "TEXTO":
                permisoPantallaSeleccionado.setTipo("TEXTO");
            case " ":
                permisoPantallaSeleccionado.setTipo(" ");
                break;
            default:
                break;
        }
        modificarPermisosPantallas(permisoPantallaSeleccionado);
    }

    public void modificarPermisosPantallas(PermisosPantallas objeto) {
        permisoPantallaSeleccionado = objeto;
        if (!listaPermisosPantallasCrear.contains(permisoPantallaSeleccionado)) {
            if (listaPermisosPantallasModificar.isEmpty()) {
                listaPermisosPantallasModificar.add(permisoPantallaSeleccionado);
            } else if (!listaPermisosPantallasModificar.contains(permisoPantallaSeleccionado)) {
                listaPermisosPantallasModificar.add(permisoPantallaSeleccionado);
            }
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
        RequestContext.getCurrentInstance().update("form:datosPermisosPantallas");
    }

    public void borrarPermisosPantallas() {
        if (permisoPantallaSeleccionado != null) {
            if (!listaPermisosPantallasModificar.isEmpty() && listaPermisosPantallasModificar.contains(permisoPantallaSeleccionado)) {
                int modIndex = listaPermisosPantallasModificar.indexOf(permisoPantallaSeleccionado);
                listaPermisosPantallasModificar.remove(modIndex);
                listaPermisosPantallasBorrar.add(permisoPantallaSeleccionado);
            } else if (!listaPermisosPantallasCrear.isEmpty() && listaPermisosPantallasCrear.contains(permisoPantallaSeleccionado)) {
                int crearIndex = listaPermisosPantallasCrear.indexOf(permisoPantallaSeleccionado);
                listaPermisosPantallasCrear.remove(crearIndex);
            } else {
                listaPermisosPantallasBorrar.add(permisoPantallaSeleccionado);
            }
            listaPermisosPantallas.remove(permisoPantallaSeleccionado);
            if (tipoLista == 1) {
                listaPermisosPantallasFiltrar.remove(permisoPantallaSeleccionado);
            }
            permisoPantallaSeleccionado = null;
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosPermisosPantallas");
        }
    }

    public void revisarDialogoGuardar() {
        if (!listaPermisosPantallasBorrar.isEmpty() || !listaPermisosPantallasCrear.isEmpty() || !listaPermisosPantallasModificar.isEmpty()) {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:confirmarGuardar");
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
        }
    }

    public void modificacionTipo(PermisosPantallas tipoPermiso, String tipo) {
        permisoPantallaSeleccionado = tipoPermiso;
        permisoPantallaSeleccionado.setTipo(tipo);
        guardado = false;
        if (listaPermisosPantallasModificar.isEmpty() || !listaPermisosPantallasModificar.contains(permisoPantallaSeleccionado)) {
            listaPermisosPantallasModificar.add(permisoPantallaSeleccionado);
            mapTipos.put(permisoPantallaSeleccionado.getSecuencia(), permisoPantallaSeleccionado.getTipo());
        }
        int n = listaPermisosPantallasModificar.indexOf(permisoPantallaSeleccionado);
        listaPermisosPantallasModificar.get(n).setTipo(tipo);
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void guardarPermisoPantalla() {
        RequestContext context = RequestContext.getCurrentInstance();
        try {
            if (guardado == false) {
                msgError = "";
                if (!listaPermisosPantallasBorrar.isEmpty()) {
                    for (int i = 0; i < listaPermisosPantallasBorrar.size(); i++) {
                        msgError = administrarPermisos.borrarPermiso(listaPermisosPantallasBorrar.get(i));
                    }
                    listaPermisosPantallasBorrar.clear();
                }
                if (!listaPermisosPantallasCrear.isEmpty()) {
                    for (int i = 0; i < listaPermisosPantallasCrear.size(); i++) {
                        msgError = administrarPermisos.crearPermiso(listaPermisosPantallasCrear.get(i));
                    }
                    listaPermisosPantallasCrear.clear();
                }
                if (!listaPermisosPantallasModificar.isEmpty()) {
                    for (int i = 0; i < listaPermisosPantallasModificar.size(); i++) {
                        msgError = administrarPermisos.modificarPermiso(listaPermisosPantallasModificar.get(i));
                    }
                }
                listaPermisosPantallasModificar.clear();

                if (msgError.equals("EXITO")) {
                    listaPermisosPantallas = null;
                    getListaPermisosPantallas();
                    k = 0;
                    FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                    RequestContext.getCurrentInstance().update("form:growl");
                    contarRegistros();
                    guardado = true;
                    permisoPantallaSeleccionado = null;
                    RequestContext.getCurrentInstance().update("form:datosPermisosPantallas");
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
        if (permisoPantallaSeleccionado != null) {
            editarPermisoPantalla = permisoPantallaSeleccionado;
            RequestContext context = RequestContext.getCurrentInstance();
            switch (cualCelda) {
                case 1:
                    RequestContext.getCurrentInstance().update("formularioDialogos:editDescripcion");
                    RequestContext.getCurrentInstance().execute("PF('editDescripcion').show()");
                    cualCelda = -1;
                    break;
                case 2:
                    RequestContext.getCurrentInstance().update("formularioDialogos:editNombre");
                    RequestContext.getCurrentInstance().execute("PF('editNombre').show()");
                    cualCelda = -1;
                    break;
//                case 3:
//                    RequestContext.getCurrentInstance().update("formularioDialogos:editModulo");
//                    RequestContext.getCurrentInstance().execute("PF('editModulo').show()");
//                    cualCelda = -1;
//                    break;
                default:
                    break;
            }

        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void agregarNuevoPermisoPantalla() {
        int contador = 0;
        int duplicados = 0;

        Integer a = 0;
        a = null;
        mensajeValidacion = " ";

        if (nuevoPermisoPantalla.getPerfil().getSecuencia() == null || nuevoPermisoPantalla.getPerfil().getSecuencia().equals("")) {
            mensajeValidacion = " Los campos marcados con asterisco son obligatorios";
        } else {
            contador++;
        }

        if (nuevoPermisoPantalla.getObjetofrm().getSecuencia() == null || nuevoPermisoPantalla.getObjetofrm().getSecuencia().equals("")) {
            mensajeValidacion = " Los campos marcados con asterisco son obligatorios";
        } else {
            contador++;
        }

        if (nuevoPermisoPantalla.getTipo() == null) {
            nuevoPermisoPantalla.setTipo("");
        }
        Integer cont = administrarPermisos.conteoPantallas(nuevoPermisoPantalla.getPerfil().getSecuencia(), nuevoPermisoPantalla.getObjetofrm().getSecuencia());
        System.out.println("cont: " + cont);
        if (cont > 0) {
            mensajeValidacion="El dato ya esta registrado. por favor ingrese uno nuevo";
        }else{
            contador++;
            
        }

        if (contador == 3) {
            if (bandera == 1) {
                tipoC = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosPermisosPantallas:tipo");
                tipoC.setFilterStyle("display: none; visibility: hidden;");
                nombre = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosPermisosPantallas:nombre");
                nombre.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosPermisosPantallas:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosPermisosPantallas");
                bandera = 0;
                listaPermisosPantallasFiltrar = null;
                tipoLista = 0;
//                altoTabla = "315";
            }
            k++;
            l = BigInteger.valueOf(k);
            nuevoPermisoPantalla.setSecuencia(l);
            listaPermisosPantallasCrear.add(nuevoPermisoPantalla);
            listaPermisosPantallas.add(0, nuevoPermisoPantalla);
//            
//            for (int i = 0; i < listaPermisosPantallasCrear.size(); i++) {
//                msgError = administrarPermisos.crearPermiso(listaPermisosPantallasCrear.get(i));
//            }
//
//            if (msgError.equals("EXITO")) {
//                   duplicados++;
//                } else {
//                    mensajeValidacion = "El dato ya esta registrado. Por favor ingrese uno nuevo";
//                }
//            
//            if (duplicados == 1) {
//                if (bandera == 1) {
//                    tipoC = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosPermisosPantallas:tipo");
//                    tipoC.setFilterStyle("display: none; visibility: hidden;");
//                    nombre = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosPermisosPantallas:nombre");
//                    nombre.setFilterStyle("display: none; visibility: hidden;");
//                    descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosPermisosPantallas:descripcion");
//                    descripcion.setFilterStyle("display: none; visibility: hidden;");
//                    RequestContext.getCurrentInstance().update("form:datosPermisosPantallas");
//                    bandera = 0;
//                    listaPermisosPantallasFiltrar = null;
//                    tipoLista = 0;
////                altoTabla = "315";
//                }
            permisoPantallaSeleccionado = nuevoPermisoPantalla;
            RequestContext.getCurrentInstance().update("form:datosPermisosPantallas");
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().execute("PF('nuevoRegistroPermiso').hide()");
            nuevoPermisoPantalla = new PermisosPantallas();
        } else {
            RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevaCentroCosto");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevaCentroCosto').show()");
            contador = 0;
        }
//        } else {
//            RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevaCentroCosto");
//            RequestContext.getCurrentInstance().execute("PF('validacionNuevaCentroCosto').show()");
//            contador = 0;
//        }
    }

    public void limpiarNuevoPermisoPantalla() {
        nuevoPermisoPantalla = new PermisosPantallas();
    }

    public void duplicandoPermisoPantalla() {
        if (permisoPantallaSeleccionado != null) {
            duplicarPermisoPantalla = new PermisosPantallas();
            k++;
            l = BigInteger.valueOf(k);
            duplicarPermisoPantalla.setSecuencia(l);
            duplicarPermisoPantalla.setPerfil(permisoPantallaSeleccionado.getPerfil());
            duplicarPermisoPantalla.setObjetofrm(permisoPantallaSeleccionado.getObjetofrm());
            duplicarPermisoPantalla.setTipo(permisoPantallaSeleccionado.getTipo());
            duplicarPermisoPantalla.setD(permisoPantallaSeleccionado.getD());
            duplicarPermisoPantalla.setE(permisoPantallaSeleccionado.getE());
            duplicarPermisoPantalla.setI(permisoPantallaSeleccionado.getI());
            duplicarPermisoPantalla.setS(permisoPantallaSeleccionado.getS());
            duplicarPermisoPantalla.setU(permisoPantallaSeleccionado.getU());
//            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarRegistroPermiso");
//            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroPermiso').show();");
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarpp");
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroPermiso').show()");
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

        if (duplicarPermisoPantalla.getPerfil() == (null) || duplicarPermisoPantalla.getPerfil().equals("")) {
            mensajeValidacion = " Los campos marcados con asterisco son obligatorios";
        } else {
            contador++;
        }
        if (duplicarPermisoPantalla.getObjetofrm() == (null) || duplicarPermisoPantalla.getObjetofrm().equals("")) {
            mensajeValidacion = " Los campos marcados con asterisco son obligatorios";
        } else {
            contador++;
        }

        if (contador == 2) {
            listaPermisosPantallas.add(0, duplicarPermisoPantalla);
            listaPermisosPantallasCrear.add(duplicarPermisoPantalla);
            permisoPantallaSeleccionado = duplicarPermisoPantalla;
            RequestContext.getCurrentInstance().update("form:datosPermisosPantallas");
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            if (bandera == 1) {
                tipoC = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosPermisosPantallas:tipo");
                tipoC.setFilterStyle("display: none; visibility: hidden;");
                nombre = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosPermisosPantallas:nombre");
                nombre.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosPermisosPantallas:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosPermisosPantallas");
                bandera = 0;
                listaPermisosPantallasFiltrar = null;
                tipoLista = 0;
                altoTabla = "270";
            }
            duplicarPermisoPantalla = new PermisosPantallas();
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroPermiso').hide()");

        } else {
            contador = 0;
            RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
            RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
        }
    }

    public void limpiarDuplicarPermisosPantallas() {
        duplicarPermisoPantalla = new PermisosPantallas();
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosPermisosPantallasExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "PermisosPantallas", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosPermisosPantallasExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "PermisosPantallas", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void verificarRastro() {
        if (permisoPantallaSeleccionado != null) {
            int resultado = administrarRastros.obtenerTabla(permisoPantallaSeleccionado.getSecuencia(), "PERMISOSPANTALLAS"); //En ENCARGATURAS lo cambia por el nombre de su tabla
            switch (resultado) {
                case 1:
                    RequestContext.getCurrentInstance().execute("PF('errorPermisoPantalla').show()");
                    break;
                case 2:
                    RequestContext.getCurrentInstance().execute("PF('confirmarRastro').show()");
                    break;
                case 3:
                    RequestContext.getCurrentInstance().execute("PF('errorRegistroRastro').show()");
                    break;
                case 4:
                    RequestContext.getCurrentInstance().execute("PF('errorTablaConRastro').show()");
                    break;
                case 5:
                    RequestContext.getCurrentInstance().execute("PF('errorTablaSinRastro').show()");
                    break;
                default:
                    break;
            }
        } else if (administrarRastros.verificarHistoricosTabla("PERMISOSPANTALLAS")) { // igual acá
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
    }

    public void salir() {
        limpiarListasValor();
        restaurarTabla();
        activarLov = true;
        listaPermisosPantallasBorrar.clear();
        listaPermisosPantallasCrear.clear();
        listaPermisosPantallasModificar.clear();
        objetoBloqueSeleccionado = null;
        k = 0;
        listaPermisosPantallas = null;
        guardado = true;
        RequestContext.getCurrentInstance().update("form:datosPermisosPantallas");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        navegar("atras");
    }

    public void contarRegistros() {
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
    }

    public void contarRegistrosPerfil() {
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroPerfil");
    }

    public void contarRegistrosObjetosFRM() {
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroObjetosFRM");
    }

    public void habilitarBotonLov() {
        activarLov = false;
        RequestContext.getCurrentInstance().update("form:listaValores");
    }

    public void deshabilitarBotonLov() {
        activarLov = true;
        RequestContext.getCurrentInstance().update("form:listaValores");

    }

    public void actualizarPerfil() {
        switch (tipoActualizacion) {
            case 0:
                System.out.println("Ingrese caso 0");
                permisoPantallaSeleccionado.setPerfil(perfilesSeleccionado);
                if (!listaPermisosPantallasCrear.contains(permisoPantallaSeleccionado)) {
                    if (listaPermisosPantallasModificar.isEmpty()) {
                        listaPermisosPantallasModificar.add(permisoPantallaSeleccionado);
                    } else if (!listaPermisosPantallasModificar.contains(permisoPantallaSeleccionado)) {
                        listaPermisosPantallasModificar.add(permisoPantallaSeleccionado);
                    }
                }
                if (guardado == true) {
                    guardado = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
                permitirIndex = true;
                RequestContext.getCurrentInstance().update("form:datosPermisosPantallas");
                break;
            case 1:
                System.out.println("Ingrese caso 1");
                nuevoPermisoPantalla.setPerfil(perfilesSeleccionado);
                RequestContext.getCurrentInstance().update("formularioDialogos:nuevoPermiso");
                break;
            case 2:
                System.out.println("Ingrese caso 2");
                duplicarPermisoPantalla.setPerfil(perfilesSeleccionado);
                RequestContext.getCurrentInstance().update("formularioDialogos:duplicarpp");
                break;
            default:
                break;
        }
        lovPerfilesFiltrar = null;
        perfilesSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;

    }

    public void cancelarPerfil() {
        lovPerfilesFiltrar = null;
        perfilesSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        permitirIndex = true;
    }

    public void actualizarObjetoFRM() {
        switch (tipoActualizacion) {
            case 0:
                System.out.println("Ingrese caso 0");
                permisoPantallaSeleccionado.setObjetofrm(objetoBloqueSeleccionado);
                if (!listaPermisosPantallasCrear.contains(permisoPantallaSeleccionado)) {
                    if (listaPermisosPantallasModificar.isEmpty()) {
                        listaPermisosPantallasModificar.add(permisoPantallaSeleccionado);
                    } else if (!listaPermisosPantallasModificar.contains(permisoPantallaSeleccionado)) {
                        listaPermisosPantallasModificar.add(permisoPantallaSeleccionado);
                    }
                }
                if (guardado == true) {
                    guardado = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
                permitirIndex = true;
                RequestContext.getCurrentInstance().update("form:datosPermisosPantallas");
                break;
            case 1:
                System.out.println("Ingrese caso 1");
                nuevoPermisoPantalla.setObjetofrm(objetoBloqueSeleccionado);
                RequestContext.getCurrentInstance().update("formularioDialogos:nuevoPermiso");
                break;
            case 2:
                System.out.println("Ingrese caso 2");
                duplicarPermisoPantalla.setPerfil(perfilesSeleccionado);
                RequestContext.getCurrentInstance().update("formularioDialogos:duplicarpp");
                break;
            default:
                break;
        }
        lovObjetosBloquesFiltrar = null;
        objetoBloqueSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;

    }

    public void cancelarObjetosFRM() {
        lovObjetosBloquesFiltrar = null;
        objetoBloqueSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        permitirIndex = true;
    }

    //////////////GETS Y SETS////////
    public List<PermisosPantallas> getListaPermisosPantallas() {
        if (listaPermisosPantallas == null) {
            listaPermisosPantallas = administrarPermisos.consultarPermisosPantallas();
        }
        return listaPermisosPantallas;
    }

    public void setListaPermisosPantallas(List<PermisosPantallas> listaPermisosPantallas) {
        this.listaPermisosPantallas = listaPermisosPantallas;
    }

    public List<PermisosPantallas> getListaPermisosPantallasFiltrar() {
        return listaPermisosPantallasFiltrar;
    }

    public void setListaPermisosPantallasFiltrar(List<PermisosPantallas> listaPermisosPantallasFiltrar) {
        this.listaPermisosPantallasFiltrar = listaPermisosPantallasFiltrar;
    }

    public List<PermisosPantallas> getListaPermisosPantallasCrear() {
        return listaPermisosPantallasCrear;
    }

    public void setListaPermisosPantallasCrear(List<PermisosPantallas> listaPermisosPantallasCrear) {
        this.listaPermisosPantallasCrear = listaPermisosPantallasCrear;
    }

    public List<PermisosPantallas> getListaPermisosPantallasModificar() {
        return listaPermisosPantallasModificar;
    }

    public void setListaPermisosPantallasModificar(List<PermisosPantallas> listaPermisosPantallasModificar) {
        this.listaPermisosPantallasModificar = listaPermisosPantallasModificar;
    }

    public List<PermisosPantallas> getListaPermisosPantallasBorrar() {
        return listaPermisosPantallasBorrar;
    }

    public void setListaPermisosPantallasBorrar(List<PermisosPantallas> listaPermisosPantallasBorrar) {
        this.listaPermisosPantallasBorrar = listaPermisosPantallasBorrar;
    }

    public PermisosPantallas getNuevoPermisoPantalla() {
        return nuevoPermisoPantalla;
    }

    public void setNuevoPermisoPantalla(PermisosPantallas nuevoPermisoPantalla) {
        this.nuevoPermisoPantalla = nuevoPermisoPantalla;
    }

    public PermisosPantallas getDuplicarPermisoPantalla() {
        return duplicarPermisoPantalla;
    }

    public void setDuplicarPermisoPantalla(PermisosPantallas duplicarPermisoPantalla) {
        this.duplicarPermisoPantalla = duplicarPermisoPantalla;
    }

    public PermisosPantallas getEditarPermisoPantalla() {
        return editarPermisoPantalla;
    }

    public void setEditarPermisoPantalla(PermisosPantallas editarPermisoPantalla) {
        this.editarPermisoPantalla = editarPermisoPantalla;
    }

    public PermisosPantallas getPermisoPantallaSeleccionado() {
        return permisoPantallaSeleccionado;
    }

    public void setPermisoPantallaSeleccionado(PermisosPantallas permisoPantallaSeleccionado) {
        this.permisoPantallaSeleccionado = permisoPantallaSeleccionado;
    }

    public List<ObjetosBloques> getLovObjetosBloques() {
        if (lovObjetosBloques == null) {
            lovObjetosBloques = administrarObjetosBloques.consultarObjetosBloques();
        }
        return lovObjetosBloques;
    }

    public void setLovObjetosBloques(List<ObjetosBloques> lovObjetosBloques) {
        this.lovObjetosBloques = lovObjetosBloques;
    }

    public List<ObjetosBloques> getLovObjetosBloquesFiltrar() {
        return lovObjetosBloquesFiltrar;
    }

    public void setLovObjetosBloquesFiltrar(List<ObjetosBloques> lovObjetosBloquesFiltrar) {
        this.lovObjetosBloquesFiltrar = lovObjetosBloquesFiltrar;
    }

    public ObjetosBloques getObjetoBloqueSeleccionado() {
        return objetoBloqueSeleccionado;
    }

    public void setObjetoBloqueSeleccionado(ObjetosBloques objetoBloqueSeleccionado) {
        this.objetoBloqueSeleccionado = objetoBloqueSeleccionado;
    }

    public List<Perfiles> getLovPerfiles() {
        if (lovPerfiles == null) {
            lovPerfiles = administrarPerfiles.consultarPerfiles();
        }
        return lovPerfiles;
    }

    public void setLovPerfiles(List<Perfiles> lovPerfiles) {
        this.lovPerfiles = lovPerfiles;
    }

    public List<Perfiles> getLovPerfilesFiltrar() {
        return lovPerfilesFiltrar;
    }

    public void setLovPerfilesFiltrar(List<Perfiles> lovPerfilesFiltrar) {
        this.lovPerfilesFiltrar = lovPerfilesFiltrar;
    }

    public Perfiles getPerfilesSeleccionado() {
        return perfilesSeleccionado;
    }

    public void setPerfilesSeleccionado(Perfiles perfilesSeleccionado) {
        this.perfilesSeleccionado = perfilesSeleccionado;
    }

    public int getCualCelda() {
        return cualCelda;
    }

    public void setCualCelda(int cualCelda) {
        this.cualCelda = cualCelda;
    }

    public int getTipoLista() {
        return tipoLista;
    }

    public void setTipoLista(int tipoLista) {
        this.tipoLista = tipoLista;
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

    public String getAltoTablaReg() {
        descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosPermisosPantallas:descripcion");
        if (descripcion.getFilterStyle().startsWith("width: 85%")) {
            altoTablaReg = "" + 13;
        } else {
            bandera = 0;
            altoTablaReg = "" + 14;
        }
        return altoTablaReg;
    }

    public void setAltoTablaReg(String altoTablaReg) {
        this.altoTablaReg = altoTablaReg;
    }

    public String getAltoTablaH() {
        descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosPermisosPantallas:descripcion");
        if (descripcion.getFilterStyle().startsWith("width: 85%")) {
            altoTablaH = "" + 290;
        } else {
            bandera = 0;
            altoTablaH = "" + 310;
        }
        return altoTablaH;
    }

    public String getAltoTabla() {
        return altoTabla;
    }

    public void setAltoTabla(String altoTabla) {
        this.altoTabla = altoTabla;
    }

    public void setAltoTablaH(String altoTablaH) {
        this.altoTablaH = altoTablaH;
    }

    public String getInfoRegistro() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosPermisosPantallas");
        infoRegistro = String.valueOf(tabla.getRowCount());
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

    public String getInfoRegistroObjetosBloques() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovObjetosFRM");
        infoRegistroObjetosBloques = String.valueOf(tabla.getRowCount());
        return infoRegistroObjetosBloques;
    }

    public void setInfoRegistroObjetosBloques(String infoRegistroObjetosBloques) {
        this.infoRegistroObjetosBloques = infoRegistroObjetosBloques;
    }

    public String getInfoRegistroPerfiles() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovPerfiles");
        infoRegistroPerfiles = String.valueOf(tabla.getRowCount());
        return infoRegistroPerfiles;
    }

    public void setInfoRegistroPerfiles(String infoRegistroPerfiles) {
        this.infoRegistroPerfiles = infoRegistroPerfiles;
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
