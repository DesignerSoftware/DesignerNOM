/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.ActualUsuario;
import Entidades.AportesCorrecciones;
import Entidades.Empleados;
import Entidades.Empresas;
import Entidades.ParametrosCorreccionesAutoL;
import Entidades.ParametrosEstructuras;
import Entidades.ParametrosInformes;
import Entidades.Terceros;
import Entidades.TiposEntidades;
import Entidades.TiposTrabajadores;
import Exportar.ExportarPDF;
import Exportar.ExportarPDFTablasAnchas;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarParametrosCorreccionAutoLInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
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
public class ControlParametrosCorreccionAutoL implements Serializable {

    @EJB
    AdministrarParametrosCorreccionAutoLInterface administrarParametroCorreccionAutoL;
    @EJB
    AdministrarRastrosInterface administrarRastros;

    //PARAMETROSCORRECCIONAUTOL
    private List<ParametrosCorreccionesAutoL> listaParametrosCorrecciones;
    private List<ParametrosCorreccionesAutoL> filtrarListaParametrosCorrecciones;
    private ParametrosCorreccionesAutoL parametroCorreccionSeleccionado;
    private ParametrosCorreccionesAutoL nuevoParametro;
    private ParametrosCorreccionesAutoL editarParametro;
    private ParametrosCorreccionesAutoL duplicarParametro;
    private List<ParametrosCorreccionesAutoL> listParametrosCorreccionesCrear;
    private List<ParametrosCorreccionesAutoL> listParametrosCorreccionesModificar;
    private List<ParametrosCorreccionesAutoL> listParametrosCorreccionesBorrar;
    private int cualCelda;
    private int bandera, tipoLista;
    private boolean permitirIndex;
    /////APORTES ENTIDADES
    private List<AportesCorrecciones> listaAportesCorrecciones;
    private List<AportesCorrecciones> filtrarListaAportesCorrecciones;
    private AportesCorrecciones aporteTablaSeleccionado;
    private AportesCorrecciones nuevoAporteEntidad;
    private AportesCorrecciones editarAporteEntidad;
    private AportesCorrecciones duplicarAporteEntidad;
    private List<AportesCorrecciones> listAportesCorreccionesCrear;
    private List<AportesCorrecciones> listAportesCorreccionesModificar;
    private List<AportesCorrecciones> listAportesCorreccionesBorrar;
    private int cualCeldaAporte;
    private int banderaAporte, tipoListaAporte;
    private boolean permitirIndexAporte;
    private boolean disabledBuscar;
    //OTROS
    private int tipoActualizacion, k, cualTabla;
    private BigInteger l;
    private boolean aceptar, guardado;
    private String altoTablaAporte;
    private String paginaAnterior;
    private String infoRegistroParametro, infoRegistroAporte;
    private String visibilidadMostrarTodos;
    private DataTable tablaC;
    private ParametrosEstructuras parametroEstructura;
    private ParametrosInformes parametroInforme;
    private ActualUsuario usuario;
    private String altoTabla;
    //COLUMNAS PARAMETRO CORRECCION
    private Column parametroAno, parametroTipoTrabajador, parametroEmpresa, parametroMes, tipoPlanilla, planilla, fechaplan;
    //COLUMNAS APORTES ENTIDADES
    private Column aporteAno, aporteMes, aporteNombreEmpleado, aportetipoPlanilla, aporteNIT, aporteTercero, aporteTipoEntidad;
    private Column aporteEmpleado, aporteEmpleador, aporteAjustePatronal, aporteSolidaridadl, aporteSubSistencia;
    private Column aporteSubsPensionados, aporteSalarioBasico, aporteIBC, aporteIBCReferencia, aporteDiasCotizados, aporteTipoAportante;
    private Column aporteING, aporteRET, aporteTDA, aporteTAA, aporteVSP, aporteVTE, aporteVST, aporteSLN, aporteIGE, aporteLMA, aporteVAC, aporteAVP, aporteVCT, aporteIRP, aporteSUS, aporteINTE;
    private Column aporteTarifaEPS, aporteTarifaAAFP, aporteTarifaACTT, aporteCodigoCTT, aporteAVPEValor, aporteAVPPValor, aporteRETCONTAValor, aporteCodigoNEPS, aporteCodigoNAFP;
    private Column aporteEGValor, aporteEGAutorizacion, aporteMaternidadValor, aporteMaternidadAuto, aporteUPCValor, aporteTipo, aporteTipoPensionado, aportePensionCompartida, aporteFechaIngreso;
    ///LOV'S
    private List<TiposTrabajadores> lovTiposTrabajadores;
    private List<TiposTrabajadores> filtrarLovTiposTrabajadores;
    private TiposTrabajadores tipoTrabajadorSeleccionado;
    private String infoRegistroTipoTrabajador;
    //--//
    private List<Empresas> lovEmpresas;
    private List<Empresas> filtrarLovEmpresas;
    private Empresas empresaSeleccionada;
    private String infoRegistroEmpresa;
    //--//
    private List<Empleados> lovEmpleados;
    private List<Empleados> filtrarLovEmpleados;
    private Empleados empleadoSeleccionado;
    private String infoRegistroEmpleado;
    //--//
    private List<Terceros> lovTerceros;
    private List<Terceros> filtrarLovTerceros;
    private Terceros terceroSeleccionado;
    private String infoRegistroTercero;
    //--//
    private List<TiposEntidades> lovTiposEntidades;
    private List<TiposEntidades> filtrarLovTiposEntidades;
    private TiposEntidades tipoEntidadSeleccionado;
    private String infoRegistroTipoEntidad;
    //
    private List<AportesCorrecciones> lovAportesCorrecciones;
    private List<AportesCorrecciones> filtrarLovAportesCorrecciones;
    private AportesCorrecciones aporteEntidadlovSeleccionado;
    private String infoRegistroAporteEntidad, auxTipoTrabajador;

    public ControlParametrosCorreccionAutoL() {
        altoTabla = "40";
        altoTablaAporte = "160";
        disabledBuscar = true;
        visibilidadMostrarTodos = "hidden";
        parametroCorreccionSeleccionado = new ParametrosCorreccionesAutoL();
        aporteTablaSeleccionado = new AportesCorrecciones();

        nuevoParametro = new ParametrosCorreccionesAutoL();
        nuevoParametro.setEmpresa(new Empresas());
        nuevoParametro.setTipotrabajador(new TiposTrabajadores());
        duplicarParametro = new ParametrosCorreccionesAutoL();
        duplicarParametro.setEmpresa(new Empresas());
        duplicarParametro.setTipotrabajador(new TiposTrabajadores());
        editarParametro = new ParametrosCorreccionesAutoL();

        nuevoAporteEntidad = new AportesCorrecciones();
        nuevoAporteEntidad.setAno(parametroCorreccionSeleccionado.getAno());
        nuevoAporteEntidad.setMes(parametroCorreccionSeleccionado.getMes());
        nuevoAporteEntidad.setEmpresa(parametroCorreccionSeleccionado.getEmpresa());
        nuevoAporteEntidad.setTerceroRegistro(new Terceros());
        nuevoAporteEntidad.setTipoentidad(new TiposEntidades());
        nuevoAporteEntidad.setEmpleado(new Empleados());
        ///
        duplicarAporteEntidad = new AportesCorrecciones();
        duplicarAporteEntidad.setAno(aporteTablaSeleccionado.getAno());
        duplicarAporteEntidad.setMes(aporteTablaSeleccionado.getMes());
        duplicarAporteEntidad.setEmpresa(aporteTablaSeleccionado.getEmpresa());
        duplicarAporteEntidad.setTerceroRegistro(new Terceros());
        duplicarAporteEntidad.setTipoentidad(new TiposEntidades());
        duplicarAporteEntidad.setEmpleado(new Empleados());
        editarAporteEntidad = new AportesCorrecciones();

        listParametrosCorreccionesCrear = new ArrayList<ParametrosCorreccionesAutoL>();
        listParametrosCorreccionesModificar = new ArrayList<ParametrosCorreccionesAutoL>();
        listParametrosCorreccionesBorrar = new ArrayList<ParametrosCorreccionesAutoL>();

        listaAportesCorrecciones = null;
        listAportesCorreccionesCrear = new ArrayList<AportesCorrecciones>();
        listAportesCorreccionesBorrar = new ArrayList<AportesCorrecciones>();
        listAportesCorreccionesModificar = new ArrayList<AportesCorrecciones>();

        cualCelda = -1;
        cualCeldaAporte = -1;
        bandera = 0;
        banderaAporte = 0;
        tipoLista = 0;
        tipoListaAporte = 0;
        permitirIndex = true;
        permitirIndexAporte = true;
        aceptar = true;
        guardado = true;

        lovTiposTrabajadores = null;
        tipoTrabajadorSeleccionado = new TiposTrabajadores();
        lovEmpresas = null;
        empresaSeleccionada = new Empresas();
        lovEmpleados = null;
        empleadoSeleccionado = new Empleados();
        lovTerceros = null;
        terceroSeleccionado = new Terceros();
        lovTiposEntidades = null;
        tipoEntidadSeleccionado = new TiposEntidades();
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarParametroCorreccionAutoL.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());

        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void recibirPagina(String pagina) {
        paginaAnterior = pagina;
        listaParametrosCorrecciones = null;
        listaAportesCorrecciones = null;
        getListaParametrosCorrecciones();
        getListaAportesCorrecciones();
        contarRegistrosAporte();
        contarRegistrosParametros();
        if (listaParametrosCorrecciones != null) {
            if (!listaParametrosCorrecciones.isEmpty()) {
                parametroCorreccionSeleccionado = listaParametrosCorrecciones.get(0);
            }
        }
        if (listaAportesCorrecciones != null) {
            if (!listaAportesCorrecciones.isEmpty()) {
                aporteTablaSeleccionado = listaAportesCorrecciones.get(0);
            }
        }

    }

    public String redirigir() {
        return paginaAnterior;
    }

    public void modificarParametroAutoliq(ParametrosCorreccionesAutoL parametro) {
        RequestContext context = RequestContext.getCurrentInstance();
        parametroCorreccionSeleccionado = parametro;
        if (tipoLista == 0) {
            if (!listParametrosCorreccionesCrear.contains(parametroCorreccionSeleccionado)) {

                if (listParametrosCorreccionesModificar.isEmpty()) {
                    listParametrosCorreccionesModificar.add(parametroCorreccionSeleccionado);
                } else if (!listParametrosCorreccionesModificar.contains(parametroCorreccionSeleccionado)) {
                    listParametrosCorreccionesModificar.add(parametroCorreccionSeleccionado);
                }
                if (guardado == true) {
                    guardado = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
            }
        } else if (!listParametrosCorreccionesCrear.contains(parametroCorreccionSeleccionado)) {

            if (listParametrosCorreccionesModificar.isEmpty()) {
                listParametrosCorreccionesModificar.add(parametroCorreccionSeleccionado);
            } else if (!listParametrosCorreccionesModificar.contains(parametroCorreccionSeleccionado)) {
                listParametrosCorreccionesModificar.add(parametroCorreccionSeleccionado);
            }
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        } 
        RequestContext.getCurrentInstance().update("form:datosParametroAuto");
    }

    public void modificarAporteEntidad(AportesCorrecciones aporte) {
        System.out.println("entró a modificar aporte entidad   ");
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoListaAporte == 0) {
            if (listAportesCorreccionesModificar.isEmpty()) {
                listAportesCorreccionesModificar.add(aporteTablaSeleccionado);
            } else if (!listAportesCorreccionesModificar.contains(aporteTablaSeleccionado)) {
                listAportesCorreccionesModificar.add(aporteTablaSeleccionado);
            }
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        } else {
            if (listAportesCorreccionesModificar.isEmpty()) {
                listAportesCorreccionesModificar.add(aporteTablaSeleccionado);
            } else if (!listAportesCorreccionesModificar.contains(aporteTablaSeleccionado)) {
                listAportesCorreccionesModificar.add(aporteTablaSeleccionado);
            }
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        }
        RequestContext.getCurrentInstance().update("form:tablaAportesCorrecciones");
    }

    public void modificarParametroAutoliq(ParametrosCorreccionesAutoL parametro, String confirmarCambio, String valorConfirmar) {
        parametroCorreccionSeleccionado = parametro;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("TIPOTRABAJADOR")) {
            if (!valorConfirmar.isEmpty()) {
                if (tipoLista == 0) {
                    parametroCorreccionSeleccionado.getTipotrabajador().setNombre(nuevoParametro.getTipotrabajador().getNombre());
                } else {
                    parametroCorreccionSeleccionado.getTipotrabajador().setNombre(nuevoParametro.getTipotrabajador().getNombre());
                }
                for (int i = 0; i < lovTiposTrabajadores.size(); i++) {
                    if (lovTiposTrabajadores.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    if (tipoLista == 0) {
                        parametroCorreccionSeleccionado.setTipotrabajador(lovTiposTrabajadores.get(indiceUnicoElemento));
                    } else {
                        parametroCorreccionSeleccionado.setTipotrabajador(lovTiposTrabajadores.get(indiceUnicoElemento));
                    }
                    lovTiposTrabajadores.clear();
                    getLovTiposTrabajadores();
                } else {
                    permitirIndex = false;
                    RequestContext.getCurrentInstance().update("formularioLovTipoTrabajador:TipoTrabajadorDialogo");
                    RequestContext.getCurrentInstance().execute("PF('TipoTrabajadorDialogo').show()");
                    tipoActualizacion = 0;
                }
            } else {
                coincidencias = 1;
                if (tipoLista == 0) {
                    parametroCorreccionSeleccionado.setTipotrabajador(new TiposTrabajadores());
                } else {
                    parametroCorreccionSeleccionado.setTipotrabajador(new TiposTrabajadores());
                }
                lovTiposTrabajadores.clear();
                getLovTiposTrabajadores();
            }
        }
        if (coincidencias == 1) {
            if (tipoLista == 0) {
                if (!listParametrosCorreccionesCrear.contains(parametroCorreccionSeleccionado)) {

                    if (listParametrosCorreccionesModificar.isEmpty()) {
                        listParametrosCorreccionesModificar.add(parametroCorreccionSeleccionado);
                    } else if (!listParametrosCorreccionesModificar.contains(parametroCorreccionSeleccionado)) {
                        listParametrosCorreccionesModificar.add(parametroCorreccionSeleccionado);
                    }
                    if (guardado == true) {
                        guardado = false;
                        RequestContext.getCurrentInstance().update("form:ACEPTAR");
                    }
                }
            } else if (!listParametrosCorreccionesCrear.contains(parametroCorreccionSeleccionado)) {

                if (listParametrosCorreccionesModificar.isEmpty()) {
                    listParametrosCorreccionesModificar.add(parametroCorreccionSeleccionado);
                } else if (!listParametrosCorreccionesModificar.contains(parametroCorreccionSeleccionado)) {
                    listParametrosCorreccionesModificar.add(parametroCorreccionSeleccionado);
                }
                if (guardado == true) {
                    guardado = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
            }              RequestContext.getCurrentInstance().update("form:acumDif");
        }
        RequestContext.getCurrentInstance().update("form:datosParametroAuto");
    }

    public void modificarAporteEntidad(AportesCorrecciones aporte, String confirmarCambio, String valorConfirmar) {
        aporteTablaSeleccionado = aporte;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("TIPOENTIDAD")) {
            if (tipoListaAporte == 0) {
                aporteTablaSeleccionado.getTipoentidad().setNombre(aporteTablaSeleccionado.getTipoentidad().getNombre());
            } else {
                aporteTablaSeleccionado.getTipoentidad().setNombre(aporteTablaSeleccionado.getTipoentidad().getNombre());
            }
            for (int i = 0; i < lovTiposEntidades.size(); i++) {
                if (lovTiposEntidades.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoListaAporte == 0) {
                    aporteTablaSeleccionado.setTipoentidad(lovTiposEntidades.get(indiceUnicoElemento));
                } else {
                    aporteTablaSeleccionado.setTipoentidad(lovTiposEntidades.get(indiceUnicoElemento));
                }
                lovTiposEntidades.clear();
                getLovTiposEntidades();
            } else {
                permitirIndexAporte = false;
                RequestContext.getCurrentInstance().update("formularioLovTipoEntidad:TipoEntidadDialogo");
                RequestContext.getCurrentInstance().execute("PF('TipoEntidadDialogo').show()");
                tipoActualizacion = 0;
            }
        }
        if (confirmarCambio.equalsIgnoreCase("TERCERO")) {
            if (!valorConfirmar.isEmpty()) {
                if (tipoListaAporte == 0) {
                    aporteTablaSeleccionado.getTerceroRegistro().setNit(aporteTablaSeleccionado.getTerceroRegistro().getNit());
                } else {
                    aporteTablaSeleccionado.getTerceroRegistro().setNit(aporteTablaSeleccionado.getTerceroRegistro().getNit());
                }
                for (int i = 0; i < lovTerceros.size(); i++) {
                    if (lovTerceros.get(i).getStrNit().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    if (tipoListaAporte == 0) {
                        aporteTablaSeleccionado.setTerceroRegistro(lovTerceros.get(indiceUnicoElemento));
                    } else {
                        aporteTablaSeleccionado.setTerceroRegistro(lovTerceros.get(indiceUnicoElemento));
                    }
                    lovTerceros.clear();
                    getLovTerceros();
                } else {
                    permitirIndexAporte = false;
                    RequestContext.getCurrentInstance().update("formularioLovTercero:TerceroDialogo");
                    RequestContext.getCurrentInstance().execute("PF('TerceroDialogo').show()");
                    tipoActualizacion = 0;
                }
            } else {
                coincidencias = 1;
                if (tipoListaAporte == 0) {
                    aporteTablaSeleccionado.setTerceroRegistro(new Terceros());
                } else {
                    aporteTablaSeleccionado.setTerceroRegistro(new Terceros());
                }
                lovTerceros.clear();
                getLovTerceros();
            }
        }
        if (confirmarCambio.equalsIgnoreCase("EMPLEADO")) {
            aporteTablaSeleccionado.getEmpleado().getPersona().setNombreCompleto(aporteTablaSeleccionado.getEmpleado().getPersona().getNombreCompleto());
            for (int i = 0; i < lovEmpleados.size(); i++) {
                if (lovEmpleados.get(i).getPersona().getNombreCompleto().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoListaAporte == 0) {
                    aporteTablaSeleccionado.setEmpleado(lovEmpleados.get(indiceUnicoElemento));
                } else {
                    aporteTablaSeleccionado.setEmpleado(lovEmpleados.get(indiceUnicoElemento));
                }
                lovEmpleados.clear();
                getLovEmpleados();
            } else {
                permitirIndexAporte = false;
                RequestContext.getCurrentInstance().update("formularioLovEmpleado:EmpleadoDialogo");
                RequestContext.getCurrentInstance().execute("PF('EmpleadoDialogo').show()");
                tipoActualizacion = 0;
            }
        }
        if (coincidencias == 1) {
            if (tipoListaAporte == 0) {
                if (listAportesCorreccionesModificar.isEmpty()) {
                    listAportesCorreccionesModificar.add(aporteTablaSeleccionado);
                } else if (!listAportesCorreccionesModificar.contains(aporteTablaSeleccionado)) {
                    listAportesCorreccionesModificar.add(aporteTablaSeleccionado);
                }
                if (guardado == true) {
                    guardado = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
            } else {
                if (listAportesCorreccionesModificar.isEmpty()) {
                    listAportesCorreccionesModificar.add(aporteTablaSeleccionado);
                } else if (!listAportesCorreccionesModificar.contains(aporteTablaSeleccionado)) {
                    listAportesCorreccionesModificar.add(aporteTablaSeleccionado);
                }
                if (guardado == true) {
                    guardado = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
            }
        }
        RequestContext.getCurrentInstance().update("form:tablaAportesCorrecciones");
    }

    public void valoresBackupAutocompletar(int tipoNuevo, String Campo) {
        if (Campo.equals("TIPOTRABAJADOR")) {
            if (tipoNuevo == 1) {
                nuevoParametro.getTipotrabajador().getNombre();
            } else if (tipoNuevo == 2) {
                duplicarParametro.getTipotrabajador().getNombre();
            }
        }
        if (Campo.equals("EMPRESA")) {
            if (tipoNuevo == 1) {
                nuevoParametro.getEmpresa().getNombre();
            } else if (tipoNuevo == 2) {
                duplicarParametro.getEmpresa().getNombre();
            }
        }

        if (Campo.equals("EMPLEADO")) {
            if (tipoNuevo == 1) {
                nuevoAporteEntidad.getEmpleado().getPersona().getNombreCompleto();
            } else if (tipoNuevo == 2) {
                duplicarAporteEntidad.getEmpleado().getPersona().getNombreCompleto();
            }
        }

        if (Campo.equals("TERCERO")) {
            if (tipoNuevo == 1) {
                nuevoAporteEntidad.getTerceroRegistro().getNombre();
            } else if (tipoNuevo == 2) {
                duplicarAporteEntidad.getTerceroRegistro().getNombre();
            }
        }

        if (Campo.equals("APORTE")) {
            if (tipoNuevo == 1) {
                nuevoAporteEntidad.getTipoentidad().getNombre();
            } else if (tipoNuevo == 2) {
                duplicarAporteEntidad.getTipoentidad().getNombre();
            }
        }

    }

    public void autocompletarNuevoyDuplicadoParametroAutoliq(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("TIPOTRABAJADOR")) {
            if (!valorConfirmar.isEmpty()) {
                if (tipoNuevo == 1) {
                    nuevoParametro.getTipotrabajador().setNombre(aporteTablaSeleccionado.getTipoentidad().getNombre());
                } else if (tipoNuevo == 2) {
                    duplicarParametro.getTipotrabajador().setNombre(aporteTablaSeleccionado.getTipoentidad().getNombre());
                }
                for (int i = 0; i < lovTiposTrabajadores.size(); i++) {
                    if (lovTiposTrabajadores.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    if (tipoNuevo == 1) {
                        nuevoParametro.setTipotrabajador(lovTiposTrabajadores.get(indiceUnicoElemento));
                        RequestContext.getCurrentInstance().update("formularioDialogos:nuevaTipoTrabajadorParametro");
                    } else if (tipoNuevo == 2) {
                        duplicarParametro.setTipotrabajador(lovTiposTrabajadores.get(indiceUnicoElemento));
                        RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTipoTrabajadorParametro");
                    }
                    lovTiposTrabajadores.clear();
                    getLovTiposTrabajadores();
                } else {
                    tipoActualizacion = tipoNuevo;
                    if (tipoNuevo == 1) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:nuevaTipoTrabajadorParametro");
                    } else if (tipoNuevo == 2) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTipoTrabajadorParametro");
                    }
                    RequestContext.getCurrentInstance().update("formularioLovTipoTrabajador:TipoTrabajadorDialogo");
                    RequestContext.getCurrentInstance().execute("PF('TipoTrabajadorDialogo').show()");
                }
            } else {
                if (tipoNuevo == 1) {
                    nuevoParametro.setTipotrabajador(new TiposTrabajadores());
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevaTipoTrabajadorParametro");
                } else if (tipoNuevo == 2) {
                    duplicarParametro.setTipotrabajador(new TiposTrabajadores());
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTipoTrabajadorParametro");
                }
                lovTiposTrabajadores.clear();
                getLovTiposTrabajadores();
            }
        }
        if (confirmarCambio.equalsIgnoreCase("EMPRESA")) {
            if (tipoNuevo == 1) {
                nuevoParametro.getEmpresa().setNombre(aporteTablaSeleccionado.getEmpresa().getNombre());
            } else if (tipoNuevo == 2) {
                duplicarParametro.getEmpresa().setNombre(aporteTablaSeleccionado.getEmpresa().getNombre());
            }
            for (int i = 0; i < lovEmpresas.size(); i++) {
                if (lovEmpresas.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevoParametro.setEmpresa(lovEmpresas.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevaEmpresaParametro");
                } else if (tipoNuevo == 2) {
                    duplicarParametro.setEmpresa(lovEmpresas.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarEmpresaParametro");
                }
                lovEmpresas.clear();
                getLovEmpresas();
            } else {
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevaEmpresaParametro");
                } else if (tipoNuevo == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarEmpresaParametro");
                }
                RequestContext.getCurrentInstance().update("formularioLovEmpresa:EmpresaDialogo");
                RequestContext.getCurrentInstance().execute("PF('EmpresaDialogo').show()");
            }
        }
    }

    public void posicionParametro() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, String> map = context.getExternalContext().getRequestParameterMap();
        String name = map.get("n"); // name attribute of node
        String type = map.get("t"); // type attribute of node
        int indice = Integer.parseInt(type);
        int columna = Integer.parseInt(name);
        cambiarIndice(listaParametrosCorrecciones.get(indice), columna);

    }

    public void posicionAporteEntidad() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, String> map = context.getExternalContext().getRequestParameterMap();
        String name = map.get("n"); // name attribute of node
        String type = map.get("t"); // type attribute of node
        int indice = Integer.parseInt(type);
        int columna = Integer.parseInt(name);
        cambiarIndiceAporteEntidad(listaAportesCorrecciones.get(indice), columna);
    }

    public void cambiarIndice(ParametrosCorreccionesAutoL parametro, int celda) {
        if (permitirIndex == true) {
            RequestContext context = RequestContext.getCurrentInstance();
            cualTabla = 1;
            RequestContext.getCurrentInstance().execute("PF('operacionEnProceso').hide()");
            parametroCorreccionSeleccionado = parametro;
            System.out.println("parametroCorreccionSeleccionado cambiar indice : " + parametroCorreccionSeleccionado);
            cualCelda = celda;
            parametroCorreccionSeleccionado.getSecuencia();
            if (celda == 0) {
                parametroCorreccionSeleccionado.getAno();
            } else if (celda == 1) {
                parametroCorreccionSeleccionado.getMes();

            } else if (celda == 2) {
                parametroCorreccionSeleccionado.getTipotrabajador();

            } else if (celda == 3) {
                parametroCorreccionSeleccionado.getEmpresa();

            } else if (celda == 4) {
                parametroCorreccionSeleccionado.getTipoplanilla();

            } else if (celda == 5) {
                parametroCorreccionSeleccionado.getCorrigeplanilla();
            } else if (celda == 6) {
                parametroCorreccionSeleccionado.getFechaplanilla();
            }

            if (banderaAporte == 1) {
                desactivarFiltradoAporteEntidad();
                banderaAporte = 0;
                filtrarListaAportesCorrecciones = null;
                tipoListaAporte = 0;
            }
            getParametroCorreccionSeleccionado();
        }
    }

    public void cargarDatosNuevos() {
        try {
            listaAportesCorrecciones = null;
            getListaAportesCorrecciones();
            if (listaAportesCorrecciones != null) {
                modificarInfoRegistroAporte(listaAportesCorrecciones.size());
            }
            Thread.sleep(2000L);
            RequestContext.getCurrentInstance().update("form:tablaAportesCorrecciones");
            RequestContext.getCurrentInstance().execute("PF('operacionEnProceso').hide()");

        } catch (Exception e) {
            System.out.println("Error cargarDatosNuevos Controlador : " + e.toString());
        }
    }

    public void cambiarIndiceAporteEntidad(AportesCorrecciones aporte, int celda) {
        if (permitirIndexAporte == true) {
            aporteTablaSeleccionado = aporte;
            cualCeldaAporte = celda;
            cualTabla = 2;
            if (cualCeldaAporte == 0) {
                aporteTablaSeleccionado.getAno();
            } else if (cualCeldaAporte == 1) {
                aporteTablaSeleccionado.getMes();
            } else if (cualCeldaAporte == 2) {
                aporteTablaSeleccionado.getEmpleado().getPersona().getNombreCompleto();
            } else if (cualCeldaAporte == 3) {
                aporteTablaSeleccionado.getTipoplanilla();
            } else if (cualCeldaAporte == 4) {
                aporteTablaSeleccionado.getTerceroRegistro().getNit();
            } else if (cualCeldaAporte == 5) {
                aporteTablaSeleccionado.getTerceroRegistro().getNombre();
            } else if (cualCeldaAporte == 6) {
                aporteTablaSeleccionado.getTipoentidad().getNombre();
            } else if (cualCeldaAporte == 7) {
                aporteTablaSeleccionado.getCotizacion1();
            } else if (cualCeldaAporte == 8) {
                aporteTablaSeleccionado.getCotizacion2();
            } else if (cualCeldaAporte == 9) {
                aporteTablaSeleccionado.getCotizacion5();
            } else if (cualCeldaAporte == 10) {
                aporteTablaSeleccionado.getNovedad3();
            } else if (cualCeldaAporte == 11) {
                aporteTablaSeleccionado.getNovedad4();
            } else if (cualCeldaAporte == 12) {
                aporteTablaSeleccionado.getNovedad5();
            } else if (cualCeldaAporte == 13) {
                aporteTablaSeleccionado.getSalariobasico();
            } else if (cualCeldaAporte == 14) {
                aporteTablaSeleccionado.getIbc();
            } else if (cualCeldaAporte == 15) {
                aporteTablaSeleccionado.getIbcreferencia();
            } else if (cualCeldaAporte == 16) {
                aporteTablaSeleccionado.getDiascotizados();
            } else if (cualCeldaAporte == 17) {
                aporteTablaSeleccionado.getTipoaportante();
            } else if (cualCeldaAporte == 18) {
                aporteTablaSeleccionado.getIng();
            } else if (cualCeldaAporte == 19) {
                aporteTablaSeleccionado.getRet();
            } else if (cualCeldaAporte == 20) {
                aporteTablaSeleccionado.getTda();
            } else if (cualCeldaAporte == 21) {
                aporteTablaSeleccionado.getTaa();
            } else if (cualCeldaAporte == 22) {
                aporteTablaSeleccionado.getVsp();
            } else if (cualCeldaAporte == 23) {
                aporteTablaSeleccionado.getVte();
            } else if (cualCeldaAporte == 24) {
                aporteTablaSeleccionado.getVst();
            } else if (cualCeldaAporte == 25) {
                aporteTablaSeleccionado.getSln();
            } else if (cualCeldaAporte == 26) {
                aporteTablaSeleccionado.getIge();
            } else if (cualCeldaAporte == 27) {
                aporteTablaSeleccionado.getLma();
            } else if (cualCeldaAporte == 28) {
                aporteTablaSeleccionado.getVac();
            } else if (cualCeldaAporte == 29) {
                aporteTablaSeleccionado.getAvp();
            } else if (cualCeldaAporte == 30) {
                aporteTablaSeleccionado.getVct();
            } else if (cualCeldaAporte == 31) {
                aporteTablaSeleccionado.getIrp();
            } else if (cualCeldaAporte == 32) {
                aporteTablaSeleccionado.getSus();
            } else if (cualCeldaAporte == 33) {
                aporteTablaSeleccionado.getIntegral();
            } else if (cualCeldaAporte == 34) {
                aporteTablaSeleccionado.getTarifaeps();
            } else if (cualCeldaAporte == 35) {
                aporteTablaSeleccionado.getTarifaafp();
            } else if (cualCeldaAporte == 36) {
                aporteTablaSeleccionado.getTarifactt();
            } else if (cualCeldaAporte == 37) {
                aporteTablaSeleccionado.getCodigoctt();
            } else if (cualCeldaAporte == 38) {
                aporteTablaSeleccionado.getAvpevalor();
            } else if (cualCeldaAporte == 39) {
                aporteTablaSeleccionado.getAvppvalor();
            } else if (cualCeldaAporte == 40) {
                aporteTablaSeleccionado.getRetcontavpvalor();
            } else if (cualCeldaAporte == 41) {
                aporteTablaSeleccionado.getCodigoneps();
            } else if (cualCeldaAporte == 42) {
                aporteTablaSeleccionado.getCodigonafp();
            } else if (cualCeldaAporte == 43) {
                aporteTablaSeleccionado.getEgvalor();
            } else if (cualCeldaAporte == 44) {
                aporteTablaSeleccionado.getEgautorizacion();
            } else if (cualCeldaAporte == 45) {
                aporteTablaSeleccionado.getMaternidadvalor();
            } else if (cualCeldaAporte == 46) {
                aporteTablaSeleccionado.getMaternidadautorizacion();
            } else if (cualCeldaAporte == 47) {
                aporteTablaSeleccionado.getUpcvalor();
            } else if (cualCeldaAporte == 48) {
                aporteTablaSeleccionado.getTipo();
            } else if (cualCeldaAporte == 49) {
                aporteTablaSeleccionado.getTipopensionado();
            } else if (cualCeldaAporte == 50) {
                aporteTablaSeleccionado.getPensioncompartida();
            } else if (cualCeldaAporte == 51) {
                aporteTablaSeleccionado.getFechaorigen();
            }
        }
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
        if (guardado == false) {
            guardarCambiosParametro();
            guardarCambiosAportes();
            visibilidadMostrarTodos = "hidden";
            RequestContext.getCurrentInstance().update("form:mostrarTodos");
        }
    }

    public void guardarCambiosParametro() {
        RequestContext context = RequestContext.getCurrentInstance();
        try {
            if (!listParametrosCorreccionesBorrar.isEmpty()) {
                administrarParametroCorreccionAutoL.borrarParametrosCorreccionAutoliq(listParametrosCorreccionesBorrar);
                listParametrosCorreccionesBorrar.clear();
            }
            if (!listParametrosCorreccionesCrear.isEmpty()) {
                administrarParametroCorreccionAutoL.crearParametrosCorreccionAutoliq(listParametrosCorreccionesCrear);
                listParametrosCorreccionesCrear.clear();
            }
            if (!listParametrosCorreccionesModificar.isEmpty()) {
                administrarParametroCorreccionAutoL.editarParametrosCorreccionAutoliq(listParametrosCorreccionesModificar);
                listParametrosCorreccionesModificar.clear();
            }
            listaParametrosCorrecciones = null;
            getListaParametrosCorrecciones();
            if (listaParametrosCorrecciones != null) {
                modificarInfoRegistroParametro(listaParametrosCorrecciones.size());
            }
            RequestContext.getCurrentInstance().update("form:datosParametroAuto");
            k = 0;
            guardado = true;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        } catch (Exception e) {
            System.out.println("Error guardarCambiosParametro  Controlador : " + e.toString());
            FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado de Parámetros de Liquidación, Por favor intente nuevamente.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }
    }

    public void guardarCambiosAportes() {
        RequestContext context = RequestContext.getCurrentInstance();
        try {
            System.out.println("entró a guardar");
            if (!listAportesCorreccionesBorrar.isEmpty()) {
                System.out.println("entró a lista correcciones borrar, tamaño lista borrar : " + listAportesCorreccionesBorrar.size());
                administrarParametroCorreccionAutoL.borrarAportesCorrecciones(listAportesCorreccionesBorrar);
                listAportesCorreccionesBorrar.clear();
            }
            if (!listAportesCorreccionesCrear.isEmpty()) {
                System.out.println("entró a lista correcciones crear , tamaño lista crear : " + listAportesCorreccionesCrear.size());
                administrarParametroCorreccionAutoL.crearAportesCorrecciones(listAportesCorreccionesCrear);
                listAportesCorreccionesCrear.clear();
            }

            if (!listAportesCorreccionesModificar.isEmpty()) {
                System.out.println("entró a lista correcciones modificar, tamaño lista modificar : " + listAportesCorreccionesModificar.size());
                administrarParametroCorreccionAutoL.editarAportesCorrecciones(listAportesCorreccionesModificar);
                listAportesCorreccionesModificar.clear();
            }
            listaAportesCorrecciones = null;
            getListaAportesCorrecciones();
            System.out.println("tamaño lista aportes : " + listaAportesCorrecciones.size());
            RequestContext.getCurrentInstance().update("form:tablaAportesCorrecciones");
            modificarInfoRegistroAporte(listaAportesCorrecciones.size());
            k = 0;
            FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
            guardado = true;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        } catch (Exception e) {
            System.out.println("Error guardarCambiosAportes  Controlador : " + e.toString());
            FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado de Aporte Entidad Corrección, Por favor intente nuevamente.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }
    }

    public void cancelarModificacion() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 1) {
            //CERRAR FILTRADO
            altoTabla = "40";
            parametroAno = (Column) c.getViewRoot().findComponent("form:datosParametroAuto:parametroAno");
            parametroAno.setFilterStyle("display: none; visibility: hidden;");
            parametroMes = (Column) c.getViewRoot().findComponent("form:datosParametroAuto:parametroMes");
            parametroMes.setFilterStyle("display: none; visibility: hidden;");
            parametroTipoTrabajador = (Column) c.getViewRoot().findComponent("form:datosParametroAuto:parametroTipoTrabajador");
            parametroTipoTrabajador.setFilterStyle("display: none; visibility: hidden;");
            parametroEmpresa = (Column) c.getViewRoot().findComponent("form:datosParametroAuto:parametroEmpresa");
            parametroEmpresa.setFilterStyle("display: none; visibility: hidden;");
            tipoPlanilla = (Column) c.getViewRoot().findComponent("form:datosParametrosAuto:parametroTipoPlanilla");
            tipoPlanilla.setFilterStyle("display: none; visibility: hidden;");
            planilla = (Column) c.getViewRoot().findComponent("form:datosParametrosAuto:parametroPlanilla");
            planilla.setFilterStyle("display: none; visibility: hidden;");
            fechaplan = (Column) c.getViewRoot().findComponent("form:datosParametrosAuto:aporteFechaPlanilla");
            fechaplan.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosParametroAuto");
            bandera = 0;
            filtrarListaParametrosCorrecciones = null;
            tipoLista = 0;
        }
        if (banderaAporte == 1) {
            desactivarFiltradoAporteEntidad();
            banderaAporte = 0;
            filtrarListaAportesCorrecciones = null;
            tipoListaAporte = 0;
        }
        visibilidadMostrarTodos = "hidden";
        RequestContext.getCurrentInstance().update("form:mostrarTodos");
        //
        listParametrosCorreccionesCrear.clear();
        listParametrosCorreccionesBorrar.clear();
        listParametrosCorreccionesModificar.clear();
        //
        listAportesCorreccionesBorrar.clear();
        listAportesCorreccionesModificar.clear();
        listAportesCorreccionesCrear.clear();
        parametroCorreccionSeleccionado = null;
        aporteTablaSeleccionado = null;
        k = 0;
        listaParametrosCorrecciones = null;
        getListaParametrosCorrecciones();
        if (listaParametrosCorrecciones != null) {
            modificarInfoRegistroParametro(listaParametrosCorrecciones.size());
        }
        listaAportesCorrecciones = null;
        getListaAportesCorrecciones();
        if (listaAportesCorrecciones != null) {
            modificarInfoRegistroAporte(listaAportesCorrecciones.size());
        } else {
            modificarInfoRegistroAporte(0);
        }
        guardado = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:tablaAportesCorrecciones");
        RequestContext.getCurrentInstance().update("form:datosParametroAuto");
    }

    public void editarCelda() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (cualTabla == 1) {
            if (parametroCorreccionSeleccionado != null) {
                editarParametro = parametroCorreccionSeleccionado;
                if (cualCelda == 0) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarAnoD");
                    RequestContext.getCurrentInstance().execute("PF('editarAnoD').show()");
                    cualCelda = -1;
                } else if (cualCelda == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarMesD");
                    RequestContext.getCurrentInstance().execute("PF('editarMesD').show()");
                    cualCelda = -1;
                } else if (cualCelda == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarTipoTrabajadorD");
                    RequestContext.getCurrentInstance().execute("PF('editarTipoTrabajadorD').show()");
                    cualCelda = -1;
                } else if (cualCelda == 3) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarEmpresaD");
                    RequestContext.getCurrentInstance().execute("PF('editarEmpresaD').show()");
                    cualCelda = -1;
                } else if (cualCelda == 4) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarTipoPlanillaD");
                    RequestContext.getCurrentInstance().execute("PF('editarTipoPlanillaD').show()");
                    cualCelda = -1;
                } else if (cualCelda == 5) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarPlanillaD");
                    RequestContext.getCurrentInstance().execute("PF('editarPlanillaD').show()");
                    cualCelda = -1;
                } else if (cualCelda == 6) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaPlanillaD");
                    RequestContext.getCurrentInstance().execute("PF('editarFechaPlanillaD').show()");
                }
                cualCelda = -1;
            }
        } else if (cualTabla == 2) {
            if (aporteTablaSeleccionado != null) {
                if (tipoListaAporte == 0) {
                    editarAporteEntidad = aporteTablaSeleccionado;
                } else {
                    editarAporteEntidad = aporteTablaSeleccionado;
                }
                if (cualCeldaAporte == 0) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarAnoAD");
                    RequestContext.getCurrentInstance().execute("PF('editarAnoAD').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarMesAD");
                    RequestContext.getCurrentInstance().execute("PF('editarMesAD').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarNombreEmplD");
                    RequestContext.getCurrentInstance().execute("PF('editarNombreEmplD').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 3) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarTipoPlanillaD");
                    RequestContext.getCurrentInstance().execute("PF('editarTipoPlanillaD').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 4) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarNitTerceroD");
                    RequestContext.getCurrentInstance().execute("PF('editarNitTerceroD').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 5) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarNombreTerceroD");
                    RequestContext.getCurrentInstance().execute("PF('editarNombreTerceroD').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 6) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarTipoEntidadD");
                    RequestContext.getCurrentInstance().execute("PF('editarTipoEntidadD').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 7) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarEmpleadoD");
                    RequestContext.getCurrentInstance().execute("PF('editarEmpleadoD').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 8) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarEmpleadorD");
                    RequestContext.getCurrentInstance().execute("PF('editarEmpleadorD').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 9) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarAjustePatronalD");
                    RequestContext.getCurrentInstance().execute("PF('editarAjustePatronalD').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 10) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarSolidaridadD");
                    RequestContext.getCurrentInstance().execute("PF('editarSolidaridadD').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 11) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarSubSistenciaD");
                    RequestContext.getCurrentInstance().execute("PF('editarSubSistenciaD').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 12) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarSubsPensionadosD");
                    RequestContext.getCurrentInstance().execute("PF('editarSubsPensionadosD').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 13) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarSalarioBasicoD");
                    RequestContext.getCurrentInstance().execute("PF('editarSalarioBasicoD').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 14) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarIBCD");
                    RequestContext.getCurrentInstance().execute("PF('editarIBCD').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 15) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarIBCReferenciaD");
                    RequestContext.getCurrentInstance().execute("PF('editarIBCReferenciaD').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 16) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarDiasCotizadosD");
                    RequestContext.getCurrentInstance().execute("PF('editarDiasCotizadosD').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 17) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarTipoAportanteD");
                    RequestContext.getCurrentInstance().execute("PF('editarTipoAportanteD').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 18) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarINGD");
                    RequestContext.getCurrentInstance().execute("PF('editarINGD').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 19) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarRETD");
                    RequestContext.getCurrentInstance().execute("PF('editarRETD').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 20) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarTDAD");
                    RequestContext.getCurrentInstance().execute("PF('editarTDAD').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 21) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarTAAD");
                    RequestContext.getCurrentInstance().execute("PF('editarTAAD').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 22) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarVSPD");
                    RequestContext.getCurrentInstance().execute("PF('editarVSPD').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 23) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarVTED");
                    RequestContext.getCurrentInstance().execute("PF('editarVTED').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 24) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarVSTD");
                    RequestContext.getCurrentInstance().execute("PF('editarVSTD').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 25) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarSLND");
                    RequestContext.getCurrentInstance().execute("PF('editarSLND').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 26) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarIGED");
                    RequestContext.getCurrentInstance().execute("PF('editarIGED').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 27) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarLMAD");
                    RequestContext.getCurrentInstance().execute("PF('editarLMAD').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 28) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarVCAD");
                    RequestContext.getCurrentInstance().execute("PF('editarVCAD').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 29) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarAVPD");
                    RequestContext.getCurrentInstance().execute("PF('editarAVPD').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 30) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarVCTD");
                    RequestContext.getCurrentInstance().execute("PF('editarVCTD').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 31) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarIRPD");
                    RequestContext.getCurrentInstance().execute("PF('editarIRPD').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 32) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarSUSD");
                    RequestContext.getCurrentInstance().execute("PF('editarSUSD').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 33) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarINTED");
                    RequestContext.getCurrentInstance().execute("PF('editarINTED').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 34) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarTarifaEPSD");
                    RequestContext.getCurrentInstance().execute("PF('editarTarifaEPSD').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 35) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarTarifaAAFPD");
                    RequestContext.getCurrentInstance().execute("PF('editarTarifaAAFPD').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 36) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarTarifaACTTD");
                    RequestContext.getCurrentInstance().execute("PF('editarTarifaACTTD').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 37) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarCodigoCTTD");
                    RequestContext.getCurrentInstance().execute("PF('editarCodigoCTTD').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 38) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarAvpeValorD");
                    RequestContext.getCurrentInstance().execute("PF('editarAvpeValorD').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 39) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarAvppValorD");
                    RequestContext.getCurrentInstance().execute("PF('editarAvppValorD').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 40) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarRetcontaValorD");
                    RequestContext.getCurrentInstance().execute("PF('editarRetcontaValorD').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 41) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarCodigoNEPSD");
                    RequestContext.getCurrentInstance().execute("PF('editarCodigoNEPSD').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 42) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarCodigoNAFPD");
                    RequestContext.getCurrentInstance().execute("PF('editarCodigoNAFPD').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 43) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarEgValorD");
                    RequestContext.getCurrentInstance().execute("PF('editarEgValorD').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 44) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarEgAutorizacionD");
                    RequestContext.getCurrentInstance().execute("PF('editarEgAutorizacionD').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 45) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarMaternidadValorD");
                    RequestContext.getCurrentInstance().execute("PF('editarMaternidadValorD').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 46) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarMaternidadAutoD");
                    RequestContext.getCurrentInstance().execute("PF('editarMaternidadAutoD').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 47) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarUpcValorD");
                    RequestContext.getCurrentInstance().execute("PF('editarUpcValorD').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 48) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarTipoD");
                    RequestContext.getCurrentInstance().execute("PF('editarTipoD').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 49) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarTPD");
                    RequestContext.getCurrentInstance().execute("PF('editarTPD').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 50) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarPCD");
                    RequestContext.getCurrentInstance().execute("PF('editarPCD').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 51) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaIngreso");
                    RequestContext.getCurrentInstance().execute("PF('editarFechaIngreso').show()");
                    cualCeldaAporte = -1;
                }
            }
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void mostrarDialogoNuevoAporte() {
        nuevoAporteEntidad.setAno(parametroCorreccionSeleccionado.getAno());
        nuevoAporteEntidad.setMes(parametroCorreccionSeleccionado.getMes());
        RequestContext.getCurrentInstance().update("formularioDialogos:nuevoAporteEntidad");
        RequestContext.getCurrentInstance().execute("PF('nuevoAporteEntidad').show()");
    }

    public void mostrarDialogoNuevoParametro() {
        RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroParametro");
        RequestContext.getCurrentInstance().execute("PF('NuevoRegistroParametro').show()");
    }

    public void mostrarDialogoElegirTabla() {
        RequestContext.getCurrentInstance().update("formularioDialogos:seleccionarTablaNewReg");
        RequestContext.getCurrentInstance().execute("PF('seleccionarTablaNewReg').show()");
    }

    public void agregarNuevaParametroAutoliq() {
        if (nuevoParametro.getAno() > 0 && nuevoParametro.getMes() > 0) {
            if (bandera == 1) {
                altoTabla = "40";
                FacesContext c = FacesContext.getCurrentInstance();
                //CERRAR FILTRADO
                parametroAno = (Column) c.getViewRoot().findComponent("form:datosParametroAuto:parametroAno");
                parametroAno.setFilterStyle("display: none; visibility: hidden;");
                parametroMes = (Column) c.getViewRoot().findComponent("form:datosParametroAuto:parametroMes");
                parametroMes.setFilterStyle("display: none; visibility: hidden;");
                parametroTipoTrabajador = (Column) c.getViewRoot().findComponent("form:datosParametroAuto:parametroTipoTrabajador");
                parametroTipoTrabajador.setFilterStyle("display: none; visibility: hidden;");
                parametroEmpresa = (Column) c.getViewRoot().findComponent("form:datosParametroAuto:parametroEmpresa");
                parametroEmpresa.setFilterStyle("display: none; visibility: hidden;");
                tipoPlanilla = (Column) c.getViewRoot().findComponent("form:datosParametrosAuto:parametroTipoPlanilla");
                tipoPlanilla.setFilterStyle("display: none; visibility: hidden;");
                planilla = (Column) c.getViewRoot().findComponent("form:datosParametrosAuto:parametroPlanilla");
                planilla.setFilterStyle("display: none; visibility: hidden;");
                fechaplan = (Column) c.getViewRoot().findComponent("form:datosParametrosAuto:aporteFechaPlanilla");
                fechaplan.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosParametroAuto");
                bandera = 0;
                filtrarListaParametrosCorrecciones = null;
                tipoLista = 0;
            }
            if (banderaAporte == 1) {
                desactivarFiltradoAporteEntidad();
                banderaAporte = 0;
                filtrarListaAportesCorrecciones = null;
                tipoListaAporte = 0;
            }
            //AGREGAR REGISTRO A LA LISTA VIGENCIAS CARGOS EMPLEADO.
            k++;
            l = BigInteger.valueOf(k);
            nuevoParametro.setSecuencia(l);
            listParametrosCorreccionesCrear.add(nuevoParametro);
            if (listaParametrosCorrecciones == null) {
                listaParametrosCorrecciones = new ArrayList<ParametrosCorreccionesAutoL>();
            }
            listaParametrosCorrecciones.add(nuevoParametro);
            parametroCorreccionSeleccionado = nuevoParametro;
            cambiarIndice(nuevoParametro, cualCelda);
            nuevoParametro = new ParametrosCorreccionesAutoL();
            nuevoParametro.setTipotrabajador(new TiposTrabajadores());
            nuevoParametro.setEmpresa(new Empresas());
            RequestContext context = RequestContext.getCurrentInstance();

            modificarInfoRegistroParametro(listaParametrosCorrecciones.size());

            RequestContext.getCurrentInstance().update("form:datosParametroAuto");
            RequestContext.getCurrentInstance().execute("PF('NuevoRegistroParametro').hide()");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('errorRegNullParametro').show()");
        }

    }

    public void agregarNuevoAporteEntidad() {

        if (banderaAporte == 1) {
            desactivarFiltradoAporteEntidad();
            banderaAporte = 0;
            filtrarListaAportesCorrecciones = null;
            tipoListaAporte = 0;
            tipoLista = 0;
        }
        //AGREGAR REGISTRO A LA LISTA VIGENCIAS CARGOS EMPLEADO.
        k++;
        l = BigInteger.valueOf(k);
        nuevoAporteEntidad.setSecuencia(l);
        listAportesCorreccionesCrear.add(nuevoAporteEntidad);
        if (listaAportesCorrecciones == null) {
            listaAportesCorrecciones = new ArrayList<AportesCorrecciones>();
        }
        listaAportesCorrecciones.add(nuevoAporteEntidad);
        aporteTablaSeleccionado = listaAportesCorrecciones.get(listaAportesCorrecciones.indexOf(nuevoAporteEntidad));
        nuevoAporteEntidad = new AportesCorrecciones();
        nuevoAporteEntidad.setAno(parametroCorreccionSeleccionado.getAno());
        nuevoAporteEntidad.setMes(parametroCorreccionSeleccionado.getMes());
        nuevoAporteEntidad.setEmpresa(parametroCorreccionSeleccionado.getEmpresa());
        nuevoAporteEntidad.setTerceroRegistro(new Terceros());
        nuevoAporteEntidad.setTipoentidad(new TiposEntidades());
        nuevoAporteEntidad.setEmpleado(new Empleados());
        RequestContext context = RequestContext.getCurrentInstance();

        modificarInfoRegistroAporte(listaAportesCorrecciones.size());

        RequestContext.getCurrentInstance().update("form:tablaAportesCorrecciones");
        RequestContext.getCurrentInstance().execute("PF('formularioDialogos:nuevoAporteEntidad').hide()");
        if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
    }

    public void limpiarNuevaParametroAutoliq() {
        nuevoParametro = new ParametrosCorreccionesAutoL();
        nuevoParametro.setTipotrabajador(new TiposTrabajadores());
        nuevoParametro.setEmpresa(new Empresas());
    }

    public void duplicarAporteEntidad() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (aporteTablaSeleccionado != null) {
            duplicarAporteEntidad = new AportesCorrecciones();

            if (tipoLista == 0) {

                duplicarAporteEntidad.setAno(aporteTablaSeleccionado.getAno());
                duplicarAporteEntidad.setMes(aporteTablaSeleccionado.getMes());
                duplicarAporteEntidad.setEmpresa(aporteTablaSeleccionado.getEmpresa());
                duplicarAporteEntidad.setTerceroRegistro(new Terceros());
                duplicarAporteEntidad.setTipoentidad(new TiposEntidades());
                duplicarAporteEntidad.setEmpleado(new Empleados());
            }
            if (tipoLista == 1) {

                duplicarAporteEntidad.setAno(aporteTablaSeleccionado.getAno());
                duplicarAporteEntidad.setMes(aporteTablaSeleccionado.getMes());
                duplicarAporteEntidad.setEmpresa(aporteTablaSeleccionado.getEmpresa());
                duplicarAporteEntidad.setTerceroRegistro(new Terceros());
                duplicarAporteEntidad.setTipoentidad(new TiposEntidades());
                duplicarAporteEntidad.setEmpleado(new Empleados());

            }
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarAporteEntidad");
            RequestContext.getCurrentInstance().execute("PF('duplicarAporteEntidad').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('formularioDialogos:seleccionarRegistro').show()");
        }
    }

    public void confirmarDuplicarAporteEntidad() {
        FacesContext c = FacesContext.getCurrentInstance();

        if (banderaAporte == 1) {
            desactivarFiltradoAporteEntidad();
            banderaAporte = 0;
            filtrarListaAportesCorrecciones = null;
            tipoListaAporte = 0;
            tipoLista = 0;
        }
        k++;
        l = BigInteger.valueOf(k);
        duplicarAporteEntidad.setSecuencia(l);
        listAportesCorreccionesCrear.add(duplicarAporteEntidad);

        if (listaAportesCorrecciones == null) {
            listaAportesCorrecciones = new ArrayList<AportesCorrecciones>();
        }
        listaAportesCorrecciones.add(duplicarAporteEntidad);
        aporteTablaSeleccionado = duplicarAporteEntidad;
        duplicarAporteEntidad = new AportesCorrecciones();
        duplicarAporteEntidad.setAno(aporteTablaSeleccionado.getAno());
        duplicarAporteEntidad.setMes(aporteTablaSeleccionado.getMes());
        duplicarAporteEntidad.setEmpresa(aporteTablaSeleccionado.getEmpresa());
        duplicarAporteEntidad.setTerceroRegistro(new Terceros());
        duplicarAporteEntidad.setTipoentidad(new TiposEntidades());
        duplicarAporteEntidad.setEmpleado(new Empleados());
        RequestContext context = RequestContext.getCurrentInstance();
        modificarInfoRegistroAporte(listaAportesCorrecciones.size());
        RequestContext.getCurrentInstance().update("form:tablaAportesCorrecciones");
        RequestContext.getCurrentInstance().execute("PF('formularioDialogos:duplicarAporteEntidad').hide()");
        if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }

    }

    public void duplicarParametroAutoliq() {
        if (cualTabla == 1) {
            if (parametroCorreccionSeleccionado != null) {
                RequestContext context = RequestContext.getCurrentInstance();
                duplicarParametro = new ParametrosCorreccionesAutoL();
                if (tipoLista == 0) {
                    duplicarParametro.setAno(parametroCorreccionSeleccionado.getAno());
                    duplicarParametro.setEmpresa(parametroCorreccionSeleccionado.getEmpresa());
                    duplicarParametro.setMes(parametroCorreccionSeleccionado.getMes());
                    duplicarParametro.setTipotrabajador(parametroCorreccionSeleccionado.getTipotrabajador());
                }
                if (tipoLista == 1) {
                    duplicarParametro.setAno(parametroCorreccionSeleccionado.getAno());
                    duplicarParametro.setEmpresa(parametroCorreccionSeleccionado.getEmpresa());
                    duplicarParametro.setMes(parametroCorreccionSeleccionado.getMes());
                    duplicarParametro.setTipotrabajador(parametroCorreccionSeleccionado.getTipotrabajador());

                }
                visibilidadMostrarTodos = "hidden";
                RequestContext.getCurrentInstance().update("form:mostrarTodos");
                RequestContext.getCurrentInstance().update("formularioDialogos:duplicarParametro");
                RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroParametro').show()");
            }
        } else if (cualTabla == 2) {
            if (aporteTablaSeleccionado != null) {
                duplicarAporteEntidad();
            }
        } else {
            RequestContext.getCurrentInstance().execute("PF('formularioDialogos:seleccionarRegistro()");
        }
    }

    public void confirmarDuplicarParametroAutoliq() {
        if (duplicarParametro.getAno() > 0 && duplicarParametro.getMes() > 0) {
            if (bandera == 1) {
                altoTabla = "40";
                FacesContext c = FacesContext.getCurrentInstance();
                parametroAno = (Column) c.getViewRoot().findComponent("form:datosParametroAuto:parametroAno");
                parametroAno.setFilterStyle("display: none; visibility: hidden;");
                parametroMes = (Column) c.getViewRoot().findComponent("form:datosParametroAuto:parametroMes");
                parametroMes.setFilterStyle("display: none; visibility: hidden;");
                parametroTipoTrabajador = (Column) c.getViewRoot().findComponent("form:datosParametroAuto:parametroTipoTrabajador");
                parametroTipoTrabajador.setFilterStyle("display: none; visibility: hidden;");
                parametroEmpresa = (Column) c.getViewRoot().findComponent("form:datosParametroAuto:parametroEmpresa");
                parametroEmpresa.setFilterStyle("display: none; visibility: hidden;");
                tipoPlanilla = (Column) c.getViewRoot().findComponent("form:datosParametrosAuto:parametroTipoPlanilla");
                tipoPlanilla.setFilterStyle("display: none; visibility: hidden;");
                planilla = (Column) c.getViewRoot().findComponent("form:datosParametrosAuto:parametroPlanilla");
                planilla.setFilterStyle("display: none; visibility: hidden;");
                fechaplan = (Column) c.getViewRoot().findComponent("form:datosParametrosAuto:aporteFechaPlanilla");
                fechaplan.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosParametroAuto");
                bandera = 0;
                filtrarListaParametrosCorrecciones = null;
                tipoLista = 0;
            }
            if (banderaAporte == 1) {
                desactivarFiltradoAporteEntidad();
                banderaAporte = 0;
                filtrarListaAportesCorrecciones = null;
                tipoListaAporte = 0;
            }
            k++;
            l = BigInteger.valueOf(k);
            duplicarParametro.setSecuencia(l);
            listaParametrosCorrecciones.add(duplicarParametro);
            listParametrosCorreccionesCrear.add(duplicarParametro);
            parametroCorreccionSeleccionado = duplicarParametro;
            RequestContext context = RequestContext.getCurrentInstance();

            modificarInfoRegistroParametro(listaParametrosCorrecciones.size());

            RequestContext.getCurrentInstance().update("form:datosParametroAuto");
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroParametro').hide()");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }

            duplicarParametro = new ParametrosCorreccionesAutoL();
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('errorRegNullParametro').show()");
        }
    }

    public void limpiarDuplicarParametroAutoliq() {
        duplicarParametro = new ParametrosCorreccionesAutoL();
        duplicarParametro.setEmpresa(new Empresas());
        duplicarParametro.setTipotrabajador(new TiposTrabajadores());
    }

    public void validarBorradoPagina() {
        if (cualTabla == 1) {
            borrarParametroAutoliq();
        } else if (cualTabla == 2) {
            borrarAporteEntidad();
        }
    }

    public void limpiarNuevoAporteEntidad() {
        nuevoAporteEntidad = new AportesCorrecciones();
        nuevoAporteEntidad.setAno(parametroCorreccionSeleccionado.getAno());
        nuevoAporteEntidad.setMes(parametroCorreccionSeleccionado.getMes());
        nuevoAporteEntidad.setEmpresa(parametroCorreccionSeleccionado.getEmpresa());
        nuevoAporteEntidad.setTerceroRegistro(new Terceros());
        nuevoAporteEntidad.setTipoentidad(new TiposEntidades());
        nuevoAporteEntidad.setEmpleado(new Empleados());
    }

    public void limpiarDuplicarAporteEntidad() {
        duplicarAporteEntidad = new AportesCorrecciones();
        duplicarAporteEntidad.setAno(parametroCorreccionSeleccionado.getAno());
        duplicarAporteEntidad.setMes(parametroCorreccionSeleccionado.getMes());
        duplicarAporteEntidad.setEmpresa(parametroCorreccionSeleccionado.getEmpresa());
        duplicarAporteEntidad.setTerceroRegistro(new Terceros());
        duplicarAporteEntidad.setTipoentidad(new TiposEntidades());
        duplicarAporteEntidad.setEmpleado(new Empleados());
    }

    public void borrarParametroAutoliq() {
        if (listaParametrosCorrecciones != null) {
            if (listaAportesCorrecciones.size() == 0) {
                if (!listParametrosCorreccionesModificar.isEmpty() && listParametrosCorreccionesModificar.contains(parametroCorreccionSeleccionado)) {
                    int modIndex = listParametrosCorreccionesModificar.indexOf(parametroCorreccionSeleccionado);
                    listParametrosCorreccionesModificar.remove(modIndex);
                    listParametrosCorreccionesBorrar.add(parametroCorreccionSeleccionado);
                } else if (!listParametrosCorreccionesCrear.isEmpty() && listParametrosCorreccionesCrear.contains(parametroCorreccionSeleccionado)) {
                    int crearIndex = listParametrosCorreccionesCrear.indexOf(parametroCorreccionSeleccionado);
                    listParametrosCorreccionesCrear.remove(crearIndex);
                } else {
                    listParametrosCorreccionesBorrar.add(parametroCorreccionSeleccionado);
                }
                listaParametrosCorrecciones.remove(parametroCorreccionSeleccionado);
                if (tipoLista == 1) {
                    filtrarListaParametrosCorrecciones.remove(parametroCorreccionSeleccionado);
                }
                RequestContext context = RequestContext.getCurrentInstance();
                modificarInfoRegistroParametro(listaParametrosCorrecciones.size());
                RequestContext.getCurrentInstance().update("form:datosParametroAuto");
                parametroCorreccionSeleccionado = null;
                if (guardado == true) {
                    guardado = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
            } else {
                RequestContext.getCurrentInstance().execute("PF('errorBorrarParametro').show()");
            }
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void borrarAporteEntidad() {
        if (aporteTablaSeleccionado != null) {
            System.out.println("entró a borrar aportes entidades");
            System.out.println("lista de aportes entidades antes de borrar : " + listaAportesCorrecciones.size());
            if (!listAportesCorreccionesModificar.isEmpty() && listAportesCorreccionesModificar.contains(aporteTablaSeleccionado)) {
                int modIndex = listAportesCorreccionesModificar.indexOf(aporteTablaSeleccionado);
                listAportesCorreccionesModificar.remove(modIndex);
                listAportesCorreccionesBorrar.add(aporteTablaSeleccionado);
            } else if (!listAportesCorreccionesCrear.isEmpty() && listAportesCorreccionesCrear.contains(aporteTablaSeleccionado)) {
                listAportesCorreccionesBorrar.remove(listAportesCorreccionesCrear.indexOf(aporteTablaSeleccionado));
            } else {
                listAportesCorreccionesBorrar.add(aporteTablaSeleccionado);
            }
            listaAportesCorrecciones.remove(aporteTablaSeleccionado);
            if (tipoLista == 1) {
                filtrarListaAportesCorrecciones.remove(aporteTablaSeleccionado);
            }
            System.out.println("lista de aportes entidades después de borrar : " + listaAportesCorrecciones.size());
            modificarInfoRegistroAporte(listaAportesCorrecciones.size());
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:tablaAportesCorrecciones");
            aporteTablaSeleccionado = null;
            System.out.println("se borró un registro de aportes entidad");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void borrarAporteEntidadProcesoAutomatico() {
        RequestContext context = RequestContext.getCurrentInstance();
        try {
            if (listaAportesCorrecciones != null) {
                if (!listaAportesCorrecciones.isEmpty()) {
                    administrarParametroCorreccionAutoL.borrarAportesCorreccionesProcesoAutomatico(parametroCorreccionSeleccionado.getEmpresa().getSecuencia(), parametroCorreccionSeleccionado.getMes(), parametroCorreccionSeleccionado.getAno());
                    listaAportesCorrecciones = null;
                    modificarInfoRegistroAporte(0);
                    disabledBuscar = true;
                    visibilidadMostrarTodos = "hidden";
                    RequestContext.getCurrentInstance().update("form:mostrarTodos");
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                    RequestContext.getCurrentInstance().update("form:buscar");
                    RequestContext.getCurrentInstance().update("form:datosParametroAuto");
                    RequestContext.getCurrentInstance().update("form:tablaAportesCorrecciones");
                    System.out.println("El borrado fue realizado con éxito");
                    FacesMessage msg = new FacesMessage("Información", "El borrado fue realizado con éxito. Recuerde que los cambios manuales deben ser borrados manualmente");
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                    RequestContext.getCurrentInstance().update("form:growl");
                }
            } else {
                System.out.println("No hay información para borrar");
                FacesMessage msg = new FacesMessage("Información", "No hay información para borrar");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                RequestContext.getCurrentInstance().update("form:growl");
            }

        } catch (Exception e) {
            System.out.println("Error borrarAporteEntidadProcesoAutomatico Controlador : " + e.toString());
            FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el proceso de borrado de Aportes Entidades.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }
    }

    public void procesoLiquidacionAporteEntidad() {
        RequestContext context = RequestContext.getCurrentInstance();
        try {
            getUsuario();
            if (usuario != null) {
                getParametroEstructura();
                getParametroInforme();
                if (parametroEstructura != null && parametroInforme != null) {
                    boolean fechasIgualesEstructura = true;
                    boolean fechasIgualesInforme = true;
                    short ano = 0;
                    short mes = 0;
                    if (tipoLista == 0) {
                        ano = parametroCorreccionSeleccionado.getAno();
                        mes = parametroCorreccionSeleccionado.getMes();
                    } else {
                        ano = parametroCorreccionSeleccionado.getAno();
                        mes = parametroCorreccionSeleccionado.getMes();
                    }

                    if ((parametroEstructura.getFechahastacausado().getMonth() + 1) != mes) {
                        fechasIgualesEstructura = false;
                    }
                    if ((parametroEstructura.getFechahastacausado().getYear() + 1900) != ano) {
                        fechasIgualesEstructura = false;
                    }
                    if ((parametroInforme.getFechahasta().getMonth() + 1) != mes) {
                        fechasIgualesInforme = false;
                    }
                    if ((parametroInforme.getFechahasta().getYear() + 1900) != ano) {
                        fechasIgualesInforme = false;
                    }
                    if (fechasIgualesEstructura == true && fechasIgualesInforme == true) {
                        RequestContext.getCurrentInstance().execute("PF('procesoLiquidacionOK').show()");
                    } else {
                        RequestContext.getCurrentInstance().execute("PF('errorProcesoLiquidacionFechas').show()");
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("Error procesoLiquidacionAporteEntidad Controlador : " + e.toString());
        }
    }

    public void procesoLiquidacionOK() {
        RequestContext context = RequestContext.getCurrentInstance();
        try {
            if (guardado == false) {
                System.out.println("entró a if 1");
                guardadoGeneral();
            } else {
                System.out.println("guardado : true");

                getParametroEstructura();
                getParametroInforme();
                String procesoInsertar = " ";
                String procesoActualizar = " ";
                String procesoIdentificar = " ";

                System.out.println("fechadesde : " + parametroEstructura.getFechadesdecausado());
                System.out.println("fecha hasta:  " + parametroEstructura.getFechahastacausado());
                System.out.println("tipo trabajador: " + parametroCorreccionSeleccionado.getTipotrabajador().getSecuencia());
                System.out.println("empresa : " + parametroCorreccionSeleccionado.getEmpresa().getNombre());

                if (tipoLista == 0) {
                    System.out.println("entró a if 2");
                    procesoInsertar = administrarParametroCorreccionAutoL.ejecutarPKGInsertarCorreccion(parametroEstructura.getFechadesdecausado(), parametroEstructura.getFechahastacausado(), parametroCorreccionSeleccionado.getTipotrabajador().getSecuencia(), parametroCorreccionSeleccionado.getEmpresa().getSecuencia());
                    System.out.println("procesoinsertar del if 2 : " + procesoInsertar);
                    procesoActualizar = administrarParametroCorreccionAutoL.ejecutarPKGActualizarNovedadesCorreccion(parametroCorreccionSeleccionado.getAno(), parametroCorreccionSeleccionado.getMes(), parametroCorreccionSeleccionado.getEmpresa().getSecuencia());
                    System.out.println("procesoActualizar del if 2 : " + procesoActualizar);
                    procesoIdentificar = administrarParametroCorreccionAutoL.ejecutarPKGIdentificaCorreccion(parametroCorreccionSeleccionado.getAno(), parametroCorreccionSeleccionado.getMes(), parametroCorreccionSeleccionado.getEmpresa().getSecuencia());
                    System.out.println("procesoIdentificarCorreccion del if 2 : " + procesoIdentificar);
                } else {
                    System.out.println("entró a else 1");
                    procesoInsertar = administrarParametroCorreccionAutoL.ejecutarPKGInsertarCorreccion(parametroEstructura.getFechadesdecausado(), parametroEstructura.getFechahastacausado(), parametroCorreccionSeleccionado.getTipotrabajador().getSecuencia(), parametroCorreccionSeleccionado.getEmpresa().getSecuencia());
                    procesoActualizar = administrarParametroCorreccionAutoL.ejecutarPKGActualizarNovedadesCorreccion(parametroCorreccionSeleccionado.getAno(), parametroCorreccionSeleccionado.getMes(), parametroCorreccionSeleccionado.getEmpresa().getSecuencia());
                    procesoIdentificar = administrarParametroCorreccionAutoL.ejecutarPKGIdentificaCorreccion(parametroCorreccionSeleccionado.getAno(), parametroCorreccionSeleccionado.getMes(), parametroCorreccionSeleccionado.getEmpresa().getSecuencia());
                }
                if ((procesoInsertar.equals("PROCESO_EXITOSO")) && (procesoActualizar.equals("PROCESO_EXITOSO")) && (procesoIdentificar.equals("PROCESO_EXITOSO"))) {
                    System.out.println("entró a if 3");
                    System.out.println("procesoinsertar del if 3 : " + procesoInsertar);
                    System.out.println("procesoActualizar del if 3 : " + procesoActualizar);
                    System.out.println("procesoIdentificar del if 3 : " + procesoIdentificar);
                    listaAportesCorrecciones = null;
                    getListaAportesCorrecciones();
                    contarRegistrosAporte();
                    disabledBuscar = true;
                    visibilidadMostrarTodos = "hidden";
                    RequestContext.getCurrentInstance().update("form:mostrarTodos");
                    RequestContext.getCurrentInstance().update("form:tablaAportesCorrecciones");
                    System.out.println("El proceso de Liquidación fue realizado con éxito");
                    System.out.println("lista aportes correcciones : " + listaAportesCorrecciones.size());
                    FacesMessage msg = new FacesMessage("Información", "El proceso de Liquidación fue realizado con éxito");
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                    RequestContext.getCurrentInstance().update("form:growl");
                } else if ((procesoInsertar.equals("ERROR_PERSISTENCIA")) || (procesoActualizar.equals("ERROR_PERSISTENCIA")) || (procesoIdentificar.equals("ERROR_PERSISTENCIA"))) {
                    System.out.println("entró a else if");
                    FacesMessage msg = new FacesMessage("Información", "Ocurrió un error en la ejecución del proceso de liquidación. Por favor, revisar los archivos de error de la carpeta SalidasUTL");
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                    RequestContext.getCurrentInstance().update("form:growl");
                }

            }

        } catch (Exception e) {
            System.out.println("Error procesoLiquidacionOK Controlador : " + e.toString());
            FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el proceso de Liquidación.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }
    }

    public void cambiarFechasParametros() {
        RequestContext context = RequestContext.getCurrentInstance();
        try {
            short ano = 0;
            short mes = 0;
            if (tipoLista == 0) {
                ano = parametroCorreccionSeleccionado.getAno();
                mes = parametroCorreccionSeleccionado.getMes();
            } else {
                ano = parametroCorreccionSeleccionado.getAno();
                mes = parametroCorreccionSeleccionado.getMes();
            }
            Date fechaDesdeParametros;

            Calendar calFin = Calendar.getInstance();

            calFin.set(Integer.parseInt(String.valueOf(ano)), Integer.parseInt(String.valueOf(mes - 1)), 1);

            fechaDesdeParametros = calFin.getTime();

            parametroEstructura.setFechadesdecausado(fechaDesdeParametros);
            parametroInforme.setFechadesde(fechaDesdeParametros);

            Date fechaHastaParametros;
            calFin.set(Integer.parseInt(String.valueOf(ano)), Integer.parseInt(String.valueOf(mes - 1)), calFin.getActualMaximum(Calendar.DAY_OF_MONTH));
            fechaHastaParametros = calFin.getTime();

            parametroEstructura.setFechahastacausado(fechaHastaParametros);
            parametroInforme.setFechahasta(fechaHastaParametros);

            administrarParametroCorreccionAutoL.modificarParametroEstructura(parametroEstructura);
            administrarParametroCorreccionAutoL.modificarParametroInforme(parametroInforme);

            parametroInforme = null;  
            parametroEstructura = null;
            RequestContext.getCurrentInstance().update("form:datosParametroAuto");
            FacesMessage msg = new FacesMessage("Información", "Se realizó con éxito el cambio de fechas ");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        } catch (Exception e) {
            System.out.println("Error cambiarFechasParametros Controlador : " + e.toString());
            FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en la modificacio de fechas ");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }
    }

    public void procesoIncrementarCorreccion() {
        RequestContext context = RequestContext.getCurrentInstance();
        try {
            getUsuario();
            parametroCorreccionSeleccionado = getParametroCorreccionSeleccionado();
            System.out.println("usuario :" + usuario.getAlias());
            if (usuario.getAlias() != null) {
                getParametroEstructura();
                getParametroInforme();
                System.out.println("parametro estructuras : " + parametroEstructura);
                System.out.println("parametro informe : " + parametroInforme);
                System.out.println("parametro seleccionado : " + parametroCorreccionSeleccionado);
                System.out.println("parada 1");
                if (parametroEstructura != null) {
                    if (parametroInforme != null) {
                        boolean fechasIgualesEstructura = true;
                        boolean fechasIgualesInforme = true;

                        System.out.println("parada 2");
                        if (fechasIgualesEstructura == true && fechasIgualesInforme == true) {
                            System.out.println("entra a acumular dif ok");
                            RequestContext.getCurrentInstance().execute("PF('acumularDiferenciaOK').show()");
                        } else {
                            RequestContext.getCurrentInstance().execute("PF('errorAcumularDiferencia').show()");
                        }
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("se estalló");
            System.out.println("Error procesoLiquidacionAporteEntidad Controlador : " + e.toString());
        }
    }

    public void incrementarCorreccionOK() {
        RequestContext context = RequestContext.getCurrentInstance();
        String resultado;
        try {
            parametroCorreccionSeleccionado = getParametroCorreccionSeleccionado();
            System.out.println("Año " + parametroCorreccionSeleccionado.getAno());
            System.out.println("Mes " + parametroCorreccionSeleccionado.getMes());
            System.out.println("Empresa " + parametroCorreccionSeleccionado.getEmpresa().getNombre());

            resultado = administrarParametroCorreccionAutoL.ejecutarPKGActualizarNovedadesCorreccion(parametroCorreccionSeleccionado.getAno(), parametroCorreccionSeleccionado.getMes(), parametroCorreccionSeleccionado.getEmpresa().getSecuencia());
            System.out.println("resultado consulta : " + resultado);
            disabledBuscar = true;
            visibilidadMostrarTodos = "hidden";
            System.out.println("entró a actualizar");
            RequestContext.getCurrentInstance().update("form:mostrarTodos");
            System.out.println("El proceso de incrementar Correción fue realizado con éxito");
            FacesMessage msg = new FacesMessage("Información", "El proceso de incrementar Correción fue realizado con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
            guardadoGeneral();
            RequestContext.getCurrentInstance().update("form:tablaAportesCorrecciones");

        } catch (Exception e) {
            System.out.println("Error incrementarCorrecionOK Controlador : " + e.toString());
            FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el proceso de incrementar Correción.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }
    }

    public void activarCtrlF11() {

        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            altoTabla = "20";
            parametroAno = (Column) c.getViewRoot().findComponent("form:datosParametroAuto:parametroAno");
            parametroAno.setFilterStyle("width: 85% !important");
            parametroMes = (Column) c.getViewRoot().findComponent("form:datosParametroAuto:parametroMes");
            parametroMes.setFilterStyle("width: 85% !important");
            parametroTipoTrabajador = (Column) c.getViewRoot().findComponent("form:datosParametroAuto:parametroTipoTrabajador");
            parametroTipoTrabajador.setFilterStyle("width: 85% !important");
            parametroEmpresa = (Column) c.getViewRoot().findComponent("form:datosParametroAuto:parametroEmpresa");
            parametroEmpresa.setFilterStyle("width: 85% !important");
            tipoPlanilla = (Column) c.getViewRoot().findComponent("form:datosParametroAuto:parametroTipoPlanilla");
            tipoPlanilla.setFilterStyle("width: 85% !important");
            planilla = (Column) c.getViewRoot().findComponent("form:datosParametroAuto:parametroPlanilla");
            planilla.setFilterStyle("width: 85% !important");
            fechaplan = (Column) c.getViewRoot().findComponent("form:datosParametroAuto:aporteFechaPlanilla");
            fechaplan.setFilterStyle("width: 85% !important");

            RequestContext.getCurrentInstance().update("form:datosParametroAuto");
            activarFiltradoAporteEntidad();
            bandera = 1;
            tipoLista = 1;
        } else if (bandera == 1) {
            altoTabla = "40";
            parametroAno = (Column) c.getViewRoot().findComponent("form:datosParametroAuto:parametroAno");
            parametroAno.setFilterStyle("display: none; visibility: hidden;");
            parametroMes = (Column) c.getViewRoot().findComponent("form:datosParametroAuto:parametroMes");
            parametroMes.setFilterStyle("display: none; visibility: hidden;");
            parametroTipoTrabajador = (Column) c.getViewRoot().findComponent("form:datosParametroAuto:parametroTipoTrabajador");
            parametroTipoTrabajador.setFilterStyle("display: none; visibility: hidden;");
            parametroEmpresa = (Column) c.getViewRoot().findComponent("form:datosParametroAuto:parametroEmpresa");
            parametroEmpresa.setFilterStyle("display: none; visibility: hidden;");
            tipoPlanilla = (Column) c.getViewRoot().findComponent("form:datosParametroAuto:parametroTipoPlanilla");
            tipoPlanilla.setFilterStyle("display: none; visibility: hidden;");
            planilla = (Column) c.getViewRoot().findComponent("form:datosParametroAuto:parametroPlanilla");
            planilla.setFilterStyle("display: none; visibility: hidden;");
            fechaplan = (Column) c.getViewRoot().findComponent("form:datosParametroAuto:aporteFechaPlanilla");
            fechaplan.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosParametroAuto");
            bandera = 0;
            filtrarListaParametrosCorrecciones = null;
            tipoLista = 0;
            desactivarFiltradoAporteEntidad();
        }
    }

    public void desactivarFiltradoAporteEntidad() {
        FacesContext c = FacesContext.getCurrentInstance();
        altoTablaAporte = "160";

        aporteAno = (Column) c.getViewRoot().findComponent("form:tablaAportesCorrecciones:aporteAno");
        aporteAno.setFilterStyle("display: none; visibility: hidden;");

        aporteMes = (Column) c.getViewRoot().findComponent("form:tablaAportesCorrecciones:aporteMes");
        aporteMes.setFilterStyle("display: none; visibility: hidden;");

        aporteNombreEmpleado = (Column) c.getViewRoot().findComponent("form:tablaAportesCorrecciones:aporteNombreEmpleado");
        aporteNombreEmpleado.setFilterStyle("display: none; visibility: hidden;");

        aportetipoPlanilla = (Column) c.getViewRoot().findComponent("form:tablaAportesCorrecciones:aporteTipoPlanilla");
        aportetipoPlanilla.setFilterStyle("display: none; visibility: hidden;");

        aporteNIT = (Column) c.getViewRoot().findComponent("form:tablaAportesCorrecciones:aporteNIT");
        aporteNIT.setFilterStyle("display: none; visibility: hidden;");

        aporteTercero = (Column) c.getViewRoot().findComponent("form:tablaAportesCorrecciones:aporteTercero");
        aporteTercero.setFilterStyle("display: none; visibility: hidden;");

        aporteTipoEntidad = (Column) c.getViewRoot().findComponent("form:tablaAportesCorrecciones:aporteTipoEntidad");
        aporteTipoEntidad.setFilterStyle("display: none; visibility: hidden;");

        aporteEmpleado = (Column) c.getViewRoot().findComponent("form:tablaAportesCorrecciones:aporteEmpleado");
        aporteEmpleado.setFilterStyle("display: none; visibility: hidden;");

        aporteEmpleador = (Column) c.getViewRoot().findComponent("form:tablaAportesCorrecciones:aporteEmpleador");
        aporteEmpleador.setFilterStyle("display: none; visibility: hidden;");

        aporteAjustePatronal = (Column) c.getViewRoot().findComponent("form:tablaAportesCorrecciones:aporteAjustePatronal");
        aporteAjustePatronal.setFilterStyle("display: none; visibility: hidden;");

        aporteSolidaridadl = (Column) c.getViewRoot().findComponent("form:tablaAportesCorrecciones:aporteSolidaridadl");
        aporteSolidaridadl.setFilterStyle("display: none; visibility: hidden;");

        aporteSubSistencia = (Column) c.getViewRoot().findComponent("form:tablaAportesCorrecciones:aporteSubSistencia");
        aporteSubSistencia.setFilterStyle("display: none; visibility: hidden;");

        aporteSubsPensionados = (Column) c.getViewRoot().findComponent("form:tablaAportesCorrecciones:aporteSubsPensionados");
        aporteSubsPensionados.setFilterStyle("display: none; visibility: hidden;");

        aporteSalarioBasico = (Column) c.getViewRoot().findComponent("form:tablaAportesCorrecciones:aporteSalarioBasico");
        aporteSalarioBasico.setFilterStyle("display: none; visibility: hidden;");

        aporteIBC = (Column) c.getViewRoot().findComponent("form:tablaAportesCorrecciones:aporteIBC");
        aporteIBC.setFilterStyle("display: none; visibility: hidden;");

        aporteIBCReferencia = (Column) c.getViewRoot().findComponent("form:tablaAportesCorrecciones:aporteIBCReferencia");
        aporteIBCReferencia.setFilterStyle("display: none; visibility: hidden;");

        aporteDiasCotizados = (Column) c.getViewRoot().findComponent("form:tablaAportesCorrecciones:aporteDiasCotizados");
        aporteDiasCotizados.setFilterStyle("display: none; visibility: hidden;");

        aporteTipoAportante = (Column) c.getViewRoot().findComponent("form:tablaAportesCorrecciones:aporteTipoAportante");
        aporteTipoAportante.setFilterStyle("display: none; visibility: hidden;");

        aporteING = (Column) c.getViewRoot().findComponent("form:tablaAportesCorrecciones:aporteING");
        aporteING.setFilterStyle("display: none; visibility: hidden;");

        aporteRET = (Column) c.getViewRoot().findComponent("form:tablaAportesCorrecciones:aporteRET");
        aporteRET.setFilterStyle("display: none; visibility: hidden;");

        aporteTDA = (Column) c.getViewRoot().findComponent("form:tablaAportesCorrecciones:aporteTDA");
        aporteTDA.setFilterStyle("display: none; visibility: hidden;");

        aporteTAA = (Column) c.getViewRoot().findComponent("form:tablaAportesCorrecciones:aporteTAA");
        aporteTAA.setFilterStyle("display: none; visibility: hidden;");

        aporteVSP = (Column) c.getViewRoot().findComponent("form:tablaAportesCorrecciones:aporteVSP");
        aporteVSP.setFilterStyle("display: none; visibility: hidden;");

        aporteVTE = (Column) c.getViewRoot().findComponent("form:tablaAportesCorrecciones:aporteVTE");
        aporteVTE.setFilterStyle("display: none; visibility: hidden;");

        aporteVST = (Column) c.getViewRoot().findComponent("form:tablaAportesCorrecciones:aporteVST");
        aporteVST.setFilterStyle("display: none; visibility: hidden;");

        aporteSLN = (Column) c.getViewRoot().findComponent("form:tablaAportesCorrecciones:aporteSLN");
        aporteSLN.setFilterStyle("display: none; visibility: hidden;");

        aporteIGE = (Column) c.getViewRoot().findComponent("form:tablaAportesCorrecciones:aporteIGE");
        aporteIGE.setFilterStyle("display: none; visibility: hidden;");

        aporteLMA = (Column) c.getViewRoot().findComponent("form:tablaAportesCorrecciones:aporteLMA");
        aporteLMA.setFilterStyle("display: none; visibility: hidden;");

        aporteVAC = (Column) c.getViewRoot().findComponent("form:tablaAportesCorrecciones:aporteVAC");
        aporteVAC.setFilterStyle("display: none; visibility: hidden;");

        aporteAVP = (Column) c.getViewRoot().findComponent("form:tablaAportesCorrecciones:aporteAVP");
        aporteAVP.setFilterStyle("display: none; visibility: hidden;");

        aporteVCT = (Column) c.getViewRoot().findComponent("form:tablaAportesCorrecciones:aporteVCT");
        aporteVCT.setFilterStyle("display: none; visibility: hidden;");

        aporteIRP = (Column) c.getViewRoot().findComponent("form:tablaAportesCorrecciones:aporteIRP");
        aporteIRP.setFilterStyle("display: none; visibility: hidden;");

        aporteSUS = (Column) c.getViewRoot().findComponent("form:tablaAportesCorrecciones:aporteSUS");
        aporteSUS.setFilterStyle("display: none; visibility: hidden;");

        aporteINTE = (Column) c.getViewRoot().findComponent("form:tablaAportesCorrecciones:aporteINTE");
        aporteINTE.setFilterStyle("display: none; visibility: hidden;");

        aporteTarifaEPS = (Column) c.getViewRoot().findComponent("form:tablaAportesCorrecciones:aporteTarifaEPS");
        aporteTarifaEPS.setFilterStyle("display: none; visibility: hidden;");

        aporteTarifaAAFP = (Column) c.getViewRoot().findComponent("form:tablaAportesCorrecciones:aporteTarifaAAFP");
        aporteTarifaAAFP.setFilterStyle("display: none; visibility: hidden;");

        aporteTarifaACTT = (Column) c.getViewRoot().findComponent("form:tablaAportesCorrecciones:aporteTarifaACTT");
        aporteTarifaACTT.setFilterStyle("display: none; visibility: hidden;");

        aporteCodigoCTT = (Column) c.getViewRoot().findComponent("form:tablaAportesCorrecciones:aporteCodigoCTT");
        aporteCodigoCTT.setFilterStyle("display: none; visibility: hidden;");

        aporteAVPEValor = (Column) c.getViewRoot().findComponent("form:tablaAportesCorrecciones:aporteAVPEValor");
        aporteAVPEValor.setFilterStyle("display: none; visibility: hidden;");

        aporteAVPPValor = (Column) c.getViewRoot().findComponent("form:tablaAportesCorrecciones:aporteAVPPValor");
        aporteAVPPValor.setFilterStyle("display: none; visibility: hidden;");

        aporteRETCONTAValor = (Column) c.getViewRoot().findComponent("form:tablaAportesCorrecciones:aporteRETCONTAValor");
        aporteRETCONTAValor.setFilterStyle("display: none; visibility: hidden;");

        aporteCodigoNEPS = (Column) c.getViewRoot().findComponent("form:tablaAportesCorrecciones:aporteCodigoNEPS");
        aporteCodigoNEPS.setFilterStyle("display: none; visibility: hidden;");

        aporteCodigoNAFP = (Column) c.getViewRoot().findComponent("form:tablaAportesCorrecciones:aporteCodigoNAFP");
        aporteCodigoNAFP.setFilterStyle("display: none; visibility: hidden;");

        aporteEGValor = (Column) c.getViewRoot().findComponent("form:tablaAportesCorrecciones:aporteEGValor");
        aporteEGValor.setFilterStyle("display: none; visibility: hidden;");

        aporteEGAutorizacion = (Column) c.getViewRoot().findComponent("form:tablaAportesCorrecciones:aporteEGAutorizacion");
        aporteEGAutorizacion.setFilterStyle("display: none; visibility: hidden;");

        aporteMaternidadValor = (Column) c.getViewRoot().findComponent("form:tablaAportesCorrecciones:aporteMaternidadValor");
        aporteMaternidadValor.setFilterStyle("display: none; visibility: hidden;");

        aporteMaternidadAuto = (Column) c.getViewRoot().findComponent("form:tablaAportesCorrecciones:aporteMaternidadAuto");
        aporteMaternidadAuto.setFilterStyle("display: none; visibility: hidden;");

        aporteUPCValor = (Column) c.getViewRoot().findComponent("form:tablaAportesCorrecciones:aporteUPCValor");
        aporteUPCValor.setFilterStyle("display: none; visibility: hidden;");

        aporteTipo = (Column) c.getViewRoot().findComponent("form:tablaAportesCorrecciones:aporteTipo");
        aporteTipo.setFilterStyle("display: none; visibility: hidden;");

        aporteTipoPensionado = (Column) c.getViewRoot().findComponent("form:tablaAportesCorrecciones:aporteTipoPensionado");
        aporteTipoPensionado.setFilterStyle("display: none; visibility: hidden;");

        aportePensionCompartida = (Column) c.getViewRoot().findComponent("form:tablaAportesCorrecciones:aportePensionCompartida");
        aportePensionCompartida.setFilterStyle("display: none; visibility: hidden;");

        aporteFechaIngreso = (Column) c.getViewRoot().findComponent("form:tablaAportesCorrecciones:aporteFechaIngreso");
        aporteFechaIngreso.setFilterStyle("display: none; visibility: hidden;");

        RequestContext.getCurrentInstance().update("form:tablaAportesCorrecciones");
    }

    public void activarFiltradoAporteEntidad() {
        FacesContext c = FacesContext.getCurrentInstance();
        altoTablaAporte = "138";

        aporteAno = (Column) c.getViewRoot().findComponent("form:tablaAportesCorrecciones:aporteAno");
        aporteAno.setFilterStyle("width: 85% !important");

        aporteMes = (Column) c.getViewRoot().findComponent("form:tablaAportesCorrecciones:aporteMes");
        aporteMes.setFilterStyle("width: 85% !important");

        aporteNombreEmpleado = (Column) c.getViewRoot().findComponent("form:tablaAportesCorrecciones:aporteNombreEmpleado");
        aporteNombreEmpleado.setFilterStyle("width: 85% !important");

        aportetipoPlanilla = (Column) c.getViewRoot().findComponent("form:tablaAportesCorrecciones:aporteTipoPlanilla");
        aportetipoPlanilla.setFilterStyle("width: 85% !important");

        aporteNIT = (Column) c.getViewRoot().findComponent("form:tablaAportesCorrecciones:aporteNIT");
        aporteNIT.setFilterStyle("width: 85% !important");

        aporteTercero = (Column) c.getViewRoot().findComponent("form:tablaAportesCorrecciones:aporteTercero");
        aporteTercero.setFilterStyle("width: 85% !important");

        aporteTipoEntidad = (Column) c.getViewRoot().findComponent("form:tablaAportesCorrecciones:aporteTipoEntidad");
        aporteTipoEntidad.setFilterStyle("width: 85% !important");

        aporteEmpleado = (Column) c.getViewRoot().findComponent("form:tablaAportesCorrecciones:aporteEmpleado");
        aporteEmpleado.setFilterStyle("width: 85% !important");

        aporteEmpleador = (Column) c.getViewRoot().findComponent("form:tablaAportesCorrecciones:aporteEmpleador");
        aporteEmpleador.setFilterStyle("width: 85% !important");

        aporteAjustePatronal = (Column) c.getViewRoot().findComponent("form:tablaAportesCorrecciones:aporteAjustePatronal");
        aporteAjustePatronal.setFilterStyle("width: 85% !important");

        aporteSolidaridadl = (Column) c.getViewRoot().findComponent("form:tablaAportesCorrecciones:aporteSolidaridadl");
        aporteSolidaridadl.setFilterStyle("width: 85% !important");

        aporteSubSistencia = (Column) c.getViewRoot().findComponent("form:tablaAportesCorrecciones:aporteSubSistencia");
        aporteSubSistencia.setFilterStyle("width: 85% !important");

        aporteSubsPensionados = (Column) c.getViewRoot().findComponent("form:tablaAportesCorrecciones:aporteSubsPensionados");
        aporteSubsPensionados.setFilterStyle("width: 85% !important");

        aporteSalarioBasico = (Column) c.getViewRoot().findComponent("form:tablaAportesCorrecciones:aporteSalarioBasico");
        aporteSalarioBasico.setFilterStyle("width: 85% !important");

        aporteIBC = (Column) c.getViewRoot().findComponent("form:tablaAportesCorrecciones:aporteIBC");
        aporteIBC.setFilterStyle("width: 85% !important");

        aporteIBCReferencia = (Column) c.getViewRoot().findComponent("form:tablaAportesCorrecciones:aporteIBCReferencia");
        aporteIBCReferencia.setFilterStyle("width: 85% !important");

        aporteDiasCotizados = (Column) c.getViewRoot().findComponent("form:tablaAportesCorrecciones:aporteDiasCotizados");
        aporteDiasCotizados.setFilterStyle("width: 85% !important");

        aporteTipoAportante = (Column) c.getViewRoot().findComponent("form:tablaAportesCorrecciones:aporteTipoAportante");
        aporteTipoAportante.setFilterStyle("width: 85% !important");

        aporteING = (Column) c.getViewRoot().findComponent("form:tablaAportesCorrecciones:aporteING");
        aporteING.setFilterStyle("width: 85% !important");

        aporteRET = (Column) c.getViewRoot().findComponent("form:tablaAportesCorrecciones:aporteRET");
        aporteRET.setFilterStyle("width: 85% !important");

        aporteTDA = (Column) c.getViewRoot().findComponent("form:tablaAportesCorrecciones:aporteTDA");
        aporteTDA.setFilterStyle("width: 85% !important");

        aporteTAA = (Column) c.getViewRoot().findComponent("form:tablaAportesCorrecciones:aporteTAA");
        aporteTAA.setFilterStyle("width: 85% !important");

        aporteVSP = (Column) c.getViewRoot().findComponent("form:tablaAportesCorrecciones:aporteVSP");
        aporteVSP.setFilterStyle("width: 85% !important");

        aporteVTE = (Column) c.getViewRoot().findComponent("form:tablaAportesCorrecciones:aporteVTE");
        aporteVTE.setFilterStyle("width: 85% !important");

        aporteVST = (Column) c.getViewRoot().findComponent("form:tablaAportesCorrecciones:aporteVST");
        aporteVST.setFilterStyle("width: 85% !important");

        aporteSLN = (Column) c.getViewRoot().findComponent("form:tablaAportesCorrecciones:aporteSLN");
        aporteSLN.setFilterStyle("width: 85% !important");

        aporteIGE = (Column) c.getViewRoot().findComponent("form:tablaAportesCorrecciones:aporteIGE");
        aporteIGE.setFilterStyle("width: 85% !important");

        aporteLMA = (Column) c.getViewRoot().findComponent("form:tablaAportesCorrecciones:aporteLMA");
        aporteLMA.setFilterStyle("width: 85% !important");

        aporteVAC = (Column) c.getViewRoot().findComponent("form:tablaAportesCorrecciones:aporteVAC");
        aporteVAC.setFilterStyle("width: 85% !important");

        aporteAVP = (Column) c.getViewRoot().findComponent("form:tablaAportesCorrecciones:aporteAVP");
        aporteAVP.setFilterStyle("width: 85% !important");

        aporteVCT = (Column) c.getViewRoot().findComponent("form:tablaAportesCorrecciones:aporteVCT");
        aporteVCT.setFilterStyle("width: 85% !important");

        aporteIRP = (Column) c.getViewRoot().findComponent("form:tablaAportesCorrecciones:aporteIRP");
        aporteIRP.setFilterStyle("width: 85% !important");

        aporteSUS = (Column) c.getViewRoot().findComponent("form:tablaAportesCorrecciones:aporteSUS");
        aporteSUS.setFilterStyle("width: 85% !important");

        aporteINTE = (Column) c.getViewRoot().findComponent("form:tablaAportesCorrecciones:aporteINTE");
        aporteINTE.setFilterStyle("width: 85% !important");

        aporteTarifaEPS = (Column) c.getViewRoot().findComponent("form:tablaAportesCorrecciones:aporteTarifaEPS");
        aporteTarifaEPS.setFilterStyle("width: 85% !important");

        aporteTarifaAAFP = (Column) c.getViewRoot().findComponent("form:tablaAportesCorrecciones:aporteTarifaAAFP");
        aporteTarifaAAFP.setFilterStyle("width: 85% !important");

        aporteTarifaACTT = (Column) c.getViewRoot().findComponent("form:tablaAportesCorrecciones:aporteTarifaACTT");
        aporteTarifaACTT.setFilterStyle("width: 85% !important");

        aporteCodigoCTT = (Column) c.getViewRoot().findComponent("form:tablaAportesCorrecciones:aporteCodigoCTT");
        aporteCodigoCTT.setFilterStyle("width: 85% !important");

        aporteAVPEValor = (Column) c.getViewRoot().findComponent("form:tablaAportesCorrecciones:aporteAVPEValor");
        aporteAVPEValor.setFilterStyle("width: 85% !important");

        aporteAVPPValor = (Column) c.getViewRoot().findComponent("form:tablaAportesCorrecciones:aporteAVPPValor");
        aporteAVPPValor.setFilterStyle("width: 85% !important");

        aporteRETCONTAValor = (Column) c.getViewRoot().findComponent("form:tablaAportesCorrecciones:aporteRETCONTAValor");
        aporteRETCONTAValor.setFilterStyle("width: 85% !important");

        aporteCodigoNEPS = (Column) c.getViewRoot().findComponent("form:tablaAportesCorrecciones:aporteCodigoNEPS");
        aporteCodigoNEPS.setFilterStyle("width: 85% !important");

        aporteCodigoNAFP = (Column) c.getViewRoot().findComponent("form:tablaAportesCorrecciones:aporteCodigoNAFP");
        aporteCodigoNAFP.setFilterStyle("width: 85% !important");

        aporteEGValor = (Column) c.getViewRoot().findComponent("form:tablaAportesCorrecciones:aporteEGValor");
        aporteEGValor.setFilterStyle("width: 85% !important");

        aporteEGAutorizacion = (Column) c.getViewRoot().findComponent("form:tablaAportesCorrecciones:aporteEGAutorizacion");
        aporteEGAutorizacion.setFilterStyle("width: 85% !important");

        aporteMaternidadValor = (Column) c.getViewRoot().findComponent("form:tablaAportesCorrecciones:aporteMaternidadValor");
        aporteMaternidadValor.setFilterStyle("width: 85% !important");

        aporteMaternidadAuto = (Column) c.getViewRoot().findComponent("form:tablaAportesCorrecciones:aporteMaternidadAuto");
        aporteMaternidadAuto.setFilterStyle("width: 85% !important");

        aporteUPCValor = (Column) c.getViewRoot().findComponent("form:tablaAportesCorrecciones:aporteUPCValor");
        aporteUPCValor.setFilterStyle("width: 85% !important");

        aporteTipo = (Column) c.getViewRoot().findComponent("form:tablaAportesCorrecciones:aporteTipo");
        aporteTipo.setFilterStyle("width: 85% !important");

        aporteTipoPensionado = (Column) c.getViewRoot().findComponent("form:tablaAportesCorrecciones:aporteTipoPensionado");
        aporteTipoPensionado.setFilterStyle("width: 85% !important");

        aportePensionCompartida = (Column) c.getViewRoot().findComponent("form:tablaAportesCorrecciones:aportePensionCompartida");
        aportePensionCompartida.setFilterStyle("width: 85% !important");

        aporteFechaIngreso = (Column) c.getViewRoot().findComponent("form:tablaAportesCorrecciones:aporteFechaIngreso");
        aporteFechaIngreso.setFilterStyle("width: 85% !important");

        RequestContext.getCurrentInstance().update("form:tablaAportesCorrecciones");

    }

    public void salir() {
        if (bandera == 1) {
            altoTabla = "40";
            FacesContext c = FacesContext.getCurrentInstance();
            parametroAno = (Column) c.getViewRoot().findComponent("form:datosParametroAuto:parametroAno");
            parametroAno.setFilterStyle("display: none; visibility: hidden;");
            parametroMes = (Column) c.getViewRoot().findComponent("form:datosParametroAuto:parametroMes");
            parametroMes.setFilterStyle("display: none; visibility: hidden;");
            parametroTipoTrabajador = (Column) c.getViewRoot().findComponent("form:datosParametroAuto:parametroTipoTrabajador");
            parametroTipoTrabajador.setFilterStyle("display: none; visibility: hidden;");
            parametroEmpresa = (Column) c.getViewRoot().findComponent("form:datosParametroAuto:parametroEmpresa");
            parametroEmpresa.setFilterStyle("display: none; visibility: hidden;");
            tipoPlanilla = (Column) c.getViewRoot().findComponent("form:datosParametrosAuto:parametroTipoPlanilla");
            tipoPlanilla.setFilterStyle("display: none; visibility: hidden;");
            planilla = (Column) c.getViewRoot().findComponent("form:datosParametrosAuto:parametroPlanilla");
            planilla.setFilterStyle("display: none; visibility: hidden;");
            fechaplan = (Column) c.getViewRoot().findComponent("form:datosParametrosAuto:aporteFechaPlanilla");
            fechaplan.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosParametroAuto");
            bandera = 0;
            filtrarListaParametrosCorrecciones = null;
            tipoLista = 0;
        }
        if (banderaAporte == 1) {
            desactivarFiltradoAporteEntidad();
            banderaAporte = 0;
            filtrarListaAportesCorrecciones = null;
            tipoListaAporte = 0;
        }

        listParametrosCorreccionesBorrar.clear();
        listParametrosCorreccionesCrear.clear();
        listParametrosCorreccionesModificar.clear();
        listAportesCorreccionesBorrar.clear();
        listAportesCorreccionesModificar.clear();
        parametroCorreccionSeleccionado = null;
        aporteTablaSeleccionado = null;
        k = 0;
        listaParametrosCorrecciones = null;
        listaAportesCorrecciones = null;
        guardado = true;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void asignarIndex(ParametrosCorreccionesAutoL parametro, int LND, int dialogo) {
        parametroCorreccionSeleccionado = parametro;
        RequestContext context = RequestContext.getCurrentInstance();
        tipoActualizacion = LND;
        System.out.println("tipo actualizacion aignar index : " + tipoActualizacion);
        if (dialogo == 1) {
            modificarInfoRegistroTercero(lovTerceros.size());
            RequestContext.getCurrentInstance().update("formularioLovTipoTrabajador:TipoTrabajadorDialogo");
            RequestContext.getCurrentInstance().execute("PF('TipoTrabajadorDialogo').show()");
        }
        if (dialogo == 2) {
            modificarInfoRegistroEmpresa(lovEmpresas.size());
            RequestContext.getCurrentInstance().update("formularioLovEmpresa:EmpresaDialogo");
            RequestContext.getCurrentInstance().execute("PF('EmpresaDialogo').show()");
            modificarInfoRegistroEmpresa(lovEmpresas.size());
        }
    }

    public void asignarIndexAporte(AportesCorrecciones aporte, int LND, int dialogo) {
        aporteTablaSeleccionado = aporte;
        RequestContext context = RequestContext.getCurrentInstance();
        tipoActualizacion = LND;
        System.out.println("tipo actualizacion aignar index aporte : " + tipoActualizacion);
        if (dialogo == 1) {
            modificarInfoRegistroEmpleados(lovEmpleados.size());
            RequestContext.getCurrentInstance().update("formularioLovEmpleado:EmpleadoDialogo");
            RequestContext.getCurrentInstance().execute("PF('EmpleadoDialogo').show()");
        }
        if (dialogo == 2) {
            modificarInfoRegistroTercero(lovTerceros.size());
            RequestContext.getCurrentInstance().update("formularioLovTercero:TerceroDialogo");
            RequestContext.getCurrentInstance().execute("PF('TerceroDialogo').show()");
        }
        if (dialogo == 3) {
            modificarInfoRegistroTiposEntidades(lovTiposEntidades.size());
            RequestContext.getCurrentInstance().update("formularioLovTipoEntidad:TipoEntidadDialogo");
            RequestContext.getCurrentInstance().execute("PF('TipoEntidadDialogo').show()");
        }
    }

    public void actualizarTipoTrabajador() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (tipoLista == 0) {
                parametroCorreccionSeleccionado.setTipotrabajador(tipoTrabajadorSeleccionado);
                if (!listParametrosCorreccionesCrear.contains(parametroCorreccionSeleccionado)) {
                    if (listParametrosCorreccionesModificar.isEmpty()) {
                        listParametrosCorreccionesModificar.add(parametroCorreccionSeleccionado);
                    } else if (!listParametrosCorreccionesModificar.contains(parametroCorreccionSeleccionado)) {
                        listParametrosCorreccionesModificar.add(parametroCorreccionSeleccionado);
                    }
                }
            } else {
                parametroCorreccionSeleccionado.setTipotrabajador(tipoTrabajadorSeleccionado);
                if (!listParametrosCorreccionesCrear.contains(parametroCorreccionSeleccionado)) {
                    if (listParametrosCorreccionesModificar.isEmpty()) {
                        listParametrosCorreccionesModificar.add(parametroCorreccionSeleccionado);
                    } else if (!listParametrosCorreccionesModificar.contains(parametroCorreccionSeleccionado)) {
                        listParametrosCorreccionesModificar.add(parametroCorreccionSeleccionado);
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            permitirIndex = true;
            RequestContext.getCurrentInstance().update("form:datosParametroAuto");
        } else if (tipoActualizacion == 1) {
            nuevoParametro.setTipotrabajador(tipoTrabajadorSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaTipoTrabajadorParametro");
        } else if (tipoActualizacion == 2) {
            duplicarParametro.setTipotrabajador(tipoTrabajadorSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTipoTrabajadorParametro");
        }
        filtrarLovTiposTrabajadores = null;
        tipoTrabajadorSeleccionado = new TiposTrabajadores();
        aceptar = true;
        parametroCorreccionSeleccionado = null;
        tipoActualizacion = -1;

        context.reset("formularioLovTipoTrabajador:lovTipoTrabajador:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovTipoTrabajador').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('TipoTrabajadorDialogo').hide()");
    }

    public void cancelarCambioTipoTrabajador() {
        filtrarLovTiposTrabajadores = null;
        tipoTrabajadorSeleccionado = new TiposTrabajadores();
        aceptar = true;
        RequestContext context = RequestContext.getCurrentInstance();
        tipoActualizacion = -1;
        permitirIndex = true;
        context.reset("formularioLovTipoTrabajador:lovTipoTrabajador:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovTipoTrabajador').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('TipoTrabajadorDialogo').hide()");
    }

    public void actualizarEmpresa() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 1) {
            nuevoParametro.setEmpresa(empresaSeleccionada);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaEmpresaParametro");
        } else if (tipoActualizacion == 2) {
            duplicarParametro.setEmpresa(empresaSeleccionada);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarEmpresaParametro");
        }
        filtrarLovEmpresas = null;
        empresaSeleccionada = new Empresas();
        aceptar = true;
        tipoActualizacion = -1;
        context.reset("formularioLovEmpresa:lovEmpresa:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovEmpresa').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('EmpresaDialogo').hide()");
    }

    public void cancelarCambioEmpresa() {
        filtrarLovEmpresas = null;
        empresaSeleccionada = new Empresas();
        aceptar = true;
        RequestContext context = RequestContext.getCurrentInstance();
        tipoActualizacion = -1;
        permitirIndex = true;
        context.reset("formularioLovEmpresa:lovEmpresa:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovEmpresa').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('EmpresaDialogo').hide()");
    }

    public void actualizarEmpleado() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (tipoListaAporte == 0) {
                aporteTablaSeleccionado.setEmpleado(empleadoSeleccionado);
                if (listAportesCorreccionesModificar.isEmpty()) {
                    listAportesCorreccionesModificar.add(aporteTablaSeleccionado);
                } else if (!listAportesCorreccionesModificar.contains(aporteTablaSeleccionado)) {
                    listAportesCorreccionesModificar.add(aporteTablaSeleccionado);
                }
            } else {
                aporteTablaSeleccionado.setEmpleado(empleadoSeleccionado);
                if (listAportesCorreccionesModificar.isEmpty()) {
                    listAportesCorreccionesModificar.add(aporteTablaSeleccionado);
                } else if (!listAportesCorreccionesModificar.contains(aporteTablaSeleccionado)) {
                    listAportesCorreccionesModificar.add(aporteTablaSeleccionado);
                }
            }
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            permitirIndexAporte = true;
            RequestContext.getCurrentInstance().update("form:tablaAportesCorrecciones");
        } else if (tipoActualizacion == 1) {
            nuevoAporteEntidad.setEmpleado(empleadoSeleccionado);
            System.out.println("empleado seleccionado :" + empleadoSeleccionado.getSecuencia());
            System.out.println("nuevoaporteempleado empleado seleccionado : " + nuevoAporteEntidad.getEmpleado().getSecuencia());
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoCodempl");
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoNomEmpl");
        } else if (tipoActualizacion == 2) {
            duplicarAporteEntidad.setEmpleado(empleadoSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCodempl");
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarNomEmpl");
        }

        filtrarLovEmpleados = null;
        aceptar = true;
        tipoActualizacion = -1;

        context.reset("formularioLovEmpleado:lovEmpleado:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovEmpleado').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('EmpleadoDialogo').hide()");
    }

    public void cancelarCambioEmpleado() {
        filtrarLovEmpleados = null;
        empleadoSeleccionado = new Empleados();
        aceptar = true;
        tipoActualizacion = -1;
        permitirIndexAporte = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioLovEmpleado:lovEmpleado:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovEmpleado').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('EmpleadoDialogo').hide()");
    }

    public void actualizarTercero() {
        System.out.println("tipo actualización actualizar tercero : " + tipoActualizacion);
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (tipoListaAporte == 0) {
                aporteTablaSeleccionado.setTerceroRegistro(terceroSeleccionado);
                if (!listAportesCorreccionesCrear.contains(terceroSeleccionado)) {
                    if (listAportesCorreccionesModificar.isEmpty()) {
                        listAportesCorreccionesModificar.add(aporteTablaSeleccionado);
                    } else if (!listAportesCorreccionesModificar.contains(aporteTablaSeleccionado)) {
                        listAportesCorreccionesModificar.add(aporteTablaSeleccionado);
                    }
                }
            } else {
                aporteTablaSeleccionado.setTerceroRegistro(terceroSeleccionado);
                if (!listAportesCorreccionesCrear.contains(terceroSeleccionado)) {
                    if (listAportesCorreccionesModificar.isEmpty()) {
                        listAportesCorreccionesModificar.add(aporteTablaSeleccionado);
                    } else if (!listAportesCorreccionesModificar.contains(aporteTablaSeleccionado)) {
                        listAportesCorreccionesModificar.add(aporteTablaSeleccionado);
                    }

                }
            }
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            permitirIndexAporte = true;
            RequestContext.getCurrentInstance().update("form:tablaAportesCorrecciones");
        } else if (tipoActualizacion == 1) {
            nuevoAporteEntidad.setTerceroRegistro(terceroSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoNitTercero");
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevonombretercero");
        } else if (tipoActualizacion == 2) {
            duplicarAporteEntidad.setTerceroRegistro(terceroSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarNitTercero");
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarnombretercero");
        }
        filtrarLovTerceros = null;
        terceroSeleccionado = new Terceros();
        aceptar = true;
        tipoActualizacion = -1;

        context.reset("formularioLovTercero:lovTercero:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovTercero').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('TerceroDialogo').hide()");
        RequestContext.getCurrentInstance().update("formularioLovTercero:TerceroDialogo");
        RequestContext.getCurrentInstance().update("formularioLovTercero:lovTercero");
        RequestContext.getCurrentInstance().update("formularioLovTercero:aceptarT");
    }

    public void cancelarCambioTercero() {
        filtrarLovTerceros = null;
        terceroSeleccionado = new Terceros();
        aceptar = true;
        tipoActualizacion = -1;
        permitirIndexAporte = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioLovTercero:lovTercero:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovTercero').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('TerceroDialogo').hide()");
        RequestContext.getCurrentInstance().update("formularioLovTercero:TerceroDialogo");
        RequestContext.getCurrentInstance().update("formularioLovTercero:lovTercero");
        RequestContext.getCurrentInstance().update("formularioLovTercero:aceptarT");
    }

    public void actualizarTipoEntidad() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (tipoListaAporte == 0) {
                aporteTablaSeleccionado.setTipoentidad(tipoEntidadSeleccionado);
                if (listAportesCorreccionesModificar.isEmpty()) {
                    listAportesCorreccionesModificar.add(aporteTablaSeleccionado);
                } else if (!listAportesCorreccionesModificar.contains(aporteTablaSeleccionado)) {
                    listAportesCorreccionesModificar.add(aporteTablaSeleccionado);
                }
            } else {
                aporteTablaSeleccionado.setTipoentidad(tipoEntidadSeleccionado);
                if (listAportesCorreccionesModificar.isEmpty()) {
                    listAportesCorreccionesModificar.add(aporteTablaSeleccionado);
                } else if (!listAportesCorreccionesModificar.contains(aporteTablaSeleccionado)) {
                    listAportesCorreccionesModificar.add(aporteTablaSeleccionado);
                }
            }
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            permitirIndexAporte = true;
            RequestContext.getCurrentInstance().update("form:tablaAportesCorrecciones");
        } else if (tipoActualizacion == 1) {
            nuevoAporteEntidad.setTipoentidad(tipoEntidadSeleccionado);
            System.out.println("tipo entidad seleccionado : " + tipoEntidadSeleccionado);
            System.out.println("nuevoaporteempleado tipoentidad : " + nuevoAporteEntidad.getTipoentidad().getSecuencia());
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevotipoentidad");
        } else if (tipoActualizacion == 2) {
            duplicarAporteEntidad.setTipoentidad(tipoEntidadSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicartipoentidad");
        }
        filtrarLovTiposEntidades = null;
        tipoEntidadSeleccionado = new TiposEntidades();
        aceptar = true;
        tipoActualizacion = -1;

        context.reset("formularioLovTipoEntidad:lovTipoEntidad:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovTipoEntidad').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('TipoEntidadDialogo').hide()");
    }

    public void cancelarCambioTipoEntidad() {
        filtrarLovTiposEntidades = null;
        tipoEntidadSeleccionado = new TiposEntidades();
        aceptar = true;
        tipoActualizacion = -1;
        permitirIndexAporte = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioLovTipoEntidad:lovTipoEntidad:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovTipoEntidad').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('TipoEntidadDialogo').hide()");
    }

    public void dispararDialogoBuscar() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (guardado == true) {
            RequestContext.getCurrentInstance().update("formularioLovAporteEntidad:BuscarAporteDialogo");
            RequestContext.getCurrentInstance().execute("PF('BuscarAporteDialogo').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
        }
    }

    public void actualizarAporteEntidad() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (banderaAporte == 1) {
            desactivarFiltradoAporteEntidad();
            banderaAporte = 0;
            filtrarListaAportesCorrecciones = null;
            tipoListaAporte = 0;
        }
        listaAportesCorrecciones.clear();
        listaAportesCorrecciones.add(aporteEntidadlovSeleccionado);
        aporteTablaSeleccionado = listaAportesCorrecciones.get(0);
        filtrarLovAportesCorrecciones = null;
        aporteEntidadlovSeleccionado = new AportesCorrecciones();
        aceptar = true;
        tipoActualizacion = -1;
        visibilidadMostrarTodos = "visible";
        RequestContext.getCurrentInstance().update("form:mostrarTodos");
        modificarInfoRegistroParametro(listaParametrosCorrecciones.size());
        modificarInfoRegistroAporte(listaAportesCorrecciones.size());
        RequestContext.getCurrentInstance().update("form:tablaAportesCorrecciones");
        /*
         RequestContext.getCurrentInstance().update("formularioLovAporteEntidad:BuscarAporteDialogo");
         RequestContext.getCurrentInstance().update("formularioLovAporteEntidad:lovBuscarAporte");
         RequestContext.getCurrentInstance().update("formularioLovAporteEntidad:aceptarBA");*/
        context.reset("formularioLovAporteEntidad:lovBuscarAporte:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovBuscarAporte').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('BuscarAporteDialogo').hide()");
    }

    public void cancelarCambioAporteEntidad() {
        filtrarLovEmpresas = null;
        empresaSeleccionada = new Empresas();
        aceptar = true;
        RequestContext context = RequestContext.getCurrentInstance();
        tipoActualizacion = -1;
        permitirIndex = true;
        context.reset("formularioLovAporteEntidad:lovBuscarAporte:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovBuscarAporte').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('BuscarAporteDialogo').hide()");
    }

    public void mostrarTodosAporteEntidad() {
        RequestContext.getCurrentInstance().update("form:mostrarTodos");
        aporteTablaSeleccionado = null;
        if (banderaAporte == 1) {
            desactivarFiltradoAporteEntidad();
            banderaAporte = 0;
            filtrarListaAportesCorrecciones = null;
            tipoListaAporte = 0;
        }
        cargarDatosNuevos();
    }

    public void listaValoresBoton() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (cualTabla == 1) {
            if (parametroCorreccionSeleccionado != null) {
                if (cualCelda == 2) {
                    RequestContext.getCurrentInstance().update("formularioLovTipoTrabajador:TipoTrabajadorDialogo");
                    RequestContext.getCurrentInstance().execute("PF('TipoTrabajadorDialogo').show()");
                    tipoActualizacion = 0;
                }

                if (cualCelda == 3) {
                    RequestContext.getCurrentInstance().update("formularioLovEmpresa:EmpresaDialogo");
                    RequestContext.getCurrentInstance().execute("PF('EmpresaDialogo').show()");
                    tipoActualizacion = 0;
                }
            }
        } else if (cualTabla == 2) {

            if (aporteTablaSeleccionado != null) {
                if (cualCeldaAporte == 2) {
                    RequestContext.getCurrentInstance().update("formularioLovEmpleado:EmpleadoDialogo");
                    RequestContext.getCurrentInstance().execute("PF('EmpleadoDialogo').show()");
                    tipoActualizacion = 0;
                }
                if (cualCeldaAporte == 4) {
                    RequestContext.getCurrentInstance().update("formularioLovTercero:TerceroDialogo");
                    RequestContext.getCurrentInstance().execute("PF('TerceroDialogo').show()");
                    tipoActualizacion = 0;
                }
                if (cualCeldaAporte == 6) {
                    RequestContext.getCurrentInstance().update("formularioLovTipoEntidad:TipoEntidadDialogo");
                    RequestContext.getCurrentInstance().execute("PF('TipoEntidadDialogo').show()");
                    tipoActualizacion = 0;
                }
            }
        }

    }

    public void activarAceptar() {
        aceptar = false;
    }

    public String exportXMLTabla() {
        String tabla = "";
        if (cualTabla == 1) {
            if (parametroCorreccionSeleccionado != null) {
                tabla = ":formExportar:datosParametroAutoExportar";
            }
        } else if (cualTabla == 2) {
            if (aporteTablaSeleccionado != null) {
                tabla = ":formExportar:datosAporteEntidadExportar";
            }
        }
        return tabla;
    }

    public String exportXMLNombreArchivo() {
        String nombre = "";
        if (cualTabla == 1) {
            if (parametroCorreccionSeleccionado != null) {
                nombre = "ParametrosCorreccionAutoliquidacion_XML";
            }
        } else if (cualTabla == 2) {
            if (aporteTablaSeleccionado != null) {
                nombre = "AportesCorrecciones_XML";
            }
        }

        return nombre;
    }

    public void validarExportPDF() throws IOException {
        if (cualTabla == 1) {
            if (parametroCorreccionSeleccionado != null) {
                exportPDF();
            }
        } else if (cualTabla == 2) {
            if (aporteTablaSeleccionado != null) {
                exportPDF_AE();
            }
        }

    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosParametroAutoExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "ParametrosCorreccionAutoliquidacion_PDF", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportPDF_AE() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosAporteEntidadExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDFTablasAnchas();
        exporter.export(context, tabla, "AportesCorrecciones_PDF", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void validarExportXLS() throws IOException {
        if (cualTabla == 1) {
            if (parametroCorreccionSeleccionado != null) {
                exportXLS();
            }

        } else if (cualTabla == 2) {
            if (aporteTablaSeleccionado != null) {
                exportXLS_AE();
            }
        }

    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosParametroAutoExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "ParametrosCorreccionAutoliquidacion_XLS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS_AE() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosAporteEntidadExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "AportesCorrecciones_XLS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void eventoFiltrar() {
        if (parametroCorreccionSeleccionado != null) {
            if (tipoLista == 0) {
                tipoLista = 1;
            }
            modificarInfoRegistroParametro(filtrarListaParametrosCorrecciones.size());
        } else {
            modificarInfoRegistroParametro(0);
        }
    }

    public void eventoFiltrarAportes() {
        RequestContext context = RequestContext.getCurrentInstance();
        modificarInfoRegistroAporte(filtrarListaAportesCorrecciones.size());
    }

    public void verificarRastroTablas() {
        if (parametroCorreccionSeleccionado != null) {
            verificarRastro();
        }
        if (aporteTablaSeleccionado != null) {
            verificarRastroAporteEntidad();
        }
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (parametroCorreccionSeleccionado != null) {
            int resultado = administrarRastros.obtenerTabla(parametroCorreccionSeleccionado.getSecuencia(), "PARAMETROSCORRECCIONAUTOL");
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
        } else if (administrarRastros.verificarHistoricosTabla("PARAMETROSCORRECCIONAUTOL")) {
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
    }

    public void verificarRastroAporteEntidad() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (listaAportesCorrecciones != null) {
            if (aporteTablaSeleccionado != null) {
                int resultado = administrarRastros.obtenerTabla(aporteTablaSeleccionado.getSecuencia(), "APORTESENTIDADES");
                if (resultado == 1) {
                    RequestContext.getCurrentInstance().execute("PF('errorObjetosDBAporte').show()");
                } else if (resultado == 2) {
                    RequestContext.getCurrentInstance().execute("PF('confirmarRastroAporte').show()");
                } else if (resultado == 3) {
                    RequestContext.getCurrentInstance().execute("PF('errorRegistroRastro').show()");
                } else if (resultado == 4) {
                    RequestContext.getCurrentInstance().execute("PF('errorTablaConRastro').show()");
                } else if (resultado == 5) {
                    RequestContext.getCurrentInstance().execute("PF('errorTablaSinRastro').show()");
                }
            } else {
                RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
            }
        } else if (administrarRastros.verificarHistoricosTabla("APORTESENTIDADES")) {
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistoricoAporte').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
    }

    public void recordarSeleccion() {
        if (parametroCorreccionSeleccionado != null) {
            FacesContext c = FacesContext.getCurrentInstance();
            tablaC = (DataTable) c.getViewRoot().findComponent("form:datosParametroAuto");
            tablaC.setSelection(parametroCorreccionSeleccionado);
        }
    }

    public void modificarInfoRegistroEmpresa(int valor) {
        infoRegistroEmpresa = String.valueOf(valor);
        RequestContext.getCurrentInstance().update("formularioLovEmpresa:infoRegistroEmpresa");
    }

    public void modificarInfoRegistroEmpleados(int valor) {
        infoRegistroEmpleado = String.valueOf(valor);
        RequestContext.getCurrentInstance().update("formularioLovEmpleado:infoRegistroEmpleado");
    }

    public void modificarInfoRegistroTercero(int valor) {
        infoRegistroTercero = String.valueOf(valor);
        RequestContext.getCurrentInstance().update("formularioLovTercero:infoRegistroTercero");
    }

    public void modificarInfoRegistroTiposTrabajadores(int valor) {
        infoRegistroTipoTrabajador = String.valueOf(valor);
        RequestContext.getCurrentInstance().update("formularioLovTipoTrabajador:infoRegistroTipoTrabajador");
    }

    public void modificarInfoRegistroTiposEntidades(int valor) {
        infoRegistroTipoEntidad = String.valueOf(valor);
        RequestContext.getCurrentInstance().update("formularioLovTipoEntidad:infoRegistroTipoEntidad");
    }

    public void modificarInfoRegistroAportesCorrecciones(int valor) {
        infoRegistroAporteEntidad = String.valueOf(valor);
        RequestContext.getCurrentInstance().update("formularioLovAporteEntidad:infoRegistroAporteEntidad");
    }

    public void modificarInfoRegistroParametro(int valor) {
        infoRegistroParametro = String.valueOf(valor);
        RequestContext.getCurrentInstance().update("form:infoRegistroParametro");
    }

    public void modificarInfoRegistroAporte(int valor) {
        infoRegistroAporte = String.valueOf(valor);
        RequestContext.getCurrentInstance().update("form:infoRegistroAporte");
    }

    public void eventoFiltrarLovEmpresas() {
        modificarInfoRegistroEmpresa(filtrarLovEmpresas.size());
    }

    public void eventoFiltrarLovTerceros() {
        modificarInfoRegistroTercero(filtrarLovTerceros.size());
    }

    public void eventoFiltrarLovTipoEntidades() {
        modificarInfoRegistroTiposEntidades(filtrarLovTiposEntidades.size());
    }

    public void eventoFiltrarLovTipoTrabajador() {
        modificarInfoRegistroTiposTrabajadores(filtrarLovTiposTrabajadores.size());
    }

    public void eventoFiltrarEmpleados() {
        modificarInfoRegistroEmpleados(filtrarLovEmpleados.size());
    }

    public void eventoFiltrarAporteEntidad() {
        modificarInfoRegistroAportesCorrecciones(filtrarLovAportesCorrecciones.size());
    }

    public void contarRegistrosAporte() {
        if (listaAportesCorrecciones != null) {
            if (!listaAportesCorrecciones.isEmpty()) {
                modificarInfoRegistroAporte(listaAportesCorrecciones.size());
            }
        } else {
            modificarInfoRegistroAporte(0);
        }
    }

    public void contarRegistrosParametros() {
        if (listaParametrosCorrecciones != null) {
            if (!listaParametrosCorrecciones.isEmpty()) {
                modificarInfoRegistroParametro(listaParametrosCorrecciones.size());
            }
        } else {
            modificarInfoRegistroParametro(0);
        }
    }

    //////////////////////GETS Y SETS//////////////////////////////////////////////////
    public List<ParametrosCorreccionesAutoL> getListaParametrosCorrecciones() {
        try {
            if (listaParametrosCorrecciones == null) {
                listaParametrosCorrecciones = administrarParametroCorreccionAutoL.consultarParametrosCorreccionesAutoliq();
                if (listaParametrosCorrecciones != null) {
                    for (int i = 0; i < listaParametrosCorrecciones.size(); i++) {
                        if (listaParametrosCorrecciones.get(i).getTipotrabajador() == null) {
                            listaParametrosCorrecciones.get(i).setTipotrabajador(new TiposTrabajadores());
                        }
                    }
                }
            }
            return listaParametrosCorrecciones;
        } catch (Exception e) {
            System.out.println("Error getListaParametrosCorrecciones");
            return null;
        }

    }

    public void setListaParametrosCorrecciones(List<ParametrosCorreccionesAutoL> listaParametrosCorrecciones) {
        this.listaParametrosCorrecciones = listaParametrosCorrecciones;
    }

    public List<ParametrosCorreccionesAutoL> getFiltrarListaParametrosCorrecciones() {
        return filtrarListaParametrosCorrecciones;
    }

    public void setFiltrarListaParametrosCorrecciones(List<ParametrosCorreccionesAutoL> filtrarListaParametrosCorrecciones) {
        this.filtrarListaParametrosCorrecciones = filtrarListaParametrosCorrecciones;
    }

    public ParametrosCorreccionesAutoL getParametroCorreccionSeleccionado() {
        return parametroCorreccionSeleccionado;
    }

    public void setParametroCorreccionSeleccionado(ParametrosCorreccionesAutoL parametroCorreccionSeleccionado) {
        this.parametroCorreccionSeleccionado = parametroCorreccionSeleccionado;
    }

    public ParametrosCorreccionesAutoL getNuevoParametro() {
        return nuevoParametro;
    }

    public void setNuevoParametro(ParametrosCorreccionesAutoL nuevoParametro) {
        this.nuevoParametro = nuevoParametro;
    }

    public ParametrosCorreccionesAutoL getDuplicarParametro() {
        return duplicarParametro;
    }

    public void setDuplicarParametro(ParametrosCorreccionesAutoL duplicarParametro) {
        this.duplicarParametro = duplicarParametro;
    }

    public List<AportesCorrecciones> getListaAportesCorrecciones() {
        try {
            if (listaAportesCorrecciones == null) {
                if (parametroCorreccionSeleccionado != null) {
                    listaAportesCorrecciones = administrarParametroCorreccionAutoL.consultarAportesCorrecciones();
                    if (listaAportesCorrecciones != null) {
                        for (int i = 0; i < listaAportesCorrecciones.size(); i++) {
                            if (listaAportesCorrecciones.get(i).getTerceroRegistro() == null) {
                                listaAportesCorrecciones.get(i).setTerceroRegistro(null);

                            }
                        }
                    }
                }
            }
            return listaAportesCorrecciones;
        } catch (Exception e) {
            System.out.println("Error !!!!!!!!!!! getListaAportesCorrecciones : " + e.toString());
            return null;
        }
    }

    public void setListaAportesCorrecciones(List<AportesCorrecciones> listaAportesCorrecciones) {
        this.listaAportesCorrecciones = listaAportesCorrecciones;
    }

    public List<AportesCorrecciones> getFiltrarListaAportesCorrecciones() {
        return filtrarListaAportesCorrecciones;
    }

    public void setFiltrarListaAportesCorrecciones(List<AportesCorrecciones> filtrarListaAportesCorrecciones) {
        this.filtrarListaAportesCorrecciones = filtrarListaAportesCorrecciones;
    }

    public AportesCorrecciones getAporteTablaSeleccionado() {
        return aporteTablaSeleccionado;
    }

    public void setAporteTablaSeleccionado(AportesCorrecciones aporteTablaSeleccionado) {
        this.aporteTablaSeleccionado = aporteTablaSeleccionado;
    }

    public AportesCorrecciones getNuevoAporteEntidad() {
        return nuevoAporteEntidad;
    }

    public void setNuevoAporteEntidad(AportesCorrecciones nuevoAporteEntidad) {
        this.nuevoAporteEntidad = nuevoAporteEntidad;
    }

    public AportesCorrecciones getDuplicarAporteEntidad() {
        return duplicarAporteEntidad;
    }

    public void setDuplicarAporteEntidad(AportesCorrecciones duplicarAporteEntidad) {
        this.duplicarAporteEntidad = duplicarAporteEntidad;
    }

    public boolean isDisabledBuscar() {
        return disabledBuscar;
    }

    public void setDisabledBuscar(boolean disabledBuscar) {
        this.disabledBuscar = disabledBuscar;
    }

    public boolean isGuardado() {
        return guardado;
    }

    public void setGuardado(boolean guardado) {
        this.guardado = guardado;
    }

    public String getAltoTablaAporte() {
        return altoTablaAporte;
    }

    public void setAltoTablaAporte(String altoTablaAporte) {
        this.altoTablaAporte = altoTablaAporte;
    }

    public String getPaginaAnterior() {
        return paginaAnterior;
    }

    public void setPaginaAnterior(String paginaAnterior) {
        this.paginaAnterior = paginaAnterior;
    }

    public String getInfoRegistroParametro() {
        return infoRegistroParametro;
    }

    public void setInfoRegistroParametro(String infoRegistroParametro) {
        this.infoRegistroParametro = infoRegistroParametro;
    }

    public String getInfoRegistroAporte() {
        return infoRegistroAporte;
    }

    public void setInfoRegistroAporte(String infoRegistroAporte) {
        this.infoRegistroAporte = infoRegistroAporte;
    }

    public List<TiposTrabajadores> getLovTiposTrabajadores() {
        if (lovTiposTrabajadores == null) {
            lovTiposTrabajadores = administrarParametroCorreccionAutoL.lovTiposTrabajadores();
        }
        return lovTiposTrabajadores;
    }

    public void setLovTiposTrabajadores(List<TiposTrabajadores> lovTiposTrabajadores) {
        this.lovTiposTrabajadores = lovTiposTrabajadores;
    }

    public List<TiposTrabajadores> getFiltrarLovTiposTrabajadores() {
        return filtrarLovTiposTrabajadores;
    }

    public void setFiltrarLovTiposTrabajadores(List<TiposTrabajadores> filtrarLovTiposTrabajadores) {
        this.filtrarLovTiposTrabajadores = filtrarLovTiposTrabajadores;
    }

    public TiposTrabajadores getTipoTrabajadorSeleccionado() {
        return tipoTrabajadorSeleccionado;
    }

    public void setTipoTrabajadorSeleccionado(TiposTrabajadores tipoTrabajadorSeleccionado) {
        this.tipoTrabajadorSeleccionado = tipoTrabajadorSeleccionado;
    }

    public String getInfoRegistroTipoTrabajador() {
        return infoRegistroTipoTrabajador;
    }

    public void setInfoRegistroTipoTrabajador(String infoRegistroTipoTrabajador) {
        this.infoRegistroTipoTrabajador = infoRegistroTipoTrabajador;
    }

    public List<Empresas> getLovEmpresas() {
        if (lovEmpresas == null) {
            lovEmpresas = administrarParametroCorreccionAutoL.lovEmpresas();
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

    public Empresas getEmpresaSeleccionada() {
        return empresaSeleccionada;
    }

    public void setEmpresaSeleccionada(Empresas empresaSeleccionada) {
        this.empresaSeleccionada = empresaSeleccionada;
    }

    public String getInfoRegistroEmpresa() {
        return infoRegistroEmpresa;
    }

    public void setInfoRegistroEmpresa(String infoRegistroEmpresa) {
        this.infoRegistroEmpresa = infoRegistroEmpresa;
    }

    public List<Empleados> getLovEmpleados() {
        if (lovEmpleados == null) {
            lovEmpleados = administrarParametroCorreccionAutoL.lovEmpleados();
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

    public Empleados getEmpleadoSeleccionado() {
        return empleadoSeleccionado;
    }

    public void setEmpleadoSeleccionado(Empleados empleadoSeleccionado) {
        this.empleadoSeleccionado = empleadoSeleccionado;
    }

    public String getInfoRegistroEmpleado() {
        return infoRegistroEmpleado;
    }

    public void setInfoRegistroEmpleado(String infoRegistroEmpleado) {
        this.infoRegistroEmpleado = infoRegistroEmpleado;
    }

    public List<Terceros> getLovTerceros() {
        if (lovTerceros == null) {
            lovTerceros = administrarParametroCorreccionAutoL.lovTerceros();
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

    public Terceros getTerceroSeleccionado() {
        return terceroSeleccionado;
    }

    public void setTerceroSeleccionado(Terceros terceroSeleccionado) {
        this.terceroSeleccionado = terceroSeleccionado;
    }

    public String getInfoRegistroTercero() {
        return infoRegistroTercero;
    }

    public void setInfoRegistroTercero(String infoRegistroTercero) {
        this.infoRegistroTercero = infoRegistroTercero;
    }

    public List<TiposEntidades> getLovTiposEntidades() {
        if (lovTiposEntidades == null) {
            lovTiposEntidades = administrarParametroCorreccionAutoL.lovTiposEntidades();
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

    public TiposEntidades getTipoEntidadSeleccionado() {
        return tipoEntidadSeleccionado;
    }

    public void setTipoEntidadSeleccionado(TiposEntidades tipoEntidadSeleccionado) {
        this.tipoEntidadSeleccionado = tipoEntidadSeleccionado;
    }

    public String getInfoRegistroTipoEntidad() {
        return infoRegistroTipoEntidad;
    }

    public void setInfoRegistroTipoEntidad(String infoRegistroTipoEntidad) {
        this.infoRegistroTipoEntidad = infoRegistroTipoEntidad;
    }

    public List<AportesCorrecciones> getLovAportesCorrecciones() {
        if (lovAportesCorrecciones == null) {
            if (parametroCorreccionSeleccionado != null) {
                lovAportesCorrecciones = administrarParametroCorreccionAutoL.consultarLovAportesCorrecciones();
                if (lovAportesCorrecciones != null) {
                    for (int i = 0; i < lovAportesCorrecciones.size(); i++) {
                        if (lovAportesCorrecciones.get(i).getTercero() == null) {
                            lovAportesCorrecciones.get(i).setTerceroRegistro(new Terceros());
                        }
                    }
                }
            }
        }
        return lovAportesCorrecciones;
    }

    public void setLovAportesCorrecciones(List<AportesCorrecciones> lovAportesCorrecciones) {
        this.lovAportesCorrecciones = lovAportesCorrecciones;
    }

    public List<AportesCorrecciones> getFiltrarLovAportesCorrecciones() {
        return filtrarLovAportesCorrecciones;
    }

    public void setFiltrarLovAportesCorrecciones(List<AportesCorrecciones> filtrarLovAportesCorrecciones) {
        this.filtrarLovAportesCorrecciones = filtrarLovAportesCorrecciones;
    }

    public AportesCorrecciones getAporteEntidadlovSeleccionado() {
        return aporteEntidadlovSeleccionado;
    }

    public void setAporteEntidadlovSeleccionado(AportesCorrecciones aporteEntidadlovSeleccionado) {
        this.aporteEntidadlovSeleccionado = aporteEntidadlovSeleccionado;
    }

    public String getInfoRegistroAporteEntidad() {
        return infoRegistroAporteEntidad;
    }

    public void setInfoRegistroAporteEntidad(String infoRegistroAporteEntidad) {
        this.infoRegistroAporteEntidad = infoRegistroAporteEntidad;
    }

    public String getVisibilidadMostrarTodos() {
        return visibilidadMostrarTodos;
    }

    public void setVisibilidadMostrarTodos(String visibilidadMostrarTodos) {
        this.visibilidadMostrarTodos = visibilidadMostrarTodos;
    }

    public String getAltoTabla() {
        return altoTabla;
    }

    public void setAltoTabla(String altoTabla) {
        this.altoTabla = altoTabla;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public void setAceptar(boolean aceptar) {
        this.aceptar = aceptar;
    }

    public ParametrosEstructuras getParametroEstructura() {
        getUsuario();
        if (usuario.getAlias() != null) {
            parametroEstructura = administrarParametroCorreccionAutoL.buscarParametroEstructura(usuario.getAlias());
        }
        return parametroEstructura;
    }

    public void setParametroEstructura(ParametrosEstructuras parametroEstructura) {
        this.parametroEstructura = parametroEstructura;
    }

    public ParametrosInformes getParametroInforme() {
        getUsuario();
        parametroInforme = administrarParametroCorreccionAutoL.buscarParametroInforme(usuario.getAlias());
        return parametroInforme;
    }

    public void setParametroInforme(ParametrosInformes parametroInforme) {
        this.parametroInforme = parametroInforme;
    }

    public ActualUsuario getUsuario() {
        if (usuario == null) {
            usuario = administrarParametroCorreccionAutoL.obtenerActualUsuario();
        }
        return usuario;
    }

    public void setUsuario(ActualUsuario usuario) {
        this.usuario = usuario;
    }

    public ParametrosCorreccionesAutoL getEditarParametro() {
        return editarParametro;
    }

    public void setEditarParametro(ParametrosCorreccionesAutoL editarParametro) {
        this.editarParametro = editarParametro;
    }

    public AportesCorrecciones getEditarAporteEntidad() {
        return editarAporteEntidad;
    }

    public void setEditarAporteEntidad(AportesCorrecciones editarAporteEntidad) {
        this.editarAporteEntidad = editarAporteEntidad;
    }

}
