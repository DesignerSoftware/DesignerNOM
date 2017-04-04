/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Empleados;
import Entidades.Empresas;
import Entidades.OdisCabeceras;
import Entidades.OdisDetalles;
import Entidades.RelacionesIncapacidades;
import Entidades.SucursalesPila;
import Entidades.Terceros;
import Entidades.TiposEntidades;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarOdiCabeceraInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
public class ControlOdiCabecera implements Serializable {

    @EJB
    AdministrarRastrosInterface administrarRastros;
    @EJB
    AdministrarOdiCabeceraInterface administrarOdiCabecera;

    private List<OdisCabeceras> listaOdiCabecera;
    private List<OdisCabeceras> filtrarListaOdiCabecera;
    private OdisCabeceras odiCabeceraSeleccionada;
    private OdisCabeceras nuevaOdiCabecera;
    private OdisCabeceras editarOdiCabecera;
    private OdisCabeceras duplicarOdiCabecera;

    private List<OdisDetalles> listaOdiDetalles;
    private List<OdisDetalles> filtrarListaOdiDetalles;
    private OdisDetalles OdiDetallesSeleccionada;
    private OdisDetalles nuevaOdiDetalles;
    private OdisDetalles editarOdiDetalles;
    private OdisDetalles duplicarOdiDetalles;

    //lovEmpresas
    private List<Empresas> lovEmpresas;
    private List<Empresas> filtrarLovEmpresas;
    private Empresas empresaSeleccionada;
    //lovTipoEntidad
    private List<TiposEntidades> lovTiposEntidades;
    private List<TiposEntidades> filtrarLovTiposEntidades;
    private TiposEntidades tipoEntidadSeleccionada;
    //lovTerceros
    private List<Terceros> lovTerceros;
    private List<Terceros> filtrarLovTerceros;
    private Terceros terceroSeleccionado;
    //lov sucursalesPila
    private List<SucursalesPila> lovSucursales;
    private List<SucursalesPila> filtrarLovSucursales;
    private SucursalesPila sucursalSeleccionada;
    //lov Empleados
    private List<Empleados> lovEmpleados;
    private List<Empleados> filtrarLovEmpleados;
    private Empleados empleadoSeleccionado;
    //lov Relaciones Incapacidades
    private List<RelacionesIncapacidades> lovRelaciones;
    private List<RelacionesIncapacidades> filtrarLovRelaciones;
    private RelacionesIncapacidades relacionSeleccionada;
    private int cualCelda, tipoLista, k, cualTabla;
    private List<OdisCabeceras> listaOdiCabeceraCrear;
    private List<OdisCabeceras> listaOdiCabeceraModificar;
    private List<OdisCabeceras> listaOdiCabeceraBorrar;
    private List<OdisDetalles> listaOdiDetallesCrear;
    private List<OdisDetalles> listaOdiDetallesModificar;
    private List<OdisDetalles> listaOdiDetallesBorrar;
    private boolean aceptar;
    private int tipoActualizacion; //Activo/Desactivo Crtl + F11
    private int bandera;
    private boolean permitirIndex, activarlov, editarDetalles;
    private int altotabla;
    private BigInteger l, auxiliar, secuenciaParametro, anioParametro, mesParametro;
    //RASTROS
    private boolean guardado, incluirdetalles;
    //OTROS
    private String infoRegistroOdiCabecera, infoRegistroOdiDetalles, infoRegistroEmpresas, infoRegistroTerceros, infoRegistroSucursales, infoRegistroTipoEntidad, infoRegistroEmpleados, infoRegistroRelacionesIncapacidades;
    private String mensajeValidacion;
    //COLUMNASODICABECERA
    private Column empresa, sucursal, nombreTercero, tipoEntidad, valor, numautorizacion, origen, detalle, anio, mes;
    //COLUMNASODIDETALLES
    private Column empleado, numdetalle, observacion, relacion, numcertificado, valorcobrado, valordetalle;
    private String paginaAnterior = "nominaf";
    private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

    public ControlOdiCabecera() {

        listaOdiCabeceraCrear = new ArrayList<OdisCabeceras>();
        listaOdiCabeceraModificar = new ArrayList<OdisCabeceras>();
        listaOdiCabeceraBorrar = new ArrayList<OdisCabeceras>();
        listaOdiDetallesBorrar = new ArrayList<OdisDetalles>();
        listaOdiDetallesCrear = new ArrayList<OdisDetalles>();
        listaOdiDetallesModificar = new ArrayList<OdisDetalles>();
        lovEmpresas = null;
        empresaSeleccionada = new Empresas();
        lovSucursales = null;
        sucursalSeleccionada = new SucursalesPila();
        lovTiposEntidades = null;
        tipoEntidadSeleccionada = new TiposEntidades();
        lovTerceros = null;
        terceroSeleccionado = new Terceros();
        lovEmpleados = null;
        empleadoSeleccionado = new Empleados();
        lovRelaciones = null;
        relacionSeleccionada = new RelacionesIncapacidades();
        nuevaOdiCabecera = new OdisCabeceras();
        nuevaOdiCabecera.setEmpresa(new Empresas());
        nuevaOdiCabecera.setSucursalpila(new SucursalesPila());
        nuevaOdiCabecera.setTercero(new Terceros());
        nuevaOdiCabecera.setTipoentidad(new TiposEntidades());
        nuevaOdiCabecera.setCheckDetalles(false);
        nuevaOdiCabecera.setAnio(anioParametro);
        nuevaOdiCabecera.setMes(mesParametro);
        duplicarOdiCabecera = new OdisCabeceras();
        duplicarOdiCabecera.setEmpresa(new Empresas());
        duplicarOdiCabecera.setSucursalpila(new SucursalesPila());
        duplicarOdiCabecera.setTercero(new Terceros());
        duplicarOdiCabecera.setTipoentidad(new TiposEntidades());
        duplicarOdiCabecera.setCheckDetalles(false);
        duplicarOdiCabecera.setAnio(anioParametro);
        duplicarOdiCabecera.setMes(mesParametro);
        nuevaOdiDetalles = new OdisDetalles();
        nuevaOdiDetalles.setEmpleado(new Empleados());
        nuevaOdiDetalles.setOdicabecera(odiCabeceraSeleccionada);
        editarOdiCabecera = new OdisCabeceras();
        altotabla = 85;
        aceptar = true;
        guardado = true;
        tipoLista = 0;
        permitirIndex = true;
        activarlov = true;
        incluirdetalles = false;
        editarDetalles = true;
        mapParametros.put("paginaAnterior", paginaAnterior);

    }

   public void limpiarListasValor() {

   }

   @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarOdiCabecera.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct ControlOdiCabecera: " + e);
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
            String pagActual = "odicabecera";
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
        limpiarListasValor();fc.getApplication().getNavigationHandler().handleNavigation(fc, null, pag);
    }

    public void recibirParametros(Short anio, Short mes, BigInteger secuenciaEmpresa) {
        anioParametro = BigInteger.valueOf(anio);
        mesParametro = BigInteger.valueOf(mes);
        secuenciaParametro = secuenciaEmpresa;
        System.out.println("valor del año: " + anioParametro);
        System.out.println("valor del mes : " + mesParametro);
        System.out.println("secuencia de la empresa :" + secuenciaParametro);
//        listaNovedades = null;
//        getListaNovedades();
//        contarRegistros();

    }

    public void recibirPag(String pag) {
        paginaAnterior = pag;
        listaOdiCabecera = null;
        getListaOdiCabecera();
        if (listaOdiCabecera == null) {
            modificarInfoRegistroOdicabecera(0);
        } else {
            modificarInfoRegistroOdicabecera(listaOdiCabecera.size());
        }
        listaOdiDetalles = null;
        getListaOdiCabecera();
        if (listaOdiDetalles == null) {
            modificarInfoRegistroOdiDetalles(0);
        } else {
            modificarInfoRegistroOdiDetalles(listaOdiDetalles.size());
        }

        cargarLovEmpresas();
        modificarInfoRegistroEmpresas(lovEmpresas.size());
        nuevaOdiCabecera.setAnio(anioParametro);
        nuevaOdiCabecera.setMes(mesParametro);
//        if (listaOdiCabecera == null) {
//            if (listaOdiCabecera.isEmpty()) {
//                odiCabeceraSeleccionada = null;
//            }
//        } else {
//            odiCabeceraSeleccionada = listaOdiCabecera.get(0);
//        }
    }

    public String volverPagAnterior() {
        return paginaAnterior;
    }

    public void cambiarIndice(OdisCabeceras odiscabecera, int celda) {
        if (permitirIndex == true) {
            odiCabeceraSeleccionada = odiscabecera;
            cualCelda = celda;
            odiCabeceraSeleccionada.getSecuencia();
            if (cualCelda == 0) {
//                habilitarBotonLov();
                if (odiCabeceraSeleccionada.getEmpresa() == null) {
                    odiCabeceraSeleccionada.setEmpresa(new Empresas());
                } else {
                    odiCabeceraSeleccionada.getEmpresa();
                }
            } else if (cualCelda == 1) {
//                habilitarBotonLov();
                if (odiCabeceraSeleccionada.getSucursalpila() == null) {
                    odiCabeceraSeleccionada.setSucursalpila(new SucursalesPila());
                } else {
                    odiCabeceraSeleccionada.getSucursalpila();
                }
            } else if (cualCelda == 2) {
//                habilitarBotonLov();
                if (odiCabeceraSeleccionada.getTercero() == null) {
                    odiCabeceraSeleccionada.setTercero(new Terceros());
                } else {
                    odiCabeceraSeleccionada.getTercero();
                }
            } else if (cualCelda == 3) {
                //                habilitarBotonLov();
                if (odiCabeceraSeleccionada.getTipoentidad() == null) {
                    odiCabeceraSeleccionada.setTipoentidad(new TiposEntidades());
                } else {
                    odiCabeceraSeleccionada.getTipoentidad();
                }
            } else if (cualCelda == 4) {
                odiCabeceraSeleccionada.getValortotal();
            } else if (cualCelda == 5) {
//                deshabilitarBotonLov();
                odiCabeceraSeleccionada.getNumeroautorizacion();
            } else if (cualCelda == 6) {
//                deshabilitarBotonLov();
                odiCabeceraSeleccionada.getOrigenincapacidad();
            } else if (cualCelda == 7) {
//                deshabilitarBotonLov();
                odiCabeceraSeleccionada.getIncluirdetalles();
            } else if (cualCelda == 8) {
//                deshabilitarBotonLov();
                odiCabeceraSeleccionada.getAnio();
            } else if (cualCelda == 9) {
//                deshabilitarBotonLov();
                odiCabeceraSeleccionada.getMes();
            }
        }
    }

    public void cambiarIndiceDetalles(OdisDetalles odisdetalles, int celda) {
        if (permitirIndex == true) {
            OdiDetallesSeleccionada = odisdetalles;
            cualCelda = celda;
            OdiDetallesSeleccionada.getSecuencia();
            if (cualCelda == 0) {
//                habilitarBotonLov();
                if (OdiDetallesSeleccionada.getEmpleado() == null) {
                    OdiDetallesSeleccionada.setEmpleado(new Empleados());
                } else {
                    OdiDetallesSeleccionada.getEmpleado();
                }
            } else if (cualCelda == 1) {
//                habilitarBotonLov();
                OdiDetallesSeleccionada.getNumerodetalle();
            } else if (cualCelda == 2) {
                OdiDetallesSeleccionada.getObservacion();
            } else if (cualCelda == 3) {
                OdiDetallesSeleccionada.getValor();
            } else if (cualCelda == 4) {
                if (OdiDetallesSeleccionada.getRelacionincapacidad() == null) {
                    OdiDetallesSeleccionada.setRelacionincapacidad(new RelacionesIncapacidades());
                } else {
                    OdiDetallesSeleccionada.getRelacionincapacidad();
                }
            } else if (cualCelda == 5) {
//                deshabilitarBotonLov();
                OdiDetallesSeleccionada.getOdicabecera().getNumeroautorizacion();
            } else if (cualCelda == 6) {
//                deshabilitarBotonLov();
                OdiDetallesSeleccionada.getParametropresupuesto();
            }
        }
    }

    public void listaValoresBoton() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (odiCabeceraSeleccionada != null) {
            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:empresasDialogo");
                RequestContext.getCurrentInstance().execute("PF('empresasDialogo').show()");
                tipoActualizacion = 0;
            }
            if (cualCelda == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:sucursalesDialogo");
                RequestContext.getCurrentInstance().execute("PF('sucursalesDialogo').show()");
                tipoActualizacion = 0;
            }
            if (cualCelda == 2) {
                RequestContext.getCurrentInstance().update("formularioDialogos:tercerosDialogo");
                RequestContext.getCurrentInstance().execute("PF('tercerosDialogo').show()");
                tipoActualizacion = 0;
            }
            if (cualCelda == 3) {
                RequestContext.getCurrentInstance().update("formularioDialogos:tiposEntidadesDialogo");
                RequestContext.getCurrentInstance().execute("PF('tiposEntidadesDialogo').show()");
                tipoActualizacion = 0;
            }
        } else if (OdiDetallesSeleccionada != null) {
            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:empleadosDialogo");
                RequestContext.getCurrentInstance().execute("PF('empleadosDialogo').show()");
                tipoActualizacion = 0;
            }
            if (cualCelda == 4) {
                RequestContext.getCurrentInstance().update("formularioDialogos:relacionesDialogo");
                RequestContext.getCurrentInstance().execute("PF('relacionesDialogo').show()");
                tipoActualizacion = 0;
            }
        }
    }

    public void editarCelda() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (odiCabeceraSeleccionada != null) {
            editarOdiCabecera = odiCabeceraSeleccionada;
            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarEmpresa");
                RequestContext.getCurrentInstance().execute("PF('editarEmpresa').show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarSucursal");
                RequestContext.getCurrentInstance().execute("PF('editarSucursal').show()");
                cualCelda = -1;
            } else if (cualCelda == 2) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarTercero");
                RequestContext.getCurrentInstance().execute("PF('editarTercero').show()");

            } else if (cualCelda == 3) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarTipoEntidad");
                RequestContext.getCurrentInstance().execute("PF('editarTipoEntidad').show()");
                cualCelda = -1;
            } else if (cualCelda == 4) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarValor");
                RequestContext.getCurrentInstance().execute("PF('editarValor').show()");
            } else if (cualCelda == 5) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarNumAutorizacion");
                RequestContext.getCurrentInstance().execute("PF('editarNumAutorizacion').show()");
                cualCelda = -1;
            } else if (cualCelda == 6) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarOrigen");
                RequestContext.getCurrentInstance().execute("PF('editarOrigen').show()");
            } else if (cualCelda == 7) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarDetalle");
                RequestContext.getCurrentInstance().execute("PF('editarDetalle').show()");
            } else if (cualCelda == 8) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarAnio");
                RequestContext.getCurrentInstance().execute("PF('editarAnio').show()");
                cualCelda = -1;
            } else if (cualCelda == 9) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarMes");
                RequestContext.getCurrentInstance().execute("PF('editarMes').show()");
                cualCelda = -1;

            }
        } else if (OdiDetallesSeleccionada != null) {
            editarOdiDetalles = OdiDetallesSeleccionada;
            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarEmpleado");
                RequestContext.getCurrentInstance().execute("PF('editarEmpleado').show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarNumDetalle");
                RequestContext.getCurrentInstance().execute("PF('editarNumDetalle').show()");
                cualCelda = -1;
            } else if (cualCelda == 2) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarObservacion");
                RequestContext.getCurrentInstance().execute("PF('editarObservacion').show()");

            } else if (cualCelda == 3) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarValorDetalle");
                RequestContext.getCurrentInstance().execute("PF('editarValorDetalle').show()");
                cualCelda = -1;
            } else if (cualCelda == 4) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarRelacion");
                RequestContext.getCurrentInstance().execute("PF('editarRelacion').show()");
            } else if (cualCelda == 5) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarNumCertificado");
                RequestContext.getCurrentInstance().execute("PF('editarNumCertificado').show()");
                cualCelda = -1;
            } else if (cualCelda == 6) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarValorCobrado");
                RequestContext.getCurrentInstance().execute("PF('editarValorCobrado').show()");
            }
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void seleccionarOrigen(String origen, int tipoNuevo) {
        if (tipoNuevo == 1) {
            if (origen.equals("EG")) {
                nuevaOdiCabecera.setOrigenincapacidad("EG");
            } else if (origen.equals("AT")) {
                nuevaOdiCabecera.setOrigenincapacidad("AT");
            } else if (origen.equals("MA")) {
                nuevaOdiCabecera.setOrigenincapacidad("MA");
            } else if (origen.equals("EP")) {
                nuevaOdiCabecera.setOrigenincapacidad("EP");
            }
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoorigen");
        } else {
            if (origen.equals("EG")) {
                duplicarOdiCabecera.setOrigenincapacidad("EG");
            } else if (origen.equals("AT")) {
                duplicarOdiCabecera.setOrigenincapacidad("AT");
            } else if (origen.equals("MA")) {
                duplicarOdiCabecera.setOrigenincapacidad("MA");
            } else if (origen.equals("EP")) {
                duplicarOdiCabecera.setOrigenincapacidad("EP");
            }
            RequestContext.getCurrentInstance().update("formularioDialogos:dupOrigen");
        }
    }

    public void limpiarNuevaOdiCabecera() {
        nuevaOdiCabecera = new OdisCabeceras();
        nuevaOdiCabecera.setEmpresa(new Empresas());
        nuevaOdiCabecera.setSucursalpila(new SucursalesPila());
        nuevaOdiCabecera.setTercero(new Terceros());
        nuevaOdiCabecera.setTipoentidad(new TiposEntidades());
        nuevaOdiCabecera.setCheckDetalles(false);
        nuevaOdiCabecera.setAnio(anioParametro);
        nuevaOdiCabecera.setMes(mesParametro);
    }

    public void limpiarDuplicarOdiCabecera() {
        duplicarOdiCabecera = new OdisCabeceras();
        duplicarOdiCabecera.setEmpresa(new Empresas());
        duplicarOdiCabecera.setSucursalpila(new SucursalesPila());
        duplicarOdiCabecera.setTercero(new Terceros());
        duplicarOdiCabecera.setTipoentidad(new TiposEntidades());
        duplicarOdiCabecera.setCheckDetalles(false);
        duplicarOdiCabecera.setAnio(anioParametro);
        duplicarOdiCabecera.setMes(mesParametro);
    }

    public void limpiarNuevaOdiDetalles() {
        nuevaOdiDetalles = new OdisDetalles();
        nuevaOdiDetalles.setEmpleado(new Empleados());
        nuevaOdiDetalles.setRelacionincapacidad(new RelacionesIncapacidades());
        nuevaOdiDetalles.setOdicabecera(odiCabeceraSeleccionada);
    }

    public void limpiarDuplicarOdiDetalles() {
        duplicarOdiDetalles = new OdisDetalles();
        duplicarOdiDetalles.setEmpleado(new Empleados());
        duplicarOdiDetalles.setRelacionincapacidad(new RelacionesIncapacidades());
        duplicarOdiDetalles.setOdicabecera(odiCabeceraSeleccionada);
    }

    public void asignarIndex(OdisCabeceras odicabecera, int dlg, int LND) {
        odiCabeceraSeleccionada = odicabecera;
        RequestContext context = RequestContext.getCurrentInstance();
        tipoActualizacion = LND;
        if (dlg == 0) {
            cargarLovEmpresas();
            RequestContext.getCurrentInstance().update("formularioDialogos:empresasDialogo");
            RequestContext.getCurrentInstance().execute("PF('empresasDialogo').show()");
        } else if (dlg == 1) {
            cargarLovSucursales();
            RequestContext.getCurrentInstance().update("formularioDialogos:sucursalesDialogo");
            RequestContext.getCurrentInstance().execute("PF('sucursalesDialogo').show()");
        } else if (dlg == 2) {
            cargarLovTerceros();
            RequestContext.getCurrentInstance().update("formularioDialogos:tercerosDialogo");
            RequestContext.getCurrentInstance().execute("PF('tercerosDialogo').show()");
        } else if (dlg == 3) {
            cargarLovTiposEntidades();
            RequestContext.getCurrentInstance().update("formularioDialogos:tiposEntidadesDialogo");
            RequestContext.getCurrentInstance().execute("PF('tiposEntidadesDialogo').show()");
        }
    }

    public void asignarIndexDetalles(OdisDetalles odidetalles, int dlg, int LND) {
        OdiDetallesSeleccionada = odidetalles;
        RequestContext context = RequestContext.getCurrentInstance();
        tipoActualizacion = LND;
        if (dlg == 1) {
            cargarLovEmpleados();
            RequestContext.getCurrentInstance().update("formularioDialogos:empleadosDialogo");
            RequestContext.getCurrentInstance().execute("PF('empleadosDialogo').show()");
        } else if (dlg == 2) {
            RequestContext.getCurrentInstance().update("formularioDialogos:relacionesDialogo");
            RequestContext.getCurrentInstance().execute("PF('relacionesDialogo').show()");
        }
    }

    public void seleccionarExportarPDF() throws IOException {
        if (OdiDetallesSeleccionada != null) {
            validarExportPDFDetalles();
        } else if (odiCabeceraSeleccionada != null) {
            validarExportPDF();
        } else if (OdiDetallesSeleccionada == null && odiCabeceraSeleccionada == null) {
            validarExportPDF();
        }
    }

    public void seleccionarExportarXLS() throws IOException {
        if (OdiDetallesSeleccionada != null) {
            exportDetallesXLS();
        } else if (odiCabeceraSeleccionada != null) {
            exportXLS();
        } else if (OdiDetallesSeleccionada == null && odiCabeceraSeleccionada == null) {
            exportXLS();
        }
    }

    public void seleccionarExportarXML() throws IOException {
        if (OdiDetallesSeleccionada != null) {
            exportXMLTablaDetalles();
        } else if (odiCabeceraSeleccionada != null) {
            exportXMLTabla();
        } else if (OdiDetallesSeleccionada == null && odiCabeceraSeleccionada == null) {
            exportXMLTabla();
        }
    }

    public void nombreXML() {
        if (OdiDetallesSeleccionada != null) {
            exportXMLNombreArchivoDetalles();
        } else if (odiCabeceraSeleccionada != null) {
            exportXMLNombreArchivo();
        } else if (OdiDetallesSeleccionada == null && odiCabeceraSeleccionada == null) {
            exportXMLNombreArchivo();
        }
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosCabeceraExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "OdiCabeceraPDF", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportPDFDetalles() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosDetallesExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "OdiDetallePDF", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosCabeceraExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "OdiCabeceraXLS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportDetallesXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosDetallesExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "OdiDetallesXLS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public String exportXMLTabla() {
        String tabla = "";
        if (odiCabeceraSeleccionada != null) {
            tabla = ":formExportar:datosCabeceraExportar";
        }

        return tabla;
    }

    public String exportXMLNombreArchivo() {
        String nombre = "";
        if (odiCabeceraSeleccionada != null) {
            nombre = "odiCabecera_XML";
        }

        return nombre;
    }

    public String exportXMLTablaDetalles() {
        String tabla = "";
        if (OdiDetallesSeleccionada != null) {
            tabla = ":formExportar:datosDetallesExportar";
        }

        return tabla;
    }

    public String exportXMLNombreArchivoDetalles() {
        String nombre = "";
        if (OdiDetallesSeleccionada != null) {
            nombre = "odiDetalle_XML";
        }

        return nombre;
    }

    public void validarExportPDF() throws IOException {
        if (odiCabeceraSeleccionada != null) {
            exportPDF();
        }
    }

    public void validarExportPDFDetalles() throws IOException {
        if (OdiDetallesSeleccionada != null) {
            exportPDFDetalles();
        }
    }

    public void guardarCambios() {
        try {
            if (guardado == false) {
                if (!listaOdiCabeceraBorrar.isEmpty()) {
                    for (int i = 0; i < listaOdiCabeceraBorrar.size(); i++) {
                        System.out.println("entra a borrar");
                        administrarOdiCabecera.borrar(listaOdiCabeceraBorrar.get(i));
                        System.out.println("sale de borrar");
                    }
                    listaOdiCabeceraBorrar.clear();
                }
                if (!listaOdiCabeceraCrear.isEmpty()) {
                    System.out.println("entra a crear");
                    for (int i = 0; i < listaOdiCabeceraCrear.size(); i++) {
                        administrarOdiCabecera.crear(listaOdiCabeceraCrear.get(i));
                    }
                    listaOdiCabeceraCrear.clear();
                    System.out.println("sale de crear");
                }
                if (!listaOdiCabeceraModificar.isEmpty()) {
                    System.out.println("entra a modificar");
                    for (int i = 0; i < listaOdiCabeceraModificar.size(); i++) {
                        administrarOdiCabecera.editar(listaOdiCabeceraModificar.get(i));
                    }
                    System.out.println("sale de modificar");
                    listaOdiCabeceraModificar.clear();
                }

                if (!listaOdiDetallesBorrar.isEmpty()) {
                    for (int i = 0; i < listaOdiDetallesBorrar.size(); i++) {
                        System.out.println("entra a borrar Detalles");
                        administrarOdiCabecera.borrarDetalle(listaOdiDetallesBorrar.get(i));
                        System.out.println("sale de borrar Detalles");
                    }
                    listaOdiDetallesBorrar.clear();
                }
                if (!listaOdiDetallesCrear.isEmpty()) {
                    System.out.println("entra a crear Detalles");
                    for (int i = 0; i < listaOdiDetallesCrear.size(); i++) {
                        administrarOdiCabecera.crearDetalle(listaOdiDetallesCrear.get(i));
                    }
                    listaOdiDetallesCrear.clear();
                    System.out.println("sale de crear Detalles");
                }
                if (!listaOdiDetallesModificar.isEmpty()) {
                    System.out.println("entra a modificar Detalles");
                    for (int i = 0; i < listaOdiDetallesModificar.size(); i++) {
                        administrarOdiCabecera.editarDetalle(listaOdiDetallesModificar.get(i));
                    }
                    System.out.println("sale de modificar Detalles");
                    listaOdiDetallesModificar.clear();
                }
                listaOdiCabecera = null;
                getListaOdiCabecera();
                modificarInfoRegistroOdicabecera(listaOdiCabecera.size());
                listaOdiDetalles = null;
                getListaOdiDetalles();
                modificarInfoRegistroOdiDetalles(listaOdiDetalles.size());
                guardado = true;
                permitirIndex = true;
                RequestContext.getCurrentInstance().update("form:datosCabecera");
//                RequestContext.getCurrentInstance().update("form:datosDetalles");
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
                FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                RequestContext.getCurrentInstance().update("form:growl");
                System.out.println("guarda datos con éxito");
                odiCabeceraSeleccionada = null;
//                OdiDetallesSeleccionada = null;
            }
        } catch (Exception e) {
            System.out.println("Error guardarCambios : " + e.toString());
            FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado, intente nuevamente.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void borrarNovedades() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (odiCabeceraSeleccionada != null) {
            if (!listaOdiCabeceraModificar.isEmpty() && listaOdiCabeceraModificar.contains(odiCabeceraSeleccionada)) {
                listaOdiCabeceraModificar.remove(listaOdiCabeceraModificar.indexOf(odiCabeceraSeleccionada));
                listaOdiCabeceraBorrar.add(odiCabeceraSeleccionada);
            } else if (!listaOdiCabeceraCrear.isEmpty() && listaOdiCabeceraCrear.contains(odiCabeceraSeleccionada)) {
                listaOdiCabeceraCrear.remove(listaOdiCabeceraCrear.indexOf(odiCabeceraSeleccionada));
                listaOdiCabeceraBorrar.add(odiCabeceraSeleccionada);
            } else {
                listaOdiCabeceraBorrar.add(odiCabeceraSeleccionada);
            }
            listaOdiCabecera.remove(odiCabeceraSeleccionada);

            if (tipoLista == 1) {
                filtrarListaOdiCabecera.remove(odiCabeceraSeleccionada);
            }
            RequestContext.getCurrentInstance().update("form:datosCabecera");
            odiCabeceraSeleccionada = null;

            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        } else if (OdiDetallesSeleccionada != null) {
            if (!listaOdiDetallesModificar.isEmpty() && listaOdiDetallesModificar.contains(OdiDetallesSeleccionada)) {
                listaOdiDetallesModificar.remove(listaOdiDetallesModificar.indexOf(OdiDetallesSeleccionada));
                listaOdiDetallesBorrar.add(OdiDetallesSeleccionada);
            } else if (!listaOdiDetallesCrear.isEmpty() && listaOdiDetallesCrear.contains(OdiDetallesSeleccionada)) {
                listaOdiDetallesCrear.remove(listaOdiDetallesCrear.indexOf(OdiDetallesSeleccionada));
                listaOdiDetallesBorrar.add(OdiDetallesSeleccionada);
            } else {
                listaOdiDetallesBorrar.add(OdiDetallesSeleccionada);
            }
            listaOdiDetalles.remove(OdiDetallesSeleccionada);

            if (tipoLista == 1) {
                filtrarListaOdiDetalles.remove(OdiDetallesSeleccionada);
            }
            RequestContext.getCurrentInstance().update("form:datosDetalles");
            OdiDetallesSeleccionada = null;

            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void activarCtrlF11() {
        if (bandera == 0) {
            //ODIS CABECERA
            empresa = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCabecera:empresa");
            empresa.setFilterStyle("width: 85% !important");
            sucursal = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCabecera:sucursal");
            sucursal.setFilterStyle("width: 85% !important");
            nombreTercero = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCabecera:tercero");
            nombreTercero.setFilterStyle("width: 85% !important");
            tipoEntidad = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCabecera:tipoentidad");
            tipoEntidad.setFilterStyle("width: 85% !important");
            valor = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCabecera:valor");
            valor.setFilterStyle("width: 85% !important");
            numautorizacion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCabecera:numautorizacion");
            numautorizacion.setFilterStyle("width: 85% !important");
            origen = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCabecera:origenIncapacidad");
            origen.setFilterStyle("width: 85% !important");
            detalle = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCabecera:detalle");
            detalle.setFilterStyle("width: 85% !important");
            anio = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCabecera:anio");
            anio.setFilterStyle("width: 85% !important");
            mes = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCabecera:mes");
            mes.setFilterStyle("width: 85% !important");

            //ODIS DETALLES
            empleado = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDetalles:empleado");
            empleado.setFilterStyle("width: 85% !important");
            numdetalle = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDetalles:numdetalle");
            numdetalle.setFilterStyle("width: 85% !important");
            observacion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDetalles:observacion");
            observacion.setFilterStyle("width: 85% !important");
            valordetalle = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDetalles:valor");
            valordetalle.setFilterStyle("width: 85% !important");
            relacion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDetalles:relacion");
            relacion.setFilterStyle("width: 85% !important");
            numcertificado = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDetalles:numcertificado");
            numcertificado.setFilterStyle("width: 85% !important");
            valorcobrado = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDetalles:valorcobrado");
            valorcobrado.setFilterStyle("width: 85% !important");
            altotabla = 89;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosCabecera");
            RequestContext.getCurrentInstance().update("form:datosDetalles");
            bandera = 1;

        } else if (bandera == 1) {
            // ODIS CABECERA
            empresa = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCabecera:empresa");
            empresa.setFilterStyle("display: none; visibility: hidden;");
            sucursal = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCabecera:sucursal");
            sucursal.setFilterStyle("display: none; visibility: hidden;");
            nombreTercero = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCabecera:tercero");
            nombreTercero.setFilterStyle("display: none; visibility: hidden;");
            tipoEntidad = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCabecera:tipoentidad");
            tipoEntidad.setFilterStyle("display: none; visibility: hidden;");
            valor = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCabecera:valor");
            valor.setFilterStyle("display: none; visibility: hidden;");
            numautorizacion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCabecera:numautorizacion");
            numautorizacion.setFilterStyle("display: none; visibility: hidden;");
            origen = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCabecera:origenIncapacidad");
            origen.setFilterStyle("display: none; visibility: hidden;");
            detalle = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCabecera:detalle");
            detalle.setFilterStyle("display: none; visibility: hidden;");
            anio = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCabecera:anio");
            anio.setFilterStyle("display: none; visibility: hidden;");
            mes = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCabecera:mes");
            mes.setFilterStyle("display: none; visibility: hidden;");

            //ODIS DETALLES
            empleado = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDetalles:empleado");
            empleado.setFilterStyle("display: none; visibility: hidden;");
            numdetalle = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDetalles:numdetalle");
            numdetalle.setFilterStyle("display: none; visibility: hidden;");
            observacion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDetalles:observacion");
            observacion.setFilterStyle("display: none; visibility: hidden;");
            valordetalle = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDetalles:valor");
            valordetalle.setFilterStyle("display: none; visibility: hidden;");
            relacion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDetalles:relacion");
            relacion.setFilterStyle("display: none; visibility: hidden;");
            numcertificado = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDetalles:numcertificado");
            numcertificado.setFilterStyle("display: none; visibility: hidden;");
            valorcobrado = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDetalles:valorcobrado");
            valorcobrado.setFilterStyle("display: none; visibility: hidden;");
            altotabla = 109;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosCabecera");
            RequestContext.getCurrentInstance().update("form:datosDetalles");
            bandera = 0;
            filtrarListaOdiCabecera = null;
            filtrarListaOdiDetalles = null;
        }
    }

    public void agregarNuevaOdiCabecera() {
        int pasa = 0;
        mensajeValidacion = "";
        RequestContext context = RequestContext.getCurrentInstance();
        FacesContext c = FacesContext.getCurrentInstance();
//        odiCabeceraSeleccionada = null;

        if (bandera == 1) {
            altotabla = 85;
            empresa = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCabecera:empresa");
            empresa.setFilterStyle("display: none; visibility: hidden;");
            sucursal = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCabecera:sucursal");
            sucursal.setFilterStyle("display: none; visibility: hidden;");
            nombreTercero = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCabecera:tercero");
            nombreTercero.setFilterStyle("display: none; visibility: hidden;");
            tipoEntidad = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCabecera:tipoentidad");
            tipoEntidad.setFilterStyle("display: none; visibility: hidden;");
            valor = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCabecera:valor");
            valor.setFilterStyle("display: none; visibility: hidden;");
            numautorizacion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCabecera:numautorizacion");
            numautorizacion.setFilterStyle("display: none; visibility: hidden;");
            origen = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCabecera:origenincapacidad");
            origen.setFilterStyle("display: none; visibility: hidden;");
            detalle = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCabecera:detalle");
            detalle.setFilterStyle("display: none; visibility: hidden;");
            anio = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCabecera:anio");
            anio.setFilterStyle("display: none; visibility: hidden;");
            mes = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCabecera:mes");
            mes.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:novedadesAuto");
            bandera = 0;
            tipoLista = 0;
        }
        k++;
        l = BigInteger.valueOf(k);
        nuevaOdiCabecera.setSecuencia(l);
        listaOdiCabeceraCrear.add(nuevaOdiCabecera);
        if (listaOdiCabecera == null) {
            listaOdiCabecera = new ArrayList<OdisCabeceras>();
        }
        listaOdiCabecera.add(nuevaOdiCabecera);
        odiCabeceraSeleccionada = nuevaOdiCabecera;

        modificarInfoRegistroOdicabecera(listaOdiCabecera.size());
        RequestContext.getCurrentInstance().update("form:datosCabecera");
        RequestContext.getCurrentInstance().execute("PF('nuevaCabecera').hide()");
        if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
        nuevaOdiCabecera = new OdisCabeceras();
        nuevaOdiCabecera.setAnio(anioParametro);
        nuevaOdiCabecera.setMes(mesParametro);
        nuevaOdiCabecera.setTipoentidad(new TiposEntidades());
        nuevaOdiCabecera.setTercero(new Terceros());
        nuevaOdiCabecera.setEmpresa(new Empresas());
        nuevaOdiCabecera.setSucursalpila(new SucursalesPila());
    }

    public void agregarNuevaOdiDetalles() {
        int pasa = 0;
        mensajeValidacion = "";
        RequestContext context = RequestContext.getCurrentInstance();
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 1) {
            altotabla = 85;
            empleado = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDetalles:empleado");
            empleado.setFilterStyle("display: none; visibility: hidden;");
            numdetalle = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDetalles:numdetalle");
            numdetalle.setFilterStyle("display: none; visibility: hidden;");
            observacion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDetalles:observacion");
            observacion.setFilterStyle("display: none; visibility: hidden;");
            valor = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDetalles:valor");
            valor.setFilterStyle("display: none; visibility: hidden;");
            relacion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDetalles:relacion");
            relacion.setFilterStyle("display: none; visibility: hidden;");
            numcertificado = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDetalles:numcertificado");
            numcertificado.setFilterStyle("display: none; visibility: hidden;");
            valorcobrado = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDetalles:valorcobrado");
            valorcobrado.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:novedadesAuto");
            bandera = 0;
            tipoLista = 0;
        }
        k++;
        l = BigInteger.valueOf(k);
        nuevaOdiDetalles.setSecuencia(l);
        listaOdiDetallesCrear.add(nuevaOdiDetalles);
        if (listaOdiDetalles == null) {
            listaOdiDetalles = new ArrayList<OdisDetalles>();
        }
        listaOdiDetalles.add(nuevaOdiDetalles);
        OdiDetallesSeleccionada = nuevaOdiDetalles;
        nuevaOdiDetalles = new OdisDetalles();
        nuevaOdiDetalles.setEmpleado(new Empleados());
        modificarInfoRegistroOdiDetalles(listaOdiDetalles.size());
        RequestContext.getCurrentInstance().update("form:datosDetalles");
        RequestContext.getCurrentInstance().execute("PF('nuevoDetalle').hide()");
        if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
    }

    public void duplicandoOdiCabecera() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (odiCabeceraSeleccionada != null) {
            duplicarOdiCabecera = new OdisCabeceras();

            if (tipoLista == 0) {
                duplicarOdiCabecera.setEmpresa(odiCabeceraSeleccionada.getEmpresa());
                duplicarOdiCabecera.setSucursalpila(odiCabeceraSeleccionada.getSucursalpila());
                duplicarOdiCabecera.setTercero(odiCabeceraSeleccionada.getTercero());
                duplicarOdiCabecera.setTipoentidad(odiCabeceraSeleccionada.getTipoentidad());
                duplicarOdiCabecera.setValortotal(odiCabeceraSeleccionada.getValortotal());
                duplicarOdiCabecera.setNumeroautorizacion(odiCabeceraSeleccionada.getNumeroautorizacion());
                duplicarOdiCabecera.setOrigenincapacidad(odiCabeceraSeleccionada.getOrigenincapacidad());
                duplicarOdiCabecera.setIncluirdetalles(odiCabeceraSeleccionada.getIncluirdetalles());
                duplicarOdiCabecera.setAnio(odiCabeceraSeleccionada.getAnio());
                duplicarOdiCabecera.setMes(odiCabeceraSeleccionada.getMes());
            }
            if (tipoLista == 1) {
                duplicarOdiCabecera.setEmpresa(odiCabeceraSeleccionada.getEmpresa());
                duplicarOdiCabecera.setSucursalpila(odiCabeceraSeleccionada.getSucursalpila());
                duplicarOdiCabecera.setTercero(odiCabeceraSeleccionada.getTercero());
                duplicarOdiCabecera.setTipoentidad(odiCabeceraSeleccionada.getTipoentidad());
                duplicarOdiCabecera.setValortotal(odiCabeceraSeleccionada.getValortotal());
                duplicarOdiCabecera.setNumeroautorizacion(odiCabeceraSeleccionada.getNumeroautorizacion());
                duplicarOdiCabecera.setOrigenincapacidad(odiCabeceraSeleccionada.getOrigenincapacidad());
                duplicarOdiCabecera.setIncluirdetalles(odiCabeceraSeleccionada.getIncluirdetalles());
                duplicarOdiCabecera.setAnio(odiCabeceraSeleccionada.getAnio());
                duplicarOdiCabecera.setMes(odiCabeceraSeleccionada.getMes());
            }
            // deshabilitarBotonLov();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCabecera");
            RequestContext.getCurrentInstance().execute("PF('duplicarCabecera').show()");
        } else if (OdiDetallesSeleccionada != null) {
            duplicarOdiDetalles = new OdisDetalles();

            if (tipoLista == 0) {
                duplicarOdiDetalles.setEmpleado(OdiDetallesSeleccionada.getEmpleado());
                duplicarOdiDetalles.setNumerodetalle(OdiDetallesSeleccionada.getNumerodetalle());
                duplicarOdiDetalles.setObservacion(OdiDetallesSeleccionada.getObservacion());
                duplicarOdiDetalles.setValor(OdiDetallesSeleccionada.getValor());
                duplicarOdiDetalles.setRelacionincapacidad(OdiDetallesSeleccionada.getRelacionincapacidad());
                duplicarOdiDetalles.setValor(OdiDetallesSeleccionada.getValor());
            }
            if (tipoLista == 1) {
                duplicarOdiDetalles.setEmpleado(OdiDetallesSeleccionada.getEmpleado());
                duplicarOdiDetalles.setNumerodetalle(OdiDetallesSeleccionada.getNumerodetalle());
                duplicarOdiDetalles.setObservacion(OdiDetallesSeleccionada.getObservacion());
                duplicarOdiDetalles.setValor(OdiDetallesSeleccionada.getValor());
                duplicarOdiDetalles.setRelacionincapacidad(OdiDetallesSeleccionada.getRelacionincapacidad());
                duplicarOdiDetalles.setValor(OdiDetallesSeleccionada.getValor());
            }
            //  deshabilitarBotonLov();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarOdiDetalle");
            RequestContext.getCurrentInstance().execute("PF('duplicarOdiDetalle').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('formularioDialogos:seleccionarRegistro').show()");
        }
    }

    public void confirmarDuplicar() {
        FacesContext c = FacesContext.getCurrentInstance();
        k++;
        l = BigInteger.valueOf(k);
        duplicarOdiCabecera.setSecuencia(l);
//        duplicarOdiCabecera.setPersona(empleado.getPersona());
        listaOdiCabeceraCrear.add(duplicarOdiCabecera);
        listaOdiCabecera.add(duplicarOdiCabecera);
        odiCabeceraSeleccionada = duplicarOdiCabecera;
        getListaOdiCabecera();
        modificarInfoRegistroOdicabecera(listaOdiCabecera.size());
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosCabecera");
        RequestContext.getCurrentInstance().execute("PF('duplicarCabecera').hide()");
        if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
        if (bandera == 1) {
            altotabla = 85;
            empresa = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCabecera:empresa");
            empresa.setFilterStyle("display: none; visibility: hidden;");
            sucursal = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCabecera:sucursal");
            sucursal.setFilterStyle("display: none; visibility: hidden;");
            nombreTercero = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCabecera:tercero");
            nombreTercero.setFilterStyle("display: none; visibility: hidden;");
            tipoEntidad = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCabecera:tipoentidad");
            tipoEntidad.setFilterStyle("display: none; visibility: hidden;");
            valor = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCabecera:valor");
            valor.setFilterStyle("display: none; visibility: hidden;");
            numautorizacion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCabecera:numautorizacion");
            numautorizacion.setFilterStyle("display: none; visibility: hidden;");
            origen = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCabecera:origenincapacidad");
            origen.setFilterStyle("display: none; visibility: hidden;");
            detalle = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCabecera:detalle");
            detalle.setFilterStyle("display: none; visibility: hidden;");
            anio = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCabecera:anio");
            anio.setFilterStyle("display: none; visibility: hidden;");
            mes = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCabecera:mes");
            mes.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:detallesCabecera");
            bandera = 0;
            filtrarListaOdiCabecera = null;
            tipoLista = 0;
        }
        duplicarOdiCabecera = new OdisCabeceras();
//        deshabilitarBotonLov();
    }

    public void confirmarDuplicarDetalles() {
        FacesContext c = FacesContext.getCurrentInstance();
        k++;
        l = BigInteger.valueOf(k);
        duplicarOdiDetalles.setSecuencia(l);
//        duplicarOdiCabecera.setPersona(empleado.getPersona());
        listaOdiDetallesCrear.add(duplicarOdiDetalles);
        listaOdiDetalles.add(duplicarOdiDetalles);
        OdiDetallesSeleccionada = duplicarOdiDetalles;
        getListaOdiDetalles();
        modificarInfoRegistroOdiDetalles(listaOdiDetalles.size());
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosDetalles");
        RequestContext.getCurrentInstance().execute("PF('duplicarOdiDetalle').hide()");
        if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
        if (bandera == 1) {
            altotabla = 85;
            empleado = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDetalles:empleado");
            empleado.setFilterStyle("display: none; visibility: hidden;");
            numdetalle = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDetalles:numdetalle");
            numdetalle.setFilterStyle("display: none; visibility: hidden;");
            observacion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDetalles:observacion");
            observacion.setFilterStyle("display: none; visibility: hidden;");
            relacion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDetalles:relacion");
            relacion.setFilterStyle("display: none; visibility: hidden;");
            numcertificado = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDetalles:numcertificado");
            numcertificado.setFilterStyle("display: none; visibility: hidden;");
            valorcobrado = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDetalles:valorcobrado");
            RequestContext.getCurrentInstance().update("form:datosDetalles");

            bandera = 0;
            filtrarListaOdiDetalles = null;
            tipoLista = 0;
        }
        duplicarOdiDetalles = new OdisDetalles();
//        deshabilitarBotonLov();
    }

    public void modificarOdiCabecera(OdisCabeceras odicabecera) {
        RequestContext context = RequestContext.getCurrentInstance();
        odiCabeceraSeleccionada = odicabecera;
        if (tipoLista == 0) {
            if (!listaOdiCabeceraCrear.contains(odiCabeceraSeleccionada)) {

                if (listaOdiCabeceraModificar.isEmpty()) {
                    listaOdiCabeceraModificar.add(odiCabeceraSeleccionada);
                } else if (!listaOdiCabeceraModificar.contains(odiCabeceraSeleccionada)) {
                    listaOdiCabeceraModificar.add(odiCabeceraSeleccionada);
                }
                if (guardado == true) {
                    guardado = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
            }
        } else if (!listaOdiCabeceraCrear.contains(odiCabeceraSeleccionada)) {

            if (listaOdiCabeceraModificar.isEmpty()) {
                listaOdiCabeceraModificar.add(odiCabeceraSeleccionada);
            } else if (!listaOdiCabeceraModificar.contains(odiCabeceraSeleccionada)) {
                listaOdiCabeceraModificar.add(odiCabeceraSeleccionada);
            }
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        }
        RequestContext.getCurrentInstance().update("form:datosCabecera");
    }

    public void modificarOdiCabecera(OdisCabeceras odicabecera, String confirmarCambio, String valorConfirmar) {
        odiCabeceraSeleccionada = odicabecera;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("TIPOENTIDAD")) {
            odiCabeceraSeleccionada.getTipoentidad().setNombre(nuevaOdiCabecera.getEmpresa().getNombre());
            for (int i = 0; i < lovTiposEntidades.size(); i++) {
                if (lovTiposEntidades.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                odiCabeceraSeleccionada.setTipoentidad(lovTiposEntidades.get(indiceUnicoElemento));
                lovTiposEntidades.clear();
                getLovTiposEntidades();
            } else {
                permitirIndex = false;
                RequestContext.getCurrentInstance().update("formularioDialogos:tiposEntidadesDialogo");
                RequestContext.getCurrentInstance().execute("PF('tiposEntidadesDialogo').show()");
                tipoActualizacion = 0;
            }
        }
        if (confirmarCambio.equalsIgnoreCase("TERCERO")) {
            if (!valorConfirmar.isEmpty()) {
                odiCabeceraSeleccionada.getTercero().setNit(odiCabeceraSeleccionada.getTercero().getNit());
                for (int i = 0; i < lovTerceros.size(); i++) {
                    if (lovTerceros.get(i).getStrNit().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    odiCabeceraSeleccionada.setTercero(lovTerceros.get(indiceUnicoElemento));
                    lovTerceros.clear();
                    getLovTerceros();
                } else {
                    permitirIndex = false;
                    RequestContext.getCurrentInstance().update("formularioDialogos:tercerosDialogo");
                    RequestContext.getCurrentInstance().execute("PF('tercerosDialogo').show()");
                    tipoActualizacion = 0;
                }
            } else {
                coincidencias = 1;
                if (tipoLista == 0) {
                    odiCabeceraSeleccionada.setTercero(new Terceros());
                } else {
                    odiCabeceraSeleccionada.setTercero(new Terceros());
                }
                lovTerceros.clear();
                getLovTerceros();
            }
        }
        if (confirmarCambio.equalsIgnoreCase("EMPRESA")) {
            odiCabeceraSeleccionada.getEmpresa().setNombre(odiCabeceraSeleccionada.getEmpresa().getNombre());
            for (int i = 0; i < lovEmpresas.size(); i++) {
                if (lovEmpresas.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                odiCabeceraSeleccionada.setEmpresa(lovEmpresas.get(indiceUnicoElemento));
                lovEmpresas.clear();
                getLovEmpresas();
            } else {
                permitirIndex = false;
                RequestContext.getCurrentInstance().update("formularioDialogos:empresasDialogo");
                RequestContext.getCurrentInstance().execute("PF('empresasDialogo').show()");
                tipoActualizacion = 0;
            }
        }
        if (confirmarCambio.equalsIgnoreCase("SUCURSAL")) {
            odiCabeceraSeleccionada.getSucursalpila().setDescripcion(odiCabeceraSeleccionada.getSucursalpila().getDescripcion());
            for (int i = 0; i < lovSucursales.size(); i++) {
                if (lovSucursales.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                odiCabeceraSeleccionada.setSucursalpila(lovSucursales.get(indiceUnicoElemento));
                //                listaSucursales.clear();
                //                getListaSucursales();
            } else {
                permitirIndex = false;
                RequestContext.getCurrentInstance().update("formularioDialogos:sucursalesDialogo");
                RequestContext.getCurrentInstance().execute("PF('sucursalesDialogo').show()");
                tipoActualizacion = 0;
            }
        }
        if (coincidencias == 1) {
            if (listaOdiCabeceraModificar.isEmpty()) {
                listaOdiCabeceraModificar.add(odiCabeceraSeleccionada);
            } else if (!listaOdiCabeceraModificar.contains(odiCabeceraSeleccionada)) {
                listaOdiCabeceraModificar.add(odiCabeceraSeleccionada);
            }
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        }
        RequestContext.getCurrentInstance().update("form:datosCabecera");
    }

    public void modificarOdiDetalles(OdisDetalles odidetalle, String confirmarCambio, String valorConfirmar) {
        OdiDetallesSeleccionada = odidetalle;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("EMPLEADO")) {
            OdiDetallesSeleccionada.getEmpleado().getPersona().setNombreCompleto(nuevaOdiDetalles.getEmpleado().getPersona().getNombreCompleto());
            for (int i = 0; i < lovEmpleados.size(); i++) {
                if (lovEmpleados.get(i).getPersona().getNombreCompleto().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                OdiDetallesSeleccionada.setEmpleado(lovEmpleados.get(indiceUnicoElemento));
                lovEmpleados.clear();
                getLovEmpleados();
            } else {
                permitirIndex = false;
                RequestContext.getCurrentInstance().update("formularioDialogos:empleadosDialogo");
                RequestContext.getCurrentInstance().execute("PF('empleadosDialogo').show()");
                tipoActualizacion = 0;
            }
        }
//        if (confirmarCambio.equalsIgnoreCase("RELACION")) {
//            if (!valorConfirmar.isEmpty()) {
////                OdiDetallesSeleccionada.getRelacionincapacidad().set.setNit(OdiDetallesSeleccionada.getTercero().getNit());
//                for (int i = 0; i < lovRelaciones.size(); i++) {
//                    if (lovRelaciones.get(i).getStrNit().startsWith(valorConfirmar.toUpperCase())) {
//                        indiceUnicoElemento = i;
//                        coincidencias++;
//                    }
//                }
//                if (coincidencias == 1) {
//                    OdiDetallesSeleccionada.setTercero(lovRelaciones.get(indiceUnicoElemento));
//                    lovRelaciones.clear();
//                    getLovTerceros();
//                } else {
//                    permitirIndex = false;
//                    RequestContext.getCurrentInstance().update("formularioDialogos:tercerosDialogo");
//                    RequestContext.getCurrentInstance().execute("PF('tercerosDialogo').show()");
//                    tipoActualizacion = 0;
//                }
//            } else {
//                coincidencias = 1;
//                if (tipoLista == 0) {
//                    OdiDetallesSeleccionada.setTercero(new Terceros());
//                } else {
//                    OdiDetallesSeleccionada.setTercero(new Terceros());
//                }
//                lovTerceros.clear();
//                getLovTerceros();
//            }
//        }
        if (coincidencias == 1) {
            if (listaOdiDetallesModificar.isEmpty()) {
                listaOdiDetallesModificar.add(OdiDetallesSeleccionada);
            } else if (!listaOdiDetallesModificar.contains(OdiDetallesSeleccionada)) {
                listaOdiDetallesModificar.add(OdiDetallesSeleccionada);
            }
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        }
        RequestContext.getCurrentInstance().update("form:datosDetalles");
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (odiCabeceraSeleccionada != null) {
            int result = administrarRastros.obtenerTabla(odiCabeceraSeleccionada.getSecuencia(), "ODISCABECERAS");
            if (result == 1) {
                RequestContext.getCurrentInstance().execute("PF('errorObjetosDB').show()");
            } else if (result == 2) {
                RequestContext.getCurrentInstance().execute("PF('confirmarRastro').show()");
            } else if (result == 3) {
                RequestContext.getCurrentInstance().execute("PF('errorRegistroRastro').show()");
            } else if (result == 4) {
                RequestContext.getCurrentInstance().execute("PF('errorTablaConRastro').show()");
            } else if (result == 5) {
                RequestContext.getCurrentInstance().execute("PF('errorTablaSinRastro').show()");
            }
        } else if (administrarRastros.verificarHistoricosTabla("ODISCABECERAS")) {
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
    }

    public void actualizarEmpresa() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            odiCabeceraSeleccionada.setEmpresa(empresaSeleccionada);
            if (!listaOdiCabeceraCrear.contains(odiCabeceraSeleccionada)) {
                if (listaOdiCabeceraModificar.isEmpty()) {
                    listaOdiCabeceraModificar.add(odiCabeceraSeleccionada);
                } else if (!listaOdiCabeceraModificar.contains(odiCabeceraSeleccionada)) {
                    listaOdiCabeceraModificar.add(odiCabeceraSeleccionada);
                }
            }
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            permitirIndex = true;
            //deshabilitarBotonLov();
            RequestContext.getCurrentInstance().update("form:datosCabecera");
        } else if (tipoActualizacion == 1) {
            nuevaOdiCabecera.setEmpresa(empresaSeleccionada);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaOdiCabecera");
        } else if (tipoActualizacion == 2) {
            duplicarOdiCabecera.setEmpresa(empresaSeleccionada);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarOdiCabecera");
        }
        auxiliar = empresaSeleccionada.getSecuencia();
        filtrarLovEmpresas = null;
        empresaSeleccionada = null;
        aceptar = true;
        tipoActualizacion = -1;
        context.reset("formularioDialogos:lovEmpresas:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovEmpresas').clearFilters()");
        RequestContext.getCurrentInstance().update("formularioDialogos:empresasDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovEmpresas");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarD");
        RequestContext.getCurrentInstance().update("formularioDialogos:sucursalesDialogo");
        RequestContext.getCurrentInstance().execute("PF('empresasDialogo').hide()");
    }

    public void cancelarCambioEmpresa() {
        filtrarLovEmpresas = null;
        aceptar = true;
        tipoActualizacion = -1;
        permitirIndex = true;
        empresaSeleccionada = null;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:lovEmpresas:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovEmpresas').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('empresasDialogo').hide()");
        RequestContext.getCurrentInstance().update("formularioDialogos:empresasDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovEmpresas");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarD");
    }

    public void actualizarTercero() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (tipoLista == 0) {
                odiCabeceraSeleccionada.setTercero(terceroSeleccionado);
                if (!listaOdiCabeceraCrear.contains(odiCabeceraSeleccionada)) {
                    if (listaOdiCabeceraModificar.isEmpty()) {
                        listaOdiCabeceraModificar.add(odiCabeceraSeleccionada);
                    } else if (!listaOdiCabeceraModificar.contains(odiCabeceraSeleccionada)) {
                        listaOdiCabeceraModificar.add(odiCabeceraSeleccionada);
                    }
                }
            } else {
                odiCabeceraSeleccionada.setTercero(terceroSeleccionado);
                if (!listaOdiCabeceraCrear.contains(odiCabeceraSeleccionada)) {
                    if (listaOdiCabeceraModificar.isEmpty()) {
                        listaOdiCabeceraModificar.add(odiCabeceraSeleccionada);
                    } else if (!listaOdiCabeceraModificar.contains(odiCabeceraSeleccionada)) {
                        listaOdiCabeceraModificar.add(odiCabeceraSeleccionada);
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            permitirIndex = true;
            //deshabilitarBotonLov();
            RequestContext.getCurrentInstance().update("form:datosCabecera");
        } else if (tipoActualizacion == 1) {
            nuevaOdiCabecera.setTercero(terceroSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaOdiCabecera");
        } else if (tipoActualizacion == 2) {
            duplicarOdiCabecera.setTercero(terceroSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarOdiCabecera");
        }
        filtrarLovTerceros = null;
        terceroSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        context.reset("formularioDialogos:lovTerceros:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovTerceros').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('tercerosDialogo').hide()");
        RequestContext.getCurrentInstance().update("formularioDialogos:tercerosDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovTerceros");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarT");

    }

    public void cancelarCambioTercero() {
        filtrarLovTerceros = null;
        terceroSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:lovTerceros:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovTerceros').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('tercerosDialogo').hide()");
        RequestContext.getCurrentInstance().update("formularioDialogos:tercerosDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovTerceros");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarT");
    }

    public void actualizarSucursal() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (tipoLista == 0) {
                odiCabeceraSeleccionada.setSucursalpila(sucursalSeleccionada);
                if (!listaOdiCabeceraCrear.contains(odiCabeceraSeleccionada)) {
                    if (listaOdiCabeceraModificar.isEmpty()) {
                        listaOdiCabeceraModificar.add(odiCabeceraSeleccionada);
                    } else if (!listaOdiCabeceraModificar.contains(odiCabeceraSeleccionada)) {
                        listaOdiCabeceraModificar.add(odiCabeceraSeleccionada);
                    }
                }
            } else {
                odiCabeceraSeleccionada.setSucursalpila(sucursalSeleccionada);
                if (!listaOdiCabeceraCrear.contains(odiCabeceraSeleccionada)) {
                    if (listaOdiCabeceraModificar.isEmpty()) {
                        listaOdiCabeceraModificar.add(odiCabeceraSeleccionada);
                    } else if (!listaOdiCabeceraModificar.contains(odiCabeceraSeleccionada)) {
                        listaOdiCabeceraModificar.add(odiCabeceraSeleccionada);
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
                //RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            permitirIndex = true;
            //deshabilitarBotonLov();
            RequestContext.getCurrentInstance().update("form:datosCabecera");
        } else if (tipoActualizacion == 1) {
            nuevaOdiCabecera.setSucursalpila(sucursalSeleccionada);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaOdiCabecera");
        } else if (tipoActualizacion == 2) {
            duplicarOdiCabecera.setSucursalpila(sucursalSeleccionada);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarOdiCabecera");
        }
        filtrarLovSucursales = null;
        sucursalSeleccionada = null;
        aceptar = true;
        tipoActualizacion = -1;
        context.reset("formularioDialogos:lovSucursales:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovSucursales').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('sucursalesDialogo').hide()");
        RequestContext.getCurrentInstance().update("formularioDialogos:sucursalesDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovSucursales");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarS");

    }

    public void cancelarCambioSucursal() {
        filtrarLovSucursales = null;
        sucursalSeleccionada = null;
        aceptar = true;
        tipoActualizacion = -1;
        permitirIndex = true;
        lovSucursales = null;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:lovSucursales:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovSucursales').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('sucursalesDialogo').hide()");
        RequestContext.getCurrentInstance().update("formularioDialogos:sucursalesDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovSucursales");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarS");
    }

    public void actualizarTipoEntidad() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (tipoLista == 0) {
                odiCabeceraSeleccionada.setTipoentidad(tipoEntidadSeleccionada);
                if (!listaOdiCabeceraCrear.contains(odiCabeceraSeleccionada)) {
                    if (listaOdiCabeceraModificar.isEmpty()) {
                        listaOdiCabeceraModificar.add(odiCabeceraSeleccionada);
                    } else if (!listaOdiCabeceraModificar.contains(odiCabeceraSeleccionada)) {
                        listaOdiCabeceraModificar.add(odiCabeceraSeleccionada);
                    }
                }
            } else {
                odiCabeceraSeleccionada.setTipoentidad(tipoEntidadSeleccionada);
                if (!listaOdiCabeceraCrear.contains(odiCabeceraSeleccionada)) {
                    if (listaOdiCabeceraModificar.isEmpty()) {
                        listaOdiCabeceraModificar.add(odiCabeceraSeleccionada);
                    } else if (!listaOdiCabeceraModificar.contains(odiCabeceraSeleccionada)) {
                        listaOdiCabeceraModificar.add(odiCabeceraSeleccionada);
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
                //RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            permitirIndex = true;
            //deshabilitarBotonLov();
            RequestContext.getCurrentInstance().update("form:datosCabecera");
        } else if (tipoActualizacion == 1) {
            nuevaOdiCabecera.setTipoentidad(tipoEntidadSeleccionada);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaOdiCabecera");
        } else if (tipoActualizacion == 2) {
            duplicarOdiCabecera.setTipoentidad(tipoEntidadSeleccionada);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarOdiCabecera");
        }
        filtrarLovTiposEntidades = null;
        tipoEntidadSeleccionada = null;
        aceptar = true;
        tipoActualizacion = -1;
        context.reset("formularioDialogos:lovTiposEntidades:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovTiposEntidades').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('tiposEntidadesDialogo').hide()");
        RequestContext.getCurrentInstance().update("formularioDialogos:tiposEntidadesDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovTiposEntidades");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarTE");

    }

    public void cancelarCambioTipoEntidad() {
        filtrarLovTiposEntidades = null;
        tipoEntidadSeleccionada = null;
        aceptar = true;
        tipoActualizacion = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:lovTiposEntidades:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovTiposEntidades').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('tiposEntidadesDialogo').hide()");
        RequestContext.getCurrentInstance().update("formularioDialogos:tiposEntidadesDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovTiposEntidades");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarTE");
    }

    public void actualizarEmpleados() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (tipoLista == 0) {
                OdiDetallesSeleccionada.setEmpleado(empleadoSeleccionado);
                if (!listaOdiDetallesCrear.contains(OdiDetallesSeleccionada)) {
                    if (listaOdiCabeceraModificar.isEmpty()) {
                        listaOdiDetallesModificar.add(OdiDetallesSeleccionada);
                    } else if (!listaOdiDetallesModificar.contains(OdiDetallesSeleccionada)) {
                        listaOdiDetallesModificar.add(OdiDetallesSeleccionada);
                    }
                }
            } else {
                OdiDetallesSeleccionada.setEmpleado(empleadoSeleccionado);
                if (!listaOdiDetallesCrear.contains(OdiDetallesSeleccionada)) {
                    if (listaOdiDetallesModificar.isEmpty()) {
                        listaOdiDetallesModificar.add(OdiDetallesSeleccionada);
                    } else if (!listaOdiDetallesModificar.contains(OdiDetallesSeleccionada)) {
                        listaOdiDetallesModificar.add(OdiDetallesSeleccionada);
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
                //RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            permitirIndex = true;
            //deshabilitarBotonLov();
            RequestContext.getCurrentInstance().update("form:datosDetalles");
        } else if (tipoActualizacion == 1) {
            nuevaOdiDetalles.setEmpleado(empleadoSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoOdiDetalle");
        } else if (tipoActualizacion == 2) {
            duplicarOdiDetalles.setEmpleado(empleadoSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarOdiDetalle");
        }
        filtrarLovEmpleados = null;
        empleadoSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        context.reset("formularioDialogos:LOVEmpleados:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVEmpleados').clearFilters()");
        RequestContext.getCurrentInstance().update("formularioDialogos:empleadosDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:LOVEmpleados");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarE");
        RequestContext.getCurrentInstance().execute("PF('empleadosDialogo').hide()");

    }

    public void cancelarCambioEmpleados() {
        filtrarLovEmpleados = null;
        empleadoSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:LOVEmpleados:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVEmpleados').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('empleadosDialogo').hide()");
        RequestContext.getCurrentInstance().update("formularioDialogos:empleadosDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:LOVEmpleados");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarTE");
    }

    public void cargarLovEmpleados() {
        lovEmpleados = null;
        getLovEmpleados();
    }

    public void cargarLovEmpresas() {
        lovEmpresas = null;
        getLovEmpresas();
    }

    public void cargarLovTiposEntidades() {
        lovTiposEntidades = null;
        getLovTiposEntidades();
    }

    public void cargarLovRelaciones() {
        lovRelaciones = null;
        getLovRelaciones();
    }

    public void cargarLovSucursales() {
        lovSucursales = null;
        getLovSucursales();
    }

    public void cargarLovTerceros() {
        lovTerceros = null;
        getLovTerceros();
    }

    public void autocompletarNuevoyDuplicado(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("EMPRESA")) {
            if (tipoNuevo == 1) {
                nuevaOdiCabecera.getEmpresa().setNombre(nuevaOdiCabecera.getEmpresa().getNombre());
            } else if (tipoNuevo == 2) {
                duplicarOdiCabecera.getEmpresa().setNombre(duplicarOdiCabecera.getEmpresa().getNombre());
            }
            for (int i = 0; i < lovEmpresas.size(); i++) {
                if (lovEmpresas.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevaOdiCabecera.setEmpresa(lovEmpresas.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevaEmpresa");
                } else if (tipoNuevo == 2) {
                    duplicarOdiCabecera.setEmpresa(lovEmpresas.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarEmpresa");
                }
                lovEmpresas.clear();
                getLovEmpresas();
            } else {
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevaEmpresa");
                } else if (tipoNuevo == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarEmpresa");
                }
            }
            RequestContext.getCurrentInstance().update("formularioDialogos:empresasDialogo");
            RequestContext.getCurrentInstance().execute("PF('empresasDialogo').show()");
        }
        if (confirmarCambio.equalsIgnoreCase("SUCURSAL")) {
            if (tipoNuevo == 1) {
                nuevaOdiCabecera.getSucursalpila().setDescripcion(nuevaOdiCabecera.getSucursalpila().getDescripcion());
            } else if (tipoNuevo == 2) {
                duplicarOdiCabecera.getSucursalpila().setDescripcion(nuevaOdiCabecera.getSucursalpila().getDescripcion());
            }
            for (int i = 0; i < lovSucursales.size(); i++) {
                if (lovSucursales.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevaOdiCabecera.setSucursalpila(lovSucursales.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevaSucursal");
                } else if (tipoNuevo == 2) {
                    duplicarOdiCabecera.setSucursalpila(lovSucursales.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarSucursal");
                }
            } else {
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevaSucursal");
                } else if (tipoNuevo == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarSucursal");
                }
            }
            RequestContext.getCurrentInstance().update("formularioDIalogos:sucursalesDialogo");
            RequestContext.getCurrentInstance().execute("PF('sucursalesDialogo').show()");
        }

        if (confirmarCambio.equalsIgnoreCase("TERCERO")) {
            if (tipoNuevo == 1) {
                nuevaOdiCabecera.getTercero().setNombre(nuevaOdiCabecera.getTercero().getNombre());
            } else if (tipoNuevo == 2) {
                duplicarOdiCabecera.getTercero().setNombre(nuevaOdiCabecera.getTercero().getNombre());
            }
            for (int i = 0; i < lovTerceros.size(); i++) {
                if (lovTerceros.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevaOdiCabecera.setTercero(lovTerceros.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTercero");
                } else if (tipoNuevo == 2) {
                    duplicarOdiCabecera.setTercero(lovTerceros.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTercero");
                }
                lovTerceros.clear();
                getLovTerceros();
            } else {
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTercero");
                } else if (tipoNuevo == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTipoEntidad");
                }
                RequestContext.getCurrentInstance().update("formularioDialogos:tercerosDialogo");
                RequestContext.getCurrentInstance().execute("PF('tercerosDialogo').show()");
            }
        }

        if (confirmarCambio.equalsIgnoreCase("TIPOENTIDAD")) {
            if (tipoNuevo == 1) {
                nuevaOdiCabecera.getTipoentidad().setNombre(nuevaOdiCabecera.getTipoentidad().getNombre());
            } else if (tipoNuevo == 2) {
                duplicarOdiCabecera.getTipoentidad().setNombre(nuevaOdiCabecera.getTipoentidad().getNombre());
            }
            for (int i = 0; i < lovTiposEntidades.size(); i++) {
                if (lovTiposEntidades.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevaOdiCabecera.setTipoentidad(lovTiposEntidades.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTipoEntidad");
                } else if (tipoNuevo == 2) {
                    duplicarOdiCabecera.setTipoentidad(lovTiposEntidades.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTipoEntidad");
                }
                lovTiposEntidades.clear();
                getLovTiposEntidades();
            } else {
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTipoEntidad");
                } else if (tipoNuevo == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTipoEntidad");
                }
            }
            RequestContext.getCurrentInstance().update("formularioDialogos:tiposEntidadesDialogo");
            RequestContext.getCurrentInstance().execute("PF('tiposEntidadesDialogo').show()");
        }

        if (confirmarCambio.equalsIgnoreCase("EMPLEADO")) {
            if (tipoNuevo == 1) {
                nuevaOdiDetalles.getEmpleado().getPersona().setNombreCompleto(nuevaOdiDetalles.getEmpleado().getPersona().getNombreCompleto());
            } else if (tipoNuevo == 2) {
                duplicarOdiDetalles.getEmpleado().getPersona().setNombreCompleto(nuevaOdiDetalles.getEmpleado().getPersona().getNombreCompleto());
            }
            for (int i = 0; i < lovEmpleados.size(); i++) {
                if (lovEmpleados.get(i).getPersona().getNombreCompleto().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevaOdiDetalles.setEmpleado(lovEmpleados.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoEmpleado");
                } else if (tipoNuevo == 2) {
                    duplicarOdiDetalles.setEmpleado(lovEmpleados.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarEmpleado");
                }
                lovEmpleados.clear();
                getLovEmpleados();
            } else {
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoEmpleado");
                } else if (tipoNuevo == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarEmpleado");
                }
            }
            RequestContext.getCurrentInstance().update("formularioDialogos:empleadosDialogo");
            RequestContext.getCurrentInstance().execute("PF('empleadosDialogo').show()");
        }

    }

    public void valoresBackupAutocompletar(int tipoNuevo, String Campo) {
        if (Campo.equals("EMPRESA")) {
            if (tipoNuevo == 1) {
                nuevaOdiCabecera.getEmpresa().getNombre();
            } else if (tipoNuevo == 2) {
                duplicarOdiCabecera.getEmpresa().getNombre();
            }
        }
        if (Campo.equals("SUCURSAL")) {
            if (tipoNuevo == 1) {
                nuevaOdiCabecera.getSucursalpila().getDescripcion();
            } else if (tipoNuevo == 2) {
                duplicarOdiCabecera.getSucursalpila().getDescripcion();
            }
        }
        if (Campo.equals("TIPOENTIDAD")) {
            if (tipoNuevo == 1) {
                nuevaOdiCabecera.getTipoentidad().getNombre();
            } else if (tipoNuevo == 2) {
                duplicarOdiCabecera.getTipoentidad().getNombre();
            }
        }
        if (Campo.equals("TERCERO")) {
            if (tipoNuevo == 1) {
                nuevaOdiCabecera.getTercero().getNit();
            } else if (tipoNuevo == 2) {
                duplicarOdiCabecera.getTercero().getNit();
            }
        }

        if (Campo.equals("EMPLEADO")) {
            if (tipoNuevo == 1) {
                nuevaOdiDetalles.getEmpleado();
            } else if (tipoNuevo == 2) {
                duplicarOdiDetalles.getEmpleado();
            }
        }

    }

    public void cancelarModificacion() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 1) {
            altotabla = 85;
            empresa = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCabecera:empresa");
            empresa.setFilterStyle("display: none; visibility: hidden;");
            sucursal = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCabecera:sucursal");
            sucursal.setFilterStyle("display: none; visibility: hidden;");
            nombreTercero = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCabecera:tercero");
            nombreTercero.setFilterStyle("display: none; visibility: hidden;");
            tipoEntidad = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCabecera:tipoentidad");
            tipoEntidad.setFilterStyle("display: none; visibility: hidden;");
            valor = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCabecera:valor");
            valor.setFilterStyle("display: none; visibility: hidden;");
            numautorizacion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCabecera:numautorizacion");
            numautorizacion.setFilterStyle("display: none; visibility: hidden;");
            origen = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCabecera:origenincapacidad");
            origen.setFilterStyle("display: none; visibility: hidden;");
            detalle = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCabecera:detalle");
            detalle.setFilterStyle("display: none; visibility: hidden;");
            anio = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCabecera:anio");
            anio.setFilterStyle("display: none; visibility: hidden;");
            mes = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCabecera:mes");
            mes.setFilterStyle("display: none; visibility: hidden;");
            empleado = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDetalles:empleado");
            empleado.setFilterStyle("display: none; visibility: hidden;");
            numdetalle = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDetalles:numdetalle");
            numdetalle.setFilterStyle("display: none; visibility: hidden;");
            observacion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDetalles:observacion");
            observacion.setFilterStyle("display: none; visibility: hidden;");
            relacion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDetalles:relacion");
            relacion.setFilterStyle("display: none; visibility: hidden;");
            numcertificado = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDetalles:numcertificado");
            numcertificado.setFilterStyle("display: none; visibility: hidden;");
            valorcobrado = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDetalles:valorcobrado");
            bandera = 0;
            tipoLista = 0;
        }

        listaOdiCabeceraBorrar.clear();
        listaOdiDetallesBorrar.clear();
        listaOdiCabeceraCrear.clear();
        listaOdiDetallesCrear.clear();
        listaOdiCabeceraModificar.clear();
        listaOdiDetallesModificar.clear();
        listaOdiCabecera = null;
        listaOdiDetalles = null;
        getListaOdiCabecera();
        getListaOdiDetalles();
        if (listaOdiCabecera != null) {
            modificarInfoRegistroOdicabecera(listaOdiCabecera.size());
        }
        if (listaOdiDetalles != null) {
            modificarInfoRegistroOdiDetalles(listaOdiDetalles.size());
        }
        odiCabeceraSeleccionada = null;
        OdiDetallesSeleccionada = null;
        aceptar = true;
        guardado = true;
        tipoLista = 0;
        permitirIndex = true;
        limpiarNuevaOdiCabecera();
        limpiarNuevaOdiDetalles();
        limpiarDuplicarOdiCabecera();
        limpiarDuplicarOdiDetalles();
        altotabla = 109;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosCabecera");
        RequestContext.getCurrentInstance().update("form:datosDetalles");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");

    }

    public void guardarSalir() {
        guardarCambios();
        salir();
    }

    public void cancelarSalir() {
        cancelarModificacion();
        salir();
    }

    public void salir() {  limpiarListasValor();
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 1) {
            altotabla = 85;
            empresa = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCabecera:empresa");
            empresa.setFilterStyle("display: none; visibility: hidden;");
            sucursal = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCabecera:sucursal");
            sucursal.setFilterStyle("display: none; visibility: hidden;");
            nombreTercero = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCabecera:tercero");
            nombreTercero.setFilterStyle("display: none; visibility: hidden;");
            tipoEntidad = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCabecera:tipoentidad");
            tipoEntidad.setFilterStyle("display: none; visibility: hidden;");
            valor = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCabecera:valor");
            valor.setFilterStyle("display: none; visibility: hidden;");
            numautorizacion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCabecera:numautorizacion");
            numautorizacion.setFilterStyle("display: none; visibility: hidden;");
            origen = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCabecera:origenincapacidad");
            origen.setFilterStyle("display: none; visibility: hidden;");
            detalle = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCabecera:detalle");
            detalle.setFilterStyle("display: none; visibility: hidden;");
            anio = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCabecera:anio");
            anio.setFilterStyle("display: none; visibility: hidden;");
            mes = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCabecera:mes");
            mes.setFilterStyle("display: none; visibility: hidden;");
            empleado = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDetalles:empleado");
            empleado.setFilterStyle("display: none; visibility: hidden;");
            numdetalle = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDetalles:numdetalle");
            numdetalle.setFilterStyle("display: none; visibility: hidden;");
            observacion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDetalles:observacion");
            observacion.setFilterStyle("display: none; visibility: hidden;");
            relacion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDetalles:relacion");
            relacion.setFilterStyle("display: none; visibility: hidden;");
            numcertificado = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDetalles:numcertificado");
            numcertificado.setFilterStyle("display: none; visibility: hidden;");
            valorcobrado = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDetalles:valorcobrado");
            bandera = 0;
            tipoLista = 0;
        }
        listaOdiCabeceraBorrar.clear();
        listaOdiDetallesBorrar.clear();
        listaOdiCabeceraCrear.clear();
        listaOdiDetallesCrear.clear();
        listaOdiCabeceraModificar.clear();
        listaOdiDetallesModificar.clear();
        listaOdiCabecera = null;
        listaOdiDetalles = null;
        odiCabeceraSeleccionada = null;
        guardado = true;

    }

    public void posicionOtro() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, String> map = context.getExternalContext().getRequestParameterMap();
        String celda = map.get("celda"); // name attribute of node 
        String registro = map.get("registro"); // type attribute of node 
        int indice = Integer.parseInt(registro);
        int columna = Integer.parseInt(celda);
        odiCabeceraSeleccionada = listaOdiCabecera.get(indice);
        cambiarIndice(odiCabeceraSeleccionada, columna);
    }

    public void habilitarBotonLov() {
        activarlov = false;
        RequestContext.getCurrentInstance().update("form:listaValores");
    }

    public void deshabilitarBotonLov() {
        activarlov = true;
        RequestContext.getCurrentInstance().update("form:listaValores");
    }

    public void eventoFiltrar() {
        if (tipoLista == 1) {
            tipoLista = 0;
        }
        modificarInfoRegistroOdicabecera(filtrarListaOdiCabecera.size());
    }

    public void recolectarINP() {

    }

    public void checkdetalles() {
        if (nuevaOdiCabecera.isCheckDetalles() == false) {
            nuevaOdiCabecera.setIncluirdetalles("N");
        } else if (nuevaOdiCabecera.isCheckDetalles() == true) {
            nuevaOdiCabecera.setIncluirdetalles("S");
        }
    }

    public void eventoFiltrarDetalles() {
        if (tipoLista == 1) {
            tipoLista = 0;
        }
        modificarInfoRegistroOdiDetalles(filtrarListaOdiDetalles.size());

    }

    public void eventoFiltrarEmpresas() {
        modificarInfoRegistroEmpresas(filtrarLovEmpresas.size());
    }

    public void eventoFiltrarTerceros() {
        modificarInfoRegistroTerceros(filtrarLovTerceros.size());
    }

    public void eventoFiltrarSucursales() {
        modificarInfoRegistroSucursales(filtrarLovSucursales.size());
    }

    public void eventoFiltrarTiposEntidades() {
        modificarInfoRegistroTipoEntidad(filtrarLovTiposEntidades.size());
    }

    public void eventoFiltrarEmpleados() {
        modificarInfoRegistroEmpleados(filtrarLovEmpleados.size());
    }

    public void eventoFiltrarRelaciones() {
        modificarInfoRegistroRelaciones(filtrarLovRelaciones.size());
    }

    public void modificarInfoRegistroOdicabecera(int valor) {
        infoRegistroOdiCabecera = String.valueOf(valor);
        RequestContext.getCurrentInstance().update("form:infoRegistroCabecera");
    }

    public void modificarInfoRegistroOdiDetalles(int valor) {
        infoRegistroOdiDetalles = String.valueOf(valor);
        RequestContext.getCurrentInstance().update("form:infoRegistroDetalle");
    }

    public void modificarInfoRegistroEmpresas(int valor) {
        infoRegistroEmpresas = String.valueOf(valor);
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroEmpresa");
    }

    public void modificarInfoRegistroTerceros(int valor) {
        infoRegistroTerceros = String.valueOf(valor);
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroTercero");
    }

    public void modificarInfoRegistroSucursales(int valor) {
        infoRegistroSucursales = String.valueOf(valor);
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroSucursal");
    }

    public void modificarInfoRegistroTipoEntidad(int valor) {
        infoRegistroTipoEntidad = String.valueOf(valor);
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroTipoEntidad");
    }

    public void modificarInfoRegistroEmpleados(int valor) {
        infoRegistroEmpleados = String.valueOf(valor);
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroEmpleados");
    }

    public void modificarInfoRegistroRelaciones(int valor) {
        infoRegistroRelacionesIncapacidades = String.valueOf(valor);
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroRelaciones");
    }

    public void validarCheckDetalles() {
        if (odiCabeceraSeleccionada != null) {
            if (odiCabeceraSeleccionada.getIncluirdetalles().equals("S")) {
                RequestContext.getCurrentInstance().update("formularioDialogos:nuevoDetalle");
                RequestContext.getCurrentInstance().execute("PF('nuevoDetalle').show()");
            } else {
                RequestContext.getCurrentInstance().execute("PF('form:validarCheckDetalles').show()");
            }
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void habilitartabla() {
        editarDetalles = false;
        RequestContext.getCurrentInstance().update("form:datosDetalles");
    }

    public void deshabilitartabla() {
        editarDetalles = true;
        RequestContext.getCurrentInstance().update("form:datosDetalles");
    }

    ////SETS Y GETS///////////
    public boolean isAceptar() {
        return aceptar;
    }

    public void setAceptar(boolean aceptar) {
        this.aceptar = aceptar;
    }

    public boolean isActivarlov() {
        return activarlov;
    }

    public void setActivarlov(boolean activarlov) {
        this.activarlov = activarlov;
    }

    public int getAltotabla() {
        return altotabla;
    }

    public void setAltotabla(int altotabla) {
        this.altotabla = altotabla;
    }

    public List<OdisCabeceras> getListaOdiCabecera() {
        if (listaOdiCabecera == null) {
            listaOdiCabecera = administrarOdiCabecera.listaNovedades(anioParametro, mesParametro, secuenciaParametro);
        }
        return listaOdiCabecera;
    }

    public void setListaOdiCabecera(List<OdisCabeceras> listaOdiCabecera) {
        this.listaOdiCabecera = listaOdiCabecera;
    }

    public List<OdisCabeceras> getFiltrarListaOdiCabecera() {
        return filtrarListaOdiCabecera;
    }

    public void setFiltrarListaOdiCabecera(List<OdisCabeceras> filtrarListaOdiCabecera) {
        this.filtrarListaOdiCabecera = filtrarListaOdiCabecera;
    }

    public List<Empresas> getLovEmpresas() {
        if (lovEmpresas == null) {
            lovEmpresas = administrarOdiCabecera.lovEmpresas();
        }
        return lovEmpresas;
    }

    public void setLovEmpresas(List<Empresas> lovEmpresas) {
        this.lovEmpresas = lovEmpresas;
    }

    public List<Empresas> getFiltrarLovEmpresas() {
        return filtrarLovEmpresas;
    }

    public void setFiltrarLovEmpresas(List<Empresas> filtrarLovEmpresas) {
        this.filtrarLovEmpresas = filtrarLovEmpresas;
    }

    public List<TiposEntidades> getLovTiposEntidades() {
        if (lovTiposEntidades == null) {
            lovTiposEntidades = administrarOdiCabecera.lovtiposEntidades(anioParametro, mesParametro);
        }
        return lovTiposEntidades;
    }

    public void setLovTiposEntidades(List<TiposEntidades> lovTiposEntidades) {
        this.lovTiposEntidades = lovTiposEntidades;
    }

    public List<TiposEntidades> getFiltrarLovTiposEntidades() {
        return filtrarLovTiposEntidades;
    }

    public void setFiltrarLovTiposEntidades(List<TiposEntidades> filtrarLovTiposEntidades) {
        this.filtrarLovTiposEntidades = filtrarLovTiposEntidades;
    }

    public List<Terceros> getLovTerceros() {
        if (lovTerceros == null) {
            lovTerceros = administrarOdiCabecera.lovTerceros(anioParametro, mesParametro);
        }
        return lovTerceros;
    }

    public void setLovTerceros(List<Terceros> lovTerceros) {
        this.lovTerceros = lovTerceros;
    }

    public List<Terceros> getFiltrarLovTerceros() {
        return filtrarLovTerceros;
    }

    public void setFiltrarLovTerceros(List<Terceros> filtrarLovTerceros) {
        this.filtrarLovTerceros = filtrarLovTerceros;
    }

    public List<RelacionesIncapacidades> getLovRelaciones() {

        //lovRelaciones = administrarOdiCabecera.lovRelacionesIncapacidades(secuenciaParametro);
        return lovRelaciones;
    }

    public void setLovRelaciones(List<RelacionesIncapacidades> lovRelaciones) {
        this.lovRelaciones = lovRelaciones;
    }

    public List<RelacionesIncapacidades> getFiltrarLovRelaciones() {
        return filtrarLovRelaciones;
    }

    public void setFiltrarLovRelaciones(List<RelacionesIncapacidades> filtrarLovRelaciones) {
        this.filtrarLovRelaciones = filtrarLovRelaciones;
    }

    public RelacionesIncapacidades getRelacionSeleccionada() {
        return relacionSeleccionada;
    }

    public void setRelacionSeleccionada(RelacionesIncapacidades relacionSeleccionada) {
        this.relacionSeleccionada = relacionSeleccionada;
    }

    public List<SucursalesPila> getLovSucursales() {
        if (lovSucursales == null) {
            lovSucursales = administrarOdiCabecera.lovSucursales(auxiliar);
        }
        return lovSucursales;
    }

    public void setLovSucursales(List<SucursalesPila> lovSucursales) {
        this.lovSucursales = lovSucursales;
    }

    public List<SucursalesPila> getFiltrarLovSucursales() {
        return filtrarLovSucursales;
    }

    public void setFiltrarLovSucursales(List<SucursalesPila> filtrarLovSucursales) {
        this.filtrarLovSucursales = filtrarLovSucursales;
    }

    public List<Empleados> getLovEmpleados() {
        if (lovEmpleados == null) {
            lovEmpleados = administrarOdiCabecera.lovEmpleados();
        }
        return lovEmpleados;
    }

    public void setLovEmpleados(List<Empleados> lovEmpleados) {
        this.lovEmpleados = lovEmpleados;
    }

    public List<Empleados> getFiltrarLovEmpleados() {
        return filtrarLovEmpleados;
    }

    public void setFiltrarLovEmpleados(List<Empleados> filtrarLovEmpleados) {
        this.filtrarLovEmpleados = filtrarLovEmpleados;
    }

    public OdisCabeceras getEditarOdiCabecera() {
        return editarOdiCabecera;
    }

    public void setEditarOdiCabecera(OdisCabeceras editarOdiCabecera) {
        this.editarOdiCabecera = editarOdiCabecera;
    }

    public OdisCabeceras getDuplicarOdiCabecera() {
        return duplicarOdiCabecera;
    }

    public void setDuplicarOdiCabecera(OdisCabeceras duplicarOdiCabecera) {
        this.duplicarOdiCabecera = duplicarOdiCabecera;
    }

    public TiposEntidades getTipoEntidadSeleccionada() {
        return tipoEntidadSeleccionada;
    }

    public void setTipoEntidadSeleccionada(TiposEntidades tipoEntidadSeleccionada) {
        this.tipoEntidadSeleccionada = tipoEntidadSeleccionada;
    }

    public Terceros getTerceroSeleccionado() {
        return terceroSeleccionado;
    }

    public void setTerceroSeleccionado(Terceros terceroSeleccionado) {
        this.terceroSeleccionado = terceroSeleccionado;
    }

    public SucursalesPila getSucursalSeleccionada() {
        return sucursalSeleccionada;
    }

    public void setSucursalSeleccionada(SucursalesPila sucursalSeleccionada) {
        this.sucursalSeleccionada = sucursalSeleccionada;
    }

    public Empleados getEmpleadoSeleccionado() {
        return empleadoSeleccionado;
    }

    public void setEmpleadoSeleccionado(Empleados empleadoSeleccionado) {
        this.empleadoSeleccionado = empleadoSeleccionado;
    }

    public String getPaginaAnterior() {
        return paginaAnterior;
    }

    public void setPaginaAnterior(String paginaAnterior) {
        this.paginaAnterior = paginaAnterior;
    }

    public String getInfoRegistroOdiCabecera() {
        return infoRegistroOdiCabecera;
    }

    public void setInfoRegistroOdiCabecera(String infoRegistroOdiCabecera) {
        this.infoRegistroOdiCabecera = infoRegistroOdiCabecera;
    }

    public String getInfoRegistroEmpresas() {
        return infoRegistroEmpresas;
    }

    public void setInfoRegistroEmpresas(String infoRegistroEmpresas) {
        this.infoRegistroEmpresas = infoRegistroEmpresas;
    }

    public String getInfoRegistroTerceros() {
        return infoRegistroTerceros;
    }

    public void setInfoRegistroTerceros(String infoRegistroTerceros) {
        this.infoRegistroTerceros = infoRegistroTerceros;
    }

    public String getInfoRegistroSucursales() {
        return infoRegistroSucursales;
    }

    public void setInfoRegistroSucursales(String infoRegistroSucursales) {
        this.infoRegistroSucursales = infoRegistroSucursales;
    }

    public String getInfoRegistroTipoEntidad() {
        return infoRegistroTipoEntidad;
    }

    public void setInfoRegistroTipoEntidad(String infoRegistroTipoEntidad) {
        this.infoRegistroTipoEntidad = infoRegistroTipoEntidad;
    }

    public String getInfoRegistroEmpleados() {
        return infoRegistroEmpleados;
    }

    public void setInfoRegistroEmpleados(String infoRegistroEmpleados) {
        this.infoRegistroEmpleados = infoRegistroEmpleados;
    }

    public String getInfoRegistroRelacionesIncapacidades() {
        return infoRegistroRelacionesIncapacidades;
    }

    public void setInfoRegistroRelacionesIncapacidades(String infoRegistroRelacionesIncapacidades) {
        this.infoRegistroRelacionesIncapacidades = infoRegistroRelacionesIncapacidades;
    }

    public String getMensajeValidacion() {
        return mensajeValidacion;
    }

    public void setMensajeValidacion(String mensajeValidacion) {
        this.mensajeValidacion = mensajeValidacion;
    }

    public OdisCabeceras getOdiCabeceraSeleccionada() {
        return odiCabeceraSeleccionada;
    }

    public void setOdiCabeceraSeleccionada(OdisCabeceras odiCabeceraSeleccionada) {
        this.odiCabeceraSeleccionada = odiCabeceraSeleccionada;
    }

    public Empresas getEmpresaSeleccionada() {
        return empresaSeleccionada;
    }

    public void setEmpresaSeleccionada(Empresas empresaSeleccionada) {
        this.empresaSeleccionada = empresaSeleccionada;
    }

    public BigInteger getAnioParametro() {
        return anioParametro;
    }

    public void setAnioParametro(BigInteger anioParametro) {
        this.anioParametro = anioParametro;
    }

    public BigInteger getMesParametro() {
        return mesParametro;
    }

    public void setMesParametro(BigInteger mesParametro) {
        this.mesParametro = mesParametro;
    }

    public OdisCabeceras getNuevaOdiCabecera() {
        return nuevaOdiCabecera;
    }

    public void setNuevaOdiCabecera(OdisCabeceras nuevaOdiCabecera) {
        this.nuevaOdiCabecera = nuevaOdiCabecera;
    }

    public List<OdisDetalles> getListaOdiDetalles() {
        return listaOdiDetalles;
    }

    public void setListaOdiDetalles(List<OdisDetalles> listaOdiDetalles) {
        this.listaOdiDetalles = listaOdiDetalles;
    }

    public List<OdisDetalles> getFiltrarListaOdiDetalles() {
        return filtrarListaOdiDetalles;
    }

    public void setFiltrarListaOdiDetalles(List<OdisDetalles> filtrarListaOdiDetalles) {
        this.filtrarListaOdiDetalles = filtrarListaOdiDetalles;
    }

    public OdisDetalles getOdiDetallesSeleccionada() {
        return OdiDetallesSeleccionada;
    }

    public void setOdiDetallesSeleccionada(OdisDetalles OdiDetallesSeleccionada) {
        this.OdiDetallesSeleccionada = OdiDetallesSeleccionada;
    }

    public OdisDetalles getNuevaOdiDetalles() {
        return nuevaOdiDetalles;
    }

    public void setNuevaOdiDetalles(OdisDetalles nuevaOdiDetalles) {
        this.nuevaOdiDetalles = nuevaOdiDetalles;
    }

    public OdisDetalles getEditarOdiDetalles() {
        return editarOdiDetalles;
    }

    public void setEditarOdiDetalles(OdisDetalles editarOdiDetalles) {
        this.editarOdiDetalles = editarOdiDetalles;
    }

    public OdisDetalles getDuplicarOdiDetalles() {
        return duplicarOdiDetalles;
    }

    public void setDuplicarOdiDetalles(OdisDetalles duplicarOdiDetalles) {
        this.duplicarOdiDetalles = duplicarOdiDetalles;
    }

    public boolean isIncluirdetalles() {

        return incluirdetalles;
    }

    public void setIncluirdetalles(boolean incluirdetalles) {
        this.incluirdetalles = incluirdetalles;
    }

    public String getInfoRegistroOdiDetalles() {
        return infoRegistroOdiDetalles;
    }

    public void setInfoRegistroOdiDetalles(String infoRegistroOdiDetalles) {
        this.infoRegistroOdiDetalles = infoRegistroOdiDetalles;
    }

    public boolean isGuardado() {
        return guardado;
    }

    public void setGuardado(boolean guardado) {
        this.guardado = guardado;
    }

    public boolean isEditarDetalles() {
        return editarDetalles;
    }

    public void setEditarDetalles(boolean editarDetalles) {
        this.editarDetalles = editarDetalles;
    }

}
