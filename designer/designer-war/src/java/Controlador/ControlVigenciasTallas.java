/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Empleados;
import Entidades.TiposTallas;
import Entidades.VigenciasTallas;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarVigenciasTallasInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import ControlNavegacion.ControlListaNavegacion;
import java.util.Map;
import java.util.LinkedHashMap;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
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
@ManagedBean
@SessionScoped
public class ControlVigenciasTallas implements Serializable {

    @EJB
    AdministrarVigenciasTallasInterface administrarVigenciasTallas;
    @EJB
    AdministrarRastrosInterface administrarRastros;
    private List<VigenciasTallas> listVigenciasTallas;
    private List<VigenciasTallas> filtrarVigenciasTallas;
    private List<VigenciasTallas> crearVigenciasTallas;
    private List<VigenciasTallas> modificarVigenciasTallas;
    private List<VigenciasTallas> borrarVigenciasTallas;
    private VigenciasTallas nuevoVigenciaTalla;
    private VigenciasTallas duplicarVigenciaTalla;
    private VigenciasTallas editarVigenciaTalla;
    private VigenciasTallas vigenciaTallaSeleccionada;
    //lov
    private List<TiposTallas> listaTiposTallas;
    private List<TiposTallas> filtradoTiposTallas;
    private TiposTallas tipoTallaSeleccionado;
    //otros
    private int cualCelda, tipoLista, tipoActualizacion, k, bandera;
    private BigInteger l;
    private boolean aceptar, guardado;
    private boolean permitirIndex;
    private Column idTipoTalla, fecha, idTalla, idObservaciones;
    //borrado
    private int registrosBorrados;
    private String mensajeValidacion;
    private BigInteger secuenciaEmpleado;
//Empleado
    private Empleados empleadoSeleccionado;
    private String nuevoParentesco;
    private String infoRegistro;
    private String infoRegistroTiposTallas;
    private int tamano;
    private boolean activarLov;
    private String paginaAnterior = "nominaf";
    private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

    public ControlVigenciasTallas() {
        listVigenciasTallas = null;
        crearVigenciasTallas = new ArrayList<VigenciasTallas>();
        modificarVigenciasTallas = new ArrayList<VigenciasTallas>();
        borrarVigenciasTallas = new ArrayList<VigenciasTallas>();
        permitirIndex = true;
        guardado = true;
        editarVigenciaTalla = new VigenciasTallas();
        nuevoVigenciaTalla = new VigenciasTallas();
        nuevoVigenciaTalla.setTipoTalla(new TiposTallas());
        nuevoVigenciaTalla.setEmpleado(new Empleados());
        duplicarVigenciaTalla = new VigenciasTallas();
        duplicarVigenciaTalla.setTipoTalla(new TiposTallas());
        duplicarVigenciaTalla.setEmpleado(new Empleados());
        empleadoSeleccionado = null;
        listaTiposTallas = null;
        filtradoTiposTallas = null;
        tipoLista = 0;
        tamano = 290;
        aceptar = true;
        secuenciaEmpleado = null;
        mapParametros.put("paginaAnterior", paginaAnterior);
        activarLov = true;
    }

    public void limpiarListasValor() {

    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarVigenciasTallas.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void recibirPaginaEntrante(String pagina) {
        paginaAnterior = pagina;
        //inicializarCosas(); Inicializar cosas de ser necesario
    }

    public void recibirParametros(Map<String, Object> map) {
        mapParametros = map;
        paginaAnterior = (String) mapParametros.get("paginaAnterior");
        //inicializarCosas(); Inicializar cosas de ser necesario
    }

    //Reemplazar la funcion volverAtras, retornarPagina, Redirigir.....Atras.etc
    public void navegar(String pag) {
        FacesContext fc = FacesContext.getCurrentInstance();
        ControlListaNavegacion controlListaNavegacion = (ControlListaNavegacion) fc.getApplication().evaluateExpressionGet(fc, "#{controlListaNavegacion}", ControlListaNavegacion.class);
        if (pag.equals("atras")) {
            pag = paginaAnterior;
            paginaAnterior = "nominaf";
            controlListaNavegacion.quitarPagina();
        } else {
            String pagActual = "vigenciatalla";
            //Map<String, Object> mapParaEnviar = new LinkedHashMap<String, Object>();
            //mapParametros.put("paginaAnterior", pagActual);
            //mas Parametros
//         if (pag.equals("rastrotabla")) {
//           ControlRastro controlRastro = (ControlRastro) fc.getApplication().evaluateExpressionGet(fc, "#{controlRastro}", ControlRastro.class);
            //           controlRastro.recibirDatosTabla(conceptoSeleccionado.getSecuencia(), "Conceptos", pagActual);
            //      } else if (pag.equals("rastrotablaH")) {
            //       ControlRastro controlRastro = (ControlRastro) fc.getApplication().evaluateExpressionGet(fc, "#{controlRastro}", ControlRastro.class);
            //     controlRastro.historicosTabla("Conceptos", pagActual);
            //   pag = "rastrotabla";
            //}
            controlListaNavegacion.adicionarPagina(pagActual);
        }
        fc.getApplication().getNavigationHandler().handleNavigation(fc, null, pag);
    }

    public void recibirEmpleado(Empleados empleado, String pagina) {
        System.out.println("empleados" + empleado);
        paginaAnterior = pagina;
        secuenciaEmpleado = empleado.getSecuencia();
        System.out.println("secuenciaEmpleado " + secuenciaEmpleado);
        empleadoSeleccionado = empleado;
        listVigenciasTallas = null;
        getEmpleadoSeleccionado();
        getListVigenciasTallas();
    }

    public void eventoFiltrar() {
        try {
            if (tipoLista == 0) {
                tipoLista = 1;
            }
            contarRegistros();
        } catch (Exception e) {
            System.out.println("ERROR ControlVigenciasTallas eventoFiltrar ERROR===" + e.getMessage());
        }
    }

    public void modificarVigenciaTalla(VigenciasTallas vigenciaT) {
        vigenciaTallaSeleccionada = vigenciaT;
        if (!crearVigenciasTallas.contains(vigenciaTallaSeleccionada)) {
            if (modificarVigenciasTallas.isEmpty()) {
                modificarVigenciasTallas.add(vigenciaTallaSeleccionada);
            } else if (!modificarVigenciasTallas.contains(vigenciaTallaSeleccionada)) {
                modificarVigenciasTallas.add(vigenciaTallaSeleccionada);
            }
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
        RequestContext.getCurrentInstance().update("form:datosVigenciasTallas");
    }

    public void cambiarIndice(VigenciasTallas vigenciaT, int celda) {
        if (permitirIndex == true) {
            vigenciaTallaSeleccionada = vigenciaT;
            cualCelda = celda;
            vigenciaTallaSeleccionada.getSecuencia();
            if (cualCelda == 0) {
                deshabilitarBotonLov();
                vigenciaTallaSeleccionada.getFechavigencia();
            } else if (cualCelda == 1) {
                habilitarBotonLov();
                vigenciaTallaSeleccionada.getTipoTalla().getDescripcion();
            } else if (cualCelda == 2) {
                deshabilitarBotonLov();
                vigenciaTallaSeleccionada.getTalla();
            } else if (cualCelda == 3) {
                deshabilitarBotonLov();
                vigenciaTallaSeleccionada.getObservaciones();
            }
        }
    }

    public void asignarIndex(VigenciasTallas vigenciaT, int LND, int dig) {
        vigenciaTallaSeleccionada = vigenciaT;
        tipoActualizacion = LND;
        if (dig == 1) {
            listaTiposTallas = null;
            getListaTiposTallas();
            contarRegistrosTiposTallas();
            RequestContext.getCurrentInstance().update("form:tipostallasDialogo");
            RequestContext.getCurrentInstance().execute("PF('tipostallasDialogo').show()");
            dig = -1;
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void listaValoresBoton() {
        if (vigenciaTallaSeleccionada != null) {
            if (cualCelda == 4) {
                listaTiposTallas = null;
                getListaTiposTallas();
                contarRegistrosTiposTallas();
                RequestContext.getCurrentInstance().update("form:tipostallasDialogo");
                RequestContext.getCurrentInstance().execute("PF('tipostallasDialogo').show()");
                tipoActualizacion = 0;
            }
        }
    }

    public void cancelarModificacion() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 1) {
            fecha = (Column) c.getViewRoot().findComponent("form:datosVigenciasTallas:fecha");
            fecha.setFilterStyle("display: none; visibility: hidden;");
            idTipoTalla = (Column) c.getViewRoot().findComponent("form:datosVigenciasTallas:idTipoTalla");
            idTipoTalla.setFilterStyle("display: none; visibility: hidden;");
            idTalla = (Column) c.getViewRoot().findComponent("form:datosVigenciasTallas:idTalla");
            idTalla.setFilterStyle("display: none; visibility: hidden;");
            idObservaciones = (Column) c.getViewRoot().findComponent("form:datosVigenciasTallas:idObservaciones");
            idObservaciones.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosVigenciasTallas");
            bandera = 0;
            filtrarVigenciasTallas = null;
            tipoLista = 0;
        }
        borrarVigenciasTallas.clear();
        crearVigenciasTallas.clear();
        modificarVigenciasTallas.clear();
        vigenciaTallaSeleccionada = null;
        k = 0;
        guardado = true;
        permitirIndex = true;
        listVigenciasTallas = null;
        getListVigenciasTallas();
        contarRegistros();
        RequestContext.getCurrentInstance().update("form:datosVigenciasTallas");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void salir() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 1) {
            fecha = (Column) c.getViewRoot().findComponent("form:datosVigenciasTallas:fecha");
            fecha.setFilterStyle("display: none; visibility: hidden;");
            idTipoTalla = (Column) c.getViewRoot().findComponent("form:datosVigenciasTallas:idTipoTalla");
            idTipoTalla.setFilterStyle("display: none; visibility: hidden;");
            idTalla = (Column) c.getViewRoot().findComponent("form:datosVigenciasTallas:idTalla");
            idTalla.setFilterStyle("display: none; visibility: hidden;");
            idObservaciones = (Column) c.getViewRoot().findComponent("form:datosVigenciasTallas:idObservaciones");
            idObservaciones.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosVigenciasTallas");
            bandera = 0;
            filtrarVigenciasTallas = null;
            tipoLista = 0;
        }
        borrarVigenciasTallas.clear();
        crearVigenciasTallas.clear();
        modificarVigenciasTallas.clear();
        vigenciaTallaSeleccionada = null;
        k = 0;
        guardado = true;
        permitirIndex = true;
        listVigenciasTallas = null;
        getListVigenciasTallas();
        contarRegistros();
        RequestContext.getCurrentInstance().update("form:datosVigenciasTallas");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            tamano = 270;
            fecha = (Column) c.getViewRoot().findComponent("form:datosVigenciasTallas:fecha");
            fecha.setFilterStyle("width: 85% !important;");
            idTipoTalla = (Column) c.getViewRoot().findComponent("form:datosVigenciasTallas:idTipoTalla");
            idTipoTalla.setFilterStyle("width: 85% !important;");
            idTalla = (Column) c.getViewRoot().findComponent("form:datosVigenciasTallas:idTalla");
            idTalla.setFilterStyle("width: 85% !important;");
            idObservaciones = (Column) c.getViewRoot().findComponent("form:datosVigenciasTallas:idObservaciones");
            idObservaciones.setFilterStyle("width: 85% !important;");
            RequestContext.getCurrentInstance().update("form:datosVigenciasTallas");
            System.out.println("Activar");
            bandera = 1;
        } else if (bandera == 1) {
            tamano = 290;
            System.out.println("Desactivar");
            fecha = (Column) c.getViewRoot().findComponent("form:datosVigenciasTallas:fecha");
            fecha.setFilterStyle("display: none; visibility: hidden;");
            idTipoTalla = (Column) c.getViewRoot().findComponent("form:datosVigenciasTallas:idTipoTalla");
            idTipoTalla.setFilterStyle("display: none; visibility: hidden;");
            idTalla = (Column) c.getViewRoot().findComponent("form:datosVigenciasTallas:idTalla");
            idTalla.setFilterStyle("display: none; visibility: hidden;");
            idObservaciones = (Column) c.getViewRoot().findComponent("form:datosVigenciasTallas:idObservaciones");
            idObservaciones.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosVigenciasTallas");
            bandera = 0;
            filtrarVigenciasTallas = null;
            tipoLista = 0;
        }
    }

    public void actualizarTipoFamiliar() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            vigenciaTallaSeleccionada.setTipoTalla(tipoTallaSeleccionado);
            if (!crearVigenciasTallas.contains(vigenciaTallaSeleccionada)) {
                if (modificarVigenciasTallas.isEmpty()) {
                    modificarVigenciasTallas.add(vigenciaTallaSeleccionada);
                } else if (!modificarVigenciasTallas.contains(vigenciaTallaSeleccionada)) {
                    modificarVigenciasTallas.add(vigenciaTallaSeleccionada);
                }
            }
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            permitirIndex = true;
            RequestContext.getCurrentInstance().update("form:datosVigenciasTallas");
        } else if (tipoActualizacion == 1) {
            nuevoVigenciaTalla.setTipoTalla(tipoTallaSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTipoTalla");
        } else if (tipoActualizacion == 2) {
            System.out.println(tipoTallaSeleccionado.getDescripcion());
            duplicarVigenciaTalla.setTipoTalla(tipoTallaSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarDescripcionTipoTallas");
        }
        filtrarVigenciasTallas = null;
        tipoTallaSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;

        RequestContext.getCurrentInstance().update("form:tipostallasDialogo");
        RequestContext.getCurrentInstance().update("form:lovTiposTallas");
        RequestContext.getCurrentInstance().update("form:aceptarS");

        context.reset("form:lovTiposTallas:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovTiposTallas').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('tipostallasDialogo').hide()");
    }

    public void cancelarCambioTiposTallas() {
        RequestContext context = RequestContext.getCurrentInstance();
        filtradoTiposTallas = null;
        tipoTallaSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        permitirIndex = true;
        RequestContext.getCurrentInstance().update("form:tipostallasDialogo");
        RequestContext.getCurrentInstance().update("form:lovTiposTallas");
        RequestContext.getCurrentInstance().update("form:aceptarS");
        context.reset("form:lovTiposTallas:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovTiposTallas').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('tipostallasDialogo').hide()");
    }

    public void borrandoVigenciasTallas() {

        if (vigenciaTallaSeleccionada != null) {
            System.out.println("Entro a borrandoVigenciasTallas");
            if (!modificarVigenciasTallas.isEmpty() && modificarVigenciasTallas.contains(vigenciaTallaSeleccionada)) {
                int modIndex = modificarVigenciasTallas.indexOf(vigenciaTallaSeleccionada);
                modificarVigenciasTallas.remove(modIndex);
                borrarVigenciasTallas.add(vigenciaTallaSeleccionada);
            } else if (!crearVigenciasTallas.isEmpty() && crearVigenciasTallas.contains(vigenciaTallaSeleccionada)) {
                int crearIndex = crearVigenciasTallas.indexOf(vigenciaTallaSeleccionada);
                crearVigenciasTallas.remove(crearIndex);
            } else {
                borrarVigenciasTallas.add(vigenciaTallaSeleccionada);
            }
            listVigenciasTallas.remove(vigenciaTallaSeleccionada);
            if (tipoLista == 1) {
                filtrarVigenciasTallas.remove(vigenciaTallaSeleccionada);
            }
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:datosVigenciasTallas");
            vigenciaTallaSeleccionada = null;

            if (guardado == true) {
                guardado = false;
            }
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void revisarDialogoGuardar() {
        if (!borrarVigenciasTallas.isEmpty() || !crearVigenciasTallas.isEmpty() || !modificarVigenciasTallas.isEmpty()) {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:confirmarGuardar");
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
        }
    }

    public void guardarVigenciasTallas() {
        try {
            RequestContext context = RequestContext.getCurrentInstance();
            if (guardado == false) {
                System.out.println("Realizando guardarVigenciasTallas");
                if (!borrarVigenciasTallas.isEmpty()) {
                    for (int i = 0; i < borrarVigenciasTallas.size(); i++) {
                        System.out.println("Borrando...");
                        if (borrarVigenciasTallas.get(i).getTipoTalla().getSecuencia() == null) {
                            borrarVigenciasTallas.get(i).setTipoTalla(new TiposTallas());
                        }
                    }
                    administrarVigenciasTallas.borrarVigenciasTallas(borrarVigenciasTallas);
                    registrosBorrados = borrarVigenciasTallas.size();
                    RequestContext.getCurrentInstance().update("form:mostrarBorrados");
                    RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
                    borrarVigenciasTallas.clear();
                }
                if (!crearVigenciasTallas.isEmpty()) {
                    for (int i = 0; i < crearVigenciasTallas.size(); i++) {
                        if (crearVigenciasTallas.get(i).getTipoTalla().getSecuencia() == null) {
                            crearVigenciasTallas.get(i).setTipoTalla(new TiposTallas());
                        }
                    }
                    System.out.println("Creando...");
                    administrarVigenciasTallas.crearVigenciasTallas(crearVigenciasTallas);
                    crearVigenciasTallas.clear();
                }
                if (!modificarVigenciasTallas.isEmpty()) {
                    for (int i = 0; i < modificarVigenciasTallas.size(); i++) {
                        if (modificarVigenciasTallas.get(i).getTipoTalla().getSecuencia() == null) {
                            modificarVigenciasTallas.get(i).setTipoTalla(new TiposTallas());
                        }
                    }
                    administrarVigenciasTallas.modificarVigenciasTallas(modificarVigenciasTallas);
                    modificarVigenciasTallas.clear();
                }
                listVigenciasTallas = null;
                RequestContext.getCurrentInstance().update("form:datosVigenciasTallas");
                FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                RequestContext.getCurrentInstance().update("form:growl");
                k = 0;
            }
            guardado = true;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        } catch (Exception e) {
            FacesMessage msg = new FacesMessage("Información", "Hubo un error en el guardado, Por favor intente de nuevo.");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                RequestContext.getCurrentInstance().update("form:growl");
        }
    }

    public void editarCelda() {
        if (vigenciaTallaSeleccionada != null) {
            editarVigenciaTalla = vigenciaTallaSeleccionada;
            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarFecha");
                RequestContext.getCurrentInstance().execute("PF('editarFecha').show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editTipoTalla");
                RequestContext.getCurrentInstance().execute("PF('editTipoTalla').show()");
                cualCelda = -1;

            } else if (cualCelda == 2) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editTallita");
                RequestContext.getCurrentInstance().execute("PF('editTallita').show()");
                cualCelda = -1;
            } else if (cualCelda == 3) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editObservacion");
                RequestContext.getCurrentInstance().execute("PF('editObservacion').show()");
                cualCelda = -1;
            }
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void agregarNuevoTiposTallas() {
        int contador = 0;
        Short a = 0;
        a = null;
        mensajeValidacion = " ";

        if (nuevoVigenciaTalla.getFechavigencia() == (null)) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
        } else {
            contador++;
        }
        if (nuevoVigenciaTalla.getTipoTalla().getSecuencia() == (null)) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
        } else {
            contador++;
        }

        if (contador == 2) {
            FacesContext c = FacesContext.getCurrentInstance();
            if (bandera == 1) {
                //CERRAR FILTRADO
                System.out.println("Desactivar");
                fecha = (Column) c.getViewRoot().findComponent("form:datosVigenciasTallas:fecha");
                fecha.setFilterStyle("display: none; visibility: hidden;");
                idTipoTalla = (Column) c.getViewRoot().findComponent("form:datosVigenciasTallas:idTipoTalla");
                idTipoTalla.setFilterStyle("display: none; visibility: hidden;");
                idTalla = (Column) c.getViewRoot().findComponent("form:datosVigenciasTallas:idTalla");
                idTalla.setFilterStyle("display: none; visibility: hidden;");
                idObservaciones = (Column) c.getViewRoot().findComponent("form:datosVigenciasTallas:idObservaciones");
                idObservaciones.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosVigenciasTallas");
                bandera = 0;
                filtrarVigenciasTallas = null;
                tipoLista = 0;
            }

            k++;
            l = BigInteger.valueOf(k);
            nuevoVigenciaTalla.setSecuencia(l);
            nuevoVigenciaTalla.setEmpleado(empleadoSeleccionado);
            crearVigenciasTallas.add(nuevoVigenciaTalla);
            listVigenciasTallas.add(nuevoVigenciaTalla);
            vigenciaTallaSeleccionada = nuevoVigenciaTalla;
            contarRegistros();
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosVigenciasTallas");
            RequestContext.getCurrentInstance().execute("PF('nuevoRegistroVigenciasTallas').hide()");
            nuevoVigenciaTalla = new VigenciasTallas();
            nuevoVigenciaTalla.setTipoTalla(new TiposTallas());
        } else {
            RequestContext.getCurrentInstance().update("form:validacionNuevaCentroCosto");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevaCentroCosto').show()");
            contador = 0;
        }
    }

    public void limpiarNuevoVigenciasTallas() {
        nuevoVigenciaTalla = new VigenciasTallas();
        nuevoVigenciaTalla.setTipoTalla(new TiposTallas());
        nuevoVigenciaTalla.setEmpleado(new Empleados());
    }

    public void duplicandoVigenciasTallas() {
        if (vigenciaTallaSeleccionada != null) {
            duplicarVigenciaTalla = new VigenciasTallas();
            duplicarVigenciaTalla.setTipoTalla(new TiposTallas());
            k++;
            l = BigInteger.valueOf(k);
            duplicarVigenciaTalla.setSecuencia(l);
            duplicarVigenciaTalla.setFechavigencia(vigenciaTallaSeleccionada.getFechavigencia());
            duplicarVigenciaTalla.setTipoTalla(vigenciaTallaSeleccionada.getTipoTalla());
            duplicarVigenciaTalla.setTalla(vigenciaTallaSeleccionada.getTalla());
            duplicarVigenciaTalla.setObservaciones(vigenciaTallaSeleccionada.getObservaciones());
            duplicarVigenciaTalla.setEmpleado(vigenciaTallaSeleccionada.getEmpleado());
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarPanelVigenciasTallas");
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroVigenciasTallas').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void confirmarDuplicar() {
        int contador = 0;
        mensajeValidacion = " ";
        if (duplicarVigenciaTalla.getFechavigencia() == null) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
        } else {
            contador++;
        }
        if (duplicarVigenciaTalla.getTipoTalla().getSecuencia() == null) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
        } else {
            contador++;
        }
        if (contador == 2) {
            crearVigenciasTallas.add(duplicarVigenciaTalla);
            listVigenciasTallas.add(duplicarVigenciaTalla);
            RequestContext.getCurrentInstance().update("form:datosVigenciasTallas");
            vigenciaTallaSeleccionada = duplicarVigenciaTalla;
            if (guardado == true) {
                guardado = false;
            }
            contarRegistros();

            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                //CERRAR FILTRADO
                fecha = (Column) c.getViewRoot().findComponent("form:datosVigenciasTallas:fecha");
                fecha.setFilterStyle("display: none; visibility: hidden;");
                idTipoTalla = (Column) c.getViewRoot().findComponent("form:datosVigenciasTallas:idTipoTalla");
                idTipoTalla.setFilterStyle("display: none; visibility: hidden;");
                idTalla = (Column) c.getViewRoot().findComponent("form:datosVigenciasTallas:idTalla");
                idTalla.setFilterStyle("display: none; visibility: hidden;");
                idObservaciones = (Column) c.getViewRoot().findComponent("form:datosVigenciasTallas:idObservaciones");
                idObservaciones.setFilterStyle("display: none; visibility: hidden;");

                RequestContext.getCurrentInstance().update("form:datosVigenciasTallas");
                bandera = 0;
                filtrarVigenciasTallas = null;
                tipoLista = 0;
            }
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroVigenciasTallas').hide()");
            duplicarVigenciaTalla = new VigenciasTallas();

        } else {
            contador = 0;
            RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
            RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
        }
    }

    public void limpiarDuplicarVigenciasTallas() {
        duplicarVigenciaTalla = new VigenciasTallas();
        duplicarVigenciaTalla.setTipoTalla(new TiposTallas());
        duplicarVigenciaTalla.setEmpleado(new Empleados());

    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosVigenciasTallasExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "REFERENCIASFAMILIARES", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosVigenciasTallasExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "REFERENCIASFAMILIARES", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (vigenciaTallaSeleccionada != null) {
            int resultado = administrarRastros.obtenerTabla(vigenciaTallaSeleccionada.getSecuencia(), "VIGENCIASTALLAS");
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
        } else if (administrarRastros.verificarHistoricosTabla("VIGENCIASTALLAS")) { // igual acá
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
    }

    public void contarRegistros() {
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
    }

    public void contarRegistrosTiposTallas() {
        RequestContext.getCurrentInstance().update("form:infoRegistroTiposTallas");
    }

    public void habilitarBotonLov() {
        activarLov = false;
        RequestContext.getCurrentInstance().update("form:listaValores");
    }

    public void deshabilitarBotonLov() {
        activarLov = true;
        RequestContext.getCurrentInstance().update("form:listaValores");
    }

    //*/*/*/*/*/*/*/*/*/*-/-*//-*/-*/*/*-*/-*/-*/*/*/*/*/---/*/*/*/*/-*/-*/-*/-*/-*/
    public List<VigenciasTallas> getListVigenciasTallas() {
        if (listVigenciasTallas == null) {
            listVigenciasTallas = administrarVigenciasTallas.consultarVigenciasTallasPorEmpleado(secuenciaEmpleado);
        }
        return listVigenciasTallas;
    }

    public void setListVigenciasTallas(List<VigenciasTallas> listVigenciasTallas) {
        this.listVigenciasTallas = listVigenciasTallas;
    }

    public List<VigenciasTallas> getFiltrarVigenciasTallas() {
        return filtrarVigenciasTallas;
    }

    public void setFiltrarVigenciasTallas(List<VigenciasTallas> filtrarVigenciasTallas) {
        this.filtrarVigenciasTallas = filtrarVigenciasTallas;
    }

    public List<VigenciasTallas> getCrearVigenciasTallas() {
        return crearVigenciasTallas;
    }

    public void setCrearVigenciasTallas(List<VigenciasTallas> crearVigenciasTallas) {
        this.crearVigenciasTallas = crearVigenciasTallas;
    }

    public VigenciasTallas getNuevoVigenciaTalla() {
        return nuevoVigenciaTalla;
    }

    public void setNuevoVigenciaTalla(VigenciasTallas nuevoVigenciaTalla) {
        this.nuevoVigenciaTalla = nuevoVigenciaTalla;
    }

    public VigenciasTallas getDuplicarVigenciaTalla() {
        return duplicarVigenciaTalla;
    }

    public void setDuplicarVigenciaTalla(VigenciasTallas duplicarVigenciaTalla) {
        this.duplicarVigenciaTalla = duplicarVigenciaTalla;
    }

    public int getRegistrosBorrados() {
        return registrosBorrados;
    }

    public void setRegistrosBorrados(int registrosBorrados) {
        this.registrosBorrados = registrosBorrados;
    }

    public BigInteger getSecuenciaPersona() {
        return secuenciaEmpleado;
    }

    public void setSecuenciaPersona(BigInteger secuenciaPersona) {
        this.secuenciaEmpleado = secuenciaPersona;
    }

    public List<TiposTallas> getListaTiposTallas() {
        if (listaTiposTallas == null) {
            listaTiposTallas = administrarVigenciasTallas.consultarLOVTiposTallas();
        }
        return listaTiposTallas;
    }

    public void setListaTiposTallas(List<TiposTallas> listaTiposTallas) {
        this.listaTiposTallas = listaTiposTallas;
    }

    public List<TiposTallas> getFiltradoTiposTallas() {
        return filtradoTiposTallas;
    }

    public void setFiltradoTiposTallas(List<TiposTallas> filtradoTiposTallas) {
        this.filtradoTiposTallas = filtradoTiposTallas;
    }

    public String getMensajeValidacion() {
        return mensajeValidacion;
    }

    public void setMensajeValidacion(String mensajeValidacion) {
        this.mensajeValidacion = mensajeValidacion;
    }

    public VigenciasTallas getEditarVigenciaTalla() {
        return editarVigenciaTalla;
    }

    public void setEditarVigenciaTalla(VigenciasTallas editarVigenciaTalla) {
        this.editarVigenciaTalla = editarVigenciaTalla;
    }

    public TiposTallas getTipoFamiliarSeleccionado() {
        return tipoTallaSeleccionado;
    }

    public void setTipoFamiliarSeleccionado(TiposTallas tipoFamiliarSeleccionado) {
        this.tipoTallaSeleccionado = tipoFamiliarSeleccionado;
    }

    public boolean isGuardado() {
        return guardado;
    }

    public Empleados getEmpleadoSeleccionado() {
        return empleadoSeleccionado;
    }

    public void setEmpleadoSeleccionado(Empleados empleadoSeleccionado) {
        this.empleadoSeleccionado = empleadoSeleccionado;
    }

    public void setGuardado(boolean guardado) {
        this.guardado = guardado;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public void setAceptar(boolean aceptar) {
        this.aceptar = aceptar;
    }

    public String getInfoRegistro() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosVigenciasTallas");
        infoRegistro = String.valueOf(tabla.getRowCount());
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

    public String getInfoRegistroTiposTallas() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovTiposTallas");
        infoRegistroTiposTallas = String.valueOf(tabla.getRowCount());
        return infoRegistroTiposTallas;
    }

    public void setInfoRegistroTiposTallas(String infoRegistroTiposTallas) {
        this.infoRegistroTiposTallas = infoRegistroTiposTallas;
    }

    public boolean isActivarLov() {
        return activarLov;
    }

    public void setActivarLov(boolean activarLov) {
        this.activarLov = activarLov;
    }

    public int getTamano() {
        return tamano;
    }

    public void setTamano(int tamano) {
        this.tamano = tamano;
    }

    public VigenciasTallas getVigenciaTallaSeleccionada() {
        return vigenciaTallaSeleccionada;
    }

    public void setVigenciaTallaSeleccionada(VigenciasTallas vigenciaTallaSeleccionada) {
        this.vigenciaTallaSeleccionada = vigenciaTallaSeleccionada;
    }

}
