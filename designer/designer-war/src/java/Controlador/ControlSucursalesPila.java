/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.SucursalesPila;
import Entidades.Empresas;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarSucursalesPilaInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
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
public class ControlSucursalesPila implements Serializable {

    @EJB
    AdministrarSucursalesPilaInterface administrarSucursalesPila;
    @EJB
    AdministrarRastrosInterface administrarRastros;
    private int cualCelda, tipoLista, tipoActualizacion, k, bandera;
    private BigInteger l;
    private boolean aceptar, guardado;
    private boolean permitirIndex;
    private int registrosBorrados;
    private String mensajeValidacion;
    private List<Empresas> listaEmpresas;
    private List<Empresas> filtradoListaEmpresas;

    private Empresas empresaSeleccionada;
    private int banderaModificacionEmpresa;
    private int indiceEmpresaMostrada;
    private List<SucursalesPila> listSucursalesPilaPorEmpresa;
    private List<SucursalesPila> filtrarSucursalesPila;
    private List<SucursalesPila> crearSucursalesPila;
    private List<SucursalesPila> modificarSucursalesPila;
    private List<SucursalesPila> borrarSucursalesPila;
    private SucursalesPila nuevaSucursalPila;
    private SucursalesPila duplicarCentroCosto;
    private SucursalesPila editarCentroCosto;
    private SucursalesPila sucursalPilaSeleccionada;

    private Column codigoSucursalP, nombreSucursalP;

    //AUTOCOMPLETAR
    private List<SucursalesPila> filterSucursalesPilaPorEmpresa;
    private Empresas backUpEmpresaActual;

    private boolean banderaSeleccionSucursalesPilaPorEmpresa;
    private int tamano;
    private String infoRegistro;
    private BigInteger contarNovedadesAutoLiquidacionesSucursal_Pila;
    private BigInteger contarNovedadesCorreccionesAutolSucursal_Pila;
    private BigInteger contarOdisCabecerasSucursal_Pila;
    private BigInteger contarOdiscorReaccionesCabSucursal_Pila;
    private BigInteger contarParametrosInformesSucursal_Pila;
    private BigInteger contarUbicacionesGeograficasSucursal_Pila;
    private boolean activarLov;
    private String paginaAnterior = "nominaf";
    private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

    public ControlSucursalesPila() {
        permitirIndex = true;
        listaEmpresas = null;
        empresaSeleccionada = null;
        indiceEmpresaMostrada = 0;
        listSucursalesPilaPorEmpresa = null;
        crearSucursalesPila = new ArrayList<SucursalesPila>();
        modificarSucursalesPila = new ArrayList<SucursalesPila>();
        borrarSucursalesPila = new ArrayList<SucursalesPila>();
        editarCentroCosto = new SucursalesPila();
        nuevaSucursalPila = new SucursalesPila();
        duplicarCentroCosto = new SucursalesPila();
        aceptar = true;
        filtradoListaEmpresas = null;
        guardado = true;
        banderaSeleccionSucursalesPilaPorEmpresa = false;
        tamano = 270;
        activarLov = true;
        mapParametros.put("paginaAnterior", paginaAnterior);
    }

   public void limpiarListasValor() {

   }

   @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarSucursalesPila.obtenerConexion(ses.getId());
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
        listSucursalesPilaPorEmpresa = null;
        getListaEmpresas();

    }

    public void recibirParametros(Map<String, Object> map) {
        mapParametros = map;
        paginaAnterior = (String) mapParametros.get("paginaAnterior");
        listaEmpresas = null;
        getListaEmpresas();
        if (listaEmpresas != null) {
            if (!listaEmpresas.isEmpty()) {
                empresaSeleccionada = listaEmpresas.get(0);
            }
        }
        listSucursalesPilaPorEmpresa = null;
        getListaEmpresas();
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
            String pagActual = "sucursal_pila";
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

    public String retornarPagina() {
        return paginaAnterior;
    }

    public void eventoFiltrar() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
        contarRegistros();
    }

    public void cambiarIndice(SucursalesPila sucursal, int celda) {

        if (permitirIndex == true) {
            sucursalPilaSeleccionada = sucursal;
            cualCelda = celda;
            sucursalPilaSeleccionada.getSecuencia();
            if (cualCelda == 0) {
                sucursalPilaSeleccionada.getCodigo();
            } else if (cualCelda == 1) {
                sucursalPilaSeleccionada.getDescripcion();
            }
        }
    }

    public void modificandoSucursalPila(SucursalesPila sucursal) {

        sucursalPilaSeleccionada = sucursal;
        if (modificarSucursalesPila.isEmpty()) {
            modificarSucursalesPila.add(sucursalPilaSeleccionada);
        } else if (!modificarSucursalesPila.contains(sucursalPilaSeleccionada)) {
            modificarSucursalesPila.add(sucursalPilaSeleccionada);
        }
        if (guardado == true) {
            guardado = false;
        }
        RequestContext.getCurrentInstance().update("form:datosSucursalesPila");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void cancelarModificacion() {
        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            codigoSucursalP = (Column) c.getViewRoot().findComponent("form:datosSucursalesPila:codigoSucursalP");
            codigoSucursalP.setFilterStyle("display: none; visibility: hidden;");
            nombreSucursalP = (Column) c.getViewRoot().findComponent("form:datosSucursalesPila:nombreSucursalP");
            nombreSucursalP.setFilterStyle("display: none; visibility: hidden;");
            bandera = 0;
            filtrarSucursalesPila = null;
            tipoLista = 0;
        }

        borrarSucursalesPila.clear();
        crearSucursalesPila.clear();
        modificarSucursalesPila.clear();
        k = 0;
        listSucursalesPilaPorEmpresa = null;
        sucursalPilaSeleccionada = null;
        guardado = true;
        aceptar = true;
        permitirIndex = true;
        getListSucursalesPilaPorEmpresa();
        contarRegistros();
        cambiarEmpresa();
        RequestContext.getCurrentInstance().update("form:datosSucursalesPila");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void salir() {
        System.out.println("entre a CONTROLSUCURSALESPILA.cancelarModificacion");
        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            codigoSucursalP = (Column) c.getViewRoot().findComponent("form:datosSucursalesPila:codigoSucursalP");
            codigoSucursalP.setFilterStyle("display: none; visibility: hidden;");
            nombreSucursalP = (Column) c.getViewRoot().findComponent("form:datosSucursalesPila:nombreSucursalP");
            nombreSucursalP.setFilterStyle("display: none; visibility: hidden;");
            bandera = 0;
            filtrarSucursalesPila = null;
            tipoLista = 0;
        }

        borrarSucursalesPila.clear();
        crearSucursalesPila.clear();
        modificarSucursalesPila.clear();
        k = 0;
        listSucursalesPilaPorEmpresa = null;
        guardado = true;
        permitirIndex = true;
        getListSucursalesPilaPorEmpresa();
        contarRegistros();
        banderaModificacionEmpresa = 0;
        if (banderaModificacionEmpresa == 0) {
            cambiarEmpresa();
        }
        RequestContext.getCurrentInstance().update("form:datosSucursalesPila");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        navegar("atras");
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void limpiarNuevoSucursalesPila() {
        nuevaSucursalPila = new SucursalesPila();
    }

    public void agregarNuevoSucursalesPila() {
        int contador = 0;
        mensajeValidacion = " ";
        int duplicados = 0;
        RequestContext context = RequestContext.getCurrentInstance();

        banderaModificacionEmpresa = 1;
        if (nuevaSucursalPila.getCodigo() == null) {
            mensajeValidacion = "El campo código es obligatorio";

        } else if (nuevaSucursalPila.getCodigo().isEmpty()) {
            mensajeValidacion = "El campo código es obligatorio";
        } else {
            for (int x = 0; x < listSucursalesPilaPorEmpresa.size(); x++) {
                if (listSucursalesPilaPorEmpresa.get(x).getCodigo().equals(nuevaSucursalPila.getCodigo())) {
                    duplicados++;
                }
            }
            if (duplicados > 0) {
                mensajeValidacion = "Existe un registro con el código ingresado. Por favor ingrese un código válido";
            } else {
                contador++;
            }
        }
        if (nuevaSucursalPila.getDescripcion() == null) {
            mensajeValidacion = "El campo descripción es obligatorio";
        } else if (nuevaSucursalPila.getDescripcion().isEmpty()) {
            mensajeValidacion = "El campo descripción es obligatorio";
        } else {
            System.out.println("Bandera : ");
            contador++;
        }
        if (contador == 2) {
            k++;
            l = BigInteger.valueOf(k);
            nuevaSucursalPila.setSecuencia(l);
            nuevaSucursalPila.setEmpresa(empresaSeleccionada);
            crearSucursalesPila.add(nuevaSucursalPila);
            sucursalPilaSeleccionada = nuevaSucursalPila;
            contarRegistros();
            listSucursalesPilaPorEmpresa.add(nuevaSucursalPila);
            RequestContext.getCurrentInstance().update("form:datosSucursalesPila");
            nuevaSucursalPila = new SucursalesPila();
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                codigoSucursalP = (Column) c.getViewRoot().findComponent("form:datosSucursalesPila:codigoSucursalP");
                codigoSucursalP.setFilterStyle("display: none; visibility: hidden;");
                nombreSucursalP = (Column) c.getViewRoot().findComponent("form:datosSucursalesPila:nombreSucursalP");
                nombreSucursalP.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosSucursalesPila");
                bandera = 0;
                filtrarSucursalesPila = null;
                tipoLista = 0;
            }
            mensajeValidacion = " ";
            RequestContext.getCurrentInstance().execute("PF('NuevoRegistroSucursalesPila').hide()");

        } else {
            contador = 0;
            RequestContext.getCurrentInstance().update("form:validacionNuevaSucursal");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevaSucursal').show()");
        }

    }

    public void mostrarDialogoNuevoTiposSucursalesPila() {
        RequestContext context = RequestContext.getCurrentInstance();
        nuevaSucursalPila = new SucursalesPila();
        RequestContext.getCurrentInstance().update("formularioDialogos:nuevoSucursalesPila");
        RequestContext.getCurrentInstance().execute("PF('NuevoRegistroSucursalesPila').show()");
    }

    public void duplicandoSucursalesPila() {
        if (sucursalPilaSeleccionada != null) {
            duplicarCentroCosto = new SucursalesPila();
            k++;
            l = BigInteger.valueOf(k);
            duplicarCentroCosto.setSecuencia(l);
            duplicarCentroCosto.setEmpresa(sucursalPilaSeleccionada.getEmpresa());
            duplicarCentroCosto.setCodigo(sucursalPilaSeleccionada.getCodigo());
            duplicarCentroCosto.setDescripcion(sucursalPilaSeleccionada.getDescripcion());
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarSucursalesPila");
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroSucursalesPila').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void limpiarDuplicarSucursalesPila() {
        duplicarCentroCosto = new SucursalesPila();
    }

    public void confirmarDuplicar() {
        int contador = 0;
        mensajeValidacion = " ";
        int duplicados = 0;
        Short a = 0;
        a = null;

        if (duplicarCentroCosto.getCodigo() == null) {
            mensajeValidacion = "El campo código es obligatorio";
        } else if (duplicarCentroCosto.getCodigo().isEmpty()) {
            mensajeValidacion = "El campo código es obligatorio";
        } else {
            for (int x = 0; x < listSucursalesPilaPorEmpresa.size(); x++) {
                if (listSucursalesPilaPorEmpresa.get(x).getCodigo().equals(duplicarCentroCosto.getCodigo())) {
                    duplicados++;
                }
            }
            if (duplicados > 0) {
                mensajeValidacion = "Existe un registro con el código ingresado. Por favor ingrese un código válido";
            } else {
                contador++;
            }
        }
        if (duplicarCentroCosto.getDescripcion() == null) {
            mensajeValidacion = "El campo descripción es obligatorio";
        } else if (duplicarCentroCosto.getDescripcion().isEmpty()) {
            mensajeValidacion = "El campo descripción es obligatorio";
        } else {
            contador++;
        }

        if (contador == 2) {
            k++;
            l = BigInteger.valueOf(k);
            duplicarCentroCosto.setSecuencia(l);
            duplicarCentroCosto.setEmpresa(empresaSeleccionada);
            listSucursalesPilaPorEmpresa.add(duplicarCentroCosto);
            crearSucursalesPila.add(duplicarCentroCosto);
            sucursalPilaSeleccionada = duplicarCentroCosto;
            RequestContext.getCurrentInstance().update("form:datosSucursalesPila");
            contarRegistros();

            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                //CERRAR FILTRADO
                codigoSucursalP = (Column) c.getViewRoot().findComponent("form:datosSucursalesPila:codigoSucursalP");
                codigoSucursalP.setFilterStyle("display: none; visibility: hidden;");
                //1
                nombreSucursalP = (Column) c.getViewRoot().findComponent("form:datosSucursalesPila:nombreSucursalP");
                nombreSucursalP.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosSucursalesPila");
                bandera = 0;
                filtrarSucursalesPila = null;
                tipoLista = 0;
            }
            duplicarCentroCosto = new SucursalesPila();
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroSucursalesPila').hide()");
            mensajeValidacion = " ";

        } else {
            contador = 0;
            RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
            RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
        }
    }

    public void verificarBorrado() {
        if (sucursalPilaSeleccionada != null) {

            contarNovedadesAutoLiquidacionesSucursal_Pila = administrarSucursalesPila.contarNovedadesAutoLiquidacionesSucursal_Pila(sucursalPilaSeleccionada.getSecuencia());
            contarNovedadesCorreccionesAutolSucursal_Pila = administrarSucursalesPila.contarNovedadesCorreccionesAutolSucursal_Pila(sucursalPilaSeleccionada.getSecuencia());;
            contarOdisCabecerasSucursal_Pila = administrarSucursalesPila.contarOdisCabecerasSucursal_Pila(sucursalPilaSeleccionada.getSecuencia());
            contarOdiscorReaccionesCabSucursal_Pila = administrarSucursalesPila.contarOdiscorReaccionesCabSucursal_Pila(sucursalPilaSeleccionada.getSecuencia());
            contarParametrosInformesSucursal_Pila = administrarSucursalesPila.contarParametrosInformesSucursal_Pila(sucursalPilaSeleccionada.getSecuencia());
            contarUbicacionesGeograficasSucursal_Pila = administrarSucursalesPila.contarUbicacionesGeograficasSucursal_Pila(sucursalPilaSeleccionada.getSecuencia());
            if (contarNovedadesAutoLiquidacionesSucursal_Pila.equals(new BigInteger("0"))
                    && contarNovedadesCorreccionesAutolSucursal_Pila.equals(new BigInteger("0"))
                    && contarOdisCabecerasSucursal_Pila.equals(new BigInteger("0"))
                    && contarOdiscorReaccionesCabSucursal_Pila.equals(new BigInteger("0"))
                    && contarParametrosInformesSucursal_Pila.equals(new BigInteger("0"))
                    && contarUbicacionesGeograficasSucursal_Pila.equals(new BigInteger("0"))) {
                System.out.println("Borrado==0");
                borrandoCentroCosto();
            } else {
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:validacionBorrar");
                RequestContext.getCurrentInstance().execute("PF('validacionBorrar').show()");
                contarNovedadesAutoLiquidacionesSucursal_Pila = new BigInteger("-1");
                contarNovedadesCorreccionesAutolSucursal_Pila = new BigInteger("-1");
                contarOdisCabecerasSucursal_Pila = new BigInteger("-1");
                contarOdiscorReaccionesCabSucursal_Pila = new BigInteger("-1");
                contarParametrosInformesSucursal_Pila = new BigInteger("-1");
                contarUbicacionesGeograficasSucursal_Pila = new BigInteger("-1");
            }
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void borrandoCentroCosto() {
        if (sucursalPilaSeleccionada != null) {
            if (!modificarSucursalesPila.isEmpty() && modificarSucursalesPila.contains(sucursalPilaSeleccionada)) {
                int modIndex = modificarSucursalesPila.indexOf(sucursalPilaSeleccionada);
                modificarSucursalesPila.remove(modIndex);
                borrarSucursalesPila.add(sucursalPilaSeleccionada);
            } else if (!crearSucursalesPila.isEmpty() && crearSucursalesPila.contains(sucursalPilaSeleccionada)) {
                int crearIndex = crearSucursalesPila.indexOf(sucursalPilaSeleccionada);
                crearSucursalesPila.remove(crearIndex);
            } else {
                borrarSucursalesPila.add(sucursalPilaSeleccionada);
            }
            listSucursalesPilaPorEmpresa.remove(sucursalPilaSeleccionada);
            if (tipoLista == 1) {
                filtrarSucursalesPila.remove(sucursalPilaSeleccionada);
            }

            if (guardado == true) {
                guardado = false;
            }
            sucursalPilaSeleccionada = null;
            RequestContext.getCurrentInstance().update("form:datosSucursalesPila");
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
    }

    public void guardarCambiosCentroCosto() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (guardado == false) {
            System.out.println("Realizando Operaciones Vigencias Localizacion");
            if (!borrarSucursalesPila.isEmpty()) {
                administrarSucursalesPila.borrarSucursalesPila(borrarSucursalesPila);
                registrosBorrados = borrarSucursalesPila.size();
                RequestContext.getCurrentInstance().update("form:mostrarBorrados");
                RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
                borrarSucursalesPila.clear();
            }
            if (!crearSucursalesPila.isEmpty()) {
                administrarSucursalesPila.crearSucursalesPila(crearSucursalesPila);
                crearSucursalesPila.clear();
            }
            if (!modificarSucursalesPila.isEmpty()) {
                administrarSucursalesPila.modificarSucursalesPila(modificarSucursalesPila);
                modificarSucursalesPila.clear();
            }
            FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
            System.out.println("Se guardaron los datos con exito");
            listSucursalesPilaPorEmpresa = null;
            RequestContext.getCurrentInstance().update(":form:datosSucursalesPila");
            k = 0;
            guardado = true;
            aceptar = true;
        }
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        banderaModificacionEmpresa = 0;
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
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            tamano = 250;
            System.out.println("Activar");
            codigoSucursalP = (Column) c.getViewRoot().findComponent("form:datosSucursalesPila:codigoSucursalP");
            codigoSucursalP.setFilterStyle("width: 85% !important;");
            nombreSucursalP = (Column) c.getViewRoot().findComponent("form:datosSucursalesPila:nombreSucursalP");
            nombreSucursalP.setFilterStyle("width: 85% !important;");
            RequestContext.getCurrentInstance().update("form:datosSucursalesPila");
            bandera = 1;
        } else if (bandera == 1) {
            System.out.println("Desactivar");
            tamano = 270;
            codigoSucursalP = (Column) c.getViewRoot().findComponent("form:datosSucursalesPila:codigoSucursalP");
            codigoSucursalP.setFilterStyle("display: none; visibility: hidden;");
            nombreSucursalP = (Column) c.getViewRoot().findComponent("form:datosSucursalesPila:nombreSucursalP");
            nombreSucursalP.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosSucursalesPila");
            bandera = 0;
            filtrarSucursalesPila = null;
            tipoLista = 0;
        }
    }

    public void editarCelda() {
        if (sucursalPilaSeleccionada != null) {
            editarCentroCosto = sucursalPilaSeleccionada;
            RequestContext context = RequestContext.getCurrentInstance();
            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarCCC");
                RequestContext.getCurrentInstance().execute("PF('editarCCC').show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarNCC");
                RequestContext.getCurrentInstance().execute("PF('editarNCC').show()");
                cualCelda = -1;
            }
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosSucursalesPilaExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "SucursalesPila", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    /**
     *
     * @throws IOException
     */
    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosSucursalesPilaExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "SucursalesPila", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (sucursalPilaSeleccionada != null) {
            int resultado = administrarRastros.obtenerTabla(sucursalPilaSeleccionada.getSecuencia(), "SUCURSALESPILA");
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
        } else if (administrarRastros.verificarHistoricosTabla("SUCURSALESPILA")) {
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
    }

    public void lovEmpresas() {
        sucursalPilaSeleccionada = null;
        cualCelda = -1;
        RequestContext.getCurrentInstance().execute("PF('EmpresasDialogo').show()");
    }

    public void cambiarEmpresa() {
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:nombreEmpresa");
        RequestContext.getCurrentInstance().update("form:nitEmpresa");
        getListSucursalesPilaPorEmpresa();
        filtradoListaEmpresas = null;
        listSucursalesPilaPorEmpresa = null;
        aceptar = true;
        RequestContext.getCurrentInstance().update("form:datosSucursalesPila");
        contarRegistros();
        context.reset("formularioDialogos:lovEmpresas:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovEmpresas').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('EmpresasDialogo').hide()");

        RequestContext.getCurrentInstance().update("formularioDialogos:EmpresasDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovEmpresas");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarE");
        backUpEmpresaActual = empresaSeleccionada;
        banderaModificacionEmpresa = 0;
    }

    public void cancelarCambioEmpresa() {
        filtradoListaEmpresas = null;
        banderaModificacionEmpresa = 0;
        aceptar = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:lovEmpresas:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovEmpresas').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('EmpresasDialogo').hide()");

        RequestContext.getCurrentInstance().update("formularioDialogos:EmpresasDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovEmpresas");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarE");
    }

    public void contarRegistros() {
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
    }

    public void contarRegistrosEmpresas() {
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroEmpresas");
    }

//----------------GETS Y SETS----------------------**
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
        if (listaEmpresas == null) {
            listaEmpresas = administrarSucursalesPila.buscarEmpresas();
        }
        return listaEmpresas;
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

    public List<SucursalesPila> getListSucursalesPilaPorEmpresa() {
        if (listSucursalesPilaPorEmpresa == null) {
            if (empresaSeleccionada != null) {
                listSucursalesPilaPorEmpresa = administrarSucursalesPila.consultarSucursalPila(empresaSeleccionada.getSecuencia());
            }
        }
        return listSucursalesPilaPorEmpresa;
    }

    public void setListSucursalesPilaPorEmpresa(List<SucursalesPila> listSucursalesPilaPorEmpresa) {
        this.listSucursalesPilaPorEmpresa = listSucursalesPilaPorEmpresa;
    }

    public List<SucursalesPila> getFiltrarSucursalesPila() {
        return filtrarSucursalesPila;
    }

    public void setFiltrarSucursalesPila(List<SucursalesPila> filtrarSucursalesPila) {
        this.filtrarSucursalesPila = filtrarSucursalesPila;
    }

    public SucursalesPila getNuevaSucursalPila() {
        return nuevaSucursalPila;
    }

    public void setNuevaSucursalPila(SucursalesPila nuevaSucursalPila) {
        this.nuevaSucursalPila = nuevaSucursalPila;
    }

    public SucursalesPila getDuplicarCentroCosto() {
        return duplicarCentroCosto;
    }

    public void setDuplicarCentroCosto(SucursalesPila duplicarCentroCosto) {
        this.duplicarCentroCosto = duplicarCentroCosto;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public void setAceptar(boolean aceptar) {
        this.aceptar = aceptar;
    }

    public List<SucursalesPila> getFilterSucursalesPilaPorEmpresa() {
        return filterSucursalesPilaPorEmpresa;
    }

    public void setFilterSucursalesPilaPorEmpresa(List<SucursalesPila> filterSucursalesPilaPorEmpresa) {
        this.filterSucursalesPilaPorEmpresa = filterSucursalesPilaPorEmpresa;
    }

    public SucursalesPila getEditarCentroCosto() {
        return editarCentroCosto;
    }

    public void setEditarCentroCosto(SucursalesPila editarCentroCosto) {
        this.editarCentroCosto = editarCentroCosto;
    }

    public boolean isGuardado() {
        return guardado;
    }

    public void setGuardado(boolean guardado) {
        this.guardado = guardado;
    }

    public SucursalesPila getSucursalPilaSeleccionada() {
        return sucursalPilaSeleccionada;
    }

    public void setSucursalPilaSeleccionada(SucursalesPila sucursalPilaSeleccionada) {
        this.sucursalPilaSeleccionada = sucursalPilaSeleccionada;
    }

    public String getInfoRegistro() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosSucursalesPila");
        infoRegistro = String.valueOf(tabla.getRowCount());
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
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

    public int getTamano() {
        return tamano;
    }

    public void setTamano(int tamano) {
        this.tamano = tamano;
    }

    public boolean isActivarLov() {
        return activarLov;
    }

    public void setActivarLov(boolean activarLov) {
        this.activarLov = activarLov;
    }

}
