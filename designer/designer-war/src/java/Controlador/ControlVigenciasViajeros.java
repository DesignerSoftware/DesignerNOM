/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Empleados;
import Entidades.Tiposviajeros;
import Entidades.VigenciasViajeros;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarRastrosInterface;
import InterfaceAdministrar.AdministrarVigenciasViajerosInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
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
public class ControlVigenciasViajeros implements Serializable {

    @EJB
    AdministrarVigenciasViajerosInterface administrarVigenciasViajeros;
    @EJB
    AdministrarRastrosInterface administrarRastros;
    private List<VigenciasViajeros> listVigenciasViajerosPorEmpleado;
    private List<VigenciasViajeros> filtrarVigenciasViajerosPorEmpleado;
    private List<VigenciasViajeros> crearVigenciasViajerosPorEmpleado;
    private List<VigenciasViajeros> modificarVigenciasViajerosPorEmpleado;
    private List<VigenciasViajeros> borrarVigenciasViajerosPorEmpleado;
    private VigenciasViajeros nuevoVigenciasViajeros;
    private VigenciasViajeros duplicarVigenciasViajeros;
    private VigenciasViajeros editarVigenciasViajeros;
    private VigenciasViajeros vigenciaSeleccionada;
    //otros
    private int cualCelda, tipoLista, tipoActualizacion, k, bandera;
    private BigInteger l;
    private boolean aceptar, guardado;
    //AutoCompletar
    private boolean permitirIndex;
    //RASTRO
    private Column fecha, parentesco;
    //borrado
    private int registrosBorrados;
    private String mensajeValidacion;
    private BigInteger secuenciaEmpleado;
    //Empleado
    private Empleados empleadoSeleccionado;
    //autocompletar
    private String viajeros;
    private List<Tiposviajeros> listaTiposviajeros;
    private List<Tiposviajeros> filtradoTiposviajeros;
    private Tiposviajeros viajeroSeleccionado;
    private String nuevoYduplicarCompletarViajero;
    private String altoTabla;
    public String infoRegistro;
    public String infoRegistroTiposViajeros;
    //
    private DataTable tablaC;
    //
    private Boolean activarLOV;

    public ControlVigenciasViajeros() {
        listVigenciasViajerosPorEmpleado = null;
        crearVigenciasViajerosPorEmpleado = new ArrayList<VigenciasViajeros>();
        modificarVigenciasViajerosPorEmpleado = new ArrayList<VigenciasViajeros>();
        borrarVigenciasViajerosPorEmpleado = new ArrayList<VigenciasViajeros>();
        permitirIndex = true;
        editarVigenciasViajeros = new VigenciasViajeros();
        nuevoVigenciasViajeros = new VigenciasViajeros();
        nuevoVigenciasViajeros.setTipoViajero(new Tiposviajeros());
        //duplicarVigenciasViajeros = new VigenciasViajeros();
        empleadoSeleccionado = null;
        bandera = 0;
        //secuenciaEmpleado = null;
        listaTiposviajeros = null;
        filtradoTiposviajeros = null;
        guardado = true;
        aceptar = true;
        altoTabla = "292";
        vigenciaSeleccionada = null;
        activarLOV = true;
        viajeroSeleccionado = null;
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarVigenciasViajeros.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct ControlVigenciasCargos: " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void recibirEmpleado(BigInteger sec) {
        secuenciaEmpleado = sec;
        getListVigenciasViajerosPorEmpleado();
        contarRegistrosTV();
        if (listVigenciasViajerosPorEmpleado != null) {
            vigenciaSeleccionada = listVigenciasViajerosPorEmpleado.get(0);
            modificarInfoRegistro(listVigenciasViajerosPorEmpleado.size());
        } else {
            modificarInfoRegistro(0);
        }
    }

    public void mostrarInfo(VigenciasViajeros vViajero, int celda) {
        int contador = 0;
        int fechas = 0;
        mensajeValidacion = " ";
        vigenciaSeleccionada = vViajero;
        cualCelda = celda;
        RequestContext context = RequestContext.getCurrentInstance();
        if (permitirIndex) {
            if (vigenciaSeleccionada.getFechavigencia() == null) {
                mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                contador++;
            } else {
                for (int j = 0; j < listVigenciasViajerosPorEmpleado.size(); j++) {
                    if (listVigenciasViajerosPorEmpleado.get(j) != vViajero) {
                        if (vigenciaSeleccionada.getFechavigencia().equals(listVigenciasViajerosPorEmpleado.get(j).getFechavigencia())) {
                            fechas++;
                        }
                    }
                }
            }
            if (fechas > 0) {
                context.update("form:validacionFechas");
                context.execute("validacionFechas.show()");
                contador++;
            }
            if (contador == 0) {
                if (!crearVigenciasViajerosPorEmpleado.contains(vigenciaSeleccionada)) {
                    if (modificarVigenciasViajerosPorEmpleado.isEmpty()) {
                        modificarVigenciasViajerosPorEmpleado.add(vigenciaSeleccionada);
                    } else if (!modificarVigenciasViajerosPorEmpleado.contains(vigenciaSeleccionada)) {
                        modificarVigenciasViajerosPorEmpleado.add(vigenciaSeleccionada);
                    }
                    if (guardado) {
                        guardado = false;
                        context.update("form:ACEPTAR");
                    }
                    context.update("form:datosViajeros");

                }
            } else {
                context.update("form:validacionModificar");
                context.execute("validacionModificar.show()");
                cancelarModificacion();
            }

        }
    }

    public void cambiarIndice(VigenciasViajeros vViajeros, int celda) {
        RequestContext context = RequestContext.getCurrentInstance();
        if (permitirIndex == true) {
            vigenciaSeleccionada = vViajeros;
            cualCelda = celda;
            if (cualCelda == 1) {
                activarLOV = false;
                RequestContext.getCurrentInstance().update("form:listaValores");
                viajeros = vigenciaSeleccionada.getTipoViajero().getNombre();

            }
        } else {
            context.execute("datosViajeros.selectRow(" + vViajeros + ", false); datosViajeros.unselectAllRows()");
        }
        RequestContext.getCurrentInstance().update("form:listaValores");
    }

    public void asignarIndex(VigenciasViajeros vViajeros, int LND, int dig) {
        RequestContext context = RequestContext.getCurrentInstance();
        vigenciaSeleccionada = vViajeros;
        activarLOV = false;
        viajeroSeleccionado = null;
        RequestContext.getCurrentInstance().update("form:listaValores");
        try {
            if (LND == 0) {
                tipoActualizacion = 0;
            } else if (LND == 1) {
                tipoActualizacion = 1;
            } else if (LND == 2) {
                tipoActualizacion = 2;
            }
            if (dig == 1) {
                modificarInfoRegistroViajero(listaTiposviajeros.size());
                context.update("form:sucursalesDialogo");
                context.execute("sucursalesDialogo.show()");
                dig = -1;
            }
        } catch (Exception e) {
            System.out.println("ERROR CONTROLBETAEMPLVIGENCIANORMALABORAL.asignarIndex ERROR======" + e.getMessage());
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void listaValoresBoton() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (vigenciaSeleccionada == null) {
            context.execute("seleccionarRegistro.show()");
        } else {
            if (vigenciaSeleccionada != null) {
                if (cualCelda == 1) {
                    viajeroSeleccionado = null;
                    context.update("form:sucursalesDialogo");
                    context.execute("sucursalesDialogo.show()");
                    tipoActualizacion = 0;
                }
            }
        }
    }

    public void cancelarModificacion() {
        cerrarFiltrado();
        activarLOV = true;
        RequestContext.getCurrentInstance().update("form:listaValores");
        borrarVigenciasViajerosPorEmpleado.clear();
        crearVigenciasViajerosPorEmpleado.clear();
        modificarVigenciasViajerosPorEmpleado.clear();
        vigenciaSeleccionada = null;
        k = 0;
        listVigenciasViajerosPorEmpleado = null;
        getListVigenciasViajerosPorEmpleado();
        contarRegistrosTV();
        guardado = true;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("form:informacionRegistro");
        context.update("form:datosViajeros");
        context.update("form:ACEPTAR");
    }

    public void activarCtrlF11() {
        if (bandera == 0) {
            FacesContext c = FacesContext.getCurrentInstance();
            fecha = (Column) c.getViewRoot().findComponent("form:datosViajeros:fecha");
            fecha.setFilterStyle("width: 85%;");
            parentesco = (Column) c.getViewRoot().findComponent("form:datosViajeros:parentesco");
            parentesco.setFilterStyle("width: 85%");
            altoTabla = "272";
            RequestContext.getCurrentInstance().update("form:datosViajeros");
            bandera = 1;
        } else if (bandera == 1) {
            cerrarFiltrado();
        }
    }

    private void cerrarFiltrado() {
        FacesContext c = FacesContext.getCurrentInstance();
        fecha = (Column) c.getViewRoot().findComponent("form:datosViajeros:fecha");
        fecha.setFilterStyle("display: none; visibility: hidden;");
        parentesco = (Column) c.getViewRoot().findComponent("form:datosViajeros:parentesco");
        parentesco.setFilterStyle("display: none; visibility: hidden;");
        altoTabla = "292";
        RequestContext.getCurrentInstance().update("form:datosViajeros");
        bandera = 0;
        filtrarVigenciasViajerosPorEmpleado = null;
        tipoLista = 0;
    }

    public void modificandoVigenciasViajeros(VigenciasViajeros vViajeros, String confirmarCambio, String valorConfirmar) {
        vigenciaSeleccionada = vViajeros;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        int contador = 0;
        boolean banderita = false;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("N")) {
            activarLOV = true;
            RequestContext.getCurrentInstance().update("form:listaValores");
            if (!crearVigenciasViajerosPorEmpleado.contains(vigenciaSeleccionada)) {
                if (vigenciaSeleccionada.getFechavigencia() == null) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita = false;
                } else {
                    for (int j = 0; j < listVigenciasViajerosPorEmpleado.size(); j++) {
                        if (listVigenciasViajerosPorEmpleado.get(j) != vViajeros) {
                            if (vigenciaSeleccionada.getFechavigencia().equals(listVigenciasViajerosPorEmpleado.get(j).getFechavigencia())) {
                                contador++;
                            }
                        }
                    }
                    if (contador > 0) {
                        mensajeValidacion = "FECHAS REPETIDAS";
                        banderita = false;
                    } else {
                        banderita = true;
                    }
                }

                if (banderita == true) {
                    if (modificarVigenciasViajerosPorEmpleado.isEmpty()) {
                        modificarVigenciasViajerosPorEmpleado.add(vigenciaSeleccionada);
                    } else if (!modificarVigenciasViajerosPorEmpleado.contains(vigenciaSeleccionada)) {
                        modificarVigenciasViajerosPorEmpleado.add(vigenciaSeleccionada);
                    }
                    if (guardado) {
                        guardado = false;
                        context.update("form:ACEPTAR");
                    }

                } else {
                    context.update("form:validacionModificar");
                    context.execute("validacionModificar.show()");
                    cancelarModificacion();
                }
            }

            context.update("form:datosViajeros");
        } else if (confirmarCambio.equalsIgnoreCase("VIGENCIASVIAJEROS")) {
            activarLOV = false;
            RequestContext.getCurrentInstance().update("form:listaValores");
            if (!vigenciaSeleccionada.getTipoViajero().getNombre().equals("")) {
                vigenciaSeleccionada.getTipoViajero().setNombre(viajeros);
                for (int i = 0; i < listaTiposviajeros.size(); i++) {
                    if (listaTiposviajeros.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }

                if (coincidencias == 1) {
                    vigenciaSeleccionada.setTipoViajero(listaTiposviajeros.get(indiceUnicoElemento));
                    listaTiposviajeros.clear();
                    listaTiposviajeros = null;
                    //getListaTiposFamiliares();

                } else {
                    permitirIndex = false;
                    context.update("form:sucursalesDialogo");
                    context.execute("sucursalesDialogo.show()");
                    tipoActualizacion = 0;
                }
            } else {
                vigenciaSeleccionada.getTipoViajero().setNombre(viajeros);
                context.update("form:sucursalesDialogo");
                context.execute("sucursalesDialogo.show()");
            }

            if (coincidencias == 1) {
                if (!crearVigenciasViajerosPorEmpleado.contains(vigenciaSeleccionada)) {

                    if (modificarVigenciasViajerosPorEmpleado.isEmpty()) {
                        modificarVigenciasViajerosPorEmpleado.add(vigenciaSeleccionada);
                    } else if (!modificarVigenciasViajerosPorEmpleado.contains(vigenciaSeleccionada)) {
                        modificarVigenciasViajerosPorEmpleado.add(vigenciaSeleccionada);
                    }
                }

                if (guardado) {
                    guardado = false;
                    context.update("form:ACEPTAR");
                }
                context.update("form:datosViajeros");
            }
        }

    }

    public void actualizarViajero() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            vigenciaSeleccionada.setTipoViajero(viajeroSeleccionado);
            if (!crearVigenciasViajerosPorEmpleado.contains(vigenciaSeleccionada)) {
                if (modificarVigenciasViajerosPorEmpleado.isEmpty()) {
                    modificarVigenciasViajerosPorEmpleado.add(vigenciaSeleccionada);
                } else if (!modificarVigenciasViajerosPorEmpleado.contains(vigenciaSeleccionada)) {
                    modificarVigenciasViajerosPorEmpleado.add(vigenciaSeleccionada);
                }
            }

            if (guardado) {
                guardado = false;
                context.update("form:ACEPTAR");
            }
            permitirIndex = true;
            context.update("form:datosViajeros");
        } else if (tipoActualizacion == 1) {
            nuevoVigenciasViajeros.setTipoViajero(viajeroSeleccionado);
            context.update("formularioDialogos:nuevoNombreSucursal");
        } else if (tipoActualizacion == 2) {
            duplicarVigenciasViajeros.setTipoViajero(viajeroSeleccionado);
            context.update("formularioDialogos:duplicarTipoCentroCostos");
        }
        filtradoTiposviajeros = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        context.reset("form:lovTiposFamiliares:globalFilter");
        context.execute("lovTiposFamiliares.clearFilters()");
        context.execute("sucursalesDialogo.hide()");
        //context.update("form:lovTiposFamiliares");
        //context.update("form:datosViajeros");
    }

    public void cancelarCambioVigenciaViajero() {
        filtradoTiposviajeros = null;
        viajeroSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovTiposFamiliares:globalFilter");
        context.execute("lovTiposFamiliares.clearFilters()");
        context.execute("sucursalesDialogo.hide()");
    }

    public void valoresBackupAutocompletar(int tipoNuevo) {
        if (tipoNuevo == 1) {
            nuevoYduplicarCompletarViajero = nuevoVigenciasViajeros.getTipoViajero().getNombre();
        } else if (tipoNuevo == 2) {
            nuevoYduplicarCompletarViajero = duplicarVigenciasViajeros.getTipoViajero().getNombre();
        }
    }

    public void autocompletarNuevo(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("VIGENCIASVIAJEROS")) {

            if (!nuevoVigenciasViajeros.getTipoViajero().getNombre().equals("")) {
                nuevoVigenciasViajeros.getTipoViajero().setNombre(nuevoYduplicarCompletarViajero);
                getListaTiposviajeros();
                for (int i = 0; i < listaTiposviajeros.size(); i++) {
                    if (listaTiposviajeros.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    nuevoVigenciasViajeros.setTipoViajero(listaTiposviajeros.get(indiceUnicoElemento));
                    listaTiposviajeros = null;
                    getListaTiposviajeros();
                } else {
                    context.update("form:sucursalesDialogo");
                    context.execute("sucursalesDialogo.show()");
                    tipoActualizacion = tipoNuevo;
                }
            } else {
                nuevoVigenciasViajeros.getTipoViajero().setNombre(nuevoYduplicarCompletarViajero);
                nuevoVigenciasViajeros.setTipoViajero(new Tiposviajeros());
                nuevoVigenciasViajeros.getTipoViajero().setNombre(" ");
            }
            context.update("formularioDialogos:nuevoNombreSucursal");
        }
    }

    public void asignarVariableTiposviajeros(int tipoNuevo) {
        if (tipoNuevo == 0) {
            tipoActualizacion = 1;
        }
        if (tipoNuevo == 1) {
            tipoActualizacion = 2;
        }
        viajeroSeleccionado = null;
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("form:sucursalesDialogo");
        context.execute("sucursalesDialogo.show()");
    }

    public void limpiarNuevoViajero() {
        try {
            nuevoVigenciasViajeros = new VigenciasViajeros();
            nuevoVigenciasViajeros.setTipoViajero(new Tiposviajeros());
        } catch (Exception e) {
            System.out.println("Error ControlVigenciasViajeros LIMPIAR NUEVO VIAJERO ERROR :" + e.getMessage());
        }
    }

    public void cargarTiposviajerosNuevoYDuplicar(int tipoNuevo) {
        if (tipoNuevo == 0) {
            tipoActualizacion = 1;
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("form:sucursalesDialogo");
            context.execute("sucursalesDialogo.show()");
        } else if (tipoNuevo == 1) {
            activarLOV = false;
            RequestContext.getCurrentInstance().update("form:listaValores");
            tipoActualizacion = 2;
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("form:sucursalesDialogo");
            context.execute("sucursalesDialogo.show()");
        }
    }

    public void autocompletarDuplicado(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("VIGENCIASVIAJEROS")) {

            if (!duplicarVigenciasViajeros.getTipoViajero().getNombre().equals("")) {
                duplicarVigenciasViajeros.getTipoViajero().setNombre(nuevoYduplicarCompletarViajero);
                for (int i = 0; i < listaTiposviajeros.size(); i++) {
                    if (listaTiposviajeros.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    duplicarVigenciasViajeros.setTipoViajero(listaTiposviajeros.get(indiceUnicoElemento));
                    listaTiposviajeros = null;
                    getListaTiposviajeros();
                } else {
                    context.update("form:sucursalesDialogo");
                    context.execute("sucursalesDialogo.show()");
                    tipoActualizacion = tipoNuevo;
                }
            } else {
                if (tipoNuevo == 2) {
                    //duplicarVigenciasViajeros.getTipoViajero().setNombre(nuevoYduplicarCompletarNormaLaboral);
                    duplicarVigenciasViajeros.setTipoViajero(new Tiposviajeros());
                    duplicarVigenciasViajeros.getTipoViajero().setNombre(" ");
                    vigenciaSeleccionada.getTipoViajero().setNombre(nuevoYduplicarCompletarViajero);
                }

            }
            context.update("formularioDialogos:duplicarTipoCentroCostos");
        }
    }

    public void borrandoHvEntrevistas() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (vigenciaSeleccionada == null) {
            context.execute("seleccionarRegistro.show()");
        } else {
            if (vigenciaSeleccionada != null) {
                if (!modificarVigenciasViajerosPorEmpleado.isEmpty() && modificarVigenciasViajerosPorEmpleado.contains(vigenciaSeleccionada)) {
                    int modIndex = modificarVigenciasViajerosPorEmpleado.indexOf(vigenciaSeleccionada);
                    modificarVigenciasViajerosPorEmpleado.remove(modIndex);
                    borrarVigenciasViajerosPorEmpleado.add(vigenciaSeleccionada);
                } else if (!crearVigenciasViajerosPorEmpleado.isEmpty() && crearVigenciasViajerosPorEmpleado.contains(vigenciaSeleccionada)) {
                    int crearIndex = crearVigenciasViajerosPorEmpleado.indexOf(vigenciaSeleccionada);
                    crearVigenciasViajerosPorEmpleado.remove(crearIndex);
                } else {
                    borrarVigenciasViajerosPorEmpleado.add(vigenciaSeleccionada);
                }
                listVigenciasViajerosPorEmpleado.remove(vigenciaSeleccionada);
                if (tipoLista == 1) {
                    filtrarVigenciasViajerosPorEmpleado.remove(vigenciaSeleccionada);
                }
                modificarInfoRegistro(listVigenciasViajerosPorEmpleado.size());
                vigenciaSeleccionada = null;
                activarLOV = true;
                RequestContext.getCurrentInstance().update("form:listaValores");
                if (guardado) {
                    guardado = false;
                    context.update("form:ACEPTAR");
                }
                context.update("form:informacionRegistro");
                context.update("form:datosViajeros");

            }
        }
    }

    public void revisarDialogoGuardar() {

        if (!borrarVigenciasViajerosPorEmpleado.isEmpty() || !crearVigenciasViajerosPorEmpleado.isEmpty() || !modificarVigenciasViajerosPorEmpleado.isEmpty()) {
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("form:confirmarGuardar");
            context.execute("confirmarGuardar.show()");
        }

    }

    public void guardarVigenciasViajeros() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (guardado == false) {
            if (!borrarVigenciasViajerosPorEmpleado.isEmpty()) {
                for (int i = 0; i < borrarVigenciasViajerosPorEmpleado.size(); i++) {
                    administrarVigenciasViajeros.borrarVigenciasViajeros(borrarVigenciasViajerosPorEmpleado);
                }
                //mostrarBorrados
                registrosBorrados = borrarVigenciasViajerosPorEmpleado.size();
                context.update("form:mostrarBorrados");
                context.execute("mostrarBorrados.show()");
                borrarVigenciasViajerosPorEmpleado.clear();
            }
            System.out.println("crearVigenciasViajerosPorEmpleado: " + crearVigenciasViajerosPorEmpleado);

            if (!crearVigenciasViajerosPorEmpleado.isEmpty()) {
                for (int i = 0; i < crearVigenciasViajerosPorEmpleado.size(); i++) {
                    administrarVigenciasViajeros.crearVigenciasViajeros(crearVigenciasViajerosPorEmpleado);
                }
                crearVigenciasViajerosPorEmpleado.clear();
            }

            if (!modificarVigenciasViajerosPorEmpleado.isEmpty()) {
                administrarVigenciasViajeros.modificarVigenciasViajeros(modificarVigenciasViajerosPorEmpleado);
                modificarVigenciasViajerosPorEmpleado.clear();
            }
            listVigenciasViajerosPorEmpleado = null;
            getListVigenciasViajerosPorEmpleado();
            contarRegistrosTV();
            if (listVigenciasViajerosPorEmpleado != null && !listVigenciasViajerosPorEmpleado.isEmpty()) {
                vigenciaSeleccionada = listVigenciasViajerosPorEmpleado.get(0);
                modificarInfoRegistro(listVigenciasViajerosPorEmpleado.size());
            } else {
                modificarInfoRegistro(0);
            }
            activarLOV = true;
            context.update("form:datosViajeros");
            RequestContext.getCurrentInstance().update("form:listaValores");
            guardado = true;
            context.update("form:ACEPTAR");
            context.update("form:informacionRegistro");
            k = 0;
            permitirIndex = true;
            FacesMessage msg = new FacesMessage("Información", "Se guardarón los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            context.update("form:growl");
        }
        // vigenciaSeleccionada = null;
    }

    public void editarCelda() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (vigenciaSeleccionada == null) {
            context.execute("seleccionarRegistro.show()");
        } else {
            if (vigenciaSeleccionada != null) {
                editarVigenciasViajeros = vigenciaSeleccionada;
                if (cualCelda == 0) {
                    context.update("formularioDialogos:editarFecha");
                    context.execute("editarFecha.show()");
                    cualCelda = -1;
                } else if (cualCelda == 1) {
                    context.update("formularioDialogos:editPuntaje");
                    context.execute("editPuntaje.show()");
                    cualCelda = -1;
                }

            }
        }
    }

    public void agregarNuevoVigenciasViajeros() {
        int contador = 0;
        int fechas = 0;
        int pasa = 0;
        mensajeValidacion = " ";
        nuevoVigenciasViajeros.setEmpleado(empleadoSeleccionado);
        RequestContext context = RequestContext.getCurrentInstance();
        //vigenciaSeleccionada = null;
        if (nuevoVigenciasViajeros.getFechavigencia() == null || nuevoVigenciasViajeros.getFechavigencia().equals("")) {
            mensajeValidacion = " *Fecha\n";
        } else {
            if (listVigenciasViajerosPorEmpleado != null) {
                for (int i = 0; i < listVigenciasViajerosPorEmpleado.size(); i++) {
                    if (nuevoVigenciasViajeros.getFechavigencia().equals(listVigenciasViajerosPorEmpleado.get(i).getFechavigencia())) {
                        fechas++;
                    }
                }
            }
            if (fechas > 0) {
                context.update("form:validacionFechas");
                context.execute("validacionFechas.show()");
                pasa++;
            } else {
                contador++;
            }
        }
        if (nuevoVigenciasViajeros.getTipoViajero().getSecuencia() == null) {
            mensajeValidacion = mensajeValidacion + "   *Tipo Viajero\n";
        } else {
            contador++;
        }
        if (contador == 2 && pasa == 0) {
            //FacesContext c = FacesContext.getCurrentInstance();
            if (bandera == 1) {
                cerrarFiltrado();
            }
            k++;
            l = BigInteger.valueOf(k);
            nuevoVigenciasViajeros.setSecuencia(l);
            //nuevoVigenciasViajeros.setEmpleado(empleadoSeleccionado);
            crearVigenciasViajerosPorEmpleado.add(nuevoVigenciasViajeros);
            if (listVigenciasViajerosPorEmpleado == null || listVigenciasViajerosPorEmpleado.isEmpty()) {
                listVigenciasViajerosPorEmpleado = new ArrayList<VigenciasViajeros>();
                listVigenciasViajerosPorEmpleado.add(nuevoVigenciasViajeros);
            } else {
                listVigenciasViajerosPorEmpleado.add(nuevoVigenciasViajeros);
            }
            activarLOV = true;
            RequestContext.getCurrentInstance().update("form:listaValores");
            modificarInfoRegistro(listVigenciasViajerosPorEmpleado.size());
            context.update("form:informacionRegistro");
            System.out.println("nuevoVigenciasViajeros: " + nuevoVigenciasViajeros);
            nuevoVigenciasViajeros = new VigenciasViajeros();
            nuevoVigenciasViajeros.setTipoViajero(new Tiposviajeros());

            context.update("form:datosViajeros");
            if (guardado) {
                guardado = false;
                context.update("form:ACEPTAR");
            }
            context.execute("nuevoRegistroEvalEmpresas.hide()");
            // vigenciaSeleccionada = null;
        } else if (pasa == 0 && contador != 2) {
            context.update("form:validacionNuevaCentroCosto");
            context.execute("validacionNuevaCentroCosto.show()");
            contador = 0;
            pasa = 0;
        }
        for (int i = 0; i < crearVigenciasViajerosPorEmpleado.size(); i++) {
            System.out.println("crearVigenciasViajerosPorEmpleado Poss " + i + " : " + crearVigenciasViajerosPorEmpleado.get(i));
        }
    }

    public void limpiarNuevoVigenciasViajeros() {
        nuevoVigenciasViajeros = new VigenciasViajeros();
        nuevoVigenciasViajeros.setTipoViajero(new Tiposviajeros());

    }

    //------------------------------------------------------------------------------
    public void duplicandoVigenciasViajeros() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (vigenciaSeleccionada == null) {
            context.execute("seleccionarRegistro.show()");
        } else {
            if (vigenciaSeleccionada != null) {
                duplicarVigenciasViajeros = new VigenciasViajeros();
                k++;
                l = BigInteger.valueOf(k);

                duplicarVigenciasViajeros.setSecuencia(l);
                duplicarVigenciasViajeros.setEmpleado(vigenciaSeleccionada.getEmpleado());
                duplicarVigenciasViajeros.setFechavigencia(vigenciaSeleccionada.getFechavigencia());
                duplicarVigenciasViajeros.setTipoViajero(vigenciaSeleccionada.getTipoViajero());

                context.update("formularioDialogos:duplicarEvC");
                context.execute("duplicarRegistroEvalCompetencias.show()");
            }
        }
    }

    public void confirmarDuplicar() {
        int contador = 0;
        mensajeValidacion = " ";
        int pasa = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        Short a = 0;
        int fechas = 0;
        a = null;

        if (duplicarVigenciasViajeros.getFechavigencia() == null) {
            mensajeValidacion = mensajeValidacion + "   * Fecha \n";
        } else {

            for (int j = 0; j < listVigenciasViajerosPorEmpleado.size(); j++) {
                if (duplicarVigenciasViajeros.getFechavigencia().equals(listVigenciasViajerosPorEmpleado.get(j).getFechavigencia())) {
                    fechas++;
                }
            }
            if (fechas > 0) {
                context.update("form:validacionFechas");
                context.execute("validacionFechas.show()");
                pasa++;

            } else {
                contador++;
            }
        }
        if (duplicarVigenciasViajeros.getTipoViajero().getNombre() == null || duplicarVigenciasViajeros.getTipoViajero().getNombre().isEmpty() || duplicarVigenciasViajeros.getTipoViajero().getNombre().equals(" ")) {
            mensajeValidacion = mensajeValidacion + "   * Tipo Viajero \n";
        } else {
            contador++;
        }
        if (duplicarVigenciasViajeros.getEmpleado().getSecuencia() == null) {
            duplicarVigenciasViajeros.setEmpleado(empleadoSeleccionado);
        }
        if (contador == 2 && pasa == 0) {
            listVigenciasViajerosPorEmpleado.add(duplicarVigenciasViajeros);
            crearVigenciasViajerosPorEmpleado.add(duplicarVigenciasViajeros);
            activarLOV = true;
            RequestContext.getCurrentInstance().update("form:listaValores");
            modificarInfoRegistro(listVigenciasViajerosPorEmpleado.size());
            context.update("form:informacionRegistro");
            vigenciaSeleccionada = listVigenciasViajerosPorEmpleado.get(listVigenciasViajerosPorEmpleado.indexOf(duplicarVigenciasViajeros));
            context.update("form:datosViajeros");
            if (guardado) {
                guardado = false;
                context.update("form:ACEPTAR");
            }
            if (bandera == 1) {
                cerrarFiltrado();
            }
            duplicarVigenciasViajeros = new VigenciasViajeros();
            RequestContext.getCurrentInstance().execute("duplicarRegistroEvalCompetencias.hide()");

        } else if (pasa == 0 && contador != 2) {
            context.update("form:validacionDuplicarVigencia");
            context.execute("validacionDuplicarVigencia.show()");
            contador = 0;
            pasa = 0;
        }
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void limpiarDuplicarVigenciasViajeros() {
        duplicarVigenciasViajeros = new VigenciasViajeros();
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosViajerosExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "VIGENCIASVIAJEROS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosViajerosExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "VIGENCIASVIAJEROS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (vigenciaSeleccionada != null) {
            int resultado = administrarRastros.obtenerTabla(vigenciaSeleccionada.getSecuencia(), "VIGENCIASVIAJEROS"); //En ENCARGATURAS lo cambia por el nombre de su tabla
            if (resultado == 1) {
                context.execute("errorObjetosDB.show()");
            } else if (resultado == 2) {
                context.execute("confirmarRastro.show()");
            } else if (resultado == 3) {
                context.execute("errorRegistroRastro.show()");
            } else if (resultado == 4) {
                context.execute("errorTablaConRastro.show()");
            } else if (resultado == 5) {
                context.execute("errorTablaSinRastro.show()");
            }
        } else {
            if (administrarRastros.verificarHistoricosTabla("VIGENCIASVIAJEROS")) { // igual acá
                context.execute("confirmarRastroHistorico.show()");
            } else {
                context.execute("errorRastroHistorico.show()");
            }
        }
    }

    public void eventoFiltrar() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
        activarLOV = true;
        RequestContext.getCurrentInstance().update("form:listaValores");
        RequestContext context = RequestContext.getCurrentInstance();
        modificarInfoRegistro(filtrarVigenciasViajerosPorEmpleado.size());
        context.update("form:informacionRegistro");
    }

    public void eventoFiltrarViajero() {
        modificarInfoRegistroViajero(filtradoTiposviajeros.size());
        RequestContext.getCurrentInstance().update("form:infoRegistroTiposViajeros");
    }

    private void modificarInfoRegistro(int valor) {
        infoRegistro = String.valueOf(valor);
        System.out.println("infoRegistro: " + infoRegistro);
    }

    private void modificarInfoRegistroViajero(int valor) {
        infoRegistroTiposViajeros = String.valueOf(valor);
    }

    public void contarRegistrosTV() {
        if (listVigenciasViajerosPorEmpleado != null) {
            modificarInfoRegistro(listVigenciasViajerosPorEmpleado.size());
        } else {
            modificarInfoRegistro(0);
        }
    }

    public void recordarSeleccion() {
        if (vigenciaSeleccionada != null) {
            FacesContext c = FacesContext.getCurrentInstance();
            tablaC = (DataTable) c.getViewRoot().findComponent("form:datosViajeros");
            tablaC.setSelection(vigenciaSeleccionada);
        } else {
            vigenciaSeleccionada = null;
        }
    }

    public void anularLOV() {
        activarLOV = true;
        RequestContext.getCurrentInstance().update("form:listaValores");
    }

    //*/*/*/*/*/*/*/*/*/*-/-*//-*/-*/*/*-*/-*/-*/*/*/*/*/---/*/*/*/*/-*/-*/-*/-*/-*/
    public List<VigenciasViajeros> getListVigenciasViajerosPorEmpleado() {
        if (listVigenciasViajerosPorEmpleado == null) {
            listVigenciasViajerosPorEmpleado = administrarVigenciasViajeros.consultarVigenciasViajerosPorEmpleado(secuenciaEmpleado);
        }
        return listVigenciasViajerosPorEmpleado;
    }

    public void setListVigenciasViajerosPorEmpleado(List<VigenciasViajeros> listVigenciasViajerosPorEmpleado) {
        this.listVigenciasViajerosPorEmpleado = listVigenciasViajerosPorEmpleado;
    }

    public List<VigenciasViajeros> getFiltrarVigenciasViajerosPorEmpleado() {
        return filtrarVigenciasViajerosPorEmpleado;
    }

    public void setFiltrarVigenciasViajerosPorEmpleado(List<VigenciasViajeros> filtrarVigenciasViajerosPorEmpleado) {
        this.filtrarVigenciasViajerosPorEmpleado = filtrarVigenciasViajerosPorEmpleado;
    }

    public VigenciasViajeros getNuevoVigenciasViajeros() {
        return nuevoVigenciasViajeros;
    }

    public void setNuevoVigenciasViajeros(VigenciasViajeros nuevoVigenciasViajeros) {
        this.nuevoVigenciasViajeros = nuevoVigenciasViajeros;
    }

    public VigenciasViajeros getDuplicarVigenciasViajeros() {
        return duplicarVigenciasViajeros;
    }

    public void setDuplicarVigenciasViajeros(VigenciasViajeros duplicarVigenciasViajeros) {
        this.duplicarVigenciasViajeros = duplicarVigenciasViajeros;
    }

    public VigenciasViajeros getEditarVigenciasViajeros() {
        return editarVigenciasViajeros;
    }

    public void setEditarVigenciasViajeros(VigenciasViajeros editarVigenciasViajeros) {
        this.editarVigenciasViajeros = editarVigenciasViajeros;
    }

    public int getRegistrosBorrados() {
        return registrosBorrados;
    }

    public void setRegistrosBorrados(int registrosBorrados) {
        this.registrosBorrados = registrosBorrados;
    }

    public String getMensajeValidacion() {
        return mensajeValidacion;
    }

    public void setMensajeValidacion(String mensajeValidacion) {
        this.mensajeValidacion = mensajeValidacion;
    }

    public Empleados getEmpleadoSeleccionado() {
        if (empleadoSeleccionado == null) {
            empleadoSeleccionado = administrarVigenciasViajeros.consultarEmpleado(secuenciaEmpleado);
        }
        return empleadoSeleccionado;
    }

    public void setEmpleadoSeleccionado(Empleados empleadoSeleccionado) {
        this.empleadoSeleccionado = empleadoSeleccionado;
    }

    public List<Tiposviajeros> getListaTiposviajeros() {
        if (listaTiposviajeros == null) {
            listaTiposviajeros = administrarVigenciasViajeros.consultarLOVTiposViajeros();
        }
        return listaTiposviajeros;
    }

    public void setListaTiposviajeros(List<Tiposviajeros> listaTiposviajeros) {
        this.listaTiposviajeros = listaTiposviajeros;
    }

    public List<Tiposviajeros> getFiltradoTiposviajeros() {
        return filtradoTiposviajeros;
    }

    public void setFiltradoTiposviajeros(List<Tiposviajeros> filtradoTiposviajeros) {
        this.filtradoTiposviajeros = filtradoTiposviajeros;
    }

    public Tiposviajeros getNormaLaboralSeleccionada() {
        return viajeroSeleccionado;
    }

    public void setNormaLaboralSeleccionada(Tiposviajeros normaLaboralSeleccionada) {
        this.viajeroSeleccionado = normaLaboralSeleccionada;
    }

    public boolean isGuardado() {
        return guardado;
    }

    public void setGuardado(boolean guardado) {
        this.guardado = guardado;
    }

    public VigenciasViajeros getVigenciaSeleccionada() {
        return vigenciaSeleccionada;
    }

    public void setVigenciaSeleccionada(VigenciasViajeros vigenciaSeleccionada) {
        this.vigenciaSeleccionada = vigenciaSeleccionada;
    }

    public String getAltoTabla() {
        return altoTabla;
    }

    public String getInfoRegistro() {
        return infoRegistro;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public void setAceptar(boolean aceptar) {
        this.aceptar = aceptar;
    }

    public String getInfoRegistroTiposViajeros() {
        return infoRegistroTiposViajeros;
    }

    public Boolean getActivarLOV() {
        return activarLOV;
    }

    public void setActivarLOV(Boolean activarLOV) {
        this.activarLOV = activarLOV;
    }
}
