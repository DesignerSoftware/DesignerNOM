/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Periodicidades;
import Entidades.Unidades;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarPeriodicidadesInterface;
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
public class ControlPeriodicidades implements Serializable {

    @EJB
    AdministrarPeriodicidadesInterface administrarPeriodicidades;
    @EJB
    AdministrarRastrosInterface administrarRastros;
    private List<Periodicidades> filtrarPeriodicidades;
    private List<Periodicidades> crearPeriodicidades;
    private List<Periodicidades> modificarPeriodicidades;
    private List<Periodicidades> borrarPeriodicidades;
    private Periodicidades nuevaPeriodicidad;
    private Periodicidades duplicarPeriodicidad;
    private Periodicidades editarUnidad;
    private Periodicidades periocidadSeleccionadaTabla;
    private int cualCelda, tipoLista, tipoActualizacion, k, bandera;
    private BigInteger l;
    private boolean aceptar, guardado;
    private int registrosBorrados;
    private String mensajeValidacion;
    private List<Periodicidades> listPeriodicidades;
    private Column codigoCC, nombreUnidad,
            tipoUnidad, codigoUnidad, codigoUnidadbase,
            unidadBase;

    private List<Unidades> listaUnidades;
    private List<Unidades> filtradoUnidades;
    private Unidades unidadSeleccionada;
    private Unidades unidadBaseSeleccionada;
    private List<Periodicidades> filterPericiodidades;

    private int tamano;
    private String infoRegistroUnidades;
    private String infoRegistroUnidadesBase;
    private String infoRegistro;
    private boolean activarLov;
    //VARIABLES PARA VERIFICAR BORRADO
    private BigInteger contarCPCompromisosPeriodicidad;
    private BigInteger contarDetallesPeriodicidadesPeriodicidad;
    private BigInteger contarEersPrestamosDtosPeriodicidad;
    private BigInteger contarEmpresasPeriodicidad;
    private BigInteger contarFormulasAseguradasPeriodicidad;
    private BigInteger contarFormulasContratosPeriodicidad;
    private BigInteger contarGruposProvisionesPeriodicidad;
    private BigInteger contarNovedadPeriodicidad;
    private BigInteger contadorInterconHelisa;
    private BigInteger contadorInterconSapbo;
    private String paginaAnterior = "nominaf";
    private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

    public ControlPeriodicidades() {
        listPeriodicidades = null;
//        listPeriodicidadesBoton = null;
        crearPeriodicidades = new ArrayList<Periodicidades>();
        modificarPeriodicidades = new ArrayList<Periodicidades>();
        borrarPeriodicidades = new ArrayList<Periodicidades>();
        editarUnidad = new Periodicidades();
        nuevaPeriodicidad = new Periodicidades();
        nuevaPeriodicidad.setUnidad(new Unidades());
        nuevaPeriodicidad.setUnidadbase(new Unidades());
        duplicarPeriodicidad = new Periodicidades();
        listaUnidades = null;
        aceptar = true;
        guardado = true;
        tamano = 270;
        mapParametros.put("paginaAnterior", paginaAnterior);
    }

   public void limpiarListasValor() {

   }

   @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarPeriodicidades.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void recibirPaginaEntrante(String pagina) {
        paginaAnterior = pagina;
        listPeriodicidades = null;
        getListPeriodicidades();
        if (listPeriodicidades != null) {
            if (!listPeriodicidades.isEmpty()) {
                periocidadSeleccionadaTabla = listPeriodicidades.get(0);
            }
        }
    }

    public void recibirParametros(Map<String, Object> map) {
        mapParametros = map;
        paginaAnterior = (String) mapParametros.get("paginaAnterior");
        listPeriodicidades = null;
        getListPeriodicidades();
        if (listPeriodicidades != null) {
            if (!listPeriodicidades.isEmpty()) {
                periocidadSeleccionadaTabla = listPeriodicidades.get(0);
            }
        }
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
            String pagActual = "periodicidad";
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

    public String redirigir() {
        return paginaAnterior;
    }

    public void eventoFiltrar() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
        contarRegistros();
    }

    public void refrescar() {
        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            tamano = 270;
            codigoCC = (Column) c.getViewRoot().findComponent("form:datosPeriodicidades:codigoCC");
            codigoCC.setFilterStyle("display: none; visibility: hidden;");
            nombreUnidad = (Column) c.getViewRoot().findComponent("form:datosPeriodicidades:nombreUnidad");
            nombreUnidad.setFilterStyle("display: none; visibility: hidden;");
            tipoUnidad = (Column) c.getViewRoot().findComponent("form:datosPeriodicidades:tipoUnidad");
            tipoUnidad.setFilterStyle("display: none; visibility: hidden;");
            codigoUnidad = (Column) c.getViewRoot().findComponent("form:datosPeriodicidades:codigoUnidad");
            codigoUnidad.setFilterStyle("display: none; visibility: hidden;");
            codigoUnidadbase = (Column) c.getViewRoot().findComponent("form:datosPeriodicidades:codigoUnidadbase");
            codigoUnidadbase.setFilterStyle("display: none; visibility: hidden;");
            unidadBase = (Column) c.getViewRoot().findComponent("form:datosPeriodicidades:unidadBase");
            unidadBase.setFilterStyle("display: none; visibility: hidden;");

            RequestContext.getCurrentInstance().update("form:datosPeriodicidades");
            bandera = 0;
            filtrarPeriodicidades = null;
            tipoLista = 0;
        }
        crearPeriodicidades.clear();
        borrarPeriodicidades.clear();
        modificarPeriodicidades.clear();
        periocidadSeleccionadaTabla = null;
        k = 0;
        listPeriodicidades = null;
        guardado = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosPeriodicidades");
    }

    public void cambiarIndice(Periodicidades periodicidad, int celda) {
        periocidadSeleccionadaTabla = periodicidad;
        cualCelda = celda;
        periocidadSeleccionadaTabla.getSecuencia();
        if (cualCelda == 0) {
            deshabilitarBotonlov();
            periocidadSeleccionadaTabla.getCodigo();
        }
        if (cualCelda == 1) {
            deshabilitarBotonlov();
            periocidadSeleccionadaTabla.getNombre();
        }
        if (cualCelda == 2) {
            habilitarBotonLov();
            periocidadSeleccionadaTabla.getUnidad().getCodigo();
        }
        if (cualCelda == 3) {
            habilitarBotonLov();
            periocidadSeleccionadaTabla.getUnidad().getNombre();
        }
        if (cualCelda == 4) {
            habilitarBotonLov();
            periocidadSeleccionadaTabla.getUnidadbase().getCodigo();
        }
        if (cualCelda == 5) {
            habilitarBotonLov();
            periocidadSeleccionadaTabla.getUnidadbase().getNombre();
        }
    }

    public void modificandoUnidad(Periodicidades periodicidad) {

        periocidadSeleccionadaTabla = periodicidad;
        if (!crearPeriodicidades.contains(periocidadSeleccionadaTabla)) {
            if (modificarPeriodicidades.isEmpty()) {
                modificarPeriodicidades.add(periocidadSeleccionadaTabla);
            } else if (!modificarPeriodicidades.contains(periocidadSeleccionadaTabla)) {
                modificarPeriodicidades.add(periocidadSeleccionadaTabla);
            }
        }
        guardado = false;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("form:datosPeriodicidades");

    }

    public void cancelarModificacion() {
        try {
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                //CERRAR FILTRADO
                codigoCC = (Column) c.getViewRoot().findComponent("form:datosPeriodicidades:codigoCC");
                codigoCC.setFilterStyle("display: none; visibility: hidden;");
                nombreUnidad = (Column) c.getViewRoot().findComponent("form:datosPeriodicidades:nombreUnidad");
                nombreUnidad.setFilterStyle("display: none; visibility: hidden;");
                tipoUnidad = (Column) c.getViewRoot().findComponent("form:datosPeriodicidades:tipoUnidad");
                tipoUnidad.setFilterStyle("display: none; visibility: hidden;");
                codigoUnidad = (Column) c.getViewRoot().findComponent("form:datosPeriodicidades:codigoUnidad");
                codigoUnidad.setFilterStyle("display: none; visibility: hidden;");
                codigoUnidadbase = (Column) c.getViewRoot().findComponent("form:datosPeriodicidades:codigoUnidadbase");
                codigoUnidadbase.setFilterStyle("display: none; visibility: hidden;");
                unidadBase = (Column) c.getViewRoot().findComponent("form:datosPeriodicidades:unidadBase");
                unidadBase.setFilterStyle("display: none; visibility: hidden;");
                bandera = 0;
                filtrarPeriodicidades = null;
                tipoLista = 0;
            }

            borrarPeriodicidades.clear();
            crearPeriodicidades.clear();
            modificarPeriodicidades.clear();
            listPeriodicidades = null;
            getListPeriodicidades();
            contarRegistros();
            k = 0;
            guardado = true;
            RequestContext.getCurrentInstance().update("form:datosPeriodicidades");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        } catch (Exception E) {
            System.out.println("ERROR CONTROLPERIODICIDADES.ModificarModificacion ERROR====================" + E.getMessage());
        }
    }

    public void salir() {  limpiarListasValor();
        try {
            System.out.println("entre a CONTROLPERIODICIDADES.cancelarModificacion");
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                codigoCC = (Column) c.getViewRoot().findComponent("form:datosPeriodicidades:codigoCC");
                codigoCC.setFilterStyle("display: none; visibility: hidden;");
                nombreUnidad = (Column) c.getViewRoot().findComponent("form:datosPeriodicidades:nombreUnidad");
                nombreUnidad.setFilterStyle("display: none; visibility: hidden;");
                tipoUnidad = (Column) c.getViewRoot().findComponent("form:datosPeriodicidades:tipoUnidad");
                tipoUnidad.setFilterStyle("display: none; visibility: hidden;");
                codigoUnidad = (Column) c.getViewRoot().findComponent("form:datosPeriodicidades:codigoUnidad");
                codigoUnidad.setFilterStyle("display: none; visibility: hidden;");
                codigoUnidadbase = (Column) c.getViewRoot().findComponent("form:datosPeriodicidades:codigoUnidadbase");
                codigoUnidadbase.setFilterStyle("display: none; visibility: hidden;");
                unidadBase = (Column) c.getViewRoot().findComponent("form:datosPeriodicidades:unidadBase");
                unidadBase.setFilterStyle("display: none; visibility: hidden;");
                bandera = 0;
                filtrarPeriodicidades = null;
                tipoLista = 0;
            }

            borrarPeriodicidades.clear();
            crearPeriodicidades.clear();
            modificarPeriodicidades.clear();
            listPeriodicidades = null;
            getListPeriodicidades();
            contarRegistros();
            k = 0;
            guardado = true;
            RequestContext.getCurrentInstance().update("form:datosPeriodicidades");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            navegar("atras");
        } catch (Exception E) {
            System.out.println("ERROR CONTROLPERIODICIDADES.ModificarModificacion ERROR====================" + E.getMessage());
        }
    }

    public void asignarIndex(Periodicidades periodicidad, int LND, int dig) {
        periocidadSeleccionadaTabla = periodicidad;
        tipoActualizacion = LND;

        if (dig == 2) {
            RequestContext.getCurrentInstance().update("form:tiposPeriodicidadesDialogo");
            RequestContext.getCurrentInstance().execute("PF('tiposPeriodicidadesDialogo').show()");
            dig = -1;
        }
        if (dig == 3) {
            RequestContext.getCurrentInstance().update("form:unidadesBaseDialogo");
            RequestContext.getCurrentInstance().execute("PF('unidadesBaseDialogo').show()");
            dig = -1;
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void actualizarTipoPeriodicidad() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            periocidadSeleccionadaTabla.setUnidad(unidadSeleccionada);
            if (!crearPeriodicidades.contains(periocidadSeleccionadaTabla)) {
                if (modificarPeriodicidades.isEmpty()) {
                    modificarPeriodicidades.add(periocidadSeleccionadaTabla);
                } else if (!modificarPeriodicidades.contains(periocidadSeleccionadaTabla)) {
                    modificarPeriodicidades.add(periocidadSeleccionadaTabla);
                }
                if (guardado == true) {
                    guardado = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
            }
            RequestContext.getCurrentInstance().update("form:datosPeriodicidades");
        } else if (tipoActualizacion == 1) {
            nuevaPeriodicidad.setUnidad(unidadSeleccionada);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaTipoUnidads");
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaTipoUnidadsCodigo");
            RequestContext.getCurrentInstance().update("form:datosPeriodicidades");
        } else if (tipoActualizacion == 2) {
            duplicarPeriodicidad.setUnidad(unidadSeleccionada);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCodigoUnidades");
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTipoUnidads");
            RequestContext.getCurrentInstance().update("form:datosPeriodicidades");
        }
        filtradoUnidades = null;
        unidadSeleccionada = null;
        aceptar = true;
        tipoActualizacion = -1;

        RequestContext.getCurrentInstance().update("form:tiposPeriodicidadesDialogo");
        RequestContext.getCurrentInstance().update("form:lovTipoPeriodicidades");
        RequestContext.getCurrentInstance().update("form:aceptarTCC");

        context.reset("form:lovTipoPeriodicidades:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovTipoPeriodicidades').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('tiposPeriodicidadesDialogo').hide()");
    }

    public void actualizarUnidadBase() {
        try {
            RequestContext context = RequestContext.getCurrentInstance();
            if (tipoActualizacion == 0) {
                periocidadSeleccionadaTabla.setUnidadbase(unidadBaseSeleccionada);
                if (!crearPeriodicidades.contains(periocidadSeleccionadaTabla)) {
                    if (modificarPeriodicidades.isEmpty()) {
                        modificarPeriodicidades.add(periocidadSeleccionadaTabla);
                    } else if (!modificarPeriodicidades.contains(periocidadSeleccionadaTabla)) {
                        modificarPeriodicidades.add(periocidadSeleccionadaTabla);
                    }
                    if (guardado == true) {
                        guardado = false;
                        RequestContext.getCurrentInstance().update("form:ACEPTAR");
                    }
                }
                RequestContext.getCurrentInstance().update("form:datosPeriodicidades");
            } else if (tipoActualizacion == 1) {
                nuevaPeriodicidad.setUnidadbase(unidadBaseSeleccionada);
                RequestContext.getCurrentInstance().update("formularioDialogos:nuevaCodigoBase");
                RequestContext.getCurrentInstance().update("formularioDialogos:nuevaNombreBase");
            } else if (tipoActualizacion == 2) {
                duplicarPeriodicidad.setUnidadbase(unidadBaseSeleccionada);
                RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCodigoUnidadesBase");
                RequestContext.getCurrentInstance().update("formularioDialogos:duplicarDescripcionUnidadesBase");
            }
            filtradoUnidades = null;
            unidadBaseSeleccionada = null;
            aceptar = true;
            tipoActualizacion = -1;
            RequestContext.getCurrentInstance().update("form:unidadesBaseDialogo");
            RequestContext.getCurrentInstance().update("form:lovUnidadesBase");
            RequestContext.getCurrentInstance().update("form:aceptarUB");
            context.reset("form:lovUnidadesBase:globalFilter");
            RequestContext.getCurrentInstance().execute("PF('lovUnidadesBase').clearFilters()");
            RequestContext.getCurrentInstance().execute("PF('unidadesBaseDialogo').hide()");

        } catch (Exception e) {
            System.out.println("ERROR BETA .actualizarUnidad ERROR============" + e.getMessage());
        }
    }

    public void cancelarCambioTipoPeriodicidad() {
        filtradoUnidades = null;
        unidadSeleccionada = null;
        aceptar = true;
        tipoActualizacion = -1;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:lovTipoPeriodicidades");
        RequestContext.getCurrentInstance().update("form:aceptarTCC");

        context.reset("form:lovTipoPeriodicidades:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovTipoPeriodicidades').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('tiposPeriodicidadesDialogo').hide()");
    }

    public void cancelarCambioUnidadBase() {
        filtradoUnidades = null;
        unidadBaseSeleccionada = null;
        aceptar = true;
        tipoActualizacion = -1;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:tiposPeriodicidadesDialogo");
        RequestContext.getCurrentInstance().update("form:unidadesBaseDialogo");
        RequestContext.getCurrentInstance().update("form:lovUnidadesBase");
        RequestContext.getCurrentInstance().update("form:aceptarUB");
        context.reset("form:lovUnidadesBase:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovUnidadesBase').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('unidadesBaseDialogo').hide()");
    }

    public void limpiarNuevoPeriodicidades() {
        try {
            nuevaPeriodicidad = new Periodicidades();
            nuevaPeriodicidad.setUnidad(new Unidades());
            nuevaPeriodicidad.setUnidadbase(new Unidades());
        } catch (Exception e) {
            System.out.println("Error CONTROLPERIODICIDADES.LimpiarNuevoPeriodicidades ERROR=============================" + e.getMessage());
        }
    }

    public void agregarNuevoPeriodicidades() {
        try {
            int contador = 0;
            mensajeValidacion = " ";
            int duplicados = 0;
            RequestContext context = RequestContext.getCurrentInstance();

            if (nuevaPeriodicidad.getCodigo() == null) {
                mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            } else {
                for (int x = 0; x < listPeriodicidades.size(); x++) {
                    if (listPeriodicidades.get(x).getCodigo().equals(nuevaPeriodicidad.getCodigo())) {
                        duplicados++;
                    }
                }
                if (duplicados > 0) {
                    mensajeValidacion = "Existe una periodicidad con el código iungresado. Por favor ingrese un código válido";
                } else {
                    contador++;
                }
            }
            if (nuevaPeriodicidad.getNombre() == null) {
                mensajeValidacion = "Los campos marcados con asterisco son obligatorios";

            } else {
                contador++;
            }
            if (nuevaPeriodicidad.getUnidad().getCodigo() == null) {
                mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            } else {
                System.out.println("Bandera : ");
                contador++;
            }
            if (nuevaPeriodicidad.getUnidadbase().getCodigo() == null) {
                mensajeValidacion = "Los campos marcados con asterisco son obligatorios";

            } else {
                contador++;
            }
            if (contador == 4) {
                k++;
                l = BigInteger.valueOf(k);
                nuevaPeriodicidad.setSecuencia(l);
                crearPeriodicidades.add(nuevaPeriodicidad);
                listPeriodicidades.add(nuevaPeriodicidad);
                periocidadSeleccionadaTabla = nuevaPeriodicidad;
                RequestContext.getCurrentInstance().update("form:datosPeriodicidades");
                contarRegistros();
                nuevaPeriodicidad = new Periodicidades();
                if (guardado == true) {
                    guardado = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
                if (bandera == 1) {
                    FacesContext c = FacesContext.getCurrentInstance();
                    codigoCC = (Column) c.getViewRoot().findComponent("form:datosPeriodicidades:codigoCC");
                    codigoCC.setFilterStyle("display: none; visibility: hidden;");
                    nombreUnidad = (Column) c.getViewRoot().findComponent("form:datosPeriodicidades:nombreUnidad");
                    nombreUnidad.setFilterStyle("display: none; visibility: hidden;");
                    tipoUnidad = (Column) c.getViewRoot().findComponent("form:datosPeriodicidades:tipoUnidad");
                    tipoUnidad.setFilterStyle("display: none; visibility: hidden;");
                    codigoUnidad = (Column) c.getViewRoot().findComponent("form:datosPeriodicidades:codigoUnidad");
                    codigoUnidad.setFilterStyle("display: none; visibility: hidden;");
                    codigoUnidadbase = (Column) c.getViewRoot().findComponent("form:datosPeriodicidades:codigoUnidadbase");
                    codigoUnidadbase.setFilterStyle("display: none; visibility: hidden;");
                    unidadBase = (Column) c.getViewRoot().findComponent("form:datosPeriodicidades:unidadBase");
                    unidadBase.setFilterStyle("display: none; visibility: hidden;");
                    RequestContext.getCurrentInstance().update("form:datosPeriodicidades");
                    bandera = 0;
                    filtrarPeriodicidades = null;
                    tipoLista = 0;
                }
                RequestContext.getCurrentInstance().execute("PF('nuevoRegistroPeriodicidades').hide()");
            } else {
                contador = 0;
                RequestContext.getCurrentInstance().update("form:validacionNuevaPeriodicidad");
                RequestContext.getCurrentInstance().execute("PF('validacionNuevaPeriodicidad').show()");
            }

        } catch (Exception e) {
            System.out.println("ERROR CONTROLPERIODICIDADES.agregarNuevoPeriodicidades ERROR===========================" + e.getMessage());
        }
    }

    public void duplicandoPeriodicidades() {
        if (periocidadSeleccionadaTabla != null) {
            duplicarPeriodicidad = new Periodicidades();
            duplicarPeriodicidad.setUnidad(new Unidades());
            duplicarPeriodicidad.setUnidadbase(new Unidades());
            k++;
            l = BigInteger.valueOf(k);
            duplicarPeriodicidad.setSecuencia(l);
            duplicarPeriodicidad.setCodigo(periocidadSeleccionadaTabla.getCodigo());
            duplicarPeriodicidad.setNombre(periocidadSeleccionadaTabla.getNombre());
            duplicarPeriodicidad.getUnidad().setCodigo(periocidadSeleccionadaTabla.getUnidad().getCodigo());
            duplicarPeriodicidad.getUnidad().setNombre(periocidadSeleccionadaTabla.getUnidad().getNombre());
            duplicarPeriodicidad.getUnidadbase().setCodigo(periocidadSeleccionadaTabla.getUnidadbase().getCodigo());
            duplicarPeriodicidad.getUnidadbase().setNombre(periocidadSeleccionadaTabla.getUnidadbase().getNombre());

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarUnidads");
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroPeriodicidades').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void limpiarDuplicarPeriodicidades() {
        duplicarPeriodicidad = new Periodicidades();
        duplicarPeriodicidad.setUnidad(new Unidades());
        duplicarPeriodicidad.setUnidadbase(new Unidades());
    }

    public void confirmarDuplicar() {
        int contador = 0;
        mensajeValidacion = " ";
        int duplicados = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        Short a = 0;
        a = null;
        if (duplicarPeriodicidad.getCodigo() == null) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
        } else {
            for (int x = 0; x < listPeriodicidades.size(); x++) {
                if (listPeriodicidades.get(x).getCodigo().equals(duplicarPeriodicidad.getCodigo())) {
                    duplicados++;
                }
            }

            if (duplicados > 0) {
                mensajeValidacion = "Existe una periodicidad con el código ingresado. Por favor ingrese un código válido.";
            } else {
                contador++;
            }

        }
        if (duplicarPeriodicidad.getNombre().isEmpty()) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";

        } else if (duplicarPeriodicidad.getNombre() == null) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";

        } else {
            contador++;
        }
        if (duplicarPeriodicidad.getUnidad().getCodigo() == null) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";

        } else {
            contador++;
        }
        if (duplicarPeriodicidad.getUnidadbase().getCodigo() == null) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";

        } else {
            contador++;
        }
        if (contador == 4) {
            listPeriodicidades.add(duplicarPeriodicidad);
            crearPeriodicidades.add(duplicarPeriodicidad);
            RequestContext.getCurrentInstance().update("form:datosPeriodicidades");
            contarRegistros();
            periocidadSeleccionadaTabla = duplicarPeriodicidad;
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                //CERRAR FILTRADO
                codigoCC = (Column) c.getViewRoot().findComponent("form:datosPeriodicidades:codigoCC");
                codigoCC.setFilterStyle("display: none; visibility: hidden;");
                nombreUnidad = (Column) c.getViewRoot().findComponent("form:datosPeriodicidades:nombreUnidad");
                nombreUnidad.setFilterStyle("display: none; visibility: hidden;");
                tipoUnidad = (Column) c.getViewRoot().findComponent("form:datosPeriodicidades:tipoUnidad");
                tipoUnidad.setFilterStyle("display: none; visibility: hidden;");
                codigoUnidad = (Column) c.getViewRoot().findComponent("form:datosPeriodicidades:codigoUnidad");
                codigoUnidad.setFilterStyle("display: none; visibility: hidden;");
                codigoUnidadbase = (Column) c.getViewRoot().findComponent("form:datosPeriodicidades:codigoUnidadbase");
                codigoUnidadbase.setFilterStyle("display: none; visibility: hidden;");
                unidadBase = (Column) c.getViewRoot().findComponent("form:datosPeriodicidades:unidadBase");
                unidadBase.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosPeriodicidades");
                RequestContext.getCurrentInstance().update("form:datosPeriodicidades");
                bandera = 0;
                filtrarPeriodicidades = null;
                tipoLista = 0;
            }
            duplicarPeriodicidad = new Periodicidades();
            duplicarPeriodicidad.setUnidad(new Unidades());
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroPeriodicidades').hide()");

        } else {
            contador = 0;
            RequestContext.getCurrentInstance().update("form:validacionDuplicarPeriodicidad");
            RequestContext.getCurrentInstance().execute("PF('validacionDuplicarPeriodicidad').show()");
        }
    }

    public void verificarBorrado() {
        System.out.println("Estoy en verificarBorrado");
        System.out.println("TIPOLISTA = " + tipoLista);
        BigInteger pruebilla;
        contarCPCompromisosPeriodicidad = administrarPeriodicidades.contarCPCompromisosPeriodicidad(periocidadSeleccionadaTabla.getSecuencia());
        contarDetallesPeriodicidadesPeriodicidad = administrarPeriodicidades.contarDetallesPeriodicidadesPeriodicidad(periocidadSeleccionadaTabla.getSecuencia());;
        contarEersPrestamosDtosPeriodicidad = administrarPeriodicidades.contarEersPrestamosDtosPeriodicidad(periocidadSeleccionadaTabla.getSecuencia());
        contarEmpresasPeriodicidad = administrarPeriodicidades.contarEmpresasPeriodicidad(periocidadSeleccionadaTabla.getSecuencia());
        contarFormulasAseguradasPeriodicidad = administrarPeriodicidades.contarFormulasAseguradasPeriodicidad(periocidadSeleccionadaTabla.getSecuencia());
        contarFormulasContratosPeriodicidad = administrarPeriodicidades.contarFormulasContratosPeriodicidad(periocidadSeleccionadaTabla.getSecuencia());
        contarGruposProvisionesPeriodicidad = administrarPeriodicidades.contarGruposProvisionesPeriodicidad(periocidadSeleccionadaTabla.getSecuencia());
        contarNovedadPeriodicidad = administrarPeriodicidades.contarNovedadesPeriodicidad(periocidadSeleccionadaTabla.getSecuencia());
        contadorInterconHelisa = administrarPeriodicidades.contarParametrosCambiosMasivosPeriodicidad(periocidadSeleccionadaTabla.getSecuencia());
        contadorInterconSapbo = administrarPeriodicidades.contarVigenciasFormasPagosPeriodicidad(periocidadSeleccionadaTabla.getSecuencia());
        if (contarDetallesPeriodicidadesPeriodicidad.equals(new BigInteger("0"))
                && contarEersPrestamosDtosPeriodicidad.equals(new BigInteger("0"))
                && contarEmpresasPeriodicidad.equals(new BigInteger("0"))
                && contarFormulasAseguradasPeriodicidad.equals(new BigInteger("0"))
                && contarFormulasContratosPeriodicidad.equals(new BigInteger("0"))
                && contarGruposProvisionesPeriodicidad.equals(new BigInteger("0"))
                && contarNovedadPeriodicidad.equals(new BigInteger("0"))
                && contadorInterconHelisa.equals(new BigInteger("0"))
                && contadorInterconSapbo.equals(new BigInteger("0"))
                && contarCPCompromisosPeriodicidad.equals(new BigInteger("0"))) {
            borrandoUnidad();
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:validacionBorrar");
            RequestContext.getCurrentInstance().execute("PF('validacionBorrar').show()");
            contarCPCompromisosPeriodicidad = new BigInteger("-1");
            contarDetallesPeriodicidadesPeriodicidad = new BigInteger("-1");
            contarEersPrestamosDtosPeriodicidad = new BigInteger("-1");
            contarEmpresasPeriodicidad = new BigInteger("-1");
            contarFormulasAseguradasPeriodicidad = new BigInteger("-1");
            contarFormulasContratosPeriodicidad = new BigInteger("-1");
            contarGruposProvisionesPeriodicidad = new BigInteger("-1");
            contarNovedadPeriodicidad = new BigInteger("-1");
            contadorInterconHelisa = new BigInteger("-1");
            contadorInterconSapbo = new BigInteger("-1");
        }
    }

    public void borrandoUnidad() {
        if (periocidadSeleccionadaTabla != null) {
            if (!modificarPeriodicidades.isEmpty() && modificarPeriodicidades.contains(periocidadSeleccionadaTabla)) {
                int modIndex = modificarPeriodicidades.indexOf(periocidadSeleccionadaTabla);
                modificarPeriodicidades.remove(modIndex);
                borrarPeriodicidades.add(periocidadSeleccionadaTabla);
            } else if (!crearPeriodicidades.isEmpty() && crearPeriodicidades.contains(periocidadSeleccionadaTabla)) {
                int crearIndex = crearPeriodicidades.indexOf(periocidadSeleccionadaTabla);
                crearPeriodicidades.remove(crearIndex);
            } else {
                borrarPeriodicidades.add(periocidadSeleccionadaTabla);
            }
            listPeriodicidades.remove(periocidadSeleccionadaTabla);
            if (tipoLista == 1) {
                filtrarPeriodicidades.remove(periocidadSeleccionadaTabla);
            }
            contarRegistros();
            periocidadSeleccionadaTabla = null;
            if (guardado == true) {
                guardado = false;
            }
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosPeriodicidades");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void guardarCambiosPeriodicidad() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (guardado == false) {
            System.out.println("Realizando Operaciones Vigencias Localizacion");
            if (!borrarPeriodicidades.isEmpty()) {
                administrarPeriodicidades.borrarPeriodicidades(borrarPeriodicidades);
                registrosBorrados = borrarPeriodicidades.size();
                RequestContext.getCurrentInstance().update("form:mostrarBorrados");
                RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
                borrarPeriodicidades.clear();
            }
            if (!modificarPeriodicidades.isEmpty()) {
                administrarPeriodicidades.modificarPeriodicidades(modificarPeriodicidades);
                modificarPeriodicidades.clear();
            }
            if (!crearPeriodicidades.isEmpty()) {
                administrarPeriodicidades.crearPeriodicidades(crearPeriodicidades);
                crearPeriodicidades.clear();
            }
            System.out.println("Se guardaron los datos con exito");
            listPeriodicidades = null;
            RequestContext.getCurrentInstance().update("form:datosPeriodicidades");
            FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
            k = 0;
            guardado = true;

        }
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void activarCtrlF11() {
        System.out.println("\n ENTRE A CONTROLPERIODICIDADES ACTIVARCTRLF11 \n");
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {

            tamano = 250;
            System.out.println("Activar");
            codigoCC = (Column) c.getViewRoot().findComponent("form:datosPeriodicidades:codigoCC");
            codigoCC.setFilterStyle("width: 85% !important");
            nombreUnidad = (Column) c.getViewRoot().findComponent("form:datosPeriodicidades:nombreUnidad");
            nombreUnidad.setFilterStyle("width:  85% !important");
            tipoUnidad = (Column) c.getViewRoot().findComponent("form:datosPeriodicidades:tipoUnidad");
            tipoUnidad.setFilterStyle("width: 85% !important;");
            codigoUnidad = (Column) c.getViewRoot().findComponent("form:datosPeriodicidades:codigoUnidad");
            codigoUnidad.setFilterStyle("width: 85% !important;");
            codigoUnidadbase = (Column) c.getViewRoot().findComponent("form:datosPeriodicidades:codigoUnidadbase");
            codigoUnidadbase.setFilterStyle("width: 85% !important;");
            unidadBase = (Column) c.getViewRoot().findComponent("form:datosPeriodicidades:unidadBase");
            unidadBase.setFilterStyle("width: 85% !important;");
            RequestContext.getCurrentInstance().update("form:datosPeriodicidades");
            bandera = 1;
        } else if (bandera == 1) {
            System.out.println("Desactivar");
            //0
            tamano = 270;
            codigoCC = (Column) c.getViewRoot().findComponent("form:datosPeriodicidades:codigoCC");
            codigoCC.setFilterStyle("display: none; visibility: hidden;");
            nombreUnidad = (Column) c.getViewRoot().findComponent("form:datosPeriodicidades:nombreUnidad");
            nombreUnidad.setFilterStyle("display: none; visibility: hidden;");
            tipoUnidad = (Column) c.getViewRoot().findComponent("form:datosPeriodicidades:tipoUnidad");
            tipoUnidad.setFilterStyle("display: none; visibility: hidden;");
            codigoUnidad = (Column) c.getViewRoot().findComponent("form:datosPeriodicidades:codigoUnidad");
            codigoUnidad.setFilterStyle("display: none; visibility: hidden;");
            codigoUnidadbase = (Column) c.getViewRoot().findComponent("form:datosPeriodicidades:codigoUnidadbase");
            codigoUnidadbase.setFilterStyle("display: none; visibility: hidden;");
            unidadBase = (Column) c.getViewRoot().findComponent("form:datosPeriodicidades:unidadBase");
            unidadBase.setFilterStyle("display: none; visibility: hidden;");

            RequestContext.getCurrentInstance().update("form:datosPeriodicidades");
            bandera = 0;
            filtrarPeriodicidades = null;
            tipoLista = 0;
        }
    }

    public void editarCelda() {
        if (periocidadSeleccionadaTabla != null) {
            editarUnidad = periocidadSeleccionadaTabla;
            RequestContext context = RequestContext.getCurrentInstance();
            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarCCC");
                RequestContext.getCurrentInstance().execute("PF('editarCCC').show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarNCC");
                RequestContext.getCurrentInstance().execute("PF('editarNCC').show()");
                cualCelda = -1;
            } else if (cualCelda == 2) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarTCC");
                RequestContext.getCurrentInstance().execute("PF('editarTCC').show()");
                cualCelda = -1;
            } else if (cualCelda == 3) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarMO");
                RequestContext.getCurrentInstance().execute("PF('editarMO').show()");
                cualCelda = -1;
            } else if (cualCelda == 4) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarCAT");
                RequestContext.getCurrentInstance().execute("PF('editarCAT').show()");
                cualCelda = -1;
            } else if (cualCelda == 5) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarO");
                RequestContext.getCurrentInstance().execute("PF('editarO').show()");
                cualCelda = -1;
            }
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void listaValoresBoton() {
        if (periocidadSeleccionadaTabla != null) {
            RequestContext context = RequestContext.getCurrentInstance();
            System.out.println("\n ListaValoresBoton \n");
            if (cualCelda == 2) {
                RequestContext.getCurrentInstance().update("form:tiposPeriodicidadesDialogo");
                RequestContext.getCurrentInstance().execute("PF('tiposPeriodicidadesDialogo').show()");
                tipoActualizacion = 0;
            }
            if (cualCelda == 3) {
                RequestContext.getCurrentInstance().update("form:tiposPeriodicidadesDialogo");
                RequestContext.getCurrentInstance().execute("PF('tiposPeriodicidadesDialogo').show()");
                tipoActualizacion = 0;
            }
            if (cualCelda == 4) {
                RequestContext.getCurrentInstance().update("form:unidadesBaseDialogo");
                RequestContext.getCurrentInstance().execute("PF('unidadesBaseDialogo').show()");
                tipoActualizacion = 0;
            }
            if (cualCelda == 5) {
                RequestContext.getCurrentInstance().update("form:unidadesBaseDialogo");
                RequestContext.getCurrentInstance().execute("PF('unidadesBaseDialogo').show()");
                tipoActualizacion = 0;
            }
        }
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosPeriodicidadesExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "Periodicidades", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosPeriodicidadesExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "Periodicidades", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (periocidadSeleccionadaTabla != null) {
            int resultado = administrarRastros.obtenerTabla(periocidadSeleccionadaTabla.getSecuencia(), "PERIODICIDADES");
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
        } else if (administrarRastros.verificarHistoricosTabla("PERIODICIDADES")) { // igual acá
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
    }

    public void contarRegistros() {
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
    }

    public void contarRegistrosUnidades() {
        RequestContext.getCurrentInstance().update("form:infoRegistroUnidades");
    }

    public void contarRegistrosUnidadesBase() {
        RequestContext.getCurrentInstance().update("form:infoRegistroUnidadesBase");
    }

    public void habilitarBotonLov() {
        activarLov = false;
        RequestContext.getCurrentInstance().update("form:listaValores");
    }

    public void deshabilitarBotonlov() {
        activarLov = true;
        RequestContext.getCurrentInstance().update("form:listaValores");
    }

//------------SETS Y GETS--------------------------------------**
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

    public List<Periodicidades> getListPeriodicidades() {
        if (listPeriodicidades == null) {
            listPeriodicidades = administrarPeriodicidades.consultarPeriodicidades();
        }
        return listPeriodicidades;
    }

    public void setListPeriodicidades(List<Periodicidades> listPeriodicidades) {
        this.listPeriodicidades = listPeriodicidades;
    }

    public void setListPericiodidades(List<Periodicidades> listPeriodicidades) {
        this.listPeriodicidades = listPeriodicidades;
    }

    public List<Periodicidades> getFiltrarPeriodicidades() {
        return filtrarPeriodicidades;
    }

    public void setFiltrarPeriodicidades(List<Periodicidades> filtrarPeriodicidades) {
        this.filtrarPeriodicidades = filtrarPeriodicidades;
    }

    public Periodicidades getNuevaPeriodicidad() {
        if (nuevaPeriodicidad == null) {
            nuevaPeriodicidad = new Periodicidades();
        }
        return nuevaPeriodicidad;
    }

    public void setNuevaPeriodicidad(Periodicidades nuevaPeriodicidad) {
        this.nuevaPeriodicidad = nuevaPeriodicidad;
    }

    public Periodicidades getDuplicarPeriodicidad() {
        return duplicarPeriodicidad;
    }

    public void setDuplicarPeriodicidad(Periodicidades duplicarPeriodicidad) {
        this.duplicarPeriodicidad = duplicarPeriodicidad;
    }

    public List<Unidades> getListaUnidades() {
        if (listaUnidades == null) {
            listaUnidades = administrarPeriodicidades.consultarLOVUnidades();
        }
        return listaUnidades;
    }

    public void setListaUnidades(List<Unidades> listaUnidades) {
        this.listaUnidades = listaUnidades;
    }

    public List<Unidades> getFiltradoUnidades() {
        return filtradoUnidades;
    }

    public void setFiltradoUnidades(List<Unidades> filtradoUnidades) {
        this.filtradoUnidades = filtradoUnidades;
    }

    public Unidades getUnidadSeleccionada() {
        return unidadSeleccionada;
    }

    public void setUnidadSeleccionada(Unidades unidadSeleccionada) {
        this.unidadSeleccionada = unidadSeleccionada;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public void setAceptar(boolean aceptar) {
        this.aceptar = aceptar;
    }

    public List<Periodicidades> getFilterPericiodidades() {
        return filterPericiodidades;
    }

    public void setFilterPericiodidades(List<Periodicidades> filterPericiodidades) {
        this.filterPericiodidades = filterPericiodidades;
    }

    public Periodicidades getEditarUnidad() {
        return editarUnidad;
    }

    public void setEditarUnidad(Periodicidades editarUnidad) {
        this.editarUnidad = editarUnidad;
    }

    public boolean isGuardado() {
        return guardado;
    }

    public void setGuardado(boolean guardado) {
        this.guardado = guardado;
    }

    public int getTamano() {
        return tamano;
    }

    public void setTamano(int tamano) {
        this.tamano = tamano;
    }

    public Periodicidades getPeriocidadSeleccionadaTabla() {
        return periocidadSeleccionadaTabla;
    }

    public void setPeriocidadSeleccionadaTabla(Periodicidades periocidadSeleccionadaTabla) {
        this.periocidadSeleccionadaTabla = periocidadSeleccionadaTabla;
    }

    public String getInfoRegistro() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosPeriodicidades");
        infoRegistro = String.valueOf(tabla.getRowCount());
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

    public String getInfoRegistroUnidades() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovTipoPeriodicidades");
        infoRegistroUnidades = String.valueOf(tabla.getRowCount());
        return infoRegistroUnidades;
    }

    public void setInfoRegistroUnidades(String infoRegistroUnidades) {
        this.infoRegistroUnidades = infoRegistroUnidades;
    }

    public String getInfoRegistroUnidadesBase() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovUnidadesBase");
        infoRegistroUnidadesBase = String.valueOf(tabla.getRowCount());
        return infoRegistroUnidadesBase;
    }

    public void setInfoRegistroUnidadesBase(String infoRegistroUnidadesBase) {
        this.infoRegistroUnidadesBase = infoRegistroUnidadesBase;
    }

    public boolean isActivarLov() {
        return activarLov;
    }

    public void setActivarLov(boolean activarLov) {
        this.activarLov = activarLov;
    }

    public Unidades getUnidadBaseSeleccionada() {
        return unidadBaseSeleccionada;
    }

    public void setUnidadBaseSeleccionada(Unidades unidadBaseSeleccionada) {
        this.unidadBaseSeleccionada = unidadBaseSeleccionada;
    }

}
