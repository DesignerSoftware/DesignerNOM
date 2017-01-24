/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.UbicacionesGeograficas;
import Entidades.Empresas;
import Entidades.Ciudades;
import Entidades.SucursalesPila;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarUbicacionesGeograficasInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
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
public class ControlUbicacionesGeograficas implements Serializable {

    @EJB
    AdministrarUbicacionesGeograficasInterface administrarUbicacionesGeograficas;
    @EJB
    AdministrarRastrosInterface administrarRastros;
    private int cualCelda, tipoLista, tipoActualizacion, k, bandera;
    private BigInteger l;
    private boolean aceptar, guardado;
    //AutoCompletar
    private boolean permitirIndex;
    //borrado
    private int registrosBorrados;
    private String mensajeValidacion;
//EMPRESA
    private List<Empresas> listaEmpresas;
    private List<Empresas> filtradoListaEmpresas;

    private Empresas empresaSeleccionada;
    private int banderaModificacionEmpresa;
    private int indiceEmpresaMostrada;
//LISTA CENTRO COSTO
    private List<UbicacionesGeograficas> listUbicacionesGeograficasPorEmpresa;
    private List<UbicacionesGeograficas> filtrarUbicacionesGeograficas;
    private List<UbicacionesGeograficas> crearUbicacionesGeograficas;
    private List<UbicacionesGeograficas> modificarUbicacionesGeograficas;
    private List<UbicacionesGeograficas> borrarUbicacionesGeograficas;
    private UbicacionesGeograficas nuevaUbicacionGeografica;
    private UbicacionesGeograficas duplicarUbicacionGeografica;
    private UbicacionesGeograficas editarCentroCosto;
    private UbicacionesGeograficas ubicacionGeograficaSeleccionada;

    private Column codigoUG, descripcionUG, ciudadUG, direccionUG, telefono, fax;
    private Column observacion, zonaUG, actividadEconomica, sucursalPila, codigoAT;
    private String ciudadesAutocompletar;
    private List<Ciudades> listaCiudades;
    private List<Ciudades> filtradoCiudades;
    private Ciudades ciudadSeleccionada;
    private String sucursalPilaAutocompletar;
    private List<SucursalesPila> listaSucursalesPilas;
    private List<SucursalesPila> filtradoSucursalesPilas;
    private SucursalesPila sucursalesPilasSeleccionada;
    private List<UbicacionesGeograficas> filterUbicacionesGeograficasPorEmpresa;
    private String nuevoTipoCCAutoCompletar;
    private Empresas backUpEmpresaActual;
    private Integer backUpCodigo;
    private String backUpDescripcion;
    private UbicacionesGeograficas UbicacionesGeograficasPorEmpresaSeleccionado;
    private boolean banderaSeleccionUbicacionesGeograficasPorEmpresa;
    private int tamano;
    private String infoRegistro;
    private BigInteger contarAfiliacionesEntidadesUbicacionGeografica;
    private BigInteger contarInspeccionesUbicacionGeografica;
    private BigInteger contarParametrosInformesUbicacionGeografica;
    private BigInteger contarRevisionesUbicacionGeografica;
    private BigInteger contarVigenciasUbicacionesUbicacionGeografica;
    private String paginaAnterior;
    private String nuevoSucursalPilaAutocompletar;
    private String infoRegistroCiudades;
    private String infoRegistroSucursalesPila;

    public ControlUbicacionesGeograficas() {
        permitirIndex = true;
        listaEmpresas = null;
        empresaSeleccionada = null;
        indiceEmpresaMostrada = 0;
        listUbicacionesGeograficasPorEmpresa = null;
        crearUbicacionesGeograficas = new ArrayList<UbicacionesGeograficas>();
        modificarUbicacionesGeograficas = new ArrayList<UbicacionesGeograficas>();
        borrarUbicacionesGeograficas = new ArrayList<UbicacionesGeograficas>();
        editarCentroCosto = new UbicacionesGeograficas();
        nuevaUbicacionGeografica = new UbicacionesGeograficas();
        nuevaUbicacionGeografica.setCiudad(new Ciudades());
        nuevaUbicacionGeografica.setSucursalPila(new SucursalesPila());
        duplicarUbicacionGeografica = new UbicacionesGeograficas();
        listaCiudades = null;
        aceptar = true;
        filtradoListaEmpresas = null;
        guardado = true;
        banderaSeleccionUbicacionesGeograficasPorEmpresa = false;
        listaSucursalesPilas = null;
        tamano = 270;
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarUbicacionesGeograficas.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void recibirPaginaEntrante(String pagina) {
        paginaAnterior = pagina;
        listaEmpresas = null;
        getListaEmpresas();
        if (listaEmpresas != null) {
            if (!listaEmpresas.isEmpty()) {
                empresaSeleccionada = listaEmpresas.get(0);
            }
        }
    }

    public String redirigirPaginaAnterior() {
        return paginaAnterior;
    }

    public void eventoFiltrar() {
        try {
            System.out.println("\n ENTRE A CONTROLUBICACIONESGEOGRAFICAS.eventoFiltrar \n");
            if (tipoLista == 0) {
                tipoLista = 1;
            }
            contarRegistros();
        } catch (Exception e) {
            System.out.println("ERROR CONTROLUBICACIONESGEOGRAFICAS eventoFiltrar ERROR===" + e);
        }
    }

    public void contarRegistros() {
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
    }

    public void contarRegistrosCiudades() {
        RequestContext.getCurrentInstance().update("form:infoRegistroCiudades");
    }

    public void contarRegistrosSucursales() {
        RequestContext.getCurrentInstance().update("form:infoRegistroSucursalesPila");
    }

    public void contarRegistrosEmpresas() {
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroEmpresas");
    }

    public void cambiarIndice(UbicacionesGeograficas ubicacion, int celda) {
        if (permitirIndex == true) {
            ubicacionGeograficaSeleccionada = ubicacion;
            cualCelda = celda;
            System.err.println("CAMBIAR INDICE CUALCELDA = " + cualCelda);
            ubicacionGeograficaSeleccionada.getSecuencia();
            if (cualCelda == 0) {
                backUpCodigo = ubicacionGeograficaSeleccionada.getCodigo();
            } else if (cualCelda == 1) {
                backUpDescripcion = ubicacionGeograficaSeleccionada.getDescripcion();
            } else if (cualCelda == 2) {
                ciudadesAutocompletar = ubicacionGeograficaSeleccionada.getCiudad().getNombre();
            } else if (cualCelda == 3) {
                ubicacionGeograficaSeleccionada.getDireccion();
            } else if (cualCelda == 4) {
                ubicacionGeograficaSeleccionada.getTelefono();
            } else if (cualCelda == 5) {
                ubicacionGeograficaSeleccionada.getFax();
            } else if (cualCelda == 6) {
                ubicacionGeograficaSeleccionada.getObservacion();
            } else if (cualCelda == 8) {
                ubicacionGeograficaSeleccionada.getActividadeconomica();
            } else if (cualCelda == 9) {
                sucursalPilaAutocompletar = ubicacionGeograficaSeleccionada.getSucursalPila().getDescripcion();
            } else if (cualCelda == 10) {
                ubicacionGeograficaSeleccionada.getCodigoalternativo();
            }
        }
    }

    public void modificandoUbicacionGeografica(UbicacionesGeograficas ubicacion) {

        ubicacionGeograficaSeleccionada = ubicacion;
        if (modificarUbicacionesGeograficas.isEmpty()) {
            modificarUbicacionesGeograficas.add(ubicacionGeograficaSeleccionada);
        } else if (!modificarUbicacionesGeograficas.contains(ubicacionGeograficaSeleccionada)) {
            modificarUbicacionesGeograficas.add(ubicacionGeograficaSeleccionada);
        }
        if (guardado == true) {
            guardado = false;
        }
        RequestContext.getCurrentInstance().update("form:datosUbicacionesGeograficas");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");

    }

    public void cancelarModificacion() {
        try {
            FacesContext c = FacesContext.getCurrentInstance();
            System.out.println("entre a CONTROLUBICACIONESGEOGRAFICAS.cancelarModificacion");
            if (bandera == 1) {
                codigoUG = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:codigoUG");
                codigoUG.setFilterStyle("display: none; visibility: hidden;");
                descripcionUG = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:descripcionUG");
                descripcionUG.setFilterStyle("display: none; visibility: hidden;");
                ciudadUG = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:ciudadUG");
                ciudadUG.setFilterStyle("display: none; visibility: hidden;");
                direccionUG = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:direccionUG");
                direccionUG.setFilterStyle("display: none; visibility: hidden;");
                telefono = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:telefono");
                telefono.setFilterStyle("display: none; visibility: hidden;");
                fax = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:fax");
                fax.setFilterStyle("display: none; visibility: hidden;");
                observacion = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:observacion");
                observacion.setFilterStyle("display: none; visibility: hidden;");
                zonaUG = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:zonaUG");
                zonaUG.setFilterStyle("display: none; visibility: hidden;");
                actividadEconomica = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:actividadEconomica");
                actividadEconomica.setFilterStyle("display: none; visibility: hidden;");
                sucursalPila = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:sucursalPila");
                sucursalPila.setFilterStyle("display: none; visibility: hidden;");
                codigoAT = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:codigoAT");
                codigoAT.setFilterStyle("display: none; visibility: hidden;");
                bandera = 0;
                filtrarUbicacionesGeograficas = null;
                tipoLista = 0;
            }

            borrarUbicacionesGeograficas.clear();
            crearUbicacionesGeograficas.clear();
            modificarUbicacionesGeograficas.clear();
            ubicacionGeograficaSeleccionada = null;
            k = 0;
            listUbicacionesGeograficasPorEmpresa = null;
            guardado = true;
            permitirIndex = true;
            RequestContext context = RequestContext.getCurrentInstance();
            banderaModificacionEmpresa = 0;
            if (banderaModificacionEmpresa == 0) {
                cambiarEmpresa();
            }
            getListUbicacionesGeograficasPorEmpresa();
            RequestContext.getCurrentInstance().update("form:datosUbicacionesGeograficas");
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        } catch (Exception E) {
            System.out.println("ERROR CONTROLUBICACIONESGEOGRAFICAS.ModificarModificacion ERROR====================" + E.getMessage());
        }
    }

    public void salir() {
        try {
            FacesContext c = FacesContext.getCurrentInstance();
            System.out.println("entre a CONTROLUBICACIONESGEOGRAFICAS.cancelarModificacion");
            if (bandera == 1) {
                //CERRAR FILTRADO
                //0
                codigoUG = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:codigoUG");
                codigoUG.setFilterStyle("display: none; visibility: hidden;");
                descripcionUG = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:descripcionUG");
                descripcionUG.setFilterStyle("display: none; visibility: hidden;");
                ciudadUG = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:ciudadUG");
                ciudadUG.setFilterStyle("display: none; visibility: hidden;");
                direccionUG = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:direccionUG");
                direccionUG.setFilterStyle("display: none; visibility: hidden;");
                telefono = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:telefono");
                telefono.setFilterStyle("display: none; visibility: hidden;");
                fax = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:fax");
                fax.setFilterStyle("display: none; visibility: hidden;");
                observacion = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:observacion");
                observacion.setFilterStyle("display: none; visibility: hidden;");
                zonaUG = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:zonaUG");
                zonaUG.setFilterStyle("display: none; visibility: hidden;");
                actividadEconomica = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:actividadEconomica");
                actividadEconomica.setFilterStyle("display: none; visibility: hidden;");
                sucursalPila = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:sucursalPila");
                sucursalPila.setFilterStyle("display: none; visibility: hidden;");
                codigoAT = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:codigoAT");
                codigoAT.setFilterStyle("display: none; visibility: hidden;");
                bandera = 0;
                filtrarUbicacionesGeograficas = null;
                tipoLista = 0;
            }

            borrarUbicacionesGeograficas.clear();
            crearUbicacionesGeograficas.clear();
            modificarUbicacionesGeograficas.clear();
            ubicacionGeograficaSeleccionada = null;
            k = 0;
            listUbicacionesGeograficasPorEmpresa = null;
            guardado = true;
            permitirIndex = true;
            RequestContext context = RequestContext.getCurrentInstance();
            banderaModificacionEmpresa = 0;
            if (banderaModificacionEmpresa == 0) {
                cambiarEmpresa();
            }
            getListUbicacionesGeograficasPorEmpresa();
            RequestContext.getCurrentInstance().update("form:datosUbicacionesGeograficas");
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        } catch (Exception E) {
            System.out.println("ERROR CONTROLUBICACIONESGEOGRAFICAS.ModificarModificacion ERROR====================" + E.getMessage());
        }
    }

    public void mostrarInfo(UbicacionesGeograficas ubicacion, int celda) {
        if (permitirIndex == true) {
            RequestContext context = RequestContext.getCurrentInstance();

            banderaModificacionEmpresa = 1;
            ubicacionGeograficaSeleccionada = ubicacion;
            cualCelda = celda;
            ubicacionGeograficaSeleccionada.getSecuencia();
            System.out.println("Modificar Zona : " + ubicacionGeograficaSeleccionada.getZona());
            if (!crearUbicacionesGeograficas.contains(ubicacionGeograficaSeleccionada)) {
                if (modificarUbicacionesGeograficas.isEmpty()) {
                    modificarUbicacionesGeograficas.add(ubicacionGeograficaSeleccionada);
                } else if (!modificarUbicacionesGeograficas.contains(ubicacionGeograficaSeleccionada)) {
                    modificarUbicacionesGeograficas.add(ubicacionGeograficaSeleccionada);
                }
                if (guardado == true) {
                    guardado = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
            }
            RequestContext.getCurrentInstance().update("form:datosUbicacionesGeograficas");

        }

    }

    public void asignarIndex(UbicacionesGeograficas ubicacion, int LND, int dig) {
        try {
            System.out.println("\n ENTRE A CONTROLUBICACIONESGEOGRAFICAS.asignarIndex \n");
            ubicacionGeograficaSeleccionada = ubicacion;
            tipoActualizacion = LND;
            if (dig == 2) {
                contarRegistrosCiudades();
                RequestContext.getCurrentInstance().update("form:ciudadesDialogo");
                RequestContext.getCurrentInstance().execute("PF('ciudadesDialogo').show()");
            }
            if (dig == 9) {
                System.out.println("Secuencia Empresa Seleccionada = " + empresaSeleccionada.getSecuencia());
                listaSucursalesPilas = null;
                getListaSucursalesPilas();
                contarRegistrosSucursales();
                RequestContext.getCurrentInstance().update("form:sucursalesPilaDialogo");
                RequestContext.getCurrentInstance().execute("PF('sucursalesPilaDialogo').show()");
            }

        } catch (Exception e) {
            System.out.println("ERROR CONTROLUBICACIONESGEOGRAFICAS ASIGNARINDEX ERROR :" + e);
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void actualizarCiudad() {
        banderaModificacionEmpresa = 1;
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            ubicacionGeograficaSeleccionada.setCiudad(ciudadSeleccionada);
            if (!crearUbicacionesGeograficas.contains(ubicacionGeograficaSeleccionada)) {
                if (modificarUbicacionesGeograficas.isEmpty()) {
                    modificarUbicacionesGeograficas.add(ubicacionGeograficaSeleccionada);
                } else if (!modificarUbicacionesGeograficas.contains(ubicacionGeograficaSeleccionada)) {
                    modificarUbicacionesGeograficas.add(ubicacionGeograficaSeleccionada);
                }
                if (guardado == true) {
                    guardado = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }

            }
            RequestContext.getCurrentInstance().update("form:datosUbicacionesGeograficas");
        } else if (tipoActualizacion == 1) {
            nuevaUbicacionGeografica.setCiudad(ciudadSeleccionada);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaTipoCentroCostos");
        } else if (tipoActualizacion == 2) {
            duplicarUbicacionGeografica.setCiudad(ciudadSeleccionada);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTipoCentroCostos");

        }
        filtradoCiudades = null;
        ciudadSeleccionada = null;
        aceptar = true;
        tipoActualizacion = -1;
        permitirIndex = true;

        context.reset("form:lovCiudades:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovCiudades').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('ciudadesDialogo').hide()");

        RequestContext.getCurrentInstance().update("form:ciudadesDialogo");
        RequestContext.getCurrentInstance().update("form:lovCiudades");
        RequestContext.getCurrentInstance().update("form:aceptarTCC");

    }

    public void cancelarCambioCiudad() {
        filtradoCiudades = null;
        ciudadSeleccionada = null;
        aceptar = true;
        tipoActualizacion = -1;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovCiudades:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovCiudades').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('ciudadesDialogo').hide()");

        RequestContext.getCurrentInstance().update("form:ciudadesDialogo");
        RequestContext.getCurrentInstance().update("form:lovCiudades");
        RequestContext.getCurrentInstance().update("form:aceptarTCC");

    }

    public void actualizarSucursalPila() {
        banderaModificacionEmpresa = 1;
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            ubicacionGeograficaSeleccionada.setSucursalPila(sucursalesPilasSeleccionada);
            if (!crearUbicacionesGeograficas.contains(ubicacionGeograficaSeleccionada)) {
                if (modificarUbicacionesGeograficas.isEmpty()) {
                    modificarUbicacionesGeograficas.add(ubicacionGeograficaSeleccionada);
                } else if (!modificarUbicacionesGeograficas.contains(ubicacionGeograficaSeleccionada)) {
                    modificarUbicacionesGeograficas.add(ubicacionGeograficaSeleccionada);
                }
                if (guardado == true) {
                    guardado = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }

            }
            RequestContext.getCurrentInstance().update("form:datosUbicacionesGeograficas");
        } else if (tipoActualizacion == 1) {
            nuevaUbicacionGeografica.setSucursalPila(sucursalesPilasSeleccionada);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaSucursal");
            RequestContext.getCurrentInstance().update("form:datosUbicacionesGeograficas");

        } else if (tipoActualizacion == 2) {
            duplicarUbicacionGeografica.setSucursalPila(sucursalesPilasSeleccionada);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTipoCentroCostos");
        }
        filtradoCiudades = null;
        ciudadSeleccionada = null;
        aceptar = true;
        tipoActualizacion = -1;
        permitirIndex = true;

        context.reset("form:lovSucursalesPila:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovSucursalesPila').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('sucursalesPilaDialogo').hide()");

        RequestContext.getCurrentInstance().update("form:sucursalesPilaDialogo");
        RequestContext.getCurrentInstance().update("form:lovSucursalesPila");
        RequestContext.getCurrentInstance().update("form:aceptarSP");
    }

    public void cancelarCambioSucursalPila() {
        filtradoSucursalesPilas = null;
        sucursalesPilasSeleccionada = null;
        aceptar = true;
        tipoActualizacion = -1;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovSucursalesPila:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovSucursalesPila').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('sucursalesPilaDialogo').hide()");

        RequestContext.getCurrentInstance().update("form:sucursalesPilaDialogo");
        RequestContext.getCurrentInstance().update("form:lovSucursalesPila");
        RequestContext.getCurrentInstance().update("form:aceptarSP");
    }

//    public void seleccionUbicacionesGeograficasPorEmpresa() {
//        try {
//            RequestContext context = RequestContext.getCurrentInstance();
//
//            if (guardado == true) {
//                listUbicacionesGeograficasPorEmpresa.clear();
//                System.err.println("seleccionUbicacionesGeograficasPorEmpresa " + UbicacionesGeograficasPorEmpresaSeleccionado.getDescripcion());
//                listUbicacionesGeograficasPorEmpresa.add(UbicacionesGeograficasPorEmpresaSeleccionado);
//                System.err.println("listUbicacionesGeograficasPorEmpresa tamaño " + listUbicacionesGeograficasPorEmpresa.size());
//                System.err.println("listUbicacionesGeograficasPorEmpresa nombre " + listUbicacionesGeograficasPorEmpresa.get(0).getDescripcion());
//                UbicacionesGeograficasPorEmpresaSeleccionado = null;
//                filterUbicacionesGeograficasPorEmpresa = null;
//                aceptar = true;
//                banderaModificacionEmpresa = 1;
//                RequestContext.getCurrentInstance().update("form:datosUbicacionesGeograficas");
//                RequestContext.getCurrentInstance().execute("PF('buscarUbicacionesGeograficasDialogo').hide()");
//                context.reset("formularioDialogos:lovUbicacionesGeograficas:globalFilter");
//            }
//        } catch (Exception e) {
//            System.out.println("ERROR CONTROLUBICACIONESGEOGRAFICAS.seleccionaVigencia ERROR====" + e);
//        }
//    }
//
//    public void cancelarSeleccionCentroCostoPorEmpresa() {
//        try {
//            RequestContext context = RequestContext.getCurrentInstance();
//            UbicacionesGeograficasPorEmpresaSeleccionado = null;
//            filterUbicacionesGeograficasPorEmpresa = null;
//            aceptar = true;
//            tipoActualizacion = -1;
//            RequestContext.getCurrentInstance().update("form:aceptarNCC");
//
//        } catch (Exception e) {
//            System.out.println("ERROR CONTROLUBICACIONESGEOGRAFICAS.cancelarSeleccionVigencia ERROR====" + e);
//        }
//    }
    public void valoresBackupAutocompletar(int tipoNuevo, String Campo) {
        System.out.println("1...");
        if (Campo.equals("CIUDADES")) {
            if (tipoNuevo == 1) {
                nuevoTipoCCAutoCompletar = nuevaUbicacionGeografica.getCiudad().getNombre();
            } else if (tipoNuevo == 2) {
                nuevoTipoCCAutoCompletar = duplicarUbicacionGeografica.getCiudad().getNombre();
            }

        }
        if (Campo.equals("SUCURSALESPILA")) {
            if (tipoNuevo == 1) {
                nuevoSucursalPilaAutocompletar = nuevaUbicacionGeografica.getSucursalPila().getDescripcion();
            } else if (tipoNuevo == 2) {
                nuevoSucursalPilaAutocompletar = duplicarUbicacionGeografica.getSucursalPila().getDescripcion();
            }

        }
    }

    public void autocompletarNuevo(String confirmarCambio, String valorConfirmar, int tipoNuevo) {

        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("CIUDADES")) {
            nuevaUbicacionGeografica.getCiudad().setNombre(nuevoTipoCCAutoCompletar);
            getListaCiudades();
            for (int i = 0; i < listaCiudades.size(); i++) {
                if (listaCiudades.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            System.out.println("Coincidencias: " + coincidencias);
            if (coincidencias == 1) {
                nuevaUbicacionGeografica.setCiudad(listaCiudades.get(indiceUnicoElemento));
                listaCiudades = null;
                getListaCiudades();
            } else {
                RequestContext.getCurrentInstance().update("form:ciudadesDialogo");
                RequestContext.getCurrentInstance().execute("PF('ciudadesDialogo').show()");
                tipoActualizacion = tipoNuevo;
            }

            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaTipoCentroCostos");
        }
        if (confirmarCambio.equalsIgnoreCase("SUCURSALESPILA")) {
            nuevaUbicacionGeografica.getSucursalPila().setDescripcion(nuevoSucursalPilaAutocompletar);
            getListaSucursalesPilas();
            for (int i = 0; i < listaSucursalesPilas.size(); i++) {
                if (listaSucursalesPilas.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            System.out.println("Coincidencias: " + coincidencias);
            if (coincidencias == 1) {
                nuevaUbicacionGeografica.setSucursalPila(listaSucursalesPilas.get(indiceUnicoElemento));
                listaSucursalesPilas = null;
                getListaCiudades();
            } else {
                RequestContext.getCurrentInstance().update("form:sucursalesPilaDialogo");
                RequestContext.getCurrentInstance().execute("PF('sucursalesPilaDialogo').show()");
                tipoActualizacion = tipoNuevo;
            }

            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaSucursal");
        }

    }

    public void asignarVariableSucursalesPila(int tipoNuevo) {
        if (tipoNuevo == 0) {
            tipoActualizacion = 1;
        }
        if (tipoNuevo == 1) {
            tipoActualizacion = 2;
        }
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:sucursalesPilaDialogo");
        RequestContext.getCurrentInstance().execute("PF('sucursalesPilaDialogo').show()");
    }

    public void autocompletarDuplicado(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        if (confirmarCambio.equalsIgnoreCase("CIUDADES")) {
            if (!duplicarUbicacionGeografica.getCiudad().getNombre().equals("")) {
                duplicarUbicacionGeografica.getCiudad().setNombre(nuevoTipoCCAutoCompletar);
                for (int i = 0; i < listaCiudades.size(); i++) {
                    if (listaCiudades.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    duplicarUbicacionGeografica.setCiudad(listaCiudades.get(indiceUnicoElemento));
                    listaCiudades = null;
                    getListaCiudades();
                } else {
                    RequestContext.getCurrentInstance().update("form:ciudadesDialogo");
                    RequestContext.getCurrentInstance().execute("PF('ciudadesDialogo').show()");
                    tipoActualizacion = tipoNuevo;
                }
            } else {
                if (tipoLista == 0) {
                    ubicacionGeograficaSeleccionada.getCiudad().setNombre(nuevoTipoCCAutoCompletar);
                } else {
                    ubicacionGeograficaSeleccionada.getCiudad().setNombre(nuevoTipoCCAutoCompletar);
                }
                duplicarUbicacionGeografica.setCiudad(new Ciudades());
            }

            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTipoCentroCostos");
        }
        if (confirmarCambio.equalsIgnoreCase("SUCURSALESPILA")) {
            if (!duplicarUbicacionGeografica.getSucursalPila().getDescripcion().equals("")) {
                duplicarUbicacionGeografica.getSucursalPila().setDescripcion(nuevoSucursalPilaAutocompletar);
                for (int i = 0; i < listaSucursalesPilas.size(); i++) {
                    if (listaSucursalesPilas.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                System.out.println("Coincidencias: " + coincidencias);
                if (coincidencias == 1) {
                    duplicarUbicacionGeografica.setSucursalPila(listaSucursalesPilas.get(indiceUnicoElemento));
                    listaSucursalesPilas = null;
                    getListaCiudades();
                } else {
                    RequestContext.getCurrentInstance().update("form:sucursalesPilaDialogo");
                    RequestContext.getCurrentInstance().execute("PF('sucursalesPilaDialogo').show()");
                    tipoActualizacion = tipoNuevo;
                }
            } else {
                if (tipoLista == 0) {
                    ubicacionGeograficaSeleccionada.getSucursalPila().setDescripcion(nuevoSucursalPilaAutocompletar);
                } else {
                    ubicacionGeograficaSeleccionada.getSucursalPila().setDescripcion(nuevoSucursalPilaAutocompletar);
                }
                duplicarUbicacionGeografica.setSucursalPila(new SucursalesPila());
            }

            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarSucursalPila");
        }

    }

    public void limpiarNuevaUbicacionGeografica() {
        nuevaUbicacionGeografica = new UbicacionesGeograficas();
    }

    public void agregarNuevaUbicacionGeografica() {
        System.out.println("\n ENTRE A CONTROLUBICACIONESGEOGRAFICAS.agregarNuevaUbicacionGeograficas \n");
        int contador = 0;
        mensajeValidacion = " ";
        int duplicados = 0;
        RequestContext context = RequestContext.getCurrentInstance();

        banderaModificacionEmpresa = 1;
        if (nuevaUbicacionGeografica.getCodigo() == null) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            for (int x = 0; x < listUbicacionesGeograficasPorEmpresa.size(); x++) {
                if (listUbicacionesGeograficasPorEmpresa.get(x).getCodigo().equals(nuevaUbicacionGeografica.getCodigo())) {
                    duplicados++;
                }
            }

            if (duplicados > 0) {
                mensajeValidacion = "Existe un registro con el código ingresado. Por favor ingrese un código válido";
                System.out.println("Mensaje validacion : " + mensajeValidacion);
            } else {
                System.out.println("bandera");
                contador++;
            }
        }
        if (nuevaUbicacionGeografica.getDescripcion() == null) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";

        } else {
            contador++;
        }
        if (nuevaUbicacionGeografica.getCiudad().getNombre() == null) {
            nuevaUbicacionGeografica.setCiudad(new Ciudades());
        }
        if (nuevaUbicacionGeografica.getSucursalPila().getDescripcion() == null) {
            nuevaUbicacionGeografica.setSucursalPila(new SucursalesPila());
        }
        if (contador == 2) {
            k++;
            l = BigInteger.valueOf(k);
            nuevaUbicacionGeografica.setSecuencia(l);
            nuevaUbicacionGeografica.setEmpresa(empresaSeleccionada);
            ubicacionGeograficaSeleccionada = nuevaUbicacionGeografica;
            if (crearUbicacionesGeograficas.contains(nuevaUbicacionGeografica)) {
            } else {
                crearUbicacionesGeograficas.add(nuevaUbicacionGeografica);

            }
            listUbicacionesGeograficasPorEmpresa.add(nuevaUbicacionGeografica);
            RequestContext.getCurrentInstance().update("form:datosUbicacionesGeograficas");
            contarRegistros();
            nuevaUbicacionGeografica = new UbicacionesGeograficas();
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                codigoUG = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:codigoUG");
                codigoUG.setFilterStyle("display: none; visibility: hidden;");
                descripcionUG = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:descripcionUG");
                descripcionUG.setFilterStyle("display: none; visibility: hidden;");
                ciudadUG = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:ciudadUG");
                ciudadUG.setFilterStyle("display: none; visibility: hidden;");
                direccionUG = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:direccionUG");
                direccionUG.setFilterStyle("display: none; visibility: hidden;");
                telefono = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:telefono");
                telefono.setFilterStyle("display: none; visibility: hidden;");
                fax = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:fax");
                fax.setFilterStyle("display: none; visibility: hidden;");
                observacion = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:observacion");
                observacion.setFilterStyle("display: none; visibility: hidden;");
                zonaUG = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:zonaUG");
                zonaUG.setFilterStyle("display: none; visibility: hidden;");
                actividadEconomica = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:actividadEconomica");
                actividadEconomica.setFilterStyle("display: none; visibility: hidden;");
                sucursalPila = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:sucursalPila");
                sucursalPila.setFilterStyle("display: none; visibility: hidden;");
                codigoAT = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:codigoAT");
                codigoAT.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosUbicacionesGeograficas");

                bandera = 0;
                filtrarUbicacionesGeograficas = null;
                tipoLista = 0;
            }
            mensajeValidacion = " ";
            RequestContext.getCurrentInstance().execute("PF('NuevoRegistroUbicacionesGeograficas').hide()");

        } else {
            contador = 0;
            RequestContext.getCurrentInstance().update("form:validacionNuevaUbicacion");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevaUbicacion').show()");
        }
    }

    public void mostrarDialogoNuevoCiudades() {
        RequestContext context = RequestContext.getCurrentInstance();
        nuevaUbicacionGeografica = new UbicacionesGeograficas();
        nuevaUbicacionGeografica.setCiudad(new Ciudades());
        RequestContext.getCurrentInstance().update("formularioDialogos:nuevaUbicacionGeograficas");
        RequestContext.getCurrentInstance().execute("PF('NuevoRegistroUbicacionesGeograficas').show()");
    }

    public void mostrarDialogoListaEmpresas() {
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().execute("PF('buscarUbicacionesGeograficasDialogo').show()");
    }

    public void duplicandoCentroCostos() {
        //try {
        banderaModificacionEmpresa = 1;

        if (ubicacionGeograficaSeleccionada != null) {
            System.out.println("\n ENTRE A CONTROLUBICACIONESGEOGRAFICAS.duplicarUbicacionGeograficas TIPOLISTA===" + tipoLista);

            duplicarUbicacionGeografica = new UbicacionesGeograficas();
            duplicarUbicacionGeografica.setSucursalPila(new SucursalesPila());
            duplicarUbicacionGeografica.setCiudad(new Ciudades());
            k++;
            l = BigInteger.valueOf(k);
            duplicarUbicacionGeografica.setSecuencia(l);
            duplicarUbicacionGeografica.setEmpresa(ubicacionGeograficaSeleccionada.getEmpresa());
            duplicarUbicacionGeografica.setCodigo(ubicacionGeograficaSeleccionada.getCodigo());
            duplicarUbicacionGeografica.setDescripcion(ubicacionGeograficaSeleccionada.getDescripcion());
            duplicarUbicacionGeografica.getCiudad().setNombre(ubicacionGeograficaSeleccionada.getCiudad().getNombre());
            duplicarUbicacionGeografica.setDireccion(ubicacionGeograficaSeleccionada.getDireccion());
            duplicarUbicacionGeografica.setTelefono(ubicacionGeograficaSeleccionada.getTelefono());
            duplicarUbicacionGeografica.setFax(ubicacionGeograficaSeleccionada.getFax());
            duplicarUbicacionGeografica.setObservacion(ubicacionGeograficaSeleccionada.getObservacion());
            duplicarUbicacionGeografica.setZona(ubicacionGeograficaSeleccionada.getZona());
            duplicarUbicacionGeografica.setActividadeconomica(ubicacionGeograficaSeleccionada.getActividadeconomica());
            duplicarUbicacionGeografica.getSucursalPila().setDescripcion(ubicacionGeograficaSeleccionada.getSucursalPila().getDescripcion());
            duplicarUbicacionGeografica.setCodigoalternativo(ubicacionGeograficaSeleccionada.getCodigoalternativo());

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarUbicacionGeograficas");
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroCentroCostos').show()");
        }
        /* } catch (Exception e) {
         System.out.println("ERROR CONTROLUBICACIONESGEOGRAFICAS.DuplicarCentroCostos ERROR===============" + e);
         }*/
    }

    public void limpiarDuplicarCentroCostos() {
        duplicarUbicacionGeografica = new UbicacionesGeograficas();
    }

    public void confirmarDuplicar() {
        System.err.println("ESTOY EN CONFIRMAR DUPLICAR CONTROLTIPOSCENTROSCOSTOS");
        int contador = 0;
        mensajeValidacion = " ";
        int duplicados = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        Short a = 0;
        a = null;

        if (duplicarUbicacionGeografica.getCodigo() == null) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";

        } else {
            for (int x = 0; x < listUbicacionesGeograficasPorEmpresa.size(); x++) {
                if (listUbicacionesGeograficasPorEmpresa.get(x).getCodigo().equals(duplicarUbicacionGeografica.getCodigo())) {
                    duplicados++;
                }
            }
            if (duplicados > 0) {
                mensajeValidacion = "Existe un registro con ese código. Por favor ingrese un código válido";
                System.out.println("Mensaje validacion : " + mensajeValidacion);
            } else {
                System.out.println("bandera");
                contador++;
            }

        }
        if (duplicarUbicacionGeografica.getDescripcion().isEmpty()) {
            mensajeValidacion = mensajeValidacion + "Los campos marcados con asterisco son obligatorios";
        } else if (duplicarUbicacionGeografica.getDescripcion() == null) {
            mensajeValidacion = mensajeValidacion + "Los campos marcados con asterisco son obligatorios";
        } else {
            System.out.println("Bandera : ");
            contador++;
        }
        if (duplicarUbicacionGeografica.getCiudad().getSecuencia() == null) {
            duplicarUbicacionGeografica.setCiudad(new Ciudades());

        }

        if (contador == 2) {
            if (crearUbicacionesGeograficas.contains(duplicarUbicacionGeografica)) {
                System.out.println("Ya lo contengo.");
            } else {
                listUbicacionesGeograficasPorEmpresa.add(duplicarUbicacionGeografica);
            }
            crearUbicacionesGeograficas.add(duplicarUbicacionGeografica);
            RequestContext.getCurrentInstance().update("form:datosUbicacionesGeograficas");
            contarRegistros();
            ubicacionGeograficaSeleccionada = duplicarUbicacionGeografica;
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                codigoUG = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:codigoUG");
                codigoUG.setFilterStyle("display: none; visibility: hidden;");
                descripcionUG = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:descripcionUG");
                descripcionUG.setFilterStyle("display: none; visibility: hidden;");
                ciudadUG = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:ciudadUG");
                ciudadUG.setFilterStyle("display: none; visibility: hidden;");
                direccionUG = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:direccionUG");
                direccionUG.setFilterStyle("display: none; visibility: hidden;");
                telefono = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:telefono");
                telefono.setFilterStyle("display: none; visibility: hidden;");
                fax = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:fax");
                fax.setFilterStyle("display: none; visibility: hidden;");
                observacion = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:observacion");
                observacion.setFilterStyle("display: none; visibility: hidden;");
                zonaUG = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:zonaUG");
                zonaUG.setFilterStyle("display: none; visibility: hidden;");
                actividadEconomica = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:actividadEconomica");
                actividadEconomica.setFilterStyle("display: none; visibility: hidden;");
                sucursalPila = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:sucursalPila");
                sucursalPila.setFilterStyle("display: none; visibility: hidden;");
                codigoAT = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:codigoAT");
                codigoAT.setFilterStyle("display: none; visibility: hidden;");

                RequestContext.getCurrentInstance().update("form:datosUbicacionesGeograficas");
                bandera = 0;
                filtrarUbicacionesGeograficas = null;
                tipoLista = 0;
            }
            duplicarUbicacionGeografica = new UbicacionesGeograficas();
            duplicarUbicacionGeografica.setCiudad(new Ciudades());
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroCentroCostos').hide()");
            mensajeValidacion = " ";
            banderaModificacionEmpresa = 1;

        } else {
            contador = 0;
            RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
            RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
        }
    }

    public void verificarBorrado() {
        System.out.println("Estoy en verificarBorrado");
        System.out.println("TIPOLISTA = " + tipoLista);
        try {
            contarAfiliacionesEntidadesUbicacionGeografica = administrarUbicacionesGeograficas.contarAfiliacionesEntidadesUbicacionGeografica(ubicacionGeograficaSeleccionada.getSecuencia());
            contarInspeccionesUbicacionGeografica = administrarUbicacionesGeograficas.contarInspeccionesUbicacionGeografica(ubicacionGeograficaSeleccionada.getSecuencia());
            contarParametrosInformesUbicacionGeografica = administrarUbicacionesGeograficas.contarParametrosInformesUbicacionGeografica(ubicacionGeograficaSeleccionada.getSecuencia());
            contarRevisionesUbicacionGeografica = administrarUbicacionesGeograficas.contarRevisionesUbicacionGeografica(ubicacionGeograficaSeleccionada.getSecuencia());
            contarVigenciasUbicacionesUbicacionGeografica = administrarUbicacionesGeograficas.contarVigenciasUbicacionesUbicacionGeografica(ubicacionGeograficaSeleccionada.getSecuencia());
            System.out.println("contarAfiliacionesEntidadesUbicacionGeografica : " + contarAfiliacionesEntidadesUbicacionGeografica);
            System.out.println("contarInspeccionesUbicacionGeografica : " + contarInspeccionesUbicacionGeografica);
            System.out.println("contarParametrosInformesUbicacionGeografica : " + contarParametrosInformesUbicacionGeografica);
            System.out.println("contarRevisionesUbicacionGeografica : " + contarRevisionesUbicacionGeografica);
            System.out.println("contarVigenciasUbicacionesUbicacionGeografica : " + contarVigenciasUbicacionesUbicacionGeografica);
            if (contarAfiliacionesEntidadesUbicacionGeografica.equals(new BigInteger("0"))
                    && contarInspeccionesUbicacionGeografica.equals(new BigInteger("0"))
                    && contarParametrosInformesUbicacionGeografica.equals(new BigInteger("0"))
                    && contarRevisionesUbicacionGeografica.equals(new BigInteger("0"))
                    && contarVigenciasUbicacionesUbicacionGeografica.equals(new BigInteger("0"))) {
                System.out.println("Borrado==0");
                borrandoUbicacionesGeograficas();
            } else {

                System.out.println("Borrado>0");
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:validacionBorrar");
                RequestContext.getCurrentInstance().execute("PF('validacionBorrar').show()");
                ubicacionGeograficaSeleccionada = null;
                contarAfiliacionesEntidadesUbicacionGeografica = new BigInteger("-1");
                contarInspeccionesUbicacionGeografica = new BigInteger("-1");
                contarParametrosInformesUbicacionGeografica = new BigInteger("-1");
                contarRevisionesUbicacionGeografica = new BigInteger("-1");
                contarVigenciasUbicacionesUbicacionGeografica = new BigInteger("-1");
            }
        } catch (Exception e) {
            System.err.println("ERROR CONTROLUBICACIONESGEOGRAFICAS VERIFICARBORRADO ERROR " + e);
        }
    }

    public void borrandoUbicacionesGeograficas() {
        if (ubicacionGeograficaSeleccionada != null) {
            if (!modificarUbicacionesGeograficas.isEmpty() && modificarUbicacionesGeograficas.contains(ubicacionGeograficaSeleccionada)) {
                int modIndex = modificarUbicacionesGeograficas.indexOf(ubicacionGeograficaSeleccionada);
                modificarUbicacionesGeograficas.remove(modIndex);
                borrarUbicacionesGeograficas.add(ubicacionGeograficaSeleccionada);
            } else if (!crearUbicacionesGeograficas.isEmpty() && crearUbicacionesGeograficas.contains(ubicacionGeograficaSeleccionada)) {
                int crearIndex = crearUbicacionesGeograficas.indexOf(ubicacionGeograficaSeleccionada);
                crearUbicacionesGeograficas.remove(crearIndex);
            } else {

                borrarUbicacionesGeograficas.add(ubicacionGeograficaSeleccionada);
            }
            listUbicacionesGeograficasPorEmpresa.remove(ubicacionGeograficaSeleccionada);
            if (tipoLista == 1) {
                filtrarUbicacionesGeograficas.remove(ubicacionGeograficaSeleccionada);
            }

            if (guardado == true) {
                guardado = false;
            }
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosUbicacionesGeograficas");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void guardarCambiosUbicacionGeografica() {
        try {
            RequestContext context = RequestContext.getCurrentInstance();
            if (guardado == false) {
                if (!borrarUbicacionesGeograficas.isEmpty()) {
                    administrarUbicacionesGeograficas.borrarUbicacionesGeograficas(borrarUbicacionesGeograficas);
                    registrosBorrados = borrarUbicacionesGeograficas.size();
                    RequestContext.getCurrentInstance().update("form:mostrarBorrados");
                    RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
                    borrarUbicacionesGeograficas.clear();
                }
                if (!modificarUbicacionesGeograficas.isEmpty()) {
                    administrarUbicacionesGeograficas.modificarUbicacionesGeograficas(modificarUbicacionesGeograficas);
                    modificarUbicacionesGeograficas.clear();
                }
                if (!crearUbicacionesGeograficas.isEmpty()) {
                    administrarUbicacionesGeograficas.crearUbicacionesGeograficas(crearUbicacionesGeograficas);
                    crearUbicacionesGeograficas.clear();
                }
                RequestContext.getCurrentInstance().update("form:datosUbicacionesGeograficas");
                FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                RequestContext.getCurrentInstance().update("form:growl");
            }

            if (banderaModificacionEmpresa == 0) {
                cambiarEmpresa();
                banderaModificacionEmpresa = 1;
            }
            k = 0;
            guardado = true;
            ubicacionGeograficaSeleccionada = null;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            banderaModificacionEmpresa = 0;
            System.out.println("Se guardaron los datos con exito");
        } catch (Exception e) {
            System.err.println("CONTROLUBICACIONESGEOGRAFICAS GUARDARCAMBIOS: " + e);
            RequestContext context = RequestContext.getCurrentInstance();
            FacesMessage msg = new FacesMessage("Información", "Hubo un error en el guardado, Por favor intente nuevamente.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }
    }

    public void cancelarCambios() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (banderaModificacionEmpresa == 0) {
            empresaSeleccionada = backUpEmpresaActual;
            RequestContext.getCurrentInstance().update("formularioDialogos:lovEmpresas");
            banderaModificacionEmpresa = 1;
        }

    }

    public void activarCtrlF11() {
        System.out.println("\n ENTRE A CONTROLUBICACIONESGEOGRAFICAS.activarCtrlF11 \n");
        FacesContext c = FacesContext.getCurrentInstance();
        try {

            if (bandera == 0) {
                System.out.println("Activar");
                tamano = 250;
                codigoUG = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:codigoUG");
                codigoUG.setFilterStyle("width: 85% !important;");
                descripcionUG = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:descripcionUG");
                descripcionUG.setFilterStyle("width: 85% !important;");
                ciudadUG = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:ciudadUG");
                ciudadUG.setFilterStyle("width: 85% !important;");
                direccionUG = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:direccionUG");
                direccionUG.setFilterStyle("width: 85% !important;");
                telefono = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:telefono");
                telefono.setFilterStyle("width: 85% !important;");
                fax = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:fax");
                fax.setFilterStyle("width: 85% !important;");
                observacion = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:observacion");
                observacion.setFilterStyle("width: 85% !important;");
                zonaUG = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:zonaUG");
                zonaUG.setFilterStyle("width: 85% !important;");
                actividadEconomica = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:actividadEconomica");
                actividadEconomica.setFilterStyle("width: 85% !important;");
                sucursalPila = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:sucursalPila");
                sucursalPila.setFilterStyle("width: 85% !important;");
                codigoAT = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:codigoAT");
                codigoAT.setFilterStyle("width: 85% !important;");
                RequestContext.getCurrentInstance().update("form:datosUbicacionesGeograficas");
                bandera = 1;
            } else if (bandera == 1) {
                System.out.println("Desactivar");
                //0
                codigoUG = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:codigoUG");
                codigoUG.setFilterStyle("display: none; visibility: hidden;");
                descripcionUG = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:descripcionUG");
                descripcionUG.setFilterStyle("display: none; visibility: hidden;");
                ciudadUG = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:ciudadUG");
                ciudadUG.setFilterStyle("display: none; visibility: hidden;");
                direccionUG = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:direccionUG");
                direccionUG.setFilterStyle("display: none; visibility: hidden;");
                telefono = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:telefono");
                telefono.setFilterStyle("display: none; visibility: hidden;");
                fax = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:fax");
                fax.setFilterStyle("display: none; visibility: hidden;");
                observacion = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:observacion");
                observacion.setFilterStyle("display: none; visibility: hidden;");
                zonaUG = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:zonaUG");
                zonaUG.setFilterStyle("display: none; visibility: hidden;");
                actividadEconomica = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:actividadEconomica");
                actividadEconomica.setFilterStyle("display: none; visibility: hidden;");
                sucursalPila = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:sucursalPila");
                sucursalPila.setFilterStyle("display: none; visibility: hidden;");
                codigoAT = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:codigoAT");
                codigoAT.setFilterStyle("display: none; visibility: hidden;");
                tamano = 270;

                RequestContext.getCurrentInstance().update("form:datosUbicacionesGeograficas");
                bandera = 0;
                filtrarUbicacionesGeograficas = null;
                tipoLista = 0;
            }
        } catch (Exception e) {
            System.out.println("ERROR CONTROLUBICACIONESGEOGRAFICAS.activarCtrlF11 ERROR " + e);
        }
    }

    public void editarCelda() {
        if (ubicacionGeograficaSeleccionada != null) {
            editarCentroCosto = ubicacionGeograficaSeleccionada;
            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarCodigo");
                RequestContext.getCurrentInstance().execute("PF('editarCodigo').show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarDescripcion");
                RequestContext.getCurrentInstance().execute("PF('editarDescripcion').show()");
                cualCelda = -1;
            } else if (cualCelda == 2) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarCiudad");
                RequestContext.getCurrentInstance().execute("PF('editarCiudad').show()");
                cualCelda = -1;
            } else if (cualCelda == 3) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarDireccion");
                RequestContext.getCurrentInstance().execute("PF('editarDireccion').show()");
                cualCelda = -1;
            } else if (cualCelda == 4) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarTelefono");
                RequestContext.getCurrentInstance().execute("PF('editarTelefono').show()");
                cualCelda = -1;
            } else if (cualCelda == 5) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarFax");
                RequestContext.getCurrentInstance().execute("PF('editarFax').show()");
                cualCelda = -1;
            } else if (cualCelda == 6) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarObservacion");
                RequestContext.getCurrentInstance().execute("PF('editarObservacion').show()");
                cualCelda = -1;
            } else if (cualCelda == 8) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarActividadEconomica");
                RequestContext.getCurrentInstance().execute("PF('editarActividadEconomica').show()");
                cualCelda = -1;
            } else if (cualCelda == 9) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarSucursalPilla");
                RequestContext.getCurrentInstance().execute("PF('editarSucursalPilla').show()");
                cualCelda = -1;
            } else if (cualCelda == 10) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarCodigoAlt");
                RequestContext.getCurrentInstance().execute("PF('editarCodigoAlt').show()");
                cualCelda = -1;
            }
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void listaValoresBoton() {

        try {
            if (ubicacionGeograficaSeleccionada != null) {
                RequestContext context = RequestContext.getCurrentInstance();
                if (cualCelda == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:ciudadesDialogo");
                    RequestContext.getCurrentInstance().execute("PF('ciudadesDialogo').show()");
                    tipoActualizacion = 0;
                }
                if (cualCelda == 9) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:sucursalesPilaDialogo");
                    RequestContext.getCurrentInstance().execute("PF('sucursalesPilaDialogo').show()");
                    tipoActualizacion = 0;
                }
            }
        } catch (Exception e) {
            System.out.println("\n ERROR CONTROLUBICACIONESGEOGRAFICAS.listaValoresBoton ERROR====================" + e);

        }
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosUbicacionesGeograficasExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "UBICACIONESGEOGRAFICAS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosUbicacionesGeograficasExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "UBICACIONESGEOGRAFICAS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (ubicacionGeograficaSeleccionada != null) {
            System.out.println("lol 2");
            int resultado = administrarRastros.obtenerTabla(ubicacionGeograficaSeleccionada.getSecuencia(), "UBICACIONESGEOGRAFICAS"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
        } else if (administrarRastros.verificarHistoricosTabla("UBICACIONESGEOGRAFICAS")) { // igual acá
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
    }

    public void lovEmpresas() {
        ubicacionGeograficaSeleccionada = null;
        cualCelda = -1;
        RequestContext.getCurrentInstance().execute("PF('EmpresasDialogo').show()");
    }

    public void cambiarEmpresa() {
        RequestContext context = RequestContext.getCurrentInstance();
        System.err.println("Cambiar empresa  GUARDADO = " + guardado);
        System.err.println("Cambiar empresa  GUARDADO = " + empresaSeleccionada.getNombre());
        RequestContext.getCurrentInstance().update("form:nombreEmpresa");
        RequestContext.getCurrentInstance().update("form:nitEmpresa");
        listUbicacionesGeograficasPorEmpresa = null;
        getListUbicacionesGeograficasPorEmpresa();
        filtradoListaEmpresas = null;
        aceptar = true;
        backUpEmpresaActual = empresaSeleccionada;
        banderaModificacionEmpresa = 0;
        listaSucursalesPilas = null;
        RequestContext.getCurrentInstance().update("form:datosUbicacionesGeograficas");
        contarRegistros();
        context.reset("formularioDialogos:lovEmpresas:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovEmpresas').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('EmpresasDialogo').hide()");

        RequestContext.getCurrentInstance().update("formularioDialogos:EmpresasDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovEmpresas");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarE");

    }

    public void cancelarCambioEmpresa() {
        filtradoListaEmpresas = null;
        banderaModificacionEmpresa = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:lovEmpresas:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovEmpresas').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('EmpresasDialogo').hide()");

        RequestContext.getCurrentInstance().update("formularioDialogos:EmpresasDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovEmpresas");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarE");
    }
//-----------------------gets y sets---------------------------------**

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
    private String infoRegistroEmpresas;

    public List<Empresas> getListaEmpresas() {
        try {
            if (listaEmpresas == null) {
                listaEmpresas = administrarUbicacionesGeograficas.consultarEmpresas();
            }
            return listaEmpresas;
        } catch (Exception e) {
            System.out.println("ERROR LISTA EMPRESAS " + e);
            return null;
        }
    }

    public void setListaEmpresas(List<Empresas> listaEmpresas) {
        this.listaEmpresas = listaEmpresas;
    }

    public List<Empresas> getFiltradoListaEmpresas() {
        return filtradoListaEmpresas;
    }

    public void setFiltradoListaEmpresas(List<Empresas> filtradoListaEmpresas) {
        this.filtradoListaEmpresas = filtradoListaEmpresas;
    }

    public Empresas getEmpresaSeleccionada() {
        return empresaSeleccionada;
    }

    public void setEmpresaSeleccionada(Empresas empresaSeleccionada) {
        this.empresaSeleccionada = empresaSeleccionada;
    }

    public List<UbicacionesGeograficas> getListUbicacionesGeograficasPorEmpresa() {
        //try {
        if (listUbicacionesGeograficasPorEmpresa == null) {
            if (empresaSeleccionada != null) {
                listUbicacionesGeograficasPorEmpresa = administrarUbicacionesGeograficas.consultarUbicacionesGeograficasPorEmpresa(empresaSeleccionada.getSecuencia());
            }
        }
        return listUbicacionesGeograficasPorEmpresa;
    }

    public void setListUbicacionesGeograficasPorEmpresa(List<UbicacionesGeograficas> listUbicacionesGeograficasPorEmpresa) {
        this.listUbicacionesGeograficasPorEmpresa = listUbicacionesGeograficasPorEmpresa;
    }

    public List<UbicacionesGeograficas> getFiltrarUbicacionesGeograficas() {
        return filtrarUbicacionesGeograficas;
    }

    public void setFiltrarUbicacionesGeograficas(List<UbicacionesGeograficas> filtrarUbicacionesGeograficas) {
        this.filtrarUbicacionesGeograficas = filtrarUbicacionesGeograficas;
    }

    public UbicacionesGeograficas getNuevaUbicacionGeografica() {
        if (nuevaUbicacionGeografica == null) {
            nuevaUbicacionGeografica = new UbicacionesGeograficas();
        }
        return nuevaUbicacionGeografica;
    }

    public void setNuevaUbicacionGeografica(UbicacionesGeograficas nuevaUbicacionGeografica) {
        this.nuevaUbicacionGeografica = nuevaUbicacionGeografica;
    }

    public UbicacionesGeograficas getDuplicarUbicacionGeografica() {
        return duplicarUbicacionGeografica;
    }

    public void setDuplicarUbicacionGeografica(UbicacionesGeograficas duplicarUbicacionGeografica) {
        this.duplicarUbicacionGeografica = duplicarUbicacionGeografica;
    }

    public List<Ciudades> getListaCiudades() {
        if (listaCiudades == null) {
            listaCiudades = administrarUbicacionesGeograficas.lovCiudades();
        }
        return listaCiudades;
    }

    public void setListaCiudades(List<Ciudades> listaCiudades) {
        this.listaCiudades = listaCiudades;
    }

    public List<Ciudades> getFiltradoCiudades() {
        return filtradoCiudades;
    }

    public void setFiltradoCiudades(List<Ciudades> filtradoCiudades) {
        this.filtradoCiudades = filtradoCiudades;
    }

    public Ciudades getTipoCentroCostoSeleccionado() {
        return ciudadSeleccionada;
    }

    public void setTipoCentroCostoSeleccionado(Ciudades ciudadSeleccionada) {
        this.ciudadSeleccionada = ciudadSeleccionada;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public void setAceptar(boolean aceptar) {
        this.aceptar = aceptar;
    }

    public List<UbicacionesGeograficas> getFilterUbicacionesGeograficasPorEmpresa() {
        return filterUbicacionesGeograficasPorEmpresa;
    }

    public void setFilterUbicacionesGeograficasPorEmpresa(List<UbicacionesGeograficas> filterUbicacionesGeograficasPorEmpresa) {
        this.filterUbicacionesGeograficasPorEmpresa = filterUbicacionesGeograficasPorEmpresa;
    }

    public UbicacionesGeograficas getUbicacionesGeograficasPorEmpresaSeleccionado() {
        return UbicacionesGeograficasPorEmpresaSeleccionado;
    }

    public void setUbicacionesGeograficasPorEmpresaSeleccionado(UbicacionesGeograficas UbicacionesGeograficasPorEmpresaSeleccionado) {
        this.UbicacionesGeograficasPorEmpresaSeleccionado = UbicacionesGeograficasPorEmpresaSeleccionado;
    }

    public UbicacionesGeograficas getEditarCentroCosto() {
        return editarCentroCosto;
    }

    public void setEditarCentroCosto(UbicacionesGeograficas editarCentroCosto) {
        this.editarCentroCosto = editarCentroCosto;
    }

    public boolean isGuardado() {
        return guardado;
    }

    public void setGuardado(boolean guardado) {
        this.guardado = guardado;
    }

    public List<SucursalesPila> getListaSucursalesPilas() {
        listaSucursalesPilas = null;
        if (listaSucursalesPilas == null) {
            listaSucursalesPilas = administrarUbicacionesGeograficas.lovSucursalesPilaPorEmpresa(empresaSeleccionada.getSecuencia());
        }
        return listaSucursalesPilas;
    }

    public void setListaSucursalesPilas(List<SucursalesPila> listaSucursalesPilas) {
        this.listaSucursalesPilas = listaSucursalesPilas;
    }

    public List<SucursalesPila> getFiltradoSucursalesPilas() {
        return filtradoSucursalesPilas;
    }

    public void setFiltradoSucursalesPilas(List<SucursalesPila> filtradoSucursalesPilas) {
        this.filtradoSucursalesPilas = filtradoSucursalesPilas;
    }

    public SucursalesPila getSucursalesPilasSeleccionada() {
        return sucursalesPilasSeleccionada;
    }

    public void setSucursalesPilasSeleccionada(SucursalesPila sucursalesPilasSeleccionada) {
        this.sucursalesPilasSeleccionada = sucursalesPilasSeleccionada;
    }

    public Ciudades getCiudadSeleccionada() {
        return ciudadSeleccionada;
    }

    public void setCiudadSeleccionada(Ciudades ciudadSeleccionada) {
        this.ciudadSeleccionada = ciudadSeleccionada;
    }

    public int getTamano() {
        return tamano;
    }

    public void setTamano(int tamano) {
        this.tamano = tamano;
    }

    public UbicacionesGeograficas getUbicacionGeograficaSeleccionada() {
        return ubicacionGeograficaSeleccionada;
    }

    public void setUbicacionGeograficaSeleccionada(UbicacionesGeograficas ubicacionGeograficaSeleccionada) {
        this.ubicacionGeograficaSeleccionada = ubicacionGeograficaSeleccionada;
    }

    public String getInfoRegistro() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas");
        infoRegistro = String.valueOf(tabla.getRowCount());
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

    public String getInfoRegistroCiudades() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovCiudades");
        infoRegistroCiudades = String.valueOf(tabla.getRowCount());
        return infoRegistroCiudades;
    }

    public void setInfoRegistroCiudades(String infoRegistroCiudades) {
        this.infoRegistroCiudades = infoRegistroCiudades;
    }

    public String getInfoRegistroSucursalesPila() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovSucursalesPila");
        infoRegistroSucursalesPila = String.valueOf(tabla.getRowCount());
        return infoRegistroSucursalesPila;
    }

    public void setInfoRegistroSucursalesPila(String infoRegistroSucursalesPila) {
        this.infoRegistroSucursalesPila = infoRegistroSucursalesPila;
    }

    public String getInfoRegistroEmpresas() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovEmpresas");
        infoRegistroEmpresas = String.valueOf(tabla.getRowCount());
        return infoRegistroEmpresas;
    }

    public void setInfoRegistroEmpresas(String infoRegistroEmpresas) {
        this.infoRegistroEmpresas = infoRegistroEmpresas;
    }

}
