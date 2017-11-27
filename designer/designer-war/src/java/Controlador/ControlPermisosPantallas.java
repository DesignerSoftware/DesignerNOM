///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package Controlador;
//
//import ControlNavegacion.ControlListaNavegacion;
//import Entidades.ObjetosBloques;
//import Entidades.ObjetosBloques;
//import Entidades.ObjetosBloques;
//import Entidades.Perfiles;
//import Entidades.PermisosPantallas;
//import InterfaceAdministrar.AdministrarPerfilesInterface;
//import InterfaceAdministrar.AdministrarPermisosPantallasInterface;
//import InterfaceAdministrar.AdministrarRastrosInterface;
//import javax.inject.Named;
//import javax.enterprise.context.SessionScoped;
//import java.io.Serializable;
//import java.math.BigInteger;
//import java.util.ArrayList;
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.Map;
//import javax.annotation.PostConstruct;
//import javax.annotation.PreDestroy;
//import javax.ejb.EJB;
//import javax.faces.context.FacesContext;
//import javax.servlet.http.HttpSession;
//import org.apache.log4j.Logger;
//import org.primefaces.component.column.Column;
//import org.primefaces.context.RequestContext;
//
///**
// *
// * @author user
// */
//@Named(value = "controlPermisosPantallas")
//@SessionScoped
//public class ControlPermisosPantallas implements Serializable {
//
//    private static Logger log = Logger.getLogger(ControlPermisosPantallas.class);
//
//    @EJB
//    AdministrarPermisosPantallasInterface administrarPermisos;
//    @EJB
//    AdministrarPerfilesInterface administrarPerfiles;
//    @EJB
//    AdministrarRastrosInterface administrarRastros;
//
//    private List<PermisosPantallas> listaPermisosPantallas;
//    private List<PermisosPantallas> listaPermisosPantallasFiltrar;
//    private List<PermisosPantallas> listaPermisosPantallasCrear;
//    private List<PermisosPantallas> listaPermisosPantallasModificar;
//    private List<PermisosPantallas> listaPermisosPantallasBorrar;
//    private PermisosPantallas nuevoPermisoPantalla;
//    private PermisosPantallas duplicarPermisoPantalla;
//    private PermisosPantallas editarPermisoPantalla;
//    private PermisosPantallas permisoPantallaSeleccionado;
//    //lov objetos bloques
//    private List<ObjetosBloques> lovObjetosBloques;
//    private List<ObjetosBloques> lovObjetosBloquesFiltrar;
//    private ObjetosBloques objetoBloqueSeleccionado;
//    // lov perfiles
//    private List<Perfiles> lovPerfiles;
//    private List<Perfiles> lovPerfilesFiltrar;
//    private Perfiles perfilesSeleccionado;
//    //
//    private int cualCelda, tipoLista, tipoActualizacion, k, bandera;
//    private BigInteger l;
//    private boolean aceptar, guardado;
//    private Column descripcion, nombre, tipo, select, delete, enable, insert, update;
//    private String altoTabla;
//    //
//    private String infoRegistro, infoRegistroObjetosBloques;
//    private String mensajeValidacion;
//    private String paginaAnterior = "nominaf";
//    private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();
//    private String msgError;
//    private boolean activarLov;
//
//    public ControlPermisosPantallas() {
//        listaPermisosPantallas = null;
//        listaPermisosPantallasCrear = new ArrayList<PermisosPantallas>();
//        listaPermisosPantallasModificar = new ArrayList<PermisosPantallas>();
//        listaPermisosPantallasBorrar = new ArrayList<PermisosPantallas>();
//        editarPermisoPantalla = new PermisosPantallas();
//        nuevoPermisoPantalla = new PermisosPantallas();
//        nuevoPermisoPantalla.setObjetofrm(new ObjetosBloques());
//        nuevoPermisoPantalla.setTipo("TABLE");
//        duplicarPermisoPantalla = new PermisosPantallas();
//        guardado = true;
//        mapParametros.put("paginaAnterior", paginaAnterior);
//        altoTabla = "315";
//        activarLov = true;
//    }
//
//    public void recibirPaginaEntrante(String pagina) {
//        paginaAnterior = pagina;
//        //inicializarCosas(); Inicializar cosas de ser necesario
//    }
//
//    public void recibirParametros(Map<String, Object> map) {
//        mapParametros = map;
//        paginaAnterior = (String) mapParametros.get("paginaAnterior");
//        //inicializarCosas(); Inicializar cosas de ser necesario
//    }
//
//    public void navegar(String pag) {
//        FacesContext fc = FacesContext.getCurrentInstance();
//        ControlListaNavegacion controlListaNavegacion = (ControlListaNavegacion) fc.getApplication().evaluateExpressionGet(fc, "#{controlListaNavegacion}", ControlListaNavegacion.class);
//        String pagActual = "objetosdb";
//        if (pag.equals("atras")) {
//            pag = paginaAnterior;
//            paginaAnterior = "nominaf";
//            controlListaNavegacion.quitarPagina(pagActual, this.getClass().getSimpleName());
//        } else {
//            controlListaNavegacion.guardarNavegacion(pagActual, pag);
//            fc.getApplication().getNavigationHandler().handleNavigation(fc, null, pag);
//        }
//        limpiarListasValor();
//    }
//
//    public void limpiarListasValor() {
//        lovObjetosBloques = null;
//    }
//
//    @PreDestroy
//    public void destruyendose() {
//        log.info(this.getClass().getName() + ".destruyendoce() @Destroy");
//    }
//
//    @PostConstruct
//    public void inicializarAdministrador() {
//        try {
//            FacesContext x = FacesContext.getCurrentInstance();
//            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
//            administrarPerfiles.obtenerConexion(ses.getId());
//            administrarPermisos.obtenerConexion(ses.getId());
//            administrarRastros.obtenerConexion(ses.getId());
//            listaPermisosPantallas = null;
//            getListaPermisosPantallas();
//            if (listaPermisosPantallas != null) {
//                permisoPantallaSeleccionado = listaPermisosPantallas.get(0);
//            }
//
//        } catch (Exception e) {
//            log.error("Error postconstruct " + this.getClass().getName() + ":  ", e);
//            log.error("Causa: " + e.getCause());
//        }
//    }
//
//    public void cambiarIndice(PermisosPantallas objeto, int celda) {
//        permisoPantallaSeleccionado = objeto;
//        cualCelda = celda;
//        permisoPantallaSeleccionado.getSecuencia();
//        if (cualCelda == 0) {
//            permisoPantallaSeleccionado.getPerfil().getDescripcion();
//            deshabilitarBotonLov();
//        } else if (cualCelda == 1) {
//            permisoPantallaSeleccionado.getObjetofrm().getNombre();
//            deshabilitarBotonLov();
//        } else if (cualCelda == 2) {
//            permisoPantallaSeleccionado.getTipo();
//            habilitarBotonLov();
//        }
//    }
//
//    public void asignarIndex(PermisosPantallas objeto, int dlg, int lnd) {
//        permisoPantallaSeleccionado = objeto;
//        tipoActualizacion = lnd;
//        if (dlg == 0) {
//            lovPerfiles = null;
//            getLovPerfiles();
//            contarRegistrosPerfiles();
//            RequestContext.getCurrentInstance().update("formularioDialogos:perfilesDialogo");
//            RequestContext.getCurrentInstance().execute("PF('modulosDialogo').show()");
//        } else if (dlg == 1) {
//            lovObjetosBloques = null;
//            getLovObjetosBloques();
//            contarRegistrosObjetosBloques();
//            RequestContext.getCurrentInstance().update("formularioDialogos:objetosBloquesDialogo");
//            RequestContext.getCurrentInstance().execute("PF('modulosDialogo').show()");
//        }
//
//    }
//
//    public void listaValoresBoton() {
//        if (cualCelda == 0) {
//            lovPerfiles = null;
//            getLovPerfiles();
//            RequestContext.getCurrentInstance().update("formularioDialogos:perfilesDialogo");
//            RequestContext.getCurrentInstance().execute("PF('modulosDialogo').show()");
//        }else if (cualCelda == 1) {
//            lovObjetosBloques = null;
//            getLovObjetosBloques();
//            RequestContext.getCurrentInstance().update("formularioDialogos:objetosBloquesDialogo");
//            RequestContext.getCurrentInstance().execute("PF('modulosDialogo').show()");
//        }
//    }
//
//    public void activarAceptar() {
//        aceptar = false;
//    }
//
//    public void cancelarModificacion() {
//        if (bandera == 1) {
//            //CERRAR FILTRADO
//            descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosPermisosPantallas:descripcion");
//            descripcion.setFilterStyle("display: none; visibility: hidden;");
//            nombre = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosPermisosPantallas:nombre");
//            nombre.setFilterStyle("display: none; visibility: hidden;");
//            tipo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosPermisosPantallas:tipo");
//            tipo.setFilterStyle("display: none; visibility: hidden;");
//            RequestContext.getCurrentInstance().update("form:datosPermisosPantallas");
//            bandera = 0;
//            listaPermisosPantallasFiltrar = null;
//            tipoLista = 0;
//            altoTabla = "315";
//        }
//
//        listaPermisosPantallasBorrar.clear();
//        listaPermisosPantallasCrear.clear();
//        listaPermisosPantallasModificar.clear();
//        permisoPantallaSeleccionado = null;
//        k = 0;
//        listaPermisosPantallas = null;
//        guardado = true;
//        RequestContext context = RequestContext.getCurrentInstance();
//        RequestContext.getCurrentInstance().update("form:datosPermisosPantallas");
//        RequestContext.getCurrentInstance().update("form:ACEPTAR");
//    }
//
//    public void eventoFiltrar() {
//        if (tipoLista == 0) {
//            tipoLista = 1;
//        }
//        contarRegistros();
//    }
//
//    public void activarCtrlF11() {
//        if (bandera == 0) {
//            altoTabla = "295";
//            tipo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosPermisosPantallas:tipo");
//            tipo.setFilterStyle("width: 85% !important;");
//            nombre = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosPermisosPantallas:nombre");
//            nombre.setFilterStyle("width: 85% !important;");
//            descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosPermisosPantallas:descripcion");
//            descripcion.setFilterStyle("width: 85% !important;");
//            RequestContext.getCurrentInstance().update("form:datosPermisosPantallas");
//            bandera = 1;
//        } else if (bandera == 1) {
//            altoTabla = "315";
//            tipo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosPermisosPantallas:tipo");
//            tipo.setFilterStyle("display: none; visibility: hidden;");
//            nombre = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosPermisosPantallas:nombre");
//            nombre.setFilterStyle("display: none; visibility: hidden;");
//            descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosPermisosPantallas:descripcion");
//            descripcion.setFilterStyle("display: none; visibility: hidden;");
//            RequestContext.getCurrentInstance().update("form:datosPermisosPantallas");
//            bandera = 0;
//            listaPermisosPantallasFiltrar = null;
//            tipoLista = 0;
//        }
//    }
//
//    public void seleccionarComboBox(PermisosPantallas objeto, String estado) {
//        permisoPantallaSeleccionado = objeto;
//        if (estado.equals("FUNCTION")) {
//            permisoPantallaSeleccionado.setTipo("FUNCTION");
//        } else if (estado.equals("PACKAGE")) {
//            permisoPantallaSeleccionado.setTipo("PACKAGE");
//        } else if (estado.equals("PROCEDURE")) {
//            permisoPantallaSeleccionado.setTipo("PROCEDURE");
//        } else if (estado.equals("SEQUENCE")) {
//            permisoPantallaSeleccionado.setTipo("SEQUENCE");
//        } else if (estado.equals("TABLE")) {
//            permisoPantallaSeleccionado.setTipo("TABLE");
//        } else if (estado.equals("VIEW")) {
//            permisoPantallaSeleccionado.setTipo("VIEW");
//        }
//        modificarPermisosPantallas(permisoPantallaSeleccionado);
//    }
//
//    public void modificarPermisosPantallas(PermisosPantallas objeto) {
//        permisoPantallaSeleccionado = objeto;
//        if (!listaPermisosPantallasCrear.contains(permisoPantallaSeleccionado)) {
//            if (listaPermisosPantallasModificar.isEmpty()) {
//                listaPermisosPantallasModificar.add(permisoPantallaSeleccionado);
//            } else if (!listaPermisosPantallasModificar.contains(permisoPantallaSeleccionado)) {
//                listaPermisosPantallasModificar.add(permisoPantallaSeleccionado);
//            }
//            guardado = false;
//            RequestContext.getCurrentInstance().update("form:ACEPTAR");
//        }
//        RequestContext.getCurrentInstance().update("form:datosPermisosPantallas");
//    }
//
//    public void borrarPermisosPantallas() {
//        if (permisoPantallaSeleccionado != null) {
//            if (!listaPermisosPantallasModificar.isEmpty() && listaPermisosPantallasModificar.contains(permisoPantallaSeleccionado)) {
//                int modIndex = listaPermisosPantallasModificar.indexOf(permisoPantallaSeleccionado);
//                listaPermisosPantallasModificar.remove(modIndex);
//                listaPermisosPantallasBorrar.add(permisoPantallaSeleccionado);
//            } else if (!listaPermisosPantallasCrear.isEmpty() && listaPermisosPantallasCrear.contains(permisoPantallaSeleccionado)) {
//                int crearIndex = listaPermisosPantallasCrear.indexOf(permisoPantallaSeleccionado);
//                listaPermisosPantallasCrear.remove(crearIndex);
//            } else {
//                listaPermisosPantallasBorrar.add(permisoPantallaSeleccionado);
//            }
//            listaPermisosPantallas.remove(permisoPantallaSeleccionado);
//            if (tipoLista == 1) {
//                listaPermisosPantallasFiltrar.remove(permisoPantallaSeleccionado);
//            }
//            permisoPantallaSeleccionado = null;
//            guardado = false;
//            RequestContext.getCurrentInstance().update("form:ACEPTAR");
//            RequestContext.getCurrentInstance().update("form:datosPermisosPantallas");
//        }
//    }
//
//    public void revisarDialogoGuardar() {
//        if (!listaPermisosPantallasBorrar.isEmpty() || !listaPermisosPantallasCrear.isEmpty() || !listaPermisosPantallasModificar.isEmpty()) {
//            RequestContext context = RequestContext.getCurrentInstance();
//            RequestContext.getCurrentInstance().update("form:confirmarGuardar");
//            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
//        }
//    }
//
//    public void guardarObjetosBD() {
//        RequestContext context = RequestContext.getCurrentInstance();
//        try {
//            if (guardado == false) {
//                msgError = "";
//                if (!listaPermisosPantallasBorrar.isEmpty()) {
//                    for (int i = 0; i < listaPermisosPantallasBorrar.size(); i++) {
//                        msgError = administrarObjetosDb.borrarPermisosPantallas(listaPermisosPantallasBorrar.get(i));
//                    }
//                    listaPermisosPantallasBorrar.clear();
//                }
//                if (!listaPermisosPantallasCrear.isEmpty()) {
//                    for (int i = 0; i < listaPermisosPantallasCrear.size(); i++) {
//                        msgError = administrarObjetosDb.crearPermisosPantallas(listaPermisosPantallasCrear.get(i));
//                    }
//                    listaPermisosPantallasCrear.clear();
//                }
//                if (!listaPermisosPantallasModificar.isEmpty()) {
//                    for (int i = 0; i < listaPermisosPantallasModificar.size(); i++) {
//                        msgError = administrarObjetosDb.modificarPermisosPantallas(listaPermisosPantallasModificar.get(i));
//                    }
//                }
//                listaPermisosPantallasModificar.clear();
//
//                if (msgError.equals("EXITO")) {
//                    listaPermisosPantallas = null;
//                    getListaPermisosPantallas();
//                    k = 0;
//                    FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
//                    FacesContext.getCurrentInstance().addMessage(null, msg);
//                    RequestContext.getCurrentInstance().update("form:growl");
//                    contarRegistros();
//                    guardado = true;
//                    permisoPantallaSeleccionado = null;
//                    RequestContext.getCurrentInstance().update("form:datosPermisosPantallas");
//                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
//                } else {
//                    RequestContext.getCurrentInstance().update("formularioDialogos:errorGuardadoBD");
//                    RequestContext.getCurrentInstance().execute("PF('errorGuardadoBD').show()");
//                }
//
//            }
//        } catch (Exception e) {
//            log.warn("Error guardarCambios : " + e.toString());
//            FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado, intente nuevamente.");
//            FacesContext.getCurrentInstance().addMessage(null, msg);
//            RequestContext.getCurrentInstance().update("form:growl");
//        }
//
//    }
//
//    public void editarCelda() {
//        if (permisoPantallaSeleccionado != null) {
//            editarObjetoDB = permisoPantallaSeleccionado;
//            RequestContext context = RequestContext.getCurrentInstance();
//            if (cualCelda == 1) {
//                RequestContext.getCurrentInstance().update("formularioDialogos:editNombre");
//                RequestContext.getCurrentInstance().execute("PF('editNombre').show()");
//                cualCelda = -1;
//            } else if (cualCelda == 2) {
//                RequestContext.getCurrentInstance().update("formularioDialogos:editModulo");
//                RequestContext.getCurrentInstance().execute("PF('editModulo').show()");
//                cualCelda = -1;
//            } else if (cualCelda == 3) {
//                RequestContext.getCurrentInstance().update("formularioDialogos:editDescripcion");
//                RequestContext.getCurrentInstance().execute("PF('editDescripcion').show()");
//                cualCelda = -1;
//            }
//
//        } else {
//            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
//        }
//    }
//
//    public void agregarNuevoObjetoDB() {
//        int contador = 0;
//        int duplicados = 0;
//
//        Integer a = 0;
//        a = null;
//        mensajeValidacion = " ";
//        if (nuevoObjetoDB.getNombre() == (null) || nuevoObjetoDB.getNombre().equals("")) {
//            mensajeValidacion = " Los campos marcados con asterisco son obligatorios";
//        } else {
//            for (int x = 0; x < listaPermisosPantallas.size(); x++) {
//                if (listaPermisosPantallas.get(x).getNombre().equals(nuevoObjetoDB.getNombre())) {
//                    duplicados++;
//                }
//            }
//            if (duplicados > 0) {
//                mensajeValidacion = "El Nombre ingresado ya está registrado. Por favor ingrese un Nombre válido";
//            } else {
//                contador++;
//            }
//        }
//
//        if (nuevoObjetoDB.getModulo() == (null)) {
//            mensajeValidacion = " Los campos marcados con asterisco son obligatorios";
//        } else {
//            contador++;
//        }
//        if (nuevoObjetoDB.getTipo() == null) {
//            nuevoObjetoDB.setTipo("FUNCTION");
//        }
//
//        if (contador == 2) {
//            if (bandera == 1) {
//                tipo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosPermisosPantallas:tipo");
//                tipo.setFilterStyle("display: none; visibility: hidden;");
//                nombre = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosPermisosPantallas:nombre");
//                nombre.setFilterStyle("display: none; visibility: hidden;");
//                descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosPermisosPantallas:descripcion");
//                descripcion.setFilterStyle("display: none; visibility: hidden;");
//                RequestContext.getCurrentInstance().update("form:datosPermisosPantallas");
//                bandera = 0;
//                listaPermisosPantallasFiltrar = null;
//                tipoLista = 0;
//                altoTabla = "315";
//            }
//            k++;
//            l = BigInteger.valueOf(k);
//            nuevoObjetoDB.setSecuencia(l);
//            listaPermisosPantallasCrear.add(nuevoObjetoDB);
//            listaPermisosPantallas.add(0, nuevoObjetoDB);
//            permisoPantallaSeleccionado = nuevoObjetoDB;
//            RequestContext.getCurrentInstance().update("form:datosPermisosPantallas");
//            guardado = false;
//            RequestContext.getCurrentInstance().update("form:ACEPTAR");
//            RequestContext.getCurrentInstance().execute("PF('nuevoRegistroObjetosBD').hide()");
//            nuevoObjetoDB = new PermisosPantallas();
//
//        } else {
//            RequestContext.getCurrentInstance().update("form:validacionNuevaCentroCosto");
//            RequestContext.getCurrentInstance().execute("PF('validacionNuevaCentroCosto').show()");
//            contador = 0;
//        }
//    }
//
//    public void limpiarNuevoObjetoDB() {
//        nuevoObjetoDB = new PermisosPantallas();
//    }
//
//    public void duplicandoPermisosPantallas() {
//        if (permisoPantallaSeleccionado != null) {
//            duplicarObjetoDB = new PermisosPantallas();
//            k++;
//            l = BigInteger.valueOf(k);
//            duplicarObjetoDB.setSecuencia(l);
//            duplicarObjetoDB.setAutorizada(permisoPantallaSeleccionado.getAutorizada());
//            duplicarObjetoDB.setClasificacion(permisoPantallaSeleccionado.getClasificacion());
//            duplicarObjetoDB.setDescripcion(permisoPantallaSeleccionado.getDescripcion());
//            duplicarObjetoDB.setExcluirasignacionperfil(permisoPantallaSeleccionado.getExcluirasignacionperfil());
//            duplicarObjetoDB.setModulo(permisoPantallaSeleccionado.getModulo());
//            duplicarObjetoDB.setNombre(permisoPantallaSeleccionado.getNombre());
//            duplicarObjetoDB.setTipo(permisoPantallaSeleccionado.getTipo());
//            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarObj");
//            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroMotivosDefinitivas').show()");
//        } else {
//            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
//        }
//    }
//
//    public void confirmarDuplicar() {
//        int contador = 0;
//        mensajeValidacion = " ";
//        int duplicados = 0;
//        RequestContext context = RequestContext.getCurrentInstance();
//        Integer a = 0;
//        a = null;
//
//        if (duplicarObjetoDB.getNombre() == (null) || duplicarObjetoDB.getNombre().equals("")) {
//            mensajeValidacion = " Los campos marcados con asterisco son obligatorios";
//        } else {
//            for (int x = 0; x < listaPermisosPantallas.size(); x++) {
//                if (listaPermisosPantallas.get(x).getNombre().equals(duplicarObjetoDB.getNombre())) {
//                    duplicados++;
//                }
//            }
//            if (duplicados > 0) {
//                mensajeValidacion = "El Nombre ingresado ya está registrado. Por favor ingrese un Nombre válido";
//            } else {
//                contador++;
//            }
//        }
//        if (duplicarObjetoDB.getModulo() == (null)) {
//            mensajeValidacion = " Los campos marcados con asterisco son obligatorios";
//        } else {
//            contador++;
//        }
//        if (duplicarObjetoDB.getTipo() == null) {
//            duplicarObjetoDB.setTipo("FUNCTION");
//        }
//
//        if (contador == 2) {
//            listaPermisosPantallas.add(0, duplicarObjetoDB);
//            listaPermisosPantallasCrear.add(duplicarObjetoDB);
//            permisoPantallaSeleccionado = duplicarObjetoDB;
//            RequestContext.getCurrentInstance().update("form:datosPermisosPantallas");
//            guardado = false;
//            RequestContext.getCurrentInstance().update("form:ACEPTAR");
//            if (bandera == 1) {
//                tipo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosPermisosPantallas:tipo");
//                tipo.setFilterStyle("display: none; visibility: hidden;");
//                nombre = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosPermisosPantallas:nombre");
//                nombre.setFilterStyle("display: none; visibility: hidden;");
//                descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosPermisosPantallas:descripcion");
//                descripcion.setFilterStyle("display: none; visibility: hidden;");
//                RequestContext.getCurrentInstance().update("form:datosPermisosPantallas");
//                bandera = 0;
//                listaPermisosPantallasFiltrar = null;
//                tipoLista = 0;
//                altoTabla = "270";
//            }
//            duplicarObjetoDB = new PermisosPantallas();
//            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroMotivosDefinitivas').hide()");
//
//        } else {
//            contador = 0;
//            RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
//            RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
//        }
//    }
//
//    public void limpiarDuplicarPermisosPantallas() {
//        duplicarObjetoDB = new PermisosPantallas();
//    }
//
//    public void exportPDF() throws IOException {
//        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosPermisosPantallasExportar");
//        FacesContext context = FacesContext.getCurrentInstance();
//        Exporter exporter = new ExportarPDF();
//        exporter.export(context, tabla, "MotivosDefinitivas", false, false, "UTF-8", null, null);
//        context.responseComplete();
//    }
//
//    public void exportXLS() throws IOException {
//        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosPermisosPantallasExportar");
//        FacesContext context = FacesContext.getCurrentInstance();
//        Exporter exporter = new ExportarXLS();
//        exporter.export(context, tabla, "MotivosDefinitivas", false, false, "UTF-8", null, null);
//        context.responseComplete();
//    }
//
//    public void verificarRastro() {
//        if (permisoPantallaSeleccionado != null) {
//            int resultado = administrarRastros.obtenerTabla(permisoPantallaSeleccionado.getSecuencia(), "OBJETOSDB"); //En ENCARGATURAS lo cambia por el nombre de su tabla
//            if (resultado == 1) {
//                RequestContext.getCurrentInstance().execute("PF('errorPermisosPantallas').show()");
//            } else if (resultado == 2) {
//                RequestContext.getCurrentInstance().execute("PF('confirmarRastro').show()");
//            } else if (resultado == 3) {
//                RequestContext.getCurrentInstance().execute("PF('errorRegistroRastro').show()");
//            } else if (resultado == 4) {
//                RequestContext.getCurrentInstance().execute("PF('errorTablaConRastro').show()");
//            } else if (resultado == 5) {
//                RequestContext.getCurrentInstance().execute("PF('errorTablaSinRastro').show()");
//            }
//        } else if (administrarRastros.verificarHistoricosTabla("OBJETOSDB")) { // igual acá
//            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
//        } else {
//            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
//        }
//    }
//
//    public void salir() {
//        limpiarListasValor();
//        if (bandera == 1) {
//            tipo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosPermisosPantallas:tipo");
//            tipo.setFilterStyle("display: none; visibility: hidden;");
//            nombre = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosPermisosPantallas:nombre");
//            nombre.setFilterStyle("display: none; visibility: hidden;");
//            descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosPermisosPantallas:descripcion");
//            descripcion.setFilterStyle("display: none; visibility: hidden;");
//            RequestContext.getCurrentInstance().update("form:datosPermisosPantallas");
//            bandera = 0;
//            listaPermisosPantallasFiltrar = null;
//            tipoLista = 0;
//            altoTabla = "315";
//        }
//        listaPermisosPantallasBorrar.clear();
//        listaPermisosPantallasCrear.clear();
//        listaPermisosPantallasModificar.clear();
//        permisoPantallaSeleccionado = null;
//        k = 0;
//        listaPermisosPantallas = null;
//        guardado = true;
//        RequestContext.getCurrentInstance().update("form:datosPermisosPantallas");
//        RequestContext.getCurrentInstance().update("form:ACEPTAR");
//        navegar("atras");
//    }
//
//    public void contarRegistros() {
//        RequestContext.getCurrentInstance().update("form:informacionRegistro");
//    }
//
//    public void contarRegistrosObjetosBloques() {
//        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroObjetosBloques");
//    }
//
//    public void habilitarBotonLov() {
//        activarLov = false;
//        RequestContext.getCurrentInstance().update("form:listaValores");
//    }
//
//    public void deshabilitarBotonLov() {
//        activarLov = true;
//        RequestContext.getCurrentInstance().update("form:listaValores");
//
//    }
//
//    public void actualizarModulo() {
//        RequestContext context = RequestContext.getCurrentInstance();
//        if (tipoActualizacion == 0) {
//            permisoPantallaSeleccionado.setModulo(moduloSeleccionado);
//            if (!listaPermisosPantallasCrear.contains(permisoPantallaSeleccionado)) {
//                if (listaPermisosPantallasModificar.isEmpty()) {
//                    listaPermisosPantallasModificar.add(permisoPantallaSeleccionado);
//                } else if (!listaPermisosPantallasModificar.contains(permisoPantallaSeleccionado)) {
//                    listaPermisosPantallasModificar.add(permisoPantallaSeleccionado);
//                }
//            }
//            guardado = false;
//            RequestContext.getCurrentInstance().update("form:ACEPTAR");
//            deshabilitarBotonLov();
//            RequestContext.getCurrentInstance().update("form:datosPermisosPantallas");
//        } else if (tipoActualizacion == 1) {
//            nuevoObjetoDB.setModulo(moduloSeleccionado);
//            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoObj");
//        } else if (tipoActualizacion == 2) {
//            duplicarObjetoDB.setModulo(moduloSeleccionado);
//            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarObj");
//        }
//        lovObjetosBloquesFiltrar = null;
//        moduloSeleccionado = null;
//        aceptar = true;
//        tipoActualizacion = -1;
//        RequestContext.getCurrentInstance().update("formularioDialogos:modulosDialogo");
//        RequestContext.getCurrentInstance().update("formularioDialogos:lovObjetosBloques");
//        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarM");
//        context.reset("formularioDialogos:lovObjetosBloques:globalFilter");
//        RequestContext.getCurrentInstance().execute("PF('lovObjetosBloques').clearFilters()");
//        RequestContext.getCurrentInstance().execute("PF('modulosDialogo').hide()");
//    }
//
//    public void cancelarCambioModulo() {
//        lovObjetosBloquesFiltrar = null;
//        moduloSeleccionado = null;
//        aceptar = true;
//        tipoActualizacion = -1;
//        RequestContext.getCurrentInstance().update("formularioDialogos:modulosDialogo");
//        RequestContext.getCurrentInstance().update("formularioDialogos:lovObjetosBloques");
//        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarM");
//        RequestContext.getCurrentInstance().reset("formularioDialogos:lovObjetosBloques:globalFilter");
//        RequestContext.getCurrentInstance().execute("PF('lovObjetosBloques').clearFilters()");
//        RequestContext.getCurrentInstance().execute("PF('modulosDialogo').hide()");
//    }
//
//    //////////GET Y SET///
//    public List<PermisosPantallas> getListaPermisosPantallas() {
//        return listaPermisosPantallas;
//    }
//
//    public void setListaPermisosPantallas(List<PermisosPantallas> listaPermisosPantallas) {
//        this.listaPermisosPantallas = listaPermisosPantallas;
//    }
//
//    public List<PermisosPantallas> getListaPermisosPantallasFiltrar() {
//        return listaPermisosPantallasFiltrar;
//    }
//
//    public void setListaPermisosPantallasFiltrar(List<PermisosPantallas> listaPermisosPantallasFiltrar) {
//        this.listaPermisosPantallasFiltrar = listaPermisosPantallasFiltrar;
//    }
//
//    public PermisosPantallas getNuevoPermisoPantalla() {
//        return nuevoPermisoPantalla;
//    }
//
//    public void setNuevoPermisoPantalla(PermisosPantallas nuevoPermisoPantalla) {
//        this.nuevoPermisoPantalla = nuevoPermisoPantalla;
//    }
//
//    public PermisosPantallas getDuplicarPermisoPantalla() {
//        return duplicarPermisoPantalla;
//    }
//
//    public void setDuplicarPermisoPantalla(PermisosPantallas duplicarPermisoPantalla) {
//        this.duplicarPermisoPantalla = duplicarPermisoPantalla;
//    }
//
//    public PermisosPantallas getEditarPermisoPantalla() {
//        return editarPermisoPantalla;
//    }
//
//    public void setEditarPermisoPantalla(PermisosPantallas editarPermisoPantalla) {
//        this.editarPermisoPantalla = editarPermisoPantalla;
//    }
//
//    public PermisosPantallas getPermisoPantallaSeleccionado() {
//        return permisoPantallaSeleccionado;
//    }
//
//    public void setPermisoPantallaSeleccionado(PermisosPantallas permisoPantallaSeleccionado) {
//        this.permisoPantallaSeleccionado = permisoPantallaSeleccionado;
//    }
//
//    public List<ObjetosBloques> getLovObjetosBloques() {
//        return lovObjetosBloques;
//    }
//
//    public void setLovObjetosBloques(List<ObjetosBloques> lovObjetosBloques) {
//        this.lovObjetosBloques = lovObjetosBloques;
//    }
//
//    public List<ObjetosBloques> getLovObjetosBloquesFiltrar() {
//        return lovObjetosBloquesFiltrar;
//    }
//
//    public void setLovObjetosBloquesFiltrar(List<ObjetosBloques> lovObjetosBloquesFiltrar) {
//        this.lovObjetosBloquesFiltrar = lovObjetosBloquesFiltrar;
//    }
//
//    public ObjetosBloques getObjetoBloqueSeleccionado() {
//        return objetoBloqueSeleccionado;
//    }
//
//    public void setObjetoBloqueSeleccionado(ObjetosBloques objetoBloqueSeleccionado) {
//        this.objetoBloqueSeleccionado = objetoBloqueSeleccionado;
//    }
//
//    public boolean isAceptar() {
//        return aceptar;
//    }
//
//    public void setAceptar(boolean aceptar) {
//        this.aceptar = aceptar;
//    }
//
//    public boolean isGuardado() {
//        return guardado;
//    }
//
//    public void setGuardado(boolean guardado) {
//        this.guardado = guardado;
//    }
//
//    public String getAltoTabla() {
//        return altoTabla;
//    }
//
//    public void setAltoTabla(String altoTabla) {
//        this.altoTabla = altoTabla;
//    }
//
//    public String getInfoRegistro() {
//        return infoRegistro;
//    }
//
//    public void setInfoRegistro(String infoRegistro) {
//        this.infoRegistro = infoRegistro;
//    }
//
//    public String getInfoRegistroObjetosBloques() {
//        return infoRegistroObjetosBloques;
//    }
//
//    public void setInfoRegistroObjetosBloques(String infoRegistroObjetosBloques) {
//        this.infoRegistroObjetosBloques = infoRegistroObjetosBloques;
//    }
//
//    public String getMensajeValidacion() {
//        return mensajeValidacion;
//    }
//
//    public void setMensajeValidacion(String mensajeValidacion) {
//        this.mensajeValidacion = mensajeValidacion;
//    }
//
//    public String getMsgError() {
//        return msgError;
//    }
//
//    public void setMsgError(String msgError) {
//        this.msgError = msgError;
//    }
//
//    public boolean isActivarLov() {
//        return activarLov;
//    }
//
//    public void setActivarLov(boolean activarLov) {
//        this.activarLov = activarLov;
//    }
//
//    public List<Perfiles> getLovPerfiles() {
//        return lovPerfiles;
//    }
//
//    public void setLovPerfiles(List<Perfiles> lovPerfiles) {
//        this.lovPerfiles = lovPerfiles;
//    }
//
//    public List<Perfiles> getLovPerfilesFiltrar() {
//        return lovPerfilesFiltrar;
//    }
//
//    public void setLovPerfilesFiltrar(List<Perfiles> lovPerfilesFiltrar) {
//        this.lovPerfilesFiltrar = lovPerfilesFiltrar;
//    }
//    
//    
//
//}
