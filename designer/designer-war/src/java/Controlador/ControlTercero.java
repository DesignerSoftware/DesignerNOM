/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Ciudades;
import Entidades.Empleados;
import Entidades.Empresas;
import Entidades.Terceros;
import Entidades.TercerosSucursales;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarRastrosInterface;
import InterfaceAdministrar.AdministrarTerceroInterface;
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
import utilidadesUI.PrimefacesContextUI;

/**
 *
 * @author user
 */
@ManagedBean
@SessionScoped
public class ControlTercero implements Serializable {

    @EJB
    AdministrarTerceroInterface administrarTercero;
    @EJB
    AdministrarRastrosInterface administrarRastros;
    //
    private List<Terceros> listTerceros;
    private List<Terceros> filtrarListTercero;
    private List<Terceros> listTercerosLOV;
    private List<Terceros> filtrarListTerceroLOV;
    private Terceros terceroTablaSeleccionado;
    private Terceros terceroLOVSeleccionado;
    //
    private List<TercerosSucursales> listTercerosSucursales;
    private List<TercerosSucursales> filtrarListTercerosSucursales;
    private TercerosSucursales terceroSucursalTablaSeleccionado;
    //
    private Empresas empresaSeleccionada;
    //private Empresas empresaSeleccionada;
    private List<Empresas> listEmpresas;
    private List<Empresas> filtrarListEmpresas;
    private Empresas empresaActual;
    //
    private List<Ciudades> listCiudades;
    private List<Ciudades> filtrarListCiudades;
    private Ciudades ciudadSeleccionada;
    //
    private List<Terceros> listTerceroConsolidador;
    private List<Terceros> filtrarListTerceroConsolidador;
    private Terceros terceroCSeleccionado;
    //
    private Empleados empleado;
    private int tipoActualizacion;
    private int bandera;
    private int banderaTS;
    //Columnas Tabla VL
    private Column terceroNombre, terceroNIT, terceroDigitoVerificacion, terceroNITAlternativo, terceroCodigoSS, terceroCodigoSP, terceroCodigoSC, terceroTConsolidador, terceroNITConsolidado, terceroCiudad, terceroCodigoAlterno;
    //Columnas Tabla VP
    private Column terceroSucursalCodigo, terceroSucursalPatronal, terceroSucursalObservacion, terceroSucursalCiudad;
    //Otros
    private boolean aceptar;
    private List<Terceros> listTerceroModificar;
    private boolean guardado;
    public Terceros nuevoTercero;
    private List<Terceros> listTerceroCrear;
    private BigInteger l;
    private int k;
    private List<Terceros> listTerceroBorrar;
    private Terceros editarTercero;
    private TercerosSucursales editarTerceroSucursal;
    private int cualCelda, tipoLista;
    private Terceros duplicarTercero;
    private boolean permitirIndex, permitirIndexTS;
    //Variables Autompletar
    //private String motivoCambioSueldo, tiposEntidades, tiposSueldos, terceros;
    //Indices VigenciaProrrateo / VigenciaProrrateoProyecto
    private int cualCeldaTS, tipoListaTS;
    private String nombreXML;
    private String nombreTabla;
    private boolean cambiosTercero, cambiosTerceroSucursal;
    private String msnConfirmarRastro, msnConfirmarRastroHistorico;
    private BigInteger backUp;
    private String nombreTablaRastro;
    private List<TercerosSucursales> listTerceroSucursalCrear, listTerceroSucursalModificar, listTerceroSucursalBorrar;
    private TercerosSucursales nuevoTerceroSucursal, duplicarTerceroSucursal;
    private String terceroConsolidador, ciudad, ciudadTS;
    private long nitConsolidado;
    private String paginaAnterior;
    //
    private String infoRegistroEmpresa, infoRegistroCiudad2, infoRegistroCiudad1, infoRegistroTercero, infoRegistroTerceroConsolidador, infoRegistroTerceroSucursal;
    //
    private String altoTablaTercero, altoTablaSucursal;
    //
    private String auxNombreTercero;
    private long auxNitTercero;
    //
    private BigInteger auxCodigoSucursal;
    //
    private DataTable tablaT;
    //
    private boolean activarLOV;

    /**
     * Creates a new instance of ControlTercero
     */
    public ControlTercero() {
        altoTablaTercero = "150";
        altoTablaSucursal = "70";
        terceroLOVSeleccionado = new Terceros();
        listTercerosLOV = null;
        listTerceros = null;
        editarTerceroSucursal = new TercerosSucursales();
        listCiudades = null;
        listTerceroConsolidador = null;
        empresaSeleccionada = new Empresas();
        empresaActual = new Empresas();
        listEmpresas = null;
        listTercerosSucursales = null;
        nombreTablaRastro = "";
        backUp = null;
        msnConfirmarRastro = "";
        msnConfirmarRastroHistorico = "";
        terceroTablaSeleccionado = null;
        terceroSucursalTablaSeleccionado = null;
        empleado = new Empleados();
        //Otros
        aceptar = true;
        //borrar aficiones
        listTerceroBorrar = new ArrayList<Terceros>();
        //crear aficiones
        listTerceroCrear = new ArrayList<Terceros>();
        listTerceroSucursalBorrar = new ArrayList<TercerosSucursales>();
        listTerceroSucursalModificar = new ArrayList<TercerosSucursales>();
        listTerceroSucursalCrear = new ArrayList<TercerosSucursales>();
        k = 0;
        //modificar aficiones
        listTerceroModificar = new ArrayList<Terceros>();
        //editar
        editarTercero = new Terceros();
        cualCelda = -1;
        tipoLista = 0;
        tipoListaTS = 0;
        //guardar
        guardado = true;
        //Crear VC
        nuevoTercero = new Terceros();
        nuevoTercero.setTerceroconsolidador(new Terceros());
        nuevoTercero.setCiudad(new Ciudades());
        nuevoTerceroSucursal = new TercerosSucursales();
        nuevoTerceroSucursal.setCiudad(new Ciudades());
        bandera = 0;
        banderaTS = 0;
        permitirIndex = true;
        permitirIndexTS = true;
        cualCeldaTS = -1;

        nombreTabla = ":formExportarTerceros:datosTercerosExportar";
        nombreXML = "TercerosXML";

        cambiosTerceroSucursal = false;
        duplicarTercero = new Terceros();
        cambiosTercero = false;
        activarLOV = true;
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarTercero.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public String valorPaginaAnterior() {
        return paginaAnterior;
    }
    /*
     * public void recibirPagina(String pagina) { paginaAnterior = pagina; }
     */

    public void recibirPaginaEntrante(String pagina) {
        paginaAnterior = pagina;
        listEmpresas = administrarTercero.listEmpresas();
        int tam = listEmpresas.size();
        if (tam > 0) {
            empresaActual = listEmpresas.get(0);
            empresaSeleccionada = empresaActual;
            listTerceros = null;
            getListTerceros();
            if (listTerceros != null) {
                if (!listTerceros.isEmpty()) {
                    terceroTablaSeleccionado = listTerceros.get(0);
                }
            }
            getListTercerosSucursales();
            contarRegistrosT();
            contarRegistrosTS();
            contarRegistrosEmpresa();
        }
    }

    /**
     * Modifica los elementos de la tabla VigenciaLocalizacion que no usan
     * autocomplete
     *
     * @param indice Fila donde se efectu el cambio
     */
    public void modificarTercero(Terceros t) {
        RequestContext context = RequestContext.getCurrentInstance();
        terceroTablaSeleccionado = t;
        if (validarCamposNulosTercero(0) == true) {
            if (!listTerceroCrear.contains(terceroTablaSeleccionado)) {
                if (listTerceroModificar.isEmpty()) {
                    listTerceroModificar.add(terceroTablaSeleccionado);
                } else if (!listTerceroModificar.contains(terceroTablaSeleccionado)) {
                    listTerceroModificar.add(terceroTablaSeleccionado);
                }
                if (guardado) {
                    guardado = false;
                    PrimefacesContextUI.actualizar("form:ACEPTAR");
                }
            }
            terceroTablaSeleccionado.getTerceroconsolidador().setNit(nitConsolidado);

            cambiosTercero = true;
        } else {
            terceroTablaSeleccionado.setNit(auxNitTercero);
            terceroTablaSeleccionado.setNombre(auxNombreTercero);

            PrimefacesContextUI.ejecutar("PF('errorDatosNullTerceros').show()");
        }
        PrimefacesContextUI.actualizar("form:datosTerceros");
    }

    public void modificarTerceroAutocompletar(Terceros terceros, String confirmarCambio, String valorConfirmar) {
        terceroTablaSeleccionado = terceros;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("TERCEROCONSOLIDADOR")) {
            activarLOV = false;
            PrimefacesContextUI.actualizar("form:listaValores");
            if (!valorConfirmar.isEmpty()) {
                terceroTablaSeleccionado.getTerceroconsolidador().setNombre(terceroConsolidador);

                for (int i = 0; i < listTerceroConsolidador.size(); i++) {
                    if (listTerceroConsolidador.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    terceroTablaSeleccionado.setTerceroconsolidador(listTerceroConsolidador.get(indiceUnicoElemento));

                    listTerceroConsolidador.clear();
                    getListTerceroConsolidador();
                } else {
                    permitirIndex = false;
                    PrimefacesContextUI.actualizar("form:TerceroDialogo");
                    PrimefacesContextUI.ejecutar("PF('TerceroDialogo').show()");
                    tipoActualizacion = 0;
                }
            } else {
                coincidencias = 1;
                terceroTablaSeleccionado.setTerceroconsolidador(new Terceros());

                listTerceroConsolidador.clear();
                getListTerceroConsolidador();
            }
        }
        if (confirmarCambio.equalsIgnoreCase("CIUDAD")) {
            aceptar = false;
            if (!valorConfirmar.isEmpty()) {
                terceroTablaSeleccionado.getCiudad().setNombre(ciudad);

                for (int i = 0; i < listCiudades.size(); i++) {
                    if (listCiudades.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    terceroTablaSeleccionado.setCiudad(listCiudades.get(indiceUnicoElemento));

                    listCiudades.clear();
                    getListCiudades();
                } else {
                    permitirIndex = false;
                    PrimefacesContextUI.actualizar("form:CiudadDialogo");
                    PrimefacesContextUI.ejecutar("PF('CiudadDialogo').show()");
                    tipoActualizacion = 0;
                }
            } else {
                coincidencias = 1;
                terceroTablaSeleccionado.setCiudad(new Ciudades());

                listCiudades.clear();
                getListCiudades();
            }
        }
        if (coincidencias == 1) {
            if (!listTerceroCrear.contains(terceroTablaSeleccionado)) {
                if (listTerceroModificar.isEmpty()) {
                    listTerceroModificar.add(terceroTablaSeleccionado);
                } else if (!listTerceroModificar.contains(terceroTablaSeleccionado)) {
                    listTerceroModificar.add(terceroTablaSeleccionado);
                }
                if (guardado) {
                    guardado = false;
                    PrimefacesContextUI.actualizar("form:ACEPTAR");
                }
            }
            cambiosTercero = true;
        }
        PrimefacesContextUI.actualizar("form:datosTerceros");
    }

    public void modificarTerceroSucursal(int indice) {
        RequestContext context = RequestContext.getCurrentInstance();
        if (validarCamposNulosTerceroSucursal(0) == true) {
            if (!listTerceroSucursalCrear.contains(terceroSucursalTablaSeleccionado)) {
                if (listTerceroSucursalModificar.isEmpty()) {
                    listTerceroSucursalModificar.add(terceroSucursalTablaSeleccionado);
                } else if (!listTerceroSucursalModificar.contains(terceroSucursalTablaSeleccionado)) {
                    listTerceroSucursalModificar.add(terceroSucursalTablaSeleccionado);
                }
                if (guardado) {
                    guardado = false;
                    PrimefacesContextUI.actualizar("form:ACEPTAR");
                }
            }

            cambiosTerceroSucursal = true;
        } else {
            terceroSucursalTablaSeleccionado.setCodigosucursal(auxCodigoSucursal);

            PrimefacesContextUI.ejecutar("PF('errorDatosNullSucursal').show()");
        }
        PrimefacesContextUI.actualizar("form:datosTercerosSucursales");
    }

    public void modificarTerceroSucursalAutocompletar(Terceros terceros, String confirmarCambio, String valorConfirmar) {
        terceroTablaSeleccionado = terceros;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("CIUDAD")) {
            activarLOV = false;
            PrimefacesContextUI.actualizar("form:listaValores");
            if (!valorConfirmar.isEmpty()) {
                terceroSucursalTablaSeleccionado.getCiudad().setNombre(ciudadTS);

                for (int i = 0; i < listCiudades.size(); i++) {
                    if (listCiudades.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    terceroSucursalTablaSeleccionado.setCiudad(listCiudades.get(indiceUnicoElemento));

                    listCiudades.clear();
                    getListCiudades();
                } else {
                    permitirIndex = false;
                    PrimefacesContextUI.actualizar("form:CiudadTSDialogo");
                    PrimefacesContextUI.ejecutar("PF('CiudadTSDialogo').show()");
                    tipoActualizacion = 0;
                }
            } else {
                coincidencias = 1;
                terceroSucursalTablaSeleccionado.setCiudad(new Ciudades());

                listCiudades.clear();
                getListCiudades();
            }
        }
        if (coincidencias == 1) {
            if (!listTerceroSucursalCrear.contains(terceroSucursalTablaSeleccionado)) {
                if (listTerceroSucursalModificar.isEmpty()) {
                    listTerceroSucursalModificar.add(terceroSucursalTablaSeleccionado);
                } else if (!listTerceroSucursalModificar.contains(terceroSucursalTablaSeleccionado)) {
                    listTerceroSucursalModificar.add(terceroSucursalTablaSeleccionado);
                }
                if (guardado) {
                    guardado = false;
                    PrimefacesContextUI.actualizar("form:ACEPTAR");
                }
            }

            cambiosTerceroSucursal = true;
        }
        PrimefacesContextUI.actualizar("form:datosTercerosSucursales");
    }

    ///////////////////////////////////////////////////////////////////////////
    /**
     * Modifica los elementos de la tabla VigenciaProrrateo que no usan
     * autocomplete
     *
     * @param indice Fila donde se efectu el cambio
     */
    //Ubicacion Celda.
    /**
     * Metodo que obtiene la posicion dentro de la tabla VigenciasLocalizaciones
     *
     * @param indice Fila de la tabla
     * @param celda Columna de la tabla
     */
    public void cambiarIndice(Terceros terceros, int celda) {
        if (cambiosTerceroSucursal == false) {
            if (permitirIndex == true) {
                activarLOV = true;
                PrimefacesContextUI.actualizar("form:listaValores");
                if (banderaTS == 1) {
                    cerrarFiltradoTercero();
                }
                cualCelda = celda;
                terceroTablaSeleccionado = terceros;
                //indexAux = indice;
                auxNitTercero = terceroTablaSeleccionado.getNit();
                auxNombreTercero = terceroTablaSeleccionado.getNombre();
                if (cualCelda == 7) {
                    activarLOV = false;
                    PrimefacesContextUI.actualizar("form:listaValores");
                    terceroConsolidador = terceroTablaSeleccionado.getTerceroconsolidador().getNombre();
                }
                if (cualCelda == 8) {
                    activarLOV = false;
                    PrimefacesContextUI.actualizar("form:listaValores");
                    nitConsolidado = terceroTablaSeleccionado.getTerceroconsolidador().getNit();
                }
                if (cualCelda == 9) {
                    activarLOV = false;
                    PrimefacesContextUI.actualizar("form:listaValores");
                    ciudad = terceroTablaSeleccionado.getCiudad().getNombre();
                }
                listTercerosSucursales = null;
                getListTercerosSucursales();
                PrimefacesContextUI.actualizar("form:datosTercerosSucursales");
            }
        }
        if (cambiosTerceroSucursal == true) {
            RequestContext context = RequestContext.getCurrentInstance();
            PrimefacesContextUI.actualizar("form:confirmarGuardar");
            PrimefacesContextUI.ejecutar("PF('confirmarGuardar').show()");
        }

        contarRegistrosTS();
        PrimefacesContextUI.actualizar("form:infoRegistroTerceroSucursal");
    }

    public void cambiarIndiceTS(TercerosSucursales tercerosSucursales, int celda) {
        if (permitirIndexTS == true) {
            activarLOV = true;
            PrimefacesContextUI.actualizar("form:listaValores");
            cualCeldaTS = celda;
            terceroSucursalTablaSeleccionado = tercerosSucursales;
            auxCodigoSucursal = terceroSucursalTablaSeleccionado.getCodigosucursal();
            if (cualCeldaTS == 3) {
                activarLOV = false;
                PrimefacesContextUI.actualizar("form:listaValores");
                ciudadTS = terceroSucursalTablaSeleccionado.getCiudad().getNombre();
            }

        }
    }

    //GUARDAR
    /**
     * Metodo de guardado general para la pagina
     */
    public void guardadoGeneral() {
        if (guardado == false) {
            if (cambiosTerceroSucursal == true) {
                guardarCambiosTerceroSucursales();
            } else if (cambiosTercero == true) {
                guardarCambiosTercero();
            }
            guardado = true;
            PrimefacesContextUI.actualizar("form:ACEPTAR");
        }
        activarLOV = true;
        PrimefacesContextUI.actualizar("form:listaValores");
    }

    public void guardarCambiosTercero() {
        activarLOV = true;
        PrimefacesContextUI.actualizar("form:listaValores");
        RequestContext context = RequestContext.getCurrentInstance();
        try {
            if (guardado == false) {
                if (!listTerceroBorrar.isEmpty()) {
                    for (int i = 0; i < listTerceroBorrar.size(); i++) {
                        if (listTerceroBorrar.get(i).getTerceroconsolidador().getSecuencia() == null) {
                            listTerceroBorrar.get(i).setTerceroconsolidador(null);
                        }
                        if (listTerceroBorrar.get(i).getCiudad().getSecuencia() == null) {
                            listTerceroBorrar.get(i).setCiudad(null);
                        }
                        administrarTercero.borrarTercero(listTerceroBorrar.get(i));
                    }
                    listTerceroBorrar.clear();
                }
                if (!listTerceroCrear.isEmpty()) {
                    for (int i = 0; i < listTerceroCrear.size(); i++) {
                        if (listTerceroCrear.get(i).getTerceroconsolidador().getSecuencia() == null) {
                            listTerceroCrear.get(i).setTerceroconsolidador(null);
                        }
                        if (listTerceroCrear.get(i).getCiudad().getSecuencia() == null) {
                            listTerceroCrear.get(i).setCiudad(null);
                        }
                        administrarTercero.crearTercero(listTerceroCrear.get(i));
                    }
                    listTerceroCrear.clear();
                }
                if (!listTerceroModificar.isEmpty()) {
                    for (int i = 0; i < listTerceroModificar.size(); i++) {
                        if (listTerceroModificar.get(i).getTerceroconsolidador().getSecuencia() == null) {
                            listTerceroModificar.get(i).setTerceroconsolidador(null);
                        }
                        if (listTerceroModificar.get(i).getCiudad().getSecuencia() == null) {
                            listTerceroModificar.get(i).setCiudad(null);
                        }
                        administrarTercero.modificarTercero(listTerceroModificar.get(i));
                    }
                    listTerceroModificar.clear();
                }

                if (listTerceros != null) {
                    modificarInfoRegistro(listTerceros.size());
                } else {
                    modificarInfoRegistro(0);
                }

                //listTerceros = null;
                PrimefacesContextUI.actualizar("form:datosTerceros");
                k = 0;
                cambiosTercero = false;
                FacesMessage msg = new FacesMessage("Información", "Se guardarón los datos de Tercero con éxito");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                PrimefacesContextUI.actualizar("form:growl");
                contarRegistrosT();
                PrimefacesContextUI.actualizar("form:infoRegistro");
            }
        } catch (Exception e) {
            System.out.println("Error guardarCambiosTercero Controlador : " + e.toString());
            FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado de Tercero, intente nuevamente.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            PrimefacesContextUI.actualizar("form:growl");
        }
    }

    public void guardarCambiosTerceroSucursales() {
        RequestContext context = RequestContext.getCurrentInstance();
        activarLOV = true;
        PrimefacesContextUI.actualizar("form:listaValores");
        try {
            if (guardado == false) {
                if (!listTerceroSucursalBorrar.isEmpty()) {
                    for (int i = 0; i < listTerceroSucursalBorrar.size(); i++) {
                        if (listTerceroSucursalBorrar.get(i).getCiudad().getSecuencia() == null) {
                            listTerceroSucursalBorrar.get(i).setCiudad(null);
                        }
                        administrarTercero.borrarTerceroSucursales(listTerceroSucursalBorrar.get(i));
                    }
                    listTerceroSucursalBorrar.clear();
                }
                if (!listTerceroSucursalCrear.isEmpty()) {
                    for (int i = 0; i < listTerceroSucursalCrear.size(); i++) {
                        if (listTerceroSucursalCrear.get(i).getCiudad().getSecuencia() == null) {
                            listTerceroSucursalCrear.get(i).setCiudad(null);
                        }
                        administrarTercero.crearTerceroSucursales(listTerceroSucursalCrear.get(i));
                    }
                    listTerceroSucursalCrear.clear();
                }
                if (!listTerceroSucursalModificar.isEmpty()) {
                    for (int i = 0; i < listTerceroSucursalModificar.size(); i++) {
                        if (listTerceroSucursalModificar.get(i).getCiudad().getSecuencia() == null) {
                            listTerceroSucursalModificar.get(i).setCiudad(null);
                        }
                        administrarTercero.modificarTerceroSucursales(listTerceroSucursalModificar.get(i));
                    }
                    listTerceroSucursalModificar.clear();
                }
                if (listTercerosSucursales != null) {
                    modificarInfoRegistroTS(listTercerosSucursales.size());
                } else {
                    modificarInfoRegistroTS(0);
                }
                PrimefacesContextUI.actualizar("form:infoRegistroTerceroSucursal");
                PrimefacesContextUI.actualizar("form:datosTercerosSucursales");
                k = 0;
                cambiosTerceroSucursal = false;
                FacesMessage msg = new FacesMessage("Información", "Se guardarón los datos de Tercero Sucursal con éxito");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                PrimefacesContextUI.actualizar("form:growl");
                contarRegistrosTS();
                PrimefacesContextUI.actualizar("form:infoRegistroTerceroSucursal");
            }
        } catch (Exception e) {
            System.out.println("Error guardarCambiosTercero Controlador : " + e.toString());
            FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado de Tercero Sucursal, intente nuevamente.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            PrimefacesContextUI.actualizar("form:growl");
        }
    }

    //CANCELAR MODIFICACIONES
    /**
     * Cancela las modificaciones realizas en la pagina
     */
    public void cancelarModificacion() {
        if (bandera == 1) {
            cerrarFiltradoTercero();
        }
        if (banderaTS == 1) {
            cerrarFiltradoTerceroSucursal();
        }
        listTerceroBorrar.clear();
        listTerceroCrear.clear();
        listTerceroModificar.clear();
        listTerceroSucursalBorrar.clear();
        listTerceroSucursalCrear.clear();
        listTerceroSucursalModificar.clear();
        terceroSucursalTablaSeleccionado = null;
        activarLOV = true;
        PrimefacesContextUI.actualizar("form:listaValores");
        //indexAux = -1;
        terceroTablaSeleccionado = null;
        k = 0;
        listTerceros = null;
        listTercerosSucursales = null;
        cambiosTerceroSucursal = false;
        guardado = true;
        cambiosTercero = false;
        getListTerceros();
        getListTercerosSucursales();
        contarRegistrosT();
        PrimefacesContextUI.actualizar("form:infoRegistro");
        contarRegistrosTS();
        PrimefacesContextUI.actualizar("form:infoRegistroTerceroSucursal");
        RequestContext context = RequestContext.getCurrentInstance();
        PrimefacesContextUI.actualizar("form:datosTerceros");
        PrimefacesContextUI.actualizar("form:datosTercerosSucursales");
        PrimefacesContextUI.actualizar("form:ACEPTAR");
    }

    public void cerrarFiltradoTercero() {
        FacesContext c = FacesContext.getCurrentInstance();
        altoTablaTercero = "150";
        terceroNombre = (Column) c.getViewRoot().findComponent("form:datosTerceros:terceroNombre");
        terceroNombre.setFilterStyle("display: none; visibility: hidden;");
        terceroNIT = (Column) c.getViewRoot().findComponent("form:datosTerceros:terceroNIT");
        terceroNIT.setFilterStyle("display: none; visibility: hidden;");
        terceroDigitoVerificacion = (Column) c.getViewRoot().findComponent("form:datosTerceros:terceroDigitoVerificacion");
        terceroDigitoVerificacion.setFilterStyle("display: none; visibility: hidden;");
        terceroNITAlternativo = (Column) c.getViewRoot().findComponent("form:datosTerceros:terceroNITAlternativo");
        terceroNITAlternativo.setFilterStyle("display: none; visibility: hidden;");
        terceroCodigoSS = (Column) c.getViewRoot().findComponent("form:datosTerceros:terceroCodigoSS");
        terceroCodigoSS.setFilterStyle("display: none; visibility: hidden;");
        terceroCodigoSP = (Column) c.getViewRoot().findComponent("form:datosTerceros:terceroCodigoSP");
        terceroCodigoSP.setFilterStyle("display: none; visibility: hidden;");
        terceroCodigoSC = (Column) c.getViewRoot().findComponent("form:datosTerceros:terceroCodigoSC");
        terceroCodigoSC.setFilterStyle("display: none; visibility: hidden;");
        terceroTConsolidador = (Column) c.getViewRoot().findComponent("form:datosTerceros:terceroTConsolidador");
        terceroTConsolidador.setFilterStyle("display: none; visibility: hidden;");
        terceroNITConsolidado = (Column) c.getViewRoot().findComponent("form:datosTerceros:terceroNITConsolidado");
        terceroNITConsolidado.setFilterStyle("display: none; visibility: hidden;");
        terceroCiudad = (Column) c.getViewRoot().findComponent("form:datosTerceros:terceroCiudad");
        terceroCiudad.setFilterStyle("display: none; visibility: hidden;");
        terceroCodigoAlterno = (Column) c.getViewRoot().findComponent("form:datosTerceros:terceroCiudad");
        terceroCodigoAlterno.setFilterStyle("display: none; visibility: hidden;");
        PrimefacesContextUI.actualizar("form:datosTerceros");
        bandera = 0;
        filtrarListTercero = null;
        tipoLista = 0;
    }

    public void cerrarFiltradoTerceroSucursal() {
        FacesContext c = FacesContext.getCurrentInstance();
        altoTablaSucursal = "70";
        terceroSucursalCodigo = (Column) c.getViewRoot().findComponent("form:datosTercerosSucursales:terceroSucursalCodigo");
        terceroSucursalCodigo.setFilterStyle("display: none; visibility: hidden;");
        terceroSucursalPatronal = (Column) c.getViewRoot().findComponent("form:datosTercerosSucursales:terceroSucursalPatronal");
        terceroSucursalPatronal.setFilterStyle("display: none; visibility: hidden;");
        terceroSucursalObservacion = (Column) c.getViewRoot().findComponent("form:datosTercerosSucursales:terceroSucursalObservacion");
        terceroSucursalObservacion.setFilterStyle("display: none; visibility: hidden;");
        terceroSucursalCiudad = (Column) c.getViewRoot().findComponent("form:datosTercerosSucursales:terceroSucursalCiudad");
        terceroSucursalCiudad.setFilterStyle("display: none; visibility: hidden;");
        PrimefacesContextUI.actualizar("form:datosTercerosSucursales");
        banderaTS = 0;
        filtrarListTercerosSucursales = null;
        tipoListaTS = 0;
    }

    //MOSTRAR DATOS CELDA
    /**
     * Metodo que muestra los dialogos de editar con respecto a la lista real o
     * la lista filtrada y a la columna
     */
    public void editarCelda() {
        if (terceroTablaSeleccionado != null) {
            editarTercero = terceroTablaSeleccionado;
            activarLOV = true;
            PrimefacesContextUI.actualizar("form:listaValores");
            RequestContext context = RequestContext.getCurrentInstance();
            if (cualCelda == 0) {
                PrimefacesContextUI.actualizar("formularioDialogos:editarNombreTerceroD");
                PrimefacesContextUI.ejecutar("PF('editarNombreTerceroD').show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                PrimefacesContextUI.actualizar("formularioDialogos:editarNitTerceroD");
                PrimefacesContextUI.ejecutar("PF('editarNitTerceroD').show()");
                cualCelda = -1;
            } else if (cualCelda == 2) {
                PrimefacesContextUI.actualizar("formularioDialogos:editarDigtoVTerceroD");
                PrimefacesContextUI.ejecutar("PF('editarDigtoVTerceroD').show()");
                cualCelda = -1;
            } else if (cualCelda == 3) {
                PrimefacesContextUI.actualizar("formularioDialogos:editarNITATerceroD");
                PrimefacesContextUI.ejecutar("PF('editarNITATerceroD').show()");
                cualCelda = -1;
            } else if (cualCelda == 4) {
                PrimefacesContextUI.actualizar("formularioDialogos:editarCSSTerceroD");
                PrimefacesContextUI.ejecutar("PF('editarCSSTerceroD').show()");
                cualCelda = -1;
            } else if (cualCelda == 5) {
                PrimefacesContextUI.actualizar("formularioDialogos:editarCSPTerceroD");
                PrimefacesContextUI.ejecutar("PF('editarCSPTerceroD').show()");
                cualCelda = -1;
            } else if (cualCelda == 6) {
                PrimefacesContextUI.actualizar("formularioDialogos:editarCSCTerceroD");
                PrimefacesContextUI.ejecutar("PF('editarCSCTerceroD').show()");
                cualCelda = -1;
            } else if (cualCelda == 7) {
                PrimefacesContextUI.actualizar("formularioDialogos:editarTCTerceroD");
                PrimefacesContextUI.ejecutar("PF('editarTCTerceroD').show()");
                cualCelda = -1;
            } else if (cualCelda == 8) {
                PrimefacesContextUI.actualizar("formularioDialogos:editarNITTCTerceroD");
                PrimefacesContextUI.ejecutar("PF('editarNITTCTerceroD').show()");
                cualCelda = -1;
            } else if (cualCelda == 9) {
                PrimefacesContextUI.actualizar("formularioDialogos:editarCiudadTerceroD");
                PrimefacesContextUI.ejecutar("PF('editarCiudadTerceroD').show()");
                cualCelda = -1;
            } else if (cualCelda == 10) {
                PrimefacesContextUI.actualizar("formularioDialogos:editarCodigoATerceroD");
                PrimefacesContextUI.ejecutar("PF('editarCodigoATerceroD').show()");
                cualCelda = -1;
            }
        }
        if (terceroSucursalTablaSeleccionado != null) {
            editarTerceroSucursal = terceroSucursalTablaSeleccionado;

            RequestContext context = RequestContext.getCurrentInstance();
            if (cualCeldaTS == 0) {
                PrimefacesContextUI.actualizar("formularioDialogos:editarSucursalTerceroSD");
                PrimefacesContextUI.ejecutar("PF('editarSucursalTerceroSD').show()");
                cualCeldaTS = -1;
            } else if (cualCeldaTS == 1) {
                PrimefacesContextUI.actualizar("formularioDialogos:editarPatronalTerceroSD");
                PrimefacesContextUI.ejecutar("PF('editarPatronalTerceroSD').show()");
                cualCeldaTS = -1;
            } else if (cualCeldaTS == 2) {
                PrimefacesContextUI.actualizar("formularioDialogos:editarObservacionesTerceroSD");
                PrimefacesContextUI.ejecutar("PF('editarObservacionesTerceroSD').show()");
                cualCeldaTS = -1;
            } else if (cualCeldaTS == 3) {
                PrimefacesContextUI.actualizar("formularioDialogos:editarCiudadTerceroSD");
                PrimefacesContextUI.ejecutar("PF('editarCiudadTerceroSD').show()");
                cualCeldaTS = -1;
            }
        }
    }

    public void validarIngresoNuevoRegistro() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (terceroTablaSeleccionado != null) {
            if (cambiosTerceroSucursal == false) {
                if (listTercerosSucursales.isEmpty()) {
                    PrimefacesContextUI.ejecutar("PF('NuevoRegistroPagina').show()");
                } else {
                    PrimefacesContextUI.actualizar("form:NuevoRegistroTercero");
                    PrimefacesContextUI.ejecutar("PF('NuevoRegistroTercero').show()");
                }
            } else {
                PrimefacesContextUI.ejecutar("PF('confirmarGuardar').show()");
            }
        }
        if (terceroSucursalTablaSeleccionado != null) {
            PrimefacesContextUI.actualizar("form:NuevoRegistroTerceroSucursal");
            PrimefacesContextUI.ejecutar("PF('NuevoRegistroTerceroSucursal').show()");
        }
    }

    public void validarDuplicadoRegistro() {
        if (terceroSucursalTablaSeleccionado != null) {
            duplicarTerceroS();
        } else if (terceroTablaSeleccionado != null) {
            duplicarTercero();
        }
    }

    public void validarBorradoRegistro() {
        if (terceroSucursalTablaSeleccionado != null) {
            borrarTerceroSucursal();
        } else if (terceroTablaSeleccionado != null) {
            if (listTercerosSucursales == null || listTercerosSucursales.isEmpty()) {
                borrarTercero();
            } else {
                RequestContext context = RequestContext.getCurrentInstance();
                PrimefacesContextUI.ejecutar("PF('errorBorradoTercero').show()");
            }
        }
    }

    public boolean validarCamposNulosTercero(int i) {
        boolean retorno = true;
        if (i == 0) {
            Terceros aux = null;
            aux = terceroTablaSeleccionado;

            if (aux.getNit() < 0) {
                retorno = false;
            }
            if (aux.getNombre() == null) {
                retorno = false;
            } else {
                if (aux.getNombre().isEmpty()) {
                    retorno = false;
                }
            }
        }
        if (i == 1) {
            if (nuevoTercero.getNit() < 0) {
                retorno = false;
            }
            if (nuevoTercero.getNombre() == null) {
                retorno = false;
            } else {
                if (nuevoTercero.getNombre().isEmpty()) {
                    retorno = false;
                }
            }
        }
        if (i == 2) {
            if (duplicarTercero.getNit() < 0) {
                retorno = false;
            }
            if (duplicarTercero.getNombre() == null) {
                retorno = false;
            } else {
                if (duplicarTercero.getNombre().isEmpty()) {
                    retorno = false;
                }
            }
        }
        return retorno;
    }

    public boolean validarCamposNulosTerceroSucursal(int i) {
        System.err.println("Estoy en validarCamposNulosTerceroSucursal()");
        boolean retorno = true;
        System.out.println("El valor de i: " + i);
        if (i == 0) {
            System.out.println("Entre a if (i == 0)");
            if (terceroSucursalTablaSeleccionado.getCodigosucursal() == null) {
                retorno = false;
            }
        }
        if (i == 1) {
            System.out.println("Entre a if (i == 1)");
            if (nuevoTerceroSucursal.getCodigosucursal() == null) {
                retorno = false;
            }
        }
        if (i == 2) {
            System.out.println("Entre a if (i == 2)");
            System.out.println("duplicarTerceroSucursal.getCodigosucursal(): " + duplicarTerceroSucursal.getCodigosucursal());
            if (duplicarTerceroSucursal.getCodigosucursal() == null) {
                retorno = false;
            }
        }
        System.out.println("retorno: " + retorno);
        return retorno;
    }

    public void agregarNuevoTercero() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (validarCamposNulosTercero(1) == true) {
            if (bandera == 1) {
                cerrarFiltradoTercero();
            }
            if (banderaTS == 1) {
                cerrarFiltradoTerceroSucursal();
            }
            //AGREGAR REGISTRO A LA LISTA VIGENCIAS 
            k++;
            BigInteger var = BigInteger.valueOf(k);
            nuevoTercero.setSecuencia(var);
            nuevoTercero.setEmpresa(empresaActual);
            listTerceroCrear.add(nuevoTercero);
            if (listTerceros == null) {
                listTerceros = new ArrayList<Terceros>();
            }
            listTerceros.add(nuevoTercero);
            terceroTablaSeleccionado = listTerceros.get(listTerceros.indexOf(nuevoTercero));
            modificarInfoRegistro(listTerceros.size());
            ////------////
            nuevoTercero = new Terceros();
            nuevoTercero.setTerceroconsolidador(new Terceros());
            nuevoTercero.setCiudad(new Ciudades());
            ////-----////

            PrimefacesContextUI.actualizar("form:datosTerceros");
            if (guardado) {
                guardado = false;
                PrimefacesContextUI.actualizar("form:ACEPTAR");
            }
            PrimefacesContextUI.ejecutar("PF('NuevoRegistroTercero').hide()");
            cambiosTercero = true;
        } else {
            PrimefacesContextUI.ejecutar("PF('errorDatosNullTerceros').show()");
        }
    }

    //LIMPIAR NUEVO REGISTRO
    /**
     * Metodo que limpia las casillas de la nueva vigencia
     */
    public void limpiarNuevoTercero() {
        nuevoTercero = new Terceros();
        nuevoTercero.setTerceroconsolidador(new Terceros());
        nuevoTercero.setCiudad(new Ciudades());
    }

    ////////--- //// ---////
    public void agregarNuevoTerceroSucursal() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (validarCamposNulosTerceroSucursal(1) == true) {
            if (bandera == 1) {
                cerrarFiltradoTercero();
            }
            if (banderaTS == 1) {
                cerrarFiltradoTerceroSucursal();
            }
            //AGREGAR REGISTRO A LA LISTA VIGENCIAS 
            k++;
            BigInteger var = BigInteger.valueOf(k);
            nuevoTerceroSucursal.setSecuencia(var);
            nuevoTerceroSucursal.setTercero(terceroTablaSeleccionado);
            listTerceroSucursalCrear.add(nuevoTerceroSucursal);
            listTercerosSucursales.add(nuevoTerceroSucursal);


            if (listTercerosSucursales == null) {
                listTercerosSucursales = new ArrayList<TercerosSucursales>();
            }
            terceroSucursalTablaSeleccionado = listTercerosSucursales.get(listTercerosSucursales.indexOf(nuevoTerceroSucursal));
            modificarInfoRegistroTS(listTercerosSucursales.size());
            PrimefacesContextUI.actualizar("form:infoRegistroTerceroSucursal");
            ////------////
            nuevoTerceroSucursal = new TercerosSucursales();
            nuevoTerceroSucursal.setCiudad(new Ciudades());
            ////-----////
            PrimefacesContextUI.actualizar("form:datosTercerosSucursales");
            if (guardado) {
                guardado = false;
                PrimefacesContextUI.actualizar("form:ACEPTAR");
            }
            PrimefacesContextUI.ejecutar("PF('NuevoRegistroTerceroSucursal').hide()");
            cambiosTerceroSucursal = true;
        } else {
            PrimefacesContextUI.ejecutar("PF('errorDatosNullSucursal').show()");
        }
        terceroSucursalTablaSeleccionado = null;
    }

    //LIMPIAR NUEVO REGISTRO
    /**
     * Metodo que limpia las casillas de la nueva vigencia
     */
    public void limpiarNuevoTerceroSucursal() {
        terceroSucursalTablaSeleccionado = null;
        nuevoTerceroSucursal = new TercerosSucursales();
        nuevoTerceroSucursal.setCiudad(new Ciudades());
    }
    //DUPLICAR VL

    /**
     * Metodo que verifica que proceso de duplicar se genera con respecto a la
     * posicion en la pagina que se tiene
     */
    /**
     * Duplica una nueva vigencia localizacion
     */
    public void duplicarTercero() {
        if (terceroTablaSeleccionado != null) {
            duplicarTercero = new Terceros();
            duplicarTercero.setNombre(terceroTablaSeleccionado.getNombre());
            duplicarTercero.setNit(terceroTablaSeleccionado.getNit());
            duplicarTercero.setDigitoverificacion(terceroTablaSeleccionado.getDigitoverificacion());
            duplicarTercero.setNitalternativo(terceroTablaSeleccionado.getNitalternativo());
            duplicarTercero.setCodigoss(terceroTablaSeleccionado.getCodigoss());
            duplicarTercero.setCodigosp(terceroTablaSeleccionado.getCodigosp());
            duplicarTercero.setCodigosc(terceroTablaSeleccionado.getCodigosc());
            duplicarTercero.setTerceroconsolidador(terceroTablaSeleccionado.getTerceroconsolidador());
            duplicarTercero.setCiudad(terceroTablaSeleccionado.getCiudad());
            duplicarTercero.setCodigoalternativo(terceroTablaSeleccionado.getCodigoalternativo());
            duplicarTercero.setTiponit(terceroTablaSeleccionado.getTiponit());
            duplicarTercero.setCodigotercerosap(terceroTablaSeleccionado.getCodigotercerosap());

            if (duplicarTercero.getTerceroconsolidador() == null) {
                duplicarTercero.setTerceroconsolidador(new Terceros());
            }
            if (duplicarTercero.getCiudad() == null) {
                duplicarTercero.setCiudad(new Ciudades());
            }
            RequestContext context = RequestContext.getCurrentInstance();
            PrimefacesContextUI.actualizar("formularioDialogos:DuplicarRegistroTercero");
            PrimefacesContextUI.ejecutar("PF('DuplicarRegistroTercero').show()");
        }
    }

    /**
     * Metodo que confirma el duplicado y actualiza los datos de la tabla
     * VigenciasLocalizaciones
     */
    public void confirmarDuplicar() {
        RequestContext context = RequestContext.getCurrentInstance();
        activarLOV = true;
        PrimefacesContextUI.actualizar("form:listaValores");
        if (validarCamposNulosTercero(2) == true) {
            if (bandera == 1) {
                cerrarFiltradoTercero();
            }
            if (banderaTS == 1) {
                cerrarFiltradoTerceroSucursal();
            }
            k++;
            BigInteger var = BigInteger.valueOf(k);
            duplicarTercero.setSecuencia(var);
            duplicarTercero.setEmpresa(empresaActual);
            listTerceroCrear.add(duplicarTercero);
            listTerceros.add(duplicarTercero);
            terceroTablaSeleccionado = listTerceros.get(listTerceros.indexOf(duplicarTercero));
            modificarInfoRegistro(listTerceros.size());
            duplicarTercero = new Terceros();
            PrimefacesContextUI.actualizar("form:datosTerceros");
            if (guardado) {
                guardado = false;
                PrimefacesContextUI.actualizar("form:ACEPTAR");
            }
            PrimefacesContextUI.ejecutar("PF('DuplicarRegistroTercero').hide()");
            cambiosTercero = true;
        } else {
            PrimefacesContextUI.ejecutar("PF('errorDatosNullTerceros').show()");
        }
    }

    public void limpiarDuplicarTercero() {
        duplicarTercero = new Terceros();
        duplicarTercero.setTerceroconsolidador(new Terceros());
        duplicarTercero.setCiudad(new Ciudades());
    }

    ////-- !! --- !! --///
    public void duplicarTerceroS() {
        if (terceroSucursalTablaSeleccionado != null) {
            duplicarTerceroSucursal = new TercerosSucursales();
            duplicarTerceroSucursal.setCodigosucursal(terceroSucursalTablaSeleccionado.getCodigosucursal());
            duplicarTerceroSucursal.setCodigopatronal(terceroSucursalTablaSeleccionado.getCodigopatronal());
            duplicarTerceroSucursal.setDescripcion(terceroSucursalTablaSeleccionado.getDescripcion());
            duplicarTerceroSucursal.setCiudad(terceroSucursalTablaSeleccionado.getCiudad());

            if (duplicarTerceroSucursal.getCiudad() == null) {
                duplicarTerceroSucursal.setCiudad(new Ciudades());
            }
            RequestContext context = RequestContext.getCurrentInstance();
            PrimefacesContextUI.actualizar("formularioDialogos:DuplicarRegistroTerceroSucursal");
            PrimefacesContextUI.ejecutar("PF('DuplicarRegistroTerceroSucursal').show()");
        }
    }

    /**
     * Metodo que confirma el duplicado y actualiza los datos de la tabla
     * VigenciasLocalizaciones
     */
    public void confirmarDuplicarTS() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (validarCamposNulosTerceroSucursal(2) == true) {
            if (bandera == 1) {
                cerrarFiltradoTercero();
            }
            if (banderaTS == 1) {
                cerrarFiltradoTerceroSucursal();
            }
            k++;
            BigInteger var = BigInteger.valueOf(k);
            duplicarTerceroSucursal.setSecuencia(var);
            duplicarTerceroSucursal.setTercero(terceroTablaSeleccionado);

            listTerceroSucursalCrear.add(duplicarTerceroSucursal);
            listTercerosSucursales.add(duplicarTerceroSucursal);
            terceroSucursalTablaSeleccionado = listTercerosSucursales.get(listTercerosSucursales.indexOf(duplicarTerceroSucursal));
            duplicarTerceroSucursal = new TercerosSucursales();
            PrimefacesContextUI.actualizar("form:datosTercerosSucursales");
            if (guardado) {
                guardado = false;
                PrimefacesContextUI.actualizar("form:ACEPTAR");
            }
            PrimefacesContextUI.ejecutar("PF('DuplicarRegistroTerceroSucursal').hide()");
            cambiosTerceroSucursal = true;
        } else {
            PrimefacesContextUI.ejecutar("PF('errorDatosNullSucursal').show()");
        }
    }

    public void limpiarDuplicarTerceroSucursal() {
        duplicarTerceroSucursal = new TercerosSucursales();
        duplicarTerceroSucursal.setCiudad(new Ciudades());
    }

    ///////////////////////////////////////////////////////////////
    /**
     * Valida que registro se elimina de que tabla con respecto a la posicion en
     * la pagina
     */
    public void borrarTercero() {
        System.err.println("Entre a borrarTercero()");
        if (terceroTablaSeleccionado != null) {
            if (!listTerceroModificar.isEmpty() && listTerceroModificar.contains(terceroTablaSeleccionado)) {
                int modIndex = listTerceroModificar.indexOf(terceroTablaSeleccionado);
                listTerceroModificar.remove(modIndex);
                listTerceroBorrar.add(terceroTablaSeleccionado);
            } else if (!listTerceroCrear.isEmpty() && listTerceroCrear.contains(terceroTablaSeleccionado)) {
                int crearIndex = listTerceroCrear.indexOf(terceroTablaSeleccionado);
                listTerceroCrear.remove(crearIndex);
            } else {
                listTerceroBorrar.add(terceroTablaSeleccionado);
            }
            listTerceros.remove(terceroTablaSeleccionado);
            if (tipoLista == 1) {
                filtrarListTercero.remove(terceroTablaSeleccionado);
            }
            modificarInfoRegistro(listTerceros.size());
            RequestContext context = RequestContext.getCurrentInstance();
            PrimefacesContextUI.actualizar("form:datosTerceros");

            cambiosTercero = true;
            if (guardado) {
                guardado = false;
                PrimefacesContextUI.actualizar("form:ACEPTAR");
            }
        }
    }

    ////--- !! --- !!! ///
    public void borrarTerceroSucursal() {
        if (terceroSucursalTablaSeleccionado != null) {
            if (!listTerceroSucursalModificar.isEmpty() && listTerceroSucursalModificar.contains(terceroSucursalTablaSeleccionado)) {
                int modIndex = listTerceroSucursalModificar.indexOf(terceroSucursalTablaSeleccionado);
                listTerceroSucursalModificar.remove(modIndex);
                listTerceroSucursalBorrar.add(terceroSucursalTablaSeleccionado);
            } else if (!listTerceroSucursalCrear.isEmpty() && listTerceroSucursalCrear.contains(terceroSucursalTablaSeleccionado)) {
                int crearIndex = listTerceroSucursalCrear.indexOf(terceroSucursalTablaSeleccionado);
                listTerceroSucursalCrear.remove(crearIndex);
            } else {
                listTerceroSucursalBorrar.add(terceroSucursalTablaSeleccionado);
            }
            listTercerosSucursales.remove(terceroSucursalTablaSeleccionado);
            if (tipoLista == 1) {
                filtrarListTercerosSucursales.remove(terceroSucursalTablaSeleccionado);
            }
            RequestContext context = RequestContext.getCurrentInstance();
            modificarInfoRegistroTS(listTercerosSucursales.size());
            PrimefacesContextUI.actualizar("form:infoRegistroTerceroSucursal");
            PrimefacesContextUI.actualizar("form:datosTercerosSucursales");

            activarLOV = true;
            PrimefacesContextUI.actualizar("form:listaValores");
            cambiosTerceroSucursal = true;
            if (guardado) {
                guardado = false;
                PrimefacesContextUI.actualizar("form:ACEPTAR");
            }
        }
    }

    //CTRL + F11 ACTIVAR/DESACTIVAR
    /**
     * Metodo que activa el filtrado por medio de la opcion en el toolbar o por
     * medio de la tecla Crtl+F11
     */
    public void activarCtrlF11() {
        System.err.println("Entre a activarCtrlF11");
        if (terceroTablaSeleccionado != null) {
            //System.err.println("filtradoTercero");
            filtradoTercero();
        }
        if (terceroSucursalTablaSeleccionado != null) {
            filtradoTerceroSucursal();
        }
    }

    /**
     * Metodo que acciona el filtrado de la tabla vigencia localizacion
     */
    public void filtradoTercero() {
        if (terceroTablaSeleccionado != null) {
            System.err.println("Entre a filtradoTercero()");
            FacesContext c = FacesContext.getCurrentInstance();
            if (bandera == 0) {
                altoTablaTercero = "130";
                terceroNombre = (Column) c.getViewRoot().findComponent("form:datosTerceros:terceroNombre");
                terceroNombre.setFilterStyle("width: 85%;");
                terceroNIT = (Column) c.getViewRoot().findComponent("form:datosTerceros:terceroNIT");
                terceroNIT.setFilterStyle("width: 85%;");
                terceroDigitoVerificacion = (Column) c.getViewRoot().findComponent("form:datosTerceros:terceroDigitoVerificacion");
                terceroDigitoVerificacion.setFilterStyle("width: 85%;");
                terceroNITAlternativo = (Column) c.getViewRoot().findComponent("form:datosTerceros:terceroNITAlternativo");
                terceroNITAlternativo.setFilterStyle("width: 85%;");
                terceroCodigoSS = (Column) c.getViewRoot().findComponent("form:datosTerceros:terceroCodigoSS");
                terceroCodigoSS.setFilterStyle("width: 85%;");
                terceroCodigoSP = (Column) c.getViewRoot().findComponent("form:datosTerceros:terceroCodigoSP");
                terceroCodigoSP.setFilterStyle("width: 85%;");
                terceroCodigoSC = (Column) c.getViewRoot().findComponent("form:datosTerceros:terceroCodigoSC");
                terceroCodigoSC.setFilterStyle("width: 85%;");
                terceroTConsolidador = (Column) c.getViewRoot().findComponent("form:datosTerceros:terceroTConsolidador");
                terceroTConsolidador.setFilterStyle("width: 85%;");
                terceroNITConsolidado = (Column) c.getViewRoot().findComponent("form:datosTerceros:terceroNITConsolidado");
                terceroNITConsolidado.setFilterStyle("width: 85%;");
                terceroCiudad = (Column) c.getViewRoot().findComponent("form:datosTerceros:terceroCiudad");
                terceroCiudad.setFilterStyle("width: 85%;");
                terceroCodigoAlterno = (Column) c.getViewRoot().findComponent("form:datosTerceros:terceroCiudad");
                terceroCodigoAlterno.setFilterStyle("width: 85%;");
                PrimefacesContextUI.actualizar("form:datosTerceros");
                bandera = 1;
            } else if (bandera == 1) {
                cerrarFiltradoTercero();
            }
        }
    }

    public void filtradoTerceroSucursal() {
        if (terceroSucursalTablaSeleccionado != null) {
            FacesContext c = FacesContext.getCurrentInstance();
            if (banderaTS == 0) {
                altoTablaSucursal = "50";
                terceroSucursalCodigo = (Column) c.getViewRoot().findComponent("form:datosTercerosSucursales:terceroSucursalCodigo");
                terceroSucursalCodigo.setFilterStyle("width: 85%;");
                terceroSucursalPatronal = (Column) c.getViewRoot().findComponent("form:datosTercerosSucursales:terceroSucursalPatronal");
                terceroSucursalPatronal.setFilterStyle("width: 85%;");
                terceroSucursalObservacion = (Column) c.getViewRoot().findComponent("form:datosTercerosSucursales:terceroSucursalObservacion");
                terceroSucursalObservacion.setFilterStyle("width: 85%;");
                terceroSucursalCiudad = (Column) c.getViewRoot().findComponent("form:datosTercerosSucursales:terceroSucursalCiudad");
                terceroSucursalCiudad.setFilterStyle("width: 85%;");
                PrimefacesContextUI.actualizar("form:datosTercerosSucursales");
                banderaTS = 1;
            } else if (banderaTS == 1) {
                cerrarFiltradoTerceroSucursal();
            }
        }
    }
    //SALIR

    /**
     * Metodo que cierra la sesion y limpia los datos en la pagina
     */
    public void salir() {
        if (bandera == 1) {
            cerrarFiltradoTercero();
        }

        if (banderaTS == 1) {
            cerrarFiltradoTerceroSucursal();
        }
        activarLOV = true;
        PrimefacesContextUI.actualizar("form:listaValores");
        listTerceroBorrar.clear();
        listTerceroCrear.clear();
        listTerceroModificar.clear();
        listTerceroSucursalModificar.clear();
        listTerceroSucursalCrear.clear();
        listTerceroSucursalBorrar.clear();
        terceroTablaSeleccionado = null;
        terceroSucursalTablaSeleccionado = null;
        k = 0;
        listTerceros = null;
        listTercerosSucursales = null;
        guardado = true;
        cambiosTercero = true;
        cambiosTerceroSucursal = true;
        PrimefacesContextUI.actualizar("form:ACEPTAR");
    }
    //ASIGNAR INDEX PARA DIALOGOS COMUNES (LDN = LISTA - NUEVO - DUPLICADO) (list = ESTRUCTURAS - MOTIVOSLOCALIZACIONES - PROYECTOS)

    /**
     * Metodo que ejecuta los dialogos de estructuras, motivos localizaciones,
     * proyectos
     *
     * @param indice Fila de la tabla
     * @param dlg Dialogo
     * @param LND Tipo actualizacion = LISTA - NUEVO - DUPLICADO
     * @param tt Tipo Tabla : VigenciaLocalizacion / VigenciaProrrateo /
     * VigenciaProrrateoProyecto
     */
    public void asignarIndex(Object elemento, int dlg, int LND, int tt) {
        RequestContext context = RequestContext.getCurrentInstance();
        activarLOV = true;
        PrimefacesContextUI.actualizar("form:listaValores");
        if (tt == 0) {
            if (LND == 0) {
                terceroTablaSeleccionado = (Terceros) elemento;
                tipoActualizacion = 0;
            } else if (LND == 1) {
                tipoActualizacion = 1;
            } else if (LND == 2) {
                tipoActualizacion = 2;
            }
            if (dlg == 0) {
                contarRegistrosTerceroC();
                terceroCSeleccionado = null;
                activarLOV = false;
                PrimefacesContextUI.actualizar("form:listaValores");
                PrimefacesContextUI.actualizar("form:TerceroDialogo");
                PrimefacesContextUI.ejecutar("PF('TerceroDialogo').show()");
            } else if (dlg == 1) {
                cancelarCambioCiudad();
                ciudadSeleccionada = null;
                activarLOV = false;
                PrimefacesContextUI.actualizar("form:listaValores");
                PrimefacesContextUI.actualizar("form:CiudadDialogo");
                PrimefacesContextUI.ejecutar("PF('CiudadDialogo').show()");
            }
        }
        if (tt == 1) {
            terceroSucursalTablaSeleccionado = (TercerosSucursales) elemento;
            if (LND == 0) {
                tipoActualizacion = 0;
            } else if (LND == 1) {
                tipoActualizacion = 1;
            } else if (LND == 2) {
                tipoActualizacion = 2;
            }
            if (dlg == 0) {
                contarRegistrosCiudadTS();
                ciudadSeleccionada = null;
                activarLOV = false;
                PrimefacesContextUI.actualizar("form:listaValores");
                PrimefacesContextUI.actualizar("form:CiudadTSDialogo");
                PrimefacesContextUI.ejecutar("PF('CiudadTSDialogo').show()");
            }
        }
        contarRegistrosTS();
        PrimefacesContextUI.actualizar("form:infoRegistroTerceroSucursal");
    }

    //LISTA DE VALORES DINAMICA
    /**
     * Metodo que activa la lista de valores de todas las tablas con respecto al
     * index activo y la columna activa
     */
    public void listaValoresBoton() {
        RequestContext context = RequestContext.getCurrentInstance();
        activarLOV = true;
        PrimefacesContextUI.actualizar("form:listaValores");
        if (terceroTablaSeleccionado != null) {
            if (cualCelda == 7) {
                contarRegistrosTerceroC();
                terceroCSeleccionado = null;
                activarLOV = false;
                PrimefacesContextUI.actualizar("form:listaValores");
                PrimefacesContextUI.actualizar("form:TerceroDialogo");
                PrimefacesContextUI.ejecutar("PF('TerceroDialogo').show()");
                tipoActualizacion = 0;
            }
            if (cualCelda == 9) {
                contarRegistrosCiudadT();
                ciudadSeleccionada = null;
                activarLOV = false;
                PrimefacesContextUI.actualizar("form:listaValores");
                PrimefacesContextUI.actualizar("form:CiudadDialogo");
                PrimefacesContextUI.ejecutar("PF('CiudadDialogo').show()");
                tipoActualizacion = 0;
            }
        }
        if (terceroSucursalTablaSeleccionado != null) {
            if (cualCeldaTS == 3) {
                contarRegistrosCiudadTS();
                ciudadSeleccionada = null;
                activarLOV = false;
                PrimefacesContextUI.actualizar("form:listaValores");
                PrimefacesContextUI.actualizar("form:CiudadTSDialogo");
                PrimefacesContextUI.ejecutar("PF('CiudadTSDialogo').show()");
                tipoActualizacion = 0;
            }
        }

    }

    /**
     * Valida un proceso de nuevo registro dentro de la pagina con respecto a la
     * posicion en la pagina
     */
    /**
     * Metodo que activa el boton aceptar de la pagina y los dialogos
     */
    public void valoresBackupAutocompletar(int tipoNuevo, String Campo, int tipoLista) {
        if (Campo.equals("TERCEROCONSOLIDADOR")) {
            if (tipoNuevo == 1) {
                terceroConsolidador = nuevoTercero.getTerceroconsolidador().getNombre();
            } else if (tipoNuevo == 2) {
                terceroConsolidador = duplicarTercero.getTerceroconsolidador().getNombre();
            }
        } else if (Campo.equals("CIUDAD")) {
            if (tipoNuevo == 1) {
                ciudad = nuevoTercero.getCiudad().getNombre();
            } else if (tipoNuevo == 2) {
                ciudad = duplicarTercero.getCiudad().getNombre();
            }
        }

    }

    public void autocompletarNuevoyDuplicadoTercero(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("TERCEROCONSOLIDADOR")) {
            if (!valorConfirmar.isEmpty()) {
                if (tipoNuevo == 1) {
                    nuevoTercero.getTerceroconsolidador().setNombre(terceroConsolidador);
                } else if (tipoNuevo == 2) {
                    duplicarTercero.getTerceroconsolidador().setNombre(terceroConsolidador);
                }
                for (int i = 0; i < listTerceroConsolidador.size(); i++) {
                    if (listTerceroConsolidador.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    if (tipoNuevo == 1) {
                        nuevoTercero.setTerceroconsolidador(listTerceroConsolidador.get(indiceUnicoElemento));
                        PrimefacesContextUI.actualizar("formularioDialogos:nuevaTerceroCT");
                    } else if (tipoNuevo == 2) {
                        duplicarTercero.setTerceroconsolidador(listTerceroConsolidador.get(indiceUnicoElemento));
                        PrimefacesContextUI.actualizar("formularioDialogos:duplicarTerceroCT");
                    }
                    listTerceroConsolidador.clear();
                    getListTerceroConsolidador();
                } else {
                    PrimefacesContextUI.actualizar("form:TerceroDialogo");
                    PrimefacesContextUI.ejecutar("PF('TerceroDialogo').show()");
                    tipoActualizacion = tipoNuevo;
                    if (tipoNuevo == 1) {
                        PrimefacesContextUI.actualizar("formularioDialogos:nuevaTerceroCT");
                    } else if (tipoNuevo == 2) {
                        PrimefacesContextUI.actualizar("formularioDialogos:duplicarTerceroCT");
                    }
                }
            } else {
                if (tipoNuevo == 1) {
                    nuevoTercero.setTerceroconsolidador(new Terceros());
                    PrimefacesContextUI.actualizar("formularioDialogos:nuevaTerceroCT");
                } else if (tipoNuevo == 2) {
                    duplicarTercero.setTerceroconsolidador(new Terceros());
                    PrimefacesContextUI.actualizar("formularioDialogos:duplicarTerceroCT");
                }
                listTerceroConsolidador.clear();
                getListTerceroConsolidador();
            }
        } else if (confirmarCambio.equalsIgnoreCase("CIUDAD")) {
            if (!valorConfirmar.isEmpty()) {
                if (tipoNuevo == 1) {
                    nuevoTercero.getCiudad().setNombre(ciudad);
                } else if (tipoNuevo == 2) {
                    duplicarTercero.getCiudad().setNombre(ciudad);
                }
                for (int i = 0; i < listCiudades.size(); i++) {
                    if (listCiudades.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    if (tipoNuevo == 1) {
                        nuevoTercero.setCiudad(listCiudades.get(indiceUnicoElemento));
                        PrimefacesContextUI.actualizar("formularioDialogos:nuevaCodigoAT");
                    } else if (tipoNuevo == 2) {
                        duplicarTercero.setCiudad(listCiudades.get(indiceUnicoElemento));
                        PrimefacesContextUI.actualizar("formularioDialogos:duplicarCodigoAT");
                    }
                    listCiudades.clear();
                    getListCiudades();
                } else {
                    PrimefacesContextUI.actualizar("form:CiudadDialogo");
                    PrimefacesContextUI.ejecutar("PF('CiudadDialogo').show()");
                    tipoActualizacion = tipoNuevo;
                    if (tipoNuevo == 1) {
                        PrimefacesContextUI.actualizar("formularioDialogos:nuevaCodigoAT");
                    } else if (tipoNuevo == 2) {
                        PrimefacesContextUI.actualizar("formularioDialogos:duplicarCodigoAT");
                    }
                }
            } else {
                if (tipoNuevo == 1) {
                    nuevoTercero.setCiudad(new Ciudades());
                    PrimefacesContextUI.actualizar("formularioDialogos:nuevaCodigoAT");
                } else if (tipoNuevo == 2) {
                    duplicarTercero.setCiudad(new Ciudades());
                    PrimefacesContextUI.actualizar("formularioDialogos:duplicarCodigoAT");
                }
                listCiudades.clear();
                getListCiudades();
            }
        }
    }

    public void autocompletarNuevoyDuplicadoTerceroSucursal(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("CIUDAD")) {
            if (!valorConfirmar.isEmpty()) {
                if (tipoNuevo == 1) {
                    nuevoTerceroSucursal.getCiudad().setNombre(ciudadTS);
                } else if (tipoNuevo == 2) {
                    duplicarTerceroSucursal.getCiudad().setNombre(ciudadTS);
                }
                for (int i = 0; i < listCiudades.size(); i++) {
                    if (listCiudades.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    if (tipoNuevo == 1) {
                        nuevoTerceroSucursal.setCiudad(listCiudades.get(indiceUnicoElemento));
                        PrimefacesContextUI.actualizar("formularioDialogos:nuevaCiudadTS");
                    } else if (tipoNuevo == 2) {
                        duplicarTerceroSucursal.setCiudad(listCiudades.get(indiceUnicoElemento));
                        PrimefacesContextUI.actualizar("formularioDialogos:duplicarCiudadTS");
                    }
                    listCiudades.clear();
                    getListCiudades();
                } else {
                    PrimefacesContextUI.actualizar("form:CiudadTSDialogo");
                    PrimefacesContextUI.ejecutar("PF('CiudadTSDialogo').show()");
                    tipoActualizacion = tipoNuevo;
                    if (tipoNuevo == 1) {
                        PrimefacesContextUI.actualizar("formularioDialogos:nuevaCiudadTS");
                    } else if (tipoNuevo == 2) {
                        PrimefacesContextUI.actualizar("formularioDialogos:duplicarCiudadTS");
                    }
                }
            } else {
                if (tipoNuevo == 1) {
                    nuevoTerceroSucursal.setCiudad(new Ciudades());
                    PrimefacesContextUI.actualizar("formularioDialogos:nuevaCiudadTS");
                } else if (tipoNuevo == 2) {
                    duplicarTerceroSucursal.setCiudad(new Ciudades());
                    PrimefacesContextUI.actualizar("formularioDialogos:duplicarCiudadTS");
                }
                listCiudades.clear();
                getListCiudades();
            }
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void actualizarTerceros() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            terceroTablaSeleccionado.setTerceroconsolidador(terceroCSeleccionado);
            if (!listTerceroCrear.contains(terceroTablaSeleccionado)) {
                if (listTerceroModificar.isEmpty()) {
                    listTerceroModificar.add(terceroTablaSeleccionado);
                } else if (!listTerceroModificar.contains(terceroTablaSeleccionado)) {
                    listTerceroModificar.add(terceroTablaSeleccionado);
                }
            }

            if (guardado) {
                guardado = false;
                PrimefacesContextUI.actualizar("form:ACEPTAR");
            }
            cambiosTercero = true;
            permitirIndex = true;

            PrimefacesContextUI.actualizar("form:datosTerceros");
        } else if (tipoActualizacion == 1) {
            nuevoTercero.setTerceroconsolidador(terceroCSeleccionado);
            PrimefacesContextUI.actualizar("formularioDialogos:nuevaTerceroCT");
            PrimefacesContextUI.actualizar("formularioDialogos:nuevaTerceroCNITT");
        } else if (tipoActualizacion == 2) {
            duplicarTercero.setTerceroconsolidador(terceroCSeleccionado);
            PrimefacesContextUI.actualizar("formularioDialogos:duplicarTTerceroCT");
            PrimefacesContextUI.actualizar("formularioDialogos:duplicarTTerceroCNITT");
        }
        filtrarListTerceroConsolidador = null;
        terceroCSeleccionado = new Terceros();
        aceptar = true;
        tipoActualizacion = -1;
        /*
         * PrimefacesContextUI.actualizar("form:TerceroDialogo");
         * PrimefacesContextUI.actualizar("form:lovTercero"); PrimefacesContextUI.actualizar("form:aceptarT");
         */
        context.reset("form:lovTercero:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovTercero').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('TerceroDialogo').hide()");

    }

    public void cancelarCambioTerceros() {
        filtrarListTerceroConsolidador = null;
        terceroCSeleccionado = new Terceros();
        aceptar = true;
        terceroTablaSeleccionado = null;
        tipoActualizacion = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovTercero:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovTercero').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('TerceroDialogo').hide()");
    }

    public void actualizarCiudad() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            terceroTablaSeleccionado.setCiudad(ciudadSeleccionada);
            if (!listTerceroCrear.contains(terceroTablaSeleccionado)) {
                if (listTerceroModificar.isEmpty()) {
                    listTerceroModificar.add(terceroTablaSeleccionado);
                } else if (!listTerceroModificar.contains(terceroTablaSeleccionado)) {
                    listTerceroModificar.add(terceroTablaSeleccionado);
                }
            }

            if (guardado) {
                guardado = false;
                PrimefacesContextUI.actualizar("form:ACEPTAR");
            }
            cambiosTercero = true;
            permitirIndex = true;

            PrimefacesContextUI.actualizar("form:datosTerceros");
        } else if (tipoActualizacion == 1) {
            nuevoTercero.setCiudad(ciudadSeleccionada);
            PrimefacesContextUI.actualizar("formularioDialogos:nuevaCiudadT");
        } else if (tipoActualizacion == 2) {
            duplicarTercero.setCiudad(ciudadSeleccionada);
            PrimefacesContextUI.actualizar("formularioDialogos:duplicarTCiudadT");
        }
        filtrarListCiudades = null;
        ciudadSeleccionada = null;
        aceptar = true;
        tipoActualizacion = -1;
//        
//          PrimefacesContextUI.actualizar("form:CiudadDialogo");
//          PrimefacesContextUI.actualizar("form:lovCiudad"); 
//          PrimefacesContextUI.actualizar("form:aceptarC");

        context.reset("form:lovCiudad:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovCiudad').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('CiudadDialogo').hide()");
    }

    public void cancelarCambioCiudad() {
        filtrarListCiudades = null;
        ciudadSeleccionada = new Ciudades();
        aceptar = true;
        tipoActualizacion = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovCiudad:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovCiudad').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('CiudadDialogo').hide()");
    }

    public void actualizarCiudadTS() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            terceroSucursalTablaSeleccionado.setCiudad(ciudadSeleccionada);
            if (!listTerceroSucursalCrear.contains(terceroSucursalTablaSeleccionado)) {
                if (listTerceroSucursalModificar.isEmpty()) {
                    listTerceroSucursalModificar.add(terceroSucursalTablaSeleccionado);
                } else if (!listTerceroSucursalModificar.contains(terceroSucursalTablaSeleccionado)) {
                    listTerceroSucursalModificar.add(terceroSucursalTablaSeleccionado);
                }
            }

            if (guardado) {
                guardado = false;
                PrimefacesContextUI.actualizar("form:ACEPTAR");
            }
            cambiosTerceroSucursal = true;
            permitirIndexTS = true;
            PrimefacesContextUI.actualizar("form:datosTercerosSucursales");
        } else if (tipoActualizacion == 1) {
            nuevoTerceroSucursal.setCiudad(ciudadSeleccionada);
            PrimefacesContextUI.actualizar("formularioDialogos:nuevaCiudadTS");
        } else if (tipoActualizacion == 2) {
            duplicarTerceroSucursal.setCiudad(ciudadSeleccionada);
            PrimefacesContextUI.actualizar("formularioDialogos:duplicarCiudadTS");
        }
        filtrarListCiudades = null;
        ciudadSeleccionada = new Ciudades();
        aceptar = true;
        tipoActualizacion = -1;

        PrimefacesContextUI.actualizar("form:CiudadTSDialogo");
        PrimefacesContextUI.actualizar("form:lovCiudadTS");
        PrimefacesContextUI.actualizar("form:aceptarCTS");

        context.reset("form:lovCiudadTS:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovCiudadTS').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('CiudadTSDialogo').hide()");
    }

    public void cancelarCambioCiudadTS() {
        filtrarListCiudades = null;
        ciudadSeleccionada = new Ciudades();
        aceptar = true;
        tipoActualizacion = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovCiudadTS:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovCiudadTS').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('CiudadTSDialogo').hide()");
    }

    /**
     * Selecciona la tabla a exportar XML con respecto al index activo
     *
     * @return Nombre del dialogo a exportar en XML
     */
    public String exportXML() {
        if (terceroSucursalTablaSeleccionado != null) {
            nombreTabla = ":formExportarTercerosSucursales:datosTercerosSucursalesExportar";
            nombreXML = "TercerosSucursalesXML";
        } else if (terceroTablaSeleccionado != null) {
            nombreTabla = ":formExportarTerceros:datosTercerosExportar";
            nombreXML = "TercerosXML";
        }
        return nombreTabla;
    }

    /**
     * Valida la tabla a exportar en PDF con respecto al index activo
     *
     * @throws IOException Excepcion de In-Out de datos
     */
    public void validarExportPDF() throws IOException {
        if (terceroSucursalTablaSeleccionado != null) {
            exportPDFTS();
        } else if (terceroTablaSeleccionado != null) {
            exportPDFT();
        }
    }

    /**
     * Metodo que exporta datos a PDF Vigencia Localizacion
     *
     * @throws IOException Excepcion de In-Out de datos
     */
    public void exportPDFT() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarTerceros:datosTercerosExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "TercerosPDF", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    /**
     * Metodo que exporta datos a PDF Vigencia Prorrateo
     *
     * @throws IOException Excepcion de In-Out de datos
     */
    public void exportPDFTS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarTercerosSucursales:datosTercerosSucursalesExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "TercerosSucursalesPDF", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    /**
     * Verifica que tabla exportar XLS con respecto al index activo
     *
     * @throws IOException
     */
    public void verificarExportXLS() throws IOException {
        if (terceroSucursalTablaSeleccionado != null) {
            exportXLSTS();
        } else if (terceroTablaSeleccionado != null) {
            exportXLST();
        }
    }

    /**
     * Metodo que exporta datos a XLS Vigencia Sueldos
     *
     * @throws IOException Excepcion de In-Out de datos
     */
    public void exportXLST() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarTerceros:datosTercerosExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "TercerosXLS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    /**
     * Metodo que exporta datos a XLS Vigencia Afiliaciones
     *
     * @throws IOException Excepcion de In-Out de datos
     */
    public void exportXLSTS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarTercerosSucursales:datosTercerosSucursalesExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "TercerosSucursalesXLS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void dialogoSeleccionarTercero() {
        terceroLOVSeleccionado = null;
        getFiltrarListTerceroLOV();
        getListTerceros();
        RequestContext context = RequestContext.getCurrentInstance();
        PrimefacesContextUI.actualizar("form:BuscarTerceroDialogo");
        PrimefacesContextUI.ejecutar("PF('BuscarTerceroDialogo').show()");
    }

    public void cancelarSeleccionTercero() {
        terceroCSeleccionado = new Terceros();
        terceroLOVSeleccionado = null;
        filtrarListTerceroConsolidador = null;
        aceptar = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovBuscarTercero:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovBuscarTercero').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('BuscarTerceroDialogo').hide()");
    }

    public void validarSeleccionTercero() {
        RequestContext context = RequestContext.getCurrentInstance();
        terceroTablaSeleccionado = terceroLOVSeleccionado;
        if (cambiosTercero == false && cambiosTerceroSucursal == false) {
            listTerceros = null;
            listTerceros = new ArrayList<Terceros>();
            if (terceroLOVSeleccionado.getCiudad() == null) {
                terceroLOVSeleccionado.setCiudad(new Ciudades());
            }
            if (terceroLOVSeleccionado.getTerceroconsolidador() == null) {
                terceroLOVSeleccionado.setTerceroconsolidador(new Terceros());
            }
            listTerceros.add(terceroLOVSeleccionado);
//            terceroLOVSeleccionado = new Terceros();
//            filtrarListTerceroLOV = null;
            listTercerosLOV.clear();
            contarRegistrosT();
//            getListTercerosLOV();
            PrimefacesContextUI.actualizar("form:datosTerceros");
            listTercerosSucursales = null;
            getListTercerosSucursales();
            contarRegistrosTS();
            PrimefacesContextUI.actualizar("form:datosTercerosSucursales");
            context.reset("form:lovBuscarTercero:globalFilter");
            PrimefacesContextUI.ejecutar("PF('lovBuscarTercero').clearFilters()");
            PrimefacesContextUI.ejecutar("PF('BuscarTerceroDialogo').hide()");
        } else {
            terceroLOVSeleccionado = null;
            filtrarListTerceroLOV = null;
            PrimefacesContextUI.ejecutar("PF('confirmarGuardar').show()");
        }
        aceptar = true;
    }

    public void mostrarTodos() {
        System.out.println("ControlTercero.mostrarTodos()");
        RequestContext context = RequestContext.getCurrentInstance();
        if (cambiosTercero == false && cambiosTerceroSucursal == false) {
            listTerceros = null;
            getListTerceros();
            PrimefacesContextUI.actualizar("form:datosTerceros");
            getListTercerosSucursales();
            PrimefacesContextUI.actualizar("form:datosTercerosSucursales");
        } else {
            PrimefacesContextUI.ejecutar("PF('confirmarGuardar').show()");
        }
        contarRegistrosT();
        contarRegistrosTS();
    }

    //METODO RASTROS PARA LAS TABLAS EN EMPLVIGENCIASUELDOS
    public void verificarRastroTabla() {
        //Cuando no se ha seleccionado ningun registro:
        if (terceroTablaSeleccionado == null && terceroSucursalTablaSeleccionado == null) {
            //Dialogo para seleccionar el rastro Historico de la tabla deseada
            PrimefacesContextUI.ejecutar("PF('verificarRastrosTablas').show()");
        } else {
            //Cuando se selecciono registro:            
            if (terceroSucursalTablaSeleccionado != null) {
                verificarRastroTercerosSucursales();
            } else if (terceroTablaSeleccionado != null) {
                verificarRastroTerceros();
            }
        }
    }

    //Verificar Rastro Vigencia Terceros
    public void verificarRastroTerceros() {
        RequestContext context = RequestContext.getCurrentInstance();
        int resultado = administrarRastros.obtenerTabla(terceroTablaSeleccionado.getSecuencia(), "TERCEROS");
        backUp = terceroTablaSeleccionado.getSecuencia();
        if (resultado == 1) {
            PrimefacesContextUI.ejecutar("PF('errorObjetosDB').show()");
        } else if (resultado == 2) {
            nombreTablaRastro = "Terceros";
            msnConfirmarRastro = "La tabla TERCEROS tiene rastros para el registro seleccionado, ¿desea continuar?";
            PrimefacesContextUI.actualizar("form:msnConfirmarRastro");
            PrimefacesContextUI.ejecutar("PF('confirmarRastro').show()");
        } else if (resultado == 3) {
            PrimefacesContextUI.ejecutar("PF('errorRegistroRastro').show()");
        } else if (resultado == 4) {
            PrimefacesContextUI.ejecutar("PF('errorTablaConRastro').show()");
        } else if (resultado == 5) {
            PrimefacesContextUI.ejecutar("PF('errorTablaSinRastro').show()");
        }
    }

    public void verificarRastroTercerosHist() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (administrarRastros.verificarHistoricosTabla("TERCEROS")) {
            nombreTablaRastro = "Terceros";
            msnConfirmarRastroHistorico = "La tabla TERCEROS tiene rastros historicos, ¿Desea continuar?";
            PrimefacesContextUI.actualizar("form:confirmarRastroHistorico");
            PrimefacesContextUI.ejecutar("PF('confirmarRastroHistorico').show()");
        } else {
            PrimefacesContextUI.ejecutar("PF('errorRastroHistorico').show()");
        }
    }

    public void verificarRastroTercerosSucursales() {
        RequestContext context = RequestContext.getCurrentInstance();
        int resultado = administrarRastros.obtenerTabla(terceroSucursalTablaSeleccionado.getSecuencia(), "TERCEROSSUCURSALES");
        backUp = terceroSucursalTablaSeleccionado.getSecuencia();
        if (resultado == 1) {
            PrimefacesContextUI.ejecutar("PF('errorObjetosDB').show()");
        } else if (resultado == 2) {
            nombreTablaRastro = "TercerosSucursales";
            msnConfirmarRastro = "La tabla TERCEROSSUCURSALES tiene rastros para el registro seleccionado, ¿desea continuar?";
            PrimefacesContextUI.actualizar("form:msnConfirmarRastro");
            PrimefacesContextUI.ejecutar("PF('confirmarRastro').show()");
        } else if (resultado == 3) {
            PrimefacesContextUI.ejecutar("PF('errorRegistroRastro').show()");
        } else if (resultado == 4) {
            PrimefacesContextUI.ejecutar("PF('errorTablaConRastro').show()");
        } else if (resultado == 5) {
            PrimefacesContextUI.ejecutar("PF('errorTablaSinRastro').show()");
        }
    }

    public void verificarRastroTercerosSucursalesHist() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (administrarRastros.verificarHistoricosTabla("TERCEROSSUCURSALES")) {
            nombreTablaRastro = "TercerosSucursales";
            msnConfirmarRastroHistorico = "La tabla TERCEROSSUCURSALES tiene rastros historicos, ¿Desea continuar?";
            PrimefacesContextUI.actualizar("form:confirmarRastroHistorico");
            PrimefacesContextUI.ejecutar("PF('confirmarRastroHistorico').show()");
        } else {
            PrimefacesContextUI.ejecutar("PF('errorRastroHistorico').show()");
        }
    }

    public void lovEmpresas() {
        empresaSeleccionada = null;
        cualCelda = -1;
        cualCeldaTS = -1;
        PrimefacesContextUI.actualizar("formularioDialogos:EmpresasDialogo");
        PrimefacesContextUI.ejecutar("PF('EmpresasDialogo').show()");
    }

    public void actualizarEmpresa() {
        RequestContext context = RequestContext.getCurrentInstance();
//        terceroTablaSeleccionado = null;
//        terceroSucursalTablaSeleccionado = null;
        PrimefacesContextUI.actualizar("form:nombreEmpresa");
        PrimefacesContextUI.actualizar("form:nitEmpresa");
        filtrarListEmpresas = null;
        aceptar = true;
        PrimefacesContextUI.actualizar("formularioDialogos:EmpresasDialogo");
        PrimefacesContextUI.actualizar("formularioDialogos:lovEmpresas");
        PrimefacesContextUI.actualizar("formularioDialogos:aceptarE");
        context.reset("formularioDialogos:lovEmpresas:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovEmpresas').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('EmpresasDialogo').hide()");
        empresaActual = empresaSeleccionada;
        listTerceros = null;
        getListTerceros();
        if (listTerceros != null) {
            if (!listTerceros.isEmpty()) {
                terceroTablaSeleccionado = listTerceros.get(0);
            }
        }
        PrimefacesContextUI.actualizar("form:datosTerceros");
        contarRegistrosT();
        contarRegistrosTS();
        if (cambiosTercero == false && cambiosTerceroSucursal == false) {
            empresaSeleccionada = empresaActual;
            listTerceros = null;
            listTercerosSucursales = null;
            getListTerceros();
            getListTercerosSucursales();
            PrimefacesContextUI.actualizar("form:datosTerceros");
            PrimefacesContextUI.actualizar("form:datosTercerosSucursales");
        } else {
            PrimefacesContextUI.ejecutar("PF('confirmarGuardar').show()");
        }
        // empresaSeleccionada = null;
    }

    public void cancelarCambioEmpresa() {
        RequestContext context = RequestContext.getCurrentInstance();
        cualCelda = -1;
        cualCeldaTS = -1;
        terceroSucursalTablaSeleccionado = null;
        filtrarListEmpresas = null;
        empresaSeleccionada = null;
        context.reset("formularioDialogos:lovEmpresas:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovEmpresas').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('EmpresasDialogo').hide()");
    }

    public void limpiarMSNRastros() {
        msnConfirmarRastro = "";
        msnConfirmarRastroHistorico = "";
        nombreTablaRastro = "";
    }

    //EVENTO FILTRAR
    /**
     * Evento que cambia la lista real a la filtrada
     */
    public void eventoFiltrarT() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
        activarLOV = true;
        PrimefacesContextUI.actualizar("form:listaValores");
        terceroTablaSeleccionado = null;
        modificarInfoRegistro(filtrarListTercero.size());
        PrimefacesContextUI.actualizar("form:infoRegistro");

    }

    public void eventoFiltrarTS() {
        if (tipoListaTS == 0) {
            tipoListaTS = 1;
        }
        activarLOV = true;
        PrimefacesContextUI.actualizar("form:listaValores");
        terceroTablaSeleccionado = null;
        modificarInfoRegistroTS(filtrarListTercerosSucursales.size());
        PrimefacesContextUI.actualizar("form:infoRegistroTerceroSucursal");

    }

    public void eventoFiltrarLovTercero() {
        modificarInfoRegistroLovTercero(filtrarListTerceroLOV.size());
        PrimefacesContextUI.actualizar("form:infoRegistroTercero");
    }

    public void eventoFiltrarTerceroC() {
        modificarInfoRegistroTerceroC(filtrarListTerceroConsolidador.size());
        PrimefacesContextUI.actualizar("form:infoRegistroTerceroConsolidador");
    }

    public void eventoFiltrarCiudadT() {
        modificarInfoRegistroCiudadT(filtrarListCiudades.size());
        PrimefacesContextUI.actualizar("form:infoRegistroCiudad1");
    }

    public void eventoFiltrarCiudadTS() {
        modificarInfoRegistroCiudadTS(filtrarListCiudades.size());
        PrimefacesContextUI.actualizar("form:infoRegistroCiudad2");
    }

    public void eventoFiltrarEmpresa() {
        modificarInfoRegistroEmpresa(filtrarListEmpresas.size());
        PrimefacesContextUI.actualizar("form:infoRegistroEmpresa");
    }

    private void modificarInfoRegistro(int valor) {
        infoRegistroTercero = String.valueOf(valor);
    }

    private void modificarInfoRegistroTS(int valor) {
        infoRegistroTerceroSucursal = String.valueOf(valor);
    }

    private void modificarInfoRegistroLovTercero(int valor) {
        infoRegistroTercero = String.valueOf(valor);
    }

    private void modificarInfoRegistroTerceroC(int valor) {
        infoRegistroTerceroConsolidador = String.valueOf(valor);
    }

    private void modificarInfoRegistroCiudadT(int valor) {
        infoRegistroCiudad1 = String.valueOf(valor);
    }

    private void modificarInfoRegistroCiudadTS(int valor) {
        infoRegistroCiudad2 = String.valueOf(valor);
    }

    private void modificarInfoRegistroEmpresa(int valor) {
        infoRegistroEmpresa = String.valueOf(valor);
    }

    public void contarRegistrosT() {
        if (listTerceros != null) {
            modificarInfoRegistro(listTerceros.size());
            PrimefacesContextUI.actualizar("form:infoRegistro");
        } else {
            modificarInfoRegistro(0);
            PrimefacesContextUI.actualizar("form:infoRegistro");
        }
    }

    public void contarRegistrosTS() {
        if (listTercerosSucursales != null) {
            if (listTercerosSucursales.size() > 0) {
                modificarInfoRegistroTS(listTercerosSucursales.size());
                PrimefacesContextUI.actualizar("form:infoRegistroTerceroSucursal");
            } else {
                modificarInfoRegistroTS(0);
                PrimefacesContextUI.actualizar("form:infoRegistroTerceroSucursal");
            }
        } else {
            modificarInfoRegistroTS(0);
            PrimefacesContextUI.actualizar("form:infoRegistroTerceroSucursal");
        }
    }

    public void contarRegistrosLovTercero() {
        if (listTercerosLOV != null) {
            if (listTercerosLOV.size() > 0) {
                modificarInfoRegistroLovTercero(listTercerosLOV.size());
            } else {
                modificarInfoRegistroLovTercero(0);
            }
        } else {
            modificarInfoRegistroLovTercero(0);
        }
    }

    public void contarRegistrosTerceroC() {
        if (listTerceroConsolidador != null) {
            if (listTerceroConsolidador.size() > 0) {
                modificarInfoRegistroTerceroC(listTerceroConsolidador.size());
            } else {
                modificarInfoRegistroTerceroC(0);
            }
        } else {
            modificarInfoRegistroTerceroC(0);
        }
    }

    public void contarRegistrosCiudadT() {
        if (listCiudades != null) {
            if (listCiudades.size() > 0) {
                modificarInfoRegistroCiudadT(listCiudades.size());
            } else {
                modificarInfoRegistroCiudadT(0);
            }
        } else {
            modificarInfoRegistroCiudadT(0);
        }
    }

    public void contarRegistrosCiudadTS() {
        if (listCiudades != null) {
            if (listCiudades.size() > 0) {
                modificarInfoRegistroCiudadTS(listCiudades.size());
            } else {
                modificarInfoRegistroCiudadTS(0);
            }
        } else {
            modificarInfoRegistroCiudadTS(0);
        }
    }

    public void contarRegistrosEmpresa() {
        if (listEmpresas != null) {
            if (listEmpresas.size() > 0) {
                modificarInfoRegistroEmpresa(listEmpresas.size());
            } else {
                modificarInfoRegistroEmpresa(0);
            }
        } else {
            modificarInfoRegistroEmpresa(0);
        }
    }

    public void recordarSeleccionT() {
        if (terceroTablaSeleccionado != null) {
            FacesContext c = FacesContext.getCurrentInstance();
            tablaT = (DataTable) c.getViewRoot().findComponent("form:datosTerceros");
            tablaT.setSelection(terceroTablaSeleccionado);
        }
    }

    public void anularLOV() {
        activarLOV = true;
        PrimefacesContextUI.actualizar("form:listaValores");
    }

    public void recordarSeleccionTS() {
        if (terceroSucursalTablaSeleccionado != null) {
            FacesContext c = FacesContext.getCurrentInstance();
            tablaT = (DataTable) c.getViewRoot().findComponent("form:datosTercerosSucursales");
            tablaT.setSelection(terceroSucursalTablaSeleccionado);
        }
    }

    //GET - SET ************************************************************************************************************//////////////////////
    public List<Terceros> getListTerceros() {
        try {
            if (listTerceros == null) {
                if (empresaActual.getSecuencia() != null) {
                    listTerceros = administrarTercero.obtenerListTerceros(empresaActual.getSecuencia());
                    if (listTerceros != null) {
                        for (int i = 0; i < listTerceros.size(); i++) {
                            if (listTerceros.get(i).getCiudad() == null) {
                                listTerceros.get(i).setCiudad(new Ciudades());
                            }
                            if (listTerceros.get(i).getTerceroconsolidador() == null) {
                                listTerceros.get(i).setTerceroconsolidador(new Terceros());
                            }
                        }
                    }
                }
            }
            return listTerceros;
        } catch (Exception e) {
            System.out.println("Error getListTerceros " + e.toString());
            return null;
        }
    }

    public void setListTerceros(List<Terceros> t) {
        this.listTerceros = t;
    }

    public List<Terceros> getFiltrarListTercero() {
        return filtrarListTercero;
    }

    public void setFiltrarListTercero(List<Terceros> t) {
        this.filtrarListTercero = t;
    }

    public Empleados getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleados empleado) {
        this.empleado = empleado;
    }

    public String getNombreXML() {
        return nombreXML;
    }

    public void setNombreXML(String nombreXML) {
        this.nombreXML = nombreXML;
    }

    public String getNombreTabla() {
        return nombreTabla;
    }

    public void setNombreTabla(String nombreTabla) {
        this.nombreTabla = nombreTabla;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public void setAceptar(boolean aceptar) {
        this.aceptar = aceptar;
    }

    public List<Terceros> getListTerceroModificar() {
        return listTerceroModificar;
    }

    public void setListTerceroModificar(List<Terceros> listTerceroModificar) {
        this.listTerceroModificar = listTerceroModificar;
    }

    public Terceros getNuevoTercero() {
        return nuevoTercero;
    }

    public void setNuevoTercero(Terceros nuevoTercero) {
        this.nuevoTercero = nuevoTercero;
    }

    public List<Terceros> getListTerceroCrear() {
        return listTerceroCrear;
    }

    public void setListTerceroCrear(List<Terceros> listTerceroCrear) {
        this.listTerceroCrear = listTerceroCrear;
    }

    public List<Terceros> getListTerceroBorrar() {
        return listTerceroBorrar;
    }

    public void setListTerceroBorrar(List<Terceros> listTerceroBorrar) {
        this.listTerceroBorrar = listTerceroBorrar;
    }

    public Terceros getEditarTercero() {
        return editarTercero;
    }

    public void setEditarTercero(Terceros editarTercero) {
        this.editarTercero = editarTercero;
    }

    public Terceros getDuplicarTecero() {
        return duplicarTercero;
    }

    public void setDuplicarTecero(Terceros duplicarTecero) {
        this.duplicarTercero = duplicarTecero;
    }

    public String getMsnConfirmarRastro() {
        return msnConfirmarRastro;
    }

    public void setMsnConfirmarRastro(String msnConfirmarRastro) {
        this.msnConfirmarRastro = msnConfirmarRastro;
    }

    public String getMsnConfirmarRastroHistorico() {
        return msnConfirmarRastroHistorico;
    }

    public void setMsnConfirmarRastroHistorico(String msnConfirmarRastroHistorico) {
        this.msnConfirmarRastroHistorico = msnConfirmarRastroHistorico;
    }

    public BigInteger getBackUp() {
        return backUp;
    }

    public void setBackUp(BigInteger backUp) {
        this.backUp = backUp;
    }

    public String getNombreTablaRastro() {
        return nombreTablaRastro;
    }

    public void setNombreTablaRastro(String nombreTablaRastro) {
        this.nombreTablaRastro = nombreTablaRastro;
    }

    public List<TercerosSucursales> getListTercerosSucursales() {
        try {
            if (listTercerosSucursales == null) {
                if (terceroTablaSeleccionado != null) {
                    listTercerosSucursales = administrarTercero.obtenerListTercerosSucursales(terceroTablaSeleccionado.getSecuencia());
                    if (listTercerosSucursales != null) {
                        for (int i = 0; i < listTercerosSucursales.size(); i++) {
                            if (listTercerosSucursales.get(i).getCiudad() == null) {
                                listTercerosSucursales.get(i).setCiudad(new Ciudades());
                            }
                        }
                    }
                }
            }
            return listTercerosSucursales;
        } catch (Exception e) {
            System.out.println("Error en getListTercerosSucursales : " + e.toString());
            return null;
        }
    }

    public void setListTercerosSucursales(List<TercerosSucursales> listTercerosSucursales) {
        this.listTercerosSucursales = listTercerosSucursales;
    }

    public List<TercerosSucursales> getFiltrarListTercerosSucursales() {
        return filtrarListTercerosSucursales;
    }

    public void setFiltrarListTercerosSucursales(List<TercerosSucursales> filtrarListTercerosSucursales) {
        this.filtrarListTercerosSucursales = filtrarListTercerosSucursales;
    }

    public List<Empresas> getListEmpresas() {
        listEmpresas = administrarTercero.listEmpresas();
        return listEmpresas;
    }

    public void setListEmpresas(List<Empresas> listEmpresas) {
        this.listEmpresas = listEmpresas;
    }

    public List<Empresas> getFiltrarListEmpresas() {
        return filtrarListEmpresas;
    }

    public void setFiltrarListEmpresas(List<Empresas> filtrarListEmpresas) {
        this.filtrarListEmpresas = filtrarListEmpresas;
    }

    public Empresas getEmpresaActual() {
        return empresaActual;
    }

    public void setEmpresaActual(Empresas empresaActual) {
        this.empresaActual = empresaActual;
    }

    public Empresas getEmpresaSeleccionada() {
        return empresaSeleccionada;
    }

    public void setEmpresaSeleccionada(Empresas empresaSeleccionada) {
        this.empresaSeleccionada = empresaSeleccionada;
    }

    public List<Ciudades> getListCiudades() {
        if (listCiudades == null) {
            listCiudades = administrarTercero.listCiudades();
        }
        return listCiudades;
    }

    public void setListCiudades(List<Ciudades> listCiudades) {
        this.listCiudades = listCiudades;
    }

    public List<Ciudades> getFiltrarListCiudades() {
        return filtrarListCiudades;
    }

    public void setFiltrarListCiudades(List<Ciudades> filtrarListCiudades) {
        this.filtrarListCiudades = filtrarListCiudades;
    }

    public Ciudades getCiudadSeleccionada() {
        return ciudadSeleccionada;
    }

    public void setCiudadSeleccionada(Ciudades ciudadSeleccionada) {
        this.ciudadSeleccionada = ciudadSeleccionada;
    }

    public List<Terceros> getListTerceroConsolidador() {
        if (empresaActual.getSecuencia() != null) {
            listTerceroConsolidador = administrarTercero.obtenerListTerceros(empresaActual.getSecuencia());
        }
        return listTerceroConsolidador;
    }

    public void setListTerceroConsolidador(List<Terceros> listTerceroConsolidador) {
        this.listTerceroConsolidador = listTerceroConsolidador;
    }

    public List<Terceros> getFiltrarListTerceroConsolidador() {
        return filtrarListTerceroConsolidador;
    }

    public void setFiltrarListTerceroConsolidador(List<Terceros> filtrarListTerceroConsolidador) {
        this.filtrarListTerceroConsolidador = filtrarListTerceroConsolidador;
    }

    public Terceros getTerceroCSeleccionado() {
        return terceroCSeleccionado;
    }

    public void setTerceroCSeleccionado(Terceros terceroCSeleccionado) {
        this.terceroCSeleccionado = terceroCSeleccionado;
    }

    public TercerosSucursales getEditarTerceroSucursal() {
        return editarTerceroSucursal;
    }

    public void setEditarTerceroSucursal(TercerosSucursales editarTerceroSucursal) {
        this.editarTerceroSucursal = editarTerceroSucursal;
    }

    public long getNitConsolidado() {
        return nitConsolidado;
    }

    public void setNitConsolidado(long nitConsolidado) {
        this.nitConsolidado = nitConsolidado;
    }

    public TercerosSucursales getNuevoTerceroSucursal() {
        return nuevoTerceroSucursal;
    }

    public void setNuevoTerceroSucursal(TercerosSucursales nuevoTerceroSucursal) {
        this.nuevoTerceroSucursal = nuevoTerceroSucursal;
    }

    public TercerosSucursales getDuplicarTerceroSucursal() {
        return duplicarTerceroSucursal;
    }

    public void setDuplicarTerceroSucursal(TercerosSucursales duplicarTerceroSucursal) {
        this.duplicarTerceroSucursal = duplicarTerceroSucursal;
    }

    public Terceros getTerceroTablaSeleccionado() {
        return terceroTablaSeleccionado;
    }

    public void setTerceroTablaSeleccionado(Terceros terceroTablaSeleccionado) {
        this.terceroTablaSeleccionado = terceroTablaSeleccionado;
    }

    public TercerosSucursales getTerceroSucursalTablaSeleccionado() {
        return terceroSucursalTablaSeleccionado;
    }

    public void setTerceroSucursalTablaSeleccionado(TercerosSucursales terceroSucursalTablaSeleccionado) {
        this.terceroSucursalTablaSeleccionado = terceroSucursalTablaSeleccionado;
    }

    public String getInfoRegistroEmpresa() {
        return infoRegistroEmpresa;
    }

    public void setInfoRegistroEmpresa(String infoRegistroEmpresa) {
        this.infoRegistroEmpresa = infoRegistroEmpresa;
    }

    public String getInfoRegistroTerceroSucursal() {
        return infoRegistroTerceroSucursal;
    }

    public void setInfoRegistroTerceroSucursal(String infoRegistroTerceroSucursal) {
        this.infoRegistroTerceroSucursal = infoRegistroTerceroSucursal;
    }

    public String getInfoRegistroCiudad2() {
        return infoRegistroCiudad2;
    }

    public void setInfoRegistroCiudad2(String infoRegistroCiudad2) {
        this.infoRegistroCiudad2 = infoRegistroCiudad2;
    }

    public String getInfoRegistroCiudad1() {

        return infoRegistroCiudad1;
    }

    public void setInfoRegistroCiudad1(String infoRegistroCiudad1) {
        this.infoRegistroCiudad1 = infoRegistroCiudad1;
    }

    public String getInfoRegistroTercero() {
        return infoRegistroTercero;
    }

    public void setInfoRegistroTercero(String infoRegistroTercero) {
        this.infoRegistroTercero = infoRegistroTercero;
    }

    public String getInfoRegistroTerceroConsolidador() {
        return infoRegistroTerceroConsolidador;
    }

    public void setInfoRegistroTerceroConsolidador(String infoRegistroTerceroConsolidador) {
        this.infoRegistroTerceroConsolidador = infoRegistroTerceroConsolidador;
    }

    public List<Terceros> getListTercerosLOV() {
        if (listTercerosLOV == null) {
            listTercerosLOV = administrarTercero.obtenerListTerceros(empresaActual.getSecuencia());
        }
        return listTercerosLOV;
    }

    public void setListTercerosLOV(List<Terceros> listTercerosLOV) {
        this.listTercerosLOV = listTercerosLOV;
    }

    public List<Terceros> getFiltrarListTerceroLOV() {
        return filtrarListTerceroLOV;
    }

    public void setFiltrarListTerceroLOV(List<Terceros> filtrarListTerceroLOV) {
        this.filtrarListTerceroLOV = filtrarListTerceroLOV;
    }

    public Terceros getTerceroLOVSeleccionado() {
        return terceroLOVSeleccionado;
    }

    public void setTerceroLOVSeleccionado(Terceros terceroLOVSeleccionado) {
        this.terceroLOVSeleccionado = terceroLOVSeleccionado;
    }

    public String getAltoTablaTercero() {
        return altoTablaTercero;
    }

    public void setAltoTablaTercero(String altoTablaTercero) {
        this.altoTablaTercero = altoTablaTercero;
    }

    public String getAltoTablaSucursal() {
        return altoTablaSucursal;
    }

    public void setAltoTablaSucursal(String altoTablaSucursal) {
        this.altoTablaSucursal = altoTablaSucursal;
    }

    public boolean isGuardado() {
        return guardado;
    }

    public void setGuardado(boolean guardado) {
        this.guardado = guardado;
    }

    public boolean isActivarLOV() {
        return activarLOV;
    }

    public void setActivarLOV(boolean activarLOV) {
        this.activarLOV = activarLOV;
    }
}
